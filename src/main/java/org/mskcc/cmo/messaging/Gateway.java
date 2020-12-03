package org.mskcc.cmo.messaging;

public interface Gateway {

    void initialize() throws Exception;

    void publish(String topic, Object message) throws Exception;

    void subscribe(String topic, Class messageClass, MessageConsumer messageConsumer) throws Exception;

    void shutdown() throws Exception;

}
