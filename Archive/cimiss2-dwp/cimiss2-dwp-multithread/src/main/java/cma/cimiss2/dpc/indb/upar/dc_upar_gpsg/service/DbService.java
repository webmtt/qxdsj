package cma.cimiss2.dpc.indb.upar.dc_upar_gpsg.service;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Pattern;
import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.upar.GPS_MET;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.upar.DecodeGPS_MET;

// TODO: Auto-generated Javadoc
/**
 * The Class DbService.
 */
public class DbService {
	
	/** The Constant messageLogger. */
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	
	/** The Constant infoLogger. */
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	
	/** The list di. */
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	
	/** The di queues. */
	public static BlockingQueue<StatDi> diQueues;
	
	/** The cts code. */
	public static String cts_code = StartConfig.ctsCode();
	
	/** The sod code. */
	public static String sod_code = StartConfig.sodCode();
	
	/** The v tt. */
	public static String V_TT = "GPS/MET水汽数据产品";
	
	/** The default int. */
	public static int defaultInt = 999999;
	
	
	/**
	 * Gets the di queues.
	 *
	 * @return the di queues
	 */
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	/**
	 * Sets the di queues.
	 *
	 * @param diQueues the new di queues
	 */
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}

	/**
	 * Process success report.
	 *
	 * @param parseResult the parse result
	 * @param recv_time 解码文件路径
	 * @param fileN the file N
	 * @param loggerBuffer the logger buffer
	 * @return the data base action
	 * @Title: processSuccessReport
	 * @Description:(GPS\MET水汽资料处理) 
	 * @return: DataBaseAction
	 * @throws: 
	 */
	public static DataBaseAction processSuccessReport(ParseResult<GPS_MET> parseResult, Date recv_time, String fileN, StringBuffer loggerBuffer, String filepath) {
		java.sql.Connection connection = null;
		try {
			// 获取数据库连接
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			
			List<GPS_MET> gps_METs = parseResult.getData();
			insertDB(gps_METs, connection,recv_time, fileN,loggerBuffer, filepath);
			
			return DataBaseAction.SUCCESS;
		} catch (Exception e) {
			loggerBuffer.append("\n database connection error");
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
					loggerBuffer.append("\n database connection  close error"+e.getMessage());
				}
			}
		}
		
	}

	/**
	 * Insert DB.
	 *
	 * @param gps_METs 入库对象集合
	 * @param connection 数据库连接
	 * @param recv_time the recv time
	 * @param fileN the file N
	 * @param loggerBuffer the logger buffer
	 * @Title: insertDB
	 * @Description:(gps\met水汽数据资料入库) 
	 * @return: void
	 * @throws: 
	 */
	@SuppressWarnings({ "deprecation" })
	private static void insertDB(List<GPS_MET> gps_METs, java.sql.Connection connection, Date recv_time, String fileN, StringBuffer loggerBuffer, String filepath) {
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,D_UPDATE_TIME,"
				+ "v01301,v01300,V_STATIONCODE,v05001,v06001,v07001,"
				+ "v04001,v04002,v04003,v04004,v04005,"
				+ "V07350,V10004,V12001,V13003,V13016,V_BBB,D_SOURCE_ID) "
				+ "VALUES (?, ?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?,?,?) " ;
		if(connection != null){		
			try {	
				pStatement = new LoggableStatement(connection, sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				List<String> sqls = new ArrayList<>();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");		
				GPS_MET gps_MET = new GPS_MET();
				for(int idx = 0; idx < gps_METs.size(); idx ++){
					gps_MET = gps_METs.get(idx);
					StatDi di = new StatDi();	
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(sod_code);
					di.setDATA_TYPE_1(cts_code);
					di.setTT(V_TT);			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1");  //1成功，0失败	
					
					int stationNumberN = 999999;
					String stat = gps_MET.getStationNumberChina();
					if (Pattern.matches("\\d{5}", stat))
						stationNumberN = Integer.parseInt(stat);					
					else
						stationNumberN = Integer.parseInt(StationCodeUtil.stringToAscii(stat));
					String lat = String.valueOf((int)(gps_MET.getLatitude() * 1e6));
					String lon = String.valueOf((int)(gps_MET.getLongtitude() * 1e6));
					lat = lat.replaceAll("-", "0");
					lon = lon.replaceAll("-", "0");
					int ii = 1;
					pStatement.setString(ii++, sdf.format(gps_MET.getObservationTime())+"_"+gps_MET.getStationNumberChina()+"_"+lat+"_"+lon);
					pStatement.setString(ii++, sod_code);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(gps_MET.getObservationTime().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					
					pStatement.setString(ii++, stat);
					pStatement.setInt(ii++, stationNumberN);
					pStatement.setString(ii++, gps_MET.getStationCode());
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(gps_MET.getLatitude()))); // 纬度
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(gps_MET.getLongtitude()))); // 经度
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( gps_MET.getHeightOfSationGroundAboveMeanSeaLevel()))); // 测站高度
					
					Date date = new Date(gps_MET.getObservationTime().getTime());  //资料时间
					pStatement.setInt(ii++, date.getYear() + 1900);
					pStatement.setInt(ii++, date.getMonth() + 1);
					pStatement.setInt(ii++, date.getDate());
					pStatement.setInt(ii++, date.getHours());
					pStatement.setInt(ii++, date.getMinutes());

					//总的天顶延迟|气压|气温|相对湿度|可降水量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(gps_MET.getTotalZenithDelay())));  //总的
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(gps_MET.getPressure())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf( gps_MET.getAirTemperature())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(gps_MET.getRelativeHumidity())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(gps_MET.getPrecipitationAmount())));
					pStatement.setString(ii++, "000");
					pStatement.setString(ii++, cts_code);
					
					di.setIIiii(gps_MET.getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(gps_MET.getObservationTime(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(gps_MET.getLatitude()));
					di.setLONGTITUDE(String.valueOf(gps_MET.getLongtitude()));
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
					di.setDATA_UPDATE_FLAG("000");
					di.setHEIGHT("999999");
					
					pStatement.addBatch();
					sqls.add(((LoggableStatement)pStatement).getQueryString()); // 批量入库冲突时，单条入库
					listDi.add(di);
				} // end for
				try{
					pStatement.executeBatch();
					connection.commit();
					loggerBuffer.append(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
					loggerBuffer.append(" INSERT SUCCESS COUNT ：" + gps_METs.size() + "\n");
					
					sqls.clear();
				}catch(SQLException e){
					pStatement.clearParameters();
					pStatement.clearBatch();
					connection.rollback();
					execute_sql(sqls, connection,fileN,loggerBuffer); // 此中，入库失败的会将对应的DI的ProcessState置为1 // 1:失败，0：成功
					loggerBuffer.append(" Batch ERROR : " + sdf.format(new Date()) + "\n");
				}
			}catch (SQLException e) {
				loggerBuffer.append("\n create Statement error"+e.getMessage());
			}finally {
				if(pStatement != null) {
					try {
						pStatement.close();
					} catch (SQLException e) {
						loggerBuffer.append("\n close Statement error"+e.getMessage());
					}
				}
			}
		} 
	}
	
	/**
	 * Execute sql.
	 *
	 * @param sqls the sqls
	 * @param connection the connection
	 * @param fileN the file N
	 * @param loggerBuffer the logger buffer
	 * @Title: execute_sql
	 * @Description: TODO(批量入库失败时，采用逐条提交)
	 * @return: void
	 * @throws: 
	 */
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
					loggerBuffer.append("\n filename："+fileN
							+"\n " + listDi.get(i).getIIiii() + " " + listDi.get(i).getDATA_TIME()
							+"\n execute sql error："+sqls.get(i)+"\n "+e.getMessage());
					listDi.get(i).setPROCESS_STATE("0");
					continue;
				}
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n create Statement error"+e.getMessage());
		}finally {
			if(pStatement != null) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n close Statement error"+e.getMessage());
				}
			}
		}		
		
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		File file = new File("D:\\TEMP\\GPSMET\\Z_UPAR_C_BEHT_20190320023000_P_GPSG_vapor.txt");
		String fileN = file.getName();
		Date recv_time = new Date(file.lastModified());
		StringBuffer loggerBuffer = new StringBuffer();
		DecodeGPS_MET decodeGPS_MET = new DecodeGPS_MET();
		ParseResult<GPS_MET> parseResult = decodeGPS_MET.DecodeFile(file);	
		if(parseResult.isSuccess()){
			DataBaseAction	action = DbService.processSuccessReport(parseResult, recv_time,fileN,loggerBuffer, file.getPath());	
		}
		
	}
}
