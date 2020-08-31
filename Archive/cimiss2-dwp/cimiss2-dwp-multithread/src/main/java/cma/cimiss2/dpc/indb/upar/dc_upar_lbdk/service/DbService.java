package cma.cimiss2.dpc.indb.upar.dc_upar_lbdk.service;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Pattern;

import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.RestfulSendData;
import org.cimiss2.dwp.tools.SendType;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.EIConfig;
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.upar.UparLBand;
import cma.cimiss2.dpc.decoder.bean.upar.UparMinute;
import cma.cimiss2.dpc.decoder.bean.upar.UparSecond;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.decoder.upar.DecodeLbandUparDetection;

public class DbService {
	
	/** The list di. */
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	
	/** The di queues. */
	public static BlockingQueue<StatDi> diQueues;
	
	/** The Constant infoLogger. */
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	
	/** The v tt. */
	public static String V_TT = "L波段探空系统秒级探测资料";
//	public static String cts_code = StartConfig.ctsCode();
//	public static String sod_code = StartConfig.sodCode();	
/** The cts code. */
//	public static String cts_code = StartConfig.ctsCode();
	public static String cts_code = StartConfig.ctsCode();
//	String sodSs[] = StartConfig.sodCodes();
//	String second_sod_code = sodSs[0];
//	String minute_sod_code=sodSs[1];
//	public static String second_sod_code = "B.0009.0001.S001";
//	public static String minute_sod_code = "B.0010.0001.S001";
//	public static String cts_code = "B.0003.0001.R001";
	
	  
	  public static BlockingQueue<StatDi> getDiQueues() {
			return diQueues;
		}

