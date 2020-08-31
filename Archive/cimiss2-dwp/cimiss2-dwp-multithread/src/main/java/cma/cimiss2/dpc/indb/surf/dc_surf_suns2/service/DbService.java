package cma.cimiss2.dpc.indb.surf.dc_surf_suns2.service;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
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
import cma.cimiss2.dpc.decoder.bean.QCElement;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.surf.SurfWeaChnSsdHor;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class DbService {
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static String cts_code = StartConfig.ctsCode();
	public static String hour_sod_code = StartConfig.sodCodes()[0];
	public static String day_sod_code = StartConfig.sodCodes()[1];
	public static String rep_sod_code = StartConfig.reportSodCode();
	public int defaultInt = 999999;
//	static Map<String, Object> proMap = StationInfo.getProMap();
	
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}
	/**
	 * @param loggerBuffer 
	 * @param fileN
	 * @Title: processSuccessReport 
	 * @Description:(质控后地面日照_一体化观测数据 A.0001.0031.R001解码结果集，正确解码的报文入库) 
	 * @param parseResult  解码结果集
	 * @param filepath  文件的绝对路径
	 * @param recv_time  消息接收时间
	 * @return  返回值说明
	 * @throws
	 */

	public static DataBaseAction processSuccessReport(ParseResult<SurfWeaChnSsdHor> parseResult, Date recv_time, String fileN, StringBuffer loggerBuffer, String filepath) {
		java.sql.Connection connection = null;
		java.sql.Connection reportConnection = null;
		try {
			// 获取数据库连接
			connection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
			List<SurfWeaChnSsdHor> surfSunDatas = parseResult.getData();
			insertHourDB(surfSunDatas, connection, recv_time, fileN, loggerBuffer,filepath);
			insertDayDB(surfSunDatas, connection, recv_time, fileN, loggerBuffer,filepath);
		 
			@SuppressWarnings("rawtypes")
			List<ReportInfo> reportInfos = parseResult.getReports();
			reportConnection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
			reportInfoToDb(reportInfos, reportConnection, recv_time, filepath ,loggerBuffer);
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
					loggerBuffer.append("\n Close databse connection error: "+e.getMessage());
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
	 * @param loggerBuffer 
	 * @param fileN 
	 * 
	 * @Title: insertDB   
	 * @Description: (质控后地面日照_一体化观测数据surf_wea_chn_ssd_hor_tab表入库)   
	 * @param: @param acidrain 入库对象集合
	 * @param: @param connection 数据库连接
	 * @param: @param recv_time  接收时间
	 * @return: void      
	 * @throws
	 */
	@SuppressWarnings("deprecation")
	private static void insertHourDB(List<SurfWeaChnSsdHor> surfSunDatas, java.sql.Connection connection,
			Date recv_time,String fileN, StringBuffer loggerBuffer, String filepath) {
		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.keyTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,"
				+ "V01301,V01300,V05001,V06001,V04001,V04002,V04003,V07001,V02301,V_ACODE,V14332,V14032,V14032_001,V14032_002,V14032_003,V14032_004,V14032_005,"
				+ "V14032_006,V14032_007,V14032_008,V14032_009,V14032_010,V14032_011,V14032_012,V14032_013,V14032_014,V14032_015,"
				+ "V14032_016,V14032_017,V14032_018,V14032_019,V14032_020,V14032_021,V14032_022,V14032_023,V14032_024,Q14032_001,"
				+ "Q14032_002,Q14032_003,Q14032_004,Q14032_005,Q14032_006,Q14032_007,Q14032_008,Q14032_009,Q14032_010,Q14032_011,"
				+ "Q14032_012,Q14032_013,Q14032_014,Q14032_015,Q14032_016,Q14032_017,Q14032_018,Q14032_019,Q14032_020,Q14032_021,"
				+ "Q14032_022,Q14032_023,Q14032_024,Q14032,V_BBB,D_SOURCE_ID) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?,?, ?, ?, ?,?) ";
		if(connection != null){		
			try {	
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");		
				for(int i = 0; i <surfSunDatas.size(); i ++){
					pStatement = new LoggableStatement(connection, sql);
					connection.setAutoCommit(false);
					SurfWeaChnSsdHor surfSunData = surfSunDatas.get(i);
					if(!TimeCheckUtil.checkTime(surfSunData.getObservationTime())){
						infoLogger.info("\n datetime out of range! "+"sation: "+surfSunData.getStationNumberChina()+" file:"+fileN);
						continue;
					}
					
					StatDi di = new StatDi();	
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(hour_sod_code);
					di.setDATA_TYPE_1(cts_code);
					di.setTT("质控后地面日照资料");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1");  //1成功，0失败	
					String format = new SimpleDateFormat("yyyyMMdd").format(surfSunData.getObservationTime());
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(surfSunData.getObservationTime());
					calendar.set(Calendar.HOUR_OF_DAY, 0);
					calendar.set(Calendar.MINUTE, 0);
					calendar.set(Calendar.SECOND, 0);
					calendar.set(Calendar.MILLISECOND, 0);
					
					String stationNumberChina = surfSunData.getStationNumberChina().toUpperCase();
//					int num = stationNumberChina.substring(0, 1).toUpperCase().hashCode();
					int ii = 1;
					pStatement.setString(ii++, format + "000000" +"_"+surfSunData.getStationNumberChina());//记录标识
					pStatement.setString(ii++, hour_sod_code);//资料标识
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//入库时间
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));//收到时间
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间
					pStatement.setTimestamp(ii++, new Timestamp(new Date(calendar.getTime().getTime()).getTime()));//资料时间
					pStatement.setString(ii++, stationNumberChina);//区站号(字符)
					pStatement.setInt(ii++,Integer.parseInt(StationCodeUtil.stringToAscii(stationNumberChina)) );//区站号(数字)
					pStatement.setBigDecimal(ii++,  new BigDecimal(String.valueOf(surfSunData.getLatitude())));//V05001
					pStatement.setBigDecimal(ii++,  new BigDecimal(String.valueOf(surfSunData.getLongitude())));//V06001
					pStatement.setInt(ii++, Integer.parseInt(format.substring(0,4)));//V04001
					pStatement.setInt(ii++, Integer.parseInt(format.substring(4,6)));//V04002
					pStatement.setInt(ii++, Integer.parseInt(format.substring(6,8)));//V04003
					String info = (String) proMap.get(stationNumberChina + "+01");
					String V07001="999999";
					String V02301="999999";
					String V_ACODE="999999";
					if(info == null||"".equals(info)) {
						pStatement.setBigDecimal(ii++, new BigDecimal("999999"));
						pStatement.setBigDecimal(ii++, new BigDecimal("999999"));
						pStatement.setBigDecimal(ii++, new BigDecimal("999999"));
					}else{
						String[] infos = info.split(",");
						 V07001=("null".equals(infos[3])||"".equals(infos[3])) ? "999999" : infos[3];
						 V02301=("null".equals(infos[6])||"".equals(infos[6])) ? "999999" : infos[6];
						 V_ACODE=("null".equals(infos[5])||"".equals(infos[5])) ? "999999" : infos[5];
						
						pStatement.setBigDecimal(ii++,  new BigDecimal(V07001));//V07001
						pStatement.setBigDecimal(ii++,  new BigDecimal(V02301));//V02301
						pStatement.setBigDecimal(ii++,  new BigDecimal(V_ACODE));//V_ACODE
					}
					 pStatement.setBigDecimal(ii++,  new BigDecimal(String.valueOf(surfSunData.getTimeSystem())));//V14332 日照时制方式
					 pStatement.setBigDecimal(ii++,  new BigDecimal(String.valueOf(surfSunData.getTotalSunshinDay().getValue())));//V14032 日照时数（直接辐射计算值）
					 List<QCElement<Double>> totalSunshineveryHour = surfSunData.getTotalSunshineveryHour();
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(0).getValue().toString()));//V14032_001
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(1).getValue().toString()));//V14032_002
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(2).getValue().toString()));//V14032_003
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(3).getValue().toString()));//V14032_004
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(4).getValue().toString()));//V14032_005
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(5).getValue().toString()));//V14032_006
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(6).getValue().toString()));//V14032_007
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(7).getValue().toString()));//V14032_008
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(8).getValue().toString()));//V14032_009
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(9).getValue().toString()));//V14032_010
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(10).getValue().toString()));//V14032_011
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(11).getValue().toString()));//V14032_012
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(12).getValue().toString()));//V14032_013
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(13).getValue().toString()));//V14032_014
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(14).getValue().toString()));//V14032_015
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(15).getValue().toString()));//V14032_016
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(16).getValue().toString()));//V14032_017
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(17).getValue().toString()));//V14032_018
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(18).getValue().toString()));//V14032_019
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(19).getValue().toString()));//V14032_020
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(20).getValue().toString()));//V14032_021
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(21).getValue().toString()));//V14032_022
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(22).getValue().toString()));//V14032_023
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(23).getValue().toString()));//V14032_024
					 
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(0).getQuality().get(0).getCode()));//Q14032_001
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(1).getQuality().get(0).getCode()));//Q14032_002
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(2).getQuality().get(0).getCode()));//Q14032_003
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(3).getQuality().get(0).getCode()));//Q14032_004
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(4).getQuality().get(0).getCode()));//Q14032_005
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(5).getQuality().get(0).getCode()));//Q14032_006
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(6).getQuality().get(0).getCode()));//Q14032_007
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(7).getQuality().get(0).getCode()));//Q14032_008
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(8).getQuality().get(0).getCode()));//Q14032_009
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(9).getQuality().get(0).getCode()));//Q14032_010
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(10).getQuality().get(0).getCode()));//Q14032_011
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(11).getQuality().get(0).getCode()));//Q14032_012
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(12).getQuality().get(0).getCode()));//Q14032_013
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(13).getQuality().get(0).getCode()));//Q14032_014
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(14).getQuality().get(0).getCode()));//Q14032_015
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(15).getQuality().get(0).getCode()));//Q14032_016
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(16).getQuality().get(0).getCode()));//Q14032_017
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(17).getQuality().get(0).getCode()));//Q14032_018
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(18).getQuality().get(0).getCode()));//Q14032_019
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(19).getQuality().get(0).getCode()));//Q14032_020
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(20).getQuality().get(0).getCode()));//Q14032_021
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(21).getQuality().get(0).getCode()));//Q14032_022
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(22).getQuality().get(0).getCode()));//Q14032_023
					 pStatement.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(23).getQuality().get(0).getCode()));//Q14032_024
					 pStatement.setBigDecimal(ii++,  new BigDecimal(surfSunData.getTotalSunshinDay().getQuality().get(0).getCode()));//Q14032
					 pStatement.setString(ii++, surfSunData.getCorrectionIndicator());//文件更正标识
					 pStatement.setString(ii++, cts_code);
					
					di.setIIiii(surfSunData.getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(calendar.getTime(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(surfSunData.getLatitude()));
					di.setLONGTITUDE(String.valueOf(surfSunData.getLongitude()));
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
					di.setDATA_UPDATE_FLAG(surfSunData.getCorrectionIndicator());
					di.setHEIGHT(String.valueOf(V07001));
					listDi.add(di);
					try{
						pStatement.execute();
						connection.commit();
						loggerBuffer.append("\n 成功插入表"+StartConfig.keyTable()+"一条数据 ！: "+fileN+" :"+((LoggableStatement)pStatement).getQueryString());
					}catch(SQLException e){
						if (e.getErrorCode() == 13001 || e.getErrorCode() == 1062) {//主键冲突，则进一步判断是否是更正报
							String[] Existresult=findExistData(surfSunData,StartConfig.keyTable(),connection);
							String d_datetime=Existresult[0];
							String v_bbb=Existresult[1];//库里原有更正标识
							String V_BBB=surfSunData.getCorrectionIndicator();//当前报文的更正标识
							if (d_datetime!=null) {//库里有记录
								if(V_BBB.equals("000")&&(v_bbb==null||(v_bbb!=null&&v_bbb.equals("000"))||(v_bbb!=null&&v_bbb.startsWith("RR")))){
									updateHourData(surfSunData,connection,fileN,recv_time,loggerBuffer, di);
								}
								if(V_BBB.startsWith("RR")&&(v_bbb==null||(v_bbb!=null&&v_bbb.equals("000"))||(v_bbb!=null&&v_bbb.startsWith("RR")))){
									updateHourData(surfSunData,connection,fileN,recv_time,loggerBuffer, di);	
								}
								if(V_BBB.startsWith("C")&& (v_bbb==null||(v_bbb!=null&&(V_BBB.compareTo(v_bbb) > 0)) ||v_bbb.startsWith("RR"))){
									updateHourData(surfSunData,connection,fileN,recv_time,loggerBuffer, di);
								}
							}
						}else{
							loggerBuffer.append("\n "+StartConfig.keyTable()+"表插入一条数据失败 ！: "+fileN+" :"+e.getMessage());
							di.setPROCESS_STATE("0");// 1成功，0失败
						}
						
					}finally {
						if(pStatement != null) {
							try {
								pStatement.close();
							} catch (SQLException e) {
								loggerBuffer.append("\n insertHourDB Close Statement failed: "+e.getMessage());
							}
						}
					}
				} // end for
			}catch (SQLException e) {
				loggerBuffer.append("\n insertHourDB Create Statement failed: "+e.getMessage());
			}finally {
				if(pStatement != null) {
					try {
						pStatement.close();
					} catch (SQLException e) {
						loggerBuffer.append("\n insertHourDB Close Statement failed: "+e.getMessage());
					}
				}
			}
		} 	
	}
	private static void  updateHourData(SurfWeaChnSsdHor surfSunData,Connection connection,String fileN,Date recv_time,StringBuffer loggerBuffer,StatDi di){
		Map<String, Object> proMap = StationInfo.getProMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		PreparedStatement pStat = null;
		try{
//			String updateSql="update "+StartConfig.keyTable()+" set  D_RYMDHM=?, D_UPDATE_TIME=?, V01301=?, V01300=?, V05001=?, V06001=?, V04001=?, V04002=?, V04003=?, V07001=?, V02301=?, V_ACODE=?, V14332=?, V14032=?, V14032_001=?, V14032_002=?, V14032_003=?, V14032_004=?, V14032_005=?, V14032_006=?, V14032_007=?, V14032_008=?, V14032_009=?, V14032_010=?, V14032_011=?, V14032_012=?, V14032_013=?, V14032_014=?, V14032_015=?, V14032_016=?, V14032_017=?, V14032_018=?, V14032_019=?, V14032_020=?, V14032_021=?, V14032_022=?, V14032_023=?, V14032_024=?, Q14032_001=?, Q14032_002=?, Q14032_003=?, Q14032_004=?, Q14032_005=?, Q14032_006=?, Q14032_007=?, Q14032_008=?, Q14032_009=?, Q14032_010=?, Q14032_011=?, Q14032_012=?, Q14032_013=?, Q14032_014=?, Q14032_015=?, Q14032_016=?, Q14032_017=?, Q14032_018=?, Q14032_019=?, Q14032_020=?, Q14032_021=?, Q14032_022=?, Q14032_023=?, Q14032_024=?, Q14032=?, V_BBB=?  where D_RECORD_ID = ?  and d_datetime= ? ";
//			String updateSql="update "+StartConfig.keyTable()+" set  D_RYMDHM=?, D_UPDATE_TIME=?, V01301=?, V01300=?, V05001=?, V06001=?, V04001=?, V04002=?, V04003=?, V07001=?, V02301=?, V_ACODE=?, V14332=?, V14032=? ,V14032_001=?   where d_datetime= ? and D_RECORD_ID = ?  ";
//			String updateSql="update "+StartConfig.keyTable()+" set  V14032_002=?, V14032_003=?, V14032_004=?, V14032_004=?, V14032_005=?, V14032_006=?, V14032_007=?, V14032_008=?, V14032_009=?, V14032_010=?, V14032_011=?, V14032_012=?, V14032_013=?, V14032_014=?, V14032_015=?, V14032_016=?, V14032_017=?, V14032_018=?, V14032_019=?, V14032_020=?, V14032_021=?, V14032_022=?, V14032_023=?, V14032_024=?, Q14032_001=?, Q14032_002=?, Q14032_003=?, Q14032_004=?, Q14032_005=?, Q14032_006=?, Q14032_007=?, Q14032_008=?, Q14032_009=?, Q14032_010=?, Q14032_011=?, Q14032_012=?, Q14032_013=?, Q14032_014=?, Q14032_015=?, Q14032_016=?, Q14032_017=?, Q14032_018=?, Q14032_019=?, Q14032_020=?, Q14032_021=?, Q14032_022=?, Q14032_023=?, Q14032_024=?, Q14032=?, V_BBB=?  where d_datetime= ? and D_RECORD_ID = ?  ";
//			String updateSql="update "+StartConfig.keyTable()+" set  D_RYMDHM=?, D_UPDATE_TIME=?, V01301=?, V01300=?, V05001=?, V06001=?, V04001=?, V04002=?, V04003=?, V07001=?, V02301=?, V_ACODE=?, V14332=?, V14032=? ,V14032_001=?, V14032_002=?, V14032_003=?, V14032_004=?, V14032_004=?, V14032_005=?, V14032_006=?, V14032_007=?, V14032_008=?, V14032_009=?, V14032_010=?, V14032_011=?, V14032_012=?, V14032_013=?, V14032_014=?, V14032_015=?, V14032_016=?, V14032_017=?, V14032_018=?, V14032_019=?, V14032_020=?, V14032_021=?, V14032_022=?, V14032_023=?, V14032_024=?, Q14032_001=?, Q14032_002=?, Q14032_003=?, Q14032_004=?, Q14032_005=?, Q14032_006=?, Q14032_007=?, Q14032_008=?, Q14032_009=?, Q14032_010=?, Q14032_011=?, Q14032_012=?, Q14032_013=?, Q14032_014=?, Q14032_015=?, Q14032_016=?, Q14032_017=?, Q14032_018=?, Q14032_019=?, Q14032_020=?, Q14032_021=?, Q14032_022=?, Q14032_023=?, Q14032_024=?, Q14032=?, V_BBB=?   where d_datetime= ? and D_RECORD_ID = ?  ";
			String updateSql="update "+StartConfig.keyTable()+" set  D_RYMDHM=?, D_UPDATE_TIME=?, V01301=?, V01300=?, V05001=?, V06001=?, V04001=?, V04002=?, V04003=?, V07001=?, V02301=?, V_ACODE=?, V14332=?, V14032=? ,V14032_001=?, V14032_002=?, V14032_003=?, V14032_004=?, V14032_005=?, V14032_006=?, V14032_007=?, V14032_008=?, V14032_009=?, V14032_010=?, V14032_011=?, V14032_012=?, V14032_013=?, V14032_014=?, V14032_015=?, V14032_016=?, V14032_017=?, V14032_018=?, V14032_019=?, V14032_020=?, V14032_021=?, V14032_022=?, V14032_023=?, V14032_024=?, Q14032_001=?, Q14032_002=?, Q14032_003=?, Q14032_004=?, Q14032_005=?, Q14032_006=?, Q14032_007=?, Q14032_008=?, Q14032_009=?, Q14032_010=?, Q14032_011=?, Q14032_012=?, Q14032_013=?, Q14032_014=?, Q14032_015=?, Q14032_016=?, Q14032_017=?, Q14032_018=?, Q14032_019=?, Q14032_020=?, Q14032_021=?, Q14032_022=?, Q14032_023=?, Q14032_024=?, Q14032=?, V_BBB=?   where d_datetime= ? and V01301 = ?  ";

			pStat = new LoggableStatement(connection, updateSql);
			
			String format = new SimpleDateFormat("yyyyMMdd").format(surfSunData.getObservationTime());
			String stationNumberChina = surfSunData.getStationNumberChina().toUpperCase();
			int ii = 1;
			pStat.setTimestamp(ii++, new Timestamp(recv_time.getTime()));//收到时间
			pStat.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间
			pStat.setString(ii++, stationNumberChina);//区站号(字符)
			pStat.setInt(ii++,Integer.parseInt(StationCodeUtil.stringToAscii(stationNumberChina)) );//区站号(数字)
			pStat.setBigDecimal(ii++,  new BigDecimal(String.valueOf(surfSunData.getLatitude())));//V05001
			pStat.setBigDecimal(ii++,  new BigDecimal(String.valueOf(surfSunData.getLongitude())));//V06001
			pStat.setInt(ii++, Integer.parseInt(format.substring(0,4)));//V04001
			pStat.setInt(ii++, Integer.parseInt(format.substring(4,6)));//V04002
			pStat.setInt(ii++, Integer.parseInt(format.substring(6,8)));//V04003
			String info = (String) proMap.get(stationNumberChina + "+01");
			String V07001="999999";
			String V02301="999999";
			String V_ACODE="999999";
			if(info == null||"".equals(info)) {
				pStat.setBigDecimal(ii++, new BigDecimal("999999"));
				pStat.setBigDecimal(ii++, new BigDecimal("999999"));
				pStat.setBigDecimal(ii++, new BigDecimal("999999"));
			}else{
				String[] infos = info.split(",");
				 V07001=("null".equals(infos[3])||"".equals(infos[3])) ? "999999" : infos[3];
				 V02301=("null".equals(infos[6])||"".equals(infos[6])) ? "999999" : infos[6];
				 V_ACODE=("null".equals(infos[5])||"".equals(infos[5])) ? "999999" : infos[5];
				
				pStat.setBigDecimal(ii++,  new BigDecimal(V07001));//V07001
				pStat.setBigDecimal(ii++,  new BigDecimal(V02301));//V02301
				pStat.setBigDecimal(ii++,  new BigDecimal(V_ACODE));//V_ACODE
			}
			 pStat.setBigDecimal(ii++,  new BigDecimal(String.valueOf(surfSunData.getTimeSystem())));//V14332 日照时制方式
			 pStat.setBigDecimal(ii++,  new BigDecimal(String.valueOf(surfSunData.getTotalSunshinDay().getValue())));//V14032 日照时数（直接辐射计算值）
			 List<QCElement<Double>> totalSunshineveryHour = surfSunData.getTotalSunshineveryHour();
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(0).getValue().toString()));//V14032_001
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(1).getValue().toString()));//V14032_002
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(2).getValue().toString()));//V14032_003
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(3).getValue().toString()));//V14032_004
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(4).getValue().toString()));//V14032_005
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(5).getValue().toString()));//V14032_006
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(6).getValue().toString()));//V14032_007
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(7).getValue().toString()));//V14032_008
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(8).getValue().toString()));//V14032_009
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(9).getValue().toString()));//V14032_010
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(10).getValue().toString()));//V14032_011
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(11).getValue().toString()));//V14032_012
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(12).getValue().toString()));//V14032_013
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(13).getValue().toString()));//V14032_014
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(14).getValue().toString()));//V14032_015
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(15).getValue().toString()));//V14032_016
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(16).getValue().toString()));//V14032_017
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(17).getValue().toString()));//V14032_018
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(18).getValue().toString()));//V14032_019
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(19).getValue().toString()));//V14032_020
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(20).getValue().toString()));//V14032_021
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(21).getValue().toString()));//V14032_022
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(22).getValue().toString()));//V14032_023
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(23).getValue().toString()));//V14032_024
			 
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(0).getQuality().get(0).getCode()));//Q14032_001
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(1).getQuality().get(0).getCode()));//Q14032_002
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(2).getQuality().get(0).getCode()));//Q14032_003
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(3).getQuality().get(0).getCode()));//Q14032_004
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(4).getQuality().get(0).getCode()));//Q14032_005
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(5).getQuality().get(0).getCode()));//Q14032_006
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(6).getQuality().get(0).getCode()));//Q14032_007
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(7).getQuality().get(0).getCode()));//Q14032_008
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(8).getQuality().get(0).getCode()));//Q14032_009
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(9).getQuality().get(0).getCode()));//Q14032_010
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(10).getQuality().get(0).getCode()));//Q14032_011
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(11).getQuality().get(0).getCode()));//Q14032_012
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(12).getQuality().get(0).getCode()));//Q14032_013
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(13).getQuality().get(0).getCode()));//Q14032_014
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(14).getQuality().get(0).getCode()));//Q14032_015
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(15).getQuality().get(0).getCode()));//Q14032_016
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(16).getQuality().get(0).getCode()));//Q14032_017
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(17).getQuality().get(0).getCode()));//Q14032_018
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(18).getQuality().get(0).getCode()));//Q14032_019
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(19).getQuality().get(0).getCode()));//Q14032_020
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(20).getQuality().get(0).getCode()));//Q14032_021
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(21).getQuality().get(0).getCode()));//Q14032_022
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(22).getQuality().get(0).getCode()));//Q14032_023
			 pStat.setBigDecimal(ii++,  new BigDecimal(totalSunshineveryHour.get(23).getQuality().get(0).getCode()));//Q14032_024
			 pStat.setBigDecimal(ii++,  new BigDecimal(surfSunData.getTotalSunshinDay().getQuality().get(0).getCode()));//Q14032

			 pStat.setString(ii++, surfSunData.getCorrectionIndicator());//文件更正标识
			 
		 	 String datetime=format + "000000";
		 	 Calendar calendar = Calendar.getInstance();
			 calendar.setTime(surfSunData.getObservationTime());
			 calendar.set(Calendar.HOUR_OF_DAY, 0);
			 calendar.set(Calendar.MINUTE, 0);
			 calendar.set(Calendar.SECOND, 0);
			 calendar.set(Calendar.MILLISECOND, 0);
			 pStat.setTimestamp(ii++, new Timestamp(new Date(calendar.getTime().getTime()).getTime()));//资料时间
