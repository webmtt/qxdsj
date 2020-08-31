package cma.cimiss2.dpc.indb.cawn.dc_cawn_reg.service;

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

import cma.cimiss2.dpc.decoder.bean.cawn.REG;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
//	static Map<String, Object> proMap = StationInfo.getProMap();
	public static BlockingQueue<StatDi> diQueues;
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}

	public static DataBaseAction insert_ots(List<REG> regs, String tablename, Date recv_time, StringBuffer loggerBuffer, String fileN) {
		Map<String, Object> proMap = StationInfo.getProMap();
		if(regs != null && regs.size() > 0) {
			int successRowCount = regs.size();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String V_TT = "REG";
			for(int idx = 0; idx < regs.size(); idx ++){
				Map<String, Object> row = new HashMap<String, Object>();
				REG reg = regs.get(idx);
				Date dataTime = reg.getObservationTime();
				String station = reg.getStationNumberChina();
				int adminCode = 999999;
				
				//row.put("D_RECORD_ID", sdf.format(dataTime) + "_" + station);
	            row.put("D_DATA_ID", sod_code);
	            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_DATETIME", TimeUtil.date2String(dataTime,"yyyy-MM-dd HH:mm:ss"));
	            //"V01301,V01300,V05001,V06001,V07001,V_ACODE,V04001,V04002,V04003,V04004,"
	            row.put("V01301", station);
	            row.put("V01300", Integer.parseInt(StationCodeUtil.stringToAscii(station)));
	        	
				double lat = reg.getLatitude();
				double lon = reg.getLongtitude();
				double height = reg.getHeightAboveSeaLevel();
	            String info = (String) proMap.get(station + "+16");
				if(info == null) {
					loggerBuffer.append("\n In configuration file, this station does not exist: " + station);
				}
				else{
					String[] infos = info.split(",");						
					if(infos[5].equals("null"))
						loggerBuffer.append("\n In configuration file, V_CCCC is null!");
					else 
						adminCode = Integer.parseInt(infos[5]);
					if(String.valueOf(lat).startsWith("99999") && !infos[2].equals("null")){
						lat = Double.parseDouble(infos[2]);
						reg.setLatitude(lat);
					}
					if(String.valueOf(lon).startsWith("99999") && !infos[1].equals("null")){
						lon = Double.parseDouble(infos[1]);
						reg.setLongtitude(lon);
					}
					if(String.valueOf(height).startsWith("99999") && !infos[3].equals("null")){
						height = Double.parseDouble(infos[3]);
						reg.setHeightAboveSeaLevel(height);
					}
						
				}
	            
				row.put("V05001", reg.getLatitude());
	            row.put("V06001", reg.getLongtitude());
	            row.put("V07001", reg.getHeightAboveSeaLevel());
	            
				row.put("V_ACODE", adminCode);
				row.put("V04001", dataTime.getYear() + 1900);
				row.put("V04002", dataTime.getMonth() + 1);
				row.put("V04003", dataTime.getDate());
				row.put("V04004", dataTime.getHours());
				// "V04005,V04006,V15800,V15810,V15814,V15817,V15820,V15823,V15826,V05042_01,"
				row.put("V04005", dataTime.getMinutes());
				row.put("V04006", dataTime.getSeconds());
				row.put("V15800", reg.getRecordFrequency());
				row.put("V15810", reg.getSO2_Density());
				row.put("V15814", reg.getNO_Density());
				row.put("V15817", reg.getNO2_Density());
				row.put("V15820", reg.getNOX_Density());
				row.put("V15823", reg.getCO_Density());
				row.put("V15826", reg.getO3_Density());
				row.put("V05042_01", reg.getChannel_1());
				//"V05045_01,V05042_02,V05045_02,V05042_03,V05045_03,V05042_04,V05045_04,V05042_05,V05045_05,V05042_06,"
				row.put("V05045_01", reg.getMeasureCode_1());
				row.put("V05042_02", reg.getChannel_2());
				row.put("V05045_02", reg.getMeasureCode_2());
				row.put("V05042_03", reg.getChannel_3());
				row.put("V05045_03", reg.getMeasureCode_3());
				row.put("V05042_04", reg.getChannel_4());
				row.put("V05045_04", reg.getMeasureCode_4());
				row.put("V05042_05", reg.getChannel_5());
				row.put("V05045_05", reg.getMeasureCode_5());
				row.put("V05042_06", reg.getChannel_6());
				// "V05045_06,V05042_07,V05045_07,V15802_SO2,V15810_5_005,V15810_5_006,V15810_5_004,V15802_NO,V15814_5_005,V15814_5_006,"
				row.put("V05045_06", reg.getMeasureCode_6());
				row.put("V05042_07", reg.getChannel_7());
				row.put("V05045_07", reg.getMeasureCode_7());
				row.put("V15802_SO2", reg.getSO2_DataSign());
				row.put("V15810_5_005", reg.getSO2_5minMaxDensity());
				row.put("V15810_5_006", reg.getSO2_5minMinDensity());
				row.put("V15810_5_004", reg.getSO2_5minDensitySTDEV());
				row.put("V15802_NO", reg.getNO_DataSign());
				row.put("V15814_5_005", reg.getNO_5minMaxDensity());
				row.put("V15814_5_006", reg.getNO_5minMaxDensity());
				//"V15814_5_004,V15802_NO2,V15817_5_005,V15817_5_006,V15817_5_004,V15802_NOX,V15820_5_005,V15820_5_006,V15820_5_004,V15802_CO,"
				row.put("V15814_5_004", reg.getNO_5minDensitySTDEV());
				row.put("V15802_NO2", reg.getNO2_DataSign());
				row.put("V15817_5_005", reg.getNO2_5minMaxDensity());
				row.put("V15817_5_006", reg.getNO2_5minMinDensity());
				row.put("V15817_5_004", reg.getNO2_5minDensitySTDEV());
				row.put("V15802_NOX", reg.getNOX_DataSign());
				row.put("V15820_5_005", reg.getNOX_5minMaxDensity());
				row.put("V15820_5_006", reg.getNOX_5minMinDensity());
				row.put("V15820_5_004", reg.getNOX_5minDensitySTDEV());
				row.put("V15802_CO", reg.getCO_DataSign());
				// "V15823_5_005,V15823_5_006,V15823_5_004,V15802_O3,V15826_5_005,V15826_5_006,V15826_5_004,V12001,V15440_SO2,V15441_SO2,"
				row.put("V15823_5_005", reg.getCO_5minMaxDensity());
				row.put("V15823_5_006", reg.getCO_5minMinDensity());
				row.put("V15823_5_004", reg.getCO_5minDensitySTDEV());
				row.put("V15802_O3", reg.getO3_DataSign());
				row.put("V15826_5_005", reg.getO3_5minMaxDensity());
				row.put("V15826_5_006", reg.getO3_5minMinDensity());
				row.put("V15826_5_004", reg.getO3_5minDensitySTDEV());
				row.put("V12001", reg.getInnerTemperature());
				row.put("V15440_SO2", reg.getSO2_DeviceSN());
				row.put("V15441_SO2", reg.getSO2_DeviceStateCode());
				// "V15442_SO2,V15443_SO2,V15444_SO2,V15840_SO2,V15437_SO2,V15841_SO2,V15445_SO2,V15438_SO2,V15448_SO2,V15439_SO2,"
				row.put("V15442_SO2", reg.getSO2_DeviceInnerTemperature());
				row.put("V15443_SO2", reg.getSO2_DeviceChamberTemperature());
				row.put("V15444_SO2", reg.getSO2_DevicePressure());
				row.put("V15840_SO2", reg.getSO2_DeviceGasFlow());
				row.put("V15437_SO2", reg.getSO2_DevicePhotomultiplierVoltage());
				row.put("V15841_SO2", reg.getSO2_DeviceTubeVoltage());
				row.put("V15445_SO2", reg.getSO2_DeviceLightIntensity());
				row.put("V15438_SO2", reg.getSO2_DeviceExternalAlarm());
				row.put("V15448_SO2", reg.getSO2_DeviceZeroPoint());
				row.put("V15439_SO2", reg.getSO2_DeviceSlope());
				// "V15811_SO2,V15449_SO2,V15440_NOX,V15441_NOX,V15442_NOX,V15443_NOX,V15842_NOX,V15843_NOX,V15444_NOX,V15840_NOX,"
				row.put("V15811_SO2", reg.getSO2_DeviceCalibrationDensity());
				row.put("V15449_SO2", reg.getSO2_DeviceRevisedSign());
				row.put("V15440_NOX", reg.getNOX_DeviceSN());
				row.put("V15441_NOX", reg.getNOX_DeviceStateCode());
				row.put("V15442_NOX", reg.getNOX_DeviceInnerTemperature());
				row.put("V15443_NOX", reg.getNOX_DeviceChamberTemperature());
				row.put("V15842_NOX", reg.getNOX_DevicePhotomultiplierTemperature());
				row.put("V15843_NOX", reg.getNOX_DeviceConverterTemperature());
				row.put("V15444_NOX", reg.getNOX_DevicePressure());
				row.put("V15840_NOX", reg.getNOX_DeviceGasFlow());
				// "V15437_NOX,V15844_NOX,V15438_NOX,V15448_NO,V15439_NO,V15815_NO,V15449_NO,V15818_NO2,V15449_NO2,V15448_NOX,"
				row.put("V15437_NOX", reg.getNOX_DevicePhotomultiplierVoltage());
				row.put("V15844_NOX", reg.getNOX_DeviceO3GeneratorFlow());
				row.put("V15438_NOX", reg.getNOX_DeviceExternalAlarm());
				row.put("V15448_NO", reg.getNO_ZeroPoint());
				row.put("V15439_NO", reg.getNO_Slope());
				row.put("V15815_NO", reg.getNO_CalibrationDensity());
				row.put("V15449_NO", reg.getNO_RevisedSign());
				row.put("V15818_NO2", reg.getNO2_CalibrationDensity());
				row.put("V15449_NO2", reg.getNO2_RevisedSign());
				row.put("V15448_NOX", reg.getNOX_ZeroPoint());
				// "V15439_NOX,V15821_NOX,V15449_NOX,V15440_CO,V15441_CO,V15442_CO,V15443_CO,V15444_CO,V15840_CO,V15845_CO,"
				row.put("V15439_NOX", reg.getNOX_Slope());
				row.put("V15821_NOX", reg.getNOX_CalibrationDensity());
				row.put("V15449_NOX", reg.getNOX_RevisedSign());
				row.put("V15440_CO", reg.getCO_DeviceSN());
				row.put("V15441_CO", reg.getCO_DeviceStateCode());
				row.put("V15442_CO", reg.getCO_DeviceInnerTemperature());
				row.put("V15443_CO", reg.getCO_DeviceChamberTemperature());
				row.put("V15444_CO", reg.getCO_DevicePressure());
				row.put("V15840_CO", reg.getCO_DeviceGasFlow());
				row.put("V15845_CO", reg.getCO_DeviceBiasVoltage());
				// "V15846_CO,V15847_CO,V15848_CO,V15438_CO,V15448_CO,V15439_CO,V15824_CO,V15449_CO,V15440_O3,V15441_O3,"
				row.put("V15846_CO", reg.getCO_DeviceMotorSpeed());
				row.put("V15847_CO", reg.getCO_DeviceSRRatio());
				row.put("V15848_CO", reg.getCO_LightIntensity());
				row.put("V15438_CO", reg.getCO_DeviceExternalAlarm());
				row.put("V15448_CO", reg.getCO_DeviceZeroPoint());
				row.put("V15439_CO", reg.getCO_DeviceSlope());
				row.put("V15824_CO", reg.getCO_DeviceCalibrationDensity());
				row.put("V15449_CO", reg.getCO_DeviceRevisedSign());
				row.put("V15440_O3", reg.getO3_DeviceSN());
				row.put("V15441_O3", reg.getO3_DeviceStateCode());
				//"V15445_O3A,V15445_O3B,V15446_O3A,V15446_O3B,V16447_O3A,V15447_O3B,V15444_O3,V15849_O3,V15850_O3,V15438_O3,"
				row.put("V15445_O3A", reg.getO3_DeviceALightIntensity());
				row.put("V15445_O3B", reg.getO3_DeviceBLightIntensity());
				row.put("V15446_O3A", reg.getO3_DeviceANoise());
				row.put("V15446_O3B", reg.getO3_DeviceBNoise());
				row.put("V16447_O3A", reg.getO3_DeviceAFlowRate());
				row.put("V15447_O3B", reg.getO3_DeviceBFlowRate());
				row.put("V15444_O3", reg.getO3_DevicePressure());
				row.put("V15849_O3", reg.getO3_DeviceLightSeatTemperature());
				row.put("V15850_O3", reg.getO3_DeviceLightTemperature());
				row.put("V15438_O3", reg.getO3_DeviceExternalAlarm());
				// "V15448_O3,V15439_O3,V15827_O3,V15449_O3,V_BBB)
				row.put("V15448_O3", reg.getO3_DeviceZeroPoint());
				row.put("V15439_O3", reg.getO3_DeviceSlope());
				row.put("V15827_O3", reg.getO3_DeviceCalibrationDensity());
				row.put("V15449_O3", reg.getO3_DeviceRevisedSign());
				row.put("V_BBB", "000");
	            row.put("D_SOURCE_ID", cts_code);
	            
				StatDi di = new StatDi();	
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(sod_code);
				di.setDATA_TYPE_1(cts_code);
				di.setTT(V_TT);			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("0"); //0成功，1失败
				di.setPROCESS_STATE("0");  //0成功，1失败	
				di.setIIiii(station);
				di.setDATA_TIME(TimeUtil.date2String(dataTime, "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				di.setLATITUDE(String.valueOf(reg.getLatitude()));
				di.setLONGTITUDE(String.valueOf(reg.getLongtitude()));
				
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
	        loggerBuffer.append(" INSERT FAILED COUNT : " + (regs.size() - successRowCount) + "\n");
		}
		return DataBaseAction.SUCCESS;
	}
}
