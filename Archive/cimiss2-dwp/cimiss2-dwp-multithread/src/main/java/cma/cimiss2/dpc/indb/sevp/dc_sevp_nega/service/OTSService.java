package cma.cimiss2.dpc.indb.sevp.dc_sevp_nega.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.ots.OTSDbHelper;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.bean.sevp.Travel;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static BlockingQueue<StatDi> diQueues;
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	Map<String, Object> proMap = StationInfo.getProMap();
	public static String V_TT = "TRAVEL";
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}

	public static DataBaseAction insert_ots(List<Travel> travels, String tablename, Date recv_time, StringBuffer loggerBuffer, String fileN) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");	
		if(travels != null && travels.size() > 0) {
			int successRowCount = travels.size();
			for(int idx = 0; idx < travels.size(); idx ++){
				Map<String, Object> row = new HashMap<String, Object>();
				Travel travel = travels.get(idx);
				// 非更正报；或者为更正报，但是数据库中尚未有这一条数据
				Date dataTime = travel.getReleaseTime();
				String prodID = travel.getProductID();
				
				row.put("D_RECORD_ID", prodID);//由产品发布时间（PT）+影响景区纬度（IVLAT）+影响景区经度（IVLON）+产品信息编码（VA）+景区预警信号发布与撤销标识（WA）组成
	            row.put("D_DATA_ID", sod_code);
	            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_DATETIME", TimeUtil.date2String(dataTime,"yyyy-MM-dd HH:mm:ss"));
	            //D_SVCO,
	            row.put("D_SVCO", prodID);
	            //"V04001,V04001,V04003,V04004,V04005,V_PUNA,V_PUPROCO,V_PUCO,V_SPNA,V_SPCO,"
	        	row.put("V04001", dataTime.getYear() + 1900);
				row.put("V04002", dataTime.getMonth() + 1);
				row.put("V04003", dataTime.getDate());
				row.put("V04004", dataTime.getHours());
				row.put("V04005", dataTime.getMinutes());
				row.put("V_PUNA", travel.getReleaseOrgName());
				row.put("V_PUPROCO", travel.getProvCode());
				row.put("V_PUCO", travel.getReleaseOrgCode());
				row.put("V_SPNA", travel.getSceneryName());
				row.put("V_SPCO", travel.getSceneryCode());
	            // "V_VA,V_ISTS,V_ITEN,V_IVNA,V05001,V06001,V_IVSN,V_IVSLAT,V_IVSLON,V_PD,"
				row.put("V_VA", travel.getSceneryProductInfo());
				row.put("V_ISTS",travel.getAffectedStartTime());
				row.put("V_ITEN", travel.getAffectedEndTime());
				row.put("V_IVNA", travel.getScenicSpotName());
				row.put("V05001", travel.getScenicSpotLat());
				row.put("V06001", travel.getScenicSpotLon());
				row.put("V_IVSN", travel.getAffectedScenicSpotName());
				row.put("V_IVSLAT", travel.getAffectedScenicSportLat());
				row.put("V_IVSLON", travel.getAffectedScenicSportLon());
				row.put("V_PD", travel.getDisaOrProdContent());
				//"V_ET,V_PS,V_WA,V_PUER,V_BBB) "
				row.put("V_ET", travel.getEvolutionTrend());
				row.put("V_PS", travel.getSuggestions());
				row.put("V_WA", travel.getSignalReleaseAndCancelCode());
				row.put("V_PUER", travel.getPublisherName());
				row.put("V_BBB", "000");
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
				di.setIIiii(prodID);
				di.setDATA_TIME(TimeUtil.date2String(dataTime, "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				di.setLATITUDE(String.valueOf(travel.getAffectedScenicSportLat()));
				di.setLONGTITUDE(String.valueOf(travel.getAffectedScenicSportLon()));	
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
		    loggerBuffer.append(" INSERT FAILED COUNT : " + (travels.size() - successRowCount) + "\n");
		}
		return DataBaseAction.SUCCESS;
	}
}
