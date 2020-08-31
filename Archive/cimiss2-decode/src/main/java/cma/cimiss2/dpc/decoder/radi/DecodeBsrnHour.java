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
import cma.cimiss2.dpc.decoder.bean.radi.PositiveReferenceRadiationData;
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
 * Main Class of decode the On time reference radiation data <br>
 *正点基准辐射资料解码类<br>
 *
 * <p>
 * notes:
 * 解码程序分为两个步骤:数据读取及预处理、实体对象构建及赋值。
 * <ul>
 * <li>数据读取及预处理过程：
 * <ol>
 * <li>所有运算采用java double进行基本运算。
 * <li>无特殊说明时，所有包含”/”,"-",或者字段值为代表缺测定义的值（如9999，999999，9999999）的数据全部转化成999999。
 * <li>未通过数据校验时，该条数据将会被当成错误报文处理。
 * </ol>
 * </li>
 * <li>实体对象构建及赋值：
 * <ol>
 * <li>校验所有段的数据的数据类型是否合法。
 * <li>所有的赋值失败都会导致解码错误，该条数据将被当成错误报文处理。
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cimiss2.dpc.decoder.bean.radi.PositiveReferenceRadiationData}。
 * </ol>
 * </li>
 * </ul>
 * 
 * <strong> sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br>
 * 
 *570892018071135533340424   672////////////////////////////////11111111-------------------------------------------------------------------------------------------------V1.00<br>
2018-07-01 01:00   00000   00001   00000   00001   00000   00001   00000   00001 3840138 3770021 3990001 4270154 4230029 4320001   00000   00001   00000   00001   0   00001<br>

