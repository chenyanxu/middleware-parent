/**
 * 待办任务控制器
 *
 * @author majian <br/>
 *         date:2015-6-18
 * @version 1.0.0
 */
Ext.define('kalix.workflow.task.controller.TaskDelegateController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.taskDelegateController',

    onReset: function () {
        var viewModel = this.getViewModel();
        viewModel.set('delegateId','');
    },

    onDelegateTask: function () {
        var viewModel = this.getViewModel();
        var view=this.getView();
        var delegateUrl = viewModel.get("delegateUrl");
        var delegateId = viewModel.get("delegateId");
        var taskIds = viewModel.get("taskIds");
        Ext.Ajax.request({
            url: delegateUrl + "?taskIds=" + taskIds+"&userId="+delegateId,
            method: 'GET',
            callback: function (options, success, response) {
                var resp = Ext.JSON.decode(response.responseText);

                if (resp.success) {
                    Ext.app.Application.instance.getApplication().getStore('taskStore').load();
                    kalix.Notify.success(resp.msg, CONFIG.ALTER_TITLE_SUCCESS);
                }
                else{
                    Ext.MessageBox.alert(CONFIG.ALTER_TITLE_INFO, resp.msg);
                }
                view.close();
            }
        });
    }
})
