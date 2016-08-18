/**
 *
 */
Ext.define('kalix.workflow.mainWork.view.dashboard.DashBoardExtend', {
    extend: 'Ext.dashboard.Dashboard',
    xtype: 'dashboardextend',
    requires: [
        'kalix.workflow.mainWork.view.dashboard.WorkflowCategory'
    ],
    maxColumns: 4,
    columnWidths: [0.25, 0.25, 0.25, 0.24],
    defaultContent: [],
    parts: {
        'workflowCategory': 'workflowCategory'
    },
    constructor: function () {
        var scope = this;
        var defaultContent = {};
        Ext.Ajax.request({
            async: false,
            scope: scope,
            url: CONFIG.restRoot + "/camel/rest/categorys/getAll",
            method: 'GET',
            success: function (response, opts) {
                var obj = Ext.decode(response.responseText);
                for (var i = 0; i < obj.length; i++) {
                    var name = obj[i].name;
                    var key = obj[i].key;
                    var icon=obj[i].icon;
                    var newOjb = {
                        type: 'workflowCategory',
                        store: Ext.create('Ext.data.Store', {
                            storeId: 'workflowCategory_' + key,
                            autoLoad: true,
                            fields: ['id', 'name', 'key', 'description','processId'],
                            proxy: {
                                type: 'ajax',
                                url: CONFIG.restRoot + '/camel/rest/categorys/getType?category=' + key,
                                reader: {
                                    rootProperty: 'data'
                                }
                            },
                            listeners: {
                                load: function (target, records, successful, eOpts) {
                                    for (var index = 0; index < records.length; ++index) {
                                        records[index].set('imgUrl', CONFIG.restRoot + '/oa/resources/images/' + records[index].get('key') + '.png');
                                    }
                                }
                            }
                        }),
                        columnIndex: i % 4,
                        title: name,
                        iconCls: icon,
                        responseIndex: i
                    };

                    scope.defaultContent.push(newOjb);
                }
            },
            failure: function (response, opts) {
                console.log('server-side failure with status code ' + response.status);
            }
        }, scope);

        this.callParent(arguments);
    }
});