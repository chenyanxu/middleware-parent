/**
 * 待办任务模型
 *
 * @author
 * @version 1.0.0
 */
Ext.define('kalix.workflow.task.viewModel.TaskViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.taskViewModel',
    data: {
        rec:null,
        validation: {},  //验证错误信息
        icon: '',
        title: '',
        processShowUrl: '/image',
        view_operation:false,
        view_title:'查看项目机会',
        add_title:'添加项目机会',
        edit_title:'修改项目机会',

        delegateId:'',//委托人
        taskIds:'',//task Ids
        delegateUrl:'camel/rest/workflow/tasks/delegate',

        add_image_path: CONFIG.restRoot+'/workflow/resources/images_add.png',
        view_image_path: CONFIG.restRoot+'/workflow/resources/images_view.png',
        delete_image_path: CONFIG.restRoot+'/workflow/resources/images_delete.png',
        edit_image_path: CONFIG.restRoot+'/workflow/resources/images_edit.png',
        delegate_image_path: CONFIG.restRoot+'/workflow/resources/images/delegate.png'
    }
});