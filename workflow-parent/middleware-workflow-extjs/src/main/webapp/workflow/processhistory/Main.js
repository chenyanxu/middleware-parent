/**
 * 流程历史组件
 *
 * @author majian <br/>
 *         date:2015-6-18
 * @version 1.0.0
 */
Ext.define('kalix.workflow.processhistory.Main', {
    extend: 'kalix.container.BaseContainer',
    requires: [
        'kalix.workflow.processhistory.view.ProcessHistorySearchForm',
        'kalix.workflow.processhistory.view.ProcessHistoryGrid',
        'kalix.workflow.processhistory.viewModel.ProcessHistoryViewModel'
    ],
    storeId: 'processHistoryStore',
    viewModel: 'processHistoryViewModel',
    items: [{
        xtype: 'processHistorySearchForm',
        title: '流程历史查询'
    }, {
        xtype: 'processHistoryGrid',
        title: '流程历史列表',
        margin: 10
    }]
});