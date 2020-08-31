package cma.cimiss2.dpc.decoder.agme;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.ArrayUtils;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCrop;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCrop01;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCrop02;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCrop03;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCrop04;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCrop05;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCrop06;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCrop07;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCrop08;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCrop09;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCrop10;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.ElementValUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;
import cma.cimiss2.dpc.decoder.tools.enumeration.ObservationMethod;

// TODO: Auto-generated Javadoc
/**
* *******************************************************************************************<br>
* <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
* *******************************************************************************************<br>
* <b>Description:作物要素数据文件,包括作物生长发育、作物生长量、作物产量因素、作物产量结构、关键农事活动、本地产量水平和大田生育状况调查等信息</b><br> 
* 单站上传文件命名规则： Z_AGME_I_IIiii_YYYYMMDDhhmmss_O_CROP[-CCx].txt<br> 
* 多站上传文件命名规则：Z_AGME_C_CCCC_YYYYMMDDhhmmss_O_CROP.txt<br> 
* 作物要素数据文件，正文由10个子要素组成<br>
* <ul>
* <li>1	作物生长发育		CROP-01	要素个数 8<br>
* <li>2	叶面积指数	    	CROP-02	要素个数  6<br>
* <li>3	灌浆速度	    	CROP-03	要素个数  5<br>
* <li>4	产量因素	    	CROP-04	要素个数  5<br>
* <li>5	产量结构	    	CROP-05	要素个数  4<br>
* <li>6	关键农事活动		CROP-06	要素个数  6<br>
* <li>7	县产量水平	    	CROP-07	要素个数  5<br>
* <li>8	植株分器官干物质重量	CROP-08	要素个数  10<br>
* <li>9	大田生育状况基本情况	CROP-09	要素个数  5<br>
* <li>10 大田生育状况调查内容	CROP-10	要素个数  13<br>
* </ul>
* @author wuzuoqiang 
* @version 1.0
* @Note
* <b>ProjectName:</b> cimiss2-decode
* <br><b>PackageName:</b> cma.cimiss2.dpc.decoder.agme
* <br><b>ClassName:</b> DecodeCrop
* <br><b>Date:</b> 2019年4月3日 下午11:17:24
*/

public class DecodeCrop {
	
	/** 存放数据解析的结果集. */
	private ParseResult<ZAgmeCrop> parseResult = new ParseResult<ZAgmeCrop>(false);
	
	/** 解码对象封装. */
	private ZAgmeCrop agmeCrop = new ZAgmeCrop();
	
