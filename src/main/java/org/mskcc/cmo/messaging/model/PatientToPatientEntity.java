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
@RelationshipEntity(type="PX_TO_PX")
public class PatientToPatientEntity implements Serializable {
    @Id @GeneratedValue
    private Long id;
    private Collection<String> linkedPatientNames;
    @StartNode
    private LinkedPatient linkedPatient;
    @EndNode
    private PatientMetadata patientMetadata;

    public PatientToPatientEntity() {}

    public Collection<String> getLinkedPatientNames() {
        return linkedPatientNames;
    }

    public void setLinkedPatientNames(Collection<String> linkedPatientNames) {
        this.linkedPatientNames = linkedPatientNames;
    }

    public LinkedPatient getLinkedPatient() {
        return linkedPatient;
    }

    public void setLinkedPatient(LinkedPatient linkedPatient) {
        this.linkedPatient = linkedPatient;
    }

    public PatientMetadata getPatientMetadata() {
        return patientMetadata;
    }

    public void setPatientMetadata(PatientMetadata patientMetadata) {
        this.patientMetadata = patientMetadata;
    }

}
