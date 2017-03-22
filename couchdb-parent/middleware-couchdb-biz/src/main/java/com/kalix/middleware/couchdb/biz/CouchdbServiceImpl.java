package com.kalix.middleware.couchdb.biz;

import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.util.ConfigUtil;
import com.kalix.framework.core.util.SystemUtil;
import com.kalix.middleware.couchdb.api.biz.ICouchdbService;
import org.lightcouch.Attachment;
import org.lightcouch.CouchDbClient;
import org.lightcouch.Document;
import org.lightcouch.Response;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Dictionary;
import java.util.Map;

/**
 * @author chenyanxu
 */
public class CouchdbServiceImpl implements ICouchdbService, ManagedService {
    public static final String CONFIG_COUCH_DB = "ConfigCouchdb";
    private CouchDbClient dbClient;

    private String db_name, protocol, ip, user, password;
    private String url; //couchdb访问公网地址
    private int port;

    public CouchdbServiceImpl() {
        initDbclient();
    }

    @Override
    public Response addAttachment(String value, String key, String type) {
        Attachment attachment = new Attachment();
        Document document = new Document();
        Response response = null;
        try {
            if (dbClient == null) {
                initDbclient();
            }

            attachment.setContentType(type);
            attachment.setData(value);
            document.addAttachment(key, attachment);
            response = dbClient.save(document);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    public Response addAttachment(InputStream stream, String key, String type){
        if (dbClient == null) {
            initDbclient();
        }

        return dbClient.saveAttachment(stream,key,type);
    }

    /**
     * 移除附件
     *
     * @return
     */
    @Override
    public JsonStatus deleteAttach(String id, String rev) {
        Response response = null;

        try {
            if (dbClient == null) {
                initDbclient();
            }
            response = dbClient.remove(id, rev);

            return JsonStatus.successResult("");
        } catch (Exception e) {
            e.printStackTrace();

            return JsonStatus.failureResult("");

        }
    }

    @Override
    public String getDBUrl() {
        return url;
    }

    @Override
    public String addAttachment(Map<String, String> params) {
        return null;
    }

    @Override
    public String addNewAttachment(String value, String key, String type) {
        Response response = addAttachment(value, key, type);
        return response.getId();
    }

    @Override
    public String getAttachmentUrl() {
        return getDBUrl();
    }

    @Override
    public void updated(Dictionary<String, ?> dictionary) throws ConfigurationException {
        initDbclient();
    }

    private void readConfig() {
        db_name = (String) ConfigUtil.getConfigProp("DB_NAME", CONFIG_COUCH_DB);
        protocol = (String) ConfigUtil.getConfigProp("PROTOCOL", CONFIG_COUCH_DB);
        ip = (String) ConfigUtil.getConfigProp("IP", CONFIG_COUCH_DB);
        port = Integer.parseInt((String) ConfigUtil.getConfigProp("PORT", CONFIG_COUCH_DB));
        user = (String) ConfigUtil.getConfigProp("USER", CONFIG_COUCH_DB);
        password = (String) ConfigUtil.getConfigProp("PASSWORD", CONFIG_COUCH_DB);
        url = (String) ConfigUtil.getConfigProp("COUCHDB_URL", CONFIG_COUCH_DB);
    }

    private void initDbclient() {
        readConfig();
        try {
            dbClient = new CouchDbClient(db_name, true, protocol, ip, port, user, password);
            SystemUtil.succeedPrintln("succeed connect to couchdb! IP address is " + ip);
        } catch (Exception e) {
            SystemUtil.errorPrintln("can not connect to couchdb address " + ip + "!");
            e.printStackTrace();
        }
    }


//    @Override
//    public String updateAttach(CouchdbAttachBean couchdbAttachBean, String value, String type) {
//        Response response = new Response();
//        try {
//
//            Document document = getDocumentByIdAndRev(couchdbAttachBean);
//            Attachment attachment = getAttachmentFromDocument(couchdbAttachBean, document);
//            attachment.setData(value);
//            attachment.setContentType(type);
//            document.addAttachment(couchdbAttachBean.getOtherName(), attachment);
//            dbClient.update(document);   } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return response.getRev();
//    }
//
//    @Override
//    public Document getDocumentByIdAndRev(CouchdbAttachBean couchdbAttachBean) {
//        Document document = new Document();
//        try {
//            document = dbClient.find(Document.class, couchdbAttachBean.getCouchdbAttachId());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return document;
//    }
//
//    @Override
//    public Attachment getAttachmentByIdAndRev(CouchdbAttachBean couchdbAttachBean) {
//        return getAttachmentFromDocument(couchdbAttachBean, getDocumentByIdAndRev(couchdbAttachBean));
//    }
//
//    /**
//     * 根据Document获取Attachment
//     *
//     * @param couchdbAttachBean
//     * @param document
//     * @return
//     */
//    private Attachment getAttachmentFromDocument(CouchdbAttachBean couchdbAttachBean, Document document) {
//        Attachment attachment = new Attachment();
//        if (document != null) {
//            Map<String, Attachment> map = document.getAttachments();
//            attachment = map.get(couchdbAttachBean.getOtherName());
//        }
//        return attachment;
//    }

}
