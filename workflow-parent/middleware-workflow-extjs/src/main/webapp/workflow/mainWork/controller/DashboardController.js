/**
 * @author zangyanming
 */
Ext.define('kalix.workflow.mainWork.controller.DashboardController', {
    extend: 'Ext.app.Controller',
    requires: ['kalix.workflow.components.ActivitiProcessImageWindow'],
    alias: 'controller.dashboardController',
    /**
     * 查看流程定义
     * @param grid
     * @param rowIndex
     * @param colIndex
     */
    onOpenProcessDefinition: function (name,key) {
        var imgUrl = "processDefinitionId=" + key;
        var win = Ext.create('kalix.workflow.components.ActivitiProcessImageWindow', {
            title: '流程定义查看 - ' + name,
            imgUrl: imgUrl
        });
        win.show();
    },
});