package org.mskcc.cmo.messaging.utils;

import javax.net.ssl.SSLContext;

public interface SSLUtils {

    public SSLContext createSSLContext() throws Exception;
}
