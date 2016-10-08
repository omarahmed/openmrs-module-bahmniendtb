package org.bahmni.flowsheet.api.models;

import org.bahmni.flowsheet.api.Evaluator;
import org.openmrs.Concept;
import org.openmrs.PatientProgram;

import java.util.Date;
import java.util.Set;

public class Question {


    private String name;
    private Set<Concept> concepts;
    private Evaluator evaluator;
    private Result result;

    public Question( String name, Set<Concept> concepts, Evaluator evaluator) {

        this.name = name;
        this.concepts = concepts;
        this.evaluator = evaluator;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
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

    public Evaluator getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(Evaluator evaluator) {
        this.evaluator = evaluator;
    }

    public void evaluate(PatientProgram patientProgram, Date startDate, Date endDate) {

      this.result = evaluator.evaluate(patientProgram, concepts, startDate, endDate);
    }

}
