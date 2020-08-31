package cma.cimiss2.dpc.indb.surf.dc_surf_mwr_soil.service;

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
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.surf.SurfaceObservationMwrsoil;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class DbService {
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	public static String sod_report = StartConfig.reportSodCode();
	public static String V_TT = "MWRSoil";
	public static String V_CCCC = "9999";  //资料无本字段，赋默认值
	public static int defaultInt = 999999;
	public static String V_BBB = "000";
//	static Map<String, Object> proMap = StationInfo.getProMap();
	
	public BlockingQueue<StatDi> getDiQueues(){
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues){
		DbService.diQueues = diQueues;
	}
	
	/**
	 * 
	 * @Title: processSuccessReport   
	 * @Description: TODO(水利部土壤墒情资料处理)   
	 * @param parseResult
	 * @param recv_time
	 * @param fileN
	 * @param filepath
	 * @param loggerBuffer 
	 * @return DataBaseAction      
	 * @throws：
	 */
	public static DataBaseAction processSuccessReport(ParseResult<SurfaceObservationMwrsoil> parseResult, Date recv_time, String fileN, String filepath, StringBuffer loggerBuffer) {
		java.sql.Connection connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
		java.sql.Connection report_connection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
		@SuppressWarnings("rawtypes")
		List<ReportInfo> reportInfos = parseResult.getReports();
		reportInfoToDb(reportInfos, report_connection, recv_time, loggerBuffer);  
		
		List<SurfaceObservationMwrsoil> mwrSoils = parseResult.getData();
		insertDB(mwrSoils, connection, recv_time, fileN, filepath, loggerBuffer);
	
		for (int j = 0; j < listDi.size(); j++) {
			diQueues.offer(listDi.get(j));
		}
		listDi.clear();
		try {
			if(connection != null)
				connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			loggerBuffer.append("\n Database connection close failed："+e.getMessage());
		}
		try{
			if(report_connection != null)
				report_connection.close();
		}catch (Exception e) {
			loggerBuffer.append("\n Database connection close failed："+e.getMessage());
		}
		return DataBaseAction.SUCCESS;
	}

	/**
	 * 
	 * @Title: insertDB   
	 * @Description: TODO(水利部土壤墒情资料入库)   
	 * @param mwrSoils
	 * @param connection
	 * @param recv_time
	 * @param fileN
	 * @param filepath void      
	 * @param loggerBuffer 
	 * @throws：
	 */
	private static void insertDB(List<SurfaceObservationMwrsoil> mwrSoils, java.sql.Connection connection, Date recv_time, String fileN,String filepath, StringBuffer loggerBuffer) {
		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,D_UPDATE_TIME,"
				+ "V_FILE_NAME_SOURCE,v01301,v01300,v05001,v06001,v07001,v07031,v02001,v02301,v_acode,"
				+ "v04001,v04002,v04003,v04004,"
				+ "V71110,V71655_010,V71655_020,V71655_030,V71655_040,V71655_060,V71655_080,V71655_100, V_BBB, D_SOURCE_ID) "
				+ "VALUES (?,?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " ;
		if(connection != null){		
			try {	
				pStatement = new LoggableStatement(connection, sql);
				connection.setAutoCommit(false);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				List<String> sqls = new ArrayList<>();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");		
				SurfaceObservationMwrsoil mwrSoil = new SurfaceObservationMwrsoil();
				for(int idx = 0; idx < mwrSoils.size(); idx ++){
					mwrSoil = mwrSoils.get(idx);
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
					String stat = mwrSoil.getStationCode();
							
					String info = (String) proMap.get(stat + "+01");
					int adminCode = 999999;
					double latitude = 999999.0;
					double longtitude = 999999.0;
					double statHeight = 999999.0;
					int stationLevel = 999999;
					int stationType = 999999;
					double airPressureSensorHeightOboveSeaLevel = 999999.0;
					double fieldWaterHoldingCapacity = 999999.0; // 田间持水量
					if(info == null) {
						loggerBuffer.append("\n In configuration file, this station does not exist! " + stat);
//						continue ;
					}
					else{
						String[] infos = info.split(",");
						if(!infos[5].equals("null"))  //中国行政区域代码
							adminCode = Integer.parseInt(infos[5]);
						if(!infos[6].equals("null"))
							stationLevel = Integer.parseInt(infos[6]); // 台站级别
						if(infos[1].equals("null")){  // 经度
							loggerBuffer.append("\n In configuration file, longtitude is null!");
//							continue ;
						}
						else
							longtitude = Double.parseDouble(infos[1]);
						if(infos[2].equals("null")){  //纬度
							loggerBuffer.append("\n In configuration file, latitude is null!");
//							continue;
						}
						else
							latitude = Double.parseDouble(infos[2]);
						if(!infos[3].equals("null"))
							statHeight = Double.parseDouble(infos[3]);
						if(!infos[8].equals("null"))
							fieldWaterHoldingCapacity = Double.parseDouble(infos[8]);
					}
					int ii = 1;
					pStatement.setString(ii++, sdf.format(mwrSoil.getObservationTime())+"_"+mwrSoil.getStationCode());
					pStatement.setString(ii++, sod_code);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(mwrSoil.getObservationTime().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					
					pStatement.setString(ii++, fileN);
					pStatement.setString(ii++, mwrSoil.getStationCode());
					pStatement.setInt(ii++, stationNumberN);
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(latitude))); // 纬度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(longtitude))); // 经度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(statHeight))); // 测站高度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(airPressureSensorHeightOboveSeaLevel))); //气压传感器海拔高度
					pStatement.setInt(ii++, stationType); // 测站类型
					pStatement.setInt(ii++, stationLevel); // 台站级别
					pStatement.setInt(ii++, adminCode);
					
					pStatement.setInt(ii++, mwrSoil.getObservationTime().getYear() + 1900);
					pStatement.setInt(ii++, mwrSoil.getObservationTime().getMonth() + 1);
					pStatement.setInt(ii++, mwrSoil.getObservationTime().getDate());
					pStatement.setInt(ii++, mwrSoil.getObservationTime().getHours());
					
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fieldWaterHoldingCapacity)));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(mwrSoil.getRateOfWaterContent_10cm())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(mwrSoil.getRateOfWaterContent_20cm())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(mwrSoil.getRateOfWaterContent_30cm())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(mwrSoil.getRateOfWaterContent_40cm())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(mwrSoil.getRateOfWaterContent_60cm())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(mwrSoil.getRateOfWaterContent_80cm())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(mwrSoil.getRateOfWaterContent_100cm())));
					pStatement.setString(ii++, "000");
					pStatement.setString(ii++, cts_code);
					
					di.setIIiii(mwrSoil.getStationCode());
					di.setDATA_TIME(TimeUtil.date2String(mwrSoil.getObservationTime(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(latitude));
					di.setLONGTITUDE(String.valueOf(longtitude));
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
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
					execute_sql(sqls, connection, filepath, loggerBuffer); // 此中，入库失败的会将对应的DI的ProcessState置为1 // 1:失败，0：成功
					loggerBuffer.append("\n Batch commint failed："+filepath);
				}
			}catch (SQLException e) {
				loggerBuffer.append("\n Create statement failed: "+e.getMessage());
			}finally {
				if(pStatement != null) {
					try {
						pStatement.close();
					} catch (SQLException e) {
						loggerBuffer.append("\n Close statement failed: "+e.getMessage());
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
	@SuppressWarnings({ "rawtypes" })
	public static void reportInfoToDb( List<ReportInfo> reportInfos, java.sql.Connection connection, Date recv_time, StringBuffer loggerBuffer) {
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
			if(StartConfig.getDatabaseType() == 1) {
				pStatement.execute("select last_txc_xid()");
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			for (int i = 0; i < reportInfos.size(); i++) {
				try {
					SurfaceObservationMwrsoil mwrSoil= (SurfaceObservationMwrsoil) reportInfos.get(i).getT();
					String stat = mwrSoil.getStationCode();
					String info = (String) proMap.get(stat + "+01");
					int adminCode = 999999;
					double longtitude = 999999.0;
					double latitude = 999999.0;
					if(info == null) {
						loggerBuffer.append("\n In configuration file, this station does not exist!" + stat);
						continue ;
					}
					else{
						String[] infos = info.split(",");
						if(!infos[5].equals("null"))
							adminCode = Integer.parseInt(infos[5]);
						if(infos[1].equals("null")){
							loggerBuffer.append("\n In configuration file, longtitude is null!");
							continue;
						}
						else
							longtitude = Double.parseDouble(infos[1]);
						if(infos[2].equals("null")){
							loggerBuffer.append("\n In configuration file, latitude is null!");
							continue;
						}
						else
							latitude = Double.parseDouble(infos[2]);
					}
					String primkey = sdf.format(mwrSoil.getObservationTime())+"_"+stat+"_"+V_TT;
					int ii = 1;
					pStatement.setString(ii++, primkey);
					pStatement.setString(ii++, sod_report);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(mwrSoil.getObservationTime().getTime()));
					
					pStatement.setString(ii++, V_BBB);
					pStatement.setString(ii++, V_CCCC);
					pStatement.setString(ii++, V_TT);
					pStatement.setString(ii++, mwrSoil.getStationCode());
					pStatement.setInt(ii++, defaultInt);    //测站（数字型）
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(latitude)));   // 纬度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(longtitude))); // 经度
					pStatement.setInt(ii++, defaultInt); // V_NCODE 国家代码
					pStatement.setInt(ii++, adminCode); //   V_ACODE 中国行政区代码
					pStatement.setInt(ii++, mwrSoil.getObservationTime().getYear() + 1900);
					pStatement.setInt(ii++, mwrSoil.getObservationTime().getMonth() + 1);
					pStatement.setInt(ii++, mwrSoil.getObservationTime().getDate());
					pStatement.setInt(ii++, mwrSoil.getObservationTime().getHours());
					pStatement.setInt(ii++, 0);
					pStatement.setInt(ii++, reportInfos.get(i).getReport().length());
					pStatement.setString(ii++, reportInfos.get(i).getReport());
					
					pStatement.execute();
					if(connection.getAutoCommit() == false)
						connection.commit();
				} catch (Exception e) {
					loggerBuffer.append("sql error:" + ((LoggableStatement)pStatement).getQueryString() + "\n" +e.getMessage());
					continue;
				}
			} 
			if(pStatement != null) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n Close statement failed: "+e.getMessage());
				}
			}
		} catch (SQLException e) {
			loggerBuffer.append("Database connection failed: " + e.getMessage());
			return;
		}
	}
	/**
	 * 
	 * @Title: execute_sql   
	 * @Description: TODO(批量入库失败时，采用逐条提交)   
	 * @param  sqls 待执行的查询语句
	 * @param  connection      数据库连接
	 * @param loggerBuffer 
	 * @return: void      
	 * @throws:
	 */
	private static void execute_sql(List<String> sqls, java.sql.Connection connection, String fileN, StringBuffer loggerBuffer) {
		Statement pStatement = null;
		try {
			pStatement = connection.createStatement();
			for (int i = 0; i < sqls.size(); i++) {
//				pStatement = new LoggableStatement(connection, sqls.get(i));
				try {
					pStatement.execute(sqls.get(i));
					connection.commit();
				} catch (Exception e) {
					loggerBuffer.append("\n File name："+fileN
							+"\n " + listDi.get(i).getIIiii() + " " + listDi.get(i).getDATA_TIME()
							+"\n execute sql failed："+sqls.get(i)+"\n "+e.getMessage());
					listDi.get(i).setPROCESS_STATE("0");
				}
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n Create statement failed: "+e.getMessage());
		}finally {
			if(pStatement != null) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n Close statement failed: "+e.getMessage());
				}
			}
		}		
		
	}

}
