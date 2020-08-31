package cma.cimiss2.dpc.indb.sand.dc_cawn_sand_dds.service;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import cma.cimiss2.dpc.decoder.bean.sand.SandDds;
import cma.cimiss2.dpc.decoder.sand.DecodeSandDds;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.sand.ReportInfoService;

public class DbService {
	HashMap<String, Integer> retryMap;
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
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
	 * @Description: (处理解码结果集，正确解码的报文入库)
	 * @param parseResult 解码结果集
	 * @param recv_time 接收时间
	 * @param loggerBuffer 
	 * @param filepath 文件路径
	 * @return DataBaseAction 入库状态
	 * @throws：
	 */
	@SuppressWarnings("rawtypes")
	public static DataBaseAction processSuccessReport(ParseResult<SandDds> parseResult,
			Date recv_time, String fileN, StringBuffer loggerBuffer, String filepath) {
		java.sql.Connection connection = null;
		java.sql.Connection reportConnection = null;
		try {
			String v_tt="DDS";
			// 获取数据库连接
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			List<SandDds> sandDds = parseResult.getData();
			insertDB(sandDds, connection, recv_time,v_tt,fileN,loggerBuffer,filepath);
		 
			List<ReportInfo> reportInfos = parseResult.getReports();
			reportConnection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
			ReportInfoService.reportInfoToDb(reportInfos, reportConnection, "000", recv_time, "9999", v_tt, report_sod_code,cts_code, "15", loggerBuffer,filepath,listDi);
			return DataBaseAction.SUCCESS;
		} catch (Exception e) {
			loggerBuffer.append("\n Database connection error");
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
					loggerBuffer.append("\n Database connection close error"+e.getMessage());
				}
			}
			
