package com.kalix.middleware.workflow.api.biz;


import com.kalix.framework.core.api.biz.IBizService;
import com.kalix.framework.core.api.persistence.JsonData;
import com.kalix.middleware.workflow.entities.CategoryBean;

/**
 * @类描述：流程分类
 * @创建人： sunlf
 * @创建时间：2014/10/10
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
public interface ICategoryBeanService extends IBizService<CategoryBean> {
    /**
     * 通过分类获得流程定义
     * @param type
     * @return
     */
    public JsonData getAllWorkFlow(String type);
}
