package cma.cimiss2.dpc.indb.cawn.dc_cawn_pmmul.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.ots.OTSBatchResult;
import org.cimiss2.dwp.tools.ots.OTSDbHelper;
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alicloud.openservices.tablestore.ClientException;

import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
import cma.cimiss2.dpc.decoder.bean.cawn.AtmosphericAerosolPmmul;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.cawn.dc_cawn_pmmul.ReadIni;

public class OTSService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static BlockingQueue<StatDi> diQueues;
//	static Map<String, Object> proMap = StationInfo.getProMap();
	 
	public static String cts_code=StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}
	public static DataBaseAction insert_ots(List<AtmosphericAerosolPmmul> aaps, String tablename, Date recv_time, StringBuffer loggerBuffer, String fileN, String typeOfStorageInstrument) {
		Map<String, Object> proMap = StationInfo.getProMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");	
		StatDi di = null;
		if(aaps != null && aaps.size() > 0){
			int successRowCount = aaps.size();
			ReadIni ini = ReadIni.getIni();
			String d_data_id = ini.getValue(ReadIni.SECTION_PMMUL, ReadIni.D_DATA_ID_KEY);
			for(AtmosphericAerosolPmmul aap : aaps){
				Map<String, Object> row = new HashMap<String, Object>();
				Date d_datetime = aap.getD_datetime(); // 资料时间
				String stationNumberC = aap.getStationNumberChina(); // 字符站号
				String stationNumberN = StationCodeUtil.stringToAscii(stationNumberC);
				Double latitude = aap.getLatitude(); // 纬度
				Double longitude = aap.getLongitude(); // 纬度
				Double height = aap.getStationHeight(); // 测站海拔高度
				// 根根配置文件  获取站点基本信息
				String info = (String) proMap.get(aap.getStationNumberChina() + "+16");
			
				if (info == null){
					info = (String) proMap.get(aap.getStationNumberChina() + "+15");
				}
				if (info==null) {
					loggerBuffer.append("\n  In configuration file, this station does not exist: " + aap.getStationNumberChina());	
				}else{
					String[] infos = info.split(",");
					if(!infos[2].equals("null")){
						try{
							latitude = Double.parseDouble(infos[2]);// 经度
							aap.setLatitude(latitude);
						}catch (Exception e) {
							loggerBuffer.append("\n In configuration file, " + "get latitude of " + stationNumberC + " error!");
						}
					}
					if(!infos[1].equals("null")){
						try{
							longitude = Double.parseDouble(infos[1]); // 纬度
							aap.setLongitude(longitude);
						}catch (Exception e) {
							loggerBuffer.append("\n In configuration file, " + "get longititude of " + stationNumberC + " error!");
						}
					}
					if(!infos[3].equals("null")){
						try{
							height = Double.parseDouble(infos[3]);//测站高度
							aap.setStationHeight(height);
						}catch (Exception e) {
							loggerBuffer.append("\n In configuration file, " + "get station height of " + stationNumberC + " error!");
						}
					}
				}
				/*if (stationNumberC.contains("52866")) {
					System.out.println(TimeUtil.date2String(d_datetime,"yyyy-MM-dd HH:mm:ss"));
					System.out.println(fileN);
					
				}*/
				String lengthStr = "00000";
				// 时间(yyyyMMddHHmmss)_微秒（毫秒转微秒）SSSSSS_站号5_经度10_纬度9_海拔或高度相关8_(V_BBB)_其他字段
				String id = TimeUtil.date2String(d_datetime, TimeUtil.DATE_FMT_YMDHMS) //
						+ "_" + stationNumberC //
						+ "_" + typeOfStorageInstrument;
				// stationHeight : 测站高度
				Double stationHeight = aap.getStationHeight();
				// typeOfStorageInstrument : 存储仪器类型
				// String typeOfStorageInstrument = aap.getTypeOfStorageInstrument();
				// projectCode : 项目代码
				String projectCode = aap.getProjectCode();
				// storageLocation : 存储位置
				Double storageLocation = aap.getStorageLocation();
				// weightFactor : 重量因数
				Double weightFactor = aap.getWeightFactor();
				// errorCode : 错误代码
				Double errorCode = aap.getErrorCode();
				// batteryVoltageCode : 电池电压代码
				Double batteryVoltageCode = aap.getBatteryVoltageCode();
				// valveCurrent : 阀电流
				Double valveCurrent = aap.getValveCurrent();
				// comprehensiveCorrectionCount : 综合订正计数
				Double comprehensiveCorrectionCount = aap.getComprehensiveCorrectionCount();
				// barometricCount : 气压计数
				Double barometricCount = aap.getBarometricCount();
				// humidityCount : 湿度计数
				Double humidityCount = aap.getHumidityCount();
				// temperatureCount : 温度计数
				Double temperatureCount = aap.getTemperatureCount();
				// timeInterval : 时间间隔
				Double timeInterval = aap.getTimeInterval();
				// windSpeedMeasurementFactor : 风速计量因子
				Double windSpeedMeasurementFactor = aap.getWindSpeedMeasurementFactor();
				// windDirectionMeasurementFactor : 风向计量因子
				Double windDirectionMeasurementFactor = aap.getWindDirectionMeasurementFactor();
				// precipitationMeasurementFactor : 降水计量因子
				Double precipitationMeasurementFactor = aap.getPrecipitationMeasurementFactor();
				// temperatureLinearSlopeRate : 温度线性转化后斜率
				Double temperatureLinearSlopeRate = aap.getTemperatureLinearSlopeRate();
				// humidityLinearSlopeRate : 湿度线性转化后斜率
				Double humidityLinearSlopeRate = aap.getHumidityLinearSlopeRate();
				// pressureLinearSlopeRate : 气压线性转化后斜率
				Double pressureLinearSlopeRate = aap.getPressureLinearSlopeRate();
				// temperatureLinearIntercept : 温度线性转化后截距
				Double temperatureLinearIntercept = aap.getTemperatureLinearIntercept();
				// humidityLinearIntercept : 湿度线性转化后截距
				Double humidityLinearIntercept = aap.getHumidityLinearIntercept();
				// pressureLinearIntercept : 气压线性转化后截距
				Double pressureLinearIntercept = aap.getPressureLinearIntercept();
				// windSpeedSensitivity : 风速灵敏度
				Double windSpeedSensitivity = aap.getWindSpeedSensitivity();
				// windDirectionAngle : 风向倾角
				Double windDirectionAngle = aap.getWindDirectionAngle();
				// precipitationSensorCorrectionFactor : 降水传感器订正因子
				Double precipitationSensorCorrectionFactor = aap.getPrecipitationSensorCorrectionFactor();
				// pressure : 气压
				Double pressure = aap.getPressure();
				// relativeHumidity : 相对湿度
				Double relativeHumidity = aap.getRelativeHumidity();
				// temperature : 温度
				Double temperature = aap.getTemperature();
				// windSpeed : 风速
				Double windSpeed = aap.getWindSpeed();
				// windDirection : 风向
				Double windDirection = aap.getWindDirection();
				// precipitation : 降水
				Double precipitation = aap.getPrecipitation();
				// massConcentrationOfPM10 : PM10质量浓度
				Double massConcentrationOfPM10 = aap.getMassConcentrationOfPM10();
				// massConcentrationOfPM25 : PM2.5质量浓度
				Double massConcentrationOfPM2p5 = aap.getMassConcentrationOfPM2p5();
				// massConcentrationOfPM1 : PM1质量浓度
				Double massConcentrationOfPM1 = aap.getMassConcentrationOfPM1();
				// measurementDataFlag_pm10 : PM10测量数据标识
				String measurementDataFlag_pm10 = aap.getMeasurementDataFlag_pm10();
				// measurementDataFlag_pm2p5 : PM2.5测量数据标识
				String measurementDataFlag_pm2p5 = aap.getMeasurementDataFlag_pm2p5();
				// measurementDataFlag_pm1 : PM1测量数据标识
				String measurementDataFlag_pm1 = aap.getMeasurementDataFlag_pm1();
				// flowRate_pm10 : PM10仪器测量的流量
				Double flowRate_pm10 = aap.getFlowRate_pm10();
				// temperature_pm10 : PM10仪器测量的气温
				Double temperature_pm10 = aap.getTemperature_pm10();
				// pressure_pm10 : PM10仪器测量的气压
				Double pressure_pm10 = aap.getPressure_pm10();
				// relativeHumidity_pm10 : PM10仪器测量的相对湿度
				Double relativeHumidity_pm10 = aap.getRelativeHumidity_pm10();
				// flowRate_pm2p5 : PM2.5仪器测量的流量
				Double flowRate_pm2p5 = aap.getFlowRate_pm2p5();
				// temperature_pm2p5 : PM2.5仪器测量的气温
				Double temperature_pm2p5 = aap.getTemperature_pm2p5();
				// pressure_pm2p5 : PM2.5仪器测量的气压
				Double pressure_pm2p5 = aap.getPressure_pm2p5();
				// relativeHumidity_pm2p5 : PM2.5仪器测量的相对湿度
				Double relativeHumidity_pm2p5 = aap.getRelativeHumidity_pm2p5();
				// flowRate_pm1 : PM1.0仪器测量的流量
				Double flowRate_pm1 = aap.getFlowRate_pm1();
				// temperature_pm1 : PM1.0仪器测量的气温
				Double temperature_pm1 = aap.getTemperature_pm1();
				// pressure_pm1 : PM1.0仪器测量的气压
				Double pressure_pm1 = aap.getPressure_pm1();
				// relativeHumidity_pm1 : PM1.0仪器测量的相对湿度
				Double relativeHumidity_pm1 = aap.getRelativeHumidity_pm1();
				
				if (stationNumberC.contains("52866")) {
					System.out.println(id +    "___"+fileN);
				}
				
				//row.put("D_RECORD_ID", id);
	            row.put("D_DATA_ID", d_data_id);
	            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_DATETIME", TimeUtil.date2String(d_datetime,"yyyy-MM-dd HH:mm:ss"));
	            // `V01301`, `V01300`, `V05001`, `V06001`, `V07001`, `V04001`, `V04002`, `V04003`, `V04004`, `V04005`, 
	            row.put("V01301", stationNumberC);// 区站号(字符)
				row.put("V01300", stationNumberN);// 区站号(数字)
				row.put("V05001", latitude);// 纬度
				row.put("V06001", longitude);// 经度
				row.put("V07001", stationHeight);// 测站高度
				row.put("V04001", TimeUtil.getYear(d_datetime));// 资料观测年
				row.put("V04002", TimeUtil.getMonth(d_datetime));// 资料观测月
				row.put("V04003", TimeUtil.getDayOfMonth(d_datetime));// 资料观测日
				row.put("V04004", TimeUtil.getHourOfDay(d_datetime));// 资料观测时
				row.put("V04005", TimeUtil.getMinute(d_datetime));// 资料观测分
				//`V02850`, `V_ITEM_CODE`, `V_STORAGE_PLACE`, `V_WEIGHT_FACTOR`, `V_ERROR_CODE`, `V15752`, `V15765`, `V_CORRECT_COUNT`, `V10004_040`, `V13003_040`, 
				row.put("V02850", typeOfStorageInstrument);// 存储仪器类型
				row.put("V_ITEM_CODE", projectCode);
				row.put("V_STORAGE_PLACE", storageLocation);// 存储位置
				row.put("V_WEIGHT_FACTOR", weightFactor);// 重量因数
				row.put("V_ERROR_CODE", errorCode);// 错误代码
				row.put("V15752", batteryVoltageCode);// 电池电压代码
				row.put("V15765", valveCurrent);// 阀电流
				row.put("V_CORRECT_COUNT", comprehensiveCorrectionCount);// 综合订正计数
				row.put("V10004_040", barometricCount);// 气压计数
				row.put("V13003_040", humidityCount);// 湿度计数
				//`V12001_040`, `V_TIME_SPACING`, `V11002_071`, `V11001_071`, `V13011_071`, `V12001_072`, `V13003_072`, `V10004_072`, `V12001_073`, `V13003_073`, 
				row.put("V12001_040", temperatureCount);// 温度计数
				row.put("V_TIME_SPACING", timeInterval);// 时间间隔
				row.put("V11002_071", windSpeedMeasurementFactor);// 风速计量因子
				row.put("V11001_071", windDirectionMeasurementFactor);// 风向计量因子
				row.put("V13011_071", precipitationMeasurementFactor);// 降水计量因子
				row.put("V12001_072", temperatureLinearSlopeRate);// 温度线性转化后斜率
				row.put("V13003_072", humidityLinearSlopeRate);// 湿度线性转化后斜率
				row.put("V10004_072", pressureLinearSlopeRate);// 气压线性转化后斜率
				row.put("V12001_073", temperatureLinearIntercept);// 温度线性转化后截距
				row.put("V13003_073", humidityLinearIntercept);// 湿度线性转化后截距
				 //`V10004_073`, `V11450`, `V11451`, `V02440`, `V10004`, `V13003`, `V12001`, `V11002`, `V11001`, `V13011`, 
				row.put("V10004_073", pressureLinearIntercept);// 气压线性转化后截距
				row.put("V11450", windSpeedSensitivity);// 风速灵敏度
				row.put("V11451", windDirectionAngle);// 风向倾角
				row.put("V02440", precipitationSensorCorrectionFactor);// 降水传感器订正因子
				row.put("V10004", pressure);// 气压
				row.put("V13003", relativeHumidity);// 相对湿度
				row.put("V12001", temperature);// 温度
				row.put("V11002", windSpeed);// 风速
				row.put("V11001", windDirection);// 风向
				row.put("V13011", precipitation);// 降水
				  //`V15471`, `V15472`, `V15473`, `V01324_P10`, `V01324_P25`, `V01324_P01`, `V15730_P10`, `V12001_P10`, `V10004_P10`, `V13003_P10`, 
				row.put("V15471", massConcentrationOfPM10);// PM10质量浓度
				row.put("V15472", massConcentrationOfPM2p5);// PM2.5质量浓度
				row.put("V15473", massConcentrationOfPM1);// PM1质量浓度
				row.put("V01324_P10", measurementDataFlag_pm10);// PM10测量数据标识
				row.put("V01324_P25", measurementDataFlag_pm2p5);// PM2.5测量数据标识
				row.put("V01324_P01", measurementDataFlag_pm1);// PM1测量数据标识
				row.put("V15730_P10", flowRate_pm10);// PM10仪器测量的流量
				row.put("V12001_P10", temperature_pm10);// PM10仪器测量的气温
				row.put("V10004_P10", pressure_pm10);// PM10仪器测量的气压
				row.put("V13003_P10", relativeHumidity_pm10);// PM10仪器测量的相对湿度
				 //`V15730_P25`, `V12001_P25`, `V10004_P25`, `V13003_P25`, `V15730_P01`, `V12001_P01`, `V10004_P01`, `V13003_P01`
				row.put("V15730_P25", flowRate_pm2p5);// PM2.5仪器测量的流量
				row.put("V12001_P25", temperature_pm2p5);// PM2.5仪器测量的气温
				row.put("V10004_P25", pressure_pm2p5);// PM2.5仪器测量的气压
				row.put("V13003_P25", relativeHumidity_pm2p5);// PM2.5仪器测量的相对湿度
				row.put("V15730_P01", flowRate_pm1);// PM1.0仪器测量的流量
				row.put("V12001_P01", temperature_pm1);// PM1.0仪器测量的气温
				row.put("V10004_P01", pressure_pm1);// PM1.0仪器测量的气压
				row.put("V13003_P01", relativeHumidity_pm1);// PM1.0仪器测量的相对湿度
				row.put("V_BBB", "000");
				row.put("D_SOURCE_ID", cts_code);
				
				di = new StatDi();
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(d_data_id);
				di.setDATA_TYPE_1(cts_code);
				di.setTT("PMMUL");
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, TimeUtil.DEFAULT_DATETIME_FORMAT));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("0"); // 0成功，1失败
				di.setPROCESS_STATE("0"); // 0成功，1失败
				di.setIIiii(stationNumberC);
				di.setLATITUDE(String.valueOf(aap.getLatitude()));
				di.setLONGTITUDE(String.valueOf(aap.getLongitude()));
				di.setDATA_TIME(TimeUtil.date2String(d_datetime, TimeUtil.DEFAULT_DATETIME_FORMAT));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());
				
				try {
					OTSDbHelper.getInstance().insert(tablename, row);
					diQueues.offer(di);
				} catch (Exception e) {
					successRowCount = successRowCount -1;
					di.setPROCESS_STATE("1");
					diQueues.offer(di);
					loggerBuffer.append(row+"\n");
					loggerBuffer.append(e.getMessage()+"\n");
					if(e.getClass() == ClientException.class) {
						return DataBaseAction.CONNECTION_ERROR;
					}
				}
	        }
			loggerBuffer.append(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
		    loggerBuffer.append(" INSERT SUCCESS COUNT : " + successRowCount + "\n");
		    loggerBuffer.append(" INSERT FAILED COUNT : " + (aaps.size() - successRowCount) + "\n");
		}
		return DataBaseAction.SUCCESS;
	}
	/**
	 * 
	 * @Title: reportInfoToDb   
	 * @Description: TODO(报文信息入库)   
	 * @param  reportInfos 报文列表集合
	 * @param  connection  数据库连接
	 * @param  recv_time   资料时间
	 * @return: void      
	 * @throws:
	 */
	@SuppressWarnings({ "rawtypes" })
	public
	static void reportInfoToDb(List<ReportInfo> reportInfos, String tablename, Date recv_time, StringBuffer loggerBuffer, String v_cccc) {
		if(reportInfos != null && reportInfos.size() > 0){
			List<Map<String, Object>> batchs = new ArrayList<Map<String, Object>>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			Date oTime = new Date();
			for(int i = 0; i < reportInfos.size(); i++){
				Map<String, Object> row = new HashMap<String, Object>();
				AgmeReportHeader agmeReportHeader = (AgmeReportHeader) reportInfos.get(i).getT();		
				oTime = agmeReportHeader.getReport_time();
//				String primkey = sdf.format(agmeReportHeader.getReport_time())+"_"+agmeReportHeader.getStationNumberChina()+"_"+v_cccc;
//				row.put("D_RECORD_ID", primkey);
	            row.put("D_DATA_ID", sod_code);
	            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_DATETIME", TimeUtil.date2String(oTime, "yyyy-MM-dd HH:mm:ss"));
	           //"V_BBB,V_CCCC,V_TT,V01301,V01300,V05001,V06001,V_NCODE,V_ACODE,"
	            row.put("V_BBB", "000");
	            row.put("V_CCCC", v_cccc);
	            row.put("V_TT", "PMMUL");
	            row.put("V01301", agmeReportHeader.getStationNumberChina());
	            row.put("V01300" ,Integer.parseInt(StationCodeUtil.stringToAscii(agmeReportHeader.getStationNumberChina())));
	            row.put("V05001", agmeReportHeader.getLatitude());
	            row.put("V06001", agmeReportHeader.getLongitude());
	            row.put("V_NCODE", 2250);//V_NCODE
	            row.put("V_ACODE", StationInfo.getAdminCode(agmeReportHeader.getStationNumberChina(), "16"));// V_ACODE
	            // "V04001,V04002,V04003,V04004,V04005,"
	            row.put("V04001", oTime.getYear() + 1900);
	            row.put("V04002", oTime.getMonth() + 1);
	            row.put("V04003", oTime.getDate());
	            row.put("V04004", oTime.getHours());
	            row.put("V04005", oTime.getMinutes());
				// "V_LEN,V_REPORT)
	            row.put("V_LEN", reportInfos.get(i).getReport().length());
	            row.put("V_REPORT", reportInfos.get(i).getReport());
	            
	            batchs.add(row);
			}
			OTSBatchResult result = OTSDbHelper.getInstance().insert(tablename, batchs);
	        //System.out.println(result.getSuccessRowCount());
	        //System.out.println(result.getFailedRowCount());
	        //System.out.println(result.getFailedRows());
	        loggerBuffer.append(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
	        loggerBuffer.append(" INSERT SUCCESS COUNT : " + result.getSuccessRowCount() + "\n");
	        loggerBuffer.append(" INSERT FAILED COUNT : " + result.getFailedRowCount() + "\n");
	        loggerBuffer.append(" INSERT FAILED CONTENT : " + result.getFailedRows() + "\n");
		}
	}
}