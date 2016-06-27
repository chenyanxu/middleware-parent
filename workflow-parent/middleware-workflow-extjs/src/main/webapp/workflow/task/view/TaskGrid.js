/**
 * 待办任务定义表格
 * @author majian <br/>
 *         date:2015-7-3
 * @version 1.0.0
 */
Ext.define('kalix.workflow.task.view.TaskGrid', {
    extend: 'kalix.view.components.common.BaseGrid',
    requires: [
        'kalix.workflow.task.controller.TaskGridController',
        'kalix.workflow.task.store.TaskStore'
    ],
    alias: 'widget.taskGrid',
    xtype: 'taskGrid',
    controller: {
        type: 'taskGridController',
        storeId: 'taskStore',
        /*cfgForm: 'kalix.roffice.chance.view.TaskWindow',
         cfgViewForm: 'kalix.roffice.chance.view.TaskViewWindow',*/
        cfgModel: 'kalix.workflow.task.model.TaskModel'
    },
    viewModel: {
        type: 'taskViewModel'
    },
    store: {
        type: 'taskStore'
    },
    stripeRows: true,
    manageHeight: true,
    forceFit: true,
    selModel: {selType: 'checkboxmodel', mode: 'simple'},
    columns: {
        defaults: {
            flex: 1
        },
        items: [
            {
                xtype: "rownumberer",
                text: "行号",
                width: 50,
                flex: 0,
                align: 'center',
                renderer: this.update
            },
            {
                text: '编号',
                dataIndex: 'id',
                hidden: true

            },
            {
                text: '业务主键',
                dataIndex: 'businessKey',
                renderer: 'addTooltip'
            },
            {
                text: '任务名称',
                dataIndex: 'name',
                renderer: 'addTooltip'
            },
            {
                text: '任务描述',
                dataIndex: 'description',
                renderer: 'addTooltip'
            },
            /*{
             text: '执行人',
             dataIndex: 'assignee'
             },*/
            {
                text: '创建时间',
                dataIndex: 'createTime',
                renderer: 'addTooltip'
            },
            {
                header: '操作',
                xtype: "actioncolumn",
                flex: 1,
                items: [
                    {
                        iconCls: 'iconfont icon-current-process',
                        tooltip: '查看进度',
                        handler: 'onOpenCurrentProcess'
                    },
                    {
                        iconCls: 'iconfont icon-workflow-approval',
                        tooltip: '流程审批',
                        handler: 'onCompleteTask'
                    }
                ]
            }]
    },
    tbar: {
        xtype: 'securityToolbar',
        verifyItems: [
            {
                text: '委托',
                xtype: 'button',
                permission: '',
                iconCls: 'iconfont icon-workflow-delegate',
                handler: 'onDelegate'
            }
        ]
    }
});