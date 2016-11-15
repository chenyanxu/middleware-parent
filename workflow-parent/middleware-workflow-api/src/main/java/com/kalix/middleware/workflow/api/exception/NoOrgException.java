package com.kalix.middleware.workflow.api.exception;

/**
 * Created by sunlf on 2016-09-01.
 * 找不到组织机构异常类
 */
public class NoOrgException extends ProcessStartException {
    private final static String detailMsg = "未能找到组织机构！";

    public NoOrgException() {
        super(detailMsg);
    }
}
