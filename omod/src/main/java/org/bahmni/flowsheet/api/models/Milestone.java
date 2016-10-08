package org.bahmni.flowsheet.api.models;

import org.openmrs.PatientProgram;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

public class Milestone {
    private String name;
    private Date startDate;
    private Date endDate;
    private Set<Question> questions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }


    public void evaluate(PatientProgram patientProgram) {
        if (questions != null) {
            for (Question milestoneQuestion : questions) {
                milestoneQuestion.evaluate(patientProgram, startDate, endDate);
            }
        }
    }

}
