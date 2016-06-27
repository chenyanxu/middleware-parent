package com.kalix.middleware.workflow.api.biz;

import com.kalix.framework.core.api.IService;
import com.kalix.middleware.workflow.api.model.BizDataDTO;
import com.kalix.middleware.workflow.api.model.FormDTO;

/**
 * Created by sunlf on 2015/7/30.
 * 用于获得Form和BizData数据的服务
 */
public interface IWorkflowCommon extends IService {
    FormDTO getForm(String taskId);
    FormDTO getBizDataForm(String processDefinitionId);
}
