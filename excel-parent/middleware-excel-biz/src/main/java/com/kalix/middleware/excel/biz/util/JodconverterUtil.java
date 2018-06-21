package com.kalix.middleware.excel.biz.util;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.StreamOpenOfficeDocumentConverter;
import com.kalix.framework.core.util.ConfigUtil;
import com.lowagie.text.pdf.PdfReader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2018/6/7.
 * 跨平台openOffice有个各个平台的版本
 */
public class JodconverterUtil {

    public static void main(String[] args) {
        String docurl = "http://47.92.53.234:5984/kalix/2f0b21720b124bc6bff37d96e2769f63/中华人民共和国禁毒法.docx";
        String docfile = "E:/test.docx";
        //docurl = "http://47.92.53.234:5984/kalix/38667ba15b6944519020175a4ff69d26/行政执法评议考核制度.doc";
        docfile = "E:/test.doc";
        docfile = "E:/test.docx";
        //docfile = "/root/test.doc";
        //JacobUtil.downloadFile(docurl, docfile);
        String pdffile = "E:/test.pdf";
        //pdffile = "/root/test.pdf";
        word2pdf(docfile, pdffile);
        pdf2Image(pdffile, 96);
    }

    public static void downloadFile(String fileUrl, String localFile) {
        URL urlfile = null;
        HttpURLConnection httpUrl = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File f = new File(localFile);
        try {
            int index = fileUrl.lastIndexOf("/");
            String fileName = URLEncoder.encode(fileUrl.substring(index + 1), "utf-8");
            fileUrl = fileUrl.substring(0, index + 1);
            urlfile = new URL(fileUrl + fileName);
            httpUrl = (HttpURLConnection) urlfile.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            bos = new BufferedOutputStream(new FileOutputStream(f));
            int len = 2048;
            byte[] b = new byte[len];
            while ((len = bis.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            bos.flush();
            System.out.println("文件下载保存成功！");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
                if (httpUrl != null) httpUrl.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void word2pdf(String docfile, String pdffile) {
        System.out.println("word文档转换pdf开始...");
        long start = System.currentTimeMillis();
        // 启动服务
        // 这里是OpenOffice_EXE的安装路径
        String OpenOffice_EXE = "";
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.indexOf("linux") >= 0) {
            OpenOffice_EXE = (String) ConfigUtil.getConfigProp("linux.openoffice.exe", "ConfigOpenOffice");
        } else if (OS.indexOf("windows") >= 0) {
            OpenOffice_EXE = (String) ConfigUtil.getConfigProp("windows.openoffice.exe", "ConfigOpenOffice");
        } else {
            System.out.println("未找到openoffice应用soffice.exe安装路径，无法进行转换");
            return;
        }
        if (OS.indexOf("windows") >= 0) {
            OpenOffice_EXE = "\"" + OpenOffice_EXE + "\"";
        }
        Process pro = null;
        String openofficePort = (String) ConfigUtil.getConfigProp("openoffice.port", "ConfigOpenOffice");
        int port = Integer.valueOf(openofficePort);
        //int port = 8200;
        // 启动OpenOffice的服务
        String params = " -headless -accept=\"socket,host=127.0.0.1,port=" + openofficePort + ";urp;\" -nofirststartwizard";
        String command = OpenOffice_EXE + params;
        // connect to an OpenOffice.org instance running on port 8100
        OpenOfficeConnection connection = null;
        try {
            // liunx系统测试未通过，需要代码外启动
            if (OS.indexOf("windows") >= 0) {
                pro = Runtime.getRuntime().exec(command);
            }
            connection = new SocketOpenOfficeConnection("127.0.0.1", port);
            connection.connect();
            // convert
            // DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
            DocumentConverter converter = new StreamOpenOfficeDocumentConverter(connection);
            File fromfile = new File(docfile);
            File tofile = new File(pdffile);
            if (tofile.exists()) {
                tofile.delete();
            }
            converter.convert(fromfile, tofile);
            long end = System.currentTimeMillis();
            System.out.println("转换完成，用时：" + (end - start) + "ms");
        } catch (Exception e) {
            System.out.println("word文档转换pdf失败，原因：" + e.getMessage());
            e.printStackTrace();
        } finally {
            // close the connection
            if (connection != null) connection.disconnect();
            if (pro != null) pro.destroy();
        }
        System.out.println("word文档转换pdf完毕！");
    }

    /***
     * PDF文件转jpg图片，全部页数
     *
     * @param pdffile pdf文件，包括完整路径和.pdf文件名称
     * @param dpi     dpi越大转换后越清晰，相对转换速度越慢
     * @return
     * @author hqj:
     * @version 创建时间：2018年06月09日
     */
    public static void pdf2Image(String pdffile, int dpi) {
        System.out.println("pdf文档转换jpg开始...");
        File file = new File(pdffile);
        PDDocument pdDocument = null;
        PDFRenderer renderer = null;
        try {
            String imgPDFPath = file.getParent();
            int dot = file.getName().lastIndexOf('.');
            // 获取图片文件名
            String imagePDFName = file.getName().substring(0, dot);
            pdDocument = PDDocument.load(file);
            renderer = new PDFRenderer(pdDocument);
            /* dpi越大转换后越清晰，相对转换速度越慢 */
            PdfReader reader = new PdfReader(pdffile);
            int pages = reader.getNumberOfPages();
            int pages_len = String.valueOf(pages).length();
            String imgFilePathPrefix = imgPDFPath + File.separator + imagePDFName;
            for (int i = 0; i < pages; i++) {
                StringBuffer imgFilePath = new StringBuffer();
                imgFilePath.append(imgFilePathPrefix);
                imgFilePath.append("_");
                // 数字长度左补0
                String tmpStr = String.valueOf(i + 1);
                int b0 = pages_len - tmpStr.length();
                for (int j = 0; j < b0; j++) {
                    tmpStr = "0" + tmpStr;
                }
                imgFilePath.append(tmpStr);
                imgFilePath.append(".jpg");
                File dstFile = new File(imgFilePath.toString());
                BufferedImage image = renderer.renderImageWithDPI(i, dpi);
                ImageIO.write(image, "jpg", dstFile);
            }
            System.out.println("pdf文档转换jpg成功！");
        } catch (IOException e) {
            System.out.println("pdf文档转换jpg失败，原因：" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (pdDocument != null) {
                try {
                    pdDocument.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("pdf文档转换jpg完毕！");
    }
}
