package cma.cimiss2.dpc.indb.agme.dc_agme_manl_grass.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
import jnr.ffi.Struct.pid_t;

import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.config.CTSCodeMap;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EleUpdateGrass {
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static String v_bbb = "000";
	
	/**
	 * @param ctsCodeMap 
	 * @param loggerBuffer 
	 * @Title: updateGRASS01 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param agme_Grass_01s
	 * @param connection
	 * @param recv_time
	 * @param listDi
	 * @param fileN  返回值说明
	 * @throws
	 */
	public static  void updateGRASS01(List<Agme_Grass_01> agme_Grass_01s, java.sql.Connection connection, Date recv_time,List<StatDi> listDi,String filepath,String v_bbb, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement Pstmt = null;
		int i = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		for (i = 0; i < agme_Grass_01s.size(); i++) {
			String vbbbInDB = null;
			vbbbInDB = FindVBBBGrass.findVBBBGrass01(agme_Grass_01s.get(i), connection,loggerBuffer,ctsCodeMap.getValue_table_name()); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if (vbbbInDB == null) { // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				// insert
				Insert_db_Grass.insertOneLine_db_GRASS01(agme_Grass_01s.get(i), connection, recv_time, listDi, filepath,loggerBuffer,ctsCodeMap);
			} else if (vbbbInDB.compareTo(v_bbb) < 0) { // 数据库中有该条记录，且V_BBB的值早于当前处理文件的更正标识的值
				// update
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
				di.setLATITUDE(agme_Grass_01s.get(i).getLatitude().toString());
				di.setLONGTITUDE(agme_Grass_01s.get(i).getLongitude().toString());
				String sql01 = "update  "+ctsCodeMap.getValue_table_name()+" set D_UPDATE_TIME=?,V71010=?,V_BBB=? where D_DATETIME=? and  V01301=? and V71501=? and V71002=?";
				if (connection != null) {
					try {
						connection.setAutoCommit(true);
						// Pstmt = connection.prepareStatement(sql01);
						Pstmt = new LoggableStatement(connection, sql01);
						
						int ii = 1;

						Pstmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(agme_Grass_01s.get(i).getPerDevlopmentPeriod())));
						Pstmt.setString(ii++, v_bbb);

						Pstmt.setTimestamp(ii++, new Timestamp(agme_Grass_01s.get(i).getObservationTime().getTime()));
						Pstmt.setString(ii++, agme_Grass_01s.get(i).getStationNumberChina());
						Pstmt.setInt(ii++, agme_Grass_01s.get(i).getHerbageName());
						Pstmt.setInt(ii++, agme_Grass_01s.get(i).getDevelopmentalPeriod());

						di.setIIiii(agme_Grass_01s.get(i).getStationNumberChina());
						di.setDATA_TIME(TimeUtil.date2String(agme_Grass_01s.get(i).getObservationTime(),
								"yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setDATA_UPDATE_FLAG(v_bbb);
						di.setHEIGHT(agme_Grass_01s.get(i).getHeightOfSationGroundAboveMeanSeaLevel().toString());
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(file.length()));
						try {
							Pstmt.execute();
//							connection.commit();
							listDi.add(di);
						} catch (SQLException e) {
							di.setPROCESS_STATE("0");// 1成功，0失败
							listDi.add(di);
							loggerBuffer.append("\n filename：" + fileN + "\n " + agme_Grass_01s.get(i).getStationNumberChina() + " "
									+ sdf.format(agme_Grass_01s.get(i).getObservationTime()) + "\n execute sql error："
									+ ((LoggableStatement) Pstmt).getQueryString() + "\n " + e.getMessage());
						}
					} catch (SQLException e) {
						loggerBuffer.append("\n create Statement error: " + e.getMessage());
					} finally {
						try {
							if (Pstmt != null)
								Pstmt.close();
						} catch (SQLException e) {
							loggerBuffer.append("\n close Statement error: " + e.getMessage());
						}
					}
				} else
					loggerBuffer.append("\n Database connection error");
			} // end else if
			else { // 数据库中有该条记录，且V_BBB的值晚于当前处理文件的更正标识的值
				// 不进行插入或更新
			}
		} // end for
	}
	/**
	 * @param ctsCodeMap 
	 * @param loggerBuffer 
	 * @Title: updateGRASS02 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param agme_Grass_02s
	 * @param connection
	 * @param recv_time
	 * @param listDi
	 * @param fileN  返回值说明
	 * @throws
	 */
	public static void updateGRASS02(List<Agme_Grass_02> agme_Grass_02s, java.sql.Connection connection, Date recv_time,List<StatDi> listDi,String filepath,String v_bbb, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement Pstmt = null;
		int i = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		for (i = 0; i < agme_Grass_02s.size(); i++) {
			String vbbbInDB = null;
			vbbbInDB = FindVBBBGrass.findVBBBGrass02(agme_Grass_02s.get(i), connection,loggerBuffer,ctsCodeMap.getValue_table_name()); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if (vbbbInDB == null) { // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				// insert
				Insert_db_Grass.insertOneLine_db_GRASS02(agme_Grass_02s.get(i), connection, recv_time, listDi, filepath,loggerBuffer,ctsCodeMap);
			} else if (vbbbInDB.compareTo(v_bbb) < 0) { // 数据库中有该条记录，且V_BBB的值早于当前处理文件的更正标识的值
				// update
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
				di.setLATITUDE(agme_Grass_02s.get(i).getLatitude().toString());
				di.setLONGTITUDE(agme_Grass_02s.get(i).getLongitude().toString());
				String sql01 = "update  "+ctsCodeMap.getValue_table_name()+" set D_UPDATE_TIME=?,V71006=?,V_BBB=? where D_DATETIME=? and  V01301=? and V71501=?";
				if (connection != null) {
					try {
						connection.setAutoCommit(true);
						// Pstmt = connection.prepareStatement(sql01);
						Pstmt = new LoggableStatement(connection, sql01);
						
						int ii = 1;

						
						Pstmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(agme_Grass_02s.get(i).getHeightGrowth())));
						Pstmt.setString(ii++, v_bbb);

						Pstmt.setTimestamp(ii++, new Timestamp(agme_Grass_02s.get(i).getObservationTime().getTime()));
						Pstmt.setString(ii++, agme_Grass_02s.get(i).getStationNumberChina());
						Pstmt.setInt(ii++, agme_Grass_02s.get(i).getHerbageName());

						
						di.setIIiii(agme_Grass_02s.get(i).getStationNumberChina());
						di.setDATA_TIME(TimeUtil.date2String(agme_Grass_02s.get(i).getObservationTime(),
								"yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setDATA_UPDATE_FLAG(v_bbb);
						di.setHEIGHT(agme_Grass_02s.get(i).getHeightOfSationGroundAboveMeanSeaLevel().toString());
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(file.length()));
						try {
							Pstmt.execute();
//							connection.commit();
							listDi.add(di);
						} catch (SQLException e) {
							di.setPROCESS_STATE("0");// 1成功，0失败
							listDi.add(di);
							loggerBuffer.append("\n filename ：" + fileN + "\n " + agme_Grass_02s.get(i).getStationNumberChina() + " "
									+ sdf.format(agme_Grass_02s.get(i).getObservationTime()) + "\n execute sql error："
									+ ((LoggableStatement) Pstmt).getQueryString() + "\n " + e.getMessage());
						}
					} catch (SQLException e) {
						loggerBuffer.append("\n create Statement error: " + e.getMessage());
					} finally {
						try {
							if (Pstmt != null)
								Pstmt.close();
						} catch (SQLException e) {
							loggerBuffer.append("\n close Statement error: " + e.getMessage());
						}
					}
				} else
					loggerBuffer.append("\n Database connection error");
			} // end else if
			else { // 数据库中有该条记录，且V_BBB的值晚于当前处理文件的更正标识的值
				// 不进行插入或更新
			}
		} // end for
	}
	/**
	 * @param ctsCodeMap 
	 * @param loggerBuffer 
	 * @Title: updateGRASS03 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param agme_Grass_03s
	 * @param connection
	 * @param recv_time
	 * @param listDi
	 * @param fileN  返回值说明
	 * @throws
	 */
	public static void updateGRASS03(List<Agme_Grass_03> agme_Grass_03s, java.sql.Connection connection, Date recv_time,List<StatDi> listDi,String filepath,String v_bbb, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement Pstmt = null;
		int i = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		for (i = 0; i < agme_Grass_03s.size(); i++) {
			String vbbbInDB = null;
			vbbbInDB = FindVBBBGrass.findVBBBGrass03(agme_Grass_03s.get(i), connection,loggerBuffer,ctsCodeMap.getValue_table_name()); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if (vbbbInDB == null) { // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				// insert
				Insert_db_Grass.insertOneLine_db_GRASS03(agme_Grass_03s.get(i), connection, recv_time, listDi, filepath,loggerBuffer,ctsCodeMap);
			} else if (vbbbInDB.compareTo(v_bbb) < 0) { // 数据库中有该条记录，且V_BBB的值早于当前处理文件的更正标识的值
				// update
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
				di.setLATITUDE(agme_Grass_03s.get(i).getLatitude().toString());
				di.setLONGTITUDE(agme_Grass_03s.get(i).getLongitude().toString());
				String sql01 = "update  "+ctsCodeMap.getValue_table_name()+" set D_UPDATE_TIME=?,V71652=?,V71651=?,V71906=?,V_BBB=? where D_DATETIME=? and  V01301=? and V71501=?";
				if (connection != null) {
					try {
						connection.setAutoCommit(true);
						// Pstmt = connection.prepareStatement(sql01);
						Pstmt = new LoggableStatement(connection, sql01);
						
						int ii = 1;

						Agme_Grass_03 agme_Grass_03=agme_Grass_03s.get(i);
						Pstmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(agme_Grass_03.getDryWeight())));// 干重
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(agme_Grass_03.getFreshWeight())));// 鲜重
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(agme_Grass_03.getDryFreshRatio())));// 干鲜比
						Pstmt.setString(ii++, v_bbb);

						Pstmt.setTimestamp(ii++, new Timestamp(agme_Grass_03.getObservationTime().getTime()));
						Pstmt.setString(ii++, agme_Grass_03.getStationNumberChina());
						Pstmt.setInt(ii++, agme_Grass_03.getHerbageName());

						
						di.setIIiii(agme_Grass_03.getStationNumberChina());
						di.setDATA_TIME(TimeUtil.date2String(agme_Grass_03.getObservationTime(),
								"yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setDATA_UPDATE_FLAG(v_bbb);
						di.setHEIGHT(agme_Grass_03.getHeightOfSationGroundAboveMeanSeaLevel().toString());
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(file.length()));
						try {
							Pstmt.execute();
//							connection.commit();
							listDi.add(di);
						} catch (SQLException e) {
							di.setPROCESS_STATE("0");// 1成功，0失败
							listDi.add(di);
							loggerBuffer.append("\n filename：" + fileN + "\n " + agme_Grass_03.getStationNumberChina() + " "
									+ sdf.format(agme_Grass_03.getObservationTime()) + "\n execute sql error："
									+ ((LoggableStatement) Pstmt).getQueryString() + "\n " + e.getMessage());
						}
					} catch (SQLException e) {
						loggerBuffer.append("\n create Statement error: " + e.getMessage());
					} finally {
						try {
							if (Pstmt != null)
								Pstmt.close();
						} catch (SQLException e) {
							loggerBuffer.append("\n close Statement error: " + e.getMessage());
						}
					}
				} else
					loggerBuffer.append("\n Database connection error");
			} // end else if
			else { // 数据库中有该条记录，且V_BBB的值晚于当前处理文件的更正标识的值
				// 不进行插入或更新
			}
		} // end for
	}
	
	/**
	 * @param ctsCodeMap 
	 * @param loggerBuffer 
	 * @Title: updateGRASS04 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param agme_Grass_04s
	 * @param connection
	 * @param recv_time
	 * @param listDi
	 * @param fileN  返回值说明
	 * @throws
	 */
	public static void updateGRASS04(List<Agme_Grass_04> agme_Grass_04s, java.sql.Connection connection, Date recv_time,List<StatDi> listDi,String filepath,String v_bbb, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement Pstmt = null;
		int i = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		for (i = 0; i < agme_Grass_04s.size(); i++) {
			String vbbbInDB = null;
			vbbbInDB = FindVBBBGrass.findVBBBGrass04(agme_Grass_04s.get(i), connection,loggerBuffer,ctsCodeMap.getValue_table_name()); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if (vbbbInDB == null) { // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				// insert
				Insert_db_Grass.insertOneLine_db_GRASS04(agme_Grass_04s.get(i), connection, recv_time, listDi, filepath,loggerBuffer,ctsCodeMap);
			} else if (vbbbInDB.compareTo(v_bbb) < 0) { // 数据库中有该条记录，且V_BBB的值早于当前处理文件的更正标识的值
				// update
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
				di.setLATITUDE(agme_Grass_04s.get(i).getLatitude().toString());
				di.setLONGTITUDE(agme_Grass_04s.get(i).getLongitude().toString());
				String sql01 = "update  "+ctsCodeMap.getValue_table_name()+" set D_UPDATE_TIME=?,V71907=?,V71908=?,V71909=?,V_BBB=? where D_DATETIME=? and  V01301=? and V71009=?";
				if (connection != null) {
					try {
						connection.setAutoCommit(true);
						// Pstmt = connection.prepareStatement(sql01);
						Pstmt = new LoggableStatement(connection, sql01);
						
						int ii = 1;

						Agme_Grass_04 agme_Grass_04=agme_Grass_04s.get(i);
						Pstmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						Pstmt.setBigDecimal(ii++, new BigDecimal(agme_Grass_04.getEvaluaGrassCondition()));// 草层状况评价
						Pstmt.setBigDecimal(ii++, new BigDecimal(agme_Grass_04.getFeedingDegree()));// 采食度
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(agme_Grass_04.getFeedingRate())));// 采食率
						Pstmt.setString(ii++, v_bbb);

						Pstmt.setTimestamp(ii++, new Timestamp(agme_Grass_04.getObservationTime().getTime()));
						Pstmt.setString(ii++, agme_Grass_04.getStationNumberChina());
						Pstmt.setBigDecimal(ii++, new BigDecimal(agme_Grass_04.getCoverDegree()));

						
						di.setIIiii(agme_Grass_04.getStationNumberChina());
						di.setDATA_TIME(TimeUtil.date2String(agme_Grass_04.getObservationTime(),
								"yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setDATA_UPDATE_FLAG(v_bbb);
						di.setHEIGHT(agme_Grass_04.getHeightOfSationGroundAboveMeanSeaLevel().toString());
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(file.length()));
						try {
							Pstmt.execute();
//							connection.commit();
							listDi.add(di);
						} catch (SQLException e) {
							di.setPROCESS_STATE("0");// 1成功，0失败
							listDi.add(di);
							loggerBuffer.append("\n filename：" + fileN + "\n " + agme_Grass_04.getStationNumberChina() + " "
									+ sdf.format(agme_Grass_04.getObservationTime()) + "\n execute sql error："
									+ ((LoggableStatement) Pstmt).getQueryString() + "\n " + e.getMessage());
						}
					} catch (SQLException e) {
						loggerBuffer.append("\n create Statement error: " + e.getMessage());
					} finally {
						try {
							if (Pstmt != null)
								Pstmt.close();
						} catch (SQLException e) {
							loggerBuffer.append("\n close Statement error: " + e.getMessage());
						}
					}
				} else
					loggerBuffer.append("\n Database connection error");
			} // end else if
			else { // 数据库中有该条记录，且V_BBB的值晚于当前处理文件的更正标识的值
				// 不进行插入或更新
			}
		} // end for
	}
	
	/**
	 * @param ctsCodeMap 
	 * @param loggerBuffer 
	 * @Title: updateGRASS05 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param agme_Grass_05s
	 * @param connection
	 * @param recv_time
	 * @param listDi
	 * @param fileN  返回值说明
	 * @throws
	 */
	public static void updateGRASS05(List<Agme_Grass_05> agme_Grass_05s, java.sql.Connection connection, Date recv_time,List<StatDi> listDi,String filepath,String v_bbb, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement Pstmt = null;
		int i = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		for (i = 0; i < agme_Grass_05s.size(); i++) {
			String vbbbInDB = null;
			vbbbInDB = FindVBBBGrass.findVBBBGrass05(agme_Grass_05s.get(i), connection,loggerBuffer,ctsCodeMap.getValue_table_name()); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if (vbbbInDB == null) { // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				// insert
				Insert_db_Grass.insertOneLine_db_GRASS05(agme_Grass_05s.get(i), connection, recv_time, listDi, filepath,loggerBuffer,ctsCodeMap);
			} else if (vbbbInDB.compareTo(v_bbb) < 0) { // 数据库中有该条记录，且V_BBB的值早于当前处理文件的更正标识的值
				// update
				StatDi di = new StatDi();
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(ctsCodeMap.getSod_code());
				di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
				di.setTT(v_bbb);
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //1成功，0失败
				di.setPROCESS_STATE("1"); // 1成功，0失败
				di.setLATITUDE(agme_Grass_05s.get(i).getLatitude().toString());
				di.setLONGTITUDE(agme_Grass_05s.get(i).getLongitude().toString());
				String sql01 = "update  "+ctsCodeMap.getValue_table_name()+" set D_UPDATE_TIME=?,V71910=?,V71911=?,V_BBB=? where D_DATETIME=? and  V01301=? and V71501=?";
				if (connection != null) {
					try {
						connection.setAutoCommit(true);
						// Pstmt = connection.prepareStatement(sql01);
						Pstmt = new LoggableStatement(connection, sql01);
						
						int ii = 1;

						Agme_Grass_05 agme_Grass_05=agme_Grass_05s.get(i);
						Pstmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(agme_Grass_05.getNumPlantPerHectare())));// 每公顷株丛数
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(agme_Grass_05.getTotalNumPlantPerHectare())));// 每公顷总株丛数
						Pstmt.setString(ii++, v_bbb);

						Pstmt.setTimestamp(ii++, new Timestamp(agme_Grass_05.getObservationTime().getTime()));
						Pstmt.setString(ii++, agme_Grass_05.getStationNumberChina());
						Pstmt.setInt(ii++, agme_Grass_05.getHerbageName());

						
						di.setIIiii(agme_Grass_05.getStationNumberChina());
						di.setDATA_TIME(TimeUtil.date2String(agme_Grass_05.getObservationTime(),
								"yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setDATA_UPDATE_FLAG(v_bbb);
						di.setHEIGHT(agme_Grass_05.getHeightOfSationGroundAboveMeanSeaLevel().toString());
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(file.length()));
						try {
							Pstmt.execute();
//							connection.commit();
							listDi.add(di);
						} catch (SQLException e) {
							di.setPROCESS_STATE("0");// 1成功，0失败
							listDi.add(di);
							loggerBuffer.append("\n filename ：" + fileN + "\n " + agme_Grass_05.getStationNumberChina() + " "
									+ sdf.format(agme_Grass_05.getObservationTime()) + "\n execute sql error："
									+ ((LoggableStatement) Pstmt).getQueryString() + "\n " + e.getMessage());
						}
					} catch (SQLException e) {
						loggerBuffer.append("\n create Statement error: " + e.getMessage());
					} finally {
						try {
							if (Pstmt != null)
								Pstmt.close();
						} catch (SQLException e) {
							loggerBuffer.append("\n close Statement error: " + e.getMessage());
						}
					}
				} else
					loggerBuffer.append("\n Database connection error");
			} // end else if
			else { // 数据库中有该条记录，且V_BBB的值晚于当前处理文件的更正标识的值
				// 不进行插入或更新
			}
		} // end for
	}
	
	/**
	 * @param ctsCodeMap 
	 * @param loggerBuffer 
	 * @Title: updateGRASS06 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param agme_Grass_06s
	 * @param connection
	 * @param recv_time
	 * @param listDi
	 * @param fileN  返回值说明
	 * @throws
	 */
	public static void updateGRASS06(List<Agme_Grass_06> agme_Grass_06s, java.sql.Connection connection, Date recv_time,List<StatDi> listDi,String filepath,String v_bbb, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement Pstmt = null;
		int i = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		for (i = 0; i < agme_Grass_06s.size(); i++) {
			String vbbbInDB = null;
			vbbbInDB = FindVBBBGrass.findVBBBGrass06(agme_Grass_06s.get(i), connection,loggerBuffer,ctsCodeMap.getValue_table_name()); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if (vbbbInDB == null) { // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				// insert
				Insert_db_Grass.insertOneLine_db_GRASS06(agme_Grass_06s.get(i), connection, recv_time, listDi, filepath,loggerBuffer,ctsCodeMap);
			} else if (vbbbInDB.compareTo(v_bbb) < 0) { // 数据库中有该条记录，且V_BBB的值早于当前处理文件的更正标识的值
				// update
				StatDi di = new StatDi();
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(ctsCodeMap.getSod_code());
				di.setDATA_TYPE_1(ctsCodeMap.getCts_code());
				di.setTT(v_bbb);
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //1成功，0失败
				di.setPROCESS_STATE("1"); // 1成功，0失败
				di.setLATITUDE(agme_Grass_06s.get(i).getLatitude().toString());
				di.setLONGTITUDE(agme_Grass_06s.get(i).getLongitude().toString());
				String sql01 = "update  "+ctsCodeMap.getValue_table_name()+" set D_UPDATE_TIME=?,V71921=?,V71922=?,V_BBB=? where D_DATETIME=? and  V01301=? and V71920=?";
				if (connection != null) {
					try {
						connection.setAutoCommit(true);
						// Pstmt = connection.prepareStatement(sql01);
						Pstmt = new LoggableStatement(connection, sql01);
						
						int ii = 1;

						Agme_Grass_06 agme_Grass_06=agme_Grass_06s.get(i);
						Pstmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						
						Pstmt.setBigDecimal(ii++, new BigDecimal(agme_Grass_06.getAdultAnimalNum()));// 成畜头数
						Pstmt.setBigDecimal(ii++, new BigDecimal(agme_Grass_06.getBabyHeadNum()));// 幼畜头数
						Pstmt.setString(ii++, v_bbb);

						Pstmt.setTimestamp(ii++, new Timestamp(agme_Grass_06.getObservationTime().getTime()));
						Pstmt.setString(ii++, agme_Grass_06.getStationNumberChina());
						Pstmt.setBigDecimal(ii++, new BigDecimal(agme_Grass_06.getRankCondition()));// 膘情等级

						
						di.setIIiii(agme_Grass_06.getStationNumberChina());
						di.setDATA_TIME(TimeUtil.date2String(agme_Grass_06.getObservationTime(),
								"yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setDATA_UPDATE_FLAG(v_bbb);
						di.setHEIGHT(agme_Grass_06.getHeightOfSationGroundAboveMeanSeaLevel().toString());
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(file.length()));
						try {
							Pstmt.execute();
//							connection.commit();
							listDi.add(di);
						} catch (SQLException e) {
							di.setPROCESS_STATE("0");// 1成功，0失败
							listDi.add(di);
							loggerBuffer.append("\n filename：" + fileN + "\n " + agme_Grass_06.getStationNumberChina() + " "
									+ sdf.format(agme_Grass_06.getObservationTime()) + "\n execute sql error："
									+ ((LoggableStatement) Pstmt).getQueryString() + "\n " + e.getMessage());
						}
					} catch (SQLException e) {
						loggerBuffer.append("\n create Statement error: " + e.getMessage());
					} finally {
						try {
							if (Pstmt != null)
								Pstmt.close();
						} catch (SQLException e) {
							loggerBuffer.append("\n close Statement error: " + e.getMessage());
						}
					}
				} else
					loggerBuffer.append("\n Database connection error");
			} // end else if
			else { // 数据库中有该条记录，且V_BBB的值晚于当前处理文件的更正标识的值
				// 不进行插入或更新
			}
		} // end for
	}
	
	/**
	 * @param ctsCodeMap 
	 * @param loggerBuffer 
	 * @Title: updateGRASS07 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param agme_Grass_07s
	 * @param connection
	 * @param recv_time
	 * @param listDi
	 * @param fileN  返回值说明
	 * @throws
	 */
	public static void updateGRASS07(List<Agme_Grass_07> agme_Grass_07s, java.sql.Connection connection, Date recv_time,List<StatDi> listDi,String filepath,String v_bbb, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement Pstmt = null;
		int i = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		for (i = 0; i < agme_Grass_07s.size(); i++) {
			String vbbbInDB = null;
			vbbbInDB = FindVBBBGrass.findVBBBGrass07(agme_Grass_07s.get(i), connection,loggerBuffer,ctsCodeMap.getValue_table_name()); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if (vbbbInDB == null) { // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				// insert
				Insert_db_Grass.insertOneLine_db_GRASS07(agme_Grass_07s.get(i), connection, recv_time, listDi, filepath,loggerBuffer,ctsCodeMap);
			} else if (vbbbInDB.compareTo(v_bbb) < 0) { // 数据库中有该条记录，且V_BBB的值早于当前处理文件的更正标识的值
				// update
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
				di.setLATITUDE(agme_Grass_07s.get(i).getLatitude().toString());
				di.setLONGTITUDE(agme_Grass_07s.get(i).getLongitude().toString());
				String sql01 = "update  "+ctsCodeMap.getValue_table_name()+" set D_UPDATE_TIME=?,V_WEIGHT_AVG=?,V_BBB=? where D_DATETIME=? and  V01301=? and WEIGHT_01=? and WEIGHT_02=? and WEIGHT_03=? and WEIGHT_04=? and WEIGHT_05=?";
				if (connection != null) {
					try {
						connection.setAutoCommit(true);
						// Pstmt = connection.prepareStatement(sql01);
						Pstmt = new LoggableStatement(connection, sql01);
						
						int ii = 1;

						Agme_Grass_07 agme_Grass_07=agme_Grass_07s.get(i);
					
						Pstmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						
						Pstmt.setBigDecimal(ii++, new BigDecimal(agme_Grass_07.getAvgWeight()));// 平均
						Pstmt.setString(ii++, v_bbb);

						Pstmt.setTimestamp(ii++, new Timestamp(agme_Grass_07.getObservationTime().getTime()));
						Pstmt.setString(ii++, agme_Grass_07.getStationNumberChina());
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(agme_Grass_07.getRamWeight_1())));// 羯羊_1体重
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(agme_Grass_07.getRamWeight_2())));// 羯羊_2体重
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(agme_Grass_07.getRamWeight_3())));// 羯羊_3体重
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(agme_Grass_07.getRamWeight_4())));// 羯羊_4体重
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(agme_Grass_07.getRamWeight_5())));// 羯羊_5体重

						
						di.setIIiii(agme_Grass_07.getStationNumberChina());
						di.setDATA_TIME(TimeUtil.date2String(agme_Grass_07.getObservationTime(),
								"yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setDATA_UPDATE_FLAG(v_bbb);
						di.setHEIGHT(agme_Grass_07.getHeightOfSationGroundAboveMeanSeaLevel().toString());
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(file.length()));
						try {
							Pstmt.execute();
//							connection.commit();
							listDi.add(di);
						} catch (SQLException e) {
							di.setPROCESS_STATE("0");// 1成功，0失败
							listDi.add(di);
							loggerBuffer.append("\n filename ：" + fileN + "\n " + agme_Grass_07.getStationNumberChina() + " "
									+ sdf.format(agme_Grass_07.getObservationTime()) + "\n execute sql error："
									+ ((LoggableStatement) Pstmt).getQueryString() + "\n " + e.getMessage());
						}
					} catch (SQLException e) {
						loggerBuffer.append("\n create Statement error: " + e.getMessage());
					} finally {
						try {
							if (Pstmt != null)
								Pstmt.close();
						} catch (SQLException e) {
							loggerBuffer.append("\n close Statement error: " + e.getMessage());
						}
					}
				} else
					loggerBuffer.append("\n Database connection error");
			} // end else if
			else { // 数据库中有该条记录，且V_BBB的值晚于当前处理文件的更正标识的值
				// 不进行插入或更新
			}
		} // end for
	}
	
	/**
	 * @param ctsCodeMap 
	 * @param loggerBuffer 
	 * @Title: updateGRASS08 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param agme_Grass_08s
	 * @param connection
	 * @param recv_time
	 * @param listDi
	 * @param fileN  返回值说明
	 * @throws
	 */
	public static void updateGRASS08(List<Agme_Grass_08> agme_Grass_08s, java.sql.Connection connection, Date recv_time,List<StatDi> listDi,String filepath,String v_bbb, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement Pstmt = null;
		int i = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		for (i = 0; i < agme_Grass_08s.size(); i++) {
			String vbbbInDB = null;
			vbbbInDB = FindVBBBGrass.findVBBBGrass08(agme_Grass_08s.get(i), connection,loggerBuffer,ctsCodeMap.getValue_table_name()); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if (vbbbInDB == null) { // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				// insert
				Insert_db_Grass.insertOneLine_db_GRASS08(agme_Grass_08s.get(i), connection, recv_time, listDi, filepath,loggerBuffer,ctsCodeMap);
			} else if (vbbbInDB.compareTo(v_bbb) < 0) { // 数据库中有该条记录，且V_BBB的值早于当前处理文件的更正标识的值
				// update
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
				di.setLATITUDE(agme_Grass_08s.get(i).getLatitude().toString());
				di.setLONGTITUDE(agme_Grass_08s.get(i).getLongitude().toString());
				String sql01 = "update  "+ctsCodeMap.getValue_table_name()+" set D_UPDATE_TIME=?,V_AVG_GRAZE_01=?,V_AVG_GRAZE_02=?,V_AVG_GRAZE_03=?,V_AVG_GRAZE_04=?,V_HOVEL=?,V_HOVEL_COUNT=?,V_HOVEL_LEN=?,V_HOVEL_W=?,V_HOVEL_H=?,V_HOVEL_ST=?,V_HOVEL_TYPE=?,V_HOVEL_WD=?,V_ORGAN=?,V_BBB=? where D_DATETIME=? and  V01301=? and V71501=? and V71601=?";
				if (connection != null) {
					try {
						connection.setAutoCommit(true);
						// Pstmt = connection.prepareStatement(sql01);
						Pstmt = new LoggableStatement(connection, sql01);
						
						int ii = 1;

						Agme_Grass_08 agme_Grass_08=agme_Grass_08s.get(i);
						Pstmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						
						Pstmt.setBigDecimal(ii++, new BigDecimal(agme_Grass_08.getAvgSpringDailyGrazHours()));// 春季日平均放牧时数
						Pstmt.setBigDecimal(ii++, new BigDecimal(agme_Grass_08.getAvgSummerDailyGrazHours()));// 夏季日平均放牧时数
						Pstmt.setBigDecimal(ii++, new BigDecimal(agme_Grass_08.getAvgFallDailyGrazHours()));// 秋季日平均放牧时数
						Pstmt.setBigDecimal(ii++, new BigDecimal(agme_Grass_08.getAvgWinterDailyGrazHours()));// 冬季日平均放牧时数
						Pstmt.setBigDecimal(ii++, new BigDecimal(agme_Grass_08.getIsHaveSuccah()));// 有无棚舍
						Pstmt.setBigDecimal(ii++, new BigDecimal(agme_Grass_08.getSuccahNum()));// 棚舍数量
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(agme_Grass_08.getSuccahLong())));// 棚舍长
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(agme_Grass_08.getSuccahWide())));// 棚舍宽
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(agme_Grass_08.getSuccahHigh())));// 棚舍高
						try {
							Pstmt.setString(ii++, new String(agme_Grass_08.getSuccahFrame().getBytes(), "UTF8"));
						} catch (UnsupportedEncodingException e1) {
							Pstmt.setString(ii++, "999999");
							loggerBuffer.append("getSuccahFrame() format error: " + e1.getMessage());
						}// 棚舍结构
						try {
							Pstmt.setString(ii++, new String(agme_Grass_08.getSuccahType().getBytes(),"UTF8"));
						} catch (UnsupportedEncodingException e1) {
							Pstmt.setString(ii++, "999999");
							loggerBuffer.append("getSuccahType() format error: " + e1.getMessage());
						}// 棚舍型式
						try {
							Pstmt.setString(ii++, new String(agme_Grass_08.getSuccahWinDirection().getBytes(), "UTF8"));
						} catch (UnsupportedEncodingException e1) {
							Pstmt.setString(ii++, "999999");
							loggerBuffer.append("getSuccahWinDirection() format error: " + e1.getMessage());
						}// 棚舍门窗开向
						
						Pstmt.setString(ii++, agme_Grass_08.getLivestockUnit());// 畜群所属单位
						Pstmt.setString(ii++, v_bbb);
						Pstmt.setTimestamp(ii++, new Timestamp(agme_Grass_08.getObservationTime().getTime()));
						Pstmt.setString(ii++, agme_Grass_08.getStationNumberChina());
						try {
							Pstmt.setString(ii++, new String(agme_Grass_08.getLivestockName().getBytes(), "UTF8"));
						} catch (UnsupportedEncodingException e1) {
							Pstmt.setString(ii++, "999999");
							loggerBuffer.append("getLivestockName() format error: " + e1.getMessage());
						}// 畜群家畜名称
						try {
							Pstmt.setString(ii++, new String(agme_Grass_08.getLivestockBreeds().getBytes(), "UTF8"));
						} catch (UnsupportedEncodingException e1) {
							Pstmt.setString(ii++, "999999");
							loggerBuffer.append("getLivestockBreeds() format error: " + e1.getMessage());
						}// 家畜品种

						
						di.setIIiii(agme_Grass_08.getStationNumberChina());
						di.setDATA_TIME(TimeUtil.date2String(agme_Grass_08.getObservationTime(),
								"yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setDATA_UPDATE_FLAG(v_bbb);
						di.setHEIGHT(agme_Grass_08.getHeightOfSationGroundAboveMeanSeaLevel().toString());
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(file.length()));
						try {
							Pstmt.execute();
//							connection.commit();
							listDi.add(di);
						} catch (SQLException e) {
							di.setPROCESS_STATE("0");//1成功，0失败
							listDi.add(di);
							loggerBuffer.append("\n filename：" + fileN + "\n " + agme_Grass_08.getStationNumberChina() + " "
									+ sdf.format(agme_Grass_08.getObservationTime()) + "\n execute sql error："
									+ ((LoggableStatement) Pstmt).getQueryString() + "\n " + e.getMessage());
						}
					} catch (SQLException e) {
						loggerBuffer.append("\n create Statement error: " + e.getMessage());
					} finally {
						try {
							if (Pstmt != null)
								Pstmt.close();
						} catch (SQLException e) {
							loggerBuffer.append("\n close Statement error: " + e.getMessage());
						}
					}
				} else
					loggerBuffer.append("\n Database connection error");
			} // end else if
			else { // 数据库中有该条记录，且V_BBB的值晚于当前处理文件的更正标识的值
				// 不进行插入或更新
			}
		} // end for
	}
	
	/**
	 * @param ctsCodeMap 
	 * @param loggerBuffer 
	 * @Title: updateGRASS09 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param agme_Grass_09s
	 * @param connection
	 * @param recv_time
	 * @param listDi
	 * @param fileN  返回值说明
	 * @throws
	 */
	public static void updateGRASS09(List<Agme_Grass_09> agme_Grass_09s, java.sql.Connection connection, Date recv_time,List<StatDi> listDi,String filepath,String v_bbb, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement Pstmt = null;
		int i = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		for (i = 0; i < agme_Grass_09s.size(); i++) {
			String vbbbInDB = null;
			vbbbInDB = FindVBBBGrass.findVBBBGrass09(agme_Grass_09s.get(i), connection,loggerBuffer,ctsCodeMap.getValue_table_name()); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if (vbbbInDB == null) { // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				// insert
				Insert_db_Grass.insertOneLine_db_GRASS09(agme_Grass_09s.get(i), connection, recv_time, listDi, filepath,loggerBuffer,ctsCodeMap);
			} else if (vbbbInDB.compareTo(v_bbb) < 0) { // 数据库中有该条记录，且V_BBB的值早于当前处理文件的更正标识的值
				// update
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
				
				String sql01 = "update  "+ctsCodeMap.getValue_table_name()+" set D_UPDATE_TIME=?,V04300_017=?,V04300_018=?,V_PROD_FUNC=?,V_BBB=? where D_DATETIME=? and  V01301=? and V71616_02=?";
				if (connection != null) {
					try {
						connection.setAutoCommit(true);
						// Pstmt = connection.prepareStatement(sql01);
						Pstmt = new LoggableStatement(connection, sql01);
						
						int ii = 1;

						Agme_Grass_09 agme_Grass_09=agme_Grass_09s.get(i);
						Pstmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						Pstmt.setBigDecimal(ii++, new BigDecimal(agme_Grass_09.getObservationStarTime()));// 调查起始时间
						Pstmt.setBigDecimal(ii++, new BigDecimal(agme_Grass_09.getObservationEndTime()));// 调查终止时间
						
						try {
							Pstmt.setString(ii++, new String(agme_Grass_09.getProductPerformance().getBytes(), "UTF8"));
						} catch (UnsupportedEncodingException e1) {
							Pstmt.setString(ii++, "999999");
							loggerBuffer.append("getProductPerformance() format error: " + e1.getMessage());
						}// 生产性能
						
						Pstmt.setString(ii++, v_bbb);
						try {
							Pstmt.setString(ii++, new String(agme_Grass_09.getObservationStarTime().getBytes(), "UTF8"));
						} catch (UnsupportedEncodingException e1) {
							Pstmt.setString(ii++, "999999");
							loggerBuffer.append("getObservationStarTime() format error: " + e1.getMessage());
						}
						Pstmt.setString(ii++, agme_Grass_09.getStationNumberChina());
						Pstmt.setBigDecimal(ii++, new BigDecimal(agme_Grass_09.getAnimalHusbandryName()));// 牧事活动名称

						
						di.setIIiii(agme_Grass_09.getStationNumberChina());
						
						di.setDATA_TIME(TimeUtil.date2String(sdf.parse(agme_Grass_09.getObservationStarTime()),
									"yyyy-MM-dd HH:mm"));
						
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setLATITUDE(agme_Grass_09.getLatitude().toString());
						di.setLONGTITUDE(agme_Grass_09.getLongitude().toString());
						di.setDATA_UPDATE_FLAG(v_bbb);
						di.setHEIGHT(agme_Grass_09.getHeightOfSationGroundAboveMeanSeaLevel().toString());
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(file.length()));
						try {
							
							Pstmt.execute();
//							connection.commit();
							listDi.add(di);
						} catch (SQLException e) {
							di.setPROCESS_STATE("0");//1成功，0失败
							listDi.add(di);
							loggerBuffer.append("\n filename：" + fileN + "\n " + agme_Grass_09.getStationNumberChina() + " "
									+ TimeUtil.String2Date(agme_Grass_09.getObservationStarTime(),"yyyyMMddHHmmss") + "\n execute sql error："
									+ ((LoggableStatement) Pstmt).getQueryString() + "\n " + e.getMessage());
						}
					} catch (SQLException e) {
						loggerBuffer.append("\n create Statement error: " + e.getMessage());
					} 
					catch (ParseException e1) {
						// TODO Auto-generated catch block
						loggerBuffer.append("\n Time conversion error: " + e1.getMessage());
					}
					finally {
						try {
							if (Pstmt != null)
								Pstmt.close();
						} catch (SQLException e) {
							loggerBuffer.append("\n close Statement error: " + e.getMessage());
						}
					}
				} else
					loggerBuffer.append("\n Database connection error");
			} // end else if
			else { // 数据库中有该条记录，且V_BBB的值晚于当前处理文件的更正标识的值
				// 不进行插入或更新
			}
		} // end for
	}
	
	/**
	 * @param ctsCodeMap 
	 * @param loggerBuffer 
	 * @Title: updateGRASS10 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param agme_Grass_10s
	 * @param connection
	 * @param recv_time
	 * @param listDi
	 * @param fileN  返回值说明
	 * @throws
	 */
	public static void updateGRASS10(List<Agme_Grass_10> agme_Grass_10s, java.sql.Connection connection, Date recv_time,List<StatDi> listDi,String filepath,String v_bbb, StringBuffer loggerBuffer, CTSCodeMap ctsCodeMap) {
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement Pstmt = null;
		int i = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		for (i = 0; i < agme_Grass_10s.size(); i++) {
			String vbbbInDB = null;
			vbbbInDB = FindVBBBGrass.findVBBBGrass10(agme_Grass_10s.get(i), connection,loggerBuffer,ctsCodeMap.getValue_table_name()); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if (vbbbInDB == null) { // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				// insert
				Insert_db_Grass.insertOneLine_db_GRASS10(agme_Grass_10s.get(i), connection, recv_time, listDi, filepath,loggerBuffer,ctsCodeMap);
			} else if (vbbbInDB.compareTo(v_bbb) < 0) { // 数据库中有该条记录，且V_BBB的值早于当前处理文件的更正标识的值
				// update
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
				String sql01 = "update  "+ctsCodeMap.getValue_table_name()+" set D_UPDATE_TIME=?,V71913=?,V_BBB=? where D_DATETIME=? and  V01301=? and V71912=? and V71116=?";
				if (connection != null) {
					try {
						connection.setAutoCommit(true);
						// Pstmt = connection.prepareStatement(sql01);
						Pstmt = new LoggableStatement(connection, sql01);
						
						int ii = 1;

						Agme_Grass_10 agme_Grass_10=agme_Grass_10s.get(i);
						Pstmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(agme_Grass_10.getGrassLayerHeight())));// 草层高度
						Pstmt.setString(ii++, v_bbb);
						Pstmt.setTimestamp(ii++, new Timestamp(agme_Grass_10.getObservationTime().getTime()));
						Pstmt.setString(ii++, agme_Grass_10.getStationNumberChina());
						Pstmt.setBigDecimal(ii++, new BigDecimal(agme_Grass_10.getGrassLayerType()));// 草层类型
						Pstmt.setBigDecimal(ii++, new BigDecimal(agme_Grass_10.getMeasurementSite()));// 测量场地

						
						di.setIIiii(agme_Grass_10.getStationNumberChina());
						di.setDATA_TIME(TimeUtil.date2String(agme_Grass_10.getObservationTime(),
								"yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setLATITUDE(agme_Grass_10.getLatitude().toString());
						di.setLONGTITUDE(agme_Grass_10.getLongitude().toString());
						di.setDATA_UPDATE_FLAG(v_bbb);
						di.setHEIGHT(agme_Grass_10.getHeightOfSationGroundAboveMeanSeaLevel().toString());
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(file.length()));
						try {
							Pstmt.execute();
//							connection.commit();
							listDi.add(di);
						} catch (SQLException e) {
							di.setPROCESS_STATE("0");// 1成功，0失败
							listDi.add(di);
							loggerBuffer.append("\n filename：" + fileN + "\n " + agme_Grass_10.getStationNumberChina() + " "
									+ sdf.format(agme_Grass_10.getObservationTime()) + "\n execute sql error："
									+ ((LoggableStatement) Pstmt).getQueryString() + "\n " + e.getMessage());
						}
					} catch (SQLException e) {
						loggerBuffer.append("\n create Statement error: " + e.getMessage());
					} finally {
						try {
							if (Pstmt != null)
								Pstmt.close();
						} catch (SQLException e) {
							loggerBuffer.append("\n close Statement error: " + e.getMessage());
						}
					}
				} else
					loggerBuffer.append("\n Database connection error");
			} // end else if
			else { // 数据库中有该条记录，且V_BBB的值晚于当前处理文件的更正标识的值
				// 不进行插入或更新
			}
		} // end for
	}
	public static int doubleParsInt(Double arge) {
		double douArge = arge * 1000;
		int intArge = (int) douArge;
		return intArge;
	}
}
