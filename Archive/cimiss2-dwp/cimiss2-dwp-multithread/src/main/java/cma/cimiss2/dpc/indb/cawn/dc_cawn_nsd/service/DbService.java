package cma.cimiss2.dpc.indb.cawn.dc_cawn_nsd.service;

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
import cma.cimiss2.dpc.decoder.bean.cawn.AtmosphericCompositionAerosolConcentration;
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
	 * @Description: (大气成分气溶胶数浓度（NSD）处理解码结果集，正确解码的报文入库)
	 * @param parseResult 解码结果集
	 * @param fileN
	 * @param recv_time 接收时间
	 * @param loggerBuffer 
	 * @return DataBaseAction 入库状态
	 * @throws：
	 */
	@SuppressWarnings("rawtypes")
	public static DataBaseAction processSuccessReport(ParseResult<AtmosphericCompositionAerosolConcentration> parseResult,
			String fileN, Date recv_time, StringBuffer loggerBuffer, String filepath) {
		java.sql.Connection connection = null;
		java.sql.Connection reportConnection = null;
		try {
			String v_tt="NSD";
			// 获取数据库连接
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			List<AtmosphericCompositionAerosolConcentration> nsd = parseResult.getData();
			insertDB(nsd, connection, recv_time,v_tt,loggerBuffer,fileN,filepath);
		 
			List<ReportInfo> reportInfos = parseResult.getReports();
			reportConnection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
			ReportInfoService.reportInfoToDb(reportInfos, reportConnection, "000", recv_time, "9999", v_tt, report_sod_code,cts_code ,"16", loggerBuffer,filepath,listDi);
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
					loggerBuffer.append("\n Close Database connection error: "+e.getMessage());
				}
			}
			if(reportConnection != null) {
				try {
					reportConnection.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n Close Database connection error: "+e.getMessage());
				}
			}
		}
	}
	/**
	 * 
	 * @Title: insertDB
	 * @Description: (大气成分气溶胶数浓度（NSD）资料入库函数)
	 * @param list 待入库对象
	 * @param connection 数据库对象
	 * @param recv_time 接收时间
	 * @param v_tt 报文类别
	 * @param loggerBuffer 日志对象
	 * @param fileN 
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static void insertDB(List<AtmosphericCompositionAerosolConcentration> list, java.sql.Connection connection,
			Date recv_time, String v_tt, StringBuffer loggerBuffer, String fileN, String filepath) {
		Map<String, Object> proMap = StationInfo.getProMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		PreparedStatement pStatement = null;
		try {
			String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V01301,V01300,V05001,V06001,V07001,V_ACODE,"
					+ "V04001,V04002,V04003,V04004,V04005,V04006,V_ITEM_CODE,V_STORAGE_PLACE,V_WEIGHT_FACTOR,V_ERROR_CODE,V15752,V15765,V_CORRECT_COUNT,V_PCOUNT,"
					+"V_BACKUP1,V12001_040,V13003_040,V_TIME_SPACING,V11002_071,V11001_071,V13011_071,V12001_074,V13003_074,V10004_074,V12001_075,V13003_075,"
					+"V10004_075,V11450,V11451,V02440,V10004,V_BACKUP2,V13003,V12001,V11002,V11001,V13011,V15023_C01,V15023_C02,V15023_C03,V15023_C04,V15023_C05,"
					+"V15023_C06,V15023_C07,V15023_C08,V15023_C09,V15023_C10,V15023_C11,V15023_C12,V15023_C13,V15023_C14,V15023_C15,V15023_C16,"
					+"V15023_C17,V15023_C18,V15023_C19,V15023_C20,V15023_C21,V15023_C22,V15023_C23,V15023_C24,V15023_C25,V15023_C26,V15023_C27,V15023_C28,"
					+"V15023_C29,V15023_C30,V15023_C31,V15023_C32,V_BBB, D_SOURCE_ID) VALUES ("
					+"?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?,"
					+"?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+"?, ?, ?, ?, ?, ?, ?, ?,?,"
					+"?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+"?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+"?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+"?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			if(connection != null){
				pStatement = new LoggableStatement(connection, sql); 
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				List<String> sqls = new ArrayList<>();
				for(int i=0;i<list.size();i++){
					AtmosphericCompositionAerosolConcentration nsd = list.get(i);
					// 设置DI信息
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
					String info = (String) proMap.get(nsd.getStationNumberChina() + "+16");
					int adminCode = 999999;
					double lon =999999;
					double lat =999999;
					double height =999999;
					if(info == null)
						info = (String) proMap.get(nsd.getStationNumberChina() + "+01");
					
					if(info == null) {
						loggerBuffer.append("\n  In configuration file, this station does not exist: " + nsd.getStationNumberChina());		
					}else{
						String[] infos = info.split(",");
						if(!(infos[5].trim().equals("null") || infos[5].trim().equals(""))){
							adminCode = Integer.parseInt(infos[5]);
						}	
						if(!(infos[1].trim().equals("null") || infos[1].trim().equals(""))){
							lon = Double.parseDouble(infos[1]);
							nsd.setLongitude(lon);
						}
						if(!(infos[2].trim().equals("null") || infos[2].trim().equals(""))){
							lat = Double.parseDouble(infos[2]);
							nsd.setLatitude(lat);
						}
						if(!(infos[3].trim().equals("null") || infos[3].trim().equals(""))){
							height = Double.parseDouble(infos[3]);
						}
					}
					int ii=1;
					String PrimaryKey = sdf.format(nsd.getObservationTime())+"_"+nsd.getStationNumberChina();		
					pStatement.setString(ii ++, PrimaryKey);// 记录标识
					pStatement.setString(ii ++, sod_code);// 资料标识
					pStatement.setTimestamp(ii ++,  new Timestamp(new Date().getTime()));// 入库时间
					pStatement.setTimestamp(ii ++,  new Timestamp(recv_time.getTime()));// 收到时间
					pStatement.setTimestamp(ii ++,  new Timestamp(new Date().getTime()));// 更新时间
					pStatement.setTimestamp(ii ++, new Timestamp(nsd.getObservationTime().getTime()));// 资料时间
					pStatement.setString(ii ++, nsd.getStationNumberChina());// 区站号(字符)
					pStatement.setBigDecimal(ii ++, new BigDecimal(StationCodeUtil.stringToAscii(nsd.getStationNumberChina())));// 区站号(数字)
					pStatement.setBigDecimal(ii ++,new BigDecimal(String.valueOf(lat)));// 纬度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(lon)));// 经度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(height)));// 测站高度
					pStatement.setInt(ii++, adminCode);//中国行政区代码
					pStatement.setBigDecimal(ii ++, new BigDecimal(nsd.getObservationTime().getYear() + 1900));// 资料观测年
					pStatement.setBigDecimal(ii ++, new BigDecimal(nsd.getObservationTime().getMonth()+1));// 资料观测月
					pStatement.setBigDecimal(ii ++, new BigDecimal(nsd.getObservationTime().getDate()));// 资料观测日
					pStatement.setBigDecimal(ii ++, new BigDecimal(nsd.getObservationTime().getHours()));// 资料观测时
					pStatement.setBigDecimal(ii ++, new BigDecimal(nsd.getObservationTime().getMinutes()));// 资料观测分
					pStatement.setBigDecimal(ii ++, new BigDecimal(nsd.getObservationTime().getSeconds()));// 资料观测分
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getItemCode())));//项目代码
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getStoragePlace())));//存储位置
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getWeightFactor())));//重量因数
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getErrorCode())));//错误代码
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getBatteryVoltageCode())));//电池电压代码
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getValveCurrent())));//阀电流
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getCorrectCount())));//综合订正计数
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getPressureCount())));//气压计数
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getBackUp1())));//备用1
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getTemperatureCount())));//温度计数
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getHumidityCount())));//湿度计数
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getTimeInterval())));//时间间隔
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getWindSpeedMeteringFactor())));//风速计量因子
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getWindDirectionMeteringFactor())));//风向计量因子
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getPrecipitationMeteringFactor())));//降水计量因子
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getTemperatureSlopeCorrect())));//温度斜率订正
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getHumiditySlopeCorrect())));//湿度斜率订正
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getPressureSlopeCorrect())));//气压斜率订正
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getTemperatureBiasCorrect())));//温度偏移订正
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getHumidityBiasCorrect())));//湿度偏移订正
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getPressureBiasCorrect())));//气压偏移订正
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getWindSpeedSensitivity())));//风速灵敏度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getWindDirectionDip())));//风速 倾角
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(nsd.getPrecipitationSensorCorrectFactor())));//降水传感器订正因子
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getAirPressure())));//气压
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getBackUp2())));//备用2
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getHumidity())));//湿度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getTemperature())));//温度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getWindSpeed())));//风速
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getWindDirection())));//风向
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getPrecipitation())));//降水
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getChannelConcentration_C1())));//C1通道数浓度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getChannelConcentration_C2())));//C2通道数浓度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getChannelConcentration_C3())));//C3通道数浓度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getChannelConcentration_C4())));//C4通道数浓度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getChannelConcentration_C5())));//C5通道数浓度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getChannelConcentration_C6())));//C6通道数浓度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getChannelConcentration_C7())));//C7通道数浓度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getChannelConcentration_C8())));//C8通道数浓度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getChannelConcentration_C9())));//C9通道数浓度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getChannelConcentration_C10())));//C10通道数浓度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getChannelConcentration_C11())));//C11通道数浓度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getChannelConcentration_C12())));//C12通道数浓度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getChannelConcentration_C13())));//C13通道数浓度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getChannelConcentration_C14())));//C14通道数浓度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getChannelConcentration_C15())));//C15通道数浓度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getChannelConcentration_C16())));//C16通道数浓度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getChannelConcentration_C17())));//C17通道数浓度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getChannelConcentration_C18())));//C18通道数浓度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getChannelConcentration_C19())));//C19通道数浓度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getChannelConcentration_C20())));//C20通道数浓度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getChannelConcentration_C21())));//C21通道数浓度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getChannelConcentration_C22())));//C22通道数浓度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getChannelConcentration_C23())));//C23通道数浓度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getChannelConcentration_C24())));//C24通道数浓度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getChannelConcentration_C25())));//C25通道数浓度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getChannelConcentration_C26())));//C26通道数浓度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getChannelConcentration_C27())));//C27通道数浓度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getChannelConcentration_C28())));//C28通道数浓度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getChannelConcentration_C29())));//C29通道数浓度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getChannelConcentration_C30())));//C30通道数浓度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getChannelConcentration_C31())));//C31通道数浓度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(nsd.getChannelConcentration_C32())));//C32通道数浓度
					pStatement.setString(ii++, "000");
					pStatement.setString(ii++, cts_code);
					
					di.setIIiii(nsd.getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(nsd.getObservationTime(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLATITUDE(String.valueOf(lat));
					di.setLONGTITUDE(String.valueOf(lon));
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
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
			loggerBuffer.append("\n Create Statement error: "+e.getMessage());
		}finally {
			try {
				if(pStatement != null)
					pStatement.close();
			} catch (SQLException e) {
				loggerBuffer.append("\n  Close Statement error: "+e.getMessage());
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
		loggerBuffer.append("\n Start insert one by one!");
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
