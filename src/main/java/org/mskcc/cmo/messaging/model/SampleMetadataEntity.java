package org.mskcc.cmo.messaging.model;

import org.mskcc.cmo.shared.SampleMetadata;

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

@NodeEntity
public class SampleMetadataEntity extends SampleMetadata {
    @Id @GeneratedValue(strategy = UuidStrategy.class)
    @Convert(UuidStringConverter.class)
    private UUID metaDbUuid;
    @Relationship(type="SP_TO_SP", direction=Relationship.INCOMING)
    private List<LinkedSample> linkedSampleList;
    @Relationship(type="PX_TO_SP", direction=Relationship.INCOMING)
    private PatientMetadata patient;

    public SampleMetadataEntity() {
        super();
    }

    public SampleMetadataEntity(UUID metaDbUuid, String mrn, String cmoPatientId, String cmoSampleId, String igoId, String investigatorSampleId, String species,
                          String sex, String tumorOrNormal, String sampleType, String preservation, String tumorType, String parentTumorType,
                          String specimenType, String sampleOrigin, String tissueSource, String tissueLocation, String recipe,
                          String baitset, String fastqPath, String principalInvestigator, String ancestorSample, boolean doNotUse, String sampleStatus,
                          List<LinkedSample> linkedSampleList, PatientMetadata patient) {
        super(mrn,
            cmoPatientId,
            cmoSampleId,
            igoId,
            investigatorSampleId,
            species,
            sex,
            tumorOrNormal,
            sampleType,
            preservation,
            tumorType,
            parentTumorType,
            specimenType,
            sampleOrigin,
            tissueSource,
            tissueLocation,
            recipe,
            baitset,
            fastqPath,
            principalInvestigator,
            ancestorSample,
            doNotUse,
            sampleStatus);
        this.metaDbUuid = metaDbUuid;
        this.linkedSampleList = linkedSampleList;
        this.patient = patient;
    }

    public UUID getMetaDbUuid() {
        return metaDbUuid;
    }

    public void setMetaDbUuid(UUID metaDbUuid) {
        this.metaDbUuid = metaDbUuid;
    }

    public List<LinkedSample> getLinkedSampleList() {
        return linkedSampleList;
    }

    public void setLinkedSampleList(List<LinkedSample> linkedSampleList) {
        this.linkedSampleList = linkedSampleList;
    }

    public void linkSample(LinkedSample linkedSample) {
        if (linkedSampleList == null) {
            linkedSampleList = new ArrayList<>();
        }
        linkedSampleList.add(linkedSample);
    }

    public PatientMetadata getPatient() {
        return patient;
    }

    public void setPatient(PatientMetadata patient) {
        this.patient = patient;
    }

}
