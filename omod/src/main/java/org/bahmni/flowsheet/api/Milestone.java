package org.bahmni.flowsheet.api;

import java.util.Date;
import java.util.Set;

public class Milestone {
    private Date startDate;
    private Date endDate;
    private Set<Question> questions;

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

    public void evaluate() {

    }
}
