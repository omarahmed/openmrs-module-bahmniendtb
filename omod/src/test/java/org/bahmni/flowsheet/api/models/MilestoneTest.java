package org.bahmni.flowsheet.api.models;

import org.bahmni.flowsheet.api.Status;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.openmrs.PatientProgram;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class MilestoneTest {

    @Mock
    Question height;

    @Mock
    Question weight;

    @Mock
    Question bloodPressure;

    @Mock
    Date startDate;

    @Mock
    Date endDate;

    @Mock
    PatientProgram patientProgram;


    @Before
    public void setUp() {
        initMocks(this);
    }


    @Test
    public void shouldEvaluateForAllTheQuestions() {

        Set<Question> questions = new LinkedHashSet<>();
        questions.add(height);
        questions.add(weight);
        questions.add(bloodPressure);

        Milestone milestone = new Milestone();
        milestone.setStartDate(startDate);
        milestone.setEndDate(endDate);
        milestone.setQuestions(questions);
        milestone.setName("M1");

        milestone.evaluate(patientProgram);

        verify(height, times(1)).evaluate(patientProgram, startDate, endDate);
        verify(weight, times(1)).evaluate(patientProgram, startDate, endDate);
        verify(bloodPressure, times(1)).evaluate(patientProgram, startDate, endDate);
        assertEquals("M1", milestone.getName());

    }

}