package cma.cimiss2.dpc.indb.sevp.dc_fine_forcast.service;

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
import cma.cimiss2.dpc.decoder.bean.sevp.FineForecast;
import cma.cimiss2.dpc.decoder.bean.sevp.FineList;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static BlockingQueue<StatDi> diQueues;
	public static String sod_code = "";
//	static Map<String, Object> proMap = StationInfo.getProMap();	
	
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}
	public static DataBaseAction processSuccessReport(List<FineForecast> list_normal, Date recv_time, String cts_code, String fileN, StringBuffer loggerBuffer,String forecast_max, String interval_max) {
		Map<String, Object> proMap = StationInfo.getProMap();
		int successRowCount = list_normal.size();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		ArrayList<CTSCodeMap> ctsCodeMaps = new ArrayList<>();
		String ctSs[] = StartConfig.ctsCodes();
		String sodSs[] = StartConfig.sodCodes();
		String reportSods[] = StartConfig.reportSodCodes();
		for(int i = 0; i < ctSs.length; i ++){
			CTSCodeMap codeMap = new CTSCodeMap();
			codeMap.setCts_code(ctSs[i]);
			codeMap.setSod_code(sodSs[i]);
			codeMap.setReport_sod_code(reportSods[i]);
			ctsCodeMaps.add(codeMap);
		}
		sod_code = CTSCodeMap.findSodByCTS(cts_code, ctsCodeMaps);
		for (int i = 0; i < list_normal.size(); i++) {
			FineForecast fine = list_normal.get(i);
			// 设置DI信息
			StatDi di = new StatDi();					
			di.setFILE_NAME_O(fileN);
			di.setDATA_TYPE(sod_code);
			di.setDATA_TYPE_1(cts_code);
			di.setTT("FS");			
			di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
			di.setPROCESS_START_TIME(TimeUtil.getSysTime());
			di.setFILE_NAME_N(fileN);
			di.setBUSINESS_STATE("0"); //0成功，1失败
			di.setPROCESS_STATE("0");  //0成功，1失败	
			di.setIIiii(fine.getStationNumberChina());
			di.setDATA_TIME(TimeUtil.date2String(fine.getObservationTime(), "yyyy-MM-dd HH:mm:ss"));
			di.setPROCESS_END_TIME(TimeUtil.getSysTime());
			di.setRECORD_TIME(TimeUtil.getSysTime());	
			di.setLATITUDE(String.valueOf(fine.getLatitude()));
			di.setLONGTITUDE(String.valueOf(fine.getLongitude()));
			// 根根配置文件  获取站点基本信息
			String info = (String) proMap.get(fine.getStationNumberChina() + "+01");
			int adminCode = 999999;
			if(info == null) {
				loggerBuffer.append("\n In configuration file, this station does not exist: " + fine.getStationNumberChina());			
			}else{			
				String[] infos = info.split(",");
				if(!(infos[5].trim().equals("null") || infos[5].trim().equals(""))){
					adminCode = Integer.parseInt(infos[5]);
				}
			}		
			String primkey = sdf.format(fine.getForecastTime()) + "_"+ sdf.format(fine.getObservationTime()) + "_" + fine.getStationNumberChina()+"_"+fine.getProd_Code();
			List<FineList> fine_list = fine.getFine();
			
			boolean isRevised = false; 
			String v_bbb = fine.getCorrectSign();
			if(v_bbb.compareTo("000") > 0)
				isRevised = true;
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			for (int j = 0; j < fine_list.size(); j++) {
				FineList fine_forecast = fine_list.get(j);
				String primkeyEle =sdf.format(fine.getForecastTime()) + "_"+ sdf.format(fine.getObservationTime()) + "_" + fine.getStationNumberChina()+"_"+fine.getProd_Code()+"_"+fine_forecast.getValidtime();
				Map<String, Object> row = new HashMap<String, Object>();
				row.put("D_ELE_ID", primkeyEle);
				row.put("D_RECORD_ID", primkey);
				row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
				row.put("D_DATETIME", TimeUtil.date2String(fine.getForecastTime(),"yyyy-MM-dd HH:mm:ss"));
				//V01301,V01300,V04320,V12001,V13003,V11001,V11002,V10004,V13016,V20010,
				row.put("V01301", fine.getStationNumberChina());//站号
				row.put("V01300",Integer.parseInt(StationCodeUtil.stringToAscii(fine.getStationNumberChina())));//区站号(数字)
				row.put("V04320", fine_forecast.getValidtime());//预报时效
				row.put("V12001", fine_forecast.getTEM());//温度/气温
				row.put("V13003", fine_forecast.getRHU());//相对湿度
				row.put("V11001", fine_forecast.getWIN_D());//风向
				row.put("V11002", fine_forecast.getWIN_S());//风速
				if(fine_forecast.getPRS().doubleValue() == 999999.0 || fine_forecast.getPRS().doubleValue() == 999998.0)
					row.put("V10004", fine_forecast.getPRS());
				else
					row.put("V10004", fine_forecast.getPRS() + 1000);//气压
				row.put("V13016", fine_forecast.getPRE_PRE_Fore());//可降水分（预报降水量）
				row.put("V20010", fine_forecast.getCLO_Cov());//总云量
				//V20051,V20312,V20001,V12016,V12017,V13008_24,V13007_24,V13023,V13022,V20010_12,"
				row.put("V20051", fine_forecast.getCLO_Cov_Low());//低云量
				row.put("V20312", fine_forecast.getWEP());//天气现象
				row.put("V20001", fine_forecast.getVIS());//水平能见度(人工)
				row.put("V12016", fine_forecast.getTEM_Max_24h());//未来24小时最高气温
				row.put("V12017", fine_forecast.getTEM_Min_24h());//未来24小时最低气温
				row.put("V13008_24", fine_forecast.getRHU_Max_24h());//24小时最大相对湿度
				row.put("V13007_24", fine_forecast.getRHU_Min_24h());//24小时最小相对湿度
				row.put("V13023", fine_forecast.getPRE_24h());//未来24小时降水量
				row.put("V13022", fine_forecast.getPRE_12h());//未来12小时降水量
				row.put("V20010_12", fine_forecast.getCLO_Cov_12h());//12小时内总云量
				//"V20051_12,V20312_12,V11001_12,V11040_12,V_BBB
				row.put("V20051_12", fine_forecast.getCLO_Cov_Low_12h());//12小时内低云量
				row.put("V20312_12", fine_forecast.getWEP_Past_12h());//12小时内天气现象
				row.put("V11001_12", fine_forecast.getWIN_PD_12h());//12小时内盛行风向
				row.put("V11040_12", fine_forecast.getWIN_S_Max_12h());//12小时内最大风速
				row.put("V_BBB", fine.getCorrectSign());//更正标志
				
				row.put("D_SOURCE_ID", cts_code);
				
				if (isRevised) {
					try {
						OTSDbHelper.getInstance().update(StartConfig.valueTable(), "D_ELE_ID", row);
					} catch (Exception e) {
						loggerBuffer.append(e.getMessage());
						e.printStackTrace();
						if(e.getClass() == ClientException.class) {
							return DataBaseAction.CONNECTION_ERROR;
						}else {
							continue;
						}
					}
				}else {
					list.add(row);
				}
			}
				
			if(list.size() > 0){
				OTSDbHelper.getInstance().insert(StartConfig.valueTable(),"D_ELE_ID", list);
				list.clear();
			}
				
			Map<String, Object> rowk = new HashMap<String, Object>();
				
				
			if(isRevised) {
				rowk.put("D_RECORD_ID", primkey);
				rowk.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
				rowk.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
				rowk.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
				rowk.put("V_BBB", fine.getCorrectSign());//更正标识
									
				try {
					OTSDbHelper.getInstance().update(StartConfig.keyTable(), rowk, false);
				} catch (Exception e) {
					loggerBuffer.append(e.getMessage());
					e.printStackTrace();
					if(e.getClass() == ClientException.class) {
						return DataBaseAction.CONNECTION_ERROR;
					}else {
						rowk.put("D_RECORD_ID", primkey);
						rowk.put("D_DATA_ID", sod_code);
						rowk.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
						rowk.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
						rowk.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
						rowk.put("D_DATETIME", TimeUtil.date2String(fine.getForecastTime(), "yyyy-MM-dd HH:mm:ss"));
						//V_BBB,V01301,V01300,V05001,V06001,V07001,V_ACODE,V04001_02,V04002_02,V04003_02,"
						rowk.put("V_BBB", fine.getCorrectSign());//更正标识
						rowk.put("V01301", fine.getStationNumberChina());//区站号(字符)
						rowk.put("V01300",Integer.parseInt(StationCodeUtil.stringToAscii(fine.getStationNumberChina())) );//区站号(数字)
						rowk.put("V05001", fine.getLatitude());//纬度
						rowk.put("V06001", fine.getLongitude());//经度
						rowk.put("V07001", fine.getHeightOfSationGroundAboveMeanSeaLevel());//测站海拔高度
						rowk.put("V_ACODE", adminCode);//中国行政区代码
						rowk.put("V04001_02", fine.getForecastTime().getYear() + 1900);//预报年
						rowk.put("V04002_02", fine.getForecastTime().getMonth()+1);//预报月
						rowk.put("V04003_02", fine.getForecastTime().getDate());//预报日
						// "V04004_02,V04001,V04002,V04003,V04004,V04005,V_CCCC,V_PROD_DESC,V_PROD_CODE,V04320_005,
						rowk.put("V04004_02", fine.getForecastTime().getHours());//预报时
						rowk.put("V04001", fine.getObservationTime().getYear() + 1900);//预报产生年
						rowk.put("V04002", fine.getObservationTime().getMonth()+1);//预报产生月
						rowk.put("V04003", fine.getObservationTime().getDate());//预报产生日
						rowk.put("V04004", fine.getObservationTime().getHours());//预报产生时
						rowk.put("V04005", fine.getObservationTime().getMinutes());//预报产生分
						rowk.put("V_CCCC", fine.getBul_Center());//编报(加工)中心
						rowk.put("V_PROD_DESC", fine.getPROD_DESC());//产品描述
						rowk.put("V_PROD_CODE", fine.getProd_Code());//产品代码
						rowk.put("V04320_005", forecast_max);//最大预报时效
						//V04320_005_080,V04320_041,V_PROD_NUM
						rowk.put("V04320_005_080", interval_max);//最大时效间隔
						rowk.put("V04320_041", fine.getValidtime_Count());//预报时效个数
						rowk.put("V_PROD_NUM", fine.getV_PROD_NUM());//预报产品个数
						rowk.put("D_SOURCE_ID", cts_code);
						
						try {
							OTSDbHelper.getInstance().insert(StartConfig.keyTable(), rowk);
						} catch (Exception e1) {
							loggerBuffer.append(e1.getMessage());
							di.setPROCESS_STATE("1");
							successRowCount = successRowCount -1;
							e1.printStackTrace();
							if(e1.getClass() == ClientException.class) {
								return DataBaseAction.CONNECTION_ERROR;
							}else {
								continue;
							}
						}
					}
				}
			}else {
				
				rowk.put("D_RECORD_ID", primkey);
				rowk.put("D_DATA_ID", sod_code);
				rowk.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
				rowk.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
				rowk.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
				rowk.put("D_DATETIME", TimeUtil.date2String(fine.getForecastTime(), "yyyy-MM-dd HH:mm:ss"));
				//V_BBB,V01301,V01300,V05001,V06001,V07001,V_ACODE,V04001_02,V04002_02,V04003_02,"
				rowk.put("V_BBB", fine.getCorrectSign());//更正标识
				rowk.put("V01301", fine.getStationNumberChina());//区站号(字符)
				rowk.put("V01300",Integer.parseInt(StationCodeUtil.stringToAscii(fine.getStationNumberChina())) );//区站号(数字)
				rowk.put("V05001", fine.getLatitude());//纬度
				rowk.put("V06001", fine.getLongitude());//经度
				rowk.put("V07001", fine.getHeightOfSationGroundAboveMeanSeaLevel());//测站海拔高度
				rowk.put("V_ACODE", adminCode);//中国行政区代码
				rowk.put("V04001_02", fine.getForecastTime().getYear() + 1900);//预报年
				rowk.put("V04002_02", fine.getForecastTime().getMonth()+1);//预报月
				rowk.put("V04003_02", fine.getForecastTime().getDate());//预报日
				// "V04004_02,V04001,V04002,V04003,V04004,V04005,V_CCCC,V_PROD_DESC,V_PROD_CODE,V04320_005,
				rowk.put("V04004_02", fine.getForecastTime().getHours());//预报时
				
				rowk.put("V04001", fine.getObservationTime().getYear() + 1900);//预报产生年
				rowk.put("V04002", fine.getObservationTime().getMonth()+1);//预报产生月
				rowk.put("V04003", fine.getObservationTime().getDate());//预报产生日
				rowk.put("V04004", fine.getObservationTime().getHours());//预报产生时
				rowk.put("V04005", fine.getObservationTime().getMinutes());//预报产生分
				rowk.put("V_CCCC", fine.getBul_Center());//编报(加工)中心
				rowk.put("V_PROD_DESC", fine.getPROD_DESC());//产品描述
				rowk.put("V_PROD_CODE", fine.getProd_Code());//产品代码
				rowk.put("V04320_005", forecast_max);//最大预报时效
				//V04320_005_080,V04320_041,V_PROD_NUM
				rowk.put("V04320_005_080", interval_max);//最大时效间隔
				rowk.put("V04320_041", fine.getValidtime_Count());//预报时效个数
				rowk.put("V_PROD_NUM", fine.getV_PROD_NUM());//预报产品个数
				rowk.put("D_SOURCE_ID", cts_code);
				
				try {
					OTSDbHelper.getInstance().insert(StartConfig.keyTable(), rowk);
				} catch (Exception e) {
					loggerBuffer.append(e.getMessage());
					di.setPROCESS_STATE("1");
					successRowCount = successRowCount -1;
					e.printStackTrace();
					if(e.getClass() == ClientException.class) {
						return DataBaseAction.CONNECTION_ERROR;
					}else {
						continue;
					}
				}
			}
//				
				diQueues.offer(di);
//			
//				loggerBuffer.append(e.getMessage());
//				di.setPROCESS_STATE("1");
//				successRowCount = successRowCount -1;
//				diQueues.offer(di);
//				if(e.getClass() == ClientException.class) {
//					return DataBaseAction.CONNECTION_ERROR;
//				}
			
		}
		loggerBuffer.append(" INSERT/UPDATE SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
		loggerBuffer.append(" INSERT/UPDATE SUCCESS COUNT : " + successRowCount + "\n");
		loggerBuffer.append(" INSERT/UPDATE FAILED COUNT : " + (list_normal.size() - successRowCount) + "\n");
		return DataBaseAction.SUCCESS;
	}
	/**
	 * 
	 * @Title: reportInfoToDb   
	 * @Description: TODO(这里用一句话描述这个方法的作用)   
	 * @param reportInfos
	 * @param recv_time
	 * @param loggerBuffer void      
	 * @throws：
	 */
	public static void reportInfoToDb(@SuppressWarnings("rawtypes") List<ReportInfo> reportInfos, Date recv_time, StringBuffer loggerBuffer) {
		String tablename = StartConfig.reportTable();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		List<Map<String, Object>> batchs = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < reportInfos.size(); i++) {	
			AgmeReportHeader agmeReportHeader = (AgmeReportHeader) reportInfos.get(i).getT();		
			String primkey = sdf.format(agmeReportHeader.getReport_time())+"_"+agmeReportHeader.getStationNumberChina()+"_"+agmeReportHeader.getCropType()+"_"+agmeReportHeader.getCorrectsign();
			Map<String, Object> row = new HashMap<String, Object>();
			row.put("D_RECORD_ID", primkey);
            row.put("D_DATA_ID", sod_code);
            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
            row.put("D_DATETIME", TimeUtil.date2String(agmeReportHeader.getReport_time(),"yyyy-MM-dd HH:mm:ss"));
            //V_BBB,V_CCCC,V04001_02,V04002_02,V04003_02,V04004_02,V_TT,V_AA,V_II,V_MIMJ,"
            row.put("V_BBB", agmeReportHeader.getCorrectsign());
			row.put("V_CCCC", agmeReportHeader.getBul_center());
			row.put("V04001_02", agmeReportHeader.getForecast_time().getYear() + 1900);
			row.put("V04002_02", agmeReportHeader.getForecast_time().getMonth()+1);
			row.put("V04003_02", agmeReportHeader.getForecast_time().getDate());
			row.put("V04004_02", agmeReportHeader.getForecast_time().getHours());
			row.put("V_TT", agmeReportHeader.getTTAAii().substring(0, 2));
			row.put("V_AA", agmeReportHeader.getTTAAii().substring(2, 4));
			row.put("V_II", agmeReportHeader.getTTAAii().substring(4, 6));
			row.put("V_MIMJ", agmeReportHeader.getCropType());
			//V01301,V01300,V05001,V06001,V04001,V04002,V04003,V04004,V04005,V_LEN,V_REPORT
			row.put("V01301", agmeReportHeader.getStationNumberChina());
			row.put("V01300" ,Integer.parseInt(StationCodeUtil.stringToAscii(agmeReportHeader.getStationNumberChina())));
			row.put("V05001", agmeReportHeader.getLatitude());
			row.put("V06001", agmeReportHeader.getLongitude());
			row.put("V04001", agmeReportHeader.getReport_time().getYear()+1900);
			row.put("V04002", agmeReportHeader.getReport_time().getMonth()+1);
			row.put("V04003", agmeReportHeader.getReport_time().getDate());
			row.put("V04004", agmeReportHeader.getReport_time().getHours());
			row.put("V04005", agmeReportHeader.getReport_time().getMinutes());
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
