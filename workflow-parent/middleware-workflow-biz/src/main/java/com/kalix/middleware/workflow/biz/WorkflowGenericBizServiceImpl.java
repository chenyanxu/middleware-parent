package com.kalix.middleware.workflow.biz;

import com.google.gson.Gson;
import com.kalix.framework.core.api.dao.IGenericDao;
import com.kalix.framework.core.api.persistence.JsonData;
import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.api.security.IDataAuthService;
import com.kalix.framework.core.impl.biz.ShiroGenericBizServiceImpl;
import com.kalix.framework.core.util.DateUtil;
import com.kalix.framework.core.util.JNDIHelper;
import com.kalix.framework.core.util.SerializeUtil;
import com.kalix.middleware.workflow.api.Const;
import com.kalix.middleware.workflow.api.biz.IWorkflowBizService;
import com.kalix.middleware.workflow.api.exception.NotSameStarterException;
import com.kalix.middleware.workflow.api.exception.TaskProcessException;
import com.kalix.middleware.workflow.api.model.WorkflowEntity;
import com.kalix.middleware.workflow.api.model.WorkflowStaus;
import com.kalix.middleware.workflow.api.util.WorkflowUtil;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.Task;

import javax.transaction.Transactional;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunlf on 2016-03-03.
 * 工作流抽象类，封装通用方法
 */
public abstract class WorkflowGenericBizServiceImpl<T extends IGenericDao, TP extends WorkflowEntity> extends ShiroGenericBizServiceImpl<T, TP> implements IWorkflowBizService<TP> {
    protected IdentityService identityService;
    protected TaskService taskService;
    protected RuntimeService runtimeService;
    protected RepositoryService repositoryService;

