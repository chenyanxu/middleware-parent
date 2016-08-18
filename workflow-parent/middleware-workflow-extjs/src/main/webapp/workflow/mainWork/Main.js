/**
 * @author chenyanxu
 * 工作流快捷菜单
 */
Ext.define('kalix.workflow.mainWork.Main', {
    extend: 'Ext.container.Container',
    requires: [
        'kalix.workflow.mainWork.view.dashboard.DashBoardExtend',
        'kalix.workflow.processdefinition.viewModel.ProcessDefinitionViewModel'
    ],
    viewModel:'processDefinitionViewModel',
    items: [
        {
            xtype: 'dashboardextend'
        }
    ]
});
