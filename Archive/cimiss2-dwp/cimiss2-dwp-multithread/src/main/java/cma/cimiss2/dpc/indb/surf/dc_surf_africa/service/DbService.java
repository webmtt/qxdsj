package cma.cimiss2.dpc.indb.surf.dc_surf_africa.service;

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
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidPooledConnection;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.surf.AfricaAWS;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>

 *
 * <p>
 * notes: 非洲援建资料数据解码入库（陆地自动站）
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
 * 2019年9月17日 上午9:27:05   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class DbService {
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	/**
	 * CHN 表的dpc code
	 */
	static String dpcType = "A.0012.0001.P010"; 
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
   /**
    * DI 队列
    */
    public static BlockingQueue<StatDi> diQueues;
    /**
     * 无观测任务时，默认赋值
     */
    static double missing = 999999;
    static double noTask = 999998;
    /**
     * @Title: setDiQueues   
     * @Description: set方法  
     * @param diQueues void      
     * @throws：
     */
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
    	DbService.diQueues = diQueues;
    }
    /**
     * 
     * @Title: getDiQueues   
     * @Description: get方法
     * @return BlockingQueue<StatDi>      
     * @throws：
     */
    public static BlockingQueue<StatDi> getDiQueues(){
    	return diQueues;
    }
/**
 * 
 * @Title: processSuccessReport   
 * @Description: 接卸结果入库方法   
 * @param pr
 * @param filePath
 * @param recv
 * @return DataBaseAction      
 * @throws：
 */
	public static DataBaseAction processSuccessReport(ParseResult<AfricaAWS> pr, String filePath, Date recv) {
		 DruidPooledConnection connection = null;
		 DruidPooledConnection connection_rpt = null;
        try {
        	connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
        	connection_rpt = ConnectionPoolFactory.getInstance().getConnection("cimiss");
            List<AfricaAWS> datas = pr.getData();
            // chy 2019-11-5 报告表
            
            insertData_CHN_REP(connection_rpt, pr, filePath, recv);
            // 批量插入
            DataBaseAction action = insertDBCHN(connection, datas, filePath, recv);
            action = insertDBGLB(connection, datas, filePath, recv);
//            action = insertDBPre(connection, datas, filePath, recv);
            if (DataBaseAction.SUCCESS == action) {
                return DataBaseAction.SUCCESS;
            } else {
                return DataBaseAction.INSERT_ERROR;
            }
            
        } catch (Exception e) {
            infoLogger.error(e.getMessage());
            return DataBaseAction.CONNECTION_ERROR;
        } finally {
            if (connection != null) {
                try {
					connection.close();
				} catch (SQLException e) {
					infoLogger.error(e.getMessage());
					e.printStackTrace();
				}
            }
            
            if(connection_rpt != null){
            	try{
            		connection_rpt.close();
            	}catch (Exception e) {
					infoLogger.error(e.getMessage());
					e.printStackTrace();
				}
            }
            
            for(int i = 0; i < listDi.size(); i ++)
            	diQueues.offer(listDi.get(i));
            listDi.clear();
        }

	}
	
	
	/**
	 * @Title: insertData_CHN_REP   
	 * @Description: 入库报告表
	 * @param connection
	 * @return int      
	 * @throws：
	 */
	public static int insertData_CHN_REP(DruidPooledConnection connection, ParseResult<AfricaAWS> pr, String filePath,
			Date recv){
		PreparedStatement prestmt = null;
//		String dpccode= "A.0001.0054.P001";
		String sodcode= "A.0001.0025.S004";
		String table_name = "SURF_WEA_CHN_REP_TAB";
		String sql =  "insert into " + table_name + "(D_RECORD_ID,"
				+ "D_DATETIME,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATA_ID,"
				+ "V_BBB,V_CCCC,V_TT,V01301,V01300,V05001,"
				+ "V06001,V_NCODE,V_ACODE,V04001,V04002,V04003,V04004,V04005,"
				+ "V_LEN,V_REPORT,D_SOURCE_ID)";   // 共23个
		sql += "?,values(to_date(?,'YYYY-MM-DD HH24:MI:SS'),to_date(?,'YYYY-MM-DD HH24:MI:SS'),to_date(?,'YYYY-MM-DD HH24:MI:SS'),to_date(?,'YYYY-MM-DD HH24:MI:SS'),"
				+ "?,"
				+ "?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,"
				+ "?,?,?)";
		int ret = 0;
		List<String> sqls = new ArrayList<String>();
		@SuppressWarnings("rawtypes")
		List<ReportInfo> rpt = pr.getReports();
//		List<AfricaAWS> datas = pr.getData();
		try {
			
			prestmt = new LoggableStatement(connection,sql);
			connection.setAutoCommit(false);
			Map<String, Object> proMap = StationInfo.getProMap();
			infoLogger.info("#Report: " + pr.size());
			
			for (int i = 0; i < rpt.size(); i++) {
//				AfricaAWS surfAfricaModel = datas.get(i);
				AfricaAWS surfAfricaModel = (AfricaAWS)rpt.get(i).getT();
				String station = surfAfricaModel.getStationNumberChina();
				Date date = surfAfricaModel.getObserDateTime();
				
				int adminCode = (int)missing;
				int ncode = (int)missing;
				Double latitude = surfAfricaModel.getLatitude(); // 纬度
				Double longitude = surfAfricaModel.getLongitude(); // 经度
				Double height = surfAfricaModel.getHeightOfSationGroundAboveMeanSeaLevel(); // 测站海拔高度
				String info = (String) proMap.get(station + "+01");
				if(info != null ){
					String[] infos = info.split(",");
					// 如果 经、纬度、测站高度为缺测，表示该种格式的报文中没有台站信息，需要从lua配置文件读取
					if (latitude == 999999.0 || longitude == 999999.0 || height == 999999.0) {
						try{
							if(infos.length >= 3 && infos[2] != null)
								latitude = Double.parseDouble(infos[2]);// 纬度
							if(infos.length >= 2 && infos[1] != null)
								longitude = Double.parseDouble(infos[1]); // 经度
							if(infos.length >= 4 && infos[3] != null)
								height = Double.parseDouble(infos[3]); // 测站海拔高度
						}catch (Exception e) {
							infoLogger.error("Read latitude | longitude | stationHeight error!");
						}
						surfAfricaModel.setLatitude(latitude);
						surfAfricaModel.setLongitude(longitude);
						surfAfricaModel.setHeightOfSationGroundAboveMeanSeaLevel(height);
					}
					try{
						ncode = Integer.parseInt(infos[5]);  // 省代码
					}catch (Exception e) {
						infoLogger.info("StationClass error!");
					}
				}
				int ii = 1;
//				+ "D_DATETIME,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATA_ID,V_BBB,V01301,V01300,V05001,"
				prestmt.setString(ii++, station + "_" + sdf.format(date));
				prestmt.setString(ii++, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
				prestmt.setString(ii++, TimeUtil.getSysTime());
				prestmt.setString(ii++, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(recv));
				
				prestmt.setString(ii++, TimeUtil.getSysTime());
				
				prestmt.setString(ii++, sodcode);
//				+ "V_BBB,V_CCCC,V_TT,V01301,V01300,V05001,"
				prestmt.setString(ii++, surfAfricaModel.getCorrectSign());
				prestmt.setString(ii++, "9999");
				prestmt.setString(ii++, "REG");
				prestmt.setString(ii++, station);
				prestmt.setInt(ii++, (int)noTask);
				prestmt.setBigDecimal(ii++, new BigDecimal(latitude).setScale(4, BigDecimal.ROUND_HALF_UP));
//				+ "V06001,V_NCODE,V_ACODE,V04001,V04002,V04003,V04004,V04005,"
				prestmt.setBigDecimal(ii++, new BigDecimal(longitude).setScale(4, BigDecimal.ROUND_HALF_UP));
				prestmt.setInt(ii++, ncode);
				prestmt.setInt(ii++, adminCode);
				prestmt.setInt(ii++, date.getYear() + 1900);
				prestmt.setInt(ii++, date.getMonth() + 1);
				prestmt.setInt(ii++, date.getDate());
				prestmt.setInt(ii++, date.getHours());
				prestmt.setInt(ii++, date.getMinutes());
//				 + "V_LEN,V_REPORT,D_DATA_DPCID)";   
				prestmt.setDouble(ii++, rpt.get(i).getReport().length()); // 
				prestmt.setString(ii++, rpt.get(i).getReport());
				prestmt.setString(ii++, StartConfig.ctsCode());
			
				prestmt.addBatch();
				sqls.add(((LoggableStatement)prestmt).getQueryString());
//				System.out.println(((LoggableStatement)prestmt).getQueryString());
				
			}
			prestmt.executeBatch();
			connection.commit();
			infoLogger.info("Insert record num: " + rpt.size());
			infoLogger.info("\r\n " +"Batch commit succ!");
			ret = 1;
		}catch (SQLException e) {
			// 如果出错，则此次executeBatch()的所有数据都不入库
			infoLogger.error("\r\n " +"Batch commit failed!" + filePath + e.getMessage());
          	try {
				connection.rollback();
			} catch (SQLException e1) {
				infoLogger.error("\r\n " +"Batch commit failed!");
			}
          	Statement stmt = null;
			try {
				stmt = connection.createStatement();
				connection.setAutoCommit(true);
				// 逐条入库
				int t = rpt.size() * 60;
              	for(int i = 0; i < sqls.size(); i++){
              		try {
						stmt.execute(sqls.get(i));
						ret = 1; //有一条入库即为入库成功
						infoLogger.info("Insert one by one: insert one record succ!");
					} catch (SQLException e1) {
						t --;
						infoLogger.error("\r\n " +"Insert one by one failed: " + sqls.get(i));
						continue;
					}
              	}
              	infoLogger.info("Insert record: " + t);
			} catch (SQLException e1) {
				infoLogger.error("\r\n " +"Create Statement error!");
			}finally {
          		if(stmt != null)
          			try{
          				stmt.close();
          			}
				 catch (SQLException e1) {
					e1.printStackTrace();
					infoLogger.error("\r\n " +"Close statement error!");
				 }
          	}
		}finally {
			if (prestmt != null)
				try {
					prestmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
					infoLogger.error("\r\n " +"Close statement error!");
				}
		}
		if(ret == 1)
			return 1; // 成功
		else 
			return 0; // 失败
	}
	
	
/**
 * 
 * @Title: insertDBPre   
 * @Description: 分钟降水表入库
 * @param connection
 * @param datas
 * @param filePath
 * @param recv
 * @return DataBaseAction      
 * @throws：
 */
private static DataBaseAction insertDBPre(DruidPooledConnection connection, List<AfricaAWS> datas, String filePath,
		Date recv) {
	PreparedStatement prestmt = null;
	String dpccode= "A.0010.0001.P004";
	String sodcode= "A.0010.0001.S001";
	String table_name = "SURF_WEA_CHN_PRE_MIN_TAB";
	String sql =  "insert into " + table_name + "("
			+ "D_RECORD_ID,D_DATETIME,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATA_ID,"
			+ "V_BBB,V01301,V01300,V05001,"
			+ "V06001,V07001,V02001,V02301,V_ACODE,V04001,V04002,V04003,V04004,V04005,"
			+ "V13011,Q13011,V07032_03,V08010,V02175,D_SOURCE_ID)";   // 共26个
	sql += "values(?,to_date(?,'YYYY-MM-DD HH24:MI:SS'),to_date(?,'YYYY-MM-DD HH24:MI:SS'),to_date(?,'YYYY-MM-DD HH24:MI:SS'),to_date(?,'YYYY-MM-DD HH24:MI:SS'),"
			+ "?,?,?,?,?,"
			+ "?,?,?,?,?,?,?,?,?,?,"
			+ "?,?,?,?,?,?)";
	int ret = 0;
	List<String> sqls = new ArrayList<String>();
	try {
		prestmt = new LoggableStatement(connection,sql);
		connection.setAutoCommit(false);
		infoLogger.info("Parsed #records: " + datas.size());
		Map<String, Object> proMap = StationInfo.getProMap();
		
		for (int i = 0; i < datas.size(); i++) {
			AfricaAWS surfAfricaModel = datas.get(i);
			String station = surfAfricaModel.getStationNumberChina();
			Date date = surfAfricaModel.getObserDateTime();
			if(!(date.getMinutes() == 0 && date.getSeconds() == 0) && surfAfricaModel.getRainMinutely() != null){
				// 不是整点，不入降水表
				infoLogger.info("Report Datetime is not at xZ o'clock");
				continue;
			}
			String info = (String) proMap.get(station + "+01");
			int adminCode = (int)noTask;
			int stationClass = (int)noTask;
			Double latitude = surfAfricaModel.getLatitude(); // 纬度
			Double longitude = surfAfricaModel.getLongitude(); // 经度
			Double height = surfAfricaModel.getHeightOfSationGroundAboveMeanSeaLevel(); // 测站海拔高度
			if(info != null ){
				String[] infos = info.split(",");
				// 如果 经、纬度、测站高度为缺测，表示该种格式的报文中没有台站信息，需要从lua配置文件读取
				if (latitude == 999999.0 || longitude == 999999.0 || height == 999999.0) {
					try{
						if(infos.length >= 3 && infos[2] != null)
							latitude = Double.parseDouble(infos[2]);// 纬度
						if(infos.length >= 2 && infos[1] != null)
							longitude = Double.parseDouble(infos[1]); // 经度
						if(infos.length >= 4 && infos[3] != null)
							height = Double.parseDouble(infos[3]); // 测站海拔高度
					}catch (Exception e) {
						infoLogger.error("Read latitude | longitude | stationHeight error!");
					}
					surfAfricaModel.setLatitude(latitude);
					surfAfricaModel.setLongitude(longitude);
					surfAfricaModel.setHeightOfSationGroundAboveMeanSeaLevel(height);
				}
				try{
					stationClass = Integer.parseInt(infos[6]); // 台站级别
				}catch (Exception e) {
					infoLogger.info("Admin code or stationClass error!");
				}
			}
			
			double rain[] = surfAfricaModel.getRainMinutely();
			for(int t = 0; t < 60; t ++){
				int ii = 1;
				Date date2 = new Date();
				date2.setTime(date.getTime());
				date2.setMinutes(t);
//				+ "D_DATETIME,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATA_ID,V_BBB,V01301,V01300,V05001,"
				prestmt.setString(ii++, station + "_" + sdf.format(date2));
				prestmt.setString(ii++, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date2));
				prestmt.setString(ii++, TimeUtil.getSysTime());
				prestmt.setString(ii++, sdf2.format(recv));
				prestmt.setString(ii++, TimeUtil.getSysTime());
				prestmt.setString(ii++, sodcode);
				prestmt.setString(ii++, surfAfricaModel.getCorrectSign());
				prestmt.setString(ii++, station);
				prestmt.setInt(ii++, (int)noTask);
				prestmt.setBigDecimal(ii++, new BigDecimal(latitude).setScale(4, BigDecimal.ROUND_HALF_UP));
//				+ "V06001,V07001,V02001,V02301,V_ACODE,V04001,V04002,V04003,V04004,V04005,"
				prestmt.setBigDecimal(ii++, new BigDecimal(longitude).setScale(4, BigDecimal.ROUND_HALF_UP));
				prestmt.setBigDecimal(ii++, new BigDecimal(height).setScale(4, BigDecimal.ROUND_HALF_UP));
				prestmt.setInt(ii++, 0); //0；无人自动站
				prestmt.setInt(ii++, stationClass); // 台站级别
				prestmt.setInt(ii++, adminCode);
				prestmt.setInt(ii++, date2.getYear() + 1900);
				prestmt.setInt(ii++, date2.getMonth() + 1);
				prestmt.setInt(ii++, date2.getDate());
				prestmt.setInt(ii++, date2.getHours());
				prestmt.setInt(ii++, date2.getMinutes());
//				+ "V13011,Q13011,D_DATA_DPCID,V07032_03,V08010,V02175)";  
				prestmt.setDouble(ii++, rain[t]); // 分钟降水量 V13011
				prestmt.setInt(ii++, 9);
//				prestmt.setString(ii++, dpccode);
				prestmt.setDouble(ii++, noTask); //降水传感器离本地地面（或海上平台甲板）的高度
				prestmt.setDouble(ii++, noTask); // 地面状况
				prestmt.setDouble(ii++, noTask); // 降水测量方法
				prestmt.setString(ii++, StartConfig.ctsCode());
				
				prestmt.addBatch();
				sqls.add(((LoggableStatement)prestmt).getQueryString());
//				System.out.println(((LoggableStatement)prestmt).getQueryString());
			}
		}
		prestmt.executeBatch();
		connection.commit();
		infoLogger.info("Insert record num: " + datas.size());
		infoLogger.info("\r\n " +"Batch commit succ!");
		ret = 1;
	}catch (SQLException e) {
		// 如果出错，则此次executeBatch()的所有数据都不入库
		infoLogger.error("\r\n " +"Batch commit failed!" + filePath + e.getMessage());
      	try {
			connection.rollback();
		} catch (SQLException e1) {
			infoLogger.error("\r\n " +"Batch commit failed!");
		}
      	Statement stmt = null;
		try {
			stmt = connection.createStatement();
			connection.setAutoCommit(true);
			// 逐条入库
			int t = datas.size() * 60;
          	for(int i = 0; i < sqls.size(); i++){
          		try {
					stmt.execute(sqls.get(i));
					ret = 1; //有一条入库即为入库成功
					infoLogger.info("Insert one by one: insert one record succ!");
				} catch (SQLException e1) {
					t --;
					infoLogger.error("\r\n " +"Insert one by one failed: " + sqls.get(i));
					continue;
				}
          	}
          	infoLogger.info("Insert record: " + t);
		} catch (SQLException e1) {
			infoLogger.error("\r\n " +"Create Statement error!");
		}finally {
      		if(stmt != null)
      			try{
      				stmt.close();
      			}
			 catch (SQLException e1) {
				e1.printStackTrace();
				infoLogger.error("\r\n " +"Close statement error!");
			 }
      	}
	}finally {
		if (prestmt != null)
			try {
				prestmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				infoLogger.error("\r\n " +"Close statement error!");
			}
	}
	if(ret == 1)
		return DataBaseAction.SUCCESS; // 成功
	else 
		return DataBaseAction.INSERT_ERROR; // 失败
}
/**
 * 
 * @Title: insertDBGLB   
 * @Description: 全球地面小时表入库
 * @param connection
 * @param datas
 * @param filePath
 * @param recv
 * @return DataBaseAction      
 */
