package org.bahmni.flowsheet.definition.models;

import org.bahmni.flowsheet.definition.Handler;
import org.bahmni.flowsheet.definition.HandlerProvider;
import org.bahmni.flowsheet.api.Milestone;
import org.bahmni.flowsheet.definition.impl.TreatmentEndDateHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openmrs.Concept;
import org.openmrs.PatientProgram;
import org.openmrs.module.bahmniendtb.EndTBConstants;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.openmrs.module.bahmniendtb.EndTBConstants.OBS_QUESTION;

public class MilestoneDefinitionTest {
    @Mock
    Concept systolic;

    @Mock
    Concept diastolic;

    @Mock
    HandlerProvider handlerProvider;

    @Mock
    Handler handler;

    @Mock
    TreatmentEndDateHandler treatmentEndDateHandler;

    @InjectMocks
    private MilestoneDefinition milestoneDefinition;

    private SimpleDateFormat simpleDateFormat;

    @Before
    public void setUp() throws ParseException {
        initMocks(this);
        QuestionDefinition questionDefinition = new QuestionDefinition(1, "Blood Pressure", new LinkedHashSet<>(Arrays.asList(systolic, diastolic)), OBS_QUESTION);
        Map<String, String> config = new HashMap<>();
        config.put("min", "0");
        config.put("max", "30");
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        milestoneDefinition.setName("M1");
        milestoneDefinition.setConfig(config);
        milestoneDefinition.setQuestionDefinitions(new LinkedHashSet<>(Arrays.asList(questionDefinition)));

    }

    @Test
        public void shouldCreateMilestoneWithFlowsheetStartDateWhenHandlerIsUnavailable() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, ParseException {
        Milestone milestone = milestoneDefinition.createMilestone(simpleDateFormat.parse("2016-01-01"), new PatientProgram());

        assertEquals(simpleDateFormat.parse("2016-01-01"), milestone.getStartDate());
        assertEquals(simpleDateFormat.parse("2016-01-31"), milestone.getEndDate());
        assertEquals(1, milestone.getQuestions().size());
    }

    @Test
        public void shouldCreateMilestoneWithHandler() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, ParseException {
        String handler = EndTBConstants.TREATMENT_END_DATE_HANDLER;
        PatientProgram patientProgram = new PatientProgram();
        milestoneDefinition.setHandler(handler);
        milestoneDefinition.setHandlerProvider(handlerProvider);
        when(handlerProvider.getHandler(handler)).thenReturn(treatmentEndDateHandler);
        when(treatmentEndDateHandler.getDate(patientProgram)).thenReturn(simpleDateFormat.parse("2015-01-01"));

        Milestone milestone = milestoneDefinition.createMilestone(null, patientProgram);
        assertEquals(simpleDateFormat.parse("2015-01-01"), milestone.getStartDate());
        assertEquals(simpleDateFormat.parse("2015-01-31"), milestone.getEndDate());
        assertEquals(1, milestone.getQuestions().size());


    }

}