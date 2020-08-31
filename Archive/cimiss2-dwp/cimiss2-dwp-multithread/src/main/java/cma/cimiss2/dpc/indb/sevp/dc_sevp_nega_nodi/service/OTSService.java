package cma.cimiss2.dpc.indb.sevp.dc_sevp_nega_nodi.service;

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
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.sevp.NegOxygenIon;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static BlockingQueue<StatDi> diQueues;
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	public static String sod_report_code = StartConfig.reportSodCode();
//	static Map<String, Object> proMap = StationInfo.getProMap();
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}

	public static DataBaseAction insert_ots(List<NegOxygenIon> negOxygenIons, String tablename, Date recv_time, StringBuffer loggerBuffer, String fileN) {
		Map<String, Object> proMap = StationInfo.getProMap();
		if(negOxygenIons != null && negOxygenIons.size() > 0) {
			int successRowCount = negOxygenIons.size();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");		
			String V_TT = "NEO";
			for(int idx = 0; idx < negOxygenIons.size(); idx ++){
				Map<String, Object> row = new HashMap<String, Object>();
				NegOxygenIon negOxygenIon = negOxygenIons.get(idx);
				int stationLevel = 999999;
				int adminCode = 999999;
				Date dataTime = negOxygenIon.getObservationTime();
				String station = negOxygenIon.getStationNumberChina();
			
				row.put("D_RECORD_ID", sdf.format(dataTime)+"_"+station);
	            row.put("D_DATA_ID", sod_code);
	            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_DATETIME", TimeUtil.date2String(dataTime,"yyyy-MM-dd HH:mm:ss"));
	            
	            String info = (String) proMap.get(station + "+01");
				if(info == null) {
					loggerBuffer.append("\n In configuration file, this station does not exist: " + station);
				}
				else{
					String[] infos = info.split(",");
					if(!infos[5].equals("null"))  //中国行政区域代码
						adminCode = Integer.parseInt(infos[5]);
					if(!infos[6].equals("null"))
						stationLevel = Integer.parseInt(infos[6]); // 台站级别
				}
				//+ "V_BBB,V01301,V01300,V05001,V06001,V07001,V02001,V02301,V_ACODE,V71115,"
				row.put("V_BBB", "000");
				row.put("V01301", station);
				row.put("V01300", Integer.parseInt(StationCodeUtil.stringToAscii(station)));
				row.put("V05001", negOxygenIon.getLatitude());
				row.put("V06001", negOxygenIon.getLongtitude());
				row.put("V07001", negOxygenIon.getHeight());
				row.put("V02001", 0); // 测站类型
				row.put("V02301", stationLevel); // 台站级别
				row.put("V_ACODE", adminCode);
				row.put("V71115", negOxygenIon.getSiteMarkOfStation());
					//+ "V33307,V04001,V04002,V04003,V04004,V04005,V15551,V15551_005,V15551_005_052,V15551_006,"
				row.put("V33307", negOxygenIon.getQualityContorl());
				row.put("V04001", dataTime.getYear() + 1900);
				row.put("V04002", dataTime.getMonth() + 1);
				row.put("V04003", dataTime.getDate());
				row.put("V04004", dataTime.getHours());
				row.put("V04005", dataTime.getMinutes());
				row.put("V15551", negOxygenIon.getNegOxyIonConcentration().getValue());
				row.put("V15551_005", negOxygenIon.getMaxNegOxyIonConcentration().getValue());
				row.put("V15551_005_052", negOxygenIon.getTimeOfMaxNegOxyIonConcentration().getValue());
				row.put("V15551_006", negOxygenIon.getMinNegOxyIonConcentration().getValue());
				//+ "V15551_006_052,V12001,V12011,V12011_052,V12012,V12012_052,V13003,V13008,V13008_052,V12003,"
				row.put("V15551_006_052", negOxygenIon.getTimeOfMinNegOxyIonConcentration().getValue());
				row.put("V12001", negOxygenIon.getTemperature().getValue());
				row.put("V12011", negOxygenIon.getMaxTemperature().getValue());
				row.put("V12011_052", negOxygenIon.getTimeOfMaxTemperature().getValue());
				row.put("V12012", negOxygenIon.getMinTemperature().getValue());
				row.put("V12012_052", negOxygenIon.getTimeOfMinTemperature().getValue());
				row.put("V13003", negOxygenIon.getRelativeHumidity().getValue());
				row.put("V13008", negOxygenIon.getMaxRelativeHumidity().getValue());
				row.put("V13008_052", negOxygenIon.getTimeOfMaxRelativeHumidity().getValue());
				row.put("V12003", negOxygenIon.getDewPoint().getValue());
				//+ "V13004,Q15551,Q15551_005,Q15551_005_052,Q15551_006,Q15551_006_052,Q12001,Q12011,Q12011_052,Q12012,"
				row.put("V13004", negOxygenIon.getWaterVaporPressure().getValue());
				row.put("Q15551", negOxygenIon.getNegOxyIonConcentration().getQuality().get(0).getCode());
				row.put("Q15551_005", negOxygenIon.getMaxNegOxyIonConcentration().getQuality().get(0).getCode());
				row.put("Q15551_005_052", negOxygenIon.getTimeOfMaxNegOxyIonConcentration().getQuality().get(0).getCode());
				row.put("Q15551_006", negOxygenIon.getMinNegOxyIonConcentration().getQuality().get(0).getCode());
				row.put("Q15551_006_052", negOxygenIon.getTimeOfMinNegOxyIonConcentration().getQuality().get(0).getCode());
				row.put("Q12001", negOxygenIon.getTemperature().getQuality().get(0).getCode());
				row.put("Q12011", negOxygenIon.getMaxTemperature().getQuality().get(0).getCode());
				row.put("Q12011_052", negOxygenIon.getTimeOfMaxTemperature().getQuality().get(0).getCode());
				row.put("Q12012", negOxygenIon.getMinTemperature().getQuality().get(0).getCode());
				//+ "Q12012_052,Q13003,Q13008,Q13008_052,Q12003,Q13004) "
				row.put("Q12012_052", negOxygenIon.getTimeOfMinTemperature().getQuality().get(0).getCode());
				row.put("Q13003", negOxygenIon.getRelativeHumidity().getQuality().get(0).getCode());
				row.put("Q13008", negOxygenIon.getMaxRelativeHumidity().getQuality().get(0).getCode());
				row.put("Q13008_052", negOxygenIon.getTimeOfMaxRelativeHumidity().getQuality().get(0).getCode());
				row.put("Q12003", negOxygenIon.getDewPoint().getQuality().get(0).getCode());
				row.put("Q13004", negOxygenIon.getWaterVaporPressure().getQuality().get(0).getCode());
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
				di.setIIiii(station);
				di.setDATA_TIME(TimeUtil.date2String(dataTime, "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				di.setLATITUDE(String.valueOf(negOxygenIon.getLatitude()));
				di.setLONGTITUDE(String.valueOf(negOxygenIon.getLongtitude()));
			
				try {
					OTSDbHelper.getInstance().insert(tablename, row);
					diQueues.offer(di);
				}  catch (Exception e) {
					successRowCount = successRowCount -1;
					di.setPROCESS_STATE("1");
					diQueues.offer(di);
					loggerBuffer.append(row);
					loggerBuffer.append(e.getMessage());
				}
	        }
			 loggerBuffer.append(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
		     loggerBuffer.append(" INSERT SUCCESS COUNT : " + successRowCount + "\n");
		     loggerBuffer.append(" INSERT FAILED COUNT : " + (negOxygenIons.size() - successRowCount) + "\n");
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
	public
	static void reportInfoToDb(List<ReportInfo> reportInfos, String tablename, Date recv_time, StringBuffer loggerBuffer) {
		Map<String, Object> proMap = StationInfo.getProMap();
		if(reportInfos != null && reportInfos.size() > 0){
			List<Map<String, Object>> batchs = new ArrayList<Map<String, Object>>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String V_TT= "NEO";
			NegOxygenIon negOxygenIon = null;
			Date oTime = null;
			String primkey = null;
			String sta;
			String info;
			int adminCode = 999999;
			for (int i = 0; i < reportInfos.size(); i++) {
				Map<String, Object> row = new HashMap<String, Object>();
				negOxygenIon = (NegOxygenIon) reportInfos.get(i).getT();
				sta = negOxygenIon.getStationNumberChina();
				oTime = negOxygenIon.getObservationTime();
				primkey = sdf.format(oTime)+"_"+sta+"_"+V_TT;
				
				row.put("D_RECORD_ID", primkey);
	            row.put("D_DATA_ID", sod_report_code);
	            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_DATETIME", TimeUtil.date2String(oTime,"yyyy-MM-dd HH:mm:ss"));
	            
	        	// "V_BBB,V_CCCC,V_TT,"
				row.put("V_BBB", "000");
				row.put("V_CCCC", "9999");
				row.put("V_TT", V_TT);
				// "V01301,V01300,V05001,V06001,V_NCODE,V_ACODE,V04001,V04002,V04003,V04004,V04005,"
				row.put("V01301", sta);
				row.put("V01300", Integer.parseInt(StationCodeUtil.stringToAscii(sta)));
				info = (String) proMap.get(sta + "+01");
				if(info == null) {
					loggerBuffer.append("\n  In configuration file, this station does not exist: " + sta);
				}
				else{
					String[] infos = info.split(",");						
					if(infos[5].equals("null"))
						loggerBuffer.append("\n  In configuration file, v_cccc is null! ");
					else 
						adminCode = Integer.parseInt(infos[5]);
				}
				row.put("V05001", negOxygenIon.getLatitude());
				row.put("V06001", negOxygenIon.getLongtitude());
				row.put("V_NCODE", 2250);
				row.put("V_ACODE", adminCode);
				row.put("V04001", oTime.getYear() + 1900);
				row.put("V04002", oTime.getMonth() + 1);
				row.put("V04003", oTime.getDate());
				row.put("V04004", oTime.getHours());
				row.put("V04005", oTime.getMinutes());
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
