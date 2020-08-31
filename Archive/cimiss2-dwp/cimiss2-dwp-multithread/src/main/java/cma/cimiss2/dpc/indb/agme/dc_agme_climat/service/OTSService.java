package cma.cimiss2.dpc.indb.agme.dc_agme_climat.service;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.ots.OTSDbHelper;
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alicloud.openservices.tablestore.ClientException;

import cma.cimiss2.dpc.decoder.bean.agme.CroplandMicroclimateData;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	
	static String sod_code = "E.0023.0001.S001";
	public static BlockingQueue<StatDi> diQueues;
	
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}


	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}


	public static DataBaseAction insert_ots(List<CroplandMicroclimateData> list, String tablename, Date recv_time, StringBuffer loggerBuffer,  String cts_code, String fileN) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		if(list != null && list.size() > 0) {
			int successRowCount = list.size();
	        for (int i = 0; i < list.size(); i++) {
	        	CroplandMicroclimateData microclimateData = list.get(i);
	            Map<String, Object> row = new HashMap<String, Object>();
	            String primkey = sdf.format(microclimateData.getObservationTime()) + "_" + microclimateData.getStationNumberChina() + "_" +microclimateData.getCropName();
	            row.put("D_RECORD_ID", primkey);
	            row.put("D_DATA_ID", sod_code);
	            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_DATETIME", TimeUtil.date2String(microclimateData.getObservationTime(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("V01301", microclimateData.getStationNumberChina());
	            row.put("V01300",  StationCodeUtil.stringToAscii(microclimateData.getStationNumberChina()));
	            row.put("V_STNAME", microclimateData.getStationNameChina());
	            row.put("V05001", microclimateData.getLatitude());
	            row.put("V06001", microclimateData.getLongitude());
	            row.put("V07001", microclimateData.getHeightOfSationGroundAboveMeanSeaLevel());
	            row.put("V_PROV", microclimateData.getProvinceNameChina());
	            row.put("V04001", microclimateData.getObservationTime().getYear() + 1900);
	            row.put("V04002", microclimateData.getObservationTime().getMonth() + 1);
	            row.put("V04003", microclimateData.getObservationTime().getDate());
	            row.put("V04004", microclimateData.getObservationTime().getHours());
	            row.put("V71001", microclimateData.getCropName());
	            row.put("V07004", microclimateData.getStationPressure());
	            row.put("V12001_030", microclimateData.getTemperature_30());
	            row.put("V12001_060", microclimateData.getTemperature_60());
	            row.put("V12001_150", microclimateData.getTemperature_150());
	            row.put("V12001_300", microclimateData.getTemperature_300());
	            row.put("V12001_CNP", microclimateData.getTemperatureCanopy());
	            row.put("V13019", microclimateData.getHourlyPrecipitation());
	            row.put("V11002_030", microclimateData.getWindSpeed_30());
	            row.put("V11002_060", microclimateData.getWindSpeed_60());
	            row.put("V11002_150", microclimateData.getWindSpeed_150());
	            row.put("V11002_300", microclimateData.getWindSpeed_300());
	            row.put("V11002_600", microclimateData.getWindSpeed_600());
	            row.put("V13003_030", microclimateData.getRelativeHumidity_Air_30());
	            row.put("V13003_060", microclimateData.getRelativeHumidity_Air_60());
	            row.put("V13003_150", microclimateData.getRelativeHumidity_Air_150());
	            row.put("V13003_300", microclimateData.getRelativeHumidity_Air_300());
	            row.put("V12030_000", microclimateData.getSurfaceTemperature_0());
	            row.put("V12030_005", microclimateData.getSurfaceTemperature_5());
	            row.put("V12030_010", microclimateData.getSurfaceTemperature_10());
	            row.put("V12030_015", microclimateData.getSurfaceTemperature_15());
	            row.put("V12030_020", microclimateData.getSurfaceTemperature_20());
	            row.put("V12030_030", microclimateData.getSurfaceTemperature_30());
	            row.put("V12030_040", microclimateData.getSurfaceTemperature_40());
	            row.put("V12030_050", microclimateData.getSurfaceTemperature_50());
	            row.put("V71105_010", microclimateData.getMoisture_Soil_10());
	            row.put("V71105_020", microclimateData.getMoisture_Soil_20());
	            row.put("V71105_030", microclimateData.getMoisture_Soil_30());
	            row.put("V71105_040", microclimateData.getMoisture_Soil_40());
	            row.put("V71105_050", microclimateData.getMoisture_Soil_50());
	            row.put("V71102_010", microclimateData.getRelativeHumidity_Soil_10());
	            row.put("V71102_020", microclimateData.getRelativeHumidity_Soil_20());
	            row.put("V71102_030", microclimateData.getRelativeHumidity_Soil_30());
	            row.put("V71102_040", microclimateData.getRelativeHumidity_Soil_40());
	            row.put("V71102_050", microclimateData.getRelativeHumidity_Soil_50());
	            row.put("V_BBB", "000");
	            row.put("D_SOURCE_ID", cts_code);
	            
	            StatDi di = new StatDi();					
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(sod_code);
				di.setDATA_TYPE_1(cts_code);
		
				di.setTT("农田小气候");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("0"); //0成功，1失败
				di.setPROCESS_STATE("0");  //0成功，1失败	
				di.setLATITUDE(microclimateData.getLatitude().toString());
				di.setLONGTITUDE(microclimateData.getLongitude().toString());
				di.setIIiii(microclimateData.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(microclimateData.getObservationTime(), "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				
	            try {
					OTSDbHelper.getInstance().insert(tablename, row);
					diQueues.offer(di);
				} catch (Exception e) {
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
//	        OTSBatchResult result = OTSDbHelper.getInstance().insert(tablename, batchs);
//	        System.out.println(result.getSuccessRowCount());
//	        System.out.println(result.getFailedRowCount());
//	        System.out.println(result.getFailedRows());
	        loggerBuffer.append(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
	        loggerBuffer.append(" INSERT SUCCESS COUNT : " + successRowCount + "\n");
	        loggerBuffer.append(" INSERT FAILED COUNT : " + (list.size() - successRowCount) + "\n");
	        
		}

		
		return DataBaseAction.SUCCESS;
	}

}
