 package cma.cimiss2.dpc.indb.agme.dc_xml_agme.service;
import java.io.ByteArrayInputStream;
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
import java.util.regex.Pattern;
import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.config.ClientConn;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCropAuto;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;


public class DbService {
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code=StartConfig.sodCode_pre();
	public static String sod_device =StartConfig.sodCode_mul();
	
	public static String V_TT = "AGME";
	public static int defaultInt = 999999; 
//	static Map<String, Object> proMap = StationInfo.getProMap();
	
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}
	/**
	 * 
	 * @param loggerBuffer 
	 * @param fileN 
	 * @Title: processSuccessReport   
	 * @Description: TODO(这里用一句话描述这个方法的作用)   
	 * @param: @param parseResult
	 * @param: @param filepath
	 * @param: @param recv_time
	 * @param: @return      
	 * @return: DataBaseAction      
	 * @throws：
	 */
	public static DataBaseAction processSuccessReport(ParseResult<ZAgmeCropAuto> parseResult, Date recv_time, StringBuffer loggerBuffer, String filepath) {
		java.sql.Connection connection = null;
		java.sql.Connection reportConnection = null;
		try {
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			List<ZAgmeCropAuto> zAgmeCropAutos = parseResult.getData();
			insertCropAuto(zAgmeCropAutos, connection, recv_time, loggerBuffer,filepath);
			insertCropAutoDevice(zAgmeCropAutos, connection, recv_time, loggerBuffer,filepath);
		//	@SuppressWarnings("rawtypes")
		//	List<ReportInfo> reportInfos = parseResult.getReports();
		//	reportConnection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
		//	reportInfoToDb(reportInfos, connection, recv_time);  
			return DataBaseAction.SUCCESS;
		} catch (Exception e) {
			loggerBuffer.append("\n Database connection error");
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
	 * 
	 * @param loggerBuffer 
	 * @param fileN 
	 * @Title: insertCropAuto   
	 * @Description: TODO(农作物自动观测资料入库)   
	 * @param: @param zAgmeCropAutos
	 * @param: @param connection
	 * @param: @param recv_time      
	 * @return: void      
	 * @throws：
	 */
	private static void insertCropAuto(List<ZAgmeCropAuto> zAgmeCropAutos, java.sql.Connection connection, Date recv_time, StringBuffer loggerBuffer, String filepath) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		// chy 去掉 D_record_id
		String sql = "INSERT INTO "+StartConfig.keyTable()+" (D_record_id,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,D_UPDATE_TIME,"
			+ "V01301,V01300,V05001,V06001,V07030,V07031,V_ACODE,V04001,V04002,V04003,"
			+ "V04004,V07256,V02271,V48002,V02256,V02259,V02270,V02257,V02258,V02260,"
			+ "V48000,V48001,Q48001,V26256,Q26256,V26257,Q26257,V48009,Q48009,V48008,"
			+ "Q48008,V48010,Q48010,V48034,Q48034,V48007,Q48007,V48301,Q48301,V48006,"
			+ "Q48006,V51000,V51009,Q51002,V26269,Q26269,V26270,Q26270,V30256,"
			+ "D_STORAGE_SITE,"
			//+ "V48033,"
			+ "V35024,V33256,V_BBB, D_SOURCE_ID) "
			+ "VALUES (?,?, ?, ?, ?, ?,"
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?) " ;
		List<ZAgmeCropAuto> zAgmeCropAutos2 = new ArrayList<>();
		if(connection != null){		
			try {	
				pStatement = new LoggableStatement(connection, sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				List<String> sqls = new ArrayList<>();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");		
			
				for(int idx = 0; idx < zAgmeCropAutos.size(); idx ++){
					ZAgmeCropAuto zAgmeCropAuto = zAgmeCropAutos.get(idx);
					if(zAgmeCropAuto.getCorrectMarker() == null || zAgmeCropAuto.getCorrectMarker().isEmpty())
						zAgmeCropAuto.setCorrectMarker("000");
					if(zAgmeCropAuto.getCorrectMarker().equals("000") || findVBBCrop(zAgmeCropAuto, connection,loggerBuffer) == null){
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
						
						String stat = zAgmeCropAuto.getStationNumberChina();
						int adminCode = defaultInt;
						int stationNumberN = defaultInt;
						if (Pattern.matches("\\d{5}", stat))
							stationNumberN = Integer.parseInt(stat);
						else
							stationNumberN = Integer.parseInt(StationCodeUtil.stringToAscii(stat));
						
						String info = (String) proMap.get(stat + "+12");
						if(info != null) {
							String[] infos = info.split(",");
							if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
								adminCode = Integer.parseInt(infos[5]);
						}
						
						if(adminCode == 999999){
							info = (String) proMap.get(stat + "+01");
							if(info == null) {
								loggerBuffer.append("\n In the configuration file, the station number does not exist" + stat);
							}else {
								String[] infos = info.split(",");
								if(infos.length >= 6)
									adminCode = Integer.parseInt(infos[5]);
							}
						}
						int ii = 1;
						pStatement.setString(ii++, sdf.format(zAgmeCropAuto.getObservationTime())+"_"+stat+"_"+zAgmeCropAuto.getCropName()+"_"+zAgmeCropAuto.getCropGrowthPeriod() + "_" + zAgmeCropAuto.getImageSensorID());
						pStatement.setString(ii++, sod_code);
						pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
						pStatement.setTimestamp(ii++, new Timestamp(zAgmeCropAuto.getObservationTime().getTime()));
						pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						//"V01301,V01300,V05001,V06001,V07001,V07031,V_ACODE,V04001,V04002,V04003,"
						pStatement.setString(ii++, stat);
						pStatement.setInt(ii++, stationNumberN);
						pStatement.setBigDecimal(ii++, new BigDecimal(zAgmeCropAuto.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
						pStatement.setBigDecimal(ii++, new BigDecimal(zAgmeCropAuto.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(zAgmeCropAuto.getHeightOfStaionAboveSeaLevel())));
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(zAgmeCropAuto.getHeightOfPressureSensor())));
						pStatement.setInt(ii++, adminCode);
						pStatement.setInt(ii++, zAgmeCropAuto.getObservationTime().getYear() + 1900);
						pStatement.setInt(ii++, zAgmeCropAuto.getObservationTime().getMonth() + 1);
						pStatement.setInt(ii++, zAgmeCropAuto.getObservationTime().getDate() );
						
						// "V04004,V07256,V02271,V48002,V02256,V02259,V02270,V02257,V02258,V02260,"
						pStatement.setInt(ii++, zAgmeCropAuto.getObservationTime().getHours());
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(zAgmeCropAuto.getImageSensorHeightAboveGround())));
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(zAgmeCropAuto.getImageSensorFocus())));
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(zAgmeCropAuto.getObvArea())));
						pStatement.setInt(ii++, zAgmeCropAuto.getObvMethod());
						pStatement.setString(ii++, zAgmeCropAuto.getImageSensorID());
						pStatement.setInt(ii++, zAgmeCropAuto.getImageSensorConnectionStatus());
						pStatement.setInt(ii++, zAgmeCropAuto.getGPSTimingMarker());
						pStatement.setInt(ii++, zAgmeCropAuto.getCFPeripheralStorageMarker());
						pStatement.setString(ii++, zAgmeCropAuto.getAgmeReportSoftwareVersion());
						
						// "V48000,V48001,Q48001,V26256,Q26256,V26257,Q26257,V48009,Q48009,V48008,"
						pStatement.setInt(ii++, zAgmeCropAuto.getCropName());
						pStatement.setInt(ii++, zAgmeCropAuto.getCropGrowthPeriod());
						pStatement.setInt(ii++, zAgmeCropAuto.getCropGrowthPeriodQC());
						pStatement.setString(ii++, zAgmeCropAuto.getGrowthStarttime());
						pStatement.setInt(ii++, zAgmeCropAuto.getGrowthStarttimeQC());
						pStatement.setInt(ii++, zAgmeCropAuto.getGrowthDuration());
						pStatement.setInt(ii++, zAgmeCropAuto.getGrowthDurationQC());
						pStatement.setInt(ii++, zAgmeCropAuto.getGrowthPeriodPercent());
						pStatement.setInt(ii++, zAgmeCropAuto.getGrowthPeriodPercentQC());
						pStatement.setInt(ii++, zAgmeCropAuto.getPlantCoverage());
						
						// "Q48008,V48010,Q48010,V48034,Q48034,V48007,Q48007,V48301,Q48301,V48006,"
						pStatement.setInt(ii++, zAgmeCropAuto.getPlantCoverageQC());
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(zAgmeCropAuto.getLeafAreaIndex())));
						pStatement.setInt(ii++, zAgmeCropAuto.getLeafAreaIndexQC());
						pStatement.setInt(ii++, zAgmeCropAuto.getCanopyHeight());
						pStatement.setInt(ii++, zAgmeCropAuto.getCanopyHeightQC());
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(zAgmeCropAuto.getPlantDensity())));
						pStatement.setInt(ii++, zAgmeCropAuto.getPlantDensityQC());
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(zAgmeCropAuto.getDryWeight())));
						pStatement.setInt(ii++, zAgmeCropAuto.getDryWeightQC());
						pStatement.setInt(ii++, zAgmeCropAuto.getGrowthState());
						
						// "Q48006,V51000,V51009,Q51002,V26269,Q26269,V26270,Q26270,V30256,V48033,"
						pStatement.setInt(ii++, zAgmeCropAuto.getGrowthStateQC());
						pStatement.setInt(ii++, zAgmeCropAuto.getDisaName());
						pStatement.setInt(ii++, zAgmeCropAuto.getDisaLevel());
						pStatement.setInt(ii++, zAgmeCropAuto.getDisaLevelQC());
						pStatement.setString(ii++, zAgmeCropAuto.getDisaStarttime());
						pStatement.setInt(ii++, zAgmeCropAuto.getDisaStarttimeQC());
						pStatement.setInt(ii++, zAgmeCropAuto.getDisaDuration());
						pStatement.setInt(ii++, zAgmeCropAuto.getDisaDurationQC());
						pStatement.setString(ii++, zAgmeCropAuto.getCropImageFormat());
