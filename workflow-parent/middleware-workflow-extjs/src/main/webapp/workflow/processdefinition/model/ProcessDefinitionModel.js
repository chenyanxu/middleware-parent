/**
 * 流程定义模型
 *
 * @author majian <br/>
 * date:2015-7-3
 * @version 1.0.0
 */
Ext.define('kalix.workflow.processdefinition.model.ProcessDefinitionModel', {
    extend: 'kalix.model.BaseModel',
    fields: [
        {name: 'name', type: 'string'},//流程定义名称
        {name: 'key', type: 'string'},//关键字
        {name: 'description', type: 'string'},//描述
        {name: 'version', type: 'int'},//版本
        {name: 'suspensionState', type: 'int'},//状态
    ]
});