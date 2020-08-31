package cma.cimiss2.dpc.indb.surf.dc_surf_stat.service;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import cma.cimiss2.dpc.decoder.agme.PropertiesUtil;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.surf.DecodeStat;
import cma.cimiss2.dpc.decoder.surf.SurfStatParseConfig;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.upar.DecodeHKFlash;
import cma.cimiss2.dpc.decoder.upar.HKFlashParseConfig;

// TODO: Auto-generated Javadoc
/**
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>

 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《》 </a>
 *      <li> <a href=" "> 《》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2019年3月4日 下午1:53:04   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class DbService {
	
	/** The Constant messageLogger. */
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	
	/** The Constant infoLogger. */
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	
	/** The list di. */
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	
	/** The di queues. */
	public static BlockingQueue<StatDi> diQueues;
	
	/** The insert batch size. */
	public static int insertBatchSize = 200;
	
	/**
	 * Gets the di queues.
	 *
	 * @return the di queues
	 */
	public static BlockingQueue<StatDi> getDiQueues(){
		return diQueues;
	}
	
	/**
	 * Sets the di queues.
	 *
	 * @param diQueues the new di queues
	 */
	public static void setDiQueues(BlockingQueue<StatDi> diQueues){
		DbService.diQueues = diQueues;
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		PropertiesUtil.setConfigFile("config/Surf_Stat_DAY_setups.properties");
		SurfStatParseConfig.config = "config/Surf_Stat_DAY_TAB.xml";
		DecodeStat decodeHKFlash = new DecodeStat();
		String filepath = "C:\\BaiduNetdiskDownload\\test\\stat\\test\\Z_SURF_C_BABJ_20200429162607_P_STAT_DAY_10.tmp";
		File file = new File(filepath);
		Date recv_time = new Date(file.lastModified());
		ParseResult<String> parseResult = decodeHKFlash.decodeFile(filepath, "	", recv_time);
//		List<String> sqls = parseResult.getData();
//		for(int i=0;i<sqls.size();i++) {
//			System.out.println(sqls.get(i));
//			String sql=sqls.get(i);
//		Map<String, String> ColVal = new HashMap<String, String>();
//		String va [] = sql.split("\\(|\\)");
//		if(va.length>4){
////			for(int v=4;v<va.length;v++)
////				va[3]+="("+va[v]+")";
//			String tmp = sql.substring(sql.indexOf("(") + 1);
//			va[3] = tmp.substring(tmp.indexOf("(") + 1, tmp.length() - 1);
//
//		}
//		String tableName= PropertiesUtil.getTableName();
//		String dt = "";
//		String sta="";
//	    String record="";
//	    String ry="";
//	    String re="";
//		if(va.length >= 4){
//			String col = va[1];
//			String val = va[3];
//			String cols[] = col.split(",");
//			String vals[] = val.split(",");
//			for(int c = 0; c < cols.length; c ++){
//				ColVal.put(cols[c].trim(), vals[c].trim().replace("'", ""));
//			}
//			if(ColVal.containsKey("V01301"))
//				sta = ColVal.get("V01301");
//			if(ColVal.containsKey("D_DATETIME"))
//				dt = ColVal.get("D_DATETIME");
//			if(ColVal.containsKey("D_RECORD_ID"))
//				record = ColVal.get("D_RECORD_ID");
//			if(ColVal.containsKey("D_RYMDHM"))
//				ry = ColVal.get("D_RYMDHM");
//			if(ColVal.containsKey("D_RETRIEVAL_ TIME"))
//				re = ColVal.get("D_RETRIEVAL_ TIME");
//			}
//		updateExistData(ColVal, sta, dt, tableName, null);
//		}
		DataBaseAction action = null;
		StringBuffer loggerBuffer = new StringBuffer();

		action = DbService.processSuccessReport(parseResult, recv_time, filepath, loggerBuffer, filepath);
		System.out.println("insertDBService over!");
	
	}
	
	/**
	 * Process success report.
	 *
	 * @param parseResult the parse result
	 * @param recv_time the recv time
	 * @param fileN the file N
	 * @param loggerBuffer the logger buffer
	 * @return DataBaseAction
	 * @Title: processSuccessReport
	 * @Description: 香港闪电资料解码入库方法
	 */
	public static DataBaseAction processSuccessReport(ParseResult<String> parseResult, Date recv_time, String fileN, StringBuffer loggerBuffer, String filepath) {
		java.sql.Connection connection = null;
		try {
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			List<String> sqls = parseResult.getData();
			insert(sqls, connection,recv_time, fileN, loggerBuffer, filepath);
			return DataBaseAction.SUCCESS;
		} catch (Exception e) {
			loggerBuffer.append("\n database connection error");
			return DataBaseAction.CONNECTION_ERROR;
		}
		finally{
//			for (int j = 0; j < listDi.size(); j++) {
//				diQueues.offer(listDi.get(j));
//			}
			listDi.clear();
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n database connection close error"+e.getMessage());
				}
			}
		}
	}

	/**
	 * Insert.
	 *
	 * @param sqls the sqls
	 * @param connection the connection
	 * @param recv_time the recv time
	 * @param fileN the file N
	 * @param loggerBuffer the logger buffer
	 */
	private static void insert(List<String> sqls, Connection connection, Date recv_time, String fileN,StringBuffer loggerBuffer, String filepath) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Statement stmt = null;
		int len = sqls.size();
		for(int i = 0; i < len; i += insertBatchSize){
			int sz = (len - i < insertBatchSize) ? (len - i) : insertBatchSize;
			try{
				stmt = connection.createStatement();
				connection.setAutoCommit(false);
				for(int j = i; j < sz + i; j ++){
					StatDi di = new StatDi();	
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					
					String sql = sqls.get(j);
					String station = "999999";
					String height = "999999";
					String lat = "999999";
					String lon = "999999";
					String datatime = "";
					String recordID = "";
					Map<String, String> ColVal = new HashMap<String, String>();
					String va [] = sql.split("\\(|\\)");
					if(va.length>4){
//						for(int v=4;v<va.length;v++)
//							va[3]+="("+va[v]+")";
						String tmp = sql.substring(sql.indexOf("(") + 1);
						va[3] = tmp.substring(tmp.indexOf("(") + 1, tmp.length() - 1);

					}
					if(va.length >= 4){
						String col = va[1];
						String val = va[3];
						String cols[] = col.split(",");
						String vals[] = val.split(",");
						for(int c = 0; c < cols.length; c ++){
							ColVal.put(cols[c].trim(), vals[c].trim().replace("'", ""));
						}
						if(ColVal.containsKey("V01301"))
							station = ColVal.get("V01301");
						if(ColVal.containsKey("V05001"))
							lat = ColVal.get("V05001");
						if(ColVal.containsKey("V06001"))
							lon = ColVal.get("V06001");
						if(ColVal.containsKey("D_DATETIME"))
							datatime = ColVal.get("D_DATETIME");
						if(ColVal.containsKey("V07001"))
							height = ColVal.get("V07001");
						if(ColVal.containsKey("D_RECORD_ID"))
							recordID = ColVal.get("D_RECORD_ID").trim();
					}
					// D_record_ID由约束字段拼接而成，给拼接字段赋值 
					String recordEle[] = recordID.split("\\{|\\}");
					String recordVal = "";
					for(int r = 0; r < recordEle.length; r ++){
						String col = recordEle[r].trim();
						if(col.isEmpty())
							continue;
						else if(ColVal.containsKey(col)){
							if(col.equalsIgnoreCase("D_DATETIME")){
								recordVal = recordVal + "_" + ColVal.get(col).replaceAll(":|-|\\s+", "");
							}else if(col.equalsIgnoreCase("V_starttime")||col.equalsIgnoreCase("V_endtime")){
								recordVal = recordVal + "_" + ColVal.get(col).replaceAll(":|-|\\s+", "");
							}
							else if(col.equalsIgnoreCase("V05001") || col.equalsIgnoreCase("V06001") ){
								double latlon = Double.parseDouble(ColVal.get(col));
								String latlonStr = "";
								if(col.equalsIgnoreCase("V05001")){
									latlonStr = String.format("%d", (int)(latlon * 1e6));
								}
								else {
									latlonStr = String.format("%d", (int)(latlon * 1e6));
								}
								if(latlonStr.startsWith("-"))
									latlonStr = "0" + latlonStr.substring(1);
								recordVal = recordVal + "_" + latlonStr;
							}
							else
								recordVal = recordVal + "_" + ColVal.get(col);
						}
						else 
							recordVal = recordVal + "_" + "999998";
					}
					if(recordVal.length() >= 1)
						recordVal = recordVal.substring(1);
					sql = sql.replace(recordID, recordVal);
					sqls.set(j, sql);
					
					di.setFILE_SIZE(String.valueOf((new File(filepath)).length()));
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_NAME_O(fileN);
					di.setFILE_NAME_N(fileN);
					di.setDATA_TYPE(StartConfig.sodCode());
					di.setDATA_TYPE_1(StartConfig.ctsCode());
					di.setTT(PropertiesUtil.getTT());
					di.setLATITUDE(lat.toString());
					di.setLONGTITUDE(lon.toString());
					di.setHEIGHT(height.toString());
					di.setDATA_UPDATE_FLAG("000");
					
					String tran_time ="";
					String date_time ="";
					int idx=0;
					if(simpleDateFormat.format(recv_time).contains(":")){
					    idx = simpleDateFormat.format(recv_time).lastIndexOf(":");
					    tran_time = simpleDateFormat.format(recv_time).substring(0, idx);
					}else{
						idx=simpleDateFormat.format(new Date()).lastIndexOf(":");
						tran_time = simpleDateFormat.format(new Date()).substring(0, idx);
					}
					if(datatime.contains(":")){
						 idx = datatime.lastIndexOf(":");
						 date_time = datatime.substring(0, idx);
					}else{
						idx=simpleDateFormat.format(new Date()).lastIndexOf(":");
						 date_time = simpleDateFormat.format(new Date()).substring(0, idx);
					}
					di.setTRAN_TIME(tran_time);
					di.setIIiii(station);
					di.setDATA_TIME(date_time);
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1");  //1成功，0失败
					
					listDi.add(di);
//					System.out.println(sqls.get(j));
					stmt.addBatch(sqls.get(j));
				}
				stmt.executeBatch();
				connection.commit();
				infoLogger.info("Batch insert succeed! #Record = " + sz);
			}catch (Exception e) { 
//				e.printStackTrace();
				infoLogger.error("Batch insert failed: \n" + e.getMessage());
				try{
					stmt.clearBatch();
					connection.rollback();
					connection.setAutoCommit(true);
					
					for(int j = i; j < sz + i; j ++){
						try{
							stmt.execute(sqls.get(j));
						}catch (SQLException e1) {
							 if (e1.getErrorCode() == 13001 || e1.getErrorCode() == 1062) {//主键冲突异常，则进行更新入库（先删除后插入）
								
								 String currentsql=sqls.get(j);
									String dt = "";
									String sta="";
								    String record="";
								    String ry="";
								    String re="";
									Map<String, String> ColVal = new HashMap<String, String>();
									String va [] = currentsql.split("\\(|\\)");
									if(va.length>4){
										String tmp = currentsql.substring(currentsql.indexOf("(") + 1);
										va[3] = tmp.substring(tmp.indexOf("(") + 1, tmp.length() - 1);
									}
									if(va.length >= 4){
										String col = va[1];
										String val = va[3];
										String cols[] = col.split(",");
										String vals[] = val.split(",");
										for(int c = 0; c < cols.length; c ++){
											ColVal.put(cols[c].trim(), vals[c].trim().replace("'", ""));
										}
										if(ColVal.containsKey("V01301"))
											sta = ColVal.get("V01301");
										if(ColVal.containsKey("D_DATETIME"))
											dt = ColVal.get("D_DATETIME");
										if(ColVal.containsKey("D_RECORD_ID"))
											record = ColVal.get("D_RECORD_ID");
										if(ColVal.containsKey("D_RYMDHM"))
											ry = ColVal.get("D_RYMDHM");
										if(ColVal.containsKey("D_RETRIEVAL_ TIME"))
											re = ColVal.get("D_RETRIEVAL_ TIME");
									}
								 
								String tableName= PropertiesUtil.getTableName();
//								String ctsCode=PropertiesUtil.getCTSCode();
								int deletecount=0;
								int updatecount=0;
//								if (ctsCode.equals("G.0023.0003.R001")) {//环保部酸雨资料
//									 deletecount=deleteExistData1(sta,dt,min,sec,startdt,enddt,tableName,connection);//删除库里已有的数据
//								}else{//环保部气体 （国控/非国控）
								/*
								 * 更新逻辑
中国地面常规统计（包括日、候、旬、月、季、年）更新：当数据库中d_rymdhm字段为空或者小于本条记录的d_rymdhm字段时，更新数据库中的数据。
中国地面日值-天气现象统计更新流程：当数据库中d_retrieval_time字段为空或者小于本条记录的d_retrieval_time字段时，更新数据库中的数据。
								 */
								String[] Existresult=findExistData(dt,tableName,sta,connection);//查找日值/日照表库里原有数据
								String rymdhm=Existresult[0];
								String retrieval=Existresult[1];
								if(fileN.trim().contains("WEA")) {//地面日值-天气现象统计
								    if(retrieval==null||simpleDateFormat.parse(re).getTime()>simpleDateFormat.parse(retrieval).getTime()) {
								    	 deletecount=deleteExistData(sta,dt,tableName,connection);//删除库里已有的数据
								    }
								}else {//地面常规统计（包括日、候、旬、月、季、年）
									if(rymdhm==null||simpleDateFormat.parse(ry).getTime()>simpleDateFormat.parse(rymdhm).getTime()) {
									/*
									 * 日值统计结果入库:入库程序在已经存在记录时需要更新附录1之外的相应字段及其质控码字段，附录1字段不更新更新；更正处理遵循相同处理原则，只更新附录1之外的要素。
									 */
										if(fileN.trim().contains("DAY")) {
											updatecount=updateExistData(ColVal,sta,dt,tableName,connection);
										}else {
											deletecount=deleteExistData(sta,dt,tableName,connection);//删除库里已有的数据
										}
									}
								}
									
//								}
								if(deletecount>0||updatecount>0){
									try{
										stmt.execute(sqls.get(j));
										infoLogger.error("update a record succeed! "+fileN+"\n" );
									}catch (Exception e3) {
										if(j < listDi.size())
											listDi.get(j).setPROCESS_STATE("0");
										else{
											
										}
										infoLogger.error("update record one by one failed: "+fileN+"\n" + e3.getMessage() + sqls.get(j));
									}
								}
							 }else{//非主键冲突的其他异常
								listDi.get(j).setPROCESS_STATE("0");
								infoLogger.error("Insert record one by one failed: \n" + e1.getMessage() + sqls.get(j));
							 }
						}
					}
				}catch (Exception e2) {
					infoLogger.error("Rollback failed: \n" + e2.getMessage());
				}
			}
			finally {
				if(stmt != null){
					try{
						stmt.close();
					}catch (Exception e) {
						infoLogger.equals("close statement error!");
					}
				}
			}
		}
		
	}
	//查找库里原有数据
		static	String[] findExistData(String datetimeToday,String tableName,String sta,Connection connection){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			PreparedStatement Pstmt = null;
			ResultSet resultSet  = null;
			String rymdhm = null;
			String retrieval=null;
			String rntString [] = {rymdhm, retrieval};
			String sql = "select d_rymdhm,d_retrieval_time from "+tableName+" "
					+ "where  D_DATETIME = ? and V01301=?";
			try{
				if(connection != null){	
					Pstmt = connection.prepareStatement(sql);
					int ii = 1;
					Pstmt.setString(ii++, datetimeToday);//资料时间
					Pstmt.setString(ii++, sta);//记录标识
					resultSet = Pstmt.executeQuery();
					if(resultSet.next()){
						rymdhm = resultSet.getString(1);
						retrieval = resultSet.getString(2);
					}
				}
			}catch(SQLException e){
				infoLogger.error("\n findExistData create Statement error " + e.getMessage());
			}
			finally {
				if(Pstmt != null) {
					try {
						Pstmt.close();
					} catch (SQLException e) {
						infoLogger.error("\n findExistData close Statement error " + e.getMessage());
					}
				}
				if(resultSet != null){
					try{
						resultSet.close();
					}catch(SQLException e){
						infoLogger.error("\n findExistData close resultSet error " + e.getMessage());
					}
				}
			}
			rntString[0] = rymdhm;
			rntString[1] = retrieval;
			return rntString;
		}

	public static int deleteExistData(String stationname,String datetime,String tableName,Connection connection){
		Statement Pstmt = null;
		String deletesql = "delete from "+tableName+" "
				+ "where  D_DATETIME = "+ "'" +datetime+ "'" + " and V01301 =" + "'" +stationname+ "'";
		int dsql=0;
		try{
			if(connection != null){	
////				Pstmt = connection.prepareStatement(sql);
				Pstmt = connection.createStatement();

				dsql=Pstmt.executeUpdate(deletesql);
				connection.commit();
			}
		}catch(SQLException e){
			infoLogger.error("\n deleteExistData failed " + e.getMessage());
		}
		finally {
			if(Pstmt != null) {
				try {
					Pstmt.close();
				} catch (SQLException e) {
					infoLogger.error("\n deleteExistData close Statement error " + e.getMessage());
				}
			}
		}
		
		return dsql;
	}
	public static int updateExistData(Map<String, String> ColVal,String stationname,String datetime,String tableName,Connection connection){
		String pars="";
		if(!ColVal.isEmpty()) {
			for(String key:ColVal.keySet()) {
				pars=pars+ key+"="+ColVal.get(key)+",";
			}
		}
		pars=pars.substring(0, pars.length()-1);
		Statement Pstmt = null;
		String update = "update " + tableName + " set " + pars + "where  D_DATETIME = "+ "'" +datetime+ "'" + " and V01301 =" + "'" +stationname+ "'";
		int upsql=0;
		try{
			if(connection != null){	
////				Pstmt = connection.prepareStatement(sql);
				Pstmt = connection.createStatement();

				upsql=Pstmt.executeUpdate(update);
				connection.commit();
			}
		}catch(SQLException e){
			infoLogger.error("\n deleteExistData failed " + e.getMessage());
		}
		finally {
			if(Pstmt != null) {
				try {
					Pstmt.close();
				} catch (SQLException e) {
					infoLogger.error("\n deleteExistData close Statement error " + e.getMessage());
				}
			}
		}
		
		return upsql;
	}

}
