package cma.cimiss2.dpc.indb.ocen.dc_ocean_tide.service;

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
import cma.cimiss2.dpc.decoder.bean.ocean.Tide;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	public static String sod_report = StartConfig.reportSodCode();
	
	public static String V_TT = "TIDE";
	public static String V_BBB = "000";
	public static String V_CCCC = "9999";
	public static double defaultF = 999999.0;
//	static Map<String, Object> proMap = StationInfo.getProMap();
 
	public static BlockingQueue<StatDi> diQueues;
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}

	@SuppressWarnings("deprecation")
	public static DataBaseAction insert_ots(ParseResult<Tide> parseResult, String tablename, Date recv_time,
			StringBuffer loggerBuffer, String fileN) {
		Map<String, Object> proMap = StationInfo.getProMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");	
		List<Tide> list = parseResult.getData();
		if(list != null && list.size() > 0) {
			int successRowCount = list.size();
			if(list != null && list.size() > 0) {
		        for (int i = 0; i < list.size(); i++) {
		        	Tide tide = list.get(i);
		            Map<String, Object> row = new HashMap<String, Object>();
		            Date date = tide.getObservationTime();
					String stat = tide.getStationNumberChina();
					double latitude = defaultF;
					double longtitude = defaultF;
					String info = (String) proMap.get(stat + "+20");
					if(info == null) 
						loggerBuffer.append("\n In the configuration file, the station number does not exist:" + stat);
					else{
						String[] infos = info.split(",");
						if(infos[1].equals("null")) 
							loggerBuffer.append("\n In the configuration file, the station longitude is empty");
						else longtitude = Double.parseDouble(infos[1]);
						if(infos[2].equals("null")) 
							loggerBuffer.append("\n In the configuration file, the latitude of the station is empty");
						else latitude = Double.parseDouble(infos[2]);
					}
					tide.setLatitude(latitude);
					tide.setLongtitude(longtitude);
					
					String lat = String.valueOf((int)(tide.getLatitude() * 1e6));
					String lon = String.valueOf((int)(tide.getLongtitude() * 1e6));
					lat = lat.replaceAll("-", "0");
					lon = lon.replaceAll("-", "0");
				
					row.put("D_RECORD_ID", sdf.format(date)+"_"+stat+"_"+lat+"_"+lon+"_"+tide.getSensorType());
					row.put("D_DATA_ID", sod_code);
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
			        row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
			        row.put("D_DATETIME", TimeUtil.date2String(tide.getObservationTime(),"yyyy-MM-dd HH:mm:ss"));
			        row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
					
					// "V01301,V05001,V06001,V02007,V04001,V04002,V04003,V04004,V04005,V04006,"
					row.put("V01301", stat);
					row.put("V05001", latitude);
					row.put("V06001", longtitude);
					row.put("V02007", tide.getSensorType());
					row.put("V04001", date.getYear() + 1900);
					row.put("V04002", date.getMonth() + 1);
					row.put("V04003", date.getDate());
					row.put("V04004", date.getHours());
					row.put("V04005", date.getMinutes());
					row.put("V04006", date.getSeconds());
					//	+ "V22042,V22120,V22121,V22038,V22039,V_BBB)"
					row.put("V22042", tide.getSeaTemperature());
					row.put("V22120", tide.getAutoWaterLevelDetection());
					row.put("V22121", tide.getManuallyWaterLevelDetection());
					row.put("V22038", tide.getTidalHeightAboveChartDatum());
					row.put("V22039", tide.getResidualTidalHeight());
					row.put("V_BBB", V_BBB);
		
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
					di.setLATITUDE(String.valueOf(tide.getLatitude()));
					di.setLONGTITUDE(String.valueOf(tide.getLongtitude()));
					di.setIIiii(tide.getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(tide.getObservationTime(), "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
						
			        try {
			            	
						OTSDbHelper.getInstance().insert(tablename, row);
						diQueues.offer(di);
					}  catch (Exception e) {
						successRowCount = successRowCount -1;
						di.setPROCESS_STATE("1");
						diQueues.offer(di);
						loggerBuffer.append(e.getMessage());
						loggerBuffer.append(row);
						
						if(e.getClass() == ClientException.class) {
							return DataBaseAction.CONNECTION_ERROR;
						}
					}
		        }
		        loggerBuffer.append(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
		        loggerBuffer.append(" INSERT SUCCESS COUNT : " + successRowCount + "\n");
		        loggerBuffer.append(" INSERT FAILED COUNT : " + (list.size() - successRowCount) + "\n");
		       
			}
		}
		
		return DataBaseAction.SUCCESS;
	}

	@SuppressWarnings("deprecation")
	public static void reportInfoToDb(ParseResult<Tide> parseResult, String reptablename, Date recv_time,
			StringBuffer loggerBuffer, String fileN) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		@SuppressWarnings("rawtypes")
		List<ReportInfo> reportInfos = parseResult.getReports();
		if(reportInfos != null && reportInfos.size() > 0) {
			List<Map<String, Object>> batchs = new ArrayList<Map<String, Object>>();
	        for (int i = 0; i < reportInfos.size(); i++) {
	        	Map<String, Object> row = new HashMap<String, Object>();
	        	Tide tide = (Tide) reportInfos.get(i).getT();
				String stat = tide.getStationNumberChina();
				Date date = tide.getObservationTime();
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
				row.put("V05001", tide.getLatitude());
				row.put("V06001", tide.getLongtitude());
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
