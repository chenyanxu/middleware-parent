/**
 * @author chenyanxu
 * mixin to other class
 * the function use to operate on workflow
 */
Ext.define('kalix.workflow.common.mixins.Workflow', {
    onWorkFlowStart: function (grid, rowIndex, colIndex) {
        var rec = grid.getStore().getAt(rowIndex);
        var postUrl;

        postUrl = CONFIG.restRoot + '/camel/rest/' + this.type.split('Grid')[0].toLowerCase() + 's/workflow/startProcess?id=' + rec.data.id;

        Ext.Ajax.request({
            url: postUrl,
            method: 'GET',
            callback: function (options, success, response) {
                var resp = Ext.JSON.decode(response.responseText);

                if (resp.success) {
                    kalix.Notify.success(resp.msg, CONFIG.ALTER_TITLE_INFO);

                    var store = grid.getStore();
                    store.reload();
                    return;
                }
                Ext.MessageBox.alert(CONFIG.ALTER_TITLE_FAILURE, resp.msg);
            }
        });
    },
    onViewCurrentProcess: function (grid, rowIndex, colIndex) {
        var rec = grid.getStore().getAt(rowIndex);
        var imgUrl = 'processInstanceId=' + rec.data.processInstanceId;
        var win = Ext.create('kalix.workflow.components.ActivitiProcessImageWindow', {
            title: '当前流程 - 编号[' + rec.data.businessNo + ']',
            imgUrl: imgUrl
        });
        win.show();
    }
});