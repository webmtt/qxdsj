package cma.cimiss2.dpc.indb.ocen.dc_ocean_ship.service;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import cma.cimiss2.dpc.decoder.bean.ocean.Ship;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;


// TODO: Auto-generated Javadoc
/**
 * The Class DbService.
 */
public class DbService {
	
	/** The retry map. */
	HashMap<String, Integer> retryMap;
	
	/** The list di. */
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	
	/** The di queues. */
	public static BlockingQueue<StatDi> diQueues;
	
	/** The Constant messageLogger. */
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	
	/** The Constant infoLogger. */
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	
	/** The sod report. */
	public static String sod_report = StartConfig.reportSodCode();
	
	/** The sod code. */
	public static String sod_code = StartConfig.sodCode();
	
	/** The cts code. */
	public static String cts_code = StartConfig.ctsCode();
	
	/** The v cccc. */
	public static String V_CCCC = "9999";
	
	/** The default int. */
	public static int defaultInt = 999999;
	
	/** The pro map. */
//	static Map<String, Object> proMap = StationInfo.getProMap();
	
	
	/**
	 * Gets the di queues.
	 *
	 * @return the di queues
	 */
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	
	/**
	 * Sets the di queues.
	 *
	 * @param diQueues the new di queues
	 */
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}
	
	/**
	 * Process success report.
	 *
	 * @param parseResult the parse result
	 * @param recv_time 解码文件的路径
	 * @param fileN the file N
	 * @param loggerBuffer the logger buffer
	 * @param V_BBB the v bbb
	 * @param V_TT the v tt
	 * @return the data base action
	 * @Title: processSuccessReport
	 * @Description:(国内船舶观测资料解码入库) 
	 * @return: DataBaseAction
	 * @throws: 
	 */
	public static DataBaseAction processSuccessReport(ParseResult<Ship> parseResult, Date recv_time, String fileN, StringBuffer loggerBuffer, String V_BBB, String V_TT, String filepath) {
		java.sql.Connection connection = null;
		java.sql.Connection reportConnection = null;
		try {
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			List<Ship> ships = parseResult.getData();
			insertShip(ships, connection,recv_time, fileN,loggerBuffer,V_BBB,V_TT,filepath);
			@SuppressWarnings("rawtypes")
			List<ReportInfo> reportInfos = parseResult.getReports();
			reportConnection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
			reportInfoToDb(reportInfos, reportConnection,recv_time, fileN,V_BBB,V_TT,loggerBuffer);  
			return DataBaseAction.SUCCESS;
		} catch (Exception e) {

			loggerBuffer.append("\n Database connection error"+e.getMessage());
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
	 * Insert ship.
	 *
	 * @param ships 待入库对象集
	 * @param connection the connection
	 * @param recv_time the recv time
	 * @param fileN the file N
	 * @param loggerBuffer the logger buffer
	 * @param V_BBB the v bbb
	 * @param V_TT the v tt
	 * @Title: insertShip
	 * @Description:(国内海上船舶资料入库) 
	 * @return: void
	 * @throws: 
	 */
	@SuppressWarnings("deprecation")
	private static void insertShip(List<Ship> ships, java.sql.Connection connection, Date recv_time, String fileN, StringBuffer loggerBuffer, String V_BBB, String V_TT, String filepath) {
		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,D_UPDATE_TIME,"
				+ "V01011,V05001,V06001,V07001,V07304,V07301,V07305,V02001,V02141,V04001,"
				+ "V04002,V04003,V04004,V04005,V04006,V01012,V01013,V05021,V20003,V10051,"
				+ "V12001,V12002,V11001,V11002,V11301,V22042,V20001,V20012,V20011,V22022_1,"
				+ "V22022_2,V22303, V_BBB,D_SOURCE_ID,V20256)"
				+ "VALUES (?, ?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?) ";
		List<Ship> ships2 = new ArrayList<>();
		if(connection != null){		
			try {	
				pStatement = new LoggableStatement(connection, sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				List<String> sqls = new ArrayList<>();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");	
				String bbb;
				for(int idx = 0; idx < ships.size(); idx ++){
					Ship ship = new Ship();
					ship = ships.get(idx);
					bbb = findVBB(ship, connection,V_BBB,loggerBuffer);
					if(V_BBB.equals("000") || bbb == null){ 
						// 非更正报；或者为更正报，但是数据库中尚未有这一条数据
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
						
						String stat = ship.getShipID();
						//2019-1-29 新增   2020-4-20注释掉
//						String info = (String) proMap.get(stat + "+09");
//						if(info == null) {
////							System.out.println(stat);
//							loggerBuffer.append("\n In the configuration file, the station number does not exist:" + stat);
//							continue;
//						}
						
						int ii = 1;
						Date date = ship.getObservationTime();
						String latitude=String.format("%.4f", ship.getLatitude());
						String longtitude= String.format("%.4f", ship.getLongtitude());
						pStatement.setString(ii++, sdf.format(date)+"_"+stat+"_"+latitude+"_"+longtitude);
						pStatement.setString(ii++, sod_code);
						pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
						pStatement.setTimestamp(ii++, new Timestamp(ship.getObservationTime().getTime()));
						pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						// "V01011,V05001,V06001,V07001,V07304,V07301,V07305,V02001,V02141,V04001,"
						pStatement.setString(ii++, stat);
						pStatement.setString(ii++, String.format("%.4f", ship.getLatitude()));
						pStatement.setString(ii++, String.format("%.4f", ship.getLongtitude()));
						pStatement.setString(ii++, String.valueOf(ship.getStationHeightAboveSea()));
						pStatement.setString(ii++, String.valueOf(ship.getHeightOfAirpressureSensor()));
						pStatement.setString(ii++, String.valueOf(ship.getHeightOfWindSpeedSensor()));
						pStatement.setString(ii++, String.valueOf(ship.getDistanceBetweenDeckAndSea()));
						String[] items = fileN.split("-");
						if(items[items.length-1].substring(0, 1).equalsIgnoreCase("A")) {
							pStatement.setInt(ii++, 1);
							pStatement.setString(ii++, "1");
						}else {
							pStatement.setInt(ii++, 0);
							pStatement.setString(ii++, "0");
						}
						
						pStatement.setInt(ii++, date.getYear() + 1900);
						// "V04002,V04003,V04004,V04005,V04006,V01012,V01013,V05021,V20003,V10051,"
						pStatement.setInt(ii++, date.getMonth() + 1);
						pStatement.setInt(ii++, date.getDate());
						pStatement.setInt(ii++, date.getHours());
						pStatement.setInt(ii++, date.getMinutes());
						pStatement.setInt(ii++, date.getSeconds());
						pStatement.setInt(ii++, ship.getShipMovingDir());
						pStatement.setString(ii++, String.valueOf(ship.getShipMovingSpeed()));
						pStatement.setString(ii++, String.valueOf(ship.getBowAzimuth()));
						pStatement.setInt(ii++, ship.getWeatherCondition());
						pStatement.setString(ii++, String.valueOf(ship.getSeaLevelPressure()));
						// "V12001,V12002,V11001,V11002,V11301,V22042,V20001,V20012,V20011,V22022_1,"
						pStatement.setString(ii++, String.valueOf(ship.getDryballTemperature()));
						pStatement.setString(ii++, String.valueOf(ship.getWetballTemperature()));
						pStatement.setString(ii++, String.valueOf(ship.getWindDir()));
						pStatement.setString(ii++, String.valueOf(ship.getWindSpeed()));
						pStatement.setString(ii++, String.valueOf(ship.getWindLevel()));
						pStatement.setString(ii++, String.valueOf(ship.getSeaTemperature()));
						pStatement.setString(ii++, "999998");//V20001 能见度
						pStatement.setInt(ii++, ship.getCloudShape());
						pStatement.setInt(ii++, ship.getCloudAmount());
						pStatement.setString(ii++, String.valueOf(ship.getWaveHeightManually()));
						// "V22022_2,V22303, V_BBB,D_SOURCE_ID,V20256"
						pStatement.setString(ii++, String.valueOf(ship.getWaveHeightByInstrument()));
						pStatement.setString(ii++, String.valueOf(ship.getWaveLevel()));
						pStatement.setString(ii++, V_BBB);
						pStatement.setString(ii ++, StartConfig.ctsCode());
						
						String  visibility=String.valueOf(ship.getVisibility());//获取能见度
						visibility=visibility.startsWith("99999")?visibility:String.valueOf(ship.getVisibility()/1852);
						pStatement.setString(ii ++, visibility);//V20256 能见度级别
						
						
						di.setIIiii(stat);
						di.setDATA_TIME(TimeUtil.date2String(ship.getObservationTime(), "yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());	
						di.setLATITUDE(String.format("%.4f", ship.getLatitude()));
						di.setLONGTITUDE(String.format("%.4f", ship.getLongtitude()));
						
						di.setSEND("BFDB");
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
						di.setDATA_UPDATE_FLAG(V_BBB);
						di.setHEIGHT(String.valueOf(ship.getStationHeightAboveSea()));
						
						pStatement.addBatch();
						sqls.add(((LoggableStatement)pStatement).getQueryString()); // 批量入库冲突时，单条入库
						listDi.add(di);
					} 
					else if(bbb.compareTo(V_BBB) < 0){
						ships2.add(ship); // 更正报，需要update操作,数据库中有该条记录，且更正标识早于当前处理文件的更正标识的值
					}
					else{ // 数据库中有该条记录，且V_BBB的值晚于当前处理文件的更正标识的值
						// 不进行插入或更新
					}
				}
				try{
					pStatement.executeBatch();
					connection.commit();
					sqls.clear();
				}catch(SQLException e){
					pStatement.clearParameters();
					pStatement.clearBatch();
					execute_sql(sqls, connection,fileN,loggerBuffer); // 此中，入库失败的会将对应的DI的ProcessState置为1 // 1:失败，0：成功
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
				// 更正报的入库
				if(ships2.size() > 0)
					updateEle(ships2, connection, recv_time,fileN,V_BBB,V_TT,loggerBuffer, filepath);
			}
		} 
	}
	
	/**
	 * Update ele.
	 *
	 * @param ships 入库对象集合
	 * @param connection 数据库连接
	 * @param recv_time void
	 * @param fileN the file N
	 * @param V_BBB the v bbb
	 * @param V_TT the v tt
	 * @param loggerBuffer the logger buffer
	 * @Title: updateEle
	 * @Description:(海上船舶更正报文入库) 
	 * @throws： 
	 */
	@SuppressWarnings("deprecation")
	private static void updateEle(List<Ship> ships, java.sql.Connection connection, Date recv_time, String fileN, String V_BBB, String V_TT, StringBuffer loggerBuffer, String filepath){
		PreparedStatement Pstmt = null;
		Ship ship = new Ship();
		String sql = "update "+StartConfig.valueTable()+" set "
				+ "V_BBB=?, D_UPDATE_TIME=?,"
				+ "V05001=?,V06001=?,V07001=?,V07304=?,V07301=?,V07305=?,V02001=?,V02141=?,V01012=?,V01013=?,"
				+ "V05021=?,V20003=?,V10051=?,V12001=?,V12002=?,V11001=?,V11002=?,V11301=?,V22042=?,V20001=?,"
				+ "V20012=?,V20011=?,V22022_1=?,V22022_2=?,V22303=?,V20256=?  "
				+ "where V01011 = ? and D_datetime = ? and V05001=? and V06001=?";
		if(connection != null){
			try{
				Pstmt = new LoggableStatement(connection, sql);
				for(int idx = 0; idx < ships.size(); idx ++){
					ship = ships.get(idx);
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
					
					Date date = new Date();
					date = ship.getObservationTime();
					
					int ii = 1;
					Pstmt.setString(ii++, V_BBB);
					Pstmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					
					Pstmt.setString(ii++, String.format("%.4f", ship.getLatitude()));
					Pstmt.setString(ii++, String.format("%.4f", ship.getLongtitude()));
					Pstmt.setString(ii++, String.valueOf(ship.getStationHeightAboveSea()));
					Pstmt.setString(ii++, String.valueOf(ship.getHeightOfAirpressureSensor()));
					Pstmt.setString(ii++, String.valueOf(ship.getHeightOfWindSpeedSensor()));
					Pstmt.setString(ii++, String.valueOf(ship.getDistanceBetweenDeckAndSea()));
					String[] items = fileN.split("-");
					if(items[items.length-1].substring(0, 1).equalsIgnoreCase("A")) {
						Pstmt.setInt(ii++, 1);
						Pstmt.setString(ii++, "1");
					}else {
						Pstmt.setInt(ii++, 0);
						Pstmt.setString(ii++, "0");
					}
					Pstmt.setInt(ii++, ship.getShipMovingDir());
					Pstmt.setString(ii++, String.valueOf(ship.getShipMovingSpeed()));
					
					Pstmt.setString(ii++, String.valueOf(ship.getBowAzimuth()));
					Pstmt.setInt(ii++, ship.getWeatherCondition());
					Pstmt.setString(ii++, String.valueOf(ship.getSeaLevelPressure()));
					Pstmt.setString(ii++, String.valueOf(ship.getDryballTemperature()));
					Pstmt.setString(ii++, String.valueOf(ship.getWetballTemperature()));
					Pstmt.setString(ii++, String.valueOf(ship.getWindDir()));
					Pstmt.setString(ii++, String.valueOf(ship.getWindSpeed()));
					Pstmt.setString(ii++, String.valueOf(ship.getWindLevel()));
					Pstmt.setString(ii++, String.valueOf(ship.getSeaTemperature()));
					Pstmt.setString(ii++, "999998");//V20001 能见度
					
					Pstmt.setString(ii++, String.valueOf(ship.getCloudShape()));
					Pstmt.setString(ii++, String.valueOf(ship.getCloudAmount()));
					Pstmt.setString(ii++, String.valueOf(ship.getWaveHeightManually()));
					Pstmt.setString(ii++, String.valueOf(ship.getWaveHeightByInstrument()));
					Pstmt.setString(ii++, String.valueOf(ship.getWaveLevel()));
					
					String  visibility=String.valueOf(ship.getVisibility());//获取能见度
					visibility=visibility.startsWith("99999")?visibility:String.valueOf(ship.getVisibility()/1852);
					Pstmt.setString(ii ++, visibility);//V20256 能见度级别
					
					Pstmt.setString(ii++, ship.getShipID());
//					Pstmt.setInt(ii++, date.getYear() + 1900);
//					Pstmt.setInt(ii++, date.getMonth() + 1);
//					Pstmt.setInt(ii++, date.getDate());
//					Pstmt.setInt(ii++, date.getHours());
//					Pstmt.setInt(ii++, date.getMinutes());
//					Pstmt.setInt(ii++, date.getSeconds());
					Pstmt.setTimestamp(ii++, new Timestamp(date.getTime()));
					Pstmt.setString(ii++, String.format("%.4f", ship.getLatitude()));
					Pstmt.setString(ii++, String.format("%.4f", ship.getLongtitude()));
					
					di.setIIiii(ship.getShipID());
					di.setDATA_TIME(TimeUtil.date2String(ship.getObservationTime(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLATITUDE(String.format("%.4f", ship.getLatitude()));
					di.setLONGTITUDE(String.format("%.4f", ship.getLongtitude()));
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
					di.setDATA_UPDATE_FLAG(V_BBB);
					di.setHEIGHT(String.valueOf(ship.getStationHeightAboveSea()));
					
					listDi.add(di);
					try{
						Pstmt.execute();
						connection.commit();
						loggerBuffer.append("\n update data success! "+((LoggableStatement)Pstmt).getQueryString()+"filename："+fileN);
					}catch(SQLException e){							
						if(listDi.size() > 0)
							listDi.get(listDi.size() - 1).setPROCESS_STATE("0");//1成功，0失败
						loggerBuffer.append("\n filename："+fileN
								+"\n execute sql error："+((LoggableStatement)Pstmt).getQueryString()+"\n "+e.getMessage());
					}	
				}
			}
			catch (SQLException e) {
				loggerBuffer.append("\n create Statement error: "+ e.getMessage());
			}
			finally {
				try {
					if(Pstmt != null)
						Pstmt.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n close Statement error: "+ e.getMessage());
				}	
			}
		} 
		else 
			loggerBuffer.append("\n Database connection exception");		
	}
	
	/**
	 * Find VBB.
	 *
	 * @param ship the ship
	 * @param connection the connection
	 * @param V_BBB the v bbb
	 * @param loggerBuffer the logger buffer
	 * @return the string
	 * @Title: findVBB
	 * @Description:(查找国内海上船舶要素数据的更正标识) 
	 * @return: String
	 * @throws: 
	 */
	@SuppressWarnings("deprecation")
	private static String findVBB(Ship ship, java.sql.Connection connection, String V_BBB, StringBuffer loggerBuffer){
		PreparedStatement Pstmt = null;
		ResultSet resultSet  = null;
		String rntString = null;
		String sql = "select V_BBB from "+StartConfig.valueTable()+" "
				+ "where V01011 = ? and D_Datetime = ? and V05001=? and V06001=?";
		try{
			
			if(connection != null){	
				Pstmt = connection.prepareStatement(sql);
				Date date = new Date(ship.getObservationTime().getTime());
				int ii = 1;
				Pstmt.setString(ii++, String.valueOf(ship.getShipID()));
//				Pstmt.setInt(ii++, date.getYear() + 1900);
//				Pstmt.setInt(ii++, date.getMonth()  + 1);
//				Pstmt.setInt(ii++, date.getDate());
//				Pstmt.setInt(ii++, date.getHours());
//				Pstmt.setInt(ii++, date.getMinutes());
//				Pstmt.setInt(ii++, date.getSeconds());
				
				Pstmt.setTimestamp(ii++, new Timestamp(date.getTime()));
				Pstmt.setString(ii++, String.format("%.4f", ship.getLatitude()));
				Pstmt.setString(ii++, String.format("%.4f", ship.getLongtitude()));
				
				resultSet = Pstmt.executeQuery();
				if(resultSet.next()){
					rntString = resultSet.getString(1);
				}
			}
		}catch(SQLException e){
			loggerBuffer.append("\n create Statement error " + e.getMessage());
		}
		finally {
			if(Pstmt != null) {
				try {
					Pstmt.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n close Statement error " + e.getMessage());
				}
			}
			if(resultSet != null){
				try{
					resultSet.close();
				}catch(SQLException e){
					loggerBuffer.append("\n close resultSet error " + e.getMessage());
				}
			}
		}
		return rntString;
	}
	
	
	/**
	 * Report info to db.
	 *
	 * @param reportInfos the report infos
	 * @param connection the connection
	 * @param recv_time   资料时间
	 * @param fileN the file N
	 * @param V_BBB the v bbb
	 * @param V_TT the v tt
	 * @param loggerBuffer the logger buffer
	 * @Title: reportInfoToDb
	 * @Description:(报文信息入库) 
	 * @return: void
	 * @throws: 
	 */
	@SuppressWarnings({ "resource", "deprecation" })
	private static void reportInfoToDb(@SuppressWarnings("rawtypes") List<ReportInfo> reportInfos, java.sql.Connection connection, Date recv_time, String fileN, String V_BBB, String V_TT, StringBuffer loggerBuffer) {
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.reportTable()+" (D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,"
				+ "V_BBB,V_CCCC,V_TT,V01301,V05001,V06001,"
				+ "V04001,V04002,V04003,V04004,V04005,V_LEN,V_REPORT) VALUES"
				+ "(?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?)";
		try {
			pStatement = new LoggableStatement(connection, sql);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			for (int i = 0; i < reportInfos.size(); i++) {
				try {
					Ship ship = (Ship) reportInfos.get(i).getT();
					String stat = ship.getShipID();
					Date date = ship.getObservationTime();
					String primkey = sdf.format(date)+"_"+stat+"_"+V_TT+"_"+ V_BBB;
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
					pStatement.setBigDecimal(ii++, new BigDecimal(ship.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(ship.getLongtitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setInt(ii++, date.getYear() + 1900);
					pStatement.setInt(ii++, date.getMonth() + 1);
					pStatement.setInt(ii++, date.getDate());
					pStatement.setInt(ii++, date.getHours());
					pStatement.setInt(ii++, date.getMinutes());
					pStatement.setInt(ii++, reportInfos.get(i).getReport().length());
					pStatement.setString(ii++, reportInfos.get(i).getReport());
					
					pStatement.execute();
					connection.commit();
				} catch (Exception e) {
					loggerBuffer.append("sql error:"+((LoggableStatement)pStatement).getQueryString()+e.getMessage()+"\n");
					continue;
				}
			}
		} catch (SQLException e) {
			loggerBuffer.append("Database connection exception:" + e.getMessage());
			return;
		}
		finally {
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
	 * Execute sql.
	 *
	 * @param sqls the sqls
	 * @param connection the connection
	 * @param fileN the file N
	 * @param loggerBuffer the logger buffer
	 * @Title: execute_sql
	 * @Description:(批量入库失败时，采用逐条提交) 
	 * @return: void
	 * @throws: 
	 */
	private static void execute_sql(List<String> sqls, java.sql.Connection connection, String fileN, StringBuffer loggerBuffer) {
		Statement pStatement = null;
		try {
			pStatement = connection.createStatement();
			connection.setAutoCommit(true);
			for (int i = 0; i < sqls.size(); i++) {
//				pStatement = new LoggableStatement(connection, sqls.get(i));
				try {
					pStatement.execute(sqls.get(i));
//					connection.commit();
				} catch (Exception e) {
					System.out.println("\n filename："+fileN
							+"\n " + listDi.get(i).getIIiii() + " " + listDi.get(i).getDATA_TIME()
							+"\n execute sql error："+ sqls.get(i) +"\n "+e.getMessage());
					
					loggerBuffer.append("\n filename："+fileN
							+"\n " + listDi.get(i).getIIiii() + " " + listDi.get(i).getDATA_TIME()
							+"\n execute sql error："+ sqls.get(i) +"\n "+e.getMessage());
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
