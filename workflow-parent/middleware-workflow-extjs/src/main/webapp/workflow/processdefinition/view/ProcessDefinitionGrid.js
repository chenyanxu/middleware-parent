/**
 * 流程定义表格
 * @author majian <br/>
 *         date:2015-7-3
 * @version 1.0.0
 */
Ext.define('kalix.workflow.processdefinition.view.ProcessDefinitionGrid', {
    extend: 'kalix.view.components.common.BaseGrid',
    requires: [
        'kalix.workflow.processdefinition.store.ProcessDefinitionStore',
        'kalix.workflow.processdefinition.controller.ProcessDefinitionGridController'
    ],
    alias: 'widget.processDefinitionGrid',
    xtype: 'processDefinitionGrid',
    controller: {
        type: 'processDefinitionGridController',
        cfgModel: 'kalix.workflow.processdefinition.model.ProcessDefinitionModel'
    },
    store: {
        type: 'processDefinitionStore'
    },
    columns: [
        {
            xtype: 'rownumberer'
        },
        {
            text: '流程定义编号',
            dataIndex: 'id'
        },
        {
            text: '流程定义名称',
            dataIndex: 'name'
        },
        {
            text: '关键字',
            dataIndex: 'key'
        },
        {
            text: '描述',
            dataIndex: 'description'
        },
        {
            text: '版本',
            dataIndex: 'version'
        },
        {
            text: '状态', dataIndex: 'suspensionState',
            renderer: function (value) {
                if (value == 1)
                    return '有效';
                else
                    return '无效';
            }
        },
        {
            xtype: 'securityGridColumnCommon',
            verifyItems: [
                /*{
                 iconCls: 'iconfont icon-edit-column',
                 tooltip: '编辑',
                 handler: 'onEdit',
                 permission: 'edit'
                 },*/
                {
                    itemId: 'activateButton',
                    getClass: function (v, meta, record) {
                        if (record.data.suspensionState == 1) {
                            return 'iconfont icon-stop';
                        }
                        return 'iconfont icon-start';
                    },
                    getTip: function (value, metadata, record, row, col, store) {
                        if (record.data.suspensionState == 1) {
                            return '无效';
                        }
                        return '有效';
                    },
                    handler: 'onIsActivate',
                    permission: 'activate'
                },
                {
                    iconCls: 'iconfont icon-view-column',
                    tooltip: '查看',
                    handler: 'onOpenProcessDefinition',
                    permission: 'view'
                }]
        }
    ],
    tbar: {
        xtype: 'securityToolbar',
        verifyItems: [
            {
                text: '添加',
                xtype: 'button',
                permission: 'add',
                iconCls: 'iconfont icon-add',
                handler: 'onAdd'
            }
        ]
    }
});