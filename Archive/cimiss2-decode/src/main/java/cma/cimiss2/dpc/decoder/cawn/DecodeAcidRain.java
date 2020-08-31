package cma.cimiss2.dpc.decoder.cawn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.regex.Pattern;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
import cma.cimiss2.dpc.decoder.bean.cawn.AcidRainDailyData;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.ElementValUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;
/**
 * 
 * -------------------------------------------------------------------------------
 * <br>
 * @Title:  DecodeAcidRain.java   
 * @Package org.cimiss2.decode.z_cawn_ar   
 * @Description:    TODO(大气成分酸雨日文件资料解码类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年2月9日 下午3:49:11   maxiyue    Initial creation.
 * </pre>
 * 
 * @author maxiyue
 *---------------------------------------------------------------------------------
 */
public class DecodeAcidRain {
	/* 存放数据解析的结果集 */
	private ParseResult<AcidRainDailyData> parseResult = new ParseResult<AcidRainDailyData>(false);
	/**
	 * 
	 * @Title: decodeFile
	 * @Description: TODO(大气成分酸雨日文件资料解码函数)
	 * @param file
	 * @return ParseResult<AcidRainDailyData> 解码结果集
	 * @throws：
	 */
	@SuppressWarnings("resource")
	public ParseResult<AcidRainDailyData> decodeFile(File file) {
		if (file != null && file.exists() && file.isFile()) {
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			InputStreamReader read = null;
			Scanner scanner = null;
			try {
				// 获取文件的编码
				FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();
				String fileCode = Encoding.javaname[fileEncodeUtil.detectEncoding(file)];
				read = new InputStreamReader(new FileInputStream(file), fileCode);
				scanner = new Scanner(read).useDelimiter("=");
				while (scanner.hasNext()) {
					AcidRainDailyData acidRain = new AcidRainDailyData();
					AgmeReportHeader agmeHeader = new AgmeReportHeader();
					// 获取一个完整的报文段
					String report = scanner.next();
					char s = report.trim().charAt(0);
					if (s == 65279) { // 65279是空字符
						if (report.length() > 1) {
							report = report.substring(1);
						}
					}
					if (report.trim().equalsIgnoreCase("NNNN")) {
						// 报文结束
						break;
					}
//					String report_temp =report.replace("/", "9");
					String report_temp =report;
					// 数据第一行为报文头信息
					
					//转化成数组，判断元素个数是否为33
					String[] split = report_temp.trim().split("\n");
					if(split.length != 2) {
						ReportError re = new ReportError();
						re.setMessage("report error!");
						re.setSegment(report_temp);
						parseResult.put(re);
						continue;
					}
					String[] s1 = split[0].split("\\s+");
					String[] s2 = split[1].split("\\s+");
					for(int k=0;k <s1.length;k++){
						if(s1[k].contains("/"))
							s1[k]="999999";
					}
					for(int j=0;j<s2.length;j++){
						if(s2[j].contains("/"))
							s2[j]="999999";
					}
					if(s1.length == 5 && s2.length>=1){
						try {
							acidRain.setStationNumberChina(s1[0]);//站号
                            String lat_str = s1[1].substring(0, s1[1].length()-1);
							double lat = to_latlon(lat_str);
							if(!Double.isNaN(lat)){
								acidRain.setLatitude(lat);//纬度
							}else {
								acidRain.setLatitude(999999.0);
//								ReportError re = new ReportError();
//								re.setMessage("Latitude error!");
//								re.setSegment(report_temp);
//								parseResult.put(re);
//								continue;
							}
							String lon_str = s1[2].substring(0, s1[2].length()-1);
							double lon= to_latlon(lon_str);
							if(!Double.isNaN(lon)){
								acidRain.setLongitude(lon);//经度
							}else {
								acidRain.setLongitude(999999.0);
//								ReportError re = new ReportError();
//								re.setMessage("Longtitude error!");
//								re.setSegment(report_temp);
//								parseResult.put(re);
//								continue;
							}
							//报文头
							agmeHeader.setStationNumberChina(s1[0]);//站号
							agmeHeader.setLatitude(lat);//纬度
							agmeHeader.setLongitude(lon);//经度
							agmeHeader.setHeightOfSationGroundAboveMeanSeaLevel(ElementValUtil.ToBeValidDouble(s1[3], 0.1));//海拔高度
							agmeHeader.setReport_time(sdf.parse(s2[0]));//观测时间		
							parseResult.put(new ReportInfo<AgmeReportHeader>(agmeHeader,report));
//							parseResult.setSuccess(true);
							acidRain.setHeightOfSationGroundAboveMeanSeaLevel(ElementValUtil.ToBeValidDouble(s1[3], 0.1));//海拔高度
							acidRain.setObservationMethod(ElementValUtil.ToBeValidDouble(s1[4]));//观测方式
							acidRain.setObservationTime(sdf.parse(s2[0]));//观测时间
							
						//  2019-7-16 cuihongyuan
							if(!TimeCheckUtil.checkTime(acidRain.getObservationTime())){
								ReportError re = new ReportError();
								re.setMessage("check time error!");
								re.setSegment(report_temp);
								parseResult.put(re);
								continue;
							}
							
							acidRain.setPrecipitation_StartTime(s2.length>1?ElementValUtil.ToBeValidDouble(s2[1]):999998);//降水开始时间
							acidRain.setPrecipitation_EndTime(s2.length>2?ElementValUtil.ToBeValidDouble(s2[2]):999998);//降水结束时间
							acidRain.setAcidRainObservedPrecipitation(s2.length>3?ElementValUtil.ToBeValidDouble(s2[3],0.1):999998);//酸雨观测降水量
							acidRain.setInitialSurveySampleTemperature(s2.length>4?ElementValUtil.ToBeValidDouble(s2[4],0.1):999998);//初测时样品温度
							acidRain.setInitialSurvey_PH_1(s2.length>5?ElementValUtil.ToBeValidDouble(s2[5],0.01):999998);//初测pH值第1次读数
							acidRain.setInitialSurvey_PH_2(s2.length>6?ElementValUtil.ToBeValidDouble(s2[6],0.01):999998);//初测pH值第2次读数
							acidRain.setInitialSurvey_PH_3(s2.length>7?ElementValUtil.ToBeValidDouble(s2[7],0.01):999998);//初测pH值第3次读数
							acidRain.setInitialSurvey_Average_PH(s2.length>8?ElementValUtil.ToBeValidDouble(s2[8],0.01):999998);//初测平均pH值
							acidRain.setInitialSurvey_K_1(s2.length>9?ElementValUtil.ToBeValidDouble(s2[9],0.1):999998);//初测K值第1次读数
							acidRain.setInitialSurvey_K_2(s2.length>10?ElementValUtil.ToBeValidDouble(s2[10],0.1):999998);//初测K值第2次读数
							acidRain.setInitialSurvey_K_3(s2.length>11?ElementValUtil.ToBeValidDouble(s2[11],0.1):999998);//初测K值第3次读数
							acidRain.setInitialSurvey25_Average_K(s2.length>12?ElementValUtil.ToBeValidDouble(s2[12],0.1):999998);//初测25℃时平均K值
							acidRain.setRetestSurveySampleTemperature(s2.length>13?ElementValUtil.ToBeValidDouble(s2[13],0.1):999998);//复测时样品温度
							acidRain.setRetestSurvey_PH_1(s2.length>14?ElementValUtil.ToBeValidDouble(s2[14],0.01):999998);//复测pH值第1次读数
							acidRain.setRetestSurvey_PH_2(s2.length>15?ElementValUtil.ToBeValidDouble(s2[15],0.01):999998);//复测pH值第2次读数
							acidRain.setRetestSurvey_PH_3(s2.length>16?ElementValUtil.ToBeValidDouble(s2[16],0.01):999998);//复测pH值第3次读数
							acidRain.setRetestSurvey_Average_PH(s2.length>17?ElementValUtil.ToBeValidDouble(s2[17],0.01):999998);//复测平均pH值
							acidRain.setRetestSurvey_K_1(s2.length>18?ElementValUtil.ToBeValidDouble(s2[18],0.1):999998);//复测K值第1次读数
							acidRain.setRetestSurvey_K_2(s2.length>19?ElementValUtil.ToBeValidDouble(s2[19],0.1):999998);//复测K值第2次读数
							acidRain.setRetestSurvey_K_3(s2.length>20?ElementValUtil.ToBeValidDouble(s2[20],0.1):999998);//复测K值第3次读数
							acidRain.setRetestSurvey25_Average_K(s2.length>21?ElementValUtil.ToBeValidDouble(s2[21],0.1):999998);//复测25℃时平均K值
						    
						    double WindSpeed_14=s2.length>22?ToBeValidDouble(String.valueOf(Integer.parseInt(s2[22].substring(3))), 0.1):999998;
						    acidRain.setWindSpeed_14(WindSpeed_14);//14时风速
						    if(WindSpeed_14<=0.2){
						    	acidRain.setWindDirection_14((double)999017);//14时风向
						    }else{
						    	acidRain.setWindDirection_14(s2.length>22?field(s2[22].substring(0, 3)):999998);//14时风向
						    }
						    
						    double WindSpeed_20=s2.length>23?ToBeValidDouble(String.valueOf(Integer.parseInt(s2[23].substring(3))), 0.1):999998;
						    acidRain.setWindSpeed_20(WindSpeed_20);//20时风速
						    if(WindSpeed_20<=0.2){
						    	acidRain.setWindDirection_20((double)999017);//20时风向
						    }else{
						    	acidRain.setWindDirection_20(s2.length>23?field(s2[23].substring(0, 3)):999998);//20时风向
						    }
						    
						    double WindSpeed_02=s2.length>24?ToBeValidDouble(String.valueOf(Integer.parseInt(s2[24].substring(3))), 0.1):999998;
						    acidRain.setWindSpeed_02(WindSpeed_02);//02时风速
						    if(WindSpeed_02<=0.2){
						    	acidRain.setWindDirection_02((double)999017);//02时风向
						    }else{
						    	acidRain.setWindDirection_02(s2.length>24?field(s2[24].substring(0, 3)):999998);//02时风向
						    }
						    
						    double WindSpeed_08=s2.length>25?ToBeValidDouble(String.valueOf(Integer.parseInt(s2[25].substring(3))), 0.1):999998;
						    acidRain.setWindSpeed_08(WindSpeed_08);//08时风速
						    if(WindSpeed_08<=0.2){
						    	acidRain.setWindDirection_08((double)999017);//08时风向
						    }else{
						    	acidRain.setWindDirection_08(s2.length>25?field(s2[25].substring(0, 3)):999998);//08时风向
						    }
							acidRain.setPrecipitationPeriodWeatherPhenomenon_1(s2.length>26?ElementValUtil.ToBeValidDouble(s2[26].substring(0, 2)):999998);//降水期间天气现象1
							acidRain.setPrecipitationPeriodWeatherPhenomenon_2(s2.length>26?ElementValUtil.ToBeValidDouble(s2[26].substring(2, 4)):999998);//降水期间天气现象2
							acidRain.setPrecipitationPeriodWeatherPhenomenon_3(s2.length>26?ElementValUtil.ToBeValidDouble(s2[26].substring(4, 6)):999998);//降水期间天气现象3
							try {
								acidRain.setPrecipitationPeriodWeatherPhenomenon_4(s2.length>26?ElementValUtil.ToBeValidDouble(s2[26].substring(6, 8)):999998);//降水期间天气现象4			
							} catch (NumberFormatException e) {
								boolean result=s2[26].matches("[0-9]+");
								if (result == false) {
									acidRain.setPrecipitationPeriodWeatherPhenomenon_4(999999.0);//降水期间天气现象4
								}
								ReportError re = new ReportError();
								re.setMessage("Digit error!");
								re.setSegment(report_temp);
								parseResult.put(re);
							}
							acidRain.setRepeatSurveyIndicatorCode(s2.length>27?ElementValUtil.ToBeValidDouble(s2[27].substring(0, 1)):999998);//取编码值第1位
							acidRain.setTemperatureCompensation_K_IndicatorCode(s2.length>27?ElementValUtil.ToBeValidDouble(s2[27].substring(1, 2)):999998);//K值测量是否使用温度补偿功能指示码
							try {
								if(s2.length>27){
									if (!s2[27].substring(2, 5).equals("999")) {
										acidRain.setDelaySampleIndicatorCode(ToBeValidDouble(s2[27].substring(2, 3)));//因故延迟样品测量指示码
										
									} else {
										acidRain.setDelaySampleIndicatorCode(ElementValUtil.ToBeValidDouble(s2[27].substring(2, 3)));//因故延迟样品测量指示码
									}
								}else{
									acidRain.setDelaySampleIndicatorCode(999998.0);
								}
								
							} catch (NumberFormatException e) {
								if(s2.length>27){
									boolean result=s2[27].matches("[0-9]+");
									if (result == false) {
										acidRain.setDelaySampleIndicatorCode(999999.0);//因故延迟样品测量指示码
									}
								}else{
									acidRain.setDelaySampleIndicatorCode(999998.0);
								}
								ReportError re = new ReportError();
								re.setMessage("Digit error!");
								re.setSegment(report_temp);
								parseResult.put(re);
							}
							acidRain.setPrecipitationSampleException(s2.length>27?Double.parseDouble(s2[27].substring(3, 5)):999998);//降水样品异常状况
					
							parseResult.put(acidRain);
							parseResult.setSuccess(true);
							
						} catch (NumberFormatException e) {
							ReportError re = new ReportError();
							re.setMessage("Digit error!");
							re.setSegment(report_temp);
							parseResult.put(re);
							continue;
						}
						catch (ParseException e) {
							ReportError re = new ReportError();
							re.setMessage("DataTime error!");
							re.setSegment(report_temp);
							parseResult.put(re);
							continue;
						}
					}else {
						//第一行元素个数不足5个
						ReportError re = new ReportError();
   						re.setMessage("Report error!");
   						re.setSegment(report_temp);
   						parseResult.put(re);			
						//break;
   						continue;
					}
				}	
			} catch (UnsupportedEncodingException e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			} catch (FileNotFoundException e) {
				parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
			}  finally {
				try {
					if (read != null) {
						read.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				try{
					if(scanner !=null)
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
	 * 
	 * @Title: ToBeValidDouble
	 * @Description: TODO(转换函数)
	 * @param str
	 * @param factor
	 * @return double
	 * @throws：
	 */
	public static double ToBeValidDouble(String str, double factor){
		if (!str.equals("999")) {
			return Double.parseDouble(str) * factor;
		}
		else 
			return 999999;
	}
	/**
	 * 
	 * @Title: ToBeValidDouble
	 * @Description: TODO(转换函数)
	 * @param str
	 * @return double
	 * @throws：
	 */
	public static double ToBeValidDouble(String str){
		if (!str.equals("999")) {
			return Double.parseDouble(str);
		}
		else 
			return 999999;
	}
	/**
	 * 
	 * @Title: to_latlon
	 * @Description: TODO(经纬度转换函数)
	 * @param latlon_str
	 * @return double
	 * @throws：
	 */
	public static double to_latlon(String latlon_str){
		if(is_number(latlon_str)){
			float value = Float.parseFloat(latlon_str.substring(0, latlon_str.length()-2)) + 
					      Float.parseFloat(latlon_str.substring(latlon_str.length()-2, latlon_str.length()))/60 ;
			BigDecimal bg = new BigDecimal(value);
			return bg.setScale( 4,BigDecimal.ROUND_HALF_UP).doubleValue();
		}else {
			return Double.NaN;
		}
	}
	/**
	 * 
	 * @Title: is_number
	 * @Description: TODO(辅助函数，判断是否全是数字)
	 * @param num_str
	 * @return boolean
	 * @throws：
	 */
	public static boolean is_number(final String num_str){
		//^[0-9]*[1-9][0-9]*$
		String regex = "^[0-9]*[0-9][0-9]*$";
		return Pattern.matches(regex, num_str);
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
		case "PPN": 
			return (double) 0; 
		
		case "NNE": 
			return (double) 22.5; 
		
		case "PNE": 
			return (double) 45; 
			
		case "ENE": 
			return 67.5; 
		case "PPE": 
			return (double) 90; 
			
		case "ESE": 
			return 112.5;
		case "PSE": 
			return (double) 135; 
		
		case "SSE": 
			return (double) 157.5; 
		
		case "PPS": 
			return (double) 180; 
			
		case "SSW": 
			return 202.5; 
		case "PSW": 
			return (double) 225; 
			
		case "WSW": 
			return 247.5;
		case "PPW": 
			return (double) 270; 
		
		case "WNW": 
			return (double) 292.5; 
		
		case "PNW": 
			return (double) 315; 
			
		case "NNW": 
			return 337.5;
			
		case "PPC": 
			return (double)999017;
			
		default: 
			return (double) 999999;
		} 
		
	}
	public static void main(String[] args) {
		DecodeAcidRain decodeAcidRain = new DecodeAcidRain();
		decodeAcidRain.decodeFile(new File("D:\\tmp\\cawn\\Z_CAWN_I_58358_20191127000000_O_AR_FTM.txt"));
	}
	
	
}
