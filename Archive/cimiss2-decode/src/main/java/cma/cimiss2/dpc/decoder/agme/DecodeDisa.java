package cma.cimiss2.dpc.decoder.agme;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeDisa;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeDisa01;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeDisa02;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeDisa03;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeDisa04;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeDisa05;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.ElementValUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;
import cma.cimiss2.dpc.decoder.tools.enumeration.ObservationMethod;

// TODO: Auto-generated Javadoc
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
	灾害要素数据文件正文由5个子要素组成<br>
	
 * 1	农业气象灾害观测子要素	Disa-01	要素个数 7<br>
 * 2	农业气象灾害调查子要素	Disa-02	要素个数  8<br>
 * 3	牧草灾害子要素	Disa-03	要素个数  6<br>
 * 4	家畜灾害子要素	Disa-04	要素个数  6<br>
 * 5	植物灾害子要素	Disa-05	要素个数  7<br>
 *
 * <p>
 * notes:
 * 解码程序分为两个步骤:数据读取及预处理、实体对象构建及赋值。
 * <ul>
 * <li>数据读取及预处理过程：
 * <ol>
 * <li>所有运算采用java double进行基本运算。
 * <li>无特殊说明时，所有包含”/”的数据全部转化成999999。
 * <li>未通过数据校验时，该条数据将会被当成错误报文处理。
 * </ol>
 * </li>
 * <li>实体对象构建及赋值：
 * <ol>
 * <li>校验所有段的数据的数据类型是否合法。
 * <li>所有的赋值失败都会导致解码错误，该条数据将被当成错误报文处理。
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cimiss2.dpc.decoder.bean.agme.ZAgmeDisa}。
 * </ol>
 * </li>
 * </ul>
 * 
 * *<strong> sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br>
