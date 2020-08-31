package cma.cimiss2.dpc.indb.sand.dc_cawn_sand_nep.service;

import java.sql.Timestamp;
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

import com.alicloud.openservices.tablestore.ClientException;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.cawn.AtmosphericTurbidityData;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {
	
	public static BlockingQueue<StatDi> diQueues;
	public static String cts_code=StartConfig.ctsCode();
	public static String sod_code=StartConfig.sodCode();

	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}

	public static DataBaseAction insert_ots(ParseResult<AtmosphericTurbidityData> parseResult, Date recv_time,
			StringBuffer loggerBuffer, String fileN) {
		Map<String, Object> proMap = StationInfo.getProMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar calendar = Calendar.getInstance();
		List<AtmosphericTurbidityData> list = parseResult.getData();
		if(list != null && list.size() > 0) {
			int successRowCount = list.size();
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> row = new HashMap<String, Object>();
				
				AtmosphericTurbidityData atmoTurbidityBean=list.get(i) ;
				String obserTime=TimeUtil.date2String(atmoTurbidityBean.getObservationTime(), "yyyyMMddHHmmss");
				//row.put("D_RECORD_ID",obserTime +"_"+atmoTurbidityBean.getStationNumberChina());//记录标识
				row.put("D_DATA_ID", sod_code);//资料标识
				row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
				row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
				row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
				calendar.setTime(atmoTurbidityBean.getObservationTime());
				Date date  = convertDate(new Timestamp(atmoTurbidityBean.getObservationTime().getTime()),loggerBuffer);
				row.put("D_DATETIME", TimeUtil.date2String(date, "yyyy-MM-dd HH:mm:ss")); // 资料时间
				row.put("V01301", atmoTurbidityBean.getStationNumberChina());//区站号(字符)
				row.put("V01300",  StationCodeUtil.stringToAscii(atmoTurbidityBean.getStationNumberChina()));//区站号(数字)
				row.put("V05001", atmoTurbidityBean.getLatitude());//纬度
				row.put("V06001", atmoTurbidityBean.getLongitude());//经度
				row.put("V07001", atmoTurbidityBean.getHeightOfSationGroundAboveMeanSeaLevel());//测站海拔高度
				row.put("V_ACODE",StationInfo.getAdminCode(atmoTurbidityBean.getStationNumberChina(), "15"));//中国行政区划代码
				row.put("V04001", date.getYear() + 1900);//资料观测年
				row.put("V04002", date.getMonth()+1);//资料观测月
				row.put("V04003", date.getDate());//资料观测日
				row.put("V04004", date.getHours());//资料观测时
				row.put("V04005", date.getMinutes());//资料观测分
				row.put("V04006", date.getSeconds());//资料观测秒
				/*row.put("V04001", calendar.get(Calendar.YEAR));// 年
				row.put("V04002", calendar.get(Calendar.MONTH) + 1);// 月
				row.put("V04003", calendar.get(Calendar.DAY_OF_MONTH));// 日
				row.put("V04004", calendar.get(Calendar.HOUR));//资料观测时
				row.put("V04005", calendar.get(Calendar.MINUTE));//资料观测分
				row.put("V04006", calendar.get(Calendar.SECOND));//资料观测秒
*/			
				row.put("V_DATA_CODE", atmoTurbidityBean.getData_code());//数据识别码
				row.put("V04016", atmoTurbidityBean.getTimeInterval());//观测时间间隔
				row.put("V15700", atmoTurbidityBean.getParticleScatterCoefficient());//粒子散射系数
				row.put("V12001", atmoTurbidityBean.getTemperature());//环境温度
				row.put("V13003", atmoTurbidityBean.getHumidity());//环境相对湿度
				row.put("V10004", atmoTurbidityBean.getPressure());//环境气压
				row.put("V12505", atmoTurbidityBean.getCavityTemperature());//腔体温度
				row.put("V_BBB", "000");
				row.put("D_SOURCE_ID", cts_code);
				
				StatDi di = new StatDi();					
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(sod_code);
				di.setDATA_TYPE_1(cts_code);
				di.setTT("沙尘暴NEP");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("0"); //0成功，1失败
				di.setPROCESS_STATE("0");  //0成功，1失败	
				di.setIIiii(atmoTurbidityBean.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(atmoTurbidityBean.getObservationTime(), "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());
				di.setLATITUDE(String.valueOf(atmoTurbidityBean.getLatitude()));
				di.setLONGTITUDE(String.valueOf(atmoTurbidityBean.getLongitude()));
				
				try {
					OTSDbHelper.getInstance().insert(StartConfig.valueTable(), row);
					diQueues.offer(di);
				} catch (Exception e) {
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
		}
		
		
		return DataBaseAction.SUCCESS;
	}

	public static void reportInfoToDb(List<ReportInfo> reportInfos, Date recv_time, StringBuffer loggerBuffer,
			String fileN) {
		Map<String, Object> proMap = StationInfo.getProMap();
		if(reportInfos != null && reportInfos.size() > 0){
			String v_tt=fileN.substring(7, 10);
			String v_cccc=fileN.substring(14, 19);
			Calendar calendar = Calendar.getInstance();
			List<Map<String, Object>> batchs = new ArrayList<Map<String, Object>>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		
			for(int i = 0; i < reportInfos.size(); i++){
				Map<String, Object> row = new HashMap<String, Object>();
				ArrayList<String> headList = (ArrayList<String>) reportInfos.get(i).getT();
				String stationNumberChina = headList.get(0);//区站号
				String longitude = headList.get(1);//经度
				String latitude = headList.get(2);//纬度
				String observationTime = headList.get(3);//资料时间
				
				Date yearMoDate = TimeUtil.String2Date(observationTime, "yyyyMMddHHmmss");
				calendar.setTime(yearMoDate);
				//row.put("D_RECORD_ID", observationTime + "_" + stationNumberChina);
				row.put("D_DATA_ID", sod_code);
				row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
				row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
				row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
				row.put("D_DATETIME", TimeUtil.date2String(yearMoDate, "yyyy-MM-dd HH:mm:ss"));
				
				row.put("V_BBB", "000");//V_BBB,
				row.put("V_CCCC",v_cccc);//V_CCCC,编报(加工)中心，确认录入站号
				row.put("V_TT", v_tt);//V_TT,报告类别
				row.put("V01301", stationNumberChina);// 区站号(字符)
			
				row.put("V01300", StationCodeUtil.stringToAscii(stationNumberChina));// 区站号(数字)
			
				row.put("V05001", latitude);// 纬度
				row.put("V06001", longitude);// 经度
				row.put("V_NCODE", "2250");//V_NCODE,国家代码
				row.put("V_ACODE", StationInfo.getAdminCode(stationNumberChina, "15"));// V_ACODE
				row.put("V04001", yearMoDate.getYear() + 1900);//资料观测年
				row.put("V04002", yearMoDate.getMonth()+1);//资料观测月
				row.put("V04003", yearMoDate.getDate());//资料观测日
				row.put("V04004", yearMoDate.getHours());//资料观测时
				row.put("V04005", yearMoDate.getMinutes());//资料观测分
				
				/*row.put("V04001", calendar.get(Calendar.YEAR));// 年
				row.put("V04002", calendar.get(Calendar.MONTH) + 1);// 月
				row.put("V04003", calendar.get(Calendar.DAY_OF_MONTH));// 日
				row.put("V04004", calendar.get(Calendar.HOUR));
				row.put("V04005", calendar.get(Calendar.MINUTE));*/
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
	public static Date convertDate(Date Date, StringBuffer loggerBuffer) {
		Date date = null;
		try {
			String obserTime=TimeUtil.date2String(Date, "yyyyMMddHHmmss");
			 date=TimeUtil.String2Date(obserTime, "yyyyMMddHHmmss");
		} catch (Exception e) {
			loggerBuffer.append("\n Error formatting date" + e.getMessage());
		}
		return date;
	}
	

}
