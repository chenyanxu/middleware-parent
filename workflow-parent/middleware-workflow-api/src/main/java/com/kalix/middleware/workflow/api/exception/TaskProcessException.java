package com.kalix.middleware.workflow.api.exception;

/**
 * Created by sunlf on 2016-09-01.
 * 任务处理失败异常类
 */
public class TaskProcessException extends WorkflowException {
    private static final String detailMsg = "任务处理失败！";

    public TaskProcessException() {
        super(detailMsg);
    }
}
