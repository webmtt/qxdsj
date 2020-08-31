package cma.cimiss2.dpc.indb.sevp.dc_sevp_world_live.service;

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

import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.sevp.ForeignLive;
import cma.cimiss2.dpc.decoder.sevp.DecodeForeignLive;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class DbService {
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();	
	
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}

	public static void main(String[] args) {
		DecodeForeignLive forecast=new DecodeForeignLive();
		String filepath = "D:\\HUAXIN\\DataProcess\\M.0049.0001.R001\\M.0049.0001.R001Dst\\2018061219\\Z_SEVP_C_BABJ_20180612192000_P_GSL-2018061219.TXT";
		File file = new File(filepath);
		ParseResult<ForeignLive> parseResult = forecast.DecodeFile(file);
		DataBaseAction action = null;
		Date recv_time = new Date();
		action = DbService.processSuccessReport(parseResult, filepath, recv_time);
		System.out.println("insertDBService over!");
	}
	
	/**
	 * 
	 * @Title: processSuccessReport   
	 * @Description:(报文解码入库函数)   
	 * @param  parseResult  存放解码结果
	 * @param filepath 文件路径
	 * @param  recv_time  报文接收时间   
	 * @return: DataBaseAction      
	 * @throws:
	 */
	public static DataBaseAction processSuccessReport(ParseResult<ForeignLive> parseResult, String fileN, Date recv_time) {
		java.sql.Connection connection = null;
		int batchSize = 500;
//		java.sql.Connection reportConnection = null;
		try {
			// 获取数据库连接
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			// 获取解码结果集
			List<ForeignLive> forecasts = parseResult.getData();
			
			for(int  n = 0; n < forecasts.size(); n += batchSize){
				List<ForeignLive> fclist = null;
				if(forecasts.size() - n >= batchSize) {
					fclist = forecasts.subList(n, n + batchSize);
				}else {
					fclist = forecasts.subList(n, forecasts.size());
				}
				insertDB(fclist, connection, recv_time,fileN);
			}
			
			return DataBaseAction.SUCCESS;
		} catch (Exception e) {
			infoLogger.error("\n Database connection error!");
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
					infoLogger.error("\n Close Database connection error: "+e.getMessage());
				}
			}
			
//			if(reportConnection != null) {
//				try {
//					reportConnection.close();
//				} catch (SQLException e) {
//					logger.error("\n 数据库connection关闭异常"+e.getMessage());
//				}
//			}
		}
	}
	/**
	 * 
	 * @Title: insertDB
	 * @Description:(国外城市气象站实况资料入库)   
	 * @param forecasts 待入库对象集合
	 * @param connection 数据库连接
	 * @param recv_time 资料接收接收
	 * @param fileN 
	 * @return void 
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static void insertDB(List<ForeignLive> lives, java.sql.Connection connection, Date recv_time, String fileN){
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,D_UPDATE_TIME,"
			+ "V_BBB,V01301,V01300,V04001,V04002,V04003,V04004,V05001,V06001,V07001,"
			+ "V12001,V13003,V12003,V11001,V11002,V10004,V20312,V20001,V20010,V12510,D_SOURCE_ID) "
			+ "VALUES (?,?, ?, ?, ?, ?,"
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " ;
		if(connection != null){		
			try {	
				pStatement = new LoggableStatement(connection, sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				connection.setAutoCommit(false);
				List<String> sqls = new ArrayList<>();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");		
				String V_TT = "ACCL";
				for(int idx = 0; idx < lives.size(); idx ++){
					ForeignLive live = lives.get(idx);
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
					
					Date dataTime = live.getObservationTime();
					String station = live.getStationNumberChina();
					int ii = 1;
					pStatement.setString(ii++, sdf.format(dataTime)+"_"+station);
					pStatement.setString(ii++, sod_code);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(dataTime.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					//"V_BBB,V01301,V01300,V04001,V04002,V04003,V04004,V05001,V06001,V07001,"
					pStatement.setString(ii++, live.getCorrectSign());
					pStatement.setString(ii++, live.getStationNumberChina());
					pStatement.setInt(ii++, 999999);
					pStatement.setInt(ii++, dataTime.getYear() + 1900);
					pStatement.setInt(ii++, dataTime.getMonth() + 1);
					pStatement.setInt(ii++, dataTime.getDate());
					pStatement.setInt(ii++, dataTime.getHours());
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(live.getLatitude())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(live.getLongtitude())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(live.getHeight())));
					//V12001,V13003,V12003,V11001,V11002,V10004,V20312,V20001,V20010,V12510)
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(live.getTemperature())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(live.getHumidity())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(live.getDewpoint())));
					pStatement.setString(ii++, live.getWindDir());
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf( live.getWindSpeed())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(live.getAirPressure())));
					pStatement.setInt(ii++, live.getWeatherPheno());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(live.getVisibility())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(live.getCloudCoverageRate())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(live.getSurfaceTemperature())));
					pStatement.setString(ii++, cts_code);
					
					di.setIIiii(station);
					di.setDATA_TIME(TimeUtil.date2String(dataTime, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(live.getLatitude()));
					di.setLONGTITUDE(String.valueOf(live.getLongtitude()));
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(fileN).length()));
					di.setDATA_UPDATE_FLAG(live.getCorrectSign());
					di.setHEIGHT(String.valueOf(live.getHeight()));
					
					pStatement.addBatch();
					sqls.add(((LoggableStatement)pStatement).getQueryString()); // 批量入库冲突时，单条入库
					listDi.add(di);
				} 
				try{
					pStatement.executeBatch();
					connection.commit();
					sqls.clear();
				}catch(SQLException e){
					infoLogger.error("\n Batch commit failed: "+fileN);
					pStatement.clearParameters();
					pStatement.clearBatch();
					execute_sql(sqls, connection,fileN); // 此中，入库失败的会将对应的DI的ProcessState置为1 // 1:失败，0：成功
				}
			}catch (SQLException e) {
				infoLogger.error("\n Create Statement error: "+e.getMessage());
			}
			finally {
				if(pStatement != null) {
					try {
						pStatement.close();
					} catch (SQLException e) {
						infoLogger.error("\n Close Statement error: "+e.getMessage());
					}
				}
			}
		} 
	}
	
	/**
	 * 
	 * @Title: execute_sql   
	 * @Description:(批量入库失败时，采用逐条提交)   
	 * @param  sqls 待执行的查询语句
	 * @param  connection      数据库连接
	 * @param fileN 
	 * @return: void      
	 * @throws:
	 */
	private static void execute_sql(List<String> sqls, java.sql.Connection connection, String fileN) {
		Statement pStatement = null;
		try {
			connection.setAutoCommit(true);
			pStatement = connection.createStatement();
			for (int i = 0; i < sqls.size(); i++) {
//				pStatement = new LoggableStatement(connection, sqls.get(i));
				try {
					pStatement.execute(sqls.get(i));
				} catch (Exception e) {
					infoLogger.error("\n File name: "+fileN
							+"\n " + listDi.get(i).getIIiii() + " " + listDi.get(i).getDATA_TIME()
							+"\n execute sql error: "+sqls.get(i)+"\n "+e.getMessage());
					listDi.get(i).setPROCESS_STATE("0");
				}
			}
		} catch (SQLException e) {
			infoLogger.error("\n Create Statement error: "+e.getMessage());
		}finally {
			if(pStatement != null) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					infoLogger.error("\n Close Statement error: "+e.getMessage());
				}
			}
		}		
		
	}
}
