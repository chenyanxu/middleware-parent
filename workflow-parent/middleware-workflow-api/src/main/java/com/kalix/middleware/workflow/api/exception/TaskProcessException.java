package com.kalix.middleware.workflow.api.exception;

import org.activiti.engine.ActivitiException;

/**
 * Created by sunlf on 2016-09-01.
 * 任务处理失败异常类
 */
public class TaskProcessException extends ActivitiException {
    private static final String MSG = "任务处理失败！";

    public TaskProcessException() {
        super(MSG);
    }

    public TaskProcessException(String exceptionMsg) {
        super(MSG + exceptionMsg);
    }
}
