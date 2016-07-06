/**
 * 流程定义表格
 * @author majian <br/>
 *         date:2015-7-3
 * @version 1.0.0
 */
Ext.define('kalix.workflow.activityhistory.view.ActivityHistoryGrid', {
    extend: 'kalix.view.components.common.BaseGrid',
    requires: [
        'kalix.workflow.activityhistory.store.ActivityHistoryStore'
    ],
    alias: 'widget.activityHistoryGrid',
    xtype: 'activityHistoryGrid',
    autoLoad: false,
    stripeRows: true,
    manageHeight: true,
    store: {
        type: 'activityHistoryStore'
    },
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
                renderer: null
            },
            {text: '编号', dataIndex: 'id', flex: 1, hidden: true},
            {text: '节点名称', dataIndex: 'activityName'},
            {text: '执行人', dataIndex: 'assignee'},
            {text: '开始时间', dataIndex: 'startTime'},
            {text: '结束时间', dataIndex: 'endTime'},
            {text: '持续时长', dataIndex: 'durationInMillis'},
            {text: '审批意见', dataIndex: 'comment'}
        ]
    }
});