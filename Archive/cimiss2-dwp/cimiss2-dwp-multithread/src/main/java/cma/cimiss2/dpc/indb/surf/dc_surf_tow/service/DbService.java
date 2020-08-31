package cma.cimiss2.dpc.indb.surf.dc_surf_tow.service;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.cimiss2.dwp.tools.utils.NumberUtil;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.surf.Tower;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.surf.dc_surf_tow.ReadIni;

public class DbService {
	private static final int DEFAULT_VALUE = 999999;
	public static BlockingQueue<StatDi> diQueues;
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static String v_bbb = "000";
	// 是否是更新文件
	private static boolean isUpdate = false;
	Map<String, Object> proMap = StationInfo.getProMap();
	private static String fileName = null;
	
	public static void setDiQueues(BlockingQueue<StatDi> diQueue){
		DbService.diQueues = diQueue;
	}
	
	public static DataBaseAction insert_db(ParseResult<Tower> parseResult, Date recv_time, String fn, StringBuffer loggerBuffer) {
		// 获取数据库连接
		fileName = fn;
		java.sql.Connection connection = null;
		try {
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");

			if (connection != null) {
				connection.setAutoCommit(false); // 关闭自动提交
				// list: 所有数据
				List<Tower> list = parseResult.getData();
				ReadIni ini = ReadIni.getIni();
				
				String d_data_id = ini.getValue(ReadIni.SECTION_TOWER, ReadIni.D_DATA_ID_KEY);
				if(isUpdate) {
					update_tower_data(list, connection, recv_time, d_data_id, loggerBuffer);
				}else {
					insert_tower_data(list, connection, recv_time, d_data_id, loggerBuffer);
				}
				
			} else {
				loggerBuffer.append("\n  Database connection error!");
				return DataBaseAction.CONNECTION_ERROR;
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n Database connection error!");
			return DataBaseAction.CONNECTION_ERROR;
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n Close database connection error: " + e.getMessage());
				}
			}
		}
		return DataBaseAction.SUCCESS;
	}


	/**
	 * 
	 * @Title: insert_tower_data
	 * @Description: 铁塔数据入库
	 * @param recv_time 收到时间
	 * @param connection 数据库连接
	 * @param towers 一个文件中的所有铁塔数据
	 * @param loggerBuffer 
	 * @return DataBaseAction 成功与否标识
	 * @throws:
	 */
	private static DataBaseAction insert_tower_data(List<Tower> towers, java.sql.Connection connection, Date recv_time, String d_data_id, StringBuffer loggerBuffer) {

		List<StatDi> listDi = new ArrayList<StatDi>();

		ReadIni ini = ReadIni.getIni();

		String insert_sql = ini.getValue(ReadIni.SECTION_TOWER, ReadIni.INSERT_SQL_KEY);
		// String d_data_id = ini.getValue(ReadIniPheno.SECTION_PHENO_01, ReadIniPheno.D_DATA_ID_KEY);
		PreparedStatement pStatement = null;
		try {
			StatDi di = null;

			// pStatement = connection.prepareStatement(insert_sql);
			pStatement = new LoggableStatement(connection, insert_sql);
			if(StartConfig.getDatabaseType() == 1) {
				pStatement.execute("select last_txc_xid()");
			}
			for (Tower tower : towers) {
				di = new StatDi();
				// 对DI和pStatement设置值
				generatePstAndDi(di, tower, pStatement, recv_time, d_data_id, loggerBuffer);
				pStatement.addBatch();
				listDi.add(di);
			}
			try {
				pStatement.executeBatch();
				connection.commit();

				for (StatDi tdi : listDi) {
					diQueues.offer(tdi);
				}

				return DataBaseAction.SUCCESS;
			} catch (SQLException e) {
				pStatement.clearParameters();
				pStatement.clearBatch();
				// 如果批量入库异常，执行单条数据入库
				// connection.setAutoCommit(true); //因为之前connection设置了非自动提交，转单条数据入库时，需设置回自动提交或最后手动提交
				loggerBuffer.append("\n Batch commit error: " + fileName + "\n " + e.getMessage());
				for (Tower tower : towers) {
//					Agme_Pheno_01 agme_Pheno_01 = (Agme_Pheno_01) obj;
					insert_one_tower_data(recv_time, connection, d_data_id, pStatement, tower, loggerBuffer);
				}
				return DataBaseAction.BATCH_ERROR;
			}

		} catch (SQLException e) {
			loggerBuffer.append("\n Database connection error!");
			return DataBaseAction.CONNECTION_ERROR;
		} finally {
			if (pStatement != null) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n Close Statement error: " + e.getMessage());
				}
			}
		}
	}

	/**
	 * 
	 * @Title: insert_one_tower_data
	 * @Description: 单条数据入数据库
	 * @param recv_time
	 * @param connection
	 * @param d_data_id
	 * @param pStatement
	 * @param tower
	 * @param loggerBuffer 
	 * @return void
	 * @throws:
	 */
	private static void insert_one_tower_data(Date recv_time, java.sql.Connection connection, String d_data_id, PreparedStatement pStatement, Tower tower, StringBuffer loggerBuffer) {
		StatDi di = new StatDi();
		generatePstAndDi(di, tower, pStatement, recv_time, d_data_id, loggerBuffer);
		try {
			pStatement.execute();
			connection.commit();
		} catch (SQLException e1) {
			di.setPROCESS_STATE("0");// 1成功，0失败
			loggerBuffer.append("\n File name: " + fileName //
					+ "\n " + tower.getStationNumberC() + " " + TimeUtil.date2String(tower.getObsTime(), TimeUtil.DATE_FMT_YMDHMS) + " "
					+ tower.getHight() //
					+ "\n execute sql error: " + ((LoggableStatement) pStatement).getQueryString() //
					+ "\n " + e1.getMessage());
		} finally {
			diQueues.offer(di);
		}
	}

	/**
	 * 
	 * @Title: generatePstAndDi
	 * @Description: 设置prepareStatement和DI值
	 * @param di DI对象
	 * @param tower 中科院铁塔数据要素
	 * @param pStatement prepareStatement
	 * @param recv_time 收到时间
	 * @param loggerBuffer 
	 * @return void
	 * @throws:
	 */
	private static void generatePstAndDi(StatDi di, Tower tower, PreparedStatement pStatement, Date recv_time, String d_data_id, StringBuffer loggerBuffer) {

		di.setFILE_NAME_O(fileName);
		di.setDATA_TYPE(d_data_id);
		di.setDATA_TYPE_1(StartConfig.ctsCode());
		di.setTT("中科院铁塔");
		di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
		di.setPROCESS_START_TIME(TimeUtil.getSysTime());
		di.setFILE_NAME_N(fileName);
		di.setBUSINESS_STATE("1"); // 1成功，0失败
		di.setPROCESS_STATE("1"); // 1成功，0失败

		int stationNumberN = DEFAULT_VALUE;
		
		
		String stationNumberC = tower.getStationNumberC();	//字符站号
		Double hight = tower.getHight();//观测平台距地面的高度。单位-米
		Double windSpeedNW = tower.getWindSpeedNW();	//西北方向伸臂上风速传感器观测值。单位-米/秒
		Double windSpeedSE = tower.getWindSpeedSE();	//东南方向伸臂上风速传感器观测值。单位-米/秒
		Double windDirection = tower.getWindDirection();//风向传感器观测值。单位－度
		Double relativeHumidity = tower.getRelativeHumidity();//相对湿度传感器观测值。单位-百分比
		Double temperature = tower.getTemperature();	//大气温度传感器观测值。单位-摄氏度
		Date obsTime = tower.getObsTime();	//观测时间
		
		int num = 1;
		// 入库时间
		Timestamp insertTime = new Timestamp(new Date().getTime());
		
		Calendar c = Calendar.getInstance();
		c.setTime(obsTime);
		c.add(Calendar.HOUR_OF_DAY, -8);
//		c.set(Calendar.MINUTE, 0);
//		c.set(Calendar.SECOND, 0);
		Timestamp d_datetime = new Timestamp(c.getTime().getTime());
		
//		Double latitude = 116.36666666666666; // 纬度
//		Double longitude = 39.96666666666667; // 经度
//		Double latitude = 116.22; // 纬度
//		Double longitude = 39.58; // 经度
		
		Double latitudeBJ = 39.9744; // 纬度
		Double longitudeBJ = 116.3712; // 经度
		Double heightBJ = 325.0; // 台站高度
		Double latitudeXH = 39.7534; // 纬度
		Double longitudeXH = 116.9604; // 经度
		Double heightXH = 102.0; // 台站高度
		double latitude = latitudeBJ;
		double longitude = longitudeBJ;
		double height = heightBJ;
		if(stationNumberC.contains("XH")){
			latitude = latitudeXH;
			longitude = longitudeXH;
			height = heightXH;
		}
		
		String lengthStr = "00000";
		try {
			if (!isUpdate) {
				// 时间(yyyyMMddHHmmss)_微秒（毫秒转微秒）SSSSSS_站号5_经度10_纬度9_海拔或高度相关8_(V_BBB)_其他字段
				String id = TimeUtil.date2String(c.getTime(), TimeUtil.DATE_FMT_YMDHMS) //
						+ "_" + (lengthStr.substring(0, 5 - stationNumberC.length()) + stationNumberC) //
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(longitude * 1000000), 10) //
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(latitude * 1000000), 9)//
						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(hight * 100), 8);

				pStatement.setString(num++, id); // 记录标识
			}
			pStatement.setString(num++, d_data_id); // 资料标识，由配置文件配置
			if (!isUpdate) {
				pStatement.setTimestamp(num++, insertTime); // 入库时间
			}
			pStatement.setTimestamp(num++, new Timestamp(recv_time.getTime())); // 收到时间
			// 如果是新数据更新时间与入库时间一致
			pStatement.setTimestamp(num++, insertTime); // 更新时间第一次入库与入库时间一致

			if (!isUpdate) {
				pStatement.setTimestamp(num++, d_datetime); // 资料时间
				pStatement.setString(num++, v_bbb); // 默认000，首次入库可不填，数据库字段默认000
				pStatement.setString(num++, stationNumberC); // 区站号（字符）
				pStatement.setInt(num++, stationNumberN); // 区站号（数字）
			}
			
			
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(latitude))); // 纬度
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(longitude))); // 经度
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(height))); // 测站海拔高度
			if(!isUpdate) {
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(hight)); // 气压传感器海拔高度
			}
			
			pStatement.setString(num++, ReadIni.getIni().getValue(ReadIni.SECTION_TOWER, "collector_model"));	//采集器型号
			pStatement.setBigDecimal(num ++, new BigDecimal("999998"));	//测站类型
			pStatement.setBigDecimal(num ++, new BigDecimal("999998"));	//风速表类型
			pStatement.setBigDecimal(num ++, new BigDecimal("999998"));	//温度传感器类型
			pStatement.setBigDecimal(num ++, new BigDecimal("999998"));	//湿度传感器类型

			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getYear(c.getTime()))); // 资料观测年
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getMonth(c.getTime()))); // 资料观测月
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getDayOfMonth(c.getTime()))); // 资料观测日
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getHourOfDay(c.getTime()))); // 资料观测时
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getMinute(c.getTime()))); // 资料观测分
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getSecond(c.getTime()))); // 资料观测秒
			
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(temperature)); // 气温
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(relativeHumidity)); // 相对湿度
			pStatement.setBigDecimal(num ++, NumberUtil.FormatNumOrNine(windDirection));	//风向
			pStatement.setBigDecimal(num ++, NumberUtil.FormatNumOrNine(windSpeedNW));	
			pStatement.setBigDecimal(num ++, NumberUtil.FormatNumOrNine(windSpeedSE));
			
			
			if(isUpdate) {
				pStatement.setTimestamp(num++, d_datetime); // 资料时间
				pStatement.setString(num++, stationNumberC); // 区站号（字符）
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(hight)); // 气压传感器海拔高度
			}
			
			pStatement.setString(num ++, StartConfig.ctsCode());
			
		} catch (SQLException e) {
			loggerBuffer.append("\n Create statement error: " + e.getMessage());
		}

		di.setIIiii(stationNumberC);
		di.setDATA_TIME(TimeUtil.date2String(d_datetime, "yyyy-MM-dd HH:mm"));
		di.setPROCESS_END_TIME(TimeUtil.getSysTime());
		di.setRECORD_TIME(TimeUtil.getSysTime());
		
		di.setLATITUDE(String.valueOf(latitude));
		di.setLONGTITUDE(String.valueOf(longitude));
		di.setSEND("BFDB");
		di.setSEND_PHYS("DRDS");
		di.setFILE_SIZE(String.valueOf(new File(fileName).length()));
		di.setDATA_UPDATE_FLAG("000");
		di.setHEIGHT(String.valueOf(height));
		
	}

	/**
	 * 
	 * @Title: update_pheno_data_01
	 * @Description: 更新数据库数据
	 * @param agme_Pheno_01s
	 * @param connection
	 * @param recv_time void
	 * @param loggerBuffer 
	 * @throws:
	 */
	private static void update_tower_data(List<Tower> towers, java.sql.Connection connection, Date recv_time, String d_data_id, StringBuffer loggerBuffer) {

		ReadIni ini = ReadIni.getIni();

		String update_sql = ini.getValue(ReadIni.SECTION_TOWER, ReadIni.UPDATE_SQL_KEY);

		PreparedStatement pStatement = null;

		for (Tower tower : towers) {
			String db_v_bbb = find_tower_data(tower, connection); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if (db_v_bbb == null) { // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				insert_one_tower_data(recv_time, connection, d_data_id, pStatement, tower, loggerBuffer);
			} else if (db_v_bbb.compareTo(v_bbb) < 0) { // 数据库中有该条记录，且V_BBB的值早于当前处理文件的更正标识的值

				try {
					pStatement = new LoggableStatement(connection, update_sql);
					insert_one_tower_data(recv_time, connection, d_data_id, pStatement, tower, loggerBuffer);
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					if (pStatement != null) {
						try {
							pStatement.close();
						} catch (SQLException e) {
							loggerBuffer.append("\n Close Statement error: " + e.getMessage());
						}
					}
				}

			} else { // 数据库中有该条记录，且V_BBB的值晚于当前处理文件的更正标识的值
						// 不进行插入或更新
			}
		}
	}

	/**
	 * 
	 * @Title: find_pheno_data_01
	 * @Description: 查询数据表中的更正标识
	 * @param agme_Pheno_01
	 * @param connection
	 * @return V_BBB更正标识
	 * @throws:
	 */
	private static String find_tower_data(Tower tower, java.sql.Connection connection) {
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		ReadIni ini = ReadIni.getIni();
		String select_sql = ini.getValue(ReadIni.SECTION_TOWER, ReadIni.SELECT_SQL_KEY);
		try {

			pstmt = connection.prepareStatement(select_sql);
			int num = 1;
			Date obsTime = tower.getObsTime();
			
			Calendar c = Calendar.getInstance();
			c.setTime(obsTime);
			c.add(Calendar.HOUR_OF_DAY, -8);
//			c.set(Calendar.MINUTE, 0);
//			c.set(Calendar.SECOND, 0);
			Timestamp d_datetime = new Timestamp(c.getTime().getTime());
			
			pstmt.setTimestamp(num++, d_datetime); // 时间
			pstmt.setString(num++, tower.getStationNumberC()); // 站号
			pstmt.setBigDecimal(num++, NumberUtil.FormatNumOrNine(tower.getHight())); // 高度

			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {
				return resultSet.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			if(pstmt != null){
				try {
					pstmt.close();
				}
				catch (Exception e) {
					infoLogger.error("close statement error!");
				}
			}
			if(resultSet != null){
				try{
					resultSet.close();
				}catch (Exception e) {
					infoLogger.error("close statement error!");
				}
			}
		}
		return null;
	}

}
