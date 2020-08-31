package cma.cimiss2.dpc.indb.cawn.dc_cawn_regqc.service;

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
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.cawn.REGQuality;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class DbService {
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
//	static Map<String, Object> proMap = StationInfo.getProMap();
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}
	/**
	 * 
	 * @Title: processSuccessReport   
	 * @Description: TODO(报文解码入库函数)   
	 * @param  parseResult  存放解码结果
	 * @param filepath 文件路径
	 * @param  recv_time  报文接收时间   
	 * @return: DataBaseAction      
	 * @throws:
	 */
	public static DataBaseAction processSuccessReport(ParseResult<REGQuality> parseResult, String fileN, Date recv_time,StringBuffer loggerBuffer, String filepath) {
		java.sql.Connection connection = null;
		java.sql.Connection reportConnection = null;
		try {
			// 获取数据库连接
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			// 获取解码结果集
			List<REGQuality> regs = parseResult.getData();
			insertDB(regs, connection, recv_time, loggerBuffer, fileN, filepath);
			return DataBaseAction.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
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
					loggerBuffer.append("\n Close datbase connection error: "+e.getMessage());
				}
			}
			if(reportConnection != null) {
				try {
					reportConnection.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n Close datbase connection error: "+e.getMessage());
				}
			}
		}
	}
	/**
	 * 
	 * @Title: insertDB
	 * @Description: TODO(大气成分反应性气体观测资料入库)   
	 * @param regs 待入库对象集合
	 * @param connection 数据库连接
	 * @param recv_time 资料接收接收
	 * @param fileN 
	 * @return void 
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static void insertDB(List<REGQuality> regQualities, java.sql.Connection connection, Date recv_time, StringBuffer loggerBuffer, String fileN, String filepath){
		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,D_UPDATE_TIME,"
			+ "V01301,V01300,V05001,V06001,V07001,V_ACODE,V04001,V04002,V04003,V04004,"
			+ "V_OBS_ITEM,V_QC_BEG,V_QC_END,V_RUN_STATE,V_MAINTEN,V_ENVIRON,V_WEATHER_PHEN,V_WATCHER,V_NOTES,V_BBB, D_SOURCE_ID) "
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
				String V_TT = "REGL";
				for(int idx = 0; idx < regQualities.size(); idx ++){
					REGQuality regQuality = regQualities.get(idx);
					double latitude = 999999;
					double longtitude = 999999;
					double height = 999999;
					int adminCode = 999999;
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
						
					Date dataTime = regQuality.getObservationTime();
					String station = regQuality.getStationNumberChina();
					int ii = 1;
					pStatement.setString(ii++, sdf.format(dataTime)+"_"+station + "_" + regQuality.getObserveItem()); 
					pStatement.setString(ii++, sod_code);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(dataTime.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				
					pStatement.setString(ii++, station);
					pStatement.setInt(ii++, Integer.parseInt(StationCodeUtil.stringToAscii(station)));
					String info = (String) proMap.get(station + "+16");
					if(info == null) {
						loggerBuffer.append("\n In configuration file, this station does not exist: " + station);
					}
					else{
						String[] infos = info.split(",");						
						if(infos[1].equals("null"))
							loggerBuffer.append("\n In configuration file, longtitude is null!");
						else
							longtitude = Double.parseDouble(infos[1]);
						if(infos[2].equals("null"))
							loggerBuffer.append("\n In configuration file, latitude is null!");
						else
							latitude = Double.parseDouble(infos[2]);
						if(infos[3].equals("null"))
							loggerBuffer.append("\n In configuration file, station height is null!");
						else
							height = Double.parseDouble(infos[3]);
						if(infos[5].equals("null"))
							loggerBuffer.append("\n In configuration file, V_CCCC is null!");
						else 
							adminCode = Integer.parseInt(infos[5]);
					}
					pStatement.setBigDecimal(ii++, new BigDecimal(latitude).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(longtitude).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(height)));
					pStatement.setInt(ii++, adminCode);
					pStatement.setInt(ii++, dataTime.getYear() + 1900);
					pStatement.setInt(ii++, dataTime.getMonth() + 1);
					pStatement.setInt(ii++, dataTime.getDate());
					pStatement.setInt(ii++, dataTime.getHours());
					
					pStatement.setString(ii++, regQuality.getObserveItem());
					String qcInfoStartTime=regQuality.getQcInfoStartTime();
					qcInfoStartTime=qcInfoStartTime.replaceAll(" ", "");
					String qcInfoEndTime=regQuality.getQcInfoEndTime();
					qcInfoEndTime=qcInfoEndTime.replaceAll(" ", "");
					pStatement.setBigDecimal(ii++, new BigDecimal(qcInfoStartTime));
					pStatement.setBigDecimal(ii++, new BigDecimal(qcInfoEndTime));
					pStatement.setString(ii++, regQuality.getRunningState());
					pStatement.setString(ii++, regQuality.getMaintenance());
					pStatement.setString(ii++, regQuality.getPeripheralCondition());
					pStatement.setString(ii++, regQuality.getWeatherPhone());
					pStatement.setString(ii++, regQuality.getAttendantName());
					pStatement.setString(ii++, regQuality.getTextNote());
					pStatement.setString(ii++, "000");
					pStatement.setString(ii++, cts_code);
					
					di.setIIiii(station);
					di.setDATA_TIME(TimeUtil.date2String(dataTime, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(latitude));
					di.setLONGTITUDE(String.valueOf(longtitude));
					

					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
					di.setDATA_UPDATE_FLAG("000");
					di.setHEIGHT(String.valueOf(height));
					
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
					execute_sql(sqls, connection, loggerBuffer, fileN); // 此中，入库失败的会将对应的DI的ProcessState置为1 // 1:失败，0：成功
					loggerBuffer.append("\n Batch commit error: " + fileN);
				}
			}catch (SQLException e) {
				loggerBuffer.append("\n Create Statement error: "+e.getMessage());
			}catch (Exception e1) {
				loggerBuffer.append("\n insertDB find error: "+e1.getMessage()+fileN);
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
	 * @Description: TODO(批量入库失败时，采用逐条提交)   
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
