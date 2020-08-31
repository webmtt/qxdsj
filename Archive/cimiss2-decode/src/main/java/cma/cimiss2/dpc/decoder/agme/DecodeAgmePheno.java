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
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.ArrayUtils;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Pheno;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.InstanceInvoke;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;

// TODO: Auto-generated Javadoc
/**
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>.
 *
 * @author wuzuoqiang
 * @version 1.0
 * @Description:    TODO(农业气象自然物候要素数据解码类)<br>
 *  
 * <strong> 注意：本内容仅限于国家气象信息中心内部传阅，禁止外泄以及用于其他的商业目 </strong><br>
 * *******************************************************************************************<br>
 */
public class DecodeAgmePheno {
	
	/** 数据解析结果集. */
	private ParseResult<Agme_Pheno> parseResult = new ParseResult<Agme_Pheno>(false);
	
	/** 解码对象封装. */
	Agme_Pheno agme_Pheno = new Agme_Pheno();
	
	/** 日期时间格式. */
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	
	/** 读取物候资料配置文件. */
	ReadIniPheno ini = ReadIniPheno.getIni();
	
	/**
	 * 文件解析.
	 *
	 * @param file  待解析文件
	 * @return ParseResult<Agme_Soil> 解析结果封装
	 */
	@SuppressWarnings("resource")
	public ParseResult<Agme_Pheno> decode(File file) {

		// long s = System.currentTimeMillis();

		if (file != null && file.exists() && file.isFile()) {
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			// get file encode
			InputStreamReader read = null;
			Scanner scanner  = null;
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
//					page = page.replaceAll("\r|\n|\\s+", "");
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
//					ReadIniPheno ini = ReadIniPheno.getIni();

					// 头信息字段
					String headElement = ini.getValue("Head", "elements");
					String[] headElements = headElement.split(",");

					if (headElements.length == heads.length) {
						for (int i = 0; i < headElements.length; i++) {
							InstanceInvoke.setValues(agmeHeader, headElements[i], heads[i]);
						}
					} else {
						ReportError reportError = new ReportError();
						reportError.setMessage("report Header error");
						reportError.setSegment(head);
						parseResult.put(reportError);
					}
					// =======================================================================

					if (parseContent(agmeHeader, head, heads, ArrayUtils.remove(contents, 0))) {
						parseResult.setSuccess(true);
					}
				}
				parseResult.put(agme_Pheno);

				// long e = System.currentTimeMillis();
				// System.err.println("解析文件 : " + file.getName() + " size:" + file.length() / 1024 + "kb" + " 共花费时间 : " + (e - s) + " ms");
			} catch (UnsupportedEncodingException e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			} catch (FileNotFoundException e) {
				parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
			}catch(ArrayIndexOutOfBoundsException e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
//				ReportError reportError = new ReportError();
//				reportError.setMessage("报文错误");
//				reportError.setSegment(file.getName());
//				parseResult.put(reportError);
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
	 * @return Boolean
	 * @Title: parseContent
	 * @Description:(这里用一句话描述这个方法的作用) 
	 * @throws： 
	 */
	private Boolean parseContent(AgmeReportHeader agmeHeader, String head, String[] heads, String[] contents) {
		if (contents.length >= 3) {
			// 每一类数据的开始标识+此类数据行数
			String dataStart = contents[0];
			String[] dataStarts = dataStart.split(",");
			String startFlag = dataStarts[0].trim();
//			int dataNum = Integer.parseInt(dataStarts[1].trim());
			int ii = 0;
			while(ii < contents.length){
				if(!contents[ii].toUpperCase().startsWith("END_PHENO"))
					ii ++;
				else
					break;
			}
			int dataNum = ii - 1;
			
			// 当前类数据
			String[] datas = ArrayUtils.subarray(contents, 1, dataNum + 1);

//			ReadIniPheno ini = ReadIniPheno.getIni();

			// 头信息字段
			String headElement = ini.getValue("Head", "elements");
			if(headElement == null){
				ReportError reportError = new ReportError();
				reportError.setMessage("Head Element is null");
				parseResult.put(reportError);
				return false;
			}
			String[] headElements = headElement.split(",");
			// 数据信息字段
			String element = ini.getValue(startFlag, "elements");
			if(element == null){
				ReportError reportError = new ReportError();
				reportError.setMessage("Element is null");
				parseResult.put(reportError);
				return false;
			}
			String[] elements = element.split(",");

			// ===========================================================================
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
			
				e.printStackTrace();
			}
			for (int i = 0; i < dataNum; i++) {
				sb.append(datas[i] + "@\n");
			}
			sb.append(contents[dataNum + 1] + "@\n");
			agmeHeader.setReport_time(report_time);
			agmeHeader.setCropType(CropType);
			parseResult.put(new ReportInfo<AgmeReportHeader>(agmeHeader, sb.toString()));
			// ===========================================================================
			String className = ini.getValue(startFlag, "className");

			switchFlagToRun(agmeHeader, head, heads, contents, startFlag, dataNum, datas, headElements, elements, className);
		}
		return true;

	}

