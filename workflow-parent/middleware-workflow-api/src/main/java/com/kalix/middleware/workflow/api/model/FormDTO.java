package com.kalix.middleware.workflow.api.model;

import com.kalix.framework.core.api.web.model.BaseDTO;

/**
 * Created by sunlf on 2015/7/30.
 * 用于返回extjs的表单DTO
 */
public class FormDTO extends BaseDTO {
    private String formKey;
    private String processDefinitionId;
    private String formClass;
    private String windowClass;

    public String getFormKey() {
        return formKey;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getFormClass() {
        return formClass;
    }

    public void setFormClass(String formClass) {
        this.formClass = formClass;
    }

    public String getWindowClass() {
        return windowClass;
    }

    public void setWindowClass(String windowClass) {
        this.windowClass = windowClass;
    }
}
