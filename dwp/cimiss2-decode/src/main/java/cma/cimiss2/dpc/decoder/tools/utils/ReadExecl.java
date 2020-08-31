package cma.cimiss2.dpc.decoder.tools.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import org.apache.poi.ss.usermodel.*;
import java.util.*;

import static org.apache.poi.ss.usermodel.CellType.*;

public class ReadExecl {

    /*public static ArrayList<ArrayList<String>> readExcelForST(File file) {
        String path = file.getPath();
        String fileType = file.getPath().substring(file.getPath().lastIndexOf(".") + 1);
        // return a list contains many list
        ArrayList<ArrayList<String>> lists = new ArrayList<ArrayList<String>>();
        //读取excel文件
        InputStream is = null;
        try {
            is = new FileInputStream(path);
            //获取工作薄
            Workbook wb = null;
            if (fileType.equals("xls")) {
                wb = new HSSFWorkbook(is);
            } else if (fileType.equals("xlsx")) {
                wb = new XSSFWorkbook(is);
            } else {
                return null;
            }

            //读取第一个工作页sheet
            Sheet sheet = wb.getSheetAt(0);
            //第一行为标题
            for (Row row : sheet) {
                ArrayList<String> list = new ArrayList<String>();
                for (Cell cell : row) {
                    //根据不同类型转化成字符串
                    cell.setCellType(STRING);
                    list.add(cell.getStringCellValue());
                }
                lists.add(list);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lists;
    }*/

    /*public static void main(String[] args){
        ReadExecl excelUtil = new ReadExecl();
        //读取excel数据
        File file = new File("D:\\2016年兴安盟土壤肥力及盐碱化验数据.xls");
        ArrayList<ArrayList<String>> result = excelUtil.readExcelToObj(file);
        for(ArrayList<String> list:result){
            System.out.println("输出："+list);
        }
    }*/
    /**
     * 读取excel数据
     * @param path
     */
    public static ArrayList<ArrayList<String>> readExcelToObj(File path) {

        Workbook wb = null;
        ArrayList<ArrayList<String>> result = null;
        try {
            wb = WorkbookFactory.create(path);
            result = readExcel(wb, 0, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 读取excel文件
     * @param wb
     * @param sheetIndex sheet页下标：从0开始
     * @param startReadLine 开始读取的行:从0开始
     * @param tailLine 去除最后读取的行
     */
    private static ArrayList<ArrayList<String>> readExcel(Workbook wb,int sheetIndex, int startReadLine, int tailLine) {
        Sheet sheet = wb.getSheetAt(sheetIndex);
        Row row = null;
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        for(int i=startReadLine; i<sheet.getLastRowNum()-tailLine+1; i++) {

            row = sheet.getRow(i);
            if (row!=null) {
                ArrayList<String> list = new ArrayList<String>();
                for (Cell c : row) {
                    String returnStr = "";
                    boolean isMerge = isMergedRegion(sheet, i, c.getColumnIndex());
                    //判断是否具有合并单元格
                    if (isMerge) {
                        String rs = getMergedRegionValue(sheet, row.getRowNum(), c.getColumnIndex());
                        returnStr = rs;
                    } else {
                        returnStr =c.toString();
                       /* if (c.getCellType() == STRING) {
                            returnStr = c.getStringCellValue();
                        } else if (c.getCellType() == NUMERIC) {
                            returnStr = String.valueOf(c.getNumericCellValue());
                        }*/
                    }
                    list.add(returnStr);
                }
                result.add(list);
            }
        }
        return result;
    }

    /**
     * 获取合并单元格的值
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    public static String getMergedRegionValue(Sheet sheet ,int row , int column){
        int sheetMergeCount = sheet.getNumMergedRegions();
        for(int i = 0 ; i < sheetMergeCount ; i++){
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();
            if(row >= firstRow && row <= lastRow){
                if(column >= firstColumn && column <= lastColumn){
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);
                    return fCell.toString() ;
                }
            }
        }
        return null ;
    }

    private static int CheckRowNull(Row hssfRow){
        int num = 0;
        Iterator<Cell> cellItr =hssfRow.iterator();
        while(cellItr.hasNext()){
            Cell c =cellItr.next();
            if(c.getCellType() ==BLANK){
                num++;
            }
        }
        return num;
    }

    /**
     * 判断合并了行
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    private static boolean isMergedRow(Sheet sheet,int row ,int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if(row == firstRow && row == lastRow){
                if(column >= firstColumn && column <= lastColumn){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断指定的单元格是否是合并单元格
     * @param sheet
     * @param row 行下标
     * @param column 列下标
     * @return
     */
    private static boolean isMergedRegion(Sheet sheet,int row ,int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if(row >= firstRow && row <= lastRow){
                if(column >= firstColumn && column <= lastColumn){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断sheet页中是否含有合并单元格
     * @param sheet
     * @return
     */
    private static boolean hasMerged(Sheet sheet) {
        return sheet.getNumMergedRegions() > 0 ? true : false;
    }

    /**
     * 合并单元格
     * @param sheet
     * @param firstRow 开始行
     * @param lastRow 结束行
     * @param firstCol 开始列
     * @param lastCol 结束列
     */
    private static void mergeRegion(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
    }

    /**
     * 获取单元格的值
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell){

        if(cell == null) return "";

        if(cell.getCellType() == STRING){

            return cell.getStringCellValue();

        }else if(cell.getCellType() == BOOLEAN){

            return String.valueOf(cell.getBooleanCellValue());

        }else if(cell.getCellType() == FORMULA){

            return cell.getCellFormula() ;

        }else if(cell.getCellType() == NUMERIC){

            return String.valueOf(cell.getNumericCellValue());

        }
        return "";
    }
    /**
     * 从excel读取内容
     */
    public static void readContent(String fileName)  {
        boolean isE2007 = false;    //判断是否是excel2007格式
        if(fileName.endsWith("xlsx"))
            isE2007 = true;
        try {
            InputStream input = new FileInputStream(fileName);  //建立输入流
            Workbook wb  = null;
            //根据文件格式(2003或者2007)来初始化
            if(isE2007)
                wb = new XSSFWorkbook(input);
            else
                wb = new HSSFWorkbook(input);
            Sheet sheet = wb.getSheetAt(0);     //获得第一个表单
            Iterator<Row> rows = sheet.rowIterator(); //获得第一个表单的迭代器
            while (rows.hasNext()) {
                Row row = rows.next();  //获得行数据
                System.out.println("Row #" + row.getRowNum());  //获得行号从0开始
                Iterator<Cell> cells = row.cellIterator();    //获得第一行的迭代器
                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    System.out.println("Cell #" + cell.getColumnIndex());
                    switch (cell.getCellType()) {   //根据cell中的类型来输出数据
                        case NUMERIC:
                            System.out.println(cell.getNumericCellValue());
                            break;
                        case STRING:
                            System.out.println(cell.getStringCellValue());
                            break;
                        case BOOLEAN:
                            System.out.println(cell.getBooleanCellValue());
                            break;
                        case FORMULA:
                            System.out.println(cell.getCellFormula());
                            break;
                        default:
                            System.out.println("unsuported sell type======="+cell.getCellType());
                            break;
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

 /**
 * 获取字符串指定字符个数
 * @param str
 * @param tag
 * @return
 */
public static int getCount(String str, String tag) {
        int index = 0;
        int count = 0;
        while ((index = str.indexOf(tag)) != -1 ) {
        str = str.substring(index + tag.length());
        count++;
        }
        return count;
        }


}
