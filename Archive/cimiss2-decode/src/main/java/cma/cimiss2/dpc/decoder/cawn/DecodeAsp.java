package cma.cimiss2.dpc.decoder.cawn;

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
import cma.cimiss2.dpc.decoder.bean.cawn.AerosolScatteringCharacteristics;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;
import cma.cimiss2.dpc.decoder.tools.utils.DateUtil;
import cma.cimiss2.dpc.decoder.tools.utils.FileUtil;
/**
 * 
 * <br>
 * @Title:  DecodeAsp.java
 * @Package org.cimiss2.decode.z_cawn.asp
 * @Description:(大气成分气溶胶散射特性（ASP）解码类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年4月2日 下午4:48:43   maxiyue   Initial creation.
 * </pre>
 * 
 * @author maxiyue
 */

public class DecodeAsp {
	/* 存放数据解析的结果集 */
	private ParseResult<AerosolScatteringCharacteristics> parseResult = new ParseResult<AerosolScatteringCharacteristics>(false);
	private final static String DEFAULT_MISS_VALUE = "999999";
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	/**
	 * 
	 * @Title: decodeFile
	 * @Description:(大气成分气溶胶散射特性（ASP）解码函数)
	 * @param file 文件对象
	 * @return ParseResult<AerosolScatteringCharacteristics> 解码结果集
	 * @throws：
	 */
	public ParseResult<AerosolScatteringCharacteristics> decodeFile(File file) {
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
				List<String> txtFileContent = FileUtil.getTxtFileContent(file, fileCode);
				// 首先判断文件不是空的，然后需要判断最少有一行数据
				if (txtFileContent != null && txtFileContent.size() >= 1) {
					for (int i = 0; i < txtFileContent.size(); i++) {
						AerosolScatteringCharacteristics asp = new AerosolScatteringCharacteristics();
						String[] asp_list=txtFileContent.get(i).replace(",",", ").split(",");
						for(int idx=0;idx<asp_list.length;idx++){
							   if(asp_list[idx].equals(" ")){
								   asp_list[idx]=DEFAULT_MISS_VALUE;
							   }
						   }
					    if (asp_list.length==17) {
						   try {
								asp.setStationNumberChina(asp_list[0].trim());//站号
								asp.setItemCode(getStrValue(asp_list[1].trim()));//项目代码
								// 年份
								String year = asp_list[2].trim();
								// 序日
								String julianDay = asp_list[3].trim();
								// 时分
								String hourMin = asp_list[4].trim();
								String ymd = DateUtil.convertJulianDay2Date(Integer.parseInt(year), Integer.parseInt(julianDay));
								try {
									Date observationTime = sdf.parse(ymd + hourMin + "00");
									asp.setObservationTime(observationTime);//观测时间
									asp.setReport_time(observationTime);//观测时间
									
								//  2019-7-16 cuihongyuan
									if(!TimeCheckUtil.checkTime(asp.getObservationTime())){
										ReportError re = new ReportError();
										re.setMessage("check time error!");
										re.setSegment(txtFileContent.get(i));
										parseResult.put(re);
										continue;
									}
									
								} catch (ParseException e) {
									ReportError reportError = new ReportError();
									reportError.setMessage("DateTime error!");
									reportError.setSegment(txtFileContent.get(i));
									parseResult.put(reportError);
									continue;
								}
								asp.setTimeSign(getDoubleValue(asp_list[5].trim()));//时间标志
								asp.setSearchSign(getDoubleValue(asp_list[6].trim()));//检索标志
								asp.setRecordType(getDoubleValue(asp_list[7].trim()));//记录种类
								asp.setScatteringCoefficient_Avg(getDoubleValue(asp_list[8].trim()));//散射系数平均值
								asp.setAtmosphericTemperature_Avg(getDoubleValue(asp_list[9].trim()));//大气温度平均值
								asp.setSampleChamberTemperature_Avg(getDoubleValue(asp_list[10].trim()));//样气室温度平均值
								if(Double.parseDouble(asp_list[11].trim()) > 100.0)
									asp.setRelativeHumidity_Avg(999999);
								else {
									asp.setRelativeHumidity_Avg(getDoubleValue(asp_list[11].trim()));//相对湿度平均值
								}
									
								asp.setAtmosphericPressure_Avg(getDoubleValue(asp_list[12].trim()));//大气压平均值
								asp.setDarkCountDiagnosis(getDoubleValue(asp_list[13].trim()));//暗计数诊断
								asp.setShutterCountDiagnosis(getDoubleValue(asp_list[14].trim()));//快门计数诊断
								asp.setMeasurementRatioDiagnosis(getDoubleValue(asp_list[15].trim()));//测量比率诊断
								asp.setFinalTestRatioDiagnosis(getDoubleValue(asp_list[16].trim()));//最后测试比率诊断
								parseResult.put(new ReportInfo<AgmeReportHeader>(asp,txtFileContent.get(i)));
								parseResult.put(asp);
								parseResult.setSuccess(true);
							} catch (NumberFormatException e) {
								ReportError re = new ReportError();
								re.setMessage("Digit format error!");
								re.setSegment(txtFileContent.get(i));
								parseResult.put(re);
								continue;
							}
						} 
					    else if(asp_list.length == 20) {
							asp.setStationNumberChina(asp_list[0].trim());//站号
							asp.setItemCode(asp_list[1].trim());//项目代码
							// 年份
							String year = asp_list[2].trim();
							// 序日
							String julianDay = asp_list[3].trim();
							// 时分
							String hourMin = asp_list[4].trim();
							String ymd = DateUtil.convertJulianDay2Date(Integer.parseInt(year), Integer.parseInt(julianDay));
							try {
								Date observationTime = sdf.parse(ymd + hourMin + "00");
								asp.setObservationTime(observationTime);//观测时间
								
							//  2019-7-16 cuihongyuan
								if(!TimeCheckUtil.checkTime(asp.getObservationTime())){
									ReportError re = new ReportError();
									re.setMessage("check time error!");
									re.setSegment(txtFileContent.get(i));
									parseResult.put(re);
									continue;
								}
								
							} catch (ParseException e) {
								ReportError reportError = new ReportError();
								reportError.setMessage("DateTime error!");
								reportError.setSegment( txtFileContent.get(i));
								parseResult.put(reportError);
								continue;
							}
//							asp.setMajor_State(getIntValue(asp_list[5].trim()));//Major state 
//							asp.setDioState(getStrValue(asp_list[6].trim()));//DioState
							// 修改2019-3-27
							asp.setM_STATE(getDoubleValue(asp_list[5].trim()));
							asp.setD_STATE(getDoubleValue(asp_list[6].trim()));
							asp.setScatTotal_450nm(getDoubleValue(asp_list[7].trim()));//Scat总散射系数450nm
							asp.setScatTotal_525nm(getDoubleValue(asp_list[8].trim()));//Scat总散射系数525nm
							asp.setScatTotal_635nm(getDoubleValue(asp_list[9].trim()));//Scat总散射系数636nm
							
							asp.setScatBefore_450nm(getDoubleValue(asp_list[10].trim()));//Scat前散射系数450nm
							asp.setScatBefore_525nm(getDoubleValue(asp_list[11].trim()));//Scat前散射系数525nm
							asp.setScatBefore_635nm(getDoubleValue(asp_list[12].trim()));//Scat前散射系数635nm
							
							asp.setScatAfter_450nm(getDoubleValue(asp_list[13].trim()));//Scat前散射系数450nm
							asp.setScatAfter_525nm(getDoubleValue(asp_list[14].trim()));//Scat前散射系数525nm
							asp.setScatAfter_635nm(getDoubleValue(asp_list[15].trim()));//Scat前散射系数635nm
						
							asp.setAtmosphericTemperature_Avg(getDoubleValue(asp_list[16].trim()));//ST外部温度
							asp.setSampleChamberTemperature_Avg(getDoubleValue(asp_list[17].trim()));//ET腔体温度
							if(getDoubleValue(asp_list[18].trim()) > 100.0)
								asp.setRelativeHumidity_Avg(999999);
							else {
								asp.setRelativeHumidity_Avg(getDoubleValue(asp_list[18].trim()));//相对湿度平均值
							}
								
							asp.setAtmosphericPressure_Avg(getDoubleValue(asp_list[19].trim()));//大气压平均值
							
							parseResult.put(new ReportInfo<AgmeReportHeader>(asp,txtFileContent.get(i)));
							parseResult.put(asp);
							parseResult.setSuccess(true);
					    }
					    else if (asp_list.length==5) {
							String stationNumberChina=asp_list[0].trim();//站号
							double longitude=getDoubleValue(asp_list[1].trim());//经度
							double latitude=getDoubleValue(asp_list[2].trim());//纬度
							double heightOfSationGroundAboveMeanSeaLevel=getDoubleValue(asp_list[3].trim());//海拔高度
							double dataRecordFrequency=getDoubleValue(asp_list[4].trim());//数据记录频率
							for (int idx = 1; idx < txtFileContent.size(); idx++) {
								AerosolScatteringCharacteristics asp1 = new AerosolScatteringCharacteristics();
								String[] list=txtFileContent.get(idx).replace(",",", ").split(",");
								for(int id=0;id<list.length;id++){
									   if(list[id].equals(" ")){
										  list[id]=DEFAULT_MISS_VALUE;
									   }
								   }
								if (list.length==8) {
									try {
										asp1.setStationNumberChina(stationNumberChina);//站号
										asp1.setLongitude(longitude);//经度
										asp1.setLatitude(latitude);//纬度
										asp1.setHeightOfSationGroundAboveMeanSeaLevel(heightOfSationGroundAboveMeanSeaLevel);//测站海拔高度
										asp1.setDataRecordFrequency(dataRecordFrequency);//数据记录频率
										try {
											Date date = sdf.parse(list[0].trim());
											asp1.setObservationTime(date);//观测时间
											asp1.setReport_time(date);//报告时间
										//  2019-7-16 cuihongyuan
											if(!TimeCheckUtil.checkTime(asp1.getObservationTime())){
												ReportError re = new ReportError();
												re.setMessage("check time error!");
												re.setSegment(txtFileContent.get(i));
												parseResult.put(re);
												continue;
											}
											
										} catch (ParseException e) {
											ReportError reportError = new ReportError();
											reportError.setMessage("File time error!");
											reportError.setSegment(txtFileContent.get(idx));
											parseResult.put(reportError);
											continue;
										}
										asp1.setM_STATE(getDoubleValue(list[1].trim()));
										asp1.setD_STATE(getDoubleValue(list[2].trim()));
										asp1.setScatteringCoefficient_Avg(getDoubleValue(list[3].trim()));//散射系数平均值
										asp1.setAtmosphericTemperature_Avg(getDoubleValue(list[4].trim()));//大气温度平均值
										asp1.setSampleChamberTemperature_Avg(getDoubleValue(list[5].trim()));//样气室温度平均值
										asp1.setRelativeHumidity_Avg(getDoubleValue(list[6].trim()));//相对湿度平均值
										asp1.setAtmosphericPressure_Avg(getDoubleValue(list[7].trim()));//大气压平均值
										parseResult.put(asp1);
										parseResult.put(new ReportInfo<AgmeReportHeader>(asp1,txtFileContent.get(i)+"\n"+ txtFileContent.get(idx)));
										parseResult.setSuccess(true);
									} catch (NumberFormatException e) {
										ReportError re = new ReportError();
										re.setMessage("Digit error!");
										re.setSegment(txtFileContent.get(idx));
										parseResult.put(re);
										continue;
									}
								}
								else if (list.length==13) {
									try {
										asp1.setStationNumberChina(stationNumberChina);//站号
										asp1.setLongitude(longitude);//经度
										asp1.setLatitude(latitude);//纬度
										asp1.setHeightOfSationGroundAboveMeanSeaLevel(heightOfSationGroundAboveMeanSeaLevel);//测站海拔高度
										asp1.setDataRecordFrequency(dataRecordFrequency);//数据记录频率
										try {
											Date date = sdf.parse(list[0].trim());
											asp1.setObservationTime(date);//观测时间
											asp1.setReport_time(date);//报告时间
										//  2019-7-16 cuihongyuan
											if(!TimeCheckUtil.checkTime(asp1.getObservationTime())){
												ReportError re = new ReportError();
												re.setMessage("check time error!");
												re.setSegment(txtFileContent.get(i));
												parseResult.put(re);
												continue;
											}
										} catch (ParseException e) {
											ReportError reportError = new ReportError();
											reportError.setMessage("File time error!");
											reportError.setSegment(txtFileContent.get(idx));
											parseResult.put(reportError);
											continue;
										}
										asp1.setM_STATE(getDoubleValue(list[1].trim()));
										asp1.setD_STATE(getDoubleValue(list[2].trim()));
										asp1.setScatBefore_450nm(getDoubleValue(list[3].trim()));//Scat前散射系数450nm
										asp1.setScatBefore_525nm(getDoubleValue(list[4].trim()));//Scat前散射系数525nm
										asp1.setScatBefore_635nm(getDoubleValue(list[5].trim()));//Scat前散射系数635nm
										
										asp1.setScatAfter_450nm(getDoubleValue(list[6].trim()));//Scat前散射系数450nm
										asp1.setScatAfter_525nm(getDoubleValue(list[7].trim()));//Scat前散射系数525nm
										asp1.setScatAfter_635nm(getDoubleValue(list[8].trim()));//Scat前散射系数635nm
									
										asp1.setAtmosphericTemperature_Avg(getDoubleValue(list[9].trim()));//ST外部温度
										asp1.setSampleChamberTemperature_Avg(getDoubleValue(list[10].trim()));//ET腔体温度
										if(getDoubleValue(list[11].trim()) > 100.0)
											asp1.setRelativeHumidity_Avg(999999);
										else {
											asp1.setRelativeHumidity_Avg(getDoubleValue(list[11].trim()));//相对湿度平均值
										}
											
										asp1.setAtmosphericPressure_Avg(getDoubleValue(list[12].trim()));//大气压平均值
										
										parseResult.put(asp1);
										parseResult.put(new ReportInfo<AgmeReportHeader>(asp1,txtFileContent.get(i)+"\n"+ txtFileContent.get(idx)));
										parseResult.setSuccess(true);
									} catch (NumberFormatException e) {
										ReportError re = new ReportError();
										re.setMessage("Digit error!");
										re.setSegment(txtFileContent.get(idx));
										parseResult.put(re);
										continue;
									}
								}
								else{
									ReportError reportError = new ReportError();
									reportError.setMessage("Number of segments error!");
									reportError.setSegment(txtFileContent.get(i)+"\n"+txtFileContent.get(idx));
									parseResult.put(reportError);
									continue;
								}
							}
							i=txtFileContent.size();
						}else {			
							ReportError reportError = new ReportError();
							reportError.setMessage("Element length error!");
							reportError.setSegment(txtFileContent.get(i));
							parseResult.put(reportError);
							continue;
						}
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
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
		}
		return parseResult;
	}
	
	/**
	 * @Title: getDoubleValue   
	 * @Description: 取得浮点数 
	 * @param string
	 * @return double      
	 */
	private static double getDoubleValue(String string){
		if(string.equals("-999.9"))
			return 999999;
		else{
			try{
				return Double.parseDouble(string);
			}catch (Exception e) {
				return 999999;
			}
		}
	}

	/**
	 * @Title: getStrValue   
	 * @Description: 取得字符串类型的值  
	 * @param string
	 * @return int      
	 */
	private static String getStrValue(String string){
		if(string.equals("-999.9"))
			return "999999";
		else{
			return string;
			}
	}
	
}