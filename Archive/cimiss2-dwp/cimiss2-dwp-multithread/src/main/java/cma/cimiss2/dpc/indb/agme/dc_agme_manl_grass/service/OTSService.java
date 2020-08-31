package cma.cimiss2.dpc.indb.agme.dc_agme_manl_grass.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.config.CTSCodeMap;
import org.cimiss2.dwp.tools.ots.OTSDbHelper;
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import com.alicloud.openservices.tablestore.ClientException;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_01;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_02;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_03;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_04;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_05;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_06;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_07;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_08;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_09;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_10;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {
	
//	static Map<String, Object> proMap = StationInfo.getProMap();
	public static BlockingQueue<StatDi> diQueues;
		
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}
	public static DataBaseAction processSuccessReport(ParseResult<Agme_Grass> parseResult, Date recv_time, String fileN,
			boolean isRevised, String v_bbb, StringBuffer loggerBuffer, List<CTSCodeMap> ctsCodeMaps) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		List<Agme_Grass> agmeGrassS = parseResult.getData();
		Calendar calendar = Calendar.getInstance();
		for (String cropType : agmeGrassS.get(0).grassTypes) {
			switch (cropType) {
			case "GRASS-01":
				List<Agme_Grass_01> agme_Grass_01s = agmeGrassS.get(0).getAgmeGrass_01();
				int successRowCount_01 = agme_Grass_01s.size();
				// 用于获取时间
				for (int i = 0; i < agme_Grass_01s.size(); i++) {
					Map<String, Object> row = new HashMap<String, Object>();
					Agme_Grass_01 Agme_Grass_01 = agme_Grass_01s.get(i);
					Date obsTime = Agme_Grass_01.getObservationTime();
					// D_DATETIME, V01301, V71501, V71002
					String agme_grass01_chn_tab_pk = sdf.format(obsTime) + "_" + Agme_Grass_01.getStationNumberChina()
							+ "_" + Agme_Grass_01.getHerbageName() + "_" + Agme_Grass_01.getDevelopmentalPeriod();
					row.put("D_RECORD_ID", agme_grass01_chn_tab_pk);
					row.put("D_DATA_ID", ctsCodeMaps.get(0).getSod_code()); // 资料标识，由配置文件配置
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					calendar.setTime(obsTime);
					row.put("D_DATETIME", TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss")); // 资料时间
					row.put("V01301", Agme_Grass_01.getStationNumberChina()); // 区站号（字符）
					row.put("V01300", StationCodeUtil.stringToAscii(Agme_Grass_01.getStationNumberChina())); // 区站号（数字）
					row.put("V05001", Agme_Grass_01.getLatitude()); // 纬度
					row.put("V06001", Agme_Grass_01.getLongitude()); // 经度
					row.put("V07001",Agme_Grass_01.getHeightOfSationGroundAboveMeanSeaLevel()); // 测站海拔高度
					row.put("V07031",Agme_Grass_01.getHeightOfBarometerAboveMeanSeaLevel());// 气压传感器海拔高度
					row.put("V_ACODE", getAdminCode(Agme_Grass_01.getStationNumberChina(), loggerBuffer));// 中国行政区划代码
					row.put("V04001", calendar.get(Calendar.YEAR)); // 资料观测年
					row.put("V04002", calendar.get(Calendar.MONTH) + 1); // 资料观测月
					row.put("V04003", calendar.get(Calendar.DAY_OF_MONTH)); // 资料观测日
					row.put("V71501", Agme_Grass_01.getHerbageName());// 牧草名称
					row.put("V71002", Agme_Grass_01.getDevelopmentalPeriod());// 发育期
					row.put("V71010", Agme_Grass_01.getPerDevlopmentPeriod());// 发育期百分率
					row.put("V_BBB", v_bbb);
					row.put("D_SOURCE_ID", ctsCodeMaps.get(0).getCts_code());
					
					StatDi di = new StatDi();				
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMaps.get(0).getSod_code());
					di.setDATA_TYPE_1(ctsCodeMaps.get(0).getCts_code());
					di.setTT("GRASS01");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(agme_Grass_01s.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(agme_Grass_01s.get(i).getLatitude()));
					di.setLONGTITUDE(String.valueOf(agme_Grass_01s.get(i).getLongitude()));
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
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (agme_Grass_01s.size() - successRowCount_01) + "\n");
				
				break;
			case "GRASS-02":
				List<Agme_Grass_02> agme_Grass_02s = agmeGrassS.get(0).getAgmeGrass_02();
				int successRowCount_02 = agme_Grass_02s.size();
				for (int i = 0; i < agme_Grass_02s.size(); i++) {
					Map<String, Object> row = new HashMap<String, Object>();
					Agme_Grass_02 Agme_Grass_02 = agme_Grass_02s.get(i);
					Date obsTime = Agme_Grass_02.getObservationTime();
					// D_DATETIME, V01301, V71501
					String agme_grass02_chn_tab_pk = sdf.format(obsTime) + "_" + Agme_Grass_02.getStationNumberChina()
							+ "_" + Agme_Grass_02.getHerbageName();
					row.put("D_RECORD_ID", agme_grass02_chn_tab_pk); // 记录标识
					row.put("D_DATA_ID", ctsCodeMaps.get(1).getSod_code()); // 资料标识，由配置文件配置
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					calendar.setTime(obsTime);
					row.put("D_DATETIME", TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss")); // 资料时间
					row.put("V01301", Agme_Grass_02.getStationNumberChina()); // 区站号（字符）
					row.put("V01300", StationCodeUtil.stringToAscii(Agme_Grass_02.getStationNumberChina())); // 区站号（数字）
					row.put("V05001",Agme_Grass_02.getLatitude()); // 纬度
					row.put("V06001", Agme_Grass_02.getLongitude()); // 经度
					row.put("V07001",Agme_Grass_02.getHeightOfSationGroundAboveMeanSeaLevel()); // 测站海拔高度
					row.put("V07031",Agme_Grass_02.getHeightOfBarometerAboveMeanSeaLevel());// 气压传感器海拔高度
					row.put("V_ACODE", getAdminCode(Agme_Grass_02.getStationNumberChina(), loggerBuffer));// 中国行政区划代码
					row.put("V04001", calendar.get(Calendar.YEAR)); // 资料观测年
					row.put("V04002", calendar.get(Calendar.MONTH) + 1); // 资料观测月
					row.put("V04003", calendar.get(Calendar.DAY_OF_MONTH)); // 资料观测日
					row.put("V71501", Agme_Grass_02.getHerbageName());
					row.put("V71006",Agme_Grass_02.getHeightGrowth());
					row.put("V_BBB", v_bbb);
					row.put("D_SOURCE_ID", ctsCodeMaps.get(1).getCts_code());
					
					StatDi di = new StatDi();				
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMaps.get(1).getSod_code());
					di.setDATA_TYPE_1(ctsCodeMaps.get(1).getCts_code());
					
					di.setTT("GRASS02");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(agme_Grass_02s.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(agme_Grass_02s.get(i).getLatitude()));
					di.setLONGTITUDE(String.valueOf(agme_Grass_02s.get(i).getLongitude()));
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
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (agme_Grass_02s.size() - successRowCount_02) + "\n");
				break;
			case "GRASS-03":
				List<Agme_Grass_03> agme_Grass_03s = agmeGrassS.get(0).getAgmeGrass_03();
				int successRowCount_03 = agme_Grass_03s.size();
				for (int i = 0; i < agme_Grass_03s.size(); i++) {
					Map<String, Object> row = new HashMap<String, Object>();
					Agme_Grass_03 agme_Grass_03 = agme_Grass_03s.get(i);
					Date obsTime = agme_Grass_03.getObservationTime();
					// D_DATETIME, V01301, V71501
					String agme_grass03_chn_tab_pk = sdf.format(obsTime) + "_" + agme_Grass_03.getStationNumberChina()
							+ "_" + agme_Grass_03.getHerbageName()+ "_" +doubleParsInt(agme_Grass_03.getDryFreshRatio()) ;
					row.put("D_RECORD_ID", agme_grass03_chn_tab_pk); // 记录标识
					row.put("D_DATA_ID", ctsCodeMaps.get(2).getSod_code()); // 资料标识，由配置文件配置
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					calendar.setTime(obsTime);
					row.put("D_DATETIME", TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss")); // 资料时间
					row.put("V01301", agme_Grass_03.getStationNumberChina()); // 区站号（字符）
					row.put("V01300", StationCodeUtil.stringToAscii(agme_Grass_03.getStationNumberChina())); // 区站号（数字）
					row.put("V05001", agme_Grass_03.getLatitude()); // 纬度
					row.put("V06001", agme_Grass_03.getLongitude()); // 经度
					row.put("V07001",agme_Grass_03.getHeightOfSationGroundAboveMeanSeaLevel()); // 测站海拔高度
					row.put("V07031",agme_Grass_03.getHeightOfBarometerAboveMeanSeaLevel());// 气压传感器海拔高度 
					row.put("V_ACODE", getAdminCode(agme_Grass_03.getStationNumberChina(), loggerBuffer));// 中国行政区划代码
					row.put("V04001", calendar.get(Calendar.YEAR)); // 资料观测年
					row.put("V04002", calendar.get(Calendar.MONTH) + 1); // 资料观测月
					row.put("V04003", calendar.get(Calendar.DAY_OF_MONTH)); // 资料观测日
					row.put("V71501", agme_Grass_03.getHerbageName());// 牧草名称
					row.put("V71652", agme_Grass_03.getDryWeight());// 干重
					row.put("V71651", agme_Grass_03.getFreshWeight());// 鲜重
					row.put("V71906", agme_Grass_03.getDryFreshRatio());// 干鲜比
					row.put("V_BBB", v_bbb);
					row.put("D_SOURCE_ID", ctsCodeMaps.get(2).getCts_code());
						
					StatDi di = new StatDi();				
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMaps.get(2).getSod_code());
					di.setDATA_TYPE_1(ctsCodeMaps.get(2).getCts_code());
					di.setTT("GRASS03");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(agme_Grass_03s.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(agme_Grass_03s.get(i).getLatitude()));
					di.setLONGTITUDE(String.valueOf(agme_Grass_03s.get(i).getLongitude()));try {
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
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (agme_Grass_03s.size() - successRowCount_03) + "\n");
				break;
			case "GRASS-04":
				List<Agme_Grass_04> agme_Grass_04s = agmeGrassS.get(0).getAgmeGrass_04();
				int successRowCount_04 = agme_Grass_04s.size();
				for (int i = 0; i < agme_Grass_04s.size(); i++) {
					Map<String, Object> row = new HashMap<String, Object>();
					Agme_Grass_04 agme_Grass_04 = agme_Grass_04s.get(i);
					
					Date obsTime = agme_Grass_04.getObservationTime();
					// D_DATETIME, V01301, V71009
					int intCoverDegree = doubleParsInt(agme_Grass_04.getCoverDegree());
					String agme_grass04_chn_tab_pk = sdf.format(obsTime) + "_" + agme_Grass_04.getStationNumberChina()
							+ "_" + intCoverDegree;
					row.put("D_RECORD_ID", agme_grass04_chn_tab_pk);
					row.put("D_DATA_ID", ctsCodeMaps.get(3).getSod_code()); // 资料标识，由配置文件配置
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					calendar.setTime(obsTime);
					row.put("D_DATETIME", TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss")); // 资料时间
					row.put("V01301", agme_Grass_04.getStationNumberChina()); // 区站号（字符）
					row.put("V01300", StationCodeUtil.stringToAscii(agme_Grass_04.getStationNumberChina())); // 区站号（数字）
					row.put("V05001",agme_Grass_04.getLatitude()); // 纬度
					row.put("V06001", agme_Grass_04.getLongitude()); // 经度
					row.put("V07001",agme_Grass_04.getHeightOfSationGroundAboveMeanSeaLevel()); // 测站海拔高度
					row.put("V07031",agme_Grass_04.getHeightOfBarometerAboveMeanSeaLevel());// 气压传感器海拔高度 
					row.put("V_ACODE", getAdminCode(agme_Grass_04.getStationNumberChina(), loggerBuffer));// 中国行政区划代码
					row.put("V04001", calendar.get(Calendar.YEAR)); // 资料观测年
					row.put("V04002", calendar.get(Calendar.MONTH) + 1); // 资料观测月
					row.put("V04003", calendar.get(Calendar.DAY_OF_MONTH)); // 资料观测日

					row.put("V71009", agme_Grass_04.getCoverDegree());// 覆盖度
					row.put("V71907", agme_Grass_04.getEvaluaGrassCondition());// 草层状况评价
					row.put("V71908", agme_Grass_04.getFeedingDegree());// 采食度
					row.put("V71909", agme_Grass_04.getFeedingRate());// 采食率
					row.put("V_BBB", v_bbb);
					row.put("D_SOURCE_ID", ctsCodeMaps.get(3).getCts_code());
					
					StatDi di = new StatDi();				
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMaps.get(3).getSod_code());
					di.setDATA_TYPE_1(ctsCodeMaps.get(3).getCts_code());
					di.setTT("GRASS04");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(agme_Grass_04s.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(agme_Grass_04s.get(i).getLatitude()));
					di.setLONGTITUDE(String.valueOf(agme_Grass_04s.get(i).getLongitude()));
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
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (agme_Grass_04s.size() - successRowCount_04) + "\n");
				break;
			case "GRASS-05":
				List<Agme_Grass_05> agme_Grass_05s = agmeGrassS.get(0).getAgmeGrass_05();
				int successRowCount_05 = agme_Grass_05s.size();
				for (int i = 0; i < agme_Grass_05s.size(); i++) {
					Map<String, Object> row = new HashMap<String, Object>();
					Agme_Grass_05 agme_Grass_05 = agme_Grass_05s.get(i);
					
					Date obsTime = agme_Grass_05.getObservationTime();
					// D_DATETIME, V01301, V71501
					String agme_grass05_chn_tab_pk = sdf.format(obsTime) + "_" + agme_Grass_05.getStationNumberChina()
							+ "_" + agme_Grass_05.getHerbageName();
					row.put("D_RECORD_ID", agme_grass05_chn_tab_pk); // 记录标识
					row.put("D_DATA_ID", ctsCodeMaps.get(4).getSod_code()); // 资料标识，由配置文件配置
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					calendar.setTime(obsTime);
					row.put("D_DATETIME", TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss")); // 资料时间
					row.put("V01301", agme_Grass_05.getStationNumberChina()); // 区站号（字符）
					row.put("V01300", StationCodeUtil.stringToAscii(agme_Grass_05.getStationNumberChina())); // 区站号（数字）
					row.put("V05001", agme_Grass_05.getLatitude()); // 纬度
					row.put("V06001", agme_Grass_05.getLongitude()); // 经度
					row.put("V07001",agme_Grass_05.getHeightOfSationGroundAboveMeanSeaLevel()); // 测站海拔高度
					row.put("V07031",agme_Grass_05.getHeightOfBarometerAboveMeanSeaLevel());// 气压传感器海拔高度 

					row.put("V_ACODE", getAdminCode(agme_Grass_05.getStationNumberChina(), loggerBuffer));// 中国行政区划代码
					row.put("V04001", calendar.get(Calendar.YEAR)); // 资料观测年
					row.put("V04002",calendar.get(Calendar.MONTH) + 1); // 资料观测月
					row.put("V04003",calendar.get(Calendar.DAY_OF_MONTH)); // 资料观测日

					row.put("V71501", agme_Grass_05.getHerbageName());// 牧草名称
					row.put("V71910", agme_Grass_05.getNumPlantPerHectare());// 每公顷株丛数
					row.put("V71911", agme_Grass_05.getTotalNumPlantPerHectare());// 每公顷总株丛数
					row.put("V_BBB", v_bbb);
					row.put("D_SOURCE_ID", ctsCodeMaps.get(4).getCts_code());
					
					StatDi di = new StatDi();				
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMaps.get(4).getSod_code());
					di.setDATA_TYPE_1(ctsCodeMaps.get(4).getCts_code());
					di.setTT("GRASS05");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(agme_Grass_05s.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(agme_Grass_05s.get(i).getLatitude()));
					di.setLONGTITUDE(String.valueOf(agme_Grass_05s.get(i).getLongitude()));try {
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
			    loggerBuffer.append(" INSERT SUCCESS COUNT : " + successRowCount_05+ "\n");
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (agme_Grass_05s.size() - successRowCount_05) + "\n");
				break;
			case "GRASS-06":
				List<Agme_Grass_06> agme_Grass_06s = agmeGrassS.get(0).getAgmeGrass_06();
				int successRowCount_06 = agme_Grass_06s.size();
				for (int i = 0; i < agme_Grass_06s.size(); i++) {
					Map<String, Object> row = new HashMap<String, Object>();
					Agme_Grass_06 agme_Grass_06 = agme_Grass_06s.get(i);
					
					Date obsTime = agme_Grass_06.getObservationTime();
					// D_DATETIME, V01301, V71501
					String agme_grass06_chn_tab_pk = sdf.format(obsTime) + "_" + agme_Grass_06.getStationNumberChina()
							+ "_" + agme_Grass_06.getRankCondition();
					row.put("D_RECORD_ID", agme_grass06_chn_tab_pk); // 记录标识
					row.put("D_DATA_ID", ctsCodeMaps.get(5).getSod_code()); // 资料标识，由配置文件配置
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					calendar.setTime(obsTime);
					row.put("D_DATETIME", TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss")); // 资料时间
					row.put("V01301", agme_Grass_06.getStationNumberChina()); // 区站号（字符）
					row.put("V01300", StationCodeUtil.stringToAscii(agme_Grass_06.getStationNumberChina())); // 区站号（数字）
					row.put("V05001", agme_Grass_06.getLatitude()); // 纬度
					row.put("V06001", agme_Grass_06.getLongitude()); // 经度
					row.put("V07001",agme_Grass_06.getHeightOfSationGroundAboveMeanSeaLevel()); // 测站海拔高度
					row.put("V07031",agme_Grass_06.getHeightOfBarometerAboveMeanSeaLevel());// 气压传感器海拔高度 
					row.put("V_ACODE", getAdminCode(agme_Grass_06.getStationNumberChina(), loggerBuffer));// 中国行政区划代码
					row.put("V04001", calendar.get(Calendar.YEAR)); // 资料观测年
					row.put("V04002",calendar.get(Calendar.MONTH) + 1); // 资料观测月
					row.put("V04003", calendar.get(Calendar.DAY_OF_MONTH)); // 资料观测日

					row.put("V71920", agme_Grass_06.getRankCondition());// 膘情等级
					row.put("V71921", agme_Grass_06.getAdultAnimalNum());// 成畜头数
					row.put("V71922", agme_Grass_06.getBabyHeadNum());// 幼畜头数
					row.put("V_BBB", v_bbb);
					row.put("D_SOURCE_ID", ctsCodeMaps.get(5).getCts_code());
					
					StatDi di = new StatDi();				
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMaps.get(5).getSod_code());
					di.setDATA_TYPE_1(ctsCodeMaps.get(5).getCts_code());
					di.setTT("GRASS06");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(agme_Grass_06s.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(agme_Grass_06s.get(i).getLatitude()));
					di.setLONGTITUDE(String.valueOf(agme_Grass_06s.get(i).getLongitude()));try {
						if(isRevised) {
							OTSDbHelper.getInstance().update(ctsCodeMaps.get(5).getValue_table_name(),row);
						}else {
							OTSDbHelper.getInstance().insert(ctsCodeMaps.get(5).getValue_table_name(), row);
						}
						diQueues.offer(di);
					}  catch (Exception e) {
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
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (agme_Grass_06s.size() - successRowCount_06) + "\n");
				break;
			case "GRASS-07":
				List<Agme_Grass_07> agme_Grass_07s = agmeGrassS.get(0).getAgmeGrass_07();
				int successRowCount_07 = agme_Grass_07s.size();
				for (int i = 0; i < agme_Grass_07s.size(); i++) {
					Map<String, Object> row = new HashMap<String, Object>();
					Agme_Grass_07 agme_Grass_07 = agme_Grass_07s.get(i);
					
					Date obsTime = agme_Grass_07.getObservationTime();
					int intRamWeight_1 = doubleParsInt(agme_Grass_07.getRamWeight_1());
					int intRamWeight_2 = doubleParsInt(agme_Grass_07.getRamWeight_2());
					int intRamWeight_3 = doubleParsInt(agme_Grass_07.getRamWeight_3());
					int intRamWeight_4 = doubleParsInt(agme_Grass_07.getRamWeight_4());
					int intRamWeight_5 = doubleParsInt(agme_Grass_07.getRamWeight_5());
					String agme_grass07_chn_tab_pk = sdf.format(obsTime) + "_" + agme_Grass_07.getStationNumberChina()
							+ "_" + intRamWeight_1 + "_" + intRamWeight_2 + "_" + intRamWeight_3 + "_" + intRamWeight_4
							+ "_" + intRamWeight_5;
					row.put("D_RECORD_ID", agme_grass07_chn_tab_pk); // 记录标识
					row.put("D_DATA_ID", ctsCodeMaps.get(6).getSod_code()); // 资料标识，由配置文件配置
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					calendar.setTime(obsTime);
					row.put("D_DATETIME", TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss")); // 资料时间
					row.put("V01301", agme_Grass_07.getStationNumberChina()); // 区站号（字符）
					row.put("V01300", StationCodeUtil.stringToAscii(agme_Grass_07.getStationNumberChina())); // 区站号（数字）
					row.put("V05001", agme_Grass_07.getLatitude()); // 纬度
					row.put("V06001", agme_Grass_07.getLongitude()); // 经度
					row.put("V07001",agme_Grass_07.getHeightOfSationGroundAboveMeanSeaLevel()); // 测站海拔高度
					row.put("V07031",agme_Grass_07.getHeightOfBarometerAboveMeanSeaLevel());// 气压传感器海拔高度 
					row.put("V_ACODE", getAdminCode(agme_Grass_07.getStationNumberChina(), loggerBuffer));// 中国行政区划代码
					row.put("V04001", calendar.get(Calendar.YEAR)); // 资料观测年
					row.put("V04002", calendar.get(Calendar.MONTH) + 1); // 资料观测月
					row.put("V04003", calendar.get(Calendar.DAY_OF_MONTH)); // 资料观测日

					row.put("WEIGHT_01",agme_Grass_07.getRamWeight_1());// 羯羊_1体重
					row.put("WEIGHT_02", agme_Grass_07.getRamWeight_2());// 羯羊_2体重
					row.put("WEIGHT_03",agme_Grass_07.getRamWeight_3());// 羯羊_3体重
					row.put("WEIGHT_04",agme_Grass_07.getRamWeight_4());// 羯羊_4体重
					row.put("WEIGHT_05", agme_Grass_07.getRamWeight_5());// 羯羊_5体重
					row.put("V_WEIGHT_AVG", agme_Grass_07.getAvgWeight());// 平均
					row.put("V_BBB", v_bbb);
					row.put("D_SOURCE_ID", ctsCodeMaps.get(6).getCts_code());
					
					StatDi di = new StatDi();				
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMaps.get(6).getSod_code());
					di.setDATA_TYPE_1(ctsCodeMaps.get(6).getCts_code());
					di.setTT("GRASS07");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(agme_Grass_07s.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(agme_Grass_07s.get(i).getLatitude()));
					di.setLONGTITUDE(String.valueOf(agme_Grass_07s.get(i).getLongitude()));
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
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (agme_Grass_07s.size() - successRowCount_07) + "\n");
				break;
			case "GRASS-08":
				List<Agme_Grass_08> agme_Grass_08s = agmeGrassS.get(0).getAgmeGrass_08();
				int successRowCount_08 = agme_Grass_08s.size();
				for (int i = 0; i < agme_Grass_08s.size(); i++) {
					Map<String, Object> row = new HashMap<String, Object>();
					
					Agme_Grass_08 agme_Grass_08 = agme_Grass_08s.get(i);
					
					Date obsTime = agme_Grass_08.getObservationTime();
					// D_DATETIME, V01301, V71501, V71601
					String agme_grass08_chn_tab_pk = sdf.format(obsTime) + "_" + agme_Grass_08.getStationNumberChina()
							+ "_" + agme_Grass_08.getLivestockName() + "_" + agme_Grass_08.getLivestockBreeds();
					row.put("D_RECORD_ID", agme_grass08_chn_tab_pk); // 记录标识
					row.put("D_DATA_ID", ctsCodeMaps.get(7).getSod_code()); // 资料标识，由配置文件配置
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					calendar.setTime(obsTime);
					row.put("D_DATETIME", TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss")); // 资料时间

					row.put("V01301", agme_Grass_08.getStationNumberChina()); // 区站号（字符）
					row.put("V01300", StationCodeUtil.stringToAscii(agme_Grass_08.getStationNumberChina())); // 区站号（数字）
					row.put("V05001", agme_Grass_08.getLatitude()); // 纬度
					row.put("V06001", agme_Grass_08.getLongitude()); // 经度
					row.put("V07001",agme_Grass_08.getHeightOfSationGroundAboveMeanSeaLevel()); // 测站海拔高度
					row.put("V07031",agme_Grass_08.getHeightOfBarometerAboveMeanSeaLevel());// 气压传感器海拔高度 
					row.put("V_ACODE", getAdminCode(agme_Grass_08.getStationNumberChina(), loggerBuffer));// 中国行政区划代码
					row.put("V04001", calendar.get(Calendar.YEAR)); // 资料观测年
					row.put("V04002", calendar.get(Calendar.MONTH) + 1); // 资料观测月
					row.put("V04003", calendar.get(Calendar.DAY_OF_MONTH)); // 资料观测日
					row.put("V04004", calendar.get(Calendar.HOUR)); // 资料观测时

					row.put("V_AVG_GRAZE_01", agme_Grass_08.getAvgSpringDailyGrazHours());// 春季日平均放牧时数
					row.put("V_AVG_GRAZE_02", agme_Grass_08.getAvgSummerDailyGrazHours());// 夏季日平均放牧时数
					row.put("V_AVG_GRAZE_03", agme_Grass_08.getAvgFallDailyGrazHours());// 秋季日平均放牧时数
					row.put("V_AVG_GRAZE_04", agme_Grass_08.getAvgWinterDailyGrazHours());// 冬季日平均放牧时数
					row.put("V_HOVEL", agme_Grass_08.getIsHaveSuccah());// 有无棚舍
					row.put("V_HOVEL_COUNT", agme_Grass_08.getSuccahNum());// 棚舍数量
					row.put("V_HOVEL_LEN", agme_Grass_08.getSuccahLong());// 棚舍长
					row.put("V_HOVEL_W", agme_Grass_08.getSuccahWide());// 棚舍宽
					row.put("V_HOVEL_H", agme_Grass_08.getSuccahHigh());// 棚舍高
					row.put("V_HOVEL_ST", agme_Grass_08.getSuccahFrame());// 棚舍结构
					row.put("V_HOVEL_TYPE", agme_Grass_08.getSuccahType());// 棚舍型式
					row.put("V_HOVEL_WD", agme_Grass_08.getSuccahWinDirection());// 棚舍门窗开向
					row.put("V71501", agme_Grass_08.getLivestockName());// 畜群家畜名称
					row.put("V71601", agme_Grass_08.getLivestockBreeds());// 家畜品种
					row.put("V_ORGAN", agme_Grass_08.getLivestockUnit());// 畜群所属单位
					row.put("V_BBB", v_bbb);
					row.put("D_SOURCE_ID", ctsCodeMaps.get(7).getCts_code());
					
					StatDi di = new StatDi();				
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMaps.get(7).getSod_code());
					di.setDATA_TYPE_1(ctsCodeMaps.get(7).getCts_code());
					di.setTT("GRASS08");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(agme_Grass_08s.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(agme_Grass_08s.get(i).getLatitude()));
					di.setLONGTITUDE(String.valueOf(agme_Grass_08s.get(i).getLongitude()));
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
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (agme_Grass_08s.size() - successRowCount_08) + "\n");
				break;
			case "GRASS-09":
				List<Agme_Grass_09> agme_Grass_09s = agmeGrassS.get(0).getAgmeGrass_09();
				int successRowCount_09 = agme_Grass_09s.size();
				for (int i = 0; i < agme_Grass_09s.size(); i++) {
					Map<String, Object> row = new HashMap<String, Object>();
					
					Agme_Grass_09 agme_Grass_09 = agme_Grass_09s.get(i);
					
					String agme_grass09_chn_tab_pk = agme_Grass_09.getObservationStarTime() + "_"
							+ agme_Grass_09.getStationNumberChina() + "_" + agme_Grass_09.getAnimalHusbandryName();
					row.put("D_RECORD_ID", agme_grass09_chn_tab_pk); // 记录标识
					row.put("D_DATA_ID",ctsCodeMaps.get(8).getSod_code()); // 资料标识，由配置文件配置
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					Date obsTime = null;
					try {
						obsTime = sdf.parse(agme_Grass_09.getObservationStarTime());
					} catch (ParseException e1) {
						loggerBuffer.append("Time conversion error");
						e1.printStackTrace();
					}
					calendar.setTime(obsTime);
					row.put("D_DATETIME", TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss")); // 资料时间

					row.put("V01301", agme_Grass_09.getStationNumberChina()); // 区站号（字符）
					row.put("V01300", StationCodeUtil.stringToAscii(agme_Grass_09.getStationNumberChina())); // 区站号（数字）
					row.put("V05001",agme_Grass_09.getLatitude()); // 纬度
					row.put("V06001",agme_Grass_09.getLongitude()); // 经度
					row.put("V07001",agme_Grass_09.getHeightOfSationGroundAboveMeanSeaLevel()); // 测站海拔高度
					row.put("V07031",agme_Grass_09.getHeightOfBarometerAboveMeanSeaLevel());// 气压传感器海拔高度 

					row.put("V_ACODE", getAdminCode(agme_Grass_09.getStationNumberChina(), loggerBuffer));// 中国行政区划代码
					row.put("V04001",calendar.get(Calendar.YEAR)); // 资料观测年
					row.put("V04002",calendar.get(Calendar.MONTH) + 1); // 资料观测月
					row.put("V04003",calendar.get(Calendar.DAY_OF_MONTH)); // 资料观测日
					row.put("V04004",calendar.get(Calendar.HOUR)); // 资料观测时

					row.put("V04300_017",agme_Grass_09.getObservationStarTime());// 调查起始时间
					row.put("V04300_018",agme_Grass_09.getObservationEndTime());// 调查终止时间
					row.put("V71616_02",agme_Grass_09.getAnimalHusbandryName());// 牧事活动名称
					row.put("V_PROD_FUNC", agme_Grass_09.getProductPerformance());// 生产性能
					row.put("V_BBB", v_bbb);
					row.put("D_SOURCE_ID", ctsCodeMaps.get(8).getCts_code());
					
					StatDi di = new StatDi();				
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMaps.get(8).getSod_code());
					di.setDATA_TYPE_1(ctsCodeMaps.get(8).getCts_code());
					di.setTT("GRASS09");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(agme_Grass_09s.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(agme_Grass_09s.get(i).getLatitude()));
					di.setLONGTITUDE(String.valueOf(agme_Grass_09s.get(i).getLongitude()));
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
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (agme_Grass_09s.size() - successRowCount_09) + "\n");
				break;
			case "GRASS-10":
				List<Agme_Grass_10> agme_Grass_10s = agmeGrassS.get(0).getAgmeGrass_10();
				int successRowCount_10 = agme_Grass_10s.size();
				for (int i = 0; i < agme_Grass_10s.size(); i++) {
					Map<String, Object> row = new HashMap<String, Object>();
					
					Agme_Grass_10 agme_Grass_10 = agme_Grass_10s.get(i);
					Date obsTime = agme_Grass_10.getObservationTime();
					// D_DATETIME, V01301, V71912
					String agme_grass10_chn_tab_pk = sdf.format(obsTime) + "_" + agme_Grass_10.getStationNumberChina()
							+ "_" + agme_Grass_10.getGrassLayerType() + "_" + agme_Grass_10.getMeasurementSite();
					row.put("D_RECORD_ID", agme_grass10_chn_tab_pk); // 记录标识
					row.put("D_DATA_ID", ctsCodeMaps.get(9).getSod_code()); // 资料标识，由配置文件配置
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					calendar.setTime(obsTime);
					row.put("D_DATETIME", TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss")); // 资料时间
					row.put("V01301", agme_Grass_10.getStationNumberChina()); // 区站号（字符）
					row.put("V01300", StationCodeUtil.stringToAscii(agme_Grass_10.getStationNumberChina())); // 区站号（数字）
					row.put("V05001", agme_Grass_10.getLatitude()); // 纬度
					row.put("V06001", agme_Grass_10.getLongitude()); // 经度
					row.put("V07001",agme_Grass_10.getHeightOfSationGroundAboveMeanSeaLevel()); // 测站海拔高度
					row.put("V07031",agme_Grass_10.getHeightOfBarometerAboveMeanSeaLevel());// 气压传感器海拔高度 

					row.put("V_ACODE", getAdminCode(agme_Grass_10.getStationNumberChina(), loggerBuffer));// 中国行政区划代码
					row.put("V04001", calendar.get(Calendar.YEAR)); // 资料观测年
					row.put("V04002", calendar.get(Calendar.MONTH) + 1); // 资料观测月
					row.put("V04003", calendar.get(Calendar.DAY_OF_MONTH)); // 资料观测日
					row.put("V04004", calendar.get(Calendar.HOUR)); // 资料观测时

					row.put("V71912", agme_Grass_10.getGrassLayerType());// 草层类型
					row.put("V71116", agme_Grass_10.getMeasurementSite());// 测量场地
					row.put("V71913", agme_Grass_10.getGrassLayerHeight());// 草层高度
					row.put("V_BBB", v_bbb);
					row.put("D_SOURCE_ID", ctsCodeMaps.get(9).getCts_code());
					
					StatDi di = new StatDi();				
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMaps.get(9).getSod_code());
					di.setDATA_TYPE_1(ctsCodeMaps.get(9).getCts_code());
					di.setTT("GRASS10");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(agme_Grass_10s.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(obsTime, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(agme_Grass_10s.get(i).getLatitude()));
					di.setLONGTITUDE(String.valueOf(agme_Grass_10s.get(i).getLongitude()));
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
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (agme_Grass_10s.size() - successRowCount_10) + "\n");
				break;
			default:
				break;
			}
		}
		
		return DataBaseAction.SUCCESS;
	}
	public static String getAdminCode(String stat, StringBuffer loggerBuffer) {
		Map<String, Object> proMap = StationInfo.getProMap();
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
//		String adminCode = "999999";
//		String info = (String) proMap.get(stationNumberChina + "+12");
//		if (info == null) {
//			info = (String) proMap.get(stationNumberChina + "+01");
//		}else{
//			String[] infos = info.split(",");
//			adminCode= infos[5]; // 行政区划代码
//		}
		return adminCode;
	}

	public static int doubleParsInt(Double arge) {
		double douArge = arge * 1000;
		int intArge = (int) douArge;
		return intArge;
	}
}
