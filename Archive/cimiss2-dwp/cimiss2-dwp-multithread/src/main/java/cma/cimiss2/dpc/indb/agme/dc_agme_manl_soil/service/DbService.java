package cma.cimiss2.dpc.indb.agme.dc_agme_manl_soil.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
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
import java.util.regex.Pattern;


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
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Soil;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Soil_01;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Soil_02;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Soil_03;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Soil_04;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Soil_05;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Soil_06;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Soil_07;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Soil_08;
import cma.cimiss2.dpc.decoder.tools.ElementValUtil;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.Quality;
import cma.cimiss2.dpc.indb.agme.ReportInfoService;
import cma.cimiss2.dpc.indb.agme.dc_agme_manl_soil.ReadIni;

public class DbService {
	private static final int DEFAULT_VALUE = 999999;
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static BlockingQueue<StatDi> diQueues;
	private static final double[] NINE_ARRAYS = {99, 999, 9999, 99999 };
	static ReadIni ini = ReadIni.getIni();
	

//	static Map<String, Object> proMap = StationInfo.getProMap();
	
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}

	public static DataBaseAction insert_db(ParseResult<Agme_Soil> parseResult, Date recv_time, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer, List<CTSCodeMap> ctsCodeMaps) {
		File file = new File(filepath);
		String fileName = file.getName();
		// 获取数据库连接
		java.sql.Connection connection = null;
		try {
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");

			if (connection != null) {
				// list: 所有数据
				List<Agme_Soil> list = parseResult.getData();

				Agme_Soil agme_Soil = list.get(0); // 解析时只存了一个对象在parseResult
				List<String> soilTypes = agme_Soil.soilTypes;
				for (String type : soilTypes) {
					switch (type) {
					case ReadIni.SECTION_SOIL_01:

						List<Agme_Soil_01> agme_Soil_01s = agme_Soil.agme_Soil_01s;
						// 判断是否需要更新
						if (isUpdate) {
							connection.setAutoCommit(true);
							update_soil_data_01(agme_Soil_01s, connection, recv_time,filepath,isUpdate,v_bbb,loggerBuffer);
						} else { // 不需要更新直接入库,如果批量入库失败，需改为逐单条入库
							connection.setAutoCommit(false);
							insert_soil_data_01(agme_Soil_01s, connection, recv_time,filepath,isUpdate,v_bbb,loggerBuffer);
						}
						break;
					case ReadIni.SECTION_SOIL_02:
						List<Agme_Soil_02> agme_Soil_02s = agme_Soil.agme_Soil_02s;
						// 判断是否需要更新
						if (isUpdate) {
							connection.setAutoCommit(true);
							update_soil_data_02(agme_Soil_02s, connection, recv_time,filepath,isUpdate,v_bbb,loggerBuffer);
						} else { // 不需要更新直接入库,如果批量入库失败，需改为逐单条入库
							connection.setAutoCommit(false);
							insert_soil_data_02(agme_Soil_02s, connection, recv_time,filepath,isUpdate,v_bbb,loggerBuffer);
						}
						break;
					case ReadIni.SECTION_SOIL_03:
						List<Agme_Soil_03> agme_Soil_03s = agme_Soil.agme_Soil_03s;
						// 判断是否需要更新
						if (isUpdate) {
							connection.setAutoCommit(true);
							update_soil_data_03(agme_Soil_03s, connection, recv_time,filepath,isUpdate,v_bbb,loggerBuffer);
						} else { // 不需要更新直接入库,如果批量入库失败，需改为逐单条入库
							connection.setAutoCommit(false);
							insert_soil_data_03(agme_Soil_03s, connection, recv_time,filepath,isUpdate,v_bbb,loggerBuffer);
						}
						break;
					case ReadIni.SECTION_SOIL_04:
						List<Agme_Soil_04> agme_Soil_04s = agme_Soil.agme_Soil_04s;
						// 判断是否需要更新
						if (isUpdate) {
							connection.setAutoCommit(true);
							update_soil_data_04(agme_Soil_04s, connection, recv_time,filepath,isUpdate,v_bbb,loggerBuffer);
						} else { // 不需要更新直接入库,如果批量入库失败，需改为逐单条入库
							connection.setAutoCommit(false);
							insert_soil_data_04(agme_Soil_04s, connection, recv_time,filepath,isUpdate,v_bbb,loggerBuffer);
						}
						break;
					case ReadIni.SECTION_SOIL_05:
						List<Agme_Soil_05> agme_Soil_05s = agme_Soil.agme_Soil_05s;
						// 判断是否需要更新
						if (isUpdate) {
							connection.setAutoCommit(true);
							update_soil_data_05(agme_Soil_05s, connection, recv_time,filepath,isUpdate,v_bbb,loggerBuffer);
						} else { // 不需要更新直接入库,如果批量入库失败，需改为逐单条入库
							connection.setAutoCommit(false);
							insert_soil_data_05(agme_Soil_05s, connection, recv_time,filepath,isUpdate,v_bbb,loggerBuffer);
						}
						break;
					case ReadIni.SECTION_SOIL_06:
						List<Agme_Soil_06> agme_Soil_06s = agme_Soil.agme_Soil_06s;
						// 判断是否需要更新
						if (isUpdate) {
							connection.setAutoCommit(true);
							update_soil_data_06(agme_Soil_06s, connection, recv_time,filepath,isUpdate,v_bbb,loggerBuffer);
						} else { // 不需要更新直接入库,如果批量入库失败，需改为逐单条入库
							connection.setAutoCommit(false);
							insert_soil_data_06(agme_Soil_06s, connection, recv_time,filepath,isUpdate,v_bbb,loggerBuffer);
						}
						break;
					case ReadIni.SECTION_SOIL_07:
						List<Agme_Soil_07> agme_Soil_07s = agme_Soil.agme_Soil_07s;
						// 判断是否需要更新
						if (isUpdate) {
							connection.setAutoCommit(true);
							update_soil_data_07(agme_Soil_07s, connection, recv_time,filepath,isUpdate,v_bbb,loggerBuffer);
						} else { // 不需要更新直接入库,如果批量入库失败，需改为逐单条入库
							connection.setAutoCommit(false);
							insert_soil_data_07(agme_Soil_07s, connection, recv_time,filepath,isUpdate,v_bbb,loggerBuffer);
						}
						break;
					case ReadIni.SECTION_SOIL_08:
						List<Agme_Soil_08> agme_Soil_08s = agme_Soil.agme_Soil_08s;
						// 判断是否需要更新
						if (isUpdate) {
							connection.setAutoCommit(true);
							update_soil_data_08(agme_Soil_08s, connection, recv_time,filepath,isUpdate,v_bbb,loggerBuffer);
						} else { // 不需要更新直接入库,如果批量入库失败，需改为逐单条入库
							connection.setAutoCommit(false);
							insert_soil_data_08(agme_Soil_08s, connection, recv_time,filepath,isUpdate,v_bbb,loggerBuffer);
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
			loggerBuffer.append("\n set AutoCommit failed");
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
	 * @Description: 土壤水分01类入库
	 * @param recv_time 收到时间
	 * @param connection 数据库连接
	 * @param agme_Soil_01s 01类数据解析集合
	 * @param v_bbb 
	 * @param isUpdate 
	 * @param fileName 
	 * @param loggerBuffer 
	 * @return DataBaseAction 成功与否标识
	 * @throws:
	 */
	private static DataBaseAction insert_soil_data_01(List<Agme_Soil_01> agme_Soil_01s, java.sql.Connection connection, Date recv_time, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		File file = new File(filepath);
		String fileName = file.getName();
		List<StatDi> listDi = new ArrayList<StatDi>();

		ReadIni ini = ReadIni.getIni();

		String insert_sql = ini.getValue(ReadIni.SECTION_SOIL_01, ReadIni.INSERT_SQL_KEY).toUpperCase().toUpperCase();
		String d_data_id = ini.getValue(ReadIni.SECTION_SOIL_01, ReadIni.D_DATA_ID_KEY).toUpperCase().toUpperCase();
		PreparedStatement pStatement = null;
		try {
			StatDi di = null;

			// pStatement = connection.prepareStatement(insert_sql);
			pStatement = new LoggableStatement(connection, insert_sql);
			if (StartConfig.getDatabaseType() == 1) {
				pStatement.execute("select last_txc_xid()");
			}
			for (Agme_Soil_01 agme_Soil_01 : agme_Soil_01s) {
				di = new StatDi();
				// 对DI和pStatement设置值
				generatePstAndDi_01(di, agme_Soil_01, pStatement, recv_time, d_data_id,filepath,isUpdate,v_bbb,loggerBuffer);
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
				loggerBuffer.append("\n Batch commit failed：" + fileName + "\n " + ReadIni.SECTION_SOIL_01 + "\n " + e.getMessage());
				for (Agme_Soil_01 agme_Soil_01 : agme_Soil_01s) {
					insert_one_soil_data_01(recv_time, connection, d_data_id, pStatement, agme_Soil_01,filepath,isUpdate,v_bbb,loggerBuffer);
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
	 * @param agme_Soil_01
	 * @param loggerBuffer 
	 * @return void
	 * @throws:
	 */
	private static void insert_one_soil_data_01(Date recv_time, java.sql.Connection connection, String d_data_id, PreparedStatement pStatement, Agme_Soil_01 agme_Soil_01, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		File file = new File(filepath);
		String fileName = file.getName();
		StatDi di = new StatDi();
		generatePstAndDi_01(di, agme_Soil_01, pStatement, recv_time, d_data_id,filepath,isUpdate,v_bbb,loggerBuffer);
		try {
			pStatement.execute();
//			connection.commit();
		} catch (SQLException e1) {
			di.setPROCESS_STATE("1");// 0成功，1失败
			loggerBuffer.append("\n filename：" + fileName //
					+ "\n " + agme_Soil_01.getStationNumberChina() + " " + TimeUtil.date2String(agme_Soil_01.getObsTime(), TimeUtil.DATE_FMT_YMDHMS) //
					+ "\n execute sql error：" + ((LoggableStatement) pStatement).getQueryString() //
					+ "\n " + e1.getMessage());
		} finally {
			diQueues.offer(di);
		}
	}

	/**
	 * 
	 * @Title: generatePst
	 * @Description: 设置prepareStatement和DI值
	 * @param di DI对象
	 * @param agme_Soil_01 土壤水份01要素对象
	 * @param pStatement prepareStatement
	 * @param recv_time 收到时间
	 * @param v_bbb 
	 * @param isUpdate 
	 * @param fileName 
	 * @param loggerBuffer 
	 * @param ini 配置文件信息
	 * @return void
	 * @throws:
	 */
	private static void generatePstAndDi_01(StatDi di, Agme_Soil_01 agme_Soil_01, PreparedStatement pStatement, Date recv_time, String d_data_id, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileName = file.getName();
		di.setFILE_NAME_O(fileName);
		di.setDATA_TYPE(d_data_id);
		di.setDATA_TYPE_1(ini.getValue(ReadIni.SECTION_SOIL_01, ReadIni.CTS_CODE_KEY));
		di.setTT("");
		di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
		di.setPROCESS_START_TIME(TimeUtil.getSysTime());
		di.setFILE_NAME_N(fileName);
		di.setBUSINESS_STATE("1"); // 1成功，0失败
		di.setPROCESS_STATE("1"); // 1成功，0失败

		// int stationNumberN = DEFAULT_VALUE;
		String stationNumberC = agme_Soil_01.getStationNumberChina();
		
		String stationNumberN = StationCodeUtil.stringToAscii(stationNumberC);
		String adminCode = "999999";
		// 根据站号查询行政区划代码
		String info = (String) proMap.get(stationNumberC + "+12");
		
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
		Date obsTime = agme_Soil_01.getObsTime();
		Timestamp d_datetime = new Timestamp(obsTime.getTime());

		Double latitude = agme_Soil_01.getLatitude(); // 纬度
		Double longitude = agme_Soil_01.getLongitude(); // 经度
		
		Double heightOfSationGroundAboveMeanSeaLevel = agme_Soil_01.getHeightOfSationGroundAboveMeanSeaLevel();// 测站海拔高度
		Double heightOfBarometerAboveMeanSeaLevel = agme_Soil_01.getHeightOfBarometerAboveMeanSeaLevel();// 气压传感器海拔高度

		Double geographyType = agme_Soil_01.getGeographyType(); // 地段类型
		Double soilDepth = agme_Soil_01.getSoilDepth(); // 土层深度
		String lengthStr = "00000";
		try { // TODO 修改update语句，去掉主键及入库时间
			if (!isUpdate) {
				// 时间(yyyyMMddHHmmss)_微秒（毫秒转微秒）SSSSSS_站号5_经度10_纬度9_海拔或高度相关8_(V_BBB)_其他字段
				String id = TimeUtil.date2String(obsTime, TimeUtil.DATE_FMT_YMDHMS) //
						+ "_" + (lengthStr.substring(0, 5 - stationNumberC.length()) + stationNumberC) //
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(longitude * 100), 10) //
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(latitude * 100), 9)//
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(heightOfSationGroundAboveMeanSeaLevel * 100), 8)//
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(heightOfBarometerAboveMeanSeaLevel * 100), 8)//
						+ "_" + NumberUtil.FormatNumOrNine(geographyType) //
						+ "_" + NumberUtil.FormatNumOrNine(soilDepth);

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
			// pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(latitude / 10000.0)); // 纬度
			// pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(longitude / 10000.0)); // 经度
			// pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(heightOfSationGroundAboveMeanSeaLevel)); // 测站海拔高度
			// pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(heightOfBarometerAboveMeanSeaLevel)); // 气压传感器海拔高度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNineOnLatLon(ArrayUtils.contains(NINE_ARRAYS, latitude) ? latitude :ElementValUtil.getlatitude(String.valueOf(latitude)))); // 纬度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNineOnLatLon(ArrayUtils.contains(NINE_ARRAYS, longitude) ? longitude :ElementValUtil.getLongitude(String.valueOf(longitude)))); // 经度
			pStatement.setBigDecimal(num++,
					NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, heightOfSationGroundAboveMeanSeaLevel) ? heightOfSationGroundAboveMeanSeaLevel
							: (heightOfSationGroundAboveMeanSeaLevel / 10.0))); // 测站海拔高度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(
					ArrayUtils.contains(NINE_ARRAYS, heightOfBarometerAboveMeanSeaLevel) ? heightOfBarometerAboveMeanSeaLevel : (heightOfBarometerAboveMeanSeaLevel / 10.0))); // 气压传感器海拔高度

			// 中国行政区划代码
			pStatement.setString(num++, adminCode);

			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getYear(obsTime))); // 资料观测年
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getMonth(obsTime))); // 资料观测月
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getDayOfMonth(obsTime))); // 资料观测日
			if (!isUpdate) {
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(geographyType)); // 地段类型
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(soilDepth)); // 土层深度
			}
			
			if(ArrayUtils.contains(NINE_ARRAYS, agme_Soil_01.getSoilWaterContent()))
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_01.getSoilWaterContent()));
			else
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Soil_01.getSoilWaterContent() * 0.1))); // 田间持水量
			
			if(ArrayUtils.contains(NINE_ARRAYS,agme_Soil_01.getSoilBulkDensity()))
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_01.getSoilBulkDensity()));
			else
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Soil_01.getSoilBulkDensity() * 0.01))); // 土壤容重
			
			if(ArrayUtils.contains(NINE_ARRAYS,agme_Soil_01.getWiltingHumidity()))
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_01.getWiltingHumidity()));
			else
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Soil_01.getWiltingHumidity() * 0.1))); // 凋萎湿度
		
			pStatement.setString(num++, v_bbb); // 默认000，首次入库可不填，数据库字段默认000

			if (isUpdate) {
				pStatement.setTimestamp(num++, d_datetime); // 资料时间
				pStatement.setString(num++, stationNumberC); // 区站号（字符）
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(geographyType)); // 地段类型
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(soilDepth)); // 土层深度
			}
			if(!isUpdate)
				pStatement.setString(num++, ini.getValue(ReadIni.SECTION_SOIL_01, ReadIni.CTS_CODE_KEY));
			
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
		di.setHEIGHT(NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, heightOfSationGroundAboveMeanSeaLevel) ? heightOfSationGroundAboveMeanSeaLevel: (heightOfSationGroundAboveMeanSeaLevel / 10.0))+ "");
		di.setSEND_PHYS("DRDS");
		di.setFILE_SIZE(String.valueOf(file.length()));
	}

	/**
	 * 
	 * @Title: update_soil_data_01
	 * @Description: 更新数据库数据
	 * @param agme_Soil_01s
	 * @param connection
	 * @param recv_time void
	 * @param v_bbb 
	 * @param isUpdate 
	 * @param fileName 
	 * @param loggerBuffer 
	 * @throws:
	 */
	private static void update_soil_data_01(List<Agme_Soil_01> agme_Soil_01s, java.sql.Connection connection, Date recv_time, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {

		ReadIni ini = ReadIni.getIni();

		String d_data_id = ini.getValue(ReadIni.SECTION_SOIL_01, ReadIni.D_DATA_ID_KEY).toUpperCase();

		String update_sql = ini.getValue(ReadIni.SECTION_SOIL_01, ReadIni.UPDATE_SQL_KEY).toUpperCase();
		String insert_sql = ini.getValue(ReadIni.SECTION_SOIL_01, ReadIni.INSERT_SQL_KEY).toUpperCase();

		PreparedStatement pStatement = null;

		for (Agme_Soil_01 agme_Soil_01 : agme_Soil_01s) {
			String db_v_bbb = find_soil_data_01(agme_Soil_01, connection,loggerBuffer); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if (db_v_bbb == null) { // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				isUpdate = false;
				try {
					pStatement = new LoggableStatement(connection, insert_sql);
					insert_one_soil_data_01(recv_time, connection, d_data_id, pStatement, agme_Soil_01,filepath,isUpdate,v_bbb,loggerBuffer);
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
					insert_one_soil_data_01(recv_time, connection, d_data_id, pStatement, agme_Soil_01,filepath,isUpdate,v_bbb,loggerBuffer);
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
	 * @Title: find_soil_data_01
	 * @Description: 查询数据表中的更正标识
	 * @param agme_Soil_01
	 * @param connection
	 * @param loggerBuffer 
	 * @return V_BBB更正标识
	 * @throws:
	 */
	private static String find_soil_data_01(Agme_Soil_01 agme_Soil_01, java.sql.Connection connection, StringBuffer loggerBuffer) {
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		ReadIni ini = ReadIni.getIni();
		String select_sql = ini.getValue(ReadIni.SECTION_SOIL_01, ReadIni.SELECT_SQL_KEY);
		try {

			pstmt = connection.prepareStatement(select_sql);
			int num = 1;
			Date obsTime = agme_Soil_01.getObsTime();

			pstmt.setTimestamp(num++, new Timestamp(obsTime.getTime())); // 时间
			pstmt.setString(num++, agme_Soil_01.getStationNumberChina()); // 站号
			pstmt.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_01.getGeographyType())); // 地段类型
			pstmt.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_01.getSoilDepth())); // 土层深度

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
	 * @Description: 土壤水分02类入库
	 * @param recv_time 收到时间
	 * @param connection 数据库连接
	 * @param agme_Soil_02s 02类数据解析集合
	 * @param v_bbb 
	 * @param isUpdate 
	 * @param fileName 
	 * @param loggerBuffer 
	 * @return DataBaseAction 成功与否标识
	 * @throws:
	 */
	private static DataBaseAction insert_soil_data_02(List<Agme_Soil_02> agme_Soil_02s, java.sql.Connection connection, Date recv_time, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		File file = new File(filepath);
		String fileName = file.getName();
		List<StatDi> listDi = new ArrayList<StatDi>();

		ReadIni ini = ReadIni.getIni();

		String insert_sql = ini.getValue(ReadIni.SECTION_SOIL_02, ReadIni.INSERT_SQL_KEY).toUpperCase();
		String d_data_id = ini.getValue(ReadIni.SECTION_SOIL_02, ReadIni.D_DATA_ID_KEY).toUpperCase();
		PreparedStatement pStatement = null;
		try {
			StatDi di = null;

			// pStatement = connection.prepareStatement(insert_sql);
			pStatement = new LoggableStatement(connection, insert_sql);
			if (StartConfig.getDatabaseType() == 1) {
				pStatement.execute("select last_txc_xid()");
			}
			for (Agme_Soil_02 agme_Soil_02 : agme_Soil_02s) {
				di = new StatDi();
				// 对DI和pStatement设置值
				generatePstAndDi_02(di, agme_Soil_02, pStatement, recv_time, d_data_id,filepath,isUpdate,v_bbb,loggerBuffer);
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
				loggerBuffer.append("\n Batch commit failed：" + fileName + "\n " + ReadIni.SECTION_SOIL_02 + "\n " + e.getMessage());
				for (Agme_Soil_02 agme_Soil_02 : agme_Soil_02s) {
					insert_one_soil_data_02(recv_time, connection, d_data_id, pStatement, agme_Soil_02,filepath,isUpdate,v_bbb,loggerBuffer);
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
	 * @param agme_Soil_02
	 * @param loggerBuffer 
	 * @return void
	 * @throws:
	 */
	private static void insert_one_soil_data_02(Date recv_time, java.sql.Connection connection, String d_data_id, PreparedStatement pStatement, Agme_Soil_02 agme_Soil_02,String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		File file = new File(filepath);
		String fileName = file.getName();
		StatDi di = new StatDi();
		generatePstAndDi_02(di, agme_Soil_02, pStatement, recv_time, d_data_id,filepath,isUpdate,v_bbb,loggerBuffer);
		try {
			pStatement.execute();
//			connection.commit();
		} catch (SQLException e1) {
			di.setPROCESS_STATE("0");// 1成功，0失败
			loggerBuffer.append("\n filename：" + fileName //
					+ "\n " + agme_Soil_02.getStationNumberChina() + " " + TimeUtil.date2String(agme_Soil_02.getObsTime(), TimeUtil.DATE_FMT_YMDHMS) //
					+ "\n execute sql error：" + ((LoggableStatement) pStatement).getQueryString() //
					+ "\n " + e1.getMessage() + "\n " + d_data_id);
		} finally {
			diQueues.offer(di);
		}
	}

	/**
	 * 
	 * @Title: generatePst
	 * @Description: 设置prepareStatement和DI值
	 * @param di DI对象
	 * @param agme_Soil_02 土壤水份02要素对象
	 * @param pStatement prepareStatement
	 * @param recv_time 收到时间
	 * @param v_bbb 
	 * @param isUpdate 
	 * @param fileName 
	 * @param loggerBuffer 
	 * @param ini 配置文件信息
	 * @return void
	 * @throws:
	 */
	private static void generatePstAndDi_02(StatDi di, Agme_Soil_02 agme_Soil_02, PreparedStatement pStatement, Date recv_time, String d_data_id, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileName = file.getName();
		di.setFILE_NAME_O(fileName);
		di.setDATA_TYPE(d_data_id);
		di.setDATA_TYPE_1(ini.getValue(ReadIni.SECTION_SOIL_02, ReadIni.CTS_CODE_KEY));
		di.setTT("");
		di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
		di.setPROCESS_START_TIME(TimeUtil.getSysTime());
		di.setFILE_NAME_N(fileName);
		di.setBUSINESS_STATE("1"); // 1成功，0失败
		di.setPROCESS_STATE("1"); // 1成功，0失败

		int stationNumberN = DEFAULT_VALUE;
		String stationNumberC = agme_Soil_02.getStationNumberChina();
		if (Pattern.matches("\\d{5}", stationNumberC)) { // 如果是5位数字
			stationNumberN = Integer.parseInt(stationNumberC);
		}
		String adminCode = "999999";
		// 根据站号查询行政区划代码
		String info = (String) proMap.get(stationNumberC + "+12");
		
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
		Date obsTime = agme_Soil_02.getObsTime();
		Timestamp d_datetime = new Timestamp(obsTime.getTime());

		Double latitude = agme_Soil_02.getLatitude(); // 纬度
		Double longitude = agme_Soil_02.getLongitude(); // 经度

		Double heightOfSationGroundAboveMeanSeaLevel = agme_Soil_02.getHeightOfSationGroundAboveMeanSeaLevel();// 测站海拔高度
		Double heightOfBarometerAboveMeanSeaLevel = agme_Soil_02.getHeightOfBarometerAboveMeanSeaLevel();// 气压传感器海拔高度

		Double geographyType = agme_Soil_02.getGeographyType(); // 地段类型
		Double cropName = agme_Soil_02.getCropName(); // 作物名称
		Double cropPeriod = agme_Soil_02.getCropPeriod(); // 发育期
		String lengthStr = "00000";
		try { // TODO 修改update语句，去掉主键及入库时间
			if (!isUpdate) {
				// 时间(yyyyMMddHHmmss)_微秒（毫秒转微秒）SSSSSS_站号5_经度10_纬度9_海拔或高度相关8_(V_BBB)_其他字段
				String id = TimeUtil.date2String(obsTime, TimeUtil.DATE_FMT_YMDHMS) //
						+ "_" + (lengthStr.substring(0, 5 - stationNumberC.length()) + stationNumberC) //
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(longitude * 100), 10) //
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(latitude * 100), 9)//
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(heightOfSationGroundAboveMeanSeaLevel * 100), 8)//
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(heightOfBarometerAboveMeanSeaLevel * 100), 8)//
						+ "_" + NumberUtil.FormatNumOrNine(geographyType) //
						+ "_" + NumberUtil.FormatNumOrNine(cropName) //
						+ "_" + NumberUtil.FormatNumOrNine(cropPeriod);

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
				pStatement.setInt(num++, stationNumberN); // 区站号（数字）
			}
			// pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(latitude / 10000.0)); // 纬度
			// pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(longitude / 10000.0)); // 经度
			// pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(heightOfSationGroundAboveMeanSeaLevel)); // 测站海拔高度
			// pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(heightOfBarometerAboveMeanSeaLevel)); // 气压传感器海拔高度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNineOnLatLon(ArrayUtils.contains(NINE_ARRAYS, latitude) ? latitude :ElementValUtil.getlatitude(String.valueOf(latitude)))); // 纬度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNineOnLatLon(ArrayUtils.contains(NINE_ARRAYS, longitude) ? longitude :ElementValUtil.getLongitude(String.valueOf(longitude)))); // 经度
			pStatement.setBigDecimal(num++,
					NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, heightOfSationGroundAboveMeanSeaLevel) ? heightOfSationGroundAboveMeanSeaLevel
							: (heightOfSationGroundAboveMeanSeaLevel / 10.0))); // 测站海拔高度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(
					ArrayUtils.contains(NINE_ARRAYS, heightOfBarometerAboveMeanSeaLevel) ? heightOfBarometerAboveMeanSeaLevel : (heightOfBarometerAboveMeanSeaLevel / 10.0))); // 气压传感器海拔高度

			// 中国行政区划代码
			pStatement.setString(num++, adminCode);

			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getYear(obsTime))); // 资料观测年
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getMonth(obsTime))); // 资料观测月
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getDayOfMonth(obsTime))); // 资料观测日
			if (!isUpdate) {
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(geographyType)); // 地段类型
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(cropName)); // 作物名称
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(cropPeriod)); // 发育期
			}
			Double getSoilHumidity_10 = ElementValUtil.CheckValidValue2Double(agme_Soil_02.getSoilHumidity_10(), 9999);
			Double getSoilHumidity_20 = ElementValUtil.CheckValidValue2Double(agme_Soil_02.getSoilHumidity_20(), 9999);
			Double getSoilHumidity_30 = ElementValUtil.CheckValidValue2Double(agme_Soil_02.getSoilHumidity_30(), 9999);
			Double getSoilHumidity_40 = ElementValUtil.CheckValidValue2Double(agme_Soil_02.getSoilHumidity_40(), 9999);
			Double getSoilHumidity_50 = ElementValUtil.CheckValidValue2Double(agme_Soil_02.getSoilHumidity_50(), 9999);
			Double getSoilHumidity_60 = ElementValUtil.CheckValidValue2Double(agme_Soil_02.getSoilHumidity_60(), 9999);
			Double getSoilHumidity_70 = ElementValUtil.CheckValidValue2Double(agme_Soil_02.getSoilHumidity_70(), 9999);
			Double getSoilHumidity_80 = ElementValUtil.CheckValidValue2Double(agme_Soil_02.getSoilHumidity_80(), 9999);
			Double getSoilHumidity_90 = ElementValUtil.CheckValidValue2Double(agme_Soil_02.getSoilHumidity_90(), 9999);
			Double getSoilHumidity_100 = ElementValUtil.CheckValidValue2Double(agme_Soil_02.getSoilHumidity_100(), 9999);
			
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_02.getDrySoilThickness())); // 干土层厚度
			
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(getSoilHumidity_10))); 
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(getSoilHumidity_20))); 
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(getSoilHumidity_30))); 
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(getSoilHumidity_40))); 
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(getSoilHumidity_50))); 
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(getSoilHumidity_60))); 
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(getSoilHumidity_70))); 
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(getSoilHumidity_80))); 
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(getSoilHumidity_90))); 
			pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(getSoilHumidity_100))); 
			
		/*	pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_02.getDrySoilThickness())); // 干土层厚度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_02.getSoilHumidity_10())); // 10cm土壤相对湿度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_02.getSoilHumidity_20())); // 20cm土壤相对湿度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_02.getSoilHumidity_30())); // 30cm土壤相对湿度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_02.getSoilHumidity_40())); // 40cm土壤相对湿度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_02.getSoilHumidity_50())); // 50cm土壤相对湿度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_02.getSoilHumidity_60())); // 60cm土壤相对湿度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_02.getSoilHumidity_70())); // 70cm土壤相对湿度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_02.getSoilHumidity_80())); // 80cm土壤相对湿度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_02.getSoilHumidity_90())); // 90cm土壤相对湿度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_02.getSoilHumidity_100())); // 100cm土壤相对湿度
*/			if(agme_Soil_02.getIrrigationOrPrecipitation().intValue() == 9)
				pStatement.setBigDecimal(num++, new BigDecimal("999999"));
			else 
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_02.getIrrigationOrPrecipitation())); // 灌溉或降水
			
			if(ArrayUtils.contains(NINE_ARRAYS, agme_Soil_02.getGroundWaterLevel()))
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_02.getGroundWaterLevel()));
			else
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Soil_02.getGroundWaterLevel() * 0.1)));// 地下水位

			// 文件中没有质控码，设置默认值9
			BigDecimal nineQC = NumberUtil.FormatNumOrNine(Quality.Nine.getCode());
			pStatement.setBigDecimal(num++, nineQC); // 10cm土壤相对湿度质控码
			pStatement.setBigDecimal(num++, nineQC); // 20cm土壤相对湿度质控码
			pStatement.setBigDecimal(num++, nineQC); // 30cm土壤相对湿度质控码
			pStatement.setBigDecimal(num++, nineQC); // 40cm土壤相对湿度质控码
			pStatement.setBigDecimal(num++, nineQC); // 50cm土壤相对湿度质控码
			pStatement.setBigDecimal(num++, nineQC); // 60cm土壤相对湿度质控码
			pStatement.setBigDecimal(num++, nineQC); // 70cm土壤相对湿度质控码
			pStatement.setBigDecimal(num++, nineQC); // 80cm土壤相对湿度质控码
			pStatement.setBigDecimal(num++, nineQC); // 90cm土壤相对湿度质控码
			pStatement.setBigDecimal(num++, nineQC); // 100cm土壤相对湿度质控码

			pStatement.setString(num++, v_bbb); // 默认000，首次入库可不填，数据库字段默认000
			// TODO 质控码从哪取？

			// 查询或更新条件
			if (isUpdate) {
				pStatement.setTimestamp(num++, d_datetime); // 资料时间
				pStatement.setString(num++, stationNumberC); // 区站号（字符）
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(geographyType)); // 地段类型
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(cropName)); // 作物名称
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(cropPeriod)); // 发育期
			}
			if(!isUpdate)
				pStatement.setString(num++, ini.getValue(ReadIni.SECTION_SOIL_02, ReadIni.CTS_CODE_KEY));

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
		di.setHEIGHT(NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, heightOfSationGroundAboveMeanSeaLevel) ? heightOfSationGroundAboveMeanSeaLevel: (heightOfSationGroundAboveMeanSeaLevel / 10.0))+"");
		di.setSEND_PHYS("DRDS");
		di.setFILE_SIZE(String.valueOf(file.length()));
	}

	/**
	 * 
	 * @Title: update_soil_data_02
	 * @Description: 更新数据库数据
	 * @param agme_Soil_02s
	 * @param connection
	 * @param recv_time void
	 * @param v_bbb 
	 * @param isUpdate 
	 * @param fileName 
	 * @param loggerBuffer 
	 * @throws:
	 */
	private static void update_soil_data_02(List<Agme_Soil_02> agme_Soil_02s, java.sql.Connection connection, Date recv_time, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {

		String d_data_id = ini.getValue(ReadIni.SECTION_SOIL_02, ReadIni.D_DATA_ID_KEY).toUpperCase();

		String update_sql = ini.getValue(ReadIni.SECTION_SOIL_02, ReadIni.UPDATE_SQL_KEY).toUpperCase();
		String insert_sql = ini.getValue(ReadIni.SECTION_SOIL_02, ReadIni.INSERT_SQL_KEY).toUpperCase();

		PreparedStatement pStatement = null;

		for (Agme_Soil_02 agme_Soil_02 : agme_Soil_02s) {
			String db_v_bbb = find_soil_data_02(agme_Soil_02, connection,loggerBuffer); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if (db_v_bbb == null) { // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				isUpdate = false;
				try {
					pStatement = new LoggableStatement(connection, insert_sql);
					insert_one_soil_data_02(recv_time, connection, d_data_id, pStatement, agme_Soil_02,filepath,isUpdate,v_bbb,loggerBuffer);
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
					insert_one_soil_data_02(recv_time, connection, d_data_id, pStatement, agme_Soil_02,filepath,isUpdate,v_bbb,loggerBuffer);
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
	 * @Title: find_soil_data_02
	 * @Description: 查询数据表中的更正标识
	 * @param agme_Soil_02
	 * @param connection
	 * @param loggerBuffer 
	 * @return V_BBB更正标识
	 * @throws:
	 */
	private static String find_soil_data_02(Agme_Soil_02 agme_Soil_02, java.sql.Connection connection, StringBuffer loggerBuffer) {
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		ReadIni ini = ReadIni.getIni();
		String select_sql = ini.getValue(ReadIni.SECTION_SOIL_02, ReadIni.SELECT_SQL_KEY);
		try {

			pstmt = connection.prepareStatement(select_sql);
			int num = 1;
			Date obsTime = agme_Soil_02.getObsTime();

			pstmt.setTimestamp(num++, new Timestamp(obsTime.getTime())); // 资料时间
			pstmt.setString(num++, agme_Soil_02.getStationNumberChina()); // 区站号（字符）
			pstmt.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_02.getGeographyType())); // 地段类型
			pstmt.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_02.getCropName())); // 作物名称
			pstmt.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_02.getCropPeriod())); // 发育期

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
	 * @Description: 土壤水分03类入库
	 * @param recv_time 收到时间
	 * @param connection 数据库连接
	 * @param agme_Soil_03s 03类数据解析集合
	 * @param v_bbb 
	 * @param isUpdate 
	 * @param fileName 
	 * @param loggerBuffer 
	 * @return DataBaseAction 成功与否标识
	 * @throws:
	 */
	private static DataBaseAction insert_soil_data_03(List<Agme_Soil_03> agme_Soil_03s, java.sql.Connection connection, Date recv_time, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		File file = new File(filepath);
		String fileName = file.getName();
		List<StatDi> listDi = new ArrayList<StatDi>();

		ReadIni ini = ReadIni.getIni();

		String insert_sql = ini.getValue(ReadIni.SECTION_SOIL_03, ReadIni.INSERT_SQL_KEY).toUpperCase();
		String d_data_id = ini.getValue(ReadIni.SECTION_SOIL_03, ReadIni.D_DATA_ID_KEY).toUpperCase();
		PreparedStatement pStatement = null;
		try {
			StatDi di = null;

			// pStatement = connection.prepareStatement(insert_sql);
			pStatement = new LoggableStatement(connection, insert_sql);
			if (StartConfig.getDatabaseType() == 1) {
				pStatement.execute("select last_txc_xid()");
			}
			for (Agme_Soil_03 agme_Soil_03 : agme_Soil_03s) {
				di = new StatDi();
				// 对DI和pStatement设置值
				generatePstAndDi_03(di, agme_Soil_03, pStatement, recv_time, d_data_id,filepath,isUpdate,v_bbb,loggerBuffer);
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
				loggerBuffer.append("\n Batch commit failed：" + fileName + "\n " + ReadIni.SECTION_SOIL_03 + "\n " + e.getMessage());
				for (Agme_Soil_03 agme_Soil_03 : agme_Soil_03s) {
					insert_one_soil_data_03(recv_time, connection, d_data_id, pStatement, agme_Soil_03,filepath,isUpdate,v_bbb,loggerBuffer);
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
	 * @param agme_Soil_03
	 * @param v_bbb 
	 * @param isUpdate 
	 * @param fileName 
	 * @param loggerBuffer 
	 * @return void
	 * @throws:
	 */
	private static void insert_one_soil_data_03(Date recv_time, java.sql.Connection connection, String d_data_id, PreparedStatement pStatement, Agme_Soil_03 agme_Soil_03, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		File file = new File(filepath);
		String fileName = file.getName();
		StatDi di = new StatDi();
		generatePstAndDi_03(di, agme_Soil_03, pStatement, recv_time, d_data_id,filepath,isUpdate,v_bbb,loggerBuffer);
		try {
			pStatement.execute();
//			connection.commit();
		} catch (SQLException e1) {
			di.setPROCESS_STATE("0");// 1成功，0失败
			loggerBuffer.append("\n filename：" + fileName //
					+ "\n " + agme_Soil_03.getStationNumberChina() + " " + TimeUtil.date2String(agme_Soil_03.getObsTime(), TimeUtil.DATE_FMT_YMDHMS) //
					+ "\n execute sql error：" + ((LoggableStatement) pStatement).getQueryString() //
					+ "\n " + e1.getMessage() + "\n " + d_data_id);
		} finally {
			diQueues.offer(di);
		}
	}

	/**
	 * 
	 * @Title: generatePst
	 * @Description: 设置prepareStatement和DI值
	 * @param di DI对象
	 * @param agme_Soil_03 土壤水份02要素对象
	 * @param pStatement prepareStatement
	 * @param recv_time 收到时间
	 * @param v_bbb 
	 * @param isUpdate 
	 * @param fileName 
	 * @param loggerBuffer 
	 * @param ini 配置文件信息
	 * @return void
	 * @throws:
	 */
	private static void generatePstAndDi_03(StatDi di, Agme_Soil_03 agme_Soil_03, PreparedStatement pStatement, Date recv_time, String d_data_id, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileName = file.getName();
		di.setFILE_NAME_O(fileName);
		di.setDATA_TYPE(d_data_id);
		di.setDATA_TYPE_1(ini.getValue(ReadIni.SECTION_SOIL_03, ReadIni.CTS_CODE_KEY));
		di.setTT("");
		di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
		di.setPROCESS_START_TIME(TimeUtil.getSysTime());
		di.setFILE_NAME_N(fileName);
		di.setBUSINESS_STATE("1"); // 1成功，0失败
		di.setPROCESS_STATE("1"); // 1成功，0失败

		int stationNumberN = DEFAULT_VALUE;
		String stationNumberC = agme_Soil_03.getStationNumberChina();
		if (Pattern.matches("\\d{5}", stationNumberC)) { // 如果是5位数字
			stationNumberN = Integer.parseInt(stationNumberC);
		}
		String adminCode = "999999";
		// 根据站号查询行政区划代码
		String info = (String) proMap.get(stationNumberC + "+12");
		
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
		Date obsTime = agme_Soil_03.getObsTime();
		Timestamp d_datetime = new Timestamp(obsTime.getTime());

		Double latitude = agme_Soil_03.getLatitude(); // 纬度
		Double longitude = agme_Soil_03.getLongitude(); // 经度

		Double heightOfSationGroundAboveMeanSeaLevel = agme_Soil_03.getHeightOfSationGroundAboveMeanSeaLevel();// 测站海拔高度
		Double heightOfBarometerAboveMeanSeaLevel = agme_Soil_03.getHeightOfBarometerAboveMeanSeaLevel();// 气压传感器海拔高度

		Double geographyType = agme_Soil_03.getGeographyType(); // 地段类型
		Double cropName = agme_Soil_03.getCropName(); // 作物名称
		Double cropPeriod = agme_Soil_03.getCropPeriod(); // 发育期
		String lengthStr = "00000";
		try { // TODO 修改update语句，去掉主键及入库时间
			if (!isUpdate) {
				// 时间(yyyyMMddHHmmss)_微秒（毫秒转微秒）SSSSSS_站号5_经度10_纬度9_海拔或高度相关8_(V_BBB)_其他字段
				String id = TimeUtil.date2String(obsTime, TimeUtil.DATE_FMT_YMDHMS) //
						+ "_" + (lengthStr.substring(0, 5 - stationNumberC.length()) + stationNumberC) //
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(longitude * 100), 10) //
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(latitude * 100), 9)//
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(heightOfSationGroundAboveMeanSeaLevel * 100), 8)//
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(heightOfBarometerAboveMeanSeaLevel * 100), 8)//
						+ "_" + NumberUtil.FormatNumOrNine(geographyType) //
						+ "_" + NumberUtil.FormatNumOrNine(cropName) //
						+ "_" + NumberUtil.FormatNumOrNine(cropPeriod);

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
				pStatement.setInt(num++, stationNumberN); // 区站号（数字）
			}
			// pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(latitude / 10000.0)); // 纬度
			// pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(longitude / 10000.0)); // 经度
			// pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(heightOfSationGroundAboveMeanSeaLevel)); // 测站海拔高度
			// pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(heightOfBarometerAboveMeanSeaLevel)); // 气压传感器海拔高度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNineOnLatLon(ArrayUtils.contains(NINE_ARRAYS, latitude) ? latitude :ElementValUtil.getlatitude(String.valueOf(latitude)))); // 纬度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNineOnLatLon(ArrayUtils.contains(NINE_ARRAYS, longitude) ? longitude :ElementValUtil.getLongitude(String.valueOf(longitude)))); // 经度
			pStatement.setBigDecimal(num++,
					NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, heightOfSationGroundAboveMeanSeaLevel) ? heightOfSationGroundAboveMeanSeaLevel
							: (heightOfSationGroundAboveMeanSeaLevel / 10.0))); // 测站海拔高度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(
					ArrayUtils.contains(NINE_ARRAYS, heightOfBarometerAboveMeanSeaLevel) ? heightOfBarometerAboveMeanSeaLevel : (heightOfBarometerAboveMeanSeaLevel / 10.0))); // 气压传感器海拔高度

			// 中国行政区划代码
			pStatement.setString(num++, adminCode);

			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getYear(obsTime))); // 资料观测年
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getMonth(obsTime))); // 资料观测月
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getDayOfMonth(obsTime))); // 资料观测日
			if (!isUpdate) {
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(geographyType)); // 地段类型
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(cropName)); // 作物名称
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(cropPeriod)); // 发育期
			}
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_03.getSoilWaterContent_10())); // 10cm水分总储存量
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_03.getSoilWaterContent_20())); // 20cm水分总储存量
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_03.getSoilWaterContent_30())); // 30cm水分总储存量
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_03.getSoilWaterContent_40())); // 40cm水分总储存量
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_03.getSoilWaterContent_50())); // 50cm水分总储存量
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_03.getSoilWaterContent_60())); // 60cm水分总储存量
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_03.getSoilWaterContent_70())); // 70cm水分总储存量
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_03.getSoilWaterContent_80())); // 80cm水分总储存量
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_03.getSoilWaterContent_90())); // 90cm水分总储存量
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_03.getSoilWaterContent_100())); // 100cm水分总储存量
			pStatement.setString(num++, v_bbb); // 默认000，首次入库可不填，数据库字段默认000

			// 查询或更新条件
			if (isUpdate) {
				pStatement.setTimestamp(num++, d_datetime); // 资料时间
				pStatement.setString(num++, stationNumberC); // 区站号（字符）
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(geographyType)); // 地段类型
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(cropName)); // 作物名称
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(cropPeriod)); // 发育期
			}
			
			if(!isUpdate)
				pStatement.setString(num++, ini.getValue(ReadIni.SECTION_SOIL_03, ReadIni.CTS_CODE_KEY));
			
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
		di.setHEIGHT(NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, heightOfSationGroundAboveMeanSeaLevel) ? heightOfSationGroundAboveMeanSeaLevel
				: (heightOfSationGroundAboveMeanSeaLevel / 10.0))+"");
		di.setSEND_PHYS("DRDS");
		di.setFILE_SIZE(String.valueOf(file.length()));
	}

	/**
	 * 
	 * @Title: update_soil_data_03
	 * @Description: 更新数据库数据
	 * @param agme_Soil_03s
	 * @param connection
	 * @param recv_time void
	 * @param v_bbb 
	 * @param isUpdate 
	 * @param fileName 
	 * @param loggerBuffer 
	 * @throws:
	 */
	private static void update_soil_data_03(List<Agme_Soil_03> agme_Soil_03s, java.sql.Connection connection, Date recv_time, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {

		ReadIni ini = ReadIni.getIni();

		String d_data_id = ini.getValue(ReadIni.SECTION_SOIL_03, ReadIni.D_DATA_ID_KEY).toUpperCase();

		String update_sql = ini.getValue(ReadIni.SECTION_SOIL_03, ReadIni.UPDATE_SQL_KEY).toUpperCase();
		String insert_sql = ini.getValue(ReadIni.SECTION_SOIL_03, ReadIni.INSERT_SQL_KEY).toUpperCase();

		PreparedStatement pStatement = null;

		for (Agme_Soil_03 agme_Soil_03 : agme_Soil_03s) {
			String db_v_bbb = find_soil_data_03(agme_Soil_03, connection,loggerBuffer); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if (db_v_bbb == null) { // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				isUpdate = false;
				try {
					pStatement = new LoggableStatement(connection, insert_sql);
					insert_one_soil_data_03(recv_time, connection, d_data_id, pStatement, agme_Soil_03,filepath,isUpdate,v_bbb,loggerBuffer);
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
					insert_one_soil_data_03(recv_time, connection, d_data_id, pStatement, agme_Soil_03,filepath,isUpdate,v_bbb,loggerBuffer);
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
	 * @Title: find_soil_data_03
	 * @Description: 查询数据表中的更正标识
	 * @param agme_Soil_03
	 * @param connection
	 * @param loggerBuffer 
	 * @return V_BBB更正标识
	 * @throws:
	 */
	private static String find_soil_data_03(Agme_Soil_03 agme_Soil_03, java.sql.Connection connection, StringBuffer loggerBuffer) {
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		ReadIni ini = ReadIni.getIni();
		String select_sql = ini.getValue(ReadIni.SECTION_SOIL_03, ReadIni.SELECT_SQL_KEY);
		try {

			pstmt = connection.prepareStatement(select_sql);
			int num = 1;
			Date obsTime = agme_Soil_03.getObsTime();

			pstmt.setTimestamp(num++, new Timestamp(obsTime.getTime())); // 资料时间
			pstmt.setString(num++, agme_Soil_03.getStationNumberChina()); // 区站号（字符）
			pstmt.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_03.getGeographyType())); // 地段类型
			pstmt.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_03.getCropName())); // 作物名称
			pstmt.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_03.getCropPeriod())); // 发育期

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
	 * @Description: 土壤水分04类入库
	 * @param recv_time 收到时间
	 * @param connection 数据库连接
	 * @param agme_Soil_04s 04类数据解析集合
	 * @param v_bbb 
	 * @param isUpdate 
	 * @param fileName 
	 * @param loggerBuffer 
	 * @return DataBaseAction 成功与否标识
	 * @throws:
	 */
	private static DataBaseAction insert_soil_data_04(List<Agme_Soil_04> agme_Soil_04s, java.sql.Connection connection, Date recv_time, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		File file = new File(filepath);
		String fileName = file.getName();
		List<StatDi> listDi = new ArrayList<StatDi>();

		ReadIni ini = ReadIni.getIni();

		String insert_sql = ini.getValue(ReadIni.SECTION_SOIL_04, ReadIni.INSERT_SQL_KEY).toUpperCase();
		String d_data_id = ini.getValue(ReadIni.SECTION_SOIL_04, ReadIni.D_DATA_ID_KEY).toUpperCase();
		PreparedStatement pStatement = null;
		try {
			StatDi di = null;

			// pStatement = connection.prepareStatement(insert_sql);
			pStatement = new LoggableStatement(connection, insert_sql);
			if (StartConfig.getDatabaseType() == 1) {
				pStatement.execute("select last_txc_xid()");
			}
			for (Agme_Soil_04 agme_Soil_04 : agme_Soil_04s) {
				di = new StatDi();
				// 对DI和pStatement设置值
				generatePstAndDi_04(di, agme_Soil_04, pStatement, recv_time, d_data_id,filepath,isUpdate,v_bbb,loggerBuffer);
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
				loggerBuffer.append("\n Batch commit failed：" + fileName + "\n " + ReadIni.SECTION_SOIL_04 + "\n " + e.getMessage());
				for (Agme_Soil_04 agme_Soil_04 : agme_Soil_04s) {
					insert_one_soil_data_04(recv_time, connection, d_data_id, pStatement, agme_Soil_04,filepath,isUpdate,v_bbb,loggerBuffer);
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
	 * @param agme_Soil_04
	 * @param v_bbb 
	 * @param isUpdate 
	 * @param fileName 
	 * @param loggerBuffer 
	 * @return void
	 * @throws:
	 */
	private static void insert_one_soil_data_04(Date recv_time, java.sql.Connection connection, String d_data_id, PreparedStatement pStatement, Agme_Soil_04 agme_Soil_04, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		File file = new File(filepath);
		String fileName = file.getName();
		StatDi di = new StatDi();
		generatePstAndDi_04(di, agme_Soil_04, pStatement, recv_time, d_data_id,filepath,isUpdate,v_bbb,loggerBuffer);
		try {
			pStatement.execute();
//			connection.commit();
		} catch (SQLException e1) {
			di.setPROCESS_STATE("0");// 1成功，0失败
			loggerBuffer.append("\n filename：" + fileName //
					+ "\n " + agme_Soil_04.getStationNumberChina() + " " + TimeUtil.date2String(agme_Soil_04.getObsTime(), TimeUtil.DATE_FMT_YMDHMS) //
					+ "\n execute sql error：" + ((LoggableStatement) pStatement).getQueryString() //
					+ "\n " + e1.getMessage() + "\n " + d_data_id);
		} finally {
			diQueues.offer(di);
		}
	}

	/**
	 * 
	 * @Title: generatePst
	 * @Description: 设置prepareStatement和DI值
	 * @param di DI对象
	 * @param agme_Soil_04 土壤水份02要素对象
	 * @param pStatement prepareStatement
	 * @param recv_time 收到时间
	 * @param v_bbb 
	 * @param isUpdate 
	 * @param fileName 
	 * @param loggerBuffer 
	 * @param ini 配置文件信息
	 * @return void
	 * @throws:
	 */
	private static void generatePstAndDi_04(StatDi di, Agme_Soil_04 agme_Soil_04, PreparedStatement pStatement, Date recv_time, String d_data_id, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileName = file.getName();
		di.setFILE_NAME_O(fileName);
		di.setDATA_TYPE(d_data_id);
		di.setDATA_TYPE_1(ini.getValue(ReadIni.SECTION_SOIL_04, ReadIni.CTS_CODE_KEY));
		di.setTT("");
		di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
		di.setPROCESS_START_TIME(TimeUtil.getSysTime());
		di.setFILE_NAME_N(fileName);
		di.setBUSINESS_STATE("1"); // 1成功，0失败
		di.setPROCESS_STATE("1"); // 1成功，0失败

		int stationNumberN = DEFAULT_VALUE;
		String stationNumberC = agme_Soil_04.getStationNumberChina();
		if (Pattern.matches("\\d{5}", stationNumberC)) { // 如果是5位数字
			stationNumberN = Integer.parseInt(stationNumberC);
		}
		String adminCode = "999999";
		// 根据站号查询行政区划代码
		String info = (String) proMap.get(stationNumberC + "+12");
		
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
		Date obsTime = agme_Soil_04.getObsTime();
		Timestamp d_datetime = new Timestamp(obsTime.getTime());

		Double latitude = agme_Soil_04.getLatitude(); // 纬度
		Double longitude = agme_Soil_04.getLongitude(); // 经度

		Double heightOfSationGroundAboveMeanSeaLevel = agme_Soil_04.getHeightOfSationGroundAboveMeanSeaLevel();// 测站海拔高度
		Double heightOfBarometerAboveMeanSeaLevel = agme_Soil_04.getHeightOfBarometerAboveMeanSeaLevel();// 气压传感器海拔高度

		Double geographyType = agme_Soil_04.getGeographyType(); // 地段类型
		Double cropName = agme_Soil_04.getCropName(); // 作物名称
		Double cropPeriod = agme_Soil_04.getCropPeriod(); // 发育期
		String lengthStr = "00000";
		try { // TODO 修改update语句，去掉主键及入库时间
			if (!isUpdate) {
				// 时间(yyyyMMddHHmmss)_微秒（毫秒转微秒）SSSSSS_站号5_经度10_纬度9_海拔或高度相关8_(V_BBB)_其他字段
				String id = TimeUtil.date2String(obsTime, TimeUtil.DATE_FMT_YMDHMS) //
						+ "_" + (lengthStr.substring(0, 5 - stationNumberC.length()) + stationNumberC) //
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(longitude * 100), 10) //
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(latitude * 100), 9)//
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(heightOfSationGroundAboveMeanSeaLevel * 100), 8)//
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(heightOfBarometerAboveMeanSeaLevel * 100), 8)//
						+ "_" + NumberUtil.FormatNumOrNine(geographyType) //
						+ "_" + NumberUtil.FormatNumOrNine(cropName) //
						+ "_" + NumberUtil.FormatNumOrNine(cropPeriod);

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
				pStatement.setInt(num++, stationNumberN); // 区站号（数字）
			}
			// pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(latitude / 10000.0)); // 纬度
			// pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(longitude / 10000.0)); // 经度
			// pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(heightOfSationGroundAboveMeanSeaLevel)); // 测站海拔高度
			// pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(heightOfBarometerAboveMeanSeaLevel)); // 气压传感器海拔高度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNineOnLatLon(ArrayUtils.contains(NINE_ARRAYS, latitude) ? latitude :ElementValUtil.getlatitude(String.valueOf(latitude)))); // 纬度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNineOnLatLon(ArrayUtils.contains(NINE_ARRAYS, longitude) ? longitude :ElementValUtil.getLongitude(String.valueOf(longitude)))); // 经度
			pStatement.setBigDecimal(num++,
					NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, heightOfSationGroundAboveMeanSeaLevel) ? heightOfSationGroundAboveMeanSeaLevel
							: (heightOfSationGroundAboveMeanSeaLevel / 10.0))); // 测站海拔高度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(
					ArrayUtils.contains(NINE_ARRAYS, heightOfBarometerAboveMeanSeaLevel) ? heightOfBarometerAboveMeanSeaLevel : (heightOfBarometerAboveMeanSeaLevel / 10.0))); // 气压传感器海拔高度

			// 中国行政区划代码
			pStatement.setString(num++, adminCode);

			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getYear(obsTime))); // 资料观测年
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getMonth(obsTime))); // 资料观测月
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getDayOfMonth(obsTime))); // 资料观测日
			if (!isUpdate) {
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(geographyType)); // 地段类型
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(cropName)); // 作物名称
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(cropPeriod)); // 发育期
			}
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_04.getValidSoilWaterContent_10())); // 10cm有效水分储存量
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_04.getValidSoilWaterContent_20())); // 20cm有效水分储存量
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_04.getValidSoilWaterContent_30())); // 30cm有效水分储存量
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_04.getValidSoilWaterContent_40())); // 40cm有效水分储存量
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_04.getValidSoilWaterContent_50())); // 50cm有效水分储存量
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_04.getValidSoilWaterContent_60())); // 60cm有效水分储存量
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_04.getValidSoilWaterContent_70())); // 70cm有效水分储存量
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_04.getValidSoilWaterContent_80())); // 80cm有效水分储存量
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_04.getValidSoilWaterContent_90())); // 90cm有效水分储存量
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_04.getValidSoilWaterContent_100())); // 100cm有效水分储存量
			pStatement.setString(num++, v_bbb); // 默认000，首次入库可不填，数据库字段默认000

			// 查询或更新条件
			if (isUpdate) {
				pStatement.setTimestamp(num++, d_datetime); // 资料时间
				pStatement.setString(num++, stationNumberC); // 区站号（字符）
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(geographyType)); // 地段类型
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(cropName)); // 作物名称
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(cropPeriod)); // 发育期
			}
			
			if(!isUpdate)
				pStatement.setString(num++, ini.getValue(ReadIni.SECTION_SOIL_04, ReadIni.CTS_CODE_KEY));
			
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
		di.setHEIGHT(NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, heightOfSationGroundAboveMeanSeaLevel) ? heightOfSationGroundAboveMeanSeaLevel
				: (heightOfSationGroundAboveMeanSeaLevel / 10.0))+"");
		di.setSEND_PHYS("DRDS");
		di.setFILE_SIZE(String.valueOf(file.length()));
	}

	/**
	 * 
	 * @Title: update_soil_data_04
	 * @Description: 更新数据库数据
	 * @param agme_Soil_04s
	 * @param connection
	 * @param recv_time void
	 * @param v_bbb 
	 * @param isUpdate 
	 * @param fileName 
	 * @param loggerBuffer 
	 * @throws:
	 */
	private static void update_soil_data_04(List<Agme_Soil_04> agme_Soil_04s, java.sql.Connection connection, Date recv_time, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {

		ReadIni ini = ReadIni.getIni();

		String d_data_id = ini.getValue(ReadIni.SECTION_SOIL_04, ReadIni.D_DATA_ID_KEY).toUpperCase();

		String update_sql = ini.getValue(ReadIni.SECTION_SOIL_04, ReadIni.UPDATE_SQL_KEY).toUpperCase();
		String insert_sql = ini.getValue(ReadIni.SECTION_SOIL_04, ReadIni.INSERT_SQL_KEY).toUpperCase();

		PreparedStatement pStatement = null;

		for (Agme_Soil_04 agme_Soil_04 : agme_Soil_04s) {
			String db_v_bbb = find_soil_data_04(agme_Soil_04, connection,loggerBuffer); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if (db_v_bbb == null) { // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				isUpdate = false;
				try {
					pStatement = new LoggableStatement(connection, insert_sql);
					insert_one_soil_data_04(recv_time, connection, d_data_id, pStatement, agme_Soil_04,filepath,isUpdate,v_bbb,loggerBuffer);
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
					insert_one_soil_data_04(recv_time, connection, d_data_id, pStatement, agme_Soil_04,filepath,isUpdate,v_bbb,loggerBuffer);
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
	 * @Title: find_soil_data_04
	 * @Description: 查询数据表中的更正标识
	 * @param agme_Soil_04
	 * @param connection
	 * @param loggerBuffer 
	 * @return V_BBB更正标识
	 * @throws:
	 */
	private static String find_soil_data_04(Agme_Soil_04 agme_Soil_04, java.sql.Connection connection, StringBuffer loggerBuffer) {
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		ReadIni ini = ReadIni.getIni();
		String select_sql = ini.getValue(ReadIni.SECTION_SOIL_04, ReadIni.SELECT_SQL_KEY);
		try {

			pstmt = connection.prepareStatement(select_sql);
			int num = 1;
			Date obsTime = agme_Soil_04.getObsTime();

			pstmt.setTimestamp(num++, new Timestamp(obsTime.getTime())); // 资料时间
			pstmt.setString(num++, agme_Soil_04.getStationNumberChina()); // 区站号（字符）
			pstmt.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_04.getGeographyType())); // 地段类型
			pstmt.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_04.getCropName())); // 作物名称
			pstmt.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_04.getCropPeriod())); // 发育期

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
				loggerBuffer.append("\n close ResultSet failed !");
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
	 * @Title: insert_soil_data_05
	 * @Description: 土壤水分05类入库
	 * @param recv_time 收到时间
	 * @param connection 数据库连接
	 * @param agme_Soil_05s 05类数据解析集合
	 * @param v_bbb 
	 * @param isUpdate 
	 * @param fileName 
	 * @param loggerBuffer 
	 * @return DataBaseAction 成功与否标识
	 * @throws:
	 */
	private static DataBaseAction insert_soil_data_05(List<Agme_Soil_05> agme_Soil_05s, java.sql.Connection connection, Date recv_time, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		File file = new File(filepath);
		String fileName = file.getName();
		List<StatDi> listDi = new ArrayList<StatDi>();

		ReadIni ini = ReadIni.getIni();

		String insert_sql = ini.getValue(ReadIni.SECTION_SOIL_05, ReadIni.INSERT_SQL_KEY).toUpperCase();
		String d_data_id = ini.getValue(ReadIni.SECTION_SOIL_05, ReadIni.D_DATA_ID_KEY).toUpperCase();
		PreparedStatement pStatement = null;
		try {
			StatDi di = null;

			// pStatement = connection.prepareStatement(insert_sql);
			pStatement = new LoggableStatement(connection, insert_sql);
			if (StartConfig.getDatabaseType() == 1) {
				pStatement.execute("select last_txc_xid()");
			}
			for (Agme_Soil_05 agme_Soil_05 : agme_Soil_05s) {
				di = new StatDi();
				// 对DI和pStatement设置值
				generatePstAndDi_05(di, agme_Soil_05, pStatement, recv_time, d_data_id,filepath,isUpdate,v_bbb,loggerBuffer);
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
				loggerBuffer.append("\n Batch commit failed：" + fileName + "\n " + ReadIni.SECTION_SOIL_05 + "\n " + e.getMessage());
				for (Agme_Soil_05 agme_Soil_05 : agme_Soil_05s) {
					insert_one_soil_data_05(recv_time, connection, d_data_id, pStatement, agme_Soil_05,filepath,isUpdate,v_bbb,loggerBuffer);
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
	 * @Title: insert_one_soil_data_05
	 * @Description: 单条数据入数据库
	 * @param recv_time
	 * @param connection
	 * @param d_data_id
	 * @param pStatement
	 * @param agme_Soil_05
	 * @param v_bbb 
	 * @param isUpdate 
	 * @param fileName 
	 * @param loggerBuffer 
	 * @return void
	 * @throws:
	 */
	private static void insert_one_soil_data_05(Date recv_time, java.sql.Connection connection, String d_data_id, PreparedStatement pStatement, Agme_Soil_05 agme_Soil_05, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		File file = new File(filepath);
		String fileName = file.getName();
		StatDi di = new StatDi();
		generatePstAndDi_05(di, agme_Soil_05, pStatement, recv_time, d_data_id,filepath,isUpdate,v_bbb,loggerBuffer);
		try {
			pStatement.execute();
//			connection.commit();
		} catch (SQLException e1) {
			di.setPROCESS_STATE("0");// 1成功，0失败
			loggerBuffer.append("\n filename：" + fileName //
					+ "\n " + agme_Soil_05.getStationNumberChina() + " " + TimeUtil.date2String(agme_Soil_05.getObsTime(), TimeUtil.DATE_FMT_YMDHMS) //
					+ "\n execute sql error：" + ((LoggableStatement) pStatement).getQueryString() //
					+ "\n " + e1.getMessage() + "\n " + d_data_id);
		} finally {
			diQueues.offer(di);
		}
	}

	/**
	 * 
	 * @Title: generatePst
	 * @Description: 设置prepareStatement和DI值
	 * @param di DI对象
	 * @param agme_Soil_05 土壤水份02要素对象
	 * @param pStatement prepareStatement
	 * @param recv_time 收到时间
	 * @param v_bbb 
	 * @param isUpdate 
	 * @param fileName 
	 * @param loggerBuffer 
	 * @param ini 配置文件信息
	 * @return void
	 * @throws:
	 */
	private static void generatePstAndDi_05(StatDi di, Agme_Soil_05 agme_Soil_05, PreparedStatement pStatement, Date recv_time, String d_data_id, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileName = file.getName();
		di.setFILE_NAME_O(fileName);
		di.setDATA_TYPE(d_data_id);
		di.setDATA_TYPE_1(ini.getValue(ReadIni.SECTION_SOIL_05, ReadIni.CTS_CODE_KEY));
		di.setTT("");
		di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
		di.setPROCESS_START_TIME(TimeUtil.getSysTime());
		di.setFILE_NAME_N(fileName);
		di.setBUSINESS_STATE("1"); // 1成功，0失败
		di.setPROCESS_STATE("1"); // 1成功，0失败

		int stationNumberN = DEFAULT_VALUE;
		String stationNumberC = agme_Soil_05.getStationNumberChina();
		if (Pattern.matches("\\d{5}", stationNumberC)) { // 如果是5位数字
			stationNumberN = Integer.parseInt(stationNumberC);
		}
		String adminCode = "999999";
		// 根据站号查询行政区划代码
		String info = (String) proMap.get(stationNumberC + "+12");
		
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
		Date obsTime = agme_Soil_05.getObsTime();
		Timestamp d_datetime = new Timestamp(obsTime.getTime());

		Double latitude = agme_Soil_05.getLatitude(); // 纬度
		Double longitude = agme_Soil_05.getLongitude(); // 经度

		Double heightOfSationGroundAboveMeanSeaLevel = agme_Soil_05.getHeightOfSationGroundAboveMeanSeaLevel();// 测站海拔高度
		Double heightOfBarometerAboveMeanSeaLevel = agme_Soil_05.getHeightOfBarometerAboveMeanSeaLevel();// 气压传感器海拔高度

		Double geographyType = agme_Soil_05.getGeographyType(); // 地段类型
		Double cropName = agme_Soil_05.getCropName(); // 作物名称
		Double soilDepth = agme_Soil_05.getSoilDepth(); // 土层深度
		String lengthStr = "00000";
		try { // TODO 修改update语句，去掉主键及入库时间
			if (!isUpdate) {
				// 时间(yyyyMMddHHmmss)_微秒（毫秒转微秒）SSSSSS_站号5_经度10_纬度9_海拔或高度相关8_(V_BBB)_其他字段
				String id = TimeUtil.date2String(obsTime, TimeUtil.DATE_FMT_YMDHMS) //
						+ "_" + (lengthStr.substring(0, 5 - stationNumberC.length()) + stationNumberC) //
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(longitude * 100), 10) //
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(latitude * 100), 9)//
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(heightOfSationGroundAboveMeanSeaLevel * 100), 8)//
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(heightOfBarometerAboveMeanSeaLevel * 100), 8)//
						+ "_" + NumberUtil.FormatNumOrNine(geographyType) //
						+ "_" + NumberUtil.FormatNumOrNine(cropName); //
				if(ArrayUtils.contains(NINE_ARRAYS, soilDepth))
					id += "_" + NumberUtil.FormatNumOrNine(soilDepth);
				else 
					id += "_" + String.valueOf(soilDepth * 10);  // 土层深度

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
				pStatement.setInt(num++, stationNumberN); // 区站号（数字）
			}
			// pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(latitude / 10000.0)); // 纬度
			// pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(longitude / 10000.0)); // 经度
			// pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(heightOfSationGroundAboveMeanSeaLevel)); // 测站海拔高度
			// pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(heightOfBarometerAboveMeanSeaLevel)); // 气压传感器海拔高度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNineOnLatLon(ArrayUtils.contains(NINE_ARRAYS, latitude) ? latitude :ElementValUtil.getlatitude(String.valueOf(latitude)))); // 纬度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNineOnLatLon(ArrayUtils.contains(NINE_ARRAYS, longitude) ? longitude :ElementValUtil.getLongitude(String.valueOf(longitude)))); // 经度
			pStatement.setBigDecimal(num++,
					NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, heightOfSationGroundAboveMeanSeaLevel) ? heightOfSationGroundAboveMeanSeaLevel
							: (heightOfSationGroundAboveMeanSeaLevel / 10.0))); // 测站海拔高度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(
					ArrayUtils.contains(NINE_ARRAYS, heightOfBarometerAboveMeanSeaLevel) ? heightOfBarometerAboveMeanSeaLevel : (heightOfBarometerAboveMeanSeaLevel / 10.0))); // 气压传感器海拔高度

			// 中国行政区划代码
			pStatement.setString(num++, adminCode);

			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getYear(obsTime))); // 资料观测年
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getMonth(obsTime))); // 资料观测月
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getDayOfMonth(obsTime))); // 资料观测日
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getHourOfDay(obsTime))); // 资料观测时
			if (!isUpdate) {
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(geographyType)); // 地段类型
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(cropName)); // 作物名称
				if(ArrayUtils.contains(NINE_ARRAYS, soilDepth))
					pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(soilDepth));
				else
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(soilDepth * 10)));  // 土层深度
			}
			pStatement.setBigDecimal(num ++, NumberUtil.FormatNumOrNine(agme_Soil_05.getSoilStatus())); //土层状态
			pStatement.setString(num++, v_bbb); // 默认000，首次入库可不填，数据库字段默认000

			// 查询或更新条件
			if (isUpdate) {
				pStatement.setTimestamp(num++, d_datetime); // 资料时间
				pStatement.setString(num++, stationNumberC); // 区站号（字符）
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(geographyType)); // 地段类型
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(cropName)); // 作物名称
				
				if(ArrayUtils.contains(NINE_ARRAYS, soilDepth))
					pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(soilDepth));
				else
					pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(soilDepth * 10))); // 土层深度
			}
			
			if(!isUpdate)
				pStatement.setString(num++, ini.getValue(ReadIni.SECTION_SOIL_05, ReadIni.CTS_CODE_KEY));

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
		di.setHEIGHT(NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, heightOfSationGroundAboveMeanSeaLevel) ? heightOfSationGroundAboveMeanSeaLevel
				: (heightOfSationGroundAboveMeanSeaLevel / 10.0))+"");
		di.setSEND_PHYS("DRDS");
		di.setFILE_SIZE(String.valueOf(file.length()));
	}

	/**
	 * 
	 * @Title: update_soil_data_05
	 * @Description: 更新数据库数据
	 * @param agme_Soil_05s
	 * @param connection
	 * @param recv_time void
	 * @param v_bbb 
	 * @param isUpdate 
	 * @param fileName 
	 * @param loggerBuffer 
	 * @throws:
	 */
	private static void update_soil_data_05(List<Agme_Soil_05> agme_Soil_05s, java.sql.Connection connection, Date recv_time, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {

		ReadIni ini = ReadIni.getIni();

		String d_data_id = ini.getValue(ReadIni.SECTION_SOIL_05, ReadIni.D_DATA_ID_KEY).toUpperCase();

		String update_sql = ini.getValue(ReadIni.SECTION_SOIL_05, ReadIni.UPDATE_SQL_KEY).toUpperCase();
		String insert_sql = ini.getValue(ReadIni.SECTION_SOIL_05, ReadIni.INSERT_SQL_KEY).toUpperCase();

		PreparedStatement pStatement = null;

		for (Agme_Soil_05 agme_Soil_05 : agme_Soil_05s) {
			String db_v_bbb = find_soil_data_05(agme_Soil_05, connection,loggerBuffer); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if (db_v_bbb == null) { // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				isUpdate = false;
				try {
					pStatement = new LoggableStatement(connection, insert_sql);
					insert_one_soil_data_05(recv_time, connection, d_data_id, pStatement, agme_Soil_05,filepath,isUpdate,v_bbb,loggerBuffer);
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
			} else if (db_v_bbb.compareTo(v_bbb) < 0) { // 数据库中有该条记录，且V_BBB的值早于当前处理文件的更正标识的值
				isUpdate = true;
				try {
					pStatement = new LoggableStatement(connection, update_sql);
					insert_one_soil_data_05(recv_time, connection, d_data_id, pStatement, agme_Soil_05,filepath,isUpdate,v_bbb,loggerBuffer);
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
	 * @Title: find_soil_data_05
	 * @Description: 查询数据表中的更正标识
	 * @param agme_Soil_05
	 * @param connection
	 * @param loggerBuffer 
	 * @return V_BBB更正标识
	 * @throws:
	 */
	private static String find_soil_data_05(Agme_Soil_05 agme_Soil_05, java.sql.Connection connection, StringBuffer loggerBuffer) {
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		ReadIni ini = ReadIni.getIni();
		String select_sql = ini.getValue(ReadIni.SECTION_SOIL_05, ReadIni.SELECT_SQL_KEY);
		try {

			pstmt = connection.prepareStatement(select_sql);
			int num = 1;
			Date obsTime = agme_Soil_05.getObsTime();

			pstmt.setTimestamp(num++, new Timestamp(obsTime.getTime())); // 资料时间
			pstmt.setString(num++, agme_Soil_05.getStationNumberChina()); // 区站号（字符）
			pstmt.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_05.getGeographyType())); // 地段类型
			pstmt.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_05.getCropName())); // 作物名称
			
			if(ArrayUtils.contains(NINE_ARRAYS, agme_Soil_05.getSoilDepth()))
				pstmt.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_05.getSoilDepth()));
			else
				pstmt.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Soil_05.getSoilDepth() * 10))); // 土层深度