private static DataBaseAction insertDBGLB(DruidPooledConnection connection, List<AfricaAWS> datas, String filePath, Date recv) {
	PreparedStatement prestmt = null;
	String sodcode = "A.0013.0001.S001";
	String dpccode = "A.0013.0001.P018";
	String table_name = "SURF_WEA_GLB_MUL_HOR_TAB";
	String sql =  "insert into " + table_name + "("
			+ "D_RECORD_ID,D_DATETIME,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,"
			+ "D_DATA_ID,V_BBB,V04001_02,V04002_02,V04003_02,"
			+ "V04004_02,V01301,V01300,V05001,V06001,V07001,V07031,V07032_01,V07032_02,V07032_03,"
			+ "V07032_04,V02002,V02004,V02001,V_NCODE,V01015,V02301,V04001,V04002,V04003,"
			+ "V04004,V04005,V20013,V20001,V20010,V11001,V11002,V12001,V12003,V13003,"
			+ "V10004,V10051,V07004,V10009,V10063,V10061,V13019,V13011_02,V13020,V13021,"
			+ "V13011_09,V13022,V13011_15,V13011_18,V13023,V20003,V20004,V20005,V20011,V20350_11,"
			+ "V20350_12,V20350_13,V10062,V12405,V12014,V12016,V12015,V12017,V12013,V20062,"
			+ "V13013,V13340,V14032_01,V14032_24,V14016_01,V14015,V14021_01,V14020,V14023_01,V14022,"
			+ "V14002_01,V14001,V14004_01,V14003,V14025_01,V14024,V20054_01,V20054_02,V20054_03,V20012,"
			+ "V05021,V07021,V20012_01,V20011_01,V20013_01,V20012_02,V20011_02,V20013_02,V20012_03,V20011_03,"
			+ "V20013_03,V20012_04,V20011_04,V20013_04,V20063_01,V20063_02,V20063_03,D_DATA_DPCID,V_TT,C_CCCC,"
			+ "D_SOURCE_ID)";   // 共111个
	sql += "?,values(to_date(?,'YYYY-MM-DD HH24:MI:SS'),to_date(?,'YYYY-MM-DD HH24:MI:SS'),to_date(?,'YYYY-MM-DD HH24:MI:SS'),to_date(?,'YYYY-MM-DD HH24:MI:SS'),"
			+ "?,?,?,?,?,"
			+ "?,?,?,?,?,?,?,?,?,?,"
			+ "?,?,?,?,?,?,?,?,?,?,"
			+ "?,?,?,?,?,?,?,?,?,?,"
			+ "?,?,?,?,?,?,?,?,?,?,"
			+ "?,?,?,?,?,?,?,?,?,?,"
			+ "?,?,?,?,?,?,?,?,?,?,"
			+ "?,?,?,?,?,?,?,?,?,?,"
			+ "?,?,?,?,?,?,?,?,?,?,"
			+ "?,?,?,?,?,?,?,?,?,?,"
			+ "?,?,?,?,?,?,?,?,?,?,"
			+ "?)";  
	int ret = 0;
	List<String> sqls = new ArrayList<String>();
	try {
		prestmt = new LoggableStatement(connection,sql);
		connection.setAutoCommit(false);
		Map<String, Object> proMap = StationInfo.getProMap();
		infoLogger.info("Parsed #records: " + datas.size());
		for (int i = 0; i < datas.size(); i++) {
			AfricaAWS surfAfricaModel = datas.get(i);
			String station = surfAfricaModel.getStationNumberChina();
			Date date = surfAfricaModel.getObserDateTime();
			String info = (String) proMap.get(station + "+01");
			int stationClass = (int)missing;
			int ncode = (int)missing;
			Double latitude = surfAfricaModel.getLatitude(); // 纬度
			Double longitude = surfAfricaModel.getLongitude(); // 经度
			Double height = surfAfricaModel.getHeightOfSationGroundAboveMeanSeaLevel(); // 测站海拔高度
			if(info != null ){
				String[] infos = info.split(",");
				// 如果 经、纬度、测站高度为缺测，表示该种格式的报文中没有台站信息，需要从lua配置文件读取
				if (latitude == 999999.0 || longitude == 999999.0 || height == 999999.0) {
					try{
						if(infos.length >= 3 && infos[2] != null)
							latitude = Double.parseDouble(infos[2]);// 纬度
						if(infos.length >= 2 && infos[1] != null)
							longitude = Double.parseDouble(infos[1]); // 经度
						if(infos.length >= 4 && infos[3] != null)
							height = Double.parseDouble(infos[3]); // 测站海拔高度
					}catch (Exception e) {
						infoLogger.error("Read latitude | longitude | stationHeight error!");
					}
					surfAfricaModel.setLatitude(latitude);
					surfAfricaModel.setLongitude(longitude);
					surfAfricaModel.setHeightOfSationGroundAboveMeanSeaLevel(height);
				}
				try{
					stationClass = Integer.parseInt(infos[6]); // 台站级别
					ncode = Integer.parseInt(infos[5]);  // 省代码
				}catch (Exception e) {
					infoLogger.info("Admin code or stationClass error!");
				}
			}
			
			int ii = 1;
//			+ "D_DATETIME,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATA_ID,V_BBB,V04001_02,V04002_02,V04003_02,"
			prestmt.setString(ii++, station + "_" + sdf.format(date));
			prestmt.setString(ii++, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
			prestmt.setString(ii++, TimeUtil.getSysTime());
			prestmt.setString(ii++, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(recv));
			prestmt.setString(ii++, TimeUtil.getSysTime());
			prestmt.setString(ii++, sodcode);
			prestmt.setString(ii++, surfAfricaModel.getCorrectSign());
			prestmt.setInt(ii++, date.getYear() + 1900);
			prestmt.setInt(ii++, date.getMonth() + 1);
			prestmt.setInt(ii++, date.getDate());
//			+ "V04004_02,V01301,V01300,V05001,V06001,V07001,V07031,V07032_01,V07032_02,V07032_03,"
			prestmt.setInt(ii++, date.getHours());
			prestmt.setString(ii++, station);
			prestmt.setInt(ii++, 999998);
			prestmt.setBigDecimal(ii++, new BigDecimal(latitude).setScale(4, BigDecimal.ROUND_HALF_UP));
			prestmt.setBigDecimal(ii++, new BigDecimal(longitude).setScale(4, BigDecimal.ROUND_HALF_UP));
			prestmt.setBigDecimal(ii++, new BigDecimal(height).setScale(4, BigDecimal.ROUND_HALF_UP));
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getHeightOfBaromSensor()).setScale(4, BigDecimal.ROUND_HALF_UP));
			prestmt.setDouble(ii++, noTask);//风速传感器距地面高度
			prestmt.setDouble(ii++, noTask);
			prestmt.setDouble(ii++, noTask);
//			+ "V07032_04,V02002,V02004,V02001,V_NCODE,V01015,V02301,V04001,V04002,V04003,"
			prestmt.setDouble(ii++, noTask); 
			prestmt.setDouble(ii++, noTask);//测风仪的仪器类型
			prestmt.setDouble(ii++, noTask);//测量蒸发的仪器类型或作物类型
			prestmt.setInt(ii++, 0); //0：无人自动站
			prestmt.setInt(ii++, ncode);
			prestmt.setString(ii++, String.valueOf(noTask));
			prestmt.setInt(ii++, stationClass);
			prestmt.setInt(ii++, date.getYear() + 1900);
			prestmt.setInt(ii++, date.getMonth() + 1);
			prestmt.setInt(ii++, date.getDate());
			
//			+ "V04004,V04005,V20013,V20001,V20010,V11001,V11002,V12001,V12003,V13003,"
			prestmt.setInt(ii++, date.getHours());
			prestmt.setInt(ii++, date.getMinutes());
			prestmt.setDouble(ii++, surfAfricaModel.getHeightOfLowOrMidCloudHourly());//低云或中云的云高
			prestmt.setDouble(ii++, surfAfricaModel.getVisibility());// 水平能见度	V20001
			prestmt.setDouble(ii++, surfAfricaModel.getCloudAmountHourly());// 总云量	V20010
			prestmt.setDouble(ii++, surfAfricaModel.getWindDir());// 风向	V11001
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getWindSpd()).setScale(4, BigDecimal.ROUND_HALF_UP));// 风速	V11002
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getTemperature()).setScale(4, BigDecimal.ROUND_HALF_UP));//	 气温	V12001
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getDewPoint()).setScale(4, BigDecimal.ROUND_HALF_UP));  //露点温度
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getRelativeHumid()).setScale(4, BigDecimal.ROUND_HALF_UP));// 相对湿度
//			+ "V10004,V10051,V07004,V10009,V10063,V10061,V13019,V13011_02,V13020,V13021,"
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getStationPress()).setScale(4, BigDecimal.ROUND_HALF_UP));//	 本站气压	V10004
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getSeaLevelPress()).setScale(4, BigDecimal.ROUND_HALF_UP));// 海平面气压	V10051
			prestmt.setDouble(ii++, noTask);// 气压（测站最接近的标准层）	V07004
			prestmt.setDouble(ii++, noTask);//	位势高度（标准层）	V10009
			prestmt.setDouble(ii++, noTask);// 气压倾向的特征	V10063
			prestmt.setDouble(ii++, noTask);// 3小时变压	V10061
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getRainAmountHourly()).setScale(4, BigDecimal.ROUND_HALF_UP));//	 过去1小时降水量	V13019
			prestmt.setDouble(ii++, noTask);// 过去2小时降水量	V13011_02
			prestmt.setDouble(ii++, noTask);// 过去3小时降水量	V13020
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getRainAmount_6Hour()).setScale(4, BigDecimal.ROUND_HALF_UP));//	 过去6小时降水量	V13021
//			+ "V13011_09,V13022,V13011_15,V13011_18,V13023,V20003,V20004,V20005,V20011,V20350_11,"
			prestmt.setDouble(ii++, noTask);// 过去9小时降水量	V13011_09
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getRainAmount_12Hour()).setScale(4, BigDecimal.ROUND_HALF_UP));//	 过去12小时降水量	V13022
			prestmt.setDouble(ii++, noTask);//	 过去15小时降水量	V13011_15
			prestmt.setDouble(ii++, noTask);//	 过去18小时降水量	V13011_18
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getRainAmount_24Hour()).setScale(4, BigDecimal.ROUND_HALF_UP));//	 过去24小时降水量	V13023
			prestmt.setDouble(ii++, surfAfricaModel.getWeatherPheno());//	 现在天气	V20003
			prestmt.setDouble(ii++, noTask);//	 过去天气1	V20004
			prestmt.setDouble(ii++, noTask);//	 过去天气2	V20005
			prestmt.setDouble(ii++, surfAfricaModel.getLowOrMidCloudAmountHourly());//	 云量(低云或中云)	V20011
			prestmt.setDouble(ii++, noTask);// 低云状	V20350_11
