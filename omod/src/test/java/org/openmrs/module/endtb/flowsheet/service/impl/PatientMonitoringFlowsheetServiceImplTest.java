package org.openmrs.module.endtb.flowsheet.service.impl;

import org.bahmni.module.bahmnicore.dao.ObsDao;
import org.bahmni.module.bahmnicore.dao.impl.ObsDaoImpl;
import org.bahmni.module.bahmnicore.service.BahmniDrugOrderService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.openmrs.Obs;
import org.openmrs.api.ConceptService;
import org.openmrs.module.endtb.flowsheet.mapper.FlowsheetDrugMapper;
import org.openmrs.module.endtb.flowsheet.mapper.FlowsheetMapper;
import org.openmrs.module.endtb.flowsheet.mapper.FlowsheetObsMapper;
import org.openmrs.module.endtb.flowsheet.models.Flowsheet;
import org.openmrs.module.endtb.flowsheet.service.PatientMonitoringFlowsheetService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertTrue;
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
    private ConceptService conceptService;

    private FlowsheetObsMapper flowsheetObsMapper;
    private FlowsheetDrugMapper flowsheetDrugMapper;
    private List<FlowsheetMapper> flowsheetMappers;

    @Before
    public void setUp() {
        initMocks(this);
        flowsheetObsMapper = new FlowsheetObsMapper(obsDao, bahmniDrugOrderService, conceptService);
        flowsheetDrugMapper = new FlowsheetDrugMapper(obsDao, bahmniDrugOrderService, conceptService);
        flowsheetMappers = Arrays.asList(flowsheetObsMapper, flowsheetDrugMapper);
        patientMonitoringFlowsheetService = new PatientMonitoringFlowsheetServiceImpl(obsDao, flowsheetMappers);
    }

    @Test
    public void shouldReturnNullIfThereIsNoTreatmentStartDate() throws Exception {
        Set<String> flowsheetHeader = new LinkedHashSet<>();
        flowsheetHeader.add("M1");
        flowsheetHeader.add("M2");
        flowsheetHeader.add("M3");
        Map<String, List<String>> flowsheetData = new LinkedHashMap<>();
        flowsheetData.put("Weight (kg)", new ArrayList<String>());
        flowsheetData.put("Height (cm)", new ArrayList<String>());
        flowsheetData.put("Baseline, Prison", new ArrayList<String>());
        flowsheetData.put("Isoniazid", new ArrayList<String>());
        flowsheetData.put("Delamanid", new ArrayList<String>());

        when(obsDao.getObsByPatientProgramUuidAndConceptNames(any(String.class), any(List.class), any(Integer.class), any(ObsDaoImpl.OrderBy.class), any(Date.class), any(Date.class))).thenReturn(null);
        Flowsheet actualFlowsheet = patientMonitoringFlowsheetService.getFlowsheetForPatientProgram("patientUuid", "programUuid", "src/test/resources/patientMonitoringConf.json");
        Flowsheet expectedFlowsheet = new Flowsheet();
        expectedFlowsheet.setFlowsheetHeader(flowsheetHeader);
        expectedFlowsheet.setFlowsheetData(flowsheetData);

        assertTrue(actualFlowsheet.equals(expectedFlowsheet));
    }

    @Test
    public void shouldReturnFlowsheetIfTreatmentStartDateTodayForObsAndDrug() throws Exception {
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
        Obs obs = new Obs();
        obs.setValueDate(new Date());
        when(obsDao.getObsByPatientProgramUuidAndConceptNames(any(String.class), any(List.class), any(Integer.class), any(ObsDaoImpl.OrderBy.class), any(Date.class), any(Date.class))).thenReturn(Arrays.asList(obs)).thenReturn(null);
        when(bahmniDrugOrderService.getDrugOrders(any(String.class), any(Boolean.class), any(Set.class), any(Set.class), any(String.class))).thenReturn(null);
        Flowsheet actualFlowsheet = patientMonitoringFlowsheetService.getFlowsheetForPatientProgram("patientUuid", "programUuid", "src/test/resources/patientMonitoringConf.json");
        Flowsheet expectedFlowsheet = new Flowsheet();
        expectedFlowsheet.setFlowsheetHeader(flowsheetHeader);
        expectedFlowsheet.setFlowsheetData(flowsheetData);
        assertTrue(actualFlowsheet.equals(expectedFlowsheet));
    }
}