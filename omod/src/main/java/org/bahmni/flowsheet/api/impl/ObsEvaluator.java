package org.bahmni.flowsheet.api.impl;

import org.apache.commons.lang3.time.DateUtils;
import org.bahmni.flowsheet.api.models.Result;
import org.bahmni.flowsheet.api.Status;
import org.bahmni.flowsheet.api.Evaluator;
import org.bahmni.module.bahmnicore.dao.ObsDao;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.PatientProgram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ObsEvaluator implements Evaluator {

    ObsDao obsDao;
    String type;


    @Autowired
    public ObsEvaluator(ObsDao obsDao) {
        this.obsDao = obsDao;
        this.type = "Obs";
    }

    @Override
    public String getType() {
        return this.type;
    }

    public Result evaluate(PatientProgram patientProgram, Set<Concept> conceptSet, Date startDate, Date endDate) {
        List<String> conceptNames = new ArrayList<>();
        for (Concept concept : conceptSet) {
            conceptNames.add(concept.getName().getName());
        }

        for (String conceptName : conceptNames) {
        Result result =  getResultForConcept(patientProgram, startDate, endDate, Arrays.asList(conceptName));
            if(!result.getStatus().equals(Status.DATA_ADDED)) {
                return result;
            }
        }
        return new Result(Status.DATA_ADDED, null);
    }

    private Result getResultForConcept(PatientProgram patientProgram, Date startDate, Date endDate, List<String> conceptNames) {
        List<Obs> obsList = obsDao.getObsByPatientProgramUuidAndConceptNames(patientProgram.getUuid(), conceptNames , null, null, null, null);
        for (Obs obs : obsList) {

            Date obsDate = obs.getObsDatetime();
            if ((obsDate.after(startDate)) || DateUtils.isSameDay(obsDate, startDate)
                    && (obsDate.before(endDate)) || DateUtils.isSameDay(obsDate, endDate)) {
                return new Result(Status.DATA_ADDED, null);
            }
        }
        if(endDate.before(new Date())){
            return new Result(Status.PENDING, null);
        }
        return new Result(Status.PLANNED, null);
    }
}
