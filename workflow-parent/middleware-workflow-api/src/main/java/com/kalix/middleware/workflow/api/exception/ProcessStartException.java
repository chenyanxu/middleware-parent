package com.kalix.middleware.workflow.api.exception;

import com.kalix.framework.core.api.exception.KalixRuntimeException;

/**
 * Created by sunlf on 2016-09-01.
 * 启动流程失败异常类
 */
public class ProcessStartException extends KalixRuntimeException {
    private static final String msg = "启动流程失败！";

    public ProcessStartException(String detailMsg) {
        super(msg, detailMsg);
    }
}
