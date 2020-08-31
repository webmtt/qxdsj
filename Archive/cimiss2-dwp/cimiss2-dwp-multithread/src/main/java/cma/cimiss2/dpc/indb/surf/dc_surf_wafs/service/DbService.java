package cma.cimiss2.dpc.indb.surf.dc_surf_wafs.service;
import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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
import cma.cimiss2.dpc.decoder.bean.upar.WAFS;
import cma.cimiss2.dpc.decoder.surf.DecodeWAFS;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

// TODO: Auto-generated Javadoc
/**
 * The Class DbService.
 */
public class DbService {
	
	/** The list di. */
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	
	/** The di queues. */
	public static BlockingQueue<StatDi> diQueues;
	
	/** The Constant infoLogger. */
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	
	/** The cts code. */
	public static String cts_code = StartConfig.ctsCode();
	
	/** The sod code. */
	public static String sod_code = StartConfig.sodCode();
	
	/** The sod report code. */
	public static String sod_report_code = StartConfig.reportSodCode();
	
	/** The pro map. */
//	static Map<String, Object> proMap = StationInfo.getProMap();
	
	/**
	 * Sets the di queues.
	 *
	 * @param diQueues the new di queues
	 */
	public static void setDiQueues(BlockingQueue<StatDi> diQueues){
		DbService.diQueues = diQueues;
	}
	
