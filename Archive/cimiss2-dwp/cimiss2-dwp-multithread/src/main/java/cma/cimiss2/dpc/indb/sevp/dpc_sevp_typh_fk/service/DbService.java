package cma.cimiss2.dpc.indb.sevp.dpc_sevp_typh_fk.service;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.sevp.Typh;
import cma.cimiss2.dpc.decoder.bean.sevp.TyphEle;
import cma.cimiss2.dpc.decoder.bean.sevp.TyphKey;
import cma.cimiss2.dpc.decoder.sevp.DecodeTyph;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class DbService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	public static String sod_report_code = StartConfig.reportSodCode();
	
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
	public static DataBaseAction processSuccessReport(ParseResult<Typh> parseResult, String filepath, Date recv_time, String fileN, StringBuffer loggerBuffer) {
		java.sql.Connection connection = null;
		java.sql.Connection reportConnection = null;
		try {
			// 获取数据库连接
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			// 获取解码结果集
			List<Typh> typhs = parseResult.getData();
//			String  correctsign = null;
			List<Typh> typhs_normal = new ArrayList<>();//非更正报文集合
//			List<Typh> typhs_correct = new ArrayList<>();//更正报文集合
//			Typh typh = null;
			typhs_normal = typhs;
//			for(int j = 0; j < typhs.size(); j ++){
//				typh = typhs.get(j);
//				correctsign = typh.getTyphKey().getV_BBB();//用更正报标志判断是否为更正报文
//				if(correctsign.equals("000")){
//					typhs_normal.add(typh);
//				}else{
//					typhs_correct.add(typh);
//				}
//			}
			//报文表
			@SuppressWarnings("rawtypes")
			List<ReportInfo> reportInfos = parseResult.getReports();
		    reportConnection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
		    reportInfoToDb(reportInfos, reportConnection, recv_time, loggerBuffer, fileN);
			// 键表、要素表
			if(typhs_normal.size() > 0){
				DataBaseAction insertKeyDB = insertKey(typhs_normal, connection, recv_time,fileN, loggerBuffer);//批量插入键表		
				if(insertKeyDB == DataBaseAction.SUCCESS) { // 批量插入键表成功
					DataBaseAction insertEleDB = insertEle(typhs_normal, connection, recv_time,fileN,loggerBuffer);//插入要素表
					if(insertEleDB == DataBaseAction.BATCH_ERROR){
						for (int i = 0; i < typhs_normal.size(); i++) { 
							DataBaseAction insertOneLine_key_db = insertOneLine_key_db(typhs_normal.get(i).getTyphKey(), connection, recv_time,fileN, loggerBuffer);//单条插入键表
							if(insertOneLine_key_db == DataBaseAction.SUCCESS) {//单条插入键表成功
								insertOneLine_Ele_db(typhs_normal.get(i) ,connection, recv_time,fileN, loggerBuffer);
							}
						}
					}
				}  
				else if (insertKeyDB == DataBaseAction.BATCH_ERROR) {//批量插入键表失败
					for (int i = 0; i < typhs_normal.size(); i++) { 
						DataBaseAction insertOneLine_key_db = insertOneLine_key_db(typhs_normal.get(i).getTyphKey(), connection, recv_time,fileN, loggerBuffer);//单条插入键表
						if(insertOneLine_key_db == DataBaseAction.SUCCESS) {//单条插入键表成功
							insertOneLine_Ele_db(typhs_normal.get(i) ,connection, recv_time,fileN, loggerBuffer);//单条插入要素表
						}
					}
				}
				else {
	  				return insertKeyDB;
				}	
			}
			//更正报文
//			if(typhs_correct.size() > 0){
//				updatekeyDB(typhs_correct, connection,recv_time,fileN, loggerBuffer);
//			}
		
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
					loggerBuffer.append("\n Close databasse connection error: "+e.getMessage());
				}
			}
			
			if(reportConnection != null) {
				try {
					reportConnection.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n Close databasse connection error: "+e.getMessage());
				}
			}
		}
	}
	/**
	 * 
	 * @Title: insertKey   
	 * @Description: TODO(台风实况与预报键表入库)   
	 * @param typhs 待入库对象集合
	 * @param connection 数据库连接
	 * @param recv_time 资料接收接收
	 * @return DataBaseAction 数据入库状态      
	 * @throws：
	 */
	private static DataBaseAction insertKey(List<Typh> typhs, java.sql.Connection connection, Date recv_time, String fileN, StringBuffer loggerBuffer){
		PreparedStatement pStatement = null;
		try {
			String sql = "INSERT INTO "+StartConfig.keyTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,"
					+ "V_BBB,V04001_02,V04002_02,V04003_02,V04004_02,V_CCCC,V01035,V_TYPH_NAME,V01333,V01398,"
					+ "V04001,V04002,V04003,V04004,V04005,V01330,V01332,V04320_041, D_SOURCE_ID) VALUES("
					+ "?, ?, ?, ?, ?, ?,"
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?)";
			if(connection != null){
				connection.setAutoCommit(false);
				// 创建PreparedStatement
				pStatement = connection.prepareStatement(sql); 
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				TyphKey typhKey = null;
				for(int i = 0; i < typhs.size(); i++){
					typhKey = typhs.get(i).getTyphKey();
					Date oTime = typhKey.getObservationTime();
					int ii = 1;
					String primkey = sdf.format(oTime)+"_"+typhKey.getTyphName()+"_"+typhKey.getReportCenter()+"_"+typhKey.getProductType();
					pStatement.setString(ii++, primkey);//记录标识
					pStatement.setString(ii++,sod_code);//资料标识
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//入库时间
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));//收到时间
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间
					pStatement.setTimestamp(ii++, new Timestamp(oTime.getTime()));//资料时间
					//"V_BBB,V04001_02,V04002_02,V04003_02,V04004_02,V_CCCC,V01035,V_TYPH_NAME,V01333,V01398,"
					pStatement.setString(ii++, typhKey.getV_BBB());
					pStatement.setInt(ii++, oTime.getYear() + 1900);
					pStatement.setInt(ii++, oTime.getMonth() + 1);
					pStatement.setInt(ii++, oTime.getDate());
					pStatement.setInt(ii++, oTime.getHours());
					pStatement.setString(ii++, typhKey.getReportCenter());
					pStatement.setInt(ii++, typhKey.getReportCenterCode());
					pStatement.setString(ii++, typhKey.getTyphName());
					pStatement.setString(ii++, typhKey.getTyphLevel());
					pStatement.setInt(ii++, typhKey.getProductType());
					//+ "V04001,V04002,V04003,V04004,V04005,V01330,V01332,V04320_041)
					pStatement.setInt(ii++, oTime.getYear() + 1900);
					pStatement.setInt(ii++, oTime.getMonth() + 1);
					pStatement.setInt(ii++, oTime.getDate());
					pStatement.setInt(ii++, oTime.getHours());
					pStatement.setInt(ii++, oTime.getMinutes());
					pStatement.setInt(ii++, typhKey.getInternalCode());
					pStatement.setInt(ii++, typhKey.getInternationalCode());
					pStatement.setInt(ii++, typhKey.getNumOfForecastEfficiency());
					pStatement.setString(ii++, cts_code);
					// 批量提交
					pStatement.addBatch();		
				}
				try {
					// 执行批量			
					pStatement.executeBatch();
					// 手动提交			
					//connection.commit();
					loggerBuffer.append(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
					loggerBuffer.append(" INSERT SUCCESS COUNT ：" + typhs.size() + "\n");
					return DataBaseAction.SUCCESS;
				} catch (Exception e) {
					pStatement.clearParameters();
					pStatement.clearBatch();			
					loggerBuffer.append(" Batch ERROR : " + sdf.format(new Date()) + "\n");
					return DataBaseAction.BATCH_ERROR;
				}	
			}
			else {
				loggerBuffer.append("\n Database connection error!");
				return DataBaseAction.CONNECTION_ERROR;
			}	
		}catch (SQLException e) {		
			loggerBuffer.append("\n Database connection error!"+e.getMessage());
			return DataBaseAction.CONNECTION_ERROR;
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
	 * @Title: insertOneLine_key_db   
	 * @Description: TODO(单条插入键表数据)   
	 * @param typhKey 插入对象
	 * @param connection 数据库连接
	 * @param recv_time 资料接收时间
	 * @return DataBaseAction   入库状态   
	 * @throws：
	 */
	private static DataBaseAction insertOneLine_key_db(TyphKey typhKey, java.sql.Connection connection, Date recv_time, String fileN, StringBuffer loggerBuffer){
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.keyTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,"
				+ "V_BBB,V04001_02,V04002_02,V04003_02,V04004_02,V_CCCC,V01035,V_TYPH_NAME,V01333,V01398,"
				+ "V04001,V04002,V04003,V04004,V04005,V01330,V01332,V04320_041,D_SOURCE_ID) VALUES("
				+ "?, ?, ?, ?, ?, ?,"
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?)";
		if(connection != null){
			try {
				connection.setAutoCommit(false);
				pStatement = new LoggableStatement(connection, sql); 
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				Date oTime = typhKey.getObservationTime();
			    int ii = 1;
			    String primkey = sdf.format(oTime)+"_"+typhKey.getTyphName()+"_"+typhKey.getReportCenter()+"_"+typhKey.getProductType();
				pStatement.setString(ii++, primkey);//记录标识
				pStatement.setString(ii++,sod_code);//资料标识
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//入库时间
				pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));//收到时间
				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间
				pStatement.setTimestamp(ii++, new Timestamp(oTime.getTime()));//资料时间
				//"V_BBB,V04001_02,V04002_02,V04003_02,V04004_02,V_CCCC,V01035,V_TYPH_NAME,V01333,V01398,"
				pStatement.setString(ii++, typhKey.getV_BBB());
				pStatement.setInt(ii++, oTime.getYear() + 1900);
				pStatement.setInt(ii++, oTime.getMonth() + 1);
				pStatement.setInt(ii++, oTime.getDate());
				pStatement.setInt(ii++, oTime.getHours());
				pStatement.setString(ii++, typhKey.getReportCenter());
				pStatement.setInt(ii++, typhKey.getReportCenterCode());
				pStatement.setString(ii++, typhKey.getTyphName());
				pStatement.setString(ii++, typhKey.getTyphLevel());
				pStatement.setInt(ii++, typhKey.getProductType());
				//+ "V04001,V04002,V04003,V04004,V04005,V01330,V01332,V04320_041)
				pStatement.setInt(ii++, oTime.getYear() + 1900);
				pStatement.setInt(ii++, oTime.getMonth() + 1);
				pStatement.setInt(ii++, oTime.getDate());
				pStatement.setInt(ii++, oTime.getHours());
				pStatement.setInt(ii++, oTime.getMinutes());
				pStatement.setInt(ii++, typhKey.getInternalCode());
				pStatement.setInt(ii++, typhKey.getInternationalCode());
				pStatement.setInt(ii++, typhKey.getNumOfForecastEfficiency());
				pStatement.setString(ii++, cts_code);
				
				try {
					pStatement.execute();
				//	connection.commit();
					return DataBaseAction.SUCCESS;
				} catch (SQLException e) {
					loggerBuffer.append("\n File name: "+fileN+
							"\n " + typhKey.getTyphName() + " " + sdf.format(typhKey.getObservationTime())
							+"\n execute sql error: "+((LoggableStatement)pStatement).getQueryString()+"\n "+e.getMessage());
					return DataBaseAction.INSERT_ERROR;
				}
			}catch (SQLException e) {
				loggerBuffer.append("\n Create Statement error: "+e.getMessage());
				return DataBaseAction.INSERT_ERROR;
			}finally {
				if(pStatement != null) {
					try {
						pStatement.close();
					} catch (SQLException e) {
						e.printStackTrace();
						loggerBuffer.append("\n Close Statement error: "+e.getMessage());
						
					}
				}	
			}
		}else {
			loggerBuffer.append("\n Database connection error!");
			return DataBaseAction.CONNECTION_ERROR;
		}	
	}
	/**
	 * 
	 * @Title: insertEle  
	 * @Description: TODO(台风实况与预测报文要素入库（批量）)   
	 * @param  typhs  入库对象集合
	 * @param  connection 数据库连接
	 * @param  recv_time      资料接收时间
	 * @return  DataBaseAction 数据入库状态
	 * @throws:
	 */
	private static DataBaseAction insertEle(List<Typh> typhs, java.sql.Connection connection, Date recv_time, String fileN,StringBuffer loggerBuffer) {
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_RECORD_ID,D_DATETIME,D_UPDATE_TIME,"
				+ "V04320,V05001,V06001,V10004,V11041,V11320,V11410_07_01,V11411_07_01,V11410_07_02,V11411_07_02,"
				+ "V11410_07_03,V11411_07_03,V11410_07_04,V11411_07_04,V11410_10_01,V11411_10_01,"
				+ "V11410_10_02,V11411_10_02,V11410_10_03,V11411_10_03,V11410_10_04,V11411_10_04,"
				+ "V11410_12_01,V11411_12_01,V11411_12_02,V11410_12_02,V11410_12_03,V11411_12_03,"
				+ "V11410_12_04,V11411_12_04,V19301,V19302,V19303,V19304,V_BBB,D_SOURCE_ID) " //,V_TYPH_NAME
			+ "VALUES (?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, ?, ?) " ;//, ?
			
		try {	
			if(connection != null){
				connection.setAutoCommit(false);
				pStatement = connection.prepareStatement(sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");	
				TyphKey typhKey = null;
				List<TyphEle> typhEles = null;
				TyphEle typhEle = null;
				Typh typh = null;
				Date oTime = null;
				int sz = listDi.size();
				int num = 0;
				for(int idx = 0; idx < typhs.size(); idx ++){
					typh = typhs.get(idx);
					typhKey = typh.getTyphKey();
					typhEles = typh.getTyphEles();
					for(int i = 0; i < typhEles.size(); i ++){
						num += 1;
						oTime = typhKey.getObservationTime();
						typhEle = typhEles.get(i);
//						String lat = String.valueOf((int)(typhEle.getLatitude() * 1e6));
//						String lon = String.valueOf((int)(typhEle.getLongtitude() * 1e6));
//						lat = lat.replaceAll("-", "0");
//						lon = lon.replaceAll("-", "0");
						int ii = 1;
						// 设置DI信息
						StatDi di = new StatDi();					
						di.setFILE_NAME_O(fileN);
						di.setDATA_TYPE(sod_code);
						di.setDATA_TYPE_1(cts_code);
						di.setTT(typhKey.getV_TT());			
						di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
						di.setPROCESS_START_TIME(TimeUtil.getSysTime());
						di.setFILE_NAME_N(fileN);
						di.setBUSINESS_STATE("1"); //1成功，0失败
						di.setPROCESS_STATE("1");  //1成功，0失败	
						//pStatement.setString(ii++, sdf.format(oTime)+"_"+typhKey.getTyphName()+"_"+typhKey.getReportCenter()+"_"+typhKey.getProductType()+"_"+typhEle.getForecastEfficiency());
						pStatement.setString(ii++, sdf.format(oTime)+"_"+typhKey.getTyphName()+"_"+typhKey.getReportCenter()+"_"+typhKey.getProductType());
						
						pStatement.setTimestamp(ii++, new Timestamp(oTime.getTime()));
						pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						// "V04320,V05001,V06001,V10004,V11041,V11320,V11410_07_01,V11411_07_01,V11410_07_02,V11411_07_02,"
						pStatement.setInt(ii++, typhEle.getForecastEfficiency());  // 预报时效
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getLatitude())));
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getLongtitude())));
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getCenterPressure())));
						pStatement.setBigDecimal(ii++,new BigDecimal(typhEle.getGustSpeed()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 中心阵风
						pStatement.setBigDecimal(ii++,new BigDecimal(typhEle.getMaxSustainedWind()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 中心附近最大阵风风速
						pStatement.setBigDecimal(ii++,new BigDecimal(typhEle.getAzimuthOfWindBeyondL7_01()).setScale(4, BigDecimal.ROUND_HALF_UP));
						pStatement.setBigDecimal(ii++,new BigDecimal(typhEle.getWindCircleRadiusL7_01()).setScale(4, BigDecimal.ROUND_HALF_UP));
						pStatement.setBigDecimal(ii++,new BigDecimal(typhEle.getAzimuthOfWindBeyondL7_02()).setScale(4, BigDecimal.ROUND_HALF_UP));
						pStatement.setBigDecimal(ii++,new BigDecimal(typhEle.getWindCircleRadiusL7_02()).setScale(4, BigDecimal.ROUND_HALF_UP));
						// "V11410_07_03,V11411_07_03,V11410_07_04,V11411_07_04,V11410_10_01,V11411_10_01,"
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getAzimuthOfWindBeyondL7_03())));
						pStatement.setBigDecimal(ii++,new BigDecimal(typhEle.getWindCircleRadiusL7_03()).setScale(4, BigDecimal.ROUND_HALF_UP));
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getAzimuthOfWindBeyondL7_04())));
						pStatement.setBigDecimal(ii++,new BigDecimal(typhEle.getWindCircleRadiusL7_04()).setScale(4, BigDecimal.ROUND_HALF_UP));
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getAzimuthOfWindBeyondL10_01())));
						pStatement.setBigDecimal(ii++,new BigDecimal(typhEle.getWindCircleRadiusL10_01()).setScale(4, BigDecimal.ROUND_HALF_UP));
						// "V11410_10_02,V11411_10_02,V11410_10_03,V11411_10_03,V11410_10_04,V11411_10_04,"
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getAzimuthOfWindBeyondL10_02())));
						pStatement.setBigDecimal(ii++,new BigDecimal(typhEle.getWindCircleRadiusL10_02()).setScale(4, BigDecimal.ROUND_HALF_UP));
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getAzimuthOfWindBeyondL10_03())));
						pStatement.setBigDecimal(ii++,new BigDecimal(typhEle.getWindCircleRadiusL10_03()).setScale(4, BigDecimal.ROUND_HALF_UP));
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getAzimuthOfWindBeyondL10_04())));
						pStatement.setBigDecimal(ii++,new BigDecimal(typhEle.getWindCircleRadiusL10_04()).setScale(4, BigDecimal.ROUND_HALF_UP));
						// "V11410_12_01,V11411_12_01,V11411_12_02,V11410_12_02,V11410_12_03,V11411_12_03,"
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getAzimuthOfWindBeyondL12_01())));
						pStatement.setBigDecimal(ii++,new BigDecimal(typhEle.getWindCircleRadiusL12_01()).setScale(4, BigDecimal.ROUND_HALF_UP));
						pStatement.setBigDecimal(ii++,new BigDecimal(typhEle.getWindCircleRadiusL12_02()).setScale(4, BigDecimal.ROUND_HALF_UP));
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getAzimuthOfWindBeyondL12_02())));
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getAzimuthOfWindBeyondL12_03())));
						pStatement.setBigDecimal(ii++,new BigDecimal(typhEle.getWindCircleRadiusL12_03()).setScale(4, BigDecimal.ROUND_HALF_UP));
						// "V11410_12_04,V11411_12_04,V19301,V19302,V19303,V19304,V_BBB,D_DATETIME) "
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getAzimuthOfWindBeyondL12_04())));
						pStatement.setBigDecimal(ii++,new BigDecimal(typhEle.getWindCircleRadiusL12_04()).setScale(4, BigDecimal.ROUND_HALF_UP));
						pStatement.setBigDecimal(ii++,new BigDecimal(typhEle.getMovingDir()).setScale(4, BigDecimal.ROUND_HALF_UP));
						pStatement.setBigDecimal(ii++,new BigDecimal(typhEle.getMovingSpeed()).setScale(4, BigDecimal.ROUND_HALF_UP));
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getTyphIntensity())));
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getTrend())));
						pStatement.setString(ii++, typhKey.getV_BBB());
						pStatement.setString(ii++, cts_code);
						
						di.setIIiii(typhKey.getTyphName());
						di.setDATA_TIME(TimeUtil.date2String(oTime, "yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setLATITUDE(String.valueOf(typhEle.getLatitude()));
						di.setLONGTITUDE(String.valueOf(typhEle.getLongtitude()));
						
						di.setSEND("BFDB");
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(new File(fileN).length()));
						di.setDATA_UPDATE_FLAG(typh.getTyphKey().getV_BBB());
						di.setHEIGHT("999999");
						
						pStatement.addBatch();
						listDi.add(di);
					}
				} 
				try {
					pStatement.executeBatch();
					connection.commit();//新建的数据库连接
					return DataBaseAction.SUCCESS;
				} catch (SQLException e) {
					connection.rollback();//发生错误 ，传进来的连接回滚
					pStatement.clearParameters();
					pStatement.clearBatch();
					for(int c = 0; c < num; c ++)
						listDi.remove(listDi.get(sz));
					loggerBuffer.append("\n Element table batch commit failed: "+fileN );
					return DataBaseAction.BATCH_ERROR;
				}
			}else {
				loggerBuffer.append("\n  Database connection error!");
				return DataBaseAction.CONNECTION_ERROR;
			}
		} catch (Exception e) {
			loggerBuffer.append("\n Create PreparedStatement error: " + e.getMessage());
			return DataBaseAction.CONNECTION_ERROR;
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
	 * @Title: insertOneLine_Ele_db   
	 * @Description: TODO(台风实况与预报要素表入库（单条）)   
	 * @param tyhEle 待入库对象
	 * @param connection 数据库连接
	 * @param recv_time 资料接收时间
	 * @return   
	 * @throws：
	 */
	private static void insertOneLine_Ele_db(Typh typh, java.sql.Connection connection, Date recv_time, String fileN, StringBuffer loggerBuffer){
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_RECORD_ID,D_DATETIME,D_UPDATE_TIME,"
				+ "V04320,V05001,V06001,V10004,V11041,V11320,V11410_07_01,V11411_07_01,V11410_07_02,V11411_07_02,"
				+ "V11410_07_03,V11411_07_03,V11410_07_04,V11411_07_04,V11410_10_01,V11411_10_01,"
				+ "V11410_10_02,V11411_10_02,V11410_10_03,V11411_10_03,V11410_10_04,V11411_10_04,"
				+ "V11410_12_01,V11411_12_01,V11411_12_02,V11410_12_02,V11410_12_03,V11411_12_03,"
				+ "V11410_12_04,V11411_12_04,V19301,V19302,V19303,V19304,V_BBB, D_SOURCE_ID) " //,V_TYPH_NAME
			+ "VALUES (?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, ?, ?) " ; //, ?
		if(connection != null){
			try {
				connection.setAutoCommit(false);
				pStatement = connection.prepareStatement(sql); 
//				if(StartConfig.getDatabaseType() == 1) {
//					pStatement.execute("select last_txc_xid()");
//				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				TyphEle typhEle = null;
				TyphKey typhKey = typh.getTyphKey();
				List<TyphEle> typhEles = typh.getTyphEles();
				Date oTime = typhKey.getObservationTime();
				int sz = listDi.size();
				int num = 0;
				for(int i = 0; i < typhEles.size(); i ++){
					typhEle = typhEles.get(i);
//					String lat = String.valueOf((int)(typhEle.getLatitude() * 1e6));
//					String lon = String.valueOf((int)(typhEle.getLongtitude() * 1e6));
//					lat = lat.replaceAll("-", "0");
//					lon = lon.replaceAll("-", "0");
					int ii = 1;
					StatDi di = new StatDi();				
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(sod_code);
					di.setDATA_TYPE_1(cts_code);
					di.setTT(typhKey.getV_TT());			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1");  //1成功，0失败
					//pStatement.setString(ii++, sdf.format(oTime)+"_"+typhKey.getTyphName()+"_"+typhKey.getReportCenter()+"_"+typhKey.getProductType()+"_"+typhEle.getForecastEfficiency());
					pStatement.setString(ii++, sdf.format(oTime)+"_"+typhKey.getTyphName()+"_"+typhKey.getReportCenter()+"_"+typhKey.getProductType());
					pStatement.setTimestamp(ii++, new Timestamp(oTime.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					// "V04320,V05001,V06001,V10004,V11041,V11320,V11410_07_01,V11411_07_01,V11410_07_02,V11411_07_02,"
					pStatement.setInt(ii++, typhEle.getForecastEfficiency());  // 预报时效
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getLatitude())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getLongtitude())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getCenterPressure())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getGustSpeed()))); // 中心阵风
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getMaxSustainedWind()))); // 中心附近最大阵风风速
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getAzimuthOfWindBeyondL7_01())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getWindCircleRadiusL7_01())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getAzimuthOfWindBeyondL7_02())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getWindCircleRadiusL7_02())));
					// "V11410_07_03,V11411_07_03,V11410_07_04,V11411_07_04,V11410_10_01,V11411_10_01,"
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getAzimuthOfWindBeyondL7_03())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getWindCircleRadiusL7_03())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getAzimuthOfWindBeyondL7_04())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getWindCircleRadiusL7_04())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getAzimuthOfWindBeyondL10_01())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getWindCircleRadiusL10_01())));
					// "V11410_10_02,V11411_10_02,V11410_10_03,V11411_10_03,V11410_10_04,V11411_10_04,"
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getAzimuthOfWindBeyondL10_02())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getWindCircleRadiusL10_02())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getAzimuthOfWindBeyondL10_03())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getWindCircleRadiusL10_03())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getAzimuthOfWindBeyondL10_04())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getWindCircleRadiusL10_04())));
					// "V11410_12_01,V11411_12_01,V11411_12_02,V11410_12_02,V11410_12_03,V11411_12_03,"
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getAzimuthOfWindBeyondL12_01())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getWindCircleRadiusL12_01())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getWindCircleRadiusL12_02())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getAzimuthOfWindBeyondL12_02())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getAzimuthOfWindBeyondL12_03())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getWindCircleRadiusL12_03())));
					// "V11410_12_04,V11411_12_04,V19301,V19302,V19303,V19304,V_BBB) "
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getAzimuthOfWindBeyondL12_04())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getWindCircleRadiusL12_04())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getMovingDir())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getMovingSpeed())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getTyphIntensity())));
					pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(typhEle.getTrend())));
					pStatement.setString(ii++, typhKey.getV_BBB());
					pStatement.setString(ii++, cts_code);
					