//						pStatement.setBytes(ii++, zAgmeCropAuto.getCropImage());
						
						if(zAgmeCropAuto.getCropImage() != null && zAgmeCropAuto.getCropImage().length > 0){
							String dtString = new SimpleDateFormat("yyyyMMddHHmmss").format(zAgmeCropAuto.getObservationTime());
							//AGME_I_IIIII_yyyymmddHHMMSS_序号.jpg 注：序号从1开始，多张图片依次编码
							String path = dtString.substring(0, 4) + 
									"/" + dtString.substring(0, 8) + 
									"/" + zAgmeCropAuto.getStationNumberChina() + 
									"/AGME_I_" + zAgmeCropAuto.getStationNumberChina()+ "_" + dtString + "_" + zAgmeCropAuto.getImageSensorID() + ".jpg";
									
							String https = ClientConn.savePics(zAgmeCropAuto.getCropImage(), path);
							loggerBuffer.append(https + "\n");
							
							pStatement.setString(ii++, https);
						}
						else 
							pStatement.setString(ii ++, "");
						
//						if(zAgmeCropAuto.getCropImage() != null && zAgmeCropAuto.getCropImage().length > 0)
//							pStatement.setBinaryStream(ii++, new ByteArrayInputStream(zAgmeCropAuto.getCropImage()));
//						else {
//							pStatement.setBinaryStream(ii ++, null);
//						}
						// "V35024,V33256) 
						pStatement.setString(ii++, zAgmeCropAuto.getCorrectMarker());
						pStatement.setInt(ii++, zAgmeCropAuto.getQualityControl());
						pStatement.setString(ii++, zAgmeCropAuto.getCorrectMarker());
						pStatement.setString(ii++, cts_code);
						
						di.setIIiii(stat);
						di.setDATA_TIME(TimeUtil.date2String(zAgmeCropAuto.getObservationTime(), "yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());	
						di.setLATITUDE(String.valueOf(zAgmeCropAuto.getLatitude()));
						di.setLONGTITUDE(String.valueOf(zAgmeCropAuto.getLongitude()));
						di.setDATA_UPDATE_FLAG(zAgmeCropAuto.getCorrectMarker());
						di.setHEIGHT(String.valueOf(zAgmeCropAuto.getHeightOfStaionAboveSeaLevel()));
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(file.length()));
//						pStatement.addBatch();
						loggerBuffer.append("  "+ zAgmeCropAuto.getStationNumberChina() + "  " + sdf.format(zAgmeCropAuto.getObservationTime()) +" 000 \n" );
						sqls.add(((LoggableStatement)pStatement).getQueryString()); // 批量入库冲突时，单条入库
						listDi.add(di);
						try{
							pStatement.execute();
							connection.commit();
						}catch (Exception e) {
							di.setPROCESS_STATE("0");
							loggerBuffer.append("Insert Commit failed "+fileN + "\n");
						}
					} 
					else{
						zAgmeCropAutos2.add(zAgmeCropAuto);
					}
				}
