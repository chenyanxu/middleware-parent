package com.kalix.middleware.workflow.api.biz;

import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.middleware.workflow.api.model.JsonXml;
import org.activiti.engine.task.Task;

import java.util.Date;
import java.util.List;

/**
 * @类描述：Workflow业务服务接口类
 * @创建人： sunlingfeng
 * @创建时间：2014/9/9
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
public interface IWorkflowService {

    void findAllProcessDeployTimes();

    void findAllProcessDefinitions();

    void findAllLastVesions();

    void deleteProcessDefinitionById();

    void getProcessDefinitionContent() throws Exception;

    void getProcessDefinitionContentPng() throws Exception;

    void createProcessInstance();

    void findAllTasks();

    List<Task> findAllTasksWithDueDate(Date afterDate, Date beforeDate, String userId);

    void findPersonalTasks();

    void findCandidateUserTasks();

    void findCandidateGroupTasks();

    void findDateRangeTasks();

    void claimTask();

    void handleTask();

    void getVariableByTaskInstanceId();

    void getAllExecuteTaskNodeList();

    void findExecutionList();

    void findProcessInstanceList();

    JsonStatus deploy(JsonXml jsonXml);
}