			if(reportConnection != null) {
				try {
					reportConnection.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n Database connection  close error"+e.getMessage());
				}
			}
		}
	}
	/**
	 * 
	 * @Title: insertDB
	 * @Description: (沙尘暴干沉降观测资料入库)
	 * @param sandpmm 入库对象集合
	 * @param connection 数据库连接
	 * @param recv_time 接收时间
	 * @param v_tt void 报文类别
	 * @param fileN 
	 * @param loggerBuffer 
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static void insertDB(List<SandDds> sandDds, java.sql.Connection connection,
			Date recv_time, String v_tt, String fileN, StringBuffer loggerBuffer,String filepath) {
		Map<String, Object> proMap = StationInfo.getProMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		PreparedStatement pStatement = null;
		try {
			String sql = "INSERT INTO "+StartConfig.valueTable()+" (D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,"
					+ "D_DATETIME,V01301,V01300,V05001, V06001,V07001,V_ACODE,"
					+ "V04001,V04002,V04003,V04004,V04005,V04006,"
					+ " V04016,V15401,V04300_017,V04300_018,V15405,V15406,V15407,V04312,V10004_701,"
					+ "V12001_701,V15431,V15421,V_BBB, D_SOURCE_ID) VALUES"
					+ " (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+ " ?, ?, ?, ?, ?, ?, ?)";
			
			if(connection != null){
				pStatement = new LoggableStatement(connection, sql); 
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				List<String> sqls = new ArrayList<>();
				for(int i=0;i<sandDds.size();i++){
					SandDds dds = sandDds.get(i);
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
					String info = (String) proMap.get(dds.getStationNumberChina() + "+15");
					int adminCode = 999999;
					if(info == null) {
						loggerBuffer.append("\n In the configuration file, the station number does not exist" + dds.getStationNumberChina());
						
					}else{
						String[] infos = info.split(",");
						if(!(infos[5].trim().equals("null") || infos[5].trim().equals(""))){
							adminCode = Integer.parseInt(infos[5]);
						}
					}
					int ii=1;
					String PrimaryKey = sdf.format(dds.getObservationTime())+"_"+dds.getStationNumberChina();
					
					pStatement.setString(ii ++, PrimaryKey);// 记录标识
					pStatement.setString(ii ++, sod_code);// 资料标识
					pStatement.setTimestamp(ii ++,  new Timestamp(new Date().getTime()));// 入库时间
					pStatement.setTimestamp(ii ++,  new Timestamp(recv_time.getTime()));// 收到时间
					pStatement.setTimestamp(ii ++,  new Timestamp(new Date().getTime()));// 更新时间
					pStatement.setTimestamp(ii ++, new Timestamp(dds.getObservationTime().getTime()));// 资料时间
					pStatement.setString(ii ++, dds.getStationNumberChina());// 区站号(字符)
					pStatement.setBigDecimal(ii ++, new BigDecimal(StationCodeUtil.stringToAscii(dds.getStationNumberChina())));// 区站号(数字)
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(dds.getLatitude())));// 纬度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(dds.getLongitude())));// 经度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(dds.getHeightOfSationGroundAboveMeanSeaLevel())));// 测站高度
					pStatement.setInt(ii ++, adminCode);// 中国行政区划代码
					pStatement.setBigDecimal(ii ++, new BigDecimal(dds.getObservationTime().getYear() + 1900));// 资料观测年
					pStatement.setBigDecimal(ii ++, new BigDecimal(dds.getObservationTime().getMonth()+1));// 资料观测月
					pStatement.setBigDecimal(ii ++, new BigDecimal(dds.getObservationTime().getDate()));// 资料观测日
					pStatement.setBigDecimal(ii ++, new BigDecimal(dds.getObservationTime().getHours()));// 资料观测时
					pStatement.setBigDecimal(ii ++, new BigDecimal(dds.getObservationTime().getMinutes()));// 资料观测分
					pStatement.setBigDecimal(ii ++, new BigDecimal(dds.getObservationTime().getSeconds()));// 资料观测分
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(dds.getObservationInterval())));//观测时间间隔
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(dds.getSampMe_Num())));//采样膜编号
					pStatement.setString(ii ++, dds.getStime());// 开始时间
					pStatement.setString(ii ++, dds.getEtime());// 结束时间
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(dds.getDustCol_Num())));//集尘缸编号
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(dds.getDustCol_Area())));//集尘缸口面积
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(dds.getCUSO4_Wei())));//硫酸铜加入量
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(dds.getAccumulateDays())));//采样累积天数
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(dds.getPRS_Avg())));//平均气压
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(dds.getTEM_Avg())));//平均气温
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(dds.getSamp_Wei())));//最终样品重量
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(dds.getDustDep_Atsph())));//大气降尘量
					pStatement.setString(ii++, "000");
					pStatement.setString(ii++, cts_code);
					
					di.setIIiii(dds.getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(dds.getObservationTime(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLATITUDE(String.valueOf(dds.getLatitude()));
					di.setLONGTITUDE(String.valueOf(dds.getLongitude()));
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
					di.setDATA_UPDATE_FLAG("000");
					di.setHEIGHT(String.valueOf(dds.getHeightOfSationGroundAboveMeanSeaLevel()));
					
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
					sqls.clear();
				} catch (Exception e) {
					pStatement.clearParameters();
					pStatement.clearBatch();	
					execute_sql(sqls, connection,fileN,loggerBuffer);
					loggerBuffer.append("\n Batch commit failed："+fileN);
					
				}			
			} else {
				loggerBuffer.append("\n Database connection error");
			}														
		} catch (SQLException e) {		
			loggerBuffer.append("\n Database connection close error："+e.getMessage());
		}finally {
			try {
				if(pStatement != null)
					pStatement.close();
			} catch (SQLException e) {
				loggerBuffer.append("\n Database connection close  error："+e.getMessage());
			}			
		}
	}
		
		
	/**
	 * 
	 * @Title: execute_sql
	 * @Description: (批量入库失败时，采用逐条提交)  
	 * @param sqls sql对象集合
	 * @param connection  数据库连接
	 * @param fileN 
	 * @param loggerBuffer 
	 * @throws：
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
					//connection.commit();
				} catch (Exception e) {
					loggerBuffer.append("\n filename："+fileN
							+"\n " + listDi.get(i).getIIiii() + " " + listDi.get(i).getDATA_TIME()
							+"\n execute sql error："+sqls.get(i)+"\n "+e.getMessage());
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
	public static void main(String[] args) {
		String filepath = "D:\\TEMP\\G.1.2.1\\Z_SAND_DDS_C5_53336_20190702090000.TXT";
		File file = new File(filepath);
		String fileN = file.getName();
		StringBuffer loggerBuffer=new StringBuffer();
		Date recv_time=new Date(file.lastModified());
		DecodeSandDds decodeSandDds = new DecodeSandDds();
		ParseResult<SandDds> parseResult = decodeSandDds.decodeFile(file);			
		if(parseResult.isSuccess()){
			DataBaseAction action = DbService.processSuccessReport(parseResult,recv_time, fileN,loggerBuffer,filepath);
		}
	}
}