//			pstmt.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_05.getSoilDepth())); // 土层深度

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
	 * @Title: insert_soil_data_06
	 * @Description: 土壤水分06类入库
	 * @param recv_time 收到时间
	 * @param connection 数据库连接
	 * @param agme_Soil_06s 06类数据解析集合
	 * @param v_bbb 
	 * @param isUpdate 
	 * @param fileName 
	 * @param loggerBuffer 
	 * @return DataBaseAction 成功与否标识
	 * @throws:
	 */
	private static DataBaseAction insert_soil_data_06(List<Agme_Soil_06> agme_Soil_06s, java.sql.Connection connection, Date recv_time, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		File file = new File(filepath);
		String fileName = file.getName();
		List<StatDi> listDi = new ArrayList<StatDi>();

		ReadIni ini = ReadIni.getIni();

		String insert_sql = ini.getValue(ReadIni.SECTION_SOIL_06, ReadIni.INSERT_SQL_KEY).toUpperCase();
		String d_data_id = ini.getValue(ReadIni.SECTION_SOIL_06, ReadIni.D_DATA_ID_KEY).toUpperCase();
		PreparedStatement pStatement = null;
		try {
			StatDi di = null;

			// pStatement = connection.prepareStatement(insert_sql);
			pStatement = new LoggableStatement(connection, insert_sql);
			if (StartConfig.getDatabaseType() == 1) {
				pStatement.execute("select last_txc_xid()");
			}
			for (Agme_Soil_06 agme_Soil_06 : agme_Soil_06s) {
				di = new StatDi();
				// 对DI和pStatement设置值
				generatePstAndDi_06(di, agme_Soil_06, pStatement, recv_time, d_data_id,filepath,isUpdate,v_bbb,loggerBuffer,loggerBuffer);
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
				loggerBuffer.append("\n Batch commit failed：" + fileName + "\n " + ReadIni.SECTION_SOIL_06 + "\n " + e.getMessage());
				for (Agme_Soil_06 agme_Soil_06 : agme_Soil_06s) {
					insert_one_soil_data_06(recv_time, connection, d_data_id, pStatement, agme_Soil_06,filepath,isUpdate,v_bbb,loggerBuffer);
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
	 * @Title: insert_one_soil_data_06
	 * @Description: 单条数据入数据库
	 * @param recv_time
	 * @param connection
	 * @param d_data_id
	 * @param pStatement
	 * @param agme_Soil_06
	 * @param v_bbb 
	 * @param isUpdate 
	 * @param fileName 
	 * @param loggerBuffer 
	 * @return void
	 * @throws:
	 */
	private static void insert_one_soil_data_06(Date recv_time, java.sql.Connection connection, String d_data_id, PreparedStatement pStatement, Agme_Soil_06 agme_Soil_06, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		File file = new File(filepath);
		String fileName = file.getName();
		StatDi di = new StatDi();
		generatePstAndDi_06(di, agme_Soil_06, pStatement, recv_time, d_data_id,filepath,isUpdate,v_bbb,loggerBuffer,loggerBuffer);
		try {
			pStatement.execute();
//			connection.commit();
		} catch (SQLException e1) {
			di.setPROCESS_STATE("0");// 1成功，0失败
			loggerBuffer.append("\n filename：" + fileName //
					+ "\n " + agme_Soil_06.getStationNumberChina() + " " + TimeUtil.date2String(agme_Soil_06.getObsTime(), TimeUtil.DATE_FMT_YMDHMS) //
					+ "\n execute sql error：" + ((LoggableStatement) pStatement).getQueryString() //
					+ "\n " + e1.getMessage() + "\n " + d_data_id);
		} finally {
			diQueues.offer(di);
		}
	}

	/**
	 * 
	 * @Title: generatePst
	 * @Description: 设置prepareStatement和DI值
	 * @param di DI对象
	 * @param agme_Soil_06 土壤水份02要素对象
	 * @param pStatement prepareStatement
	 * @param recv_time 收到时间
	 * @param v_bbb 
	 * @param isUpdate 
	 * @param fileName 
	 * @param loggerBuffer 
	 * @param loggerBuffer2 
	 * @param ini 配置文件信息
	 * @return void
	 * @throws:
	 */
	private static void generatePstAndDi_06(StatDi di, Agme_Soil_06 agme_Soil_06, PreparedStatement pStatement, Date recv_time, String d_data_id, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer, StringBuffer loggerBuffer2) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileName = file.getName();
		di.setFILE_NAME_O(fileName);
		di.setDATA_TYPE(d_data_id);
		di.setDATA_TYPE_1(ini.getValue(ReadIni.SECTION_SOIL_06, ReadIni.CTS_CODE_KEY));
		di.setTT("");
		di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
		di.setPROCESS_START_TIME(TimeUtil.getSysTime());
		di.setFILE_NAME_N(fileName);
		di.setBUSINESS_STATE("1"); // 1成功，0失败
		di.setPROCESS_STATE("1"); // 1成功，0失败

		int stationNumberN = DEFAULT_VALUE;
		String stationNumberC = agme_Soil_06.getStationNumberChina();
		if (Pattern.matches("\\d{5}", stationNumberC)) { // 如果是5位数字
			stationNumberN = Integer.parseInt(stationNumberC);
		}
		String adminCode = "999999";
		// 根据站号查询行政区划代码
		String info = (String) proMap.get(stationNumberC + "+12");
		
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
		Date obsTime = agme_Soil_06.getObsTime();
		Timestamp d_datetime = new Timestamp(obsTime.getTime());

		Double latitude = agme_Soil_06.getLatitude(); // 纬度
		Double longitude = agme_Soil_06.getLongitude(); // 经度

		Double heightOfSationGroundAboveMeanSeaLevel = agme_Soil_06.getHeightOfSationGroundAboveMeanSeaLevel();// 测站海拔高度
		Double heightOfBarometerAboveMeanSeaLevel = agme_Soil_06.getHeightOfBarometerAboveMeanSeaLevel();// 气压传感器海拔高度

		Double geographyType = agme_Soil_06.getGeographyType(); // 地段类型
		Double cropName = agme_Soil_06.getCropName(); // 作物名称
		Double cropPeriod = agme_Soil_06.getCropPeriod(); // 发育期
		String lengthStr = "00000";
		try { // TODO 修改update语句，去掉主键及入库时间
			if (!isUpdate) {
				// 时间(yyyyMMddHHmmss)_微秒（毫秒转微秒）SSSSSS_站号5_经度10_纬度9_海拔或高度相关8_(V_BBB)_其他字段
				String id = TimeUtil.date2String(obsTime, TimeUtil.DATE_FMT_YMDHMS) //
						+ "_" + (lengthStr.substring(0, 5 - stationNumberC.length()) + stationNumberC) //
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(longitude * 100), 10) //
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(latitude * 100), 9)//
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(heightOfSationGroundAboveMeanSeaLevel * 100), 8)//
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(heightOfBarometerAboveMeanSeaLevel * 100), 8)//
						+ "_" + NumberUtil.FormatNumOrNine(geographyType) //
						+ "_" + NumberUtil.FormatNumOrNine(cropName) //
//						+ "_" + cropPeriod; // 之前 ， 修改
						+ "_" + NumberUtil.FormatNumOrNine(cropPeriod);

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
				pStatement.setInt(num++, stationNumberN); // 区站号（数字）
			}
			// pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(latitude / 10000.0)); // 纬度
			// pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(longitude / 10000.0)); // 经度
			// pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(heightOfSationGroundAboveMeanSeaLevel)); // 测站海拔高度
			// pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(heightOfBarometerAboveMeanSeaLevel)); // 气压传感器海拔高度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNineOnLatLon(ArrayUtils.contains(NINE_ARRAYS, latitude) ? latitude : ElementValUtil.getlatitude(String.valueOf(latitude)))); // 纬度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNineOnLatLon(ArrayUtils.contains(NINE_ARRAYS, longitude) ? longitude : ElementValUtil.getLongitude(String.valueOf(longitude)))); // 经度
			pStatement.setBigDecimal(num++,
					NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, heightOfSationGroundAboveMeanSeaLevel) ? heightOfSationGroundAboveMeanSeaLevel
							: (heightOfSationGroundAboveMeanSeaLevel / 10.0))); // 测站海拔高度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(
					ArrayUtils.contains(NINE_ARRAYS, heightOfBarometerAboveMeanSeaLevel) ? heightOfBarometerAboveMeanSeaLevel : (heightOfBarometerAboveMeanSeaLevel / 10.0))); // 气压传感器海拔高度

			// 中国行政区划代码
			pStatement.setString(num++, adminCode);

			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getYear(obsTime))); // 资料观测年
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getMonth(obsTime))); // 资料观测月
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getDayOfMonth(obsTime))); // 资料观测日
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getHourOfDay(obsTime))); // 资料观测时
			if (!isUpdate) {
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(geographyType)); // 地段类型
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(cropName)); // 作物名称
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(cropPeriod)); // 发育期
			}
			Double SoilWeightWaterContent_10 = NumberUtil.FormatNumOrNine(agme_Soil_06.getSoilWeightWaterContent_10()).doubleValue();
			//Double SoilWeightWaterContent_10 = NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, agme_Soil_06.getSoilWeightWaterContent_10()) ? SoilWeightWaterContent_10 :SoilWeightWaterContent_10 * 0.1).doubleValue();
			Double SoilWeightWaterContent_20 = NumberUtil.FormatNumOrNine(agme_Soil_06.getSoilWeightWaterContent_20()).doubleValue();
			Double SoilWeightWaterContent_30 = NumberUtil.FormatNumOrNine(agme_Soil_06.getSoilWeightWaterContent_30()).doubleValue();
			Double SoilWeightWaterContent_40 = NumberUtil.FormatNumOrNine(agme_Soil_06.getSoilWeightWaterContent_40()).doubleValue();
			Double SoilWeightWaterContent_50 = NumberUtil.FormatNumOrNine(agme_Soil_06.getSoilWeightWaterContent_50()).doubleValue();
			Double SoilWeightWaterContent_60 = NumberUtil.FormatNumOrNine(agme_Soil_06.getSoilWeightWaterContent_60()).doubleValue();
			Double SoilWeightWaterContent_70 = NumberUtil.FormatNumOrNine(agme_Soil_06.getSoilWeightWaterContent_70()).doubleValue();
			Double SoilWeightWaterContent_80 = NumberUtil.FormatNumOrNine(agme_Soil_06.getSoilWeightWaterContent_80()).doubleValue();
			Double SoilWeightWaterContent_90 = NumberUtil.FormatNumOrNine(agme_Soil_06.getSoilWeightWaterContent_90()).doubleValue();
			Double SoilWeightWaterContent_100 = NumberUtil.FormatNumOrNine(agme_Soil_06.getSoilWeightWaterContent_100()).doubleValue();
			
			if (Math.abs(SoilWeightWaterContent_10 - 999999.0) < 1e-5) {
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(SoilWeightWaterContent_10))); // 10cm土壤重量含水率
			}else{
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(SoilWeightWaterContent_10 * 0.1))); // 10cm土壤重量含水率
			}
			if (Math.abs(SoilWeightWaterContent_20 - 999999.0) < 1e-5) {
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(SoilWeightWaterContent_20))); // 20cm土壤重量含水率
			}else{
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(SoilWeightWaterContent_20 * 0.1))); // 20cm土壤重量含水率
			}
			if (Math.abs(SoilWeightWaterContent_30 - 999999.0) < 1e-5) {
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(SoilWeightWaterContent_30))); // 30cm土壤重量含水率
			}else{
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(SoilWeightWaterContent_30 * 0.1))); // 30cm土壤重量含水率
			}
			if (Math.abs(SoilWeightWaterContent_40 - 999999.0) < 1e-5) {
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(SoilWeightWaterContent_40))); // 40cm土壤重量含水率
			}else{
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(SoilWeightWaterContent_40 * 0.1))); // 40cm土壤重量含水率
			}
			if (Math.abs(SoilWeightWaterContent_50 - 999999.0) < 1e-5) {
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(SoilWeightWaterContent_50))); // 50cm土壤重量含水率
			}else{
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(SoilWeightWaterContent_50 * 0.1))); // 50cm土壤重量含水率
			}
			if (Math.abs(SoilWeightWaterContent_60 - 999999.0) < 1e-5) {
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(SoilWeightWaterContent_60))); // 60cm土壤重量含水率
			}else{
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(SoilWeightWaterContent_60 * 0.1))); // 60cm土壤重量含水率
			}
			if (Math.abs(SoilWeightWaterContent_70 - 999999.0) < 1e-5) {
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(SoilWeightWaterContent_70))); // 70cm土壤重量含水率
			}else{
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(SoilWeightWaterContent_70 * 0.1))); // 70cm土壤重量含水率
			}
			if (Math.abs(SoilWeightWaterContent_80 - 999999.0) < 1e-5) {
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(SoilWeightWaterContent_80))); // 80cm土壤重量含水率
			}else{
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(SoilWeightWaterContent_80 * 0.1))); // 80cm土壤重量含水率
			}
			if (Math.abs(SoilWeightWaterContent_90 - 999999.0) < 1e-5) {
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(SoilWeightWaterContent_90))); // 90cm土壤重量含水率
			}else{
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(SoilWeightWaterContent_90 * 0.1))); // 90cm土壤重量含水率
			}
			if (Math.abs(SoilWeightWaterContent_100 - 999999.0) < 1e-5) {
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(SoilWeightWaterContent_100))); // 100cm土壤重量含水率
			}else{
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(SoilWeightWaterContent_100 * 0.1))); // 100cm土壤重量含水率
			}
		/*	pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_06.getSoilWeightWaterContent_10() * 0.1)); // 10cm土壤重量含水率
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_06.getSoilWeightWaterContent_20() * 0.1)); // 20cm土壤重量含水率
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_06.getSoilWeightWaterContent_30() * 0.1)); // 30cm土壤重量含水率
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_06.getSoilWeightWaterContent_40() * 0.1)); // 40cm土壤重量含水率
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_06.getSoilWeightWaterContent_50() * 0.1)); // 50cm土壤重量含水率
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_06.getSoilWeightWaterContent_60() * 0.1)); // 60cm土壤重量含水率
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_06.getSoilWeightWaterContent_70() * 0.1)); // 70cm土壤重量含水率
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_06.getSoilWeightWaterContent_80() * 0.1)); // 80cm土壤重量含水率
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_06.getSoilWeightWaterContent_90() * 0.1)); // 90cm土壤重量含水率
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_06.getSoilWeightWaterContent_100() * 0.1)); // 100cm土壤重量含水率
*/
			pStatement.setString(num++, v_bbb); // 默认000，首次入库可不填，数据库字段默认000

			// 查询或更新条件
			if (isUpdate) {
				pStatement.setTimestamp(num++, d_datetime); // 资料时间
				pStatement.setString(num++, stationNumberC); // 区站号（字符）
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(geographyType)); // 地段类型
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(cropName)); // 作物名称
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(cropPeriod)); // 发育期
			}
			
			if(!isUpdate)
				pStatement.setString(num++, ini.getValue(ReadIni.SECTION_SOIL_06, ReadIni.CTS_CODE_KEY));
			
		} catch (SQLException e) {
			loggerBuffer.append("\n Batch commit failed: " + e.getMessage());
		}

		di.setIIiii(stationNumberC);
		di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm"));
		di.setPROCESS_END_TIME(TimeUtil.getSysTime());
		di.setRECORD_TIME(TimeUtil.getSysTime());
		di.setLATITUDE(NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, latitude) ? latitude : ElementValUtil.getlatitude(String.valueOf(latitude))) + "");
		di.setLONGTITUDE(NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, longitude) ? longitude : ElementValUtil.getLongitude(String.valueOf(longitude))) + "");
		di.setDATA_UPDATE_FLAG(v_bbb);
		di.setHEIGHT(NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, heightOfSationGroundAboveMeanSeaLevel) ? heightOfSationGroundAboveMeanSeaLevel
				: (heightOfSationGroundAboveMeanSeaLevel / 10.0))+"");
		di.setSEND_PHYS("DRDS");
		di.setFILE_SIZE(String.valueOf(file.length()));
	}

	/**
	 * 
	 * @Title: update_soil_data_06
	 * @Description: 更新数据库数据
	 * @param agme_Soil_06s
	 * @param connection
	 * @param recv_time void
	 * @param v_bbb 
	 * @param isUpdate 
	 * @param fileName 
	 * @param loggerBuffer 
	 * @throws:
	 */
	private static void update_soil_data_06(List<Agme_Soil_06> agme_Soil_06s, java.sql.Connection connection, Date recv_time, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {

		ReadIni ini = ReadIni.getIni();

		String d_data_id = ini.getValue(ReadIni.SECTION_SOIL_06, ReadIni.D_DATA_ID_KEY).toUpperCase();

		String update_sql = ini.getValue(ReadIni.SECTION_SOIL_06, ReadIni.UPDATE_SQL_KEY).toUpperCase();
		String insert_sql = ini.getValue(ReadIni.SECTION_SOIL_06, ReadIni.INSERT_SQL_KEY).toUpperCase();

		PreparedStatement pStatement = null;

		for (Agme_Soil_06 agme_Soil_06 : agme_Soil_06s) {
			String db_v_bbb = find_soil_data_06(agme_Soil_06, connection,loggerBuffer); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if (db_v_bbb == null) { // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				isUpdate = false;
				try {
					pStatement = new LoggableStatement(connection, insert_sql);
					insert_one_soil_data_06(recv_time, connection, d_data_id, pStatement, agme_Soil_06,filepath,isUpdate,v_bbb,loggerBuffer);
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
			} else if (db_v_bbb.compareTo(v_bbb) < 0) { // 数据库中有该条记录，且V_BBB的值早于当前处理文件的更正标识的值
				isUpdate = true;
				try {
					pStatement = new LoggableStatement(connection, update_sql);
					insert_one_soil_data_06(recv_time, connection, d_data_id, pStatement, agme_Soil_06,filepath,isUpdate,v_bbb,loggerBuffer);
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
	 * @Title: find_soil_data_06
	 * @Description: 查询数据表中的更正标识
	 * @param agme_Soil_06
	 * @param connection
	 * @param loggerBuffer 
	 * @return V_BBB更正标识
	 * @throws:
	 */
	private static String find_soil_data_06(Agme_Soil_06 agme_Soil_06, java.sql.Connection connection, StringBuffer loggerBuffer) {
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		ReadIni ini = ReadIni.getIni();
		String select_sql = ini.getValue(ReadIni.SECTION_SOIL_06, ReadIni.SELECT_SQL_KEY);
		try {

			pstmt = connection.prepareStatement(select_sql);
			int num = 1;
			Date obsTime = agme_Soil_06.getObsTime();

			pstmt.setTimestamp(num++, new Timestamp(obsTime.getTime())); // 资料时间
			pstmt.setString(num++, agme_Soil_06.getStationNumberChina()); // 区站号（字符）
			pstmt.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_06.getGeographyType())); // 地段类型
			pstmt.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_06.getCropName())); // 作物名称
			pstmt.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_06.getCropPeriod())); // 土层深度

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
				loggerBuffer.append("\nclose ResultSet failed!");
			}
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				loggerBuffer.append("\nclose PreparedStatement failed!");
			}
		}
		return null;
	}

	/**
	 * 
	 * @Title: insert_soil_data_07
	 * @Description: 土壤水分07类入库
	 * @param recv_time 收到时间
	 * @param connection 数据库连接
	 * @param agme_Soil_07s 07类数据解析集合
	 * @param v_bbb 
	 * @param isUpdate 
	 * @param fileName 
	 * @param loggerBuffer 
	 * @return DataBaseAction 成功与否标识
	 * @throws:
	 */
	private static DataBaseAction insert_soil_data_07(List<Agme_Soil_07> agme_Soil_07s, java.sql.Connection connection, Date recv_time, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		File file = new File(filepath);
		String fileName = file.getName();
		List<StatDi> listDi = new ArrayList<StatDi>();

		ReadIni ini = ReadIni.getIni();

		String insert_sql = ini.getValue(ReadIni.SECTION_SOIL_07, ReadIni.INSERT_SQL_KEY).toUpperCase();
		String d_data_id = ini.getValue(ReadIni.SECTION_SOIL_07, ReadIni.D_DATA_ID_KEY).toUpperCase();
		PreparedStatement pStatement = null;
		try {
			StatDi di = null;

			// pStatement = connection.prepareStatement(insert_sql);
			pStatement = new LoggableStatement(connection, insert_sql);
			if (StartConfig.getDatabaseType() == 1) {
				pStatement.execute("select last_txc_xid()");
			}
			for (Agme_Soil_07 agme_Soil_07 : agme_Soil_07s) {
				di = new StatDi();
				// 对DI和pStatement设置值
				generatePstAndDi_07(di, agme_Soil_07, pStatement, recv_time, d_data_id,filepath,isUpdate,v_bbb,loggerBuffer);
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
				loggerBuffer.append("\n Batch commit failed：" + fileName + "\n " + ReadIni.SECTION_SOIL_07 + "\n " + e.getMessage());
				for (Agme_Soil_07 agme_Soil_07 : agme_Soil_07s) {
					insert_one_soil_data_07(recv_time, connection, d_data_id, pStatement, agme_Soil_07,filepath,isUpdate,v_bbb,loggerBuffer);
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
	 * @Title: insert_one_soil_data_07
	 * @Description: 单条数据入数据库
	 * @param recv_time
	 * @param connection
	 * @param d_data_id
	 * @param pStatement
	 * @param agme_Soil_07
	 * @param v_bbb 
	 * @param isUpdate 
	 * @param fileName 
	 * @param loggerBuffer 
	 * @return void
	 * @throws:
	 */
	private static void insert_one_soil_data_07(Date recv_time, java.sql.Connection connection, String d_data_id, PreparedStatement pStatement, Agme_Soil_07 agme_Soil_07, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		File file = new File(filepath);
		String fileName = file.getName();
		StatDi di = new StatDi();
		generatePstAndDi_07(di, agme_Soil_07, pStatement, recv_time, d_data_id,filepath,isUpdate,v_bbb,loggerBuffer);
		try {
			pStatement.execute();
//			connection.commit();
		} catch (SQLException e1) {
			di.setPROCESS_STATE("0");// 1成功，0失败
			loggerBuffer.append("\n filename：" + fileName //
					+ "\n " + agme_Soil_07.getStationNumberChina() + " " + TimeUtil.date2String(agme_Soil_07.getObsTime(), TimeUtil.DATE_FMT_YMDHMS) //
					+ "\n execute sql error：" + ((LoggableStatement) pStatement).getQueryString() //
					+ "\n " + e1.getMessage() + "\n " + d_data_id);
		} finally {
			diQueues.offer(di);
		}
	}

	/**
	 * 
	 * @Title: generatePst
	 * @Description: 设置prepareStatement和DI值
	 * @param di DI对象
	 * @param agme_Soil_07 土壤水份02要素对象
	 * @param pStatement prepareStatement
	 * @param recv_time 收到时间
	 * @param fileName 
	 * @param v_bbb 
	 * @param isUpdate 
	 * @param loggerBuffer 
	 * @param ini 配置文件信息
	 * @return void
	 * @throws:
	 */
	private static void generatePstAndDi_07(StatDi di, Agme_Soil_07 agme_Soil_07, PreparedStatement pStatement, Date recv_time, String d_data_id, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileName = file.getName();
		di.setFILE_NAME_O(fileName);
		di.setDATA_TYPE(d_data_id);
		di.setDATA_TYPE_1(ini.getValue(ReadIni.SECTION_SOIL_07, ReadIni.CTS_CODE_KEY));
		di.setTT("");
		di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
		di.setPROCESS_START_TIME(TimeUtil.getSysTime());
		di.setFILE_NAME_N(fileName);
		di.setBUSINESS_STATE("1"); // 1成功，0失败
		di.setPROCESS_STATE("1"); // 1成功，0失败

		int stationNumberN = DEFAULT_VALUE;
		String stationNumberC = agme_Soil_07.getStationNumberChina();
		if (Pattern.matches("\\d{5}", stationNumberC)) { // 如果是5位数字
			stationNumberN = Integer.parseInt(stationNumberC);
		}
		String adminCode = "999999";
		// 根据站号查询行政区划代码
		String info = (String) proMap.get(stationNumberC + "+12");
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
		Date obsTime = agme_Soil_07.getObsTime();
		Timestamp d_datetime = new Timestamp(obsTime.getTime());

		Double latitude = agme_Soil_07.getLatitude(); // 纬度
		Double longitude = agme_Soil_07.getLongitude(); // 经度

		Double heightOfSationGroundAboveMeanSeaLevel = agme_Soil_07.getHeightOfSationGroundAboveMeanSeaLevel();// 测站海拔高度
		Double heightOfBarometerAboveMeanSeaLevel = agme_Soil_07.getHeightOfBarometerAboveMeanSeaLevel();// 气压传感器海拔高度

		Double geographyType = agme_Soil_07.getGeographyType(); // 地段类型
		Double cropName = agme_Soil_07.getCropName(); // 作物名称
		String lengthStr = "00000";
		try { // TODO 修改update语句，去掉主键及入库时间
			if (!isUpdate) {
				// 时间(yyyyMMddHHmmss)_微秒（毫秒转微秒）SSSSSS_站号5_经度10_纬度9_海拔或高度相关8_(V_BBB)_其他字段
				String id = TimeUtil.date2String(obsTime, TimeUtil.DATE_FMT_YMDHMS) //
						+ "_" + (lengthStr.substring(0, 5 - stationNumberC.length()) + stationNumberC) //
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(longitude * 100), 10) //
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(latitude * 100), 9)//
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(heightOfSationGroundAboveMeanSeaLevel * 100), 8)//
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(heightOfBarometerAboveMeanSeaLevel * 100), 8)//
						+ "_" + NumberUtil.FormatNumOrNine(geographyType) //
						+ "_" + NumberUtil.FormatNumOrNine(cropName);

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
				pStatement.setInt(num++, stationNumberN); // 区站号（数字）
			}
			// pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(latitude / 10000.0)); // 纬度
			// pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(longitude / 10000.0)); // 经度
			// pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(heightOfSationGroundAboveMeanSeaLevel)); // 测站海拔高度
			// pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(heightOfBarometerAboveMeanSeaLevel)); // 气压传感器海拔高度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNineOnLatLon(ArrayUtils.contains(NINE_ARRAYS, latitude) ? latitude : ElementValUtil.getlatitude(String.valueOf(latitude)))); // 纬度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNineOnLatLon(ArrayUtils.contains(NINE_ARRAYS, longitude) ? longitude : ElementValUtil.getLongitude(String.valueOf(longitude)))); // 经度
			pStatement.setBigDecimal(num++,
					NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, heightOfSationGroundAboveMeanSeaLevel) ? heightOfSationGroundAboveMeanSeaLevel
							: (heightOfSationGroundAboveMeanSeaLevel / 10.0))); // 测站海拔高度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(
					ArrayUtils.contains(NINE_ARRAYS, heightOfBarometerAboveMeanSeaLevel) ? heightOfBarometerAboveMeanSeaLevel : (heightOfBarometerAboveMeanSeaLevel / 10.0))); // 气压传感器海拔高度

			// 中国行政区划代码
			pStatement.setString(num++, adminCode);

			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getYear(obsTime))); // 资料观测年
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getMonth(obsTime))); // 资料观测月
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getDayOfMonth(obsTime))); // 资料观测日
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getHourOfDay(obsTime))); // 资料观测时
			if (!isUpdate) {
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(geographyType)); // 地段类型
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(cropName)); // 作物名称
			}

			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_07.getDrySoilThickness())); // 干土层百度
			
			if(agme_Soil_07.getGroundWaterLevel() == null)
				pStatement.setBigDecimal(num++, new BigDecimal(999999));
			else
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, agme_Soil_07.getGroundWaterLevel()) ? agme_Soil_07.getGroundWaterLevel()
						: (agme_Soil_07.getGroundWaterLevel() / 10.0)));
