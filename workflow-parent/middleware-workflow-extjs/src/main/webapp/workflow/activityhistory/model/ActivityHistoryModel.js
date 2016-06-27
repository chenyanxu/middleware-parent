/**
 * 用户模型
 *
 * @author majian <br/>
 *         date:2015-7-3
 * @version 1.0.0
 */
Ext.define('kalix.workflow.activityhistory.model.ActivityHistoryModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'id', type: 'string'},
        {name: 'activityName', type: 'string'},//节点名称
        {name: 'assignee', type: 'string'},//执行人
        {name: 'startTime', type: 'string'},//开始时间
        {name: 'endTime', type: 'string'},//结束时间
        {name: 'durationInMillis', type: 'string'},//任务持续时长
        {name: 'comment', type: 'string'},//审批意见
        //{name: 'taskId', type: 'string'},//任务编号
    ]
});