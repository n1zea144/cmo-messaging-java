package org.mskcc.cmo.messaging.model;

import java.io.Serializable;
import java.util.Collection;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

/**
 *
 * @author ochoaa
 */
@RelationshipEntity(type="SP_TO_SP")
public class SampleToSampleEntity implements Serializable {
    @Id @GeneratedValue
    private Long id;
    private Collection<String> linkedSampleNames;
    @StartNode
    private LinkedSample linkedSample;
    @EndNode
    private SampleMetadataEntity sampleMetadata;

    public SampleToSampleEntity() {}

    public Collection<String> getLinkedSampleNames() {
        return linkedSampleNames;
    }

    public void setLinkedSampleNames(Collection<String> linkedSampleNames) {
        this.linkedSampleNames = linkedSampleNames;
    }

    public LinkedSample getSample() {
        return linkedSample;
    }

    public void setSample(LinkedSample sample) {
        this.linkedSample = sample;
    }

    public SampleMetadataEntity getSampleMetadata() {
        return sampleMetadata;
    }

    public void setSampleMetadata(SampleMetadataEntity sampleMetadata) {
        this.sampleMetadata = sampleMetadata;
    }

}
