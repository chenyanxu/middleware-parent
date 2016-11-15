package com.kalix.middleware.workflow.api.exception;

import com.kalix.framework.core.api.exception.KalixRuntimeException;

/**
 * Created by sunlf on 2016-09-01.
 * 工作流处理失败异常类
 */
public abstract class WorkflowException extends KalixRuntimeException {
    private static final String msg = "工作流处理失败！";

    public WorkflowException(String detailMsg) {
        super(msg, detailMsg);
    }
}
