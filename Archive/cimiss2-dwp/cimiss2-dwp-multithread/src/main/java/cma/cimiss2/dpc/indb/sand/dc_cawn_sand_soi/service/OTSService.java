package cma.cimiss2.dpc.indb.sand.dc_cawn_sand_soi.service;

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
import org.cimiss2.dwp.tools.utils.NumberUtil;
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;

import com.alicloud.openservices.tablestore.ClientException;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
import cma.cimiss2.dpc.decoder.bean.sand.SandChnSoi;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {
	public static String cts_code=StartConfig.ctsCode();
	public static String sod_code=StartConfig.sodCode();
	public static BlockingQueue<StatDi> diQueues;
//	static Map<String, Object> proMap = StationInfo.getProMap();
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}

	@SuppressWarnings("deprecation")
	public static DataBaseAction insert_ots(ParseResult<SandChnSoi> parseResult, Date recv_time,
			StringBuffer loggerBuffer, String fileName) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		List<SandChnSoi> list = parseResult.getData();
		if(list != null && list.size() > 0) {
			int successRowCount = list.size();
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> row = new HashMap<String, Object>();
				SandChnSoi soi = list.get(i);
				
				Date dataTime = soi.getDataTime(); // 资料时间
				String stationNumberC = soi.getStationNumberC(); // 字符站号
				String stationNumberN = StationCodeUtil.stringToAscii(stationNumberC);
				Double latitude = soi.getLatitude(); // 纬度
				Double longitude = soi.getLongitude(); // 纬度
				Double observationTimeInterval = soi.getObservationTimeInterval(); // 观测时间间隔
				Double elevationAltitude = soi.getElevationAltitude(); // 测站海拔高度
				Double soilWeightMoistureContent_10 = soi.getSoilWeightMoistureContent_10(); // 10厘米土壤重量含水率
				Double soilWeightMoistureContent_20 = soi.getSoilWeightMoistureContent_20(); // 20厘米土壤重量含水率
				Double soilWeightMoistureContent_30 = soi.getSoilWeightMoistureContent_30(); // 30厘米土壤重量含水率
				Double soilWeightMoistureContent_40 = soi.getSoilWeightMoistureContent_40(); // 40厘米土壤重量含水率
				Double soilWeightMoistureContent_50 = soi.getSoilWeightMoistureContent_50(); // 50厘米土壤重量含水率
				
				String adminCode = getAdminCodeByStationNumberC(stationNumberC,loggerBuffer);
				String lengthStr = "00000";
				
				String id = TimeUtil.date2String(dataTime, TimeUtil.DATE_FMT_YMDHMS) //
						+ "_" + (lengthStr.substring(0, 5 - stationNumberC.length()) + stationNumberC) //
						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(longitude * 1000000), 10) //
						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(latitude * 1000000), 9)//
						+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(elevationAltitude * 100), 8);
				//row.put("D_RECORD_ID", id); // 记录标识
				row.put("D_DATA_ID", sod_code); // 资料标识，由配置文件配置
				row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss")); // 入库时间
				row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss")); // 收到时间
				row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss")); // 更新时间
				row.put("D_DATETIME", TimeUtil.date2String(dataTime, "yyyy-MM-dd HH:mm:ss")); // 资料时间

				row.put("V01301", stationNumberC); // 区站号（字符）
				row.put("V01300", stationNumberN);
				
				latitude = NumberUtil.FormatNumOrNine(latitude).doubleValue();
				longitude = NumberUtil.FormatNumOrNine(longitude).doubleValue();
				elevationAltitude =NumberUtil.FormatNumOrNine(elevationAltitude).doubleValue();
				row.put("V05001", latitude); // 纬度
				row.put("V06001", longitude); // 经度
				row.put("V07001", elevationAltitude); // 测站海拔高度
				row.put("V_ACODE", adminCode); // 行政区划代码

				row.put("V04001", dataTime.getYear()+1900); // 资料观测年
				row.put("V04002", dataTime.getMonth()+1); // 资料观测月
				row.put("V04003", dataTime.getDate()); // 资料观测日
				row.put("V04004", dataTime.getHours()); // 资料观测时
				row.put("V04005", dataTime.getMinutes()); // 资料观测分
				row.put("V04006", dataTime.getMinutes()); // 资料观测秒

				row.put("V04016", observationTimeInterval); // 观测时间间隔
				
				soilWeightMoistureContent_10=NumberUtil.FormatNumOrNine(soilWeightMoistureContent_10).doubleValue();
				soilWeightMoistureContent_20=NumberUtil.FormatNumOrNine(soilWeightMoistureContent_20).doubleValue();
				soilWeightMoistureContent_30=NumberUtil.FormatNumOrNine(soilWeightMoistureContent_30).doubleValue();
				soilWeightMoistureContent_40=NumberUtil.FormatNumOrNine(soilWeightMoistureContent_40).doubleValue();
				soilWeightMoistureContent_50=NumberUtil.FormatNumOrNine(soilWeightMoistureContent_50).doubleValue();

				
				row.put("V71104_010",soilWeightMoistureContent_10 ); // 10厘米土壤重量含水率
				row.put("V71104_020", soilWeightMoistureContent_20); // 20厘米土壤重量含水率
				row.put("V71104_030", soilWeightMoistureContent_30); // 30厘米土壤重量含水率
				row.put("V71104_040", soilWeightMoistureContent_40); // 40厘米土壤重量含水率
				row.put("V71104_050", soilWeightMoistureContent_50); // 50厘米土壤重量含水率
				row.put("V_BBB","000");
				row.put("D_SOURCE_ID", cts_code);
				
				StatDi di = new StatDi();					
				di.setFILE_NAME_O(fileName);
				di.setDATA_TYPE(sod_code);
				di.setDATA_TYPE_1(cts_code);
				di.setTT("沙尘暴soi");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileName);
				di.setBUSINESS_STATE("0"); //0成功，1失败
				di.setPROCESS_STATE("0");  //0成功，1失败	
				di.setIIiii(soi.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(soi.getDataTime(), "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());
				di.setLATITUDE(String.valueOf(NumberUtil.FormatNumOrNine(latitude)));
				di.setLONGTITUDE(String.valueOf(NumberUtil.FormatNumOrNine(longitude)));
				
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

	@SuppressWarnings("deprecation")
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
	            row.put("V_TT", "9999");
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
	
	private static String getAdminCodeByStationNumberC(String stationNumberC, StringBuffer loggerBuffer) {
		Map<String, Object> proMap = StationInfo.getProMap();
		// 根据站号查询行政区划代码
		String info = (String) proMap.get(stationNumberC + "+15");
		String defaultF="999999";
		if (info == null) {
			// 如果此站号不存在，执行下一个数据
			loggerBuffer.append("\n In the configuration file, the station number does not exist : " + stationNumberC);
			return defaultF;
		}else{
		String[] infos = info.split(",");
		return infos[5]; // 行政区划代码
		}
	}

}
