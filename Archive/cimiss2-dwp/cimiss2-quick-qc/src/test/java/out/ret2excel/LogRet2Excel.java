package out.ret2excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.List;
import java.util.Objects;

import static cma.cimiss2.dpc.quickqc.Util.TEST_FAIL;
import static cma.cimiss2.dpc.quickqc.Util.TEST_OK;

/**
 * 将日志数据导出到Excel
 * 自动生成测试结果
 *
 * @Author: When6passBye
 * @Date: 2019-08-28 15:02
 **/
public class LogRet2Excel {

    /**
     * 表头
     */
    private static String[] HEADS;
    /**
     * excel
     */
    private static HSSFWorkbook WORKBOOK;
    /**
     * 黄色样式
     */
    private static HSSFCellStyle STYLE_YELLOW;
    /**
     * 橘色样式
     */
    private static HSSFCellStyle STYLE_ORANGE;
    /**
     * 正常样式
     */
    private static HSSFCellStyle STYLE_NORMAL;

    static {
        HEADS = new String[]{"序号", "区站号", "观测时次（UTC）", "要素编码", "要素值", "缺测检查质控码", "界限值质控码", "范围值质控码", "内部一致性质控码", "文件级质控码", "对比结果", "测试文件", "花费时间(ms)"};
    }

    /**
     * 写excel表头
     *
     * @param sheet : 当前sheet
     * @return : void
     * @author : When6passBye
     * @date : 2019/9/3 2:35 PM
     */
    private static void writeHead(HSSFSheet sheet) {
        Row tHead = sheet.createRow(0);

        for (int i = 0; i < HEADS.length; i++) {
            sheet.autoSizeColumn(i);
            Cell cell = tHead.createCell(i);
            cell.setCellValue(HEADS[i]);
            cell.setCellStyle(STYLE_NORMAL);
            //自适应
            int length = HEADS[i].getBytes().length;
            if (i == 11) {
                sheet.setColumnWidth(i, length * 1280);
            } else if (i >= 5 && i <= 9 || i == 12) {
                sheet.setColumnWidth(i, length * 256);
            } else {
                sheet.setColumnWidth(i, length * 512);
            }
        }

    }

    /**
     * 写结果行
     *
     * @param sheet  :当前sheet
     * @param row    :第几行
     * @param bean   :数据bean
     * @param preRow :期望行（用于取期望sheet对应行的数据）
     * @return : void
     * @author : When6passBye
     * @date : 2019/9/3 2:28 PM
     */
    private static void writeResultRow(HSSFSheet sheet, int row, ResultBean bean, HSSFRow preRow) {
        Row noRow = sheet.createRow(row);
        String[] strValues = bean.getStrValues();
        boolean isTestOk = true;
        for (int i = 0; i < strValues.length; i++) {
            Cell cell = noRow.createCell(i);
            String strValue = strValues[i];

            HSSFCell preCell = preRow.getCell(i);
            String preValue = preCell.getStringCellValue();
            //不一样的地方设置黄色
            if (!strValue.equals(preValue)) {
                if(i!=12&&i!=10){
                    isTestOk = false;
                }
                cell.setCellStyle(STYLE_ORANGE);
            } else {
                cell.setCellStyle(STYLE_YELLOW);
            }
            if (i == 10) {
                if (isTestOk) {
                    strValue=TEST_OK;
                } else {
                    strValue=TEST_FAIL;
                }
            }
            cell.setCellValue(strValue);
        }

    }

    /**
     * 写期望行，就是将期望sheet每一行复制过来
     *
     * @param nowSheet : 当前sheet
     * @param index    : 行标
     * @param preRow   : 期望行（用于取期望sheet对应行的数据）
     * @return : void
     * @author : When6passBye
     * @date : 2019/9/3 2:37 PM
     */
    private static void writeExpectRow(HSSFSheet nowSheet, int index, HSSFRow preRow) {
        Row nowRow = nowSheet.createRow(index);
        int cellNum = preRow.getLastCellNum();
        for (int i = 0; i < cellNum; i++) {
            Cell preCell = preRow.getCell(i);
            Cell nowCell = nowRow.createCell(i);
            String cellValue = preCell.getStringCellValue();
            if(i==10){
                cellValue="";
            }
            nowCell.setCellValue(cellValue);
            nowCell.setCellStyle(STYLE_NORMAL);
        }
    }


