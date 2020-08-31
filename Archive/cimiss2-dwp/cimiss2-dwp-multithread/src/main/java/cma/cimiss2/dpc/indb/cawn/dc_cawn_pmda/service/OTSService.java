package cma.cimiss2.dpc.indb.cawn.dc_cawn_pmda.service;

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
import cma.cimiss2.dpc.decoder.bean.cawn.pmDA;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static BlockingQueue<StatDi> diQueues;
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	public static String v_tt = "PMDA";
	public int defaultInt = 999999;	
//	static Map<String, Object> proMap = StationInfo.getProMap();	

	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}
	public static DataBaseAction insert_ots(List<pmDA> list, String tablename, Date recv_time, StringBuffer loggerBuffer, String fileN) {
		Map<String, Object> proMap = StationInfo.getProMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");	
		if(list != null && list.size() > 0){
			int successRowCount = list.size();
			Date oTime = new Date();
			for(int i = 0; i<list.size();i++){
				Map<String, Object> row = new HashMap<String, Object>();
				pmDA pmDA = list.get(i);
				// 根据站号查询行政区划代码
				String info = (String) proMap.get(pmDA.getStationNumberChina() + "+16");
				if (info == null) {
					// 如果此站号不存在，执行下一个数据
					loggerBuffer.append("\n In configuration file, this station does not exist: " + pmDA.getStationNumberChina());
				} else {
					String[] infos = info.split(",");
					if(!infos[2].equals("null")){
						try{
							Double latitude = Double.parseDouble(infos[2]);// 经度
							pmDA.setLatitude(latitude);
						}catch (Exception e) {
							loggerBuffer.append("\n In configuration file, " + "get latitude of " + pmDA.getStationNumberChina() + " error!");
						}
					}
					if(!infos[1].equals("null")){
						try{
							Double longitude = Double.parseDouble(infos[1]); // 纬度
							pmDA.setLongitude(longitude);
						}catch (Exception e) {
							loggerBuffer.append("\n In configuration file, " + "get longititude of " + pmDA.getStationNumberChina() + " error!");
						}
					}
					if(!infos[3].equals("null")){
						try{
							Double height = Double.parseDouble(infos[3]);//测站高度
							pmDA.setHeightOfSationGroundAboveMeanSeaLevel(height);
						}catch (Exception e) {
							loggerBuffer.append("\n In configuration file, " + "get station height of " + pmDA.getStationNumberChina() + " error!");
						}
					}
				}
//				String PrimaryKey = sdf.format(pmDA.getObservationTime())+"_"+pmDA.getStationNumberChina();
				oTime = pmDA.getObservationTime();
//				row.put("D_RECORD_ID", PrimaryKey);
	            row.put("D_DATA_ID", sod_code);
	            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_DATETIME", TimeUtil.date2String(oTime,"yyyy-MM-dd HH:mm:ss"));
				
	            //V01301,V01300,V05001,V06001,V07001,V04001,V04002,V04003,V_PM10_201_005,V_PM10_201_030"
	            row.put("V01301", pmDA.getStationNumberChina());// 区站号(字符)
				row.put("V01300", StationCodeUtil.stringToAscii(pmDA.getStationNumberChina()));// 区站号(数字)
				row.put("V05001", pmDA.getLatitude());// 纬度
				row.put("V06001", pmDA.getLongitude());// 经度
				row.put("V07001", pmDA.getHeightOfSationGroundAboveMeanSeaLevel());// 测站高度
				row.put("V04001", pmDA.getObservationTime().getYear() + 1900);// 资料观测年
				row.put("V04002", pmDA.getObservationTime().getMonth()+1);// 资料观测月
				row.put("V04003", pmDA.getObservationTime().getDate());// 资料观测日
				row.put("V_PM10_201_005", pmDA.getPM10_5min_AVG());// 5分钟观测PM10日均值
				row.put("V_PM10_201_030", pmDA.getPM10_30min_AVG());// 30分钟观测PM10日均值
				//"V_PM10_201_101,V_PM10_201_124,V_PM25_201_005,V_PM25_201_101,V_PM25_201_124,V_PM01_201_101,V_PM10_201_005_076,V_PM10_201_030_076,V_PM10_201_101_076,V_PM10_201_124_076
				row.put("V_PM10_201_101", pmDA.getPM10_1h_AVG());// 1小时观测PM10日均值
				row.put("V_PM10_201_124", pmDA.getPM10_24h_AVG());// 24小时观测PM10日均值
				row.put("V_PM25_201_005", pmDA.getPM2p5_5min_AVG());// 5分钟观测PM2.5日均值
				row.put("V_PM25_201_101", pmDA.getPM2p5_1h_AVG());// 1小时观测PM2.5日均值
				row.put("V_PM25_201_124", pmDA.getPM2p5_24h_AVG());// 24小时观测PM2.5日均值
				row.put("V_PM01_201_101", pmDA.getPM1_1h_AVG());// 1小时观测PM1日均值
				row.put("V_PM10_201_005_076", pmDA.getPM10_5min_AVG_COUNT());// 5分钟观测PM10日均值每日内有效小时样本数
				row.put("V_PM10_201_030_076", pmDA.getPM10_30min_AVG_COUNT());// 30分钟观测PM10日均值每日内有效小时样本数
				row.put("V_PM10_201_101_076", pmDA.getPM10_1h_AVG_COUNT());// 1小时观测PM10日均值每日内有效小时样本数
				row.put("V_PM10_201_124_076", pmDA.getPM10_24h_AVG_COUNT());// 24小时观测PM10日均值每日内有效小时样本数
				//"V_PM25_201_005_076,V_PM25_201_101_076,V_PM25_201_124_076,V_PM01_201_101_076
				row.put("V_PM25_201_005_076", pmDA.getPM2p5_5min_AVG_COUNT());//5分钟观测PM25日均值每日内有效小时样本数
				row.put("V_PM25_201_101_076", pmDA.getPM2p5_1h_AVG_COUNT());//1小时观测PM25日均值每日内有效小时样本数1
				row.put("V_PM25_201_124_076", pmDA.getPM2p5_24h_AVG_COUNT());//24小时观测PM25日均值每日内有效小时样本数1
				row.put("V_PM01_201_101_076", pmDA.getPM1_1h_AVG_COUNT());//1小时观测PM1每日内有效小时的样本数
				row.put("V_BBB", "000");
				row.put("D_SOURCE_ID", cts_code);
				
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
				di.setIIiii(list.get(i).getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(pmDA.getObservationTime(), "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				di.setLATITUDE(String.valueOf(pmDA.getLatitude()));
				di.setLONGTITUDE(String.valueOf(pmDA.getLongitude()));
				
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
