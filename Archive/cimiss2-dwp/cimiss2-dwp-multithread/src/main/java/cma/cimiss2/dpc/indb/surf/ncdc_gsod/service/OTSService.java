package cma.cimiss2.dpc.indb.surf.ncdc_gsod.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.cimiss2.dwp.tools.utils.WmoStationInfo;
import org.cimiss2.dwp.tools.utils.WmoStationInfo.NcdoStationRuleBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alicloud.openservices.tablestore.ClientException;

import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.surf.SurfWeaGlbMulGsoddayData;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

//import cma.cimiss2.dpc.decoder.surf.ncdc_gsod.CompareStaMaxDate;

public class OTSService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	
	public static String cts_code = StartConfig.ctsCode();
	public static String type = StartConfig.sodCode();
	public static String v_tt = "GSOD";
//	public static Map<String, String> compareMaxDateMap = CompareStaMaxDate.getStaMap();
	public static String packagePath= "cma.cimiss2.dpc.indb.surf.ncdc_gsod.GSODSubThread";
	public static BlockingQueue<StatDi> diQueues ;
	
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}

	public static DataBaseAction insert_ots(List<SurfWeaGlbMulGsoddayData> list, String tablename, Date recv_time, StringBuffer loggerBuffer, String fileN) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		if(list != null && list.size() > 0) {
			int successRowCount = list.size();
			ArrayList<String> staList = WmoStationInfo.getStaList();
			Calendar calendar = Calendar.getInstance();
			Map<String, NcdoStationRuleBean> staMap = WmoStationInfo.getStaMap();
			for (int i = 0; i < list.size(); i++) {
	        	SurfWeaGlbMulGsoddayData surfBean = list.get(i);
	        	Map<String, Object> row = new HashMap<String, Object>();
	        	String stn = surfBean.getStationNumberLocation();
				String wban = surfBean.getWeatherBureauAirForceNavy();
				String yearMoDa = surfBean.getYearMoDa();
				
				//3.判断是不是是最新的日期，如果是最新的数据才解码入库
//				String datatTime = compareMaxDateMap.get(stn + "—" + wban);
//				if(null != datatTime && Integer.valueOf(yearMoDa) <= Integer.valueOf(datatTime)){
//					continue;
//				}
				String primkey = yearMoDa + "_" + stn;
				Date yearMoDa1 = TimeUtil.String2Date(surfBean.getYearMoDa(), "yyyyMMdd");
				calendar.setTime(yearMoDa1);
				
				row.put("D_RECORD_ID", primkey);
	            row.put("D_DATA_ID", type);
	            row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
	            row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
	            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
	            row.put("D_DATETIME", TimeUtil.date2String(yearMoDa1, "yyyy-MM-dd HH:mm:ss"));
	           
	            row.put("V_STN", stn);// 原始STN站号
	            row.put("V_WBAN", wban);// 原始WBAN站号
	            //V01301,V01300,V05001,V06001,V07001,V07031,V02301,V_ACODE,V04001,V04002,
	            /**
				 * 首先判断STN是不是WMO的站号： 1。首先看最末尾是不是0，如果不是0，则不是wmo的站号
				 * 2。末尾是0，去掉末尾的0，读取stas_wmo_zzq.txt，看是否在stas_wmo_zzq.txt中，如果在则是wmo站号，否则不是。
				 * 再得到站号值：
				 * 
				 *  1。如果不是wmo站号，则赋值“999999”
				 * 2。如果是wmo站号，则只取前5位。同时，如果首位也是0，去掉首位。 这个是跟研究室做这个资料的人沟通过后这样解的
				 */

				if (staList.contains(stn)) {
					if (stn.length() >= 5) {
						stn = stn.substring(0, 5);
						 row.put("V01301", stn);// 区站号(字符)
					} else {
						 row.put("V01301", stn);// 区站号(字符)
					}

				} else {
					stn = "999999";
					row.put("V01301", stn);// 区站号(字符)
				}
				row.put("V01300", StationCodeUtil.stringToAscii(stn));// 区站号(数字)
				NcdoStationRuleBean ncdoBean = staMap.get(
						surfBean.getStationNumberLocation() + "_" + surfBean.getWeatherBureauAirForceNavy());
				if (null != ncdoBean) {
					row.put("V05001", ncdoBean.getLat());// 纬度
					row.put("V06001", ncdoBean.getLon());// 经度
					row.put("V07001", ncdoBean.getElev());// 测站高度
				} else {
					row.put("V05001", "999999");// 纬度
					row.put("V06001", "999999");// 经度
					row.put("V07001", "999999");// 测站高度
				}
				row.put("V07031", "999999");// 气压传感器海拔高度
				row.put("V02301", "6");// 台站级别
				row.put("V_ACODE", "5");// 中国行政区划代码
				row.put("V04001", calendar.get(Calendar.YEAR));
	            row.put("V04002", calendar.get(Calendar.MONTH) + 1);
	            //V04003,V12001_701,V12001_040,V12011,V12011_MARK,V12012,V12012_MARK,V12003,V12003_040,V10051_701,"
				row.put("V04003", calendar.get(Calendar.DAY_OF_MONTH));
				row.put("V12001_701", surfBean.getDailyAvgTemperature());// 日平均气温
				row.put("V12001_040", surfBean.getDailyAvgTemperatureCount());// 日气温记录数
				row.put("V12011", surfBean.getMaxTemperature());// 日最高气温
				row.put("V12011_MARK", surfBean.getMaxFlag());// 日最高气温取值标志
				row.put("V12012", surfBean.getMinTemperature());// 日最低气温
				row.put("V12012_MARK", surfBean.getMinFlag());// 日最低气温取值标志
				row.put("V12003", surfBean.getDewPointTemperature());// 日平均露点温度
				row.put("V12003_040", surfBean.getDewPointTemperatureCount());// 日露点温度记录数
				row.put("V10051_701", surfBean.getSeaLevelPressure());// 日平均海平面气压
				
	            //V10051_040,V10004_701,V10004_040,V20001_701,V20001_040,V11291,V11291_040,V11320,V11041,V13023,
				row.put("V10051_040", surfBean.getSeaLevelPressureCount());// 日海平面气压记录数
				row.put("V10004_701", surfBean.getStationPressure());// 日平均本站气压
				row.put("V10004_040", surfBean.getStationPressureCount());// 日本站气压记录数
				row.put("V20001_701", surfBean.getVisibility());// 日平均能见度
				row.put("V20001_040", surfBean.getVisibilityCount());// 日能见度记录数
				row.put("V11291", surfBean.getWindSpeed());// 日平均风速
				row.put("V11291_040", surfBean.getWindSpeedCount());// 日风速记录数
				row.put("V11320", surfBean.getMaxSustainedWindSpeed());// 日最大持续风速
				row.put("V11041", surfBean.getMaxWindGust());// 日最大阵风风速
				row.put("V13023", surfBean.getPrecipitationDaily());// 日降水量
	            
	            //V13023_MARK,V13013,V20302_042,V20302_060,V20302_070,V20302_089,V20302_017,V20302_019
				row.put("V13023_MARK", surfBean.getPrecipitationDailyFlag());// 日降水量取值标志
				row.put("V13013", surfBean.getSnowDepth());// 日(总)雪深
				row.put("V20302_042", surfBean.getFog());// 雾
				row.put("V20302_060", surfBean.getRain());// 雨
				row.put("V20302_070", surfBean.getSnow());// 雪
				row.put("V20302_089", surfBean.getHail());// 冰雹
				row.put("V20302_017", surfBean.getThunder());// 雷
				row.put("V20302_019", surfBean.getTornado());// 龙卷风
				row.put("V_BBB", "000");
				row.put("D_SOURCE_ID", cts_code);
				
				StatDi di = new StatDi();
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(type);
				di.setDATA_TYPE_1(cts_code);
				di.setTT(v_tt);
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd hh:mm:ss"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("0"); // 0成功，1失败
				di.setPROCESS_STATE("0"); // 0成功，1失败
				di.setIIiii(surfBean.getStationNumberLocation());
				if(ncdoBean != null){
					di.setLATITUDE(ncdoBean.getLat());
					di.setLONGTITUDE(ncdoBean.getLon());
				}
				else{
					di.setLATITUDE("999999");
					di.setLONGTITUDE("999999");
				}
				di.setDATA_TIME(TimeUtil.date2String(yearMoDa1, "yyyy-MM-dd hh:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());
				
			    try {
					OTSDbHelper.getInstance().insert(tablename, row);
					diQueues.offer(di);
				}  catch (Exception e) {
					successRowCount = successRowCount -1;
					di.setPROCESS_STATE("1");
					diQueues.offer(di);
					loggerBuffer.append(row);
					loggerBuffer.append(e.getMessage());
					if(e.getClass() == ClientException.class) {
						return DataBaseAction.CONNECTION_ERROR;
					}
				}
	        }
			 loggerBuffer.append(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
		     loggerBuffer.append(" INSERT SUCCESS COUNT : " + successRowCount + "\n");
		     loggerBuffer.append(" INSERT FAILED COUNT : " + (list.size() - successRowCount) + "\n");
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
	static void reportInfoToDb(List<ReportInfo> reportInfos, String tablename, Date recv_time, StringBuffer loggerBuffer, String fileN) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		if(reportInfos != null && reportInfos.size() > 0){
			List<Map<String, Object>> batchs = new ArrayList<Map<String, Object>>();
			Calendar calendar = Calendar.getInstance();
			ArrayList<String> staList = WmoStationInfo.getStaList();
			Map<String, NcdoStationRuleBean> staMap = WmoStationInfo.getStaMap();
			String v_cccc = fileN.substring(0, 6);
			String v_tt = "GSOD";
			String v_bbb = "000";
			for(int i = 0; i < reportInfos.size(); i++){
				Map<String, Object> row = new HashMap<String, Object>();
				@SuppressWarnings("unchecked")
				ArrayList<String> surList = (ArrayList<String>) reportInfos.get(i).getT();
				String stn = surList.get(0);//原始STN站号
				String wban = surList.get(1);//原始WBAN站号
				String yearMoDa = surList.get(2);//资料时间
				Date yearMoDate = TimeUtil.String2Date(yearMoDa, "yyyyMMdd");
				calendar.setTime(yearMoDate);
				
				row.put("D_RECORD_ID", yearMoDa + "_" + wban);
	            row.put("D_DATA_ID", type);
	            row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
	            row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
	            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
	            row.put("D_DATETIME", TimeUtil.date2String(yearMoDate, "yyyy-MM-dd HH:mm:ss"));
				
	            //"V_BBB,V_CCCC,V_TT,V01301,V01300,V05001,V06001,V_NCODE,V_ACODE,"
	            row.put("V_BBB", v_bbb);
	            row.put("V_CCCC", v_cccc);
	            row.put("V_TT", v_tt);
	            if (staList.contains(stn)) {
					if (stn.length() >= 5) {
						stn = stn.substring(0, 5);
						row.put("V01301", stn);// 区站号(字符)
					} else {
						row.put("V01301", stn);// 区站号(字符)
					}

				} else {
					stn = "999999";
					row.put("V01301", stn);// 区站号(字符)
				}
				row.put("V01300", StationCodeUtil.stringToAscii(stn));// 区站号(数字)
				NcdoStationRuleBean ncdoBean = staMap.get(surList.get(0) + "_" + wban);
				if (null != ncdoBean) {
					row.put("V05001", ncdoBean.getLat());// 纬度
					row.put("V06001", ncdoBean.getLon());// 经度
				} else {
					row.put("V05001", "999999");// 纬度
					row.put("V06001", "999999");// 经度
				}
				row.put("V_NCODE", "2250");// V_NCODE
				row.put("V_ACODE", "5");// V_ACODE
	            // "V04001,V04002,V04003,V04004,V04005,"
	            row.put("V04001", calendar.get(Calendar.YEAR));
	            row.put("V04002", calendar.get(Calendar.MONTH) + 1);
	            row.put("V04003", calendar.get(Calendar.DAY_OF_MONTH));
	            row.put("V04004", calendar.get(Calendar.HOUR));
	            row.put("V04005", calendar.get(Calendar.MINUTE));
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