//				try{
//					pStatement.executeBatch();
//					connection.commit();
//					loggerBuffer.append("Batch successfully submitted ,success count："+zAgmeCropAutos.size()+"\n");
//					sqls.clear();
//				}catch(SQLException e){
//					pStatement.clearParameters();
//					pStatement.clearBatch();
//					loggerBuffer.append("Batch commit failed and start single insert"+fileN + "\n");
//					execute_sql(sqls, connection, loggerBuffer); // 此中，入库失败的会将对应的DI的ProcessState置为1 // 1:失败，0：成功
//					
//				}
			}catch (SQLException e) {
				loggerBuffer.append("\n create  Statement  error: "+e.getMessage());
			}finally {
				if(pStatement != null) {
					try {
						pStatement.close();
					} catch (SQLException e) {
						loggerBuffer.append("\n close Statement error"+e.getMessage());
					}
				}
				// 更正报的入库
				if(zAgmeCropAutos2.size() > 0)
					updateCropEle(zAgmeCropAutos2, connection, recv_time, loggerBuffer,filepath);
			}
		} 
		
	}
	
	/**
   * 
   * @param loggerBuffer 
	 * @param fileN 
	 * @Title: insertCropAuto   
   * @Description: TODO(农作物自动观测设备状态数据入库函数)   
   * @param: @param zAgmeCropAutos
   * @param: @param connection
   * @param: @param recv_time      
   * @return: void      
   * @throws：
   */
	private static void insertCropAutoDevice(List<ZAgmeCropAuto> zAgmeCropAutos, java.sql.Connection connection, Date recv_time, StringBuffer loggerBuffer, String filepath) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		// chy 去掉 D_record_id
		String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_record_id,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,D_UPDATE_TIME,"
			+ "V01301,V05001,V06001,V07030,V07031,V_ACODE,V04001,V04002,V04003,V04004,"
			+ "V02261,V02262,V02263,V02264,V02265,V02266,V02267,V02268,V02269,V35024,V_BBB,D_SOURCE_ID) "
			+ "VALUES (?, ?, ?, ?, ?, ?,"
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " ;
		List<ZAgmeCropAuto> zAgmeCropAutos2 = new ArrayList<>();
		if(connection != null){		
			try {	
				connection.setAutoCommit(false);
				pStatement = new LoggableStatement(connection, sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				List<String> sqls = new ArrayList<>();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");		
				
				for(int idx = 0; idx < zAgmeCropAutos.size(); idx ++){
					ZAgmeCropAuto zAgmeCropAuto = new ZAgmeCropAuto();
					zAgmeCropAuto = zAgmeCropAutos.get(idx);
					if(zAgmeCropAuto.getCorrectMarker() == null || zAgmeCropAuto.getCorrectMarker().isEmpty())
						zAgmeCropAuto.setCorrectMarker("000");
					if(zAgmeCropAuto.getCorrectMarker().equals("000") || findVBBCropDevice(zAgmeCropAuto, connection, loggerBuffer) == null){
						// 非更正报；或者为更正报，但是数据库中尚未有这一条数据
						StatDi di = new StatDi();	
						di.setFILE_NAME_O(fileN);
						di.setDATA_TYPE(sod_device);
						di.setDATA_TYPE_1(cts_code);
						di.setTT(V_TT);			
						di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
						di.setPROCESS_START_TIME(TimeUtil.getSysTime());
						di.setFILE_NAME_N(fileN);
						di.setBUSINESS_STATE("1"); //1成功，0失败
						di.setPROCESS_STATE("1");  //1成功，0失败	
						
						String stat = zAgmeCropAuto.getStationNumberChina();
						int adminCode = defaultInt;
						String info = (String) proMap.get(stat + "+12");
						if(info != null) {
							String[] infos = info.split(",");
							if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
								adminCode = Integer.parseInt(infos[5]);
						}
						
						if(adminCode == 999999){
							info = (String) proMap.get(stat + "+01");
							if(info == null) {
								loggerBuffer.append("\n In the configuration file, the station number does not exist" + stat);
							}else {
								String[] infos = info.split(",");
								if(infos.length >= 6)
									adminCode = Integer.parseInt(infos[5]);
							}
						}
						
						int ii = 1;
						pStatement.setString(ii++, sdf.format(zAgmeCropAuto.getObservationTime())+"_"+stat);
						pStatement.setString(ii++, sod_device);
						pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
						pStatement.setTimestamp(ii++, new Timestamp(zAgmeCropAuto.getObservationTime().getTime()));
						pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						//"V01301,V05001,V06001,V07001,V07031,V_ACODE,V04001,V04002,V04003,V04004,"
						pStatement.setString(ii++, stat);
						pStatement.setBigDecimal(ii++, new BigDecimal(zAgmeCropAuto.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
						pStatement.setBigDecimal(ii++, new BigDecimal(zAgmeCropAuto.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(zAgmeCropAuto.getHeightOfStaionAboveSeaLevel())));
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(zAgmeCropAuto.getHeightOfPressureSensor())));
						pStatement.setInt(ii++, adminCode);
						pStatement.setInt(ii++, zAgmeCropAuto.getObservationTime().getYear() + 1900);
						pStatement.setInt(ii++, zAgmeCropAuto.getObservationTime().getMonth() + 1);
						pStatement.setInt(ii++, zAgmeCropAuto.getObservationTime().getDate() );
						pStatement.setInt(ii++, zAgmeCropAuto.getObservationTime().getHours());
						
						// "V02261,V02262,V02263,V02264,V02265,V02266,V02267,V02268,V02269) 
						pStatement.setInt(ii++, zAgmeCropAuto.getColllectorRunningState());
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(zAgmeCropAuto.getCollectorVoltage())));
						pStatement.setInt(ii++, zAgmeCropAuto.getCollectorPowerSupplyType());
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(zAgmeCropAuto.getCollectorMainboardTemperature())));
						pStatement.setInt(ii++, zAgmeCropAuto.getCollectorCFstate());
						pStatement.setInt(ii++, zAgmeCropAuto.getCollectorCFRemainSpace());
						pStatement.setInt(ii++, zAgmeCropAuto.getCollectorGPSWorkingstate());
						pStatement.setInt(ii++, zAgmeCropAuto.getCollectorGateswitchState());
						pStatement.setInt(ii++, zAgmeCropAuto.getCollectorLANterminalCommunicationState());
						pStatement.setString(ii++, zAgmeCropAuto.getCorrectMarker());
						pStatement.setString(ii++, zAgmeCropAuto.getCorrectMarker());
						pStatement.setString(ii++, cts_code);
						
						di.setIIiii(stat);
						di.setDATA_TIME(TimeUtil.date2String(zAgmeCropAuto.getObservationTime(), "yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());	
						di.setLATITUDE(String.valueOf(zAgmeCropAuto.getLatitude()));
						di.setLONGTITUDE(String.valueOf(zAgmeCropAuto.getLongitude()));
						di.setDATA_UPDATE_FLAG(zAgmeCropAuto.getCorrectMarker());
						di.setHEIGHT(String.valueOf(zAgmeCropAuto.getHeightOfStaionAboveSeaLevel()));
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(file.length()));
						pStatement.addBatch();
						sqls.add(((LoggableStatement)pStatement).getQueryString()); // 批量入库冲突时，单条入库
						listDi.add(di);
					}
					else{
						zAgmeCropAutos2.add(zAgmeCropAuto);
					}
				} 
				try{
					pStatement.executeBatch();
					connection.commit();
					sqls.clear();
				}catch(SQLException e){
					connection.rollback();
					pStatement.clearParameters();
					pStatement.clearBatch();
					loggerBuffer.append("\n Batch commit failed："+fileN);
					execute_sql(sqls, connection, loggerBuffer); // 此中，入库失败的会将对应的DI的ProcessState置为1 // 1:失败，0：成功
					
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
				if(zAgmeCropAutos2.size() > 0)
					updateCropDevice(zAgmeCropAutos2, connection, recv_time, loggerBuffer,filepath);
			}
		} 
	}

	/**
	 * 
	 * @param loggerBuffer 
	 * @param fileN 
	 * @Title: updateCropEle   
	 * @Description: TODO(农作物自动观测要素入库-更正报)   
	 * @param: @param zAgmeCropAutos 要素对象，更正报：数据库中已经有该条数据
	 * @param: @param connection
	 * @param: @param recv_time      
	 * @return: void      
	 * @throws:
	 */
	private static void updateCropEle(List<ZAgmeCropAuto> zAgmeCropAutos, java.sql.Connection connection, Date recv_time, StringBuffer loggerBuffer, String filepath) {
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement Pstmt = null;
		String vbbbInDB = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");	
		ZAgmeCropAuto zAgmeCropAuto = new ZAgmeCropAuto(); 
		String sql = "update "+StartConfig.keyTable()+" set "
				+ "V35024=?, V_BBB=?, D_UPDATE_TIME=?,"
				+ "V05001=?,V06001=?,V07030=?,V07031=?,V07256=?,V02271=?,V48002=?,V02256=?,V02259=?,V02270=?,"
				+ "V02257=?,V02258=?,V02260=?,Q48001=?,V26256=?,Q26256=?,V26257=?,Q26257=?,V48009=?,Q48009=?,"
				+ "V48008=?,Q48008=?,V48010=?,Q48010=?,V48034=?,Q48034=?,V48007=?,Q48007=?,V48301=?,Q48301=?,"
				+ "V48006=?,Q48006=?,V51000=?,V51009=?,Q51002=?,V26269=?,Q26269=?,V26270=?,Q26270=?,V30256=?,"
				+ "D_STORAGE_SITE=?,V33256=?  "
				+ "where V01301 = ? and V04001 = ?"
				+ " and V04002 = ? and V04003 = ? and V04004 = ? and V48000 = ? and V48001 = ?";
		if(connection != null){
			try{
				connection.setAutoCommit(true);
				Pstmt = new LoggableStatement(connection, sql);
				
				for(int idx = 0; idx < zAgmeCropAutos.size(); idx ++){
					zAgmeCropAuto = zAgmeCropAutos.get(idx);
					vbbbInDB = findVBBCrop(zAgmeCropAuto, connection,loggerBuffer); // 插入前，从DB中查找该条记录的更正状态
					if(vbbbInDB.compareTo(zAgmeCropAuto.getCorrectMarker()) < 0){ // 数据库中有该条记录，且V_BBB的值早于当前处理文件的更正标识的值
						// update
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
						int ii = 1;
						
						Pstmt.setString(ii++, zAgmeCropAuto.getCorrectMarker());
						Pstmt.setString(ii++, zAgmeCropAuto.getCorrectMarker());
						Pstmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						
						Pstmt.setBigDecimal(ii++, new BigDecimal(zAgmeCropAuto.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
						Pstmt.setBigDecimal(ii++, new BigDecimal(zAgmeCropAuto.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(zAgmeCropAuto.getHeightOfStaionAboveSeaLevel())));
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(zAgmeCropAuto.getHeightOfPressureSensor())));
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(zAgmeCropAuto.getImageSensorHeightAboveGround())));
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(zAgmeCropAuto.getImageSensorFocus())));
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(zAgmeCropAuto.getObvArea())));
						Pstmt.setInt(ii++, zAgmeCropAuto.getObvMethod());
						Pstmt.setString(ii++, zAgmeCropAuto.getImageSensorID());
						Pstmt.setInt(ii++, zAgmeCropAuto.getImageSensorConnectionStatus());
						
						Pstmt.setInt(ii++, zAgmeCropAuto.getGPSTimingMarker());
						Pstmt.setInt(ii++, zAgmeCropAuto.getCFPeripheralStorageMarker());
						Pstmt.setString(ii++, zAgmeCropAuto.getAgmeReportSoftwareVersion());
						Pstmt.setInt(ii++, zAgmeCropAuto.getCropGrowthPeriodQC());
						Pstmt.setString(ii++, zAgmeCropAuto.getGrowthStarttime());
						Pstmt.setInt(ii++, zAgmeCropAuto.getGrowthStarttimeQC());
						Pstmt.setInt(ii++, zAgmeCropAuto.getGrowthDuration());
						Pstmt.setInt(ii++, zAgmeCropAuto.getGrowthDurationQC());
						Pstmt.setInt(ii++, zAgmeCropAuto.getGrowthPeriodPercent());
						Pstmt.setInt(ii++, zAgmeCropAuto.getGrowthPeriodPercentQC());
						
						Pstmt.setInt(ii++, zAgmeCropAuto.getPlantCoverage());
						Pstmt.setInt(ii++, zAgmeCropAuto.getPlantCoverageQC());
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(zAgmeCropAuto.getLeafAreaIndex())));
						Pstmt.setInt(ii++, zAgmeCropAuto.getLeafAreaIndexQC());
						Pstmt.setInt(ii++, zAgmeCropAuto.getCanopyHeight());
						Pstmt.setInt(ii++, zAgmeCropAuto.getCanopyHeightQC());
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(zAgmeCropAuto.getPlantDensity())));
						Pstmt.setInt(ii++, zAgmeCropAuto.getPlantDensityQC());
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(zAgmeCropAuto.getPlantDensity())));
						Pstmt.setInt(ii++, zAgmeCropAuto.getPlantDensityQC());
						
						Pstmt.setInt(ii++, zAgmeCropAuto.getGrowthState());
						Pstmt.setInt(ii++, zAgmeCropAuto.getGrowthStateQC());
						Pstmt.setInt(ii++, zAgmeCropAuto.getDisaName());
						Pstmt.setInt(ii++, zAgmeCropAuto.getDisaLevel());
						Pstmt.setInt(ii++, zAgmeCropAuto.getDisaLevelQC());
						Pstmt.setString(ii++, zAgmeCropAuto.getDisaStarttime());
						Pstmt.setInt(ii++, zAgmeCropAuto.getDisaStarttimeQC());
						Pstmt.setInt(ii++, zAgmeCropAuto.getDisaDuration());
						Pstmt.setInt(ii++, zAgmeCropAuto.getDisaDurationQC());
						Pstmt.setString(ii++, zAgmeCropAuto.getCropImageFormat());

						// chy 修改
