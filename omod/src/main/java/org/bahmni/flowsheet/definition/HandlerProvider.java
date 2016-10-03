package org.bahmni.flowsheet.definition;

import org.openmrs.api.context.Context;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class HandlerProvider {

    public Handler getHandler(String handler) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> handlerClass = Class.forName(handler);
        return  (Handler) Context.getRegisteredComponent("treatmentEndDateHandler", handlerClass);
    }
}
