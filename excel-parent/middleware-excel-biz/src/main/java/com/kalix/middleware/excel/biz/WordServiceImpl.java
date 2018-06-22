package com.kalix.middleware.excel.biz;

import com.kalix.framework.core.api.persistence.JsonData;
import com.kalix.framework.core.util.ConfigUtil;
import com.kalix.middleware.excel.api.biz.IWordService;
import com.kalix.middleware.excel.biz.util.JodconverterUtil;
import org.osgi.framework.BundleContext;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2018/6/15.
 */
public class WordServiceImpl implements IWordService{

    @Override
    public JsonData reviewWordAtt(String attId, String attName, String attPath) {
        JsonData jsonData = new JsonData();
        //String realPath = request.getSession().getServletContext().getRealPath("/");
        String realPath = (String) ConfigUtil.getConfigProp("word.review.realpath", "ConfigOpenOffice");
        if (realPath.charAt(realPath.length() - 1) != '/') {
            realPath += "/";
        }
        String reviewBaseDir = realPath + "reviewfiles";
        String imgPath = reviewBaseDir + "/" + attId;
        try {
            String fileUrl = attPath;
            String couchdbServerName = (String) ConfigUtil.getConfigProp("IP", "ConfigCouchdb");
            fileUrl = fileUrl.replaceFirst(couchdbServerName, "localhost");
            int fileTypeIndex = fileUrl.lastIndexOf(".");
            String fileType = fileUrl.substring(fileTypeIndex + 1).toLowerCase();
            if (fileType.equals("doc") || fileType.equals("docx")) {
                boolean existDir = isExistReviewDir(reviewBaseDir, attId);
                if (!existDir) {
                    String docFile = reviewBaseDir + "/" + attId + "/" + attName;
                    JodconverterUtil.downloadFile(fileUrl, docFile);
                    String pdfFile = docFile.substring(0, docFile.lastIndexOf(".")) + ".pdf";
                    JodconverterUtil.word2pdf(docFile, pdfFile);
                    JodconverterUtil.pdf2Image(pdfFile, 96);
                }
                // 筛选读取jpg图片
                List<File> imgFiles = new ArrayList<File>();
                File file = new File(imgPath);
                File[] fileList = file.listFiles();
                for (int i = 0; i < fileList.length; i++) {
                    File readFile = fileList[i];
                    if (readFile.isFile()) {
                        fileType = readFile.getName().substring(readFile.getName().lastIndexOf(".") + 1).toLowerCase();
                        if (fileType.equals("jpg")) {
                            imgFiles.add(readFile);
                        }
                    }
                }
                // 图片文件按名称排序
                Collections.sort(imgFiles, new Comparator<File>() {
                    @Override
                    public int compare(File o1, File o2) {
                        if (o1.isDirectory() && o2.isFile())
                            return 1;
                        if (o1.isFile() && o2.isDirectory())
                            return -1;
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                List<String> imgUrls = new ArrayList<String>();
                String server_url = (String) ConfigUtil.getConfigProp("server_url", "ConfigWebContext");
                String reviewBaseUrl = server_url + "/camel/servlet/review?foldername=" + attId;
                for (File imgFile : imgFiles) {
                    //BufferedImage image = ImageIO.read(imgFile);
                    // 图像缩放
                    /*int imgWidth = 100;
                    int imgHeight = 100;
                    BufferedImage outImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
                    Graphics g = outImage.createGraphics();
                    g.setColor(Color.white);
                    g.fillRect(0, 0, imgWidth, imgHeight);
                    g.drawImage(image, 0, 0, imgWidth, imgHeight, null);
                    g.dispose();*/

                    long time = System.currentTimeMillis();
                    String imgUrl = reviewBaseUrl + "&filename=" + imgFile.getName() + "&filetype=image&time=" + String.valueOf(time);
                    imgUrls.add(imgUrl);
                }
                jsonData.setTotalCount((long) imgFiles.size());
                jsonData.setData(imgUrls);
            } else {
                jsonData.setTotalCount(0L);
                //String info = "非word文件，无法预览！";
                jsonData.setData(null);
            }
        } catch (Exception e) {
            deleteAllFiles(imgPath);
            File folder = new File(imgPath);
            if (folder.exists()) folder.delete();
            jsonData.setTotalCount(0L);
            //String errInfo = e.getMessage();
            jsonData.setData(null);
            e.printStackTrace();
        }
        return jsonData;
    }

    /**
     * 判断是否存在预览文件夹
     * 不存在则创建文件夹
     *
     * @param reviewBaseDir
     * @param attid
     * @return
     */
    private boolean isExistReviewDir(String reviewBaseDir, String attid) {
        boolean exist = false;
        String filepath = reviewBaseDir + "/" + attid;
        File file = new File(filepath);
        if (file.exists()) {
            exist = true;
        } else {
            File fileBaseDir = new File(reviewBaseDir);
            if (!fileBaseDir.exists()) {
                fileBaseDir.mkdir();
            }
            file.mkdir();
        }
        return exist;
    }

    /**
     * 删除指定文件夹下所有文件
     *
     * @param path 文件夹完整绝对路径
     * @return
     */
    private boolean deleteAllFiles(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                String childPath = path + "/" + tempList[i];
                //先删除文件夹里面的文件
                deleteAllFiles(childPath);
                //再删除空文件夹
                File folder = new File(childPath);
                folder.delete();
                flag = true;
            }
        }
        return flag;
    }
}
