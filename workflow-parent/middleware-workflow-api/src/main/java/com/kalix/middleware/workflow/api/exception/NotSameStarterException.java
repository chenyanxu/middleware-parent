package com.kalix.middleware.workflow.api.exception;

import com.kalix.framework.core.api.exception.KalixRuntimeException;

/**
 * Created by sunlf on 2016-09-01.
 * 流程启动人和申请人不是同一个人的异常类
 */
public class NotSameStarterException extends KalixRuntimeException {
    private final static String content = "流程启动人和申请人不是同一个人！";

    public NotSameStarterException(String msg) {
        super(content, msg);
    }

    public NotSameStarterException(){
        super(content, "");
    }
}
