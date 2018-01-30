package com.kalix.middleware.workflow.api.exception;

/**
 * Created by sunlf on 2016-09-01.
 * 表单处理失败异常类
 */
public class FormProcessException extends WorkflowException {
    private static final String detailMsg = "找不到注册的form组件！";

    public FormProcessException() {
        super(detailMsg);
    }
}
