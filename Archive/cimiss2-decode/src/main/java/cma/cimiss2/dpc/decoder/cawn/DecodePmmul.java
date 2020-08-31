package cma.cimiss2.dpc.decoder.cawn;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
import cma.cimiss2.dpc.decoder.bean.cawn.AtmosphericAerosolPmmul;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.ElementValUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;
import cma.cimiss2.dpc.decoder.tools.utils.DateUtil;
import cma.cimiss2.dpc.decoder.tools.utils.FileUtil;

/**
 * <br>
 * @Title:  DecodePmmul.java
 * @Package org.cimiss2.decode.z_cawn.pmmul
 * @Description:   大气成分气溶胶PM10/PM2.5/PM1质量浓度（PMMUL）文件解析
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月2日 上午9:23:01   wangzunpeng    Initial creation.
 * </pre>
 * 
 * @author wangzunpeng
 */
public class DecodePmmul {

	private ParseResult<AtmosphericAerosolPmmul> parseResult = new ParseResult<AtmosphericAerosolPmmul>(false);

	private final static String BAM_FILENAME_REG = "Z_CAWN_\\w+_\\d{14}_O_AER-FLD-PMMUL-BAM.TXT";
	private final static String ESA$TEA$TDF$SHP_FILENAME_REG = "Z_CAWN_\\w+_\\d{14}_O_AER-FLD-PMMUL-((ESA)|(TEA)|(TDF)|(SHP)|(TED)).TXT";
	private final static String GIM_FILENAME_REG = "Z_(C|M)AWN_\\w+_\\d{14}_O_AER-FLD-PMMUL(-GIM)?.TXT";
	private final static double DEFAULT_MISS_VALUE = -999.9;

