package org.bahmni.flowsheet.api.models;

import org.bahmni.flowsheet.api.Status;

public class Result {
    Status status;
    Question question;

    public Result(Status status,  Question question) {
        this.status = status;
        this.question = question;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
