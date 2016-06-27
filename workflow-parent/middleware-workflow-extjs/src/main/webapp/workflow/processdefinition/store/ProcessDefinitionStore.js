/**
 * 流程定义数据仓库
 *
 * @author majian <br/>
 *         date:2015-7-3
 * @version 1.0.0
 */
Ext.define('kalix.workflow.processdefinition.store.ProcessDefinitionStore', {
    extend: 'kalix.store.BaseStore',
    model: 'kalix.workflow.processdefinition.model.ProcessDefinitionModel',
    alias: 'store.processDefinitionStore',
    xtype: 'processDefinitionStore',
    storeId: "processDefinitionStore",
    proxyUrl: CONFIG.restRoot+'/camel/rest/workflow/'
});