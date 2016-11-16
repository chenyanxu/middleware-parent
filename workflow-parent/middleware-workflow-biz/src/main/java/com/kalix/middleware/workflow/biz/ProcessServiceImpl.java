package com.kalix.middleware.workflow.biz;

import com.kalix.framework.core.api.persistence.JsonData;
import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.api.security.IShiroService;
import com.kalix.framework.core.util.*;
import com.kalix.middleware.workflow.api.biz.IProcessService;
import com.kalix.middleware.workflow.api.model.HistoricActivityInstanceDTO;
import com.kalix.middleware.workflow.api.model.HistoricProcessInstanceDTO;
import com.kalix.middleware.workflow.api.model.ProcessDefinitionDTO;
import com.kalix.middleware.workflow.api.util.WorkflowUtil;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunlf on 2015/7/30.
 */
public class ProcessServiceImpl implements IProcessService {
    private transient RepositoryService repositoryService;
    private IShiroService shiroService;
    private RuntimeService runtimeService;
    private HistoryService historyService;
    private TaskService taskService;
    private JsonData jsonData = new JsonData();


    /**
     * 获得流程定义列表
     *
     * @return
     */
    @Override
    public JsonData getProcessDefinition(int page, int limit, String jsonStr) {
        List<ProcessDefinitionDTO> processDefinitionDTOList;
        List<ProcessDefinition> processDefinitionList = null;
        //按照流程定义名称模糊查询
        if (StringUtils.isNotEmpty(jsonStr) && (!jsonStr.equals(""))) {
            Map map = SerializeUtil.json2Map(jsonStr);
            String processDefinitionName = (String) map.get("name");
            Assert.notNull(processDefinitionName);
            if (processDefinitionName.isEmpty()) {
                processDefinitionList = repositoryService.createProcessDefinitionQuery().latestVersion().listPage((page - 1) * limit, limit);
            } else {
                processDefinitionList = repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionVersion().asc()
                        .processDefinitionNameLike("%" + processDefinitionName + "%").listPage((page - 1) * limit, limit);
                Map<String, ProcessDefinition> mapProcess = new LinkedHashMap<String, ProcessDefinition>();
                if (processDefinitionList != null && processDefinitionList.size() > 0) {
                    for (ProcessDefinition pd : processDefinitionList) {
                        mapProcess.put(pd.getKey(), pd);
                    }
                }
                processDefinitionList = new ArrayList<ProcessDefinition>(mapProcess.values());
            }
        }

        if (processDefinitionList != null) {
            Mapper mapper = new DozerBeanMapper();
            processDefinitionDTOList = DozerHelper.map(mapper, processDefinitionList, ProcessDefinitionDTO.class);
            jsonData.setTotalCount((long) processDefinitionList.size());
            jsonData.setData(processDefinitionDTOList);
        }
        return jsonData;
    }


