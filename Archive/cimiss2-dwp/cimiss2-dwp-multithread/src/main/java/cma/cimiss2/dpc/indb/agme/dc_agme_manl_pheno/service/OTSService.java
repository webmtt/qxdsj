package cma.cimiss2.dpc.indb.agme.dc_agme_manl_pheno.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

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

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Pheno;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Pheno_01;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Pheno_02;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Pheno_03;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Pheno_04;
import cma.cimiss2.dpc.decoder.tools.ElementValUtil;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.agme.dc_agme_manl_pheno.ReadIni;

public class OTSService {
	static String lengthStr = "00000";
//	static Map<String, Object> proMap = StationInfo.getProMap();
	
	public static BlockingQueue<StatDi> diQueues;
	static ReadIni ini = ReadIni.getIni();
	public static String cts_code = "E.0003.0003.R001";
	private static final double[] NINE_ARRAYS = { 99, 999, 9999, 99999 };
	
	
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}

	@SuppressWarnings("deprecation")
	public static DataBaseAction processSuccessReport(ParseResult<Agme_Pheno> parseResult, Date recv_time,
			String fileName, boolean isUpdate, String v_bbb, StringBuffer loggerBuffer) {
		Map<String, Object> proMap = StationInfo.getProMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		List<Agme_Pheno> list = parseResult.getData();
		Agme_Pheno agme_Pheno = list.get(0); // 解析时只存了一个对象在parseResult
		List<String> phenoTypes = agme_Pheno.phenoTypes;

		for (String type : phenoTypes) {
			switch (type) {
			case ReadIni.SECTION_PHENO_01:
				List<Object> agme_pheno_01s = agme_Pheno.agme_pheno_01s;
				String d_data_id = ini.getValue(ReadIni.SECTION_PHENO_01, ReadIni.D_DATA_ID_KEY);
				int successRowCount_01 = agme_pheno_01s.size();
				
				for (Object obj : agme_pheno_01s) {
					Agme_Pheno_01 agme_Pheno_01 = (Agme_Pheno_01) obj;
					Map<String, Object> row = new HashMap<String, Object>();
					String stationNumberC = agme_Pheno_01.getStationNumberChina();
					String stationNumberN = StationCodeUtil.stringToAscii(stationNumberC);
					// 根据站号查询行政区划代码
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
					
					Date obsTime = agme_Pheno_01.getAppearTime();

					Double latitude = agme_Pheno_01.getLatitude(); // 纬度
					Double longitude = agme_Pheno_01.getLongitude(); // 经度

					Double heightOfSationGroundAboveMeanSeaLevel = agme_Pheno_01.getHeightOfSationGroundAboveMeanSeaLevel();// 测站海拔高度
					Double heightOfBarometerAboveMeanSeaLevel = agme_Pheno_01.getHeightOfBarometerAboveMeanSeaLevel();// 气压传感器海拔高度

					Double plantOrAnimalName = agme_Pheno_01.getPlantOrAnimalName(); // 动植物名称
					plantOrAnimalName = NumberUtil.FormatNumOrNine(plantOrAnimalName).doubleValue();
					Double phenologyName = agme_Pheno_01.getPhenologyName(); // 物候期名称
					phenologyName = NumberUtil.FormatNumOrNine(phenologyName).doubleValue();
					String id = TimeUtil.date2String(obsTime, TimeUtil.DATE_FMT_YMDHMS) //
							+ "_" + (lengthStr.substring(0, 5 - stationNumberC.length()) + stationNumberC) //
							+ "_" + plantOrAnimalName + "_" + phenologyName;
					row.put("D_RECORD_ID", id);
					row.put("D_DATA_ID",d_data_id);
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_DATETIME",TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss"));
					row.put("V01301", stationNumberC);
					row.put("V01300", stationNumberN);
					latitude = NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, latitude) ? latitude : ElementValUtil.getlatitude(String.valueOf(latitude))).doubleValue();
					row.put("V05001", latitude);
					longitude =  NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, longitude) ? longitude : ElementValUtil.getLongitude(String.valueOf(longitude))).doubleValue();
					row.put("V06001",longitude);
//					heightOfSationGroundAboveMeanSeaLevel = NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, heightOfSationGroundAboveMeanSeaLevel) ? heightOfSationGroundAboveMeanSeaLevel
//							: (heightOfSationGroundAboveMeanSeaLevel / 10.0)).doubleValue();
					
					heightOfSationGroundAboveMeanSeaLevel = (heightOfSationGroundAboveMeanSeaLevel / 10.0); // 测站海拔高度
					
					row.put("V07001",heightOfSationGroundAboveMeanSeaLevel);   
					
