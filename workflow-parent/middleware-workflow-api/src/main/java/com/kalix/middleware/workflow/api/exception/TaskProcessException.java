package com.kalix.middleware.workflow.api.exception;

import com.kalix.framework.core.api.exception.KalixRuntimeException;

/**
 * Created by sunlf on 2016-09-01.
 * 任务处理失败异常类
 */
public class TaskProcessException extends KalixRuntimeException {
    private static final String content = "任务处理失败！";

    public TaskProcessException(String exceptionMsg) {
        super(content, exceptionMsg);
    }
}
