package cma.cimiss2.dpc.indb.ocen.dc_ocean_ori_ship.service;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
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
import org.cimiss2.dwp.tools.RestfulSendData;
import org.cimiss2.dwp.tools.SendType;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.EIConfig;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ocean.OriShip;
import cma.cimiss2.dpc.decoder.bean.ocean.Ship;
import cma.cimiss2.dpc.decoder.ocen.DecodeOriShip;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;


// TODO: Auto-generated Javadoc
/**
 * The Class DbService.
 */
public class DbService {
	
	
	
	/** The list di. */
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	
	/** The di queues. */
	public static BlockingQueue<StatDi> diQueues;
	
	/** The Constant messageLogger. */
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	
	/** The Constant infoLogger. */
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	
	
	/** The sod code. */
	public static String sod_code = StartConfig.sodCode();
	
	/** The cts code. */
	public static String cts_code = StartConfig.ctsCode();
	
	/** The sod report. */
	public static String sod_report = StartConfig.reportSodCode();
	
	
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
	 * @return the data base action
	 * @Title: processSuccessReport
	 * @Description:(中远人工观测船舶资料解码入库) 
	 * @return: DataBaseAction
	 * @throws: 
	 */
	public static DataBaseAction processSuccessReport(ParseResult<OriShip> parseResult, Date recv_time, String fileN, StringBuffer loggerBuffer, String filepath) {
		java.sql.Connection connection = null;
		java.sql.Connection reportConnection = null;
		try {
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			List<OriShip> ships = parseResult.getData();
			insertOriShip(ships, connection,recv_time, fileN,loggerBuffer,filepath);
			@SuppressWarnings("rawtypes")
			List<ReportInfo> reportInfos = parseResult.getReports();
			reportConnection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
			reportInfoToDb(reportInfos, reportConnection,recv_time, fileN,loggerBuffer);  
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
	private static void insertOriShip(List<OriShip> ships, java.sql.Connection connection, Date recv_time, String fileN, StringBuffer loggerBuffer,String filepath) {
//		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement pStatement = null;
		// chy 去掉 D_RECORD_ID
		String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,D_UPDATE_TIME,"
				+ "V01011,V05001,V06001,V07001,V07304,V07301,V07305,V02001,V02141,V04001,"
				+ "V04002,V04003,V04004,V04005,V04006,V01012,V01013,V05021,V20003,V10051,"
				+ "V12001,V12002,V11001,V11002,V11301,V22042,V20001,V20012,V20011,V22022_1,"
				+ "V22022_2,V22303,V_BBB,V20256,D_SOURCE_ID)"
				+ "VALUES (?, ?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?) ";
		if(connection != null){		
			try {	
//				pStatement = new LoggableStatement(connection, sql);
//				if(StartConfig.getDatabaseType() == 1) {
//					pStatement.execute("select last_txc_xid()");
//				}
				List<String> sqls = new ArrayList<>();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");	
				
				for(int idx = 0; idx < ships.size(); idx ++){//循环几条记录
					pStatement = new LoggableStatement(connection, sql);
					OriShip ship = new OriShip();
					ship = ships.get(idx);
					StatDi di = new StatDi();	
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(sod_code);
					di.setDATA_TYPE_1(cts_code);
					di.setTT("");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1");  //1成功，0失败	
					
					String stat = ship.getShipCallSign();
					String latitude=String.format("%.4f", ship.getLatitude());
					String longtitude= String.format("%.4f", ship.getLongtitude());
					int ii = 1;
					Date date = ship.getShiptime();

					//D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,D_UPDATE_TIME
					pStatement.setString(ii++, sdf.format(date)+"_"+stat+"_"+latitude+"_"+longtitude);

					pStatement.setString(ii++, sod_code);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(ship.getShiptime().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					// "V01011,V05001,V06001,V07001,V07304,V07301,V07305,V02001,V02141,V04001,"
					pStatement.setString(ii++, stat);
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(latitude)));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(longtitude)));
					pStatement.setBigDecimal(ii++, new BigDecimal("999998"));
					pStatement.setBigDecimal(ii++, new BigDecimal("999998"));
					pStatement.setBigDecimal(ii++, new BigDecimal("999998"));
					pStatement.setBigDecimal(ii++, new BigDecimal("999998"));
					pStatement.setInt(ii++, 11);
					pStatement.setString(ii++, "11");
					pStatement.setInt(ii++, date.getYear() + 1900);
					// "V04002,V04003,V04004,V04005,V04006,V01012,V01013,V05021,V20003,V10051,"
					pStatement.setInt(ii++, date.getMonth() + 1);
					pStatement.setInt(ii++, date.getDate());
					pStatement.setInt(ii++, date.getHours());
					pStatement.setInt(ii++, date.getMinutes());
					pStatement.setInt(ii++, date.getSeconds());
					pStatement.setInt(ii++, 999998);
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ship.getShipMovingSpeed())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ship.getBowAzimuth())));
					pStatement.setInt(ii++, ship.getWeatherCondition());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ship.getSeaLevelPressure())));
					// "V12001,V12002,V11001,V11002,V11301,V22042,V20001,V20012,V20011,V22022_1,"
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ship.getDryballTemperature())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ship.getWetballTemperature())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ship.getWindDir())));//V11001
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ship.getWindSpeed())));//V11002
					pStatement.setBigDecimal(ii++, new BigDecimal("999998"));//V11301 风力级别
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ship.getSeaTemperature())));
					pStatement.setBigDecimal(ii++, new BigDecimal("999998"));//V20001 能见度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ship.getCloudShape())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ship.getCloudAmount())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ship.getWaveHeightManually())));
					// "V22022_2,V22303, V_BBB,V20256,d_source_id"
					pStatement.setBigDecimal(ii++, new BigDecimal("999998"));
					pStatement.setBigDecimal(ii++, new BigDecimal("999998"));
					pStatement.setString(ii++, "000");
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ship.getVisibilityLevel())));
					pStatement.setString(ii ++, StartConfig.ctsCode());
					
					di.setIIiii(stat);
					di.setDATA_TIME(TimeUtil.date2String(ship.getShiptime(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(latitude);
					di.setLONGTITUDE(longtitude);
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
					di.setDATA_UPDATE_FLAG("000");
					di.setHEIGHT("999999");
					
					listDi.add(di);
					try {
						pStatement.execute();
						connection.commit();
						loggerBuffer.append("\r\n " +"一条数据插入"+StartConfig.valueTable()+"表成功！"+fileN);
						System.out.println("\r\n " +"一条数据插入"+StartConfig.valueTable()+"表成功！"+fileN);
					} catch (SQLException e) {
						if (e.getErrorCode() == 13001 || e.getErrorCode() == 1062) {//主键冲突，则直接更新入库，更正报依次增加
							String[] Existresult=findExistData(StartConfig.valueTable(),ship,connection,loggerBuffer);
							String d_datetime=Existresult[0];
							String v_bbb=Existresult[1];//库里原有更正标识
							if (d_datetime!=null) {//库里有记录
								updateData(ship,connection,fileN,StartConfig.valueTable(),v_bbb,di,loggerBuffer);
							}
						}else{
							loggerBuffer.append("\n 一条数据插入"+StartConfig.valueTable()+"表失败！: "+fileN+" :"+e.getMessage());
							di.setPROCESS_STATE("0");
						}
					}finally {
						if(pStatement != null) {
							try {
								pStatement.close();
							} catch (SQLException e) {
								loggerBuffer.append("\n insertData Close Statement failed: "+e.getMessage());
							}
						}
					}
					
				}//结束循环N条记录
				
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
	
	public static String[] findExistData(String table_name, OriShip ship,Connection connection,StringBuffer loggerBuffer){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		PreparedStatement Pstmt = null;
		ResultSet resultSet  = null;
		String V_BBB = null;
		String d_datetime=null;
		String rntString [] = {d_datetime, V_BBB};
		String sql = "select D_DATETIME,V_BBB from "+table_name+" "
				+ "where  D_DATETIME = ? and V01011 = ? and V05001 = ? and V06001 = ?";
		try{
			if(connection != null){	
				Pstmt = connection.prepareStatement(sql);
				String latitude=String.format("%.4f", ship.getLatitude());
				String longtitude= String.format("%.4f", ship.getLongtitude());
				int ii = 1;
				Pstmt.setTimestamp(ii++, new Timestamp(ship.getShiptime().getTime()));//资料时间
				Pstmt.setString(ii++, ship.getShipCallSign());//呼号
				Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(latitude)));
				Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(longtitude)));
				resultSet = Pstmt.executeQuery();
				if(resultSet.next()){
					d_datetime = resultSet.getString(1);
					V_BBB = resultSet.getString(2);
				}
			}
		}catch(SQLException e){
			loggerBuffer.append("\n findExistData create Statement error " + e.getMessage());
		}
		finally {
			if(Pstmt != null) {
				try {
					Pstmt.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n findExistData close Statement error " + e.getMessage());
				}
			}
			if(resultSet != null){
				try{
					resultSet.close();
				}catch(SQLException e){
					loggerBuffer.append("\n findExistData close resultSet error " + e.getMessage());
				}
			}
		}
		rntString[0] = d_datetime;
		rntString[1] = V_BBB;
		return rntString;
	}

	private static void  updateData(OriShip ship,Connection connection,String filename,String table_name,String v_bbb,StatDi di,StringBuffer loggerBuffer){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		PreparedStatement pStat = null;
		try{
			String updateSql="update "+table_name+" set D_UPDATE_TIME=?, V07001= ?,V07304=?,V07301=?, V07305=?, "
					+ "V02001=?, V02141=?, V01012=?, V01013=?, V05021=?, V20003=?, V10051=?, V12001=?,"
					+ " V12002=? ,V11001=?, V11002= ?,V11301=?,V22042=?, V20001=?,V20012=?, V20011=?, V22022_1=?, V22022_2=?, "
					+ "V22303=?, V20256=?,V_BBB=? where d_datetime=? and V01011=? and v05001=? and v06001=?";
			pStat = new LoggableStatement(connection, updateSql);
			
			String stat = ship.getShipCallSign();
			String latitude=String.format("%.4f", ship.getLatitude());
			String longtitude= String.format("%.4f", ship.getLongtitude());
			Date datetime = ship.getShiptime();
			int ii = 1;
			pStat.setString(ii++, TimeUtil.getSysTime());//D_UPDATE_TIME
			// "V07001,V07304,V07301,V07305,V02001,V02141"
			pStat.setBigDecimal(ii++, new BigDecimal("999998"));
			pStat.setBigDecimal(ii++, new BigDecimal("999998"));
			pStat.setBigDecimal(ii++, new BigDecimal("999998"));
			pStat.setBigDecimal(ii++, new BigDecimal("999998"));
			pStat.setInt(ii++, 11);
			pStat.setString(ii++, "11");
			// "V01012,V01013,V05021,V20003,V10051,"
			pStat.setInt(ii++, 999998);
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(ship.getShipMovingSpeed())));
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(ship.getBowAzimuth())));
			pStat.setInt(ii++, ship.getWeatherCondition());
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(ship.getSeaLevelPressure())));
			// "V12001,V12002,V11001,V11002,V11301,V22042,V20001,V20012,V20011,V22022_1,"
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(ship.getDryballTemperature())));
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(ship.getWetballTemperature())));
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(ship.getWindDir())));//V11001风向
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(ship.getWindSpeed())));//V11002 取报文中“风速”
			pStat.setBigDecimal(ii++, new BigDecimal("999998"));//V11301 风力级别 赋值999998
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(ship.getSeaTemperature())));//V22042 海水温度
			pStat.setBigDecimal(ii++, new BigDecimal("999998"));//V20001 能见度 赋值999998
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(ship.getCloudShape())));
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(ship.getCloudAmount())));
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(ship.getWaveHeightManually())));
			// "V22022_2,V22303,V20256, V_BBB"
			pStat.setBigDecimal(ii++, new BigDecimal("999998"));
			pStat.setBigDecimal(ii++, new BigDecimal("999998"));
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(ship.getVisibilityLevel())));
			//第一份记录赋值“000”，后续如有更正报，依次赋值CCA、CCB……
			int bbb_3=(int)(v_bbb.charAt(2));
			int bbb_2=(int)(v_bbb.charAt(1));
			if(bbb_3>=65 && bbb_3<90){//若库里v_bbb第三位是：[A-Z)，则第三位递增
				char BBB_3=(char)(bbb_3+1);
				String V_BBB=v_bbb.substring(0,2)+BBB_3;
				pStat.setString(ii++, V_BBB);
			}else if(bbb_3==90){//若库里v_bbb第三位是：Z，则第二位递增，第三位置为A
				char BBB_2=(char)(bbb_2+1);
				String V_BBB=v_bbb.substring(0,1)+BBB_2+"A";
				pStat.setString(ii++, V_BBB);
			}else if("000".equals(v_bbb)){
				pStat.setString(ii++, "CCA");
			}
			//d_datetime,V01011,v05001,v06001
			pStat.setString(ii++, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(datetime));//D_DATETIME
			pStat.setString(ii++, stat);
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(latitude)));
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(longtitude)));
			try {
				pStat.execute();
				connection.commit();
				loggerBuffer.append("\r\n " +"成功更新"+table_name+"表一条数据！"+filename);
			} catch (Exception e) {
				loggerBuffer.append("\n 更新一条数据失败！"+e.getMessage()+filename);
				di.setPROCESS_STATE("0");// 1成功，0失败
			}
		}catch (Exception e) {
			loggerBuffer.append("\n updateData create Statement failed: "+e.getMessage());
		}finally {
			if(pStat != null) {
				try {
					pStat.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n updateData Close Statement failed: "+e.getMessage());
				}
			}
		}
	}
	
	@SuppressWarnings({ "resource", "deprecation" })
	private static void reportInfoToDb(@SuppressWarnings("rawtypes") List<ReportInfo> reportInfos, java.sql.Connection connection, Date recv_time, String fileN,StringBuffer loggerBuffer) {
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.reportTable()+" (D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,"
				+ "V_BBB,V_CCCC,V_TT,V01301,V05001,V06001,"
				+ "V04001,V04002,V04003,V04004,V04005,V_LEN,V_REPORT,D_SOURCE_ID) VALUES"
				+ "(?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?)";
		try {
			pStatement = new LoggableStatement(connection, sql);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			for (int i = 0; i < reportInfos.size(); i++) {
				try {
					String V_TT="";
					if(fileN.contains("ZH")){
					    V_TT = "ZHCB";
					}else{
						V_TT= "ZYCB";
					}
					
					OriShip ship = (OriShip) reportInfos.get(i).getT();
					String stat = ship.getShipCallSign();
					Date date = ship.getShiptime();
					String primkey = sdf.format(date)+"_"+stat+"_"+V_TT+"_"+ "000";
					int ii = 1;
					pStatement.setString(ii++, primkey);
					pStatement.setString(ii++, sod_report);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(date.getTime()));
					pStatement.setString(ii++, "000");
					pStatement.setString(ii++, "9999");//V_CCCC
					
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
					pStatement.setString(ii++,cts_code);
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
	
	//计算经纬度
	public	static double [] calcuLatAndLon(String station,double latitude,double longitude,String filename){
		Map<String, Object> proMap = StationInfo.getProMap();
		double[] LatAndLon={latitude,longitude};
	  if (latitude<-90||latitude>90) {
		  latitude=999999;
	  }
	  if(longitude<-180||longitude>180){
		  longitude=999999;
	  }
	  if(latitude==999999.0||longitude==999999.0){
			String info = (String) proMap.get(station + "+09");
			if(info == null||"".equals(info)) {
				infoLogger.error("经纬度无法解析！"  + filename);
				sendEI(station);
				return LatAndLon;
				
			}else{
				String[] infos = info.split(",");
				if(!(infos[1].trim().equals("null") || infos[1].trim().equals(""))){
					try{
					 longitude = Double.parseDouble(infos[1]);//经度
					}catch (Exception e) {
						infoLogger.error("经纬度无法解析！"  + filename);
						sendEI(station);
						return LatAndLon;
					}
				}else{
					infoLogger.error("经纬度无法解析！"  + filename);
					sendEI(station);
					return LatAndLon;
				}
				if(!(infos[2].trim().equals("null") || infos[2].trim().equals(""))){
					try{
					 latitude = Double.parseDouble(infos[2]);//纬度
					}catch (Exception e) {
						infoLogger.error("经纬度无法解析！"  + filename);
						sendEI(station);
						return LatAndLon;
					}
				}else{
					infoLogger.error("经纬度无法解析！"  + filename);
					sendEI(station);
					return LatAndLon;
				}
				latitude= Double.parseDouble(String.format("%.4f", latitude));
				longitude= Double.parseDouble(String.format("%.4f", longitude));
				LatAndLon[0]=latitude;
				LatAndLon[1]=longitude;
			}
		}else{
			latitude= Double.parseDouble(String.format("%.4f", latitude));
			longitude= Double.parseDouble(String.format("%.4f", longitude));
			LatAndLon[0]=latitude;
			LatAndLon[1]=longitude;
		}
	  return LatAndLon;
	}
	public static void sendEI(String station){
		String event_type = EIEventType.STATION_FILE_ERROR.getCode();
		EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
		if(ei == null) {
			infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: "+event_type);
		}else {
			if(StartConfig.isSendEi()) {
				ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
				ei.setKObject("cma.cimiss2.dpc.indb.ocen.dc_ocean_ori_ship.service.DbService");
				ei.setKEvent("获取台站信息异常");
				ei.setKIndex("全球高空台站："+station+"未能获取台站信息配置，无法获取经纬度");
				RestfulInfo restfulInfo = new RestfulInfo();
				restfulInfo.setType("SYSTEM.ALARM.EI ");
				restfulInfo.setName("获取台站信息EI告警信息");
				restfulInfo.setMessage("获取台站信息EI告警信息");
				restfulInfo.setFields(ei);
				List<RestfulInfo> restfulInfos = new ArrayList<>();
				restfulInfos.add(restfulInfo);
				RestfulSendData.SendData(restfulInfos, SendType.EI);
			}
		}
	}
	public static void main(String[] args) {
		
		DecodeOriShip decodeShip = new DecodeOriShip();
		String filepath ="D:\\中远人工观测船舶资料\\中远船舶样例数据\\ZY_M_20190724140030.txt";
		File file = new File(filepath);
		String fileN = file.getName();
		Date recv_time = new Date(file.lastModified());
		StringBuffer loggerBuffer = new StringBuffer();
		ParseResult<OriShip> parseResult = decodeShip.DecodeFile(file);	
		if(parseResult.isSuccess()){
			DataBaseAction action = DbService.processSuccessReport(parseResult,recv_time,fileN,loggerBuffer,filepath);
		}
	}
}
