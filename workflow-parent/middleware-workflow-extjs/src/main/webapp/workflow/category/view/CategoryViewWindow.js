/**
 * 流程分类查看表单
 *
 * @author
 * @version 1.0.0
 */

Ext.define('kalix.workflow.category.view.CategoryViewWindow', {
    extend: 'kalix.view.components.common.BaseWindow',
    requires: [
        'kalix.admin.user.store.UserStore'
    ],
    alias: 'widget.categoryViewWindow',
    xtype: 'categoryViewWindow',
    width: 400,
    items: [{
        defaults: {readOnly: true},
        xtype: 'baseForm',
        items: [
            {
                fieldLabel: '分类名称',
                allowBlank: false,
                bind: {
                    value: '{rec.name}'
                }
            },
            {
                fieldLabel: '分类主键',
                allowBlank: false,
                bind: {
                    value: '{rec.key}'
                }
            },
            {
                fieldLabel: '分类图标',
                allowBlank: false,
                bind: {
                    value: '{rec.icon}'
                }
            },
            {
                fieldLabel: '分类描述',
                xtype: 'textarea',
                bind: {
                    value: '{rec.description}'
                }
            }
        ]
    }

    ]


});