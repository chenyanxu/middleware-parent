/**
 * 流程定义组件
 *
 * @author majian
 * date:2015-6-18
 * @version 1.0.0
 */
Ext.define('kalix.workflow.processdefinition.Main', {
    extend: 'kalix.container.BaseContainer',
    requires: [
        'kalix.workflow.processdefinition.viewModel.ProcessDefinitionViewModel',
        'kalix.workflow.processdefinition.view.ProcessDefinitionGrid',
        'kalix.workflow.processdefinition.view.ProcessDefinitionSearchForm'
    ],
    storeId:'processDefinitionStore',
    viewModel:'processDefinitionViewModel',
    items: [
    {
        xtype: 'processDefinitionSearchForm',
        title: '流程定义查询'
    },
    {
        xtype: 'processDefinitionGrid',
        title: '流程定义列表',
        margin: 10
    }]
});