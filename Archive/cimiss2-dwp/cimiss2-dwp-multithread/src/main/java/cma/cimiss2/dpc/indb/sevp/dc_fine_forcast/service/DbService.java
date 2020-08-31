package cma.cimiss2.dpc.indb.sevp.dc_fine_forcast.service;

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
import cma.cimiss2.dpc.decoder.bean.sevp.FineForecast;
import cma.cimiss2.dpc.decoder.bean.sevp.FineList;
import cma.cimiss2.dpc.decoder.sevp.DecodeFineForecast;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.sevp.ReportInfoService;

public class DbService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	public static String sod_code="";
//	static Map<String, Object> proMap = StationInfo.getProMap();		
	
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}

	public static void main(String[] args) {
		DecodeFineForecast decode=new DecodeFineForecast();
		String filepath = "D:\\TEMP\\M.12.1.1\\5-21\\Z_SEVP_C_BEHF_20190519224501_P_RFFC-SPCC-201905200000-16812.TXT";
		File file = new File(filepath);
		ParseResult<FineForecast> parseResult = decode.decodeFile(file);
		DataBaseAction action = null;
		Date recv_time = new Date(file.lastModified());
		StringBuffer loggerBuffer = new StringBuffer();

//		if (StartConfig.getDatabaseType() == 1) {
			action = DbService.processSuccessReport(parseResult, filepath, recv_time, "168", "12", "M.0012.0002.R001", loggerBuffer);
			System.out.println("insertDBService over!");
//		}
	
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
	public static DataBaseAction processSuccessReport(ParseResult<FineForecast> parseResult, String fileN,
			Date recv_time, String forecast_max, String interval_max, String cts_code, StringBuffer loggerBuffer) {
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
			List<FineForecast> list = parseResult.getData();
			String  correctsign = null;
			List<FineForecast> list_normal = new ArrayList<FineForecast>();//非更正报文集合
			List<FineForecast> list_correct = new ArrayList<FineForecast>();//更正报文集合
			for(int j=0;j<list.size();j++){
				correctsign = list.get(j).getCorrectSign();//用更正报标志判断是否为更正报文
				if(correctsign.equals("000")){
					list_normal.add(list.get(j));
				}else{
					list_correct.add(list.get(j));
				}
			}
			
			for (int n = 0; n < list_normal.size()/30+1; n++) {
				List<FineForecast> sublist = null;
				if(n * 30 + 30 > list_normal.size()) {
					sublist = list_normal.subList(n*30, list_normal.size());
				}else {
					sublist = list_normal.subList(n*30, n*30+30);
				}
				// 插入报文
				if(sublist.size() > 0){
					DataBaseAction insertKeyDB =insertKeyDB(sublist, connection,recv_time,forecast_max,interval_max,cts_code,loggerBuffer,fileN);//批量插入键表		
					if(insertKeyDB == DataBaseAction.SUCCESS) { // 批量插入键表成功
						DataBaseAction insertEleDB =insertEleDB(sublist,connection,recv_time,loggerBuffer,fileN,cts_code);//插入要素表
						if (insertEleDB == DataBaseAction.BATCH_ERROR) {//批量插入要素表失败
							for (int i = 0; i < sublist.size(); i++) { 
								DataBaseAction insertOneLine_key_db = insertOneLine_key_db(sublist.get(i), connection, recv_time,forecast_max,interval_max,loggerBuffer,cts_code,fileN);//单条插入键表
									if(insertOneLine_key_db == DataBaseAction.SUCCESS) {//单条插入键表成功
										insertOneLine_Ele_db(sublist.get(i) ,connection, recv_time,loggerBuffer,fileN,cts_code);//单条插入要素表
										
									}
									
							}
						}
					}  
					else if (insertKeyDB == DataBaseAction.BATCH_ERROR) {//批量插入键表失败
						for (int i = 0; i < sublist.size(); i++) { 
							DataBaseAction insertOneLine_key_db = insertOneLine_key_db(sublist.get(i), connection, recv_time,forecast_max,interval_max,loggerBuffer,cts_code,fileN);//单条插入键表
								if(insertOneLine_key_db == DataBaseAction.SUCCESS) 
								{//单条插入键表成功
									insertOneLine_Ele_db(sublist.get(i) ,connection, recv_time,loggerBuffer,fileN,cts_code);//单条插入要素表
									
								}
								
						}
					}
					else {
		  				return insertKeyDB;
					}	
				}
			}
			
			//更正报文
			if(list_correct.size() > 0){
				updatekeyDB(list_correct, connection,recv_time,forecast_max,interval_max,loggerBuffer,cts_code,fileN);
			}
			//更正报文
			
			@SuppressWarnings("rawtypes")
			List<ReportInfo> reportInfos = parseResult.getReports();
		    reportConnection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
		    ReportInfoService.reportInfoToDb(reportInfos, reportConnection, recv_time,sod_code);
			return DataBaseAction.SUCCESS;
		}catch (Exception e) {
			loggerBuffer.append("\n Database connection error!");
			return DataBaseAction.CONNECTION_ERROR;
		}finally {
			
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
	 * @Description:(精细化预报入键表（批量）)
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
	private static DataBaseAction insertKeyDB(List<FineForecast> list_normal, java.sql.Connection connection,Date recv_time, String forecast_max, String interval_max, String cts_code, StringBuffer loggerBuffer, String fileN) {
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
					FineForecast fine = list_normal.get(i);
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
					String info = (String) proMap.get(fine.getStationNumberChina() + "+01");
					int adminCode = 999999;
					if(info == null) {
						loggerBuffer.append("\n In configuration file, this station does not exist: " + fine.getStationNumberChina());			
					}else{			
						String[] infos = info.split(",");
						if(!(infos[5].trim().equals("null") || infos[5].trim().equals(""))){
							adminCode = Integer.parseInt(infos[5]);
						}
					}		
					int ii=1;
					String primkey = sdf.format(fine.getForecastTime()) + "_"+ sdf.format(fine.getObservationTime()) + "_" + fine.getStationNumberChina()+"_"+fine.getProd_Code();
				    pStatement.setString(ii++, primkey);//记录标识
					pStatement.setString(ii++,sod_code);//资料标识
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//入库时间
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));//收到时间
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间
//					pStatement.setTimestamp(ii++, new Timestamp(fine.getObservationTime().getTime()));//资料时间
					//change to 2019-2-26
					pStatement.setTimestamp(ii++, new Timestamp(fine.getForecastTime().getTime()));//资料时间
					pStatement.setString(ii++, fine.getCorrectSign());//更正标识
					pStatement.setString(ii++, fine.getStationNumberChina());//区站号(字符)
					pStatement.setInt(ii++,Integer.parseInt(StationCodeUtil.stringToAscii(fine.getStationNumberChina())) );//区站号(数字)
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine.getLatitude())));//纬度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine.getLongitude())));//经度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine.getHeightOfSationGroundAboveMeanSeaLevel())));//测站海拔高度
					pStatement.setInt(ii++, adminCode);//中国行政区代码
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine.getForecastTime().getYear() + 1900)));//预报年
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine.getForecastTime().getMonth()+1)));//预报月
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine.getForecastTime().getDate())));//预报日
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine.getForecastTime().getHours())));//预报时
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine.getObservationTime().getYear() + 1900)));//预报产生年
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine.getObservationTime().getMonth()+1)));//预报产生月
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine.getObservationTime().getDate())));//预报产生日
					pStatement.setInt(ii++, fine.getObservationTime().getHours());//预报产生时
					pStatement.setInt(ii++, fine.getObservationTime().getMinutes());//预报产生分
					pStatement.setString(ii++, fine.getBul_Center());//编报(加工)中心
					pStatement.setString(ii++, fine.getPROD_DESC());//产品描述
					pStatement.setString(ii++, fine.getProd_Code());//产品代码
					pStatement.setString(ii++, forecast_max);//最大预报时效
					pStatement.setString(ii++, interval_max);//最大时效间隔
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine.getValidtime_Count())));//预报时效个数
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine.getV_PROD_NUM())));//预报产品个数
					pStatement.setString(ii++, cts_code);
					//System.out.println(((LoggableStatement)pStatement).getQueryString());
					
					di.setIIiii(fine.getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(fine.getObservationTime(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLATITUDE(String.valueOf(fine.getLatitude()));
					di.setLONGTITUDE(String.valueOf(fine.getLongitude()));
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(fileN).length()));
					di.setDATA_UPDATE_FLAG(fine.getCorrectSign());
					di.setHEIGHT(String.valueOf(fine.getHeightOfSationGroundAboveMeanSeaLevel()));
					
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
			loggerBuffer.append("\n Database connection error: "+e.getMessage());
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
	 * @Description:(精细化预报入键表（单条）)
	 * @param fine_forecast 入库对象集合
	 * @param connection 数据库连接
	 * @param recv_time 接收时间
	 * @param forecast_max 最大预报时效
	 * @param interval_max 最大时效间隔
	 * @param loggerBuffer 
	 * @param fileN 
	 * @return DataBaseAction 数据入库状态
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static DataBaseAction insertOneLine_key_db(FineForecast fine_forecast, java.sql.Connection connection,
			Date recv_time, String forecast_max, String interval_max, StringBuffer loggerBuffer,String cts_code, String fileN) {
		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.keyTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V_BBB,V01301,V01300,V05001,"
				+ "V06001,V07001,V_ACODE,V04001_02,V04002_02,V04003_02,V04004_02,V04001,V04002,V04003,V04004,V04005,V_CCCC,"
				+ "V_PROD_DESC,V_PROD_CODE,V04320_005,V04320_005_080,V04320_041,V_PROD_NUM,D_SOURCE_ID) VALUES("
				+"?, ?, ?, ?, ?, ?, ?, ?, ?,?,"
				+"?, ?, ?, ?, ?, ?, ?, ?, ?,?,"
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
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
				String info = (String) proMap.get(fine_forecast.getStationNumberChina() + "+01");			
				if(info == null) {
					loggerBuffer.append("\n In configuration file, this station does not exist: " + fine_forecast.getStationNumberChina());
				}else{
					String[] infos = info.split(",");
					if(!(infos[5].trim().equals("null") || infos[5].trim().equals(""))){
						adminCode = Integer.parseInt(infos[5]);
					}
				}		
			    int ii=1;
			    String primkey = sdf.format(fine_forecast.getForecastTime())+ "_" + sdf.format(fine_forecast.getObservationTime())+"_"+fine_forecast.getStationNumberChina()+"_"+fine_forecast.getProd_Code();
			    pStatement.setString(ii++, primkey);//记录标识
				pStatement.setString(ii++,sod_code);//资料标识
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//入库时间
				pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));//收到时间
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间
//				pStatement.setTimestamp(ii++, new Timestamp(fine_forecast.getObservationTime().getTime()));//资料时间	
				//change to 2019-2-26
				pStatement.setTimestamp(ii++, new Timestamp(fine_forecast.getForecastTime().getTime()));//资料时间
				
				pStatement.setString(ii++, fine_forecast.getCorrectSign());//更正标识
				pStatement.setString(ii++, fine_forecast.getStationNumberChina());//区站号(字符)
				pStatement.setInt(ii++,Integer.parseInt(StationCodeUtil.stringToAscii(fine_forecast.getStationNumberChina())) );//区站号(数字)
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getLatitude())));//纬度
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getLongitude())));//经度
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getHeightOfSationGroundAboveMeanSeaLevel())));//测站海拔高度
				pStatement.setInt(ii++, adminCode);//中国行政区代码
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getForecastTime().getYear() + 1900)));//预报年
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getForecastTime().getMonth()+1)));//预报月
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getForecastTime().getDate())));//预报日
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getForecastTime().getHours())));//预报时
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getObservationTime().getYear() + 1900)));//预报产生年
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getObservationTime().getMonth()+1)));//预报产生月
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getObservationTime().getDate())));//预报产生日
				pStatement.setInt(ii++, fine_forecast.getObservationTime().getHours());//预报产生时
				pStatement.setInt(ii++, fine_forecast.getObservationTime().getMinutes());//预报产生分
				pStatement.setString(ii++, fine_forecast.getBul_Center());//编报(加工)中心
				pStatement.setString(ii++, fine_forecast.getPROD_DESC());//产品描述
				pStatement.setString(ii++, fine_forecast.getProd_Code());//产品代码
				pStatement.setString(ii++, forecast_max);//最大预报时效
				pStatement.setString(ii++, interval_max);//最大时效间隔
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getValidtime_Count())));//预报时效个数
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getV_PROD_NUM())));//预报产品个数
				pStatement.setString(ii++, cts_code);
				
				di.setIIiii(fine_forecast.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(fine_forecast.getObservationTime(), "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				di.setLATITUDE(String.valueOf(fine_forecast.getLatitude()));
				di.setLONGTITUDE(String.valueOf(fine_forecast.getLongitude()));
				
				di.setSEND("BFDB");
				di.setSEND_PHYS("DRDS");
				di.setFILE_SIZE(String.valueOf(new File(fileN).length()));
				di.setDATA_UPDATE_FLAG(fine_forecast.getCorrectSign());
				di.setHEIGHT(String.valueOf(fine_forecast.getHeightOfSationGroundAboveMeanSeaLevel()));
				
				try {
					pStatement.execute();
					//connection.commit();
					listDi.add(di);
					return DataBaseAction.SUCCESS;
					
				} catch (SQLException e) {
					di.setPROCESS_STATE("0");//1成功，0失败
					listDi.add(di);	
					loggerBuffer.append("\n File name: "+fileN+
							"\n " + fine_forecast.getStationNumberChina() + " " + sdf.format(fine_forecast.getObservationTime())
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
 * @param recv_time 接收时间
 * @param loggerBuffer 
 * @param fileN 
 * @return DataBaseAction 数据入库状态
 * @throws：
 */
	private static  DataBaseAction insertEleDB(List<FineForecast> list_normal, java.sql.Connection connection, Date recv_time, StringBuffer loggerBuffer, String fileN,String cts_code) {
		PreparedStatement pStatement = null;
		try {
			// 获取数据库连接
			// sql 语句
			String sql = "INSERT INTO "+StartConfig.valueTable()+" (D_ELE_ID,D_RECORD_ID,D_UPDATE_TIME,D_DATETIME,V01301,V01300,V04320,V12001,"
					+ "V13003,V11001,V11002,V10004,V13016,V20010,V20051,V20312,V20001,V12016,V12017,V13008_24,V13007_24,"
					+ "V13023,V13022,V20010_12,V20051_12,V20312_12,V11001_12,V11040_12,V_BBB,D_SOURCE_ID) VALUES ("
					+ "?,?, ?, ?, ?, ?, ?,?, ?, ?,"
					+ "?,?, ?, ?, ?, ?, ?,?, ?, ?,"
					+ "?,?, ?, ?, ?, ?, ?,?,?,?)";
			if(connection !=null){
				connection.setAutoCommit(false);
				pStatement = connection.prepareStatement(sql); 
				//if(StartConfig.getDatabaseType() == 1) {
				//	pStatement.execute("select last_txc_xid()");
				//}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				for(int i =0; i<list_normal.size();i++){
					FineForecast fine_list = list_normal.get(i);
					// 获取要素行数
					for (int j = 0; j < fine_list.getFine().size(); j++) {
						FineList fine_forecast = fine_list.getFine().get(j);
						int ii=1;
						String primkey = sdf.format(fine_list.getForecastTime()) + "_" + sdf.format(fine_list.getObservationTime())+"_"+fine_list.getStationNumberChina()+"_"+fine_list.getProd_Code();
						pStatement.setString(ii++, primkey+"_"+fine_forecast.getValidtime());//要素标识
						pStatement.setString(ii++, primkey);//记录标识
						pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间
//						pStatement.setTimestamp(ii++, new Timestamp(fine_list.getObservationTime().getTime()));//资料时间
						//change to 2019-2-26
						pStatement.setTimestamp(ii++, new Timestamp(fine_list.getForecastTime().getTime()));//资料时间
						
						pStatement.setString(ii++, fine_list.getStationNumberChina());//站号
						pStatement.setInt(ii++,Integer.parseInt(StationCodeUtil.stringToAscii(fine_list.getStationNumberChina())) );//区站号(数字)
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getValidtime())));//预报时效
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getTEM())));//温度/气温
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getRHU())));//相对湿度
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getWIN_D())));//风向
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getWIN_S())));//风速
						if(fine_forecast.getPRS().doubleValue() == 999999.0 || fine_forecast.getPRS().doubleValue() == 999998.0)
							pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getPRS().doubleValue())));
						else
							pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getPRS() + 1000)));//气压
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getPRE_PRE_Fore())));//可降水分（预报降水量）
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getCLO_Cov())));//总云量
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getCLO_Cov_Low())));//低云量
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getWEP())));//天气现象
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getVIS())));//水平能见度(人工)
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getTEM_Max_24h())));//未来24小时最高气温
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getTEM_Min_24h())));//未来24小时最低气温
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getRHU_Max_24h())));//24小时最大相对湿度
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getRHU_Min_24h())));//24小时最小相对湿度
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getPRE_24h())));//未来24小时降水量
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getPRE_12h())));//未来12小时降水量
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getCLO_Cov_12h())));//12小时内总云量
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getCLO_Cov_Low_12h())));//12小时内低云量
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getWEP_Past_12h())));//12小时内天气现象
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getWIN_PD_12h())));//12小时内盛行风向
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getWIN_S_Max_12h())));//12小时内最大风速
						pStatement.setString(ii++, fine_list.getCorrectSign());//更正标志
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
				loggerBuffer.append("\n Database connection error!");
				return DataBaseAction.CONNECTION_ERROR;
			}
		} catch (Exception e) {
			loggerBuffer.append("\n Create statement error: " + e.getMessage());
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
	 * @Description:(精细化预报入要素表（单条）)
	 * @param fine_list 入库对象集合
	 * @param connection 数据库连接
	 * @param recv_time void  接收时间
	 * @param loggerBuffer 
	 * @param fileN 
	 * @throws：
	 */
	private static void insertOneLine_Ele_db(FineForecast fine_list , java.sql.Connection connection, Date recv_time, StringBuffer loggerBuffer, String fileN,String cts_code) {
		PreparedStatement pStatement = null;
		// sql 语句
		String sql = "INSERT INTO "+StartConfig.valueTable()+" (D_ELE_ID,D_RECORD_ID,D_UPDATE_TIME,D_DATETIME,V01301,V01300,V04320,V12001,"
					  + "V13003,V11001,V11002,V10004,V13016,V20010,V20051,V20312,V20001,V12016,V12017,V13008_24,V13007_24,"
					  + "V13023,V13022,V20010_12,V20051_12,V20312_12,V11001_12,V11040_12,V_BBB,D_SOURCE_ID) VALUES ("
					  + "?,?, ?, ?, ?, ?, ?,?, ?, ?,"
					  + "?,?, ?, ?, ?, ?, ?,?, ?, ?,"
					  + "?,?, ?, ?, ?, ?, ?,?, ?, ?)";
		if(connection !=null){
			try {
				connection.setAutoCommit(false);
//				pStatement = connection.prepareStatement(sql);
				pStatement = new LoggableStatement(connection, sql);
				//if(StartConfig.getDatabaseType() == 1) {
				//	pStatement.execute("select last_txc_xid()");
				//}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				for (int i = 0; i < fine_list.getFine().size(); i++) {
					FineList  fine_forecast= fine_list.getFine().get(i);
					int ii=1;
					String primkey = sdf.format(fine_list.getForecastTime()) + "_" + sdf.format(fine_list.getObservationTime())+"_"+fine_list.getStationNumberChina()+"_"+fine_list.getProd_Code();
					pStatement.setString(ii++, primkey+"_"+fine_forecast.getValidtime());//要素标识
					pStatement.setString(ii++, primkey);//记录标识
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间
//					pStatement.setTimestamp(ii++, new Timestamp(fine_list.getObservationTime().getTime()));//资料时间
					//change to 2019-2-26
					pStatement.setTimestamp(ii++, new Timestamp(fine_list.getForecastTime().getTime()));//资料时间
					
					pStatement.setString(ii++, fine_list.getStationNumberChina());//站号
					
					pStatement.setInt(ii++,Integer.parseInt(StationCodeUtil.stringToAscii(fine_list.getStationNumberChina())) );//区站号(数字)
					
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getValidtime())));//预报时效
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getTEM())));//温度/气温
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getRHU())));//相对湿度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getWIN_D())));//风向
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getWIN_S())));//风速
					if(fine_forecast.getPRS().doubleValue() == 999999.0 || fine_forecast.getPRS().doubleValue() == 999998.0)
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getPRS().doubleValue())));
					else
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getPRS() + 1000)));//气压
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getPRE_PRE_Fore())));//可降水分（预报降水量）
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getCLO_Cov())));//总云量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getCLO_Cov_Low())));//低云量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getWEP())));//天气现象
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getVIS())));//水平能见度(人工)
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getTEM_Max_24h())));//未来24小时最高气温
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getTEM_Min_24h())));//未来24小时最低气温
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getRHU_Max_24h())));//24小时最大相对湿度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getRHU_Min_24h())));//24小时最小相对湿度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getPRE_24h())));//未来24小时降水量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getPRE_12h())));//未来12小时降水量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getCLO_Cov_12h())));//12小时内总云量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getCLO_Cov_Low_12h())));//12小时内低云量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getWEP_Past_12h())));//12小时内天气现象
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getWIN_PD_12h())));//12小时内盛行风向
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getWIN_S_Max_12h())));//12小时内最大风速
					pStatement.setString(ii++, fine_list.getCorrectSign());//更正标志	
					pStatement.setString(ii++, cts_code);
					
