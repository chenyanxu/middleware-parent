package com.kalix.middleware.workflow.api.exception;

import com.kalix.framework.core.api.exception.KalixRuntimeException;

/**
 * Created by sunlf on 2016-09-01.
 * 找不到组织机构异常类
 */
public class NoOrgException extends KalixRuntimeException {
    private final static String content = "未能找到组织机构！";

    public NoOrgException(String message) {
        super(content, message);
    }
}
