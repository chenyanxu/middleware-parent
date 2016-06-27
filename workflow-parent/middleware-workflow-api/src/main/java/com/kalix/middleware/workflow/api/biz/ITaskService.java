package com.kalix.middleware.workflow.api.biz;

import com.kalix.framework.core.api.IService;
import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.api.persistence.JsonData;

/**
 * Created by sunlf on 2015/7/31.
 * 工作流任务相关接口定义
 */
public interface ITaskService extends IService {
    JsonData getTasks(int page, int limit, String jsonStr);

    String getStartUserName(String processInstanceId);

    /**
     * 为task设置代理人
     *
     * @param taskIds 任务id list，以：分割
     * @param userId  代理人id
     * @return
     */
    JsonStatus delegateTask(String taskIds, String userId);
}