//			+ "V20350_12,V20350_13,V10062,V12405,V12014,V12016,V12015,V12017,V12013,V20062,"
			prestmt.setDouble(ii++, noTask);//	 中云状	V20350_12
			prestmt.setDouble(ii++, noTask);//	 高云状	V20350_13
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getPressChange_24Hour()).setScale(4, BigDecimal.ROUND_HALF_UP));//	 24小时变压	V10062
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getTemChange_24Hour()).setScale(4, BigDecimal.ROUND_HALF_UP));//	 过去24小时变温	V12405
			prestmt.setDouble(ii++, noTask);//	 过去12小时最高气温	V12014
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getMaxTemperature_24Hour()).setScale(4, BigDecimal.ROUND_HALF_UP));//	 过去24小时最高气温	V12016
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getMinTemperature_12Hour()).setScale(4, BigDecimal.ROUND_HALF_UP));//	 过去12小时最低气温	V12015
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getMinTemperature_24Hour()).setScale(4, BigDecimal.ROUND_HALF_UP));//	 过去24小时最低气温	V12017
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getMinTemperature_12Hour()).setScale(4, BigDecimal.ROUND_HALF_UP));//	 过去12小时地面最低温度	V12013
			prestmt.setDouble(ii++, surfAfricaModel.getGroundState_6Hour()); //地面状态	V20062
