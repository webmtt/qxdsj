package cma.cimiss2.dpc.decoder.radi;


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
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
import cma.cimiss2.dpc.decoder.bean.radi.AutomaticStationRadiatingHourlyData;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.ElementValUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 *
 * Main Class of decode Automatic station radiation hour data <br>
 *自动站辐射资料解码类<br>
 *
 * <p>
 * notes:
 * 解码程序分为两个步骤:数据读取及预处理、实体对象构建及赋值。
 * <ul>
 * <li>数据读取及预处理过程：
 * <ol>
 * <li>所有运算采用java double进行基本运算。
 * <li>无特殊说明时，所有包含”/”,或者字段值为代表缺测定义的值（如999999，9999999）的数据全部转化成999999。
 * <li>未通过数据校验时，该条数据将会被当成错误报文处理。
 * </ol>
 * </li>
 * <li>实体对象构建及赋值：
 * <ol>
 * <li>校验所有段的数据的数据类型是否合法。
 * <li>所有的赋值失败都会导致解码错误，该条数据将被当成错误报文处理。
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cimiss2.dpc.decoder.bean.radi.AutomaticStationRadiatingHourlyData}。
 * </ol>
 * </li>
 * </ul>
 * 
 * <strong> sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br>
 * 
 *57083 344200 1134000<br>
20180829080000 0378 0202 0361 0140 0082 0000 0085 0378 0800 0041 0202 0800 0050 0701 0049 0361 0800 0041 0141 0759 0018 0082 0800 0000 0000 0701 05 ////=<br>
NNNN<br>

<strong>code:</strong><br>
 * DecodeRadiSads sads = new DecodeRadiSads();<br>
 * ParseResult<AutomaticStationRadiatingHourlyData> parseResult = sads.DecodeFile(new File(String filepath));<br>
 * List<ReportInfo> list = parseResult.getReports();<br>
 * System.out.println(parseResult.isSuccess());<br>
 * System.out.println(list.size());<br>
 * <strong>output:</strong><br>
 * true<br>
 * 1<br>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月28日 下午5:08:39   maxiyue   Initial creation.
 * </pre>
 * 
 * @author maxiyue
 * @version 0.0.1
 */
