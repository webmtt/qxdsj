package cma.cimiss2.dpc.indb.surf.dc_surf_rd_reg.service;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.surf.SurfaceRD;
import cma.cimiss2.dpc.decoder.surf.DecodeRD;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class DbService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code_ele = StartConfig.sodCode_mul();
	public static String sod_code_pre = StartConfig.sodCode_pre();
	public static String sod_report_code = StartConfig.reportSodCode();
	public static String report_type = "RD";
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
	 * @Description: TODO(路面交通数据处理)   
	 * @param parseResult 存储解码结果集
	 * @param  filepath 解码文件 
	 * @param  recv_time  资料接收时间 
	 * @return: DataBaseAction      
	 * @throws:
	 */
	public static DataBaseAction processSuccessReport(ParseResult<SurfaceRD> parseResult, String filepath, Date recv_time, String fileN) {
		java.sql.Connection connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
		java.sql.Connection report_connection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
		
		try {
			@SuppressWarnings("rawtypes")
			List<ReportInfo> reportInfos = parseResult.getReports();
			String[] fnames = fileN.split("_");  // needed to be considered
			reportInfoToDb(reportInfos, report_connection, recv_time, fnames[3].split("-")[0], report_type);  
			
			List<SurfaceRD> surfaceRDs = parseResult.getData();
			insertEleDB(surfaceRDs, connection, recv_time, filepath);
			insertPreDB(surfaceRDs, connection, recv_time, filepath);
			
			return DataBaseAction.SUCCESS;
		} catch (Exception e) {
			infoLogger.error("\n Database connection error!");
			return DataBaseAction.CONNECTION_ERROR;
		}finally {
			for (int j = 0; j < listDi.size(); j++) {
				diQueues.offer(listDi.get(j));
			}
			listDi.clear();
			
			try {
				if(connection != null ) { //&& !connection.isClosed()
					connection.close();
					
				}
			} catch (SQLException e) {
				infoLogger.error("\n Close database connection error"+e.getMessage());
			}
//			reportInfoToDb() 调用里关了
			try{
				if(report_connection != null ){
					report_connection.close();
				}
			}catch (Exception e) {
				infoLogger.error("\n Close database report_connection error"+e.getMessage());
			}
		}
		
		
	}

	/**
	 * 
	 * @Title: updateEleDB   
	 * @Description: TODO(路面交通基础气象要素入库-更正报)   
	 * @param  surfaceRDs 要素对象，更正报：数据库中已经有该条数据
	 * @param  connection 连接
	 * @param  recv_time  资料时间    
	 * @return: void      
	 * @throws:
	 */
	@SuppressWarnings("deprecation")
	private static void updateEleDB(List<SurfaceRD> surfaceRDs, java.sql.Connection connection, Date recv_time, String fileN) {
		PreparedStatement Pstmt = null;
		SurfaceRD surfaceRD = new SurfaceRD(); 
		String sql = "update "+StartConfig.keyTable()+" set "
				+ "V_BBB=?, D_UPDATE_TIME=?,"
				+ "V_SQCODE=?,V_PQCODE=?,V_NQCODE=?,V05001=?,V06001=?,V07001=?,"
				+ "V12001=?,V12011=?,V12011_052=?,"
				+ "V12012=?,V12012_052=?,V12003=?,V13003=?,V13007=?,V13007_052=?,V13004=?,V13019=?,V11290=?,V11291=?,"
				+ "V11292=?,V11293=?,V11296=?,V11042=?,V11042_052=?,V11001=?,V11002=?,V11211=?,V11046=?,V11046_052=?,"
				+ "V11211_001=?,V11046_001=?,V12421=?,V12422=?,V12422_052=?,V12423=?,V12423_052=?,V12424=?,V20001_701_01=?,V20059=?,"
				+ "V20059_052=?,V20062_1=?,V20062_2=?,V20062_3=?,V13013=?,V13368=?,V13369=?,V12425=?,V15505=?,V20003_01=?,"
				+ "V20003_02=?,V20003_03=?,V20003_04=?,V20003_05=?,V20003_06=?,Q12001=?,Q12011=?,Q12011_052=?,Q12012=?,Q12012_052=?,"
				+ "Q12003=?,Q13003=?,Q13007=?,Q13007_052=?,Q13004=?,Q13019=?,Q11290=?,Q11291=?,Q11292=?,Q11293=?,"
				+ "Q11296=?,Q11042=?,Q11042_052=?,Q11001=?,Q11002=?,Q11211=?,Q11046=?,Q11046_052=?,Q11211_001=?,Q11046_001=?,"
				+ "Q12421=?,Q12422=?,Q12422_052=?,Q12423=?,Q12423_052=?,Q12424=?,Q20001_701_01=?,Q20059=?,Q20059_052=?,Q20062_1=?,"
				+ "Q20062_2=?,Q20062_3=?,Q13013=?,Q13368=?,Q13369=?,Q12425=?,Q15505=?,Q20003_01=?,Q20003_02=?,Q20003_03=?,"
				+ "Q20003_04=?,Q20003_05=?,Q20003_06=? "
				+ "where V01301 = ? and D_datetime = ?";
		if(connection != null){
			try{
				connection.setAutoCommit(true);
				Pstmt = new LoggableStatement(connection, sql);

				for(int idx = 0; idx < surfaceRDs.size(); idx ++){
					surfaceRD = surfaceRDs.get(idx);
					StatDi di = new StatDi();				
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(sod_code_pre);
					di.setDATA_TYPE_1(cts_code);
					di.setTT(report_type);			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1");  //1成功，0失败	
					int ii = 1;
					Pstmt.setString(ii++, surfaceRD.getFileRevisionSign());
					Pstmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					
					Pstmt.setInt(ii++, Character.getNumericValue(surfaceRD.getQualityControl().charAt(0)));
					Pstmt.setInt(ii++, Character.getNumericValue(surfaceRD.getQualityControl().charAt(1)));
					Pstmt.setInt(ii++, Character.getNumericValue(surfaceRD.getQualityControl().charAt(2)));
					Pstmt.setBigDecimal(ii++, new BigDecimal(surfaceRD.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					Pstmt.setBigDecimal(ii++, new BigDecimal(surfaceRD.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getHeightOfSationGroundAboveMeanSeaLevel())));
					
					Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getAirTemperature().getValue())));
					Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getMaxAirTemperature().getValue())));
					Pstmt.setInt(ii++, surfaceRD.getTimeOfMaxAirTemperature().getValue());
					
					Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getMinAirTemperature().getValue())));
					Pstmt.setInt(ii++, surfaceRD.getTimeOfMinAirTemperature().getValue());
					Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getDewpointTemperature().getValue())));
					Pstmt.setInt(ii++, surfaceRD.getRelativeHumidity().getValue());
					Pstmt.setInt(ii++, surfaceRD.getMinRelativeHumidity().getValue());
					Pstmt.setInt(ii++, surfaceRD.getTimeOfMinRelativeHumidity().getValue());
					Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getWaterVaporPressure().getValue())));
					Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getHourlyCumulativePrecipitation().getValue())));
					Pstmt.setInt(ii++, surfaceRD.getTwoMinWindDirection().getValue());
					Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getTwoMinWindAvgSpeed().getValue())));
					
					Pstmt.setInt(ii++, surfaceRD.getTenMinWindDirection().getValue());
					Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getTenMinWindAvgSpeed().getValue())));
					Pstmt.setInt(ii++, surfaceRD.getDirectionOfMaxWindSpeed().getValue());
					Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getMaxWindSpeed().getValue())));
					Pstmt.setInt(ii++, surfaceRD.getTimeOfMaxWindSpeed().getValue());
					Pstmt.setInt(ii++, surfaceRD.getInstantaneousWindDirection().getValue());
					Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getInstantaneousWindSpeed().getValue())));
					Pstmt.setInt(ii++, surfaceRD.getDirectionOfExtremWindSpeed().getValue());
					Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getExtremWindSpeed().getValue())));
					Pstmt.setInt(ii++, surfaceRD.getTimeOfExtremWindSpeed().getValue());
					
					Pstmt.setInt(ii++, surfaceRD.getDirectionOfExtremWindSpeedMinutely().getValue());
					Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getExtremeWindSpeedMinutely().getValue())));
					Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getRoadSurfTemperature().getValue())));
					Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getMaxRoadSurfTemperature().getValue())));
					Pstmt.setInt(ii++, surfaceRD.getTimeOfMaxRoadSurfTemperature().getValue());
					Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getMinRoadSurfTemperature().getValue())));
					Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getTimeOfMinRoadSurfTemperature().getValue())));
					Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getRoadBaseTemperature().getValue())));
					Pstmt.setInt(ii++, surfaceRD.getOneMinAvgVisibility().getValue());
					Pstmt.setInt(ii++, surfaceRD.getMinVisibility().getValue());
					
					Pstmt.setInt(ii++, surfaceRD.getTimeOfMinVisibility().getValue());
					Pstmt.setInt(ii++, surfaceRD.getRoadSurfCondition().get(0).getValue());
					Pstmt.setInt(ii++, surfaceRD.getRoadSurfCondition().get(1).getValue());
					Pstmt.setInt(ii++, surfaceRD.getRoadSurfCondition().get(2).getValue());
					if(surfaceRD.getSnowThickness().getValue() == 999999 || surfaceRD.getSnowThickness().getValue() == 999998) {
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getSnowThickness().getValue())));
					}else {
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getSnowThickness().getValue()*0.1)));
					}
					
					Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getWaterThickness().getValue())));
					Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getIceThickness().getValue())));
					Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getFreezingPointTemperature().getValue())));
					Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getConcentrationOfSnowMeltAgent().getValue())));
					Pstmt.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(0).getValue());
					
					Pstmt.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(1).getValue());
					Pstmt.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(2).getValue());
					Pstmt.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(3).getValue());
					Pstmt.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(4).getValue());
					Pstmt.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(5).getValue());
					if(surfaceRD.getQualityControl().substring(1, 2).equals("9")) {
						Pstmt.setInt(ii++, surfaceRD.getAirTemperature().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getMaxAirTemperature().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getTimeOfMaxAirTemperature().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getMinAirTemperature().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getTimeOfMinAirTemperature().getQuality().get(0).getCode());
						
						Pstmt.setInt(ii++, surfaceRD.getDewpointTemperature().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getRelativeHumidity().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getMinRelativeHumidity().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getTimeOfMinRelativeHumidity().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getWaterVaporPressure().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getHourlyCumulativePrecipitation().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getTwoMinWindDirection().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getTwoMinWindAvgSpeed().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getTenMinWindDirection().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getTenMinWindAvgSpeed().getQuality().get(0).getCode());
						
						Pstmt.setInt(ii++, surfaceRD.getDirectionOfMaxWindSpeed().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getMaxWindSpeed().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getTimeOfMaxWindSpeed().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getInstantaneousWindDirection().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getInstantaneousWindSpeed().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getDirectionOfExtremWindSpeed().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getExtremWindSpeed().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getTimeOfExtremWindSpeed().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getDirectionOfExtremWindSpeedMinutely().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getExtremeWindSpeedMinutely().getQuality().get(0).getCode());
						
						Pstmt.setInt(ii++, surfaceRD.getRoadSurfTemperature().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getMaxRoadSurfTemperature().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getTimeOfMaxRoadSurfTemperature().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getMinRoadSurfTemperature().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getTimeOfMinRoadSurfTemperature().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getRoadBaseTemperature().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getOneMinAvgVisibility().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getMinVisibility().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getTimeOfMinVisibility().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getRoadSurfCondition().get(0).getQuality().get(0).getCode());
						
						Pstmt.setInt(ii++, surfaceRD.getRoadSurfCondition().get(1).getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getRoadSurfCondition().get(2).getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getSnowThickness().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getWaterThickness().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getIceThickness().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getFreezingPointTemperature().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getConcentrationOfSnowMeltAgent().getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(0).getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(1).getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(2).getQuality().get(0).getCode());
						
						Pstmt.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(3).getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(4).getQuality().get(0).getCode());
						Pstmt.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(5).getQuality().get(0).getCode());
					}else {
						Pstmt.setInt(ii++, surfaceRD.getAirTemperature().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getMaxAirTemperature().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getTimeOfMaxAirTemperature().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getMinAirTemperature().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getTimeOfMinAirTemperature().getQuality().get(1).getCode());
						
						Pstmt.setInt(ii++, surfaceRD.getDewpointTemperature().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getRelativeHumidity().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getMinRelativeHumidity().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getTimeOfMinRelativeHumidity().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getWaterVaporPressure().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getHourlyCumulativePrecipitation().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getTwoMinWindDirection().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getTwoMinWindAvgSpeed().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getTenMinWindDirection().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getTenMinWindAvgSpeed().getQuality().get(1).getCode());
						
						Pstmt.setInt(ii++, surfaceRD.getDirectionOfMaxWindSpeed().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getMaxWindSpeed().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getTimeOfMaxWindSpeed().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getInstantaneousWindDirection().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getInstantaneousWindSpeed().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getDirectionOfExtremWindSpeed().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getExtremWindSpeed().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getTimeOfExtremWindSpeed().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getDirectionOfExtremWindSpeedMinutely().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getExtremeWindSpeedMinutely().getQuality().get(1).getCode());
						
						Pstmt.setInt(ii++, surfaceRD.getRoadSurfTemperature().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getMaxRoadSurfTemperature().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getTimeOfMaxRoadSurfTemperature().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getMinRoadSurfTemperature().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getTimeOfMinRoadSurfTemperature().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getRoadBaseTemperature().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getOneMinAvgVisibility().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getMinVisibility().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getTimeOfMinVisibility().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getRoadSurfCondition().get(0).getQuality().get(1).getCode());
						
						Pstmt.setInt(ii++, surfaceRD.getRoadSurfCondition().get(1).getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getRoadSurfCondition().get(2).getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getSnowThickness().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getWaterThickness().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getIceThickness().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getFreezingPointTemperature().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getConcentrationOfSnowMeltAgent().getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(0).getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(1).getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(2).getQuality().get(1).getCode());
						
						Pstmt.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(3).getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(4).getQuality().get(1).getCode());
						Pstmt.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(5).getQuality().get(1).getCode());
					}

					Pstmt.setString(ii++, surfaceRD.getStationNumberChina());
