/**
 * @author chenyanxu
 */
Ext.define('kalix.attachment.controller.AttachmentGridController', {
    extend: 'kalix.controller.BaseGridController',
    requires: ['kalix.core.Notify'],
    alias: 'controller.attachmentGridController',
    onChange: function (target, event, domValue) {
        var form = target.findParentByType('form');
        var store = target.findParentByType('window').items.getAt(0).store;
        var mainId = target.findParentByType('window').viewModel.get('rec').id

        scope={mainId:mainId,store:store};

        form.submit({
            url: CONFIG.restRoot + '/camel/rest/upload',
            waitMsg: '正在上传...',
            scope: scope,
            success: function (fp, o) {
                if (o.result.success) {
                    var model=Ext.create('kalix.attachment.model.AttachmentModel');

                    delete model.data['uploadDate'];
                    model.set('mainId',mainId);
                    model.set('attachmentId', o.result.attachmentId);
                    model.set('attachmentRev',o.result.attachmentRev);
                    model.set('attachmentName',o.result.attachmentName);
                    model.set('attachmentType',o.result.attachmentType);
                    model.set('attachmentSize',o.result.attachmentSize);
                    model.set('attachmentPath',o.result.attachmentPath);
                    store.add(model);

                    store.sync(
                        {
                            callback: function (batch) {
                                store.currentPage = 1;
                                store.load();

                                var res = Ext.JSON.decode(batch.operations[0].getResponse().responseText);

                                if (batch.operations[0].success) {
                                    kalix.core.Notify.success(res.msg, CONFIG.ALTER_TITLE_SUCCESS);
                                }
                                else {
                                    Ext.Msg.alert(CONFIG.ALTER_TITLE_FAILURE, res.msg);
                                }
                            }
                        }
                    );
                }
            },
            failure: function(fp, o) {
                Ext.Msg.alert(CONFIG.ALTER_TITLE_FAILURE, o.result.msg);
            }
            });
        //target.ariaEl.dom.value = '';
    },
    onDownload:function(grid, rowIndex, colIndex){
        var selModel = grid.getStore().getData().items[rowIndex];
        var a = document.createElement("a");

        a.setAttribute("href", selModel.get('attachmentPath'));
        a.setAttribute("target", "_blank");
        a.setAttribute("id", "camnpr");
        document.body.appendChild(a);
        a.click();
    },
    onDelete: function (grid, rowIndex, colIndex) {
        var model = grid.getStore().getData().items[rowIndex];
        var store = Ext.app.Application.instance.getApplication().getStore(this.storeId);

        Ext.Msg.confirm("警告", "确定要删除吗？", function (button) {
            if (button == "yes") {
                Ext.Ajax.request({
                    url: CONFIG.restRoot + '/camel/rest/couchdb?id=' + model.get('attachmentId') + '&rev=' + model.get('attachmentRev'),
                    method:'DELETE',
                    async:false,
                    //extraParams :{id:model.get('attachmentId'),rev:model.get('attachmentRev')},
                    success: function(response, opts) {
                        var obj = Ext.decode(response.responseText);
                        //console.dir(obj);
                    },
                    failure: function(response, opts) {
                        //console.log('server-side failure with status code ' + response.status);
                    }
                });


                //store.proxy.extraParams = {};
                store.remove(model);
                store.sync(
                    {
                        callback: function (batch) {
                            store.currentPage = 1;
                            store.load();

                            var res = Ext.JSON.decode(batch.operations[0].getResponse().responseText);

                            if (batch.operations[0].success) {
                                kalix.core.Notify.success(res.msg, CONFIG.ALTER_TITLE_SUCCESS);
                            }
                            else {
                                Ext.Msg.alert(CONFIG.ALTER_TITLE_FAILURE, res.msg);
                            }
                        }
                    }
                );
            }
        });
    }
});