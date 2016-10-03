package org.bahmni.flowsheet.api.models;

import org.bahmni.flowsheet.api.Flowsheet;
import org.bahmni.flowsheet.api.Milestone;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.LinkedHashSet;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;


public class FlowsheetTest {

    @Mock
    Milestone milestone;


    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldEvaluateItself() {
        Flowsheet flowsheet = new Flowsheet();
        flowsheet.setMilestones(new LinkedHashSet<>(Arrays.asList(milestone)));

        flowsheet.evaluate();

        verify(milestone).evaluate();
    }
}