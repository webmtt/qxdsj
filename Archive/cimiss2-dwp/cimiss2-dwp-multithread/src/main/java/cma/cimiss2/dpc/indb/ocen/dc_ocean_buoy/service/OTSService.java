package cma.cimiss2.dpc.indb.ocen.dc_ocean_buoy.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Pattern;

import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.ots.OTSBatchResult;
import org.cimiss2.dwp.tools.ots.OTSDbHelper;
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.cimiss2.dwp.tools.utils.TimeUtil;

import com.alicloud.openservices.tablestore.ClientException;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ocean.Buoy;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
/**
 * -------------------------------------------------------------------------------
 * <br>
 * @Title:  OTSService.java   
 * @Package cma.cimiss2.dpc.indb.ocen.buoy.service   
 * @Description:    TODO(中国浮标站观测资料入OTS处理类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年6月7日 上午7:49:50   wuzuoqiang    Initial creation.
 * </pre>
 * 
 * @author wuzuoqiang
 *---------------------------------------------------------------------------------
 */
public class OTSService {
	// cts 四级编码
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	public static String sod_report = StartConfig.reportSodCode();
	
	public static String V_TT = "BUOY";
	public static String V_BBB = "000";
	public static String V_CCCC = "9999";
	public static int defaultInt = 999999;
	
