package cma.cimiss2.dpc.decoder.cawn;

import java.io.BufferedReader;
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

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.cawn.REG;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.utils.DateUtil;
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * Main class of decode the reactive gas data <br>
 * 反应性气体观测数据解码类
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
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cimiss2.dpc.decoder.bean.cawn.REG}。
 * </ol>
 * </li>
 * </ul>
 * 
 * *<strong> sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br>
 * 54346,0,0,0,5<br>
20180409160000,43,4C00040A,3.249,45.903,47.767,738.414,0.041,-999.9,-999.9,-999.9,-999.9,0,1,3.249,0,42,CC009600,1375.430,-25.071,1350.360,44.944,-999.9,-999.9,-999.9,603.348,0.000,-999.9,-999.9,-999.9,0,1,1375.430,0,-25.071,0,0,1,1350.360,0,48,8C040400,8.471,44.158,48.256,735.300,0.025,-999.9,-999.9,-999.9,-999.9,-999.9,0,1,8.471,0,49,0C100002,47.322,70394,63172,3.250,2.900,0.622,0.687,686.279,-999.9,-999.9,-999.9,0,1,47.322,0<br>
20180409160500,43,4C00040A,2.972,45.717,47.637,738.414,0.041,-999.9,-999.9,-999.9,-999.9,0,1,2.972,0,42,CC009600,1378.890,-26.067,1352.820,44.916,-999.9,-999.9,-999.9,604.833,0.000,-999.9,-999.9,-999.9,0,1,1378.890,0,-26.067,0,0,1,1352.820,0,48,8C040400,8.446,44.102,48.224,735.300,0.026,-999.9,-999.9,-999.9,-999.9,-999.9,0,1,8.446,0,49,0C100002,46.927,70405,63182,3.200,2.550,0.616,0.682,685.979,-999.9,-999.9,-999.9,0,1,46.927,0<br>
......<br>
 * <strong>code:</strong><br>
 * DecodeREG decodeREG = new DecodeREG();<br>
 * ParseResult<REG> parseResult = decodeREG.DecodeFile(new File(String filepath));<br>
 * List<REG> list = parseResult.getData();<br>
 * System.out.println(list.size());<br>
 * System.out.println(list.get(0).getStationNumberChina());<br>
 * <strong>output:</strong><br>
 * 12<br>
 * 54346<br>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月28日 下午2:25:02   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class DecodeREG{
	/**
	 * 解码结果
	 */
	private ParseResult<REG> parseResult = new ParseResult<REG>(false);
	/**
	 * 每条报文要素个数可能为73, 35, 26个
	 */
	public static int GRP_NUM_1 = 73;
	/**
	 * 每条报文要素个数可能为73, 35, 26个
	 */
	public static int GRP_NUM_2 = 35;
	/**
	 * 每条报文要素个数可能为73, 35, 26个
	 */
	public static int GRP_NUM_3 = 26;
	/**
	 * 反应性气体观测数据解码函数   
	 * @param file 待解码文件
	 * @return ParseResult<REG>      解码结果封装
	 */
	public ParseResult<REG> DecodeFile(File file){
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
			BufferedReader bufferedReader = null;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			 //获取文件流
			try{
				read = new InputStreamReader(new FileInputStream(file), fileCode);
				bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				String sp[];
				String header[];
				while((lineTxt = bufferedReader.readLine()) != null){
					lineTxt = lineTxt.trim();
					sp = lineTxt.split(",");
					if((header = sp).length == 5){
						while((lineTxt = bufferedReader.readLine()) != null){
							REG reg = new REG();
							reg.setStationNumberChina(header[0]);
							reg.setLongtitude(to_double(header[1]));
							reg.setLatitude(to_double(header[2]));
							reg.setHeightAboveSeaLevel(to_double(header[3]));
							reg.setRecordFrequency(to_double(header[4]));
							lineTxt = lineTxt.trim();
							sp = lineTxt.split(",");
							if(sp.length == 73){
								try{
									reg.setObservationTime(simpleDateFormat.parse(sp[0]));
									
									//2019-7-16 cuihongyuan
									if(!TimeCheckUtil.checkTime(reg.getObservationTime())){
										ReportError reportError = new ReportError();
										reportError.setMessage("time check error!");
										reportError.setSegment(lineTxt);
										parseResult.put(reportError);
										continue;
									}
									
								}catch (ParseException e) {
									ReportError re = new ReportError();
									re.setMessage("DataTime error!");
									re.setSegment(lineTxt);
									parseResult.put(re);
									continue;
								}
								reg.setSO2_DeviceSN(to_string(sp[1]));
								reg.setSO2_DeviceStateCode(to_string(sp[2]));
								reg.setSO2_Density(to_double(sp[3]));
								reg.setSO2_DeviceInnerTemperature(to_double(sp[4]));
								reg.setSO2_DeviceChamberTemperature(to_double(sp[5]));
								reg.setSO2_DevicePressure(to_double(sp[6]));
								reg.setSO2_DeviceGasFlow(to_double(sp[7]));
								reg.setSO2_DevicePhotomultiplierVoltage(to_double(sp[8]));
								reg.setSO2_DeviceTubeVoltage(to_double(sp[9]));
								reg.setSO2_DeviceLightIntensity(to_double(sp[10]));
								reg.setSO2_DeviceExternalAlarm(to_int(sp[11]));
								reg.setSO2_DeviceZeroPoint(to_double(sp[12]));
								reg.setSO2_DeviceSlope(to_double(sp[13]));
								reg.setSO2_DeviceCalibrationDensity(to_double(sp[14]));
								reg.setSO2_DeviceRevisedSign(to_int(sp[15]));
								
								reg.setNOX_DeviceSN(to_string(sp[16]));
								reg.setNOX_DeviceStateCode(to_string(sp[17]));
								reg.setNO_Density(to_double(sp[18]));
								reg.setNO2_Density(to_double(sp[19]));
								reg.setNOX_Density(to_double(sp[20]));
								reg.setNOX_DeviceInnerTemperature(to_double(sp[21]));
								reg.setNOX_DeviceChamberTemperature(to_double(sp[22]));
								reg.setNOX_DevicePhotomultiplierTemperature(to_double(sp[23]));
								reg.setNOX_DeviceConverterTemperature(to_double(sp[24]));
								reg.setNOX_DevicePressure(to_double(sp[25]));
								reg.setNOX_DeviceGasFlow(to_double(sp[26]));
								reg.setNOX_DevicePhotomultiplierVoltage(to_double(sp[27]));
								reg.setNOX_DeviceO3GeneratorFlow(to_double(sp[28]));
								reg.setNOX_DeviceExternalAlarm(to_int(sp[29]));
								
								reg.setNO_ZeroPoint(to_double(sp[30]));
								reg.setNO_Slope(to_double(sp[31]));
								reg.setNO_CalibrationDensity(to_double(sp[32]));
								reg.setNO_RevisedSign(to_int(sp[33]));
								reg.setNO2_CalibrationDensity(to_double(sp[34]));
								reg.setNO2_RevisedSign(to_int(sp[35]));
								reg.setNOX_ZeroPoint(to_double(sp[36]));
								reg.setNOX_Slope(to_double(sp[37]));
								reg.setNOX_CalibrationDensity(to_double(sp[38]));
								reg.setNOX_RevisedSign(to_int(sp[39]));
								
								reg.setCO_DeviceSN(to_string(sp[40]));
								reg.setCO_DeviceStateCode(to_string(sp[41]));
								reg.setCO_Density(to_double(sp[42]));
								reg.setCO_DeviceInnerTemperature(to_double(sp[43]));
								reg.setCO_DeviceChamberTemperature(to_double(sp[44]));
								reg.setCO_DevicePressure(to_double(sp[45]));
								reg.setCO_DeviceGasFlow(to_double(sp[46]));
								reg.setCO_DeviceBiasVoltage(to_double(sp[47]));
								reg.setCO_DeviceMotorSpeed(to_double(sp[48]));
								reg.setCO_DeviceSRRatio(to_double(sp[49]));
								reg.setCO_LightIntensity(to_double(sp[50]));
								reg.setCO_DeviceExternalAlarm(to_int(sp[51]));
								reg.setCO_DeviceZeroPoint(to_double(sp[52]));
								reg.setCO_DeviceSlope(to_double(sp[53]));
								reg.setCO_DeviceCalibrationDensity(to_double(sp[54]));
								reg.setCO_DeviceRevisedSign(to_int(sp[55]));
								
								reg.setO3_DeviceSN(to_string(sp[56]));
								reg.setO3_DeviceStateCode(to_string(sp[57]));
								reg.setO3_Density(to_double(sp[58]));
								reg.setO3_DeviceALightIntensity(to_int(sp[59]));
								reg.setO3_DeviceBLightIntensity(to_int(sp[60]));
								reg.setO3_DeviceANoise(to_double(sp[61]));
								reg.setO3_DeviceBNoise(to_double(sp[62]));
								reg.setO3_DeviceAFlowRate(to_double(sp[63]));
								reg.setO3_DeviceBFlowRate(to_double(sp[64]));
								reg.setO3_DevicePressure(to_double(sp[65]));
								reg.setO3_DeviceLightSeatTemperature(to_double(sp[66]));
								reg.setO3_DeviceLightTemperature(to_double(sp[67]));
								reg.setO3_DeviceExternalAlarm(to_int(sp[68]));
								reg.setO3_DeviceZeroPoint(to_double(sp[69]));
								reg.setO3_DeviceSlope(to_double(sp[70]));
								reg.setO3_DeviceCalibrationDensity(to_double(sp[71]));
								reg.setO3_DeviceRevisedSign(to_int(sp[72]));
								
								parseResult.put(reg);
								parseResult.setSuccess(true);
								parseResult.put(new ReportInfo<REG>(reg, lineTxt));
							}
							else{
								ReportError re = new ReportError();
								re.setMessage("Number of elements error!");
								re.setSegment(lineTxt);
								parseResult.put(re);
							}
						}
					}
					else if(sp.length == 77){
						while(lineTxt != null){
							lineTxt = lineTxt.trim();
							sp = lineTxt.split(",");
							if(sp.length == 77 && sp[4].length() == 4 && isDigital(sp[4])){
								REG reg = new REG();
								reg.setStationNumberChina(sp[0]);
								reg.setItemCode(to_int(sp[1]));
								reg.setYear(to_int(sp[2]));
								reg.setDayOfYear(to_int(sp[3]));
								reg.setHhmm(to_int(sp[4]));
								String dt = DateUtil.convertJulianDay2Date(reg.getYear(), reg.getDayOfYear()); // 格式为：yyyyMMdd
								int month = Integer.parseInt(dt.substring(4, 6));
								int day = Integer.parseInt(dt.substring(6, 8));
								Calendar calendar = Calendar.getInstance();
								int hour = Integer.parseInt(sp[4].substring(0, 2));
								int min = Integer.parseInt(sp[4].substring(2, 4));
								calendar.set(reg.getYear(), month - 1, day, hour, min, 0);
								calendar.set(Calendar.MILLISECOND, 0);
								Date date = calendar.getTime(); 
								reg.setObservationTime(date);
								
								//2019-7-16 cuihongyuan
								if(!TimeCheckUtil.checkTime(reg.getObservationTime())){
									ReportError reportError = new ReportError();
									reportError.setMessage("time check error!");
									reportError.setSegment(lineTxt);
									parseResult.put(reportError);
									lineTxt = bufferedReader.readLine();
									continue;
								}
								
								int i = 5;
								reg.setSO2_DeviceSN(to_string(sp[i++]));
								reg.setSO2_DeviceStateCode(to_string(sp[i++]));
								reg.setSO2_Density(to_double(sp[i++]));
								reg.setSO2_DeviceInnerTemperature(to_double(sp[i++]));
								reg.setSO2_DeviceChamberTemperature(to_double(sp[i++]));
								reg.setSO2_DevicePressure(to_double(sp[i++]));
								reg.setSO2_DeviceGasFlow(to_double(sp[i++]));
								reg.setSO2_DevicePhotomultiplierVoltage(to_double(sp[i++]));
								reg.setSO2_DeviceTubeVoltage(to_double(sp[i++]));
								reg.setSO2_DeviceLightIntensity(to_double(sp[i++]));
								reg.setSO2_DeviceExternalAlarm(to_int(sp[i++]));
								reg.setSO2_DeviceZeroPoint(to_double(sp[i++]));
								reg.setSO2_DeviceSlope(to_double(sp[i++]));
								reg.setSO2_DeviceCalibrationDensity(to_double(sp[i++]));
								reg.setSO2_DeviceRevisedSign(to_int(sp[i++]));
								
								reg.setNOX_DeviceSN(to_string(sp[i++]));
								reg.setNOX_DeviceStateCode(to_string(sp[i++]));
								reg.setNO_Density(to_double(sp[i++]));
								reg.setNO2_Density(to_double(sp[i++]));
								reg.setNOX_Density(to_double(sp[i++]));
								reg.setNOX_DeviceInnerTemperature(to_double(sp[i++]));
								reg.setNOX_DeviceChamberTemperature(to_double(sp[i++]));
								reg.setNOX_DevicePhotomultiplierTemperature(to_double(sp[i++]));
								reg.setNOX_DeviceConverterTemperature(to_double(sp[i++]));
								reg.setNOX_DevicePressure(to_double(sp[i++]));
								reg.setNOX_DeviceGasFlow(to_double(sp[i++]));
								reg.setNOX_DevicePhotomultiplierVoltage(to_double(sp[i++]));
								reg.setNOX_DeviceO3GeneratorFlow(to_double(sp[i++]));
								reg.setNOX_DeviceExternalAlarm(to_int(sp[i++]));
								
								reg.setNO_ZeroPoint(to_double(sp[i++]));
								reg.setNO_Slope(to_double(sp[i++]));
								reg.setNO_CalibrationDensity(to_double(sp[i++]));
								reg.setNO_RevisedSign(to_int(sp[i++]));
								reg.setNO2_CalibrationDensity(to_double(sp[i++]));
								reg.setNO2_RevisedSign(to_int(sp[i++]));
								reg.setNOX_ZeroPoint(to_double(sp[i++]));
								reg.setNOX_Slope(to_double(sp[i++]));
								reg.setNOX_CalibrationDensity(to_double(sp[i++]));
								reg.setNOX_RevisedSign(to_int(sp[i++]));
								
								reg.setCO_DeviceSN(to_string(sp[i++]));
								reg.setCO_DeviceStateCode(to_string(sp[i++]));
								reg.setCO_Density(to_double(sp[i++]));
								reg.setCO_DeviceInnerTemperature(to_double(sp[i++]));
								reg.setCO_DeviceChamberTemperature(to_double(sp[i++]));
								reg.setCO_DevicePressure(to_double(sp[i++]));
								reg.setCO_DeviceGasFlow(to_double(sp[i++]));
								reg.setCO_DeviceBiasVoltage(to_double(sp[i++]));
								reg.setCO_DeviceMotorSpeed(to_double(sp[i++]));
								reg.setCO_DeviceSRRatio(to_double(sp[i++]));
								reg.setCO_LightIntensity(to_double(sp[i++]));
								reg.setCO_DeviceExternalAlarm(to_int(sp[i++]));
								reg.setCO_DeviceZeroPoint(to_double(sp[i++]));
								reg.setCO_DeviceSlope(to_double(sp[i++]));
								reg.setCO_DeviceCalibrationDensity(to_double(sp[i++]));
								reg.setCO_DeviceRevisedSign(to_int(sp[i++]));
								
								reg.setO3_DeviceSN(to_string(sp[i++]));
								reg.setO3_DeviceStateCode(to_string(sp[i++]));
								reg.setO3_Density(to_double(sp[i++]));
								reg.setO3_DeviceALightIntensity(to_int(sp[i++]));
								reg.setO3_DeviceBLightIntensity(to_int(sp[i++]));
								reg.setO3_DeviceANoise(to_double(sp[i++]));
								reg.setO3_DeviceBNoise(to_double(sp[i++]));
								reg.setO3_DeviceAFlowRate(to_double(sp[i++]));
								reg.setO3_DeviceBFlowRate(to_double(sp[i++]));
								reg.setO3_DevicePressure(to_double(sp[i++]));
								reg.setO3_DeviceLightSeatTemperature(to_double(sp[i++]));
								reg.setO3_DeviceLightTemperature(to_double(sp[i++]));
								reg.setO3_DeviceExternalAlarm(to_int(sp[i++]));
								reg.setO3_DeviceZeroPoint(to_double(sp[i++]));
								reg.setO3_DeviceSlope(to_double(sp[i++]));
								reg.setO3_DeviceCalibrationDensity(to_double(sp[i++]));
								reg.setO3_DeviceRevisedSign(to_int(sp[i++]));
								
								parseResult.put(reg);
								parseResult.setSuccess(true);
								parseResult.put(new ReportInfo<REG>(reg, lineTxt));
								
							}
							else{
								ReportError re = new ReportError();
								re.setMessage("Number of elements error or DataTime format error!");
								re.setSegment(lineTxt);
								parseResult.put(re);
							}
							lineTxt = bufferedReader.readLine();
						}
					}
					else if(sp.length == 35){
						while(lineTxt != null){
							lineTxt = lineTxt.trim();
							sp = lineTxt.split(",");
							if(sp.length == 35 && sp[4].length() == 4 && isDigital(sp[4])){
								REG reg = new REG();
								reg.setStationNumberChina(sp[0]);
								reg.setItemCode(to_int(sp[1]));
								reg.setYear(to_int(sp[2]));
								reg.setDayOfYear(to_int(sp[3]));
								reg.setHhmm(to_int(sp[4]));
								String dt = DateUtil.convertJulianDay2Date(reg.getYear(), reg.getDayOfYear()); // 格式为：yyyyMMdd
								int month = Integer.parseInt(dt.substring(4, 6));
								int day = Integer.parseInt(dt.substring(6, 8));
								Calendar calendar = Calendar.getInstance();
								int hour = Integer.parseInt(sp[4].substring(0, 2));
								int min = Integer.parseInt(sp[4].substring(2, 4));
								calendar.set(reg.getYear(), month - 1, day, hour, min, 0);
								calendar.set(Calendar.MILLISECOND, 0);
								Date date = calendar.getTime(); 
								reg.setObservationTime(date);
								
								//2019-7-16 cuihongyuan
								if(!TimeCheckUtil.checkTime(reg.getObservationTime())){
									ReportError reportError = new ReportError();
									reportError.setMessage("time check error!");
									reportError.setSegment(lineTxt);
									parseResult.put(reportError);
									lineTxt = bufferedReader.readLine();
									continue;
								}
								
								
								reg.setSO2_5minAvgDensity(to_double(sp[5]));
								reg.setSO2_DataSign(sp[6]);
								reg.setNO_5minAvgDensity(to_double(sp[7]));
								reg.setNO_DataSign(sp[8]);
								reg.setNO2_5minAvgDensity(to_double(sp[9]));
								reg.setNO2_DataSign(sp[10]);
								reg.setNOX_5minAvgDensity(to_double(sp[11]));
								reg.setNOX_DataSign(sp[12]);
								reg.setCO_5minAvgDensity(to_double(sp[13]));
								reg.setCO_DataSign(sp[14]);
								reg.setO3_5minAvgDensity(to_double(sp[15]));
								reg.setO3_DataSign(sp[16]);
								
								reg.setSO2_5minMaxDensity(to_double(sp[17]));
								reg.setNO_5minMaxDensity(to_double(sp[18]));
								reg.setNO2_5minMaxDensity(to_double(sp[19]));
								reg.setNOX_5minMaxDensity(to_double(sp[20]));
								reg.setCO_5minMaxDensity(to_double(sp[21]));
								reg.setO3_5minMaxDensity(to_double(sp[22]));
								
								reg.setSO2_5minMinDensity(to_double(sp[23]));
								reg.setNO_5minMinDensity(to_double(sp[24]));
								reg.setNO2_5minMinDensity(to_double(sp[25]));
								reg.setNOX_5minMinDensity(to_double(sp[26]));
								reg.setCO_5minMinDensity(to_double(sp[27]));
								reg.setO3_5minMinDensity(to_double(sp[28]));
								
								reg.setSO2_5minDensitySTDEV(to_double(sp[29]));
								reg.setNO_5minDensitySTDEV(to_double(sp[30]));
								reg.setNO2_5minDensitySTDEV(to_double(sp[31]));
								reg.setNOX_5minDensitySTDEV(to_double(sp[32]));
								reg.setCO_5minDensitySTDEV(to_double(sp[33]));
								reg.setO3_5minDensitySTDEV(to_double(sp[34]));
								parseResult.put(reg);
								parseResult.setSuccess(true);
								parseResult.put(new ReportInfo<REG>(reg, lineTxt));
							}
							else{
								ReportError re = new ReportError();
								re.setMessage("Number of elements error or DataTime format error!");
								re.setSegment(lineTxt);
								parseResult.put(re);
							}
							lineTxt = bufferedReader.readLine();
						}
					}
					else if(sp.length == 26){
						while(lineTxt != null){
							lineTxt = lineTxt.trim();
							sp = lineTxt.split(",");
							if(sp.length == 26 && sp[4].length() == 4 && isDigital(sp[4])){
								REG reg = new REG();
								reg.setStationNumberChina(sp[0]);
								reg.setItemCode(to_int(sp[1]));
								reg.setYear(to_int(sp[2]));
								reg.setDayOfYear(to_int(sp[3]));
								reg.setHhmm(to_int(sp[4]));
								String dt = DateUtil.convertJulianDay2Date(reg.getYear(), reg.getDayOfYear()); // 格式为：yyyyMMdd
								int month = Integer.parseInt(dt.substring(4, 6));
								int day = Integer.parseInt(dt.substring(6, 8));
								Calendar calendar = Calendar.getInstance();
								int hour = Integer.parseInt(sp[4].substring(0, 2));
								int min = Integer.parseInt(sp[4].substring(2, 4));
								calendar.set(reg.getYear(), month - 1, day, hour, min, 0);
								calendar.set(Calendar.MILLISECOND, 0);
								Date date = calendar.getTime(); 
								reg.setObservationTime(date);
								
								//2019-7-16 cuihongyuan
								if(!TimeCheckUtil.checkTime(reg.getObservationTime())){
									ReportError reportError = new ReportError();
									reportError.setMessage("time check error!");
									reportError.setSegment(lineTxt);
									parseResult.put(reportError);
									lineTxt = bufferedReader.readLine();
									continue;
								}
								
								
								reg.setChannel_1(to_int(sp[5]));
								reg.setNO_Density(to_double(sp[6]));
								reg.setMeasureCode_1(to_int(sp[7]));
								
								reg.setChannel_2(to_int(sp[8]));
								reg.setNO2_Density(to_double(sp[9]));
								reg.setMeasureCode_2(to_int(sp[10]));
								
								reg.setChannel_3(to_int(sp[11]));
								reg.setNOX_Density(to_double(sp[12]));
								reg.setMeasureCode_3(to_int(sp[13]));
								
								reg.setChannel_4(to_int(sp[14]));
								reg.setSO2_Density(to_double(sp[15]));
								reg.setMeasureCode_4(to_int(sp[16]));
								
								reg.setChannel_5(to_int(sp[17]));
								reg.setCO_Density(to_double(sp[18]));
								reg.setMeasureCode_5(to_int(sp[19]));
								
								reg.setChannel_6(to_int(sp[20]));
								reg.setO3_Density(to_double(sp[21]));
								reg.setMeasureCode_6(to_int(sp[22]));
								
								reg.setChannel_7(to_int(sp[23]));
								reg.setInnerTemperature(to_double(sp[24]));
								reg.setMeasureCode_7(to_int(sp[25]));
								parseResult.put(reg);
								parseResult.setSuccess(true);
								parseResult.put(new ReportInfo<REG>(reg, lineTxt));
							}
							else{
								ReportError re = new ReportError();
								re.setMessage("Number of elements error or DataTime format error!");
								re.setSegment(lineTxt);
								parseResult.put(re);
							}
							lineTxt = bufferedReader.readLine();
						}
					}
					else{
						ReportError re = new ReportError();
						re.setMessage("Report format error!");
						re.setSegment(lineTxt);
						parseResult.put(re);
					}
				}
			}
			catch (UnsupportedEncodingException e) {
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
					if(bufferedReader != null)
						bufferedReader.close();
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
	 * 浮点要素解码   
	 * @param string 待解码字符串
	 * @return double      解码结果
	 */
	public double to_double(String string){
		double ret = 999999;
		if(string.startsWith("-999.9"))
			return ret;
		try{
			ret = Double.parseDouble(string);
		}catch (Exception e) {
			// TODO: handle exception
		}
		return ret;
	}
	/**
	 * 整型要素解码   
	 * @param string 待解码字符串
	 * @return int      解码结果
	 */
	public int to_int(String string){
		int ret = 999999;
		if(string.startsWith("-999.9"))
			return ret;
		try{
			ret = Integer.parseInt(string);
		}catch (Exception e) {
			// TODO: handle exception
		}
		return ret;
	}
	/**
	 * 字符串类型要素解码  
	 * @param string 待解码字符串
	 * @return String      解码结果
	 */
	public String to_string(String string){
		String ret = string;
		if(string.startsWith("-999.9"))
			ret = "999999";
		return ret;
	}
	/**
	 * 判断字符串是否均为数字组成   
	 * @param str 待判断字符串     
	 * @return: boolean      true 或者 false
	 */
	boolean isDigital(String str){
		int i = 0;
		for(i = 0; i < str.length(); i ++){
			if(str.charAt(i) > '9' || str.charAt(i) < '0')
				break;
		}
		if(i == str.length())
			return true;
		else return false;
	}
	
	public static void main(String[] args) {
		DecodeREG decodeREG = new DecodeREG();
		ParseResult<REG> parseResult = decodeREG.DecodeFile(new File("D:\\HUAXIN\\Z_CAWN_I_51058_20190625140000_O_REG-FLD-MUL.TXT"));
		List<REG> list = parseResult.getData();
		System.out.println(list.size());
		System.out.println(list.get(0).getStationNumberChina());
	}
	
}