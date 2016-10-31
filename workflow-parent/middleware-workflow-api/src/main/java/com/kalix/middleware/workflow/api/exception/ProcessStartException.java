package com.kalix.middleware.workflow.api.exception;

import org.activiti.engine.ActivitiException;

/**
 * Created by sunlf on 2016-09-01.
 * 启动流程失败异常类
 */
public class ProcessStartException extends ActivitiException {
    private static final String MSG = "启动流程失败！";

    public ProcessStartException() {
        super(MSG);
    }

    public ProcessStartException(String exceptionMsg) {
        super(MSG + exceptionMsg);
    }
}