//					Pstmt.setInt(ii++, surfaceRD.getObservationTime().getYear() + 1900);
//					Pstmt.setInt(ii++, surfaceRD.getObservationTime().getMonth() + 1);
//					Pstmt.setInt(ii++, surfaceRD.getObservationTime().getDate());
//					Pstmt.setInt(ii++, surfaceRD.getObservationTime().getHours());
//					Pstmt.setInt(ii++, surfaceRD.getObservationTime().getMinutes());
					
					Pstmt.setTimestamp(ii++, new Timestamp(surfaceRD.getObservationTime().getTime()));
					
					di.setIIiii(surfaceRD.getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(surfaceRD.getObservationTime(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLATITUDE(surfaceRD.getLatitude().toString());
					di.setLONGTITUDE(surfaceRD.getLongitude().toString());
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(fileN).length()));
					di.setDATA_UPDATE_FLAG(surfaceRD.getFileRevisionSign());
					di.setHEIGHT(String.valueOf(surfaceRD.getHeightOfSationGroundAboveMeanSeaLevel()));
					
					listDi.add(di);
					try{
						Pstmt.execute();
						connection.commit();
					}catch(SQLException e){							
						if(listDi.size() > 0)
							listDi.get(listDi.size() - 1).setPROCESS_STATE("0");//1成功，0失败
						infoLogger.error("\n File name: "+fileN
								+"\n execute sql error: "+((LoggableStatement)Pstmt).getQueryString()+"\n "+e.getMessage());
					}	
				} 
			}
			catch (SQLException e) {
				infoLogger.error("\n Create Statement error#################: "+ e.getMessage());
			}
			finally {
				try {
					if(Pstmt != null)
						Pstmt.close();
					Pstmt = null;
					System.out.println("#################Pstmt.close()");
				} catch (SQLException e) {
					infoLogger.error("\n Close Statement error: "+ e.getMessage());
				}	
			}
		} 
		else 
			infoLogger.error("\n Database connection error!");
	} 
	/**
	 * 
	 * @Title: updatePreDB   
	 * @Description: TODO(路面交通小时内分钟降水入库-更正报，逐条入库)   
	 * @param  surfaceRDs 要素对象，更正报：数据库中已经有该条数据
	 * @param  connection 数据库连接
	 * @param recv_time  资料时间
	 * @return: void      
	 * @throws:
	 */

	@SuppressWarnings("deprecation")
	private static void updatePreDB(List<SurfaceRD> surfaceRDs, java.sql.Connection connection, Date recv_time, String fileN) {
		PreparedStatement Pstmt = null;
		SurfaceRD surfaceRD = new SurfaceRD();
		String sql = "update "+StartConfig.valueTable()+" set "
				+ "V_BBB=?, D_UPDATE_TIME=?,V_SQCODE=?,V_PQCODE=?,V_NQCODE=?, V13019=?,V13011=?,Q13019=?,Q13011=? "
				+ "where V01301 = ? and D_datetime= ?";
		if(connection != null){
			try {
				connection.setAutoCommit(true);
				Pstmt = new LoggableStatement(connection, sql);
				for(int idx = 0; idx < surfaceRDs.size(); idx ++){
					surfaceRD = surfaceRDs.get(idx);
					// 如果是整点时间，则降水的120Bytes为前一小时60个分钟的降水量，（不再处理的：如果不是整点时间，则降水为当前小时、前面几十分钟的降水，）
					Date dt = surfaceRD.getObservationTime();
					Calendar cal = Calendar.getInstance();
					cal.setTime(dt);
					int mins = cal.get(Calendar.MINUTE);
					if(mins == 0){
						cal.add(Calendar.HOUR_OF_DAY, -1);
						mins = 60;
					}
					else{
						// 不处理非整点的降水数据
						continue;
					}
					
//					StatDi di = new StatDi();				
//					di.setFILE_NAME_O(fileN);
//					di.setDATA_TYPE(sod_code_pre);
//					di.setDATA_TYPE_1(cts_code);
//					di.setTT(report_type);			
//					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
//					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
//					di.setFILE_NAME_N(fileN);
//					di.setBUSINESS_STATE("1"); //1成功，0失败
//					di.setPROCESS_STATE("1");  //1成功，0失败	
					
					dt = cal.getTime();
					for(int i = 0; i < mins; i ++){
						dt.setMinutes(i+1);
						int ii = 1;
						
						StatDi di = new StatDi();				
						di.setFILE_NAME_O(fileN);
						di.setDATA_TYPE(sod_code_pre);
						di.setDATA_TYPE_1(cts_code);
						di.setTT(report_type);			
						di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
						di.setPROCESS_START_TIME(TimeUtil.getSysTime());
						di.setFILE_NAME_N(fileN);
						di.setBUSINESS_STATE("1"); //1成功，0失败
						di.setPROCESS_STATE("1");  //1成功，0失败	
						
						Pstmt.setString(ii++, surfaceRD.getFileRevisionSign());
						Pstmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						Pstmt.setInt(ii++, Character.getNumericValue(surfaceRD.getQualityControl().charAt(0)));
						Pstmt.setInt(ii++, Character.getNumericValue(surfaceRD.getQualityControl().charAt(1)));
						Pstmt.setInt(ii++, Character.getNumericValue(surfaceRD.getQualityControl().charAt(2)));
						
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getHourlyCumulativePrecipitation().getValue())));
						Pstmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getMinutelyPrecipitation().get(i).getValue())));
						if(surfaceRD.getQualityControl().substring(1, 2).equals("9")){
							Pstmt.setInt(ii++, surfaceRD.getHourlyCumulativePrecipitation().getQuality().get(0).getCode());
							Pstmt.setInt(ii++, surfaceRD.getMinutelyPrecipitation().get(i).getQuality().get(0).getCode());
						}else{
							Pstmt.setInt(ii++, surfaceRD.getHourlyCumulativePrecipitation().getQuality().get(1).getCode());
							Pstmt.setInt(ii++, surfaceRD.getMinutelyPrecipitation().get(i).getQuality().get(1).getCode());
						}
						Pstmt.setString(ii++, surfaceRD.getStationNumberChina());
