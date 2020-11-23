package org.mskcc.cmo.messaging.impl;

import org.mskcc.cmo.messaging.MessageConsumer;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;


public class MessageConsumerImpl implements MessageConsumer {

    private final Log logger = LogFactory.getLog(MessageConsumerImpl.class);

    @Override
    public void onMessage(Object message) {
        logger.info("Received message: " + message.toString());
    }

}
