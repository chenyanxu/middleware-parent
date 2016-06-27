package com.kalix.middleware.workflow.api.biz;

import com.kalix.framework.core.api.biz.IBizService;
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
    JsonStatus startProcess(String id);
    JsonStatus completeTask(String taskId, String accepted, String comment);
}
