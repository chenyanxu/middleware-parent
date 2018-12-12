package com.kalix.middleware.excel.rest;

import com.kalix.framework.core.api.security.IShiroService;
import com.kalix.framework.core.util.HttpClientUtil;
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
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.util.ClassUtils;
import org.osgi.framework.BundleContext;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
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
        //int recIndex = 0;
        int startRow = 0;
        try {
            HttpServletRequest request = ObjectHelper.cast(HttpServletRequest.class, exchange.getIn().getHeader(Exchange.HTTP_SERVLET_REQUEST));
            String ServiceUrl = "";
            Class entityClass = null;
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
            // items = uploader.parseRequest(request);
            exchange.getIn().setHeader("Content-Type", "text/html;charset=utf-8");
            if (items.isEmpty()) {
                this.rtnMap.put("success", false);
                this.rtnMap.put("msg", "文件导入失败！文件为空！");
                //throw new RuntimeException("Invalid Multipart/form-data Content, file item is empty!");
                return;
            } else {
                int rowCount = 0;
                int importCount = 0;
                String sheetName = "";
                String serviceDictUrl = "";
                String importInfo = "";
                for (FileItem item : items) {
                    if (item.isFormField()) {
                        if ("EntityName".equals(item.getFieldName())) {
                            entityClass = ClassUtils.forName(item.getString("utf-8"));
                        }
                        if ("ServiceUrl".equals(item.getFieldName())) {
                            ServiceUrl = item.getString("utf-8");
                            //bizService = JNDIHelper.getJNDIServiceForName(serviceInterface);

                        }
                        if ("serviceDictUrl".equals(item.getFieldName())) {
                            serviceDictUrl = item.getString("utf-8");
                            // bizService = JNDIHelper.getJNDIServiceForName(serviceDictInterface);
                        }
                        if ("startRow".equals(item.getFieldName())) {
                            if (StringUtils.isNotEmpty(item.getString("utf-8"))) {
                                startRow = Integer.valueOf(item.getString("utf-8"));
                            }
                        }
                        if ("sheetName".equals(item.getFieldName())) {
                            if (StringUtils.isNotEmpty(item.getString("utf-8"))) {
                                sheetName = item.getString("utf-8");
                            }
                        }

                        // 非上传组件
                        // System.out.println("组件名称:" + item.getFieldName());
                        // System.out.println("内容:" + item.getString("utf-8")); // 解决乱码问题
                    } else {
                        Map map_parm = new HashMap();
                        IShiroService shiroService = JNDIHelper.getJNDIServiceForName(IShiroService.class.getName());
                        String access_token = shiroService.getSession().getAttribute("access_token").toString();
                        String sessionId = shiroService.getSession().getId().toString();
                        map_parm.put("access_token", access_token);
                        map_parm.put("sessionId", sessionId);
                        map_parm.put("serviceDictUrl", serviceDictUrl);
//                        String name = item.getName(); // 上传文件名称
//                        name = name.substring(name.lastIndexOf("\\") + 1);
                        Object wb = excelService.OpenExcel(item.getInputStream(), item.getName());
                        Object sheet = excelService.OpenSheet(wb, sheetName);
                        rowCount = excelService.GetRowCount(sheet);
                        List<Object> bookList = (List<Object>) excelService.GetColumnDic(sheet, startRow, entityClass, map_parm);
                        importCount = bookList.size();
                        importInfo = excelService.GetImportInfo();
                        /*for (Object obj : bookList) {
                            Map<String, String> map = SerializeUtil.json2Map(SerializeUtil.serializeJson(obj, "yyyy-MM-dd HH:mm:ss"));
                            map.remove("id");
                            map.remove("version");
                            HttpClientUtil.shiroPost(ServiceUrl, map, sessionId, access_token);
                        }*/
                        if (StringUtils.isNotEmpty(importInfo)) {
                            if (importInfo.contains("列")) {
                                this.rtnMap.put("success", false);
                                this.rtnMap.put("msg", "文件导入失败！文件存在错误数据，具体原因" + importInfo);
                                return;
                            }
                        }
                        for (int i = importCount - 1; i > -1; i--) {
                            Object obj = bookList.get(i);
                            Map<String, String> map = SerializeUtil.json2Map(SerializeUtil.serializeJson(obj, "yyyy-MM-dd HH:mm:ss"));
                            map.remove("id");
                            map.remove("version");
                            if (ServiceUrl.toLowerCase().indexOf("completion") > -1) {
                                String stem = map.get("stem").toString();
                                int num = getSpaceNum(stem, "[#");
                                map.put("spaceNum", String.valueOf(num));
                                if(num!=0){
                                    HttpClientUtil.shiroPost(ServiceUrl, map, sessionId, access_token);
                                }

                            }else {
                                HttpClientUtil.shiroPost(ServiceUrl, map, sessionId, access_token);
                            }

                        }
                        // 删除临时文件
                        item.delete();
                    }
                }
                this.rtnMap.put("success", true);
                this.rtnMap.put("msg", "文件导入成功，共" + (rowCount - startRow + 1) + "条记录，导入" + importCount + "条记录" + importInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //  throw new RuntimeException(String.format("请检查表格第 %s 行", recIndex + 1));
            this.rtnMap.put("success", false);
            this.rtnMap.put("msg", "文件导入失败！异常为{" + e.toString() + "}");
            return;
        } finally {
            //items.clear();
            exchange.getIn().setBody(rtnMap);
        }
    }

    public int getSpaceNum(String stem, String findText) {
        int count = 0;
        int index = 0;
        while ((index = stem.indexOf(findText, index)) != -1) {
            index = index + findText.length();
            count++;
        }
        return count;
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
