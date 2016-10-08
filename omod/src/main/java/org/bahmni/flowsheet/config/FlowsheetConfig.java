package org.bahmni.flowsheet.config;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.openmrs.module.endtb.flowsheet.constants.FlowsheetConstant;

import java.util.*;

public class FlowsheetConfig {
    private List<MilestoneConfig> milestoneConfigs;
    private TreeMap<String, String> questionNames;

    public TreeMap<String, String> getQuestionNames() {
        return questionNames;
    }

    @JsonProperty(FlowsheetConstant.ORDER)
    public void setQuestionNames(TreeMap<String, String> questionNames) {
        this.questionNames = questionNames;
    }

    @JsonProperty(FlowsheetConstant.MILESTONES)
    public List<MilestoneConfig> getMilestoneConfigs() {
        return milestoneConfigs;
    }

    public void setMilestoneConfigs(List<MilestoneConfig> milestoneConfigs) {
        this.milestoneConfigs = milestoneConfigs;
    }
}
