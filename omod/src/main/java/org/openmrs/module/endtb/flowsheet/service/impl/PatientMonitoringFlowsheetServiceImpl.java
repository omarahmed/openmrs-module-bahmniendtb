package org.openmrs.module.endtb.flowsheet.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.bahmni.flowsheet.api.Evaluator;
import org.bahmni.flowsheet.api.Status;
import org.bahmni.flowsheet.api.models.Flowsheet;
import org.bahmni.flowsheet.api.models.Milestone;
import org.bahmni.flowsheet.api.models.Question;
import org.bahmni.flowsheet.api.models.Result;
import org.bahmni.flowsheet.config.Config;
import org.bahmni.flowsheet.config.FlowsheetConfig;
import org.bahmni.flowsheet.config.MilestoneConfig;
import org.bahmni.flowsheet.config.QuestionConfig;
import org.bahmni.flowsheet.definition.HandlerProvider;
import org.bahmni.flowsheet.definition.models.FlowsheetDefinition;
import org.bahmni.flowsheet.definition.models.MilestoneDefinition;
import org.bahmni.flowsheet.definition.models.QuestionDefinition;
import org.bahmni.module.bahmnicore.dao.ObsDao;
import org.bahmni.module.bahmnicore.dao.OrderDao;
import org.bahmni.module.bahmnicore.model.bahmniPatientProgram.BahmniPatientProgram;
import org.bahmni.module.bahmnicore.model.bahmniPatientProgram.PatientProgramAttribute;
import org.bahmni.module.bahmnicore.service.BahmniConceptService;
import org.openmrs.*;
import org.openmrs.module.bahmniendtb.EndTBConstants;
import org.openmrs.module.endtb.flowsheet.models.FlowsheetAttribute;
import org.bahmni.flowsheet.ui.FlowsheetUI;
import org.openmrs.module.endtb.flowsheet.service.PatientMonitoringFlowsheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
@Scope("prototype")
public class PatientMonitoringFlowsheetServiceImpl implements PatientMonitoringFlowsheetService {

    private OrderDao orderDao;
    private ObsDao obsDao;
    private BahmniConceptService bahmniConceptService;
    private HandlerProvider handlerProvider;
    private List<Evaluator> evaluatorList;


    @Autowired
    public PatientMonitoringFlowsheetServiceImpl(OrderDao orderDao, ObsDao obsDao, BahmniConceptService bahmniConceptService, HandlerProvider handlerProvider, List<Evaluator> evaluatorList) {
        this.orderDao = orderDao;
        this.obsDao = obsDao;
        this.bahmniConceptService = bahmniConceptService;
        this.handlerProvider = handlerProvider;
        this.evaluatorList = evaluatorList;
    }

    @Override
    public FlowsheetUI getFlowsheetForPatientProgram(PatientProgram patientProgram, Date startDate, Date endDate, String configFilePath) throws Exception {
        FlowsheetConfig flowsheetConfig = getFlowsheetConfig(configFilePath);
        FlowsheetDefinition flowsheetDefinition = getFlowsheetDefinitionFromConfig(flowsheetConfig, startDate);
        Flowsheet flowsheet = flowsheetDefinition.createFlowsheet(patientProgram);
        flowsheet.evaluate(patientProgram);


        Set<Milestone> milestones = flowsheet.getMilestones();

        Set<String> flowsheetHeaders = new LinkedHashSet<>();
        for (Milestone milestone : milestones) {
            flowsheetHeaders.add(milestone.getName());
        }

        for (Milestone milestone : milestones) {
            for (Question question : milestone.getQuestions()) {
                if (endDate != null  && milestone.getStartDate().after(endDate) && !question.getResult().getStatus().equals(Status.DATA_ADDED)) {
                    question.setResult(new Result(Status.NOT_APPLICABLE, null));
                }
            }
        }

        TreeMap<String, String> map = flowsheetConfig.getQuestionNames();
        Set<String> questionNames = new LinkedHashSet<>();
        for (String key : map.keySet()) {
            questionNames.add(map.get(key));
        }

        Map<String, List<String>> flowsheetData = new LinkedHashMap<>();
        for (String questionName : questionNames) {
            List<String> colorCodes = new LinkedList<>();
            for (Milestone milestone : milestones) {
                Question milestoneQuestion = getQuestionFromSet(milestone.getQuestions(), questionName);
                if(milestoneQuestion == null) {
                    colorCodes.add("grey");
                }
                else {
                    colorCodes.add(getColorCodeForStatus(milestoneQuestion.getResult().getStatus()));
                }
            }
            flowsheetData.put(questionName, colorCodes);
        }
        FlowsheetUI presentationFlowsheet = new FlowsheetUI();
        presentationFlowsheet.setFlowsheetHeader(flowsheetHeaders);
        presentationFlowsheet.setHighlightedMilestone(findHighlightedMilestoneName(milestones, endDate));
        presentationFlowsheet.setFlowsheetData(flowsheetData);
        return presentationFlowsheet;
    }


    private String getColorCodeForStatus(Status status) {
        if (status.equals(Status.DATA_ADDED)) {
            return "green";
        }
        if (status.equals(Status.PLANNED)) {
            return "yellow";
        }
        if (status.equals(Status.PENDING)) {
            return "purple";
        }
        if (status.equals(Status.NOT_APPLICABLE)) {
            return "grey";
        }
        return "grey";
    }

