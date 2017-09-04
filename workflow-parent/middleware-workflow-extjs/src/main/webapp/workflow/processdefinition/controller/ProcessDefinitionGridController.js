/**
 * 流程定义表格控制器
 *
 * @author majian <br/>
 *         date:2015-6-18
 * @version 1.0.0
 */
Ext.define('kalix.workflow.processdefinition.controller.ProcessDefinitionGridController', {
    extend: 'kalix.controller.BaseGridController',
    alias: 'controller.processDefinitionGridController',

    /**
     * 流程激活
     * @param grid
     * @param rowIndex
     * @param colIndex
     */
    onIsActivate: function (grid, rowIndex, colIndex) {
        var url = CONFIG.restRoot + 'camel/rest/workflow';
        var rec = grid.getStore().getAt(rowIndex);
        var postUrl;
        if (rec.data.suspensionState == 1) {
            postUrl = url + '/suspend?key=' + rec.data.key;
        } else {
            postUrl = url + '/activate?key=' + rec.data.key;
        }
        Ext.Ajax.request({
            url: postUrl,
            method: 'GET',
            callback: function (options, success, response) {
                var resp = Ext.JSON.decode(response.responseText);
                Ext.MessageBox.alert(CONFIG.ALTER_TITLE_INFO, resp.msg);
                if (resp.success) {
                    var store = grid.getStore();
                    store.reload();
                }
            }
        });
    },
    /**
     * 查看流程定义
     * @param grid
     * @param rowIndex
     * @param colIndex
     */
    onOpenProcessDefinition: function (grid, rowIndex, colIndex) {
        var rec = grid.getStore().getAt(rowIndex);
        var imgUrl = 'processDefinitionId=' + rec.data.id;
        var win = Ext.create('kalix.workflow.components.ActivitiProcessImageWindow', {
            title: '流程定义查看 - ' + rec.data.name,
            imgUrl: imgUrl
        });
        win.show();
    },
    onAdd: function () {
        var win = Ext.create('kalix.workflow.processdefinition.view.ProcessDefinitionWindow');

        win.show();
    }
});