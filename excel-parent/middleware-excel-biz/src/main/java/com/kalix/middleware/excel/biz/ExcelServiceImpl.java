package com.kalix.middleware.excel.biz;

import com.google.common.collect.Lists;
import com.kalix.framework.core.api.system.IDictBeanService;
import com.kalix.framework.core.util.JNDIHelper;
import com.kalix.framework.core.util.SerializeUtil;
import com.kalix.middleware.couchdb.api.biz.ICouchdbService;
import com.kalix.middleware.excel.api.annotation.ExcelField;
import com.kalix.middleware.excel.api.biz.IExcelService;
import com.kalix.middleware.excel.biz.util.Reflections;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.lightcouch.Response;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author chenyanxu
 */
public class ExcelServiceImpl implements IExcelService {
    private ICouchdbService couchdbService;
    //private IDictBeanService dictBeanService;
    //正则表达式 用于匹配属性的第一个字母
    private static final String REGEX = "[a-zA-Z]";

    @Override
    public Object OpenExcel(String excelPath) {
        try {
            InputStream is = new FileInputStream(excelPath);

            return OpenExcel(is, excelPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Object OpenExcel(InputStream is, String fileName) {
        try {

            Object wb = null;

            if (fileName.indexOf(".xlsx") == -1) {
                wb = new HSSFWorkbook(is);
            } else {
                wb = new XSSFWorkbook(is);
            }

            return wb;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Object OpenSheet(Object wb, String sheetName) {
        Workbook theWb = (Workbook) wb;

        return theWb.getSheet(sheetName);
    }

    @Override
    public int GetRowCount(Object sheet) {
        Sheet theSheet = (Sheet) sheet;
        int firstRowNum = theSheet.getFirstRowNum();
        int lastRowNum = theSheet.getLastRowNum();

        if (theSheet.getRow(firstRowNum) != null) {
            return lastRowNum - firstRowNum + 1;
        } else {
            return 0;
        }
    }

    @Override
    public List<Object> GetColumnNames(Object sheet, int columnRowIndex, Class<?> clazz,String serviceDictInterface) {
        List<Row> rtnList = new ArrayList<Row>();
        Sheet theSheet = (Sheet) sheet;
        int rows= GetRowCount(theSheet);
        List<Object[]> annotationList= getAnnotationList(clazz);
        for(int i=columnRowIndex;i<rows;i++)
        {
            Row row = theSheet.getRow(i);
            String col1 = getCellValue(row.getCell(0));
            String col2 = getCellValue(row.getCell(1));
            if (StringUtils.isNotEmpty(col1) && StringUtils.isNotEmpty(col2)) {
                rtnList.add(row);
            }
        }

        return returnObjectList(rtnList,clazz,annotationList,serviceDictInterface);
    }

    @Override
    public List<Object> GetColumnDic(Object sheet, int columnRowIndex, Class<?> clazz,String serviceDictInterface) {

      return GetColumnNames(sheet, columnRowIndex,clazz,serviceDictInterface);

    }

    @Override
    public Map<String, Object> GetRowMap(Object sheet, int rowIndex, Map<String, Integer> columnMap) {
        Map<String, Object> rowMap = new HashMap<String, Object>();
        Sheet theSheet = (Sheet) sheet;
        Row row = theSheet.getRow(rowIndex);
        Iterator<Map.Entry<String, Integer>> iterator = columnMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();
            Cell cell = row.getCell(entry.getValue());

            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    rowMap.put(entry.getKey(), row.getCell(entry.getValue()).getStringCellValue());

                case Cell.CELL_TYPE_NUMERIC:
                    rowMap.put(entry.getKey(), row.getCell(entry.getValue()).getNumericCellValue());

                case Cell.CELL_TYPE_BLANK:
                    rowMap.put(entry.getKey(), null);

                case Cell.CELL_TYPE_BOOLEAN:
                    rowMap.put(entry.getKey(), row.getCell(entry.getValue()).getBooleanCellValue());

                case Cell.CELL_TYPE_FORMULA:
                    // rowMap.put(entry.getKey(), row.getCell(entry.getValue()).get());

            }
        }

        return rowMap;
    }

    @Override
    public String GetJsonRowString(Object sheet, int rowIndex, Map<String, Integer> columnMap) {
        Map rowMap = GetRowMap(sheet, rowIndex, columnMap);

        return SerializeUtil.serializeJson(rowMap);
    }

    @Override
    public Map<String, Object> GetExcelFromJson(String jsonStr) {
        Map jsonMap = SerializeUtil.unserializeJson(jsonStr, java.util.Map.class);
        Map<String, Object> rtnMap = new HashMap<>();

        if (jsonMap != null) {
            ArrayList<String> fields = (ArrayList<String>) jsonMap.get("fields");
            ArrayList<String> columns = (ArrayList<String>) jsonMap.get("columns");
            ArrayList<Map> records = (ArrayList<Map>) jsonMap.get("records");
            ArrayList<Boolean> totals = (ArrayList<Boolean>) jsonMap.get("totals");
            String title = (String) jsonMap.get("title");
            String template = (String) jsonMap.get("template");
            Boolean rownumber = (Boolean) jsonMap.get("rownumber");

            if (fields != null && records != null && columns != null) {
                HSSFWorkbook wb = null;
                HSSFSheet sheet = null;
                HSSFRow row = null;
                HSSFCell cell = null;

                if (template == null) {
                    wb = new HSSFWorkbook();
                    sheet = wb.createSheet("sheet1");

                    HSSFCellStyle columnStyle = wb.createCellStyle();
                    Font font = wb.createFont();
                    columnStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    columnStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    columnStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
                    columnStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    //columnStyle.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
                    columnStyle.setBottomBorderColor(HSSFColor.BLACK.index);
                    columnStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                    columnStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    columnStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                    columnStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
                    font.setBold(true);
                    columnStyle.setFont(font);

                    if (title != null && !title.isEmpty()) {
                        row = sheet.createRow(0);
                        row.setHeight((short) 500);
                        cell = row.createCell(0);
                        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columns.size() - 1));
                        cell.setCellStyle(columnStyle);
                        cell.setCellValue(title);
                    }

                    if (title == null || title.isEmpty()) {
                        row = sheet.createRow(0);
                    } else {
                        row = sheet.createRow(1);
                    }

                    row.setHeight((short) 400);

                    for (int cIdx = 0; cIdx < columns.size(); ++cIdx) {
                        sheet.setColumnWidth(cIdx, 4000);
                        cell = row.createCell(cIdx);
                        cell.setCellStyle(columnStyle);
                        cell.setCellValue(columns.get(cIdx));
                    }
                } else {
                    wb = (HSSFWorkbook) OpenExcel(String.format("D:\\XlsTemplate\\%s.xls", template));
                    sheet = wb.getSheet("sheet1");
                }

                HSSFCellStyle cellStyle = wb.createCellStyle();

                cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
                cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);

                for (int rIndex = 0; rIndex < records.size(); ++rIndex) {
                    Map record = records.get(rIndex);

                    row = sheet.createRow(sheet.getLastRowNum() + 1);
                    row.setHeight((short) 400);

                    if (rownumber) {
                        cell = row.createCell(0);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(rIndex + 1);

                        for (int fIdx = 0; fIdx < fields.size(); ++fIdx) {
                            cell = row.createCell(fIdx + 1);
                            cell.setCellStyle(cellStyle);

                            if (totals.get(fIdx)) {
                                cell.setCellValue(Double.valueOf((String) record.get(fields.get(fIdx))));
                            } else {
                                cell.setCellValue((String) record.get(fields.get(fIdx)));
                            }
                        }
                    } else {
                        for (int fIdx = 0; fIdx < fields.size(); ++fIdx) {
                            cell = row.createCell(fIdx);
                            cell.setCellStyle(cellStyle);
                            if (totals.get(fIdx)) {
                                cell.setCellValue(Double.valueOf((String) record.get(fields.get(fIdx))));
                            } else {
                                cell.setCellValue((String) record.get(fields.get(fIdx)));
                            }
                        }
                    }
                }

                if (totals != null) {
                    row = sheet.createRow(sheet.getLastRowNum() + 1);
                    row.setHeight((short) 400);

                    String sumStr = null;

                    if (rownumber) {
                        cell = row.createCell(0);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue("合计");

                        for (int tIndex = 0; tIndex < totals.size(); ++tIndex) {
                            cell = row.createCell(tIndex + 1);
                            cell.setCellStyle(cellStyle);

                            if (totals.get(tIndex)) {
                                sumStr = String.format("SUM(%s:%s)",
                                        translateCol(tIndex + 1) + String.valueOf(sheet.getLastRowNum()),
                                        translateCol(tIndex + 1) + String.valueOf(sheet.getLastRowNum() - records.size() + 1));
//                            for (int rIndex = 0; rIndex < records.size(); ++rIndex) {
//                                sumStr += translateCol(tIndex) + String.valueOf(sheet.getLastRowNum() - rIndex) + ",";
//                            }
                                //sumStr = sumStr.substring(0,sumStr.lastIndexOf(",")) + ")";

                                cell.setCellFormula(sumStr);
                            }
                        }
                    } else {
                        for (int tIndex = 0; tIndex < totals.size(); ++tIndex) {
                            cell = row.createCell(tIndex);
                            cell.setCellStyle(cellStyle);

                            if (totals.get(tIndex)) {
                                sumStr = String.format("SUM(%s:%s)",
                                        translateCol(tIndex) + String.valueOf(sheet.getLastRowNum()),
                                        translateCol(tIndex) + String.valueOf(sheet.getLastRowNum() - records.size() + 1));
//                            for (int rIndex = 0; rIndex < records.size(); ++rIndex) {
//                                sumStr += translateCol(tIndex) + String.valueOf(sheet.getLastRowNum() - rIndex) + ",";
//                            }
                                //sumStr = sumStr.substring(0,sumStr.lastIndexOf(",")) + ")";

                                cell.setCellFormula(sumStr);
                            }
                        }
                    }
                }

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                ByteArrayOutputStream xlsStrem = new ByteArrayOutputStream();
                OutputStream out = new Base64OutputStream(stream);

                try {
                    wb.write(xlsStrem);
                    IOUtils.copy(new ByteArrayInputStream(xlsStrem.toByteArray()), out);
                    String base64Str = stream.toString();
                    Response response = couchdbService.addAttachment(base64Str,
                            "report.xls", "application/vnd.ms-excel");
                    out.close();
                    xlsStrem.close();
                    stream.close();
                    rtnMap.put("success", true);
                    rtnMap.put("msg", "处理成功");

                    String url = couchdbService.getDBUrl() + response.getId() + "/report.xls";
                    rtnMap.put("url", url);
                } catch (IOException e) {
                    e.printStackTrace();
                    rtnMap.put("success", false);
                    rtnMap.put("msg", "处理失败");
                }
            } else {
                rtnMap.put("success", false);
                rtnMap.put("msg", "数据格式错误");
            }
        } else {
            rtnMap.put("success", false);
            rtnMap.put("msg", "数据格式错误");
        }


        return rtnMap;
    }

