package com.kalix.middleware.workflow.biz;


import com.kalix.middleware.workflow.api.biz.IFormHandler;

/**
 * Created by Administrator on 2016/6/2.
 */
public abstract class AbstractModifyFormHandler implements IFormHandler {
    private String processDefinitionId;

    public AbstractModifyFormHandler(){
        String[] splits=this.getClass().getSimpleName().split("Modify");

        if(splits.length>0) {
            processDefinitionId=splits[0].toLowerCase();
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
        return "modify.form";
    }

    /**
     * 获得extjs的窗口类名称
     *
     * @return
     */
    @Override
    public String getWindowClass() {
        return "kalix.workflow.approve.view.ModifyWindow";
    }

    @Override
    public String getButtonValue() {
        return _defultButtonValue;
    }
}
