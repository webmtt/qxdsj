package cma.cimiss2.dpc.indb.sevp.dc_sevp_city.service;

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

import cma.cimiss2.dpc.decoder.bean.sevp.CityForeList;
import cma.cimiss2.dpc.decoder.bean.sevp.CityForecastLast;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static BlockingQueue<StatDi> diQueues;
	public static String sod_code = StartConfig.sodCode();
	public static String cts_code = StartConfig.ctsCode();
//	static Map<String, Object> proMap = StationInfo.getProMap();		
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}

	public static DataBaseAction processSuccessReport(List<CityForecastLast> cityForecastLasts, Date recv_time, String cts_code, String fileN) {
		Map<String, Object> proMap = StationInfo.getProMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		int successRowCount = cityForecastLasts.size();
		for (int i = 0; i < cityForecastLasts.size(); i++) {
			CityForecastLast last_list = cityForecastLasts.get(i);
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
			di.setIIiii(last_list.getStationNumberChina());
			di.setDATA_TIME(TimeUtil.date2String(last_list.getObservationTime(), "yyyy-MM-dd HH:mm:ss"));
			di.setPROCESS_END_TIME(TimeUtil.getSysTime());
			di.setRECORD_TIME(TimeUtil.getSysTime());
			di.setLATITUDE(String.valueOf(last_list.getLatitude()));
			di.setLONGTITUDE(String.valueOf(last_list.getLongitude()));
			// changed on 2019-3-11
			String primkey = sdf.format(last_list.getObservationTime()) +"_"+sdf.format(last_list.getForecastTime())+"_"+last_list.getStationNumberChina()+"_"+last_list.getProd_Code();
			List<CityForeList> cityForeLists = last_list.getEle();
			try {
				boolean isRevised = false; 
				String v_bbb = last_list.getCorrectSign();
				if(v_bbb.compareTo("000") > 0)
					isRevised = true;
				
				for(int j = 0; j < cityForeLists.size(); j ++){
					Map<String, Object> row = new HashMap<String, Object>();
					CityForeList list = cityForeLists.get(j);
					row.put("D_ELE_ID", primkey+"_"+list.getValidtime());
					row.put("D_RECORD_ID", primkey);
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
					row.put("D_DATETIME", TimeUtil.date2String(last_list.getObservationTime(),"yyyy-MM-dd HH:mm:ss"));
					//"V04320,V12001,V13003,V11001,V11002,V10004,V13016,V20010,V20051,V20312,"
					row.put("V04320", list.getValidtime());//预报时效
					row.put("V12001", list.getTEM());//温度/气温
					row.put("V13003", list.getRHU());//相对湿度
					row.put("V11001", list.getWIN_D());//风向
					row.put("V11002", list.getWIN_S());//风速
					if(list.getPRS().doubleValue() == 999999.0 || list.getPRS().doubleValue() == 999998.0)
						row.put("V10004", list.getPRS());
					else
						row.put("V10004", list.getPRS() + 1000);//气压
					row.put("V13016", list.getPRE_PRE_Fore());//可降水分（预报降水量）
					row.put("V20010", list.getCLO_Cov());//总云量
					row.put("V20051", list.getCLO_Cov_Low());//低云量
					row.put("V20312", list.getWEP());//天气现象
					// "V20001,V12016,V12017,V13008_24,V13007_24,V13023,V13022,V20010_12,V20051_12,V20312_12,
					row.put("V20001", list.getVIS());//水平能见度(人工)
					row.put("V12016", list.getTEM_Max_24h());//未来24小时最高气温
					row.put("V12017", list.getTEM_Min_24h());//未来24小时最低气温
					row.put("V13008_24", list.getRHU_Max_24h());//24小时最大相对湿度
					row.put("V13007_24", list.getRHU_Min_24h());//24小时最小相对湿度
					row.put("V13023", list.getPRE_24h());//未来24小时降水量
					row.put("V13022", list.getPRE_12h());//未来12小时降水量
					row.put("V20010_12", list.getCLO_Cov_12h());//12小时内总云量
					row.put("V20051_12", list.getCLO_Cov_Low_12h());//12小时内低云量
					row.put("V20312_12", list.getWEP_Past_12h());//12小时内天气现象
					// V11001_12,V11040_12,V_BBB,V01301
					row.put("V11001_12", list.getWIN_PD_12h());//12小时内盛行风向
					row.put("V11040_12", list.getWIN_S_Max_12h());//12小时内最大风速
					row.put("V_BBB", last_list.getCorrectSign());//更正标志
					row.put("V01301", last_list.getStationNumberChina());
					row.put("V01300",Integer.parseInt(StationCodeUtil.stringToAscii(last_list.getStationNumberChina())));//区站号(数字)
					
					row.put("D_SOURCE_ID", cts_code);
					
					if(isRevised) {
						OTSDbHelper.getInstance().update(StartConfig.valueTable(), "D_ELE_ID", row);
					}else {
						OTSDbHelper.getInstance().insert(StartConfig.valueTable(), "D_ELE_ID", row);
					}
				}
				Map<String, Object> rowk = new HashMap<String, Object>();
				// 根根配置文件  获取站点基本信息
				String info = (String) proMap.get(last_list.getStationNumberChina() + "+01");
				int adminCode = 999999;
				if(info == null) {
					infoLogger.error("\n In configuration file, this station does not exist: " + last_list.getStationNumberChina());			
				}else{			
					String[] infos = info.split(",");
					if(!(infos[5].trim().equals("null") || infos[5].trim().equals(""))){
						adminCode = Integer.parseInt(infos[5]);
					}
				}
				
				if(isRevised) {
					
					rowk.put("D_RECORD_ID", primkey);
					rowk.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					rowk.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					rowk.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
		            rowk.put("V_BBB", last_list.getCorrectSign());//更正标识
					
					OTSDbHelper.getInstance().update("SEVP_WEFC_ACPP_LAST_K_TAB", rowk);
				}else {
					
					rowk.put("D_RECORD_ID", primkey);
					rowk.put("D_DATA_ID", sod_code);
					rowk.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					rowk.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					rowk.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					rowk.put("D_DATETIME", TimeUtil.date2String(last_list.getObservationTime(), "yyyy-MM-dd HH:mm:ss"));
					//V_BBB,V01301,V01300,V05001,V06001,V07001,V_ACODE,V04001_02,V04002_02,V04003_02"
		            rowk.put("V_BBB", last_list.getCorrectSign());//更正标识
					rowk.put("V01301", last_list.getStationNumberChina());//区站号(字符)
					rowk.put("V01300",Integer.parseInt(StationCodeUtil.stringToAscii(last_list.getStationNumberChina())));//区站号(数字)
					rowk.put("V05001", last_list.getLatitude());//纬度
					rowk.put("V06001", last_list.getLongitude());//经度
					rowk.put("V07001", last_list.getHeightOfSationGroundAboveMeanSeaLevel());//测站海拔高度
					rowk.put("V_ACODE", adminCode);//中国行政区代码
					rowk.put("V04001_02", last_list.getObservationTime().getYear() + 1900);//预报年
					rowk.put("V04002_02", last_list.getObservationTime().getMonth()+1);//预报月
					rowk.put("V04003_02", last_list.getObservationTime().getDate());//预报日
					//"V04004_02,V04001,V04002,V04003,V04004,V04005,V_CCCC,V_PROD_DESC,V_PROD_CODE,V04320_005"
					rowk.put("V04004_02", last_list.getObservationTime().getHours());//预报时
					rowk.put("V04001", last_list.getForecastTime().getYear() + 1900);//预报产生年
					rowk.put("V04002", last_list.getForecastTime().getMonth()+1);//预报产生月
					rowk.put("V04003", last_list.getForecastTime().getDate());//预报产生日
					rowk.put("V04004", last_list.getForecastTime().getHours());//预报产生时
					rowk.put("V04005", last_list.getForecastTime().getMinutes());//预报产生分
					rowk.put("V_CCCC", last_list.getBul_Center());//编报(加工)中心
					rowk.put("V_PROD_DESC", last_list.getPROD_DESC());//产品描述
					rowk.put("V_PROD_CODE", last_list.getProd_Code());//产品代码
					rowk.put("V04320_005", last_list.getValidtime_Max());//最大预报时效
					//"V04320_005_080,V04320_041,V_PROD_NUM
					rowk.put("V04320_005_080", last_list.getValidtime_Max_Intr());//最大时效间隔
					rowk.put("V04320_041", last_list.getValidtime_Count());//预报时效个数
					rowk.put("V_PROD_NUM", last_list.getV_PROD_NUM());//预报产品个数
					
					rowk.put("D_SOURCE_ID", cts_code);
					
					OTSDbHelper.getInstance().insert(StartConfig.keyTable(), rowk);
				}
				
				diQueues.offer(di);
			} catch (Exception e) {
				infoLogger.error(e.getMessage());
				di.setPROCESS_STATE("1");
				successRowCount = successRowCount -1;
				diQueues.offer(di);
			}
		}
		infoLogger.info(" INSERT/UPDATE SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
		infoLogger.info(" INSERT/UPDATE SUCCESS COUNT : " + successRowCount + "\n");
		infoLogger.info(" INSERT/UPDATE FAILED COUNT : " + (cityForecastLasts.size() - successRowCount) + "\n");
		return DataBaseAction.SUCCESS;
	}
}