    /**
     * 暂停流程定义
     *
     * @param key
     * @return
     */
    public JsonStatus suspendProcessDefinition(String key) {
        JsonStatus jsonStatus = new JsonStatus();
        jsonStatus.setSuccess(true);
        try {
            /*ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            RepositoryService repositoryService = processEngine.getRepositoryService();*/

            repositoryService.suspendProcessDefinitionByKey(key);
            jsonStatus.setMsg("暂停流程成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonStatus.setSuccess(false);
            jsonStatus.setMsg("暂停流程失败");
        }
        return jsonStatus;
    }

    /**
     * 获得我的流程列表
     *
     * @return
     */
    @Override
    public JsonData getMyProcessHistory(int page, int limit, String jsonStr) {
        String loginUser = this.shiroService.getCurrentUserLoginName();
        List<HistoricProcessInstanceDTO> historicProcessDTOList;
        List<HistoricProcessInstance> processHistoryList;
        if (StringUtils.isNotEmpty(jsonStr)) {
            Map map = SerializeUtil.json2Map(jsonStr);
            //流程编号查询
            String processInstanceName = (String) map.get("name");
            if (StringUtils.isNotEmpty(processInstanceName))
                processHistoryList = historyService.createHistoricProcessInstanceQuery().processInstanceNameLike("%" + processInstanceName + "%")
                        .startedBy(loginUser).orderByProcessInstanceStartTime().desc().listPage((page - 1) * limit, limit);
            else {
                processHistoryList = historyService.createHistoricProcessInstanceQuery()
                        .startedBy(loginUser).orderByProcessInstanceStartTime().desc().listPage((page - 1) * limit, limit);
            }
        } else
            processHistoryList = historyService.createHistoricProcessInstanceQuery().startedBy(loginUser)
                    .orderByProcessInstanceStartTime().desc().listPage((page - 1) * limit, limit);
        if (processHistoryList != null) {
            generateJsonData(processHistoryList);
        }
        return jsonData;

    }

    /**
     * 获得我参与的流程列表
     *
     * @return
     */
    @Override
    public JsonData getMyInvolvedProcessHistory(int page, int limit, String jsonStr) {
        String loginUser = this.shiroService.getCurrentUserLoginName();
        List<HistoricProcessInstanceDTO> historicProcessDTOList;
        List<HistoricProcessInstance> processHistoryList;
        if (StringUtils.isNotEmpty(jsonStr)) {
            Map map = SerializeUtil.json2Map(jsonStr);
            //流程编号查询
            String processInstanceName = (String) map.get("name");
            if (StringUtils.isNotEmpty(processInstanceName))
                processHistoryList = historyService.createHistoricProcessInstanceQuery().processInstanceNameLike("%" + processInstanceName + "%")
                        .involvedUser(loginUser).orderByProcessInstanceStartTime().desc().listPage((page - 1) * limit, limit);
            else {
                processHistoryList = historyService.createHistoricProcessInstanceQuery()
                        .involvedUser(loginUser).orderByProcessInstanceStartTime().desc().listPage((page - 1) * limit, limit);
            }
        } else
            processHistoryList = historyService.createHistoricProcessInstanceQuery().involvedUser(loginUser)
                    .orderByProcessInstanceStartTime().desc().listPage((page - 1) * limit, limit);
        if (processHistoryList != null) {
            generateJsonData(processHistoryList);
        }
        return jsonData;

    }


    /**
     * 获得流程历史列表
     *
     * @return
     */
    @Override
    public JsonData getProcessHistory(int page, int limit, String jsonStr) {
//        List<HistoricProcessInstanceDTO> historicProcessDTOList;
        List<HistoricProcessInstance> processHistoryList = null;
        if (StringUtils.isNotEmpty(jsonStr)) {
            Map map = SerializeUtil.json2Map(jsonStr);
            //流程编号查询
            String processInstanceName = (String) map.get("name");
            /*String startDate=(String) map.get("startDate");
            String endDate=(String) map.get("endDate");*/
            String startUser = (String) map.get("startUser");
            if (StringUtils.isNotEmpty(processInstanceName) && StringUtils.isNotEmpty(startUser))
                processHistoryList = historyService.createHistoricProcessInstanceQuery().processInstanceNameLike("%" + processInstanceName + "%")
                        .startedBy(startUser).orderByProcessInstanceStartTime().desc().listPage((page - 1) * limit, limit);
            else if (StringUtils.isNotEmpty(processInstanceName) && (!StringUtils.isNotEmpty(startUser))) {
                processHistoryList = historyService.createHistoricProcessInstanceQuery().processInstanceNameLike("%" + processInstanceName + "%")
                        .orderByProcessInstanceStartTime().desc().listPage((page - 1) * limit, limit);

            } else if (!StringUtils.isNotEmpty(processInstanceName) && (StringUtils.isNotEmpty(startUser))) {
                processHistoryList = historyService.createHistoricProcessInstanceQuery().startedBy(startUser)
                        .orderByProcessInstanceStartTime().desc().listPage((page - 1) * limit, limit);

            } else
                processHistoryList = historyService.createHistoricProcessInstanceQuery()
                        .orderByProcessInstanceStartTime().desc().listPage((page - 1) * limit, limit);
        }



        if (processHistoryList != null) {
            generateJsonData(processHistoryList);
        }
        return jsonData;

    }

    /**
     * 获得流程历史节点信息
     *
     * @param historyProcessId
     * @return
     */
    @Override
    public JsonData getHistoricActivity(String historyProcessId) {
        List<HistoricActivityInstanceDTO> historicActivityDTOList;
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().processInstanceId(historyProcessId).list();
        for (int i = list.size(); i > 0; i--) {
            if (null == list.get(i - 1).getTaskId()) {
                list.remove(i - 1);
            }
        }

        if (list != null) {
            Mapper mapper = new DozerBeanMapper();
            historicActivityDTOList = DozerHelper.map(mapper, list, HistoricActivityInstanceDTO.class);
            for (HistoricActivityInstanceDTO historicActivityInstance : historicActivityDTOList) {
                String str = "";
                List<Comment> commentList = taskService.getTaskComments(historicActivityInstance.getTaskId());
                for (Comment comment : commentList) {
                    str = comment.getFullMessage() + str + " ";
                }
                //替换userid为usename
//                historicActivityInstance.setAssignee(commentList.get(0).getUserId());
                historicActivityInstance.setComment(str);
                if (historicActivityInstance.getDurationInMillis() != null)
                    historicActivityInstance.setDurationInMillis(DateUtil.formatDuring(Long.parseLong(historicActivityInstance.getDurationInMillis())));
            }

            jsonData.setTotalCount((long) historicActivityDTOList.size());
            jsonData.setData(historicActivityDTOList);
        }
        return jsonData;
    }

    /**
     * 启动流程定义
     *
     * @param key
     * @return
     */
    public JsonStatus activateProcessDefinition(String key) {
        JsonStatus jsonStatus = new JsonStatus();
        jsonStatus.setSuccess(true);
        try {
            /*ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            RepositoryService repositoryService = processEngine.getRepositoryService();*/
            repositoryService.activateProcessDefinitionByKey(key);
            jsonStatus.setMsg("启动流程成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonStatus.setSuccess(false);
            jsonStatus.setMsg("启动流程失败");
        }
        return jsonStatus;
    }

    private void generateJsonData(List<HistoricProcessInstance> processHistoryList) {
        List<HistoricProcessInstanceDTO> historicProcessDTOList;
        Mapper mapper = new DozerBeanMapper();
        historicProcessDTOList = DozerHelper.map(mapper, processHistoryList, HistoricProcessInstanceDTO.class);
        //设置流程状态
        for (HistoricProcessInstanceDTO dto : historicProcessDTOList) {
            if (dto.getEndTime() != null) {
                dto.setStatus("<a style='color:red'>结束</a>");
            } else {
                dto.setStatus("进行中");
            }
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(dto.getProcessInstanceId()).singleResult();
            if (processInstance != null) {
                dto.setEntityId(WorkflowUtil.getBizId(processInstance.getBusinessKey()));
            } else {

                HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(dto.getProcessInstanceId()).singleResult();
                if (historicProcessInstance != null)
                    dto.setEntityId(WorkflowUtil.getBizId(historicProcessInstance.getBusinessKey()));
            }
            if (dto.getDurationInMillis() != null) {
                dto.setDurationInMillis(DateUtil.formatDuring(Long.decode(dto.getDurationInMillis())));
            }
        }
        long count = processHistoryList.size();
        jsonData.setTotalCount(count);
        jsonData.setData(historicProcessDTOList);
    }

    public void setRuntimeService(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public void setRepositoryService(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }

    public void setShiroService(IShiroService shiroService) {
        this.shiroService = shiroService;
    }
}
