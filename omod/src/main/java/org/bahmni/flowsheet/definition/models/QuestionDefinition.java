package org.bahmni.flowsheet.definition.models;

import org.bahmni.flowsheet.api.Question;
import org.openmrs.Concept;

import java.util.Set;

public class QuestionDefinition {
    private Integer questionId;
    private String name;
    private Set<Concept> concepts;
    private String questionType;

    public QuestionDefinition(Integer questionId, String name, Set<Concept> concepts, String questionType) {
        this.questionId = questionId;
        this.name = name;
        this.concepts = concepts;
        this.questionType = questionType;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
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

    public Question createQuestion() {

        if (this.questionType.equals("Drug")) {
            return new DrugQuestion(questionId, name, concepts, null);
        }
        return new ObsQuestion(questionId, name, concepts, null);
    }
}
