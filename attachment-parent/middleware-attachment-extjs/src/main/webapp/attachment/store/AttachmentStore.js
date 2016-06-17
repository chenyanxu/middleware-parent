/**
 * @author chenyanxu
 */
Ext.define('kalix.attachment.store.AttachmentStore', {
    extend: 'kalix.store.BaseStore',
    alias: 'store.attachmentStore',
    xtype: 'attachmentStore',
    model: 'kalix.attachment.model.AttachmentModel',
    autoLoad: false,
    storeId: "attachmentStore",
    proxyUrl: CONFIG.restRoot + '/camel/rest/attachments'
});