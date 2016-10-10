/**
 * 待办流程模型
 *
 * @author
 * @version 1.0.0
 */
Ext.define('kalix.workflow.task.viewModel.TaskViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.taskViewModel',
    data: {
        delegateId:'',//委托人
        taskIds:'',//task Ids
        delegateUrl:'camel/rest/workflow/tasks/delegate'
    }
});