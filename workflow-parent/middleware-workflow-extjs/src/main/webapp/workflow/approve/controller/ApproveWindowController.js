/**
 * @author chenyanxu
 */
Ext.define('kalix.workflow.approve.controller.ApproveWindowController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.approveWindowController',
    requires: ['kalix.Notify'],
    onApprove: function () {
        var scope = this;
        var status, approveOpinion, businessKey, taskId;

        status = arguments[0].text;
        approveOpinion = this.getViewModel().get('approveOpinion');
        businessKey = this.getViewModel().get('businessKey');
        taskId = this.getViewModel().get('taskId');

        Ext.Ajax.request({
            url: CONFIG.restRoot + '/camel/rest/' + businessKey + 's/workflow/completeTask?taskId=' + taskId + "&accepted=" + status + "&comment=" + approveOpinion,
            method: "GET",
            callback: function (options, success, response) {
                var jsonStatus = Ext.JSON.decode(response.responseText);
                if (jsonStatus.success) {
                    kalix.Notify.success(jsonStatus.msg, CONFIG.ALTER_TITLE_SUCCESS);
                    Ext.app.Application.instance.getApplication().getStore('taskStore').reload();
                    scope.getView().close();
                }
                else {
                    Ext.MessageBox.alert(CONFIG.ALTER_TITLE_FAILURE, jsonStatus.msg);
                }
            }
        });
    },
    onEnd: function () {
        this.onApprove(arguments[0]);
    },
    onModify: function () {
        //var sealModifyForm = this.getView().items.items[0];
        var businessKey;
        var entity=this.getViewModel().get('rec');
        businessKey = this.getViewModel().get('businessKey');

        Ext.Ajax.request({
            url: CONFIG.restRoot + '/camel/rest/'+businessKey+'s/'+entity.id,
            scope: this,
            defaultPostHeader : 'application/json;charset=utf-8',
            method: 'PUT',
            params :Ext.encode(entity),
            callback: function (options, success, response) {
                var rtn = Ext.JSON.decode(response.responseText);

                if (rtn.success) {
                    this.onApprove();
                }
                else{

                }
            }
        });


        //this.onApprove(btn);
    }
});
