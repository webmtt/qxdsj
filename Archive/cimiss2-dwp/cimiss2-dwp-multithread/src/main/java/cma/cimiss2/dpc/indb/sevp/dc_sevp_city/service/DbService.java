package cma.cimiss2.dpc.indb.sevp.dc_sevp_city.service;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
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
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP.Connection.Start;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.sevp.CityForeList;
import cma.cimiss2.dpc.decoder.bean.sevp.CityForecastLast;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.sevp.ReportInfoService;

public class DbService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static List<StatDi> listDi = new ArrayList<StatDi>();;
	public static BlockingQueue<StatDi> diQueues;
	public static String sod_code = StartConfig.sodCode();
	public static String cts_code = StartConfig.ctsCode();
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
	 * @Description:(报文解码入库函数)
	 * @param parseResult 存放解码结果
	 * @param filepath 文件路径
	 * @param recv_time 接收时间
	 * @param fileN 
	 * @param forecast_max  最大预报时效
	 * @param interval_max  最大时效间隔
	 * @return DataBaseAction 数据入库状态
	 * @throws：
	 */
	public static DataBaseAction processSuccessReport(ParseResult<CityForecastLast> parseResult, Date recv_time, String fileN) {
		java.sql.Connection connection = null;
		java.sql.Connection reportConnection = null;
		try {
			// 获取数据库连接
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			// 获取解码结果集
			List<CityForecastLast> list = parseResult.getData();
			String  correctsign = null;
			List<CityForecastLast> list_normal = new ArrayList<>();//非更正报文集合
			List<CityForecastLast> list_correct = new ArrayList<>();//更正报文集合
			for(int j = 0;j < list.size(); j++){
				correctsign = list.get(j).getCorrectSign();//用更正报标志判断是否为更正报文
				if(correctsign.equals("000")){
					list_normal.add(list.get(j));
				}else{
					list_correct.add(list.get(j));
				}
			}
			// 插入报文
			if(list_normal.size() > 0){
				
				for (int i = 0; i < list_normal.size(); i++) { 
					DataBaseAction insertOneLine_key_db = insertOneLine_key_db(list_normal.get(i), connection, recv_time, fileN);//单条插入键表
//					if(insertOneLine_key_db == DataBaseAction.SUCCESS) {//单条插入键表成功
					insertOneLine_Ele_db(list_normal.get(i), connection, recv_time, fileN);//单条插入要素表
//					}
				}
				
//				DataBaseAction insertKeyDB = insertKeyDB(list_normal, connection, recv_time,fileN);//批量插入键表	
//				if(insertKeyDB == DataBaseAction.SUCCESS) { // 批量插入键表成功
//					DataBaseAction insertEleDB = insertEleDB(list_normal, connection, recv_time, fileN);//插入要素表
//					if (insertEleDB == DataBaseAction.BATCH_ERROR) {//批量插入要素表失败
//						
//					}  
//				}
//				else if(insertKeyDB == DataBaseAction.BATCH_ERROR){
//					for (int i = 0; i < list_normal.size(); i++) { 
//						DataBaseAction insertOneLine_key_db = insertOneLine_key_db(list_normal.get(i), connection, recv_time,fileN);//单条插入键表
//						if(insertOneLine_key_db == DataBaseAction.SUCCESS) {//单条插入键表成功
//							insertOneLine_Ele_db(list_normal.get(i), connection, recv_time, fileN);//单条插入要素表
//						}
//					}
//				}
//				else {
//	  				return insertKeyDB;
//				}
			}
			//更正报文
			if(list_correct.size() > 0){
				updatekeyDB(list_correct, connection,recv_time,fileN);
			}
			@SuppressWarnings("rawtypes")
			List<ReportInfo> reportInfos = parseResult.getReports();
		    reportConnection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
		    ReportInfoService.reportInfoToDb(reportInfos, reportConnection, recv_time, StartConfig.reportSodCode());
			return DataBaseAction.SUCCESS;
		}catch (Exception e) {
			infoLogger.error("\n Database connection error!");
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
					infoLogger.error("\n Close databasse connection error: "+e.getMessage());
				}
			}
			if(reportConnection != null) {
				try {
					reportConnection.close();
				} catch (SQLException e) {
					infoLogger.error("\n Close databasse connection error: "+e.getMessage());
				}
			}
		}
	}

	/**
	 * 
	 * @Title: insertKeyDB
	 * @Description:(城镇天气预报最终报告键表（批量）)
	 * @param list_normal 入库对象集合
	 * @param connection 数据库连接
	 * @param recv_time 接收时间
	 * @param fileN 
	 * @param forecast_max 最大预报时效
	 * @param interval_max 最大时效间隔
	 * @return DataBaseAction 数据入库状态
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static DataBaseAction insertKeyDB(List<CityForecastLast> list_normal, java.sql.Connection connection,Date recv_time, String fileN) {
		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement pStatement = null;
		try {
			String sql = "INSERT INTO " + StartConfig.keyTable() +"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V_BBB,V01301,V01300,V05001,"
					+ "V06001,V07001,V_ACODE,V04001_02,V04002_02,V04003_02,V04004_02,V04001,V04002,V04003,V04004,V04005,V_CCCC,"
					+ "V_PROD_DESC,V_PROD_CODE,V04320_005,V04320_005_080,V04320_041,V_PROD_NUM,D_SOURCE_ID) VALUES("
					+"?, ?, ?, ?, ?, ?, ?, ?, ?,?,"
					+"?, ?, ?, ?, ?, ?, ?, ?, ?,?,"
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			if(connection != null){
				// 创建PreparedStatement
				connection.setAutoCommit(false);
				pStatement = connection.prepareStatement(sql); 
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				for(int i=0;i<list_normal.size();i++){
					CityForecastLast last = list_normal.get(i);
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
					String info = (String) proMap.get(last.getStationNumberChina() + "+01");
					int adminCode = 999999;
					if(info == null) {
						infoLogger.error("\n In configuration file, this station does not exist: " + last.getStationNumberChina());			
					}else{			
						String[] infos = info.split(",");
						if(!(infos[5].trim().equals("null") || infos[5].trim().equals(""))){
							adminCode = Integer.parseInt(infos[5]);
						}
					}		
					int ii = 1;
					String primkey = sdf.format(last.getObservationTime()) +"_"+sdf.format(last.getForecastTime())+"_"+last.getStationNumberChina()+"_"+last.getProd_Code();
				    pStatement.setString(ii++, primkey);//记录标识
					pStatement.setString(ii++,sod_code);//资料标识
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//入库时间
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));//收到时间
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间
					pStatement.setTimestamp(ii++, new Timestamp(last.getObservationTime().getTime()));//资料时间
					pStatement.setString(ii++, last.getCorrectSign());//更正标识
					pStatement.setString(ii++, last.getStationNumberChina());//区站号(字符)
					pStatement.setInt(ii++,Integer.parseInt(StationCodeUtil.stringToAscii(last.getStationNumberChina())) );//区站号(数字)
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(last.getLatitude())));//纬度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(last.getLongitude())));//经度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(last.getHeightOfSationGroundAboveMeanSeaLevel())));//测站海拔高度
					pStatement.setInt(ii++, adminCode);//中国行政区代码
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(last.getObservationTime().getYear() + 1900)));//预报年
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(last.getObservationTime().getMonth()+1)));//预报月
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(last.getObservationTime().getDate())));//预报日
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(last.getObservationTime().getHours())));//预报时
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(last.getForecastTime().getYear() + 1900)));//预报产生年
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(last.getForecastTime().getMonth()+1)));//预报产生月
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(last.getForecastTime().getDate())));//预报产生日
					pStatement.setInt(ii++, last.getForecastTime().getHours());//预报产生时
					pStatement.setInt(ii++, last.getForecastTime().getMinutes());//预报产生分
					pStatement.setString(ii++, last.getBul_Center());//编报(加工)中心
					pStatement.setString(ii++, last.getPROD_DESC());//产品描述
					pStatement.setString(ii++, last.getProd_Code());//产品代码
					pStatement.setInt(ii++, last.getValidtime_Max());//最大预报时效
					pStatement.setInt(ii++, last.getValidtime_Max_Intr());//最大时效间隔
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(last.getValidtime_Count())));//预报时效个数
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(last.getV_PROD_NUM())));//预报产品个数
					pStatement.setString(ii++, cts_code);
					
					di.setIIiii(last.getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(last.getForecastTime(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLATITUDE(String.valueOf(last.getLatitude()));
					di.setLONGTITUDE(String.valueOf(last.getLongitude()));
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(fileN).length()));
					di.setDATA_UPDATE_FLAG(last.getCorrectSign());
					di.setHEIGHT(String.valueOf(last.getHeightOfSationGroundAboveMeanSeaLevel()));
					
					// 批量提交
					pStatement.addBatch();		
					listDi.add(di);	
				}			
				try {
					// 执行批量			
					pStatement.executeBatch();
					connection.commit();
					infoLogger.info(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
					infoLogger.info(" INSERT SUCCESS COUNT ：" + list_normal.size() + "\n");
					return DataBaseAction.SUCCESS;
				} catch (Exception e) {
					pStatement.clearParameters();
					pStatement.clearBatch();			
					infoLogger.error("\n Batch commit error: "+fileN);
					return DataBaseAction.BATCH_ERROR;
				}			
			} else {
				infoLogger.error("\n Database connection error!");
				return DataBaseAction.CONNECTION_ERROR;
			}														
		} catch (SQLException e) {		
			infoLogger.error("\n Database connection error: "+e.getMessage());
			return DataBaseAction.CONNECTION_ERROR;
		}finally {
			try {
				if(pStatement != null)
					pStatement.close();
			} catch (SQLException e) {
				infoLogger.error("\n Close statement error: "+e.getMessage());
			}			
		}
	}
	/**
	 * 
	 * @Title: insertOneLine_key_db
	 * @Description:(全国城镇天气预报最终报告键表（单条）)
	 * @param fine_forecast 入库对象集合
	 * @param connection 数据库连接
	 * @param recv_time 接收时间
	 * @param fileN 
	 * @param forecast_max 最大预报时效
	 * @param interval_max 最大时效间隔
	 * @return DataBaseAction 数据入库状态
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static DataBaseAction insertOneLine_key_db(CityForecastLast last, java.sql.Connection connection,
			Date recv_time, String fileN) {
		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+ StartConfig.keyTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V_BBB,V01301,V01300,V05001,"
				+ "V06001,V07001,V_ACODE,V04001_02,V04002_02,V04003_02,V04004_02,V04001,V04002,V04003,V04004,V04005,V_CCCC,"
				+ "V_PROD_DESC,V_PROD_CODE,V04320_005,V04320_005_080,V04320_041,V_PROD_NUM, D_SOURCE_ID) VALUES("
				+"?, ?, ?, ?, ?, ?, ?, ?, ?,?,"
				+"?, ?, ?, ?, ?, ?, ?, ?, ?,?,"
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		if(connection !=null){
			try {
				connection.setAutoCommit(true);
				pStatement = new LoggableStatement(connection, sql); 
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
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
				String info = (String) proMap.get(last.getStationNumberChina() + "+01");			
				if(info == null) {
					infoLogger.error("\n In configuration file, this station does not exist: " + last.getStationNumberChina());
				}else{
					String[] infos = info.split(",");
					if(!(infos[5].trim().equals("null") || infos[5].trim().equals(""))){
						adminCode = Integer.parseInt(infos[5]);
					}
				}
			    int ii = 1;
			    String primkey = sdf.format(last.getObservationTime()) +"_"+sdf.format(last.getForecastTime())+"_"+last.getStationNumberChina()+"_"+last.getProd_Code();
			    pStatement.setString(ii++, primkey);//记录标识
				pStatement.setString(ii++,sod_code);//资料标识
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//入库时间
				pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));//收到时间
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间
				pStatement.setTimestamp(ii++, new Timestamp(last.getObservationTime().getTime()));//资料时间	
				pStatement.setString(ii++, last.getCorrectSign());//更正标识
				pStatement.setString(ii++, last.getStationNumberChina());//区站号(字符)
				pStatement.setInt(ii++,Integer.parseInt(StationCodeUtil.stringToAscii(last.getStationNumberChina())) );//区站号(数字)
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(last.getLatitude())));//纬度
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(last.getLongitude())));//经度
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(last.getHeightOfSationGroundAboveMeanSeaLevel())));//测站海拔高度
				pStatement.setInt(ii++, adminCode);//中国行政区代码
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(last.getObservationTime().getYear() + 1900)));//预报年
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(last.getObservationTime().getMonth()+1)));//预报月
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(last.getObservationTime().getDate())));//预报日
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(last.getObservationTime().getHours())));//预报时
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(last.getForecastTime().getYear() + 1900)));//预报产生年
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(last.getForecastTime().getMonth()+1)));//预报产生月
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(last.getForecastTime().getDate())));//预报产生日
				pStatement.setInt(ii++, last.getForecastTime().getHours());//预报产生时
				pStatement.setInt(ii++, last.getForecastTime().getMinutes());//预报产生分
				pStatement.setString(ii++, last.getBul_Center());//编报(加工)中心
				pStatement.setString(ii++, last.getPROD_DESC());//产品描述
				pStatement.setString(ii++, last.getProd_Code());//产品代码
				pStatement.setInt(ii++, last.getValidtime_Max());//最大预报时效
				pStatement.setInt(ii++, last.getValidtime_Max_Intr());//最大时效间隔
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(last.getValidtime_Count())));//预报时效个数
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(last.getV_PROD_NUM())) );//预报产品个数
				pStatement.setString(ii++, cts_code);
				
				di.setIIiii(last.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(last.getForecastTime(), "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				di.setLATITUDE(String.valueOf(last.getLatitude()));
				di.setLONGTITUDE(String.valueOf(last.getLongitude()));
				
				di.setSEND("BFDB");
				di.setSEND_PHYS("DRDS");
				di.setFILE_SIZE(String.valueOf(new File(fileN).length()));
				di.setDATA_UPDATE_FLAG(last.getCorrectSign());
				di.setHEIGHT(String.valueOf(last.getHeightOfSationGroundAboveMeanSeaLevel()));
				
				try {
					pStatement.execute();
					listDi.add(di);
					return DataBaseAction.SUCCESS;
					
				} catch (SQLException e) {
					di.setPROCESS_STATE("0"); //1成功，0 失败
					listDi.add(di);	
					infoLogger.error("\n File name: "+fileN+
							"\n " + last.getStationNumberChina() + " " + sdf.format(last.getObservationTime())
							+"\n execute sql error: "+((LoggableStatement)pStatement).getQueryString()+"\n "+e.getMessage());
					return DataBaseAction.INSERT_ERROR;
				}
			}catch (SQLException e) {
				infoLogger.error("\n Create Statement error: "+e.getMessage());
				return DataBaseAction.INSERT_ERROR;
			}finally {
				if(pStatement != null) {
					try {
						pStatement.close();
					} catch (SQLException e) {
						e.printStackTrace();
						infoLogger.error("\n Close Statement error: "+e.getMessage());
					}
				}	
			}
		}else {
			infoLogger.error("\n Database connection error!");
			return DataBaseAction.CONNECTION_ERROR;
		}	
	}

/**
 * 
 * @Title: insertEleDB
 * @Description:(全国城镇天气预报最终报告要素表（批量）)
 * @param list_normal 入库对象集合
 * @param connection 
 * @param recv_time 接收时间
 * @param fileN 
 * @return DataBaseAction 数据入库状态
 * @throws：
 */
	private static  DataBaseAction insertEleDB(List<CityForecastLast> list_normal, java.sql.Connection connection, Date recv_time, String fileN) {
		PreparedStatement pStatement = null;
		try {
			// sql 语句
			String sql = "INSERT INTO "+StartConfig.valueTable()+" (D_ELE_ID,D_RECORD_ID,D_UPDATE_TIME,D_DATETIME,V04320,V12001,"
					+ "V13003,V11001,V11002,V10004,V13016,V20010,V20051,V20312,V20001,V12016,V12017,V13008_24,V13007_24,"
					+ "V13023,V13022,V20010_12,V20051_12,V20312_12,V11001_12,V11040_12,V_BBB,V01301,V01300,D_SOURCE_ID) VALUES ("
					+ "?,?, ?, ?, ?, ?, ?,?, ?, ?,"
					+ "?,?, ?, ?, ?, ?, ?,?, ?, ?,"
					+ "?,?, ?, ?, ?, ?, ?,?,?, ?)";
			if(connection != null){
				connection.setAutoCommit(false);
				pStatement = connection.prepareStatement(sql); 
//				if(StartConfig.getDatabaseType() == 1) {
//					pStatement.execute("select last_txc_xid()");
//				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				for(int i =0; i<list_normal.size();i++){
					CityForecastLast last_list = list_normal.get(i);
					// 获取要素行数
					for (int j = 0; j < last_list.getEle().size(); j++) {
						CityForeList list = last_list.getEle().get(j);
						int ii=1;
						String primkey = sdf.format(last_list.getObservationTime()) +"_"+sdf.format(last_list.getForecastTime())+"_"+last_list.getStationNumberChina()+"_"+last_list.getProd_Code();
						pStatement.setString(ii++, primkey+"_"+list.getValidtime());//要素标识
						pStatement.setString(ii++, primkey);//记录标识
						pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间
						pStatement.setTimestamp(ii++, new Timestamp(last_list.getObservationTime().getTime()));//资料时间
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getValidtime())));//预报时效
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getTEM())));//温度/气温
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getRHU())));//相对湿度
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getWIN_D())));//风向
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getWIN_S())));//风速
						if(list.getPRS().doubleValue() == 999999.0 || list.getPRS().doubleValue() == 999998.0)
							pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getPRS())) );
						else
							pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getPRS() + 1000)));//气压
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getPRE_PRE_Fore())));//可降水分（预报降水量）
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getCLO_Cov())));//总云量
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getCLO_Cov_Low())));//低云量
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getWEP())));//天气现象
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getVIS())));//水平能见度(人工)
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getTEM_Max_24h())));//未来24小时最高气温
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getTEM_Min_24h())));//未来24小时最低气温
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getRHU_Max_24h())));//24小时最大相对湿度
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getRHU_Min_24h())));//24小时最小相对湿度
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getPRE_24h())));//未来24小时降水量
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getPRE_12h())));//未来12小时降水量
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getCLO_Cov_12h())));//12小时内总云量
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getCLO_Cov_Low_12h())));//12小时内低云量
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getWEP_Past_12h())));//12小时内天气现象
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getWIN_PD_12h())));//12小时内盛行风向
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getWIN_S_Max_12h())));//12小时内最大风速
						pStatement.setString(ii++, last_list.getCorrectSign());//更正标志
						pStatement.setString(ii++, last_list.getStationNumberChina());
						pStatement.setInt(ii++,Integer.parseInt(StationCodeUtil.stringToAscii(last_list.getStationNumberChina())) );//区站号(数字)
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
					infoLogger.error("\n Element Table batch commit failed: "+fileN );
					return DataBaseAction.BATCH_ERROR;
				}
			}else {
				infoLogger.error("\n Database connection error!");
				return DataBaseAction.CONNECTION_ERROR;
			}
		} catch (Exception e) {
			infoLogger.error("\n Database connection error: " + e.getMessage());
			return DataBaseAction.CONNECTION_ERROR;
		}finally {
			if(pStatement != null) {
				try {
					pStatement.close();
					pStatement = null;
				} catch (SQLException e) {
					infoLogger.error("\n Close statement error!");
				}
			}
		}
	}
	/**
	 * 
	 * @Title: insertOneLine_Ele_db
	 * @Description:(全国城镇天气预报最终报告要素表（单条）)
	 * @param fine_list 入库对象集合
	 * @param connection 数据库连接
	 * @param recv_time void  接收时间
	 * @param fileN 
	 * @throws：
	 */
	private static void insertOneLine_Ele_db(CityForecastLast last , java.sql.Connection connection, Date recv_time, String fileN) {
		PreparedStatement pStatement = null;
		// sql 语句
		String sql = "INSERT INTO "+StartConfig.valueTable()+" (D_RECORD_ID,D_UPDATE_TIME,D_DATETIME,V04320,V12001,"
					  + "V13003,V11001,V11002,V10004,V13016,V20010,V20051,V20312,V20001,V12016,V12017,V13008_24,V13007_24,"
					  + "V13023,V13022,V20010_12,V20051_12,V20312_12,V11001_12,V11040_12,V_BBB,V01301,V01300,D_SOURCE_ID) VALUES ("
					  + "?, ?, ?, ?, ?, ?,?, ?, ?,"
					  + "?,?, ?, ?, ?, ?, ?,?, ?, ?,"
					  + "?,?, ?, ?, ?, ?, ?,?,?,?)";
		if(connection !=null){
			try {
				connection.setAutoCommit(false);
				pStatement = connection.prepareStatement(sql); 
//				if(StartConfig.getDatabaseType() == 1) {
//					pStatement.execute("select last_txc_xid()");
//				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				for (int i = 0; i < last.getEle().size(); i++) {
					CityForeList  list = last.getEle().get(i);
					int ii=1;
					String primkey = sdf.format(last.getObservationTime()) +"_"+sdf.format(last.getForecastTime())+"_"+last.getStationNumberChina()+"_"+last.getProd_Code();
					//pStatement.setString(ii++, primkey+"_"+list.getValidtime());//要素标识
					pStatement.setString(ii++, primkey);//记录标识
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间
					pStatement.setTimestamp(ii++, new Timestamp(last.getObservationTime().getTime()));//资料时间
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getValidtime())));//预报时效
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getTEM())));//温度/气温
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getRHU())));//相对湿度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getWIN_D())));//风向
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getWIN_S())));//风速
					if(list.getPRS().doubleValue() == 999999.0 || list.getPRS().doubleValue() == 999998.0)
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getPRS() )));
					else
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getPRS() + 1000)));//气压
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getPRE_PRE_Fore())));//可降水分（预报降水量）
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getCLO_Cov())));//总云量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getCLO_Cov_Low())));//低云量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getWEP())));//天气现象
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getVIS())));//水平能见度(人工)
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getTEM_Max_24h())));//未来24小时最高气温
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getTEM_Min_24h())));//未来24小时最低气温
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getRHU_Max_24h())));//24小时最大相对湿度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getRHU_Min_24h())));//24小时最小相对湿度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getPRE_24h())));//未来24小时降水量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getPRE_12h())));//未来12小时降水量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getCLO_Cov_12h())));//12小时内总云量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getCLO_Cov_Low_12h())));//12小时内低云量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getWEP_Past_12h())));//12小时内天气现象
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getWIN_PD_12h())));//12小时内盛行风向
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getWIN_S_Max_12h())));//12小时内最大风速
					pStatement.setString(ii++, last.getCorrectSign());//更正标志	
					pStatement.setString(ii++, last.getStationNumberChina());
					pStatement.setInt(ii++,Integer.parseInt(StationCodeUtil.stringToAscii(last.getStationNumberChina())) );//区站号(数字)\
					pStatement.setString(ii++, cts_code);
					pStatement.addBatch();	
				}
				try {
					pStatement.executeBatch();
					connection.commit();
				} catch (Exception e) {
					connection.rollback();
					infoLogger.error("\n File name: "+fileN+
							"\n " + last.getStationNumberChina() + " " + sdf.format(last.getObservationTime())
							+"\n "+e.getMessage());
				}
			}catch (Exception e) {
				infoLogger.error("\n Create PreparedStatement error: " +e.getMessage());
			}finally {
				try {
					if(pStatement!=null){
						pStatement.close();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}else {
			infoLogger.error("\n Database connection error!");
		}	
	}
	
	/**
	 * 
	 * @Title: updatekeyDB
	 * @Description:(更新键表)
	 * @param list_correct 更新对象集合
	 * @param connection 数据库连接
	 * @param recv_time 接收时间
	 * @param fileN 
	 * @param forecast_max 最大预报时效
	 * @param interval_max 最大时效间隔
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static void updatekeyDB(List<CityForecastLast> list_correct, java.sql.Connection connection, Date recv_time, String fileN) {
		PreparedStatement Pstat = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		for(int i=0;i<list_correct.size();i++){
			String vbbbInDB = null;
			//插入前，从DB中查找该条记录的状态，有、无、更正状态
			vbbbInDB = findVBBB_key(list_correct.get(i),connection);
			if(vbbbInDB == null){//该更正报之前数据库中没有该条记录，直接插入,给UPDATE_TIME赋值
				DataBaseAction  updatekey =insertOneLine_key_db(list_correct.get(i), connection, recv_time,fileN);	
				if (updatekey==DataBaseAction.SUCCESS) {
					insertOneLine_Ele_db(list_correct.get(i), connection, recv_time,fileN);				
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
						+ " V_BBB=?,D_UPDATE_TIME=?"
						+ " WHERE V01301=? AND D_Datetime=? AND V04001=?"
						+ " AND V04002=? AND V04003=? AND V04004=? AND V04005=? AND V_PROD_CODE=? ";
				if(connection != null){
					try {
						connection.setAutoCommit(true);
						Pstat = new LoggableStatement(connection, sql);
//						if(StartConfig.getDatabaseType() == 1) {
//							Pstat.execute("select last_txc_xid()");
//						}
						int ii = 1;
						CityForecastLast last = list_correct.get(i);
//						int strYear = last.getObservationTime().getYear() + 1900;
//						int strMonth = last.getObservationTime().getMonth() + 1;
//						int strDate = last.getObservationTime().getDate();
						Date date = last.getForecastTime();
						Pstat.setString(ii++, last.getCorrectSign());//更正标识
						Pstat.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间			
						Pstat.setString(ii++,last.getStationNumberChina());//站号
//						Pstat.setInt(ii++, strYear);//资料观测年
//						Pstat.setInt(ii++, strMonth);//资料观测月
//						Pstat.setInt(ii++, strDate);//资料观测月
//						Pstat.setInt(ii++, last.getObservationTime().getHours());//资料观测日
						Pstat.setTimestamp(ii++, new Timestamp(last.getObservationTime().getTime()));
						
						Pstat.setInt(ii++, date.getYear() + 1900);
						Pstat.setInt(ii++, date.getMonth() + 1);
						Pstat.setInt(ii++, date.getDate());
						Pstat.setInt(ii++, date.getHours());
						Pstat.setInt(ii++, date.getMinutes());
						
//						Pstat.setInt(ii++, last.getObservationTime().getMinutes());//资料观测分
						Pstat.setString(ii++, last.getProd_Code());//产品描述
						di.setIIiii(last.getStationNumberChina());
						di.setDATA_TIME(TimeUtil.date2String(last.getForecastTime(), "yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setLATITUDE(String.valueOf(last.getLatitude()));
						di.setLONGTITUDE(String.valueOf(last.getLongitude()));
						
						di.setSEND("BFDB");
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(new File(fileN).length()));
						di.setDATA_UPDATE_FLAG(last.getCorrectSign());
						di.setHEIGHT(String.valueOf(last.getHeightOfSationGroundAboveMeanSeaLevel()));
						
						try{
							Pstat.execute();	
							listDi.add(di);
							updateEle(list_correct.get(i), recv_time, connection,fileN);
						}
						catch(SQLException e){							
							di.setPROCESS_STATE("0");//1成功，0失败
							infoLogger.error("\n File name: "+fileN+
									"\n " + list_correct.get(i).getStationNumberChina() + " " + sdf.format(list_correct.get(i).getObservationTime())
									+"\n execute sql error: "+((LoggableStatement)Pstat).getQueryString()+"\n "+e.getMessage());
						}				
					}	catch (SQLException e) {
					infoLogger.error("\n Create Statement error: "+ e.getMessage());
					}finally {
						try {
							if(Pstat != null)
								Pstat.close();
						} catch (SQLException e) {
							infoLogger.error("\n Close Statement error: "+ e.getMessage());
						}	
					}
				}else
					infoLogger.error("\n Database connection error:");		
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
	 * @param fileN 
	 * @throws：
	 */
	private static void updateEle(CityForecastLast last, Date  recv_time ,java.sql.Connection connection, String fileN) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");				 
        PreparedStatement pStatement = null;
		String sql ="UPDATE "+StartConfig.valueTable()+" SET"
				+ " V_BBB=?, D_UPDATE_TIME=?, V04320=?, V12001=?, V13003=?, V11001=?, V11002=?, V10004=?, V13016=?, "
				+ " V20010=?, V20051=?, V20312=?, V20001=?, V12016=?, V12017=?, V13008_24=?, V13007_24=?,"
				+ " V13023=?, V13022=?, V20010_12=?, V20051_12=?, V20312_12=?, V11001_12=?, V11040_12=?"
				+ "	WHERE D_RECORD_ID=? AND V04320=? and D_datetime=?";
		
		if(connection != null){
			try{
				connection.setAutoCommit(false);
				pStatement = new LoggableStatement(connection, sql);
//				if(StartConfig.getDatabaseType() == 1) {
//					pStatement.execute("select last_txc_xid()");
//				}
				int j =0;
				int list_size = last.getEle().size();
				for(int i = 0; i < list_size; i ++){
					int ii = 1;
					CityForeList list =last.getEle().get(j); 
					pStatement.setString(ii++, last.getCorrectSign());//更正标识
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间			
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getValidtime())));//预报时效
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getTEM())));//温度/气温
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getRHU())));//相对湿度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getWIN_D())));//风向
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getWIN_S())));//风速
					if(list.getPRS().doubleValue() == 999999.0 || list.getPRS().doubleValue() == 999998.0)
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getPRS())) );
					else
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getPRS() + 1000)));//气压
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getPRE_PRE_Fore())));//可降水分（预报降水量）
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getCLO_Cov())));//总云量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getCLO_Cov_Low())));//低云量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getWEP())));//天气现象
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getVIS())));//水平能见度(人工)
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getTEM_Max_24h())));//未来24小时最高气温
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getTEM_Min_24h())));//未来24小时最低气温
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getRHU_Max_24h())));//24小时最大相对湿度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getRHU_Min_24h())));//24小时最小相对湿度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getPRE_24h())));//未来24小时降水量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getPRE_12h())));//未来12小时降水量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getCLO_Cov_12h())));//12小时内总云量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getCLO_Cov_Low_12h())));//12小时内低云量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getWEP_Past_12h())));//12小时内天气现象
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getWIN_PD_12h())));//12小时内盛行风向
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getWIN_S_Max_12h())));//12小时内最大风速
					String primary_key = sdf.format(last.getObservationTime()) +"_"+sdf.format(last.getForecastTime())+"_"+last.getStationNumberChina()+"_"+last.getProd_Code();
					pStatement.setString(ii++,primary_key);//记录标识
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.getValidtime())));//预报时效
					
					pStatement.setTimestamp(ii++, new Timestamp(last.getObservationTime().getTime()));
					
					pStatement.addBatch();
				}
				try{
					pStatement.executeBatch();
					connection.commit();
				}
				catch(SQLException e){		
					connection.rollback();
					infoLogger.error("\n File name: "+fileN+
							"\n " + last.getStationNumberChina() + " " + sdf.format(last.getObservationTime())
							+"\n execute sql error："+((LoggableStatement)pStatement).getQueryString()+"\n "+e.getMessage());
				}					
			}catch (SQLException e) {
				infoLogger.error("\n Create Statement error: "+ e.getMessage());
			}finally {
				try {
					if(pStatement != null)
						pStatement.close();
				} catch (SQLException e) {
					infoLogger.error("\n Close Statement error: "+ e.getMessage());
				}	
			}
		}else
			infoLogger.error("\n Database connection error!");				
	}
	
	/**
	 * @Title: findVBBB_key 
	 * @Description:(更正报文  需要查询原始报文是否存在) 
	 * @param connection  数据库连接对象
	 * @return  
	 *    String  更正标识  如果原始报文不存在 则返回  null
	 * @throws
	 */
	private static String findVBBB_key(CityForecastLast last, java.sql.Connection connection) {
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
				Pstmt.setString(ii++, last.getStationNumberChina());//站号
				Pstmt.setString(ii++, sdf.format(last.getObservationTime()) +"_"+sdf.format(last.getForecastTime()) +"_"+last.getStationNumberChina()+"_"+last.getProd_Code());
				resultSet = Pstmt.executeQuery();
				if(resultSet.next())
					rntString= resultSet.getString(1);
			}
		}catch(SQLException e){
			infoLogger.error("\n Create PreparedStatement failed: " + e.getMessage());
		}finally {
			if(Pstmt != null) {
				try {
					Pstmt.close();
				} catch (SQLException e) {
					infoLogger.error("\n Close PreparedStatement failed: " + e.getMessage());
				}
			}
			if(resultSet != null){
				try{
					resultSet.close();
				}
				catch (Exception e) {
					infoLogger.error("\n close resultset error!");
				}
			}
		}
		return rntString;	
	}
}