//						Pstmt.setInt(ii++, dt.getYear() + 1900);
//						Pstmt.setInt(ii++, dt.getMonth() + 1);
//						Pstmt.setInt(ii++, dt.getDate());
//						Pstmt.setInt(ii++, dt.getHours());
//						Pstmt.setInt(ii++, dt.getMinutes());
						Pstmt.setTimestamp(ii++, new Timestamp(dt.getTime()));
						
						di.setIIiii(surfaceRD.getStationNumberChina());
						di.setDATA_TIME(TimeUtil.date2String(dt, "yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setLATITUDE(surfaceRD.getLatitude().toString());
						di.setLONGTITUDE(surfaceRD.getLongitude().toString());
						
						di.setSEND("BFDB");
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(new File(fileN).length()));
						di.setDATA_UPDATE_FLAG(surfaceRD.getFileRevisionSign());
						di.setHEIGHT(String.valueOf(surfaceRD.getHeightOfSationGroundAboveMeanSeaLevel()));
						listDi.add(di);
												
						try{
							Pstmt.execute();
						}catch(SQLException e){				
//							if(listDi.size() > 0)
//								listDi.get(listDi.size() - 1).setPROCESS_STATE("0");//1成功，0失败
							di.setPROCESS_STATE("0");
							infoLogger.error("\n File name"+fileN
									+"\n execute sql error: "+((LoggableStatement)Pstmt).getQueryString()+"\n "+e.getMessage());
						}	
					} 
