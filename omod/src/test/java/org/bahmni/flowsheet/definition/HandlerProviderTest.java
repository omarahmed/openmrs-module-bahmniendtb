package org.bahmni.flowsheet.definition;

import org.bahmni.flowsheet.definition.impl.TreatmentEndDateHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openmrs.api.context.Context;
import org.openmrs.module.bahmniendtb.EndTBConstants;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class})

public class HandlerProviderTest {

    private HandlerProvider handlerProvider;

    @Autowired
    TreatmentEndDateHandler treatmentEndDateHandler;

    @Before
    public void setup() {
        initMocks(this);
        mockStatic(Context.class);
        handlerProvider = new HandlerProvider();
    }

    @Test(expected = ClassNotFoundException.class)
    public void shouldReturnNullIfHandlerNotPresent() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        handlerProvider.getHandler("someHandler");
    }

    @Test
    public void shouldReturnTreatmentStartDateHandlerObject() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        when(Context.getRegisteredComponent("treatmentEndDateHandler", TreatmentEndDateHandler.class)).thenReturn(treatmentEndDateHandler);

        Handler treatmentStartDateHandler = handlerProvider.getHandler(EndTBConstants.TREATMENT_END_DATE_HANDLER);

        assertEquals(TreatmentEndDateHandler.class, treatmentStartDateHandler.getClass());
    }

}