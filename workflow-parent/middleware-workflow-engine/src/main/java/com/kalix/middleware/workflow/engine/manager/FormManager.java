package com.kalix.middleware.workflow.engine.manager;


import com.kalix.middleware.workflow.api.biz.IFormHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunlf on 2015/7/29.
 */
public class FormManager {

    private static FormManager install;
    //form集合
    private Map<String, List<IFormHandler>> formMap = new HashMap<>();

    private FormManager() {

    }

    public synchronized static FormManager getInstall() {
        if (install == null) {
            install = new FormManager();
        }
        return install;
    }

    public List<IFormHandler> getFormPanelMap(String key) {
        List<IFormHandler> list = formMap.get(key);
        return list;
    }


    /**
     * 根据ProcessDefinitionId添加
     *
     * @param formHandler
     */
    public void add(IFormHandler formHandler) {
        if(formHandler!=null)
        {
            List<IFormHandler> list;
            String name = formHandler.getProcessDefinitionId();
            if (formMap.containsKey(name)) {
                list = formMap.get(name);
                list.add(formHandler);
            } else {
                list = new ArrayList<>();
                list.add(formHandler);
                formMap.put(name, list);
            }
        }
    }

    public void remove(IFormHandler formHandler) {
        if(formHandler!=null){
            List<IFormHandler> list;
            String name = formHandler.getProcessDefinitionId();
            list = formMap.get(name);
            list.remove(formHandler);
            if (list.size() == 0) {
                formMap.remove(name);
            }
        }
    }


    /**
     * 通过流程id和formkey返回form实例
     *
     * @param DefinitionKey 流程定义主键
     * @param formKey      formkey
     * @return
     */
    public IFormHandler findFormByKey(String DefinitionKey, String formKey) {
        List<IFormHandler> formHandlerList = formMap.get(DefinitionKey);

        if(formHandlerList!=null) {
            for (IFormHandler form : formHandlerList) {
                if (form.getFormKey().equals(formKey))
                    return form;
            }
        }

        return null;
    }


}