package cma.cimiss2.dpc.indb.cawn.dc_cawn_pmda.service;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;

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
import cma.cimiss2.dpc.decoder.bean.cawn.pmDA;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.sand.ReportInfoService;


public class DbService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	public static String report_sod_code = StartConfig.reportSodCode();
	public static String v_tt = "PMDA";
	public int defaultInt = 999999;
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
	 * @Description:(气溶胶初级质控日值资料报文解码入库函数)
	 * @param parseResult 解码结果集
	 * @param fileN 文件对象
	 * @param recv_time 接收时间
	 * @param loggerBuffer 日志对象
	 * @param typeOfStorageInstrument 
	 * @return DataBaseAction 入库状态
	 * @throws：
	 */

	@SuppressWarnings("rawtypes")
	public static DataBaseAction processSuccessReport(ParseResult<pmDA> parseResult, String fileN,
			Date recv_time, StringBuffer loggerBuffer) {
		java.sql.Connection connection = null;
		java.sql.Connection reportConnection = null;
		try {
			// 获取数据库连接
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			// 获取解码结果集
			List<pmDA> list = parseResult.getData();
			insert_db(list, connection, recv_time, loggerBuffer,fileN);
			
			List<ReportInfo> reportInfos = parseResult.getReports();
			reportConnection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
			ReportInfoService.reportInfoToDb(reportInfos, reportConnection, "000", recv_time, "9999", v_tt, report_sod_code, cts_code,"16", loggerBuffer,fileN,listDi);
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
					loggerBuffer.append("\n Close Database connection error: "+e.getMessage());
				}
			}
			if(reportConnection != null) {
				try {
					reportConnection.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n Close Database connection error: "+e.getMessage());
				}
			}
			
		}
	}
	/**
	 * 
	 * @Title: insert_db
	 * @Description:(气溶胶初级质控日值资料批量入库函数)
	 * @param list 待入库对象
	 * @param connection 数据库连接
	 * @param recv_time 接收时间
	 * @param loggerBuffer 日志对象
	 * @param fileN 
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static void insert_db(List<pmDA> list, java.sql.Connection connection, Date recv_time,
			StringBuffer loggerBuffer, String fileN) {
		Map<String, Object> proMap = StationInfo.getProMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");	
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,"
				+ "D_UPDATE_TIME,D_DATETIME,V01301,V01300,V05001,V06001,V07001,V04001,V04002,"
				+ "V04003,V_PM10_201_005,V_PM10_201_030,V_PM10_201_101,V_PM10_201_124,V_PM25_201_005,"
				+ "V_PM25_201_101,V_PM25_201_124,V_PM01_201_101,V_PM10_201_005_076,V_PM10_201_030_076,"
				+ "V_PM10_201_101_076,V_PM10_201_124_076,V_PM25_201_005_076,V_PM25_201_101_076,"
				+ "V_PM25_201_124_076,V_PM01_201_101_076,V_BBB,D_SOURCE_ID) "
				+ "  VALUES  (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		if(connection != null){		
			try {	
				pStatement = new LoggableStatement(connection, sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				connection.setAutoCommit(false);
				List<String> sqls = new ArrayList<>();
				for(int i = 0; i<list.size();i++){
					pmDA pmDA = list.get(i);
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
					
					// 根据站号查询行政区划代码
					String info = (String) proMap.get(pmDA.getStationNumberChina() + "+16");
					if (info == null) {
						// 如果此站号不存在，执行下一个数据
						loggerBuffer.append("\n In configuration file, this station does not exist: " + pmDA.getStationNumberChina());
					} else {
						String[] infos = info.split(",");
						
						if(!infos[2].equals("null")){
							try{
								Double latitude = Double.parseDouble(infos[2]);// 经度
								pmDA.setLatitude(latitude);
							}catch (Exception e) {
								loggerBuffer.append("\n In configuration file, " + "get latitude of " + pmDA.getStationNumberChina() + " error!");
							}
						}
						if(!infos[1].equals("null")){
							try{
								Double longitude = Double.parseDouble(infos[1]); // 纬度
								pmDA.setLongitude(longitude);
							}catch (Exception e) {
								loggerBuffer.append("\n In configuration file, " + "get longititude of " + pmDA.getStationNumberChina() + " error!");
							}
						}
						if(!infos[3].equals("null")){
							try{
								Double height = Double.parseDouble(infos[3]);//测站高度
								pmDA.setHeightOfSationGroundAboveMeanSeaLevel(height);
							}catch (Exception e) {
								loggerBuffer.append("\n In configuration file, " + "get station height of " + pmDA.getStationNumberChina() + " error!");
							}
						}
					}
				
					int ii = 1;
					String PrimaryKey = sdf.format(pmDA.getObservationTime())+"_"+pmDA.getStationNumberChina();
					
					pStatement.setString(ii ++, PrimaryKey);// 记录标识
					pStatement.setString(ii ++, sod_code);// 资料标识
					pStatement.setTimestamp(ii ++,  new Timestamp(new Date().getTime()));// 入库时间
					pStatement.setTimestamp(ii ++,  new Timestamp(recv_time.getTime()));// 收到时间
					pStatement.setTimestamp(ii ++,  new Timestamp(new Date().getTime()));// 更新时间
					pStatement.setTimestamp(ii ++, new Timestamp(pmDA.getObservationTime().getTime()));// 资料时间
					pStatement.setString(ii ++, pmDA.getStationNumberChina());// 区站号(字符)
					pStatement.setBigDecimal(ii ++, new BigDecimal(StationCodeUtil.stringToAscii(pmDA.getStationNumberChina())));// 区站号(数字)
					pStatement.setBigDecimal(ii ++, new BigDecimal(pmDA.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));// 纬度
					pStatement.setBigDecimal(ii ++, new BigDecimal(pmDA.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));// 经度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(pmDA.getHeightOfSationGroundAboveMeanSeaLevel())));// 测站高度
					pStatement.setBigDecimal(ii ++, new BigDecimal(pmDA.getObservationTime().getYear() + 1900));// 资料观测年
					pStatement.setBigDecimal(ii ++, new BigDecimal(pmDA.getObservationTime().getMonth()+1));// 资料观测月
					pStatement.setBigDecimal(ii ++, new BigDecimal(pmDA.getObservationTime().getDate()));// 资料观测日
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(pmDA.getPM10_5min_AVG())));// 5分钟观测PM10日均值
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(pmDA.getPM10_30min_AVG())));// 30分钟观测PM10日均值
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(pmDA.getPM10_1h_AVG())));// 1小时观测PM10日均值
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(pmDA.getPM10_24h_AVG())));// 24小时观测PM10日均值
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(pmDA.getPM2p5_5min_AVG())));// 5分钟观测PM2.5日均值
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(pmDA.getPM2p5_1h_AVG())));// 1小时观测PM2.5日均值
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(pmDA.getPM2p5_24h_AVG())));// 24小时观测PM2.5日均值
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(pmDA.getPM1_1h_AVG())));// 1小时观测PM1日均值
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(pmDA.getPM10_5min_AVG_COUNT())));// 5分钟观测PM10日均值每日内有效小时样本数
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(pmDA.getPM10_30min_AVG_COUNT())));// 30分钟观测PM10日均值每日内有效小时样本数
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(pmDA.getPM10_1h_AVG_COUNT())));// 1小时观测PM10日均值每日内有效小时样本数
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(pmDA.getPM10_24h_AVG_COUNT())));// 24小时观测PM10日均值每日内有效小时样本数
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(pmDA.getPM2p5_5min_AVG_COUNT())));//5分钟观测PM25日均值每日内有效小时样本数
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(pmDA.getPM2p5_1h_AVG_COUNT())));//1小时观测PM25日均值每日内有效小时样本数1
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(pmDA.getPM2p5_24h_AVG_COUNT())));//24小时观测PM25日均值每日内有效小时样本数1
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(pmDA.getPM1_1h_AVG_COUNT())));//1小时观测PM1每日内有效小时的样本数
					pStatement.setString(ii++, "000");
					pStatement.setString(ii++, cts_code);
					
					di.setIIiii(list.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(pmDA.getObservationTime(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(pmDA.getLatitude()));
					di.setLONGTITUDE(String.valueOf(pmDA.getLongitude()));
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(fileN).length()));
					di.setDATA_UPDATE_FLAG("000");
					di.setHEIGHT(String.valueOf(pmDA.getHeightOfSationGroundAboveMeanSeaLevel()));
					
					// 批量提交
					pStatement.addBatch();
					sqls.add(((LoggableStatement)pStatement).getQueryString());
					listDi.add(di);				
				} // end for
				try{
					pStatement.executeBatch();
					connection.commit();
					loggerBuffer.append(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
					loggerBuffer.append(" INSERT SUCCESS COUNT ：" + list.size() + "\n");
					sqls.clear();
				}catch(SQLException e){
					pStatement.clearParameters();
					pStatement.clearBatch();
					loggerBuffer.append(" Batch ERROR : " + sdf.format(new Date()) + "\n");
					execute_sql(sqls, connection,loggerBuffer); // 此中，入库失败的会将对应的DI的ProcessState置为1 // 1:失败，0：成功
				}
			}catch (SQLException e) {
				loggerBuffer.append("\n Create Statement error: "+e.getMessage());
			}finally {
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
	 * 
	 * @Title: execute_sql
	 * @Description:(批量插入失败时，单条插入函数)
	 * @param sqls sql对象
	 * @param connection 数据库连接
	 * @param loggerBuffer 日志对象
	 * @throws：
	 */
	private static void execute_sql(List<String> sqls, java.sql.Connection connection, StringBuffer loggerBuffer) {
		Statement pStatement = null;
		loggerBuffer.append("\n Start insert one by one.");
		try {
			connection.setAutoCommit(true);
			pStatement = connection.createStatement();
			for (int i = 0; i < sqls.size(); i++) {
//				pStatement = new LoggableStatement(connection, sqls.get(i));
				try {
					pStatement.execute(sqls.get(i));
					loggerBuffer.append(" " + listDi.get(i).getIIiii() + " " + listDi.get(i).getDATA_TIME() +" 000\n");
				} catch (Exception e) {
					// 单行插入失败
					loggerBuffer.append("\n " + listDi.get(i).getIIiii() + " " + listDi.get(i).getDATA_TIME()
							+"\n execute sql error: "+sqls.get(i)+"\n "+e.getMessage());
					listDi.get(i).setPROCESS_STATE("0");
				}
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n Create Statement error: "+e.getMessage());
		}finally {
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
