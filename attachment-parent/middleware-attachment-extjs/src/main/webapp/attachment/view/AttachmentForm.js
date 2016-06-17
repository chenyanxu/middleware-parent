/** for attachment upload
 * @aurthor author
 */
Ext.define('kalix.attachment.view.AttachmentForm', {
    extend: 'Ext.form.Panel',
    requires: ['kalix.attachment.view.AttachmentFileField'],
    alias: 'widget.attachmentForm',
    xtype: 'attachmentForm',
    frame: true,
    timeout: 60,
    height:20,
    items: [
        {
            xtype: 'attachmentFileField',
            width:50,
            buttonOnly:true,
            buttonText: '上传',
            name: 'attachment',
            listeners:{
                change:function(target,value, eOpts){
                    target.findParentByType('grid').getController().onChange(target, value, eOpts);
                }
            }
        }
    ],
    listeners:{
        afterrender: function (target) {
           target.ariaEl.dom.style.border='none';
        },
        beforerender: function (target) {
            this.items.getAt(0).triggers.filebutton.component.icon = this.filebutonIcon;
        }
    }
});