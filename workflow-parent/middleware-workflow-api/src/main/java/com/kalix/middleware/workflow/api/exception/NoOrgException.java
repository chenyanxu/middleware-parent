package com.kalix.middleware.workflow.api.exception;

import org.activiti.engine.ActivitiException;

/**
 * Created by sunlf on 2016-09-01.
 * 找不到组织机构异常类
 */
public class NoOrgException extends ActivitiException{
    private final static String msg="未能找到组织机构！";
    public NoOrgException()
    {
        super(msg);
    }
}
