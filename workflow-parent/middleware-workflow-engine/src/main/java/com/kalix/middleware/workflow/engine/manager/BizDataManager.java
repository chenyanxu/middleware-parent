package com.kalix.middleware.workflow.engine.manager;


import com.kalix.middleware.workflow.api.biz.IBizDataHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @类描述：业务数据管理类
 * @创建人： sunlingfeng
 * @创建时间：2014/9/12
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
public class BizDataManager {
    private static BizDataManager install;

    //业务数据map
    private Map<String, IBizDataHandler> bizDataMap = new HashMap<>();

    private BizDataManager() {

    }

    public synchronized static BizDataManager getInstall() {
        if (install == null) {
            install = new BizDataManager();
        }
        return install;
    }

    /**
     * 根据ProcessDefinitionId添加
     *
     * @param bizDataHandler
     */
    public void put(IBizDataHandler bizDataHandler) {
        if(bizDataHandler!=null){
            if (!bizDataMap.containsKey(bizDataHandler.getProcessDefinitionId())) {
                bizDataMap.put(bizDataHandler.getProcessDefinitionId(), bizDataHandler);
            }
        }
    }

    /**
     * 返回panel根据processDefinitionId
     *
     * @param processDefinitionId
     * @return
     */
    public IBizDataHandler findPanelByKey(String processDefinitionId) {
        return bizDataMap.get(processDefinitionId);
    }

    /**
     * 根据ProcessDefinitionId remove
     *
     * @param bizDataHandler
     */
    public void remove(IBizDataHandler bizDataHandler) {
        if(bizDataHandler!=null)
        {
            if (bizDataMap.containsKey(bizDataHandler.getProcessDefinitionId())) {
                bizDataMap.remove(bizDataHandler.getProcessDefinitionId());
            }
        }
    }
}
