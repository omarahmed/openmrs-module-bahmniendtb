package org.bahmni.flowsheet.api.models;

import org.openmrs.PatientProgram;

import java.util.Date;
import java.util.Set;

public class Flowsheet {

    private Date startDate;
    private Set<Milestone> milestones;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Set<Milestone> getMilestones() {
        return milestones;
    }

    public void setMilestones(Set<Milestone> milestones) {
        this.milestones = milestones;
    }




    public void evaluate(PatientProgram patientProgram) {
        for (Milestone milestone : milestones) {
             milestone.evaluate(patientProgram);
        }

    }
}
