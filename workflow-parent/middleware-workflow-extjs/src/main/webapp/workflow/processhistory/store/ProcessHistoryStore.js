/**
 * 流程历史数据仓库
 *
 * @author majian <br/>
 *         date:2015-7-3
 * @version 1.0.0
 */
Ext.define('kalix.workflow.processhistory.store.ProcessHistoryStore', {
    extend: 'kalix.store.BaseStore',
    model: 'kalix.workflow.processhistory.model.ProcessHistoryModel',
    alias: 'store.processHistoryStore',
    xtype: 'processHistoryStore',
    storeId: "processHistoryStore",
    proxyUrl: CONFIG.restRoot + '/camel/rest/workflow/historys'
});