package cma.cimiss2.dpc.indb.radi.dc_radi_sads.service;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.radi.AutomaticStationRadiatingHourlyData;
import cma.cimiss2.dpc.decoder.radi.DecodeRadiSads;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.radi.ReportInfoService;

public class DbService {
	public static String v_tt = "SADS";
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	public static String report_sod_code=StartConfig.reportSodCode();
	public static BlockingQueue<StatDi> diQueues;
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	 
//	static Map<String, Object> proMap = StationInfo.getProMap();

	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}
	/**
	 * 
	 * @Title: processSuccessReport   
	 * @Description: (报文解码入库函数)   
	 * @param  parseResult  存放解码结果
	 * @param filepath 文件路径
	 * @param  recv_time  报文接收时间   
	 * @param v_bbb 
	 * @param isRevised 
	 * @param loggerBuffer 
	 * @return: DataBaseAction  入库状态  
	 * @throws:
	 */
	public static DataBaseAction processSuccessReport(ParseResult<AutomaticStationRadiatingHourlyData> parseResult, String fileN, Date recv_time, StringBuffer loggerBuffer, boolean isRevised, String v_bbb, String filepath) {
		java.sql.Connection connection = null;
		java.sql.Connection reportConnection = null;
		try {
			// 获取数据库连接
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			// 获取解码结果集
			List<AutomaticStationRadiatingHourlyData> sads = parseResult.getData();
			if (isRevised) {
				updateDB(sads, connection, recv_time,fileN,v_bbb,loggerBuffer,filepath);
			}else{
				insertDB(sads, connection, recv_time,fileN,v_bbb,loggerBuffer, filepath);
			}		
			@SuppressWarnings("rawtypes")
			List<ReportInfo> reportInfos = parseResult.getReports();
			reportConnection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
			ReportInfoService.reportInfoToDb(reportInfos, reportConnection, v_bbb, recv_time, "9999", v_tt, report_sod_code,cts_code,filepath,listDi,"11");	 
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
					loggerBuffer.append("\n database connection close error"+e.getMessage());
				}
			}
			
			if(reportConnection != null) {
				try {
					reportConnection.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n database connection close error"+e.getMessage());
				}
			}
		}
	}
	/**
	 * 
	 * @Title: insertDB
	 * @Description:(自动站辐射资料入库(批量入库))
	 * @param Sads 待入库对象
	 * @param connection 数据库连接
	 * @param recv_time 接收时间
	 * @param v_bbb 
	 * @param fileN 
	 * @param loggerBuffer 
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static void insertDB(List<AutomaticStationRadiatingHourlyData> Sads, java.sql.Connection connection, Date recv_time, String fileN, String v_bbb, StringBuffer loggerBuffer, String filepath){
		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement pStatement = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		int sz = listDi.size();
		// chy 去掉 D_RECORD_ID
		String sql = "INSERT into "+StartConfig.valueTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,"
				+ "D_DATETIME,V_BBB,V01301,V01300,V05001,V06001,V07001,V04001,V04002,V04003,V04004,"
				+ "V04005,V14311,V14312,V14313,V14314,V14315,V14316,V14320,V14311_05,V14021_05_052,"
				+ "V14308,V14312_05,V14312_05_052,V14312_06,V14312_06_052,V14322,V14313_05,V14313_05_052,"
				+ "V14309,V14314_05,V14314_05_052,V14306,V14315_05,V14315_05_052,V14307,V14316_05,"
				+ "V14316_05_052,V14032,V15483,Q14311,Q14312,Q14313,Q14314,Q14315,Q14316,Q14320,Q14311_05,"
				+ "Q14021_05_052,Q14308,Q14312_05,Q14312_05_052,Q14312_06,Q14312_06_052,Q14322,Q14313_05,"
				+ "Q14313_05_052,Q14309,Q14314_05,Q14314_05_052,Q14306,Q14315_05,Q14315_05_052,Q14307,"
				+ "Q14316_05,Q14316_05_052,Q14032,Q15483, D_SOURCE_ID) "
				+ " VALUES (?,?, ?, ?, ?, ?,?,?,?,?,"
				+ " ?, ?, ?, ?, ?, ?,?,?,?,?,"
				+ "?, ?, ?, ?, ?, ?,?,?,?,?,"
				+ "?, ?, ?, ?, ?, ?,?,?,?,?,"
				+ "?, ?, ?, ?, ?, ?,?,?,?,?,"
				+ "?, ?, ?, ?, ?, ?,?,?,?,?,"
				+ "?, ?, ?, ?, ?, ?,?,?,?,?,"		
				+ " ?, ?, ?, ?) " ;
		if(connection != null){		
			try {	
				pStatement = new LoggableStatement(connection, sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				for(int idx = 0; idx < Sads.size(); idx ++){
					AutomaticStationRadiatingHourlyData sads = Sads.get(idx);
					StatDi di = new StatDi();	
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(sod_code);
					di.setDATA_TYPE_1(cts_code);
					di.setTT(v_tt);			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1");  //1成功，0失败	
					// 根根配置文件  获取站点基本信息
					double height = 999999;
					String info = (String) proMap.get(sads.getStationNumberChina() + "+11");
					if(info == null) {//若在站网代码11中取不到值，则取01中的
						 info = (String) proMap.get(sads.getStationNumberChina() + "+01");
						 if (info!=null) {
							 String[] infos = info.split(",");
							 if(!(infos[3].trim().equals("null") || infos[3].trim().equals(""))){
								 height = Double.parseDouble(infos[3]);
							 }
						 }else{
						    loggerBuffer.append("\n In the configuration file, the station number does not exist" + sads.getStationNumberChina());
						 }
					}else{//若在站网代码11中能取到值
						String[] infos = info.split(",");
						if(!(infos[3].trim().equals("null") || infos[3].trim().equals(""))){
							height = Double.parseDouble(infos[3]);
						}
					}		
					int ii = 1;
					pStatement.setString(ii++, sdf.format(sads.getObservationTime())+"_"+sads.getStationNumberChina());//记录标识
					pStatement.setString(ii++, sod_code);//资料标识
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//入库时间
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));//收到时间
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间
					
					// D_DATETIME 时间规整
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(sads.getObservationTime());
					calendar.set(Calendar.MINUTE, 0);
					calendar.set(Calendar.SECOND, 0);
					
					pStatement.setTimestamp(ii++, new Timestamp((calendar.getTime().getTime())));//资料时间
					pStatement.setString(ii++, v_bbb);  // 更正报标识
					pStatement.setString(ii++, sads.getStationNumberChina());//区站号(字符)
					pStatement.setInt(ii++, Integer.parseInt(StationCodeUtil.stringToAscii(sads.getStationNumberChina())));//区站号(数字)
					
					BigDecimal latitude= new BigDecimal(sads.getLatitude());
					Double latitude2 = latitude.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
					BigDecimal longitude= new BigDecimal(sads.getLongitude());
					Double longitude2 = longitude.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
					
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(latitude2)));//纬度
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(longitude2)));//经度
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(height)));//测站海拔高度
					pStatement.setInt(ii++, sads.getObservationTime().getYear() + 1900); // 资料年
					pStatement.setInt(ii++, sads.getObservationTime().getMonth() + 1); // 资料月
					pStatement.setInt(ii++, sads.getObservationTime().getDate()); // 资料日
					pStatement.setInt(ii++, sads.getObservationTime().getHours()); // 资料时
					pStatement.setInt(ii++, sads.getObservationTime().getMinutes()); //资料分
				
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getTotalRadiationIrradiance())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getNetRadiationIrradiance())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getDirectRadiationIrradiance())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getScatteringRadiationIrradiance())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getReflectionRadiationIrradiance())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getUltravioletRadiationIrradiance())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getTotalRadiationExposure())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getRadiationIrradianceMax())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getRadiationIrradianceMaxtime())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getNetRadiationExposure())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getNetRadiationIrradiationMax())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getNetRadiationIrradiationMaxtime())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getNetRadiationIrradiationMin())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getNetRadiationIrradiationMintime())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getDirectRadiationExposure())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getDirectRadiationIrradianceMax())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getDirectRadiationIrradianceMaxtime())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getScatteredRadiationExposure())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getScatteringRadiationIrradianceMax())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getScatteringRadiationIrradianceMaxtime())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getReflectedRadiationExposure())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getReflectedRadiationIrradianceMax())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getReflectedRadiationIrradianceMaxtime())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getUltravioletRadiationExposure())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getUvIrradiationMax())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getUvIrradiationMaxtime())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getSunShineHours())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getAtmosphericTurbidityIndex())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getQ_totalRadiationIrradiance())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getQ_netRadiationIrradiance())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getQ_directRadiationIrradiance())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getQ_scatteringRadiationIrradianc())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getQ_reflectionRadiationIrradiance())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getQ_ultravioletRadiationIrradiance())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getQ_totalRadiationExposure())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getQ_radiationIrradianceMax())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getQ_radiationIrradianceMaxtime())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getQ_netRadiationExposure())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getQ_netRadiationIrradiationMax())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getQ_netRadiationIrradiationMaxtime())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getQ_netRadiationIrradiationMin())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getQ_netRadiationIrradiationMintime())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getQ_directRadiationExposure())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getQ_directRadiationIrradianceMax())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getQ_directRadiationIrradianceMaxtime())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getQ_scatteredRadiationExposure())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getQ_scatteringRadiationIrradianceMax())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getQ_scatteringRadiationIrradianceMaxtime())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getQ_reflectedRadiationExposure())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getQ_reflectedRadiationIrradianceMax())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getQ_reflectedRadiationIrradianceMaxtime())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getQ_ultravioletRadiationExposure())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getQ_uvIrradiationMax())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getQ_uvIrradiationMaxtime())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getQ_sunShineHours())));;
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(sads.getQ_atmosphericTurbidityIndex())));;
					pStatement.setString(ii++, StartConfig.ctsCode());
					
					di.setIIiii(sads.getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(sads.getObservationTime(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLONGTITUDE(String.valueOf(sads.getLongitude()));
					di.setLATITUDE(String.valueOf(sads.getLatitude()));
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
					di.setDATA_UPDATE_FLAG(v_bbb);
					di.setHEIGHT(String.valueOf(sads.getHeightOfSationGroundAboveMeanSeaLevel()));
					
					pStatement.addBatch();
					listDi.add(di);
					
				} 
				try{
					pStatement.executeBatch();
					connection.commit();
				}catch(SQLException e){
//					e.printStackTrace();
					pStatement.clearParameters();
					pStatement.clearBatch();
					for(int i = 0; i < Sads.size(); i ++){
						listDi.remove(sz);
					}
					for(int i = 0; i < Sads.size(); i ++){
						insertOneLine_db(Sads.get(i), connection, recv_time,loggerBuffer,fileN,v_bbb,filepath);
					}
					loggerBuffer.append("\n Batch commit failed："+fileN);
				}
			}catch (SQLException e) {
				loggerBuffer.append("\n create Statement error"+e.getMessage());
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
	 * @Title: insertOneLine_db
	 * @Description:(自动站辐射资料单条入库)
	 * @param automaticStationRadiatingHourlyData 待入库对象
	 * @param connection 数据库连接
	 * @param recv_time 接收时间
	 * @param loggerBuffer 
	 * @param fileN 
	 * @param v_bbb 
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static void insertOneLine_db(AutomaticStationRadiatingHourlyData automaticStationRadiatingHourlyData,
			java.sql.Connection connection, Date recv_time, StringBuffer loggerBuffer, String fileN, String v_bbb, String filepath) {
		Map<String, Object> proMap = StationInfo.getProMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		PreparedStatement pStatement = null;
		// chy 去掉 D_RECORD_ID
		String sql = "INSERT into "+StartConfig.valueTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,"
				+ "D_DATETIME,V_BBB,V01301,V01300,V05001,V06001,V07001,V04001,V04002,V04003,V04004,"
				+ "V04005,V14311,V14312,V14313,V14314,V14315,V14316,V14320,V14311_05,V14021_05_052,"
				+ "V14308,V14312_05,V14312_05_052,V14312_06,V14312_06_052,V14322,V14313_05,V14313_05_052,"
				+ "V14309,V14314_05,V14314_05_052,V14306,V14315_05,V14315_05_052,V14307,V14316_05,"
				+ "V14316_05_052,V14032,V15483,Q14311,Q14312,Q14313,Q14314,Q14315,Q14316,Q14320,Q14311_05,"
				+ "Q14021_05_052,Q14308,Q14312_05,Q14312_05_052,Q14312_06,Q14312_06_052,Q14322,Q14313_05,"
				+ "Q14313_05_052,Q14309,Q14314_05,Q14314_05_052,Q14306,Q14315_05,Q14315_05_052,Q14307,"
				+ "Q14316_05,Q14316_05_052,Q14032,Q15483,D_SOURCE_ID) "
				+ " VALUES (?,?, ?, ?, ?, ?,?,?,?,?,"
				+ " ?, ?, ?, ?, ?, ?,?,?,?,?,"
				+ "?, ?, ?, ?, ?, ?,?,?,?,?,"
				+ "?, ?, ?, ?, ?, ?,?,?,?,?,"
				+ "?, ?, ?, ?, ?, ?,?,?,?,?,"
				+ "?, ?, ?, ?, ?, ?,?,?,?,?,"
				+ "?, ?, ?, ?, ?, ?,?,?,?,?,"		
				+ " ?, ?, ?, ?) " ;
		if(connection != null){	
			try {
				connection.setAutoCommit(true);
				pStatement = new LoggableStatement(connection, sql);
				StatDi di = new StatDi();	
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(sod_code);
				di.setDATA_TYPE_1(cts_code);
				di.setTT(v_tt);			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //1成功，0失败
				di.setPROCESS_STATE("1");  //1成功，0失败	
				// 根根配置文件  获取站点基本信息
				double height = 999999;
				String info = (String) proMap.get(automaticStationRadiatingHourlyData.getStationNumberChina() + "+11");
				if(info == null) {
					info = (String) proMap.get(automaticStationRadiatingHourlyData.getStationNumberChina() + "+01");
					if (info!=null) {
						String[] infos = info.split(",");
						if(!(infos[3].trim().equals("null") && infos[3].trim().equals(""))){
							height = Double.parseDouble(infos[3]);
						}
					}else{
						loggerBuffer.append("\n In the configuration file, the station number does not exist" + automaticStationRadiatingHourlyData.getStationNumberChina());
					}
				}else{
					
					String[] infos = info.split(",");
					if(!(infos[3].trim().equals("null") || infos[3].trim().equals(""))){
						height = Double.parseDouble(infos[3]);
					}
				}
				int ii = 1;
				pStatement.setString(ii++, sdf.format(automaticStationRadiatingHourlyData.getObservationTime())+"_"+automaticStationRadiatingHourlyData.getStationNumberChina());//记录标识
				pStatement.setString(ii++, sod_code);//资料标识
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//入库时间
				pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));//收到时间
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(automaticStationRadiatingHourlyData.getObservationTime());
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				
				pStatement.setTimestamp(ii++, new Timestamp((calendar.getTime().getTime())));//资料时间
				pStatement.setString(ii++, v_bbb);  // 更正报标识
				pStatement.setString(ii++, automaticStationRadiatingHourlyData.getStationNumberChina());//区站号(字符)
				pStatement.setInt(ii++, Integer.parseInt(StationCodeUtil.stringToAscii(automaticStationRadiatingHourlyData.getStationNumberChina())));//区站号(数字)
				
				BigDecimal latitude= new BigDecimal(automaticStationRadiatingHourlyData.getLatitude());
				Double latitude2 = latitude.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
				BigDecimal longitude= new BigDecimal(automaticStationRadiatingHourlyData.getLongitude());
				Double longitude2 = longitude.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
				
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(latitude2)));//纬度
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(longitude2)));//经度
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( height)));//测站高度
				pStatement.setInt(ii++, automaticStationRadiatingHourlyData.getObservationTime().getYear() + 1900); // 资料年
				pStatement.setInt(ii++, automaticStationRadiatingHourlyData.getObservationTime().getMonth() + 1); // 资料月
				pStatement.setInt(ii++, automaticStationRadiatingHourlyData.getObservationTime().getDate()); // 资料日
				pStatement.setInt(ii++, automaticStationRadiatingHourlyData.getObservationTime().getHours()); //资料时
				pStatement.setInt(ii++, automaticStationRadiatingHourlyData.getObservationTime().getMinutes()); //资料分
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getTotalRadiationIrradiance())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getNetRadiationIrradiance())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getDirectRadiationIrradiance())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getScatteringRadiationIrradiance())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getReflectionRadiationIrradiance())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getUltravioletRadiationIrradiance())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getTotalRadiationExposure())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getRadiationIrradianceMax())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getRadiationIrradianceMaxtime())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getNetRadiationExposure())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getNetRadiationIrradiationMax())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getNetRadiationIrradiationMaxtime())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getNetRadiationIrradiationMin())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getNetRadiationIrradiationMintime())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getDirectRadiationExposure())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getDirectRadiationIrradianceMax())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getDirectRadiationIrradianceMaxtime())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getScatteredRadiationExposure())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getScatteringRadiationIrradianceMax())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getScatteringRadiationIrradianceMaxtime())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getReflectedRadiationExposure())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getReflectedRadiationIrradianceMax())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getReflectedRadiationIrradianceMaxtime())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getUltravioletRadiationExposure())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getUvIrradiationMax())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getUvIrradiationMaxtime())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getSunShineHours())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getAtmosphericTurbidityIndex())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getQ_totalRadiationIrradiance())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getQ_netRadiationIrradiance())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getQ_directRadiationIrradiance())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getQ_scatteringRadiationIrradianc())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getQ_reflectionRadiationIrradiance())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getQ_ultravioletRadiationIrradiance())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getQ_totalRadiationExposure())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getQ_radiationIrradianceMax())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getQ_radiationIrradianceMaxtime())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getQ_netRadiationExposure())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getQ_netRadiationIrradiationMax())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getQ_netRadiationIrradiationMaxtime())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getQ_netRadiationIrradiationMin())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getQ_netRadiationIrradiationMintime())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getQ_directRadiationExposure())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getQ_directRadiationIrradianceMax())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getQ_directRadiationIrradianceMaxtime())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getQ_scatteredRadiationExposure())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getQ_scatteringRadiationIrradianceMax())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getQ_scatteringRadiationIrradianceMaxtime())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getQ_reflectedRadiationExposure())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getQ_reflectedRadiationIrradianceMax())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getQ_reflectedRadiationIrradianceMaxtime())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getQ_ultravioletRadiationExposure())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getQ_uvIrradiationMax())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getQ_uvIrradiationMaxtime())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getQ_sunShineHours())));
				pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( automaticStationRadiatingHourlyData.getQ_atmosphericTurbidityIndex())));
				pStatement.setString(ii++, StartConfig.ctsCode());
				
				di.setIIiii(automaticStationRadiatingHourlyData.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(automaticStationRadiatingHourlyData.getObservationTime(), "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());		
				di.setLONGTITUDE(String.valueOf(automaticStationRadiatingHourlyData.getLongitude()));
				di.setLATITUDE(String.valueOf(automaticStationRadiatingHourlyData.getLatitude()));
				
				di.setSEND("BFDB");
				di.setSEND_PHYS("DRDS");
				di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
				di.setDATA_UPDATE_FLAG(v_bbb);
				di.setHEIGHT(String.valueOf(automaticStationRadiatingHourlyData.getHeightOfSationGroundAboveMeanSeaLevel()));
				
				
				try{
					pStatement.execute();
					listDi.add(di);	
				}
				catch(SQLException e){							
					di.setPROCESS_STATE("0");//1成功，0失败
					listDi.add(di);		
					loggerBuffer.append("\n filename："+fileN+
								"\n " + automaticStationRadiatingHourlyData.getStationNumberChina() + " " + sdf.format(automaticStationRadiatingHourlyData.getObservationTime())
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
		}else {
			loggerBuffer.append("\n Database connection error");
		}
		
	}
	
	/**
	 * 
	 * @Title: updateDB
	 * @Description: (更正报报文的更新入库)
	 * @param sads 待入库的对象
	 * @param connection 数据库连接
	 * @param recv_time 接收时间
	 * @param v_bbb 
	 * @param fileN 
	 * @param loggerBuffer 
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static void updateDB(List<AutomaticStationRadiatingHourlyData> sads, java.sql.Connection connection, Date recv_time, String fileN, String v_bbb, StringBuffer loggerBuffer, String filepath){
		PreparedStatement Pstmt = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		for(int i = 0; i < sads.size(); i ++){
			String vbbbInDB = null;
			vbbbInDB = findVBB(sads.get(i), connection,loggerBuffer); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if(vbbbInDB == null){ // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				// insert
				insertOneLine_db(sads.get(i), connection, recv_time,loggerBuffer,fileN,v_bbb, filepath);
			}
			else if(vbbbInDB.compareTo(v_bbb) < 0){ // 数据库中有该条记录，且V_BBB的值早于当前处理文件的更正标识的值
				// update
				StatDi di = new StatDi();				
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(sod_code);
				di.setDATA_TYPE_1(cts_code);
				di.setTT(v_tt);			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //1成功，0失败
				di.setPROCESS_STATE("1");  //1成功，0失败			
				String sql =  "update "+StartConfig.valueTable()+" set "
						+ "V_BBB=?, D_UPDATE_TIME=?, V14311=?, V14312=?, V14313=?, V14314=?, V14315=?,"
						+ " V14316=?, V14320=?, V14311_05=?, V14021_05_052=?, V14308=?, V14312_05=?, "
						+ " V14312_05_052=?, V14312_06=?, V14312_06_052=?, V14322=?, V14313_05=?, "
						+ " V14313_05_052=?, V14309=?, V14314_05=?, V14314_05_052=?, V14306=?, V14315_05=?, "
						+ " V14315_05_052=?, V14307=?, V14316_05=?, V14316_05_052=?, V14032=?, V15483=? "
						+ " where V01301 = ? and D_Datetime = ?";
				if(connection != null){
					try{
						Pstmt = new LoggableStatement(connection, sql);
						if(StartConfig.getDatabaseType() == 1) {
							Pstmt.execute("select last_txc_xid()");
						}
						int ii = 1;
						Pstmt.setString(ii++, v_bbb);
						Pstmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						Pstmt.setBigDecimal(ii++,new BigDecimal(String.valueOf( sads.get(i).getTotalRadiationIrradiance())));
						Pstmt.setBigDecimal(ii++,new BigDecimal(String.valueOf( sads.get(i).getNetRadiationIrradiance())));
						Pstmt.setBigDecimal(ii++,new BigDecimal(String.valueOf( sads.get(i).getDirectRadiationIrradiance())));
						Pstmt.setBigDecimal(ii++,new BigDecimal(String.valueOf( sads.get(i).getScatteringRadiationIrradiance())));
						Pstmt.setBigDecimal(ii++,new BigDecimal(String.valueOf( sads.get(i).getReflectionRadiationIrradiance())));
						Pstmt.setBigDecimal(ii++,new BigDecimal(String.valueOf( sads.get(i).getUltravioletRadiationIrradiance())));
						Pstmt.setBigDecimal(ii++,new BigDecimal(String.valueOf( sads.get(i).getTotalRadiationExposure())));
						Pstmt.setBigDecimal(ii++,new BigDecimal(String.valueOf( sads.get(i).getRadiationIrradianceMax())));
						Pstmt.setBigDecimal(ii++,new BigDecimal(String.valueOf( sads.get(i).getRadiationIrradianceMaxtime())));
						Pstmt.setBigDecimal(ii++,new BigDecimal(String.valueOf( sads.get(i).getNetRadiationExposure())));
						Pstmt.setBigDecimal(ii++,new BigDecimal(String.valueOf( sads.get(i).getNetRadiationIrradiationMax())));
						Pstmt.setBigDecimal(ii++,new BigDecimal(String.valueOf( sads.get(i).getNetRadiationIrradiationMaxtime())));
						Pstmt.setBigDecimal(ii++,new BigDecimal(String.valueOf( sads.get(i).getNetRadiationIrradiationMin())));
						Pstmt.setBigDecimal(ii++,new BigDecimal(String.valueOf( sads.get(i).getNetRadiationIrradiationMintime())));
						Pstmt.setBigDecimal(ii++,new BigDecimal(String.valueOf( sads.get(i).getDirectRadiationExposure())));
						Pstmt.setBigDecimal(ii++,new BigDecimal(String.valueOf( sads.get(i).getDirectRadiationIrradianceMax())));
						Pstmt.setBigDecimal(ii++,new BigDecimal(String.valueOf( sads.get(i).getDirectRadiationIrradianceMaxtime())));
						Pstmt.setBigDecimal(ii++,new BigDecimal(String.valueOf( sads.get(i).getScatteredRadiationExposure())));
						Pstmt.setBigDecimal(ii++,new BigDecimal(String.valueOf( sads.get(i).getScatteringRadiationIrradianceMax())));
						Pstmt.setBigDecimal(ii++,new BigDecimal(String.valueOf( sads.get(i).getScatteringRadiationIrradianceMaxtime())));
						Pstmt.setBigDecimal(ii++,new BigDecimal(String.valueOf( sads.get(i).getReflectedRadiationExposure())));
						Pstmt.setBigDecimal(ii++,new BigDecimal(String.valueOf( sads.get(i).getReflectedRadiationIrradianceMax())));
						Pstmt.setBigDecimal(ii++,new BigDecimal(String.valueOf( sads.get(i).getReflectedRadiationIrradianceMaxtime())));
						Pstmt.setBigDecimal(ii++,new BigDecimal(String.valueOf( sads.get(i).getUltravioletRadiationExposure())));
						Pstmt.setBigDecimal(ii++,new BigDecimal(String.valueOf( sads.get(i).getUvIrradiationMax())));
						Pstmt.setBigDecimal(ii++,new BigDecimal(String.valueOf( sads.get(i).getUvIrradiationMaxtime())));
						Pstmt.setBigDecimal(ii++,new BigDecimal(String.valueOf( sads.get(i).getSunShineHours())));
						Pstmt.setBigDecimal(ii++,new BigDecimal(String.valueOf( sads.get(i).getAtmosphericTurbidityIndex())));	
						Pstmt.setString(ii++, sads.get(i).getStationNumberChina());	
//						Pstmt.setInt(ii++, sads.get(i).getObservationTime().getYear() + 1900); // 资料年
//						Pstmt.setInt(ii++, sads.get(i).getObservationTime().getMonth() + 1); // 资料月
//						Pstmt.setInt(ii++, sads.get(i).getObservationTime().getDate()); // 资料日
//						Pstmt.setInt(ii++, sads.get(i).getObservationTime().getHours()); //资料时
//						Pstmt.setInt(ii++, sads.get(i).getObservationTime().getMinutes()); //资料分	
						
						Pstmt.setTimestamp(ii++, new Timestamp(sads.get(i).getObservationTime().getTime()));
						
						di.setIIiii(sads.get(i).getStationNumberChina());
						di.setDATA_TIME(TimeUtil.date2String(sads.get(i).getObservationTime(), "yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setLONGTITUDE(String.valueOf(sads.get(i).getLongitude()));
						di.setLATITUDE(String.valueOf(sads.get(i).getLatitude()));
						
						di.setSEND("BFDB");
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
						di.setDATA_UPDATE_FLAG(v_bbb);
						di.setHEIGHT(String.valueOf(sads.get(i).getHeightOfSationGroundAboveMeanSeaLevel()));
						
						
						try{
							Pstmt.execute();	
							connection.commit();
							listDi.add(di);	
						}
						catch(SQLException e){							
							di.setPROCESS_STATE("0");//1成功，0失败
							listDi.add(di);		
							loggerBuffer.append("\n filename："+fileN+
									"\n " + sads.get(i).getStationNumberChina() + " " + sdf.format(sads.get(i).getObservationTime())
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
	 * @Title: findVBB
	 * @Description:(查找自动站辐射资料的更正标识)
	 * @param automaticStationRadiatingHourlyData 查找对象
	 * @param connection 数据库连接
	 * @param loggerBuffer 
	 * @return String 返回查找到的V_BBB，未找到时，返回null
	 * @throws：
	 */
	private static String findVBB(AutomaticStationRadiatingHourlyData automaticStationRadiatingHourlyData, java.sql.Connection connection, StringBuffer loggerBuffer){
		PreparedStatement Pstmt = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		ResultSet resultSet  = null;
		String rntString = null;
		// chy 去掉 D_RECORD_ID
		String sql = "select V_BBB from "+StartConfig.valueTable()+" "
				+ "where V01301=? and D_datetime=?";
		try{
			
			if(connection != null){			
				Pstmt = connection.prepareStatement(sql);
				int ii = 1;
				String primary_key = sdf.format(automaticStationRadiatingHourlyData.getObservationTime())+"_"+automaticStationRadiatingHourlyData.getStationNumberChina();
//				Pstmt.setString(ii++, primary_key);
				Pstmt.setString(ii++, automaticStationRadiatingHourlyData.getStationNumberChina());
				Pstmt.setTimestamp(ii++, new Timestamp(automaticStationRadiatingHourlyData.getObservationTime().getTime()));
				
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
	public static void main(String args[]) {
		
		File file = new File("D:\\TEMP\\D.1.2.1\\7-4\\Z_RADI_I_57494_20190702112400_O_ARS_FTM-CCB.txt");
		String fileN = file.getName();
		Date recv_time = new Date(file.lastModified());
		DecodeRadiSads decodeRadiSads = new DecodeRadiSads();
		StringBuffer loggerBuffer = new StringBuffer();
		boolean isRevised=false;
		String v_bbb = "000";
		// 判断文件是否为更正报
		if(fileN.contains("-CC")){
			isRevised = true;
			v_bbb = fileN.substring(fileN.indexOf("-CC") + 1, fileN.indexOf("-CC") + 4);
		}
		else{
			isRevised = false;
			v_bbb = "000";
		}
		ParseResult<AutomaticStationRadiatingHourlyData> parseResult = decodeRadiSads.DecodeFile(file);			
		if(parseResult.isSuccess()){
			DataBaseAction action = DbService.processSuccessReport(parseResult, fileN, recv_time,loggerBuffer,isRevised,v_bbb, file.getPath());
		}
	}
}