	/**
	 * Switch flag to run.
	 *
	 * @param agmeHeader the agme header
	 * @param head the head
	 * @param heads the heads
	 * @param contents the contents
	 * @param startFlag the start flag
	 * @param dataNum the data num
	 * @param datas the datas
	 * @param headElements the head elements
	 * @param elements the elements
	 * @param className the class name
	 */
	private void switchFlagToRun(AgmeReportHeader agmeHeader, String head, String[] heads, String[] contents, String startFlag, int dataNum, String[] datas, String[] headElements,
			String[] elements, String className) {
		switch (startFlag) {
		case "PHENO-01":
			prop(agmeHeader, head, heads, contents, startFlag, dataNum, datas, headElements, elements, className, agme_Pheno.agme_pheno_01s);
			break;
		case "PHENO-02":
			prop(agmeHeader, head, heads, contents, startFlag, dataNum, datas, headElements, elements, className, agme_Pheno.agme_pheno_02s);
			break;
		case "PHENO-03":
			prop(agmeHeader, head, heads, contents, startFlag, dataNum, datas, headElements, elements, className, agme_Pheno.agme_pheno_03s);
			break;
		case "PHENO-04":
			prop(agmeHeader, head, heads, contents, startFlag, dataNum, datas, headElements, elements, className, agme_Pheno.agme_pheno_04s);
			break;

		default:
			ReportError reportError = new ReportError();
			reportError.setMessage("Unrecognized message");
			reportError.setSegment(ArrayUtils.toString(contents));
			parseResult.put(reportError);
			break;
		}
	}

	/**
	 * Prop.
	 *
	 * @param agmeHeader the agme header
	 * @param head the head
	 * @param heads the heads
	 * @param contents the contents
	 * @param startFlag the start flag
	 * @param dataNum the data num
	 * @param datas the datas
	 * @param headElements the head elements
	 * @param elements the elements
	 * @param className the class name
	 * @param list the list
	 */
	private void prop(AgmeReportHeader agmeHeader, String head, String[] heads, String[] contents, String startFlag, int dataNum, String[] datas, String[] headElements,
			String[] elements, String className, List<Object> list) {
		if (setEleValue(startFlag, heads, datas, headElements, elements, className, list) > 0) {
			if (!agme_Pheno.phenoTypes.contains(startFlag)) {
				agme_Pheno.phenoTypes.add(startFlag);
			}
		}
		// 设置完成之后，原有数组去掉已解析的数据,然后递归
		parseContent(agmeHeader, head, heads, ArrayUtils.subarray(contents, dataNum + 2, contents.length));
	}

	/**
	 * Sets the ele value.
	 *
	 * @param startFlag the start flag
	 * @param heads 头信息数据
	 * @param datas 数据
	 * @param headElements 头信息字段名称
	 * @param elements 数据字段名称
	 * @param className the class name
	 * @param list 存储已设置好值的对象的list
	 * @return the int
	 * @Title: setEleValue
	 * @Description: 为对象设置值
	 * @throws: 
	 */
	private int setEleValue(String startFlag, String[] heads, String[] datas, String[] headElements, String[] elements, String className, List<Object> list) {
		int msgCount = datas.length;
		// 当前多条数据解析
		for (int dNum = 0; dNum < msgCount; dNum++) {
			// Agme_Pheno_01 agme_Pheno_01 = new Agme_Pheno_01();
			Object obj = null;
			try {
				obj = Class.forName(className).newInstance();
			} catch (InstantiationException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
			if (obj != null) {

				try {
					for (int i = 0; i < headElements.length; i++) {
						InstanceInvoke.setValues(obj, headElements[i], heads[i]);
					}

					String lineContent = datas[dNum].trim();
					String[] lineContents = lineContent.split(",");
					if(elements.length != lineContents.length) {
						throw new RuntimeException(startFlag + " Message length error");
					}
					for (int i = 0; i < lineContents.length; i++) {
						InstanceInvoke.setValues(obj, elements[i], lineContents[i]);
					}
				} catch (Exception e) {
					ReportError reportError = new ReportError();
					reportError.setMessage("Message segment format conversion error:" + e.getMessage());
					reportError.setSegment(datas[dNum]);
					parseResult.put(reportError);
					msgCount--;
					continue;
				}
				list.add(obj);
			} else {
				return 0;
			}
		}
		return msgCount;
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		File file = new File("D:\\Z_AGME_C_BEHF_20191223001710_O_PHENO.txt");
		ParseResult<Agme_Pheno> decode = new DecodeAgmePheno().decode(file);
		System.out.println(decode.getData().get(0).agme_pheno_03s.size());
	}
	
	
}
