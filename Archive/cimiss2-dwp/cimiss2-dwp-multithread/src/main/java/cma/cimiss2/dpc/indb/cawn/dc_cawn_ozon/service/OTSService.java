package cma.cimiss2.dpc.indb.cawn.dc_cawn_ozon.service;

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
import cma.cimiss2.dpc.decoder.bean.cawn.AntarcticOzone;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static BlockingQueue<StatDi> diQueues;
	public static String cts_code=StartConfig.ctsCode();
	public static String sod_code=StartConfig.sodCode();
	public static String v_tt = "CAWN";
	public int defaultInt = 999999;
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}
//	static Map<String, Object> proMap = StationInfo.getProMap();
	public static DataBaseAction insert_ots(List<AntarcticOzone> list, String tablename, Date recv_time, StringBuffer loggerBuffer, String fileN) {
		Map<String, Object> proMap = StationInfo.getProMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		if(list != null && list.size() > 0) {
			int successRowCount = list.size();
			for(int i = 0; i<list.size();i++){
				Map<String, Object> row = new HashMap<String, Object>();
				AntarcticOzone ozone = list.get(i);
				String info = (String) proMap.get(ozone.getStationNumberChina() + "+01");
				if (info == null) {
					// 如果此站号不存在，执行下一个数据
					loggerBuffer.append("\n  In configuration file, this station does not exist: " + ozone.getStationNumberChina());
				} else {
					String[] infos = info.split(",");
					Double height = Double.parseDouble(infos[3]);//测站高度
					ozone.setHeightOfSationGroundAboveMeanSeaLevel(height);
				}
//				String PrimaryKey = sdf.format(ozone.getObservationTime())+"_"+ozone.getStationNumberChina();
//				
//				row.put("D_RECORD_ID", PrimaryKey);
	            row.put("D_DATA_ID", sod_code);
	            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_DATETIME", TimeUtil.date2String(ozone.getObservationTime(),"yyyy-MM-dd HH:mm:ss"));
				//V01301,V01300,V05001,V06001,V07001,V04001,V04002,V04003,V04004,V15020_1,V15020_2
	            row.put("V01301", ozone.getStationNumberChina());// 区站号(字符)
				row.put("V01300", StationCodeUtil.stringToAscii(ozone.getStationNumberChina()));// 区站号(数字)
				row.put("V05001", ozone.getLatitude());// 纬度
				row.put("V06001", ozone.getLongitude());// 经度
				row.put("V07001", ozone.getHeightOfSationGroundAboveMeanSeaLevel());// 测站高度
				row.put("V04001", ozone.getObservationTime().getYear() + 1900);// 资料观测年
				row.put("V04002", ozone.getObservationTime().getMonth()+1);// 资料观测月
				row.put("V04003", ozone.getObservationTime().getDate());// 资料观测日
				row.put("V04004", ozone.getObservationTime().getHours());// 资料观测时
				row.put("V15020_1", ozone.getO3_Ds());// Ds臭氧总量
				row.put("V15020_2", ozone.getO3_Zs());//Zs臭氧总量
	            row.put("V_BBB", "000");
	            row.put("D_SOURCE_ID", cts_code);
	            
				StatDi di = new StatDi();					
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(sod_code);
				di.setDATA_TYPE_1(cts_code);
				di.setTT(v_tt);			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd hh:mm:ss"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("0"); //0成功，1失败
				di.setPROCESS_STATE("0");  //0成功，1失败
				di.setIIiii(list.get(i).getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(ozone.getObservationTime(), "yyyy-MM-dd hh:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				di.setLATITUDE(String.valueOf(ozone.getLatitude()));
				di.setLONGTITUDE(String.valueOf(ozone.getLongitude()));
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
					}else {
						continue;
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
	            row.put("V_ACODE", StationInfo.getAdminCode(agmeReportHeader.getStationNumberChina(), "01"));// V_ACODE
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
