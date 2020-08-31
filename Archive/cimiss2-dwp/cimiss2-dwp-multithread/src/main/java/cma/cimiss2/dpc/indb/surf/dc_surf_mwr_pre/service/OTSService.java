package cma.cimiss2.dpc.indb.surf.dc_surf_mwr_pre.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.ots.OTSDbHelper;
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alicloud.openservices.tablestore.ClientException;

import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.surf.SurfaceObservationMwrpre;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	public static String sod_report = StartConfig.reportSodCode();
	public static String V_TT = "MWRPre"; 
	public static String V_CCCC = "9999";  //资料无本字段
	public static int defaultInt = 999999;
	public static String V_BBB = "000";
//	static Map<String, Object> proMap = StationInfo.getProMap();
	public static BlockingQueue<StatDi> diQueues;
	
	public static void setDiQueues(BlockingQueue<StatDi> diQueue){
		OTSService.diQueues = diQueue;
	}
	
	public static DataBaseAction insert_ots(List<SurfaceObservationMwrpre> list, String tablename, Date recv_time, StringBuffer loggerBuffer, String fileN, String filepath) {
		Map<String, Object> proMap = StationInfo.getProMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		if(list != null && list.size() > 0) {
			int successRowCount = list.size();
	        for (int i = 0; i < list.size(); i++) {
	        	SurfaceObservationMwrpre mwrPre = list.get(i);
	            Map<String, Object> row = new HashMap<String, Object>();
	           
	            String stat = mwrPre.getStationNumberChina();
				String info = (String) proMap.get(stat + "+01");
				int adminCode = 999999;
				double latitude = 999999.0;
				double longtitude = 999999.0;
				double statHeight = 999999.0;
				int stationLevel = 999999;
				int stationType = 999999;
				double airPressureSensorHeightOboveSeaLevel = 999999.0;
				if(stat.equals("999998")||fileN.contains("DAY") || fileN.contains("HOUR")){
					latitude = mwrPre.getLatitude();
					longtitude = mwrPre.getLongtitude();
				}
				else if(info == null) {
					loggerBuffer.append("\n In configuration file, this station does not exist!" + stat);
					continue ;
				}
				else{
					String[] infos = info.split(",");
					if(!infos[5].equals("null"))  //中国行政区域代码
						adminCode = Integer.parseInt(infos[5]);
					if(!infos[6].equals("null"))
						stationLevel = Integer.parseInt(infos[6]); // 台站级别
					if(infos[1].equals("null")){  // 经度
						loggerBuffer.append("\n In configuration file, longtitude is null!");
						continue ;
					}
					else
						longtitude = Double.parseDouble(infos[1]);
					if(infos[2].equals("null")){  //纬度
						loggerBuffer.append("\n In configuration file, latitude is null!");
						continue;
					}
					else
						latitude = Double.parseDouble(infos[2]);
					if(!infos[3].equals("null"))
						statHeight = Double.parseDouble(infos[3]);
				}
				
				String primkey = sdf.format(mwrPre.getObservationTime())+"_"+stat +"_"+String.valueOf((int)(latitude*1e6))+"_"+String.valueOf((int)(longtitude*1e6))+"_"+mwrPre.getTimeScale();
	            row.put("D_RECORD_ID", primkey);
	            row.put("D_DATA_ID", sod_code);
	            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_DATETIME", TimeUtil.date2String(mwrPre.getObservationTime(),"yyyy-MM-dd HH:mm:ss"));
	           //"V_PADFN,V_PPTFN,v01301,v01300,v05001,v06001,v07001,v07031,v02001,v02301,v_acode,"
	            if(fileN.contains("D") || fileN.contains("d")){ // 24降雨文件
					row.put("V_PADFN", fileN);
					row.put("V_PPTFN", "999999");
				}
				else{  //6小时降雨文件
					row.put("V_PADFN", "999999");
					row.put("V_PPTFN", fileN);
				}
	            row.put("V01301", stat);
				row.put("V01300", Integer.parseInt(StationCodeUtil.stringToAscii(stat)));
				row.put("V05001", latitude); // 纬度
				row.put("V06001", longtitude); // 经度
				row.put("V07001", statHeight); // 测站高度
				row.put("V07031", airPressureSensorHeightOboveSeaLevel); //气压传感器海拔高度
				row.put("V02001", stationType); // 测站类型
				row.put("V02301", stationLevel); // 台站级别
				row.put("V_ACODE", adminCode);
				// "v04001,v04002,v04003,v04004,V13021,V13023) "
				row.put("V04001", mwrPre.getObservationTime().getYear() + 1900);
				row.put("V04002", mwrPre.getObservationTime().getMonth() + 1);
				row.put("V04003", mwrPre.getObservationTime().getDate());
				row.put("V04004", mwrPre.getObservationTime().getHours());
				row.put("V13021", mwrPre.getHourlyRainfall());
				row.put("V13023", mwrPre.getDailyRainfall());
				row.put("V13019", mwrPre.getOneHourRainfall());
				
				row.put("V_BBB", V_BBB);
				row.put("V_TIMESCALE", mwrPre.getTimeScale());
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
				di.setIIiii(stat);
				di.setDATA_TIME(TimeUtil.date2String(mwrPre.getObservationTime(), "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				di.setLATITUDE(String.valueOf(latitude));
				di.setLONGTITUDE(String.valueOf(longtitude));
				try {
					OTSDbHelper.getInstance().insert(tablename, row);
					diQueues.offer(di);
//					System.out.println(row);
				} catch (Exception e) {
					successRowCount = successRowCount -1;
					di.setPROCESS_STATE("1");
					diQueues.offer(di);
					loggerBuffer.append(row);
					loggerBuffer.append(fileN + ": " + e.getMessage());
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
	public static void reportInfoToDb(List<ReportInfo> reportInfos, String tablename, Date recv_time, StringBuffer loggerBuffer, String fileN) {
		Map<String, Object> proMap = StationInfo.getProMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		loggerBuffer.append("start insert report " + tablename + "\n");
		int successRowCount = 0;
		if(reportInfos != null)
			successRowCount = reportInfos.size();
		if(reportInfos != null && reportInfos.size() > 0){
			for(int i = 0; i < reportInfos.size(); i++){
				Map<String, Object> row = new HashMap<String, Object>();
				SurfaceObservationMwrpre mwrPre= (SurfaceObservationMwrpre) reportInfos.get(i).getT();
				String stat = mwrPre.getStationNumberChina();
				String info = (String) proMap.get(stat + "+01");
				int CountryCode = 9999;
				double longtitude = 999999.0;
				double latitude = 999999.0;
				if(stat.equals("999998")){
					latitude = mwrPre.getLatitude();
					longtitude = mwrPre.getLongtitude();
				}
				else if(info == null) {
//					infoLogger.error("\n In configuration file, this station does not exist!" + stat);
					continue ;
				}
				else{
					String[] infos = info.split(",");
					if(!infos[4].equals("null"))
						CountryCode = Integer.parseInt(infos[4]);
					if(infos[1].equals("null")){
//						infoLogger.error("\n In configuration file, longtitude is null!");
						continue;
					}
					else
						longtitude = Double.parseDouble(infos[1]);
					if(infos[2].equals("null")){
//						infoLogger.error("\n  In configuration file, latitude is null!");
						continue;
					}
					else
						latitude = Double.parseDouble(infos[2]);
				}
				String primkey = sdf.format(mwrPre.getObservationTime())+"_"+stat+"_"+String.valueOf((int)(latitude*1e6))+"_"+String.valueOf((int)(longtitude*1e6))+"_"+mwrPre.getTimeScale();
				row.put("D_RECORD_ID", primkey);
	            row.put("D_DATA_ID", sod_report);
	            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_DATETIME", TimeUtil.date2String(mwrPre.getObservationTime(),"yyyy-MM-dd HH:mm:ss"));
	            //"V_BBB,V_CCCC,V_TT,V01301,V01300,V05001,V06001,V_NCODE,V_ACODE,"
	            row.put("V_BBB", V_BBB);
	            row.put("V_CCCC", V_CCCC);
	            row.put("V_TT", V_TT);
	            row.put("V01301", mwrPre.getStationNumberChina());
	            row.put("V01300", Integer.parseInt(StationCodeUtil.stringToAscii(stat)));
	            row.put("V05001", latitude);
	            row.put("V06001", longtitude);
	            row.put("V_NCODE", CountryCode);
	            row.put("V_ACODE", defaultInt);
	        	//"V04001,V04002,V04003,V04004,V04005,V_LEN,V_REPORT
	            row.put("V04001", mwrPre.getObservationTime().getYear() + 1900);
	            row.put("V04002", mwrPre.getObservationTime().getMonth() + 1);
	            row.put("V04003", mwrPre.getObservationTime().getDate());
	            row.put("V04004", mwrPre.getObservationTime().getHours());
	            row.put("V04005", mwrPre.getObservationTime().getMinutes());
	            row.put("V_LEN", reportInfos.get(i).getReport().length());
	            row.put("V_REPORT", reportInfos.get(i).getReport());
	            try {
					OTSDbHelper.getInstance().insert(tablename, row);
				}  catch (Exception e) {
					loggerBuffer.append(row);
				}
			}
			loggerBuffer.append(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
		    loggerBuffer.append(" INSERT SUCCESS COUNT : " + successRowCount + "\n");
		    loggerBuffer.append(" INSERT FAILED COUNT : " + (reportInfos.size() - successRowCount) + "\n");
		}
		
	}	
}
