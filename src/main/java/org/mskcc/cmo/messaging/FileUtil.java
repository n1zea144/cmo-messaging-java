package org.mskcc.cmo.messaging;

import java.io.IOException;

public interface FileUtil {
    void savePublishFailureMessage(String topic, String message) throws IOException;
}