    @Override
    @Transactional
    public JsonStatus startProcess(String id) {
        JsonStatus jsonStatus = new JsonStatus();

        jsonStatus.setSuccess(true);
        try {
            String bizKey = getProcessKeyName() + ":" + id;
            //获得当前登陆用户
            String userName = this.getShiroService().getSubject().getPrincipal().toString();
            //设置流程启动人
            identityService.setAuthenticatedUserId(userName);
            TP bean = this.getEntity(new Long(id));
            //检查流程启动人和申请人是同一个人
            if (!bean.getCreateBy().equals(this.getShiroService().getCurrentUserRealName()))
                throw new NotSameStarterException();

            //获得启动参数
            Map map = new HashMap<>();
            getStartMap(map, bean);
            map.put(Const.VAR_INITIATOR, userName);
            //启动流程
            //创建流程业务编号
            String bizNo = createBusinessNo();
            map.put(Const.BUSINESS_NO, bizNo);


            ProcessInstance instance = runtimeService.startProcessInstanceByKey(getProcessKeyName(), bizKey, map);

            List<Task> task = taskService.createTaskQuery().processInstanceId(instance.getProcessInstanceId()).list();

            //设置实体状态
            bean.setProcessInstanceId(instance.getProcessInstanceId());
            bean.setCurrentNode(task.get(0).getName());
            bean.setAuditResult("审批中...");

            bean.setBusinessNo(bizNo);
            bean.setStatus(WorkflowStaus.ACTIVE);
            this.updateEntity(bean);

            runtimeService.setProcessInstanceName(instance.getId(), bizNo);

            jsonStatus.setMsg("启动流程成功！");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return jsonStatus;
    }

    /**
     * 初始化启动的参数，可以重载增加新的变量
     *
     * @param bean
     * @return
     */
    public void getStartMap(Map map, TP bean) {
        //put orgName to variant

        map.put(Const.VAR_STARTER_ORG_Name, String.valueOf(bean.getOrgName()));
        map.put(Const.VAR_TITLE, bean.getTitle());
    }

    /**
     * 创建流程业务编号
     * 格式：流程名称-当前日期-流水号（3位）
     *
     * @return
     */
    @Override
    public String createBusinessNo() {
        String no = "";
        Date dateNow = new Date();
        try {
            List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionKey(getProcessKeyName()).latestVersion().list();
            String processDefinitionName = processDefinitionList.get(0).getName();
            List list = dao.find("SELECT t.id from " +
                            this.persistentClass.getSimpleName() + " t  where " +
                            "t.status >0 and " +
                            "t.updateDate BETWEEN ?1 AND ?2",
                    DateUtil.getCurrentDayStartTime(),
                    DateUtil.getCurrentDayEndTime());

            no = String.format("%s-%s-%03d", processDefinitionName,
                    new SimpleDateFormat("yyyyMMdd").format(dateNow), list.size() + 1);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("创建流程编号失败！");
        }

        return no;
    }


    /**
     * 完成人工任务
     *
     * @param taskId
     * @param accepted
     * @param comment
     * @return
     */
    @Override
    @Transactional
    public JsonStatus completeTask(String taskId, String accepted, String comment) {
        JsonStatus jsonStatus = new JsonStatus();

        try {
            jsonStatus.setSuccess(true);
            String currentUserId = this.getShiroService().getSubject().getPrincipal().toString();
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            String currentTaskName = task.getName();
            //通过任务对象获取流程实例
            final String processInstanceId = task.getProcessInstanceId();
            ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

            //通过流程实例获取“业务键”
            String businessKey = pi.getBusinessKey();
            //拆分业务键，拆分成“业务对象名称”和“业务对象ID”的数组
            String beanId = WorkflowUtil.getBizId(businessKey);

            TP bean = this.getEntity(new Long(beanId));

            String userName = this.getShiroService().getCurrentUserRealName();
            //判断是否有人委托
            if (task.getDelegationState() != null && task.getDelegationState().equals(DelegationState.PENDING)) {
                taskService.resolveTask(task.getId());
            } else {
                taskService.claim(task.getId(), currentUserId);
            }

            writeClaimResult(task.getTaskDefinitionKey(), userName, bean);

            //添加备注信息
            identityService.setAuthenticatedUserId(userName);
            taskService.addComment(task.getId(), processInstanceId, comment);
            Map<String, Object> submitMap = new HashMap<String, Object>();
//            boolean passed = accepted.equals("同意") ? true : false;
            //完成任务
            submitMap.put(Const.VAR_ACCEPTED, accepted);
            Map vars = getVariantMap(submitMap, bean);
            //保存变量并关联taskid
            taskService.setVariablesLocal(task.getId(), vars);
            taskService.complete(task.getId(), vars);

            List<Task> curTask = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
            //设置实体状态
            if (curTask.size() > 0) {//流程未结束
                bean.setCurrentNode(curTask.get(0).getName());
                bean.setStatus(WorkflowStaus.ACTIVE);
            } else {//流程已结束
                bean.setCurrentNode("");
                bean.setStatus(WorkflowStaus.FINISH);
//                String result = passed ? "审批通过" : currentTaskName + "不通过";
                String result = "审批结果:" + currentTaskName + accepted;
                bean.setAuditResult(result);
            }

            this.updateEntity(bean);
            jsonStatus.setMsg("任务处理成功！");
        } catch (Exception e) {
            e.printStackTrace();
            throw new TaskProcessException();
        }
        return jsonStatus;
    }

    /**
     * 添加处理人的名字到实体中
     *
     * @param currentTaskId
     * @param bean
     */
    @Override
    public void writeClaimResult(String currentTaskId, String userName, TP bean) {
        try {
            // 将属性的首字符大写，方便构造get，set
            String name = currentTaskId.substring(0, 1).toUpperCase() + currentTaskId.substring(1);
            Method method = bean.getClass().getDeclaredMethod("set" + name, String.class);
            method.invoke(bean, userName);
        } catch (Exception e) {
            e.printStackTrace();
//            throw new TaskProcessException();
        }
    }

    @Override
    public JsonData getAllEntityByQuery(Integer page, Integer limit, String jsonStr, String sort) {
        IDataAuthService dataAuthService = null;
        try {
            dataAuthService = JNDIHelper.getJNDIServiceForName(IDataAuthService.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //false 只能查看自己的数据
        Long userid = this.shiroService.getCurrentUserId();
        if (!dataAuthService.isAuth(this.entityClassName, userid)) {
            Map<String, String> map = SerializeUtil.json2Map(jsonStr);
            map.put("createById", String.valueOf(userid));
            jsonStr = new Gson().toJson(map);
        }

        return super.getAllEntityByQuery(page, limit, jsonStr, sort);
    }

    public Map getVariantMap(Map map, T bean) {
        return map;
    }

    public void setIdentityService(IdentityService identityService) {
        this.identityService = identityService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public void setRuntimeService(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    public void setRepositoryService(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }
}
