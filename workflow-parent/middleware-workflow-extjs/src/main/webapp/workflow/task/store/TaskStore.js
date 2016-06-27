/**
 * 待办任务数据仓库
 *
 * @author majian <br/>
 *         date:2015-7-3
 * @version 1.0.0
 */
Ext.define('kalix.workflow.task.store.TaskStore', {
    extend: 'kalix.store.BaseStore',
    model: 'kalix.workflow.task.model.TaskModel',
    alias: 'store.taskStore',
    xtype: 'taskStore',
    storeId: "taskStore",
    proxyUrl: CONFIG.restRoot+'/camel/rest/workflow/tasks'
});