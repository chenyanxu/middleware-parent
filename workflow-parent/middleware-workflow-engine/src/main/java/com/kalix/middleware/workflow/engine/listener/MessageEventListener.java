package com.kalix.middleware.workflow.engine.listener;

import com.kalix.framework.core.util.JNDIHelper;
import org.activiti.engine.HistoryService;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.impl.ActivitiEntityEventImpl;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.json.JSONObject;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by sunlf on 2016-02-22.
 * 自定义事件监听，监听任务分配
 */
public class MessageEventListener implements ActivitiEventListener {
    public static final String WORKFLOW_MESSAGE_TOPIC = "com/kalix/workflow/engine/message";
    public static final String WORKFLOW_STARTER_TOPIC = "com/kalix/workflow/engine/starter";
    private EventAdmin eventAdmin;
    private HistoryService historyService;

    public MessageEventListener() {
        try {
            eventAdmin = JNDIHelper.getJNDIServiceForName("org.osgi.service.event.EventAdmin");
//            historyService=JNDIHelper.getJNDIServiceForName("org.activiti.engine.HistoryService");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEvent(ActivitiEvent event) {
        switch (event.getType()) {
            case TASK_CREATED:
                postCreateEvent(event);
                break;
            case TASK_COMPLETED:
                postCompleteEvent(event);
                break;
            default:
                System.out.println("Event received: " + event.getType());
        }
    }

    /**
     * 发送任务完成事件给工作流的发起人
     * @param event
     */
    private void postCompleteEvent(ActivitiEvent event) {
        try {
            historyService = JNDIHelper.getJNDIServiceForName("org.activiti.engine.HistoryService");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String processInstanceId = event.getProcessInstanceId();
        JSONObject taskJson=new JSONObject();
        HistoricProcessInstance historicProcessInstance=historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        String startUserId=historicProcessInstance.getStartUserId();
        Dictionary properties = new Hashtable();

        taskJson.put("startUserId",startUserId);
        taskJson.put("businessKey", historicProcessInstance.getName());

        properties.put("body", taskJson.toString());
        Event osgi_event = new Event(WORKFLOW_STARTER_TOPIC, properties);
        System.out.println("A task of " + startUserId + " is completed!");
        eventAdmin.postEvent(osgi_event);
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }

    /**
     * 发送 osgi event 给相应的group,具体的实现在common-parent消息中
     * @param event
     */
    private void postCreateEvent(ActivitiEvent event) {
        try {
            historyService = JNDIHelper.getJNDIServiceForName("org.activiti.engine.HistoryService");
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject taskJson=new JSONObject();
        String processInstanceId = event.getProcessInstanceId();
        ActivitiEntityEventImpl entityEvent= (ActivitiEntityEventImpl) event;
        TaskEntity task= (TaskEntity) entityEvent.getEntity();
        List<IdentityLinkEntity> idList=task.getIdentityLinks();
        if (idList.size() > 0) {
            for (IdentityLinkEntity id : idList) {

                HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                        .processInstanceId(processInstanceId).singleResult();

                if (historicProcessInstance != null) {
                    taskJson.put("group", id.getGroupId());
                    taskJson.put("businessKey", historicProcessInstance.getBusinessKey());
                    System.out.println("A task group of " + id.getGroupId() + " is assigned!");
                    //添加相关内容到消息体
                    Dictionary properties = new Hashtable();
                    properties.put("body", taskJson.toString());
                    Event osgi_event = new Event(WORKFLOW_MESSAGE_TOPIC, properties);
                    eventAdmin.postEvent(osgi_event);
                } else {
                    System.out.println("No find a historicProcessInstance!");
                }
            }
        } else {
            System.out.println("No find a IdentityLinkEntity!");
        }

    }

    public void setEventAdmin(EventAdmin eventAdmin) {
        this.eventAdmin = eventAdmin;
    }

    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }
}
