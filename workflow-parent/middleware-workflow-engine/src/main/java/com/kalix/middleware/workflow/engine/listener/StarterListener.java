package com.kalix.middleware.workflow.engine.listener;

import com.kalix.middleware.workflow.api.Const;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * 启动人监听器，用于处理当前启动人
 * 该类直接在流程定义文件中实例化
 */
public class StarterListener implements TaskListener {

    public StarterListener() {

    }

    @Override
    public void notify(DelegateTask delegateTask) {
        //get starter user name
        String initiator = (String) delegateTask.getVariable(Const.VAR_INITIATOR);
        delegateTask.addCandidateUser(initiator);
    }
}
