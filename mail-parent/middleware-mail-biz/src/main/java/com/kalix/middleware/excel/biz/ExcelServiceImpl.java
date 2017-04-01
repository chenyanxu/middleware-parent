package com.kalix.middleware.excel.biz;

import com.kalix.framework.core.util.SerializeUtil;
import com.kalix.middleware.couchdb.api.biz.ICouchdbService;
import com.kalix.middleware.excel.api.biz.IExcelService;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.lightcouch.Response;

import java.io.*;
import java.util.*;

/**
 * @author chenyanxu
 */
public class ExcelServiceImpl implements IExcelService {
    private ICouchdbService couchdbService;

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
    public List<String> GetColumnNames(Object sheet, int columnRowIndex) {
        List<String> rtnList = new ArrayList<String>();
        Sheet theSheet = (Sheet) sheet;
        Row row = theSheet.getRow(columnRowIndex);

        for (int colIndex = 0; colIndex < row.getLastCellNum(); ++colIndex) {
            rtnList.add(row.getCell(colIndex).getStringCellValue().trim());
        }

        return rtnList;
    }

    @Override
    public Map<String, Integer> GetColumnDic(Object sheet, int columnRowIndex, Dictionary<String, Object> dic) {

        List<String> colNameList = GetColumnNames(sheet, columnRowIndex);
        List<String> configKeys = new ArrayList<String>();
        List<String> configValues = new ArrayList<String>();
        Enumeration<String> keys = dic.keys();
        Enumeration<Object> elements = dic.elements();
        String temp = null;
        Map<String, Integer> columnMap = new HashMap<>();

        while (keys.hasMoreElements()) {
            temp = keys.nextElement();
            configKeys.add(temp.trim());
        }

        while (elements.hasMoreElements()) {
            temp = (String) elements.nextElement();
            configValues.add(temp.trim());
        }

        for (int idx = 0; idx < colNameList.size(); ++idx) {
            for (int configIndex = 0; configIndex < configKeys.size(); ++configIndex) {
                if (colNameList.get(idx).equals(configValues.get(configIndex))) {
                    columnMap.put(configKeys.get(configIndex), idx);


                }
            }
        }


        return columnMap;
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
}
