package cma.cimiss2.dpc.indb.upar.dc_upar_hkflash.service;

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
import java.util.concurrent.LinkedBlockingQueue;

import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import cma.cimiss2.dpc.decoder.agme.PropertiesUtil;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
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
		PropertiesUtil.setConfigFile("config/Light_3333_setups.properties");
		HKFlashParseConfig.config = "config/Light_3333_TAB.xml";
		DecodeHKFlash decodeHKFlash = new DecodeHKFlash();
		String filepath = "D:\\HUAXIN\\DataProcess\\闪电\\201802210412_lightning_3min_accum.txt";
		File file = new File(filepath);
		Date recv_time = new Date(file.lastModified());
		ParseResult<String> parseResult = decodeHKFlash.decodeFile(filepath, "\\s+", recv_time);
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
					if(va.length == 4){
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
					
					
					
					int idx = simpleDateFormat.format(recv_time).lastIndexOf(":");
					String tran_time = simpleDateFormat.format(recv_time).substring(0, idx);
					String date_time = datatime.substring(0, idx);
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
				infoLogger.error("Batch insert failed: \n" + e.getMessage());
				try{
					stmt.clearBatch();
					connection.rollback();
					connection.setAutoCommit(true);
					
					for(int j = i; j < sz + i; j ++){
						try{
							stmt.execute(sqls.get(j));
						}catch (Exception e1) {
							listDi.get(j).setPROCESS_STATE("0");
							infoLogger.error("Insert record one by one failed: \n" + e1.getMessage() + sqls.get(j));
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
}