//			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_07.getGroundWaterLevel() == null ? 999999 : agme_Soil_07.getGroundWaterLevel() * 0.1)); // 地下水位

			pStatement.setString(num++, v_bbb); // 默认000，首次入库可不填，数据库字段默认000

			// 查询或更新条件
			if (isUpdate) {
				pStatement.setTimestamp(num++, d_datetime); // 资料时间
				pStatement.setString(num++, stationNumberC); // 区站号（字符）
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(geographyType)); // 地段类型
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(cropName)); // 作物名称
			}
			
			if(!isUpdate)
				pStatement.setString(num++, ini.getValue(ReadIni.SECTION_SOIL_07, ReadIni.CTS_CODE_KEY));
			
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
		di.setHEIGHT(NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, heightOfSationGroundAboveMeanSeaLevel) ? heightOfSationGroundAboveMeanSeaLevel
				: (heightOfSationGroundAboveMeanSeaLevel / 10.0))+"");
		di.setSEND_PHYS("DRDS");
		di.setFILE_SIZE(String.valueOf(file.length()));
	}

	/**
	 * 
	 * @Title: update_soil_data_07
	 * @Description: 更新数据库数据
	 * @param agme_Soil_07s
	 * @param connection
	 * @param recv_time void
	 * @param v_bbb 
	 * @param isUpdate 
	 * @param fileName 
	 * @param loggerBuffer 
	 * @throws:
	 */
	private static void update_soil_data_07(List<Agme_Soil_07> agme_Soil_07s, java.sql.Connection connection, Date recv_time, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {

		ReadIni ini = ReadIni.getIni();

		String d_data_id = ini.getValue(ReadIni.SECTION_SOIL_07, ReadIni.D_DATA_ID_KEY).toUpperCase();

		String update_sql = ini.getValue(ReadIni.SECTION_SOIL_07, ReadIni.UPDATE_SQL_KEY).toUpperCase();
		String insert_sql = ini.getValue(ReadIni.SECTION_SOIL_07, ReadIni.INSERT_SQL_KEY).toUpperCase();

		PreparedStatement pStatement = null;

		for (Agme_Soil_07 agme_Soil_07 : agme_Soil_07s) {
			String db_v_bbb = find_soil_data_07(agme_Soil_07, connection,loggerBuffer); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if (db_v_bbb == null) { // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				isUpdate = false;
				try {
					pStatement = new LoggableStatement(connection, insert_sql);
					insert_one_soil_data_07(recv_time, connection, d_data_id, pStatement, agme_Soil_07,filepath,isUpdate,v_bbb,loggerBuffer);
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
					insert_one_soil_data_07(recv_time, connection, d_data_id, pStatement, agme_Soil_07,filepath,isUpdate,v_bbb,loggerBuffer);
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
	 * @Title: find_soil_data_07
	 * @Description: 查询数据表中的更正标识
	 * @param agme_Soil_07
	 * @param connection
	 * @param loggerBuffer 
	 * @return V_BBB更正标识
	 * @throws:
	 */
	private static String find_soil_data_07(Agme_Soil_07 agme_Soil_07, java.sql.Connection connection, StringBuffer loggerBuffer) {
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		ReadIni ini = ReadIni.getIni();
		String select_sql = ini.getValue(ReadIni.SECTION_SOIL_07, ReadIni.SELECT_SQL_KEY);
		try {

			pstmt = connection.prepareStatement(select_sql);
			int num = 1;
			Date obsTime = agme_Soil_07.getObsTime();

			pstmt.setTimestamp(num++, new Timestamp(obsTime.getTime())); // 资料时间
			pstmt.setString(num++, agme_Soil_07.getStationNumberChina()); // 区站号（字符）
			pstmt.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_07.getGeographyType())); // 地段类型
			pstmt.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_07.getCropName())); // 作物名称

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
				loggerBuffer.append("\nclose ResultSet failed!");
			}
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				loggerBuffer.append("\nclose PreparedStatement failed!");
			}
		}
		return null;
	}

	/**
	 * 
	 * @Title: insert_soil_data_08
	 * @Description: 土壤水分08类入库
	 * @param recv_time 收到时间
	 * @param connection 数据库连接
	 * @param agme_Soil_08s 08类数据解析集合
	 * @param v_bbb 
	 * @param isUpdate 
	 * @param fileName 
	 * @param loggerBuffer 
	 * @return DataBaseAction 成功与否标识
	 * @throws:
	 */
	private static DataBaseAction insert_soil_data_08(List<Agme_Soil_08> agme_Soil_08s, java.sql.Connection connection, Date recv_time, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		File file = new File(filepath);
		String fileName = file.getName();
		List<StatDi> listDi = new ArrayList<StatDi>();

		ReadIni ini = ReadIni.getIni();

		String insert_sql = ini.getValue(ReadIni.SECTION_SOIL_08, ReadIni.INSERT_SQL_KEY).toUpperCase();
		String d_data_id = ini.getValue(ReadIni.SECTION_SOIL_08, ReadIni.D_DATA_ID_KEY).toUpperCase();
		PreparedStatement pStatement = null;
		try {
			StatDi di = null;

			// pStatement = connection.prepareStatement(insert_sql);
			pStatement = new LoggableStatement(connection, insert_sql);
			if (StartConfig.getDatabaseType() == 1) {
				pStatement.execute("select last_txc_xid()");
			}
			for (Agme_Soil_08 agme_Soil_08 : agme_Soil_08s) {
				di = new StatDi();
				// 对DI和pStatement设置值
				generatePstAndDi_08(di, agme_Soil_08, pStatement, recv_time, d_data_id,filepath,isUpdate,v_bbb,loggerBuffer);
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
				loggerBuffer.append("\n Batch Commit failed：" + fileName + "\n " + ReadIni.SECTION_SOIL_08 + "\n " + e.getMessage());
				for (Agme_Soil_08 agme_Soil_08 : agme_Soil_08s) {
					insert_one_soil_data_08(recv_time, connection, d_data_id, pStatement, agme_Soil_08,filepath,isUpdate,v_bbb,loggerBuffer);
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
					loggerBuffer.append("\n close tatement  error" + e.getMessage());
				}
			}
		}
	}

	/**
	 * 
	 * @Title: insert_one_soil_data_08
	 * @Description: 单条数据入数据库
	 * @param recv_time
	 * @param connection
	 * @param d_data_id
	 * @param pStatement
	 * @param agme_Soil_08
	 * @param v_bbb 
	 * @param isUpdate 
	 * @param fileName 
	 * @param loggerBuffer 
	 * @return void
	 * @throws:
	 */
	private static void insert_one_soil_data_08(Date recv_time, java.sql.Connection connection, String d_data_id, PreparedStatement pStatement, Agme_Soil_08 agme_Soil_08, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		File file = new File(filepath);
		String fileName = file.getName();
		StatDi di = new StatDi();
		generatePstAndDi_08(di, agme_Soil_08, pStatement, recv_time, d_data_id,filepath,isUpdate,v_bbb,loggerBuffer);
		try {
			pStatement.execute();
//			connection.commit();
		} catch (SQLException e1) {
			di.setPROCESS_STATE("0");// 1成功，0失败
			loggerBuffer.append("\n filename：" + fileName //
					+ "\n " + agme_Soil_08.getStationNumberChina() + " " + TimeUtil.date2String(agme_Soil_08.getObsTime(), TimeUtil.DATE_FMT_YMDHMS) //
					+ "\n execute sql error：" + ((LoggableStatement) pStatement).getQueryString() //
					+ "\n " + e1.getMessage() + "\n " + d_data_id);
		} finally {
			diQueues.offer(di);
		}
	}

	/**
	 * 
	 * @Title: generatePst
	 * @Description: 设置prepareStatement和DI值
	 * @param di DI对象
	 * @param agme_Soil_08 土壤水份02要素对象
	 * @param pStatement prepareStatement
	 * @param recv_time 收到时间
	 * @param v_bbb 
	 * @param isUpdate 
	 * @param fileName 
	 * @param loggerBuffer 
	 * @param ini 配置文件信息
	 * @return void
	 * @throws:
	 */
	private static void generatePstAndDi_08(StatDi di, Agme_Soil_08 agme_Soil_08, PreparedStatement pStatement, Date recv_time, String d_data_id, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileName = file.getName();
		di.setFILE_NAME_O(fileName);
		di.setDATA_TYPE(d_data_id);
		di.setDATA_TYPE_1(ini.getValue(ReadIni.SECTION_SOIL_08, ReadIni.CTS_CODE_KEY));
		di.setTT("");
		di.setTRAN_TIME(TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm"));
		di.setPROCESS_START_TIME(TimeUtil.getSysTime());
		di.setFILE_NAME_N(fileName);
		di.setBUSINESS_STATE("1"); //1成功，0失败
		di.setPROCESS_STATE("1"); // 1成功，0失败

		int stationNumberN = DEFAULT_VALUE;
		String stationNumberC = agme_Soil_08.getStationNumberChina();
		if (Pattern.matches("\\d{5}", stationNumberC)) { // 如果是5位数字
			stationNumberN = Integer.parseInt(stationNumberC);
		}
		String adminCode = "999999";
		// 根据站号查询行政区划代码
		String info = (String) proMap.get(stationNumberC + "+12");
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
		Date obsTime = agme_Soil_08.getObsTime();
		Timestamp d_datetime = new Timestamp(obsTime.getTime());

		Double latitude = agme_Soil_08.getLatitude(); // 纬度
		Double longitude = agme_Soil_08.getLongitude(); // 经度

		Double heightOfSationGroundAboveMeanSeaLevel = agme_Soil_08.getHeightOfSationGroundAboveMeanSeaLevel();// 测站海拔高度
		Double heightOfBarometerAboveMeanSeaLevel = agme_Soil_08.getHeightOfBarometerAboveMeanSeaLevel();// 气压传感器海拔高度

		Double geographyType = agme_Soil_08.getGeographyType(); // 地段类型
		Double cropName = agme_Soil_08.getCropName(); // 作物名称
		String lengthStr = "00000";
		try { // TODO 修改update语句，去掉主键及入库时间
			
			if (!isUpdate) {
				String occurTime = "999999";
				
				try {
					occurTime = new String(agme_Soil_08.getOccurTime().getBytes(), "UTF8");
				} catch (UnsupportedEncodingException e) {
					loggerBuffer.append("OccurTime format error! " + fileName);
				} // 出现时间
				
				
				// 时间(yyyyMMddHHmmss)_微秒（毫秒转微秒）SSSSSS_站号5_经度10_纬度9_海拔或高度相关8_(V_BBB)_其他字段
				String id = TimeUtil.date2String(obsTime, TimeUtil.DATE_FMT_YMDHMS) //
						+ "_" + (lengthStr.substring(0, 5 - stationNumberC.length()) + stationNumberC) //
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(longitude * 100), 10) //
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(latitude * 100), 9)//
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(heightOfSationGroundAboveMeanSeaLevel * 100), 8)//
//						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(heightOfBarometerAboveMeanSeaLevel * 100), 8)//
						+ "_" + occurTime
						+ "_" + NumberUtil.FormatNumOrNine(geographyType) //
						+ "_" + NumberUtil.FormatNumOrNine(cropName);

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
				pStatement.setInt(num++, stationNumberN); // 区站号（数字）
			}
			// pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(latitude / 10000.0)); // 纬度
			// pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(longitude / 10000.0)); // 经度
			// pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(heightOfSationGroundAboveMeanSeaLevel)); // 测站海拔高度
			// pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(heightOfBarometerAboveMeanSeaLevel)); // 气压传感器海拔高度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNineOnLatLon(ArrayUtils.contains(NINE_ARRAYS, latitude) ? latitude : ElementValUtil.getlatitude(String.valueOf(latitude)))); // 纬度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNineOnLatLon(ArrayUtils.contains(NINE_ARRAYS, longitude) ? longitude : ElementValUtil.getLongitude(String.valueOf(longitude)))); // 经度
			pStatement.setBigDecimal(num++,
					NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, heightOfSationGroundAboveMeanSeaLevel) ? heightOfSationGroundAboveMeanSeaLevel
							: (heightOfSationGroundAboveMeanSeaLevel / 10.0))); // 测站海拔高度
			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(
					ArrayUtils.contains(NINE_ARRAYS, heightOfBarometerAboveMeanSeaLevel) ? heightOfBarometerAboveMeanSeaLevel : (heightOfBarometerAboveMeanSeaLevel / 10.0))); // 气压传感器海拔高度

			// 中国行政区划代码
			pStatement.setString(num++, adminCode);

			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getYear(obsTime))); // 资料观测年
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getMonth(obsTime))); // 资料观测月
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getDayOfMonth(obsTime))); // 资料观测日
			pStatement.setBigDecimal(num++, new BigDecimal(TimeUtil.getHourOfDay(obsTime))); // 资料观测时
			if (!isUpdate) {
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(geographyType)); // 地段类型
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(cropName)); // 作物名称
			}

			pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_08.getPreOrIrrigationOrInfiltrationPro())); // 降水灌溉与渗透
			
			if(ArrayUtils.contains(NINE_ARRAYS, agme_Soil_08.getPreOrIrrigationOrInfiltrationDepth()))
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_08.getPreOrIrrigationOrInfiltrationDepth()));
			else 
				pStatement.setBigDecimal(num++, new BigDecimal(String.valueOf(agme_Soil_08.getPreOrIrrigationOrInfiltrationDepth() * 0.1))); // 降水灌溉量或渗透深度
			
