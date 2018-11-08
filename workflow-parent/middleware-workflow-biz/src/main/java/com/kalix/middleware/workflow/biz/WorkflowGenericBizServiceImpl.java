package com.kalix.middleware.workflow.biz;

import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.code.Orient;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.series.Pie;
import com.kalix.framework.core.api.dao.IGenericDao;
import com.kalix.framework.core.api.persistence.JpaStatistic;
import com.kalix.framework.core.api.persistence.JsonData;
import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.api.web.model.QueryDTO;
import com.kalix.framework.core.impl.biz.ShiroGenericBizServiceImpl;
import com.kalix.framework.core.util.DateUtil;
import com.kalix.framework.core.util.SerializeUtil;
import com.kalix.framework.core.util.StringUtils;
import com.kalix.middleware.workflow.api.Const;
import com.kalix.middleware.workflow.api.biz.IWorkflowBizService;
import com.kalix.middleware.workflow.api.exception.NotSameStarterException;
import com.kalix.middleware.workflow.api.exception.TaskProcessException;
import com.kalix.middleware.workflow.api.model.WorkflowEntity;
import com.kalix.middleware.workflow.api.model.WorkflowStatus;
import com.kalix.middleware.workflow.api.util.WorkflowUtil;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.Task;

import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

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
            TP bean = this.getEntity(id);
            //检查流程启动人和申请人是同一个人
            if (!bean.getCreateBy().equals(this.getShiroService().getCurrentUserRealName()))
                throw new NotSameStarterException();

            //获得启动参数
            Map map = new HashMap<>();
            map = getVariantMap(map, bean);
            getStartMap(map, bean);
            map.put(Const.VAR_INITIATOR, userName);
            //启动流程
            //创建流程业务编号
            String bizNo = createBusinessNo(bean);
            map.put(Const.BUSINESS_NO, bizNo);


            ProcessInstance instance = runtimeService.startProcessInstanceByKey(getProcessKeyName(), bizKey, map);

            List<Task> task = taskService.createTaskQuery().processInstanceId(instance.getProcessInstanceId()).list();

            //设置实体状态
            bean.setProcessInstanceId(instance.getProcessInstanceId());
            bean.setCurrentNode(task.get(0).getName());
            bean.setApplyDate(new Date());
            bean.setAuditResult("审批中...");

            bean.setBusinessNo(bizNo);
            bean.setStatus(WorkflowStatus.ACTIVE);
            beforeStartProcess(bean);
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
     * 用于流程启动前处理业务数据
     *
     * @param bean 业务实体
     */
    @Override
    public void beforeStartProcess(TP bean) {

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
    public synchronized String createBusinessNo(TP bean) {
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

            TP bean = this.getEntity(beanId);

            String userName = this.getShiroService().getCurrentUserRealName();
            //判断是否有人委托
            if (task.getDelegationState() != null && task.getDelegationState().equals(DelegationState.PENDING)) {
                taskService.resolveTask(task.getId());
            } else {
                taskService.claim(task.getId(), currentUserId);
            }
            //判断当前是否是驳回
            boolean passed = accepted.equals("同意") ? true : false;
            bean.setReject(!passed);

            writeClaimResult(task.getTaskDefinitionKey(), userName, bean);

            //添加备注信息
            identityService.setAuthenticatedUserId(userName);
            taskService.addComment(task.getId(), processInstanceId, comment);
            Map<String, Object> submitMap = new HashMap<String, Object>();

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
                bean.setStatus(WorkflowStatus.ACTIVE);
            } else {//流程已结束
                bean.setCurrentNode("");
                bean.setStatus(WorkflowStatus.FINISH);
//                String result = passed ? "审批通过" : currentTaskName + "不通过";
                String result = "审批结果:" + currentTaskName + accepted;
                bean.setAuditResult(result);
                afterFinishProcess(bean, accepted);
                //用于子类调用该方法时判断流程是否结束
                jsonStatus.setTag("流程结束:" + beanId);
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
     * 用于流程结束后处理业务数据
     *
     * @param bean   业务实体
     * @param result 审批结果
     */
    @Override
    public void afterFinishProcess(TP bean, String result) {

    }

    /**
     * 添加处理人的名字到实体中,通过流程定义UserTask的id和业务实体对应来实现
     * 例如业务实体depUser属性，在UserTAsk中id定义为depUser，这样处理人就可以通过反射写到实体中
     *
     * @param currentTaskId
     * @param bean
     */
    @Override
    public void writeClaimResult(String currentTaskId, String userName, TP bean) {
        try {
            // 将属性的首字符大写，方便构造get，set
            String name = currentTaskId.substring(0, 1).toUpperCase() + currentTaskId.substring(1);
            // 判断是否是修改状态，非修改处理审批人，修改不处理
            if (!name.toLowerCase().equals("modify")) {
                if (bean.getReject()) { // 判断是驳回状态,审批人设置为空
                    // 将属性的首字符大写，方便构造get，set
                    Method method = bean.getClass().getDeclaredMethod("set" + name, String.class);
                    method.invoke(bean, "");
                } else {
                    Method method = bean.getClass().getDeclaredMethod("get" + name);
                    String str = (String) method.invoke(bean);
                    Method method1 = bean.getClass().getDeclaredMethod("set" + name, String.class);
                    if (StringUtils.isEmpty(str)) {
                        method1.invoke(bean, userName);
                    } else {
                        // 判断审批人之前是否签字，如果已经签字，再次同意后不需签字（同时包括会签和单个人员签字）
                        if (!str.contains(userName))
                            method1.invoke(bean, str + "," + userName); //会签环节，附加审批人
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new TaskProcessException();
        }
    }

    @Override
    public JsonData getAllEntityByQuery(Integer page, Integer limit, String jsonStr, String sort) {
        /*IDataAuthService dataAuthService = null;
        try {
            dataAuthService = JNDIHelper.getJNDIServiceForName(IDataAuthService.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //false 只能查看自己的数据
        /*Long userid = this.shiroService.getCurrentUserId();
        if (!dataAuthService.isAuth(this.entityClassName, userid)) {
            Map<String, String> map = SerializeUtil.json2Map(jsonStr);
            map.put("createById", String.valueOf(userid));
            jsonStr = new Gson().toJson(map);
        }*/
        return super.getAllEntityByQuery(page, limit, jsonStr, sort);
    }

    @Override
    public void afterDeleteProcess(TP bean) {

    }

    /**
     * 流程中止服务
     *
     * @param processInstanceId 流程实例id
     * @return
     */
    @Override
    public JsonStatus deleteProcess(String processInstanceId, String reason) {
        JsonStatus jsonStatus = new JsonStatus();
        jsonStatus.setSuccess(true);
        try {
            //通过任务对象获取流程实例
            ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

            //通过流程实例获取“业务键”
            String businessKey = pi.getBusinessKey();
            //拆分业务键，拆分成“业务对象名称”和“业务对象ID”的数组
            String beanId = WorkflowUtil.getBizId(businessKey);

            TP bean = this.getEntity(beanId);
            bean.setStatus(WorkflowStatus.DELETE);
            bean.setAuditResult("业务中止");
            afterDeleteProcess(bean);
            this.updateEntity(bean);
            runtimeService.deleteProcessInstance(processInstanceId, reason);
            jsonStatus.setMsg("流程中止成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonStatus.setSuccess(false);
            jsonStatus.setMsg("流程中止失败");
        }
        return jsonStatus;
    }

    public Map getVariantMap(Map map, TP bean) {
        map.put(Const.VAR_TITLE, bean.getTitle());
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

    /**
     * 工作流统计
     *
     * @param jsonStr 包含sql 查询的参数，图表展现的参数
     * @return
     */
    public JsonData getWorkFlowStatistic(String jsonStr) {
//        Map<String, String> jsonMap = null;
        Map<String, Object> jsonMap = null;
        Map<String, String> barMap = new HashMap<>();
        String chartTitle = "";
        String groupSelectValue = "";
        String chartSelectValue = "";
        List<Map<String, String>> statusMap = null;
        if (jsonStr != null && !jsonStr.isEmpty()) {
            jsonMap = SerializeUtil.jsonToMap(jsonStr);
            for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
                if ("groupSelectValue".equals(entry.getKey())) {
                    groupSelectValue = (String) entry.getValue();
                    break;
                }
            }
            for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
                if ("chartSelectValue".equals(entry.getKey())) {
                    chartSelectValue = (String) entry.getValue();
                    break;
                }
            }
            for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
                if ("chartTitle".equals(entry.getKey())) {
                    chartTitle = (String) entry.getValue();
                    break;
                }
            }
            for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
                if ("workflowStatus".equals(entry.getKey())) {
                    statusMap = (List<Map<String, String>>) entry.getValue();
                    break;
                }
            }
        }
        String[] groupbys = null;
        String[] selectNotStatistics = null;
        String[] selectStatistics = null;
        if ("2".equals(groupSelectValue)) {
            groupbys = new String[]{"auditResult"};
            selectNotStatistics = new String[]{"auditResult"};
            selectStatistics = new String[]{"auditResult"};
        } else if ("1".equals(groupSelectValue)) {
            groupbys = new String[]{"status"};
            selectNotStatistics = new String[]{"status"};
            selectStatistics = new String[]{"status"};
        } else {
            groupbys = new String[]{"orgName"};
            selectNotStatistics = new String[]{"orgName"};
            selectStatistics = new String[]{"orgId"};
        }
        QueryDTO dto = new QueryDTO();
        // select orgName,count(orgId) from oa_workflow_redheadapply group by orgName;
        JpaStatistic jpaStatistic = new JpaStatistic();
        jpaStatistic.setGroupBys(groupbys);
        jpaStatistic.setSelectNotStatistics(selectNotStatistics);
        jpaStatistic.setSelectStatistics(selectStatistics);
        jpaStatistic.setStatisticTypes(new JpaStatistic.Statistic[]{JpaStatistic.Statistic.COUNT});
        Map<String, String> params = jpaStatistic.getStatisticParam();
        if (jsonMap != null && !jsonMap.isEmpty()) {
            for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
                if (!"groupSelectValue".equals(entry.getKey()) && !"chartTitle".equals(entry.getKey())
                        && !"selectValue".equals(entry.getKey()) && !"workflowStatus".equals(entry.getKey())
                        && !"chartSelectValue".equals(entry.getKey())) {
                    params.put(entry.getKey(), (String) entry.getValue());
                }
            }
        }
        dto.setJsonMap(params);
        JsonData data = dao.getAllByStatistic(dto);
        List<Tuple> list = data.getData();
        JsonData d1 = new JsonData();
        List<Map<String, String>> dataList = new ArrayList<>();
        String[] types = new String[list.size()];
        int[] datas = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            if ("1".equals(groupSelectValue)) {
                //types[i] = statusMap.get(list.get(i).get(0));
                for (Map<String, String> stMap : statusMap) {
                    if (stMap.get("value").equals(String.valueOf(list.get(i).get(0)))) {
                        types[i] = stMap.get("label");
                    }
                }
            } else {
                types[i] = String.valueOf(list.get(i).get(0));
            }
            datas[i] = Integer.parseInt(list.get(i).get(1).toString());
        }
        String chartData = "";
        if ("2".equals(chartSelectValue)) {
            chartData = pieChart(types, datas, chartTitle);
        } else if ("1".equals(chartSelectValue)) {
            chartData = lineChart(types, datas, chartTitle);
        } else {
            chartData = barChart(types, datas, chartTitle);
        }
        barMap.put("option", chartData);
        dataList.add(barMap);
        d1.setData(dataList);
        return d1;
    }

    /**
     * 饼图Options
     *
     * @param types
     * @param datas
     * @param chartTitle
     * @return
     */
    private String pieChart(String[] types, int[] datas, String chartTitle) {
        GsonOption option = new GsonOption();
        option.title().text(chartTitle).x("center");// 大标题、标题位置
        // 提示工具 鼠标在每一个数据项上，触发显示提示数据
        option.tooltip().trigger(Trigger.item).formatter("{a} <br/>{b} : {c} ({d}%)");
        option.legend().orient(Orient.horizontal).x("left").data(types);// 图例及位置
        option.calculable(true);// 拖动进行计算
        Pie pie = new Pie();
        // 标题、半径、位置
        pie.name(chartTitle).radius("80%").center("50%", "60%");
        // 循环数据
        for (int i = 0; i < types.length; i++) {
            Map<String, Object> map = new HashMap<>(2);
            map.put("value", datas[i]);
            map.put("name", types[i]);
            pie.data(map);
        }
        option.series(pie);
        return option.toString();
    }

    /**
     * 折线图Options
     *
     * @param types
     * @param datas
     * @param chartTitle
     * @return
     */
    private String lineChart(String[] types, int[] datas, String chartTitle) {
        GsonOption option = new GsonOption();
        option.title(chartTitle); // 标题
        option.tooltip().show(true).formatter("{a} <br/>{b} : {c}");//显示工具提示,设置提示格式
        option.legend(chartTitle);// 图例
        Line line = new Line();
        CategoryAxis category = new CategoryAxis();// 轴分类
        category.data(types);// 轴数据类别
        // 循环数据
        for (int i = 0; i < datas.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("value", datas[i]);
            line.data(map);
        }
        option.xAxis(category);// x轴
        option.yAxis(new com.github.abel533.echarts.axis.ValueAxis());// y轴
        option.series(line);
        return option.toString();
    }

    /**
     * 柱状图Options
     *
     * @param types
     * @param datas
     * @param chartTitle
     * @return
     */
    private String barChart(String[] types, int[] datas, String chartTitle) {
        String title = chartTitle;
        GsonOption option = new GsonOption();
        option.title(title); // 标题
        // 工具栏
//        option.toolbox().show(true).feature(Tool.mark, // 辅助线
//                Tool.dataView, // 数据视图
//                new MagicType(Magic.line, Magic.bar),// 线图、柱状图切换
//                Tool.restore,// 还原
//                Tool.saveAsImage);// 保存为图片
        option.tooltip().show(true).formatter("{a} <br/>{b} : {c}");//显示工具提示,设置提示格式
        option.legend(title);// 图例
        Bar bar = new Bar(title);// 图类别(柱状图)
        CategoryAxis category = new CategoryAxis();// 轴分类
        category.data(types);// 轴数据类别
        // 循环数据
        for (int i = 0; i < datas.length; i++) {
            int data = datas[i];
            Map<String, Object> map = new HashMap<>();
            map.put("value", data);
            bar.data(map);
        }
        option.xAxis(category);// x轴
        option.yAxis(new com.github.abel533.echarts.axis.ValueAxis());// y轴
        option.series(bar);
        return option.toString();
    }
}
