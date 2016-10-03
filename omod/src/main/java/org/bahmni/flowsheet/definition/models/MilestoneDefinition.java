package org.bahmni.flowsheet.definition.models;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.bahmni.flowsheet.definition.HandlerProvider;
import org.bahmni.flowsheet.api.Milestone;
import org.bahmni.flowsheet.api.Question;
import org.openmrs.PatientProgram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Component
@Scope("prototype")
public class MilestoneDefinition {

    private String name;
    private Map<String, String> config;
    private String handler;
    private Set<QuestionDefinition> questionDefinitions;
    private HandlerProvider handlerProvider;

    @Autowired
    public MilestoneDefinition(HandlerProvider handlerProvider) {

        this.handlerProvider = handlerProvider;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public Set<QuestionDefinition> getQuestionDefinitions() {
        return questionDefinitions;
    }

    public void setQuestionDefinitions(Set<QuestionDefinition> questionDefinitions) {
        this.questionDefinitions = questionDefinitions;
    }

    public HandlerProvider getHandlerProvider() {
        return handlerProvider;
    }

    public void setHandlerProvider(HandlerProvider handlerProvider) {
        this.handlerProvider = handlerProvider;
    }

    public Milestone createMilestone(Date startDate, PatientProgram patientProgram) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Milestone milestone = new Milestone();
        if(StringUtils.isNotEmpty(this.handler)) {
            startDate = handlerProvider.getHandler(this.handler).getDate(patientProgram);
        }
        milestone.setStartDate(DateUtils.addDays(startDate, Integer.parseInt(config.get("min"))));
        milestone.setEndDate(DateUtils.addDays(startDate, Integer.parseInt(config.get("max"))));
        Set<Question> questions = new LinkedHashSet<>();
        for(QuestionDefinition questionDefinition : this.questionDefinitions) {
            questions.add(questionDefinition.createQuestion());
        }
        milestone.setQuestions(questions);
        return milestone;
    }
}
