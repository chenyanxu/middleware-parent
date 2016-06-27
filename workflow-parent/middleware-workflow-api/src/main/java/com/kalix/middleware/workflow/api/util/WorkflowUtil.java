package com.kalix.middleware.workflow.api.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @类描述：${INPUT}
 * @创建人： sunlingfeng
 * @创建时间：2014/9/12
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
public class WorkflowUtil {
    public static String getProcessKey(String processId) {
        //拆分业务键，拆分成“业务对象名称”和“业务对象ID”的数组
        String processKey = null;
        if (StringUtils.isNotBlank(processId)) {
            processKey = processId.split(":")[0];
        }
        return processKey;
    }

    public static String getBizId(String businessKey) {
        //拆分业务键，拆分成“业务对象名称”和“业务对象ID”的数组
        String beanId = null;
        if (StringUtils.isNotBlank(businessKey)) {
            beanId = businessKey.split(":")[1];
        }
        return beanId;
    }
}
