package cma.cimiss2.dpc.indb.radi.dc_radi_sads.service;
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

import com.alicloud.openservices.tablestore.ClientException;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
import cma.cimiss2.dpc.decoder.bean.radi.AutomaticStationRadiatingHourlyData;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;


public class OTSService {
	public static String v_tt = "SADS";
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode(); 
	public static BlockingQueue<StatDi> diQueues;
//	static Map<String, Object> proMap = StationInfo.getProMap();

	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}

	@SuppressWarnings("deprecation")
	public static DataBaseAction insert_ots(ParseResult<AutomaticStationRadiatingHourlyData> parseResult,
			Date recv_time, StringBuffer loggerBuffer, String fileN, boolean isRevised, String v_bbb) {
		Map<String, Object> proMap = StationInfo.getProMap();
		List<AutomaticStationRadiatingHourlyData> list = parseResult.getData();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		int successRowCount = list.size();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> row = new HashMap<String, Object>();
			AutomaticStationRadiatingHourlyData sads = list.get(i);
			
			// 根根配置文件  获取站点基本信息
			double height = 999999;
			String info = (String) proMap.get(sads.getStationNumberChina() + "+11");
			if(info == null) {
				info = (String) proMap.get(sads.getStationNumberChina() + "+01");
				 if (info!=null) {
					 String[] infos = info.split(",");
					 if(!(infos[3].trim().equals("null") || infos[3].trim().equals(""))){
						 height = Double.parseDouble(infos[3]);
					 }
				 }else{
				    loggerBuffer.append("\n In the configuration file, the station number does not exist" + sads.getStationNumberChina());
				 }
				
			}else{
				String[] infos = info.split(",");
				if(!(infos[3].trim().equals("null") || infos[3].trim().equals(""))){
					height = Double.parseDouble(infos[3]);
				}
			}
			String lat = String.valueOf((int)(sads.getLatitude() * 1e6));
			String lon = String.valueOf((int)(sads.getLongitude() * 1e6));
			lat = lat.replaceAll("-", "0");
			lon = lon.replaceAll("-", "0");
			row.put("D_RECORD_ID", sdf.format(sads.getObservationTime())+"_"+sads.getStationNumberChina());//记录标识
			row.put("D_DATA_ID", sod_code);//资料标识
			row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss")); // 入库时间
			row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss")); // 收到时间
			row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss")); // 更新时间
			row.put("D_DATETIME", TimeUtil.date2String(sads.getObservationTime(), "yyyy-MM-dd HH:00:00")); // 资料时间
			row.put("V_BBB", v_bbb);  // 更正报标识
			row.put("V01301", sads.getStationNumberChina());//区站号(字符)
			row.put("V01300", Integer.parseInt(StationCodeUtil.stringToAscii(sads.getStationNumberChina())));//区站号(数字)
			row.put("V05001", sads.getLatitude());//纬度
			row.put("V06001", sads.getLongitude());//经度
			row.put("V07001", height);//经度
			row.put("V04001", sads.getObservationTime().getYear() + 1900); // 资料年
			row.put("V04002", sads.getObservationTime().getMonth() + 1); // 资料月
			row.put("V04003", sads.getObservationTime().getDate()); // 资料日
			row.put("V04004", sads.getObservationTime().getHours()); // 资料时
			row.put("V04005", sads.getObservationTime().getMinutes()); //资料分
		
			row.put("V14311", sads.getTotalRadiationIrradiance());
			row.put("V14312", sads.getNetRadiationIrradiance());
			row.put("V14313", sads.getDirectRadiationIrradiance());
			row.put("V14314", sads.getScatteringRadiationIrradiance());
			row.put("V14315", sads.getReflectionRadiationIrradiance());
			row.put("V14316", sads.getUltravioletRadiationIrradiance());
			row.put("V14320", sads.getTotalRadiationExposure());
			row.put("V14311_05", sads.getRadiationIrradianceMax());
			row.put("V14021_05_052", sads.getRadiationIrradianceMaxtime());
			row.put("V14308", sads.getNetRadiationExposure());
			row.put("V14312_05", sads.getNetRadiationIrradiationMax());
			row.put("V14312_05_052", sads.getNetRadiationIrradiationMaxtime());
			row.put("V14312_06", sads.getNetRadiationIrradiationMin());
			row.put("V14312_06_052", sads.getNetRadiationIrradiationMintime());
			row.put("V14322", sads.getDirectRadiationExposure());
			row.put("V14313_05", sads.getDirectRadiationIrradianceMax());
			row.put("V14313_05_052", sads.getDirectRadiationIrradianceMaxtime());
			row.put("V14309", sads.getScatteredRadiationExposure());
			row.put("V14314_05", sads.getScatteringRadiationIrradianceMax());
			row.put("V14314_05_052", sads.getScatteringRadiationIrradianceMaxtime());
			row.put("V14306", sads.getReflectedRadiationExposure());
			row.put("V14315_05", sads.getReflectedRadiationIrradianceMax());
			row.put("V14315_05_052", sads.getReflectedRadiationIrradianceMaxtime());
			row.put("V14307", sads.getUltravioletRadiationExposure());
			row.put("V14316_05", sads.getUvIrradiationMax());
			row.put("V14316_05_052", sads.getUvIrradiationMaxtime());
			row.put("V14032", sads.getSunShineHours());
			row.put("V15483", sads.getAtmosphericTurbidityIndex());
			row.put("Q14311", sads.getQ_totalRadiationIrradiance());
			row.put("Q14312", sads.getQ_netRadiationIrradiance());
			row.put("Q14313", sads.getQ_directRadiationIrradiance());
			row.put("Q14314", sads.getQ_scatteringRadiationIrradianc());
			row.put("Q14315", sads.getQ_reflectionRadiationIrradiance());
			row.put("Q14316", sads.getQ_ultravioletRadiationIrradiance());
			row.put("Q14320", sads.getQ_totalRadiationExposure());
			row.put("Q14311_05", sads.getQ_radiationIrradianceMax());
			row.put("Q14021_05_052", sads.getQ_radiationIrradianceMaxtime());
			row.put("Q14308", sads.getQ_netRadiationExposure());
			row.put("Q14312_05", sads.getQ_netRadiationIrradiationMax());
			row.put("Q14312_05_052", sads.getQ_netRadiationIrradiationMaxtime());
			row.put("Q14312_06", sads.getQ_netRadiationIrradiationMin());
			row.put("Q14312_06_052", sads.getQ_netRadiationIrradiationMintime());
			row.put("Q14322", sads.getQ_directRadiationExposure());
			row.put("Q14313_05", sads.getQ_directRadiationIrradianceMax());
			row.put("Q14313_05_052", sads.getQ_directRadiationIrradianceMaxtime());
			row.put("Q14309", sads.getQ_scatteredRadiationExposure());
			row.put("Q14314_05", sads.getQ_scatteringRadiationIrradianceMax());
			row.put("Q14314_05_052", sads.getQ_scatteringRadiationIrradianceMaxtime());
			row.put("Q14306", sads.getQ_reflectedRadiationExposure());
			row.put("Q14315_05", sads.getQ_reflectedRadiationIrradianceMax());
			row.put("Q14315_05_052", sads.getQ_reflectedRadiationIrradianceMaxtime());
			row.put("Q14307", sads.getQ_ultravioletRadiationExposure());
			row.put("Q14316_05", sads.getQ_uvIrradiationMax());
			row.put("Q14316_05_052", sads.getQ_uvIrradiationMaxtime());
			row.put("Q14032", sads.getQ_sunShineHours());
			row.put("Q15483", sads.getQ_atmosphericTurbidityIndex());
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
			di.setIIiii(sads.getStationNumberChina());
			di.setDATA_TIME(TimeUtil.date2String(sads.getObservationTime(), "yyyy-MM-dd HH:mm:ss"));
			di.setPROCESS_END_TIME(TimeUtil.getSysTime());
			di.setRECORD_TIME(TimeUtil.getSysTime());
			di.setLONGTITUDE(String.valueOf(sads.getLongitude()));
			di.setLATITUDE(String.valueOf(sads.getLatitude()));
			
			try {
				if(isRevised) {
					OTSDbHelper.getInstance().update(StartConfig.valueTable(),row);
				}else {
					OTSDbHelper.getInstance().insert(StartConfig.valueTable(), row);
				}
				diQueues.offer(di);
			}   catch (Exception e) {
				loggerBuffer.append(e.getMessage());
				successRowCount = successRowCount -1;
				di.setPROCESS_STATE("1");
				diQueues.offer(di);
				loggerBuffer.append(row);
				if(e.getClass() == ClientException.class) {
					return DataBaseAction.CONNECTION_ERROR;
				}
			}
		}
		loggerBuffer.append(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
	    loggerBuffer.append(" INSERT SUCCESS COUNT : " + successRowCount + "\n");
	    loggerBuffer.append(" INSERT FAILED COUNT : " + (list.size() - successRowCount) + "\n");
		
		return DataBaseAction.SUCCESS;
	}

	@SuppressWarnings("deprecation")
	public static void reportInfoToDb(List<ReportInfo> reportInfos, Date recv_time, StringBuffer loggerBuffer,
			String v_bbb) {
		if(reportInfos != null && reportInfos.size() > 0){
			List<Map<String, Object>> batchs = new ArrayList<Map<String, Object>>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			Date oTime = new Date();
			for(int i = 0; i < reportInfos.size(); i++){
				Map<String, Object> row = new HashMap<String, Object>();
				AgmeReportHeader agmeReportHeader = (AgmeReportHeader) reportInfos.get(i).getT();		
				oTime = agmeReportHeader.getReport_time();
				String primkey = sdf.format(agmeReportHeader.getReport_time())+"_"+agmeReportHeader.getStationNumberChina()+"_"+"9999"+v_bbb;
				row.put("D_RECORD_ID", primkey);
	            row.put("D_DATA_ID", sod_code);
	            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_DATETIME", TimeUtil.date2String(oTime, "yyyy-MM-dd HH:00:00"));
	           //"V_BBB,V_CCCC,V_TT,V01301,V01300,V05001,V06001,V_NCODE,V_ACODE,"
	            row.put("V_BBB", v_bbb);
	            row.put("V_CCCC", "9999");
	            row.put("V_TT", v_tt);
	            row.put("V01301", agmeReportHeader.getStationNumberChina());
	            row.put("V01300" ,Integer.parseInt(StationCodeUtil.stringToAscii(agmeReportHeader.getStationNumberChina())));
	            row.put("V05001", agmeReportHeader.getLatitude());
	            row.put("V06001", agmeReportHeader.getLongitude());
	            row.put("V_NCODE", 2250);//V_NCODE
	            row.put("V_ACODE", StationInfo.getAdminCode(agmeReportHeader.getStationNumberChina(), "11"));// V_ACODE
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
			OTSBatchResult result = OTSDbHelper.getInstance().insert(StartConfig.reportTable(), batchs);
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