//					pStatement.setString(ii++, typhKey.getTyphName());
					di.setIIiii(typhKey.getTyphName());
					di.setDATA_TIME(TimeUtil.date2String(oTime, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLATITUDE(String.valueOf(typhEle.getLatitude()));
					di.setLONGTITUDE(String.valueOf(typhEle.getLongtitude()));
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(fileN).length()));
					di.setDATA_UPDATE_FLAG(typh.getTyphKey().getV_BBB());
					di.setHEIGHT("999999");
					
					pStatement.addBatch();
					listDi.add(di);
					num ++;
				}
				try {
					pStatement.executeBatch();
					connection.commit();
				} catch (Exception e) {
					connection.rollback();
					for(int c = 0; c < num; c ++)
						listDi.get(sz + c).setPROCESS_STATE("0");//1成功，0失败
					loggerBuffer.append("\n File name: "+fileN+
							"\n " + typhKey.getTyphName() + " " + sdf.format(typhKey.getObservationTime()));
				}
			}catch (Exception e) {
				loggerBuffer.append("\n Create PreparedStatement error: " +e.getMessage());
			}
			finally{
				if(pStatement != null){
					try{
						pStatement.close();
					}catch (Exception e) {
						loggerBuffer.append("close statement error!");
					}
				}
			}
		}else {
			loggerBuffer.append("\n Database connection error!");
		}	
	}
	
	/**
	 * 
	 * @Title: reportInfoToDb   
	 * @Description: TODO(报文信息入库)   
	 * @param  reportInfos 报文列表集合
	 * @param  connection  数据库连接
	 * @param  recv_time   资料时间
	 * @return: void      
	 * @throws:
	 */
	@SuppressWarnings({ "rawtypes" })
	public static void reportInfoToDb( List<ReportInfo> reportInfos, java.sql.Connection connection, Date recv_time, StringBuffer loggerBuffer, String fileN) {
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO  "+StartConfig.reportTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,"
				+ "V_BBB,V_CCCC,V04001_01,V04002_01,V04003_01,V04004_01,V04005_01,V_TT,V_AA,V_II,"
				+ "V_LEN,V_BULLETIN_T, D_source_id) VALUES"
				+ "(?,?,?,?,?,?,"
				+ " ?,?,?,?,?,?,?,?,?,?,"
				+ " ?,?,?)";
		try {
			pStatement = new LoggableStatement(connection, sql);
//			if(StartConfig.getDatabaseType() == 1) {
//				pStatement.execute("select last_txc_xid()");
//			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String V_TT= "";
			Typh typh = null;
			TyphKey typhKey = null;
			Date oTime = null;
			String primkey = null;
			for (int i = 0; i < reportInfos.size(); i++) {
				StatDi di = new StatDi();				
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(StartConfig.reportSodCode());
				di.setDATA_TYPE_1(cts_code);
							
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //1成功，0失败
				di.setPROCESS_STATE("1");  //1成功，0失败
				try {
					typh = (Typh) reportInfos.get(i).getT();
					typhKey = typh.getTyphKey();
					V_TT = typhKey.getV_TT();
				
					di.setTT(typhKey.getV_TT());
					
					oTime = typhKey.getObservationTime();
					primkey = sdf.format(oTime)+"_"+V_TT+"_"+typhKey.getTyphName()+"_"+typhKey.getReportCenter()+"_"+typhKey.getProductType() + "_" + typhKey.getV_BBB();
					int ii = 1;
					pStatement.setString(ii++, primkey);
					pStatement.setString(ii++, sod_report_code);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(oTime.getTime()));
					// "V_BBB,V_CCCC,V04001_01,V04002_01,V04003_01,V04004_01,V04005_01,V_TT,V_AA,V_II,"
					pStatement.setString(ii++, typhKey.getV_BBB());
					pStatement.setString(ii++, typhKey.getReportCenter());  // 编报中心
					pStatement.setInt(ii++, oTime.getYear() + 1900);
					pStatement.setInt(ii++, oTime.getMonth() + 1);
					pStatement.setInt(ii++, oTime.getDate());
					pStatement.setInt(ii++, oTime.getHours());
					pStatement.setInt(ii++, oTime.getMinutes());
					pStatement.setString(ii++, V_TT);  // 资料类型
					pStatement.setString(ii++, typhKey.getV_AA());
					pStatement.setString(ii++, typhKey.getV_II());
					// "V_LEN,V_BULLETIN_T) 
					pStatement.setInt(ii++, reportInfos.get(i).getReport().length());
					pStatement.setString(ii++, reportInfos.get(i).getReport());
					pStatement.setString(ii++, cts_code);
					
					di.setIIiii(typhKey.getTyphName());
					di.setDATA_TIME(TimeUtil.date2String(oTime, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					if(typh.getTyphEles() != null && typh.getTyphEles().size() > 0){
						di.setLATITUDE(String.valueOf(typh.getTyphEles().get(0).getLatitude()));
						di.setLONGTITUDE(String.valueOf(typh.getTyphEles().get(0).getLongtitude()));
					}else{
						di.setLATITUDE("999999");
						di.setLONGTITUDE("999999");
					}
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(fileN).length()));
					di.setDATA_UPDATE_FLAG(typh.getTyphKey().getV_BBB());
					listDi.add(di);
					
					pStatement.execute();
					if(connection.getAutoCommit() == false)
						connection.commit();
				} catch (Exception e) {
					loggerBuffer.append("sql error:" + ((LoggableStatement)pStatement).getQueryString());
					di.setPROCESS_STATE("0");
					continue;
				}
			}  
//			if(pStatement != null) {
//				try {
//					pStatement.close();
//				} catch (SQLException e) {
//					loggerBuffer.append("\n Close Statement error: "+e.getMessage());
//				}
//			}
		} catch (SQLException e) {
			loggerBuffer.append("Database connection error: " + e.getMessage());
			return;
		}
		finally{
			if(pStatement != null) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n Close Statement error: "+e.getMessage());
				}
			}
		}
	}	
	/**
	 * 
	 * @Title: updatekeyDB   
	 * @Description: TODO(台风实况与预报键表更新)   
	 * @param typhs 待更新的对象集合
	 * @param connection 数据库连接
	 * @param recv_time 资料接收时间  
	 * @throws：
	 */
	private static void updatekeyDB(List<Typh> typhs, java.sql.Connection connection, Date recv_time, String fileN, StringBuffer loggerBuffer){
		PreparedStatement Pstat = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		TyphKey typhKey = null;
		for(int i = 0; i < typhs.size(); i ++){
			String vbbbInDB = null;
			Typh typh = typhs.get(i);
			typhKey = typh.getTyphKey();
			//插入前，从DB中查找该条记录的状态，有、无、更正状态
			vbbbInDB = findVBBB_key(typh, connection, loggerBuffer);
			if(vbbbInDB == null){//该更正报之前数据库中没有该条记录，直接插入,给UPDATE_TIME赋值
				DataBaseAction updatekey = insertOneLine_key_db(typhKey, connection, recv_time, fileN,loggerBuffer);	
				if (updatekey == DataBaseAction.SUCCESS) {
					insertOneLine_Ele_db(typh, connection, recv_time, fileN,loggerBuffer);				
				}
			}else if(vbbbInDB.compareTo(typhKey.getV_BBB()) < 0) {
				String sql = "UPDATE "+StartConfig.keyTable()+" SET"
						+ " V_BBB=?,D_UPDATE_TIME=?,V01333=?,V01398=?,V01330=?,V01332=?,V04320_041=? "
						+ " WHERE  D_DATETIME=? and D_RECORD_ID=?";
				Date oTime = typhKey.getObservationTime();
				if(connection != null){
					try {
						connection.setAutoCommit(false);
						Pstat = connection.prepareStatement(sql);
//						if(StartConfig.getDatabaseType() == 1) {
//							Pstat.execute("select last_txc_xid()");
//						}
						int ii = 1;
						Pstat.setString(ii++, typhKey.getV_BBB());//更正标识
						Pstat.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间	
						Pstat.setString(ii++, typhKey.getTyphLevel());
						Pstat.setInt(ii++, typhKey.getProductType());
						Pstat.setInt(ii++, typhKey.getInternalCode());
						Pstat.setInt(ii++, typhKey.getInternationalCode());
						Pstat.setInt(ii++, typhKey.getNumOfForecastEfficiency());
						
						Pstat.setTimestamp(ii++, new Timestamp(oTime.getTime()));//资料时间
						Pstat.setString(ii++, sdf.format(oTime) + "_" + typhKey.getTyphName() + "_" + typhKey.getReportCenter() +"_"+typhKey.getProductType());
//						Pstat.setString(ii++, typhKey.getTyphName()); // 台风名
//						Pstat.setInt(ii++, oTime.getYear() + 1900);//资料观测年
//						Pstat.setInt(ii++, oTime.getMonth() + 1);//资料观测月
//						Pstat.setInt(ii++, oTime.getDate());//资料观测日
//						Pstat.setInt(ii++, oTime.getHours());
//						Pstat.setInt(ii++, oTime.getMinutes());
//						Pstat.setString(ii++, typhKey.getReportCenter());
//						Pstat.setInt(ii++, typhKey.getProductType());
						
						try{
							Pstat.executeUpdate();	
						//	connection.commit();
							updateEle(typh, recv_time, connection,fileN, loggerBuffer);
						}
						catch(SQLException e){							
							loggerBuffer.append("\n File name: "+fileN+
									"\n " + typhKey.getTyphName() + " " + sdf.format(oTime)
									+"\n execute sql error: "+((LoggableStatement)Pstat).getQueryString()+"\n "+e.getMessage());
						}				
					}catch (SQLException e) {
						loggerBuffer.append("\n Create Statement error: "+ e.getMessage());
					}finally {
						try {
							if(Pstat != null)
								Pstat.close();
						} catch (SQLException e) {
							loggerBuffer.append("\n Close Statement error: "+ e.getMessage());
						}	
					}
				}else
					loggerBuffer.append("\n Database connection error!");		
			} // end else if	
			else{ // 数据库中有该条记录，且V_BBB的值晚于当前处理文件的更正标识的值
			// 不进行插入或更新	
			}
		} 
	}
	/**
	 * @Title: deleteEle   
	 * @Description: TODO(更正要素表时，先删除旧的)   
	 * @param typh
	 * @param recv_time
	 * @param connection void      
	 * @throws：
	 */
	private static void deleteEle(Typh typh, Date recv_time, java.sql.Connection connection, String fileN, StringBuffer loggerBuffer){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		PreparedStatement pStmt = null;
		String sql = "Delete from "+StartConfig.valueTable()+" where D_DATETIME = ? and D_RECORD_ID=?";
		if(connection != null){
			try{
				connection.setAutoCommit(false);
				pStmt = connection.prepareStatement(sql);
				TyphKey typhKey = typh.getTyphKey();
				Date oTime = typhKey.getObservationTime();
				String primkey = sdf.format(oTime)+"_"+typhKey.getTyphName()+"_"+typhKey.getReportCenter()+"_"+typhKey.getProductType();
				
				pStmt.setTimestamp(1, new Timestamp(oTime.getTime()));//资料时间
				pStmt.setString(2, primkey);
				pStmt.executeUpdate();
			}catch(SQLException e){	
				loggerBuffer.append("\n File name: "+ fileN +
						"\n " + typh.getTyphKey().getTyphName() + " " + sdf.format(typh.getTyphKey().getObservationTime())
						+"\n execute sql error: "+"\n "+e.getMessage());
				e.printStackTrace();
			}
			finally {
				try {
					if(pStmt != null)
						pStmt.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n Close Statement error: "+ e.getMessage());
				}	
			}
		}else
			loggerBuffer.append("\n Database connection error!");
	}
	/**
	 * 
	 * @Title: updateEle   
	 * @Description: TODO(台风实况与预报要素表更新)   
	 * @param typh 待更新对象
	 * @param recv_time 资料接收时间
	 * @param connection 数据库连接      
	 * @throws：
	 */
	private static void updateEle(Typh typh, Date recv_time, java.sql.Connection connection, String fileN, StringBuffer loggerBuffer) {
		if(connection != null){
			deleteEle(typh, recv_time, connection,fileN,loggerBuffer);
			insertOneLine_Ele_db(typh, connection, recv_time,fileN,loggerBuffer);
		}
		else
			loggerBuffer.append("\n Database connction error!");	
	}