//					heightOfBarometerAboveMeanSeaLevel = NumberUtil.FormatNumOrNine(
//							ArrayUtils.contains(NINE_ARRAYS, heightOfBarometerAboveMeanSeaLevel) ? heightOfBarometerAboveMeanSeaLevel : (heightOfBarometerAboveMeanSeaLevel / 10.0)).doubleValue(); 
					heightOfBarometerAboveMeanSeaLevel = heightOfBarometerAboveMeanSeaLevel.doubleValue() == 99999.0 ? 999999 : heightOfBarometerAboveMeanSeaLevel / 10.0;
					
					row.put("V07031",heightOfBarometerAboveMeanSeaLevel);
					row.put("V_ACODE", adminCode); // V_ACODE		
					row.put("V04001", obsTime.getYear()+1900);
					row.put("V04002", obsTime.getMonth()+1);
					row.put("V04003", obsTime.getDate());
					row.put("V71501",plantOrAnimalName);
					row.put("V71618", phenologyName);
					row.put("V_BBB", v_bbb);
					row.put("D_SOURCE_ID", cts_code);
					
					StatDi di = new StatDi();					
					di.setFILE_NAME_O(fileName);
					di.setDATA_TYPE(d_data_id);
					di.setDATA_TYPE_1(cts_code);
					di.setTT("PHENO_01");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileName);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(agme_Pheno_01.getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(agme_Pheno_01.getAppearTime(), "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLATITUDE(String.valueOf(latitude));
					di.setLONGTITUDE(String.valueOf(longitude));
					try {
						if(isUpdate) {
							OTSDbHelper.getInstance().update("AGME_PHENO01_CHN_TAB",row);
						}else {
							OTSDbHelper.getInstance().insert("AGME_PHENO01_CHN_TAB", row);
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
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (agme_pheno_01s.size() - successRowCount_01) + "\n");
				break;
			case ReadIni.SECTION_PHENO_02:
				List<Object> agme_pheno_02s = agme_Pheno.agme_pheno_02s;
				d_data_id = ini.getValue(ReadIni.SECTION_PHENO_02, ReadIni.D_DATA_ID_KEY);
				int successRowCount_02 = agme_pheno_02s.size();
				for (Object obj : agme_pheno_02s) {
					Agme_Pheno_02 agme_Pheno_02 = (Agme_Pheno_02) obj;
					Map<String, Object> row = new HashMap<String, Object>();
					
					String stationNumberC = agme_Pheno_02.getStationNumberChina();
					
					String stationNumberN = StationCodeUtil.stringToAscii(stationNumberC);
					// 根据站号查询行政区划代码
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
					
					Date obsTime = agme_Pheno_02.getAppearTime();

					Double latitude = agme_Pheno_02.getLatitude(); // 纬度
					Double longitude = agme_Pheno_02.getLongitude(); // 经度

					Double heightOfSationGroundAboveMeanSeaLevel = agme_Pheno_02.getHeightOfSationGroundAboveMeanSeaLevel();// 测站海拔高度
					Double heightOfBarometerAboveMeanSeaLevel = agme_Pheno_02.getHeightOfBarometerAboveMeanSeaLevel();// 气压传感器海拔高度

					Double plantOrAnimalName = agme_Pheno_02.getPlantOrAnimalName(); // 动植物名称
					plantOrAnimalName = NumberUtil.FormatNumOrNine(plantOrAnimalName).doubleValue();
					Double phenologyName = agme_Pheno_02.getPhenologyName(); // 物候期名称
					phenologyName = NumberUtil.FormatNumOrNine(phenologyName).doubleValue();
					String id = TimeUtil.date2String(obsTime, TimeUtil.DATE_FMT_YMDHMS) //
							+ "_" + (lengthStr.substring(0, 5 - stationNumberC.length()) + stationNumberC) //
							+ "_" + plantOrAnimalName + "_" + phenologyName;
					row.put("D_RECORD_ID", id);
					row.put("D_DATA_ID",d_data_id);
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_DATETIME",TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss"));
					row.put("V01301", stationNumberC);
					row.put("V01300", stationNumberN);
					latitude = NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, latitude) ? latitude : ElementValUtil.getlatitude(String.valueOf(latitude))).doubleValue();
					row.put("V05001", latitude);
					longitude =  NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, longitude) ? longitude : ElementValUtil.getLongitude(String.valueOf(longitude))).doubleValue();
					row.put("V06001",longitude);