//			+ "V13013,V13340,V14032_01,V14032_24,V14016_01,V14015,V14021_01,V14020,V14023_01,V14022,"
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getSnowDepth_0_6_12Hour()).setScale(4, BigDecimal.ROUND_HALF_UP));// 积雪深度	V13013
			prestmt.setDouble(ii++, noTask);//日蒸发量	V13340
			prestmt.setDouble(ii++, noTask);//过去1小时日照时数	V14032_01
			prestmt.setDouble(ii++, noTask);//过去24小时日照时数	V14032_24
			prestmt.setDouble(ii++, noTask);//过去1小时净辐射	V14016_01
			prestmt.setDouble(ii++, noTask);//过去24小时净辐射	V14015
			prestmt.setDouble(ii++, noTask);//过去1小时总太阳辐射	V14021_01
			prestmt.setDouble(ii++, noTask);//过去24小时总太阳辐射	V14020
			prestmt.setDouble(ii++, noTask);//过去1小时漫射太阳辐射	V14023_01
			prestmt.setDouble(ii++, noTask);//过去24小时漫射太阳辐射	V14022
			//+ "V14002_01,V14001,V14004_01,V14003,V14025_01,V14024,V20054_01,V20054_02,V20054_03,V20012,"
			prestmt.setDouble(ii++, noTask);//过去1小时长波辐射	V14002_01
			prestmt.setDouble(ii++, noTask);//过去24小时长波辐射	V14001
			prestmt.setDouble(ii++, noTask);//过去1小时短波辐射	V14004_01
			prestmt.setDouble(ii++, noTask);//过去24小时短波辐射	V14003
			prestmt.setDouble(ii++, noTask);//过去1小时直接太阳辐射	V14025_01
			prestmt.setDouble(ii++, noTask);//过去24小时直接太阳辐射	V14024
			prestmt.setDouble(ii++, noTask);//低云移向	V20054_01
			prestmt.setDouble(ii++, noTask);//中云移向	V20054_02
			prestmt.setDouble(ii++, noTask);//高云移向	V20054_03
			prestmt.setDouble(ii++, noTask);//地形云或垂直发展云的云属	V20012
			//+ "V05021,V07021,V20012_01,V20011_01,V20013_01,V20012_02,V20011_02,V20013_02,V20012_03,V20011_03,"
			prestmt.setDouble(ii++, noTask);//地形云或垂直发展云的方位	V05021
			prestmt.setDouble(ii++, noTask);//地形云或垂直发展云的高度角	V07021
			prestmt.setDouble(ii++, noTask);//云属1	V20012_01
			prestmt.setDouble(ii++, noTask);//云属1的云量	V20011_01
			prestmt.setDouble(ii++, noTask);//云属1的云高	V20013_01
			prestmt.setDouble(ii++, noTask);//云属2	V20012_02
			prestmt.setDouble(ii++, noTask);//云属2的云量	V20011_02
			prestmt.setDouble(ii++, noTask);//云属2的云高	V20013_02
			prestmt.setDouble(ii++, noTask);//云属3	V20012_03
			prestmt.setDouble(ii++, noTask);//云属3的云量	V20011_03
