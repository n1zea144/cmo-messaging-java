package org.mskcc.cmo.messaging.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

/**
 *
 * @author ochoaa
 */
@RelationshipEntity(type="PX_TO_SP")
public class PatientToSampleEntity implements Serializable {
    @Id @GeneratedValue
    private Long id;
    private Collection<UUID> sampleUuidList;
    @StartNode
    private PatientMetadata patient;
    @EndNode
    private SampleMetadataEntity sampleMetadata;

    public PatientToSampleEntity() {}

    public Collection<UUID> getSampleUuidList() {
        return sampleUuidList;
    }

    public void setSampleUuidList(Collection<UUID> sampleUuidList) {
        this.sampleUuidList = sampleUuidList;
    }

    public PatientMetadata getPatient() {
        return patient;
    }

    public void setPatient(PatientMetadata patient) {
        this.patient = patient;
    }

    public SampleMetadataEntity getSampleMetadata() {
        return sampleMetadata;
    }

    public void setSampleMetadata(SampleMetadataEntity sampleMetadata) {
        this.sampleMetadata = sampleMetadata;
    }

}
