package cma.cimiss2.dpc.indb.surf.dc_surf_kppr.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Pattern;

import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.ots.OTSBatchResult;
import org.cimiss2.dwp.tools.ots.OTSDbHelper;
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alicloud.openservices.tablestore.ClientException;

import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.surf.SurfaceObservationKppr;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	
	private static String sod_code = StartConfig.sodCode();
	private static String cts_code = StartConfig.ctsCode();
//	static Map<String, Object> proMap = StationInfo.getProMap();
	public static String V_TT = "朝鲜降水";    // 报文资料类别
	private static String sod_report_code = StartConfig.reportSodCode();
	private static String V_BBB = "000";
	private static String V_CCCC = "DKPY";  // 区域代码
	public static BlockingQueue<StatDi> diQueues;
	
	public static void setDiQueues(BlockingQueue<StatDi> diQueues){
		OTSService.diQueues = diQueues;
	}
	public static BlockingQueue<StatDi> getDiQueues(){
		return diQueues;
	}
	
	public static DataBaseAction insert_ots(List<SurfaceObservationKppr> list, String tablename, Date recv_time, StringBuffer loggerBuffer, String fileN, String filepath) {
		Map<String, Object> proMap = StationInfo.getProMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		if(list != null && list.size() > 0) {
			int	successRowCount = list.size();
	        for (int i = 0; i < list.size(); i++) {
	        	SurfaceObservationKppr kppr = list.get(i);
	            Map<String, Object> row = new HashMap<String, Object>();
	            String primkey = sdf.format(kppr.getObservationTime())+"_"+kppr.getStationNumberChina();
	            int stationNumberN = 999999;
				String stat = kppr.getStationNumberChina();
				if (Pattern.matches("\\d{5}", stat))
					stationNumberN = Integer.parseInt(stat);
				else
					stationNumberN = Integer.parseInt(StationCodeUtil.stringToAscii(stat));
			
				String info = (String) proMap.get(stat + "+01");
				int adminCode = 999999;
				double latitude = 999999.0;
				double longtitude = 999999.0;
				double statHeight = 999999.0;
				double airPressureSensorHeightOboveSeaLevel = 999999.0; 
				int stationLevel = 999999;
				int stationType = 999999;
				if(info == null) {
					loggerBuffer.append("\n In configuration file, this station does not exist!" + stat);
				}
				else{
					String[] infos = info.split(",");
					if(!infos[5].equals("null"))
						adminCode = Integer.parseInt(infos[5]);
					if(!infos[6].equals("null"))
						stationLevel = Integer.parseInt(infos[6]); 
					if(!infos[7].equals("null"))
						stationType = Integer.parseInt(infos[7]); 
					if(infos[1].equals("null")){
						loggerBuffer.append("\n In configuration file, longtitude is null!");
						 
					}
					else
						longtitude = Double.parseDouble(infos[1]);
					if(infos[2].equals("null")){
						loggerBuffer.append("\n In configuration file, latitude is null!");
						
					}
					else
						latitude = Double.parseDouble(infos[2]);
					if(!infos[3].equals("null"))
						statHeight = Double.parseDouble(infos[3]);
				}
	            row.put("D_RECORD_ID", primkey);
	            row.put("D_DATA_ID", sod_code);
	            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_DATETIME", TimeUtil.date2String(kppr.getObservationTime(),"yyyy-MM-dd HH:mm:ss"));
	            // "v_file_name_source,v01301,v01300,v05001,v06001,v07001,v07031,v02001,v02301,v_acode,"
	            row.put("V_FILE_NAME_SOURCE", fileN);
	            row.put("V01301", kppr.getStationNumberChina());
	            row.put("V01300", stationNumberN);
				row.put("V05001", latitude);
				row.put("V06001", longtitude);
				row.put("V07001", statHeight);
				row.put("V07031", airPressureSensorHeightOboveSeaLevel);
				row.put("V02001", stationType);
				row.put("V02301", stationLevel);
				row.put("V_ACODE", adminCode);
				// "v04001,v04002,v04003,v04004,v13019,v13023
				row.put("V04001", kppr.getObservationTime().getYear() + 1900);
				row.put("V04002", kppr.getObservationTime().getMonth() + 1);
				row.put("V04003", kppr.getObservationTime().getDate());
				row.put("V04004", kppr.getObservationTime().getHours());
				row.put("V13019", kppr.getFKPPR_V13019());
				row.put("V13023", kppr.getFKPPR_V13023());
				row.put("V13023", kppr.getFKPPR_V13023());
				row.put("V_BBB", "000");
				row.put("D_SOURCE_ID", cts_code);
				
				StatDi di = new StatDi();					
				di.setFILE_NAME_O(filepath);
				di.setDATA_TYPE(sod_code);
				di.setDATA_TYPE_1(cts_code);
				di.setTT(V_TT);			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(filepath);
				di.setBUSINESS_STATE("0"); //0成功，1失败
				di.setPROCESS_STATE("0");  //0成功，1失败	
				di.setIIiii(kppr.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(kppr.getObservationTime(), "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				di.setLATITUDE(String.valueOf(latitude));
				di.setLONGTITUDE(String.valueOf(longtitude));
			    try {
					OTSDbHelper.getInstance().insert(tablename, row);
					diQueues.offer(di);
				}  catch (Exception e) {
					successRowCount = successRowCount -1;
					di.setPROCESS_STATE("1");
					diQueues.offer(di);
					loggerBuffer.append(row+"\n");
					loggerBuffer.append(fileN + ": " + e.getMessage()+"\n");
					if(e.getClass() == ClientException.class) {
						return DataBaseAction.CONNECTION_ERROR;
					}
				}
	        }
	        loggerBuffer.append(" \nINSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
	        loggerBuffer.append(" INSERT SUCCESS COUNT : " + successRowCount + "\n");
	        loggerBuffer.append(" INSERT FAILED COUNT : " + (list.size() - successRowCount) + "\n");
		}
		return DataBaseAction.SUCCESS;
	}
	/**
	 * 
	 * @Title: reportInfoToDb   
	 * @Description: TODO(报文信息入库)   
	 * @param  reportInfos 报文列表集合
	 * @param  connection  数据库连接
	 * @param  recv_time   资料时间
	 * @return: void      
	 * @throws:
	 */
	@SuppressWarnings({ "rawtypes" })
	public static void reportInfoToDb(List<ReportInfo> reportInfos, String tablename, Date recv_time, StringBuffer loggerBuffer) {
		Map<String, Object> proMap = StationInfo.getProMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		if(reportInfos != null && reportInfos.size() > 0){
			loggerBuffer.append("start insert report ........");
			List<Map<String, Object>> batchs = new ArrayList<Map<String, Object>>();
			for(int i = 0; i < reportInfos.size(); i++){
				Map<String, Object> row = new HashMap<String, Object>();
				SurfaceObservationKppr kppr = (SurfaceObservationKppr) reportInfos.get(i).getT();
				String stat = kppr.getStationNumberChina();
				String info = (String) proMap.get(stat + "+01");
				int adminCode = 999999;
				double longtitude = 999999.0;
				double latitude = 999999.0;
				int countryCode = 999999;
				if(info == null) {
					loggerBuffer.append("\n In configuration file, this station does not exist!" + stat);
				}
				else{
					String[] infos = info.split(",");
					if(!infos[5].equals("null"))
						adminCode = Integer.parseInt(infos[5]);
					if(infos[1].equals("null")){
						loggerBuffer.append("\n In configuration file, longtitude is null!");
					}
					else
						longtitude = Double.parseDouble(infos[1]);
					if(infos[2].equals("null")){
						loggerBuffer.append("\n In configuration file, latitude is null!");
					}
					else
						latitude = Double.parseDouble(infos[2]);
					if(!infos[4].equals("null"))
						countryCode = Integer.parseInt(infos[4]);
				}
				String primkey = sdf.format(kppr.getObservationTime())+"_"+stat+"_"+V_TT;
				
				row.put("D_RECORD_ID", primkey);
	            row.put("D_DATA_ID", sod_report_code);
	            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_DATETIME", TimeUtil.date2String(kppr.getObservationTime(),"yyyy-MM-dd HH:mm:ss"));
				//"V_BBB,V_CCCC,V_TT,V01301,V01300,V05001,V06001,V_NCODE,V_ACODE,"
	            row.put("V_BBB", V_BBB);
	            row.put("V_CCCC", V_CCCC);
	            row.put("V_TT", V_TT);
	            row.put("V01301", kppr.getStationNumberChina());
	            row.put("V01300", Integer.parseInt(StationCodeUtil.stringToAscii(stat)));
	            row.put("V05001", latitude);
	            row.put("V06001", longtitude);
	            row.put("V_NCODE", countryCode);
	            row.put("V_ACODE", adminCode);
	            // "V04001,V04002,V04003,V04004,V04005,"
	            row.put("V04001", kppr.getObservationTime().getYear() + 1900);
	            row.put("V04002", kppr.getObservationTime().getMonth() + 1);
	            row.put("V04003", kppr.getObservationTime().getDate());
	            row.put("V04004", kppr.getObservationTime().getHours());
	            row.put("V04005", kppr.getObservationTime().getMinutes());
				// "V_LEN,V_REPORT)
	            row.put("V_LEN", reportInfos.get(i).getReport().length());
	            row.put("V_REPORT", reportInfos.get(i).getReport());
	            
	            batchs.add(row);
			}
			OTSBatchResult result = OTSDbHelper.getInstance().insert(tablename, batchs);
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
