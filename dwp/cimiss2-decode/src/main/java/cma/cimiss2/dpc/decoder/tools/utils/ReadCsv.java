package cma.cimiss2.dpc.decoder.tools.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author WuYanJun
 * @date 2019/5/6 15:27
 */
public class ReadCsv {

    public static ArrayList<String[]> readCsv(byte[] data, String fileCode) throws UnsupportedEncodingException, FileNotFoundException, IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data), fileCode));
        String[] item = null;
        String line = null;
        ArrayList<String[]> strings = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            item = line.split(",");
            if (item.length != 1) {
                strings.add(item);
            }
        }
        strings.remove(0);
        return strings;
    }

    public static String readNote(File file) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"GBK"));
        String line = null;
        String message = new String();
        while ((line = reader.readLine()) != null) {
            message += line + "#";
        }
        return message;
    }

    public static ArrayList<String[]> readDat(File file) throws UnsupportedEncodingException, FileNotFoundException, IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String[] item = null;
        String line = null;
        ArrayList<String[]> strings = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            item = line.split(" ");
            List<String> list = new ArrayList<String>();
            if (item.length != 1) {
                for (int i = 0; i < item.length; i++) {
                    if (item[i] == null || "".equals(item[i].trim().toString())) {
                        continue;
                    } else {
                        list.add(item[i]);
                    }
                }
            }
            String[] newArray = new String[list.size()];
            for (int i = 0; i < newArray.length; i++) {
                newArray[i] = list.get(i);
            }
            strings.add(newArray);
        }
        return strings;
    }

    public static ArrayList<String[]> readFile(File file) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String[] item = null;
        String line = null;
        ArrayList<String[]> strings = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            item = line.split(" ");
            List<String> list = new ArrayList<String>();
            if (item.length != 1) {
                for (int i = 0; i < item.length; i++) {
                    if (item[i] == null || "".equals(item[i].trim())) {
                        continue;
                    } else {
                        list.add(item[i]);
                    }
                }
            }
            String[] newArray = new String[list.size()];
            for (int i = 0; i < newArray.length; i++) {
                newArray[i] = list.get(i);
            }
            strings.add(newArray);
        }
        return strings;
    }

    public static ArrayList<String[]> readTxt(File file) throws UnsupportedEncodingException, FileNotFoundException, IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String[] item = null;
        String line = null;
        ArrayList<String[]> strings = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            item = line.split(" ");
            List<String> list = new ArrayList<String>();
            if (item.length != 1) {
                for (int i = 0; i < item.length; i++) {
                    if (item[i] == null || "".equals(item[i].trim().toString())) {
                        continue;
                    } else {
                        if ("///".equals(item[i]) || "////".equals(item[i]) || "/////".equals(item[i])) {
                            list.add("0");
                        } else {
                            list.add(item[i]);
                        }
                    }
                }
            }
            String[] newArray = new String[list.size()];
            for (int i = 0; i < newArray.length; i++) {
                newArray[i] = list.get(i);
            }
            strings.add(newArray);
        }
        return strings;
    }


    public static ArrayList<String[]> readCdp(File file) throws UnsupportedEncodingException, FileNotFoundException, IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String[] item = null;
        String line = null;
        ArrayList<String[]> strings = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            item = line.split(",");
            if (item.length != 1) {
                strings.add(item);
            }
        }
        return strings;
    }


    public static ArrayList<ArrayList<String>> readExcel(File excel) {
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        try {
            if (excel.isFile() && excel.exists()) {   //判断文件是否存在

                String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
                Workbook wb = null;
                //根据文件后缀（xls/xlsx）进行判断
                if ("xls".equals(split[1])) {
                    FileInputStream fis = new FileInputStream(excel);   //文件流对象
                    wb = new HSSFWorkbook(fis);
                } else if ("xlsx".equals(split[1])) {
                    wb = new XSSFWorkbook(excel);
                } else {
                    System.out.println("文件类型错误!");
                }

                //开始解析
                Sheet sheet = wb.getSheetAt(0);     //读取sheet 0

                int firstRowIndex = sheet.getFirstRowNum() + 1;   //第一行是列名，所以不读
                int lastRowIndex = sheet.getLastRowNum();
                System.out.println("firstRowIndex: " + firstRowIndex);
                System.out.println("lastRowIndex: " + lastRowIndex);

                for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
                    Row row = sheet.getRow(rIndex);
                    if (row != null) {
                        int firstCellIndex = row.getFirstCellNum();
                        int lastCellIndex = row.getLastCellNum();
                        ArrayList<String> strings = new ArrayList<>();
                        for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {//遍历列
                            Cell cell = row.getCell(cIndex);
                            if (cell != null) {
                                String newName = new String(cell.toString().getBytes(), "UTF-8");
                                if (cIndex == 0) {
                                    //用于转化为日期格式
                                    Date d = cell.getDateCellValue();
                                    DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    newName = formater.format(d);
                                } else if (cIndex == 1) {
                                    DecimalFormat df = new DecimalFormat("0");
                                    newName = df.format(cell.getNumericCellValue());
                                }
                                strings.add(newName);
                            } else {
                                strings.add("");
                            }
                        }
                        list.add(strings);
                    }
                }
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<ArrayList<String>> readExcelForSend(File excel) {
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        try {
            if (excel.isFile() && excel.exists()) {   //判断文件是否存在

                String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
                Workbook wb = null;
                //根据文件后缀（xls/xlsx）进行判断
                if ("xls".equals(split[1])) {
                    FileInputStream fis = new FileInputStream(excel);   //文件流对象
                    wb = new HSSFWorkbook(fis);
                } else if ("xlsx".equals(split[1])) {
                    wb = new XSSFWorkbook(excel);
                } else {
                    System.out.println("文件类型错误!");
                }

                //开始解析
                Sheet sheet = wb.getSheetAt(0);     //读取sheet 0

                int firstRowIndex = sheet.getFirstRowNum() + 1;   //第一行是列名，所以不读
                int lastRowIndex = 47;
                System.out.println("firstRowIndex: " + firstRowIndex);
                System.out.println("lastRowIndex: " + lastRowIndex);

                for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
                    Row row = sheet.getRow(rIndex);
                    if (row != null) {
                        int firstCellIndex = row.getFirstCellNum();
                        int lastCellIndex = row.getLastCellNum();
                        ArrayList<String> strings = new ArrayList<>();
                        for (int cIndex = firstCellIndex; cIndex < 18; cIndex++) {//遍历列
                            Cell cell = row.getCell(cIndex);
                            if (cell != null) {
                                String newName = new String(cell.toString().getBytes(), "UTF-8");
                                if (cIndex == 2 || cIndex == 16) {
                                    //用于转化为日期格式
                                    Date d = cell.getDateCellValue();
                                    DateFormat formater = new SimpleDateFormat("yyyy-M-d");
                                    newName = formater.format(d);
                                }
                                strings.add(newName);
                            } else {
                                strings.add("");
                            }
                        }
                        list.add(strings);
                    }else{
                        break;
                    }
                }
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<ArrayList<String>> readExcelForWind(File excel) {
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        try {
            if (excel.isFile() && excel.exists()) {   //判断文件是否存在

                String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
                Workbook wb = null;
                //根据文件后缀（xls/xlsx）进行判断
                if ("xls".equals(split[1])) {
                    FileInputStream fis = new FileInputStream(excel);   //文件流对象
                    wb = new HSSFWorkbook(fis);
                } else if ("xlsx".equals(split[1])) {
                    wb = new XSSFWorkbook(excel);
                } else {
                    System.out.println("文件类型错误!");
                }

                //开始解析
                Sheet sheet = wb.getSheetAt(0);     //读取sheet 0

                int firstRowIndex = sheet.getFirstRowNum() + 1;   //第一行是列名，所以不读
                int lastRowIndex = sheet.getLastRowNum();
                System.out.println("firstRowIndex: " + firstRowIndex);
                System.out.println("lastRowIndex: " + lastRowIndex);

                for (int rIndex = firstRowIndex; rIndex <= 19; rIndex++) {   //遍历行
                    Row row = sheet.getRow(rIndex);
                    if (row != null) {
                        int firstCellIndex = row.getFirstCellNum();
                        int lastCellIndex = row.getLastCellNum();
                        ArrayList<String> strings = new ArrayList<>();
                        for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {//遍历列
                            Cell cell = row.getCell(cIndex);
                            if (cell != null) {
                                String newName = new String(cell.toString().getBytes(), "UTF-8");
                                if (cIndex == 2 || cIndex == 8) {
                                    //用于转化为日期格式
                                    Date d = cell.getDateCellValue();
                                    DateFormat formater = new SimpleDateFormat("yyyy-M-d");
                                    newName = formater.format(d);
                                }
                                strings.add(newName);
                            } else {
                                strings.add("");
                            }
                        }
                        list.add(strings);
                    }
                }
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<ArrayList<String>> readExcelForPress(File excel) {
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        try {
            if (excel.isFile() && excel.exists()) {   //判断文件是否存在

                String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
                Workbook wb = null;
                //根据文件后缀（xls/xlsx）进行判断
                if ("xls".equals(split[1])) {
                    FileInputStream fis = new FileInputStream(excel);   //文件流对象
                    wb = new HSSFWorkbook(fis);
                } else if ("xlsx".equals(split[1])) {
                    FileInputStream fis = new FileInputStream(excel);   //文件流对象
                    wb = new XSSFWorkbook(fis);
                } else {
                    System.out.println("文件类型错误!");
                }

                //开始解析
                Sheet sheet = wb.getSheetAt(0);     //读取sheet 0

                int firstRowIndex = sheet.getFirstRowNum() + 1;   //第一行是列名，所以不读
                int lastRowIndex = sheet.getLastRowNum();
                System.out.println("firstRowIndex: " + firstRowIndex);
                System.out.println("lastRowIndex: " + lastRowIndex);

                for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
                    Row row = sheet.getRow(rIndex);
                    if (row != null) {
                        int firstCellIndex = row.getFirstCellNum();
                        int lastCellIndex = row.getLastCellNum();
                        ArrayList<String> strings = new ArrayList<>();
                        for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {//遍历列
                            Cell cell = row.getCell(cIndex);
                            if (cell != null) {
                                strings.add(cell.toString());
                            } else {
                                strings.add("");
                            }
                        }
                        list.add(strings);
                    }
                }
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<ArrayList<String>> readExcelForMul(File excel) {
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        try {
            if (excel.isFile() && excel.exists()) {   //判断文件是否存在

                String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
                Workbook wb = null;
                //根据文件后缀（xls/xlsx）进行判断
                if ("xls".equals(split[1])) {
                    FileInputStream fis = new FileInputStream(excel);   //文件流对象
                    wb = new HSSFWorkbook(fis);
                } else if ("xlsx".equals(split[1])) {
                    FileInputStream fis = new FileInputStream(excel);   //文件流对象
                    wb = new XSSFWorkbook(fis);
                } else {
                    System.out.println("文件类型错误!");
                }

                //开始解析
                Sheet sheet = wb.getSheetAt(0);     //读取sheet 0

                int firstRowIndex = sheet.getFirstRowNum() + 1;   //第一行是列名，所以不读
                int lastRowIndex = sheet.getLastRowNum();
                System.out.println("firstRowIndex: " + firstRowIndex);
                System.out.println("lastRowIndex: " + lastRowIndex);

                for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
                    Row row = sheet.getRow(rIndex);
                    if (row != null) {
                        int firstCellIndex = row.getFirstCellNum();
                        int lastCellIndex = row.getLastCellNum();
                        ArrayList<String> strings = new ArrayList<>();
                        for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {//遍历列
                            Cell cell = row.getCell(cIndex);
                            if (cell != null) {
                                String newName = new String(cell.toString().getBytes(), "UTF-8");
                                if (cIndex == 1 || cIndex == 4 || cIndex == 7 || cIndex == 9 || cIndex == 12 || cIndex == 14 || cIndex == 17 || cIndex == 19
                                        || cIndex == 26 || cIndex == 31 || cIndex == 36 || cIndex == 40 || cIndex == 45 || cIndex == 47 || cIndex == 50 || cIndex == 52
                                        || cIndex == 55 || cIndex == 57 || cIndex == 66 || cIndex == 75) {
                                    //用于转化为日期格式
                                    Date d = cell.getDateCellValue();
                                    DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    newName = formater.format(d);
                                }
                                strings.add(newName);
                            } else {
                                strings.add("0.0");
                            }
                        }
                        list.add(strings);
                    }
                }
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<ArrayList<String>> readExcelForZsl(File excel) {
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        try {
            if (excel.isFile() && excel.exists()) {   //判断文件是否存在

                String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
                Workbook wb = null;
                //根据文件后缀（xls/xlsx）进行判断
                if ("xls".equals(split[1])) {
                    FileInputStream fis = new FileInputStream(excel);   //文件流对象
                    wb = new HSSFWorkbook(fis);
                } else if ("xlsx".equals(split[1])) {
                    wb = new XSSFWorkbook(excel);
                } else {
                    System.out.println("文件类型错误!");
                }

                //开始解析
                Sheet sheet = wb.getSheetAt(0);     //读取sheet 0

                int firstRowIndex = sheet.getFirstRowNum() + 1;   //第一行是列名，所以不读
                int lastRowIndex = sheet.getLastRowNum();
                System.out.println("firstRowIndex: " + firstRowIndex);
                System.out.println("lastRowIndex: " + lastRowIndex);

                for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
                    Row row = sheet.getRow(rIndex);
                    if (row != null) {
                        int firstCellIndex = row.getFirstCellNum();
                        int lastCellIndex = row.getLastCellNum();
                        ArrayList<String> strings = new ArrayList<>();
                        for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {//遍历列
                            Cell cell = row.getCell(cIndex);
                            if (cell != null) {
                                String newName = new String(cell.toString().getBytes(), "UTF-8");
                                if (cIndex == 2 || cIndex == 14) {
                                    //用于转化为日期格式
                                    Date d = cell.getDateCellValue();
                                    DateFormat formater = new SimpleDateFormat("yyyy-M-d");
                                    newName = formater.format(d);
                                }
                                strings.add(newName);
                            } else {
                                strings.add("0");
                            }
                        }
                        list.add(strings);
                    }
                }
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<ArrayList<String>> readExcelForGress(File excel) {
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        try {
            if (excel.isFile() && excel.exists()) {   //判断文件是否存在

                String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
                Workbook wb = null;
                //根据文件后缀（xls/xlsx）进行判断
                if ("xls".equals(split[1])) {
                    FileInputStream fis = new FileInputStream(excel);   //文件流对象
                    wb = new HSSFWorkbook(fis);
                } else if ("xlsx".equals(split[1])) {
                    wb = new XSSFWorkbook(excel);
                } else {
                    System.out.println("文件类型错误!");
                }

                //开始解析
                Sheet sheet = wb.getSheetAt(0);  //读取sheet 0

                int firstRowIndex = sheet.getFirstRowNum() + 1;   //第一行是列名，所以不读
                int lastRowIndex = sheet.getLastRowNum();
                DateFormat formater = new SimpleDateFormat("yyyy-M-d");
                System.out.println("firstRowIndex: " + firstRowIndex);
                System.out.println("lastRowIndex: " + lastRowIndex);

                for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
                    Row row = sheet.getRow(rIndex);
                    if (row != null) {
                        int firstCellIndex = row.getFirstCellNum();
                        int lastCellIndex = row.getLastCellNum();
                        ArrayList<String> strings = new ArrayList<>();
                        for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {//遍历列
                            Cell cell = row.getCell(cIndex);
                            if (cell != null) {
                                String newName = new String(cell.toString().getBytes(), "UTF-8");
                                if (cIndex == 1 || cIndex == 5) {
                                    //用于转化为日期格式
                                    newName = formater.format(cell.getDateCellValue().getTime());
                                }
                                strings.add(newName);
                            } else {
                                strings.add("");
                            }
                        }
                        list.add(strings);
                    }
                }
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<ArrayList<String>> readExcelForBurn(File excel) {
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        try {
            if (excel.isFile() && excel.exists()) {   //判断文件是否存在

                String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
                Workbook wb = null;
                //根据文件后缀（xls/xlsx）进行判断
                if ("xls".equals(split[1])) {
                    FileInputStream fis = new FileInputStream(excel);   //文件流对象
                    wb = new HSSFWorkbook(fis);
                } else if ("xlsx".equals(split[1])) {
                    wb = new XSSFWorkbook(excel);
                } else {
                    System.out.println("文件类型错误!");
                }

                //开始解析
                Sheet sheet = wb.getSheetAt(0);  //读取sheet 0

                int firstRowIndex = sheet.getFirstRowNum() + 1;   //第一行是列名，所以不读
                int lastRowIndex = sheet.getLastRowNum();
                DateFormat formater = new SimpleDateFormat("yyyy-M-d");
                System.out.println("firstRowIndex: " + firstRowIndex);
                System.out.println("lastRowIndex: " + lastRowIndex);
                DecimalFormat df = new DecimalFormat("0");
                DecimalFormat dff = new DecimalFormat("0.0");
                for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
                    Row row = sheet.getRow(rIndex);
                    if (row != null) {
                        int firstCellIndex = row.getFirstCellNum();
                        int lastCellIndex = row.getLastCellNum();
                        ArrayList<String> strings = new ArrayList<>();
                        for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {//遍历列
                            Cell cell = row.getCell(cIndex);
                            if (cell != null) {
                                /*if (cell.getCellType() == CellType.NUMERIC) {
                                    strings.add(df.format(cell.getNumericCellValue()));
                                } else if (cell.getCellType() == CellType.FORMULA) {
                                    strings.add(dff.format(Double.parseDouble(String.valueOf((cell.getNumericCellValue())))));
                                } else */if (cIndex == 2 || cIndex == 18) {
                                    String newName = new String(cell.toString().getBytes(), "UTF-8");
                                    //用于转化为日期格式
                                    strings.add(formater.format(cell.getDateCellValue().getTime()));
                                } else {
                                    strings.add(cell.toString());
                                }
                            }else{
                                strings.add("");
                            }
                        }
                        list.add(strings);
                    }
                }
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
}

        public static ArrayList<ArrayList<String>> readExcelForXls(File excel){
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        try{
            if (excel.isFile() && excel.exists()) {   //判断文件是否存在

                String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
                Workbook wb = null;
                //根据文件后缀（xls/xlsx）进行判断
                if ("xls".equals(split[1])) {
                    FileInputStream fis = new FileInputStream(excel);   //文件流对象
                    wb = new HSSFWorkbook(fis);
                } else if ("xlsx".equals(split[1])) {
                    wb = new XSSFWorkbook(excel);
                } else {
                    System.out.println("文件类型错误!");
                }

                //开始解析
                Sheet sheet = wb.getSheetAt(0);  //读取sheet 0

                int firstRowIndex = sheet.getFirstRowNum() + 1;   //第一行是列名，所以不读
                int lastRowIndex = sheet.getLastRowNum();
                System.out.println("firstRowIndex: " + firstRowIndex);
                System.out.println("lastRowIndex: " + lastRowIndex);
                DecimalFormat df = new DecimalFormat("0.00");
                for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
                    Row row = sheet.getRow(rIndex);
                    if (row != null) {
                        int firstCellIndex = row.getFirstCellNum();
                        int lastCellIndex = row.getLastCellNum();
                        ArrayList<String> strings = new ArrayList<>();
                        for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {//遍历列
                            Cell cell = row.getCell(cIndex);
                            if (cell != null&&!cell.toString().equals("")) {
                                /*if( cell.getCellType()== CellType.NUMERIC){
                                    strings.add(df.format(cell.getNumericCellValue()));
                                }else if(cell.getCellType()==CellType.FORMULA){
                                    strings.add(df.format(Double.parseDouble(String.valueOf((cell.getNumericCellValue())))));
                                }else{*/
                                    strings.add(cell.toString());
                                }else{
                                strings.add("");
                            }
                        }
                        if(strings.size()!=0){
                            list.add(strings);
                        }
                    }
                }
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<ArrayList<String>> readExcelForAr(File excel){
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        try{
            if (excel.isFile() && excel.exists()) {   //判断文件是否存在

                String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
                Workbook wb = null;
                //根据文件后缀（xls/xlsx）进行判断
                if ("xls".equals(split[1])) {
                    FileInputStream fis = new FileInputStream(excel);   //文件流对象
                    wb = new HSSFWorkbook(fis);
                } else if ("xlsx".equals(split[1])) {
                    wb = new XSSFWorkbook(excel);
                } else {
                    System.out.println("文件类型错误!");
                }

                //开始解析
                Sheet sheet = wb.getSheetAt(0);  //读取sheet 0

                int firstRowIndex = sheet.getFirstRowNum() + 1;   //第一行是列名，所以不读
                int lastRowIndex = sheet.getLastRowNum();
                System.out.println("firstRowIndex: " + firstRowIndex);
                System.out.println("lastRowIndex: " + lastRowIndex);
                DecimalFormat df = new DecimalFormat("0");
                for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
                    Row row = sheet.getRow(rIndex);
                    if (row != null) {
                        int firstCellIndex = row.getFirstCellNum();
                        int lastCellIndex = row.getLastCellNum();
                        ArrayList<String> strings = new ArrayList<>();
                        for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {//遍历列
                            Cell cell = row.getCell(cIndex);
                            if (cell != null&&!cell.toString().equals("")) {
                                /*if( cell.getCellType()== CellType.NUMERIC){
                                    strings.add(df.format(cell.getNumericCellValue()));
                                }else if(cell.getCellType()==CellType.FORMULA){
                                    strings.add(df.format(Double.parseDouble(String.valueOf((cell.getNumericCellValue())))));
                                }else{*/
                                    strings.add(cell.toString());
                                //}
                            }
                        }
                        if(strings.size()!=0){
                            list.add(strings);
                        }
                    }
                }
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<ArrayList<String>> readExcelForArten(File excel){
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        try{
            if (excel.isFile() && excel.exists()) {   //判断文件是否存在

                String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
                Workbook wb = null;
                //根据文件后缀（xls/xlsx）进行判断
                if ("xls".equals(split[1])) {
                    FileInputStream fis = new FileInputStream(excel);   //文件流对象
                    wb = new HSSFWorkbook(fis);
                } else if ("xlsx".equals(split[1])) {
                    wb = new XSSFWorkbook(excel);
                } else {
                    System.out.println("文件类型错误!");
                }

                //开始解析
                Sheet sheet = wb.getSheetAt(0);  //读取sheet 0

                int firstRowIndex = sheet.getFirstRowNum() + 3;   //第一行是列名，所以不读
                int lastRowIndex = sheet.getLastRowNum();
                System.out.println("firstRowIndex: " + firstRowIndex);
                System.out.println("lastRowIndex: " + lastRowIndex);
                DecimalFormat df = new DecimalFormat("0");
                for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
                    Row row = sheet.getRow(rIndex);
                    if (row != null) {
                        int firstCellIndex = row.getFirstCellNum();
                        int lastCellIndex = row.getLastCellNum();
                        ArrayList<String> strings = new ArrayList<>();
                        for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {//遍历列
                            Cell cell = row.getCell(cIndex);
                            if (cell != null&&!cell.toString().equals("")) {
                                if( cell.getCellType()== CellType.NUMERIC){
                                    strings.add(df.format(cell.getNumericCellValue()));
                                }else if(cell.getCellType()==CellType.FORMULA){
                                    strings.add(df.format(Double.parseDouble(String.valueOf((cell.getNumericCellValue())))));
                                }else if ("xx".equals(cell.toString())||" BD".equals(cell.toString())){
                                    strings.add("0");
                                }else{
                                    strings.add(cell.toString());
                                }
                            }else{
                                strings.add("0");
                            }
                        }
                        if(strings.size()!=0){
                            list.add(strings);
                        }
                    }
                }
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<ArrayList<String>> readExcelForArmon(File excel){
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        try{
            if (excel.isFile() && excel.exists()) {   //判断文件是否存在

                String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
                Workbook wb = null;
                //根据文件后缀（xls/xlsx）进行判断
                if ("xls".equals(split[1])) {
                    FileInputStream fis = new FileInputStream(excel);   //文件流对象
                    wb = new HSSFWorkbook(fis);
                } else if ("xlsx".equals(split[1])) {
                    wb = new XSSFWorkbook(excel);
                } else {
                    System.out.println("文件类型错误!");
                }

                //开始解析
                Sheet sheet = wb.getSheetAt(0);  //读取sheet 0

                int firstRowIndex = sheet.getFirstRowNum() + 1;   //第一行是列名，所以不读
                int lastRowIndex = sheet.getLastRowNum();
                System.out.println("firstRowIndex: " + firstRowIndex);
                System.out.println("lastRowIndex: " + lastRowIndex);
                for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
                    Row row = sheet.getRow(rIndex);
                    if (row != null) {
                        int firstCellIndex = row.getFirstCellNum();
                        int lastCellIndex = row.getLastCellNum();
                        ArrayList<String> strings = new ArrayList<>();
                        for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {//遍历列
                            Cell cell = row.getCell(cIndex);
                            if (cell != null&&!cell.toString().equals("")) {
                                /*if( cell.getCellType()== CellType.NUMERIC){
                                    double d = cell.getNumericCellValue();
                                    NumberFormat nf = NumberFormat.getInstance();
                                    String s = nf.format(d);
                                    strings.add(s);
                                }else{*/
                                    strings.add(cell.toString());
                                //}
                            }
                        }
                        if(strings.size()!=0){
                            list.add(strings);
                        }
                    }
                }
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }


    public static ArrayList<ArrayList<String>> readExcelForSoil(File excel){
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        try{
            if (excel.isFile() && excel.exists()) {   //判断文件是否存在

                String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
                Workbook wb = null;
                //根据文件后缀（xls/xlsx）进行判断
                if ("xls".equals(split[1])) {
                    FileInputStream fis = new FileInputStream(excel);   //文件流对象
                    wb = new HSSFWorkbook(fis);
                } else if ("xlsx".equals(split[1])) {
                    wb = new XSSFWorkbook(excel);
                } else {
                    System.out.println("文件类型错误!");
                }

                //开始解析
                Sheet sheet = wb.getSheetAt(0);  //读取sheet 0

                int firstRowIndex = sheet.getFirstRowNum() + 1;   //第一行是列名，所以不读
                int lastRowIndex = sheet.getLastRowNum();
                System.out.println("firstRowIndex: " + firstRowIndex);
                System.out.println("lastRowIndex: " + lastRowIndex);
                DateFormat formater = new SimpleDateFormat("yyyy-M-d");
                for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
                    Row row = sheet.getRow(rIndex);
                    if (row != null) {
                        int firstCellIndex = row.getFirstCellNum();
                        int lastCellIndex = row.getLastCellNum();
                        ArrayList<String> strings = new ArrayList<>();
                        for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {//遍历列
                            Cell cell = row.getCell(cIndex);
                            if (cell != null) {
                                String newName=new String(cell.toString().getBytes(),"UTF-8");
                                if( cell.getCellType()== CellType.NUMERIC) {
                                    if(cIndex==2) {
                                        //用于转化为日期格式
                                        newName = formater.format(cell.getDateCellValue().getTime());
                                        strings.add(newName);
                                    }else{
                                        double d = cell.getNumericCellValue();
                                        NumberFormat nf = NumberFormat.getInstance();
                                        String s = nf.format(d);
                                        strings.add(s);
                                    }
                                }else{
                                    strings.add(cell.toString());
                                }
                            }else{
                                strings.add("");
                            }
                        }
                        if(strings.size()!=0){
                            list.add(strings);
                        }
                    }
                }
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
    public static byte[] getBytesByFile(String filePath) {
        try {
            File file=new File(filePath);
            //获取输入流
            FileInputStream fis = new FileInputStream(file);

            //新的 byte 数组输出流，缓冲区容量1024byte
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
            //缓存
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            //改变为byte[]
            byte[] data = bos.toByteArray();
            //
            bos.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 经纬度转换 ，度分秒转度
     * @param
     * @author Cai_YF
     * @return
     */
    public static String Dms2D(String jwd){
        if(jwd!=null&&(jwd.contains("°"))){//如果不为空并且存在度单位
            DecimalFormat df = new DecimalFormat("0.0000");
            if(jwd.contains("″")){
                String[] str = jwd.split("°");
                String[] str1 = str[1].split("′");
                String[] str2 = str1[1].split("");
                jwd = df.format(Double.parseDouble(str[0])+Double.parseDouble(str1[0])/30+Double.parseDouble(str2[0])/3600);
            }else if(!jwd.contains("″")){
                String[] str = jwd.split("°");
                 str[1]= str[1].replace("′","");
                jwd = df.format(Double.parseDouble(str[0])+Double.parseDouble(str[1])/30);
            }
        }
        return jwd;
    }

    public static String getOrderNo(){
        String orderNo = "" ; String trandNo = String.valueOf((Math.random() * 9 + 1) * 1000000); String sdf = new SimpleDateFormat("yyyyMMddHHMMSS").format(new Date()); orderNo = trandNo.toString().substring(0, 4); orderNo = orderNo + sdf ; return orderNo ; }

}


