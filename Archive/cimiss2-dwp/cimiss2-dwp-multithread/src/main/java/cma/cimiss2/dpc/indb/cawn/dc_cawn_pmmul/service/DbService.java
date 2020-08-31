package cma.cimiss2.dpc.indb.cawn.dc_cawn_pmmul.service;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;

import java.sql.SQLException;
import java.sql.Timestamp;
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

import com.alibaba.druid.pool.DruidPooledConnection;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.cawn.AtmosphericAerosolPmmul;
import cma.cimiss2.dpc.decoder.cawn.DecodePmmul;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.cawn.dc_cawn_pmmul.ReadIni;
import cma.cimiss2.dpc.indb.sand.ReportInfoService;


public class DbService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static BlockingQueue<StatDi> diQueues;
//	static Map<String, Object> proMap = StationInfo.getProMap();
	public static String cts_code=StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	public static String report_sod_code = StartConfig.reportSodCode();
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}

	/**
	 * @Title: insert_db
	 * @Description: 大气成分气溶胶PM10/PM2.5/PM1质量浓度（PMMUL）入库
	 * @param parseResult 解析结果集
	 * @param recv_time 收到时间，即文件落地时间
	 * @param typeOfStorageInstrument 
	 * @param fileName 
	 * @param loggerBuffer 
	 * @return DataBaseAction 入库结果
	 * @throws: 
	 */
	public static DataBaseAction insert_db(ParseResult<AtmosphericAerosolPmmul> parseResult, Date recv_time, String fileName, String typeOfStorageInstrument, StringBuffer loggerBuffer, String filepath) {
		// 获取数据库连接
		java.sql.Connection connection = null;
		try {
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			if (connection != null) {
				// connection.setAutoCommit(false); // 关闭自动提交
				// list: 所有数据
				List<AtmosphericAerosolPmmul> list = parseResult.getData();
				ReadIni ini = ReadIni.getIni();
				String d_data_id = ini.getValue(ReadIni.SECTION_PMMUL, ReadIni.D_DATA_ID_KEY);
				insert_data(list, connection, recv_time, sod_code,fileName,typeOfStorageInstrument, loggerBuffer, filepath);

				@SuppressWarnings("rawtypes")
				List<ReportInfo> reportInfos = parseResult.getReports();

				DruidPooledConnection cimissConnection = null;
				List<StatDi> listDi = new ArrayList<StatDi>();
				try {
					cimissConnection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
					if (cimissConnection != null) {
						ReportInfoService.reportInfoToDb(reportInfos, cimissConnection, "000", recv_time, "9999", ReadIni.SECTION_PMMUL, report_sod_code,cts_code, "16", loggerBuffer,filepath,listDi);
					} else {
						loggerBuffer.append("\n get cimiss database connection error!");
					}
				} finally {
					for (int j = 0; j < listDi.size(); j++) {
						diQueues.offer(listDi.get(j));
					}
					listDi.clear();
					
					if (cimissConnection != null) {
						try {
							cimissConnection.close();
						} catch (SQLException e) {
							loggerBuffer.append("\n Close database connection error: " + e.getMessage());
						}
					}
				}

			} else {
				loggerBuffer.append("\n Get database connection error!");
				return DataBaseAction.CONNECTION_ERROR;
			}
		} finally {
			
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n Close database connection error: " + e.getMessage());
				}
			}
		}
		return DataBaseAction.SUCCESS;
	}

	/**
	 * 
	 * @Title: insert_data
	 * @Description: 大气成分气溶胶PM10/PM2.5/PM1质量浓度（PMMUL）入库
	 * @param recv_time 收到时间
	 * @param connection 数据库连接
	 * @param typeOfStorageInstrument 
	 * @param fileName 
	 * @param loggerBuffer 
	 * @param towers 一个文件中的所有铁塔数据
	 * @return DataBaseAction 成功与否标识
	 * @throws:
	 */
	private static DataBaseAction insert_data(List<AtmosphericAerosolPmmul> aaps, java.sql.Connection connection, Date recv_time, String d_data_id, String fileName, String typeOfStorageInstrument, StringBuffer loggerBuffer, String filepath) {

		List<StatDi> listDi = new ArrayList<StatDi>();

		ReadIni ini = ReadIni.getIni();

		String insert_sql = ini.getValue(ReadIni.SECTION_PMMUL, ReadIni.INSERT_SQL_KEY);
		PreparedStatement pStatement = null;
		try {
			StatDi di = null;

			pStatement = new LoggableStatement(connection, insert_sql);
			if (StartConfig.getDatabaseType() == 1) {
				pStatement.execute("select last_txc_xid()");
			}
			for (AtmosphericAerosolPmmul aap : aaps) {
				di = new StatDi();
				// 对DI和pStatement设置值
				generatePstAndDi(di, aap, pStatement, recv_time, d_data_id,fileName,typeOfStorageInstrument, loggerBuffer, filepath);
				pStatement.addBatch();
//			((LoggableStatement)pStatement).getQueryString();
				listDi.add(di);
			}
			try {
				pStatement.executeBatch();
				connection.commit();

				for (StatDi tdi : listDi) {
					diQueues.offer(tdi);
				}

				return DataBaseAction.SUCCESS;
			} catch (SQLException e) {
				if (pStatement != null) {
					try {
						pStatement.close();
					} catch (SQLException e1) {
						loggerBuffer.append("\n Close Statement error: " + e1.getMessage());
					}
				}
				connection.setAutoCommit(true);
				pStatement = new LoggableStatement(connection, insert_sql);
				// pStatement.clearParameters();
				// pStatement.clearBatch();
				// 如果批量入库异常，执行单条数据入库
				// connection.setAutoCommit(true); //因为之前connection设置了非自动提交，转单条数据入库时，需设置回自动提交或最后手动提交
				loggerBuffer.append("\n Batch commit error: " + fileName + "\n " + e.getMessage());
				for (AtmosphericAerosolPmmul aap : aaps) {
					insert_one_data(recv_time, connection, d_data_id, pStatement, aap,fileName,typeOfStorageInstrument, loggerBuffer,filepath);
				}
				return DataBaseAction.BATCH_ERROR;
			}

		} catch (SQLException e) {
			loggerBuffer.append("\n Get database connection error!");
			return DataBaseAction.CONNECTION_ERROR;
		} finally {
			listDi.clear();
			if (pStatement != null) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n Close Statement error: " + e.getMessage());
				}
			}
		}
	}

	/**
	 * 
	 * @Title: insert_one_data
	 * @Description: 单条数据入数据库
	 * @param recv_time
	 * @param connection
	 * @param d_data_id
	 * @param pStatement
	 * @param typeOfStorageInstrument 
	 * @param fileName 
	 * @param loggerBuffer 
	 * @param tower
	 * @return void
	 * @throws:
	 */
	private static void insert_one_data(Date recv_time, java.sql.Connection connection, String d_data_id, PreparedStatement pStatement, AtmosphericAerosolPmmul aap, String fileName, String typeOfStorageInstrument, StringBuffer loggerBuffer,String filepath) {
		StatDi di = new StatDi();
		generatePstAndDi(di, aap, pStatement, recv_time, d_data_id,fileName,typeOfStorageInstrument, loggerBuffer,filepath);
		try {
			pStatement.execute();
			// connection.commit();
		} catch (SQLException e1) {
			di.setPROCESS_STATE("0");// 0成功，1失败
			loggerBuffer.append("\n File name: " + fileName //
					+ "\n " + aap.getStationNumberChina() + " " + TimeUtil.date2String(aap.getD_datetime(), TimeUtil.DATE_FMT_YMDHMS) //
					+ "\n execute sql error: " + ((LoggableStatement) pStatement).getQueryString() //
					+ "\n " + e1.getMessage());
		} finally {
			diQueues.offer(di);
		}
	}

	/**
	 * 
	 * @Title: generatePstAndDi
	 * @Description: 设置prepareStatement和DI值
	 * @param di DI对象
	 * @param aap 大气成分气溶胶PM10/PM2.5/PM1质量浓度（PMMUL）实体类
	 * @param pStatement prepareStatement
	 * @param recv_time 收到时间
	 * @param typeOfStorageInstrument 
	 * @param fileName 
	 * @param loggerBuffer 
	 * @return void
	 * @throws:
	 */
	private static void generatePstAndDi(StatDi di, AtmosphericAerosolPmmul aap, PreparedStatement pStatement, Date recv_time, String d_data_id, String fileName, String typeOfStorageInstrument, StringBuffer loggerBuffer, String filepath) {
		Map<String, Object> proMap = StationInfo.getProMap();
		di.setFILE_NAME_O(fileName);
		di.setDATA_TYPE(d_data_id);
		di.setDATA_TYPE_1(cts_code);
		di.setTT("PMMUL");
		di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
		di.setPROCESS_START_TIME(TimeUtil.getSysTime());
		di.setFILE_NAME_N(fileName);
		di.setBUSINESS_STATE("1"); // 1成功，0失败
		di.setPROCESS_STATE("1"); // 1成功，0失败

		Date d_datetime = aap.getD_datetime(); // 资料时间
		String stationNumberC = aap.getStationNumberChina(); // 字符站号
		String stationNumberN = StationCodeUtil.stringToAscii(stationNumberC);

		Double latitude = aap.getLatitude(); // 纬度
		Double longitude = aap.getLongitude(); // 纬度
		Double height = aap.getStationHeight(); // 测站海拔高度
		// 根根配置文件  获取站点基本信息
		String info = (String) proMap.get(aap.getStationNumberChina() + "+16");
	
		if (info == null) {
			info = (String) proMap.get(aap.getStationNumberChina() + "+15");
		}
		
		if(info == null){
			infoLogger.error("\n  In configuration file, this station does not exist: " + aap.getStationNumberChina());	
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
		
		/*if (latitude == 999999.0) {
			// 根据站号查询行政区划代码
			String info = (String) proMap.get(stationNumberC + "+16");
			if (info == null) {
				// 如果此站号不存在，执行下一个数据
				loggerBuffer.append("\n In configuration file, this station does not exist: " + stationNumberC);
			} else {
				String[] infos = info.split(",");
				latitude = Double.parseDouble(infos[1]);// 经度
				longitude = Double.parseDouble(infos[2]); // 纬度
				aap.setLatitude(latitude);

				aap.setLongitude(longitude);
			}
		}*/

		// String adminCode = getAdminCodeByStationNumberC(stationNumberC); // 行政区划代码

		// ==================================================================================

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
		
		//1小时 PM10质量浓度
		Double massConcentrationOfPM10_1hour = aap.getMassConcentrationOfPM10_1hour();
		//1小时 PM2.5质量浓度
		Double massConcentrationOfPM2p5_1hour = aap.getMassConcentrationOfPM2p5_1hour();
		//1小时 PM1质量浓度
		Double massConcentrationOfPM1_1hour = aap.getMassConcentrationOfPM1_1hour();
		//24小时 PM10质量浓度
		Double massConcentrationOfPM10_24hour = aap.getMassConcentrationOfPM10_24hour();
		//24小时 PM2.5质量浓度
		Double massConcentrationOfPM2p5_24hour = aap.getMassConcentrationOfPM2p5_24hour();
		//24小时 PM1质量浓度
		Double massConcentrationOfPM1_24hour = aap.getMassConcentrationOfPM1_24hour();
		//旁路流量
		Double bypassFlow = aap.getBypassFlow();
		//PM10-2.5主路流量
		Double mainRoadFlowOfPM10_2p5 = aap.getMainRoadFlowOfPM10_2p5();
		//负载率
		Double loadRate = aap.getLoadRate();
		//频率
		Double frequency = aap.getFrequency();
		//噪音
		Double noise = aap.getNoise();
		//运行状态码
		int runningStatusCode = (int)aap.getRunningStatusCode();
		// ==================================================================================

		int num = 1;
		// 入库时间
		Timestamp insertTime = new Timestamp(new Date().getTime());
		// 资料时间
		Timestamp _dataTime = new Timestamp(d_datetime.getTime());

		String lengthStr = "00000";
		try {
			// 时间(yyyyMMddHHmmss)_微秒（毫秒转微秒）SSSSSS_站号5_经度10_纬度9_海拔或高度相关8_(V_BBB)_其他字段
			String id = TimeUtil.date2String(d_datetime, TimeUtil.DATE_FMT_YMDHMS) //
					+ "_" + stationNumberC //
//					+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(longitude * 1000000), 10) //
//					+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(latitude * 1000000), 9)//
//					+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(stationHeight * 100), 8)
					+ "_" + typeOfStorageInstrument;

			pStatement.setString(num++, id);// 记录标识
			pStatement.setString(num++, d_data_id);// 资料标识
			pStatement.setTimestamp(num++, insertTime);// 入库时间
			pStatement.setTimestamp(num++, new Timestamp(recv_time.getTime()));// 收到时间
			pStatement.setTimestamp(num++, insertTime);// 更新时间
			pStatement.setTimestamp(num++, _dataTime);// 资料时间
			pStatement.setString(num++, stationNumberC);// 区站号(字符)
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(stationNumberN)));// 区站号(数字)
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(latitude)));// 纬度
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(longitude)));// 经度
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(stationHeight)));// 测站高度
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(TimeUtil.getYear(d_datetime))));// 资料观测年
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(TimeUtil.getMonth(d_datetime))));// 资料观测月
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(TimeUtil.getDayOfMonth(d_datetime))));// 资料观测日
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(TimeUtil.getHourOfDay(d_datetime))));// 资料观测时
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(TimeUtil.getMinute(d_datetime))));// 资料观测分
			pStatement.setString(num++, typeOfStorageInstrument);// 存储仪器类型
			// pStatement.setBigDecimal(num ++, new BigDecimal(projectCode));// 项目代码
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
			
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(massConcentrationOfPM10_1hour)));//1小时 PM10质量浓度
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(massConcentrationOfPM10_24hour)));//24小时 PM10质量浓度
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(massConcentrationOfPM2p5_1hour)));//1小时 PM2.5质量浓度
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(massConcentrationOfPM2p5_24hour)));//24小时 PM2.5质量浓度
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(massConcentrationOfPM1_1hour)));//1小时 PM1质量浓度
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(massConcentrationOfPM1_24hour)));//24小时 PM1质量浓度
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(bypassFlow)));//旁路流量
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(mainRoadFlowOfPM10_2p5)));//PM10-2.5主路流量
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(loadRate)));//负载率
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(frequency)));//频率
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(noise)));//噪音
			pStatement.setInt(num++, runningStatusCode);//运行状态码
			
			pStatement.setString(num++, "000");
			pStatement.setString(num++, cts_code);
		} catch (Exception e) {
			loggerBuffer.append("\n Set element value failed: " + e.getMessage());
		}

		di.setIIiii(stationNumberC);
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
		
	}
	public static void main(String[] args){
		File file = new File("C:\\BaiduNetdiskDownload\\test\\G.2.8.1\\Z_CAWN_I_O5130_20191224060000_O_AER-FLD-PMMUL-BAM.TXT");
		DecodePmmul dp = new DecodePmmul();
		ParseResult<AtmosphericAerosolPmmul> parseResult = dp.decode(file);
		Date recv_time = new Date(file.lastModified());
		String fileName = file.getName();
		String typeOfStorageInstrument = "999999";
		String tempFileName = fileName.toUpperCase();
		String[] fs = tempFileName.split("_|-|\\.");
		if (fs.length == 11) {
			typeOfStorageInstrument = fs[9];
		}
		StringBuffer loggerBuffer = new StringBuffer();
		loggerBuffer.append(file.getPath());
		if (parseResult.isSuccess()) {
			DataBaseAction dataBaseAction = DbService.insert_db(parseResult, recv_time, fileName, typeOfStorageInstrument, loggerBuffer,file.getPath());
		}
	}
}
