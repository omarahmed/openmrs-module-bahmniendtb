package org.openmrs.module.endtb.flowsheet.service.impl;

import org.bahmni.flowsheet.api.Evaluator;
import org.bahmni.flowsheet.definition.HandlerProvider;
import org.bahmni.module.bahmnicore.dao.ObsDao;
import org.bahmni.module.bahmnicore.dao.OrderDao;
import org.bahmni.module.bahmnicore.service.BahmniConceptService;
import org.bahmni.module.bahmnicore.service.BahmniDrugOrderService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.openmrs.Concept;
import org.openmrs.PatientProgram;
import org.openmrs.api.OrderService;
import org.openmrs.module.endtb.flowsheet.models.FlowsheetConfig;
import org.bahmni.flowsheet.ui.FlowsheetUI;
import org.openmrs.module.endtb.flowsheet.service.PatientMonitoringFlowsheetService;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PatientMonitoringFlowsheetServiceImplTest {
    private PatientMonitoringFlowsheetService patientMonitoringFlowsheetService;

    @Mock
    private ObsDao obsDao;
    @Mock
    private BahmniDrugOrderService bahmniDrugOrderService;
    @Mock
    private BahmniConceptService bahmniConceptService;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderService orderService;
    @Mock
    private HandlerProvider handlerProvider;
    @Mock
    private List<Evaluator> evaluatorList;
    @Mock
    private PatientProgram patientProgram;


    @Before
    public void setUp() {
        initMocks(this);
        patientMonitoringFlowsheetService = new PatientMonitoringFlowsheetServiceImpl(orderDao, obsDao,bahmniConceptService, handlerProvider, evaluatorList);
        when(bahmniConceptService.getConceptByFullySpecifiedName(any(String.class))).thenReturn(new Concept());
    }


    @Test
    public void shouldCreateFlowsheetDefinitionFromConfig() {
        FlowsheetConfig flowsheetConfig = new FlowsheetConfig();
    }


    private FlowsheetUI getDummyFlowsheet() {
        Set<String> flowsheetHeader = new LinkedHashSet<>();
        flowsheetHeader.add("M1");
        flowsheetHeader.add("M2");
        flowsheetHeader.add("M3");
        Map<String, List<String>> flowsheetData = new LinkedHashMap<>();
        flowsheetData.put("Weight (kg)", Arrays.asList("yellow", "yellow", "yellow"));
        flowsheetData.put("Height (cm)", Arrays.asList("yellow", "yellow", "yellow"));
        flowsheetData.put("Baseline, Prison", Arrays.asList("yellow", "grey", "grey"));
        flowsheetData.put("Isoniazid", Arrays.asList("yellow", "grey", "grey"));
        flowsheetData.put("Delamanid", Arrays.asList("yellow", "yellow", "yellow"));
        flowsheetData.put("Bacteriology, Fluoroquinolone", Arrays.asList("yellow", "grey", "grey"));
        flowsheetData.put("Bacteriology, Culture results", Arrays.asList("yellow", "yellow", "yellow"));
        FlowsheetUI flowsheet = new FlowsheetUI();
        flowsheet.setFlowsheetHeader(flowsheetHeader);
        flowsheet.setFlowsheetData(flowsheetData);
        flowsheet.setHighlightedMilestone("M1");
        return flowsheet;
    }

    private String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        return simpleDateFormat.format(currentDate);
    }
}