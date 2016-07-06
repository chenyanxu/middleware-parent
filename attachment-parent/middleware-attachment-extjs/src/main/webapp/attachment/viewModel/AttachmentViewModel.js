/**
 * @author chenyanxu
 */
Ext.define('kalix.attachment.viewModel.AttachmentViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.attachmentViewModel',
    data: {
        rec: null,
        validation: {},  //验证错误信息
        iconCls: 'iconfont icon-attachment-column',
        title: '附件管理',
        view_operation: true,
        view_title: '',
        add_title: '',
        edit_title: ''
    }
});