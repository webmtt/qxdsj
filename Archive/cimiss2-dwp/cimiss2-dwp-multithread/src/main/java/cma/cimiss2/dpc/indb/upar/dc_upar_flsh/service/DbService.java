package cma.cimiss2.dpc.indb.upar.dc_upar_flsh.service;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.concurrent.BlockingQueue;

import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.upar.LightingData;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.upar.DecodeLDP;

// 2019-5-21日修改  闪电位置纬度 闪电位置经度 setScale(4, BigDecimal.ROUND_HALF_UP),改为setScale(6, BigDecimal.ROUND_HALF_UP)
public class DbService {
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	static String type = StartConfig.sodCode();
	
	public static BlockingQueue<StatDi> getDiQueues(){
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues){
		DbService.diQueues = diQueues;
	}
	
	public static void main(String[] args) {
		DecodeLDP decodeLDP =new DecodeLDP();
		String filepath = "C:\\Users\\cuihongyuan\\Desktop\\对比\\高空资料 对比验证\\ADTD系统雷电定位数据-CIMISS与云平台入库对比验证 (崔红元提交)\\Z_P_LPD__C_BABJ_20190228183201_2019_03_01.txt";
		File file = new File(filepath);
		Date recv_time = new Date(file.lastModified());
		ParseResult<LightingData> parseResult = decodeLDP.DecodeLPD(file);
		DataBaseAction action = null;
		StringBuffer loggerBuffer = new StringBuffer();

//		if (StartConfig.getDatabaseType() == 1) {
			action = DbService.processSuccessReport(parseResult, recv_time, StartConfig.ctsCode(),loggerBuffer, filepath);
			System.out.println("insertDBService over!");
//		}
	
	}
	
	
	/**
	 * 
	 * @Title: processSuccessReport
	 * @Description:(ADTD系统雷电定位数据的处理)
	 * @param parseResult
	 * @param recv_time
	 * @param cts_code
	 * @param loggerBuffer
	 * @param fileN
	 * @return DataBaseAction
	 * @throws：
	 */
	public static DataBaseAction processSuccessReport(ParseResult<LightingData> parseResult,Date recv_time, String cts_code,StringBuffer loggerBuffer ,String fileN) {
		java.sql.Connection connection = null;
		
		try {
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			//List<ReportInfo> reportInfos = parseResult.getReports();
			//reportInfoToDb(reportInfos, report_connection, recv_time);  
			List<LightingData> lightingDatas = parseResult.getData();
			insert_db(lightingDatas, connection, recv_time, loggerBuffer,fileN,cts_code);
			return DataBaseAction.SUCCESS;
		} catch (Exception e) {
			loggerBuffer.append("\n Database connection error");
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
					loggerBuffer.append("\n Database connection close error"+e.getMessage());
				}
			}
			
		}
	}
	
	/**
	 * @param cts_code 
	 * @param loggerBuffer 
	 * @param cts_code 
	 * 
	 * @Title: insert_db   
	 * @Description:(ADTD系统雷电定位数据入库)   
	 * @param: @param list 数据结合
	 * @param: @param recv_time  消息接收时间
	 * @param: @param is_Batch  是否Batch提交
	 * @param: @return      
	 * @return: DataBaseAction  返回处理状态    
	 * @throws
	 */
	@SuppressWarnings("deprecation")
	private static void insert_db(List<LightingData> list, Connection connection, Date recv_time, StringBuffer loggerBuffer,String fileN, String cts_code) {
		PreparedStatement pStatement = null;
//		String type = "B.0016.0003.S001";
		try {
			//connection = JdbcUtilsSing.getInstance("config/z_p_lpd/z_p_lpd_db_config.xml").getConnection();
			String sql = " INSERT INTO "+StartConfig.valueTable()+"("
					+ "D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_UPDATE_TIME,D_RYMDHM,D_DATETIME,V05001,V06001,V04001,V04002,V04003,V04004,V04005,V04006, "
					+ "V04007,V08300,V73016,V73023,V73011,V73110,V01015_1,V01015_2,V01015_3,V_BBB,D_SOURCE_ID)   "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			pStatement = new LoggableStatement(connection, sql); 
			connection.setAutoCommit(false);
			if(StartConfig.getDatabaseType() == 1) {
				pStatement.execute("select last_txc_xid()");
			}
			listDi = new ArrayList<StatDi>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			for (int i = 0; i < list.size(); i++) {
				StatDi di = new StatDi();					
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(type);
				di.setDATA_TYPE_1(cts_code);
				di.setTT("ADTD");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //1成功，0失败
				di.setPROCESS_STATE("1");  //1成功，0失败	
				
				int ii = 1;
				/*
				 * 记录标识 资料标识 入库时间 收到时间 资料时间
				 */
				int strYear = list.get(i).getObservationTime().getYear() + 1900;
				int strMonth = list.get(i).getObservationTime().getMonth() + 1;
				int strDate = list.get(i).getObservationTime().getDate();
				String primkey = sdf.format(list.get(i).getObservationTime()) + list.get(i).getMillis() + "_";
				if(list.get(i).getLatitude() > 0) {
					primkey = primkey + "0"+(int)(list.get(i).getLatitude()*1000000) + "_";
				}else {
					primkey = primkey + "1"+(int)(list.get(i).getLatitude()*1000000) + "_";
				}
				
				if(list.get(i).getLongitude() > 0) {
					primkey = primkey + "0"+(int)(list.get(i).getLongitude()*1000000) + "_";
				}else {
					primkey = primkey + "1"+(int)(list.get(i).getLongitude()*1000000) + "_";
				}
				
				primkey = primkey + list.get(i).getLocateMode();
				pStatement.setString(ii++, primkey);
				pStatement.setString(ii++, type);
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(list.get(i).getObservationTime().getTime()));// 一会改ee

				/*
				 * 闪电位置纬度 闪电位置经度
				 */
				pStatement.setBigDecimal(ii++, new BigDecimal(list.get(i).getLatitude()).setScale(6, BigDecimal.ROUND_HALF_UP));
				pStatement.setBigDecimal(ii++, new BigDecimal(list.get(i).getLongitude()).setScale(6, BigDecimal.ROUND_HALF_UP));
				/*
				 * 资料观测年 资料观测月 资料观测日 资料观测时 资料观测分 资料观测秒 资料观测毫秒
				 */

				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(strYear)));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(strMonth)));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(strDate)));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getObservationTime().getHours())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getObservationTime().getMinutes())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getObservationTime().getSeconds())));
