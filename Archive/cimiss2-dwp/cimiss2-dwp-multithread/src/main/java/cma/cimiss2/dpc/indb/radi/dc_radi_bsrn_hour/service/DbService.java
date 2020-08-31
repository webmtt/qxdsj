package cma.cimiss2.dpc.indb.radi.dc_radi_bsrn_hour.service;

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

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.radi.PositiveReferenceRadiationData;
import cma.cimiss2.dpc.decoder.radi.DecodeBsrnHour;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.radi.ReportInfoService;

public class DbService {
	
	public static BlockingQueue<StatDi> diQueues;
	private static List<StatDi> listDi = new ArrayList<StatDi>();
 	public Map<String, Object> proMap = StationInfo.getProMap();
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
	 * @param filepath 文件路径
	 * @param recv_time 接收时间
	 * @param loggerBuffer 
	 * @return DataBaseAction 入库状态
	 * @throws：
	 */
	@SuppressWarnings("rawtypes")
	public static DataBaseAction processSuccessReport(ParseResult<PositiveReferenceRadiationData> parseResult,
			String fileN, Date recv_time, StringBuffer loggerBuffer, String filepath) {
		java.sql.Connection connection = null;
		java.sql.Connection reportConnection = null;
		try {
			String v_tt="RAHR";
			// 获取数据库连接
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			List<PositiveReferenceRadiationData> bsrnHour = parseResult.getData();
			insertDB(bsrnHour, connection, recv_time,v_tt,loggerBuffer,fileN, filepath);
		 
			List<ReportInfo> reportInfos = parseResult.getReports();
			reportConnection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
			ReportInfoService.reportInfoToDb(reportInfos, reportConnection, "000", recv_time, "9999", v_tt,report_sod_code,cts_code,filepath,listDi,"11");	 
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
					loggerBuffer.append("\n database connection close error"+e.getMessage());
				}
			}
			
