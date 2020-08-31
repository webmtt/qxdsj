package cma.cimiss2.dpc.indb.upar.dc_upar_ttua.service;

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
import org.cimiss2.dwp.tools.utils.TimeUtil;

import com.alicloud.openservices.tablestore.ClientException;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.upar.UDUA;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {
	public static String sod_report_code = StartConfig.reportSodCode();
	
	public static String sod_code = StartConfig.sodCode();
	static String V_TT = "";
	public static String cts_code = StartConfig.ctsCode();
	public static BlockingQueue<StatDi> diQueues;
	
	public static double noObser = 999998;
	public static String noObserStr = "999998";
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}


	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}


	@SuppressWarnings("rawtypes")
	public static DataBaseAction processSuccessReport(ParseResult<UDUA> parseResult, String eletablename, String reptablename,
			Date recv_time, StringBuffer loggerBuffer, String fileN) {
		DataBaseAction action = null;
		action = insert_ots(parseResult,eletablename,recv_time,loggerBuffer,fileN);
		
		List<ReportInfo> reportInfos = parseResult.getReports();
		reportInfoToDb(reportInfos,reptablename, recv_time,loggerBuffer);  
		
		return action;
	}
	
	
	@SuppressWarnings("deprecation")
	public static DataBaseAction insert_ots(ParseResult<UDUA> parseResult, String tablename, Date recv_time,
			StringBuffer loggerBuffer, String fileN) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		List<UDUA> list = parseResult.getData();
		if(list != null && list.size() > 0) {
			int successRowCount = list.size();
	        for (int i = 0; i < list.size(); i++) {
	        	UDUA udua = list.get(i);
	        	if(udua.getReportType().equals("AMDAR"))
					V_TT = "UD";
				else V_TT = "UA";
	            Map<String, Object> row = new HashMap<String, Object>();
	            Date dataTime = udua.getDataTime();
				// oTime 2019-4-15修改
//				Date oTime = udua.getObservationTime();
				Date oTime = new Date(dataTime.getTime());
//				oTime.setMinutes(0);
				// 小时不归约，comment on 2019-4-11
//				int hour = oTime.getHours();
//				oTime.setHours(((hour + 23) % 24 / 3 + 1) * 3 % 24);
				String lat = String.valueOf((int)(udua.getLatitude() * 1e6));
				String lon = String.valueOf((int)(udua.getLongtitude() * 1e6));
				lat = lat.replaceAll("-", "0");
				lon = lon.replaceAll("-", "0");
				boolean isRevised = false; 
				String v_bbb = udua.getCorrectSign();
				if(v_bbb.compareTo("000") > 0)
					isRevised = true;
				
				row.put("D_RECORD_ID", sdf.format(dataTime)+"_"+udua.getPlaneID()+"_"+lat+"_"+lon+"_"+(int)(udua.getHeightOfStandardPressure()*1e4)+"_"+udua.getPlaneStateAndObsType());
				row.put("D_DATA_ID", sod_code);
				row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
		        row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
		        row.put("D_DATETIME", TimeUtil.date2String(oTime,"yyyy-MM-dd HH:mm:ss"));  
		        row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
				// "V_BBB,V04001_02,V04002_02,V04003_02,V04004_02,V04005_02,V01301,V05001,V06001,V04001,"
				row.put("V_BBB", udua.getCorrectSign());  // 更正报标识
				row.put("V04001_02", dataTime.getYear() + 1900); // 资料年
				row.put("V04002_02", dataTime.getMonth() + 1); // 资料月
				row.put("V04003_02", dataTime.getDate()); // 资料日
				row.put("V04004_02", dataTime.getHours()); //资料时
//				row.put("V04005_02", 0); //资料分
				row.put("V04005_02", dataTime.getMinutes());
				
				row.put("V01301", udua.getPlaneID()); 
				row.put("V05001", udua.getLatitude());
				row.put("V06001", udua.getLongtitude());
				row.put("V04001", dataTime.getYear() + 1900);// 观测时间 年
				
				//+ "V04002,V04003,V04004,V04005,V04006,V07010,V07002,V08009,V12001,V12003,"
				row.put("V04002", dataTime.getMonth() + 1);// 观测时间 月
				row.put("V04003", dataTime.getDate()); // 观测时间 日
				row.put("V04004", dataTime.getHours()); // 观测时间 时
				row.put("V04005", dataTime.getMinutes()); // 观测时间 分
				row.put("V04006", 0); // 观测时间 秒
				row.put("V07010", udua.getAircraftHeightAboveSeaLevel());
				row.put("V07002", udua.getHeightOfStandardPressure());
				row.put("V08009", udua.getPlaneStateAndObsType());
				row.put("V12001", udua.getTemperature());
				row.put("V12003", udua.getDewPoint());
				
				//+ "V13003,V11001,V11002,V11031,V02061,V02062,V02005,V11036) "
				row.put("V13003", udua.getRelativehumidity());
				row.put("V11001", udua.getWindDir());
				row.put("V11002", udua.getWindSpeed());
				row.put("V11031", udua.getTurbulence());
				row.put("V02061", udua.getNaviType());
				row.put("V02062", udua.getDataTransType());
				row.put("V02005", udua.getTemperatureObsAccuCode());
				row.put("V11036", udua.getVerticalWindSpeed());
				// added on 2019-4-11
				row.put("D_SOURCE_ID", cts_code);
				
//				+ "D_DATA_DPCID,V_TT,V_STT,C_CCCC,V01032,V01031,V01008,V01023,V01110,V01111,"
//				row.put("D_DATA_DPCID", "B.0014.0001.P001");
				row.put("V_TT", udua.getReportType());
				row.put("V_STT", udua.getReportType());
				row.put("C_CCCC", udua.getReportCenter());
				row.put("V01032", noObser);
				row.put("V01031", noObser);
				row.put("V01008", noObserStr);
				row.put("V01023", noObser);
				row.put("V01110", noObserStr);
				row.put("V01111", noObserStr);
				//+ "V01112,V02170, V02002,V02001,V02065,V33025,V02070,V07001,V_ACODE,V07004,"
				row.put("V01112", noObserStr);
				row.put("V02170", noObser);
				row.put("V02002", noObser);
				row.put("V02001", noObser);
				row.put("V02065", noObserStr);
				row.put("V33025", noObser);
				row.put("V02070", noObser);
				row.put("V07001", noObser);
//				row.put("V_ACODE", noObser);
				row.put("V07004", noObser);
//				+ "V10070,V13002,V20043,V20044,V20045,V33026,V11034,V11035,V11075,V11076,"
				row.put("V10070", noObser);
				row.put("V13002", noObser);
				row.put("V20043", noObser);
				row.put("V20044", noObser);
				row.put("V20045", noObser);
				row.put("V33026", noObser);
				row.put("V11034", noObser);
				row.put("V11035", noObser);
				row.put("V11075", noObser);
				row.put("V11076", noObser);
//				+ "V11077,V11039,V11037,V11100,V11101,V11102,V11103,V11104,V02063,V02064,"
				row.put("V11077", noObser);
				row.put("V11039", noObser);
				row.put("V11037", noObser);
				row.put("V11100", noObser);
				row.put("V11101", noObser);
				row.put("V11102", noObser);
				row.put("V11103", noObser);
				row.put("V11104", noObser);
				row.put("V02063", noObser);
				row.put("V02064", noObser);
//				+ "Q07004,Q07010,Q07002,Q10070,Q08009,Q12001,Q02005,Q12003,Q13003,Q13002,"
				row.put("Q07004", 9);
				row.put("Q07010", 9);
				row.put("Q07002", 9);
				row.put("Q10070", 9);
				row.put("Q08009", 9);
				row.put("Q12001", 9);
				row.put("Q02005", 9);
				row.put("Q12003", 9);
				row.put("Q13003", 9);
				row.put("Q13002", 9);
//				+ "Q20043,Q20044,Q20045,Q33026,Q11001,Q11002,Q11034,Q11035,Q11036,Q11075,"
				row.put("Q20043", 9);
				row.put("Q20044", 9);
				row.put("Q20045", 9);
				row.put("Q33026", 9);
				row.put("Q11001", 9);
				row.put("Q11002", 9);
				row.put("Q11034", 9);
				row.put("Q11035", 9);
				row.put("Q11036", 9);
				row.put("Q11075", 9);
//				+ "Q11076,Q11077,Q11039,Q11037,Q11031,Q11100,Q11101,Q11102,Q11103,Q11104,"
				row.put("Q11076", 9);
				row.put("Q11077", 9);
				row.put("Q11039", 9);
				row.put("Q11037", 9);
				row.put("Q11031", 9);
				row.put("Q11100", 9);
				row.put("Q11101", 9);
				row.put("Q11102", 9);
				row.put("Q11103", 9);
				row.put("Q11104", 9);
//				+ "Q02063,Q02064,QR07004,QR07010,QR07002,QR08009,QR12001,QR12003,QR13003,"
				row.put("Q02063", 9);
				row.put("Q02064", 9);
				row.put("QR07004", noObserStr);
				row.put("QR07010", noObserStr);
				row.put("QR07002", noObserStr);
				row.put("QR08009", noObserStr);
				row.put("QR12001", noObserStr);
				row.put("QR12003", noObserStr);
				row.put("QR13003", noObserStr);
//				+ "QR13002,QR11001,QR11002,QR11034,QR11035,QR11031) "
				row.put("QR13002", noObserStr);
				row.put("QR11001", noObserStr);
				row.put("QR11002", noObserStr);
				row.put("QR11034", noObserStr);
				row.put("QR11035", noObserStr);
				row.put("QR11031", noObserStr);
				
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
				di.setLATITUDE(String.valueOf(udua.getLatitude()));
				di.setLONGTITUDE(String.valueOf(udua.getLongtitude()));
				di.setIIiii(udua.getPlaneID());
				di.setDATA_TIME(TimeUtil.date2String(udua.getObservationTime(), "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				
	            try {
	            	if(isRevised)
	            		OTSDbHelper.getInstance().update(tablename, row);
	            	else
	            		OTSDbHelper.getInstance().insert(tablename, row);
	            	
					diQueues.offer(di);
				}  catch (Exception e) {
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
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	private static void reportInfoToDb(List<ReportInfo> reportInfos, String reptablename, Date recv_time,
			StringBuffer loggerBuffer) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		if(reportInfos != null && reportInfos.size() > 0) {
			List<Map<String, Object>> batchs = new ArrayList<Map<String, Object>>();
	        for (int i = 0; i < reportInfos.size(); i++) {
	        	Map<String, Object> row = new HashMap<String, Object>();
	        	UDUA udua = (UDUA) reportInfos.get(i).getT();
				String stat = udua.getPlaneID();
				V_TT = udua.getReportType().substring(0, 2);
//				Date oTime = udua.getObservationTime();
//				int hour = oTime.getHours();
//				oTime.setHours(((hour + 23) % 24 / 3 + 1) * 3 % 24);
//				Date dataTime = udua.getDataTime();
				Date dataTime = udua.getDataTime();
				// 修改oTime 2019-4-15
				Date oTime = new Date(dataTime.getTime());
//				oTime.setMinutes(0);
				
				String lat = String.valueOf((int)(udua.getLatitude() * 1e6));
				String lon = String.valueOf((int)(udua.getLongtitude() * 1e6));
				lat = lat.replaceAll("-", "0");
				lon = lon.replaceAll("-", "0");
//				String primkey = sdf.format(oTime)+"_" +sdf.format(dataTime)+"_"+stat+"_"+lat+"_"+lon+"_"+(int)(udua.getHeightOfStandardPressure()*1e4)+"_"+udua.getPlaneStateAndObsType()+"_"+udua.getAircraftHeightAboveSeaLevel();
				String primkey = sdf.format(dataTime)+"_"+udua.getPlaneID()+"_"+lat+"_"+lon+"_"+(int)(udua.getHeightOfStandardPressure()*1e4)+"_"+udua.getPlaneStateAndObsType();
				
				row.put("D_RECORD_ID", primkey);
				row.put("D_DATA_ID", sod_report_code);
				row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
		        row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
		        row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
		        row.put("D_DATETIME", TimeUtil.date2String(oTime,"yyyy-MM-dd HH:mm:ss"));  
				//"V_BBB,V_CCCC,V04001_02,V04002_02,V04003_02,V04004_02,V04005_02,V_TT,V_AA,V_II,"
				row.put("V_BBB", udua.getCorrectSign());
				row.put("V_CCCC", udua.getReportCenter());  // 编报中心
				row.put("V04001_02", dataTime.getYear() + 1900);
				row.put("V04002_02", dataTime.getMonth() + 1);
				row.put("V04003_02", dataTime.getDate());
				row.put("V04004_02", dataTime.getHours());
//				row.put("V04005_02", 0);;
				row.put("V04005_02", dataTime.getMinutes());
				
				row.put("V_TT", V_TT);  // 资料类型
				row.put("V_AA", udua.getReportType().substring(2, 4));
				row.put("V_II", udua.getReportType().substring(4, 6));
				//+ "V_MIMJ,V01301,V01300,V05001,V06001,V07002,V08009,V04001,V04002,V04003,"
				if(V_TT.equals("UD"))
					row.put("V_MIMJ", "AMDAR");
				else
					row.put("V_MIMJ", "AIREP");
				row.put("V01301", stat);  // 飞机标识
				row.put("V01300", 999999);
				row.put("V05001", udua.getLatitude());   // 纬度
				row.put("V06001", udua.getLongtitude()); // 经度
				row.put("V07002", udua.getHeightOfStandardPressure());
				row.put("V08009", udua.getPlaneStateAndObsType());
				row.put("V04001", dataTime.getYear() + 1900);
				row.put("V04002", dataTime.getMonth() + 1);
				row.put("V04003", dataTime.getDate());
				
				//+ "V04004,V04005,V_LEN,V_REPORT)
				row.put("V04004", dataTime.getHours());
				row.put("V04005", dataTime.getMinutes());
				row.put("V_LEN", reportInfos.get(i).getReport().length());
				row.put("V_REPORT", reportInfos.get(i).getReport());

	            batchs.add(row);
	        }
	        OTSBatchResult result = OTSDbHelper.getInstance().insert(reptablename, batchs);
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
