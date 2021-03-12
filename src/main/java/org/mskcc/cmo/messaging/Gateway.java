package org.mskcc.cmo.messaging;

public interface Gateway {
    void connect() throws Exception;
    void connect(String clusterId, String clientId, String natsUrl) throws Exception;
    boolean isConnected();
    void publish(String topic, Object message) throws Exception;
    void subscribe(String topic, Class messageClass, MessageConsumer messageConsumer) throws Exception;
    void shutdown() throws Exception;
}
