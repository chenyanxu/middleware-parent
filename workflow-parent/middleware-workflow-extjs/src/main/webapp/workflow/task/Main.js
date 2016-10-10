/**
 * 待办流程首页
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
    ],
    storeId:'taskStore',
    items: [{
        xtype: 'taskSearchForm',
        title: '待办流程查询'
    }, {
        xtype: 'taskGrid',
        title: '待办流程列表'
    }]
});