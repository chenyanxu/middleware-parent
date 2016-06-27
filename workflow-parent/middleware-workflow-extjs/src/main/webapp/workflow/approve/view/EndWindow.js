/**审批窗口
 * @author chenyanxu <br/>
 *         date:2016-3-3
 * @version 1.0.0
 */
Ext.define('kalix.workflow.approve.view.EndWindow', {
    extend: 'Ext.Window',
    requires: [
        'kalix.workflow.approve.controller.ApproveWindowController',
        'kalix.workflow.approve.viewModel.ApproveViewModel',
        'kalix.workflow.activityhistory.view.ActivityHistoryGrid'
    ],
    xtype: 'endWindow',
    controller: 'approveWindowController',
    viewModel: 'approveViewModel',
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
            title: '执行结果',
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
            text: '确定',
            handler: 'onEnd'
        },
        {
            text: '关闭',
            glyph: 'xf00d@FontAwesome',
            handler: function () {
                this.up('window').close();
            }
        }
    ]
});