//			byte b [] = agme_Soil_08.getOccurTime().getBytes();
			try {
				pStatement.setString(num++, new String(agme_Soil_08.getOccurTime().getBytes(), "UTF8"));
			} catch (UnsupportedEncodingException e) {
				pStatement.setString(num++, "999999");
				loggerBuffer.append("OccurTime format error! " + fileName);
			} // 出现时间
			pStatement.setString(num++, v_bbb); // 默认000，首次入库可不填，数据库字段默认000

			// 查询或更新条件
			if (isUpdate) {
				pStatement.setTimestamp(num++, d_datetime); // 资料时间
				pStatement.setString(num++, stationNumberC); // 区站号（字符）
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(geographyType)); // 地段类型
				pStatement.setBigDecimal(num++, NumberUtil.FormatNumOrNine(cropName)); // 作物名称
				try {
					pStatement.setString(num++, new String(agme_Soil_08.getOccurTime().getBytes(), "UTF8"));
				} catch (UnsupportedEncodingException e) {
					pStatement.setString(num++, "999999");
					loggerBuffer.append("OccurTime format error! " + fileName);
				} // 出现时间
			}
			
			if(!isUpdate)
				pStatement.setString(num++, ini.getValue(ReadIni.SECTION_SOIL_08, ReadIni.CTS_CODE_KEY));
			
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
		di.setHEIGHT(NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, heightOfSationGroundAboveMeanSeaLevel) ? heightOfSationGroundAboveMeanSeaLevel
				: (heightOfSationGroundAboveMeanSeaLevel / 10.0))+"");
		di.setSEND_PHYS("DRDS");
		di.setFILE_SIZE(String.valueOf(file.length()));
	}

	/**
	 * 
	 * @Title: update_soil_data_08
	 * @Description: 更新数据库数据
	 * @param agme_Soil_08s
	 * @param connection
	 * @param recv_time void
	 * @param v_bbb 
	 * @param isUpdate 
	 * @param fileName 
	 * @param loggerBuffer 
	 * @throws:
	 */
	private static void update_soil_data_08(List<Agme_Soil_08> agme_Soil_08s, java.sql.Connection connection, Date recv_time, String filepath, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {

		ReadIni ini = ReadIni.getIni();

		String d_data_id = ini.getValue(ReadIni.SECTION_SOIL_08, ReadIni.D_DATA_ID_KEY).toUpperCase();

		String update_sql = ini.getValue(ReadIni.SECTION_SOIL_08, ReadIni.UPDATE_SQL_KEY).toUpperCase();
		String insert_sql = ini.getValue(ReadIni.SECTION_SOIL_08, ReadIni.INSERT_SQL_KEY).toUpperCase();

		PreparedStatement pStatement = null;

		for (Agme_Soil_08 agme_Soil_08 : agme_Soil_08s) {
			String db_v_bbb = find_soil_data_08(agme_Soil_08, connection,loggerBuffer); // 插入前，从DB中查找该条记录的状态，有、无、更正状态
			if (db_v_bbb == null) { // 该更正报之前数据库中没有该条记录，直接插入，但是要给UPDATE_TIME赋值
				isUpdate = false;
				try {
					pStatement = new LoggableStatement(connection, insert_sql);
					insert_one_soil_data_08(recv_time, connection, d_data_id, pStatement, agme_Soil_08,filepath,isUpdate,v_bbb,loggerBuffer);
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					if (pStatement != null) {
						try {
							pStatement.close();
						} catch (SQLException e) {
							loggerBuffer.append("\n  close Statement error" + e.getMessage());
						}
					}
				}
			} else if (db_v_bbb.compareTo(v_bbb) < 0) { // 数据库中有该条记录，且V_BBB的值早于当前处理文件的更正标识的值
				isUpdate = true;
				try {
					pStatement = new LoggableStatement(connection, update_sql);
					insert_one_soil_data_08(recv_time, connection, d_data_id, pStatement, agme_Soil_08,filepath,isUpdate,v_bbb,loggerBuffer);
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
	 * @Title: find_soil_data_08
	 * @Description: 查询数据表中的更正标识
	 * @param agme_Soil_08
	 * @param connection
	 * @param loggerBuffer 
	 * @return V_BBB更正标识
	 * @throws:
	 */
	private static String find_soil_data_08(Agme_Soil_08 agme_Soil_08, java.sql.Connection connection, StringBuffer loggerBuffer) {
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		ReadIni ini = ReadIni.getIni();
		String select_sql = ini.getValue(ReadIni.SECTION_SOIL_08, ReadIni.SELECT_SQL_KEY);
		try {

			pstmt = connection.prepareStatement(select_sql);
			int num = 1;
			Date obsTime = agme_Soil_08.getObsTime();

			pstmt.setTimestamp(num++, new Timestamp(obsTime.getTime())); // 资料时间
			pstmt.setString(num++, agme_Soil_08.getStationNumberChina()); // 区站号（字符）
			pstmt.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_08.getGeographyType())); // 地段类型
			pstmt.setBigDecimal(num++, NumberUtil.FormatNumOrNine(agme_Soil_08.getCropName())); // 作物名称

			pstmt.setString(num++, agme_Soil_08.getOccurTime());
			
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
				loggerBuffer.append("\n close  ResultSet failed!");
			}
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				loggerBuffer.append("\nclose PreparedStatement failed!");
			}
		}
		return null;
	}

}
