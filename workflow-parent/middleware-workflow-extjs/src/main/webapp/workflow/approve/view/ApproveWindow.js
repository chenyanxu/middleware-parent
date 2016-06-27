/**审批窗口
 * @author chenyanxu <br/>
 *         date:2016-3-3
 * @version 1.0.0
 */
Ext.define('kalix.workflow.approve.view.ApproveWindow', {
    extend: 'Ext.Window',
    requires: [
        'kalix.workflow.approve.controller.ApproveWindowController',
        'kalix.workflow.approve.viewModel.ApproveViewModel',
        'kalix.workflow.activityhistory.view.ActivityHistoryGrid'
    ],
    xtype: 'approveWindow',
    controller: 'approveWindowController',
    viewModel: 'approveViewModel',
    iconCls: 'iconfont icon-workflow-approval',
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
        },
        {
            xtype: 'fieldset',
            padding: '0 10 0 10',
            margin: 0,
            title: '审批意见',
            defaults: {anchor: '100%'},
            layout: 'anchor',
            items: [
                {
                    xtype: 'textarea',
                    bind: {
                        value: '{approveOpinion}'
                    }
                }
            ],
            bind: {
                hidden: '{view_operation}'
            }
        }
    ],
    buttons: [
        {
            text: '同意',
            handler: 'onApprove',
            bind: {
                hidden: '{view_operation}'
            }
        },
        {
            text: '不同意',
            handler: 'onApprove',
            bind: {
                hidden: '{view_operation}'
            }
        },
        {
            text: '关闭',
            glyph: 'xf00d@FontAwesome',
            handler: function () {
                this.up('window').close();
            },
            bind: {
                hidden: '{!view_operation}'
            }
        }
    ]
});