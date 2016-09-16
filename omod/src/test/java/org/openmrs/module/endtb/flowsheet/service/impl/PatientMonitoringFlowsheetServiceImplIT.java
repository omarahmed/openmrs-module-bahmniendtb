package org.openmrs.module.endtb.flowsheet.service.impl;


import org.bahmni.module.bahmnicore.model.bahmniPatientProgram.BahmniPatientProgram;
import org.bahmni.module.bahmnicore.service.BahmniProgramWorkflowService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openmrs.Concept;
import org.openmrs.OrderType;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.ConceptService;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.bahmniendtb.EndTBConstants;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

import static org.bahmni.module.bahmnicore.mapper.PatientIdentifierMapper.EMR_PRIMARY_IDENTIFIER_TYPE;

@RunWith(SpringJUnit4ClassRunner.class)
@org.springframework.test.context.ContextConfiguration(locations = {"classpath:TestingApplicationContext.xml"}, inheritLocations = true)
public class PatientMonitoringFlowsheetServiceImplIT extends BaseModuleContextSensitiveTest {


    private BahmniProgramWorkflowService bahmniProgramWorkflowService;

    private PatientService patientService;

    private AdministrationService administrationService;

    private org.openmrs.api.OrderService orderService;

    private ConceptService conceptService;

    @Before
    public void setUp() throws Exception {
        executeDataSet("visitTestData.xml");
        executeDataSet("patientProgramTestData.xml");
        bahmniProgramWorkflowService = Context.getService(BahmniProgramWorkflowService.class);
        patientService = Context.getPatientService();
        administrationService = Context.getAdministrationService();
        orderService = Context.getOrderService();
        conceptService = Context.getConceptService();
    }

    @Test
    public void shouldSetFlowsheetAttributes() {
        BahmniPatientProgram bahmniPatientProgram = (BahmniPatientProgram) bahmniProgramWorkflowService.getPatientProgramByUuid("dfdfoifo-dkcd-475d-b939-6d82327f36a3");
        String uuid = bahmniPatientProgram.getUuid();
        PatientIdentifierType patientIdentifierType = patientService.getPatientIdentifierTypeByUuid(administrationService.getGlobalProperty(EMR_PRIMARY_IDENTIFIER_TYPE));
        OrderType orderType = orderService.getOrderTypeByUuid(OrderType.DRUG_ORDER_TYPE_UUID);
        Set<Concept> conceptsForDrugs = new HashSet<>();
        conceptsForDrugs.add(conceptService.getConceptByName(EndTBConstants.DRUG_BDQ));
        conceptsForDrugs.add(conceptService.getConceptByName(EndTBConstants.DRUG_DELAMANID));
//        PatientMonitoringFlowsheetService = new PatientMonitoringFlowsheetServiceImpl();

//        patientMonitoringFlowsheetService.getFlowsheetAttributesForPatientProgram(bahmniPatientProgram, primaryIdentifierType, orderType, conceptsForDrugs);

    }


}