		public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
			DbService.diQueues = diQueues;
		}
	
	/**
	 * Insert DB service.
	 *
	 * @param parseResult the parse result
	 * @param recv_time the recv time
	 * @param fileN the file N
	 * @param loggerBuffer the logger buffer
	 * @return the data base action
	 * @Title: processSuccessReport
	 * @Description:(报文解码入库函数) 
	 * @return: DataBaseAction
	 * @throws: 
	 */
  public static DataBaseAction insertDBService(ParseResult<UparLBand> parseResult, Date recv_time, String fileN, StringBuffer loggerBuffer, String filepath) {
	   
		String sodSs[] = StartConfig.sodCodes();
		String second_sod_code = sodSs[0];
		String minute_sod_code=sodSs[1];
		String para_sod_code=sodSs[2];
		java.sql.Connection connection1 = null;
		java.sql.Connection connection2 = null;
		
		try {
			connection1 = ConnectionPoolFactory.getInstance().getConnection("rdb");
			connection2 = ConnectionPoolFactory.getInstance().getConnection("rdb");
			try {
			List<UparLBand> uparLBands = parseResult.getData();	
//			String ktableName_second = "UPAR_WEA_CHN_MUL_NSEC_K_TAB";//秒级键表名
//			String ktableName_Minute = "UPAR_WEA_C_MUL_MIN_K_TAB";//分钟级键表名
//			String ParaTableName="UPAR_WEA_CHN_PARA_TAB";//基本参数表名
			
			String[] kTables =StartConfig.keyTable().trim().split(",");
			String ktableName_second =kTables[0];//秒级键表名
			String ktableName_Minute =kTables[1];//分钟级键表名
			String ParaTableName=kTables[2];//基本参数表名
			for(UparLBand uparLBand:uparLBands){
				double latitude=uparLBand.getLatitude();
				double longitude=uparLBand.getLongitude();
				String station=uparLBand.getStationNumberChina();
				double cal[]=calcuLatAndLon(station,latitude,longitude,fileN);
				latitude=cal[0];
				longitude=cal[1];
				//秒级 
				DataBaseAction insertEleDB = insertdb_second(uparLBand, connection1,recv_time, fileN,loggerBuffer, filepath,latitude,longitude);//插入要素表				
				if(insertEleDB==DataBaseAction.SUCCESS) { // 批量插入要素表成功				
					DataBaseAction insertKeyDB = insertKeyDB(uparLBand,ktableName_second,second_sod_code,connection1,recv_time,loggerBuffer,cts_code,fileN, filepath,latitude,longitude);//批量插入秒级键表

				}
				DataBaseAction insertParaTab = insertParaTab(uparLBand,ParaTableName,para_sod_code,connection1,recv_time,loggerBuffer,cts_code,fileN, filepath,latitude,longitude);//批量插入基本参数表
				
				//分钟级
//				insertEleDB = insertdb_minute(uparLBand, connection2,recv_time, fileN,loggerBuffer);//插入要素表
//				if(insertEleDB == DataBaseAction.SUCCESS) {
//					DataBaseAction insertKeyDB =insertKeyDB(uparLBand,ktableName_Minute,minute_sod_code,connection2,recv_time,loggerBuffer,cts_code,fileN);//批量插入分级键表
//								
//				}
			}											
			return DataBaseAction.SUCCESS;
		    }catch (Exception e) {
		    	loggerBuffer.append("\n database connection  error");
		    	return DataBaseAction.CONNECTION_ERROR;
		    }
		} catch (Exception e) {
			loggerBuffer.append("\n database connection  error");
			return DataBaseAction.CONNECTION_ERROR;
		}
		finally{
			System.out.println(listDi.size());
//			if(diQueues == null){
//				diQueues = new LinkedBlockingQueue<StatDi>();
//			}
			for (int j = 0; j < listDi.size(); j++) {
				diQueues.offer(listDi.get(j));
			}
			listDi.clear();
			if(connection1 != null) {
				try {
					connection1.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n database connection1  close error"+e.getMessage());
					
				}
			}
			if(connection2 != null) {
				try {
					connection2.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n database connection2  close error"+e.getMessage());
					
				}
			}
		}		
	}
  /**
	 * 
	 * @Title: insertdb_second   
	 * @Description: (L波段探空系统秒级探测资料入库)   
	 * @param:  uparLBand 待入库的对象集合
	 * @param:  connection 数据库连接
	 * @param:  recv_time 资料接收时间
	 * @param:  fileN
	 * @param:  loggerBuffer      
	 * @return: void      
	 * @throws
	 */
	private static DataBaseAction insertdb_second(UparLBand uparLBand, Connection connection, Date recv_time, String fileN,StringBuffer loggerBuffer, String filepath,double latitude,double longitude) {	
		String second_sod_code = StartConfig.sodCodes()[0];
		String ValueTables=StartConfig.valueTable();
		String secondValueTable=ValueTables.trim().split(",")[0];
		PreparedStatement pStatement = null;
		try {
		String sql = "INSERT INTO "+secondValueTable+"(D_RECORD_ID,d_ele_id,D_DATETIME,d_update_time,"
				+ "V01301,V04086,V05015,V06015,V12001,V07004,V13003,V07021,V05021,V06021,V11001,V11002,V10009,"
				+ "Q05015,Q06015,Q12001,Q07004,Q13003,Q07021,Q05021,Q06021,Q11001,Q11002,Q10009,D_SOURCE_ID"
				+ ")"
				+ "VALUES (?, ?, ?, ?,"
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?"
				+ ") ";
		if(connection != null) {			
				pStatement = new LoggableStatement(connection, sql);
				connection.setAutoCommit(false);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				List<String> sqls = new ArrayList<>();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				
					List<UparSecond> uparSeconds = uparLBand.getUparsecond();				
					
					for (int i = 0; i < uparSeconds.size(); i++) {
						
//						StatDi di = new StatDi();
//						di.setFILE_NAME_O(fileN);
//						di.setDATA_TYPE(second_sod_code);
//						di.setDATA_TYPE_1(cts_code);
//						di.setTT(V_TT);			
//						di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
//						di.setPROCESS_START_TIME(TimeUtil.getSysTime());
//						di.setFILE_NAME_N(fileN);
//						di.setBUSINESS_STATE("1"); //0成功，1失败
//						di.setPROCESS_STATE("1");  //0成功，1失败
						
						int stationNumberN = 999999;
						String stat = uparLBand.getStationNumberChina();//获取头文件的台站号
						if (Pattern.matches("\\d{5}", stat))
							stationNumberN = Integer.parseInt(stat);
						else
							stationNumberN = Integer.parseInt(StationCodeUtil.stringToAscii(stat));
						String lat = String.valueOf((int)(latitude * 1e6));//获取头文件的纬度
						String lon = String.valueOf((int)(longitude * 1e6));//获取头文件的经度
						lat = lat.replaceAll("-", "0");
						lon = lon.replaceAll("-", "0");
						
						Date date = new Date();
						date = uparLBand.getCastingUtcTime();//获取头文件的施放时间（世界时）
						

						UparSecond uparSecond = uparSeconds.get(i);//获取单条秒级数据
						int ii = 1;
						Date dataTime = new Date(date.getTime());
						dataTime.setMinutes(0);
						dataTime.setSeconds(0);
//						int hours = dataTime.getHours();
//						boolean flag=false;
//						if(hours<=2){
//							flag=true;
//						}
//						hours = ((hours + 23) % 24 / 3 + 1) * 3 % 24;
//						dataTime.setHours(hours);
//						if(hours == 0&&flag==false){
//							Calendar c = Calendar.getInstance();
//							c.setTime(dataTime);
//							c.add(c.DAY_OF_MONTH, 1);
//							dataTime = c.getTime();
//						}
						
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(dataTime);
						if(calendar.get(Calendar.HOUR_OF_DAY) % 6 == 5){
							calendar.add(Calendar.HOUR_OF_DAY, 1);
						}else if(calendar.get(Calendar.HOUR_OF_DAY) % 6 == 1){
							calendar.add(Calendar.HOUR_OF_DAY, -1);
						}else if(calendar.get(Calendar.HOUR_OF_DAY) % 6 == 2){
							calendar.add(Calendar.HOUR_OF_DAY, -2);
						}
						pStatement.setString(ii++, sdf.format(date.getTime())+"_"+sdf.format(calendar.getTime())+"_"+stat+"_"+lat+"_"+lon);//D_RECORD_ID	
						pStatement.setString(ii++, sdf.format(date.getTime())+"_"+sdf.format(calendar.getTime())+"_"+stat+"_"+lat+"_"+lon+"_"+uparSecond.getRelativeTime());//d_ele_id	
						
//						Date dataTime = new Date(date.getTime());
//						dataTime.setMinutes(0);
//						dataTime.setSeconds(0);
//						int hours = dataTime.getHours();
//						hours = ((hours + 23) % 24 / 3 + 1) * 3 % 24;
//						dataTime.setHours(hours);
//						if(hours == 0){
//							Calendar c = Calendar.getInstance();
//							c.setTime(dataTime);
//							c.add(c.DAY_OF_MONTH, 1);
//							dataTime = c.getTime();
//						}
						
						pStatement.setTimestamp(ii++, new Timestamp(new Date(calendar.getTime().getTime()).getTime()));//D_DATETIME 资料时间
//						pStatement.setTimestamp(ii++, new Timestamp(uparLBand.get(idx).getCastingUtcTime().getTime()));//D_DATETIME 资料时间
						pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间d_update_time
						pStatement.setString(ii++, stat);//区站号V01301
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparSecond.getRelativeTime())));//时间差V04086
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparSecond.getLatitudeeDev())));//纬度偏差V05015
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparSecond.getLongitudeDev())));//经度偏差V06015
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparSecond.getTemperature())));//温度V12001
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparSecond.getPressure())));//气压V07004
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparSecond.getRelativeHumidity())));//相对湿度V13003
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparSecond.getLookUpAngle())));//仰角V07021
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparSecond.getBearing())));//方位角V05021
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparSecond.getDistance())));//距离V06021
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparSecond.getWindDir())));//风向V11001
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparSecond.getWindSpeed())));//风速V11002
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparSecond.getHeight())));//位势高度V10009	
						pStatement.setInt(ii++, 9);//纬度偏差质量标志Q05015
						pStatement.setInt(ii++, 9);//经度偏差质量标志Q06015
						pStatement.setInt(ii++, 9);//温度质量标志Q12001
						pStatement.setInt(ii++, 9);//气压（标准层）质量标志Q07004
						pStatement.setInt(ii++, 9);//相对湿度质量标志Q13003
						pStatement.setInt(ii++, 9);//仰角质量标志Q07021
						pStatement.setInt(ii++, 9);//方位角质量标志Q05021
						pStatement.setInt(ii++, 9);//距离质量标志Q06021
						pStatement.setInt(ii++, 9);//风向质量标志Q11001
						pStatement.setInt(ii++, 9);//风速质量标志Q11002
						pStatement.setInt(ii++, 9);//位势高度质量标志Q10009
						pStatement.setString(ii++, cts_code);//资料四级编码						
						//System.out.println(((LoggableStatement)pStatement).getQueryString());
						
//						di.setIIiii(uparLBand.getStationNumberChina());
//						di.setDATA_TIME(TimeUtil.date2String(date, "yyyy-MM-dd HH:mm"));
//						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
//						di.setRECORD_TIME(TimeUtil.getSysTime());	
//						di.setLATITUDE(String.valueOf(latitude));
//						di.setLONGTITUDE(String.valueOf(longitude));
//						di.setHEIGHT(uparLBand.getHeightOfSationGroundAboveMeanSeaLevel().toString());
//						di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
//						di.setSEND("BFDB");
//						di.setSEND_PHYS("DRDS");
//						di.setDATA_UPDATE_FLAG(uparLBand.getCorrectSign());
						
						
						pStatement.addBatch();
						sqls.add(((LoggableStatement)pStatement).getQueryString()); // 批量入库冲突时，单条入库
