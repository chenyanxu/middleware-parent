package com.kalix.middleware.workflow.api.model;

/**
 * @类描述：${INPUT}
 * @创建人： sunlingfeng
 * @创建时间：2014/9/9
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
public interface WorkflowStatus {
    public static short INACTIVE = 0;//未申请
    public static short ACTIVE = 1;//处理中
    public static short FINISH = 2;//结束
    public static short DELETE = 3;//中止
}
