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
        },
        {
            xtype: 'textfield',
            fieldLabel: '启动用户',
            labelAlign: 'right',
            labelWidth: 60,
            width: 200,
            name: 'startUser'
        },
        /*{
            xtype: 'datefield',
            format: 'Y-m-d',
         fieldLabel: '开始时间:',
            labelAlign: 'right',
            labelWidth: 60,
            width: 200,
         name: 'startDate'
        },
        {
            xtype: 'label',
            text: '-',
            margin: '5 5 0 5'
        },
        {
            xtype: 'datefield',
            format: 'Y-m-d',
            headLabel: true,
            labelAlign: 'right',
            width: 140,
         name: 'endDate'
         }*/
    ]
});
