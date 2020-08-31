package cma.cimiss2.dpc.decoder.sevp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.sevp.Typh;
import cma.cimiss2.dpc.decoder.bean.sevp.TyphEle;
import cma.cimiss2.dpc.decoder.bean.sevp.TyphKey;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * Main class of decode the Typhoon Live and Forecast data <br>
 * 台风实况与预报数据解码类
 *
 * <p>
 * notes:
 * 解码程序分为两个步骤:数据读取及预处理、实体对象构建及赋值。
 * <ul>
 * <li>数据读取及预处理过程：
 * <ol>
 * <li>所有运算采用java double 进行基本运算。
 * <li>无特殊说明时，所有包含”/”的数据全部转化成999999。
 * <li>未通过数据校验时，该条数据将会被当成错误报文处理。
 * </ol>
 * </li>
 * <li>实体对象构建及赋值：
 * <ol>
 * <li>校验所有段的数据的数据类型是否合法。
 * <li>所有的赋值失败都会导致解码错误，该条数据将被当成错误报文处理。
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cimiss2.dpc.decoder.bean.sevp.Typh}。
 * </ol>
 * </li>
 * </ul>
 * 
 * <strong> sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br>
 * ZCZC 641<br>

WTKO20 RKSL 150600<br>

KMA TROPICAL CYCLONE ADVISORY NO. 10<br>

NAME 1512 HALOLA<br>

ANALYSIS<br>

POSITION 150600UTC 17.7N 171.0E<br>

MOVEMENT NW 9KT<br>

PRES/VMAX 965HPA 72KT<br>

FORECAST<br>

24HR<br>

POSITION 160600UTC 18.9N 166.9E WITHIN 75NM<br>

PRES/VMAX 960HPA 76KT<br>

48HR<br>

POSITION 170600UTC 19.5N 161.3E WITHIN 125NM<br>

PRES/VMAX 955HPA 78KT<br>

72HR<br>

POSITION 180600UTC 20.1N 155.8E WITHIN 175NM<br>

PRES/VMAX 955HPA 78KT<br>

96HR<br>

POSITION 190600UTC 21.3N 151.6E WITHIN 250NM<br>

PRES/VMAX 950HPA 84KT<br>

120HR<br>

POSITION 200600UTC 23.1N 147.2E WITHIN 295NM<br>

PRES/VMAX 950HPA 84KT<br>

KOREA METEOROLOGICAL ADMINISTRATION.<br>

