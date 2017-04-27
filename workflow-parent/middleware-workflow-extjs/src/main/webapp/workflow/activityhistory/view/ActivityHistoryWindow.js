/**审批窗口
 * @author chenyanxu <br/>
 *         date:2016-3-3
 * @version 1.0.0
 */
Ext.define('kalix.workflow.activityhistory.view.ActivityHistoryWindow', {
    extend: 'Ext.Window',
    requires: [
        'kalix.workflow.activityhistory.view.ActivityHistoryGrid',
        'kalix.workflow.activityhistory.viewModel.ActivityHistoryViewModel',
        'kalix.attachment.view.AttachmentGrid'
    ],
    xtype: 'activityHistoryWindow',
    viewModel: 'activityHistoryViewModel',
    iconCls: 'iconfont icon-history',
    width: 900,
    buttonAlign: 'center',
    border: false,
    modal: true,
    bind: {
        title: '{title}'
    },
    items: [
        {
            xtype: 'tabpanel',
            height: 280,
            bodyPadding: 0,
            items: [
                {
                    title: '流程历史',
                    xtype: 'activityHistoryGrid'
                },
                {
                    title: '附件',
                    xtype: 'attachmentGrid',
                    tbar: null
                }
            ]
        }
    ],
    buttons: [
        {
            text: '关闭',
            handler: function () {
                this.up('window').close();
            }
        }
    ],
    listeners: {
        beforeshow: function () {
            var store = this.items.getAt(1).items.getAt(1).store;
            var mainId = this.getViewModel().get('rec').id;

            store.proxy.extraParams = {jsonStr: '{mainId:' + mainId + '}'}
            store.load();
        }
    }
});