package cma.cimiss2.dpc.indb.sand.dc_cawn_sand_koreaPM10.service;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.sand.KoreaSand_PM10;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class DbService {
	
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	public static String cts_code=StartConfig.ctsCode();
	public static String sod_code=StartConfig.sodCode();
	public static int defaultInt = 999999;
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
	 * @Description:(报文解码入库函数)
	 * @param parseResult 解码结果集
	 * @param fileN 文件对象
	 * @param recv_time 接收时间
	 * @param loggerBuffer 日志对象
	 * @return DataBaseAction 入库状态
	 * @throws：
	 */

	public static DataBaseAction processSuccessReport(ParseResult<KoreaSand_PM10> parseResult, String fileN,
			Date recv_time, StringBuffer loggerBuffer,String filepath) {
		java.sql.Connection connection = null;
		try {
			// 获取数据库连接
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			// 获取解码结果集
			List<KoreaSand_PM10> list = parseResult.getData();
			insert_db(list, connection, recv_time, loggerBuffer,fileN, filepath);
			
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
					loggerBuffer.append("\n Database connection close  error"+e.getMessage());
				}
			}
			
		}
	}
	
	/**
	 * 
	 * @Title: insert_db
	 * @Description: (韩国沙尘暴PM10观测资料批量入库函数)
	 * @param list 待入库对象
	 * @param connection 数据库连接
	 * @param recv_time 接收时间
	 * @param loggerBuffer 日志对象
	 * @param fileN 
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static void insert_db(List<KoreaSand_PM10> list, java.sql.Connection connection, Date recv_time,
			StringBuffer loggerBuffer, String fileN, String filepath) {
		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement pStatement = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");	
		String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,"
				+ "D_DATETIME,V01301,V01300,V05001,V06001,V07001,V_ACODE,V04001,V04002,V04003,V04004,V04005,"
				+ "V04006,V02321,V04016,V15471,V15487,V15488,V15471_001_701,V15471_024_701,V12001,V10004,"
				+ "V71617,V15730_P,V15730_S,V15880,V15883,V15886,V02375,V12001_P,V13003_P,V13003_S,V13003,V_BBB,D_SOURCE_ID) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
		if(connection != null){		
			try {	
				pStatement = new LoggableStatement(connection, sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				connection.setAutoCommit(false);
				List<String> sqls = new ArrayList<>();
				for(int i = 0; i<list.size();i++){
					KoreaSand_PM10 pm10 = list.get(i);
					StatDi di = new StatDi();					
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(sod_code);
					di.setDATA_TYPE_1(cts_code);
					di.setTT("韩国沙尘暴pm10资料");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1");  //1成功，0失败	
					int ii = 1;

					double statHeight = defaultInt;
					double adminCode = defaultInt;
					String info = (String) proMap.get(list.get(i).getStationNumberChina() + "+01");
					if(info == null) {
						loggerBuffer.append("\n In the configuration file, the station number does not exist:" + list.get(i).getStationNumberChina());
					}		
					else{
						String[] infos = info.split(",");
						if(!infos[3].equals("null")||!infos[3].equals("")){
							
							statHeight = Double.parseDouble(infos[3]);				
						}	
						if(!infos[5].equals("null")||!infos[5].equals("")){
							
							adminCode = Double.parseDouble(infos[5]);				
						}	
					}
					String PrimaryKey = sdf.format(pm10.getObservationTime())+"_"+pm10.getStationNumberChina();		
					pStatement.setString(ii ++, PrimaryKey);// 记录标识
					pStatement.setString(ii ++, sod_code);// 资料标识
					pStatement.setTimestamp(ii ++,  new Timestamp(new Date().getTime()));// 入库时间
					pStatement.setTimestamp(ii ++,  new Timestamp(recv_time.getTime()));// 收到时间
					pStatement.setTimestamp(ii ++,  new Timestamp(new Date().getTime()));// 更新时间
					pStatement.setTimestamp(ii ++, new Timestamp(pm10.getObservationTime().getTime()));// 资料时间
					pStatement.setString(ii ++, pm10.getStationNumberChina());// 区站号(字符)
					pStatement.setBigDecimal(ii ++, new BigDecimal(StationCodeUtil.stringToAscii(pm10.getStationNumberChina())));// 区站号(数字)
					pStatement.setBigDecimal(ii ++,new BigDecimal(String.valueOf(pm10.getLatitude())));// 纬度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(pm10.getLongitude())));// 经度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(statHeight)));// 测站高度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(adminCode)));//中国行政区代码
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(pm10.getObservationTime().getYear() + 1900)));// 资料观测年
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(pm10.getObservationTime().getMonth()+1)));// 资料观测月
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(pm10.getObservationTime().getDate())));// 资料观测日
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(pm10.getObservationTime().getHours())));// 资料观测时
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(pm10.getObservationTime().getMinutes())));// 资料观测分
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(pm10.getObservationTime().getSeconds())));// 资料观测秒
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(String.valueOf(pm10.getInstrumentStatusCode()))));// 仪器状态码
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(String.valueOf(pm10.getTimeInterval()))));// 观测时间间隔
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(String.valueOf(pm10.getAvgConcentration_5Min()))));// 质量浓度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(String.valueOf(pm10.getTotalMass()))));// 总质量
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(String.valueOf(pm10.getNormalFlow()))));// 质量标准流量
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(pm10.getAvgConcentration_1Hour())));// 1小时质量浓度平均值
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(pm10.getAvgConcentration_24Hour())));// 24小时质量浓度平均值
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(pm10.getAirTemperature())));// 环境温度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(pm10.getAirPressure())));// 环境气压
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(pm10.getProjectCode())));// 项目代码
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(pm10.getMainRoadFlow())));// 主路流量
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(pm10.getSideRoadFlow())));// 旁路流量
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(pm10.getLoadFactor())));// 负载率
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(pm10.getFrequency())));// 频率
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(pm10.getNoise())));// 噪音
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(pm10.getRunningState())));// 运行状态码
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(pm10.getMainRoadTemperature())));// 主路温度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(pm10.getMainRoadRelativeHumidity())));// 主路相对湿度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(pm10.getSideRoadRelativeHumidity())));// 旁路相对湿度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(pm10.getAirRelativeHumidity())));// 空气相对湿度
					pStatement.setString(ii++, "000");
					pStatement.setString(ii++, cts_code);
					
					di.setIIiii(list.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(pm10.getObservationTime(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(pm10.getLatitude()));
					di.setLONGTITUDE(String.valueOf(pm10.getLongitude()));
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
					di.setDATA_UPDATE_FLAG("000");
					di.setHEIGHT(String.valueOf(pm10.getHeightOfSationGroundAboveMeanSeaLevel()));
					
					// 批量提交
					pStatement.addBatch();
					sqls.add(((LoggableStatement)pStatement).getQueryString());
					listDi.add(di);				
				} // end for
				try{
					pStatement.executeBatch();
					connection.commit();
					loggerBuffer.append(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
					loggerBuffer.append(" INSERT SUCCESS COUNT ：" + list.size() + "\n");
					sqls.clear();
				}catch(SQLException e){
					pStatement.clearParameters();
					pStatement.clearBatch();
					loggerBuffer.append(" Batch ERROR : " + sdf.format(new Date()) + "\n");
					execute_sql(sqls, connection,loggerBuffer); // 此中，入库失败的会将对应的DI的ProcessState置为1 // 1:失败，0：成功
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
	 * @Title: execute_sql
	 * @Description:(韩国沙尘暴PM10观测资料单条入库函数)
	 * @param sqls sql对象集合
	 * @param connection 数据库连接
	 * @param loggerBuffer 日志对象
	 * @throws：
	 */
	private static void execute_sql(List<String> sqls, java.sql.Connection connection, StringBuffer loggerBuffer) {
		Statement pStatement = null;
		loggerBuffer.append("\n Start a single insert");
		try {
			connection.setAutoCommit(true);
			pStatement = connection.createStatement();
			for (int i = 0; i < sqls.size(); i++) {
//				pStatement = new LoggableStatement(connection, sqls.get(i));
				try {
					pStatement.execute(sqls.get(i));
					loggerBuffer.append(" " + listDi.get(i).getIIiii() + " " + listDi.get(i).getDATA_TIME() +" 000\n");
				} catch (Exception e) {
					// 单行插入失败
					loggerBuffer.append("\n " + listDi.get(i).getIIiii() + " " + listDi.get(i).getDATA_TIME()
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
