/**
 * 流程分类新增和修改表单
 *
 * @author
 * @version 1.0.0
 */
Ext.define('kalix.workflow.category.view.CategoryWindow', {
    extend: 'kalix.view.components.common.BaseWindow',
    requires: [
        //'kalix.workflow.category.viewModel.CategoryViewModel',
        'kalix.controller.BaseWindowController',
        'kalix.admin.user.store.UserStore'
    ],
    alias: 'widget.categoryWindow',
    //viewModel: 'categoryViewModel',
    controller: {
        type: 'baseWindowController',
        storeId: 'categoryStore'
    },
    xtype: "categoryWindow",
    width: 400,
    //todo 在此修改表单
    items: [

        {
        xtype: 'baseForm',
        items: [
            	{
            		fieldLabel: '分类名称',
            		allowBlank: false,
            		bind: {
            			value: '{rec.name}'
            		}
            	},
            	{
            		fieldLabel: '分类主键',
            		allowBlank: false,
            		bind: {
            			value: '{rec.key}'
            		}
            	},
            	{
            		fieldLabel: '分类图标',
            		allowBlank: false,
            		bind: {
            			value: '{rec.icon}'
            		}
            	},
            	{
            		fieldLabel: '分类描述',
					xtype: 'textarea',
            		bind: {
            			value: '{rec.description}'
            		}
            	}
        ]
        }
    ]
});