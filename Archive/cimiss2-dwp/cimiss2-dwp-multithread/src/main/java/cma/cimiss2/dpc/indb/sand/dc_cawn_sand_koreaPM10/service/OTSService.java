package cma.cimiss2.dpc.indb.sand.dc_cawn_sand_koreaPM10.service;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.ots.OTSDbHelper;
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;

import com.alicloud.openservices.tablestore.ClientException;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.sand.KoreaSand_PM10;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;


public class OTSService {
	public static String cts_code=StartConfig.ctsCode();
	public static String sod_code=StartConfig.sodCode();
	public static int defaultInt = 999999;
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");	
//	static Map<String, Object> proMap = StationInfo.getProMap();	
	public static BlockingQueue<StatDi> diQueues;

	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}

	@SuppressWarnings("deprecation")
	public static DataBaseAction insert_ots(ParseResult<KoreaSand_PM10> parseResult, Date recv_time,
			StringBuffer loggerBuffer, String fileN) {
		Map<String, Object> proMap = StationInfo.getProMap();
		List<KoreaSand_PM10> list = parseResult.getData();
		if(list != null && list.size() > 0) {
			int successRowCount = list.size();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> row = new HashMap<String, Object>();
				KoreaSand_PM10 pm10 = list.get(i);
				
				double statHeight = defaultInt;
				double adminCode = defaultInt;
				String info = (String) proMap.get(list.get(i).getStationNumberChina() + "+01");
				if(info == null) {
					loggerBuffer.append("\n In the configuration file, the station number does not exist:" + list.get(i).getStationNumberChina());
				}		
				else{
					String[] infos = info.split(",");
					if(!infos[3].equals("null")||!infos[3].equals("")){
						
						statHeight = Double.parseDouble(infos[3]);				
					}	
					if(!infos[5].equals("null")||!infos[5].equals("")){
						
						adminCode = Double.parseDouble(infos[5]);				
					}	
				}
				
//				String PrimaryKey = sdf.format(pm10.getObservationTime())+"_"+pm10.getStationNumberChina();		
//				row.put("D_RECORD_ID", PrimaryKey);// 记录标识
				row.put("D_DATA_ID", sod_code);// 资料标识
				row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss")); // 入库时间
				row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss")); // 收到时间
				// 如果是新数据更新时间与入库时间一致
				row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss")); // 更新时间第一次入库与入库时间一致
				row.put("D_DATETIME", TimeUtil.date2String(pm10.getObservationTime(), "yyyy-MM-dd HH:mm:ss")); // 资料时间
				row.put("V01301", pm10.getStationNumberChina());// 区站号(字符)
				row.put("V01300", StationCodeUtil.stringToAscii(pm10.getStationNumberChina()));// 区站号(数字)
				row.put("V05001",pm10.getLatitude());// 纬度
				row.put("V06001", pm10.getLongitude());// 经度
				row.put("V07001", statHeight);// 测站高度
				row.put("V_ACODE", adminCode);//中国行政区代码
				row.put("V04001", pm10.getObservationTime().getYear() + 1900);// 资料观测年
				row.put("V04002", pm10.getObservationTime().getMonth()+1);// 资料观测月
				row.put("V04003", pm10.getObservationTime().getDate());// 资料观测日
				row.put("V04004", pm10.getObservationTime().getHours());// 资料观测时
				row.put("V04005", pm10.getObservationTime().getMinutes());// 资料观测分
				row.put("V04006", pm10.getObservationTime().getSeconds());// 资料观测分
				row.put("V02321", pm10.getInstrumentStatusCode());// 仪器状态码
				row.put("V04016", pm10.getTimeInterval());// 观测时间间隔
				row.put("V15471", pm10.getAvgConcentration_5Min());// 质量浓度
				row.put("V15487", pm10.getTotalMass());// 总质量
				row.put("V15488", pm10.getNormalFlow());// 质量标准流量
				row.put("V15471_001_701", pm10.getAvgConcentration_1Hour());// 1小时质量浓度平均值
				row.put("V15471_024_701", pm10.getAvgConcentration_24Hour());// 24小时质量浓度平均值
				row.put("V12001", pm10.getAirTemperature());// 环境温度
				row.put("V10004", pm10.getAirPressure());// 环境气压
				row.put("V71617", pm10.getProjectCode());// 项目代码
				row.put("V15730_P", pm10.getMainRoadFlow());// 主路流量
				row.put("V15730_S", pm10.getSideRoadFlow());// 旁路流量
				row.put("V15880", pm10.getLoadFactor());// 负载率
				row.put("V15883", pm10.getFrequency());// 频率
				row.put("V15886", pm10.getNoise());// 噪音
				row.put("V02375", pm10.getRunningState());// 运行状态码
				row.put("V12001_P", pm10.getMainRoadTemperature());// 主路温度
				row.put("V13003_P", pm10.getMainRoadRelativeHumidity());// 主路相对湿度
				row.put("V13003_S", pm10.getSideRoadRelativeHumidity());// 旁路相对湿度
				row.put("V13003", pm10.getAirRelativeHumidity());// 空气相对湿度
				row.put("V_BBB", "000");
				row.put("D_SOURCE_ID", cts_code);
				
				StatDi di = new StatDi();					
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(sod_code);
				di.setDATA_TYPE_1(cts_code);
				di.setTT("韩国沙尘暴PM10");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("0"); //0成功，1失败
				di.setPROCESS_STATE("0");  //0成功，1失败	
				di.setIIiii(pm10.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(pm10.getObservationTime(), "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());
				di.setLATITUDE(String.valueOf(pm10.getLatitude()));
				di.setLONGTITUDE(String.valueOf(pm10.getLongitude()));
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
	

}
