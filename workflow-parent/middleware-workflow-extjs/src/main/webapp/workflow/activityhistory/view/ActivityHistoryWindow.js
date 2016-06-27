/**审批窗口
 * @author chenyanxu <br/>
 *         date:2016-3-3
 * @version 1.0.0
 */
Ext.define('kalix.workflow.activityhistory.view.ActivityHistoryWindow', {
    extend: 'Ext.Window',
    requires: [
        'kalix.workflow.activityhistory.view.ActivityHistoryGrid',
        'kalix.workflow.activityhistory.viewModel.ActivityHistoryViewModel'
    ],
    xtype: 'activityHistoryWindow',
    viewModel: 'activityHistoryViewModel',
    iconCls: 'iconfont icon-history',
    width: 900,
    buttonAlign: "center",
    border: false,
    modal: true,
    bind: {
        title: '{title}'
    },
    items: [
        {
            xtype: 'fieldset',
            padding: '0',
            margin: 0,
            title: '流程历史',
            items: [
                {
                    xtype: 'activityHistoryGrid',
                    height: 180
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
    ]
});