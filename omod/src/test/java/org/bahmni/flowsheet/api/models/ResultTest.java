package org.bahmni.flowsheet.api.models;

import org.bahmni.flowsheet.api.Status;
import org.bahmni.flowsheet.api.models.Question;
import org.bahmni.flowsheet.api.models.Result;
import org.junit.Test;
import org.mockito.Mock;
import org.openmrs.Concept;

import java.util.Arrays;
import java.util.LinkedHashSet;


public class ResultTest {

    @Mock
    Concept systolic;

    @Mock
    Concept diastolic;


    @Test
    public void shouldHaveValue() {
        Result result = new Result(Status.PENDING,  new Question("Blood Pressure", new LinkedHashSet<>(Arrays.asList(systolic, diastolic)), null));

    }

}