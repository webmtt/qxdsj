package cma.cimiss2.dpc.decoder.upar;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.bean.upar.GPS_MET;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.tools.ElementValUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * Main class of decode GPS/MET vapor data <br> 
 * GPS/MET水汽数据资料解码类
 * <p>
 * notes:
 * 解码程序分为两个步骤:数据读取及预处理、实体对象构建及赋值。
 * <ul>
 * <li>数据读取及预处理过程：
 * <ol>
 * <li>所有运算采用java double进行基本运算。
 * <li>无特殊说明时，所有包含”/”的数据全部转化成999999;若一条报文的所有要素值均缺测时，该记录不入库。
 * <li>未通过数据校验时，该条数据将会被当成错误报文处理。
 * </ol>
 * </li>
 * <li>实体对象构建及赋值：
 * <ol>
 * <li>校验所有段的数据的数据类型是否合法。
 * <li>所有的赋值失败都会导致解码错误，该条数据将被当成错误报文处理。
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cimiss2.dpc.decoder.bean.upar.GPS_MET}。
 * </ol>
 * </li>
 * </ul>
 * 
 * <strong> sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br>
 * GNSS/MET Observations, Meteorological Observation Centre of CMA, Version 2.0<br>
7, Zenith total delay (mm), Pressure (hPa), Temperature (degree celsius), Relative humidity (percent),  Precipitable water vapor (mm), PWV Sigma (mm), ZTD Sigma (mm)<br>
    1075<br>