			if(reportConnection != null) {
				try {
					reportConnection.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n database connection close error"+e.getMessage());
				}
			}
		}
	}
	/**
	 * 
	 * @Title: insertDB
	 * @Description: (正点基准辐射资料入库函数(批量入库))
	 * @param list 待入库报文
	 * @param connection 数据库连接
	 * @param recv_time 接收时间
	 * @param v_tt 报文类别
	 * @param loggerBuffer 
	 * @param fileN 
	 * @throws：
	 */
	@SuppressWarnings({ "deprecation", "unused" })
	private static void insertDB(List<PositiveReferenceRadiationData> list, java.sql.Connection connection,
			Date recv_time, String v_tt, StringBuffer loggerBuffer, String fileN, String filepath) {
		PreparedStatement pStatement = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			// 去掉 D_Record_id
			String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,"
					+ "D_UPDATE_TIME,D_DATETIME,V01301,V01300,V05001,V06001,V07001,V07032_1,V07032_2,"
					+ "V07032_3,V07032_4,V07032_5,V07032_6,V07032_7,V07032_8,V04001,V04002,V04003,"
					+ "V04004,V14313_701_60,V14322,V14313_05_60,V14313_05_052,V14314_701_60,V14309,"
					+ "V14314_05_60,V14314_05_052,V14311_701_60,V14320,V14311_05_60,V14311_05_052,"
					+ "V14315_701_60,V14305,V14315_05_60,V14315_05_052,V14318_701_60,V14323,V14318_06_60,"
					+ "V14318_06_052,V14318_05_60,V14318_05_052,V14319_701_60,V14324,V14319_06_60,"
					+ "V14319_06_052,V14319_05_60,V14319_05_052,V14316_701_60,V14307,V14316_05_01,"
					+ "V14316_05_052,V14317_701_60,V14310,V14317_05_60,V14317_05_052,V14340_701_60,"
					+ "V14340_05_60,V14340_05_052,V_BBB,D_SOURCE_ID) VALUES"
					+ " (?,?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+ "?, ?, ?, ?)";
			
			if(connection != null){
				pStatement = new LoggableStatement(connection, sql); 
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				connection.setAutoCommit(false);
				List<String> sqls = new ArrayList<>();
				for(int i=0;i<list.size();i++){
					PositiveReferenceRadiationData bsrnHour = list.get(i);
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
					int ii=1;
					String PrimaryKey = sdf.format(bsrnHour.getObservationTime())+"_"+bsrnHour.getStationNumberChina();		
					pStatement.setString(ii ++, PrimaryKey);// 记录标识
					pStatement.setString(ii ++, sod_code);// 资料标识
					pStatement.setTimestamp(ii ++,  new Timestamp(new Date().getTime()));// 入库时间
					pStatement.setTimestamp(ii ++,  new Timestamp(recv_time.getTime()));// 收到时间
					pStatement.setTimestamp(ii ++,  new Timestamp(new Date().getTime()));// 更新时间
					pStatement.setTimestamp(ii ++, new Timestamp(bsrnHour.getObservationTime().getTime()));// 资料时间
					pStatement.setString(ii ++, bsrnHour.getStationNumberChina());// 区站号(字符)
					pStatement.setBigDecimal(ii ++, new BigDecimal(StationCodeUtil.stringToAscii(bsrnHour.getStationNumberChina())));// 区站号(数字)
					
					BigDecimal latitude= new BigDecimal(bsrnHour.getLatitude());
					Double latitude2 = latitude.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
					BigDecimal longitude= new BigDecimal(bsrnHour.getLongitude());
					Double longitude2 = longitude.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
					
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(latitude2)));// 纬度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(longitude2)));// 经度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(bsrnHour.getHeightOfSationGroundAboveMeanSeaLevel())));// 测站高度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(bsrnHour.getDRA_Sensor_Heigh())));// 太阳直接辐射辐射表距地高度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(bsrnHour.getSRA_Sensor_Heigh())));//散射辐射辐射表距地高度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(bsrnHour.getQRA_Sensor_Heigh())));// 总辐射辐射表距地高度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(bsrnHour.getRRA_Sensor_Heigh())));// 反射辐射辐射表距地高度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(bsrnHour.getLR_Atmo_Sensor_Heigh())));// 大气长波辐射辐射表距地高度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(bsrnHour.getLR_Earth_Sensor_Heigh())));// 地球长波辐射辐射表距地高度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(bsrnHour.getUV_Sensor_Heigh())));// 紫外辐射辐射表距地高度
					pStatement.setBigDecimal(ii ++, new BigDecimal(String.valueOf(bsrnHour.getPAR_Sensor_Heigh())));// 光合有效辐射辐射表距地高度
					pStatement.setBigDecimal(ii ++, new BigDecimal(bsrnHour.getObservationTime().getYear() + 1900));// 资料观测年
					pStatement.setBigDecimal(ii ++, new BigDecimal(bsrnHour.getObservationTime().getMonth()+1));// 资料观测月
					pStatement.setBigDecimal(ii ++, new BigDecimal(bsrnHour.getObservationTime().getDate()));// 资料观测日
					pStatement.setBigDecimal(ii ++, new BigDecimal(bsrnHour.getObservationTime().getHours()));// 资料观测时
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getDRA_Avg_Hour())));//太阳直接辐射辐照度小时平均值
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getDirectRadiationExposure())));//直接辐射(曝辐量)
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getDRA_Max_Hour())));//太阳直接辐射辐照度小时内最大值
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getDRA_Max_OTime())));//直接辐射辐照度最大出现时间
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getSRA_Avg_Hour())));//散射辐射辐照度小时平均值
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getScatteredRadiationExposure())));//散射辐射(曝辐量)
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getSRA_Max_Hour())));//散射辐射辐照度小时内最大值
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getSRA_Max_OTime())));//散射辐射辐照度最大出现时间
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getQRA_Avg_Hour())));//总辐射辐照度小时平均值
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getTotalRadiationExposure())));//总辐射(曝辐量)
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getQRA_Max_Hour())));//总辐射辐照度小时内最大值
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getQRA_Max_Hour_OTime())));//总辐射辐照度小时内最大值出现时间
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getRRA_Avg_Hour())));//反射辐射辐照度小时平均值
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getRRA_Hour())));//反射辐射小时曝辐量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getRRA_Max_Hour())));//反射辐射辐照度小时内最大值
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getRRA_Max_OTime())));//反射辐射辐照度最大出现时间
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getALR_Avg_Hour())));//大气长波辐射辐照度小时平均值
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getALR_Hour())));//大气长波辐射小时曝辐量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getALR_Min_Hour())));//大气长波辐射辐照度小时内最小值
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getALR_Min_Mi_OTIime())));//大气长波辐射辐照度小时内最小值出现时间
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getALR_Max_Hour())));//大气长波辐射辐照度小时内最大值
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getALR_Max_Mi_OTime())));//大气长波辐射辐照度小时内最大值出现时间
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getELR_Avg_Hour())));//地球长波辐射辐照度小时平均值
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getELR_Hour())));//地球长波辐射小时曝辐量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getELR_Min_Hour())));//地球长波辐射辐照度60分钟最小值
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getELR_Min_Mi_Otime())));//地球长波辐射辐照度小时内最小值出现时间
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getELR_Max_Hour())));//地球长波辐射辐照度小时最大值
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getELR_Max_Mi_OTime())));//地球长波辐射辐照度小时内最大值出现时间
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getUVA_Avg_Hour())));//紫外辐射（UVA）辐照度小时平均值
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getUltravioletRadiationExposure())));//紫外辐射(曝辐量)
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getUV_Max_Mi())));//紫外辐射（UVA）辐照度1分钟最大值
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getUV_Max_OTime())));//紫外辐射辐照度最大出现时间
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getUVB_Avg_Hour())));//紫外辐射（UVB）辐照度小时平均值
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getUV_Hour())));//紫外辐射（UVB）小时曝辐量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getUVB_Max_Hour())));//紫外辐射（UVB）辐照度小时内最大值
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getUVB_Max_Hour_OTime())));//紫外辐射（UVB）辐照度小时内最大值出现时间
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getPAR_Avg_Hour())));//光合有效辐射辐照度小时平均值
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getPAR_Max_Hour())));//光合有效辐射辐照度小时内最大值
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(bsrnHour.getPAR_Max_Mi_OTime())));//光合有效辐射小时内最大值出现时间
					pStatement.setString(ii++, "000");
					pStatement.setString(ii++, cts_code);
					
					di.setIIiii(bsrnHour.getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(bsrnHour.getObservationTime(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLATITUDE(String.valueOf(bsrnHour.getLatitude()));
					di.setLONGTITUDE(String.valueOf(bsrnHour.getLongitude()));
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
					di.setDATA_UPDATE_FLAG("000");
					di.setHEIGHT(String.valueOf(bsrnHour.getHeightOfSationGroundAboveMeanSeaLevel()));
					
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
					execute_sql(sqls, connection,loggerBuffer,fileN);
					loggerBuffer.append(" Batch ERROR : " + sdf.format(new Date()) + "\n");
					
				}			
			} else {
				loggerBuffer.append("\n Database connection error");
			}														
		} catch (SQLException e) {		
			loggerBuffer.append("\n  Database connection  close error："+e.getMessage());
		}finally {
			try {
				if(pStatement != null)
					pStatement.close();
			} catch (SQLException e) {
				loggerBuffer.append("\n Database connection  close error："+e.getMessage());
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
	 * @param fileN 
	 * @throws：
	 */
	private static void execute_sql(List<String> sqls, java.sql.Connection connection, StringBuffer loggerBuffer, String fileN) {
		Statement pStatement = null;
		loggerBuffer.append("\n Start a single insert");
		try {
			connection.setAutoCommit(true);
			pStatement = connection.createStatement();
			for (int i = 0; i < sqls.size(); i++) {
//				pStatement = new LoggableStatement(connection, sqls.get(i));
				try {
					pStatement.execute(sqls.get(i));
					//connection.commit();
				} catch (Exception e) {
					// 单行插入失败
					loggerBuffer.append("\n " + listDi.get(i).getIIiii() + " " + listDi.get(i).getDATA_TIME()
							+"\n execute sql error："+sqls.get(i)+"\n "+e.getMessage());listDi.get(i).setPROCESS_STATE("0");
				}
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n create Statement error"+e.getMessage());
		}finally {
			if(pStatement != null) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n close Statement error "+e.getMessage());
				}
			}
		}		
		
	}
	public static void main(String[] args) {
		File file = new File("D:\\TEMP\\D.11.2.1\\Z_RADI_I_54102_20190430160001_O_BSRN-MUL-HOR.TXT");
		String fileN = file.getName();
		Date recv_time = new Date();
		StringBuffer loggerBuffer = new StringBuffer();
		DecodeBsrnHour decodeBsrnHour = new DecodeBsrnHour();
		ParseResult<PositiveReferenceRadiationData> parseResult = decodeBsrnHour.decodeFile(file);			
		if(parseResult.isSuccess()){
		DataBaseAction action = DbService.processSuccessReport(parseResult, fileN, recv_time,loggerBuffer, file.getPath());
		}
	}
}