package cma.cimiss2.dpc.indb.sevp.dc_sevp_nega.service;

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
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.sevp.Travel;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class DbService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	Map<String, Object> proMap = StationInfo.getProMap();
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
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
	public static DataBaseAction processSuccessReport(ParseResult<Travel> parseResult, String fileN, Date recv_time,StringBuffer loggerBuffer) {
		java.sql.Connection connection = null;
		java.sql.Connection reportConnection = null;
		try {
			// 获取数据库连接
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			// 获取解码结果集
			List<Travel> travels = parseResult.getData();
			insertDB(travels, connection, recv_time, loggerBuffer,fileN);
			return DataBaseAction.SUCCESS;
		} catch (Exception e) {
			loggerBuffer.append("\n Database connection error!");
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
					loggerBuffer.append("\n Close database connection error: "+e.getMessage());
				}
			}
			if(reportConnection != null) {
				try {
					reportConnection.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n Close database connection error: "+e.getMessage());
				}
			}
		}
	}
	/**
	 * 
	 * @Title: insertDB
	 * @Description:(旅游景区气象服务产品入库)   
	 * @param travels 待入库对象集合
	 * @param connection 数据库连接
	 * @param recv_time 资料接收接收
	 * @param fileN 
	 * @return void 
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static void insertDB(List<Travel> travels, java.sql.Connection connection, Date recv_time, StringBuffer loggerBuffer, String fileN){
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_RECORD_ID,D_DATA_ID,D_SVCO,D_IYMDHM,D_RYMDHM,D_DATETIME,D_UPDATE_TIME,"
			+ "V04001,V04002,V04003,V04004,V04005,V_PUNA,V_PUPROCO,V_PUCO,V_SPNA,V_SPCO,"
			+ "V_VA,V_ISTS,V_ITEN,V_IVNA,V05001,V06001,V_IVSN,V_IVSLAT,V_IVSLON,V_PD,"
			+ "V_ET,V_PS,V_WA,V_PUER,V_BBB,D_SOURCE_ID) "
			+ "VALUES (?,?, ?, ?, ?, ?, ?,"
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?) " ;
		if(connection != null){		
			try {	
				pStatement = new LoggableStatement(connection, sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				connection.setAutoCommit(false);
				List<String> sqls = new ArrayList<>();
				String V_TT = "TRAVEL";
				for(int idx = 0; idx < travels.size(); idx ++){
					Travel travel = travels.get(idx);
					// 非更正报；或者为更正报，但是数据库中尚未有这一条数据
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
						
					Date dataTime = travel.getReleaseTime();
					String prodID = travel.getProductID();
					int ii = 1;
					pStatement.setString(ii++, prodID); //由产品发布时间（PT）+影响景区纬度（IVLAT）+影响景区经度（IVLON）+产品信息编码（VA）+景区预警信号发布与撤销标识（WA）组成
					pStatement.setString(ii++, sod_code);
					pStatement.setString(ii++, prodID);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					
//					Date datatime1=new Date();
//					datatime1.setTime(dataTime.getTime());
//					datatime1.setMinutes(0);
//					datatime1.setSeconds(0);
//					pStatement.setTimestamp(ii++, new Timestamp(datatime1.getTime()));//D_DATETIME
					pStatement.setTimestamp(ii++, new Timestamp(dataTime.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					//"V04001,V04002,V04003,V04004,V04005,V_PUNA,V_PUPROCO,V_PUCO,V_SPNA,V_SPCO,"
					pStatement.setInt(ii++, dataTime.getYear() + 1900);
					pStatement.setInt(ii++, dataTime.getMonth() + 1);
					pStatement.setInt(ii++, dataTime.getDate());
					pStatement.setInt(ii++, dataTime.getHours());
					pStatement.setInt(ii++, dataTime.getMinutes());
					pStatement.setString(ii++, travel.getReleaseOrgName());
					pStatement.setString(ii++, travel.getProvCode());
					pStatement.setString(ii++, travel.getReleaseOrgCode());
					pStatement.setString(ii++, travel.getSceneryName());
					pStatement.setString(ii++, travel.getSceneryCode());
					//"V_VA,V_ISTS,V_ITEN,V_IVNA,V05001,V06001,V_IVSN,V_IVSLAT,V_IVSLON,V_PD,"
					pStatement.setInt(ii++, travel.getSceneryProductInfo());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(travel.getAffectedStartTime())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(travel.getAffectedEndTime())));
					pStatement.setString(ii++, travel.getScenicSpotName());
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(travel.getScenicSpotLat())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(travel.getScenicSpotLon())));
					pStatement.setString(ii++, travel.getAffectedScenicSpotName());
					pStatement.setString(ii++, travel.getAffectedScenicSportLat());
					pStatement.setString(ii++, travel.getAffectedScenicSportLon());
					pStatement.setString(ii++, travel.getDisaOrProdContent());
					//"V_ET,V_PS,V_WA,V_PUER,V_BBB) "
					pStatement.setInt(ii++, travel.getEvolutionTrend());
					pStatement.setString(ii++, travel.getSuggestions());
					pStatement.setInt(ii++, travel.getSignalReleaseAndCancelCode());
					pStatement.setString(ii++, travel.getPublisherName());
					pStatement.setString(ii++, "000");
					pStatement.setString(ii++, cts_code);
					
					di.setIIiii(prodID);
					di.setDATA_TIME(TimeUtil.date2String(dataTime, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(travel.getAffectedScenicSportLat()));
					di.setLONGTITUDE(String.valueOf(travel.getAffectedScenicSportLon()));
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(fileN).length()));
					di.setDATA_UPDATE_FLAG("000");
					di.setHEIGHT("999999");
					
					pStatement.addBatch();
					sqls.add(((LoggableStatement)pStatement).getQueryString()); // 批量入库冲突时，单条入库
					listDi.add(di);
				} 
				try{
					pStatement.executeBatch();
					connection.commit();
					sqls.clear();
				}catch(SQLException e){
					pStatement.clearParameters();
					pStatement.clearBatch();
					execute_sql(sqls, connection, loggerBuffer,fileN); // 此中，入库失败的会将对应的DI的ProcessState置为1 // 1:失败，0：成功
					loggerBuffer.append("\n Batch commit failed: "+fileN);
				}
			}catch (SQLException e) {
				loggerBuffer.append("\n Create Statement error: "+e.getMessage());
			}
			finally {
				if(pStatement != null) {
					try {
						pStatement.close();
					} catch (SQLException e) {
						loggerBuffer.append("\n Close Statement error: "+e.getMessage());
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
	private static void execute_sql(List<String> sqls, java.sql.Connection connection, StringBuffer loggerBuffer, String fileN) {
		Statement pStatement = null;
		try {
			connection.setAutoCommit(true);
			pStatement = connection.createStatement();
			for (int i = 0; i < sqls.size(); i++) {
//				pStatement = new LoggableStatement(connection, sqls.get(i));
				try {
					pStatement.execute(sqls.get(i));
				} catch (Exception e) {
					loggerBuffer.append("\n File name: "+fileN
							+"\n " + listDi.get(i).getIIiii() + " " + listDi.get(i).getDATA_TIME()
							+"\n execute sql error: "+sqls.get(i)+"\n "+e.getMessage());
					listDi.get(i).setPROCESS_STATE("0");
				}
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n Create Statement error: "+e.getMessage());
		}finally {
			if(pStatement != null) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n Close Statement error: "+e.getMessage());
				}
			}
		}		
		
	}
}
