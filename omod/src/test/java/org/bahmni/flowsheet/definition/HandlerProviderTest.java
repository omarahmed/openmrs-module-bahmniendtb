package org.bahmni.flowsheet.definition;

import org.bahmni.flowsheet.definition.impl.TreatmentEndDateHandler;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.bahmniendtb.EndTBConstants;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

public class HandlerProviderTest {

    private HandlerProvider handlerProvider;

    @Before
    public void setup() {
        handlerProvider = new HandlerProvider();
    }

    @Test(expected = ClassNotFoundException.class)
    public void shouldReturnNullIfHandlerNotPresent() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        handlerProvider.getHandler("someHandler");
    }

    @Test
    public void shouldReturnTreatmentStartDateHandlerObject() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Handler treatmentStartDateHandler = handlerProvider.getHandler(EndTBConstants.TREATMENT_END_DATE_HANDLER);
        assertEquals(TreatmentEndDateHandler.class, treatmentStartDateHandler.getClass());
    }

}