//			+ "V20013_03,V20012_04,V20011_04,V20013_04,V20063_01,V20063_02,V20063_03,D_DATA_DPCID,V_TT,C_CCCC,"
			prestmt.setDouble(ii++, noTask);//云属3的云高	V20013_03
			prestmt.setDouble(ii++, noTask);//积雨云的云属	V20012_04
			prestmt.setDouble(ii++, noTask);//积雨云的云量	V20011_04
			prestmt.setDouble(ii++, noTask);//积雨云的云高	V20013_04
			prestmt.setDouble(ii++, noTask);//特殊天气现象1	V20063_01
			prestmt.setDouble(ii++, noTask);//特殊天气现象2	V20063_02
			prestmt.setDouble(ii++, noTask);//特殊天气现象3	V20063_03
			prestmt.setString(ii++, dpccode);
			prestmt.setString(ii++, String.valueOf(noTask));
			prestmt.setString(ii++, String.valueOf(noTask));
//			+ "V_STT)";
//			prestmt.setString(ii++, String.valueOf(noTask));
			prestmt.setString(ii++, StartConfig.ctsCode());
				
			prestmt.addBatch();
			sqls.add(((LoggableStatement)prestmt).getQueryString());
//			System.out.println(((LoggableStatement)prestmt).getQueryString());
		}
		prestmt.executeBatch();
		connection.commit();
		infoLogger.info("Insert record num: " + datas.size());
		infoLogger.info("\r\n " +"Batch commit succ!");
		ret = 1;
	}catch (SQLException e) {
		// 如果出错，则此次executeBatch()的所有数据都不入库
		infoLogger.error("\r\n " +"Batch commit failed!" + filePath + e.getMessage());
      	try {
			connection.rollback();
		} catch (SQLException e1) {
			infoLogger.error("\r\n " +"Batch commit failed!");
		}
      	Statement stmt = null;
		try {
			stmt = connection.createStatement();
			connection.setAutoCommit(true);
			// 逐条入库
			int t = datas.size();
          	for(int i = 0; i < sqls.size(); i++){
          		try {
					stmt.execute(sqls.get(i));
					ret = 1; //有一条入库即为入库成功
					infoLogger.info("Insert one by one: insert one record succ!");
				} catch (SQLException e1) {
					boolean isInserted = deleteAndInsert(datas.get(i).getObserDateTime(), datas.get(i).getStationNumberChina(), 
							table_name, connection, datas.get(i).getCorrectSign(), sqls.get(i));
					if(!isInserted){
						t --;
						listDi.get(i).setPROCESS_STATE("0");
						infoLogger.error("\r\n " +"Insert one by one failed: " + sqls.get(i));
						continue;
					}
				}
          	}
          	infoLogger.info("Insert record: " + t);
		} catch (SQLException e1) {
			infoLogger.error("\r\n " +"Create Statement error!");
		}finally {
      		if(stmt != null)
      			try{
      				stmt.close();
      			}
			 catch (SQLException e1) {
				e1.printStackTrace();
				infoLogger.error("\r\n " +"Close statement error!");
			 }
      	}
	}finally {
		if (prestmt != null)
			try {
				prestmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				infoLogger.error("\r\n " +"Close statement error!");
			}
	}
	if(ret == 1)
		return DataBaseAction.SUCCESS; // 成功
	else
		return DataBaseAction.INSERT_ERROR;
}
/**
 * 
 * @Title: insertDBCHN   
 * @Description: 国家站地面小时表入库
 * @param connection
 * @param datas
 * @param filePath
 * @param recv
 * @return DataBaseAction      
 * @throws：
 */
