/**
 * 流程历史查询表单
 * @author
 * @version 1.0.0
 */
Ext.define('kalix.workflow.processhistory.view.ProcessHistorySearchForm', {
    extend: 'kalix.view.components.common.BaseSearchForm',
    alias: 'widget.processHistorySearchForm',
    xtype: 'processHistorySearchForm',
    storeId: 'processHistoryStore',
    items: [
        {
            xtype: 'textfield',
            fieldLabel: '流程编号',
            labelAlign: 'right',
            labelWidth: 60,
            width: 200,
            name: 'name'
        }
    ]
});
