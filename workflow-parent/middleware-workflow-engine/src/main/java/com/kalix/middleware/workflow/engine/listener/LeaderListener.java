package com.kalix.middleware.workflow.engine.listener;

import com.kalix.framework.core.util.JNDIHelper;
import com.kalix.middleware.workflow.api.Const;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.json.JSONObject;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import static com.kalix.middleware.workflow.engine.listener.MessageEventListener.WORKFLOW_MESSAGE_TOPIC;

/**
 * 部门领导监听器，用于处理当前申请人的部门领导
 * 该类直接在流程定义文件中实例化
 */
public class LeaderListener implements TaskListener {

    /* private ITaskService taskService;
             private IShiroService shiroService;*/
    JSONObject taskJson = new JSONObject();
    private EventAdmin eventAdmin;

    public LeaderListener() {
        try {
            /*taskService = JNDIHelper.getJNDIServiceForName(ITaskService.class.getName());
            shiroService = JNDIHelper.getJNDIServiceForName(IShiroService.class.getName());*/
            eventAdmin = JNDIHelper.getJNDIServiceForName(EventAdmin.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        //get starter user name
        String rtnStr = null;
        //读取组织结构id
        String orgName = (String) delegateTask.getVariable(Const.STARTER_ORG_Name);
        String group = orgName + Const.CONNECTOR_CHAR + Const.LEADER_NAME;
        delegateTask.addCandidateGroup(group);
//        DelegateExecution execution = delegateTask.getExecution();

        //发送消息
        String businessNo = (String) delegateTask.getVariable(Const.BUSINESS_NO);
        ;
        taskJson.put("group", group);
        taskJson.put("businessNo", businessNo);
        System.out.println("A task group of " + group + " is assigned!");
        //添加相关内容到消息体
        Dictionary properties = new Hashtable();
        properties.put("body", taskJson.toString());
        Event osgi_event = new Event(WORKFLOW_MESSAGE_TOPIC, properties);
        eventAdmin.postEvent(osgi_event);

        /*boolean succeed = false;

        //获得兄弟机构下名称为“上级领导”职位下的全部用户
        try {
            rtnStr = HttpClientUtil.shiroGet("http://localhost:8181/kalix/camel/rest/users/user/dutys/" + orgName + "/" + "上级领导", this.shiroService.getSession().getId().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> userNameList = new ArrayList<>();

        if (rtnStr != null) {
            userNameList = SerializeUtil.unserializeJson(rtnStr, List.class);
        }
        if (userNameList.size() > 0) {
            for (String userName : userNameList)
                delegateTask.addCandidateGroup(userName);
            succeed = true;
        } else {
            throw new NoPersonInDutyException();
        }

        if (!succeed) {
            throw new NoLeaderException();
        }*/
    }
}
