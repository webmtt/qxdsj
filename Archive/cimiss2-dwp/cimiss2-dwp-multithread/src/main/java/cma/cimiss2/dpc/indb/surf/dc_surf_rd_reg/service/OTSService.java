package cma.cimiss2.dpc.indb.surf.dc_surf_rd_reg.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import cma.cimiss2.dpc.decoder.bean.surf.SurfaceRD;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static BlockingQueue<StatDi> diQueues;
	public static String fileN = null;
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code_ele = StartConfig.sodCode_mul();
	public static String sod_code_pre = StartConfig.sodCode_pre();
	public static String sod_report_code = StartConfig.reportSodCode();
	public static String report_type = "RD";
//	static Map<String, Object> proMap = StationInfo.getProMap();
	
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}
	@SuppressWarnings("deprecation")
	public static DataBaseAction processSuccessReport(List<SurfaceRD> surfaceRDs, Date recv_time, String fileN, StringBuffer loggerBuffer) {
		Map<String, Object> proMap = StationInfo.getProMap();
		int successRowCount = surfaceRDs.size();
		int countPre = 0;
		int sucessPre = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		int stationNumberN = 999999;
		List<Map<String, Object>> batchs = new ArrayList<>();
		for(int idx = 0; idx < surfaceRDs.size(); idx ++){
			SurfaceRD surfaceRD = surfaceRDs.get(idx);
			// 非更正报；或者为更正报，但是数据库中尚未有这一条数据
			StatDi di = new StatDi();	
			di.setFILE_NAME_O(fileN);
			di.setDATA_TYPE(sod_code_ele);
			di.setDATA_TYPE_1(cts_code);
			di.setTT(report_type);			
			di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
			di.setPROCESS_START_TIME(TimeUtil.getSysTime());
			di.setFILE_NAME_N(fileN);
			di.setBUSINESS_STATE("0"); //0成功，1失败
			di.setPROCESS_STATE("0");  //0成功，1失败	
			String stat = surfaceRD.getStationNumberChina();
			if (Pattern.matches("\\d{5}", stat))
				stationNumberN = Integer.parseInt(stat);					
			else
				stationNumberN = Integer.parseInt(StationCodeUtil.stringToAscii(stat));
			String info = (String) proMap.get(stat + "+01");
			int adminCode = 999999;
			int stationLevel = 999999;
			int stationType = 40;
			if(info == null) {
				loggerBuffer.append("\n In configuration file, this station does not exist: " + stat);
			}else{
				String[] infos = info.split(",");
				if(!infos[5].equals("null"))
					adminCode = Integer.parseInt(infos[5]);
				if(!infos[6].equals("null"))
					stationLevel = Integer.parseInt(infos[6]); 
			}
			boolean isRevised = false; 
				String v_bbb = surfaceRD.getFileRevisionSign();
			Map<String, Object> row = new HashMap<String, Object>();
			if(v_bbb.compareTo("000") > 0)
				isRevised = true;
			try {
				row.put("D_RECORD_ID", sdf.format(surfaceRDs.get(idx).getObservationTime())+"_"+surfaceRDs.get(idx).getStationNumberChina()+"_"+(int)(surfaceRDs.get(idx).getLatitude()*1e6)+"_"+(int)(surfaceRDs.get(idx).getLongitude()*1e6));
				row.put("D_DATA_ID", sod_code_ele);
				row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
				row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
				row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
				row.put("D_DATETIME", TimeUtil.date2String(surfaceRD.getObservationTime(), "yyyy-MM-dd HH:mm:ss"));
//				V_SQCODE,V_PQCODE,V_NQCODE,V01301,V01300,V05001,V06001,V07001,V02001,V02301,"
				row.put("V_SQCODE", Character.getNumericValue(surfaceRD.getQualityControl().charAt(0)));
				row.put("V_PQCODE", Character.getNumericValue(surfaceRD.getQualityControl().charAt(1)));
				row.put("V_NQCODE", Character.getNumericValue(surfaceRD.getQualityControl().charAt(2)));
				row.put("V01301", surfaceRD.getStationNumberChina());
				row.put("V01300", stationNumberN);
				row.put("V05001", surfaceRD.getLatitude());
				row.put("V06001", surfaceRD.getLongitude());
				row.put("V07001", surfaceRD.getHeightOfSationGroundAboveMeanSeaLevel());
				//测站类型
				row.put("V02001", stationType); 
				// 台站级别
				row.put("V02301", stationLevel); 
//				+ "V_ACODE,V04001,V04002,V04003,V04004,V04005,V04006,V12001,V12011,V12011_052,"
				row.put("V_ACODE", adminCode);
				row.put("V04001", surfaceRD.getObservationTime().getYear() + 1900);
				row.put("V04002", surfaceRD.getObservationTime().getMonth() + 1);
				row.put("V04003", surfaceRD.getObservationTime().getDate());
				row.put("V04004", surfaceRD.getObservationTime().getHours());
				row.put("V04005", surfaceRD.getObservationTime().getMinutes());
				row.put("V04006", surfaceRD.getObservationTime().getSeconds());
				row.put("V12001", surfaceRD.getAirTemperature().getValue());
				row.put("V12011", surfaceRD.getMaxAirTemperature().getValue());
				row.put("V12011_052", surfaceRD.getTimeOfMaxAirTemperature().getValue());
//				+ "V12012,V12012_052,V12003,V13003,V13007,V13007_052,V13004,V13019,V11290,V11291,"
				row.put("V12012", surfaceRD.getMinAirTemperature().getValue());
				row.put("V12012_052", surfaceRD.getTimeOfMinAirTemperature().getValue());
				row.put("V12003", surfaceRD.getDewpointTemperature().getValue());
				row.put("V13003", surfaceRD.getRelativeHumidity().getValue());
				row.put("V13007", surfaceRD.getMinRelativeHumidity().getValue());
				row.put("V13007_052", surfaceRD.getTimeOfMinRelativeHumidity().getValue());
				row.put("V13004", surfaceRD.getWaterVaporPressure().getValue());
				row.put("V13019", surfaceRD.getHourlyCumulativePrecipitation().getValue());
				row.put("V11290", surfaceRD.getTwoMinWindDirection().getValue());
				row.put("V11291", surfaceRD.getTwoMinWindAvgSpeed().getValue());
//				+ "V11292,V11293,V11296,V11042,V11042_052,V11001,V11002,V11211,V11046,V11046_052,"
				row.put("V11292", surfaceRD.getTenMinWindDirection().getValue());
				row.put("V11293", surfaceRD.getTenMinWindAvgSpeed().getValue());
				row.put("V11296", surfaceRD.getDirectionOfMaxWindSpeed().getValue());
				row.put("V11042", surfaceRD.getMaxWindSpeed().getValue());
				row.put("V11042_052", surfaceRD.getTimeOfMaxWindSpeed().getValue());
				row.put("V11001", surfaceRD.getInstantaneousWindDirection().getValue());
				row.put("V11002", surfaceRD.getInstantaneousWindSpeed().getValue());
				row.put("V11211", surfaceRD.getDirectionOfExtremWindSpeed().getValue());
				row.put("V11046", surfaceRD.getExtremWindSpeed().getValue());
				row.put("V11046_052", surfaceRD.getTimeOfExtremWindSpeed().getValue());
//				+ "V11211_001,V11046_001,V12421,V12422,V12422_052,V12423,V12423_052,V12424,V20001_701_01,V20059,"
				row.put("V11211_001", surfaceRD.getDirectionOfExtremWindSpeedMinutely().getValue());
				row.put("V11046_001", surfaceRD.getExtremeWindSpeedMinutely().getValue());
				row.put("V12421", surfaceRD.getRoadSurfTemperature().getValue());
				row.put("V12422", surfaceRD.getMaxRoadSurfTemperature().getValue());
				row.put("V12422_052", surfaceRD.getTimeOfMaxRoadSurfTemperature().getValue());
				row.put("V12423", surfaceRD.getMinRoadSurfTemperature().getValue());
				row.put("V12423_052", surfaceRD.getTimeOfMinRoadSurfTemperature().getValue());
				row.put("V12424", surfaceRD.getRoadBaseTemperature().getValue());
				row.put("V20001_701_01", surfaceRD.getOneMinAvgVisibility().getValue());
				row.put("V20059", surfaceRD.getMinVisibility().getValue());
//				+ "V20059_052,V20062_1,V20062_2,V20062_3,V13013,V13368,V13369,V12425,V15505,V20003_01,"
				row.put("V20059_052", surfaceRD.getTimeOfMinVisibility().getValue());
				row.put("V20062_1", surfaceRD.getRoadSurfCondition().get(0).getValue());
				row.put("V20062_2", surfaceRD.getRoadSurfCondition().get(1).getValue());
				row.put("V20062_3", surfaceRD.getRoadSurfCondition().get(2).getValue());
				if(surfaceRD.getSnowThickness().getValue() != 999999.0 || surfaceRD.getSnowThickness().getValue() !=999998.0) {
					row.put("V13013", surfaceRD.getSnowThickness().getValue()*0.1);
				}
				
				row.put("V13368", surfaceRD.getWaterThickness().getValue());
				row.put("V13369", surfaceRD.getIceThickness().getValue());
				row.put("V12425", surfaceRD.getFreezingPointTemperature().getValue());
				row.put("V15505", surfaceRD.getConcentrationOfSnowMeltAgent().getValue());
				row.put("V20003_01", surfaceRD.getWeatherPhenomenonCode().get(0).getValue());
//				+ "V20003_02,V20003_03,V20003_04,V20003_05,V20003_06,Q12001,Q12011,Q12011_052,Q12012,Q12012_052,"
				row.put("V20003_02", surfaceRD.getWeatherPhenomenonCode().get(1).getValue());
				row.put("V20003_03", surfaceRD.getWeatherPhenomenonCode().get(2).getValue());
				row.put("V20003_04", surfaceRD.getWeatherPhenomenonCode().get(3).getValue());
				row.put("V20003_05", surfaceRD.getWeatherPhenomenonCode().get(4).getValue());
				row.put("V20003_06", surfaceRD.getWeatherPhenomenonCode().get(5).getValue());
				row.put("Q12001", surfaceRD.getAirTemperature().getQuality().get(1).getCode());
				row.put("Q12011", surfaceRD.getMaxAirTemperature().getQuality().get(1).getCode());
				row.put("Q12011_052", surfaceRD.getTimeOfMaxAirTemperature().getQuality().get(1).getCode());
				row.put("Q12012", surfaceRD.getMinAirTemperature().getQuality().get(1).getCode());
				row.put("Q12012_052", surfaceRD.getTimeOfMinAirTemperature().getQuality().get(1).getCode());
//				+ "Q12003,Q13003,Q13007,Q13007_052,Q13004,Q13019,Q11290,Q11291,Q11292,Q11293,"
				row.put("Q12003", surfaceRD.getDewpointTemperature().getQuality().get(1).getCode());
				row.put("Q13003", surfaceRD.getRelativeHumidity().getQuality().get(1).getCode());
				row.put("Q13007", surfaceRD.getMinRelativeHumidity().getQuality().get(1).getCode());
				row.put("Q13007_052", surfaceRD.getTimeOfMinRelativeHumidity().getQuality().get(1).getCode());
				row.put("Q13004", surfaceRD.getWaterVaporPressure().getQuality().get(1).getCode());
				row.put("Q13019", surfaceRD.getHourlyCumulativePrecipitation().getQuality().get(1).getCode());
				row.put("Q11290", surfaceRD.getTwoMinWindDirection().getQuality().get(1).getCode());
				row.put("Q11291", surfaceRD.getTwoMinWindAvgSpeed().getQuality().get(1).getCode());
				row.put("Q11292", surfaceRD.getTenMinWindDirection().getQuality().get(1).getCode());
				row.put("Q11293", surfaceRD.getTenMinWindAvgSpeed().getQuality().get(1).getCode());
//				+ "Q11296,Q11042,Q11042_052,Q11001,Q11002,Q11211,Q11046,Q11046_052,Q11211_001,Q11046_001,"
				row.put("Q11296", surfaceRD.getDirectionOfMaxWindSpeed().getQuality().get(1).getCode());
				row.put("Q11042", surfaceRD.getMaxWindSpeed().getQuality().get(1).getCode());
				row.put("Q11042_052", surfaceRD.getTimeOfMaxWindSpeed().getQuality().get(1).getCode());
				row.put("Q11001", surfaceRD.getInstantaneousWindDirection().getQuality().get(1).getCode());
				row.put("Q11002", surfaceRD.getInstantaneousWindSpeed().getQuality().get(1).getCode());
				row.put("Q11211", surfaceRD.getDirectionOfExtremWindSpeed().getQuality().get(1).getCode());
				row.put("Q11046", surfaceRD.getExtremWindSpeed().getQuality().get(1).getCode());
				row.put("Q11046_052", surfaceRD.getTimeOfExtremWindSpeed().getQuality().get(1).getCode());
				row.put("Q11211_001", surfaceRD.getDirectionOfExtremWindSpeedMinutely().getQuality().get(1).getCode());
				row.put("Q11046_001", surfaceRD.getExtremeWindSpeedMinutely().getQuality().get(1).getCode());
//				+ "Q12421,Q12422,Q12422_052,Q12423,Q12423_052,Q12424,Q20001_701_01,Q20059,Q20059_052,Q20062_1,"
				row.put("Q12421", surfaceRD.getRoadSurfTemperature().getQuality().get(1).getCode());
				row.put("Q12422", surfaceRD.getMaxRoadSurfTemperature().getQuality().get(1).getCode());
				row.put("Q12422_052", surfaceRD.getTimeOfMaxRoadSurfTemperature().getQuality().get(1).getCode());
				row.put("Q12423", surfaceRD.getMinRoadSurfTemperature().getQuality().get(1).getCode());
				row.put("Q12423_052", surfaceRD.getTimeOfMinRoadSurfTemperature().getQuality().get(1).getCode());
				row.put("Q12424", surfaceRD.getRoadBaseTemperature().getQuality().get(1).getCode());
				row.put("Q20001_701_01", surfaceRD.getOneMinAvgVisibility().getQuality().get(1).getCode());
				row.put("Q20059", surfaceRD.getMinVisibility().getQuality().get(1).getCode());
				row.put("Q20059_052", surfaceRD.getTimeOfMinVisibility().getQuality().get(1).getCode());
				row.put("Q20062_1", surfaceRD.getRoadSurfCondition().get(0).getQuality().get(1).getCode());
//				+ "Q20062_2,Q20062_3,Q13013,Q13368,Q13369,Q12425,Q15505,Q20003_01,Q20003_02,Q20003_03,"
				row.put("Q20062_2", surfaceRD.getRoadSurfCondition().get(1).getQuality().get(1).getCode());
				row.put("Q20062_3", surfaceRD.getRoadSurfCondition().get(2).getQuality().get(1).getCode());
				row.put("Q13013", surfaceRD.getSnowThickness().getQuality().get(1).getCode());
				row.put("Q13368", surfaceRD.getWaterThickness().getQuality().get(1).getCode());
				row.put("Q13369", surfaceRD.getIceThickness().getQuality().get(1).getCode());
				row.put("Q12425", surfaceRD.getFreezingPointTemperature().getQuality().get(1).getCode());
				row.put("Q15505", surfaceRD.getConcentrationOfSnowMeltAgent().getQuality().get(1).getCode());
				row.put("Q20003_01", surfaceRD.getWeatherPhenomenonCode().get(0).getQuality().get(1).getCode());
				row.put("Q20003_02", surfaceRD.getWeatherPhenomenonCode().get(1).getQuality().get(1).getCode());
				row.put("Q20003_03", surfaceRD.getWeatherPhenomenonCode().get(2).getQuality().get(1).getCode());
//				+ "Q20003_04,Q20003_05,Q20003_06,v_bbb	
				row.put("Q20003_04", surfaceRD.getWeatherPhenomenonCode().get(3).getQuality().get(1).getCode());
				row.put("Q20003_05", surfaceRD.getWeatherPhenomenonCode().get(4).getQuality().get(1).getCode());
				row.put("Q20003_06", surfaceRD.getWeatherPhenomenonCode().get(5).getQuality().get(1).getCode());
				row.put("v_bbb", surfaceRD.getFileRevisionSign());
				row.put("D_SOURCE_ID", cts_code);
				
				batchs.add(row);
				di.setIIiii(surfaceRD.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(surfaceRD.getObservationTime(), "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				di.setLATITUDE(surfaceRD.getLatitude().toString());
				di.setLONGTITUDE(surfaceRD.getLongitude().toString());
				
				if(isRevised) {
					OTSDbHelper.getInstance().update(StartConfig.keyTable(), row);
				}else {
					@SuppressWarnings("unused")
					OTSBatchResult result = OTSDbHelper.getInstance().insert(StartConfig.keyTable(), batchs);
					
				}
				
				diQueues.offer(di);
			}catch (Exception e) {
				loggerBuffer.append(e.getMessage());
				successRowCount = successRowCount -1;	
				di.setPROCESS_STATE("1");
				diQueues.offer(di);
				if(e.getClass() == ClientException.class) {
					return DataBaseAction.CONNECTION_ERROR;
				}
				continue;
			}
			// 如果是整点时间，则降水的120Bytes为前一小时60个分钟的降水量，如果不是整点时间，则降水为当前小时、前面几十分钟的降水
			Date dt = surfaceRD.getObservationTime();
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt);
			int MinsLen = 0;

			MinsLen = dt.getMinutes();
			if(MinsLen == 0){
				MinsLen = 60;
				cal.add(Calendar.HOUR_OF_DAY, -1);
			}
			else{
				// 不是整点的降水数据，不入库
				continue;
			}
			countPre += MinsLen;
			dt = cal.getTime();
			List<Map<String, Object>> bMaps = new ArrayList<>();
			for(int i = 0; i < MinsLen; i ++){ // 小时内分钟降水,2个Byte为一个降水
				StatDi diP = new StatDi();		
				try{
					Map<String, Object> rowPre = new HashMap<String, Object>();
					diP.setFILE_NAME_O(fileN);
					diP.setDATA_TYPE(sod_code_pre);
					diP.setDATA_TYPE_1(cts_code);
					diP.setTT(report_type);			
					diP.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					diP.setPROCESS_START_TIME(TimeUtil.getSysTime());
					diP.setFILE_NAME_N(fileN);
					diP.setBUSINESS_STATE("0"); //0成功，1失败
					diP.setPROCESS_STATE("0");  //0成功，1失败
					dt.setMinutes(i);
					rowPre.put("D_RECORD_ID", sdf.format(dt)+"_"+surfaceRD.getStationNumberChina()+"_"+(int)(surfaceRDs.get(idx).getLatitude()*1e6)+"_"+(int)(surfaceRDs.get(idx).getLongitude()*1e6));
					rowPre.put("D_DATA_ID", sod_code_pre);
					rowPre.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					rowPre.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					rowPre.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					rowPre.put("D_DATETIME", TimeUtil.date2String(dt, "yyyy-MM-dd HH:mm:ss"));
	//				V_SQCODE,V_PQCODE,V_NQCODE,V01301,V01300,V05001,V06001,V07001,V02001,V02301,"
					rowPre.put("V_SQCODE", Character.getNumericValue(surfaceRD.getQualityControl().charAt(0)));
					rowPre.put("V_PQCODE", Character.getNumericValue(surfaceRD.getQualityControl().charAt(1)));
					rowPre.put("V_NQCODE", Character.getNumericValue(surfaceRD.getQualityControl().charAt(2)));
					rowPre.put("V01301", surfaceRD.getStationNumberChina());
					rowPre.put("V01300", stationNumberN);
					rowPre.put("V05001", surfaceRD.getLatitude());
					rowPre.put("V06001", surfaceRD.getLongitude());
					rowPre.put("V07001", surfaceRD.getHeightOfSationGroundAboveMeanSeaLevel());
					//测站类型
					rowPre.put("V02001", stationType); 
					// 台站级别
					rowPre.put("V02301", stationLevel); 
	//				+ "V_ACODE,V04001,V04002,V04003,V04004,V04005,V04006,"
					rowPre.put("V_ACODE", adminCode);
					rowPre.put("V04001", dt.getYear() + 1900);
					rowPre.put("V04002", dt.getMonth() + 1);
					rowPre.put("V04003", dt.getDate());
					rowPre.put("V04004", dt.getHours());
					rowPre.put("V04005", dt.getMinutes());
					rowPre.put("V04006", dt.getSeconds());
	//				+ "V13019,V13011,Q13019,Q13011,V_BBB
					rowPre.put("V13019", surfaceRD.getHourlyCumulativePrecipitation().getValue());
					rowPre.put("V13011", surfaceRD.getMinutelyPrecipitation().get(i).getValue());
					if(surfaceRD.getQualityControl().substring(1, 2).equals("9")){
						rowPre.put("Q13019", surfaceRD.getHourlyCumulativePrecipitation().getQuality().get(0).getCode());
						rowPre.put("Q13011", surfaceRD.getMinutelyPrecipitation().get(i).getQuality().get(0).getCode());
					}
					else{
						rowPre.put("Q13019", surfaceRD.getHourlyCumulativePrecipitation().getQuality().get(1).getCode());
						rowPre.put("Q13011", surfaceRD.getMinutelyPrecipitation().get(i).getQuality().get(1).getCode());
					}
					rowPre.put("V_BBB", surfaceRD.getFileRevisionSign());
					rowPre.put("D_SOURCE_ID", cts_code);
					
					diP.setIIiii(surfaceRD.getStationNumberChina());
					diP.setDATA_TIME(TimeUtil.date2String(dt, "yyyy-MM-dd HH:mm:ss"));
					diP.setPROCESS_END_TIME(TimeUtil.getSysTime());
					diP.setRECORD_TIME(TimeUtil.getSysTime());	
					diP.setLATITUDE(surfaceRD.getLatitude().toString());
					diP.setLONGTITUDE(surfaceRD.getLongitude().toString());
					if(isRevised) {
						OTSDbHelper.getInstance().update(StartConfig.valueTable(), rowPre);
					}else {
						bMaps.add(rowPre);
					}
					if(bMaps.size() > 0) {
						OTSDbHelper.getInstance().insert(StartConfig.valueTable(), bMaps, false);
					}
					sucessPre ++;
					diQueues.offer(diP);
					
				}catch (Exception e) {
					loggerBuffer.append(e.getMessage());
					di.setPROCESS_STATE("1");
					diQueues.offer(diP);
					if(e.getClass() == ClientException.class) {
						return DataBaseAction.CONNECTION_ERROR;
					}
					continue;
				}
			}// end for
		}// end for
		loggerBuffer.append(" INSERT/UPDATE SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
		loggerBuffer.append(" INSERT/UPDATE SUCCESS COUNT ("+StartConfig.keyTable()+"): " + successRowCount + "\n");
		loggerBuffer.append(" INSERT/UPDATE FAILED COUNT ("+StartConfig.keyTable()+"): " + (surfaceRDs.size() - successRowCount) + "\n");
		
		loggerBuffer.append(" INSERT/UPDATE SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
		loggerBuffer.append(" INSERT/UPDATE SUCCESS COUNT ("+StartConfig.valueTable()+"): " + sucessPre + "\n");
		loggerBuffer.append(" INSERT/UPDATE FAILED COUNT ("+StartConfig.valueTable()+"): " + (countPre - sucessPre) + "\n");
		
		
		return DataBaseAction.SUCCESS;
	}
	/**
	 * 
	 * @Title: reportInfoToDb   
	 * @Description: TODO(这里用一句话描述这个方法的作用)   
	 * @param reportInfos
	 * @param recv_time
	 * @param v_cccc
	 * @param v_tt
	 * @param loggerBuffer void      
	 * @throws：
	 */
	@SuppressWarnings("deprecation")
	public static void reportInfoToDb(@SuppressWarnings("rawtypes") List<ReportInfo> reportInfos, Date recv_time, String v_cccc, String v_tt, StringBuffer loggerBuffer) {
		Map<String, Object> proMap = StationInfo.getProMap();
		loggerBuffer.append("start process reports ......");
		String tablename = StartConfig.reportTable();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		List<Map<String, Object>> batchs = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < reportInfos.size(); i++) {
			SurfaceRD surfaceRD = (SurfaceRD) reportInfos.get(i).getT();
			Map<String, Object> row = new HashMap<String, Object>();
			String stat = surfaceRD.getStationNumberChina();
			String info = (String) proMap.get(stat + "+01");
			int adminCode = 999999;
			if(info == null) {
				loggerBuffer.append("\n In configuration file, this station does not exist: " + stat);
			//	continue ;
			}
			else{
				String[] infos = info.split(",");
				adminCode = Integer.parseInt(infos[5]);
			}
			String primkey = sdf.format(surfaceRD.getObservationTime())+"_"+stat+"_"+v_tt+"_"+ surfaceRD.getFileRevisionSign();
			row.put("D_RECORD_ID", primkey);
            row.put("D_DATA_ID", sod_report_code);
            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
            row.put("D_DATETIME", TimeUtil.date2String(surfaceRD.getObservationTime(),"yyyy-MM-dd HH:mm:ss"));
//            V_BBB,V_CCCC,V_TT,V01301,V01300,V05001,V06001,V_NCODE,V_ACODE,V04001"
            row.put("V_BBB", surfaceRD.getFileRevisionSign());
			row.put("V_CCCC", v_cccc);
			row.put("V_TT", v_tt);
			row.put("V01301", surfaceRD.getStationNumberChina());
			row.put("V01300", Integer.parseInt(StationCodeUtil.stringToAscii(stat)));
			row.put("V05001", surfaceRD.getLatitude());
			row.put("V06001", surfaceRD.getLongitude());
			row.put("V_NCODE", 2250); // V_NCODE
			row.put("V_ACODE", adminCode); // V_ACODE
			row.put("V04001", surfaceRD.getObservationTime().getYear() + 1900);
//			+ V04002,V04003,V04004,V04005,V_LEN,V_REPORT
			row.put("V04002", surfaceRD.getObservationTime().getMonth() + 1);
			row.put("V04003", surfaceRD.getObservationTime().getDate());
			row.put("V04004", surfaceRD.getObservationTime().getHours());
			row.put("V04005", surfaceRD.getObservationTime().getMinutes());
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