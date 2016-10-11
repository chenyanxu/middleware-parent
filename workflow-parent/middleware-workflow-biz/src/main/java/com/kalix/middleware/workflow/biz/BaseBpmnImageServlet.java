package com.kalix.middleware.workflow.biz;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.ProcessEngineImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;

import javax.servlet.http.HttpServlet;
import java.io.InputStream;
import java.util.*;

/**
 * Created by sunlf on 2015/8/1.
 */
public class BaseBpmnImageServlet extends HttpServlet {
    protected RepositoryService repositoryService;
    protected HistoryService historyService;
    protected RuntimeService runtimeService;
    protected ProcessEngine processEngine;

    public void setRepositoryService(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }

    public void setRuntimeService(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    public void setProcessEngine(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    protected InputStream getInputStream(String processDefinitionId) {
        /**流程实例**/
        BpmnModel bpmnModel = processEngine.getRepositoryService()
                .getBpmnModel(processDefinitionId);
//                    List<String> activeActivityIds =  processEngine.getRuntimeService().getActiveActivityIds(task.getProcessInstanceId());
        ProcessEngineImpl defaultProcessEngine = (ProcessEngineImpl) ProcessEngines.getDefaultProcessEngine();
        Context.setProcessEngineConfiguration(defaultProcessEngine.getProcessEngineConfiguration());
        /**得到图片输出流**/
        InputStream imageStream = new DefaultProcessDiagramGenerator().generateDiagram(bpmnModel, "png",
                Collections.<String>emptyList(), Collections.<String>emptyList(), processEngine.getProcessEngineConfiguration().getActivityFontName(),
                processEngine.getProcessEngineConfiguration().getLabelFontName(), null, null, 1.0);
        return imageStream;
    }

    protected InputStream getInputStream(String processDefinitionId, String processInstanceId) {
        /**流程实例**/
        BpmnModel bpmnModel = repositoryService
                .getBpmnModel(processDefinitionId);
        List<String> activeActivityIds = runtimeService.getActiveActivityIds(processInstanceId);
        ProcessEngineImpl defaultProcessEngine = (ProcessEngineImpl) ProcessEngines.getDefaultProcessEngine();
        Context.setProcessEngineConfiguration(defaultProcessEngine.getProcessEngineConfiguration());
        /**得到图片输出流**/
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
        List<String> highLightedFlows = getHighLightedFlows(processDefinition, processInstanceId);

        DefaultProcessDiagramGenerator defaultProcessDiagramGenerator = new DefaultProcessDiagramGenerator();
        InputStream imageStream = defaultProcessDiagramGenerator.generateDiagram(bpmnModel, "png",
                activeActivityIds, highLightedFlows, processEngine.getProcessEngineConfiguration().getActivityFontName(),
                processEngine.getProcessEngineConfiguration().getLabelFontName(), null, null, 1.0);
        return imageStream;
    }

    /**
     * 新版本的getHighLightedFlows方法
     *
     * @param processDefinition
     * @param processInstanceId
     * @return
     */
    protected List<String> getHighLightedFlows(ProcessDefinitionEntity processDefinition, String processInstanceId) {

        List<String> highLightedFlows = new ArrayList<String>();

        List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                        //用开始时间升序不准确,直接默认按id升序排是准确的
                        //.orderByHistoricActivityInstanceStartTime().asc()/*.orderByActivityId().asc()*/
                .list();

        LinkedList<HistoricActivityInstance> hisActInstList = new LinkedList<HistoricActivityInstance>();
        hisActInstList.addAll(historicActivityInstances);

        getHighlightedFlows(processDefinition.getActivities(), hisActInstList, highLightedFlows);

        return highLightedFlows;
    }

    /**
     * getHighlightedFlows
     *
     * @param activityList
     * @param hisActInstList
     * @param highLightedFlows
     */
    private void getHighlightedFlows(List<ActivityImpl> activityList, LinkedList<HistoricActivityInstance> hisActInstList, List<String> highLightedFlows) {

        //Activity定义中的startEvent活动
        List<ActivityImpl> startEventActList = new ArrayList<ActivityImpl>();
        Map<String, ActivityImpl> activityMap = new HashMap<String, ActivityImpl>(activityList.size());
        for (ActivityImpl activity : activityList) {

            activityMap.put(activity.getId(), activity);

            String actType = (String) activity.getProperty("type");
            if (actType != null && actType.toLowerCase().indexOf("startevent") >= 0) {
                startEventActList.add(activity);
            }
        }

        //检查第一个节点是否是startEvent(如果流程是由别的callActivity节启动的则没有startEvent),
        //如果不是startEvent节点则要找出startEvent的高亮flow
        HistoricActivityInstance firstHistActInst = hisActInstList.getFirst();
        String firstActType = firstHistActInst.getActivityType();
        if (firstActType != null && firstActType.toLowerCase().indexOf("startevent") < 0) {
            PvmTransition startTrans = getStartTransaction(startEventActList, firstHistActInst);
            if (startTrans != null) {
                highLightedFlows.add(startTrans.getId());
            }
        }

        while (hisActInstList.size() > 0) {
            HistoricActivityInstance histActInst = hisActInstList.removeFirst();
            ActivityImpl activity = activityMap.get(histActInst.getActivityId());

            boolean isParallel = false;
            String type = histActInst.getActivityType();
            if ("parallelGateway".equals(type) || "inclusiveGateway".equals(type)) {
                isParallel = true;
            } else if ("subProcess".equals(histActInst.getActivityType())) {
                getHighlightedFlows(activity.getActivities(), hisActInstList, highLightedFlows);
            }

            List<PvmTransition> outgoingTrans = new ArrayList<PvmTransition>();
            outgoingTrans.addAll(activity.getOutgoingTransitions());
            outgoingTrans.addAll(getBoundaryEventOutgoingTransitions(activity));

            List<String> activityHighLightedFlowIds = getHighlightedFlows(outgoingTrans, hisActInstList, isParallel);

            highLightedFlows.addAll(activityHighLightedFlowIds);
        }

    }

    /**
     * 查找startEventActList列表中,谁的outgoing flow连接的是firstActInst
     *
     * @param startEventActList
     * @param firstActInst
     * @return
     */
    private PvmTransition getStartTransaction(List<ActivityImpl> startEventActList, HistoricActivityInstance firstActInst) {
        for (ActivityImpl startEventAct : startEventActList) {
            for (PvmTransition trans : startEventAct.getOutgoingTransitions()) {
                if (trans.getDestination().getId().equals(firstActInst.getActivityId())) {
                    return trans;
                }
            }
        }
        return null;
    }

    /**
     * @param activity
     * @return
     */
    private List<PvmTransition> getBoundaryEventOutgoingTransitions(ActivityImpl activity) {
        List<PvmTransition> boundaryTrans = new ArrayList<PvmTransition>();
        for (ActivityImpl subActivity : activity.getActivities()) {
            String type = (String) subActivity.getProperty("type");
            if (type != null && type.toLowerCase().indexOf("boundary") >= 0) {
                boundaryTrans.addAll(subActivity.getOutgoingTransitions());
            }
        }
        return boundaryTrans;
    }

    /**
     * 获取单个activity的高亮flow
     *
     * @param pvmTransitionList
     * @param hisActInstList
     * @param isParallel        是否只有单一的outgoing(比如说exclusiveGateway, Task上的BoundaryEvent)
     * @return
     */
    private List<String> getHighlightedFlows(List<PvmTransition> pvmTransitionList, LinkedList<HistoricActivityInstance> hisActInstList, boolean isParallel) {

        List<String> highLightedFlowIds = new ArrayList<String>();

        PvmTransition earliestTrans = null;
        HistoricActivityInstance earliestHisActInst = null;

        for (PvmTransition pvmTransition : pvmTransitionList) {

            String destActId = pvmTransition.getDestination().getId();
            HistoricActivityInstance destHisActInst = findHisActInst(hisActInstList, destActId);
            if (destHisActInst != null) {

                if (isParallel) {
                    highLightedFlowIds.add(pvmTransition.getId());
                } else {
                    if (earliestHisActInst == null || (earliestHisActInst.getId().compareTo(destHisActInst.getId()) > 0)) {//用开始时间比较不准确,直接默认按id比较是准确的
                        earliestTrans = pvmTransition;
                        earliestHisActInst = destHisActInst;
                    }
                }
            }
        }

        if ((!isParallel) && earliestTrans != null) {
            highLightedFlowIds.add(earliestTrans.getId());
        }

        return highLightedFlowIds;
    }

    private HistoricActivityInstance findHisActInst(LinkedList<HistoricActivityInstance> hisActInstList, String actId) {
        for (HistoricActivityInstance hisActInst : hisActInstList) {
            if (hisActInst.getActivityId().equals(actId)) {
                return hisActInst;
            }
        }
        return null;
    }
}
