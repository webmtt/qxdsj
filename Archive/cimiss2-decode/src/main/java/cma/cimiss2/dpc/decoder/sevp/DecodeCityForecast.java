package cma.cimiss2.dpc.decoder.sevp;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.sevp.CityWeatherForeCast;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.sevp.BullHeader;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
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
import java.util.List;
import java.util.Scanner;
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * Main class of decode City Weather Forecast data <br>
 * 城镇天气预报报告（FS）解码类
 *
 * <p>
 * notes:
 * 解码程序分为两个步骤:数据读取及预处理、实体对象构建及赋值。
 * <ul>
 * <li>数据读取及预处理过程：
 * <ol>
 * <li>无特殊说明时，所有包含”/”的数据全部转化成999999。
 * <li>未通过数据校验时，该条数据将会被当成错误报文处理。
 * </ol>
 * </li>
 * <li>实体对象构建及赋值：
 * <ol>
 * <li>校验所有段的数据的数据类型是否合法。
 * <li>所有的赋值失败都会导致解码错误，该条数据将被当成错误报文处理。
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cimiss2.dpc.decoder.bean.sevp.CityWeatherForeCast}。
 * </ol>
 * </li>
 * </ul>
 * 
 * <strong> sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br> 
 * ZCZC 180<br>

FSXX40 BABJ 140600 <br>

