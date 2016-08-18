/**
 * 流程分类首页
 *
 * @author
 * @version 1.0.0
 */
Ext.define('kalix.workflow.category.Main', {
    extend: 'kalix.container.BaseContainer',
    requires: [
        'kalix.workflow.category.view.CategoryGrid',
        'kalix.workflow.category.view.CategorySearchForm',
        'kalix.workflow.category.viewModel.CategoryViewModel'
    ],
    storeId: 'categoryStore',
    viewModel: 'categoryViewModel',
    items: [
        {
            title: '流程分类查询',
            xtype: 'categorySearchForm'
        }, {
            xtype: 'categoryGridPanel',
            id: 'categoryGridPanel',
            title: '流程分类列表',
            margin: 10
        }
    ]
});
