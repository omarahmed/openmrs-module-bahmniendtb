package org.bahmni.flowsheet.api;

import java.util.Date;
import java.util.Set;

public class Flowsheet {

    private Date startDate;
    private Set<Milestone> milestones;
    private Set<Question> questions;

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

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public void evaluate() {
        for (Milestone milestone : milestones) {
            milestone.evaluate();
        }

    }
}
