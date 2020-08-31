package cma.cimiss2.dpc.indb.agme.dc_agme_manl_disa.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Pattern;

import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.config.CTSCodeMap;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeDisa;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeDisa01;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeDisa02;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeDisa03;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeDisa04;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeDisa05;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.agme.ReportInfoService;


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

	/** The Constant messageLogger. */
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	
	/** The Constant infoLogger. */
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	
	/** The default int. */
	public static int defaultInt = 999999;
	
	/** The pro map. */
//	static Map<String, Object> proMap = StationInfo.getProMap();
	
	/**
	 * Process success report.
	 *
	 * @param parseResult the parse result
	 * @param recv_time the recv time
	 * @param fileN the file N
	 * @param v_bbb the v bbb
	 * @param isRevised the is revised
	 * @param loggerBuffer the logger buffer
	 * @param ctsCodeMaps the cts code maps
	 * @return the data base action
	 * @Title: processSuccessReport
	 * @Description: TODO(灾害资料入库)
	 * @param: @param parseResult
	 * @param: @param filepath
	 * @return: void
	 * @throws: 
	 */
	public static DataBaseAction processSuccessReport(ParseResult<ZAgmeDisa> parseResult, Date recv_time, String filepath, String v_bbb, boolean isRevised, StringBuffer loggerBuffer, List<CTSCodeMap> ctsCodeMaps) {
		java.sql.Connection connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
		File file = new File(filepath);
		String fileN = file.getName();
		try {
			List<ZAgmeDisa> agmeDisas = parseResult.getData();
			for (String disaType : agmeDisas.get(0).disaTypes) {
				switch(disaType) {
				case "DISA-01":
					List<ZAgmeDisa01> agmeDisa01s = agmeDisas.get(0).zAgmeDisa01s;
					if(isRevised) {
						connection.setAutoCommit(true);
						EleUpdateDISA01(agmeDisa01s, connection,recv_time, v_bbb,loggerBuffer,filepath,ctsCodeMaps.get(0));
					} else {
						connection.setAutoCommit(false);
						insert_db_DISA01(agmeDisa01s,connection, recv_time,v_bbb,loggerBuffer,filepath,ctsCodeMaps.get(0));
					}
					break;
					
				case "DISA-02":
					List<ZAgmeDisa02> agmeDisa02s = agmeDisas.get(0).zAgmeDisa02s;
					if(isRevised) {
						connection.setAutoCommit(true);						
						EleUpdateDISA02(agmeDisa02s, connection,recv_time, v_bbb,loggerBuffer,filepath,ctsCodeMaps.get(1));
					} else {
						connection.setAutoCommit(false);
						insert_db_DISA02(agmeDisa02s,connection,recv_time, v_bbb,loggerBuffer,filepath,ctsCodeMaps.get(1));
					}
					break;
					
				case "DISA-03":
					List<ZAgmeDisa03> agmeDisa03s = agmeDisas.get(0).zAgmeDisa03s;
					
					if(isRevised) {
						connection.setAutoCommit(true);						
						EleUpdateDISA03(agmeDisa03s,connection,recv_time, v_bbb,loggerBuffer,filepath,ctsCodeMaps.get(2));
					}
					else {
						connection.setAutoCommit(false);						
						insert_db_DISA03(agmeDisa03s,connection,recv_time, v_bbb,loggerBuffer,filepath,ctsCodeMaps.get(2));
					}
					break;
				case "DISA-04":
					List<ZAgmeDisa04> agmeDisa04s = agmeDisas.get(0).zAgmeDisa04s;
					if(isRevised) {
						connection.setAutoCommit(true);						
						EleUpdateDISA04(agmeDisa04s, connection,recv_time, v_bbb,loggerBuffer,filepath,ctsCodeMaps.get(3));
					} else {
						connection.setAutoCommit(false);						
						insert_db_DISA04(agmeDisa04s,connection,recv_time, v_bbb,loggerBuffer,filepath,ctsCodeMaps.get(3));
					}
					break;
				case "DISA-05":
					List<ZAgmeDisa05> agmeDisa05s = agmeDisas.get(0).zAgmeDisa05s;
					if(isRevised) {
						connection.setAutoCommit(true);
						EleUpdateDISA05(agmeDisa05s, connection,recv_time, v_bbb,loggerBuffer,filepath,ctsCodeMaps.get(4));
					} else {
						connection.setAutoCommit(false);						
						insert_db_DISA05(agmeDisa05s,connection,recv_time, v_bbb,loggerBuffer,filepath,ctsCodeMaps.get(4));
					}
					break;
				default:
					break;
				}
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n Setting automatic  submit transaction failed" +e.getMessage());
		} finally {
			try {
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				loggerBuffer.append("\n Database connection close error："+e.getMessage());
			}
		}
		
		@SuppressWarnings("rawtypes")
		List<ReportInfo> reportInfos = parseResult.getReports();
		String[] fnames = fileN.split("_");
		java.sql.Connection report_connection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
		ReportInfoService.reportInfoToDb(reportInfos, report_connection,v_bbb, recv_time, fnames[3], fnames[1],ctsCodeMaps, filepath, listDi);
		
		for (int j = 0; j < listDi.size(); j++) {
			diQueues.offer(listDi.get(j));
		}
		listDi.clear();
		
		
		return DataBaseAction.SUCCESS;
	}
	
	/**
	 * Insert db DISA 05.
	 *
	 * @param list the list
	 * @param connection the connection
	 * @param recv_time the recv time
	 * @param v_bbb the v bbb
	 * @param loggerBuffer the logger buffer
	 * @param fileN the file N
	 * @param ctsCodeMap the cts code map
	 * @return DataBaseAction
	 * @Title: insert_db_DISA05
	 * @Description: TODO(disa05资料入库)
	 * @throws： 
	 */
	@SuppressWarnings("deprecation")
	private static DataBaseAction insert_db_DISA05(List<ZAgmeDisa05> list, java.sql.Connection connection, Date recv_time, String v_bbb, StringBuffer loggerBuffer, String filepath, CTSCodeMap ctsCodeMap) {
		// TODO Auto-generated method stub
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		int sz = listDi.size();
		try{
			// chy  去掉 D_RECORD_ID
			String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+" (D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,"
					+ "V01301,V01300,V05001,V06001,V07001,V07031,"
					+ "V_ACODE,V04001,V04002,V04003,V04004,"
					+ "V04300_017, V04300_018,"				
					+ "V71501,V71040,V71042,V71614,"
					+ "V_BBB, D_UPDATE_TIME, D_SOURCE_ID) "
					+ "VALUES ( ?, ?, ?, ?,"
					+ " ?, ?, ?, ?, ?,?, "
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?)";
			if(connection != null){
				pStatement = connection.prepareStatement(sql);	
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				for (int i = 0; i < list.size(); i++) {
					StatDi di = new StatDi();					
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMap.getSod_code());
					di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
					di.setTT("");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1");  //1成功，0失败	
					
					int stationNumberN = defaultInt;
					String stat = list.get(i).getStationNumberChina();
					if (Pattern.matches("\\d{5}", stat))
						stationNumberN = Integer.parseInt(stat);					
							
					String info = (String) proMap.get(stat + "+12");
					String adminCode = "999999";
					if(info != null) {
						String[] infos = info.split(",");
						if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
							adminCode = infos[5];
					}
					
					if(adminCode.startsWith("999999")){
						info = (String) proMap.get(stat + "+01");
						if(info == null) {
							loggerBuffer.append("\n In the configuration file, the station number does not exist" + stat);
						}else {
							String[] infos = info.split(",");
							if(infos.length >= 6)
								adminCode = infos[5];
						}
					}
					
					int ii = 1;
//					pStatement.setString(ii++, sdf.format(list.get(i).getObservationDate())+"_"+list.get(i).getStationNumberChina()+"_"+list.get(i).getDamagePlant()+"_"+list.get(i).getDisaName());
					pStatement.setString(ii++, ctsCodeMap.getSod_code());
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(list.get(i).getObservationDate().getTime()));
					pStatement.setString(ii++, list.get(i).getStationNumberChina());
					pStatement.setInt(ii++, stationNumberN);
					pStatement.setBigDecimal(ii++, new BigDecimal(list.get(i).getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(list.get(i).getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getHeightOfSationGroundAboveMeanSeaLevel())));              
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getHeightOfPressureSensor())));
					pStatement.setString(ii++, adminCode); // V_ACODE		
					pStatement.setInt(ii++, list.get(i).getObservationDate().getYear() + 1900);
					pStatement.setInt(ii++, list.get(i).getObservationDate().getMonth() + 1);
					pStatement.setInt(ii++, list.get(i).getObservationDate().getDate());
					pStatement.setInt(ii++, list.get(i).getObservationDate().getHours());
					pStatement.setBigDecimal(ii++, new BigDecimal(sdf.format(list.get(i).getStartTime())));
					pStatement.setBigDecimal(ii++, new BigDecimal(sdf.format(list.get(i).getEndTime())));			
					pStatement.setInt(ii++, list.get(i).getDamagePlant());
					pStatement.setInt(ii++, list.get(i).getDisaName());
					pStatement.setInt(ii++, list.get(i).getDegreeOfDamage());
					try {
						pStatement.setString(ii++, new String(list.get(i).getSymptomOfDisaster().getBytes(), "UTF8"));
					} catch (UnsupportedEncodingException e) {
						pStatement.setString(ii++, "999999");
						loggerBuffer.append("getSymptomOfDisaster() format error: " + e.getMessage());
					}
					pStatement.setString(ii++, v_bbb);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setString(ii++, ctsCodeMap.getCts_code());
					
					di.setIIiii(list.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(list.get(i).getObservationDate(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLATITUDE(String.valueOf(list.get(i).getLatitude()));
					di.setLONGTITUDE(String.valueOf(list.get(i).getLongitude()));
					di.setDATA_UPDATE_FLAG(v_bbb);
					di.setHEIGHT(list.get(i).getHeightOfSationGroundAboveMeanSeaLevel().toString());
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(file.length()));
					pStatement.addBatch();				
					listDi.add(di);
				} 
				try {
					pStatement.executeBatch();
					connection.commit();
					return DataBaseAction.SUCCESS;
				} catch (Exception e) {
					pStatement.clearParameters();
					pStatement.clearBatch();
					for(int i = 0; i < list.size(); i ++){
						listDi.remove(sz);
					}
					connection.setAutoCommit(true);
					for(int i = 0; i < list.size(); i ++){
						insertOneLine_db_DISA05(list.get(i), connection, recv_time,v_bbb,filepath,loggerBuffer,ctsCodeMap);
					}
					loggerBuffer.append("\n Batch commit failed："+fileN);
					return DataBaseAction.BATCH_ERROR;
				}
			}
			else {
				loggerBuffer.append("\n Database connection error");
				return DataBaseAction.CONNECTION_ERROR;
			}
		}catch(SQLException e){
			loggerBuffer.append("\n Database connection close error："+e.getMessage());
			return DataBaseAction.CONNECTION_ERROR;
		}finally {
			try {
				if(pStatement != null)
					pStatement.close();
			} catch (SQLException e) {
				loggerBuffer.append("\n Database connection close error："+e.getMessage());
			}			
		}
	}

