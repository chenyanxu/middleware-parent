package com.kalix.middleware.workflow.api.exception;

import com.kalix.framework.core.api.exception.KalixRuntimeException;

/**
 * Created by sunlf on 2016-09-01.
 * 找不到上级领导异常类
 */
public class NoLeaderException extends KalixRuntimeException {
    private static final String content = "未能找到名称为上级领导的职位！";

    public NoLeaderException(String message) {
        super(content, message);
    }
}
