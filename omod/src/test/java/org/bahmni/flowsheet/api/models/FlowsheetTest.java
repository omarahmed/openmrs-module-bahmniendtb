package org.bahmni.flowsheet.api.models;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.openmrs.PatientProgram;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;


public class FlowsheetTest {

    @Mock
    Milestone baseline;

    @Mock
    Milestone endOfTreatment;

    @Mock
    PatientProgram patientProgram;


    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldEvaluateItself() {
        Flowsheet flowsheet = new Flowsheet();

        flowsheet.setMilestones(new LinkedHashSet<>(Arrays.asList(baseline, endOfTreatment)));

        flowsheet.evaluate(patientProgram);

        verify(baseline).evaluate(patientProgram);
        verify(endOfTreatment).evaluate(patientProgram);
    }
}