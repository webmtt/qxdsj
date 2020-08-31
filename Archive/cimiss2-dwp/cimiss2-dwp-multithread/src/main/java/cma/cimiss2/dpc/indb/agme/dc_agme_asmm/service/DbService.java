package cma.cimiss2.dpc.indb.agme.dc_agme_asmm.service;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.cimiss2.dwp.tools.config.CTSCodeMap;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cma.cimiss2.dpc.decoder.agme.DecodeASM;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
import cma.cimiss2.dpc.decoder.bean.cawn.ASMLevel;
import cma.cimiss2.dpc.decoder.bean.cawn.ZAgmeASM;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class DbService {
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static List<StatDi> listDi = new ArrayList<StatDi>();;

//	static Map<String, Object> proMap = StationInfo.getProMap();
	public static BlockingQueue<StatDi> diQueues;	
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}

	/**
	 * @param cts_code2 
	 * @param loggerBuffer 
	 * @param v_bbb
	 * @param isRevised 
	 * @param cts_code 
	 * @Title: processSuccessReport 
	 * @Description: TODO(解码成功结果集入库) 
	 * @param parseResult 解码结果集
	 * @param filepath  文件路径
	 * @param recv_time  消息接收时间
	 * @return  
	 *    DataBaseAction   入库状态
	 * @throws
	 */
	public static DataBaseAction processSuccessReport(ParseResult<ZAgmeASM> parseResult,
			Date recv_time, String cts_code,String  filepath, boolean isRevised, String v_bbb, StringBuffer loggerBuffer, List<CTSCodeMap> ctsCodeMaps) {
		File file = new File(filepath);
		String fileN = file.getName();
		java.sql.Connection connection = null;
		java.sql.Connection reportConnection = null;
		try {
			// 获取数据库连接
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			// 获取解码结果集
			List<ZAgmeASM> list = parseResult.getData();
			// 更正报文
			if(isRevised){
				connection.setAutoCommit(false);
				// updatekeyDB中，先更新要素表，再更新键表，之后再提交
				updatekeyDB(list, connection,recv_time,filepath,v_bbb, loggerBuffer,ctsCodeMaps);
			}else{	
				// 插入报文
//				DataBaseAction insertKeyDB =insertKeyDB(list, connection,recv_time,fileN,v_bbb, loggerBuffer,ctsCodeMaps);
//				if(insertKeyDB == DataBaseAction.SUCCESS) {   // 批量插入成功
//					insertEleDB(list, connection, recv_time,fileN,v_bbb, loggerBuffer,ctsCodeMaps);
//				}else if (insertKeyDB == DataBaseAction.BATCH_ERROR) {
//					for (int i = 0; i < list.size(); i++) {
//						DataBaseAction insertOneLine_key_db = insertOneLine_key_db(list.get(i), connection, recv_time,fileN,v_bbb, loggerBuffer,ctsCodeMaps);
//						if(insertOneLine_key_db == DataBaseAction.SUCCESS) {
//							insertOneLine_Ele_db(list.get(i), connection, recv_time,fileN,v_bbb, loggerBuffer,ctsCodeMaps);
//						}
//					}
//				}else {
//					return insertKeyDB;
//				}
				
				DataBaseAction insertEleDB = insertEleDB(list, connection, recv_time,fileN,v_bbb, loggerBuffer,ctsCodeMaps);
				if(insertEleDB == DataBaseAction.SUCCESS) {   // 要素批量插入成功，键表批量插入
					DataBaseAction insertKeyDB = insertKeyDB(list, connection,recv_time,filepath,v_bbb, loggerBuffer,ctsCodeMaps);
					if(insertKeyDB == DataBaseAction.BATCH_ERROR){ // 键表、要素表均会回滚，再单条插入
						for (int i = 0; i < list.size(); i++) {
							DataBaseAction insertOneLine_ele_db = insertOneLine_Ele_db(list.get(i), connection, recv_time,fileN,v_bbb, loggerBuffer,ctsCodeMaps);
							if(insertOneLine_ele_db == DataBaseAction.SUCCESS) {
								insertOneLine_key_db(list.get(i), connection, recv_time,filepath,v_bbb, loggerBuffer,ctsCodeMaps);
							}
						}
					}
				}else if (insertEleDB == DataBaseAction.BATCH_ERROR) { // 要素表批量提交失败
					for (int i = 0; i < list.size(); i++) {
						DataBaseAction insertOneLine_ele_db = insertOneLine_Ele_db(list.get(i), connection, recv_time,fileN,v_bbb, loggerBuffer,ctsCodeMaps);
						if(insertOneLine_ele_db == DataBaseAction.SUCCESS) {
							insertOneLine_key_db(list.get(i), connection, recv_time,filepath,v_bbb, loggerBuffer,ctsCodeMaps);
						}
					}
				}else {
					return insertEleDB;
				}
			}
			
			@SuppressWarnings("rawtypes")
			List<ReportInfo> reportInfos = parseResult.getReports();
		    String[] fnames = fileN.split("_");
		    reportConnection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
		    DbService.reportInfoToDb(reportInfos, reportConnection,v_bbb, recv_time, fnames[3], fnames[1],loggerBuffer,ctsCodeMaps,filepath);
		    //ReportInfoService.reportInfoToDb(reportInfos, reportConnection,v_bbb, recv_time, fnames[3], fnames[1],ctsCodeMaps);
			return DataBaseAction.SUCCESS;
		} catch (SQLException e) {
			return DataBaseAction.CONNECTION_ERROR;
		}finally {
			for (int j = 0; j < listDi.size(); j++) {
				diQueues.offer(listDi.get(j));
			}
			listDi.clear();
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n database connection close error"+e.getMessage());
				}
			}
			
			if(reportConnection != null) {
				try {
					reportConnection.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n database connection close error"+e.getMessage());
				}
			}
		}
	}

	/**
	 * @param ctsCodeMaps 
	 * @param loggerBuffer 
	 * @param v_bbb 
	 * @param fileN 
	 * @Title: insertKeyDB 插入数据  key表
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param list 解码结果集
	 * @param connection  数据连接对象
	 * @param recv_time  消息接收时间
	 * @return  
	 *    DataBaseAction  数据入库状态
	 * @throws
	 */
	@SuppressWarnings("deprecation")
	private static DataBaseAction insertKeyDB(List<ZAgmeASM> list, java.sql.Connection connection,Date recv_time, String filepath, String v_bbb, StringBuffer loggerBuffer, List<CTSCodeMap> ctsCodeMaps) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		try {
			String sql = "INSERT INTO "+StartConfig.keyTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,"
					+"D_UPDATE_TIME,D_DATETIME,V01301,V01300,V05001,V06001,V07001,V_ACODE,"
					+"V04001,V04002,V04003,V04004,V04005,V71115,V_BBB,D_SOURCE_ID) VALUES("
					+"?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+"?, ?, ?, ?, ?, ?, ?, ? ,?, ?)";
			if(connection != null){
				// 创建PreparedStatement
				pStatement = connection.prepareStatement(sql); 
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				for (int i = 0; i < list.size(); i++) {
					// 获取ZAgmeASM 对象
					ZAgmeASM agmeASM = list.get(i);
					// 设置DI信息
					StatDi di = new StatDi();					
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMaps.get(0).getSod_code());
					di.setDATA_TYPE_1(ctsCodeMaps.get(0).getCts_code());
					di.setTT("ASM");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //0成功，1失败
					di.setPROCESS_STATE("1");  //0成功，1失败
					
					// 根根配置文件  获取站点基本信息
					String info = (String) proMap.get(agmeASM.getStationNumberChina() + "+13");
					int adminCode = 999999;
					
					if(info != null) {
						String[] infos = info.split(",");
						if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
							adminCode = Integer.parseInt(infos[5]);
						if(agmeASM.getHeightOfSationGroundAboveMeanSeaLevel().doubleValue() == 999999.0 && infos.length >= 4 && !infos[3].trim().equals("null")){
							agmeASM.setHeightOfSationGroundAboveMeanSeaLevel(Double.parseDouble(infos[3]));
						}
					}

					if(adminCode == 999999){
						info = (String) proMap.get(agmeASM.getStationNumberChina() + "+01");
						if(info == null) {
							loggerBuffer.append("\n In the configuration file, the station number does not exist" + agmeASM.getStationNumberChina());
						}else {
							String[] infos = info.split(",");
							if(infos.length >= 6)
								adminCode = Integer.parseInt(infos[5]);
							if(agmeASM.getHeightOfSationGroundAboveMeanSeaLevel().doubleValue() == 999999.0 && infos.length >= 4 && !infos[3].trim().equals("null")){
								agmeASM.setHeightOfSationGroundAboveMeanSeaLevel(Double.parseDouble(infos[3]));
							}
						}
					}
					
					
					int ii=1;
					String primkey = sdf.format(agmeASM.getObservationTime())+"_"+agmeASM.getStationNumberChina();
				    pStatement.setString(ii++, primkey);//记录标识
					pStatement.setString(ii++, ctsCodeMaps.get(0).getSod_code());//资料标识
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//入库时间
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));//收到时间
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间
					pStatement.setTimestamp(ii++, new Timestamp(agmeASM.getObservationTime().getTime()));//资料时间				
					pStatement.setString(ii++, agmeASM.getStationNumberChina());//区站号(字符)
					pStatement.setInt(ii++,Integer.parseInt(StationCodeUtil.stringToAscii(agmeASM.getStationNumberChina())) );//区站号(数字)
					pStatement.setBigDecimal(ii++, new BigDecimal(agmeASM.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));//纬度
					pStatement.setBigDecimal(ii++, new BigDecimal(agmeASM.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));//经度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(agmeASM.getHeightOfSationGroundAboveMeanSeaLevel())));//测站海拔高度
					pStatement.setInt(ii++, adminCode);//中国行政区代码
					pStatement.setBigDecimal(ii++, new BigDecimal(agmeASM.getObservationTime().getYear() + 1900));//资料观测年
					pStatement.setBigDecimal(ii++, new BigDecimal(agmeASM.getObservationTime().getMonth()+1));//资料观测月
					pStatement.setBigDecimal(ii++, new BigDecimal(agmeASM.getObservationTime().getDate()));//资料观测日
					pStatement.setInt(ii++, agmeASM.getObservationTime().getHours());//资料观测时
					pStatement.setInt(ii++, agmeASM.getObservationTime().getMinutes());//资料观测分
					pStatement.setString(ii++, agmeASM.getMeasureLocationIndication());//测量地段标识
					pStatement.setString(ii++, v_bbb);//更正标识
					pStatement.setString(ii++, ctsCodeMaps.get(0).getCts_code());
					
					//	System.out.println(((LoggableStatement)pStatement).getQueryString());
					di.setIIiii(agmeASM.getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(agmeASM.getObservationTime(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setDATA_UPDATE_FLAG(v_bbb);
					di.setLATITUDE(agmeASM.getLatitude().toString());
					di.setLONGTITUDE(agmeASM.getLongitude().toString());
					di.setHEIGHT(agmeASM.getHeightOfSationGroundAboveMeanSeaLevel().toString());
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(file.length()));
					// 批量提交
					pStatement.addBatch();		
					listDi.add(di);
					
				} //end for 
				try {
					// 执行批量
					pStatement.executeBatch();
					// 手动提交
					connection.commit();
					return DataBaseAction.SUCCESS;
				} catch (Exception e) {
					connection.rollback();
					
					pStatement.clearParameters();
					pStatement.clearBatch();
					loggerBuffer.append("\n Batch commit failed ："+fileN);
					return DataBaseAction.BATCH_ERROR;
				}
				
			} else {
				loggerBuffer.append("\n database connect error");
				return DataBaseAction.CONNECTION_ERROR;
			}						
									
		} catch (SQLException e) {		
			loggerBuffer.append("\n database connect close error："+e.getMessage());
			return DataBaseAction.CONNECTION_ERROR;
		}finally {
			try {
				if(pStatement != null)
					pStatement.close();
			} catch (SQLException e) {
				loggerBuffer.append("\n database connect close error："+e.getMessage());
			}			
		}
	}


	/**
	 * @param ctsCodeMaps 
	 * @param loggerBuffer 
	 * @param v_bbb 
	 * @param fileN 
	 * @Title: insertEleDB 
	 * @Description: TODO(批量提交要素文件) 
	 * @param list 解码结果集
	 * @param connection  数据库连接对象
	 * @param recv_time   消息接收时间
	 * @return  
	 *    DataBaseAction   数据入库状态
	 * @throws
	 */
	private static  DataBaseAction insertEleDB(List<ZAgmeASM> list, java.sql.Connection connection,
			Date recv_time, String fileN, String v_bbb, StringBuffer loggerBuffer, List<CTSCodeMap> ctsCodeMaps) {
		PreparedStatement pStatement = null;
		try {
			// sql 语句
			// chy 去掉了 D_ELE_ID
			String sql = "INSERT INTO "+StartConfig.valueTable()+" (D_RECORD_ID,D_DATETIME,D_UPDATE_TIME,V01301,V07061,V71105,"
					+ "V71102,V71104,V71107,Q71105,Q71102,Q71104,Q71107,V_BBB, D_SOURCE_ID) VALUES (?,?, ?,?,?, ?, ?, ?, ?,"
					+ " ?, ?, ?, ?,?, ?)";
			if(connection !=null){
				pStatement = connection.prepareStatement(sql); 
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

				for (int i = 0; i < list.size(); i++){
					ZAgmeASM agmeASM = list.get(i);	
					// 获取层次数
					for (int j = 0; j < agmeASM.getAsm_soil().size(); j++) {
						ASMLevel asmLevel = agmeASM.getAsm_soil().get(j);
						int ii=1;
						String primkey = sdf.format(agmeASM.getObservationTime())+"_"+agmeASM.getStationNumberChina();
//						pStatement.setString(ii++, primkey+"_" + (asmLevel.getSoilLevelLabeling()));//要素标识  之前为：(int)(asmLevel.getSoilLevelLabeling()*100)
						pStatement.setString(ii++, primkey);//记录标识
						pStatement.setTimestamp(ii++, new Timestamp(agmeASM.getObservationTime().getTime()));//资料时间
						pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//资料时间
						pStatement.setString(ii++, agmeASM.getStationNumberChina());
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(asmLevel.getSoilLevelLabeling())));//土壤深度
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(asmLevel.getSoilVolumeMoistureContent())));//土壤体积含水量
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(asmLevel.getSoilRelativeHumidity())));//土壤相对湿度
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(asmLevel.getSoilWeightMoistureContent())));//土壤重量含水率
						pStatement.setInt(ii++,asmLevel.getSoilAavailableWaterContent());	//土壤有效水分贮存量
						pStatement.setInt(ii++, 9);//土壤体积含水量质控码
						pStatement.setInt(ii++, 9);//土壤相对湿度质控码
						pStatement.setInt(ii++, 9);//土壤重量含水率质控码
						pStatement.setInt(ii++, 9);//土壤有效水分贮存量质控码	
						pStatement.setString(ii++, v_bbb);//更正标识
						pStatement.setString(ii++, ctsCodeMaps.get(0).getCts_code());
						//批量提交
