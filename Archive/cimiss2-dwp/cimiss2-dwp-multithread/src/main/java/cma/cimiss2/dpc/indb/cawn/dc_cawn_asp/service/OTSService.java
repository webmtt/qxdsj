package cma.cimiss2.dpc.indb.cawn.dc_cawn_asp.service;

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
import cma.cimiss2.dpc.decoder.bean.cawn.AerosolScatteringCharacteristics;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	public static BlockingQueue<StatDi> diQueues;
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	public static String v_tt ="CASP";
//	static Map<String, Object> proMap = StationInfo.getProMap();	
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}
	public static DataBaseAction insert_ots(List<AerosolScatteringCharacteristics> list, String tablename, Date recv_time, StringBuffer loggerBuffer, String fileN) {
		Map<String, Object> proMap = StationInfo.getProMap();
		if(list != null && list.size() > 0) {
			int successRowCount = list.size();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> row = new HashMap<String, Object>();
				AerosolScatteringCharacteristics asp = list.get(i);
					
				int adminCode = 999999;
				Double latitude = asp.getLatitude(); // 纬度
				Double longitude = asp.getLongitude(); // 经度
				Double height = asp.getHeightOfSationGroundAboveMeanSeaLevel(); // 测站海拔高度
				
				// 根根配置文件  获取站点基本信息
				String info = (String) proMap.get(asp.getStationNumberChina() + "+16");
				if(info == null)
					info = (String) proMap.get(asp.getStationNumberChina() + "+01");
				
				if (info==null) {
					loggerBuffer.append("\n  In configuration file, this station does not exist: " + asp.getStationNumberChina());	
				}else{
					String[] infos = info.split(",");
					if (latitude == 999999.0 && !infos[2].trim().equals("null")) {
						latitude = Double.parseDouble(infos[2]);// 纬度
						asp.setLatitude(latitude);
					}
					if(longitude == 999999.0 && !infos[1].trim().equals("null")){
						longitude = Double.parseDouble(infos[1]); // 经度
						asp.setLongitude(longitude);
					}
					if(height == 999999.0 && !infos[3].trim().equals("null")){
						height = Double.parseDouble(infos[3]); // 测站海拔高度
						asp.setHeightOfSationGroundAboveMeanSeaLevel(height);
					}
						
					if(!infos[5].trim().equals("null")){
						adminCode = Integer.parseInt(infos[5]);
					}
					
				}

//				String PrimaryKey = sdf.format(asp.getObservationTime())+"_"+asp.getStationNumberChina();
//				row.put("D_RECORD_ID", PrimaryKey);
	            row.put("D_DATA_ID", sod_code);
	            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_DATETIME", TimeUtil.date2String(asp.getObservationTime(),"yyyy-MM-dd HH:mm:ss"));
				//V01301,V01300,V05001,V06001,V07001,V_ACODE,V04001,V04002,V04003,V04004,"
	            row.put("V01301", asp.getStationNumberChina());// 区站号(字符)
				row.put("V01300", StationCodeUtil.stringToAscii(asp.getStationNumberChina()));// 区站号(数字)
				row.put("V05001",latitude);// 纬度
				row.put("V06001", longitude);// 经度
				row.put("V07001", height);// 测站高度
				row.put("V_ACODE", adminCode);//中国行政区代码
				row.put("V04001", asp.getObservationTime().getYear() + 1900);// 资料观测年
				row.put("V04002", asp.getObservationTime().getMonth()+1);// 资料观测月
				row.put("V04003", asp.getObservationTime().getDate());// 资料观测日
				row.put("V04004", asp.getObservationTime().getHours());// 资料观测时
				//"V04005,V04006,V_ITEM_CODE,V_ITIME_SIGN,V_SEARCH_SIGN,V_RECORD_SIGN,V15700_701,V12001_701,V15710,V13003_701,
				row.put("V04005", asp.getObservationTime().getMinutes());// 资料观测分
				row.put("V04006", asp.getObservationTime().getSeconds());// 资料观测分
				row.put("V_ITEM_CODE", asp.getItemCode());//项目代码
				row.put("V_ITIME_SIGN", asp.getTimeSign());//时间标记
				row.put("V_SEARCH_SIGN", asp.getSearchSign());//检索标志
				row.put("V_RECORD_SIGN", asp.getRecordType());//记录种类
				row.put("V15700_701", asp.getScatteringCoefficient_Avg());//散射系数平均值
				row.put("V12001_701", asp.getAtmosphericTemperature_Avg());//大气温度平均值
				row.put("V15710", asp.getSampleChamberTemperature_Avg());//样气室温度平均值
				row.put("V13003_701", asp.getRelativeHumidity_Avg());//相对湿度平均值
				//V10004_701,V15720,V15721,V15722,V15723,V_M_STATE,V_D_STATE,V15800)
				row.put("V10004_701", asp.getAtmosphericPressure_Avg());//大气压平均值
				row.put("V15720", asp.getDarkCountDiagnosis());//暗计数诊断
				row.put("V15721", asp.getShutterCountDiagnosis());//快门计数诊断
				row.put("V15722", asp.getMeasurementRatioDiagnosis());//测量比率诊断
				row.put("V15723", asp.getFinalTestRatioDiagnosis());//最后测试比率诊断
				row.put("V_M_STATE", asp.getM_STATE());//V_M_STATE,
				row.put("V_D_STATE", asp.getD_STATE());//V_D_STATE
				row.put("V15800", asp.getDataRecordFrequency());//数据记录频率
				row.put("V_BBB", "000");
				row.put("D_SOURCE_ID", cts_code);
				
				row.put("V_Scat_T_450nm", asp.getScatTotal_450nm());
				row.put("V_Scat_T_525nm", asp.getScatTotal_525nm());
				row.put("V_Scat_T_635nm", asp.getScatTotal_635nm());
				row.put("V_Scat_B_450nm", asp.getScatBefore_450nm());
				row.put("V_Scat_B_525nm", asp.getScatBefore_525nm());
				row.put("V_Scat_B_635nm", asp.getScatBefore_635nm());
				row.put("V_Scat_A_450nm", asp.getScatAfter_450nm());
				row.put("V_Scat_A_525nm", asp.getScatAfter_525nm());
				row.put("V_Scat_A_635nm", asp.getScatAfter_635nm());
				
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
				di.setIIiii(asp.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(asp.getObservationTime(), "yyyy-MM-dd hh:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());
				di.setLATITUDE(String.valueOf(asp.getLatitude()));
				di.setLONGTITUDE(String.valueOf(asp.getLongitude()));
				
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