	/**
	 * Gets the di queues.
	 *
	 * @return the di queues
	 */
	public static BlockingQueue<StatDi> getDiQueues(){
		return diQueues;
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		DecodeWAFS decodeWAFS = new DecodeWAFS();
		String filepath = "D:\\HUAXIN\\对比\\地面资料验证\\wafs-metar-抽检\\A_SAAL34DAAA160000_C_BABJ_20190516000905_71090.MSG";
		File file = new File(filepath);
		long length = StartConfig.maxFileSize();
		long filesize = file.length();
		if(file.length()  > StartConfig.maxFileSize()){
			System.out.println(file.length());
		}
		
		ParseResult<WAFS> parseResult = decodeWAFS.decodeWAFS(file);
		DataBaseAction action = null;
		Date recv_time = new Date();
		StringBuffer loggerBuffer = new StringBuffer();
	
//		if (StartConfig.getDatabaseType() == 1) {
			action = DbService.processSuccessReport(parseResult, filepath,recv_time,loggerBuffer);
			System.out.println("insertDBService over!");
//		}
	
	}
	
	
	/**
	 * Process success report.
	 *
	 * @param parseResult the parse result
	 * @param filepath the filepath
	 * @param recv_time the recv time
	 * @param loggerBuffer the logger buffer
	 * @return the data base action
	 * @Title: processSuccessReport
	 * @Description: TODO(飞机天气报的处理)
	 * @return: DataBaseAction
	 * @throws: 
	 */
	public static DataBaseAction processSuccessReport(ParseResult<WAFS> parseResult, String filepath, Date recv_time, StringBuffer loggerBuffer) {
		java.sql.Connection connection = null;
//		java.sql.Connection reportConnection = null;
		try {
			// 获取数据库连接
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			List<WAFS> wafss = parseResult.getData();
			insertDB(wafss, connection, recv_time, filepath, loggerBuffer);
		
//			@SuppressWarnings("rawtypes")
//			List<ReportInfo> reportInfos = parseResult.getReports();
//			reportConnection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
//			reportInfoToDb(reportInfos, reportConnection, recv_time);  
			
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
					infoLogger.error("\n Close database connection error: "+e.getMessage());
				}
			}
			
//			if(reportConnection != null) {
//				try {
//					reportConnection.close();
//				} catch (SQLException e) {
//					infoLogger.error("\n Close database connection error"+e.getMessage());
//				}
//			}
		}
	}
	
	/**
	 * Insert DB.
	 *
	 * @param wafss the wafss
	 * @param connection the connection
	 * @param recv_time the recv time
	 * @param fileN the file N
	 * @param loggerBuffer the logger buffer
	 * @Title: insertDB
	 * @Description: TODO(WAFS机场地面天气资料入库)
	 * @return: void
	 * @throws: 
	 */
	private static void insertDB(List<WAFS> wafss, java.sql.Connection connection, Date recv_time, String fileN, StringBuffer loggerBuffer) {
		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,D_UPDATE_TIME,"
			+ "V_CCCC,V_OBCC,C_TYPE,C05021_001,C05021_002,C_RW1,C_RWVT1,C_RW2,C_RWVT2,C_RW3,"
			+ "C_RWVT3,C_RW4,C_RWVT4,C_RW5,C_RWVT5,C20003_001,C20003_002,C20003_003,C20011_001,C20011_002,"
			+ "C20011_003,C20003_004,C20003_005,C20003_006,V07001,V05001,V06001,V10004,V11001,V11002,"
			+ "V11041,V11041_001,V11041_002,V20001_001,V20001_002,V20002,V_RWVMIN1,V_RWVMAX1,V_RWVMIN2,V_RWVMAX2,"
			+ "V_RWVMIN3,V_RWVMAX3,V_RWVMIN4,V_RWVMAX4,V_RWVMIN5,V_RWVMAX5,V20013_001,V20013_002,V20013_003,V12001,"
			+ "V12003,V10051,V_NCODE,V02001,V04001,V04002,V04003,V04004,V04005,V_BBB,"
			+ "C_RWY1,C_RWY2,C_RWY3,C_RMK,D_SOURCE_ID) "
			+ "VALUES (?, ?, ?, ?, ?,"
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?) " ;
		if(connection != null){		
			try {	
				pStatement = new LoggableStatement(connection, sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				List<String> sqls = new ArrayList<>();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");		
				WAFS wafs = new WAFS();
				String V_TT = "";
				for(int idx = 0; idx < wafss.size(); idx ++){
					wafs = wafss.get(idx);
					if(wafs.getReportType().equals("METAR") || wafs.getReportType().equals("SA"))
						V_TT = "SA";
					else V_TT = "SP";
					StatDi di = new StatDi();	
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(sod_code);
					di.setDATA_TYPE_1(cts_code);
					di.setTT(wafs.getReportType());			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1");  //1成功，0失败	
					
					String stat = wafs.getTerminalSign();
					String info = (String) proMap.get(stat + "+01");
					double latitude = 999999.0;
					double longtitude = 999999.0;
					double statHeight = 999999.0;
					int countryCode = 999999;
					if(info == null) {
						loggerBuffer.append("\n  In configuration file, this station does not exist: " + stat);
//						continue ;
					}
					else{
						String[] infos = info.split(",");
						if(infos[1].equals("null")){
							loggerBuffer.append("\n In configuration file, longtitude is null!");
//							continue ;
						}
						else
							longtitude = Double.parseDouble(infos[1]);
						if(infos[2].equals("null")){
							loggerBuffer.append("\n  In configuration file, latitude is null!");
//							continue;
						}
						else
							latitude = Double.parseDouble(infos[2]);
						if(!infos[3].equals("null"))
							statHeight = Double.parseDouble(infos[3]);
						if(!infos[4].equals("null"))
							countryCode = Integer.parseInt(infos[4]);
					}
					int ii = 1;
//					pStatement.setString(ii++, sdf.format(wafs.getObservationTime())+"_"+wafs.getTerminalSign()+"_"+wafs.getReportType()+"_"+wafs.getReportCenter()+"_"+wafs.getCorrectSign());
//					pStatement.setString(ii++, sdf.format(wafs.getObservationTime())+"_"+wafs.getTerminalSign()+"_"+wafs.getReportCenter()+"_"+wafs.getReportType());
					
					pStatement.setString(ii++, sod_code);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					
					Date date = wafs.getObservationTime();
//		            Calendar calendar = Calendar.getInstance();
//		            calendar.setTime(date);
//		            calendar.set(Calendar.MINUTE, 0);
//		            calendar.set(Calendar.SECOND, 0);
//		            calendar.set(Calendar.MILLISECOND, 0);
//					pStatement.setTimestamp(ii++, new Timestamp(calendar.getTime().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(date.getTime()));
					
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					//"V_CCCC,V_OBCC,C_TYPE,C05021_001,C05021_002,C_RW1,C_RWVT1,C_RW2,C_RWVT2,C_RW3,"
					pStatement.setString(ii++, wafs.getReportCenter().replaceAll("\u0000", ""));
					pStatement.setString(ii++, wafs.getTerminalSign().replaceAll("\u0000", ""));
					pStatement.setString(ii++, wafs.getReportType().replaceAll("\u0000", ""));
					pStatement.setString(ii++, wafs.getDirectionOfHorizontalVisibility().replaceAll("\u0000", "")); // 水平能见度方向 
					pStatement.setString(ii++, wafs.getDirOfMaxHorizontalVisibility().replaceAll("\u0000", "")); // 最大水平能见度的方向
					pStatement.setString(ii++, wafs.getRunwayNumbers().get(0).replaceAll("\u0000", "")); // 跑道编号1 
					pStatement.setString(ii++, wafs.getTrendsOfRunwayViusalRange().get(0).toString().replaceAll("\u0000", "")); //跑道视程变化趋势1 
					pStatement.setString(ii++, wafs.getRunwayNumbers().get(1).replaceAll("\u0000", "")); // 跑道编号2 
					pStatement.setString(ii++, wafs.getTrendsOfRunwayViusalRange().get(1).toString().replaceAll("\u0000", "")); //跑道视程变化趋势2
					pStatement.setString(ii++, wafs.getRunwayNumbers().get(2).replaceAll("\u0000", "")); // 跑道编号3 
					// "C_RWVT3,C_RW4,C_RWVT4,C_RW5,C_RWVT5,C20003_001,C20003_002,C20003_003,C20011_001,C20011_002,"
					pStatement.setString(ii++, wafs.getTrendsOfRunwayViusalRange().get(2).toString().replaceAll("\u0000", "")); //跑道视程变化趋势3
					pStatement.setString(ii++, wafs.getRunwayNumbers().get(3).replaceAll("\u0000", "")); // 跑道编号4 
					pStatement.setString(ii++, wafs.getTrendsOfRunwayViusalRange().get(3).toString().replaceAll("\u0000", "")); //跑道视程变化趋势4
					pStatement.setString(ii++, wafs.getRunwayNumbers().get(4).replaceAll("\u0000", "")); // 跑道编号5 
					pStatement.setString(ii++, wafs.getTrendsOfRunwayViusalRange().get(4).toString().replaceAll("\u0000", "")); //跑道视程变化趋势5
					pStatement.setString(ii++, wafs.getWeatherPhenomenons().get(0).replaceAll("\u0000", ""));//天气现象1 ~ 3
					pStatement.setString(ii++, wafs.getWeatherPhenomenons().get(1).replaceAll("\u0000", ""));
					pStatement.setString(ii++, wafs.getWeatherPhenomenons().get(2).replaceAll("\u0000", ""));
					pStatement.setString(ii++, wafs.getCloudShapeAndAmount().get(0).replaceAll("\u0000", ""));//云状及云量1 ~ 3
					pStatement.setString(ii++, wafs.getCloudShapeAndAmount().get(1).replaceAll("\u0000", ""));
					// "C20011_003,C20003_004,C20003_005,C20003_006,V07001,V05001,V06001,V10004,V11001,V11002,"
					pStatement.setString(ii++, wafs.getCloudShapeAndAmount().get(2).replaceAll("\u0000", ""));
					pStatement.setString(ii++, wafs.getRecentWeatherPhenomenons().get(0).replaceAll("\u0000", ""));// 近时天气现象1 ~ 3
					pStatement.setString(ii++, wafs.getRecentWeatherPhenomenons().get(1).replaceAll("\u0000", ""));
					pStatement.setString(ii++, wafs.getRecentWeatherPhenomenons().get(2).replaceAll("\u0000", ""));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(statHeight)));// 测站高度
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(latitude))); // 纬度
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(longtitude))); //经度
					pStatement.setDouble(ii++,999998); // 本站气压
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(wafs.getWindDirection()))); // 风向 
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(wafs.getWindSpeed()))); // 风速
					// "V11041,V11041_001,V11041_002,V20001_001,V20001_002,V20002,V_RWVMIN1,V_RWVMAX1,V_RWVMIN2,V_RWVMAX2,"
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(wafs.getMaxWindSpeed()))); // 最大风速
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(wafs.getExtremeChangeValueOfWindDireInCounterClockwise())));//风向反时针变化极值 
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(wafs.getExtremeChangeValueOfWindDireInClockwise())));//风向顺时针变化极值
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(wafs.getHorizontalVisibility())));// 水平能见度（或最小能见度）
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(wafs.getMaxHorizontalVisibility())));// 最大水平能见度
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(wafs.getVerticalVisibility())));// 垂直能见度
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(wafs.getRunwayVisualRange().get(0))));//跑道视程（或10分钟内最小平均跑道视程）1 ~ 5； 10分钟内最大平均跑道视程1  ~ 5
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(wafs.getMaxRunwayVisualRangeEvery10mins().get(0))));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(wafs.getRunwayVisualRange().get(1))));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(wafs.getMaxRunwayVisualRangeEvery10mins().get(1))));
					// "V_RWVMIN3,V_RWVMAX3,V_RWVMIN4,V_RWVMAX4,V_RWVMIN5,V_RWVMAX5,V20013_001,V20013_002,V20013_003,V12001,"
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(wafs.getRunwayVisualRange().get(2))));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(wafs.getMaxRunwayVisualRangeEvery10mins().get(2))));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(wafs.getRunwayVisualRange().get(3))));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(wafs.getMaxRunwayVisualRangeEvery10mins().get(3))));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(wafs.getRunwayVisualRange().get(4))));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(wafs.getMaxRunwayVisualRangeEvery10mins().get(4))));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(wafs.getHeightOfCloudBase().get(0))));// 云底高1 ~ 3
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(wafs.getHeightOfCloudBase().get(1))));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(wafs.getHeightOfCloudBase().get(2))));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(wafs.getTemperature()))); // 温度
					// "V12003,V10051,V_NCODE,V02001,V04001,V04002,V04003,V04004,V04005,V_BBB,"
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(wafs.getDewPoint()))); // 露点 
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(wafs.getPressureAboveSeaLevel()))); // 订正海平面气压
					pStatement.setInt(ii++, countryCode);  // 国家代码 
					pStatement.setInt(ii++, wafs.getAutoStationMark()); // 自动站标识
					pStatement.setInt(ii++, wafs.getObservationTime().getYear() + 1900);
					pStatement.setInt(ii++, wafs.getObservationTime().getMonth() + 1);
					pStatement.setInt(ii++, wafs.getObservationTime().getDate());
					pStatement.setInt(ii++, wafs.getObservationTime().getHours());
					pStatement.setInt(ii++, wafs.getObservationTime().getMinutes());
					pStatement.setString(ii++, wafs.getCorrectSign().replaceAll("\u0000", ""));
					// "C_RWY1,C_RWY2,C_RWY3,C_RMK) "
					pStatement.setString(ii++, wafs.getWindShearRunwayNumbers().get(0).replaceAll("\u0000", ""));  // 风切变跑道号1 ~ 3
					pStatement.setString(ii++, wafs.getWindShearRunwayNumbers().get(1).replaceAll("\u0000", ""));
					pStatement.setString(ii++, wafs.getWindShearRunwayNumbers().get(2).replaceAll("\u0000", ""));
					pStatement.setString(ii++, wafs.getAnnotations().replaceAll("\u0000", ""));
					pStatement.setString(ii++, cts_code);
					
					di.setIIiii(wafs.getTerminalSign());
					di.setDATA_TIME(TimeUtil.date2String(wafs.getObservationTime(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(latitude));
					di.setLONGTITUDE(String.valueOf(longtitude));
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(fileN).length()));
					di.setDATA_UPDATE_FLAG("000");
					di.setHEIGHT(String.valueOf(statHeight));
					
					pStatement.addBatch();
					sqls.add(((LoggableStatement)pStatement).getQueryString()); // 批量入库冲突时，单条入库
					listDi.add(di);
				} // end for
				try{
					pStatement.executeBatch();
					connection.commit();
					sqls.clear();
				}catch(SQLException e){
					pStatement.clearParameters();
					pStatement.clearBatch();
					execute_sql(sqls, connection, fileN); // 此中，入库失败的会将对应的DI的ProcessState置为1 // 1:失败，0：成功
					loggerBuffer.append("\n Batch commit failed: "+fileN);
				}
			}catch (SQLException e) {
				loggerBuffer.append("\n Create Statement error, " + fileN + e.getMessage());
			}catch (Exception e) {
				e.printStackTrace();
				loggerBuffer.append("\n " + e.getMessage());
			}
			finally {
				if(pStatement != null) {
					try {
						pStatement.close();
					} catch (SQLException e) {
						loggerBuffer.append("\n Close Statement error: "+e.getMessage());
					}
				}
			}
		} 
	}
	
	/**
	 * Report info to db.
	 *
	 * @param reportInfos the report infos
	 * @param connection the connection
	 * @param recv_time the recv time
	 * @Title: reportInfoToDb
	 * @Description: TODO(报文信息入库)
	 * @param:  recv_time   资料时间
	 * @return: void
	 * @throws: 
	 */
	@SuppressWarnings({ "rawtypes" })
	public static void reportInfoToDb( List<ReportInfo> reportInfos, java.sql.Connection connection, Date recv_time) {
		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.reportTable()+" (D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,"
				+ "V_BBB,V_CCCC,V_TT,V01301,V01300,V05001,V06001,V_NCODE,V_ACODE,"
				+ "V04001,V04002,V04003,V04004,V04005,"
				+ "V_LEN,V_REPORT) VALUES"
				+ "(?,?,?,?,?,"
				+ " ?,?,?,?,?,?,?,?,?,"
				+ " ?,?,?,?,?,"
				+ " ?,?)";
		try {
			pStatement = new LoggableStatement(connection, sql);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String V_TT= "";
			for (int i = 0; i < reportInfos.size(); i++) {
				try {
					WAFS wafs = (WAFS) reportInfos.get(i).getT();
					String stat = wafs.getTerminalSign();
					String info = (String) proMap.get(stat + "+01");
					int adminCode = 999999;
					double longtitude = 999999.0;
					double latitude = 999999.0;
					int countryCode = 999999;
					if(info == null) {
						infoLogger.error("\n In configuration file, this station does not exist: " + stat);
						continue ;
					}
					else{
						String[] infos = info.split(",");
						if(!infos[5].equals("null"))
							adminCode = Integer.parseInt(infos[5]);
						if(infos[1].equals("null")){
							infoLogger.error("\n In configuration file, longtitude is null!");
							continue;
						}
						else
							longtitude = Double.parseDouble(infos[1]);
						if(infos[2].equals("null")){
							infoLogger.error("\n  In configuration file, latitude is null!");
							continue;
						}
						else
							latitude = Double.parseDouble(infos[2]);
						if(!infos[4].equals("null"))
							countryCode = Integer.parseInt(infos[4]);
					}
					if(wafs.getReportType().equals("METAR"))
						V_TT = "SA";
					else
						V_TT = "SP";
					String primkey = sdf.format(wafs.getObservationTime())+"_"+stat+"_"+wafs.getReportType()+"_"+wafs.getReportCenter() +"_"+wafs.getCorrectSign();
					int ii = 1;
//					pStatement.setString(ii++, primkey);
					pStatement.setString(ii++, sod_report_code);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(wafs.getObservationTime().getTime()));
					
					pStatement.setString(ii++, wafs.getCorrectSign());
					pStatement.setString(ii++, wafs.getReportCenter());  // 编报中心
					pStatement.setString(ii++, wafs.getReportType());  // 资料类型
					pStatement.setString(ii++, wafs.getTerminalSign());  // 航班号
					pStatement.setInt(ii++, Integer.parseInt(StationCodeUtil.stringToAscii(stat)));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(latitude)));   // 纬度
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(longtitude))); // 经度
					pStatement.setInt(ii++, countryCode); // V_NCODE 国家代码
					pStatement.setInt(ii++, adminCode); //   V_ACODE 行政区划
					pStatement.setInt(ii++, wafs.getObservationTime().getYear() + 1900);
					pStatement.setInt(ii++, wafs.getObservationTime().getMonth() + 1);
					pStatement.setInt(ii++, wafs.getObservationTime().getDate());
					pStatement.setInt(ii++, wafs.getObservationTime().getHours());
					pStatement.setInt(ii++, wafs.getObservationTime().getMinutes());
					pStatement.setInt(ii++, reportInfos.get(i).getReport().length());
					pStatement.setString(ii++, reportInfos.get(i).getReport());
					
					pStatement.execute();
					if(connection.getAutoCommit() == false)
						connection.commit();
				} catch (Exception e) {
					infoLogger.error("sql error: " + ((LoggableStatement)pStatement).getQueryString());
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
	 * Execute sql.
	 *
	 * @param sqls the sqls
	 * @param connection the connection
	 * @param fileN the file N
	 * @Title: execute_sql
	 * @Description: TODO(批量入库失败时，采用逐条提交)
	 * @return: void
	 * @throws： 
	 */
	private static void execute_sql(List<String> sqls, java.sql.Connection connection, String fileN) {
		Statement pStatement = null;
		try {
			if(connection.getAutoCommit() == false)
				connection.setAutoCommit(true);
			pStatement = connection.createStatement();
			for (int i = 0; i < sqls.size(); i++) {
//				pStatement = new LoggableStatement(connection, sqls.get(i));
//		
				try {
					pStatement.execute(sqls.get(i));
//					connection.commit();
				} catch (Exception e) {
					infoLogger.error("\n File name: "+fileN
							+"\n " + listDi.get(i).getIIiii() + " " + listDi.get(i).getDATA_TIME()
							+"\n execute sql error: "+sqls.get(i)+"\n "+e.getMessage());
					listDi.get(i).setPROCESS_STATE("0");
				}
			}
		} catch (SQLException e) {
			infoLogger.error("\n Create Statement error: "+ "  " + fileN + e.getMessage());
		}catch (Exception e) {
			infoLogger.error(e.getMessage());
//			System.out.println(((LoggableStatement)pStatement).getQueryString());
		}
		finally {
			if(pStatement != null) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					infoLogger.error("\n Close Statement failed: "+e.getMessage());
				}
			}
		}		
		
	} 
//	public static void main(String[] args) {
//		Date recv_time = new Date();
//		StringBuffer loggerBuffer = new StringBuffer();
//		DecodeWAFS decodeWAFS = new DecodeWAFS();
//		String filepath = "D:\\DataTest\\A.0001.0005.R001\\A_SAKB31NGTT071600_C_BABJ_20190107160011_06613.MSG";
//		ParseResult<WAFS> parseResult = decodeWAFS.decodeWAFS(new File(filepath));
//		DataBaseAction action = DbService.processSuccessReport(parseResult,filepath, recv_time,loggerBuffer); //loggerBuffer
//		System.out.println(action);
//	}
}
