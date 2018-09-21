package com.kalix.middleware.excel.rest;

import com.kalix.framework.core.api.biz.IBizService;
import com.kalix.framework.core.api.persistence.PersistentEntity;
import com.kalix.framework.core.delegate.DelegatedClassLoadingHelper;
import com.kalix.framework.core.util.ConfigUtil;
import com.kalix.framework.core.util.JNDIHelper;
import com.kalix.framework.core.util.SerializeUtil;
import com.kalix.middleware.excel.api.biz.IExcelService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.util.ObjectHelper;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.shiro.util.ClassUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExcelProcessor implements Processor {
    private BundleContext bundleContext;
    private IExcelService excelService = null;
    private ServletFileUpload uploader = null;
    private Map<String, Object> rtnMap = null;

    public ExcelProcessor() {
        this.rtnMap = new HashMap<>();
        this.uploader = new ServletFileUpload(new DiskFileItemFactory());

    }

    @Override
    public void process(Exchange exchange) throws Exception {
        this.rtnMap.clear();
        int recIndex = 0;
        //List<FileItem> items=null;
        try {
            HttpServletRequest request = ObjectHelper.cast(HttpServletRequest.class, exchange.getIn().getHeader(Exchange.HTTP_SERVLET_REQUEST));
            String serviceInterface = "";
            Class entityClass= null;
            if (!ServletFileUpload.isMultipartContent(request)) {
                throw new RuntimeException("Invalid Multipart Content request!");
            }
            uploader.setHeaderEncoding("utf-8");
            ServletRequestContextWrapper wrapper = new ServletRequestContextWrapper(request);
            wrapper.setInputStream(exchange.getIn().getBody(InputStream.class));
            List<FileItem> items = uploader.parseRequest(wrapper);
           // items = uploader.parseRequest(request);
            exchange.getIn().setHeader("Content-Type", "text/html;charset=utf-8");
            if (items.isEmpty()) {
                throw new RuntimeException("Invalid Multipart/form-data Content, file item is empty!");
            }
            else {
                IBizService bizService=null;
                String serviceDictInterface="";
                for (FileItem item : items) {
                    if (item.isFormField()) {
                        if("EntityName".equals(item.getFieldName()))
                        {
                            entityClass=ClassUtils.forName(item.getString("utf-8"));
                        }
                        if("ServiceInterface".equals(item.getFieldName()))
                        {
                            serviceInterface=item.getString("utf-8");
                            bizService = JNDIHelper.getJNDIServiceForName(serviceInterface);
                        }
                        if("serviceDictInterface".equals(item.getFieldName()))
                        {
                            serviceDictInterface=item.getString("utf-8");
                           // bizService = JNDIHelper.getJNDIServiceForName(serviceDictInterface);
                        }

                        // 非上传组件
                       // System.out.println("组件名称:" + item.getFieldName());
                       // System.out.println("内容:" + item.getString("utf-8")); // 解决乱码问题
                    } else {
                        // 上传组件
                       // System.out.println("组件名称:" + item.getFieldName());
                      //  System.out.println("上传文件名称:" + item.getName());

                        String name = item.getName(); // 上传文件名称
                        System.out.println(name);
                        name = name.substring(name.lastIndexOf("\\") + 1);
                        Object wb = excelService.OpenExcel(item.getInputStream(), item.getName());
                        Object sheet = excelService.OpenSheet(wb, "Sheet1");
                        int startRow = new Integer(2) - 1;
                        int rowCount = excelService.GetRowCount(sheet);
                        List<Object> bookList = (List<Object>)  excelService.GetColumnDic(sheet, startRow, entityClass,serviceDictInterface);
                        for(Object obj:bookList){
                            PersistentEntity objEntity= (PersistentEntity)obj;
                            bizService.saveEntity(objEntity);

                        }
                        // 删除临时文件
                        item.delete();
                    }

                }
            }

 //           FileItem fileItem = null;
//
//            if (items.size() == 1) {
//                fileItem = items.get(0);
//            }

//            if (fileItem != null) {
//
//                if (fileItem.getSize() > (10 * 1024 * 1024)) {
//                    this.rtnMap.put("success", false);
//                    this.rtnMap.put("msg", "文件过大（上限10MB）！");
//                } else {
//                   // Dictionary dictionary = ConfigUtil.getAllConfig(configId);
//                    Object wb = excelService.OpenExcel(fileItem.getInputStream(), fileItem.getName());
//                    Object sheet = excelService.OpenSheet(wb, "Sheet1");
//                    //Object   className  = entityName.getClass().getInterfaces();
//                    List<Object> bookList = (List<Object>)  excelService.GetColumnDic(sheet, 0, entityName.getClass());
//                    int startRow = new Integer(dictionary.get("start_row").toString()) - 1;
//                    int rowCount = excelService.GetRowCount(sheet);
//
//                    for (int idx = startRow; idx < rowCount; ++idx) {
//                        Map rowMap = excelService.GetRowMap(sheet, idx, columnMap);
//                        recIndex = idx;
//                        PersistentEntity obj = SerializeUtil.unserializeJson(SerializeUtil.serializeJson(rowMap), entityClass);
//
//                        bizService.saveEntity(obj);
//                    }
//                }
//            }

            this.rtnMap.put("success", true);
            this.rtnMap.put("msg", "文件导入成功");

        } catch (Exception e) {
            e.printStackTrace();
          //  throw new RuntimeException(String.format("请检查表格第 %s 行", recIndex + 1));
            this.rtnMap.put("success", false);
            this.rtnMap.put("msg", "文件导入失败！异常为{" + e.toString() + "}");
        }
        finally {
            //items.clear();
            exchange.getIn().setBody(rtnMap);
        }
    }

    public IExcelService getExcelService() {
        return excelService;
    }

    public void setExcelService(IExcelService excelService) {
        this.excelService = excelService;
    }

    public BundleContext getBundleContext() {
        return bundleContext;
    }

    public void setBundleContext(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
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
