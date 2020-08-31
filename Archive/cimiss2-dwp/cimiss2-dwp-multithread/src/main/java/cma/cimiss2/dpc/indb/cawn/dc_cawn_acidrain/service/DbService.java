package cma.cimiss2.dpc.indb.cawn.dc_cawn_acidrain.service;

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
import cma.cimiss2.dpc.decoder.bean.cawn.AcidRainDailyData;
import cma.cimiss2.dpc.decoder.cawn.DecodeAcidRain;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.sand.ReportInfoService;

public class DbService {
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static String v_bbb="000";
	public static String acode="16";
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	public static String report_sod_code = StartConfig.reportSodCode();
	public int defaultInt = 999999;
//	static Map<String, Object> proMap = StationInfo.getProMap();
	
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}
	/**
	 * @param loggerBuffer 
	 * @param fileN
	 * @Title: processSuccessReport 
	 * @Description:(大气成分酸雨处理解码结果集，正确解码的报文入库) 
	 * @param parseResult  解码结果集
	 * @param filepath  文件的绝对路径
	 * @param recv_time  消息接收时间
	 * @return  返回值说明
	 * @throws
	 */

	public static DataBaseAction processSuccessReport(ParseResult<AcidRainDailyData> parseResult, Date recv_time, String fileN, StringBuffer loggerBuffer, String filepath) {
		java.sql.Connection connection = null;
		java.sql.Connection reportConnection = null;
		try {
			String v_tt="AR";
			String v_cccc = fileN.substring(9, 13);
		    char[] chars=v_cccc.toCharArray();  
	        boolean isPhontic = false;  
	        for(int i = 0; i < chars.length; i++) {  
	            isPhontic = (chars[i] >= 'a' && chars[i] <= 'z') || (chars[i] >= 'A' && chars[i] <= 'Z');  
	            if (!isPhontic) {  
	            	v_cccc="9999";  
	            }  
	        }  
			// 获取数据库连接
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			List<AcidRainDailyData> acidrain = parseResult.getData();
			insertDB(acidrain, connection, recv_time, v_tt,fileN, loggerBuffer,filepath);
		 
			@SuppressWarnings("rawtypes")
			List<ReportInfo> reportInfos = parseResult.getReports();
			reportConnection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
			ReportInfoService.reportInfoToDb(reportInfos, reportConnection, v_bbb, recv_time, v_cccc, v_tt, report_sod_code,cts_code,acode, loggerBuffer,filepath,listDi);
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
					loggerBuffer.append("\n Close databse connection error: "+e.getMessage());
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
	 * @param loggerBuffer 
	 * @param fileN 
	 * 
	 * @Title: insertDB   
	 * @Description: (大气成分酸雨日数据资料入库)   
	 * @param: @param acidrain 入库对象集合
	 * @param: @param connection 数据库连接
	 * @param: @param recv_time  接收时间
	 * @return: void      
	 * @throws
	 */
	@SuppressWarnings("deprecation")
	private static void insertDB(List<AcidRainDailyData> acidrain, java.sql.Connection connection,
			Date recv_time,String v_tt, String fileN, StringBuffer loggerBuffer, String filepath) {
		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V01301,"
				+ "V01300,V05001,V06001,V07001,V01405,V04001,V04002,V04003,V04307,V04308,V13011,V12501_01,V15532_01_1,V15532_01_2,"
				+ "V15532_01_3,V15532_01_701,V13371_01_1,V13371_01_2,V13371_01_3,V13371_01_701,V12501_02,V15532_02_1,V15532_02_2,"
				+ "V15532_02_3,V15532_02_701,V13371_02_1,V13371_02_2,V13371_02_3,V13371_02_701,V11001_14,V11002_14,V11001_20,V11002_20,"
				+ "V11001_02,V11002_02,V11001_08,V11002_08,V20003_01,V20003_02,V20003_03,V20003_04,V02452,V02450,V02451,V13372,V_BBB,D_SOURCE_ID) "
				+ "VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, ?, ?,?, ?,"
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
				+ "?,?,?,?) ";
		if(connection != null){		
			try {	
				pStatement = new LoggableStatement(connection, sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				connection.setAutoCommit(false);
				List<String> sqls = new ArrayList<>();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");		
				for(int i = 0; i <acidrain.size(); i ++){
					AcidRainDailyData ar = acidrain.get(i);
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
					
					double latitude=ar.getLatitude();//纬度
					double longitude=ar.getLongitude();//经度
					double height = ar.getHeightOfSationGroundAboveMeanSeaLevel();//测站高度
					if(latitude==999999.0||longitude==999999.0){
						String info = (String) proMap.get(ar.getStationNumberChina() + "+17");
						if(info == null) {
							    continue;
						}else{
							String[] infos = info.split(",");
							if(!(infos[1].trim().equals("null") || infos[1].trim().equals(""))){
								try{
								 longitude = Double.parseDouble(infos[1]);//经度
								}catch (Exception e) {
									loggerBuffer.append("\n In configuration file, " + "get longitude of " + ar.getStationNumberChina() + " error!");
								}
							}else{
								continue;
							}
							if(!(infos[2].trim().equals("null") || infos[2].trim().equals(""))){
								try{
								 latitude = Double.parseDouble(infos[2]);//纬度
								}catch (Exception e) {
									loggerBuffer.append("\n In configuration file, " + "get latitude of " + ar.getStationNumberChina() + " error!");
								}
							}else{
								continue;
							}
							if(!(infos[3].trim().equals("null") || infos[3].trim().equals(""))){
								try{
								 height = Double.parseDouble(infos[3]);//测站高度
								}catch (Exception e) {
									loggerBuffer.append("\n In configuration file, " + "get height of " + ar.getStationNumberChina() + " error!");
								}
							}
						}
					}
					int ii = 1;
//					String primary = sdf.format(ar.getObservationTime())+"_"+ar.getStationNumberChina();
					pStatement.setString(ii++, sdf.format(ar.getObservationTime())+"_"+ar.getStationNumberChina() + "_" + ar.getPrecipitation_StartTime() + "_" + ar.getPrecipitation_EndTime());//记录标识
					pStatement.setString(ii++, sod_code);//资料标识
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//入库时间
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));//收到时间
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间
					pStatement.setTimestamp(ii++, new Timestamp(ar.getObservationTime().getTime()));//资料时间
					pStatement.setString(ii++, ar.getStationNumberChina());//区站号(字符)
					pStatement.setInt(ii++,Integer.parseInt(StationCodeUtil.stringToAscii(ar.getStationNumberChina())) );//区站号(数字)
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(latitude)));//纬度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(longitude)));//经度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(height)));//测站高度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getObservationMethod())));//观测方式
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getObservationTime().getYear() + 1900)));//资料观测年
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getObservationTime().getMonth()+1)));//资料观测月
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getObservationTime().getDate())));//资料观测日
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getPrecipitation_StartTime())));//降水开始时间
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getPrecipitation_EndTime())));//降水结束时间
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getAcidRainObservedPrecipitation())));//酸雨观测降水量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getInitialSurveySampleTemperature())));//初测时样品温度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getInitialSurvey_PH_1())));//初测pH值第1次读数
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getInitialSurvey_PH_2())));//初测pH值第2次读数
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getInitialSurvey_PH_3())));//初测pH值第3次读数
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getInitialSurvey_Average_PH())));//初测平均pH值
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getInitialSurvey_K_1())));//初测K值第1次读数
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getInitialSurvey_K_2())));//初测K值第2次读数
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getInitialSurvey_K_3())));//初测K值第3次读数
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getInitialSurvey25_Average_K())));//初测25℃时平均K值
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getRetestSurveySampleTemperature())));//复测时样品温度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getRetestSurvey_PH_1())));//复测pH值第1次读数
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getRetestSurvey_PH_2())));//复测pH值第2次读数
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getRetestSurvey_PH_3())));//复测pH值第3次读数
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getRetestSurvey_Average_PH())));//复测平均pH值
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getRetestSurvey_K_1())));//复测K值第1次读数
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getRetestSurvey_K_2())));//复测K值第2次读数
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getRetestSurvey_K_3())));//复测K值第3次读数
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getRetestSurvey25_Average_K())));//复测25℃时平均K值
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getWindDirection_14())));//14时风向
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getWindSpeed_14())));//14时风速
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getWindDirection_20())));//20时风向
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getWindSpeed_20())));//20时风速
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getWindDirection_02())));//02时风向
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getWindSpeed_02())));//02时风速
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getWindDirection_08())));//08时风向
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getWindSpeed_08())));//08时风速
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getPrecipitationPeriodWeatherPhenomenon_1())));//降水期间天气现象1
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getPrecipitationPeriodWeatherPhenomenon_2())));//降水期间天气现象2
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getPrecipitationPeriodWeatherPhenomenon_3())));//降水期间天气现象3
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getPrecipitationPeriodWeatherPhenomenon_4())));//降水期间天气现象4
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getRepeatSurveyIndicatorCode())));//复测指示码
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getTemperatureCompensation_K_IndicatorCode())));//K值测量是否使用温度补偿功能指示码
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getDelaySampleIndicatorCode())));//因故延迟样品测量指示码
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(ar.getPrecipitationSampleException())));//降水样品异常状况
					pStatement.setString(ii++, "000");//降水样品异常状况
					pStatement.setString(ii++, cts_code);
					
					di.setIIiii(ar.getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(ar.getObservationTime(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(latitude));
					di.setLONGTITUDE(String.valueOf(longitude));
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
					di.setDATA_UPDATE_FLAG("000");
					di.setHEIGHT(String.valueOf(height));
					
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
					execute_sql(sqls, connection,fileN, loggerBuffer); // 此中，入库失败的会将对应的DI的ProcessState置为1 // 1:失败，0：成功
					loggerBuffer.append("\n Batch commit failed: "+fileN);
				}
			}catch (SQLException e) {
				loggerBuffer.append("\n Create Statement failed: "+e.getMessage());
			}finally {
				if(pStatement != null) {
					try {
						pStatement.close();
					} catch (SQLException e) {
						loggerBuffer.append("\n Close Statement failed: "+e.getMessage());
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
	 * @param: @param sqls
	 * @param: @param connection      
	 * @return: void      
	 * @throws
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
					connection.commit();
				} catch (Exception e) {
					loggerBuffer.append("\n File name："+fileN
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
	public static void main(String[] args) {
		File file = new File("C:\\BaiduNetdiskDownload\\test\\G.0003.0001.R001\\Z_CAWN_I_58040_20191223000000_O_AR_FTM.txt");
		String fileN = file.getName();
		Date recv_time = new Date();
		StringBuffer loggerBuffer = new StringBuffer();
		DecodeAcidRain decodeAcidRain = new DecodeAcidRain();
		ParseResult<AcidRainDailyData> parseResult = decodeAcidRain.decodeFile(file);	
		if(parseResult.isSuccess()){
			DataBaseAction action = DbService.processSuccessReport(parseResult, recv_time, fileN, loggerBuffer,file.getPath());
		}
	}
}
