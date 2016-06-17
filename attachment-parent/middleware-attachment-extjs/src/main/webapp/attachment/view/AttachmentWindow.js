/** for attachment upload
 * @aurthor author
 */
Ext.define('kalix.attachment.view.AttachmentWindow', {
    extend: 'kalix.view.components.common.BaseWindow',
    requires: [
        'kalix.controller.BaseWindowController',
        'kalix.attachment.viewModel.AttachmentViewModel',
        'kalix.attachment.view.AttachmentGrid',
        'kalix.attachment.view.AttachmentForm'
    ],
    alias: 'widget.attachmentWindow',
    xtype: "attachmentWindow",
    viewModel: 'attachmentViewModel',
    controller: {
        type: 'baseWindowController',
        storeId: 'attachmentStore'
    },
    layout: 'container',
    defaults: {},
    height: 550,
    items: [
        {
            xtype: 'attachmentGrid'
        }
    ],
    listeners: {
        beforeshow: function () {
            var store = this.items.getAt(0).store;
            var mainId = this.getViewModel().get('rec').id;

            store.on('load', function (target, records, successful, eOpts) {
                //records.forEach(function(item) {
                    //item.set('attachmentSize', item.get('attachmentSize') / 1048576);
                //});
            });

            store.proxy.extraParams = {jsonStr: '{mainId:' + mainId + '}'}
            store.load();
        }
    }
});