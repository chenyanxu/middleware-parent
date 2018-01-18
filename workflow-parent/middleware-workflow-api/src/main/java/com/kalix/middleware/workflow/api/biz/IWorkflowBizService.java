package com.kalix.middleware.workflow.api.biz;

import com.kalix.framework.core.api.biz.IBizService;
import com.kalix.framework.core.api.persistence.JsonData;
import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.api.persistence.PersistentEntity;

import java.util.Map;

/**
 * Created by Chenyanxu on 2016/6/24.
 */
public interface IWorkflowBizService<T extends PersistentEntity> extends IBizService<T> {
    //获得工作流主键名称
    String getProcessKeyName();
    //加入流程环节变量到map中
    Map getVariantMap(Map map, T bean);
    //添加处理人的名字到实体中
    void writeClaimResult(String currentTaskId, String userName, T bean);

    /**
     * 流程启动
     * @param id
     * @return
     */
    JsonStatus startProcess(String id);

    /**
     * 流程中止
     *
     * @param processInstanceId
     * @return
     */
    JsonStatus deleteProcess(String processInstanceId, String reason);

    /**
     * 用于流程中止后处理业务数据
     *
     * @param bean 业务实体
     */
    void afterDeleteProcess(T bean);

    /**
     * 用于流程启动前处理业务数据
     *
     * @param bean 业务实体
     */
    void beforeStartProcess(T bean);

    /**
     * 用于流程结束后处理业务数据
     *
     * @param bean   业务实体
     * @param result 审批结果
     */
    void afterFinishProcess(T bean, String result);
    String createBusinessNo(T bean);
    JsonStatus completeTask(String taskId, String accepted, String comment);
    JsonData getWorkFlowStatistic(String jsonStr);
}
