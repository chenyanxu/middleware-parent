package com.kalix.middleware.excel.biz;

import com.kalix.framework.core.api.security.IShiroService;
import com.kalix.framework.core.util.JNDIHelper;
import com.kalix.middleware.excel.api.biz.IExportExcelService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hqj on 2018/02/02.
 * servlet基类服务，根据模板下载文件
 * 请求地址rest/blueprint.xml配置: "/camel/servlet/download"
 * 请求参数: "?beanname=&id=xxx&filetype=word"
 */
public class ExportExcelServlet extends HttpServlet {
    private IExportExcelService exportExcelService;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OutputStream out = null;
        PrintWriter outHtml = null;
        try {
            // 实体名称
            String jsonStr = req.getParameter("jsonStr") == null ? "" : req.getParameter("jsonStr");
            String EntityName = req.getParameter("EntityName") == null ? "" : req.getParameter("EntityName");
            String ServiceUrl = req.getParameter("ServiceUrl") == null ? "" : req.getParameter("ServiceUrl");
            String serviceDictUrl = req.getParameter("serviceDictUrl") == null ? "" : req.getParameter("serviceDictUrl");
            Map map = new HashMap();
            map.put("jsonStr",jsonStr);
            map.put("EntityName",EntityName);
            map.put("ServiceUrl",ServiceUrl);
            map.put("serviceDictUrl",serviceDictUrl);
            exportExcelService = JNDIHelper.getJNDIServiceForName(IExportExcelService.class.getName());
            exportExcelService.doExport(map, resp);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
            if (outHtml != null) {
                outHtml.flush();
                outHtml.close();
            }
        }
    }


}