//						listDi.add(di);
						System.out.println(((LoggableStatement)pStatement).getQueryString());
					}//end for uparLTempSeconds
					
								
				try{
					pStatement.executeBatch();
					connection.commit();
					loggerBuffer.append("\n"+fileN+" :BATCH INSERT UPAR_WEA_CHN_MUL_NSEC_TAB SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
//					loggerBuffer.append("BATCH INSERT UPAR_WEA_CHN_MUL_NSEC_TAB SUCCESS \n");					
					sqls.clear();
				    return DataBaseAction.SUCCESS;
				}catch(SQLException e){
					pStatement.clearParameters();
					pStatement.clearBatch();
					connection.rollback();
					loggerBuffer.append("\n"+fileN+" BATCH INSERT UPAR_WEA_CHN_MUL_NSEC_TAB ERROR : " + sdf.format(new Date()) + "\n");
					boolean f=execute_sql(sqls, connection,fileN,loggerBuffer); // 此中，入库失败的会将对应的DI的ProcessState置为1 // 1:失败，0：成功
				    if(f==true){ 
				    	return DataBaseAction.SUCCESS;
				    }else{
				    	return DataBaseAction.BATCH_ERROR;
				    }
				}
				//return DataBaseAction.SUCCESS; 
		}// if connection
		else {
			loggerBuffer.append("\n Database connection error!");
			return DataBaseAction.CONNECTION_ERROR;
		}
		
		} catch (Exception e) {
				e.printStackTrace();
				return DataBaseAction.INSERT_ERROR;
		}finally {
				try {
					if (pStatement != null)
						pStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
		}
				
	}
	/**
	 * 
	 * @Title: insertKeyDB   
	 * @Description: TODO(中国高空探测秒/分数据键表)   
	 * @param: @param uparLTemps
	 * @param: @param ktablename
	 * @param: @param connection
	 * @param: @param recv_time
	 * @param: @param loggerBuffer
	 * @param: @param cts_code2
	 * @param: @param fileN
	 * @param: @return      
	 * @return: DataBaseAction      
	 * @throws
	 */
	private static DataBaseAction insertKeyDB(UparLBand uparLBand,String ktablename,String sodcode,Connection connection, Date recv_time, StringBuffer loggerBuffer,String cts_code2, String fileN, String filepath,double latitude,double longitude) {
		int year,month,day,hour,minute,second;
		PreparedStatement pStatement = null;
		try {
			String sql = "INSERT INTO "+ktablename+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V_BBB,"
					+ "V04001_02,V04002_02,V04003_02,V04004_02,V01301,V01300,V05001,V06001,V07001,V04001,V04002,V04003,V04004,V04005,V04006,V31001,D_SOURCE_ID) VALUES("
					+"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
			if(connection != null) {
					pStatement = new LoggableStatement(connection, sql);
					connection.setAutoCommit(false);
					if(StartConfig.getDatabaseType() == 1) {
						pStatement.execute("select last_txc_xid()");
					}
//					List<String> sqls = new ArrayList<>();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
					Calendar calendar = Calendar.getInstance();//year,month,day,hour,minute,second					
						
						Date date = uparLBand.getCastingUtcTime();//获取施放时间（世界时）
						calendar.setTime(date);//Date类型转int
						year = calendar.get(Calendar.YEAR);//获取年
						month = calendar.get(Calendar.MONTH) + 1;//获取月
						day = calendar.get(Calendar.DAY_OF_MONTH);//获取日
						hour = calendar.get(Calendar.HOUR_OF_DAY);//获取日
						minute = calendar.get(Calendar.MINUTE);//获取分
						second = calendar.get(Calendar.SECOND);//获取秒
						StatDi di = new StatDi();
						di.setFILE_NAME_O(fileN);
						//di.setDATA_TYPE(sod_code);
						di.setDATA_TYPE(sodcode);
						di.setDATA_TYPE_1(cts_code);
						di.setTT(V_TT);			
						di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
						di.setPROCESS_START_TIME(TimeUtil.getSysTime());
						di.setFILE_NAME_N(fileN);
						di.setBUSINESS_STATE("1"); //0成功，1失败
						di.setPROCESS_STATE("1");  //0成功，1失败
						
						int stationNumberN = 999999;
						String stat = uparLBand.getStationNumberChina();//获取台站号
						if (Pattern.matches("\\d{5}", stat))
							stationNumberN = Integer.parseInt(stat);
						else
							stationNumberN = Integer.parseInt(StationCodeUtil.stringToAscii(stat));
						String lat = String.valueOf((int)(latitude * 1e6));
						String lon = String.valueOf((int)(longitude * 1e6));
						lat = lat.replaceAll("-", "0");
						lon = lon.replaceAll("-", "0");						
						di.setIIiii(uparLBand.getStationNumberChina());
//						di.setDATA_TIME(TimeUtil.date2String(date, "yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());	
						di.setLATITUDE(String.valueOf(latitude));
						di.setLONGTITUDE(String.valueOf(longitude));
						
						di.setSEND("BFDB");
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
						di.setDATA_UPDATE_FLAG(uparLBand.getCorrectSign());
						di.setHEIGHT(String.valueOf(uparLBand.getHeightOfSationGroundAboveMeanSeaLevel()));
						
						int ii=1;
						Date dataTime = new Date(date.getTime());
						dataTime.setMinutes(0);
						dataTime.setSeconds(0);
//						int hours = dataTime.getHours();
//						hours = ((hours + 23) % 24 / 3 + 1) * 3 % 24;
//						dataTime.setHours(hours);
//						if(hours == 0){
//							Calendar c = Calendar.getInstance();
//							c.setTime(dataTime);
//							c.add(c.DAY_OF_MONTH, 1);
//							dataTime = c.getTime();
//						}
						Calendar calendar2 = Calendar.getInstance();
						calendar2.setTime(dataTime);
						if(calendar2.get(Calendar.HOUR_OF_DAY) % 6 == 5){
							calendar2.add(Calendar.HOUR_OF_DAY, 1);
						}else if(calendar2.get(Calendar.HOUR_OF_DAY) % 6 == 1){
							calendar2.add(Calendar.HOUR_OF_DAY, -1);
						}else if(calendar2.get(Calendar.HOUR_OF_DAY) % 6 == 2){
							calendar2.add(Calendar.HOUR_OF_DAY, -2);
						}
						di.setDATA_TIME(TimeUtil.date2String(calendar2.getTime(), "yyyy-MM-dd HH:mm"));
						pStatement.setString(ii++, sdf.format(date.getTime())+"_"+sdf.format(calendar2.getTime())+"_"+stat+"_"+lat+"_"+lon);//记录标识D_RECORD_ID	
						pStatement.setString(ii++,sodcode);//资料标识				
						pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//入库时间
						pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));//收到时间
						pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间d_update_id
					
//						Date dataTime = new Date(date.getTime());
//						dataTime.setMinutes(0);
//						dataTime.setSeconds(0);
//						int hours = dataTime.getHours();
//						hours = ((hours + 23) % 24 / 3 + 1) * 3 % 24;
//						dataTime.setHours(hours);
//						if(hours == 0){
//							Calendar c = Calendar.getInstance();
//							c.setTime(dataTime);
//							c.add(c.DAY_OF_MONTH, 1);
//							dataTime = c.getTime();
//						}
						pStatement.setTimestamp(ii++, new Timestamp(new Date(calendar2.getTime().getTime()).getTime()));//资料时间D_DATETIME
						
//						pStatement.setTimestamp(ii++, new Timestamp(uparLBand.getCastingUtcTime().getTime()));//资料时间D_DATETIME
						pStatement.setString(ii++, uparLBand.getCorrectSign());//更正标识
						
						pStatement.setInt(ii++, calendar2.getTime().getYear()+1900);//资料年
						pStatement.setInt(ii++, calendar2.getTime().getMonth()+1);//资料月
						pStatement.setInt(ii++,calendar2.getTime().getDate());//资料日
						pStatement.setInt(ii++, calendar2.getTime().getHours());//资料时
						pStatement.setString(ii++, stat);//字符区站号V01301
						pStatement.setInt(ii++, stationNumberN);//数字区站号V01300
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(latitude)));//纬度
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(longitude)));//经度
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparLBand.getHeightOfSationGroundAboveMeanSeaLevel())));//测站海拔高度
						pStatement.setInt(ii++, year);//气球施放时间（年）
						pStatement.setInt(ii++, month);//气球施放时间（月）
						pStatement.setInt(ii++, day);//气球施放时间（日）
						pStatement.setInt(ii++, hour);//气球施放时间（时）
						pStatement.setInt(ii++, minute);//气球施放时间（分）
						pStatement.setInt(ii++, second);//气球施放时间（秒）
						
						if("UPAR_WEA_CHN_MUL_NSEC_K_TAB".equals(ktablename)){
							pStatement.setInt(ii++, uparLBand.getUparsecond().size());//秒数据层数
						}else if("UPAR_WEA_C_MUL_MIN_K_TAB".equals(ktablename)){
							pStatement.setInt(ii++, uparLBand.getUparminute().size());//分钟数据层数
						}
						pStatement.setString(ii++, cts_code);//资料四级编码

						System.out.println(((LoggableStatement)pStatement).getQueryString());

