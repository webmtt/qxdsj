package cma.cimiss2.dpc.indb.sevp.dpc_sevp_typh_fk.service;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.sevp.Typh;
import cma.cimiss2.dpc.decoder.bean.sevp.TyphEle;
import cma.cimiss2.dpc.decoder.bean.sevp.TyphKey;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static BlockingQueue<StatDi> diQueues;
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	public static String sod_report_code = StartConfig.reportSodCode();
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}
	public static DataBaseAction processSuccessReport(List<Typh> typhs, Date recv_time, String cts_code, String fileN, StringBuffer loggerBuffer) {
		int successRowCount = 0;
		int rowCount = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		TyphKey typhKey = null;
		for(int i = 0; i < typhs.size(); i++){
			Typh typh = typhs.get(i);
			typhKey = typh.getTyphKey();
			Date oTime = typhKey.getObservationTime();
			String primkey = sdf.format(oTime)+"_"+typhKey.getTyphName()+"_"+typhKey.getReportCenter()+"_"+typhKey.getProductType();
			
			List<TyphEle> typhEles = typh.getTyphEles();;
			// 2019-03-11 反馈：不做更正
//			boolean isRevised = false; 
//			String v_bbb = typhKey.getV_BBB();
//			if(v_bbb.compareTo("000") > 0)
//				isRevised = true;  
			oTime = typhKey.getObservationTime();
			successRowCount += typhEles.size();
			rowCount += typhEles.size();
			for(int j = 0; j < typhEles.size(); j ++){
				TyphEle typhEle = typhEles.get(j);
				String primkeyEle = primkey +"_"+typhEle.getForecastEfficiency();
				StatDi di = new StatDi();
				try {
					Map<String, Object> row = new HashMap<String, Object>();
					row.put("D_ELE_ID", primkeyEle);
					row.put("D_RECORD_ID", primkey);
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
					row.put("D_DATETIME", TimeUtil.date2String(typhKey.getObservationTime(),"yyyy-MM-dd HH:mm:ss"));
					// "V04320,V05001,V06001,V10004,V11041,V11320,V11410_07_01,V11411_07_01,V11410_07_02,V11411_07_02,"
					row.put("V04320", typhEle.getForecastEfficiency());  // 预报时效
					row.put("V05001", typhEle.getLatitude());
					row.put("V06001", typhEle.getLongtitude());
					row.put("V10004", typhEle.getCenterPressure());
					row.put("V11041", typhEle.getGustSpeed()); // 中心阵风
					row.put("V11320", typhEle.getMaxSustainedWind()); // 中心附近最大阵风风速
					row.put("V11410_07_01", typhEle.getAzimuthOfWindBeyondL7_01());
					row.put("V11411_07_01", typhEle.getWindCircleRadiusL7_01());
					row.put("V11410_07_02", typhEle.getAzimuthOfWindBeyondL7_02());
					row.put("V11411_07_02", typhEle.getWindCircleRadiusL7_02());
					// "V11410_07_03,V11411_07_03,V11410_07_04,V11411_07_04,V11410_10_01,V11411_10_01,"
					row.put("V11410_07_03", typhEle.getAzimuthOfWindBeyondL7_03());
					row.put("V11411_07_03", typhEle.getWindCircleRadiusL7_03());
					row.put("V11410_07_04", typhEle.getAzimuthOfWindBeyondL7_04());
					row.put("V11411_07_04", typhEle.getWindCircleRadiusL7_04());
					row.put("V11410_10_01", typhEle.getAzimuthOfWindBeyondL10_01());
					row.put("V11411_10_01", typhEle.getWindCircleRadiusL10_01());
					// "V11410_10_02,V11411_10_02,V11410_10_03,V11411_10_03,V11410_10_04,V11411_10_04,"
					row.put("V11410_10_02", typhEle.getAzimuthOfWindBeyondL10_02());
					row.put("V11411_10_02", typhEle.getWindCircleRadiusL10_02());
					row.put("V11410_10_03", typhEle.getAzimuthOfWindBeyondL10_03());
					row.put("V11411_10_03", typhEle.getWindCircleRadiusL10_03());
					row.put("V11410_10_04", typhEle.getAzimuthOfWindBeyondL10_04());
					row.put("V11411_10_04", typhEle.getWindCircleRadiusL10_04());
					// "V11410_12_01,V11411_12_01,V11411_12_02,V11410_12_02,V11410_12_03,V11411_12_03,"
					row.put("V11410_12_01", typhEle.getAzimuthOfWindBeyondL12_01());
					row.put("V11411_12_01", typhEle.getWindCircleRadiusL12_01());
					row.put("V11411_12_02", typhEle.getWindCircleRadiusL12_02());
					row.put("V11410_12_02", typhEle.getAzimuthOfWindBeyondL12_02());
					row.put("V11410_12_03", typhEle.getAzimuthOfWindBeyondL12_03());
					row.put("V11411_12_03", typhEle.getWindCircleRadiusL12_03());
					// "V11410_12_04,V11411_12_04,V19301,V19302,V19303,V19304,V_BBB) "
					row.put("V11410_12_04", typhEle.getAzimuthOfWindBeyondL12_04());
					row.put("V11411_12_04", typhEle.getWindCircleRadiusL12_04());
					row.put("V19301", typhEle.getMovingDir());
					row.put("V19302", typhEle.getMovingSpeed());
					row.put("V19303", typhEle.getTyphIntensity());
					row.put("V19304", typhEle.getTrend());
					row.put("V_BBB", typhKey.getV_BBB());
					row.put("D_SOURCE_ID", cts_code);
				
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(sod_code);
					di.setDATA_TYPE_1(cts_code);
					di.setTT(typhKey.getV_TT());			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败	
					di.setIIiii(typhKey.getTyphName());
					di.setDATA_TIME(TimeUtil.date2String(oTime, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLATITUDE(String.valueOf(typhEle.getLatitude()));
					di.setLONGTITUDE(String.valueOf(typhEle.getLongtitude()));
//					if(isRevised) {
//						OTSDbHelper.getInstance().update(StartConfig.valueTable(), "D_ELE_ID", row);
//					}else {
//						OTSDbHelper.getInstance().insert(StartConfig.valueTable(), "D_ELE_ID", row);
//					}
					// 2019-3-11 根据反馈：不做更正
					OTSDbHelper.getInstance().insert(StartConfig.valueTable(), "D_ELE_ID", row);
					
					diQueues.offer(di);
				} catch (Exception e) {
					loggerBuffer.append(e.getMessage());
					di.setPROCESS_STATE("1");
					successRowCount = successRowCount -1;
					diQueues.offer(di);
				}
			}
			Map<String, Object> rowk = new HashMap<String, Object>();
			try{
				rowk.put("D_RECORD_ID", primkey);
				rowk.put("D_DATA_ID", sod_code);
				rowk.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
				rowk.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
				rowk.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
				rowk.put("D_DATETIME", TimeUtil.date2String(typhKey.getObservationTime(), "yyyy-MM-dd HH:mm:ss"));
				
				//"V_BBB,V04001_02,V04002_02,V04003_02,V04004_02,V_CCCC,V01035,V_TYPH_NAME,V01333,V01398,"
				rowk.put("V_BBB", typhKey.getV_BBB());
				rowk.put("V04001_02", oTime.getYear() + 1900);
				rowk.put("V04002_02", oTime.getMonth() + 1);
				rowk.put("V04003_02", oTime.getDate());
				rowk.put("V04004_02", oTime.getHours());
				rowk.put("V_CCCC", typhKey.getReportCenter());
				rowk.put("V01035", typhKey.getReportCenterCode());
				rowk.put("V_TYPH_NAME", typhKey.getTyphName());
				rowk.put("V01333", typhKey.getTyphLevel());
				rowk.put("V01398", typhKey.getProductType());
				//+ "V04001,V04002,V04003,V04004,V04005,V01330,V01332,V04320_041)
				rowk.put("V04001", oTime.getYear() + 1900);
				rowk.put("V04002", oTime.getMonth() + 1);
				rowk.put("V04003", oTime.getDate());
				rowk.put("V04004", oTime.getHours());
				rowk.put("V04005", oTime.getMinutes());
				rowk.put("V01330", typhKey.getInternalCode());
				rowk.put("V01332", typhKey.getInternationalCode());
				rowk.put("V04320_041", typhKey.getNumOfForecastEfficiency());
				rowk.put("D_SOURCE_ID", cts_code);
				
				
//				if(isRevised) {
//					OTSDbHelper.getInstance().update(StartConfig.keyTable(), rowk);
//				}else {
//					OTSDbHelper.getInstance().insert(StartConfig.keyTable(), rowk);
//				}
				// 2019-3-11 根据反馈：不做更正
				OTSDbHelper.getInstance().insert(StartConfig.keyTable(), rowk);
			} catch (Exception e) {
				loggerBuffer.append(e.getMessage());
			}
		}
		loggerBuffer.append(" INSERT/UPDATE SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
	    loggerBuffer.append(" INSERT/UPDATE SUCCESS COUNT : " + successRowCount + "\n");
	    loggerBuffer.append(" INSERT/UPDATE FAILED COUNT : " + (rowCount - successRowCount) + "\n");
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
	public static void reportInfoToDb( List<ReportInfo> reportInfos, Date recv_time, StringBuffer loggerBuffer) {
		String tablename = StartConfig.reportTable();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String V_TT= "";
		Typh typh = null;
		TyphKey typhKey = null;
		Date oTime = null;
		String primkey = null;
		List<Map<String, Object>> batchs = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < reportInfos.size(); i++) {
			Map<String, Object> row = new HashMap<String, Object>();
			typh = (Typh) reportInfos.get(i).getT();
			typhKey = typh.getTyphKey();
			V_TT = typhKey.getV_TT();
			oTime = typhKey.getObservationTime();
			primkey = sdf.format(oTime)+"_"+V_TT+"_"+typhKey.getTyphName()+"_"+typhKey.getReportCenter()+"_"+typhKey.getProductType() + "_" + typhKey.getV_BBB();
			row.put("D_RECORD_ID", primkey);
            row.put("D_DATA_ID", sod_report_code);
            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
            row.put("D_DATETIME", TimeUtil.date2String(oTime,"yyyy-MM-dd HH:mm:ss"));
			
			// "V_BBB,V_CCCC,V04001_01,V04002_01,V04003_01,V04004_01,V04005_01,V_TT,V_AA,V_II,"
			row.put("V_BBB", typhKey.getV_BBB());
			row.put("V_CCCC", typhKey.getReportCenter());  // 编报中心
			row.put("V04001_01", oTime.getYear() + 1900);
			row.put("V04002_01", oTime.getMonth() + 1);
			row.put("V04003_01", oTime.getDate());
			row.put("V04004_01", oTime.getHours());
			row.put("V04005_01", oTime.getMinutes());
			row.put("V_TT", V_TT);  // 资料类型
			row.put("V_AA", typhKey.getV_AA());
			row.put("V_II", typhKey.getV_II());
			// "V_LEN,V_BULLETIN_T) 
			row.put("V_LEN", reportInfos.get(i).getReport().length());
			row.put("V_BULLETIN_T", reportInfos.get(i).getReport());
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