HYSQ 112.60 26.89 120.2 1<br>
2018 06 01 00 00 00 2637.8000 1002.6 20.2 92.0 56.73 1.18 7.3<br>
HOND 111.66 36.23 462.9 1<br>
2018 06 01 00 00 00 99999 99999 99999 99999 99999 99999 99999<br>
HHXP 110.60 27.91 211.1 1<br>
2018 06 01 00 00 00 2595.3000 991.1 18.8 100.0 53.95 1.24 7.7<br>
HKQH 110.46 19.23 44.5 1<br>
2018 06 01 00 00 00 99999 99999 99999 99999 99999 99999 99999<br>
HHZJ 109.68 27.46 290.8 1<br>
2018 06 01 00 00 00 2574.7000 983.7 18.1 100.0 53.25 1.42 8.8<br>
......<br>
 * 
 * <strong>code:</strong><br>
 * DecodeGPS_MET decodeGPS_MET = new DecodeGPS_MET();<br>
 * ParseResult<GPS_MET> parseResult = decodeGPS_MET.DecodeFile(new File(String filepath)));<br>
 * List<GPS_MET> list1 = parseResult.getData();<br>
 * System.out.println(list1.size());<br>
 * List<ReportInfo> list = parseResult.getReports();<br>
 * System.out.println(list.size());<br>
 * 
 * <strong>output:</strong><br>
 * 357<br>
 * 1075<br>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月27日 下午5:20:35   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class DecodeGPS_MET{
	/**
	 * 解码结果集
	 */
	private ParseResult<GPS_MET> parseResult = new ParseResult<GPS_MET>(false);
	/**
	 * GPS/MET水汽数据资料解码函数   
	 * @param  file 待解析文件   
	 * @return: ParseResult<GPS_MET>   解码结果集封装
	 */
	@SuppressWarnings("resource")
	public ParseResult<GPS_MET> DecodeFile(File file){
		if (file != null && file.exists() && file.isFile() ) {
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
//			if(file.getName().indexOf("GPSG") == -1){
//				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
//				return parseResult;
//			}
			// 获取文件的编码
			FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();     
			String fileCode = fileEncodeUtil.javaname[fileEncodeUtil.detectEncoding(file)];
			InputStreamReader read = null;
			String timeLine = "";
			Scanner scanner = null;
			 //获取文件流
	        try{
	        	read = new InputStreamReader(new FileInputStream(file), fileCode);
				scanner =  new Scanner(read).useDelimiter("(\r\n)|\n"); // 用换行符读取文件
				if(file.getName().indexOf("GPSG") >= 0){
					Date date = new Date(); // 一个文件一个date
					if(scanner.hasNext())
					 {
						scanner.next();	
					}
					if(scanner.hasNext()){
						timeLine = scanner.next();  // 第二行，日期 + 时间 （格式：年 月 日 时 分）
						int Year = Integer.parseInt(timeLine.substring(0, 2)) + 2000;
						timeLine = Year + timeLine.substring(2);
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy MM dd HH mm");
						try{
							date = simpleDateFormat.parse(timeLine);
						}catch (ParseException e) {
							ReportError re = new ReportError();
							re.setMessage("time format conversion exception or time field is empty");
							re.setSegment(timeLine);
							parseResult.put(re);
							return parseResult;
						}
					}
					if(scanner.hasNext())
					 {
						scanner.next();	
					}
					while (scanner.hasNext()) { 
						// 报文要素解析
						String report = scanner.next();
						// 移除空行 
						report = report.replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1").replaceAll("^((\r\n)|\n)", "");
						if(!report.equals("")){
							GPS_MET gps_MET = new GPS_MET();
							gps_MET.setObservationTime(date);  // 资料时间
							String sp[] = report.split("\\s+");
							if(sp.length == 11){
								gps_MET.setStationCode(sp[0]);  // 台站代码
								gps_MET.setStationNumberChina(sp[1]);
								if(!TimeCheckUtil.checkTime(date)){
									ReportError re = new ReportError();
									re.setMessage("Time out of range：time:"+date+" stationCode:"+sp[0]);
									re.setSegment(report);
									parseResult.put(re);
									continue;
								}
								try{
									gps_MET.setLatitude(Float.parseFloat(sp[2]));
									gps_MET.setLongtitude(Float.parseFloat(sp[3]));
									parseResult.put(new ReportInfo<GPS_MET>(gps_MET, report));
									gps_MET.setHeightOfSationGroundAboveMeanSeaLevel(Float.parseFloat(sp[4]));
									// 总的天顶延迟 |气压 |气温| 相对湿度 |可降水量|电子浓度 （最后一个字段不入库）
									// 如果要素值不全是缺测，则为有效数据，进行解析
//									if((!sp[5].equals("99999")) || (!sp[6].equals("99999"))|| (!sp[7].equals("99999"))|| (!sp[8].equals("99999"))|| (!sp[9].equals("99999"))|| (!sp[10].equals("99999"))){
										gps_MET.setTotalZenithDelay(getValue(sp[5]));
										gps_MET.setPressure(getValue(sp[6]));
										gps_MET.setAirTemperature(getValue(sp[7]));
										gps_MET.setRelativeHumidity(getValue(sp[8]));
										gps_MET.setPrecipitationAmount(getValue(sp[9]));
										gps_MET.setElectronic(getValue(sp[10]));
										parseResult.put(gps_MET);
										parseResult.setSuccess(true);
//									}
//									else{
//										ReportError re = new ReportError();
//										re.setMessage("All the elements are missing");
//										re.setSegment(report);
//										parseResult.put(re);
//										continue;
//									}
								}catch(Exception e){
									ReportError re = new ReportError();
									re.setMessage("A string to numeric conversion error");
									re.setSegment(report);
									parseResult.put(re);
									continue;
								}
							}
							else{
								ReportError re = new ReportError();
								re.setMessage("this report segment error");
								re.setSegment(report);
								parseResult.put(re);
								continue;
							}
						}
					} 
				}// end if
				else if(file.getName().indexOf("GPS2") >= 0){
					// 移除报文前三行
					if(scanner.hasNext())
						scanner.next();	
					if(scanner.hasNext())
						scanner.next();
					if(scanner.hasNext())
						scanner.next();
					while (scanner.hasNext()) { 
						// 报文要素解析
						String report = scanner.next();
						String rpt = report;
						// 移除空行 
//						report = report.replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1").replaceAll("^((\r\n)|\n)", "");
						String sp[] = report.split("\\s+");
						if(sp.length == 5){
							String stationCode = sp[0];
							float longtitude = 999999;
							float latitude = 999999;
							float height = 999999;
							int totalN = 0;
							int line = 0;
							try{
								longtitude = (float)ElementValUtil.ToBeValidDouble(sp[1]);
								latitude = (float)ElementValUtil.ToBeValidDouble(sp[2]);
								height = (float)(float)ElementValUtil.ToBeValidDouble(sp[3]);
								totalN = Integer.parseInt(sp[4]);
							}catch (Exception e) {
								ReportError reportError = new ReportError();
								reportError.setMessage("Station info error!");
								reportError.setSegment(report);
								parseResult.put(reportError);
								continue;
							}
							GPS_MET gps_MET = null;
							while(line < totalN && scanner.hasNext() && !(report = scanner.next()).equals("")){
								line ++;
								sp = report.split("\\s+");
								if(sp.length >= 11){
									gps_MET = new GPS_MET();
									gps_MET.setStationCode(stationCode);  // 台站代码
									gps_MET.setStationNumberChina("999999");
									gps_MET.setLongtitude(longtitude);
									gps_MET.setLatitude(latitude);
									gps_MET.setHeightOfSationGroundAboveMeanSeaLevel(height);
									Date date = new Date();
									date.setYear(Integer.parseInt(sp[0]) - 1900);
									date.setMonth(Integer.parseInt(sp[1]) - 1);
									date.setDate(Integer.parseInt(sp[2]));
									date.setHours(Integer.parseInt(sp[3]));
									date.setMinutes(Integer.parseInt(sp[4]));
									date.setSeconds(Integer.parseInt(sp[5]));
									gps_MET.setObservationTime(date);  // 资料时间
									if(!TimeCheckUtil.checkTime(date)){
										ReportError re = new ReportError();
										re.setMessage("Time out of range：time:"+date+" stationCode:"+stationCode);
										re.setSegment(report);
										parseResult.put(re);
										continue;
									}
									try{
										gps_MET.setTotalZenithDelay((float)ElementValUtil.ToBeValidDouble(sp[6]));
										gps_MET.setPressure((float)ElementValUtil.ToBeValidDouble(sp[7]));
										gps_MET.setAirTemperature((float)ElementValUtil.ToBeValidDouble(sp[8]));
										gps_MET.setRelativeHumidity((float)ElementValUtil.ToBeValidDouble(sp[9]));
										gps_MET.setPrecipitationAmount((float)ElementValUtil.ToBeValidDouble(sp[10]));
									}catch (Exception e) {
										ReportError reportError = new ReportError();
										reportError.setMessage("Element format error!");
										reportError.setSegment(report);
										parseResult.put(reportError);
										continue;
									}
									rpt += ("\n" + report);
									if(Math.abs(gps_MET.getTotalZenithDelay() - 999999.0) > 1e-5 ||
											Math.abs(gps_MET.getPrecipitationAmount() - 999999.0) > 1e-5||
											Math.abs(gps_MET.getPressure() - 999999.0) > 1e-5||
											Math.abs(gps_MET.getAirTemperature() - 999999.0) > 1e-5||
											Math.abs(gps_MET.getRelativeHumidity()-999999.0) > 1e-5){
										parseResult.setSuccess(true);
										parseResult.put(gps_MET);
									}
								}// end if
								else
									break;
							}//while
							parseResult.put(new ReportInfo<GPS_MET>(gps_MET, rpt));
							
						}// end if
					}// end while
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
	 * 数值型数据解析   
	 * @param str 待解析字符串
	 * @return float   解码结果   
	 */
	private float getValue(String str){
		if(str.equals("99999"))
			return 999999;
		else{
			return Float.parseFloat(str);
		}
	}
	
	public static void main(String[] args) {
		DecodeGPS_MET decodeGPS_MET = new DecodeGPS_MET();
		ParseResult<GPS_MET> parseResult = decodeGPS_MET.DecodeFile(new File
				("D:\\TEMP\\B.5.5.1\\11-1\\Z_UPAR_C_BATC_20191101000000_P_GPSG_vapor.txt"));
		List<GPS_MET> list1 = parseResult.getData();
		System.out.println(list1.size());
		List<ReportInfo> list = parseResult.getReports();
		System.out.println(list.size());
	}
	
}