//						if(asmLevel.getSoilVolumeMoistureContent() >=0 && asmLevel.getSoilRelativeHumidity() >= 0 && asmLevel.getSoilWeightMoistureContent() >= 0){
							pStatement.addBatch();
//						}
//						else
//							continue;
					}
				}
				
				try {
					pStatement.executeBatch();
//					connection.commit();
					return DataBaseAction.SUCCESS;
				} catch (Exception e) {
					pStatement.clearParameters();
					pStatement.clearBatch();
//					connection.rollback();
					loggerBuffer.append("\n Batch commit failed ："+fileN );
					return DataBaseAction.BATCH_ERROR;
					
				}
			}else {
				loggerBuffer.append("\n database connect error");
				return DataBaseAction.CONNECTION_ERROR;
			}
		} catch (Exception e) {
			loggerBuffer.append("\n database connect create PreparedStatement error" + e.getMessage());
			return DataBaseAction.CONNECTION_ERROR;
		}finally {
			if(pStatement != null) {
				try {
					pStatement.close();
					pStatement = null;
				} catch (SQLException e) {
					loggerBuffer.append("\n close database error ");
				}
				
			}
		}
		
	}
	@SuppressWarnings("resource")
	private static DataBaseAction insertOneLine_Ele_db(ZAgmeASM zAgmeASM, java.sql.Connection connection, Date recv_time, String fileN, String v_bbb, StringBuffer loggerBuffer, List<CTSCodeMap> ctsCodeMaps) {
		PreparedStatement pStatement = null;
		// chy 去掉 D_ELE_ID
		String sql = "INSERT INTO "+StartConfig.valueTable()+" (D_RECORD_ID,D_DATETIME,D_UPDATE_TIME,V01301,V07061,V71105,"
				+ "V71102,V71104,V71107,Q71105,Q71102,Q71104,Q71107,V_BBB, D_SOURCE_ID) VALUES (?,?,?,?, ?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, ?)";

		if(connection !=null){
			try {
				pStatement = new LoggableStatement(connection, sql); 
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				
				for (int i = 0; i < zAgmeASM.getAsm_soil().size(); i++) {
					ASMLevel asmLevel = zAgmeASM.getAsm_soil().get(i);
					int ii = 1;
					String primkey = sdf.format(zAgmeASM.getObservationTime())+"_"+zAgmeASM.getStationNumberChina();
//					pStatement.setString(ii++, primkey+"_" + asmLevel.getSoilLevelLabeling());//要素标识 之前为：(int)(asmLevel.getSoilLevelLabeling()*100)
					pStatement.setString(ii++, primkey);//记录标识
					pStatement.setTimestamp(ii++, new Timestamp(zAgmeASM.getObservationTime().getTime()));//资料时间
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//资料时间
					pStatement.setString(ii++, zAgmeASM.getStationNumberChina());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(asmLevel.getSoilLevelLabeling())));//土壤深度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(asmLevel.getSoilVolumeMoistureContent())));//土壤体积含水量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(asmLevel.getSoilRelativeHumidity())));//土壤相对湿度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(asmLevel.getSoilWeightMoistureContent())));//土壤重量含水率
					pStatement.setInt(ii++,asmLevel.getSoilAavailableWaterContent());	//土壤有效水分贮存量
					pStatement.setInt(ii++, 9);//土壤体积含水量质控码
					pStatement.setInt(ii++, 9);//土壤相对湿度质控码
					pStatement.setInt(ii++, 9);//土壤重量含水率质控码
					pStatement.setInt(ii++, 9);//土壤有效水分贮存量质控码	
					pStatement.setString(ii++, v_bbb);//更正标识
					pStatement.setString(ii++, StartConfig.ctsCode());
					
//					if(asmLevel.getSoilVolumeMoistureContent() >=0 && asmLevel.getSoilRelativeHumidity() >= 0 && asmLevel.getSoilWeightMoistureContent() >= 0){
						pStatement.addBatch();
//					}
//					else
//						continue;
				}
				
				try {
					pStatement.executeBatch();
					//connection.commit();
					return DataBaseAction.SUCCESS;
				} catch (Exception e) {
//					connection.rollback();
					loggerBuffer.append("\n filename："+fileN+
							"\n " + zAgmeASM.getStationNumberChina() + " " + sdf.format(zAgmeASM.getObservationTime())
							+"\n "+e.getMessage());
					return DataBaseAction.INSERT_ERROR;
				}
			}catch (Exception e) {
				loggerBuffer.append("\n database connect create PreparedStatement " +e.getMessage());
				return DataBaseAction.CONNECTION_ERROR;
			}
			finally {
				if(pStatement != null)
					try{
						pStatement.close();
					}catch (Exception e2) {
						infoLogger.error("\n Close pStatement error" + e2.getMessage());
					}
			}
		}else {
			loggerBuffer.append("\n database connect error");
			return DataBaseAction.CONNECTION_ERROR;
		}
		
	}



	@SuppressWarnings("deprecation")
	private static DataBaseAction insertOneLine_key_db(ZAgmeASM asm, java.sql.Connection connection,
			Date recv_time, String filepath, String v_bbb, StringBuffer loggerBuffer, List<CTSCodeMap> ctsCodeMaps) {
		Map<String, Object> proMap = StationInfo.getProMap();
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.keyTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,"
				+"D_UPDATE_TIME,D_DATETIME,V01301,V01300,V05001,V06001,V07001,V_ACODE,"
				+"V04001,V04002,V04003,V04004,V04005,V71115,V_BBB,D_SOURCE_ID) VALUES("
				+"?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
				+"?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		if(connection !=null){
			try {
				connection.setAutoCommit(false);
				pStatement = new LoggableStatement(connection, sql); 
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				StatDi di = new StatDi();				
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(ctsCodeMaps.get(0).getSod_code());
				di.setDATA_TYPE_1(ctsCodeMaps.get(0).getCts_code());
				di.setTT("");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //1成功，0失败
				di.setPROCESS_STATE("1");  //1成功，0失败
				
				int adminCode = 999999;
				
				String info = (String) proMap.get(asm.getStationNumberChina() + "+13");
				if(info != null) {
					String[] infos = info.split(",");
					if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
						adminCode = Integer.parseInt(infos[5]);
					if(asm.getHeightOfSationGroundAboveMeanSeaLevel().doubleValue() == 999999.0 && infos.length >= 4 && !infos[3].trim().equals("null")){
						asm.setHeightOfSationGroundAboveMeanSeaLevel(Double.parseDouble(infos[3]));
					}
				}
				
				if(adminCode == 999999){
					info = (String) proMap.get(asm.getStationNumberChina() + "+01");
					if(info == null) {
						loggerBuffer.append("\n In the configuration file, the station number does not exist" + asm.getStationNumberChina());
					}else {
						String[] infos = info.split(",");
						if(infos.length >= 6)
							adminCode = Integer.parseInt(infos[5]);
						if(asm.getHeightOfSationGroundAboveMeanSeaLevel().doubleValue() == 999999.0 && infos.length >= 4 && !infos[3].trim().equals("null")){
							asm.setHeightOfSationGroundAboveMeanSeaLevel(Double.parseDouble(infos[3]));
						}
					}
				}
				
				
			    int ii=1;
			    int strYear = asm.getObservationTime().getYear() + 1900;
				int strMonth = asm.getObservationTime().getMonth() + 1;
				int strDate = asm.getObservationTime().getDate();
				String primkey = sdf.format(asm.getObservationTime())+"_"+asm.getStationNumberChina();
				pStatement.setString(ii++, primkey);//记录标识
				pStatement.setString(ii++, ctsCodeMaps.get(0).getSod_code());//资料标识
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//入库时间
				pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));//收到时间
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间
				pStatement.setTimestamp(ii++, new Timestamp(asm.getObservationTime().getTime()));//资料时间				
				pStatement.setString(ii++, asm.getStationNumberChina());//区站号(字符)
				pStatement.setInt(ii++,Integer.parseInt(StationCodeUtil.stringToAscii(asm.getStationNumberChina())) );//区站号(数字)
				pStatement.setBigDecimal(ii++, new BigDecimal(asm.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));//纬度
				pStatement.setBigDecimal(ii++, new BigDecimal(asm.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));//经度
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(asm.getHeightOfSationGroundAboveMeanSeaLevel())));//测站海拔高度
				pStatement.setInt(ii++, adminCode);//中国行政区代码
				pStatement.setBigDecimal(ii++, new BigDecimal(strYear));
				pStatement.setBigDecimal(ii++, new BigDecimal(strMonth));
				pStatement.setBigDecimal(ii++, new BigDecimal(strDate));
				pStatement.setInt(ii++, asm.getObservationTime().getHours());//资料观测时
				pStatement.setInt(ii++, asm.getObservationTime().getMinutes());//资料观测分
				pStatement.setString(ii++, asm.getMeasureLocationIndication());//测量地段标识
				pStatement.setString(ii++, v_bbb);//测量标识
				pStatement.setString(ii++, StartConfig.ctsCode());
				
				di.setIIiii(asm.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(asm.getObservationTime(), "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				di.setDATA_UPDATE_FLAG(v_bbb);
				di.setLATITUDE(asm.getLatitude().toString());
				di.setLONGTITUDE(asm.getLongitude().toString());
				di.setHEIGHT(asm.getHeightOfSationGroundAboveMeanSeaLevel().toString());
				di.setSEND_PHYS("DRDS");
				di.setFILE_SIZE(String.valueOf(file.length()));

				try {
					pStatement.execute();
					connection.commit();
					listDi.add(di);
					return DataBaseAction.SUCCESS;
					
				} catch (SQLException e) {
					connection.rollback();//发生错误 ，传进来的连接回滚
					di.setPROCESS_STATE("0");//1成功，0失败
					listDi.add(di);	
					loggerBuffer.append("\n filename："+fileN+
							"\n " + asm.getStationNumberChina() + " " + sdf.format(asm.getObservationTime())
							+"\n execute sql error："+((LoggableStatement)pStatement).getQueryString()+"\n "+e.getMessage());
					return DataBaseAction.INSERT_ERROR;
				}
			}catch (SQLException e) {
				loggerBuffer.append("\n create Statement error"+e.getMessage());
				return DataBaseAction.INSERT_ERROR;
			}finally {
				if(pStatement != null) {
					try {
						pStatement.close();
					} catch (SQLException e) {
						e.printStackTrace();
						loggerBuffer.append("\n close Statement error"+e.getMessage());
						
					}
				}	
			}
		}
				
		
				
		return DataBaseAction.SUCCESS;		
	}
	
	@SuppressWarnings("deprecation")
	private static DataBaseAction updatekeyDB(List<ZAgmeASM> list, java.sql.Connection connection, Date recv_time, String filepath, String v_bbb, StringBuffer loggerBuffer, List<CTSCodeMap> ctsCodeMaps) {
		File file = new File(filepath);
		String fileN = file.getName();
		PreparedStatement Pstat = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		int i=0;
		//list
		for( i =0;i<list.size();i++){
			String vbbbInDB = null;
			//插入前，从DB中查找该条记录的状态，有、无、更正状态
			vbbbInDB = findVBBB_key(list.get(i),connection, loggerBuffer,StartConfig.keyTable());
			if(vbbbInDB == null){//该更正报之前数据库中没有该条记录，直接插入,给UPDATE_TIME赋值
				DataBaseAction dataBaseAction = insertOneLine_Ele_db(list.get(i), connection, recv_time,fileN,v_bbb, loggerBuffer,ctsCodeMaps);
				if(dataBaseAction == DataBaseAction.SUCCESS)
					insertOneLine_key_db(list.get(i), connection, recv_time,filepath,v_bbb, loggerBuffer,ctsCodeMaps);	
			}
			else if (vbbbInDB.compareTo(v_bbb)<0) {
				StatDi di = new StatDi();
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(ctsCodeMaps.get(0).getSod_code());
				di.setDATA_TYPE_1(ctsCodeMaps.get(0).getCts_code());
				di.setTT(v_bbb);
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //0成功，1失败
				di.setPROCESS_STATE("1");  //0成功，1失败	
				
				String sql ="UPDATE "+StartConfig.keyTable()+" SET"
						+ " V_BBB=?,D_UPDATE_TIME=?,V71115=?"
						+ " WHERE V01301=? AND V04001=?"
						+ " AND V04002=? AND V04003=? AND V04004=? AND V04005=? ";
				if(connection != null){
					try {
						Pstat = new LoggableStatement(connection, sql);
						
						int ii = 1;
						int strYear = list.get(i).getObservationTime().getYear() + 1900;
						int strMonth = list.get(i).getObservationTime().getMonth() + 1;
						int strDate = list.get(i).getObservationTime().getDate();
						Pstat.setString(ii++, v_bbb);
						Pstat.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						Pstat.setString(ii++, list.get(i).getMeasureLocationIndication());
						
						Pstat.setString(ii++, list.get(i).getStationNumberChina());
						Pstat.setInt(ii++, strYear);
						Pstat.setInt(ii++, strMonth);
						Pstat.setInt(ii++, strDate);
						Pstat.setInt(ii++, list.get(i).getObservationTime().getHours());
						Pstat.setInt(ii++, list.get(i).getObservationTime().getMinutes());
						di.setIIiii(list.get(i).getStationNumberChina());
						di.setDATA_TIME(TimeUtil.date2String(list.get(i).getObservationTime(), "yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setDATA_UPDATE_FLAG(v_bbb);
						di.setLATITUDE(list.get(i).getLatitude().toString());
						di.setLONGTITUDE(list.get(i).getLongitude().toString());
						di.setHEIGHT(list.get(i).getHeightOfSationGroundAboveMeanSeaLevel().toString());
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(file.length()));
						
						try{
							
							updateEle(list.get(i),recv_time,connection,filepath,v_bbb, loggerBuffer,ctsCodeMaps);
							
							Pstat.execute();
							connection.commit();
							listDi.add(di);
						}
						catch(SQLException e){	
							connection.rollback();
							Pstat.clearParameters();
							
							di.setPROCESS_STATE("1");//0成功，1失败
							listDi.add(di);		
							loggerBuffer.append("\n filename："+fileN+
									"\n " + list.get(i).getStationNumberChina() + " " + sdf.format(list.get(i).getObservationTime())
									+"\n execute sql error："+((LoggableStatement)Pstat).getQueryString()+"\n "+e.getMessage());
						}				
					}	catch (SQLException e) {
					loggerBuffer.append("\n create Statement error: "+ e.getMessage());
					}finally {
						try {
							if(Pstat != null)
								Pstat.close();
						} catch (SQLException e) {
							loggerBuffer.append("\n close Statement error: "+ e.getMessage());
						}	
					}
				}else
					loggerBuffer.append("\n database connect error");		
			} // end else if	
			else{ // 数据库中有该条记录，且V_BBB的值晚于当前处理文件的更正标识的值
			// 不进行插入或更新	
			}
		} // end for
		
		
		
		return DataBaseAction.SUCCESS;
	}


	//@SuppressWarnings("unused")
	private static void updateEle(ZAgmeASM zAgmeASM,Date  recv_time ,java.sql.Connection connection, String filepath, String v_bbb, StringBuffer loggerBuffer, List<CTSCodeMap> ctsCodeMaps) {
		File file = new File(filepath);
		String fileN = file.getName();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String primary_key =sdf.format(zAgmeASM.getObservationTime())+"_"+zAgmeASM.getStationNumberChina();					 
        PreparedStatement pStatement = null;
		StatDi di = new StatDi();				
		di.setFILE_NAME_O(fileN);
		di.setDATA_TYPE(ctsCodeMaps.get(0).getSod_code());
		di.setDATA_TYPE_1(ctsCodeMaps.get(0).getCts_code());
		di.setTT(v_bbb);			
		di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
		di.setPROCESS_START_TIME(TimeUtil.getSysTime());
		di.setFILE_NAME_N(fileN);
		di.setBUSINESS_STATE("1"); //0成功，1失败
		di.setPROCESS_STATE("1");  //0成功，1失败	
		
		String sql ="UPDATE "+StartConfig.valueTable()+" SET"
				+ " V_BBB=?,  V07061=?, V71105=?, V71102=?, V71104=?, V71107=?, Q71105=?, Q71102=?, Q71104=?, Q71107=?"
//				+ "	WHERE D_RECORD_ID=? AND V07061=? ";
				+ "	WHERE D_datetime=? and V01301=? AND V07061=? ";
		
		if(connection != null){
			try{
				pStatement = new LoggableStatement(connection, sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				int j =0;
				int soil_size = zAgmeASM.getAsm_soil().size();
				while(j<soil_size){
					int ii = 1;
					int q=9;
					ASMLevel asmLevel = zAgmeASM.getAsm_soil().get(j);
					pStatement.setString(ii++, v_bbb);//更正标识
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(asmLevel.getSoilLevelLabeling())));//土壤深度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(asmLevel.getSoilVolumeMoistureContent())));//土壤体积含水量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(asmLevel.getSoilRelativeHumidity())));//土壤相对湿度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(asmLevel.getSoilWeightMoistureContent())));//土壤重量含水率
					pStatement.setInt(ii++,asmLevel.getSoilAavailableWaterContent());	//土壤有效水分贮存量
					pStatement.setInt(ii++, q);//土壤体积含水量质控码
					pStatement.setInt(ii++, q);//土壤相对湿度质控码
					pStatement.setInt(ii++, q);//土壤重量含水率质控码
					pStatement.setInt(ii++, q);//土壤有效水分贮存量质控码
//					pStatement.setString(ii++,primary_key);//记录标识
					// chy 修改
					pStatement.setTimestamp(ii++, new Timestamp(zAgmeASM.getObservationTime().getTime()));
					pStatement.setString(ii++, zAgmeASM.getStationNumberChina());
					
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(asmLevel.getSoilLevelLabeling())));//土壤深度
					di.setIIiii(zAgmeASM.getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(zAgmeASM.getObservationTime(), "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setDATA_UPDATE_FLAG(v_bbb);
					di.setLATITUDE(zAgmeASM.getLatitude().toString());
					di.setLONGTITUDE(zAgmeASM.getLongitude().toString());
					di.setHEIGHT(zAgmeASM.getHeightOfSationGroundAboveMeanSeaLevel().toString());
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(file.length()));
					
					try{
						pStatement.execute();
						//connection.commit();
						listDi.add(di);
					}
					catch(SQLException e){							
						di.setPROCESS_STATE("1");//0成功，1失败
						listDi.add(di);		
						loggerBuffer.append("\n filename："+fileN+
								"\n " + zAgmeASM.getStationNumberChina() + " " + sdf.format(zAgmeASM.getObservationTime())
								+"\n execute sql error："+((LoggableStatement)pStatement).getQueryString()+"\n "+e.getMessage());
					}	
					
					j=j+1;
				}
					
				
				
			}	catch (SQLException e) {
			loggerBuffer.append("\n create Statement error: "+ e.getMessage());
			}finally {
				try {
					if(pStatement != null)
						pStatement.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n close Statement error: "+ e.getMessage());
				}	
			}
		}else
			loggerBuffer.append("\n database connect error");			
		
	}
	
	/**
	 * @param tablename 
	 * @param loggerBuffer 
	 * @Title: findVBBB_key 
	 * @Description: TODO(更正报文  需要查询原始报文是否存在) 
	 * @param zAgmeASM
	 * @param connection  数据库连接对象
	 * @return  
	 *    String  更正标识  如果原始报文不存在 则返回  null
	 * @throws
	 */
	private static String findVBBB_key(ZAgmeASM zAgmeASM, java.sql.Connection connection, StringBuffer loggerBuffer, String tablename) {
		PreparedStatement Pstmt = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		ResultSet resultSet = null;
		String sql ="SELECT V_BBB FROM "+tablename+" "
				// chy 修改
//				+ " WHERE D_RECORD_ID = ? ";
				+ " WHERE D_datetime = ? and V01301=?";
		try{	
			
			if(connection != null){
				Pstmt = connection.prepareStatement(sql);
			    String  D_RECORD_ID = sdf.format(zAgmeASM.getObservationTime())+"_"+zAgmeASM.getStationNumberChina();
//				Pstmt.setString(1, D_RECORD_ID);
			    // chy 修改
			    Pstmt.setTimestamp(1, new Timestamp(zAgmeASM.getObservationTime().getTime()));
			    Pstmt.setString(2, zAgmeASM.getStationNumberChina());
			    
			    resultSet = Pstmt.executeQuery();
				if(resultSet.next())
					return resultSet.getString(1);
			}
		}catch(SQLException e){
			loggerBuffer.append("\n create PreparedStatement failed" + e.getMessage());
			
		}finally {
			if(Pstmt != null) {
				try {
					Pstmt.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n close  PreparedStatement fileed" + e.getMessage());
				}
			}
			if(resultSet != null)
				try {
					resultSet.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n close  resultSet fileed" + e.getMessage());
				}
		}
		return null;
	}
	@SuppressWarnings({ "rawtypes", "deprecation" })
	private static void reportInfoToDb(List<ReportInfo> reportInfos, Connection connection, String v_bbb, Date recv_time,String v_cccc, String v_tt, StringBuffer loggerBuffer, List<CTSCodeMap> codeMaps,String  filepath) {
		PreparedStatement pStatement = null;
		// chy 去掉D_RECORD_ID
		String sql = "INSERT INTO "+codeMaps.get(0).getReport_table_name()+" (D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,"
				+ "V_BBB,V_CCCC,V01301,V01300,V05001,V06001,V_ACODE,"
				+ "V04001,V04002,V04003,V04004,V04005,V04006,"
				+ "V_TT,V_ELE_KIND,V01323,V_LEN,V_REPORT,D_SOURCE_ID) VALUES"
				+ "(?,?,?,?,?,"
				+ "?,?,?,?,?,"
				+ "?,?,?,?,?,"
				+ "?,?,?,?,?,"
				+ "?,?,?,?)";
		try {
			pStatement = new LoggableStatement(connection, sql);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			File file=new File(filepath);
			String fileN=file.getName();
			for (int i = 0; i < reportInfos.size(); i++) {
				try {
					AgmeReportHeader agmeReportHeader = (AgmeReportHeader) reportInfos.get(i).getT();
					StatDi di = new StatDi();	
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(codeMaps.get(0).getReport_sod_code());
					di.setDATA_TYPE_1(codeMaps.get(0).getCts_code());
					di.setTT(v_tt);			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1");  //1成功，0失败	
					
					String CropType = agmeReportHeader.getCropType().replaceAll("-", "");
					String primkey = sdf.format(agmeReportHeader.getReport_time())+"_"+agmeReportHeader.getStationNumberChina()+"_"+CropType+"_"+v_tt+"_"+v_bbb;
//					pStatement.setString(1, primkey);
					int ii = 1;
					pStatement.setString(ii++, codeMaps.get(0).getReport_sod_code());
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(agmeReportHeader.getReport_time().getTime()));
					pStatement.setString(ii++, v_bbb);
					pStatement.setString(ii++, v_cccc);
					pStatement.setString(ii++, agmeReportHeader.getStationNumberChina());
					pStatement.setInt(ii++, Integer.parseInt(StationCodeUtil.stringToAscii(agmeReportHeader.getStationNumberChina())));
					pStatement.setBigDecimal(ii++, new BigDecimal(agmeReportHeader.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(agmeReportHeader.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setInt(ii++, 0);
					pStatement.setInt(ii++, agmeReportHeader.getReport_time().getYear()+1900);
					pStatement.setInt(ii++, agmeReportHeader.getReport_time().getMonth()+1);
					pStatement.setInt(ii++, agmeReportHeader.getReport_time().getDate());
					pStatement.setInt(ii++, agmeReportHeader.getReport_time().getHours());
					pStatement.setInt(ii++, agmeReportHeader.getReport_time().getMinutes());
					pStatement.setInt(ii++, agmeReportHeader.getReport_time().getSeconds());
					pStatement.setString(ii++, v_tt);
					pStatement.setString(ii++, CropType);
					pStatement.setString(ii++, CropType);
					pStatement.setInt(ii++, reportInfos.get(i).getReport().length());
					pStatement.setString(ii++, reportInfos.get(i).getReport());
					pStatement.setString(ii++, codeMaps.get(0).getCts_code());
					
					di.setIIiii(agmeReportHeader.getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(agmeReportHeader.getReport_time(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLONGTITUDE(String.valueOf(agmeReportHeader.getLongitude()));
					di.setLATITUDE(String.valueOf(agmeReportHeader.getLatitude()));

					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
					di.setDATA_UPDATE_FLAG(v_bbb);
					di.setHEIGHT(String.valueOf(agmeReportHeader.getHeightOfSationGroundAboveMeanSeaLevel()));
					try{
						pStatement.execute();
						connection.commit();
						listDi.add(di);
					}catch (SQLException e) {
						di.setPROCESS_STATE("0");//1成功，0失败
						listDi.add(di);
					}
				} catch (Exception e) {
					loggerBuffer.append("sql error:"+e.getMessage());
					continue;
				}
			}
		} catch (SQLException e) {
			loggerBuffer.append("Database connection error: " + e.getMessage());
		}finally {
			if(pStatement != null) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					
				}
			}
			
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					
				}
			}
		}
		
		
	}
	public static void main(String[] args) {
		boolean isRevised = false;
		String v_bbb = "000";
		String cts_code="E.0001.0003.R001";
		StringBuffer loggerBuffer = new StringBuffer();
		List<CTSCodeMap> ctsCodeMaps=new ArrayList<CTSCodeMap>();
		StartConfig.setConfigFile("config/agme_cli_config.properties");
		String ctS = StartConfig.ctsCode();
		String sodSs[] = StartConfig.sodCodes();
		String reportSods[] = StartConfig.reportSodCodes();
		String valuetables[] = StartConfig.valueTables();
		String reportTable = StartConfig.reportTable();
		for(int i = 0; i < valuetables.length; i ++){
			CTSCodeMap codeMap = new CTSCodeMap();
			codeMap.setCts_code(ctS);
			codeMap.setSod_code(sodSs[i]);
			codeMap.setReport_sod_code(reportSods[i]);
			codeMap.setValue_table_name(valuetables[i]);
			codeMap.setReport_table_name(reportTable);
			ctsCodeMaps.add(codeMap);
		}
		File file = new File("D:\\DataTest\\agme\\Z_AGME_C_BEHK_20190516164932_O_ASM-FTM.txt");
		String fileN = file.getName();
		Date recv_time = new Date(file.lastModified());
		// 判断文件是否为  更正报文
		if(fileN.contains("-CC")){
			isRevised = true;
			v_bbb = fileN.substring(fileN.indexOf("-CC") + 1, fileN.indexOf("-CC") + 4);
		}
		else{
			isRevised = false;
			v_bbb = "000";
		}
		DecodeASM decodeAsm = new DecodeASM();
		// 解析文件  返回结果集
		ParseResult<ZAgmeASM> parseResult = decodeAsm.decodeFile(file);
		if(parseResult.isSuccess()){
			DbService.processSuccessReport(parseResult, recv_time,cts_code, fileN, isRevised,v_bbb, loggerBuffer,ctsCodeMaps);
		}
	}
		

}