53923,354400,1073800,14210,14219,1,2@<br>
DISA-01,1@<br>
20180408000000,0203,010301,0035,2,00,"叶片发黄，叶尖干枯"@<br>
END_DISA-01@<br>
DISA-05,4@<br>
20180408000000,20180406000000,20180408000000,03021503,0203,0080,"8日最低气温1.2℃，地面最低-3.4℃。受害程度：重"@<br>
20180408000000,20180406000000,20180408000000,03021501,0203,0070,"8日最低气温1.2℃，地面最低-3.4℃。受害程度：重"@<br>
20180408000000,20180406000000,20180408000000,03021502,0203,0035,"8日最低气温1.2℃，地面最低-3.4℃。受害程度：轻"@<br>
20180408000000,20180406000000,20180408000000,03018024,0203,0015,"8日最低气温1.2℃，地面最低-3.4℃。受害程度：轻"@<br>
END_DISA-05@<br>
=<br>
NNNN<br>

 * <strong>code:</strong><br>
 * DecodeDisa decodeDisa = new DecodeDisa();<br>
 * ParseResult<ZAgmeDisa> parseResult = decodeDisa.decodeFile(new File(String filepath));<br>
 * List<ZAgmeDisa> list = parseResult.getData();<br>
 * System.out.println(list.size());<br>
 * System.out.println(list.get(0).disaTypes);<br>
 * <strong>output:</strong><br>
 * 1<br>
 * [DISA-01, DISA-05]<br>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月26日 上午10:12:51   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class DecodeDisa {
	
	/** 存放数据解析的结果集. */
	private ParseResult<ZAgmeDisa> parseResult = new ParseResult<ZAgmeDisa>(false);
	
	/** 解码对象封装. */
	private ZAgmeDisa agmeDisa = new ZAgmeDisa();
	
	/**
	 * 解析灾害要素数据文件   .
	 *
	 * @param file   待解析的文件
	 * @return ParseResult<ZAgmeDisa>    解码结果集包裹对象
	 */
	@SuppressWarnings("resource")
	public ParseResult<ZAgmeDisa> decodeFile(File file) {
		if(file != null && file.exists() && file.isFile()) {
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			Scanner scanner = null;
			InputStreamReader read = null;
			try {
				// 获取文件的编码
				FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();     
		        String fileCode = Encoding.javaname[fileEncodeUtil.detectEncoding(file)];
		        
		        fileCode = fileCode.equals("ISO8859_1") ? "GBK" : fileCode;
				fileCode = fileCode.equals("GB2312") ? "GBK" : fileCode;
				fileCode = fileCode.equals("ASCII") ? "GBK" : fileCode;
				
				read = new InputStreamReader(new FileInputStream(file), fileCode);
				scanner = new Scanner(read).useDelimiter("\n=");
				while (scanner.hasNext()) {
					// 获取一个完整的报文段
					String report = scanner.next();
					// 去掉无效的空格换行
					Pattern p = Pattern.compile("\\s*|\t|\r|\n");
					Matcher m = p.matcher(report);
					report = m.replaceAll("");
					if(report.equals("") || report.length() < 1)
						continue;
					// 数据第一行为报文头信息
					String[] reports = report.split("@");
					if(report.trim().equalsIgnoreCase("NNNN")) {
						// 报文结束
						break;
					}
					// cannot be removed
					char s = report.trim().charAt(0);   
					if(s == 65279){    //65279是空字符   
					  if(report.length() > 1){   
						  report = report.substring(1); 
					  }  
					}
					
					if(reports.length < 4) {
						ReportError reportError = new ReportError();
						reportError.setMessage("Empty message segment");
						reportError.setSegment(report);
						parseResult.put(reportError);
						continue;
					}
					
					String report_header = reports[0];
					String[] headers = report_header.split(",");
					if(headers.length == 7) {
						// 解析报文段头信息
						AgmeReportHeader agmeHeader = new AgmeReportHeader();
						try {
							// 区站号
							agmeHeader.setStationNumberChina(headers[0]);
							// 纬度
							agmeHeader.setLatitude(ElementValUtil.getlatitude(headers[1]));
							// 经度
							agmeHeader.setLongitude(ElementValUtil.getLongitude(headers[2]));
							// 测站高度
							agmeHeader.setHeightOfSationGroundAboveMeanSeaLevel(ElementValUtil.ToBeValidDouble(headers[3], 0.1));
							// 气压传感器高度
							agmeHeader.setHeightOfBarometerAboveMeanSeaLevel(ElementValUtil.ToBeValidDouble(headers[4], 0.1));							
							// 观测方式
							agmeHeader.setObservationMethod(ObservationMethod.MANUAL);
							// 报文记录数
							agmeHeader.setReporetCount(Integer.parseInt(headers[6]));
							reports = ArrayUtils.remove(reports, 0);
							if(processReports(reports, agmeHeader, report_header)) {
								parseResult.setSuccess(true);
							}

						
						} catch (Exception e) {
							ReportError reportError = new ReportError();
							reportError.setMessage("report Header error");
							reportError.setSegment(report);
							parseResult.put(reportError);
							continue;
						}
					}else {  // 报文头信息错误
						ReportError reportError = new ReportError();
						reportError.setMessage(" report Header error");
						reportError.setSegment(report);
						parseResult.put(reportError);
					}
				}
				parseResult.put(agmeDisa);
			} catch (UnsupportedEncodingException e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			} catch (FileNotFoundException e) {
				parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
			}catch (Exception e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			}
			finally {
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
		}else {
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
		}
		return parseResult;
	}
	
	/**
	 * 对文件内部报文段分割   .
	 *
	 * @param reports 待解析的报文
	 * @param agmeReportHeader the agme report header
	 * @param report_header the report header
	 * @return boolean 是否成功解析
	 */
	private boolean processReports(String[] reports, AgmeReportHeader agmeReportHeader, String report_header) {
		AgmeReportHeader agmeHeader = new AgmeReportHeader(agmeReportHeader);
		StringBuilder sb = new StringBuilder();
		sb.append(report_header+"@\n");
		try {
			String[] disaTypes = reports[0].split(",");
			sb.append(reports[0] + "@\n");
//			int disa_count = Integer.parseInt(disaTypes[1]);
			
			int ii = 0;
			while(ii < reports.length){
				if(!reports[ii].toUpperCase().startsWith("END_DISA"))
					ii ++;
				else
					break;
			}
			int disa_count = ii - 1;
			
			reports = ArrayUtils.remove(reports, 0);
			String[] tempDisas;
			switch (disaTypes[0]) {
			case "DISA-01":
				tempDisas = ArrayUtils.subarray(reports, 0, disa_count);
				if(processDisa01(tempDisas, agmeHeader) > 0) {
					if(!agmeDisa.disaTypes.contains(disaTypes[0])) {
						agmeDisa.disaTypes.add(disaTypes[0]);
					}
				}
				
				for (int i = 0; i < tempDisas.length; i++) {
					sb.append(tempDisas[i]+"@\n");
				}
				sb.append(reports[disa_count]+"@\n");
				reports = ArrayUtils.subarray(reports, disa_count+1, reports.length);
				agmeHeader.setCropType(disaTypes[0]);
				parseResult.put(new ReportInfo<AgmeReportHeader>(agmeHeader, sb.toString()));
				
				if(reports.length >= 3) {
					processReports(reports, agmeHeader, report_header);
				}
				break;
			case "DISA-02":
				tempDisas = ArrayUtils.subarray(reports, 0, disa_count);
				if(processDisa02(tempDisas, agmeHeader) > 0) {
					if(!agmeDisa.disaTypes.contains(disaTypes[0])) {
						agmeDisa.disaTypes.add(disaTypes[0]);
					}
				}
				
				for (int i = 0; i < tempDisas.length; i++) {
					sb.append(tempDisas[i]+"@\n");
				}
				sb.append(reports[disa_count]+"@\n");
				reports = ArrayUtils.subarray(reports, disa_count+1, reports.length);
				agmeHeader.setCropType(disaTypes[0]);
				parseResult.put(new ReportInfo<AgmeReportHeader>(agmeHeader, sb.toString()));
				if(reports.length >= 3) {
					processReports(reports, agmeHeader, report_header);
				}
				break;
				
			case "DISA-03":
				tempDisas = ArrayUtils.subarray(reports, 0, disa_count);
				if(processDisa03(tempDisas, agmeHeader) > 0) {
					if(!agmeDisa.disaTypes.contains(disaTypes[0])) {
						agmeDisa.disaTypes.add(disaTypes[0]);
					}
				}
				
				for (int i = 0; i < tempDisas.length; i++) {
					sb.append(tempDisas[i]+"@\n");
				}
				sb.append(reports[disa_count]+"@\n");
				reports = ArrayUtils.subarray(reports, disa_count+1, reports.length);
				agmeHeader.setCropType(disaTypes[0]);
				parseResult.put(new ReportInfo<AgmeReportHeader>(agmeHeader, sb.toString()));
				if(reports.length >= 3) {
					processReports(reports, agmeHeader, report_header);
				}
				break;
				
			case "DISA-04":
				tempDisas = ArrayUtils.subarray(reports, 0, disa_count);
				if(processDisa04(tempDisas, agmeHeader) > 0) {
					if(!agmeDisa.disaTypes.contains(disaTypes[0])) {
						agmeDisa.disaTypes.add(disaTypes[0]);
					}
				}
				
				for (int i = 0; i < tempDisas.length; i++) {
					sb.append(tempDisas[i]+"@\n");
				}
				sb.append(reports[disa_count]+"@\n");
				reports = ArrayUtils.subarray(reports, disa_count+1, reports.length);
				agmeHeader.setCropType(disaTypes[0]);
				parseResult.put(new ReportInfo<AgmeReportHeader>(agmeHeader, sb.toString()));
				if(reports.length >= 3) {
					processReports(reports, agmeHeader, report_header);
				}
				break;
				
			case "DISA-05":
				tempDisas = ArrayUtils.subarray(reports, 0, disa_count);
				if(processDisa05(tempDisas, agmeHeader) > 0) {
					if(!agmeDisa.disaTypes.contains(disaTypes[0])) {
						agmeDisa.disaTypes.add(disaTypes[0]);
					}
				}
				
				for (int i = 0; i < tempDisas.length; i++) {
					sb.append(tempDisas[i]+"@\n");
				}
				sb.append(reports[disa_count]+"@\n");
				reports = ArrayUtils.subarray(reports, disa_count+1, reports.length);
				agmeHeader.setCropType(disaTypes[0]);
				parseResult.put(new ReportInfo<AgmeReportHeader>(agmeHeader, sb.toString()));
				if(reports.length >= 3) {
					processReports(reports, agmeHeader, report_header);
				}
				break;	
			default:
				ReportError reportError = new ReportError();
				reportError.setMessage("Unrecognized message");
				reportError.setSegment(ArrayUtils.toString(reports));
				parseResult.put(reportError);
				break;
				
			}
			return true;
		} catch (Exception e) {
			ReportError reportError = new ReportError();
			reportError.setMessage("report segment format conversion error");
			reportError.setSegment(ArrayUtils.toString(reports));
			parseResult.put(reportError);
			return false;
		}
		
	}
	
	/**
	 * 1	农业气象灾害观测子要素	DISA-01	7 .
	 *
	 * @param tempDisas 农业气象灾害观测子要素报文
	 * @param agmeHeader 该段报文的头信息
	 * @return    int 解码成功的记录数
	 */
	private int processDisa01(String[] tempDisas, AgmeReportHeader agmeHeader) {
		int message_count = tempDisas.length;
		for (int i = 0; i < tempDisas.length; i++) {
			String[] items = tempDisas[i].split(",", 7);
			try {
				ZAgmeDisa01 zAgmeDisa01 = new ZAgmeDisa01();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				// 观测时间	14	日期	年月日时分秒
				zAgmeDisa01.setObservationDate(sdf.parse(items[0]));
				
				// 2019-7-16 cuihongyuan
				if(!TimeCheckUtil.checkTime(zAgmeDisa01.getObservationDate())){
					ReportError reportError = new ReportError();
					reportError.setMessage("check time error!");
					reportError.setSegment(tempDisas[i]);
					parseResult.put(reportError);
					continue;
				}
				
				agmeHeader.setReport_time(sdf.parse(items[0]));
				// 灾害名称	4	编码	详见《编码》灾害名称部分
				zAgmeDisa01.setDisaName(ElementValUtil.ToBeValidInt(items[1]));
				// 受灾作物	6	编码	详见《编码》作物名称部分
				zAgmeDisa01.setDisaCrop(ElementValUtil.ToBeValidInt(items[2]));
				// 器官受害程度	4	%	反映植株受灾的严重性
				zAgmeDisa01.setDegreeOfOrganDamage(ElementValUtil.ToBeValidInt(items[3]));
				// 预计对产量的影响	1	无	0为无；1为轻微；2为轻；3为中；4为重
				zAgmeDisa01.setExpectedImpactOnOutput(ElementValUtil.ToBeValidInt(items[4]));
				// 减产成数	2	成	减产程度估计
				zAgmeDisa01.setReductionInOutput(ElementValUtil.ToBeValidInt(items[5]));
				// 受害征状	50	字符	描述作物受灾的器官、部位、形态的变化
				zAgmeDisa01.setSymptomOfDisaster(items[6]);
				
				zAgmeDisa01.setStationNumberChina(agmeHeader.getStationNumberChina());
				zAgmeDisa01.setHeightOfPressureSensor(agmeHeader.getHeightOfBarometerAboveMeanSeaLevel());
				zAgmeDisa01.setHeightOfSationGroundAboveMeanSeaLevel(agmeHeader.getHeightOfSationGroundAboveMeanSeaLevel());
				zAgmeDisa01.setLatitude(agmeHeader.getLatitude());
				zAgmeDisa01.setLongitude(agmeHeader.getLongitude());
				
				agmeDisa.zAgmeDisa01s.add(zAgmeDisa01);
			}catch (Exception e) {
				
				ReportError reportError = new ReportError();
				reportError.setMessage("report segment format conversion error");
				reportError.setSegment(tempDisas[i]);
				parseResult.put(reportError);
				message_count = message_count-1;
				continue;
			}
		}
		return message_count;
	}
	
	/**
	 * 2	农业气象灾害调查子要素	DISA-02	8   .
	 *
	 * @param tempDisas 农业气象灾害调查子要素报文
	 * @param agmeHeader 该段报文的头信息
	 * @return int 解码成功的记录数
	 */
	private int processDisa02(String[] tempDisas, AgmeReportHeader agmeHeader) {
		int message_count = tempDisas.length;
		for (int i = 0; i < tempDisas.length; i++) {
			String[] items = tempDisas[i].split(",", 8);
			try {
				ZAgmeDisa02 zAgmeDisa02 = new ZAgmeDisa02();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				// 调查时间	14	日期	年月日时分秒
				zAgmeDisa02.setObservationDate(sdf.parse(items[0]));
				
				// 2019-7-16 cuihongyuan
				if(!TimeCheckUtil.checkTime(zAgmeDisa02.getObservationDate())){
					ReportError reportError = new ReportError();
					reportError.setMessage("check time error!");
					reportError.setSegment(tempDisas[i]);
					parseResult.put(reportError);
					continue;
				}
				
				agmeHeader.setReport_time(sdf.parse(items[0]));
				// 灾害名称	4	编码	详见《编码》灾害名称部分
				zAgmeDisa02.setDisaName(ElementValUtil.ToBeValidInt(items[1]));
				// 受灾作物	6	编码	详见《编码》作物名称部分
				zAgmeDisa02.setDisaCrop(ElementValUtil.ToBeValidInt(items[2]));
				// 器官受害程度	4	%	反映植株受灾的严重性
				zAgmeDisa02.setDegreeOfOrganDamage(ElementValUtil.ToBeValidInt(items[3]));
				// 成灾面积	6	0.1公顷	县内成灾面积
				zAgmeDisa02.setDisaArea((float)ElementValUtil.ToBeValidDouble(items[4], 0.1));
				// 成灾比例	4	0.1%	县内成灾比例
				zAgmeDisa02.setDisaPercentage((float)ElementValUtil.ToBeValidDouble(items[5], 0.1));
				// 减产百分率	2	成	县内减产趋势估计
				zAgmeDisa02.setReductionPercentage(ElementValUtil.ToBeValidInt(items[6]));
				// 受害征状	50	字符	描述作物受灾的器官、部位、形态的变化
				zAgmeDisa02.setSymptomOfDisaster(items[7]);
				
				zAgmeDisa02.setStationNumberChina(agmeHeader.getStationNumberChina());
				zAgmeDisa02.setHeightOfPressureSensor(agmeHeader.getHeightOfBarometerAboveMeanSeaLevel());
				zAgmeDisa02.setHeightOfSationGroundAboveMeanSeaLevel(agmeHeader.getHeightOfSationGroundAboveMeanSeaLevel());
				zAgmeDisa02.setLatitude(agmeHeader.getLatitude());
				zAgmeDisa02.setLongitude(agmeHeader.getLongitude());
				
				agmeDisa.zAgmeDisa02s.add(zAgmeDisa02);
			}
			catch (Exception e) {
				
				ReportError reportError = new ReportError();
				reportError.setMessage("report segment format conversion error");
				reportError.setSegment(tempDisas[i]);
				parseResult.put(reportError);
				message_count = message_count-1;
				continue;
			}
		}
		return message_count;	
	}
	
	/**
	 * 3	牧草灾害子要素	DISA-03	6  .
	 *
	 * @param tempDisas 牧草灾害子要素报文
	 * @param agmeHeader 该段报文的头信息
	 * @return int 解码成功的记录数
	 */
	private int processDisa03(String[] tempDisas, AgmeReportHeader agmeHeader) {
		int message_count = tempDisas.length;
		for (int i = 0; i < tempDisas.length; i++) {
			String[] items = tempDisas[i].split(",", 6);
			try {
				ZAgmeDisa03 zAgmeDisa03 = new ZAgmeDisa03();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				// 观测时间	14	日期	年月日时分秒
				zAgmeDisa03.setObservationDate(sdf.parse(items[0]));
				
				// 2019-7-16 cuihongyuan
				if(!TimeCheckUtil.checkTime(zAgmeDisa03.getObservationDate())){
					ReportError reportError = new ReportError();
					reportError.setMessage("check time error!");
					reportError.setSegment(tempDisas[i]);
					parseResult.put(reportError);
					continue;
				}
				
				agmeHeader.setReport_time(sdf.parse(items[0]));
				// 起始时间	14	日期	年月日时分秒
				zAgmeDisa03.setStartTime(sdf.parse(items[1]));
				// 终止时间	14	日期	年月日时分秒
				zAgmeDisa03.setEndTime(sdf.parse(items[2]));
				// 灾害名称	4	编码	详见《编码》灾害名称部分
				zAgmeDisa03.setDisaName(ElementValUtil.ToBeValidInt(items[3]));
				// 受害等级	1	无	1为轻；2为中；3为重；4为很重
				zAgmeDisa03.setDisaLevel(ElementValUtil.ToBeValidInt(items[4]));
				// 受害征状	50	字符	描述牧草受灾情况
				zAgmeDisa03.setSymptomOfDisaster(items[5]);
				
				zAgmeDisa03.setStationNumberChina(agmeHeader.getStationNumberChina());
				zAgmeDisa03.setHeightOfPressureSensor(agmeHeader.getHeightOfBarometerAboveMeanSeaLevel());
				zAgmeDisa03.setHeightOfSationGroundAboveMeanSeaLevel(agmeHeader.getHeightOfSationGroundAboveMeanSeaLevel());
				zAgmeDisa03.setLatitude(agmeHeader.getLatitude());
				zAgmeDisa03.setLongitude(agmeHeader.getLongitude());
				
				agmeDisa.zAgmeDisa03s.add(zAgmeDisa03);
			}
			catch (Exception e) {
				
				ReportError reportError = new ReportError();
				reportError.setMessage("report segment format conversion error");
				reportError.setSegment(tempDisas[i]);
				parseResult.put(reportError);
				message_count = message_count-1;
				continue;
			}
		}
		return message_count;	
	}
	
	/**
	 * 4	家畜灾害子要素	DISA-04	6   .
	 *
	 * @param tempDisas 家畜灾害子要素报文
	 * @param agmeHeader 该段报文的头信息
	 * @return int 解码成功的记录数
	 */
	private int processDisa04(String[] tempDisas, AgmeReportHeader agmeHeader) {
		int message_count = tempDisas.length;
		for (int i = 0; i < tempDisas.length; i++) {
			String[] items = tempDisas[i].split(",", 6);
			try {
				ZAgmeDisa04 zAgmeDisa04 = new ZAgmeDisa04();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				// 观测时间	14	日期	年月日时分秒
				zAgmeDisa04.setObservationDate(sdf.parse(items[0]));
				
				// 2019-7-16 cuihongyuan
				if(!TimeCheckUtil.checkTime(zAgmeDisa04.getObservationDate())){
					ReportError reportError = new ReportError();
					reportError.setMessage("check time error!");
					reportError.setSegment(tempDisas[i]);
					parseResult.put(reportError);
					continue;
				}
				
				agmeHeader.setReport_time(sdf.parse(items[0]));
				// 起始时间	14	日期	年月日时分秒
				zAgmeDisa04.setStartTime(sdf.parse(items[1]));
				// 终止时间	14	日期	年月日时分秒
				zAgmeDisa04.setEndTime(sdf.parse(items[2]));
				// 灾害名称	4	编码	编码，详见《编码》灾害名称部分
				zAgmeDisa04.setDisaName(ElementValUtil.ToBeValidInt(items[3]));
				// 受害等级	1	无	1为轻；2为中；3为重；4为很重
				zAgmeDisa04.setDisaLevel(ElementValUtil.ToBeValidInt(items[4]));
				// 受害征状	50	字符	描述家畜受灾情况
				zAgmeDisa04.setSymptomOfDisaster(items[5]);
				
				zAgmeDisa04.setStationNumberChina(agmeHeader.getStationNumberChina());
				zAgmeDisa04.setHeightOfPressureSensor(agmeHeader.getHeightOfBarometerAboveMeanSeaLevel());
				zAgmeDisa04.setHeightOfSationGroundAboveMeanSeaLevel(agmeHeader.getHeightOfSationGroundAboveMeanSeaLevel());
				zAgmeDisa04.setLatitude(agmeHeader.getLatitude());
				zAgmeDisa04.setLongitude(agmeHeader.getLongitude());
				
				agmeDisa.zAgmeDisa04s.add(zAgmeDisa04);
			}
			catch (Exception e) {
				
				ReportError reportError = new ReportError();
				reportError.setMessage("report segment format conversion error");
				reportError.setSegment(tempDisas[i]);
				parseResult.put(reportError);
				message_count = message_count-1;
				continue;
			}
		}
		return message_count;
	}
	
	/**
	 * 5	植物灾害子要素	DISA-05	7   .
	 *
	 * @param tempDisas 植物灾害子要素报文
	 * @param agmeHeader 该段报文的头信息
	 * @return int 解码成功的记录数
	 */
	private int processDisa05(String[] tempDisas, AgmeReportHeader agmeHeader) {
		int message_count = tempDisas.length;
		for (int i = 0; i < tempDisas.length; i++) {
			String[] items = tempDisas[i].split(",", 7);
			try {
				ZAgmeDisa05 zAgmeDisa05 = new ZAgmeDisa05();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				// 观测时间	14	日期	年月日时分秒
				zAgmeDisa05.setObservationDate(sdf.parse(items[0]));
				// 2019-7-16 cuihongyuan
				if(!TimeCheckUtil.checkTime(zAgmeDisa05.getObservationDate())){
					ReportError reportError = new ReportError();
					reportError.setMessage("check time error!");
					reportError.setSegment(tempDisas[i]);
					parseResult.put(reportError);
					continue;
				}
				
				agmeHeader.setReport_time(sdf.parse(items[0]));
				// 起始时间	14	日期	年月日时分秒
				zAgmeDisa05.setStartTime(sdf.parse(items[1]));
				// 终止时间	14	日期	年月日时分秒
				zAgmeDisa05.setEndTime(sdf.parse(items[2]));
				// 受害植物	8	编码	详见《编码》植物动物名称部分
				zAgmeDisa05.setDamagePlant(ElementValUtil.ToBeValidInt(items[3]));
				// 灾害名称	4	编码	详见《编码》灾害名称部分
				zAgmeDisa05.setDisaName(ElementValUtil.ToBeValidInt(items[4]));
				// 受害程度	4	%	反映植株受灾的严重性
				zAgmeDisa05.setDegreeOfDamage(ElementValUtil.ToBeValidInt(items[5]));
				// 受害征状	50	字符	描述家畜受灾情况
				zAgmeDisa05.setSymptomOfDisaster(items[6]);
				
				zAgmeDisa05.setStationNumberChina(agmeHeader.getStationNumberChina());
				zAgmeDisa05.setHeightOfPressureSensor(agmeHeader.getHeightOfBarometerAboveMeanSeaLevel());
				zAgmeDisa05.setHeightOfSationGroundAboveMeanSeaLevel(agmeHeader.getHeightOfSationGroundAboveMeanSeaLevel());
				zAgmeDisa05.setLatitude(agmeHeader.getLatitude());
				zAgmeDisa05.setLongitude(agmeHeader.getLongitude());
				
				agmeDisa.zAgmeDisa05s.add(zAgmeDisa05);
			}
			catch (Exception e) {
				
				ReportError reportError = new ReportError();
				reportError.setMessage("report segment format conversion error");
				reportError.setSegment(tempDisas[i]);
				parseResult.put(reportError);
				message_count = message_count-1;
				continue;
			}
		}
		return message_count;
	}
//	
//	public static void main(String[] args) {
//		DecodeDisa decodeDisa = new DecodeDisa();
//		ParseResult<ZAgmeDisa> parseResult = decodeDisa.decodeFile(new File("D:\\HUAXIN\\DataProcess\\E.0003.0005.R001\\Z_AGME_C_BCLZ_20180428033600_O_DISA.TXT"));
//		List<ZAgmeDisa> list = parseResult.getData();
//		System.out.println(list.size());
//		System.out.println(list.get(0).disaTypes);
//	}
	
}
