package cma.cimiss2.dpc.indb.agme.dc_agme_manl_pheno.service;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Date;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;


import org.apache.commons.lang3.ArrayUtils;


import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.config.CTSCodeMap;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.NumberUtil;
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Pheno;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Pheno_01;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Pheno_02;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Pheno_03;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Pheno_04;
import cma.cimiss2.dpc.decoder.tools.ElementValUtil;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.agme.ReportInfoService;
import cma.cimiss2.dpc.indb.agme.dc_agme_manl_pheno.ReadIni;

public class DbService {
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static BlockingQueue<StatDi> diQueues;
	static ReadIni ini = ReadIni.getIni();
//	static Map<String, Object> proMap = StationInfo.getProMap();
	private static final double[] NINE_ARRAYS = { 99, 999, 9999, 99999 };
	
	
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}

	public static DataBaseAction insert_db(ParseResult<Agme_Pheno> parseResult, Date recv_time, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer, List<CTSCodeMap> ctsCodeMaps) {
		File file = new File(filepath);
		String fileName = file.getName();
		// 获取数据库连接
		java.sql.Connection connection = null;
		try {
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");

			if (connection != null) {
				// list: 所有数据
				List<Agme_Pheno> list = parseResult.getData();
				Agme_Pheno agme_Pheno = list.get(0); // 解析时只存了一个对象在parseResult
				List<String> phenoTypes = agme_Pheno.phenoTypes;

				for (String type : phenoTypes) {
					switch (type) {
					case ReadIni.SECTION_PHENO_01:
						List<Object> agme_pheno_01s = agme_Pheno.agme_pheno_01s;

						String d_data_id = ini.getValue(ReadIni.SECTION_PHENO_01, ReadIni.D_DATA_ID_KEY);
						// 判断是否需要更新
						if (isUpdate) {
							connection.setAutoCommit(true);
							update_pheno_data_01(agme_pheno_01s, connection, recv_time, d_data_id,filepath,isUpdate,v_bbb,loggerBuffer);
						} else { // 不需要更新直接入库,如果批量入库失败，需改为逐单条入库
							connection.setAutoCommit(false);
							insert_pheno_data_01(agme_pheno_01s, connection, recv_time, d_data_id,filepath,isUpdate,v_bbb,loggerBuffer);
						}
						break;
					case ReadIni.SECTION_PHENO_02:
						List<Object> agme_pheno_02s = agme_Pheno.agme_pheno_02s;

						d_data_id = ini.getValue(ReadIni.SECTION_PHENO_02, ReadIni.D_DATA_ID_KEY);
						// 判断是否需要更新
						if (isUpdate) {
							connection.setAutoCommit(true);
							update_pheno_data_02(agme_pheno_02s, connection, recv_time, d_data_id,isUpdate,filepath,v_bbb,loggerBuffer);
						} else { // 不需要更新直接入库,如果批量入库失败，需改为逐单条入库
							connection.setAutoCommit(false);
							insert_pheno_data_02(agme_pheno_02s, connection, recv_time, d_data_id,filepath,isUpdate,v_bbb,loggerBuffer);
						}
						break;
					case ReadIni.SECTION_PHENO_03:
						List<Object> agme_pheno_03s = agme_Pheno.agme_pheno_03s;

						d_data_id = ini.getValue(ReadIni.SECTION_PHENO_03, ReadIni.D_DATA_ID_KEY);
						// 判断是否需要更新
						if (isUpdate) {
							connection.setAutoCommit(true);
							update_pheno_data_03(agme_pheno_03s, connection, recv_time, d_data_id,isUpdate,filepath,v_bbb,loggerBuffer);
						} else { // 不需要更新直接入库,如果批量入库失败，需改为逐单条入库
							connection.setAutoCommit(false);
							insert_pheno_data_03(agme_pheno_03s, connection, recv_time, d_data_id,filepath,isUpdate,v_bbb,loggerBuffer);
						}
						break;
					case ReadIni.SECTION_PHENO_04:
						List<Object> agme_pheno_04s = agme_Pheno.agme_pheno_04s;

						d_data_id = ini.getValue(ReadIni.SECTION_PHENO_04, ReadIni.D_DATA_ID_KEY);
						// 判断是否需要更新
						if (isUpdate) {
							connection.setAutoCommit(true);
							update_pheno_data_04(agme_pheno_04s, connection, recv_time, d_data_id,isUpdate,filepath,v_bbb,loggerBuffer);
						} else { // 不需要更新直接入库,如果批量入库失败，需改为逐单条入库
							connection.setAutoCommit(false);
							insert_pheno_data_04(agme_pheno_04s, connection, recv_time, d_data_id,filepath,isUpdate,v_bbb,loggerBuffer);
						}
						break;
					default:
						break;
					}

				}
				@SuppressWarnings("rawtypes")
				List<ReportInfo> reportInfos = parseResult.getReports();
				String[] fnames = fileName.split("_");
				java.sql.Connection report_connection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
				List<StatDi> listDi = new ArrayList<StatDi>();
				ReportInfoService.reportInfoToDb(reportInfos, report_connection, v_bbb, recv_time, fnames[3], fnames[1],ctsCodeMaps, filepath, listDi);
				for (int j = 0; j < listDi.size(); j++) {
					diQueues.offer(listDi.get(j));
				}
				listDi.clear();
			} else {
				loggerBuffer.append("\n get database connection error");
				return DataBaseAction.CONNECTION_ERROR;
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n  set AutoCommit failed");
			return DataBaseAction.CONNECTION_ERROR;
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n close database connection error：" + e.getMessage());
				}
			}
		}
		return DataBaseAction.SUCCESS;
	}

	/**
	 * 
	 * @Title: insert_soil_data_01
	 * @Description: 自然物候气象要素数据01、02、04类入库
	 * @param recv_time 收到时间
	 * @param connection 数据库连接
	 * @param agme_Pheno_01s 01、02、04类数据解析集合
	 * @param fileName 
	 * @param isUpdate 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @return DataBaseAction 成功与否标识
	 * @throws:
	 */
	private static DataBaseAction insert_pheno_data_01(List<Object> agme_Pheno_01s, java.sql.Connection connection, Date recv_time, String d_data_id, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		File file = new File(filepath);
		String fileName = file.getName();
		List<StatDi> listDi = new ArrayList<StatDi>();

		ReadIni ini = ReadIni.getIni();

		String insert_sql = ini.getValue(ReadIni.SECTION_PHENO_01, ReadIni.INSERT_SQL_KEY).toUpperCase().toUpperCase();
		// String d_data_id = ini.getValue(ReadIniPheno.SECTION_PHENO_01, ReadIniPheno.D_DATA_ID_KEY);
		PreparedStatement pStatement = null;
		try {
			StatDi di = null;

			// pStatement = connection.prepareStatement(insert_sql);
			pStatement = new LoggableStatement(connection, insert_sql);
			if (StartConfig.getDatabaseType() == 1) {
				pStatement.execute("select last_txc_xid()");
			}
			for (Object obj : agme_Pheno_01s) {
				Agme_Pheno_01 agme_Pheno_01 = (Agme_Pheno_01) obj;
				di = new StatDi();
				// 对DI和pStatement设置值
				generatePstAndDi_01(di, agme_Pheno_01, pStatement, recv_time, d_data_id,filepath,isUpdate,v_bbb,loggerBuffer);
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
				if (pStatement != null) {
					try {
						pStatement.close();
					} catch (SQLException e1) {
						loggerBuffer.append("\n close Statement error" + e1.getMessage());
					}
				}
				connection.setAutoCommit(true);
				pStatement = new LoggableStatement(connection, insert_sql);
//				pStatement.clearParameters();
//				pStatement.clearBatch();
				// 如果批量入库异常，执行单条数据入库
				// connection.setAutoCommit(true); //因为之前connection设置了非自动提交，转单条数据入库时，需设置回自动提交或最后手动提交
				loggerBuffer.append("\n Batch commit failed：" + fileName + "\n " + e.getMessage());
				for (Object obj : agme_Pheno_01s) {
					Agme_Pheno_01 agme_Pheno_01 = (Agme_Pheno_01) obj;
					insert_one_pheno_data_01(recv_time, connection, d_data_id, pStatement, agme_Pheno_01,filepath,isUpdate,v_bbb,loggerBuffer);
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
	 * @Title: insert_one_soil_data_01
	 * @Description: 单条数据入数据库
	 * @param recv_time
	 * @param connection
	 * @param d_data_id
	 * @param pStatement
	 * @param fileName 
	 * @param isUpdate 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @param agme_Soil_01
	 * @return void
	 * @throws:
	 */
	private static void insert_one_pheno_data_01(Date recv_time, java.sql.Connection connection, String d_data_id, PreparedStatement pStatement, Agme_Pheno_01 agme_Pheno_01, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		File file = new File(filepath);
		String fileName = file.getName();
		StatDi di = new StatDi();
		generatePstAndDi_01(di, agme_Pheno_01, pStatement, recv_time, d_data_id,filepath,isUpdate,v_bbb,loggerBuffer);
		try {
			pStatement.execute();
//			connection.commit();
		} catch (SQLException e1) {
			di.setPROCESS_STATE("1");// 0成功，1失败
			loggerBuffer.append("\n filename：" + fileName //
					+ "\n " + agme_Pheno_01.getStationNumberChina() + " " + TimeUtil.date2String(agme_Pheno_01.getAppearTime(), TimeUtil.DATE_FMT_YMDHMS) + " "
					+ agme_Pheno_01.getPlantOrAnimalName() + " " + agme_Pheno_01.getPhenologyName() //
					+ "\n execute sql error：" + ((LoggableStatement) pStatement).getQueryString() //
					+ "\n " + e1.getMessage());
		} finally {
			diQueues.offer(di);
		}
	}

	/**
	 * 
	 * @Title: generatePstAndDi_01
	 * @Description: 设置prepareStatement和DI值
	 * @param di DI对象
	 * @param agme_Pheno_01 自然物候气象要素数据01、02、04要素对象
	 * @param pStatement prepareStatement
	 * @param recv_time 收到时间
	 * @param fileName 
	 * @param isUpdate 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @param ini 配置文件信息
	 * @return void
	 * @throws:
	 */
	private static void generatePstAndDi_01(StatDi di, Agme_Pheno_01 agme_Pheno_01, PreparedStatement pStatement, Date recv_time, String d_data_id, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileName = file.getName();
		di.setFILE_NAME_O(fileName);
		di.setDATA_TYPE(d_data_id);
		di.setDATA_TYPE_1(ini.getValue(ReadIni.SECTION_PHENO_01, ReadIni.CTS_CODE_KEY));
		di.setTT("");
		di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
		di.setPROCESS_START_TIME(TimeUtil.getSysTime());
		di.setFILE_NAME_N(fileName);
		di.setBUSINESS_STATE("1"); // 1成功，0失败
		di.setPROCESS_STATE("1"); // 1成功，0失败

		// int stationNumberN = DEFAULT_VALUE;
		String stationNumberC = agme_Pheno_01.getStationNumberChina();
		// if (Pattern.matches("\\d{5}", stationNumberC)) { // 如果是5位数字
		// stationNumberN = Integer.parseInt(stationNumberC);
		// }
		String stationNumberN = StationCodeUtil.stringToAscii(stationNumberC);
		// 根据站号查询行政区划代码
		String info = (String) proMap.get(stationNumberC + "+12");
		String adminCode = "999999";
		if(info != null) {
			String[] infos = info.split(",");
			if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
				adminCode = infos[5];
		}
		
		if(adminCode.startsWith("999999")){
			info = (String) proMap.get(stationNumberC + "+01");
			if(info == null) {
				loggerBuffer.append("\n In the configuration file, the station number does not exist" + stationNumberC);
			}else {
				String[] infos = info.split(",");
				if(infos.length >= 6)
					adminCode = infos[5];
			}
		}

		int num = 1;
		// 入库时间
		Timestamp insertTime = new Timestamp(new Date().getTime());
		// 资料时间
		Date obsTime = agme_Pheno_01.getAppearTime();
		Timestamp d_datetime = new Timestamp(obsTime.getTime());

		Double latitude = agme_Pheno_01.getLatitude(); // 纬度
		Double longitude = agme_Pheno_01.getLongitude(); // 经度

		Double heightOfSationGroundAboveMeanSeaLevel = agme_Pheno_01.getHeightOfSationGroundAboveMeanSeaLevel();// 测站海拔高度
		Double heightOfBarometerAboveMeanSeaLevel = agme_Pheno_01.getHeightOfBarometerAboveMeanSeaLevel();// 气压传感器海拔高度

		Double plantOrAnimalName = agme_Pheno_01.getPlantOrAnimalName(); // 动植物名称
		Double phenologyName = agme_Pheno_01.getPhenologyName(); // 物候期名称
		String lengthStr = "00000";
		try {
			if (!isUpdate) {
				// 时间(yyyyMMddHHmmss)_微秒（毫秒转微秒）SSSSSS_站号5_经度10_纬度9_海拔或高度相关8_(V_BBB)_其他字段
				String id = TimeUtil.date2String(obsTime, TimeUtil.DATE_FMT_YMDHMS) //
						+ "_" + (lengthStr.substring(0, 5 - stationNumberC.length()) + stationNumberC) //
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(longitude * 100), 10) //
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(latitude * 100), 9)//
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(heightOfSationGroundAboveMeanSeaLevel * 10), 8)//
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(heightOfBarometerAboveMeanSeaLevel * 10), 8)//
						+ "_" + plantOrAnimalName + "_" + phenologyName;

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
				pStatement.setString(num++, stationNumberC); // 区站号（字符）
				// pStatement.setInt(num++, stationNumberN); // 区站号（数字）
				pStatement.setBigDecimal(num++, new BigDecimal(stationNumberN));
			}
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNineOnLatLon(ArrayUtils.contains(NINE_ARRAYS, latitude) ? latitude : ElementValUtil.getlatitude(String.valueOf(latitude)))); // 纬度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNineOnLatLon(ArrayUtils.contains(NINE_ARRAYS, longitude) ? longitude : ElementValUtil.getLongitude(String.valueOf(longitude)))); // 经度
//			pStatement.setBigDecimal(num++,
//					NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, heightOfSationGroundAboveMeanSeaLevel) ? heightOfSationGroundAboveMeanSeaLevel
//							: (heightOfSationGroundAboveMeanSeaLevel / 10.0))); // 测站海拔高度
			
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(heightOfSationGroundAboveMeanSeaLevel / 10.0))); // 测站海拔高度
			
