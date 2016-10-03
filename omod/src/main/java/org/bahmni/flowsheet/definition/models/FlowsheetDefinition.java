package org.bahmni.flowsheet.definition.models;

import org.bahmni.flowsheet.api.Flowsheet;
import org.bahmni.flowsheet.api.Milestone;
import org.bahmni.flowsheet.api.Question;
import org.openmrs.PatientProgram;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

public class FlowsheetDefinition {

    private Date startDate;
    private Set<MilestoneDefinition> milestoneDefinitionList;
    private Set<QuestionDefinition> questionDefinitionList;

    public FlowsheetDefinition(Date startDate, Set<MilestoneDefinition> milestoneDefinitionList, Set<QuestionDefinition> questionDefinitionList) {
        this.startDate = startDate;
        this.milestoneDefinitionList = milestoneDefinitionList;
        this.questionDefinitionList = questionDefinitionList;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Set<MilestoneDefinition> getMilestoneDefinitionList() {
        return milestoneDefinitionList;
    }

    public void setMilestoneDefinitionList(Set<MilestoneDefinition> milestoneDefinitionList) {
        this.milestoneDefinitionList = milestoneDefinitionList;
    }

    public Set<QuestionDefinition> getQuestionDefinitionList() {
        return questionDefinitionList;
    }

    public void setQuestionDefinitionList(Set<QuestionDefinition> questionDefinitionList) {
        this.questionDefinitionList = questionDefinitionList;
    }

    public Flowsheet createFlowsheet(PatientProgram patientProgram) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Flowsheet flowsheet = new Flowsheet();
        flowsheet.setStartDate(this.startDate);
        Set<Milestone> milestones = new LinkedHashSet<>();
        for(MilestoneDefinition milestoneDefinition : this.milestoneDefinitionList) {
            milestones.add(milestoneDefinition.createMilestone(this.startDate, patientProgram));
        }

        Set<Question> questions = new LinkedHashSet<>();
        for (QuestionDefinition questionDefinition : this.questionDefinitionList) {
            questions.add(questionDefinition.createQuestion());
        }

        flowsheet.setMilestones(milestones);
        flowsheet.setQuestions(questions);
        return flowsheet;
    }
}
