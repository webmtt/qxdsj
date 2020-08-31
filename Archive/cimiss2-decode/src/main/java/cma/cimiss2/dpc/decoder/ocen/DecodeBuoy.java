package cma.cimiss2.dpc.decoder.ocen;

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

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.ocean.Buoy;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * Main class of decode the Sea Buoy data <br>
 * 国内海上浮标资料解码类
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
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cimiss2.dpc.decoder.bean.ocean.Buoy}。
 * </ol>
 * </li>
 * </ul>
 * <strong> sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br>
 * 58878 261028N 1223020E 00100 0020 0000 ////MCA204 1<br>
20180610073000 0055 0280 //// //// //// //// //// //// //// //// 0012 0074 0008 0067 0017 //// 0067 //// //// //// //// //// ////=<br>
NNNN<br>
 * <strong>code:</strong><br>
 * DecodeBuoy decodeBuoy = new DecodeBuoy();<br>
 * ParseResult<Buoy> parseResult = decodeBuoy.DecodeFile(new File(String filepath));<br>
 * List<ReportInfo> list = parseResult.getReports();<br>
 * System.out.println(parseResult.getData().size());<br>
 * <strong>output:</strong><br>
 * 1<br>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月27日 下午2:43:26   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class DecodeBuoy {
	/**
	 * 解码结果封装
	 */
	private ParseResult<Buoy> parseResult = new ParseResult<Buoy>(false);
	/**
	 * 缺测浮点数替代值
	 */
	private static double defaultF = 999999; 
	/**
	 * 缺测整型值替代值
	 */
	private static int defaultI = 999999;
	/**
	 * 海上浮标资料解码主函数   
	 * @param  file 待解析文件    
	 * @return ParseResult<Buoy>    解码结果封装  
	 */
	
	@SuppressWarnings("resource")
	public ParseResult<Buoy> DecodeFile(File file){
		if (file != null && file.exists() && file.isFile()) {
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			// 获取文件的编码
			FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();     
			@SuppressWarnings("static-access")
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
					Buoy buoy = new Buoy();
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
							buoy.setStationNumberChina(h_sp[0]);
							// group 2 纬度
							String latitude = h_sp[1];
							if(latitude.contains("000000")){
								ReportError re = new ReportError();
								re.setMessage("Latitude error!");
								re.setSegment(latitude);
								parseResult.put(re);
								continue;
							}
							if(latitude.equals("999999") || latitude.contains("/"))
								buoy.setLatitude(defaultF);
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
								buoy.setLatitude(lati);
							}
							else{
								ReportError re = new ReportError();
								re.setMessage("Latitude parsing error");
								re.setSegment(sp[0]);
								parseResult.put(re);
								continue;
							}
							// group3  经度
							String longtitude = h_sp[2];
							if(longtitude.contains("0000000")){
								ReportError re = new ReportError();
								re.setMessage("Longtitude error!");
								re.setSegment(longtitude);
								parseResult.put(re);
								continue;
							}
							if(longtitude.equals("999999") || longtitude.contains("/"))
								buoy.setLongtitude(defaultF);
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
								buoy.setLongtitude(longti);
							}
							else {
								ReportError re = new ReportError();
								re.setMessage("Error of longitude parsing");
								re.setSegment(sp[0]);
								parseResult.put(re);
								continue;
							}
							// group 4  测站高度
							buoy.setHeightOfStationGroundAboveMeanSeaLevel(getValueF(h_sp[3], 10));
							// group 5  海盐传感器距海面深度
							buoy.setDepthOfSeasaltSensor(getValueF(h_sp[4], 10));
							// group 6  波高传感器距海面高度
							buoy.setHeightOfWaveHeightSensor(getValueF(h_sp[5], 10));
							// group 7 采集器型号
							String collector = h_sp[6];
							int idx = collector.lastIndexOf("/");
							if(idx > -1) 
								if(idx < collector.length() - 1) // 去除前面填充的 //
									collector = collector.substring(idx + 1);
								else  // 缺测
									collector = "999999";
							buoy.setCollectorType(collector);
							// group 8 站类标识
							buoy.setStationType(getValueI(h_sp[7]));
						}
						// 解析每条报文的第二行
						if(sp.length == 2){
							String e_sp[] = sp[1].split("\\s+");
							if(e_sp.length != 24){
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
									buoy.setObservationTime(date);
									//2019-7-16 cuihongyuan
									if(!TimeCheckUtil.checkTime(buoy.getObservationTime())){
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
								parseResult.setSuccess(true);
								parseResult.put(new ReportInfo<Buoy>(buoy, report));
								// group 2 ~ 24
								buoy.setBuoyDir(getValueI(e_sp[1])); //  浮标方位
								buoy.setSeaTemperature(getValueF(e_sp[2], 10));//海表温度
								buoy.setMaxSeaTemperature(getValueF(e_sp[3], 10));//海表最高温度
								buoy.setTimeOfmaxSeaTemperature(getValueI(e_sp[4]));//海表最高出现时间
								buoy.setMinSeaTemperature(getValueF(e_sp[5], 10));//海表最低温度
								buoy.setTimeOfMinSeaTemperature(getValueI(e_sp[6]));//海表最低出现时间
								buoy.setSalinity(getValueF(e_sp[7], 10));//海水盐度
								buoy.setAvgSalinity(getValueF(e_sp[8], 10));//海水平均盐度
								buoy.setElectricalConductivity(getValueF(e_sp[9], 100));//海水电导率
								buoy.setAvgElectricalConductivity(getValueF(e_sp[10], 100));//海水平均电导率
								buoy.setSignificantWaveHeight(getValueF(e_sp[11], 10));//有效波高
								buoy.setCycleOfSignificantWaveHeight(getValueF(e_sp[12], 10));//有效波高的周期
								buoy.setAvgWaveHeight(getValueF(e_sp[13], 10));//平均波高
								buoy.setCycleOfAvgWave(getValueF(e_sp[14], 10));//平均波周期
								buoy.setMaxWaveHeight(getValueF(e_sp[15], 10));//最大波高
								buoy.setTimeOfMaxWaveHeight(getValueI(e_sp[16]));//最大波高出现时间
								buoy.setCycleOfMaxWaveHeight(getValueF(e_sp[17], 10));//最大波高的周期
								buoy.setCurrentVelocity(getValueF(e_sp[18], 10));//表层海洋面流速
								buoy.setCurrentDir(getValueI(e_sp[19]));//表层海洋面波向
								buoy.setSeaTurbidity(getValueI(e_sp[20]));//海水浊度
								buoy.setAvgSeaTurbidity(getValueI(e_sp[21]));//海水平均浊度
								buoy.setChlorophyllConcentration(getValueI(e_sp[22]));//海水叶绿素浓度
								buoy.setAvgChlorophyllConcentration(getValueI(e_sp[23]));//海水平均叶绿素浓度

								parseResult.setSuccess(true);
								parseResult.put(buoy);
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
		} else {
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
		}
		return parseResult;
	}
	/**
	 * 解析获得浮点型数值   
	 * @param str 待解析字符串
	 * @param factor  数值放缩因子    
	 * @return double      解析结果
	 */
	public double getValueF(String str, int factor){
		if(str.charAt(0) == '/')
			return defaultF;
		else{
			return Double.parseDouble(str) / factor;
		}
	}
	/**
	 * 解析获得整型数值   
	 * @param str  待解析字符
	 * @return int   解析结果    
	 */
	public int getValueI(String str){
		if(str.charAt(0) == '/')
			return defaultI;
		else{
			return Integer.parseInt(str);
		}
	}
	
	public static void main(String[] args) {
		DecodeBuoy decodeBuoy = new DecodeBuoy();
		ParseResult<Buoy> parseResult = decodeBuoy.DecodeFile(new File("D:\\CIMISS2\\wind\\Z_OCEN_I_A5902-REG_20181226002000_O_AWS_FTM.txt"));
		
		System.out.println(parseResult.getData().size());
	}
}