/**
 * Insert db DISA 04.
 *
 * @param list the list
 * @param connection the connection
 * @param recv_time the recv time
 * @param v_bbb the v bbb
 * @param loggerBuffer the logger buffer
 * @param fileN the file N
 * @param ctsCodeMap the cts code map
 * @return DataBaseAction
 * @Title: insert_db_DISA04
 * @Description: TODO(disa04资料入库)
 * @throws： 
 */
	@SuppressWarnings("deprecation")
	private static DataBaseAction insert_db_DISA04(List<ZAgmeDisa04> list, java.sql.Connection connection, Date recv_time, String v_bbb, StringBuffer loggerBuffer, String filepath, CTSCodeMap ctsCodeMap) {
		// TODO Auto-generated method stub
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		int sz = listDi.size();
		try{
			// chy 去掉 D_RECORD_ID
			String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+"(D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,"
					+ "V01301,V01300,V05001,V06001,V07001,V07031,"
					+ "V_ACODE,V04001,V04002,V04003,"
					+ "V04300_017, V04300_018,"				
					+ "V71040,V71048,V71614,"
					+ "V_BBB, D_UPDATE_TIME, D_SOURCE_ID) "
					+ "VALUES (?, ?, ?, ?,"
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?)";
			if(connection != null){
				pStatement = connection.prepareStatement(sql);	
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				for (int i = 0; i < list.size(); i++) {
					StatDi di = new StatDi();					
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMap.getSod_code());
					di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
					di.setTT("");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1");  //1成功，0失败	
					
					int stationNumberN = defaultInt;
					String stat = list.get(i).getStationNumberChina();
					if (Pattern.matches("\\d{5}", stat))
						stationNumberN = Integer.parseInt(stat);					
							
					String info = (String) proMap.get(stat + "+12");
					String adminCode = "999999";
					if(info != null) {
						String[] infos = info.split(",");
						if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
							adminCode = infos[5];
					}
					
					if(adminCode.startsWith("999999")){
						info = (String) proMap.get(stat + "+01");
						if(info == null) {
							loggerBuffer.append("\n In the configuration file, the station number does not exist" + stat);
						}else {
							String[] infos = info.split(",");
							if(infos.length >= 6)
								adminCode = infos[5];
						}
					}
							
					int ii = 1;
//					pStatement.setString(ii++, sdf.format(list.get(i).getObservationDate())+"_"+list.get(i).getStationNumberChina()+"_"+list.get(i).getDisaName());
					pStatement.setString(ii++, ctsCodeMap.getSod_code());
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(list.get(i).getObservationDate().getTime()));
					pStatement.setString(ii++, list.get(i).getStationNumberChina());
					pStatement.setInt(ii++, stationNumberN);
					pStatement.setBigDecimal(ii++, new BigDecimal(list.get(i).getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(list.get(i).getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getHeightOfSationGroundAboveMeanSeaLevel())));              
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getHeightOfPressureSensor())));
					pStatement.setString(ii++, adminCode); // V_ACODE		
					pStatement.setInt(ii++, list.get(i).getObservationDate().getYear() + 1900);
					pStatement.setInt(ii++, list.get(i).getObservationDate().getMonth() + 1);
					pStatement.setInt(ii++, list.get(i).getObservationDate().getDate());
					pStatement.setBigDecimal(ii++, new BigDecimal(sdf.format(list.get(i).getStartTime())));
					pStatement.setBigDecimal(ii++, new BigDecimal(sdf.format(list.get(i).getEndTime())));			
					pStatement.setInt(ii++, list.get(i).getDisaName());
					pStatement.setInt(ii++, list.get(i).getDisaLevel());
					try {
						pStatement.setString(ii++, new String(list.get(i).getSymptomOfDisaster().getBytes(), "UTF8"));
					} catch (UnsupportedEncodingException e) {
						pStatement.setString(ii++, "999999");
						loggerBuffer.append("getSymptomOfDisaster() format error: " + e.getMessage());
					}
					pStatement.setString(ii++, v_bbb);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setString(ii++, ctsCodeMap.getCts_code());
					
					di.setIIiii(list.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(list.get(i).getObservationDate(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLATITUDE(String.valueOf(list.get(i).getLatitude()));
					di.setLONGTITUDE(String.valueOf(list.get(i).getLongitude()));
					di.setDATA_UPDATE_FLAG(v_bbb);
					di.setHEIGHT(list.get(i).getHeightOfSationGroundAboveMeanSeaLevel().toString());
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(file.length()));
					pStatement.addBatch();				
					listDi.add(di);
				} 
				try {
					pStatement.executeBatch();
					connection.commit();
					return DataBaseAction.SUCCESS;
				} catch (Exception e) {
					pStatement.clearParameters();
					pStatement.clearBatch();
					for(int i = 0; i < list.size(); i ++){
						listDi.remove(sz);
					}
					connection.setAutoCommit(true);
					for(int i = 0; i < list.size(); i ++){
						insertOneLine_db_DISA04(list.get(i), connection, recv_time,v_bbb,filepath,loggerBuffer,ctsCodeMap);
					}
					loggerBuffer.append("\n Batch commit failed："+fileN);
					return DataBaseAction.BATCH_ERROR;
				}
			}
			else {
				loggerBuffer.append("\n Database connection error");
				return DataBaseAction.CONNECTION_ERROR;
			}
		}catch(SQLException e){
			loggerBuffer.append("\n Database connection close error："+e.getMessage());
			return DataBaseAction.CONNECTION_ERROR;
		}finally {
			try {
				if(pStatement != null)
					pStatement.close();
			} catch (SQLException e) {
				loggerBuffer.append("\n  Database connection close error："+e.getMessage());
			}			
		}
	}

/**
 * Insert db DISA 03.
 *
 * @param list the list
 * @param connection the connection
 * @param recv_time the recv time
 * @param v_bbb the v bbb
 * @param loggerBuffer the logger buffer
 * @param fileN the file N
 * @param ctsCodeMap the cts code map
 * @return DataBaseAction
 * @Title: insert_db_DISA03
 * @Description: TODO(disa03资料入库)
 * @throws： 
 */
	@SuppressWarnings("deprecation")
	private static DataBaseAction insert_db_DISA03(List<ZAgmeDisa03> list, java.sql.Connection connection, Date recv_time, String v_bbb, StringBuffer loggerBuffer, String filepath, CTSCodeMap ctsCodeMap) {
		// TODO Auto-generated method stub
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		int sz = listDi.size();
		try{
			// chy 去掉 D_RECORD_ID
			String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+"(D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,"
					+ "V01301,V01300,V05001,V06001,V07001,V07031,"
					+ "V_ACODE,V04001,V04002,V04003,"
					+ "V04300_017, V04300_018,"				
					+ "V71040,V71048,V71614,"
					+ "V_BBB, D_UPDATE_TIME, D_SOURCE_ID) "
					+ "VALUES ( ?, ?, ?, ?,"
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?)";
			if(connection != null){
				pStatement = connection.prepareStatement(sql);	
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				for (int i = 0; i < list.size(); i++) {
					StatDi di = new StatDi();					
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMap.getSod_code());
					di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
					di.setTT("");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1");  //1成功，0失败	
					
					int stationNumberN = defaultInt;
					String stat = list.get(i).getStationNumberChina();
					if (Pattern.matches("\\d{5}", stat))
						stationNumberN = Integer.parseInt(stat);					
							
					String info = (String) proMap.get(stat + "+12");
					String adminCode = "999999";
					if(info != null) {
						String[] infos = info.split(",");
						if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
							adminCode = infos[5];
					}
					
					if(adminCode.startsWith("999999")){
						info = (String) proMap.get(stat + "+01");
						if(info == null) {
							loggerBuffer.append("\n In the configuration file, the station number does not exist" + stat);
						}else {
							String[] infos = info.split(",");
							if(infos.length >= 6)
								adminCode = infos[5];
						}
					}
					
					int ii = 1;
//					pStatement.setString(ii++, sdf.format(list.get(i).getObservationDate())+"_"+list.get(i).getStationNumberChina()+"_"+list.get(i).getDisaName());
					pStatement.setString(ii++, ctsCodeMap.getSod_code());
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(list.get(i).getObservationDate().getTime()));
					pStatement.setString(ii++, list.get(i).getStationNumberChina());
					pStatement.setInt(ii++, stationNumberN);
					pStatement.setBigDecimal(ii++, new BigDecimal(list.get(i).getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(list.get(i).getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getHeightOfSationGroundAboveMeanSeaLevel())));              
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getHeightOfPressureSensor())));
					pStatement.setString(ii++, adminCode); // V_ACODE		
					pStatement.setInt(ii++, list.get(i).getObservationDate().getYear() + 1900);
					pStatement.setInt(ii++, list.get(i).getObservationDate().getMonth() + 1);
					pStatement.setInt(ii++, list.get(i).getObservationDate().getDate());
					pStatement.setBigDecimal(ii++, new BigDecimal(sdf.format(list.get(i).getStartTime())));
					pStatement.setBigDecimal(ii++, new BigDecimal(sdf.format(list.get(i).getEndTime())));			
					pStatement.setInt(ii++, list.get(i).getDisaName());
					pStatement.setInt(ii++, list.get(i).getDisaLevel());
					try {
						pStatement.setString(ii++, new String(list.get(i).getSymptomOfDisaster().getBytes(), "UTF8"));
					} catch (UnsupportedEncodingException e) {
						pStatement.setString(ii++, "999999");
						loggerBuffer.append("getSymptomOfDisaster() format error: " + e.getMessage());
					}
					pStatement.setString(ii++, v_bbb);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setString(ii++, ctsCodeMap.getCts_code());
					
					di.setLATITUDE(String.valueOf(list.get(i).getLatitude()));
					di.setLONGTITUDE(String.valueOf(list.get(i).getLongitude()));
					di.setIIiii(list.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(list.get(i).getObservationDate(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setDATA_UPDATE_FLAG(v_bbb);
					di.setHEIGHT(list.get(i).getHeightOfSationGroundAboveMeanSeaLevel().toString());
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(file.length()));
						
					pStatement.addBatch();				
					listDi.add(di);
				} 
				try {
					pStatement.executeBatch();
					connection.commit();
					return DataBaseAction.SUCCESS;
				} catch (Exception e) {
					pStatement.clearParameters();
					pStatement.clearBatch();
					for(int i = 0; i < list.size(); i ++){
						listDi.remove(sz);
					}
					connection.setAutoCommit(true);
					for(int i = 0; i < list.size(); i ++){
						insertOneLine_db_DISA03(list.get(i), connection, recv_time,v_bbb,filepath,loggerBuffer,ctsCodeMap);
					}
					loggerBuffer.append("\n Batch commit failed："+fileN);
					return DataBaseAction.BATCH_ERROR;
				}
			}
			else {
				loggerBuffer.append("\n Database connection error");
				return DataBaseAction.CONNECTION_ERROR;
			}
		}catch(SQLException e){
			loggerBuffer.append("\n Database connection close error："+e.getMessage());
			return DataBaseAction.CONNECTION_ERROR;
		}finally {
			try {
				if(pStatement != null)
					pStatement.close();
			} catch (SQLException e) {
				loggerBuffer.append("\n Database connection close error："+e.getMessage());
			}			
		}
	}

