package cma.cimiss2.dpc.indb.sevp.dc_6h_forcast.service;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.config.CTSCodeMap;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.sevp.Forecast6h;
import cma.cimiss2.dpc.decoder.bean.sevp.List6h;
import cma.cimiss2.dpc.decoder.sevp.DecodeForecast6h;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.sevp.ReportInfoService;

public class DbService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	public static String sod_code="";
//	static Map<String, Object> proMap = StationInfo.getProMap();	
		
	
	public static void main(String[] args) {
		DecodeForecast6h decode=new DecodeForecast6h();
		String filepath = "C:\\Users\\cuihongyuan\\Desktop\\对比\\服务类 对比验证\\大城市逐6小时精细化预报-CIMISS（业务库）与云平台入库对比验证-崔红元提交\\Z_SEVP_C_BECS_20190226224502_P_RFFC_SPCC6H_201902270000_02406.txt";
		File file = new File(filepath);
		ParseResult<Forecast6h> parseResult = decode.decodeFile(file);
		DataBaseAction action = null;
		Date recv_time = new Date();
		StringBuffer loggerBuffer = new StringBuffer();

//		if (StartConfig.getDatabaseType() == 1) {
			action = DbService.processSuccessReport(parseResult, filepath, recv_time, "024", "06", loggerBuffer, "M.0032.0002.R001");
			System.out.println("insertDBService over!");
//		}
	
	}
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}

	/**
	 * 
	 * @Title: processSuccessReport
	 * @Description:(报文解码入库函数)
	 * @param parseResult 存放解码结果
	 * @param filepath 文件路径
	 * @param recv_time 接收时间
	 * @param forecast_max  最大预报时效
	 * @param interval_max  最大时效间隔
	 * @param cts_code 
	 * @param loggerBuffer 
	 * @return DataBaseAction 数据入库状态
	 * @throws：
	 */
	public static DataBaseAction processSuccessReport(ParseResult<Forecast6h> parseResult, String fileN,
			Date recv_time, String forecast_max, String interval_max, StringBuffer loggerBuffer, String cts_code) {
		java.sql.Connection connection = null;
		java.sql.Connection reportConnection = null;
		ArrayList<CTSCodeMap> ctsCodeMaps = new ArrayList<>();
		String ctSs[] = StartConfig.ctsCodes();
		String sodSs[] = StartConfig.sodCodes();
		String reportSods[] = StartConfig.reportSodCodes();
		for(int i = 0; i < ctSs.length; i ++){
			CTSCodeMap codeMap = new CTSCodeMap();
			codeMap.setCts_code(ctSs[i]);
			codeMap.setSod_code(sodSs[i]);
			codeMap.setReport_sod_code(reportSods[i]);
			ctsCodeMaps.add(codeMap);
		}
		sod_code = CTSCodeMap.findSodByCTS(cts_code, ctsCodeMaps);
		try {
			// 获取数据库连接
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			// 获取解码结果集
			List<Forecast6h> list = parseResult.getData();
			String  correctsign = null;
			List<Forecast6h> list_normal = new ArrayList<>();//非更正报文集合
			List<Forecast6h> list_correct = new ArrayList<>();//更正报文集合
			for(int j=0;j<list.size();j++){
				correctsign = list.get(j).getCorrectSign();//用更正报标志判断是否为更正报文
				if(correctsign.equals("000")){
					list_normal.add(list.get(j));
				}else{
					list_correct.add(list.get(j));
				}
			}
			// 插入报文
			if (list_normal.size()>0) {
				DataBaseAction insertKeyDB =insertKeyDB(list_normal, connection,recv_time,forecast_max,interval_max,loggerBuffer,cts_code,fileN);//批量插入键表		
				if(insertKeyDB == DataBaseAction.SUCCESS) { // 批量插入键表成功
					DataBaseAction insertEleDB =insertEleDB(list_normal,connection,recv_time,loggerBuffer,fileN, cts_code);//插入要素表
					if (insertEleDB == DataBaseAction.BATCH_ERROR) {//批量插入要素表失败
						for (int i = 0; i < list_normal.size(); i++) { 
							DataBaseAction insertOneLine_key_db = insertOneLine_key_db(list_normal.get(i), connection, recv_time,forecast_max,interval_max,loggerBuffer,cts_code,fileN);//单条插入键表
								if(insertOneLine_key_db == DataBaseAction.SUCCESS) {//单条插入键表成功
									insertOneLine_Ele_db(list_normal.get(i) ,connection, recv_time,loggerBuffer,cts_code,fileN);//单条插入要素表
									
								}
						}
					}
				}  
				else if (insertKeyDB == DataBaseAction.BATCH_ERROR) {//批量插入键表失败
					for (int i = 0; i < list_normal.size(); i++) { 
						DataBaseAction insertOneLine_key_db = insertOneLine_key_db(list_normal.get(i), connection, recv_time,forecast_max,interval_max,loggerBuffer,cts_code,fileN);//单条插入键表
							if(insertOneLine_key_db == DataBaseAction.SUCCESS) {//单条插入键表成功
								insertOneLine_Ele_db(list_normal.get(i) ,connection, recv_time,loggerBuffer,cts_code,fileN);//单条插入要素表
								}
							}
				}
				else {
	  				return insertKeyDB;
				}	
				
			}
			//更正报文
			if (list_correct.size()>0) {
				updatekeyDB(list_correct, connection,recv_time,forecast_max,interval_max,loggerBuffer,cts_code,fileN);
			}
			
			List<ReportInfo> reportInfos = parseResult.getReports();
		    reportConnection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
		    ReportInfoService.reportInfoToDb(reportInfos, reportConnection, recv_time,sod_code);
			return DataBaseAction.SUCCESS;
		}catch (Exception e) {
			loggerBuffer.append("\n Database connection error!");
			return DataBaseAction.CONNECTION_ERROR;
		}
		finally {
			
			for (int j = 0; j < listDi.size(); j++) {
				diQueues.offer(listDi.get(j));
			}
			listDi.clear();
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n Close databasse connection error: "+e.getMessage());
				}
			}
			
			if(reportConnection != null) {
				try {
					reportConnection.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n Close databasse connection error: "+e.getMessage());
				}
			}
		}
	}

	/**
	 * 
	 * @Title: insertKeyDB
	 * @Description:(大城市逐6小时精细化预报资料入键表（批量）)
	 * @param list_normal 入库对象集合
	 * @param connection 数据库连接
	 * @param recv_time 接收时间
	 * @param forecast_max 最大预报时效
	 * @param interval_max 最大时效间隔
	 * @param cts_code 
	 * @param loggerBuffer 
	 * @param fileN 
	 * @return DataBaseAction 数据入库状态
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static DataBaseAction insertKeyDB(List<Forecast6h> list_normal, java.sql.Connection connection,Date recv_time, String forecast_max, String interval_max, StringBuffer loggerBuffer, String cts_code, String fileN) {
		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement pStatement = null;
		try {
			String sql = "INSERT INTO "+StartConfig.keyTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V_BBB,V01301,V01300,V05001,"
					+ "V06001,V07001,V_ACODE,V04001_02,V04002_02,V04003_02,V04004_02,V04001,V04002,V04003,V04004,V04005,V_CCCC,"
					+ "V_PROD_DESC,V_PROD_CODE,V04320_005,V04320_005_080,V04320_041,V_PROD_NUM,D_SOURCE_ID) VALUES("
					+"?, ?, ?, ?, ?, ?, ?, ?, ?,?,"
					+"?, ?, ?, ?, ?, ?, ?, ?, ?,?,"
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			if(connection != null){
				connection.setAutoCommit(false);
				// 创建PreparedStatement
				pStatement = connection.prepareStatement(sql); 
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				for(int i=0;i<list_normal.size();i++){
					Forecast6h forecast6h = list_normal.get(i);
					// 设置DI信息
					StatDi di = new StatDi();					
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(sod_code);
					di.setDATA_TYPE_1(cts_code);
					di.setTT("FS");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1");  //1成功，0失败	
					// 根根配置文件  获取站点基本信息
					String info = (String) proMap.get(forecast6h.getStationNumberChina() + "+01");
					int adminCode = 999999;
					if(info == null) {
						loggerBuffer.append("\n In configuration file, this station does not exist: " + forecast6h.getStationNumberChina());			
					}else{			
						String[] infos = info.split(",");
						if(!(infos[5].trim().equals("null") || infos[5].trim().equals(""))){
							adminCode = Integer.parseInt(infos[5]);
						}
					}		
					int ii=1;
					String primkey = sdf.format(forecast6h.getObservationTime()) + "_" + sdf.format(forecast6h.getForecastTime())+"_"+forecast6h.getStationNumberChina()+"_"+forecast6h.getProd_Code();
				    pStatement.setString(ii++, primkey);//记录标识
					pStatement.setString(ii++,sod_code);//资料标识
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//入库时间
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));//收到时间
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间
					pStatement.setTimestamp(ii++, new Timestamp(forecast6h.getObservationTime().getTime()));//资料时间
					pStatement.setString(ii++, forecast6h.getCorrectSign());//更正标识
					pStatement.setString(ii++, forecast6h.getStationNumberChina());//区站号(字符)
					pStatement.setInt(ii++,Integer.parseInt(StationCodeUtil.stringToAscii(forecast6h.getStationNumberChina())) );//区站号(数字)
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(forecast6h.getLatitude())));//纬度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(forecast6h.getLongitude())));//经度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(forecast6h.getHeightOfSationGroundAboveMeanSeaLevel())));//测站海拔高度
					pStatement.setInt(ii++, adminCode);//中国行政区代码
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(forecast6h.getObservationTime().getYear() + 1900)));//预报年
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(forecast6h.getObservationTime().getMonth()+1)));//预报月
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(forecast6h.getObservationTime().getDate())));//预报日
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(forecast6h.getObservationTime().getHours())));//预报时
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(forecast6h.getForecastTime().getYear() + 1900)));//预报产生年
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(forecast6h.getForecastTime().getMonth()+1)));//预报产生月
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(forecast6h.getForecastTime().getDate())));//预报产生日
					pStatement.setInt(ii++, forecast6h.getForecastTime().getHours());//预报产生时
					pStatement.setInt(ii++, forecast6h.getForecastTime().getMinutes());//预报产生分
					pStatement.setString(ii++, forecast6h.getBul_Center());//编报(加工)中心
					pStatement.setString(ii++, forecast6h.getPROD_DESC());//产品描述
					pStatement.setString(ii++, forecast6h.getProd_Code());//产品代码
					pStatement.setString(ii++, forecast_max);//最大预报时效
					pStatement.setString(ii++, interval_max);//最大时效间隔
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(forecast6h.getValidtime_Count())));//预报时效个数
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(forecast6h.getV_PROD_NUM())));//预报产品个数
					pStatement.setString(ii++, cts_code);
					
					di.setIIiii(forecast6h.getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(forecast6h.getForecastTime(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLATITUDE(String.valueOf(forecast6h.getLatitude()));
					di.setLONGTITUDE(String.valueOf(forecast6h.getLongitude()));
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(fileN).length()));
					di.setDATA_UPDATE_FLAG(forecast6h.getCorrectSign());
					di.setHEIGHT(String.valueOf(forecast6h.getHeightOfSationGroundAboveMeanSeaLevel()));
					
					// 批量提交
					pStatement.addBatch();		
					listDi.add(di);	
				}			
				try {
					// 执行批量			
					pStatement.executeBatch();
					// 手动提交			
					//connection.commit();
					loggerBuffer.append(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
					loggerBuffer.append(" INSERT SUCCESS COUNT ：" + list_normal.size() + "\n");
					return DataBaseAction.SUCCESS;
				} catch (Exception e) {
					pStatement.clearParameters();
					pStatement.clearBatch();			
					loggerBuffer.append(" Batch ERROR : " + sdf.format(new Date()) + "\n");
					return DataBaseAction.BATCH_ERROR;
				}			
			} else {
				loggerBuffer.append("\n Database connection error!");
				return DataBaseAction.CONNECTION_ERROR;
			}														
		} catch (SQLException e) {		
			loggerBuffer.append("\n Database connection error!"+e.getMessage());
			return DataBaseAction.CONNECTION_ERROR;
		}finally {
			try {
				if(pStatement != null)
					pStatement.close();
			} catch (SQLException e) {
				loggerBuffer.append("\n Close statement error: "+e.getMessage());
			}			
		}
	}
	/**
	 * 
	 * @Title: insertOneLine_key_db
	 * @Description:(大城市逐6小时精细化预报资料键表（单条）)
	 * @param fine_forecast 入库对象集合
	 * @param connection 数据库连接
	 * @param recv_time 接收时间
	 * @param forecast_max 最大预报时效
	 * @param interval_max 最大时效间隔
	 * @param cts_code 
	 * @param loggerBuffer 
	 * @param fileN 
	 * @return DataBaseAction 数据入库状态
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static DataBaseAction insertOneLine_key_db(Forecast6h Forecast6h, java.sql.Connection connection,
			Date recv_time, String forecast_max, String interval_max, StringBuffer loggerBuffer, String cts_code, String fileN) {
		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.keyTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V_BBB,V01301,V01300,V05001,"
				+ "V06001,V07001,V_ACODE,V04001_02,V04002_02,V04003_02,V04004_02,V04001,V04002,V04003,V04004,V04005,V_CCCC,"
				+ "V_PROD_DESC,V_PROD_CODE,V04320_005,V04320_005_080,V04320_041,V_PROD_NUM,D_SOURCE_ID) VALUES("
				+"?, ?, ?, ?, ?, ?, ?, ?, ?,?,"
				+"?, ?, ?, ?, ?, ?, ?, ?, ?,?,"
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		if(connection !=null){
			try {
				connection.setAutoCommit(false);
				pStatement = new LoggableStatement(connection, sql); 
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				StatDi di = new StatDi();				
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(sod_code);
				di.setDATA_TYPE_1(cts_code);
				di.setTT("FS");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //1成功，0失败
				di.setPROCESS_STATE("1");  //1成功，0失败
				int adminCode = 999999;
				String info = (String) proMap.get(Forecast6h.getStationNumberChina() + "+01");			
				if(info == null) {
					loggerBuffer.append("\n In configuration file, this station does not exist: " + Forecast6h.getStationNumberChina());
				}else{
					String[] infos = info.split(",");
					if(!(infos[5].trim().equals("null") || infos[5].trim().equals(""))){
						adminCode = Integer.parseInt(infos[5]);
					}
				}		
			    int ii=1;
			    String primkey = sdf.format(Forecast6h.getObservationTime()) + "_" + sdf.format(Forecast6h.getForecastTime()) +"_"+Forecast6h.getStationNumberChina()+"_"+Forecast6h.getProd_Code();
			    pStatement.setString(ii++, primkey);//记录标识
				pStatement.setString(ii++,sod_code);//资料标识
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//入库时间
				pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));//收到时间
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间
				pStatement.setTimestamp(ii++, new Timestamp(Forecast6h.getObservationTime().getTime()));//资料时间	
				pStatement.setString(ii++, Forecast6h.getCorrectSign());//更正标识
				pStatement.setString(ii++, Forecast6h.getStationNumberChina());//区站号(字符)
				pStatement.setInt(ii++,Integer.parseInt(StationCodeUtil.stringToAscii(Forecast6h.getStationNumberChina())) );//区站号(数字)
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(Forecast6h.getLatitude())));//纬度
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(Forecast6h.getLongitude())));//经度
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(Forecast6h.getHeightOfSationGroundAboveMeanSeaLevel())));//测站海拔高度
				pStatement.setInt(ii++, adminCode);//中国行政区代码
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(Forecast6h.getObservationTime().getYear() + 1900)));//预报年
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(Forecast6h.getObservationTime().getMonth()+1)));//预报月
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(Forecast6h.getObservationTime().getDate())));//预报日
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(Forecast6h.getObservationTime().getHours())));//预报时
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(Forecast6h.getForecastTime().getYear() + 1900)));//预报产生年
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(Forecast6h.getForecastTime().getMonth()+1)));//预报产生月
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(Forecast6h.getForecastTime().getDate())));//预报产生日
				pStatement.setInt(ii++, Forecast6h.getForecastTime().getHours());//预报产生时
				pStatement.setInt(ii++, Forecast6h.getForecastTime().getMinutes());//预报产生分
				pStatement.setString(ii++, Forecast6h.getBul_Center());//编报(加工)中心
				pStatement.setString(ii++, Forecast6h.getPROD_DESC());//产品描述
				pStatement.setString(ii++, Forecast6h.getProd_Code());//产品代码
				pStatement.setString(ii++, forecast_max);//最大预报时效
				pStatement.setString(ii++, interval_max);//最大时效间隔
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(Forecast6h.getValidtime_Count())));//预报时效个数
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(Forecast6h.getV_PROD_NUM())));//预报产品个数
				pStatement.setString(ii++, cts_code);
				
				di.setIIiii(Forecast6h.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(Forecast6h.getForecastTime(), "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());
				di.setLATITUDE(String.valueOf(Forecast6h.getLatitude()));
				di.setLONGTITUDE(String.valueOf(Forecast6h.getLongitude()));
				
				di.setSEND("BFDB");
				di.setSEND_PHYS("DRDS");
				di.setFILE_SIZE(String.valueOf(new File(fileN).length()));
				di.setDATA_UPDATE_FLAG(Forecast6h.getCorrectSign());
				di.setHEIGHT(String.valueOf(Forecast6h.getHeightOfSationGroundAboveMeanSeaLevel()));
				
				try {
					pStatement.execute();
					listDi.add(di);
					return DataBaseAction.SUCCESS;
					
				} catch (SQLException e) {
					di.setPROCESS_STATE("0");//0成功，1失败
					listDi.add(di);	
					loggerBuffer.append("\n File name: "+fileN+
							"\n " + Forecast6h.getStationNumberChina() + " " + sdf.format(Forecast6h.getObservationTime())
							+"\n execute sql error: "+((LoggableStatement)pStatement).getQueryString()+"\n "+e.getMessage());
					return DataBaseAction.INSERT_ERROR;
				}
			}catch (SQLException e) {
				loggerBuffer.append("\n Create Statement error: "+e.getMessage());
				return DataBaseAction.INSERT_ERROR;
			}finally {
				if(pStatement != null) {
					try {
						pStatement.close();
					} catch (SQLException e) {
						e.printStackTrace();
						loggerBuffer.append("\n Close Statement error: "+e.getMessage());
						
					}
				}	
			}
		}else {
			loggerBuffer.append("\n Database connection error!");
			return DataBaseAction.CONNECTION_ERROR;
		}	
		
	}

/**
 * 
 * @Title: insertEleDB
 * @Description:(精细化预报入要素表（批量）)
 * @param list_normal 入库对象集合
 * @param connection 
 * @param connection_ele 
 * @param recv_time 接收时间
 * @param cts_code 
 * @param loggerBuffer 
 * @param fileN 
 * @return DataBaseAction 数据入库状态
 * @throws：
 */
	private static  DataBaseAction insertEleDB(List<Forecast6h> list_normal, java.sql.Connection connection, Date recv_time, StringBuffer loggerBuffer, String fileN,String cts_code) {
		PreparedStatement pStatement = null;
		//java.sql.Connection connection_ele = null;
		try {
			//connection_ele = ConnectionPoolFactory.getInstance().getConnection("rdb");
			// sql 语句
			String sql = "INSERT INTO "+StartConfig.valueTable()+" (D_ELE_ID,D_RECORD_ID,D_UPDATE_TIME,D_DATETIME,V01301,V01300,V04320,V20312_06,"
				+ "V12011_06,V12012_06,V11001_06,V11040_06,V13021,V_BBB,D_SOURCE_ID) VALUES ("
					+ "?,?, ?, ?, ?, ?, ?,?, ?, ?,"
					+ "?,?,?,?,?)";
			if(connection !=null){
				connection.setAutoCommit(false);
				pStatement = connection.prepareStatement(sql); 
				//if(StartConfig.getDatabaseType() == 1) {
				//	pStatement.execute("select last_txc_xid()");
				//}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				for(int i =0; i<list_normal.size();i++){
					Forecast6h forecast6h = list_normal.get(i);
					// 获取要素行数
					for (int j = 0; j < forecast6h.getList6h().size(); j++) {
						List6h list6h = forecast6h.getList6h().get(j);
						int ii=1;
						String primkey = sdf.format(forecast6h.getObservationTime())+"_" + sdf.format(forecast6h.getForecastTime())+"_"+forecast6h.getStationNumberChina()+"_"+forecast6h.getProd_Code();
						pStatement.setString(ii++, primkey+"_"+list6h.getValidtime());//要素标识
						pStatement.setString(ii++, primkey);//记录标识
						pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间
						pStatement.setTimestamp(ii++, new Timestamp(forecast6h.getObservationTime().getTime()));//资料时间
						pStatement.setString(ii++, forecast6h.getStationNumberChina());//站号
						pStatement.setInt(ii++,Integer.parseInt(StationCodeUtil.stringToAscii(forecast6h.getStationNumberChina())) );//区站号(数字)
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list6h.getValidtime())));//预报时效
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list6h.getWEP_Past_6h())));//6小时内天气现象
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list6h.getTEM_Max_6h())));//6小时最高温度
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list6h.getTEM_Min_6h())));//6小时最低温度
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list6h.getWIN_PD_6h())));//6小时内盛行风向
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list6h.getWIN_S_Max_6h())));//6小时内最大风速
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list6h.getPRE_6h())));//过去6小时降水量
						pStatement.setString(ii++, forecast6h.getCorrectSign());//更正标志
						pStatement.setString(ii++, cts_code);
						//批量提交
						pStatement.addBatch();
					}
				}	
				try {
					pStatement.executeBatch();
					connection.commit();
					return DataBaseAction.SUCCESS;
				} catch (Exception e) {
					connection.rollback();
					pStatement.clearParameters();
					pStatement.clearBatch();
					loggerBuffer.append("\n Element table batch commit failed: "+fileN );
					return DataBaseAction.BATCH_ERROR;
				}
			}else {
				loggerBuffer.append("\n  Database connection error!");
				return DataBaseAction.CONNECTION_ERROR;
			}
		} catch (Exception e) {
			loggerBuffer.append("\n Create PreparedStatement error: " + e.getMessage());
			return DataBaseAction.CONNECTION_ERROR;
		}finally {
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
	 * 
	 * @Title: insertOneLine_Ele_db
	 * @Description:(大城市逐6小时精细化预报资料入要素表（单条）)
	 * @param fine_list 入库对象集合
	 * @param connection 数据库连接
	 * @param recv_time void  接收时间
	 * @param loggerBuffer 
	 * @param cts_code 
	 * @param cts_code 
	 * @param fileN 
	 * @throws：
	 */
	private static void insertOneLine_Ele_db(Forecast6h forecast6h , java.sql.Connection connection, Date recv_time, StringBuffer loggerBuffer, String cts_code, String fileN) {
		PreparedStatement pStatement = null;
		// sql 语句
		String sql = "INSERT INTO "+StartConfig.valueTable()+" (D_ELE_ID,D_RECORD_ID,D_UPDATE_TIME,D_DATETIME,V01301,V01300,V04320,V20312_06,"
				+ "V12011_06,V12012_06,V11001_06,V11040_06,V13021,V_BBB,D_SOURCE_ID) VALUES ("
					  + "?,?, ?, ?, ?, ?, ?,?, ?, ?,"
					  + "?,?,?,?,?)";
		if(connection !=null){
			try {
				connection.setAutoCommit(false);
				pStatement = connection.prepareStatement(sql); 
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				for (int i = 0; i < forecast6h.getList6h().size(); i++) {
					List6h  list6h= forecast6h.getList6h().get(i);
					int ii=1;
					String primkey = sdf.format(forecast6h.getObservationTime())+"_"+sdf.format(forecast6h.getForecastTime())+"_"+forecast6h.getStationNumberChina()+"_"+forecast6h.getProd_Code();
					pStatement.setString(ii++, primkey+"_"+list6h.getValidtime());//要素标识
					pStatement.setString(ii++, primkey);//记录标识
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间
					pStatement.setTimestamp(ii++, new Timestamp(forecast6h.getObservationTime().getTime()));//资料时间
					pStatement.setString(ii++, forecast6h.getStationNumberChina());//站号
					pStatement.setInt(ii++,Integer.parseInt(StationCodeUtil.stringToAscii(forecast6h.getStationNumberChina())) );//区站号(数字)
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list6h.getValidtime())));//预报时效
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list6h.getWEP_Past_6h())));//6小时内天气现象
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list6h.getTEM_Max_6h())));//6小时最高温度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list6h.getTEM_Min_6h())));//6小时最低温度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list6h.getWIN_PD_6h())));//6小时内盛行风向
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list6h.getWIN_S_Max_6h())));//6小时内最大风速
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list6h.getPRE_6h())));//过去6小时降水量
					pStatement.setString(ii++, forecast6h.getCorrectSign());//更正标志	
					pStatement.setString(ii++, cts_code);
					pStatement.addBatch();	
				}
				try {
					pStatement.executeBatch();
					connection.commit();
				} catch (Exception e) {
					connection.rollback();
					loggerBuffer.append("\n File name: "+fileN+
							"\n " + forecast6h.getStationNumberChina() + " " + sdf.format(forecast6h.getObservationTime())
							+"\n "+e.getMessage());
				}
			}catch (Exception e) {
				loggerBuffer.append("\n Create PreparedStatement error: " +e.getMessage());
			}finally {
				if(pStatement != null) {
					try {
						pStatement.close();
						pStatement = null;
					} catch (SQLException e) {
						loggerBuffer.append("\n Close statement error!");
					}
				}
			}
		}else {
			loggerBuffer.append("\n Database connection error!");
		}	
	}


	
	/**
	 * 
	 * @Title: updatekeyDB
	 * @Description:(更新键表)
	 * @param list_correct 更新对象集合
	 * @param connection 数据库连接
	 * @param recv_time 接收时间
	 * @param forecast_max 最大预报时效
	 * @param interval_max 最大时效间隔
	 * @param cts_code 
	 * @param loggerBuffer 
	 * @param fileN 
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static void updatekeyDB(List<Forecast6h> list_correct, java.sql.Connection connection, Date recv_time, String forecast_max, String interval_max, StringBuffer loggerBuffer, String cts_code, String fileN) {
		PreparedStatement Pstat = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		//list
		//List<Forecast6h> listTable = new ArrayList<Forecast6h>();//加入KEY表的数据行主键的集合
		for(int i=0;i<list_correct.size();i++){
			String vbbbInDB = null;
			//插入前，从DB中查找该条记录的状态，有、无、更正状态
			if(connection!=null){
				vbbbInDB = findVBBB_key(list_correct.get(i),connection,loggerBuffer);
			}
			if(vbbbInDB == null){//该更正报之前数据库中没有该条记录，直接插入,给UPDATE_TIME赋值
				DataBaseAction  updatekey =insertOneLine_key_db(list_correct.get(i), connection, recv_time,forecast_max,interval_max,loggerBuffer,cts_code,fileN);	
				if (updatekey==DataBaseAction.SUCCESS) {
					insertOneLine_Ele_db(list_correct.get(i), connection, recv_time,loggerBuffer,cts_code,fileN);				
				}			
			}else if (vbbbInDB.compareTo(list_correct.get(i).getCorrectSign())<0) {
				StatDi di = new StatDi();
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(sod_code);
				di.setDATA_TYPE_1(cts_code);
				di.setTT("FS");
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //1成功，0失败
				di.setPROCESS_STATE("1");  //1成功，0失败	
//				String sql ="UPDATE "+StartConfig.keyTable()+" SET"
//						+ " V_BBB=?,D_UPDATE_TIME=?"
//						+ " WHERE V01301=? AND V04001_02=?"
//						+ " AND V04002_02=? AND V04003_02=? AND V04004_02=?"
//						+ " AND V04001=? AND V04002=? AND V04003=? AND V04004=? AND V04005=? AND V_PROD_CODE=? ";
				String sql ="UPDATE "+StartConfig.keyTable()+" SET"
						+ " V_BBB=?,D_UPDATE_TIME=?"
						+ " WHERE V01301=? AND D_datetime=?"
						+ " AND V04001=? AND V04002=? AND V04003=? AND V04004=? AND V04005=? AND V_PROD_CODE=? ";
				if(connection != null){
					try {
						connection.setAutoCommit(false);
						Pstat = new LoggableStatement(connection, sql);
						if(StartConfig.getDatabaseType() == 1) {
							Pstat.execute("select last_txc_xid()");
						}
						int ii = 1;
//						int strYear = list_correct.get(i).getObservationTime().getYear() + 1900;
//						int strMonth = list_correct.get(i).getObservationTime().getMonth() + 1;
//						int strDate = list_correct.get(i).getObservationTime().getDate();
						
						int Year = list_correct.get(i).getForecastTime().getYear() + 1900;
						int Month = list_correct.get(i).getForecastTime().getMonth() + 1;
						int Date = list_correct.get(i).getForecastTime().getDate();
						
						Pstat.setString(ii++, list_correct.get(i).getCorrectSign());//更正标识
						Pstat.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间			
						Pstat.setString(ii++,list_correct.get(i).getStationNumberChina());//站号
//						Pstat.setInt(ii++, strYear);//资料观测年
//						Pstat.setInt(ii++, strMonth);//资料观测月
//						Pstat.setInt(ii++, strDate);//资料观测月
//						Pstat.setInt(ii++, list_correct.get(i).getObservationTime().getHours());//资料观测时
						
						Pstat.setTimestamp(ii++, new Timestamp(list_correct.get(i).getObservationTime().getTime()));
						
						// add on 2019-02-26
						Pstat.setInt(ii++, Year);
						Pstat.setInt(ii++, Month);
						Pstat.setInt(ii++, Date);
						Pstat.setInt(ii++, list_correct.get(i).getForecastTime().getHours());
						Pstat.setInt(ii++, list_correct.get(i).getForecastTime().getMinutes());
						
						Pstat.setString(ii++, list_correct.get(i).getProd_Code());//产品描述
						di.setIIiii(list_correct.get(i).getStationNumberChina());
						di.setDATA_TIME(TimeUtil.date2String(list_correct.get(i).getForecastTime(), "yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setLATITUDE(String.valueOf(list_correct.get(i).getLatitude()));
						di.setLONGTITUDE(String.valueOf(list_correct.get(i).getLongitude()));
						
						di.setSEND("BFDB");
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(new File(fileN).length()));
						di.setDATA_UPDATE_FLAG(list_correct.get(i).getCorrectSign());
						di.setHEIGHT(String.valueOf(list_correct.get(i).getHeightOfSationGroundAboveMeanSeaLevel()));
						
						try{
							Pstat.execute();	
							//connection.commit();
							listDi.add(di);
							//更新要素表
							updateEle(list_correct.get(i),recv_time,connection,loggerBuffer,cts_code,fileN);
							
							//listTable.add(list_correct.get(i));
						}
						catch(SQLException e){							
							di.setPROCESS_STATE("0");//1成功，0失败
							loggerBuffer.append("\n File name: "+fileN+
									"\n " + list_correct.get(i).getStationNumberChina() + " " + sdf.format(list_correct.get(i).getObservationTime())
									+"\n execute sql error: "+((LoggableStatement)Pstat).getQueryString()+"\n "+e.getMessage());
						}				
					}catch (SQLException e) {
						loggerBuffer.append("\n Create Statement error: "+ e.getMessage());
					}finally {
						try {
							if(Pstat != null)
								Pstat.close();
						} catch (SQLException e) {
							loggerBuffer.append("\n Close Statement error: "+ e.getMessage());
						}	
					}
				}else
					loggerBuffer.append("\n Database connection error!");		
			} // end else if	
			else{ // 数据库中有该条记录，且V_BBB的值晚于当前处理文件的更正标识的值
			// 不进行插入或更新	
			}
		} // end for
		/*for(int j=0;j<listTable.size();j++){//更新要素表
			updateEle(listTable.get(j),recv_time,connection,loggerBuffer,cts_code);
		}*/
	}
	/**
	 * 
	 * @Title: updateEle
	 * @Description:(更新要素表)
	 * @param fine 更新对象集合
	 * @param recv_time 接收时间
	 * @param connection 数据库连接
	 * @param cts_code 
	 * @param loggerBuffer 
	 * @param cts_code 
	 * @param loggerBuffer 
	 * @param fileN 
	 * @throws：
	 */
	//@SuppressWarnings("unused")
	private static void updateEle(Forecast6h forecast6h,Date recv_time ,java.sql.Connection connection, StringBuffer loggerBuffer, String cts_code, String fileN) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");				 
        PreparedStatement pStatement = null;
