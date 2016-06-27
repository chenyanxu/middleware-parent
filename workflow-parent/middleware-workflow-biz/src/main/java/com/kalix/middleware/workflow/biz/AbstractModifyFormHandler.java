package com.kalix.middleware.workflow.biz;


import com.kalix.middleware.workflow.api.biz.IFormHandler;

/**
 * Created by Administrator on 2016/6/2.
 */
public abstract class AbstractModifyFormHandler implements IFormHandler {
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
}
