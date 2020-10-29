package org.mskcc.cmo.messaging.model;

import java.io.Serializable;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

/**
 *
 * @author ochoaa
 */
@NodeEntity
public class LinkedPatient implements Serializable {
    @Id @GeneratedValue
    private Long id;
    private String linkedPatientName;
    private String linkedSystemName;
    @Relationship(type="PX_TO_PX", direction=Relationship.OUTGOING)
    private PatientMetadata patientMetadata;

    public LinkedPatient() {}

    public LinkedPatient(String linkedPatientName, String linkedSystemName) {
        this.linkedPatientName = linkedPatientName;
        this.linkedSystemName = linkedSystemName;
    }

    public String getLinkedPatientName() {
        return linkedPatientName;
    }

    public void setLinkedPatientName(String linkedPatientName) {
        this.linkedPatientName = linkedPatientName;
    }

    public String getLinkedSystemName() {
        return linkedSystemName;
    }

    public void setLinkedSystemName(String linkedSystemName) {
        this.linkedSystemName = linkedSystemName;
    }

    public PatientMetadata getPatientMetadata() {
        return patientMetadata;
    }

    public void setPatientMetadata(PatientMetadata patientMetadata) {
        this.patientMetadata = patientMetadata;
    }

}
