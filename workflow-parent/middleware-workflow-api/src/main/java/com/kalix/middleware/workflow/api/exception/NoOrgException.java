package com.kalix.middleware.workflow.api.exception;

/**
 * Created by sunlf on 2016-09-01.
 * 找不到组织机构异常类
 */
public class NoOrgException extends RuntimeException{
    private final static String msg="未能找到组织机构！";
    public NoOrgException()
    {
        super(msg);
    }
}
