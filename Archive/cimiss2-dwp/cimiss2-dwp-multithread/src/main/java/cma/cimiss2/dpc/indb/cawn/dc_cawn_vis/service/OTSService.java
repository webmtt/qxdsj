package cma.cimiss2.dpc.indb.cawn.dc_cawn_vis.service;

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
import cma.cimiss2.dpc.decoder.bean.cawn.VIS;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	public static String sod_report_code = StartConfig.reportSodCode();
//	static Map<String, Object> proMap = StationInfo.getProMap();
	public static BlockingQueue<StatDi> diQueues;
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}

	public static DataBaseAction insert_ots(List<VIS> viss, String tablename, Date recv_time, StringBuffer loggerBuffer, String fileN) {
		Map<String, Object> proMap = StationInfo.getProMap();
		if(viss != null && viss.size() > 0) {
			int successRowCount = viss.size();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");		
			String V_TT = "VIS";
			String info;
			for (int i = 0; i < viss.size(); i++) {
				Map<String, Object> row = new HashMap<String, Object>();
				VIS vis = viss.get(i);
				double latitude = 999999;
				double longtitude = 999999;
				double height = 999999;
				int adminCode = 999999;
				Date dataTime = vis.getDataObservationTime();
				String station = vis.getStationNumberChina();
			   
				//row.put("D_RECORD_ID", sdf.format(dataTime)+"_"+station); //+"_"+V_TT
	            row.put("D_DATA_ID", sod_code);
	            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_DATETIME", TimeUtil.date2String(dataTime,"yyyy-MM-dd HH:mm:ss"));
	            
	          //+ "V01301,V01300,V05001,V06001,V07001,V_ACODE,V04001,V04002,V04003,V04004,"
				row.put("V01301", station);
				row.put("V01300", Integer.parseInt(StationCodeUtil.stringToAscii(station)));
				info = (String) proMap.get(station + "+01");
				if(info == null) {
					loggerBuffer.append("\n In configuration file, this station does not exist: " + station);
				}
				else{
					String[] infos = info.split(",");						
					if(infos[1].equals("null"))
						loggerBuffer.append("\n In configuration file, longtitude is null!");
					else
						longtitude = Double.parseDouble(infos[1]);
					if(infos[2].equals("null"))
						loggerBuffer.append("\n In configuration file, latitude is null!");
					else
						latitude = Double.parseDouble(infos[2]);
					if(infos[3].equals("null"))
						loggerBuffer.append("\n In configuration file, station height is null!");
					else
						height = Double.parseDouble(infos[3]);
					if(infos[5].equals("null"))
						loggerBuffer.append("\n In configuration file, V_CCCC is null!");
					else 
						adminCode = Integer.parseInt(infos[5]);
				}
				row.put("V05001", latitude);
				row.put("V06001", longtitude);
				row.put("V07001", height);
				row.put("V_ACODE", adminCode);
				row.put("V04001", dataTime.getYear() + 1900);
				row.put("V04002", dataTime.getMonth() + 1);
				row.put("V04003", dataTime.getDate());
				row.put("V04004", dataTime.getHours());
				//+ "V04005,V04006,V04016,V_ITEM_CODE,V02321,V20001_701_01,V20001_701_10,V20311,V_BBB) "
				row.put("V04005", dataTime.getMinutes());
				row.put("V04006", dataTime.getSeconds());
				row.put("V04016", 999998); // 观测时间间隔，固定为300s
				row.put("V_ITEM_CODE", vis.getItemCode());
				row.put("V02321", vis.getStateCode());
				row.put("V20001_701_01", vis.getVisibility_1min()); //1分钟平均能见度
				row.put("V20001_701_10", vis.getVisibility_10min()); //10分钟平均能见度
				row.put("V20311", vis.getTrend()); // 能见度变化趋势
				row.put("V_BBB", "000"); //更正报标识
	            row.put("D_SOURCE_ID", cts_code);
				
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
				di.setIIiii(station);
				di.setDATA_TIME(TimeUtil.date2String(dataTime, "yyyy-MM-dd HH:mm:ss"));
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
					loggerBuffer.append(row+"\n");
					loggerBuffer.append(e.getMessage()+"\n");
					if(e.getClass() == ClientException.class) {
						return DataBaseAction.CONNECTION_ERROR;
					}
				}
	        }
			 loggerBuffer.append(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
		     loggerBuffer.append(" INSERT SUCCESS COUNT : " + successRowCount + "\n");
		     loggerBuffer.append(" INSERT FAILED COUNT : " + (viss.size() - successRowCount) + "\n");
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
	static void reportInfoToDb(List<ReportInfo> reportInfos, String tablename, Date recv_time, StringBuffer loggerBuffer) {
		Map<String, Object> proMap = StationInfo.getProMap();
		if(reportInfos != null && reportInfos.size() > 0){
			List<Map<String, Object>> batchs = new ArrayList<Map<String, Object>>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String V_TT= "VIS";
			VIS vis = null;
			Date oTime = null;
			String primkey = null;
			String sta;
			String info;
			double latitude = 999999;
			double longtitude = 999999;
			int adminCode = 999999;
			for(int i = 0; i < reportInfos.size(); i++){
				Map<String, Object> row = new HashMap<String, Object>();
				vis = (VIS) reportInfos.get(i).getT();
				sta = vis.getStationNumberChina();
				oTime = vis.getDataObservationTime();
				primkey = sdf.format(oTime) + "_" + sta + "_" + V_TT;
				
				//row.put("D_RECORD_ID", primkey);
	            row.put("D_DATA_ID", sod_report_code);
	            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_DATETIME", TimeUtil.date2String(oTime,"yyyy-MM-dd HH:mm:ss"));
	          //"V_BBB,V_CCCC,V_TT,V01301,V01300,V05001,V06001,V_NCODE,V_ACODE,"
	            row.put("V_BBB", "000");
	            row.put("V_CCCC", "9999");
	            row.put("V_TT", V_TT);
	            row.put("V01301", sta);
	            row.put("V01300", Integer.parseInt(StationCodeUtil.stringToAscii(sta)));
	            info = (String) proMap.get(sta + "+01");
				if(info == null) {
					loggerBuffer.append("\n  In configuration file, this station does not exist: " + sta);
//					continue ;
				}
				else{
					String[] infos = info.split(",");						
					if(infos[1].equals("null"))
						loggerBuffer.append("\n  In configuration file, longtitude is null!");
					else
						longtitude = Double.parseDouble(infos[1]);
					if(infos[2].equals("null"))
						loggerBuffer.append("\n  In configuration file, latitude is null!");
					else
						latitude = Double.parseDouble(infos[2]);
					if(infos[5].equals("null"))
						loggerBuffer.append("\n  In configuration file, V_CCCC is null!");
					else 
						adminCode = Integer.parseInt(infos[5]);
				}
	            row.put("V05001", latitude);
	            row.put("V06001", longtitude);
	            row.put("V_NCODE", 2250);
	            row.put("V_ACODE", adminCode);
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
