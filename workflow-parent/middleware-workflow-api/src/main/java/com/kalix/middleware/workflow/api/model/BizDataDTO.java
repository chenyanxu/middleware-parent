package com.kalix.middleware.workflow.api.model;

import com.kalix.framework.core.api.web.model.BaseDTO;

/**
 * Created by sunlf on 2015/7/30.
 */
public class BizDataDTO extends BaseDTO {
    private String bizName;
    private String processDefinitionId;
    private String componentClass;

    public String getBizName() {
        return bizName;
    }

    public void setBizName(String bizName) {
        this.bizName = bizName;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getComponentClass() {
        return componentClass;
    }

    public void setComponentClass(String componentClass) {
        this.componentClass = componentClass;
    }
}
