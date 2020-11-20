package org.mskcc.cmo.messaging.impl;

import org.mskcc.cmo.messaging.Gateway;
import org.mskcc.cmo.messaging.MessageConsumer;

import com.google.gson.Gson;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Message;
import io.nats.client.MessageHandler;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NATSGatewayImpl implements Gateway {
    @Value("${nats.flush_duration:5}")
    private Integer natsFlushDuration;

    @Autowired
    private Connection natsConnection;

    private Gson gson;
    private Map<String, Dispatcher> dispatchers;
    private boolean initialized;


    public NATSGatewayImpl(Connection natsConnection) {
        this.natsConnection = natsConnection;
        this.gson = new Gson();
        this.dispatchers = new HashMap<>();
        this.initialized = (natsConnection != null);
    }

    public NATSGatewayImpl() {
        this.gson = new Gson();
        this.dispatchers = new HashMap<>();
        this.initialized = (natsConnection != null);
    }

    @Override
    public void publish(String topic, Object message) throws Exception {
        if (!initialized) return;
        String msg = gson.toJson(message);
        natsConnection.publish(topic, msg.getBytes(StandardCharsets.UTF_8));
        natsConnection.flush(Duration.ofSeconds(natsFlushDuration));
    }

    @Override
    public void subscribe(String topic, Class messageClass, MessageConsumer consumer) throws Exception {
        if (!initialized) return;
        if (!dispatchers.containsKey(topic)) {
            Dispatcher d = natsConnection.createDispatcher(new MessageHandler() {
                @Override
                public void onMessage(Message msg) throws InterruptedException {
                    String json = new String(msg.getData(), StandardCharsets.UTF_8);
                    Object message = gson.fromJson(json, messageClass);
                    consumer.onMessage(message);
                }
            });
            d.subscribe(topic);
            dispatchers.put(topic, d);
        }
    }

    @Override
    public Object request(String topic, Class messageClass) throws Exception {
        if (!initialized) return null;
        Message msg = natsConnection.request(topic, null, Duration.ofSeconds(natsFlushDuration));
        String json = new String(msg.getData(), StandardCharsets.UTF_8);
        return gson.fromJson(json, messageClass);
    }

    @Override
    public void shutdown() throws Exception {
        if (initialized) {
            natsConnection.close();
        }
    }

}
