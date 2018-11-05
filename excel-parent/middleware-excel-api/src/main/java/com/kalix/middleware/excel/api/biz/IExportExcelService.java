package com.kalix.middleware.excel.api.biz;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author fengweibin
 */
public interface IExportExcelService extends IExcelService {

//    public  void  init(String title, Class<?> cls, int type, int... groups);
//
//    public void initialize(String title, List<String> headerList);
//
//    public Row addRow();
//
//    public Map<String, CellStyle> createStyles(Workbook wb);
//
//    public Cell addCell(Row row, int column, Object val, int align, Class<?> fieldType);
//
//    public <E> void setDataList(List<E> list);
//
//    public void write(HttpServletResponse response, String fileName) throws IOException;
//
//   // public ExportExcel writeFile(String name) throws FileNotFoundException, IOException;
//
//    public void dispose();

    public void doExport(Map map, HttpServletResponse response) throws IOException;
}