NNNN<br>
 * <strong>code:</strong><br>
 * DecodeTyph decodeTyph = new DecodeTyph();<br>
 * ParseResult<Typh> parseResult = decodeTyph.DecodeFile(new File(String filepath));<br>
 * List<Typh> list = parseResult.getData();<br>
 * TyphKey key = list.get(0).getTyphKey();<br>
 * List<TyphEle> eles = list.get(0).getTyphEles();<br>
 * System.out.println("台风名称、台风级别、台风国际编号： " + key.getTyphName() + " " + key.getTyphLevel() + " " + key.getInternalCode());<br>
 * System.out.println("纬度、经度：" + eles.get(0).getLatitude() + " " + eles.get(0).getLongtitude());<br>
 * <strong>output:</strong><br>
 * 台风名称、台风级别、台风国际编号： HALOLA 999999 1512<br>
 * 纬度、经度：17.7 171.0<br>
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月28日 下午4:42:43   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class DecodeTyphFK{
	/**
	 * 解码结果集
	 */
	private ParseResult<Typh> parseResult = new ParseResult<Typh>(false);
	
	/**
	 * 观测资料年月
	 */
	String yyyyMM = "";  
	/**
	 * 编报中心代号
	 */
	static Map<String, Integer> center = null;
	/**
	 * 台风实况与预报数据解码主函数   
	 * @param file 待解码文件
	 * @return ParseResult<Typh>     解码结果封装  
	 */
	@SuppressWarnings({ "resource", "deprecation", "static-access" })
	public ParseResult<Typh> DecodeFile(File file){
		if (file != null && file.exists() && file.isFile()) {
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			// 获得资料日期
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			Date oDate = new Date(file.lastModified()); // 观测时间
			yyyyMM = simpleDateFormat.format(oDate).substring(0, 6);
			if(file.getParent().toLowerCase().contains("his")){
				yyyyMM = file.getName().split("_")[4].substring(0, 6);
			}
			// 获取文件的编码
			FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();     
			String fileCode = fileEncodeUtil.javaname[fileEncodeUtil.detectEncoding(file)];
			InputStreamReader read = null;
			Scanner scanner = null;
			CenterCode();
			 // 获取文件流
			 try{
	        	read = new InputStreamReader(new FileInputStream(file), fileCode);
				scanner = new Scanner(read).useDelimiter("NNNN"); // 
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
				String report = "";
				while (scanner.hasNext() && (report = scanner.next().trim()).length() > 0) {  
					String  headerLine= "";
					// 移除空行 
					report = report.replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1").replaceAll("^((\r\n)|\n)", "");
					String reports[] = report.split("\n|(\r\n)");
					if(report == null || reports.length < 3) {
						ReportError re = new ReportError();
						re.setMessage("Report too short, format error!");
						re.setSegment(report);
						parseResult.put(re);
						continue;
					}
					BullHeader bullHeader = new BullHeader();
					Date dataTime;
					int ret = -1;
					// 1. 一个编报中心的资料，首行标识判断
					reports[0] = reports[0].trim();
					if(reports[0].substring(0, 4).compareTo("ZCZC") == 0 
							|| reports[0].substring(0, 3).compareTo("ZCZ") == 0
							|| reports[0].substring(0, 3).compareTo("CZC") == 0){
						reports[1] = reports[1].trim();
						headerLine = reports[1];
						ret = BullHeader.decodeHeader(reports[1], bullHeader);
						if(ret < 0){ // 报头格式错误
							ReportError re = new ReportError();
							re.setMessage("Report header format error or null!");
							re.setSegment(reports[1]);
							parseResult.put(re);
							continue;
						}
						 // 资料时间
						try { 
							dataTime = sdf.parse(yyyyMM + bullHeader.getYYGGgg());
							dataTime.setSeconds(0);
//							if(!TimeCheckUtil.checkTime(dataTime)){
//								ReportError re = new ReportError();
//								re.setMessage("DataTime out of range：time:"+dataTime);
//								re.setSegment(reports[1]);
//								parseResult.put(re);
//								continue;
//							}
						} catch (ParseException e) {
							ReportError re = new ReportError();
							re.setMessage("DateTime format error!");
							re.setSegment(reports[1]);
							parseResult.put(re);
							continue;
						}
						report = "";
						for(int i = 2; i < reports.length; i ++)
							report += (reports[i] + " ");
					}
					else{
						// 2. 一个编报中心的资料, 第二行标识判断
						reports[2] = reports[2].trim();
						headerLine = reports[2];
						ret = BullHeader.decodeHeader(reports[2], bullHeader);
						if(ret < 0){ // 报头格式错误
							ReportError re = new ReportError();
							re.setMessage("Report header format error or null!");
							re.setSegment(reports[2]);
							parseResult.put(re);
							continue;
						}
						 // 资料时间
						try { 
							dataTime = sdf.parse(yyyyMM + bullHeader.getYYGGgg());
							dataTime.setSeconds(0);
//							if(!TimeCheckUtil.checkTime(dataTime)){
//								ReportError re = new ReportError();
//								re.setMessage("DataTime out of range：time:"+dataTime);
//								re.setSegment(reports[2]);
//								parseResult.put(re);
//								continue;
//							}
						} catch (ParseException e){
							ReportError re = new ReportError();
							re.setMessage("DateTime format error!");
							re.setSegment(reports[2]);
							parseResult.put(re);
							continue;
						}
						report = "";
						for(int i = 3; i < reports.length; i ++)
							report += (reports[i] + " ");
					}
					
					reports = report.trim().split("=");
					TyphKey typhKey = null;
					List<TyphEle> typhEles = null;
					Typh typh = null;
					for(int i = 0; i < reports.length; i++){
						reports[i] = reports[i].replaceAll("(\r\n)|\n", " ");
						reports[i] = reports[i].replaceAll("\\s+", " "); 
						typhKey = new TyphKey();
						typhEles = new ArrayList<TyphEle>();
						typhKey.setV_BBB(bullHeader.getBbb());
						typhKey.setV_TT(bullHeader.getTt());
						typhKey.setV_AA(bullHeader.getAa());
						typhKey.setV_II(bullHeader.getIi());
						typhKey.setObservationTime(dataTime);
						// 判断发报中心，根据不同的发报中心进行不同 方式的解码
						String cccc = bullHeader.getCccc();
						typhKey.setReportCenter(cccc);
						if(bullHeader.getTt().equals("FK"))
							ret = Proc_KT(reports[i], typhKey, typhEles);
						else { // 发报中心不在解码范围内
							System.out.println(cccc + ": \t" + file);
							continue;
						}
						if(ret == 0 && typhKey.getNumOfForecastEfficiency() != 0){ 
							typh = new Typh();
							typh.setTyphEles(typhEles);
							typh.setTyphKey(typhKey);
							parseResult.put(typh);
							parseResult.put(new ReportInfo<Typh>(typh, headerLine + " " + reports[i]));
							parseResult.setSuccess(true);
						}
						else{
							ReportError re = new ReportError();
							re.setMessage("Report too short, format error!");
							re.setSegment(reports[i]);
							parseResult.put(re);
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
					if(scanner != null){
						scanner.close();
					}
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
	 * 解析报文类型为KT的台风实况与预报资料
	 */
	private int Proc_KT(String rep, TyphKey typhkey, List<TyphEle> typhEles) {
		Map<Integer, TyphEle> elementMap = new HashMap<Integer, TyphEle>();
		// 要素表条数 （预报时效个数）  V04320_041
		typhkey.setNumOfForecastEfficiency(0);
		TyphEle typhEle = null;
		// 编报中心
		typhkey.setReportCenter(typhkey.getReportCenter());
		// 编报中心代码
	    int code = 999998;
		if(center.get(typhkey.getReportCenter()) != null)
			code = center.get(typhkey.getReportCenter());
		typhkey.setReportCenterCode(code);
		// 台风等级
		typhkey.setTyphLevel("999998");
		// 产品类型
		typhkey.setProductType(999998);
		
		rep = rep.replace("(\r\n)|\n|\r", " ");
		// 台风国内编号、国际编号
		typhkey.setInternalCode(999998);
		typhkey.setInternationalCode(999998);
		
		Boolean isPrevFound = false;
		int end = 0;
		int start = 0;
		String curr;
		String tmp;
		// 起报时间：年 月 日 时 世界时  例如：DTG: 20170828/0000Z
		Pattern reg = Pattern.compile("(DTG\\s*:\\s+)([0-9]{8}\\/[0-9]{4})(Z)");
		Matcher matcher = reg.matcher(rep);
		if(matcher.find()){
			isPrevFound = true;
			end = matcher.end();
			start = matcher.start();
			curr = rep.substring(start, end);
			tmp = matcher.group(2);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd/HHmm");
			Date date = new Date();
			try {
				date.setTime(simpleDateFormat.parse(tmp).getTime());
			} catch (ParseException e) {
				ReportError re = new ReportError();
				re.setMessage("Start report time error!");
				re.setSegment(tmp);
				parseResult.put(re);
			}
			typhkey.setObservationTime(date);
			
		}else
			isPrevFound = false;
		if(isPrevFound)
			rep = rep.substring(end);
		
		//台风名称
		reg = Pattern.compile("(TC\\s*:\\s+)([a-zA-Z0-9|\\-\\_\\\\]+)");
		matcher = reg.matcher(rep);
		if(matcher.find()){
			isPrevFound = true;
			end = matcher.end();
			start = matcher.start();
			curr = rep.substring(start, end);
			tmp = matcher.group(2);
			typhkey.setTyphName(tmp.trim());
		}
		else
			isPrevFound = false;
		if(isPrevFound)
			rep = rep.substring(end);
		
		// 实况中心位置：N1920 E14705 (北纬19度20分，东经147度05分)  
		typhEle = new TyphEle();
		typhEle.setForecastEfficiency(0);
		reg = Pattern.compile("(PSN\\s*:\\s+)((N|S)[0-9|\\/]{4}\\s+)((E|W)[0-9]{5})");  // "\\/" 缺测
		matcher = reg.matcher(rep);
		if(matcher.find()){
			isPrevFound = true;
			end = matcher.end();
			start = matcher.start();
			curr = rep.substring(start, end);
			tmp = matcher.group(2);
			typhEle.setLatitude(getLatLon(tmp));
			tmp = matcher.group(4);
			typhEle.setLongtitude(getLatLon(tmp));
		}
		else {
			isPrevFound = false;
		}
		if(isPrevFound)
			rep = rep.substring(end);
		
		//MOV:  NNW 03KT-----------移动的方向和速度：北西北 3海里/小时（需换算5.556公里/小时）
		reg = Pattern.compile("(MOV\\s*:\\s+)([A-Z]+\\s+)([0-9|\\/]+)(KT)"); // "\\/" 缺测情况
		matcher = reg.matcher(rep);
		if(matcher.find()){
			isPrevFound  = true;
			end = matcher.end();
			start = matcher.start();
			curr = rep.substring(start, end);
			tmp = matcher.group(2).trim();
			typhEle.setMovingDir(GetWindAzimuth(tmp));
			tmp = matcher.group(3);
			try{
				typhEle.setMovingSpeed(Double.parseDouble(tmp) * 1852.0 / 3600.0); //海里/小时转化为 米/秒
			}
			catch (Exception e) {
				typhEle.setMovingSpeed(999999);
			}
		}
		else{
			isPrevFound = false;
		}
		if(isPrevFound == true)
			rep = rep.substring(end);
		
		// C:  1002HPA--------中心气压965 百帕
		reg = Pattern.compile("(C\\s*:\\s+)([0-9|\\/]+)(\\s*HPA)");
		matcher = reg.matcher(rep);
		if(matcher.find()){
			isPrevFound = true;
			end = matcher.end();
		    start = matcher.start();
		    tmp = matcher.group(2);
		    try{
		    	typhEle.setCenterPressure(Double.parseDouble(tmp));
		    }catch (Exception e) {
		    	typhEle.setCenterPressure(999999);
			}
		}
		else
			isPrevFound = false;
		if(isPrevFound)
			rep = rep.substring(end);
		// MAX WIND:            30KT-------中心附近最大地面风：30海里/小时
		reg = Pattern.compile("(MAX\\s*WIND\\s*:\\s+)([0-9|\\/]+)(\\s*KT)");
		matcher = reg.matcher(rep);
		if(matcher.find()){
			isPrevFound = true;
			end = matcher.end();
			start = matcher.start();
			curr = rep.substring(start, end);
			tmp = matcher.group(2);
			try{
				typhEle.setGustSpeed(Double.parseDouble(tmp) * 1852.0 / 3600.0); //海里/小时转化为 米/秒
			}catch (Exception e) {
				typhEle.setGustSpeed(999999);
			}
		}
		else
			isPrevFound = false;
		if(isPrevFound)
			rep = rep.substring(end);
		elementMap.put(typhEle.getForecastEfficiency(), typhEle);
		// 预报
		reg = Pattern.compile("(FCST\\s+PSN\\s+\\+)(\\d+)(\\s*HR\\s*:\\s+\\d+\\/\\d+Z\\s+)((N|S)[0-9|\\/]{4}\\s+)((E|W)[0-9\\/]{5})");
		// 地面最大风（最大阵风速）
		Pattern wind  = Pattern.compile("(FCST\\s+MAX\\s+WIND\\s+\\+)(\\d+)(\\s*HR\\s*:\\s+)([0-9|\\/]+)(KT)");
		
		while((matcher = reg.matcher(rep)).find()){
			start = matcher.start();
			end = matcher.end();
			curr = rep.substring(start, end);
			typhEle = new TyphEle();
			tmp = matcher.group(2); // 时效
			typhEle.setForecastEfficiency(Integer.parseInt(tmp));
			tmp = matcher.group(4); // 纬度
			typhEle.setLatitude(getLatLon(tmp));
			tmp = matcher.group(6); // 经度
			typhEle.setLongtitude(getLatLon(tmp));
			// 最大阵风速
			rep = rep.substring(matcher.end());
			matcher = wind.matcher(rep);
			if(matcher.find()){
				isPrevFound = true;
				curr = rep.substring(matcher.start(), matcher.end());
				tmp = matcher.group(2);
				if(Integer.parseInt(tmp) == typhEle.getForecastEfficiency()){
					tmp = matcher.group(4); //  最大阵风速
					try{
						typhEle.setGustSpeed(Double.parseDouble(tmp) * 1852.0 / 3600.0); //海里/小时转化为 米/秒
					}catch (Exception e) {
						typhEle.setGustSpeed(999999);
					}
				}
				rep = rep.substring(matcher.end());
			}
			else 
				isPrevFound = false;
			elementMap.put(typhEle.getForecastEfficiency(), typhEle);
		}// end while
		
		typhkey.setNumOfForecastEfficiency(elementMap.size());
		typhEles.addAll(elementMap.values());
		
		return 0;
	}
	
	/**
	 * 解析风的方向角   
	 * @param str 待解析的字符串
	 * @return float  解析结果：>=0解码成功；<0 解码有错误，失败
	 */
	float GetWindAzimuth(String str){
		float azimuth = 0;
		str = str.trim();
		if(str.compareTo("N") == 0 || str.compareTo("NORTH") == 0)
			azimuth = 0;
		else if(str.compareTo("NNE") == 0 || str.compareTo("NORTHNORTHEAST") == 0 || str.compareTo("NORTH-NORTHEAST") == 0)
			azimuth = (float) 22.5;
		else if(str.compareTo("NE") == 0 || str.compareTo("NORTHEAST") == 0)
			azimuth = 45;
		else if(str.compareTo("ENE") == 0 || str.compareTo("EASTNORTHEAST") == 0 || str.compareTo("EAST-NORTHEAST") == 0)
			azimuth = (float) 67.5;
		else if(str.compareTo("E") == 0 || str.compareTo("EAST") == 0)
			azimuth = 90;
		else if(str.compareTo("ESE") == 0 || str.compareTo("EASTSOUTHEAST") == 0 || str.compareTo("EAST-SOUTHEAST") == 0)
			azimuth = (float) 112.5;
		else if(str.compareTo("SE") == 0 || str.compareTo("SOUTHEAST") == 0)
			azimuth = 135;
		else if(str.compareTo("SSE") == 0 || str.compareTo("SOUTHSOUTHEAST") == 0 || str.compareTo("SOUTH-SOUTHEAST") == 0)
			azimuth = (float) 157.5;
		else if(str.compareTo("S") == 0 || str.compareTo("SOUTH") == 0)
			azimuth = 180;
		else if(str.compareTo("SSW") == 0 || str.compareTo("SOUTHSOUTHWEST") == 0 || str.compareTo("SOUTH-SOUTHWEST") == 0)
			azimuth = (float) 202.5;
		else if(str.compareTo("SW") == 0 || str.compareTo("SOUTHWEST") == 0)
			azimuth = 225;
		else if(str.compareTo("WSW") == 0 || str.compareTo("WESTSOUTHWEST") == 0 || str.compareTo("WEST-SOUTHWEST") == 0)
			azimuth = (float) 247.5;
		else if(str.compareTo("W") == 0 || str.compareTo("WEST") == 0)
			azimuth = 270;
		else if(str.compareTo("WNW") == 0 || str.compareTo("WESTNORTHWEST") == 0 || str.compareTo("WEST-NORTHWEST") == 0)
			azimuth = (float) 292.5;
		else if(str.compareTo("NW") == 0 || str.compareTo("NORTHWEST") == 0)
			azimuth = 315;
		else if(str.compareTo("NNW") == 0 || str.compareTo("NORTHNORTHWEST") == 0 || str.compareTo("NORTH-NORTHWEST") == 0)
			azimuth = (float) 337.5;
		else if(str.compareTo("ALMOST") == 0) 
			azimuth = 0;
		else azimuth = -1;
		return azimuth;
	}
	/**
	 * @Title: getLatLon   
	 * @Description: 经纬度解析换算   
	 * @param latOrLonStr
	 * @return double      
	 * @throws：
	 */
	static double getLatLon(String latOrLonStr){
		String up = latOrLonStr.toUpperCase().trim();
		String dir = up.substring(0, 1);
		String val = up.substring(1);
		double returnVal = 999999;
		if(dir.equals("N") || dir.equals("S")){
			try{
				returnVal = Double.parseDouble(val.substring(0, 2)) + Double.parseDouble(val.substring(2)) / 60;
				if(dir.equals("S"))
					returnVal = -returnVal;
			}
			catch (Exception e) {
				
			}
		}
		else if(dir.equals("E") || dir.equals("W")){
			try{
				returnVal = Double.parseDouble(val.substring(0, 3)) + Double.parseDouble(val.substring(3)) / 60;
				if(dir.equals("W"))
					returnVal = -returnVal;
			}catch (Exception e) {
				
			}
		}
		return returnVal;
	}
	/**
	 * @Title: getCenterCode   
	 * @Description: 根据编报中心，获取对应编报中心代号  
	 * @param CCCC
	 * @return double      
	 * @throws：
	 */
	static void CenterCode(){
//		110	VHHH
//		201	RPMM
//		28	DEMS
//		34	RJTD
//		36	VIBB
//		38	BABJ
//		40	RKSL
//		999	PGTW
		center = new HashMap<String, Integer>();
		center.put("VHHH", 110);
		center.put("RPMM", 201);
		center.put("DEMS", 28);
		center.put("RJTD", 34);
		center.put("VIBB", 36);
		center.put("BABJ", 38);
		center.put("RKSL", 40);
		center.put("PGTW", 999);
	}
	public static void main(String[] args) {
		DecodeTyphFK decodeTyph = new DecodeTyphFK();
		ParseResult<Typh> parseResult = decodeTyph.DecodeFile(new File
				("D:\\HUAXIN\\DataProcess\\Typh_RJTD\\FK\\A_FKPZ24KNHC131432_C_BABJ_20190813144006_75145.txt"));
		List<Typh> list = parseResult.getData();
		TyphKey key = list.get(0).getTyphKey();
		List<TyphEle> eles = list.get(0).getTyphEles();
		System.out.println("台风名称、台风级别、台风国际编号： " + key.getTyphName() + " " + key.getTyphLevel() + " " + key.getInternalCode());
//		System.out.println("纬度、经度：" + eles.get(0).getLatitude() + " " + eles.get(0).getLongtitude());
	}
}