//					heightOfSationGroundAboveMeanSeaLevel = NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, heightOfSationGroundAboveMeanSeaLevel) ? heightOfSationGroundAboveMeanSeaLevel
//							: (heightOfSationGroundAboveMeanSeaLevel / 10.0)).doubleValue();
					
					heightOfSationGroundAboveMeanSeaLevel = (heightOfSationGroundAboveMeanSeaLevel / 10.0); // 测站海拔高度
					
					row.put("V07001",heightOfSationGroundAboveMeanSeaLevel);   
//					heightOfBarometerAboveMeanSeaLevel = NumberUtil.FormatNumOrNine(
//							ArrayUtils.contains(NINE_ARRAYS, heightOfBarometerAboveMeanSeaLevel) ? heightOfBarometerAboveMeanSeaLevel : (heightOfBarometerAboveMeanSeaLevel / 10.0)).doubleValue();
					heightOfBarometerAboveMeanSeaLevel = heightOfBarometerAboveMeanSeaLevel.doubleValue() == 99999.0 ? 999999 : heightOfBarometerAboveMeanSeaLevel / 10.0;
					row.put("V07031",heightOfBarometerAboveMeanSeaLevel);
					row.put("V_ACODE", adminCode); // V_ACODE		
					row.put("V04001", obsTime.getYear()+1900);
					row.put("V04002", obsTime.getMonth()+1);
					row.put("V04003", obsTime.getDate());
					row.put("V71501",plantOrAnimalName);
					row.put("V71618", phenologyName);
					row.put("V_BBB", v_bbb);
					row.put("D_SOURCE_ID", cts_code);
					
					StatDi di = new StatDi();					
					di.setFILE_NAME_O(fileName);
					di.setDATA_TYPE(d_data_id);
					di.setDATA_TYPE_1(cts_code);
					di.setTT("PHENO_02");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileName);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(agme_Pheno_02.getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(agme_Pheno_02.getAppearTime(), "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLATITUDE(String.valueOf(latitude));
					di.setLONGTITUDE(String.valueOf(longitude));
					try {
						if(isUpdate) {
							OTSDbHelper.getInstance().update("AGME_PHENO01_CHN_TAB",row);
						}else {
							OTSDbHelper.getInstance().insert("AGME_PHENO01_CHN_TAB", row);
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
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (agme_pheno_02s.size() - successRowCount_02) + "\n");
				
				break;
			case ReadIni.SECTION_PHENO_03:
				List<Object> agme_pheno_03s = agme_Pheno.agme_pheno_03s;
				d_data_id = ini.getValue(ReadIni.SECTION_PHENO_03, ReadIni.D_DATA_ID_KEY);
				int successRowCount_03= agme_pheno_03s.size();
				for (Object obj : agme_pheno_03s) {
					Agme_Pheno_03 agme_Pheno_03 = (Agme_Pheno_03) obj;
					Map<String, Object> row = new HashMap<String, Object>();
					
					String stationNumberC = agme_Pheno_03.getStationNumberChina();
					String stationNumberN = StationCodeUtil.stringToAscii(stationNumberC);
					// 根据站号查询行政区划代码
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
					Date obsTime = agme_Pheno_03.getAppearTime();

					Double latitude = agme_Pheno_03.getLatitude(); // 纬度
					Double longitude = agme_Pheno_03.getLongitude(); // 经度

					Double heightOfSationGroundAboveMeanSeaLevel = agme_Pheno_03.getHeightOfSationGroundAboveMeanSeaLevel();// 测站海拔高度
					Double heightOfBarometerAboveMeanSeaLevel = agme_Pheno_03.getHeightOfBarometerAboveMeanSeaLevel();// 气压传感器海拔高度

					Double hydrologicalPhenomenonName = agme_Pheno_03.getHydrologicalPhenomenonName(); // 水文现象名称
					hydrologicalPhenomenonName =NumberUtil.FormatNumOrNine(hydrologicalPhenomenonName).doubleValue();
					// 时间(yyyyMMddHHmmss)_微秒（毫秒转微秒）SSSSSS_站号5_经度10_纬度9_海拔或高度相关8_(V_BBB)_其他字段
					String id = TimeUtil.date2String(obsTime, TimeUtil.DATE_FMT_YMDHMS) //
							+ "_" + (lengthStr.substring(0, 5 - stationNumberC.length()) + stationNumberC) //
							+ "_" + hydrologicalPhenomenonName;
					row.put("D_RECORD_ID", id);
					row.put("D_DATA_ID",d_data_id);
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_DATETIME",TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss"));
					row.put("V01301", stationNumberC);
					row.put("V01300", stationNumberN);
					latitude = NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, latitude) ? latitude : ElementValUtil.getlatitude(String.valueOf(latitude))).doubleValue();
					row.put("V05001", latitude);
					longitude =  NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, longitude) ? longitude : ElementValUtil.getLongitude(String.valueOf(longitude))).doubleValue();
					row.put("V06001",longitude);