//					di.setIIiii(surfaceRD.getStationNumberChina());
//					di.setDATA_TIME(TimeUtil.date2String(surfaceRD.getObservationTime(), "yyyy-MM-dd HH:mm"));
//					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
//					di.setRECORD_TIME(TimeUtil.getSysTime());
//					di.setLATITUDE(surfaceRD.getLatitude().toString());
//					di.setLONGTITUDE(surfaceRD.getLongitude().toString());
//					
//					di.setSEND("BFDB");
//					di.setSEND_PHYS("DRDS");
//					di.setFILE_SIZE(String.valueOf(new File(fileN).length()));
//					di.setDATA_UPDATE_FLAG(surfaceRD.getFileRevisionSign());
//					di.setHEIGHT(String.valueOf(surfaceRD.getHeightOfSationGroundAboveMeanSeaLevel()));
//					listDi.add(di);
				} 
			}
			catch (SQLException e) {
				infoLogger.error("\n Create Statement error$$$$$$$$$$$$$: "+ e.getMessage());
			}
			finally {
				try {
					if(Pstmt != null)
						Pstmt.close();
					Pstmt = null;
				} catch (SQLException e) {
					infoLogger.error("\n Close Statement error: "+ e.getMessage());
				}	
			}
		} 
		else 
			infoLogger.error("\n Database connection error!");
	}

	/**
	 * 
	 * @Title: insertPreDB   
	 * @Description: TODO(地面交通小时内分钟降水数据入库)   
	 * @param  surfaceRDs 入库对象
	 * @param  connection 数据库连接
	 * @param  recv_time  资料时间
	 * @return: void      
	 * @throws:
	 */
	@SuppressWarnings("deprecation")
	private static void insertPreDB(List<SurfaceRD> surfaceRDs, java.sql.Connection connection, Date recv_time,String fileN) {
		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement pStatement = null;
		if(connection != null){
			List<SurfaceRD> surfaceRDs2 = new ArrayList<>();
			try {
				connection.setAutoCommit(false);
				String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,D_UPDATE_TIME,"
						+ "V_SQCODE,V_PQCODE,V_NQCODE,V01301,V01300,V05001,V06001,V07001,V02001,V02301,"
						+ "V_ACODE,V04001,V04002,V04003,V04004,V04005,V04006,"
						+ "V13019,V13011,Q13019,Q13011,V_BBB, D_SOURCE_ID"
						+ ") "
						+ "VALUES (?,?, ?, ?, ?, ?,"
						+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
						+ " ?, ?, ?, ?, ?, ?, ?, "
						+ " ?, ?, ?, ?, ?, ?)";	
				pStatement = new LoggableStatement(connection, sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				List<String> sqls = new ArrayList<>();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				int stationNumberN = 999999;
				for(int idx = 0; idx < surfaceRDs.size(); idx ++){
					SurfaceRD surfaceRD = new SurfaceRD();
					surfaceRD = surfaceRDs.get(idx);
					// 如果是整点时间，则降水的120Bytes为前一小时60个分钟的降水量，如果不是整点时间，则降水为当前小时、前面几十分钟的降水
					Date dt = surfaceRD.getObservationTime();
					Calendar cal = Calendar.getInstance();
					cal.setTime(dt);
					
					int mins = cal.get(Calendar.MINUTE);
					int StartMinute = 0;
					int MinsLen = 0;
					if(mins == 0){
						cal.add(Calendar.HOUR_OF_DAY, -1);
						MinsLen  = 60;
					}
					else{
						//不是整点时间的过去1小时降水，不入库
						continue;
					}
					
					if(surfaceRD.getFileRevisionSign().equals("000")){
//						StatDi di = new StatDi();					
//						di.setFILE_NAME_O(fileN);
//						di.setDATA_TYPE(sod_code_pre);
//						di.setDATA_TYPE_1(cts_code);
//						di.setTT(report_type);			
//						di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
//						di.setPROCESS_START_TIME(TimeUtil.getSysTime());
//						di.setFILE_NAME_N(fileN);
//						di.setBUSINESS_STATE("1"); //1成功，0失败
//						di.setPROCESS_STATE("1");  //1成功，0失败	
						
						// 非更正报；或者为更正报，但是数据库中尚未有这一条数据
						String stat = surfaceRD.getStationNumberChina();
						if (Pattern.matches("\\d{5}", stat))
							stationNumberN = Integer.parseInt(stat);					
						else
							stationNumberN = Integer.parseInt(StationCodeUtil.stringToAscii(stat));
						String info = (String) proMap.get(stat + "+01");
						int adminCode = 999999;
						int stationLevel = 999999;
						int stationType = 40;
						if(info == null) {
							infoLogger.error("\n In configuration file, this station does not exist: " + stat);
						//	continue ;
						}
						else{
							String[] infos = info.split(",");
							if(!infos[5].equals("null"))
								adminCode = Integer.parseInt(infos[5]);
							if(!infos[6].equals("null"))
								stationLevel = Integer.parseInt(infos[6]); 
						}
						
//						// 如果是整点时间，则降水的120Bytes为前一小时60个分钟的降水量，如果不是整点时间，则降水为当前小时、前面几十分钟的降水
//						Date dt = surfaceRD.getObservationTime();
//						Calendar cal = Calendar.getInstance();
//						cal.setTime(dt);
//						int mins = cal.get(Calendar.MINUTE);
//						int StartMinute = 0;
//						int MinsLen = 0;
//						if(LastProcessed.containsKey(stat)){  // 当前站，在当前的时间(精确到分钟)前的降水数据已入库
//							Date lastT = new Date(LastProcessed.get(stat).getTime());
//							if(lastT.after(dt))
//								continue ;
//							else{ // 当前站，当前时间（精确到分钟） 的降水数据需要入库，计算
//								// 如果为同一小时，分钟不同，计算分钟差
//								Long diff = (dt.getTime() - lastT.getTime()) / 1000 / 60; // 相差的分钟数
//								if(mins == 0){
//									cal.set(10, cal.get(10) - 1);
//									if(MinsLen <= 60){  // 当前为整点时间，上次录入在几十分钟前，录入上一小时最后几十分钟的数据
//										MinsLen = diff.intValue();
//										StartMinute = 60 - MinsLen;
//									}
//									else if(MinsLen > 60){
//										MinsLen = 60;
//									}
//								}
//								else if(mins > 0) {
//									if(MinsLen <= 60 && dt.getHours() == lastT.getHours()) {
//										MinsLen = diff.intValue();
//										StartMinute = lastT.getMinutes();
//									}
//									else if(MinsLen <= 60 && dt.getHours() == lastT.getHours() + 1){
//										MinsLen = mins;
//									}
//									else if(MinsLen > 60){
//										MinsLen = mins;
//									}
//								}
//								LastProcessed.put(stat, dt);
//							}
//						}
//						else { // stat 还未入库，不在库中
//							if(mins == 0){
//								cal.add(Calendar.HOUR_OF_DAY, -1);
//								MinsLen  = 60;
//							}
//							else{
////								MinsLen = mins;
//								//不是整点时间的过去1小时降水，不入库
//								continue;
//							}
//							LastProcessed.put(stat, dt);
//						}
						dt = cal.getTime();
						
						for(int i = StartMinute; i < StartMinute + MinsLen; i ++){ // 小时内分钟降水,2个Byte为一个降水
							StatDi di = new StatDi();					
							di.setFILE_NAME_O(fileN);
							di.setDATA_TYPE(sod_code_pre);
							di.setDATA_TYPE_1(cts_code);
							di.setTT(report_type);			
							di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
							di.setPROCESS_START_TIME(TimeUtil.getSysTime());
							di.setFILE_NAME_N(fileN);
							di.setBUSINESS_STATE("1"); //1成功，0失败
							di.setPROCESS_STATE("1");  //1成功，0失败	
							int ii = 1;
							dt.setMinutes(i+1);
							pStatement.setString(ii++, sdf.format(dt)+"_"+surfaceRD.getStationNumberChina()+"_"+(int)(surfaceRDs.get(idx).getLatitude()*1e6)+"_"+(int)(surfaceRDs.get(idx).getLongitude()*1e6));
							pStatement.setString(ii++, sod_code_pre);
							pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
							pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
							pStatement.setTimestamp(ii++, new Timestamp(dt.getTime()));
							pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
							
							pStatement.setInt(ii++, Character.getNumericValue(surfaceRD.getQualityControl().charAt(0)));
							pStatement.setInt(ii++, Character.getNumericValue(surfaceRD.getQualityControl().charAt(1)));
							pStatement.setInt(ii++, Character.getNumericValue(surfaceRD.getQualityControl().charAt(2)));
							pStatement.setString(ii++, surfaceRD.getStationNumberChina());
							pStatement.setInt(ii++, stationNumberN);
							pStatement.setBigDecimal(ii++, new BigDecimal(surfaceRD.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
							pStatement.setBigDecimal(ii++, new BigDecimal(surfaceRD.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
							pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getHeightOfSationGroundAboveMeanSeaLevel())));
							//测站类型
							pStatement.setInt(ii++, stationType); 
							// 台站级别
							pStatement.setInt(ii++, stationLevel); 
							
							pStatement.setInt(ii++, adminCode);
							pStatement.setInt(ii++, dt.getYear() + 1900);
							pStatement.setInt(ii++, dt.getMonth() + 1);
							pStatement.setInt(ii++, dt.getDate());
							pStatement.setInt(ii++, dt.getHours());
							pStatement.setInt(ii++, dt.getMinutes());
							pStatement.setInt(ii++, dt.getSeconds());
							
							pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getHourlyCumulativePrecipitation().getValue())));
							pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getMinutelyPrecipitation().get(i).getValue())));
							if(surfaceRD.getQualityControl().substring(1, 2).equals("9")){
								pStatement.setInt(ii++, surfaceRD.getHourlyCumulativePrecipitation().getQuality().get(0).getCode());
								pStatement.setInt(ii++, surfaceRD.getMinutelyPrecipitation().get(i).getQuality().get(0).getCode());
							}else{
								pStatement.setInt(ii++, surfaceRD.getHourlyCumulativePrecipitation().getQuality().get(1).getCode());
								pStatement.setInt(ii++, surfaceRD.getMinutelyPrecipitation().get(i).getQuality().get(1).getCode());
							}
							pStatement.setString(ii++, surfaceRD.getFileRevisionSign());
							pStatement.setString(ii++, cts_code);
														
							pStatement.addBatch();
							sqls.add(((LoggableStatement)pStatement).getQueryString()); // 批量入库冲突时，单条入库
							
							di.setIIiii(surfaceRD.getStationNumberChina());
							
							di.setDATA_TIME(TimeUtil.date2String(dt, "yyyy-MM-dd HH:mm"));
							di.setPROCESS_END_TIME(TimeUtil.getSysTime());
							di.setRECORD_TIME(TimeUtil.getSysTime());	
							di.setLATITUDE(surfaceRD.getLatitude().toString());
							di.setLONGTITUDE(surfaceRD.getLongitude().toString());
							
							di.setSEND("BFDB");
							di.setSEND_PHYS("DRDS");
							di.setFILE_SIZE(String.valueOf(new File(fileN).length()));
							di.setDATA_UPDATE_FLAG(surfaceRD.getFileRevisionSign());
							di.setHEIGHT(String.valueOf(surfaceRD.getHeightOfSationGroundAboveMeanSeaLevel()));
							listDi.add(di);
						} 
//						di.setIIiii(surfaceRD.getStationNumberChina());
//						
//						di.setDATA_TIME(TimeUtil.date2String(surfaceRD.getObservationTime(), "yyyy-MM-dd HH:mm"));
//						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
//						di.setRECORD_TIME(TimeUtil.getSysTime());	
//						di.setLATITUDE(surfaceRD.getLatitude().toString());
//						di.setLONGTITUDE(surfaceRD.getLongitude().toString());
//						
//						di.setSEND("BFDB");
//						di.setSEND_PHYS("DRDS");
//						di.setFILE_SIZE(String.valueOf(new File(fileN).length()));
//						di.setDATA_UPDATE_FLAG(surfaceRD.getFileRevisionSign());
//						di.setHEIGHT(String.valueOf(surfaceRD.getHeightOfSationGroundAboveMeanSeaLevel()));
//						listDi.add(di);
						try{
							pStatement.executeBatch();
							connection.commit();
							sqls.clear();
							infoLogger.info("\n Batch commit success: "+fileN);
						}catch(SQLException e){
							infoLogger.error("\n Batch commit failed: "+fileN);
							pStatement.clearParameters();
							pStatement.clearBatch();
							execute_sql(sqls, connection, fileN, 1); // 此中，入库失败的会将对应的DI的ProcessState置为1 // 1:失败，0：成功
							sqls.clear();
						}
					} 
					else{
						surfaceRDs2.add(surfaceRD); // 更正报，需要update操作,数据库中有该条记录，且更正标识早于当前处理文件的更正标识的值
					}
					
				}
				
				if(surfaceRDs2.size() > 0)
					updatePreDB(surfaceRDs2, connection, recv_time, fileN);
				
			}catch (Exception e) {
				e.printStackTrace();
				infoLogger.error("\n Create Statement error(((((((((((: "+e.getMessage());
			}finally {
				if(pStatement != null) {
					try {
						pStatement.close();
						pStatement = null;
					} catch (SQLException e) {
						infoLogger.error("\n Close Statement error: "+e.getMessage());
					}
				}
				try {
					connection.close();
					connection = null;
				} catch (SQLException e) {
					infoLogger.error("\n Close connection error: "+e.getMessage());
				}
			}
		} 
	}
	/**
	 * 
	 * @Title: insertEleDB   
	 * @Description: TODO(路面交通基本气象要素入库)   
	 * @param  surfaceRDs 入库对象
	 * @param  connection 数据库连接
	 * @param  recv_time  资料时间
	 * @return: void      
	 * @throws:
	 */
	@SuppressWarnings("deprecation")
	private static void insertEleDB(List<SurfaceRD> surfaceRDs, java.sql.Connection connection, Date recv_time,String fileN) {
		Map<String, Object> proMap = StationInfo.getProMap();
		// TODO Auto-generated method stub
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.keyTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,D_UPDATE_TIME,"
				+ "V_SQCODE,V_PQCODE,V_NQCODE,V01301,V01300,V05001,V06001,V07001,V02001,V02301,"
				+ "V_ACODE,V04001,V04002,V04003,V04004,V04005,V04006,V12001,V12011,V12011_052,"
				+ "V12012,V12012_052,V12003,V13003,V13007,V13007_052,V13004,V13019,V11290,V11291,"
				+ "V11292,V11293,V11296,V11042,V11042_052,V11001,V11002,V11211,V11046,V11046_052,"
				+ "V11211_001,V11046_001,V12421,V12422,V12422_052,V12423,V12423_052,V12424,V20001_701_01,V20059,"
				+ "V20059_052,V20062_1,V20062_2,V20062_3,V13013,V13368,V13369,V12425,V15505,V20003_01,"
				+ "V20003_02,V20003_03,V20003_04,V20003_05,V20003_06,Q12001,Q12011,Q12011_052,Q12012,Q12012_052,"
				+ "Q12003,Q13003,Q13007,Q13007_052,Q13004,Q13019,Q11290,Q11291,Q11292,Q11293,"
				+ "Q11296,Q11042,Q11042_052,Q11001,Q11002,Q11211,Q11046,Q11046_052,Q11211_001,Q11046_001,"
				+ "Q12421,Q12422,Q12422_052,Q12423,Q12423_052,Q12424,Q20001_701_01,Q20059,Q20059_052,Q20062_1,"
				+ "Q20062_2,Q20062_3,Q13013,Q13368,Q13369,Q12425,Q15505,Q20003_01,Q20003_02,Q20003_03,"
				+ "Q20003_04,Q20003_05,Q20003_06,v_bbb, D_SOURCE_ID"
				+ ") "
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
				+ " ?, ?, ?, ?, ?"
				+ ")";
		List<SurfaceRD> surfaceRDs2 = new ArrayList<>();
		if(connection != null){		
			try {	
//				pStatement = connection.prepareStatement(sql);
				pStatement = new LoggableStatement(connection, sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				List<String> sqls = new ArrayList<>();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");		
				String bbb;
				for(int idx = 0; idx < surfaceRDs.size(); idx ++){
					SurfaceRD surfaceRD = surfaceRDs.get(idx);
//					bbb = findVBBSurfRDEle(surfaceRD, connection);
					if(surfaceRD.getFileRevisionSign().equals("000")){
						// 非更正报；或者为更正报，但是数据库中尚未有这一条数据
						StatDi di = new StatDi();	
						di.setFILE_NAME_O(fileN);
						di.setDATA_TYPE(sod_code_ele);
						di.setDATA_TYPE_1(cts_code);
						di.setTT(report_type);			
						di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
						di.setPROCESS_START_TIME(TimeUtil.getSysTime());
						di.setFILE_NAME_N(fileN);
						di.setBUSINESS_STATE("1"); //1成功，0失败
						di.setPROCESS_STATE("1");  //1成功，0失败	
						
						int stationNumberN = 999999;
						String stat = surfaceRD.getStationNumberChina();
						if (Pattern.matches("\\d{5}", stat))
							stationNumberN = Integer.parseInt(stat);
						else
							stationNumberN = Integer.parseInt(StationCodeUtil.stringToAscii(stat));
							
						String info = (String) proMap.get(stat + "+01");
						int adminCode = 999999;
						int stationLevel = 999999;
						int stationType = 40;
						if(info == null) {
							infoLogger.error("\n In configuration file, this station does not exist: " + stat);
						//	continue ;
						}
						else{
							String[] infos = info.split(",");
							if(!infos[5].equals("null"))
								adminCode = Integer.parseInt(infos[5]);
							if(!infos[6].equals("null"))
								stationLevel = Integer.parseInt(infos[6]); 
						}
						int ii = 1;
						pStatement.setString(ii++, sdf.format(surfaceRDs.get(idx).getObservationTime())+"_"+surfaceRDs.get(idx).getStationNumberChina()+"_"+(int)(surfaceRDs.get(idx).getLatitude()*1e6)+"_"+(int)(surfaceRDs.get(idx).getLongitude()*1e6));
						pStatement.setString(ii++, sod_code_ele);
						pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
						pStatement.setTimestamp(ii++, new Timestamp(surfaceRD.getObservationTime().getTime()));
						pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
						
						pStatement.setInt(ii++, Character.getNumericValue(surfaceRD.getQualityControl().charAt(0)));
						pStatement.setInt(ii++, Character.getNumericValue(surfaceRD.getQualityControl().charAt(1)));
						pStatement.setInt(ii++, Character.getNumericValue(surfaceRD.getQualityControl().charAt(2)));
						pStatement.setString(ii++, surfaceRD.getStationNumberChina());
						pStatement.setInt(ii++, stationNumberN);
						pStatement.setBigDecimal(ii++, new BigDecimal(surfaceRD.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
						pStatement.setBigDecimal(ii++, new BigDecimal(surfaceRD.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getHeightOfSationGroundAboveMeanSeaLevel())));
						//测站类型
						pStatement.setInt(ii++, stationType); 
						// 台站级别
						pStatement.setInt(ii++, stationLevel); 
						
						pStatement.setInt(ii++, adminCode);
						pStatement.setInt(ii++, surfaceRD.getObservationTime().getYear() + 1900);
						pStatement.setInt(ii++, surfaceRD.getObservationTime().getMonth() + 1);
						pStatement.setInt(ii++, surfaceRD.getObservationTime().getDate());
						pStatement.setInt(ii++, surfaceRD.getObservationTime().getHours());
						pStatement.setInt(ii++, surfaceRD.getObservationTime().getMinutes());
						pStatement.setInt(ii++, surfaceRD.getObservationTime().getSeconds());
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getAirTemperature().getValue())));
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getMaxAirTemperature().getValue())));
						pStatement.setInt(ii++, surfaceRD.getTimeOfMaxAirTemperature().getValue());
						
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getMinAirTemperature().getValue())));
						pStatement.setInt(ii++, surfaceRD.getTimeOfMinAirTemperature().getValue());
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getDewpointTemperature().getValue())));
						pStatement.setInt(ii++, surfaceRD.getRelativeHumidity().getValue());
						pStatement.setInt(ii++, surfaceRD.getMinRelativeHumidity().getValue());
						pStatement.setInt(ii++, surfaceRD.getTimeOfMinRelativeHumidity().getValue());
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getWaterVaporPressure().getValue())));
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getHourlyCumulativePrecipitation().getValue())));
						pStatement.setInt(ii++, surfaceRD.getTwoMinWindDirection().getValue());
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getTwoMinWindAvgSpeed().getValue())));
						
						pStatement.setInt(ii++, surfaceRD.getTenMinWindDirection().getValue());
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getTenMinWindAvgSpeed().getValue())));
						pStatement.setInt(ii++, surfaceRD.getDirectionOfMaxWindSpeed().getValue());
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getMaxWindSpeed().getValue())));
						pStatement.setInt(ii++, surfaceRD.getTimeOfMaxWindSpeed().getValue());
						pStatement.setInt(ii++, surfaceRD.getInstantaneousWindDirection().getValue());
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getInstantaneousWindSpeed().getValue())));
						pStatement.setInt(ii++, surfaceRD.getDirectionOfExtremWindSpeed().getValue());
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getExtremWindSpeed().getValue())));
						pStatement.setInt(ii++, surfaceRD.getTimeOfExtremWindSpeed().getValue());
						
						pStatement.setInt(ii++, surfaceRD.getDirectionOfExtremWindSpeedMinutely().getValue());
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getExtremeWindSpeedMinutely().getValue())));
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getRoadSurfTemperature().getValue())));
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getMaxRoadSurfTemperature().getValue())));
						pStatement.setInt(ii++, surfaceRD.getTimeOfMaxRoadSurfTemperature().getValue());
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getMinRoadSurfTemperature().getValue())));
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getTimeOfMinRoadSurfTemperature().getValue())));
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getRoadBaseTemperature().getValue())));
						pStatement.setInt(ii++, surfaceRD.getOneMinAvgVisibility().getValue());
						pStatement.setInt(ii++, surfaceRD.getMinVisibility().getValue());
						
						pStatement.setInt(ii++, surfaceRD.getTimeOfMinVisibility().getValue());
						pStatement.setInt(ii++, surfaceRD.getRoadSurfCondition().get(0).getValue());
						pStatement.setInt(ii++, surfaceRD.getRoadSurfCondition().get(1).getValue());
						pStatement.setInt(ii++, surfaceRD.getRoadSurfCondition().get(2).getValue());
						if(surfaceRD.getSnowThickness().getValue() == 999999 || surfaceRD.getSnowThickness().getValue()==999998) {
							pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getSnowThickness().getValue())));
						}else {
							pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getSnowThickness().getValue()*0.1)));
						}
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getWaterThickness().getValue())));
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getIceThickness().getValue())));
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getFreezingPointTemperature().getValue())));
						pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfaceRD.getConcentrationOfSnowMeltAgent().getValue())));
						pStatement.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(0).getValue());
						
						pStatement.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(1).getValue());
						pStatement.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(2).getValue());
						pStatement.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(3).getValue());
						pStatement.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(4).getValue());
						pStatement.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(5).getValue());
						if(surfaceRD.getQualityControl().substring(1, 2).equals("9")) {
							pStatement.setInt(ii++, surfaceRD.getAirTemperature().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getMaxAirTemperature().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getTimeOfMaxAirTemperature().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getMinAirTemperature().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getTimeOfMinAirTemperature().getQuality().get(0).getCode());
							
							pStatement.setInt(ii++, surfaceRD.getDewpointTemperature().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getRelativeHumidity().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getMinRelativeHumidity().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getTimeOfMinRelativeHumidity().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getWaterVaporPressure().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getHourlyCumulativePrecipitation().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getTwoMinWindDirection().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getTwoMinWindAvgSpeed().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getTenMinWindDirection().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getTenMinWindAvgSpeed().getQuality().get(0).getCode());
							
							pStatement.setInt(ii++, surfaceRD.getDirectionOfMaxWindSpeed().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getMaxWindSpeed().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getTimeOfMaxWindSpeed().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getInstantaneousWindDirection().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getInstantaneousWindSpeed().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getDirectionOfExtremWindSpeed().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getExtremWindSpeed().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getTimeOfExtremWindSpeed().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getDirectionOfExtremWindSpeedMinutely().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getExtremeWindSpeedMinutely().getQuality().get(0).getCode());
							
							pStatement.setInt(ii++, surfaceRD.getRoadSurfTemperature().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getMaxRoadSurfTemperature().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getTimeOfMaxRoadSurfTemperature().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getMinRoadSurfTemperature().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getTimeOfMinRoadSurfTemperature().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getRoadBaseTemperature().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getOneMinAvgVisibility().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getMinVisibility().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getTimeOfMinVisibility().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getRoadSurfCondition().get(0).getQuality().get(0).getCode());
							
							pStatement.setInt(ii++, surfaceRD.getRoadSurfCondition().get(1).getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getRoadSurfCondition().get(2).getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getSnowThickness().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getWaterThickness().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getIceThickness().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getFreezingPointTemperature().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getConcentrationOfSnowMeltAgent().getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(0).getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(1).getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(2).getQuality().get(0).getCode());
							
							pStatement.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(3).getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(4).getQuality().get(0).getCode());
							pStatement.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(5).getQuality().get(0).getCode());
						}else {
							pStatement.setInt(ii++, surfaceRD.getAirTemperature().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getMaxAirTemperature().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getTimeOfMaxAirTemperature().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getMinAirTemperature().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getTimeOfMinAirTemperature().getQuality().get(1).getCode());
							
							pStatement.setInt(ii++, surfaceRD.getDewpointTemperature().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getRelativeHumidity().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getMinRelativeHumidity().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getTimeOfMinRelativeHumidity().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getWaterVaporPressure().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getHourlyCumulativePrecipitation().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getTwoMinWindDirection().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getTwoMinWindAvgSpeed().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getTenMinWindDirection().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getTenMinWindAvgSpeed().getQuality().get(1).getCode());
							
							pStatement.setInt(ii++, surfaceRD.getDirectionOfMaxWindSpeed().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getMaxWindSpeed().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getTimeOfMaxWindSpeed().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getInstantaneousWindDirection().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getInstantaneousWindSpeed().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getDirectionOfExtremWindSpeed().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getExtremWindSpeed().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getTimeOfExtremWindSpeed().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getDirectionOfExtremWindSpeedMinutely().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getExtremeWindSpeedMinutely().getQuality().get(1).getCode());
							
							pStatement.setInt(ii++, surfaceRD.getRoadSurfTemperature().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getMaxRoadSurfTemperature().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getTimeOfMaxRoadSurfTemperature().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getMinRoadSurfTemperature().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getTimeOfMinRoadSurfTemperature().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getRoadBaseTemperature().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getOneMinAvgVisibility().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getMinVisibility().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getTimeOfMinVisibility().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getRoadSurfCondition().get(0).getQuality().get(1).getCode());
							
							pStatement.setInt(ii++, surfaceRD.getRoadSurfCondition().get(1).getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getRoadSurfCondition().get(2).getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getSnowThickness().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getWaterThickness().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getIceThickness().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getFreezingPointTemperature().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getConcentrationOfSnowMeltAgent().getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(0).getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(1).getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(2).getQuality().get(1).getCode());
							
							pStatement.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(3).getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(4).getQuality().get(1).getCode());
							pStatement.setInt(ii++, surfaceRD.getWeatherPhenomenonCode().get(5).getQuality().get(1).getCode());
						}
						
						pStatement.setString(ii++, surfaceRD.getFileRevisionSign());
						pStatement.setString(ii++, cts_code);
						
						di.setIIiii(surfaceRD.getStationNumberChina());
						di.setDATA_TIME(TimeUtil.date2String(surfaceRD.getObservationTime(), "yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());	
						di.setLATITUDE(surfaceRD.getLatitude().toString());
						di.setLONGTITUDE(surfaceRD.getLongitude().toString());
						
						di.setSEND("BFDB");
						di.setSEND_PHYS("DRDS");
						di.setFILE_SIZE(String.valueOf(new File(fileN).length()));
						di.setDATA_UPDATE_FLAG(surfaceRD.getFileRevisionSign());
						di.setHEIGHT(String.valueOf(surfaceRD.getHeightOfSationGroundAboveMeanSeaLevel()));;
						
						
						pStatement.addBatch();
						sqls.add(((LoggableStatement)pStatement).getQueryString());
						listDi.add(di);
					} 
					else{
						surfaceRDs2.add(surfaceRD); // 更正报，需要update操作,数据库中有该条记录，且更正标识早于当前处理文件的更正标识的值
					}
				} 
				try{
					pStatement.executeBatch();
					connection.commit();
					sqls.clear();
					infoLogger.info("\n Batch commit success: "+fileN);
				}catch(SQLException e){
					infoLogger.error("\n Batch commit error！！: "+fileN);
					pStatement.clearParameters();
					pStatement.clearBatch();
					execute_sql(sqls, connection, fileN, 0); // 此中，入库失败的会将对应的DI的ProcessState置为1 // 1:失败，0：成功
					sqls.clear();
				}
				
				// 更正报的入库
				if(surfaceRDs2.size() > 0)
					updateEleDB(surfaceRDs2, connection, recv_time, fileN);
			}catch (Exception e) {
				e.printStackTrace();
				infoLogger.error("\n Create Statement error-----: "+e.getMessage());
			}finally {
				for (int j = 0; j < listDi.size(); j++) {
					diQueues.offer(listDi.get(j));
				}
				listDi.clear();
				if(pStatement != null) {
					try {
						pStatement.close();
						pStatement = null;
						System.out.println("insertEleDB pStatement.close(); ");
					} catch (SQLException e) {
						infoLogger.error("\n Close Statement error: "+e.getMessage());
					}
				}
				
			}
		} 
	}
	/**
	 * 
	 * @Title: reportInfoToDb   
	 * @Description: TODO(报文信息入库)   
	 * @param  reportInfos 报文列表集合
	 * @param  connection  数据库连接
	 * @param recv_time   资料时间
	 * @param  v_cccc      区域代码
	 * @param  v_tt        报文资料类别
	 * @return: void      
	 * @throws:
	 */
	@SuppressWarnings("deprecation")
	public static void reportInfoToDb(@SuppressWarnings("rawtypes") List<ReportInfo> reportInfos, java.sql.Connection connection, Date recv_time, String v_cccc, String v_tt) {
		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.reportTable()+" (D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,"
				+ "V_BBB,V_CCCC,V_TT,V01301,V01300,V05001,V06001,V_NCODE,V_ACODE,"
				+ "V04001,V04002,V04003,V04004,V04005,"
				+ "V_LEN,V_REPORT) VALUES"
				+ "(?,?,?,?,?,?,"
				+ " ?,?,?,?,?,?,?,?,?,"
				+ " ?,?,?,?,?,"
				+ " ?,?)";
		try {
			connection.setAutoCommit(true);
			pStatement = new LoggableStatement(connection, sql);
			if(StartConfig.getDatabaseType() == 1) {
				pStatement.execute("select last_txc_xid()");
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			for (int i = 0; i < reportInfos.size(); i++) {
				try {
					SurfaceRD surfaceRD = (SurfaceRD) reportInfos.get(i).getT();
					
					String stat = surfaceRD.getStationNumberChina();
					String info = (String) proMap.get(stat + "+01");
					int adminCode = 999999;
					if(info == null) {
						infoLogger.error("\n In configuration file, this station does not exist: " + stat);
					//	continue ;
					}
					else{
						String[] infos = info.split(",");
						adminCode = Integer.parseInt(infos[5]);
					}
					String primkey = sdf.format(surfaceRD.getObservationTime())+"_"+stat+"_"+v_tt+"_"+ surfaceRD.getFileRevisionSign();
					int ii = 1;
					pStatement.setString(ii++, primkey);
					pStatement.setString(ii++, sod_report_code);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(surfaceRD.getObservationTime().getTime()));
					pStatement.setString(ii++, surfaceRD.getFileRevisionSign());
					pStatement.setString(ii++, v_cccc);
					pStatement.setString(ii++, v_tt);
					pStatement.setString(ii++, surfaceRD.getStationNumberChina());
					pStatement.setInt(ii++, Integer.parseInt(StationCodeUtil.stringToAscii(stat)));
					pStatement.setBigDecimal(ii++, new BigDecimal(surfaceRD.getLatitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setBigDecimal(ii++, new BigDecimal(surfaceRD.getLongitude()).setScale(4, BigDecimal.ROUND_HALF_UP));
					pStatement.setInt(ii++, 2250); // V_NCODE
					pStatement.setInt(ii++, adminCode); // V_ACODE
					pStatement.setInt(ii++, surfaceRD.getObservationTime().getYear() + 1900);
					pStatement.setInt(ii++, surfaceRD.getObservationTime().getMonth() + 1);
					pStatement.setInt(ii++, surfaceRD.getObservationTime().getDate());
					pStatement.setInt(ii++, surfaceRD.getObservationTime().getHours());
					pStatement.setInt(ii++, surfaceRD.getObservationTime().getMinutes());
					pStatement.setInt(ii++, reportInfos.get(i).getReport().length());
					pStatement.setString(ii++, reportInfos.get(i).getReport());
					
					pStatement.execute();
					
//					connection.commit();
				} catch (Exception e) {
					infoLogger.error("sql error:" + e.getMessage()+"\n"+((LoggableStatement)pStatement).getQueryString());
					continue;
				}
			}// end for
			
		} catch (SQLException e) {
			infoLogger.error("Database connection error: " + e.getMessage());
		}finally {
			if(pStatement != null) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					infoLogger.error("\n Close Statement error: "+e.getMessage());
				}
			}
			try {
				connection.close();
				connection = null;
			} catch (SQLException e1) {
				infoLogger.error("\n Close connection error: "+e1.getMessage());
			}
		}
	}
	/**
	 * 
	 * @Title: findVBBSurfRD   
	 * @Description: TODO(查找地面交通基本要素数据的更正标识)   
	 * @param  surfaceRD 查找对象
	 * @param  connection 数据库连接      
	 * @return: String      
	 * @throws:
	 */
	@SuppressWarnings("deprecation")
	private static String findVBBSurfRDEle(SurfaceRD surfaceRD, java.sql.Connection connection){
		PreparedStatement Pstmt = null;
		ResultSet resultSet  = null;
		String rntString = null;
		String sql = "select V_BBB from "+StartConfig.keyTable()+" "
				+ "where V01301 = ? and D_datetime = ?";
		try{
			if(connection != null){			
				Pstmt = connection.prepareStatement(sql);
				Date date = new Date(surfaceRD.getObservationTime().getTime());
				int ii = 1;
				Pstmt.setString(ii++, surfaceRD.getStationNumberChina());
//				Pstmt.setInt(ii++, date.getYear() + 1900);
//				Pstmt.setInt(ii++, date.getMonth()  + 1);
//				Pstmt.setInt(ii++, date.getDate());
//				Pstmt.setInt(ii++, date.getHours());
//				Pstmt.setInt(ii++, date.getMinutes());
				Pstmt.setTimestamp(ii++, new Timestamp(date.getTime()));
				
				resultSet = Pstmt.executeQuery();
				if(resultSet.next()){
					rntString = resultSet.getString(1);
				}
			}
		}catch(SQLException e){
			infoLogger.error("\n Create Statement error*************: " + e.getMessage());
		}
		finally {
			if(Pstmt != null) {
				try {
					Pstmt.close();
					Pstmt = null;
					System.out.println("findVBBSurfRDEle Pstmt.close(); ");
				} catch (SQLException e) {
					infoLogger.error("\n Close Statement error: " + e.getMessage());
				}
			}
			if(resultSet != null){
				try{
					resultSet.close();
					resultSet = null;
				}catch(SQLException e){
					infoLogger.error("\n Close resultSet error: " + e.getMessage());
				}
			}
		}
		return rntString;
	}
	/**
	 * 
	 * @Title: findVBBSurfRDPre   
	 * @Description: TODO(查找地面交通分钟降水数据的更正标识)   
	 * @param  surfaceRD 查找对象
	 * @param connection  数据库连接
	 * @return: String      
	 * @throws:
	 */
	@SuppressWarnings("deprecation")
	private static String findVBBSurfRDPre(SurfaceRD surfaceRD, java.sql.Connection connection){
		PreparedStatement Pstmt = null;
		ResultSet resultSet  = null;
		String rntString = null;
		String sql = "select V_BBB from "+StartConfig.valueTable()+" "
				+ "where V01301 = ? and D_datetime = ?";
		try{
			
			if(connection != null){				
				Pstmt = connection.prepareStatement(sql);
				Date date = new Date(surfaceRD.getObservationTime().getTime());
				int ii = 1;
				Pstmt.setString(ii++, surfaceRD.getStationNumberChina());
//				Pstmt.setInt(ii++, date.getYear() + 1900);
//				Pstmt.setInt(ii++, date.getMonth()  + 1);
//				Pstmt.setInt(ii++, date.getDate());
//				Pstmt.setInt(ii++, date.getHours());
//				Pstmt.setInt(ii++, date.getMinutes());
				Pstmt.setTimestamp(ii++, new Timestamp(date.getTime()));
				
				resultSet = Pstmt.executeQuery();
				if(resultSet.next()){
					rntString = resultSet.getString(1);
				}
			}
		}catch(SQLException e){
			infoLogger.error("\n Create Statement error++++++++++++: " + e.getMessage());
		}
		finally {
			
			if(resultSet != null){
				try{
					resultSet.close();
					resultSet = null;
				}catch(SQLException e){
					infoLogger.error("\n Close resultSet error: " + e.getMessage());
				}
			}
			if(Pstmt != null) {
				try {
					Pstmt.close();
					Pstmt = null;
					System.out.println("findVBBSurfRDPre Pstmt.close(); ");
				} catch (SQLException e) {
					infoLogger.error("\n Close Statement error: " + e.getMessage());
				}
			}
			
		}
		return rntString;
	}
	/**
	 * 
	 * @Title: execute_sql   
	 * @Description: TODO(批量入库失败时，采用逐条提交)   
	 * @param sqls 待执行的查询语句
	 * @param  connection    数据库连接  
	 * @return: void      
	 * @throws:
	 */
	private static void execute_sql(List<String> sqls, java.sql.Connection connection, String fileN, int sign) {
		Statement pStatement = null;
		try {
			connection.setAutoCommit(true);
			pStatement = connection.createStatement();
			for (int i = 0; i < sqls.size(); i++) {
//				pStatement = new LoggableStatement(connection, sqls.get(i));
				
				try {
					pStatement.execute(sqls.get(i));
					infoLogger.info("\n single commit success: "+fileN+" "+sqls.get(i));
				} catch (Exception e) {
//					infoLogger.error("\n single commit failed: "+fileN+" "+sqls.get(i));
					if(i < listDi.size()){
						infoLogger.error("\n File name: "+fileN
								+"\n execute sql error: "+sqls.get(i)+"\n "+e.getMessage());
						if(sign == 1){// 降水
							listDi.get(listDi.size() - 1).setPROCESS_STATE("0");
						}
						else
							listDi.get(i).setPROCESS_STATE("0");
					}
					continue;
				}
			}
		} catch (SQLException e) {
			infoLogger.error("\n Create Statement error++++++++++==========: "+e.getMessage());
		}finally {
			if(pStatement != null) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					infoLogger.error("\n Close Statement error: "+e.getMessage());
				}
			}
		}		
		
	}
	
	public static void main(String[] args) {
		String filepath = "D:\\TEMP\\A.1.27.1\\Z_SURF_C_BENJ-REG_20190622030212_O_AWS-RD_FTM.txt";
		File file = new File(filepath);
		String fileN = file.getName();
		Date recv_time = new Date();
		DecodeRD decodeRD = new DecodeRD();
		ParseResult<SurfaceRD> parseResult = decodeRD.DecodeFile(file);	
		if(parseResult.isSuccess()){
			DataBaseAction action = DbService.processSuccessReport(parseResult, filepath, recv_time, fileN);
		}
	}
}
