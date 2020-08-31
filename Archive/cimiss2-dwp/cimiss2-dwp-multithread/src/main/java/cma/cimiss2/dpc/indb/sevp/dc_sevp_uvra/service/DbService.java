package cma.cimiss2.dpc.indb.sevp.dc_sevp_uvra.service;


import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
import cma.cimiss2.dpc.decoder.bean.sevp.UvMonitorForecastproduct;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.sevp.DecodeUV;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class DbService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	public static String report_sod_code = StartConfig.reportSodCode();
	public static String v_tt = "UVRA";
	public static String V_CCCC = "9999";
	public static int defaultInt = 999999;
		
//	static Map<String, Object> proMap = StationInfo.getProMap();
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}
	public static void main(String[] args) {
		DecodeUV decodeUV=new DecodeUV();
		String filepath = "D:\\UV\\2019022601\\QBZQ_YC022602.URP";
		File file = new File(filepath);
		ParseResult<UvMonitorForecastproduct> parseResult = decodeUV.decodeFile(file);
		DataBaseAction action = null;
		Date recv_time = new Date();
		StringBuffer loggerBuffer = new StringBuffer();

		action = DbService.processSuccessReport(parseResult, file.getName(), filepath, recv_time,loggerBuffer);
		System.out.println("insertDBService over!");
	
	}
	/**
	 * 
	 * @Title: processSuccessReport
	 * @Description:(报文解码入库函数)
	 * @param parseResult 解码结果集
	 * @param fileN 文件对象
	 * @param recv_time 接收时间
	 * @param loggerBuffer 日志对象
	 * @return DataBaseAction 入库状态
	 * @throws：
	 */
	@SuppressWarnings("rawtypes")
	public static DataBaseAction processSuccessReport(ParseResult<UvMonitorForecastproduct> parseResult, String fileN, String filepath,
			Date recv_time, StringBuffer loggerBuffer) {
		java.sql.Connection connection = null;
		java.sql.Connection reportConnection = null;
		try {
			// 获取数据库连接
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			// 获取解码结果集
			List<UvMonitorForecastproduct> list = parseResult.getData();
			insert_db(list, connection, recv_time, loggerBuffer,fileN, filepath);
			
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
	 * @Title: insert_db
	 * @Description: (省级紫外线监测与预报产品批量入库函数)
	 * @param list 待入库对象
	 * @param connection 数据库连接
	 * @param recv_time 接收时间
	 * @param loggerBuffer 日志对象
	 * @param fileN 
	 * @param filepath
	 * @throws：
	 */
	private static void insert_db(List<UvMonitorForecastproduct> list, java.sql.Connection connection, Date recv_time,
			StringBuffer loggerBuffer, String fileN, String filepath) {
		Map<String, Object> proMap = StationInfo.getProMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");	
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,"
				+ "D_UPDATE_TIME,D_DATETIME,V04001_02,V04002_02,V04003_02,V04004_02,"
				+ "V01301,V01300,V05001,V06001,V07001,V14400,V14401,V14256_01,V14256_02,D_SOURCE_ID) VALUES ("
				+ "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?)";
		if(connection != null){		
			try {	
				pStatement = new LoggableStatement(connection, sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				connection.setAutoCommit(false);
				List<String> sqls = new ArrayList<>();
				for(int i = 0; i<list.size();i++){
					StatDi di = new StatDi();					
					di.setFILE_NAME_O(filepath);
					di.setDATA_TYPE(sod_code);
					di.setDATA_TYPE_1(cts_code);
					di.setTT(v_tt);			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(filepath);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1");  //1成功，0失败	
					int ii = 1;
					double latitude = defaultInt;
					double longtitude = defaultInt;
					double statHeight = defaultInt;
					
					String info = (String) proMap.get(list.get(i).getStationNumberChina() + "+01");
					if(info == null) {
						loggerBuffer.append("\n In configuration file, this station does not exist: " + list.get(i).getStationNumberChina());
					}		
					else{
						String[] infos = info.split(",");
						if(!infos[1].equals("null")){
							longtitude = Double.parseDouble(infos[1]);					
						}	
						if(!infos[2].equals("null")){
							latitude = Double.parseDouble(infos[2]);			
						}
						if(!infos[3].equals("null")){
							statHeight = Double.parseDouble(infos[3]);				
						}	
					}
					int year = Integer.parseInt(sdf.format(recv_time).substring(0, 4));//资料观测年
					int month = Integer.parseInt(fileN.substring(7, 9));//资料观测月
					int day = Integer.parseInt(fileN.substring(9, 11));//资料观测日
					int hour = Integer.parseInt(fileN.substring(11, 13));//资料观测时
					if ((hour==2) ||(hour==1)) {
						hour=2;
					}else if ((hour==7) ||(hour==8)) {
						hour=8;
					}else {
						loggerBuffer.append("\n File time error: " + hour);
						continue;
					}
					Calendar c = Calendar.getInstance();
					c.set(Calendar.YEAR, year);
				    c.set(Calendar.MONTH, month-1);
				    c.set(Calendar.DAY_OF_MONTH,day);
				    c.set(Calendar.HOUR_OF_DAY, hour);
				    c.set(Calendar.MINUTE, 0);
				    c.set(Calendar.SECOND, 0);
				    c.set(Calendar.MILLISECOND, 0);
				    if(!TimeCheckUtil.checkTime(c.getTime())){
						infoLogger.info("DataTime out of range! time:"+c.getTime());
						continue;
					}
					
//				    Calendar dt = Calendar.getInstance();
//				    dt.setTime(c.getTime());
//				    dt.add(Calendar.HOUR, 8);
				    
				    Date datatime = c.getTime();//得到资料时间
					String  primary = sdf.format(c.getTime())+"_"+list.get(i).getStationNumberChina();
					pStatement.setString(ii++, primary);//记录标识
					pStatement.setString(ii++, sod_code);//资料标识
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//入库时间 
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));//收到时间 
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间 
					pStatement.setTimestamp(ii++, new Timestamp(c.getTime().getTime()));
//					pStatement.setTimestamp(ii++, new Timestamp(dt.getTime().getTime()));
					pStatement.setBigDecimal(ii++, new BigDecimal(year));//资料观测年
					pStatement.setBigDecimal(ii++, new BigDecimal(month));//资料观测月
					pStatement.setBigDecimal(ii++, new BigDecimal(day));//资料观测日
					pStatement.setBigDecimal(ii++, new BigDecimal(hour));//资料观测时
					pStatement.setString(ii++, list.get(i).getStationNumberChina());//区站号(字符)
					pStatement.setInt(ii++,Integer.parseInt(StationCodeUtil.stringToAscii(list.get(i).getStationNumberChina()))); //区站号(数字)
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(latitude)));//经度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(longtitude)));//纬度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(statHeight)));//测站海拔高度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getUvObserveAverageValue())));//观测平均值
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getUvDailyObserveMaximum())));//日观测最大值
					if (hour==2) {
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getUvPredictedValue())));//资料时间为2的观测值
						pStatement.setString(ii++, "999999");//资料时间为8的观测值
					} else{	
						pStatement.setString(ii++, "999999");//资料时间为2的观测值
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getUvPredictedValue())));//资料时间为8的观测值
					}
					
					pStatement.setString(ii++, cts_code);
					
					di.setIIiii(list.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(datatime, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(latitude));
					di.setLONGTITUDE(String.valueOf(longtitude));
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
					di.setDATA_UPDATE_FLAG(list.get(i).getCorrectsign());
					di.setHEIGHT(String.valueOf(list.get(i).getHeightOfSationGroundAboveMeanSeaLevel()));
					
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
					execute_sql(sqls, connection,loggerBuffer,filepath); // 此中，入库失败的会将对应的DI的ProcessState置为1 // 1:失败，0：成功
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
	 * @Description:(省级紫外线监测与预报产品单条入库函数)
	 * @param sqls sql对象集合
	 * @param connection 数据库连接
	 * @param loggerBuffer 日志对象
	 * @param fileN 
	 * @throws：
	 */
	private static void execute_sql(List<String> sqls, java.sql.Connection connection, StringBuffer loggerBuffer, String fileN) {
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
							+"\n execure sql error: "+sqls.get(i)+"\n "+e.getMessage());
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
	
	/**
	 * 
	 * @Title: reportInfoToDb
	 * @Description:(省级紫外线监测与预报产品入报文库函数)
	 * @param reportInfos 待入报文库对象
	 * @param connection 数据库连接
	 * @param recv_time 接收时间
	 * @param loggerBuffer 日志对象
	 * @param fileN 
	 * @throws：
	 */
	private static void reportInfoToDb(@SuppressWarnings("rawtypes") List<ReportInfo> reportInfos, java.sql.Connection connection, Date recv_time,
			StringBuffer loggerBuffer, String fileN) {
		Map<String, Object> proMap = StationInfo.getProMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");	
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.reportTable() + " "
				+ " (D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V_BBB,V_CCCC,V_TT,V01301, "
				+ " V01300,V05001,V06001,V04001,V04002,V04003,V04004,V04005,V_LEN,V_REPORT,V04001_02,V04002_02,V04003_02,V04004_02,V_AA,V_II,V_MIMJ,D_SOURCE_ID) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			connection.setAutoCommit(false);
			pStatement = new LoggableStatement(connection, sql);
			
			if(StartConfig.getDatabaseType() == 1) {
				pStatement.execute("select last_txc_xid()");
			}
			for (int i = 0; i < reportInfos.size(); i++) {
				try {	
					AgmeReportHeader agmeReportHeader = (AgmeReportHeader) reportInfos.get(i).getT();	
					String stationNumberChina = agmeReportHeader.getStationNumberChina();//区站号
					double latitude = defaultInt;
					double longtitude = defaultInt;
					double ACODE =defaultInt;
					String info = (String) proMap.get(stationNumberChina + "+01");
					if(info == null) {
						loggerBuffer.append("\n  In configuration file, this station does not exist: " + stationNumberChina);
					}		
					else{
						String[] infos = info.split(",");
						if(!infos[1].equals("null")){
							longtitude = Double.parseDouble(infos[1]);					
						}
						if(!infos[2].equals("null")){
							latitude = Double.parseDouble(infos[2]);			
						}
						if(!infos[5].equals("null")){
							ACODE = Double.parseDouble(infos[5]);				
						}	
					}
					int year = Integer.parseInt(sdf.format(recv_time).substring(0, 4));//资料观测年
					int month = Integer.parseInt(fileN.substring(7, 9));//资料观测月
					int day = Integer.parseInt(fileN.substring(9, 11));//资料观测日
					int hour = Integer.parseInt(fileN.substring(11, 13));//资料观测时
					if ((hour==2) ||(hour==1)) {
						hour=2;
					}else if ((hour==7) ||(hour==8)) {
						hour=8;
					}else {
						loggerBuffer.append("\n File time error: " + hour);
						continue;
					}
					Calendar c = Calendar.getInstance();
					c.set(Calendar.YEAR, year);
				    c.set(Calendar.MONTH, month-1);
				    c.set(Calendar.DAY_OF_MONTH,day);
				    c.set(Calendar.HOUR_OF_DAY, hour);
				    c.set(Calendar.MINUTE, 0);
				    c.set(Calendar.SECOND, 0);
				    c.set(Calendar.MILLISECOND, 0);
				    
//				    Calendar dt = Calendar.getInstance();
//				    dt.setTime(dt.getTime());;
//				    dt.add(Calendar.HOUR, 8);
				    
					String  primary = sdf.format(c.getTime())+"_"+stationNumberChina;
					int ii = 1;
					pStatement.setString(ii++, primary);
					pStatement.setString(ii++, report_sod_code);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(c.getTime().getTime()));
					pStatement.setString(ii++, "000");//V_BBB,
					pStatement.setString(ii++,V_CCCC);//V_CCCC,编报(加工)中心，确认录入站号
					pStatement.setString(ii++, v_tt);//V_TT,报告类别
					pStatement.setString(ii++, stationNumberChina);// 区站号(字符)
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(Integer.parseInt(StationCodeUtil.stringToAscii(stationNumberChina)))));// 区站号(数字)
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(latitude)));// 纬度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(longtitude)));// 经度
//					pStatement.setBigDecimal(ii++, new BigDecimal(2250));// V_NCODE,国家代码
//					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ACODE)));// V_ACODE
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(year)));//资料观测年
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(month)));//资料观测月
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(day)));//资料观测日
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(hour)));//资料观测时
					pStatement.setBigDecimal(ii++, new BigDecimal(0));//资料观测时
					pStatement.setInt(ii++, reportInfos.get(i).getReport().length());
					pStatement.setString(ii++, reportInfos.get(i).getReport());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(year)));//资料观测年
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(month)));//资料观测月
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(day)));//资料观测日
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(hour)));//资料观测时
					pStatement.setString(ii++, "99");//V_AA
					pStatement.setString(ii++, "99");//V_II
					pStatement.setString(ii++, "99");//V_MIMJ
					pStatement.setString(ii++, cts_code);//D_SOURCE_ID
					
					pStatement.execute();
					connection.commit();
				} catch (Exception e) {
					loggerBuffer.append("sql error:" + ((LoggableStatement)pStatement).getQueryString());
					continue;
				}
			} // end for 
			if(pStatement != null) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					loggerBuffer.append("\nClose Statement error: "+e.getMessage());
				}
			}
		} catch (SQLException e) {
			loggerBuffer.append("Database connection error: " + e.getMessage());
			return;
		}
			
	}
}
