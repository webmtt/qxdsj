package cma.cimiss2.dpc.indb.agme.dc_agme_manl_soil.service;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Pattern;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Soil;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Soil_01;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Soil_02;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Soil_03;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Soil_04;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Soil_05;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Soil_06;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Soil_07;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Soil_08;
import cma.cimiss2.dpc.decoder.tools.ElementValUtil;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.agme.dc_agme_manl_soil.ReadIni;

import org.apache.commons.lang3.ArrayUtils;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.config.CTSCodeMap;
import org.cimiss2.dwp.tools.ots.OTSBatchResult;
import org.cimiss2.dwp.tools.ots.OTSDbHelper;
import org.cimiss2.dwp.tools.utils.NumberUtil;
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;

import com.alicloud.openservices.tablestore.ClientException;

public class OTSService {
	private static final int DEFAULT_VALUE = 999999;
	public static BlockingQueue<StatDi> diQueues;
	static ReadIni ini = ReadIni.getIni();
	private static final double[] NINE_ARRAYS = {99, 999, 9999, 99999 };
	static String cts_code ="E.0003.0002.R001";
	
//	static Map<String, Object> proMap = StationInfo.getProMap();
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}
	@SuppressWarnings("deprecation")
	public static DataBaseAction processSuccessReport(ParseResult<Agme_Soil> parseResult, Date recv_time,
			String fileName, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		Map<String, Object> proMap = StationInfo.getProMap();
		List<Agme_Soil> list = parseResult.getData();
		Agme_Soil agme_Soil = list.get(0); // 解析时只存了一个对象在parseResult
		List<String> soilTypes = agme_Soil.soilTypes;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		for (String type : soilTypes) {
			switch (type) {
			case ReadIni.SECTION_SOIL_01:

				List<Agme_Soil_01> agme_Soil_01s = agme_Soil.agme_Soil_01s;
				int successRowCount_01 = agme_Soil_01s.size();
				// 用于获取时间
				for (int i = 0; i < agme_Soil_01s.size(); i++) {
					Map<String, Object> row = new HashMap<String, Object>();
					String d_data_id = ini.getValue(ReadIni.SECTION_SOIL_01, ReadIni.D_DATA_ID_KEY);
					Agme_Soil_01 agme_Soil_01 = agme_Soil_01s.get(i);
					String stationNumberC = agme_Soil_01.getStationNumberChina();
					
					String stationNumberN = StationCodeUtil.stringToAscii(stationNumberC);
					String adminCode = "999999";
					// 根据站号查询行政区划代码
					String info = (String) proMap.get(stationNumberC + "+12");
					if(info != null) {
						String[] infos = info.split(",");
						if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
							adminCode = infos[5];
					}
					
					if(adminCode.startsWith("999999")){
						info = (String) proMap.get(stationNumberC + "+01");
						if(info == null) {
							loggerBuffer.append("\n In the configuration file, the station number does not exist" + stationNumberC);
						}else {
							String[] infos = info.split(",");
							if(infos.length >= 6)
								adminCode = infos[5];
						}
					}
					// 资料时间
					Date obsTime = agme_Soil_01.getObsTime();

					Double latitude = agme_Soil_01.getLatitude(); // 纬度
					Double longitude = agme_Soil_01.getLongitude(); // 经度

					Double heightOfSationGroundAboveMeanSeaLevel = agme_Soil_01.getHeightOfSationGroundAboveMeanSeaLevel();// 测站海拔高度
					Double heightOfBarometerAboveMeanSeaLevel = agme_Soil_01.getHeightOfBarometerAboveMeanSeaLevel();// 气压传感器海拔高度

					Double geographyType = agme_Soil_01.getGeographyType(); // 地段类型
					Double soilDepth = agme_Soil_01.getSoilDepth(); // 土层深度
					String lengthStr = "00000";
					// 时间(yyyyMMddHHmmss)_微秒（毫秒转微秒）SSSSSS_站号5_经度10_纬度9_海拔或高度相关8_(V_BBB)_其他字段
					String id = TimeUtil.date2String(obsTime, TimeUtil.DATE_FMT_YMDHMS) //
									+ "_" + (lengthStr.substring(0, 5 - stationNumberC.length()) + stationNumberC) + "_" + 
							NumberUtil.FormatNumOrNine(geographyType) //
									+ "_" + NumberUtil.FormatNumOrNine(soilDepth);

					row.put("D_RECORD_ID", id); // 记录标识
						
					row.put("D_DATA_ID", d_data_id); // 资料标识，由配置文件配置
						
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss")); // 入库时间

					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss")); // 收到时间
					// 如果是新数据更新时间与入库时间一致
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss")); // 更新时间第一次入库与入库时间一致
					row.put("D_DATETIME", TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss")); // 资料时间
					row.put("V01301", stationNumberC); // 区站号（字符）
			
					row.put("V01300", stationNumberN);
					latitude = NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, latitude) ? latitude : ElementValUtil.getlatitude(String.valueOf(latitude))).doubleValue();
					row.put("V05001", latitude); // 纬度
					longitude =  NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, longitude) ? longitude : ElementValUtil.getLongitude(String.valueOf(longitude))).doubleValue();
					row.put("V06001",longitude); // 经度
					heightOfSationGroundAboveMeanSeaLevel= NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, heightOfSationGroundAboveMeanSeaLevel) ? heightOfSationGroundAboveMeanSeaLevel
							: (heightOfSationGroundAboveMeanSeaLevel / 10.0)).doubleValue();
					row.put("V07001", heightOfSationGroundAboveMeanSeaLevel); // 测站海拔高度
					heightOfBarometerAboveMeanSeaLevel =  NumberUtil.FormatNumOrNine(
							ArrayUtils.contains(NINE_ARRAYS, heightOfBarometerAboveMeanSeaLevel) ? heightOfBarometerAboveMeanSeaLevel : (heightOfBarometerAboveMeanSeaLevel / 10.0)).doubleValue();
					row.put("V07031",heightOfBarometerAboveMeanSeaLevel); // 气压传感器海拔高度
					row.put("V_ACODE", adminCode);
					row.put("V04001", obsTime.getYear()+1900); // 资料观测年
					row.put("V04002", obsTime.getMonth()+1); // 资料观测月
					row.put("V04003",obsTime.getDate()); // 资料观测日
					geographyType = NumberUtil.FormatNumOrNine(geographyType).doubleValue();
					row.put("V71115",geographyType ); // 地段类型
					soilDepth = NumberUtil.FormatNumOrNine(soilDepth).doubleValue();
					row.put("V07061", soilDepth); // 土层深度
					Double SoilWaterContent =  NumberUtil.FormatNumOrNine(agme_Soil_01.getSoilWaterContent()).doubleValue();
					row.put("V71110",SoilWaterContent * 0.1); // 田间持水量
					Double SoilBulkDensity = NumberUtil.FormatNumOrNine(agme_Soil_01.getSoilBulkDensity()).doubleValue();
					row.put("V71109", SoilBulkDensity * 0.01); // 土壤容重
					Double WiltingHumidity = NumberUtil.FormatNumOrNine(agme_Soil_01.getWiltingHumidity()).doubleValue();
					row.put("V71108",WiltingHumidity * 0.1); // 凋萎湿度
					row.put("V_BBB", v_bbb); // 默认000，首次入库可不填，数据库字段默认000
					row.put("D_SOURCE_ID", cts_code);
					
					StatDi di = new StatDi();	
					di.setFILE_NAME_O(fileName);
					di.setDATA_TYPE(d_data_id);
					di.setDATA_TYPE_1(cts_code);
					di.setTT("SOIL01");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileName);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(agme_Soil_01s.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(latitude));
					di.setLONGTITUDE(String.valueOf(longitude));
					try {
						if(isUpdate) {
							OTSDbHelper.getInstance().update("AGME_SOIL01_CHN_TAB",row);
						}else {
							OTSDbHelper.getInstance().insert("AGME_SOIL01_CHN_TAB", row);
						}
						diQueues.offer(di);
					}   catch (Exception e) {
						successRowCount_01 = successRowCount_01 -1;
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
			    loggerBuffer.append(" INSERT SUCCESS COUNT : " + successRowCount_01 + "\n");
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (agme_Soil_01s.size() - successRowCount_01) + "\n");
				break;
			case ReadIni.SECTION_SOIL_02:
				List<Agme_Soil_02> agme_Soil_02s = agme_Soil.agme_Soil_02s;
				int successRowCount_02 = agme_Soil_02s.size();
				// 用于获取时间
				for (int i = 0; i < agme_Soil_02s.size(); i++) {
					Map<String, Object> row = new HashMap<String, Object>();StatDi di = new StatDi();
					String d_data_id = ini.getValue(ReadIni.SECTION_SOIL_02, ReadIni.D_DATA_ID_KEY);
					Agme_Soil_02 agme_Soil_02 = agme_Soil_02s.get(i);
					
					int stationNumberN = DEFAULT_VALUE;
					String stationNumberC = agme_Soil_02.getStationNumberChina();
					if (Pattern.matches("\\d{5}", stationNumberC)) { // 如果是5位数字
						stationNumberN = Integer.parseInt(stationNumberC);
					}
					String adminCode = "999999";
					// 根据站号查询行政区划代码
					String info = (String) proMap.get(stationNumberC + "+12");
					if(info != null) {
						String[] infos = info.split(",");
						if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
							adminCode = infos[5];
					}
					
					if(adminCode.startsWith("999999")){
						info = (String) proMap.get(stationNumberC + "+01");
						if(info == null) {
							loggerBuffer.append("\n In the configuration file, the station number does not exist" + stationNumberC);
						}else {
							String[] infos = info.split(",");
							if(infos.length >= 6)
								adminCode = infos[5];
						}
					}

					Date obsTime = agme_Soil_02.getObsTime();


					Double latitude = agme_Soil_02.getLatitude(); // 纬度
					Double longitude = agme_Soil_02.getLongitude(); // 经度

					Double heightOfSationGroundAboveMeanSeaLevel = agme_Soil_02.getHeightOfSationGroundAboveMeanSeaLevel();// 测站海拔高度
					Double heightOfBarometerAboveMeanSeaLevel = agme_Soil_02.getHeightOfBarometerAboveMeanSeaLevel();// 气压传感器海拔高度

					Double geographyType = agme_Soil_02.getGeographyType(); // 地段类型
					Double cropName = agme_Soil_02.getCropName(); // 作物名称
					Double cropPeriod = agme_Soil_02.getCropPeriod(); // 发育期
					String lengthStr = "00000";
					
					
					// 时间(yyyyMMddHHmmss)_微秒（毫秒转微秒）SSSSSS_站号5_经度10_纬度9_海拔或高度相关8_(V_BBB)_其他字段
					String id = TimeUtil.date2String(obsTime, TimeUtil.DATE_FMT_YMDHMS) //
							+ "_" + (lengthStr.substring(0, 5 - stationNumberC.length()) + stationNumberC) //
							+ "_" + NumberUtil.FormatNumOrNine(cropName) //
							+ "_" + NumberUtil.FormatNumOrNine(cropPeriod);

					row.put("D_RECORD_ID", id); // 记录标识
					
					row.put("D_DATA_ID", d_data_id); // 资料标识，由配置文件配置
						
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss")); // 入库时间

					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss")); // 收到时间
					// 如果是新数据更新时间与入库时间一致
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss")); // 更新时间第一次入库与入库时间一致
					row.put("D_DATETIME", TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss")); // 资料时间
					row.put("V01301", stationNumberC); // 区站号（字符）
			
					row.put("V01300",stationNumberN);
					latitude = NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, latitude) ? latitude : ElementValUtil.getlatitude(String.valueOf(latitude))).doubleValue();
					row.put("V05001", latitude); // 纬度
					longitude =  NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, longitude) ? longitude : ElementValUtil.getLongitude(String.valueOf(longitude))).doubleValue();
					row.put("V06001",longitude); // 经度
					heightOfSationGroundAboveMeanSeaLevel= NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, heightOfSationGroundAboveMeanSeaLevel) ? heightOfSationGroundAboveMeanSeaLevel
							: (heightOfSationGroundAboveMeanSeaLevel / 10.0)).doubleValue();
					row.put("V07001", heightOfSationGroundAboveMeanSeaLevel); // 测站海拔高度
					heightOfBarometerAboveMeanSeaLevel =  NumberUtil.FormatNumOrNine(
							ArrayUtils.contains(NINE_ARRAYS, heightOfBarometerAboveMeanSeaLevel) ? heightOfBarometerAboveMeanSeaLevel : (heightOfBarometerAboveMeanSeaLevel / 10.0)).doubleValue();
					row.put("V07031",heightOfBarometerAboveMeanSeaLevel); // 气压传感器海拔高度
					row.put("V_ACODE", adminCode);
					row.put("V04001", obsTime.getYear()+1900); // 资料观测年
					row.put("V04002", obsTime.getMonth()+1); // 资料观测月
					row.put("V04003",obsTime.getDate()); // 资料观测日
					geographyType = NumberUtil.FormatNumOrNine(geographyType).doubleValue();
					row.put("V71115",geographyType ); // 地段类型
					cropName =  NumberUtil.FormatNumOrNine(cropName).doubleValue();
					row.put("V71001",cropName); // 作物名称
					cropPeriod = NumberUtil.FormatNumOrNine(cropPeriod).doubleValue();
					row.put("V71002",cropPeriod ); // 发育期
				
					
					Double DrySoilThickness = NumberUtil.FormatNumOrNine(agme_Soil_02.getDrySoilThickness()).doubleValue();
					row.put("V71101",DrySoilThickness ); // 干土层厚度
					Double getSoilHumidity_10 = ElementValUtil.CheckValidValue2Double(agme_Soil_02.getSoilHumidity_10(), 9999);
					Double getSoilHumidity_20 = ElementValUtil.CheckValidValue2Double(agme_Soil_02.getSoilHumidity_20(), 9999);
					Double getSoilHumidity_30 = ElementValUtil.CheckValidValue2Double(agme_Soil_02.getSoilHumidity_30(), 9999);
					Double getSoilHumidity_40 = ElementValUtil.CheckValidValue2Double(agme_Soil_02.getSoilHumidity_40(), 9999);
					Double getSoilHumidity_50 = ElementValUtil.CheckValidValue2Double(agme_Soil_02.getSoilHumidity_50(), 9999);
					Double getSoilHumidity_60 = ElementValUtil.CheckValidValue2Double(agme_Soil_02.getSoilHumidity_60(), 9999);
					Double getSoilHumidity_70 = ElementValUtil.CheckValidValue2Double(agme_Soil_02.getSoilHumidity_70(), 9999);
					Double getSoilHumidity_80 = ElementValUtil.CheckValidValue2Double(agme_Soil_02.getSoilHumidity_80(), 9999);
					Double getSoilHumidity_90 = ElementValUtil.CheckValidValue2Double(agme_Soil_02.getSoilHumidity_90(), 9999);
					Double getSoilHumidity_100 = ElementValUtil.CheckValidValue2Double(agme_Soil_02.getSoilHumidity_100(), 9999);
