package cma.cimiss2.dpc.indb.ocen.dc_ocean_tide.service;

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
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ocean.Tide;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;


public class DbService {
	HashMap<String, Integer> retryMap;
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	public static String sod_report = StartConfig.reportSodCode();
	
	public static String V_TT = "TIDE";
	public static String V_BBB = "000";
	public static String V_CCCC = "9999";
	public static double defaultF = 999999.0;
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
	 * @Description:(全球潮位站观测资料解码入库)   
	 * @param  parseResult 解码结果集
	 * @param recv_time 解码文件的路径
	 * @param  fileN     资料接收时间
	 * @param fileN2 
	 * @return: DataBaseAction      
	 * @throws:
	 */
	public static DataBaseAction processSuccessReport(ParseResult<Tide> parseResult, Date recv_time, String fileN, StringBuffer loggerBuffer, String filepath) {
		java.sql.Connection connection = null;
		java.sql.Connection reportConnection = null;
		try {
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			List<Tide> tides = parseResult.getData();
			insertTide(tides, connection,recv_time, fileN,loggerBuffer, filepath);
			
			@SuppressWarnings("rawtypes")
			List<ReportInfo> reportInfos = parseResult.getReports();
			reportConnection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
			reportInfoToDb(reportInfos, reportConnection,recv_time, fileN,loggerBuffer);  
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
	 * @Title: insertTide   
	 * @Description:(全球潮位站资料入库)   
	 * @param ships 待入库对象集
	 * @param  connection 数据库连接
	 * @param  recv_time      资料接收时间
	 * @param fileN 
	 * @param loggerBuffer 
	 * @return: void      
	 * @throws:
	 */
	@SuppressWarnings("deprecation")
	private static void insertTide(List<Tide> tides, java.sql.Connection connection, Date recv_time, String fileN, StringBuffer loggerBuffer,  String filepath) {
		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement pStatement = null;
		// chy 去掉 D_RECORD_ID
		String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,D_UPDATE_TIME,"
				+ "V01301,V05001,V06001,V02007,V04001,V04002,V04003,V04004,V04005,V04006,"
				+ "V22042,V22120,V22121,V22038,V22039,V_BBB, D_SOURCE_ID) "
				+ "VALUES (?, ?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, ?) ";
		if(connection != null){		
			try {	
				pStatement = new LoggableStatement(connection, sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				connection.setAutoCommit(false);
				List<String> sqls = new ArrayList<>();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");		
				for(int idx = 0; idx < tides.size(); idx ++){
					Tide tide = new Tide();
					tide = tides.get(idx);
					StatDi di = new StatDi();	
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(sod_code);
					di.setDATA_TYPE_1(cts_code);
					di.setTT(V_TT);			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1");  //1成功，0失败	
			
					Date date = tide.getObservationTime();
					String stat = tide.getStationNumberChina();
					double latitude = defaultF;
					double longtitude = defaultF;
					String info = (String) proMap.get(stat + "+20");
					if(info == null) 
						loggerBuffer.append("\n In the configuration file, the station number does not exist:" + stat);
					else{
						String[] infos = info.split(",");
						if(infos[1].equals("null")) 
							loggerBuffer.append("\n In the configuration file, the station longitude is empty");
						else longtitude = Double.parseDouble(infos[1]);
						if(infos[2].equals("null")) 
							loggerBuffer.append("\n In the configuration file, the latitude of the station is empty");
						else latitude = Double.parseDouble(infos[2]);
					}
					tide.setLatitude(latitude);
					tide.setLongtitude(longtitude);
					
					String lat = String.valueOf((int)(tide.getLatitude() * 1e6));
					String lon = String.valueOf((int)(tide.getLongtitude() * 1e6));
					lat = lat.replaceAll("-", "0");
					lon = lon.replaceAll("-", "0");
					
					int ii = 1;
					pStatement.setString(ii++, sdf.format(date)+"_"+stat+"_"+lat+"_"+lon+"_"+tide.getSensorType());
					pStatement.setString(ii++, sod_code);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(tide.getObservationTime().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					
					// "V01301,V05001,V06001,V02007,V04001,V04002,V04003,V04004,V04005,V04006,"
					pStatement.setString(ii++, stat);
					pStatement.setBigDecimal(ii++, new BigDecimal(latitude).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(longtitude).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setString(ii++, tide.getSensorType());
					pStatement.setInt(ii++, date.getYear() + 1900);
					pStatement.setInt(ii++, date.getMonth() + 1);
					pStatement.setInt(ii++, date.getDate());
					pStatement.setInt(ii++, date.getHours());
					pStatement.setInt(ii++, date.getMinutes());
					pStatement.setInt(ii++, date.getSeconds());
					//	+ "V22042,V22120,V22121,V22038,V22039,V_BBB)"
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(tide.getSeaTemperature())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(tide.getAutoWaterLevelDetection())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(tide.getManuallyWaterLevelDetection())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(tide.getTidalHeightAboveChartDatum())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(tide.getResidualTidalHeight())));
					pStatement.setString(ii++, V_BBB);
					pStatement.setString(ii++, cts_code);
					
					di.setIIiii(stat);
					di.setDATA_TIME(TimeUtil.date2String(tide.getObservationTime(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(latitude));
					di.setLONGTITUDE(String.valueOf(longtitude));
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
					di.setDATA_UPDATE_FLAG(V_BBB);
					di.setHEIGHT("999999");
					
					pStatement.addBatch();
					sqls.add(((LoggableStatement)pStatement).getQueryString()); // 批量入库冲突时，单条入库
					listDi.add(di);
				} 
				try{
					pStatement.executeBatch();
					connection.commit();
					sqls.clear();
				}catch(SQLException e){
					pStatement.clearParameters();
					pStatement.clearBatch();
					execute_sql(sqls, connection,fileN,loggerBuffer); // 此中，入库失败的会将对应的DI的ProcessState置为1 // 0:失败，1：成功
					loggerBuffer.append("\n Batch submission failed："+fileN);
				}
			}catch (SQLException e) {
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
	
	/**
	 * 
	 * @Title: reportInfoToDb   
	 * @Description:(报文信息入库)   
	 * @param  reportInfos 报文列表集合
	 * @param  connection  数据库连接
	 * @param recv_time   资料时间
	 * @param loggerBuffer 
	 * @param fileN 
	 * @return: void      
	 * @throws:
	 */
	@SuppressWarnings({ "resource", "deprecation" })
	private static void reportInfoToDb(@SuppressWarnings("rawtypes") List<ReportInfo> reportInfos, java.sql.Connection connection, Date recv_time, String fileN, StringBuffer loggerBuffer) {
		PreparedStatement pStatement = null;
		// chy 去掉 D_RECORD_ID
		String sql = "INSERT INTO "+StartConfig.reportTable()+" (D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,"
				+ "V_BBB,V_CCCC,V_TT,V01301,V05001,V06001,"
				+ "V04001,V04002,V04003,V04004,V04005,V_LEN,V_REPORT,d_source_ID) VALUES"
				+ "(?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?)";
		try {
			pStatement = new LoggableStatement(connection, sql);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			for (int i = 0; i < reportInfos.size(); i++) {
				try {
					Tide tide = (Tide)reportInfos.get(i).getT();
					String stat = tide.getStationNumberChina();
					Date date = tide.getObservationTime();
					String lat = String.valueOf((int)(tide.getLatitude() * 1e6));
					String lon = String.valueOf((int)(tide.getLongtitude() * 1e6));
					lat = lat.replaceAll("-", "0");
					lon = lon.replaceAll("-", "0");
					String primkey = sdf.format(date)+"_"+stat+"_"+lat+"_"+lon+"_"+tide.getSensorType()+"_"+V_TT+"_"+ V_BBB;
					int ii = 1;
					pStatement.setString(ii++, primkey);
					pStatement.setString(ii++, sod_report);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(date.getTime()));
					pStatement.setString(ii++, V_BBB);
					pStatement.setString(ii++, V_CCCC);
					pStatement.setString(ii++, V_TT);
					pStatement.setString(ii++, stat);
					pStatement.setBigDecimal(ii++, new BigDecimal(tide.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(tide.getLongtitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setInt(ii++, date.getYear() + 1900);
					pStatement.setInt(ii++, date.getMonth() + 1);
					pStatement.setInt(ii++, date.getDate());
					pStatement.setInt(ii++, date.getHours());
					pStatement.setInt(ii++, date.getMinutes());
					pStatement.setInt(ii++, reportInfos.get(i).getReport().length());
					pStatement.setString(ii++, reportInfos.get(i).getReport());
					pStatement.setString(ii++, cts_code);
					
					pStatement.execute();
					connection.commit();
				} catch (Exception e) {
					loggerBuffer.append("sql error:"+e.getMessage());
					continue;
				}
			}
		} catch (SQLException e) {
			loggerBuffer.append("Database connection exception:" + e.getMessage());
			return;
		}finally {
			if(pStatement != null) {
				try {
					pStatement.close();
					pStatement = null;
				} catch (SQLException e) {
					loggerBuffer.append("\n Close statement error!");
				}
			}
		}
	}

	/**
	 * 
	 * @Title: execute_sql   
	 * @Description:(批量入库失败时，采用逐条提交)   
	 * @param  sqls 待执行的查询语句
	 * @param  connection      数据库连接
	 * @param fileN 
	 * @param loggerBuffer 
	 * @return: void      
	 * @throws:
	 */
	private static void execute_sql(List<String> sqls, java.sql.Connection connection, String fileN, StringBuffer loggerBuffer) {
		Statement pStatement = null;
		try {
			connection.setAutoCommit(true);
			pStatement = connection.createStatement();
			for (int i = 0; i < sqls.size(); i++) {
//				pStatement = new LoggableStatement(connection, sqls.get(i));
				try {
					pStatement.execute(sqls.get(i));
//					connection.commit();
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