//					heightOfSationGroundAboveMeanSeaLevel = NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, heightOfSationGroundAboveMeanSeaLevel) ? heightOfSationGroundAboveMeanSeaLevel
//							: (heightOfSationGroundAboveMeanSeaLevel / 10.0)).doubleValue();
					
					heightOfSationGroundAboveMeanSeaLevel = (heightOfSationGroundAboveMeanSeaLevel / 10.0); // 测站海拔高度
					
					row.put("V07001",heightOfSationGroundAboveMeanSeaLevel);   
//					heightOfBarometerAboveMeanSeaLevel = NumberUtil.FormatNumOrNine(
//							ArrayUtils.contains(NINE_ARRAYS, heightOfBarometerAboveMeanSeaLevel) ? heightOfBarometerAboveMeanSeaLevel : (heightOfBarometerAboveMeanSeaLevel / 10.0)).doubleValue();
					
					heightOfBarometerAboveMeanSeaLevel = heightOfBarometerAboveMeanSeaLevel.doubleValue() == 99999.0 ? 999999 : heightOfBarometerAboveMeanSeaLevel / 10.0;
					row.put("V07031",heightOfBarometerAboveMeanSeaLevel);
					row.put("V_ACODE", adminCode); // V_ACODE		
					row.put("V04001", obsTime.getYear()+1900);
					row.put("V04002", obsTime.getMonth()+1);
					row.put("V04003", obsTime.getDate());
					row.put("V71300", hydrologicalPhenomenonName);
					row.put("V_BBB", v_bbb);
					row.put("D_SOURCE_ID", cts_code);
					
					StatDi di = new StatDi();					
					di.setFILE_NAME_O(fileName);
					di.setDATA_TYPE(d_data_id);
					di.setDATA_TYPE_1(cts_code);
					di.setTT("PHENO_03");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileName);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(agme_Pheno_03.getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(agme_Pheno_03.getAppearTime(), "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLATITUDE(String.valueOf(latitude));
					di.setLONGTITUDE(String.valueOf(longitude));
					
					try {
						if(isUpdate) {
							OTSDbHelper.getInstance().update("AGME_PHENO03_CHN_TAB",row);
						}else {
							OTSDbHelper.getInstance().insert("AGME_PHENO03_CHN_TAB", row);
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
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (agme_pheno_03s.size() - successRowCount_03) + "\n");
				
				break;
			case ReadIni.SECTION_PHENO_04:
				List<Object> agme_pheno_04s = agme_Pheno.agme_pheno_04s;
				d_data_id = ini.getValue(ReadIni.SECTION_PHENO_04, ReadIni.D_DATA_ID_KEY);
				int successRowCount_04 = agme_pheno_04s.size();
				for (Object obj : agme_pheno_04s) {
					Agme_Pheno_04 agme_Pheno_04 = (Agme_Pheno_04) obj;
					Map<String, Object> row = new HashMap<String, Object>();
					
					String stationNumberC = agme_Pheno_04.getStationNumberChina();
					String stationNumberN = StationCodeUtil.stringToAscii(stationNumberC);
					// 根据站号查询行政区划代码
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
					Date obsTime = agme_Pheno_04.getAppearTime();

					Double latitude = agme_Pheno_04.getLatitude(); // 纬度
					Double longitude = agme_Pheno_04.getLongitude(); // 经度

					Double heightOfSationGroundAboveMeanSeaLevel = agme_Pheno_04.getHeightOfSationGroundAboveMeanSeaLevel();// 测站海拔高度
					Double heightOfBarometerAboveMeanSeaLevel = agme_Pheno_04.getHeightOfBarometerAboveMeanSeaLevel();// 气压传感器海拔高度

					Double plantOrAnimalName = agme_Pheno_04.getPlantOrAnimalName(); // 动植物名称
					plantOrAnimalName = NumberUtil.FormatNumOrNine(plantOrAnimalName).doubleValue();
					Double phenologyName = agme_Pheno_04.getPhenologyName(); // 物候期名称
					phenologyName = NumberUtil.FormatNumOrNine(phenologyName).doubleValue();
					String id = TimeUtil.date2String(obsTime, TimeUtil.DATE_FMT_YMDHMS) //
							+ "_" + (lengthStr.substring(0, 5 - stationNumberC.length()) + stationNumberC) //
							+ "_" + plantOrAnimalName + "_" + phenologyName;
					row.put("D_RECORD_ID", id);
					row.put("D_DATA_ID",d_data_id);
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_DATETIME",TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss"));
					row.put("V01301", stationNumberC);
					row.put("V01300", stationNumberN);
					latitude = NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, latitude) ? latitude : ElementValUtil.getlatitude(String.valueOf(latitude))).doubleValue();
					row.put("V05001", latitude);
					longitude =  NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, longitude) ? longitude : ElementValUtil.getLongitude(String.valueOf(longitude))).doubleValue();
					row.put("V06001",longitude);
