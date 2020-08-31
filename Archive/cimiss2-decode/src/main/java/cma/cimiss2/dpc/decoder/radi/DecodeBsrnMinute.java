package cma.cimiss2.dpc.decoder.radi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
import cma.cimiss2.dpc.decoder.bean.radi.MinutesReferenceRadiationData;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.ElementValUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;
import cma.cimiss2.dpc.decoder.tools.utils.FileUtil;
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 *
 * Main Class of decode Reference radiation minutes data <br>
 *基准辐射分钟资料解码类<br>
 *
 * <p>
 * notes:
 * 解码程序分为两个步骤:数据读取及预处理、实体对象构建及赋值。
 * <ul>
 * <li>数据读取及预处理过程：
 * <ol>
 * <li>所有运算采用java double进行基本运算。
 * <li>无特殊说明时，所有包含”/”,"-",或者字段值为代表缺测定义的值（如999999，9999999）的数据全部转化成999999。
 * <li>未通过数据校验时，该条数据将会被当成错误报文处理。
 * </ol>
 * </li>
 * <li>实体对象构建及赋值：
 * <ol>
 * <li>校验所有段的数据的数据类型是否合法。
 * <li>所有的赋值失败都会导致解码错误，该条数据将被当成错误报文处理。
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cimiss2.dpc.decoder.bean.radi.MinutesReferenceRadiationData}。
 * </ol>
 * </li>
 * </ul>
 * 
 * <strong> sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br>
 * 
 *56187201808281035140304456  5477  15  16  16  15  16  14  16  16---------------------------------------------------------------------------------------------------------------------------------------------------------------------------V1.00<br>
2018-08-28 00:01   0   0   0       0   0   0   0       0  85----   0   0   0       0  84----   0   0   0       0-------- 434 434 435       4  85 289 445 445 445       0---- 290   0   0   0       0   0   0   0       0----   0   0   0       0<br>

