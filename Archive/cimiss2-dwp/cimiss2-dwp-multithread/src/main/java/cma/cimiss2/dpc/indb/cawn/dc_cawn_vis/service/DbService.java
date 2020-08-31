package cma.cimiss2.dpc.indb.cawn.dc_cawn_vis.service;

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
import cma.cimiss2.dpc.decoder.bean.cawn.VIS;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class DbService {
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
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
	 * @Description: TODO(报文解码入库函数)   
	 * @param  parseResult  存放解码结果
	 * @param  recv_time  报文接收时间   
	 * @param loggerBuffer 
	 * @param filepath 文件路径
	 * @return: DataBaseAction      
	 * @throws:
	 */
	public static DataBaseAction processSuccessReport(ParseResult<VIS> parseResult, Date recv_time, String fileN, StringBuffer loggerBuffer, String filepath) {
		java.sql.Connection connection = null;
		java.sql.Connection reportConnection = null;
		try {
			// 获取数据库连接
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			// 获取解码结果集
			List<VIS> viss = parseResult.getData();
			insertDB(viss, connection, recv_time,fileN,loggerBuffer, filepath);
			@SuppressWarnings("rawtypes")
			List<ReportInfo> reportInfos = parseResult.getReports();
			reportConnection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
			reportInfoToDb(reportInfos, reportConnection, recv_time, loggerBuffer,filepath);  
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
					loggerBuffer.append("\n Close Database connection error: "+e.getMessage());
				}
			}
			
			if(reportConnection != null) {
				try {
					reportConnection.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n Close Database connection error: "+e.getMessage());
				}
			}
		}
	}
	/**
	 * 
	 * @Title: insertDB
	 * @Description: TODO(气溶胶能见度（VIS）资料入库)   
	 * @param viss 待入库对象集合
	 * @param connection 数据库连接
	 * @param recv_time 资料接收接收
	 * @param fileN 
	 * @param loggerBuffer 
	 * @return void 
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static void insertDB(List<VIS> viss, java.sql.Connection connection, Date recv_time, String fileN, StringBuffer loggerBuffer, String filepath){
		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,D_UPDATE_TIME,"
			+ "V01301,V01300,V05001,V06001,V07001,V_ACODE,V04001,V04002,V04003,V04004,"
			+ "V04005,V04006,V04016,V_ITEM_CODE,V02321,V20001_701_01,V20001_701_10,V20311,V_BBB, D_SOURCE_ID) "
			+ "VALUES (?, ?, ?, ?, ?,"
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " ;
		if(connection != null){		
			try {	
				pStatement = new LoggableStatement(connection, sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				connection.setAutoCommit(false);
				List<String> sqls = new ArrayList<>();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");		
				String V_TT = "VIS";
				String info;
				for(int idx = 0; idx < viss.size(); idx ++){
					VIS vis = viss.get(idx);
					double latitude = 999999;
					double longtitude = 999999;
					double height = 999999;
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
						
					Date dataTime = vis.getDataObservationTime();
					String station = vis.getStationNumberChina();
					int ii = 1;
					pStatement.setString(ii++, sdf.format(dataTime)+"_"+station);//记录标识 +"_"+V_TT
					pStatement.setString(ii++, sod_code);//资料标识
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//入库时间
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));//收到时间
					pStatement.setTimestamp(ii++, new Timestamp(dataTime.getTime()));//资料时间
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间
					//+ "V01301,V01300,V05001,V06001,V07001,V_ACODE,V04001,V04002,V04003,V04004,"
					pStatement.setString(ii++, station);
					pStatement.setInt(ii++, Integer.parseInt(StationCodeUtil.stringToAscii(station)));
					info = (String) proMap.get(station + "+01");
					if(info == null) {
						loggerBuffer.append("\n In configuration file, this station does not exist: " + station);
					}
					else{
						String[] infos = info.split(",");						
						if(infos[1].equals("null"))
							loggerBuffer.append("\n In configuration file, longtitude is null!");
						else
							longtitude = Double.parseDouble(infos[1]);
						if(infos[2].equals("null"))
							loggerBuffer.append("\n In configuration file, latitude is null!");
						else
							latitude = Double.parseDouble(infos[2]);
						if(infos[3].equals("null"))
							loggerBuffer.append("\n In configuration file, station height is null!");
						else
							height = Double.parseDouble(infos[3]);
						if(infos[5].equals("null"))
							loggerBuffer.append("\n In configuration file, V_CCCC is null!");
						else 
							adminCode = Integer.parseInt(infos[5]);
					}
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(latitude)));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(longtitude)));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(height)));
					pStatement.setInt(ii++, adminCode);
					pStatement.setInt(ii++, dataTime.getYear() + 1900);
					pStatement.setInt(ii++, dataTime.getMonth() + 1);
					pStatement.setInt(ii++, dataTime.getDate());
					pStatement.setInt(ii++, dataTime.getHours());
					//+ "V04005,V04006,V04016,V_ITEM_CODE,V02321,V20001_701_01,V20001_701_10,V20311,V_BBB) "
					pStatement.setInt(ii++, dataTime.getMinutes());
					pStatement.setInt(ii++, dataTime.getSeconds());
					pStatement.setInt(ii++, 999998); // 观测时间间隔，固定为300s
					pStatement.setInt(ii++, vis.getItemCode());
					pStatement.setInt(ii++, vis.getStateCode());
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(vis.getVisibility_1min()))); //1分钟平均能见度
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(vis.getVisibility_10min()))); //10分钟平均能见度
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(vis.getTrend()))); // 能见度变化趋势
					pStatement.setString(ii++, "000"); //更正报标识
					pStatement.setString(ii++, cts_code);
					
					di.setIIiii(station);
					di.setDATA_TIME(TimeUtil.date2String(dataTime, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(latitude));
					di.setLONGTITUDE(String.valueOf(longtitude));

					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
					di.setDATA_UPDATE_FLAG("000");
					di.setHEIGHT(String.valueOf(height));
					
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
					execute_sql(sqls, connection,fileN, loggerBuffer); // 此中，入库失败的会将对应的DI的ProcessState置为1 // 1:失败，0：成功
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
	 * @Description: TODO(报文信息入库)   
	 * @param  reportInfos 报文列表集合
	 * @param  connection  数据库连接
	 * @param  recv_time   资料时间
	 * @param loggerBuffer 
	 * @return: void      
	 * @throws:
	 */
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public static void reportInfoToDb( List<ReportInfo> reportInfos, java.sql.Connection connection, Date recv_time, StringBuffer loggerBuffer,String filepath) {
		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO  "+StartConfig.reportTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,"
				+ "V_BBB,V_CCCC,V_TT,"
				+ "V01301,V01300,V05001,V06001,V_NCODE,V_ACODE,V04001,V04002,V04003,V04004,V04005,"
				+ "V_LEN,V_REPORT) VALUES"
				+ "(?,?,?,?,?,?,"
				+ " ?,?,?,"
				+ " ?,?,?,?,?,?,?,?,?,?,?,"
				+ " ?,?)";
		try {
			connection.setAutoCommit(true);
			pStatement = new LoggableStatement(connection, sql);
			File file=new File(filepath);
			String fileN=file.getName();
			StatDi di = new StatDi();	
			di.setFILE_NAME_O(fileN);
			di.setDATA_TYPE(sod_report_code);
			di.setDATA_TYPE_1(cts_code);
				
			di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
			di.setPROCESS_START_TIME(TimeUtil.getSysTime());
			di.setFILE_NAME_N(fileN);
			di.setBUSINESS_STATE("1"); //1成功，0失败
			di.setPROCESS_STATE("1");  //1成功，0失败
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String V_TT= "VIS";
			VIS vis = null;
			Date oTime = null;
			String primkey = null;
			String sta;
			String info;
			double latitude = 999999;
			double longtitude = 999999;
			int adminCode = 999999;
			double height=999999;
			for (int i = 0; i < reportInfos.size(); i++) {
				try {
					vis = (VIS) reportInfos.get(i).getT();
					sta = vis.getStationNumberChina();
					oTime = vis.getDataObservationTime();
					primkey = sdf.format(oTime)+"_"+sta+"_"+V_TT;
					int ii = 1;
					pStatement.setString(ii++, primkey);
					pStatement.setString(ii++, sod_report_code);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(oTime.getTime()));

					pStatement.setString(ii++, "000");
					pStatement.setString(ii++, "9999");
					pStatement.setString(ii++, V_TT);
					//	+ "V01301,V01300,V05001,V06001,V_NCODE,V_ACODE,V04001,V04002,V04003,V04004,V04005,"
					pStatement.setString(ii++, sta);
					pStatement.setInt(ii++, Integer.parseInt(StationCodeUtil.stringToAscii(sta)));
					info = (String) proMap.get(sta + "+01");
					if(info == null) {
						loggerBuffer.append("\n  In configuration file, this station does not exist: " + sta);
//						continue ;
					}
					else{
						String[] infos = info.split(",");						
						if(infos[1].equals("null"))
							loggerBuffer.append("\n  In configuration file, longtitude is null!");
						else
							longtitude = Double.parseDouble(infos[1]);
						if(infos[2].equals("null"))
							loggerBuffer.append("\n  In configuration file, latitude is null!");
						else
							latitude = Double.parseDouble(infos[2]);
						if(infos[5].equals("null"))
							loggerBuffer.append("\n  In configuration file, V_CCCC is null!");
						else 
							adminCode = Integer.parseInt(infos[5]);
						if(infos[3].equals("null"))
							height = Double.parseDouble(infos[3]);
					}
					pStatement.setBigDecimal(ii++, new BigDecimal(latitude).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(longtitude).setScale(4, BigDecimal.ROUND_HALF_UP));
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
					
					di.setTT(V_TT);	
					di.setIIiii(sta);
					di.setDATA_TIME(TimeUtil.date2String(oTime, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLONGTITUDE(String.valueOf(latitude));
					di.setLATITUDE(String.valueOf(longtitude));

					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
					di.setDATA_UPDATE_FLAG("000");
					di.setHEIGHT(String.valueOf(height));
					
					try{
						pStatement.execute();
						listDi.add(di);
					}catch (SQLException e) {
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
		}
	}	
	/**
	 * 
	 * @Title: execute_sql   
	 * @Description: TODO(批量入库失败时，采用逐条提交)   
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
//				if(StartConfig.getDatabaseType() == 1) {
//					pStatement.execute("select last_txc_xid()");
//				}
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
			loggerBuffer.append("\n Close Statement error: "+e.getMessage());
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
