package com.kalix.middleware.workflow.api.exception;

/**
 * Created by sunlf on 2016-09-01.
 * 该职务下未找到任何审批人异常类
 */
public class NoPersonInDutyException extends ProcessStartException {
    private final static String detailMsg = "该职务下未找到任何审批人！";

    public NoPersonInDutyException() {
        super(detailMsg);
    }
}