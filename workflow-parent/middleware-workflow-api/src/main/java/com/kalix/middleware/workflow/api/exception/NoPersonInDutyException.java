package com.kalix.middleware.workflow.api.exception;

import org.activiti.engine.ActivitiException;

/**
 * Created by sunlf on 2016-09-01.
 * 该职务下未找到任何审批人异常类
 */
public class NoPersonInDutyException extends ActivitiException {
    private final static String msg="该职务下未找到任何审批人！";
    public NoPersonInDutyException()
    {
        super(msg);
    }
}