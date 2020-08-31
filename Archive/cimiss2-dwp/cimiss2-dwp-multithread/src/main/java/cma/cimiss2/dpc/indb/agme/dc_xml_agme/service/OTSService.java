package cma.cimiss2.dpc.indb.agme.dc_xml_agme.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Pattern;

import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.ots.OTSDbHelper;
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;

import com.alicloud.openservices.tablestore.ClientException;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCropAuto;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {
	public static String V_TT = "AGME";
	public static BlockingQueue<StatDi> diQueues;
	//public static String sod_code = "E.0015.0011.S001";
	public static String sod_code=StartConfig.sodCode_pre();
	public static int defaultInt = 999999; 
	//public static String cts_code = "E.0021.0001.R001";
	public static String cts_code = StartConfig.ctsCode();
	
	//public static String sod_device = "E.0015.0012.S001";
	public static String sod_device =StartConfig.sodCode_mul();
	
//	static Map<String, Object> proMap = StationInfo.getProMap();
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}

	public static DataBaseAction processSuccessReport(ParseResult<ZAgmeCropAuto> parseResult, Date recv_time,
			String fileN, StringBuffer loggerBuffer) {
		try {
			insertCropAuto(parseResult,  recv_time, loggerBuffer,fileN);
			
			insertCropAutoDevice(parseResult,recv_time, loggerBuffer,fileN);
			
		} catch (Exception e) {
			if(e.getClass() == ClientException.class) {
				return DataBaseAction.CONNECTION_ERROR;
			}
		}
		return DataBaseAction.SUCCESS;
		
	
		
	}

	@SuppressWarnings("deprecation")
	private static void insertCropAutoDevice(ParseResult<ZAgmeCropAuto> parseResult, Date recv_time,
			StringBuffer loggerBuffer, String fileN) {
		Map<String, Object> proMap = StationInfo.getProMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		List<ZAgmeCropAuto> list = parseResult.getData();
		
		if(list != null && list.size() > 0) {
			int successRowCount = list.size();
	        for (int i = 0; i < list.size(); i++) {
				ZAgmeCropAuto zAgmeCropAuto = list.get(i);
	            Map<String, Object> row = new HashMap<String, Object>();
	            if(zAgmeCropAuto.getCorrectMarker() == null || zAgmeCropAuto.getCorrectMarker().isEmpty())
					zAgmeCropAuto.setCorrectMarker("000");
					
				String stat = zAgmeCropAuto.getStationNumberChina();
				int adminCode = defaultInt;
				int stationNumberN = defaultInt;
				if (Pattern.matches("\\d{5}", stat))
					stationNumberN = Integer.parseInt(stat);
				else
					stationNumberN = Integer.parseInt(StationCodeUtil.stringToAscii(stat));
				String info = (String) proMap.get(stat + "+12");
				if(info != null) {
					String[] infos = info.split(",");
					if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
						adminCode = Integer.parseInt(infos[5]);
				}
				
				if(adminCode == 999999){
					info = (String) proMap.get(stat + "+01");
					if(info == null) {
						loggerBuffer.append("\n In the configuration file, the station number does not exist" + stat);
					}else {
						String[] infos = info.split(",");
						if(infos.length >= 6)
							adminCode = Integer.parseInt(infos[5]);
					}
				}
				row.put("D_RECORD_ID", sdf.format(zAgmeCropAuto.getObservationTime())+"_"+stat+"_"+zAgmeCropAuto.getCropName()+"_"+zAgmeCropAuto.getCropGrowthPeriod());
				row.put("D_DATA_ID", sod_code);
				row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
		        row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
		        row.put("D_DATETIME", TimeUtil.date2String(zAgmeCropAuto.getObservationTime(),"yyyy-MM-dd HH:mm:ss"));  
		        row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
				//"V01301,V01300,V05001,V06001,V07001,V07031,V_ACODE,V04001,V04002,V04003,"
				row.put("V01301", stat);
				row.put("V01300", stationNumberN);
				row.put("V05001", zAgmeCropAuto.getLatitude());
				row.put("V06001", zAgmeCropAuto.getLongitude());
				row.put("V07030", zAgmeCropAuto.getHeightOfStaionAboveSeaLevel());
				row.put("V07031", zAgmeCropAuto.getHeightOfPressureSensor());
				row.put("V_ACODE", adminCode);
				row.put("V04001", zAgmeCropAuto.getObservationTime().getYear() + 1900);
				row.put("V04002", zAgmeCropAuto.getObservationTime().getMonth() + 1);
				row.put("V04003", zAgmeCropAuto.getObservationTime().getDate() );
				// "V04004,V07256,V02271,V48002,V02256,V02259,V02270,V02257,V02258,V02260,"
				row.put("V04004", zAgmeCropAuto.getObservationTime().getHours());
				row.put("V07256", zAgmeCropAuto.getImageSensorHeightAboveGround());
				row.put("V02271", zAgmeCropAuto.getImageSensorFocus());
				row.put("V48002", zAgmeCropAuto.getObvArea());
				row.put("V02256", zAgmeCropAuto.getObvMethod());
				row.put("V02259", zAgmeCropAuto.getImageSensorID());
				row.put("V02270", zAgmeCropAuto.getImageSensorConnectionStatus());
				row.put("V02257", zAgmeCropAuto.getGPSTimingMarker());
				row.put("V02258", zAgmeCropAuto.getCFPeripheralStorageMarker());
				row.put("V02260", zAgmeCropAuto.getAgmeReportSoftwareVersion());
				// "V48000,V48001,Q48001,V26256,Q26256,V26257,V26257,V48009,Q48009,V48008,"
				row.put("V48000", zAgmeCropAuto.getCropName());
				row.put("V48001", zAgmeCropAuto.getCropGrowthPeriod());
				row.put("Q48001", zAgmeCropAuto.getCropGrowthPeriodQC());
				row.put("V26256", zAgmeCropAuto.getGrowthStarttime());
				row.put("Q26256", zAgmeCropAuto.getGrowthStarttimeQC());
				row.put("V26257", zAgmeCropAuto.getGrowthDuration());
				row.put("Q26257", zAgmeCropAuto.getGrowthDurationQC());
				row.put("V48009", zAgmeCropAuto.getGrowthPeriodPercent());
				row.put("Q48009", zAgmeCropAuto.getGrowthPeriodPercentQC());
				row.put("V48008", zAgmeCropAuto.getPlantCoverage());
				
				// "Q48008,V48010,Q48010,V48034,Q48034,V48007,Q48007,V48301,Q48301,V48006,"
				row.put("Q48008", zAgmeCropAuto.getPlantCoverageQC());
				row.put("V48010", zAgmeCropAuto.getLeafAreaIndex());
				row.put("Q48010", zAgmeCropAuto.getLeafAreaIndexQC());
				row.put("V48034", zAgmeCropAuto.getCanopyHeight());
				row.put("Q48034", zAgmeCropAuto.getCanopyHeightQC());
				row.put("V48007", zAgmeCropAuto.getPlantDensity());
				row.put("Q48007", zAgmeCropAuto.getPlantDensityQC());
				row.put("V48301", zAgmeCropAuto.getPlantDensity());
				row.put("Q48301", zAgmeCropAuto.getPlantDensityQC());
				row.put("V48006", zAgmeCropAuto.getGrowthState());
				
				// "Q48006,V51000,V51009,Q51002,V26269,Q26269,V26270,Q26270,V30256,V48033,"
				row.put("Q48006", zAgmeCropAuto.getGrowthStateQC());
				row.put("V51000", zAgmeCropAuto.getDisaName());
				row.put("V51009", zAgmeCropAuto.getDisaLevel());
				row.put("Q51002", zAgmeCropAuto.getDisaLevelQC());
				row.put("V26269", zAgmeCropAuto.getDisaStarttime());
				row.put("Q26269", zAgmeCropAuto.getDisaStarttimeQC());
				row.put("V26270", zAgmeCropAuto.getDisaDuration());
				row.put("Q26270", zAgmeCropAuto.getDisaDurationQC());
				row.put("V30256", zAgmeCropAuto.getCropImageFormat());
				
				if (zAgmeCropAuto.getCropImage() != null) {
					row.put("V48033",zAgmeCropAuto.getCropImage());
				}
				row.put("V35024", zAgmeCropAuto.getCorrectMarker());
				row.put("V33256", zAgmeCropAuto.getQualityControl());
				row.put("V_BBB", zAgmeCropAuto.getCorrectMarker());
				row.put("D_SOURCE_ID", cts_code);	
				
				StatDi di = new StatDi();					
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(sod_code);
				di.setDATA_TYPE_1(cts_code);
				di.setTT(V_TT);			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("0"); //0成功，1失败
				di.setPROCESS_STATE("0");  //0成功，1失败	
				di.setLATITUDE(String.valueOf(zAgmeCropAuto.getLatitude()));
				di.setLONGTITUDE(String.valueOf(zAgmeCropAuto.getLongitude()));
				di.setIIiii(zAgmeCropAuto.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(zAgmeCropAuto.getObservationTime(), "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				try {	            	
		        	if(zAgmeCropAuto.getCorrectMarker().equals("000")) {
		        		OTSDbHelper.getInstance().insert(StartConfig.keyTable(), row);
						
					}else {
						OTSDbHelper.getInstance().update(StartConfig.keyTable(),row);
					}
					diQueues.offer(di);
				} catch (Exception e) {
					successRowCount = successRowCount -1;
					di.setPROCESS_STATE("1");
					diQueues.offer(di);
					loggerBuffer.append(row);
					
					loggerBuffer.append(e.getMessage());
					
					
				}

	        }
	        loggerBuffer.append(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
	        loggerBuffer.append(" INSERT SUCCESS COUNT : " + successRowCount + "\n");
	        loggerBuffer.append(" INSERT FAILED COUNT : " + (list.size() - successRowCount) + "\n");
	        
	       }
	}

	@SuppressWarnings("deprecation")
	private static void insertCropAuto(ParseResult<ZAgmeCropAuto> parseResult, Date recv_time,
			StringBuffer loggerBuffer, String fileN) {
		Map<String, Object> proMap = StationInfo.getProMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		List<ZAgmeCropAuto> list = parseResult.getData();
		
		if(list != null && list.size() > 0) {
			int successRowCount = list.size();
	        for (int i = 0; i < list.size(); i++) {
				ZAgmeCropAuto zAgmeCropAuto = list.get(i);
	            Map<String, Object> row = new HashMap<String, Object>();
	            if(zAgmeCropAuto.getCorrectMarker() == null || zAgmeCropAuto.getCorrectMarker().isEmpty())
					zAgmeCropAuto.setCorrectMarker("000");
					
	            String stat = zAgmeCropAuto.getStationNumberChina();
				int adminCode = defaultInt;
				
				String info = (String) proMap.get(stat + "+12");
				if(info != null) {
					String[] infos = info.split(",");
					if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
						adminCode = Integer.parseInt(infos[5]);
				}
				
				if(adminCode == 999999){
					info = (String) proMap.get(stat + "+01");
					if(info == null) {
						loggerBuffer.append("\n In the configuration file, the station number does not exist" + stat);
					}else {
						String[] infos = info.split(",");
						if(infos.length >= 6)
							adminCode = Integer.parseInt(infos[5]);
					}
				}
				
				row.put("D_RECORD_ID", sdf.format(zAgmeCropAuto.getObservationTime())+"_"+stat);
				row.put("D_DATA_ID", sod_device);
				row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
		        row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
		        row.put("D_DATETIME", TimeUtil.date2String(zAgmeCropAuto.getObservationTime(),"yyyy-MM-dd HH:mm:ss"));  
		        row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
				//"V01301,V05001,V06001,V07001,V07031,V_ACODE,V04001,V04002,V04003,V04004,"
				row.put("V01301", stat);
				row.put("V05001", zAgmeCropAuto.getLatitude());
				row.put("V06001", zAgmeCropAuto.getLongitude());
				row.put("V07030", zAgmeCropAuto.getHeightOfStaionAboveSeaLevel());
				row.put("V07031", zAgmeCropAuto.getHeightOfPressureSensor());
				row.put("V_ACODE", adminCode);
				row.put("V04001", zAgmeCropAuto.getObservationTime().getYear() + 1900);
				row.put("V04002", zAgmeCropAuto.getObservationTime().getMonth() + 1);
				row.put("V04003", zAgmeCropAuto.getObservationTime().getDate() );
				row.put("V04004", zAgmeCropAuto.getObservationTime().getHours());
				
				// "V02261,V02262,V02263,V02264,V02265,V02266,V02267,V02268,V02269) 
				row.put("V02261", zAgmeCropAuto.getColllectorRunningState());
				row.put("V02262", zAgmeCropAuto.getCollectorVoltage());
				row.put("V02263", zAgmeCropAuto.getCollectorPowerSupplyType());
				row.put("V02264", zAgmeCropAuto.getCollectorMainboardTemperature());
				row.put("V02265", zAgmeCropAuto.getCollectorCFstate());
				row.put("V02266", zAgmeCropAuto.getCollectorCFRemainSpace());
				row.put("V02267", zAgmeCropAuto.getCollectorGPSWorkingstate());
				row.put("V02268", zAgmeCropAuto.getCollectorGateswitchState());
				row.put("V02269", zAgmeCropAuto.getCollectorLANterminalCommunicationState());
				row.put("V35024", zAgmeCropAuto.getCorrectMarker());
				row.put("V_BBB", zAgmeCropAuto.getCorrectMarker());
				row.put("D_SOURCE_ID", cts_code);
				
				StatDi di = new StatDi();					
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(sod_code);
				di.setDATA_TYPE_1(cts_code);
			
				di.setTT(V_TT);			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("0"); //0成功，1失败
				di.setPROCESS_STATE("0");  //0成功，1失败	
				di.setLATITUDE(String.valueOf(zAgmeCropAuto.getLatitude()));
				di.setLONGTITUDE(String.valueOf(zAgmeCropAuto.getLongitude()));
				di.setIIiii(zAgmeCropAuto.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(zAgmeCropAuto.getObservationTime(), "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());		
				try {	            	
		        	if(zAgmeCropAuto.getCorrectMarker().equals("000")) {
		        		OTSDbHelper.getInstance().insert(StartConfig.valueTable(),row);
						
					}else {
						OTSDbHelper.getInstance().update(StartConfig.valueTable(),row);
					}
					diQueues.offer(di);
				} catch (Exception e ){
					successRowCount = successRowCount -1;
					di.setPROCESS_STATE("1");
					diQueues.offer(di);
					loggerBuffer.append(row);
					loggerBuffer.append(e.getMessage());
					}

	        }
	        loggerBuffer.append(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
	        loggerBuffer.append(" INSERT SUCCESS COUNT : " + successRowCount + "\n");
	        loggerBuffer.append(" INSERT FAILED COUNT : " + (list.size() - successRowCount) + "\n");
	        
	       }
		
	}
	

}
