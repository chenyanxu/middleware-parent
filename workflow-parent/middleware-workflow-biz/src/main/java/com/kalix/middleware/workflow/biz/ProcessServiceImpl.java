package com.kalix.middleware.workflow.biz;

import com.kalix.framework.core.api.persistence.JsonData;
import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.api.security.IShiroService;
import com.kalix.framework.core.util.*;
import com.kalix.middleware.workflow.api.Const;
import com.kalix.middleware.workflow.api.biz.IProcessService;
import com.kalix.middleware.workflow.api.model.HistoricActivityInstanceDTO;
import com.kalix.middleware.workflow.api.model.HistoricProcessInstanceDTO;
import com.kalix.middleware.workflow.api.model.ProcessDefinitionDTO;
import com.kalix.middleware.workflow.api.util.WorkflowUtil;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.persistence.entity.CommentEntity;
import org.activiti.engine.impl.persistence.entity.HistoricVariableInstanceEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.IdentityLink;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.util.*;
import java.util.stream.Collectors;

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

    private ProcessEngine processEngine;

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
        } else {
            processDefinitionList = repositoryService.createProcessDefinitionQuery().latestVersion().listPage((page - 1) * limit, limit);
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
        HistoricProcessInstanceQuery query=null;
        long count = 0;
        if (StringUtils.isNotEmpty(jsonStr)) {
            Map map = SerializeUtil.json2Map(jsonStr);
            //流程编号查询
            String processInstanceName = (String) map.get("name");

            if (StringUtils.isNotEmpty(processInstanceName)) {
                query=historyService.createHistoricProcessInstanceQuery().processInstanceNameLike("%" + processInstanceName + "%")
                        .startedBy(loginUser).orderByProcessInstanceStartTime().desc();
            }
            else {
                query=historyService.createHistoricProcessInstanceQuery().startedBy(loginUser).
                        orderByProcessInstanceStartTime().desc();
            }
        } else {
            query=historyService.createHistoricProcessInstanceQuery().startedBy(loginUser)
                    .orderByProcessInstanceStartTime().desc();
        }

        processHistoryList=query.listPage((page - 1) * limit, limit);

        if (processHistoryList != null) {
            generateJsonData(processHistoryList,query.count());
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
        HistoricProcessInstanceQuery query=null;
        if (StringUtils.isNotEmpty(jsonStr)) {
            Map map = SerializeUtil.json2Map(jsonStr);
            //流程编号查询
            String processInstanceName = (String) map.get("name");
            //流程启动用户查询条件
            String startUser = (String) map.get("startUser");
            if (StringUtils.isNotEmpty(processInstanceName) && StringUtils.isNotEmpty(startUser)) {
                query = historyService.createHistoricProcessInstanceQuery().involvedUser(loginUser)
                        .processInstanceNameLike("%" + processInstanceName + "%").startedBy(startUser)
                        .orderByProcessInstanceStartTime().desc();
            } else if (StringUtils.isNotEmpty(processInstanceName) && !StringUtils.isNotEmpty(startUser)) {
                query = historyService.createHistoricProcessInstanceQuery().involvedUser(loginUser)
                        .processInstanceNameLike("%" + processInstanceName + "%")
                        .orderByProcessInstanceStartTime().desc();
            } else if (!StringUtils.isNotEmpty(processInstanceName) && StringUtils.isNotEmpty(startUser)) {
                query = historyService.createHistoricProcessInstanceQuery().involvedUser(loginUser)
                        .startedBy(startUser).orderByProcessInstanceStartTime().desc();
            } else {
                query = historyService.createHistoricProcessInstanceQuery().involvedUser(loginUser)
                        .orderByProcessInstanceStartTime().desc();
            }
        } else {
            query = historyService.createHistoricProcessInstanceQuery().involvedUser(loginUser)
                    .orderByProcessInstanceStartTime().desc();
        }
        processHistoryList = query.listPage((page - 1) * limit, limit);
        if (processHistoryList != null) {
            generateJsonData(processHistoryList,query.count());
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
        HistoricProcessInstanceQuery query=null;
        if (StringUtils.isNotEmpty(jsonStr)) {
            Map map = SerializeUtil.json2Map(jsonStr);
            //流程编号查询
            String processInstanceName = (String) map.get("name");
            /*String startDate=(String) map.get("startDate");
            String endDate=(String) map.get("endDate");*/
            String startUser = (String) map.get("startUser");
            if (StringUtils.isNotEmpty(processInstanceName) && StringUtils.isNotEmpty(startUser)) {
                query = historyService.createHistoricProcessInstanceQuery()
                        .processInstanceNameLike("%" + processInstanceName + "%").startedBy(startUser)
                        .orderByProcessInstanceStartTime().desc();

                // 自定义查询(表act_hi_procinst)
                /*String sql = "SELECT * FROM " + processEngine.getManagementService().getTableName(HistoricProcessInstance.class) + " T " +
                        " WHERE T.NAME_ LIKE '%" + processInstanceName + "%' AND T.START_USER_ID_ LIKE '%" + startUser + "%' " +
                        " ORDER BY T.START_TIME_ DESC ";
                        //" AND ROWNUM<=" + num + " ORDER BY T.START_TIME_ DESC ";
                NativeHistoricProcessInstanceQuery nhpiq = historyService.createNativeHistoricProcessInstanceQuery().sql(sql);
                processHistoryList = nhpiq.listPage((page - 1) * limit, limit);*/
            } else if (StringUtils.isNotEmpty(processInstanceName) && (!StringUtils.isNotEmpty(startUser))) {
                query = historyService.createHistoricProcessInstanceQuery()
                        .processInstanceNameLike("%" + processInstanceName + "%")
                        .orderByProcessInstanceStartTime().desc();
            } else if (!StringUtils.isNotEmpty(processInstanceName) && (StringUtils.isNotEmpty(startUser))) {
                query = historyService.createHistoricProcessInstanceQuery()
                        .startedBy(startUser).orderByProcessInstanceStartTime().desc();
            } else {
                query = historyService.createHistoricProcessInstanceQuery()
                        .orderByProcessInstanceStartTime().desc();
            }
        } else {
            query = historyService.createHistoricProcessInstanceQuery()
                    .orderByProcessInstanceStartTime().desc();
        }

        processHistoryList = query.listPage((page - 1) * limit, limit);
        if (processHistoryList != null) {
            generateJsonData(processHistoryList,query.count());
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
                List<Comment> commentList =taskService.getTaskComments(historicActivityInstance.getTaskId());
                for (Comment comment : commentList) {
                    str = ((CommentEntity)comment).getMessage() + str + " ";
                }
                List<HistoricVariableInstance> varList = historyService.createHistoricVariableInstanceQuery()
                        .processInstanceId(historyProcessId).taskId(historicActivityInstance.getTaskId()).list();
                for (HistoricVariableInstance var : varList) {
                    HistoricVariableInstanceEntity varEntity = (HistoricVariableInstanceEntity) var;
                    if (varEntity.getName().equals(Const.VAR_ACCEPTED)) {
                        String result = varEntity.getTextValue();
                        historicActivityInstance.setResult(result);
                        break;
                    }
                }

                //替换userid为usename
//                historicActivityInstance.setAssignee(commentList.get(0).getUserId());
                if (historicActivityInstance.getAssignee() == null) {
                    //获得任务的执行人
//                    HistoricTaskInstance historyTask= historyService.createHistoricTaskInstanceQuery().taskId(historicActivityInstance.getTaskId()).singleResult();
                    try {
                        List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(historicActivityInstance.getTaskId());
                        String _str = "";
                        for (IdentityLink id : identityLinks) {
                            if (id.getUserId() != null)
                                _str = _str + id.getUserId();
                            else
                                _str = "无";
                        }
                        historicActivityInstance.setAssignee(_str);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

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

    private void generateJsonData(List<HistoricProcessInstance> processHistoryList,long count) {
        List<HistoricProcessInstanceDTO> historicProcessDTOList;
        Mapper mapper = new DozerBeanMapper();
        historicProcessDTOList = DozerHelper.map(mapper, processHistoryList, HistoricProcessInstanceDTO.class);
        //设置流程状态
        for (HistoricProcessInstanceDTO dto : historicProcessDTOList) {
            if (dto.getEndTime() != null) {
//                dto.setStatus("<a style='color:red'>结束</a>");
                dto.setStatus("结束");
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

            //create title
            List<HistoricVariableInstance> varList = historyService.createHistoricVariableInstanceQuery()
                    .processInstanceId(dto.getProcessInstanceId()).list();
            for (HistoricVariableInstance var : varList) {
                HistoricVariableInstanceEntity varEntity = (HistoricVariableInstanceEntity) var;
                if (varEntity.getName().equals(Const.VAR_TITLE)) {
                    dto.setTitle(varEntity.getTextValue());
                    break;
                }
            }

            if (dto.getDurationInMillis() != null) {
                dto.setDurationInMillis(DateUtil.formatDuring(Long.decode(dto.getDurationInMillis())));
            }
        }
        //long count = processHistoryList.size();
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

    public void setProcessEngine(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }
    public JsonData getAllProcessNameAndKey() {
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().latestVersion().list();
        List<Map<String,String>> list = new ArrayList<>();
        if (processDefinitionList != null && !processDefinitionList.isEmpty()) {
            // 去掉集合中重复name的数据
            List<ProcessDefinition> uniqueDefinitionList = processDefinitionList.stream().collect(
                    Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ProcessDefinition::getKey))), ArrayList::new)
            );
            for (ProcessDefinition definition : uniqueDefinitionList) {
                Map<String, String> nameAndKey = new HashMap<>();
                nameAndKey.put("label", definition.getName());
                nameAndKey.put("value", definition.getKey());
                list.add(nameAndKey);
            }
        }
        jsonData.setData(list);
        return jsonData;
    }
}
