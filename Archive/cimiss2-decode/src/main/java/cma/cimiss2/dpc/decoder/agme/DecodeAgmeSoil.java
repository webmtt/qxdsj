package cma.cimiss2.dpc.decoder.agme;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import org.apache.commons.lang3.ArrayUtils;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Soil;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Soil_01;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Soil_02;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Soil_03;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Soil_04;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Soil_05;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Soil_06;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Soil_07;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Soil_08;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.InstanceInvoke;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;

// TODO: Auto-generated Javadoc
/**
 * ----------------------------------------------------
 * <br>.
 *
 * @author wangzunpeng
 * @Title:  DecodeAgmeSoil2.java
 * @Package org.cimiss2.decode.z_agme_soil
 * @Description: 土壤水分8要素解析
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月27日 上午10:14:40   wangzunpeng    Initial creation.
 * </pre>
 */
public class DecodeAgmeSoil {
	
	/** The sdf. */
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");


	

	/** 数据解析结果集. */
	private ParseResult<Agme_Soil> parseResult = new ParseResult<Agme_Soil>(false);
	
	/** The agme soil. */
	Agme_Soil agme_Soil = new Agme_Soil();

	/**
	 * Decode.
	 *
	 * @param file the file
	 * @return ParseResult<Agme_Soil>
	 * @Title: decode
	 * @Description:文件解析 
	 * @throws: 
	 */
	@SuppressWarnings("resource")
	public ParseResult<Agme_Soil> decode(File file) {

		// long s = System.currentTimeMillis();

		if (file != null && file.exists() && file.isFile()) {
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			// get file encode
			InputStreamReader read = null;
			Scanner scanner = null;
			try {
				FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();
				String fileCode = Encoding.javaname[fileEncodeUtil.detectEncoding(file)];
				
				fileCode = fileCode.equals("ISO8859_1") ? "GBK" : fileCode;
				fileCode = fileCode.equals("GB2312") ? "GBK" : fileCode;
				fileCode = fileCode.equals("ASCII") ? "GBK" : fileCode;
				read = new InputStreamReader(new FileInputStream(file), fileCode);

				// 文件中存在“=”分隔多个数据段
				scanner = new Scanner(read).useDelimiter("\n=");
				while (scanner.hasNext()) {

					String page = scanner.next(); // 获取一段数据

					if (page.trim().equalsIgnoreCase("NNNN")) {
						// 报文结束
						break;
					}

					// 去掉每一行的回车换行
					page = page.trim().replaceAll("\r|\n|\r\n", "@").replaceAll("@{2,}", "@").replaceAll("\\s+", "");
					String[] contents = page.split("@"); // 每一行都会以@结尾，以此分隔数据
					if(contents[0].trim().equalsIgnoreCase("NNNN"))
						contents = ArrayUtils.subarray(contents, 1, contents.length);
					// 每段数据第一行都是数据公共信息头，包含站号、经纬度等
					String head = contents[0];
					String[] heads = head.split(",");
					// 获取头信息后，移除头信息，只保留内容体
					// contents = ArrayUtils.remove(contents, 0);
					// ===========================增加整理头信息到实体类============================
					AgmeReportHeader agmeHeader = new AgmeReportHeader();
					ReadIniSoil ini = ReadIniSoil.getIni();

					// 头信息字段
					String headElement = ini.getValue("Head", "elements");
					String[] headElements = headElement.split(",");

					if (headElements.length == heads.length) {
						for (int i = 0; i < headElements.length; i++) {
							InstanceInvoke.setValues(agmeHeader, headElements[i], heads[i]);
						}
					} else {
						ReportError reportError = new ReportError();
						reportError.setMessage("report header error");
						reportError.setSegment(page);
						parseResult.put(reportError);
					}
					// =======================================================================

					if (parseContent(agmeHeader, head, heads, ArrayUtils.remove(contents, 0))) {
						parseResult.setSuccess(true);
					}
				}
				parseResult.put(agme_Soil);

				// long e = System.currentTimeMillis();
				// System.err.println("解析文件 : " + file.getName() + " size:" + file.length() / 1024 + "kb" + " 共花费时间 : " + (e - s) + " ms");
			} catch (UnsupportedEncodingException e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			} catch (FileNotFoundException e) {
				parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
			}finally {
				try {
					if (read != null) {
						read.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				try{
					if(scanner != null)
						scanner.close();
					}catch (Exception e) {
						e.printStackTrace();
					}
			}
		} else {
			// file not exsit
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
		}

		return parseResult;
	}

	/**
	 * Parses the content.
	 *
	 * @param agmeHeader the agme header
	 * @param head the head
	 * @param heads the heads
	 * @param contents the contents
	 * @return the boolean
	 */
	private Boolean parseContent(AgmeReportHeader agmeHeader,String head, String[] heads, String[] contents) {
		if (contents.length >= 3) {
			
			
			// 每一类数据的开始标识+此类数据行数
			String dataStart = contents[0];
			String[] dataStarts = dataStart.split(",");
			String startFlag = dataStarts[0].trim();
			//此类数据一共多少行
//			int dataNum = Integer.parseInt(dataStarts[1].trim());
			int ii = 0;
			while(ii < contents.length){
				if(!contents[ii].toUpperCase().startsWith("END_SOIL"))
					ii ++;
				else
					break;
			}
			int dataNum = ii - 1;
			// 当前类数据
			String[] datas = ArrayUtils.subarray(contents, 1, dataNum + 1);

			ReadIniSoil ini = ReadIniSoil.getIni();

			// 头信息字段
			String headElement = ini.getValue("Head", "elements");
			String[] headElements = headElement.split(",");
			// 数据信息字段
			String element = ini.getValue(startFlag, "elements");
			
			// this line added on 2019-4-1
			if(element == null){
				return true;
			}
			
			String[] elements = element.split(",");
			// String className = ini.getValue(startFlag, "className");
			// Class.forName(className);
			//===========================================================================
			StringBuilder sb = new StringBuilder();
			sb.append(head + "@\n");
			String[] type = dataStart.split(",");
			String CropType = type[0];
			sb.append(dataStart + "@\n");
			String [] time =datas[0].split(",");
			Date report_time = null;
			
			try {
				 report_time = sdf.parse(time[0]);
				 if(!TimeCheckUtil.checkTime(report_time)){
					 ReportError reportError = new ReportError();
					 reportError.setMessage("check time error!");
					 reportError.setSegment(ArrayUtils.toString(contents));
					 parseResult.put(reportError);
					 return false;
				 }
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int i = 0; i < dataNum; i++) {
				sb.append(datas[i] + "@\n");
			}
			sb.append(contents[dataNum + 1] + "@\n");
			agmeHeader.setReport_time(report_time);
			agmeHeader.setCropType(CropType);
			parseResult.put(new ReportInfo<AgmeReportHeader>(agmeHeader, sb.toString()));
			//===========================================================================
			switch (startFlag) {
			case "SOIL-01":
				if (setEleValue_01(heads, datas, headElements, elements) > 0) {
					if (!agme_Soil.soilTypes.contains(startFlag)) {
						agme_Soil.soilTypes.add(startFlag);
					}
				}
				// 设置完成之后，原有数组去掉已解析的数据,然后递归
				parseContent(agmeHeader, head, heads, ArrayUtils.subarray(contents, dataNum + 2, contents.length));
				break;
			case "SOIL-02":
				if (setEleValue_02(heads, datas, headElements, elements) > 0) {
					if (!agme_Soil.soilTypes.contains(startFlag)) {
						agme_Soil.soilTypes.add(startFlag);
					}
				}
				// 设置完成之后，原有数组去掉已解析的数据,然后递归
				parseContent(agmeHeader, head, heads, ArrayUtils.subarray(contents, dataNum + 2, contents.length));
				break;
			case "SOIL-03":
				if (setEleValue_03(heads, datas, headElements, elements) > 0) {
					if (!agme_Soil.soilTypes.contains(startFlag)) {
						agme_Soil.soilTypes.add(startFlag);
					}
				}
				// 设置完成之后，原有数组去掉已解析的数据,然后递归
				parseContent(agmeHeader, head, heads, ArrayUtils.subarray(contents, dataNum + 2, contents.length));
				break;
			case "SOIL-04":
				if (setEleValue_04(heads, datas, headElements, elements) > 0) {
					if (!agme_Soil.soilTypes.contains(startFlag)) {
						agme_Soil.soilTypes.add(startFlag);
					}
				}
				// 设置完成之后，原有数组去掉已解析的数据,然后递归
				parseContent(agmeHeader, head, heads, ArrayUtils.subarray(contents, dataNum + 2, contents.length));
				break;
			case "SOIL-05":
				if (setEleValue_05(heads, datas, headElements, elements) > 0) {
					if (!agme_Soil.soilTypes.contains(startFlag)) {
						agme_Soil.soilTypes.add(startFlag);
					}
				}
				// 设置完成之后，原有数组去掉已解析的数据,然后递归
				parseContent(agmeHeader, head, heads, ArrayUtils.subarray(contents, dataNum + 2, contents.length));
				break;
			case "SOIL-06":
				if (setEleValue_06(heads, datas, headElements, elements) > 0) {
					if (!agme_Soil.soilTypes.contains(startFlag)) {
						agme_Soil.soilTypes.add(startFlag);
					}
				}
				// 设置完成之后，原有数组去掉已解析的数据,然后递归
				parseContent(agmeHeader, head, heads, ArrayUtils.subarray(contents, dataNum + 2, contents.length));
				break;
			case "SOIL-07":
				
				if (setEleValue_07(heads, datas, headElements, elements) > 0) {
					if (!agme_Soil.soilTypes.contains(startFlag)) {
						agme_Soil.soilTypes.add(startFlag);
					}
				}
				// 设置完成之后，原有数组去掉已解析的数据,然后递归
				parseContent(agmeHeader, head, heads, ArrayUtils.subarray(contents, dataNum + 2, contents.length));
				break;
			case "SOIL-08":
				if (setEleValue_08(heads, datas, headElements, elements) > 0) {
					if (!agme_Soil.soilTypes.contains(startFlag)) {
						agme_Soil.soilTypes.add(startFlag);
					}
				}
				// 设置完成之后，原有数组去掉已解析的数据,然后递归
				parseContent(agmeHeader, head, heads, ArrayUtils.subarray(contents, dataNum + 2, contents.length));
				break;

			default:
				ReportError reportError = new ReportError();
				reportError.setMessage("Unrecognizertd report");
				reportError.setSegment(ArrayUtils.toString(contents));
				parseResult.put(reportError);
				break;
			}
		}
		return true;

	}

	/**
	 * Sets the ele value 08.
	 *
	 * @param heads 头信息数据
	 * @param datas 数据
	 * @param headElements 头信息字段名称
	 * @param elements 数据字段名称
	 * @return the int
	 * @Title: setEleValue_08
	 * @Description: 为soil_08对象设置值
	 * @throws: 
	 */
	private int setEleValue_08(String[] heads, String[] datas, String[] headElements, String[] elements) {
		int msgCount = datas.length;
		// 当前多条数据解析
		for (int dNum = 0; dNum < msgCount; dNum++) {
			Agme_Soil_08 agme_Soil_08 = new Agme_Soil_08();
			try {
				for (int i = 0; i < headElements.length; i++) {
					InstanceInvoke.setValues(agme_Soil_08, headElements[i], heads[i]);
				}

				String lineContent = datas[dNum].trim();
				if(lineContent.endsWith(","))
					lineContent = lineContent + "999999";
				String[] lineContents = lineContent.split(",", 6);
				int length = lineContents.length;
				if(length != 6) {
					throw new RuntimeException("SOIL_08_report length error");
				}
				for (int i = 0; i < length; i++) {
					InstanceInvoke.setValues(agme_Soil_08, elements[i], lineContents[i]);
				}
			} catch (Exception e) {
				ReportError reportError = new ReportError();
				reportError.setMessage("report segment format conversion error:" + e.getMessage());
				reportError.setSegment(datas[dNum]);
				parseResult.put(reportError);
				msgCount--;
				continue;
			}
			agme_Soil.agme_Soil_08s.add(agme_Soil_08);
		}
		return msgCount;
	}

	/**
	 * Sets the ele value 07.
	 *
	 * @param heads 头信息数据
	 * @param datas 数据
	 * @param headElements 头信息字段名称
	 * @param elements 数据字段名称
	 * @return the int
	 * @Title: setEleValue_07
	 * @Description: 为soil_07对象设置值
	 * @throws: 
	 */
	private int setEleValue_07(String[] heads, String[] datas, String[] headElements, String[] elements) {
		int msgCount = datas.length;
		// 当前多条数据解析
		for (int dNum = 0; dNum < msgCount; dNum++) {
			Agme_Soil_07 agme_Soil_07 = new Agme_Soil_07();
			try {
				for (int i = 0; i < headElements.length; i++) {
					InstanceInvoke.setValues(agme_Soil_07, headElements[i], heads[i]);
				}

				String lineContent = datas[dNum].trim();
				if(lineContent.endsWith(","))
					lineContent = lineContent + "999999";
				String[] lineContents = lineContent.split(",");
				int length = lineContents.length;
				if(length != 5) {
					throw new RuntimeException("SOIL_07_report length error");
				}
				for (int i = 0; i < length; i++) {
					InstanceInvoke.setValues(agme_Soil_07, elements[i], lineContents[i]);
				}
			} catch (Exception e) {
				ReportError reportError = new ReportError();
				reportError.setMessage("report segment format conversion error:" + e.getMessage());
				reportError.setSegment(datas[dNum]);
				parseResult.put(reportError);
				msgCount--;
				continue;
			}
			agme_Soil.agme_Soil_07s.add(agme_Soil_07);
		}
		return msgCount;
	}

	/**
	 * Sets the ele value 06.
	 *
	 * @param heads 头信息数据
	 * @param datas 数据
	 * @param headElements 头信息字段名称
	 * @param elements 数据字段名称
	 * @return the int
	 * @Title: setEleValue_06
	 * @Description: 为soil_06对象设置值
	 * @throws: 
	 */
	private int setEleValue_06(String[] heads, String[] datas, String[] headElements, String[] elements) {
		int msgCount = datas.length;
		// 当前多条数据解析
		for (int dNum = 0; dNum < msgCount; dNum++) {
			Agme_Soil_06 agme_Soil_06 = new Agme_Soil_06();
			try {
				for (int i = 0; i < headElements.length; i++) {
					InstanceInvoke.setValues(agme_Soil_06, headElements[i], heads[i]);
				}

				String lineContent = datas[dNum].trim();
				if(lineContent.endsWith(","))
					lineContent = lineContent + "999999";
				String[] lineContents = lineContent.split(",");
				int length = lineContents.length;
				if(length != 14) {
					throw new RuntimeException("SOIL_06_report length error");
				}
				for (int i = 0; i < length; i++) {
					InstanceInvoke.setValues(agme_Soil_06, elements[i], lineContents[i]);
				}
			} catch (Exception e) {
				ReportError reportError = new ReportError();
				reportError.setMessage("report segment format conversion error:" + e.getMessage());
				reportError.setSegment(datas[dNum]);
				parseResult.put(reportError);
				msgCount--;
				continue;
			}
			agme_Soil.agme_Soil_06s.add(agme_Soil_06);
		}
		return msgCount;
	}

	/**
	 * Sets the ele value 05.
	 *
	 * @param heads 头信息数据
	 * @param datas 数据
	 * @param headElements 头信息字段名称
	 * @param elements 数据字段名称
	 * @return the int
	 * @Title: setEleValue_05
	 * @Description: 为soil_05对象设置值
	 * @throws: 
	 */
	private int setEleValue_05(String[] heads, String[] datas, String[] headElements, String[] elements) {
		int msgCount = datas.length;
		// 当前多条数据解析
		for (int dNum = 0; dNum < msgCount; dNum++) {
			Agme_Soil_05 agme_Soil_05 = new Agme_Soil_05();
			try {
				for (int i = 0; i < headElements.length; i++) {
					InstanceInvoke.setValues(agme_Soil_05, headElements[i], heads[i]);
				}

				String lineContent = datas[dNum].trim();
				if(lineContent.endsWith(","))
					lineContent = lineContent + "999999";
				String[] lineContents = lineContent.split(",");
				int length = lineContents.length;
				if(length != 5) {
					throw new RuntimeException("SOIL_05_report length error");
				}
				for (int i = 0; i < length; i++) {
					InstanceInvoke.setValues(agme_Soil_05, elements[i], lineContents[i]);
				}
			} catch (Exception e) {
				ReportError reportError = new ReportError();
				reportError.setMessage("report segment format conversion error:" + e.getMessage());
				reportError.setSegment(datas[dNum]);
				parseResult.put(reportError);
				msgCount--;
				continue;
			}
			agme_Soil.agme_Soil_05s.add(agme_Soil_05);
		}
		return msgCount;
	}

	/**
	 * Sets the ele value 04.
	 *
	 * @param heads 头信息数据
	 * @param datas 数据
	 * @param headElements 头信息字段名称
	 * @param elements 数据字段名称
	 * @return the int
	 * @Title: setEleValue_04
	 * @Description: 为soil_04对象设置值
	 * @throws: 
	 */
	private int setEleValue_04(String[] heads, String[] datas, String[] headElements, String[] elements) {
		int msgCount = datas.length;
		// 当前多条数据解析
		for (int dNum = 0; dNum < msgCount; dNum++) {
			Agme_Soil_04 agme_Soil_04 = new Agme_Soil_04();
			try {
				for (int i = 0; i < headElements.length; i++) {
					InstanceInvoke.setValues(agme_Soil_04, headElements[i], heads[i]);
				}

				String lineContent = datas[dNum].trim();
				if(lineContent.endsWith(","))
					lineContent = lineContent + "999999";
				String[] lineContents = lineContent.split(",");
				int length = lineContents.length;
				if(length != 14) {
					throw new RuntimeException("SOIL_04_report length error");
				}
				for (int i = 0; i < length; i++) {
					InstanceInvoke.setValues(agme_Soil_04, elements[i], lineContents[i]);
				}
			} catch (Exception e) {
				ReportError reportError = new ReportError();
				reportError.setMessage("report segment format conversion error:" + e.getMessage());
				reportError.setSegment(datas[dNum]);
				parseResult.put(reportError);
				msgCount--;
				continue;
			}
			agme_Soil.agme_Soil_04s.add(agme_Soil_04);
		}
		return msgCount;
	}

	/**
	 * Sets the ele value 03.
	 *
	 * @param heads 头信息数据
	 * @param datas 数据
	 * @param headElements 头信息字段名称
	 * @param elements 数据字段名称
	 * @return the int
	 * @Title: setEleValue_03
	 * @Description: 为soil_03对象设置值
	 * @throws: 
	 */
	private int setEleValue_03(String[] heads, String[] datas, String[] headElements, String[] elements) {
		int msgCount = datas.length;
		// 当前多条数据解析
		for (int dNum = 0; dNum < msgCount; dNum++) {
			Agme_Soil_03 agme_Soil_03 = new Agme_Soil_03();
			try {
				for (int i = 0; i < headElements.length; i++) {
					InstanceInvoke.setValues(agme_Soil_03, headElements[i], heads[i]);
				}

				String lineContent = datas[dNum].trim();
				if(lineContent.endsWith(","))
					lineContent = lineContent + "999999";
				
				String[] lineContents = lineContent.split(",");
				int length = lineContents.length;
				if(length != 14) {
					throw new RuntimeException("SOIL_03_report length error");
				}
				for (int i = 0; i < length; i++) {
					InstanceInvoke.setValues(agme_Soil_03, elements[i], lineContents[i]);
				}
			} catch (Exception e) {
				ReportError reportError = new ReportError();
				reportError.setMessage("report segment format conversion error:" + e.getMessage());
				reportError.setSegment(datas[dNum]);
				parseResult.put(reportError);
				msgCount--;
				continue;
			}
			agme_Soil.agme_Soil_03s.add(agme_Soil_03);
		}
		return msgCount;
	}

	/**
	 * Sets the ele value 02.
	 *
	 * @param heads 头信息数据
	 * @param datas 数据
	 * @param headElements 头信息字段名称
	 * @param elements 数据字段名称
	 * @return the int
	 * @Title: setEleValue_02
	 * @Description: 为soil_02对象设置值
	 * @throws: 
	 */
	private int setEleValue_02(String[] heads, String[] datas, String[] headElements, String[] elements) {
		int msgCount = datas.length;
		// 当前多条数据解析
		for (int dNum = 0; dNum < msgCount; dNum++) {
			Agme_Soil_02 agme_Soil_02 = new Agme_Soil_02();
			try {
				for (int i = 0; i < headElements.length; i++) {
					InstanceInvoke.setValues(agme_Soil_02, headElements[i], heads[i]);
				}
				
				String lineContent = datas[dNum].trim();
				if(lineContent.endsWith(","))
					lineContent = lineContent + "999999";
				String[] lineContents = lineContent.split(",");
				int length = lineContents.length;
				if(length != 17) {
					throw new RuntimeException("SOIL_02_report length error");
				}
				for (int i = 0; i < length; i++) {
					InstanceInvoke.setValues(agme_Soil_02, elements[i], lineContents[i]);
				}
			} catch (Exception e) {
				ReportError reportError = new ReportError();
				reportError.setMessage("report segment format conversion error:" + e.getMessage());
				reportError.setSegment(datas[dNum]);
				parseResult.put(reportError);
				msgCount--;
				continue;
			}
			agme_Soil.agme_Soil_02s.add(agme_Soil_02);
		}
		return msgCount;
	}

	/**
	 * Sets the ele value 01.
	 *
	 * @param heads 头信息数据
	 * @param datas 数据
	 * @param headElements 头信息字段名称
	 * @param elements 数据字段名称
	 * @return the int
	 * @Title: setEleValue_01
	 * @Description: 为soil_01对象设置值
	 * @throws: 
	 */
	private int setEleValue_01(String[] heads, String[] datas, String[] headElements, String[] elements) {
		int msgCount = datas.length;
		// 当前多条数据解析
		for (int dNum = 0; dNum < msgCount; dNum++) {
			Agme_Soil_01 agme_Soil_01 = new Agme_Soil_01();
			try {
				for (int i = 0; i < headElements.length; i++) {
					InstanceInvoke.setValues(agme_Soil_01, headElements[i], heads[i]);
				}

				String lineContent = datas[dNum].trim();
				if(lineContent.endsWith(","))
					lineContent = lineContent + "999999";
				String[] lineContents = lineContent.split(",");
				int length = lineContents.length;
				if(length != 6) {
					throw new RuntimeException("SOIL_01_report length error");
				}
				for (int i = 0; i < length; i++) {
					InstanceInvoke.setValues(agme_Soil_01, elements[i], lineContents[i]);
				}
			} catch (Exception e) {
				ReportError reportError = new ReportError();
				reportError.setMessage("report segment format conversion error:" + e.getMessage());
				reportError.setSegment(datas[dNum]);
				parseResult.put(reportError);
				msgCount--;
				continue;
			}
			agme_Soil.agme_Soil_01s.add(agme_Soil_01);
		}
		return msgCount;
	}
	
	

}
