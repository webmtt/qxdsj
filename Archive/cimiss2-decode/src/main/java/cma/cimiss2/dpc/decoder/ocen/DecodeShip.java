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

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.ocean.Ship;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;

/**
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * Main class of decode the ship data <br>
 * 海上船舶资料解码
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
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cimiss2.dpc.decoder.bean.ocean.Ship}。
 * </ol>
 * </li>
 * </ul>
 * 
 * <strong> sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br>
 * BPKI / / / / -------- -------- 1<br>
20180601120000 1225600E 331100N / 6.3 / 01 1016 25 23 337.5 / 4 18 12964.0 / / / / 1 -------- -------- -------- -------- -------- --------=<br>
BPKG / / / / -------- -------- 1<br>
20180601120000 1135800E 215400N / / / 01 1008 33 30 180 / 4 27 12964.0 / / / / 0.5 -------- -------- -------- -------- -------- --------=<br>
BPKJ / / / / -------- -------- 1<br>
20180601120000 1180600E 385700N / / / 01 1014 24 21 202.5 / 4 15 12964.0 / / / / 0.5 -------- -------- -------- -------- -------- --------=<br>
BPKK / / / / -------- -------- 1<br>
20180601120000 1215700E 273800N / 5.8 / 01 1013 25 23 22.5 / 3 21 12964.0 / / / / 0.3 -------- -------- -------- -------- -------- --------=<br>
NNNN<br>
 * <strong>code:</strong><br>
 *  DecodeShip decodeShip = new DecodeShip();<br>
 * 	ParseResult<Ship> parseResult = decodeShip.DecodeFile(new File(String filepath));<br>
 * 	List<Ship> list = parseResult.getData();<br>
 * 	System.out.println(list.size());<br>
 * <strong>output:</strong><br>
 * 4<br>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月27日 下午2:56:56   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class DecodeShip {
	/**
	 * 解码结果集
	 */
	private ParseResult<Ship> parseResult = new ParseResult<Ship>(false);
	/**
	 *  浮点数缺测时替代值
	 */
	private static double defaultF = 999999; 
	/**
	 *  整数缺测时替代值
	 */
	private static int defaultI = 999999;
	/**
	 * 海上船舶资料解码主函数   
	 * @param file 待解析文件
	 * @return: ParseResult<Ship>      解码结果封装
	 */
	@SuppressWarnings({ "resource", "static-access" })
	public ParseResult<Ship> DecodeFile(File file){
		if (file != null && file.exists() && file.isFile()) {
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			// 获取文件的编码
			FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();     
			String fileCode = fileEncodeUtil.javaname[fileEncodeUtil.detectEncoding(file)];
			InputStreamReader read = null;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			Scanner scanner = null;
			 //获取文件流
	        try{
	        	read = new InputStreamReader(new FileInputStream(file), fileCode);
				scanner = new Scanner(read).useDelimiter("="); // 用换行符读取文件
				while (scanner.hasNext()) {   //
					// 获取一个完整的报文段
					String report = scanner.next();
					if(report.contains("NNNN"))
						break; // 文件结束
					Ship ship = new Ship();
					// 移除空行 
					report = report.replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1").replaceAll("^((\r\n)|\n)", "");
					String[] sp = report.split("\n");
					if(sp.length >= 1){
						String h_sp[] = sp[0].split("\\s+");
						if(h_sp.length != 8){
							ReportError re = new ReportError();
							re.setMessage("The number of elements in the header is incorrect");
							re.setSegment(sp[0]);
							parseResult.put(re);
							continue;
						}else{
						// 解析每条报文的第一行
							// group 1
							ship.setShipID(h_sp[0]);
							// group 2~5
							try{
								ship.setStationHeightAboveSea(getValueF(h_sp[1], 10));
								ship.setHeightOfAirpressureSensor(getValueF(h_sp[2], 10));
								ship.setHeightOfWindSpeedSensor(getValueF(h_sp[3], 10));
								ship.setDistanceBetweenDeckAndSea(getValueF(h_sp[4], 10));
							}catch(Exception e){
								ReportError re = new ReportError();
								re.setMessage("Field format error,There are characters out of '/' and Numbers");
								re.setSegment(h_sp[1] + " "+ h_sp[2]+ " "+ h_sp[3]+ " "+ h_sp[4]);
								parseResult.put(re);
								continue;
							}
							// group 6,7 保留
							// group 8
							ship.setStationTypeStr(h_sp[7]);
							ship.setStationTypeInt(Integer.parseInt(h_sp[7]));
						}
						// 解析每条报文的第二行
						if(sp.length == 2){
							String e_sp[] = sp[1].split("\\s+");
							if(e_sp.length != 26){
								ReportError re = new ReportError();
								re.setMessage("The number of elements of a message entity is wrong");
								re.setSegment(sp[1]);
								parseResult.put(re);
								continue;
							}
							else{
								// group 1
								Date date = new Date();
								try{
									date = simpleDateFormat.parse(e_sp[0]);
									Calendar cal = Calendar.getInstance();   
								    cal.setTime(date);   
								    cal.add(Calendar.HOUR, -8);
								    date = cal.getTime();
									ship.setObservationTime(date);
									
									//2019-7-16 cuihongyuan
									if(!TimeCheckUtil.checkTime(ship.getObservationTime())){
										ReportError reportError = new ReportError();
										reportError.setMessage("time check error!");
										reportError.setSegment(sp[1]);
										parseResult.put(reportError);
										continue;
									}
									
								}catch(ParseException e){
									ReportError re = new ReportError();
									re.setMessage("Data time parsing error");
									re.setSegment(sp[1]);
									parseResult.put(re);
									continue;
								}
								// group 2
								String longtitude = e_sp[1];
								if(longtitude.equals("999999") || longtitude.equals("/"))
									ship.setLongtitude(defaultF);
								else if(longtitude.length() >= 6){
									int lon1 = Integer.parseInt(longtitude.substring(0, 3));
									int lon2 = Integer.parseInt(longtitude.substring(3, 5));
									double longti = lon1 + lon2 / 60.0;
									if(longtitude.length() == 8){
										int lon3 = Integer.parseInt(longtitude.substring(5, 7));
										longti += lon3 / 3600.0;
									}
									if(longtitude.charAt(longtitude.length() - 1) == 'W') // 西经
										longti = -longti;
									ship.setLongtitude(longti);
								}
								else {
									ReportError re = new ReportError();
									re.setMessage("Ship location (longitude) parsing error");
									re.setSegment(sp[1]);
									parseResult.put(re);
									continue;
								}
								// group 3
								String latitude = e_sp[2];
								if(latitude.equals("999999") || latitude.equals("/"))
									ship.setLatitude(defaultF);
								else if(latitude.length() >= 5){
									int lat1 = Integer.parseInt(latitude.substring(0, 2));
									int lat2 = Integer.parseInt(latitude.substring(2, 4));
									double lati = lat1 + lat2 / 60.0;
									if(latitude.length() == 7){
										int lat3 = Integer.parseInt(latitude.substring(4, 6));
										lati +=  lat3 / 3600.0;
									}
									if(latitude.charAt(latitude.length() - 1) == 'S') // 南纬
										lati = -lati;
									ship.setLatitude(lati);
								}
								else{
									ReportError re = new ReportError();
									re.setMessage("Ship's location (latitude) parsing error");
									re.setSegment(sp[1]);
									parseResult.put(re);
									continue;
								}
								try{
//								parseResult.setSuccess(true);
								parseResult.put(new ReportInfo<Ship>(ship, report));
								// group 4 ~ 20
								ship.setShipMovingDir(getValueI(e_sp[3]));
								ship.setShipMovingSpeed(getValueF(e_sp[4], 1));
								ship.setBowAzimuth(getValueF(e_sp[5], 1));
								ship.setWeatherCondition(getValueI(e_sp[6]));
								ship.setSeaLevelPressure(getValueF(e_sp[7], 1));
								ship.setDryballTemperature(getValueF(e_sp[8], 1));
								ship.setWetballTemperature(getValueF(e_sp[9], 1));
								ship.setWindDir(getValueF(e_sp[10], 1));
								ship.setWindSpeed(getValueF(e_sp[11], 1));
								ship.setWindLevel(getValueI(e_sp[12]));
								ship.setSeaTemperature(getValueF(e_sp[13], 1));
								ship.setVisibility(getValueF(e_sp[14], 1));
								ship.setCloudShape(getValueI(e_sp[15]));
								ship.setCloudAmount(getValueI(e_sp[16]));
								ship.setWaveLevel(getValueF(e_sp[17], 1));
								ship.setWaveHeightByInstrument(getValueF(e_sp[18], 1));
								ship.setWaveHeightManually(getValueF(e_sp[19], 1));
								// group 21 ~ 26 保留
								parseResult.setSuccess(true);
								parseResult.put(ship);
								}catch (Exception e) {
									ReportError re = new ReportError();
									re.setMessage("data format error！");
									re.setSegment(sp[1]);
									parseResult.put(re);
									continue;
								}
							} 
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
		if(str.charAt(0) == '/')
			return defaultF;
		else{
			return Double.parseDouble(str) / factor;
		}
	}
	/**
	 * 整型数值解析  
	 * @param str     待解析字符
	 * @return: int      解码结果
	 */
	public int getValueI(String str){
		if(str.charAt(0) == '/')
			return defaultI;
		else{
			return Integer.parseInt(str);
		}
	}
	
	public static void main(String[] args) {
		DecodeShip decodeShip = new DecodeShip();
		ParseResult<Ship> parseResult = decodeShip.DecodeFile(new File("D:\\TEMP\\C.1.6.1\\10-17\\Z_SURF_C_BABJ_20191015140051_O_SHIP-ZY-M.txt"));
		List<Ship> list = parseResult.getData();
		System.out.println(list.size());
	}
}
