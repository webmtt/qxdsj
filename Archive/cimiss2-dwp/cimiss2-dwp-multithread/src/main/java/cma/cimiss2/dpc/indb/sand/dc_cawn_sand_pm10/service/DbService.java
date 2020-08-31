package cma.cimiss2.dpc.indb.sand.dc_cawn_sand_pm10.service;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.sand.AtmosphericAerosolMassConcentration_PM10;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.sand.ReportInfoService;

public class DbService {
	
	HashMap<String, Integer> retryMap;
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
//    public static Map<String, Object> proMap = StationInfo.getProMap();
	public static String v_bbb="000";
	public static String acode="15";
	public static String cts_code=StartConfig.ctsCode();
	public static String sod_code=StartConfig.sodCode();
	public static String report_sod_code=StartConfig.reportSodCode();
	public static int defaultInt = 999999;
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}
	/**
	 * @param loggerBuffer 
	 * @Title: processSuccessReport 
	 * @Description:(处理解码结果集，正确解码的报文入库) 
	 * @param parseResult  解码结果集
	 * @param filepath  文件的绝对路径
	 * @param recv_time  消息接收时间
	 * @return  返回值说明
	 * @throws
	 */
	@SuppressWarnings("rawtypes")
	public static DataBaseAction processSuccessReport(ParseResult<AtmosphericAerosolMassConcentration_PM10> parseResult,
			String fileN, Date recv_time, StringBuffer loggerBuffer, String filepath) {
		java.sql.Connection connection = null;
		java.sql.Connection reportConnection = null;
		try {
			String v_tt=fileN.substring(7, 10);
			String v_cccc = "9999";
			// 获取数据库连接
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			List<AtmosphericAerosolMassConcentration_PM10> sand_pm10 = parseResult.getData();
			insertDB(sand_pm10, connection, recv_time, v_tt,fileN,loggerBuffer, filepath);
		 

			List<ReportInfo> reportInfos = parseResult.getReports();
			reportConnection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
			ReportInfoService.reportInfoToDb(reportInfos, reportConnection, v_bbb, recv_time, v_cccc, v_tt, report_sod_code,cts_code,acode, loggerBuffer,filepath,listDi);
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
			
			if(reportConnection != null) {
				try {
					reportConnection.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n Database connection close error"+e.getMessage());
				}
			}
		}
	}
	/**
	 * @param loggerBuffer 
	 * @param fileN 
	 * 
	 * @Title: insertDB   
	 * @Description:(沙尘暴气溶胶质量浓度PM10资料入库)   
	 * @param: @param sand_pm10  入库对象集合
	 * @param: @param connection 数据库连接
	 * @param: @param recv_time    接收时间  
	 * @return: void      
	 * @throws
	 */
	@SuppressWarnings("deprecation")
	private static void insertDB(List<AtmosphericAerosolMassConcentration_PM10> sand_pm10, java.sql.Connection connection,
			Date recv_time,String v_tt, String fileN, StringBuffer loggerBuffer, String filepath) {
		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,"
				+ "D_DATETIME,V01301,V01300,V05001,V06001,V07001,V_ACODE,V04001,V04002,V04003,V04004,V04005,"
				+ "V04006,V02321,V04016,V15471,V15487,V15488,V15471_001_701,V15471_024_701,V12001,V10004,"
				+ "V71617,V15730_P,V15730_S,V15880,V15883,V15886,V02375,V12001_P,V13003_P,V13003_S,V13003,V_BBB,D_SOURCE_ID) "
				+ "VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
		if(connection != null){		
			try {	
				pStatement = new LoggableStatement(connection, sql);
				connection.setAutoCommit(false);
				List<String> sqls = new ArrayList<>();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");		
				for(int i = 0; i <sand_pm10.size(); i ++){
					AtmosphericAerosolMassConcentration_PM10 pm10 = sand_pm10.get(i);
					StatDi di = new StatDi();	
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(sod_code);
					di.setDATA_TYPE_1(cts_code);
					di.setTT(v_tt);			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1");  //1成功，0失败	
					// 根根配置文件  获取站点基本信息	
					int adminCode = 999999;
					String info = (String) proMap.get(pm10.getStationNumberChina() + "+15");
					if(info == null) {
						loggerBuffer.append("\n In the configuration file, the station number does not exist" + pm10.getStationNumberChina());
					}else{
						String[] infos = info.split(",");
						if(!(infos[5].trim().equals("null") || infos[5].trim().equals(""))){
							adminCode = Integer.parseInt(infos[5]);
						}
					}
					int ii = 1;
					pStatement.setString(ii++, sdf.format(pm10.getObservationTime())+"_"+pm10.getStationNumberChina());//记录标识
					pStatement.setString(ii++, sod_code);//资料标识
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//入库时间
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));//收到时间
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间
					pStatement.setTimestamp(ii++, new Timestamp(sand_pm10.get(i).getObservationTime().getTime()));//资料时间
					pStatement.setString(ii++, pm10.getStationNumberChina());//区站号(字符)
					pStatement.setInt(ii++,Integer.parseInt(StationCodeUtil.stringToAscii(pm10.getStationNumberChina())) );//区站号(数字)
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(pm10.getLatitude())));//纬度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(pm10.getLongitude())));//经度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(pm10.getHeightOfSationGroundAboveMeanSeaLevel())));//测站海拔高度
					pStatement.setInt(ii++, adminCode);//中国行政区代码
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(pm10.getObservationTime().getYear() + 1900)));//资料观测年
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(pm10.getObservationTime().getMonth()+1)));//资料观测月
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(pm10.getObservationTime().getDate())));//资料观测日
					pStatement.setInt(ii++, pm10.getObservationTime().getHours());//资料观测时
					pStatement.setInt(ii++, pm10.getObservationTime().getMinutes());//资料观测分
					pStatement.setInt(ii++, pm10.getObservationTime().getSeconds());//资料观测秒
					pStatement.setString(ii++, pm10.getInstrumentStatusCode());//仪器状态码
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(pm10.getTimeInterval())));//观测时间间隔
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(pm10.getMassConcentration())));//质量浓度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(pm10.getTotalMass())));//总质量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(pm10.getNormalFlow())));//质量标准流量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(pm10.getAverageMassConcentration_1())));//1小时质量浓度平均值
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(pm10.getAverageMassConcentration_24())));//24小时质量浓度平均值
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(pm10.getEnvironmentTemperature())));//环境温度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(pm10.getEnvironmentPressure())));//环境气压
					pStatement.setBigDecimal(ii++, new BigDecimal(defaultInt));//项目代码
					pStatement.setBigDecimal(ii++, new BigDecimal(defaultInt));//主路流量
					pStatement.setBigDecimal(ii++, new BigDecimal(defaultInt));//旁路流量
					pStatement.setBigDecimal(ii++, new BigDecimal(defaultInt));//负载率
					pStatement.setBigDecimal(ii++, new BigDecimal(defaultInt));//频率
					pStatement.setBigDecimal(ii++, new BigDecimal(defaultInt));//噪音
					pStatement.setBigDecimal(ii++, new BigDecimal(defaultInt));//运行状态码
					pStatement.setBigDecimal(ii++, new BigDecimal(defaultInt));//主路温度
					pStatement.setBigDecimal(ii++, new BigDecimal(defaultInt));//主路相对湿度
					pStatement.setBigDecimal(ii++, new BigDecimal(defaultInt));//旁路相对湿度
					pStatement.setBigDecimal(ii++, new BigDecimal(defaultInt));//空气相对湿度
					pStatement.setString(ii++, "000");
					pStatement.setString(ii++, cts_code);
					
					di.setIIiii(pm10.getStationNumberChina());
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
					execute_sql(sqls, connection,fileN,loggerBuffer); // 此中，入库失败的会将对应的DI的ProcessState置为1 // 1:失败，0：成功
					loggerBuffer.append("\n Batch commit failed："+fileN);
				}
			}catch (SQLException e) {
				loggerBuffer.append("\n create Statementn error"+e.getMessage());
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
	 * @param loggerBuffer 
	 * @param fileN 
	 * 
	 * @Title: execute_sql   
	 * @Description:(批量入库失败时，采用逐条提交)   
	 * @param: @param sqls sql对象集合
	 * @param: @param connection   数据库连接   
	 * @return: void      
	 * @throws
	 */
	private static void execute_sql(List<String> sqls, java.sql.Connection connection, String fileN, StringBuffer loggerBuffer) {
		Statement pStatement = null;
		try {
			pStatement = connection.createStatement();
			for (int i = 0; i < sqls.size(); i++) {
//				pStatement = new LoggableStatement(connection, sqls.get(i));
				try {
					pStatement.execute(sqls.get(i));
					connection.commit();
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
