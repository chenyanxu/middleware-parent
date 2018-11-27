package com.kalix.middleware.attachment.biz;

import com.kalix.framework.core.impl.biz.ShiroGenericBizServiceImpl;
import com.kalix.middleware.attachment.api.biz.IAttachmentBeanService;
import com.kalix.middleware.attachment.api.dao.IAttachmentBeanDao;
import com.kalix.middleware.attachment.entities.AttachmentBean;

import java.util.List;

/**
 * Created by lenovo on 2015/11/23.
 */
public class AttachmentBeanServiceImpl extends ShiroGenericBizServiceImpl<IAttachmentBeanDao, AttachmentBean> implements IAttachmentBeanService {
    @Override
    public List findByMainId(Long mainId) {
        return  this.dao.findByMainId(mainId);
    }
}
