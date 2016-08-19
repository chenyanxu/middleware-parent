/**
 * 流程分类查询表单
 * @author
 * @version 1.0.0
 */
Ext.define('kalix.workflow.category.view.CategorySearchForm', {
    extend: 'kalix.view.components.common.BaseSearchForm',
    alias: 'widget.categorySearchForm',
    xtype: 'categorySearchForm',
    storeId: 'categoryStore',
    items: [
    	{
    		xtype: 'textfield',
    		fieldLabel: '分类名称',
    		labelAlign: 'right',
    		labelWidth: 60,
    		width: 200,
    		name: 'name'
    	},
    	{
    		xtype: 'textfield',
    		fieldLabel: '分类主键',
    		labelAlign: 'right',
    		labelWidth: 60,
    		width: 200,
    		name: 'key'
    	}
    ]

});
