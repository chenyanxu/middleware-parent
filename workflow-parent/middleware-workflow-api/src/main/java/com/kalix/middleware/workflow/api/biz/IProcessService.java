package com.kalix.middleware.workflow.api.biz;


import com.kalix.framework.core.api.IService;
import com.kalix.framework.core.api.persistence.JsonData;
import com.kalix.framework.core.api.persistence.JsonStatus;

/**
 * Created by sunlf on 2015/7/30.
 * 流程服务接口
 */
public interface IProcessService extends IService {
    JsonData getProcessDefinition(int page, int limit, String jsonStr);

    JsonStatus activateProcessDefinition(String key);

    JsonStatus suspendProcessDefinition(String key);

    JsonData getProcessHistory(int page, int limit, String jsonStr);

    JsonData getMyProcessHistory(int page, int limit, String jsonStr);

    JsonData getMyInvolvedProcessHistory(int page, int limit, String jsonStr);

    JsonData getHistoricActivity(String historyProcessId);

    JsonData getAllProcessNameAndKey();
}
