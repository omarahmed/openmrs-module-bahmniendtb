package org.bahmni.flowsheet.api.impl;

import org.bahmni.flowsheet.api.Status;
import org.bahmni.flowsheet.api.models.Result;
import org.bahmni.module.bahmnicore.service.BahmniDrugOrderService;
import org.bahmni.module.referencedata.labconcepts.contract.Drug;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openmrs.Concept;
import org.openmrs.Patient;
import org.openmrs.PatientProgram;
import org.openmrs.module.bahmniemrapi.drugorder.contract.BahmniDrugOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DrugEvaluatorTest {

    @Mock
    PatientProgram patientProgram;

    @Mock
    BahmniDrugOrderService bahmniDrugOrderService;

    @Mock
    Date startDate, endDate;

    @Mock
    Concept delamanid;

    @Mock
    BahmniDrugOrder bahmniDrugOrder;


    @InjectMocks
    DrugEvaluator drugEvaluator;

    @Before
    public void setUp() {
        initMocks(this);

    }

    @Test
    public void shouldEvaluateStatusAsPendingWhenEndDateIsBeforeToday() throws ParseException {
        Patient patient = new Patient();
        patient.setUuid("patientUuid");
        when(patientProgram.getPatient()).thenReturn(patient);
        when(patientProgram.getUuid()).thenReturn("programUuid");
        when(endDate.before(any(Date.class))).thenReturn(true);
        when(bahmniDrugOrderService.getDrugOrders("patientUuid",null, new HashSet<>(Arrays.asList(delamanid)) ,null,"programUuid")).thenReturn(null);

        Result result = drugEvaluator.evaluate(patientProgram, new HashSet<>(Arrays.asList(delamanid)),
                startDate, endDate);

        assertNotNull(result);
        assertEquals("Drug", drugEvaluator.getType());
        assertEquals(Status.PENDING,result.getStatus());
    }

    @Test
    public void shouldEvaluateStatusAsPlannedWhenEndDateIsAfterToday() throws ParseException {
        Patient patient = new Patient();
        patient.setUuid("patientUuid");
        when(patientProgram.getPatient()).thenReturn(patient);
        when(patientProgram.getUuid()).thenReturn("programUuid");
        when(endDate.before(any(Date.class))).thenReturn(false);
        when(bahmniDrugOrderService.getDrugOrders("patientUuid",null, new HashSet<>(Arrays.asList(delamanid)) ,null,"programUuid")).thenReturn(null);

        Result result = drugEvaluator.evaluate(patientProgram, new HashSet<>(Arrays.asList(delamanid)),
                startDate, endDate);

        assertNotNull(result);
        assertEquals(Status.PLANNED,result.getStatus());
    }


    @Test
    public void shouldEvaluateStatusAsDataAddedWhenDrugOrderIsPresentWithinStartDateAndEndDate() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = simpleDateFormat.parse("2016-1-10");
        Date endDate = simpleDateFormat.parse("2016-03-30");
        Date drugOrderStartDate = simpleDateFormat.parse("2016-03-30");
        Date drugOrderEndDate = simpleDateFormat.parse("2016-03-30");
        Patient patient = new Patient();
        patient.setUuid("patientUuid");

        when(bahmniDrugOrder.getEffectiveStartDate()).thenReturn(drugOrderStartDate);
        when(bahmniDrugOrder.getEffectiveStopDate()).thenReturn(drugOrderEndDate);
        when(patientProgram.getPatient()).thenReturn(patient);
        when(patientProgram.getUuid()).thenReturn("programUuid");
        when(bahmniDrugOrderService.getDrugOrders("patientUuid",null, new HashSet<>(Arrays.asList(delamanid)) ,null,"programUuid")).thenReturn(Arrays.asList(bahmniDrugOrder));

        Result result = drugEvaluator.evaluate(patientProgram, new HashSet<>(Arrays.asList(delamanid)),
                startDate, endDate);

        assertNotNull(result);
        assertEquals(Status.DATA_ADDED,result.getStatus());
    }

}