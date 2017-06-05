/**
 * 流程分类表格
 * @author
 * @version 1.0.0
 */
Ext.define('kalix.workflow.category.view.CategoryGrid', {
    extend: 'kalix.view.components.common.BaseGrid',
    requires: [
        'kalix.workflow.category.controller.CategoryGridController',
        'kalix.workflow.category.store.CategoryStore'
    ],
    alias: 'widget.categoryGrid',
    xtype: 'categoryGridPanel',
    controller: {
        type: 'categoryGridController',
        storeId: 'categoryStore',
        cfgForm: 'kalix.workflow.category.view.CategoryWindow',
        cfgViewForm: 'kalix.workflow.category.view.CategoryViewWindow',
        cfgModel: 'kalix.workflow.category.model.CategoryModel'
    },
    store: {
        type: 'categoryStore'
    },

    columns: [
        {
            xtype: 'rownumberer',
            text: '行号',
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
            text: '分类名称',
            dataIndex: 'name'
        },
        {
            text: '分类主键',
            dataIndex: 'key'
        },
        {
            text: '分类图标',
            dataIndex: 'icon'
        },
        {
            text: '分类描述',
            dataIndex: 'description'
        },

        {
            xtype: 'securityGridColumnCommon',
            items: [
                {
                    iconCls: 'iconfont icon-view-column',
                    permission: 'view',
                    tooltip: '查看',
                    handler: 'onView'
                },
                {
                    iconCls: 'iconfont icon-edit-column',
                    permission: 'edit',
                    tooltip: '编辑',
                    handler: 'onEdit'
                },
                {
                    iconCls: 'iconfont icon-delete',
                    permission: 'delete',
                    tooltip: '删除',
                    handler: 'onDelete'
                }
            ]
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
