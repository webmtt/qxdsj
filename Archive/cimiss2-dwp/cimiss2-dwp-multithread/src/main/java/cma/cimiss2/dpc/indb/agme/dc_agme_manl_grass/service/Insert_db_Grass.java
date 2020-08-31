package cma.cimiss2.dpc.indb.agme.dc_agme_manl_grass.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.config.CTSCodeMap;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_01;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_02;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_03;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_04;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_05;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_06;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_07;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_08;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_09;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_10;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class Insert_db_Grass {
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");

	public static String v_bbb = "000";

//	static Map<String, Object> proMap = StationInfo.getProMap();

	/**
	 * @param loggerBuffer 
	 * @param ctsCodeMap 
	 * @Title: insert_db_GRASS01 @Description: TODO(这里用一句话描述这个方法的作用) @param
	 * list @param connection @param recv_time @param listDi @param
	 * fileN @return 返回值说明 @throws
	 */
	public static DataBaseAction insert_db_GRASS01(List<Agme_Grass_01> list, java.sql.Connection connection,
			Date recv_time, List<StatDi> listDi, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		int sz = listDi.size();
		try {
			// chy  去掉 D_RECORD_ID
			String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+" (D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V01301,"
					+ "V01300,V05001,V06001,V07001,V07031,V_ACODE,V04001,V04002,V04003,V71501,V71002,V71010,D_SOURCE_ID)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			if (connection != null) {
				connection.setAutoCommit(false);
				pStatement = connection.prepareStatement(sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				// 用于获取时间
				Calendar calendar = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				for (int i = 0; i < list.size(); i++) {

					StatDi di = new StatDi();
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMap.getSod_code());
					di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
					di.setTT("");
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); // 1成功，0失败
					di.setPROCESS_STATE("1"); // 1成功，0失败
					
					Agme_Grass_01 Agme_Grass_01 = list.get(i);
					di.setLATITUDE(Agme_Grass_01.getLatitude().toString());
					di.setLONGTITUDE(Agme_Grass_01.getLongitude().toString());
					Date obsTime = Agme_Grass_01.getObservationTime();
					int num = 1;
					// D_DATETIME, V01301, V71501, V71002
					String agme_grass01_chn_tab_pk = sdf.format(obsTime) + "_" + Agme_Grass_01.getStationNumberChina()
							+ "_" + Agme_Grass_01.getHerbageName() + "_" + Agme_Grass_01.getDevelopmentalPeriod();
					pStatement.setString(num++, agme_grass01_chn_tab_pk);
					pStatement.setString(num++, ctsCodeMap.getSod_code()); // 资料标识，由配置文件配置
					pStatement.setTimestamp(num++, new Timestamp(new Date().getTime())); // 入库时间
					pStatement.setTimestamp(num++, new Timestamp(recv_time.getTime())); // 收到时间
					pStatement.setTimestamp(num++, new Timestamp(new Date().getTime())); // 更新时间
					calendar.setTime(obsTime);
					pStatement.setTimestamp(num++, new Timestamp(obsTime.getTime())); // 资料时间
					pStatement.setString(num++, Agme_Grass_01.getStationNumberChina()); // 区站号（字符）
					pStatement.setString(num++, StationCodeUtil.stringToAscii(Agme_Grass_01.getStationNumberChina())); // 区站号（数字）
					pStatement.setBigDecimal(num++, new BigDecimal(Agme_Grass_01.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 纬度
					pStatement.setBigDecimal(num++, new BigDecimal(Agme_Grass_01.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 经度
					pStatement.setBigDecimal(num++,
							new BigDecimal(String.valueOf(Agme_Grass_01.getHeightOfSationGroundAboveMeanSeaLevel()))); // 测站海拔高度
					pStatement.setBigDecimal(num++,
							new BigDecimal(String.valueOf(Agme_Grass_01.getHeightOfBarometerAboveMeanSeaLevel())));// 气压传感器海拔高度
																									// //
					pStatement.setString(num++, getAdminCode(Agme_Grass_01.getStationNumberChina(), loggerBuffer));

					pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.YEAR))); // 资料观测年
					pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.MONTH) + 1)); // 资料观测月
					pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.DAY_OF_MONTH))); // 资料观测日

					pStatement.setBigDecimal(num++, new BigDecimal(Agme_Grass_01.getHerbageName()));// 牧草名称
					pStatement.setBigDecimal(num++, new BigDecimal(Agme_Grass_01.getDevelopmentalPeriod()));// 发育期
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(Agme_Grass_01.getPerDevlopmentPeriod())));// 发育期百分率
					pStatement.setString(num++, ctsCodeMap.getCts_code());
						
					di.setIIiii(list.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLATITUDE(list.get(i).getLatitude().toString());
					di.setLONGTITUDE(list.get(i).getLongitude().toString());
					di.setDATA_UPDATE_FLAG(v_bbb);
					di.setHEIGHT(list.get(i).getHeightOfSationGroundAboveMeanSeaLevel().toString());
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(file.length()));
					pStatement.addBatch();
					listDi.add(di);
				} // for end
				try {
					pStatement.executeBatch();
					connection.commit();
					return DataBaseAction.SUCCESS;
				} catch (Exception e) {
					pStatement.clearParameters();
					pStatement.clearBatch();
					for (int i = 0; i < list.size(); i++) {
						listDi.remove(sz);
					}
					for (int i = 0; i < list.size(); i++) {
						insertOneLine_db_GRASS01(list.get(i), connection, recv_time, listDi, filepath,loggerBuffer,ctsCodeMap);
					}
					loggerBuffer.append("\n Batch commit failed ：" + fileN);
					return DataBaseAction.BATCH_ERROR;
				}
			} // end if
			else {
				loggerBuffer.append("\n Database connection error");
				return DataBaseAction.CONNECTION_ERROR;
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n Database connection close error：" + e.getMessage());
			return DataBaseAction.CONNECTION_ERROR;
		} finally {
			try {
				if (pStatement != null)
					pStatement.close();
			} catch (SQLException e) {
				loggerBuffer.append("\n Database connection close error：" + e.getMessage());
			}
		}
	}

	/**
	 * @param loggerBuffer 
	 * @param ctsCodeMap 
	 * @Title: insertOneLine_db_GRASS01 @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param agme_Grass_01 @param connection @param
	 * recv_time @param listDi @param fileN 返回值说明 @throws
	 */
	public static void insertOneLine_db_GRASS01(Agme_Grass_01 agme_Grass_01, java.sql.Connection connection,
			Date recv_time, List<StatDi> listDi, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		Calendar calendar = Calendar.getInstance();
		// chy 去掉 D_RECORD_ID
		String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+" (D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V01301,"
				+ "V01300,V05001,V06001,V07001,V07031,V_ACODE,V04001,V04002,V04003,V71501,V71002,V71010,D_SOURCE_ID)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		if (connection != null) {
			try {
				connection.setAutoCommit(true);
				// pStatement = connection.prepareStatement(sql);
				pStatement = new LoggableStatement(connection, sql);
				/*if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}*/
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				StatDi di = new StatDi();
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(ctsCodeMap.getSod_code());
				di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
				di.setTT(v_bbb);
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); // 1成功，0失败
				di.setPROCESS_STATE("1"); // 1成功，0失败
				di.setLATITUDE(agme_Grass_01.getLatitude().toString());
				di.setLONGTITUDE(agme_Grass_01.getLongitude().toString());
				Date obsTime = agme_Grass_01.getObservationTime();
				int num = 1;
				// D_DATETIME, V01301, V71501, V71002
				String agme_grass01_chn_tab_pk = sdf.format(obsTime) + "_" + agme_Grass_01.getStationNumberChina() + "_"
						+ agme_Grass_01.getHerbageName() + "_" + agme_Grass_01.getDevelopmentalPeriod();
				pStatement.setString(num++, agme_grass01_chn_tab_pk);
				pStatement.setString(num++, ctsCodeMap.getSod_code()); // 资料标识，由配置文件配置
				pStatement.setTimestamp(num++, new Timestamp(new Date().getTime())); // 入库时间
				pStatement.setTimestamp(num++, new Timestamp(recv_time.getTime())); // 收到时间
				pStatement.setTimestamp(num++, new Timestamp(new Date().getTime())); // 更新时间

				calendar.setTime(obsTime);
				pStatement.setTimestamp(num++, new Timestamp(obsTime.getTime())); // 资料时间
				pStatement.setString(num++, agme_Grass_01.getStationNumberChina()); // 区站号（字符）
				pStatement.setString(num++, StationCodeUtil.stringToAscii(agme_Grass_01.getStationNumberChina())); // 区站号（数字）
				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_01.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 纬度
				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_01.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 经度
				pStatement.setBigDecimal(num++,
						new BigDecimal(String.valueOf(agme_Grass_01.getHeightOfSationGroundAboveMeanSeaLevel()))); // 测站海拔高度
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_01.getHeightOfBarometerAboveMeanSeaLevel())));// 气压传感器海拔高度
																														// //
				pStatement.setString(num++, getAdminCode(agme_Grass_01.getStationNumberChina(), loggerBuffer));

				pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.YEAR))); // 资料观测年
				pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.MONTH) + 1)); // 资料观测月
				pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.DAY_OF_MONTH))); // 资料观测日

				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_01.getHerbageName()));// 牧草名称
				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_01.getDevelopmentalPeriod()));// 发育期
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_01.getPerDevlopmentPeriod())));// 发育期百分率
				pStatement.setString(num++, ctsCodeMap.getCts_code());
				
				di.setIIiii(agme_Grass_01.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());
				di.setDATA_UPDATE_FLAG(v_bbb);
				di.setHEIGHT(agme_Grass_01.getHeightOfSationGroundAboveMeanSeaLevel().toString());
				di.setSEND_PHYS("DRDS");
				di.setFILE_SIZE(String.valueOf(file.length()));
			
				try {
					pStatement.execute();
//					connection.commit();
					listDi.add(di);
				} catch (SQLException e) {
					di.setPROCESS_STATE("0");// 1成功，0失败
					listDi.add(di);
					loggerBuffer.append("\n filename：" + fileN + "\n " + agme_Grass_01.getStationNumberChina() + " "
							+ sdf.format(agme_Grass_01.getObservationTime()) + "\n execute sql error："
							+ ((LoggableStatement) pStatement).getQueryString() + "\n " + e.getMessage());
				}
			} catch (SQLException e) {
				loggerBuffer.append("\n create Statement error" + e.getMessage());
			} finally {
				if (pStatement != null) {
					try {
						pStatement.close();
					} catch (SQLException e) {
						loggerBuffer.append("\n close Statement error" + e.getMessage());
					}
				}
			}
		}else {
			loggerBuffer.append("\n Database connection error");
		}
		
	}

	/**
	 * @param loggerBuffer 
	 * @param ctsCodeMap 
	 * @Title: insert_db_GRASS02 @Description: TODO(这里用一句话描述这个方法的作用) @param
	 * list @param connection @param recv_time @param listDi @param
	 * fileN @return 返回值说明 @throws
	 */
	public static DataBaseAction insert_db_GRASS02(List<Agme_Grass_02> list, java.sql.Connection connection,
			Date recv_time, List<StatDi> listDi, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		int sz = listDi.size();
		try {
			// chy 去掉 D_RECORD_ID
			String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+" (D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V01301,"
					+ "V01300,V05001,V06001,V07001,V07031,V_ACODE,V04001,V04002,V04003,V71501,V71006,D_SOURCE_ID)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			if (connection != null) {
				connection.setAutoCommit(false);
				pStatement = connection.prepareStatement(sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				// 用于获取时间
				Calendar calendar = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				for (int i = 0; i < list.size(); i++) {

					StatDi di = new StatDi();
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMap.getSod_code());
					di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
					di.setTT("");
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); // 1成功，0失败
					di.setPROCESS_STATE("1"); // 1成功，0失败

					Agme_Grass_02 Agme_Grass_02 = list.get(i);
					Date obsTime = Agme_Grass_02.getObservationTime();
					int num = 1;
					// D_DATETIME, V01301, V71501
					String agme_grass02_chn_tab_pk = sdf.format(obsTime) + "_" + Agme_Grass_02.getStationNumberChina()
							+ "_" + Agme_Grass_02.getHerbageName();
					pStatement.setString(num++, agme_grass02_chn_tab_pk); // 记录标识
					pStatement.setString(num++, ctsCodeMap.getSod_code()); // 资料标识，由配置文件配置
					pStatement.setTimestamp(num++, new Timestamp(new Date().getTime())); // 入库时间
					pStatement.setTimestamp(num++, new Timestamp(recv_time.getTime())); // 收到时间
					pStatement.setTimestamp(num++, new Timestamp(new Date().getTime())); // 更新时间

					calendar.setTime(obsTime);

					pStatement.setTimestamp(num++, new Timestamp(obsTime.getTime())); // 资料时间
					pStatement.setString(num++, Agme_Grass_02.getStationNumberChina()); // 区站号（字符）
					pStatement.setString(num++, StationCodeUtil.stringToAscii(Agme_Grass_02.getStationNumberChina())); // 区站号（数字）
					pStatement.setBigDecimal(num++, new BigDecimal(Agme_Grass_02.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 纬度
					pStatement.setBigDecimal(num++, new BigDecimal(Agme_Grass_02.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 经度
					pStatement.setBigDecimal(num++,
							new BigDecimal(String.valueOf(Agme_Grass_02.getHeightOfSationGroundAboveMeanSeaLevel()))); // 测站海拔高度
					pStatement.setBigDecimal(num++,
							new BigDecimal(String.valueOf(Agme_Grass_02.getHeightOfBarometerAboveMeanSeaLevel())));// 气压传感器海拔高度
																									// //

					pStatement.setString(num++, getAdminCode(Agme_Grass_02.getStationNumberChina(), loggerBuffer));
					pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.YEAR))); // 资料观测年
					pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.MONTH) + 1)); // 资料观测月
					pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.DAY_OF_MONTH))); // 资料观测日
					pStatement.setBigDecimal(num++, new BigDecimal(Agme_Grass_02.getHerbageName()));
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(Agme_Grass_02.getHeightGrowth())));
					pStatement.setString(num ++, ctsCodeMap.getCts_code());
						
					di.setIIiii(list.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLATITUDE(list.get(i).getLatitude().toString());
					di.setLONGTITUDE(list.get(i).getLongitude().toString());
					di.setDATA_UPDATE_FLAG(v_bbb);
					di.setHEIGHT(list.get(i).getHeightOfSationGroundAboveMeanSeaLevel().toString());
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(file.length()));
					pStatement.addBatch();
					listDi.add(di);
				} // for end
				try {
					pStatement.executeBatch();
					connection.commit();
					return DataBaseAction.SUCCESS;
				} catch (Exception e) {
					pStatement.clearParameters();
					pStatement.clearBatch();
					for (int i = 0; i < list.size(); i++) {
						listDi.remove(sz);
					}
					for (int i = 0; i < list.size(); i++) {
						insertOneLine_db_GRASS02(list.get(i), connection, recv_time, listDi, filepath,loggerBuffer,ctsCodeMap);
					}
					loggerBuffer.append("\n Batch commit failed：" + fileN);
					return DataBaseAction.BATCH_ERROR;
				}
			} // end if
			else {
				loggerBuffer.append("\n Database connection error");
				return DataBaseAction.CONNECTION_ERROR;
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n Database connection clsoe error：" + e.getMessage());
			return DataBaseAction.CONNECTION_ERROR;
		} finally {
			try {
				if (pStatement != null)
					pStatement.close();
			} catch (SQLException e) {
				loggerBuffer.append("\n Database connection clsoe error：" + e.getMessage());
			}
		}
	}

	/**
	 * @param loggerBuffer 
	 * @param ctsCodeMap 
	 * @Title: insertOneLine_db_GRASS02 @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param agme_Grass_02 @param connection @param
	 * recv_time @param listDi @param fileN 返回值说明 @throws
	 */
	public static void insertOneLine_db_GRASS02(Agme_Grass_02 agme_Grass_02, java.sql.Connection connection,
			Date recv_time, List<StatDi> listDi, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		Calendar calendar = Calendar.getInstance();
		// chy 去掉 D_RECORD_ID
		String sql = " INSERT INTO "+ctsCodeMap.getValue_table_name()+" (D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V01301,V01300,"
				+ "V05001,V06001,V07001,V07031,V_ACODE,V04001,V04002,V04003,V71501,V71006,D_SOURCE_ID)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		if (connection != null) {
			try {
				connection.setAutoCommit(true);
				// pStatement = connection.prepareStatement(sql);
				pStatement = new LoggableStatement(connection, sql);
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				StatDi di = new StatDi();
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(ctsCodeMap.getSod_code());
				di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
				di.setTT(v_bbb);
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); // 1成功，0失败
				di.setPROCESS_STATE("1"); // 1成功，0失败

				Date obsTime = agme_Grass_02.getObservationTime();
				int num = 1;
				// D_DATETIME, V01301, V71501
				String agme_grass02_chn_tab_pk = sdf.format(obsTime) + "_" + agme_Grass_02.getStationNumberChina() + "_"
						+ agme_Grass_02.getHerbageName();
				pStatement.setString(num++, agme_grass02_chn_tab_pk); // 记录标识
				pStatement.setString(num++,ctsCodeMap.getSod_code()); // 资料标识，由配置文件配置
				pStatement.setTimestamp(num++, new Timestamp(new Date().getTime())); // 入库时间
				pStatement.setTimestamp(num++, new Timestamp(recv_time.getTime())); // 收到时间
				pStatement.setTimestamp(num++, new Timestamp(new Date().getTime())); // 更新时间

				calendar.setTime(obsTime);

				pStatement.setTimestamp(num++, new Timestamp(obsTime.getTime())); // 资料时间
				pStatement.setString(num++, agme_Grass_02.getStationNumberChina()); // 区站号（字符）
				pStatement.setString(num++, StationCodeUtil.stringToAscii(agme_Grass_02.getStationNumberChina())); // 区站号（数字）
				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_02.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 纬度
				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_02.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 经度
				pStatement.setBigDecimal(num++,
						new BigDecimal(String.valueOf(agme_Grass_02.getHeightOfSationGroundAboveMeanSeaLevel()))); // 测站海拔高度
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_02.getHeightOfBarometerAboveMeanSeaLevel())));// 气压传感器海拔高度
																														// //
																														// 中国行政区划代码

				pStatement.setString(num++, getAdminCode(agme_Grass_02.getStationNumberChina(), loggerBuffer));
				pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.YEAR))); // 资料观测年
				pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.MONTH) + 1)); // 资料观测月
				pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.DAY_OF_MONTH))); // 资料观测日
				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_02.getHerbageName()));
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_02.getHeightGrowth())));
				pStatement.setString(num++, ctsCodeMap.getCts_code());
					
				di.setIIiii(agme_Grass_02.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());
				di.setLATITUDE(agme_Grass_02.getLatitude().toString());
				di.setLONGTITUDE(agme_Grass_02.getLongitude().toString());
				di.setDATA_UPDATE_FLAG(v_bbb);
				di.setHEIGHT(agme_Grass_02.getHeightOfSationGroundAboveMeanSeaLevel().toString());
				di.setSEND_PHYS("DRDS");
				di.setFILE_SIZE(String.valueOf(file.length()));
				try {
					pStatement.execute();
//					connection.commit();
					listDi.add(di);
				} catch (SQLException e) {
					di.setPROCESS_STATE("1");// 0成功，1失败
					listDi.add(di);
					loggerBuffer.append("\n filename：" + fileN + "\n " + agme_Grass_02.getStationNumberChina() + " "
							+ sdf.format(agme_Grass_02.getObservationTime()) + "\n execute sql error："
							+ ((LoggableStatement) pStatement).getQueryString() + "\n " + e.getMessage());
				}
			} catch (SQLException e) {
				loggerBuffer.append("\n create Statement error" + e.getMessage());
			} finally {
				if (pStatement != null) {
					try {
						pStatement.close();
					} catch (SQLException e) {
						loggerBuffer.append("\n close Statement error" + e.getMessage());
					}
				}
			}
		}else{
			loggerBuffer.append("\n Database connection error");
		}
		
	}

	/**
	 * @param loggerBuffer 
	 * @param ctsCodeMap 
	 * @Title: insert_db_GRASS03 @Description: TODO(这里用一句话描述这个方法的作用) @param
	 * list @param connection @param recv_time @param listDi @param
	 * fileN @return 返回值说明 @throws
	 */
	public static DataBaseAction insert_db_GRASS03(List<Agme_Grass_03> list, java.sql.Connection connection,
			Date recv_time, List<StatDi> listDi, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		int sz = listDi.size();
		try {
			// chy 去掉D_RECORD_ID
			String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+" (D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V01301,V01300,"
					+ "V05001,V06001,V07001,V07031,V_ACODE,V04001,V04002,V04003,V71501,V71652,V71651,V71906,D_SOURCE_ID)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			if (connection != null) {
				connection.setAutoCommit(false);
				pStatement = connection.prepareStatement(sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				// 用于获取时间
				Calendar calendar = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				for (int i = 0; i < list.size(); i++) {

					StatDi di = new StatDi();
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMap.getSod_code());
					di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
					di.setTT("");
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); // 1成功，0失败
					di.setPROCESS_STATE("1"); // 1成功，0失败

					Agme_Grass_03 agme_Grass_03 = list.get(i);
					Date obsTime = agme_Grass_03.getObservationTime();
					int num = 1;
					// D_DATETIME, V01301, V71501
					String agme_grass03_chn_tab_pk = sdf.format(obsTime) + "_" + agme_Grass_03.getStationNumberChina()
							+ "_" + agme_Grass_03.getHerbageName()+ "_" +doubleParsInt(agme_Grass_03.getDryFreshRatio()) ;
					pStatement.setString(num++, agme_grass03_chn_tab_pk); // 记录标识
					pStatement.setString(num++, ctsCodeMap.getSod_code()); // 资料标识，由配置文件配置
					pStatement.setTimestamp(num++, new Timestamp(new Date().getTime())); // 入库时间
					pStatement.setTimestamp(num++, new Timestamp(recv_time.getTime())); // 收到时间
					pStatement.setTimestamp(num++, new Timestamp(new Date().getTime())); // 更新时间
					calendar.setTime(obsTime);

					pStatement.setTimestamp(num++, new Timestamp(obsTime.getTime())); // 资料时间
					pStatement.setString(num++, agme_Grass_03.getStationNumberChina()); // 区站号（字符）
					pStatement.setString(num++, StationCodeUtil.stringToAscii(agme_Grass_03.getStationNumberChina())); // 区站号（数字）
					pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_03.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 纬度
					pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_03.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 经度
					pStatement.setBigDecimal(num++,
							new BigDecimal(String.valueOf(agme_Grass_03.getHeightOfSationGroundAboveMeanSeaLevel()))); // 测站海拔高度
					pStatement.setBigDecimal(num++,
							new BigDecimal(String.valueOf(agme_Grass_03.getHeightOfBarometerAboveMeanSeaLevel())));
					// 气压传感器海拔高度 // 中国行政区划代码

					pStatement.setString(num++, getAdminCode(agme_Grass_03.getStationNumberChina(), loggerBuffer));
					pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.YEAR))); // 资料观测年
					pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.MONTH) + 1)); // 资料观测月
					pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.DAY_OF_MONTH))); // 资料观测日

					pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_03.getHerbageName()));// 牧草名称
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_03.getDryWeight())));// 干重
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_03.getFreshWeight())));// 鲜重
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_03.getDryFreshRatio())));// 干鲜比
					pStatement.setString(num++, ctsCodeMap.getCts_code());
						
					di.setIIiii(list.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLATITUDE(list.get(i).getLatitude().toString());
					di.setLONGTITUDE(list.get(i).getLongitude().toString());
					di.setDATA_UPDATE_FLAG(v_bbb);
					di.setHEIGHT(list.get(i).getHeightOfSationGroundAboveMeanSeaLevel().toString());
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(file.length()));
					pStatement.addBatch();
					listDi.add(di);
				} // for end
				try {
					pStatement.executeBatch();
					connection.commit();
					return DataBaseAction.SUCCESS;
				} catch (Exception e) {
					pStatement.clearParameters();
					pStatement.clearBatch();
					for (int i = 0; i < list.size(); i++) {
						listDi.remove(sz);
					}
					for (int i = 0; i < list.size(); i++) {
						insertOneLine_db_GRASS03(list.get(i), connection, recv_time, listDi, filepath,loggerBuffer,ctsCodeMap);
					}
					loggerBuffer.append("\n Batch commit failed：" + fileN);
					return DataBaseAction.BATCH_ERROR;
				}
			} // end if
			else {
				loggerBuffer.append("\n Database connection error");
				return DataBaseAction.CONNECTION_ERROR;
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n Database connection close error：" + e.getMessage());
			return DataBaseAction.CONNECTION_ERROR;
		} finally {
			try {
				if (pStatement != null)
					pStatement.close();
			} catch (SQLException e) {
				loggerBuffer.append("\n Database connection close error：" + e.getMessage());
			}
		}
	}

	/**
	 * @param loggerBuffer 
	 * @param ctsCodeMap 
	 * @Title: insertOneLine_db_GRASS03 @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param agme_Grass_03 @param connection @param
	 * recv_time @param listDi @param fileN 返回值说明 @throws
	 */
	public static void insertOneLine_db_GRASS03(Agme_Grass_03 agme_Grass_03, java.sql.Connection connection,
			Date recv_time, List<StatDi> listDi, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		Calendar calendar = Calendar.getInstance();
		// chy 去掉 D_RECORD_ID
		String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+" (D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V01301,V01300,"
				+ "V05001,V06001,V07001,V07031,V_ACODE,V04001,V04002,V04003,V71501,V71652,V71651,V71906,D_SOURCE_ID)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		if (connection != null) {
			try {
				connection.setAutoCommit(true);
				// pStatement = connection.prepareStatement(sql);
				pStatement = new LoggableStatement(connection, sql);
			
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				StatDi di = new StatDi();
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(ctsCodeMap.getSod_code());
				di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
				di.setTT(v_bbb);
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); // 1成功，0失败
				di.setPROCESS_STATE("1"); // 1成功，0失败

				Date obsTime = agme_Grass_03.getObservationTime();
				int num = 1;
				// D_DATETIME, V01301, V71501
				String agme_grass03_chn_tab_pk = sdf.format(obsTime) + "_" + agme_Grass_03.getStationNumberChina() + "_"
						+ agme_Grass_03.getHerbageName()+ "_" +doubleParsInt(agme_Grass_03.getDryFreshRatio());
				pStatement.setString(num++, agme_grass03_chn_tab_pk); // 记录标识
				pStatement.setString(num++, ctsCodeMap.getSod_code()); // 资料标识，由配置文件配置
				pStatement.setTimestamp(num++, new Timestamp(new Date().getTime())); // 入库时间
				pStatement.setTimestamp(num++, new Timestamp(recv_time.getTime())); // 收到时间
				pStatement.setTimestamp(num++, new Timestamp(new Date().getTime())); // 更新时间
				calendar.setTime(obsTime);

				pStatement.setTimestamp(num++, new Timestamp(obsTime.getTime())); // 资料时间
				pStatement.setString(num++, agme_Grass_03.getStationNumberChina()); // 区站号（字符）
				pStatement.setString(num++, StationCodeUtil.stringToAscii(agme_Grass_03.getStationNumberChina())); // 区站号（数字）
				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_03.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 纬度
				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_03.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 经度
				pStatement.setBigDecimal(num++,
						new BigDecimal(String.valueOf(agme_Grass_03.getHeightOfSationGroundAboveMeanSeaLevel()))); // 测站海拔高度
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_03.getHeightOfBarometerAboveMeanSeaLevel())));
				// 气压传感器海拔高度 // 中国行政区划代码

				pStatement.setString(num++, getAdminCode(agme_Grass_03.getStationNumberChina(), loggerBuffer));
				pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.YEAR))); // 资料观测年
				pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.MONTH) + 1)); // 资料观测月
				pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.DAY_OF_MONTH))); // 资料观测日

				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_03.getHerbageName()));// 牧草名称
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_03.getDryWeight())));// 干重
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_03.getFreshWeight())));// 鲜重
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_03.getDryFreshRatio())));// 干鲜比
				pStatement.setString(num++, ctsCodeMap.getCts_code());
				
				di.setIIiii(agme_Grass_03.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());
				di.setLATITUDE(agme_Grass_03.getLatitude().toString());
				di.setLONGTITUDE(agme_Grass_03.getLongitude().toString());
				di.setDATA_UPDATE_FLAG(v_bbb);
				di.setHEIGHT(agme_Grass_03.getHeightOfSationGroundAboveMeanSeaLevel().toString());
				di.setSEND_PHYS("DRDS");
				di.setFILE_SIZE(String.valueOf(file.length()));
				try {
					pStatement.execute();
					listDi.add(di);
				} catch (SQLException e) {
					di.setPROCESS_STATE("0");// 1成功，0失败
					listDi.add(di);
					loggerBuffer.append("\n filename：" + fileN + "\n " + agme_Grass_03.getStationNumberChina() + " "
							+ sdf.format(agme_Grass_03.getObservationTime()) + "\n execute sql error："
							+ ((LoggableStatement) pStatement).getQueryString() + "\n " + e.getMessage());
				}
			} catch (SQLException e) {
				loggerBuffer.append("\n create Statement error" + e.getMessage());
			} finally {
				if (pStatement != null) {
					try {
						pStatement.close();
					} catch (SQLException e) {
						loggerBuffer.append("\n close Statement error" + e.getMessage());
					}
				}
			}
		}else {
			loggerBuffer.append("\n Database connection error");
		}
		
	}

	/**
	 * @param loggerBuffer 
	 * @param ctsCodeMap 
	 * @Title: insert_db_GRASS04 @Description: TODO(这里用一句话描述这个方法的作用) @param
	 * list @param connection @param recv_time @param listDi @param
	 * fileN @return 返回值说明 @throws
	 */
	public static DataBaseAction insert_db_GRASS04(List<Agme_Grass_04> list, java.sql.Connection connection,
			Date recv_time, List<StatDi> listDi, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		int sz = listDi.size();
		try {
			// chy 去掉 D_RECORD_ID
			String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+" (D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V01301,V01300,"
					+ "V05001,V06001,V07001,V07031,V_ACODE,V04001,V04002,V04003,V71009,V71907,V71908,V71909,D_SOURCE_ID)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			if (connection != null) {
				connection.setAutoCommit(false);
				pStatement = connection.prepareStatement(sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				// 用于获取时间
				Calendar calendar = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				for (int i = 0; i < list.size(); i++) {

					StatDi di = new StatDi();
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMap.getSod_code());
					di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
					di.setTT("");
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); // 1成功，0失败
					di.setPROCESS_STATE("1"); // 1成功，0失败

					Agme_Grass_04 agme_Grass_04 = list.get(i);

					int num = 1;
					Date obsTime = agme_Grass_04.getObservationTime();
					// D_DATETIME, V01301, V71009
					int intCoverDegree = doubleParsInt(agme_Grass_04.getCoverDegree());
					String agme_grass04_chn_tab_pk = sdf.format(obsTime) + "_" + agme_Grass_04.getStationNumberChina()
							+ "_" + intCoverDegree;
					pStatement.setString(num++, agme_grass04_chn_tab_pk);
					pStatement.setString(num++, ctsCodeMap.getSod_code()); // 资料标识，由配置文件配置
					pStatement.setTimestamp(num++, new Timestamp(new Date().getTime())); // 入库时间
					pStatement.setTimestamp(num++, new Timestamp(recv_time.getTime())); // 收到时间
					pStatement.setTimestamp(num++, new Timestamp(new Date().getTime())); // 更新时间

					calendar.setTime(obsTime);

					pStatement.setTimestamp(num++, new Timestamp(obsTime.getTime())); // 资料时间
					pStatement.setString(num++, agme_Grass_04.getStationNumberChina()); // 区站号（字符）
					pStatement.setString(num++, StationCodeUtil.stringToAscii(agme_Grass_04.getStationNumberChina())); // 区站号（数字）
					pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_04.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 纬度
					pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_04.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 经度
					pStatement.setBigDecimal(num++,
							new BigDecimal(String.valueOf(agme_Grass_04.getHeightOfSationGroundAboveMeanSeaLevel()))); // 测站海拔高度
					pStatement.setBigDecimal(num++,
							new BigDecimal(String.valueOf(agme_Grass_04.getHeightOfBarometerAboveMeanSeaLevel())));
					// 气压传感器海拔高度 // 中国行政区划代码

					pStatement.setString(num++, getAdminCode(agme_Grass_04.getStationNumberChina(), loggerBuffer));
					pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.YEAR))); // 资料观测年
					pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.MONTH) + 1)); // 资料观测月
					pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.DAY_OF_MONTH))); // 资料观测日

					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_04.getCoverDegree())));// 覆盖度
					pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_04.getEvaluaGrassCondition()));// 草层状况评价
					pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_04.getFeedingDegree()));// 采食度
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_04.getFeedingRate())));// 采食率
					pStatement.setString(num++, ctsCodeMap.getCts_code());
					
					di.setIIiii(list.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLATITUDE(list.get(i).getLatitude().toString());
					di.setLONGTITUDE(list.get(i).getLongitude().toString());
					di.setDATA_UPDATE_FLAG(v_bbb);
					di.setHEIGHT(list.get(i).getHeightOfSationGroundAboveMeanSeaLevel().toString());
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(file.length()));
					pStatement.addBatch();
					listDi.add(di);
				} // for end
				try {
					pStatement.executeBatch();
					connection.commit();
					return DataBaseAction.SUCCESS;
				} catch (Exception e) {
					pStatement.clearParameters();
					pStatement.clearBatch();
					for (int i = 0; i < list.size(); i++) {
						listDi.remove(sz);
					}
					for (int i = 0; i < list.size(); i++) {
						insertOneLine_db_GRASS04(list.get(i), connection, recv_time, listDi, filepath,loggerBuffer,ctsCodeMap);
					}
					loggerBuffer.append("\n Batch commit failed：" + fileN);
					return DataBaseAction.BATCH_ERROR;
				}
			} // end if
			else {
				loggerBuffer.append("\n Database connection error");
				return DataBaseAction.CONNECTION_ERROR;
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n Database connection close error：" + e.getMessage());
			return DataBaseAction.CONNECTION_ERROR;
		} finally {
			try {
				if (pStatement != null)
					pStatement.close();
			} catch (SQLException e) {
				loggerBuffer.append("\n Database connection close error：" + e.getMessage());
			}
		}
	}

	/**
	 * @param loggerBuffer 
	 * @param ctsCodeMap 
	 * @Title: insertOneLine_db_GRASS04 @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param agme_Grass_04 @param connection @param
	 * recv_time @param listDi @param fileN 返回值说明 @throws
	 */
	public static void insertOneLine_db_GRASS04(Agme_Grass_04 agme_Grass_04, java.sql.Connection connection,
			Date recv_time, List<StatDi> listDi, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		Calendar calendar = Calendar.getInstance();
		// chy 去掉 D_RECORD_ID
		String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+" (D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V01301,V01300,"
				+ "V05001,V06001,V07001,V07031,V_ACODE,V04001,V04002,V04003,V71009,V71907,V71908,V71909,D_SOURCE_ID)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		if (connection != null) {
			try {
				connection.setAutoCommit(true);
				// pStatement = connection.prepareStatement(sql);
				pStatement = new LoggableStatement(connection, sql);
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				StatDi di = new StatDi();
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(ctsCodeMap.getSod_code());
				di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
				di.setTT(v_bbb);
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); // 1成功，0失败
				di.setPROCESS_STATE("01"); // 1成功，0失败

				int num = 1;
				Date obsTime = agme_Grass_04.getObservationTime();
				// D_DATETIME, V01301, V71009
				int intCoverDegree = doubleParsInt(agme_Grass_04.getCoverDegree());
				String agme_grass04_chn_tab_pk = sdf.format(obsTime) + "_" + agme_Grass_04.getStationNumberChina() + "_"
						+ intCoverDegree;
				pStatement.setString(num++, agme_grass04_chn_tab_pk);
				pStatement.setString(num++, ctsCodeMap.getSod_code()); // 资料标识，由配置文件配置
				pStatement.setTimestamp(num++, new Timestamp(new Date().getTime())); // 入库时间
				pStatement.setTimestamp(num++, new Timestamp(recv_time.getTime())); // 收到时间
				pStatement.setTimestamp(num++, new Timestamp(new Date().getTime())); // 更新时间

				calendar.setTime(obsTime);

				pStatement.setTimestamp(num++, new Timestamp(obsTime.getTime())); // 资料时间
				pStatement.setString(num++, agme_Grass_04.getStationNumberChina()); // 区站号（字符）
				pStatement.setString(num++, StationCodeUtil.stringToAscii(agme_Grass_04.getStationNumberChina())); // 区站号（数字）
				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_04.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 纬度
				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_04.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 经度
				pStatement.setBigDecimal(num++,
						new BigDecimal(String.valueOf(agme_Grass_04.getHeightOfSationGroundAboveMeanSeaLevel()))); // 测站海拔高度
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_04.getHeightOfBarometerAboveMeanSeaLevel())));
				// 气压传感器海拔高度 // 中国行政区划代码

				pStatement.setString(num++, getAdminCode(agme_Grass_04.getStationNumberChina(), loggerBuffer));
				pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.YEAR))); // 资料观测年
				pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.MONTH) + 1)); // 资料观测月
				pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.DAY_OF_MONTH))); // 资料观测日

				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_04.getCoverDegree())));// 覆盖度
				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_04.getEvaluaGrassCondition()));// 草层状况评价
				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_04.getFeedingDegree()));// 采食度
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_04.getFeedingRate())));// 采食率
				pStatement.setString(num++, ctsCodeMap.getCts_code());
				
				di.setIIiii(agme_Grass_04.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());
				di.setLATITUDE(agme_Grass_04.getLatitude().toString());
				di.setLONGTITUDE(agme_Grass_04.getLongitude().toString());
				di.setDATA_UPDATE_FLAG(v_bbb);
				di.setHEIGHT(agme_Grass_04.getHeightOfSationGroundAboveMeanSeaLevel().toString());
				di.setSEND_PHYS("DRDS");
				di.setFILE_SIZE(String.valueOf(file.length()));
				try {
					pStatement.execute();
					listDi.add(di);
				} catch (SQLException e) {
					di.setPROCESS_STATE("0");// 1成功，0失败
					listDi.add(di);
					loggerBuffer.append("\n filename：" + fileN + "\n " + agme_Grass_04.getStationNumberChina() + " "
							+ sdf.format(agme_Grass_04.getObservationTime()) + "\n execute sql error："
							+ ((LoggableStatement) pStatement).getQueryString() + "\n " + e.getMessage());
				}
			} catch (SQLException e) {
				loggerBuffer.append("\n create Statement error" + e.getMessage());
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
	}

	/**
	 * @param loggerBuffer 
	 * @param ctsCodeMap 
	 * @Title: insert_db_GRASS05 @Description: TODO(这里用一句话描述这个方法的作用) @param
	 * list @param connection @param recv_time @param listDi @param
	 * fileN @return 返回值说明 @throws
	 */
	public static DataBaseAction insert_db_GRASS05(List<Agme_Grass_05> list, java.sql.Connection connection,
			Date recv_time, List<StatDi> listDi, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		int sz = listDi.size();
		try {
			// chy 去掉 D_RECORD_ID
			String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+" (D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V01301,V01300,"
					+ "V05001,V06001,V07001,V07031,V_ACODE,V04001,V04002,V04003,V71501,V71910,V71911,D_SOURCE_ID)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			if (connection != null) {
				connection.setAutoCommit(false);
				pStatement = connection.prepareStatement(sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				// 用于获取时间
				Calendar calendar = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				for (int i = 0; i < list.size(); i++) {

					StatDi di = new StatDi();
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMap.getSod_code());
					di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
					di.setTT("");
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1"); // 1成功，0失败

					Agme_Grass_05 agme_Grass_05 = list.get(i);

					int num = 1;
					Date obsTime = agme_Grass_05.getObservationTime();
					// D_DATETIME, V01301, V71501
					String agme_grass05_chn_tab_pk = sdf.format(obsTime) + "_" + agme_Grass_05.getStationNumberChina()
							+ "_" + agme_Grass_05.getHerbageName();
					pStatement.setString(num++, agme_grass05_chn_tab_pk); // 记录标识
					pStatement.setString(num++, ctsCodeMap.getSod_code()); // 资料标识，由配置文件配置
					pStatement.setTimestamp(num++, new Timestamp(new Date().getTime())); // 入库时间
					pStatement.setTimestamp(num++, new Timestamp(recv_time.getTime())); // 收到时间
					pStatement.setTimestamp(num++, new Timestamp(new Date().getTime())); // 更新时间
					calendar.setTime(obsTime);

					pStatement.setTimestamp(num++, new Timestamp(obsTime.getTime())); // 资料时间
					pStatement.setString(num++, agme_Grass_05.getStationNumberChina()); // 区站号（字符）
					pStatement.setString(num++, StationCodeUtil.stringToAscii(agme_Grass_05.getStationNumberChina())); // 区站号（数字）
					pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_05.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 纬度
					pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_05.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 经度
					pStatement.setBigDecimal(num++,
							new BigDecimal(String.valueOf(agme_Grass_05.getHeightOfSationGroundAboveMeanSeaLevel()))); // 测站海拔高度
					pStatement.setBigDecimal(num++,
							new BigDecimal(String.valueOf(agme_Grass_05.getHeightOfBarometerAboveMeanSeaLevel())));
					// 气压传感器海拔高度 // 中国行政区划代码

					pStatement.setString(num++, getAdminCode(agme_Grass_05.getStationNumberChina(), loggerBuffer));
					pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.YEAR))); // 资料观测年
					pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.MONTH) + 1)); // 资料观测月
					pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.DAY_OF_MONTH))); // 资料观测日

					pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_05.getHerbageName()));// 牧草名称
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_05.getNumPlantPerHectare())));// 每公顷株丛数
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_05.getTotalNumPlantPerHectare())));// 每公顷总株丛数
					pStatement.setString(num++, ctsCodeMap.getCts_code());
					
					di.setIIiii(list.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLATITUDE(list.get(i).getLatitude().toString());
					di.setLONGTITUDE(list.get(i).getLongitude().toString());
					di.setDATA_UPDATE_FLAG(v_bbb);
					di.setHEIGHT(list.get(i).getHeightOfSationGroundAboveMeanSeaLevel().toString());
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(file.length()));
					pStatement.addBatch();
					listDi.add(di);
				} // for end
				try {
					pStatement.executeBatch();
					connection.commit();
					return DataBaseAction.SUCCESS;
				} catch (Exception e) {
					pStatement.clearParameters();
					pStatement.clearBatch();
					for (int i = 0; i < list.size(); i++) {
						listDi.remove(sz);
					}
					for (int i = 0; i < list.size(); i++) {
						insertOneLine_db_GRASS05(list.get(i), connection, recv_time, listDi, filepath,loggerBuffer,ctsCodeMap);
					}
					loggerBuffer.append("\n Batch commit failed：" + fileN);
					return DataBaseAction.BATCH_ERROR;
				}
			} // end if
			else {
				loggerBuffer.append("\n Database connection error");
				return DataBaseAction.CONNECTION_ERROR;
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n Database connection close error：" + e.getMessage());
			return DataBaseAction.CONNECTION_ERROR;
		} finally {
			try {
				if (pStatement != null)
					pStatement.close();
			} catch (SQLException e) {
				loggerBuffer.append("\n Database connection close error：" + e.getMessage());
			}
		}
	}

	/**
	 * @param loggerBuffer 
	 * @param ctsCodeMap 
	 * @Title: insertOneLine_db_GRASS05 @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param agme_Grass_05 @param connection @param
	 * recv_time @param listDi @param fileN 返回值说明 @throws
	 */
	public static void insertOneLine_db_GRASS05(Agme_Grass_05 agme_Grass_05, java.sql.Connection connection,
			Date recv_time, List<StatDi> listDi, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		Calendar calendar = Calendar.getInstance();
		// chy 去掉 D_RECORD_ID
		String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+" (D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V01301,V01300,"
				+ "V05001,V06001,V07001,V07031,V_ACODE,V04001,V04002,V04003,V71501,V71910,V71911,D_SOURCE_ID)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		if (connection != null) {
			try {
				connection.setAutoCommit(true);
				// pStatement = connection.prepareStatement(sql);
				pStatement = new LoggableStatement(connection, sql);
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				StatDi di = new StatDi();
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(ctsCodeMap.getSod_code());
				di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
				di.setTT(v_bbb);
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); // 1成功，0失败
				di.setPROCESS_STATE("1"); // 1成功，0失败

				int num = 1;
				Date obsTime = agme_Grass_05.getObservationTime();
				// D_DATETIME, V01301, V71501
				String agme_grass05_chn_tab_pk = sdf.format(obsTime) + "_" + agme_Grass_05.getStationNumberChina() + "_"
						+ agme_Grass_05.getHerbageName();
				pStatement.setString(num++, agme_grass05_chn_tab_pk); // 记录标识
				pStatement.setString(num++, ctsCodeMap.getSod_code()); // 资料标识，由配置文件配置
				pStatement.setTimestamp(num++, new Timestamp(new Date().getTime())); // 入库时间
				pStatement.setTimestamp(num++, new Timestamp(recv_time.getTime())); // 收到时间
				pStatement.setTimestamp(num++, new Timestamp(new Date().getTime())); // 更新时间
				calendar.setTime(obsTime);

				pStatement.setTimestamp(num++, new Timestamp(obsTime.getTime())); // 资料时间
				pStatement.setString(num++, agme_Grass_05.getStationNumberChina()); // 区站号（字符）
				pStatement.setString(num++, StationCodeUtil.stringToAscii(agme_Grass_05.getStationNumberChina())); // 区站号（数字）
				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_05.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 纬度
				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_05.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 经度
				pStatement.setBigDecimal(num++,
						new BigDecimal(String.valueOf(agme_Grass_05.getHeightOfSationGroundAboveMeanSeaLevel()))); // 测站海拔高度
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_05.getHeightOfBarometerAboveMeanSeaLevel())));
				
				// 气压传感器海拔高度 // 中国行政区划代码
				pStatement.setString(num++, getAdminCode(agme_Grass_05.getStationNumberChina(), loggerBuffer));
				pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.YEAR))); // 资料观测年
				pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.MONTH) + 1)); // 资料观测月
				pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.DAY_OF_MONTH))); // 资料观测日

				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_05.getHerbageName()));// 牧草名称
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_05.getNumPlantPerHectare())));// 每公顷株丛数
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_05.getTotalNumPlantPerHectare())));// 每公顷总株丛数
				pStatement.setString(num++, ctsCodeMap.getCts_code());
				
				di.setIIiii(agme_Grass_05.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());
				di.setLATITUDE(agme_Grass_05.getLatitude().toString());
				di.setLONGTITUDE(agme_Grass_05.getLongitude().toString());
				di.setDATA_UPDATE_FLAG(v_bbb);
				di.setHEIGHT(agme_Grass_05.getHeightOfSationGroundAboveMeanSeaLevel().toString());
				di.setSEND_PHYS("DRDS");
				di.setFILE_SIZE(String.valueOf(file.length()));
				try {
					pStatement.execute();
					listDi.add(di);
				} catch (SQLException e) {
					di.setPROCESS_STATE("0");// 1成功，0失败
					listDi.add(di);
					loggerBuffer.append("\n filename：" + fileN + "\n " + agme_Grass_05.getStationNumberChina() + " "
							+ sdf.format(agme_Grass_05.getObservationTime()) + "\n execute sql error："
							+ ((LoggableStatement) pStatement).getQueryString() + "\n " + e.getMessage());
				}
			} catch (SQLException e) {
				loggerBuffer.append("\n create Statement error" + e.getMessage());
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
	}

	/**
	 * @param loggerBuffer 
	 * @param ctsCodeMap 
	 * @Title: insert_db_GRASS06 @Description: TODO(这里用一句话描述这个方法的作用) @param
	 * list @param connection @param recv_time @param listDi @param
	 * fileN @return 返回值说明 @throws
	 */
	public static DataBaseAction insert_db_GRASS06(List<Agme_Grass_06> list, java.sql.Connection connection,
			Date recv_time, List<StatDi> listDi, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		int sz = listDi.size();
		try {
			// chy 去掉 D_record_id
			String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+" (D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V01301,V01300,"
					+ "V05001,V06001,V07001,V07031,V_ACODE,V04001,V04002,V04003,V71920,V71921,V71922,D_SOURCE_ID)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			if (connection != null) {
				connection.setAutoCommit(false);
				pStatement = connection.prepareStatement(sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				// 用于获取时间
				Calendar calendar = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				for (int i = 0; i < list.size(); i++) {

					StatDi di = new StatDi();
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMap.getSod_code());
					di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
					di.setTT("");
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); // 1成功，0失败
					di.setPROCESS_STATE("1"); // 1成功，0失败

					Agme_Grass_06 agme_Grass_06 = list.get(i);

					int num = 1;
					Date obsTime = agme_Grass_06.getObservationTime();
					// D_DATETIME, V01301, V71501
					String agme_grass06_chn_tab_pk = sdf.format(obsTime) + "_" + agme_Grass_06.getStationNumberChina()
							+ "_" + agme_Grass_06.getRankCondition();
					pStatement.setString(num++, agme_grass06_chn_tab_pk); // 记录标识
					pStatement.setString(num++, ctsCodeMap.getSod_code()); // 资料标识，由配置文件配置
					pStatement.setTimestamp(num++, new Timestamp(new Date().getTime())); // 入库时间
					pStatement.setTimestamp(num++, new Timestamp(recv_time.getTime())); // 收到时间
					pStatement.setTimestamp(num++, new Timestamp(new Date().getTime())); // 更新时间
					calendar.setTime(obsTime);

					pStatement.setTimestamp(num++, new Timestamp(obsTime.getTime())); // 资料时间
					pStatement.setString(num++, agme_Grass_06.getStationNumberChina()); // 区站号（字符）
					pStatement.setString(num++, StationCodeUtil.stringToAscii(agme_Grass_06.getStationNumberChina())); // 区站号（数字）
					pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_06.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 纬度
					pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_06.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 经度
					pStatement.setBigDecimal(num++,
							new BigDecimal(String.valueOf(agme_Grass_06.getHeightOfSationGroundAboveMeanSeaLevel()))); // 测站海拔高度
					pStatement.setBigDecimal(num++,
							new BigDecimal(String.valueOf(agme_Grass_06.getHeightOfBarometerAboveMeanSeaLevel())));
					// 气压传感器海拔高度 // 中国行政区划代码

					pStatement.setString(num++, getAdminCode(agme_Grass_06.getStationNumberChina(), loggerBuffer));
					pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.YEAR))); // 资料观测年
					pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.MONTH) + 1)); // 资料观测月
					pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.DAY_OF_MONTH))); // 资料观测日

					pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_06.getRankCondition()));// 膘情等级
					pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_06.getAdultAnimalNum()));// 成畜头数
					pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_06.getBabyHeadNum()));// 幼畜头数
					pStatement.setString(num++, ctsCodeMap.getCts_code());
					
					di.setIIiii(list.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLATITUDE(list.get(i).getLatitude().toString());
					di.setLONGTITUDE(list.get(i).getLongitude().toString());
					di.setDATA_UPDATE_FLAG(v_bbb);
					di.setHEIGHT(list.get(i).getHeightOfSationGroundAboveMeanSeaLevel().toString());
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(file.length()));
					pStatement.addBatch();
					listDi.add(di);
				} // for end
				try {
					pStatement.executeBatch();
					connection.commit();
					return DataBaseAction.SUCCESS;
				} catch (Exception e) {
					pStatement.clearParameters();
					pStatement.clearBatch();
					for (int i = 0; i < list.size(); i++) {
						listDi.remove(sz);
					}
					for (int i = 0; i < list.size(); i++) {
						insertOneLine_db_GRASS06(list.get(i), connection, recv_time, listDi, filepath,loggerBuffer,ctsCodeMap);
					}
					loggerBuffer.append("\n Batch commit failed：" + fileN);
					return DataBaseAction.BATCH_ERROR;
				}
			} // end if
			else {
				loggerBuffer.append("\n Database connection  error");
				return DataBaseAction.CONNECTION_ERROR;
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n Database connection close error：" + e.getMessage());
			return DataBaseAction.CONNECTION_ERROR;
		} finally {
			try {
				if (pStatement != null)
					pStatement.close();
			} catch (SQLException e) {
				loggerBuffer.append("\n Database connection close error：" + e.getMessage());
			}
		}
	}

	/**
	 * @param loggerBuffer 
	 * @param ctsCodeMap 
	 * @Title: insertOneLine_db_GRASS06 @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param agme_Grass_06 @param connection @param
	 * recv_time @param listDi @param fileN 返回值说明 @throws
	 */
	public static void insertOneLine_db_GRASS06(Agme_Grass_06 agme_Grass_06, java.sql.Connection connection,
			Date recv_time, List<StatDi> listDi, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		Calendar calendar = Calendar.getInstance();
		//chy 去掉 D_record_id
		String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+" (D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V01301,V01300,V05001,"
				+ "V06001,V07001,V07031,V_ACODE,V04001,V04002,V04003,V71920,V71921,V71922,D_SOURCE_ID)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		if (connection != null) {
			try {
				connection.setAutoCommit(true);
				// pStatement = connection.prepareStatement(sql);
				pStatement = new LoggableStatement(connection, sql);
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				StatDi di = new StatDi();
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(ctsCodeMap.getSod_code());
				di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
				di.setTT(v_bbb);
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); // 1成功，0失败
				di.setPROCESS_STATE("1"); // 1成功，0失败
				int num = 1;
				Date obsTime = agme_Grass_06.getObservationTime();
				// D_DATETIME, V01301, V71501
				String agme_grass06_chn_tab_pk = sdf.format(obsTime) + "_" + agme_Grass_06.getStationNumberChina() + "_"
						+ agme_Grass_06.getRankCondition();
				pStatement.setString(num++, agme_grass06_chn_tab_pk); // 记录标识
				pStatement.setString(num++, ctsCodeMap.getSod_code()); // 资料标识，由配置文件配置
				pStatement.setTimestamp(num++, new Timestamp(new Date().getTime())); // 入库时间
				pStatement.setTimestamp(num++, new Timestamp(recv_time.getTime())); // 收到时间
				pStatement.setTimestamp(num++, new Timestamp(new Date().getTime())); // 更新时间
				calendar.setTime(obsTime);

				pStatement.setTimestamp(num++, new Timestamp(obsTime.getTime())); // 资料时间
				pStatement.setString(num++, agme_Grass_06.getStationNumberChina()); // 区站号（字符）
				pStatement.setString(num++, StationCodeUtil.stringToAscii(agme_Grass_06.getStationNumberChina())); // 区站号（数字）
				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_06.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 纬度
				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_06.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 经度
				pStatement.setBigDecimal(num++,
						new BigDecimal(String.valueOf(agme_Grass_06.getHeightOfSationGroundAboveMeanSeaLevel()))); // 测站海拔高度
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_06.getHeightOfBarometerAboveMeanSeaLevel())));
				// 气压传感器海拔高度 // 中国行政区划代码

				pStatement.setString(num++, getAdminCode(agme_Grass_06.getStationNumberChina(), loggerBuffer));
				pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.YEAR))); // 资料观测年
				pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.MONTH) + 1)); // 资料观测月
				pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.DAY_OF_MONTH))); // 资料观测日

				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_06.getRankCondition()));// 膘情等级
				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_06.getAdultAnimalNum()));// 成畜头数
				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_06.getBabyHeadNum()));// 幼畜头数
				pStatement.setString(num++, ctsCodeMap.getCts_code());
					
				di.setIIiii(agme_Grass_06.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());
				di.setLATITUDE(agme_Grass_06.getLatitude().toString());
				di.setLONGTITUDE(agme_Grass_06.getLongitude().toString());
				di.setDATA_UPDATE_FLAG(v_bbb);
				di.setHEIGHT(agme_Grass_06.getHeightOfSationGroundAboveMeanSeaLevel().toString());
				di.setSEND_PHYS("DRDS");
				di.setFILE_SIZE(String.valueOf(file.length()));
				try {
					pStatement.execute();
					listDi.add(di);
				} catch (SQLException e) {
					di.setPROCESS_STATE("0");// 1成功，0失败
					listDi.add(di);
					loggerBuffer.append("\n filename：" + fileN + "\n " + agme_Grass_06.getStationNumberChina() + " "
							+ sdf.format(agme_Grass_06.getObservationTime()) + "\n execute sql error："
							+ ((LoggableStatement) pStatement).getQueryString() + "\n " + e.getMessage());
				}
			} catch (SQLException e) {
				loggerBuffer.append("\n create Statement error" + e.getMessage());
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
	}

	/**
	 * @param loggerBuffer 
	 * @param ctsCodeMap 
	 * @Title: insert_db_GRASS07 @Description: TODO(这里用一句话描述这个方法的作用) @param
	 * list @param connection @param recv_time @param listDi @param
	 * fileN @return 返回值说明 @throws
	 */
	public static DataBaseAction insert_db_GRASS07(List<Agme_Grass_07> list, java.sql.Connection connection,
			Date recv_time, List<StatDi> listDi, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		int sz = listDi.size();
		try {
			//chy 去掉 D_record_id
			String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+" (D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V01301,V01300,"
					+ "V05001,V06001,V07001,V07031,V_ACODE,V04001,V04002,V04003,WEIGHT_01,WEIGHT_02,WEIGHT_03,WEIGHT_04,WEIGHT_05,V_WEIGHT_AVG,D_SOURCE_ID)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			if (connection != null) {
				connection.setAutoCommit(false);
				pStatement = connection.prepareStatement(sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				// 用于获取时间
				Calendar calendar = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				for (int i = 0; i < list.size(); i++) {

					StatDi di = new StatDi();
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMap.getSod_code());
					di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
					di.setTT("");
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); // 1成功，0失败
					di.setPROCESS_STATE("1"); // 1成功，0失败

					Agme_Grass_07 agme_Grass_07 = list.get(i);

					int num = 1;
					// 更新时间不入
					Date obsTime = agme_Grass_07.getObservationTime();
					// D_DATETIME, V01301, WEIGHT_01, WEIGHT_02, WEIGHT_03,
					// WEIGHT_04, WEIGHT_05
					int intRamWeight_1 = doubleParsInt(agme_Grass_07.getRamWeight_1());
					int intRamWeight_2 = doubleParsInt(agme_Grass_07.getRamWeight_2());
					int intRamWeight_3 = doubleParsInt(agme_Grass_07.getRamWeight_3());
					int intRamWeight_4 = doubleParsInt(agme_Grass_07.getRamWeight_4());
					int intRamWeight_5 = doubleParsInt(agme_Grass_07.getRamWeight_5());
					String agme_grass07_chn_tab_pk = sdf.format(obsTime) + "_" + agme_Grass_07.getStationNumberChina()
							+ "_" + intRamWeight_1 + "_" + intRamWeight_2 + "_" + intRamWeight_3 + "_" + intRamWeight_4
							+ "_" + intRamWeight_5;

					pStatement.setString(num++, agme_grass07_chn_tab_pk); // 记录标识
					pStatement.setString(num++, ctsCodeMap.getSod_code()); // 资料标识，由配置文件配置
					pStatement.setTimestamp(num++, new Timestamp(new Date().getTime())); // 入库时间
					pStatement.setTimestamp(num++, new Timestamp(recv_time.getTime())); // 收到时间
					pStatement.setTimestamp(num++, new Timestamp(new Date().getTime())); // 更新时间
					calendar.setTime(obsTime);

					pStatement.setTimestamp(num++, new Timestamp(obsTime.getTime())); // 资料时间
					pStatement.setString(num++, agme_Grass_07.getStationNumberChina()); // 区站号（字符）
					pStatement.setString(num++, StationCodeUtil.stringToAscii(agme_Grass_07.getStationNumberChina())); // 区站号（数字）
					pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_07.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 纬度
					pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_07.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 经度
					pStatement.setBigDecimal(num++,
							new BigDecimal(String.valueOf(agme_Grass_07.getHeightOfSationGroundAboveMeanSeaLevel()))); // 测站海拔高度
					pStatement.setBigDecimal(num++,
							new BigDecimal(String.valueOf(agme_Grass_07.getHeightOfBarometerAboveMeanSeaLevel())));
					// 气压传感器海拔高度 // 中国行政区划代码

					pStatement.setString(num++, getAdminCode(agme_Grass_07.getStationNumberChina(), loggerBuffer));
					pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.YEAR))); // 资料观测年
					pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.MONTH) + 1)); // 资料观测月
					pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.DAY_OF_MONTH))); // 资料观测日

					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_07.getRamWeight_1())));// 羯羊_1体重
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_07.getRamWeight_2())));// 羯羊_2体重
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_07.getRamWeight_3())));// 羯羊_3体重
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_07.getRamWeight_4())));// 羯羊_4体重
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_07.getRamWeight_5())));// 羯羊_5体重
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_07.getAvgWeight())));// 平均
					pStatement.setString(num++, ctsCodeMap.getCts_code());
					
					di.setIIiii(list.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLATITUDE(list.get(i).getLatitude().toString());
					di.setLONGTITUDE(list.get(i).getLongitude().toString());
					di.setDATA_UPDATE_FLAG(v_bbb);
					di.setHEIGHT(list.get(i).getHeightOfSationGroundAboveMeanSeaLevel().toString());
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(file.length()));
					pStatement.addBatch();
					listDi.add(di);
				} // for end
				try {
					pStatement.executeBatch();
					connection.commit();
					return DataBaseAction.SUCCESS;
				} catch (Exception e) {
					pStatement.clearParameters();
					pStatement.clearBatch();
					for (int i = 0; i < list.size(); i++) {
						listDi.remove(sz);
					}
					for (int i = 0; i < list.size(); i++) {
						insertOneLine_db_GRASS07(list.get(i), connection, recv_time, listDi, filepath,loggerBuffer,ctsCodeMap);
					}
					loggerBuffer.append("\n Batch commit failed：" + fileN);
					return DataBaseAction.BATCH_ERROR;
				}
			} // end if
			else {
				loggerBuffer.append("\n Database connection error");
				return DataBaseAction.CONNECTION_ERROR;
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n Database connection close error：" + e.getMessage());
			return DataBaseAction.CONNECTION_ERROR;
		} finally {
			try {
				if (pStatement != null)
					pStatement.close();
			} catch (SQLException e) {
				loggerBuffer.append("\n Database connection close error：" + e.getMessage());
			}
		}
	}

	/**
	 * @param loggerBuffer 
	 * @param ctsCodeMap 
	 * @Title: insertOneLine_db_GRASS07 @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param agme_Grass_07 @param connection @param
	 * recv_time @param listDi @param fileN 返回值说明 @throws
	 */
	public static void insertOneLine_db_GRASS07(Agme_Grass_07 agme_Grass_07, java.sql.Connection connection,
			Date recv_time, List<StatDi> listDi, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		Calendar calendar = Calendar.getInstance();
		//chy 去掉 D_record_id
		String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+" (D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V01301,V01300,"
				+ "V05001,V06001,V07001,V07031,V_ACODE,V04001,V04002,V04003,WEIGHT_01,WEIGHT_02,WEIGHT_03,WEIGHT_04,WEIGHT_05,V_WEIGHT_AVG,D_SOURCE_ID)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		if (connection != null) {
			try {
				connection.setAutoCommit(true);
				// pStatement = connection.prepareStatement(sql);
				pStatement = new LoggableStatement(connection, sql);
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				StatDi di = new StatDi();
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(ctsCodeMap.getSod_code());
				di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
				;
				di.setTT(v_bbb);
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); // 1成功，0失败
				di.setPROCESS_STATE("1"); // 1成功，0失败
				int num = 1;
				// 更新时间不入
				Date obsTime = agme_Grass_07.getObservationTime();
				// D_DATETIME, V01301, WEIGHT_01, WEIGHT_02, WEIGHT_03,
				// WEIGHT_04, WEIGHT_05
				int intRamWeight_1 = doubleParsInt(agme_Grass_07.getRamWeight_1());
				int intRamWeight_2 = doubleParsInt(agme_Grass_07.getRamWeight_2());
				int intRamWeight_3 = doubleParsInt(agme_Grass_07.getRamWeight_3());
				int intRamWeight_4 = doubleParsInt(agme_Grass_07.getRamWeight_4());
				int intRamWeight_5 = doubleParsInt(agme_Grass_07.getRamWeight_5());
				String agme_grass07_chn_tab_pk = sdf.format(obsTime) + "_" + agme_Grass_07.getStationNumberChina() + "_"
						+ intRamWeight_1 + "_" + intRamWeight_2 + "_" + intRamWeight_3 + "_" + intRamWeight_4 + "_"
						+ intRamWeight_5;

				pStatement.setString(num++, agme_grass07_chn_tab_pk); // 记录标识
				pStatement.setString(num++, ctsCodeMap.getSod_code()); // 资料标识，由配置文件配置
				pStatement.setTimestamp(num++, new Timestamp(new Date().getTime())); // 入库时间
				pStatement.setTimestamp(num++, new Timestamp(recv_time.getTime())); // 收到时间
				pStatement.setTimestamp(num++, new Timestamp(new Date().getTime())); // 更新时间
				calendar.setTime(obsTime);

				pStatement.setTimestamp(num++, new Timestamp(obsTime.getTime())); // 资料时间
				pStatement.setString(num++, agme_Grass_07.getStationNumberChina()); // 区站号（字符）
				pStatement.setString(num++, StationCodeUtil.stringToAscii(agme_Grass_07.getStationNumberChina())); // 区站号（数字）
				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_07.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 纬度
				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_07.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 经度
				pStatement.setBigDecimal(num++,
						new BigDecimal(String.valueOf(agme_Grass_07.getHeightOfSationGroundAboveMeanSeaLevel()))); // 测站海拔高度
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_07.getHeightOfBarometerAboveMeanSeaLevel())));
				// 气压传感器海拔高度 // 中国行政区划代码

				pStatement.setString(num++, getAdminCode(agme_Grass_07.getStationNumberChina(), loggerBuffer));
				pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.YEAR))); // 资料观测年
				pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.MONTH) + 1)); // 资料观测月
				pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.DAY_OF_MONTH))); // 资料观测日

				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_07.getRamWeight_1())));// 羯羊_1体重
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_07.getRamWeight_2())));// 羯羊_2体重
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_07.getRamWeight_3())));// 羯羊_3体重
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_07.getRamWeight_4())));// 羯羊_4体重
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_07.getRamWeight_5())));// 羯羊_5体重
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_07.getAvgWeight())));// 平均
				pStatement.setString(num++, ctsCodeMap.getCts_code());
					
				di.setIIiii(agme_Grass_07.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());
				di.setLATITUDE(agme_Grass_07.getLatitude().toString());
				di.setLONGTITUDE(agme_Grass_07.getLongitude().toString());
				di.setDATA_UPDATE_FLAG(v_bbb);
				di.setHEIGHT(agme_Grass_07.getHeightOfSationGroundAboveMeanSeaLevel().toString());
				di.setSEND_PHYS("DRDS");
				di.setFILE_SIZE(String.valueOf(file.length()));
				try {
					pStatement.execute();
					listDi.add(di);
				} catch (SQLException e) {
					di.setPROCESS_STATE("0");// 1成功，0失败
					listDi.add(di);
					loggerBuffer.append("\n filename：" + fileN + "\n " + agme_Grass_07.getStationNumberChina() + " "
							+ sdf.format(agme_Grass_07.getObservationTime()) + "\n execute sql error："
							+ ((LoggableStatement) pStatement).getQueryString() + "\n " + e.getMessage());
				}
			} catch (SQLException e) {
				loggerBuffer.append("\n create Statement error" + e.getMessage());
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
	}

	/**
	 * @param loggerBuffer 
	 * @param ctsCodeMap 
	 * @Title: insert_db_GRASS08 @Description: TODO(这里用一句话描述这个方法的作用) @param
	 * list @param connection @param recv_time @param listDi @param
	 * fileN @return 返回值说明 @throws
	 */
	public static DataBaseAction insert_db_GRASS08(List<Agme_Grass_08> list, java.sql.Connection connection,
			Date recv_time, List<StatDi> listDi, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		int sz = listDi.size();
		try {
			//chy 去掉 D_record_id
			String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+" (D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V01301,V01300,"
					+ "V05001,V06001,V07001,V07031,V_ACODE,V04001,V04002,V04003,V04004,V_AVG_GRAZE_01,V_AVG_GRAZE_02,V_AVG_GRAZE_03,V_AVG_GRAZE_04,"
					+ "V_HOVEL,V_HOVEL_COUNT,V_HOVEL_LEN,V_HOVEL_W,V_HOVEL_H,V_HOVEL_ST,V_HOVEL_TYPE,V_HOVEL_WD,V71501,V71601,V_ORGAN,D_SOURCE_ID)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			if (connection != null) {
				connection.setAutoCommit(false);
				pStatement = connection.prepareStatement(sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				// 用于获取时间
				Calendar calendar = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				for (int i = 0; i < list.size(); i++) {

					StatDi di = new StatDi();
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMap.getSod_code());
					di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
					di.setTT("");
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); // 1成功，0失败
					di.setPROCESS_STATE("1"); // 1成功，0失败

					Agme_Grass_08 agme_Grass_08 = list.get(i);

					int num = 1;
					Date obsTime = agme_Grass_08.getObservationTime();
					// D_DATETIME, V01301, V71501, V71601
					String agme_grass08_chn_tab_pk = sdf.format(obsTime) + "_" + agme_Grass_08.getStationNumberChina()
							+ "_" + agme_Grass_08.getLivestockName() + "_" + agme_Grass_08.getLivestockBreeds();
					pStatement.setString(num++, agme_grass08_chn_tab_pk); // 记录标识
					pStatement.setString(num++, ctsCodeMap.getSod_code()); // 资料标识，由配置文件配置
					pStatement.setTimestamp(num++, new Timestamp(new Date().getTime())); // 入库时间
					pStatement.setTimestamp(num++, new Timestamp(recv_time.getTime())); // 收到时间
					pStatement.setTimestamp(num++, new Timestamp(new Date().getTime()));// 更新时间
																						// //
																						// //更新时间不入，sql中的D_UPDATE_TIME去掉
					calendar.setTime(obsTime);

					pStatement.setTimestamp(num++, new Timestamp(obsTime.getTime())); // 资料时间
					pStatement.setString(num++, agme_Grass_08.getStationNumberChina()); // 区站号（字符）
					pStatement.setString(num++, StationCodeUtil.stringToAscii(agme_Grass_08.getStationNumberChina())); // 区站号（数字）
					pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_08.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 纬度
					pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_08.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 经度
					pStatement.setBigDecimal(num++,
							new BigDecimal(String.valueOf(agme_Grass_08.getHeightOfSationGroundAboveMeanSeaLevel()))); // 测站海拔高度
					pStatement.setBigDecimal(num++,
							new BigDecimal(String.valueOf(agme_Grass_08.getHeightOfBarometerAboveMeanSeaLevel())));
					// 气压传感器海拔高度 // 中国行政区划代码

					pStatement.setString(num++, getAdminCode(agme_Grass_08.getStationNumberChina(), loggerBuffer));
					pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.YEAR))); // 资料观测年
					pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.MONTH) + 1)); // 资料观测月
					pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.DAY_OF_MONTH))); // 资料观测日
					pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.HOUR))); // 资料观测时

					pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_08.getAvgSpringDailyGrazHours()));// 春季日平均放牧时数
					pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_08.getAvgSummerDailyGrazHours()));// 夏季日平均放牧时数
					pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_08.getAvgFallDailyGrazHours()));// 秋季日平均放牧时数
					pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_08.getAvgWinterDailyGrazHours()));// 冬季日平均放牧时数
					pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_08.getIsHaveSuccah()));// 有无棚舍
					pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_08.getSuccahNum()));// 棚舍数量
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_08.getSuccahLong())));// 棚舍长
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_08.getSuccahWide())));// 棚舍宽
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_08.getSuccahHigh())));// 棚舍高
					try {
						pStatement.setString(num++, new String(agme_Grass_08.getSuccahFrame().getBytes(), "UTF8"));
					} catch (UnsupportedEncodingException e) {
						pStatement.setString(num++, "999999");
						loggerBuffer.append("getSuccahFrame() format error: " + e.getMessage());
					}// 棚舍结构
					try {
						pStatement.setString(num++, new String(agme_Grass_08.getSuccahType().getBytes(), "UTF8"));
					} catch (UnsupportedEncodingException e) {
						pStatement.setString(num++, "999999");
						loggerBuffer.append("getSuccahType() format error: " + e.getMessage());
					}// 棚舍型式
					try {
						pStatement.setString(num++, new String(agme_Grass_08.getSuccahWinDirection().getBytes(), "UTF8"));
					} catch (UnsupportedEncodingException e) {
						pStatement.setString(num++, "999999");
						loggerBuffer.append("getSuccahWinDirection() format error: " + e.getMessage());
					}// 棚舍门窗开向
					try {
						pStatement.setString(num++, new String(agme_Grass_08.getLivestockName().getBytes(), "UTF8"));
					} catch (UnsupportedEncodingException e) {
						pStatement.setString(num++, "999999");
						loggerBuffer.append("getLivestockName() format error: " + e.getMessage());
					}// 畜群家畜名称
					try {
						pStatement.setString(num++, new String(agme_Grass_08.getLivestockBreeds().getBytes(), "UTF8"));
					} catch (UnsupportedEncodingException e) {
						pStatement.setString(num++, "999999");
						loggerBuffer.append("getLivestockBreeds() format error: " + e.getMessage());
					}// 家畜品种
					try {
						pStatement.setString(num++, new String(agme_Grass_08.getLivestockUnit().getBytes(), "UTF8"));
					} catch (UnsupportedEncodingException e) {
						pStatement.setString(num++, "999999");
						loggerBuffer.append("getLivestockUnit() format error: " + e.getMessage());
					}// 畜群所属单位
					pStatement.setString(num++, ctsCodeMap.getCts_code());
						
					di.setIIiii(list.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLATITUDE(list.get(i).getLatitude().toString());
					di.setLONGTITUDE(list.get(i).getLongitude().toString());
					di.setDATA_UPDATE_FLAG(v_bbb);
					di.setHEIGHT(list.get(i).getHeightOfSationGroundAboveMeanSeaLevel().toString());
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(file.length()));
					pStatement.addBatch();
					listDi.add(di);
				} // for end
				try {
					pStatement.executeBatch();
					connection.commit();
					return DataBaseAction.SUCCESS;
				} catch (Exception e) {
					pStatement.clearParameters();
					pStatement.clearBatch();
					for (int i = 0; i < list.size(); i++) {
						listDi.remove(sz);
					}
					for (int i = 0; i < list.size(); i++) {
						insertOneLine_db_GRASS08(list.get(i), connection, recv_time, listDi, filepath,loggerBuffer,ctsCodeMap);
					}
					loggerBuffer.append("\n Batch commit failed：" + fileN);
					return DataBaseAction.BATCH_ERROR;
				}
			} // end if
			else {
				loggerBuffer.append("\n Database connection error");
				return DataBaseAction.CONNECTION_ERROR;
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n Database connection close  error：" + e.getMessage());
			return DataBaseAction.CONNECTION_ERROR;
		} finally {
			try {
				if (pStatement != null)
					pStatement.close();
			} catch (SQLException e) {
				loggerBuffer.append("\n Database connection close error：" + e.getMessage());
			}
		}
	}

	/**
	 * @param loggerBuffer 
	 * @param ctsCodeMap 
	 * @Title: insertOneLine_db_GRASS08 @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param agme_Grass_08 @param connection @param
	 * recv_time @param listDi @param fileN 返回值说明 @throws
	 */
	public static void insertOneLine_db_GRASS08(Agme_Grass_08 agme_Grass_08, java.sql.Connection connection,
			Date recv_time, List<StatDi> listDi, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		Calendar calendar = Calendar.getInstance();
		//chy 去掉 D_record_id
		String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+" (D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V01301,V01300,V05001,"
				+ "V06001,V07001,V07031,V_ACODE,V04001,V04002,V04003,V04004,V_AVG_GRAZE_01,V_AVG_GRAZE_02,V_AVG_GRAZE_03,V_AVG_GRAZE_04,V_HOVEL,V_HOVEL_COUNT,"
				+ "V_HOVEL_LEN,V_HOVEL_W,V_HOVEL_H,V_HOVEL_ST,V_HOVEL_TYPE,V_HOVEL_WD,V71501,V71601,V_ORGAN,D_SOURCE_ID)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		if (connection != null) {
			try {
				connection.setAutoCommit(true);
				// pStatement = connection.prepareStatement(sql);
				pStatement = new LoggableStatement(connection, sql);
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				StatDi di = new StatDi();
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(ctsCodeMap.getSod_code());
				di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
				di.setTT(v_bbb);
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); // 1成功，0失败
				di.setPROCESS_STATE("1"); // 1成功，0失败
				int num = 1;

				Date obsTime = agme_Grass_08.getObservationTime();
				// D_DATETIME, V01301, V71501, V71601
				String agme_grass08_chn_tab_pk = sdf.format(obsTime) + "_" + agme_Grass_08.getStationNumberChina() + "_"
						+ agme_Grass_08.getLivestockName() + "_" + agme_Grass_08.getLivestockBreeds();
				pStatement.setString(num++, agme_grass08_chn_tab_pk); // 记录标识
				pStatement.setString(num++, ctsCodeMap.getSod_code()); // 资料标识，由配置文件配置
				pStatement.setTimestamp(num++, new Timestamp(new Date().getTime())); // 入库时间
				pStatement.setTimestamp(num++, new Timestamp(recv_time.getTime())); // 收到时间
				pStatement.setTimestamp(num++, new Timestamp(new Date().getTime()));// 更新时间
																					// //
																					// //更新时间不入，sql中的D_UPDATE_TIME去掉
				calendar.setTime(obsTime);

				pStatement.setTimestamp(num++, new Timestamp(obsTime.getTime())); // 资料时间
				pStatement.setString(num++, agme_Grass_08.getStationNumberChina()); // 区站号（字符）
				pStatement.setString(num++, StationCodeUtil.stringToAscii(agme_Grass_08.getStationNumberChina())); // 区站号（数字）
				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_08.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 纬度
				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_08.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 经度
				pStatement.setBigDecimal(num++,
						new BigDecimal(String.valueOf(agme_Grass_08.getHeightOfSationGroundAboveMeanSeaLevel()))); // 测站海拔高度
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_08.getHeightOfBarometerAboveMeanSeaLevel())));
				// 气压传感器海拔高度 // 中国行政区划代码

				pStatement.setString(num++, getAdminCode(agme_Grass_08.getStationNumberChina(), loggerBuffer));
				pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.YEAR))); // 资料观测年
				pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.MONTH) + 1)); // 资料观测月
				pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.DAY_OF_MONTH))); // 资料观测日
				pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.HOUR))); // 资料观测时

				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_08.getAvgSpringDailyGrazHours()));// 春季日平均放牧时数
				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_08.getAvgSummerDailyGrazHours()));// 夏季日平均放牧时数
				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_08.getAvgFallDailyGrazHours()));// 秋季日平均放牧时数
				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_08.getAvgWinterDailyGrazHours()));// 冬季日平均放牧时数
				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_08.getIsHaveSuccah()));// 有无棚舍
				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_08.getSuccahNum()));// 棚舍数量
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_08.getSuccahLong())));// 棚舍长
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_08.getSuccahWide())));// 棚舍宽
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_08.getSuccahHigh())));// 棚舍高
				
				try {
					pStatement.setString(num++, new String(agme_Grass_08.getSuccahFrame().getBytes(), "UTF8"));
				} catch (UnsupportedEncodingException e) {
					pStatement.setString(num++, "999999");
					loggerBuffer.append("getSuccahFrame() format error: " + e.getMessage());
				}// 棚舍结构
				try {
					pStatement.setString(num++, new String(agme_Grass_08.getSuccahType().getBytes(), "UTF8"));
				} catch (UnsupportedEncodingException e) {
					pStatement.setString(num++, "999999");
					loggerBuffer.append("getSuccahType() format error: " + e.getMessage());
				}// 棚舍型式
				try {
					pStatement.setString(num++, new String(agme_Grass_08.getSuccahWinDirection().getBytes(), "UTF8"));
				} catch (UnsupportedEncodingException e) {
					pStatement.setString(num++, "999999");
					loggerBuffer.append("getSuccahWinDirection() format error: " + e.getMessage());
				}// 棚舍门窗开向
				try {
					pStatement.setString(num++, new String(agme_Grass_08.getLivestockName().getBytes(), "UTF8"));
				} catch (UnsupportedEncodingException e) {
					pStatement.setString(num++, "999999");
					loggerBuffer.append("getLivestockName() format error: " + e.getMessage());
				}// 畜群家畜名称
				try {
					pStatement.setString(num++, new String(agme_Grass_08.getLivestockBreeds().getBytes(), "UTF8"));
				} catch (UnsupportedEncodingException e) {
					pStatement.setString(num++, "999999");
					loggerBuffer.append("getLivestockBreeds() format error: " + e.getMessage());
				}// 家畜品种
				try {
					pStatement.setString(num++, new String(agme_Grass_08.getLivestockUnit().getBytes(), "UTF8"));
				} catch (UnsupportedEncodingException e) {
					pStatement.setString(num++, "999999");
					loggerBuffer.append("getLivestockUnit() format error: " + e.getMessage());
				}// 畜群所属单位
				
				pStatement.setString(num++, ctsCodeMap.getCts_code());
				
				di.setIIiii(agme_Grass_08.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());
				di.setLATITUDE(agme_Grass_08.getLatitude().toString());
				di.setLONGTITUDE(agme_Grass_08.getLongitude().toString());
				di.setDATA_UPDATE_FLAG(v_bbb);
				di.setHEIGHT(agme_Grass_08.getHeightOfSationGroundAboveMeanSeaLevel().toString());
				di.setSEND_PHYS("DRDS");
				di.setFILE_SIZE(String.valueOf(file.length()));
				try {
					pStatement.execute();
					listDi.add(di);
				} catch (SQLException e) {
					di.setPROCESS_STATE("0");// 1成功，0失败
					listDi.add(di);
					loggerBuffer.append("\n filename：" + fileN + "\n " + agme_Grass_08.getStationNumberChina() + " "
							+ sdf.format(agme_Grass_08.getObservationTime()) + "\n execute sql error："
							+ ((LoggableStatement) pStatement).getQueryString() + "\n " + e.getMessage());
				}
			} catch (SQLException e) {
				loggerBuffer.append("\n create Statement error" + e.getMessage());
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
	}

	/**
	 * @param loggerBuffer 
	 * @param ctsCodeMap 
	 * @Title: insert_db_GRASS09 @Description: TODO(这里用一句话描述这个方法的作用) @param
	 * list @param connection @param recv_time @param listDi @param
	 * fileN @return 返回值说明 @throws
	 */
	public static DataBaseAction insert_db_GRASS09(List<Agme_Grass_09> list, java.sql.Connection connection,
			Date recv_time, List<StatDi> listDi, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		int sz = listDi.size();
		try {
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddhhmmss");
			//chy 去掉 D_record_id
			String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+" (D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V01301,V01300,"
					+ "V05001,V06001,V07001,V07031,V_ACODE,V04001,V04002,V04003,V04004,V04300_017,V04300_018,V71616_02,V_PROD_FUNC,D_SOURCE_ID)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			if (connection != null) {
				connection.setAutoCommit(false);
				pStatement = connection.prepareStatement(sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				// 用于获取时间
				Calendar calendar = Calendar.getInstance();
				for (int i = 0; i < list.size(); i++) {

					StatDi di = new StatDi();
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMap.getSod_code());
					di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
					;
					di.setTT("");
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); // 1成功，0失败
					di.setPROCESS_STATE("1"); // 1成功，0失败

					Agme_Grass_09 agme_Grass_09 = list.get(i);
					int num = 1;
					String agme_grass09_chn_tab_pk = agme_Grass_09.getObservationStarTime() + "_"
							+ agme_Grass_09.getStationNumberChina() + "_" + agme_Grass_09.getAnimalHusbandryName();
					pStatement.setString(num++, agme_grass09_chn_tab_pk); // 记录标识
					pStatement.setString(num++, ctsCodeMap.getSod_code()); // 资料标识，由配置文件配置
					pStatement.setTimestamp(num++, new Timestamp(new Date().getTime())); // 入库时间
					pStatement.setTimestamp(num++, new Timestamp(recv_time.getTime())); // 收到时间
					pStatement.setTimestamp(num++, new Timestamp(new Date().getTime()));// 更新时间
					Date obsTime = sdf1.parse(agme_Grass_09.getObservationStarTime());

					calendar.setTime(obsTime);
					pStatement.setTimestamp(num++, new Timestamp(obsTime.getTime())); // 资料时间
					pStatement.setString(num++, agme_Grass_09.getStationNumberChina()); // 区站号（字符）
					pStatement.setString(num++, StationCodeUtil.stringToAscii(agme_Grass_09.getStationNumberChina())); // 区站号（数字）
					pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_09.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 纬度
					pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_09.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 经度
					pStatement.setBigDecimal(num++,
							new BigDecimal(String.valueOf(agme_Grass_09.getHeightOfSationGroundAboveMeanSeaLevel()))); // 测站海拔高度
					pStatement.setBigDecimal(num++,
							new BigDecimal(String.valueOf(agme_Grass_09.getHeightOfBarometerAboveMeanSeaLevel())));
					// 气压传感器海拔高度 // 中国行政区划代码

					pStatement.setString(num++, getAdminCode(agme_Grass_09.getStationNumberChina(), loggerBuffer));
					pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.YEAR))); // 资料观测年
					pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.MONTH) + 1)); // 资料观测月
					pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.DAY_OF_MONTH))); // 资料观测日
					pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.HOUR))); // 资料观测时

					pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_09.getObservationStarTime()));// 调查起始时间
					pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_09.getObservationEndTime()));// 调查终止时间
					pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_09.getAnimalHusbandryName()));// 牧事活动名称
					try {
						pStatement.setString(num++, new String(agme_Grass_09.getProductPerformance().getBytes(), "UTF8"));
					} catch (UnsupportedEncodingException e) {
						pStatement.setString(num++, "999999");
						loggerBuffer.append("getProductPerformance() format error: " + e.getMessage());
					}// 生产性能
					pStatement.setString(num++, ctsCodeMap.getCts_code());
					
					di.setIIiii(list.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLATITUDE(list.get(i).getLatitude().toString());
					di.setLONGTITUDE(list.get(i).getLongitude().toString());
					di.setDATA_UPDATE_FLAG(v_bbb);
					di.setHEIGHT(list.get(i).getHeightOfSationGroundAboveMeanSeaLevel().toString());
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(file.length()));
					pStatement.addBatch();
					listDi.add(di);
				} // for end
				try {
					pStatement.executeBatch();
					connection.commit();
					return DataBaseAction.SUCCESS;
				} catch (Exception e) {
					pStatement.clearParameters();
					pStatement.clearBatch();
					for (int i = 0; i < list.size(); i++) {
						listDi.remove(sz);
					}
					for (int i = 0; i < list.size(); i++) {
						insertOneLine_db_GRASS09(list.get(i), connection, recv_time, listDi, filepath,loggerBuffer,ctsCodeMap);
					}
					loggerBuffer.append("\n Batch commit failed：" + fileN);
					return DataBaseAction.BATCH_ERROR;
				}
			} // end if
			else {
				loggerBuffer.append("\n Database connection error");
				return DataBaseAction.CONNECTION_ERROR;
			}
		} catch (ParseException e) {
			loggerBuffer.append("\n Date conversion exception");
			return DataBaseAction.CONNECTION_ERROR;
		} catch (SQLException e) {
			loggerBuffer.append("\n Database connection close  error：" + e.getMessage());
			return DataBaseAction.CONNECTION_ERROR;
		} finally {
			try {
				if (pStatement != null)
					pStatement.close();
			} catch (SQLException e) {
				loggerBuffer.append("\n Database connection close error：" + e.getMessage());
			}
		}
	}

	/**
	 * @param loggerBuffer 
	 * @param ctsCodeMap 
	 * @Title: insertOneLine_db_GRASS09 @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param agme_Grass_09 @param connection @param
	 * recv_time @param listDi @param fileN 返回值说明 @throws
	 */
	public static void insertOneLine_db_GRASS09(Agme_Grass_09 agme_Grass_09, java.sql.Connection connection,
			Date recv_time, List<StatDi> listDi, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddhhmmss");
		//chy 去掉 D_record_id
		String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+" (D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V01301,V01300,"
				+ "V05001,V06001,V07001,V07031,V_ACODE,V04001,V04002,V04003,V04004,V04300_017,V04300_018,V71616_02,V_PROD_FUNC,D_SOURCE_ID)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		if (connection != null) {
			try {
				connection.setAutoCommit(true);
				// pStatement = connection.prepareStatement(sql);
				pStatement = new LoggableStatement(connection, sql);
				
				StatDi di = new StatDi();
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(ctsCodeMap.getSod_code());
				di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
				di.setTT(v_bbb);
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); // 1成功，0失败
				di.setPROCESS_STATE("1"); // 1成功，0失败

				int num = 1;
				String agme_grass09_chn_tab_pk = agme_Grass_09.getObservationStarTime() + "_"
						+ agme_Grass_09.getStationNumberChina() + "_" + agme_Grass_09.getAnimalHusbandryName();
				pStatement.setString(num++, agme_grass09_chn_tab_pk); // 记录标识
				pStatement.setString(num++, ctsCodeMap.getSod_code()); // 资料标识，由配置文件配置
				pStatement.setTimestamp(num++, new Timestamp(new Date().getTime())); // 入库时间
				pStatement.setTimestamp(num++, new Timestamp(recv_time.getTime())); // 收到时间
				pStatement.setTimestamp(num++, new Timestamp(new Date().getTime()));// 更新时间
				Date obsTime = sdf1.parse(agme_Grass_09.getObservationStarTime());
				calendar.setTime(obsTime);
				pStatement.setTimestamp(num++, new Timestamp(obsTime.getTime())); // 资料时间
				pStatement.setString(num++, agme_Grass_09.getStationNumberChina()); // 区站号（字符）
				pStatement.setString(num++, StationCodeUtil.stringToAscii(agme_Grass_09.getStationNumberChina())); // 区站号（数字）
				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_09.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 纬度
				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_09.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 经度
				pStatement.setBigDecimal(num++,
						new BigDecimal(String.valueOf(agme_Grass_09.getHeightOfSationGroundAboveMeanSeaLevel()))); // 测站海拔高度
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_09.getHeightOfBarometerAboveMeanSeaLevel())));
				// 气压传感器海拔高度 // 中国行政区划代码

				pStatement.setString(num++, getAdminCode(agme_Grass_09.getStationNumberChina(), loggerBuffer));
				pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.YEAR))); // 资料观测年
				pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.MONTH) + 1)); // 资料观测月
				pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.DAY_OF_MONTH))); // 资料观测日
				pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.HOUR))); // 资料观测时

				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_09.getObservationStarTime()));// 调查起始时间
				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_09.getObservationEndTime()));// 调查终止时间
				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_09.getAnimalHusbandryName()));// 牧事活动名称
				try {
					pStatement.setString(num++, new String(agme_Grass_09.getProductPerformance().getBytes(), "UTF8"));
				} catch (UnsupportedEncodingException e) {
					pStatement.setString(num++, "999999");
					loggerBuffer.append("getProductPerformance() format error: " + e.getMessage());
				}// 生产性能
				pStatement.setString(num++, ctsCodeMap.getCts_code());
				
				di.setIIiii(agme_Grass_09.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());
				di.setLATITUDE(agme_Grass_09.getLatitude().toString());
				di.setLONGTITUDE(agme_Grass_09.getLongitude().toString());
				di.setDATA_UPDATE_FLAG(v_bbb);
				di.setHEIGHT(agme_Grass_09.getHeightOfSationGroundAboveMeanSeaLevel().toString());
				di.setSEND_PHYS("DRDS");
				di.setFILE_SIZE(String.valueOf(file.length()));
				try {
					pStatement.execute();
					listDi.add(di);
				} catch (SQLException e) {
					di.setPROCESS_STATE("0");// 1成功，0失败
					listDi.add(di);
					loggerBuffer.append("\n filename：" + fileN + "\n " + agme_Grass_09.getStationNumberChina() + " "
							+ TimeUtil.String2Date(agme_Grass_09.getObservationStarTime(), "yyyyMMddHHmmss")
							+ "\n execute sql error：" + ((LoggableStatement) pStatement).getQueryString() + "\n "
							+ e.getMessage());
				}
			} catch (ParseException e) {
				loggerBuffer.append("\n Date conversion exception" + e.getMessage());
			} catch (SQLException e) {
				loggerBuffer.append("\n create Statement error" + e.getMessage());
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
	}

	/**
	 * @param loggerBuffer 
	 * @param ctsCodeMap 
	 * @Title: insert_db_GRASS10 @Description: TODO(这里用一句话描述这个方法的作用) @param
	 * list @param connection @param recv_time @param listDi @param
	 * fileN @return 返回值说明 @throws
	 */
	public static DataBaseAction insert_db_GRASS10(List<Agme_Grass_10> list, java.sql.Connection connection,
			Date recv_time, List<StatDi> listDi, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		int sz = listDi.size();
		try {
			//chy 去掉 D_record_id
			String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+" (D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V01301,V01300,"
					+ "V05001,V06001,V07001,V07031,V_ACODE,V04001,V04002,V04003,V04004,V71912,V71116,V71913,D_SOURCE_ID)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			if (connection != null) {
				connection.setAutoCommit(false);
				pStatement = connection.prepareStatement(sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				// 用于获取时间
				Calendar calendar = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				for (int i = 0; i < list.size(); i++) {

					StatDi di = new StatDi();
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMap.getSod_code());
					di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
					di.setTT("");
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); // 1成功，0失败
					di.setPROCESS_STATE("1"); // 1成功，0失败

					Agme_Grass_10 agme_Grass_10 = list.get(i);
					int num = 1;
					Date obsTime = agme_Grass_10.getObservationTime();
					// D_DATETIME, V01301, V71912
					String agme_grass10_chn_tab_pk = sdf.format(obsTime) + "_" + agme_Grass_10.getStationNumberChina()
							+ "_" + agme_Grass_10.getGrassLayerType() + "_" + agme_Grass_10.getMeasurementSite();
					pStatement.setString(num++, agme_grass10_chn_tab_pk); // 记录标识
					pStatement.setString(num++, ctsCodeMap.getSod_code()); // 资料标识，由配置文件配置
					pStatement.setTimestamp(num++, new Timestamp(new Date().getTime())); // 入库时间
					pStatement.setTimestamp(num++, new Timestamp(recv_time.getTime())); // 收到时间
					pStatement.setTimestamp(num++, new Timestamp(new Date().getTime()));// 更新时间
					// Date obsTime = agme_Grass_10.getObservationTime();
					calendar.setTime(obsTime);

					pStatement.setTimestamp(num++, new Timestamp(obsTime.getTime())); // 资料时间
					pStatement.setString(num++, agme_Grass_10.getStationNumberChina()); // 区站号（字符）
					pStatement.setString(num++, StationCodeUtil.stringToAscii(agme_Grass_10.getStationNumberChina())); // 区站号（数字）
					pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_10.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 纬度
					pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_10.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 经度
					pStatement.setBigDecimal(num++,
							new BigDecimal(String.valueOf(agme_Grass_10.getHeightOfSationGroundAboveMeanSeaLevel()))); // 测站海拔高度
					pStatement.setBigDecimal(num++,
							new BigDecimal(String.valueOf(agme_Grass_10.getHeightOfBarometerAboveMeanSeaLevel())));
					// 气压传感器海拔高度 // 中国行政区划代码

					pStatement.setString(num++, getAdminCode(agme_Grass_10.getStationNumberChina(), loggerBuffer));
					pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.YEAR))); // 资料观测年
					pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.MONTH) + 1)); // 资料观测月
					pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.DAY_OF_MONTH))); // 资料观测日
					pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.HOUR))); // 资料观测时

					pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_10.getGrassLayerType()));// 草层类型
					pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_10.getMeasurementSite()));// 测量场地
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_10.getGrassLayerHeight())));// 草层高度
					pStatement.setString(num++, ctsCodeMap.getCts_code());
					
					di.setIIiii(list.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLATITUDE(list.get(i).getLatitude().toString());
					di.setLONGTITUDE(list.get(i).getLongitude().toString());
					di.setDATA_UPDATE_FLAG(v_bbb);
					di.setHEIGHT(list.get(i).getHeightOfSationGroundAboveMeanSeaLevel().toString());
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(file.length()));
					pStatement.addBatch();
					listDi.add(di);
				} // for end
				try {
					pStatement.executeBatch();
					connection.commit();
					return DataBaseAction.SUCCESS;
				} catch (Exception e) {
					pStatement.clearParameters();
					pStatement.clearBatch();

					for (int i = 0; i < list.size(); i++) {
						listDi.remove(sz);
					}
					for (int i = 0; i < list.size(); i++) {
						insertOneLine_db_GRASS10(list.get(i), connection, recv_time, listDi, filepath,loggerBuffer,ctsCodeMap);
					}
					loggerBuffer.append("\n Batch commit failed：" + fileN);
					return DataBaseAction.BATCH_ERROR;
				}
			} // end if
			else {
				loggerBuffer.append("\n Database connection error");
				return DataBaseAction.CONNECTION_ERROR;
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n Database connection close error：" + e.getMessage());
			return DataBaseAction.CONNECTION_ERROR;
		} finally {
			try {
				if (pStatement != null)
					pStatement.close();
			} catch (SQLException e) {
				loggerBuffer.append("\n Database connection close  error：" + e.getMessage());
			}
		}
	}

	/**
	 * @param loggerBuffer 
	 * @param ctsCodeMap 
	 * @Title: insertOneLine_db_GRASS10 @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param agme_Grass_10 @param connection @param
	 * recv_time @param listDi @param fileN 返回值说明 @throws
	 */
	public static void insertOneLine_db_GRASS10(Agme_Grass_10 agme_Grass_10, java.sql.Connection connection,
			Date recv_time, List<StatDi> listDi, String filepath, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		Calendar calendar = Calendar.getInstance();
		//chy 去掉 D_record_id
		String sql = "INSERT INTO "+ctsCodeMap.getValue_table_name()+" (D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V01301,V01300,"
				+ "V05001,V06001,V07001,V07031,V_ACODE,V04001,V04002,V04003,V04004,V71912,V71116,V71913,D_SOURCE_ID)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		if (connection != null) {
			try {
				connection.setAutoCommit(true);
				// pStatement = connection.prepareStatement(sql);
				pStatement = new LoggableStatement(connection, sql);
			
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				StatDi di = new StatDi();
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(ctsCodeMap.getSod_code());
				di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
				di.setTT(v_bbb);
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); // 1成功，0失败
				di.setPROCESS_STATE("1"); // 1成功，0失败

				int num = 1;
				Date obsTime = agme_Grass_10.getObservationTime();
				// D_DATETIME, V01301, V71912
				String agme_grass10_chn_tab_pk = sdf.format(obsTime) + "_" + agme_Grass_10.getStationNumberChina() + "_"
						+ agme_Grass_10.getGrassLayerType() + "_" + agme_Grass_10.getMeasurementSite();
				pStatement.setString(num++, agme_grass10_chn_tab_pk); // 记录标识
				pStatement.setString(num++, ctsCodeMap.getSod_code()); // 资料标识，由配置文件配置
				pStatement.setTimestamp(num++, new Timestamp(new Date().getTime())); // 入库时间
				pStatement.setTimestamp(num++, new Timestamp(recv_time.getTime())); // 收到时间
				pStatement.setTimestamp(num++, new Timestamp(new Date().getTime()));// 更新时间
				// Date obsTime = agme_Grass_10.getObservationTime();
				calendar.setTime(obsTime);

				pStatement.setTimestamp(num++, new Timestamp(obsTime.getTime())); // 资料时间
				pStatement.setString(num++, agme_Grass_10.getStationNumberChina()); // 区站号（字符）
				pStatement.setString(num++, StationCodeUtil.stringToAscii(agme_Grass_10.getStationNumberChina())); // 区站号（数字）
				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_10.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 纬度
				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_10.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 经度
				pStatement.setBigDecimal(num++,
						new BigDecimal(String.valueOf(agme_Grass_10.getHeightOfSationGroundAboveMeanSeaLevel()))); // 测站海拔高度
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_10.getHeightOfBarometerAboveMeanSeaLevel())));
				// 气压传感器海拔高度 // 中国行政区划代码

				pStatement.setString(num++, getAdminCode(agme_Grass_10.getStationNumberChina(), loggerBuffer));
				pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.YEAR))); // 资料观测年
				pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.MONTH) + 1)); // 资料观测月
				pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.DAY_OF_MONTH))); // 资料观测日
				pStatement.setBigDecimal(num++, new BigDecimal(calendar.get(Calendar.HOUR))); // 资料观测时

				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_10.getGrassLayerType()));// 草层类型
				pStatement.setBigDecimal(num++, new BigDecimal(agme_Grass_10.getMeasurementSite()));// 测量场地
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Grass_10.getGrassLayerHeight())));// 草层高度
				pStatement.setString(num++, ctsCodeMap.getCts_code());
				
				di.setIIiii(agme_Grass_10.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());
				di.setLATITUDE(agme_Grass_10.getLatitude().toString());
				di.setLONGTITUDE(agme_Grass_10.getLongitude().toString());
				di.setDATA_UPDATE_FLAG(v_bbb);
				di.setHEIGHT(agme_Grass_10.getHeightOfSationGroundAboveMeanSeaLevel().toString());
				di.setSEND_PHYS("DRDS");
				di.setFILE_SIZE(String.valueOf(file.length()));
				try {
					pStatement.execute();
					listDi.add(di);
				} catch (SQLException e) {

					di.setPROCESS_STATE("0");// 1成功，0失败
					listDi.add(di);
					loggerBuffer.append("\n filename ：" + fileN + "\n " + agme_Grass_10.getStationNumberChina() + " "
							+ sdf.format(agme_Grass_10.getObservationTime()) + "\n execute sql error："
							+ ((LoggableStatement) pStatement).getQueryString() + "\n " + e.getMessage());
				}
			} catch (SQLException e) {
				loggerBuffer.append("\n create Statement error" + e.getMessage());
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
	}

	/**
	 * @Title: getAdminCode @Description: TODO(这里用一句话描述这个方法的作用) @param
	 * stationNumberChina @return 返回值说明 @throws
	 */
	public static String getAdminCode(String stat, StringBuffer loggerBuffer) {
		Map<String, Object> proMap = StationInfo.getProMap();
		String info = (String) proMap.get(stat + "+12");
		String adminCode = "999999";
		if(info != null) {
			String[] infos = info.split(",");
			if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
				adminCode = infos[5];
		}
		
		if(adminCode.startsWith("999999")){
			info = (String) proMap.get(stat + "+01");
			if(info == null) {
				loggerBuffer.append("\n In the configuration file, the station number does not exist" + stat);
			}else {
				String[] infos = info.split(",");
				if(infos.length >= 6)
					adminCode = infos[5];
			}
		}
		
//		String adminCode = null;
//		String info = (String) proMap.get(stationNumberChina + "+12");
//		String[] infos = info.split(",");
//		adminCode = infos[5];
		return adminCode;
	}

	public static int doubleParsInt(Double arge) {
		double douArge = arge * 1000;
		int intArge = (int) douArge;
		return intArge;
	}

}
