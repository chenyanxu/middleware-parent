package com.kalix.middleware.workflow.api.exception;

import com.kalix.framework.core.api.exception.KalixRuntimeException;

/**
 * Created by sunlf on 2016-09-01.
 * 该职务下未找到任何审批人异常类
 */
public class NoPersonInDutyException extends KalixRuntimeException {
    private final static String content = "该职务下未找到任何审批人！";

    public NoPersonInDutyException(String message) {
        super(content, message);
    }
}