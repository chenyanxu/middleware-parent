/**
 * 流程定义表格
 * @author majian <br/>
 *         date:2015-7-3
 * @version 1.0.0
 */
Ext.define('kalix.workflow.activityhistory.view.ActivityHistoryGrid', {
    extend: 'kalix.view.components.common.BaseGrid',
    requires: [
        'kalix.workflow.activityhistory.store.ActivityHistoryStore',
        'kalix.controller.BaseGridController'
    ],
    alias: 'widget.activityHistoryGrid',
    xtype: 'activityHistoryGrid',
    autoLoad: false,
    stripeRows: true,
    manageHeight: true,
    plugins: ['zorderPlugin'],
    store: {
        type: 'activityHistoryStore'
    },
    controller: 'baseGridController',
    columns: [
        {xtype: 'rownumberer'},
        {text: '编号', dataIndex: 'id', hidden: true},
        {text: '节点名称', dataIndex: 'activityName'},
        {text: '执行人', dataIndex: 'assignee'},
        {text: '开始时间', dataIndex: 'startTime'},
        {text: '结束时间', dataIndex: 'endTime'},
        {text: '持续时长', dataIndex: 'durationInMillis'},
        {text: '审批结果', dataIndex: 'result'},
        {text: '审批意见', dataIndex: 'comment'}
    ]
});