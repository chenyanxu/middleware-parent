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
    }

    @Override
    public Response addAttachment(String value, String key, String type) {
        Attachment attachment = new Attachment();
        Document document = new Document();
        Response response = null;
        try {
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
    public void updated(Dictionary<String, ?> dictionary)  {
        db_name = (String) dictionary.get("DB_NAME");
        protocol = (String) dictionary.get("PROTOCOL");
        ip = (String) dictionary.get("IP");
        port = Integer.parseInt((String) dictionary.get("PORT"));
        user = (String) dictionary.get("USER");
        password = (String) dictionary.get("PASSWORD");
        url = (String) dictionary.get("COUCHDB_URL");
        try {
            dbClient = new CouchDbClient(db_name, true, protocol, ip, port, user, password);
            SystemUtil.succeedPrintln("succeed connect to couchdb! IP address is " + ip);
        } catch (Exception e) {
            SystemUtil.errorPrintln("can not connect to couchdb address " + ip + "!");
//            e.printStackTrace();
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
