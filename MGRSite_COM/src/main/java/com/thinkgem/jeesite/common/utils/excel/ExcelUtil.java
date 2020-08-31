package com.thinkgem.jeesite.common.utils.excel;

import java.sql.ResultSet;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


public class ExcelUtil {

	public static void fillExcelData(ResultSet rs,Workbook wb,String[] headers)throws Exception{
		int rowIndex=0;
		Sheet sheet=wb.createSheet();
		Row row=sheet.createRow(rowIndex++);
		for(int i=0;i<headers.length;i++){
			row.createCell(i).setCellValue(headers[i]);
		}
		while(rs.next()){
			row=sheet.createRow(rowIndex++);
			for(int i=0;i<headers.length;i++){
				row.createCell(i).setCellValue(rs.getObject(i+1).toString());
			}
		}
	}
	
//	public static Workbook fillExcelDataWithTemplate(List list,String templateFileName)throws Exception{
////		InputStream inp=ExcelUtil.class.getResourceAsStream("/com/sjxd/template/"+templateFileName);
////		POIFSFileSystem fs=new POIFSFileSystem(inp);
////		Workbook wb=new HSSFWorkbook(fs);
////		Sheet sheet=wb.getSheetAt(0);    //获取第一个sheet页
////		ApiParamClassDefine ddddd ;
////		// 获取列数
////		int cellNums=sheet.getRow(0).getLastCellNum();
////		int rowIndex=1;
////		/*while(rs.next()){
////			Row row=sheet.createRow(rowIndex++);
////			for(int i=0;i<cellNums;i++){
////				row.createCell(i).setCellValue(rs.getObject(i+1).toString());
////			}
////		}*/
////		for (int i = 0; i < list.size(); i++) {
////			Row row=sheet.createRow(rowIndex++);
////			ddddd = (ApiParamClassDefine) list.get(i);
////			row.createCell(0).setCellValue(ddddd.getDescription());
////			row.createCell(1).setCellValue(ddddd.getParamClassId());
////			row.createCell(2).setCellValue(ddddd.getSerialNo());
////			row.createCell(3).setCellValue(ddddd.getParamClassName());
////			//System.out.println(((ApiParamClassDefine) list.get(i)).getDescription());
//		}
//		return wb;
//	}
	
	public static String formatCell(HSSFCell hssfCell){
		if(hssfCell==null){
			return "";
		}else{
			if(hssfCell.getCellType()==HSSFCell.CELL_TYPE_BOOLEAN){
				return String.valueOf(hssfCell.getBooleanCellValue());
			}else if(hssfCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
				return String.valueOf(hssfCell.getNumericCellValue());
			}else{
				return String.valueOf(hssfCell.getStringCellValue());
			}
		}
	}
}