//						pStatement.addBatch();
//						sqls.add(((LoggableStatement)pStatement).getQueryString()); // 批量入库冲突时，单条入库
						listDi.add(di);	
						
					
					//return DataBaseAction.SUCCESS;
					try{
						pStatement.execute();
						connection.commit();
						loggerBuffer.append("\n filename:"+fileN+" INSERT "+ktablename+" SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
//						loggerBuffer.append("BATCH INSERT "+ktablename+"SUCCESS \n");
					    return DataBaseAction.SUCCESS;						
					}catch(SQLException e){
//						execute_sql(sqls, connection,fileN,loggerBuffer); // 此中，入库失败的会将对应的DI的ProcessState置为1 // 1:失败，0：成功
						loggerBuffer.append("\n filename:"+fileN+" INSERT "+ktablename+"ERROR : " +((LoggableStatement)pStatement).getQueryString()+ e.getMessage() + "\n");
						di.setPROCESS_STATE("0");
						return DataBaseAction.INSERT_ERROR;
					}
					
			}else {
				loggerBuffer.append("\n Database connection error!");
				return DataBaseAction.CONNECTION_ERROR;
			} 	
		}catch (Exception e) {
			e.printStackTrace();
			return DataBaseAction.INSERT_ERROR;
		}finally {
			try {
				if (pStatement != null)
					pStatement.close();
			} catch (SQLException e) {
				loggerBuffer.append("\n Close statement error: "+e.getMessage());
			}
					
		}
	}
	/**
	 * 
	 * @Title: insertdb_minute   
	 * @Description: (L波段探空系统分钟级探测资料入库)   
	 * @param: uparLBands 待入库的对象集合
	 * @param:  connection 数据库连接
	 * @param:  recv_time 资料接收时间
	 * @param:  fileN
	 * @param:  loggerBuffer      
	 * @return: void      
	 * @throws
	 */
	private static DataBaseAction insertdb_minute(List<UparLBand> uparLBands, Connection connection, Date recv_time, String fileN,StringBuffer loggerBuffer, String filepath) {
		String minute_sod_code = StartConfig.sodCodes()[1];
		String ValueTables=StartConfig.valueTable();
		String minuteValueTable=ValueTables.trim().split(",")[1];
		PreparedStatement pStatement = null;
		try {
			String sql = "INSERT INTO "+minuteValueTable+"(D_RECORD_ID,d_ele_id,D_DATETIME,d_update_time,"
					+ "V01301,V01300,V04086,V05015,V06015,V12001,V07004,V13003,V11001,V11002,V10009,"
					+ "Q05015,Q06015,Q12001,Q07004,Q13003,Q11001,Q11002,Q10009,D_SOURCE_ID"
					+ ")"
					+ "VALUES (?, ?, ?, ?,"
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+ "?, ?, ?, ?, ?, ?, ?, ?,?"
					+ ") ";
			if(connection != null) {
					pStatement = new LoggableStatement(connection, sql);
					connection.setAutoCommit(false);
					if(StartConfig.getDatabaseType() == 1) {
						pStatement.execute("select last_txc_xid()");
					}
					List<String> sqls = new ArrayList<>();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
					
					for(int idx = 0; idx < uparLBands.size(); idx ++){
						
						List<UparMinute> uparMinutes = uparLBands.get(idx).getUparminute();
						
						for (int i = 0; i < uparMinutes.size(); i++) {
							StatDi di = new StatDi();
							di.setFILE_NAME_O(fileN);
							//di.setDATA_TYPE(sod_code);
							di.setDATA_TYPE(minute_sod_code);
							di.setDATA_TYPE_1(cts_code);
							di.setTT(V_TT);			
							di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
							di.setPROCESS_START_TIME(TimeUtil.getSysTime());
							di.setFILE_NAME_N(fileN);
							di.setBUSINESS_STATE("1"); //0成功，1失败
							di.setPROCESS_STATE("1");  //0成功，1失败
							
							int stationNumberN = 999999;
							String stat = uparLBands.get(idx).getStationNumberChina();//获取台站号
							if (Pattern.matches("\\d{5}", stat))
								stationNumberN = Integer.parseInt(stat);
							else
								stationNumberN = Integer.parseInt(StationCodeUtil.stringToAscii(stat));
							String lat = String.valueOf((int)(uparLBands.get(idx).getLatitude() * 1e6));//纬度
							String lon = String.valueOf((int)(uparLBands.get(idx).getLongitude() * 1e6));//经度
							lat = lat.replaceAll("-", "0");
							lon = lon.replaceAll("-", "0");
							
							Date date = new Date();
							date = uparLBands.get(idx).getCastingUtcTime();//施放时间（世界时）
							
							
							UparMinute uparMinute = uparMinutes.get(i);//获取单条分钟级数据
							int ii = 1;
							Date dataTime = new Date(date.getTime());
							dataTime.setMinutes(0);
							dataTime.setSeconds(0);
//							int hours = dataTime.getHours();
//							hours = ((hours + 23) % 24 / 3 + 1) * 3 % 24;
//							dataTime.setHours(hours);
//							if(hours == 0){
//								Calendar c = Calendar.getInstance();
//								c.setTime(dataTime);
//								c.add(c.DAY_OF_MONTH, 1);
//								dataTime = c.getTime();
//							}
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(dataTime);
							if(calendar.get(Calendar.HOUR_OF_DAY) % 6 == 5){
								calendar.add(Calendar.HOUR_OF_DAY, 1);
							}else if(calendar.get(Calendar.HOUR_OF_DAY) % 6 == 1){
								calendar.add(Calendar.HOUR_OF_DAY, -1);
							}else if(calendar.get(Calendar.HOUR_OF_DAY) % 6 == 2){
								calendar.add(Calendar.HOUR_OF_DAY, -2);
							}
							
							pStatement.setString(ii++, sdf.format(date.getTime())+"_"+sdf.format(calendar.getTime())+"_"+stat+"_"+lat+"_"+lon);//D_RECORD_ID	
							pStatement.setString(ii++, sdf.format(date.getTime())+"_"+sdf.format(calendar.getTime())+"_"+stat+"_"+lat+"_"+lon+"_"+uparMinute.getRelativeTime());//d_ele_id
							
//							Date dataTime = new Date(date.getTime());
//							dataTime.setMinutes(0);
//							dataTime.setSeconds(0);
//							int hours = dataTime.getHours();
//							hours = ((hours + 23) % 24 / 3 + 1) * 3 % 24;
//							dataTime.setHours(hours);
//							if(hours == 0){
//								Calendar c = Calendar.getInstance();
//								c.setTime(dataTime);
//								c.add(c.DAY_OF_MONTH, 1);
//								dataTime = c.getTime();
//							}
							pStatement.setTimestamp(ii++, new Timestamp(new Date(calendar.getTime().getTime()).getTime()));//D_DATETIME
							
//							pStatement.setTimestamp(ii++, new Timestamp(uparLBands.get(idx).getCastingUtcTime().getTime()));//D_DATETIME
							pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间d_update_id
							pStatement.setString(ii++, stat);//区站号V01301
							pStatement.setInt(ii++, stationNumberN);//区站号ascii V01300
							pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparMinute.getRelativeTime())));//时间差V04086
							pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparMinute.getLatitudeeDev())));//纬度偏差V05015
							pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparMinute.getLongitudeDev())));//经度偏差V06015
							pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparMinute.getTemperature())));//温度V12001
							pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparMinute.getPressure())));//气压V07004
							pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparMinute.getRelativeHumidity())));//相对湿度V13003
							pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparMinute.getWindDir())));//风向V11001
							pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparMinute.getWindSpeed())));//风速V11002
							pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparMinute.getHeight())));//位势高度V10009	
							pStatement.setInt(ii++, 9);//纬度偏差质量标志Q05015
							pStatement.setInt(ii++, 9);//经度偏差质量标志Q06015
							pStatement.setInt(ii++, 9);//温度质量标志Q12001
							pStatement.setInt(ii++, 9);//气压（标准层）质量标志Q07004
							pStatement.setInt(ii++, 9);//相对湿度质量标志Q13003
							pStatement.setInt(ii++, 9);//风向质量标志Q11001
							pStatement.setInt(ii++, 9);//风速质量标志Q11002
							pStatement.setInt(ii++, 9);//位势高度质量标志Q10009
							pStatement.setString(ii++, cts_code);//资料四级编码
							
							di.setIIiii(uparLBands.get(idx).getStationNumberChina());
							di.setDATA_TIME(TimeUtil.date2String(calendar.getTime(), "yyyy-MM-dd HH:mm"));
							di.setPROCESS_END_TIME(TimeUtil.getSysTime());
							di.setRECORD_TIME(TimeUtil.getSysTime());	
							di.setLATITUDE(uparLBands.get(idx).getLatitude().toString());
							di.setLONGTITUDE(uparLBands.get(idx).getLongitude().toString());
							
							di.setSEND("BFDB");
							di.setSEND_PHYS("DRDS");
							di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
							di.setDATA_UPDATE_FLAG(uparLBands.get(idx).getCorrectSign());
							di.setHEIGHT(String.valueOf(uparLBands.get(idx).getHeightOfSationGroundAboveMeanSeaLevel()));
							
							pStatement.addBatch();
							sqls.add(((LoggableStatement)pStatement).getQueryString()); // 批量入库冲突时，单条入库
							listDi.add(di);
							
							System.out.println(((LoggableStatement)pStatement).getQueryString());
						}//end for uparLTempMinutes					
						
					}// end for uparLTemps.size()
					try{
						pStatement.executeBatch();
						connection.commit();
						loggerBuffer.append("BATCH INSERT UPAR_WEA_C_MUL_MIN_TAB SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
						loggerBuffer.append("BATCH INSERT UPAR_WEA_C_MUL_MIN_TAB SUCCESS \n");				
						sqls.clear();
					    return DataBaseAction.SUCCESS;

					}catch(SQLException e){
						pStatement.clearParameters();
						pStatement.clearBatch();
						connection.rollback();
						execute_sql(sqls, connection,fileN,loggerBuffer); // 此中，入库失败的会将对应的DI的ProcessState置为1 // 1:失败，0：成功
						loggerBuffer.append(" BATCH INSERT UPAR_WEA_C_MUL_MIN_TAB ERROR : " + sdf.format(new Date()) + "\n");
					    return DataBaseAction.BATCH_ERROR;
					}
					//return DataBaseAction.SUCCESS;
			}//if connection
			else {
				loggerBuffer.append("\n Database connection error!");
				return DataBaseAction.CONNECTION_ERROR;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return DataBaseAction.INSERT_ERROR;
		}finally {
			try {
				if (pStatement != null)
					pStatement.close();
			} catch (SQLException e) {
				loggerBuffer.append("\n Close statement error: "+e.getMessage());
			}
			
		}
		
	}
	/**
	 * (L波段探空系统基本参数表入库) 
	 * @param uparLBands
	 * @param ktablename
	 * @param sodcode
	 * @param connection
	 * @param recv_time
	 * @param loggerBuffer
	 * @param cts_code2
	 * @param fileN
	 * @return
	 */
	private static DataBaseAction insertParaTab(UparLBand uparLBand,String ParaTableName,String sodcode,Connection connection, Date recv_time, StringBuffer loggerBuffer,String cts_code2, String fileN, String filepath,double latitude,double longitude) {
		int year,month,day,hour,minute,second;
		String defaultS="999998";
		double  defaultF=999998;
		int defaultInt=999998;
		PreparedStatement pStatement = null;
		try {
			String sql = "INSERT INTO "+ParaTableName+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V_BBB,"
					+ "V04001_02,V04002_02,V04003_02,V04004_02,V01301,V01300,V05001,V06001,V07001,V04001,"
					+ "V04002,V04003,V04004,V04005,V04006,V01081,V01082,V02067,V02081,V02084,"
					+ "V02095,V02096,V02097,V02303,V02304,V02420,V02421,V02305,V31001,V02082,"
					+ "V02102,V02307,V02308,V02309,V02422,V02310,V12312,V12313,V12401,V10303,"
					+ "V10304,V10402,V13360,V13361,V13362,V02311,V04300_017_01,V04300_017_02,V04300_018_01,V04300_018_02,"
					+ "V35035_01,V35035_02,V35300_01,V35300_02,V07022_01,V07022_02,V12001,V10004,V13003,V11001,"
					+ "V11002,V20001,V20012_01,V20012_02,V20012_03,V20010,V20051,V20003_01,V20003_02,V20003_03,"
					+ "V05021,V07021,V06021,D_SOURCE_ID) VALUES("
					+"?, ?, ?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+ " ?, ?, ?, ?)";
			if(connection != null) {
					pStatement = new LoggableStatement(connection, sql);
					connection.setAutoCommit(false);
					if(StartConfig.getDatabaseType() == 1) {
						pStatement.execute("select last_txc_xid()");
					}
					List<String> sqls = new ArrayList<>();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
					Calendar calendar = Calendar.getInstance();//year,month,day,hour,minute,second					
						
						Date date = uparLBand.getCastingUtcTime();//获取施放时间（世界时）
						calendar.setTime(date);//Date类型转int
						year = calendar.get(Calendar.YEAR);//获取年
						month = calendar.get(Calendar.MONTH) + 1;//获取月
						day = calendar.get(Calendar.DAY_OF_MONTH);//获取日
						hour = calendar.get(Calendar.HOUR_OF_DAY);//获取日
						minute = calendar.get(Calendar.MINUTE);//获取分
						second = calendar.get(Calendar.SECOND);//获取秒
						StatDi di = new StatDi();
						di.setFILE_NAME_O(fileN);
						//di.setDATA_TYPE(sod_code);
						di.setDATA_TYPE(sodcode);
						di.setDATA_TYPE_1(cts_code);
						di.setTT(V_TT);			
						di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
						di.setPROCESS_START_TIME(TimeUtil.getSysTime());
						di.setFILE_NAME_N(fileN);
						di.setBUSINESS_STATE("1"); //0成功，1失败
						di.setPROCESS_STATE("1");  //0成功，1失败
						
						int stationNumberN = 999999;
						String stat = uparLBand.getStationNumberChina();//获取台站号
						if (Pattern.matches("\\d{5}", stat))
							stationNumberN = Integer.parseInt(stat);
						else
							stationNumberN = Integer.parseInt(StationCodeUtil.stringToAscii(stat));
						String lat = String.valueOf((int)(latitude * 1e6));
						String lon = String.valueOf((int)(longitude * 1e6));
						lat = lat.replaceAll("-", "0");
						lon = lon.replaceAll("-", "0");						
						di.setIIiii(uparLBand.getStationNumberChina());
//						di.setDATA_TIME(TimeUtil.date2String(date, "yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());	
						di.setLATITUDE(String.valueOf(latitude));
						di.setLONGTITUDE(String.valueOf(longitude));
						
						di.setSEND("BFDB");
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
						di.setDATA_UPDATE_FLAG(uparLBand.getCorrectSign());
						di.setHEIGHT(String.valueOf(uparLBand.getHeightOfSationGroundAboveMeanSeaLevel()));
						
						int ii=1;
						Date dataTime = new Date(date.getTime());
						dataTime.setMinutes(0);
						dataTime.setSeconds(0);
//						int hours = dataTime.getHours();
//						hours = ((hours + 23) % 24 / 3 + 1) * 3 % 24;
//						dataTime.setHours(hours);
//						if(hours == 0){
//							Calendar c = Calendar.getInstance();
//							c.setTime(dataTime);
//							c.add(c.DAY_OF_MONTH, 1);
//							dataTime = c.getTime();
//						}
						Calendar calendar2 = Calendar.getInstance();
						calendar2.setTime(dataTime);
						if(calendar2.get(Calendar.HOUR_OF_DAY) % 6 == 5){
							calendar2.add(Calendar.HOUR_OF_DAY, 1);
						}else if(calendar2.get(Calendar.HOUR_OF_DAY) % 6 == 1){
							calendar2.add(Calendar.HOUR_OF_DAY, -1);
						}else if(calendar2.get(Calendar.HOUR_OF_DAY) % 6 == 2){
							calendar2.add(Calendar.HOUR_OF_DAY, -2);
						}
						di.setDATA_TIME(TimeUtil.date2String(calendar2.getTime(), "yyyy-MM-dd HH:mm"));
						//D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V_BBB,
						pStatement.setString(ii++, sdf.format(date.getTime())+"_"+sdf.format(calendar2.getTime())+"_"+stat+"_"+lat+"_"+lon);//记录标识D_RECORD_ID	
						pStatement.setString(ii++,sodcode);//资料标识				
						pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//入库时间
						pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));//收到时间
						pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间d_update_id
					
