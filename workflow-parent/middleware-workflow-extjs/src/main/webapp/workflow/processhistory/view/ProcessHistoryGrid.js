/**
 * 流程历史表格
 * @author majian <br/>
 *         date:2015-7-3
 * @version 1.0.0
 */
Ext.define('kalix.workflow.processhistory.view.ProcessHistoryGrid', {
    extend: 'kalix.view.components.common.BaseGrid',
    requires: [
        'kalix.workflow.processhistory.store.ProcessHistoryStore',
        'kalix.workflow.processhistory.controller.ProcessHistoryGridController'
    ],
    alias: 'widget.processHistoryGrid',
    xtype: 'processHistoryGrid',
    controller: {
        type: 'processHistoryGridController',
        storeId: 'processHistoryStore',
        cfgModel: 'kalix.workflow.processhistory.model.ProcessHistoryModel'
    },
    viewModel: {
        type: 'processHistoryViewModel'
    },
    store: {
        type: 'processHistoryStore'
    },
    columns: {
        defaults: {
            flex: 1,
            renderer: 'addTooltip'
        },
        items: [
            {
                xtype: "rownumberer",
                text: "行号",
                width: 50,
                flex: 0,
                align: 'center',
                renderer: null
            },
            {
                text: '编号',
                dataIndex: 'id',
                hidden: true
            },
            {
                text: '业务主键',
                dataIndex: 'businessKey'
            },
            {
                text: '流程定义编号',
                dataIndex: 'processDefinitionId'
            },
            {
                text: '启动用户',
                dataIndex: 'startUserId'
            },
            {
                text: '开始时间',
                dataIndex: 'startTime',
                /*xtype: 'datecolumn',
                format: 'Y-m-d H:i:s',
                 renderer: null*/
                /*renderer: function (value) {
                    var createDate = new Date(value);
                    return createDate.format("yyyy-MM-dd hh:mm:ss");
                 }*/
            },
            {
                text: '结束时间',
                dataIndex: 'endTime',
                /*renderer: function (value) {
                    if (value != null && value != "") {
                        var createDate = new Date(value);
                        return createDate.format("yyyy-MM-dd hh:mm:ss");
                    } else {
                        return "";
                    }
                 }*/
            },
            {
               text: '持续时长',
               dataIndex: 'durationInMillis'
           },
            {
                text: '状态',
                dataIndex: 'status'
            },
            {
                header: '操作',
                xtype: "actioncolumn",
                items: [{
                    iconCls: 'iconfont icon-history',
                    tooltip: '查看流程历史',
                    handler: 'onOpenHistoryActivity'
                }]
            }
        ]
    }
});