/**
 * Insert db DISA 02.
 *
 * @param list the list
 * @param connection the connection
 * @param recv_time the recv time
 * @param v_bbb the v bbb
 * @param loggerBuffer the logger buffer
 * @param fileN the file N
 * @param ctsCodeMap the cts code map
 * @return DataBaseAction
 * @Title: insert_db_DISA02
 * @Description: TODO(disa02资料入库)
 * @throws： 
 */
	@SuppressWarnings("deprecation")
	private static DataBaseAction insert_db_DISA02(List<ZAgmeDisa02> list, java.sql.Connection connection, Date recv_time, String v_bbb, StringBuffer loggerBuffer, String filepath, CTSCodeMap ctsCodeMap) {
		// TODO Auto-generated method stub
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		int sz = listDi.size();
		try{
			// chy 去掉 D_RECORD_ID
			String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+"(D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,"
					+ "V01301,V01300,V05001,V06001,V07001,V07031,"
					+ "V_ACODE,V04001,V04002,V04003,"
					+ "V71040,V71001,"
					+ "V71042,V71043,V71044,"
					+ "V71083, V71614, V_BBB, D_UPDATE_TIME, D_SOURCE_ID) "
					+ "VALUES (?, ?, ?, ?,"
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?)";
			if(connection != null){
				pStatement = connection.prepareStatement(sql);	
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				for (int i = 0; i < list.size(); i++) {
					StatDi di = new StatDi();					
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMap.getSod_code());
					di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
					di.setTT("");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1");  //1成功，0失败	
					
					int stationNumberN = defaultInt;
					String stat = list.get(i).getStationNumberChina();
					if (Pattern.matches("\\d{5}", stat))
						stationNumberN = Integer.parseInt(stat);					
							
					String info = (String) proMap.get(stat + "+12");
					String adminCode = "999999";
					if(info != null) {
						String[] infos = info.split(",");
						if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
							adminCode = infos[5];
					}
					
					if(adminCode.startsWith("999999")){
						info = (String) proMap.get(stat + "+01");
						if(info == null) {
							loggerBuffer.append("\n In the configuration file, the station number does not exist" + stat);
						}else {
							String[] infos = info.split(",");
							if(infos.length >= 6)
								adminCode = infos[5];
						}
					}
					
					int ii = 1;
//					pStatement.setString(ii++, sdf.format(list.get(i).getObservationDate())+"_"+list.get(i).getStationNumberChina()+"_"+list.get(i).getDisaName()+"_"+list.get(i).getDisaCrop()+"_"+list.get(i).getDegreeOfOrganDamage()+"_"+list.get(i).getDisaArea()+"_"+list.get(i).getDisaPercentage()+"_"+list.get(i).getReductionPercentage());
					pStatement.setString(ii++, ctsCodeMap.getSod_code());
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(list.get(i).getObservationDate().getTime()));
					pStatement.setString(ii++, list.get(i).getStationNumberChina());
					pStatement.setInt(ii++, stationNumberN);
					pStatement.setBigDecimal(ii++, new BigDecimal(list.get(i).getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(list.get(i).getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getHeightOfSationGroundAboveMeanSeaLevel())));              
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getHeightOfPressureSensor())));
					pStatement.setString(ii++, adminCode); // V_ACODE		
					pStatement.setInt(ii++, list.get(i).getObservationDate().getYear() + 1900);
					pStatement.setInt(ii++, list.get(i).getObservationDate().getMonth() + 1);
					pStatement.setInt(ii++, list.get(i).getObservationDate().getDate());
					pStatement.setInt(ii++, list.get(i).getDisaName());
					pStatement.setInt(ii++, list.get(i).getDisaCrop());
					pStatement.setInt(ii++, list.get(i).getDegreeOfOrganDamage());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getDisaArea())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getDisaPercentage())));
					pStatement.setInt(ii++, list.get(i).getReductionPercentage());
					try {
						pStatement.setString(ii++, new String(list.get(i).getSymptomOfDisaster().getBytes(), "UTF8"));
					} catch (UnsupportedEncodingException e) {
						pStatement.setString(ii++, "999999");
						loggerBuffer.append("getSymptomOfDisaster() format error: " + e.getMessage());
					}
					pStatement.setString(ii++, v_bbb);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setString(ii++, ctsCodeMap.getCts_code());
					
					di.setIIiii(list.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(list.get(i).getObservationDate(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(list.get(i).getLatitude()));
					di.setLONGTITUDE(String.valueOf(list.get(i).getLongitude()));
					di.setDATA_UPDATE_FLAG(v_bbb);
					di.setHEIGHT(list.get(i).getHeightOfSationGroundAboveMeanSeaLevel().toString());
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(file.length()));
					pStatement.addBatch();				
					listDi.add(di);
				} 
				try {
					pStatement.executeBatch();
					connection.commit();
					return DataBaseAction.SUCCESS;
				} catch (Exception e) {
					pStatement.clearParameters();
					pStatement.clearBatch();
					for(int i = 0; i < list.size(); i ++){
						listDi.remove(sz);
					}
					connection.setAutoCommit(true);
					for(int i = 0; i < list.size(); i ++){
						insertOneLine_db_DISA02(list.get(i), connection, recv_time,v_bbb,filepath,loggerBuffer,ctsCodeMap);
					}
					loggerBuffer.append("\n Batch commit failed："+fileN);
					return DataBaseAction.BATCH_ERROR;
				}
			}
			else {
				loggerBuffer.append("\n Database connection  error");
				return DataBaseAction.CONNECTION_ERROR;
			}
		}catch(SQLException e){
			loggerBuffer.append("\n Database connection close error："+e.getMessage());
			return DataBaseAction.CONNECTION_ERROR;
		}finally {
			try {
				if(pStatement != null)
					pStatement.close();
			} catch (SQLException e) {
				loggerBuffer.append("\n Database connection close error："+e.getMessage());
			}			
		}
	}

/**
 * Insert db DISA 01.
 *
 * @param list the list
 * @param connection the connection
 * @param recv_time the recv time
 * @param v_bbb the v bbb
 * @param loggerBuffer the logger buffer
 * @param fileN the file N
 * @param ctsCodeMap the cts code map
 * @return DataBaseAction
 * @Title: insert_db_DISA01
 * @Description: TODO(disa01资料入库)
 * @throws： 
 */
	@SuppressWarnings("deprecation")
	private static DataBaseAction insert_db_DISA01(List<ZAgmeDisa01> list, java.sql.Connection connection, Date recv_time, String v_bbb, StringBuffer loggerBuffer, String filepath, CTSCodeMap ctsCodeMap) {
		// TODO Auto-generated method stub
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		int sz = listDi.size();
		try{
			// chy 去掉 D_RECORD_ID
			String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+" ( D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,"
					+ "V01301,V01300,V05001,V06001,V07001,V07031,"
					+ "V_ACODE,V04001,V04002,V04003,"
					+ "V71040,V71001,"
					+ "V71042,V71082,V71923,"
					+ "V71614, V_BBB, D_UPDATE_TIME, D_SOURCE_ID) "
					+ "VALUES (?, ?, ?, ?, ?,"
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?)";
			if(connection != null){
				pStatement = connection.prepareStatement(sql);	
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				for (int i = 0; i < list.size(); i++) {
					StatDi di = new StatDi();					
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMap.getSod_code());
					di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
					di.setTT("");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1");  //1成功，0失败	
					
					int stationNumberN = defaultInt;
					String stat = list.get(i).getStationNumberChina();
					if (Pattern.matches("\\d{5}", stat))
						stationNumberN = Integer.parseInt(stat);					
							
					String info = (String) proMap.get(stat + "+12");
					String adminCode = "999999";
					if(info != null) {
						String[] infos = info.split(",");
						if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
							adminCode = infos[5];
					}
					
					if(adminCode.startsWith("999999")){
						info = (String) proMap.get(stat + "+01");
						if(info == null) {
							loggerBuffer.append("\n In the configuration file, the station number does not exist" + stat);
						}else {
							String[] infos = info.split(",");
							if(infos.length >= 6)
								adminCode = infos[5];
						}
					}
							
					int ii = 1;
					pStatement.setString(ii++, sdf.format(list.get(i).getObservationDate())+"_"+list.get(i).getStationNumberChina()+"_"+list.get(i).getDisaName()+"_"+list.get(i).getDisaCrop()+"_"+list.get(i).getDegreeOfOrganDamage());
					pStatement.setString(ii++, ctsCodeMap.getSod_code());
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(list.get(i).getObservationDate().getTime()));
					pStatement.setString(ii++, list.get(i).getStationNumberChina());
					pStatement.setInt(ii++, stationNumberN);
					pStatement.setBigDecimal(ii++, new BigDecimal(list.get(i).getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(list.get(i).getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getHeightOfSationGroundAboveMeanSeaLevel())));              
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getHeightOfPressureSensor())));
					pStatement.setString(ii++, adminCode); // V_ACODE		
					pStatement.setInt(ii++, list.get(i).getObservationDate().getYear() + 1900);
					pStatement.setInt(ii++, list.get(i).getObservationDate().getMonth() + 1);
					pStatement.setInt(ii++, list.get(i).getObservationDate().getDate());
					pStatement.setInt(ii++, list.get(i).getDisaName());
					pStatement.setInt(ii++, list.get(i).getDisaCrop());
					pStatement.setInt(ii++, list.get(i).getDegreeOfOrganDamage());
					pStatement.setInt(ii++, list.get(i).getExpectedImpactOnOutput());
					pStatement.setInt(ii++, list.get(i).getReductionInOutput());
					try {
						pStatement.setString(ii++, new String(list.get(i).getSymptomOfDisaster().getBytes(), "UTF8"));
					} catch (UnsupportedEncodingException e) {
						pStatement.setString(ii++, "999999");
						loggerBuffer.append("getSymptomOfDisaster() format error: " + e.getMessage());
					}
					pStatement.setString(ii++, v_bbb);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setString(ii++, ctsCodeMap.getCts_code());
					
					di.setIIiii(list.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(list.get(i).getObservationDate(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLATITUDE(String.valueOf(list.get(i).getLatitude()));
					di.setLONGTITUDE(String.valueOf(list.get(i).getLongitude()));
					di.setDATA_UPDATE_FLAG(v_bbb);
					di.setHEIGHT(list.get(i).getHeightOfSationGroundAboveMeanSeaLevel().toString());
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(file.length()));
					pStatement.addBatch();				
					listDi.add(di);
				} 
				try {
					pStatement.executeBatch();
					connection.commit();
					return DataBaseAction.SUCCESS;
				} catch (Exception e) {
					pStatement.clearParameters();
					pStatement.clearBatch();
					for(int i = 0; i < list.size(); i ++){
						listDi.remove(sz);
					}
					connection.setAutoCommit(true);
					for(int i = 0; i < list.size(); i ++){
						insertOneLine_db_DISA01(list.get(i), connection, recv_time,v_bbb,filepath,loggerBuffer,ctsCodeMap);
					}
					loggerBuffer.append("\n Batch commit failed："+fileN);
					return DataBaseAction.BATCH_ERROR;
				}
			}
			else {
				loggerBuffer.append("\n Database connection  error");
				return DataBaseAction.CONNECTION_ERROR;
			}
		}catch(SQLException e){
			loggerBuffer.append("\n Database connection close error："+e.getMessage());
			return DataBaseAction.CONNECTION_ERROR;
		}finally {
			try {
				if(pStatement != null)
					pStatement.close();
			} catch (SQLException e) {
				loggerBuffer.append("\n Database connection close error："+e.getMessage());
			}			
		}
	}

