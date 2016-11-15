package com.kalix.middleware.workflow.api.exception;

/**
 * Created by sunlf on 2016-09-01.
 * 找不到上级领导异常类
 */
public class NoLeaderException extends ProcessStartException {
    private static final String detailMsg = "未能找到名称为上级领导的职位！";

    public NoLeaderException() {
        super(detailMsg);
    }
}
