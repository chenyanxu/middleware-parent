/**
 * 待办任务首页
 *
 * @author majian
 * date:2015-6-18
 * @version 1.0.0
 */
Ext.define('kalix.workflow.task.Main', {
    extend: 'kalix.container.BaseContainer',
    requires: [
        'kalix.workflow.task.view.TaskSearchForm',
        'kalix.workflow.task.view.TaskGrid',
        'kalix.workflow.task.viewModel.TaskViewModel'
    ],
    storeId:'taskStore',
    viewModel:'taskViewModel',
    items: [{
        xtype: 'taskSearchForm',
        title: '待办任务查询'
    }, {
        xtype: 'taskGrid',
        title: '待办任务列表',
        margin: 10
    }]
});