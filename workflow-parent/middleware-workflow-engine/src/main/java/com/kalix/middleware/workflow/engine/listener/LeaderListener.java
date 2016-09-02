package com.kalix.middleware.workflow.engine.listener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kalix.framework.core.api.security.IShiroService;
import com.kalix.framework.core.util.HttpClientUtil;
import com.kalix.framework.core.util.JNDIHelper;
import com.kalix.framework.core.util.SerializeUtil;
import com.kalix.middleware.workflow.api.biz.ITaskService;
import com.kalix.middleware.workflow.api.exception.NoLeaderException;
import com.kalix.middleware.workflow.api.exception.NoOrgException;
import com.kalix.middleware.workflow.api.exception.NoPersonInDutyException;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 部门领导监听器，用于处理当前申请人的部门领导
 */
public class LeaderListener implements TaskListener {
    private ITaskService taskService;
    private IShiroService shiroService;

    public LeaderListener() {
        try {
            taskService = JNDIHelper.getJNDIServiceForName(ITaskService.class.getName());
            shiroService = JNDIHelper.getJNDIServiceForName(IShiroService.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        //get starter user name
        String rtnStr = null;
        //读取组织结构id
        String orgId = (String) delegateTask.getVariable("startOrgId");
        boolean succeed = false;

        //获得兄弟机构下名称为“上级领导”职位下的全部用户
        try {
            rtnStr = HttpClientUtil.shiroGet("http://localhost:8181/kalix/camel/rest/users/user/dutys/" + orgId + "/" + "上级领导", this.shiroService.getSession().getId().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> userNameList = new ArrayList<>();

        if (rtnStr != null) {
            userNameList = SerializeUtil.unserializeJson(rtnStr, List.class);
        }
        if (userNameList.size() > 0) {
            for (String userName : userNameList)
                delegateTask.addCandidateUser(userName);
            succeed = true;
        } else {
            throw new NoPersonInDutyException();
        }

        if (!succeed) {
            throw new NoLeaderException();
        }
    }
}
