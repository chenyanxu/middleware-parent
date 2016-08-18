/**
 * 流程分类模型
 *
 * @author
 * @version 1.0.0
 */

Ext.define('kalix.workflow.category.viewModel.CategoryViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.categoryViewModel',
    data: {
        rec: null,
        validation: {},  //验证错误信息
        icon: '',
        title: '',
        view_operation: false,
        view_title: '查看流程分类',
        add_title: '添加流程分类',
        edit_title: '修改流程分类',
        add_image_path: '/kalix/app/pms/category/resources/images/category_add.png',
        view_image_path: '/kalix/app/pms/category/resources/images/category_view.png',
        edit_image_path: '/kalix/app/pms/category/resources/images/category_edit.png',
    }
});