//				pStatement.setBigDecimal(ii++, new BigDecimal(NumberUtil.addZeroForNum(list.get(i).getMillis(), 7)));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getMillis())));

				/*
				 * 雷电序号 回击峰值强度 回击最大陡度 定位误差 定位方式
				 */
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getLdpId())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getIntensity())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getSteepness())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getErrorValue())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getLocateMode())));
				

				/*
				 * 雷电地理位置信息省 雷电地理位置信息市 雷电地理位置信息县
				 */

				pStatement.setString(ii++, list.get(i).getProvince());
				pStatement.setString(ii++, list.get(i).getCity());
				pStatement.setString(ii++, list.get(i).getCounty());
				pStatement.setString(ii++, "000");
				pStatement.setString(ii++, cts_code);
				
				di.setIIiii(primkey); //没有台站，怎么办？
				di.setDATA_TIME(TimeUtil.date2String(list.get(i).getObservationTime(), "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());
				di.setLATITUDE(list.get(i).getLatitude().toString());
				di.setLONGTITUDE(list.get(i).getLongitude().toString());
				
				di.setSEND("BFDB");
				di.setSEND_PHYS("DRDS");
				di.setFILE_SIZE(String.valueOf(new File(fileN).length()));
				di.setDATA_UPDATE_FLAG("000");
				di.setHEIGHT("999999");
				
				loggerBuffer.append(" " + list.get(i).getLongitude()+" " +list.get(i).getLatitude()+ " " + sdf.format(list.get(i).getObservationTime()) +" 000\n");
				pStatement.addBatch(); 
			    listDi.add(di);
				// 是否批量提交
				
				
			} // end for
			try {
				pStatement.executeBatch();
				connection.commit();						
				loggerBuffer.append("\n Batch submission successful："+fileN);
				
			} catch (Exception e) {
				pStatement.clearParameters();
				pStatement.clearBatch();
				connection.rollback();
				execute_sql(list, connection, type, connection, loggerBuffer,recv_time);
				loggerBuffer.append("\n Batch submission error"+ fileN);

			}
			

		} catch (SQLException e) {
			loggerBuffer.append("\n Database connection exception: "+e.getMessage());
		
		}finally {
			for (int j = 0; j < listDi.size(); j++) {
				diQueues.offer(listDi.get(j));
			}
			listDi.clear();
			try {
				if (pStatement != null)
					pStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 
	 * @Title: execute_sql
	 * @Description:(ADTD系统雷电定位数据单条入库)
	 * @param list
	 * @param connection
	 * @param type
	 * @param connection
	 * @param loggerBuffer
	 * @param recv_time void
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static void execute_sql(List<LightingData> list, java.sql.Connection connection, String type, Connection connection2 , StringBuffer loggerBuffer,Date recv_time) {
		PreparedStatement pStatement = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String sql = " INSERT INTO "+StartConfig.valueTable()+"("
				+ "D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_UPDATE_TIME,D_RYMDHM,D_DATETIME,V05001,V06001,V04001,V04002,V04003,V04004,V04005,V04006, "
				+ "V04007,V08300,V73016,V73023,V73011,V73110,V01015_1,V01015_2,V01015_3,V_BBB,D_SOURCE_ID)   "
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			connection.setAutoCommit(true);
			pStatement = new LoggableStatement(connection, sql);
			for (int i = 0; i < list.size(); i++) {
				int ii = 1;
				/*
				 * 记录标识 资料标识 入库时间 收到时间 资料时间
				 */
				int strYear = list.get(i).getObservationTime().getYear() + 1900;
				int strMonth = list.get(i).getObservationTime().getMonth() + 1;
				int strDate = list.get(i).getObservationTime().getDate();
				String primkey = sdf.format(list.get(i).getObservationTime()) + list.get(i).getMillis() + "_";
				if(list.get(i).getLatitude() > 0) {
					primkey = primkey + "0"+(int)(list.get(i).getLatitude()*1000000) + "_";
				}else {
					primkey = primkey + "1"+(int)Math.abs((list.get(i).getLatitude()*1000000)) + "_";
				}
				
				if(list.get(i).getLongitude() > 0) {
					primkey = primkey + "0"+(int)(list.get(i).getLongitude()*1000000) + "_";
				}else {
					primkey = primkey + "1"+(int)Math.abs((list.get(i).getLongitude()*1000000)) + "_";
				}
				
				primkey = primkey + list.get(i).getLocateMode();
				pStatement.setString(ii++, primkey);
				pStatement.setString(ii++, type);
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
				pStatement.setTimestamp(ii++, new Timestamp(list.get(i).getObservationTime().getTime()));// 一会改ee

				/*
				 * 闪电位置纬度 闪电位置经度
				 */
				pStatement.setBigDecimal(ii++, new BigDecimal(list.get(i).getLatitude()).setScale(6, BigDecimal.ROUND_HALF_UP));
				pStatement.setBigDecimal(ii++, new BigDecimal(list.get(i).getLongitude()).setScale(6, BigDecimal.ROUND_HALF_UP));
				/*
				 * 资料观测年 资料观测月 资料观测日 资料观测时 资料观测分 资料观测秒 资料观测毫秒
				 */

				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(strYear)));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(strMonth)));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(strDate)));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getObservationTime().getHours())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getObservationTime().getMinutes())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getObservationTime().getSeconds())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getMillis())));

				/*
				 * 雷电序号 回击峰值强度 回击最大陡度 定位误差 定位方式
				 */
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getLdpId())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getIntensity())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getSteepness())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getErrorValue())));
				pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(list.get(i).getLocateMode())));

				/*
				 * 雷电地理位置信息省 雷电地理位置信息市 雷电地理位置信息县
				 */

				pStatement.setString(ii++, list.get(i).getProvince());
				pStatement.setString(ii++, list.get(i).getCity());
				pStatement.setString(ii++, list.get(i).getCounty());
				pStatement.setString(ii++, "000");
				pStatement.setString(ii++, StartConfig.ctsCode());
				try {
					pStatement.execute();
				} catch (Exception e) {
					loggerBuffer.append("\n "+ list.get(i).getLdpId() + " " + sdf.format(list.get(i).getObservationTime()) + 
							"\n sql error" + ((LoggableStatement)pStatement).getQueryString()+
							"\n " + e.getMessage());
					
					listDi.get(i).setPROCESS_STATE("0");
				}
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n create Statement error");
		} finally {
			if(pStatement != null) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n close Statement error");
				}
			}
		}
		
	}

}
