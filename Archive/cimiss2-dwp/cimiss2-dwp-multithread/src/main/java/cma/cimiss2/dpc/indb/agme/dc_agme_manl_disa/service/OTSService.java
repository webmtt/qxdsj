package cma.cimiss2.dpc.indb.agme.dc_agme_manl_disa.service;

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

import com.alicloud.openservices.tablestore.ClientException;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeDisa;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeDisa01;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeDisa02;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeDisa03;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeDisa04;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeDisa05;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;


// TODO: Auto-generated Javadoc
/**
 * The Class OTSService.
 */
public class OTSService {
	
	/** The di queues. */
	public static BlockingQueue<StatDi> diQueues;
	
	/**
	 * Gets the di queues.
	 *
	 * @return the di queues
	 */
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	
	/**
	 * Sets the di queues.
	 *
	 * @param diQueues the new di queues
	 */
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}
	
	/** The pro map. */
//	static Map<String, Object> proMap = StationInfo.getProMap();
	
	/** The default int. */
	public static int defaultInt = 999999;
	
	/**
	 * Process success report.
	 *
	 * @param parseResult the parse result
	 * @param recv_time the recv time
	 * @param fileN the file N
	 * @param isRevised the is revised
	 * @param v_bbb the v bbb
	 * @param loggerBuffer the logger buffer
	 * @param ctsCodeMaps the cts code maps
	 * @return the data base action
	 */
	@SuppressWarnings("deprecation")
	public static DataBaseAction processSuccessReport(ParseResult<ZAgmeDisa> parseResult, Date recv_time, String fileN,
			boolean isRevised, String v_bbb, StringBuffer loggerBuffer, List<CTSCodeMap> ctsCodeMaps) {
		Map<String, Object> proMap = StationInfo.getProMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		List<ZAgmeDisa> agmeDisas = parseResult.getData();
		for (String disaType : agmeDisas.get(0).disaTypes) {
			switch(disaType) {
			case "DISA-01":
				List<ZAgmeDisa01> agmeDisa01s = agmeDisas.get(0).zAgmeDisa01s;
				int successRowCount_01 = agmeDisa01s.size();
				for (int i = 0; i < agmeDisa01s.size(); i++) {
					Map<String, Object> row = new HashMap<String, Object>();
					
					int stationNumberN = defaultInt;
					String stat = agmeDisa01s.get(i).getStationNumberChina();
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
							
					row.put("D_RECORD_ID", sdf.format(agmeDisa01s.get(i).getObservationDate())+"_"+agmeDisa01s.get(i).getStationNumberChina()+"_"+agmeDisa01s.get(i).getDisaName()+"_"+agmeDisa01s.get(i).getDisaCrop()+"_"+agmeDisa01s.get(i).getDegreeOfOrganDamage());
					row.put("D_DATA_ID", ctsCodeMaps.get(0).getSod_code());
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					row.put("D_DATETIME",TimeUtil.date2String(agmeDisa01s.get(i).getObservationDate(), "yyyy-MM-dd HH:mm:ss"));
					row.put("V01301", agmeDisa01s.get(i).getStationNumberChina());
					row.put("V01300", stationNumberN);
					row.put("V05001", agmeDisa01s.get(i).getLatitude());
					row.put("V06001", agmeDisa01s.get(i).getLongitude());
					row.put("V07001", agmeDisa01s.get(i).getHeightOfSationGroundAboveMeanSeaLevel());              
					row.put("V07031", agmeDisa01s.get(i).getHeightOfPressureSensor());
					row.put("V_ACODE", adminCode); // V_ACODE		
					row.put("V04001", agmeDisa01s.get(i).getObservationDate().getYear() + 1900);
					row.put("V04002", agmeDisa01s.get(i).getObservationDate().getMonth() + 1);
					row.put("V04003", agmeDisa01s.get(i).getObservationDate().getDate());
					row.put("V71040", agmeDisa01s.get(i).getDisaName());
					row.put("V71001", agmeDisa01s.get(i).getDisaCrop());
					row.put("V71042", agmeDisa01s.get(i).getDegreeOfOrganDamage());
					row.put("V71082", agmeDisa01s.get(i).getExpectedImpactOnOutput());
					row.put("V71923", agmeDisa01s.get(i).getReductionInOutput());
					row.put("V71614", agmeDisa01s.get(i).getSymptomOfDisaster());
					row.put("V_BBB", v_bbb);
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_SOURCE_ID", ctsCodeMaps.get(0).getCts_code());
					
					StatDi di = new StatDi();				
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMaps.get(0).getSod_code());
					di.setDATA_TYPE_1(ctsCodeMaps.get(0).getCts_code());
					di.setTT("DISA01");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(agmeDisa01s.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(agmeDisa01s.get(i).getObservationDate(), "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(agmeDisa01s.get(i).getLatitude()));
					di.setLONGTITUDE(String.valueOf(agmeDisa01s.get(i).getLongitude()));
					
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
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (agmeDisa01s.size() - successRowCount_01) + "\n");
				break;
				
			case "DISA-02":
				List<ZAgmeDisa02> agmeDisa02s = agmeDisas.get(0).zAgmeDisa02s;
				int successRowCount_02 = agmeDisa02s.size();
				for (int i = 0; i < agmeDisa02s.size(); i++) {
					Map<String, Object> row = new HashMap<String, Object>();
					
					int stationNumberN = defaultInt;
					String stat = agmeDisa02s.get(i).getStationNumberChina();
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
					
					row.put("D_RECORD_ID", sdf.format(agmeDisa02s.get(i).getObservationDate())+"_"+agmeDisa02s.get(i).getStationNumberChina()+"_"+agmeDisa02s.get(i).getDisaName()+"_"+agmeDisa02s.get(i).getDisaCrop()+"_"+agmeDisa02s.get(i).getDegreeOfOrganDamage()+"_"+agmeDisa02s.get(i).getDisaArea()+"_"+agmeDisa02s.get(i).getDisaPercentage()+"_"+agmeDisa02s.get(i).getReductionPercentage());
					row.put("D_DATA_ID", ctsCodeMaps.get(1).getSod_code());
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					row.put("D_DATETIME",TimeUtil.date2String(agmeDisa02s.get(i).getObservationDate(), "yyyy-MM-dd HH:mm:ss"));
					row.put("V01301", agmeDisa02s.get(i).getStationNumberChina());
					row.put("V01300", stationNumberN);
					row.put("V05001", agmeDisa02s.get(i).getLatitude());
					row.put("V06001", agmeDisa02s.get(i).getLongitude());
					row.put("V07001", agmeDisa02s.get(i).getHeightOfSationGroundAboveMeanSeaLevel());              
					row.put("V07031", agmeDisa02s.get(i).getHeightOfPressureSensor());
					row.put("V_ACODE", adminCode); // V_ACODE		
					row.put("V04001", agmeDisa02s.get(i).getObservationDate().getYear() + 1900);
					row.put("V04002", agmeDisa02s.get(i).getObservationDate().getMonth() + 1);
					row.put("V04003", agmeDisa02s.get(i).getObservationDate().getDate());
					row.put("V71040", agmeDisa02s.get(i).getDisaName());
					row.put("V71001", agmeDisa02s.get(i).getDisaCrop());
					row.put("V71042", agmeDisa02s.get(i).getDegreeOfOrganDamage());
					row.put("V71043", agmeDisa02s.get(i).getDisaArea());
					row.put("V71044", agmeDisa02s.get(i).getDisaPercentage());
					row.put("V71083", agmeDisa02s.get(i).getReductionPercentage());
					row.put("V71614", agmeDisa02s.get(i).getSymptomOfDisaster());
					row.put("V_BBB", v_bbb);
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_SOURCE_ID", ctsCodeMaps.get(1).getCts_code());
					
					StatDi di = new StatDi();				
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMaps.get(1).getSod_code());
					di.setDATA_TYPE_1(ctsCodeMaps.get(1).getCts_code());
					di.setTT("DISA02");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(agmeDisa02s.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(agmeDisa02s.get(i).getObservationDate(), "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(agmeDisa02s.get(i).getLatitude()));
					di.setLONGTITUDE(String.valueOf(agmeDisa02s.get(i).getLongitude()));
					
					try {
						if(isRevised) {
							OTSDbHelper.getInstance().update(ctsCodeMaps.get(1).getValue_table_name(),row);
						}else {
							OTSDbHelper.getInstance().insert(ctsCodeMaps.get(1).getValue_table_name(), row);
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
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (agmeDisa02s.size() - successRowCount_02) + "\n");
				break;
				
			case "DISA-03":
				List<ZAgmeDisa03> agmeDisa03s = agmeDisas.get(0).zAgmeDisa03s;
				int successRowCount_03 = agmeDisa03s.size();
				for (int i = 0; i < agmeDisa03s.size(); i++) {
					Map<String, Object> row = new HashMap<String, Object>();
					
					int stationNumberN = defaultInt;
					String stat = agmeDisa03s.get(i).getStationNumberChina();
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
					
					row.put("D_RECORD_ID", sdf.format(agmeDisa03s.get(i).getObservationDate())+"_"+agmeDisa03s.get(i).getStationNumberChina()+"_"+agmeDisa03s.get(i).getDisaName());
					row.put("D_DATA_ID", ctsCodeMaps.get(2).getSod_code());
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					row.put("D_DATETIME",TimeUtil.date2String(agmeDisa03s.get(i).getObservationDate(), "yyyy-MM-dd HH:mm:ss"));
					row.put("V01301", agmeDisa03s.get(i).getStationNumberChina());
					row.put("V01300", stationNumberN);
					row.put("V05001", agmeDisa03s.get(i).getLatitude());
					row.put("V06001", agmeDisa03s.get(i).getLongitude());
					row.put("V07001", agmeDisa03s.get(i).getHeightOfSationGroundAboveMeanSeaLevel());              
					row.put("V07031", agmeDisa03s.get(i).getHeightOfPressureSensor());
					row.put("V_ACODE", adminCode); // V_ACODE		
					row.put("V04001", agmeDisa03s.get(i).getObservationDate().getYear() + 1900);
					row.put("V04002", agmeDisa03s.get(i).getObservationDate().getMonth() + 1);
					row.put("V04003", agmeDisa03s.get(i).getObservationDate().getDate());
					row.put("V04300_017", sdf.format(agmeDisa03s.get(i).getStartTime()));
					row.put("V04300_018", sdf.format(agmeDisa03s.get(i).getEndTime()));			
					row.put("V71040", agmeDisa03s.get(i).getDisaName());
					row.put("V71048", agmeDisa03s.get(i).getDisaLevel());
					row.put("V71614", agmeDisa03s.get(i).getSymptomOfDisaster());
					row.put("V_BBB", v_bbb);
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_SOURCE_ID", ctsCodeMaps.get(2).getCts_code());
					
					StatDi di = new StatDi();				
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMaps.get(2).getSod_code());
					di.setDATA_TYPE_1(ctsCodeMaps.get(2).getCts_code());
					di.setTT("DISA03");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(agmeDisa03s.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(agmeDisa03s.get(i).getObservationDate(), "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(agmeDisa03s.get(i).getLatitude()));
					di.setLONGTITUDE(String.valueOf(agmeDisa03s.get(i).getLongitude()));
					
					try {
						if(isRevised) {
							OTSDbHelper.getInstance().update(ctsCodeMaps.get(2).getValue_table_name(),row);
						}else {
							OTSDbHelper.getInstance().insert(ctsCodeMaps.get(2).getValue_table_name(), row);
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
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (agmeDisa03s.size() - successRowCount_03) + "\n");
				break;
				
			case "DISA-04":
				
				List<ZAgmeDisa04> agmeDisa04s = agmeDisas.get(0).zAgmeDisa04s;
				int successRowCount_04 = agmeDisa04s.size();
				for (int i = 0; i < agmeDisa04s.size(); i++) {
					Map<String, Object> row = new HashMap<String, Object>();
					
					int stationNumberN = defaultInt;
					String stat = agmeDisa04s.get(i).getStationNumberChina();
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
						
							
					row.put("D_RECORD_ID", sdf.format(agmeDisa04s.get(i).getObservationDate())+"_"+agmeDisa04s.get(i).getStationNumberChina()+"_"+agmeDisa04s.get(i).getDisaName());
					row.put("D_DATA_ID", ctsCodeMaps.get(3).getSod_code());
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					row.put("D_DATETIME",TimeUtil.date2String(agmeDisa04s.get(i).getObservationDate(), "yyyy-MM-dd HH:mm:ss"));
					row.put("V01301", agmeDisa04s.get(i).getStationNumberChina());
					row.put("V01300", stationNumberN);
					row.put("V05001", agmeDisa04s.get(i).getLatitude());
					row.put("V06001", agmeDisa04s.get(i).getLongitude());
					row.put("V07001", agmeDisa04s.get(i).getHeightOfSationGroundAboveMeanSeaLevel());              
					row.put("V07031", agmeDisa04s.get(i).getHeightOfPressureSensor());
					row.put("V_ACODE", adminCode); // V_ACODE		
					row.put("V04001", agmeDisa04s.get(i).getObservationDate().getYear() + 1900);
					row.put("V04002", agmeDisa04s.get(i).getObservationDate().getMonth() + 1);
					row.put("V04003", agmeDisa04s.get(i).getObservationDate().getDate());
					row.put("V04300_017", sdf.format(agmeDisa04s.get(i).getStartTime()));
					row.put("V04300_018", sdf.format(agmeDisa04s.get(i).getEndTime()));			
					row.put("V71040", agmeDisa04s.get(i).getDisaName());
					row.put("V71048", agmeDisa04s.get(i).getDisaLevel());
					row.put("V71614", agmeDisa04s.get(i).getSymptomOfDisaster());
					row.put("V_BBB", v_bbb);
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_SOURCE_ID", ctsCodeMaps.get(3).getCts_code());
					
					StatDi di = new StatDi();				
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMaps.get(3).getSod_code());
					di.setDATA_TYPE_1(ctsCodeMaps.get(3).getCts_code());
					di.setTT("DISA04");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(agmeDisa04s.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(agmeDisa04s.get(i).getObservationDate(), "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(agmeDisa04s.get(i).getLatitude()));
					di.setLONGTITUDE(String.valueOf(agmeDisa04s.get(i).getLongitude()));
					
					try {
						if(isRevised) {
							OTSDbHelper.getInstance().update(ctsCodeMaps.get(3).getValue_table_name(),row);
						}else {
							OTSDbHelper.getInstance().insert(ctsCodeMaps.get(3).getValue_table_name(), row);
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
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (agmeDisa04s.size() - successRowCount_04) + "\n");
				break;
				
			case "DISA-05":
				
				List<ZAgmeDisa05> agmeDisa05s = agmeDisas.get(0).zAgmeDisa05s;
				int successRowCount_05 = agmeDisa05s.size();
				for (int i = 0; i < agmeDisa05s.size(); i++) {
					Map<String, Object> row = new HashMap<String, Object>();
					
					int stationNumberN = defaultInt;
					String stat = agmeDisa05s.get(i).getStationNumberChina();
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
						
					
					row.put("D_RECORD_ID", sdf.format(agmeDisa05s.get(i).getObservationDate())+"_"+agmeDisa05s.get(i).getStationNumberChina()+"_"+agmeDisa05s.get(i).getDamagePlant()+"_"+agmeDisa05s.get(i).getDisaName());
					row.put("D_DATA_ID", ctsCodeMaps.get(4).getSod_code());
					row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					row.put("D_DATETIME",TimeUtil.date2String(agmeDisa05s.get(i).getObservationDate(), "yyyy-MM-dd HH:mm:ss"));
					row.put("V01301", agmeDisa05s.get(i).getStationNumberChina());
					row.put("V01300", stationNumberN);
					row.put("V05001", agmeDisa05s.get(i).getLatitude());
					row.put("V06001", agmeDisa05s.get(i).getLongitude());
					row.put("V07001", agmeDisa05s.get(i).getHeightOfSationGroundAboveMeanSeaLevel());              
					row.put("V07031", agmeDisa05s.get(i).getHeightOfPressureSensor());
					row.put("V_ACODE", adminCode); // V_ACODE		
					row.put("V04001", agmeDisa05s.get(i).getObservationDate().getYear() + 1900);
					row.put("V04002", agmeDisa05s.get(i).getObservationDate().getMonth() + 1);
					row.put("V04003", agmeDisa05s.get(i).getObservationDate().getDate());
					row.put("V04004", agmeDisa05s.get(i).getObservationDate().getHours());
					row.put("V04300_017", sdf.format(agmeDisa05s.get(i).getStartTime()));
					row.put("V04300_018", sdf.format(agmeDisa05s.get(i).getEndTime()));			
					row.put("V71501", agmeDisa05s.get(i).getDamagePlant());
					row.put("V71040", agmeDisa05s.get(i).getDisaName());
					row.put("V71042", agmeDisa05s.get(i).getDegreeOfDamage());
					row.put("V71614", agmeDisa05s.get(i).getSymptomOfDisaster());
					row.put("V_BBB", v_bbb);
					row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					row.put("D_SOURCE_ID", ctsCodeMaps.get(4).getCts_code());
					
					StatDi di = new StatDi();				
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(ctsCodeMaps.get(4).getSod_code());
					di.setDATA_TYPE_1(ctsCodeMaps.get(4).getCts_code());
					di.setTT("DISA05");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					di.setIIiii(agmeDisa05s.get(i).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(agmeDisa05s.get(i).getObservationDate(), "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(agmeDisa05s.get(i).getLatitude()));
					di.setLONGTITUDE(String.valueOf(agmeDisa05s.get(i).getLongitude()));
					
					try {
						if(isRevised) {
							OTSDbHelper.getInstance().update(ctsCodeMaps.get(4).getValue_table_name(),row);
						}else {
							OTSDbHelper.getInstance().insert(ctsCodeMaps.get(4).getValue_table_name(), row);
						}
						diQueues.offer(di);
					}   catch (Exception e) {
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
			    loggerBuffer.append(" INSERT FAILED COUNT : " + (agmeDisa05s.size() - successRowCount_05) + "\n");
				break;
			default:
				break;
			}
		}
		return DataBaseAction.SUCCESS;
	}
	

}
