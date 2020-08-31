package cma.cimiss2.dpc.indb.cawn.dc_cawn_nsd.service;

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
import cma.cimiss2.dpc.decoder.bean.cawn.AtmosphericCompositionAerosolConcentration;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {
	public static BlockingQueue<StatDi> diQueues;
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
//    public static Map<String, Object> proMap = StationInfo.getProMap();
    public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}
	static String v_tt = "NSD";
	public static DataBaseAction insert_ots(List<AtmosphericCompositionAerosolConcentration> list, String tablename, Date recv_time, StringBuffer loggerBuffer, String fileN) {
		Map<String, Object> proMap = StationInfo.getProMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		if(list != null && list.size() > 0) {
			int successRowCount = list.size();
			for(int i = 0; i < list.size(); i ++){
				Map<String, Object> row = new HashMap<String, Object>();
				AtmosphericCompositionAerosolConcentration nsd = list.get(i);
				// 根根配置文件  获取站点基本信息
				String info = (String) proMap.get(nsd.getStationNumberChina() + "+16");
				int adminCode = 999999;
				double lon =999999;
				double lat =999999;
				double height =999999;
				if(info == null)
					info = (String) proMap.get(nsd.getStationNumberChina() + "+01");
				
				if(info == null) {
					loggerBuffer.append("\n  In configuration file, this station does not exist: " + nsd.getStationNumberChina());		
				}else{
					String[] infos = info.split(",");
					if(!(infos[5].trim().equals("null") || infos[5].trim().equals(""))){
						adminCode = Integer.parseInt(infos[5]);
					}	
					if(!(infos[1].trim().equals("null") || infos[1].trim().equals(""))){
						lon = Double.parseDouble(infos[1]);
						nsd.setLongitude(lon);
					}
					if(!(infos[2].trim().equals("null") || infos[2].trim().equals(""))){
						lat = Double.parseDouble(infos[2]);
						nsd.setLatitude(lat);
					}
					if(!(infos[3].trim().equals("null") || infos[3].trim().equals(""))){
						height = Double.parseDouble(infos[3]);
					}
				}
//				String PrimaryKey = sdf.format(nsd.getObservationTime())+"_"+nsd.getStationNumberChina();
//				row.put("D_RECORD_ID", PrimaryKey);
	            row.put("D_DATA_ID", sod_code);
	            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_DATETIME", TimeUtil.date2String(nsd.getObservationTime(),"yyyy-MM-dd HH:mm:ss"));
	            //V01301,V01300,V05001,V06001,V07001,V_ACODE,V04001,V04002,V04003,V04004
				row.put("V01301", nsd.getStationNumberChina());// 区站号(字符)
				row.put("V01300", StationCodeUtil.stringToAscii(nsd.getStationNumberChina()));// 区站号(数字)
				row.put("V05001",lat);// 纬度
				row.put("V06001", lon);// 经度
				row.put("V07001", height);// 测站高度
				row.put("V_ACODE", adminCode);//中国行政区代码
				row.put("V04001", nsd.getObservationTime().getYear() + 1900);// 资料观测年
				row.put("V04002", nsd.getObservationTime().getMonth()+1);// 资料观测月
				row.put("V04003", nsd.getObservationTime().getDate());// 资料观测日
				row.put("V04004", nsd.getObservationTime().getHours());// 资料观测时
				//V04005,V04006,V_ITEM_CODE,V_STORAGE_PLACE,V_WEIGHT_FACTOR,V_ERROR_CODE,V15752,V15765,V_CORRECT_COUNT,V_PCOUNT,"
				row.put("V04005", nsd.getObservationTime().getMinutes());// 资料观测分
				row.put("V04006", nsd.getObservationTime().getSeconds());// 资料观测分
				row.put("V_ITEM_CODE", nsd.getItemCode());//项目代码
				row.put("V_STORAGE_PLACE", nsd.getStoragePlace());//存储位置
				row.put("V_WEIGHT_FACTOR", nsd.getWeightFactor());//重量因数
				row.put("V_ERROR_CODE", nsd.getErrorCode());//错误代码
				row.put("V15752", nsd.getBatteryVoltageCode());//电池电压代码
				row.put("V15765", nsd.getValveCurrent());//阀电流
				row.put("V_CORRECT_COUNT", nsd.getCorrectCount());//综合订正计数
				row.put("V_PCOUNT", nsd.getPressureCount());//气压计数
				//V_BACKUP1,V12001_040,V13003_040,V_TIME_SPACING,V11002_071,V11001_071,V13011_071,V12001_074,V13003_074,V10004_074,
				row.put("V_BACKUP1", nsd.getBackUp1());//备用1
				row.put("V12001_040", nsd.getTemperatureCount());//温度计数
				row.put("V13003_040", nsd.getHumidityCount());//湿度计数
				row.put("V_TIME_SPACING", nsd.getTimeInterval());//时间间隔
				row.put("V11002_071", nsd.getWindSpeedMeteringFactor());//风速计量因子
				row.put("V11001_071", nsd.getWindDirectionMeteringFactor());//风向计量因子
				row.put("V13011_071", nsd.getPrecipitationMeteringFactor());//降水计量因子
				row.put("V12001_074", nsd.getTemperatureSlopeCorrect());//温度斜率订正
				row.put("V13003_074", nsd.getHumiditySlopeCorrect());//湿度斜率订正
				row.put("V10004_074", nsd.getPressureSlopeCorrect());//气压斜率订正
				//V12001_075,V13003_075,V10004_075,V11450,V11451,V02440,V10004,V_BACKUP2,V13003,V12001,
				row.put("V12001_075", nsd.getTemperatureBiasCorrect());//温度偏移订正
				row.put("V13003_075", nsd.getHumidityBiasCorrect());//湿度偏移订正
				row.put("V10004_075", nsd.getPressureBiasCorrect());//气压偏移订正
				row.put("V11450", nsd.getWindSpeedSensitivity());//风速灵敏度
				row.put("V11451", nsd.getWindDirectionDip());//风速 倾角
				row.put("V02440",nsd.getPrecipitationSensorCorrectFactor());//降水传感器订正因子
				row.put("V10004", nsd.getAirPressure());//气压
				row.put("V_BACKUP2", nsd.getBackUp2());//备用2
				row.put("V13003", nsd.getHumidity());//湿度
				row.put("V12001", nsd.getTemperature());//温度
				 //V11002,V11001,V13011,V15023_C01,V15023_C02,V15023_C03,V15023_C04,V15023_C05,V15023_C06,V15023_C07
				row.put("V11002", nsd.getWindSpeed());//风速
				row.put("V11001", nsd.getWindDirection());//风向
				row.put("V13011", nsd.getPrecipitation());//降水
				row.put("V15023_C01", nsd.getChannelConcentration_C1());//C1通道数浓度
				row.put("V15023_C02", nsd.getChannelConcentration_C2());//C2通道数浓度
				row.put("V15023_C03", nsd.getChannelConcentration_C3());//C3通道数浓度
				row.put("V15023_C04", nsd.getChannelConcentration_C4());//C4通道数浓度
				row.put("V15023_C05", nsd.getChannelConcentration_C5());//C5通道数浓度
				row.put("V15023_C06", nsd.getChannelConcentration_C6());//C6通道数浓度
				row.put("V15023_C07", nsd.getChannelConcentration_C7());//C7通道数浓度
				//V15023_C08,V15023_C09,V15023_C10,V15023_C11,V15023_C12,V15023_C13,V15023_C14,V15023_C15,V15023_C16,V15023_C17
				row.put("V15023_C08", nsd.getChannelConcentration_C8());//C8通道数浓度
				row.put("V15023_C09", nsd.getChannelConcentration_C9());//C9通道数浓度
				row.put("V15023_C10", nsd.getChannelConcentration_C10());//C10通道数浓度
				row.put("V15023_C11", nsd.getChannelConcentration_C11());//C11通道数浓度
				row.put("V15023_C12", nsd.getChannelConcentration_C12());//C12通道数浓度
				row.put("V15023_C13", nsd.getChannelConcentration_C13());//C13通道数浓度
				row.put("V15023_C14", nsd.getChannelConcentration_C14());//C14通道数浓度
				row.put("V15023_C15", nsd.getChannelConcentration_C15());//C15通道数浓度
				row.put("V15023_C16", nsd.getChannelConcentration_C16());//C16通道数浓度
				row.put("V15023_C17", nsd.getChannelConcentration_C17());//C17通道数浓度
				//V15023_C18,V15023_C19,V15023_C20,V15023_C21,V15023_C22,V15023_C23,V15023_C24,V15023_C25,V15023_C26,V15023_C27
				row.put("V15023_C18", nsd.getChannelConcentration_C18());//C18通道数浓度
				row.put("V15023_C19", nsd.getChannelConcentration_C19());//C19通道数浓度
				row.put("V15023_C20", nsd.getChannelConcentration_C20());//C20通道数浓度
				row.put("V15023_C21", nsd.getChannelConcentration_C21());//C21通道数浓度
				row.put("V15023_C22", nsd.getChannelConcentration_C22());//C22通道数浓度
				row.put("V15023_C23", nsd.getChannelConcentration_C23());//C23通道数浓度
				row.put("V15023_C24", nsd.getChannelConcentration_C24());//C24通道数浓度
				row.put("V15023_C25", nsd.getChannelConcentration_C25());//C25通道数浓度
				row.put("V15023_C26", nsd.getChannelConcentration_C26());//C26通道数浓度
				row.put("V15023_C27", nsd.getChannelConcentration_C27());//C27通道数浓度
				//V15023_C28,V15023_C29,V15023_C30,V15023_C31,V15023_C32
				row.put("V15023_C28", nsd.getChannelConcentration_C28());//C28通道数浓度
				row.put("V15023_C29", nsd.getChannelConcentration_C29());//C29通道数浓度
				row.put("V15023_C30", nsd.getChannelConcentration_C30());//C30通道数浓度
				row.put("V15023_C31", nsd.getChannelConcentration_C31());//C31通道数浓度
				row.put("V15023_C32", nsd.getChannelConcentration_C32());//C32通道数浓度
				row.put("V_BBB", "000");
				row.put("D_SOURCE_ID", cts_code);
				
				// 设置DI信息
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
				di.setIIiii(nsd.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(nsd.getObservationTime(), "yyyy-MM-dd hh:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());
				di.setLATITUDE(String.valueOf(nsd.getLatitude()));
				di.setLONGTITUDE(String.valueOf(nsd.getLongitude()));
				
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
