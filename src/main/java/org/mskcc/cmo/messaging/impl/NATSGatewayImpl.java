package org.mskcc.cmo.messaging.impl;

import org.mskcc.cmo.messaging.Gateway;
import org.mskcc.cmo.messaging.MessageConsumer;

import io.nats.client.Nats;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import java.util.Map;
import java.util.HashMap;
import java.time.Duration;
import java.nio.charset.StandardCharsets;

@Component("NATSGateway")
public class NATSGatewayImpl implements Gateway
{
    Gson gson;
    Connection natsConnection;
    Map<String,Dispatcher> dispatchers;
    boolean initialized;

    public NATSGatewayImpl() throws Exception
    {
        gson = new Gson();
        // server and other config should be injected via properties
        natsConnection = Nats.connect("");
        dispatchers = new HashMap<String, Dispatcher>();
        initialized = true;
    }

    @Override
    public void shutdown() throws Exception
    {
        if (initialized) {
            natsConnection.close();
        }
    }

    @Override
    public void publish(String topic, Object message) throws Exception
    {
        if (!initialized) return;
        String msg = gson.toJson(message);
        natsConnection.publish(topic, msg.getBytes(StandardCharsets.UTF_8));
        natsConnection.flush(Duration.ofSeconds(5));
    }

    @Override
    public void subscribe(String topic, Class messageClass, MessageConsumer consumer) throws Exception
    {
        if (!initialized) return;

        if (!dispatchers.containsKey(topic)) {
            Dispatcher d = natsConnection.createDispatcher((msg) -> {
                    Gson gson = new Gson();
                    String json = new String (msg.getData(), StandardCharsets.UTF_8);
                    Object message = gson.fromJson(json, messageClass);
                    consumer.onMessage(message);
                });
            d.subscribe(topic);
            dispatchers.put(topic, d);
        }
    }

    @Override
    public Object request(String topic, Class messageClass) throws Exception
    {
        return null;
    }
}