//						Date dataTime = new Date(date.getTime());
//						dataTime.setMinutes(0);
//						dataTime.setSeconds(0);
//						int hours = dataTime.getHours();
//						hours = ((hours + 23) % 24 / 3 + 1) * 3 % 24;
//						dataTime.setHours(hours);
//						if(hours == 0){
//							Calendar c = Calendar.getInstance();
//							c.setTime(dataTime);
//							c.add(c.DAY_OF_MONTH, 1);
//							dataTime = c.getTime();
//						}
						pStatement.setTimestamp(ii++, new Timestamp(new Date(calendar2.getTime().getTime()).getTime()));//资料时间D_DATETIME
//						pStatement.setTimestamp(ii++, new Timestamp(uparLBands.get(idx).getCastingUtcTime().getTime()));//资料时间D_DATETIME
						pStatement.setString(ii++, uparLBand.getCorrectSign());//更正标识
						
						//"V04001_02,V04002_02,V04003_02,V04004_02,V01301,V01300,V05001,V06001,V07001,V04001"
						pStatement.setInt(ii++, calendar2.getTime().getYear()+1900);//资料年
						pStatement.setInt(ii++, calendar2.getTime().getMonth()+1);//资料月
						pStatement.setInt(ii++,calendar2.getTime().getDate());//资料日
						pStatement.setInt(ii++, calendar2.getTime().getHours());//资料时
						pStatement.setString(ii++, stat);//字符区站号V01301
						pStatement.setInt(ii++, stationNumberN);//数字区站号V01300
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(latitude)));//纬度
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(longitude)));//经度
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparLBand.getHeightOfSationGroundAboveMeanSeaLevel())));//测站海拔高度V07001
						pStatement.setInt(ii++, year);//气球施放时间（年）
						
						//"V04002,V04003,V04004,V04005,V04006,V01081,V01082,V02067,V02081,V02084,"
						pStatement.setInt(ii++, month);//气球施放时间（月）
						pStatement.setInt(ii++, day);//气球施放时间（日）
						pStatement.setInt(ii++, hour);//气球施放时间（时）
						pStatement.setInt(ii++, minute);//气球施放时间（分）
						pStatement.setInt(ii++, second);//气球施放时间（秒）
						pStatement.setString(ii++, defaultS);//无线电探空仪系列号V01081
						pStatement.setInt(ii++, defaultInt);//无线电探空仪上升序列号V01082
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(defaultF)));//无线电探空仪业务频率V02067
						pStatement.setInt(ii++, defaultInt);//气球类型V02081
						pStatement.setInt(ii++, defaultInt);//气球中所用的气体类型V02084
						
						//"V02095,V02096,V02097,V02303,V02304,V02420,V02421,V02305,V31001,V02082,"
						pStatement.setInt(ii++, defaultInt);//气压传感器类型V02095
						pStatement.setInt(ii++, defaultInt);//温度传感器类型V02096
						pStatement.setInt(ii++, defaultInt);//湿度传感器类型V02097
						pStatement.setString(ii++, uparLBand.getTypeOfDetectionSystem());//探测系统型号V02303
						pStatement.setString(ii++, uparLBand.getRadiosondeModel());//探空仪型号V02304
						pStatement.setInt(ii++, defaultInt);//探空仪生产厂家V02420
						pStatement.setInt(ii++, defaultInt);//探空仪生产日期V02421
						pStatement.setString(ii++, uparLBand.getInstrumentNumber());//探空仪编号V02305
						pStatement.setInt(ii++, uparLBand.getCastCount());//施放计数V31001
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparLBand.getBallWeight())));//气球重量V02082
						
						//"V02102,V02307,V02308,V02309,V02422,V02310,V12312,V12313,V12401,V10303,"
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparLBand.getAntennaHeight())));//探测系统天线高度V02102
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparLBand.getAdditiveWeight())));//附加物重量V02307
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparLBand.getTotalForce())));//总举力V02308
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparLBand.getNetLifting())));//净举力V02309
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(defaultF)));//球与探空仪间实际绳长V02422
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparLBand.getAverageSpeedOfLift())));//平均升速V02310
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparLBand.getBaseTemperatureValue())));//温度基测值V12312
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparLBand.getInstrumentTemperatureValue())));//温度仪器值V12313
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparLBand.getTemperatureDeviation())));//温度偏差V12401
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparLBand.getBasePressureValue())));//气压基测值V10303
						
						//"V10304,V10402,V13360,V13361,V13362,V02311,V04300_017_01,V04300_017_02,V04300_018_01,V04300_018_02,"
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparLBand.getInstrumentPressureValue())));//气压仪器值V10304
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparLBand.getPressureDeviation())));//气压偏差V10402
						pStatement.setInt(ii++, uparLBand.getBaseRelativeHumidityValue());//相对湿度基测值V13360
						pStatement.setInt(ii++,  uparLBand.getInstrumentRelativeHumidityValue());//相对湿度仪器值V13361
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparLBand.getRelativeHumidityDeviation())));//相对湿度偏差V13362
						pStatement.setInt(ii++, uparLBand.getInstrumentTestConclusion());//仪器检测结论V02311
						pStatement.setLong(ii++, Long.valueOf(sdf.format(uparLBand.getCastingUtcTime().getTime())));//施放时间（世界时）V04300_017_01
						pStatement.setLong(ii++, Long.valueOf(sdf.format(uparLBand.getCastingLocalTime().getTime())));//施放时间（地方时）	V04300_017_02
						pStatement.setLong(ii++, Long.valueOf(sdf.format(uparLBand.getSoundingTerminationUtcTime().getTime())));//探空终止时间（世界时）	V04300_018_01
						pStatement.setLong(ii++, Long.valueOf(sdf.format(uparLBand.getAnemometryTerminationUtcTime().getTime())));//测风终止时间（世界时）	V04300_018_02
						
						// "V35035_01,V35035_02,V35300_01,V35300_02,V07022_01,V07022_02,V12001,V10004,V13003,V11001,"
						pStatement.setInt(ii++, uparLBand.getReasonForSoundingTermination());//探空终止原因	V35035_01
						pStatement.setInt(ii++, uparLBand.getReasonForAnemometryTermination());//测风终止原因	V35035_02
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparLBand.getSoundingTerminationHeight())));//探空终止高度	V35300_01
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparLBand.getAnemometryTerminationHeight())));//测风终止高度	V35300_02
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparLBand.getSolarElevationAngle())));//施放时太阳高度角	V07022_01
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(defaultF)));//终止时太阳高度角	V07022_02
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparLBand.getInstantGroundTemperature())));//施放瞬间地面气温	V12001
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparLBand.getInstantGroundPressure())));//施放瞬间地面气压	V10004
						pStatement.setInt(ii++, uparLBand.getInstantGroundRelativeHumidity());//施放瞬间地面相对湿度	V13003
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparLBand.getInstantGroundWindDirection())));//施放瞬间地面风向	V11001
						
						//"V11002,V20001,V20012_01,V20012_02,V20012_03,V20010,V20051,V20003_01,V20003_02,V20003_03,"
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparLBand.getInstantGroundWindSpeed())));//施放瞬间地面风速	V11002
//							int instantVisibility=new Double(temp).intValue();
							BigDecimal aa=new BigDecimal(uparLBand.getInstantVisibility()*1000).setScale(0,BigDecimal.ROUND_HALF_UP);
							int instantVisibility=aa.intValue();
						pStatement.setInt(ii++, instantVisibility);//施放瞬间能见度	V20001
						pStatement.setInt(ii++, uparLBand.getInstantCloudGenus1());//施放瞬间云属1	V20012_01
						pStatement.setInt(ii++, uparLBand.getInstantCloudGenus2());//施放瞬间云属2	V20012_02
						pStatement.setInt(ii++, uparLBand.getInstantCloudGenus3());//施放瞬间云属3	V20012_03
						pStatement.setInt(ii++, uparLBand.getInstantTotalCloudCover());//施放瞬间总云量	V20010
						pStatement.setInt(ii++, uparLBand.getInstantLowCloudCover());//施放瞬间低云量	V20051
						pStatement.setInt(ii++, uparLBand.getInstantWeatherPhenomenon1());//施放瞬间天气现象1	V20003_01
						pStatement.setInt(ii++, uparLBand.getInstantWeatherPhenomenon2());//施放瞬间天气现象2	V20003_02
						pStatement.setInt(ii++, uparLBand.getInstantWeatherPhenomenon3());//施放瞬间天气现象3	V20003_03
						
						//V05021,V07021,V06021,D_SOURCE_ID
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparLBand.getCastPointAzimuth())));//施放点方位角	V05021
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparLBand.getCastPointElevation())));//施放点仰角	V07021
						pStatement.setBigDecimal(ii++,new BigDecimal(String.valueOf(uparLBand.getCastPointDistance())));//施放点距离（探测仪器与天线之间距离）	V06021
						pStatement.setString(ii++, cts_code);//资料四级编码D_SOURCE_ID

						System.out.println(((LoggableStatement)pStatement).getQueryString());

