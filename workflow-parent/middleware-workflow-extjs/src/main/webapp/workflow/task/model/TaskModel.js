/**
 * 待办流程模型
 *
 * @author majian <br/>
 *         date:2015-7-3
 * @version 1.0.0
 */
Ext.define('kalix.workflow.task.model.TaskModel', {
    extend: 'kalix.model.BaseModel',
    fields: [
        {name: 'name', type: 'string'},//任务名称
        {name: 'businessNo', type: 'string'},//流水号
        {name: 'description', type: 'string'},//描述
        {name: 'assignee', type: 'string'},//执行人
    ]
});