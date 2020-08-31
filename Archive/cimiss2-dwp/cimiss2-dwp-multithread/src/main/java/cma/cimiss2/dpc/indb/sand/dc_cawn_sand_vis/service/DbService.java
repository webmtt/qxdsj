package cma.cimiss2.dpc.indb.sand.dc_cawn_sand_vis.service;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.NumberUtil;
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.sand.SandChnVis;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.sand.ReportInfoService;
import cma.cimiss2.dpc.indb.sand.dc_cawn_sand_vis.ReadIni;

public class DbService {
	
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	HashMap<String, Integer> retryMap;
	public static BlockingQueue<StatDi> diQueues;
//	static Map<String, Object> proMap = StationInfo.getProMap();
	public static String cts_code=StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	
	
	
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}

	/**
	 * @Title: insert_db
	 * @Description:(沙尘暴大气能见度数据入库)
	 * @param parseResult
	 * @param recv_time
	 * @param loggerBuffer 
	 * @return
	 * DataBaseAction
	 * @throws: 
	 */
	public static DataBaseAction insert_db(ParseResult<SandChnVis> parseResult, Date recv_time,String fileName, StringBuffer loggerBuffer,String filepath) {
		// 获取数据库连接
		java.sql.Connection connection = null;
		java.sql.Connection reportConnection = null;
		try {
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			reportConnection = ConnectionPoolFactory.getInstance().getConnection("cimiss");

			if (connection != null) {
				connection.setAutoCommit(false); // 关闭自动提交
				// list: 所有数据
				List<SandChnVis> list = parseResult.getData();
				ReadIni ini = ReadIni.getIni();

				String d_data_id = ini.getValue(ReadIni.SECTION_VIS, ReadIni.D_DATA_ID_KEY);
				insert_data(list, connection, recv_time, d_data_id,fileName,loggerBuffer,filepath);

				List<StatDi> listDi = new ArrayList<StatDi>();
				String report_sod_code=StartConfig.reportSodCode();
				String cts_code=StartConfig.ctsCode();
				@SuppressWarnings("rawtypes")
				List<ReportInfo> reportInfos = parseResult.getReports();
				
				ReportInfoService.reportInfoToDb(reportInfos, reportConnection, "000", recv_time, "9999", ReadIni.SECTION_VIS, report_sod_code,cts_code, "15", loggerBuffer,filepath,listDi);

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
					loggerBuffer.append("\n close  database connection error：" + e.getMessage());
				}
			}
			if(reportConnection != null) {
				try {
					reportConnection.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n Database connection close error"+e.getMessage());
				}
			}
		}
		return DataBaseAction.SUCCESS;
	}

	/**
	 * 
	 * @Title: insert_data
	 * @Description: 沙尘暴大气能见度数据入库
	 * @param recv_time 收到时间
	 * @param connection 数据库连接
	 * @param fileName 
	 * @param loggerBuffer 
	 * @param towers 一个文件中的所有铁塔数据
	 * @return DataBaseAction 成功与否标识
	 * @throws:
	 */
	private static DataBaseAction insert_data(List<SandChnVis> viss, java.sql.Connection connection, Date recv_time, String d_data_id, String fileName, StringBuffer loggerBuffer,String filepath) {

		List<StatDi> listDi = new ArrayList<StatDi>();

		ReadIni ini = ReadIni.getIni();

		String insert_sql = ini.getValue(ReadIni.SECTION_VIS, ReadIni.INSERT_SQL_KEY);
		PreparedStatement pStatement = null;
		try {
			StatDi di = null;

			pStatement = new LoggableStatement(connection, insert_sql);
			for (SandChnVis vis : viss) {
				di = new StatDi();
				// 对DI和pStatement设置值
				generatePstAndDi(di, vis, pStatement, recv_time, d_data_id,fileName,loggerBuffer,filepath);
				pStatement.addBatch();
				listDi.add(di);
			}
			try {
				pStatement.executeBatch();
				connection.commit();
				for (StatDi tdi : listDi) {
					diQueues.offer(tdi);
				}
				listDi.clear();
				return DataBaseAction.SUCCESS;
			} catch (SQLException e) {
				pStatement.clearParameters();
				pStatement.clearBatch();
				// 如果批量入库异常，执行单条数据入库
				// connection.setAutoCommit(true); //因为之前connection设置了非自动提交，转单条数据入库时，需设置回自动提交或最后手动提交
				loggerBuffer.append("\n Batch commit failed：" + fileName + "\n " + e.getMessage());
				for (SandChnVis soi : viss) {
					insert_one_data(recv_time, connection, d_data_id, pStatement, soi,fileName,loggerBuffer,filepath);
				}
				return DataBaseAction.BATCH_ERROR;
			}

		} catch (SQLException e) {
			loggerBuffer.append("\n get database connection error");
			return DataBaseAction.CONNECTION_ERROR;
		} finally {
			if (pStatement != null) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n close Statement error" + e.getMessage());
				}
			}
		}
	}

	/**
	 * 
	 * @Title: insert_one_data
	 * @Description: 单条数据入数据库
	 * @param recv_time
	 * @param connection
	 * @param d_data_id
	 * @param pStatement
	 * @param fileName 
	 * @param loggerBuffer 
	 * @param tower
	 * @return void
	 * @throws:
	 */
	private static void insert_one_data(Date recv_time, java.sql.Connection connection, String d_data_id, PreparedStatement pStatement, SandChnVis soi, String fileName, StringBuffer loggerBuffer, String filepath) {
		StatDi di = new StatDi();
		generatePstAndDi(di, soi, pStatement, recv_time, d_data_id,fileName,loggerBuffer, filepath);
		try {
			pStatement.execute();
			connection.commit();
		} catch (SQLException e1) {
			di.setPROCESS_STATE("0");// 0成功，1失败
			loggerBuffer.append("\n filename：" + fileName //
					+ "\n " + soi.getStationNumberChina() + " " + TimeUtil.date2String(soi.getD_dateTime(), TimeUtil.DATE_FMT_YMDHMS) //
					+ "\n execute sql error：" + ((LoggableStatement) pStatement).getQueryString() //
					+ "\n " + e1.getMessage());
			System.out.println(((LoggableStatement) pStatement).getQueryString());
		} finally {
			diQueues.offer(di);
		}
	}

	/**
	 * 
	 * @Title: generatePstAndDi
	 * @Description: 设置prepareStatement和DI值
	 * @param di DI对象
	 * @param vis 沙尘暴大气能见度实体类
	 * @param pStatement prepareStatement
	 * @param recv_time 收到时间
	 * @param fileName 
	 * @param loggerBuffer 
	 * @return void
	 * @throws:
	 */
	private static void generatePstAndDi(StatDi di, SandChnVis vis, PreparedStatement pStatement, Date recv_time, String d_data_id, String fileName, StringBuffer loggerBuffer, String filepath) {
		Map<String, Object> proMap = StationInfo.getProMap();
		di.setFILE_NAME_O(fileName);
		di.setDATA_TYPE(d_data_id);
		di.setDATA_TYPE_1(cts_code);
		di.setTT("");
		di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
		di.setPROCESS_START_TIME(TimeUtil.getSysTime());
		di.setFILE_NAME_N(fileName);
		di.setBUSINESS_STATE("1"); // 1成功，0失败
		di.setPROCESS_STATE("1"); // 1成功，0失败

//		int stationNumberN = DEFAULT_VALUE;
		Double latitude = 999999.0;
		Double longitude = 999999.0;
		Double elevationAltitude = 999999.0;
		
		Date dataTime = vis.getD_dateTime(); // 资料时间
		String stationNumberC = vis.getStationNumberChina(); // 字符站号
		String stationNumberN = StationCodeUtil.stringToAscii(stationNumberC);
		if (vis.getLatitude() !=999999.0) {
			latitude = vis.getLatitude(); // 纬
			longitude= vis.getLongitude(); // 纬度
			elevationAltitude = vis.getElevationAltitude(); // 测站海拔高度
		}else{
			String info = (String) proMap.get(stationNumberC + "+15");
			if (info == null) {
				loggerBuffer.append("\n In the configuration file, the station number does not exist : " + stationNumberC);

			}else{
				String[] infos = info.split(",");
				longitude = Double.parseDouble(infos[1]);
				latitude = Double.parseDouble(infos[2]);
				elevationAltitude=Double.parseDouble(infos[3]);
				
			}
		}
		String adminCode = getAdminCodeByStationNumberC(stationNumberC,loggerBuffer);
		Double observationTimeInterval = vis.getObservationTimeInterval(); // 观测时间间隔
//		Double projectCode = vis.getProjectCode();	//项目代码
//		Double stateCode = vis.getStateCode();	//状态码
		Double averageVisibility_1min = vis.getAverageVisibility_1min();	//1分钟平均能见度
		Double averageVisibility_10min = vis.getAverageVisibility_10min();	//10分钟平均能见度
		Double trendOfVisibilityChange = vis.getTrendOfVisibilityChange();	//能见度变化趋势

		int num = 1;
		// 入库时间
		Timestamp insertTime = new Timestamp(new Date().getTime());
		// 资料时间
		Timestamp _dataTime = new Timestamp(dataTime.getTime());

		String lengthStr = "00000";
		try {
			// 时间(yyyyMMddHHmmss)_微秒（毫秒转微秒）SSSSSS_站号5_经度10_纬度9_海拔或高度相关8_(V_BBB)_其他字段
			String id = TimeUtil.date2String(dataTime, TimeUtil.DATE_FMT_YMDHMS) //
					+ "_" + (lengthStr.substring(0, 5 - stationNumberC.length()) + stationNumberC); //
//					+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(longitude * 1000000), 10) //
//					+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(latitude * 1000000), 9)//
//					+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(elevationAltitude * 100), 8);

			pStatement.setString(num++, id); // 记录标识
			pStatement.setString(num++, d_data_id); // 资料标识，由配置文件配置
			pStatement.setTimestamp(num++, insertTime); // 入库时间
			pStatement.setTimestamp(num++, new Timestamp(recv_time.getTime())); // 收到时间
			// 如果是新数据更新时间与入库时间一致
			pStatement.setTimestamp(num++, insertTime); // 更新时间第一次入库与入库时间一致

			pStatement.setTimestamp(num++, _dataTime); // 资料时间
			pStatement.setString(num++, stationNumberC); // 区站号（字符）
			pStatement.setBigDecimal(num++, new BigDecimal(stationNumberN)); // 区站号（数字）

			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(latitude)); // 纬度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(longitude)); // 经度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(elevationAltitude)); // 测站海拔高度
			pStatement.setBigDecimal(num++, new BigDecimal(adminCode)); // 行政区划代码

			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getYear(dataTime))); // 资料观测年
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getMonth(dataTime))); // 资料观测月
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getDayOfMonth(dataTime))); // 资料观测日
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getHourOfDay(dataTime))); // 资料观测时
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getMinute(dataTime))); // 资料观测分
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getSecond(dataTime))); // 资料观测秒

			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(observationTimeInterval))); // 观测时间间隔

			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(vis.getProjectCode()))); // 项目代码
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(vis.getStateCode()))); // 状态码
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(averageVisibility_1min)); // 1分钟平均能见度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(averageVisibility_10min)); // 10分钟平均能见度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(trendOfVisibilityChange)); // 能见度变化趋势
			pStatement.setString(num++, "000");
			pStatement.setString(num++, cts_code);
			// if (isUpdate) {
			// pStatement.setTimestamp(num++, _dataTime); // 资料时间
			// pStatement.setString(num++, stationNumberC); // 区站号（字符）
			// }

		} catch (SQLException e) {
			loggerBuffer.append("\n Failed to set field values: " + e.getMessage());
		}

		di.setIIiii(stationNumberC);
		di.setDATA_TIME(TimeUtil.date2String(dataTime, "yyyy-MM-dd HH:mm"));
		di.setPROCESS_END_TIME(TimeUtil.getSysTime());
		di.setRECORD_TIME(TimeUtil.getSysTime());
		di.setLATITUDE(String.valueOf(NumberUtil.FormatNumOrNine(latitude)));
		di.setLONGTITUDE(String.valueOf(NumberUtil.FormatNumOrNine(longitude)));
		
		di.setSEND("BFDB");
		di.setSEND_PHYS("DRDS");
		di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
		di.setDATA_UPDATE_FLAG("000");
		di.setHEIGHT(String.valueOf(elevationAltitude));
		
	}
	
	private static String getAdminCodeByStationNumberC(String stationNumberC, StringBuffer loggerBuffer) {
		Map<String, Object> proMap = StationInfo.getProMap();
		// 根据站号查询行政区划代码
		String info = (String) proMap.get(stationNumberC + "+15");
		String defaultS="999999";
		if (info == null) {
			// 如果此站号不存在，执行下一个数据
			loggerBuffer.append("\n In the configuration file, the station number does not exist : " + stationNumberC);
			return defaultS;
		}else{
			String[] infos = info.split(",");
			return infos[5]; // 行政区划代码
		}
	}
}
