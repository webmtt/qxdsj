package cma.cimiss2.dpc.indb.cawn.dc_cawn_pm10.service;

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
import cma.cimiss2.dpc.decoder.bean.cawn.CawnPM10;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {
	public static BlockingQueue<StatDi> diQueues;
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
//    public static Map<String, Object> proMap = StationInfo.getProMap();
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	public static String v_tt="PM10";
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}
	public static DataBaseAction insert_ots(List<CawnPM10> list, String tablename, Date recv_time, StringBuffer loggerBuffer, String fileN) {
		 Map<String, Object> proMap = StationInfo.getProMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		if(list != null && list.size() > 0) {
			int successRowCount = list.size();
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> row = new HashMap<String, Object>();
				CawnPM10 pm10 = list.get(i);
				
				// 根根配置文件  获取站点基本信息
				String info = (String) proMap.get(pm10.getStationNumberChina() + "+16");
				int adminCode = 999999;
				double lon =999999;
				double lat =999999;
				double height =999999;
				if(info == null) {
					loggerBuffer.append("\n In configuration file, this station does not exist: " + pm10.getStationNumberChina());			
				}else {
					String[] infos = info.split(",");
					if(!(infos[5].trim().equals("null") || infos[5].trim().equals(""))){
						adminCode = Integer.parseInt(infos[5]);
					}	
					if(!(infos[1].trim().equals("null") || infos[1].trim().equals(""))){
						lon = Double.parseDouble(infos[1]);
						pm10.setLongitude(lon);
					}
					if(!(infos[2].trim().equals("null") || infos[2].trim().equals(""))){
						lat = Double.parseDouble(infos[2]);
						pm10.setLatitude(lat);
					}
					if(!(infos[3].trim().equals("null") || infos[3].trim().equals(""))){
						height = Double.parseDouble(infos[3]);
					}
				}	
//				String PrimaryKey = sdf.format(pm10.getObservationTime())+"_"+pm10.getStationNumberChina();
//				row.put("D_RECORD_ID", PrimaryKey);
	            row.put("D_DATA_ID", sod_code);
	            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_DATETIME", TimeUtil.date2String(pm10.getObservationTime(),"yyyy-MM-dd HH:mm:ss"));
				//V01301,V01300,V05001,V06001,V07001,V_ACODE,V04001,V04002,V04003,V04004,"
	            row.put("V01301", pm10.getStationNumberChina());// 区站号(字符)
				row.put("V01300", StationCodeUtil.stringToAscii(pm10.getStationNumberChina()));// 区站号(数字)
				row.put("V05001",lat);// 纬度
				row.put("V06001", lon);// 经度
				row.put("V07001", height);// 测站高度
				row.put("V_ACODE", adminCode);//中国行政区代码
				row.put("V04001", pm10.getObservationTime().getYear() + 1900);// 资料观测年
				row.put("V04002", pm10.getObservationTime().getMonth()+1);// 资料观测月
				row.put("V04003", pm10.getObservationTime().getDate());// 资料观测日
				row.put("V04004", pm10.getObservationTime().getHours());// 资料观测时
				//"V04005,V04006,V02321,V04016,V15471,V15487,V15488,V15471_001_701,V15471_024_701,V12001,"
				row.put("V04005", pm10.getObservationTime().getMinutes());// 资料观测分
				row.put("V04006", pm10.getObservationTime().getSeconds());// 资料观测分
				row.put("V02321", pm10.getInstrumentStatusCode());// 仪器状态码
				row.put("V04016", pm10.getTimeInterval());// 观测时间间隔
				row.put("V15471", pm10.getAvgConcentration_5Min());// 质量浓度
				row.put("V15487", pm10.getTotalMass());// 总质量
				row.put("V15488", pm10.getNormalFlow());// 质量标准流量
				row.put("V15471_001_701", pm10.getAvgConcentration_1Hour());// 1小时质量浓度平均值
				row.put("V15471_024_701", pm10.getAvgConcentration_24Hour());// 24小时质量浓度平均值
				row.put("V12001", pm10.getAirTemperature());// 环境温度
				//"V10004,V71617,V15730_P,V15730_S,V15880,V15883,V15886,V02375,V12001_P,V13003_P,
				row.put("V10004", pm10.getAirPressure());// 环境气压
				row.put("V71617", pm10.getProjectCode());// 项目代码
				row.put("V15730_P", pm10.getMainRoadFlow());// 主路流量
				row.put("V15730_S", pm10.getSideRoadFlow());// 旁路流量
				row.put("V15880", pm10.getLoadFactor());// 负载率
				row.put("V15883", pm10.getFrequency());// 频率
				row.put("V15886", pm10.getNoise());// 噪音
				row.put("V02375", pm10.getRunningState());// 运行状态码
				row.put("V12001_P", pm10.getMainRoadTemperature());// 主路温度
				row.put("V13003_P", pm10.getMainRoadRelativeHumidity());// 主路相对湿度
				 //V13003_S,V13003
				row.put("V13003_S", pm10.getSideRoadRelativeHumidity());// 旁路相对湿度
				row.put("V13003", pm10.getAirRelativeHumidity());// 空气相对湿度
				row.put("V_BBB", "000");
				row.put("D_SOURCE_ID", cts_code);
				// 设置DI信息
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
				di.setIIiii(pm10.getStationNumberChina());
				di.setLATITUDE(String.valueOf(pm10.getLatitude()));
				di.setLONGTITUDE(String.valueOf(pm10.getLongitude()));
				di.setDATA_TIME(TimeUtil.date2String(pm10.getObservationTime(), "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());
				try {
					OTSDbHelper.getInstance().insert(tablename, row);
					diQueues.offer(di);
				} catch (Exception e) {
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
	static void reportInfoToDb(List<ReportInfo> reportInfos, String tablename, Date recv_time, StringBuffer loggerBuffer, String v_cccc) {
		if(reportInfos != null && reportInfos.size() > 0){
			List<Map<String, Object>> batchs = new ArrayList<Map<String, Object>>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			Date oTime = new Date();
			for(int i = 0; i < reportInfos.size(); i++){
				Map<String, Object> row = new HashMap<String, Object>();
				AgmeReportHeader agmeReportHeader = (AgmeReportHeader) reportInfos.get(i).getT();		
				oTime = agmeReportHeader.getReport_time();
//				String primkey = sdf.format(agmeReportHeader.getReport_time())+"_"+agmeReportHeader.getStationNumberChina()+"_"+v_cccc;
//				row.put("D_RECORD_ID", primkey);
	            row.put("D_DATA_ID", sod_code);
	            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_DATETIME", TimeUtil.date2String(oTime, "yyyy-MM-dd HH:mm:ss"));
	           //"V_BBB,V_CCCC,V_TT,V01301,V01300,V05001,V06001,V_NCODE,V_ACODE,"
	            row.put("V_BBB", "000");
	            row.put("V_CCCC", v_cccc);
	            row.put("V_TT", v_tt);
	            row.put("V01301", agmeReportHeader.getStationNumberChina());
	            row.put("V01300" ,Integer.parseInt(StationCodeUtil.stringToAscii(agmeReportHeader.getStationNumberChina())));
	            row.put("V05001", agmeReportHeader.getLatitude());
	            row.put("V06001", agmeReportHeader.getLongitude());
	            row.put("V_NCODE", 2250);//V_NCODE
	            row.put("V_ACODE", StationInfo.getAdminCode(agmeReportHeader.getStationNumberChina(), "16"));// V_ACODE
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
