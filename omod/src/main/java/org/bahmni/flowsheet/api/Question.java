package org.bahmni.flowsheet.api;

import org.openmrs.Concept;
import org.openmrs.PatientProgram;
import org.openmrs.logic.op.In;

import java.util.Date;
import java.util.List;
import java.util.Set;

public abstract class Question {

    private Integer id;
    private String name;
    private Set<Concept> concepts;
    private String value;

    public Question(Integer id, String name, Set<Concept> concepts, String value) {
        this.id = id;
        this.name = name;
        this.concepts = concepts;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Concept> getConcepts() {
        return concepts;
    }

    public void setConcepts(Set<Concept> concepts) {
        this.concepts = concepts;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public abstract void evaluate(PatientProgram patientProgram, Date startDate, Date endDate);
}
