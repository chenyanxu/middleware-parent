package com.kalix.middleware.workflow.biz;

import com.kalix.framework.core.api.persistence.JsonData;
import com.kalix.framework.core.api.persistence.PersistentEntity;
import com.kalix.framework.core.impl.biz.GenericBizServiceImpl;
import com.kalix.middleware.workflow.api.biz.ICategoryBeanService;
import com.kalix.middleware.workflow.api.dao.ICategoryBeanDao;
import com.kalix.middleware.workflow.entities.CategoryBean;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-05-24.
 */
public class CategoryBeanServiceImpl extends GenericBizServiceImpl<ICategoryBeanDao, CategoryBean> implements ICategoryBeanService {
    private ProcessEngine processEngine;
    public CategoryBeanServiceImpl() {
        super.init(CategoryBean.class.getName());
    }

    /**
     * 通过分类获得流程定义
     * @param type
     * @return
     */
    @Override
    public JsonData getAllWorkFlow(String type) {
        final String namespace="http://www.activiti.org/"+type;
        JsonData jsonData = new JsonData();
        List<PersistentEntity> resultList=new ArrayList<>();

        RepositoryService repositoryService = processEngine.getRepositoryService();
        //对应数据表 ACT_RE_PROCDEF
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().processDefinitionCategory(namespace).latestVersion().list();

        if (list != null && list.size() > 0) {
            long i=1; //自己构造id
            for (ProcessDefinition processDefinition : list) {
                ProcessDef processDef=new ProcessDef();
                processDef.setId(i+"");
                processDef.setProcessId(processDefinition.getId());//用于查看工作流定义图片
                processDef.setKey(processDefinition.getKey());
                processDef.setName(processDefinition.getName());
                processDef.setDescription(processDefinition.getDescription());
                resultList.add(processDef);
                i++;
            }
        }
        else
        {
            ProcessDef processDef=new ProcessDef();
            processDef.setId("0");//没有工作流的id为0
            processDef.setName("暂无数据");
            processDef.setKey("empty");
            processDef.setDescription("还没有定义工作流。");
            resultList.add(processDef);
        }

        jsonData.setTotalCount((long) list.size());
        jsonData.setData(resultList);
        return jsonData;
    }

    public void setProcessEngine(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    /**
     * 用于返回流程定义的临时类
     */
    private class ProcessDef extends PersistentEntity{
        private String name;//分类名称
        private String key; //分类关键字
        private String description;
        private String processId; //流程定义id

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getProcessId() {
            return processId;
        }

        public void setProcessId(String processId) {
            this.processId = processId;
        }
    }
}
