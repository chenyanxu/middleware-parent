package com.kalix.middleware.workflow.engine.listener;

import com.kalix.middleware.workflow.api.biz.IFormHandler;
import com.kalix.middleware.workflow.engine.manager.FormManager;

/**
 * Created with IntelliJ IDEA.
 * User: sunlf
 * Date: 14-1-24
 * Time: 下午6:17
 * Form监听者
 */
public class FormListener {
    /**
     * 监听到Form
     *
     * @param formHandler
     */
    public void bind(IFormHandler formHandler) {
        FormManager.getInstall().add(formHandler);
    }

    /**
     * Form被移除
     *
     * @param formHandler
     */
    public void unbind(IFormHandler formHandler) {
        FormManager.getInstall().remove(formHandler);
    }

}
