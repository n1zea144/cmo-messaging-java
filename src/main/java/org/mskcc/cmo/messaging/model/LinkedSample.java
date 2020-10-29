package org.mskcc.cmo.messaging.model;

import java.io.Serializable;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

/**
 * Node entity representing the linked sample entity from an external system.
 * @author ochoaa
 */
@NodeEntity
public class LinkedSample implements Serializable {
    @Id @GeneratedValue
    private Long id;
    private String linkedSampleName;
    private String linkedSystemName;
    @Relationship(type="SP_TO_SP", direction=Relationship.OUTGOING)
    private SampleMetadataEntity sampleMetadata;

    public LinkedSample() {}

    public LinkedSample(String linkedSampleName, String linkedSystemName) {
        this.linkedSampleName = linkedSampleName;
        this.linkedSystemName = linkedSystemName;
    }

    public String getLinkedSampleName() {
        return linkedSampleName;
    }

    public void setLinkedSampleName(String linkedSampleName) {
        this.linkedSampleName = linkedSampleName;
    }

    public String getLinkedSystemName() {
        return linkedSystemName;
    }

    public void setLinkedSystemName(String linkedSystemName) {
        this.linkedSystemName = linkedSystemName;
    }

    public SampleMetadataEntity getSampleMetadata() {
        return sampleMetadata;
    }

    public void setSampleMetadata(SampleMetadataEntity sampleMetadata) {
        this.sampleMetadata = sampleMetadata;
    }

}
