package cma.cimiss2.dpc.decoder.tools.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author ywj
 * @title: ExcelUtil
 * @projectName da_service
 * @description: excel
 * @date 2019/5/6 15:13
 */
public class ExcelUtil {

    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";

    /**
     * 判断Excel的版本,获取Workbook
     * @param file
     * @return
     * @throws IOException
     */
    public static Workbook getWorkbok(FileInputStream in, File file) throws IOException{
        Workbook wb = null;
        //Excel&nbsp;2003
        if(file.getName().endsWith(EXCEL_XLS)){
            wb = new HSSFWorkbook(in);
            // Excel 2007/2010
        }else if(file.getName().endsWith(EXCEL_XLSX)){
            wb = new XSSFWorkbook(in);
        }
        return wb;
    }

    /**
     * 判断Excel的版本,获取Workbook
     * @param fileName
     * @return
     * @throws IOException
     */
    public static Workbook getWorkbokByFilename(ByteArrayInputStream in, String fileName) throws IOException{
        Workbook wb = null;
        //Excel&nbsp;2003
        if(fileName.endsWith(EXCEL_XLS)){
            wb = new HSSFWorkbook(in);
            // Excel 2007/2010
        }else if(fileName.endsWith(EXCEL_XLSX)){
            wb = new XSSFWorkbook(in);
        }
        return wb;
    }

    /**
     * 获取单元格数据
     * @param row 行
     * @param column 列
     * @return 单元格数据
     */
    public static String getCell(Row row, int column){
        //赋值 列
        String cellValue = null;
        Cell cell = row.getCell(column);
        if(cell != null){
            switch (cell.getCellTypeEnum()){
                case STRING:
                    cellValue = cell.getStringCellValue();
                    break;
                case NUMERIC:
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                case BOOLEAN:
                    cellValue = String.valueOf(cell.getBooleanCellValue());
                    break;
                case BLANK:
                    cellValue = null;
                    break;
                case FORMULA:
                    cellValue = String.valueOf(cell.getCellFormula());
                    break;
                case ERROR:
                    cellValue = "非法字符";
                    break;
                default:
                    cellValue = "未知类型";
                    break;
            }
        }
        return cellValue;
    }

    /**
     * 字符串转Double
     * @param s
     * @return
     */
    public static Double parseDouble(String s) {
        if(null == s || "".equals(s) ) {
            return null;
        }else {
            return Double.parseDouble(s);
        }
    }

    /**
     * 字符串转Integer
     * @param s
     * @return
     */
    public static Integer parseInt(String s) {
        if(null == s || "".equals(s) ) {
            return null;
        }else {
            return Integer.parseInt(s.split("\\.")[0]);
        }
    }
}
