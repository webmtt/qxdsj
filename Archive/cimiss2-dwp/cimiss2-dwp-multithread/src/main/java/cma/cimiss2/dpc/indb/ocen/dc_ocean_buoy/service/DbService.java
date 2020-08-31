package cma.cimiss2.dpc.indb.ocen.dc_ocean_buoy.service;

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
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ocean.Buoy;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

/**
 * -------------------------------------------------------------------------------
 * <br>
 * @Title:  DbService.java   
 * @Package cma.cimiss2.dpc.indb.ocen.buoy.service   
 * @Description:    TODO(数据库操作service 主要是操作结构化数据)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年5月24日 下午2:45:04   wuzuoqiang    Initial creation.
 * </pre>
 * 
 * @author wuzuoqiang
 *---------------------------------------------------------------------------------
 */
public class DbService {
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	public static String sod_report = StartConfig.reportSodCode();
	
	public static String V_TT = "BUOY";
	public static String V_BBB = "000";
	public static String V_CCCC = "9999";
	public static int defaultInt = 999999;
	public static int defaultInt2 = 999998;
	
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}
	/**
	 * 
	 * @Title: processSuccessReport   
	 * @Description:(海上浮标站数据处理)   
	 * @param  parseResult 解码结果集
	 * @param  recv_time 解析文件的路径
	 * @param  fileN 资料接收时间  
	 * @param fileN2 
	 * @return: DataBaseAction      
	 * @throws:
	 */
	public static DataBaseAction processSuccessReport(ParseResult<Buoy> parseResult, Date recv_time, String fileN, StringBuffer loggerBuffer, String filepath) {
		java.sql.Connection connection = null;
		java.sql.Connection reportConnection = null;
		try {
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			List<Buoy> buoys = parseResult.getData();
			insertBuoy(buoys, connection,recv_time, fileN,loggerBuffer, filepath);
			@SuppressWarnings("rawtypes")
			List<ReportInfo> reportInfos = parseResult.getReports();
			reportConnection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
			reportInfoToDb(reportInfos, reportConnection, recv_time,fileN,loggerBuffer);  
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
	 * 
	 * @Title: insertBuoy   
	 * @Description:(国内海上浮标站资料入库)   
	 * @param  buoys 待入库的对象集
	 * @param  connection 数据库连接
	 * @param  recv_time      资料接收时间
	 * @param fileN 
	 * @param loggerBuffer 
	 * @return: void      
	 * @throws:
	 */
	@SuppressWarnings("deprecation")
	private static void insertBuoy(List<Buoy> buoys, java.sql.Connection connection, Date recv_time, String fileN, StringBuffer loggerBuffer, String filepath) {
		PreparedStatement pStatement = null;
		// chy 去掉 D_RECORD_ID
		String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,D_UPDATE_TIME,"
				+ "V01301,V01300,V05001,V06001,V07001,V07002,V07301,V07302,V07303,V02001,"
				+ "V02320,V04001,V04002,V04003,V04004,V04005,V04006,V22400,V22049,V22300,"
				+ "V22300_052,V22301,V22301_052,V22380,V22380_061,V22381,V22381_061,V22073,V22073_052,V22073_061,"
				+ "V22385,V22386,V22062,V22062_701,V22306,V22306_701,V22310,V22310_701,V22311,V22311_701,"
				+ "V_BBB, D_SOURCE_ID)"
				+ "VALUES (?,?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?) ";
		if(connection != null){		
			try {	
				pStatement = new LoggableStatement(connection, sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				List<String> sqls = new ArrayList<>();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");		
				Buoy buoy = new Buoy();
				for(int idx = 0; idx < buoys.size(); idx ++){
					buoy = buoys.get(idx);
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
					
					int stationNumberN = defaultInt;
					String stat = buoy.getStationNumberChina();
					if (Pattern.matches("\\d{5}", stat))
						stationNumberN = Integer.parseInt(stat);
					else
						stationNumberN = Integer.parseInt(StationCodeUtil.stringToAscii(stat));
					
					int ii = 1;
					Date date = buoy.getObservationTime();
					String latitude=String.format("%.4f", buoy.getLatitude());
					String longtitude= String.format("%.4f", buoy.getLongtitude());
					pStatement.setString(ii++, sdf.format(date)+"_"+stat+"_"+latitude+"_"+longtitude);
					pStatement.setString(ii++, sod_code);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(buoy.getObservationTime().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					// "V01301,V01300,V05001,V06001,V07001,V07002,V07301,V07302,V07303,V02001,"
					pStatement.setString(ii++, stat);
					pStatement.setInt(ii++, stationNumberN);
					pStatement.setBigDecimal(ii++, new BigDecimal(buoy.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(buoy.getLongtitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(buoy.getHeightOfStationGroundAboveMeanSeaLevel())));
					pStatement.setInt(ii++, defaultInt2);
					pStatement.setInt(ii++, defaultInt2);
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(buoy.getDepthOfSeasaltSensor())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(buoy.getHeightOfWaveHeightSensor())));
					pStatement.setInt(ii++, buoy.getStationType());
					// + "V02320,V04001,V04002,V04003,V04004,V04005,V04006,V22400,V22049,V22300,"
					pStatement.setString(ii++, buoy.getCollectorType());
					pStatement.setInt(ii++, date.getYear() + 1900);
					pStatement.setInt(ii++, date.getMonth() + 1);
					pStatement.setInt(ii++, date.getDate());
					pStatement.setInt(ii++, date.getHours());
					pStatement.setInt(ii++, date.getMinutes());
					pStatement.setInt(ii++, date.getSeconds());
					//V22400,V22049,V22300,"
					
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(buoy.getBuoyDir()))); //浮标方位
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(buoy.getSeaTemperature()))); //海表温度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(buoy.getMaxSeaTemperature())));//海表最高温度
					// + "V22300_052,V22301,V22301_052
					
					pStatement.setInt(ii++, buoy.getTimeOfmaxSeaTemperature()); // 海表最高出现时间
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(buoy.getMinSeaTemperature())));//海表最低温度
					pStatement.setInt(ii++, buoy.getTimeOfMinSeaTemperature());//海表最低出现时间
					//,V22380,V22380_061,V22381,V22381_061,V22073,V22073_052,V22073_061,"
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(buoy.getSignificantWaveHeight())));//有效波高
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(buoy.getCycleOfSignificantWaveHeight())));//有效波高的周期
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(buoy.getAvgWaveHeight())));//平均波高
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(buoy.getCycleOfAvgWave())));//平均波周期
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(buoy.getMaxWaveHeight())));//最大波高
					pStatement.setInt(ii++, buoy.getTimeOfMaxWaveHeight());//最大波高出现时间
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(buoy.getCycleOfMaxWaveHeight())));//最大波高的周期
					
					// 	+ "V22385,V22386,V22062,V22062_701,V22306,V22306_701,V22310,V22310_701,V22311,V22311_701,"
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(buoy.getCurrentVelocity())));//表层海洋面流速
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(buoy.getCurrentDir())));//表层海洋面波向
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(buoy.getSalinity())));//海水盐度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(buoy.getAvgSalinity())));//海水平均盐度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(buoy.getElectricalConductivity())));//海水电导率
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(buoy.getAvgElectricalConductivity())));//海水平均电导率
					
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(buoy.getSeaTurbidity())));//海水浊度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(buoy.getAvgSeaTurbidity())));//海水平均浊度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(buoy.getChlorophyllConcentration())));//海水叶绿素浓度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(buoy.getAvgChlorophyllConcentration())));//海水平均叶绿素浓度
					
					pStatement.setString(ii++, V_BBB);
					pStatement.setString(ii++, cts_code);
					
					di.setIIiii(stat);
					di.setDATA_TIME(TimeUtil.date2String(date, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(buoy.getLatitude()));
					di.setLONGTITUDE(String.valueOf(buoy.getLongtitude()));
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
					di.setDATA_UPDATE_FLAG(V_BBB);
					di.setHEIGHT(String.valueOf(buoy.getHeightOfStationGroundAboveMeanSeaLevel()));
					
					pStatement.addBatch();
					sqls.add(((LoggableStatement)pStatement).getQueryString()); // 批量入库冲突时，单条入库
					listDi.add(di);
				} // end for
				try{
					pStatement.executeBatch();
					connection.commit();
					sqls.clear();
				}catch(SQLException e){
					pStatement.clearParameters();
					pStatement.clearBatch();
					connection.rollback();
					execute_sql(sqls, connection,fileN,loggerBuffer); // 此中，入库失败的会将对应的DI的ProcessState置为1 // 0:失败，1：成功
					loggerBuffer.append("\n Batch submission failed："+fileN);
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
	 * 
	 * @Title: reportInfoToDb   
	 * @Description:(报文信息入库)   
	 * @param  reportInfos 报文列表集合
	 * @param  connection  数据库连接
	 * @param  recv_time   资料时间
	 * @param loggerBuffer 
	 * @param fileN 
	 * @return: void      
	 * @throws:
	 */
	@SuppressWarnings({ "resource", "deprecation" })
	private static void reportInfoToDb(@SuppressWarnings("rawtypes") List<ReportInfo> reportInfos, java.sql.Connection connection, Date recv_time, String fileN, StringBuffer loggerBuffer) {
		PreparedStatement pStatement = null;
		// chy 去掉 D_RECORD_ID
		String sql = "INSERT INTO "+StartConfig.reportTable()+" (D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,"
				+ "V_BBB,V_CCCC,V_TT,V01301,V05001,V06001,"
				+ "V04001,V04002,V04003,V04004,V04005,V_LEN,V_REPORT,D_SOURCE_ID) VALUES"
				+ "(?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?)";
		try {
			pStatement = new LoggableStatement(connection, sql);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			for (int i = 0; i < reportInfos.size(); i++) {
				try {
					Buoy buoy = (Buoy) reportInfos.get(i).getT();
					String stat = buoy.getStationNumberChina();
					Date date = buoy.getObservationTime();
					String primkey = sdf.format(date)+"_"+stat+"_"+V_TT+"_"+ V_BBB;
					int ii = 1;
					pStatement.setString(ii++, primkey);
					pStatement.setString(ii++, sod_report);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(date.getTime()));
					pStatement.setString(ii++, V_BBB);
					pStatement.setString(ii++, V_CCCC);
					pStatement.setString(ii++, V_TT);
					pStatement.setString(ii++, stat);
					pStatement.setBigDecimal(ii++, new BigDecimal(buoy.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(buoy.getLongtitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setInt(ii++, date.getYear() + 1900);
					pStatement.setInt(ii++, date.getMonth() + 1);
					pStatement.setInt(ii++, date.getDate());
					pStatement.setInt(ii++, date.getHours());
					pStatement.setInt(ii++, date.getMinutes());
					pStatement.setInt(ii++, reportInfos.get(i).getReport().length());
					pStatement.setString(ii++, reportInfos.get(i).getReport());
					pStatement.setString(ii++, cts_code);
					
					pStatement.execute();
					connection.commit();
				} catch (Exception e) {
					loggerBuffer.append("sql error:"+e.getMessage());
					continue;
				}
			}
		} catch (SQLException e) {
			loggerBuffer.append("Database connection exception:" + e.getMessage());
			return;
		}finally {
			if(pStatement != null) {
				try {
					pStatement.close();
					pStatement = null;
				} catch (SQLException e) {
					loggerBuffer.append("\n Close statement error!");
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
	 * @param loggerBuffer 
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
}
