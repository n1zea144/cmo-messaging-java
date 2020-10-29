package org.mskcc.cmo.messaging.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;
import org.neo4j.ogm.id.UuidStrategy;
import org.neo4j.ogm.typeconversion.UuidStringConverter;

/**
 *
 * @author ochoaa
 */
@NodeEntity
public class PatientMetadata implements Serializable {
    @Id @GeneratedValue(strategy = UuidStrategy.class)
    @Convert(UuidStringConverter.class)
    private UUID metaDbUuid;
    private String investigatorPatientId;
    @Relationship(type="PX_TO_SP", direction=Relationship.OUTGOING)
    private List<SampleMetadataEntity> sampleMetadataList;
    @Relationship(type="PX_TO_PX", direction=Relationship.INCOMING)
    private List<LinkedPatient>  linkedPatientList;

    public PatientMetadata() {}

    public UUID getMetaDbUuid() {
        return metaDbUuid;
    }

    public void setMetaDbUuid(UUID metaDbUuid) {
        this.metaDbUuid = metaDbUuid;
    }

    public String getInvestigatorPatientId() {
        return investigatorPatientId;
    }

    public void setInvestigatorPatientId(String investigatorPatientId) {
        this.investigatorPatientId = investigatorPatientId;
    }

    public List<SampleMetadataEntity> getSampleMetadataList() {
        return sampleMetadataList;
    }

    public void setSampleMetadataList(List<SampleMetadataEntity> sampleMetadataList) {
        this.sampleMetadataList = sampleMetadataList;
    }

    public void linkSampleMetadata(SampleMetadataEntity sampleMetadata) {
        if (sampleMetadataList == null) {
            sampleMetadataList = new ArrayList<>();
        }
        sampleMetadataList.add(sampleMetadata);
    }

    public List<LinkedPatient> getLinkedPatientList() {
        return linkedPatientList;
    }

    public void setLinkedPatientList(List<LinkedPatient> linkedPatientList) {
        this.linkedPatientList = linkedPatientList;
    }

    public void linkPatient(LinkedPatient linkedPatient) {
        if (linkedPatientList == null) {
            linkedPatientList = new ArrayList<>();
        }
        linkedPatientList.add(linkedPatient);
    }

}
