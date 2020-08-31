package cma.cimiss2.dpc.indb.cawn.dc_cawn_pm25.service;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.cawn.AtmosphericAerosolPmmul;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.sand.ReportInfoService;

/**
 * <br>
 * @Title:  DbService.java   
 * @Package cma.cimiss2.dpc.indb.cawn.pm25.service   
 * @Description:    TODO(用一句话描述该文件做什么)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年5月28日 上午9:23:00   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 *
 *
 */
public class DbService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	public static String cts_code=StartConfig.ctsCode();
	public static String sod_code=StartConfig.sodCode();
	public static String report_sod_code=StartConfig.reportSodCode();
	public static String v_tt="PM25";
	public int defaultInt = 999999;
		
//	static Map<String, Object> proMap = StationInfo.getProMap();	
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}
	/**
	 * 
	 * @Title: processSuccessReport
	 * @Description:(大气成分气溶胶PM25质量浓度（PM25）报文解码入库函数)
	 * @param parseResult 解码结果集
	 * @param fileN 文件对象
	 * @param recv_time 接收时间
	 * @param loggerBuffer 日志对象
	 * @param typeOfStorageInstrument 
	 * @return DataBaseAction 入库状态
	 * @throws：
	 */

	@SuppressWarnings("rawtypes")
	public static DataBaseAction processSuccessReport(ParseResult<AtmosphericAerosolPmmul> parseResult, String fileN,
			Date recv_time, StringBuffer loggerBuffer, String typeOfStorageInstrument, String filepath) {
		java.sql.Connection connection = null;
		java.sql.Connection reportConnection = null;
		try {
			// 获取数据库连接
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			// 获取解码结果集
			List<AtmosphericAerosolPmmul> list = parseResult.getData();
			insert_db(list, connection, recv_time, loggerBuffer,typeOfStorageInstrument,fileN,filepath);
			
			List<ReportInfo> reportInfos = parseResult.getReports();
			reportConnection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
			ReportInfoService.reportInfoToDb(reportInfos, reportConnection, "000", recv_time, "9999", v_tt, report_sod_code,cts_code, "16", loggerBuffer,filepath,listDi);
			return DataBaseAction.SUCCESS;
			
		} catch (Exception e) {
			loggerBuffer.append("\n Database connection error!");
			return DataBaseAction.CONNECTION_ERROR;
		}
		finally{
			for (int j = 0; j < listDi.size(); j++) {
				diQueues.offer(listDi.get(j));
			}
			listDi.clear();
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n Close database connection error: "+e.getMessage());
				}
			}
			if(reportConnection != null) {
				try {
					reportConnection.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n Close database connection error: 常"+e.getMessage());
				}
			}
			
		}
	}
	/**
	 * 
	 * @Title: insert_db
	 * @Description:(大气成分气溶胶PM25质量浓度（PM25）资料批量入库函数)
	 * @param list 待入库对象
	 * @param connection 数据库连接
	 * @param recv_time 接收时间
	 * @param loggerBuffer 日志对象
	 * @param typeOfStorageInstrument 
	 * @param fileN 
	 * @throws：
	 */
	private static void insert_db(List<AtmosphericAerosolPmmul> list, java.sql.Connection connection, Date recv_time,
			StringBuffer loggerBuffer, String typeOfStorageInstrument, String fileN, String filepath) {
		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement pStatement = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");	
		String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_RECORD_ID,D_DATA_ID, D_IYMDHM, D_RYMDHM, D_UPDATE_TIME,"
				+ " D_DATETIME, V01301, V01300,V05001, V06001, V07001, V04001, V04002, V04003, V04004, V04005, "
				+ "V02850, V_ITEM_CODE, V_STORAGE_PLACE, V_WEIGHT_FACTOR, V_ERROR_CODE, V15752, V15765, "
				+ "V_CORRECT_COUNT, V10004_040, V13003_040, V12001_040, V_TIME_SPACING, V11002_071, "
				+ "V11001_071, V13011_071, V12001_072, V13003_072, V10004_072, V12001_073, V13003_073, "
				+ "V10004_073, V11450, V11451, V02440, V10004, V13003, V12001, V11002, V11001, V13011, "
				+ "V15471, V15472, V15473, V01324_P10, V01324_P25, V01324_P01, V15730_P10, V12001_P10,"
				+ " V10004_P10, V13003_P10, V15730_P25, V12001_P25, V10004_P25, V13003_P25, V15730_P01,"
				+ " V12001_P01, V10004_P01, V13003_P01, V_BBB, D_SOURCE_ID) "
				+ "  VALUES  (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		if(connection != null){		
			try {	
				pStatement = new LoggableStatement(connection, sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				connection.setAutoCommit(false);
				List<String> sqls = new ArrayList<>();
				for(int i = 0; i<list.size();i++){
					AtmosphericAerosolPmmul pm25 = list.get(i);
					StatDi di = new StatDi();					
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(sod_code);
					di.setDATA_TYPE_1(cts_code);
					di.setTT(v_tt);			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1");  //1成功，0失败	
					
					Date d_datetime = pm25.getD_datetime(); // 资料时间
					String stationNumberC = pm25.getStationNumberChina(); // 字符站号
					String stationNumberN = StationCodeUtil.stringToAscii(stationNumberC);

					Double latitude = pm25.getLatitude(); // 纬度
					Double longitude = pm25.getLongitude(); // 纬度
					Double height =pm25.getStationHeight();//测站海拔高度
					
					// 根据站号查询行政区划代码
					String info = (String) proMap.get(stationNumberC + "+16");
					if (info == null) {
						// 如果此站号不存在，执行下一个数据
						loggerBuffer.append("\n In configuration file, this station does not exist: " + stationNumberC);
					} else {
						String[] infos = info.split(",");
						if(!infos[2].equals("null")){
							try{
								latitude = Double.parseDouble(infos[2]);// 经度
								pm25.setLatitude(latitude);
							}catch (Exception e) {
								loggerBuffer.append("\n In configuration file, " + "get latitude of " + stationNumberC + " error!");
							}
						}
						if(!infos[1].equals("null")){
							try{
								longitude = Double.parseDouble(infos[1]); // 纬度
								pm25.setLongitude(longitude);
							}catch (Exception e) {
								loggerBuffer.append("\n In configuration file, " + "get longititude of " + stationNumberC + " error!");
							}
						}
						if(!infos[3].equals("null")){
							try{
								height = Double.parseDouble(infos[3]);//测站高度
								pm25.setStationHeight(height);
							}catch (Exception e) {
								loggerBuffer.append("\n In configuration file, " + "get station height of " + stationNumberC + " error!");
							}
						}
					}
				
					String projectCode = pm25.getProjectCode();
					// storageLocation : 存储位置
					Double storageLocation = pm25.getStorageLocation();
					// weightFactor : 重量因数
					Double weightFactor = pm25.getWeightFactor();
					// errorCode : 错误代码
					Double errorCode = pm25.getErrorCode();
					// batteryVoltageCode : 电池电压代码
					Double batteryVoltageCode = pm25.getBatteryVoltageCode();
					// valveCurrent : 阀电流
					Double valveCurrent = pm25.getValveCurrent();
					// comprehensiveCorrectionCount : 综合订正计数
					Double comprehensiveCorrectionCount = pm25.getComprehensiveCorrectionCount();
					// barometricCount : 气压计数
					Double barometricCount = pm25.getBarometricCount();
					// humidityCount : 湿度计数
					Double humidityCount = pm25.getHumidityCount();
					// temperatureCount : 温度计数
					Double temperatureCount = pm25.getTemperatureCount();
					// timeInterval : 时间间隔
					Double timeInterval = pm25.getTimeInterval();
					// windSpeedMeasurementFactor : 风速计量因子
					Double windSpeedMeasurementFactor = pm25.getWindSpeedMeasurementFactor();
					// windDirectionMeasurementFactor : 风向计量因子
					Double windDirectionMeasurementFactor = pm25.getWindDirectionMeasurementFactor();
					// precipitationMeasurementFactor : 降水计量因子
					Double precipitationMeasurementFactor = pm25.getPrecipitationMeasurementFactor();
					// temperatureLinearSlopeRate : 温度线性转化后斜率
					Double temperatureLinearSlopeRate = pm25.getTemperatureLinearSlopeRate();
					// humidityLinearSlopeRate : 湿度线性转化后斜率
					Double humidityLinearSlopeRate = pm25.getHumidityLinearSlopeRate();
					// pressureLinearSlopeRate : 气压线性转化后斜率
					Double pressureLinearSlopeRate = pm25.getPressureLinearSlopeRate();
					// temperatureLinearIntercept : 温度线性转化后截距
					Double temperatureLinearIntercept = pm25.getTemperatureLinearIntercept();
					// humidityLinearIntercept : 湿度线性转化后截距
					Double humidityLinearIntercept = pm25.getHumidityLinearIntercept();
					// pressureLinearIntercept : 气压线性转化后截距
					Double pressureLinearIntercept = pm25.getPressureLinearIntercept();
					// windSpeedSensitivity : 风速灵敏度
					Double windSpeedSensitivity = pm25.getWindSpeedSensitivity();
					// windDirectionAngle : 风向倾角
					Double windDirectionAngle = pm25.getWindDirectionAngle();
					// precipitationSensorCorrectionFactor : 降水传感器订正因子
					Double precipitationSensorCorrectionFactor = pm25.getPrecipitationSensorCorrectionFactor();
					// pressure : 气压
					Double pressure = pm25.getPressure();
					// relativeHumidity : 相对湿度
					Double relativeHumidity = pm25.getRelativeHumidity();
					// temperature : 温度
					Double temperature = pm25.getTemperature();
					// windSpeed : 风速
					Double windSpeed = pm25.getWindSpeed();
					// windDirection : 风向
					Double windDirection = pm25.getWindDirection();
					// precipitation : 降水
					Double precipitation = pm25.getPrecipitation();
					// massConcentrationOfPM10 : PM10质量浓度
					Double massConcentrationOfPM10 = pm25.getMassConcentrationOfPM10();
					// massConcentrationOfPM25 : PM2.5质量浓度
					Double massConcentrationOfPM2p5 = pm25.getMassConcentrationOfPM2p5();
					// massConcentrationOfPM1 : PM1质量浓度
					Double massConcentrationOfPM1 = pm25.getMassConcentrationOfPM1();
					// measurementDataFlag_pm10 : PM10测量数据标识
					String measurementDataFlag_pm10 = pm25.getMeasurementDataFlag_pm10();
					// measurementDataFlag_pm2p5 : PM2.5测量数据标识
					String measurementDataFlag_pm2p5 = pm25.getMeasurementDataFlag_pm2p5();
					// measurementDataFlag_pm1 : PM1测量数据标识
					String measurementDataFlag_pm1 = pm25.getMeasurementDataFlag_pm1();
					// flowRate_pm10 : PM10仪器测量的流量
					Double flowRate_pm10 = pm25.getFlowRate_pm10();
					// temperature_pm10 : PM10仪器测量的气温
					Double temperature_pm10 = pm25.getTemperature_pm10();
					// pressure_pm10 : PM10仪器测量的气压
					Double pressure_pm10 = pm25.getPressure_pm10();
					// relativeHumidity_pm10 : PM10仪器测量的相对湿度
					Double relativeHumidity_pm10 = pm25.getRelativeHumidity_pm10();
					// flowRate_pm2p5 : PM2.5仪器测量的流量
					Double flowRate_pm2p5 = pm25.getFlowRate_pm2p5();
					// temperature_pm2p5 : PM2.5仪器测量的气温
					Double temperature_pm2p5 = pm25.getTemperature_pm2p5();
					// pressure_pm2p5 : PM2.5仪器测量的气压
					Double pressure_pm2p5 = pm25.getPressure_pm2p5();
					// relativeHumidity_pm2p5 : PM2.5仪器测量的相对湿度
					Double relativeHumidity_pm2p5 = pm25.getRelativeHumidity_pm2p5();
					// flowRate_pm1 : PM1.0仪器测量的流量
					Double flowRate_pm1 = pm25.getFlowRate_pm1();
					// temperature_pm1 : PM1.0仪器测量的气温
					Double temperature_pm1 = pm25.getTemperature_pm1();
					// pressure_pm1 : PM1.0仪器测量的气压
					Double pressure_pm1 = pm25.getPressure_pm1();
					// relativeHumidity_pm1 : PM1.0仪器测量的相对湿度
					Double relativeHumidity_pm1 = pm25.getRelativeHumidity_pm1();
					
					int num = 1;
					// 入库时间
					Timestamp insertTime = new Timestamp(new Date().getTime());
					// 资料时间
					Timestamp dataTime = new Timestamp(d_datetime.getTime());

					String primary_key = TimeUtil.date2String(d_datetime, TimeUtil.DATE_FMT_YMDHMS) + "_" +stationNumberC + "_" + typeOfStorageInstrument;
					
					pStatement.setString(num++, primary_key);// 记录标识
					pStatement.setString(num++, sod_code);// 资料标识
					pStatement.setTimestamp(num++, insertTime);// 入库时间
					pStatement.setTimestamp(num++, new Timestamp(recv_time.getTime()));// 收到时间
					pStatement.setTimestamp(num++, insertTime);// 更新时间
					pStatement.setTimestamp(num++, dataTime);// 资料时间
					pStatement.setString(num++, stationNumberC);// 区站号(字符)
					pStatement.setBigDecimal(num++, new BigDecimal(stationNumberN));// 区站号(数字)
					pStatement.setBigDecimal(num++, new BigDecimal(latitude).setScale(4, BigDecimal.ROUND_HALF_UP));// 纬度
					pStatement.setBigDecimal(num++, new BigDecimal(longitude).setScale(4, BigDecimal.ROUND_HALF_UP));// 经度
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(height)));// 测站高度
					pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getYear(d_datetime)));// 资料观测年
					pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getMonth(d_datetime)));// 资料观测月
					pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getDayOfMonth(d_datetime)));// 资料观测日
					pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getHourOfDay(d_datetime)));// 资料观测时
					pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getMinute(d_datetime)));// 资料观测分
					pStatement.setString(num++, typeOfStorageInstrument);// 存储仪器类型
					pStatement.setString(num++, projectCode);
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(storageLocation)));// 存储位置
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(weightFactor)));// 重量因数
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(errorCode)));// 错误代码
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(batteryVoltageCode)));// 电池电压代码
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(valveCurrent)));// 阀电流
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(comprehensiveCorrectionCount)));// 综合订正计数
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(barometricCount)));// 气压计数
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(humidityCount)));// 湿度计数
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(temperatureCount)));// 温度计数
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(timeInterval)));// 时间间隔
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(windSpeedMeasurementFactor)));// 风速计量因子
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(windDirectionMeasurementFactor)));// 风向计量因子
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(precipitationMeasurementFactor)));// 降水计量因子
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(temperatureLinearSlopeRate)));// 温度线性转化后斜率
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(humidityLinearSlopeRate)));// 湿度线性转化后斜率
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(pressureLinearSlopeRate)));// 气压线性转化后斜率
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(temperatureLinearIntercept)));// 温度线性转化后截距
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(humidityLinearIntercept)));// 湿度线性转化后截距
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(pressureLinearIntercept)));// 气压线性转化后截距
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(windSpeedSensitivity)));// 风速灵敏度
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(windDirectionAngle)));// 风向倾角
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(precipitationSensorCorrectionFactor)));// 降水传感器订正因子
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(pressure)));// 气压
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(relativeHumidity)));// 相对湿度
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(temperature)));// 温度
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(windSpeed)));// 风速
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(windDirection)));// 风向
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(precipitation)));// 降水
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(massConcentrationOfPM10)));// PM10质量浓度
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(massConcentrationOfPM2p5)));// PM2.5质量浓度
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(massConcentrationOfPM1)));// PM1质量浓度
					pStatement.setString(num++, measurementDataFlag_pm10);// PM10测量数据标识
					pStatement.setString(num++, measurementDataFlag_pm2p5);// PM2.5测量数据标识
					pStatement.setString(num++, measurementDataFlag_pm1);// PM1测量数据标识
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(flowRate_pm10)));// PM10仪器测量的流量
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(temperature_pm10)));// PM10仪器测量的气温
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(pressure_pm10)));// PM10仪器测量的气压
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(relativeHumidity_pm10)));// PM10仪器测量的相对湿度
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(flowRate_pm2p5)));// PM2.5仪器测量的流量
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(temperature_pm2p5)));// PM2.5仪器测量的气温
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(pressure_pm2p5)));// PM2.5仪器测量的气压
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(relativeHumidity_pm2p5)));// PM2.5仪器测量的相对湿度
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(flowRate_pm1)));// PM1.0仪器测量的流量
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(temperature_pm1)));// PM1.0仪器测量的气温
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(pressure_pm1)));// PM1.0仪器测量的气压
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(relativeHumidity_pm1)));// PM1.0仪器测量的相对湿度
					pStatement.setString(num++, "000");
					pStatement.setString(num++, cts_code);
					
					di.setIIiii(list.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(d_datetime, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(latitude));
					di.setLONGTITUDE(String.valueOf(longitude));
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
					di.setDATA_UPDATE_FLAG("000");
					di.setHEIGHT(String.valueOf(height));
					
					// 批量提交
					pStatement.addBatch();
					sqls.add(((LoggableStatement)pStatement).getQueryString());
					listDi.add(di);				
				} // end for
				try{
					pStatement.executeBatch();
					connection.commit();
					loggerBuffer.append(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
					loggerBuffer.append(" INSERT SUCCESS COUNT ：" + list.size() + "\n");
					sqls.clear();
				}catch(SQLException e){
					pStatement.clearParameters();
					pStatement.clearBatch();
					loggerBuffer.append(" Batch ERROR : " + sdf.format(new Date()) + "\n");
					execute_sql(sqls, connection,loggerBuffer); // 此中，入库失败的会将对应的DI的ProcessState置为1 // 1:失败，0：成功
				}
			}catch (SQLException e) {
				loggerBuffer.append("\n Create Statement error: "+e.getMessage());
			}finally {
				if(pStatement != null) {
					try {
						pStatement.close();
					} catch (SQLException e) {
						loggerBuffer.append("\n Close Statement error: "+e.getMessage());
					}
				}
			}
		} 
	}
	/**
	 * 
	 * @Title: execute_sql
	 * @Description:(批量插入失败时，单条插入函数)
	 * @param sqls sql对象
	 * @param connection 数据库连接
	 * @param loggerBuffer 日志对象
	 * @throws：
	 */
	private static void execute_sql(List<String> sqls, java.sql.Connection connection, StringBuffer loggerBuffer) {
		Statement pStatement = null;
		loggerBuffer.append("\n Start insert one by one.");
		try {
			connection.setAutoCommit(true);
			pStatement = connection.createStatement();
			for (int i = 0; i < sqls.size(); i++) {
//				pStatement = new LoggableStatement(connection, sqls.get(i));
				try {
					pStatement.execute(sqls.get(i));
					loggerBuffer.append(" " + listDi.get(i).getIIiii() + " " + listDi.get(i).getDATA_TIME() +" 000\n");
				} catch (Exception e) {
					// 单行插入失败
					loggerBuffer.append("\n " + listDi.get(i).getIIiii() + " " + listDi.get(i).getDATA_TIME()
							+"\n execute sql error: "+sqls.get(i)+"\n "+e.getMessage());
					listDi.get(i).setPROCESS_STATE("0");
				}
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n Create Statement error: "+e.getMessage());
		}finally {
			if(pStatement != null) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n Close Statement error: "+e.getMessage());
				}
			}
		}		
	}
}
