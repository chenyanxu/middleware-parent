package com.kalix.middleware.attachment.api.dao;


import com.kalix.framework.core.api.dao.IGenericDao;
import com.kalix.middleware.attachment.entities.AttachmentBean;

import java.util.List;

/**
 * @author chenyanxu
 */
public interface IAttachmentBeanDao extends IGenericDao<AttachmentBean, Long> {
    List findByMainId(Long mainId);
}
