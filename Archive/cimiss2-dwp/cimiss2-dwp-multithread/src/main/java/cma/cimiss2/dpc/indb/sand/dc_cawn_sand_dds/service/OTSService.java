package cma.cimiss2.dpc.indb.sand.dc_cawn_sand_dds.service;

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
import cma.cimiss2.dpc.decoder.bean.sand.SandDds;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {
//	public static Map<String, Object> proMap = StationInfo.getProMap();
	public static BlockingQueue<StatDi> diQueues;
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}
	@SuppressWarnings("deprecation")
	public static DataBaseAction insert_ots(ParseResult<SandDds> parseResult, Date recv_time, StringBuffer loggerBuffer,
			String fileN) {
		List<SandDds> sandDds = parseResult.getData();
		Map<String, Object> proMap = StationInfo.getProMap();
		if(sandDds != null && sandDds.size() > 0) {
			int successRowCount = sandDds.size();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			for (int i = 0; i < sandDds.size(); i++) {
				Map<String, Object> row = new HashMap<String, Object>();
				SandDds dds = sandDds.get(i);
				// 根根配置文件  获取站点基本信息
				String info = (String) proMap.get(dds.getStationNumberChina() + "+15");
				int adminCode = 999999;
				if(info == null) {
					loggerBuffer.append("\n In the configuration file, the station number does not exist" + dds.getStationNumberChina());
					
				}else{
					String[] infos = info.split(",");
					if(!(infos[5].trim().equals("null") || infos[5].trim().equals(""))){
						adminCode = Integer.parseInt(infos[5]);
					}
				}
//				String PrimaryKey = sdf.format(dds.getObservationTime())+"_"+dds.getStationNumberChina();
//				row.put("D_RECORD_ID", PrimaryKey);// 记录标识
				row.put("D_DATA_ID", sod_code);// 资料标识
				row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss")); // 入库时间
				row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss")); // 收到时间
				row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss")); // 更新时间第一次入库与入库时间一致
				row.put("D_DATETIME", TimeUtil.date2String(dds.getObservationTime(), "yyyy-MM-dd HH:mm:ss")); // 资料时间
				row.put("V01301", dds.getStationNumberChina());// 区站号(字符)
				row.put("V01300", StationCodeUtil.stringToAscii(dds.getStationNumberChina()));// 区站号(数字)
				row.put("V05001", dds.getLatitude());// 纬度
				row.put("V06001", dds.getLongitude());// 经度
				row.put("V07001", dds.getHeightOfSationGroundAboveMeanSeaLevel());// 测站高度
				row.put("V_ACODE", adminCode);// 中国行政区划代码
				row.put("V04001", dds.getObservationTime().getYear() + 1900);// 资料观测年
				row.put("V04002", dds.getObservationTime().getMonth()+1);// 资料观测月
				row.put("V04003", dds.getObservationTime().getDate());// 资料观测日
				row.put("V04004", dds.getObservationTime().getHours());// 资料观测时
				row.put("V04005", dds.getObservationTime().getMinutes());// 资料观测分
				row.put("V04006", dds.getObservationTime().getSeconds());// 资料观测分
				row.put("V04016", dds.getObservationInterval());//观测时间间隔
				row.put("V15401", dds.getSampMe_Num());//采样膜编号
				row.put("V04300_017", dds.getStime());// 开始时间
				row.put("V04300_018", dds.getEtime());// 结束时间
				row.put("V15405", dds.getDustCol_Num());//集尘缸编号
				row.put("V15406", dds.getDustCol_Area());//集尘缸口面积
				row.put("V15407", dds.getCUSO4_Wei());//硫酸铜加入量
				row.put("V04312", dds.getAccumulateDays());//采样累积天数
				row.put("V10004_701", dds.getPRS_Avg());//平均气压
				row.put("V12001_701", dds.getTEM_Avg());//平均气温
				row.put("V15431", dds.getSamp_Wei());//最终样品重量
				row.put("V15421", dds.getDustDep_Atsph());//大气降尘量
				row.put("V_BBB", "000");
				row.put("D_SOURCE_ID", cts_code);
				
				StatDi di = new StatDi();					
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(sod_code);
				di.setDATA_TYPE_1(cts_code);
				di.setTT("DDS");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("0"); //0成功，1失败
				di.setPROCESS_STATE("0");  //0成功，1失败	
				di.setIIiii(dds.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(dds.getObservationTime(), "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());
				di.setLATITUDE(String.valueOf(dds.getLatitude()));
				di.setLONGTITUDE(String.valueOf(dds.getLongitude()));
				
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
		    loggerBuffer.append(" INSERT FAILED COUNT : " + (sandDds.size() - successRowCount) + "\n");
		}
		return DataBaseAction.SUCCESS;
	}
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public static void reportInfoToDb(List<ReportInfo> reports, Date recv_time, StringBuffer loggerBuffer) {
		
		if(reports != null && reports.size() > 0){
			List<Map<String, Object>> batchs = new ArrayList<Map<String, Object>>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			Date oTime = new Date();
			for(int i = 0; i < reports.size(); i++){
				Map<String, Object> row = new HashMap<String, Object>();
				AgmeReportHeader agmeReportHeader = (AgmeReportHeader) reports.get(i).getT();		
				oTime = agmeReportHeader.getReport_time();
//				String primkey = sdf.format(agmeReportHeader.getReport_time())+"_"+agmeReportHeader.getStationNumberChina()+"_"+"9999";
//				row.put("D_RECORD_ID", primkey);
	            row.put("D_DATA_ID", sod_code);
	            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_DATETIME", TimeUtil.date2String(oTime, "yyyy-MM-dd HH:mm:ss"));
	           //"V_BBB,V_CCCC,V_TT,V01301,V01300,V05001,V06001,V_NCODE,V_ACODE,"
	            row.put("V_BBB", "000");
	            row.put("V_CCCC", "9999");
	            row.put("V_TT", "DDS");
	            row.put("V01301", agmeReportHeader.getStationNumberChina());
	            row.put("V01300" ,Integer.parseInt(StationCodeUtil.stringToAscii(agmeReportHeader.getStationNumberChina())));
	            row.put("V05001", agmeReportHeader.getLatitude());
	            row.put("V06001", agmeReportHeader.getLongitude());
	            row.put("V_NCODE", 2250);//V_NCODE
	            row.put("V_ACODE", StationInfo.getAdminCode(agmeReportHeader.getStationNumberChina(), "15"));// V_ACODE
	            // "V04001,V04002,V04003,V04004,V04005,"
	            row.put("V04001", oTime.getYear() + 1900);
	            row.put("V04002", oTime.getMonth() + 1);
	            row.put("V04003", oTime.getDate());
	            row.put("V04004", oTime.getHours());
	            row.put("V04005", oTime.getMinutes());
				// "V_LEN,V_REPORT)
	            row.put("V_LEN", reports.get(i).getReport().length());
	            row.put("V_REPORT", reports.get(i).getReport());
	            
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
