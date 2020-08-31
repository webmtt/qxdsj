package cma.cimiss2.dpc.indb.agme.dc_agme_climat.service;

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
import java.util.concurrent.BlockingQueue;

import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import cma.cimiss2.dpc.decoder.bean.agme.CroplandMicroclimateData;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class DbService {
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static List<StatDi> listDi = null;
	// 与DI发送线程 共享对列
	public static BlockingQueue<StatDi> diQueues;
	
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}


	
	/**
	 * @Title: insert_db 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param list
	 * @param recv_time
	 * @param loggerBuffer 
	 * @param cts_code 
	 * @return  返回值说明
	 * @throws:
	 */
	@SuppressWarnings("deprecation")
	public static DataBaseAction insert_db(List<CroplandMicroclimateData> list,Date recv_time, StringBuffer loggerBuffer, String cts_code, String filepath) {
		File file = new File(filepath);
		String fileN = file.getName();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.sql.Connection connection = null;
		PreparedStatement pStatement = null;
		listDi = new ArrayList<StatDi>();
		String type = "E.0023.0001.S001";
		try {
			// 获取数据库连接
			//connection = JdbcUtilsSing.getInstance("config/agme_cli/z_agme_cli_db_config.xml").getConnection();
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			// chy 去掉D_record_id
			String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_record_id,D_DATA_ID,D_IYMDHM,D_UPDATE_TIME,D_RYMDHM,D_DATETIME,V01301,V01300,V05001,V06001,V07001,V_PROV,V04001,V04002,V04003,V04004,"
					+ "V71001,V07004,"
					+ "V12001_030,V12001_060,V12001_150,V12001_300,V12001_CNP,V13019,"
					+ "V11002_030,V11002_060,V11002_150,V11002_300,V11002_600,"
					+ "V13003_030,V13003_060,V13003_150,V13003_300,"
					+ "V12030_000,V12030_005,V12030_010,V12030_015,V12030_020,V12030_030,V12030_040,V12030_050,"
					+ "V71105_010,V71105_020,V71105_030,V71105_040,V71105_050,"
					+ "V71102_010,V71102_020,V71102_030,V71102_040,V71102_050,V_STNAME,V_BBB,D_SOURCE_ID) VALUES ("
					+ "?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			if(connection != null){
				pStatement = new LoggableStatement(connection, sql); 
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				// 关闭自动提交，手动批量提交
				List<String> sqls = new ArrayList<>();
				
				for (int i = 0; i < list.size(); i++) {
					CroplandMicroclimateData croplandMicroclimateData = list.get(i);
					StatDi di = new StatDi();					
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(type);
					di.setDATA_TYPE_1(cts_code);
			
					di.setTT("农田小气候");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd hh:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1");  //1成功，0失败	
					di.setLATITUDE(croplandMicroclimateData.getLatitude().toString());
					di.setLONGTITUDE(croplandMicroclimateData.getLongitude().toString());
					di.setHEIGHT(croplandMicroclimateData.getHeightOfSationGroundAboveMeanSeaLevel().toString());
					di.setDATA_UPDATE_FLAG("000");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(file.length()));
					int ii = 1;
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
					String primkey = sdf.format(croplandMicroclimateData.getObservationTime()) + "_" + croplandMicroclimateData.getStationNumberChina() + "_" +croplandMicroclimateData.getCropName();
					pStatement.setString(ii++, primkey);
					pStatement.setString(ii++, type);
					
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(croplandMicroclimateData.getObservationTime().getTime()));
					pStatement.setString(ii++, croplandMicroclimateData.getStationNumberChina());
					pStatement.setString(ii++,  StationCodeUtil.stringToAscii(croplandMicroclimateData.getStationNumberChina()));
					pStatement.setBigDecimal(ii++, new BigDecimal(croplandMicroclimateData.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(croplandMicroclimateData.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getHeightOfSationGroundAboveMeanSeaLevel())));              
					pStatement.setString(ii++, croplandMicroclimateData.getProvinceNameChina());
					pStatement.setBigDecimal(ii++, new BigDecimal(croplandMicroclimateData.getObservationTime().getYear() + 1900));
					pStatement.setBigDecimal(ii++, new BigDecimal(croplandMicroclimateData.getObservationTime().getMonth() + 1));
					pStatement.setBigDecimal(ii++, new BigDecimal(croplandMicroclimateData.getObservationTime().getDate()));
					pStatement.setBigDecimal(ii++, new BigDecimal(croplandMicroclimateData.getObservationTime().getHours()));	
					pStatement.setString(ii++, croplandMicroclimateData.getCropName());  //Cuihongyuan: change setBigDecimal() to setString()					
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getStationPressure())));		
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getTemperature_30())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getTemperature_60())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getTemperature_150())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getTemperature_300())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getTemperatureCanopy())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getHourlyPrecipitation())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getWindSpeed_30())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getWindSpeed_60())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getWindSpeed_150())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getWindSpeed_300())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getWindSpeed_600())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getRelativeHumidity_Air_30())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getRelativeHumidity_Air_60())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getRelativeHumidity_Air_150())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getRelativeHumidity_Air_300())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getSurfaceTemperature_0())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getSurfaceTemperature_5())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getSurfaceTemperature_10())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getSurfaceTemperature_15())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getSurfaceTemperature_20())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getSurfaceTemperature_30())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getSurfaceTemperature_40())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getSurfaceTemperature_50())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getMoisture_Soil_10())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getMoisture_Soil_20())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getMoisture_Soil_30())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getMoisture_Soil_40())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getMoisture_Soil_50())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getRelativeHumidity_Soil_10())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getRelativeHumidity_Soil_20())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getRelativeHumidity_Soil_30())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getRelativeHumidity_Soil_40())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getRelativeHumidity_Soil_50())));
					pStatement.setString(ii++, croplandMicroclimateData.getStationNameChina());
					pStatement.setString(ii++, "000");
					pStatement.setString(ii++, cts_code);
					
				//	System.out.println(((LoggableStatement)pStatement).getQueryString());
					di.setIIiii(croplandMicroclimateData.getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(croplandMicroclimateData.getObservationTime(), "yyyy-MM-dd hh:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());				
					loggerBuffer.append(" " + croplandMicroclimateData.getStationNumberChina() + " " + simpleDateFormat.format(croplandMicroclimateData.getObservationTime()) +" 000\n");
					
					// 批量提交
					pStatement.addBatch();
					sqls.add(((LoggableStatement)pStatement).getQueryString());
					listDi.add(di);
					
				} // for end
				
				try {
					pStatement.executeBatch();
					connection.commit();
					loggerBuffer.append(" INSERT SUCCESS FINISH TIME : " + simpleDateFormat.format(new Date()) + "\n");
					loggerBuffer.append(" INSERT SUCCESS COUNT ：" + list.size() + "\n");
					sqls.clear();
					return DataBaseAction.SUCCESS;
				} catch (Exception e) {
					pStatement.clearParameters();
					pStatement.clearBatch();
					loggerBuffer.append(" Batch ERROR : " + simpleDateFormat.format(new Date()) + "\n");
					connection.rollback();
					if(pStatement != null) {
						pStatement.close();
						pStatement = null;
					}
						
					execute_sql(sqls, connection, loggerBuffer);
					//execute_sql(list, connection, type, recv_time);
					loggerBuffer.append("\n 批量提交失败："+fileN);
					return DataBaseAction.BATCH_ERROR;
				}
				
			} else {
				loggerBuffer.append("\n 数据库连接错误");
				return DataBaseAction.CONNECTION_ERROR;
			}						
									
		} catch (SQLException e) {		
			loggerBuffer.append("\n 数据库连接关闭异常："+e.getMessage());
			return DataBaseAction.CONNECTION_ERROR;
		}finally {
			
			try {
				if(pStatement != null) {
					pStatement.clearParameters();
					pStatement.clearBatch();
					pStatement.close();
					System.out.println("finally ++++++++++++++++++++++ pStatement.close(); ");

				}
					
					
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				loggerBuffer.append("\n 数据库连接关闭异常："+e.getMessage());
			}	
			
			
			System.out.println("====================="+listDi);
			for (int j = 0; j < listDi.size(); j++) {
				diQueues.offer(listDi.get(j));
			}
			listDi.clear();
		}
	}
	 
	
	/**
	 * @Title: execute_sql 
	 * @Description: TODO(单条执行sql函数) 
	 * @param sqls sql语句集合
	 * @param connection 数据库连接对象
	 * @param loggerBuffer log日志记录对象
	 * @throws:
	 */
	private static void execute_sql(List<String> sqls, java.sql.Connection connection, StringBuffer loggerBuffer) {
		Statement pStatement = null;
		loggerBuffer.append("\n 开始单条插入");
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
							+"\n 执行sql错误："+sqls.get(i)+"\n "+e.getMessage());
					listDi.get(i).setPROCESS_STATE("1");
				}
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n 创建Statement异常"+e.getMessage());
		}finally {
			if(pStatement != null) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n 关闭Statement异常"+e.getMessage());
				}
			}
		}		
		
		
		
	}

	
	@SuppressWarnings({ "unused", "deprecation" })
	private void execute_sql(List<CroplandMicroclimateData> list, java.sql.Connection connection, String type,
			Date recv_time, String fileN) {
		PreparedStatement pStatement = null;
		// chy 去掉 D_RECORD_ID
		String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_UPDATE_TIME,D_RYMDHM,D_DATETIME,V01301,V01300,V05001,V06001,V07001,V_PROV,V04001,V04002,V04003,V04004,"
				+ "V71001,V07004,"
				+ "V12001_030,V12001_060,V12001_150,V12001_300,V12001_CNP,V13019,"
				+ "V11002_030,V11002_060,V11002_150,V11002_300,V11002_600,"
				+ "V13003_030,V13003_060,V13003_150,V13003_300,"
				+ "V12030_000,V12030_005,V12030_010,V12030_015,V12030_020,V12030_030,V12030_040,V12030_050,"
				+ "V71105_010,V71105_020,V71105_030,V71105_040,V71105_050,"
				+ "V71102_010,V71102_020,V71102_030,V71102_040,V71102_050,V_STNAME,V_BBB,D_SOURCE_ID) VALUES ("
				+ "?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			connection.setAutoCommit(true);
			pStatement = new LoggableStatement(connection, sql);
			for (int i = 0; i < list.size(); i++) {
				CroplandMicroclimateData croplandMicroclimateData = list.get(i);
				int ii = 1;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				String primkey = sdf.format(croplandMicroclimateData.getObservationTime()) + "_" + croplandMicroclimateData.getStationNumberChina() + "_" +croplandMicroclimateData.getCropName();
				pStatement.setString(ii++, primkey);
				pStatement.setString(ii++, type);
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(croplandMicroclimateData.getObservationTime().getTime()));
				pStatement.setString(ii++, croplandMicroclimateData.getStationNumberChina());
				pStatement.setString(ii++,  StationCodeUtil.stringToAscii(croplandMicroclimateData.getStationNumberChina()));
				
				pStatement.setBigDecimal(ii++, new BigDecimal(croplandMicroclimateData.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
				pStatement.setBigDecimal(ii++, new BigDecimal(croplandMicroclimateData.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getHeightOfSationGroundAboveMeanSeaLevel())));              
				pStatement.setString(ii++, croplandMicroclimateData.getProvinceNameChina());
				pStatement.setBigDecimal(ii++, new BigDecimal(croplandMicroclimateData.getObservationTime().getYear() + 1900));
				pStatement.setBigDecimal(ii++, new BigDecimal(croplandMicroclimateData.getObservationTime().getMonth() + 1));
				pStatement.setBigDecimal(ii++, new BigDecimal(croplandMicroclimateData.getObservationTime().getDate()));
				pStatement.setBigDecimal(ii++, new BigDecimal(croplandMicroclimateData.getObservationTime().getHours()));	
				pStatement.setString(ii++, croplandMicroclimateData.getCropName());  //Cuihongyuan: change setBigDecimal() to setString()					
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getStationPressure())));		
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getTemperature_30())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getTemperature_60())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getTemperature_150())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getTemperature_300())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getTemperatureCanopy())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getHourlyPrecipitation())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getWindSpeed_30())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getWindSpeed_60())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getWindSpeed_150())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getWindSpeed_300())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getWindSpeed_600())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getRelativeHumidity_Air_30())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getRelativeHumidity_Air_60())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getRelativeHumidity_Air_150())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getRelativeHumidity_Air_300())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getSurfaceTemperature_0())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getSurfaceTemperature_5())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getSurfaceTemperature_10())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getSurfaceTemperature_15())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getSurfaceTemperature_20())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getSurfaceTemperature_30())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getSurfaceTemperature_40())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getSurfaceTemperature_50())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getMoisture_Soil_10())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getMoisture_Soil_20())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getMoisture_Soil_30())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getMoisture_Soil_40())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getMoisture_Soil_50())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getRelativeHumidity_Soil_10())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getRelativeHumidity_Soil_20())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getRelativeHumidity_Soil_30())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getRelativeHumidity_Soil_40())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(croplandMicroclimateData.getRelativeHumidity_Soil_50())));
				pStatement.setString(ii++, croplandMicroclimateData.getStationNameChina());
				pStatement.setString(ii++, "000");
				pStatement.setString(ii++, StartConfig.ctsCode());
				
				try {
					pStatement.execute();
				} catch (SQLException e) {
					infoLogger.error("\n 文件名："+fileN+
							"\n " + croplandMicroclimateData.getStationNumberChina() + " " + sdf.format(croplandMicroclimateData.getObservationTime())
							+"\n 执行sql错误："+((LoggableStatement)pStatement).getQueryString()+"\n "+e.getMessage());
					listDi.get(i).setPROCESS_STATE("1");
				}
			}
		} catch (SQLException e) {
			infoLogger.error("\n 创建Statement异常"+e.getMessage());
		}finally {
			if(pStatement != null) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					infoLogger.error("\n 关闭Statement异常"+e.getMessage());
				}
			}
		}		
	}

}
