package cma.cimiss2.dpc.indb.cawn.dc_cawn_aap.service;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alicloud.openservices.tablestore.ClientException;

import cma.cimiss2.dpc.decoder.bean.cawn.cawnAAP;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {
	public static BlockingQueue<StatDi> diQueues;
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	static String cts_code = StartConfig.ctsCode();
	static String sod_code = StartConfig.sodCode();
//	public static Map<String, Object> proMap = StationInfo.getProMap();
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}

	public static DataBaseAction insert_ots(List<cawnAAP> cawnAAPs, String tablename, Date recv_time, StringBuffer loggerBuffer, String filepath) {
		Map<String, Object> proMap = StationInfo.getProMap();
		if(cawnAAPs != null && cawnAAPs.size() > 0) {
			int successRowCount = cawnAAPs.size();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");	
			String info = null;
			for(int i = 0; i <cawnAAPs.size(); i ++){
				cawnAAP cawnAAP = cawnAAPs.get(i);
				StatDi di = new StatDi();	
				di.setFILE_NAME_O(filepath);
				di.setDATA_TYPE(sod_code);
				di.setDATA_TYPE_1(cts_code);
				di.setTT("黑碳");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(filepath);
				di.setBUSINESS_STATE("0"); //0成功，1失败
				di.setPROCESS_STATE("0");  //0成功，1失败	
				
				String station = cawnAAP.getV01301();
				Date date = cawnAAP.getObservationTime();
				info = (String) proMap.get(station + "+16");
				double latitude = 999999;
				double longtitude = 999999;
				double height = 999999;
				double adminCode = 999999;
				if(info == null) {
					infoLogger.error("\n In configuration file, this station does not exist: " + station);
				}
				else{
					String[] infos = info.split(",");						
					if(infos[1].equals("null"))
						infoLogger.error("\n In configuration file, longtitude is null!");
					else
						longtitude = Double.parseDouble(infos[1]);
					if(infos[2].equals("null"))
						infoLogger.error("\n In configuration file, latitude is null!");
					else
						latitude = Double.parseDouble(infos[2]);
					if(infos[3].equals("null"))
						infoLogger.error("\n In configuration file, station height is null!");
					else
						height = Double.parseDouble(infos[3]);
					if(infos[5].equals("null"))
						infoLogger.error("\n In configuration file, V_CCCC is null!");
					else 
						adminCode = Integer.parseInt(infos[5]);
				}
				Map<String, Object> row = new HashMap<String, Object>();
				//row.put("D_RECORD_ID", sdf.format(date)+"_"+station+"_"+cawnAAP.getItemCode());
	            row.put("D_DATA_ID", sod_code);
	            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_DATETIME", TimeUtil.date2String(date,"yyyy-MM-dd HH:mm:ss"));
	          //+ "V01301,V01300,V_BBB,V04001,V04002,V04003,V04004,V04005,V05001,V06001,"
				row.put("V01301", station);
				row.put("V01300", Integer.parseInt(StationCodeUtil.stringToAscii(station)));
				row.put("V_BBB", "000");
				row.put("V04001", date.getYear() + 1900);
				row.put("V04002", date.getMonth() + 1);
				row.put("V04003", date.getDate());
				row.put("V04004", date.getHours());
				row.put("V04005", date.getMinutes());
				row.put("V05001", latitude);
				row.put("V06001", longtitude);
				//+ "V07001,V_ACODE,V_ITEM_CODE,V70012,V_370nmBC,V_470nmBC,V_520nmBC,V_590nmBC,V_660nmBC,V_880nmBC,"
				row.put("V07001", height);
				row.put("V_ACODE", adminCode);
				row.put("V_ITEM_CODE", cawnAAP.getItemCode());
				row.put("V70012", cawnAAP.getFlow());
				row.put("V_370nmBC", cawnAAP.getConcentration370());
				row.put("V_470nmBC", cawnAAP.getConcentration470());
				row.put("V_520nmBC", cawnAAP.getConcentration520());
				row.put("V_590nmBC", cawnAAP.getConcentration590());
				row.put("V_660nmBC", cawnAAP.getConcentration660());
				row.put("V_880nmBC", cawnAAP.getConcentration880());
				//+ "V_950nmBC,V_SZ_370nm,V_SB_370nm,V_RZ_370nm,V_RB_370nm,V_Fri_370nm,V_Attn_370nm,V_SZ_470nm,V_SB_470nm,V_RZ_470nm,"
				row.put("V_950nmBC", cawnAAP.getConcentration950());
				row.put("V_SZ_370nm", cawnAAP.getSampleZoneZeroPointSignal370());
				row.put("V_SB_370nm", cawnAAP.getSampleZoneMeasureSignal370());
				row.put("V_RZ_370nm", cawnAAP.getReferZoneZeroPointSignal370());
				row.put("V_RB_370nm", cawnAAP.getReferZoneMeasureSignal370());
				row.put("V_Fri_370nm", cawnAAP.getSplitRatio370());
				row.put("V_Attn_370nm", cawnAAP.getLightLossRate370());
				row.put("V_SZ_470nm", cawnAAP.getSampleZoneZeroPointSignal470());
				row.put("V_SB_470nm", cawnAAP.getSampleZoneMeasureSignal470());
				row.put("V_RZ_470nm", cawnAAP.getReferZoneZeroPointSignal470());
				//+ "V_RB_470nm,V_Fri_470nm,V_Attn_470nm,V_SZ_520nm,V_SB_520nm,V_RZ_520nm,V_RB_520nm,V_Fri_520nm,V_Attn_520nm,V_SZ_590nm,"
				row.put("V_RB_470nm", cawnAAP.getReferZoneMeasureSignal470());
				row.put("V_Fri_470nm", cawnAAP.getSplitRatio470());
				row.put("V_Attn_470nm", cawnAAP.getLightLossRate470());
				row.put("V_SZ_520nm", cawnAAP.getSampleZoneZeroPointSignal520());
				row.put("V_SB_520nm", cawnAAP.getSampleZoneMeasureSignal520());
				row.put("V_RZ_520nm", cawnAAP.getReferZoneZeroPointSignal520());
				row.put("V_RB_520nm", cawnAAP.getReferZoneMeasureSignal520());
				row.put("V_Fri_520nm", cawnAAP.getSplitRatio520());
				row.put("V_Attn_520nm", cawnAAP.getLightLossRate520());
				row.put("V_SZ_590nm", cawnAAP.getSampleZoneZeroPointSignal590());
				//+ "V_SB_590nm,V_RZ_590nm,V_RB_590nm,V_Fri_590nm,V_Attn_590nm,V_SZ_660nm,V_SB_660nm,V_RZ_660nm,V_RB_660nm,V_Fri_660nm,"
				row.put("V_SB_590nm", cawnAAP.getSampleZoneMeasureSignal590());
				row.put("V_RZ_590nm", cawnAAP.getReferZoneZeroPointSignal590());
				row.put("V_RB_590nm", cawnAAP.getReferZoneMeasureSignal590());
				row.put("V_Fri_590nm", cawnAAP.getSplitRatio590());
				row.put("V_Attn_590nm", cawnAAP.getLightLossRate590());
				row.put("V_SZ_660nm", cawnAAP.getSampleZoneZeroPointSignal660());
				row.put("V_SB_660nm", cawnAAP.getSampleZoneMeasureSignal660());
				row.put("V_RZ_660nm", cawnAAP.getReferZoneZeroPointSignal660());
				row.put("V_RB_660nm", cawnAAP.getReferZoneMeasureSignal660());
				row.put("V_Fri_660nm", cawnAAP.getSplitRatio660());
				//+ "V_Attn_660nm,V_SZ_880nm,V_SB_880nm,V_RZ_880nm,V_RB_880nm,V_Fri_880nm,V_Attn_880nm,V_SZ_950nm,V_SB_950nm,V_RZ_950nm,"
				row.put("V_Attn_660nm", cawnAAP.getLightLossRate660());
				row.put("V_SZ_880nm", cawnAAP.getSampleZoneZeroPointSignal880());
				row.put("V_SB_880nm", cawnAAP.getSampleZoneMeasureSignal880());
				row.put("V_RZ_880nm", cawnAAP.getReferZoneZeroPointSignal880());
				row.put("V_RB_880nm", cawnAAP.getReferZoneMeasureSignal880());
				row.put("V_Fri_880nm", cawnAAP.getSplitRatio880());
				row.put("V_Attn_880nm", cawnAAP.getLightLossRate880());
				row.put("V_SZ_950nm", cawnAAP.getSampleZoneZeroPointSignal950());
				row.put("V_SB_950nm", cawnAAP.getSampleZoneMeasureSignal950());
				row.put("V_RZ_950nm", cawnAAP.getReferZoneZeroPointSignal950());
				//+ "V_RB_950nm,V_Fri_950nm,V_Attn_950nm)";
				row.put("V_RB_950nm", cawnAAP.getReferZoneMeasureSignal950());
				row.put("V_Fri_950nm", cawnAAP.getSplitRatio950());
				row.put("V_Attn_950nm", cawnAAP.getLightLossRate950());
				//+ "V_TIME_BASE,V_RC_370nm,V_S1C_370nm,V_S2C_370nm,V_RC_470nm,V_S1C_470nm,V_S2C_470nm,"
				row.put("V_TIME_BASE", cawnAAP.getTimebase());
				row.put("V_RC_370nm", cawnAAP.getReferenceSignal370());
				row.put("V_S1C_370nm",  cawnAAP.getFirstSampleSignal370());
				row.put("V_S2C_370nm",  cawnAAP.getSecondSampleSignal370());
				row.put("V_RC_470nm",  cawnAAP.getReferenceSignal470());
				row.put("V_S1C_470nm",  cawnAAP.getFirstSampleSignal470());
				row.put("V_S2C_470nm",  cawnAAP.getSecondSampleSignal470());
				//+"V_RC_520nm,V_S1C_520nm,V_S2C_520nm,V_RC_590nm,V_S1C_590nm,V_S2C_590nm,V_RC_660nm,V_S1C_660nm,V_S2C_660nm,"
				row.put("V_RC_520nm", cawnAAP.getReferenceSignal520());
				row.put("V_S1C_520nm",cawnAAP.getFirstSampleSignal520());
				row.put("V_S2C_520nm", cawnAAP.getSecondSampleSignal520());
				row.put("V_RC_590nm", cawnAAP.getReferenceSignal590());
				row.put("V_S1C_590nm", cawnAAP.getFirstSampleSignal590());
				row.put("V_S2C_590nm", cawnAAP.getSecondSampleSignal590());
				row.put("V_RC_660nm", cawnAAP.getReferenceSignal660());
				row.put("V_S1C_660nm", cawnAAP.getFirstSampleSignal660());
				row.put("V_S2C_660nm", cawnAAP.getSecondSampleSignal660());
				//+"V_RC_880nm,V_S1C_880nm,V_S2C_880nm,V_RC_950nm,V_S1C_950nm,V_S2C_950nm,Flow1,Flow2,V_PRESSURE,V_TEMP,V_BBCR,"
				row.put("V_RC_880nm", cawnAAP.getReferenceSignal880());
				row.put("V_S1C_880nm",cawnAAP.getFirstSampleSignal880());
				row.put("V_S2C_880nm", cawnAAP.getSecondSampleSignal880());
				row.put("V_RC_950nm", cawnAAP.getReferenceSignal950());
				row.put("V_S1C_950nm", cawnAAP.getFirstSampleSignal950());
				row.put("V_S2C_950nm", cawnAAP.getSecondSampleSignal950());
				row.put("Flow1", cawnAAP.getFlow1());
				row.put("Flow2", cawnAAP.getFlow2());
				
				row.put("V_PRESSURE", cawnAAP.getPressure());
				row.put("V_TEMP",cawnAAP.getTemperature());
				row.put("V_BBCR",cawnAAP.getBB());
				//+"V_CONT_TEMP,V_SUPPLY_TEMP,V_STATUS,V_CONT_STATUS,V_DETECT_STATUS,V_LED_STATUS,V_VALUE_STATUS,V_LED_TEMP,"
				row.put("V_CONT_TEMP", cawnAAP.getContTemp());
				row.put("V_SUPPLY_TEMP", cawnAAP.getSupplyTemp());
				row.put("V_STATUS", cawnAAP.getStatus());
				row.put("V_CONT_STATUS", cawnAAP.getContStatus());
				row.put("V_DETECT_STATUS", cawnAAP.getDetectStatus());
				row.put("V_LED_STATUS", cawnAAP.getLedStatus());
				row.put("V_VALUE_STATUS", cawnAAP.getValveStatus());
				row.put("V_LED_TEMP", cawnAAP.getLedTemp());
				//+"V_SBC1_370nm,V_SBC2_370nm,V_SBC1_470nm,V_SBC2_470nm,V_SBC1_520nm,V_SBC2_520nm,V_SBC1_590nm,V_SBC2_590nm,"
				row.put("V_SBC1_370nm", cawnAAP.getFirstSanmpleCon370());
				row.put("V_SBC2_370nm", cawnAAP.getSecondSanmpleCon370());
				row.put("V_SBC1_470nm", cawnAAP.getFirstSanmpleCon470());
				row.put("V_SBC2_470nm", cawnAAP.getSecondSanmpleCon470());
				row.put("V_SBC1_520nm", cawnAAP.getFirstSanmpleCon520());
				row.put("V_SBC2_520nm", cawnAAP.getSecondSanmpleCon520());
				row.put("V_SBC1_590nm", cawnAAP.getFirstSanmpleCon590());
				row.put("V_SBC2_590nm", cawnAAP.getSecondSanmpleCon590());
				//+"V_SBC1_660nm,V_SBC2_660nm,V_SBC1_880nm,V_SBC2_880nm,V_SBC1_950nm,V_SBC2_950nm,"
				row.put("V_SBC1_660nm", cawnAAP.getFirstSanmpleCon660());
				row.put("V_SBC2_660nm", cawnAAP.getSecondSanmpleCon660());
				row.put("V_SBC1_880nm", cawnAAP.getFirstSanmpleCon880());
				row.put("V_SBC2_880nm", cawnAAP.getSecondSanmpleCon880());
				row.put("V_SBC1_950nm", cawnAAP.getFirstSanmpleCon950());
				row.put("V_SBC2_950nm", cawnAAP.getSecondSanmpleCon950());
				//+ "V_K_370nm,V_K_470nm,V_K_520nm,V_K_590nm,V_K_660nm,V_K_880nm,V_K_950nm,V_TAPE_ADV_COUNT,D_SOURCE_ID)";
				row.put("V_K_370nm", cawnAAP.getCompCoeffi370());
				row.put("V_K_470nm", cawnAAP.getCompCoeffi470());
				row.put("V_K_520nm", cawnAAP.getCompCoeffi520());
				row.put("V_K_590nm", cawnAAP.getCompCoeffi590());
				row.put("V_K_660nm", cawnAAP.getCompCoeffi660());
				row.put("V_K_880nm", cawnAAP.getCompCoeffi880());
				row.put("V_K_950nm", cawnAAP.getCompCoeffi950());
				row.put("V_TAPE_ADV_COUNT", cawnAAP.getTapeAdvCount());
				row.put("D_SOURCE_ID", "G.0002.0001.R001");
				
				di.setIIiii(station);
				di.setLONGTITUDE(String.valueOf(longtitude));
				di.setLATITUDE(String.valueOf(latitude));
				di.setDATA_TIME(TimeUtil.date2String(date, "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
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
		     loggerBuffer.append(" INSERT FAILED COUNT : " + (cawnAAPs.size() - successRowCount) + "\n");
		}
		return DataBaseAction.SUCCESS;
	}
}
