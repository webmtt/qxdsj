package cma.cimiss2.dpc.indb.agme.dc_agme_asmm.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.config.CTSCodeMap;
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
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
import cma.cimiss2.dpc.decoder.bean.cawn.ASMLevel;
import cma.cimiss2.dpc.decoder.bean.cawn.ZAgmeASM;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {
	public static BlockingQueue<StatDi> diQueues;
	
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
//	private static Map<String, Object> proMap = StationInfo.getProMap();
	
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}
	/**
	 * @Title: processSuccessReport 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param list
	 * @param recv_time
	 * @param cts_code
	 * @param fileN
	 * @param isRevised
	 * @param v_bbb
	 * @param loggerBuffer 
	 * @param ctsCodeMaps 
	 * @return DataBaseAction
	 * @throws:
	 */
	@SuppressWarnings("deprecation")
	public static DataBaseAction processSuccessReport(List<ZAgmeASM> list, Date recv_time,
			String cts_code, String fileN, boolean isRevised, String v_bbb, StringBuffer loggerBuffer, List<CTSCodeMap> ctsCodeMaps) {
		Map<String, Object> proMap = StationInfo.getProMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		for (int i = 0; i < list.size(); i++){
			ZAgmeASM agmeASM = list.get(i);	
			
			StatDi di = new StatDi();					
			di.setFILE_NAME_O(fileN);
			di.setDATA_TYPE(ctsCodeMaps.get(0).getSod_code());
			di.setDATA_TYPE_1(ctsCodeMaps.get(0).getCts_code());
			di.setTT("ASM");			
			di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
			di.setPROCESS_START_TIME(TimeUtil.getSysTime());
			di.setFILE_NAME_N(fileN);
			di.setBUSINESS_STATE("0"); //0成功，1失败
			di.setPROCESS_STATE("0");  //0成功，1失败
			di.setIIiii(agmeASM.getStationNumberChina());
			di.setDATA_TIME(TimeUtil.date2String(agmeASM.getObservationTime(), "yyyy-MM-dd HH:mm:ss"));
			di.setPROCESS_END_TIME(TimeUtil.getSysTime());
			di.setRECORD_TIME(TimeUtil.getSysTime());
			String primkey = sdf.format(agmeASM.getObservationTime())+"_"+agmeASM.getStationNumberChina();
			List<ASMLevel> asmLevels = agmeASM.getAsm_soil();
			try {
				for (int j = 0; j < asmLevels.size(); j++) {
					ASMLevel asmLevel = asmLevels.get(j);
					
					Map<String, Object> row = new HashMap<String, Object>();
					row.put("D_ELE_ID", primkey+"_"+(asmLevel.getSoilLevelLabeling()));
					row.put("D_RECORD_ID", primkey);
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
					
					row.put("D_DATETIME", TimeUtil.date2String(agmeASM.getObservationTime(),"yyyy-MM-dd HH:mm:ss"));
					row.put("V01301", agmeASM.getStationNumberChina());
					row.put("V07061", asmLevel.getSoilLevelLabeling());
					row.put("V71105", asmLevel.getSoilVolumeMoistureContent());
					row.put("V71102", asmLevel.getSoilRelativeHumidity());
					row.put("V71104", asmLevel.getSoilWeightMoistureContent());
					row.put("V71107", asmLevel.getSoilAavailableWaterContent());
					row.put("Q71105", 9);
					row.put("Q71102", 9);
					row.put("Q71104", 9);
					row.put("Q71107", 9);
					row.put("V_BBB", v_bbb);
					row.put("D_SOURCE_ID", cts_code);
					
//					if(asmLevel.getSoilVolumeMoistureContent() >=0 && asmLevel.getSoilRelativeHumidity() >= 0 && asmLevel.getSoilWeightMoistureContent() >= 0){
						if(isRevised) {
							OTSDbHelper.getInstance().update(StartConfig.valueTable(),"D_ELE_ID",row);
						}else {
							OTSDbHelper.getInstance().insert(StartConfig.valueTable(), "D_ELE_ID",row);
						}
//					}
//					else
//						continue;
					
				}
				
				Map<String, Object> rowk = new HashMap<String, Object>();
				rowk.put("D_RECORD_ID", primkey);
				rowk.put("D_DATA_ID", ctsCodeMaps.get(0).getSod_code());
				rowk.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
				rowk.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
				rowk.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
				rowk.put("D_DATETIME", TimeUtil.date2String(agmeASM.getObservationTime(), "yyyy-MM-dd HH:mm:ss"));
				rowk.put("V01301", agmeASM.getStationNumberChina());
				rowk.put("V01300", Integer.parseInt(StationCodeUtil.stringToAscii(agmeASM.getStationNumberChina())) );
				rowk.put("V05001", agmeASM.getLatitude());
				rowk.put("V06001", agmeASM.getLongitude());
				
				// 根根配置文件  获取站点基本信息
				String info = (String) proMap.get(agmeASM.getStationNumberChina() + "+13");
				int adminCode = 999999;
				if(info != null) {
					String[] infos = info.split(",");
					if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
						adminCode = Integer.parseInt(infos[5]);
					if(agmeASM.getHeightOfSationGroundAboveMeanSeaLevel().doubleValue() == 999999.0 && infos.length >= 4 && !infos[3].trim().equals("null")){
						agmeASM.setHeightOfSationGroundAboveMeanSeaLevel(Double.parseDouble(infos[3]));
					}
				}
				
				if(adminCode == 999999){
					info = (String) proMap.get(agmeASM.getStationNumberChina() + "+01");
					if(info == null) {
						loggerBuffer.append("\n In the configuration file, the station number does not exist" + agmeASM.getStationNumberChina());
					}else {
						String[] infos = info.split(",");
						if(infos.length >= 6)
							adminCode = Integer.parseInt(infos[5]);
						if(agmeASM.getHeightOfSationGroundAboveMeanSeaLevel().doubleValue() == 999999.0 && infos.length >= 4 && !infos[3].trim().equals("null")){
							agmeASM.setHeightOfSationGroundAboveMeanSeaLevel(Double.parseDouble(infos[3]));
						}
					}
				}
				rowk.put("V07001", agmeASM.getHeightOfSationGroundAboveMeanSeaLevel());
				rowk.put("V_ACODE", adminCode);
				rowk.put("V04001", agmeASM.getObservationTime().getYear() + 1900);
				rowk.put("V04002", agmeASM.getObservationTime().getMonth()+1);
				rowk.put("V04003", agmeASM.getObservationTime().getDate());
				rowk.put("V04004", agmeASM.getObservationTime().getHours());
				rowk.put("V04005", agmeASM.getObservationTime().getMinutes());
				rowk.put("V71115", agmeASM.getMeasureLocationIndication());
				rowk.put("V_BBB", v_bbb);
				rowk.put("D_SOURCE_ID", cts_code);
				
				if(isRevised) {
					OTSDbHelper.getInstance().update(StartConfig.keyTable(), rowk);
				}else {
					OTSDbHelper.getInstance().insert(StartConfig.keyTable(), rowk);
				}
				
				diQueues.offer(di);
				
			} catch (Exception e) {
				loggerBuffer.append(e.getMessage());
				di.setPROCESS_STATE("1");
				diQueues.offer(di);
				if(e.getClass() == ClientException.class) {
					return DataBaseAction.CONNECTION_ERROR;
				}
			}
		}
		return DataBaseAction.SUCCESS;
	}
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public static void reportInfoToDb(List<ReportInfo> reportInfos, String v_bbb, Date recv_time,String v_cccc, String v_tt, StringBuffer loggerBuffer, List<CTSCodeMap> codeMaps) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		if(reportInfos != null && reportInfos.size() > 0) {
			List<Map<String, Object>> batchs = new ArrayList<Map<String, Object>>();
	        for (int i = 0; i < reportInfos.size(); i++) {
	        	Map<String, Object> row = new HashMap<String, Object>();
	        	AgmeReportHeader agmeReportHeader = (AgmeReportHeader) reportInfos.get(i).getT();
	        	String CropType = agmeReportHeader.getCropType().replaceAll("-", "");
				String primkey = sdf.format(agmeReportHeader.getReport_time())+"_"+agmeReportHeader.getStationNumberChina()+"_"+CropType+"_"+v_tt+"_"+v_bbb;
				row.put("D_RECORD_ID", primkey);
				row.put("D_DATA_ID", codeMaps.get(0).getReport_sod_code());
				row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
		        row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
		        row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
		        row.put("D_DATETIME", TimeUtil.date2String(agmeReportHeader.getReport_time(),"yyyy-MM-dd HH:mm:ss"));  
				row.put("V_BBB", v_bbb);
				row.put("V_CCCC", v_cccc);
				row.put("V01301", agmeReportHeader.getStationNumberChina());
				row.put("V01300", Integer.parseInt(StationCodeUtil.stringToAscii(agmeReportHeader.getStationNumberChina())));
				row.put("V05001", agmeReportHeader.getLatitude());
				row.put("V06001", agmeReportHeader.getLongitude());
				row.put("V_ACODE", 0);
				row.put("V04001", agmeReportHeader.getReport_time().getYear()+1900);
				row.put("V04002", agmeReportHeader.getReport_time().getMonth()+1);
				row.put("V04003", agmeReportHeader.getReport_time().getDate());
				row.put("V04004", agmeReportHeader.getReport_time().getHours());
				row.put("V04005", agmeReportHeader.getReport_time().getMinutes());
				row.put("V04006", agmeReportHeader.getReport_time().getSeconds());
				row.put("V_TT", v_tt);
				row.put("V_ELE_KIND", CropType);
				row.put("V01323", CropType);
				row.put("V_LEN", reportInfos.get(i).getReport().length());
				row.put("V_REPORT", reportInfos.get(i).getReport());

	            batchs.add(row);
	        }
	        OTSBatchResult result = OTSDbHelper.getInstance().insert("AGME_ECO_REP_ABRE_SECT_TAB", batchs);
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