//			 pStat.setString(ii++, format + "000000" +"_"+surfSunData.getStationNumberChina());//记录标识
			 pStat.setString(ii++, surfSunData.getStationNumberChina());//站号
			try {
				pStat.execute();
				connection.commit();
				loggerBuffer.append("\n"+ StartConfig.keyTable()+"成功更新一条数据！"+fileN+"ObservationTime:"+surfSunData.getObservationTime()+" StationNumber:"+surfSunData.getStationNumberChina());
			} catch (Exception e) {
				di.setPROCESS_STATE("0");// 1成功，0失败
				loggerBuffer.append("\n "+ StartConfig.keyTable()+"更新数据 失败！"+((LoggableStatement)pStat).getQueryString()+e.getMessage());
			}
		}catch (Exception e) {
			loggerBuffer.append("\n updateHourData create Statement failed: "+e.getMessage());
		}finally {
			if(pStat != null) {
				try {
					pStat.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n  updateHourData Close Statement failed: "+e.getMessage());
				}
			}
		}
	}
	
	/**
	 * @param loggerBuffer 
	 * @param fileN 
	 * 
	 * @Title: insertDB   
	 * @Description: (质控后地面日照_一体化观测数据surf_wea_chn_mul_day_tab表入库)   
	 * @param: @param acidrain 入库对象集合
	 * @param: @param connection 数据库连接
	 * @param: @param recv_time  接收时间
	 * @return: void      
	 * @throws
	 */
	@SuppressWarnings("deprecation")
	private static void insertDayDB(List<SurfWeaChnSsdHor> surfSunDatas, java.sql.Connection connection,
			Date recv_time,String fileN, StringBuffer loggerBuffer, String filepath) {
		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,"
				+ "V01301,V01300,V04001,V04002,V04003,V07001,V02301,V_ACODE,V14032,Q14032,V_BBB,D_SOURCE_ID) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, ?, ?) ";
		if(connection != null){		
			try {	
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");		
				for(int i = 0; i <surfSunDatas.size(); i ++){
					pStatement = new LoggableStatement(connection, sql);
					connection.setAutoCommit(false);
					SurfWeaChnSsdHor surfSunData = surfSunDatas.get(i);
					StatDi di = new StatDi();	
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(day_sod_code);
					di.setDATA_TYPE_1(cts_code);
					di.setTT("质控后地面日照资料");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1");  //1成功，0失败	
					String format = new SimpleDateFormat("yyyyMMdd").format(surfSunData.getObservationTime());
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(surfSunData.getObservationTime());
					calendar.set(Calendar.HOUR_OF_DAY, 0);
					calendar.set(Calendar.MINUTE, 0);
					calendar.set(Calendar.SECOND, 0);
					calendar.set(Calendar.MILLISECOND, 0);
					
					String stationNumberChina = surfSunData.getStationNumberChina().toUpperCase();
//					int num = stationNumberChina.substring(0, 1).toUpperCase().hashCode();
					int ii = 1;
					pStatement.setString(ii++, format + "000000" +"_"+surfSunData.getStationNumberChina());//记录标识
					pStatement.setString(ii++, day_sod_code);//资料标识
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//入库时间
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));//收到时间
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间
					pStatement.setTimestamp(ii++, new Timestamp(new Date(calendar.getTime().getTime()).getTime()));//资料时间
					pStatement.setString(ii++, stationNumberChina);//区站号(字符)
					pStatement.setInt(ii++,Integer.parseInt(StationCodeUtil.stringToAscii(stationNumberChina)) );//区站号(数字)
					pStatement.setInt(ii++, Integer.parseInt(format.substring(0,4)));//V04001
					pStatement.setInt(ii++, Integer.parseInt(format.substring(4,6)));//V04002
					pStatement.setInt(ii++, Integer.parseInt(format.substring(6,8)));//V04003
					String info = (String) proMap.get(stationNumberChina + "+01");
					String V07001="999999";
					String V02301="999999";
					String V_ACODE="999999";
					if(info == null||"".equals(info)) {
						pStatement.setBigDecimal(ii++, new BigDecimal("999999"));
						pStatement.setBigDecimal(ii++, new BigDecimal("999999"));
						pStatement.setBigDecimal(ii++, new BigDecimal("999999"));
					}else{
						String[] infos = info.split(",");
						 V07001=("null".equals(infos[3])||"".equals(infos[3])) ? "999999" : infos[3];
						 V02301=("null".equals(infos[6])||"".equals(infos[6])) ? "999999" : infos[6];
						 V_ACODE=("null".equals(infos[5])||"".equals(infos[5])) ? "999999" : infos[5];
						
						pStatement.setBigDecimal(ii++,  new BigDecimal(V07001));//V07001
						pStatement.setBigDecimal(ii++,  new BigDecimal(V02301));//V02301
						pStatement.setBigDecimal(ii++,  new BigDecimal(V_ACODE));//V_ACODE
					}
					 pStatement.setBigDecimal(ii++,  new BigDecimal(String.valueOf(surfSunData.getTotalSunshinDay().getValue())));//V14032日照时数 
					 pStatement.setBigDecimal(ii++,  new BigDecimal(surfSunData.getTotalSunshinDay().getQuality().get(0).getCode()));//Q14032
					 pStatement.setString(ii++, surfSunData.getCorrectionIndicator());//文件更正标识
					 pStatement.setString(ii++, cts_code);
					
					di.setIIiii(surfSunData.getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(calendar.getTime(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(surfSunData.getLatitude()));
					di.setLONGTITUDE(String.valueOf(surfSunData.getLongitude()));
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
					di.setDATA_UPDATE_FLAG(surfSunData.getCorrectionIndicator());
					di.setHEIGHT(String.valueOf(V07001));
					listDi.add(di);
					try{
						pStatement.execute();
						connection.commit();
						loggerBuffer.append("\n 成功插入表"+StartConfig.valueTable()+"一条数据 ！: "+fileN+" :"+((LoggableStatement)pStatement).getQueryString());
					}catch(SQLException e){
						if (e.getErrorCode() == 13001 || e.getErrorCode() == 1062) {//主键冲突，则更新入库
							String[] Existresult=findExistData(surfSunData,StartConfig.valueTable(),connection);
							String d_datetime=Existresult[0];
							String v_bbb=Existresult[1];//库里原有更正标识
							String V_BBB=surfSunData.getCorrectionIndicator();//当前报文的更正标识
							if (d_datetime!=null) {//库里有记录
//								if(V_BBB.equals("000")&&(v_bbb==null||(v_bbb!=null&&v_bbb.equals("000"))||(v_bbb!=null&&v_bbb.startsWith("RR")))){
//									updateDayData(surfSunData,connection,fileN,recv_time,loggerBuffer,di);
//								}
//								if(V_BBB.startsWith("RR")&&(v_bbb==null||(v_bbb!=null&&v_bbb.equals("000"))||(v_bbb!=null&&v_bbb.startsWith("RR")))){
//									updateDayData(surfSunData,connection,fileN,recv_time,loggerBuffer,di);
//								}
//								if(V_BBB.startsWith("C")&& (v_bbb==null||(v_bbb!=null&&(V_BBB.compareTo(v_bbb) > 0)) ||v_bbb.startsWith("RR"))){
//									updateDayData(surfSunData,connection,fileN,recv_time,loggerBuffer,di);
//								}
								updateDayData(surfSunData,connection,fileN,recv_time,loggerBuffer,di);
							}
						}else{
							loggerBuffer.append("\n "+StartConfig.valueTable()+"表插入一条数据失败 ！: "+fileN+" :"+e.getMessage());
							di.setPROCESS_STATE("0");
						}
					}finally {
						if(pStatement != null) {
							try {
								pStatement.close();
							} catch (SQLException e) {
								loggerBuffer.append("\n insertDayDB Close Statement failed: "+e.getMessage());
							}
						}
					}
				} // end for
			}catch (SQLException e) {
				loggerBuffer.append("\n insertDayDB Create Statement failed: "+e.getMessage());
			}finally {
				if(pStatement != null) {
					try {
						pStatement.close();
					} catch (SQLException e) {
						loggerBuffer.append("\n insertDayDB Close Statement failed: "+e.getMessage());
					}
				}
			}
		} 	
	}
	
	private static void  updateDayData(SurfWeaChnSsdHor surfSunData,Connection connection,String fileN,Date recv_time,StringBuffer loggerBuffer,StatDi di){
		Map<String, Object> proMap = StationInfo.getProMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		PreparedStatement pStat = null;
		try{
//			String updateSql="update "+StartConfig.keyTable()+" set  D_RYMDHM=?, D_UPDATE_TIME=?, V01301=?, V01300=?, V05001=?, V06001=?, V04001=?, V04002=?, V04003=?, V07001=?, V02301=?, V_ACODE=?, V14332=?, V14032=?, V14032_001=?, V14032_002=?, V14032_003=?, V14032_004=?, V14032_005=?, V14032_006=?, V14032_007=?, V14032_008=?, V14032_009=?, V14032_010=?, V14032_011=?, V14032_012=?, V14032_013=?, V14032_014=?, V14032_015=?, V14032_016=?, V14032_017=?, V14032_018=?, V14032_019=?, V14032_020=?, V14032_021=?, V14032_022=?, V14032_023=?, V14032_024=?, Q14032_001=?, Q14032_002=?, Q14032_003=?, Q14032_004=?, Q14032_005=?, Q14032_006=?, Q14032_007=?, Q14032_008=?, Q14032_009=?, Q14032_010=?, Q14032_011=?, Q14032_012=?, Q14032_013=?, Q14032_014=?, Q14032_015=?, Q14032_016=?, Q14032_017=?, Q14032_018=?, Q14032_019=?, Q14032_020=?, Q14032_021=?, Q14032_022=?, Q14032_023=?, Q14032_024=?, Q14032=?, V_BBB=?  where D_RECORD_ID = ?  and d_datetime= ? ";
//			String updateSql="update "+StartConfig.keyTable()+" set  D_RYMDHM=?, D_UPDATE_TIME=?, V01301=?, V01300=?, V05001=?, V06001=?, V04001=?, V04002=?, V04003=?, V07001=?, V02301=?, V_ACODE=?, V14332=?, V14032=? ,V14032_001=?   where d_datetime= ? and D_RECORD_ID = ?  ";
//			String updateSql="update "+StartConfig.keyTable()+" set  V14032_002=?, V14032_003=?, V14032_004=?, V14032_004=?, V14032_005=?, V14032_006=?, V14032_007=?, V14032_008=?, V14032_009=?, V14032_010=?, V14032_011=?, V14032_012=?, V14032_013=?, V14032_014=?, V14032_015=?, V14032_016=?, V14032_017=?, V14032_018=?, V14032_019=?, V14032_020=?, V14032_021=?, V14032_022=?, V14032_023=?, V14032_024=?, Q14032_001=?, Q14032_002=?, Q14032_003=?, Q14032_004=?, Q14032_005=?, Q14032_006=?, Q14032_007=?, Q14032_008=?, Q14032_009=?, Q14032_010=?, Q14032_011=?, Q14032_012=?, Q14032_013=?, Q14032_014=?, Q14032_015=?, Q14032_016=?, Q14032_017=?, Q14032_018=?, Q14032_019=?, Q14032_020=?, Q14032_021=?, Q14032_022=?, Q14032_023=?, Q14032_024=?, Q14032=?, V_BBB=?  where d_datetime= ? and D_RECORD_ID = ?  ";
//			String updateSql="update "+StartConfig.keyTable()+" set  D_RYMDHM=?, D_UPDATE_TIME=?, V01301=?, V01300=?, V05001=?, V06001=?, V04001=?, V04002=?, V04003=?, V07001=?, V02301=?, V_ACODE=?, V14332=?, V14032=? ,V14032_001=?, V14032_002=?, V14032_003=?, V14032_004=?, V14032_004=?, V14032_005=?, V14032_006=?, V14032_007=?, V14032_008=?, V14032_009=?, V14032_010=?, V14032_011=?, V14032_012=?, V14032_013=?, V14032_014=?, V14032_015=?, V14032_016=?, V14032_017=?, V14032_018=?, V14032_019=?, V14032_020=?, V14032_021=?, V14032_022=?, V14032_023=?, V14032_024=?, Q14032_001=?, Q14032_002=?, Q14032_003=?, Q14032_004=?, Q14032_005=?, Q14032_006=?, Q14032_007=?, Q14032_008=?, Q14032_009=?, Q14032_010=?, Q14032_011=?, Q14032_012=?, Q14032_013=?, Q14032_014=?, Q14032_015=?, Q14032_016=?, Q14032_017=?, Q14032_018=?, Q14032_019=?, Q14032_020=?, Q14032_021=?, Q14032_022=?, Q14032_023=?, Q14032_024=?, Q14032=?, V_BBB=?   where d_datetime= ? and D_RECORD_ID = ?  ";
			String updateSql="update "+StartConfig.valueTable()+" set  D_UPDATE_TIME=?, V14032=?, Q14032=?   where d_datetime= ? and V01301 = ?  ";

			pStat = new LoggableStatement(connection, updateSql);
			
			String format = new SimpleDateFormat("yyyyMMdd").format(surfSunData.getObservationTime());
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(surfSunData.getObservationTime());
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			
//			String stationNumberChina = surfSunData.getStationNumberChina().toUpperCase();
			int ii = 1;
//			pStat.setTimestamp(ii++, new Timestamp(recv_time.getTime()));//收到时间
			pStat.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间
			pStat.setBigDecimal(ii++,  new BigDecimal(String.valueOf(surfSunData.getTotalSunshinDay().getValue())));//V14032 日照时数（直接辐射计算值）
			pStat.setBigDecimal(ii++,  new BigDecimal(surfSunData.getTotalSunshinDay().getQuality().get(0).getCode()));//Q14032
//			pStat.setString(ii++, surfSunData.getCorrectionIndicator());//文件更正标识
			pStat.setTimestamp(ii++, new Timestamp(new Date(calendar.getTime().getTime()).getTime()));//资料时间
//			pStat.setString(ii++, format + "000000" +"_"+surfSunData.getStationNumberChina());//记录标识
			pStat.setString(ii++, surfSunData.getStationNumberChina());//站号
			try {
				pStat.execute();
				connection.commit();
				loggerBuffer.append("\n"+ StartConfig.valueTable()+"成功更新一条数据！"+fileN+"ObservationTime:"+surfSunData.getObservationTime()+" StationNumber:"+surfSunData.getStationNumberChina());
			} catch (Exception e) {
				di.setPROCESS_STATE("0");// 1成功，0失败
				loggerBuffer.append("\n "+ StartConfig.valueTable()+"更新数据 失败！"+((LoggableStatement)pStat).getQueryString()+e.getMessage());
			}
		}catch (Exception e) {
			loggerBuffer.append("\n updateDayData create Statement failed: "+e.getMessage());
		}finally {
			if(pStat != null) {
				try {
					pStat.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n  updateDayData Close Statement failed: "+e.getMessage());
				}
			}
		}
	}
	
	
	public static void reportInfoToDb(@SuppressWarnings("rawtypes") List<ReportInfo> reportInfos, Connection connection, Date recv_time, String filepath, StringBuffer loggerBuffer ) {
		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement pStatement = null;
		File file=new File(filepath);
		String fileN=file.getName();
		String[] fileNameSplit = fileN.split("_");
		String V_TT = fileNameSplit[6].split("-")[0];
        String V_CCCC = fileNameSplit[3].substring(0, 4);
		String sql = "INSERT INTO "+StartConfig.reportTable()+" "
				+ " (D_RECORD_ID,D_DATA_ID,D_SOURCE_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V_BBB,V_CCCC,V_TT,V01301, "
				+ " V01300,V05001,V06001,V_NCODE,V_ACODE,V04001,V04002,V04003,V04004,V04005,V_LEN,V_REPORT) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";		
		try {
			pStatement = new LoggableStatement(connection, sql);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			for (int i = 0; i < reportInfos.size(); i++) {	
				try {
					StatDi di = new StatDi();	
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(rep_sod_code);
					di.setDATA_TYPE_1(cts_code);
					di.setTT(V_TT);			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1");  //1成功，0失败
					
					Map<String, Object> repmap = (Map<String, Object>) reportInfos.get(i).getT();//报文头
					String observationTime = (String) repmap.get("D_DATETIME");
					String observationTimeAfter=observationTime.substring(0,8)+"000000";
					String stationNumberChina = (String) repmap.get("V01301");
			        int num = stationNumberChina.substring(0, 1).hashCode();
			        String D_RECORD_ID = observationTimeAfter + "_" + stationNumberChina + "_" +cts_code+  "_" + V_TT;
//					String primkey = sdf.format(agmeReportHeader.getReport_time())+"_"+agmeReportHeader.getStationNumberChina()+"_"+v_cccc;
					int ii=1;
			        pStatement.setString(1, D_RECORD_ID);
					pStatement.setString(ii++, rep_sod_code);
					pStatement.setString(ii++, cts_code);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new SimpleDateFormat("yyyyMMddHHmmss").parse(observationTimeAfter).getTime()));
					pStatement.setString(ii++, repmap.get("V_BBB").toString());
					pStatement.setString(ii++, V_CCCC);
					pStatement.setString(ii++, V_TT);
					pStatement.setString(ii++, repmap.get("V01301").toString());
					if (num >= 48 & num <= 57) {
						pStatement.setString(ii++,  stationNumberChina);
		            } else {
		            	pStatement.setString(ii++,  String.valueOf(num) + stationNumberChina.substring(1));
		            }
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(repmap.get("V5001"))));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(repmap.get("V6001"))));
					pStatement.setInt(ii++, 1);//V_NCODE
					pStatement.setBigDecimal(ii++, new BigDecimal(StationInfo.getAdminCode(stationNumberChina, "01")));// V_ACODE
					pStatement.setInt(ii++, Integer.parseInt(observationTimeAfter.substring(0, 4)));
					pStatement.setInt(ii++, Integer.parseInt(observationTimeAfter.substring(4, 6)));
					pStatement.setInt(ii++, Integer.parseInt(observationTimeAfter.substring(6, 8)));
					pStatement.setInt(ii++, Integer.parseInt(observationTimeAfter.substring(8, 10)));
					pStatement.setInt(ii++, Integer.parseInt(observationTimeAfter.substring(10,12)));
					pStatement.setInt(ii++, reportInfos.get(i).getReport().length());
					pStatement.setString(ii++, reportInfos.get(i).getReport());
					
					Date obstime=new SimpleDateFormat("yyyyMMddHHmmss").parse(observationTimeAfter);
					String obstime1=new SimpleDateFormat("yyyy-MM-dd HH:mm").format(obstime);
					String HeightOfSation="999999";
					String info = (String) proMap.get(stationNumberChina + "+01");
					if(info != null && !"".equals(info)) {
						String[] infos = info.split(",");
						HeightOfSation=("null".equals(infos[3])||"".equals(infos[3])) ? "999999" : infos[3];
					}
					
					di.setIIiii(stationNumberChina);
					di.setDATA_TIME(obstime1);
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLONGTITUDE(String.valueOf(repmap.get("V5001")));
					di.setLATITUDE(String.valueOf(repmap.get("V6001")));
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
					di.setDATA_UPDATE_FLAG(repmap.get("V_BBB").toString());
					di.setHEIGHT(String.valueOf(HeightOfSation));
					try {
						pStatement.execute();
						connection.commit();
						loggerBuffer.append("\n 成功插入报告表一条数据！"+fileN);
						listDi.add(di);
					} catch (SQLException e) {
						loggerBuffer.append("\n reportInfoToDb sql error:"+e.getMessage()+fileN);
						di.setPROCESS_STATE("0");//1成功，0失败
						listDi.add(di);
						continue;
					}
				} catch (Exception e) {
					loggerBuffer.append("\n reportInfoToDb  error:"+e.getMessage()+fileN);
					continue;
				} 
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n reportInfoToDb Database connection error:" + e.getMessage());
			return;
		}
		finally {
			if(pStatement != null){
				try {
					pStatement.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n close statement: " + e.getMessage());
				}
			}
		}
	}
	static	String[]  findExistData(SurfWeaChnSsdHor surfSunData,String tableName,Connection connection){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(surfSunData.getObservationTime());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		PreparedStatement Pstmt = null;
		ResultSet resultSet  = null;
		String v_bbb = null;
		String d_datetime=null;
		String rntString [] = {d_datetime, v_bbb};
		String sql = "select D_DATETIME,V_BBB from "+tableName+" "
				+ "where  D_DATETIME = ? and V01301 = ? ";
		try{
			if(connection != null){	
				Pstmt = connection.prepareStatement(sql);
				int ii = 1;
				String format = new SimpleDateFormat("yyyyMMdd").format(surfSunData.getObservationTime());
				Pstmt.setTimestamp(ii++, new Timestamp(new Date(calendar.getTime().getTime()).getTime()));//资料时间
//				Pstmt.setString(ii++, format + "000000" +"_"+surfSunData.getStationNumberChina());//记录标识
				Pstmt.setString(ii++, surfSunData.getStationNumberChina());//站号
				resultSet = Pstmt.executeQuery();
				if(resultSet.next()){
					d_datetime = resultSet.getString(1);
					v_bbb = resultSet.getString(2);
				}
			}
		}catch(SQLException e){
			infoLogger.error("\n findExistData create Statement error " + e.getMessage());
		}
		finally {
			if(Pstmt != null) {
				try {
					Pstmt.close();
				} catch (SQLException e) {
					infoLogger.error("\n findExistData close Statement error " + e.getMessage());
				}
			}
			if(resultSet != null){
				try{
					resultSet.close();
				}catch(SQLException e){
					infoLogger.error("\n findExistData close resultSet error " + e.getMessage());
				}
			}
		}
		rntString[0] = d_datetime;
		rntString[1] = v_bbb;
		return rntString;
	}
	
	public static void main(String[] args) {
//		File file = new File("D:\\TEMP\\G.3.1.1\\Z_CAWN_I_57518_20190314013028_O_AR_FTM.txt");
//		String fileN = file.getName();
//		Date recv_time = new Date();
//		StringBuffer loggerBuffer = new StringBuffer();
//		DecodeDAY decodeSurfDay = new DecodeDAY();
//		ParseResult<SurfaceObservationDataDay> parseResult= (ParseResult<SurfaceObservationDataDay>) decodeSurfDay.decode(file, new HashSet<String>());
//		if(parseResult.isSuccess()){
//			DataBaseAction action = DbService.processSuccessReport(parseResult, recv_time, fileN, loggerBuffer,file.getPath());
//		}
	}
}