/**
 * Ele update DISA 05.
 *
 * @param di05 the di 05
 * @param connection the connection
 * @param recv_time void
 * @param v_bbb the v bbb
 * @param loggerBuffer the logger buffer
 * @param fileN the file N
 * @param ctsCodeMap the cts code map
 * @Title: EleUpdateDISA05
 * @Description: TODO(disa05资料更正报入库)
 * @throws： 
 */
	@SuppressWarnings("deprecation")
	private static void EleUpdateDISA05(List<ZAgmeDisa05> di05, java.sql.Connection connection, Date recv_time, String v_bbb, StringBuffer loggerBuffer, String filepath, CTSCodeMap ctsCodeMap) {
		// TODO Auto-generated method stub
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement Pstmt = null;
		int i = 0;	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		for(i = 0; i < di05.size(); i ++){
			String vbbbInDB = null;
			vbbbInDB = findVBBBDisa05(di05.get(i), connection,loggerBuffer,ctsCodeMap.getValue_table_name()); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if(vbbbInDB == null){ // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				// insert
				insertOneLine_db_DISA05(di05.get(i), connection, recv_time,v_bbb,filepath,loggerBuffer,ctsCodeMap);
			}
			else if(vbbbInDB.compareTo(v_bbb) < 0){ // 数据库中有该条记录，且V_BBB的值早于当前处理文件的更正标识的值
				// update
				StatDi di = new StatDi();				
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(ctsCodeMap.getSod_code());
				di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
				di.setTT("");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //1成功，0失败
				di.setPROCESS_STATE("1");  //1成功，0失败	
				String sql01 =  "update "+ctsCodeMap.getValue_table_name()+" set "
						+ "V_BBB=?, D_UPDATE_TIME=?,V04300_017=?,V04300_018=?,V71042=?,V71614=? "
						+ "where V01301 = ? and V04001 = ?"
						+ " and V04002 = ? and V04003 = ? and V04004 = ?  and V71501=? and V71040 = ? ";
				if(connection != null){
					try{
						Pstmt = new LoggableStatement(connection, sql01);
						if(StartConfig.getDatabaseType() == 1) {
							Pstmt.execute("select last_txc_xid()");
						}
						int ii = 1;
						Pstmt.setString(ii++, v_bbb);
						Pstmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						Pstmt.setBigDecimal(ii++, new BigDecimal(sdf.format(di05.get(i).getStartTime())));
						Pstmt.setBigDecimal(ii++, new BigDecimal(sdf.format(di05.get(i).getEndTime())));
						Pstmt.setInt(ii++, di05.get(i).getDegreeOfDamage());
						try {
							Pstmt.setString(ii++, new String(di05.get(i).getSymptomOfDisaster().getBytes(), "UTF8"));
						} catch (UnsupportedEncodingException e1) {
							Pstmt.setString(ii++, "999999");
							loggerBuffer.append("getSymptomOfDisaster() format error: " + e1.getMessage());
						}
						
						Pstmt.setString(ii++, di05.get(i).getStationNumberChina());
						Pstmt.setInt(ii++, di05.get(i).getObservationDate().getYear() + 1900);
						Pstmt.setInt(ii++, di05.get(i).getObservationDate().getMonth() + 1);
						Pstmt.setInt(ii++, di05.get(i).getObservationDate().getDate());
						Pstmt.setInt(ii++, di05.get(i).getObservationDate().getHours());
						Pstmt.setInt(ii++, di05.get(i).getDamagePlant());
						Pstmt.setInt(ii++, di05.get(i).getDisaName());
						
						di.setIIiii(di05.get(i).getStationNumberChina());
						di.setDATA_TIME(TimeUtil.date2String(di05.get(i).getObservationDate(), "yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setLATITUDE(String.valueOf(di05.get(i).getLatitude()));
						di.setLONGTITUDE(String.valueOf(di05.get(i).getLongitude()));
						di.setDATA_UPDATE_FLAG(v_bbb);
						di.setHEIGHT(di05.get(i).getHeightOfSationGroundAboveMeanSeaLevel().toString());
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(file.length()));
						try{
							Pstmt.execute();
//							connection.commit();
							listDi.add(di);	
						}
						catch(SQLException e){							
							di.setPROCESS_STATE("0");//1成功，0失败
							listDi.add(di);		
							loggerBuffer.append("\n filename："+fileN+
									"\n " + di05.get(i).getStationNumberChina() + " " + sdf.format(di05.get(i).getObservationDate())
									+"\n execute sql error："+((LoggableStatement)Pstmt).getQueryString()+"\n "+e.getMessage());
						}				
					}	catch (SQLException e) {
					loggerBuffer.append("\n create Statement error: "+ e.getMessage());
					}finally {
						try {
							if(Pstmt != null)
								Pstmt.close();
						} catch (SQLException e) {
							loggerBuffer.append("\n close Statement error: "+ e.getMessage());
						}	
					}
				}else
					loggerBuffer.append("\n Database connection error");		
			} 	
			else{ // 数据库中有该条记录，且V_BBB的值晚于当前处理文件的更正标识的值
			// 不进行插入或更新	
			}
		} 		
	}

/**
 * Ele update DISA 04.
 *
 * @param di04 the di 04
 * @param connection the connection
 * @param recv_time void
 * @param v_bbb the v bbb
 * @param loggerBuffer the logger buffer
 * @param fileN the file N
 * @param ctsCodeMap the cts code map
 * @Title: EleUpdateDISA04
 * @Description: TODO(disa04资料更正报入库)
 * @throws： 
 */
	@SuppressWarnings("deprecation")
	private static void EleUpdateDISA04(List<ZAgmeDisa04> di04, java.sql.Connection connection, Date recv_time, String v_bbb, StringBuffer loggerBuffer, String filepath, CTSCodeMap ctsCodeMap) {
		// TODO Auto-generated method stub
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement Pstmt = null;
		int i = 0;	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		for(i = 0; i < di04.size(); i ++){
			String vbbbInDB = null;
			vbbbInDB = findVBBBDisa04(di04.get(i), connection,loggerBuffer,ctsCodeMap.getValue_table_name()); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if(vbbbInDB == null){ // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				// insert
				insertOneLine_db_DISA04(di04.get(i), connection, recv_time,v_bbb,filepath,loggerBuffer,ctsCodeMap);
			}
			else if(vbbbInDB.compareTo(v_bbb) < 0){ // 数据库中有该条记录，且V_BBB的值早于当前处理文件的更正标识的值
				// update
				StatDi di = new StatDi();				
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(ctsCodeMap.getSod_code());
				di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
				di.setTT("");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //1成功，0失败
				di.setPROCESS_STATE("1");  //1成功，0失败	
				String sql01 =  "update "+ctsCodeMap.getValue_table_name()+" set "
						+ "V_BBB=?, D_UPDATE_TIME=?,V04300_017=?,V04300_018=?,V71048=?,V71614=? "
						+ "where V01301 = ? and V04001 = ?"
						+ " and V04002 = ? and V04003 = ? and V71040 = ? ";
				if(connection != null){
					try{
						
						Pstmt = new LoggableStatement(connection, sql01);
						if(StartConfig.getDatabaseType() == 1) {
							Pstmt.execute("select last_txc_xid()");
						}
						int ii = 1;
						Pstmt.setString(ii++, v_bbb);
						Pstmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						Pstmt.setBigDecimal(ii++, new BigDecimal(sdf.format(di04.get(i).getStartTime())));
						Pstmt.setBigDecimal(ii++, new BigDecimal(sdf.format(di04.get(i).getEndTime())));
						Pstmt.setInt(ii++, di04.get(i).getDisaLevel());
						try {
							Pstmt.setString(ii++, new String(di04.get(i).getSymptomOfDisaster().getBytes(), "UTF8"));
						} catch (UnsupportedEncodingException e1) {
							Pstmt.setString(ii++, "999999");
							loggerBuffer.append("getSymptomOfDisaster() format error: " + e1.getMessage());
						}
						
						Pstmt.setString(ii++, di04.get(i).getStationNumberChina());
						Pstmt.setInt(ii++, di04.get(i).getObservationDate().getYear() + 1900);
						Pstmt.setInt(ii++, di04.get(i).getObservationDate().getMonth() + 1);
						Pstmt.setInt(ii++, di04.get(i).getObservationDate().getDate());
						Pstmt.setInt(ii++, di04.get(i).getDisaName());
						
						di.setIIiii(di04.get(i).getStationNumberChina());
						di.setDATA_TIME(TimeUtil.date2String(di04.get(i).getObservationDate(), "yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setLATITUDE(String.valueOf(di04.get(i).getLatitude()));
						di.setLONGTITUDE(String.valueOf(di04.get(i).getLongitude()));
						di.setDATA_UPDATE_FLAG(v_bbb);
						di.setHEIGHT(di04.get(i).getHeightOfSationGroundAboveMeanSeaLevel().toString());
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(file.length()));
						try{
							Pstmt.execute();	
//							connection.commit();
							listDi.add(di);	
						}
						catch(SQLException e){							
							di.setPROCESS_STATE("0");//1成功，0失败
							listDi.add(di);		
							loggerBuffer.append("\n filename："+fileN+
									"\n " + di04.get(i).getStationNumberChina() + " " + sdf.format(di04.get(i).getObservationDate())
									+"\n execute sql error："+((LoggableStatement)Pstmt).getQueryString()+"\n "+e.getMessage());
						}				
					}	catch (SQLException e) {
					loggerBuffer.append("\n create Statement error: "+ e.getMessage());
					}finally {
						try {
							if(Pstmt != null)
								Pstmt.close();
						} catch (SQLException e) {
							loggerBuffer.append("\n close Statement error: "+ e.getMessage());
						}	
					}
				}else
					loggerBuffer.append("\n  Database connection error");		
			} 
			else{ // 数据库中有该条记录，且V_BBB的值晚于当前处理文件的更正标识的值
			// 不进行插入或更新	
			}
		}
	}

/**
 * Ele update DISA 03.
 *
 * @param di03 the di 03
 * @param connection the connection
 * @param recv_time void
 * @param v_bbb the v bbb
 * @param loggerBuffer the logger buffer
 * @param fileN the file N
 * @param ctsCodeMap the cts code map
 * @Title: EleUpdateDISA03
 * @Description: TODO(disa03资料更正报入库)
 * @throws： 
 */
	@SuppressWarnings("deprecation")
	private static void EleUpdateDISA03(List<ZAgmeDisa03> di03, java.sql.Connection connection, Date recv_time, String v_bbb, StringBuffer loggerBuffer, String filepath, CTSCodeMap ctsCodeMap) {
		// TODO Auto-generated method stub
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement Pstmt = null;
		int i = 0;	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		for(i = 0; i < di03.size(); i ++){
			String vbbbInDB = null;
			vbbbInDB = findVBBBDisa03(di03.get(i), connection,loggerBuffer,ctsCodeMap.getValue_table_name()); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if(vbbbInDB == null){ // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				// insert
				insertOneLine_db_DISA03(di03.get(i), connection, recv_time,v_bbb,filepath,loggerBuffer,ctsCodeMap);
			}
			else if(vbbbInDB.compareTo(v_bbb) < 0){ // 数据库中有该条记录，且V_BBB的值早于当前处理文件的更正标识的值
				// update
				StatDi di = new StatDi();				
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(ctsCodeMap.getSod_code());
				di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
				di.setTT("");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //1成功，0失败
				di.setPROCESS_STATE("1");  //1成功，0失败	
				String sql01 =  "update "+ctsCodeMap.getValue_table_name()+" set "
						+ "V_BBB=?, D_UPDATE_TIME=?,V04300_017=?,V04300_018=?,V71048=?,V71614=? "
						+ "where V01301 = ? and V04001 = ?"
						+ " and V04002 = ? and V04003 = ? and V71040 = ? ";
				if(connection != null){
					try{
						
						Pstmt = new LoggableStatement(connection, sql01);
						if(StartConfig.getDatabaseType() == 1) {
							Pstmt.execute("select last_txc_xid()");
						}
						int ii = 1;
						Pstmt.setString(ii++, v_bbb);
						Pstmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						Pstmt.setBigDecimal(ii++, new BigDecimal(sdf.format(di03.get(i).getStartTime())));
						Pstmt.setBigDecimal(ii++, new BigDecimal(sdf.format(di03.get(i).getEndTime())));
						Pstmt.setInt(ii++, di03.get(i).getDisaLevel());
						try {
							Pstmt.setString(ii++, new String(di03.get(i).getSymptomOfDisaster().getBytes(), "UTF8"));
						} catch (UnsupportedEncodingException e1) {
							Pstmt.setString(ii++, "999999");
							loggerBuffer.append("getSymptomOfDisaster() format error: " + e1.getMessage());
						}
						
						Pstmt.setString(ii++, di03.get(i).getStationNumberChina());
						Pstmt.setInt(ii++, di03.get(i).getObservationDate().getYear() + 1900);
						Pstmt.setInt(ii++, di03.get(i).getObservationDate().getMonth() + 1);
						Pstmt.setInt(ii++, di03.get(i).getObservationDate().getDate());
						Pstmt.setInt(ii++, di03.get(i).getDisaName());
						
						di.setIIiii(di03.get(i).getStationNumberChina());
						di.setDATA_TIME(TimeUtil.date2String(di03.get(i).getObservationDate(), "yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setLATITUDE(String.valueOf(di03.get(i).getLatitude()));
						di.setLONGTITUDE(String.valueOf(di03.get(i).getLongitude()));
						di.setDATA_UPDATE_FLAG(v_bbb);
						di.setHEIGHT(di03.get(i).getHeightOfSationGroundAboveMeanSeaLevel().toString());
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(file.length()));
						try{
							Pstmt.execute();	
//							connection.commit();
							listDi.add(di);	
						}
						catch(SQLException e){							
							di.setPROCESS_STATE("1");//0成功，1失败
							listDi.add(di);		
							loggerBuffer.append("\n filename："+fileN+
									"\n " + di03.get(i).getStationNumberChina() + " " + sdf.format(di03.get(i).getObservationDate())
									+"\n execute sql error："+((LoggableStatement)Pstmt).getQueryString()+"\n "+e.getMessage());
						}				
					}	catch (SQLException e) {
					loggerBuffer.append("\n create Statement error: "+ e.getMessage());
					}finally {
						try {
							if(Pstmt != null)
								Pstmt.close();
						} catch (SQLException e) {
							loggerBuffer.append("\n close Statement  error: "+ e.getMessage());
						}	
					}
				}else
					loggerBuffer.append("\n Database connection error");		
			} 
			else{ // 数据库中有该条记录，且V_BBB的值晚于当前处理文件的更正标识的值
			// 不进行插入或更新	
			}
		} 
	}

/**
 * Ele update DISA 02.
 *
 * @param di02 the di 02
 * @param connection the connection
 * @param recv_time void
 * @param v_bbb the v bbb
 * @param loggerBuffer the logger buffer
 * @param fileN the file N
 * @param ctsCodeMap the cts code map
 * @Title: EleUpdateDISA02
 * @Description: TODO(disa02资料更正报入库)
 * @throws： 
 */
	@SuppressWarnings("deprecation")
	private static void EleUpdateDISA02(List<ZAgmeDisa02> di02, java.sql.Connection connection, Date recv_time, String v_bbb, StringBuffer loggerBuffer, String filepath, CTSCodeMap ctsCodeMap) {
		// TODO Auto-generated method stub
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement Pstmt = null;
		int i = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		for(i = 0; i < di02.size(); i ++){
			String vbbbInDB = null;
			vbbbInDB = findVBBBDisa02(di02.get(i), connection,loggerBuffer,ctsCodeMap.getValue_table_name()); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if(vbbbInDB == null){ // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				// insert
				insertOneLine_db_DISA02(di02.get(i), connection, recv_time,v_bbb,filepath,loggerBuffer,ctsCodeMap);
			}
			else if(vbbbInDB.compareTo(v_bbb) < 0){ // 数据库中有该条记录，且V_BBB的值早于当前处理文件的更正标识的值
				// update
				StatDi di = new StatDi();				
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(ctsCodeMap.getSod_code());
				di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
				di.setTT("");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //1成功，0失败
				di.setPROCESS_STATE("1");  //1成功，0失败	
				String sql01 =  "update "+ctsCodeMap.getValue_table_name()+" set "
						+ "V_BBB=?, D_UPDATE_TIME=?,V71614=? "
						+ "where V01301 = ? and V04001 = ?"
						+ " and V04002 = ? and V04003 = ? and V71040 = ? and V71001 = ? and V71042 = ? and V71043 = ? and V71044 = ? and V71083 = ?";
				if(connection != null){
					try{
						
						Pstmt = new LoggableStatement(connection, sql01);
						if(StartConfig.getDatabaseType() == 1) {
							Pstmt.execute("select last_txc_xid()");
						}
						int ii = 1;
						Pstmt.setString(ii++, v_bbb);
						Pstmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						try {
							Pstmt.setString(ii++, new String(di02.get(i).getSymptomOfDisaster().getBytes(), "UTF8"));
						} catch (UnsupportedEncodingException e1) {
							Pstmt.setString(ii++, "999999");
							loggerBuffer.append("getSymptomOfDisaster() format error: " + e1.getMessage());
						}
						
						Pstmt.setString(ii++, di02.get(i).getStationNumberChina());
						Pstmt.setInt(ii++, di02.get(i).getObservationDate().getYear() + 1900);
						Pstmt.setInt(ii++, di02.get(i).getObservationDate().getMonth() + 1);
						Pstmt.setInt(ii++, di02.get(i).getObservationDate().getDate());
						Pstmt.setInt(ii++, di02.get(i).getDisaName());
						Pstmt.setInt(ii++, di02.get(i).getDisaCrop());
						Pstmt.setInt(ii++, di02.get(i).getDegreeOfOrganDamage());
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(di02.get(i).getDisaArea())));
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(di02.get(i).getDisaPercentage())));
						Pstmt.setInt(ii++, di02.get(i).getReductionPercentage());
						
						di.setIIiii(di02.get(i).getStationNumberChina());
						di.setDATA_TIME(TimeUtil.date2String(di02.get(i).getObservationDate(), "yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setLATITUDE(String.valueOf(di02.get(i).getLatitude()));
						di.setLONGTITUDE(String.valueOf(di02.get(i).getLongitude()));
						di.setDATA_UPDATE_FLAG(v_bbb);
						di.setHEIGHT(di02.get(i).getHeightOfSationGroundAboveMeanSeaLevel().toString());
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(file.length()));
						try{
							Pstmt.execute();	
//							connection.commit();
							listDi.add(di);	
						}
						catch(SQLException e){							
							di.setPROCESS_STATE("0");//1成功，0失败
							listDi.add(di);		
							loggerBuffer.append("\n filename："+fileN+
									"\n " + di02.get(i).getStationNumberChina() + " " + sdf.format(di02.get(i).getObservationDate())
									+"\n execute sql error："+((LoggableStatement)Pstmt).getQueryString()+"\n "+e.getMessage());
						}				
					}	catch (SQLException e) {
					loggerBuffer.append("\n create Statement error: "+ e.getMessage());
					}finally {
						try {
							if(Pstmt != null)
								Pstmt.close();
						} catch (SQLException e) {
							loggerBuffer.append("\n close Statement error: "+ e.getMessage());
						}	
					}
				}else
					loggerBuffer.append("\n Database connection error");		
			} 
			else{ // 数据库中有该条记录，且V_BBB的值晚于当前处理文件的更正标识的值
			// 不进行插入或更新	
			}
		} 
	}