	/**
	 * 作物要素数据文件解码函数 .
	 *
	 * @param file 文件对象
	 * @return ParseResult<ZAgmeCrop>  解码结果集包裹对象
	 */
	@SuppressWarnings("resource")
	public ParseResult<ZAgmeCrop> decodeFile(File file) {
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
		        fileCode = fileCode.equalsIgnoreCase("ISO8859_1") ? "GBK" : fileCode;
		    	fileCode = fileCode.equalsIgnoreCase("GB2312") ? "GBK" : fileCode;
		    	fileCode = fileCode.equalsIgnoreCase("ASCII") ? "GBK" : fileCode;
		    	
				read = new InputStreamReader(new FileInputStream(file), fileCode);
				scanner = new Scanner(read).useDelimiter("\n=");
				while (scanner.hasNext()) {
					// 获取一个完整的报文段
					String report = scanner.next();
					// 去掉无效的空格换行
//					Pattern p = Pattern.compile("\\s*|\t|\r|\n");
					Pattern p = Pattern.compile("\t|\r|\n");
					Matcher m = p.matcher(report);
					report = m.replaceAll("");
					if(report.equals("") || report.length() < 1)
						continue;
					

					if(report.trim().equalsIgnoreCase("NNNN")) {
						// 报文结束
						break;
					}
					report = report.replace("NNNN", "");
					// 数据第一行为报文头信息
					String[] reports = report.split("@");
					/* added by Cuihongyuan, the following 6 lines*/
					// cannot be removed
					char s =report.trim().charAt(0);   
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
							/* modified by Cuihongyuan, the following 6 lines*/
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
							reportError.setMessage("Header error");
							reportError.setSegment(report);
							parseResult.put(reportError);
							continue;
						}
						
					}else {  // 报文头信息错误
						ReportError reportError = new ReportError();
						reportError.setMessage("Header error");
						reportError.setSegment(report);
						parseResult.put(reportError);
					}
					
				}
				parseResult.put(agmeCrop);
				
			} catch (UnsupportedEncodingException e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			} catch (FileNotFoundException e) {
				parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
			} catch (Exception e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			} finally {
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
	 * 分割报文处理函数 .
	 *
	 * @param reports  报文
	 * @param agmeReportHeader the agme report header
	 * @param report_header  报文头信息字符串
	 * @return    boolean  处理是否成功
	 */
	private boolean processReports(String[] reports, AgmeReportHeader agmeReportHeader, String report_header) {
		AgmeReportHeader agmeHeader = new AgmeReportHeader(agmeReportHeader);
		StringBuilder sb = new StringBuilder();
		sb.append(report_header+"@\n");
		try {
			String[] cropTypes = reports[0].split(",");
			sb.append(reports[0] + "@\n");
//			int crop_count = Integer.parseInt(cropTypes[1]);
			int ii = 0;
			while(ii < reports.length){
				if(!reports[ii].toUpperCase().startsWith("END_CROP"))
					ii ++;
				else
					break;
			}
			int crop_count = ii - 1;
			
			reports = ArrayUtils.remove(reports, 0);
			String[] tempCrops;
			switch (cropTypes[0]) {
			
			case "CROP-01":
				tempCrops = ArrayUtils.subarray(reports, 0, crop_count);
				if(processCrop01(tempCrops, agmeHeader) > 0) {
					if(!agmeCrop.cropTypes.contains(cropTypes[0])) {
						agmeCrop.cropTypes.add(cropTypes[0]);
					}
				}
				
				for (int i = 0; i < tempCrops.length; i++) {
					sb.append(tempCrops[i]+"@\n");
				}
				sb.append(reports[crop_count]+"@\n");
				reports = ArrayUtils.subarray(reports, crop_count+1, reports.length);
				agmeHeader.setCropType(cropTypes[0]);
				parseResult.put(new ReportInfo<AgmeReportHeader>(agmeHeader, sb.toString()));
				if(reports.length >= 3) {
					processReports(reports, agmeHeader, report_header);
				}
				break;
			case "CROP-02":
				tempCrops = ArrayUtils.subarray(reports, 0, crop_count);
				if(processCrop02(tempCrops, agmeHeader) > 0) {
					if(!agmeCrop.cropTypes.contains(cropTypes[0])) {
						agmeCrop.cropTypes.add(cropTypes[0]);
					}
				}
				
				for (int i = 0; i < tempCrops.length; i++) {
					sb.append(tempCrops[i]+"@\n");
				}
				sb.append(reports[crop_count]+"@\n");
				reports = ArrayUtils.subarray(reports, crop_count+1, reports.length);
				agmeHeader.setCropType(cropTypes[0]);
				parseResult.put(new ReportInfo<AgmeReportHeader>(agmeHeader, sb.toString()));
				if(reports.length >= 3) {
					processReports(reports, agmeHeader, report_header);
				}
				break;
				
			case "CROP-03":
				tempCrops = ArrayUtils.subarray(reports, 0, crop_count);
				if(processCrop03(tempCrops, agmeHeader) > 0) {
					if(!agmeCrop.cropTypes.contains(cropTypes[0])) {
						agmeCrop.cropTypes.add(cropTypes[0]);
					}
				}
				for (int i = 0; i < tempCrops.length; i++) {
					sb.append(tempCrops[i]+"@\n");
				}
				sb.append(reports[crop_count]+"@\n");
				reports = ArrayUtils.subarray(reports, crop_count+1, reports.length);
				agmeHeader.setCropType(cropTypes[0]);
				parseResult.put(new ReportInfo<AgmeReportHeader>(agmeHeader, sb.toString()));
				if(reports.length >= 3) {
					processReports(reports, agmeHeader, report_header);
				}
				break;
				
			case "CROP-04":
				tempCrops = ArrayUtils.subarray(reports, 0, crop_count);
				if(processCrop04(tempCrops, agmeHeader) > 0) {
					if(!agmeCrop.cropTypes.contains(cropTypes[0])) {
						agmeCrop.cropTypes.add(cropTypes[0]);
					}
				}
				for (int i = 0; i < tempCrops.length; i++) {
					sb.append(tempCrops[i]+"@\n");
				}
				sb.append(reports[crop_count]+"@\n");
				reports = ArrayUtils.subarray(reports, crop_count+1, reports.length);
				agmeHeader.setCropType(cropTypes[0]);
				parseResult.put(new ReportInfo<AgmeReportHeader>(agmeHeader, sb.toString()));
				if(reports.length >= 3) {
					processReports(reports, agmeHeader, report_header);
				}
				break;
				
			case "CROP-05":
				tempCrops = ArrayUtils.subarray(reports, 0, crop_count);
				if(processCrop05(tempCrops, agmeHeader) > 0) {
					if(!agmeCrop.cropTypes.contains(cropTypes[0])) {
						agmeCrop.cropTypes.add(cropTypes[0]);
					}
				}
				for (int i = 0; i < tempCrops.length; i++) {
					sb.append(tempCrops[i]+"@\n");
				}
				sb.append(reports[crop_count]+"@\n");
				reports = ArrayUtils.subarray(reports, crop_count+1, reports.length);
				agmeHeader.setCropType(cropTypes[0]);
				parseResult.put(new ReportInfo<AgmeReportHeader>(agmeHeader, sb.toString()));
				if(reports.length >= 3) {
					processReports(reports, agmeHeader, report_header);
				}
				break;
			
			case "CROP-06":
				tempCrops = ArrayUtils.subarray(reports, 0, crop_count);
				if(processCrop06(tempCrops, agmeHeader) > 0) {
					if(!agmeCrop.cropTypes.contains(cropTypes[0])) {
						agmeCrop.cropTypes.add(cropTypes[0]);
					}
				}
				for (int i = 0; i < tempCrops.length; i++) {
					sb.append(tempCrops[i]+"@\n");
				}
				sb.append(reports[crop_count]+"@\n");
				reports = ArrayUtils.subarray(reports, crop_count+1, reports.length);
				agmeHeader.setCropType(cropTypes[0]);
				parseResult.put(new ReportInfo<AgmeReportHeader>(agmeHeader, sb.toString()));
				if(reports.length >= 3) {
					processReports(reports, agmeHeader, report_header);
				}
				break;
				
			case "CROP-07":
				tempCrops = ArrayUtils.subarray(reports, 0, crop_count);
				if(processCrop07(tempCrops, agmeHeader) > 0) {
					if(!agmeCrop.cropTypes.contains(cropTypes[0])) {
						agmeCrop.cropTypes.add(cropTypes[0]);
					}
				}
				for (int i = 0; i < tempCrops.length; i++) {
					sb.append(tempCrops[i]+"@\n");
				}
				sb.append(reports[crop_count]+"@\n");
				reports = ArrayUtils.subarray(reports, crop_count+1, reports.length);
				agmeHeader.setCropType(cropTypes[0]);
				parseResult.put(new ReportInfo<AgmeReportHeader>(agmeHeader, sb.toString()));
				if(reports.length >= 3) {
					processReports(reports, agmeHeader, report_header);
				}
				break;
			
			case "CROP-08":
				tempCrops = ArrayUtils.subarray(reports, 0, crop_count);
				if(processCrop08(tempCrops, agmeHeader) > 0) {
					if(!agmeCrop.cropTypes.contains(cropTypes[0])) {
						agmeCrop.cropTypes.add(cropTypes[0]);
					}
				}
				for (int i = 0; i < tempCrops.length; i++) {
					sb.append(tempCrops[i]+"@\n");
				}
				sb.append(reports[crop_count]+"@\n");
				reports = ArrayUtils.subarray(reports, crop_count+1, reports.length);
				agmeHeader.setCropType(cropTypes[0]);
				parseResult.put(new ReportInfo<AgmeReportHeader>(agmeHeader, sb.toString()));
				if(reports.length >= 3) {
					processReports(reports, agmeHeader, report_header);
				}
				break;
			
			case "CROP-09":
				tempCrops = ArrayUtils.subarray(reports, 0, crop_count);
				if(processCrop09(tempCrops, agmeHeader) > 0) {
					if(!agmeCrop.cropTypes.contains(cropTypes[0])) {
						agmeCrop.cropTypes.add(cropTypes[0]);
					}
					
					for (int i = 0; i < tempCrops.length; i++) {
						sb.append(tempCrops[i]+"@\n");
					}
					
					sb.append(reports[crop_count]+"@\n");
					reports = ArrayUtils.subarray(reports, crop_count+1, reports.length);
					agmeHeader.setCropType(cropTypes[0]);
					parseResult.put(new ReportInfo<AgmeReportHeader>(agmeHeader, sb.toString()));
					if(reports.length >= 3) {
						processReports(reports, agmeHeader, report_header);
					}
				}
				break;
			
			case "CROP-10":
				tempCrops = ArrayUtils.subarray(reports, 0, crop_count);
				if(processCrop10(tempCrops, agmeHeader) > 0) {
					if(!agmeCrop.cropTypes.contains(cropTypes[0])) {
						agmeCrop.cropTypes.add(cropTypes[0]);
					}
				}
				for (int i = 0; i < tempCrops.length; i++) {
					sb.append(tempCrops[i]+"@\n");
				}
				sb.append(reports[crop_count]+"@\n");
				reports = ArrayUtils.subarray(reports, crop_count+1, reports.length);
				agmeHeader.setCropType(cropTypes[0]);
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
			reportError.setMessage("Message segment format conversion error");
			reportError.setSegment(ArrayUtils.toString(reports));
			parseResult.put(reportError);
			return false;
		}
		
	}
	
	/**
	 * 10	大田生育状况调查内容	CROP-10	15 .
	 *
	 * @param tempCrops 大田生育状况调查内容子要素报文段
	 * @param agmeHeader  大田生育状况调查内容报文头信息
	 * @return int 解析出的对象个数
	 */
	private int processCrop10(String[] tempCrops, AgmeReportHeader agmeHeader) {
		int message_count = tempCrops.length;
		for (int i = 0; i < tempCrops.length; i++) {
			String[] items = tempCrops[i].split(",", 15);
			try {
				ZAgmeCrop10 zAgmeCrop10 = new ZAgmeCrop10();
				// 观测日期	14	日期	年月日时分秒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				zAgmeCrop10.setObservationDate(sdf.parse(items[0]));
				agmeHeader.setReport_time(sdf.parse(items[0]));
				if(!TimeCheckUtil.checkTime(zAgmeCrop10.getObservationDate())){
					ReportError re = new ReportError();
					re.setMessage("time check error!");
					re.setSegment(tempCrops[i]);
					parseResult.put(re);
					continue;
				}
				/* modified by Cuihongyuan, the ElementValUtil.toBeValid() Parts*/
				// 大田水平	1	无	0为上等田；1为中等田；2为下等田
				zAgmeCrop10.setFieldLevel(ElementValUtil.ToBeValidInt(items[1]));
				// 作物名称	6	编码	详见《编码》作物名称部分
				zAgmeCrop10.setCropName(ElementValUtil.ToBeValidInt(items[2]));
				// 发育期	2	编码	详见《编码》作物发育期部分
				zAgmeCrop10.setPeriodOfGrowth(ElementValUtil.ToBeValidInt(items[3]));
				// 植株高度	4	厘米（cm）
				zAgmeCrop10.setPlantHeight((float)ElementValUtil.ToBeValidDouble(items[4]));
				// 植株密度	8	0.01株（茎）数/平方米				
				zAgmeCrop10.setPlantDensity((float)ElementValUtil.ToBeValidDouble(items[5], 0.01));				
				// 生长状况	1	无	1为一类苗；2为二类苗；3为三类苗
				zAgmeCrop10.setGrowthState(ElementValUtil.ToBeValidInt(items[6]));
				// 产量因素名称1	2	编码	详见《编码》作物产量因素部分
				zAgmeCrop10.setOutputFactorName1(ElementValUtil.ToBeValidInt(items[7]));
				// 产量因素测量值1	8	单位  0.01
				zAgmeCrop10.setOutputFactorMeasureValue1((float) ElementValUtil.ToBeValidDouble(items[8], 0.01));
				// 产量因素名称2	2	无			
				zAgmeCrop10.setOutputFactorName2(ElementValUtil.ToBeValidInt(items[9]));			
				// 产量因素测量值2	8	单位 ：0.01
				zAgmeCrop10.setOutputFactorMeasureValue2((float) ElementValUtil.ToBeValidDouble(items[10], 0.01));
				// 产量因素名称3	2	无
				zAgmeCrop10.setOutputFactorName3(ElementValUtil.ToBeValidInt(items[11]));
				// 产量因素测量值3	8	单位 ：0.01
				zAgmeCrop10.setOutputFactorMeasureValue3((float) ElementValUtil.ToBeValidDouble(items[12], 0.01));
				// 产量因素名称3	2	无
				zAgmeCrop10.setOutputFactorName4(ElementValUtil.ToBeValidInt(items[13]));
				// 产量因素测量值4	8	单位 ：0.01
				zAgmeCrop10.setOutputFactorMeasureValue4((float) ElementValUtil.ToBeValidDouble(items[14], 0.01));
				
				zAgmeCrop10.setStationNumberChina(agmeHeader.getStationNumberChina());
				zAgmeCrop10.setHeightOfPressureSensor(agmeHeader.getHeightOfBarometerAboveMeanSeaLevel());
				zAgmeCrop10.setHeightOfSationGroundAboveMeanSeaLevel(agmeHeader.getHeightOfSationGroundAboveMeanSeaLevel());
				zAgmeCrop10.setLatitude(agmeHeader.getLatitude());
				zAgmeCrop10.setLongitude(agmeHeader.getLongitude());
				
				agmeCrop.zAgmeCrop10s.add(zAgmeCrop10);
			} catch (Exception e) {
				
				ReportError reportError = new ReportError();
				reportError.setMessage("Message segment format conversion error");
				reportError.setSegment(tempCrops[i]);
				parseResult.put(reportError);
				message_count = message_count-1;
				continue;
			}
		}
		return message_count;
	}
	
	/**
	 * 大田生育状况基本情况	CROP-09	5   .
	 *
	 * @param tempCrops  待解析报文段
	 * @param agmeHeader 该段报文的头信息
	 * @return int 解析出的对象个数
	 */
	private int processCrop09(String[] tempCrops, AgmeReportHeader agmeHeader) {
		int message_count = tempCrops.length;
		
		for (int i = 0; i < tempCrops.length; i++) {
			if(tempCrops[i].split(",").length > 5){
				ReportError reportError = new ReportError();
				reportError.setMessage("Message segment format conversion error");
				reportError.setSegment(tempCrops[i]);
				parseResult.put(reportError);
				message_count = message_count-1;
				continue;
			}
			String[] items = tempCrops[i].split(",", 5);
			try {
				ZAgmeCrop09 zAgmeCrop09 = new ZAgmeCrop09();
				// 大田水平	1	无	0为上等田；1为中等田；2为下等田
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				
				/* modified by Cuihongyuan, the ElementValUtil.toBeValid() Parts*/
				zAgmeCrop09.setFieldLevel(ElementValUtil.ToBeValidInt(items[1]));
				// 作物名称	6	编码	详见《编码》作物名称部分
				zAgmeCrop09.setCropName(ElementValUtil.ToBeValidInt(items[0]));
				// 				播种时间	14	日期	
				zAgmeCrop09.setSeedingTime(sdf.parse(items[2]));
				
				// 2019-7-16 cuihongyuan
				if(!TimeCheckUtil.checkTime(zAgmeCrop09.getSeedingTime())){
					ReportError re = new ReportError();
					re.setMessage("time check error!");
					re.setSegment(tempCrops[i]);
					parseResult.put(re);
					continue;
				}
				
				
				agmeHeader.setReport_time(sdf.parse(items[2]));
				// 				收获时间	14	日期	
				try{
					zAgmeCrop09.setHarvestTime(sdf.parse(items[3]));
				}catch (Exception e) {
					zAgmeCrop09.setHarvestTime(sdf.parse(items[2]));
					ReportError reportError = new ReportError();
					reportError.setMessage("Harvest Time parse error, put in Seeding time!");
					reportError.setSegment(tempCrops[i]);
					parseResult.put(reportError);
				}
				// 				单产	6	单位 0.1公斤/公顷	收获单产
				zAgmeCrop09.setUnitOuput((float) ElementValUtil.ToBeValidDouble(items[4], 0.1));
				zAgmeCrop09.setStationNumberChina(agmeHeader.getStationNumberChina());
				zAgmeCrop09.setHeightOfPressureSensor(agmeHeader.getHeightOfBarometerAboveMeanSeaLevel());
				zAgmeCrop09.setHeightOfSationGroundAboveMeanSeaLevel(agmeHeader.getHeightOfSationGroundAboveMeanSeaLevel());
				zAgmeCrop09.setLatitude(agmeHeader.getLatitude());
				zAgmeCrop09.setLongitude(agmeHeader.getLongitude());
				
				agmeCrop.zAgmeCrop09s.add(zAgmeCrop09);
			} catch (Exception e) {
				ReportError reportError = new ReportError();
				reportError.setMessage("Message segment format conversion error");
				reportError.setSegment(tempCrops[i]);
				parseResult.put(reportError);
				message_count = message_count-1;
				continue;
			}
		}
		return message_count;
	}
	
	/**
	 * 8	植株分器官干物质重量	CROP-08	10   .
	 *
	 * @param tempCrops 待解析报文段
	 * @param agmeHeader 该段报文的头信息
	 * @return int 解析出的对象个数
	 */
	private int processCrop08(String[] tempCrops, AgmeReportHeader agmeHeader) {
		int message_count = tempCrops.length;
		for (int i = 0; i < tempCrops.length; i++) {
			String[] items = tempCrops[i].split(",", 10);
			try {
				ZAgmeCrop08 zAgmeCrop08 = new ZAgmeCrop08();
				// 测定时间	14	日期	年月日时分秒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				zAgmeCrop08.setObservationDate(sdf.parse(items[0]));
				// 2019-7-16 cuihongyuan
				if(!TimeCheckUtil.checkTime(zAgmeCrop08.getObservationDate())){
					ReportError re = new ReportError();
					re.setMessage("time check error!");
					re.setSegment(tempCrops[i]);
					parseResult.put(re);
					continue;
				}
				
				
				agmeHeader.setReport_time(sdf.parse(items[0]));
				/* modified by Cuihongyuan, the ElementValUtil.toBeValid() Parts*/
				// 作物名称	6	编码	详见《编码》作物名称部分
				zAgmeCrop08.setCropName(ElementValUtil.ToBeValidInt(items[1]));
				// 发育期	2	编码	详见《编码》作物发育期部分
				zAgmeCrop08.setPeriodOfGrowth(ElementValUtil.ToBeValidInt(items[2]));
				// 器官名称	1	无	0为整株；1为叶；2为叶鞘；3为茎；4为果实
				zAgmeCrop08.setOrganName(ElementValUtil.ToBeValidInt(items[3]));
				// 株茎鲜重	7	单位：0.001克	器官鲜重
				zAgmeCrop08.setPlantFreshWeight((float) ElementValUtil.ToBeValidDouble(items[4], 0.001));
				// 株茎干重	7	单位：0.001克	器官干重
				zAgmeCrop08.setPlantDryWeight((float) ElementValUtil.ToBeValidDouble(items[5], 0.001));
				// 平方米株茎鲜重	6	单位：0.1克/平方米	1平方米器官鲜重
				zAgmeCrop08.setAvgFreshWeightPerSquareMeter((float) ElementValUtil.ToBeValidDouble(items[6], 0.1));
				// 平方米株茎干重	6	单位：0.1克/平方米	1平方米器官干重
				zAgmeCrop08.setAvgDryWeightPerSquareMeter((float) ElementValUtil.ToBeValidDouble(items[7], 0.1));
				// 含水率	6	单位：0.1％	器官含水率
				zAgmeCrop08.setPercentageOfWater((float) ElementValUtil.ToBeValidDouble(items[8], 0.1));
				// 生长率	6	单位：0.1克/（平方米.日）	器官的干物质增长量
				zAgmeCrop08.setPercentageOfGrowth((float) ElementValUtil.ToBeValidDouble(items[9], 0.1));
				zAgmeCrop08.setStationNumberChina(agmeHeader.getStationNumberChina());
				zAgmeCrop08.setHeightOfPressureSensor(agmeHeader.getHeightOfBarometerAboveMeanSeaLevel());
				zAgmeCrop08.setHeightOfSationGroundAboveMeanSeaLevel(agmeHeader.getHeightOfSationGroundAboveMeanSeaLevel());
				zAgmeCrop08.setLatitude(agmeHeader.getLatitude());
				zAgmeCrop08.setLongitude(agmeHeader.getLongitude());
				agmeCrop.zAgmeCrop08s.add(zAgmeCrop08);
			} catch (Exception e) {
				ReportError reportError = new ReportError();
				reportError.setMessage("Message segment format conversion error");
				reportError.setSegment(tempCrops[i]);
				parseResult.put(reportError);
				message_count = message_count-1;
				continue;
			}
		}
		return message_count;
	}
	
	/**
	 * 7	县产量水平	CROP-07	5   .
	 *
	 * @param tempCrops the temp crops
	 * @param agmeHeader the agme header
	 * @return int 解析出的对象个数
	 */
	private int processCrop07(String[] tempCrops, AgmeReportHeader agmeHeader) {
		int message_count = tempCrops.length;
		for (int i = 0; i < tempCrops.length; i++) {
			String[] items = tempCrops[i].split(",", 5);
			try {
				ZAgmeCrop07 zAgmeCrop07 = new ZAgmeCrop07();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				/* modified by Cuihongyuan, the ElementValUtil.toBeValid() Parts*/
				// 年度	4	日期	年
				zAgmeCrop07.setYear(Integer.parseInt(items[0]));
				agmeHeader.setReport_time(sdf.parse(items[0] + "0101000000"));
				
				if(!TimeCheckUtil.checkTime(agmeHeader.getReport_time())){
					ReportError re = new ReportError();
					re.setMessage("time check error!");
					re.setSegment(tempCrops[i]);
					parseResult.put(re);
					continue;
				}
				
				// 作物名称	8	编码	详见《编码》（作物名称部分
				zAgmeCrop07.setCropName(ElementValUtil.ToBeValidInt(items[1]));
				// 测站产量水平	6	0.1公斤/公顷	观测场地产量水平
				zAgmeCrop07.setOutputLevelOfStation((float) ElementValUtil.ToBeValidDouble(items[2], 0.1));
				// 县平均单产	6	0.1公斤/公顷	测站所在县产量水平
				zAgmeCrop07.setCountyAverageOutput((float) ElementValUtil.ToBeValidDouble(items[3], 0.1));
				// 县产量增减产百分率	6	0.1%	测站所在县产量与上一年的增减情况
				zAgmeCrop07.setCountyOutputChangeRate((float) ElementValUtil.ToBeValidDouble(items[4], 0.1));
				zAgmeCrop07.setStationNumberChina(agmeHeader.getStationNumberChina());
				zAgmeCrop07.setHeightOfPressureSensor(agmeHeader.getHeightOfBarometerAboveMeanSeaLevel());
				zAgmeCrop07.setHeightOfSationGroundAboveMeanSeaLevel(agmeHeader.getHeightOfSationGroundAboveMeanSeaLevel());
				zAgmeCrop07.setLatitude(agmeHeader.getLatitude());
				zAgmeCrop07.setLongitude(agmeHeader.getLongitude());
				
				agmeCrop.zAgmeCrop07s.add(zAgmeCrop07);
			} catch (Exception e) {
				ReportError reportError = new ReportError();
				reportError.setMessage("Message segment format conversion error");
				reportError.setSegment(tempCrops[i]);
				parseResult.put(reportError);
				message_count = message_count - 1;
				continue;
			}
		}
		return message_count;
	}
	
	/**
	 * 6	关键农事活动	CROP-06	6  .
	 *
	 * @param tempCrops  待解析报文段
	 * @param agmeHeader   该段报文的头信息
	 * @return int 解析出的对象个数
	 */
	private int processCrop06(String[] tempCrops, AgmeReportHeader agmeHeader) {
		int message_count = tempCrops.length;
		for (int i = 0; i < tempCrops.length; i++) {
			String[] items = tempCrops[i].split(",", 6);
			try {
				ZAgmeCrop06 zAgmeCrop06 = new ZAgmeCrop06();
				// 起始时间	14	日期	年月日时分秒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				zAgmeCrop06.setStartTime(sdf.parse(items[0]));
				
				//2019-7-16 cuihongyuan
				if(!TimeCheckUtil.checkTime(zAgmeCrop06.getStartTime())){
					ReportError re = new ReportError();
					re.setMessage("time check error!");
					re.setSegment(tempCrops[i]);
					parseResult.put(re);
					continue;
				}
				
				agmeHeader.setReport_time(sdf.parse(items[0]));
				// 结束时间	14	日期	年月日时分秒
				zAgmeCrop06.setEndTime(sdf.parse(items[1]));
				
				/* modified by Cuihongyuan, the ElementValUtil.toBeValid() Parts*/
				// 作物名称	6	编码	详见《编码》（作物名称部分
				zAgmeCrop06.setCropName(ElementValUtil.ToBeValidInt(items[2]));
				// 项目名称	4	编码	详见《编码》田间工作部分。
				zAgmeCrop06.setItemName2(ElementValUtil.ToBeValidInt(items[3]));
				// 质量	1	无	1为较差；2为中等；3为优良
				zAgmeCrop06.setQuality(ElementValUtil.ToBeValidInt(items[4]));
				// 方法和工具	100	字符	文字描述
				zAgmeCrop06.setMethodAndTool(items[5]);
				zAgmeCrop06.setStationNumberChina(agmeHeader.getStationNumberChina());
				zAgmeCrop06.setHeightOfPressureSensor(agmeHeader.getHeightOfBarometerAboveMeanSeaLevel());
				zAgmeCrop06.setHeightOfSationGroundAboveMeanSeaLevel(agmeHeader.getHeightOfSationGroundAboveMeanSeaLevel());
				zAgmeCrop06.setLatitude(agmeHeader.getLatitude());
				zAgmeCrop06.setLongitude(agmeHeader.getLongitude());
				agmeCrop.zAgmeCrop06s.add(zAgmeCrop06);
			} catch (Exception e) {
				ReportError reportError = new ReportError();
				reportError.setMessage("Message segment format conversion error");
				reportError.setSegment(tempCrops[i]);
				parseResult.put(reportError);
				message_count = message_count -1;
				continue;
			}
		}
		return message_count;
	}
	
	/**
	 * 5	产量结构	CROP-05	4 .
	 *
	 * @param tempCrops the temp crops
	 * @param agmeHeader the agme header
	 * @return int 解析出的对象个数
	 */
	private int processCrop05(String[] tempCrops, AgmeReportHeader agmeHeader) {
		int message_count = tempCrops.length;
		for (int i = 0; i < tempCrops.length; i++) {
			String[] items = tempCrops[i].split(",", 4);
			try {
				ZAgmeCrop05 zAgmeCrop05 = new ZAgmeCrop05();
				// 测定时间	14	YYYY-MM-DD	年月日时分秒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				zAgmeCrop05.setObservationDate(sdf.parse(items[0]));
				
				//2019-7-16 cuihongyuan
				if(!TimeCheckUtil.checkTime(zAgmeCrop05.getObservationDate())){
					ReportError re = new ReportError();
					re.setMessage("time check error!");
					re.setSegment(tempCrops[i]);
					parseResult.put(re);
					continue;
				}
				
				agmeHeader.setReport_time(sdf.parse(items[0]));
				/* modified by Cuihongyuan, the ElementValUtil.toBeValid() Parts*/
				// 作物名称	6	编码	详见《编码》作物名称部分
				zAgmeCrop05.setCropName(ElementValUtil.ToBeValidInt(items[1]));
				// 项目名称	2	编码	详见《编码》作物产量结构部分
				zAgmeCrop05.setItemName1(ElementValUtil.ToBeValidInt(items[2]));
				// 测定值	8	0.01	各项目测值均保留2位小数
				zAgmeCrop05.setObservationValue((float) ElementValUtil.ToBeValidDouble(items[3], 0.01));
				zAgmeCrop05.setStationNumberChina(agmeHeader.getStationNumberChina());
				zAgmeCrop05.setHeightOfPressureSensor(agmeHeader.getHeightOfBarometerAboveMeanSeaLevel());
				zAgmeCrop05.setHeightOfSationGroundAboveMeanSeaLevel(agmeHeader.getHeightOfSationGroundAboveMeanSeaLevel());
				zAgmeCrop05.setLatitude(agmeHeader.getLatitude());
				zAgmeCrop05.setLongitude(agmeHeader.getLongitude());
				agmeCrop.zAgmeCrop05s.add(zAgmeCrop05);
			} catch (Exception e) {
				ReportError reportError = new ReportError();
				reportError.setMessage("Message segment format conversion error");
				reportError.setSegment(tempCrops[i]);
				parseResult.put(reportError);
				message_count = message_count-1;
				continue;
			}
		}
		return message_count;
	}
	
	/**
	 * 4	产量因素	CROP-04	5  .
	 *
	 * @param tempCrops the temp crops
	 * @param agmeHeader the agme header
	 * @return int 解析出的对象个数
	 */
	private int processCrop04(String[] tempCrops, AgmeReportHeader agmeHeader) {
		int message_count = tempCrops.length;
		for (int i = 0; i < tempCrops.length; i++) {
			String[] items = tempCrops[i].split(",", 5);
			try {
				ZAgmeCrop04 zAgmeCrop04 = new ZAgmeCrop04();
				// 测定时间	14	日期	年月日时分秒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				zAgmeCrop04.setObservationDate(sdf.parse(items[0]));
				
				//2019-7-16 cuihongyuan
				if(!TimeCheckUtil.checkTime(zAgmeCrop04.getObservationDate())){
					ReportError re = new ReportError();
					re.setMessage("time check error!");
					re.setSegment(tempCrops[i]);
					parseResult.put(re);
					continue;
				}
				
				agmeHeader.setReport_time(sdf.parse(items[0]));
				/* modified by Cuihongyuan, the ElementValUtil.toBeValid() Parts*/
				// 作物名称	6	编码	详见《编码》作物名称部分
				zAgmeCrop04.setCropName(ElementValUtil.ToBeValidInt(items[1]));
				// 发育期	2	编码	详见《编码》作物发育期部分
				zAgmeCrop04.setPeriodOfGrowth(ElementValUtil.ToBeValidInt(items[2]));
				// 项目名称	2	编码	详见《编码》作物产量因素部分
				zAgmeCrop04.setItemName1(ElementValUtil.ToBeValidInt(items[3]));
				// 测定值	8	0.01	各项目测值均保留2位小数
				zAgmeCrop04.setObservationValue((float) ElementValUtil.ToBeValidDouble(items[4], 0.01));
				zAgmeCrop04.setStationNumberChina(agmeHeader.getStationNumberChina());
				zAgmeCrop04.setHeightOfPressureSensor(agmeHeader.getHeightOfBarometerAboveMeanSeaLevel());
				zAgmeCrop04.setHeightOfSationGroundAboveMeanSeaLevel(agmeHeader.getHeightOfSationGroundAboveMeanSeaLevel());
				zAgmeCrop04.setLatitude(agmeHeader.getLatitude());
				zAgmeCrop04.setLongitude(agmeHeader.getLongitude());
				
				agmeCrop.zAgmeCrop04s.add(zAgmeCrop04);
			} catch (Exception e) {
				ReportError reportError = new ReportError();
				reportError.setMessage("Message segment format conversion error");
				reportError.setSegment(tempCrops[i]);
				parseResult.put(reportError);
				message_count = message_count-1;
				continue;
			}
		}
		return message_count;
	}
	
	/**
	 * 3	灌浆速度	CROP-03	5   .
	 *
	 * @param tempCrops 待解析报文段
	 * @param agmeHeader the agme header
	 * @return  int 解析出对象个数
	 */
	private int processCrop03(String[] tempCrops, AgmeReportHeader agmeHeader) {
		int message_count = tempCrops.length;
		for (int i = 0; i < tempCrops.length; i++) {
			String[] items = tempCrops[i].split(",", 5);
			try {
				ZAgmeCrop03 zAgmeCrop03 = new ZAgmeCrop03();
				// 测定时间	14	日期	年月日时分秒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				zAgmeCrop03.setObservationDate(sdf.parse(items[0]));
				
				//2019-7-16 cuihongyuan
				if(!TimeCheckUtil.checkTime(zAgmeCrop03.getObservationDate())){
					ReportError re = new ReportError();
					re.setMessage("time check error!");
					re.setSegment(tempCrops[i]);
					parseResult.put(re);
					continue;
				}
				
				agmeHeader.setReport_time(sdf.parse(items[0]));
				/* modified by Cuihongyuan, the ElementValUtil.toBeValid() Parts*/
				// 作物名称	6	编码	详见《编码》作物名称部分
				zAgmeCrop03.setCropName(ElementValUtil.ToBeValidInt(items[1]));
				// 含水率	6	0.01％	计算子粒的含水百分率
				zAgmeCrop03.setPercentageOfWater((float) ElementValUtil.ToBeValidDouble(items[2], 0.01));
				// 千粒重	6	0.01克（g）	计算1000粒平均子粒干重的值
				zAgmeCrop03.setDryWeight((float) ElementValUtil.ToBeValidDouble(items[3], 0.01));
				// 灌浆速度	6	0.01克/（千粒.日）	计算单位时间子粒干物质增长量
				zAgmeCrop03.setFillingRate((float)ElementValUtil.ToBeValidDouble(items[4], 0.01));
				zAgmeCrop03.setStationNumberChina(agmeHeader.getStationNumberChina());
				zAgmeCrop03.setHeightOfPressureSensor(agmeHeader.getHeightOfBarometerAboveMeanSeaLevel());
				zAgmeCrop03.setHeightOfSationGroundAboveMeanSeaLevel(agmeHeader.getHeightOfSationGroundAboveMeanSeaLevel());
				zAgmeCrop03.setLatitude(agmeHeader.getLatitude());
				zAgmeCrop03.setLongitude(agmeHeader.getLongitude());
				agmeCrop.zAgmeCrop03s.add(zAgmeCrop03);
			} catch (Exception e) {
				ReportError reportError = new ReportError();
				reportError.setMessage("Message segment format conversion error");
				reportError.setSegment(tempCrops[i]);
				parseResult.put(reportError);
				message_count = message_count-1;
				continue;
			}
		}
		return message_count;
	}
	
	/**
	 * 2	叶面积指数	CROP-02	6  .
	 *
	 * @param tempCrops the temp crops
	 * @param agmeHeader the agme header
	 * @return  int 解析出对象个数
	 */
	private int processCrop02(String[] tempCrops, AgmeReportHeader agmeHeader) {
		int message_count = tempCrops.length;
		for (int i = 0; i < tempCrops.length; i++) {
			String[] items = tempCrops[i].split(",", 6);
			try {
				ZAgmeCrop02 zAgmeCrop02 = new ZAgmeCrop02();
				// 测定时间	14	日期	年月日时分秒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				zAgmeCrop02.setObservationDate(sdf.parse(items[0]));
				
				//2019-7-16 cuihongyuan
				if(!TimeCheckUtil.checkTime(zAgmeCrop02.getObservationDate())){
					ReportError re = new ReportError();
					re.setMessage("time check error!");
					re.setSegment(tempCrops[i]);
					parseResult.put(re);
					continue;
				}
				
				agmeHeader.setReport_time(sdf.parse(items[0]));
				/* modified by Cuihongyuan, the ElementValUtil.toBeValid() Parts*/
				// 作物名称	6	编码	详见《编码》作物名称部分
				zAgmeCrop02.setCropName(ElementValUtil.ToBeValidInt(items[1]));
				// 发育期	2	编码	详见《编码》作物发育期部分
				zAgmeCrop02.setPeriodOfGrowth(ElementValUtil.ToBeValidInt(items[2]));
				// 生长率	6	0.01克/（平方米.日）	计算总干重部分的干物质增长量
				zAgmeCrop02.setPercentageOfGrowth((float)ElementValUtil.ToBeValidDouble(items[3], 0.01));
				// 含水率	6	0.01％	器官或株（茎）含水率
				zAgmeCrop02.setPercentageOfWater((float) ElementValUtil.ToBeValidDouble(items[4], 0.01));
				// 叶面积指数	6	0.1	单位土地面积上的绿叶面积的倍数
				zAgmeCrop02.setIndexOfLeafArea((float)ElementValUtil.ToBeValidDouble(items[5], 0.1));
				zAgmeCrop02.setStationNumberChina(agmeHeader.getStationNumberChina());
				zAgmeCrop02.setHeightOfPressureSensor(agmeHeader.getHeightOfBarometerAboveMeanSeaLevel());
				zAgmeCrop02.setHeightOfSationGroundAboveMeanSeaLevel(agmeHeader.getHeightOfSationGroundAboveMeanSeaLevel());
				zAgmeCrop02.setLatitude(agmeHeader.getLatitude());
				zAgmeCrop02.setLongitude(agmeHeader.getLongitude());
				agmeCrop.zAgmeCrop02s.add(zAgmeCrop02);
			} catch (Exception e) {
				ReportError reportError = new ReportError();
				reportError.setMessage("Message segment format conversion error");
				reportError.setSegment(tempCrops[i]);
				parseResult.put(reportError);
				message_count = message_count-1;
				continue;
			}
		}
		return message_count;
	}
	
	/**
	 * 1	作物生长发育	CROP-01	8   .
	 *
	 * @param tempCrops the temp crops
	 * @param agmeHeader the agme header
	 * @return  int 解析出对象个数
	 */
	private int processCrop01(String[] tempCrops, AgmeReportHeader agmeHeader) {
		int message_count = tempCrops.length;
		for (int i = 0; i < tempCrops.length; i++) {
			ZAgmeCrop01 zAgmeCrop01 = new ZAgmeCrop01();
			String[] items = tempCrops[i].split(",", 8);
			try {
				/* modified by Cuihongyuan, the ElementValUtil.toBeValid() Parts*/
				// 作物名称	6	编码	详见《农业气象观测数据编码》（简称编码）作物名称部分
				zAgmeCrop01.setCropName(ElementValUtil.ToBeValidInt(items[0]));
				// 发育期	2	编码	详见《编码》作物发育期部分
				zAgmeCrop01.setPeriodOfGrowth(ElementValUtil.ToBeValidInt(items[1]));
				// 发育时间	14	日期	年月日时分秒(国际时，YYYYMMddhhmmss);如观测精度未到秒级则秒位编00; 如观测精度未到分级则分位编00，下同
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				zAgmeCrop01.setGrowthDate(sdf.parse(items[2]));
				
				//2019-7-16 cuihongyuan
				if(!TimeCheckUtil.checkTime(zAgmeCrop01.getGrowthDate())){
					ReportError re = new ReportError();
					re.setMessage("time check error!");
					re.setSegment(tempCrops[i]);
					parseResult.put(re);
					continue;
				}
				
				agmeHeader.setReport_time(sdf.parse(items[2]));
				// 发育期距平	4	天	与历史平均发育期之差，正数发育期推迟，负数为提前
				zAgmeCrop01.setDevelopAnomaly(ElementValUtil.ToBeValidInt(items[3]));
				// 发育期百分率	4	%	进入发育期的株（茎）数比例
				zAgmeCrop01.setPercentageOfGrowthPeriod(ElementValUtil.ToBeValidInt(items[4]));
				// 生长状况	1	无	1为一类苗 2为二类苗  3为三类苗
				zAgmeCrop01.setGrowthState(ElementValUtil.ToBeValidInt(items[5]));
				// 植株高度	4	厘米（cm）	测区植株平均高度
				zAgmeCrop01.setPlantHeight(ElementValUtil.ToBeValidInt(items[6]));
				// 植株密度	8	0.01株（茎）数/平方米	单位面积上的植株数量
				zAgmeCrop01.setPlantDensity((float) ElementValUtil.ToBeValidDouble(items[7], 0.01));
				zAgmeCrop01.setStationNumberChina(agmeHeader.getStationNumberChina());
				zAgmeCrop01.setHeightOfPressureSensor(agmeHeader.getHeightOfBarometerAboveMeanSeaLevel());
				zAgmeCrop01.setHeightOfSationGroundAboveMeanSeaLevel(agmeHeader.getHeightOfSationGroundAboveMeanSeaLevel());
				zAgmeCrop01.setLatitude(agmeHeader.getLatitude());
				zAgmeCrop01.setLongitude(agmeHeader.getLongitude());
				agmeCrop.zAgmeCrop01s.add(zAgmeCrop01);
			} catch (Exception e) {
				ReportError reportError = new ReportError();
				reportError.setMessage("Message segment format conversion error");
				reportError.setSegment(tempCrops[i]);
				parseResult.put(reportError);
				message_count = message_count-1;
				continue;
			}		
		}
		return message_count;
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		DecodeCrop decodeCropEx = new DecodeCrop();

		ParseResult<ZAgmeCrop> parseResult = decodeCropEx.decodeFile(new File("D:\\DataTest\\agmeTest\\Z_AGME_I_50871_20190515000000_O_CROP.txt"));
		List<ZAgmeCrop> list = parseResult.getData();
		@SuppressWarnings("rawtypes")
		List<ReportInfo> reportInfos = parseResult.getReports();
		for(int index = 0; index < reportInfos.size(); index ++){
			AgmeReportHeader agmeReportHeader = (AgmeReportHeader) reportInfos.get(index).getT();
			if(agmeReportHeader.getCropType().equals("CROP-07"))
				System.out.println(agmeReportHeader.getReport_time());
			System.out.println("--------------");
		}
		System.out.println(list.size());
		System.out.println(list.get(0).cropTypes);
	}

}
