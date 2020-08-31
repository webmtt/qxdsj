package cma.cimiss2.dpc.indb.agme.dc_agme_manl_crop.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
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
import org.cimiss2.dwp.tools.config.CTSCodeMap;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.agme.DecodeCrop;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCrop;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCrop01;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCrop02;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCrop03;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCrop04;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCrop05;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCrop06;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCrop07;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCrop08;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCrop09;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCrop10;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.agme.ReportInfoService;


public class DbService {
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}

	public static int defaultInt = 999999;
	public static int noTask = 999998;
//	static Map<String, Object> proMap = StationInfo.getProMap();
	
	/**
	 * @Title: processSuccessReport 
	 * @Description: TODO(处理解码结果集，正确解码的报文入库) 
	 * @param parseResult  解码结果集
	 * @param filepath  文件的绝对路径
	 * @param recv_time  消息接收时间
	 * @param isRevised 
	 * @param loggerBuffer 
	 * @return  返回值说明
	 * @throws:
	 */
	public static DataBaseAction processSuccessReport(ParseResult<ZAgmeCrop> parseResult, Date recv_time,String filepath,String v_bbb, boolean isRevised, StringBuffer loggerBuffer, List<CTSCodeMap> ctsCodeMaps) {
		File file = new File(filepath);
		String fileN = file.getName();
		java.sql.Connection connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
		if(connection != null) {
			try {
				List<ZAgmeCrop> agmeCrops = parseResult.getData();
				for (String cropType : agmeCrops.get(0).cropTypes) {
					switch (cropType) {
					case "CROP-01":
						List<ZAgmeCrop01> agmeCrop01s = agmeCrops.get(0).zAgmeCrop01s;
						if (isRevised)
							EleUpdateCROP01(agmeCrop01s, connection, recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMaps.get(0));
						else
							insert_db_CROP01(agmeCrop01s, connection, recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMaps.get(0));
						break;

					case "CROP-02":
						List<ZAgmeCrop02> agmeCrop02s = agmeCrops.get(0).zAgmeCrop02s;
						if (isRevised)
							EleUpdateCROP02(agmeCrop02s, connection, recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMaps.get(1));
						else
							insert_db_CROP02(agmeCrop02s, connection, recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMaps.get(1));
						break;

					case "CROP-03":
						List<ZAgmeCrop03> agmeCrop03s = agmeCrops.get(0).zAgmeCrop03s;

						if (isRevised)
							EleUpdateCROP03(agmeCrop03s, connection, recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMaps.get(2));
						else
							insert_db_CROP03(agmeCrop03s, connection, recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMaps.get(2));
						break;
					case "CROP-04":
						List<ZAgmeCrop04> agmeCrop04s = agmeCrops.get(0).zAgmeCrop04s;
						if (isRevised)
							EleUpdateCROP04(agmeCrop04s, connection, recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMaps.get(3));
						else
							insert_db_CROP04(agmeCrop04s, connection, recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMaps.get(3));
						break;
					case "CROP-05":
						List<ZAgmeCrop05> agmeCrop05s = agmeCrops.get(0).zAgmeCrop05s;
						if (isRevised)
							EleUpdateCROP05(agmeCrop05s, connection, recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMaps.get(4));
						else
							insert_db_CROP05(agmeCrop05s, connection, recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMaps.get(4));
						break;
					case "CROP-06":
						List<ZAgmeCrop06> agmeCrop06s = agmeCrops.get(0).zAgmeCrop06s;
						if (isRevised)
							EleUpdateCROP06(agmeCrop06s, connection, recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMaps.get(5));
						else
							insert_db_CROP06(agmeCrop06s, connection, recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMaps.get(5));
						break;
					case "CROP-07":
						List<ZAgmeCrop07> agmeCrop07s = agmeCrops.get(0).zAgmeCrop07s;
						if (isRevised)
							EleUpdateCROP07(agmeCrop07s, connection, recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMaps.get(6));
						else
							insert_db_CROP07(agmeCrop07s, connection, recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMaps.get(6));
						break;
					case "CROP-08":
						List<ZAgmeCrop08> agmeCrop08s = agmeCrops.get(0).zAgmeCrop08s;
						if (isRevised)
							EleUpdateCROP08(agmeCrop08s, connection, recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMaps.get(7));
						else
							insert_db_CROP08(agmeCrop08s, connection, recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMaps.get(7));
						break;
					case "CROP-09":
						List<ZAgmeCrop09> agmeCrop09s = agmeCrops.get(0).zAgmeCrop09s;
						if (isRevised)
							EleUpdateCROP09(agmeCrop09s, connection, recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMaps.get(8));
						else
							insert_db_CROP09(agmeCrop09s, connection, recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMaps.get(8));
						break;
					case "CROP-10":
						List<ZAgmeCrop10> agmeCrop10s = agmeCrops.get(0).zAgmeCrop10s;
						if (isRevised)
							EleUpdateCROP10(agmeCrop10s, connection, recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMaps.get(9));
						else
							insert_db_CROP10(agmeCrop10s, connection, recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMaps.get(9));
						break;
					default:
						break;
					}
				} 
			} finally {
				try {
					if(connection != null)
						connection.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n  database connection closes an exception.："+e.getMessage());
				}
			}
			@SuppressWarnings("rawtypes")
			List<ReportInfo> reportInfos = parseResult.getReports();
			String[] fnames = fileN.split("_");
			java.sql.Connection report_connection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
			ReportInfoService.reportInfoToDb(reportInfos, report_connection,v_bbb, recv_time, fnames[3], fnames[1], ctsCodeMaps,filepath,listDi);
			for (int j = 0; j < listDi.size(); j++) {
				diQueues.offer(listDi.get(j));
			}
			listDi.clear();
			return DataBaseAction.SUCCESS;
		}else {
			return DataBaseAction.CONNECTION_ERROR;
		}

		
	}

	/**
	 * 
	 * @Title: insert_db_CROP01   
	 * @Description: TODO(crop01解码结果入库)   
	 * @param list
	 * @param connection
	 * @param recv_time
	 * @param fileN 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @return DataBaseAction      
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static DataBaseAction insert_db_CROP01(List<ZAgmeCrop01> list, java.sql.Connection connection, Date recv_time, String v_bbb, String filepath, StringBuffer loggerBuffer,CTSCodeMap ctsCodeMap) {
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
					+ "V71001,V71002,"
					+ "V04001_03,V04002_03,V04003_03,"
					+ "V71005,V71010,V71007,V71006,V71008, V_BBB, D_UPDATE_TIME,D_SOURCE_ID) "
					+ "VALUES ( ?, ?, ?, ?,"
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, ?, ?, ?)";
			if(connection != null){
				connection.setAutoCommit(false);
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
//					pStatement.setString(ii++, sdf.format(list.get(i).getGrowthDate())+"_"+list.get(i).getStationNumberChina()+"_"+list.get(i).getCropName()+"_"+list.get(i).getPeriodOfGrowth());
					pStatement.setString(ii++, ctsCodeMap.getSod_code());
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(list.get(i).getGrowthDate().getTime()));
					pStatement.setString(ii++, list.get(i).getStationNumberChina());
					pStatement.setInt(ii++, stationNumberN);
					pStatement.setBigDecimal(ii++, new BigDecimal(list.get(i).getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(list.get(i).getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getHeightOfSationGroundAboveMeanSeaLevel())));              
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getHeightOfPressureSensor())));
					pStatement.setString(ii++, adminCode); // V_ACODE		
					pStatement.setInt(ii++, list.get(i).getGrowthDate().getYear() + 1900);
					pStatement.setInt(ii++, list.get(i).getGrowthDate().getMonth() + 1);
					pStatement.setInt(ii++, list.get(i).getGrowthDate().getDate());
					pStatement.setInt(ii++, list.get(i).getCropName());
					pStatement.setInt(ii++, list.get(i).getPeriodOfGrowth());
					pStatement.setInt(ii++, list.get(i).getGrowthDate().getYear() + 1900);
					pStatement.setInt(ii++, list.get(i).getGrowthDate().getMonth() +1);
					pStatement.setInt(ii++, list.get(i).getGrowthDate().getDate());
					pStatement.setInt(ii++, list.get(i).getDevelopAnomaly());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getPercentageOfGrowthPeriod())));
					pStatement.setInt(ii++, list.get(i).getGrowthState());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getPlantHeight())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getPlantDensity())));
					pStatement.setString(ii++, v_bbb);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setString(ii++, ctsCodeMap.getCts_code());
					
					di.setIIiii(list.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(list.get(i).getGrowthDate(), "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLONGTITUDE(String.valueOf(list.get(i).getLongitude()));
					di.setLATITUDE(String.valueOf(list.get(i).getLatitude()));
					di.setDATA_UPDATE_FLAG(v_bbb);
					di.setHEIGHT(list.get(i).getHeightOfSationGroundAboveMeanSeaLevel().toString());
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(file.length()));
					
					pStatement.addBatch();	
					listDi.add(di);
				} // for end
				try {
					pStatement.executeBatch();
					connection.commit();
					return DataBaseAction.SUCCESS;
				} catch (Exception e) {
					pStatement.clearParameters();
					pStatement.clearBatch();
					connection.rollback();
					for(int i = 0; i < list.size(); i ++){
						listDi.remove(sz);
					}
					for(int i = 0; i < list.size(); i ++){
						insertOneLine_db_CROP01(list.get(i), connection, recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMap);
					}
					loggerBuffer.append("\n Batch commit failed："+fileN);
					return DataBaseAction.BATCH_ERROR;
				}
			} // end if
			else {
				loggerBuffer.append("\n  Database connection error");
				return DataBaseAction.CONNECTION_ERROR;
			}
		}catch(SQLException e){
			loggerBuffer.append("\n  Database connection close error："+e.getMessage());
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
 * 
 * @Title: insert_db_CROP02   
 * @Description: TODO(crop02解码结果入库)   
 * @param list
 * @param connection
 * @param recv_time
 * @param fileN 
 * @param v_bbb 
 * @param loggerBuffer 
 * @return DataBaseAction      
 * @throws：
 */
	@SuppressWarnings("deprecation")
	private static DataBaseAction insert_db_CROP02(List<ZAgmeCrop02> list, java.sql.Connection connection, Date recv_time, String v_bbb, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		int sz = listDi.size();
		try {
			// chy 去掉 D_RECORD_ID 
			String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+"(D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,"
					+ "V01301,V01300,V05001,V06001,V07001,V07031,"
					+ "V_ACODE,V04001,V04002,V04003,"
					+ "V71001,V71002,"
					+ "V71656,V71655,V71604, V_BBB, D_UPDATE_TIME,D_SOURCE_ID) "
					+ "VALUES (?, ?, ?, ?,"
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, ?, ?, ?)";
			if(connection != null){
				connection.setAutoCommit(false);
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
//					pStatement.setString(ii++, sdf.format(list.get(i).getObservationDate())+"_"+list.get(i).getStationNumberChina()+"_"+list.get(i).getCropName()+"_"+list.get(i).getPeriodOfGrowth());
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
					pStatement.setInt(ii++, list.get(i).getObservationDate().getYear() + 1900 );
					pStatement.setInt(ii++, list.get(i).getObservationDate().getMonth() + 1);
					pStatement.setInt(ii++, list.get(i).getObservationDate().getDate());
					pStatement.setInt(ii++, list.get(i).getCropName());
					pStatement.setInt(ii++, list.get(i).getPeriodOfGrowth());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getPercentageOfGrowth())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getPercentageOfWater())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getIndexOfLeafArea())));
					pStatement.setString(ii++, v_bbb);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setString(ii++, ctsCodeMap.getCts_code());
					
					di.setIIiii(list.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(list.get(i).getObservationDate(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLONGTITUDE(String.valueOf(list.get(i).getLongitude()));
					di.setLATITUDE(String.valueOf(list.get(i).getLatitude()));
					di.setDATA_UPDATE_FLAG(v_bbb);
					di.setHEIGHT(list.get(i).getHeightOfSationGroundAboveMeanSeaLevel().toString());
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(file.length()));
					
					pStatement.addBatch();
					listDi.add(di);
				}// end for
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
					for(int i = 0; i < list.size(); i ++){
						insertOneLine_db_CROP02(list.get(i), connection, recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMap);
					}
					loggerBuffer.append("\n Batch commit failed："+fileN);
					return DataBaseAction.BATCH_ERROR;
				}
			} // end if
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
	 * 
	 * @Title: insert_db_CROP03   
	 * @Description: TODO(crop03解码结果入库)   
	 * @param list
	 * @param connection
	 * @param recv_time
	 * @param fileN 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @return DataBaseAction      
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static DataBaseAction insert_db_CROP03(List<ZAgmeCrop03> list, java.sql.Connection connection, Date recv_time, String v_bbb, String filepath, StringBuffer loggerBuffer,CTSCodeMap ctsCodeMap) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		int sz = listDi.size();
		// chy 去掉D_RECORD_ID
		String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+"(D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,"
					+ "V01301,V01300,V05001,V06001,V07001,V07031,"
					+ "V_ACODE,V04001,V04002,V04003,"
					+ "V71001,V71655,"
					+ "V71089,V71900, V_BBB,D_UPDATE_TIME,D_SOURCE_ID) "
					+ "VALUES (?, ?, ?, ?,"
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?,?,?, ?, ?)";
		try {
			if(connection != null){	
				connection.setAutoCommit(false);
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
//					pStatement.setString(ii++, sdf.format(list.get(i).getObservationDate())+"_"+list.get(i).getStationNumberChina()+"_"+list.get(i).getCropName());
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
					pStatement.setInt(ii++, list.get(i).getCropName());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getPercentageOfWater())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getDryWeight())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getFillingRate())));
					pStatement.setString(ii++, v_bbb);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setString(ii++, ctsCodeMap.getCts_code());
					
					di.setIIiii(list.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(list.get(i).getObservationDate(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLONGTITUDE(String.valueOf(list.get(i).getLongitude()));
					di.setLATITUDE(String.valueOf(list.get(i).getLatitude()));
					di.setDATA_UPDATE_FLAG(v_bbb);
					di.setHEIGHT(list.get(i).getHeightOfSationGroundAboveMeanSeaLevel().toString());
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(file.length()));
					pStatement.addBatch();
					
					listDi.add(di);
				} // end for
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
					for(int i = 0; i < list.size(); i ++){
						insertOneLine_db_CROP03(list.get(i), connection, recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMap);
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
	 * 
	 * @Title: insert_db_CROP04   
	 * @Description: TODO(crop04解码结果入库)   
	 * @param list
	 * @param connection
	 * @param recv_time
	 * @param fileN 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @return DataBaseAction      
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static DataBaseAction insert_db_CROP04(List<ZAgmeCrop04> list, java.sql.Connection connection, Date recv_time, String v_bbb, String filepath, StringBuffer loggerBuffer,CTSCodeMap ctsCodeMap) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		int sz = listDi.size();
		// chy去掉 D_RECORD_ID
		String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+"(D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,"
					+ "V01301,V01300,V05001,V06001,V07001,V07031,"
					+ "V_ACODE,V04001,V04002,V04003,"
					+ "V71001,V71002,"
					+ "V71616_01,V71632, V_BBB, D_UPDATE_TIME,D_SOURCE_ID) "
					+ "VALUES (?, ?, ?, ?,"
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, ?, ?)";
		try {
			if(connection != null){
				connection.setAutoCommit(false);
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
//					pStatement.setString(ii++, sdf.format(list.get(i).getObservationDate())+"_"+list.get(i).getStationNumberChina()+"_"+list.get(i).getCropName()+"_"+list.get(i).getPeriodOfGrowth()+"_"+list.get(i).getItemName1());
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
					pStatement.setInt(ii++, list.get(i).getCropName());
					pStatement.setInt(ii++, list.get(i).getPeriodOfGrowth());
					pStatement.setInt(ii++, list.get(i).getItemName1());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getObservationValue())));
					pStatement.setString(ii++, v_bbb);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setString(ii++, ctsCodeMap.getCts_code());
					
					di.setIIiii(list.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(list.get(i).getObservationDate(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLONGTITUDE(String.valueOf(list.get(i).getLongitude()));
					di.setLATITUDE(String.valueOf(list.get(i).getLatitude()));
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
					for(int i = 0; i < list.size(); i ++){
						insertOneLine_db_CROP04(list.get(i), connection, recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMap);
					}
					loggerBuffer.append("\n Batch commit failed："+fileN);
					return DataBaseAction.BATCH_ERROR;
				}
			} // end if
			else {
				loggerBuffer.append("\n Database connection error");
				return DataBaseAction.CONNECTION_ERROR;
			}
		}catch(SQLException e){
			loggerBuffer.append("\n Database connection close  error："+e.getMessage());
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
	 * 
	 * @Title: insert_db_CROP05   
	 * @Description: TODO(crop05解码结果入库)   
	 * @param list
	 * @param connection
	 * @param recv_time
	 * @param fileN 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @return DataBaseAction      
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static DataBaseAction insert_db_CROP05(List<ZAgmeCrop05> list,java.sql.Connection connection, Date recv_time, String v_bbb, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		int sz = listDi.size();
		// chy 去掉 D_RECORD_ID
		String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+"(D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,"
					+ "V01301,V01300,V05001,V06001,V07001,V07031,"
					+ "V_ACODE,V04001,V04002,V04003,"
					+ "V71001,V71002,"
					+ "V71616_01,V71632, V_BBB, D_UPDATE_TIME,D_SOURCE_ID) "
					+ "VALUES (?, ?, ?, ?,"
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?,?, ?, ?, ?, ?)";
		try{
			if(connection != null){	
				connection.setAutoCommit(false);
				pStatement = new LoggableStatement(connection, sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				//pStatement = connection.prepareStatement(sql);				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				List<String> sqls = new ArrayList<>();
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
//					pStatement.setString(ii++, sdf.format(list.get(i).getObservationDate())+"_"+list.get(i).getStationNumberChina()+"_"+list.get(i).getCropName()+"_"+noTask+"_"+list.get(i).getItemName1());
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
					pStatement.setInt(ii++, list.get(i).getCropName());
					pStatement.setInt(ii++, noTask);
					pStatement.setInt(ii++, list.get(i).getItemName1());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getObservationValue())));
					pStatement.setString(ii++, v_bbb);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setString(ii++, ctsCodeMap.getCts_code());
					
					sqls.add(((LoggableStatement)pStatement).getQueryString());
					di.setIIiii(list.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(list.get(i).getObservationDate(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLONGTITUDE(String.valueOf(list.get(i).getLongitude()));
					di.setLATITUDE(String.valueOf(list.get(i).getLatitude()));
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
					for(int i = 0; i < list.size(); i ++){
						insertOneLine_db_CROP05(list.get(i), connection, recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMap);
					}
					loggerBuffer.append("\n Batch commit failed："+fileN);
					return DataBaseAction.BATCH_ERROR;
				}
			} // end if
			else {
				loggerBuffer.append("\n  Database connection error");
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
	 * 
	 * @Title: insert_db_CROP06   
	 * @Description: TODO(crop06解码结果入库)   
	 * @param list
	 * @param connection
	 * @param recv_time
	 * @param fileN 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @return DataBaseAction      
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static DataBaseAction insert_db_CROP06(List<ZAgmeCrop06> list,java.sql.Connection connection, Date recv_time, String v_bbb, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		int sz = listDi.size();
		try {
			// chy 去掉 D_RECORD_ID
			String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+"(D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,"
					+ "V01301, V01300,V05001,V06001,V07001,V07031,"
					+ "V_ACODE,V04001,V04002,V04003,"
					+ "V04300_017,V04300_018, V71001,"
					+ "V71616_02,V71901, V71902,V_BBB, D_UPDATE_TIME,D_SOURCE_ID) "
					+ "VALUES (?, ?, ?, ?,"
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, ?,?, ?, ?)";
			if(connection != null){
				connection.setAutoCommit(false);
				pStatement = connection.prepareStatement(sql);			
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
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
//					pStatement.setString(ii++, sdf.format(list.get(i).getStartTime())+"_"+list.get(i).getStationNumberChina()+"_"+list.get(i).getCropName()+"_"+list.get(i).getItemName2());
					pStatement.setString(ii++, ctsCodeMap.getSod_code());
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(list.get(i).getStartTime().getTime()));
					pStatement.setString(ii++, list.get(i).getStationNumberChina());
					pStatement.setInt(ii++, stationNumberN);
					pStatement.setBigDecimal(ii++, new BigDecimal(list.get(i).getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(list.get(i).getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getHeightOfSationGroundAboveMeanSeaLevel())));              
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getHeightOfPressureSensor())));
					pStatement.setString(ii++, adminCode); // V_ACODE		
					pStatement.setInt(ii++, list.get(i).getStartTime().getYear() + 1900);
					pStatement.setInt(ii++, list.get(i).getStartTime().getMonth() + 1);
					pStatement.setInt(ii++, list.get(i).getStartTime().getDate());
					pStatement.setBigDecimal(ii++, new BigDecimal(df.format(list.get(i).getStartTime())));
					pStatement.setBigDecimal(ii++, new BigDecimal(df.format(list.get(i).getEndTime())));
					pStatement.setInt(ii++, list.get(i).getCropName());
					pStatement.setInt(ii++, list.get(i).getItemName2());
					pStatement.setInt(ii++, list.get(i).getQuality());
					try {
						pStatement.setString(ii++, new String(list.get(i).getMethodAndTool().getBytes(), "UTF8"));
					} catch (UnsupportedEncodingException e) {
						pStatement.setString(ii++, "999999");
						loggerBuffer.append("getMethodAndTool() format error: "+e.getMessage());
					}
					pStatement.setString(ii++, v_bbb);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setString(ii++, ctsCodeMap.getCts_code());
					
					di.setIIiii(list.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(list.get(i).getStartTime(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLONGTITUDE(String.valueOf(list.get(i).getLongitude()));
					di.setLATITUDE(String.valueOf(list.get(i).getLatitude()));
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
					for(int i = 0; i < list.size(); i ++){
						insertOneLine_db_CROP06(list.get(i), connection, recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMap);
					}
					loggerBuffer.append("\n Batch commit failed："+fileN);
					return DataBaseAction.BATCH_ERROR;
				}
			} // end if		
			else {
				loggerBuffer.append("\n Database connection error");
				return DataBaseAction.CONNECTION_ERROR;
			}
		}catch(SQLException e){
			loggerBuffer.append("\n  Database connection close error："+e.getMessage());
			return DataBaseAction.CONNECTION_ERROR;
		}finally {
			try {
				if(pStatement != null)
					pStatement.close();
			} catch (SQLException e) {
				loggerBuffer.append("\n Database connection  close error："+e.getMessage());
			}			
		}
	}
	/**
	 * 
	 * @Title: insert_db_CROP07   
	 * @Description: TODO(crop07解码结果入库)   
	 * @param list
	 * @param connection
	 * @param recv_time
	 * @param fileN 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @return DataBaseAction      
	 * @throws：
	 */
	private static DataBaseAction insert_db_CROP07(List<ZAgmeCrop07> list,java.sql.Connection connection, Date recv_time, String v_bbb, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		int sz = listDi.size();
		try {
			// chy 去掉 D_RECORD_ID
			String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+"(D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,"
					+ "V01301,V01300,V05001,V06001,V07001,V07031,"
					+ "V_ACODE,V04001,"
					+ "V71001,"
					+ "V71601,V71091, V71083,V_BBB, D_UPDATE_TIME,D_SOURCE_ID) "
					+ "VALUES (?, ?, ?, ?,"
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?,?, ?, ?)";
			
			if(connection != null){		
				connection.setAutoCommit(false);
				pStatement = connection.prepareStatement(sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
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
					String DateStr = list.get(i).getYear() + "0101000000";
//					pStatement.setString(ii++, DateStr +"_"+list.get(i).getStationNumberChina()+"_"+list.get(i).getCropName());
					pStatement.setString(ii++, ctsCodeMap.getSod_code());
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(TimeUtil.String2Date(DateStr, "yyyyMMddHHmmss").getTime()));
					pStatement.setString(ii++, list.get(i).getStationNumberChina());
					pStatement.setInt(ii++, stationNumberN);
					pStatement.setBigDecimal(ii++, new BigDecimal(list.get(i).getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(list.get(i).getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getHeightOfSationGroundAboveMeanSeaLevel())));              
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getHeightOfPressureSensor())));
					pStatement.setString(ii++, adminCode); // V_ACODE		
					pStatement.setInt(ii++, list.get(i).getYear());
					pStatement.setInt(ii++, list.get(i).getCropName());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getOutputLevelOfStation())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getCountyAverageOutput())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getCountyOutputChangeRate())));
					pStatement.setString(ii++, v_bbb);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setString(ii++, ctsCodeMap.getCts_code());
					
					di.setIIiii(list.get(i).getStationNumberChina());
					di.setDATA_TIME(String.valueOf(TimeUtil.date2String(TimeUtil.String2Date(DateStr, "yyyyMMddHHmmss"), "yyyy-MM-dd HH:mm")));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLONGTITUDE(String.valueOf(list.get(i).getLongitude()));
					di.setLATITUDE(String.valueOf(list.get(i).getLatitude()));
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
					for(int i = 0; i < list.size(); i ++){
						insertOneLine_db_CROP07(list.get(i), connection, recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMap);
					}
					loggerBuffer.append("\n Batch commit failed："+fileN);
					return DataBaseAction.BATCH_ERROR;
				}
			} // end if		
			else {
				loggerBuffer.append("\n Database connection error");
				return DataBaseAction.CONNECTION_ERROR;
			}
		}catch(SQLException e){
			loggerBuffer.append("\n  Database connection  close error："+e.getMessage());
			return DataBaseAction.CONNECTION_ERROR;
		}finally {
			try {
				if(pStatement != null)
					pStatement.close();
			} catch (SQLException e) {
				loggerBuffer.append("\n Database connection  close error："+e.getMessage());
			}			
		}
	}
	/**
	 * 
	 * @Title: insert_db_CROP08   
	 * @Description: TODO(crop08解码结果入库)   
	 * @param list
	 * @param connection
	 * @param recv_time
	 * @param fileN 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @return DataBaseAction      
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static DataBaseAction insert_db_CROP08(List<ZAgmeCrop08> list,java.sql.Connection connection, Date recv_time, String v_bbb, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		int sz = listDi.size();
		// chy 去掉D_RECORD_ID
		String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+"(D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,"
					+ "V01301,V01300,V05001,V06001,V07001,V07031,"
					+ "V_ACODE,V04001,V04002,V04003,V04004,V04005,V04006,"
					+ "V71001,V71002,V71650,V71651,V71652,"
					+ "V71653,V71654, V71655,V71656,V_BBB, D_UPDATE_TIME,D_SOURCE_ID) "
					+ "VALUES (?, ?, ?, ?,"
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, ?,?,?, ?, ?)";
		try {	
			if(connection != null){
				connection.setAutoCommit(false);
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
//					pStatement.setString(ii++, sdf.format(list.get(i).getObservationDate())+"_"+list.get(i).getStationNumberChina()+"_"+list.get(i).getCropName()+"_"+list.get(i).getPeriodOfGrowth()+"_"+list.get(i).getOrganName());
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
					pStatement.setInt(ii++, list.get(i).getObservationDate().getMinutes());
					pStatement.setInt(ii++, list.get(i).getObservationDate().getSeconds());
					pStatement.setInt(ii++, list.get(i).getCropName());
					pStatement.setInt(ii++, list.get(i).getPeriodOfGrowth());
					pStatement.setInt(ii++, list.get(i).getOrganName());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getPlantFreshWeight())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getPlantDryWeight())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getAvgFreshWeightPerSquareMeter())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getAvgDryWeightPerSquareMeter())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getPercentageOfWater())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getPercentageOfGrowth())));
					pStatement.setString(ii++, v_bbb);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setString(ii++, ctsCodeMap.getCts_code());
					
					di.setIIiii(list.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(list.get(i).getObservationDate(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLONGTITUDE(String.valueOf(list.get(i).getLongitude()));
					di.setLATITUDE(String.valueOf(list.get(i).getLatitude()));
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
					for(int i = 0; i < list.size(); i ++){
						insertOneLine_db_CROP08(list.get(i), connection, recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMap);
					}
					loggerBuffer.append("\n Batch commit failed："+fileN);
					return DataBaseAction.BATCH_ERROR;
				}
			} // end if		
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
				loggerBuffer.append("\n Database connection close  error："+e.getMessage());
			}			
		}
	}
	/**
	 * 
	 * @Title: insert_db_CROP09   
	 * @Description: TODO(crop09解码结果入库)   
	 * @param list
	 * @param connection
	 * @param recv_time
	 * @param fileN 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @return DataBaseAction      
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static DataBaseAction insert_db_CROP09(List<ZAgmeCrop09> list,java.sql.Connection connection, Date recv_time, String v_bbb, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		int sz = listDi.size();
		// chy  去掉 D_RECORD_ID
		String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+"(D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,"
					+ "V01301,V01300,V05001,V06001,V07001,V07031,"
					+ "V_ACODE,V04001,V04002,V04003,"
					+ "V71001,V71903,V04300_017,V04300_018,"
					+ "V71091, V_BBB, D_UPDATE_TIME,D_SOURCE_ID) "
					+ "VALUES (?, ?, ?, ?,"
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			if(connection != null){	
				connection.setAutoCommit(false);
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
//					pStatement.setString(ii++, sdf.format(list.get(i).getSeedingTime())+"_"+list.get(i).getStationNumberChina()+"_"+list.get(i).getCropName()+"_"+list.get(i).getFieldLevel());
					pStatement.setString(ii++, ctsCodeMap.getSod_code());
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(list.get(i).getSeedingTime().getTime()));
					pStatement.setString(ii++, list.get(i).getStationNumberChina());
					pStatement.setInt(ii++, stationNumberN);
					pStatement.setBigDecimal(ii++, new BigDecimal(list.get(i).getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(list.get(i).getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getHeightOfSationGroundAboveMeanSeaLevel())));              
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getHeightOfPressureSensor())));
					pStatement.setString(ii++, adminCode); // V_ACODE		
					pStatement.setInt(ii++, list.get(i).getSeedingTime().getYear() + 1900);
					pStatement.setInt(ii++, list.get(i).getSeedingTime().getMonth() + 1);
					pStatement.setInt(ii++, list.get(i).getSeedingTime().getDate());
					pStatement.setInt(ii++, list.get(i).getCropName());
					pStatement.setInt(ii++, list.get(i).getFieldLevel());
					pStatement.setBigDecimal(ii++, new BigDecimal(sdf.format(list.get(i).getSeedingTime())));
					pStatement.setBigDecimal(ii++, new BigDecimal(sdf.format(list.get(i).getHarvestTime())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getUnitOuput())));
					pStatement.setString(ii++, v_bbb);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setString(ii++, ctsCodeMap.getCts_code());
					
					di.setIIiii(list.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(list.get(i).getSeedingTime(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLONGTITUDE(String.valueOf(list.get(i).getLongitude()));
					di.setLATITUDE(String.valueOf(list.get(i).getLatitude()));
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
					for(int i = 0; i < list.size(); i ++){
						insertOneLine_db_CROP09(list.get(i), connection, recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMap);
					}
					loggerBuffer.append("\n Batch commit failed："+fileN);
					return DataBaseAction.BATCH_ERROR;
				}
			} // end if		
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
	 * 
	 * @Title: insert_db_CROP10   
	 * @Description: TODO(crop10解码结果入库)   
	 * @param list
	 * @param connection
	 * @param recv_time
	 * @param fileN 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @return DataBaseAction      
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static DataBaseAction insert_db_CROP10(List<ZAgmeCrop10> list,java.sql.Connection connection, Date recv_time, String v_bbb, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		int sz = listDi.size();
		// chy 去掉 D_RECORD_ID
		String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+"(D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,"
					+ "V01301,V01300,V05001,V06001,V07001,V07031,"
					+ "V_ACODE,V04001,V04002,V04003,V04004,V04005,V04006,"
					+ "V71903,V71001,V71002,V71006,V71008,V71007,"
					+ "V71630_1, V71632_1,"
					+ "V71630_2, V71632_2,"
					+ "V71630_3, V71632_3,"
					+ "V71630_4, V71632_4, V_BBB, D_UPDATE_TIME,D_SOURCE_ID) "
					+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?,?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?)";
		try {	
			if(connection != null){
				connection.setAutoCommit(false);
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
//					pStatement.setString(ii++, sdf.format(list.get(i).getObservationDate())+"_"+list.get(i).getStationNumberChina()+"_"+list.get(i).getFieldLevel()+"_"+list.get(i).getCropName()+"_"+list.get(i).getPeriodOfGrowth());
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
					pStatement.setInt(ii++, list.get(i).getObservationDate().getMinutes());
					pStatement.setInt(ii++, list.get(i).getObservationDate().getSeconds());	
					pStatement.setInt(ii++, list.get(i).getFieldLevel());
					pStatement.setInt(ii++, list.get(i).getCropName());
					pStatement.setInt(ii++, list.get(i).getPeriodOfGrowth());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getPlantHeight())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getPlantDensity())));
					pStatement.setInt(ii++, list.get(i).getGrowthState());
					pStatement.setInt(ii++, list.get(i).getOutputFactorName1());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getOutputFactorMeasureValue1())));				
					pStatement.setInt(ii++, list.get(i).getOutputFactorName2());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getOutputFactorMeasureValue2())));
					pStatement.setInt(ii++, list.get(i).getOutputFactorName3());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getOutputFactorMeasureValue3())));
					pStatement.setInt(ii++, list.get(i).getOutputFactorName4());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getOutputFactorMeasureValue4())));
					pStatement.setString(ii++, v_bbb);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setString(ii++, ctsCodeMap.getCts_code());
					
					di.setIIiii(list.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(list.get(i).getObservationDate(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLONGTITUDE(String.valueOf(list.get(i).getLongitude()));
					di.setLATITUDE(String.valueOf(list.get(i).getLatitude()));
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
					for(int i = 0; i < list.size(); i ++){
						insertOneLine_db_CROP10(list.get(i), connection, recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMap);
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
			loggerBuffer.append("\n Database connection clsoe error："+e.getMessage());
			return DataBaseAction.CONNECTION_ERROR;
		}finally {
			try {
				if(pStatement != null)
					pStatement.close();
			} catch (SQLException e) {
				loggerBuffer.append("\n Database connection  close error："+e.getMessage());
			}			
		}
	}
	
	/**
	 * 
	 * @param fileN 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @Title: EleUpdateCROP01   
	 * @Description: TODO(当文件名带有更正标识时的入库处理)   
	 * @param: @param cr01
	 * @param: @param recv_time
	 * @param: @return      
	 * @return: DataBaseAction      
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static void EleUpdateCROP01(List<ZAgmeCrop01> cr01, java.sql.Connection connection, Date recv_time, String v_bbb, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap){
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement Pstmt = null;
		int i = 0;	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		for(i = 0; i < cr01.size(); i ++){
			String vbbbInDB = null;
			vbbbInDB = findVBBBCrop01(cr01.get(i), connection,loggerBuffer, ctsCodeMap.getValue_table_name()); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if(vbbbInDB == null){ // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				// insert
				insertOneLine_db_CROP01(cr01.get(i), connection, recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMap);
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
						+ "V_BBB=?, D_UPDATE_TIME=?,V71005=?,V71010=?,V71007=?,V71006=?,V71008=? "
						+ "where V01301 = ? and V04001 = ?"
						+ " and V04002 = ? and V04003 = ? and V71001 = ? and V71002 = ?";
				if(connection != null){
					try{
						connection.setAutoCommit(true);
						Pstmt = new LoggableStatement(connection, sql01);
						
						int ii = 1;
						Pstmt.setString(ii++, v_bbb);
						Pstmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						Pstmt.setInt(ii++, cr01.get(i).getDevelopAnomaly());
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr01.get(i).getPercentageOfGrowthPeriod())));
						Pstmt.setInt(ii++, cr01.get(i).getGrowthState());
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr01.get(i).getPlantHeight())));
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr01.get(i).getPlantDensity())));
							
						Pstmt.setString(ii++, cr01.get(i).getStationNumberChina());
						Pstmt.setInt(ii++, cr01.get(i).getGrowthDate().getYear() + 1900);
						Pstmt.setInt(ii++, cr01.get(i).getGrowthDate().getMonth() + 1);
						Pstmt.setInt(ii++, cr01.get(i).getGrowthDate().getDate());
						Pstmt.setInt(ii++, cr01.get(i).getCropName());
						Pstmt.setInt(ii++, cr01.get(i).getPeriodOfGrowth());
								
						di.setIIiii(cr01.get(i).getStationNumberChina());
						di.setDATA_TIME(TimeUtil.date2String(cr01.get(i).getGrowthDate(), "yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setLONGTITUDE(String.valueOf(cr01.get(i).getLongitude()));
						di.setLATITUDE(String.valueOf(cr01.get(i).getLatitude()));
						di.setDATA_UPDATE_FLAG(v_bbb);
						di.setHEIGHT(cr01.get(i).getHeightOfSationGroundAboveMeanSeaLevel().toString());
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(file.length()));
						try{
							Pstmt.execute();	
							listDi.add(di);	
						}
						catch(SQLException e){							
							di.setPROCESS_STATE("0");//1成功，0失败
							listDi.add(di);		
							loggerBuffer.append("\n filename："+fileN+
									"\n " + cr01.get(i).getStationNumberChina() + " " + sdf.format(cr01.get(i).getGrowthDate())
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
	 * 
	 * @param fileN 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @Title: EleUpdateCROP02   
	 * @Description: TODO(当文件名带有更正标识时的入库处理)   
	 * @param: @param cr02
	 * @param: @param recv_time
	 * @param: @return      
	 * @return: DataBaseAction      
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static void EleUpdateCROP02(List<ZAgmeCrop02> cr02, java.sql.Connection connection, Date recv_time, String v_bbb, String filepath, StringBuffer loggerBuffer,CTSCodeMap ctsCodeMap){
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement Pstmt = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		int i = 0;
		for(i = 0; i < cr02.size(); i ++){
			String vbbbInDB = null;
			vbbbInDB = findVBBBCrop02(cr02.get(i),connection,loggerBuffer, ctsCodeMap.getValue_table_name()); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if(vbbbInDB == null){ // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				// insert
				insertOneLine_db_CROP02(cr02.get(i), connection,recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMap);
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

				String sql02 =  "update "+ctsCodeMap.getValue_table_name()+" set "
						+ "V_BBB=?, D_UPDATE_TIME=?,V71656=?,V71655=?,V71604=? "
						+ "where V01301 = ? and V04001 = ?"
						+ " and V04002 = ? and V04003 = ? and V71001 = ? and V71002 = ?";
				if(connection != null){
					try{
						connection.setAutoCommit(true);
						Pstmt = new LoggableStatement(connection, sql02);

						int ii = 1;
						Pstmt.setString(ii++, v_bbb);
						Pstmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr02.get(i).getPercentageOfGrowth())));
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr02.get(i).getPercentageOfWater())));
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr02.get(i).getIndexOfLeafArea())));
							
						Pstmt.setString(ii++, cr02.get(i).getStationNumberChina());
						Pstmt.setInt(ii++, cr02.get(i).getObservationDate().getYear() +1900);
						Pstmt.setInt(ii++, cr02.get(i).getObservationDate().getMonth() + 1);
						Pstmt.setInt(ii++, cr02.get(i).getObservationDate().getDate());
						Pstmt.setInt(ii++, cr02.get(i).getCropName());
						Pstmt.setInt(ii++, cr02.get(i).getPeriodOfGrowth());
						
						di.setIIiii(cr02.get(i).getStationNumberChina());
						di.setDATA_TIME(TimeUtil.date2String(cr02.get(i).getObservationDate(), "yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setLONGTITUDE(String.valueOf(cr02.get(i).getLongitude()));
						di.setLATITUDE(String.valueOf(cr02.get(i).getLatitude()));
						di.setDATA_UPDATE_FLAG(v_bbb);
						di.setHEIGHT(cr02.get(i).getHeightOfSationGroundAboveMeanSeaLevel().toString());
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(file.length()));
						try{
							Pstmt.execute();
							listDi.add(di);	
						}
						catch(SQLException e){							
							di.setPROCESS_STATE("0");//1成功，0失败
							listDi.add(di);		
							loggerBuffer.append("\n filename："+fileN+
									"\n " + cr02.get(i).getStationNumberChina() + " " + sdf.format(cr02.get(i).getObservationDate())
									+"\n execute sql error："+((LoggableStatement)Pstmt).getQueryString()+"\n "+e.getMessage());
						}				
					}	catch (SQLException e) {
					loggerBuffer.append("\n execute Statement error: "+ e.getMessage());
					}finally {
						try {
							if(Pstmt != null)
								Pstmt.close();
						} catch (SQLException e) {
							loggerBuffer.append("\n close Statement error: "+ e.getMessage());
						}	
					}
				}else
					loggerBuffer.append("\n  Database connection error ");		
			} 
			else{ // 数据库中有该条记录，且V_BBB的值晚于当前处理文件的更正标识的值
			// 不进行插入或更新	
			}
		} 
	}
	/**
	 * 
	 * @Title: EleUpdateCROP03   
	 * @Description: TODO(当文件名带有更正标识时的入库处理)   
	 * @param cr03
	 * @param connection
	 * @param recv_time void      
	 * @param fileN 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static void EleUpdateCROP03(List<ZAgmeCrop03> cr03, java.sql.Connection connection, Date recv_time, String v_bbb, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap){
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement Pstmt = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		int i = 0;
		for(i = 0; i < cr03.size(); i ++){
			String vbbbInDB = null;
			vbbbInDB = findVBBBCrop03(cr03.get(i), connection,loggerBuffer, ctsCodeMap.getValue_table_name()); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if(vbbbInDB == null){ // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				// insert
				insertOneLine_db_CROP03(cr03.get(i), connection,recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMap);
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
				
				String sql03 =  "update "+ctsCodeMap.getValue_table_name()+" set "
						+ "V_BBB=?,D_UPDATE_TIME=?,V71655=?,V71089=?,V71900=? "
						+ "where V01301 = ? and V04001 = ?"
						+ " and V04002 = ? and V04003 = ? and V71001 = ?";
				if(connection != null){	
					try{	
						connection.setAutoCommit(true);
						Pstmt = new LoggableStatement(connection, sql03);

						int ii = 1;
						Pstmt.setString(ii++, v_bbb);
						Pstmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr03.get(i).getPercentageOfWater())));
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr03.get(i).getDryWeight())));
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr03.get(i).getFillingRate())));
							
						Pstmt.setString(ii++, cr03.get(i).getStationNumberChina());
						Pstmt.setInt(ii++, cr03.get(i).getObservationDate().getYear() + 1900);
						Pstmt.setInt(ii++, cr03.get(i).getObservationDate().getMonth() + 1);
						Pstmt.setInt(ii++, cr03.get(i).getObservationDate().getDate());
						Pstmt.setInt(ii++, cr03.get(i).getCropName());
							
						di.setIIiii(cr03.get(i).getStationNumberChina());
						di.setDATA_TIME(TimeUtil.date2String(cr03.get(i).getObservationDate(), "yyyy-MM-dd HH:mm:ss"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setLONGTITUDE(String.valueOf(cr03.get(i).getLongitude()));
						di.setLATITUDE(String.valueOf(cr03.get(i).getLatitude()));
						try{
							Pstmt.execute();	
							listDi.add(di);	
						}
						catch(SQLException e){							
							di.setPROCESS_STATE("0");//1成功，0失败
							listDi.add(di);		
							loggerBuffer.append("\n filename："+fileN+
									"\n " + cr03.get(i).getStationNumberChina() + " " + sdf.format(cr03.get(i).getObservationDate())
									+"\n execute sql error："+((LoggableStatement)Pstmt).getQueryString()+"\n "+e.getMessage());
						}				
					}	catch (SQLException e) {
					loggerBuffer.append("\n execute Statement  error: "+ e.getMessage());
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
	 * 
	 * @Title: EleUpdateCROP04   
	 * @Description: TODO(当文件名带有更正标识时的入库处理)   
	 * @param cr04
	 * @param connection
	 * @param recv_time void      
	 * @param fileN 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static void EleUpdateCROP04(List<ZAgmeCrop04> cr04, java.sql.Connection connection, Date recv_time, String v_bbb, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap){
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement Pstmt = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		int i = 0;
		for(i = 0; i < cr04.size(); i ++){
			String vbbbInDB = null;
			vbbbInDB = findVBBBCrop04(cr04.get(i),connection,loggerBuffer, ctsCodeMap.getValue_table_name()); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if(vbbbInDB == null){ // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				// insert
				insertOneLine_db_CROP04(cr04.get(i), connection,recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMap);
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
				
				String sql04 =  "update "+ctsCodeMap.getValue_table_name()+" set "
						+ "V_BBB=?,D_UPDATE_TIME=?,V71632=? "
						+ "where V01301 = ? and V04001 = ?"
						+ " and V04002 = ? and V04003 = ? and V71001 = ? and V71002 = ? and V71616_01 = ?";
				if(connection != null){		
					try{	
					//	Pstmt = connection.prepareStatement(sql04);
						connection.setAutoCommit(true);
						Pstmt = new LoggableStatement(connection, sql04);
					
						int ii = 1;
						Pstmt.setString(ii++, v_bbb);
						Pstmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr04.get(i).getObservationValue())));
							
						Pstmt.setString(ii++, cr04.get(i).getStationNumberChina());
						Pstmt.setInt(ii++, cr04.get(i).getObservationDate().getYear() + 1900);
						Pstmt.setInt(ii++, cr04.get(i).getObservationDate().getMonth() + 1);
						Pstmt.setInt(ii++, cr04.get(i).getObservationDate().getDate());
						Pstmt.setInt(ii++, cr04.get(i).getCropName());
						Pstmt.setInt(ii++, cr04.get(i).getPeriodOfGrowth());
						Pstmt.setInt(ii++, cr04.get(i).getItemName1());
						
						di.setIIiii(cr04.get(i).getStationNumberChina());
						di.setDATA_TIME(TimeUtil.date2String(cr04.get(i).getObservationDate(), "yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setLONGTITUDE(String.valueOf(cr04.get(i).getLongitude()));
						di.setLATITUDE(String.valueOf(cr04.get(i).getLatitude()));
						di.setDATA_UPDATE_FLAG(v_bbb);
						di.setHEIGHT(cr04.get(i).getHeightOfSationGroundAboveMeanSeaLevel().toString());
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(file.length()));
						try{
							Pstmt.execute();
							listDi.add(di);	
						}
						catch(SQLException e){							
							di.setPROCESS_STATE("0");//1成功，0失败
							listDi.add(di);		
							loggerBuffer.append("\n filename："+fileN+
									"\n " + cr04.get(i).getStationNumberChina() + " " + sdf.format(cr04.get(i).getObservationDate())
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
	 * 
	 * @Title: EleUpdateCROP05   
	 * @Description: TODO(当文件名带有更正标识时的入库处理)   
	 * @param cr05
	 * @param connection
	 * @param recv_time void      
	 * @param fileN 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static void EleUpdateCROP05(List<ZAgmeCrop05> cr05, java.sql.Connection connection, Date recv_time, String v_bbb, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap){
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement Pstmt = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		int i = 0;
		for(i = 0; i < cr05.size(); i ++){
			String vbbbInDB = null;
			vbbbInDB = findVBBBCrop05(cr05.get(i), connection,loggerBuffer, ctsCodeMap.getValue_table_name()); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if(vbbbInDB == null){ // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				// insert
				insertOneLine_db_CROP05(cr05.get(i), connection, recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMap);
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
				
				String sql04 =  "update "+ctsCodeMap.getValue_table_name()+" set "
						+ "V_BBB=?,D_UPDATE_TIME=?,V71632=? "
						+ "where V01301 = ? and V04001 = ?"
						+ " and V04002 = ? and V04003 = ? and V71001 = ? and V71002 = ? and V71616_01 = ?";
				if(connection != null){	
					try{	
						connection.setAutoCommit(true);
						Pstmt = new LoggableStatement(connection, sql04);
					
						int ii = 1;
						Pstmt.setString(ii++, v_bbb);
						Pstmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr05.get(i).getObservationValue())));
							
						Pstmt.setString(ii++, cr05.get(i).getStationNumberChina());
						Pstmt.setInt(ii++, cr05.get(i).getObservationDate().getYear() + 1900);
						Pstmt.setInt(ii++, cr05.get(i).getObservationDate().getMonth() + 1);
						Pstmt.setInt(ii++, cr05.get(i).getObservationDate().getDate());
						Pstmt.setInt(ii++, cr05.get(i).getCropName());
						Pstmt.setInt(ii++, noTask);
						Pstmt.setInt(ii++, cr05.get(i).getItemName1());
							
						di.setIIiii(cr05.get(i).getStationNumberChina());
						di.setDATA_TIME(TimeUtil.date2String(cr05.get(i).getObservationDate(), "yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setLONGTITUDE(String.valueOf(cr05.get(i).getLongitude()));
						di.setLATITUDE(String.valueOf(cr05.get(i).getLatitude()));
						di.setDATA_UPDATE_FLAG(v_bbb);
						di.setHEIGHT(cr05.get(i).getHeightOfSationGroundAboveMeanSeaLevel().toString());
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(file.length()));
						try{
							Pstmt.execute();
							listDi.add(di);	
						}
						catch(SQLException e){							
							di.setPROCESS_STATE("0");//1成功，0失败
							listDi.add(di);		
							loggerBuffer.append("\n filename："+fileN+
									"\n " + cr05.get(i).getStationNumberChina() + " " + sdf.format(cr05.get(i).getObservationDate())
									+"\n execute sql error："+((LoggableStatement)Pstmt).getQueryString()+"\n "+e.getMessage());
						}				
					}	catch (SQLException e) {
					loggerBuffer.append("\n create Statement  error: "+ e.getMessage());
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
	 * 
	 * @Title: EleUpdateCROP06   
	 * @Description: TODO(当文件名带有更正标识时的入库处理)   
	 * @param cr06
	 * @param connection
	 * @param recv_time void      
	 * @param fileN 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static void EleUpdateCROP06(List<ZAgmeCrop06> cr06, java.sql.Connection connection, Date recv_time, String v_bbb, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap){
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement Pstmt = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		int i = 0;
		for(i = 0; i < cr06.size(); i ++){
			String vbbbInDB = null;
			vbbbInDB = findVBBBCrop06(cr06.get(i), connection,loggerBuffer, ctsCodeMap.getValue_table_name()); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if(vbbbInDB == null){ // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				// insert
				insertOneLine_db_CROP06(cr06.get(i), connection, recv_time,v_bbb,filepath,loggerBuffer, ctsCodeMap);
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
				
				String sql06 =  "update "+ctsCodeMap.getValue_table_name()+" set "
						+ "V_BBB=?,D_UPDATE_TIME=?,V04300_018=?,V71901=?,V71902=? "
						+ "where V01301 = ? and V04001 = ?"
						+ " and V04002 = ? and V04003 = ? and V71001 = ? and V71616_02 = ?";
				
				if(connection != null){		
					try{	
						connection.setAutoCommit(true);
						Pstmt = new LoggableStatement(connection, sql06);
						
						int ii = 1;
						Pstmt.setString(ii++, v_bbb);
						Pstmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						Pstmt.setBigDecimal(ii++, new BigDecimal(sdf.format(cr06.get(i).getEndTime())));
						Pstmt.setInt(ii++, cr06.get(i).getQuality());
						
						try {
							Pstmt.setString(ii++, new String(cr06.get(i).getMethodAndTool().getBytes(),"UTF8"));
						} catch (UnsupportedEncodingException e1) {
							Pstmt.setString(ii++, "999999");
							loggerBuffer.append("getMethodAndTool() formate error: "+ e1.getMessage());
						}
							
						Pstmt.setString(ii++, cr06.get(i).getStationNumberChina());
						Pstmt.setInt(ii++, cr06.get(i).getStartTime().getYear() +1900);
						Pstmt.setInt(ii++, cr06.get(i).getStartTime().getMonth() + 1);
						Pstmt.setInt(ii++, cr06.get(i).getStartTime().getDate());
						Pstmt.setInt(ii++, cr06.get(i).getCropName());
						Pstmt.setInt(ii++, cr06.get(i).getItemName2());
							
						di.setIIiii(cr06.get(i).getStationNumberChina());
						di.setDATA_TIME(TimeUtil.date2String(cr06.get(i).getStartTime(), "yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setLONGTITUDE(String.valueOf(cr06.get(i).getLongitude()));
						di.setLATITUDE(String.valueOf(cr06.get(i).getLatitude()));
						di.setDATA_UPDATE_FLAG(v_bbb);
						di.setHEIGHT(cr06.get(i).getHeightOfSationGroundAboveMeanSeaLevel().toString());
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(file.length()));
						try{
							Pstmt.execute();
							listDi.add(di);	
						}
						catch(SQLException e){							
							di.setPROCESS_STATE("0");//1成功，0失败
							listDi.add(di);		
							loggerBuffer.append("\n filename："+fileN+
									"\n " + cr06.get(i).getStationNumberChina() + " " + sdf.format(cr06.get(i).getStartTime())
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
	 * 
	 * @Title: EleUpdateCROP07   
	 * @Description: TODO(当文件名带有更正标识时的入库处理)   
	 * @param cr07
	 * @param connection
	 * @param recv_time void      
	 * @param fileN 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @throws：
	 */
	private static void EleUpdateCROP07(List<ZAgmeCrop07> cr07, java.sql.Connection connection, Date recv_time, String v_bbb, String filepath, StringBuffer loggerBuffer, CTSCodeMap codeMap){
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement Pstmt = null;
		int i = 0;
		for(i = 0; i < cr07.size(); i ++){
			String vbbbInDB = null;
			vbbbInDB = findVBBBCrop07(cr07.get(i), connection,loggerBuffer, codeMap.getValue_table_name()); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if(vbbbInDB == null){ // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				// insert
				insertOneLine_db_CROP07(cr07.get(i), connection, recv_time,v_bbb,filepath,loggerBuffer, codeMap);
			}
			else if(vbbbInDB.compareTo(v_bbb) < 0){ // 数据库中有该条记录，且V_BBB的值早于当前处理文件的更正标识的值
				// update
				StatDi di = new StatDi();				
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(codeMap.getSod_code());
				di.setDATA_TYPE_1(codeMap.getCts_code());
				di.setTT("");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //1成功，0失败
				di.setPROCESS_STATE("1");  //1成功，0失败
				
				String sql07 =  "update "+codeMap.getValue_table_name()+" set "
						+ "V_BBB=?,D_UPDATE_TIME=?,V71601=?,V71091=?,V71083=? "
						+ "where V01301 = ? and V04001 = ?"
						+ " and V71001 = ?";
				
				if(connection != null){	
					try{	
						connection.setAutoCommit(true);
						Pstmt = new LoggableStatement(connection, sql07);
						
						int ii = 1;
						Pstmt.setString(ii++, v_bbb);
						Pstmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr07.get(i).getOutputLevelOfStation())));
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr07.get(i).getCountyAverageOutput())));
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr07.get(i).getCountyOutputChangeRate())));
						
						Pstmt.setString(ii++, cr07.get(i).getStationNumberChina());
						SimpleDateFormat yearsdf = new SimpleDateFormat("yyyyMMddHHmmss");
						Pstmt.setTimestamp(ii++, new Timestamp(yearsdf.parse(String.valueOf(cr07.get(i).getYear() + "0101000000")).getTime()));
						Pstmt.setInt(ii++, cr07.get(i).getCropName());
						
						di.setIIiii(cr07.get(i).getStationNumberChina());
						di.setDATA_TIME(String.valueOf(TimeUtil.date2String(TimeUtil.String2Date(cr07.get(i).getYear() + "0101000000", "yyyyMMddHHmmss"), "yyyy-MM-dd HH:mm")));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setLONGTITUDE(String.valueOf(cr07.get(i).getLongitude()));
						di.setLATITUDE(String.valueOf(cr07.get(i).getLatitude()));
						di.setDATA_UPDATE_FLAG(v_bbb);
						di.setHEIGHT(cr07.get(i).getHeightOfSationGroundAboveMeanSeaLevel().toString());
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(file.length()));
						try{
							Pstmt.execute();
							listDi.add(di);	
						}
						catch(SQLException e){							
							di.setPROCESS_STATE("0");//1成功，0失败
							listDi.add(di);		
							loggerBuffer.append("\n filename："+fileN+
									"\n " + cr07.get(i).getStationNumberChina() + " " + cr07.get(i)
									+"\n execute sql error："+((LoggableStatement)Pstmt).getQueryString()+"\n "+e.getMessage());
						}				
					}	catch (SQLException | ParseException e) {
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
	 * 
	 * @Title: EleUpdateCROP08   
	 * @Description: TODO(当文件名带有更正标识时的入库处理)   
	 * @param cr08
	 * @param connection
	 * @param recv_time void      
	 * @param fileN 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static void EleUpdateCROP08(List<ZAgmeCrop08> cr08,java.sql.Connection connection, Date recv_time, String v_bbb, String filepath, StringBuffer loggerBuffer, CTSCodeMap codeMap){
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement Pstmt = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		int i = 0;
		for(i = 0; i < cr08.size(); i ++){
			String vbbbInDB = null;
			vbbbInDB = findVBBBCrop08(cr08.get(i), connection,loggerBuffer, codeMap.getValue_table_name()); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if(vbbbInDB == null){ // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				// insert
				insertOneLine_db_CROP08(cr08.get(i),connection, recv_time,v_bbb,filepath,loggerBuffer, codeMap);
			}
			else if(vbbbInDB.compareTo(v_bbb) < 0){ // 数据库中有该条记录，且V_BBB的值早于当前处理文件的更正标识的值
				// update
				StatDi di = new StatDi();				
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(codeMap.getSod_code());
				di.setDATA_TYPE_1(codeMap.getCts_code());
				di.setTT("");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //1成功，0失败
				di.setPROCESS_STATE("1");  //1成功，0失败
				
				String sql08 =  "update "+codeMap.getValue_table_name()+" set "
						+ "V_BBB=?,D_UPDATE_TIME=?,V71651=?,V71652=?,V71653=?,V71654=?,V71655=?,V71656=? "
						+ "where V01301 = ? and V04001 = ?"
						+ " and V04002 = ? and V04003 = ? and V71001 = ? and V71002 = ? and V71650 = ?";
				
				if(connection != null){	
					try{	
						connection.setAutoCommit(true);
						Pstmt = new LoggableStatement(connection, sql08);
						
						int ii = 1;
						Pstmt.setString(ii++, v_bbb);
						Pstmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr08.get(i).getPlantFreshWeight())));
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr08.get(i).getPlantDryWeight())));
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr08.get(i).getAvgFreshWeightPerSquareMeter())));
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr08.get(i).getAvgDryWeightPerSquareMeter())));
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr08.get(i).getPercentageOfWater())));
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr08.get(i).getPercentageOfGrowth())));
						
						Pstmt.setString(ii++, cr08.get(i).getStationNumberChina());
						Pstmt.setInt(ii++, cr08.get(i).getObservationDate().getYear() + 1900);
						Pstmt.setInt(ii++, cr08.get(i).getObservationDate().getMonth() + 1);
						Pstmt.setInt(ii++, cr08.get(i).getObservationDate().getDate());
						Pstmt.setInt(ii++, cr08.get(i).getCropName());
						Pstmt.setInt(ii++, cr08.get(i).getPeriodOfGrowth());
						Pstmt.setInt(ii++, cr08.get(i).getOrganName());
						
						di.setIIiii(cr08.get(i).getStationNumberChina());
						di.setDATA_TIME(TimeUtil.date2String(cr08.get(i).getObservationDate(), "yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setLONGTITUDE(String.valueOf(cr08.get(i).getLongitude()));
						di.setLATITUDE(String.valueOf(cr08.get(i).getLatitude()));
						di.setDATA_UPDATE_FLAG(v_bbb);
						di.setHEIGHT(cr08.get(i).getHeightOfSationGroundAboveMeanSeaLevel().toString());
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(file.length()));
						try{
							Pstmt.execute();	
							listDi.add(di);	
						}
						catch(SQLException e){							
							di.setPROCESS_STATE("0");//1成功，0失败
							listDi.add(di);		
							loggerBuffer.append("\n filename："+fileN+
									"\n " + cr08.get(i).getStationNumberChina() + " " + sdf.format(cr08.get(i).getObservationDate())
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
	 * 
	 * @Title: EleUpdateCROP09   
	 * @Description: TODO(当文件名带有更正标识时的入库处理)   
	 * @param cr09
	 * @param connection
	 * @param recv_time void      
	 * @param fileN 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static void EleUpdateCROP09(List<ZAgmeCrop09> cr09, java.sql.Connection connection, Date recv_time, String v_bbb, String filepath, StringBuffer loggerBuffer, CTSCodeMap codeMap){
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement Pstmt = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		int i = 0;
		for(i = 0; i < cr09.size(); i ++){
			String vbbbInDB = null;
			vbbbInDB = findVBBBCrop09(cr09.get(i), connection,loggerBuffer, codeMap.getValue_table_name()); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if(vbbbInDB == null){ // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				// insert
				insertOneLine_db_CROP09(cr09.get(i), connection, recv_time,v_bbb,filepath,loggerBuffer, codeMap);
			}
			else if(vbbbInDB.compareTo(v_bbb) < 0){ // 数据库中有该条记录，且V_BBB的值早于当前处理文件的更正标识的值
				// update
				StatDi di = new StatDi();				
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(codeMap.getSod_code());
				di.setDATA_TYPE_1(codeMap.getCts_code());
				di.setTT("");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //1成功，0失败
				di.setPROCESS_STATE("1");  //1成功，0失败
				
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				String sql09 =  "update "+codeMap.getValue_table_name()+" set "
						+ "V_BBB=?,D_UPDATE_TIME=?,V04300_018=?,V71091=? "
						+ "where V01301 = ? and V04001 = ?"
						+ " and V04002 = ? and V04003 = ? and V71001 = ? and V71903 = ?";
				
				if(connection != null){		
					try{
						connection.setAutoCommit(true);
						Pstmt = new LoggableStatement(connection, sql09);
						int ii = 1;
						Pstmt.setString(ii++, v_bbb);
						Pstmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						Pstmt.setBigDecimal(ii++, new BigDecimal(df.format(cr09.get(i).getHarvestTime())));
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr09.get(i).getUnitOuput())));
							
						Pstmt.setString(ii++, cr09.get(i).getStationNumberChina());
						Pstmt.setInt(ii++, cr09.get(i).getSeedingTime().getYear() + 1900);
						Pstmt.setInt(ii++, cr09.get(i).getSeedingTime().getMonth() +1);
						Pstmt.setInt(ii++, cr09.get(i).getSeedingTime().getDate());
						Pstmt.setInt(ii++, cr09.get(i).getCropName());
						Pstmt.setInt(ii++, cr09.get(i).getFieldLevel());
						
						di.setIIiii(cr09.get(i).getStationNumberChina());
						di.setDATA_TIME(TimeUtil.date2String(cr09.get(i).getSeedingTime(), "yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setLONGTITUDE(String.valueOf(cr09.get(i).getLongitude()));
						di.setLATITUDE(String.valueOf(cr09.get(i).getLatitude()));
						di.setDATA_UPDATE_FLAG(v_bbb);
						di.setHEIGHT(cr09.get(i).getHeightOfSationGroundAboveMeanSeaLevel().toString());
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(file.length()));
						try{
							Pstmt.execute();
							listDi.add(di);	
						}
						catch(SQLException e){							
							di.setPROCESS_STATE("0");//1成功，0失败
							listDi.add(di);		
							loggerBuffer.append("\n filename："+fileN+
									"\n " + cr09.get(i).getStationNumberChina() + " " + sdf.format(cr09.get(i).getSeedingTime())
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
	 * 
	 * @param fileN 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @Title: EleUpdateCROP10   
	 * @Description: TODO(当文件名带有更正标识时的入库处理)   
	 * @param: @param cr10
	 * @param: @param recv_time
	 * @param: @return      
	 * @return: DataBaseAction      
	 * @throws:
	 */
	@SuppressWarnings("deprecation")
	private static void EleUpdateCROP10(List<ZAgmeCrop10> cr10, java.sql.Connection connection, Date recv_time, String v_bbb, String filepath, StringBuffer loggerBuffer,CTSCodeMap codeMap){
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement Pstmt = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		int i = 0;
		for(i = 0; i < cr10.size(); i ++){
			String vbbbInDB = null;
			vbbbInDB = findVBBBCrop10(cr10.get(i), connection,loggerBuffer, codeMap.getValue_table_name()); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if(vbbbInDB == null){ // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				// insert
				insertOneLine_db_CROP10(cr10.get(i), connection, recv_time,v_bbb,filepath,loggerBuffer, codeMap);
			}
			else if(vbbbInDB.compareTo(v_bbb) < 0){ // 数据库中有该条记录，且V_BBB的值早于当前处理文件的更正标识的值
				// update
				StatDi di = new StatDi();				
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(codeMap.getSod_code());
				di.setDATA_TYPE_1(codeMap.getCts_code());
				di.setTT("");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //1成功，0失败
				di.setPROCESS_STATE("1");  //1成功，0失败
				
				String sql10 =  "update "+codeMap.getValue_table_name()+" set "
						+ "V_BBB=?,D_UPDATE_TIME=?,V71006=?,V71008=?,V71007=?,V71630_1=?,V71632_1=?,V71630_2=?,V71632_2=?,V71630_3=?,V71632_3=?,V71630_4=?,V71632_4=? "
						+ "where V01301 = ? and V04001 = ?"
						+ " and V04002 = ? and V04003 = ? and V71001 = ? and V71002 = ? and V71903 = ?";
				
				if(connection != null){	
					try{
						connection.setAutoCommit(true);
						Pstmt = new LoggableStatement(connection, sql10);
		
						int ii = 1;
						Pstmt.setString(ii++, v_bbb);
						Pstmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr10.get(i).getPlantHeight())));
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr10.get(i).getPlantDensity())));
						Pstmt.setInt(ii++, cr10.get(i).getGrowthState());
						Pstmt.setInt(ii++, cr10.get(i).getOutputFactorName1());
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr10.get(i).getOutputFactorMeasureValue1())));
						Pstmt.setInt(ii++, cr10.get(i).getOutputFactorName2());
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr10.get(i).getOutputFactorMeasureValue2())));
						Pstmt.setInt(ii++, cr10.get(i).getOutputFactorName3());
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr10.get(i).getOutputFactorMeasureValue3())));
						Pstmt.setInt(ii++, cr10.get(i).getOutputFactorName4());
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr10.get(i).getOutputFactorMeasureValue4())));
							
						Pstmt.setString(ii++, cr10.get(i).getStationNumberChina());
						Pstmt.setInt(ii++, cr10.get(i).getObservationDate().getYear() + 1900);
						Pstmt.setInt(ii++, cr10.get(i).getObservationDate().getMonth() + 1);
						Pstmt.setInt(ii++, cr10.get(i).getObservationDate().getDate());
						Pstmt.setInt(ii++, cr10.get(i).getCropName());
						Pstmt.setInt(ii++, cr10.get(i).getPeriodOfGrowth());
						Pstmt.setInt(ii++, cr10.get(i).getFieldLevel());
						
						di.setIIiii(cr10.get(i).getStationNumberChina());
						di.setDATA_TIME(TimeUtil.date2String(cr10.get(i).getObservationDate(), "yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setLONGTITUDE(String.valueOf(cr10.get(i).getLongitude()));
						di.setLATITUDE(String.valueOf(cr10.get(i).getLatitude()));
						di.setDATA_UPDATE_FLAG(v_bbb);
						di.setHEIGHT(cr10.get(i).getHeightOfSationGroundAboveMeanSeaLevel().toString());
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(file.length()));
						try{
							Pstmt.execute();	
							listDi.add(di);	
						}
						catch(SQLException e){							
							di.setPROCESS_STATE("0");//1成功，0失败
							listDi.add(di);		
							loggerBuffer.append("\n filename："+fileN+
									"\n " + cr10.get(i).getStationNumberChina() + " " + sdf.format(cr10.get(i).getObservationDate())
									+"\n execute sql error："+((LoggableStatement)Pstmt).getQueryString()+"\n "+e.getMessage());
						}				
					}	catch (SQLException e) {
					loggerBuffer.append("\n create Statement error: "+ e.getMessage());
					}finally {
						try {
							if(Pstmt != null)
								Pstmt.close();
						} catch (SQLException e) {
							loggerBuffer.append("\n close Statement error : "+ e.getMessage());
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
	 * 
	 * @Title: findVBBBCrop01   
	 * @Description: TODO(查找VBBB的值)   
	 * @param cr01
	 * @param connection
	 * @param loggerBuffer 
	 * @return String      
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static String findVBBBCrop01(ZAgmeCrop01 cr01, java.sql.Connection connection, StringBuffer loggerBuffer, String tablename){
		
		PreparedStatement Pstmt = null;
		ResultSet resultSet  = null;
		String sql01 =  "select V_BBB from "+tablename+" "
				+ "where V01301 = ? and V04001 = ?"
				+ " and V04002 = ? and V04003 = ? and V71001 = ? and V71002 = ?";
		try{
			
			
			if(connection != null){		
				Pstmt = connection.prepareStatement(sql01);
				int ii = 1;					
				Pstmt.setString(ii++, cr01.getStationNumberChina());
				Pstmt.setInt(ii++, cr01.getGrowthDate().getYear() + 1900);
				Pstmt.setInt(ii++, cr01.getGrowthDate().getMonth() + 1);
				Pstmt.setInt(ii++, cr01.getGrowthDate().getDate());
				Pstmt.setInt(ii++, cr01.getCropName());
				Pstmt.setInt(ii++, cr01.getPeriodOfGrowth());				
				resultSet = Pstmt.executeQuery();		
				if(resultSet.next()){
					return resultSet.getString(1);
				}
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			release(resultSet, Pstmt,loggerBuffer);
		}
		return null;
	}
	/**
	 * 
	 * @Title: findVBBBCrop02   
	 * @Description: TODO(查找VBB的值)   
	 * @param cr02
	 * @param connection
	 * @param loggerBuffer 
	 * @return String      
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static String findVBBBCrop02(ZAgmeCrop02 cr02, java.sql.Connection connection, StringBuffer loggerBuffer, String tablename){
		
		PreparedStatement Pstmt = null;
		ResultSet resultSet = null;
		String sql02 =  "select V_BBB from "+tablename+" "				
				+ "where V01301 = ? and V04001 = ?"
				+ " and V04002 = ? and V04003 = ? and V71001 = ? and V71002 = ?";
		try{
			
			
			if(connection != null){		
				Pstmt = connection.prepareStatement(sql02);
				int ii = 1;
				Pstmt.setString(ii++, cr02.getStationNumberChina());
				Pstmt.setInt(ii++, cr02.getObservationDate().getYear() +1900);
				Pstmt.setInt(ii++, cr02.getObservationDate().getMonth() + 1);
				Pstmt.setInt(ii++, cr02.getObservationDate().getDate());
				Pstmt.setInt(ii++, cr02.getCropName());
				Pstmt.setInt(ii++, cr02.getPeriodOfGrowth());
				resultSet = Pstmt.executeQuery();
				if(resultSet.next()){
					return resultSet.getString(1);
				}
			}	
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			release(resultSet, Pstmt,loggerBuffer);
		}
		return null;
	}
	/**
	 * 
	 * @Title: findVBBBCrop03   
	 * @Description: TODO(查找VBB的值)   
	 * @param cr03
	 * @param connection
	 * @param loggerBuffer 
	 * @return String      
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static String findVBBBCrop03(ZAgmeCrop03 cr03, java.sql.Connection connection, StringBuffer loggerBuffer, String tablename){
		
		PreparedStatement Pstmt = null;
		ResultSet resultSet = null;
		String sql03 =  "select V_BBB from "+tablename+" "				
				+ "where V01301 = ? and V04001 = ?"
				+ " and V04002 = ? and V04003 = ? and V71001 = ?";
		try{
			
			
			if(connection != null){		
				Pstmt = connection.prepareStatement(sql03);
				int ii = 1;
				Pstmt.setString(ii++, cr03.getStationNumberChina());
				Pstmt.setInt(ii++, cr03.getObservationDate().getYear() + 1900);
				Pstmt.setInt(ii++, cr03.getObservationDate().getMonth() + 1);
				Pstmt.setInt(ii++, cr03.getObservationDate().getDate());
				Pstmt.setInt(ii++, cr03.getCropName());	
				resultSet = Pstmt.executeQuery();
				if(resultSet.next())
					return resultSet.getString(1);
			}
		}catch(SQLException e){
		e.printStackTrace();
		}finally {
			release(resultSet, Pstmt,loggerBuffer);
		}
		return null;
	}
	/**
	 * 
	 * @Title: findVBBBCrop04   
	 * @Description: TODO(查找VBB的值)   
	 * @param cr04
	 * @param connection
	 * @param loggerBuffer 
	 * @return String      
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static String findVBBBCrop04(ZAgmeCrop04 cr04, java.sql.Connection connection, StringBuffer loggerBuffer, String tablename){
		
		PreparedStatement Pstmt = null;
		ResultSet resultSet = null;
		String sql04 =  "select V_BBB from "+tablename+" "
				+ "where V01301 = ? and V04001 = ?"
				+ " and V04002 = ? and V04003 = ? and V71001 = ? and V71002 = ? and V71616_01 = ?";
		try{
			
			
			if(connection != null){	
				Pstmt = connection.prepareStatement(sql04);
				int ii = 1;								
				Pstmt.setString(ii++, cr04.getStationNumberChina());
				Pstmt.setInt(ii++, cr04.getObservationDate().getYear() + 1900);
				Pstmt.setInt(ii++, cr04.getObservationDate().getMonth() + 1);
				Pstmt.setInt(ii++, cr04.getObservationDate().getDate());
				Pstmt.setInt(ii++, cr04.getCropName());
				Pstmt.setInt(ii++, cr04.getPeriodOfGrowth());
				Pstmt.setInt(ii++, cr04.getItemName1());
				
				resultSet = Pstmt.executeQuery();
				if(resultSet.next())
					return resultSet.getString(1);
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}finally {
			release(resultSet, Pstmt,loggerBuffer);
		}
		return null;
	}
	/**
	 * 
	 * @Title: findVBBBCrop05   
	 * @Description: TODO(查找VBB的值)   
	 * @param cr05
	 * @param connection
	 * @param loggerBuffer 
	 * @return String      
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static String findVBBBCrop05(ZAgmeCrop05 cr05, java.sql.Connection connection, StringBuffer loggerBuffer, String tablename){
		
		PreparedStatement Pstmt = null;
		ResultSet resultSet = null;
		String sql04 =  "select V_BBB from "+tablename+" "			
				+ "where V01301 = ? and V04001 = ?"
				+ " and V04002 = ? and V04003 = ? and V71001 = ? and V71002 = ? and V71616_01 = ?";
		try{
			
			
			if(connection != null){		
				Pstmt = connection.prepareStatement(sql04);
				int ii = 1;					
				Pstmt.setString(ii++, cr05.getStationNumberChina());
				Pstmt.setInt(ii++, cr05.getObservationDate().getYear() + 1900);
				Pstmt.setInt(ii++, cr05.getObservationDate().getMonth() + 1);
				Pstmt.setInt(ii++, cr05.getObservationDate().getDate());
				Pstmt.setInt(ii++, cr05.getCropName());
				Pstmt.setInt(ii++, noTask);
				Pstmt.setInt(ii++, cr05.getItemName1());
				
				resultSet = Pstmt.executeQuery();
			    if(resultSet.next())
			    	return resultSet.getString(1);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			release(resultSet, Pstmt,loggerBuffer);
		}
		return null;
	}
	/**
	 * 
	 * @Title: findVBBBCrop06   
	 * @Description: TODO(查找VBB的值)   
	 * @param cr06
	 * @param connection
	 * @param loggerBuffer 
	 * @return String      
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static String findVBBBCrop06(ZAgmeCrop06 cr06, java.sql.Connection connection, StringBuffer loggerBuffer, String tablename){
		
		PreparedStatement Pstmt = null;
		ResultSet resultSet = null;
		String sql06 =  "select V_BBB from "+tablename+" "
				+ "where V01301 = ? and V04001 = ?"
				+ " and V04002 = ? and V04003 = ? and V71001 = ? and V71616_02 = ?";
		try{
			
			
			if(connection != null){		
				Pstmt = connection.prepareStatement(sql06);
				int ii = 1;										
				Pstmt.setString(ii++, cr06.getStationNumberChina());
				Pstmt.setInt(ii++, cr06.getStartTime().getYear() +1900);
				Pstmt.setInt(ii++, cr06.getStartTime().getMonth() + 1);
				Pstmt.setInt(ii++, cr06.getStartTime().getDate());
				Pstmt.setInt(ii++, cr06.getCropName());
				Pstmt.setInt(ii++, cr06.getItemName2());
				resultSet = Pstmt.executeQuery();
				if(resultSet.next())
					return resultSet.getString(1);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			release(resultSet, Pstmt,loggerBuffer);
		}
		return null;
	}
	/**
	 * 
	 * @Title: findVBBBCrop07   
	 * @Description: TODO(查找VBB的值)   
	 * @param cr07
	 * @param connection
	 * @param loggerBuffer 
	 * @return String      
	 * @throws：
	 */
	private static String findVBBBCrop07(ZAgmeCrop07 cr07, java.sql.Connection connection, StringBuffer loggerBuffer, String tablename){
		
		PreparedStatement Pstmt = null;
		ResultSet resultSet = null;
		String sql07 =  "select V_BBB from "+tablename+" "				
				+ "where V01301 = ? and V04001 = ?"
				+ " and V71001 = ?";
		try{
			
			
			if(connection != null){			
				Pstmt = connection.prepareStatement(sql07);
				int ii = 1;									
				Pstmt.setString(ii++, cr07.getStationNumberChina());
				Pstmt.setInt(ii++, cr07.getYear());
				Pstmt.setInt(ii++, cr07.getCropName());		
				
				resultSet = Pstmt.executeQuery();
				if(resultSet.next())
					return resultSet.getString(1);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			release(resultSet, Pstmt,loggerBuffer);
		}
		return null;
	}
	/**
	 * 
	 * @Title: findVBBBCrop08   
	 * @Description: TODO(查找VBB的值)   
	 * @param cr08
	 * @param connection
	 * @param loggerBuffer 
	 * @return String      
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static String findVBBBCrop08(ZAgmeCrop08 cr08, java.sql.Connection connection, StringBuffer loggerBuffer, String tablename){
		PreparedStatement Pstmt = null;
		ResultSet resultSet = null;
		String sql08 =  "select V_BBB from "+tablename+" "			
				+ "where V01301 = ? and V04001 = ?"
				+ " and V04002 = ? and V04003 = ? and V71001 = ? and V71002 = ? and V71650 = ?";
		try{
			
			
			if(connection != null){		
				Pstmt = connection.prepareStatement(sql08);
				int ii = 1;					
				Pstmt.setString(ii++, cr08.getStationNumberChina());
				Pstmt.setInt(ii++, cr08.getObservationDate().getYear() + 1900);
				Pstmt.setInt(ii++, cr08.getObservationDate().getMonth() + 1);
				Pstmt.setInt(ii++, cr08.getObservationDate().getDate());
				Pstmt.setInt(ii++, cr08.getCropName());
				Pstmt.setInt(ii++, cr08.getPeriodOfGrowth());
				Pstmt.setInt(ii++, cr08.getOrganName());
					
				resultSet = Pstmt.executeQuery();
				if(resultSet.next())
					return resultSet.getString(1);
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}finally {
			release(resultSet, Pstmt,loggerBuffer);
		}
		return null;
	}
	/**
	 * 
	 * @Title: findVBBBCrop09   
	 * @Description: TODO(查找VBB的值)   
	 * @param cr09
	 * @param connection
	 * @param loggerBuffer 
	 * @return String      
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static String findVBBBCrop09(ZAgmeCrop09 cr09, java.sql.Connection connection, StringBuffer loggerBuffer, String tablename){
		PreparedStatement Pstmt = null;
		ResultSet resultSet = null;
		String sql09 =  "select V_BBB from "+tablename+" "			
				+ "where V01301 = ? and V04001 = ?"
				+ " and V04002 = ? and V04003 = ? and V71001 = ? and V71903 = ?";
		try{
			
			if(connection != null){		
				Pstmt = connection.prepareStatement(sql09);
				int ii = 1;										
				Pstmt.setString(ii++, cr09.getStationNumberChina());
				Pstmt.setInt(ii++, cr09.getSeedingTime().getYear() + 1900);
				Pstmt.setInt(ii++, cr09.getSeedingTime().getMonth() +1);
				Pstmt.setInt(ii++, cr09.getSeedingTime().getDate());
				Pstmt.setInt(ii++, cr09.getCropName());
				Pstmt.setInt(ii++, cr09.getFieldLevel());					
				
				resultSet = Pstmt.executeQuery();
				if(resultSet.next())
					return resultSet.getString(1);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			release(resultSet, Pstmt,loggerBuffer);
		}				
		return null;
	}
	/**
	 * 
	 * @Title: findVBBBCrop10   
	 * @Description: TODO(查找VBB的值)   
	 * @param cr10
	 * @param connection
	 * @param loggerBuffer 
	 * @return String      
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static String findVBBBCrop10(ZAgmeCrop10 cr10, java.sql.Connection connection, StringBuffer loggerBuffer, String tablename){
		PreparedStatement Pstmt = null;
		ResultSet resultSet = null;
		String sql10 =  "select V_BBB from "+tablename+" "
				+ "where V01301 = ? and V04001 = ?"
				+ " and V04002 = ? and V04003 = ? and V71001 = ? and V71002 = ? and V71903 = ?";
		try{
			
			if(connection != null){
				Pstmt = connection.prepareStatement(sql10);
				int ii = 1;									
				Pstmt.setString(ii++, cr10.getStationNumberChina());
				Pstmt.setInt(ii++, cr10.getObservationDate().getYear() + 1900);
				Pstmt.setInt(ii++, cr10.getObservationDate().getMonth() + 1);
				Pstmt.setInt(ii++, cr10.getObservationDate().getDate());
				Pstmt.setInt(ii++, cr10.getCropName());
				Pstmt.setInt(ii++, cr10.getPeriodOfGrowth());
				Pstmt.setInt(ii++, cr10.getFieldLevel());
					
				resultSet = Pstmt.executeQuery();
				if(resultSet.next())
					return resultSet.getString(1);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			release(resultSet, Pstmt,loggerBuffer);
		}
		return null;
	}
	
	/**
	 * 
	 * @param fileN 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @Title: insertOneLine_db_CROP01   
	 * @Description: TODO(单条插入)   
	 * @param: @param cr01
	 * @param: @param recv_time
	 * @param: @return      
	 * @return: DataBaseAction      
	 * @throws:
	 */
	@SuppressWarnings("deprecation")
	private static void insertOneLine_db_CROP01(ZAgmeCrop01 cr01, java.sql.Connection connection, Date recv_time, String v_bbb, String filepath, StringBuffer loggerBuffer, CTSCodeMap codeMap) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		// chy 去掉 D_RECORD_ID
		String sql = "INSERT INTO "+codeMap.getValue_table_name()+"( D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,"
					+ "V01301,V01300,V05001,V06001,V07001,V07031,"
					+ "V_ACODE,V04001,V04002,V04003,"
					+ "V71001,V71002,"
					+ "V04001_03,V04002_03,V04003_03,"
					+ "V71005,V71010,V71007,V71006,V71008, V_BBB, D_UPDATE_TIME,D_SOURCE_ID) "
					+ "VALUES (?,?, ?, ?, ?,"
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, ?,?, ?)";
		if(connection != null){		
			try {	
				connection.setAutoCommit(true);
				pStatement = new LoggableStatement(connection, sql);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");		
				StatDi di = new StatDi();				
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(codeMap.getSod_code());
				di.setDATA_TYPE_1(codeMap.getCts_code());
				di.setTT("");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //1成功，0失败
				di.setPROCESS_STATE("1");  //1成功，0失败	
					
				int stationNumberN = defaultInt;
				String stat = cr01.getStationNumberChina();
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
				pStatement.setString(ii++, sdf.format(cr01.getGrowthDate())+"_"+cr01.getStationNumberChina()+"_"+cr01.getCropName()+"_"+cr01.getPeriodOfGrowth());
				pStatement.setString(ii++, codeMap.getSod_code());
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(cr01.getGrowthDate().getTime()));
				pStatement.setString(ii++, cr01.getStationNumberChina());
				pStatement.setInt(ii++, stationNumberN);
				pStatement.setBigDecimal(ii++, new BigDecimal(cr01.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
				pStatement.setBigDecimal(ii++, new BigDecimal(cr01.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr01.getHeightOfSationGroundAboveMeanSeaLevel())));              
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr01.getHeightOfPressureSensor())));
				pStatement.setString(ii++, adminCode); // V_ACODE		
				pStatement.setInt(ii++, cr01.getGrowthDate().getYear() + 1900);
				pStatement.setInt(ii++, cr01.getGrowthDate().getMonth() + 1);
				pStatement.setInt(ii++, cr01.getGrowthDate().getDate());
				pStatement.setInt(ii++, cr01.getCropName());
				pStatement.setInt(ii++, cr01.getPeriodOfGrowth());
				pStatement.setInt(ii++, cr01.getGrowthDate().getYear() + 1900);
				pStatement.setInt(ii++, cr01.getGrowthDate().getMonth() +1);
				pStatement.setInt(ii++, cr01.getGrowthDate().getDate());
				pStatement.setInt(ii++, cr01.getDevelopAnomaly());
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr01.getPercentageOfGrowthPeriod())));
				pStatement.setInt(ii++, cr01.getGrowthState());
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr01.getPlantHeight())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr01.getPlantDensity())));
				pStatement.setString(ii++, v_bbb);
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setString(ii++, codeMap.getCts_code());
				
				di.setIIiii(cr01.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(cr01.getGrowthDate(), "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				di.setLONGTITUDE(String.valueOf(cr01.getLongitude()));
				di.setLATITUDE(String.valueOf(cr01.getLatitude()));
				di.setDATA_UPDATE_FLAG(v_bbb);
				di.setHEIGHT(cr01.getHeightOfSationGroundAboveMeanSeaLevel().toString());
				di.setSEND_PHYS("DRDS");
				di.setFILE_SIZE(String.valueOf(file.length()));
				try{
					pStatement.execute();
					listDi.add(di);	
				}
				catch(SQLException e){							
					di.setPROCESS_STATE("0");//1成功，0失败
					listDi.add(di);		
					loggerBuffer.append("\n filename："+fileN+
								"\n " + cr01.getStationNumberChina() + " " + sdf.format(cr01.getGrowthDate())
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
	 * 
	 * @param fileN 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @Title: insertOneLine_db_CROP02   
	 * @Description: TODO(单挑插入)   
	 * @param: @param cr02
	 * @param: @param recv_time
	 * @param: @return      
	 * @return: DataBaseAction      
	 * @throws:
	 */
	@SuppressWarnings("deprecation")
	private static void insertOneLine_db_CROP02(ZAgmeCrop02 cr02,java.sql.Connection connection,Date recv_time, String v_bbb, String filepath, StringBuffer loggerBuffer, CTSCodeMap codeMap) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		// chy 去掉D_RECORD_ID
		String sql = "INSERT INTO "+codeMap.getValue_table_name()+"( D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,"
					+ "V01301,V01300,V05001,V06001,V07001,V07031,"
					+ "V_ACODE,V04001,V04002,V04003,"
					+ "V71001,V71002,"
					+ "V71656,V71655,V71604, V_BBB,D_UPDATE_TIME,D_SOURCE_ID) "
					+ "VALUES (?,?, ?, ?, ?,"
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, ?,?, ?)";
		if(connection != null){								
			try {	
				connection.setAutoCommit(true);
				pStatement = new LoggableStatement(connection, sql);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");				
				StatDi di = new StatDi();					
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(codeMap.getSod_code());
				di.setDATA_TYPE_1(codeMap.getCts_code());
				di.setTT("");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //1成功，0失败
				di.setPROCESS_STATE("1");  //1成功，0失败
					
				int stationNumberN = defaultInt;
				String stat = cr02.getStationNumberChina();
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
				pStatement.setString(ii++, sdf.format(cr02.getObservationDate())+"_"+cr02.getStationNumberChina()+"_"+cr02.getCropName()+"_"+cr02.getPeriodOfGrowth());
				pStatement.setString(ii++, codeMap.getSod_code());
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(cr02.getObservationDate().getTime()));
				pStatement.setString(ii++, cr02.getStationNumberChina());
				pStatement.setInt(ii++, stationNumberN);
				pStatement.setBigDecimal(ii++, new BigDecimal(cr02.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
				pStatement.setBigDecimal(ii++, new BigDecimal(cr02.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr02.getHeightOfSationGroundAboveMeanSeaLevel())));              
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr02.getHeightOfPressureSensor())));
				pStatement.setString(ii++, adminCode); // V_ACODE		
				pStatement.setInt(ii++, cr02.getObservationDate().getYear() + 1900 );
				pStatement.setInt(ii++, cr02.getObservationDate().getMonth() + 1);
				pStatement.setInt(ii++, cr02.getObservationDate().getDate());
				pStatement.setInt(ii++, cr02.getCropName());
				pStatement.setInt(ii++, cr02.getPeriodOfGrowth());
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr02.getPercentageOfGrowth())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr02.getPercentageOfWater())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr02.getIndexOfLeafArea())));
				pStatement.setString(ii++, v_bbb);
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setString(ii++, codeMap.getCts_code());	
				
				di.setIIiii(cr02.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(cr02.getObservationDate(), "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());		
				di.setLONGTITUDE(String.valueOf(cr02.getLongitude()));
				di.setLATITUDE(String.valueOf(cr02.getLatitude()));
				di.setDATA_UPDATE_FLAG(v_bbb);
				di.setHEIGHT(cr02.getHeightOfSationGroundAboveMeanSeaLevel().toString());
				di.setSEND_PHYS("DRDS");
				di.setFILE_SIZE(String.valueOf(file.length()));
				try{
					pStatement.execute();
					listDi.add(di);	
				}
				catch(SQLException e){							
					di.setPROCESS_STATE("0");//1成功，0失败
					listDi.add(di);		
					loggerBuffer.append("\n filename："+fileN+
								"\n " + cr02.getStationNumberChina() + " " + sdf.format(cr02.getObservationDate())
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
	 * 
	 * @param fileN 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @Title: insertOneLine_db_CROP03   
	 * @Description: TODO(单条插入)   
	 * @param: @param cr03
	 * @param: @param recv_time
	 * @param: @return      
	 * @return: DataBaseAction      
	 * @throws:
	 */
	@SuppressWarnings("deprecation")
	private static void insertOneLine_db_CROP03(ZAgmeCrop03 cr03,java.sql.Connection connection,Date recv_time, String v_bbb, String filepath, StringBuffer loggerBuffer, CTSCodeMap codeMap) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		// chy 去掉 D_RECORD_ID
		String sql = "INSERT INTO "+codeMap.getValue_table_name()+"( D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,"
					+ "V01301,V01300,V05001,V06001,V07001,V07031,"
					+ "V_ACODE,V04001,V04002,V04003,"
					+ "V71001,V71655,"
					+ "V71089,V71900, V_BBB,D_UPDATE_TIME,D_SOURCE_ID) "
					+ "VALUES (?, ?, ?, ?, ?,"
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?,?,?,?, ?)";
		if(connection != null){	
			try {		
				connection.setAutoCommit(true);
				pStatement = new LoggableStatement(connection, sql);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");				
				StatDi di = new StatDi();					
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(codeMap.getSod_code());
				di.setDATA_TYPE_1(codeMap.getCts_code());
				di.setTT("");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //1成功，0失败
				di.setPROCESS_STATE("1");  //1成功，0失败
					
				int stationNumberN = defaultInt;
				String stat = cr03.getStationNumberChina();
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
				pStatement.setString(ii++, sdf.format(cr03.getObservationDate())+"_"+cr03.getStationNumberChina()+"_"+cr03.getCropName());
				pStatement.setString(ii++, codeMap.getSod_code());
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(cr03.getObservationDate().getTime()));
				pStatement.setString(ii++, cr03.getStationNumberChina());
				pStatement.setInt(ii++, stationNumberN);
				pStatement.setBigDecimal(ii++, new BigDecimal(cr03.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
				pStatement.setBigDecimal(ii++, new BigDecimal(cr03.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr03.getHeightOfSationGroundAboveMeanSeaLevel())));              
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr03.getHeightOfPressureSensor())));
				pStatement.setString(ii++, adminCode); // V_ACODE		
				pStatement.setInt(ii++, cr03.getObservationDate().getYear() + 1900);
				pStatement.setInt(ii++, cr03.getObservationDate().getMonth() + 1);
				pStatement.setInt(ii++, cr03.getObservationDate().getDate());
				pStatement.setInt(ii++, cr03.getCropName());
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr03.getPercentageOfWater())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr03.getDryWeight())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr03.getFillingRate())));
				pStatement.setString(ii++, v_bbb);
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setString(ii++, codeMap.getCts_code());	
				
				di.setIIiii(cr03.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(cr03.getObservationDate(), "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());
				di.setLONGTITUDE(String.valueOf(cr03.getLongitude()));
				di.setLATITUDE(String.valueOf(cr03.getLatitude()));
				di.setDATA_UPDATE_FLAG(v_bbb);
				di.setHEIGHT(cr03.getHeightOfSationGroundAboveMeanSeaLevel().toString());
				di.setSEND_PHYS("DRDS");
				di.setFILE_SIZE(String.valueOf(file.length()));
				try{
					pStatement.execute();
					listDi.add(di);	
				}
				catch(SQLException e){							
					di.setPROCESS_STATE("0");//1成功，0失败
					listDi.add(di);		
					loggerBuffer.append("\n filename："+fileN+
								"\n " + cr03.getStationNumberChina() + " " + sdf.format(cr03.getObservationDate())
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
	 * 
	 * @param fileN 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @Title: insertOneLine_db_CROP04   
	 * @Description: TODO(单条插入)   
	 * @param: @param cr04
	 * @param: @param recv_time
	 * @param: @return      
	 * @return: DataBaseAction      
	 * @throws:
	 */
	@SuppressWarnings("deprecation")
	private static void insertOneLine_db_CROP04(ZAgmeCrop04 cr04, java.sql.Connection connection,Date recv_time, String v_bbb, String filepath, StringBuffer loggerBuffer, CTSCodeMap codeMap) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;		
		// chy 去掉 D_RECORD_ID
		String sql = "INSERT INTO "+codeMap.getValue_table_name()+"( D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,"
					+ "V01301,V01300,V05001,V06001,V07001,V07031,"
					+ "V_ACODE,V04001,V04002,V04003,"
					+ "V71001,V71002,"
					+ "V71616_01,V71632, V_BBB,D_UPDATE_TIME,D_SOURCE_ID) "
					+ "VALUES (?,?, ?, ?, ?,"
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?,?, ?)";
		if(connection != null){							
			try {		
				connection.setAutoCommit(true);
				pStatement = new LoggableStatement(connection, sql);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");				
				StatDi di = new StatDi();					
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(codeMap.getSod_code());
				di.setDATA_TYPE_1(codeMap.getCts_code());
				di.setTT("");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //1成功，0失败
				di.setPROCESS_STATE("1");  //1成功，0失败
					
				int stationNumberN = defaultInt;
				String stat = cr04.getStationNumberChina();
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
				pStatement.setString(ii++, sdf.format(cr04.getObservationDate())+"_"+cr04.getStationNumberChina()+"_"+cr04.getCropName()+"_"+cr04.getPeriodOfGrowth()+"_"+cr04.getItemName1());
				pStatement.setString(ii++, codeMap.getSod_code());
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(cr04.getObservationDate().getTime()));
				pStatement.setString(ii++, cr04.getStationNumberChina());
				pStatement.setInt(ii++, stationNumberN);
				pStatement.setBigDecimal(ii++, new BigDecimal(cr04.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
				pStatement.setBigDecimal(ii++, new BigDecimal(cr04.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr04.getHeightOfSationGroundAboveMeanSeaLevel())));              
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr04.getHeightOfPressureSensor())));
				pStatement.setString(ii++, adminCode); // V_ACODE		
				pStatement.setInt(ii++, cr04.getObservationDate().getYear() + 1900);
				pStatement.setInt(ii++, cr04.getObservationDate().getMonth() + 1);
				pStatement.setInt(ii++, cr04.getObservationDate().getDate());
				pStatement.setInt(ii++, cr04.getCropName());
				pStatement.setInt(ii++, cr04.getPeriodOfGrowth());
				pStatement.setInt(ii++, cr04.getItemName1());
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr04.getObservationValue())));
				pStatement.setString(ii++, v_bbb);
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setString(ii++, codeMap.getCts_code());	
				
				di.setIIiii(cr04.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(cr04.getObservationDate(), "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				di.setLONGTITUDE(String.valueOf(cr04.getLongitude()));
				di.setLATITUDE(String.valueOf(cr04.getLatitude()));
				di.setDATA_UPDATE_FLAG(v_bbb);
				di.setHEIGHT(cr04.getHeightOfSationGroundAboveMeanSeaLevel().toString());
				di.setSEND_PHYS("DRDS");
				di.setFILE_SIZE(String.valueOf(file.length()));
				try{
					pStatement.execute();
					listDi.add(di);	
				}
				catch(SQLException e){							
					di.setPROCESS_STATE("0");//1成功，0失败
					listDi.add(di);		
					loggerBuffer.append("\n filename："+fileN+
								"\n " + cr04.getStationNumberChina() + " " + sdf.format(cr04.getObservationDate())
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
	 * 
	 * @Title: insertOneLine_db_CROP05   
	 * @Description: TODO(单条插入)   
	 * @param cr05
	 * @param connection
	 * @param recv_time void      
	 * @param fileN 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static void insertOneLine_db_CROP05(ZAgmeCrop05 cr05,java.sql.Connection connection,Date recv_time, String v_bbb, String filepath, StringBuffer loggerBuffer, CTSCodeMap codeMap) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		// chy 去掉 D_RECORD_ID
		String sql = "INSERT INTO "+codeMap.getValue_table_name()+"( D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,"
					+ "V01301,V01300,V05001,V06001,V07001,V07031,"
					+ "V_ACODE,V04001,V04002,V04003,"
					+ "V71001,V71002,"
					+ "V71616_01,V71632, V_BBB,D_UPDATE_TIME,D_SOURCE_ID) "
					+ "VALUES (?,?, ?, ?, ?,"
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?,?, ?, ?,?, ?)";
		if(connection != null){	
			try {
				connection.setAutoCommit(true);
				pStatement = new LoggableStatement(connection, sql);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");				
				StatDi di = new StatDi();
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(codeMap.getSod_code());
				di.setDATA_TYPE_1(codeMap.getCts_code());
				di.setTT("");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //1成功，0失败
				di.setPROCESS_STATE("1");  //1成功，0失败
					
				int stationNumberN = defaultInt;
				String stat = cr05.getStationNumberChina();
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
				pStatement.setString(ii++, sdf.format(cr05.getObservationDate())+"_"+cr05.getStationNumberChina()+"_"+cr05.getCropName()+"_"+noTask+"_"+cr05.getItemName1());
				pStatement.setString(ii++, codeMap.getSod_code());
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(cr05.getObservationDate().getTime()));
				pStatement.setString(ii++, cr05.getStationNumberChina());
				pStatement.setInt(ii++, stationNumberN);
				pStatement.setBigDecimal(ii++, new BigDecimal(cr05.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
				pStatement.setBigDecimal(ii++, new BigDecimal(cr05.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr05.getHeightOfSationGroundAboveMeanSeaLevel())));              
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr05.getHeightOfPressureSensor())));
				pStatement.setString(ii++, adminCode); // V_ACODE		
				pStatement.setInt(ii++, cr05.getObservationDate().getYear() + 1900);
				pStatement.setInt(ii++, cr05.getObservationDate().getMonth() + 1);
				pStatement.setInt(ii++, cr05.getObservationDate().getDate());
				pStatement.setInt(ii++, cr05.getCropName());
				pStatement.setInt(ii++, noTask);
				pStatement.setInt(ii++, cr05.getItemName1());
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr05.getObservationValue())));
				pStatement.setString(ii++, v_bbb);
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setString(ii++, codeMap.getCts_code());	
				
				di.setIIiii(cr05.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(cr05.getObservationDate(), "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				di.setLONGTITUDE(String.valueOf(cr05.getLongitude()));
				di.setLATITUDE(String.valueOf(cr05.getLatitude()));
				di.setDATA_UPDATE_FLAG(v_bbb);
				di.setHEIGHT(cr05.getHeightOfSationGroundAboveMeanSeaLevel().toString());
				di.setSEND_PHYS("DRDS");
				di.setFILE_SIZE(String.valueOf(file.length()));
				try{
					pStatement.execute();
					listDi.add(di);	
				}
				catch(SQLException e){							
					di.setPROCESS_STATE("0");//1成功，0失败
					listDi.add(di);		
					loggerBuffer.append("\n filename："+fileN+
								"\n " + cr05.getStationNumberChina() + " " + sdf.format(cr05.getObservationDate())
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
	 * 
	 * @Title: insertOneLine_db_CROP06   
	 * @Description: TODO(单条插入)   
	 * @param cr06
	 * @param connection
	 * @param recv_time void      
	 * @param fileN 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static void insertOneLine_db_CROP06(ZAgmeCrop06 cr06,java.sql.Connection connection,Date recv_time, String v_bbb, String filepath, StringBuffer loggerBuffer, CTSCodeMap codeMap) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		// chy 去掉 D_RECORD_ID
		String sql = "INSERT INTO "+codeMap.getValue_table_name()+"( D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,"
					+ "V01301, V01300,V05001,V06001,V07001,V07031,"
					+ "V_ACODE,V04001,V04002,V04003,"
					+ "V04300_017,V04300_018, V71001,"
					+ "V71616_02,V71901, V71902,V_BBB,D_UPDATE_TIME,D_SOURCE_ID) "
					+ "VALUES (?,?, ?, ?, ?,"
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, ?,?,?, ?)";
					
		if(connection != null){	
			try {
				connection.setAutoCommit(true);
				pStatement = new LoggableStatement(connection, sql);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				StatDi di = new StatDi();					
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(codeMap.getSod_code());
				di.setDATA_TYPE_1(codeMap.getCts_code());
				di.setTT("");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //1成功，0失败
				di.setPROCESS_STATE("1");  //1成功，0失败
					
				int stationNumberN = defaultInt;
				String stat = cr06.getStationNumberChina();
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
				pStatement.setString(ii++, sdf.format(cr06.getStartTime())+"_"+cr06.getStationNumberChina()+"_"+cr06.getCropName()+"_"+cr06.getItemName2());
				pStatement.setString(ii++, codeMap.getSod_code());
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(cr06.getStartTime().getTime()));
				pStatement.setString(ii++, cr06.getStationNumberChina());
				pStatement.setInt(ii++, stationNumberN);
				pStatement.setBigDecimal(ii++, new BigDecimal(cr06.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
				pStatement.setBigDecimal(ii++, new BigDecimal(cr06.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr06.getHeightOfSationGroundAboveMeanSeaLevel())));              
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr06.getHeightOfPressureSensor())));
				pStatement.setString(ii++, adminCode); // V_ACODE		
				pStatement.setInt(ii++, cr06.getStartTime().getYear() + 1900);
				pStatement.setInt(ii++, cr06.getStartTime().getMonth() + 1);
				pStatement.setInt(ii++, cr06.getStartTime().getDate());
				pStatement.setBigDecimal(ii++, new BigDecimal(df.format(cr06.getStartTime())));
				pStatement.setBigDecimal(ii++, new BigDecimal(df.format(cr06.getEndTime())));
				pStatement.setInt(ii++, cr06.getCropName());
				pStatement.setInt(ii++, cr06.getItemName2());
				pStatement.setInt(ii++, cr06.getQuality());
				
				try {
					pStatement.setString(ii++, new String(cr06.getMethodAndTool().getBytes(), "UTF8"));
				} catch (UnsupportedEncodingException e1) {
					pStatement.setString(ii++, "999999");
					loggerBuffer.append("getMethodAndTool() formate error: "+ e1.getMessage());
				}
				
				pStatement.setString(ii++, v_bbb);
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setString(ii++, codeMap.getCts_code());	
				
				di.setIIiii(cr06.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(cr06.getStartTime(), "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				di.setLONGTITUDE(String.valueOf(cr06.getLongitude()));
				di.setLATITUDE(String.valueOf(cr06.getLatitude()));
				di.setDATA_UPDATE_FLAG(v_bbb);
				di.setHEIGHT(cr06.getHeightOfSationGroundAboveMeanSeaLevel().toString());
				di.setSEND_PHYS("DRDS");
				di.setFILE_SIZE(String.valueOf(file.length()));
				try{
					pStatement.execute();
					listDi.add(di);	
				}
				catch(SQLException e){							
					di.setPROCESS_STATE("0");//1成功，0失败
					listDi.add(di);		
					loggerBuffer.append("\n filename："+fileN+
								"\n " + cr06.getStationNumberChina() + " " + sdf.format(cr06.getStartTime())
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
	 * 
	 * @Title: insertOneLine_db_CROP07   
	 * @Description: TODO(单条插入)   
	 * @param cr07
	 * @param connection
	 * @param recv_time void      
	 * @param fileN 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @throws：
	 */
	private static void insertOneLine_db_CROP07(ZAgmeCrop07 cr07,java.sql.Connection connection,Date recv_time, String v_bbb, String filepath, StringBuffer loggerBuffer, CTSCodeMap codeMap) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		// chy 去掉 D_RECORD_ID
		String sql = "INSERT INTO "+codeMap.getValue_table_name()+"( D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,"
					+ "V01301,V01300,V05001,V06001,V07001,V07031,"
					+ "V_ACODE,V04001,"
					+ "V71001,"
					+ "V71601,V71091, V71083,V_BBB,D_UPDATE_TIME,D_SOURCE_ID) "
					+ "VALUES (?, ?, ?, ?, ?,"
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?,?,?, ?)";
			
		if(connection != null){
			try {
				connection.setAutoCommit(true);
				pStatement = new LoggableStatement(connection, sql);
				StatDi di = new StatDi();					
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(codeMap.getSod_code());
				di.setDATA_TYPE_1(codeMap.getCts_code());
				di.setTT("");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //1成功，0失败
				di.setPROCESS_STATE("1");  //1成功，0失败
					
				int stationNumberN = defaultInt;
				String stat = cr07.getStationNumberChina();
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
				SimpleDateFormat yearsdf = new SimpleDateFormat("yyyyMMddHHmmss");
				pStatement.setString(ii++, cr07.getYear()+"0101000000"+"_"+cr07.getStationNumberChina()+"_"+cr07.getCropName());
				pStatement.setString(ii++, codeMap.getSod_code());
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(yearsdf.parse(cr07.getYear()+"0101000000").getTime()));
				pStatement.setString(ii++, cr07.getStationNumberChina());
				pStatement.setInt(ii++, stationNumberN);
				pStatement.setBigDecimal(ii++, new BigDecimal(cr07.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
				pStatement.setBigDecimal(ii++, new BigDecimal(cr07.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr07.getHeightOfSationGroundAboveMeanSeaLevel())));              
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr07.getHeightOfPressureSensor())));
				pStatement.setString(ii++, adminCode); // V_ACODE		
				pStatement.setInt(ii++, cr07.getYear());
				pStatement.setInt(ii++, cr07.getCropName());
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr07.getOutputLevelOfStation())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr07.getCountyAverageOutput())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr07.getCountyOutputChangeRate())));
				pStatement.setString(ii++, v_bbb);
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setString(ii++, codeMap.getCts_code());	
				
				di.setIIiii(cr07.getStationNumberChina());
				di.setDATA_TIME(String.valueOf(TimeUtil.date2String(TimeUtil.String2Date(cr07.getYear() + "0101000000", "yyyyMMddHHmmss"), "yyyy-MM-dd HH:mm")));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());
				di.setLONGTITUDE(String.valueOf(cr07.getLongitude()));
				di.setLATITUDE(String.valueOf(cr07.getLatitude()));
				di.setDATA_UPDATE_FLAG(v_bbb);
				di.setHEIGHT(cr07.getHeightOfSationGroundAboveMeanSeaLevel().toString());
				di.setSEND_PHYS("DRDS");
				di.setFILE_SIZE(String.valueOf(file.length()));
				try{
					pStatement.execute();
					listDi.add(di);	
				}
				catch(SQLException e){							
					di.setPROCESS_STATE("0");//1成功，0失败
					listDi.add(di);		
					loggerBuffer.append("\n filename："+fileN+
								"\n " + cr07.getStationNumberChina() + " " + cr07.getYear()
								+"\n execute sql error："+((LoggableStatement)pStatement).getQueryString()+"\n "+e.getMessage());
				}				
			}catch (SQLException | ParseException e) {
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
	 * 
	 * @Title: insertOneLine_db_CROP08   
	 * @Description: TODO(单条插入)   
	 * @param cr08
	 * @param connection
	 * @param recv_time void      
	 * @param fileN 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static void insertOneLine_db_CROP08(ZAgmeCrop08 cr08,java.sql.Connection connection,Date recv_time, String v_bbb, String filepath, StringBuffer loggerBuffer, CTSCodeMap codeMap) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		// chy 去掉 D_RECORD_ID
		String sql = "INSERT INTO "+codeMap.getValue_table_name()+"( D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,"
					+ "V01301,V01300,V05001,V06001,V07001,V07031,"
					+ "V_ACODE,V04001,V04002,V04003,V04004,V04005,V04006,"
					+ "V71001,V71002,V71650,V71651,V71652,"
					+ "V71653,V71654, V71655,V71656,V_BBB,D_UPDATE_TIME,D_SOURCE_ID) "
					+ "VALUES (?, ?, ?, ?, ?,"
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, ?,?,?,?, ?)";
		if(connection != null){	
			try {
				connection.setAutoCommit(true);
				pStatement = new LoggableStatement(connection, sql);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");				
				StatDi di = new StatDi();					
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(codeMap.getSod_code());
				di.setDATA_TYPE_1(codeMap.getCts_code());
				di.setTT("");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //1成功，0失败
				di.setPROCESS_STATE("1");  //1成功，0失败
					
				int stationNumberN = defaultInt;
				String stat = cr08.getStationNumberChina();
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
				pStatement.setString(ii++, sdf.format(cr08.getObservationDate())+"_"+cr08.getStationNumberChina()+"_"+cr08.getCropName()+"_"+cr08.getPeriodOfGrowth()+"_"+cr08.getOrganName());
				pStatement.setString(ii++, codeMap.getSod_code());
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(cr08.getObservationDate().getTime()));
				pStatement.setString(ii++, cr08.getStationNumberChina());
				pStatement.setInt(ii++, stationNumberN);
				pStatement.setBigDecimal(ii++, new BigDecimal(cr08.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
				pStatement.setBigDecimal(ii++, new BigDecimal(cr08.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr08.getHeightOfSationGroundAboveMeanSeaLevel())));              
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr08.getHeightOfPressureSensor())));
				pStatement.setString(ii++, adminCode); // V_ACODE
				pStatement.setInt(ii++, cr08.getObservationDate().getYear() + 1900);
				pStatement.setInt(ii++, cr08.getObservationDate().getMonth() + 1);
				pStatement.setInt(ii++, cr08.getObservationDate().getDate());
				pStatement.setInt(ii++, cr08.getObservationDate().getHours());
				pStatement.setInt(ii++, cr08.getObservationDate().getMinutes());
				pStatement.setInt(ii++, cr08.getObservationDate().getSeconds());
				pStatement.setInt(ii++, cr08.getCropName());
				pStatement.setInt(ii++, cr08.getPeriodOfGrowth());
				pStatement.setInt(ii++, cr08.getOrganName());
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr08.getPlantFreshWeight())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr08.getPlantDryWeight())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr08.getAvgFreshWeightPerSquareMeter())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr08.getAvgDryWeightPerSquareMeter())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr08.getPercentageOfWater())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr08.getPercentageOfGrowth())));
				pStatement.setString(ii++, v_bbb);
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setString(ii++, codeMap.getCts_code());	
				
				di.setIIiii(cr08.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(cr08.getObservationDate(), "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());
				di.setLONGTITUDE(String.valueOf(cr08.getLongitude()));
				di.setLATITUDE(String.valueOf(cr08.getLatitude()));
				di.setDATA_UPDATE_FLAG(v_bbb);
				di.setHEIGHT(cr08.getHeightOfSationGroundAboveMeanSeaLevel().toString());
				di.setSEND_PHYS("DRDS");
				di.setFILE_SIZE(String.valueOf(file.length()));
				try{
					pStatement.execute();
					listDi.add(di);	
				}
				catch(SQLException e){							
					di.setPROCESS_STATE("0");//1成功，0失败
					listDi.add(di);		
					loggerBuffer.append("\n filename："+fileN+
								"\n " + cr08.getStationNumberChina() + " " + sdf.format(cr08.getObservationDate())
								+"\n execute sql error："+((LoggableStatement)pStatement).getQueryString()+"\n "+e.getMessage());
				}				
			}catch (SQLException e) {
				loggerBuffer.append("\n  create Statement error"+e.getMessage());
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
	 * 
	 * @Title: insertOneLine_db_CROP09   
	 * @Description: TODO(单条插入)   
	 * @param cr09
	 * @param connection
	 * @param recv_time void      
	 * @param fileN 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static void insertOneLine_db_CROP09(ZAgmeCrop09 cr09,java.sql.Connection connection,Date recv_time, String v_bbb, String filepath, StringBuffer loggerBuffer, CTSCodeMap codeMap) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		// chy 去掉 D_RECORD_ID
		String sql = "INSERT INTO "+codeMap.getValue_table_name()+"( D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,"
					+ "V01301,V01300,V05001,V06001,V07001,V07031,"
					+ "V_ACODE,V04001,V04002,V04003,"
					+ "V71001,V71903,V04300_017,V04300_018,"
					+ "V71091, V_BBB,D_UPDATE_TIME,D_SOURCE_ID) "
					+ "VALUES (?, ?, ?, ?, ?,"
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, ?,?, ?)";
					
		if(connection != null){				
			try {
				connection.setAutoCommit(true);
				pStatement = new LoggableStatement(connection, sql);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");			
				StatDi di = new StatDi();					
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(codeMap.getSod_code());
				di.setDATA_TYPE_1(codeMap.getCts_code());
				di.setTT("");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //1成功，0失败
				di.setPROCESS_STATE("1");  //1成功，0失败
				
				int stationNumberN = defaultInt;
				String stat = cr09.getStationNumberChina();
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
				pStatement.setString(ii++, sdf.format(cr09.getSeedingTime())+"_"+cr09.getStationNumberChina()+"_"+cr09.getCropName()+"_"+cr09.getFieldLevel());
				pStatement.setString(ii++, codeMap.getSod_code());
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(cr09.getSeedingTime().getTime()));
				pStatement.setString(ii++, cr09.getStationNumberChina());
				pStatement.setInt(ii++, stationNumberN);
				pStatement.setBigDecimal(ii++, new BigDecimal(cr09.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
				pStatement.setBigDecimal(ii++, new BigDecimal(cr09.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr09.getHeightOfSationGroundAboveMeanSeaLevel())));              
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr09.getHeightOfPressureSensor())));
				pStatement.setString(ii++, adminCode); // V_ACODE		
				pStatement.setInt(ii++, cr09.getSeedingTime().getYear() + 1900);
				pStatement.setInt(ii++, cr09.getSeedingTime().getMonth() + 1);
				pStatement.setInt(ii++, cr09.getSeedingTime().getDate());
				pStatement.setInt(ii++, cr09.getCropName());
				pStatement.setInt(ii++, cr09.getFieldLevel());
				pStatement.setBigDecimal(ii++, new BigDecimal(sdf.format(cr09.getSeedingTime())));
				pStatement.setBigDecimal(ii++, new BigDecimal(sdf.format(cr09.getHarvestTime())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr09.getUnitOuput())));
				pStatement.setString(ii++, v_bbb);
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setString(ii++, codeMap.getCts_code());
				
				di.setIIiii(cr09.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(cr09.getSeedingTime(), "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());		
				di.setLONGTITUDE(String.valueOf(cr09.getLongitude()));
				di.setLATITUDE(String.valueOf(cr09.getLatitude()));
				di.setDATA_UPDATE_FLAG(v_bbb);
				di.setHEIGHT(cr09.getHeightOfSationGroundAboveMeanSeaLevel().toString());
				di.setSEND_PHYS("DRDS");
				di.setFILE_SIZE(String.valueOf(file.length()));
				try{
					pStatement.execute();
					listDi.add(di);	
				}
				catch(SQLException e){							
					di.setPROCESS_STATE("0");//1成功，0失败
					listDi.add(di);		
					loggerBuffer.append("\n filename："+fileN+
								"\n " + cr09.getStationNumberChina() + " " + sdf.format(cr09.getSeedingTime())
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
	 * 
	 * @Title: insertOneLine_db_CROP10   
	 * @Description: TODO(单条插入)   
	 * @param cr10
	 * @param connection
	 * @param recv_time void      
	 * @param fileN 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static void insertOneLine_db_CROP10(ZAgmeCrop10 cr10,java.sql.Connection connection,Date recv_time, String v_bbb, String filepath, StringBuffer loggerBuffer, CTSCodeMap codeMap) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		// chy 去掉 D_RECORD_ID
		String sql = "INSERT INTO "+codeMap.getValue_table_name()+"( D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,"
					+ "V01301,V01300,V05001,V06001,V07001,V07031,"
					+ "V_ACODE,V04001,V04002,V04003,V04004,V04005,V04006,"
					+ "V71903,V71001,V71002,V71006,V71008,V71007,"
					+ "V71630_1, V71632_1,"
					+ "V71630_2, V71632_2,"
					+ "V71630_3, V71632_3,"
					+ "V71630_4, V71632_4, V_BBB,D_UPDATE_TIME,D_SOURCE_ID) "
					+ "VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?,?, ?, ?, ?, ?, "
					+ " ?, ?, ?,?, ?)";
		if(connection != null){			
			try {
				connection.setAutoCommit(true);
				pStatement = new LoggableStatement(connection, sql);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");				
				StatDi di = new StatDi();					
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(codeMap.getSod_code());
				di.setDATA_TYPE_1(codeMap.getCts_code());
				di.setTT("");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //1成功，0失败
				di.setPROCESS_STATE("1");  //1成功，0失败
					
				int stationNumberN = defaultInt;
				String stat = cr10.getStationNumberChina();
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
				pStatement.setString(ii++, sdf.format(cr10.getObservationDate())+"_"+cr10.getStationNumberChina()+"_"+cr10.getFieldLevel()+"_"+cr10.getCropName()+"_"+cr10.getPeriodOfGrowth());
				pStatement.setString(ii++, codeMap.getSod_code());
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(cr10.getObservationDate().getTime()));
				pStatement.setString(ii++, cr10.getStationNumberChina());
				pStatement.setInt(ii++, stationNumberN);
				pStatement.setBigDecimal(ii++, new BigDecimal(cr10.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
				pStatement.setBigDecimal(ii++, new BigDecimal(cr10.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr10.getHeightOfSationGroundAboveMeanSeaLevel())));              
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr10.getHeightOfPressureSensor())));
				pStatement.setString(ii++, adminCode); // V_ACODE		
				pStatement.setInt(ii++, cr10.getObservationDate().getYear() + 1900);
				pStatement.setInt(ii++, cr10.getObservationDate().getMonth() + 1);
				pStatement.setInt(ii++, cr10.getObservationDate().getDate());
				pStatement.setInt(ii++, cr10.getObservationDate().getHours());
				pStatement.setInt(ii++, cr10.getObservationDate().getMinutes());
				pStatement.setInt(ii++, cr10.getObservationDate().getSeconds());	
				pStatement.setInt(ii++, cr10.getFieldLevel());
				pStatement.setInt(ii++, cr10.getCropName());
				pStatement.setInt(ii++, cr10.getPeriodOfGrowth());
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr10.getPlantHeight())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr10.getPlantDensity())));
				pStatement.setInt(ii++, cr10.getGrowthState());
				pStatement.setInt(ii++, cr10.getOutputFactorName1());
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr10.getOutputFactorMeasureValue1())));				
				pStatement.setInt(ii++, cr10.getOutputFactorName2());
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr10.getOutputFactorMeasureValue2())));
				pStatement.setInt(ii++, cr10.getOutputFactorName3());
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr10.getOutputFactorMeasureValue3())));
				pStatement.setInt(ii++, cr10.getOutputFactorName4());
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(cr10.getOutputFactorMeasureValue4())));
				pStatement.setString(ii++, v_bbb);
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setString(ii++, codeMap.getCts_code());
				
				di.setIIiii(cr10.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(cr10.getObservationDate(), "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());		
				di.setLONGTITUDE(String.valueOf(cr10.getLongitude()));
				di.setLATITUDE(String.valueOf(cr10.getLatitude()));
				di.setDATA_UPDATE_FLAG(v_bbb);
				di.setHEIGHT(cr10.getHeightOfSationGroundAboveMeanSeaLevel().toString());
				di.setSEND_PHYS("DRDS");
				di.setFILE_SIZE(String.valueOf(file.length()));
				try{
					pStatement.execute();
					listDi.add(di);	
				}
				catch(SQLException e){							
					di.setPROCESS_STATE("0");//1成功，0失败
					listDi.add(di);		
					loggerBuffer.append("\n filename："+fileN+
								"\n " + cr10.getStationNumberChina() + " " + sdf.format(cr10.getObservationDate())
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
	
	public static void release(ResultSet resultSet, PreparedStatement pstmt, StringBuffer loggerBuffer) {
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
				loggerBuffer.append("\n  close PreparedStatement error");
			}
		}
	}
	public static void main(String[] args) {
		String filepath = "D:\\TEMP\\E.3.1.1\\Z_AGME_I_53789_20190718000000_O_CROP.txt";
		File file = new File(filepath);
		String fileN = file.getName();
		 boolean isRevised = false;
		 String v_bbb = "000";
		 StringBuffer loggerBuffer = new StringBuffer();
		Date recv_time = new Date(file.lastModified());
		// 判断文件是否为更正报
		if(fileN.contains("-CC")){
			isRevised = true;
			v_bbb = fileN.substring(fileN.indexOf("-CC") + 1, fileN.indexOf("-CC") + 4);
		}
		else{
			isRevised = false;
			v_bbb = "000";
		}
		String ctS = StartConfig.ctsCode();
		String sodSs[] = StartConfig.sodCodes();
		String reportSods[] = StartConfig.reportSodCodes();
		String valuetables[] = StartConfig.valueTables();
		String reportTable = StartConfig.reportTable();
		ArrayList<CTSCodeMap> ctsCodeMaps = new ArrayList<>();
		for(int i = 0; i < valuetables.length; i ++){
			CTSCodeMap codeMap = new CTSCodeMap();
			codeMap.setCts_code(ctS);
			codeMap.setSod_code(sodSs[i]);
			codeMap.setReport_sod_code(reportSods[i]);
			codeMap.setValue_table_name(valuetables[i]);
			codeMap.setReport_table_name(reportTable);
			ctsCodeMaps.add(codeMap);
		}
		DecodeCrop decodeCrop = new DecodeCrop();
		ParseResult<ZAgmeCrop> parseResult = decodeCrop.decodeFile(file);
		if(parseResult.isSuccess()) {
		DataBaseAction	action = DbService.processSuccessReport(parseResult, recv_time, filepath,v_bbb,isRevised,loggerBuffer,ctsCodeMaps);

		}
	}
}