public class DecodeRadiSads {
	/**
	 * 解码结果集封装
	 */
	private ParseResult<AutomaticStationRadiatingHourlyData> parseResult = new ParseResult<AutomaticStationRadiatingHourlyData>(false); // 存储解码结果
	/**
	 * 缺测时替代值
	 */
	private static double defaultF = 999999; // 缺测时替代值
	@SuppressWarnings("resource")
	/**
	 * @Description:(自动站辐射资料解码函数)
	 * @param file 待解码报文文件
	 * @return ParseResult<AutomaticStationRadiatingHourlyData> 解码结果集
	 */
	public ParseResult<AutomaticStationRadiatingHourlyData> DecodeFile(File file){
		if (file != null && file.exists() && file.isFile()) {
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			InputStreamReader read = null;
			Scanner scanner = null;
			// 获取文件的编码
			FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();     
			@SuppressWarnings("static-access")
			String fileCode = fileEncodeUtil.javaname[fileEncodeUtil.detectEncoding(file)];
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			try{  
				read = new InputStreamReader(new FileInputStream(file), fileCode);
				scanner = new Scanner(read).useDelimiter("="); 
				while (scanner.hasNext()) { 
					// 获取一个完整的报文段
					String report = scanner.next();
					if(report.contains("NNNN"))
						break; // 文件结束
					AutomaticStationRadiatingHourlyData sads = new AutomaticStationRadiatingHourlyData();
					// 移除空行 
					report = report.replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1").replaceAll("^((\r\n)|\n)", "");
					String[] sp = report.split("\n");
					if(sp.length >= 1){
						String h_sp[] = sp[0].split("\\s+");
						if(h_sp.length != 3){
							ReportError re = new ReportError();
							re.setMessage("The number of elements in the header is wrong");
							re.setSegment(sp[0]);
							parseResult.put(re);
							continue;
						}else{
						// 解析每条报文的第一行		
							sads.setStationNumberChina(h_sp[0]);//站号
							// 纬度解析
							String latitude = h_sp[1];
							try {
								if(latitude.equals("999999") || latitude.contains("/")){
									sads.setLatitude(defaultF);
									}
								else{
									double lat =ElementValUtil.getlatitude(h_sp[1]);
									sads.setLatitude(lat);
								}
							} catch (Exception e) {
								ReportError re = new ReportError();
								re.setMessage("(latitude) parsing error");
								re.setSegment(report);
								parseResult.put(re);
								continue;
							}					
							//经度解析
							String longtitude = h_sp[2];
							try {
								if(longtitude.equals("9999999") || longtitude.contains("/"))
									sads.setLongitude(defaultF);
								else {
									double lon = ElementValUtil.getLongitude(h_sp[2]);
									sads.setLongitude(lon);
								}
							} catch (Exception e) {
								ReportError re = new ReportError();
								re.setMessage("（Longitude）Parsing error");
								re.setSegment(report);
								parseResult.put(re);
								continue;
							}			
						}
						// 解析每条报文的第二行
						if(sp.length == 2){
							StringBuffer sb = new StringBuffer();
							sb.append(sp[1]);
							if (sp[1].substring(sp[1].length()-1).contains(" ")) {
								sb.append("999999");			
							}
							sp[1]= sb.toString();
							String report_ele =sp[1].trim();
							String e_sp[] = report_ele.split("\\s+");
							if(e_sp.length != 29){
								try {
									Date date = new Date();
									String dString= e_sp[0].substring(0, 10);
									date = simpleDateFormat.parse(dString);
									sads.setReport_time(date);
									//2019-7-16 cuihongyuan
									if(!TimeCheckUtil.checkTime(sads.getObservationTime())){
										ReportError reportError = new ReportError();
										reportError.setMessage("time check error!");
										reportError.setSegment(sp[1]);
										parseResult.put(reportError);
										continue;
									}
									
									parseResult.put(new ReportInfo<AgmeReportHeader>(sads, report));
									parseResult.setSuccess(true);
								} catch (Exception e) {
									ReportError re = new ReportError();
									re.setMessage("Data time parse error");
									re.setSegment(report);
									parseResult.put(re);
									continue;
								}
								ReportError re = new ReportError();
								re.setMessage("Error in the number of elements in a message entity");
								re.setSegment(report);
								parseResult.put(re);
								continue;
							}
							else{
								//时间解析
								Date date = new Date();
								try{
									String dString= e_sp[0].trim();
									date = simpleDateFormat.parse(dString);
									sads.setObservationTime(date);
									sads.setReport_time(date);
									parseResult.setSuccess(true);
									parseResult.put(new ReportInfo<AgmeReportHeader>(sads, report));
								}catch(ParseException e){
									ReportError re = new ReportError();
									re.setMessage("Data time parse error");
									re.setSegment(report);
									parseResult.put(re);
									continue;
								}
								sads.setTotalRadiationIrradiance(getValueF(e_sp[1], 1));
								sads.setNetRadiationIrradiance(getValueF(e_sp[2], 1));
								sads.setDirectRadiationIrradiance(getValueF(e_sp[3], 1));
								sads.setScatteringRadiationIrradiance(getValueF(e_sp[4], 1));
								sads.setReflectionRadiationIrradiance(getValueF(e_sp[5], 1));
								sads.setUltravioletRadiationIrradiance(getValueF(e_sp[6], 1));
								sads.setTotalRadiationExposure(getValueF(e_sp[7], 100));
								sads.setRadiationIrradianceMax(getValueF(e_sp[8], 1));
								sads.setRadiationIrradianceMaxtime(getValueF(e_sp[9], 1));
								sads.setNetRadiationExposure(getValueF(e_sp[10], 100));
								sads.setNetRadiationIrradiationMax(getValueF(e_sp[11], 1));
								sads.setNetRadiationIrradiationMaxtime(getValueF(e_sp[12], 1));
								sads.setNetRadiationIrradiationMin(getValueF(e_sp[13], 1));
								sads.setNetRadiationIrradiationMintime(getValueF(e_sp[14], 1));
								sads.setDirectRadiationExposure(getValueF(e_sp[15], 100));
								sads.setDirectRadiationIrradianceMax(getValueF(e_sp[16], 1));
								sads.setDirectRadiationIrradianceMaxtime(getValueF(e_sp[17], 1));
								sads.setScatteredRadiationExposure(getValueF(e_sp[18], 100));
								sads.setScatteringRadiationIrradianceMax(getValueF(e_sp[19],1 ));
								sads.setScatteringRadiationIrradianceMaxtime(getValueF(e_sp[20], 1));
								sads.setReflectedRadiationExposure(getValueF(e_sp[21], 100));
								sads.setReflectedRadiationIrradianceMax(getValueF(e_sp[22], 1));
								sads.setReflectedRadiationIrradianceMaxtime(getValueF(e_sp[23], 1));
								sads.setUltravioletRadiationExposure(getValueF(e_sp[24], 100));
								sads.setUvIrradiationMax(getValueF(e_sp[25], 1));
								sads.setUvIrradiationMaxtime(getValueF(e_sp[26], 1));
								sads.setSunShineHours(getValueF(e_sp[27], 10));
								sads.setAtmosphericTurbidityIndex(getValueF(e_sp[28], 100));
								parseResult.put(sads);
								parseResult.setSuccess(true);
								
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
	 * 
	 * @Title: getValueF
	 * @Description: (浮点值解析)
	 * @param str 待解析字符串
	 * @param factor 数值缩放因子
	 * @return double 解码结果
	 */
	public double getValueF(String str, int factor){
		if(str.charAt(0) == '/')
			return defaultF;
		else{
			return Double.parseDouble(str) / factor;
		}
	}
	
	
}
