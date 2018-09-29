package com.kalix.middleware.excel.biz;

import com.kalix.framework.core.api.biz.IDownloadService;
import com.kalix.framework.core.util.JNDIHelper;
import com.kalix.framework.core.util.StringUtils;
import com.kalix.middleware.excel.api.biz.IExportExcelService;
import org.apache.shiro.util.ClassUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
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
            String beanName = req.getParameter("beanname") == null ? "" : req.getParameter("beanname");
            // 实体id
            String id = req.getParameter("id") == null ? "" : req.getParameter("id");
            exportExcelService = JNDIHelper.getJNDIServiceForName(IExportExcelService.class.getName());
            exportExcelService.doExport("测试模板",resp);

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

    public void setExportExcelService(IExportExcelService exportExcelService) {
        this.exportExcelService = exportExcelService;
    }
}
