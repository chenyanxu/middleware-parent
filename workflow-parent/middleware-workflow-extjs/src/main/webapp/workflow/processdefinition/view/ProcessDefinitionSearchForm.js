/**
 * 流程定义查询表单
 * @author
 * @version 1.0.0
 */
Ext.define('kalix.workflow.processdefinition.view.ProcessDefinitionSearchForm', {
    extend: 'kalix.view.components.common.BaseSearchForm',
    alias: 'widget.processDefinitionSearchForm',
    xtype: 'processDefinitionSearchForm',
    storeId: 'processDefinitionStore',
    items: [
        {
            xtype: 'textfield',
            fieldLabel: '流程定义名称',
            labelAlign: 'right',
            labelWidth: 100,
            width: 250,
            name: 'name'
        }
    ]
});
