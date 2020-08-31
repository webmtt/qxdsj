package cma.cimiss2.dpc.indb.surf.dc_surf_mwr_pre.service;

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
import cma.cimiss2.dpc.decoder.bean.surf.SurfaceObservationMwrpre;
import cma.cimiss2.dpc.decoder.surf.DecodeMWRPre;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class DbService {
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	public static String sod_report = StartConfig.reportSodCode();
	
	public static String V_TT = "MWRPre"; 
	public static String V_CCCC = "9999";  //资料无本字段
	public static int defaultInt = 999999;
	public static String V_BBB = "000";
//	static Map<String, Object> proMap = StationInfo.getProMap();
	
	public static void setDiQueues(BlockingQueue<StatDi> diQueue){
		DbService.diQueues = diQueue;
	}
	/**
	 * 
	 * @Title: processSuccessReport   
	 * @Description: TODO(水利部降水资料处理)   
	 * @param  parseResult  解码结果
	 * @param  filepath 解析的文件
	 * @param  recv_time    资料接收时间 
	 * @return: DataBaseAction      
	 * @throws:
	 */
	public static DataBaseAction processSuccessReport(ParseResult<SurfaceObservationMwrpre> parseResult, Date recv_time, String fileN, String filepath) {
		java.sql.Connection connection = null;
		java.sql.Connection report_connection = null;
		connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
		report_connection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
		
		List<SurfaceObservationMwrpre> mwrPres = parseResult.getData();
		int batchSize = 100;
		for (int n = 0; n < mwrPres.size()/batchSize+1; n++) {
			List<SurfaceObservationMwrpre> list = null;
			if(n * batchSize + batchSize > mwrPres.size()) {
				list = mwrPres.subList(n*batchSize, mwrPres.size());
			}else {
				list = mwrPres.subList(n*batchSize, n*batchSize+batchSize);
			}
			insertDB(list, connection, recv_time, fileN, filepath);
		}
		@SuppressWarnings("rawtypes")
		List<ReportInfo> reportInfos = parseResult.getReports();
		reportInfoToDb(reportInfos, report_connection, recv_time,filepath);  
		
		for (int j = 0; j < listDi.size(); j++) {
			diQueues.offer(listDi.get(j));
		}
		listDi.clear();
		try {
			if(connection != null)
				connection.close();
		} catch (SQLException e) {
			infoLogger.error("\n Database connection close failed：" + e.getMessage());
		}
		try{
			if(report_connection != null)
				report_connection.close();
		}catch (Exception e) {
			infoLogger.error("\n Database connection close failed："+e.getMessage());
		}
		return DataBaseAction.SUCCESS;
	}

	/**
	 * 
	 * @Title: insertDB   
	 * @Description: TODO(水利部降水资料入库)   
	 * @param  kpprs 待入库对象
	 * @param  connection 数据库连接
	 * @param recv_time      资料接收时间
	 * @return: void      
	 * @throws:
	 */
	private static void insertDB(List<SurfaceObservationMwrpre> mwrPres, java.sql.Connection connection, Date recv_time, String fileN, String filepath) {
		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,D_UPDATE_TIME,"
				+ "V_PADFN,V_PPTFN,v01301,v01300,v05001,v06001,v07001,v07031,v02001,v02301,v_acode,"
				+ "v04001,v04002,v04003,v04004,V13021,V13023,V13019,V_BBB,V_TIMESCALE, D_SOURCE_ID) "
				+ "VALUES (?,?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
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
				SurfaceObservationMwrpre mwrPre = new SurfaceObservationMwrpre();
				for(int idx = 0; idx < mwrPres.size(); idx ++){
					mwrPre = mwrPres.get(idx);
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
					
					String stat = mwrPre.getStationNumberChina();
					String info = (String) proMap.get(stat + "+01");
					int adminCode = 999999;
					double latitude = 999999.0;
					double longtitude = 999999.0;
					double statHeight = 999999.0;
					int stationLevel = 999999;
					int stationType = 999999;
					double airPressureSensorHeightOboveSeaLevel = 999999.0;
					if(stat.equals("999998")||fileN.contains("DAY") || fileN.contains("HOUR")){
						latitude = mwrPre.getLatitude();
						longtitude = mwrPre.getLongtitude();
					}
					else if(info == null) {
						infoLogger.error("\n In configuration file, this station does not exist! " + stat);
//						continue ;
					}
					else{
						String[] infos = info.split(",");
						if(!infos[5].equals("null"))  //中国行政区域代码
							adminCode = Integer.parseInt(infos[5]);
						if(!infos[6].equals("null"))
							stationLevel = Integer.parseInt(infos[6]); // 台站级别
						if(infos[1].equals("null")){  // 经度
							infoLogger.error("\n In configuration file, longtitude is null!");
//							continue ;
						}
						else
							longtitude = Double.parseDouble(infos[1]);
						if(infos[2].equals("null")){  //纬度
							infoLogger.error("\n In configuration file, latitude is null!");
//							continue;
						}
						else
							latitude = Double.parseDouble(infos[2]);
						if(!infos[3].equals("null"))
							statHeight = Double.parseDouble(infos[3]);//测站高度
					}
					
					int ii = 1;
					pStatement.setString(ii++, sdf.format(mwrPre.getObservationTime())+"_"+stat +"_"+String.valueOf((int)(latitude*1e6))+"_"+String.valueOf((int)(longtitude*1e6))+"_"+mwrPre.getTimeScale());
					pStatement.setString(ii++, sod_code);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(mwrPre.getObservationTime().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					//fileN.contains("PAD") || fileN.contains("pad") || 
					if(fileN.contains("D") || fileN.contains("d")){ // 24降雨文件
						pStatement.setString(ii++, fileN);
						pStatement.setString(ii++, "999999");
					}
					else{  //6小时降雨文件
						pStatement.setString(ii++, "999999");
						pStatement.setString(ii++, fileN);
					}
					pStatement.setString(ii++, stat);
					int stationNumberN=999998;
					if(!"999998".equals(stat)){
					stationNumberN = Integer.parseInt(StationCodeUtil.stringToAscii(stat));
					}
					pStatement.setBigDecimal(ii++, new BigDecimal(stationNumberN));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(latitude))); // 纬度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(longtitude))); // 经度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(statHeight))); // 测站高度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(airPressureSensorHeightOboveSeaLevel))); //气压传感器海拔高度
					pStatement.setInt(ii++, stationType); // 测站类型
					pStatement.setInt(ii++, stationLevel); // 台站级别
					pStatement.setInt(ii++, adminCode);
					
					pStatement.setInt(ii++, mwrPre.getObservationTime().getYear() + 1900);
					pStatement.setInt(ii++, mwrPre.getObservationTime().getMonth() + 1);
					pStatement.setInt(ii++, mwrPre.getObservationTime().getDate());
					pStatement.setInt(ii++, mwrPre.getObservationTime().getHours());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(mwrPre.getHourlyRainfall())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(mwrPre.getDailyRainfall())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(mwrPre.getOneHourRainfall())));
					pStatement.setString(ii++, "000");
					pStatement.setString(ii++, mwrPre.getTimeScale());
					pStatement.setString(ii++, cts_code);
					
					di.setIIiii(stat);
					di.setDATA_TIME(TimeUtil.date2String(mwrPre.getObservationTime(), "yyyy-MM-dd HH:mm"));
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
					System.out.println("------------"+idx);
					sqls.add(((LoggableStatement)pStatement).getQueryString()); // 批量入库冲突时，单条入库
					System.out.println(((LoggableStatement)pStatement).getQueryString());
					listDi.add(di);
				} // end for
				try{
					pStatement.executeBatch();
					connection.commit();
					sqls.clear();
				}catch(SQLException e){
					pStatement.clearParameters();
					pStatement.clearBatch();
					execute_sql(sqls, connection, filepath); // 此中，入库失败的会将对应的DI的ProcessState置为1 // 1:失败，0：成功
					infoLogger.error("\n Batch commint failed："+filepath);
				}
			}catch (SQLException e) {
				infoLogger.error("\n Create statement failed: "+e.getMessage());
			}finally {
				if(pStatement != null) {
					try {
						pStatement.close();
					} catch (SQLException e) {
						infoLogger.error("\n Cloase statement failed: "+e.getMessage());
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
	 * @param recv_time   资料时间
	 * @return: void      
	 * @throws:
	 */
	@SuppressWarnings({ "rawtypes" })
	public static void reportInfoToDb( List<ReportInfo> reportInfos, java.sql.Connection connection, Date recv_time,String filepath) {
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
			File file=new File(filepath);
			String fileN=file.getName();
			for (int i = 0; i < reportInfos.size(); i++) {
				try {
					StatDi di = new StatDi();	
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(sod_report);
					di.setDATA_TYPE_1(cts_code);
					di.setTT(V_TT);			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1");  //1成功，0失败
					
					SurfaceObservationMwrpre mwrPre= (SurfaceObservationMwrpre) reportInfos.get(i).getT();
					String stat = mwrPre.getStationNumberChina();
					String info = (String) proMap.get(stat + "+01");
					int CountryCode = 9999;
					double longtitude = 999999.0;
					double latitude = 999999.0;
					double stationHeight=999999.0;
					if(stat.equals("999998")){
						latitude = mwrPre.getLatitude();
						longtitude = mwrPre.getLongtitude();
					}
					else if(info == null) {
						infoLogger.error("\n In configuration file, this station does not exist!" + stat);
						continue ;
					}
					else{
						String[] infos = info.split(",");
						if(!infos[4].equals("null"))
							CountryCode = Integer.parseInt(infos[4]);
						if(infos[1].equals("null")){
							infoLogger.error("\n In configuration file, longtitude is null!");
							continue;
						}
						else
							longtitude = Double.parseDouble(infos[1]);
						if(infos[2].equals("null")){
							infoLogger.error("\n In configuration file, latitude is null!");
							continue;
						}
						else
							latitude = Double.parseDouble(infos[2]);
						if(!infos[3].equals("null"))
							stationHeight = Double.parseDouble(infos[3]);
					}
					String primkey = sdf.format(mwrPre.getObservationTime())+"_"+stat+"_"+String.valueOf((int)(latitude*1e6))+"_"+String.valueOf((int)(longtitude*1e6))+"_"+mwrPre.getTimeScale();
					int ii = 1;
					pStatement.setString(ii++, primkey);
					pStatement.setString(ii++, sod_report);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(mwrPre.getObservationTime().getTime()));
					
					pStatement.setString(ii++, V_BBB);
					pStatement.setString(ii++, V_CCCC);
					pStatement.setString(ii++, V_TT);
					pStatement.setString(ii++, mwrPre.getStationNumberChina());
					pStatement.setBigDecimal(ii++, new BigDecimal(mwrPre.getStationNumberChina()));    //测站（数字型）
					pStatement.setDouble(ii++, latitude);   // 纬度
					pStatement.setDouble(ii++, longtitude); // 经度
					pStatement.setInt(ii++, CountryCode); // V_NCODE 国家代码
					pStatement.setInt(ii++, defaultInt); //   V_ACODE 中国行政区代码
					Date date = new Date(mwrPre.getObservationTime().getTime());
					pStatement.setInt(ii++, date.getYear() + 1900);
					pStatement.setInt(ii++, date.getMonth() + 1);
					pStatement.setInt(ii++, date.getDate());
					pStatement.setInt(ii++, date.getHours());
					pStatement.setInt(ii++, date.getMinutes());
					pStatement.setInt(ii++, reportInfos.get(i).getReport().length());
					pStatement.setString(ii++, reportInfos.get(i).getReport());
					
					di.setIIiii(stat);
					di.setDATA_TIME(TimeUtil.date2String(mwrPre.getObservationTime(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLONGTITUDE(String.valueOf(latitude));
					di.setLATITUDE(String.valueOf(longtitude));

					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
					di.setDATA_UPDATE_FLAG(V_BBB);
					di.setHEIGHT(String.valueOf(stationHeight));
					
					try{
						pStatement.execute();
						if(connection.getAutoCommit() == false)
							connection.commit();
						listDi.add(di);
					}catch (Exception e) {
						di.setPROCESS_STATE("0");//1成功，0失败
						listDi.add(di);
					}
				} catch (Exception e) {
					infoLogger.error("sql error: " + ((LoggableStatement)pStatement).getQueryString());
					infoLogger.error("Because: " + e.getMessage());
					continue;
				}
			} // end for 
			if(pStatement != null) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					infoLogger.error("\n Close statement failed: "+e.getMessage());
				}
			}
		} catch (SQLException e) {
			infoLogger.error("Database connection failed: " + e.getMessage());
//			return;
		}
	}
	/**
	 * 
	 * @Title: execute_sql   
	 * @Description: TODO(批量入库失败时，采用逐条提交)   
	 * @param  sqls 待执行的查询语句
	 * @param  connection 数据库连接      
	 * @return: void      
	 * @throws:
	 */
	private static void execute_sql(List<String> sqls, java.sql.Connection connection, String filepath) {
		Statement pStatement = null;
		try {
			pStatement = connection.createStatement();
			for (int i = 0; i < sqls.size(); i++) {
				//pStatement = new LoggableStatement(connection, sqls.get(i));
				try {
					pStatement.execute(sqls.get(i));
					connection.commit();
				} catch (Exception e) {
					infoLogger.error("\n File name："+filepath
							+"\n " + listDi.get(i).getIIiii() + " " + listDi.get(i).getDATA_TIME()
							+"\n execute sql failed："+sqls.get(i)+"\n "+e.getMessage());
					listDi.get(i).setPROCESS_STATE("0");
				}
			}
		} catch (SQLException e) {
			infoLogger.error("\n Create statement failed: "+e.getMessage());
		}finally {
			if(pStatement != null) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					infoLogger.error("\n Close statement failed: "+e.getMessage());
				}
			}
		}		
	}
	public static void main(String[] args) {
		String filepath = "D:\\water\\HOUR2019031002.txt";
		File file = new File(filepath);
		String fileN = file.getName();
		DecodeMWRPre decodeMWRPre = new DecodeMWRPre();
		ParseResult<SurfaceObservationMwrpre> parseResult = decodeMWRPre.DecodeFile(file);	
		Date recv_time = new Date();
		if(parseResult.isSuccess()){
			DataBaseAction action = DbService.processSuccessReport(parseResult, recv_time, fileN, filepath);
		}
		
	}
}
