package cma.cimiss2.dpc.indb.sevp.dc_city_forcast.service;

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
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.sevp.CityWeatherForeCast;
import cma.cimiss2.dpc.decoder.sevp.BullHeader;
import cma.cimiss2.dpc.decoder.sevp.DecodeCityForecast;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class DbService {
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	public static String sod_report_code = StartConfig.reportSodCode();
//	static Map<String, Object> proMap = StationInfo.getProMap();
	
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}

	public static void main(String[] args) {
		DecodeCityForecast decode=new DecodeCityForecast();
		String filepath = "C:\\BaiduNetdiskDownload\\test_del\\M.0001.0002.R001\\FS_MSG__WP050855.CGZ";
		File file = new File(filepath);
		ParseResult<CityWeatherForeCast> parseResult = decode.DecodeFile(file);
		DataBaseAction action = null;
		Date recv_time = new Date();
//		StringBuffer loggerBuffer = new StringBuffer();

//		if (StartConfig.getDatabaseType() == 1) {
			action = DbService.processSuccessReport(parseResult, filepath, recv_time);
			System.out.println("insertDBService over!");
//		}
	
	}
	
	/**
	 * 
	 * @Title: processSuccessReport   
	 * @Description:(报文解码入库函数)   
	 * @param  parseResult  存放解码结果
	 * @param filepath 文件路径
	 * @param  recv_time  报文接收时间   
	 * @return: DataBaseAction      
	 * @throws:
	 */
	public static DataBaseAction processSuccessReport(ParseResult<CityWeatherForeCast> parseResult, String fileN, Date recv_time) {
		java.sql.Connection connection = null;
		java.sql.Connection reportConnection = null;
		try {
			// 获取数据库连接
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			// 获取解码结果集
			List<CityWeatherForeCast> cityWeatherForeCasts = parseResult.getData();
			insertDB(cityWeatherForeCasts, connection, recv_time,fileN);
			@SuppressWarnings("rawtypes")
			List<ReportInfo> reportInfos = parseResult.getReports();
			reportConnection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
			reportInfoToDb(reportInfos, reportConnection, recv_time,fileN);  
			return DataBaseAction.SUCCESS;
		} catch (Exception e) {
			infoLogger.error("\n Database connection error!");
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
	 * @Title: insertDB
	 * @Description:(城镇天气预报报告(FS)键表入库)   
	 * @param cityWeatherForeCasts 待入库对象集合
	 * @param connection 数据库连接
	 * @param recv_time 资料接收接收
	 * @param fileN 
	 * @return void 
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static void insertDB(List<CityWeatherForeCast> cityWeatherForeCasts, java.sql.Connection connection, Date recv_time, String fileN){
		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement pStatement = null;
		String valueTable = StartConfig.valueTable();
		System.out.println(valueTable);
		String sql = "INSERT INTO " + valueTable + "(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,D_UPDATE_TIME,"
			+ "V_BBB,V01301,V01300,V04001,V04002,V04003,V04004,V04320,V20312_20_08,V20312_08_20,"
			+ "V11303,V11313,V11301,V11302,V12021,V12022,D_SOURCE_ID) "
			+ "VALUES (?,?, ?, ?, ?, ?,"
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, ?) " ;
		List<CityWeatherForeCast> cityWeatherForeCasts2 = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		if(connection != null){		
			try {	
				pStatement = new LoggableStatement(connection, sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				connection.setAutoCommit(false);
				List<String> sqls = new ArrayList<>();	
				String V_TT = "";
				String bbb ;
				String V_BBB;
				for(int idx = 0; idx < cityWeatherForeCasts.size(); idx ++){
					CityWeatherForeCast cityWeatherForeCast = cityWeatherForeCasts.get(idx);
					V_BBB = cityWeatherForeCast.getCorrectSign();
					bbb = findVBB(cityWeatherForeCast, connection);
					V_TT = cityWeatherForeCast.getBullHeader().getTt();
					if(V_BBB.equals("000") || bbb == null){ 
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
						
						Date dataTime = cityWeatherForeCast.getObservationTime();
						String station = cityWeatherForeCast.getStationNumberChina();
						int ii = 1;
						pStatement.setString(ii++, sdf.format(dataTime)+"_"+station+"_"+cityWeatherForeCast.getForecastEfficiency());
						pStatement.setString(ii++, sod_code);
						pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
						pStatement.setTimestamp(ii++, new Timestamp(dataTime.getTime()));
						pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						// "V_BBB,V01301,V01300,V04001,V04002,V04003,V04004,V04320,V20312_20_08,V20312_08_20,"
						pStatement.setString(ii++, cityWeatherForeCast.getCorrectSign());  // 更正报标识
						pStatement.setString(ii++, station);
						pStatement.setInt(ii++, Integer.parseInt(StationCodeUtil.stringToAscii(station)));
						pStatement.setInt(ii++, dataTime.getYear() + 1900); // 资料年
						pStatement.setInt(ii++, dataTime.getMonth() + 1); // 资料月
						pStatement.setInt(ii++, dataTime.getDate()); // 资料日
						pStatement.setInt(ii++, dataTime.getHours()); //资料时
						pStatement.setInt(ii++, cityWeatherForeCast.getForecastEfficiency()); 
						pStatement.setInt(ii++, cityWeatherForeCast.getWeatherPhenomenon20_08());
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(cityWeatherForeCast.getWeatherPhenomenon08_20())));
						// + "V11303,V11313,V11301,V11302,V12021,V12022) 
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(cityWeatherForeCast.getWindDir())));
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(cityWeatherForeCast.getWindTurnDir())));
						pStatement.setInt(ii++, cityWeatherForeCast.getWindLevel());
						pStatement.setInt(ii++, cityWeatherForeCast.getWindTurnLevel());
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(cityWeatherForeCast.getMinTemperature())));
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(cityWeatherForeCast.getMaxTemperature())));
						pStatement.setString(ii++, cts_code);
						
						double latitude = 999999;
						double longtitude = 999999;
						String info = (String) proMap.get(station + "+01");
						if(info == null) {
							infoLogger.error("\n In configuration file, this station does not exist: " + station);
						}
						else{
							String[] infos = info.split(",");
							if(infos[1].equals("null"))
								infoLogger.error("\n  In configuration file, longtitude is null!");
							else
								longtitude = Double.parseDouble(infos[1]);
							if(infos[2].equals("null"))
								infoLogger.error("\n  In configuration file, latitude is null!");
							else
								latitude = Double.parseDouble(infos[2]);
						}
						di.setIIiii(station);
						di.setDATA_TIME(TimeUtil.date2String(dataTime, "yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());	
						di.setLATITUDE(String.valueOf(latitude));
						di.setLONGTITUDE(String.valueOf(longtitude));
						
						di.setSEND("BFDB");
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(new File(fileN).length()));
						di.setDATA_UPDATE_FLAG(cityWeatherForeCast.getCorrectSign());
						di.setHEIGHT("999999");
						
						pStatement.addBatch();
						sqls.add(((LoggableStatement)pStatement).getQueryString()); // 批量入库冲突时，单条入库
						listDi.add(di);
					}
					else if(bbb.compareTo(V_BBB) < 0){
						cityWeatherForeCasts2.add(cityWeatherForeCast); // 更正报，需要update操作,数据库中有该条记录，且更正标识早于当前处理文件的更正标识的值
					}
					else{ // 数据库中有该条记录，且V_BBB的值晚于当前处理文件的更正标识的值
						// 不进行插入或更新
					}
				} 
				try{
					pStatement.executeBatch();
					connection.commit();
					sqls.clear();
				}catch(SQLException e){
					pStatement.clearParameters();
					pStatement.clearBatch();
					execute_sql(sqls, connection,fileN); // 此中，入库失败的会将对应的DI的ProcessState置为1 // 1:失败，0：成功
					infoLogger.error("\n Batch commit error: "+fileN);
				}
			}catch (SQLException e) {
				infoLogger.error("\n Create Statement error: "+e.getMessage());
			}
			finally {
				if(pStatement != null) {
					try {
						pStatement.close();
					} catch (SQLException e) {
						infoLogger.error("\n Close Statement error: "+e.getMessage());
					}
				}
				// 更正报的入库
				if(cityWeatherForeCasts2.size() > 0)
					updateDB(cityWeatherForeCasts2, connection, recv_time,fileN);
			}
		} 
	}
	
	/**
	 * 
	 * @Title: updateDB   
	 * @Description:(更正报报文的更新)   
	 * @param  cityWeatherForeCasts 待入库的对象
	 * @param  connection 数据库连接
	 * @param  recv_time   数据接收时间  
	 * @param fileN 
	 * @throws:
	 */
	@SuppressWarnings("deprecation")
	private static void updateDB(List<CityWeatherForeCast> cityWeatherForeCasts, java.sql.Connection connection, Date recv_time, String fileN){
		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement Pstmt = null;
		CityWeatherForeCast cityWeatherForeCast = new CityWeatherForeCast();
		String sql = "update " + StartConfig.valueTable() + " set "
				+ "V_BBB=?, D_UPDATE_TIME=?,"
				+ "V20312_20_08=?,V20312_08_20=?,V11303=?,V11313=?,V11301=?,V11302=?,V12021=?,V12022=? "
				+ "where v01301 = ? and V04320=? and D_datetime=?";
		if(connection != null){
			try{
				Pstmt = new LoggableStatement(connection, sql);
//				if(StartConfig.getDatabaseType() == 1) {
//					Pstmt.execute("select last_txc_xid()");
//				}
				for(int idx = 0; idx < cityWeatherForeCasts.size(); idx ++){
					cityWeatherForeCast = cityWeatherForeCasts.get(idx);
					StatDi di = new StatDi();				
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(sod_code);
					di.setDATA_TYPE_1(cts_code);
					di.setTT(cityWeatherForeCast.getBullHeader().getTt());			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1");  //1成功，0失败	
					
					Date dataTime = cityWeatherForeCast.getObservationTime();
					int ii = 1;
					Pstmt.setString(ii++, cityWeatherForeCast.getCorrectSign());
					Pstmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					
					Pstmt.setInt(ii++, cityWeatherForeCast.getWeatherPhenomenon20_08());
					Pstmt.setInt(ii++, cityWeatherForeCast.getWeatherPhenomenon08_20());
					Pstmt.setBigDecimal(ii++,new BigDecimal(String.valueOf(cityWeatherForeCast.getWindDir())));
					Pstmt.setBigDecimal(ii++,new BigDecimal(String.valueOf(cityWeatherForeCast.getWindTurnDir())));
					Pstmt.setInt(ii++, cityWeatherForeCast.getWindLevel());
					Pstmt.setInt(ii++, cityWeatherForeCast.getWindTurnLevel());
					Pstmt.setBigDecimal(ii++,new BigDecimal(String.valueOf(cityWeatherForeCast.getMinTemperature())));
					Pstmt.setBigDecimal(ii++,new BigDecimal(String.valueOf(cityWeatherForeCast.getMaxTemperature())));
				 
					Pstmt.setString(ii++, cityWeatherForeCast.getStationNumberChina());
//					Pstmt.setInt(ii++, dataTime.getYear() + 1900); // 资料年
//					Pstmt.setInt(ii++, dataTime.getMonth() + 1); // 资料月
//					Pstmt.setInt(ii++, dataTime.getDate()); // 资料日
//					Pstmt.setInt(ii++, dataTime.getHours()); //资料时
					Pstmt.setInt(ii++, cityWeatherForeCast.getForecastEfficiency()); 
					
					Pstmt.setTimestamp(ii++, new Timestamp(dataTime.getTime()));
					
					di.setIIiii(cityWeatherForeCast.getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(dataTime, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					double latitude = 999999;
					double longtitude = 999999;
					String info = (String) proMap.get(cityWeatherForeCast.getStationNumberChina() + "+01");
					if(info == null) {
						infoLogger.error("\n In configuration file, this station does not exist: " + cityWeatherForeCast.getStationNumberChina());
					}
					else{
						String[] infos = info.split(",");
						if(infos[1].equals("null"))
							infoLogger.error("\n In configuration file, longtitude is null!");
						else
							longtitude = Double.parseDouble(infos[1]);
						if(infos[2].equals("null"))
							infoLogger.error("\n In configuration file, latitude is null!");
						else
							latitude = Double.parseDouble(infos[2]);
					}
					di.setLATITUDE(String.valueOf(latitude));
					di.setLONGTITUDE(String.valueOf(longtitude));
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(fileN).length()));
					di.setDATA_UPDATE_FLAG(cityWeatherForeCast.getCorrectSign());
					di.setHEIGHT("999999");
					
					listDi.add(di);
					try{
						Pstmt.execute();
						connection.commit();
					}catch(SQLException e){							
						if(listDi.size() > 0)
							listDi.get(listDi.size() - 1).setPROCESS_STATE("0");//1成功，0失败
						infoLogger.error("\n File name: "+fileN
								+"\n execute sql error: "+((LoggableStatement)Pstmt).getQueryString()+"\n "+e.getMessage());
					}	
				}
			}
			catch (SQLException e) {
				infoLogger.error("\n Create Statement error: "+ e.getMessage());
			}
			finally {
				try {
					if(Pstmt != null)
						Pstmt.close();
				} catch (SQLException e) {
					infoLogger.error("\n Close Statement error: "+ e.getMessage());
				}	
			}
		}
		else 
			infoLogger.error("\n Database connection error!");		
	}
	
	/**
	 * 
	 * @Title: findVBB   
	 * @Description:(查找城镇天气预报报告（FS）数据的更正标识)
	 * @param cityWeatherForeCast  查找对象
	 * @param  connection  数据库连接   
	 * @return: String      返回查找到的V_BBB，未找到时，返回null
	 * @throws:
	 */
	@SuppressWarnings("deprecation")
	private static String findVBB(CityWeatherForeCast cityWeatherForeCast, java.sql.Connection connection){
		PreparedStatement Pstmt = null;
		ResultSet resultSet  = null;
		String rntString = null;
		String sql = "select V_BBB from "+ StartConfig.valueTable() 
				+ " where V01301 = ? and V04001 = ?"
				+ " and V04002 = ? and V04003 = ? and V04004 = ? and V04320=?";
		try{
			
//			if(StartConfig.getDatabaseType() == 1) {
//				Pstmt.execute("select last_txc_xid()");
//			}
			if(connection != null){		
				Pstmt = connection.prepareStatement(sql);
				Date date = cityWeatherForeCast.getObservationTime();
				int ii = 1;
//				Pstmt.setString(ii++, cityWeatherForeCast.getCorrectSign());
				Pstmt.setString(ii++, cityWeatherForeCast.getStationNumberChina());
				Pstmt.setInt(ii++, date.getYear() + 1900);
				Pstmt.setInt(ii++, date.getMonth()  + 1);
				Pstmt.setInt(ii++, date.getDate());
				Pstmt.setInt(ii++, date.getHours());
				Pstmt.setInt(ii++, cityWeatherForeCast.getForecastEfficiency());
				
				resultSet = Pstmt.executeQuery();
				if(resultSet.next()){
					rntString = resultSet.getString(1);
				}
			}
		}catch(SQLException e){
			infoLogger.error("\n Create query Statement error: " + e.getMessage());
		}
		finally {
			if(Pstmt != null) {
				try {
					Pstmt.close();
				} catch (SQLException e) {
					infoLogger.error("\n Close query Statement error: " + e.getMessage());
				}
			}
			if(resultSet != null){
				try{
					resultSet.close();
				}catch(SQLException e){
					infoLogger.error("\n Close resultSet error:  " + e.getMessage());
				}
			}
		}
		return rntString;
	}
	/**
	 * 
	 * @Title: reportInfoToDb   
	 * @Description:(报文信息入库)   
	 * @param  reportInfos 报文列表集合
	 * @param  connection  数据库连接
	 * @param  recv_time   资料时间
	 * @return: void      
	 * @throws:
	 */
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public static void reportInfoToDb( List<ReportInfo> reportInfos, java.sql.Connection connection, Date recv_time,String filepath) {
		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO " + StartConfig.reportTable()+ "(D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,"
				+ "V_BBB,V_CCCC,V04001_02,V04002_02,V04003_02,V04004_02,V_TT,V_AA,V_II,V_MIMJ,"
				+ "V01301,V01300,V05001,V06001,V04001,V04002,V04003,V04004,V04005,"
				+ "V_LEN,V_REPORT) VALUES"
				+ "(?,?,?,?,?,"
				+ " ?,?,?,?,?,?,?,?,?,?,"
				+ " ?,?,?,?,?,?,?,?,?,"
				+ " ?,?)";
		try {
			pStatement = new LoggableStatement(connection, sql);
			if(StartConfig.getDatabaseType() == 1) {
				pStatement.execute("select last_txc_xid()");
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String V_TT= "";
			CityWeatherForeCast cityWeatherForeCast = null;
			Date oTime = null;
			String primkey = null;
			BullHeader bullHeader;
			String sta;
			String info;
			double latitude = 999999;
			double longtitude = 999999;
			File file=new File(filepath);
			String fileN =file.getName();
			for (int i = 0; i < reportInfos.size(); i++) {
				try {
					StatDi di = new StatDi();	
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(sod_report_code);
					di.setDATA_TYPE_1(cts_code);
					di.setTT(V_TT);			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1");  //1成功，0失败
					
					cityWeatherForeCast = (CityWeatherForeCast) reportInfos.get(i).getT();
					sta = cityWeatherForeCast.getStationNumberChina();
					bullHeader = cityWeatherForeCast.getBullHeader();
					V_TT = cityWeatherForeCast.getBullHeader().getTt();
					oTime = cityWeatherForeCast.getObservationTime();
					primkey = sdf.format(oTime)+"_"+sta+"_"+V_TT+"_"+bullHeader.getCccc()+"_"+bullHeader.getBbb();
					int ii = 1;
					//pStatement.setString(ii++, primkey);
					pStatement.setString(ii++, sod_report_code);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(oTime.getTime()));
			//		"V_BBB,V_CCCC,V04001_02,V04002_02,V04003_02,V04004_02,V_TT,V_AA,V_II,V_MIMJ,"
					pStatement.setString(ii++, bullHeader.getBbb());
					pStatement.setString(ii++, bullHeader.getCccc());  // 编报中心
					pStatement.setInt(ii++, oTime.getYear() + 1900);
					pStatement.setInt(ii++, oTime.getMonth() + 1);
					pStatement.setInt(ii++, oTime.getDate());
					pStatement.setInt(ii++, oTime.getHours());
					pStatement.setString(ii++, V_TT);  // 报告类别
					pStatement.setString(ii++, bullHeader.getAa());
					pStatement.setString(ii++, bullHeader.getIi());
					pStatement.setString(ii++, V_TT); // 资料类别
					//+ "V01301,V01300,V05001,V06001,V04001,V04002,V04003,V04004,V04005,"
					pStatement.setString(ii++, sta);
					pStatement.setInt(ii++, Integer.parseInt(StationCodeUtil.stringToAscii(sta)));
					
					double stationHeight=999999.0;
					info = (String) proMap.get(sta + "+01");
					if(info == null) {
						infoLogger.error("\n In configuration file, this station does not exist:" + sta);
//						continue ;
					}
					else{
						String[] infos = info.split(",");						
						if(infos[1].equals("null"))
							infoLogger.error("\n In configuration file, longtitude is null!");
						else
							longtitude = Double.parseDouble(infos[1]);
						if(infos[2].equals("null"))
							infoLogger.error("\n In configuration file, latitude is null!");
						else
							latitude = Double.parseDouble(infos[2]);
						if(!infos[3].equals("null"))
							stationHeight = Double.parseDouble(infos[3]);
					}
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(latitude)));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(longtitude)));
					pStatement.setInt(ii++, oTime.getYear() + 1900);
					pStatement.setInt(ii++, oTime.getMonth() + 1);
					pStatement.setInt(ii++, oTime.getDate());
					pStatement.setInt(ii++, oTime.getHours());
					pStatement.setInt(ii++, oTime.getMinutes());
					// "V_LEN,V_REPORT) 
					pStatement.setInt(ii++, reportInfos.get(i).getReport().length());
					pStatement.setString(ii++, reportInfos.get(i).getReport());
					
					di.setIIiii(sta);
					di.setDATA_TIME(TimeUtil.date2String(oTime, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLONGTITUDE(String.valueOf(latitude));
					di.setLATITUDE(String.valueOf(longtitude));
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
					di.setDATA_UPDATE_FLAG(bullHeader.getBbb());
					di.setHEIGHT(String.valueOf(stationHeight));
					
					try {
						pStatement.execute();
						if(connection.getAutoCommit() == false)
							connection.commit();
						listDi.add(di);
					} catch (SQLException e) {
						di.setPROCESS_STATE("0");//1成功，0失败
						listDi.add(di);
					}
				} catch (Exception e) {
					infoLogger.error("sql error:" + ((LoggableStatement)pStatement).getQueryString());
					infoLogger.error("Because: " + e.getMessage());
					continue;
				}
			}  
			if(pStatement != null) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					infoLogger.error("\n Close Statement error: "+e.getMessage());
				}
			}
		} catch (SQLException e) {
			infoLogger.error("Database connection error: " + e.getMessage());
			return;
		}
	}	
	/**
	 * 
	 * @Title: execute_sql   
	 * @Description:(批量入库失败时，采用逐条提交)   
	 * @param  sqls 待执行的查询语句
	 * @param  connection      数据库连接
	 * @param fileN 
	 * @return: void      
	 * @throws:
	 */
	private static void execute_sql(List<String> sqls, java.sql.Connection connection, String fileN) {
		Statement pStatement = null;
		try {
			connection.setAutoCommit(true);
			pStatement = connection.createStatement();
			for (int i = 0; i < sqls.size(); i++) {
//				
				try {
					pStatement.execute(sqls.get(i));
				} catch (Exception e) {
					infoLogger.error("\n File name: "+fileN
							+"\n " + listDi.get(i).getIIiii() + " " + listDi.get(i).getDATA_TIME()
							+"\n execute sql error: "+sqls.get(i)+"\n "+e.getMessage());
					listDi.get(i).setPROCESS_STATE("0");
				}
			}
		} catch (SQLException e) {
			infoLogger.error("\n Create Statement error: "+e.getMessage());
		}finally {
			if(pStatement != null) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					infoLogger.error("\n Close Statement error: "+e.getMessage());
				}
			}
		}		
		
	}
}
