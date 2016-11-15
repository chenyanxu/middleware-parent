package com.kalix.middleware.workflow.api.exception;

/**
 * Created by sunlf on 2016-09-01.
 * 流程启动人和申请人不是同一个人的异常类
 */
public class NotSameStarterException extends ProcessStartException {
    private final static String detailMsg = "流程启动人和申请人不是同一个人！";

    public NotSameStarterException() {
        super(detailMsg);
    }
}
