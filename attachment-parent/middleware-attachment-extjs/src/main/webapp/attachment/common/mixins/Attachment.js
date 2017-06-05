/**
 * @author chenyanxu
 * mixin to other class
 * the function use to add user ids  to a relation table
 */
Ext.define('kalix.attachment.common.mixins.Attachment', {
    onAttachmentManage: function (grid, rowIndex, colIndex) {
        var view = Ext.create('kalix.attachment.view.AttachmentWindow');
        var selModel = grid.getStore().getData().items[rowIndex];
        var vm = view.lookupViewModel();

        vm.set('rec', selModel);
        vm.set('iconCls', 'iconfont icon-attachment-column');
        vm.set('title', '附件管理');
        vm.set('view_operation', true);
        view.show();
        grid.setSelection(selModel);
    }
});