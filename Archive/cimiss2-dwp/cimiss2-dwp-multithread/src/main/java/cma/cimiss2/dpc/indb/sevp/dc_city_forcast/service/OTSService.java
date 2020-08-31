package cma.cimiss2.dpc.indb.sevp.dc_city_forcast.service;

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
import cma.cimiss2.dpc.decoder.bean.sevp.CityWeatherForeCast;
import cma.cimiss2.dpc.decoder.sevp.BullHeader;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {
	public static BlockingQueue<StatDi> diQueues;
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
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
	public static DataBaseAction processSuccessReport(List<CityWeatherForeCast> cityWeatherForeCasts, Date recv_time, String fileN, StringBuffer loggerBuffer) {
		Map<String, Object> proMap = StationInfo.getProMap();
		int successRowCount = cityWeatherForeCasts.size();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String V_TT = "";
		for(int idx = 0; idx < cityWeatherForeCasts.size(); idx ++){
			Map<String, Object> row = new HashMap<String, Object>();
			CityWeatherForeCast cityWeatherForeCast = cityWeatherForeCasts.get(idx);
			V_TT = cityWeatherForeCast.getBullHeader().getTt();
			Date dataTime = cityWeatherForeCast.getObservationTime();
			String station = cityWeatherForeCast.getStationNumberChina();
			// 设置DI信息
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
			double latitude = 999999;
			double longtitude = 999999;
			String info = (String) proMap.get(station + "+01");
			if(info == null) {
				infoLogger.error("\n In configuration file, this station does not exist: " + station);
			}
			else{
				String[] infos = info.split(",");
				if(infos[1].equals("null"))
					infoLogger.error("\n  In configuration file, longtitude is null!");
				else
					longtitude = Double.parseDouble(infos[1]);
				if(infos[2].equals("null"))
					infoLogger.error("\n  In configuration file, latitude is null!");
				else
					latitude = Double.parseDouble(infos[2]);
			}
			di.setIIiii(station);
			di.setDATA_TIME(TimeUtil.date2String(dataTime, "yyyy-MM-dd HH:mm:ss"));
			di.setPROCESS_END_TIME(TimeUtil.getSysTime());
			di.setRECORD_TIME(TimeUtil.getSysTime());	
			di.setLATITUDE(String.valueOf(latitude));
			di.setLONGTITUDE(String.valueOf(longtitude));
			try {
				boolean isRevised = false; 
				String v_bbb = cityWeatherForeCast.getCorrectSign();
				if(v_bbb.compareTo("000") > 0)
					isRevised = true;
				row.put("D_RECORD_ID", sdf.format(dataTime)+"_"+station+"_"+cityWeatherForeCast.getForecastEfficiency());
				row.put("D_DATA_ID", sod_code);
				row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
				row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
				row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
				row.put("D_DATETIME", TimeUtil.date2String(dataTime, "yyyy-MM-dd HH:mm:ss"));
				
				// "V_BBB,V01301,V01300,V04001,V04002,V04003,V04004,V04320,V20312_20_08,V20312_08_20,"
				row.put("V_BBB", cityWeatherForeCast.getCorrectSign());  // 更正报标识
				row.put("V01301", station);
				row.put("V01300", Integer.parseInt(StationCodeUtil.stringToAscii(station)));
				row.put("V04001", dataTime.getYear() + 1900); // 资料年
				row.put("V04002", dataTime.getMonth() + 1); // 资料月
				row.put("V04003", dataTime.getDate()); // 资料日
				row.put("V04004", dataTime.getHours()); //资料时
				row.put("V04320", cityWeatherForeCast.getForecastEfficiency()); 
				row.put("V20312_20_08", cityWeatherForeCast.getWeatherPhenomenon20_08());
				row.put("V20312_08_20", cityWeatherForeCast.getWeatherPhenomenon08_20());
				// + "V11303,V11313,V11301,V11302,V12021,V12022) 
				row.put("V11303", cityWeatherForeCast.getWindDir());
				row.put("V11313", cityWeatherForeCast.getWindTurnDir());
				row.put("V11301", cityWeatherForeCast.getWindLevel());
				row.put("V11302", cityWeatherForeCast.getWindTurnLevel());
				row.put("V12021", cityWeatherForeCast.getMinTemperature());
				row.put("V12022", cityWeatherForeCast.getMaxTemperature());
				row.put("D_SOURCE_ID", cts_code);
				
				if(isRevised) {
					OTSDbHelper.getInstance().update(StartConfig.valueTable(), row);
				}else {
					OTSDbHelper.getInstance().insert(StartConfig.valueTable(), row);
				}
				diQueues.offer(di);
			} catch (Exception e) {
				loggerBuffer.append(e.getMessage());
				successRowCount = successRowCount -1;	
				di.setPROCESS_STATE("1");
				diQueues.offer(di);
			}
		}
		loggerBuffer.append(" INSERT/UPDATE SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
	    loggerBuffer.append(" INSERT/UPDATE SUCCESS COUNT : " + successRowCount + "\n");
	    loggerBuffer.append(" INSERT/UPDATE FAILED COUNT : " + (cityWeatherForeCasts.size() - successRowCount) + "\n");
		return DataBaseAction.SUCCESS;
	}
	/**
	 * 
	 * @Title: reportInfoToDb   
	 * @Description:(报文信息入库)   
	 * @param  reportInfos 报文列表集合
	 * @param  connection  数据库连接
	 * @param  recv_time   资料时间
	 * @return: void      
	 * @throws:
	 */
	@SuppressWarnings({ "rawtypes" })
	public static void reportInfoToDb( List<ReportInfo> reportInfos, Date recv_time, StringBuffer loggerBuffer){
		Map<String, Object> proMap = StationInfo.getProMap();
		String tablename = StartConfig.reportTable();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String V_TT= "";
		CityWeatherForeCast cityWeatherForeCast = null;
		Date oTime = null;
		String primkey = null;
		BullHeader bullHeader;
		String sta;
		String info;
		double latitude = 999999;
		double longtitude = 999999;
		List<Map<String, Object>> batchs = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < reportInfos.size(); i++) {
			cityWeatherForeCast = (CityWeatherForeCast) reportInfos.get(i).getT();
			sta = cityWeatherForeCast.getStationNumberChina();
			bullHeader = cityWeatherForeCast.getBullHeader();
			V_TT = cityWeatherForeCast.getBullHeader().getTt();
			oTime = cityWeatherForeCast.getObservationTime();
			primkey = sdf.format(oTime)+"_"+sta+"_"+V_TT+"_"+bullHeader.getCccc()+"_"+bullHeader.getBbb();
			Map<String, Object> row = new HashMap<String, Object>();
			row.put("D_RECORD_ID", primkey);
            row.put("D_DATA_ID", sod_report_code);
            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
            row.put("D_DATETIME", TimeUtil.date2String(oTime,"yyyy-MM-dd HH:mm:ss"));
//    		"V_BBB,V_CCCC,V04001_02,V04002_02,V04003_02,V04004_02,V_TT,V_AA,V_II,V_MIMJ,"
			row.put("V_BBB", bullHeader.getBbb());
			row.put("V_CCCC", bullHeader.getCccc());  // 编报中心
			row.put("V04001_02", oTime.getYear() + 1900);
			row.put("V04002_02", oTime.getMonth() + 1);
			row.put("V04003_02", oTime.getDate());
			row.put("V04004_02", oTime.getHours());
			row.put("V_TT", V_TT);  // 报告类别
			row.put("V_AA", bullHeader.getAa());
			row.put("V_II", bullHeader.getIi());
			row.put("V_MIMJ", V_TT); // 资料类别
			//+ "V01301,V01300,V05001,V06001,V04001,V04002,V04003,V04004,V04005,"
			row.put("V01301", sta);
			row.put("V01300", Integer.parseInt(StationCodeUtil.stringToAscii(sta)));
			info = (String) proMap.get(sta + "+01");
			if(info == null) {
				infoLogger.error("\n In configuration file, this station does not exist:" + sta);
//				continue ;
			}
			else{
				String[] infos = info.split(",");						
				if(infos[1].equals("null"))
					infoLogger.error("\n In configuration file, longtitude is null!");
				else
					longtitude = Double.parseDouble(infos[1]);
				if(infos[2].equals("null"))
					infoLogger.error("\n In configuration file, latitude is null!");
				else
					latitude = Double.parseDouble(infos[2]);
			}
			row.put("V05001", latitude);
			row.put("V06001", longtitude);
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
