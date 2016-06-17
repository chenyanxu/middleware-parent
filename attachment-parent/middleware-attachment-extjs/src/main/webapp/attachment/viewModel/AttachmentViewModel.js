/**
 * @author chenyanxu
 */
Ext.define('kalix.attachment.viewModel.AttachmentViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.attachmentViewModel',
    data: {
        rec: null,
        validation: {},  //验证错误信息
        //icon: 'attachment/resources/images/attachment_manage.png',
        iconCls: 'iconfont icon-attachment-column',
        title: '附件管理',
        view_operation: true,
        view_title: '',
        add_title: '',
        edit_title: '',
        add_image_path: CONFIG.restRoot + '/attachment/resources/images/attachment_add.png',
        view_image_path: '',
        edit_image_path: ''
    }
});