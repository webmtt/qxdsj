package cma.cimiss2.dpc.indb.upar.dc_upar_ttua.service;

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
import cma.cimiss2.dpc.decoder.bean.upar.UDUA;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.decoder.upar.DecodeUDUA;

public class DbService {
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	public static String cts_code = StartConfig.ctsCode();
	
//	public static String dpc_code = "B.0014.0001.P001";
	public static String sod_code = StartConfig.sodCode();
	
//	public static String dpc_report_code = "B.0001.0009.PT01";
	public static String sod_report_code = StartConfig.reportSodCode();

	public static double noObser = 999998;
	public static String noObserStr = "999998";
	public static int noObserInt = 999998;
	
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}
	/** 
	 * 
	 * @Title: processSuccessReport   
	 * @Description:(飞机高空探测报告(UD/UA)报文解码入库函数)   
	 * @param  parseResult  存放解码结果
	 * @param recv_time 文件路径
	 * @param  recv_time,fileN  报文接收时间   
	 * @param loggerBuffer 
	 * @return: DataBaseAction      
	 * @throws:
	 */
	public static DataBaseAction processSuccessReport(ParseResult<UDUA> parseResult, Date recv_time, String fileN, StringBuffer loggerBuffer, String filepath) {
		java.sql.Connection connection = null;
		java.sql.Connection reportConnection = null;
		try {
			// 获取数据库连接
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			reportConnection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
			List<UDUA> uduas = parseResult.getData();
			insertDB(uduas, connection, recv_time,fileN,loggerBuffer, filepath);
		
			@SuppressWarnings("rawtypes")
			List<ReportInfo> reportInfos = parseResult.getReports();
			reportInfoToDb(reportInfos, reportConnection, recv_time,fileN,loggerBuffer);  
			return DataBaseAction.SUCCESS;
		} catch (Exception e) {
			loggerBuffer.append("\n database connection  error");
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
					loggerBuffer.append("\n database connection  close error"+e.getMessage());
			}
			
			if(reportConnection != null) {
				try {
					reportConnection.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n database connection  close error"+e.getMessage());
				}
			}
		}
	}
	}
	/**
	 * 
	 * @Title: insertDB   
	 * @Description:(飞机高空探测报文资料入库)   
	 * @param  uduas  入库对象集合
	 * @param  connection 数据库连接
	 * @param  recv_time      资料接收时间
	 * @param fileN 
	 * @return: void      
	 * @throws:
	 */
	@SuppressWarnings("deprecation")
	private static void insertDB(List<UDUA> uduas, java.sql.Connection connection, Date recv_time, String fileN,StringBuffer loggerBuffer, String filepath) {
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,D_UPDATE_TIME,"
			+ "V_BBB,V04001_02,V04002_02,V04003_02,V04004_02,V04005_02,V01301,V05001,V06001,V04001,"
			+ "V04002,V04003,V04004,V04005,V04006,V07010,V07002,V08009,V12001,V12003,"
			+ "V13003,V11001,V11002,V11031,V02061,V02062,V02005,V11036,D_SOURCE_ID,"
		
//			+ "D_DATA_DPCID,V_TT,V_STT,C_CCCC,V01032,V01031,V01008,V01023,V01110,V01111,"
			+ "V_TT,V_STT,C_CCCC,V01032,V01031,V01008,V01023,V01110,V01111,"
//			+ "V01112,V02170, V02002,V02001,V02065,V33025,V02070,V07001,V_ACODE,V07004,"
			+ "V01112,V02170, V02002,V02001,V02065,V33025,V02070,V07001,V07004,"
			+ "V10070,V13002,V20043,V20044,V20045,V33026,V11034,V11035,V11075,V11076,"
			+ "V11077,V11039,V11037,V11100,V11101,V11102,V11103,V11104,V02063,V02064,"
			+ "Q07004,Q07010,Q07002,Q10070,Q08009,Q12001,Q02005,Q12003,Q13003,Q13002,"
			+ "Q20043,Q20044,Q20045,Q33026,Q11001,Q11002,Q11034,Q11035,Q11036,Q11075,"
			+ "Q11076,Q11077,Q11039,Q11037,Q11031,Q11100,Q11101,Q11102,Q11103,Q11104,"
			+ "Q02063,Q02064,QR07004,QR07010,QR07002,QR08009,QR12001,QR12003,QR13003,"
			+ "QR13002,QR11001,QR11002,QR11034,QR11035,QR11031) "
		
			+ "VALUES (?, ?, ?, ?, ?, ?,"
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			
			+ "?, ?, ?, ?, ?, ?, ?, ?, ?,"
			+ "?, ?, ?, ?, ?, ?, ?, ?, ?,"
			+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
			+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
			+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
			+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
			+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
			+ "?, ?, ?, ?, ?, ?, ?, ?, ?,"
			+ "?, ?, ?, ?, ?, ?) " ;
		UDUA udua = new UDUA();
		if(connection != null){		
			try {	
				pStatement = new LoggableStatement(connection, sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				List<String> sqls = new ArrayList<>();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");		
				
				String V_TT = "";
				for(int idx = 0; idx < uduas.size(); idx ++){
					udua = uduas.get(idx);
					if(udua.getReportType().equals("AMDAR"))
						V_TT = "UD";
					else V_TT = "UA";
					StatDi di = new StatDi();	
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(sod_code);
					di.setDATA_TYPE_1(cts_code);
					di.setTT(V_TT);			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //0成功，1失败
					di.setPROCESS_STATE("1");  //0成功，1失败	
					
					double latitude=udua.getLatitude();
					double longitude=udua.getLongtitude();
					String station=udua.getPlaneID();
					double []cal=calcuLatAndLon(station, latitude, longitude, fileN);
					latitude=cal[0];
					longitude=cal[1];
					Date dataTime = udua.getDataTime();
					// oTime 2019-4-15修改
//					Date oTime = udua.getObservationTime();
					Date oTime = new Date(dataTime.getTime());
//					oTime.setMinutes(0);
					
					String lat = String.valueOf((int)(latitude * 1e6));
					String lon = String.valueOf((int)(longitude * 1e6));
					lat = lat.replaceAll("-", "0");
					lon = lon.replaceAll("-", "0");
					int ii = 1;
//					pStatement.setString(ii++, sdf.format(oTime)+"_"+udua.getPlaneID()+"_"+lat+"_"+lon+"_"+(int)(udua.getHeightOfStandardPressure()*1e4)+"_"+udua.getPlaneStateAndObsType()+"_"+V_TT+"_"+udua.getReportCenter()+"_"+udua.getCorrectSign());
//					pStatement.setString(ii++, sdf.format(oTime)+"_" + sdf.format(dataTime)+"_"+udua.getPlaneID()+"_"+lat+"_"+lon+"_"+(int)(udua.getHeightOfStandardPressure()*1e4)+"_"+udua.getPlaneStateAndObsType());
					pStatement.setString(ii++, sdf.format(dataTime)+"_"+udua.getPlaneID()+"_"+lat+"_"+lon+"_"+(int)(udua.getHeightOfStandardPressure()*1e4)+"_"+udua.getPlaneStateAndObsType());
					
					pStatement.setString(ii++, sod_code);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(oTime.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					// "V_BBB,V04001_02,V04002_02,V04003_02,V04004_02,V04005_02,V01301,V05001,V06001,V04001,"
					pStatement.setString(ii++, udua.getCorrectSign());  // 更正报标识
					pStatement.setInt(ii++, oTime.getYear() + 1900); // 资料年
					pStatement.setInt(ii++, oTime.getMonth() + 1); // 资料月
					pStatement.setInt(ii++, oTime.getDate()); // 资料日
					pStatement.setInt(ii++, oTime.getHours()); //资料时
//					pStatement.setInt(ii++, 0); //资料分
					pStatement.setInt(ii++, oTime.getMinutes()); // 资料分
					pStatement.setString(ii++, udua.getPlaneID()); 
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(latitude)));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(longitude)));
					pStatement.setInt(ii++, dataTime.getYear() + 1900);// 观测时间 年
					
					//+ "V04002,V04003,V04004,V04005,V04006,V07010,V07002,V08009,V12001,V12003,"
					pStatement.setInt(ii++, dataTime.getMonth() + 1);// 观测时间 月
					pStatement.setInt(ii++, dataTime.getDate()); // 观测时间 日
					pStatement.setInt(ii++, dataTime.getHours()); // 观测时间 时
					pStatement.setInt(ii++, dataTime.getMinutes()); // 观测时间 分
					pStatement.setInt(ii++, 0); // 观测时间 秒
					pStatement.setBigDecimal(ii++, new BigDecimal(udua.getAircraftHeightAboveSeaLevel()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(udua.getHeightOfStandardPressure()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setInt(ii++, udua.getPlaneStateAndObsType());
					pStatement.setBigDecimal(ii++, new BigDecimal(udua.getTemperature()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(udua.getDewPoint()).setScale(4, BigDecimal.ROUND_HALF_UP));
					
					//+ "V13003,V11001,V11002,V11031,V02061,V02062,V02005,V11036) "
					pStatement.setBigDecimal(ii++, new BigDecimal(udua.getRelativehumidity()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setInt(ii++, udua.getWindDir());
					pStatement.setBigDecimal(ii++, new BigDecimal(udua.getWindSpeed()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(udua.getTurbulence()));
					pStatement.setInt(ii++, udua.getNaviType());
					pStatement.setInt(ii++, udua.getDataTransType());
					pStatement.setInt(ii++, udua.getTemperatureObsAccuCode());
					pStatement.setBigDecimal(ii++, new BigDecimal(udua.getVerticalWindSpeed()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setString(ii++, cts_code);
					
//					+ "D_DATA_DPCID,V_TT,V_STT,C_CCCC,V01032,V01031,V01008,V01023,V01110,V01111,"
//					pStatement.setString(ii++, "B.0014.0001.P001");
					pStatement.setString(ii++, udua.getReportType());
					pStatement.setString(ii++, udua.getReportType());
					pStatement.setString(ii++, udua.getReportCenter());
					pStatement.setInt(ii++, noObserInt);
					pStatement.setInt(ii++, noObserInt);
					pStatement.setString(ii++, noObserStr);
					pStatement.setDouble(ii++, noObser);
					pStatement.setString(ii++, noObserStr);
					pStatement.setString(ii++, noObserStr);
					//+ "V01112,V02170, V02002,V02001,V02065,V33025,V02070,V07001,V_ACODE,V07004,"
					pStatement.setString(ii++, noObserStr);
					pStatement.setInt(ii++, noObserInt);
					pStatement.setInt(ii++, noObserInt);
					pStatement.setDouble(ii++, noObser);
					pStatement.setString(ii++, noObserStr);
					pStatement.setInt(ii++, noObserInt);
					pStatement.setDouble(ii++, noObser);
					pStatement.setDouble(ii++, noObser);
//					pStatement.setInt(ii++, noObserInt);
					pStatement.setDouble(ii++, noObser);
//					+ "V10070,V13002,V20043,V20044,V20045,V33026,V11034,V11035,V11075,V11076,"
					pStatement.setDouble(ii++, noObser);
					pStatement.setDouble(ii++, noObser);
					pStatement.setDouble(ii++, noObser);
					pStatement.setDouble(ii++, noObser);
					pStatement.setInt(ii++, noObserInt);
					pStatement.setInt(ii++, noObserInt);
					pStatement.setDouble(ii++, noObser);
					pStatement.setDouble(ii++, noObser);
					pStatement.setDouble(ii++, noObser);
					pStatement.setDouble(ii++, noObser);
//					+ "V11077,V11039,V11037,V11100,V11101,V11102,V11103,V11104,V02063,V02064,"
					pStatement.setDouble(ii++, noObser);
					pStatement.setInt(ii++, noObserInt);
					pStatement.setInt(ii++, noObserInt);
					pStatement.setDouble(ii++, noObser);
					pStatement.setDouble(ii++, noObser);
					pStatement.setDouble(ii++, noObser);
					pStatement.setDouble(ii++, noObser);
					pStatement.setDouble(ii++, noObser);
					pStatement.setDouble(ii++, noObser);
					pStatement.setDouble(ii++, noObser);
//					+ "Q07004,Q07010,Q07002,Q10070,Q08009,Q12001,Q02005,Q12003,Q13003,Q13002,"
					pStatement.setInt(ii++, 9);
					pStatement.setInt(ii++, 9);
					pStatement.setInt(ii++, 9);
					pStatement.setInt(ii++, 9);
					pStatement.setInt(ii++, 9);
					pStatement.setInt(ii++, 9);
					pStatement.setInt(ii++, 9);
					pStatement.setInt(ii++, 9);
					pStatement.setInt(ii++, 9);
					pStatement.setInt(ii++, 9);
//					+ "Q20043,Q20044,Q20045,Q33026,Q11001,Q11002,Q11034,Q11035,Q11036,Q11075,"
					pStatement.setInt(ii++, 9);
					pStatement.setInt(ii++, 9);
					pStatement.setInt(ii++, 9);
					pStatement.setInt(ii++, 9);
					pStatement.setInt(ii++, 9);
					pStatement.setInt(ii++, 9);
					pStatement.setInt(ii++, 9);
					pStatement.setInt(ii++, 9);
					pStatement.setInt(ii++, 9);
					pStatement.setInt(ii++, 9);
//					+ "Q11076,Q11077,Q11039,Q11037,Q11031,Q11100,Q11101,Q11102,Q11103,Q11104,"
					pStatement.setInt(ii++, 9);
					pStatement.setInt(ii++, 9);
					pStatement.setInt(ii++, 9);
					pStatement.setInt(ii++, 9);
					pStatement.setInt(ii++, 9);
					pStatement.setInt(ii++, 9);
					pStatement.setInt(ii++, 9);
					pStatement.setInt(ii++, 9);
					pStatement.setInt(ii++, 9);
					pStatement.setInt(ii++, 9);
//					+ "Q02063,Q02064,QR07004,QR07010,QR07002,QR08009,QR12001,QR12003,QR13003,"
					pStatement.setInt(ii++, 9);
					pStatement.setInt(ii++, 9);
					pStatement.setString(ii++, noObserStr);
					pStatement.setString(ii++, noObserStr);
					pStatement.setString(ii++, noObserStr);
					pStatement.setString(ii++, noObserStr);
					pStatement.setString(ii++, noObserStr);
					pStatement.setString(ii++, noObserStr);
					pStatement.setString(ii++, noObserStr);
//					+ "QR13002,QR11001,QR11002,QR11034,QR11035,QR11031) "
					pStatement.setString(ii++, noObserStr);
					pStatement.setString(ii++, noObserStr);
					pStatement.setString(ii++, noObserStr);
					pStatement.setString(ii++, noObserStr);
					pStatement.setString(ii++, noObserStr);
					pStatement.setString(ii++, noObserStr);
					
					di.setIIiii(udua.getPlaneID());
					di.setDATA_TIME(TimeUtil.date2String(udua.getDataTime(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(latitude));
					di.setLONGTITUDE(String.valueOf(longitude));
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
					di.setDATA_UPDATE_FLAG(udua.getCorrectSign());
					di.setHEIGHT(String.valueOf(udua.getHeightOfStandardPressure()));
					
					pStatement.addBatch();
					sqls.add(((LoggableStatement)pStatement).getQueryString()); // 批量入库冲突时，单条入库
					listDi.add(di);
				} // end for
				try{
					pStatement.executeBatch();
					connection.commit();
					loggerBuffer.append(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
					loggerBuffer.append(" INSERT SUCCESS COUNT ：" + uduas.size() + "\n");
					sqls.clear();
				}catch(SQLException e){
					pStatement.clearParameters();
					pStatement.clearBatch();
					connection.rollback();
					execute_sql(sqls, connection,fileN,loggerBuffer); // 此中，入库失败的会将对应的DI的ProcessState置为1 // 1:失败，0：成功
					loggerBuffer.append(" Batch ERROR : " + sdf.format(new Date()) + "\n");
				}
			}catch (SQLException e) {
				loggerBuffer.append("\n create Statement  error"+e.getMessage());
			}
			finally {
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
	 * @param  recv_time   资料时间
	 * @param loggerBuffer 
	 * @param fileN 
	 * @return: void      
	 * @throws:
	 */
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public static void reportInfoToDb( List<ReportInfo> reportInfos, java.sql.Connection connection, Date recv_time, String fileN, StringBuffer loggerBuffer) {
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO  "+StartConfig.reportTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,"
				+ "V_BBB,V_CCCC,V04001_02,V04002_02,V04003_02,V04004_02,V04005_02,V_TT,V_AA,V_II,"
				+ "V_MIMJ,V01301,V01300,V05001,V06001,V07002,V08009,V04001,V04002,V04003,"
				+ "V04004,V04005,V_LEN,V_REPORT) VALUES"
				+ "(?,?,?,?,?,?,"
				+ " ?,?,?,?,?,?,?,?,?,?,"
				+ " ?,?,?,?,?,?,?,?,?,?,"
				+ " ?,?,?,?)";
		try {
			pStatement = new LoggableStatement(connection, sql);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String V_TT= "";
			for (int i = 0; i < reportInfos.size(); i++) {
				try {
					UDUA udua = (UDUA) reportInfos.get(i).getT();
					String stat = udua.getPlaneID();
					V_TT = udua.getReportType().substring(0, 2);
//					Date oTime = udua.getObservationTime();
					Date dataTime = udua.getDataTime();
					// 修改oTime 2019-4-15
					Date oTime = new Date(dataTime.getTime());
//					oTime.setMinutes(0);
					double latitude=udua.getLatitude();
					double longitude=udua.getLongtitude();
					String station=udua.getPlaneID();
					double []cal=calcuLatAndLon(station, latitude, longitude, fileN);
					latitude=cal[0];
					longitude=cal[1];
					
					String lat = String.valueOf((int)(latitude * 1e6));
					String lon = String.valueOf((int)(longitude * 1e6));
					lat = lat.replaceAll("-", "0");
					lon = lon.replaceAll("-", "0");
//					String primkey = sdf.format(oTime)+"_"+stat+"_"+lat+"_"+lon+"_"+(int)(udua.getHeightOfStandardPressure()*1e4)+"_"+udua.getPlaneStateAndObsType()+"_"+V_TT+"_"+udua.getReportCenter()+"_"+udua.getCorrectSign();
//					String primkey = sdf.format(oTime)+"_" + sdf.format(dataTime)+"_"+udua.getPlaneID()+"_"+lat+"_"+lon+"_"+(int)(udua.getHeightOfStandardPressure()*1e4)+"_"+udua.getPlaneStateAndObsType();
					String primkey = sdf.format(dataTime)+"_"+udua.getPlaneID()+"_"+lat+"_"+lon+"_"+(int)(udua.getHeightOfStandardPressure()*1e4)+"_"+udua.getPlaneStateAndObsType();
					
					
					int ii = 1;
					pStatement.setString(ii++, primkey);
					pStatement.setString(ii++, sod_report_code);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(oTime.getTime()));
					//"V_BBB,V_CCCC,V04001_02,V04002_02,V04003_02,V04004_02,V04005_02,V_TT,V_AA,V_II,"
					pStatement.setString(ii++, udua.getCorrectSign());
					pStatement.setString(ii++, udua.getReportCenter());  // 编报中心
					pStatement.setInt(ii++, dataTime.getYear() + 1900);
					pStatement.setInt(ii++, dataTime.getMonth() + 1);
					pStatement.setInt(ii++, dataTime.getDate());
					pStatement.setInt(ii++, dataTime.getHours());
					pStatement.setInt(ii++, 0);
					pStatement.setString(ii++, V_TT);  // 资料类型
					pStatement.setString(ii++, udua.getReportType().substring(2, 4));
					pStatement.setString(ii++, udua.getReportType().substring(4, 6));
					//+ "V_MIMJ,V01301,V01300,V05001,V06001,V07002,V08009,V04001,V04002,V04003,"
					if(V_TT.equals("UD"))
						pStatement.setString(ii++, "AMDAR");
					else
						pStatement.setString(ii++, "AIREP");
					pStatement.setString(ii++, stat);  // 飞机标识
					pStatement.setInt(ii++, 999999);
					pStatement.setDouble(ii++, latitude);   // 纬度
					pStatement.setDouble(ii++, longitude); // 经度
					pStatement.setDouble(ii++, udua.getHeightOfStandardPressure());
					pStatement.setInt(ii++, udua.getPlaneStateAndObsType());
					pStatement.setInt(ii++, dataTime.getYear() + 1900);
					pStatement.setInt(ii++, dataTime.getMonth() + 1);
					pStatement.setInt(ii++, dataTime.getDate());
					
					//+ "V04004,V04005,V_LEN,V_REPORT)
					pStatement.setInt(ii++, dataTime.getHours());
					pStatement.setInt(ii++, dataTime.getMinutes());
					pStatement.setInt(ii++, reportInfos.get(i).getReport().length());
					pStatement.setString(ii++, reportInfos.get(i).getReport());
					
					pStatement.execute();
					if(connection.getAutoCommit() == false)
						connection.commit();
				} catch (Exception e) {
					loggerBuffer.append("sql error:" +e.getMessage()+ ((LoggableStatement)pStatement).getQueryString());
					continue;
				}
			} // end for 
			if(pStatement != null) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n close Statement error"+e.getMessage());
				}
			}
		} catch (SQLException e) {
			loggerBuffer.append("database connection exception" + e.getMessage());
			return;
		}
	}	
	/**
	 * 
	 * @Title: updateEle   
	 * @Description:(更正报报文的更新)   
	 * @param  uduas 待入库的对象
	 * @param  connection 数据库连接
	 * @param  recv_time   数据接收时间  
	 * @throws:
	 */
	@SuppressWarnings("unused")
	private void updateEle(List<UDUA> uduas, java.sql.Connection connection, Date recv_time,String fileN,StringBuffer loggerBuffer, String filepath){
		PreparedStatement Pstmt = null;
		String vbbbInDB = null;
		UDUA udua = new UDUA();
		String sql = "update "+StartConfig.valueTable()+" set "
				+ "V_BBB=?, D_UPDATE_TIME=?,"
				+ "V07010=?,V12001=?,V12003=?,V13003=?,V11001=?,V11002=?,V11031=?,V02061=?,V02062=?,V02005=?,V11036=? "
				+ "where v01301 = ? and D_datetime = ? and V05001>? and V05001<? and V06001>? and V06001<? and V07002>? and V07002<? "
				+ " and V08009 = ?";
		if(connection != null){
			try{
				connection.setAutoCommit(true);
				Pstmt = new LoggableStatement(connection, sql);
				for(int idx = 0; idx < uduas.size(); idx ++){
					udua = uduas.get(idx);
					vbbbInDB = findVBB(udua, connection,loggerBuffer); // 插入前，从DB中查找该条记录的更正状态
					if(vbbbInDB.compareTo(udua.getCorrectSign()) < 0){ // 数据库中有该条记录，且V_BBB的值早于当前处理文件的更正标识的值
						// update
						StatDi di = new StatDi();				
						di.setFILE_NAME_O(fileN);
						di.setDATA_TYPE(sod_code);
						di.setDATA_TYPE_1(cts_code);
						di.setTT(udua.getReportType().substring(0, 2));			
						di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
						di.setPROCESS_START_TIME(TimeUtil.getSysTime());
						di.setFILE_NAME_N(fileN);
						di.setBUSINESS_STATE("1"); //0成功，1失败
						di.setPROCESS_STATE("1");  //0成功，1失败	
						
						Date date = udua.getDataTime();
						int ii = 1;
						Pstmt.setString(ii++, udua.getCorrectSign());
						Pstmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						
						Pstmt.setBigDecimal(ii++, new BigDecimal(udua.getAircraftHeightAboveSeaLevel()).setScale(4, BigDecimal.ROUND_HALF_UP));
						Pstmt.setBigDecimal(ii++, new BigDecimal(udua.getTemperature()).setScale(4, BigDecimal.ROUND_HALF_UP));
						Pstmt.setBigDecimal(ii++, new BigDecimal(udua.getDewPoint()).setScale(4, BigDecimal.ROUND_HALF_UP));
						Pstmt.setBigDecimal(ii++, new BigDecimal(udua.getRelativehumidity()).setScale(4, BigDecimal.ROUND_HALF_UP));
						Pstmt.setInt(ii++, udua.getWindDir());
						Pstmt.setBigDecimal(ii++, new BigDecimal(udua.getWindSpeed()).setScale(4, BigDecimal.ROUND_HALF_UP));
						Pstmt.setInt(ii++, udua.getTurbulence());
						Pstmt.setInt(ii++, udua.getNaviType());
						Pstmt.setInt(ii++, udua.getDataTransType());
						Pstmt.setInt(ii++, udua.getTemperatureObsAccuCode());
						Pstmt.setBigDecimal(ii++, new BigDecimal(udua.getVerticalWindSpeed()).setScale(4, BigDecimal.ROUND_HALF_UP));
						
						Pstmt.setString(ii++, udua.getPlaneID());
//						Pstmt.setInt(ii++, date.getYear() + 1900);
//						Pstmt.setInt(ii++, date.getMonth() + 1);
//						Pstmt.setInt(ii++, date.getDate());
//						Pstmt.setInt(ii++, date.getHours());
//						Pstmt.setInt(ii++, date.getMinutes());
						Pstmt.setTimestamp(ii++, new Timestamp(date.getTime()));
						
						Pstmt.setBigDecimal(ii++, new BigDecimal(udua.getLatitude() - 1e-3).setScale(4, BigDecimal.ROUND_HALF_UP));
						Pstmt.setBigDecimal(ii++, new BigDecimal(udua.getLatitude() + 1e-3).setScale(4, BigDecimal.ROUND_HALF_UP));
						Pstmt.setBigDecimal(ii++, new BigDecimal(udua.getLongtitude() - 1e-3).setScale(4, BigDecimal.ROUND_HALF_UP));
						Pstmt.setBigDecimal(ii++, new BigDecimal(udua.getLongtitude() + 1e-3).setScale(4, BigDecimal.ROUND_HALF_UP));
						Pstmt.setBigDecimal(ii++, new BigDecimal(udua.getHeightOfStandardPressure() - 1e-3).setScale(4, BigDecimal.ROUND_HALF_UP));
						Pstmt.setBigDecimal(ii++, new BigDecimal(udua.getHeightOfStandardPressure() + 1e-3).setScale(4, BigDecimal.ROUND_HALF_UP));
						Pstmt.setInt(ii++, udua.getPlaneStateAndObsType());
						
						di.setIIiii(udua.getPlaneID());
						di.setDATA_TIME(TimeUtil.date2String(udua.getDataTime(), "yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setLATITUDE(String.valueOf(udua.getLatitude()));
						di.setLONGTITUDE(String.valueOf(udua.getLongtitude()));
						
						di.setSEND("BFDB");
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
						di.setDATA_UPDATE_FLAG(udua.getCorrectSign());
						di.setHEIGHT(String.valueOf(udua.getHeightOfStandardPressure()));
						
						listDi.add(di);
						try{
							Pstmt.execute();
							connection.commit();
						}catch(SQLException e){							
							if(listDi.size() > 0)
								listDi.get(listDi.size() - 1).setPROCESS_STATE("0");//1成功，0失败
							loggerBuffer.append("\n filename："+fileN
									+"\n excute sql error："+((LoggableStatement)Pstmt).getQueryString()+"\n "+e.getMessage());
						}	
					} 
					else{ // 数据库中有该条记录，且V_BBB的值晚于当前处理文件的更正标识的值
						// 不进行插入或更新
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
			loggerBuffer.append("\n database connection exception");		
	}
	
	/**
	 * 
	 * @Title: findVBB   
	 * @Description:(查找飞机高空探测报要素数据的更正标识)
	 * @param udua  查找对象
	 * @param  connection  数据库连接   
	 * @return: String      返回查找到的V_BBB，未找到时，返回null
	 * @throws:
	 */
	@SuppressWarnings("deprecation")
	private String findVBB(UDUA udua, java.sql.Connection connection,StringBuffer loggerBuffer){
		PreparedStatement Pstmt = null;
		ResultSet resultSet  = null;
		String rntString = null;
		String sql = "select V_BBB from "+StartConfig.valueTable()+" "
				+ "where V01301 = ? and D_datetime = ? and v05001>? and V05001<? and v06001>? and V06001<?"
				+ " and v07002>? and V07002<? and v08009=? ";
		try{
			if(connection != null){	
				Pstmt = connection.prepareStatement(sql);
				Date date = udua.getDataTime();
				int ii = 1;
				Pstmt.setString(ii++, udua.getCorrectSign());
//				Pstmt.setInt(ii++, date.getYear() + 1900);
//				Pstmt.setInt(ii++, date.getMonth()  + 1);
//				Pstmt.setInt(ii++, date.getDate());
//				Pstmt.setInt(ii++, date.getHours());
//				Pstmt.setInt(ii++, date.getMinutes());
				Pstmt.setTimestamp(ii++, new Timestamp(date.getTime()));
				
				Pstmt.setBigDecimal(ii++, new BigDecimal(udua.getLatitude() - 1e-3).setScale(4, BigDecimal.ROUND_HALF_UP));
				Pstmt.setBigDecimal(ii++, new BigDecimal(udua.getLatitude() + 1e-3).setScale(4, BigDecimal.ROUND_HALF_UP));
				Pstmt.setBigDecimal(ii++, new BigDecimal(udua.getLongtitude() - 1e-3).setScale(4, BigDecimal.ROUND_HALF_UP));
				Pstmt.setBigDecimal(ii++, new BigDecimal(udua.getLongtitude() + 1e-3).setScale(4, BigDecimal.ROUND_HALF_UP));
				Pstmt.setBigDecimal(ii++, new BigDecimal(udua.getHeightOfStandardPressure() - 1e-3).setScale(4, BigDecimal.ROUND_HALF_UP));
				Pstmt.setBigDecimal(ii++, new BigDecimal(udua.getHeightOfStandardPressure() + 1e-3).setScale(4, BigDecimal.ROUND_HALF_UP));
				Pstmt.setInt(ii++, udua.getPlaneStateAndObsType());
				
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
				String info = (String) proMap.get(station + "+04");
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
					latitude= Double.parseDouble(String.format("%.6f", latitude));
					longitude= Double.parseDouble(String.format("%.6f", longitude));
					LatAndLon[0]=latitude;
					LatAndLon[1]=longitude;
				}
			}else{
				latitude= Double.parseDouble(String.format("%.6f", latitude));
				longitude= Double.parseDouble(String.format("%.6f", longitude));
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
			String filepath ="D:\\TEMP\\B.1.9.1\\UD_A_UDOC01AMMC060200RRA_C_BABJ_20190806020035_42333.MSG";
			File file = new File(filepath);
			String fileN = file.getName();
			Date recv_time = new Date(file.lastModified());
			StringBuffer loggerBuffer = new StringBuffer();
			DecodeUDUA decodeUDUA= new DecodeUDUA();
			ParseResult<UDUA> parseResult = decodeUDUA.DecodeFile(file);			
			if(parseResult.isSuccess()){
				DataBaseAction action = DbService.processSuccessReport(parseResult, recv_time,fileN,loggerBuffer, filepath);	
			}
		}
}
