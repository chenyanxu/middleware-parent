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
        cfgModel: 'kalix.workflow.processhistory.model.ProcessHistoryModel'
    },
    store: {
        type: 'processHistoryStore'
    },
    columns: [
        {
            xtype: 'rownumberer'
        },
        {
            text: '编号',
            dataIndex: 'id',
            hidden: true
        },
        {
            text: '编号',
            dataIndex: 'name',
            flex: 1.5
        },
        {
            text: '业务名称',
            dataIndex: 'title'
        },
        {
            text: '启动用户',
            dataIndex: 'startUserId'
        },
        {
            text: '开始时间',
            dataIndex: 'startTime'
            /*xtype: 'datecolumn',
             format: 'Y-m-d H:i:s',
             renderer: null*/
            /*renderer: function (value) {
             var createDate = new Date(value);
             return createDate.format('yyyy-MM-dd hh:mm:ss');
             }*/
        },
        {
            text: '结束时间',
            dataIndex: 'endTime'
            /*renderer: function (value) {
             if (value != null && value != '') {
             var createDate = new Date(value);
             return createDate.format('yyyy-MM-dd hh:mm:ss');
             } else {
             return '';
             }
             }*/
        },
        {
            text: '持续时长',
            dataIndex: 'durationInMillis'
        },
        {
            text: '状态',
            xtype: 'templatecolumn',
            tpl: '<tpl if="status==\'结束\'"><span style="color:red"></tpl>{status}<tpl if="status==\'结束\'"></span></tpl>'
        },
        {
            xtype: 'securityGridColumnCommon',
            verifyItems: [{
                iconCls: 'iconfont icon-history',
                tooltip: '查看流程历史',
                handler: 'onOpenHistoryActivity',
                permission: 'view'
            }]
        }
    ]

});