//					heightOfSationGroundAboveMeanSeaLevel = NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, heightOfSationGroundAboveMeanSeaLevel) ? heightOfSationGroundAboveMeanSeaLevel
//							: (heightOfSationGroundAboveMeanSeaLevel / 10.0)).doubleValue();
					heightOfSationGroundAboveMeanSeaLevel = (heightOfSationGroundAboveMeanSeaLevel / 10.0); // 测站海拔高度
					
					row.put("V07001",heightOfSationGroundAboveMeanSeaLevel);   
//					heightOfBarometerAboveMeanSeaLevel = NumberUtil.FormatNumOrNine(
//							ArrayUtils.contains(NINE_ARRAYS, heightOfBarometerAboveMeanSeaLevel) ? heightOfBarometerAboveMeanSeaLevel : (heightOfBarometerAboveMeanSeaLevel / 10.0)).doubleValue();
					
					heightOfBarometerAboveMeanSeaLevel = heightOfBarometerAboveMeanSeaLevel.doubleValue() == 99999.0 ? 999999 : heightOfBarometerAboveMeanSeaLevel / 10.0;
					row.put("V07031",heightOfBarometerAboveMeanSeaLevel);
					row.put("V_ACODE", adminCode); // V_ACODE		
					row.put("V04001", obsTime.getYear()+1900);
					row.put("V04002", obsTime.getMonth()+1);
					row.put("V04003", obsTime.getDate());
					row.put("V71501",plantOrAnimalName);
					row.put("V71618", phenologyName);
					row.put("V_BBB", v_bbb);
					row.put("D_SOURCE_ID", cts_code);
					
					StatDi di = new StatDi();					
					di.setFILE_NAME_O(fileName);
					di.setDATA_TYPE(d_data_id);
					di.setDATA_TYPE_1(cts_code);
					di.setTT("PHENO_04");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileName);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(agme_Pheno_04.getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(agme_Pheno_04.getAppearTime(), "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLATITUDE(String.valueOf(latitude));
					di.setLONGTITUDE(String.valueOf(longitude));
					
					try {
						if(isUpdate) {
							OTSDbHelper.getInstance().update("AGME_PHENO01_CHN_TAB",row);
						}else {
							OTSDbHelper.getInstance().insert("AGME_PHENO01_CHN_TAB", row);
						}
						diQueues.offer(di);
					}   catch (Exception e) {
						successRowCount_04 = successRowCount_04 -1;
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
			    loggerBuffer.append(" INSERT SUCCESS COUNT : " + successRowCount_04 + "\n");
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (agme_pheno_04s.size() - successRowCount_04) + "\n");
				
				break;
			default:
				break;
			}

		}
		
		return DataBaseAction.SUCCESS;
	}
	
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
				Double longitude = agmeReportHeader.getLongitude();
//				latitude = NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, latitude) ? latitude : (latitude / 10000.0)).doubleValue();
//				longitude =  NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, longitude) ? longitude : (longitude / 10000.0)).doubleValue();
				latitude = NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, latitude) ? latitude : ElementValUtil.getlatitude(String.valueOf(latitude))).doubleValue();
				longitude = NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, longitude) ? longitude : ElementValUtil.getLongitude(String.valueOf(longitude))).doubleValue();
				row.put("V05001", latitude);
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
