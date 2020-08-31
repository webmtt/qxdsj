package cma.cimiss2.dpc.indb.cawn.dc_cawn_reg.service;

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
import cma.cimiss2.dpc.decoder.bean.cawn.REG;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class DbService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
//	static Map<String, Object> proMap = StationInfo.getProMap();
	
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
	public static DataBaseAction processSuccessReport(ParseResult<REG> parseResult, String fileN, Date recv_time,StringBuffer loggerBuffer) {
		java.sql.Connection connection = null;
		java.sql.Connection reportConnection = null;
		try {
			// 获取数据库连接
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			// 获取解码结果集
			List<REG> regs = parseResult.getData();
			insertDB(regs, connection, recv_time, loggerBuffer,fileN);
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
					loggerBuffer.append("\n Close database connection error! "+e.getMessage());
				}
			}
			if(reportConnection != null) {
				try {
					reportConnection.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n  Close database connection error! "+e.getMessage());
				}
			}
		}
	}
	/**
	 * 
	 * @Title: insertDB
	 * @Description: TODO(大气成分反应性气体观测资料入库)   
	 * @param regs 待入库对象集合
	 * @param connection 数据库连接
	 * @param recv_time 资料接收接收
	 * @param fileN
	 * @return void 
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	private static void insertDB(List<REG> regs, java.sql.Connection connection, Date recv_time, StringBuffer loggerBuffer, String fileN){
		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,D_UPDATE_TIME,"
			+ "V01301,V01300,V05001,V06001,V07001,V_ACODE,V04001,V04002,V04003,V04004,"
			+ "V04005,V04006,V15800,V15810,V15814,V15817,V15820,V15823,V15826,V05042_01,"
			+ "V05045_01,V05042_02,V05045_02,V05042_03,V05045_03,V05042_04,V05045_04,V05042_05,V05045_05,V05042_06,"
			+ "V05045_06,V05042_07,V05045_07,V15802_SO2,V15810_5_005,V15810_5_006,V15810_5_004,V15802_NO,V15814_5_005,V15814_5_006,"
			+ "V15814_5_004,V15802_NO2,V15817_5_005,V15817_5_006,V15817_5_004,V15802_NOX,V15820_5_005,V15820_5_006,V15820_5_004,V15802_CO,"
			+ "V15823_5_005,V15823_5_006,V15823_5_004,V15802_O3,V15826_5_005,V15826_5_006,V15826_5_004,V12001,V15440_SO2,V15441_SO2,"
			+ "V15442_SO2,V15443_SO2,V15444_SO2,V15840_SO2,V15437_SO2,V15841_SO2,V15445_SO2,V15438_SO2,V15448_SO2,V15439_SO2,"
			+ "V15811_SO2,V15449_SO2,V15440_NOX,V15441_NOX,V15442_NOX,V15443_NOX,V15842_NOX,V15843_NOX,V15444_NOX,V15840_NOX,"
			+ "V15437_NOX,V15844_NOX,V15438_NOX,V15448_NO,V15439_NO,V15815_NO,V15449_NO,V15818_NO2,V15449_NO2,V15448_NOX,"
			+ "V15439_NOX,V15821_NOX,V15449_NOX,V15440_CO,V15441_CO,V15442_CO,V15443_CO,V15444_CO,V15840_CO,V15845_CO,"
			+ "V15846_CO,V15847_CO,V15848_CO,V15438_CO,V15448_CO,V15439_CO,V15824_CO,V15449_CO,V15440_O3,V15441_O3,"
			+ "V15445_O3A,V15445_O3B,V15446_O3A,V15446_O3B,V16447_O3A,V15447_O3B,V15444_O3,V15849_O3,V15850_O3,V15438_O3,"
			+ "V15448_O3,V15439_O3,V15827_O3,V15449_O3,V_BBB,D_SOURCE_ID) "
			+ "VALUES (?,?, ?, ?, ?, ?,"
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, ?, ?) " ;
		if(connection != null){		
			try {	
				pStatement = new LoggableStatement(connection, sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				connection.setAutoCommit(false);
				List<String> sqls = new ArrayList<>();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");		
				String V_TT = "REG";
				for(int idx = 0; idx < regs.size(); idx ++){
					REG reg = regs.get(idx);
					// 非更正报；或者为更正报，但是数据库中尚未有这一条数据
					StatDi di = new StatDi();	
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(sod_code);
					di.setDATA_TYPE_1(cts_code);
					di.setTT(V_TT);			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //0成功，1失败
					di.setPROCESS_STATE("1");  //0成功，1失败	
					
					double lat = reg.getLatitude();
					double lon = reg.getLongtitude();
					double height = reg.getHeightAboveSeaLevel();
					
					Date dataTime = reg.getObservationTime();
					String station = reg.getStationNumberChina();
					int adminCode = 999999;
					int ii = 1;
					pStatement.setString(ii++, sdf.format(dataTime)+"_"+station); 
					pStatement.setString(ii++, sod_code);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(dataTime.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					
					String info = (String) proMap.get(station + "+16");
					if(info == null) {
						loggerBuffer.append("\n In configuration file, this station does not exist: " + station);
					}
					else{
						String[] infos = info.split(",");						
						if(infos[5].equals("null"))
							loggerBuffer.append("\n In configuration file, V_CCCC is null!");
						else 
							adminCode = Integer.parseInt(infos[5]);
						if(String.valueOf(lat).startsWith("99999") && !infos[2].equals("null")){
							lat = Double.parseDouble(infos[2]);
							reg.setLatitude(lat);
						}
						if(String.valueOf(lon).startsWith("99999") && !infos[1].equals("null")){
							lon = Double.parseDouble(infos[1]);
							reg.setLongtitude(lon);
						}
						if(String.valueOf(height).startsWith("99999") && !infos[3].equals("null")){
							height = Double.parseDouble(infos[3]);
							reg.setHeightAboveSeaLevel(height);
						}
							
					}
					
					pStatement.setString(ii++, station);
					pStatement.setInt(ii++, Integer.parseInt(StationCodeUtil.stringToAscii(station)));
					pStatement.setBigDecimal(ii++, new BigDecimal(reg.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(reg.getLongtitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getHeightAboveSeaLevel())));
					
					pStatement.setInt(ii++, adminCode);
					pStatement.setInt(ii++, dataTime.getYear() + 1900);
					pStatement.setInt(ii++, dataTime.getMonth() + 1);
					pStatement.setInt(ii++, dataTime.getDate());
					pStatement.setInt(ii++, dataTime.getHours());
					
					pStatement.setInt(ii++, dataTime.getMinutes());
					pStatement.setInt(ii++, dataTime.getSeconds());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getRecordFrequency())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getSO2_Density())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getNO_Density())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getNO2_Density())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getNOX_Density())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getCO_Density())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getO3_Density())));
					pStatement.setInt(ii++, reg.getChannel_1());
					
					pStatement.setInt(ii++, reg.getMeasureCode_1());
					pStatement.setInt(ii++, reg.getChannel_2());
					pStatement.setInt(ii++, reg.getMeasureCode_2());
					pStatement.setInt(ii++, reg.getChannel_3());
					pStatement.setInt(ii++, reg.getMeasureCode_3());
					pStatement.setInt(ii++, reg.getChannel_4());
					pStatement.setInt(ii++, reg.getMeasureCode_4());
					pStatement.setInt(ii++, reg.getChannel_5());
					pStatement.setInt(ii++, reg.getMeasureCode_5());
					pStatement.setInt(ii++, reg.getChannel_6());
					
					pStatement.setInt(ii++, reg.getMeasureCode_6());
					pStatement.setInt(ii++, reg.getChannel_7());
					pStatement.setInt(ii++, reg.getMeasureCode_7());
					pStatement.setString(ii++, reg.getSO2_DataSign());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getSO2_5minMaxDensity())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getSO2_5minMinDensity())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getSO2_5minDensitySTDEV())));
					pStatement.setString(ii++, reg.getNO_DataSign());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getNO_5minMaxDensity())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getNO_5minMaxDensity())));
					
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getNO_5minDensitySTDEV())));
					pStatement.setString(ii++, reg.getNO2_DataSign());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getNO2_5minMaxDensity())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getNO2_5minMinDensity())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getNO2_5minDensitySTDEV())));
					pStatement.setString(ii++, reg.getNOX_DataSign());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getNOX_5minMaxDensity())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getNOX_5minMinDensity())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getNOX_5minDensitySTDEV())));
					pStatement.setString(ii++, reg.getCO_DataSign());
					
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getCO_5minMaxDensity())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getCO_5minMinDensity())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getCO_5minDensitySTDEV())));
					pStatement.setString(ii++, reg.getO3_DataSign());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getO3_5minMaxDensity())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getO3_5minMinDensity())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getO3_5minDensitySTDEV())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getInnerTemperature())));
					pStatement.setString(ii++, reg.getSO2_DeviceSN());
					pStatement.setString(ii++, reg.getSO2_DeviceStateCode());
					
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getSO2_DeviceInnerTemperature())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getSO2_DeviceChamberTemperature())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getSO2_DevicePressure())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getSO2_DeviceGasFlow())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getSO2_DevicePhotomultiplierVoltage())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getSO2_DeviceTubeVoltage())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getSO2_DeviceLightIntensity())));
					pStatement.setInt(ii++, reg.getSO2_DeviceExternalAlarm());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getSO2_DeviceZeroPoint())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getSO2_DeviceSlope())));
					//	+ "V15811_SO2,V15449_SO2,V15440_NOX,V15441_NOX,V15442_NOX,V15443_NOX,V15842_NOX,V15843_NOX,V15444_NOX,V15840_NOX,"
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getSO2_DeviceCalibrationDensity())));
					pStatement.setInt(ii++, reg.getSO2_DeviceRevisedSign());
					pStatement.setString(ii++, reg.getNOX_DeviceSN());
					pStatement.setString(ii++, reg.getNOX_DeviceStateCode());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getNOX_DeviceInnerTemperature())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getNOX_DeviceChamberTemperature())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getNOX_DevicePhotomultiplierTemperature())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getNOX_DeviceConverterTemperature())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getNOX_DevicePressure())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getNOX_DeviceGasFlow())));
					
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getNOX_DevicePhotomultiplierVoltage())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getNOX_DeviceO3GeneratorFlow())));
					pStatement.setInt(ii++, reg.getNOX_DeviceExternalAlarm());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getNO_ZeroPoint())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getNO_Slope())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getNO_CalibrationDensity())));
					pStatement.setInt(ii++, reg.getNO_RevisedSign());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getNO2_CalibrationDensity())));
					pStatement.setInt(ii++, reg.getNO2_RevisedSign());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getNOX_ZeroPoint())));
					
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getNOX_Slope())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getNOX_CalibrationDensity())));
					pStatement.setInt(ii++, reg.getNOX_RevisedSign());
					pStatement.setString(ii++, reg.getCO_DeviceSN());
					pStatement.setString(ii++, reg.getCO_DeviceStateCode());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getCO_DeviceInnerTemperature())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getCO_DeviceChamberTemperature())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getCO_DevicePressure())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getCO_DeviceGasFlow())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getCO_DeviceBiasVoltage())));
					
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getCO_DeviceMotorSpeed())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getCO_DeviceSRRatio())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getCO_LightIntensity())));
					pStatement.setInt(ii++, reg.getCO_DeviceExternalAlarm());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getCO_DeviceZeroPoint())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf( reg.getCO_DeviceSlope())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getCO_DeviceCalibrationDensity())));
					pStatement.setInt(ii++, reg.getCO_DeviceRevisedSign());
					pStatement.setString(ii++, reg.getO3_DeviceSN());
					pStatement.setString(ii++, reg.getO3_DeviceStateCode());
					
					pStatement.setInt(ii++, reg.getO3_DeviceALightIntensity());
					pStatement.setInt(ii++, reg.getO3_DeviceBLightIntensity());
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getO3_DeviceANoise())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getO3_DeviceBNoise())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getO3_DeviceAFlowRate())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getO3_DeviceBFlowRate())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getO3_DevicePressure())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getO3_DeviceLightSeatTemperature())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getO3_DeviceLightTemperature())));
					pStatement.setInt(ii++, reg.getO3_DeviceExternalAlarm());
					
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getO3_DeviceZeroPoint())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getO3_DeviceSlope())));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(reg.getO3_DeviceCalibrationDensity())));
					pStatement.setInt(ii++, reg.getO3_DeviceRevisedSign());
					pStatement.setString(ii++, "000");
					pStatement.setString(ii++, cts_code);
					
					di.setIIiii(station);
					di.setDATA_TIME(TimeUtil.date2String(dataTime, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(lat));
					di.setLONGTITUDE(String.valueOf(lon));
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(fileN).length()));
					di.setDATA_UPDATE_FLAG("000");
					di.setHEIGHT(String.valueOf(height));
					
					pStatement.addBatch();
					sqls.add(((LoggableStatement)pStatement).getQueryString()); // 批量入库冲突时，单条入库
					listDi.add(di);
				} 
				try{
					pStatement.executeBatch();
					connection.commit();
					sqls.clear();
				}catch(SQLException e){
					pStatement.clearParameters();
					pStatement.clearBatch();
					execute_sql(sqls, connection, loggerBuffer, fileN); // 此中，入库失败的会将对应的DI的ProcessState置为1 // 1:失败，0：成功
					loggerBuffer.append("\n Batch commit failed: "+fileN);
				}
			}catch (SQLException e) {
				loggerBuffer.append("\n Create Statement error: "+e.getMessage());
			}
			finally {
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
	
	/**
	 * 
	 * @Title: execute_sql   
	 * @Description: TODO(批量入库失败时，采用逐条提交)   
	 * @param  sqls 待执行的查询语句
	 * @param  connection      数据库连接
	 * @return: void      
	 * @throws:
	 */
	private static void execute_sql(List<String> sqls, java.sql.Connection connection, StringBuffer loggerBuffer, String fileN) {
		Statement pStatement = null;
		try {
			connection.setAutoCommit(true);
			pStatement = connection.createStatement();
			for (int i = 0; i < sqls.size(); i++) {
//				pStatement = new LoggableStatement(connection, sqls.get(i));
				try {
					pStatement.execute(sqls.get(i));
				} catch (Exception e) {
					loggerBuffer.append("\n File name："+fileN
							+"\n " + listDi.get(i).getIIiii() + " " + listDi.get(i).getDATA_TIME()
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
