package org.mskcc.cmo.messaging.impl;

import com.google.gson.Gson;
import io.nats.streaming.Message;
import io.nats.streaming.MessageHandler;
import io.nats.streaming.NatsStreaming;
import io.nats.streaming.Options;
import io.nats.streaming.StreamingConnection;
import io.nats.streaming.StreamingConnectionFactory;
import io.nats.streaming.Subscription;
import io.nats.streaming.SubscriptionOptions;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.mskcc.cmo.messaging.Gateway;
import org.mskcc.cmo.messaging.MessageConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NATSGatewayImpl implements Gateway {

    @Value("${nats.clusterid}")
    private String clusterID;

    @Value("${nats.clientid}")
    private String clientID;

    @Value("${nats.url}")
    private String natsURL;

    private static StreamingConnection stanConnection;

    private static final Gson gson = new Gson();
    private static final Map<String, Subscription> subscribers = new HashMap<String, Subscription>();

    private static boolean initialized = false;
    private static volatile boolean shutdownInitiated;
    private static final ExecutorService exec = Executors.newSingleThreadExecutor();
    private static final CountDownLatch publishingShutdownLatch = new CountDownLatch(1);
    private static final BlockingQueue<PublishingQueueTask> publishingQueue =
        new LinkedBlockingQueue<PublishingQueueTask>();

    private class PublishingQueueTask {
        String topic;
        Object message;

        PublishingQueueTask(String topic, Object message) {
            this.topic = topic;
            this.message = message;
        }
    }

    private class NATSPublisher implements Runnable {

        StreamingConnection sc;
        boolean interrupted = false;

        NATSPublisher() throws Exception {
            Options opts = new Options.Builder().natsConn(stanConnection.getNatsConnection()).build();
            this.sc = NatsStreaming.connect(clusterID, clientID + "-publisher", opts);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    PublishingQueueTask task = publishingQueue.poll(100, TimeUnit.MILLISECONDS);
                    if (task != null) {
                        String msg = gson.toJson(task.message);
                        sc.publish(task.topic, msg.getBytes(StandardCharsets.UTF_8));
                    }
                    if (interrupted && publishingQueue.isEmpty()) {
                        break;
                    }
                } catch (InterruptedException e) {
                    interrupted = true;
                } catch (Exception e) {
                    // TBD requeue?
                    System.err.printf("Error during publishing: %s\n", e.getMessage());
                }
            }
            try {
                sc.close();
                publishingShutdownLatch.countDown();
            } catch (Exception e) {
                System.err.printf("Error closing streaming connection: %s\n", e.getMessage());
            }
        }
    }

    @Override
    public void publish(String topic, Object message) throws Exception {
        if (!initialized) {
            throw new IllegalStateException("Gateway has not been initialized");
        }
        if (!shutdownInitiated) {
            PublishingQueueTask task = new PublishingQueueTask(topic, message);
            publishingQueue.put(task);
        } else {
            System.err.printf("Shutdown initiated, not accepting publish request: %s\n", message);
            throw new IllegalStateException("Shutdown initiated, not accepting anymore publish requests");
        }
    }

    @Override
    public void initialize() throws Exception {
        StreamingConnectionFactory cf = new StreamingConnectionFactory(clusterID, clientID);
        cf.setNatsUrl(natsURL);
        stanConnection = cf.createConnection();
        exec.execute(new NATSPublisher());
        initialized = true;
    }

    @Override
    public void subscribe(String topic, Class messageClass, MessageConsumer consumer) throws Exception {
        if (!initialized) {
            throw new IllegalStateException("Gateway has not been initialized");
        }
        // we may want to change this check -
        // to allow for more than one consumer per topic
        if (!subscribers.containsKey(topic)) {
            Subscription sub = stanConnection.subscribe(topic, new MessageHandler() {
                @Override
                public void onMessage(Message m) {
                    String json = new String(m.getData(), StandardCharsets.UTF_8);
                    Object message = gson.fromJson(json, messageClass);
                    consumer.onMessage(message);
                }
            }, new SubscriptionOptions.Builder().durableName(topic + "-" + clientID).build());
            subscribers.put(topic, sub);
        }
    }

    @Override
    public void shutdown() throws Exception {
        if (!initialized) {
            throw new IllegalStateException("Gateway has not been initialized");
        }
        exec.shutdownNow();
        shutdownInitiated = true;
        publishingShutdownLatch.await();
        stanConnection.close();
    }
}
