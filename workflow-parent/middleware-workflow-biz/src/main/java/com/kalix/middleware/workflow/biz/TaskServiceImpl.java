package com.kalix.middleware.workflow.biz;

import com.kalix.framework.core.api.persistence.JsonData;
import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.api.security.IShiroService;
import com.kalix.framework.core.util.*;
import com.kalix.middleware.workflow.api.biz.ITaskService;
import com.kalix.middleware.workflow.api.model.TaskDTO;
import com.kalix.middleware.workflow.api.util.WorkflowUtil;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sunlf on 2015/7/31.
 * 任务服务的实现类
 */
public class TaskServiceImpl implements ITaskService {
    private TaskService taskService;
    private RuntimeService runtimeService;
    private HistoryService historyService;
    private JsonData jsonData = new JsonData();
    private IShiroService shiroService;

    /**
     * 获得工作流任务列表
     *
     * @return
     */
    @Override
    public JsonData getTasks(int page, int limit, String jsonStr) {
        //获得当前登陆用户
        String userName = this.shiroService.getCurrentUserLoginName();
        List<TaskDTO> taskDTOList;
        List<Task> taskGroupList = new ArrayList<>();//获得用户组的任务列表
        List<Task> taskUserList;//获得基于用户的任务列表
        //获得该用户的职位，职位组成标准：orgName-dutyName
        String rtnStr = null;
        try {
            rtnStr = HttpClientUtil.shiroGet("/users/user/dutys/list", this.shiroService.getSession().getId().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> dutyNameList = null;

        if (rtnStr != null) {
            dutyNameList = SerializeUtil.unserializeJson(rtnStr, List.class);
        } else {
            dutyNameList = new ArrayList<>();
        }
        //获得查询条件
        Map map = SerializeUtil.json2Map(jsonStr);
        String taskName = (String) map.get("name");
        //Assert.notNull(taskName);
        if (StringUtils.isNotEmpty(taskName)) {
            if (dutyNameList.size() > 0)
                taskGroupList = taskService
                        .createTaskQuery().taskCandidateGroupIn(dutyNameList)
                        .taskNameLike("%" + taskName + "%").orderByTaskCreateTime().desc()
                        .listPage((page - 1) * limit, limit);

            taskUserList = taskService
                    .createTaskQuery().taskCandidateUser(userName)
                    .taskNameLike("%" + taskName + "%").orderByTaskCreateTime().desc()
                    .listPage((page - 1) * limit, limit);
        } else {
            taskUserList = taskService
                    .createTaskQuery().taskCandidateUser(userName)
                    .orderByTaskCreateTime().desc()
                    .listPage((page - 1) * limit, limit);
            if (dutyNameList.size() > 0)
                taskGroupList = taskService
                        .createTaskQuery().taskCandidateGroupIn(dutyNameList)
                        .orderByTaskCreateTime().desc()
                        .listPage((page - 1) * limit, limit);

        }

        taskGroupList.addAll(taskUserList);

        if (taskGroupList != null) {
            Mapper mapper = new DozerBeanMapper();
            taskDTOList = DozerHelper.map(mapper, taskGroupList, TaskDTO.class);
            jsonData.setTotalCount((long) taskGroupList.size());
            //获得业务实体id
            for (TaskDTO dto : taskDTOList) {
                ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(dto.getProcessInstanceId()).singleResult();
                if (processInstance != null) {
                    dto.setEntityId(WorkflowUtil.getBizId(processInstance.getBusinessKey()));
                    dto.setBusinessNo(processInstance.getName());
                    dto.setBusinessKey(processInstance.getBusinessKey());
                } else {
                    HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(dto.getProcessInstanceId()).singleResult();

                    if (historicProcessInstance != null) {
                        dto.setEntityId(WorkflowUtil.getBizId(historicProcessInstance.getBusinessKey()));
                        dto.setBusinessNo(processInstance.getName());
                        dto.setBusinessKey(processInstance.getBusinessKey());
                    }
                }
                if (dto.getDuration() != null)
                    dto.setDuration(DateUtil.formatDuring(Long.parseLong(dto.getDuration())));
            }
            jsonData.setData(taskDTOList);
        }
        return jsonData;
    }

    /**
     * 获得流程启动用户id
     *
     * @param processInstanceId 流程实例id
     * @return
     */
    public String getStartUserName(String processInstanceId) {
        String userName = "";
        HistoricProcessInstance historicProcessInstance;
        historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (historicProcessInstance != null)
            userName = historicProcessInstance.getStartUserId();
        return userName;
    }

    @Override
    public JsonStatus delegateTask(String taskIds, String userId) {
        JsonStatus jsonStatus = new JsonStatus();
        try {

            jsonStatus.setSuccess(true);
            String[] taskId = taskIds.split(":");
            for (String id : taskId) {
                taskService.claim(id, this.shiroService.getCurrentUserLoginName());
                taskService.delegateTask(id, userId);
            }
            jsonStatus.setMsg("委托任务处理成功！");
            return jsonStatus;
        } catch (Exception e) {
            e.printStackTrace();
            jsonStatus.setFailure(true);
            jsonStatus.setSuccess(false);
            jsonStatus.setMsg("委托任务处理失败！");
        }
        return jsonStatus;

    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public void setRuntimeService(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }

    public void setShiroService(IShiroService shiroService) {
        this.shiroService = shiroService;
    }
}
