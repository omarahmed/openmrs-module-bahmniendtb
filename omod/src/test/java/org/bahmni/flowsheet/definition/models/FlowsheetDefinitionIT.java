package org.bahmni.flowsheet.definition.models;


import org.bahmni.flowsheet.api.Flowsheet;
import org.bahmni.module.bahmnicore.service.BahmniProgramWorkflowService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openmrs.Concept;
import org.openmrs.PatientProgram;
import org.openmrs.api.ConceptService;
import org.openmrs.api.ProgramWorkflowService;
import org.openmrs.api.context.Context;
import org.openmrs.module.bahmniendtb.EndTBConstants;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@org.springframework.test.context.ContextConfiguration(locations = {"classpath:TestingApplicationContext.xml"}, inheritLocations = true)
public class FlowsheetDefinitionIT extends BaseModuleContextSensitiveTest {

    public static final String systolic = "Systolic";
    public static final String diastolic = "Diastolic";


    private ConceptService conceptService;

    private ProgramWorkflowService programWorkflowService;

    @Autowired
    MilestoneDefinition baselineMonth;

    @Before
    public void setUp() throws Exception {

        executeDataSet("flowsheetTestData.xml");
        conceptService = Context.getConceptService();
        programWorkflowService = Context.getProgramWorkflowService();
    }
    @Test
    public void shouldCreateFlowsheetFromFlowsheetDefinition() throws ParseException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Concept delamanid = conceptService.getConceptByName(EndTBConstants.DRUG_DELAMANID);
        Concept bdq = conceptService.getConceptByName(EndTBConstants.DRUG_BDQ);

        Concept systolicConcept = conceptService.getConceptByName(systolic);
        Concept diastolicConcept = conceptService.getConceptByName(diastolic);
        QuestionDefinition q1 = new QuestionDefinition(1, "New Drugs", new LinkedHashSet<>(Arrays.asList(delamanid, bdq)), EndTBConstants.DRUG_QUESTION);
        QuestionDefinition q2 = new QuestionDefinition(2, "Blood Pressure", new LinkedHashSet<>(Arrays.asList(systolicConcept, diastolicConcept)), EndTBConstants.OBS_QUESTION);

        Map<String, String> config = new HashMap<>();
        config.put("min", "0");
        config.put("max", "30");

        baselineMonth.setName("M1");
        baselineMonth.setConfig(config);
        baselineMonth.setQuestionDefinitions(new LinkedHashSet<>(Arrays.asList(q1)));


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date flowsheetStartDate = simpleDateFormat.parse("2016-10-10");

        FlowsheetDefinition flowsheetDefinition = new FlowsheetDefinition(flowsheetStartDate, new LinkedHashSet<>(Arrays.asList(baselineMonth)), new LinkedHashSet<>(Arrays.asList(q1,q2)));

        PatientProgram patientProgram = programWorkflowService.getPatientProgram(456);
        Flowsheet flowsheet = flowsheetDefinition.createFlowsheet(patientProgram);

        assertEquals(1, flowsheet.getMilestones().size());
        assertEquals(2, flowsheet.getQuestions().size());
    }

}
