package org.mskcc.cmo.messaging;

import io.nats.client.Connection;
import io.nats.client.Nats;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NATSConnection {

    @Value("${nats.connection_url}")
    private String natsConnectionUrl;

    @Bean
    public Connection natsConnection() throws IOException, InterruptedException {
        Connection natsConnection = Nats.connect(natsConnectionUrl);
        return natsConnection;
    }

}
