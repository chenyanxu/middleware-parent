/**
 * 流程历史组件
 *
 * @author majian <br/>
 *         date:2015-6-18
 * @version 1.0.0
 */
Ext.define('kalix.workflow.myinvolvedprocesshistory.Main', {
    extend: 'kalix.workflow.processhistory.Main',
    items: [
        {
            xtype: 'processHistorySearchForm',
            title: '我参与的流程查询'
        },
        {
            xtype: 'processHistoryGrid',
            title: '我参与的流程列表',
            store: {
                type: 'processHistoryStore',
                proxyUrl: CONFIG.restRoot + '/camel/rest/workflow/myInvolvedHistory'
            }
        }
    ]
});