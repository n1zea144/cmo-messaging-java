package org.mskcc.cmo.messaging.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mskcc.cmo.messaging.MessageConsumer;

public class MessageConsumerImpl implements MessageConsumer {

	private final Log LOG = LogFactory.getLog(MessageConsumerImpl.class);

	@Override
	public void onMessage(Object message) {
		LOG.info("Received message: " + message.toString());
	}

}
