package cma.cimiss2.dpc.indb.upar.dc_upar_zkylight.service;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import cma.cimiss2.dpc.decoder.bean.upar.Upar_zkyLight;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;



public class DbService {
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	public static String V_TT = "";
	
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}
	/**
	 * 
	 * @Title: processSuccessReport   
	 * @Description:(中科院闪电数据处理函数)   
	 * @param  parseResult 解码结果集合
	 * @param  recv_time 解析文件的路径
	 * @param fileN 资料接收时间   
	 * @param loggerBuffer 
	 * @return: DataBaseAction      
	 * @throws:
	 */
	
	public static DataBaseAction processSuccessReport(ParseResult<Upar_zkyLight> parseResult, Date recv_time, String fileN, StringBuffer loggerBuffer, String filepath) {
		java.sql.Connection connection = null;
		try {
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			List<Upar_zkyLight> upar_zkyLights = parseResult.getData();
			insertdb(upar_zkyLights, connection,recv_time, fileN,loggerBuffer, filepath);
			return DataBaseAction.SUCCESS;
		} catch (Exception e) {
			loggerBuffer.append("\n database connection  error");
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
					loggerBuffer.append("\n database connection  close error"+e.getMessage());
					
				}
			}
		}
	}
	/**
	 * 
	 * @Title: insertdb
	 * @Description:(中科院闪电数据资料入库)   
	 * @param  upar_zkyLights 待入库的对象集合
	 * @param  connection 数据库连接
	 * @param  recv_time      资料接收时间
	 * @param fileN 
	 * @param loggerBuffer 
	 * @return: void      
	 * @throws:
	 */
	@SuppressWarnings("deprecation")
	private static void insertdb(List<Upar_zkyLight> upar_zkyLights, java.sql.Connection connection, Date recv_time, String fileN, StringBuffer loggerBuffer, String filepath) {
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,D_UPDATE_TIME,"
				+ "V05001,V06001,V07001,V04001,V04002,V04003,V04004,V04005,V04006,V04007,"
				+ "V73016,V73023,V73011,V73110,V01015_1,V01015_2,V01015_3,V_PROCESSFLAG,V_USEDIDS,V_CG_IC,V_BBB,D_SOURCE_ID"
				+ ")"
				+ "VALUES (?, ?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?"
				+ ") ";
		if(connection != null){		
			try {	
				pStatement = new LoggableStatement(connection, sql);
				connection.setAutoCommit(false);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				List<String> sqls = new ArrayList<>();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");		
				Upar_zkyLight upar_zkyLight = new Upar_zkyLight();
				for(int idx = 0; idx < upar_zkyLights.size(); idx ++){
					upar_zkyLight = upar_zkyLights.get(idx);
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
					
					String lat = String.valueOf((int)(upar_zkyLight.getLatitude() * 1e6));
					String lon = String.valueOf((int)(upar_zkyLight.getLongtitude() * 1e6));
					lat = lat.replaceAll("-", "0");
					lon = lon.replaceAll("-", "0");
					int ii = 1;
					Date date = new Date();
					date = upar_zkyLight.getObservationTime();
					
					pStatement.setString(ii++, sdf.format(date)+"_"+upar_zkyLight.getMillisecond()+"_"+lat+"_"+lon);
					pStatement.setString(ii++, sod_code);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(date);
//					calendar.set(Calendar.MILLISECOND, 0);
//					calendar.set(Calendar.SECOND, 0);
					Timestamp timestamp = new Timestamp(calendar.getTime().getTime());
					pStatement.setTimestamp(ii++, timestamp); // dateTime
					
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime())); // updateTime
					
					pStatement.setBigDecimal(ii++, new BigDecimal(upar_zkyLight.getLatitude()).setScale(6, BigDecimal.ROUND_HALF_UP)); //闪电位置纬度
					pStatement.setBigDecimal(ii++, new BigDecimal(upar_zkyLight.getLongtitude()).setScale(6, BigDecimal.ROUND_HALF_UP));//闪电位置经度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(upar_zkyLight.getHeight())));//闪电位置高度
					pStatement.setInt(ii++, date.getYear() +1900);//资料观测年
					pStatement.setInt(ii++, date.getMonth() + 1);//资料观测月
					pStatement.setInt(ii++, date.getDate());//资料观测日
					pStatement.setInt(ii++, date.getHours());//资料观测时
					pStatement.setInt(ii++, date.getMinutes());//资料观测分
					pStatement.setInt(ii++, date.getSeconds());//资料观测秒
					pStatement.setInt(ii++, upar_zkyLight.getMillisecond());//资料观测毫秒
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(upar_zkyLight.getCurrentIntensity())));//电流强度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(upar_zkyLight.getMaxSlope())));//回击最大陡度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(upar_zkyLight.getError())));//定位误差
					pStatement.setString(ii++, upar_zkyLight.getLocationType());//定位方式
					pStatement.setString(ii++, upar_zkyLight.getProv());//雷电地理位置信息省
					pStatement.setString(ii++, upar_zkyLight.getDistrict());//雷电地理位置信息市
					pStatement.setString(ii++, upar_zkyLight.getCountry());//雷电地理位置信息县
					pStatement.setInt(ii++, upar_zkyLight.getProcessFlag());//标志位
					pStatement.setString(ii++, upar_zkyLight.getUsedIDs());//定位仪编号
					pStatement.setString(ii++, upar_zkyLight.getCG_IC());//云/地闪
					pStatement.setString(ii++, "000");
					pStatement.setString(ii++, cts_code);
					
					// 闪电数据 没有台站号，只有位置和省市县信息
					di.setIIiii(upar_zkyLight.getProv()+" "+ upar_zkyLight.getDistrict() +" "+upar_zkyLight.getCountry());
					di.setDATA_TIME(TimeUtil.date2String(date, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(upar_zkyLight.getLatitude().toString());
					di.setLONGTITUDE(upar_zkyLight.getLongtitude().toString());
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
					di.setDATA_UPDATE_FLAG("000");
					di.setHEIGHT(String.valueOf(upar_zkyLight.getHeight()));
					
					pStatement.addBatch();
					sqls.add(((LoggableStatement)pStatement).getQueryString()); // 批量入库冲突时，单条入库
					listDi.add(di);
				} // end for
				try{
					pStatement.executeBatch();
					connection.commit();
					loggerBuffer.append(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
					loggerBuffer.append(" INSERT SUCCESS COUNT ：" + upar_zkyLights.size() + "\n");
					sqls.clear();
				}catch(SQLException e){
					pStatement.clearParameters();
					pStatement.clearBatch();
					execute_sql(sqls, connection,fileN,loggerBuffer); // 此中，入库失败的会将对应的DI的ProcessState置为1 // 1:失败，0：成功
					loggerBuffer.append(" Batch ERROR : " + sdf.format(new Date()) + "\n");
				}
			}catch (SQLException e) {
				loggerBuffer.append("\n create Statement  error"+e.getMessage());
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
	 * @Description:(批量入库失败时，采用逐条提交)   
	 * @param sqls 待执行的查询语句
	 * @param  connection     数据库连接 
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
				/*if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}*/
				try {
					pStatement.execute(sqls.get(i));
				} catch (Exception e) {
					loggerBuffer.append("\n filename："+fileN
							+"\n " + listDi.get(i).getIIiii() + " " + listDi.get(i).getDATA_TIME()
							+"\n excute sql error："+sqls.get(i)+"\n "+e.getMessage());
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
