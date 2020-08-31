package cma.cimiss2.dpc.indb.cawn.dc_cawn_regqc.service;

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

import cma.cimiss2.dpc.decoder.bean.cawn.REGQuality;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
//	static Map<String, Object> proMap = StationInfo.getProMap();
	public static BlockingQueue<StatDi> diQueues;
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}

	public static DataBaseAction insert_ots(List<REGQuality> regQualities, String tablename, Date recv_time, StringBuffer loggerBuffer, String fileN) {
		Map<String, Object> proMap = StationInfo.getProMap();
		if(regQualities != null && regQualities.size() > 0) {
			int successRowCount = regQualities.size();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String V_TT = "REGL";
			for(int idx = 0; idx < regQualities.size(); idx ++){
				Map<String, Object> row = new HashMap<String, Object>();
				REGQuality regQuality = regQualities.get(idx);
				double latitude = 999999;
				double longtitude = 999999;
				double height = 999999;
				int adminCode = 999999;
				Date dataTime = regQuality.getObservationTime();
				String station = regQuality.getStationNumberChina();
				
				//row.put("D_RECORD_ID", sdf.format(dataTime)+"_"+station + "_" + regQuality.getObserveItem());
	            row.put("D_DATA_ID", sod_code);
	            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_DATETIME", TimeUtil.date2String(dataTime,"yyyy-MM-dd HH:mm:ss"));
	         
	            row.put("V01301", station);
				row.put("V01300", Integer.parseInt(StationCodeUtil.stringToAscii(station)));
				String info = (String) proMap.get(station + "+16");
				if(info == null) {
					loggerBuffer.append("\n In configuration file, this station does not exist: " + station);
				}
				else{
					String[] infos = info.split(",");						
					if(infos[1].equals("null"))
						loggerBuffer.append("\n In configuration file, longtitude is null!");
					else
						longtitude = Double.parseDouble(infos[1]);
					if(infos[2].equals("null"))
						loggerBuffer.append("\n In configuration file, latitude is null!");
					else
						latitude = Double.parseDouble(infos[2]);
					if(infos[3].equals("null"))
						loggerBuffer.append("\n In configuration file, station height is null!");
					else
						height = Double.parseDouble(infos[3]);
					if(infos[5].equals("null"))
						loggerBuffer.append("\n In configuration file, V_CCCC is null!");
					else 
						adminCode = Integer.parseInt(infos[5]);
				}
				row.put("V05001", latitude);
				row.put("V06001", longtitude);
				row.put("V07001", height);
				row.put("V_ACODE", adminCode);
				row.put("V04001", dataTime.getYear() + 1900);
				row.put("V04002", dataTime.getMonth() + 1);
				row.put("V04003", dataTime.getDate());
				row.put("V04004", dataTime.getHours());
				//"V_OBS_ITEM,V_QC_BEG,V_QC_END,V_RUN_STATE,V_MAINTEN,V_ENVIRON,V_WEATHER_PHEN,V_WATCHER,V_NOTES,V_BBB) "
				row.put("V_OBS_ITEM", regQuality.getObserveItem());
				row.put("V_QC_BEG", regQuality.getQcInfoStartTime());
				row.put("V_QC_END", regQuality.getQcInfoEndTime());
				row.put("V_RUN_STATE", regQuality.getRunningState());
				row.put("V_MAINTEN", regQuality.getMaintenance());
				row.put("V_ENVIRON", regQuality.getPeripheralCondition());
				row.put("V_WEATHER_PHEN", regQuality.getWeatherPhone());
				row.put("V_WATCHER", regQuality.getAttendantName());
				row.put("V_NOTES", regQuality.getTextNote());
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
				di.setIIiii(station);
				di.setDATA_TIME(TimeUtil.date2String(dataTime, "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				di.setLATITUDE(String.valueOf(latitude));
				di.setLONGTITUDE(String.valueOf(longtitude));
				
				try {
					OTSDbHelper.getInstance().insert(tablename, row);
					diQueues.offer(di);
				}catch (Exception e) {
					successRowCount = successRowCount -1;
					di.setPROCESS_STATE("1");
					diQueues.offer(di);
					loggerBuffer.append(row+"\n");
					loggerBuffer.append(e.getMessage()+"\n");
					if(e.getClass() == ClientException.class) {
						return DataBaseAction.CONNECTION_ERROR;
					}
				}
			}
			loggerBuffer.append(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
	        loggerBuffer.append(" INSERT SUCCESS COUNT : " + successRowCount + "\n");
	        loggerBuffer.append(" INSERT FAILED COUNT : " + (regQualities.size() - successRowCount) + "\n");
		}
		return DataBaseAction.SUCCESS;
	}
}
