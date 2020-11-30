package org.mskcc.cmo.messaging.impl;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.mskcc.cmo.messaging.Gateway;
import org.mskcc.cmo.messaging.MessageConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;
import io.nats.streaming.Message;
import io.nats.streaming.MessageHandler;
import io.nats.streaming.StreamingConnection;
import io.nats.streaming.Subscription;
import io.nats.streaming.SubscriptionOptions;

@Component
public class NATSGatewayImpl implements Gateway {
	
	@Value("${nats.flush_duration:5}")
	private Integer natsFlushDuration;
	
	@Value("${nats.durable_name}")
	private String durableName;
    
    @Autowired
    private StreamingConnection stanConnection;

    private Gson gson;
    private Map<String, Subscription> subscribers;
    private boolean initialized;


    public NATSGatewayImpl(StreamingConnection natsConnection) {
        this.stanConnection = stanConnection;
        this.gson = new Gson();
        this.subscribers = new HashMap<>();
        this.initialized = (natsConnection != null);
    }

    public NATSGatewayImpl() {
        this.gson = new Gson();
        this.subscribers = new HashMap<>();
        this.initialized = (stanConnection != null);
    }

    @Override
    public void publish(String topic, Object message) throws Exception {
        if (!initialized) return;
        String msg = gson.toJson(message);
        stanConnection.publish(topic, msg.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void subscribe(String topic, Class messageClass, MessageConsumer consumer) throws Exception {
    	if (!initialized) return;
        if (!subscribers.containsKey(topic)) {
        	final CountDownLatch doneSignal = new CountDownLatch(10);
        	Subscription sub = stanConnection.subscribe(topic, new MessageHandler() {
        		public void onMessage(Message m) {
                	String json = new String(m.getData(), StandardCharsets.UTF_8);
                    Object message = gson.fromJson(json, messageClass);
                    consumer.onMessage(message);
                    doneSignal.countDown();
                }
             }, new SubscriptionOptions.Builder().durableName(durableName).build());

            subscribers.put(topic, sub);
            doneSignal.await();
        }
    }

    @Override
    public void shutdown() throws Exception {
        if (initialized) {
            stanConnection.close();
        }
    }

}
