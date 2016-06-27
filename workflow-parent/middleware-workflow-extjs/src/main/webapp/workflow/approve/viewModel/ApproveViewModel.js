/**

 * @author chenyanxu <br/>
 *         date:2016-3-3
 * @version 1.0.0
 */
Ext.define('kalix.workflow.approve.viewModel.ApproveViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.approveViewModel',
    data: {
        view_operation: false,
        approveOpinion: '',
        businessKey: '',
        taskId: '',
        title: '',
        rec: null
    }
});