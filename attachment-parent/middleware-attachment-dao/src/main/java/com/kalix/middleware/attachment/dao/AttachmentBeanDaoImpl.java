package com.kalix.middleware.attachment.dao;

import com.kalix.framework.core.impl.dao.GenericDao;
import com.kalix.middleware.attachment.api.dao.IAttachmentBeanDao;
import com.kalix.middleware.attachment.entities.AttachmentBean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by dell on 14-1-16.
 */
public class AttachmentBeanDaoImpl extends GenericDao<AttachmentBean, Long> implements IAttachmentBeanDao {
    @Override
    @PersistenceContext(unitName = "middleware-attachment-unit")
    public void setEntityManager(EntityManager em) {
        super.setEntityManager(em);
    }

    @Override
    public List findByMainId(Long mainId)  {
        String sql = "select *  from " + super.getTableName() + " where mainId =?1 " ;
        return this.findByNativeSql(sql, AttachmentBean.class, mainId);
    }
}
