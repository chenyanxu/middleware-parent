package com.kalix.middleware.workflow.api.exception;

import org.activiti.engine.ActivitiException;

/**
 * Created by sunlf on 2016-09-01.
 * 找不到上级领导异常类
 */
public class NoLeaderException extends ActivitiException {
    private static final String msg="未能找到名称为上级领导的职位！";
    public NoLeaderException()
    {
        super(msg);
    }
}