//						Pstmt.setBytes(ii++,zAgmeCropAuto.getCropImage());
						if(zAgmeCropAuto.getCropImage() != null && zAgmeCropAuto.getCropImage().length > 0){
							String dtString = new SimpleDateFormat("yyyyMMddHHmmss").format(zAgmeCropAuto.getObservationTime());
							//AGME_I_IIIII_yyyymmddHHMMSS_序号.jpg 注：序号从1开始，多张图片依次编码
							String path = dtString.substring(0, 4) + 
									"/" + dtString.substring(0, 8) + 
									"/" + zAgmeCropAuto.getStationNumberChina() + 
									"/AGME_I_" + zAgmeCropAuto.getStationNumberChina()+ "_" + dtString + "_" + zAgmeCropAuto.getImageSensorID() + ".jpg";
									
							String https = ClientConn.savePics(zAgmeCropAuto.getCropImage(), path);
							loggerBuffer.append(https + "\n");
							
							Pstmt.setString(ii++, https);
						}
						else 
							Pstmt.setString(ii ++, "");
						

						Pstmt.setInt(ii++, zAgmeCropAuto.getQualityControl());
						
						Date date = new Date();
						date = zAgmeCropAuto.getObservationTime();
						Pstmt.setString(ii++, zAgmeCropAuto.getStationNumberChina());
						Pstmt.setInt(ii++, date.getYear() +1900);
						Pstmt.setInt(ii++, date.getMonth() + 1);
						Pstmt.setInt(ii++, date.getDate());
						Pstmt.setInt(ii++, date.getHours());
						Pstmt.setInt(ii++, zAgmeCropAuto.getCropName());
						Pstmt.setInt(ii++, zAgmeCropAuto.getCropGrowthPeriod());
						
						di.setIIiii(zAgmeCropAuto.getStationNumberChina());
						di.setDATA_TIME(TimeUtil.date2String(zAgmeCropAuto.getObservationTime(), "yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setLATITUDE(String.valueOf(zAgmeCropAuto.getLatitude()));
						di.setLONGTITUDE(String.valueOf(zAgmeCropAuto.getLongitude()));
						di.setDATA_UPDATE_FLAG(zAgmeCropAuto.getCorrectMarker());
						di.setHEIGHT(String.valueOf(zAgmeCropAuto.getHeightOfStaionAboveSeaLevel()));
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(file.length()));
						
						loggerBuffer.append("  "+ zAgmeCropAuto.getStationNumberChina() + "  " + sdf.format(zAgmeCropAuto.getObservationTime()) +"  "+zAgmeCropAuto.getCorrectMarker()+" \n" );
						listDi.add(di);
						try{
							Pstmt.execute();

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
			loggerBuffer.append("\n Database connection error");
	}
	
	/**
	 * 
	 * @param loggerBuffer 
	 * @Title: findVBBCrop   
	 * @Description: TODO(查找农作物自动站观测数据的更正标识)   
	 * @param: @param zAgmeCropAuto
	 * @param: @param connection
	 * @param: @return      
	 * @return: String      
	 * @throws:
	 */
	private static String findVBBCrop(ZAgmeCropAuto zAgmeCropAuto, java.sql.Connection connection, StringBuffer loggerBuffer) {
		  PreparedStatement Pstmt = null;
		  ResultSet resultSet  = null;
		  String rntString = null;
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		  // chy 修改
//		  String sql = "select V35024 from "+StartConfig.keyTable()+" "+ "where D_RECORD_ID = ?";
		  String sql = "select V35024 from "+StartConfig.keyTable()+" "+ "where D_datetime = ? and V01301=? and V48000=? and V48001 = ? and V02259=?";
		  try{
			
			if(connection != null){			
				Pstmt = connection.prepareStatement(sql);
//				Pstmt.setString(1, sdf.format(zAgmeCropAuto.getObservationTime())+"_"+zAgmeCropAuto.getStationNumberChina()+"_"+zAgmeCropAuto.getCropName()+"_"+zAgmeCropAuto.getCropGrowthPeriod());
				Pstmt.setTimestamp(1, new Timestamp(zAgmeCropAuto.getObservationTime().getTime()));
				Pstmt.setString(2, zAgmeCropAuto.getStationNumberChina());
				Pstmt.setInt(3, zAgmeCropAuto.getCropName());
				Pstmt.setInt(4, zAgmeCropAuto.getCropGrowthPeriod());
				Pstmt.setString(5, zAgmeCropAuto.getImageSensorID());
				resultSet = Pstmt.executeQuery();
				if(resultSet.next()){
					rntString = resultSet.getString(1);
				}
			}
			}catch(SQLException e){
				loggerBuffer.append("\n create Statement error " + e.getMessage());
			}
			finally {
				if(resultSet != null){
					try{
						resultSet.close();
					}catch(SQLException e){
						loggerBuffer.append("\n close resultSet error " + e.getMessage());
					}
				}
				if(Pstmt != null) {
					try {
						Pstmt.close();
					} catch (SQLException e) {
						loggerBuffer.append("\n close Statement error" + e.getMessage());
					}
				}
			}
		  return rntString;
		}
	/**
	 * 
	 * @param loggerBuffer 
	 * @param fileN 
	 * @Title: updateCropDevice   
	 * @Description: TODO(农作物自动观测设备要素入库-更正报)   
	 * @param: @param zAgmeCropAutos  要素对象，更正报：数据库中已经有该条数据
	 * @param: @param connection
	 * @param: @param recv_time      
	 * @return: void      
	 * @throws:
	 */
	private static void updateCropDevice(List<ZAgmeCropAuto> zAgmeCropAutos, java.sql.Connection connection, Date recv_time, StringBuffer loggerBuffer, String filepath) {
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement Pstmt = null;
		String vbbbInDB = null;
		ZAgmeCropAuto zAgmeCropAuto = new ZAgmeCropAuto(); 
		String sql = "update "+StartConfig.valueTable()+" set "
				+ "V35024=?, V_BBB=?, D_UPDATE_TIME=?,"
				+ "V05001=?,V06001=?,V07030=?,V07031=?,V02261=?,V02262=?,V02263=?,V02264=?,V02265=?,V02266=?,"
				+ "V02267=?,V02268=?,V02269=? "
				+ "where V01301 = ? and V04001 = ?"
				+ " and V04002 = ? and V04003 = ? and V04004 = ?";
		if(connection != null){
			try{
				connection.setAutoCommit(true);
				Pstmt = new LoggableStatement(connection, sql);
				for(int idx = 0; idx < zAgmeCropAutos.size(); idx ++){
					zAgmeCropAuto = zAgmeCropAutos.get(idx);
					vbbbInDB = findVBBCropDevice(zAgmeCropAuto, connection, loggerBuffer); // 插入前，从DB中查找该条记录的更正状态
					if(vbbbInDB.compareTo(zAgmeCropAuto.getCorrectMarker()) < 0){ // 数据库中有该条记录，且V_BBB的值早于当前处理文件的更正标识的值
						// update
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
						int ii = 1;
						Date date = new Date();
						date = zAgmeCropAuto.getObservationTime();
						Pstmt.setString(ii++, zAgmeCropAuto.getCorrectMarker());
						Pstmt.setString(ii++, zAgmeCropAuto.getCorrectMarker());
						Pstmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						
						Pstmt.setBigDecimal(ii++, new BigDecimal(zAgmeCropAuto.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
						Pstmt.setBigDecimal(ii++, new BigDecimal(zAgmeCropAuto.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(zAgmeCropAuto.getHeightOfStaionAboveSeaLevel())));
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(zAgmeCropAuto.getHeightOfPressureSensor())));
						Pstmt.setInt(ii++, zAgmeCropAuto.getColllectorRunningState());
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(zAgmeCropAuto.getCollectorVoltage())));
						Pstmt.setInt(ii++, zAgmeCropAuto.getCollectorPowerSupplyType());
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(zAgmeCropAuto.getCollectorMainboardTemperature())));
						Pstmt.setInt(ii++, zAgmeCropAuto.getCollectorCFstate());
						Pstmt.setInt(ii++, zAgmeCropAuto.getCollectorCFRemainSpace());
						
						Pstmt.setInt(ii++, zAgmeCropAuto.getCollectorGPSWorkingstate());
						Pstmt.setInt(ii++, zAgmeCropAuto.getCollectorGateswitchState());
						Pstmt.setInt(ii++, zAgmeCropAuto.getCollectorLANterminalCommunicationState());
						
						Pstmt.setString(ii++, zAgmeCropAuto.getStationNumberChina());
						Pstmt.setInt(ii++, date.getYear() + 1900);
						Pstmt.setInt(ii++, date.getMonth() + 1);
						Pstmt.setInt(ii++, date.getDate());
						Pstmt.setInt(ii++, date.getHours());
						
						di.setIIiii(zAgmeCropAuto.getStationNumberChina());
						di.setDATA_TIME(TimeUtil.date2String(zAgmeCropAuto.getObservationTime(), "yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setLATITUDE(String.valueOf(zAgmeCropAuto.getLatitude()));
						di.setLONGTITUDE(String.valueOf(zAgmeCropAuto.getLongitude()));
						di.setDATA_UPDATE_FLAG(zAgmeCropAuto.getCorrectMarker());
						di.setHEIGHT(String.valueOf(zAgmeCropAuto.getHeightOfStaionAboveSeaLevel()));
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(file.length()));
						
						listDi.add(di);
						try{
							Pstmt.execute();
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
			loggerBuffer.append("\n Database connection error");
	}
	/**
	 * 
	 * @param loggerBuffer 
	 * @Title: findVBBCropDevice   
	 * @Description: TODO(查找农作物自动站观设备状态测数据的更正标识)   
	 * @param: @param zAgmeCropAuto
	 * @param: @param connection
	 * @param: @return      
	 * @return: String      
	 * @throws：
	 */
	private static String findVBBCropDevice(ZAgmeCropAuto zAgmeCropAuto, java.sql.Connection connection, StringBuffer loggerBuffer) {
		  PreparedStatement Pstmt = null;
		  ResultSet resultSet  = null;
		  String rntString = null;
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		  String sql = "select V35024 from "+StartConfig.valueTable()+" "
				  // chy 修改
//					+ "where D_RECORD_ID = ?";
				  + "where D_DATETIME = ?  and V01301=?";
		  try{
			
			if(connection != null){				
				Pstmt = connection.prepareStatement(sql);
//				Pstmt.setString(1, sdf.format(zAgmeCropAuto.getObservationTime())+"_"+zAgmeCropAuto.getStationNumberChina());
				Pstmt.setTimestamp(1, new Timestamp(zAgmeCropAuto.getObservationTime().getTime()));
				Pstmt.setString(2, zAgmeCropAuto.getStationNumberChina());
				
				resultSet = Pstmt.executeQuery();
				if(resultSet.next()){
					rntString = resultSet.getString(1);
				}
			}
			}catch(SQLException e){
				loggerBuffer.append("\n create Statement error " + e.getMessage());
			}
			finally {
				if(resultSet != null){
					try{
						resultSet.close();
					}catch(SQLException e){
						loggerBuffer.append("\n close resultSet error " + e.getMessage());
					}
				}
				if(Pstmt != null) {
					try {
						Pstmt.close();
					} catch (SQLException e) {
						loggerBuffer.append("\n close Statement error " + e.getMessage());
					}
				}
			}
		  return rntString;
	}
	/**
	 * 
	 * @param loggerBuffer 
	 * @Title: execute_sql   
	 * @Description: TODO(批量入库失败时，采用逐条提交)   
	 * @param: @param sqls
	 * @param: @param connection      
	 * @return: void      
	 * @throws：
	 */
	private static void execute_sql(List<String> sqls, java.sql.Connection connection, StringBuffer loggerBuffer) {
		Statement pStatement = null;
		try {
			connection.setAutoCommit(true);
			pStatement = connection.createStatement();
			for (int i = 0; i < sqls.size(); i++) {
//				pStatement = new LoggableStatement(connection, sqls.get(i));
				try {
					pStatement.execute(sqls.get(i));
					loggerBuffer.append("\n " + listDi.get(i).getIIiii() + " " + listDi.get(i).getDATA_TIME());
				} catch (Exception e) {
					loggerBuffer.append("\n " + listDi.get(i).getIIiii() + " " + listDi.get(i).getDATA_TIME()
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
}
