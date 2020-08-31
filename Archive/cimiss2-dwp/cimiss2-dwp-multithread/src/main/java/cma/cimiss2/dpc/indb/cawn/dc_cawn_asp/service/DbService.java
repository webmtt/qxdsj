package cma.cimiss2.dpc.indb.cawn.dc_cawn_asp.service;

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
import cma.cimiss2.dpc.decoder.bean.cawn.AerosolScatteringCharacteristics;
import cma.cimiss2.dpc.decoder.cawn.DecodeAsp;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.sand.ReportInfoService;

public class DbService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	public static String report_sod_code = StartConfig.reportSodCode();
	public static String v_tt ="CASP";
		
//	static Map<String, Object> proMap = StationInfo.getProMap();	
	
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}

	public static void main(String[] args) {
		DecodeAsp dec=new DecodeAsp();
		String filepath = "C:\\BaiduNetdiskDownload\\test\\G.0002.0003.R001\\Z_CAWN_I_54419_20191224050000_O_AER-FLD-ASP.TXT";
		File file = new File(filepath);
		ParseResult<AerosolScatteringCharacteristics> parseResult = dec.decodeFile(file);
		DataBaseAction action = null;
		Date recv_time = new Date();
		StringBuffer loggerBuffer = new StringBuffer();

//		if (StartConfig.getDatabaseType() == 1) {
			action = DbService.processSuccessReport(parseResult, filepath,recv_time,loggerBuffer);
			System.out.println("insertDBService over!");
//		}
	
	}
	
	/**
	 * 
	 * @Title: processSuccessReport
	 * @Description:(大气成分散射特性asp报文解码入库函数)
	 * @param parseResult 解码结果集
	 * @param fileN 文件对象
	 * @param recv_time 接收时间
	 * @param loggerBuffer 日志对象
	 * @return DataBaseAction 入库状态
	 * @throws：
	 */
	@SuppressWarnings("rawtypes")
	public static DataBaseAction processSuccessReport(ParseResult<AerosolScatteringCharacteristics> parseResult, String fileN,
			Date recv_time, StringBuffer loggerBuffer) {
		java.sql.Connection connection = null;
		java.sql.Connection reportConnection = null;
		try {
			// 获取数据库连接
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			// 获取解码结果集
			List<AerosolScatteringCharacteristics> list = parseResult.getData();
			insert_db(list, connection, recv_time, loggerBuffer,fileN);
			
			List<ReportInfo> reportInfos = parseResult.getReports();
			reportConnection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
			ReportInfoService.reportInfoToDb(reportInfos, reportConnection, "000", recv_time, "9999", v_tt, report_sod_code,cts_code, "16", loggerBuffer,fileN,listDi);
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
	 * @Title: insert_db
	 * @Description: (散射特性（ASP）监测项目小时资料批量入库函数)
	 * @param list 待入库对象
	 * @param connection 数据库连接
	 * @param recv_time 接收时间
	 * @param loggerBuffer 日志对象
	 * @param fileN 
	 * @throws：
	 */
	@SuppressWarnings({  "deprecation" })
	private static void insert_db(List<AerosolScatteringCharacteristics> list, java.sql.Connection connection, Date recv_time,
			StringBuffer loggerBuffer, String fileN) {
		Map<String, Object> proMap = StationInfo.getProMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");	
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,"
				+ "D_DATETIME,V01301,V01300,V05001,V06001,V07001,V_ACODE,V04001,V04002,V04003,V04004,"
				+ "V04005,V04006,V_ITEM_CODE,V_ITIME_SIGN,V_SEARCH_SIGN,V_RECORD_SIGN,V15700_701,"
				+ "V12001_701,V15710,V13003_701,V10004_701,V15720,V15721,V15722,V15723,V_M_STATE, "
				+ "V_D_STATE,V15800,V_BBB, D_SOURCE_ID, "
				+ "V_Scat_T_450nm,V_Scat_T_525nm,V_Scat_T_635nm,V_Scat_B_450nm,V_Scat_B_525nm,V_Scat_B_635nm,V_Scat_A_450nm,V_Scat_A_525nm,V_Scat_A_635nm) VALUES ("
				+"?,?, ?, ?, ?, ?, ?, ?, ?,?, "
				+"?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
				+"?, ?, ?, ?, ?, ?, ?, ?,?,?,"
				+"?, ?, ?, ?, ?, ?, "
				+"?, ?, ?, ?, ?, ?, ?, ?, ?)";
		if(connection != null){		
			try {	
				pStatement = new LoggableStatement(connection, sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				connection.setAutoCommit(false);
				List<String> sqls = new ArrayList<>();
				for(int i = 0; i<list.size();i++){
					AerosolScatteringCharacteristics asp = list.get(i);
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
					
					int adminCode = 999999;
					Double latitude = asp.getLatitude(); // 纬度
					Double longitude = asp.getLongitude(); // 经度
					Double height = asp.getHeightOfSationGroundAboveMeanSeaLevel(); // 测站海拔高度
					
					// 根根配置文件  获取站点基本信息
					String info = (String) proMap.get(asp.getStationNumberChina() + "+16");
					if(info == null)
						info =  (String) proMap.get(asp.getStationNumberChina() + "+01");
					
					if (info==null) {
						loggerBuffer.append("\n  In configuration file, this station does not exist: " + asp.getStationNumberChina());	
					}else{
						String[] infos = info.split(",");
						if (latitude == 999999.0 && !infos[2].trim().equals("null")) {
							latitude = Double.parseDouble(infos[2]);// 纬度
							asp.setLatitude(latitude);
						}
						if(longitude == 999999.0 && !infos[1].trim().equals("null")){
							longitude = Double.parseDouble(infos[1]); // 经度
							asp.setLongitude(longitude);
						}
						if(height == 999999.0 && !infos[3].trim().equals("null")){
							height = Double.parseDouble(infos[3]); // 测站海拔高度
							asp.setHeightOfSationGroundAboveMeanSeaLevel(height);
						}
							
						if(!infos[5].trim().equals("null")){
							adminCode = Integer.parseInt(infos[5]);
						}
						
					}
					
					int ii=1;
					String PrimaryKey = sdf.format(asp.getObservationTime())+"_"+asp.getStationNumberChina();		
					pStatement.setString(ii ++, PrimaryKey);// 记录标识
					pStatement.setString(ii ++, sod_code);// 资料标识
					pStatement.setTimestamp(ii ++,  new Timestamp(new Date().getTime()));// 入库时间
					pStatement.setTimestamp(ii ++,  new Timestamp(recv_time.getTime()));// 收到时间
					pStatement.setTimestamp(ii ++,  new Timestamp(new Date().getTime()));// 更新时间
					pStatement.setTimestamp(ii ++, new Timestamp(asp.getObservationTime().getTime()));// 资料时间
					pStatement.setString(ii ++, asp.getStationNumberChina());// 区站号(字符)
					pStatement.setBigDecimal(ii ++, new BigDecimal(StationCodeUtil.stringToAscii(asp.getStationNumberChina())));// 区站号(数字)
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(latitude)));// 纬度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(longitude)));// 经度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(height)));// 测站高度
					pStatement.setInt(ii++, adminCode);//中国行政区代码
					pStatement.setBigDecimal(ii ++, new BigDecimal(asp.getObservationTime().getYear() + 1900));// 资料观测年
					pStatement.setBigDecimal(ii ++, new BigDecimal(asp.getObservationTime().getMonth()+1));// 资料观测月
					pStatement.setBigDecimal(ii ++, new BigDecimal(asp.getObservationTime().getDate()));// 资料观测日
					pStatement.setBigDecimal(ii ++, new BigDecimal(asp.getObservationTime().getHours()));// 资料观测时
					pStatement.setBigDecimal(ii ++, new BigDecimal(asp.getObservationTime().getMinutes()));// 资料观测分
					pStatement.setBigDecimal(ii ++, new BigDecimal(asp.getObservationTime().getSeconds()));// 资料观测分
					pStatement.setString(ii++, asp.getItemCode());//项目代码
					pStatement.setBigDecimal(ii++,  new BigDecimal(String.valueOf(asp.getTimeSign())));//时间标记
					pStatement.setBigDecimal(ii++,  new BigDecimal(String.valueOf(asp.getSearchSign())));//检索标志
					pStatement.setBigDecimal(ii++,  new BigDecimal(String.valueOf(asp.getRecordType())));//记录种类
					pStatement.setBigDecimal(ii++,  new BigDecimal(String.valueOf(asp.getScatteringCoefficient_Avg())));//散射系数平均值
					pStatement.setBigDecimal(ii++,  new BigDecimal(String.valueOf(asp.getAtmosphericTemperature_Avg())));//大气温度平均值
					pStatement.setBigDecimal(ii++,  new BigDecimal(String.valueOf(asp.getSampleChamberTemperature_Avg())));//样气室温度平均值
					pStatement.setBigDecimal(ii++,  new BigDecimal(String.valueOf(asp.getRelativeHumidity_Avg())));//相对湿度平均值
					pStatement.setBigDecimal(ii++,  new BigDecimal(String.valueOf(asp.getAtmosphericPressure_Avg())));//大气压平均值
					pStatement.setBigDecimal(ii++,  new BigDecimal(String.valueOf(asp.getDarkCountDiagnosis())));//暗计数诊断
					pStatement.setBigDecimal(ii++,  new BigDecimal(String.valueOf(asp.getShutterCountDiagnosis())));//快门计数诊断
					pStatement.setBigDecimal(ii++,  new BigDecimal(String.valueOf(asp.getMeasurementRatioDiagnosis())));//测量比率诊断
					pStatement.setBigDecimal(ii++,  new BigDecimal(String.valueOf(asp.getFinalTestRatioDiagnosis())));//最后测试比率诊断
					pStatement.setBigDecimal(ii++,  new BigDecimal(String.valueOf(asp.getM_STATE())));//V_M_STATE,
					pStatement.setBigDecimal(ii++,  new BigDecimal(String.valueOf(asp.getD_STATE())));//V_D_STATE
					pStatement.setBigDecimal(ii++,  new BigDecimal(String.valueOf(asp.getDataRecordFrequency())));//数据记录频率
					pStatement.setString(ii++, "000");
					pStatement.setString(ii++, cts_code);
					
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(asp.getScatTotal_450nm()))); //Scat总散射系数450nm
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(asp.getScatTotal_525nm())));//Scat总散射系数525nm
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(asp.getScatTotal_635nm())));//Scat总散射系数635nm
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(asp.getScatBefore_450nm())));//Scat前散射系数450nm
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(asp.getScatBefore_525nm())));//Scat前散射系数525nm
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(asp.getScatBefore_635nm())));//Scat前散射系数635nm
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(asp.getScatAfter_450nm())));//Scat后散射系数450nm
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(asp.getScatAfter_525nm())));//Scat后散射系数525nm
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(asp.getScatAfter_635nm())));//Scat后散射系数635nm
					
					di.setIIiii(asp.getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(asp.getObservationTime(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLATITUDE(String.valueOf(latitude));
					di.setLONGTITUDE(String.valueOf(longitude));
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(fileN).length()));
					di.setDATA_UPDATE_FLAG("000");
					di.setHEIGHT(String.valueOf(height));
					
					// 批量提交
					pStatement.addBatch();
					sqls.add(((LoggableStatement)pStatement).getQueryString());
					System.out.println(((LoggableStatement)pStatement).getQueryString());
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
	/**
	 * 
	 * @Title: execute_sql
	 * @Description: (散射特性（ASP）监测项目小时资料单条入库函数)
	 * @param sqls sql对象集合
	 * @param connection 数据库连接
	 * @param loggerBuffer 日志对象
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
	
}
