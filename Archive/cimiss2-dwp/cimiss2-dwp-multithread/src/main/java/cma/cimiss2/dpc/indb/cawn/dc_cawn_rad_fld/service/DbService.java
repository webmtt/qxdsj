package cma.cimiss2.dpc.indb.cawn.dc_cawn_rad_fld.service;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import cma.cimiss2.dpc.decoder.agme.PropertiesUtil;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.cawn.DecodeRadfld;
import cma.cimiss2.dpc.decoder.cawn.RadfldParseConfig;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

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
	public static int insertBatchSize = 500;
	
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
		PropertiesUtil.setConfigFile("config/RAD_FLD_setups.properties");
		RadfldParseConfig.config = "config/RAD_FLD_TAB.xml";
		DecodeRadfld decoderad = new DecodeRadfld();
		String filepath = "C:\\BaiduNetdiskDownload\\test\\G.0002.0011.R001\\G.0002.0011.R001\\20200315\\Z_CAWN_I_54084_20200315000000_O_RAD-FLD-MUL.TXT";
		File file = new File(filepath);
		Date recv_time = new Date(file.lastModified());
		ParseResult<String> parseResult = decoderad.decodeFile(filepath, ",", recv_time);
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
			for (int j = 0; j < listDi.size(); j++) {
				diQueues.offer(listDi.get(j));
			}
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
									String min="";
									String sec="";
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
										if(ColVal.containsKey("V04005"))
											min = ColVal.get("V04005");
										if(ColVal.containsKey("V04006"))
											sec = ColVal.get("V04006");
									}
								 
								String tableName= PropertiesUtil.getTableName();
								String ctsCode=PropertiesUtil.getCTSCode();
								int deletecount=0;
//								if (ctsCode.equals("G.0023.0003.R001")) {//环保部酸雨资料
//									 deletecount=deleteExistData1(sta,dt,min,sec,startdt,enddt,tableName,connection);//删除库里已有的数据
//								}else{//环保部气体 （国控/非国控）
									 deletecount=deleteExistData2(sta,dt,min,sec,tableName,connection);//删除库里已有的数据
//								}
								if(deletecount>0){
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
	public static int deleteExistData1(String stationname,String datetime,String min,String second,String startdate,String enddate,String tableName,Connection connection){
		Statement Pstmt = null;
		String deletesql = "delete from "+tableName+" "
				+ "where  D_DATETIME = "+ "'" +datetime+ "'" + " and V01301 =" + "'" +stationname+ "'" +" and v04005= "+ "'" +min+ "'" +" and v04006= "+ "'" +second+ "'" +" and V_starttime="+ "'" +startdate+ "'" +" and V_endtime="+ "'" +enddate+ "'   ";
		int dsql=0;
		try{
			if(connection != null){	
////				Pstmt = connection.prepareStatement(sql);
				Pstmt = connection.createStatement();

				dsql=Pstmt.executeUpdate(deletesql);
				connection.commit();
			}
		}catch(SQLException e){
			infoLogger.error("\n deleteExistData1 failed " + e.getMessage());
		}
		finally {
			if(Pstmt != null) {
				try {
					Pstmt.close();
				} catch (SQLException e) {
					infoLogger.error("\n deleteExistData1 close Statement error " + e.getMessage());
				}
			}
		}
		
		return dsql;
	}
	public static int deleteExistData2(String stationname,String datetime,String min,String second,String tableName,Connection connection){
		Statement Pstmt = null;
		String deletesql = "delete from "+tableName+" "
				+ "where  D_DATETIME = "+ "'" +datetime+ "'" + " and V01301 =" + "'" +stationname ;
		int dsql=0;
		try{
			if(connection != null){	
////				Pstmt = connection.prepareStatement(sql);
				Pstmt = connection.createStatement();

				dsql=Pstmt.executeUpdate(deletesql);
				connection.commit();
			}
		}catch(SQLException e){
			infoLogger.error("\n deleteExistData2 failed " + e.getMessage());
		}
		finally {
			if(Pstmt != null) {
				try {
					Pstmt.close();
				} catch (SQLException e) {
					infoLogger.error("\n deleteExistData2 close Statement error " + e.getMessage());
				}
			}
		}
		
		return dsql;
	}
}
