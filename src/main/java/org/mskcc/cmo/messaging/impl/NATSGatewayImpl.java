package org.mskcc.cmo.messaging.impl;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.mskcc.cmo.messaging.Gateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;
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

    public NATSGatewayImpl() {
        this.gson = new Gson();
        this.subscribers = new HashMap<>();
    }

    @Override
    public void publish(String topic, Object message) throws Exception {
        if (stanConnection == null) return;
        String msg = gson.toJson(message);
        stanConnection.publish(topic, msg.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void subscribe(String topic, MessageHandler messageHandler) throws Exception {
    	if (stanConnection == null) return;
        if (!subscribers.containsKey(topic)) {
        	Subscription sub = stanConnection.subscribe(topic, messageHandler
        			, new SubscriptionOptions.Builder().durableName(durableName).build());
        	
            subscribers.put(topic, sub);
        }
    }

    @Override
    public void shutdown() throws Exception {
        if (stanConnection != null) {
            stanConnection.close();
        }
    }

}
