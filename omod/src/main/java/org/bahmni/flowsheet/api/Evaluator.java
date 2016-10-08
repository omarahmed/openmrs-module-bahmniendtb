package org.bahmni.flowsheet.api;


import org.bahmni.flowsheet.api.models.Result;
import org.openmrs.Concept;
import org.openmrs.PatientProgram;

import java.util.Date;
import java.util.Set;

public interface Evaluator {
    String getType();

    public Result evaluate(PatientProgram patientProgram, Set<Concept> concepts, Date startDate, Date endDate);

}
