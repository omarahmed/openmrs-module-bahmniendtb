package org.bahmni.flowsheet.definition.models;

import org.bahmni.flowsheet.api.models.Question;
import org.bahmni.flowsheet.api.Evaluator;
import org.openmrs.Concept;

import java.util.List;
import java.util.Set;

public class QuestionDefinition {
    private String name;
    private Set<Concept> concepts;
    private String questionType;

    public QuestionDefinition( String name, Set<Concept> concepts, String questionType) {
        this.name = name;
        this.concepts = concepts;
        this.questionType = questionType;
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

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public Question createQuestion(List<Evaluator> evaluatorList) {
        return new Question(name, concepts, getEvaluatorFromList(evaluatorList));
    }

    private Evaluator getEvaluatorFromList(List<Evaluator> evaluatorList) {
        if(evaluatorList == null ||evaluatorList.isEmpty()){
            return null;
        }
        for (Evaluator evaluator : evaluatorList) {
            if(this.questionType.equals(evaluator.getType())) {
                return evaluator;
            }
        }
        return null;
    }
}