	/**
	 * 
	 * @Title: decode
	 * @Description: 大气成分气溶胶PM10/PM2.5/PM1质量浓度（PMMUL）解析
	 * @param file
	 * @return ParseResult<AtmosphericAerosolPmmul>
	 * @throws:
	 */
	public ParseResult<AtmosphericAerosolPmmul> decode(File file) {

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

				// Z_CAWN_I_J4810_20180223000000_O_AER-FLD-PMMUL-BAM.TXT

				String fileName = file.getName();
				// Matcher m = Pattern.compile(BAM_FILENAME_REG, Pattern.CASE_INSENSITIVE).matcher(fileName);
				// BAM格式解析，共23个字段
				if (Pattern.compile(BAM_FILENAME_REG, Pattern.CASE_INSENSITIVE).matcher(fileName).find()) {
					
					this.decodeBAM(file, fileCode);
				} else if (Pattern.compile(ESA$TEA$TDF$SHP_FILENAME_REG, Pattern.CASE_INSENSITIVE).matcher(fileName).find()) {
					// ESA格式解析，共8个字段
					this.decodeESA(file, fileCode);
				} else if (Pattern.compile(GIM_FILENAME_REG, Pattern.CASE_INSENSITIVE).matcher(fileName).find()) {
					this.decodeGIM(file, fileCode);
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
	 * @Title: decodeESA
	 * @Description: 大气成分气溶胶PM10/PM2.5/PM1质量浓度（PMMUL）ESA、TEA、TDF、SHP格式解析
	 * @param file
	 * @param fileCode
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 * void
	 * @throws: 
	 */
	private void decodeESA(File file, String fileCode) throws UnsupportedEncodingException, FileNotFoundException {
		List<String> txtFileContent = FileUtil.getTxtFileContent(file, fileCode);
		// 首先判断文件不是空的，然后需要判断最少有1行数据
		if (txtFileContent != null && txtFileContent.size() >= 1) {
			SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DATETIME_FORMAT_YMDHMS);
			for (String dataLine : txtFileContent) {
				dataLine=dataLine.replaceAll("\\/+", "999999");
				String[] elementDatas = (dataLine.replaceAll("\\s+", "") + " ").split(",");
				if (elementDatas.length == 8) {
					AtmosphericAerosolPmmul aap = new AtmosphericAerosolPmmul();
					// 观测站区站号
					String stationNumberChina = elementDatas[0];
					aap.setStationNumberChina(stationNumberChina);
					// 项目代码
					// int itemCode = Integer.parseInt(elementDatas[1]);
//					double projectCode = Double.parseDouble(elementDatas[1]);
					String projectCode= elementDatas[1];
					if("".equals(projectCode)||projectCode==null){
						projectCode="999999";
					}
					aap.setProjectCode(projectCode);
					// 年份
					String year = elementDatas[2];
					// 序日
					String julianDay = elementDatas[3];
					// 时分
					String hourMin = elementDatas[4];
					
					hourMin = String.format("%04d", Integer.parseInt(hourMin));

					String ymd = DateUtil.convertJulianDay2Date(Integer.parseInt(year), Integer.parseInt(julianDay));

					// aap.setDataObservationalYear(Double.parseDouble(year));
					// aap.setDataObservationMonth(Double.parseDouble(ymd.substring(4, 6)));
					// aap.setDataObservationDay(Double.parseDouble(ymd.substring(6, 8)));
					// aap.setDataObservationHour(Double.parseDouble(hourMin.substring(0, 2)));
					// aap.setDataObservationMin(Double.parseDouble(hourMin.substring(2, 4)));

					try {
						Date d_datetime = sdf.parse(ymd + hourMin + "00");
						aap.setD_datetime(d_datetime);
						aap.setReport_time(d_datetime);
						//2019-7-16 cuihongyuan
						if(!TimeCheckUtil.checkTime(d_datetime)){
							ReportError reportError = new ReportError();
							reportError.setMessage("time check error!");
							reportError.setSegment(dataLine);
							parseResult.put(reportError);
							continue;
						}
						
					} catch (ParseException e) {
						ReportError reportError = new ReportError();
						reportError.setMessage("DataTime error!");
						reportError.setSegment(txtFileContent.toString());
						parseResult.put(reportError);
						continue;
					}

					// PM10质量浓度
					String massConcentrationOfPM10 = elementDatas[5];
					aap.setMassConcentrationOfPM10(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM10, DEFAULT_MISS_VALUE));
					// PM2.5质量浓度
					String massConcentrationOfPM2p5 = elementDatas[6];
					aap.setMassConcentrationOfPM2p5(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM2p5, DEFAULT_MISS_VALUE));
					// PM1质量浓度
					String massConcentrationOfPM1 = elementDatas[7].trim();
					aap.setMassConcentrationOfPM1(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM1, DEFAULT_MISS_VALUE));

					parseResult.put(aap);
					parseResult.put(new ReportInfo<AgmeReportHeader>(aap, dataLine));

				}else if(elementDatas.length == 24){//24列报文
					AtmosphericAerosolPmmul aap = new AtmosphericAerosolPmmul();
					// 观测站区站号
					String stationNumberChina = elementDatas[0];
					aap.setStationNumberChina(stationNumberChina);
					// 项目代码
					// int itemCode = Integer.parseInt(elementDatas[1]);
//					double projectCode = Double.parseDouble(elementDatas[1]);
					String projectCode= elementDatas[1];
					if("".equals(projectCode)||projectCode==null){
						projectCode="999999";
					}
					aap.setProjectCode(projectCode);
					// 年份
					String year = elementDatas[2];
					// 序日
					String julianDay = elementDatas[3];
					// 时分
					String hourMin = elementDatas[4];
					
					hourMin = String.format("%04d", Integer.parseInt(hourMin));

					String ymd = DateUtil.convertJulianDay2Date(Integer.parseInt(year), Integer.parseInt(julianDay));

					try {
						Date d_datetime = sdf.parse(ymd + hourMin + "00");
						aap.setD_datetime(d_datetime);
						aap.setReport_time(d_datetime);
						
						//2019-7-16 cuihongyuan
						if(!TimeCheckUtil.checkTime(d_datetime)){
							ReportError reportError = new ReportError();
							reportError.setMessage("time check error!");
							reportError.setSegment(dataLine);
							parseResult.put(reportError);
							continue;
						}
						
					} catch (ParseException e) {
						ReportError reportError = new ReportError();
						reportError.setMessage("DataTime error!");
						reportError.setSegment(txtFileContent.toString());
						parseResult.put(reportError);
						continue;
					}

					// PM10质量浓度
					String massConcentrationOfPM10 = elementDatas[5];
					aap.setMassConcentrationOfPM10(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM10, DEFAULT_MISS_VALUE));
					// PM2.5质量浓度
					String massConcentrationOfPM2p5 = elementDatas[6];
					aap.setMassConcentrationOfPM2p5(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM2p5, DEFAULT_MISS_VALUE));
					// PM1质量浓度
					String massConcentrationOfPM1 = elementDatas[7].trim();
					aap.setMassConcentrationOfPM1(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM1, DEFAULT_MISS_VALUE));
					//1小时 PM10质量浓度
					String massConcentrationOfPM10_1hour = elementDatas[8].trim();
					aap.setMassConcentrationOfPM10_1hour(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM10_1hour, DEFAULT_MISS_VALUE));
					//1小时 PM2.5质量浓度
					String massConcentrationOfPM2p5_1hour = elementDatas[9].trim();
					aap.setMassConcentrationOfPM2p5_1hour(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM2p5_1hour, DEFAULT_MISS_VALUE));
					//1小时 PM1质量浓度
					String massConcentrationOfPM1_1hour = elementDatas[10].trim();
					aap.setMassConcentrationOfPM1_1hour(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM1_1hour, DEFAULT_MISS_VALUE));
					//24小时 PM10质量浓度
					String massConcentrationOfPM10_24hour = elementDatas[11].trim();
					aap.setMassConcentrationOfPM10_24hour(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM10_24hour, DEFAULT_MISS_VALUE));
					//24小时 PM2.5质量浓度
					String massConcentrationOfPM2p5_24hour = elementDatas[12].trim();
					aap.setMassConcentrationOfPM2p5_24hour(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM2p5_24hour, DEFAULT_MISS_VALUE));
					//24小时 PM1质量浓度
					String massConcentrationOfPM1_24hour = elementDatas[13].trim();
					aap.setMassConcentrationOfPM1_24hour(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM1_24hour, DEFAULT_MISS_VALUE));
					//旁路流量
					String bypassFlow = elementDatas[14].trim();
					aap.setBypassFlow(ElementValUtil.CheckValidValue2Double(bypassFlow, DEFAULT_MISS_VALUE));
					//PM10-2.5主路流量
					String mainRoadFlowOfPM10_2p5 = elementDatas[16].trim();
					aap.setMainRoadFlowOfPM10_2p5(ElementValUtil.CheckValidValue2Double(mainRoadFlowOfPM10_2p5, DEFAULT_MISS_VALUE));
					//负载率
					String loadRate = elementDatas[17].trim();
					aap.setLoadRate(ElementValUtil.CheckValidValue2Double(loadRate, DEFAULT_MISS_VALUE));
					//频率
					String frequency = elementDatas[18].trim();
					aap.setFrequency(ElementValUtil.CheckValidValue2Double(frequency, DEFAULT_MISS_VALUE));
					//噪音
					String noise = elementDatas[19].trim();
					aap.setNoise(ElementValUtil.CheckValidValue2Double(noise, DEFAULT_MISS_VALUE));
					//气温
					String temperature = elementDatas[20].trim();
					aap.setTemperature(ElementValUtil.CheckValidValue2Double(temperature, DEFAULT_MISS_VALUE));
					//气压
					String pressure = elementDatas[21].trim();
					aap.setPressure(ElementValUtil.CheckValidValue2Double(pressure, DEFAULT_MISS_VALUE));
					//相对湿度
					String relativeHumidity = elementDatas[22].trim();
					aap.setRelativeHumidity(ElementValUtil.CheckValidValue2Double(relativeHumidity, DEFAULT_MISS_VALUE));
					//运行状态码
					String runningStatusCode = elementDatas[23].trim();
					aap.setRunningStatusCode(ElementValUtil.CheckValidValue2Double(runningStatusCode, DEFAULT_MISS_VALUE));
					
					parseResult.put(aap);
					parseResult.put(new ReportInfo<AgmeReportHeader>(aap, dataLine));
				}else {
					ReportError reportError = new ReportError();
					reportError.setMessage("Report error!");
					reportError.setSegment(dataLine);
					parseResult.put(reportError);
				}
				parseResult.setSuccess(true);
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
	}

	/**
	 * @Title: decodeGIM
	 * @Description: 大气成分气溶胶PM10/PM2.5/PM1质量浓度（PMMUL）GIM格式解析
	 * @param file
	 * @param fileCode
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 * void
	 * @throws: 
	 */
	private void decodeGIM(File file, String fileCode) throws UnsupportedEncodingException, FileNotFoundException {
		List<String> txtFileContent = FileUtil.getTxtFileContent(file, fileCode);
		// 首先判断文件不是空的，然后需要判断最少有两行数据
		if (txtFileContent != null && txtFileContent.size() >= 1) {
			SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DATETIME_FORMAT_YMDHMS);
			for (String dataLine : txtFileContent) {
				dataLine=dataLine.replaceAll("\\/+", "999999");
				String[] elementDatas = (dataLine.replaceAll("\\s+", "") + " ").split(",");
				if (elementDatas.length == 38) {
					AtmosphericAerosolPmmul aap = new AtmosphericAerosolPmmul();
					// 观测站区站号
					String stationNumberChina = elementDatas[0];
					aap.setStationNumberChina(stationNumberChina);
					// 项目代码
					// int itemCode = Integer.parseInt(elementDatas[1]);
//					double projectCode = Double.parseDouble(elementDatas[1]);
					String projectCode = elementDatas[1];
					if("".equals(projectCode)||projectCode==null){
						projectCode="999999";
					}
					aap.setProjectCode(projectCode);
					// 年份
					String year = elementDatas[2];
					// 序日
					String julianDay = elementDatas[3];
					// 时分
					String hourMin = elementDatas[4];
					
					hourMin = String.format("%04d", Integer.parseInt(hourMin));

					String ymd = DateUtil.convertJulianDay2Date(Integer.parseInt(year), Integer.parseInt(julianDay));

					// aap.setDataObservationalYear(Double.parseDouble(year));
					// aap.setDataObservationMonth(Double.parseDouble(ymd.substring(4, 6)));
					// aap.setDataObservationDay(Double.parseDouble(ymd.substring(6, 8)));
					// aap.setDataObservationHour(Double.parseDouble(hourMin.substring(0, 2)));
					// aap.setDataObservationMin(Double.parseDouble(hourMin.substring(2, 4)));
					try {
						Date d_datetime = sdf.parse(ymd + hourMin + "00");
						aap.setD_datetime(d_datetime);
						aap.setReport_time(d_datetime);
						//2019-7-16 cuihongyuan
						if(!TimeCheckUtil.checkTime(d_datetime)){
							ReportError reportError = new ReportError();
							reportError.setMessage("time check error!");
							reportError.setSegment(dataLine);
							parseResult.put(reportError);
							continue;
						}
					} catch (ParseException e) {
						ReportError reportError = new ReportError();
						reportError.setMessage("DataTime error!");
						reportError.setSegment(txtFileContent.toString());
						parseResult.put(reportError);
						continue;
					}
					// 存储位置
					String storageLocation = elementDatas[5];
					aap.setStorageLocation(ElementValUtil.CheckValidValue2Double(storageLocation, DEFAULT_MISS_VALUE));
					// 重量因数
					String weightFactor = elementDatas[6];
					aap.setWeightFactor(ElementValUtil.CheckValidValue2Double(weightFactor, DEFAULT_MISS_VALUE));
					// 错误代码
					String errorCode = elementDatas[7];
					aap.setErrorCode(ElementValUtil.CheckValidValue2Double(errorCode, DEFAULT_MISS_VALUE));
					// 电池电压代码
					String batteryVoltageCode = elementDatas[8];
					aap.setBatteryVoltageCode(ElementValUtil.CheckValidValue2Double(batteryVoltageCode, DEFAULT_MISS_VALUE));
					// 阀电流
					String valveCurrent = elementDatas[9];
					aap.setValveCurrent(ElementValUtil.CheckValidValue2Double(valveCurrent, DEFAULT_MISS_VALUE));
					// UeL综合订证计数
					String comprehensiveCorrectionCount = elementDatas[10];
					aap.setComprehensiveCorrectionCount(ElementValUtil.CheckValidValue2Double(comprehensiveCorrectionCount, DEFAULT_MISS_VALUE));
					// Ue4气压计数
					String barometricCount = elementDatas[11];
					aap.setBarometricCount(ElementValUtil.CheckValidValue2Double(barometricCount, DEFAULT_MISS_VALUE));
					// Ue3备用
					// String ue3 = elementDatas[12];
					// Ue2湿度计数
					String humidityCount = elementDatas[13];
					aap.setHumidityCount(ElementValUtil.CheckValidValue2Double(humidityCount, DEFAULT_MISS_VALUE));
					// Ue1温度计数
					String temperatureCount = elementDatas[14];
					aap.setTemperatureCount(ElementValUtil.CheckValidValue2Double(temperatureCount, DEFAULT_MISS_VALUE));
					// 时间间隔
					String timeInterval = elementDatas[15];
					aap.setTimeInterval(ElementValUtil.CheckValidValue2Double(timeInterval, DEFAULT_MISS_VALUE));
					// S1风速计量因子
					String windSpeedMeasurementFactor = elementDatas[16];
					aap.setWindSpeedMeasurementFactor(ElementValUtil.CheckValidValue2Double(windSpeedMeasurementFactor, DEFAULT_MISS_VALUE));
					// S2风向计量因子
					String windDirectionMeasurementFactor = elementDatas[17];
					aap.setWindDirectionMeasurementFactor(ElementValUtil.CheckValidValue2Double(windDirectionMeasurementFactor, DEFAULT_MISS_VALUE));
					// S3降水计量因子
					String precipitationMeasurementFactor = elementDatas[18];
					aap.setPrecipitationMeasurementFactor(ElementValUtil.CheckValidValue2Double(precipitationMeasurementFactor, DEFAULT_MISS_VALUE));
					// T_K温度斜率订证
					String temperatureLinearSlopeRate = elementDatas[19];
					aap.setTemperatureLinearSlopeRate(ElementValUtil.CheckValidValue2Double(temperatureLinearSlopeRate, DEFAULT_MISS_VALUE));
					// H_K湿度斜率订证
					String humidityLinearSlopeRate = elementDatas[20];
					aap.setHumidityLinearSlopeRate(ElementValUtil.CheckValidValue2Double(humidityLinearSlopeRate, DEFAULT_MISS_VALUE));
					// P_K气压斜率订证
					String pressureLinearSlopeRate = elementDatas[21];
					aap.setPressureLinearSlopeRate(ElementValUtil.CheckValidValue2Double(pressureLinearSlopeRate, DEFAULT_MISS_VALUE));
					// T_b温度偏移订证
					String temperatureLinearIntercept = elementDatas[22];
					aap.setTemperatureLinearIntercept(ElementValUtil.CheckValidValue2Double(temperatureLinearIntercept, DEFAULT_MISS_VALUE));
					// H_b湿度偏移订证
					String humidityLinearIntercept = elementDatas[23];
					aap.setHumidityLinearIntercept(ElementValUtil.CheckValidValue2Double(humidityLinearIntercept, DEFAULT_MISS_VALUE));
					// P_b气压偏移订证
					String pressureLinearIntercept = elementDatas[24];
					aap.setPressureLinearIntercept(ElementValUtil.CheckValidValue2Double(pressureLinearIntercept, DEFAULT_MISS_VALUE));
					// WS风速灵敏度
					String windSpeedSensitivity = elementDatas[25];
					aap.setWindSpeedSensitivity(ElementValUtil.CheckValidValue2Double(windSpeedSensitivity, DEFAULT_MISS_VALUE));
					// WD风向倾角
					String windDirectionAngle = elementDatas[26];
					aap.setWindDirectionAngle(ElementValUtil.CheckValidValue2Double(windDirectionAngle, DEFAULT_MISS_VALUE));
					// Rain降水传感器订证因子
					String precipitationSensorCorrectionFactor = elementDatas[27];
					aap.setPrecipitationSensorCorrectionFactor(ElementValUtil.CheckValidValue2Double(precipitationSensorCorrectionFactor, DEFAULT_MISS_VALUE));
					// 气压
					String pressure = elementDatas[28];
					aap.setPressure(ElementValUtil.CheckValidValue2Double(pressure, DEFAULT_MISS_VALUE));
					// 备用
					// String bak = elementDatas[29];
					// 湿度
					String relativeHumidity = elementDatas[30];
					aap.setRelativeHumidity(ElementValUtil.CheckValidValue2Double(relativeHumidity, DEFAULT_MISS_VALUE));
					// 温度
					String temperature = elementDatas[31];
					aap.setTemperature(ElementValUtil.CheckValidValue2Double(temperature, DEFAULT_MISS_VALUE));
					// 风速
					String windSpeed = elementDatas[32];
					aap.setWindSpeed(ElementValUtil.CheckValidValue2Double(windSpeed, DEFAULT_MISS_VALUE));
					// 风向
					String windDirection = elementDatas[33];
					aap.setWindDirection(ElementValUtil.CheckValidValue2Double(windDirection, DEFAULT_MISS_VALUE));
					// 降水
					String precipitation = elementDatas[34];
					aap.setPrecipitation(ElementValUtil.CheckValidValue2Double(precipitation, DEFAULT_MISS_VALUE));
					// PM10
					String massConcentrationOfPM10 = elementDatas[35];
					aap.setMassConcentrationOfPM10(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM10, DEFAULT_MISS_VALUE));
					// PM2.5
					String massConcentrationOfPM2p5 = elementDatas[36];
					aap.setMassConcentrationOfPM2p5(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM2p5, DEFAULT_MISS_VALUE));
					// PM1
					String massConcentrationOfPM1 = elementDatas[37].trim();
					aap.setMassConcentrationOfPM1(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM1, DEFAULT_MISS_VALUE));

					parseResult.put(aap);
					parseResult.put(new ReportInfo<AgmeReportHeader>(aap, dataLine));
				}else if(elementDatas.length == 24){//24列报文
					AtmosphericAerosolPmmul aap = new AtmosphericAerosolPmmul();
					// 观测站区站号
					String stationNumberChina = elementDatas[0];
					aap.setStationNumberChina(stationNumberChina);
					// 项目代码
					// int itemCode = Integer.parseInt(elementDatas[1]);
//					double projectCode = Double.parseDouble(elementDatas[1]);
					String projectCode= elementDatas[1];
					if("".equals(projectCode)||projectCode==null){
						projectCode="999999";
					}
					aap.setProjectCode(projectCode);
					// 年份
					String year = elementDatas[2];
					// 序日
					String julianDay = elementDatas[3];
					// 时分
					String hourMin = elementDatas[4];
					
					hourMin = String.format("%04d", Integer.parseInt(hourMin));

					String ymd = DateUtil.convertJulianDay2Date(Integer.parseInt(year), Integer.parseInt(julianDay));

					try {
						Date d_datetime = sdf.parse(ymd + hourMin + "00");
						aap.setD_datetime(d_datetime);
						aap.setReport_time(d_datetime);
						//2019-7-16 cuihongyuan
						if(!TimeCheckUtil.checkTime(d_datetime)){
							ReportError reportError = new ReportError();
							reportError.setMessage("time check error!");
							reportError.setSegment(dataLine);
							parseResult.put(reportError);
							continue;
						}
					} catch (ParseException e) {
						ReportError reportError = new ReportError();
						reportError.setMessage("DataTime error!");
						reportError.setSegment(txtFileContent.toString());
						parseResult.put(reportError);
						continue;
					}

					// PM10质量浓度
					String massConcentrationOfPM10 = elementDatas[5];
					aap.setMassConcentrationOfPM10(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM10, DEFAULT_MISS_VALUE));
					// PM2.5质量浓度
					String massConcentrationOfPM2p5 = elementDatas[6];
					aap.setMassConcentrationOfPM2p5(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM2p5, DEFAULT_MISS_VALUE));
					// PM1质量浓度
					String massConcentrationOfPM1 = elementDatas[7].trim();
					aap.setMassConcentrationOfPM1(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM1, DEFAULT_MISS_VALUE));
					//1小时 PM10质量浓度
					String massConcentrationOfPM10_1hour = elementDatas[8].trim();
					aap.setMassConcentrationOfPM10_1hour(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM10_1hour, DEFAULT_MISS_VALUE));
					//1小时 PM2.5质量浓度
					String massConcentrationOfPM2p5_1hour = elementDatas[9].trim();
					aap.setMassConcentrationOfPM2p5_1hour(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM2p5_1hour, DEFAULT_MISS_VALUE));
					//1小时 PM1质量浓度
					String massConcentrationOfPM1_1hour = elementDatas[10].trim();
					aap.setMassConcentrationOfPM1_1hour(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM1_1hour, DEFAULT_MISS_VALUE));
					//24小时 PM10质量浓度
					String massConcentrationOfPM10_24hour = elementDatas[11].trim();
					aap.setMassConcentrationOfPM10_24hour(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM10_24hour, DEFAULT_MISS_VALUE));
					//24小时 PM2.5质量浓度
					String massConcentrationOfPM2p5_24hour = elementDatas[12].trim();
					aap.setMassConcentrationOfPM2p5_24hour(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM2p5_24hour, DEFAULT_MISS_VALUE));
					//24小时 PM1质量浓度
					String massConcentrationOfPM1_24hour = elementDatas[13].trim();
					aap.setMassConcentrationOfPM1_24hour(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM1_24hour, DEFAULT_MISS_VALUE));
					//旁路流量
					String bypassFlow = elementDatas[14].trim();
					aap.setBypassFlow(ElementValUtil.CheckValidValue2Double(bypassFlow, DEFAULT_MISS_VALUE));
					//PM10-2.5主路流量
					String mainRoadFlowOfPM10_2p5 = elementDatas[16].trim();
					aap.setMainRoadFlowOfPM10_2p5(ElementValUtil.CheckValidValue2Double(mainRoadFlowOfPM10_2p5, DEFAULT_MISS_VALUE));
					//负载率
					String loadRate = elementDatas[17].trim();
					aap.setLoadRate(ElementValUtil.CheckValidValue2Double(loadRate, DEFAULT_MISS_VALUE));
					//频率
					String frequency = elementDatas[18].trim();
					aap.setFrequency(ElementValUtil.CheckValidValue2Double(frequency, DEFAULT_MISS_VALUE));
					//噪音
					String noise = elementDatas[19].trim();
					aap.setNoise(ElementValUtil.CheckValidValue2Double(noise, DEFAULT_MISS_VALUE));
					//气温
					String temperature = elementDatas[20].trim();
					aap.setTemperature(ElementValUtil.CheckValidValue2Double(temperature, DEFAULT_MISS_VALUE));
					//气压
					String pressure = elementDatas[21].trim();
					aap.setPressure(ElementValUtil.CheckValidValue2Double(pressure, DEFAULT_MISS_VALUE));
					//相对湿度
					String relativeHumidity = elementDatas[22].trim();
					aap.setRelativeHumidity(ElementValUtil.CheckValidValue2Double(relativeHumidity, DEFAULT_MISS_VALUE));
					//运行状态码
					String runningStatusCode = elementDatas[23].trim();
					aap.setRunningStatusCode(ElementValUtil.CheckValidValue2Double(runningStatusCode, DEFAULT_MISS_VALUE));
					
					parseResult.put(aap);
					parseResult.put(new ReportInfo<AgmeReportHeader>(aap, dataLine));
				}else {
					ReportError reportError = new ReportError();
					reportError.setMessage("Report error!");
					reportError.setSegment(dataLine);
					parseResult.put(reportError);
				}
				parseResult.setSuccess(true);
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
	}

	/**
	 * @Title: decodeBAM
	 * @Description: 大气成分气溶胶PM10/PM2.5/PM1质量浓度（PMMUL）BAM格式数据解析
	 * @param file
	 * @param fileCode
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 */
	private void decodeBAM(File file, String fileCode) throws UnsupportedEncodingException, FileNotFoundException {
		List<String> txtFileContent = FileUtil.getTxtFileContent(file, fileCode);
		// 首先判断文件不是空的，然后需要判断最少有1行数据
		if (txtFileContent != null && txtFileContent.size() >= 1) {
			SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DATETIME_FORMAT_YMDHMS);
			for (String dataLine : txtFileContent) {
				dataLine=dataLine.replaceAll("\\/+", "999999");
				String[] elementDatas = (dataLine.replaceAll("\\s+", "") + " ").split(",");
				if (elementDatas.length == 8) {
                    AtmosphericAerosolPmmul aap = new AtmosphericAerosolPmmul();
                    String stationNumberChina = elementDatas[0];// 观测站区站号
                    aap.setStationNumberChina(stationNumberChina);
                    String projectCode = elementDatas[1]; // 项目代码
                    if ("".equals(projectCode) || projectCode == null) {
                        projectCode = "999999";
                    }
                    aap.setProjectCode(projectCode);
                    String year = elementDatas[2];// 年份
                    String julianDay = elementDatas[3]; // 序日
                    String hourMin = elementDatas[4]; // 时分
                    hourMin = String.format("%04d", Integer.parseInt(hourMin));
                    String ymd = DateUtil.convertJulianDay2Date(Integer.parseInt(year), Integer.parseInt(julianDay));
                    try {
                        Date d_datetime = sdf.parse(ymd + hourMin + "00");
                        aap.setD_datetime(d_datetime);
                        aap.setReport_time(d_datetime);
                        if (!TimeCheckUtil.checkTime(d_datetime)) {//2019-7-16 cuihongyuan
                            ReportError reportError = new ReportError();
                            reportError.setMessage("time check error!");
                            reportError.setSegment(dataLine);
                            parseResult.put(reportError);
                            continue;
                        }
                    } catch (ParseException e) {
                        ReportError reportError = new ReportError();
                        reportError.setMessage("DataTime error!");
                        reportError.setSegment(txtFileContent.toString());
                        parseResult.put(reportError);
                        continue;
                    }
                    // PM10质量浓度
                    String massConcentrationOfPM10 = elementDatas[5];
                    aap.setMassConcentrationOfPM10(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM10, DEFAULT_MISS_VALUE));
                    // PM2.5质量浓度
                    String massConcentrationOfPM2p5 = elementDatas[6];
                    aap.setMassConcentrationOfPM2p5(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM2p5, DEFAULT_MISS_VALUE));
                    // PM1质量浓度
                    String massConcentrationOfPM1 = elementDatas[7].trim();
                    aap.setMassConcentrationOfPM1(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM1, DEFAULT_MISS_VALUE));
                    parseResult.put(aap);
                    parseResult.put(new ReportInfo<AgmeReportHeader>(aap, dataLine));
                }else if (elementDatas.length == 23) {
					AtmosphericAerosolPmmul aap = new AtmosphericAerosolPmmul();
					// 观测站区站号
					String stationNumberChina = elementDatas[0];
					aap.setStationNumberChina(stationNumberChina);
					// 项目代码
					// int itemCode = Integer.parseInt(elementDatas[1]);
//					double projectCode = Double.parseDouble(elementDatas[1]);
					String projectCode = elementDatas[1];
					if("".equals(projectCode)||projectCode==null){
						projectCode="999999";
					}
					aap.setProjectCode(projectCode);
					// 年份
					String year = elementDatas[2];
					// 序日
					String julianDay = elementDatas[3];
					// 时分
					String hourMin = elementDatas[4];
					
					hourMin = String.format("%04d", Integer.parseInt(hourMin));

					String ymd = DateUtil.convertJulianDay2Date(Integer.parseInt(year), Integer.parseInt(julianDay));

					// aap.setDataObservationalYear(Double.parseDouble(year));
					// aap.setDataObservationMonth(Double.parseDouble(ymd.substring(4, 6)));
					// aap.setDataObservationDay(Double.parseDouble(ymd.substring(6, 8)));
					// aap.setDataObservationHour(Double.parseDouble(hourMin.substring(0, 2)));
					// aap.setDataObservationMin(Double.parseDouble(hourMin.substring(2, 4)));

					try {
						Date d_datetime = sdf.parse(ymd + hourMin + "00");
						aap.setD_datetime(d_datetime);
						aap.setReport_time(d_datetime);
						//2019-7-16 cuihongyuan
						if(!TimeCheckUtil.checkTime(d_datetime)){
							ReportError reportError = new ReportError();
							reportError.setMessage("time check error!");
							reportError.setSegment(dataLine);
							parseResult.put(reportError);
							continue;
						}
					} catch (ParseException e) {
						ReportError reportError = new ReportError();
						reportError.setMessage("DataTime error!");
						reportError.setSegment(txtFileContent.toString());
						parseResult.put(reportError);
						continue;
					}

					// PM10浓度(标况)
					double standardConcentration_pm10 = ElementValUtil.CheckValidValue2Double(elementDatas[5], DEFAULT_MISS_VALUE);
					aap.setMassConcentrationOfPM10(standardConcentration_pm10);
					// PM2.5浓度(标况)
					double standardConcentration_pm2p5 = ElementValUtil.CheckValidValue2Double(elementDatas[6], DEFAULT_MISS_VALUE);
					aap.setMassConcentrationOfPM2p5(standardConcentration_pm2p5);
					// PM1浓度(标况)
					double standardConcentration_pm1 = ElementValUtil.CheckValidValue2Double(elementDatas[7], DEFAULT_MISS_VALUE);
					aap.setMassConcentrationOfPM1(standardConcentration_pm1);
					// PM10测量数据标识
					String measurementDataIdentification_pm10 = elementDatas[8];
					aap.setMeasurementDataFlag_pm10(measurementDataIdentification_pm10);
					// PM2.5测量数据标识
					String measurementDataIdentification_pm2p5 = elementDatas[9];
					aap.setMeasurementDataFlag_pm2p5(measurementDataIdentification_pm2p5);
					// PM1测量数据标识
					String measurementDataIdentification_pm1 = elementDatas[10];
					aap.setMeasurementDataFlag_pm1(measurementDataIdentification_pm1);
					// PM10仪器测量的流量
					double flowRate_pm10 = ElementValUtil.CheckValidValue2Double(elementDatas[11], DEFAULT_MISS_VALUE);
					aap.setFlowRate_pm10(flowRate_pm10);
					// PM10仪器测量的气温
					double temperature_pm10 = ElementValUtil.CheckValidValue2Double(elementDatas[12], DEFAULT_MISS_VALUE);
					aap.setTemperature_pm10(temperature_pm10);
					// PM10仪器测量的气压
					double pressure_pm10 = ElementValUtil.CheckValidValue2Double(elementDatas[13], DEFAULT_MISS_VALUE);
					if(!String.valueOf(pressure_pm10).startsWith("99999")){
						pressure_pm10*=10;
					}
					aap.setPressure_pm10(pressure_pm10);
					// PM10仪器测量的相对湿度
					double relativeHumidity_pm10 = ElementValUtil.CheckValidValue2Double(elementDatas[14], DEFAULT_MISS_VALUE);
					aap.setRelativeHumidity_pm10(relativeHumidity_pm10);
					// PM2.5仪器测量的流量
					double flowRate_pm2p5 = ElementValUtil.CheckValidValue2Double(elementDatas[15], DEFAULT_MISS_VALUE);
					aap.setFlowRate_pm2p5(flowRate_pm2p5);
					// PM2.5仪器测量的气温
					double temperature_pm2p5 = ElementValUtil.CheckValidValue2Double(elementDatas[16], DEFAULT_MISS_VALUE);
					aap.setTemperature_pm2p5(temperature_pm2p5);
					// PM2.5仪器测量的气压
					double pressure_pm2p5 = ElementValUtil.CheckValidValue2Double(elementDatas[17], DEFAULT_MISS_VALUE);
					if(!String.valueOf(pressure_pm2p5).startsWith("99999")){
						pressure_pm2p5*=10;
					}
					aap.setPressure_pm2p5(pressure_pm2p5);
					// PM2.5仪器测量的相对湿度
					double relativeHumidity_pm2p5 = ElementValUtil.CheckValidValue2Double(elementDatas[18], DEFAULT_MISS_VALUE);
					aap.setRelativeHumidity_pm2p5(relativeHumidity_pm2p5);
					// PM1.0仪器测量的流量
					double flowRate_pm1 = ElementValUtil.CheckValidValue2Double(elementDatas[19], DEFAULT_MISS_VALUE);
					aap.setFlowRate_pm1(flowRate_pm1);
					// PM1.0仪器测量的气温
					double temperature_pm1 = ElementValUtil.CheckValidValue2Double(elementDatas[20], DEFAULT_MISS_VALUE);
					aap.setTemperature_pm1(temperature_pm1);
					// PM1.0仪器测量的气压
					double pressure_pm1 = ElementValUtil.CheckValidValue2Double(elementDatas[21], DEFAULT_MISS_VALUE);
					if(!String.valueOf(pressure_pm1).startsWith("99999")){
						pressure_pm1*=10;
					}
					aap.setPressure_pm1(pressure_pm1);
					// PM1.0仪器测量的相对湿度
					double relativeHumidity_pm1 = ElementValUtil.CheckValidValue2Double(elementDatas[22].trim(), DEFAULT_MISS_VALUE);
					aap.setRelativeHumidity_pm1(relativeHumidity_pm1);

					parseResult.put(aap);

					parseResult.put(new ReportInfo<AgmeReportHeader>(aap, dataLine));
				}else if(elementDatas.length == 24){//24列报文
					AtmosphericAerosolPmmul aap = new AtmosphericAerosolPmmul();
					// 观测站区站号
					String stationNumberChina = elementDatas[0];
					aap.setStationNumberChina(stationNumberChina);
					// 项目代码
					// int itemCode = Integer.parseInt(elementDatas[1]);
//					double projectCode = Double.parseDouble(elementDatas[1]);
					String projectCode= elementDatas[1];
					if("".equals(projectCode)||projectCode==null){
						projectCode="999999";
					}
					aap.setProjectCode(projectCode);
					// 年份
					String year = elementDatas[2];
					// 序日
					String julianDay = elementDatas[3];
					// 时分
					String hourMin = elementDatas[4];
					
					hourMin = String.format("%04d", Integer.parseInt(hourMin));

					String ymd = DateUtil.convertJulianDay2Date(Integer.parseInt(year), Integer.parseInt(julianDay));

					try {
						Date d_datetime = sdf.parse(ymd + hourMin + "00");
						aap.setD_datetime(d_datetime);
						aap.setReport_time(d_datetime);
						//2019-7-16 cuihongyuan
						if(!TimeCheckUtil.checkTime(d_datetime)){
							ReportError reportError = new ReportError();
							reportError.setMessage("time check error!");
							reportError.setSegment(dataLine);
							parseResult.put(reportError);
							continue;
						}
					} catch (ParseException e) {
						ReportError reportError = new ReportError();
						reportError.setMessage("DataTime error!");
						reportError.setSegment(txtFileContent.toString());
						parseResult.put(reportError);
						continue;
					}

					// PM10质量浓度
					String massConcentrationOfPM10 = elementDatas[5];
					aap.setMassConcentrationOfPM10(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM10, DEFAULT_MISS_VALUE));
					// PM2.5质量浓度
					String massConcentrationOfPM2p5 = elementDatas[6];
					aap.setMassConcentrationOfPM2p5(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM2p5, DEFAULT_MISS_VALUE));
					// PM1质量浓度
					String massConcentrationOfPM1 = elementDatas[7].trim();
					aap.setMassConcentrationOfPM1(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM1, DEFAULT_MISS_VALUE));
					//1小时 PM10质量浓度
					String massConcentrationOfPM10_1hour = elementDatas[8].trim();
					aap.setMassConcentrationOfPM10_1hour(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM10_1hour, DEFAULT_MISS_VALUE));
					//1小时 PM2.5质量浓度
					String massConcentrationOfPM2p5_1hour = elementDatas[9].trim();
					aap.setMassConcentrationOfPM2p5_1hour(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM2p5_1hour, DEFAULT_MISS_VALUE));
					//1小时 PM1质量浓度
					String massConcentrationOfPM1_1hour = elementDatas[10].trim();
					aap.setMassConcentrationOfPM1_1hour(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM1_1hour, DEFAULT_MISS_VALUE));
					//24小时 PM10质量浓度
					String massConcentrationOfPM10_24hour = elementDatas[11].trim();
					aap.setMassConcentrationOfPM10_24hour(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM10_24hour, DEFAULT_MISS_VALUE));
					//24小时 PM2.5质量浓度
					String massConcentrationOfPM2p5_24hour = elementDatas[12].trim();
					aap.setMassConcentrationOfPM2p5_24hour(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM2p5_24hour, DEFAULT_MISS_VALUE));
					//24小时 PM1质量浓度
					String massConcentrationOfPM1_24hour = elementDatas[13].trim();
					aap.setMassConcentrationOfPM1_24hour(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM1_24hour, DEFAULT_MISS_VALUE));
					//旁路流量
					String bypassFlow = elementDatas[14].trim();
					aap.setBypassFlow(ElementValUtil.CheckValidValue2Double(bypassFlow, DEFAULT_MISS_VALUE));
					//PM10-2.5主路流量
					String mainRoadFlowOfPM10_2p5 = elementDatas[16].trim();
					aap.setMainRoadFlowOfPM10_2p5(ElementValUtil.CheckValidValue2Double(mainRoadFlowOfPM10_2p5, DEFAULT_MISS_VALUE));
					//负载率
					String loadRate = elementDatas[17].trim();
					aap.setLoadRate(ElementValUtil.CheckValidValue2Double(loadRate, DEFAULT_MISS_VALUE));
					//频率
					String frequency = elementDatas[18].trim();
					aap.setFrequency(ElementValUtil.CheckValidValue2Double(frequency, DEFAULT_MISS_VALUE));
					//噪音
					String noise = elementDatas[19].trim();
					aap.setNoise(ElementValUtil.CheckValidValue2Double(noise, DEFAULT_MISS_VALUE));
					//气温
					String temperature = elementDatas[20].trim();
					aap.setTemperature(ElementValUtil.CheckValidValue2Double(temperature, DEFAULT_MISS_VALUE));
					//气压
					String pressure = elementDatas[21].trim();
					aap.setPressure(ElementValUtil.CheckValidValue2Double(pressure, DEFAULT_MISS_VALUE));
					//相对湿度
					String relativeHumidity = elementDatas[22].trim();
					aap.setRelativeHumidity(ElementValUtil.CheckValidValue2Double(relativeHumidity, DEFAULT_MISS_VALUE));
					//运行状态码
					String runningStatusCode = elementDatas[23].trim();
					aap.setRunningStatusCode(ElementValUtil.CheckValidValue2Double(runningStatusCode, DEFAULT_MISS_VALUE));
					
					parseResult.put(aap);
					parseResult.put(new ReportInfo<AgmeReportHeader>(aap, dataLine));
				}else {
					ReportError reportError = new ReportError();
					reportError.setMessage("Report error!");
					reportError.setSegment(dataLine);
					parseResult.put(reportError);
				}
			}
			parseResult.setSuccess(true);

		} else {
			if (txtFileContent == null || txtFileContent.size() == 0) {
				// 空文件
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
			} else {
				// 数据只有一行，格式不正确
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			}
		}
	}
	public static void main(String[] args) {
		File file = new File("D:\\TEMP\\G.2.8.1\\5-23\\4\\Z_CAWN_I_57186_20190522080000_O_AER-FLD-PMMUL-BAM.TXT");
		DecodePmmul dp = new DecodePmmul();
		ParseResult<AtmosphericAerosolPmmul> parseResult = dp.decode(file);
	}

}