//			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(
//					ArrayUtils.contains(NINE_ARRAYS, heightOfBarometerAboveMeanSeaLevel) ? heightOfBarometerAboveMeanSeaLevel : (heightOfBarometerAboveMeanSeaLevel / 10.0))); // 气压传感器海拔高度
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(heightOfBarometerAboveMeanSeaLevel.doubleValue() == 99999.0 ? 999999 : heightOfBarometerAboveMeanSeaLevel / 10.0))); // 气压传感器海拔高度

			// 中国行政区划代码
			pStatement.setString(num++, adminCode);

			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getYear(obsTime))); // 资料观测年
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getMonth(obsTime))); // 资料观测月
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getDayOfMonth(obsTime))); // 资料观测日
			if (!isUpdate) {
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(plantOrAnimalName)); // 动植物名称
			}
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(phenologyName)); // 物候期名称
			pStatement.setString(num++, v_bbb); // 默认000，首次入库可不填，数据库字段默认000

			if (isUpdate) {
				pStatement.setTimestamp(num++, d_datetime); // 资料时间
				pStatement.setString(num++, stationNumberC); // 区站号（字符）
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(plantOrAnimalName)); // 动植物名称
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(phenologyName));
			}
			if(!isUpdate)
				pStatement.setString(num++, ini.getValue(ReadIni.SECTION_PHENO_01, ReadIni.CTS_CODE_KEY));
			
		} catch (SQLException e) {
			loggerBuffer.append("\n Failed to set field values: " + e.getMessage());
		}

		di.setIIiii(stationNumberC);
		di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm"));
		di.setPROCESS_END_TIME(TimeUtil.getSysTime());
		di.setRECORD_TIME(TimeUtil.getSysTime());
		di.setLATITUDE(NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, latitude) ? latitude : ElementValUtil.getlatitude(String.valueOf(latitude))) + "");
		di.setLONGTITUDE(NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, longitude) ? longitude : ElementValUtil.getLongitude(String.valueOf(longitude))) + "");
		di.setDATA_UPDATE_FLAG(v_bbb);
		di.setHEIGHT(String.valueOf(heightOfSationGroundAboveMeanSeaLevel / 10.0));
		di.setSEND_PHYS("DRDS");
		di.setFILE_SIZE(String.valueOf(file.length()));
	}

	/**
	 * 
	 * @Title: update_pheno_data_01
	 * @Description: 更新数据库数据
	 * @param agme_Pheno_01s
	 * @param connection
	 * @param recv_time void
	 * @param fileName 
	 * @param isUpdate 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @throws:
	 */
	private static void update_pheno_data_01(List<Object> agme_Pheno_01s, java.sql.Connection connection, Date recv_time, String d_data_id, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {

		ReadIni ini = ReadIni.getIni();

		String update_sql = ini.getValue(ReadIni.SECTION_PHENO_01, ReadIni.UPDATE_SQL_KEY).toUpperCase();
		String insert_sql = ini.getValue(ReadIni.SECTION_PHENO_01, ReadIni.INSERT_SQL_KEY).toUpperCase();
		PreparedStatement pStatement = null;

		for (Object obj : agme_Pheno_01s) {
			Agme_Pheno_01 agme_Pheno_01 = (Agme_Pheno_01) obj;
			String db_v_bbb = find_pheno_data_01(agme_Pheno_01, connection,loggerBuffer); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if (db_v_bbb == null) { // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				isUpdate = false;
				try {
					pStatement = new LoggableStatement(connection, insert_sql);
					insert_one_pheno_data_01(recv_time, connection, d_data_id, pStatement, agme_Pheno_01,filepath,isUpdate,v_bbb,loggerBuffer);
				} catch (SQLException e) {
					e.printStackTrace();
				}finally {
					if (pStatement != null) {
						try {
							pStatement.close();
						} catch (SQLException e) {
							loggerBuffer.append("\n close Statement error" + e.getMessage());
						}
					}
				}
			} else if (db_v_bbb.compareTo(v_bbb) < 0) { // 数据库中有该条记录，且V_BBB的值早于当前处理文件的更正标识的值
				isUpdate = true;
				try {
					pStatement = new LoggableStatement(connection, update_sql);
					insert_one_pheno_data_01(recv_time, connection, d_data_id, pStatement, agme_Pheno_01,filepath,isUpdate,v_bbb,loggerBuffer);
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					if (pStatement != null) {
						try {
							pStatement.close();
						} catch (SQLException e) {
							loggerBuffer.append("\n close Statement error" + e.getMessage());
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
	 * @param loggerBuffer 
	 * @return V_BBB更正标识
	 * @throws:
	 */
	private static String find_pheno_data_01(Agme_Pheno_01 agme_Pheno_01, java.sql.Connection connection, StringBuffer loggerBuffer) {
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		ReadIni ini = ReadIni.getIni();
		String select_sql = ini.getValue(ReadIni.SECTION_PHENO_01, ReadIni.SELECT_SQL_KEY);
		try {

			pstmt = connection.prepareStatement(select_sql);
			int num = 1;
			Date obsTime = agme_Pheno_01.getAppearTime();

			pstmt.setTimestamp(num++, new Timestamp(obsTime.getTime())); // 时间
			pstmt.setString(num++, agme_Pheno_01.getStationNumberChina()); // 站号
			pstmt.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Pheno_01.getPlantOrAnimalName())); // 植(动)物名称
			pstmt.setBigDecimal(num ++, NumberUtil.FormatNumOrNine(agme_Pheno_01.getPhenologyName()));
			
			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {
				return resultSet.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(resultSet != null) {
					resultSet.close();
				}
			} catch (SQLException e) {
				loggerBuffer.append("\n close ResultSet failed!");
			}
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				loggerBuffer.append("\n close PreparedStatement failed!");
			}
		}
		return null;
	}

	/**
	 * 
	 * @Title: insert_soil_data_02
	 * @Description: 自然物候气象要素数据01、02、04类入库
	 * @param recv_time 收到时间
	 * @param connection 数据库连接
	 * @param agme_Pheno_02s 01、02、04类数据解析集合
	 * @param fileName 
	 * @param isUpdate 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @return DataBaseAction 成功与否标识
	 * @throws:
	 */
	private static DataBaseAction insert_pheno_data_02(List<Object> agme_Pheno_02s, java.sql.Connection connection, Date recv_time, String d_data_id, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		File file = new File(filepath);
		String fileName = file.getName();
		List<StatDi> listDi = new ArrayList<StatDi>();

		ReadIni ini = ReadIni.getIni();

		String insert_sql = ini.getValue(ReadIni.SECTION_PHENO_01, ReadIni.INSERT_SQL_KEY).toUpperCase();
		// String d_data_id = ini.getValue(ReadIniPheno.SECTION_PHENO_01, ReadIniPheno.D_DATA_ID_KEY);
		PreparedStatement pStatement = null;
		try {
			StatDi di = null;

			// pStatement = connection.prepareStatement(insert_sql);
			pStatement = new LoggableStatement(connection, insert_sql);
			if (StartConfig.getDatabaseType() == 1) {
				pStatement.execute("select last_txc_xid()");
			}
			for (Object obj : agme_Pheno_02s) {
				Agme_Pheno_02 agme_Pheno_02 = (Agme_Pheno_02) obj;
				di = new StatDi();
				// 对DI和pStatement设置值
				generatePstAndDi_02(di, agme_Pheno_02, pStatement, recv_time, d_data_id,filepath,isUpdate,v_bbb,loggerBuffer);
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
				if (pStatement != null) {
					try {
						pStatement.close();
					} catch (SQLException e1) {
						loggerBuffer.append("\n close Statement error" + e1.getMessage());
					}
				}
				connection.setAutoCommit(true);
				pStatement = new LoggableStatement(connection, insert_sql);
//				pStatement.clearParameters();
//				pStatement.clearBatch();
				// 如果批量入库异常，执行单条数据入库
				// connection.setAutoCommit(true); //因为之前connection设置了非自动提交，转单条数据入库时，需设置回自动提交或最后手动提交
				loggerBuffer.append("\n Batch commit failed：" + fileName + "\n " + e.getMessage());
				for (Object obj : agme_Pheno_02s) {
					Agme_Pheno_02 agme_Pheno_02 = (Agme_Pheno_02) obj;
					insert_one_pheno_data_02(recv_time, connection, d_data_id, pStatement, agme_Pheno_02,filepath,isUpdate,v_bbb,loggerBuffer);
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
	 * @Title: insert_one_soil_data_02
	 * @Description: 单条数据入数据库
	 * @param recv_time
	 * @param connection
	 * @param d_data_id
	 * @param pStatement
	 * @param isUpdate 
	 * @param fileName 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @param agme_Soil_01
	 * @return void
	 * @throws:
	 */
	private static void insert_one_pheno_data_02(Date recv_time, java.sql.Connection connection, String d_data_id, PreparedStatement pStatement, Agme_Pheno_02 agme_Pheno_02, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		File file = new File(filepath);
		String fileName = file.getName();
		StatDi di = new StatDi();
		generatePstAndDi_02(di, agme_Pheno_02, pStatement, recv_time, d_data_id,filepath,isUpdate,v_bbb,loggerBuffer);
		try {
			pStatement.execute();
//			connection.commit();
		} catch (SQLException e1) {
			di.setPROCESS_STATE("0");// 1成功，0失败
			loggerBuffer.append("\n filename：" + fileName //
					+ "\n " + agme_Pheno_02.getStationNumberChina() + " " + TimeUtil.date2String(agme_Pheno_02.getAppearTime(), TimeUtil.DATE_FMT_YMDHMS) + " "
					+ agme_Pheno_02.getPlantOrAnimalName() + " " + agme_Pheno_02.getPhenologyName() //
					+ "\n execute sql error：" + ((LoggableStatement) pStatement).getQueryString() //
					+ "\n " + e1.getMessage());
		} finally {
			diQueues.offer(di);
		}
	}

	/**
	 * 
	 * @Title: generatePstAndDi_02
	 * @Description: 设置prepareStatement和DI值
	 * @param di DI对象
	 * @param agme_Pheno_02 自然物候气象要素数据01、02、04要素对象
	 * @param pStatement prepareStatement
	 * @param recv_time 收到时间
	 * @param fileName 
	 * @param isUpdate 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @param ini 配置文件信息
	 * @return void
	 * @throws:
	 */
	private static void generatePstAndDi_02(StatDi di, Agme_Pheno_02 agme_Pheno_02, PreparedStatement pStatement, Date recv_time, String d_data_id, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileName = file.getName();
		di.setFILE_NAME_O(fileName);
		di.setDATA_TYPE(d_data_id);
		di.setDATA_TYPE_1(ini.getValue(ReadIni.SECTION_PHENO_02, ReadIni.CTS_CODE_KEY));
		di.setTT("");
		di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
		di.setPROCESS_START_TIME(TimeUtil.getSysTime());
		di.setFILE_NAME_N(fileName);
		di.setBUSINESS_STATE("1"); // 1成功，0失败
		di.setPROCESS_STATE("1"); // 1成功，0失败

		// int stationNumberN = DEFAULT_VALUE;
		String stationNumberC = agme_Pheno_02.getStationNumberChina();
		// if (Pattern.matches("\\d{5}", stationNumberC)) { // 如果是5位数字
		// stationNumberN = Integer.parseInt(stationNumberC);
		// }
		String stationNumberN = StationCodeUtil.stringToAscii(stationNumberC);
		// 根据站号查询行政区划代码
		String info = (String) proMap.get(stationNumberC + "+12");
		String adminCode = "999999";
		if(info != null) {
			String[] infos = info.split(",");
			if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
				adminCode = infos[5];
		}
		
		if(adminCode.startsWith("999999")){
			info = (String) proMap.get(stationNumberC + "+01");
			if(info == null) {
				loggerBuffer.append("\n In the configuration file, the station number does not exist" + stationNumberC);
			}else {
				String[] infos = info.split(",");
				if(infos.length >= 6)
					adminCode = infos[5];
			}
		}

		int num = 1;
		// 入库时间
		Timestamp insertTime = new Timestamp(new Date().getTime());
		// 资料时间
		Date obsTime = agme_Pheno_02.getAppearTime();
		Timestamp d_datetime = new Timestamp(obsTime.getTime());

		Double latitude = agme_Pheno_02.getLatitude(); // 纬度
		Double longitude = agme_Pheno_02.getLongitude(); // 经度

		Double heightOfSationGroundAboveMeanSeaLevel = agme_Pheno_02.getHeightOfSationGroundAboveMeanSeaLevel();// 测站海拔高度
		Double heightOfBarometerAboveMeanSeaLevel = agme_Pheno_02.getHeightOfBarometerAboveMeanSeaLevel();// 气压传感器海拔高度

		Double plantOrAnimalName = agme_Pheno_02.getPlantOrAnimalName(); // 动植物名称
		Double phenologyName = agme_Pheno_02.getPhenologyName(); // 物候期名称
		String lengthStr = "00000";
		try {
			if (!isUpdate) {
				// 时间(yyyyMMddHHmmss)_微秒（毫秒转微秒）SSSSSS_站号5_经度10_纬度9_海拔或高度相关8_(V_BBB)_其他字段
				String id = TimeUtil.date2String(obsTime, TimeUtil.DATE_FMT_YMDHMS) //
						+ "_" + (lengthStr.substring(0, 5 - stationNumberC.length()) + stationNumberC) //
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(longitude * 100), 10) //
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(latitude * 100), 9)//
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(heightOfSationGroundAboveMeanSeaLevel * 10), 8)//
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(heightOfBarometerAboveMeanSeaLevel * 10), 8)//
						+ "_" + plantOrAnimalName + "_" + phenologyName;

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
				pStatement.setString(num++, stationNumberC); // 区站号（字符）
				// pStatement.setInt(num++, stationNumberN); // 区站号（数字）
				pStatement.setBigDecimal(num++, new BigDecimal(stationNumberN));
			}
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNineOnLatLon(ArrayUtils.contains(NINE_ARRAYS, latitude) ? latitude : ElementValUtil.getlatitude(String.valueOf(latitude)))); // 纬度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNineOnLatLon(ArrayUtils.contains(NINE_ARRAYS, longitude) ? longitude : ElementValUtil.getLongitude(String.valueOf(longitude)))); // 经度
//			pStatement.setBigDecimal(num++,
//					NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, heightOfSationGroundAboveMeanSeaLevel) ? heightOfSationGroundAboveMeanSeaLevel
//							: (heightOfSationGroundAboveMeanSeaLevel / 10.0))); // 测站海拔高度
			
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(heightOfSationGroundAboveMeanSeaLevel / 10.0))); // 测站海拔高度
			
//			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(
//					ArrayUtils.contains(NINE_ARRAYS, heightOfBarometerAboveMeanSeaLevel) ? heightOfBarometerAboveMeanSeaLevel : (heightOfBarometerAboveMeanSeaLevel / 10.0))); // 气压传感器海拔高度
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(heightOfBarometerAboveMeanSeaLevel.doubleValue() == 99999.0 ? 999999 : heightOfBarometerAboveMeanSeaLevel / 10.0))); // 气压传感器海拔高度
			// 中国行政区划代码
			pStatement.setString(num++, adminCode);

			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getYear(obsTime))); // 资料观测年
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getMonth(obsTime))); // 资料观测月
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getDayOfMonth(obsTime))); // 资料观测日
			if (!isUpdate) {
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(plantOrAnimalName)); // 动植物名称
			}
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(phenologyName)); // 物候期名称
			pStatement.setString(num++, v_bbb); // 默认000，首次入库可不填，数据库字段默认000

			if (isUpdate) {
				pStatement.setTimestamp(num++, d_datetime); // 资料时间
				pStatement.setString(num++, stationNumberC); // 区站号（字符）
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(plantOrAnimalName)); // 动植物名称
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(phenologyName));
			}
			if(!isUpdate)
				pStatement.setString(num++, ini.getValue(ReadIni.SECTION_PHENO_02, ReadIni.CTS_CODE_KEY));
		} catch (SQLException e) {
			loggerBuffer.append("\n Failed to set field values: " + e.getMessage());
		}

		di.setIIiii(stationNumberC);
		di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm"));
		di.setPROCESS_END_TIME(TimeUtil.getSysTime());
		di.setRECORD_TIME(TimeUtil.getSysTime());
		di.setLATITUDE(NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, latitude) ? latitude : ElementValUtil.getlatitude(String.valueOf(latitude))) + "");
		di.setLONGTITUDE(NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, longitude) ? longitude : ElementValUtil.getLongitude(String.valueOf(longitude))) + "");
		di.setDATA_UPDATE_FLAG(v_bbb);
		di.setHEIGHT(String.valueOf(heightOfSationGroundAboveMeanSeaLevel / 10.0));
		di.setSEND_PHYS("DRDS");
		di.setFILE_SIZE(String.valueOf(file.length()));
	}

	/**
	 * 
	 * @Title: update_pheno_data_02
	 * @Description: 更新数据库数据
	 * @param agme_Pheno_02s
	 * @param connection
	 * @param recv_time void
	 * @param isUpdate 
	 * @param fileName 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @throws:
	 */
	private static void update_pheno_data_02(List<Object> agme_Pheno_02s, java.sql.Connection connection, Date recv_time, String d_data_id, boolean isUpdate, String filepath, String v_bbb, StringBuffer loggerBuffer) {

		ReadIni ini = ReadIni.getIni();

		String update_sql = ini.getValue(ReadIni.SECTION_PHENO_01, ReadIni.UPDATE_SQL_KEY).toUpperCase();
		String insert_sql = ini.getValue(ReadIni.SECTION_PHENO_01, ReadIni.INSERT_SQL_KEY).toUpperCase();

		PreparedStatement pStatement = null;

		for (Object obj : agme_Pheno_02s) {
			Agme_Pheno_02 agme_Pheno_02 = (Agme_Pheno_02) obj;
			String db_v_bbb = find_pheno_data_02(agme_Pheno_02, connection,loggerBuffer); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if (db_v_bbb == null) { // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				isUpdate = false;
				try {
					pStatement = new LoggableStatement(connection, insert_sql);
					insert_one_pheno_data_02(recv_time, connection, d_data_id, pStatement, agme_Pheno_02,filepath,isUpdate,v_bbb,loggerBuffer);
				} catch (SQLException e) {
					e.printStackTrace();
				}finally {
					if (pStatement != null) {
						try {
							pStatement.close();
						} catch (SQLException e) {
							loggerBuffer.append("\n close Statement error" + e.getMessage());
						}
					}
				}
			} else if (db_v_bbb.compareTo(v_bbb) < 0) { // 数据库中有该条记录，且V_BBB的值早于当前处理文件的更正标识的值
				isUpdate = true;
				try {
					pStatement = new LoggableStatement(connection, update_sql);
					insert_one_pheno_data_02(recv_time, connection, d_data_id, pStatement, agme_Pheno_02,filepath,isUpdate,v_bbb,loggerBuffer);
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					if (pStatement != null) {
						try {
							pStatement.close();
						} catch (SQLException e) {
							loggerBuffer.append("\n close  Statement error" + e.getMessage());
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
	 * @Title: find_pheno_data_02
	 * @Description: 查询数据表中的更正标识
	 * @param agme_Pheno_02
	 * @param connection
	 * @param loggerBuffer 
	 * @return V_BBB更正标识
	 * @throws:
	 */
	private static String find_pheno_data_02(Agme_Pheno_02 agme_Pheno_02, java.sql.Connection connection, StringBuffer loggerBuffer) {
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		ReadIni ini = ReadIni.getIni();
		String select_sql = ini.getValue(ReadIni.SECTION_PHENO_01, ReadIni.SELECT_SQL_KEY);
		try {

			pstmt = connection.prepareStatement(select_sql);
			int num = 1;
			Date obsTime = agme_Pheno_02.getAppearTime();

			pstmt.setTimestamp(num++, new Timestamp(obsTime.getTime())); // 时间
			pstmt.setString(num++, agme_Pheno_02.getStationNumberChina()); // 站号
			pstmt.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Pheno_02.getPlantOrAnimalName())); // 植(动)物名称
			pstmt.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Pheno_02.getPhenologyName()));
			
			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {
				return resultSet.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(resultSet != null) {
					resultSet.close();
				}
			} catch (SQLException e) {
				loggerBuffer.append("\n close ResultSet failed!");
			}
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				loggerBuffer.append("\n close PreparedStatement failed!");
			}
		}
		return null;
	}

	/**
	 * 
	 * @Title: insert_soil_data_04
	 * @Description: 自然物候气象要素数据01、02、04类入库
	 * @param recv_time 收到时间
	 * @param connection 数据库连接
	 * @param agme_Pheno_04s 01、02、04类数据解析集合
	 * @param fileName 
	 * @param isUpdate 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @return DataBaseAction 成功与否标识
	 * @throws:
	 */
	private static DataBaseAction insert_pheno_data_04(List<Object> agme_Pheno_04s, java.sql.Connection connection, Date recv_time, String d_data_id, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		File file = new File(filepath);
		String fileName = file.getName();
		List<StatDi> listDi = new ArrayList<StatDi>();

		ReadIni ini = ReadIni.getIni();

		String insert_sql = ini.getValue(ReadIni.SECTION_PHENO_01, ReadIni.INSERT_SQL_KEY).toUpperCase();
		// String d_data_id = ini.getValue(ReadIniPheno.SECTION_PHENO_01, ReadIniPheno.D_DATA_ID_KEY);
		PreparedStatement pStatement = null;
		try {
			StatDi di = null;

			// pStatement = connection.prepareStatement(insert_sql);
			pStatement = new LoggableStatement(connection, insert_sql);
			if (StartConfig.getDatabaseType() == 1) {
				pStatement.execute("select last_txc_xid()");
			}
			for (Object obj : agme_Pheno_04s) {
				Agme_Pheno_04 agme_Pheno_04 = (Agme_Pheno_04) obj;
				di = new StatDi();
				// 对DI和pStatement设置值
				generatePstAndDi_04(di, agme_Pheno_04, pStatement, recv_time, d_data_id,filepath,isUpdate,v_bbb,loggerBuffer);
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
				if (pStatement != null) {
					try {
						pStatement.close();
					} catch (SQLException e1) {
						loggerBuffer.append("\n close Statement error " + e1.getMessage());
					}
				}
				connection.setAutoCommit(true);
				pStatement = new LoggableStatement(connection, insert_sql);
//				pStatement.clearParameters();
//				pStatement.clearBatch();
				// 如果批量入库异常，执行单条数据入库
				// connection.setAutoCommit(true); //因为之前connection设置了非自动提交，转单条数据入库时，需设置回自动提交或最后手动提交
				loggerBuffer.append("\n Batch commit failed：" + fileName + "\n " + e.getMessage());
				for (Object obj : agme_Pheno_04s) {
					Agme_Pheno_04 agme_Pheno_04 = (Agme_Pheno_04) obj;
					insert_one_pheno_data_04(recv_time, connection, d_data_id, pStatement, agme_Pheno_04,filepath,isUpdate,v_bbb,loggerBuffer);
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
	 * @Title: insert_one_soil_data_04
	 * @Description: 单条数据入数据库
	 * @param recv_time
	 * @param connection
	 * @param d_data_id
	 * @param pStatement
	 * @param isUpdate 
	 * @param fileName 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @param agme_Soil_01
	 * @return void
	 * @throws:
	 */
	private static void insert_one_pheno_data_04(Date recv_time, java.sql.Connection connection, String d_data_id, PreparedStatement pStatement, Agme_Pheno_04 agme_Pheno_04, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		File file = new File(filepath);
		String fileName = file.getName();
		StatDi di = new StatDi();
		generatePstAndDi_04(di, agme_Pheno_04, pStatement, recv_time, d_data_id,filepath,isUpdate,v_bbb,loggerBuffer);
		try {
			pStatement.execute();
//			connection.commit();
		} catch (SQLException e1) {
			di.setPROCESS_STATE("1");// 0成功，1失败
			loggerBuffer.append("\n filename：" + fileName //
					+ "\n " + agme_Pheno_04.getStationNumberChina() + " " + TimeUtil.date2String(agme_Pheno_04.getAppearTime(), TimeUtil.DATE_FMT_YMDHMS) + " "
					+ agme_Pheno_04.getPlantOrAnimalName() + " " + agme_Pheno_04.getPhenologyName() //
					+ "\n  execute sql error：" + ((LoggableStatement) pStatement).getQueryString() //
					+ "\n " + e1.getMessage());
		} finally {
			diQueues.offer(di);
		}
	}

	/**
	 * 
	 * @Title: generatePstAndDi_04
	 * @Description: 设置prepareStatement和DI值
	 * @param di DI对象
	 * @param agme_Pheno_04 自然物候气象要素数据01、02、04要素对象
	 * @param pStatement prepareStatement
	 * @param recv_time 收到时间
	 * @param fileName 
	 * @param isUpdate 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @param ini 配置文件信息
	 * @return void
	 * @throws:
	 */
	private static void generatePstAndDi_04(StatDi di, Agme_Pheno_04 agme_Pheno_04, PreparedStatement pStatement, Date recv_time, String d_data_id, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileName = file.getName();
		di.setFILE_NAME_O(fileName);
		di.setDATA_TYPE(d_data_id);
		di.setDATA_TYPE_1(ini.getValue(ReadIni.SECTION_PHENO_04, ReadIni.CTS_CODE_KEY));
		di.setTT("");
		di.setTRAN_TIME(TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm"));
		di.setPROCESS_START_TIME(TimeUtil.getSysTime());
		di.setFILE_NAME_N(fileName);
		di.setBUSINESS_STATE("1"); // 1成功，0失败
		di.setPROCESS_STATE("1"); // 1成功，0失败

		// int stationNumberN = DEFAULT_VALUE;
		String stationNumberC = agme_Pheno_04.getStationNumberChina();
		// if (Pattern.matches("\\d{5}", stationNumberC)) { // 如果是5位数字
		// stationNumberN = Integer.parseInt(stationNumberC);
		// }
		String stationNumberN = StationCodeUtil.stringToAscii(stationNumberC);
		// 根据站号查询行政区划代码
		String info = (String) proMap.get(stationNumberC + "+12");
		String adminCode = "999999";
		if(info != null) {
			String[] infos = info.split(",");
			if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
				adminCode = infos[5];
		}
		
		if(adminCode.startsWith("999999")){
			info = (String) proMap.get(stationNumberC + "+01");
			if(info == null) {
				loggerBuffer.append("\n In the configuration file, the station number does not exist" + stationNumberC);
			}else {
				String[] infos = info.split(",");
				if(infos.length >= 6)
					adminCode = infos[5];
			}
		}

		int num = 1;
		// 入库时间
		Timestamp insertTime = new Timestamp(new Date().getTime());
		// 资料时间
		Date obsTime = agme_Pheno_04.getAppearTime();
		Timestamp d_datetime = new Timestamp(obsTime.getTime());

		Double latitude = agme_Pheno_04.getLatitude(); // 纬度
		Double longitude = agme_Pheno_04.getLongitude(); // 经度

		Double heightOfSationGroundAboveMeanSeaLevel = agme_Pheno_04.getHeightOfSationGroundAboveMeanSeaLevel();// 测站海拔高度
		Double heightOfBarometerAboveMeanSeaLevel = agme_Pheno_04.getHeightOfBarometerAboveMeanSeaLevel();// 气压传感器海拔高度

		Double plantOrAnimalName = agme_Pheno_04.getPlantOrAnimalName(); // 动植物名称
		Double phenologyName = agme_Pheno_04.getPhenologyName(); // 物候期名称
		String lengthStr = "00000";
		try {
			if (!isUpdate) {
				// 时间(yyyyMMddHHmmss)_微秒（毫秒转微秒）SSSSSS_站号5_经度10_纬度9_海拔或高度相关8_(V_BBB)_其他字段
				String id = TimeUtil.date2String(obsTime, TimeUtil.DATE_FMT_YMDHMS) //
						+ "_" + (lengthStr.substring(0, 5 - stationNumberC.length()) + stationNumberC) //
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(longitude * 100), 10) //
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(latitude * 100), 9)//
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(heightOfSationGroundAboveMeanSeaLevel * 10), 8)//
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(heightOfBarometerAboveMeanSeaLevel * 10), 8)//
						+ "_" + plantOrAnimalName + "_" + phenologyName;

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
				pStatement.setString(num++, stationNumberC); // 区站号（字符）
				// pStatement.setInt(num++, stationNumberN); // 区站号（数字）
				pStatement.setBigDecimal(num++, new BigDecimal(stationNumberN));
			}
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNineOnLatLon(ArrayUtils.contains(NINE_ARRAYS, latitude) ? latitude : ElementValUtil.getlatitude(String.valueOf(latitude)))); // 纬度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNineOnLatLon(ArrayUtils.contains(NINE_ARRAYS, longitude) ? longitude : ElementValUtil.getLongitude(String.valueOf(longitude)))); // 经度
//			pStatement.setBigDecimal(num++,
//					NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, heightOfSationGroundAboveMeanSeaLevel) ? heightOfSationGroundAboveMeanSeaLevel
//							: (heightOfSationGroundAboveMeanSeaLevel / 10.0))); // 测站海拔高度
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(heightOfSationGroundAboveMeanSeaLevel / 10.0))); // 测站海拔高度
			
//			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(
//					ArrayUtils.contains(NINE_ARRAYS, heightOfBarometerAboveMeanSeaLevel) ? heightOfBarometerAboveMeanSeaLevel : (heightOfBarometerAboveMeanSeaLevel / 10.0))); // 气压传感器海拔高度
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(heightOfBarometerAboveMeanSeaLevel.doubleValue() == 99999.0 ? 999999 : heightOfBarometerAboveMeanSeaLevel / 10.0))); // 气压传感器海拔高度
			// 中国行政区划代码
			pStatement.setString(num++, adminCode);

			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getYear(obsTime))); // 资料观测年
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getMonth(obsTime))); // 资料观测月
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getDayOfMonth(obsTime))); // 资料观测日
			if (!isUpdate) {
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(plantOrAnimalName)); // 动植物名称
			}
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(phenologyName)); // 物候期名称
			pStatement.setString(num++, v_bbb); // 默认000，首次入库可不填，数据库字段默认000

			if (isUpdate) {
				pStatement.setTimestamp(num++, d_datetime); // 资料时间
				pStatement.setString(num++, stationNumberC); // 区站号（字符）
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(plantOrAnimalName)); // 动植物名称
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(phenologyName));
			}
			if(!isUpdate)
				pStatement.setString(num++, ini.getValue(ReadIni.SECTION_PHENO_04, ReadIni.CTS_CODE_KEY));
		} catch (SQLException e) {
			loggerBuffer.append("\n Failed to set field values: " + e.getMessage());
		}

		di.setIIiii(stationNumberC);
		di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm"));
		di.setPROCESS_END_TIME(TimeUtil.getSysTime());
		di.setRECORD_TIME(TimeUtil.getSysTime());
		di.setLATITUDE(NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, latitude) ? latitude : ElementValUtil.getlatitude(String.valueOf(latitude))) + "");
		di.setLONGTITUDE(NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, longitude) ? longitude : ElementValUtil.getLongitude(String.valueOf(longitude))) + "");
		di.setDATA_UPDATE_FLAG(v_bbb);
		di.setHEIGHT(String.valueOf(heightOfSationGroundAboveMeanSeaLevel / 10.0));
		di.setSEND_PHYS("DRDS");
		di.setFILE_SIZE(String.valueOf(file.length()));
	}

	/**
	 * 
	 * @Title: update_pheno_data_04
	 * @Description: 更新数据库数据
	 * @param agme_Pheno_04s
	 * @param connection
	 * @param recv_time void
	 * @param isUpdate 
	 * @param fileName 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @throws:
	 */
	private static void update_pheno_data_04(List<Object> agme_Pheno_04s, java.sql.Connection connection, Date recv_time, String d_data_id, boolean isUpdate, String filepath, String v_bbb, StringBuffer loggerBuffer) {

		ReadIni ini = ReadIni.getIni();

		String update_sql = ini.getValue(ReadIni.SECTION_PHENO_01, ReadIni.UPDATE_SQL_KEY).toUpperCase();
		String insert_sql = ini.getValue(ReadIni.SECTION_PHENO_01, ReadIni.INSERT_SQL_KEY).toUpperCase();

		PreparedStatement pStatement = null;

		for (Object obj : agme_Pheno_04s) {
			Agme_Pheno_04 agme_Pheno_04 = (Agme_Pheno_04) obj;
			String db_v_bbb = find_pheno_data_04(agme_Pheno_04, connection,loggerBuffer); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if (db_v_bbb == null) { // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				isUpdate = false;
				try {
					pStatement = new LoggableStatement(connection, insert_sql);
					insert_one_pheno_data_04(recv_time, connection, d_data_id, pStatement, agme_Pheno_04,filepath,isUpdate,v_bbb,loggerBuffer);
				} catch (SQLException e) {
					e.printStackTrace();
				}finally {
					if (pStatement != null) {
						try {
							pStatement.close();
						} catch (SQLException e) {
							loggerBuffer.append("\n close Statement error" + e.getMessage());
						}
					}
				}
			} else if (db_v_bbb.compareTo(v_bbb) < 0) { // 数据库中有该条记录，且V_BBB的值早于当前处理文件的更正标识的值
				isUpdate = true;
				try {
					pStatement = new LoggableStatement(connection, update_sql);
					insert_one_pheno_data_04(recv_time, connection, d_data_id, pStatement, agme_Pheno_04,filepath,isUpdate,v_bbb,loggerBuffer);
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					if (pStatement != null) {
						try {
							pStatement.close();
						} catch (SQLException e) {
							loggerBuffer.append("\n close Statement error" + e.getMessage());
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
	 * @Title: find_pheno_data_04
	 * @Description: 查询数据表中的更正标识
	 * @param agme_Pheno_04
	 * @param connection
	 * @param loggerBuffer 
	 * @return V_BBB更正标识
	 * @throws:
	 */
	private static String find_pheno_data_04(Agme_Pheno_04 agme_Pheno_04, java.sql.Connection connection, StringBuffer loggerBuffer) {
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		ReadIni ini = ReadIni.getIni();
		String select_sql = ini.getValue(ReadIni.SECTION_PHENO_01, ReadIni.SELECT_SQL_KEY);
		try {

			pstmt = connection.prepareStatement(select_sql);
			int num = 1;
			Date obsTime = agme_Pheno_04.getAppearTime();

			pstmt.setTimestamp(num++, new Timestamp(obsTime.getTime())); // 时间
			pstmt.setString(num++, agme_Pheno_04.getStationNumberChina()); // 站号
			pstmt.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Pheno_04.getPlantOrAnimalName())); // 植(动)物名称
			pstmt.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Pheno_04.getPhenologyName()));
			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {
				return resultSet.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(resultSet != null) {
					resultSet.close();
				}
			} catch (SQLException e) {
				loggerBuffer.append("\n close ResultSet failed!");
			}
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				loggerBuffer.append("\n close PreparedStatement failed!");
			}
		}
		return null;
	}

	/**
	 * 
	 * @Title: insert_soil_data_03
	 * @Description: 自然物候气象要素数据03类入库
	 * @param recv_time 收到时间
	 * @param connection 数据库连接
	 * @param agme_Pheno_03s 03类数据解析集合
	 * @param fileName 
	 * @param isUpdate 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @return DataBaseAction 成功与否标识
	 * @throws:
	 */
	private static DataBaseAction insert_pheno_data_03(List<Object> agme_Pheno_03s, java.sql.Connection connection, Date recv_time, String d_data_id, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		File file = new File(filepath);
		String fileName = file.getName();
		List<StatDi> listDi = new ArrayList<StatDi>();

		ReadIni ini = ReadIni.getIni();

		String insert_sql = ini.getValue(ReadIni.SECTION_PHENO_03, ReadIni.INSERT_SQL_KEY).toUpperCase();
		PreparedStatement pStatement = null;
		try {
			StatDi di = null;

			// pStatement = connection.prepareStatement(insert_sql);
			pStatement = new LoggableStatement(connection, insert_sql);
			if (StartConfig.getDatabaseType() == 1) {
				pStatement.execute("select last_txc_xid()");
			}
			for (Object obj : agme_Pheno_03s) {
				Agme_Pheno_03 agme_Pheno_03 = (Agme_Pheno_03) obj;
				di = new StatDi();
				// 对DI和pStatement设置值
				generatePstAndDi_03(di, agme_Pheno_03, pStatement, recv_time, d_data_id,filepath,isUpdate,v_bbb,loggerBuffer);
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
				if (pStatement != null) {
					try {
						pStatement.close();
					} catch (SQLException e1) {
						loggerBuffer.append("\n close Statement error" + e1.getMessage());
					}
				}
				connection.setAutoCommit(true);
				pStatement = new LoggableStatement(connection, insert_sql);
//				pStatement.clearParameters();
//				pStatement.clearBatch();
				// 如果批量入库异常，执行单条数据入库
				// connection.setAutoCommit(true); //因为之前connection设置了非自动提交，转单条数据入库时，需设置回自动提交或最后手动提交
				loggerBuffer.append("\n Batch commit failed：" + fileName + "\n " + e.getMessage());
				for (Object obj : agme_Pheno_03s) {
					Agme_Pheno_03 agme_Pheno_03 = (Agme_Pheno_03) obj;
					insert_one_pheno_data_03(recv_time, connection, d_data_id, pStatement, agme_Pheno_03,filepath,isUpdate,v_bbb,loggerBuffer);
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
	 * @Title: insert_one_soil_data_03
	 * @Description: 单条数据入数据库
	 * @param recv_time
	 * @param connection
	 * @param d_data_id
	 * @param pStatement
	 * @param agme_Pheno_03
	 * @param isUpdate 
	 * @param fileName 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @return void
	 * @throws:
	 */
	private static void insert_one_pheno_data_03(Date recv_time, java.sql.Connection connection, String d_data_id, PreparedStatement pStatement, Agme_Pheno_03 agme_Pheno_03, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		File file = new File(filepath);
		String fileName = file.getName();
		StatDi di = new StatDi();
		generatePstAndDi_03(di, agme_Pheno_03, pStatement, recv_time, d_data_id,filepath,isUpdate,v_bbb,loggerBuffer);
		try {
			pStatement.execute();
//			connection.commit();
		} catch (SQLException e1) {
			di.setPROCESS_STATE("1");// 0成功，1失败
			loggerBuffer.append("\n filename：" + fileName //
					+ "\n " + agme_Pheno_03.getStationNumberChina() + " " + TimeUtil.date2String(agme_Pheno_03.getAppearTime(), TimeUtil.DATE_FMT_YMDHMS) + " "
					+ agme_Pheno_03.getHydrologicalPhenomenonName() //
					+ "\n execute sql error：" + ((LoggableStatement) pStatement).getQueryString() //
					+ "\n " + e1.getMessage());
		} finally {
			diQueues.offer(di);
		}
	}

	/**
	 * 
	 * @Title: generatePstAndDi_03
	 * @Description: 设置prepareStatement和DI值
	 * @param di DI对象
	 * @param agme_Pheno_03 自然物候气象要素数据03要素对象
	 * @param pStatement prepareStatement
	 * @param recv_time 收到时间
	 * @param fileName 
	 * @param isUpdate 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @param ini 配置文件信息
	 * @return void
	 * @throws:
	 */
	private static void generatePstAndDi_03(StatDi di, Agme_Pheno_03 agme_Pheno_03, PreparedStatement pStatement, Date recv_time, String d_data_id, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileName = file.getName();
		di.setFILE_NAME_O(fileName);
		di.setDATA_TYPE(d_data_id);
		di.setDATA_TYPE_1(ini.getValue(ReadIni.SECTION_PHENO_03, ReadIni.CTS_CODE_KEY));
		di.setTT("");
		di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
		di.setPROCESS_START_TIME(TimeUtil.getSysTime());
		di.setFILE_NAME_N(fileName);
		di.setBUSINESS_STATE("1"); // 1成功，0失败
		di.setPROCESS_STATE("1"); // 1成功，0失败

		// int stationNumberN = DEFAULT_VALUE;
		String stationNumberC = agme_Pheno_03.getStationNumberChina();
		// if (Pattern.matches("\\d{5}", stationNumberC)) { // 如果是5位数字
		// stationNumberN = Integer.parseInt(stationNumberC);
		// }
		String stationNumberN = StationCodeUtil.stringToAscii(stationNumberC);
		// 根据站号查询行政区划代码
		String info = (String) proMap.get(stationNumberC + "+12");
		String adminCode = "999999";
		if(info != null) {
			String[] infos = info.split(",");
			if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
				adminCode = infos[5];
		}
		
		if(adminCode.startsWith("999999")){
			info = (String) proMap.get(stationNumberC + "+01");
			if(info == null) {
				loggerBuffer.append("\n In the configuration file, the station number does not exist" + stationNumberC);
			}else {
				String[] infos = info.split(",");
				if(infos.length >= 6)
					adminCode = infos[5];
			}
		}

		int num = 1;
		// 入库时间
		Timestamp insertTime = new Timestamp(new Date().getTime());
		// 资料时间
		Date obsTime = agme_Pheno_03.getAppearTime();
		Timestamp d_datetime = new Timestamp(obsTime.getTime());

		Double latitude = agme_Pheno_03.getLatitude(); // 纬度
		Double longitude = agme_Pheno_03.getLongitude(); // 经度

		Double heightOfSationGroundAboveMeanSeaLevel = agme_Pheno_03.getHeightOfSationGroundAboveMeanSeaLevel();// 测站海拔高度
		Double heightOfBarometerAboveMeanSeaLevel = agme_Pheno_03.getHeightOfBarometerAboveMeanSeaLevel();// 气压传感器海拔高度

		Double hydrologicalPhenomenonName = agme_Pheno_03.getHydrologicalPhenomenonName(); // 水文现象名称
		String lengthStr = "00000";
		try {
			if (!isUpdate) {
				// 时间(yyyyMMddHHmmss)_微秒（毫秒转微秒）SSSSSS_站号5_经度10_纬度9_海拔或高度相关8_(V_BBB)_其他字段
				String id = TimeUtil.date2String(obsTime, TimeUtil.DATE_FMT_YMDHMS) //
						+ "_" + (lengthStr.substring(0, 5 - stationNumberC.length()) + stationNumberC) //
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(longitude * 100), 10) //
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(latitude * 100), 9)//
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(heightOfSationGroundAboveMeanSeaLevel * 10), 8)//
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(heightOfBarometerAboveMeanSeaLevel * 10), 8) //
						+ "_" + hydrologicalPhenomenonName;

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
				pStatement.setString(num++, stationNumberC); // 区站号（字符）
				// pStatement.setInt(num++, stationNumberN); // 区站号（数字）
				pStatement.setBigDecimal(num++, new BigDecimal(stationNumberN));
			}
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNineOnLatLon(ArrayUtils.contains(NINE_ARRAYS, latitude) ? latitude : ElementValUtil.getlatitude(String.valueOf(latitude)))); // 纬度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNineOnLatLon(ArrayUtils.contains(NINE_ARRAYS, longitude) ? longitude : ElementValUtil.getLongitude(String.valueOf(longitude)))); // 经度
			
//			pStatement.setBigDecimal(num++,
//					NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, heightOfSationGroundAboveMeanSeaLevel) ? heightOfSationGroundAboveMeanSeaLevel
//							: (heightOfSationGroundAboveMeanSeaLevel / 10.0))); // 测站海拔高度
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(heightOfSationGroundAboveMeanSeaLevel / 10.0))); // 测站海拔高度
			