//					System.out.println(((LoggableStatement)pStatement).getQueryString());
				
					pStatement.addBatch();
				}
				try {
					pStatement.executeBatch();
					connection.commit();
				} catch (Exception e) {
					connection.rollback();
					loggerBuffer.append("\n File name: "+fileN+
							"\n " + fine_list.getStationNumberChina() + " " + sdf.format(fine_list.getObservationTime())
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
	 * @param loggerBuffer 
	 * @param cts_code 
	 * @param fileN 
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static void updatekeyDB(List<FineForecast> list_correct, java.sql.Connection connection, Date recv_time, String forecast_max, String interval_max, StringBuffer loggerBuffer, String cts_code, String fileN) {
		PreparedStatement Pstat = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		//list
		//List<FineForecast> listTable = new ArrayList<FineForecast>();//加入KEY表的数据行主键的集合
		for(int i=0;i<list_correct.size();i++){
			String vbbbInDB = null;
			//插入前，从DB中查找该条记录的状态，有、无、更正状态
			vbbbInDB = findVBBB_key(list_correct.get(i),connection,loggerBuffer);
			if(vbbbInDB == null){//该更正报之前数据库中没有该条记录，直接插入,给UPDATE_TIME赋值
				DataBaseAction  updatekey =insertOneLine_key_db(list_correct.get(i), connection, recv_time,forecast_max,interval_max,loggerBuffer,cts_code,fileN);	
				if (updatekey==DataBaseAction.SUCCESS) {
					insertOneLine_Ele_db(list_correct.get(i), connection, recv_time,loggerBuffer,fileN,cts_code);				
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
				String sql ="UPDATE "+StartConfig.keyTable()+" SET"
						+ " V_BBB=?,D_UPDATE_TIME=?,V_CCCC=?,D_RYMDHM=?,V05001=?,V06001=?,V07001=?,V_PROD_DESC=?,V04320_041=?,V_PROD_NUM=? "
						+ " WHERE V01301=? "
						+ " AND D_datetime=? AND V04001=? AND V04002=? AND V04003=? AND V04004=? AND V04005=?  AND V_PROD_CODE=? ";
				if(connection != null){
					try {
						
						connection.setAutoCommit(false);
						Pstat = new LoggableStatement(connection, sql);
						if(StartConfig.getDatabaseType() == 1) {
							Pstat.execute("select last_txc_xid()");
						}
						int ii = 1;
//						int strYear = list_correct.get(i).getForecastTime().getYear() + 1900;
//						int strMonth = list_correct.get(i).getForecastTime().getMonth() + 1;
//						int strDate = list_correct.get(i).getForecastTime().getDate();
						int obsYear = list_correct.get(i).getObservationTime().getYear() + 1900;
						int obsMonth = list_correct.get(i).getObservationTime().getMonth() + 1;
						int obsDate = list_correct.get(i).getObservationTime().getDate();
						
						Pstat.setString(ii++, list_correct.get(i).getCorrectSign());//更正标识
						Pstat.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间			
						Pstat.setString(ii++, list_correct.get(i).getBul_Center());//V_CCCC编报(加工)中心
						
						File f=new File(fileN);
						Date recv_time2 = new Date(f.lastModified());
						Pstat.setTimestamp(ii++, new Timestamp(recv_time2.getTime()));//收到时间
						Pstat.setBigDecimal(ii++, new BigDecimal(String.valueOf(list_correct.get(i).getLatitude())));//纬度
						Pstat.setBigDecimal(ii++, new BigDecimal(String.valueOf(list_correct.get(i).getLongitude())));//经度
						Pstat.setBigDecimal(ii++, new BigDecimal(String.valueOf(list_correct.get(i).getHeightOfSationGroundAboveMeanSeaLevel())));//测站海拔高度
						Pstat.setString(ii++, list_correct.get(i).getPROD_DESC());//产品描述
						Pstat.setBigDecimal(ii++, new BigDecimal(String.valueOf(list_correct.get(i).getValidtime_Count())));//预报时效个数
						Pstat.setBigDecimal(ii++, new BigDecimal(String.valueOf(list_correct.get(i).getV_PROD_NUM())));//预报产品个数
						
						Pstat.setString(ii++,list_correct.get(i).getStationNumberChina());//站号
//						Pstat.setInt(ii++, strYear);//资料观测年
//						Pstat.setInt(ii++, strMonth);//资料观测月
//						Pstat.setInt(ii++, strDate);//资料观测日
//						Pstat.setInt(ii++, list_correct.get(i).getForecastTime().getHours());//资料观测时
						Pstat.setTimestamp(ii++, new Timestamp(list_correct.get(i).getForecastTime().getTime()));
						// add
						Pstat.setInt(ii++, obsYear);
						Pstat.setInt(ii++, obsMonth);
						Pstat.setInt(ii++, obsDate);
						Pstat.setInt(ii++, list_correct.get(i).getObservationTime().getHours());
						Pstat.setInt(ii++, list_correct.get(i).getObservationTime().getMinutes());
						
						Pstat.setString(ii++, list_correct.get(i).getProd_Code());//产品描述
						di.setIIiii(list_correct.get(i).getStationNumberChina());
						di.setDATA_TIME(TimeUtil.date2String(list_correct.get(i).getObservationTime(), "yyyy-MM-dd HH:mm"));
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
							updateEle(list_correct.get(i),recv_time,connection,loggerBuffer,cts_code,fileN);
							//listTable.add(list_correct.get(i));
						}
						catch(SQLException e){							
							di.setPROCESS_STATE("0");//1成功，0失败
							listDi.add(di);		
							loggerBuffer.append("\n File name: "+fileN+
									"\n " + list_correct.get(i).getStationNumberChina() + " " + sdf.format(list_correct.get(i).getObservationTime())
									+"\n execute sql error: "+((LoggableStatement)Pstat).getQueryString()+"\n "+e.getMessage());
						}				
					}	catch (SQLException e) {
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
	}
	/**
	 * 
	 * @Title: updateEle
	 * @Description:(更新要素表)
	 * @param fine 更新对象集合
	 * @param recv_time 接收时间
	 * @param connection 数据库连接
	 * @param loggerBuffer 
	 * @param cts_code 
	 * @param fileN 
	 * @throws：
	 */
	//@SuppressWarnings("unused")
	private static void updateEle(FineForecast fine,Date  recv_time ,java.sql.Connection connection, StringBuffer loggerBuffer, String cts_code, String fileN) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");				 
        PreparedStatement pStatement = null;
		String sql ="UPDATE "+StartConfig.valueTable()+" SET"
				+ " V_BBB=?, D_UPDATE_TIME=?, V04320=?, V12001=?, V13003=?, V11001=?, V11002=?, V10004=?, V13016=?, "
				+ " V20010=?, V20051=?, V20312=?, V20001=?, V12016=?, V12017=?, V13008_24=?, V13007_24=?,"
				+ " V13023=?, V13022=?, V20010_12=?, V20051_12=?, V20312_12=?, V11001_12=?, V11040_12=?"
				+ "	WHERE D_RECORD_ID=? AND V04320=? and D_DATETIME=?";
		
		if(connection != null){
			try{
				connection.setAutoCommit(false);
				pStatement = new LoggableStatement(connection, sql);
				int list_size = fine.getFine().size();
				for(int i = 0; i < list_size; i ++){
					int ii = 1;
					FineList fine_forecast =fine.getFine().get(i); 
					pStatement.setString(ii++, fine.getCorrectSign());//更正标识
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间			
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getValidtime())));//预报时效
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getTEM())));//温度/气温
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getRHU())));//相对湿度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getWIN_D())));//风向
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getWIN_S())));//风速
					if(fine_forecast.getPRS().doubleValue() == 999999.0 || fine_forecast.getPRS().doubleValue() == 999998.0)
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getPRS())));//气压
					else
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getPRS() + 1000)));//气压
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getPRE_PRE_Fore())));//可降水分（预报降水量）
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getCLO_Cov())));//总云量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getCLO_Cov_Low())));//低云量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getWEP())));//天气现象
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getVIS())));//水平能见度(人工)
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getTEM_Max_24h())));//未来24小时最高气温
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getTEM_Min_24h())));//未来24小时最低气温
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getRHU_Max_24h())));//24小时最大相对湿度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getRHU_Min_24h())));//24小时最小相对湿度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getPRE_24h())));//未来24小时降水量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getPRE_12h())));//未来12小时降水量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getCLO_Cov_12h())));//12小时内总云量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getCLO_Cov_Low_12h())));//12小时内低云量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getWEP_Past_12h())));//12小时内天气现象
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getWIN_PD_12h())));//12小时内盛行风向
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getWIN_S_Max_12h())));//12小时内最大风速
					String primary_key = sdf.format(fine.getForecastTime()) + "_" + sdf.format(fine.getObservationTime())+"_"+fine.getStationNumberChina()+"_"+fine.getProd_Code();
					pStatement.setString(ii++,primary_key);//记录标识
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(fine_forecast.getValidtime())));//预报时效
					
					pStatement.setTimestamp(ii++, new Timestamp(fine.getForecastTime().getTime()));
					
					pStatement.addBatch();	
				}try{				
					pStatement.executeBatch();
					connection.commit();
				}
				catch(SQLException e){	
					connection.rollback();
					loggerBuffer.append("\n File name: "+fileN+
							"\n " + fine.getStationNumberChina() + " " + sdf.format(fine.getObservationTime())
							+"\n "+e.getMessage());}		
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

	private static String findVBBB_key(FineForecast fine_forecast, java.sql.Connection connection, StringBuffer loggerBuffer) {
		PreparedStatement Pstmt = null;
		ResultSet resultSet = null;
		String rntString = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String sql ="SELECT V_BBB FROM "+StartConfig.keyTable()+" "			
				+ " WHERE D_RECORD_ID=? and V01301=? ";
		try{	
			
			if(connection != null){
				Pstmt = connection.prepareStatement(sql);
				if(StartConfig.getDatabaseType() == 1) {
					Pstmt.execute("select last_txc_xid()");
				}
				int ii = 1;		
				Pstmt.setString(ii++, sdf.format(fine_forecast.getForecastTime())+ "_" + sdf.format(fine_forecast.getObservationTime())+"_"+fine_forecast.getStationNumberChina()+"_"+fine_forecast.getProd_Code());
				Pstmt.setString(ii++, fine_forecast.getStationNumberChina());//站号
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
				}catch (Exception e) {
					loggerBuffer.append("close resultset error!");
				}
			}
		}
		return rntString;	
	}
	
}
