/**model of attachment
 *  @author chenyanxu
 */
Ext.define('kalix.attachment.model.AttachmentModel', {
    extend: 'kalix.model.BaseModel',
    fields: [
        {name: 'mainId'},
        {name: 'attachmentId'},
        {name: 'attachmentRev'},
        {name: 'attachmentName'},
        {name: 'attachmentType'},
        {name: 'attachmentSize'},
        {name: 'attachmentPath'},
        {name: 'uploadDate', type: 'date', dateFormat: 'Y-m-d H:i:s'}
    ]
});