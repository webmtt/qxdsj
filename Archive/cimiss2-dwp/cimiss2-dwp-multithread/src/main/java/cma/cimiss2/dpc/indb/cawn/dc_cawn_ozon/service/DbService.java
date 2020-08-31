package cma.cimiss2.dpc.indb.cawn.dc_cawn_ozon.service;

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
import cma.cimiss2.dpc.decoder.bean.cawn.AntarcticOzone;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.sand.ReportInfoService;

public class DbService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	public static String cts_code=StartConfig.ctsCode();
	public static String sod_code=StartConfig.sodCode();
	public static String report_sod_code=StartConfig.reportSodCode();
	public static String v_tt = "CAWN";
	public int defaultInt = 999999;
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}
//	static Map<String, Object> proMap = StationInfo.getProMap();
	/**
	 * 
	 * @Title: processSuccessReport
	 * @Description:(南极臭氧资料报文解码入库函数)
	 * @param parseResult 解码结果集
	 * @param fileN 文件对象
	 * @param recv_time 接收时间
	 * @param loggerBuffer 日志对象
	 * @param typeOfStorageInstrument 
	 * @return DataBaseAction 入库状态
	 * @throws：
	 */
	@SuppressWarnings("rawtypes")
	public static DataBaseAction processSuccessReport(ParseResult<AntarcticOzone> parseResult, String fileN,
			Date recv_time, StringBuffer loggerBuffer) {
		java.sql.Connection connection = null;
		java.sql.Connection reportConnection = null;
		try {
			// 获取数据库连接
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			// 获取解码结果集
			List<AntarcticOzone> list = parseResult.getData();
			insert_db(list, connection, recv_time, loggerBuffer,fileN);
			
			List<ReportInfo> reportInfos = parseResult.getReports();
			reportConnection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
			ReportInfoService.reportInfoToDb(reportInfos, reportConnection, "000", recv_time, "9999", v_tt, report_sod_code,cts_code, "01", loggerBuffer,fileN,listDi);
			return DataBaseAction.SUCCESS;
			
		} catch (Exception e) {
			loggerBuffer.append("\n  Database connection error!");
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
					loggerBuffer.append("\n Close Database connection error:"+e.getMessage());
				}
			}
			
		}
	}
	/**
	 * 
	 * @Title: insert_db
	 * @Description:(南极臭氧观测资料批量入库函数)
	 * @param list 待入库对象
	 * @param connection 数据库连接
	 * @param recv_time 接收时间
	 * @param loggerBuffer 日志对象
	 * @param fileN 
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static void insert_db(List<AntarcticOzone> list, java.sql.Connection connection, Date recv_time,
			StringBuffer loggerBuffer, String fileN) {
		Map<String, Object> proMap = StationInfo.getProMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,"
				+ "D_UPDATE_TIME,D_DATETIME,V01301,V01300,V05001,V06001,V07001,V04001,V04002,"
				+ "V04003,V04004,V15020_1,V15020_2,V_BBB, D_SOURCE_ID) "
				+ "  VALUES  (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ "?, ?,?,?)";
		if(connection != null){		
			try {	
				pStatement = new LoggableStatement(connection, sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				connection.setAutoCommit(false);
				List<String> sqls = new ArrayList<>();
				for(int i = 0; i<list.size();i++){
					AntarcticOzone ozone = list.get(i);
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
					String info = (String) proMap.get(ozone.getStationNumberChina() + "+01");
					if (info == null) {
						// 如果此站号不存在，执行下一个数据
						loggerBuffer.append("\n  In configuration file, this station does not exist: " + ozone.getStationNumberChina());
					} else {
						String[] infos = info.split(",");
						Double height = Double.parseDouble(infos[3]);//测站高度
						ozone.setHeightOfSationGroundAboveMeanSeaLevel(height);
					}
				
					int ii = 1;
					String PrimaryKey = sdf.format(ozone.getObservationTime())+"_"+ozone.getStationNumberChina();
					
					pStatement.setString(ii ++, PrimaryKey);// 记录标识
					pStatement.setString(ii ++, sod_code);// 资料标识
					pStatement.setTimestamp(ii ++,  new Timestamp(new Date().getTime()));// 入库时间
					pStatement.setTimestamp(ii ++,  new Timestamp(recv_time.getTime()));// 收到时间
					pStatement.setTimestamp(ii ++,  new Timestamp(new Date().getTime()));// 更新时间
					pStatement.setTimestamp(ii ++, new Timestamp(ozone.getObservationTime().getTime()));// 资料时间
					pStatement.setString(ii ++, ozone.getStationNumberChina());// 区站号(字符)
					pStatement.setBigDecimal(ii ++, new BigDecimal(StationCodeUtil.stringToAscii(ozone.getStationNumberChina())));// 区站号(数字)
					pStatement.setBigDecimal(ii ++, new BigDecimal(ozone.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));// 纬度
					pStatement.setBigDecimal(ii ++, new BigDecimal(ozone.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));// 经度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(ozone.getHeightOfSationGroundAboveMeanSeaLevel())));// 测站高度
					pStatement.setBigDecimal(ii ++, new BigDecimal(ozone.getObservationTime().getYear() + 1900));// 资料观测年
					pStatement.setBigDecimal(ii ++, new BigDecimal(ozone.getObservationTime().getMonth()+1));// 资料观测月
					pStatement.setBigDecimal(ii ++, new BigDecimal(ozone.getObservationTime().getDate()));// 资料观测日
					pStatement.setBigDecimal(ii ++, new BigDecimal(ozone.getObservationTime().getHours()));// 资料观测时
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(ozone.getO3_Ds())));// Ds臭氧总量
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(ozone.getO3_Zs())));//Zs臭氧总量
					pStatement.setString(ii++, "000");
					pStatement.setString(ii++, cts_code);
					
					di.setIIiii(list.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(ozone.getObservationTime(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(ozone.getLatitude()));
					di.setLONGTITUDE(String.valueOf(ozone.getLongitude()));
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(fileN).length()));
					di.setDATA_UPDATE_FLAG("000");
					di.setHEIGHT(String.valueOf(ozone.getHeightOfSationGroundAboveMeanSeaLevel()));
					
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
						loggerBuffer.append("\n Create Statement error: "+e.getMessage());
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
