package org.mskcc.cmo.messaging;

public interface MessageConsumer
{
    void onMessage(Object message);
}
