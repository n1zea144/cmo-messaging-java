package org.mskcc.cmo.messaging;

import io.nats.streaming.StreamingConnection;
import io.nats.streaming.StreamingConnectionFactory;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NATSConnection {
    
    @Value("${nats.clusterid}")
    private String clusterID;
    
    @Value("${nats.clientid}")
    private String clientID;
    
    
    @Bean
    public StreamingConnection stanConnection() throws IOException, InterruptedException {
    	StreamingConnectionFactory cf = new StreamingConnectionFactory(clusterID, clientID);
    	StreamingConnection sc = cf.createConnection();
        return sc;
    }

}
