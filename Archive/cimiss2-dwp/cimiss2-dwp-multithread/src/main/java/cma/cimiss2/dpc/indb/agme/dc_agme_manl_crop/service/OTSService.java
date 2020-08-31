package cma.cimiss2.dpc.indb.agme.dc_agme_manl_crop.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Pattern;

import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.config.CTSCodeMap;
import org.cimiss2.dwp.tools.ots.OTSDbHelper;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alicloud.openservices.tablestore.ClientException;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCrop;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCrop01;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCrop02;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCrop03;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCrop04;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCrop05;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCrop06;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCrop07;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCrop08;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCrop09;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCrop10;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {
	
	public static BlockingQueue<StatDi> diQueues;
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static int defaultInt = 999999;
//	static Map<String, Object> proMap = StationInfo.getProMap();
	@SuppressWarnings("deprecation")
	public static DataBaseAction processSuccessReport(ParseResult<ZAgmeCrop> parseResult, Date recv_time, String fileN,
			boolean isRevised, String v_bbb, StringBuffer loggerBuffer, List<CTSCodeMap> ctsCodeMaps) {
		Map<String, Object> proMap = StationInfo.getProMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		List<ZAgmeCrop> agmeCrops = parseResult.getData();
		for (String cropType : agmeCrops.get(0).cropTypes) {
			switch (cropType) {
			case "CROP-01":
				List<ZAgmeCrop01> crop01 = agmeCrops.get(0).zAgmeCrop01s;
				int successRowCount_01 = crop01.size();
				for (int i = 0; i < crop01.size(); i++) {
					Map<String, Object> row = new HashMap<String, Object>();
					
					int stationNumberN = defaultInt;
					String stat = crop01.get(i).getStationNumberChina();
					if (Pattern.matches("\\d{5}", stat))
						stationNumberN = Integer.parseInt(stat);					
							
					String info = (String) proMap.get(stat + "+12");
					String adminCode = "999999";
					if(info != null) {
						String[] infos = info.split(",");
						if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
							adminCode = infos[5];
					}
					
					if(adminCode.startsWith("999999")){
						info = (String) proMap.get(stat + "+01");
						if(info == null) {
							loggerBuffer.append("\n In the configuration file, the station number does not exist" + stat);
						}else {
							String[] infos = info.split(",");
							if(infos.length >= 6)
								adminCode = infos[5];
						}
					}
					
							
					row.put("D_RECORD_ID", sdf.format(crop01.get(i).getGrowthDate())+"_"+crop01.get(i).getStationNumberChina()+"_"+crop01.get(i).getCropName()+"_"+crop01.get(i).getPeriodOfGrowth());
//					row.put("D_DATA_ID", AgmeSodType.CROP01.getSod_code());
					row.put("D_DATA_ID", ctsCodeMaps.get(0).getSod_code());
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					row.put("D_DATETIME",TimeUtil.date2String(crop01.get(i).getGrowthDate(), "yyyy-MM-dd HH:mm:ss"));
					row.put("V01301", crop01.get(i).getStationNumberChina());
					row.put("V01300", stationNumberN);
					row.put("V05001", crop01.get(i).getLatitude());
					row.put("V06001", crop01.get(i).getLongitude());
					row.put("V07001", crop01.get(i).getHeightOfSationGroundAboveMeanSeaLevel());              
					row.put("V07031", crop01.get(i).getHeightOfPressureSensor());
					row.put("V_ACODE", adminCode); // V_ACODE		
					row.put("V04001", crop01.get(i).getGrowthDate().getYear() + 1900);
					row.put("V04002", crop01.get(i).getGrowthDate().getMonth() + 1);
					row.put("V04003", crop01.get(i).getGrowthDate().getDate());
					row.put("V71001", crop01.get(i).getCropName());
					row.put("V71002", crop01.get(i).getPeriodOfGrowth());
					row.put("V04001_03", crop01.get(i).getGrowthDate().getYear() + 1900);
					row.put("V04002_03", crop01.get(i).getGrowthDate().getMonth() +1);
					row.put("V04003_03", crop01.get(i).getGrowthDate().getDate());
					row.put("V71005", crop01.get(i).getDevelopAnomaly());
					row.put("V71010", crop01.get(i).getPercentageOfGrowthPeriod());
					row.put("V71007", crop01.get(i).getGrowthState());
					row.put("V71006", crop01.get(i).getPlantHeight());
					row.put("V71008", crop01.get(i).getPlantDensity());
					row.put("V_BBB", v_bbb);
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_SOURCE_ID", ctsCodeMaps.get(0).getCts_code());
					
					StatDi di = new StatDi();					
					di.setFILE_NAME_O(fileN);
//					di.setDATA_TYPE(AgmeSodType.CROP01.getSod_code());
//					di.setDATA_TYPE_1(AgmeSodType.CROP01.getCts_code());
					di.setDATA_TYPE(ctsCodeMaps.get(0).getSod_code());
					di.setDATA_TYPE_1(ctsCodeMaps.get(0).getCts_code());
					di.setTT("crop01");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(crop01.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(crop01.get(i).getGrowthDate(), "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLONGTITUDE(String.valueOf(crop01.get(i).getLongitude()));
					di.setLATITUDE(String.valueOf(crop01.get(i).getLatitude()));
					
					try {
						if(isRevised) {
							OTSDbHelper.getInstance().update(ctsCodeMaps.get(0).getValue_table_name(),row);
						}else {
							OTSDbHelper.getInstance().insert(ctsCodeMaps.get(0).getValue_table_name(), row);
						}
						diQueues.offer(di);
					}  catch (Exception e) {
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
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (crop01.size() - successRowCount_01) + "\n");
				break;

			case "CROP-02":
				List<ZAgmeCrop02> crop02 = agmeCrops.get(0).zAgmeCrop02s;
				int successRowCount_02 = crop02.size();
				for (int i = 0; i < crop02.size(); i++) {
					Map<String, Object> row = new HashMap<String, Object>();
	
					int stationNumberN = defaultInt;
					String stat = crop02.get(i).getStationNumberChina();
					if (Pattern.matches("\\d{5}", stat))
						stationNumberN = Integer.parseInt(stat);					
						
					String info = (String) proMap.get(stat + "+12");
					String adminCode = "999999";
					if(info != null) {
						String[] infos = info.split(",");
						if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
							adminCode = infos[5];
					}
					
					if(adminCode.startsWith("999999")){
						info = (String) proMap.get(stat + "+01");
						if(info == null) {
							loggerBuffer.append("\n In the configuration file, the station number does not exist" + stat);
						}else {
							String[] infos = info.split(",");
							if(infos.length >= 6)
								adminCode = infos[5];
						}
					}
					
					row.put("D_RECORD_ID", sdf.format(crop02.get(i).getObservationDate())+"_"+crop02.get(i).getStationNumberChina()+"_"+crop02.get(i).getCropName()+"_"+crop02.get(i).getPeriodOfGrowth());
//					row.put("D_DATA_ID", AgmeSodType.CROP02.getSod_code());
					row.put("D_DATA_ID", ctsCodeMaps.get(1).getSod_code());
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					row.put("D_DATETIME",TimeUtil.date2String(crop02.get(i).getObservationDate(), "yyyy-MM-dd HH:mm:ss"));
					row.put("V01301", crop02.get(i).getStationNumberChina());
					row.put("V01300", stationNumberN);
					row.put("V05001", crop02.get(i).getLatitude());
					row.put("V06001", crop02.get(i).getLongitude());
					row.put("V07001", crop02.get(i).getHeightOfSationGroundAboveMeanSeaLevel());              
					row.put("V07031", crop02.get(i).getHeightOfPressureSensor());
					row.put("V_ACODE", adminCode); // V_ACODE		
					row.put("V04001", crop02.get(i).getObservationDate().getYear() + 1900 );
					row.put("V04002", crop02.get(i).getObservationDate().getMonth() + 1);
					row.put("V04003", crop02.get(i).getObservationDate().getDate());
					row.put("V71001", crop02.get(i).getCropName());
					row.put("V71002", crop02.get(i).getPeriodOfGrowth());
					row.put("V71656", crop02.get(i).getPercentageOfGrowth());
					row.put("V71655", crop02.get(i).getPercentageOfWater());
					row.put("V71604", crop02.get(i).getIndexOfLeafArea());
					row.put("V_BBB", v_bbb);
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_SOURCE_ID", ctsCodeMaps.get(1).getCts_code());
					
					StatDi di = new StatDi();					
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMaps.get(1).getSod_code());
					di.setDATA_TYPE_1(ctsCodeMaps.get(1).getCts_code());
					di.setTT("crop02");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(crop02.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(crop02.get(i).getObservationDate(), "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLONGTITUDE(String.valueOf(crop02.get(i).getLongitude()));
					di.setLATITUDE(String.valueOf(crop02.get(i).getLatitude()));
					
					try {
						if(isRevised) {
							OTSDbHelper.getInstance().update(ctsCodeMaps.get(1).getValue_table_name(),row);
						}else {
							OTSDbHelper.getInstance().insert(ctsCodeMaps.get(1).getValue_table_name(), row);
						}
						diQueues.offer(di);
					}  catch (Exception e) {
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
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (crop02.size() - successRowCount_02) + "\n");
				break;
			case "CROP-03":
				List<ZAgmeCrop03> crop03 = agmeCrops.get(0).zAgmeCrop03s;
				int successRowCount_03 = crop03.size();
				for (int i = 0; i < crop03.size(); i++) {
					Map<String, Object> row = new HashMap<String, Object>();
					
					int stationNumberN = defaultInt;
					String stat = crop03.get(i).getStationNumberChina();
					if (Pattern.matches("\\d{5}", stat))
						stationNumberN = Integer.parseInt(stat);					
					String info = (String) proMap.get(stat + "+12");
					String adminCode = "999999";
					if(info != null) {
						String[] infos = info.split(",");
						if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
							adminCode = infos[5];
					}
					
					if(adminCode.startsWith("999999")){
						info = (String) proMap.get(stat + "+01");
						if(info == null) {
							loggerBuffer.append("\n In the configuration file, the station number does not exist" + stat);
						}else {
							String[] infos = info.split(",");
							if(infos.length >= 6)
								adminCode = infos[5];
						}
					}
					
					row.put("D_RECORD_ID", sdf.format(crop03.get(i).getObservationDate())+"_"+crop03.get(i).getStationNumberChina()+"_"+crop03.get(i).getCropName());
					row.put("D_DATA_ID", ctsCodeMaps.get(2).getSod_code());
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					row.put("D_DATETIME",TimeUtil.date2String(crop03.get(i).getObservationDate(), "yyyy-MM-dd HH:mm:ss"));
					row.put("V01301", crop03.get(i).getStationNumberChina());
					row.put("V01300", stationNumberN);
					row.put("V05001", crop03.get(i).getLatitude());
					row.put("V06001", crop03.get(i).getLongitude());
					row.put("V07001", crop03.get(i).getHeightOfSationGroundAboveMeanSeaLevel());              
					row.put("V07031", crop03.get(i).getHeightOfPressureSensor());
					row.put("V_ACODE", adminCode); // V_ACODE		
					row.put("V04001", crop03.get(i).getObservationDate().getYear() + 1900);
					row.put("V04002", crop03.get(i).getObservationDate().getMonth() + 1);
					row.put("V04003", crop03.get(i).getObservationDate().getDate());
					row.put("V71001", crop03.get(i).getCropName());
					row.put("V71655", crop03.get(i).getPercentageOfWater());
					row.put("V71089", crop03.get(i).getDryWeight());
					row.put("V71900", crop03.get(i).getFillingRate());
					row.put("V_BBB", v_bbb);
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_SOURCE_ID", ctsCodeMaps.get(2).getCts_code());
					
					StatDi di = new StatDi();					
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMaps.get(2).getSod_code());
					di.setDATA_TYPE_1(ctsCodeMaps.get(2).getCts_code());
					di.setTT("crop03");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(crop03.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(crop03.get(i).getObservationDate(), "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLONGTITUDE(String.valueOf(crop03.get(i).getLongitude()));
					di.setLATITUDE(String.valueOf(crop03.get(i).getLatitude()));

					try {
						if(isRevised) {
							OTSDbHelper.getInstance().update(ctsCodeMaps.get(2).getValue_table_name(),row);
						}else {
							OTSDbHelper.getInstance().insert(ctsCodeMaps.get(2).getValue_table_name(), row);
						}
						diQueues.offer(di);
					}  catch (Exception e) {
						successRowCount_03 = successRowCount_03 -1;
						di.setPROCESS_STATE("1");
						diQueues.offer(di);
						loggerBuffer.append(row);
					}
				}
				loggerBuffer.append(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
			    loggerBuffer.append(" INSERT SUCCESS COUNT : " + successRowCount_03 + "\n");
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (crop03.size() - successRowCount_03) + "\n");

				break;
			case "CROP-04":
				List<ZAgmeCrop04> crop04 = agmeCrops.get(0).zAgmeCrop04s;
				int successRowCount_04 = crop04.size();
				for (int i = 0; i < crop04.size(); i++) {
					Map<String, Object> row = new HashMap<String, Object>();
					
					int stationNumberN = defaultInt;
					String stat = crop04.get(i).getStationNumberChina();
					if (Pattern.matches("\\d{5}", stat))
						stationNumberN = Integer.parseInt(stat);					
					String info = (String) proMap.get(stat + "+12");
					String adminCode = "999999";
					if(info != null) {
						String[] infos = info.split(",");
						if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
							adminCode = infos[5];
					}
					
					if(adminCode.startsWith("999999")){
						info = (String) proMap.get(stat + "+01");
						if(info == null) {
							loggerBuffer.append("\n In the configuration file, the station number does not exist" + stat);
						}else {
							String[] infos = info.split(",");
							if(infos.length >= 6)
								adminCode = infos[5];
						}
					}
					row.put("D_RECORD_ID", sdf.format(crop04.get(i).getObservationDate())+"_"+crop04.get(i).getStationNumberChina()+"_"+crop04.get(i).getCropName()+"_"+crop04.get(i).getPeriodOfGrowth()+"_"+crop04.get(i).getItemName1());
					row.put("D_DATA_ID", ctsCodeMaps.get(3).getSod_code());
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					row.put("D_DATETIME",TimeUtil.date2String(crop04.get(i).getObservationDate(), "yyyy-MM-dd HH:mm:ss"));
					row.put("V01301", crop04.get(i).getStationNumberChina());
					row.put("V01300", stationNumberN);
					row.put("V05001", crop04.get(i).getLatitude());
					row.put("V06001", crop04.get(i).getLongitude());
					row.put("V07001", crop04.get(i).getHeightOfSationGroundAboveMeanSeaLevel());              
					row.put("V07031", crop04.get(i).getHeightOfPressureSensor());
					row.put("V_ACODE", adminCode); // V_ACODE		
					row.put("V04001", crop04.get(i).getObservationDate().getYear() + 1900);
					row.put("V04002", crop04.get(i).getObservationDate().getMonth() + 1);
					row.put("V04003", crop04.get(i).getObservationDate().getDate());
					row.put("V71001", crop04.get(i).getCropName());
					row.put("V71002", crop04.get(i).getPeriodOfGrowth());
					row.put("V71616_01", crop04.get(i).getItemName1());
					row.put("V71632", crop04.get(i).getObservationValue());
					row.put("V_BBB", v_bbb);
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_SOURCE_ID", ctsCodeMaps.get(3).getCts_code());
					
					StatDi di = new StatDi();					
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMaps.get(3).getSod_code());
					di.setDATA_TYPE_1(ctsCodeMaps.get(3).getCts_code());
					di.setTT("crop04");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(crop04.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(crop04.get(i).getObservationDate(), "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLONGTITUDE(String.valueOf(crop04.get(i).getLongitude()));
					di.setLATITUDE(String.valueOf(crop04.get(i).getLatitude()));

					try {
						if(isRevised) {
							OTSDbHelper.getInstance().update(ctsCodeMaps.get(3).getValue_table_name(),row);
						}else {
							OTSDbHelper.getInstance().insert(ctsCodeMaps.get(3).getValue_table_name(), row);
						}
						diQueues.offer(di);
					}  catch (Exception e) {
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
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (crop04.size() - successRowCount_04) + "\n");

				break;
			case "CROP-05":
				List<ZAgmeCrop05> crop05 = agmeCrops.get(0).zAgmeCrop05s;
				int successRowCount_05 = crop05.size();
				for (int i = 0; i < crop05.size(); i++) {
					Map<String, Object> row = new HashMap<String, Object>();
					int stationNumberN = defaultInt;
					String stat = crop05.get(i).getStationNumberChina();
					if (Pattern.matches("\\d{5}", stat))
						stationNumberN = Integer.parseInt(stat);					
					
					String info = (String) proMap.get(stat + "+12");
					String adminCode = "999999";
					if(info != null) {
						String[] infos = info.split(",");
						if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
							adminCode = infos[5];
					}
					
					if(adminCode.startsWith("999999")){
						info = (String) proMap.get(stat + "+01");
						if(info == null) {
							loggerBuffer.append("\n In the configuration file, the station number does not exist" + stat);
						}else {
							String[] infos = info.split(",");
							if(infos.length >= 6)
								adminCode = infos[5];
						}
					}
					
					row.put("D_RECORD_ID", sdf.format(crop05.get(i).getObservationDate())+"_"+crop05.get(i).getStationNumberChina()+"_"+crop05.get(i).getCropName()+"_"+defaultInt+"_"+crop05.get(i).getItemName1());
					row.put("D_DATA_ID", ctsCodeMaps.get(4).getSod_code());
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					row.put("D_DATETIME",TimeUtil.date2String(crop05.get(i).getObservationDate(), "yyyy-MM-dd HH:mm:ss"));
					row.put("V01301", crop05.get(i).getStationNumberChina());
					row.put("V01300", stationNumberN);
					row.put("V05001", crop05.get(i).getLatitude());
					row.put("V06001", crop05.get(i).getLongitude());
					row.put("V07001", crop05.get(i).getHeightOfSationGroundAboveMeanSeaLevel());              
					row.put("V07031", crop05.get(i).getHeightOfPressureSensor());
					row.put("V_ACODE", adminCode); // V_ACODE		
					row.put("V04001", crop05.get(i).getObservationDate().getYear() + 1900);
					row.put("V04002", crop05.get(i).getObservationDate().getMonth() + 1);
					row.put("V04003", crop05.get(i).getObservationDate().getDate());
					row.put("V71001", crop05.get(i).getCropName());
					row.put("V71002", defaultInt);
					row.put("V71616_01", crop05.get(i).getItemName1());
					row.put("V71632", crop05.get(i).getObservationValue());
					row.put("V_BBB", v_bbb);
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_SOURCE_ID", ctsCodeMaps.get(4).getCts_code());
					
					StatDi di = new StatDi();					
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMaps.get(4).getSod_code());
					di.setDATA_TYPE_1(ctsCodeMaps.get(4).getCts_code());
					di.setTT("crop05");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(crop05.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(crop05.get(i).getObservationDate(), "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLONGTITUDE(String.valueOf(crop05.get(i).getLongitude()));
					di.setLATITUDE(String.valueOf(crop05.get(i).getLatitude()));
					
					try {
						if(isRevised) {
							OTSDbHelper.getInstance().update(ctsCodeMaps.get(4).getValue_table_name(),row);
						}else {
							OTSDbHelper.getInstance().insert(ctsCodeMaps.get(4).getValue_table_name(), row);
						}
						diQueues.offer(di);
					}  catch (Exception e) {
						successRowCount_05 = successRowCount_05 -1;
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
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (crop05.size() - successRowCount_05) + "\n");
				break;
			case "CROP-06":
				List<ZAgmeCrop06> crop06 = agmeCrops.get(0).zAgmeCrop06s;
				int successRowCount_06 = crop06.size();
				for (int i = 0; i < crop06.size(); i++) {
					Map<String, Object> row = new HashMap<String, Object>();
					
					int stationNumberN = defaultInt;
					String stat = crop06.get(i).getStationNumberChina();
					if (Pattern.matches("\\d{5}", stat))
						stationNumberN = Integer.parseInt(stat);					
					
					String info = (String) proMap.get(stat + "+12");
					String adminCode = "999999";
					if(info != null) {
						String[] infos = info.split(",");
						if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
							adminCode = infos[5];
					}
					
					if(adminCode.startsWith("999999")){
						info = (String) proMap.get(stat + "+01");
						if(info == null) {
							loggerBuffer.append("\n In the configuration file, the station number does not exist" + stat);
						}else {
							String[] infos = info.split(",");
							if(infos.length >= 6)
								adminCode = infos[5];
						}
					}
					
					row.put("D_RECORD_ID", sdf.format(crop06.get(i).getStartTime())+"_"+crop06.get(i).getStationNumberChina()+"_"+crop06.get(i).getCropName()+"_"+crop06.get(i).getItemName2());
					row.put("D_DATA_ID", ctsCodeMaps.get(5).getSod_code());
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					row.put("D_DATETIME",TimeUtil.date2String(crop06.get(i).getStartTime(), "yyyy-MM-dd HH:mm:ss"));
					row.put("V01301", crop06.get(i).getStationNumberChina());
					row.put("V01300", stationNumberN);
					row.put("V05001", crop06.get(i).getLatitude());
					row.put("V06001", crop06.get(i).getLongitude());
					row.put("V07001", crop06.get(i).getHeightOfSationGroundAboveMeanSeaLevel());              
					row.put("V07031", crop06.get(i).getHeightOfPressureSensor());
					row.put("V_ACODE", adminCode); // V_ACODE		
					row.put("V04001", crop06.get(i).getStartTime().getYear() + 1900);
					row.put("V04002", crop06.get(i).getStartTime().getMonth() + 1);
					row.put("V04003", crop06.get(i).getStartTime().getDate());
					row.put("V04300_017", sdf.format(crop06.get(i).getStartTime()));
					row.put("V04300_018",sdf.format(crop06.get(i).getEndTime()));
					row.put("V71001", crop06.get(i).getCropName());
					row.put("V71616_02", crop06.get(i).getItemName2());
					row.put("V71901", crop06.get(i).getQuality());
					row.put("V71902", crop06.get(i).getMethodAndTool());
					row.put("V_BBB", v_bbb);
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_SOURCE_ID", ctsCodeMaps.get(5).getCts_code());
					
					StatDi di = new StatDi();					
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMaps.get(5).getSod_code());
					di.setDATA_TYPE_1(ctsCodeMaps.get(5).getCts_code());
					di.setTT("crop06");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(crop06.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(crop06.get(i).getStartTime(), "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLONGTITUDE(String.valueOf(crop06.get(i).getLongitude()));
					di.setLATITUDE(String.valueOf(crop06.get(i).getLatitude()));

					try {
						if(isRevised) {
							OTSDbHelper.getInstance().update(ctsCodeMaps.get(5).getValue_table_name(),row);
						}else {
							OTSDbHelper.getInstance().insert(ctsCodeMaps.get(5).getValue_table_name(), row);
						}
						diQueues.offer(di);
					}  catch (Exception e) {
						successRowCount_06= successRowCount_06 -1;
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
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (crop06.size() - successRowCount_06) + "\n");

				break;
			case "CROP-07":
				List<ZAgmeCrop07> crop07 = agmeCrops.get(0).zAgmeCrop07s;
				int successRowCount_07 = crop07.size();
				
				for (int i = 0; i < crop07.size(); i++) {
					Map<String, Object> row = new HashMap<String, Object>();
					
					int stationNumberN = defaultInt;
					String stat = crop07.get(i).getStationNumberChina();
					if (Pattern.matches("\\d{5}", stat))
						stationNumberN = Integer.parseInt(stat);					
					
					String info = (String) proMap.get(stat + "+12");
					String adminCode = "999999";
					if(info != null) {
						String[] infos = info.split(",");
						if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
							adminCode = infos[5];
					}
					
					if(adminCode.startsWith("999999")){
						info = (String) proMap.get(stat + "+01");
						if(info == null) {
							loggerBuffer.append("\n In the configuration file, the station number does not exist" + stat);
						}else {
							String[] infos = info.split(",");
							if(infos.length >= 6)
								adminCode = infos[5];
						}
					}
					//SimpleDateFormat yearsdf = new SimpleDateFormat("yyyy");
					String DateStr = crop07.get(i).getYear() + "0101000000";
					row.put("D_RECORD_ID", DateStr +"_"+crop07.get(i).getStationNumberChina()+"_"+crop07.get(i).getCropName());
					row.put("D_DATA_ID", ctsCodeMaps.get(6).getSod_code());
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					/**
						try {
						row.put("D_DATETIME",TimeUtil.date2String(yearsdf.parse(String.valueOf(crop07.get(i).getYear())), "yyyy"));
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					 */
					row.put("D_DATETIME", String.valueOf(TimeUtil.date2String(TimeUtil.String2Date(DateStr, "yyyyMMddHHmmss"), "yyyy-MM-dd HH:mm:ss")));
					row.put("V01301", crop07.get(i).getStationNumberChina());
					row.put("V01300", stationNumberN);
					row.put("V05001", crop07.get(i).getLatitude());
					row.put("V06001", crop07.get(i).getLongitude());
					row.put("V07001", crop07.get(i).getHeightOfSationGroundAboveMeanSeaLevel());              
					row.put("V07031", crop07.get(i).getHeightOfPressureSensor());
					row.put("V_ACODE", adminCode); // V_ACODE		
					row.put("V04001", crop07.get(i).getYear());
					row.put("V71001", crop07.get(i).getCropName());
					row.put("V71601", crop07.get(i).getOutputLevelOfStation());
					row.put("V71091", crop07.get(i).getCountyAverageOutput());
					row.put("V71083", crop07.get(i).getCountyOutputChangeRate());
					row.put("V_BBB", v_bbb);
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_SOURCE_ID", ctsCodeMaps.get(6).getCts_code());
					
					StatDi di = new StatDi();					
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMaps.get(6).getSod_code());
					di.setDATA_TYPE_1(ctsCodeMaps.get(6).getCts_code());
					di.setTT("crop07");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(crop07.get(i).getStationNumberChina());
					di.setDATA_TIME(String.valueOf(TimeUtil.date2String(TimeUtil.String2Date(DateStr, "yyyyMMddHHmmss"), "yyyy-MM-dd HH:mm:ss")));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLONGTITUDE(String.valueOf(crop07.get(i).getLongitude()));
					di.setLATITUDE(String.valueOf(crop07.get(i).getLatitude()));
					
					try {
						if(isRevised) {
							OTSDbHelper.getInstance().update(ctsCodeMaps.get(6).getValue_table_name(),row);
						}else {
							OTSDbHelper.getInstance().insert(ctsCodeMaps.get(6).getValue_table_name(), row);
						}
						diQueues.offer(di);
					}  catch (Exception e) {
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
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (crop07.size() - successRowCount_07) + "\n");
				break;
			case "CROP-08":
				List<ZAgmeCrop08> crop08 = agmeCrops.get(0).zAgmeCrop08s;
				int successRowCount_08 = crop08.size();
				for (int i = 0; i < crop08.size(); i++) {
					Map<String, Object> row = new HashMap<String, Object>();
					int stationNumberN = defaultInt;
					String stat = crop08.get(i).getStationNumberChina();
					if (Pattern.matches("\\d{5}", stat))
						stationNumberN = Integer.parseInt(stat);					
					
					String info = (String) proMap.get(stat + "+12");
					String adminCode = "999999";
					if(info != null) {
						String[] infos = info.split(",");
						if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
							adminCode = infos[5];
					}
					
					if(adminCode.startsWith("999999")){
						info = (String) proMap.get(stat + "+01");
						if(info == null) {
							loggerBuffer.append("\n In the configuration file, the station number does not exist" + stat);
						}else {
							String[] infos = info.split(",");
							if(infos.length >= 6)
								adminCode = infos[5];
						}
					}
					row.put("D_RECORD_ID", sdf.format(crop08.get(i).getObservationDate())+"_"+crop08.get(i).getStationNumberChina()+"_"+crop08.get(i).getCropName()+"_"+crop08.get(i).getPeriodOfGrowth()+"_"+crop08.get(i).getOrganName());
					row.put("D_DATA_ID", ctsCodeMaps.get(7).getSod_code());
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					row.put("D_DATETIME",TimeUtil.date2String(crop08.get(i).getObservationDate(), "yyyy-MM-dd HH:mm:ss"));
					row.put("V01301", crop08.get(i).getStationNumberChina());
					row.put("V01300", stationNumberN);
					row.put("V05001", crop08.get(i).getLatitude());
					row.put("V06001", crop08.get(i).getLongitude());
					row.put("V07001", crop08.get(i).getHeightOfSationGroundAboveMeanSeaLevel());              
					row.put("V07031", crop08.get(i).getHeightOfPressureSensor());
					row.put("V_ACODE", adminCode); // V_ACODE
					row.put("V04001", crop08.get(i).getObservationDate().getYear() + 1900);
					row.put("V04002", crop08.get(i).getObservationDate().getMonth() + 1);
					row.put("V04003", crop08.get(i).getObservationDate().getDate());
					row.put("V04004", crop08.get(i).getObservationDate().getHours());
					row.put("V04005", crop08.get(i).getObservationDate().getMinutes());
					row.put("V04006", crop08.get(i).getObservationDate().getSeconds());
					row.put("V71001", crop08.get(i).getCropName());
					row.put("V71002", crop08.get(i).getPeriodOfGrowth());
					row.put("V71650", crop08.get(i).getOrganName());
					row.put("V71651", crop08.get(i).getPlantFreshWeight());
					row.put("V71652", crop08.get(i).getPlantDryWeight());
					row.put("V71653", crop08.get(i).getAvgFreshWeightPerSquareMeter());
					row.put("V71654", crop08.get(i).getAvgDryWeightPerSquareMeter());
					row.put("V71655", crop08.get(i).getPercentageOfWater());
					row.put("V71656", crop08.get(i).getPercentageOfGrowth());
					row.put("V_BBB", v_bbb);
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_SOURCE_ID", ctsCodeMaps.get(7).getCts_code());
					
					StatDi di = new StatDi();					
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMaps.get(7).getSod_code());
					di.setDATA_TYPE_1(ctsCodeMaps.get(7).getCts_code());
					di.setTT("crop08");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(crop08.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(crop08.get(i).getObservationDate(), "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLONGTITUDE(String.valueOf(crop08.get(i).getLongitude()));
					di.setLATITUDE(String.valueOf(crop08.get(i).getLatitude()));

					try {
						if(isRevised) {
							OTSDbHelper.getInstance().update(ctsCodeMaps.get(7).getValue_table_name(),row);
						}else {
							OTSDbHelper.getInstance().insert(ctsCodeMaps.get(7).getValue_table_name(), row);
						}
						diQueues.offer(di);
					}  catch (Exception e) {
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
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (crop08.size() - successRowCount_08) + "\n");
				break;
			case "CROP-09":
				List<ZAgmeCrop09> crop09 = agmeCrops.get(0).zAgmeCrop09s;
				int successRowCount_09 = crop09.size();
				for (int i = 0; i < crop09.size(); i++) {
					Map<String, Object> row = new HashMap<String, Object>();
					
					int stationNumberN = defaultInt;
					String stat = crop09.get(i).getStationNumberChina();
					if (Pattern.matches("\\d{5}", stat))
						stationNumberN = Integer.parseInt(stat);					
					
					String info = (String) proMap.get(stat + "+12");
					String adminCode = "999999";
					if(info != null) {
						String[] infos = info.split(",");
						if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
							adminCode = infos[5];
					}
					
					if(adminCode.startsWith("999999")){
						info = (String) proMap.get(stat + "+01");
						if(info == null) {
							loggerBuffer.append("\n In the configuration file, the station number does not exist" + stat);
						}else {
							String[] infos = info.split(",");
							if(infos.length >= 6)
								adminCode = infos[5];
						}
					}

					row.put("D_RECORD_ID", sdf.format(crop09.get(i).getSeedingTime())+"_"+crop09.get(i).getStationNumberChina()+"_"+crop09.get(i).getCropName()+"_"+crop09.get(i).getFieldLevel());
					row.put("D_DATA_ID", ctsCodeMaps.get(8).getSod_code());
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					row.put("D_DATETIME",TimeUtil.date2String(crop09.get(i).getSeedingTime(), "yyyy-MM-dd HH:mm:ss"));
					row.put("V01301", crop09.get(i).getStationNumberChina());
					row.put("V01300", stationNumberN);
					row.put("V05001", crop09.get(i).getLatitude());
					row.put("V06001", crop09.get(i).getLongitude());
					row.put("V07001", crop09.get(i).getHeightOfSationGroundAboveMeanSeaLevel());              
					row.put("V07031", crop09.get(i).getHeightOfPressureSensor());
					row.put("V_ACODE", adminCode); // V_ACODE		
					row.put("V04001", crop09.get(i).getSeedingTime().getYear() + 1900);
					row.put("V04002", crop09.get(i).getSeedingTime().getMonth() + 1);
					row.put("V04003", crop09.get(i).getSeedingTime().getDate());
					row.put("V71001", crop09.get(i).getCropName());
					row.put("V71903", crop09.get(i).getFieldLevel());
					row.put("V04300_017", sdf.format(crop09.get(i).getSeedingTime()));
					row.put("V04300_018", sdf.format(crop09.get(i).getHarvestTime()));
					row.put("V71091", crop09.get(i).getUnitOuput());
					row.put("V_BBB", v_bbb);
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_SOURCE_ID", ctsCodeMaps.get(8).getCts_code());
					
					StatDi di = new StatDi();					
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMaps.get(8).getSod_code());
					di.setDATA_TYPE_1(ctsCodeMaps.get(8).getCts_code());
					di.setTT("crop09");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(crop09.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(crop09.get(i).getSeedingTime(), "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLONGTITUDE(String.valueOf(crop09.get(i).getLongitude()));
					di.setLATITUDE(String.valueOf(crop09.get(i).getLatitude()));

					try {
						if(isRevised) {
							OTSDbHelper.getInstance().update(ctsCodeMaps.get(8).getValue_table_name(),row);
						}else {
							OTSDbHelper.getInstance().insert(ctsCodeMaps.get(8).getValue_table_name(), row);
						}
						diQueues.offer(di);
					}  catch (Exception e) {
						successRowCount_09 = successRowCount_09 -1;
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
			    loggerBuffer.append(" INSERT SUCCESS COUNT : " + successRowCount_09 + "\n");
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (crop09.size() - successRowCount_09) + "\n");

				break;
			case "CROP-10":
				List<ZAgmeCrop10> crop10 = agmeCrops.get(0).zAgmeCrop10s;
				int successRowCount_10 = crop10.size();
				for (int i = 0; i < crop10.size(); i++) {
					Map<String, Object> row = new HashMap<String, Object>();
					int stationNumberN = defaultInt;
					String stat = crop10.get(i).getStationNumberChina();
					if (Pattern.matches("\\d{5}", stat))
						stationNumberN = Integer.parseInt(stat);					
					
					String info = (String) proMap.get(stat + "+12");
					String adminCode = "999999";
					if(info != null) {
						String[] infos = info.split(",");
						if(infos.length >= 6 && !infos[5].equals("null") && !infos[5].isEmpty())
							adminCode = infos[5];
					}
					
					if(adminCode.startsWith("999999")){
						info = (String) proMap.get(stat + "+01");
						if(info == null) {
							loggerBuffer.append("\n In the configuration file, the station number does not exist" + stat);
						}else {
							String[] infos = info.split(",");
							if(infos.length >= 6)
								adminCode = infos[5];
						}
					}

					row.put("D_RECORD_ID", sdf.format(crop10.get(i).getObservationDate())+"_"+crop10.get(i).getStationNumberChina()+"_"+crop10.get(i).getFieldLevel()+"_"+crop10.get(i).getCropName()+"_"+crop10.get(i).getPeriodOfGrowth());
					row.put("D_DATA_ID", ctsCodeMaps.get(9).getSod_code());
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					row.put("D_DATETIME",TimeUtil.date2String(crop10.get(i).getObservationDate(), "yyyy-MM-dd HH:mm:ss"));
					row.put("V01301", crop10.get(i).getStationNumberChina());
					row.put("V01300", stationNumberN);
					row.put("V05001", crop10.get(i).getLatitude());
					row.put("V06001", crop10.get(i).getLongitude());
					row.put("V07001", crop10.get(i).getHeightOfSationGroundAboveMeanSeaLevel());              
					row.put("V07031", crop10.get(i).getHeightOfPressureSensor());
					row.put("V_ACODE", adminCode); // V_ACODE		
					row.put("V04001", crop10.get(i).getObservationDate().getYear() + 1900);
					row.put("V04002", crop10.get(i).getObservationDate().getMonth() + 1);
					row.put("V04003", crop10.get(i).getObservationDate().getDate());
					row.put("V04004", crop10.get(i).getObservationDate().getHours());
					row.put("V04005", crop10.get(i).getObservationDate().getMinutes());
					row.put("V04006", crop10.get(i).getObservationDate().getSeconds());	
					row.put("V71903", crop10.get(i).getFieldLevel());
					row.put("V71001", crop10.get(i).getCropName());
					row.put("V71002", crop10.get(i).getPeriodOfGrowth());
					row.put("V71006", crop10.get(i).getPlantHeight());
					row.put("V71008", crop10.get(i).getPlantDensity());
					row.put("V71007", crop10.get(i).getGrowthState());
					row.put("V71630_1", crop10.get(i).getOutputFactorName1());
					row.put("V71632_1", crop10.get(i).getOutputFactorMeasureValue1());				
					row.put("V71630_2", crop10.get(i).getOutputFactorName2());
					row.put("V71632_2", crop10.get(i).getOutputFactorMeasureValue2());
					row.put("V71630_3", crop10.get(i).getOutputFactorName3());
					row.put("V71632_3", crop10.get(i).getOutputFactorMeasureValue3());
					row.put("V71630_4", crop10.get(i).getOutputFactorName4());
					row.put("V71632_4", crop10.get(i).getOutputFactorMeasureValue4());
					row.put("V_BBB", v_bbb);
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_SOURCE_ID", ctsCodeMaps.get(9).getCts_code());
					
					StatDi di = new StatDi();					
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMaps.get(9).getSod_code());
					di.setDATA_TYPE_1(ctsCodeMaps.get(9).getCts_code());
					di.setTT("crop10");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(crop10.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(crop10.get(i).getObservationDate(), "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLONGTITUDE(String.valueOf(crop10.get(i).getLongitude()));
					di.setLATITUDE(String.valueOf(crop10.get(i).getLatitude()));

					try {
						if(isRevised) {
							OTSDbHelper.getInstance().update(ctsCodeMaps.get(9).getValue_table_name(),row);
						}else {
							OTSDbHelper.getInstance().insert(ctsCodeMaps.get(9).getValue_table_name(), row);
						}
						diQueues.offer(di);
					}  catch (Exception e) {
						successRowCount_10 = successRowCount_10 -1;
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
			    loggerBuffer.append(" INSERT SUCCESS COUNT : " + successRowCount_10 + "\n");
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (crop10.size() - successRowCount_10) + "\n");
				break;
			default:
				break;
			}
		}
		return DataBaseAction.SUCCESS;
	}

}