//		String sql ="UPDATE "+StartConfig.valueTable()+" SET"
//				+ " V_BBB=?, D_UPDATE_TIME=?, V04320=?, V20312_06=?, V12011_06=?, V12012_06=?, "
//				+ "V11001_06=?, V11040_06=?, V13021=?"
//				+ "	WHERE D_RECORD_ID=? AND V04320=? ";
        String sql ="UPDATE "+StartConfig.valueTable()+" SET"
				+ " V_BBB=?, D_UPDATE_TIME=?, V04320=?, V20312_06=?, V12011_06=?, V12012_06=?, "
				+ "V11001_06=?, V11040_06=?, V13021=?"
				+ "	WHERE D_RECORD_ID=? AND V04320=? and D_DATETIME=?";
		if(connection != null){
			try{
				connection.setAutoCommit(false);
				pStatement = new LoggableStatement(connection, sql);
				int list_size = forecast6h.getList6h().size();
				for(int i = 0; i < list_size; i ++){
					int ii = 1;
					List6h list6h =forecast6h.getList6h().get(i); 
					pStatement.setString(ii++, forecast6h.getCorrectSign());//更正标识
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间			
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list6h.getValidtime())));//预报时效
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list6h.getWEP_Past_6h())));//6小时内天气现象
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list6h.getTEM_Max_6h())));//6小时最高温度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list6h.getTEM_Min_6h())));//6小时最低温度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list6h.getWIN_PD_6h())));//6小时内盛行风向
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list6h.getWIN_S_Max_6h())));//6小时内最大风速
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list6h.getPRE_6h())));//过去6小时降水量
					String primary_key = sdf.format(forecast6h.getObservationTime())+"_"+sdf.format(forecast6h.getForecastTime())+"_"+forecast6h.getStationNumberChina()+"_"+forecast6h.getProd_Code();
					pStatement.setString(ii++,primary_key);//记录标识
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(list6h.getValidtime())));//预报时效
					
					pStatement.setTimestamp(ii++, new Timestamp(forecast6h.getObservationTime().getTime()));
					
					pStatement.addBatch();
				}try{
					pStatement.executeBatch();
					connection.commit();
				}catch(SQLException e){	
					connection.rollback();
					loggerBuffer.append("\n File name: "+fileN+
							"\n " + forecast6h.getStationNumberChina() + " " + sdf.format(forecast6h.getObservationTime())
							+"\n execute sql error: "+((LoggableStatement)pStatement).getQueryString()+"\n "+e.getMessage());
				}					
			}catch (SQLException e) {
				loggerBuffer.append("\n Create Statement error: "+ e.getMessage());
			}finally {
				try {
					if(pStatement != null)
						pStatement.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n Close Statement error: "+ e.getMessage());
				}	
			}
		}else
			loggerBuffer.append("\n Database connection error!");				
	}
	
	/**
	 * @param loggerBuffer 
	 * @Title: findVBBB_key 
	 * @Description:(更正报文  需要查询原始报文是否存在) 
	 * @param connection  数据库连接对象
	 * @return  
	 *    String  更正标识  如果原始报文不存在 则返回  null
	 * @throws
	 */
	@SuppressWarnings("deprecation")
	private static String findVBBB_key(Forecast6h forecast6h, java.sql.Connection connection, StringBuffer loggerBuffer) {
		PreparedStatement Pstmt = null;
		ResultSet resultSet = null;
		String rntString = null;
		String sql ="SELECT V_BBB FROM "+StartConfig.keyTable()+" "			
				+ " WHERE V01301 = ? AND V04001_02 = ?"
				+ " AND V04002_02 = ? AND V04003_02 = ? AND V04004_02 = ? AND V04001 = ?"
				+ " AND V04002 = ? AND V04003 = ? AND V04004 = ? AND V04005=? AND V_PROD_CODE=?";
		try{	
			
			if(connection != null){
				Pstmt = connection.prepareStatement(sql);
				int ii = 1;		
				Date date = forecast6h.getForecastTime();
				int strYear =forecast6h.getObservationTime().getYear() + 1900;
				int strMonth = forecast6h.getObservationTime().getMonth() + 1;
				int strDate = forecast6h.getObservationTime().getDate();
				Pstmt.setString(ii++, forecast6h.getStationNumberChina());//站号
				Pstmt.setInt(ii++, strYear);//资料观测年
				Pstmt.setInt(ii++, strMonth);//资料观测月
				Pstmt.setInt(ii++, strDate);//资料观测日
				Pstmt.setInt(ii++, forecast6h.getObservationTime().getHours());//资料观测时
				
				Pstmt.setInt(ii++, date.getYear() + 1900);
				Pstmt.setInt(ii++, date.getMonth() +1 );
				Pstmt.setInt(ii++, date.getDate());
				Pstmt.setInt(ii++, date.getHours());
				Pstmt.setInt(ii++, date.getMinutes());
				
				Pstmt.setString(ii++, forecast6h.getProd_Code());//产品种类
				resultSet = Pstmt.executeQuery();
				if(resultSet.next())
					rntString= resultSet.getString(1);
			}
		}catch(SQLException e){
			loggerBuffer.append("\n Create PreparedStatement error: " + e.getMessage());
		}finally {
			if(Pstmt != null) {
				try {
					Pstmt.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n Close PreparedStatement error: " + e.getMessage());
				}
			}
			if(resultSet != null){
				try{
					resultSet.close();
				}
				catch (Exception e) {
					loggerBuffer.append("close resultset error");
				}
			}
		}
		return rntString;	
	}
}
