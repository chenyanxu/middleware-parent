package com.kalix.middleware.attachment.biz;

import com.kalix.framework.core.api.persistence.JsonData;
import com.kalix.framework.core.impl.biz.ShiroGenericBizServiceImpl;
import com.kalix.framework.core.util.StringUtils;
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
        return this.dao.findByMainId(mainId);
    }

    @Override
    public JsonData getAllEntityByQuery(Integer page, Integer limit, String jsonStr, String sort) {
        if (StringUtils.isEmpty(sort)) {
            sort = "[{'property': 'downloadStatus', 'direction': 'ASC'},{'property': 'uploadDate', 'direction': 'DESC'}]";
        }
        JsonData jsonData = new JsonData();
        jsonData = super.getAllEntityByQuery(page, limit, jsonStr, sort);
        for (int i = 0; i < jsonData.getData().size(); i++) {
            AttachmentBean attachmentBean = (AttachmentBean) jsonData.getData().get(i);
            String downloadStatus = attachmentBean.getDownloadStatus();
            String downloadStatusName = "未下载";
            if (downloadStatus != null && downloadStatus.equals("1")) {
                downloadStatusName = "已下载";
            }
            attachmentBean.setDownloadStatusName(downloadStatusName);
        }
        return jsonData;
    }
}
