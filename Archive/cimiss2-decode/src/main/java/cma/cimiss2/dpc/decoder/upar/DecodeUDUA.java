package cma.cimiss2.dpc.decoder.upar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;

import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.upar.UDUA;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * Main class of decode the UDUA data <br>
 * 飞机高空探测（UD/UA)报告解码类
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
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cimiss2.dpc.decoder.bean.upar.UDUA}。
 * </ol>
 * </li>
 * </ul>
 * 
 * <strong> sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br>
 * ZCZC 65638<br>

UABZ01 SBBR 090100<br>

ARP PRHJM 2313S 04613W 1825 F360 MS50 307/127=<br>

NNNN<br>
 * <strong>code:</strong><br>
 *  DecodeUDUA decodeUDUA = new DecodeUDUA();<br>
 * 	ParseResult<UDUA> parseResult = decodeUDUA.DecodeFile(new File(String filepath));"<br>
 * 	List<UDUA> list = parseResult.getData();<br>
 * 	System.out.println(list.size());<br>
 * 	System.out.println(list.get(0).getPlaneID());<br>
 * <strong>output:</strong><br>
 * 1<br>
 * PRHJM<br>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月28日 上午10:03:34   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class DecodeUDUA{
	/**
	 * 存放数据解析的结果集 
	 */
	ParseResult<UDUA> parseResult = new ParseResult<UDUA>(false);
	/**
	 * 缺测值
	 */
	private int defaultValue = 999999; 
	/**
	 * 无需观测值
	 */
	private int defaultValue2 = 999998; 
	/**
	 * 飞机高空探测报解码主函数   
	 * @param file 待解析文件   
	 * @return: ParseResult<UDUA>      解码结果封装
	 */
	@SuppressWarnings("resource")
	public ParseResult<UDUA> DecodeFile(File file){
		if (file != null && file.exists() && file.isFile()) {
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			// 获得资料日期
			Date oDate = new Date(file.lastModified()); // 观测时间
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(oDate);
			calendar.add(Calendar.HOUR_OF_DAY, -8);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			oDate = calendar.getTime();
			
			// 获取文件的编码

			FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();    
			String fileCode = null;
			try{
			    fileCode = fileEncodeUtil.javaname[fileEncodeUtil.detectEncoding(file)];
			}catch(Exception e){
				fileCode = "utf-8";
//				ReportError re = new ReportError();
//				re.setMessage("File Encoding format error!");
//				parseResult.put(re);
//				return parseResult;
			}
			try {
				fileCode = fileEncodeUtil.javaname[fileEncodeUtil.detectEncoding(file)];
			} catch (Exception e) {
				fileCode="utf-8";
//				ReportError re = new ReportError();
//				re.setMessage("File Encoding format error!");
//				parseResult.put(re);
//				return parseResult;
			}
			InputStreamReader read = null;
			Scanner scanner = null;
			 // 获取文件流
			 try{
		        	read = new InputStreamReader(new FileInputStream(file), fileCode);
					scanner = new Scanner(read).useDelimiter("NNNN"); // 
//					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
					String report = "";
					while (scanner.hasNext() && (report = scanner.next()).length() > 3) {  
						 // 一个编报中心的资料
						// 移除空行 
						report = report.replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1").replaceAll("^((\r\n)|\n)", "");
//						if(report.contains("CNFLVR6")){
//							System.out.println(file);
//						}
						String reports[] = report.split("\n|(\r\n)");
						if(reports.length < 3) {
							ReportError re = new ReportError();
							re.setMessage("report too short, format error");
							re.setSegment(report);
							parseResult.put(re);
							continue;
						}
						// 1. 一个编报中心的资料，首行标识判断
						reports[0] = reports[0].trim();
						if(reports[0].substring(0, 4).compareTo("ZCZC") != 0 
								&& reports[0].substring(0, 3).compareTo("ZCZ") != 0
								&& reports[0].substring(0, 3).compareTo("CZC") != 0){
							// 报文标识错误
							continue;
						}
						// 2. 一个编报中心的资料, 第二行标识判断
						reports[1] = reports[1].trim();
						BullHeader bullHeader = new BullHeader();
						int ret = decodeHeader(reports[1], bullHeader);
						if(ret < 0){ // 报头格式错误
							ReportError re = new ReportError();
							re.setMessage("Header format error or null");
							re.setSegment(reports[1]);
							parseResult.put(re);
							continue;
						}
						try {
							oDate.setDate(Integer.parseInt(bullHeader.getYYGGgg().substring(0, 2)));
							oDate.setHours(Integer.parseInt(bullHeader.getYYGGgg().substring(2, 4)));
							oDate.setMinutes(Integer.parseInt(bullHeader.getYYGGgg().substring(4, 6)));
						} catch (Exception e) { 
							ReportError re = new ReportError();
							re.setMessage("Data time format conversion exception");
							re.setSegment(reports[1]);
							parseResult.put(re);
							continue;
						}
						Date dataTime = new Date(oDate.getTime());
						if(!TimeCheckUtil.checkTime(dataTime)){
							ReportError re = new ReportError();
							re.setMessage("DataTime out of range：time:"+dataTime);
							re.setSegment(report);
							parseResult.put(re);
							continue;
						}
						// 移除前两行， 取得以“=”分隔的报文
						report = "";
						for(int i = 2; i < reports.length; i ++)
							report += (reports[i] + " ");
						reports = report.trim().split("=");
						for(int i = 0; i < reports.length; i ++){
							reports[i] = reports[i].replaceAll("(\r\n)|\n", " ");
							reports[i] = reports[i].replaceAll("\\s+", " "); 
							reports[i] = reports[i].trim();
							if(!reports[i].equals("") && dataTime != null){
								report = reports[i];
								UDUA udua = new UDUA();
								udua.setObservationTime(new Date(oDate.getTime()));
								udua.setDataTime(new Date(oDate.getTime()));
								udua.setCorrectSign(bullHeader.getBbb());   // V_BBB
								udua.setReportCenter(bullHeader.getCccc()); // CCCC
								udua.setReportType(bullHeader.getTt() + bullHeader.getAa() + bullHeader.getIi()); // 1. 报文类别
//								parseResult.setSuccess(true);
								if(bullHeader.getTt().equals("UD")){   // 如果资料类型为UD的解码
									ret = DecodeUDs(report, udua); 
									if(ret > 0){
										parseResult.put(udua);
										parseResult.put(new ReportInfo<UDUA>(udua, report));
										parseResult.setSuccess(true);
									}
								}
								else if(bullHeader.getTt().equals("UA")){
									if(report.contains("NIL")){
										ReportError re = new ReportError();
										re.setMessage("report is NII!");
										re.setSegment(report);
										parseResult.put(re);
										continue;
									}
									ret = DecodeUAs(report, udua); // 各个要素
									if(ret > 0){
										parseResult.put(udua);
										parseResult.put(new ReportInfo<UDUA>(udua, report));
										parseResult.setSuccess(true);
									}
								}
								else {
									ReportError re = new ReportError();
									re.setMessage("report type error");
									re.setSegment(report);
									parseResult.put(re);
									continue;
								}
							}
						} 
					}
		        } catch (UnsupportedEncodingException e) {
					parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
				} catch (FileNotFoundException e) {
					parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
				}  catch (Exception e) {
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
		}
		else {
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
		}
		return parseResult;
	}
	/**
	 * 资料类型为UD的报文解码函数   
	 * @param report 待解码报文
	 * @param udua 解码结果    
	 * @return int      解码成功，返回1;其他，返回值<0
	 */
	private int DecodeUDs(String report, UDUA udua) {
		int ret = -1;
		int cursor = 0;
		String ele[] = report.split(" ");
		int len = ele.length;
		if(ele[0].equals("AMDAR") && len >= 2) 
			cursor += 2;
		if(cursor < len){
			if(ele[cursor].equals("UNS"))   //飞行状态和观测类型标识
				udua.setPlaneStateAndObsType(2);
			else if(ele[cursor].equals("LVR"))
				udua.setPlaneStateAndObsType(3);
			else if(ele[cursor].equals("LVW"))
				udua.setPlaneStateAndObsType(4);
			else if(ele[cursor].equals("ASC"))
				udua.setPlaneStateAndObsType(5);
			else if(ele[cursor].equals("DES"))
				udua.setPlaneStateAndObsType(6);
			else{
				udua.setPlaneStateAndObsType(defaultValue);
			}
		}
		cursor ++;
		// 飞机标识符,字母起首，长度小于8
		if(cursor < len && ele[cursor].length() <= 6 && Character.isAlphabetic(ele[cursor].charAt(0))) 
			udua.setPlaneID(ele[cursor]);
		else if(cursor < len && ele[cursor].length() > 6 && Character.isAlphabetic(ele[cursor].charAt(0)))
			udua.setPlaneID(ele[cursor].substring(0, 6));  // 长度大于8时，只取前6位
		else{
			ReportError re = new ReportError();
			re.setMessage("Aircraft identification error");
			re.setSegment(report);
			parseResult.put(re);
			return -2; // 飞机标识错误
		}
		cursor ++;
		
		// 纬度，度、分占4位， S、N结束
		String latStr = "";
		udua.setLatitude(defaultValue);
		if(cursor < len && (latStr = ele[cursor++]).length() == 5 && !latStr.contains("/") && isDigital(latStr.substring(0, 4))){
			double lat = Integer.parseInt(latStr.substring(0, 2)) + Integer.parseInt(latStr.substring(2, 4)) / 60.0;
			if(latStr.endsWith("S"))
				lat = -lat;
			udua.setLatitude(lat);
		}
		
		// 经度，度、分占5位，W、E结束
		String lonStr = "";
		udua.setLongtitude(defaultValue);
		if(cursor < len && (lonStr = ele[cursor ++]).length() == 6 && !lonStr.contains("/") && isDigital(lonStr.substring(0, 5))){
			double lon = Integer.parseInt(lonStr.substring(0, 3)) + Integer.parseInt(lonStr.substring(3, 5)) / 60.0;
			if(lonStr.endsWith("W"))
				lon = -lon;
			udua.setLongtitude(lon);
		}
		// 日时分
		String dateHourMin = "";
		Date dataTime = udua.getDataTime(); // 观测时间
		if(cursor < len && (dateHourMin = ele[cursor ++]).length() == 6){
			try {
				Calendar calendar2 = Calendar.getInstance();
				calendar2.setTime(dataTime);
				calendar2.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dateHourMin.substring(2, 4)));
				calendar2.set(Calendar.MINUTE, Integer.parseInt(dateHourMin.substring(4, 6)));
				if(dataTime.getHours() < Integer.parseInt(dateHourMin.substring(2, 4))){
					calendar2.add(Calendar.DAY_OF_MONTH, -1);
				}
				dataTime = calendar2.getTime();
				udua.setDataTime(dataTime);
			} catch (Exception e) {
				ReportError re = new ReportError();
				re.setMessage(report);
				re.setSegment(dateHourMin);
				parseResult.put(re);
				return -3;  // 时间解析错误
			}
			udua.getObservationTime().setDate(Integer.parseInt(dateHourMin.substring(0, 2)));
			udua.getObservationTime().setHours(Integer.parseInt(dateHourMin.substring(2, 4)));
			udua.getObservationTime().setMinutes(Integer.parseInt(dateHourMin.substring(4, 6)));
		}
		if(dateHourMin.length() != 6){
			ReportError re = new ReportError();
			re.setMessage("YYGGgg format error");
			re.setSegment(report);
			parseResult.put(re);
			return -3;  // 时间解析错误
		}
		ret = 1;
		// 飞行高度,百英尺换算成米
		String hStr = "";
		if(cursor < len && (hStr = ele[cursor ++]).length() >= 4 && isDigital(hStr.substring(1, 4))){
			int h = Integer.parseInt(hStr.substring(1, 4));
			udua.setHeightOfStandardPressure(h * 30.48);
		}
		else udua.setHeightOfStandardPressure(defaultValue);
		// 温度
		String tempStr = "";
		if(cursor < len && (tempStr = ele[cursor ++]).length() == 5 && isDigital(tempStr.substring(2))){
			double temp = Integer.parseInt(tempStr.substring(2)) * 0.1;
			if(tempStr.startsWith("MS"))
				temp = -temp;
			udua.setTemperature(temp);
		}
		else udua.setTemperature(defaultValue);
		// 风向、风速  ddd/fff(dddfff)
		String windStr = "";
		String speed = "";
		String dir = "";
		udua.setWindDir(defaultValue);
		udua.setWindSpeed(defaultValue);
//		if(cursor < len && (windStr = ele[cursor ++]).length() == 6){
//			dir = windStr.substring(0, 3);
//			speed = windStr.substring(4, 6);
//		}
//		else 
			if(cursor < len && (windStr = ele[cursor ++]).length() >= 7 && windStr.charAt(3) == '/'){
			 dir = windStr.substring(0, 3);
			 speed = windStr.substring(4, 7);
		}
		if(!dir.isEmpty() && isDigital(dir))
			udua.setWindDir(Integer.parseInt(dir));
		if(!speed.isEmpty() && isDigital(speed) && (udua.getReportCenter().equals("ZBBB")||udua.getReportCenter().equals("BABJ")))
			udua.setWindSpeed(Integer.parseInt(speed));
		else if(!speed.isEmpty() && isDigital(speed))
//			udua.setWindSpeed(Integer.parseInt(speed) * 0.51 + 0.5); // 海里换算成米
			// 修改于 2019-5-7
			udua.setWindSpeed(Integer.parseInt(speed) * 0.51); // 海里换算成米
		
		// 湍流度，代码表TBBa
		String turbulenceStr = "";
		udua.setTurbulence(defaultValue);
		if(cursor < len && (turbulenceStr = ele[cursor ++]).length() == 3 && turbulenceStr.startsWith("TB")
				&& isDigital(turbulenceStr.substring(2))){
			udua.setTurbulence(Integer.parseInt(turbulenceStr.substring(2)));
		}
		// 导航系统类型SS1S2S3
		String naviType = "";
		udua.setNaviType(defaultValue);
		udua.setDataTransType(defaultValue);
		udua.setTemperatureObsAccuCode(defaultValue);
		if(cursor < len && (naviType = ele[cursor ++]).length() == 4 && naviType.charAt(0) == 'S'){
			if(isDigital(naviType.substring(1, 2)))
				udua.setNaviType(Integer.parseInt(naviType.substring(1, 2)));
			if(isDigital(naviType.substring(2, 3)))
				udua.setDataTransType(Integer.parseInt(naviType.substring(2, 3)));
			if(isDigital(naviType.substring(3)))
				udua.setTemperatureObsAccuCode(Integer.parseInt(naviType.substring(3)));
		}
		
		if(cursor < len && ele[cursor ++].equals("333")){
			 // 跳过 333
			// Fhdhdhd
			if(cursor < len && (hStr = ele[cursor ++]).length() >= 4 && hStr.charAt(0) == 'F' &&
					isDigital(hStr.substring(1, 4))){
				double h = Integer.parseInt(hStr.substring(1, 4)) * 30.48;
				if(Math.abs(udua.getHeightOfStandardPressure() - defaultValue) > 1e-3)
					// 不是默认值，则赋值
					udua.setHeightOfStandardPressure(h);
			}
			// 最大等价垂直阵风速 VGfgfgfg
			String verticalWind = "";
			udua.setVerticalWindSpeed(defaultValue);
//			if(cursor < len && (verticalWind = ele[cursor ++]).length() >= 5 && verticalWind.startsWith("VG")
//					&& isDigital(verticalWind.substring(2, 5))){
			// 修改于2019-5-7 cuihongyuan
			if(cursor < len && (verticalWind = ele[cursor ++]).length() == 5 && verticalWind.startsWith("VG")
					&& isDigital(verticalWind.substring(2, 5))){
				udua.setVerticalWindSpeed(Integer.parseInt(verticalWind.substring(2, 5)) * 0.1);
			}
			udua.setAircraftHeightAboveSeaLevel(defaultValue2);
			udua.setDataTransType(defaultValue2);
			udua.setTemperatureObsAccuCode(defaultValue2);
			udua.setDewPoint(defaultValue2);
			udua.setRelativehumidity(defaultValue2);
		}
		else{
			udua.setAircraftHeightAboveSeaLevel(defaultValue);
			udua.setDataTransType(defaultValue);
			udua.setTemperatureObsAccuCode(defaultValue);
			udua.setDewPoint(defaultValue);
			udua.setRelativehumidity(defaultValue);
		}
		return ret;
	}
	/**
	 * 资料类型为UA的报文的解码函数   
	 * @param  report 待解码报文
	 * @param  udua 解码结果
	 * @return int      如果返回1，解码正确；返回<0, 解码失败
	 */
	private int DecodeUAs(String report, UDUA udua) {
		int ret = -1;
		int cursor = 0;
		int idx = report.lastIndexOf("ARP");
		if(idx == -1)
			idx = report.lastIndexOf("AIREP");
		if(idx == -1)
			idx = 0;
		report = report.substring(idx);
		String ele[] = report.split(" ");
		int len = ele.length;
		if((ele[0].equals("ARP") || ele[0].equals("AIREP")) && len >= 2) 
			cursor ++;
		// 飞机标识符,字母起首，长度小于8
		if(cursor < len && ele[cursor].length() <= 8 && Character.isAlphabetic(ele[cursor].charAt(0))) 
			udua.setPlaneID(ele[cursor]);
		else if(cursor < len && ele[cursor].length() > 8 && Character.isAlphabetic(ele[cursor].charAt(0)))
			udua.setPlaneID(ele[cursor].substring(0, 8));  // 长度大于8时，只取前6位
		else{
			ReportError re = new ReportError();
			re.setMessage("Aircraft identification error");
			re.setSegment(report);
			parseResult.put(re);
			return -2; // 飞机标识错误
		}
		cursor ++;
		if((ele[cursor].endsWith("E") || ele[cursor].endsWith("W"))){
//			if(ele[cursor].contains("S"))
//				latlon = ele[cursor].split("S");
//			else if(ele[cursor].contains("N"))
//				latlon = ele[cursor].split("N");
//			if(latlon.length == 2){
//				udua.setLatitude(getLat(latlon[0]));
//				udua.setLongtitude(getLon(latlon[1]));
//			}
//			cursor ++;
			return -2;
		}
		else{
			//纬度
			udua.setLatitude(getLat(ele[cursor ++]));
			//经度
			udua.setLongtitude(getLon(ele[cursor ++]));
		}
		// 时分 GGgg
		String HourMin = "";
		Date dataTime = new Date(udua.getDataTime().getTime()); // 观测时间
		if(cursor < len && (HourMin = ele[cursor ++]).length() == 4){
			try {
				Calendar calendar2 = Calendar.getInstance();
				calendar2.setTime(dataTime);
				calendar2.set(Calendar.HOUR_OF_DAY, Integer.parseInt(HourMin.substring(0, 2)));
				calendar2.set(Calendar.MINUTE, Integer.parseInt(HourMin.substring(2, 4)));
				if(dataTime.getHours() < Integer.parseInt(HourMin.substring(0, 2))){
					calendar2.add(Calendar.DAY_OF_MONTH, -1);
				}
				dataTime = calendar2.getTime();
				udua.setDataTime(dataTime);
			}catch (Exception e) {
				ReportError re = new ReportError();
				re.setMessage("GGgg format error");
				re.setSegment(report);
				parseResult.put(re);
				return -3;  // 时间解析错误
			}
		}
		if(HourMin.length() != 4){
			ReportError re = new ReportError();
			re.setMessage("GGgg format error");
			re.setSegment(report);
			parseResult.put(re);
			return -3;  // 时间解析错误
		}
		ret = 1;
		// 飞行高度,百英尺换算成米
		String hStr = "";
		if(cursor < len && (hStr = ele[cursor ++]).length() == 4 && isDigital(hStr.substring(1, 4))){
			int h = Integer.parseInt(hStr.substring(1, 4));
//			udua.setHeightOfStandardPressure(h * 30.48 + 0.5);
			// cuihongyuan修改 201-5-7
			udua.setHeightOfStandardPressure(h * 30.48);
		}
		else udua.setHeightOfStandardPressure(defaultValue);
		// 温度 SSTaTa
		String tempStr = "";
		double temp = defaultValue;
		if(cursor < len && (tempStr = ele[cursor]).length() >= 4 && isDigital(tempStr.substring(2, 4))){
			temp = Integer.parseInt(tempStr.substring(2, 4));
		}
		else if(cursor < len &&(tempStr = ele[cursor]).length() >= 3 && isDigital(tempStr.substring(1, 3))){
		    temp = Integer.parseInt(tempStr.substring(1, 3));
		}
		if(tempStr.startsWith("M"))
			udua.setTemperature(-temp);
		else{
			if(tempStr.startsWith("P"))
				udua.setTemperature(temp);
			else
				udua.setTemperature(defaultValue);
		}
		
		cursor ++;
		// 风向、风速  ddd/fff(dddfff)
		udua.setWindDir(defaultValue);
		udua.setWindSpeed(defaultValue2);
		if(cursor < len){
			double []wind = new double[2];
			wind = getWindDirSpeed(ele[cursor]);
			udua.setWindDir((int) wind[0]);
			udua.setWindSpeed(wind[1]);
			if(!udua.getReportCenter().equals("ZBBB") && !udua.getReportCenter().equals("BABJ") && Math.abs(999999.0 - wind[1]) > 1e-3) 
				//编报中心不为ZBBB时，取值  * 0.51 + 0.5
//				udua.setWindSpeed(wind[1] * 0.51 + 0.5);
				// cuihongyuan 修改为2019-5-7
				udua.setWindSpeed(wind[1] * 0.51);
		}
		// 默认值
		udua.setVerticalWindSpeed(defaultValue2);
		udua.setNaviType(defaultValue2);
		udua.setTurbulence(defaultValue2);
		udua.setAircraftHeightAboveSeaLevel(defaultValue2);
		udua.setDewPoint(defaultValue2);
		udua.setRelativehumidity(defaultValue2);
		udua.setDataTransType(defaultValue2);
		udua.setTemperatureObsAccuCode(defaultValue2);
		String YYGGgg = ele[len - 2];
		if(isDigital(YYGGgg) && YYGGgg.length() == 6){
			udua.getObservationTime().setDate(Integer.parseInt(YYGGgg.substring(0, 2)));
			udua.getObservationTime().setHours(Integer.parseInt(YYGGgg.substring(2, 4)));
		}
		
		return ret;
	}

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * 报文头解码结构体
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《飞机高空探测报告(UD/UA)数据表》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月28日 上午10:06:36   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
	public class BullHeader{
		/**
		 * 报文类型
		 */
		private String tt = "";
		/**
		 * 国家编码
		 */
		private String aa = "";
		/**
		 * 编码
		 */
		private String ii = "";
		/**
		 * 发报中心
		 */
		private String cccc = "";
		/**
		 * 发报日时分
		 */
		private String YYGGgg = "";
		/**
		 * 更正报标识
		 */
		private String bbb = "";
		
		public String getTt() {
			return tt;
		}
		public void setTt(String tt) {
			this.tt = tt;
		}
		public String getAa() {
			return aa;
		}
		public void setAa(String aa) {
			this.aa = aa;
		}
		public String getIi() {
			return ii;
		}
		public void setIi(String ii) {
			this.ii = ii;
		}
		public String getCccc() {
			return cccc;
		}
		public void setCccc(String cccc) {
			this.cccc = cccc;
		}
		public String getYYGGgg() {
			return YYGGgg;
		}
		public void setYYGGgg(String yYGGgg) {
			YYGGgg = yYGGgg;
		}
		public String getBbb() {
			return bbb;
		}
		public void setBbb(String bbb) {
			this.bbb = bbb;
		}
		
	}
	/**
	 * 解析报文头，TTAAII CCCC YYGGgg [BBB]   
	 * @param header 待解析字符串
	 * @param bullheader 存储报文头解析结果
	 * @return int      解析成功返回1；否则返回-1
	 */
	public static int decodeHeader(String header, BullHeader bullheader){
		if(header == null || header.equals(""))
			return -1;
		String sp[] = header.trim().split("\\s+");
		
		if(sp.length == 3 || sp.length == 4){
			// 1. TTAAII 例如 SAAU31(资料类型+国家+号码)
			if(sp[0].length() == 6 || sp[0].length() == 5){
				bullheader.setTt(sp[0].substring(0, 2));
				bullheader.setAa(sp[0].substring(2, 4));
				bullheader.setIi(sp[0].substring(4));
			}
			else
				return -1; // TTAAII 格式错误
			// 2. CCCC编报中心  例如：AMMC、RJTD
			if(sp[1].length() == 4){
				bullheader.setCccc(sp[1]);
			}
			else
				return -1; // CCCC 格式错误
			// 3. YYGGgg, (日期+时+分，分别占两位)
			if(sp[2].length() == 6){
				int i = 0;
				for(i = 0; i < 6; i ++){
					if(!Character.isDigit(sp[2].charAt(i)))
						break;
				}
				if(i == 6)
					bullheader.setYYGGgg(sp[2]);
				else{
					return -1; // YYGGgg 格式错误
				}
			}
			else
				return -1; //YYGGgg 格式错误
			// 4. V_BBB: 更正标识，例如：CCA
			bullheader.setBbb("000");
			if(sp.length == 4){
				String tmp_val = sp[3];
				char s = tmp_val.charAt(0);
				if(tmp_val.length() == 3 && (s == 'A' || s == 'R' ||s == 'C' ||s == 'P')){
					bullheader.setBbb(sp[3]);
				}
				else
					return -1; // V_BBB 格式错误
			}
		} 
		else{
			return -1;// 报头格式错误
		}
		return 1; // 报头解析成功
	}
	/**
	 * 判断字符串是否均为数字组成   
	 * @param str 待判断字符串     
	 * @return boolean  true 或者 false 
	 */
	boolean isDigital(String str){
		int i = 0;
		if(str.startsWith("-") || str.startsWith("+") || (str.charAt(0) <= '9' && str.charAt(0) >= '0'))
			for(i = 1; i < str.length(); i ++){
				if(str.charAt(i) > '9' || str.charAt(i) < '0'){
					break;
				}
			}
		
		if(i == str.length())
			return true;
		else return false;
	}
	/**
	 * 解码获得纬度值   
	 * @param  str 待解析字符      
	 * @return double     纬度值 
	 */
	double getLat(String str){
		double lat = defaultValue; 
		if(str.length() == 3 && isDigital(str.substring(0, 2)))
			lat = Integer.parseInt(str.substring(0, 2));
		else if(str.length() == 5 && isDigital(str.substring(0, 4)))
			lat = Integer.parseInt(str.substring(0, 2)) + Integer.parseInt(str.substring(2, 4)) / 60.0;
		else
			return defaultValue;
		if(str.endsWith("S"))
			lat = -lat;
		return lat;
	}
	/**
	 * 解码获得经度值   
	 * @param  Str 待解析字符    
	 * @return double    经度值  
	 */
	double getLon(String Str){
		double lon = defaultValue;
		if(Str.length() == 4 && isDigital(Str.substring(0, 3)))
			lon = Integer.parseInt(Str.substring(0,3));
		else if(Str.length() == 6 && isDigital(Str.substring(0,5)))
			lon = Integer.parseInt(Str.substring(0,3)) + Integer.parseInt(Str.substring(3,5)) / 60.0;
		else 
			return defaultValue;
		if(Str.endsWith("W"))
			lon = -lon;
		return lon;
	}
	/**
	 * 解码获得风向、风速   
	 * @param str 待解析字符    
	 * @return double[]      风向、风速
	 */
	double[] getWindDirSpeed(String str){
		double Wind[] = new double[2];
		Wind[0] = defaultValue; // 风向
		Wind[1] = defaultValue; // 风速
		int len = str.length();
		
		if(len >= 6){
			if(isDigital(str.substring(0, 3))){
				Wind[0] = Integer.parseInt(str.substring(0, 3));
			}
			if(len >= 7 && isDigital(str.substring(6, 7))){
				str = str.substring(0, 7);
				if(str.charAt(3) == '/' && isDigital(str.substring(4)))
					Wind[1] = Integer.parseInt(str.substring(4));
			}
			else{ 
				str = str.substring(0, 6);
				if(str.charAt(3) != '/' && isDigital(str.substring(3)))
					Wind[1] = Integer.parseInt(str.substring(3));
				else if(str.charAt(3) == '/' && isDigital(str.substring(4)))
					Wind[1] = Integer.parseInt(str.substring(4));
			}
		}
		return Wind;
	}
	
	public static void main(String[] args) {
		DecodeUDUA decodeUDUA = new DecodeUDUA();

		ParseResult<UDUA> parseResult = decodeUDUA.DecodeFile(new File("D:\\TEMP\\B.1.9.1\\2-15\\UD_A_UANT01CWAO131300_C_BABJ_20200213130655_26174.MSG"));
		List<UDUA> list = parseResult.getData();
		System.out.println(list.size());
//		System.out.println(list.get(0).getPlaneID());
	}
}