private static DataBaseAction insertDBCHN(DruidPooledConnection connection, List<AfricaAWS> datas, String filePath,
		Date recv) {

	PreparedStatement prestmt = null;
	String table_name = "SURF_WEA_CHN_MUL_HOR_TAB";
	String sql =  "insert into " + table_name + "("
			+ "D_RECORD_ID,D_DATETIME,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATA_ID,V_BBB,V01301,V01300,V05001,"
			+ "V06001,V07001,V07031,V07032_04,V02001,V02301,V_ACODE,V04001,V04002,V04003,"
			+ "V04004,V10004,V10051,V10061,V10062,V10301,V10301_052,V10302,V10302_052,V12001,"
			+ "V12011,V12011_052,V12012,V12012_052,V12405,V12016,V12017,V12003,V13003,V13007,"
			+ "V13007_052,V13004,V13019,V13020,V13021,V13022,V13023,V04080_04,V13011,V13033,"
			+ "V11290,V11291,V11292,V11293,V11296,V11042,V11042_052,V11201,V11202,V11211,"
			+ "V11046,V11046_052,V11503_06,V11504_06,V11503_12,V11504_12,V12120,V12311,V12311_052,V12121,"
			+ "V12121_052,V12013,V12030_005,V12030_010,V12030_015,V12030_020,V12030_040,V12030_080,V12030_160,V12030_320,"
			+ "V12314,V12315,V12315_052,V12316,V12316_052,V20001_701_01,V20001_701_10,V20059,V20059_052,V20001,"
			+ "V20010,V20051,V20011,V20013,V20350_01,V20350_02,V20350_03,V20350_04,V20350_05,V20350_06,"
			+ "V20350_07,V20350_08,V20350_11,V20350_12,V20350_13,V20003,V04080_05,V20004,V20005,V20062,"
			+ "V13013,V13330,V20330_01,V20331_01,V20330_02,V20331_02,V08010,V07032_01,V07032_02,V02183,"
			+ "D_SOURCE_ID)";   // 共122个
	sql += "values(?,to_date(?,'YYYY-MM-DD HH24:MI:SS'),to_date(?,'YYYY-MM-DD HH24:MI:SS'),to_date(?,'YYYY-MM-DD HH24:MI:SS'),to_date(?,'YYYY-MM-DD HH24:MI:SS'),"
			+ "?,?,?,?,?,"
			+ "?,?,?,?,?,?,?,?,?,?,"
			+ "?,?,?,?,?,?,?,?,?,?,"
			+ "?,?,?,?,?,?,?,?,?,?,"
			+ "?,?,?,?,?,?,?,?,?,?,"
			+ "?,?,?,?,?,?,?,?,?,?,"
			+ "?,?,?,?,?,?,?,?,?,?,"
			+ "?,?,?,?,?,?,?,?,?,?,"
			+ "?,?,?,?,?,?,?,?,?,?,"
			+ "?,?,?,?,?,?,?,?,?,?,"
			+ "?,?,?,?,?,?,?,?,?,?,"
			+ "?,?,?,?,?,?,?,?,?,?,"
			+ "?)";  
	int ret = 0;
	List<String> sqls = new ArrayList<String>();
	try {
		prestmt = new LoggableStatement(connection,sql);
		connection.setAutoCommit(false);
		infoLogger.info("Parsed #records: " + datas.size());
		int shape[] = {(int)noTask, (int)noTask, (int)noTask, (int)noTask, (int)noTask, (int)noTask, (int)noTask, (int)noTask};
		Map<String, Object> proMap = StationInfo.getProMap();
		for (int i = 0; i < datas.size(); i++) {
			AfricaAWS surfAfricaModel = datas.get(i);
			StatDi di = new StatDi();	
			di.setFILE_NAME_O(new File(filePath).getName());
			di.setDATA_TYPE(StartConfig.sodCode());
			di.setDATA_TYPE_1(StartConfig.ctsCode());
			di.setTT("Africa");	
			int idx = sdf2.format(recv).lastIndexOf(":");
			String tran_time = sdf2.format(recv).substring(0, idx);
			di.setTRAN_TIME(tran_time);
			di.setPROCESS_START_TIME(TimeUtil.getSysTime());
			di.setFILE_NAME_N(new File(filePath).getName());
			di.setBUSINESS_STATE("1"); //1成功，0失败
			di.setPROCESS_STATE("1");  //1成功，0失败
			
			String station = surfAfricaModel.getStationNumberChina();
			Date date = surfAfricaModel.getObserDateTime();
			
			int adminCode = (int)missing;
			int stationClass = (int)noTask;
//			int ncode = (int)noTask;
			Double latitude = surfAfricaModel.getLatitude(); // 纬度
			Double longitude = surfAfricaModel.getLongitude(); // 经度
			Double height = surfAfricaModel.getHeightOfSationGroundAboveMeanSeaLevel(); // 测站海拔高度
			String info = (String) proMap.get(station + "+01");
			if(info != null ){
				String[] infos = info.split(",");
				// 如果 经、纬度、测站高度为缺测，表示该种格式的报文中没有台站信息，需要从lua配置文件读取
				if (latitude == 999999.0 || longitude == 999999.0 || height == 999999.0) {
					try{
						if(infos.length >= 3 && infos[2] != null)
							latitude = Double.parseDouble(infos[2]);// 纬度
						if(infos.length >= 2 && infos[1] != null)
							longitude = Double.parseDouble(infos[1]); // 经度
						if(infos.length >= 4 && infos[3] != null)
							height = Double.parseDouble(infos[3]); // 测站海拔高度
					}catch (Exception e) {
						infoLogger.error("Read latitude | longitude | stationHeight error!");
					}
					surfAfricaModel.setLatitude(latitude);
					surfAfricaModel.setLongitude(longitude);
					surfAfricaModel.setHeightOfSationGroundAboveMeanSeaLevel(height);
				}
				try{
					stationClass = Integer.parseInt(infos[6]); // 台站级别
//					ncode = Integer.parseInt(infos[5]);  // 省代码
				}catch (Exception e) {
					infoLogger.info("StationClass error!");
				}
			}
			
			int ii = 1;
//			+ "D_DATETIME,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATA_ID,V_BBB,V01301,V01300,V05001,"
			prestmt.setString(ii++, station + "_" + sdf.format(date));
			prestmt.setString(ii++, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
			prestmt.setString(ii++, TimeUtil.getSysTime());
			prestmt.setString(ii++, sdf2.format(recv));
			prestmt.setString(ii++, TimeUtil.getSysTime());
			prestmt.setString(ii++, StartConfig.sodCode());
			prestmt.setString(ii++, surfAfricaModel.getCorrectSign());
			prestmt.setString(ii++, station);
			prestmt.setInt(ii++, 999998);
			prestmt.setBigDecimal(ii++, new BigDecimal(latitude).setScale(4, BigDecimal.ROUND_HALF_UP));
//			+ "V06001,V07001,V07031,V07032_04,V02001,V02301,V_ACODE,V04001,V04002,V04003,"
			prestmt.setBigDecimal(ii++, new BigDecimal(longitude).setScale(4, BigDecimal.ROUND_HALF_UP));
			prestmt.setBigDecimal(ii++, new BigDecimal(height).setScale(4, BigDecimal.ROUND_HALF_UP));
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getHeightOfBaromSensor()).setScale(4, BigDecimal.ROUND_HALF_UP));
			prestmt.setDouble(ii++, noTask);//风速传感器距地面高度
			prestmt.setInt(ii++, 0); //0；无人自动站
			prestmt.setInt(ii++, stationClass); // 台站级别
			prestmt.setInt(ii++, adminCode);
			prestmt.setInt(ii++, date.getYear() + 1900);
			prestmt.setInt(ii++, date.getMonth() + 1);
			prestmt.setInt(ii++, date.getDate());
//			+ "V04004,V10004,V10051,V10061,V10062,V10301,V10301_052,V10302,V10302_052,V12001,"
			prestmt.setInt(ii++, date.getHours());
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getStationPress()).setScale(4, BigDecimal.ROUND_HALF_UP));
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getSeaLevelPress()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 海平面气压
			prestmt.setDouble(ii++, noTask); // 3小时变压
			prestmt.setDouble(ii++, noTask); // 24小时变压
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getMaxStationPress()).setScale(4, BigDecimal.ROUND_HALF_UP));
			prestmt.setInt(ii++, surfAfricaModel.getOccurTimeOfMaxStaPress());
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getMinStationPress()).setScale(4, BigDecimal.ROUND_HALF_UP));
			prestmt.setInt(ii++, surfAfricaModel.getOccurTimeOfMinStaPress());
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getTemperature()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 气温
//			+ "V12011,V12011_052,V12012,V12012_052,V12405,V12016,V12017,V12003,V13003,V13007,"
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getMaxTemperature()).setScale(4, BigDecimal.ROUND_HALF_UP));   //小时内最高气温
			prestmt.setInt(ii++, surfAfricaModel.getOccurTimeOfMaxTemp());
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getMinTemperature()).setScale(4, BigDecimal.ROUND_HALF_UP)); //小时内最低气温
			prestmt.setInt(ii++, surfAfricaModel.getOccurTimeOfMinTemp());
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getTemChange_24Hour()).setScale(4, BigDecimal.ROUND_HALF_UP));  // V12405 24小时变温
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getMaxTemperature_24Hour()).setScale(4, BigDecimal.ROUND_HALF_UP)); //过去24小时最高气温
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getMinTemperature_24Hour()).setScale(4, BigDecimal.ROUND_HALF_UP));
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getDewPoint()).setScale(4, BigDecimal.ROUND_HALF_UP));  //露点温度
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getRelativeHumid()).setScale(4, BigDecimal.ROUND_HALF_UP));// 相对湿度
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getMinRelativeHumid()).setScale(4, BigDecimal.ROUND_HALF_UP));//小时内最小相对湿度
//			+ "V13007_052,V13004,V13019,V13020,V13021,V13022,V13023,V04080_04,V13011,V13033,"
			prestmt.setInt(ii++, surfAfricaModel.getOccurTimeOfMinRelativeHumid());
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getVaporPress()).setScale(4, BigDecimal.ROUND_HALF_UP));  //水汽压
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getRainAmountHourly()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 1小时
			prestmt.setDouble(ii++, noTask); // 3小时降水
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getRainAmount_6Hour()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 6小时
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getRainAmount_12Hour()).setScale(4, BigDecimal.ROUND_HALF_UP)); //V13022 12小时
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getRainAmount_24Hour()).setScale(4, BigDecimal.ROUND_HALF_UP)); // 过去24小时降水量	V13023
			prestmt.setDouble(ii++, noTask); // 人工加密观测降水量描述时间周期	V04080_04
			prestmt.setDouble(ii++, noTask); // 人工加密观测降水量	V13011
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getVaporHourly()).setScale(4, BigDecimal.ROUND_HALF_UP));// 小时蒸发量	V13033
//			+ "V11290,V11291,V11292,V11293,V11296,V11042,V11042_052,V11201,V11202,V11211,"
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getWindDir_2min()).setScale(4, BigDecimal.ROUND_HALF_UP));//2分钟平均风向	V11290
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getWindSpd_2min()).setScale(4, BigDecimal.ROUND_HALF_UP));//	2分钟平均风速	V11291
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getWindDir_10min()).setScale(4, BigDecimal.ROUND_HALF_UP));//10分钟平均风向	V11292
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getWindSpd_10min()).setScale(4, BigDecimal.ROUND_HALF_UP));//10分钟平均风速	V11293
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getWindDirOfMaxSpd()).setScale(4, BigDecimal.ROUND_HALF_UP));//小时内最大风速的风向	V11296
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getMaxWindSpd()).setScale(4, BigDecimal.ROUND_HALF_UP));//小时内最大风速	V11042
			prestmt.setInt(ii++, surfAfricaModel.getOccurTimeOfMaxWind());//	小时内最大风速出现时间	V11042_052
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getWindDir()).setScale(4, BigDecimal.ROUND_HALF_UP));//瞬时风向	V11201
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getWindSpd()).setScale(4, BigDecimal.ROUND_HALF_UP));//瞬时风速	V11202
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getWindDirOfExtremMaxWind()).setScale(4, BigDecimal.ROUND_HALF_UP));//	小时内极大风速的风向	V11211
//			+ "V11046,V11046_052,V11503_06,V11504_06,V11503_12,V11504_12,V12120,V12311,V12311_052,V12121,"
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getExtremMaxWindSpd()).setScale(4, BigDecimal.ROUND_HALF_UP));//小时内极大风速	V11046
			prestmt.setInt(ii++, surfAfricaModel.getOccurTimeOfExtremMaxWind());//小时内极大风速出现时间	V11046_052
			prestmt.setDouble(ii++, noTask);//过去6小时极大瞬时风向	V11503_06
			prestmt.setDouble(ii++, noTask);//过去6小时极大瞬时风速	V11504_06
			prestmt.setDouble(ii++, noTask);//过去12小时极大瞬时风向	V11503_12
			prestmt.setDouble(ii++, noTask);//过去12小时极大瞬时风速	V11504_12
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getGroundTemp()).setScale(4, BigDecimal.ROUND_HALF_UP));//	地面温度	V12120
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getMaxGroundTemp()).setScale(4, BigDecimal.ROUND_HALF_UP));//	小时内最高地面温度	V12311
			prestmt.setInt(ii++, surfAfricaModel.getOccurTimeOfMaxGroundTemp());//	 小时内最高地面温度出现时间	V12311_052
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getMinGroundTemp()).setScale(4, BigDecimal.ROUND_HALF_UP));//	 小时内最低地面温度	V12121
//			+ "V12121_052,V12013,V12030_005,V12030_010,V12030_015,V12030_020,V12030_040,V12030_080,V12030_160,V12030_320,"
			prestmt.setInt(ii++, surfAfricaModel.getOccurTimeOfMinGroundTemp());//	 小时内最低地面温度出现时间	V12121_052
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getMinTemperature_12Hour()).setScale(4, BigDecimal.ROUND_HALF_UP));//	 过去12小时最低地面温度	V12013
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getGroundTemp_5cm()).setScale(4, BigDecimal.ROUND_HALF_UP));
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getGroundTemp_10cm()).setScale(4, BigDecimal.ROUND_HALF_UP));
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getGroundTemp_15cm()).setScale(4, BigDecimal.ROUND_HALF_UP));
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getGroundTemp_20cm()).setScale(4, BigDecimal.ROUND_HALF_UP));
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getGroundTemp_40cm()).setScale(4, BigDecimal.ROUND_HALF_UP));
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getGroundTemp_80cm()).setScale(4, BigDecimal.ROUND_HALF_UP));
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getGroundTemp_160cm()).setScale(4, BigDecimal.ROUND_HALF_UP));
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getGroundTemp_320cm()).setScale(4, BigDecimal.ROUND_HALF_UP));
//			+ "V12314,V12315,V12315_052,V12316,V12316_052,V20001_701_01,V20001_701_10,V20059,V20059_052,V20001,"
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getGrassGroundTemp()).setScale(4, BigDecimal.ROUND_HALF_UP));//草面（雪面）温度	V12314
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getMinGrassGroundTemp()).setScale(4, BigDecimal.ROUND_HALF_UP));//	小时内草面（雪面）最高温度	V12315
			prestmt.setInt(ii++, surfAfricaModel.getOccurTimeOfMaxGrassGroundTemp());//	小时内草面（雪面）最高温度出现时间	V12315_052
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getMinGrassGroundTemp()).setScale(4, BigDecimal.ROUND_HALF_UP));//	小时内草面（雪面）最低温度	V12316
			prestmt.setInt(ii++, surfAfricaModel.getOccurTimeOfMinGrassGroundTemp());//	小时内草面（雪面）最低温度出现时间	V12316_052
			prestmt.setDouble(ii++, noTask);//1分钟平均水平能见度	V20001_701_01
			prestmt.setDouble(ii++, noTask);//10分钟平均水平能见度	V20001_701_10
			prestmt.setBigDecimal(ii++, new BigDecimal(surfAfricaModel.getMinVisibility()).setScale(4, BigDecimal.ROUND_HALF_UP));//最小水平能见度	V20059
			prestmt.setInt(ii++, surfAfricaModel.getOccurTimeOfMinVisibility());//最小水平能见度出现时间	V20059_052
			prestmt.setDouble(ii++, surfAfricaModel.getVisibility());//水平能见度（人工）	V20001
