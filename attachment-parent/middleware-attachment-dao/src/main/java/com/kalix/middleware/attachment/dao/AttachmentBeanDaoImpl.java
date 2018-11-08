package com.kalix.middleware.attachment.dao;

import com.kalix.framework.core.impl.dao.GenericDao;
import com.kalix.middleware.attachment.api.dao.IAttachmentBeanDao;
import com.kalix.middleware.attachment.entities.AttachmentBean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by dell on 14-1-16.
 */
public class AttachmentBeanDaoImpl extends GenericDao<AttachmentBean, String> implements IAttachmentBeanDao {
    @Override
    @PersistenceContext(unitName = "middleware-attachment-unit")
    public void setEntityManager(EntityManager em) {
        super.setEntityManager(em);
    }
}
