/**
 * Created by Administrator on 2016-03-03.
 */
Ext.define('kalix.workflow.task.view.TaskDelegateWindow', {
    extend: 'Ext.window.Window',
    requires: [
        'kalix.workflow.task.viewModel.TaskViewModel',
        'kalix.workflow.task.controller.TaskDelegateController',
        'kalix.admin.user.component.UserComboBox'
    ],
    alias: 'widget.taskDelegateWindow',
    xtype: "taskDelegateWindow",
    controller: 'taskDelegateController',
    viewModel:'taskViewModel',
    width: 400,
    border: false,
    modal: true,
    resizable: false,
    title:'请选择委托人',
    buttonAlign: 'center',
    iconCls: 'iconfont icon-workflow-delegate',
    layout: {
        type: 'hbox',
        align: 'stretch'
    },
    viewModel: 'taskViewModel',
    items: [{
        fieldLabel: '委托人',
        xtype: 'userCombobox',
        name: 'delegateId',
        valueField: 'loginName',
        bind: {
            value: '{delegateId}'
        }
    }],
    defaults: {
        layout: 'form',
        xtype: 'baseForm',
        flex: 1,
        defaults: {
            labelAlign: 'right'
        }
    },
    buttons: [{
        text: '提交',
        iconCls:'iconfont icon-save iconfont-btn-small',
        handler: 'onDelegateTask'
    }, {
        text: '重置',
        iconCls:'iconfont icon-reset iconfont-btn-small',
        handler: 'onReset'
    }]
});