//			+ "V20010,V20051,V20011,V20013,V20350_01,V20350_02,V20350_03,V20350_04,V20350_05,V20350_06,"
			prestmt.setDouble(ii++, surfAfricaModel.getCloudAmountHourly());// 总云量	V20010
			prestmt.setDouble(ii++, surfAfricaModel.getLowCloudAmountHourly());// 低云量	V20051
			prestmt.setDouble(ii++, surfAfricaModel.getLowOrMidCloudAmountHourly());// 低云或中云的云量	V20011
			prestmt.setDouble(ii++, surfAfricaModel.getHeightOfLowOrMidCloudHourly()); // 低云或中云的云高	V20013
			
			if(surfAfricaModel.getCloudShape() == null){
				surfAfricaModel.setCloudShape(shape);
			}
			prestmt.setInt(ii++, surfAfricaModel.getCloudShape()[0]);// 云状1	V20350_01
			prestmt.setInt(ii++, surfAfricaModel.getCloudShape()[1]);// 云状2	V20350_02
			prestmt.setInt(ii++, surfAfricaModel.getCloudShape()[2]);// 云状3	V20350_03
			prestmt.setInt(ii++, surfAfricaModel.getCloudShape()[3]);// 云状4	V20350_04
			prestmt.setInt(ii++, surfAfricaModel.getCloudShape()[4]);// 云状5	V20350_05
			prestmt.setInt(ii++, surfAfricaModel.getCloudShape()[5]);// 云状6	V20350_06				
