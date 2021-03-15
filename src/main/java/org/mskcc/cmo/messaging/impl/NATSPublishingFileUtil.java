package org.mskcc.cmo.messaging.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.mskcc.cmo.messaging.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NATSPublishingFileUtil implements FileUtil {
    @Value("${metadb.publishing_failures_filepath}")
    private String filePath;

    public boolean exists() {
        File f = new File(filePath);
        return f.exists();
    }

    @Override
    public void savePublishFailureMessage(String topic, String message) throws IOException {
        if (!exists()) {
            File f = new File(filePath);
            f.createNewFile();
        }
        BufferedWriter publishFailureFile = new BufferedWriter(new FileWriter(filePath, true));
        publishFailureFile.write(generatePublishFailureRecord(topic, message));
        publishFailureFile.close();
    }

    /**
     * Generates record to write to publishing failure file.
     * @param topic
     * @param message
     * @return String
     */
    private String generatePublishFailureRecord(String topic, String message) {
        String currentDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        StringBuilder builder = new StringBuilder();
        builder.append(currentDate)
                .append("\t")
                .append(topic)
                .append("\t")
                .append(message)
                .append("\n");
        return builder.toString();
    }
}
