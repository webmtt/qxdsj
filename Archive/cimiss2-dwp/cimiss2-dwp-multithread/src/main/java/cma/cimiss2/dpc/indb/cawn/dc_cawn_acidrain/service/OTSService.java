package cma.cimiss2.dpc.indb.cawn.dc_cawn_acidrain.service;

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

import com.alicloud.openservices.tablestore.ClientException;

import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
import cma.cimiss2.dpc.decoder.bean.cawn.AcidRainDailyData;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {
	public static BlockingQueue<StatDi> diQueues;
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static String v_bbb = "000";
	public static String acode = "16";
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	public int defaultInt = 999999;
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}

	public static DataBaseAction insert_ots(List<AcidRainDailyData> acidrain, String tablename, Date recv_time, StringBuffer loggerBuffer, String fileN, String v_tt) {
	
		if(acidrain != null && acidrain.size() > 0) {
			int successRowCount = acidrain.size();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");	
			for(int i = 0; i <acidrain.size(); i ++){
				Map<String, Object> row = new HashMap<String, Object>();
				AcidRainDailyData ar = acidrain.get(i);
				//row.put("D_RECORD_ID", sdf.format(ar.getObservationTime())+"_"+ar.getStationNumberChina() + "_" + ar.getPrecipitation_StartTime() + "_" + ar.getPrecipitation_EndTime());
	            row.put("D_DATA_ID", sod_code);
	            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_DATETIME", TimeUtil.date2String(ar.getObservationTime(),"yyyy-MM-dd HH:mm:ss"));
	            //V01301,V01300,V05001,V06001,V07001,V01405,V04001,V04002,V04003,V04307,
				row.put("V01301", ar.getStationNumberChina());//区站号(字符)
				row.put("V01300",Integer.parseInt(StationCodeUtil.stringToAscii(ar.getStationNumberChina())) );//区站号(数字)
				row.put("V05001", ar.getLatitude());//纬度
				row.put("V06001", ar.getLongitude());//经度
				row.put("V07001", ar.getHeightOfSationGroundAboveMeanSeaLevel());//测站高度
				row.put("V01405", ar.getObservationMethod());//观测方式
				row.put("V04001", ar.getObservationTime().getYear() + 1900);//资料观测年
				row.put("V04002", ar.getObservationTime().getMonth()+1);//资料观测月
				row.put("V04003", ar.getObservationTime().getDate());//资料观测日
				row.put("V04307", ar.getPrecipitation_StartTime());//降水开始时间
				//V04308,V13011,V12501_01,V15532_01_1,V15532_01_2,V15532_01_3,V15532_01_701,V13371_01_1,V13371_01_2,V13371_01_3,
				row.put("V04308", ar.getPrecipitation_EndTime());//降水结束时间
				row.put("V13011", ar.getAcidRainObservedPrecipitation());//酸雨观测降水量
				row.put("V12501_01", ar.getInitialSurveySampleTemperature());//初测时样品温度
				row.put("V15532_01_1", ar.getInitialSurvey_PH_1());//初测pH值第1次读数
				row.put("V15532_01_2", ar.getInitialSurvey_PH_2());//初测pH值第2次读数
				row.put("V15532_01_3", ar.getInitialSurvey_PH_3());//初测pH值第3次读数
				row.put("V15532_01_701", ar.getInitialSurvey_Average_PH());//初测平均pH值
				row.put("V13371_01_1", ar.getInitialSurvey_K_1());//初测K值第1次读数
				row.put("V13371_01_2", ar.getInitialSurvey_K_2());//初测K值第2次读数
				row.put("V13371_01_3", ar.getInitialSurvey_K_3());//初测K值第3次读数
				// "V13371_01_701,V12501_02,V15532_02_1,V15532_02_2,V15532_02_3,V15532_02_701,V13371_02_1,V13371_02_2,V13371_02_3,V13371_02_701
				row.put("V13371_01_701", ar.getInitialSurvey25_Average_K());//初测25℃时平均K值
				row.put("V12501_02", ar.getRetestSurveySampleTemperature());//复测时样品温度
				row.put("V15532_02_1", ar.getRetestSurvey_PH_1());//复测pH值第1次读数
				row.put("V15532_02_2", ar.getRetestSurvey_PH_2());//复测pH值第2次读数
				row.put("V15532_02_3", ar.getRetestSurvey_PH_3());//复测pH值第3次读数
				row.put("V15532_02_701", ar.getRetestSurvey_Average_PH());//复测平均pH值
				row.put("V13371_02_1", ar.getRetestSurvey_K_1());//复测K值第1次读数
				row.put("V13371_02_2", ar.getRetestSurvey_K_2());//复测K值第2次读数
				row.put("V13371_02_3", ar.getRetestSurvey_K_3());//复测K值第3次读数
				row.put("V13371_02_701", ar.getRetestSurvey25_Average_K());//复测25℃时平均K值
				//V11001_14,V11002_14,V11001_20,V11002_20,V11001_02,V11002_02,V11001_08,V11002_08,V20003_01,V20003_02,V20003_03
	    		row.put("V11001_14", ar.getWindDirection_14());//14时风向
				row.put("V11002_14", ar.getWindSpeed_14());//14时风速
				row.put("V11001_20", ar.getWindDirection_20());//20时风向
				row.put("V11002_20", ar.getWindSpeed_20());//20时风速
				row.put("V11001_02", ar.getWindDirection_02());//02时风向
				row.put("V11002_02", ar.getWindSpeed_02());//02时风速
				row.put("V11001_08", ar.getWindDirection_08());//08时风向
				row.put("V11002_08", ar.getWindSpeed_08());//08时风速
				row.put("V20003_01", ar.getPrecipitationPeriodWeatherPhenomenon_1());//降水期间天气现象1
				row.put("V20003_02", ar.getPrecipitationPeriodWeatherPhenomenon_2());//降水期间天气现象2
				row.put("V20003_03", ar.getPrecipitationPeriodWeatherPhenomenon_3());//降水期间天气现象3
				// V20003_04,V02452,V02450,V02451,V13372)
				row.put("V20003_04", ar.getPrecipitationPeriodWeatherPhenomenon_4());//降水期间天气现象4
				row.put("V02452", ar.getRepeatSurveyIndicatorCode());//复测指示码
				row.put("V02450", ar.getTemperatureCompensation_K_IndicatorCode());//K值测量是否使用温度补偿功能指示码
				row.put("V02451", ar.getDelaySampleIndicatorCode());//因故延迟样品测量指示码
				row.put("V13372", ar.getPrecipitationSampleException());//降水样品异常状况
				row.put("V_BBB", "000");
				row.put("D_SOURCE_ID", cts_code);
				
				StatDi di = new StatDi();	
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(sod_code);
				di.setDATA_TYPE_1(cts_code);
				di.setTT(v_tt);			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("0"); //0成功，1失败
				di.setPROCESS_STATE("0");  //0成功，1失败	
				di.setIIiii(ar.getStationNumberChina());
				di.setLATITUDE(String.valueOf(ar.getLatitude()));
				di.setLONGTITUDE(String.valueOf(ar.getLongitude()));
				di.setDATA_TIME(TimeUtil.date2String(ar.getObservationTime(), "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				
				try {
					OTSDbHelper.getInstance().insert(tablename, row);
					diQueues.offer(di);
				}  catch (Exception e) {
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
		    loggerBuffer.append(" INSERT FAILED COUNT : " + (acidrain.size() - successRowCount) + "\n");
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
	static void reportInfoToDb(List<ReportInfo> reportInfos, String tablename, Date recv_time, StringBuffer loggerBuffer, String v_cccc) {
		Map<String, Object> proMap = StationInfo.getProMap();
		if(reportInfos != null && reportInfos.size() > 0){
			List<Map<String, Object>> batchs = new ArrayList<Map<String, Object>>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			Date oTime = new Date();
			for(int i = 0; i < reportInfos.size(); i++){
				Map<String, Object> row = new HashMap<String, Object>();
				AgmeReportHeader agmeReportHeader = (AgmeReportHeader) reportInfos.get(i).getT();		
				oTime = agmeReportHeader.getReport_time();
				//String primkey = sdf.format(agmeReportHeader.getReport_time())+"_"+agmeReportHeader.getStationNumberChina()+"_"+v_cccc;
				//row.put("D_RECORD_ID", primkey);
	            row.put("D_DATA_ID", sod_code);
	            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_DATETIME", TimeUtil.date2String(oTime, "yyyy-MM-dd HH:mm:ss"));
	           //"V_BBB,V_CCCC,V_TT,V01301,V01300,V05001,V06001,V_NCODE,V_ACODE,"
	            row.put("V_BBB", v_bbb);
	            row.put("V_CCCC", v_cccc);
	            row.put("V_TT", "AR");
	            row.put("V01301", agmeReportHeader.getStationNumberChina());
	            row.put("V01300" ,Integer.parseInt(StationCodeUtil.stringToAscii(agmeReportHeader.getStationNumberChina())));
	            row.put("V05001", agmeReportHeader.getLatitude());
	            row.put("V06001", agmeReportHeader.getLongitude());
	            row.put("V_NCODE", 2250);//V_NCODE
	            row.put("V_ACODE", StationInfo.getAdminCode(agmeReportHeader.getStationNumberChina(), acode));// V_ACODE
	            // "V04001,V04002,V04003,V04004,V04005,"
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
