package cma.cimiss2.dpc.indb.sevp.dc_sevp_nega_nodi.service;

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
import cma.cimiss2.dpc.decoder.bean.sevp.NegOxygenIon;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class DbService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	public static String sod_report_code = StartConfig.reportSodCode();
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
	 * @Description:(报文解码入库函数)   
	 * @param  parseResult  存放解码结果
	 * @param filepath 文件路径
	 * @param  recv_time  报文接收时间   
	 * @return: DataBaseAction      
	 * @throws:
	 */
	public static DataBaseAction processSuccessReport(ParseResult<NegOxygenIon> parseResult, String fileN, Date recv_time,StringBuffer loggerBuffer) {
		java.sql.Connection connection = null;
		java.sql.Connection reportConnection = null;
		try {
			// 获取数据库连接
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			// 获取解码结果集
			List<NegOxygenIon> negOxygenIons = parseResult.getData();
			insertDB(negOxygenIons, connection, recv_time, loggerBuffer,fileN);
			@SuppressWarnings("rawtypes")
			List<ReportInfo> reportInfos = parseResult.getReports();
			reportConnection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
			reportInfoToDb(reportInfos, reportConnection, recv_time, loggerBuffer,fileN);  
			return DataBaseAction.SUCCESS;
		} catch (Exception e) {
			loggerBuffer.append("\n  Database connection error!");
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
					loggerBuffer.append("\n Close database connection error: "+e.getMessage());
				}
			}
		}
	}
	/**
	 * 
	 * @Title: insertDB
	 * @Description:(旅游景区负氧离子观测数据入库)   
	 * @param negOxygenIons 待入库对象集合
	 * @param connection 数据库连接
	 * @param recv_time 资料接收接收
	 * @param fileN 
	 * @return void 
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static void insertDB(List<NegOxygenIon> negOxygenIons, java.sql.Connection connection, Date recv_time, StringBuffer loggerBuffer, String fileN){
		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,D_UPDATE_TIME,"
			+ "V_BBB,V01301,V01300,V05001,V06001,V07001,V02001,V02301,V_ACODE,V71115,"
			+ "V33307,V04001,V04002,V04003,V04004,V04005,V15551,V15551_005,V15551_005_052,V15551_006,"
			+ "V15551_006_052,V12001,V12011,V12011_052,V12012,V12012_052,V13003,V13008,V13008_052,V12003,"
			+ "V13004,Q15551,Q15551_005,Q15551_005_052,Q15551_006,Q15551_006_052,Q12001,Q12011,Q12011_052,Q12012,"
			+ "Q12012_052,Q13003,Q13008,Q13008_052,Q12003,Q13004,D_SOURCE_ID) "
			+ "VALUES (?,?, ?, ?, ?, ?,"
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?,?) " ;
		if(connection != null){		
			try {	
				pStatement = new LoggableStatement(connection, sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				connection.setAutoCommit(false);
				List<String> sqls = new ArrayList<>();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");		
				String V_TT = "NEO";
				for(int idx = 0; idx < negOxygenIons.size(); idx ++){
					NegOxygenIon negOxygenIon = negOxygenIons.get(idx);
					int stationLevel = 999999;
					int adminCode = 999999;
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
						
					Date dataTime = negOxygenIon.getObservationTime();
					String station = negOxygenIon.getStationNumberChina();
					int ii = 1;
					pStatement.setString(ii++, sdf.format(dataTime)+"_"+station);
					pStatement.setString(ii++, sod_code);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(dataTime.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					
					String info = (String) proMap.get(station + "+01");
					if(info == null) {
						loggerBuffer.append("\n In configuration file, this station does not exist: " + station);
					}
					else{
						String[] infos = info.split(",");
						if(!infos[5].equals("null"))  //中国行政区域代码
							adminCode = Integer.parseInt(infos[5]);
						if(!infos[6].equals("null"))
							stationLevel = Integer.parseInt(infos[6]); // 台站级别
					}
					//+ "V_BBB,V01301,V01300,V05001,V06001,V07001,V02001,V02301,V_ACODE,V71115,"
					pStatement.setString(ii++, "000");
					pStatement.setString(ii++, station);
					pStatement.setInt(ii++, Integer.parseInt(StationCodeUtil.stringToAscii(station)));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(negOxygenIon.getLatitude())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(negOxygenIon.getLongtitude())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(negOxygenIon.getHeight())));
					pStatement.setInt(ii++, 0); // 测站类型
					pStatement.setInt(ii++, stationLevel); // 台站级别
					pStatement.setInt(ii++, adminCode);
					pStatement.setInt(ii++, negOxygenIon.getSiteMarkOfStation());
 					//+ "V33307,V04001,V04002,V04003,V04004,V04005,V15551,V15551_005,V15551_005_052,V15551_006,"
					pStatement.setInt(ii++, negOxygenIon.getQualityContorl());
					pStatement.setInt(ii++, dataTime.getYear() + 1900);
					pStatement.setInt(ii++, dataTime.getMonth() + 1);
					pStatement.setInt(ii++, dataTime.getDate());
					pStatement.setInt(ii++, dataTime.getHours());
					pStatement.setInt(ii++, dataTime.getMinutes());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(negOxygenIon.getNegOxyIonConcentration().getValue())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(negOxygenIon.getMaxNegOxyIonConcentration().getValue())));
					pStatement.setInt(ii++, negOxygenIon.getTimeOfMaxNegOxyIonConcentration().getValue());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(negOxygenIon.getMinNegOxyIonConcentration().getValue())));
					//+ "V15551_006_052,V12001,V12011,V12011_052,V12012,V12012_052,V13003,V13008,V13008_052,V12003,"
					pStatement.setInt(ii++, negOxygenIon.getTimeOfMinNegOxyIonConcentration().getValue());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(negOxygenIon.getTemperature().getValue())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(negOxygenIon.getMaxTemperature().getValue())));
					pStatement.setInt(ii++, negOxygenIon.getTimeOfMaxTemperature().getValue());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(negOxygenIon.getMinTemperature().getValue())));
					pStatement.setInt(ii++, negOxygenIon.getTimeOfMinTemperature().getValue());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(negOxygenIon.getRelativeHumidity().getValue())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(negOxygenIon.getMaxRelativeHumidity().getValue())));
					pStatement.setInt(ii++, negOxygenIon.getTimeOfMaxRelativeHumidity().getValue());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(negOxygenIon.getDewPoint().getValue())));
					//+ "V13004,Q15551,Q15551_005,Q15551_005_052,Q15551_006,Q15551_006_052,Q12001,Q12011,Q12011_052,Q12012,"
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(negOxygenIon.getWaterVaporPressure().getValue())));
					pStatement.setInt(ii++, negOxygenIon.getNegOxyIonConcentration().getQuality().get(0).getCode());
					pStatement.setInt(ii++, negOxygenIon.getMaxNegOxyIonConcentration().getQuality().get(0).getCode());
					pStatement.setInt(ii++, negOxygenIon.getTimeOfMaxNegOxyIonConcentration().getQuality().get(0).getCode());
					pStatement.setInt(ii++, negOxygenIon.getMinNegOxyIonConcentration().getQuality().get(0).getCode());
					pStatement.setInt(ii++, negOxygenIon.getTimeOfMinNegOxyIonConcentration().getQuality().get(0).getCode());
					pStatement.setInt(ii++, negOxygenIon.getTemperature().getQuality().get(0).getCode());
					pStatement.setInt(ii++, negOxygenIon.getMaxTemperature().getQuality().get(0).getCode());
					pStatement.setInt(ii++, negOxygenIon.getTimeOfMaxTemperature().getQuality().get(0).getCode());
					pStatement.setInt(ii++, negOxygenIon.getMinTemperature().getQuality().get(0).getCode());
					//+ "Q12012_052,Q13003,Q13008,Q13008_052,Q12003,Q13004) "
					pStatement.setInt(ii++, negOxygenIon.getTimeOfMinTemperature().getQuality().get(0).getCode());
					pStatement.setInt(ii++, negOxygenIon.getRelativeHumidity().getQuality().get(0).getCode());
					pStatement.setInt(ii++, negOxygenIon.getMaxRelativeHumidity().getQuality().get(0).getCode());
					pStatement.setInt(ii++, negOxygenIon.getTimeOfMaxRelativeHumidity().getQuality().get(0).getCode());
					pStatement.setInt(ii++, negOxygenIon.getDewPoint().getQuality().get(0).getCode());
					pStatement.setInt(ii++, negOxygenIon.getWaterVaporPressure().getQuality().get(0).getCode());
					pStatement.setString(ii++, cts_code);
					
					di.setIIiii(station);
					di.setDATA_TIME(TimeUtil.date2String(dataTime, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(negOxygenIon.getLatitude()));
					di.setLONGTITUDE(String.valueOf(negOxygenIon.getLongtitude()));
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(fileN).length()));
					di.setDATA_UPDATE_FLAG("000");
					di.setHEIGHT(String.valueOf(negOxygenIon.getHeight()));
					
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
					execute_sql(sqls, connection, loggerBuffer,fileN); // 此中，入库失败的会将对应的DI的ProcessState置为1 // 1:失败，0：成功
					loggerBuffer.append("\n Batch commit failed: "+fileN);
				}
			}catch (SQLException e) {
				loggerBuffer.append("\n Create Statement error: "+e.getMessage());
			}
			finally {
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
	 * @Title: reportInfoToDb   
	 * @Description:(报文信息入库)   
	 * @param  reportInfos 报文列表集合
	 * @param  connection  数据库连接
	 * @param  recv_time   资料时间
	 * @return: void      
	 * @throws:
	 */
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public static void reportInfoToDb( List<ReportInfo> reportInfos, java.sql.Connection connection, Date recv_time, StringBuffer loggerBuffer,String filepath) {
		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.reportTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,"
				+ "V_BBB,V_CCCC,V_TT,"
				+ "V01301,V01300,V05001,V06001,V_NCODE,V_ACODE,V04001,V04002,V04003,V04004,V04005,"
				+ "V_LEN,V_REPORT) VALUES"
				+ "(?,?,?,?,?,?,"
				+ " ?,?,?,"
				+ " ?,?,?,?,?,?,?,?,?,?,?,"
				+ " ?,?)";
		try {
			pStatement = new LoggableStatement(connection, sql);
			if(StartConfig.getDatabaseType() == 1) {
				pStatement.execute("select last_txc_xid()");
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String V_TT= "NEO";
			NegOxygenIon negOxygenIon = null;
			Date oTime = null;
			String primkey = null;
			String sta;
			String info;
			int adminCode = 999999;
			File file=new File(filepath);
			String fileN=file.getName();
			for (int i = 0; i < reportInfos.size(); i++) {
				try {
					StatDi di = new StatDi();	
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(sod_report_code);
					di.setDATA_TYPE_1(cts_code);
					di.setTT(V_TT);			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1");  //1成功，0失败
					
					negOxygenIon = (NegOxygenIon) reportInfos.get(i).getT();
					sta = negOxygenIon.getStationNumberChina();
					oTime = negOxygenIon.getObservationTime();
					primkey = sdf.format(oTime)+"_"+sta+"_"+V_TT;
					int ii = 1;
					pStatement.setString(ii++, primkey);
					pStatement.setString(ii++, sod_report_code);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(oTime.getTime()));
					// "V_BBB,V_CCCC,V_TT,"
					pStatement.setString(ii++, "000");
					pStatement.setString(ii++, "9999");
					pStatement.setString(ii++, V_TT);
					//	+ "V01301,V01300,V05001,V06001,V_NCODE,V_ACODE,V04001,V04002,V04003,V04004,V04005,"
					pStatement.setString(ii++, sta);
					pStatement.setInt(ii++, Integer.parseInt(StationCodeUtil.stringToAscii(sta)));
					
					double stationHeight=999999.0;
					info = (String) proMap.get(sta + "+01");
					if(info == null) {
						loggerBuffer.append("\n  In configuration file, this station does not exist: " + sta);
					}
					else{
						String[] infos = info.split(",");						
						if(infos[5].equals("null"))
							loggerBuffer.append("\n  In configuration file, v_cccc is null! ");
						else 
							adminCode = Integer.parseInt(infos[5]);
						if(!infos[3].equals("null")){
							stationHeight = Double.parseDouble(infos[3]);
						}
					}
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(negOxygenIon.getLatitude())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(negOxygenIon.getLongtitude())));
					pStatement.setInt(ii++, 2250);
					pStatement.setInt(ii++, adminCode);
					pStatement.setInt(ii++, oTime.getYear() + 1900);
					pStatement.setInt(ii++, oTime.getMonth() + 1);
					pStatement.setInt(ii++, oTime.getDate());
					pStatement.setInt(ii++, oTime.getHours());
					pStatement.setInt(ii++, oTime.getMinutes());
					// "V_LEN,V_REPORT) 
					pStatement.setInt(ii++, reportInfos.get(i).getReport().length());
					pStatement.setString(ii++, reportInfos.get(i).getReport());
					
					di.setIIiii(sta);
					di.setDATA_TIME(TimeUtil.date2String(oTime, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLONGTITUDE(String.valueOf(negOxygenIon.getLatitude()));
					di.setLATITUDE(String.valueOf(negOxygenIon.getLongtitude()));

					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
					di.setDATA_UPDATE_FLAG("000");
					di.setHEIGHT(String.valueOf(stationHeight));
					
					try {
						pStatement.execute();
						if(connection.getAutoCommit() == false)
							connection.commit();
						listDi.add(di);
					} catch (Exception e) {
						di.setPROCESS_STATE("0");//1成功，0失败
						listDi.add(di);
					}
					
				} catch (Exception e) {
					loggerBuffer.append("sql error:" + ((LoggableStatement)pStatement).getQueryString());
					continue;
				}
			}  
			if(pStatement != null) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n Close Statement error: "+e.getMessage());
				}
			}
		} catch (SQLException e) {
			loggerBuffer.append("Database connection error: " + e.getMessage());
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
	 * @return: void      
	 * @throws:
	 */
	private static void execute_sql(List<String> sqls, java.sql.Connection connection, StringBuffer loggerBuffer, String fileN) {
		Statement pStatement = null;
		try {
			connection.setAutoCommit(true);
			pStatement = connection.createStatement();
			for (int i = 0; i < sqls.size(); i++) {
//				pStatement = new LoggableStatement(connection, sqls.get(i));
				try {
					pStatement.execute(sqls.get(i));
				} catch (Exception e) {
					loggerBuffer.append("\n File name: "+fileN
							+"\n " + listDi.get(i).getIIiii() + " " + listDi.get(i).getDATA_TIME()
							+"\n execute sql error: "+sqls.get(i)+"\n "+e.getMessage());
					listDi.get(i).setPROCESS_STATE("0");
				}
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n  Create Statement error: "+e.getMessage());
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