//			+ "V20350_07,V20350_08,V20350_11,V20350_12,V20350_13,V20003,V04080_05,V20004,V20005,V20062,"
			prestmt.setInt(ii++, surfAfricaModel.getCloudShape()[6]);
			prestmt.setInt(ii++, surfAfricaModel.getCloudShape()[7]);
			prestmt.setDouble(ii++, noTask);// 低云状	V20350_11
			prestmt.setDouble(ii++, noTask);// 中云状	V20350_12
			prestmt.setDouble(ii++, noTask);// 高云状	V20350_13
			prestmt.setInt(ii++, surfAfricaModel.getWeatherPheno());// 现在天气	V20003
			prestmt.setDouble(ii++, noTask);// 过去天气描述时间周期	V04080_05
			prestmt.setDouble(ii++, noTask);// 过去天气1	V20004
			prestmt.setDouble(ii++, noTask);// 过去天气2	V20005
			prestmt.setDouble(ii++, surfAfricaModel.getGroundState_6Hour());
//			+ "V13013,V13330,V20330_01,V20331_01,V20330_02,V20331_02,V08010,V07032_01,V07032_02,V02183,"
			prestmt.setDouble(ii++, surfAfricaModel.getSnowDepth_0_6_12Hour());// 积雪深度	V13013
			prestmt.setDouble(ii++, surfAfricaModel.getSnowPress_0_6_12Hour());// 雪压	V13330
			prestmt.setDouble(ii++, noTask);// 冻土第1冻结层上限值	V20330_01
			prestmt.setDouble(ii++, noTask);// 冻土第1冻结层下限值	V20331_01
			prestmt.setDouble(ii++, noTask);// 冻土第2冻结层上限值	V20330_02
			prestmt.setDouble(ii++, noTask);// 冻土第2冻结层下限值	V20331_02
			prestmt.setDouble(ii++, noTask); // 地面状况
			prestmt.setDouble(ii++, noTask); // 温湿传感器距地高度
			prestmt.setDouble(ii++, noTask); // 能见度传感器距地高度
			prestmt.setDouble(ii++, noTask); // 云探测系统
//			+ "V20214,D_DATA_DPCID)"; 
//			prestmt.setDouble(ii++, surfAfricaModel.getHailDiameterOfImportantWeather());
//			prestmt.setString(ii++, dpcType);
			prestmt.setString(ii++, StartConfig.ctsCode());
			//	
			di.setIIiii(station);
			String date_time = TimeUtil.date2String(date, "yyyy-MM-dd HH:mm:ss").substring(0, idx);
			di.setDATA_TIME(date_time);
			di.setPROCESS_END_TIME(TimeUtil.getSysTime());
			di.setRECORD_TIME(TimeUtil.getSysTime());	
			di.setLATITUDE(String.valueOf(latitude));
			di.setLONGTITUDE(String.valueOf(longitude));
			di.setHEIGHT(String.valueOf(height));
			di.setFILE_SIZE(String.valueOf(new File(filePath).length()));
			di.setDATA_UPDATE_FLAG("000");
			di.setSEND("BFDB");
			di.setSEND_PHYS("DRDS");
			
			listDi.add(di);
			prestmt.addBatch();
			sqls.add(((LoggableStatement)prestmt).getQueryString());
//			System.out.println(((LoggableStatement)prestmt).getQueryString());
		}
		prestmt.executeBatch();
		connection.commit();
		infoLogger.info("Insert record num: " + datas.size());
		infoLogger.info("\r\n " +"Batch commit succ!");
		ret = 1;
	}catch (SQLException e) {
		// 如果出错，则此次executeBatch()的所有数据都不入库
		infoLogger.error("\r\n " +"Batch commit failed!" + filePath + e.getMessage());
      	try {
			connection.rollback();
		} catch (SQLException e1) {
			infoLogger.error("\r\n " +"Batch commit failed!");
		}
      	Statement stmt = null;
		try {
			stmt = connection.createStatement();
			connection.setAutoCommit(true);
			// 逐条入库
			int t = datas.size();
          	for(int i = 0; i < sqls.size(); i++){
          		try {
					stmt.execute(sqls.get(i));
					ret = 1; //有一条入库即为入库成功
					infoLogger.info("Insert one by one: insert one record succ!");
				} catch (SQLException e1) {
//					t --;
//					listDi.get(i).setPROCESS_STATE("0");
//					infoLogger.error("\r\n " +"Insert one by one failed: " + sqls.get(i));
//					continue;
					boolean isInserted = deleteAndInsert(datas.get(i).getObserDateTime(), datas.get(i).getStationNumberChina(), 
							table_name, connection, datas.get(i).getCorrectSign(), sqls.get(i));
					if(!isInserted){
						t --;
						listDi.get(i).setPROCESS_STATE("0");
						infoLogger.error("\r\n " +"Insert one by one failed: " + sqls.get(i));
						continue;
					}
				}
          	}
          	infoLogger.info("Insert record: " + t);
		} catch (SQLException e1) {
			infoLogger.error("\r\n " +"Create Statement error!");
		}finally {
      		if(stmt != null)
      			try{
      				stmt.close();
      			}
			 catch (SQLException e1) {
				e1.printStackTrace();
				infoLogger.error("\r\n " +"Close statement error!");
			 }
      	}
	}finally {
		if (prestmt != null)
			try {
				prestmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				infoLogger.error("\r\n " +"Close statement error!");
			}
	}
	if(ret == 1)
		return DataBaseAction.SUCCESS; // 成功
	else 
		return DataBaseAction.INSERT_ERROR; // 失败
	}

/**
 * @Title: deleteAndInsert   
 * @Description: 逐条入库失败时，检查更正标识，再入库  
 * @param datetime
 * @param station
 * @param table_name
 * @param connection
 * @param comingCor
 * @param insertSql
 * @return boolean    入库是否成功  
 * @throws：
 */
	public static boolean deleteAndInsert(Date datetime, String station, String table_name, Connection connection, String comingCor, String insertSql){
		String delete = "delete from " + table_name + " where d_datetime=" + "'" + sdf2.format(datetime) +
				"'" +" and V01301=" + "'" + station + "'";
		String select = "select V_BBB from " + table_name + " where d_datetime=" + "'" + sdf2.format(datetime) + 
				"'" +" and V01301=" + "'" + station + "'";
		Statement statement = null;
		ResultSet resultSet = null;
		try{
			statement = connection.createStatement();
			resultSet = statement.executeQuery(select);
			String cor = "";
			if(resultSet.next()){
				cor = resultSet.getString(1);
				if(comingCor.compareTo(cor) > 0){
					// 更正,先删除
					statement.execute(delete);
					// 再插入
					statement.execute(insertSql);
					if(connection.getAutoCommit() == false)
						connection.commit();
					return true;// 入库成功
				}
				else{
					// 只是主键冲突，不需要更正
					return false;
				}
			}else{
				// 不是主键冲突，不是正常sql
				return false;
			}
		}
		catch (Exception e) {
			infoLogger.error(e.getMessage());
			return false;
		}
		finally {
			if(statement != null){
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					infoLogger.error(e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}
}
