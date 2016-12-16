package com.kalix.middleware.workflow.biz;


import com.kalix.middleware.workflow.api.biz.IFormHandler;

/**
 * Created by Administrator on 2016/6/2.
 */
public abstract class AbstractBizDataFormHandler implements IFormHandler {
    private String processDefinitionId;

    public AbstractBizDataFormHandler(){
        String[] splits=this.getClass().getSimpleName().split("BizData");

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
        return "bizData.form";
    }

    /**
     * 获得extjs的窗口类名称
     *
     * @return
     */
    @Override
    public String getWindowClass() {
        return "";
    }

    @Override
    public String getButtonValue() {
        throw new RuntimeException("not implements!");
    }
}
