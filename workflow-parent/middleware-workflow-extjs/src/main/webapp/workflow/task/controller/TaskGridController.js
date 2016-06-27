/**
 * 待办任务控制器
 *
 * @author majian <br/>
 *         date:2015-6-18
 * @version 1.0.0
 */
Ext.define('kalix.workflow.task.controller.TaskGridController', {
        extend: 'kalix.controller.BaseGridController',
    requires: [
        'kalix.workflow.approve.view.ApproveWindow'
    ],
        alias: 'controller.taskGridController',
        /**
         * 查看当前环节
         * @param grid
         * @param rowIndex
         * @param colIndex
         */
        onOpenCurrentProcess: function (grid, rowIndex, colIndex) {
            var rec = grid.getStore().getAt(rowIndex);
            var imgUrl = this.getView().getViewModel().get('processShowUrl') + '?taskId=' + rec.data.id;
            var win = Ext.create('kalix.workflow.components.ActivitiProcessImageWindow', {
                title: '流程查看 - ' + rec.data.name,
                imgUrl: imgUrl
            });
            win.show();
        },
        /**
         * 处理任务项
         * @param grid
         * @param rowIndex
         * @param colIndex
         */
        onCompleteTask: function (grid, rowIndex, colIndex) {
            var rec = grid.getStore().getAt(rowIndex);
            var businessKey = rec.data.businessKey;
            var key = businessKey.split(':');
            var bizUrl = key[0];//获得bizkey的头 例如：carapply
            Ext.Ajax.request({
                url: CONFIG.restRoot + '/camel/rest/' + bizUrl + 's/' + rec.data.entityId,
                method: "GET",
                callback: function (options, success, response) {
                    var entity = Ext.JSON.decode(response.responseText);

                    if (entity == null) {
                        Ext.Msg.alert(CONFIG.ALTER_TITLE_FAILURE, "实体不能为空.");
                        return;
                    }

                    Ext.Ajax.request({
                        url: CONFIG.restRoot + '/camel/rest/workflow/form?taskId=' + rec.data.id,
                        method: "GET",
                        callback: function (options, success, response) {
                            var component = Ext.JSON.decode(response.responseText);
                            if(component.windowClass == null || component.windowClass == ''){
                                Ext.Msg.alert(CONFIG.ALTER_TITLE_FAILURE, "没有窗口类.");
                                return;
                            }
                            //var approvalWindow = Ext.create('kalix.workflow.approve.view.ApproveWindow');
                            var approvalWindow = Ext.create(component.windowClass);
                            var vm = approvalWindow.lookupViewModel();

                            vm.set('title', rec.data.name);
                            vm.set('rec', entity);
                            vm.set('taskId', rec.data.id);
                            vm.set('businessKey', bizUrl);

                            approvalWindow.insert(0, Ext.create(component.formClass, {
                                layout: {
                                    type: 'table',
                                    columns: 6
                                }
                            }));

                            approvalWindow.show();
                            approvalWindow.items.getAt(1).items.getAt(0).getStore().getProxy().extraParams = {historyProcessId: rec.data.processInstanceId};
                            approvalWindow.items.getAt(1).items.getAt(0).getStore().load();
                        }
                    });
                }
            });
        },
        /**
         * 委托操作.
         */
        onDelegate: function () {
            var grid = this.getView();
            var selModel = grid.getSelectionModel();
            var viewModel = grid.getViewModel();
            if (selModel.hasSelection()) {
                Ext.Msg.confirm("警告", "确定要委托吗？", function (button) {
                    if (button == "yes") {
                        var rows = selModel.getSelection();
                        var ids = "";
                        for (var i = 0; i < rows.length; i++) {
                            if (rows[i] != null && rows[i].id != null) {
                                ids += rows[i].id;
                                if (i + 1 != rows.length) {
                                    ids += ":";
                                }
                            }
                        }
                        // viewModel.set('taskIds',ids);
                        var view = Ext.create('kalix.workflow.task.view.TaskDelegateWindow', {
                            bind: {icon: '{delegate_image_path}'}
                        });

                        view.lookupViewModel().set('taskIds', ids);
                        view.show();

                    }
                });
            } else {
                Ext.Msg.alert(CONFIG.ALTER_TITLE_ERROR, "请选择要操作的记录！");
            }
        }
    }
);