<strong>code:</strong><br>
 * DecodeBsrnHour bsrnhour = new DecodeBsrnHour();<br>
 * ParseResult<PositiveReferenceRadiationData> parseResult = bsrnhour.decodeFile(new File(String filepath));<br>
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
public class DecodeBsrnHour {
	/**
	 * 解码结果集封装
	 */
	private ParseResult<PositiveReferenceRadiationData> parseResult = new ParseResult<PositiveReferenceRadiationData>(false);
	/**
	 * 缺测时替代值
	 */
	private static double defaultF = 999999; // 缺测时替代值
	/**
	 * @Description:(正点基准辐射资料解码函数)
	 * @param file 待解码报文文件
	 * @return ParseResult<PositiveReferenceRadiationData> 解码结果集
	 */
	public ParseResult<PositiveReferenceRadiationData> decodeFile(File file) {
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
					if (head.length() == 172) {
						String stationNumberC = head.substring(0, 5); // 区站号
						String longtitude = head.substring(11,18); // 经度
						String latitude = head.substring(18,24); // 纬度
						String elevationAltitude =head.substring(24,30); // 海拔高度
						String DRA_Sensor_Heigh=head.substring(30,34); // 太阳直接辐射表距地高度
						String SRA_Sensor_Heigh=head.substring(34,38); // 散射辐射表距地高度
						
						String QRA_Sensor_Heigh=head.substring(38,42); // 总辐射表距地高度
						String RRA_Sensor_Heigh=head.substring(42,46); // 反射辐射表距地高度
						String LR_Atmo_Sensor_Heigh=head.substring(46,50); // 大气长波辐射表距地高度
						String LR_Earth_Sensor_Heigh=head.substring(50,54); // 地球长波辐射表距地高度
						String UV_Sensor_Heigh=head.substring(54,58); // 紫外辐射表距地高度
						String PAR_Sensor_Heigh=head.substring(58,62); // 光合有效辐射表距地高度
						
						if(QRA_Sensor_Heigh.endsWith("."))
							QRA_Sensor_Heigh = RRA_Sensor_Heigh = LR_Atmo_Sensor_Heigh = LR_Earth_Sensor_Heigh = UV_Sensor_Heigh = PAR_Sensor_Heigh = "999999";
						else if(RRA_Sensor_Heigh.endsWith("."))
							RRA_Sensor_Heigh = LR_Atmo_Sensor_Heigh = LR_Earth_Sensor_Heigh = UV_Sensor_Heigh = PAR_Sensor_Heigh = "999999";
						else if(LR_Atmo_Sensor_Heigh.endsWith("."))
							LR_Atmo_Sensor_Heigh = LR_Earth_Sensor_Heigh = UV_Sensor_Heigh = PAR_Sensor_Heigh = "999999";
						else if(LR_Earth_Sensor_Heigh.endsWith("."))
							LR_Earth_Sensor_Heigh = UV_Sensor_Heigh = PAR_Sensor_Heigh = "999999";
						else if(UV_Sensor_Heigh.endsWith("."))
							UV_Sensor_Heigh = PAR_Sensor_Heigh = "999999";
						else if(PAR_Sensor_Heigh.endsWith("."))
							PAR_Sensor_Heigh = "999999";
						// txtFileContent.remove(0); //去掉头信息
						String s = "";
						for(int i = 0; i < 156; i ++){
							s += "-";
						}
						for (int i = 1; i < txtFileContent.size(); i++) {
							PositiveReferenceRadiationData brsnHour = new PositiveReferenceRadiationData();
							String data = txtFileContent.get(i);
							if (data.length()==172) {
								try {
									brsnHour.setStationNumberChina(stationNumberC);//站号
									brsnHour.setHeightOfSationGroundAboveMeanSeaLevel(getValueF(elevationAltitude, 10));
									// 纬度解析
									try {
										if(latitude.equals("999999") || latitude.contains("/")){
											brsnHour.setLatitude(defaultF);
											}
										else{
											double lat =ElementValUtil.getlatitude(latitude);
											brsnHour.setLatitude(lat);
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
										if(longtitude.equals("9999999") || longtitude.contains("/"))
											brsnHour.setLongitude(defaultF);
										else {
											double lon = ElementValUtil.getLongitude(longtitude);
											brsnHour.setLongitude(lon);
										}
									} catch (Exception e) {
										ReportError re = new ReportError();
										re.setMessage("(Longitude)Parsing error");
										re.setSegment(head);
										parseResult.put(re);
										continue;
									}
									brsnHour.setDRA_Sensor_Heigh(getValueF(DRA_Sensor_Heigh, 10));
									brsnHour.setSRA_Sensor_Heigh(getValueF(SRA_Sensor_Heigh, 10));
									
									brsnHour.setQRA_Sensor_Heigh(getValueF(QRA_Sensor_Heigh, 10));
									brsnHour.setRRA_Sensor_Heigh(getValueF(RRA_Sensor_Heigh, 10));
									brsnHour.setLR_Atmo_Sensor_Heigh(getValueF(LR_Atmo_Sensor_Heigh, 10));
									brsnHour.setLR_Earth_Sensor_Heigh(getValueF(LR_Earth_Sensor_Heigh, 10));
									brsnHour.setUV_Sensor_Heigh(getValueF(UV_Sensor_Heigh, 10));
									brsnHour.setPAR_Sensor_Heigh(getValueF(PAR_Sensor_Heigh, 10));
									
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
										brsnHour.setObservationTime(date);
										brsnHour.setReport_time(date);
										//2019-7-16 cuihongyuan
										if(!TimeCheckUtil.checkTime(brsnHour.getObservationTime())){
											ReportError reportError = new ReportError();
											reportError.setMessage("time check error!");
											reportError.setSegment(data);
											parseResult.put(reportError);
											continue;
										}
										
									} catch (ParseException e) {
										ReportError reportError = new ReportError();
										reportError.setMessage("Time conversion error");
										reportError.setSegment(txtFileContent.get(i));
										parseResult.put(reportError);
										continue;
									}
									
									brsnHour.setDRA_Avg_Hour(ToBeValidDouble(data.substring(16, 20), 1));//太阳直接辐射辐照度小时平均值
									brsnHour.setDirectRadiationExposure(ToBeValidDouble(data.substring(20, 24), 0.01));//太阳直接辐射小时曝辐量
									brsnHour.setDRA_Max_Hour(ToBeValidDouble(data.substring(24, 28), 1));//太阳直接辐射辐照度小时内最大值
									brsnHour.setDRA_Max_OTime(ToBeValidDouble(data.substring(28, 32), 1));//太阳直接辐射辐照度小时内最大值出现时间
									brsnHour.setSRA_Avg_Hour(ToBeValidDouble(data.substring(32, 36), 1));//散射辐射辐照度小时平均值
									brsnHour.setScatteredRadiationExposure(ToBeValidDouble(data.substring(36, 40), 0.01));//散射辐射小时曝辐量
									brsnHour.setSRA_Max_Hour(ToBeValidDouble(data.substring(40, 44), 1));//散射辐射辐照度小时内最大值
									brsnHour.setSRA_Max_OTime(ToBeValidDouble(data.substring(44, 48), 1));//散射辐射辐照度小时内最大值出现时间
									brsnHour.setQRA_Avg_Hour(ToBeValidDouble(data.substring(48, 52), 1));//总辐射辐照度小时平均值
									brsnHour.setTotalRadiationExposure(ToBeValidDouble(data.substring(52, 56), 0.01));//总辐射小时曝辐量
									brsnHour.setQRA_Max_Hour(ToBeValidDouble(data.substring(56, 60), 1));//总辐射辐照度小时内最大值
									brsnHour.setQRA_Max_Hour_OTime(ToBeValidDouble(data.substring(60, 64), 1));//总辐射辐照度小时内最大值出现时间
									brsnHour.setRRA_Avg_Hour(ToBeValidDouble(data.substring(64, 68), 1));//反射辐射辐照度小时平均值
									brsnHour.setRRA_Hour(ToBeValidDouble(data.substring(68, 72), 0.01));//反射辐射小时曝辐量
									brsnHour.setRRA_Max_Hour(ToBeValidDouble(data.substring(72, 76), 1));//反射辐射辐照度小时内最大值
									brsnHour.setRRA_Max_OTime(ToBeValidDouble(data.substring(76, 80), 1));//反射辐射辐照度小时内最大值出现时间
									brsnHour.setALR_Avg_Hour(ToBeValidDouble(data.substring(80, 84), 1));//大气长波辐射辐照度小时平均值
									brsnHour.setALR_Hour(ToBeValidDouble(data.substring(84, 88), 0.01));//大气长波辐射小时曝辐量
									brsnHour.setALR_Min_Hour(ToBeValidDouble(data.substring(88, 92), 1));//大气长波辐射辐照度小时内最小值
									brsnHour.setALR_Min_Mi_OTIime(ToBeValidDouble(data.substring(92, 96), 1));//大气长波辐射辐照度小时内最小值出现时间
									brsnHour.setALR_Max_Hour(ToBeValidDouble(data.substring(96, 100), 1));//大气长波辐射辐照度小时内最大值
									brsnHour.setALR_Max_Mi_OTime(ToBeValidDouble(data.substring(100, 104), 1));//大气长波辐射辐照度小时内最大值出现时间
									brsnHour.setELR_Avg_Hour(ToBeValidDouble(data.substring(104, 108), 1));//地球长波辐射辐照度小时平均值
									brsnHour.setELR_Hour(ToBeValidDouble(data.substring(108, 112), 0.01));//地球长波辐射小时曝辐量
									brsnHour.setELR_Min_Hour(ToBeValidDouble(data.substring(112, 116), 1));//地球长波辐射辐照度小时内最小值
									brsnHour.setELR_Min_Mi_Otime(ToBeValidDouble(data.substring(116, 120), 1));//地球长波辐射辐照度小时内最小值出现时间
									brsnHour.setELR_Max_Hour(ToBeValidDouble(data.substring(120, 124), 1));//地球长波辐射辐照度小时内最大值
									brsnHour.setELR_Max_Mi_OTime(ToBeValidDouble(data.substring(124, 128), 1));//地球长波辐射辐照度小时内最大值出现时间
									brsnHour.setUVA_Avg_Hour(ToBeValidDouble(data.substring(128, 132), 0.01));//紫外辐射（UVA）辐照度小时平均值
									brsnHour.setUltravioletRadiationExposure(ToBeValidDouble(data.substring(132, 136), 0.001));//紫外辐射（UVA）小时曝辐量
									brsnHour.setUV_Max_Mi(ToBeValidDouble(data.substring(136, 140), 0.01));//紫外辐射（UVA）辐照度小时内最大值
									brsnHour.setUV_Max_OTime(ToBeValidDouble(data.substring(140, 144), 1));//紫外辐射（UVA）辐照度小时内最大值出现时间
									brsnHour.setUVB_Avg_Hour(ToBeValidDouble(data.substring(144, 148), 0.01));//紫外辐射（UVB）辐照度小时平均值
									brsnHour.setUV_Hour(ToBeValidDouble(data.substring(148, 152), 0.001));//紫外辐射（UVB）小时曝辐量
									brsnHour.setUVB_Max_Hour(ToBeValidDouble(data.substring(152, 156), 0.01));//紫外辐射（UVB）辐照度小时内最大值
									brsnHour.setUVB_Max_Hour_OTime(ToBeValidDouble(data.substring(156, 160), 1));//紫外辐射（UVB）辐照度小时内最大值出现时间
									brsnHour.setPAR_Avg_Hour(ToBeValidDouble(data.substring(160, 164), 1));//光合有效辐射小时平均值
									brsnHour.setPAR_Max_Hour(ToBeValidDouble(data.substring(164, 168), 1));//光合有效辐射小时内最大值
									brsnHour.setPAR_Max_Mi_OTime(ToBeValidDouble(data.substring(168, 172), 1));//光合有效辐射小时内最大值出现时间	
									parseResult.put(brsnHour);
									parseResult.put(new ReportInfo<AgmeReportHeader>(brsnHour, head + "\n" + data));
									parseResult.setSuccess(true);
								} catch (NumberFormatException e) {
									ReportError reportError = new ReportError();
									reportError.setMessage("Digital conversion error");
									reportError.setSegment(head);
									parseResult.put(reportError);
								}			
							}else{
								ReportError reportError = new ReportError();
								reportError.setMessage("report element length error");
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
		if(str_temp.charAt(0) == '/' || str_temp.charAt(0)=='-')
			return defaultF;
		else if (str_temp.equals("9999") ||str_temp.equals("999999")  ) {
			return defaultF;	
		}else {
			return Double.parseDouble(str_temp) / factor;
		}
	}
	
	/**
	 * 
	 * @Title: ToBeValid   
	 * @Description: TODO(浮点数值数据 的处理 )   
	 * @param: @param str
	 * @param: @param factor
	 * @param: @return      
	 * @return: double      
	 * @throws
	 */
	public static double ToBeValidDouble(String str, double factor) {
		int len = str.length();
		int cnt = 0;
		for (int i = 0; i < len; i++) {
			if (str.charAt(i) == '9'||str.charAt(i) == '-')
				cnt++;
		}
		if (cnt == len)
			return 999999;
		else
			try{
				return Double.parseDouble(str) * factor;
			}catch (Exception e) {
				return 999999;
			}
	}
		
	public static void main(String[] args) {
//		DecodeBsrnHour dec = new DecodeBsrnHour();
//		System.out.println(dec.getValueF("4.", 10));
		DecodeBsrnHour decodeBsrnHour = new DecodeBsrnHour();
		ParseResult<PositiveReferenceRadiationData> parseResult = decodeBsrnHour.decodeFile(new File("D:\\TEMP\\D.11.2.1\\3-20\\Z_RADI_I_54102_20200331160001_O_BSRN-MUL-HOR.TXT"));
		List<PositiveReferenceRadiationData> hour = parseResult.getData();
		System.out.println(hour.get(0).getBul_center());
	}
	
}
