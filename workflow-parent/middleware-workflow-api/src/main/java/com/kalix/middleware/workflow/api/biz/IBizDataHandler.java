package com.kalix.middleware.workflow.api.biz;

/**
 * 实现工作流业务数据的统一接口,用于流程历史查看
 */
public interface IBizDataHandler {

    /**
     * 返回业务数据的描述
     *
     * @return
     */
    String getBizName();


    /**
     * 获得form的实现Extjs类
     *
     * @return
     */
    String getComponentClass();

    /**
     * 获得流程定义名称
     *
     * @return
     */
    String getProcessDefinitionId();
}
