package com.kalix.middleware.workflow.biz;


import com.kalix.middleware.workflow.api.biz.IFormHandler;

/**
 * Created by Administrator on 2016/6/2.
 */
public abstract class AbstractAuditFormHandler implements IFormHandler {
    private String processDefinitionId;
    private final String _defultButtonValue = "同意,不同意";
    private final String _defultFormKey = "audit.form";

    public AbstractAuditFormHandler() {

        String[] splits = this.getClass().getSimpleName().split("Audit");

        if (splits.length > 0) {
            processDefinitionId = splits[0].toLowerCase();
        }
    }

    @Override
    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    /**
     * 获得form的key
     *
     * @return
     */
    @Override
    public String getFormKey() {
        return _defultFormKey;
    }

    /**
     * 获得extjs的窗口类名称
     *
     * @return
     */
    @Override
    public String getWindowClass() {
        return "kalix.workflow.approve.view.ApproveWindow";
    }

    @Override
    public String getButtonValue() {
        return _defultButtonValue;
    }


}