/**
 * 
 * @Title: findVBBB_key   
 * @Description: TODO(台风实况预报键表更正标识查找)   
 * @param typh 待查找对象
 * @param connection 数据库连接
 * @return String      
 * @throws：
 */
	private static String findVBBB_key(Typh typh, java.sql.Connection connection,StringBuffer loggerBuffer) {
		PreparedStatement Pstmt = null;
		ResultSet resultSet = null;
		String rntString = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String sql ="SELECT V_BBB FROM "+StartConfig.keyTable()+" "			
				+ " WHERE D_DATETIME=? and D_RECORD_ID= ?";
		try{	
			
//			if(StartConfig.getDatabaseType() == 1) {
//				Pstmt.execute("select last_txc_xid()");
//			}
			TyphKey typhKey = typh.getTyphKey();
			Date oTime = typhKey.getObservationTime();
			if(connection != null){
				Pstmt = connection.prepareStatement(sql);
				int ii = 1;		
				Pstmt.setTimestamp(ii++, new Timestamp(oTime.getTime()));//资料时间
				Pstmt.setString(ii++, sdf.format(oTime) + "_" + typhKey.getTyphName() + "_" + typhKey.getReportCenter() +"_"+typhKey.getProductType());
//				Pstmt.setString(ii++, typhKey.getTyphName());
//				Pstmt.setInt(ii++, oTime.getYear() + 1900);//资料观测年
//				Pstmt.setInt(ii++, oTime.getMonth() + 1);//资料观测月
//				Pstmt.setInt(ii++, oTime.getDate());//资料观测日
//				Pstmt.setInt(ii++, oTime.getHours());//资料观测时
//				Pstmt.setInt(ii++, oTime.getMinutes()); 
//				Pstmt.setString(ii++, typhKey.getReportCenter());
//				Pstmt.setInt(ii++, typhKey.getProductType());
				resultSet = Pstmt.executeQuery();
				if(resultSet.next())
					rntString= resultSet.getString(1);
			}
		}catch(SQLException e){
			loggerBuffer.append("\n Create PreparedStatement error: " + e.getMessage());
		}finally {
			if(Pstmt != null) {
				try {
					Pstmt.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n Close PreparedStatement error: " + e.getMessage());
				}
			}
			if(resultSet != null){
				try{
					resultSet.close();
				}
				catch (Exception e) {
					loggerBuffer.append("\n close resultSet error!");
				}
			}
		}
		return rntString;
	}

	
	public static void main(String[] args) {
		DecodeTyph dec=new DecodeTyph();
		String filepath = "D:\\HUAXIN\\对比\\服务类 对比验证\\（原数据删除-复测）2轮-M.0001.0001.R001-台风实况与预报报告（WTFX）-云平台与CIMISS对比验证-崔红元提交\\WT_MSG__XX010410.ABJ";
		File file = new File(filepath);
		ParseResult<Typh> parseResult = dec.DecodeFile(file);
		DataBaseAction action = null;
		Date recv_time = new Date();
		StringBuffer loggerBuffer = new StringBuffer();

		action = DbService.processSuccessReport(parseResult, filepath,recv_time,filepath,loggerBuffer);
		System.out.println("insertDBService over!");
	
	}
	
}
