package cma.cimiss2.dpc.decoder.ocen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.ocean.OriShip;

import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;

/**
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * Main class of decode the ship data <br>
 * 中远人工观测船舶资料解码
 *
 * <p>
 * notes:
 * 解码程序分为两个步骤:数据读取及预处理、实体对象构建及赋值。
 * <ul>
 * <li>数据读取及预处理过程：
 * <ol>
 * <li>所有运算采用java double进行基本运算。
 * <li>
 * <li>未通过数据校验时，该条数据将会被当成错误报文处理。
 * </ol>
 * </li>
 * <li>实体对象构建及赋值：
 * <ol>
 * <li>校验所有段的数据的数据类型是否合法。
 * <li>所有的赋值失败都会导致解码错误，该条数据将被当成错误报文处理。
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cimiss2.dpc.decoder.bean.ocean.Ship}。
 * </ol>
 * </li>
 * </ul>
 * 
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2019年7月30日 11:39:00   liym    Initial creation.
 * </pre>
 * 
 * @author liym
 * @version 0.0.1
 */
public class DecodeOriShip {
	/**
	 * 解码结果集
	 */
	private ParseResult<OriShip> parseResult = new ParseResult<OriShip>(false);
	/**
	 *  浮点数缺测时替代值
	 */
	private static double defaultF = 999999; 
	/**
	 *  整数缺测时替代值
	 */
	private static int defaultI = 999999;
	/**
	 *  字符串缺测时替代值
	 */
	private static String defaultS = "999999";
	
