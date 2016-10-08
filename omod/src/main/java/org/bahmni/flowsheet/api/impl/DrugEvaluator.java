package org.bahmni.flowsheet.api.impl;

import org.bahmni.flowsheet.api.models.Result;
import org.bahmni.flowsheet.api.Status;
import org.bahmni.flowsheet.api.Evaluator;
import org.bahmni.module.bahmnicore.service.BahmniDrugOrderService;
import org.openmrs.Concept;
import org.openmrs.PatientProgram;
import org.openmrs.module.bahmniemrapi.drugorder.contract.BahmniDrugOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;

@Component
public class DrugEvaluator implements Evaluator {

    String type;
    BahmniDrugOrderService bahmniDrugOrderService;


    @Autowired
    public DrugEvaluator(BahmniDrugOrderService bahmniDrugOrderService) {
        this.bahmniDrugOrderService = bahmniDrugOrderService;
        this.type = "Drug";
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public Result evaluate(PatientProgram patientProgram, Set<Concept> concepts, Date startDate, Date endDate) {

        try {
            for (Concept concept : concepts) {
                Result result = getResultForDrug(patientProgram, startDate, endDate, concept);
                if (!result.getStatus().equals(Status.DATA_ADDED)) {
                    return result;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Result(Status.DATA_ADDED, null);
    }

    private Result getResultForDrug(PatientProgram patientProgram, Date startDate, Date endDate, Concept concept) throws ParseException {
        List<BahmniDrugOrder> bahmniDrugOrders = bahmniDrugOrderService.getDrugOrders(patientProgram.getPatient().getUuid(), null, new HashSet<>(Arrays.asList(concept)), null, patientProgram.getUuid());
        if (bahmniDrugOrders != null) {
            for (BahmniDrugOrder drug : bahmniDrugOrders) {
                Date drugStartDate = drug.getEffectiveStartDate();
                Date drugStopDate = drug.getEffectiveStopDate() != null ? drug.getEffectiveStopDate() : new Date();
                if ((startDate.before(drugStopDate) || startDate.equals(drugStopDate)) &&
                        (endDate.after(drugStartDate) || endDate.equals(drugStartDate))) {
                    return new Result(Status.DATA_ADDED, null);
                }
            }
        }
        if (endDate.before(new Date())) {
            return new Result(Status.PENDING, null);
        }
        return new Result(Status.PLANNED, null);
    }
}
