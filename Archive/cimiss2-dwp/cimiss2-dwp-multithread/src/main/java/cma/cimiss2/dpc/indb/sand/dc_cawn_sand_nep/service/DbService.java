package cma.cimiss2.dpc.indb.sand.dc_cawn_sand_nep.service;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import cma.cimiss2.dpc.decoder.bean.cawn.AtmosphericTurbidityData;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.sand.SurfReportInfoService;

public class DbService {
	
	HashMap<String, Integer> retryMap;
	private static List<StatDi> listDi = null;
	public static BlockingQueue<StatDi> diQueues;
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
//	static Map<String, Object> proMap = StationInfo.getProMap();
	public static String type = StartConfig.sodCode();
	public static String report_sod_code = StartConfig.reportSodCode();
	public static String v_bbb="000";
	public static String acodeNo="15";
	
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}

	/**
	 * @Title: insert_db
	 * @Description:(沙尘暴土壤湿度入库方法)
	 * @param parseResult
	 * @param recv_time
	 * @param loggerBuffer 
	 * @param fileName 
	 * @return
	 * DataBaseAction
	 * @throws: 
	 */
	public static DataBaseAction processSuccessReport(ParseResult<AtmosphericTurbidityData> parseResult, Date recv_time, String fileN, StringBuffer loggerBuffer, String filepath) {
		// 获取数据库连接
		java.sql.Connection connection = null;
		java.sql.Connection report_connection = null;
		try {
//			connection = JdbcUtilsSing.getInstance("config/sand_soi/z_sand_soi_db_config.xml").getConnection();
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			report_connection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
			if (connection != null) {
				connection.setAutoCommit(false); // 关闭自动提交
				// list: 所有数据
				List<AtmosphericTurbidityData> list = parseResult.getData();
	
				insert_db(list, connection, recv_time, fileN,loggerBuffer, filepath);

				@SuppressWarnings("rawtypes")
				List<ReportInfo> reportInfos = parseResult.getReports();
					  
				String v_tt=fileN.substring(7, 10);
				String v_cccc=fileN.substring(14, 19);
				
				listDi = new ArrayList<StatDi>();
				if(report_connection != null){
					SurfReportInfoService.reportInfoToDb(reportInfos, report_connection, acodeNo,v_bbb,type,report_sod_code, recv_time, v_cccc,v_tt,filepath,listDi);
				}
				for (int j = 0; j < listDi.size(); j++) {
					diQueues.offer(listDi.get(j));
				}
				listDi.clear();
			} else {
				loggerBuffer.append("\n get database connection error");
				return DataBaseAction.CONNECTION_ERROR;
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n get database connection error");
			return DataBaseAction.CONNECTION_ERROR;
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n close database connection error：" + e.getMessage());
				}
			}
			if(report_connection != null){
				try{
					report_connection.close();
				}catch (Exception e) {
					loggerBuffer.append("\n close database connection error：" + e.getMessage());
				}
			}
		}
		return DataBaseAction.SUCCESS;
	}

	/**
	 * 
	 * @param connection2 
	 * @Title: insert_db
	 *  @Description:(入库函数) 
	 *  @param: @param list 解码实体类集合
	 *  @param recv_time 消息接收时间 
	 * @param fileN 
	 * @param loggerBuffer 
	 *  @return: DataBaseAction sql 语句执行状态
	 */
	@SuppressWarnings("unused")
	private static DataBaseAction insert_db(List<AtmosphericTurbidityData> list, Connection connection, Date recv_time, String fileN, StringBuffer loggerBuffer, String filepath) {
		Map<String, Object> proMap = StationInfo.getProMap();
		//java.sql.Connection connection = null;
		PreparedStatement pStatement = null;
		
		try {
		// 获取数据库连接	
		//connection = JdbcUtilsSing.getInstance(dbPath).getConnection();
			String sql = "INSERT INTO "+StartConfig.valueTable()+"( "+
    "D_RECORD_ID, D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V01301,"+
    "  V01300,V05001,V06001,V07001,V_ACODE,V04001,V04002,V04003,V04004,V04005,V04006,V_DATA_CODE,V04016, "+
    "  V15700,V12001,V13003,V10004,V12505,V_BBB,D_SOURCE_ID)VALUES( "+
    " ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			pStatement = new LoggableStatement(connection, sql);
			if(StartConfig.getDatabaseType() == 1) {
				pStatement.execute("select last_txc_xid()");
			}
			if (connection != null) {
				connection.setAutoCommit(false);
				Calendar calendar = Calendar.getInstance();
				// 关闭自动提交，手动批量提交
				List<String> sqls = new ArrayList<>();
				listDi = new ArrayList<StatDi>();
				for (int i = 0; i < list.size(); i++) {
					StatDi di = new StatDi();
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(type);
					di.setDATA_TYPE_1(StartConfig.ctsCode());
					
					di.setTT("");
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); // 1成功，0失败
					di.setPROCESS_STATE("1"); // 1成功，0失败
					
					int ii = 1;
					AtmosphericTurbidityData atmoTurbidityBean=list.get(i) ;
					String obserTime=TimeUtil.date2String(atmoTurbidityBean.getObservationTime(), "yyyyMMddHHmmss");
					pStatement.setString(ii++,obserTime +"_"+atmoTurbidityBean.getStationNumberChina());//记录标识
					pStatement.setString(ii++, type);//资料标识
					
					calendar.setTime(atmoTurbidityBean.getObservationTime());
					
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime())); // 入库时间
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime())); // 收到时间
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime())); // 更新时间
					
					Date date  = convertDate(new Timestamp(atmoTurbidityBean.getObservationTime().getTime()),loggerBuffer);
					pStatement.setTimestamp(ii++, new Timestamp(date.getTime()));// 资料时间
					
					pStatement.setString(ii++, atmoTurbidityBean.getStationNumberChina());//区站号(字符)
					pStatement.setString(ii++,  StationCodeUtil.stringToAscii(atmoTurbidityBean.getStationNumberChina()));//区站号(数字)
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(atmoTurbidityBean.getLatitude())));//纬度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(atmoTurbidityBean.getLongitude())));//经度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(atmoTurbidityBean.getHeightOfSationGroundAboveMeanSeaLevel())));//测站海拔高度
					pStatement.setString(ii++,StationInfo.getAdminCode(atmoTurbidityBean.getStationNumberChina(), acodeNo));//中国行政区划代码
					pStatement.setBigDecimal(ii++, new BigDecimal(date.getYear() + 1900));//资料观测年
					pStatement.setBigDecimal(ii++, new BigDecimal(date.getMonth()+1));//资料观测月
					pStatement.setBigDecimal(ii++, new BigDecimal(date.getDate()));//资料观测日
					pStatement.setInt(ii++, date.getHours());//资料观测时
					pStatement.setInt(ii++, date.getMinutes());//资料观测分
					pStatement.setInt(ii++, date.getSeconds());//资料观测秒
					/*pStatement.setBigDecimal(ii++, new BigDecimal(calendar.get(Calendar.YEAR)));// 年
					pStatement.setBigDecimal(ii++, new BigDecimal(calendar.get(Calendar.MONTH) + 1));// 月
					pStatement.setBigDecimal(ii++, new BigDecimal(calendar.get(Calendar.DAY_OF_MONTH)));// 日

					pStatement.setBigDecimal(ii++, new BigDecimal(calendar.get(Calendar.HOUR)));//资料观测时
					pStatement.setBigDecimal(ii++, new BigDecimal(calendar.get(Calendar.MINUTE)));//资料观测分
					pStatement.setBigDecimal(ii++, new BigDecimal(calendar.get(Calendar.SECOND)));//资料观测秒
*/				
					pStatement.setString(ii++, atmoTurbidityBean.getData_code());//数据识别码
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(atmoTurbidityBean.getTimeInterval())));//观测时间间隔
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(atmoTurbidityBean.getParticleScatterCoefficient())));//粒子散射系数
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(atmoTurbidityBean.getTemperature())));//环境温度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(atmoTurbidityBean.getHumidity())));//环境相对湿度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(atmoTurbidityBean.getPressure())));//环境气压
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(atmoTurbidityBean.getCavityTemperature())));//腔体温度
					pStatement.setString(ii++, "000");
					pStatement.setString(ii++, StartConfig.ctsCode());
					
					// System.out.println(((LoggableStatement)pStatement).getQueryString());
					di.setIIiii(atmoTurbidityBean.getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(atmoTurbidityBean.getObservationTime(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLATITUDE(String.valueOf(atmoTurbidityBean.getLatitude()));
					di.setLONGTITUDE(String.valueOf(atmoTurbidityBean.getLongitude()));
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
					di.setDATA_UPDATE_FLAG("000");
					di.setHEIGHT(String.valueOf(atmoTurbidityBean.getHeightOfSationGroundAboveMeanSeaLevel()));

					// 批量提交
					pStatement.addBatch();
					sqls.add(((LoggableStatement) pStatement).getQueryString());
					listDi.add(di);

				} 

				try {
					pStatement.executeBatch();
					connection.commit();
					sqls.clear();
					return DataBaseAction.SUCCESS;
				} catch (Exception e) {
					e.printStackTrace();
					pStatement.clearParameters();
					pStatement.clearBatch();
					execute_sql(sqls, connection,fileN,loggerBuffer);
					// execute_sql(list, connection, type, recv_time);
					loggerBuffer.append("\n Batch commit failed：" + fileN);
					return DataBaseAction.BATCH_ERROR;
				}

			} else {
				loggerBuffer.append("\n "+"Database connection error");
				return DataBaseAction.CONNECTION_ERROR;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			loggerBuffer.append("\n "+"Database connection error：" + e.getMessage());
			return DataBaseAction.CONNECTION_ERROR;
		}catch (Exception e) {
			e.printStackTrace();
				loggerBuffer.append("\n Database Batch commit failed！：" + e.getMessage());
				return DataBaseAction.CONNECTION_ERROR;
			}
		finally {

			for (int j = 0; j < listDi.size(); j++) {
				diQueues.offer(listDi.get(j));
			}
			listDi.clear();
			try {
				if (pStatement != null)
					pStatement.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				loggerBuffer.append("\n Database connection close  error：" + e.getMessage());
			}
		}
	}

	private static void execute_sql(List<String> sqls, java.sql.Connection connection, String fileN, StringBuffer loggerBuffer) {
		Statement pStatement = null;
		try {
			connection.setAutoCommit(true);
			pStatement = connection.createStatement();
			for (int i = 0; i < sqls.size(); i++) {
//				pStatement = new LoggableStatement(connection, sqls.get(i));
				try {
					pStatement.execute(sqls.get(i));
				} catch (Exception e) {
					loggerBuffer.append("\n filename：" + fileN + "\n " + listDi.get(i).getIIiii() + " "
							+ listDi.get(i).getDATA_TIME() + "\n execute sql error："
							+ sqls.get(i) + "\n " + e.getMessage());
					listDi.get(i).setPROCESS_STATE("0");
				}
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n single insert  create Statement error" + e.getMessage());
		}catch (Exception e) {
			loggerBuffer.append("\n single insert error" + e.getMessage());
		}  
		finally {
			if (pStatement != null) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n single insert close Statement error" + e.getMessage());
				}
			}
		}

	}
	public static Date convertDate(Date Date, StringBuffer loggerBuffer) {
		Date date = null;
		try {
			String obserTime=TimeUtil.date2String(Date, "yyyyMMddHHmmss");
			 date=TimeUtil.String2Date(obserTime, "yyyyMMddHHmmss");
		} catch (Exception e) {
			loggerBuffer.append("\n Error formatting date" + e.getMessage());
		}
		return date;
	}

}
