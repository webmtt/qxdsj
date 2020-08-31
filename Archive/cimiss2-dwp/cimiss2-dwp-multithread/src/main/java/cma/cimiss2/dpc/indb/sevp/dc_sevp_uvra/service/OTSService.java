package cma.cimiss2.dpc.indb.sevp.dc_sevp_uvra.service;

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
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
import cma.cimiss2.dpc.decoder.bean.sevp.UvMonitorForecastproduct;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static BlockingQueue<StatDi> diQueues;
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	public static String v_tt = "UVRA";
	public static String V_CCCC = "9999";
	public static int defaultInt = 999999;
			
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}
//	static Map<String, Object> proMap = StationInfo.getProMap();
	public static DataBaseAction insert_ots(List<UvMonitorForecastproduct> list, String tablename, Date recv_time, StringBuffer loggerBuffer, String fileN, String filepath) {
		Map<String, Object> proMap = StationInfo.getProMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		if(list != null && list.size() > 0) {
			int successRowCount = list.size();
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> row = new HashMap<String, Object>();
				double latitude = defaultInt;
				double longtitude = defaultInt;
				double statHeight = defaultInt;
				String info = (String) proMap.get(list.get(i).getStationNumberChina() + "+01");
				if(info == null) {
					loggerBuffer.append("\n In configuration file, this station does not exist: " + list.get(i).getStationNumberChina());
				}		
				else{
					String[] infos = info.split(",");
					if(!infos[1].equals("null")){
						longtitude = Double.parseDouble(infos[1]);					
					}	
					if(!infos[2].equals("null")){
						latitude = Double.parseDouble(infos[2]);			
					}
					if(!infos[3].equals("null")){
						statHeight = Double.parseDouble(infos[3]);				
					}	
				}
				int year = Integer.parseInt(sdf.format(recv_time).substring(0, 4));//资料观测年
				int month = Integer.parseInt(fileN.substring(7, 9));//资料观测月
				int day = Integer.parseInt(fileN.substring(9, 11));//资料观测日
				int hour = Integer.parseInt(fileN.substring(11, 13));//资料观测时
				if ((hour==2) ||(hour==1)) {
					hour=2;
				}else if ((hour==7) ||(hour==8)) {
					hour=8;
				}else {
					loggerBuffer.append("\n File time error: " + hour);
					continue;
				}
				Calendar c = Calendar.getInstance();
				c.set(Calendar.YEAR, year);
			    c.set(Calendar.MONTH, month-1);
			    c.set(Calendar.DAY_OF_MONTH,day);
			    c.set(Calendar.HOUR_OF_DAY, hour);
			    c.set(Calendar.MINUTE, 0);
			    c.set(Calendar.SECOND, 0);
			    c.set(Calendar.MILLISECOND, 0);
			    Date datatime = c.getTime();//得到资料时间
				String  primary = sdf.format(c.getTime())+"_"+list.get(i).getStationNumberChina();
				row.put("D_RECORD_ID", primary);
	            row.put("D_DATA_ID", sod_code);
	            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_DATETIME", TimeUtil.date2String(datatime,"yyyy-MM-dd HH:mm:ss"));
	            //V04001_02,V04002_02,V04003_02,V04004_02,V01301,V01300,V05001,V06001,V07001,V14400,
				row.put("V04001_02", year);//资料观测年
				row.put("V04002_02", month);//资料观测月
				row.put("V04003_02", day);//资料观测日
				row.put("V04004_02", hour);//资料观测时
				row.put("V01301", list.get(i).getStationNumberChina());//区站号(字符)
				row.put("V01300",Integer.parseInt(StationCodeUtil.stringToAscii(list.get(i).getStationNumberChina()))); //区站号(数字)
				row.put("V05001", latitude);//经度
				row.put("V06001", longtitude);//纬度
				row.put("V07001", statHeight);//测站海拔高度
				row.put("V14400", list.get(i).getUvObserveAverageValue());//观测平均值
				//V14401,V14256_01,V14256_02
				row.put("V14401", list.get(i).getUvDailyObserveMaximum());//日观测最大值
				if (hour==2) {
					row.put("V14256_01", list.get(i).getUvPredictedValue());//资料时间为2的观测值
					row.put("V14256_02", "999999");//资料时间为8的观测值
				} else{	
					row.put("V14256_01", "999999");//资料时间为2的观测值
					row.put("V14256_02", list.get(i).getUvPredictedValue());//资料时间为8的观测值
				}
				row.put("D_SOURCE_ID", cts_code);
				
				StatDi di = new StatDi();					
				di.setFILE_NAME_O(filepath);
				di.setDATA_TYPE(sod_code);
				di.setDATA_TYPE_1(cts_code);
				di.setTT(v_tt);			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(filepath);
				di.setBUSINESS_STATE("0"); //0成功，1失败
				di.setPROCESS_STATE("0");  //0成功，1失败	
				di.setIIiii(list.get(i).getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(datatime, "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				di.setLATITUDE(String.valueOf(latitude));
				di.setLONGTITUDE(String.valueOf(longtitude));
				try {
					OTSDbHelper.getInstance().insert(tablename, row);
					diQueues.offer(di);
				}  catch (Exception e) {
					successRowCount = successRowCount -1;
					di.setPROCESS_STATE("1");
					diQueues.offer(di);
					loggerBuffer.append(row);
					loggerBuffer.append(e.getMessage());
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
	static void reportInfoToDb(List<ReportInfo> reportInfos, String tablename, Date recv_time,
			StringBuffer loggerBuffer, String fileN) {
		Map<String, Object> proMap = StationInfo.getProMap();
		if(reportInfos != null && reportInfos.size() > 0){
			List<Map<String, Object>> batchs = new ArrayList<Map<String, Object>>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			for (int i = 0; i < reportInfos.size(); i++) {
				Map<String, Object> row = new HashMap<String, Object>();
				AgmeReportHeader agmeReportHeader = (AgmeReportHeader) reportInfos.get(i).getT();	
				String stationNumberChina = agmeReportHeader.getStationNumberChina();//区站号
				double latitude = defaultInt;
				double longtitude = defaultInt;
				double ACODE =defaultInt;
				String info = (String) proMap.get(stationNumberChina + "+01");
				if(info == null) {
					loggerBuffer.append("\n  In configuration file, this station does not exist: " + stationNumberChina);
				}		
				else{
					String[] infos = info.split(",");
					if(!infos[1].equals("null")){
						longtitude = Double.parseDouble(infos[1]);					
					}
					if(!infos[2].equals("null")){
						latitude = Double.parseDouble(infos[2]);			
					}
					if(!infos[5].equals("null")){
						ACODE = Double.parseDouble(infos[5]);				
					}	
				}
				int year = Integer.parseInt(sdf.format(recv_time).substring(0, 4));//资料观测年
				int month = Integer.parseInt(fileN.substring(7, 9));//资料观测月
				int day = Integer.parseInt(fileN.substring(9, 11));//资料观测日
				int hour = Integer.parseInt(fileN.substring(11, 13));//资料观测时
				if ((hour==2) ||(hour==1)) {
					hour=2;
				}else if ((hour==7) ||(hour==8)) {
					hour=8;
				}else {
					loggerBuffer.append("\n File time error: " + hour);
					continue;
				}
				Calendar c = Calendar.getInstance();
				c.set(Calendar.YEAR, year);
			    c.set(Calendar.MONTH, month-1);
			    c.set(Calendar.DAY_OF_MONTH,day);
			    c.set(Calendar.HOUR_OF_DAY, hour);
			    c.set(Calendar.MINUTE, 0);
			    c.set(Calendar.SECOND, 0);
			    c.set(Calendar.MILLISECOND, 0);
			    Date datatime = c.getTime();//得到资料时间
				String  primary = sdf.format(c.getTime())+"_"+stationNumberChina;
				
				row.put("D_RECORD_ID", primary);
	            row.put("D_DATA_ID", sod_code);
	            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_DATETIME", TimeUtil.date2String(datatime,"yyyy-MM-dd HH:mm:ss"));
	        	
	            row.put("V_BBB", "000");//V_BBB,
				row.put("V_CCCC", V_CCCC);//V_CCCC,编报(加工)中心，确认录入站号
				row.put("V_TT", v_tt);//V_TT,报告类别
			//V01301, V01300,V05001,V06001,V_NCODE,V_ACODE,V04001,V04002,V04003,V04004,V04005,V_LEN,V_REPORT
				row.put("V01301", stationNumberChina);// 区站号(字符)
				row.put("V01300", Integer.parseInt(StationCodeUtil.stringToAscii(stationNumberChina)));// 区站号(数字)
				row.put("V05001", latitude);// 纬度
				row.put("V06001", longtitude);// 经度
				row.put("V_NCODE", 2250);// V_NCODE,国家代码
				row.put("V_ACODE", ACODE);// V_ACODE
				row.put("V04001", year);//资料观测年
				row.put("V04002", month);//资料观测月
				row.put("V04003", day);//资料观测日
				row.put("V04004", hour);//资料观测时
				row.put("V04005", 0);//资料观测时
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
