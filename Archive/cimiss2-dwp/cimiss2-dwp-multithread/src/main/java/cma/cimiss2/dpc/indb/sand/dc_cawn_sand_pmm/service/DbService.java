package cma.cimiss2.dpc.indb.sand.dc_cawn_sand_pmm.service;


import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import cma.cimiss2.dpc.decoder.bean.sand.SandAerosolPmm;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.sand.ReportInfoService;




public class DbService {
	
	HashMap<String, Integer> retryMap;
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
//    public static Map<String, Object> proMap = StationInfo.getProMap();
	public static String cts_code=StartConfig.ctsCode();
	public static String sod_code=StartConfig.sodCode();
	public static String report_sod_code=StartConfig.reportSodCode();
	
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}
	/**
	 * 
	 * @Title: processSuccessReport
	 * @Description: (处理解码结果集，正确解码的报文入库)
	 * @param parseResult 解码结果集
	 * @param recv_time 接收时间
	 * @param loggerBuffer 
	 * @param filepath 文件路径
	 * @return DataBaseAction 入库状态
	 * @throws：
	 */
	@SuppressWarnings("rawtypes")
	public static DataBaseAction processSuccessReport(ParseResult<SandAerosolPmm> parseResult,
			Date recv_time, String fileN, StringBuffer loggerBuffer, String filepath) {
		java.sql.Connection connection = null;
		java.sql.Connection reportConnection = null;
		try {
			String v_tt="PMM";
			// 获取数据库连接
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			List<SandAerosolPmm> sand_pmm = parseResult.getData();
			insertDB(sand_pmm, connection, recv_time,v_tt,fileN,loggerBuffer, filepath);
		 
			List<ReportInfo> reportInfos = parseResult.getReports();
			reportConnection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
			ReportInfoService.reportInfoToDb(reportInfos, reportConnection, "000", recv_time, "9999", v_tt, report_sod_code,cts_code, "15", loggerBuffer,filepath,listDi);
			return DataBaseAction.SUCCESS;
		} catch (Exception e) {
			loggerBuffer.append("\n Database connection error");
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
					loggerBuffer.append("\n Database connection close error"+e.getMessage());
				}
			}
			
			if(reportConnection != null) {
				try {
					reportConnection.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n Database connection close error"+e.getMessage());
				}
			}
		}
	}
	/**
	 * 
	 * @Title: insertDB
	 * @Description: (沙尘暴气溶胶质量浓度PMM资料入库)
	 * @param sandpmm 入库对象集合
	 * @param connection 数据库连接
	 * @param recv_time 接收时间
	 * @param v_tt void 报文类别
	 * @param fileN 
	 * @param loggerBuffer 
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static void insertDB(List<SandAerosolPmm> sandpmm, java.sql.Connection connection,
			Date recv_time, String v_tt, String fileN, StringBuffer loggerBuffer, String filepath) {
		Map<String, Object> proMap = StationInfo.getProMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		PreparedStatement pStatement = null;
		try {
			String sql = "INSERT INTO "+StartConfig.valueTable()+" (D_RECORD_ID,D_DATA_ID, D_IYMDHM, D_RYMDHM, D_UPDATE_TIME, D_DATETIME, "
					+ "V01301, V01300, V05001, V06001, V07001, V04001, V04002, V04003, V04004, V04005, V02850, V_ITEM_CODE, "
					+ "V_STORAGE_PLACE, V_WEIGHT_FACTOR, V_ERROR_CODE, V15752, V15765, V_CORRECT_COUNT, V10004_040, V13003_040, V12001_040, V_TIME_SPACING, V11002_071, "
					+ "V11001_071, V13011_071, V12001_072, V13003_072, V10004_072, V12001_073, V13003_073,V10004_073, V11450, V11451,"
					+ " V02440, V10004, V13003, V12001, V11002, V11001, V13011, V15471, V15472, V15473, V01324_P10, "
					+ "V01324_P25,V01324_P01, V15730_P10, V12001_P10, V10004_P10, V13003_P10, V15730_P25, V12001_P25, V10004_P25, V13003_P25, "
					+ "V15730_P01, V12001_P01, V10004_P01, V13003_P01,V_BBB, D_SOURCE_ID) VALUES"
					+ " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			pStatement = new LoggableStatement(connection, sql); 
			if(StartConfig.getDatabaseType() == 1) {
				pStatement.execute("select last_txc_xid()");
			}
			if(connection != null){
				List<String> sqls = new ArrayList<>();
				for(int i=0;i<sandpmm.size();i++){
					SandAerosolPmm sap = sandpmm.get(i);
					// 设置DI信息
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
					// 根根配置文件  获取站点基本信息
					String info = (String) proMap.get(sap.getStationNumberChina() + "+15");
					Double latitude = sap.getLatitude(); // 纬度
					Double longitude = sap.getLongitude(); // 纬度
					Double heightOfSationGroundAboveMeanSeaLevel =sap.getHeightOfSationGroundAboveMeanSeaLevel();//测站告诉
					// 根据站号查询行政区划代码
					if (info == null) {
						// 如果此站号不存在，执行下一个数据
						loggerBuffer.append("\n In the configuration file, the station number does not exist: " + sap.getStationNumberChina());
					}else {
						String[] infos = info.split(",");
						latitude = Double.parseDouble(infos[2]);// 纬度
						longitude = Double.parseDouble(infos[1]); //经度
						heightOfSationGroundAboveMeanSeaLevel=Double.parseDouble(infos[3]);//测站高度
						sap.setLatitude(latitude);					
						sap.setLongitude(longitude);
					}
						
					
					int ii=1;
					String PrimaryKey = sdf.format(sap.getObservationTime())+"_"+sap.getStationNumberChina();
					
					pStatement.setString(ii ++, PrimaryKey);// 记录标识
					pStatement.setString(ii ++, sod_code);// 资料标识
					pStatement.setTimestamp(ii ++,  new Timestamp(new Date().getTime()));// 入库时间
					pStatement.setTimestamp(ii ++,  new Timestamp(recv_time.getTime()));// 收到时间
					pStatement.setTimestamp(ii ++,  new Timestamp(new Date().getTime()));// 更新时间
					pStatement.setTimestamp(ii ++, new Timestamp(sap.getObservationTime().getTime()));// 资料时间
					pStatement.setString(ii ++, sap.getStationNumberChina());// 区站号(字符)
					pStatement.setBigDecimal(ii ++, new BigDecimal(StationCodeUtil.stringToAscii(sap.getStationNumberChina())));// 区站号(数字)
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(latitude)));// 纬度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(longitude)));// 经度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(heightOfSationGroundAboveMeanSeaLevel)));// 测站高度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getObservationTime().getYear() + 1900)));// 资料观测年
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getObservationTime().getMonth()+1)));// 资料观测月
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getObservationTime().getDate())));// 资料观测日
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getObservationTime().getHours())));// 资料观测时
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getObservationTime().getMinutes())));// 资料观测分
					pStatement.setString(ii ++, sap.getTypeOfStorageInstrument());// 存储仪器类型
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getProjectCode())));// 项目代码
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getStorageLocation())));// 存储位置
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getWeightFactor())));// 重量因数
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getErrorCode())));// 错误代码
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getBatteryVoltageCode())));// 电池电压代码
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getValveCurrent())));// 阀电流
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getComprehensiveCorrectionCount())));// 综合订正计数
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getBarometricCount())));// 气压计数
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getHumidityCount())));// 湿度计数
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getTemperatureCount())));// 温度计数
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getTimeInterval())));// 时间间隔
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getWindSpeedMeasurementFactor())));// 风速计量因子
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getWindDirectionMeasurementFactor())));// 风向计量因子
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getPrecipitationMeasurementFactor())));// 降水计量因子
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getTemperatureLinearSlopeRate())));// 温度线性转化后斜率
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getHumidityLinearSlopeRate())));// 湿度线性转化后斜率
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getPressureLinearSlopeRate())));// 气压线性转化后斜率
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getTemperatureLinearIntercept())));// 温度线性转化后截距
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getHumidityLinearIntercept())));// 湿度线性转化后截距
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getPressureLinearIntercept())));// 气压线性转化后截距
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getWindSpeedSensitivity())));// 风速灵敏度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getWindDirectionAngle())));// 风向倾角
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getPrecipitationSensorCorrectionFactor())));// 降水传感器订正因子
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getPressure())));// 气压
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getRelativeHumidity())));// 相对湿度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getTemperature())));// 温度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getWindSpeed())));// 风速
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getWindDirection())));// 风向
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getPrecipitation())));// 降水
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getMassConcentrationOfPM10())));// PM10质量浓度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getMassConcentrationOfPM2p5())));// PM2.5质量浓度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getMassConcentrationOfPM1())));// PM1质量浓度
					pStatement.setString(ii ++, sap.getMeasurementDataFlag_pm10());// PM10测量数据标识
					pStatement.setString(ii ++, sap.getMeasurementDataFlag_pm2p5());// PM2.5测量数据标识
					pStatement.setString(ii ++, sap.getMeasurementDataFlag_pm1());// PM1测量数据标识
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getFlowRate_pm10())));// PM10仪器测量的流量
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getTemperature_pm10())));// PM10仪器测量的气温
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getPressure_pm10())));// PM10仪器测量的气压
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getRelativeHumidity_pm10())));// PM10仪器测量的相对湿度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getFlowRate_pm2p5())));// PM2.5仪器测量的流量
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getTemperature())));// PM2.5仪器测量的气温
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getPressure())));// PM2.5仪器测量的气压
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getRelativeHumidity())));// PM2.5仪器测量的相对湿度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getFlowRate_pm1())));// PM1.0仪器测量的流量
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getTemperature())));// PM1.0仪器测量的气温
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getPressure())));// PM1.0仪器测量的气压
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(sap.getRelativeHumidity())));// PM1.0仪器测量的相对湿度
					pStatement.setString(ii++, "000");
					pStatement.setString(ii++, cts_code);
				
					di.setIIiii(sap.getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(sap.getObservationTime(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLATITUDE(String.valueOf(sap.getLatitude()));
					di.setLONGTITUDE(String.valueOf(sap.getLongitude()));
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
					di.setDATA_UPDATE_FLAG("000");
					di.setHEIGHT(String.valueOf(sap.getHeightOfSationGroundAboveMeanSeaLevel()));
					
					// 批量提交
					pStatement.addBatch();
					sqls.add(((LoggableStatement)pStatement).getQueryString());
					listDi.add(di);	
				}			
				try {
					// 执行批量			
					pStatement.executeBatch();
					// 手动提交			
					connection.commit();
					sqls.clear();
				} catch (Exception e) {
					pStatement.clearParameters();
					pStatement.clearBatch();	
					execute_sql(sqls, connection,fileN,loggerBuffer);
					loggerBuffer.append("\n Batch commit failed："+fileN);
					
				}			
			} else {
				loggerBuffer.append("\n Database connection error");
			}														
		} catch (SQLException e) {		
			loggerBuffer.append("\n  Database connection close error："+e.getMessage());
		}finally {
			try {
				if(pStatement != null)
					pStatement.close();
			} catch (SQLException e) {
				loggerBuffer.append("\n Database connection close error："+e.getMessage());
			}			
		}
	}
		
		
	/**
	 * 
	 * @Title: execute_sql
	 * @Description: (批量入库失败时，采用逐条提交)  
	 * @param sqls sql对象集合
	 * @param connection  数据库连接
	 * @param fileN 
	 * @param loggerBuffer 
	 * @throws：
	 */
	private static void execute_sql(List<String> sqls, java.sql.Connection connection, String fileN, StringBuffer loggerBuffer) {
		Statement pStatement = null;
		try {
			pStatement = connection.createStatement();
			for (int i = 0; i < sqls.size(); i++) {
//				pStatement = new LoggableStatement(connection, sqls.get(i));
				/*if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}*/
				try {
					pStatement.execute(sqls.get(i));
					connection.commit();
				} catch (Exception e) {
					loggerBuffer.append("\n filename："+fileN
							+"\n " + listDi.get(i).getIIiii() + " " + listDi.get(i).getDATA_TIME()
							+"\n execute sql error："+sqls.get(i)+"\n "+e.getMessage());
					listDi.get(i).setPROCESS_STATE("0");
				}
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n create Statement error"+e.getMessage());
		}finally {
			if(pStatement != null) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n close Statement error"+e.getMessage());
				}
			}
		}		
		
	}

}