	public static BlockingQueue<StatDi> diQueues;
	
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}

	/**
	 * @Title: insert_ots 
	 * @Description: TODO(要素入库处理函数) 
	 * @param parseResult 解码结果集
	 * @param tablename 数据库表名
	 * @param recv_time 消息接收时间
	 * @param loggerBuffer  
	 * @param fileN  文件名
	 * @return DataBaseAction 返回处理状态
	 * @throws:
	 */
	@SuppressWarnings("deprecation")
	public static DataBaseAction insert_ots(ParseResult<Buoy> parseResult, String tablename, Date recv_time,
			StringBuffer loggerBuffer, String fileN) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		// 获取解码实体类结果集
		List<Buoy> list = parseResult.getData();
		
		if(list != null && list.size() > 0) {
			int successRowCount = list.size();
	        for (int i = 0; i < list.size(); i++) {
	        	Buoy buoy = list.get(i);
	            Map<String, Object> row = new HashMap<String, Object>();
	            int stationNumberN = defaultInt;
				String stat = buoy.getStationNumberChina();
				if (Pattern.matches("\\d{5}", stat))
					stationNumberN = Integer.parseInt(stat);
				else
					stationNumberN = Integer.parseInt(StationCodeUtil.stringToAscii(stat));
				
				String lat = String.valueOf((int)(buoy.getLatitude() * 1e6));
				String lon = String.valueOf((int)(buoy.getLongtitude() * 1e6));
				lat = lat.replaceAll("-", "0");
				lon = lon.replaceAll("-", "0");
				Date date = buoy.getObservationTime();
				row.put("D_RECORD_ID", sdf.format(date)+"_"+stat+"_"+lat + "_"+lon);
				row.put("D_DATA_ID", sod_code);
				row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
		        row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
		        Date newDate = new Date(date.getTime());
		        newDate.setMinutes(0);
		        newDate.setSeconds(0);
		        row.put("D_DATETIME", TimeUtil.date2String(newDate,"yyyy-MM-dd HH:mm:ss"));
		        row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
				// "V01301,V01300,V05001,V06001,V07001,V07002,V07301,V07302,V07303,V02001,"
				row.put("V01301", stat);
				row.put("V01300", stationNumberN);
				row.put("V05001", buoy.getLatitude());
				row.put("V06001", buoy.getLongtitude());
				row.put("V07001", buoy.getHeightOfStationGroundAboveMeanSeaLevel());
				row.put("V07002", defaultInt);
				row.put("V07301", defaultInt);
				row.put("V07302", buoy.getDepthOfSeasaltSensor());
				row.put("V07303", buoy.getHeightOfWaveHeightSensor());
				row.put("V02001", buoy.getStationType());
				// + "V02320,V04001,V04002,V04003,V04004,V04005,V04006,V22400,V22049,V22300,"
				row.put("V02320", buoy.getCollectorType());
				row.put("V04001", date.getYear() + 1900);
				row.put("V04002", date.getMonth() + 1);
				row.put("V04003", date.getDate());
				row.put("V04004", date.getHours());
				row.put("V04005", date.getMinutes());
				row.put("V04006", date.getSeconds());
				row.put("V22400", buoy.getBuoyDir()); //浮标方位
				row.put("V22049", buoy.getSeaTemperature()); //海表温度
				row.put("V22300", buoy.getMaxSeaTemperature());//海表最高w温度
				// + "V22300_052,V22301,V22301_052,V22380,V22380_061,V22381,V22381_061,V22073,V22073_052,V22073_061,"
				row.put("V22300_052", buoy.getTimeOfmaxSeaTemperature()); // 
				row.put("V22301", buoy.getMinSeaTemperature());//
				row.put("V22301_052", buoy.getTimeOfMinSeaTemperature());//
				row.put("V22380", buoy.getSignificantWaveHeight());//
				row.put("V22380_061", buoy.getCycleOfSignificantWaveHeight());//
				row.put("V22381", buoy.getAvgWaveHeight());//
				row.put("V22381_061", buoy.getCycleOfAvgWave());//
				row.put("V22073", buoy.getMaxWaveHeight());//
				row.put("V22073_052", buoy.getTimeOfMaxWaveHeight());//
				row.put("V22073_061", buoy.getCycleOfMaxWaveHeight());//
				// 	+ "V22385,V22386,V22062,V22062_701,V22306,V22306_701,V22310,V22310_701,V22311,V22311_701,"
				row.put("V22385", buoy.getCurrentVelocity());//表层海洋面波速
				row.put("V22386", buoy.getCurrentDir());//表层海洋面波向
				row.put("V22062", buoy.getSalinity());//海水盐度
				row.put("V22062_701", buoy.getAvgSalinity());//海水平均盐度
				row.put("V22306", buoy.getElectricalConductivity());//海水电导率
				row.put("V22306_701", buoy.getAvgElectricalConductivity());//海水平均电导率
				row.put("V22310", buoy.getSeaTurbidity());//海水浊度
				row.put("V22310_701", buoy.getAvgSeaTurbidity());//海水平均浊度
				row.put("V22311", buoy.getChlorophyllConcentration());//海水叶绿素浓度
				row.put("V22311_701", buoy.getAvgChlorophyllConcentration());//海水平均叶绿素浓度
				row.put("V_BBB", V_BBB);
				// 构造di信息
				StatDi di = new StatDi();					
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(sod_code);
				di.setDATA_TYPE_1(cts_code);
			
				di.setTT("BUOY");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("0"); //0成功，1失败
				di.setPROCESS_STATE("0");  //0成功，1失败	
				di.setLATITUDE(buoy.getLatitude().toString());
				di.setLONGTITUDE(String.valueOf(buoy.getLongtitude()));
				di.setIIiii(buoy.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(buoy.getObservationTime(), "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
					
		        try {
		            	
					OTSDbHelper.getInstance().insert(tablename, row);
					diQueues.offer(di);
				}  catch (Exception e) {
					successRowCount = successRowCount -1;
					di.setPROCESS_STATE("1");
					diQueues.offer(di);
					loggerBuffer.append(row);
					loggerBuffer.append(e.getMessage());
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
	public static void reportInfoToDb(ParseResult<Buoy> parseResult, String reptablename, Date recv_time,
			StringBuffer loggerBuffer, String fileN) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		loggerBuffer.append("start process report ........");
		List<ReportInfo> reportInfos = parseResult.getReports();
		if(reportInfos != null && reportInfos.size() > 0) {
			List<Map<String, Object>> batchs = new ArrayList<Map<String, Object>>();
	        for (int i = 0; i < reportInfos.size(); i++) {
	        	Map<String, Object> row = new HashMap<String, Object>();
	        	Buoy buoy = (Buoy) reportInfos.get(i).getT();
				String stat = buoy.getStationNumberChina();
				Date date = buoy.getObservationTime();
				String primkey = sdf.format(date)+"_"+stat+"_"+V_TT+"_"+ V_BBB;
				row.put("D_RECORD_ID", primkey);
				row.put("D_DATA_ID", sod_report);
				row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
		        row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
		        row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
		        row.put("D_DATETIME", TimeUtil.date2String(date,"yyyy-MM-dd HH:mm:ss"));
				row.put("V_BBB", V_BBB);
				row.put("V_CCCC", V_CCCC);
				row.put("V_TT", V_TT);
				row.put("V01301", stat);
				row.put("V05001", buoy.getLatitude());
				row.put("V06001", buoy.getLongtitude());
				row.put("V04001", date.getYear() + 1900);
				row.put("V04002", date.getMonth() + 1);
				row.put("V04003", date.getDate());
				row.put("V04004", date.getHours());
				row.put("V04005", date.getMinutes());
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
