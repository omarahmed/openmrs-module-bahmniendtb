package org.bahmni.flowsheet.definition.models;

import org.bahmni.flowsheet.api.Question;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.openmrs.Concept;

import java.util.Arrays;
import java.util.LinkedHashSet;

import static org.junit.Assert.*;
import static org.openmrs.module.bahmniendtb.EndTBConstants.OBS_QUESTION;

public class QuestionDefinitionTest {

    @Mock
    Concept systolic;

    @Mock
    Concept diastolic;

    QuestionDefinition questionDefinition;


    @Before
    public void setUp()  {
        questionDefinition = new QuestionDefinition(1, "Blood Pressure", new LinkedHashSet<>(Arrays.asList(systolic, diastolic)), OBS_QUESTION);
    }


    @Test
    public void shouldCreateQuestion() {
        Question question = questionDefinition.createQuestion();
        assertNotNull(question);
    }

}