    public FlowsheetDefinition getFlowsheetDefinitionFromConfig(FlowsheetConfig flowsheetConfig, Date startDate) {

        Set<MilestoneDefinition> milestoneDefinitions = new LinkedHashSet<>();
        List<MilestoneConfig> milestoneConfigs = flowsheetConfig.getMilestoneConfigs();
        for (MilestoneConfig milestoneConfig : milestoneConfigs) {
            Config config = milestoneConfig.getConfig();
            Map<String, String> configMap = new LinkedHashMap<>();
            configMap.put("min", config.getMin());
            configMap.put("max", config.getMax());
            MilestoneDefinition milestoneDefinition = new MilestoneDefinition(milestoneConfig.getName(), configMap, milestoneConfig.getHandler(), handlerProvider, evaluatorList);

            Set<QuestionDefinition> questionDefinitions = new LinkedHashSet<>();
            for (QuestionConfig questionConfig : milestoneConfig.getQuestionConfigList()) {
                Set<Concept> conceptSet = new LinkedHashSet<>();
                for (String conceptName : questionConfig.getConcepts()) {
                    conceptSet.add(bahmniConceptService.getConceptByFullySpecifiedName(conceptName));
                }
                questionDefinitions.add(new QuestionDefinition(questionConfig.getName(), conceptSet, questionConfig.getType()));
            }
            milestoneDefinition.setQuestionDefinitions(questionDefinitions);
            milestoneDefinitions.add(milestoneDefinition);
        }
        return new FlowsheetDefinition(startDate, milestoneDefinitions);
    }

    public Question getQuestionFromSet(Set<Question> questions, String name) {
        for (Question question : questions) {
            if (question.getName().equals(name))
                return question;
        }
        return null;
    }

    @Override
    public FlowsheetAttribute getFlowsheetAttributesForPatientProgram(BahmniPatientProgram bahmniPatientProgram, PatientIdentifierType primaryIdentifierType, OrderType orderType, Set<Concept> concepts) {
        FlowsheetAttribute flowsheetAttribute = new FlowsheetAttribute();
        List<Obs> startDateConceptObs = obsDao.getObsByPatientProgramUuidAndConceptNames(bahmniPatientProgram.getUuid(), Arrays.asList(EndTBConstants.TI_TREATMENT_START_DATE), null, null, null, null);
        Date startDate = null;
        if (CollectionUtils.isNotEmpty(startDateConceptObs)) {
            startDate = startDateConceptObs.get(0).getValueDate();
        }
        Date newDrugTreatmentStartDate = getNewDrugTreatmentStartDate(bahmniPatientProgram.getUuid(), orderType, concepts);
        flowsheetAttribute.setNewDrugTreatmentStartDate(newDrugTreatmentStartDate);
        flowsheetAttribute.setMdrtbTreatmentStartDate(startDate);
        flowsheetAttribute.setTreatmentRegistrationNumber(getProgramAttribute(bahmniPatientProgram, EndTBConstants.PROGRAM_ATTRIBUTE_REG_NO));
        flowsheetAttribute.setPatientEMRID(bahmniPatientProgram.getPatient().getPatientIdentifier(primaryIdentifierType).getIdentifier());
        return flowsheetAttribute;
    }

    @Override
    public Date getStartDateForDrugConcepts(String patientProgramUuid, Set<String> drugConcepts, OrderType orderType) {
        return getNewDrugTreatmentStartDate(patientProgramUuid, orderType, getConceptObjects(drugConcepts));
    }

    private Set<Concept> getConceptObjects(Set<String> conceptNames) {
        Set<Concept> conceptsList = new HashSet<>();
        for (String concept : conceptNames) {
            conceptsList.add(bahmniConceptService.getConceptByFullySpecifiedName(concept));
        }
        return conceptsList;
    }

    private Date getNewDrugTreatmentStartDate(String patientProgramUuid, OrderType orderType, Set<Concept> concepts) {
        List<Order> orders = orderDao.getOrdersByPatientProgram(patientProgramUuid, orderType, concepts);
        if (orders.size() > 0) {
            Order firstOrder = orders.get(0);
            Date newDrugTreatmentStartDate = firstOrder.getScheduledDate() != null ? firstOrder.getScheduledDate() : firstOrder.getDateActivated();
            for (Order order : orders) {
                Date toCompare = order.getScheduledDate() != null ? order.getScheduledDate() : order.getDateActivated();
                if (newDrugTreatmentStartDate.compareTo(toCompare) > 0) {
                    newDrugTreatmentStartDate = toCompare;
                }
            }
            return newDrugTreatmentStartDate;
        }
        return null;
    }

    private String getProgramAttribute(BahmniPatientProgram bahmniPatientProgram, String attribute) {
        for (PatientProgramAttribute patientProgramAttribute : bahmniPatientProgram.getActiveAttributes()) {
            if (patientProgramAttribute.getAttributeType().getName().equals(attribute))
                return patientProgramAttribute.getValueReference();
        }
        return "";
    }

    private String findHighlightedMilestoneName(Set<Milestone> milestones, Date endDate) {
        if(endDate == null) {
            endDate = new Date();
        }
        for (Milestone milestone : milestones) {
            if (milestone.getStartDate().before(endDate) && milestone.getEndDate().after(endDate)) {
                return milestone.getName();
            }
        }
        return "";
    }

    private FlowsheetConfig getFlowsheetConfig(String configFilePath) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FlowsheetConfig flowsheetConfig = mapper.readValue(new File(configFilePath), FlowsheetConfig.class);
        return flowsheetConfig;
    }

}
