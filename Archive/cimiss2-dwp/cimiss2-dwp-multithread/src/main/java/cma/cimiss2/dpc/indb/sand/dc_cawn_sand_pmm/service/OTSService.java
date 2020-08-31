package cma.cimiss2.dpc.indb.sand.dc_cawn_sand_pmm.service;

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
import cma.cimiss2.dpc.decoder.bean.sand.SandAerosolPmm;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {

	public static BlockingQueue<StatDi> diQueues;
//	public static Map<String, Object> proMap = StationInfo.getProMap();
	public static String cts_code=StartConfig.ctsCode();
	public static String sod_code=StartConfig.sodCode();
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}

	@SuppressWarnings("deprecation")
	public static DataBaseAction insert_ots(ParseResult<SandAerosolPmm> parseResult, Date recv_time,
			StringBuffer loggerBuffer, String fileN) {
		Map<String, Object> proMap = StationInfo.getProMap();
		List<SandAerosolPmm> sandpmm = parseResult.getData();
		if(sandpmm != null && sandpmm.size() > 0) {
			int successRowCount = sandpmm.size();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			for (int i = 0; i < sandpmm.size(); i++) {
				Map<String, Object> row = new HashMap<String, Object>();
				SandAerosolPmm sap = sandpmm.get(i);	
				// 根根配置文件  获取站点基本信息
				String info = (String) proMap.get(sap.getStationNumberChina() + "+15");
				Double latitude = sap.getLatitude(); // 纬度
				Double longitude = sap.getLongitude(); // 纬度
				Double heightOfSationGroundAboveMeanSeaLevel =sap.getHeightOfSationGroundAboveMeanSeaLevel();//测站告诉
				// 根据站号查询行政区划代码
				if (info == null) {
					// 如果此站号不存在，执行下一个数据
					loggerBuffer.append("\n In the configuration file, the station number does not exist: " + sap.getStationNumberChina());
				}else {
					String[] infos = info.split(",");
					latitude = Double.parseDouble(infos[2]);// 纬度
					longitude = Double.parseDouble(infos[1]); //经度
					heightOfSationGroundAboveMeanSeaLevel=Double.parseDouble(infos[3]);//测站高度
					sap.setLatitude(latitude);					
					sap.setLongitude(longitude);
				}
//				String PrimaryKey = sdf.format(sap.getObservationTime())+"_"+sap.getStationNumberChina();
//				
//				row.put("D_RECORD_ID", PrimaryKey);// 记录标识
				row.put("D_DATA_ID", sod_code);// 资料标识
				row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
				row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
				row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
				row.put("D_DATETIME", TimeUtil.date2String(sap.getObservationTime(), "yyyy-MM-dd HH:mm:ss")); // 资料时间
				row.put("V01301", sap.getStationNumberChina());// 区站号(字符)
				row.put("V01300", StationCodeUtil.stringToAscii(sap.getStationNumberChina()));// 区站号(数字)
				row.put("V05001", latitude);// 纬度
				row.put("V06001", longitude);// 经度
				row.put("V07001", heightOfSationGroundAboveMeanSeaLevel);// 测站高度
				row.put("V04001", sap.getObservationTime().getYear() + 1900);// 资料观测年
				row.put("V04002", sap.getObservationTime().getMonth()+1);// 资料观测月
				row.put("V04003", sap.getObservationTime().getDate());// 资料观测日
				row.put("V04004", sap.getObservationTime().getHours());// 资料观测时
				row.put("V04005", sap.getObservationTime().getMinutes());// 资料观测分
				row.put("V02850", sap.getTypeOfStorageInstrument());// 存储仪器类型
				row.put("V_ITEM_CODE", sap.getProjectCode());// 项目代码
				row.put("V_STORAGE_PLACE", sap.getStorageLocation());// 存储位置
				row.put("V_WEIGHT_FACTOR", sap.getWeightFactor());// 重量因数
				row.put("V_ERROR_CODE", sap.getErrorCode());// 错误代码
				row.put("V15752", sap.getBatteryVoltageCode());// 电池电压代码
				row.put("V15765", sap.getValveCurrent());// 阀电流
				row.put("V_CORRECT_COUNT", sap.getComprehensiveCorrectionCount());// 综合订正计数
				row.put("V10004_040", sap.getBarometricCount());// 气压计数
				row.put("V13003_040", sap.getHumidityCount());// 湿度计数
				row.put("V12001_040", sap.getTemperatureCount());// 温度计数
				row.put("V_TIME_SPACING", sap.getTimeInterval());// 时间间隔
				row.put("V11002_071", sap.getWindSpeedMeasurementFactor());// 风速计量因子
				row.put("V11001_071", sap.getWindDirectionMeasurementFactor());// 风向计量因子
				row.put("V13011_071", sap.getPrecipitationMeasurementFactor());// 降水计量因子
				row.put("V12001_072", sap.getTemperatureLinearSlopeRate());// 温度线性转化后斜率
				row.put("V13003_072", sap.getHumidityLinearSlopeRate());// 湿度线性转化后斜率
				row.put("V10004_072", sap.getPressureLinearSlopeRate());// 气压线性转化后斜率
				row.put("V12001_073", sap.getTemperatureLinearIntercept());// 温度线性转化后截距
				row.put("V13003_073", sap.getHumidityLinearIntercept());// 湿度线性转化后截距
				row.put("V10004_073", sap.getPressureLinearIntercept());// 气压线性转化后截距
				row.put("V11450", sap.getWindSpeedSensitivity());// 风速灵敏度
				row.put("V11451", sap.getWindDirectionAngle());// 风向倾角
				row.put("V02440", sap.getPrecipitationSensorCorrectionFactor());// 降水传感器订正因子
				row.put("V10004", sap.getPressure());// 气压
				row.put("V13003", sap.getRelativeHumidity());// 相对湿度
				row.put("V12001", sap.getTemperature());// 温度
				row.put("V11002", sap.getWindSpeed());// 风速
				row.put("V11001", sap.getWindDirection());// 风向
				row.put("V13011", sap.getPrecipitation());// 降水
				row.put("V15471", sap.getMassConcentrationOfPM10());// PM10质量浓度
				row.put("V15472", sap.getMassConcentrationOfPM2p5());// PM2.5质量浓度
				row.put("V15473", sap.getMassConcentrationOfPM1());// PM1质量浓度
				row.put("V01324_P10", sap.getMeasurementDataFlag_pm10());// PM10测量数据标识
				row.put("V01324_P25", sap.getMeasurementDataFlag_pm2p5());// PM2.5测量数据标识
				row.put("V01324_P01", sap.getMeasurementDataFlag_pm1());// PM1测量数据标识
				row.put("V15730_P10", sap.getFlowRate_pm10());// PM10仪器测量的流量
				row.put("V12001_P10", sap.getTemperature_pm10());// PM10仪器测量的气温
				row.put("V10004_P10", sap.getPressure_pm10());// PM10仪器测量的气压
				row.put("V13003_P10", sap.getRelativeHumidity_pm10());// PM10仪器测量的相对湿度
				row.put("V15730_P25", sap.getFlowRate_pm2p5());// PM2.5仪器测量的流量
				row.put("V12001_P25", sap.getTemperature());// PM2.5仪器测量的气温
				row.put("V10004_P25", sap.getPressure());// PM2.5仪器测量的气压
				row.put("V13003_P25", sap.getRelativeHumidity());// PM2.5仪器测量的相对湿度
				row.put("V15730_P01", sap.getFlowRate_pm1());// PM1.0仪器测量的流量
				row.put("V12001_P01", sap.getTemperature());// PM1.0仪器测量的气温
				row.put("V10004_P01", sap.getPressure());// PM1.0仪器测量的气压
				row.put("V13003_P01", sap.getRelativeHumidity());// PM1.0仪器测量的相对湿度
				row.put("V_BBB", "000");
				row.put("D_SOURCE_ID", cts_code);
				
				StatDi di = new StatDi();					
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(sod_code);
				di.setDATA_TYPE_1(cts_code);
				di.setTT("PMM");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("0"); //0成功，1失败
				di.setPROCESS_STATE("0");  //0成功，1失败	
				di.setIIiii(sap.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(sap.getObservationTime(), "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());
				di.setLATITUDE(String.valueOf(sap.getLatitude()));
				di.setLONGTITUDE(String.valueOf(sap.getLongitude()));
				
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
		    loggerBuffer.append(" INSERT FAILED COUNT : " + (sandpmm.size() - successRowCount) + "\n");
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
	            row.put("V_TT", "PMM");
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