//			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(
//					ArrayUtils.contains(NINE_ARRAYS, heightOfBarometerAboveMeanSeaLevel) ? heightOfBarometerAboveMeanSeaLevel : (heightOfBarometerAboveMeanSeaLevel / 10.0))); // 气压传感器海拔高度
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(heightOfBarometerAboveMeanSeaLevel.doubleValue() == 99999.0 ? 999999 : heightOfBarometerAboveMeanSeaLevel / 10.0))); // 气压传感器海拔高度

			// 中国行政区划代码
			pStatement.setString(num++, adminCode);

			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getYear(obsTime))); // 资料观测年
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getMonth(obsTime))); // 资料观测月
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getDayOfMonth(obsTime))); // 资料观测日
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(hydrologicalPhenomenonName)); // 水文现象名称
			pStatement.setString(num++, v_bbb); // 默认000，首次入库可不填，数据库字段默认000

			if (isUpdate) {
				pStatement.setTimestamp(num++, d_datetime); // 资料时间
				pStatement.setString(num++, stationNumberC); // 区站号（字符）
			}
			if(!isUpdate)
				pStatement.setString(num++, ini.getValue(ReadIni.SECTION_PHENO_03, ReadIni.CTS_CODE_KEY));
		} catch (SQLException e) {
			loggerBuffer.append("\n Failed to set field values: " + e.getMessage());
		}

		di.setIIiii(stationNumberC);
		di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm"));
		di.setPROCESS_END_TIME(TimeUtil.getSysTime());
		di.setRECORD_TIME(TimeUtil.getSysTime());
		di.setLATITUDE(NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, latitude) ? latitude : ElementValUtil.getlatitude(String.valueOf(latitude))) + "");
		di.setLONGTITUDE(NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, longitude) ? longitude : ElementValUtil.getLongitude(String.valueOf(longitude))) + "");
		di.setDATA_UPDATE_FLAG(v_bbb);
		di.setHEIGHT(String.valueOf(heightOfSationGroundAboveMeanSeaLevel / 10.0));
		di.setSEND_PHYS("DRDS");
		di.setFILE_SIZE(String.valueOf(file.length()));
	}

	/**
	 * 
	 * @Title: update_pheno_data_03
	 * @Description: 更新数据库数据
	 * @param agme_Pheno_03s
	 * @param connection
	 * @param recv_time void
	 * @param isUpdate 
	 * @param fileName 
	 * @param v_bbb 
	 * @param loggerBuffer 
	 * @throws:
	 */
	private static void update_pheno_data_03(List<Object> agme_Pheno_03s, java.sql.Connection connection, Date recv_time, String d_data_id, boolean isUpdate, String filepath, String v_bbb, StringBuffer loggerBuffer) {

		ReadIni ini = ReadIni.getIni();

		// String d_data_id = ini.getValue(ReadIniPheno.SECTION_SOIL_01, ReadIniPheno.D_DATA_ID_KEY);

		String update_sql = ini.getValue(ReadIni.SECTION_PHENO_03, ReadIni.UPDATE_SQL_KEY).toUpperCase();
		String insert_sql = ini.getValue(ReadIni.SECTION_PHENO_03, ReadIni.INSERT_SQL_KEY).toUpperCase();

		PreparedStatement pStatement = null;

		for (Object obj : agme_Pheno_03s) {
			Agme_Pheno_03 agme_Pheno_03 = (Agme_Pheno_03) obj;
			String db_v_bbb = find_pheno_data_03(agme_Pheno_03, connection,loggerBuffer); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if (db_v_bbb == null) { // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				isUpdate = false;
				try {
					pStatement = new LoggableStatement(connection, insert_sql);
					insert_one_pheno_data_03(recv_time, connection, d_data_id, pStatement, agme_Pheno_03,filepath,isUpdate,v_bbb,loggerBuffer);
				} catch (SQLException e) {
					e.printStackTrace();
				}finally {
					if (pStatement != null) {
						try {
							pStatement.close();
						} catch (SQLException e) {
							loggerBuffer.append("\n close Statement error" + e.getMessage());
						}
					}
				}
			} else if (db_v_bbb.compareTo(v_bbb) < 0) { // 数据库中有该条记录，且V_BBB的值早于当前处理文件的更正标识的值
				isUpdate = true;
				try {
					pStatement = new LoggableStatement(connection, update_sql);
					insert_one_pheno_data_03(recv_time, connection, d_data_id, pStatement, agme_Pheno_03,filepath,isUpdate,v_bbb,loggerBuffer);
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					if (pStatement != null) {
						try {
							pStatement.close();
						} catch (SQLException e) {
							loggerBuffer.append("\n clsoe Statement error" + e.getMessage());
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
	 * @Title: find_pheno_data_03
	 * @Description: 查询数据表中的更正标识
	 * @param agme_Pheno_03
	 * @param connection
	 * @param loggerBuffer 
	 * @return V_BBB更正标识
	 * @throws:
	 */
	private static String find_pheno_data_03(Agme_Pheno_03 agme_Pheno_03, java.sql.Connection connection, StringBuffer loggerBuffer) {
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		ReadIni ini = ReadIni.getIni();
		String select_sql = ini.getValue(ReadIni.SECTION_PHENO_03, ReadIni.SELECT_SQL_KEY);
		try {

			pstmt = connection.prepareStatement(select_sql);
			int num = 1;
			Date obsTime = agme_Pheno_03.getAppearTime();

			pstmt.setTimestamp(num++, new Timestamp(obsTime.getTime())); // 时间
			pstmt.setString(num++, agme_Pheno_03.getStationNumberChina()); // 站号

			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {
				return resultSet.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(resultSet != null) {
					resultSet.close();
				}
			} catch (SQLException e) {
				loggerBuffer.append("\n close ResultSet failed!");
			}
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				loggerBuffer.append("\n close PreparedStatement failed!");
			}
		}
		return null;
	}
}
