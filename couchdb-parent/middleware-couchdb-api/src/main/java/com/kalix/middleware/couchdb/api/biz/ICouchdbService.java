package com.kalix.middleware.couchdb.api.biz;

import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.api.system.IAttachmentService;
import org.lightcouch.Response;

import java.util.Map;

/**
 ** @author chenyanxu
 */
public interface ICouchdbService extends IAttachmentService {
    /**
     * 添加附件到couchdb数据库
     *
     * @param value
     * @param key
     * @param type
     * @return
     */
    Response addAttachment(String value, String key, String type);

    /**
     * 移除附件
     */
    JsonStatus deleteAttach(String id, String rev);

    /**
     * 修改附件内容
     *
     * @param couchdbAttachBean
     * @param value
     * @param type
     * @return
     */
    //String updateAttach(CouchdbAttachBean couchdbAttachBean, String value, String type);

    /**
     * 获取文档对象
     *
     * @param couchdbAttachBean
     * @return
     */
    //Document getDocumentByIdAndRev(CouchdbAttachBean couchdbAttachBean);

    /**
     * 获取附件对象
     *
     * @param couchdbAttachBean
     * @return
     */
    //Attachment getAttachmentByIdAndRev(CouchdbAttachBean couchdbAttachBean);

    /**
     *
     * @return
     */
    String getDBUrl();

    String addAttachment(Map<String,String> params);
}