    public ICouchdbService getCouchdbService() {
        return couchdbService;
    }

//    public void setDictBeanService(IDictBeanService dictBeanService) {
//        this.dictBeanService = dictBeanService;
//    }

    public void setCouchdbService(ICouchdbService couchdbService) {
        this.couchdbService = couchdbService;
    }

    private String translateCol(int index) {
        switch (index) {
            case 0:
                return "A";
            case 1:
                return "B";
            case 2:
                return "C";
            case 4:
                return "E";
            case 5:
                return "F";
            case 6:
                return "G";
            case 7:
                return "H";
            case 8:
                return "I";
            case 9:
                return "J";
            case 10:
                return "K";
            case 11:
                return "L";
            case 12:
                return "M";
            case 13:
                return "N";
            case 14:
                return "0";
            case 15:
                return "P";
            case 16:
                return "Q";
            case 17:
                return "R";
            case 18:
                return "S";
            case 19:
                return "T";
            case 20:
                return "U";
            case 21:
                return "V";
            case 22:
                return "W";
            case 23:
                return "X";
            case 24:
                return "Y";
            case 25:
                return "Z";
            default:
                return "";
        }
    }


    /**
     * 功能:获取单元格的值
     */
    private static String getCellValue(Cell cell) {
        Object result = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    result = cell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    result = cell.getNumericCellValue();
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    result = cell.getBooleanCellValue();
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    result = cell.getCellFormula();
                    break;
                case Cell.CELL_TYPE_ERROR:
                    result = cell.getErrorCellValue();
                    break;
                case Cell.CELL_TYPE_BLANK:
                    break;
                default:
                    break;
            }
        }
        return result.toString();
    }

    /**
     * 功能:返回指定的对象集合
     */
    private  List<Object> returnObjectList(List<Row> rowList,Class<?> clazz,List<Object[]> annotationList,String serviceDictInterface) {
        List<Object> objectList=null;
        Object obj=null;
        int j=0;
        try {
            objectList=new ArrayList<Object>();
            for (Row row : rowList) {
                obj = clazz.newInstance();
                setAttrributeValue(obj,annotationList,row,objectList,serviceDictInterface);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objectList;
    }


    private List<Object[]>  getAnnotationList(Class cls, int... groups){
        List<Object[]> annotationList = Lists.newArrayList();
        // Get annotation field
        Field[] fs = cls.getDeclaredFields();
        for (Field f : fs){
            ExcelField ef = f.getAnnotation(ExcelField.class);
            if (ef != null && (ef.type()==0 || ef.type()==2)){
                if (groups!=null && groups.length>0){
                    boolean inGroup = false;
                    for (int g : groups){
                        if (inGroup){
                            break;
                        }
                        for (int efg : ef.groups()){
                            if (g == efg){
                                inGroup = true;
                                annotationList.add(new Object[]{ef, f});
                                break;
                            }
                        }
                    }
                }else{
                    annotationList.add(new Object[]{ef, f});
                }
            }
        }
        // Get annotation method
        Method[] ms = cls.getDeclaredMethods();
        for (Method m : ms){
            ExcelField ef = m.getAnnotation(ExcelField.class);
            if (ef != null && (ef.type()==0 || ef.type()==2)){
                if (groups!=null && groups.length>0){
                    boolean inGroup = false;
                    for (int g : groups){
                        if (inGroup){
                            break;
                        }
                        for (int efg : ef.groups()){
                            if (g == efg){
                                inGroup = true;
                                annotationList.add(new Object[]{ef, m});
                                break;
                            }
                        }
                    }
                }else{
                    annotationList.add(new Object[]{ef, m});
                }
            }
        }
        // Field sorting
        Collections.sort(annotationList, new Comparator<Object[]>() {
            public int compare(Object[] o1, Object[] o2) {
                return new Integer(((ExcelField)o1[0]).sort()).compareTo(
                        new Integer(((ExcelField)o2[0]).sort()));
            };
        });
        return annotationList;
    }


    /**
     * 功能:给指定对象的指定属性赋值
     */
    private static void setAttrributeValue(Object obj, List<Object[]> annotationList,Row row,List<Object> objectList,String serviceDictInterface) throws IOException {
        //得到该属性的set方法名
       // String method_name = convertToMethodName(attribute,obj.getClass(),true);
       // Method[] methods = obj.getClass().getMethods();
        Object val=null;
        int column = 0;
        for (Object[] os : annotationList) {
            val = getCellValue(row.getCell(column++));
            /**
             * 因为这里只是调用bean中属性的set方法，属性名称不能重复
             * 所以set方法也不会重复，所以就直接用方法名称去锁定一个方法
             * （注：在java中，锁定一个方法的条件是方法名及参数）
             */
            if (val != null) {
                ExcelField ef = (ExcelField)os[0];
                // If is dict type, get dict value
                if (ef.dictType()!=null&& !"".equals(ef.dictType())){
                   // String serviceDictInterface="com.kalix.enrolment.system.dict.api.biz.IEnrolmentDictBeanService";
                    IDictBeanService dictBeanService = JNDIHelper.getJNDIServiceForName(serviceDictInterface);
                    val= dictBeanService.getValueByTypeAndLabel(ef.dictType(),val.toString());
                    //val= dictBeanService.getByTypeAndLabel(ef.dictType(),val.toString());
                    //val=0;
                }
                // Get param type and type cast
                Class<?> valType = obj.getClass();
                if (os[1] instanceof Field){
                    valType = ((Field)os[1]).getType();
                }else if (os[1] instanceof Method){
                    Method method = ((Method)os[1]);
                    if ("get".equals(method.getName().substring(0, 3))){
                        valType = method.getReturnType();
                    }else if("set".equals(method.getName().substring(0, 3))){
                        valType = ((Method)os[1]).getParameterTypes()[0];
                    }
                }
                //log.debug("Import value type: ["+i+","+column+"] " + valType);
                try {
                    if (valType == String.class){
                        String s = String.valueOf(val.toString());
                        if(StringUtils.endsWith(s, ".0")){
                            val = StringUtils.substringBefore(s, ".0");
                        }else{
                            val = String.valueOf(val.toString());
                        }
                    }
                    else if (valType == Integer.class){
                        val = Double.valueOf(val.toString()).intValue();
                    }else if (valType == Long.class){
                        val = Double.valueOf(val.toString()).longValue();
                    }else if (valType == Double.class){
                        val = Double.valueOf(val.toString());
                    }else if (valType == Float.class){
                        val = Float.valueOf(val.toString());
                    }else if (valType == Date.class){
                        val = DateUtil.getJavaDate((Double)val);
                    }else{
                        if (ef.fieldType() != Class.class){
                            val = ef.fieldType().getMethod("getValue", String.class).invoke(null, val.toString());
                        }else{
//                            val = Class.forName(this.getClass().getName().replaceAll(this.getClass().getSimpleName(),
//                                    "fieldtype."+valType.getSimpleName()+"Type")).getMethod("getValue", String.class).invoke(null, val.toString());
                        }
                    }
                } catch (Exception ex) {
                   // log.info("Get cell value ["+i+","+column+"] error: " + ex.toString());
                    val = null;
                }
                // set entity value
                if (os[1] instanceof Field){
                    Reflections.invokeSetter(obj, ((Field)os[1]).getName(), val);
                }else if (os[1] instanceof Method){
                    String mthodName = ((Method)os[1]).getName();
                    if ("get".equals(mthodName.substring(0, 3))){
                        mthodName = "set"+ StringUtils.substringAfter(mthodName, "get");
                    }
                    Reflections.invokeMethod(obj, mthodName, new Class[] {valType}, new Object[] {val});
                }

                }
            }
            objectList.add(obj);
    //    }
    }

    /**
     * 功能:根据属性生成对应的set/get方法
     */
    private static String convertToMethodName(String attribute,Class<?> objClass,boolean isSet) {
        /** 通过正则表达式来匹配第一个字符 **/
        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(attribute);
        StringBuilder sb = new StringBuilder();
        /** 如果是set方法名称 **/
        if(isSet)
        {
            sb.append("set");
        }else{
            /** get方法名称 **/
            try {
                Field attributeField = objClass.getDeclaredField(attribute);
                /** 如果类型为boolean **/
                if(attributeField.getType() == boolean.class||attributeField.getType() == Boolean.class)
                {
                    sb.append("is");
                }else
                {
                    sb.append("get");
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        /** 针对以下划线开头的属性 **/
        if(attribute.charAt(0)!='_' && m.find())
        {
            sb.append(m.replaceFirst(m.group().toUpperCase()));
        }else{
            sb.append(attribute);
        }
        return sb.toString();
    }
}
