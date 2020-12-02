package org.mskcc.cmo.messaging;

import io.nats.streaming.MessageHandler;

public interface Gateway {
	
    void publish(String topic, Object message) throws Exception;
    void subscribe(String topic, MessageHandler messageHandler) throws Exception;
    void shutdown() throws Exception;
    
}
