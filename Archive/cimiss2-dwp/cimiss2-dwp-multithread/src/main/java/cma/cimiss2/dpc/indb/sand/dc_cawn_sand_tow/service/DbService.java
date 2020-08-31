package cma.cimiss2.dpc.indb.sand.dc_cawn_sand_tow.service;

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
import cma.cimiss2.dpc.decoder.bean.sand.AveObservationSandTowData;
import cma.cimiss2.dpc.decoder.bean.sand.AveObservationSandTowerData;
import cma.cimiss2.dpc.decoder.sand.DecodeSandTow;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.sand.SurfReportInfoService;

public class DbService {
	
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static List<StatDi> listDi = null;
	public static BlockingQueue<StatDi> diQueues;
	public static String type = StartConfig.sodCode();
	public static String report_sod_code = StartConfig.reportSodCode();
	public static String cts_code = StartConfig.ctsCode();
	public static String acodeNo="15";
	public static String v_bbb="000";
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}
	public static void main(String[] args) {
		DbService dbService=new DbService();
		DecodeSandTow decodeSandTower = new DecodeSandTow();
		
		StringBuffer loggerBuffer = new StringBuffer();
		String filepath="C:\\BaiduNetdiskDownload\\test\\G.0001.0014.R001\\Z_SAND_TOW_C5_54517_20200202155001.txt";
		File file = new File(filepath);
		Date recv_time = new Date(file.lastModified());
		ParseResult<AveObservationSandTowData> parseResult = decodeSandTower.DecodeFile(file);
		DataBaseAction action=dbService.processSuccessReport(parseResult, recv_time, file.getName(), loggerBuffer, filepath);
		System.out.println(action);
	}
	public static DataBaseAction processSuccessReport(ParseResult<AveObservationSandTowData> parseResult, Date recv_time, String fileN, StringBuffer loggerBuffer, String filepath) {
		// 获取数据库连接
		java.sql.Connection connection = null;
		java.sql.Connection report_connection = null;
		try {
//			connection = JdbcUtilsSing.getInstance("config/sand_soi/z_sand_soi_db_config.xml").getConnection();
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
//			report_connection = ConnectionPoolFactory.getInstance().getConnection("cimiss");

			if (connection != null) {
				connection.setAutoCommit(false); // 关闭自动提交
				// list: 所有数据
				List<AveObservationSandTowData> list = parseResult.getData();
	
				insert_db(list, connection, recv_time, fileN,loggerBuffer,filepath);

				@SuppressWarnings("rawtypes")
				List<ReportInfo> reportInfos = parseResult.getReports();
					  
				String v_tt=fileN.substring(7, 10);
				String v_cccc=fileN.substring(14, 19);
				
				listDi = new ArrayList<StatDi>();
				if(report_connection != null){
					SurfReportInfoService.reportInfoToDb(reportInfos, report_connection, acodeNo, v_bbb, report_sod_code,cts_code, recv_time, v_cccc, v_tt,filepath,listDi);
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
					loggerBuffer.append("\n clsoe database connection error：" + e.getMessage());
				}
			}
			if(report_connection != null){
				try{
					report_connection.close();
				}catch (Exception e) {
					loggerBuffer.append("\n clsoe database connection error：" + e.getMessage());
				}
			}
		}
		return DataBaseAction.SUCCESS;
	}

	/**
	 * 
	 * @param connection 
	 * @Title: insert_db
	 *  @Description:(入库函数) 
	 *  @param: @param list 解码实体类集合
	 *  @param recv_time 消息接收时间 
	 * @param fileN 
	 * @param loggerBuffer 
	 *  @return: DataBaseAction sql 语句执行状态
	 */
	private static DataBaseAction insert_db(List<AveObservationSandTowData> list, Connection connection, Date recv_time, String fileN, StringBuffer loggerBuffer, String filepath) {
//		Map<String, Object> proMap = StationInfo.getProMap();
		//java.sql.Connection connection = null;
		PreparedStatement pStatement = null;
		
		try {
		// 获取数据库连接
		 //connection =JdbcUtilsSing.getInstance(dbPath).getConnection();
//			String sql = "INSERT INTO "+StartConfig.valueTable()+"( "
			String sql = "INSERT INTO  SAND_CHN_TOWER_TAB ( "		
  +"D_RECORD_ID, D_DATA_ID, D_IYMDHM, D_RYMDHM, D_UPDATE_TIME, D_DATETIME, V01301, V_BBB,"
  + " V_STARTTIME, V_ENDTIME, V04001, V04002, V04003, V04004, V04005, V04006,"
  + " V_RT , V07002, V11002_701, V11001_701, V13003, V12001, D_SOURCE_ID)VALUES " 
  +"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			pStatement = new LoggableStatement(connection, sql);
			if(StartConfig.getDatabaseType() == 1) {
				pStatement.execute("select last_txc_xid()");
			}
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
				AveObservationSandTowData sandTowerBean=list.get(i) ;
				String obserTime=TimeUtil.date2String(sandTowerBean.getObservationTime(), "yyyyMMddHHmmss");
				pStatement.setString(ii++,obserTime +"_"+sandTowerBean.getStationNumberChina());//记录标识
				pStatement.setString(ii++, type);//资料标识
				
				calendar.setTime(sandTowerBean.getObservationTime());
				
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime())); // 入库时间
				pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime())); // 收到时间
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime())); // 更新时间
				
				Date date  = convertDate(new Timestamp(sandTowerBean.getObservationTime().getTime()),loggerBuffer);
				pStatement.setTimestamp(ii++, new Timestamp(date.getTime()));// 资料时间
				pStatement.setString(ii++, sandTowerBean.getStationNumberChina());//区站号(字符)
				pStatement.setString(ii++, "000");//V_BBB
				Date startDate  = convertDate(new Timestamp(sandTowerBean.getStartTimeOfobservation().getTime()),loggerBuffer);
				Date endDate  = convertDate(new Timestamp(sandTowerBean.getEndTimeOfobservation().getTime()),loggerBuffer);
				pStatement.setTimestamp(ii++, new Timestamp(startDate.getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(endDate.getTime()));
				pStatement.setBigDecimal(ii++, new BigDecimal(date.getYear() + 1900));//资料观测年
				pStatement.setBigDecimal(ii++, new BigDecimal(date.getMonth()+1));//资料观测月
				pStatement.setBigDecimal(ii++, new BigDecimal(date.getDate()));//资料观测日
				pStatement.setInt(ii++, date.getHours());//资料观测时
				pStatement.setInt(ii++, date.getMinutes());//资料观测分
				pStatement.setInt(ii++, date.getSeconds());//资料观测秒
				//此处编写表结构中的其他字段，待韩老师确认后编写	
                pStatement.setInt(ii++, sandTowerBean.getNumberOfSensorLayers());
            	
                pStatement.setDouble(ii++, sandTowerBean.getSensorHeight());
				pStatement.setDouble(ii++,sandTowerBean.getMeanWindSpeed());
				pStatement.setDouble(ii++, sandTowerBean.getMeanWindSpod());
				pStatement.setDouble(ii++, sandTowerBean.getRelativeHumidity());
				pStatement.setDouble(ii++, sandTowerBean.getTemperature());
				pStatement.setString(ii++, StartConfig.ctsCode());
				System.out.println(((LoggableStatement) pStatement).getQueryString());
				di.setIIiii(sandTowerBean.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(sandTowerBean.getObservationTime(), "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());
				
				di.setSEND("BFDB");
				di.setSEND_PHYS("DRDS");
				di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
				di.setDATA_UPDATE_FLAG("000");

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
				pStatement.clearParameters();
				pStatement.clearBatch();
				execute_sql(sqls, connection,fileN,loggerBuffer);
				// execute_sql(list, connection, type, recv_time);
				loggerBuffer.append("\n Batch commit failed：" + fileN);
				return DataBaseAction.BATCH_ERROR;
			}

		} catch (SQLException e) {
			loggerBuffer.append("\n "+"Database connection exception：" + e.getMessage());
			return DataBaseAction.CONNECTION_ERROR;
		}catch (Exception e) {
				loggerBuffer.append("\n "+"Database batch insert exception！：" + e.getMessage());
				return DataBaseAction.CONNECTION_ERROR;
			}
		finally {

//			for (int j = 0; j < listDi.size(); j++) {
//				diQueues.offer(listDi.get(j));
//			}
			listDi.clear();
			try {
				if (pStatement != null)
					pStatement.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				loggerBuffer.append("\n "+"Database connection close error：" + e.getMessage());
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
					System.out.println("\n execute sql error："
							+ sqls.get(i) + "\n " + e.getMessage());
					listDi.get(i).setPROCESS_STATE("0");
				}
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n "+"Single insert create Statement error" + e.getMessage());
			System.out.println("\n "+"Single insert create Statement error" + e.getMessage());
		}catch (Exception e) {
			loggerBuffer.append("\n "+"Single insert error" + e.getMessage());
			System.out.println("\n "+"Single insert error" + e.getMessage());
		}  
		finally {
			if (pStatement != null) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n "+"Single insert close Statement error" + e.getMessage());
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
			loggerBuffer.append("\n "+"Format date error" + e.getMessage());
		}
		return date;
	}


}
