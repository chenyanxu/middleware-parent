package com.kalix.middleware.couchdb.rest;

import com.kalix.middleware.couchdb.api.biz.ICouchdbService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.util.ObjectHelper;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.lightcouch.Response;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理附件，写入到couchdb
 */
public class AttachmentProcessor implements Processor {
    private ICouchdbService couchdbService = null;
    private ServletFileUpload uploader = null;
    //private CouchdbJsonStatus jsonStatus;
    private Map<String, Object> rtnMap = null;

    public AttachmentProcessor() {
        this.rtnMap = new HashMap();
        this.uploader = new ServletFileUpload(new DiskFileItemFactory());
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        this.rtnMap.clear();

        try {
            HttpServletRequest request = ObjectHelper.cast(HttpServletRequest.class, exchange.getIn().getHeader(Exchange.HTTP_SERVLET_REQUEST));

            if (!ServletFileUpload.isMultipartContent(request)) {
                this.rtnMap.put("success", false);
                this.rtnMap.put("msg", "文件导入失败！无效的请求！");
                //throw new RuntimeException("Invalid Multipart Content request!");
                return;
            }

            uploader.setHeaderEncoding("utf-8");

            ServletRequestContextWrapper wrapper = new ServletRequestContextWrapper(request);
            wrapper.setInputStream(exchange.getIn().getBody(InputStream.class));
            List<FileItem> items = uploader.parseRequest(wrapper);
            if (items.isEmpty()) {
                this.rtnMap.put("success", false);
                this.rtnMap.put("msg", "文件导入失败！文件为空！");
                //throw new RuntimeException("Invalid Multipart/form-data Content, file item is empty!");
                return;
            }

            FileItem fileItem = null;

            if (items.size() == 1) {
                fileItem = items.get(0);
            }

            Response response = null;

            //exchange.getIn().setHeader("Content-Type", "application/json;charset=utf-8");
            exchange.getIn().setHeader("Content-Type", "text/html;charset=utf-8");

            if (fileItem != null) {
//                if(fileItem.getSize()>(10*1024*1024)){
//                    //jsonStatus = CouchdbJsonStatus.failureResult("文件过大（上限10MB）！");
//                    this.rtnMap.put("success", false);
//                    this.rtnMap.put("msg", "文件过大（上限10MB）！");
//                    //exchange.getIn().setBody(jsonStatus);
//                    //return;
//                } else {
                try {
                    String fileName = fileItem.getName();
//                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                        OutputStream out = new Base64OutputStream(stream);
//                        IOUtils.copy(fileItem.getInputStream(), out);
//                        String base64Str = stream.toString();
//                        fileItem.getInputStream().close();
//                        out.close();

                    response = couchdbService.addAttachment(fileItem.getInputStream(),
                            fileName, fileItem.getContentType());
                    //jsonStatus = CouchdbJsonStatus.successResult("上传文件成功!", response.getId(), response.getRev(), fileName, fileItem.getContentType(), fileItem.getSize());
                    //jsonStatus.setAttachmentPath(couchdbService.getDBUrl() + response.getId() + "/" + fileName);
                    //Map<String,Object> rtnMap=new HashMap<String,Object>();

                    rtnMap.put("success", true);
                    rtnMap.put("attachmentId", response.getId());
                    rtnMap.put("attachmentRev", response.getRev());
                    rtnMap.put("attachmentName", fileName);
                    rtnMap.put("attachmentType", fileItem.getContentType());
                    rtnMap.put("attachmentSize", fileItem.getSize());
                    rtnMap.put("attachmentPath", couchdbService.getDBUrl() + response.getId() + "/" + fileName);
                    rtnMap.put("msg", "上传文件成功!");

                    //exchange.getIn().setBody(jsonStatus);
                    //exchange.getIn().setBody(rtnMap);
                } catch (Exception e) {
                    e.printStackTrace();
                    //jsonStatus = CouchdbJsonStatus.failureResult("上传文件失败！异常为{" + e.toString() + "}");
                    this.rtnMap.put("success", false);
                    this.rtnMap.put("msg", "上传文件失败！异常为{" + e.toString() + "}");
                    return;
                }
            }
            //}
        } catch (Exception e) {
            //jsonStatus = CouchdbJsonStatus.failureResult("上传文件失败！异常为{" + e.toString() + "}");
            //exchange.getOut().setBody(jsonStatus);
            e.printStackTrace();
            this.rtnMap.put("success", false);
            this.rtnMap.put("msg", "上传文件失败！异常为{" + e.toString() + "}");
            return;
        } finally {
            exchange.getIn().setBody(rtnMap);
        }
    }

    public ICouchdbService getCouchdbService() {
        return couchdbService;
    }

    public void setCouchdbService(ICouchdbService couchdbService) {
        this.couchdbService = couchdbService;
    }


    public static final class ServletRequestContextWrapper extends ServletRequestContext {

        public InputStream inputStream;

        public ServletRequestContextWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public InputStream getInputStream() {
            return inputStream;
        }

        public void setInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }
    }
}
