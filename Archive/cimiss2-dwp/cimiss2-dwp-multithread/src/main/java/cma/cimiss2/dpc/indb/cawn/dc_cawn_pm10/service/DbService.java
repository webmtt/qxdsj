package cma.cimiss2.dpc.indb.cawn.dc_cawn_pm10.service;

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
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.cawn.CawnPM10;
import cma.cimiss2.dpc.decoder.cawn.DecodePm10;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.sand.ReportInfoService;


public class DbService {
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
//    public static Map<String, Object> proMap = StationInfo.getProMap();
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	public static String report_sod_code = StartConfig.reportSodCode();
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}
	/**
	 * 
	 * @Title: processSuccessReport
	 * @Description: (大气成分气溶胶PM10质量浓度（PM10）处理解码结果集，正确解码的报文入库)
	 * @param parseResult 解码结果集
	 * @param filepath 文件路径
	 * @param recv_time 接收时间
	 * @param loggerBuffer 
	 * @return DataBaseAction 入库状态
	 * @throws：
	 */
	@SuppressWarnings("rawtypes")
	public static DataBaseAction processSuccessReport(ParseResult<CawnPM10> parseResult,
			String fileN, Date recv_time, StringBuffer loggerBuffer) {
		java.sql.Connection connection = null;
		java.sql.Connection reportConnection = null;
		try {
			String v_tt="PM10";
			// 获取数据库连接
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			List<CawnPM10> pm10 = parseResult.getData();
			insertDB(pm10, connection, recv_time,v_tt,loggerBuffer,fileN);
		 
			List<ReportInfo> reportInfos = parseResult.getReports();
			reportConnection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
			ReportInfoService.reportInfoToDb(reportInfos, reportConnection, "000", recv_time, "9999", v_tt, report_sod_code, cts_code,"16", loggerBuffer,fileN,listDi);
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
	 * @Description:(大气成分气溶胶PM10质量浓度（PM10）入库函数)
	 * @param list 待入库对象
	 * @param connection 数据库连接
	 * @param recv_time 接收时间
	 * @param v_tt 报文类别
	 * @param loggerBuffer 日志对象
	 * @param fileN 
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static void insertDB(List<CawnPM10> list, java.sql.Connection connection,
			Date recv_time, String v_tt, StringBuffer loggerBuffer, String fileN) {
		Map<String, Object> proMap = StationInfo.getProMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		PreparedStatement pStatement = null;
		try {
			String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,"
					+ "D_DATETIME,V01301,V01300,V05001,V06001,V07001,V_ACODE,V04001,V04002,V04003,V04004,V04005,"
					+ "V04006,V02321,V04016,V15471,V15487,V15488,V15471_001_701,V15471_024_701,V12001,V10004,"
					+ "V71617,V15730_P,V15730_S,V15880,V15883,V15886,V02375,V12001_P,V13003_P,V13003_S,V13003,V_BBB,D_SOURCE_ID) "
					+ "VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
			
			if(connection != null){
				pStatement = new LoggableStatement(connection, sql); 
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				List<String> sqls = new ArrayList<>();
				for(int i=0;i<list.size();i++){
					CawnPM10 pm10 = list.get(i);
					// 设置DI信息
					StatDi di = new StatDi();					
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(sod_code);
					di.setDATA_TYPE_1(cts_code);
					di.setTT(v_tt);			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //0成功，1失败
					di.setPROCESS_STATE("1");  //0成功，1失败
					// 根根配置文件  获取站点基本信息
					String info = (String) proMap.get(pm10.getStationNumberChina() + "+16");
					int adminCode = 999999;
					double lon =999999;
					double lat =999999;
					double height =999999;
					if(info == null) {
						loggerBuffer.append("\n In configuration file, this station does not exist: " + pm10.getStationNumberChina());			
					}else {
						String[] infos = info.split(",");
						if(!(infos[5].trim().equals("null") || infos[5].trim().equals(""))){
							adminCode = Integer.parseInt(infos[5]);
						}	
						if(!(infos[1].trim().equals("null") || infos[1].trim().equals(""))){
							lon = Double.parseDouble(infos[1]);
							pm10.setLongitude(lon);
						}
						if(!(infos[2].trim().equals("null") || infos[2].trim().equals(""))){
							lat = Double.parseDouble(infos[2]);
							pm10.setLatitude(lat);
						}
						if(!(infos[3].trim().equals("null") || infos[3].trim().equals(""))){
							height = Double.parseDouble(infos[3]);
						}
					}			
					int ii=1;
					String PrimaryKey = sdf.format(pm10.getObservationTime())+"_"+pm10.getStationNumberChina();		
					pStatement.setString(ii ++, PrimaryKey);// 记录标识
					pStatement.setString(ii ++, sod_code);// 资料标识
					pStatement.setTimestamp(ii ++,  new Timestamp(new Date().getTime()));// 入库时间
					pStatement.setTimestamp(ii ++,  new Timestamp(recv_time.getTime()));// 收到时间
					pStatement.setTimestamp(ii ++,  new Timestamp(new Date().getTime()));// 更新时间
					pStatement.setTimestamp(ii ++, new Timestamp(pm10.getObservationTime().getTime()));// 资料时间
					pStatement.setString(ii ++, pm10.getStationNumberChina());// 区站号(字符)
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(StationCodeUtil.stringToAscii(pm10.getStationNumberChina()))));// 区站号(数字)
					pStatement.setBigDecimal(ii ++,new BigDecimal(String.valueOf(lat)));// 纬度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(lon)));// 经度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(height)));// 测站高度
					pStatement.setInt(ii++, adminCode);//中国行政区代码
					pStatement.setBigDecimal(ii ++, new BigDecimal(pm10.getObservationTime().getYear() + 1900));// 资料观测年
					pStatement.setBigDecimal(ii ++, new BigDecimal(pm10.getObservationTime().getMonth()+1));// 资料观测月
					pStatement.setBigDecimal(ii ++, new BigDecimal(pm10.getObservationTime().getDate()));// 资料观测日
					pStatement.setBigDecimal(ii ++, new BigDecimal(pm10.getObservationTime().getHours()));// 资料观测时
					pStatement.setBigDecimal(ii ++, new BigDecimal(pm10.getObservationTime().getMinutes()));// 资料观测分
					pStatement.setBigDecimal(ii ++, new BigDecimal(pm10.getObservationTime().getSeconds()));// 资料观测分
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(pm10.getInstrumentStatusCode())));// 仪器状态码
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(pm10.getTimeInterval())));// 观测时间间隔
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(pm10.getAvgConcentration_5Min())));// 质量浓度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(pm10.getTotalMass())));// 总质量
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(pm10.getNormalFlow())));// 质量标准流量
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
					
					di.setIIiii(pm10.getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(pm10.getObservationTime(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLATITUDE(String.valueOf(lat));
					di.setLONGTITUDE(String.valueOf(lon));
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(fileN).length()));
					di.setDATA_UPDATE_FLAG("000");
					di.setHEIGHT(String.valueOf(height));
					// 批量提交
					pStatement.addBatch();
					sqls.add(((LoggableStatement)pStatement).getQueryString());
					listDi.add(di);	
				}			
				try {
					// 执行批量			
					pStatement.executeBatch();
					// 手动提交			
					connection.commit();
					loggerBuffer.append(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
					loggerBuffer.append(" INSERT SUCCESS COUNT ：" + list.size() + "\n");
					sqls.clear();
				} catch (Exception e) {
					pStatement.clearParameters();
					pStatement.clearBatch();
					loggerBuffer.append(" Batch ERROR : " + sdf.format(new Date()) + "\n");
					execute_sql(sqls, connection,loggerBuffer);
					
				}			
			} else {
				loggerBuffer.append("\n Database connection error!");
			}														
		} catch (SQLException e) {		
			loggerBuffer.append("\n Create statement error: "+e.getMessage());
		}finally {
			try {
				if(pStatement != null)
					pStatement.close();
			} catch (SQLException e) {
				loggerBuffer.append("\n Close statement error: "+e.getMessage());
			}			
		}
	}
		
		
	/**
	 * 
	 * @Title: execute_sql
	 * @Description: (批量入库失败时，采用逐条提交)  
	 * @param sqls sql对象集合
	 * @param connection  数据库连接
	 * @param loggerBuffer 
	 * @throws：
	 */
	private static void execute_sql(List<String> sqls, java.sql.Connection connection, StringBuffer loggerBuffer) {
		Statement pStatement = null;
		loggerBuffer.append("\n Start insert one by one.");
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
	public static void main(String[] args){
		String filepath ="D:\\TEMP\\pm10\\Z_CAWN_I_54084_20190318070000_O_AER-FLD-PM10.TXT";
		File file = new File(filepath);
		Date recv_time = new Date(file.lastModified());
		StringBuffer loggerBuffer = new StringBuffer();
		DecodePm10 decodePm10 = new DecodePm10();
		ParseResult<CawnPM10> parseResult = decodePm10.decodeFile(file);
		if(parseResult.isSuccess()){
			DataBaseAction action = DbService.processSuccessReport(parseResult, filepath, recv_time, loggerBuffer);
		}
	}
}
