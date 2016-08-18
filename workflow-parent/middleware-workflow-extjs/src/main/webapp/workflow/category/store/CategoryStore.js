/**
 * 流程分类数据仓库
 *
 * @author
 * @version 1.0.0
 */
Ext.define('kalix.workflow.category.store.CategoryStore', {
    extend: 'kalix.store.BaseStore',
    model: 'kalix.workflow.category.model.CategoryModel',
    alias: 'store.categoryStore',
    xtype: 'categoryStore',
    storeId: "categoryStore",
    proxyUrl: '/kalix/camel/rest/categorys'
});