<strong>code:</strong><br>
 * DecodeBsrnMinute bsrnMinute = new DecodeBsrnMinute();<br>
 * ParseResult<MinutesReferenceRadiationData> parseResult = bsrnMinute.decodeFile(new File(String filepath));<br>
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
public class DecodeBsrnMinute {
	/**
	 * 解码结果集封装
	 */
	private ParseResult<MinutesReferenceRadiationData> parseResult = new ParseResult<MinutesReferenceRadiationData>(false);
	/**
	 * 缺测时替代值
	 */
	private static double defaultF = 999999; // 缺测时替代值
	/**
	 * @Description:(基准辐射分钟资料解码函数)
	 * @param file 待解码报文文件
	 * @return ParseResult<MinutesReferenceRadiationData> 解码结果集
	 */
	public ParseResult<MinutesReferenceRadiationData> decodeFile(File file) {
		if (file != null && file.exists() && file.isFile()) {
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			try {
				// get file encode
				FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();
				String fileCode = Encoding.javaname[fileEncodeUtil.detectEncoding(file)];
				fileCode = fileCode.equals("ISO8859_1") ? "GBK" : fileCode;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				List<String> txtFileContent = FileUtil.getTxtFileContent(file, fileCode);
				// 首先判断文件不是空的，然后需要判断最少有两行数据
				if (txtFileContent != null && txtFileContent.size() >= 2) {
					String head = txtFileContent.get(0);
					if (head.length() == 240) {
						String stationNumberC = head.substring(0, 5); // 区站号
						String longtitude = head.substring(13,20); // 经度
						String latitude = head.substring(20,26); // 纬度
						String elevationAltitude =head.substring(26,32); // 海拔高度
						String DRA_Sensor_Heigh=head.substring(32,36); // 太阳直接辐射表距地高度
						String SRA_Sensor_Heigh=head.substring(36,40); // 散射辐射表距地高度
						String QRA_Sensor_Heigh=head.substring(40,44); // 总辐射表距地高度
						String 	RRA_Sensor_Heigh=head.substring(44,48); // 反射辐射表距地高度
						String 	LR_Atmo_Sensor_Heigh=head.substring(48,52); // 大气长波辐射表距地高度
						String 	LR_Earth_Sensor_Heigh=head.substring(52,56); // 地球长波辐射表距地高度
						String UV_Sensor_Heigh=head.substring(56,60); // 紫外辐射表距地高度
						String PAR_Sensor_Heigh=head.substring(60,64); // 光合有效辐射表距地高度
						if(QRA_Sensor_Heigh.contains(".")){
							QRA_Sensor_Heigh="999999";
						}
						if (RRA_Sensor_Heigh.contains(".")) {
							RRA_Sensor_Heigh="999999";
						}
						if (LR_Atmo_Sensor_Heigh.contains(".")) {
							LR_Atmo_Sensor_Heigh="999999";
						}
						if(LR_Earth_Sensor_Heigh.contains(".")){
							LR_Earth_Sensor_Heigh="999999";
						}
						if(UV_Sensor_Heigh.contains(".")){
							UV_Sensor_Heigh="999999";
						}
						if (PAR_Sensor_Heigh.contains(".")) {
							PAR_Sensor_Heigh="999999";
						}
						// txtFileContent.remove(0); //去掉头信息
						String s = "";
						for(int i = 0; i < 224; i ++){
							s += "-";
						}
						
						for (int i = 1; i < txtFileContent.size(); i++) {
							MinutesReferenceRadiationData brsnMinute = new MinutesReferenceRadiationData();
							String data = txtFileContent.get(i);
							if (data.length()==240) {
								try {
									brsnMinute.setStationNumberChina(stationNumberC);//站号
									brsnMinute.setHeightOfSationGroundAboveMeanSeaLevel(getValueF(elevationAltitude, 10));
									// 纬度解析
									try {
										if(latitude.equals("999999") || latitude.contains("/") ||latitude.contains("-")){
											brsnMinute.setLatitude(defaultF);
											}
										else{
											double lat =ElementValUtil.getlatitude(latitude);
											brsnMinute.setLatitude(lat);
										}
									} catch (Exception e) {
										ReportError re = new ReportError();
										re.setMessage("(latitude) parsing error");
										re.setSegment(head);
										parseResult.put(re);
										continue;
									}
									//经度解析
									try {
										if(longtitude.equals("9999999") || longtitude.contains("/") || longtitude.contains("-"))
											brsnMinute.setLongitude(defaultF);
										else {
											double lon = ElementValUtil.getLongitude(longtitude);
											brsnMinute.setLongitude(lon);
										}
									} catch (Exception e) {
										ReportError re = new ReportError();
										re.setMessage("（Longitude）Parsing error");
										re.setSegment(head);
										parseResult.put(re);
										continue;
									}	
									brsnMinute.setDRA_Sensor_Heigh(getValueF(DRA_Sensor_Heigh, 10));
									brsnMinute.setSRA_Sensor_Heigh(getValueF(SRA_Sensor_Heigh, 10));
									brsnMinute.setQRA_Sensor_Heigh(getValueF(QRA_Sensor_Heigh, 10));
									brsnMinute.setRRA_Sensor_Heigh(getValueF(RRA_Sensor_Heigh, 10));
									brsnMinute.setLR_Atmo_Sensor_Heigh(getValueF(LR_Atmo_Sensor_Heigh, 10));
									brsnMinute.setLR_Earth_Sensor_Heigh(getValueF(LR_Earth_Sensor_Heigh, 10));
									brsnMinute.setUV_Sensor_Heigh(getValueF(UV_Sensor_Heigh, 10));
									brsnMinute.setPAR_Sensor_Heigh(getValueF(PAR_Sensor_Heigh, 10));
									
//									if(data.substring(16).equals(s)){
//										ReportError reportError = new ReportError();
//										reportError.setMessage("All elements are invalid except datetime!");
//										reportError.setSegment(txtFileContent.get(i));
//										parseResult.put(reportError);
//										continue;
//									}
									
									//时间解析
									try {
										Date date = sdf.parse(data.substring(0, 16));
										brsnMinute.setObservationTime(date);
										brsnMinute.setReport_time(date);
										
										//2019-7-16 cuihongyuan
										if(!TimeCheckUtil.checkTime(brsnMinute.getObservationTime())){
											ReportError reportError = new ReportError();
											reportError.setMessage("time check error!");
											reportError.setSegment(data);
											parseResult.put(reportError);
											continue;
										}
										
										
										if("24".equals(data.substring(11, 13))){
											brsnMinute.setDATE_Flag(24);
										}
										
									} catch (ParseException e) {
										ReportError reportError = new ReportError();
										reportError.setMessage("Time conversion error");
										reportError.setSegment(txtFileContent.get(i));
										parseResult.put(reportError);
										continue;
									}
									brsnMinute.setDRA_Avg_Mi(getValueF(data.substring(16, 20), 1));//太阳直接辐射辐照度1分钟平均值
									brsnMinute.setDRA_Min_Mi(getValueF(data.substring(20, 24), 1));//太阳直接辐射辐照度1分钟最小值
									brsnMinute.setDRA_Max_Mi(getValueF(data.substring(24,28), 1));//太阳直接辐射辐照度1分钟最大值
									brsnMinute.setDRA_Mi_SD(getValueF(data.substring(28, 36), 100));//太阳直接辐射辐照度1分钟标准差
									brsnMinute.setSRA_Avg_Mi(getValueF(data.substring(36, 40), 1));//散射辐射辐照度1分钟平均值
									brsnMinute.setSRA_Min_Mi(getValueF(data.substring(40,44), 1));//散射辐射辐照度1分钟最小值
									brsnMinute.setSRA_Max_Mi(getValueF(data.substring(44,48), 1));//散射辐射辐照度1分钟最大值
									brsnMinute.setSRA_Mi_SD(getValueF(data.substring(48,56), 100));//散射辐射辐照度1分钟标准差
									brsnMinute.setWIND_S_Avg_SRA_Mi(getValueF(data.substring(56,60), 10));//散射辐射辐射表1分钟平均通风速度
									brsnMinute.setTEM_Avg_SRA_Mi(getValueF(data.substring(60,64), 10));//散射辐射辐射表1分钟平均辐射表体温度
									brsnMinute.setQRA_Avg_Mi(getValueF(data.substring(64,68), 1));//总辐射辐照度1分钟平均值
									brsnMinute.setQRA_Min_Mi(getValueF(data.substring(68,72), 1));//总辐射辐照度1分钟最小值
									brsnMinute.setQRA_Max_Min(getValueF(data.substring(72,76), 1));//总辐射辐照度1分钟最大值
									brsnMinute.setQRA_Mi_SD(getValueF(data.substring(76,84), 100));//总辐射辐照度1分钟标准差
									brsnMinute.setWIND_S_Avg_QRA_Mi(getValueF(data.substring(84, 88), 10));//总辐射辐射表1分钟平均通风速度
									brsnMinute.setTEM_Avg_QRA_Mi(getValueF(data.substring(88, 92), 10));//总辐射辐射表1分钟平均辐射表体温度
									brsnMinute.setRRA_Avg_Mi(getValueF(data.substring(92,96), 1));//反射辐射辐照度1分钟平均值
									brsnMinute.setRRA_Min_Mi(getValueF(data.substring(96, 100), 1));//反射辐射辐照度1分钟最小值
									brsnMinute.setRRA_Max_Mi(getValueF(data.substring(100, 104), 1));//反射辐射辐照度1分钟最大值
									brsnMinute.setRRA_Mi_SD(getValueF(data.substring(104,112), 100));//反射辐射辐照度1分钟标准差
									brsnMinute.setWIND_S_Avg_RRA_Mi(getValueF(data.substring(112, 116), 10));//反射辐射辐射表1分钟平均通风速度
									brsnMinute.setTEM_Avg_RRA_Mi(getValueF(data.substring(116, 120), 10));//反射辐射辐射表1分钟平均辐射表体温度
									brsnMinute.setALR_Avg_Mi(getValueF(data.substring(120, 124), 1));//大气长波辐射辐照度1分钟平均值
									brsnMinute.setALR_Min_Mi(getValueF(data.substring(124, 128), 1));//大气长波辐射辐照度1分钟最小值
									brsnMinute.setALR_Max_Mi(getValueF(data.substring(128,132), 1));//大气长波辐射辐照度1分钟最大值
									brsnMinute.setALR_Mi_SD(getValueF(data.substring(132, 140), 100));//大气长波辐射辐照度1分钟标准差
									brsnMinute.setWIND_S_Avg_ALR_Mi(getValueF(data.substring(140, 144), 10));//大气长波辐射辐射表1分钟平均通风速度
									brsnMinute.setTEM_Avg_ALR_Mi(getValueF(data.substring(144, 148), 10));//大气长波辐射辐射表1分钟平均腔体温度
									brsnMinute.setELR_Avg_Mi(getValueF(data.substring(148, 152), 1));//地球长波辐射辐照度1分钟平均值
									brsnMinute.setELR_Min_Mi(getValueF(data.substring(152, 156), 1));//地球长波辐射辐照度1分钟最小值
									brsnMinute.setELR_Max_Mi(getValueF(data.substring(156,160), 1));//地球长波辐射辐照度1分钟最大值
									brsnMinute.setELR_Mi_SD(getValueF(data.substring(160, 168), 100));//地球长波辐射辐照度1分钟标准差
									brsnMinute.setWIND_S_Avg_ELR_Mi(getValueF(data.substring(168, 172), 10));//地球长波辐射辐射表1分钟平均通风速度
									brsnMinute.setTEM_Avg_ELR_Mi(getValueF(data.substring(172, 176), 10));//地球长波辐射辐射表1分钟平均腔体温度
									brsnMinute.setUVA_Avg_Mi(getValueF(data.substring(176, 180), 100));//紫外辐射（UVA）辐照度1分钟平均值
									brsnMinute.setUVA_Min_Mi(getValueF(data.substring(180, 184), 100));//紫外辐射（UVA）辐照度1分钟最小值
									brsnMinute.setUV_Max_Mi(getValueF(data.substring(184,188), 100));//紫外辐射（UVA）辐照度1分钟最大值
									brsnMinute.setUVA_Mi_SD(getValueF(data.substring(188, 196), 100));//紫外辐射（UVA）辐照度1分钟标准差
									brsnMinute.setUVB_Avg_Mi(getValueF(data.substring(196, 200), 100));//紫外辐射（UVB）辐照度1分钟平均值
									brsnMinute.setUVB_Min_Mi(getValueF(data.substring(200, 204), 100));//紫外辐射（UVB）辐照度1分钟最小值
									brsnMinute.setUVB_Max_Mi(getValueF(data.substring(204,208), 100));//紫外辐射（UVB）辐照度1分钟最大值
									brsnMinute.setUVB_Mi_SD(getValueF(data.substring(208, 216), 100));//紫外辐射（UVB）辐照度1分钟标准差
									brsnMinute.setTEM_Avg_UV_Mi(getValueF(data.substring(216, 220), 10));//紫外辐射辐射表恒温器1分钟平均温度
									brsnMinute.setPAR_Avg_Mi(getValueF(data.substring(220, 224), 1));//光合有效辐射辐照度1分钟平均值
									brsnMinute.setPAR_Min_Mi(getValueF(data.substring(224,228), 1));//光合有效辐射辐照度1分钟最小值
									brsnMinute.setPAR_Max_Mi(getValueF(data.substring(228, 232), 1));//光合有效辐射辐照度1分钟最大值
									brsnMinute.setPAR_Mi_SD(getValueF(data.substring(232,240), 100));//光合有效辐射辐照度1分钟标准差
									parseResult.put(brsnMinute);
									parseResult.put(new ReportInfo<AgmeReportHeader>(brsnMinute, head + "\n" + data));
									parseResult.setSuccess(true);
								} catch (NumberFormatException e) {
									ReportError reportError = new ReportError();
									reportError.setMessage("Digital conversion error");
									reportError.setSegment(head);
									parseResult.put(reportError);
								}			
							}else{
								ReportError reportError = new ReportError();
								reportError.setMessage("Message element length error");
								reportError.setSegment(head);
								parseResult.put(reportError);
							}
						}

					} else {
						ReportError reportError = new ReportError();
						reportError.setMessage("report header length error");
						reportError.setSegment(head);
						parseResult.put(reportError);
					}

				} else {
					if (txtFileContent == null || txtFileContent.size() == 0) {
						// 空文件
						parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
					} else {
						// 数据只有一行，格式不正确
						parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
					}
				}
			} catch (UnsupportedEncodingException e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			} catch (FileNotFoundException e) {
				parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
			}
		} else {
			// file not exsit
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
	 * @return double 解析结果
	 */
	public double getValueF(String str, int factor){
		String str_temp=str.replaceAll("\\s*", "");
		int len = str_temp.length();
		int cnt = 0;
		for (int i = 0; i < len; i++) {
			if (str_temp.charAt(i) == '-') {
				cnt++;
			}else if (str_temp.charAt(i) == '/') {
				cnt++;
			}	
		}
		if (cnt == len){
			return defaultF;
			}
		else if (str_temp.startsWith("9999")) {
			return defaultF;
		}else{
			return Double.parseDouble(str_temp) / factor;
		}
		
	}
}
