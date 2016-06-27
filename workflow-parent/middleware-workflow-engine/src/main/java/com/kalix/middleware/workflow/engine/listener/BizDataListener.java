package com.kalix.middleware.workflow.engine.listener;

import com.kalix.middleware.workflow.api.biz.IBizDataHandler;
import com.kalix.middleware.workflow.engine.manager.BizDataManager;

/**
 * @类描述：业务数据监听者
 * @创建人： sunlingfeng
 * @创建时间：2014/9/12
 * @修改人：
 * @修改时间：
 * @修改备注：
 */

public class BizDataListener {
    /**
     * 监听到BizData
     *
     * @param bizDataHandler
     */
    public void bind(IBizDataHandler bizDataHandler) {
        BizDataManager.getInstall().put(bizDataHandler);

    }


    /**
     * Form被移除
     *
     * @param bizDataHandler
     */
    public void unbind(IBizDataHandler bizDataHandler) {
        BizDataManager.getInstall().remove(bizDataHandler);
    }

}
