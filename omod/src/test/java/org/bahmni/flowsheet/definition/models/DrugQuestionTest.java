package org.bahmni.flowsheet.definition.models;

import org.bahmni.flowsheet.api.Question;
import org.junit.Test;
import org.mockito.Mock;
import org.openmrs.Concept;
import org.openmrs.PatientProgram;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;

import static org.junit.Assert.*;

public class DrugQuestionTest {

    @Mock
    Concept bedaquiline;

    @Mock
    Concept delamanid;

    @Mock
    PatientProgram patientProgram;

    @Test
    public void shouldEvaluateItselfAndSetItsValue() {
        Question drugQuestion = new DrugQuestion(1, "New Drugs", new LinkedHashSet<>(Arrays.asList(bedaquiline, delamanid)), null);

        drugQuestion.evaluate(patientProgram, new Date(), new Date());

        assertNotNull(drugQuestion.getValue());
    }

}