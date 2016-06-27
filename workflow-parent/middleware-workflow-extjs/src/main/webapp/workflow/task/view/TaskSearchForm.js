/**
 * 待办任务查询表单
 * @author
 * @version 1.0.0
 */
Ext.define('kalix.workflow.task.view.TaskSearchForm', {
    extend: 'kalix.view.components.common.BaseSearchForm',
    alias: 'widget.taskSearchForm',
    xtype: 'taskSearchForm',
    storeId:'taskStore',
    items: [
        {
            xtype: 'textfield',
            fieldLabel: '任务名称',
            labelAlign: 'right',
            labelWidth: 60,
            width: 200,
            name: 'name'
        }
    ]
});
