package org.mskcc.cmo.messaging.utils.impl;

import io.nats.client.Options;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import org.mskcc.cmo.messaging.utils.SSLUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SSLUtilsImpl implements SSLUtils {

    @Value("${nats.keystore_path}")
    private String keystorePath;

    @Value("${nats.truststore_path}")
    private String truststorePath;

    @Value("${nats.key_password}")
    private String keyPassword;

    @Value("${nats.store_password}")
    private String storePassword;

    @Value("${nats.ssl_algorithm:SunX509}")
    private String sslAlgorithm;

    @Override
    public SSLContext createSSLContext() throws Exception {
        SSLContext ctx = SSLContext.getInstance(Options.DEFAULT_SSL_PROTOCOL);
        ctx.init(createKeyManagers(), createTrustManagers(), new SecureRandom());
        return ctx;
    }

    private KeyManager[] createKeyManagers() throws Exception {
        KeyStore store = loadKeystore(keystorePath);
        KeyManagerFactory factory = KeyManagerFactory.getInstance(sslAlgorithm);
        factory.init(store, keyPassword.toCharArray());
        return factory.getKeyManagers();
    }

    private TrustManager[] createTrustManagers() throws Exception {
        KeyStore store = loadKeystore(truststorePath);
        TrustManagerFactory factory = TrustManagerFactory.getInstance(sslAlgorithm);
        factory.init(store);
        return factory.getTrustManagers();
    }

    private KeyStore loadKeystore(String path) throws Exception {
        KeyStore store = KeyStore.getInstance("JKS");
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(path));

        try {
            store.load(in, storePassword.toCharArray());
        } finally {
            if (in != null) {
                in.close();
            }
        }

        return store;
    }
}
