package cma.cimiss2.dpc.indb.sevp.dc_sevp_world_live.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.ots.OTSDbHelper;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.bean.sevp.ForeignLive;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {
	public static BlockingQueue<StatDi> diQueues;
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();	
	
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}


	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}


	public static DataBaseAction insert_ots(List<ForeignLive> lives, String tablename, Date recv_time, StringBuffer loggerBuffer, String fileN) {
		
		if(lives != null && lives.size() > 0) {
			int successRowCount = lives.size();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");		
			String V_TT = "ACCL";
			for(int idx = 0; idx < lives.size(); idx ++){
				Map<String, Object> row = new HashMap<String, Object>();
				ForeignLive live = lives.get(idx);
				Date dataTime = live.getObservationTime();
				String station = live.getStationNumberChina();
				
				row.put("D_RECORD_ID", sdf.format(dataTime)+"_"+station);
	            row.put("D_DATA_ID", sod_code);
	            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_DATETIME", TimeUtil.date2String(dataTime,"yyyy-MM-dd HH:mm:ss"));
	        	//"V_BBB,V01301,V01300,V04001,V04002,V04003,V04004,V05001,V06001,V07001,"
				row.put("V_BBB", live.getCorrectSign());
				row.put("V01301", live.getStationNumberChina());
				row.put("V01300", 999999);
				row.put("V04001", dataTime.getYear() + 1900);
				row.put("V04002", dataTime.getMonth() + 1);
				row.put("V04003", dataTime.getDate());
				row.put("V04004", dataTime.getHours());
				row.put("V05001", live.getLatitude());
				row.put("V06001", live.getLongtitude());
				row.put("V07001", live.getHeight());
				//V12001,V13003,V12003,V11001,V11002,V10004,V20312,V20001,V20010,V12510)
				row.put("V12001", live.getTemperature());
				row.put("V13003", live.getHumidity());
				row.put("V12003", live.getDewpoint());
				row.put("V11001", live.getWindDir());
				row.put("V11002", live.getWindSpeed());
				row.put("V10004", live.getAirPressure());
				row.put("V20312", live.getWeatherPheno());
				row.put("V20001", live.getVisibility());
				row.put("V20010", live.getCloudCoverageRate());
				row.put("V12510", live.getSurfaceTemperature());
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
				di.setLATITUDE(String.valueOf(live.getLatitude()));
				di.setLONGTITUDE(String.valueOf(live.getLongtitude()));
				
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
		     loggerBuffer.append(" INSERT FAILED COUNT : " + (lives.size() - successRowCount) + "\n");
		}
		return DataBaseAction.SUCCESS;
	}
}
