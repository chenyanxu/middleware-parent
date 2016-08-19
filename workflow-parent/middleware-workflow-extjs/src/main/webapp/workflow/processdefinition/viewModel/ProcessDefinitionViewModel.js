/**
 * 待办任务模型
 *
 * @author
 * @version 1.0.0
 */
Ext.define('kalix.workflow.processdefinition.viewModel.ProcessDefinitionViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.processDefinitionViewModel',
    data: {
        rec:null,
        validation: {},  //验证错误信息
        icon: '',
        title: '',
        processShowUrl: '/image',
        view_operation:false,
        view_title:'查看流程定义',
        add_title:'添加流程定义',
        edit_title:'修改流程定义',
        add_image_path: CONFIG.restRoot+'/workflow/resources/images/processdef_add.png',
        view_image_path: CONFIG.restRoot+'/workflow/resources/images/processdef_view.png',
        delete_image_path: CONFIG.restRoot+'/workflow/resources/images/processdef_delete.png',
        edit_image_path: CONFIG.restRoot+'/workflow/resources/images/processdef_edit.png',
    }
});