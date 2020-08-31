package cma.cimiss2.dpc.indb.surf.dc_surf_kppr.service;

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
import java.util.regex.Pattern;

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
import cma.cimiss2.dpc.decoder.bean.surf.SurfaceObservationKppr;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class DbService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	private static String cts_code = StartConfig.ctsCode();
	private static String V_CCCC = "DKPY";  // 区域代码
 	private static String V_BBB = "000"; 
	private static String sod_code = StartConfig.sodCode();
	private static String V_TT = "朝鲜降水";    // 报文资料类别
//	static Map<String, Object> proMap = StationInfo.getProMap();
	private static String sod_report_code = StartConfig.reportSodCode();
	
	public static void setDiQueues(BlockingQueue<StatDi> diQueues){
		DbService.diQueues = diQueues;
	}
	public static BlockingQueue<StatDi> getDiQueues(){
		return diQueues;
	}
	/**
	 * 
	 * @Title: processSuccessReport   
	 * @Description: TODO(朝鲜降水数据的处理)   
	 * @param  parseResult 解码结果集
	 * @param  filepath 解码文件路径 
	 * @param  recv_time    资料接收时间
	 * @return: DataBaseAction      
	 * @throws:
	 */
	public static DataBaseAction processSuccessReport(ParseResult<SurfaceObservationKppr> parseResult, Date recv_time, String fileN, String filepath) {
		java.sql.Connection connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
		java.sql.Connection report_connection = ConnectionPoolFactory.getInstance().getConnection("cimiss");

		if(connection != null || report_connection != null) {
			@SuppressWarnings("rawtypes")
			List<ReportInfo> reportInfos = parseResult.getReports();
			reportInfoToDb(reportInfos, report_connection, recv_time);  
			
			List<SurfaceObservationKppr> kpprs = parseResult.getData();
			insertDB(kpprs, connection, recv_time, fileN, filepath);
			
			for (int j = 0; j < listDi.size(); j++) {
				diQueues.offer(listDi.get(j));
			}
			listDi.clear();
		}
		
		return DataBaseAction.SUCCESS;
	}
	
	/**
	 * 
	 * @Title: insertDB   
	 * @Description: TODO(朝鲜降雨资料入库)   
	 * @param  kpprs 待入库对象集合
	 * @param  connection 数据库连接
	 * @param  recv_time      资料接收时间
	 * @return: void      
	 * @throws:
	 */
	private static void insertDB(List<SurfaceObservationKppr> kpprs, java.sql.Connection connection, Date recv_time, String fileN, String filepath) {
		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,D_UPDATE_TIME,"
				+ "v_file_name_source,v01301,v01300,v05001,v06001,v07001,v07031,v02001,v02301,v_acode,"
				+ "v04001,v04002,v04003,v04004,v13019,v13023,V_BBB,D_SOURCE_ID) "
				+ "VALUES (?,?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, ?, ?) " ;
		if(connection != null){		
			try {	
				pStatement = new LoggableStatement(connection, sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				List<String> sqls = new ArrayList<>();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");		
				SurfaceObservationKppr kppr = new SurfaceObservationKppr();
				for(int idx = 0; idx < kpprs.size(); idx ++){
					kppr = kpprs.get(idx);
					StatDi di = new StatDi();	
					di.setFILE_NAME_O(filepath);
					di.setDATA_TYPE(sod_code);
					di.setDATA_TYPE_1(cts_code);
					di.setTT(V_TT);			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(filepath);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1");  //1成功，0失败	
					
					int stationNumberN = 999999;
					String stat = kppr.getStationNumberChina();
					if (Pattern.matches("\\d{5}", stat))
						stationNumberN = Integer.parseInt(stat);
					else
						stationNumberN = Integer.parseInt(StationCodeUtil.stringToAscii(stat));
							
					String info = (String) proMap.get(stat + "+01");
					int adminCode = 999999;
					double latitude = 999999.0;
					double longtitude = 999999.0;
					double statHeight = 999999.0;
					double airPressureSensorHeightOboveSeaLevel = 999999.0; 
					int stationLevel = 999999;
					int stationType = 999999;
					if(info == null) {
						infoLogger.error("\n In configuration file, this station does not exist!" + stat);
					}
					else{
						String[] infos = info.split(",");
						if(!infos[5].equals("null"))
							adminCode = Integer.parseInt(infos[5]);
						if(!infos[6].equals("null"))
							stationLevel = Integer.parseInt(infos[6]); 
						if(!infos[7].equals("null"))
							stationType = Integer.parseInt(infos[7]); 
						if(infos[1].equals("null")){
							infoLogger.error("\n In configuration file, longtitude is null!");
							 
						}
						else
							longtitude = Double.parseDouble(infos[1]);
						if(infos[2].equals("null")){
							infoLogger.error("\n In configuration file, latitude is null!");
							
						}
						else
							latitude = Double.parseDouble(infos[2]);
						if(!infos[3].equals("null"))
							statHeight = Double.parseDouble(infos[3]);
					}
					int ii = 1;
					pStatement.setString(ii++, sdf.format(kpprs.get(idx).getObservationTime())+"_"+kppr.getStationNumberChina());
					pStatement.setString(ii++, sod_code);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(kppr.getObservationTime().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					
					pStatement.setString(ii++, fileN);
					pStatement.setString(ii++, kppr.getStationNumberChina());
					pStatement.setInt(ii++, stationNumberN);
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(latitude))); // 纬度
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(longtitude))); // 经度
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(statHeight))); // 测站高度
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(airPressureSensorHeightOboveSeaLevel))); //气压传感器海拔高度
					pStatement.setInt(ii++, stationType); // 测站类型
					pStatement.setInt(ii++, stationLevel); // 台站级别
					pStatement.setInt(ii++, adminCode);
					
					pStatement.setInt(ii++, kppr.getObservationTime().getYear() + 1900);
					pStatement.setInt(ii++, kppr.getObservationTime().getMonth() + 1);
					pStatement.setInt(ii++, kppr.getObservationTime().getDate());
					pStatement.setInt(ii++, kppr.getObservationTime().getHours());
					pStatement.setInt(ii++, kppr.getFKPPR_V13019());
					pStatement.setInt(ii++, kppr.getFKPPR_V13023());
					pStatement.setString(ii++, "000");
					pStatement.setString(ii++, cts_code);
					
					di.setIIiii(kppr.getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(kppr.getObservationTime(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(latitude));
					di.setLONGTITUDE(String.valueOf(longtitude));
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(fileN).length()));
					di.setDATA_UPDATE_FLAG("000");
					di.setHEIGHT(String.valueOf(statHeight));
					
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
					connection.rollback();
					execute_sql(sqls, connection, filepath); // 此中，入库失败的会将对应的DI的ProcessState置为1 // 1:失败，0：成功
					infoLogger.error("\n Batch commit failed: "+filepath);
				}
			}catch (SQLException e) {
				infoLogger.error("\n Create Statement failed: "+e.getMessage());
			}finally {
				if(pStatement != null) {
					try {
						pStatement.close();
					} catch (SQLException e) {
						infoLogger.error("\n Close Statement failed: "+e.getMessage());
					}
				}
				try {
					if(connection != null)
						connection.close();
				} catch (SQLException e1) {
					infoLogger.error("\n Close connection failed: "+e1.getMessage());
				}
			}
		} 
		else{
			infoLogger.error("\n Close connection failed!");
		}
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
	private static void reportInfoToDb( List<ReportInfo> reportInfos, java.sql.Connection connection, Date recv_time) {
		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.reportTable()+" (D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,"
				+ "V_BBB,V_CCCC,V_TT,V01301,V01300,V05001,V06001,V_NCODE,V_ACODE,"
				+ "V04001,V04002,V04003,V04004,V04005,"
				+ "V_LEN,V_REPORT) VALUES"
				+ "(?,?,?,?,?,?,"
				+ " ?,?,?,?,?,?,?,?,?,"
				+ " ?,?,?,?,?,"
				+ " ?,?)";
		try {
			pStatement = new LoggableStatement(connection, sql);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			for (int i = 0; i < reportInfos.size(); i++) {
				try {
					SurfaceObservationKppr kppr = (SurfaceObservationKppr) reportInfos.get(i).getT();
					String stat = kppr.getStationNumberChina();
					String info = (String) proMap.get(stat + "+01");
					int adminCode = 999999;
					double longtitude = 999999.0;
					double latitude = 999999.0;
					int countryCode = 999999;
					if(info == null) {
						infoLogger.error("\n In configuration file, this station does not exist!" + stat);
						
					}
					else{
						String[] infos = info.split(",");
						if(!infos[5].equals("null"))
							adminCode = Integer.parseInt(infos[5]);
						if(infos[1].equals("null")){
							infoLogger.error("\n In configuration file, longtitude is null!");
			
						}
						else
							longtitude = Double.parseDouble(infos[1]);
						if(infos[2].equals("null")){
							infoLogger.error("\n In configuration file, latitude is null!");
					
						}
						else
							latitude = Double.parseDouble(infos[2]);
						if(!infos[4].equals("null"))
							countryCode = Integer.parseInt(infos[4]);
					}
					String primkey = sdf.format(kppr.getObservationTime())+"_"+stat+"_"+V_TT;
					int ii = 1;
					pStatement.setString(ii++, primkey);
					pStatement.setString(ii++, sod_report_code);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(kppr.getObservationTime().getTime()));
					
					pStatement.setString(ii++, V_BBB);
					pStatement.setString(ii++, V_CCCC);
					pStatement.setString(ii++, V_TT);
					pStatement.setString(ii++, kppr.getStationNumberChina());
					pStatement.setInt(ii++, Integer.parseInt(StationCodeUtil.stringToAscii(stat)));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(latitude)));   // 纬度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(longtitude))); // 经度
					pStatement.setInt(ii++, countryCode); // V_NCODE 国家代码
					pStatement.setInt(ii++, adminCode); //   V_ACODE 行政区划
					pStatement.setInt(ii++, kppr.getObservationTime().getYear() + 1900);
					pStatement.setInt(ii++, kppr.getObservationTime().getMonth() + 1);
					pStatement.setInt(ii++, kppr.getObservationTime().getDate());
					pStatement.setInt(ii++, kppr.getObservationTime().getHours());
					pStatement.setInt(ii++, kppr.getObservationTime().getMinutes());
					pStatement.setInt(ii++, reportInfos.get(i).getReport().length());
					pStatement.setString(ii++, reportInfos.get(i).getReport());
					
					pStatement.execute();
					connection.commit();
				} catch (Exception e) {
					infoLogger.error("sql error:" + ((LoggableStatement)pStatement).getQueryString());
					continue;
				}
			}
			
		} catch (SQLException e) {
			infoLogger.error("DataBase connection failed: " + e.getMessage());
		}finally {
			if(pStatement != null) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					infoLogger.error("\n Close Statement failed: "+e.getMessage());
				}
			}
			try {
				if(connection != null)
					connection.close();
			} catch (SQLException e1) {
				infoLogger.error("\n Close connection failed: "+e1.getMessage());
			}
		}
	}	
	
	/**
	 * 
	 * @Title: execute_sql   
	 * @Description: TODO(批量入库失败时，采用逐条提交)   
	 * @param  sqls 待执行的查询语句集合
	 * @param connection    数据库连接
	 * @return: void      
	 * @throws:
	 */
	private static void execute_sql(List<String> sqls, java.sql.Connection connection,String filepath) {
		Statement pStatement = null;
		try {
			connection.setAutoCommit(true);
			pStatement = connection.createStatement();
			for (int i = 0; i < sqls.size(); i++) {
				//pStatement = new LoggableStatement(connection, sqls.get(i));
				
				try {
					pStatement.execute(sqls.get(i));
				} catch (Exception e) {
					infoLogger.error("\n file name："+filepath
							+"\n " + listDi.get(i).getIIiii() + " " + listDi.get(i).getDATA_TIME()
							+"\n execute sql error："+sqls.get(i)+"\n "+e.getMessage());
					listDi.get(i).setPROCESS_STATE("0");
				}
			}
		} catch (SQLException e) {
			infoLogger.error("\n Create Statement failed: "+e.getMessage());
		}finally {
			if(pStatement != null) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					infoLogger.error("\n Close Statement failed: "+e.getMessage());
				}
			}
		}		
		
	}

}