//					Double getSoilHumidity_10 = NumberUtil.FormatNumOrNine(agme_Soil_02.getSoilHumidity_10()).doubleValue();
//					Double getSoilHumidity_20 = NumberUtil.FormatNumOrNine(agme_Soil_02.getSoilHumidity_20()).doubleValue();
//					Double getSoilHumidity_30 = NumberUtil.FormatNumOrNine(agme_Soil_02.getSoilHumidity_30()).doubleValue();
//					Double getSoilHumidity_40 = NumberUtil.FormatNumOrNine(agme_Soil_02.getSoilHumidity_40()).doubleValue();
//					Double getSoilHumidity_50 = NumberUtil.FormatNumOrNine(agme_Soil_02.getSoilHumidity_50()).doubleValue();
//					Double getSoilHumidity_60 = NumberUtil.FormatNumOrNine(agme_Soil_02.getSoilHumidity_60()).doubleValue();
//					Double getSoilHumidity_70 = NumberUtil.FormatNumOrNine(agme_Soil_02.getSoilHumidity_70()).doubleValue();
//					Double getSoilHumidity_80 = NumberUtil.FormatNumOrNine(agme_Soil_02.getSoilHumidity_80()).doubleValue();
//					Double getSoilHumidity_90 = NumberUtil.FormatNumOrNine(agme_Soil_02.getSoilHumidity_90()).doubleValue();
//					Double getSoilHumidity_100 = NumberUtil.FormatNumOrNine(agme_Soil_02.getSoilHumidity_100()).doubleValue();
					
					
					row.put("V71102_010", getSoilHumidity_10); // 10cm土壤相对湿度
					row.put("V71102_020", getSoilHumidity_20); // 20cm土壤相对湿度
					row.put("V71102_030", getSoilHumidity_30); // 30cm土壤相对湿度
					row.put("V71102_040", getSoilHumidity_40); // 40cm土壤相对湿度
					row.put("V71102_050", getSoilHumidity_50); // 50cm土壤相对湿度
					row.put("V71102_060", getSoilHumidity_60); // 60cm土壤相对湿度
					row.put("V71102_070", getSoilHumidity_70); // 70cm土壤相对湿度
					row.put("V71102_080", getSoilHumidity_80); // 80cm土壤相对湿度
					row.put("V71102_090", getSoilHumidity_90); // 90cm土壤相对湿度
					row.put("V71102_100", getSoilHumidity_100); // 100cm土壤相对湿度
					
					Double IrrigationOrPrecipitation = (double)999999;
					if(agme_Soil_02.getIrrigationOrPrecipitation().intValue() != 9)
						IrrigationOrPrecipitation = NumberUtil.FormatNumOrNine(agme_Soil_02.getIrrigationOrPrecipitation()).doubleValue();
					
					row.put("V71100",IrrigationOrPrecipitation); // 灌溉或降水
					Double GroundWaterLevel = NumberUtil.FormatNumOrNine(agme_Soil_02.getGroundWaterLevel()).doubleValue();
					row.put("V71111",GroundWaterLevel * 0.1);// 地下水位

					// 文件中没有质控码，设置默认值9
					//BigDecimal nineQC = NumberUtil.FormatNumOrNine(Quality.Nine.getCode());
					row.put("Q71102_010", 9); // 10cm土壤相对湿度质控码
					row.put("Q71102_020", 9); // 20cm土壤相对湿度质控码
					row.put("Q71102_030", 9); // 30cm土壤相对湿度质控码
					row.put("Q71102_040", 9); // 40cm土壤相对湿度质控码
					row.put("Q71102_050", 9); // 50cm土壤相对湿度质控码
					row.put("Q71102_060", 9); // 60cm土壤相对湿度质控码
					row.put("Q71102_070", 9); // 70cm土壤相对湿度质控码
					row.put("Q71102_080", 9); // 80cm土壤相对湿度质控码
					row.put("Q71102_090", 9); // 90cm土壤相对湿度质控码
					row.put("Q71102_100", 9); // 100cm土壤相对湿度质控码

					row.put("V_BBB", v_bbb); // 默认000，首次入库可不填，数据库字段默认000
					row.put("D_SOURCE_ID", cts_code);
					
					
					di.setFILE_NAME_O(fileName);
					di.setDATA_TYPE(d_data_id);
					di.setDATA_TYPE_1(cts_code);
					di.setTT("SOIL02");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileName);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(agme_Soil_02s.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(latitude));
					di.setLONGTITUDE(String.valueOf(longitude));
					try {
						if(isUpdate) {
							OTSDbHelper.getInstance().update("AGME_SOIL02_CHN_TAB",row);
						}else {
							OTSDbHelper.getInstance().insert("AGME_SOIL02_CHN_TAB", row);
						}
						diQueues.offer(di);
					}   catch (Exception e) {
						successRowCount_02 = successRowCount_02 -1;
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
			    loggerBuffer.append(" INSERT SUCCESS COUNT : " + successRowCount_02 + "\n");
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (agme_Soil_02s.size() - successRowCount_02) + "\n");
				
				break;
			case ReadIni.SECTION_SOIL_03:
				List<Agme_Soil_03> agme_Soil_03s = agme_Soil.agme_Soil_03s;
				int successRowCount_03 = agme_Soil_03s.size();
				// 用于获取时间
				for (int i = 0; i < agme_Soil_03s.size(); i++) {
					Map<String, Object> row = new HashMap<String, Object>();
					String d_data_id = ini.getValue(ReadIni.SECTION_SOIL_03, ReadIni.D_DATA_ID_KEY);
					Agme_Soil_03 agme_Soil_03= agme_Soil_03s.get(i);
					
					int stationNumberN = DEFAULT_VALUE;
					String stationNumberC = agme_Soil_03.getStationNumberChina();
					if (Pattern.matches("\\d{5}", stationNumberC)) { // 如果是5位数字
						stationNumberN = Integer.parseInt(stationNumberC);
					}
					String adminCode = "999999";
					// 根据站号查询行政区划代码
					String info = (String) proMap.get(stationNumberC + "+12");
					if(info != null) {
						String[] infos = info.split(",");
						if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
							adminCode = infos[5];
					}
					
					if(adminCode.startsWith("999999")){
						info = (String) proMap.get(stationNumberC + "+01");
						if(info == null) {
							loggerBuffer.append("\n In the configuration file, the station number does not exist" + stationNumberC);
						}else {
							String[] infos = info.split(",");
							if(infos.length >= 6)
								adminCode = infos[5];
						}
					}					
					

					Date obsTime = agme_Soil_03.getObsTime();

					Double latitude = agme_Soil_03.getLatitude(); // 纬度
					Double longitude = agme_Soil_03.getLongitude(); // 经度

					Double heightOfSationGroundAboveMeanSeaLevel = agme_Soil_03.getHeightOfSationGroundAboveMeanSeaLevel();// 测站海拔高度
					Double heightOfBarometerAboveMeanSeaLevel = agme_Soil_03.getHeightOfBarometerAboveMeanSeaLevel();// 气压传感器海拔高度

					Double geographyType = agme_Soil_03.getGeographyType(); // 地段类型
					Double cropName = agme_Soil_03.getCropName(); // 作物名称
					Double cropPeriod = agme_Soil_03.getCropPeriod(); // 发育期
					String lengthStr = "00000";
					
					String id = TimeUtil.date2String(obsTime, TimeUtil.DATE_FMT_YMDHMS) //
							+ "_" + (lengthStr.substring(0, 5 - stationNumberC.length()) + stationNumberC) //
							+ "_" + NumberUtil.FormatNumOrNine(geographyType) //
							+ "_" + NumberUtil.FormatNumOrNine(cropName) //
							+ "_" + NumberUtil.FormatNumOrNine(cropPeriod);
					
					row.put("D_RECORD_ID", id); // 记录标识
					
					row.put("D_DATA_ID", d_data_id); // 资料标识，由配置文件配置
						
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss")); // 入库时间

					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss")); // 收到时间
					// 如果是新数据更新时间与入库时间一致
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss")); // 更新时间第一次入库与入库时间一致
					row.put("D_DATETIME", TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss")); // 资料时间
					row.put("V01301", stationNumberC); // 区站号（字符）
			
					row.put("V01300", stationNumberN);
					latitude = NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, latitude) ? latitude : ElementValUtil.getlatitude(String.valueOf(latitude))).doubleValue();
					row.put("V05001", latitude); // 纬度
					longitude =  NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, longitude) ? longitude : ElementValUtil.getLongitude(String.valueOf(longitude))).doubleValue();
					row.put("V06001",longitude); // 经度
					heightOfSationGroundAboveMeanSeaLevel= NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, heightOfSationGroundAboveMeanSeaLevel) ? heightOfSationGroundAboveMeanSeaLevel
							: (heightOfSationGroundAboveMeanSeaLevel / 10.0)).doubleValue();
					row.put("V07001", heightOfSationGroundAboveMeanSeaLevel); // 测站海拔高度
					heightOfBarometerAboveMeanSeaLevel =  NumberUtil.FormatNumOrNine(
							ArrayUtils.contains(NINE_ARRAYS, heightOfBarometerAboveMeanSeaLevel) ? heightOfBarometerAboveMeanSeaLevel : (heightOfBarometerAboveMeanSeaLevel / 10.0)).doubleValue();
					row.put("V07031",heightOfBarometerAboveMeanSeaLevel); // 气压传感器海拔高度
					row.put("V_ACODE", adminCode);
					row.put("V04001", obsTime.getYear()+1900); // 资料观测年
					row.put("V04002", obsTime.getMonth()+1); // 资料观测月
					row.put("V04003",obsTime.getDate()); // 资料观测日
					
					geographyType = NumberUtil.FormatNumOrNine(geographyType).doubleValue();
					cropName = NumberUtil.FormatNumOrNine(cropName).doubleValue();
					cropPeriod =  NumberUtil.FormatNumOrNine(cropPeriod).doubleValue();
					
					row.put("V71115", geographyType); // 地段类型
					row.put("V71001", cropName); // 作物名称
					row.put("V71002",cropPeriod); // 发育期
					
					Double SoilWaterContent_10 =  NumberUtil.FormatNumOrNine(agme_Soil_03.getSoilWaterContent_10()).doubleValue();
					Double SoilWaterContent_20 =  NumberUtil.FormatNumOrNine(agme_Soil_03.getSoilWaterContent_20()).doubleValue();
					Double SoilWaterContent_30 =  NumberUtil.FormatNumOrNine(agme_Soil_03.getSoilWaterContent_30()).doubleValue();
					Double SoilWaterContent_40 =  NumberUtil.FormatNumOrNine(agme_Soil_03.getSoilWaterContent_40()).doubleValue();
					Double SoilWaterContent_50 =  NumberUtil.FormatNumOrNine(agme_Soil_03.getSoilWaterContent_50()).doubleValue();
					Double SoilWaterContent_60 =  NumberUtil.FormatNumOrNine(agme_Soil_03.getSoilWaterContent_60()).doubleValue();
					Double SoilWaterContent_70 =  NumberUtil.FormatNumOrNine(agme_Soil_03.getSoilWaterContent_70()).doubleValue();
					Double SoilWaterContent_80 =  NumberUtil.FormatNumOrNine(agme_Soil_03.getSoilWaterContent_80()).doubleValue();
					Double SoilWaterContent_90 =  NumberUtil.FormatNumOrNine(agme_Soil_03.getSoilWaterContent_90()).doubleValue();
					Double SoilWaterContent_100 =  NumberUtil.FormatNumOrNine(agme_Soil_03.getSoilWaterContent_100()).doubleValue();
					
					
					
					
					row.put("V71106_010",SoilWaterContent_10); // 10cm水分总储存量
					row.put("V71106_020", SoilWaterContent_20); // 20cm水分总储存量
					row.put("V71106_030", SoilWaterContent_30); // 30cm水分总储存量
					row.put("V71106_040", SoilWaterContent_40); // 40cm水分总储存量
					row.put("V71106_050", SoilWaterContent_50); // 50cm水分总储存量
					row.put("V71106_060", SoilWaterContent_60); // 60cm水分总储存量
					row.put("V71106_070", SoilWaterContent_70); // 70cm水分总储存量
					row.put("V71106_080", SoilWaterContent_80); // 80cm水分总储存量
					row.put("V71106_090", SoilWaterContent_90); // 90cm水分总储存量
					row.put("V71106_100", SoilWaterContent_100); // 100cm水分总储存量
					row.put("V_BBB", v_bbb); // 默认000，首次入库可不填，数据库字段默认000
					row.put("D_SOURCE_ID", cts_code);
					
					StatDi di = new StatDi();
					di.setFILE_NAME_O(fileName);
					di.setDATA_TYPE(d_data_id);
					di.setDATA_TYPE_1(cts_code);
					di.setTT("SOIL03");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileName);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(agme_Soil_03s.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(latitude));
					di.setLONGTITUDE(String.valueOf(longitude));
					try {
						if(isUpdate) {
							OTSDbHelper.getInstance().update("AGME_SOIL03_CHN_TAB",row);
						}else {
							OTSDbHelper.getInstance().insert("AGME_SOIL03_CHN_TAB", row);
						}
						diQueues.offer(di);
					}   catch (Exception e) {
						successRowCount_03 = successRowCount_03 -1;
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
			    loggerBuffer.append(" INSERT SUCCESS COUNT : " + successRowCount_03 + "\n");
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (agme_Soil_03s.size() - successRowCount_03) + "\n");
				
				break;
			case ReadIni.SECTION_SOIL_04:
				List<Agme_Soil_04> agme_Soil_04s = agme_Soil.agme_Soil_04s;
				int successRowCount_04 = agme_Soil_04s.size();
				// 用于获取时间
				for (int i = 0; i < agme_Soil_04s.size(); i++) {
					Map<String, Object> row = new HashMap<String, Object>();StatDi di = new StatDi();
					String d_data_id = ini.getValue(ReadIni.SECTION_SOIL_04, ReadIni.D_DATA_ID_KEY);
					Agme_Soil_04 agme_Soil_04= agme_Soil_04s.get(i);
					int stationNumberN = DEFAULT_VALUE;
					String stationNumberC = agme_Soil_04.getStationNumberChina();
					if (Pattern.matches("\\d{5}", stationNumberC)) { // 如果是5位数字
						stationNumberN = Integer.parseInt(stationNumberC);
					}
					String adminCode = "999999";
					// 根据站号查询行政区划代码
					String info = (String) proMap.get(stationNumberC + "+12");
					if(info != null) {
						String[] infos = info.split(",");
						if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
							adminCode = infos[5];
					}
					
					if(adminCode.startsWith("999999")){
						info = (String) proMap.get(stationNumberC + "+01");
						if(info == null) {
							loggerBuffer.append("\n In the configuration file, the station number does not exist" + stationNumberC);
						}else {
							String[] infos = info.split(",");
							if(infos.length >= 6)
								adminCode = infos[5];
						}
					}

					// 资料时间
					Date obsTime = agme_Soil_04.getObsTime();

					Double latitude = agme_Soil_04.getLatitude(); // 纬度
					Double longitude = agme_Soil_04.getLongitude(); // 经度

					Double heightOfSationGroundAboveMeanSeaLevel = agme_Soil_04.getHeightOfSationGroundAboveMeanSeaLevel();// 测站海拔高度
					Double heightOfBarometerAboveMeanSeaLevel = agme_Soil_04.getHeightOfBarometerAboveMeanSeaLevel();// 气压传感器海拔高度

					Double geographyType = agme_Soil_04.getGeographyType(); // 地段类型
					Double cropName = agme_Soil_04.getCropName(); // 作物名称
					Double cropPeriod = agme_Soil_04.getCropPeriod(); // 发育期
					String lengthStr = "00000";
					
					// 时间(yyyyMMddHHmmss)_微秒（毫秒转微秒）SSSSSS_站号5_经度10_纬度9_海拔或高度相关8_(V_BBB)_其他字段
					String id = TimeUtil.date2String(obsTime, TimeUtil.DATE_FMT_YMDHMS) //
							+ "_" + (lengthStr.substring(0, 5 - stationNumberC.length()) + stationNumberC) //
							+ "_" + NumberUtil.FormatNumOrNine(geographyType) //
							+ "_" + NumberUtil.FormatNumOrNine(cropName) //
							+ "_" + NumberUtil.FormatNumOrNine(cropPeriod);

					row.put("D_RECORD_ID", id); // 记录标识
					
					row.put("D_DATA_ID", d_data_id); // 资料标识，由配置文件配置
						
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss")); // 入库时间

					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss")); // 收到时间
					// 如果是新数据更新时间与入库时间一致
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss")); // 更新时间第一次入库与入库时间一致
					row.put("D_DATETIME", TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss")); // 资料时间
					row.put("V01301", stationNumberC); // 区站号（字符）
			
					row.put("V01300", stationNumberN);
					latitude = NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, latitude) ? latitude : ElementValUtil.getlatitude(String.valueOf(latitude))).doubleValue();
					row.put("V05001", latitude); // 纬度
					longitude =  NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, longitude) ? longitude : ElementValUtil.getLongitude(String.valueOf(longitude))).doubleValue();
					row.put("V06001",longitude); // 经度
					heightOfSationGroundAboveMeanSeaLevel= NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, heightOfSationGroundAboveMeanSeaLevel) ? heightOfSationGroundAboveMeanSeaLevel
							: (heightOfSationGroundAboveMeanSeaLevel / 10.0)).doubleValue();
					row.put("V07001", heightOfSationGroundAboveMeanSeaLevel); // 测站海拔高度
					heightOfBarometerAboveMeanSeaLevel =  NumberUtil.FormatNumOrNine(
							ArrayUtils.contains(NINE_ARRAYS, heightOfBarometerAboveMeanSeaLevel) ? heightOfBarometerAboveMeanSeaLevel : (heightOfBarometerAboveMeanSeaLevel / 10.0)).doubleValue();
					row.put("V07031",heightOfBarometerAboveMeanSeaLevel); // 气压传感器海拔高度
					row.put("V_ACODE", adminCode);
					row.put("V04001", obsTime.getYear()+1900); // 资料观测年
					row.put("V04002", obsTime.getMonth()+1); // 资料观测月
					row.put("V04003",obsTime.getDate()); // 资料观测日
					
					geographyType = NumberUtil.FormatNumOrNine(geographyType).doubleValue();
					cropName = NumberUtil.FormatNumOrNine(cropName).doubleValue();
					cropPeriod =  NumberUtil.FormatNumOrNine(cropPeriod).doubleValue();

					row.put("V71115",geographyType); // 地段类型
					row.put("V71001", cropName); // 作物名称
					row.put("V71002",cropPeriod); // 发育期
					
					Double ValidSoilWaterContent_10 =  NumberUtil.FormatNumOrNine(agme_Soil_04.getValidSoilWaterContent_10()).doubleValue();
					Double ValidSoilWaterContent_20 =  NumberUtil.FormatNumOrNine(agme_Soil_04.getValidSoilWaterContent_20()).doubleValue();
					Double ValidSoilWaterContent_30 =  NumberUtil.FormatNumOrNine(agme_Soil_04.getValidSoilWaterContent_30()).doubleValue();
					Double ValidSoilWaterContent_40 =  NumberUtil.FormatNumOrNine(agme_Soil_04.getValidSoilWaterContent_40()).doubleValue();
					Double ValidSoilWaterContent_50 =  NumberUtil.FormatNumOrNine(agme_Soil_04.getValidSoilWaterContent_50()).doubleValue();
					Double ValidSoilWaterContent_60 =  NumberUtil.FormatNumOrNine(agme_Soil_04.getValidSoilWaterContent_60()).doubleValue();
					Double ValidSoilWaterContent_70 =  NumberUtil.FormatNumOrNine(agme_Soil_04.getValidSoilWaterContent_70()).doubleValue();
					Double ValidSoilWaterContent_80 =  NumberUtil.FormatNumOrNine(agme_Soil_04.getValidSoilWaterContent_80()).doubleValue();
					Double ValidSoilWaterContent_90 =  NumberUtil.FormatNumOrNine(agme_Soil_04.getValidSoilWaterContent_90()).doubleValue();
					Double ValidSoilWaterContent_100 =  NumberUtil.FormatNumOrNine(agme_Soil_04.getValidSoilWaterContent_100()).doubleValue();
					
				
					row.put("V71107_010",ValidSoilWaterContent_10); // 10cm有效水分储存量
					row.put("V71107_020", ValidSoilWaterContent_20); // 20cm有效水分储存量
					row.put("V71107_030", ValidSoilWaterContent_30); // 30cm有效水分储存量
					row.put("V71107_040", ValidSoilWaterContent_40); // 40cm有效水分储存量
					row.put("V71107_050", ValidSoilWaterContent_50); // 50cm有效水分储存量
					row.put("V71107_060", ValidSoilWaterContent_60); // 60cm有效水分储存量
					row.put("V71107_070", ValidSoilWaterContent_70); // 70cm有效水分储存量
					row.put("V71107_080", ValidSoilWaterContent_80); // 80cm有效水分储存量
					row.put("V71107_090", ValidSoilWaterContent_90); // 90cm有效水分储存量
					row.put("V71107_100", ValidSoilWaterContent_100); // 100cm有效水分储存量
					row.put("V_BBB", v_bbb); // 默认000，首次入库可不填，数据库字段默认000
					row.put("D_SOURCE_ID", cts_code);
					
					di.setFILE_NAME_O(fileName);
					di.setDATA_TYPE(d_data_id);
					di.setDATA_TYPE_1(cts_code);
					di.setTT("SOIL04");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileName);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(agme_Soil_04s.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(latitude));
					di.setLONGTITUDE(String.valueOf(longitude));
					try {
						if(isUpdate) {
							OTSDbHelper.getInstance().update("AGME_SOIL04_CHN_TAB",row);
						}else {
							OTSDbHelper.getInstance().insert("AGME_SOIL04_CHN_TAB", row);
						}
						diQueues.offer(di);
					}   catch (Exception e) {
						successRowCount_04= successRowCount_04 -1;
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
			    loggerBuffer.append(" INSERT SUCCESS COUNT : " + successRowCount_04+ "\n");
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (agme_Soil_04s.size() - successRowCount_04) + "\n");
				
				break;
			case ReadIni.SECTION_SOIL_05:
				List<Agme_Soil_05> agme_Soil_05s = agme_Soil.agme_Soil_05s;
				int successRowCount_05 = agme_Soil_05s.size();
				// 用于获取时间
				for (int i = 0; i < agme_Soil_05s.size(); i++) {
					Map<String, Object> row = new HashMap<String, Object>();StatDi di = new StatDi();				
					String d_data_id = ini.getValue(ReadIni.SECTION_SOIL_05, ReadIni.D_DATA_ID_KEY);
					
					Agme_Soil_05 agme_Soil_05= agme_Soil_05s.get(i);
					
					int stationNumberN = DEFAULT_VALUE;
					String stationNumberC = agme_Soil_05.getStationNumberChina();
					if (Pattern.matches("\\d{5}", stationNumberC)) { // 如果是5位数字
						stationNumberN = Integer.parseInt(stationNumberC);
					}
					String info = (String) proMap.get(stationNumberC + "+12");
					String adminCode = "999999";
					if(info != null) {
						String[] infos = info.split(",");
						if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
							adminCode = infos[5];
					}
					
					if(adminCode.startsWith("999999")){
						info = (String) proMap.get(stationNumberC + "+01");
						if(info == null) {
							loggerBuffer.append("\n In the configuration file, the station number does not exist" + stationNumberC);
						}else {
							String[] infos = info.split(",");
							if(infos.length >= 6)
								adminCode = infos[5];
						}
					}
					
					// 资料时间
					Date obsTime = agme_Soil_05.getObsTime();

					Double latitude = agme_Soil_05.getLatitude(); // 纬度
					Double longitude = agme_Soil_05.getLongitude(); // 经度

					Double heightOfSationGroundAboveMeanSeaLevel = agme_Soil_05.getHeightOfSationGroundAboveMeanSeaLevel();// 测站海拔高度
					Double heightOfBarometerAboveMeanSeaLevel = agme_Soil_05.getHeightOfBarometerAboveMeanSeaLevel();// 气压传感器海拔高度

					Double geographyType = agme_Soil_05.getGeographyType(); // 地段类型
					Double cropName = agme_Soil_05.getCropName(); // 作物名称
					Double soilDepth = agme_Soil_05.getSoilDepth(); // 土层深度
					String lengthStr = "00000";
					
					String id = TimeUtil.date2String(obsTime, TimeUtil.DATE_FMT_YMDHMS) //
							+ "_" + (lengthStr.substring(0, 5 - stationNumberC.length()) + stationNumberC) //
							+ "_" + NumberUtil.FormatNumOrNine(geographyType) //
							+ "_" + NumberUtil.FormatNumOrNine(cropName); //
					
					if(ArrayUtils.contains(NINE_ARRAYS, soilDepth))
						id += "_" + NumberUtil.FormatNumOrNine(soilDepth);
					else 
						id += "_" + String.valueOf(soilDepth * 10);  // 土层深度
					
					row.put("D_RECORD_ID", id); // 记录标识
					
					row.put("D_DATA_ID", d_data_id); // 资料标识，由配置文件配置
						
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss")); // 入库时间

					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss")); // 收到时间
					// 如果是新数据更新时间与入库时间一致
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss")); // 更新时间第一次入库与入库时间一致
					row.put("D_DATETIME", TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss")); // 资料时间
					row.put("V01301", stationNumberC); // 区站号（字符）
			
					row.put("V01300", stationNumberN);
					latitude = NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, latitude) ? latitude : ElementValUtil.getlatitude(String.valueOf(latitude))).doubleValue();
					row.put("V05001", latitude); // 纬度
					longitude =  NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, longitude) ? longitude : ElementValUtil.getLongitude(String.valueOf(longitude))).doubleValue();
					row.put("V06001",longitude); // 经度
					heightOfSationGroundAboveMeanSeaLevel= NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, heightOfSationGroundAboveMeanSeaLevel) ? heightOfSationGroundAboveMeanSeaLevel
							: (heightOfSationGroundAboveMeanSeaLevel / 10.0)).doubleValue();
					row.put("V07001", heightOfSationGroundAboveMeanSeaLevel); // 测站海拔高度
					heightOfBarometerAboveMeanSeaLevel =  NumberUtil.FormatNumOrNine(
							ArrayUtils.contains(NINE_ARRAYS, heightOfBarometerAboveMeanSeaLevel) ? heightOfBarometerAboveMeanSeaLevel : (heightOfBarometerAboveMeanSeaLevel / 10.0)).doubleValue();
					row.put("V07031",heightOfBarometerAboveMeanSeaLevel); // 气压传感器海拔高度
					row.put("V_ACODE", adminCode);
					row.put("V04001", obsTime.getYear()+1900); // 资料观测年
					row.put("V04002", obsTime.getMonth()+1); // 资料观测月
					row.put("V04003",obsTime.getDate()); // 资料观测日
					
					geographyType = NumberUtil.FormatNumOrNine(geographyType).doubleValue();
					cropName = NumberUtil.FormatNumOrNine(cropName).doubleValue();
					soilDepth =  NumberUtil.FormatNumOrNine(soilDepth).doubleValue();
					
					row.put("V71115", geographyType); // 地段类型
					row.put("V71001", cropName); // 作物名称
					row.put("V07061", soilDepth); // 土层深度
					Double SoilStatus = NumberUtil.FormatNumOrNine(agme_Soil_05.getSoilStatus()).doubleValue();
					row.put("V71200", SoilStatus); //土层状态
					row.put("V_BBB", v_bbb); // 默认000，首次入库可不填，数据库字段默认000
					row.put("D_SOURCE_ID", cts_code);
					
					di.setFILE_NAME_O(fileName);
					di.setDATA_TYPE(d_data_id);
					di.setDATA_TYPE_1(cts_code);
					di.setTT("SOIL05");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileName);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(agme_Soil_05s.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(latitude));
					di.setLONGTITUDE(String.valueOf(longitude));
					try {
						if(isUpdate) {
							OTSDbHelper.getInstance().update("AGME_SOIL05_CHN_TAB",row);
						}else {
							OTSDbHelper.getInstance().insert("AGME_SOIL05_CHN_TAB", row);
						}
						diQueues.offer(di);
					}   catch (Exception e) {
						successRowCount_05= successRowCount_05 -1;
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
			    loggerBuffer.append(" INSERT SUCCESS COUNT : " + successRowCount_05 + "\n");
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (agme_Soil_05s.size() - successRowCount_05) + "\n");
				
				break;
			case ReadIni.SECTION_SOIL_06:
				List<Agme_Soil_06> agme_Soil_06s = agme_Soil.agme_Soil_06s;
				int successRowCount_06 = agme_Soil_06s.size();
				// 用于获取时间
				for (int i = 0; i < agme_Soil_06s.size(); i++) {
					Map<String, Object> row = new HashMap<String, Object>();StatDi di = new StatDi();				
					String d_data_id = ini.getValue(ReadIni.SECTION_SOIL_06, ReadIni.D_DATA_ID_KEY);
					
					Agme_Soil_06 agme_Soil_06= agme_Soil_06s.get(i);
					
					int stationNumberN = DEFAULT_VALUE;
					String stationNumberC = agme_Soil_06.getStationNumberChina();
					if (Pattern.matches("\\d{5}", stationNumberC)) { // 如果是5位数字
						stationNumberN = Integer.parseInt(stationNumberC);
					}
					String adminCode = "999999";
					// 根据站号查询行政区划代码
					String info = (String) proMap.get(stationNumberC + "+12");
					if(info != null) {
						String[] infos = info.split(",");
						if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
							adminCode = infos[5];
					}
					
					if(adminCode.startsWith("999999")){
						info = (String) proMap.get(stationNumberC + "+01");
						if(info == null) {
							loggerBuffer.append("\n In the configuration file, the station number does not exist" + stationNumberC);
						}else {
							String[] infos = info.split(",");
							if(infos.length >= 6)
								adminCode = infos[5];
						}
					}

					// 资料时间
					Date obsTime = agme_Soil_06.getObsTime();

					Double latitude = agme_Soil_06.getLatitude(); // 纬度
					Double longitude = agme_Soil_06.getLongitude(); // 经度

					Double heightOfSationGroundAboveMeanSeaLevel = agme_Soil_06.getHeightOfSationGroundAboveMeanSeaLevel();// 测站海拔高度
					Double heightOfBarometerAboveMeanSeaLevel = agme_Soil_06.getHeightOfBarometerAboveMeanSeaLevel();// 气压传感器海拔高度

					Double geographyType = agme_Soil_06.getGeographyType(); // 地段类型
					Double cropName = agme_Soil_06.getCropName(); // 作物名称
					Double cropPeriod = agme_Soil_06.getCropPeriod(); // 发育期
					String lengthStr = "00000";
					String id = TimeUtil.date2String(obsTime, TimeUtil.DATE_FMT_YMDHMS) //
									+ "_" + (lengthStr.substring(0, 5 - stationNumberC.length()) + stationNumberC) //
									+ "_" + NumberUtil.FormatNumOrNine(geographyType) //
									+ "_" + NumberUtil.FormatNumOrNine(cropName) //
									+ "_" + NumberUtil.FormatNumOrNine(cropPeriod);
					
					row.put("D_RECORD_ID", id); // 记录标识
					
					row.put("D_DATA_ID", d_data_id); // 资料标识，由配置文件配置
						
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss")); // 入库时间

					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss")); // 收到时间
					// 如果是新数据更新时间与入库时间一致
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss")); // 更新时间第一次入库与入库时间一致
					row.put("D_DATETIME", TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss")); // 资料时间
					row.put("V01301", stationNumberC); // 区站号（字符）
			
					row.put("V01300",stationNumberN);
					latitude = NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, latitude) ? latitude : ElementValUtil.getlatitude(String.valueOf(latitude))).doubleValue();
					row.put("V05001", latitude); // 纬度
					longitude =  NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, longitude) ? longitude : ElementValUtil.getLongitude(String.valueOf(longitude))).doubleValue();
					row.put("V06001",longitude); // 经度
					heightOfSationGroundAboveMeanSeaLevel= NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, heightOfSationGroundAboveMeanSeaLevel) ? heightOfSationGroundAboveMeanSeaLevel
							: (heightOfSationGroundAboveMeanSeaLevel / 10.0)).doubleValue();
					row.put("V07001", heightOfSationGroundAboveMeanSeaLevel); // 测站海拔高度
					heightOfBarometerAboveMeanSeaLevel =  NumberUtil.FormatNumOrNine(
							ArrayUtils.contains(NINE_ARRAYS, heightOfBarometerAboveMeanSeaLevel) ? heightOfBarometerAboveMeanSeaLevel : (heightOfBarometerAboveMeanSeaLevel / 10.0)).doubleValue();
					row.put("V07031",heightOfBarometerAboveMeanSeaLevel); // 气压传感器海拔高度
					row.put("V_ACODE", adminCode);
					row.put("V04001", obsTime.getYear()+1900); // 资料观测年
					row.put("V04002", obsTime.getMonth()+1); // 资料观测月
					row.put("V04003",obsTime.getDate()); // 资料观测日
					row.put("V04004",obsTime.getHours()); // 资料观测日
					
					geographyType = NumberUtil.FormatNumOrNine(geographyType).doubleValue();
					cropName = NumberUtil.FormatNumOrNine(cropName).doubleValue();
					cropPeriod =  NumberUtil.FormatNumOrNine(cropPeriod).doubleValue();
					
					row.put("V71115", geographyType); // 地段类型
					row.put("V71001", cropName); // 作物名称
					row.put("V71002", cropPeriod); // 发育期

					Double SoilWeightWaterContent_10 = NumberUtil.FormatNumOrNine(agme_Soil_06.getSoilWeightWaterContent_10()).doubleValue();
					//Double SoilWeightWaterContent_10 = NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, agme_Soil_06.getSoilWeightWaterContent_10()) ? SoilWeightWaterContent_10 :SoilWeightWaterContent_10 * 0.1).doubleValue();
					Double SoilWeightWaterContent_20 = NumberUtil.FormatNumOrNine(agme_Soil_06.getSoilWeightWaterContent_20()).doubleValue();
					Double SoilWeightWaterContent_30 = NumberUtil.FormatNumOrNine(agme_Soil_06.getSoilWeightWaterContent_30()).doubleValue();
					Double SoilWeightWaterContent_40 = NumberUtil.FormatNumOrNine(agme_Soil_06.getSoilWeightWaterContent_40()).doubleValue();
					Double SoilWeightWaterContent_50 = NumberUtil.FormatNumOrNine(agme_Soil_06.getSoilWeightWaterContent_50()).doubleValue();
					Double SoilWeightWaterContent_60 = NumberUtil.FormatNumOrNine(agme_Soil_06.getSoilWeightWaterContent_60()).doubleValue();
					Double SoilWeightWaterContent_70 = NumberUtil.FormatNumOrNine(agme_Soil_06.getSoilWeightWaterContent_70()).doubleValue();
					Double SoilWeightWaterContent_80 = NumberUtil.FormatNumOrNine(agme_Soil_06.getSoilWeightWaterContent_80()).doubleValue();
					Double SoilWeightWaterContent_90 = NumberUtil.FormatNumOrNine(agme_Soil_06.getSoilWeightWaterContent_90()).doubleValue();
					Double SoilWeightWaterContent_100 = NumberUtil.FormatNumOrNine(agme_Soil_06.getSoilWeightWaterContent_100()).doubleValue();
					
					if (Math.abs(SoilWeightWaterContent_10 - 999999.0) < 1e-5) {
						row.put("V71104_010",SoilWeightWaterContent_10); // 10cm土壤重量含水率
					}else{
						row.put("V71104_010",SoilWeightWaterContent_10 * 0.1); // 10cm土壤重量含水率
					}
					if (Math.abs(SoilWeightWaterContent_20 - 999999.0) < 1e-5) {
						row.put("V71104_020",SoilWeightWaterContent_20); // 20cm土壤重量含水率
					}else{
						row.put("V71104_020",SoilWeightWaterContent_20 * 0.1); // 20cm土壤重量含水率
					}
					if (Math.abs(SoilWeightWaterContent_30 - 999999.0) < 1e-5) {
						row.put("V71104_030",SoilWeightWaterContent_30); // 30cm土壤重量含水率
					}else{
						row.put("V71104_030",SoilWeightWaterContent_30 * 0.1); // 30cm土壤重量含水率
					}
					if (Math.abs(SoilWeightWaterContent_40 - 999999.0) < 1e-5) {
						row.put("V71104_040",SoilWeightWaterContent_40); // 40cm土壤重量含水率
					}else{
						row.put("V71104_040",SoilWeightWaterContent_40 * 0.1); // 40cm土壤重量含水率
					}
					if (Math.abs(SoilWeightWaterContent_50 - 999999.0) < 1e-5) {
						row.put("V71104_050",SoilWeightWaterContent_50); // 50cm土壤重量含水率
					}else{
						row.put("V71104_050",SoilWeightWaterContent_50 * 0.1); // 50cm土壤重量含水率
					}
					if (Math.abs(SoilWeightWaterContent_60 - 999999.0) < 1e-5) {
						row.put("V71104_060",SoilWeightWaterContent_60); // 60cm土壤重量含水率
					}else{
						row.put("V71104_060",SoilWeightWaterContent_60 * 0.1); // 60cm土壤重量含水率
					}
					if (Math.abs(SoilWeightWaterContent_70 - 999999.0) < 1e-5) {
						row.put("V71104_070",SoilWeightWaterContent_70); // 70cm土壤重量含水率
					}else{
						row.put("V71104_070",SoilWeightWaterContent_10 * 0.1); // 70cm土壤重量含水率
					}
					if (Math.abs(SoilWeightWaterContent_80 - 999999.0) < 1e-5) {
						row.put("V71104_080",SoilWeightWaterContent_80); // 80cm土壤重量含水率
					}else{
						row.put("V71104_080",SoilWeightWaterContent_80 * 0.1); // 80cm土壤重量含水率
					}
					if (Math.abs(SoilWeightWaterContent_90 - 999999.0) < 1e-5) {
						row.put("V71104_090",SoilWeightWaterContent_90); // 90cm土壤重量含水率
					}else{
						row.put("V71104_090",SoilWeightWaterContent_90 * 0.1); // 90cm土壤重量含水率
					}
					if (Math.abs(SoilWeightWaterContent_100 - 999999.0) < 1e-5) {
						row.put("V71104_100",SoilWeightWaterContent_100); // 100cm土壤重量含水率
					}else{
						row.put("V71104_100",SoilWeightWaterContent_100 * 0.1); // 100cm土壤重量含水率
					}
					
				/*	row.put("V71104_020", SoilWeightWaterContent_20 * 0.1); // 20cm土壤重量含水率
					row.put("V71104_030", SoilWeightWaterContent_30 * 0.1); // 30cm土壤重量含水率
					row.put("V71104_040", SoilWeightWaterContent_40 * 0.1); // 40cm土壤重量含水率
					row.put("V71104_050", SoilWeightWaterContent_50 * 0.1); // 50cm土壤重量含水率
					row.put("V71104_060", SoilWeightWaterContent_60 * 0.1); // 60cm土壤重量含水率
					row.put("V71104_070", SoilWeightWaterContent_70 * 0.1); // 70cm土壤重量含水率
					row.put("V71104_080", SoilWeightWaterContent_80 * 0.1); // 80cm土壤重量含水率
					row.put("V71104_090", SoilWeightWaterContent_90 * 0.1); // 90cm土壤重量含水率
					row.put("V71104_100", SoilWeightWaterContent_100 * 0.1); // 100cm土壤重量含水率
*/	
					row.put("V_BBB", v_bbb); // 默认000，首次入库可不填，数据库字段默认000
					row.put("D_SOURCE_ID", cts_code);
						
					di.setFILE_NAME_O(fileName);
					di.setDATA_TYPE(d_data_id);
					di.setDATA_TYPE_1(cts_code);
					di.setTT("SOIL06");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileName);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(agme_Soil_06s.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(latitude));
					di.setLONGTITUDE(String.valueOf(longitude));
					try {
						if(isUpdate) {
							OTSDbHelper.getInstance().update("AGME_SOIL06_CHN_TAB",row);
						}else {
							OTSDbHelper.getInstance().insert("AGME_SOIL06_CHN_TAB", row);
						}
						diQueues.offer(di);
					}   catch (Exception e) {
						successRowCount_06 = successRowCount_06 -1;
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
			    loggerBuffer.append(" INSERT SUCCESS COUNT : " + successRowCount_06 + "\n");
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (agme_Soil_06s.size() - successRowCount_06) + "\n");
				
				break;
			case ReadIni.SECTION_SOIL_07:
				List<Agme_Soil_07> agme_Soil_07s = agme_Soil.agme_Soil_07s;
				int successRowCount_07 = agme_Soil_07s.size();
				// 用于获取时间
				for (int i = 0; i < agme_Soil_07s.size(); i++) {
					Map<String, Object> row = new HashMap<String, Object>();StatDi di = new StatDi();
					String d_data_id = ini.getValue(ReadIni.SECTION_SOIL_07, ReadIni.D_DATA_ID_KEY);
					
					Agme_Soil_07 agme_Soil_07= agme_Soil_07s.get(i);
					
					int stationNumberN = DEFAULT_VALUE;
					String stationNumberC = agme_Soil_07.getStationNumberChina();
					if (Pattern.matches("\\d{5}", stationNumberC)) { // 如果是5位数字
						stationNumberN = Integer.parseInt(stationNumberC);
					}
					String adminCode = "999999";
					// 根据站号查询行政区划代码
					String info = (String) proMap.get(stationNumberC + "+12");
					if(info != null) {
						String[] infos = info.split(",");
						if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
							adminCode = infos[5];
					}
					
					if(adminCode.startsWith("999999")){
						info = (String) proMap.get(stationNumberC + "+01");
						if(info == null) {
							loggerBuffer.append("\n In the configuration file, the station number does not exist" + stationNumberC);
						}else {
							String[] infos = info.split(",");
							if(infos.length >= 6)
								adminCode = infos[5];
						}
					}

					// 资料时间
					Date obsTime = agme_Soil_07.getObsTime();

					Double latitude = agme_Soil_07.getLatitude(); // 纬度
					Double longitude = agme_Soil_07.getLongitude(); // 经度

					Double heightOfSationGroundAboveMeanSeaLevel = agme_Soil_07.getHeightOfSationGroundAboveMeanSeaLevel();// 测站海拔高度
					Double heightOfBarometerAboveMeanSeaLevel = agme_Soil_07.getHeightOfBarometerAboveMeanSeaLevel();// 气压传感器海拔高度

					Double geographyType = agme_Soil_07.getGeographyType(); // 地段类型
					Double cropName = agme_Soil_07.getCropName(); // 作物名称
					String lengthStr = "00000";
					String id = TimeUtil.date2String(obsTime, TimeUtil.DATE_FMT_YMDHMS) //
							+ "_" + (lengthStr.substring(0, 5 - stationNumberC.length()) + stationNumberC) //
							+ "_" + NumberUtil.FormatNumOrNine(geographyType) //
							+ "_" + NumberUtil.FormatNumOrNine(cropName);
					
					row.put("D_RECORD_ID", id); // 记录标识
					
					row.put("D_DATA_ID", d_data_id); // 资料标识，由配置文件配置
						
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss")); // 入库时间

					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss")); // 收到时间
					// 如果是新数据更新时间与入库时间一致
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss")); // 更新时间第一次入库与入库时间一致
					row.put("D_DATETIME", TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss")); // 资料时间
					row.put("V01301", stationNumberC); // 区站号（字符）
			
					row.put("V01300", stationNumberN);
					latitude = NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, latitude) ? latitude : ElementValUtil.getlatitude(String.valueOf(latitude))).doubleValue();
					row.put("V05001", latitude); // 纬度
					longitude =  NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, longitude) ? longitude : ElementValUtil.getLongitude(String.valueOf(longitude))).doubleValue();
					row.put("V06001",longitude); // 经度
					heightOfSationGroundAboveMeanSeaLevel= NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, heightOfSationGroundAboveMeanSeaLevel) ? heightOfSationGroundAboveMeanSeaLevel
							: (heightOfSationGroundAboveMeanSeaLevel / 10.0)).doubleValue();
					row.put("V07001", heightOfSationGroundAboveMeanSeaLevel); // 测站海拔高度
					heightOfBarometerAboveMeanSeaLevel =  NumberUtil.FormatNumOrNine(
							ArrayUtils.contains(NINE_ARRAYS, heightOfBarometerAboveMeanSeaLevel) ? heightOfBarometerAboveMeanSeaLevel : (heightOfBarometerAboveMeanSeaLevel / 10.0)).doubleValue();
					row.put("V07031",heightOfBarometerAboveMeanSeaLevel); // 气压传感器海拔高度
					row.put("V_ACODE", adminCode);
					row.put("V04001", obsTime.getYear()+1900); // 资料观测年
					row.put("V04002", obsTime.getMonth()+1); // 资料观测月
					row.put("V04003",obsTime.getDate()); // 资料观测日
					row.put("V04004",obsTime.getHours()); // 资料观测时
					
					geographyType = NumberUtil.FormatNumOrNine(geographyType).doubleValue();
					cropName = NumberUtil.FormatNumOrNine(cropName).doubleValue();
					
					row.put("V71115", geographyType); // 地段类型
					row.put("V71001", cropName); // 作物名称
					
					Double DrySoilThicknes = NumberUtil.FormatNumOrNine(agme_Soil_07.getDrySoilThickness()).doubleValue();
					row.put("V71101", DrySoilThicknes); // 干土层百度
					Double GroundWaterLevel = NumberUtil.FormatNumOrNine(agme_Soil_07.getGroundWaterLevel() == null ? 999999 : agme_Soil_07.getGroundWaterLevel()).doubleValue();
					row.put("V71111", GroundWaterLevel * 0.1); // 地下水位

					row.put("V_BBB", v_bbb); // 默认000，首次入库可不填，数据库字段默认000
					row.put("D_SOURCE_ID", cts_code);
					
					
					di.setFILE_NAME_O(fileName);
					di.setDATA_TYPE(d_data_id);
					di.setDATA_TYPE_1(cts_code);
					di.setTT("SOIL07");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileName);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(agme_Soil_07s.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(latitude));
					di.setLONGTITUDE(String.valueOf(longitude));
					try {
						if(isUpdate) {
							OTSDbHelper.getInstance().update("AGME_SOIL07_CHN_TAB",row);
						}else {
							OTSDbHelper.getInstance().insert("AGME_SOIL07_CHN_TAB", row);
						}
						diQueues.offer(di);
					}   catch (Exception e) {
						successRowCount_07 = successRowCount_07 -1;
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
			    loggerBuffer.append(" INSERT SUCCESS COUNT : " + successRowCount_07 + "\n");
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (agme_Soil_07s.size() - successRowCount_07) + "\n");
				
				break;
			case ReadIni.SECTION_SOIL_08:
				List<Agme_Soil_08> agme_Soil_08s = agme_Soil.agme_Soil_08s;
				int successRowCount_08 = agme_Soil_08s.size();
				// 用于获取时间
				for (int i = 0; i < agme_Soil_08s.size(); i++) {
					Map<String, Object> row = new HashMap<String, Object>();
					String d_data_id = ini.getValue(ReadIni.SECTION_SOIL_08, ReadIni.D_DATA_ID_KEY);
					
					Agme_Soil_08 agme_Soil_08= agme_Soil_08s.get(i);
					int stationNumberN = DEFAULT_VALUE;
					String stationNumberC = agme_Soil_08.getStationNumberChina();
					if (Pattern.matches("\\d{5}", stationNumberC)) { // 如果是5位数字
						stationNumberN = Integer.parseInt(stationNumberC);
					}
					String adminCode = "999999";
					// 根据站号查询行政区划代码
					String info = (String) proMap.get(stationNumberC + "+12");
					if(info != null) {
						String[] infos = info.split(",");
						if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
							adminCode = infos[5];
					}
					
					if(adminCode.startsWith("999999")){
						info = (String) proMap.get(stationNumberC + "+01");
						if(info == null) {
							loggerBuffer.append("\n In the configuration file, the station number does not exist" + stationNumberC);
						}else {
							String[] infos = info.split(",");
							if(infos.length >= 6)
								adminCode = infos[5];
						}
					}

					// 资料时间
					Date obsTime = agme_Soil_08.getObsTime();

					Double latitude = agme_Soil_08.getLatitude(); // 纬度
					Double longitude = agme_Soil_08.getLongitude(); // 经度

					Double heightOfSationGroundAboveMeanSeaLevel = agme_Soil_08.getHeightOfSationGroundAboveMeanSeaLevel();// 测站海拔高度
					Double heightOfBarometerAboveMeanSeaLevel = agme_Soil_08.getHeightOfBarometerAboveMeanSeaLevel();// 气压传感器海拔高度

					Double geographyType = agme_Soil_08.getGeographyType(); // 地段类型
					Double cropName = agme_Soil_08.getCropName(); // 作物名称
					String lengthStr = "00000";
					String id = TimeUtil.date2String(obsTime, TimeUtil.DATE_FMT_YMDHMS) //
							+ "_" + (lengthStr.substring(0, 5 - stationNumberC.length()) + stationNumberC) //
							+ "_" + NumberUtil.FormatNumOrNine(geographyType) //
							+ "_" + NumberUtil.FormatNumOrNine(cropName);
					
					row.put("D_RECORD_ID", id); // 记录标识
					
					row.put("D_DATA_ID", d_data_id); // 资料标识，由配置文件配置
						
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss")); // 入库时间

					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss")); // 收到时间
					// 如果是新数据更新时间与入库时间一致
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss")); // 更新时间第一次入库与入库时间一致
					row.put("D_DATETIME", TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss")); // 资料时间
					row.put("V01301", stationNumberC); // 区站号（字符）
			
					row.put("V01300", stationNumberN);
					latitude = NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, latitude) ? latitude : ElementValUtil.getlatitude(String.valueOf(latitude))).doubleValue();
					row.put("V05001", latitude); // 纬度
					longitude =  NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, longitude) ? longitude : ElementValUtil.getLongitude(String.valueOf(longitude))).doubleValue();
					row.put("V06001",longitude); // 经度
					heightOfSationGroundAboveMeanSeaLevel= NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, heightOfSationGroundAboveMeanSeaLevel) ? heightOfSationGroundAboveMeanSeaLevel
							: (heightOfSationGroundAboveMeanSeaLevel / 10.0)).doubleValue();
					row.put("V07001", heightOfSationGroundAboveMeanSeaLevel); // 测站海拔高度
					heightOfBarometerAboveMeanSeaLevel =  NumberUtil.FormatNumOrNine(
							ArrayUtils.contains(NINE_ARRAYS, heightOfBarometerAboveMeanSeaLevel) ? heightOfBarometerAboveMeanSeaLevel : (heightOfBarometerAboveMeanSeaLevel / 10.0)).doubleValue();
					row.put("V07031",heightOfBarometerAboveMeanSeaLevel); // 气压传感器海拔高度
					row.put("V_ACODE", adminCode);
					row.put("V04001", obsTime.getYear()+1900); // 资料观测年
					row.put("V04002", obsTime.getMonth()+1); // 资料观测月
					row.put("V04003",obsTime.getDate()); // 资料观测日
					row.put("V04004",obsTime.getHours()); // 资料观测时
					
					geographyType = NumberUtil.FormatNumOrNine(geographyType).doubleValue();
					cropName = NumberUtil.FormatNumOrNine(cropName).doubleValue();
					
					row.put("V71115", geographyType); // 地段类型
					row.put("V71001", cropName); // 作物名称
				
					Double PreOrIrrigationOrInfiltrationPro =NumberUtil.FormatNumOrNine(agme_Soil_08.getPreOrIrrigationOrInfiltrationPro()).doubleValue();
					row.put("V71100", PreOrIrrigationOrInfiltrationPro); // 降水灌溉与渗透
					Double PreOrIrrigationOrInfiltrationDepth =  NumberUtil.FormatNumOrNine(agme_Soil_08.getPreOrIrrigationOrInfiltrationDepth()).doubleValue();
					row.put("V71100_01",PreOrIrrigationOrInfiltrationDepth * 0.1); // 降水灌溉量或渗透深度
					row.put("V_APPEAR_TIME", agme_Soil_08.getOccurTime()); // 出现时间
					row.put("V_BBB", v_bbb); // 默认000，首次入库可不填，数据库字段默认000
					row.put("D_SOURCE_ID", cts_code);

					
					StatDi di = new StatDi();
					di.setFILE_NAME_O(fileName);
					di.setDATA_TYPE(d_data_id);
					di.setDATA_TYPE_1(cts_code);
					di.setTT("SOIL08");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileName);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(agme_Soil_08s.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(latitude));
					di.setLONGTITUDE(String.valueOf(longitude));
					try {
						if(isUpdate) {
							OTSDbHelper.getInstance().update("AGME_SOIL08_CHN_TAB",row);
						}else {
							OTSDbHelper.getInstance().insert("AGME_SOIL08_CHN_TAB", row);
						}
						diQueues.offer(di);
					}   catch (Exception e) {
						successRowCount_08 = successRowCount_08 -1;
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
			    loggerBuffer.append(" INSERT SUCCESS COUNT : " + successRowCount_08 + "\n");
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (agme_Soil_08s.size() - successRowCount_08) + "\n");
				
				break;
			default:
				break;
			}
		}
		
		return DataBaseAction.SUCCESS;
	}
	@SuppressWarnings("deprecation")
	public static void reportInfoToDb(List<ReportInfo> reportInfos, String v_bbb, Date recv_time, String v_cccc,
			String v_tt, StringBuffer loggerBuffer, List<CTSCodeMap> codeMaps) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		if(reportInfos != null && reportInfos.size() > 0) {
			List<Map<String, Object>> batchs = new ArrayList<Map<String, Object>>();
	        for (int i = 0; i < reportInfos.size(); i++) {
	        	Map<String, Object> row = new HashMap<String, Object>();
	        	AgmeReportHeader agmeReportHeader = (AgmeReportHeader) reportInfos.get(i).getT();
	        	String CropType = agmeReportHeader.getCropType().replaceAll("-", "");
				int length = CropType.length();
				int type = Integer.parseInt(CropType.substring(length -2));
				String primkey = sdf.format(agmeReportHeader.getReport_time())+"_"+agmeReportHeader.getStationNumberChina()+"_"+CropType+"_"+v_tt+"_"+v_bbb;
				row.put("D_RECORD_ID", primkey);
				row.put("D_DATA_ID", codeMaps.get(type - 1).getReport_sod_code());
				row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
		        row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
		        row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
		        row.put("D_DATETIME", TimeUtil.date2String(agmeReportHeader.getReport_time(),"yyyy-MM-dd HH:mm:ss"));  
				row.put("V_BBB", v_bbb);
				row.put("V_CCCC", v_cccc);
				row.put("V01301", agmeReportHeader.getStationNumberChina());
				row.put("V01300", Integer.parseInt(StationCodeUtil.stringToAscii(agmeReportHeader.getStationNumberChina())));
				Double latitude = agmeReportHeader.getLatitude();
				latitude = NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, latitude) ? latitude : ElementValUtil.getlatitude(String.valueOf(latitude))).doubleValue();
				row.put("V05001", latitude);
				Double longitude = agmeReportHeader.getLongitude();
				longitude =  NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, longitude) ? longitude : ElementValUtil.getLongitude(String.valueOf(longitude))).doubleValue();
				row.put("V06001",longitude);
				row.put("V_ACODE", 0);
				row.put("V04001", agmeReportHeader.getReport_time().getYear()+1900);
				row.put("V04002", agmeReportHeader.getReport_time().getMonth()+1);
				row.put("V04003", agmeReportHeader.getReport_time().getDate());
				row.put("V04004", agmeReportHeader.getReport_time().getHours());
				row.put("V04005", agmeReportHeader.getReport_time().getMinutes());
				row.put("V04006", agmeReportHeader.getReport_time().getSeconds());
				row.put("V_TT", v_tt);
				row.put("V_ELE_KIND", CropType.substring(0, length - 2));
				row.put("V01323", CropType);
				row.put("V_LEN", reportInfos.get(i).getReport().length());
				row.put("V_REPORT", reportInfos.get(i).getReport());

	            batchs.add(row);
	        }
	        OTSBatchResult result = OTSDbHelper.getInstance().insert("AGME_ECO_REP_ABRE_SECT_TAB", batchs);
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
