package cma.cimiss2.dpc.indb.sevp.dc_sevp_world_forcast.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.ots.OTSDbHelper;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.bean.sevp.ForeignForecast;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {
	public static BlockingQueue<StatDi> diQueues;
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}

	public static DataBaseAction insert_ots(List<ForeignForecast> forecasts, String tablename, Date recv_time, StringBuffer loggerBuffer, String fileN) {
		
		if(forecasts != null && forecasts.size() > 0) {
			int successRowCount = forecasts.size();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");		
			String V_TT = "ACCF";
			Date dataTime = null;
			String station = "";
			for(int idx = 0; idx < forecasts.size(); idx ++){
				Map<String, Object> row = new HashMap<String, Object>();
				ForeignForecast forecast = forecasts.get(idx);
					
				dataTime = forecast.getObservationTime();
				station = forecast.getStationNumberChina();	
				row.put("D_RECORD_ID", sdf.format(dataTime)+"_"+station+"_"+forecast.getForecastNumberOfDays());
	            row.put("D_DATA_ID", sod_code);
	            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_DATETIME", TimeUtil.date2String(dataTime,"yyyy-MM-dd HH:mm:ss"));
	          //"V_BBB,V01301,V01300,V05001,V06001,V07001,V04001,V04002,V04003,V04004,"
				row.put("V_BBB", forecast.getCorrectSign());
				row.put("V01301", forecast.getStationNumberChina());
				row.put("V01300", 999999);
				row.put("V05001", forecast.getLatitude());
				row.put("V06001", forecast.getLongtitude());
				row.put("V07001", forecast.getHeight());
				row.put("V04001", dataTime.getYear() + 1900);
				row.put("V04002", dataTime.getMonth() + 1);
				row.put("V04003", dataTime.getDate());
				row.put("V04004", dataTime.getHours());
				//+ "V04320_040,V04322,V12052,V12053,V11303,V11303_20_08,V11301,V11301_20_08,V20312_20_08,V20312_08_20,"
				row.put("V04320_040", forecast.getNumberOfForecastEfficiency());
				row.put("V04322", forecast.getForecastNumberOfDays());
				row.put("V12052", forecast.getDayTemperature());
				row.put("V12053", forecast.getNightTemperature());
				row.put("V11303", forecast.getDayWindDir());
				row.put("V11303_20_08", forecast.getNightWindDir());
				row.put("V11301", forecast.getDayWindPower());
				row.put("V11301_20_08", forecast.getNightWindPower());
				row.put("V20312_20_08", forecast.getNightWeatherPheno());
				row.put("V20312_08_20", forecast.getDayWeatherPheno());
				//+ "V20010,V20010_20_08,V13016,V13016_20_08,V13320_08_20,V13320_20_08) 
				row.put("V20010", forecast.getDayCloudCoverageRate());
				row.put("V20010_20_08", forecast.getNightCloudCoverageRate());
				row.put("V13016", forecast.getDayRainfall());
				row.put("V13016_20_08", forecast.getNightRainfall());
				row.put("V13320_08_20", forecast.getDayPrecipitationProbability());
				row.put("V13320_20_08", forecast.getNightPrecipitationProbability());
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
				di.setLATITUDE(String.valueOf(forecast.getLatitude()));
				di.setLONGTITUDE(String.valueOf(forecast.getLongtitude()));
				try {
					OTSDbHelper.getInstance().insert(tablename, row);
					diQueues.offer(di);
				}  catch (Exception e) {
					loggerBuffer.append(e.getMessage());
					successRowCount = successRowCount -1;
					di.setPROCESS_STATE("1");
					diQueues.offer(di);
				}
	        }
			 loggerBuffer.append(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
		     loggerBuffer.append(" INSERT SUCCESS COUNT : " + successRowCount + "\n");
		     loggerBuffer.append(" INSERT FAILED COUNT : " + (forecasts.size() - successRowCount) + "\n");
		}
		return DataBaseAction.SUCCESS;
	}
}
