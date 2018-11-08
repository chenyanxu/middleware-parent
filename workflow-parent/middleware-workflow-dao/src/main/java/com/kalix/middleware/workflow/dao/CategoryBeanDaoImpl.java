package com.kalix.middleware.workflow.dao;

import com.kalix.framework.core.impl.dao.GenericDao;
import com.kalix.middleware.workflow.api.dao.ICategoryBeanDao;
import com.kalix.middleware.workflow.entities.CategoryBean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * 类描述:    工作流分类Dao实现类
 * 创建人:    sunlf
 * 创建时间:  2016/5/24 15:57
 * 修改人:
 * 修改时间:
 * 修改备注:  [说明本次修改内容]
 */
public class CategoryBeanDaoImpl extends GenericDao<CategoryBean, String> implements ICategoryBeanDao {
    @Override
    @PersistenceContext(unitName = "workflow-cm")
    public void setEntityManager(EntityManager em) {
        super.setEntityManager(em);
    }
}
