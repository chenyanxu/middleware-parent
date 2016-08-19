/**
 * 流程分类模型
 *
 * @author
 * @version 1.0.0
 */


Ext.define('kalix.workflow.category.model.CategoryModel', {
    extend: 'kalix.model.BaseModel',

    //todo 在此修改模型定义
    fields: [
    	{
    	name: 'name',
    	type: 'string'
    	},	{
    	name: 'key',
    	type: 'string'
    	},	{
    	name: 'icon',
    	type: 'string'
    	},	{
    	name: 'description',
    	type: 'string'
    	}
    ],
    //todo 在此修改模型验证提示信息
    	validators: {
    		name:[{
    			type: 'presence',
    			message: '分类名称不能为空'
    		}],
    		key:[{
    			type: 'presence',
    			message: '分类主键不能为空'
    		}],
    		icon:[{
    			type: 'presence',
    			message: '分类图标不能为空'
    		}],
    		description:[{
    			type: 'presence',
    			message: '分类描述不能为空'
    		}]
    	}
});