//						pStatement.addBatch();
//						sqls.add(((LoggableStatement)pStatement).getQueryString()); // 批量入库冲突时，单条入库
						listDi.add(di);	
						
					//return DataBaseAction.SUCCESS;
					try{
						pStatement.execute();
						connection.commit();
						loggerBuffer.append("\n filename:"+fileN+" INSERT "+ParaTableName+" SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
//						loggerBuffer.append("BATCH INSERT "+ParaTableName+"SUCCESS \n");
						sqls.clear();
					    return DataBaseAction.SUCCESS;						
					}catch(SQLException e){
//						execute_sql(sqls, connection,fileN,loggerBuffer); // 此中，入库失败的会将对应的DI的ProcessState置为1 // 1:失败，0：成功
						loggerBuffer.append("\n filename:"+fileN+" INSERT "+ParaTableName+" ERROR : " +((LoggableStatement)pStatement).getQueryString()+ "\n"+e.getMessage() + "\n");
						di.setPROCESS_STATE("0");
						return DataBaseAction.INSERT_ERROR;
					}
					
			}else {
				loggerBuffer.append("\n Database connection error!");
				return DataBaseAction.CONNECTION_ERROR;
			} 	
		}catch (Exception e) {
			e.printStackTrace();
			return DataBaseAction.INSERT_ERROR;
		}finally {
			try {
				if (pStatement != null)
					pStatement.close();
			} catch (SQLException e) {
				loggerBuffer.append("\n Close statement error: "+e.getMessage());
			}
					
		}
	}
	/**
	 * 
	 * @Title: execute_sql   
	 * @Description: TODO(批量入库失败时，采用逐条提交)   
	 * @param: sqls 待执行的查询语句
	 * @param: connection      数据库连接
	 * @param:  fileN
	 * @param:  loggerBuffer      
	 * @return: void      
	 * @throws
	 */
	private static boolean execute_sql(List<String> sqls, Connection connection, String fileN, StringBuffer loggerBuffer) {
		Statement pStatement = null;
		boolean f=false;
		try {
			connection.setAutoCommit(true);
			pStatement = connection.createStatement();
			for (int i = 0; i < sqls.size(); i++) {
//				pStatement = new LoggableStatement(connection, sqls.get(i));
				/*if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}*/
				try {
					pStatement.execute(sqls.get(i));
					loggerBuffer.append("\n filename："+fileN+" execute sql success:"+sqls.get(i));
					f=true;
				} catch (Exception e) {
					loggerBuffer.append("\n filename："+fileN+" excute sql error："+sqls.get(i)+"\n "+e.getMessage());
//					listDi.get(i).setPROCESS_STATE("0");
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
		return f;
	}
		
		/**
		 * The main method.
		 *
		 * @param args the arguments
		 */
		public static void main(String[] args) {
			DecodeLbandUparDetection decodeLbandUparDetection = new DecodeLbandUparDetection();
//			String filepath = "D:\\HUAXIN\\DataProcess\\B.0003.0001.R001\\ss\\Z_UPAR_I_44373_20181223231845_O_TEMP-L.txt";
			String filepath = "D:\\TEMP\\B.3.1.1\\8-1\\Z_UPAR_I_59948_20190731231556_O_TEMP-L.txt";
			
			File file = new File(filepath);
			ParseResult<UparLBand> parseResult = decodeLbandUparDetection.DecodeFile(file);
			DataBaseAction action = null;
			Date recv_time = new Date();
			StringBuffer loggerBuffer = new StringBuffer();
			String fileN1 = null;
		    fileN1 = file.getName();
//			if (StartConfig.getDatabaseType() == 1) {
				action = DbService.insertDBService(parseResult, recv_time,fileN1,loggerBuffer, filepath);
				System.out.println("insertDBService over!");
//			}
		
		}
	//计算经纬度
	public	static double [] calcuLatAndLon(String station,double latitude,double longitude,String filename){
	  Map<String, Object> proMap = StationInfo.getProMap();
	  double[] LatAndLon={latitude,longitude};
	  if (latitude<-90||latitude>90) {
		  latitude=999999;
	  }
	  if(longitude<-180||longitude>180){
		  longitude=999999;
	  }
	  if(latitude==999999.0||longitude==999999.0){
			String info = (String) proMap.get(station + "+04");
			if(info == null||"".equals(info)) {
				infoLogger.error("经纬度无法解析！"  + filename);
				sendEI(station);
				return LatAndLon;
				
			}else{
				String[] infos = info.split(",");
				if(!(infos[1].trim().equals("null") || infos[1].trim().equals(""))){
					try{
					 longitude = Double.parseDouble(infos[1]);//经度
					}catch (Exception e) {
						infoLogger.error("经纬度无法解析！"  + filename);
						sendEI(station);
						return LatAndLon;
					}
				}else{
					infoLogger.error("经纬度无法解析！"  + filename);
					sendEI(station);
					return LatAndLon;
				}
				if(!(infos[2].trim().equals("null") || infos[2].trim().equals(""))){
					try{
					 latitude = Double.parseDouble(infos[2]);//纬度
					}catch (Exception e) {
						infoLogger.error("经纬度无法解析！"  + filename);
						sendEI(station);
						return LatAndLon;
					}
				}else{
					infoLogger.error("经纬度无法解析！"  + filename);
					sendEI(station);
					return LatAndLon;
				}
				latitude= Double.parseDouble(String.format("%.6f", latitude));
				longitude= Double.parseDouble(String.format("%.6f", longitude));
				LatAndLon[0]=latitude;
				LatAndLon[1]=longitude;
			}
		}else{
			latitude= Double.parseDouble(String.format("%.6f", latitude));
			longitude= Double.parseDouble(String.format("%.6f", longitude));
			LatAndLon[0]=latitude;
			LatAndLon[1]=longitude;
		}
	  return LatAndLon;
	}
	public static void sendEI(String station){
		String event_type = EIEventType.STATION_FILE_ERROR.getCode();
		EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
		if(ei == null) {
			infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: "+event_type);
		}else {
			if(StartConfig.isSendEi()) {
				ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
				ei.setKObject("cma.cimiss2.dpc.indb.ocen.dc_ocean_ori_ship.service.DbService");
				ei.setKEvent("获取台站信息异常");
				ei.setKIndex("全球高空台站："+station+"未能获取台站信息配置，无法获取经纬度");
				RestfulInfo restfulInfo = new RestfulInfo();
				restfulInfo.setType("SYSTEM.ALARM.EI ");
				restfulInfo.setName("获取台站信息EI告警信息");
				restfulInfo.setMessage("获取台站信息EI告警信息");
				restfulInfo.setFields(ei);
				List<RestfulInfo> restfulInfos = new ArrayList<>();
				restfulInfos.add(restfulInfo);
				RestfulSendData.SendData(restfulInfos, SendType.EI);
			}
		}
	}
}
