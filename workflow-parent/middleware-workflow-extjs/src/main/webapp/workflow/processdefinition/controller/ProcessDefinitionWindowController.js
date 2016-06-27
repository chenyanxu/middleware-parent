/**
 * 流程定义表格控制器
 *
 * @author majian <br/>
 *         date:2015-6-18
 * @version 1.0.0
 */
Ext.define('kalix.workflow.processdefinition.controller.ProcessDefinitionWindowController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.processDefinitionWindowController',
    onSave:function(){
        var view=this.getView();

        window.frames[0].bpmnModeler.saveXML({format: true}, function (err, xml) {
            console.log(xml);


            Ext.Ajax.request({
                url: CONFIG.restRoot + '/camel/rest/workflow/deploy',
                scope: this,
                defaultPostHeader : 'application/json;charset=utf-8',
                method: 'POST',
                params :Ext.encode({xml:xml}),
                callback: function (options, success, response) {
                    var rtn = Ext.JSON.decode(response.responseText);

                    if (rtn) {

                    }
                }
            });
            view.close();
        });
    }
});