47662 00000 10000 21726=<br>
NNNN<br>
 * <strong>code:</strong><br>
 * DecodeCityForecast decodeCityForecast = new DecodeCityForecast();<br>
 * ParseResult<CityWeatherForeCast> parseResult = decodeCityForecast.DecodeFile(new File(String filepath));<br>
 * List<CityWeatherForeCast> list = parseResult.getData();<br>
 * System.out.println(list.size());<br>
 * System.out.println(list.get(0).getStationNumberChina());<br>
 * <strong>output:</strong><br>
 * 1<br>
 * 47662<br>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月28日 下午3:46:07   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class DecodeCityForecast{
	/**
	 * 存储解码结果集
	 */
	private ParseResult<CityWeatherForeCast> parseResult = new ParseResult<CityWeatherForeCast>(false);
	/**
	 * 缺省值
	 */
	private double defaultF = 999999.0;
	/**
	 * 资料时间（年月）
	 */
	String yyyyMM = "";  
	/**
	 * 城镇天气预报报告解码主函数  
	 * @param file 待解码文件 
	 * @return ParseResult<CityWeatherForeCast>     解码结果封装
	 */
	@SuppressWarnings("resource")
	public ParseResult<CityWeatherForeCast> DecodeFile(File file){
		if (file != null && file.exists() && file.isFile()) {
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			// 获得资料日期
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHH");
			String filePath = file.getPath();
			String sp[] = filePath.split("\\" + System.getProperty("file.separator")); 
			Date oDate = new Date(); // 观测时间
			// 从文件路径、文件名解析信息
			if(sp.length >= 2){ 
				try{
					oDate = simpleDateFormat.parse(sp[sp.length - 2]);
				}catch(ParseException e){
					ReportError re = new ReportError();
					re.setMessage("DataTime format error!");
					re.setSegment(filePath);
					parseResult.put(re);
					return parseResult;
				}
			}
			else{ // 无法获取资料日期，返回
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
				return parseResult;
			}
			yyyyMM = sp[sp.length - 2].substring(0, 6);
			// 获取文件的编码
			FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();     
			String fileCode = fileEncodeUtil.javaname[fileEncodeUtil.detectEncoding(file)];
			InputStreamReader read = null;
			Scanner scanner = null;
			 // 获取文件流
			 try{
	        	read = new InputStreamReader(new FileInputStream(file), fileCode);
				scanner = new Scanner(read).useDelimiter("NNNN"); 
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
				String report = "";
				while (scanner.hasNext() && (report = scanner.next()).length() >= 3) {  
					// 移除空行 
					report = report.replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1").replaceAll("^((\r\n)|\n)", "");
					String reports[] = report.split("\n|(\r\n)");
					if(reports.length < 3) {
						ReportError re = new ReportError();
						re.setMessage("Report format error!");
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
						ReportError re = new ReportError();
						re.setMessage("Report header: data type beyond process scope!");
						re.setSegment(report);
						parseResult.put(re);
						continue;
					}
					// 2. 一个编报中心的资料, 第二行标识判断
					reports[1] = reports[1].trim();
					BullHeader bullHeader = new BullHeader();
					int ret = BullHeader.decodeHeader(reports[1], bullHeader);
					if(ret < 0){ // 报头格式错误
						ReportError re = new ReportError();
						re.setMessage("Report header format error or null!");
						re.setSegment(reports[1]);
						parseResult.put(re);
						continue;
					}
					 // 资料时间
					Date dataTime;
					try { 
						dataTime = sdf.parse(yyyyMM + bullHeader.getYYGGgg());
						dataTime.setSeconds(0);
						if(!TimeCheckUtil.checkTime(dataTime)){
							ReportError re = new ReportError();
							re.setMessage("DataTime out of range：time:"+dataTime);
							re.setSegment(report);
							parseResult.put(re);
							continue;
						}
					} catch (ParseException e) {
						ReportError re = new ReportError();
						re.setMessage("DataTime formate error!");
						re.setSegment(reports[1]);
						parseResult.put(re);
						continue;
					}
					// 移除前两行， 取得以“=”分隔的报文
					report = "";
					for(int i = 2; i < reports.length; i ++)
						report += (reports[i] + " ");
					reports = report.trim().split("=");
					int segNum = 0;
					String tmp;
					int weather[] = new int[2];
					int wind[] = new int[4];
					int temp[] = new int[2];
					String station = "";
					for(int i = 0; i < reports.length; i ++){
						reports[i] = reports[i].replaceAll("(\r\n)|\n", " ");
						reports[i] = reports[i].replaceAll("\\s+", " ").trim(); 
						char s = reports[i].trim().charAt(0);   
//						if(s == 65279){    //65279是空字符   
//						  if(reports[i].length() > 1){   
//							  reports[i] = reports[i].substring(1); 
//						  }   
//						}
						sp = reports[i].split(" ");
						segNum = sp.length;
						if(segNum >= 1)
							station = sp[0];
						else{
							ReportError re = new ReportError();
							re.setMessage("Station error!");
							re.setSegment(reports[i]);
							parseResult.put(re);
							continue;
						}
						int effectHours = 0;  // 时效
						int cnt = 0; // 序号
						List<CityWeatherForeCast> cityWeatherForeCasts = new ArrayList<CityWeatherForeCast>();
						for(int j = 1; j < segNum; j ++){
							CityWeatherForeCast cityWeatherForeCast = null;
							tmp = sp[j];
							char c = tmp.charAt(0);
							if(c == '0' || c == '3' || c == '6' || c == '9' || c == 'C'){
								if(c == 'C')
									cnt = 12 / 3;
								else
									cnt = Character.getNumericValue(c) / 3;
								ret = getWeatherPheno(tmp, weather);
								if(ret == 1){
									effectHours = (cnt + 1) * 24;
									cityWeatherForeCast = new CityWeatherForeCast();
									cityWeatherForeCast.setObservationTime(dataTime);
									cityWeatherForeCast.setStationNumberChina(station);
									cityWeatherForeCast.setForecastEfficiency(effectHours);
									cityWeatherForeCast.setBullHeader(bullHeader);
									cityWeatherForeCast.setWeatherPhenomenon20_08(weather[0]);
									cityWeatherForeCast.setWeatherPhenomenon08_20(weather[1]);
									cityWeatherForeCasts.add(cityWeatherForeCast);
									parseResult.put(cityWeatherForeCast);
									parseResult.setSuccess(true);
								}
							}
							else if(c == '1'|| c == '4' ||c == '7' ||c == 'A' ||c == 'D'){
								if(c == 'A')
									cnt = 10 / 3;
								else if(c == 'D')
									cnt = 13 / 3;
								else
									cnt = Character.getNumericValue(c) / 3;
								ret = getWind(tmp, wind);
								if(ret == 1){
									effectHours = (cnt + 1) * 24;
									if(cityWeatherForeCasts.size() < cnt + 1){
										cityWeatherForeCast = new CityWeatherForeCast();
										cityWeatherForeCast.setObservationTime(dataTime);
										cityWeatherForeCast.setStationNumberChina(station);
										cityWeatherForeCast.setForecastEfficiency(effectHours);
										cityWeatherForeCast.setBullHeader(bullHeader);
										cityWeatherForeCasts.add(cityWeatherForeCast);
										parseResult.put(cityWeatherForeCast);
										parseResult.setSuccess(true);
									}
									cityWeatherForeCast = cityWeatherForeCasts.get(cnt);
									cityWeatherForeCast.setWindDir(wind[0]);
									cityWeatherForeCast.setWindTurnDir(wind[1]);
									cityWeatherForeCast.setWindLevel(wind[2]);
									cityWeatherForeCast.setWindTurnLevel(wind[3]);
								}
							}
							else if(c == '2' || c == '5' || c =='8' || c == 'B' || c == 'E'){
								if(c == 'B')
									cnt = 11 / 3;
								else if(c == 'E')
									cnt = 14 / 3;
								else
									cnt = Character.getNumericValue(c) / 3;
								ret = getTemperature(tmp, temp);
								if(ret == 1){
									effectHours = (cnt + 1) * 24;
									if(cityWeatherForeCasts.size() < cnt + 1){
										cityWeatherForeCast = new CityWeatherForeCast();
										cityWeatherForeCast.setObservationTime(dataTime);
										cityWeatherForeCast.setStationNumberChina(station);
										cityWeatherForeCast.setForecastEfficiency(effectHours);
										cityWeatherForeCast.setBullHeader(bullHeader);
										cityWeatherForeCasts.add(cityWeatherForeCast);
										parseResult.put(cityWeatherForeCast);
										parseResult.setSuccess(true);
									}
									cityWeatherForeCast = cityWeatherForeCasts.get(cnt);
									cityWeatherForeCast.setMinTemperature(temp[0]);
									cityWeatherForeCast.setMaxTemperature(temp[1]);
								}
							}
							else{
								ReportError re = new ReportError();
								re.setMessage("Report format error!");
								re.setSegment(reports[i]);
								parseResult.put(re);
								break;
							}
						}
						// 用于报文表入库时使用
						if(cityWeatherForeCasts.size() > 0){
							parseResult.put(new ReportInfo<CityWeatherForeCast>(cityWeatherForeCasts.get(0), reports[i]));
						}
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
		}
		else {
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
		}
		return parseResult;
	}
	/**
	 * 天气现象解码   
	 * @param str  待解码字符串
	 * @param weatherPhone  存储解码结果
	 * @return int   成功返回1，失败返回-1
	 */
	int getWeatherPheno(String str, int []weatherPhone){
		if(isValidGroup(str) && weatherPhone.length >= 2){
			char c = str.charAt(0);
			if(c == '0' || c == '3' || c == '6'|| c == '9'|| c == 'C'){
				try{
					weatherPhone[0] = Integer.parseInt(str.substring(1, 3));
				}catch (Exception e) {
					weatherPhone[0] = (int)defaultF;
				}
				try{
					weatherPhone[1] = Integer.parseInt(str.substring(3, 5));
				}catch (Exception e) {
					weatherPhone[1] = (int)defaultF;
				}
				return 1;
			}
			else return -1;
		}
		else 
			return -1;
	}
	/**
	 * 风向、风的转向、风力、转后风力解码   
	 * @param str 待解码字符串
	 * @param wind 存储解码结果
	 * @return int  成功返回1，失败返回-1
	 */
	int getWind(String str, int []wind){
		if(isValidGroup(str) && wind.length >= 4){
			char c = str.charAt(0);
			if(c == '1' || c == '4' ||c == '7' ||c == 'A' ||c == 'D'){
				try{
					wind[0] = Integer.parseInt(str.substring(1, 2));
				}catch (Exception e) {
					wind[0] = (int)defaultF;
				}
				try{
					wind[1] = Integer.parseInt(str.substring(2, 3));
				}catch (Exception e) {
					wind[1] = (int)defaultF;
				}
				try{
					wind[2] = Integer.parseInt(str.substring(3, 4));
				}catch (Exception e) {
					wind[2] = (int)defaultF;
				}
				try{
					wind[3] = Integer.parseInt(str.substring(4, 5));
				}catch (Exception e) {
					wind[3] = (int)defaultF;
				}
				return 1;
			}
			else return -1;
		}
		else return -1;
	}
	/**
	 * 最低、最高气温解码   
	 * @param str 待解码字段
	 * @param temp 存储解码结果
	 * @return int    成功返回1，失败返回-1
	 */
	int getTemperature(String str, int []temp){
		if(isValidGroup(str) && temp.length >= 2){
			char c = str.charAt(0);
			if(c == '2' || c == '5' || c == '8' || c == 'B' || c == 'E'){
				try{
					temp[0] = Integer.parseInt(str.substring(1, 3));
					if(temp[0] > 50)
						temp[0] = 50 - temp[0];
				}
				catch(Exception e){
					temp[0] = (int)defaultF;
				}
				try{
					temp[1] = Integer.parseInt(str.substring(3, 5));
					if(temp[1] > 50)
						temp[1] = 50 - temp[1];
				}catch (Exception e) {
					temp[1] = (int)defaultF;
				}
				return 1;
			}
			else return -1;
		}
		else return -1;
	}
	/**
	 * 判断字符串str格式是否正确   
	 * @param str  待解码字符串
	 * @return boolean   true 或者 false
	 */
	boolean isValidGroup(String str){
		if(!str.isEmpty() && str.length() == 5){
			char c = str.charAt(0);
			if(c > 'E' || c < '0')
				return false;
//			for(int i = 1; i < 5; i ++){
//				c = str.charAt(i);
//				if(c > '9' || c < '0')
//					return false;
//			}
			return true;
		}
		else
			return false;
	}
	
	public static void main(String[] args) {
		DecodeCityForecast decodeCityForecast = new DecodeCityForecast();
		ParseResult<CityWeatherForeCast> parseResult = decodeCityForecast.DecodeFile(new File
				("D:\\DataTest\\sevp\\M.0001.0002.R001-城镇天气预报报告（FS）-云平台入库验证-崔红元提交\\201905\\2019050106\\FS_MSG__XX010640.ABJ"));
		List<CityWeatherForeCast> list = parseResult.getData();
		System.out.println(list.size());
		System.out.println(list.get(0).getStationNumberChina());
	}
	
}