	/**
	 * 中远人工观测船舶资料解码主函数   
	 * @param file 待解析文件
	 * @return: ParseResult<Ship>      解码结果封装
	 */
	@SuppressWarnings({ "resource", "static-access" })
	public ParseResult<OriShip> DecodeFile(File file){
		if (file != null && file.exists() && file.isFile()) {
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			// 获取文件的编码
			InputStreamReader read = null;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
			Scanner scanner = null;
			 //获取文件流
	        try{
	        	FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();     
				String fileCode = fileEncodeUtil.javaname[fileEncodeUtil.detectEncoding(file)];
	        	read = new InputStreamReader(new FileInputStream(file), fileCode);
				scanner = new Scanner(read).useDelimiter("\n");// 用换行符读取文件
				while (scanner.hasNext()) {   //
					// 获取一行报文
					String report = scanner.next();
//					if(report.contains("NNNN"))
//						break; // 文件结束
					OriShip ship = new OriShip();
					// 移除空行 
//					report = report.replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1").replaceAll("^((\r\n)|\n)", "");
					String[] sp = report.split("/");//以斜杠分组
					if(sp.length >= 16){
						ship.setShipName(getValueS(sp[0]));//船舶名称
						ship.setShipCallSign(getValueS(sp[1]));//呼号
						Date date = new Date();
						try{
							date = simpleDateFormat.parse(sp[2]);
							Calendar cal = Calendar.getInstance();   
						    cal.setTime(date);   
						    cal.add(Calendar.HOUR, -8);//报文的北京时转换为世界时
						    date = cal.getTime();
							ship.setShiptime(date);//船位时间
							
							if(!TimeCheckUtil.checkTime(ship.getShiptime())){
								ReportError reportError = new ReportError();
								reportError.setMessage("time check error!");
								reportError.setSegment(sp[1]);
								parseResult.put(reportError);
								continue;
							}
						}catch(ParseException e){
							ReportError re = new ReportError();
							re.setMessage("Data time parsing error");
							re.setSegment(sp[2]);
							parseResult.put(re);
							continue;
						}

						// 经纬度
						if(sp[3].trim().contains("*")){ 
							ship.setLatitude(defaultF);
							ship.setLongtitude(defaultF);
						}else {
							String latlon[]=sp[3].trim().split("N|S|E|W");
							String latitude=latlon[0];
							String longtitude=latlon[1];
							if (latitude.length()>=4){//纬度
								int lat1 = Integer.parseInt(latitude.substring(0, 2));
								int lat2 = Integer.parseInt(latitude.substring(2, 4));
								double lati = lat1 + lat2 / 60.0;
								if(latitude.length() == 6){
									int lat3 = Integer.parseInt(latitude.substring(4, 6));
									lati +=  lat3 / 3600.0;
								}
								String NorS=sp[3].trim().substring(latitude.length(),latitude.length()+1);
								if("S".equals(NorS)){// 南纬
									lati = -lati;
								}
								ship.setLatitude(lati);
							}else{//若无法转换纬度，则赋缺测值999999
								ship.setLatitude(999999);
							}
							if(longtitude.length()>=5){//经度
								int lon1 = Integer.parseInt(longtitude.substring(0, 3));
								int lon2 = Integer.parseInt(longtitude.substring(3, 5));
								double longti = lon1 + lon2 / 60.0;
								if(longtitude.length() == 7){
									int lon3 = Integer.parseInt(longtitude.substring(5, 7));
									longti += lon3 / 3600.0;
								}
								String WorE=sp[3].trim().substring(sp[3].trim().length()-1);
								if("W".equals(WorE)){// 西经
									longti = -longti;
								}
								ship.setLongtitude(longti);
							}else {//若无法转换经度，则赋缺测值999999
								ship.setLongtitude(999999);
							}
						}
						ship.setWeatherCondition(getWeatherCode(sp[4].trim()));//天气情况
//							if (sp[5].contains("*")) {//海平面气压
//								ship.setSeaLevelPressure(defaultF);
//							}else{
//								String seaLevelPressure=String.format("%.1f",Double.parseDouble(sp[5].trim()));
//								double pressure=Double.parseDouble(seaLevelPressure);
//								ship.setSeaLevelPressure(pressure);//海平面气压
//							}
						ship.setSeaLevelPressure(getValueF(sp[5],1));//海平面气压
						ship.setDryballTemperature(getValueF(sp[6],1));//干球温度
						ship.setWetballTemperature(getValueF(sp[7],1));//湿球温度
						
						Matcher strMatcher1 = Pattern.compile("[a-zA-Z]+").matcher(sp[8].trim());
						String windDir=defaultS;
						String windSpeed=defaultS;
						while (strMatcher1.find()) {
							 windDir = strMatcher1.group(0);
							 windSpeed=sp[8].trim().substring(windDir.length(),sp[8].trim().length());//风速
						}
						if("*0".equals(sp[8].trim())){//报文值"*0"表示静风，风向赋值999017
							ship.setWindDir(999017);//风向
							ship.setWindSpeed(0);//风速
						}else{
							ship.setWindDir(field(windDir));//风向
							ship.setWindSpeed(getValueF(windSpeed,1));//风速
						}
//							String windlevel=defaultS;
//							Matcher strMatcher2 = Pattern.compile("\\d+").matcher(sp[8].trim());
//							while (strMatcher2.find()) {
//								 windlevel = strMatcher2.group(0);//风力级别
//							}
//							ship.setWindLevel(Integer.parseInt(windlevel));//风力级别
						ship.setSeaTemperature(getValueF(sp[9],1));//海水温度
						ship.setVisibilityLevel(getValueI(sp[10]));//能见度级别
						ship.setCloudShape(getValueI(sp[11]));//云状
						ship.setCloudAmount(getValueI(sp[12]));//云量
						ship.setWaveHeightManually(getValueF(sp[13],1));//浪高(人工测量)
						
						String moveSpeed=String.format("%.4f",getValueF(sp[14],1));
						ship.setShipMovingSpeed(Double.parseDouble(moveSpeed));//船舶航行速度(报文中单位：1米/秒,表结构单位：米/秒)
						ship.setBowAzimuth(getValueF(sp[15],1));//船艏方位
						
						parseResult.put(new ReportInfo<OriShip>(ship, report));
						parseResult.setSuccess(true);
						parseResult.put(ship);
						
					} 
				} 
			}catch (UnsupportedEncodingException e) {
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
		}else {
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
		}
		return parseResult;
	}
	
	/**
	 * 浮点值解析   
	 * @param str 待解析字符串
	 * @param factor  数值缩放因子     
	 * @return: double     解码结果 
	 */
	public double getValueF(String str, int factor){
		if(str.charAt(0) == '*')
			return defaultF;
		else{
			try{
				return Double.parseDouble(str) / factor;
			}catch (Exception e) {
				return defaultF;
			}
		}
	}
	/**
	 * 整型数值解析  
	 * @param str     待解析字符
	 * @return: int      解码结果
	 */
	public int getValueI(String str){
		if(str.charAt(0) == '*')
			return defaultI;
		else{
			try {
				return Integer.parseInt(str);
			} catch (Exception e) {
				return defaultI;
			}
		}
	}
	/**
	 * 字符串解析  
	 * @param str     待解析字符
	 * @return: string      解码结果
	 */
	public String getValueS(String str){
		if(str.charAt(0) == '*')
			return defaultS;
		else{
			return str;
		}
	}
	
	public int getWeatherCode(String str){
		if(str.charAt(0) == '*')
			return defaultI;
		else{
			if("B".equals(str))
				return 0;
			if("C".equals(str))
				return 1;
			if("O".equals(str))
				return 3;
			if("R".equals(str))
				return 21;
			if("S".equals(str))
				return 22;	
			if("F".equals(str))
				return 28;	
			if("G".equals(str))
				return 999999;	
			if("M".equals(str))
				return 10;	
			if("H".equals(str))
				return 5;
			if("T".equals(str))
				return 29;	
			else 
				return 999999;	
			}
		}
	/**
	 * 
	 * @Title: field
	 * @Description: TODO(风向方位转换函数)
	 * @param key
	 * @return Double
	 * @throws：
	 */
	public Double field(String key){
		switch(key) 
		{ 
		case "N": 
			return (double) 360; 
		
		case "NNE": 
			return (double) 22.5; 
		
		case "NE": 
			return (double) 45; 
			
		case "ENE": 
			return 67.5; 
		case "E": 
			return (double) 90; 
			
		case "ESE": 
			return 112.5;
		case "SE": 
			return (double) 135; 
		
		case "SSE": 
			return (double) 157.5; 
		
		case "S": 
			return (double) 180; 
			
		case "SSW": 
			return 202.5; 
		case "SW": 
			return (double) 225; 
			
		case "WSW": 
			return 247.5;
		case "W": 
			return (double) 270; 
		
		case "WNW": 
			return (double) 292.5; 
		
		case "NW": 
			return (double) 315; 
			
		case "NNW": 
			return 337.5;
			
		default: 
			return (double) 999999;
		} 
	}
	public static void main(String[] args) {
		DecodeOriShip decodeShip = new DecodeOriShip();
		ParseResult<OriShip> parseResult = decodeShip.DecodeFile(new File("D:\\中远人工观测船舶资料\\中远船舶样例数据\\ZY_M_20190724140030.txt"));
		List<OriShip> list = parseResult.getData();
		System.out.println(list.size());
		
		
	}
}
