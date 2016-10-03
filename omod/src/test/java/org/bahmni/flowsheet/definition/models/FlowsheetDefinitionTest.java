package org.bahmni.flowsheet.definition.models;

import org.bahmni.flowsheet.api.Flowsheet;
import org.bahmni.flowsheet.definition.HandlerProvider;
import org.bahmni.flowsheet.api.Milestone;
import org.bahmni.flowsheet.api.Question;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openmrs.Concept;
import org.openmrs.PatientProgram;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.openmrs.module.bahmniendtb.EndTBConstants.*;

public class FlowsheetDefinitionTest {

    @Mock
    Concept systolic;

    @Mock
    Concept diastolic;

    @Mock
    HandlerProvider handlerProvider;


    @InjectMocks
    MilestoneDefinition milestoneDefinition;

    private FlowsheetDefinition flowsheetDefinition;
    private SimpleDateFormat simpleDateFormat;

    @Before
    public void setup() throws ParseException {
        initMocks(this);
        QuestionDefinition questionDefinition = new QuestionDefinition(1, "Blood Pressure", new LinkedHashSet<>(Arrays.asList(systolic, diastolic)), OBS_QUESTION);

        Map<String, String> config = new HashMap<>();
        config.put("min", "0");
        config.put("max", "30");

        milestoneDefinition.setName("M1");
        milestoneDefinition.setConfig(config);
        milestoneDefinition.setQuestionDefinitions(new LinkedHashSet<>(Arrays.asList(questionDefinition)));

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse("2016-10-10");
        Set<MilestoneDefinition> milestoneDefinitions = new LinkedHashSet<>();
        milestoneDefinitions.add(milestoneDefinition);
        Set<QuestionDefinition> questionDefinitions = new LinkedHashSet<>();
        questionDefinitions.add(questionDefinition);

        flowsheetDefinition = new FlowsheetDefinition(date, milestoneDefinitions, questionDefinitions);
    }

    @Test
    public void shouldCreateFlowsheetFromDefinition() throws ParseException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Flowsheet flowsheet = flowsheetDefinition.createFlowsheet(new PatientProgram());
        Date date = simpleDateFormat.parse("2016-10-10");
        Set<Milestone> milestones = flowsheet.getMilestones();
        Set<Question> questions = flowsheet.getQuestions();


        assertEquals(date, flowsheet.getStartDate());
        assertEquals(1, milestones.size());
        assertEquals(1, questions.size());
    }

}