/**
 * Ele update DISA 01.
 *
 * @param di01 the di 01
 * @param connection the connection
 * @param recv_time void
 * @param v_bbb the v bbb
 * @param loggerBuffer the logger buffer
 * @param fileN the file N
 * @param ctsCodeMap the cts code map
 * @Title: EleUpdateDISA01
 * @Description: TODO(disa01资料更正报入库)
 * @throws： 
 */
	@SuppressWarnings("deprecation")
	private static void EleUpdateDISA01(List<ZAgmeDisa01> di01, java.sql.Connection connection, Date recv_time, String v_bbb, StringBuffer loggerBuffer, String filepath, CTSCodeMap ctsCodeMap) {
		// TODO Auto-generated method stub
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement Pstmt = null;
		int i = 0;	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		for(i = 0; i < di01.size(); i ++){
			String vbbbInDB = null;
			vbbbInDB = findVBBBDisa01(di01.get(i), connection,loggerBuffer,ctsCodeMap.getValue_table_name()); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if(vbbbInDB == null){ // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				// insert
				insertOneLine_db_DISA01(di01.get(i), connection, recv_time,v_bbb,filepath,loggerBuffer,ctsCodeMap);
			}
			else if(vbbbInDB.compareTo(v_bbb) < 0){ // 数据库中有该条记录，且V_BBB的值早于当前处理文件的更正标识的值
				// update
				StatDi di = new StatDi();				
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(ctsCodeMap.getSod_code());
				di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
				di.setTT("");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //1成功，0失败
				di.setPROCESS_STATE("1");  //1成功，0失败	
				String sql01 =  "update "+ctsCodeMap.getValue_table_name()+" set "
						+ "V_BBB=?, D_UPDATE_TIME=?,V71082=?,V71923=?,V71614=? "
						+ "where V01301 = ? and V04001 = ?"
						+ " and V04002 = ? and V04003 = ? and V71040 = ? and V71001 = ? and V71042=?";
				if(connection != null){
					try{
						
						Pstmt = new LoggableStatement(connection, sql01);
						if(StartConfig.getDatabaseType() == 1) {
							Pstmt.execute("select last_txc_xid()");
						}
						int ii = 1;
						Pstmt.setString(ii++, v_bbb);
						Pstmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						Pstmt.setInt(ii++, di01.get(i).getExpectedImpactOnOutput());
						Pstmt.setInt(ii++, di01.get(i).getReductionInOutput());
						try {
							Pstmt.setString(ii++, new String(di01.get(i).getSymptomOfDisaster().getBytes(), "UTF8"));
						} catch (UnsupportedEncodingException e1) {
							Pstmt.setString(ii++, "999999");
							loggerBuffer.append("getSymptomOfDisaster() format error: " + e1.getMessage());
						}
						
						Pstmt.setString(ii++, di01.get(i).getStationNumberChina());
						Pstmt.setInt(ii++, di01.get(i).getObservationDate().getYear() + 1900);
						Pstmt.setInt(ii++, di01.get(i).getObservationDate().getMonth() + 1);
						Pstmt.setInt(ii++, di01.get(i).getObservationDate().getDate());
						Pstmt.setInt(ii++, di01.get(i).getDisaName());
						Pstmt.setInt(ii++, di01.get(i).getDisaCrop());
						Pstmt.setInt(ii++, di01.get(i).getDegreeOfOrganDamage());
						
						di.setIIiii(di01.get(i).getStationNumberChina());
						di.setDATA_TIME(TimeUtil.date2String(di01.get(i).getObservationDate(), "yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setLATITUDE(String.valueOf(di01.get(i).getLatitude()));
						di.setLONGTITUDE(String.valueOf(di01.get(i).getLongitude()));
						di.setDATA_UPDATE_FLAG(v_bbb);
						di.setHEIGHT(di01.get(i).getHeightOfSationGroundAboveMeanSeaLevel().toString());
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(file.length()));
						try{
							Pstmt.execute();	
//							connection.commit();
							listDi.add(di);	
						}
						catch(SQLException e){							
							di.setPROCESS_STATE("1");//0成功，1失败
							listDi.add(di);		
							loggerBuffer.append("\n filename："+fileN+
									"\n " + di01.get(i).getStationNumberChina() + " " + sdf.format(di01.get(i).getObservationDate())
									+"\n execute sql error："+((LoggableStatement)Pstmt).getQueryString()+"\n "+e.getMessage());
						}				
					}	catch (SQLException e) {
					loggerBuffer.append("\n create Statement error: "+ e.getMessage());
					}finally {
						try {
							if(Pstmt != null)
								Pstmt.close();
						} catch (SQLException e) {
							loggerBuffer.append("\n close Statement error: "+ e.getMessage());
						}	
					}
				}else
					loggerBuffer.append("\n  Database connection error");		
			} 
			else{ // 数据库中有该条记录，且V_BBB的值晚于当前处理文件的更正标识的值
			// 不进行插入或更新	
			}
		}
				
	}	
	
	/**
	 * Insert one line db DISA 05.
	 *
	 * @param di05 the di 05
	 * @param connection the connection
	 * @param recv_time void
	 * @param v_bbb the v bbb
	 * @param fileN the file N
	 * @param loggerBuffer the logger buffer
	 * @param ctsCodeMap the cts code map
	 * @Title: insertOneLine_db_DISA05
	 * @Description: TODO(disa05单条插入)
	 * @throws： 
	 */
	@SuppressWarnings("deprecation")
	private static void insertOneLine_db_DISA05(ZAgmeDisa05 di05, java.sql.Connection connection, Date recv_time, String v_bbb, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		// TODO Auto-generated method stub
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		// chy 去掉 D_RECORD_ID
		String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+" ( D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,"
				+ "V01301,V01300,V05001,V06001,V07001,V07031,"
				+ "V_ACODE,V04001,V04002,V04003,V04004,"
				+ "V04300_017, V04300_018, V71501, V71040,V71042,V71614,"
				+ "V_BBB, D_UPDATE_TIME, D_SOURCE_ID) "
				+ "VALUES (?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?)";
		if(connection != null){		
			try {	
				pStatement = new LoggableStatement(connection, sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");		
				StatDi di = new StatDi();				
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(ctsCodeMap.getSod_code());
				di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
				di.setTT("");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //1成功，0失败
				di.setPROCESS_STATE("1");  //1成功，0失败	
					
				int stationNumberN = defaultInt;
				String stat = di05.getStationNumberChina();
				if (Pattern.matches("\\d{5}", stat))
					stationNumberN = Integer.parseInt(stat);					
					
				String info = (String) proMap.get(stat + "+12");
				String adminCode = "999999";
				if(info != null) {
					String[] infos = info.split(",");
					if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
						adminCode = infos[5];
				}
				
				if(adminCode.startsWith("999999")){
					info = (String) proMap.get(stat + "+01");
					if(info == null) {
						loggerBuffer.append("\n In the configuration file, the station number does not exist" + stat);
					}else {
						String[] infos = info.split(",");
						if(infos.length >= 6)
							adminCode = infos[5];
					}
				}
					
				int ii = 1;
				pStatement.setString(ii++, sdf.format(di05.getObservationDate())+"_"+di05.getStationNumberChina()+"_"+di05.getDamagePlant()+"_"+di05.getDisaName());
				pStatement.setString(ii++, ctsCodeMap.getSod_code());
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(di05.getObservationDate().getTime()));
				pStatement.setString(ii++, di05.getStationNumberChina());
				pStatement.setInt(ii++, stationNumberN);
				pStatement.setBigDecimal(ii++, new BigDecimal(di05.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
				pStatement.setBigDecimal(ii++, new BigDecimal(di05.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(di05.getHeightOfSationGroundAboveMeanSeaLevel())));              
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(di05.getHeightOfPressureSensor())));
				pStatement.setString(ii++, adminCode); // V_ACODE		
				pStatement.setInt(ii++, di05.getObservationDate().getYear() + 1900);
				pStatement.setInt(ii++, di05.getObservationDate().getMonth() + 1);
				pStatement.setInt(ii++, di05.getObservationDate().getDate());
				pStatement.setInt(ii++, di05.getObservationDate().getHours());
				pStatement.setBigDecimal(ii++, new BigDecimal(sdf.format(di05.getStartTime())));
				pStatement.setBigDecimal(ii++, new BigDecimal(sdf.format(di05.getEndTime())));			
				pStatement.setInt(ii++, di05.getDamagePlant());
				pStatement.setInt(ii++, di05.getDisaName());
				pStatement.setInt(ii++, di05.getDegreeOfDamage());
				try {
					pStatement.setString(ii++, new String(di05.getSymptomOfDisaster().getBytes(), "UTF8"));
				} catch (UnsupportedEncodingException e1) {
					pStatement.setString(ii++, "999999");
					loggerBuffer.append("getSymptomOfDisaster() format error: " + e1.getMessage());
				}
				pStatement.setString(ii++, v_bbb);
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setString(ii++, ctsCodeMap.getCts_code());
				
				di.setIIiii(di05.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(di05.getStartTime(), "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				di.setLATITUDE(String.valueOf(di05.getLatitude()));
				di.setLONGTITUDE(String.valueOf(di05.getLongitude()));
				di.setDATA_UPDATE_FLAG(v_bbb);
				di.setHEIGHT(di05.getHeightOfSationGroundAboveMeanSeaLevel().toString());
				di.setSEND_PHYS("DRDS");
				di.setFILE_SIZE(String.valueOf(file.length()));
				try{
					pStatement.execute();
//					connection.commit();
					listDi.add(di);	
				}
				catch(SQLException e){							
					di.setPROCESS_STATE("0");//1成功，0失败
					listDi.add(di);		
					loggerBuffer.append("\n filename："+fileN+
								"\n " + di05.getStationNumberChina() + " " + sdf.format(di05.getStartTime())
								+"\n execute sql error："+((LoggableStatement)pStatement).getQueryString()+"\n "+e.getMessage());
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
			}
		} 
	}
	
	/**
	 * Insert one line db DISA 04.
	 *
	 * @param di04 the di 04
	 * @param connection the connection
	 * @param recv_time void
	 * @param v_bbb the v bbb
	 * @param fileN the file N
	 * @param loggerBuffer the logger buffer
	 * @param ctsCodeMap the cts code map
	 * @Title: insertOneLine_db_DISA04
	 * @Description: TODO(disa04单条插入)
	 * @throws： 
	 */
	@SuppressWarnings("deprecation")
	private static void insertOneLine_db_DISA04(ZAgmeDisa04 di04, java.sql.Connection connection, Date recv_time, String v_bbb, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		// TODO Auto-generated method stub
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		// chy 去掉 D_RECORD_ID
		String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+"( D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,"
				+ "V01301,V01300,V05001,V06001,V07001,V07031,"
				+ "V_ACODE,V04001,V04002,V04003,"
				+ "V04300_017, V04300_018,"				
				+ "V71040,V71048,V71614,"
				+ "V_BBB, D_UPDATE_TIME, D_SOURCE_ID) "
				+ "VALUES (?,?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, "
				+ " ?, ?, ?)";
		if(connection != null){		
			try {	
				
				pStatement = new LoggableStatement(connection, sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");		
				StatDi di = new StatDi();				
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(ctsCodeMap.getSod_code());
				di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
				di.setTT("");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //1成功，0失败
				di.setPROCESS_STATE("1");  //1成功，0失败	
					
				int stationNumberN = defaultInt;
				String stat = di04.getStationNumberChina();
				if (Pattern.matches("\\d{5}", stat))
					stationNumberN = Integer.parseInt(stat);					
					
				String info = (String) proMap.get(stat + "+12");
				String adminCode = "999999";
				if(info != null) {
					String[] infos = info.split(",");
					if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
						adminCode = infos[5];
				}
				
				if(adminCode.startsWith("999999")){
					info = (String) proMap.get(stat + "+01");
					if(info == null) {
						loggerBuffer.append("\n In the configuration file, the station number does not exist" + stat);
					}else {
						String[] infos = info.split(",");
						if(infos.length >= 6)
							adminCode = infos[5];
					}
				}
					
				int ii = 1;
				pStatement.setString(ii++, sdf.format(di04.getObservationDate())+"_"+di04.getStationNumberChina()+"_"+di04.getDisaName());
				pStatement.setString(ii++, ctsCodeMap.getSod_code());
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(di04.getObservationDate().getTime()));
				pStatement.setString(ii++, di04.getStationNumberChina());
				pStatement.setInt(ii++, stationNumberN);
				pStatement.setBigDecimal(ii++, new BigDecimal(di04.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
				pStatement.setBigDecimal(ii++, new BigDecimal(di04.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(di04.getHeightOfSationGroundAboveMeanSeaLevel())));              
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(di04.getHeightOfPressureSensor())));
				pStatement.setString(ii++, adminCode); // V_ACODE		
				pStatement.setInt(ii++, di04.getObservationDate().getYear() + 1900);
				pStatement.setInt(ii++, di04.getObservationDate().getMonth() + 1);
				pStatement.setInt(ii++, di04.getObservationDate().getDate());
				pStatement.setBigDecimal(ii++, new BigDecimal(sdf.format(di04.getStartTime())));
				pStatement.setBigDecimal(ii++, new BigDecimal(sdf.format(di04.getEndTime())));			
				pStatement.setInt(ii++, di04.getDisaName());
				pStatement.setInt(ii++, di04.getDisaLevel());
				try {
					pStatement.setString(ii++, new String(di04.getSymptomOfDisaster().getBytes(), "UTF8"));
				} catch (UnsupportedEncodingException e1) {
					pStatement.setString(ii++, "999999");
					loggerBuffer.append("getSymptomOfDisaster() format error: " + e1.getMessage());
				}
				pStatement.setString(ii++, v_bbb);
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setString(ii++, ctsCodeMap.getCts_code());
				
				di.setIIiii(di04.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(di04.getStartTime(), "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				di.setLATITUDE(String.valueOf(di04.getLatitude()));
				di.setLONGTITUDE(String.valueOf(di04.getLongitude()));
				di.setDATA_UPDATE_FLAG(v_bbb);
				di.setHEIGHT(di04.getHeightOfSationGroundAboveMeanSeaLevel().toString());
				di.setSEND_PHYS("DRDS");
				di.setFILE_SIZE(String.valueOf(file.length()));
				try{
					pStatement.execute();
//					connection.commit();
					listDi.add(di);	
				}
				catch(SQLException e){							
					di.setPROCESS_STATE("0");//1成功，0失败
					listDi.add(di);		
					loggerBuffer.append("\n filename："+fileN+
								"\n " + di04.getStationNumberChina() + " " + sdf.format(di04.getStartTime())
								+"\n execute sql error："+((LoggableStatement)pStatement).getQueryString()+"\n "+e.getMessage());
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
			}
		} 
	}
	
	/**
	 * Insert one line db DISA 03.
	 *
	 * @param di03 the di 03
	 * @param connection the connection
	 * @param recv_time void
	 * @param v_bbb the v bbb
	 * @param fileN the file N
	 * @param loggerBuffer the logger buffer
	 * @param ctsCodeMap the cts code map
	 * @Title: insertOneLine_db_DISA03
	 * @Description: TODO(disa03单条插入)
	 * @throws： 
	 */
	@SuppressWarnings("deprecation")
	private static void insertOneLine_db_DISA03(ZAgmeDisa03 di03, java.sql.Connection connection, Date recv_time, String v_bbb, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		// TODO Auto-generated method stub
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		// chy  去掉 D_RECORD_ID
		String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+"( D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,"
				+ "V01301,V01300,V05001,V06001,V07001,V07031,"
				+ "V_ACODE,V04001,V04002,V04003,"
				+ "V04300_017, V04300_018,"				
				+ "V71040,V71048,V71614,"
				+ "V_BBB, D_UPDATE_TIME, D_SOURCE_ID) "
				+ "VALUES (?,?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, "
				+ " ?, ?, ?)";
		if(connection != null){		
			try {	
				
				pStatement = new LoggableStatement(connection, sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");		
				StatDi di = new StatDi();				
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(ctsCodeMap.getSod_code());
				di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
				di.setTT("");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //1成功，0失败
				di.setPROCESS_STATE("1");  //1成功，0失败	
					
				int stationNumberN = defaultInt;
				String stat = di03.getStationNumberChina();
				if (Pattern.matches("\\d{5}", stat))
					stationNumberN = Integer.parseInt(stat);					
					
				String info = (String) proMap.get(stat + "+12");
				String adminCode = "999999";
				if(info != null) {
					String[] infos = info.split(",");
					if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
						adminCode = infos[5];
				}
				
				if(adminCode.startsWith("999999")){
					info = (String) proMap.get(stat + "+01");
					if(info == null) {
						loggerBuffer.append("\n In the configuration file, the station number does not exist" + stat);
					}else {
						String[] infos = info.split(",");
						if(infos.length >= 6)
							adminCode = infos[5];
					}
				}
					
				int ii = 1;
				pStatement.setString(ii++, sdf.format(di03.getObservationDate())+"_"+di03.getStationNumberChina()+"_"+di03.getDisaName());
				pStatement.setString(ii++, ctsCodeMap.getSod_code());
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(di03.getObservationDate().getTime()));
				pStatement.setString(ii++, di03.getStationNumberChina());
				pStatement.setInt(ii++, stationNumberN);
				pStatement.setBigDecimal(ii++, new BigDecimal(di03.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
				pStatement.setBigDecimal(ii++, new BigDecimal(di03.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(di03.getHeightOfSationGroundAboveMeanSeaLevel())));              
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(di03.getHeightOfPressureSensor())));
				pStatement.setString(ii++, adminCode); // V_ACODE		
				pStatement.setInt(ii++, di03.getObservationDate().getYear() + 1900);
				pStatement.setInt(ii++, di03.getObservationDate().getMonth() + 1);
				pStatement.setInt(ii++, di03.getObservationDate().getDate());
				pStatement.setBigDecimal(ii++, new BigDecimal(sdf.format(di03.getStartTime())));
				pStatement.setBigDecimal(ii++, new BigDecimal(sdf.format(di03.getEndTime())));			
				pStatement.setInt(ii++, di03.getDisaName());
				pStatement.setInt(ii++, di03.getDisaLevel());
				try {
					pStatement.setString(ii++, new String(di03.getSymptomOfDisaster().getBytes(), "UTF8"));
				} catch (UnsupportedEncodingException e1) {
					pStatement.setString(ii++, "999999");
					loggerBuffer.append("getSymptomOfDisaster() format error: " + e1.getMessage());
				}
				pStatement.setString(ii++, v_bbb);
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setString(ii++, ctsCodeMap.getCts_code());
				
				di.setIIiii(di03.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(di03.getStartTime(), "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				di.setLATITUDE(String.valueOf(di03.getLatitude()));
				di.setLONGTITUDE(String.valueOf(di03.getLongitude()));
				di.setDATA_UPDATE_FLAG(v_bbb);
				di.setHEIGHT(di03.getHeightOfSationGroundAboveMeanSeaLevel().toString());
				di.setSEND_PHYS("DRDS");
				di.setFILE_SIZE(String.valueOf(file.length()));
				try{
					pStatement.execute();
//					connection.commit();
					listDi.add(di);	
				}
				catch(SQLException e){							
					di.setPROCESS_STATE("0");//1成功，0失败
					listDi.add(di);		
					loggerBuffer.append("\n filename："+fileN+
								"\n " + di03.getStationNumberChina() + " " + sdf.format(di03.getStartTime())
								+"\n execute sql error："+((LoggableStatement)pStatement).getQueryString()+"\n "+e.getMessage());
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
			}
		} 
	}

/**
 * Insert one line db DISA 02.
 *
 * @param di02 the di 02
 * @param connection the connection
 * @param recv_time void
 * @param v_bbb the v bbb
 * @param fileN the file N
 * @param loggerBuffer the logger buffer
 * @param ctsCodeMap the cts code map
 * @Title: insertOneLine_db_DISA02
 * @Description: TODO(disa02单条插入)
 * @throws： 
 */
	@SuppressWarnings("deprecation")
	private static void insertOneLine_db_DISA02(ZAgmeDisa02 di02, java.sql.Connection connection, Date recv_time, String v_bbb, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		// TODO Auto-generated method stub
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		// chy  去掉 D_RECORD_ID
		String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+"( D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,"
				+ "V01301,V01300,V05001,V06001,V07001,V07031,"
				+ "V_ACODE,V04001,V04002,V04003,"
				+ "V71040,V71001,"
				+ "V71042,V71043,V71044,"
				+ "V71083, V71614, V_BBB, D_UPDATE_TIME, D_SOURCE_ID) "
				+ "VALUES (?,?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?)";
		if(connection != null){		
			try {	
				
				pStatement = new LoggableStatement(connection, sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");		
				StatDi di = new StatDi();				
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(ctsCodeMap.getSod_code());
				di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
				di.setTT("");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //1成功，0失败
				di.setPROCESS_STATE("1");  //1成功，0失败	
					
				int stationNumberN = defaultInt;
				String stat = di02.getStationNumberChina();
				if (Pattern.matches("\\d{5}", stat))
					stationNumberN = Integer.parseInt(stat);					
					
				String info = (String) proMap.get(stat + "+12");
				String adminCode = "999999";
				if(info != null) {
					String[] infos = info.split(",");
					if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
						adminCode = infos[5];
				}
				
				if(adminCode.startsWith("999999")){
					info = (String) proMap.get(stat + "+01");
					if(info == null) {
						loggerBuffer.append("\n In the configuration file, the station number does not exist" + stat);
					}else {
						String[] infos = info.split(",");
						if(infos.length >= 6)
							adminCode = infos[5];
					}
				}
				int ii = 1;
				pStatement.setString(ii++, sdf.format(di02.getObservationDate())+"_"+di02.getStationNumberChina()+"_"+di02.getDisaName()+"_"+di02.getDisaCrop()+"_"+di02.getDegreeOfOrganDamage()+"_"+di02.getDisaArea()+"_"+di02.getDisaPercentage()+"_"+di02.getReductionPercentage());
				pStatement.setString(ii++, ctsCodeMap.getSod_code());
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(di02.getObservationDate().getTime()));
				pStatement.setString(ii++, di02.getStationNumberChina());
				pStatement.setInt(ii++, stationNumberN);
				pStatement.setBigDecimal(ii++, new BigDecimal(di02.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
				pStatement.setBigDecimal(ii++, new BigDecimal(di02.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(di02.getHeightOfSationGroundAboveMeanSeaLevel())));              
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(di02.getHeightOfPressureSensor())));
				pStatement.setString(ii++, adminCode); // V_ACODE		
				pStatement.setInt(ii++, di02.getObservationDate().getYear() + 1900);
				pStatement.setInt(ii++, di02.getObservationDate().getMonth() + 1);
				pStatement.setInt(ii++, di02.getObservationDate().getDate());
				pStatement.setInt(ii++, di02.getDisaName());
				pStatement.setInt(ii++, di02.getDisaCrop());
				pStatement.setInt(ii++, di02.getDegreeOfOrganDamage());
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(di02.getDisaArea())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(di02.getDisaPercentage())));
				pStatement.setInt(ii++, di02.getReductionPercentage());
				try {
					pStatement.setString(ii++, new String(di02.getSymptomOfDisaster().getBytes(), "UTF8"));
				} catch (UnsupportedEncodingException e1) {
					pStatement.setString(ii++, "999999");
					loggerBuffer.append("getSymptomOfDisaster() format error: " + e1.getMessage());
				}
				pStatement.setString(ii++, v_bbb);
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setString(ii++, ctsCodeMap.getCts_code());
				
				di.setIIiii(di02.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(di02.getObservationDate(), "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				di.setLATITUDE(String.valueOf(di02.getLatitude()));
				di.setLONGTITUDE(String.valueOf(di02.getLongitude()));
				di.setDATA_UPDATE_FLAG(v_bbb);
				di.setHEIGHT(di02.getHeightOfSationGroundAboveMeanSeaLevel().toString());
				di.setSEND_PHYS("DRDS");
				di.setFILE_SIZE(String.valueOf(file.length()));
				try{
					pStatement.execute();
//					connection.commit();
					listDi.add(di);	
				}
				catch(SQLException e){							
					di.setPROCESS_STATE("0");//1成功，0失败
					listDi.add(di);		
					loggerBuffer.append("\n filename："+fileN+
								"\n " + di02.getStationNumberChina() + " " + sdf.format(di02.getObservationDate())
								+"\n execute sql error："+((LoggableStatement)pStatement).getQueryString()+"\n "+e.getMessage());
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
			}
		} 
	}
	
	/**
	 * Insert one line db DISA 01.
	 *
	 * @param di01 the di 01
	 * @param connection the connection
	 * @param recv_time void
	 * @param v_bbb the v bbb
	 * @param fileN the file N
	 * @param loggerBuffer the logger buffer
	 * @param ctsCodeMap the cts code map
	 * @Title: insertOneLine_db_DISA01
	 * @Description: TODO(disa01单条插入)
	 * @throws： 
	 */
	@SuppressWarnings("deprecation")
	private static void insertOneLine_db_DISA01(ZAgmeDisa01 di01, java.sql.Connection connection, Date recv_time, String v_bbb, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		// chy 去掉 D_RECORD_ID
		String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+"( D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,"
				+ "V01301,V01300,V05001,V06001,V07001,V07031,"
				+ "V_ACODE,V04001,V04002,V04003,"
				+ "V71040,V71001,"
				+ "V71042,V71082,V71923,"
				+ "V71614, V_BBB, D_UPDATE_TIME, D_SOURCE_ID) "
				+ "VALUES (?,?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?)";
		if(connection != null){		
			try {	
				pStatement = new LoggableStatement(connection, sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");		
				StatDi di = new StatDi();				
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(ctsCodeMap.getSod_code());
				di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
				di.setTT("");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //1成功，0失败
				di.setPROCESS_STATE("1");  //1成功，0失败	
					
				int stationNumberN = defaultInt;
				String stat = di01.getStationNumberChina();
				if (Pattern.matches("\\d{5}", stat))
					stationNumberN = Integer.parseInt(stat);					
					
				String info = (String) proMap.get(stat + "+12");
				String adminCode = "999999";
				if(info != null) {
					String[] infos = info.split(",");
					if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
						adminCode = infos[5];
				}
				
				if(adminCode.startsWith("999999")){
					info = (String) proMap.get(stat + "+01");
					if(info == null) {
						loggerBuffer.append("\n In the configuration file, the station number does not exist" + stat);
					}else {
						String[] infos = info.split(",");
						if(infos.length >= 6)
							adminCode = infos[5];
					}
				}
					
				int ii = 1;
				pStatement.setString(ii++, sdf.format(di01.getObservationDate())+"_"+di01.getStationNumberChina()+"_"+di01.getDisaName()+"_"+di01.getDisaCrop()+"_"+di01.getDegreeOfOrganDamage());
				pStatement.setString(ii++, ctsCodeMap.getSod_code());
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(di01.getObservationDate().getTime()));
				pStatement.setString(ii++, di01.getStationNumberChina());
				pStatement.setInt(ii++, stationNumberN);
				pStatement.setBigDecimal(ii++, new BigDecimal(di01.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
				pStatement.setBigDecimal(ii++, new BigDecimal(di01.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(di01.getHeightOfSationGroundAboveMeanSeaLevel())));              
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(di01.getHeightOfPressureSensor())));
				pStatement.setString(ii++, adminCode); // V_ACODE		
				pStatement.setInt(ii++, di01.getObservationDate().getYear() + 1900);
				pStatement.setInt(ii++, di01.getObservationDate().getMonth() + 1);
				pStatement.setInt(ii++, di01.getObservationDate().getDate());
				pStatement.setInt(ii++, di01.getDisaName());
				pStatement.setInt(ii++, di01.getDisaCrop());
				pStatement.setInt(ii++, di01.getDegreeOfOrganDamage());
				pStatement.setInt(ii++, di01.getExpectedImpactOnOutput());
				pStatement.setInt(ii++, di01.getReductionInOutput());
				try {
					pStatement.setString(ii++, new String(di01.getSymptomOfDisaster().getBytes(), "UTF8"));
				} catch (UnsupportedEncodingException e1) {
					pStatement.setString(ii++, "999999");
					loggerBuffer.append("getSymptomOfDisaster() format error: " + e1.getMessage());
				}
				pStatement.setString(ii++, v_bbb);
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setString(ii++, ctsCodeMap.getCts_code());
				
				di.setIIiii(di01.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(di01.getObservationDate(), "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				di.setLATITUDE(String.valueOf(di01.getLatitude()));
				di.setLONGTITUDE(String.valueOf(di01.getLongitude()));
				di.setDATA_UPDATE_FLAG(v_bbb);
				di.setHEIGHT(di01.getHeightOfSationGroundAboveMeanSeaLevel().toString());
				di.setSEND_PHYS("DRDS");
				di.setFILE_SIZE(String.valueOf(file.length()));
				try{
					pStatement.execute();
//					connection.commit();
					listDi.add(di);	
				}
				catch(SQLException e){							
					di.setPROCESS_STATE("0");//1成功，0失败
					listDi.add(di);		
					loggerBuffer.append("\n filename："+fileN+
								"\n " + di01.getStationNumberChina() + " " + sdf.format(di01.getObservationDate())
								+"\n execute sql error："+((LoggableStatement)pStatement).getQueryString()+"\n "+e.getMessage());
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
			}
		} 
	}

/**
 * Find VBBB disa 05.
 *
 * @param di05 the di 05
 * @param connection the connection
 * @param loggerBuffer the logger buffer
 * @param tablename the tablename
 * @return String
 * @Title: findVBBBDisa05
 * @Description: TODO(查找VBBB的值)
 * @throws： 
 */
	@SuppressWarnings("deprecation")
	private static String findVBBBDisa05(ZAgmeDisa05 di05, java.sql.Connection connection, StringBuffer loggerBuffer, String tablename) {
		// TODO Auto-generated method stub
		PreparedStatement Pstmt = null;
		ResultSet resultSet = null;
		String sql05 =  "select V_BBB from "+tablename+" "
				+ "where V01301 = ? and V04001 = ?"
				+ " and V04002 = ? and V04003 = ? and V71501 = ? and V71040 = ?";
		try{
			
			
			if(connection != null){
				Pstmt = connection.prepareStatement(sql05);
				if(StartConfig.getDatabaseType() == 1) {
					Pstmt.execute("select last_txc_xid()");
				}
				int ii = 1;									
				Pstmt.setString(ii++, di05.getStationNumberChina());
				Pstmt.setInt(ii++, di05.getObservationDate().getYear() + 1900);
				Pstmt.setInt(ii++, di05.getObservationDate().getMonth() + 1);
				Pstmt.setInt(ii++, di05.getObservationDate().getDate());
				Pstmt.setInt(ii++, di05.getDamagePlant());
				Pstmt.setInt(ii++, di05.getDisaName());
					
				resultSet = Pstmt.executeQuery();
				if(resultSet.next())
					return resultSet.getString(1);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			free(resultSet, Pstmt,loggerBuffer);
		}
		return null;
	}
	
	/**
	 * Find VBBB disa 04.
	 *
	 * @param di04 the di 04
	 * @param connection the connection
	 * @param loggerBuffer the logger buffer
	 * @param tablename the tablename
	 * @return String
	 * @Title: findVBBBDisa04
	 * @Description: TODO(查找VBBB的值)
	 * @throws： 
	 */
	@SuppressWarnings("deprecation")
	private static String findVBBBDisa04(ZAgmeDisa04 di04, java.sql.Connection connection, StringBuffer loggerBuffer, String tablename) {
		// TODO Auto-generated method stub
		PreparedStatement Pstmt = null;
		ResultSet resultSet = null;
		String sql04 =  "select V_BBB from "+tablename+" "
				+ "where V01301 = ? and V04001 = ?"
				+ " and V04002 = ? and V04003 = ? and V71040 = ?";
		try{
			
			
			if(connection != null){
				Pstmt = connection.prepareStatement(sql04);
				if(StartConfig.getDatabaseType() == 1) {
					Pstmt.execute("select last_txc_xid()");
				}
				int ii = 1;									
				Pstmt.setString(ii++, di04.getStationNumberChina());
				Pstmt.setInt(ii++, di04.getObservationDate().getYear() + 1900);
				Pstmt.setInt(ii++, di04.getObservationDate().getMonth() + 1);
				Pstmt.setInt(ii++, di04.getObservationDate().getDate());
				Pstmt.setInt(ii++, di04.getDisaName());
					
				resultSet = Pstmt.executeQuery();
				if(resultSet.next())
					return resultSet.getString(1);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			free(resultSet, Pstmt,loggerBuffer);
		}
		return null;
	}
	
	/**
	 * Find VBBB disa 03.
	 *
	 * @param di03 the di 03
	 * @param connection the connection
	 * @param loggerBuffer the logger buffer
	 * @param tablename the tablename
	 * @return String
	 * @Title: findVBBBDisa03
	 * @Description: TODO(查找VBBB的值)
	 * @throws： 
	 */
	@SuppressWarnings("deprecation")
	private static String findVBBBDisa03(ZAgmeDisa03 di03, java.sql.Connection connection, StringBuffer loggerBuffer, String tablename) {
		// TODO Auto-generated method stub
		PreparedStatement Pstmt = null;
		ResultSet resultSet = null;
		String sql03 =  "select V_BBB from "+tablename+" "
				+ "where V01301 = ? and V04001 = ?"
				+ " and V04002 = ? and V04003 = ? and V71040 = ?";
		try{
			
			
			if(connection != null){
				Pstmt = connection.prepareStatement(sql03);
				if(StartConfig.getDatabaseType() == 1) {
					Pstmt.execute("select last_txc_xid()");
				}
				int ii = 1;									
				Pstmt.setString(ii++, di03.getStationNumberChina());
				Pstmt.setInt(ii++, di03.getObservationDate().getYear() + 1900);
				Pstmt.setInt(ii++, di03.getObservationDate().getMonth() + 1);
				Pstmt.setInt(ii++, di03.getObservationDate().getDate());
				Pstmt.setInt(ii++, di03.getDisaName());
					
				resultSet = Pstmt.executeQuery();
				if(resultSet.next())
					return resultSet.getString(1);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			free(resultSet, Pstmt,loggerBuffer);
		}
		return null;
	}
	
	/**
	 * Find VBBB disa 02.
	 *
	 * @param di02 the di 02
	 * @param connection the connection
	 * @param loggerBuffer the logger buffer
	 * @param tablename the tablename
	 * @return String
	 * @Title: findVBBBDisa02
	 * @Description: TODO(查找VBBB的值)
	 * @throws： 
	 */
	@SuppressWarnings("deprecation")
	private static String findVBBBDisa02(ZAgmeDisa02 di02, java.sql.Connection connection, StringBuffer loggerBuffer, String tablename) {
		// TODO Auto-generated method stub
		PreparedStatement Pstmt = null;
		ResultSet resultSet = null;
		String sql02 =  "select V_BBB from "+tablename+" "
				+ "where V01301 = ? and V04001 = ?"
				+ " and V04002 = ? and V04003 = ? and V71040=? and V71001 = ? and V71042 = ?";
		try{
			
			
			if(connection != null){
				Pstmt = connection.prepareStatement(sql02);
				if(StartConfig.getDatabaseType() == 1) {
					Pstmt.execute("select last_txc_xid()");
				}
				int ii = 1;									
				Pstmt.setString(ii++, di02.getStationNumberChina());
				Pstmt.setInt(ii++, di02.getObservationDate().getYear() + 1900);
				Pstmt.setInt(ii++, di02.getObservationDate().getMonth() + 1);
				Pstmt.setInt(ii++, di02.getObservationDate().getDate());
				Pstmt.setInt(ii++, di02.getDisaName());
				Pstmt.setInt(ii++, di02.getDisaCrop());	
				Pstmt.setInt(ii++, di02.getDegreeOfOrganDamage());
				
				resultSet = Pstmt.executeQuery();
				if(resultSet.next())
					return resultSet.getString(1);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}	finally {
			free(resultSet, Pstmt,loggerBuffer);
		}
		return null;
	}
	
	/**
	 * Find VBBB disa 01.
	 *
	 * @param di01 the di 01
	 * @param connection the connection
	 * @param loggerBuffer the logger buffer
	 * @param tablename the tablename
	 * @return String
	 * @Title: findVBBBDisa01
	 * @Description: TODO(查找VBBB的值)
	 * @throws： 
	 */
	@SuppressWarnings("deprecation")
	private static String findVBBBDisa01(ZAgmeDisa01 di01, java.sql.Connection connection, StringBuffer loggerBuffer, String tablename) {
		// TODO Auto-generated method stub
		PreparedStatement Pstmt = null;
		ResultSet resultSet = null;
		String sql01 =  "select V_BBB from "+tablename+" "
				+ "where V01301 = ? and V04001 = ?"
				+ " and V04002 = ? and V04003 = ? and V71040=? and V71001 = ? and V71042 = ?";
		try{
			
			
			if(connection != null){
				Pstmt = connection.prepareStatement(sql01);
				if(StartConfig.getDatabaseType() == 1) {
					Pstmt.execute("select last_txc_xid()");
				}
				int ii = 1;									
				Pstmt.setString(ii++, di01.getStationNumberChina());
				Pstmt.setInt(ii++, di01.getObservationDate().getYear() + 1900);
				Pstmt.setInt(ii++, di01.getObservationDate().getMonth() + 1);
				Pstmt.setInt(ii++, di01.getObservationDate().getDate());
				Pstmt.setInt(ii++, di01.getDisaName());
				Pstmt.setInt(ii++, di01.getDisaCrop());	
				Pstmt.setInt(ii++, di01.getDegreeOfOrganDamage());
				
				resultSet = Pstmt.executeQuery();
				if(resultSet.next())
					return resultSet.getString(1);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			free(resultSet, Pstmt,loggerBuffer);
		}
		return null;
	}
	
	/**
	 * Free.
	 *
	 * @param resultSet the result set
	 * @param pstmt the pstmt
	 * @param loggerBuffer the logger buffer
	 */
	public static void free(ResultSet resultSet, PreparedStatement pstmt, StringBuffer loggerBuffer) {
		if (resultSet != null) {
			try {
				resultSet.close();
				resultSet = null;
			} catch (SQLException e) {
				loggerBuffer.append("\n close ResultSet error");
			}

		}
		if (pstmt != null) {
			try {
				pstmt.close();
				pstmt = null;
			} catch (SQLException e) {
				loggerBuffer.append("\n close PreparedStatement error");
			}
		}

	}
}
