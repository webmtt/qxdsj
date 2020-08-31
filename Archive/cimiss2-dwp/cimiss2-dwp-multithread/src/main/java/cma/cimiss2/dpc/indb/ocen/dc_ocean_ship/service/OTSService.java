package cma.cimiss2.dpc.indb.ocen.dc_ocean_ship.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.ots.OTSBatchResult;
import org.cimiss2.dwp.tools.ots.OTSDbHelper;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import com.alicloud.openservices.tablestore.ClientException;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ocean.Ship;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

// TODO: Auto-generated Javadoc
/**
 * The Class OTSService.
 */
public class OTSService {
	
	/** The sod report. */
	public static String sod_report = StartConfig.reportSodCode();
	
	/** The v cccc. */
	public static String V_CCCC = "9999";
	
	/** The sod code. */
	public static String sod_code = StartConfig.sodCode();
	
	/** The cts code. */
	public static String cts_code = StartConfig.ctsCode();
	
	/** The di queues. */
	public static BlockingQueue<StatDi> diQueues;
	
	
	/**
	 * Gets the di queues.
	 *
	 * @return the di queues
	 */
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	/**
	 * Sets the di queues.
	 *
	 * @param diQueues the new di queues
	 */
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}

	/**
	 * Insert ots.
	 *
	 * @param parseResult the parse result
	 * @param tablename the tablename
	 * @param recv_time the recv time
	 * @param loggerBuffer the logger buffer
	 * @param fileN the file N
	 * @param V_BBB the v bbb
	 * @return the data base action
	 */
	@SuppressWarnings("deprecation")
	public static DataBaseAction insert_ots(ParseResult<Ship> parseResult, String tablename, Date recv_time,
			StringBuffer loggerBuffer, String fileN, String V_BBB) {
		Map<String, Object> proMap = StationInfo.getProMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		List<Ship> list = parseResult.getData();
		
		if(list != null && list.size() > 0) {
			int successRowCount = list.size();
	        for (int i = 0; i < list.size(); i++) {
	        	Ship ship = list.get(i);
	            Map<String, Object> row = new HashMap<String, Object>();
	            String stat = ship.getShipID();
	           
				//2019-1-29 新增
				String info = (String) proMap.get(stat + "+09");
				if(info == null) {
//					System.out.println(stat);
					loggerBuffer.append("\n In the configuration file, the station number does not exist:" + stat);
					continue;
				}
				Date date = ship.getObservationTime();
				String lat = String.valueOf((int)(ship.getLatitude() * 1e6));
				String lon = String.valueOf((int)(ship.getLongtitude() * 1e6));
				lat = lat.replaceAll("-", "0");
				lon = lon.replaceAll("-", "0");
				row.put("D_RECORD_ID", sdf.format(date) + "_" + stat + "_" + lat + "_" + lon);
				row.put("D_DATA_ID", sod_code);
				row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
		        row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
		        Date newDate = new Date(date.getTime());
		        newDate.setMinutes(0);
		        newDate.setSeconds(0);
		        row.put("D_DATETIME", TimeUtil.date2String(newDate,"yyyy-MM-dd HH:mm:ss"));
		        row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
				// "V01011,V05001,V06001,V07001,V07304,V07301,V07305,V02001,V02141,V04001,"
				row.put("V01011", stat);
				row.put("V05001", ship.getLatitude());
				row.put("V06001", ship.getLongtitude());
				row.put("V07001", ship.getStationHeightAboveSea());
				row.put("V07304", ship.getHeightOfAirpressureSensor());
				row.put("V07301", ship.getHeightOfWindSpeedSensor());
				row.put("V07305", ship.getDistanceBetweenDeckAndSea());
				row.put("V02001", ship.getStationTypeInt());
				row.put("V02141", ship.getStationTypeStr());
				row.put("V04001", date.getYear() + 1900);
				// "V04002,V04003,V04004,V04005,V04006,V01012,V01013,V05021,V20003,V10051,"
				row.put("V04002", date.getMonth() + 1);
				row.put("V04003", date.getDate());
				row.put("V04004", date.getHours());
				row.put("V04005", date.getMinutes());
				row.put("V04006", date.getSeconds());
				row.put("V01012", ship.getShipMovingDir());
				row.put("V01013", ship.getShipMovingSpeed());
				row.put("V05021", ship.getBowAzimuth());
				row.put("V20003", ship.getWeatherCondition());
				row.put("V10051", ship.getSeaLevelPressure());
				// "V12001,V12002,V11001,V11002,V11301,V22042,V20001,V20012,V20011,V22022_1,"
				row.put("V12001", ship.getDryballTemperature());
				row.put("V12002", ship.getWetballTemperature());
				row.put("V11001", ship.getWindDir());
				row.put("V11002", ship.getWindSpeed());
				row.put("V11301", ship.getWindLevel());
				row.put("V22042", ship.getSeaTemperature());
				row.put("V20001", ship.getVisibility());
				row.put("V20012", ship.getCloudShape());
				row.put("V20011", ship.getCloudAmount());
				row.put("V22022_1", ship.getWaveLevel());  // V22022_1  与 V22303字段值互换
				// "V22022_2,V22303, V_BBB"
				row.put("V22022_2", ship.getWaveHeightByInstrument());
				row.put("V22303", ship.getWaveHeightManually());
				row.put("V_BBB", V_BBB);
				
				String  visibility=String.valueOf(ship.getVisibility());//获取能见度
				visibility=visibility.startsWith("99999")?visibility:String.valueOf(ship.getVisibility()/1852);
				row.put("", visibility);//V20256 能见度级别
	
				StatDi di = new StatDi();					
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(sod_code);
				di.setDATA_TYPE_1(cts_code);
		
				di.setTT("船舶观测资料");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("0"); //0成功，1失败
				di.setPROCESS_STATE("0");  //0成功，1失败	
				di.setLATITUDE(String.valueOf(ship.getLatitude()));
				di.setLONGTITUDE(String.valueOf(ship.getLongtitude()));
				di.setIIiii(stat);
				di.setDATA_TIME(TimeUtil.date2String(ship.getObservationTime(), "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				
	            try {        	
					OTSDbHelper.getInstance().insert(tablename, row);
					diQueues.offer(di);
				}  catch (Exception e) {
					successRowCount = successRowCount -1;
					di.setPROCESS_STATE("1");
					diQueues.offer(di);
					loggerBuffer.append(row);
					loggerBuffer.append(e.getMessage());
					if(e.getClass() == ClientException.class) {
						return DataBaseAction.CONNECTION_ERROR;
					}
				}
	        }
            loggerBuffer.append(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
	        loggerBuffer.append(" INSERT SUCCESS COUNT : " + successRowCount + "\n");
	        loggerBuffer.append(" INSERT FAILED COUNT : " + (list.size() - successRowCount) + "\n");
		}
		return DataBaseAction.SUCCESS;
	}

	/**
	 * Update ots.
	 *
	 * @param parseResult the parse result
	 * @param tablename the tablename
	 * @param recv_time the recv time
	 * @param loggerBuffer the logger buffer
	 * @param fileN the file N
	 * @param V_BBB the v bbb
	 * @return the data base action
	 */
	@SuppressWarnings("deprecation")
	public static DataBaseAction update_ots(ParseResult<Ship> parseResult, String tablename, Date recv_time,
			StringBuffer loggerBuffer, String fileN, String V_BBB) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		List<Ship> list = parseResult.getData();
		
		if(list != null && list.size() > 0) {
			int successRowCount = list.size();
	        for (int i = 0; i < list.size(); i++) {
	        	Ship ship = list.get(i);
	            Map<String, Object> row = new HashMap<String, Object>();
	            String stat = ship.getShipID();
				Date date = ship.getObservationTime();
				row.put("D_RECORD_ID", sdf.format(date)+"_"+stat);
				row.put("D_DATA_ID", sod_code);
				row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
		        row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
		        row.put("D_DATETIME", TimeUtil.date2String(ship.getObservationTime(),"yyyy-MM-dd HH:mm:ss"));
		        row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
				// "V01011,V05001,V06001,V07001,V07304,V07301,V07305,V02001,V02141,V04001,"
				row.put("V01011", stat);
				row.put("V05001", ship.getLatitude());
				row.put("V06001", ship.getLongtitude());
				row.put("V07001", ship.getStationHeightAboveSea());
				row.put("V07304", ship.getHeightOfAirpressureSensor());
				row.put("V07301", ship.getHeightOfWindSpeedSensor());
				row.put("V07305", ship.getDistanceBetweenDeckAndSea());
				row.put("V02001", ship.getStationTypeInt());
				row.put("V02141", ship.getStationTypeStr());
				row.put("V04001", date.getYear() + 1900);
				// "V04002,V04003,V04004,V04005,V04006,V01012,V01013,V05021,V20003,V10051,"
				row.put("V04002", date.getMonth() + 1);
				row.put("V04003", date.getDate());
				row.put("V04004", date.getHours());
				row.put("V04005", date.getMinutes());
				row.put("V04006", date.getSeconds());
				row.put("V01012", ship.getShipMovingDir());
				row.put("V01013", ship.getShipMovingSpeed());
				row.put("V05021", ship.getBowAzimuth());
				row.put("V20003", ship.getWeatherCondition());
				row.put("V10051", ship.getSeaLevelPressure());
				// "V12001,V12002,V11001,V11002,V11301,V22042,V20001,V20012,V20011,V22022_1,"
				row.put("V12001", ship.getDryballTemperature());
				row.put("V12002", ship.getWetballTemperature());
				row.put("V11001", ship.getWindDir());
				row.put("V11002", ship.getWindSpeed());
				row.put("V11301", ship.getWindLevel());
				row.put("V22042", ship.getSeaTemperature());
				row.put("V20001", ship.getVisibility());
				row.put("V20012", ship.getCloudShape());
				row.put("V20011", ship.getCloudAmount());
				row.put("V22022_1", ship.getWaveHeightManually());
				// "V22022_2,V22303, V_BBB"
				row.put("V22022_2", ship.getWaveHeightByInstrument());
				row.put("V22303", ship.getWaveLevel());
				row.put("V_BBB", V_BBB);
				
				String  visibility=String.valueOf(ship.getVisibility());//获取能见度
				visibility=visibility.startsWith("99999")?visibility:String.valueOf(ship.getVisibility()/1852);
				row.put("", visibility);//V20256 能见度级别
				
				StatDi di = new StatDi();					
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(sod_code);
				di.setDATA_TYPE_1(cts_code);
		
				di.setTT("船舶观测资料");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("0"); //0成功，1失败
				di.setPROCESS_STATE("0");  //0成功，1失败	
				di.setLATITUDE(String.valueOf(ship.getLatitude()));
				di.setLONGTITUDE(String.valueOf(ship.getLongtitude()));
				di.setIIiii(stat);
				di.setDATA_TIME(TimeUtil.date2String(ship.getObservationTime(), "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				
				try {
					OTSDbHelper.getInstance().update(tablename,row);
					diQueues.offer(di);
				}  catch (Exception e) {
					successRowCount = successRowCount -1;
					di.setPROCESS_STATE("1");
					diQueues.offer(di);
					loggerBuffer.append(row);
					loggerBuffer.append(e.getMessage());
					if(e.getClass() == ClientException.class) {
						return DataBaseAction.CONNECTION_ERROR;
					}
				}
	            
	        } 
	        loggerBuffer.append(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
	        loggerBuffer.append(" INSERT SUCCESS COUNT : " + successRowCount + "\n");
	        loggerBuffer.append(" INSERT FAILED COUNT : " + (list.size() - successRowCount) + "\n");
	        
		}
		return DataBaseAction.SUCCESS;
	}
	
	/**
	 * Report info to db.
	 *
	 * @param parseResult the parse result
	 * @param reptablename the reptablename
	 * @param recv_time the recv time
	 * @param loggerBuffer the logger buffer
	 * @param fileN the file N
	 * @param V_TT the v tt
	 * @param V_BBB the v bbb
	 */
	@SuppressWarnings("deprecation")
	public static void reportInfoToDb(ParseResult<Ship> parseResult, String reptablename, Date recv_time,
			StringBuffer loggerBuffer, String fileN, String V_TT, String V_BBB) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		@SuppressWarnings("rawtypes")
		List<ReportInfo> reportInfos = parseResult.getReports();
		if(reportInfos != null && reportInfos.size() > 0) {
			List<Map<String, Object>> batchs = new ArrayList<Map<String, Object>>();
	        for (int i = 0; i < reportInfos.size(); i++) {
	        	Map<String, Object> row = new HashMap<String, Object>();
	        	Ship ship = (Ship) reportInfos.get(i).getT();
				String stat = ship.getShipID();
				Date date = ship.getObservationTime();
				String primkey = sdf.format(date)+"_"+stat+"_"+V_TT+"_"+ V_BBB;
				row.put("D_RECORD_ID", primkey);
				row.put("D_DATA_ID", sod_report);
				row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
		        row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
		        row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
		        row.put("D_DATETIME", TimeUtil.date2String(date,"yyyy-MM-dd HH:mm:ss"));
				row.put("V_BBB", V_BBB);
				row.put("V_CCCC", V_CCCC);
				row.put("V_TT", V_TT);
				row.put("V01301", stat);
				row.put("V05001", ship.getLatitude());
				row.put("V06001", ship.getLongtitude());
				row.put("V04001", date.getYear() + 1900);
				row.put("V04002", date.getMonth() + 1);
				row.put("V04003", date.getDate());
				row.put("V04004", date.getHours());
				row.put("V04005", date.getMinutes());
				row.put("V_LEN", reportInfos.get(i).getReport().length());
				row.put("V_REPORT", reportInfos.get(i).getReport());

	            batchs.add(row);
	        }
	        OTSBatchResult result = OTSDbHelper.getInstance().insert(reptablename, batchs);
	        System.out.println(result.getSuccessRowCount());
	        System.out.println(result.getFailedRowCount());
	        System.out.println(result.getFailedRows());
	        loggerBuffer.append(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
	        loggerBuffer.append(" INSERT SUCCESS COUNT : " + result.getSuccessRowCount() + "\n");
	        loggerBuffer.append(" INSERT FAILED COUNT : " + result.getFailedRowCount() + "\n");
	        loggerBuffer.append(" INSERT FAILED CONTENT : " + result.getFailedRows() + "\n");
		}
		
	}
}
