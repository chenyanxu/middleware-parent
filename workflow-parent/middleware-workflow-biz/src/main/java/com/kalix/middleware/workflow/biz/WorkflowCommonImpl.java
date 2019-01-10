package com.kalix.middleware.workflow.biz;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.kalix.middleware.workflow.api.biz.IFormHandler;
import com.kalix.middleware.workflow.api.biz.IWorkflowCommon;
import com.kalix.middleware.workflow.api.exception.FormProcessException;
import com.kalix.middleware.workflow.api.model.FormDTO;
import com.kalix.middleware.workflow.engine.manager.FormManager;
import org.activiti.engine.FormService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;


/**
 * Created by sunlf on 2015/7/30.
 * 工作流通用业务服务实现类
 */
public class WorkflowCommonImpl implements IWorkflowCommon {
    private FormService formService;
    private RepositoryService repositoryService;
    private TaskService taskService;

    public void setFormService(FormService formService) {
        this.formService = formService;
    }

    public void setRepositoryService(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public FormDTO getForm(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        //获得流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
        //获得任务form数据
        TaskFormData taskFormData = formService.getTaskFormData(taskId);
        IFormHandler formHandler = FormManager.getInstall().findFormByKey(processDefinition.getKey(), taskFormData.getFormKey());

        if (formHandler != null) {
            Mapper mapper = DozerBeanMapperBuilder.buildDefault();
            FormDTO formDTO = mapper.map(formHandler, FormDTO.class);
            return formDTO;
        } else
            throw new FormProcessException();
    }

    @Override
    public FormDTO getBizDataForm(String processDefinitionId) {
        if (processDefinitionId != null) {
            String[] splits = processDefinitionId.split(":");

            if (splits.length > 0) {
                IFormHandler formHandler = FormManager.getInstall().findFormByKey(splits[0], "bizData.form");

                if (formHandler != null) {
                    Mapper mapper = DozerBeanMapperBuilder.buildDefault();
                    FormDTO formDTO = mapper.map(formHandler, FormDTO.class);

                    return formDTO;
                }
            }
        }


        return null;
    }
}