    /**
     * 初始化单元格样式
     *
     * @return : void
     * @author : When6passBye
     * @date : 2019/9/3 2:38 PM
     */
    private static void initStyle() {
        STYLE_ORANGE = WORKBOOK.createCellStyle();
        STYLE_ORANGE.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
        STYLE_ORANGE.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        STYLE_ORANGE.setBorderBottom(BorderStyle.THIN); //下边框
        STYLE_ORANGE.setBorderLeft(BorderStyle.THIN);//左边框
        STYLE_ORANGE.setBorderTop(BorderStyle.THIN);//上边框
        STYLE_ORANGE.setBorderRight(BorderStyle.THIN);//右边框
        STYLE_ORANGE.setAlignment(HorizontalAlignment.CENTER); // 水平居中
        STYLE_ORANGE.setVerticalAlignment(VerticalAlignment.CENTER); // 垂直居中

        STYLE_YELLOW = WORKBOOK.createCellStyle();
        STYLE_YELLOW.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        STYLE_YELLOW.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        STYLE_YELLOW.setBorderBottom(BorderStyle.THIN); //下边框
        STYLE_YELLOW.setBorderLeft(BorderStyle.THIN);//左边框
        STYLE_YELLOW.setBorderTop(BorderStyle.THIN);//上边框
        STYLE_YELLOW.setBorderRight(BorderStyle.THIN);//右边框
        STYLE_YELLOW.setAlignment(HorizontalAlignment.CENTER); // 水平居中
        STYLE_YELLOW.setVerticalAlignment(VerticalAlignment.CENTER); // 垂直居中

        STYLE_NORMAL = WORKBOOK.createCellStyle();
        STYLE_NORMAL.setBorderBottom(BorderStyle.THIN); //下边框
        STYLE_NORMAL.setBorderLeft(BorderStyle.THIN);//左边框
        STYLE_NORMAL.setBorderTop(BorderStyle.THIN);//上边框
        STYLE_NORMAL.setBorderRight(BorderStyle.THIN);//右边框
        STYLE_NORMAL.setAlignment(HorizontalAlignment.CENTER); // 水平居中
        STYLE_NORMAL.setVerticalAlignment(VerticalAlignment.CENTER); // 垂直居中
    }

    /**
     * @param dir             : excel文件存放路径
     * @param list            : 数据BeanList
     * @param newSheetName    : 新建sheet的名称
     * @param exceptSheetName : 期望sheet的名称
     * @return : void
     * @author : When6passBye
     * @date : 2019/9/3 2:39 PM
     */
    public static void readAndWriteExcel(String dir, List<ResultBean> list, String newSheetName, String exceptSheetName) {
        String path = Objects.requireNonNull(LogRet2Excel.class.getClassLoader().getResource(dir)).getPath();
        File file = new File(path);
        OutputStream os = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            //每次写excel都要重新赋值
            WORKBOOK = new HSSFWorkbook(fis);
            //初始化Style
            initStyle();
            //期望值的sheet页
            HSSFSheet exceptSheet = WORKBOOK.getSheet(exceptSheetName);
            //结果sheet页
            HSSFSheet retSheet = WORKBOOK.createSheet(newSheetName);
            System.out.println("期望值工作表名称" + exceptSheet.getSheetName());
            //得到有效行数
            int rowNumber = exceptSheet.getPhysicalNumberOfRows();
            System.out.println("有效行数" + rowNumber);
            //直接写
            writeHead(retSheet);
            for (int i = 0, j = 0; i < list.size(); i++, j += 2) {
                //先写期望结果
                HSSFRow expRow = exceptSheet.getRow(i + 1);
                writeExpectRow(retSheet, j + 1, expRow);
                //再写测试结果,和期望结果不一样的单元格设置黄色
                ResultBean bean = list.get(i);
                writeResultRow(retSheet, j + 2, bean, expRow);

            }
            //直接输出到原文件
            os = new FileOutputStream(file);
            WORKBOOK.write(os);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                WORKBOOK.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
