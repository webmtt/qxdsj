package cma.cimiss2.dpc.indb.surf.aws.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.ots.OTSDbHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alicloud.openservices.tablestore.ClientException;

import cma.cimiss2.dpc.decoder.bean.surf.AWS;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.core.tools.TimeUtil;

public class OTSService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	
	private static String sod_code = StartConfig.sodCode();
	private static String cts_code = StartConfig.ctsCode();
	public static String V_TT = "地面设备";    // 报文资料类别
	public static BlockingQueue<StatDi> diQueues;
	
	public static void setDiQueues(BlockingQueue<StatDi> diQueues){
		OTSService.diQueues = diQueues;
	}
	public static BlockingQueue<StatDi> getDiQueues(){
		return diQueues;
	}
	
	/**
	 * @Title: insert_ots   
	 * @Description: TODO(地面设备状态信息资料入库方法)   
	 * @param list
	 * @param tablename
	 * @param recv_time
	 * @param loggerBuffer
	 * @param fileN
	 * @return DataBaseAction      
	 */
	public static DataBaseAction insert_ots(List<AWS> list, String tablename, Date recv_time, StringBuffer loggerBuffer, String fileN){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		int successRowCount = 0;
		if(list != null)
			successRowCount = list.size();
		if(list != null && list.size() > 0) {
	        for (int i = 0; i < list.size(); i++) {
	        	AWS awsModel = list.get(i);
	            Map<String, Object> row = new HashMap<String, Object>();
	            Date date = awsModel.getObservationTime();
	            String primkey = sdf.format(date) + "_" + awsModel.getStationNumberChina();
	            
	            row.put("D_RECORD_ID", primkey);
	            row.put("D_DATA_ID", sod_code);
	            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_DATETIME", TimeUtil.date2String(date,"yyyy-MM-dd HH:mm:ss"));
				// "V01301,V_BBB,V04001,V04002,V04003,V04004,V04005,V04006,V05001,V06001,"
				row.put("V01301", awsModel.getStationNumberChina());
				row.put("V_BBB", "000");
				row.put("V04001", date.getYear() + 1900);
				row.put("V04002", date.getMonth() + 1);
				row.put("V04003", date.getDate());
				row.put("V04004", date.getHours());
				row.put("V04005", date.getMinutes());
				row.put("V04006", date.getSeconds());
				row.put("V05001", awsModel.getLatitude());
				row.put("V06001", awsModel.getLongitude());
				//+ "V02460,V02461,V02462,V02463,V02464,V02465,V02466,V02467,V02468,V02470,"
				row.put("V02460", awsModel.getCommunication());
				row.put("V02461", awsModel.getPressure());
				row.put("V02462", awsModel.getTemperature());
				row.put("V02463", awsModel.getWetbulbTemperature());
				row.put("V02464", awsModel.getHumidity());
				row.put("V02465", awsModel.getWindDirection());
				row.put("V02466", awsModel.getWindSpeed());
				row.put("V02467", awsModel.getRainfall());
				row.put("V02468", awsModel.getRain());
				row.put("V02470", awsModel.getGrassTemperature());
				//+ "V02471,V02472_005,V02472_010,V02472_015,V02472_020,V02472_040,V02472_080,V02472_160,V02472_320,V02473,"
				row.put("V02471", awsModel.getGroundTemperature());
				row.put("V02472_005", awsModel.getGroundTemperature5cm());
				row.put("V02472_010", awsModel.getGroundTemperature10cm());
				row.put("V02472_015", awsModel.getGroundTemperature15cm());
				row.put("V02472_020", awsModel.getGroundTemperature20cm());
				row.put("V02472_040", awsModel.getGroundTemperature40cm());
				row.put("V02472_080", awsModel.getGroundTemperature80cm());
				row.put("V02472_160", awsModel.getGroundTemperature160cm());
				row.put("V02472_320", awsModel.getGroundTemperature320cm());
				row.put("V02473", awsModel.getEvaporation());
				//+ "V02474,V02475,V02476,V02477,V02491,V02492,V02495,V02496,V02497,V14421,"
				row.put("V02474", awsModel.getSunshine());
				row.put("V02475", awsModel.getVisibility());
				row.put("V02476", awsModel.getCloudAmount());
				row.put("V02477", awsModel.getCloudHeight());
				row.put("V02491", awsModel.getSubStationClock());
				row.put("V02492", awsModel.getDataCollection());
				row.put("V02495", awsModel.getPowerSupply());
				row.put("V02496", awsModel.getMotherboardVoltage());
				row.put("V02497", awsModel.getMotherboardTemperature());
				row.put("V14421", awsModel.getCollectorCommunication());
//				+ "V12505,V15750,V14420_12,V14420_13,V14422,V14420_22,V14420_23,V14420_32,V14420_33,V14420_42,"
				row.put("V12505", awsModel.getChassisTemperature());
				row.put("V15750", awsModel.getPowerVoltage());
				row.put("V14420_12", awsModel.getSolar());
				row.put("V14420_13", awsModel.getSolarVentilation());
				row.put("V14422", awsModel.getTracker());
				row.put("V14420_22", awsModel.getScattering());
				row.put("V14420_23", awsModel.getScatteringVentilation());
				row.put("V14420_32", awsModel.getPyranometer());
				row.put("V14420_33", awsModel.getPyranometerVentilation());
				row.put("V14420_42", awsModel.getReflection());
//				+ "V14420_43,V14420_52,V14420_53,V14420_54,V14420_62,V14420_63,V14420_64,V14420_72,V14420_74,V14420_82,"
				row.put("V14420_43", awsModel.getReflectionVentilation());
				row.put("V14420_52", awsModel.getLongWave());
				row.put("V14420_53", awsModel.getLongWaveVentilation());
				row.put("V14420_54", awsModel.getLongWaveTemperature());
				row.put("V14420_62", awsModel.getEarthWave());
				row.put("V14420_63", awsModel.getEarthWaveVentilation());
				row.put("V14420_64", awsModel.getEarthWaveTemperature());
				row.put("V14420_72", awsModel.getUltraviolet());
				row.put("V14420_74", awsModel.getUltravioletTemperature());
				row.put("V14420_82", awsModel.getPARMeter());
//				+ "V_InstrumentS,V_ClimateSta,V_GroundTemStau,V_HumiditySta,V_RadiationSta,V_SensorWork,V_Tem15dm,V_GrassSurf,V_Surface,V_Ground5cm,"
				row.put("V_InstrumentS", awsModel.getInstrument());
				row.put("V_ClimateSta", awsModel.getClimate());
				row.put("V_GroundTemStau", awsModel.getGroundTemperature_SelfCheck());
				row.put("V_HumiditySta", awsModel.getHumidity_SelfCheck());
				row.put("V_RadiationSta", awsModel.getRadiation());
				row.put("V_SensorWork", awsModel.getSensorWorking());
				row.put("V_Tem15dm", awsModel.getTemperature15dm());
				row.put("V_GrassSurf", awsModel.getGrassSurface());
				row.put("V_Surface", awsModel.getSurface());
				row.put("V_Ground5cm", awsModel.getGround5cm());
//				+ "V_Ground10cm,V_Ground15cm,V_Ground20cm,V_Ground40cm,V_Ground80cm,V_Ground160cm,V_Ground320cm,V_SurfTem,V_FreezingTem,V_RH15dm,"
				row.put("V_Ground10cm", awsModel.getGround10cm());
				row.put("V_Ground15cm", awsModel.getGround15cm());
				row.put("V_Ground20cm", awsModel.getGround20cm());
				row.put("V_Ground40cm", awsModel.getGround40cm());
				row.put("V_Ground80cm", awsModel.getGround80cm());
				row.put("V_Ground160cm", awsModel.getGround160cm());
				row.put("V_Ground320cm", awsModel.getGround320cm());
				row.put("V_SurfTem", awsModel.getSurfaceTemperature());
				row.put("V_FreezingTem", awsModel.getFreezingTemperature());
				row.put("V_RH15dm", awsModel.getRelativeHumidity15dm());
//				+ "V_WindDir,V_WindSpeed,V_Pressure,V_NoWeiRain,V_WeiRain,V_Evaporation,V_Pyrwork,V_RefSensor,V_DireSensor,V_ScatSensor,"
				row.put("V_WindDir", awsModel.getWindDirection_State());
				row.put("V_WindSpeed", awsModel.getWindSpeed_State());
				row.put("V_Pressure", awsModel.getPressure_State());
				row.put("V_NoWeiRain", awsModel.getNoWeighingRaingauge());
				row.put("V_WeiRain", awsModel.getWeighingRaingauge());
				row.put("V_Evaporation", awsModel.getEvaporation_State());
				row.put("V_Pyrwork", awsModel.getPyranometer_State());
				row.put("V_RefSensor", awsModel.getReflection_State());
				row.put("V_DireSensor", awsModel.getDirect());
				row.put("V_ScatSensor", awsModel.getScattering_State());
				// + "V_NetPyr,V_UlABSensor,V_UltraASensor,V_UltraBSensor,V_PARMeterSensor,V_LWaveSensor,V_GWaveSensor,V_Sunshine,V_CloudHeight,V_CloudAmount,"
				row.put("V_NetPyr", awsModel.getNetPyranometer());
				row.put("V_UlABSensor", awsModel.getUltravioletAB());
				row.put("V_UltraASensor", awsModel.getUltravioletA());
				row.put("V_UltraBSensor", awsModel.getUltravioletB());
				row.put("V_PARMeterSensor", awsModel.getPARMeter_State());
				row.put("V_LWaveSensor", awsModel.getLongWave_State());
				row.put("V_GWaveSensor", awsModel.getGroundWave());
				row.put("V_Sunshine", awsModel.getSunshine_State());
				row.put("V_CloudHeight", awsModel.getCloudHeight_State());
				row.put("V_CloudAmount", awsModel.getCloudAmount_State());
				//+ "V_CloudForm,V_VisIns,V_PheIns,V_AntennaIcing,V_RoadCondition,V_ExternalPow,V_Climate,V_GroundTemP,V_HumidityP,V_RadiationP,"
				row.put("V_CloudForm", awsModel.getCloudForm());
				row.put("V_VisIns", awsModel.getVisibilityInstrument());
				row.put("V_PheIns", awsModel.getPhenomenonInstrument());
				row.put("V_AntennaIcing", awsModel.getAntennaIcing());
				row.put("V_RoadCondition", awsModel.getRoadCondition());
				row.put("V_ExternalPow", awsModel.getExternalPower());
				row.put("V_Climate", awsModel.getClimate_State());
				row.put("V_GroundTemP", awsModel.getGroundTemperature_PowerState());
				row.put("V_HumidityP", awsModel.getHumidity_PowerState());
				row.put("V_RadiationP", awsModel.getRadiation_PowerState());
				//+ "V_InstrumentV,V_ClimateV,V_GroundTemV,V_HumidityV,V_RadiationV,V_ImageV,V_BatteryVoltage,V_AC-DCV,V_VisorV,V_TiltHeadV,"
				row.put("V_InstrumentV", awsModel.getInstrumentVoltage());
				row.put("V_ClimateV", awsModel.getClimateVoltage());
				row.put("V_GroundTemV", awsModel.getGroundTemperatureVoltage());
				row.put("V_HumidityV", awsModel.getHumidityVoltage());
				row.put("V_RadiationV", awsModel.getRadiationVoltage());
				row.put("V_ImageV", awsModel.getImageVoltage());
				row.put("V_BatteryVoltage", awsModel.getBatteryVoltage());
				row.put("V_AC_DCV", awsModel.getACDCVoltage());
				row.put("V_VisorV", awsModel.getVisorVoltage());
				row.put("V_TiltHeadV", awsModel.getTiltHeadVoltage());
				//+ "V_InsC,V_TemC,V_GroundTemC,V_HumidityC,V_RadiationC,V_SolarPanels,V_InstaTem,V_Temmb,V_GroundTemmb,V_Humiditymb,"
				row.put("V_InsC", awsModel.getInstrumentCurrent());
				row.put("V_TemC", awsModel.getTemperatureCurrent());
				row.put("V_GroundTemC", awsModel.getGroundTemperatureCurrent());
				row.put("V_HumidityC", awsModel.getHumidityCurrent());
				row.put("V_RadiationC", awsModel.getRadiationCurrent());
				row.put("V_SolarPanels", awsModel.getSolarPanels());
				row.put("V_InstaTem", awsModel.getInstrument_TemperatureState());
				row.put("V_Temmb", awsModel.getTemperatureState());
				row.put("V_GroundTemmb", awsModel.getGroundTemperature_State());
				row.put("V_Humiditymb", awsModel.getHumidity_State());
				//+ "V_Radiationmb,V_Probe,V_Chassis,V_PyrC,V_RefCav,V_DirCav,V_ScaCav,V_NetPyrCav,V_UltCavAB,V_UltCavA,"
				row.put("V_Radiationmb", awsModel.getRadiationMotherboard());
				row.put("V_Probe", awsModel.getProbe());
				row.put("V_Chassis", awsModel.getChassis());
				row.put("V_PyrC", awsModel.getPyranometerCavity());
				row.put("V_RefCav", awsModel.getReflectionCavity());
				row.put("V_DirCav", awsModel.getDirectCavity());
				row.put("V_ScaCav", awsModel.getScatteringCavity());
				row.put("V_NetPyrCav", awsModel.getNetPyranometerCavity());
				row.put("V_UltCavAB", awsModel.getUltravioletCavityAB());
				row.put("V_UltCavA", awsModel.getUltravioletCavityA());
				//+ "V_UltCavB,V_PARCav,V_LWaveCav,V_GWaveCav,V_Thermostat,V_PyrTher,V_RefTherm,V_DirectTher,V_ScaTher,V_NetPyrTher,"
				row.put("V_UltCavB", awsModel.getUltravioletCavityB());
				row.put("V_PARCav", awsModel.getPARMeterCavity());
				row.put("V_LWaveCav", awsModel.getLongWaveCavity());
				row.put("V_GWaveCav", awsModel.getGroundWaveCavity());
				row.put("V_Thermostat", awsModel.getThermostat());
				row.put("V_PyrTher", awsModel.getPyranometerThermostat());	
				row.put("V_RefTherm", awsModel.getReflectionThermostat());
				row.put("V_DirectTher", awsModel.getDirectThermostat());
				row.put("V_ScaTher", awsModel.getScatteringThermostat());
				row.put("V_NetPyrTher", awsModel.getNetPyranometerThermostat());
				//+ "V_UltTherAB,V_UltTherA,V_UltTherB,V_PARTher,V_LWaveTher,V_GWaveTher,V_ChassisTem,V_EquHea,V_TranHeat,V_ReceHeat,"
				row.put("V_UltTherAB", awsModel.getUltravioletThermostatAB());
				row.put("V_UltTherA", awsModel.getUltravioletThermostatA());
				row.put("V_UltTherB", awsModel.getUltravioletThermostatB());
				row.put("V_PARTher", awsModel.getPARMeterThermostat());
				row.put("V_LWaveTher", awsModel.getLongWaveThermostat());
				row.put("V_GWaveTher", awsModel.getGroundWaveThermostat());
				row.put("V_ChassisTem", awsModel.getChassisTemperature_State());
				row.put("V_EquHea", awsModel.getEquipmentHeating());
				row.put("V_TranHeat", awsModel.getTransmitterHeating());
				row.put("V_ReceHeat", awsModel.getReceiverHeating());
				//+ "V_CameHeat,V_VideoCaHeat,V_Instrument,V_Transmitter,V_Receiver,V_Hood,V_HoodSpeed,V_PyrSta,V_Reflection,V_Direct,"
				row.put("V_CameHeat", awsModel.getCameraHeating());
				row.put("V_VideoCaHeat", awsModel.getVideoCameraHeating());
				row.put("V_Instrument", awsModel.getInstrument_State());
				row.put("V_Transmitter", awsModel.getTransmitter());
				row.put("V_Receiver", awsModel.getReceiver());
				row.put("V_Hood", awsModel.getHood());
				row.put("V_HoodSpeed", awsModel.getHoodSpeed());
				row.put("V_PyrSta", awsModel.getPyranometer_AirState());
				row.put("V_Reflection", awsModel.getReflection_AirState());
				row.put("V_Direct", awsModel.getDirect_AirState());
				//+ "V_Scattering,V_NetPyrmeter,V_UltravAB,V_UltraA,V_UltraB,V_PARMeter,V_LongWave,V_GroundWave,V_Connection,V_Bus,"
				row.put("V_Scattering", awsModel.getScattering_AirState());
				row.put("V_NetPyrmeter", awsModel.getNetPyranometer_State());
				row.put("V_UltravAB", awsModel.getUltravioletAB_AirState());
				row.put("V_UltraA", awsModel.getUltravioletA_AirState());
				row.put("V_UltraB", awsModel.getUltravioletAB_AirState());
				row.put("V_PARMeter", awsModel.getPARMeter_AirState());
				row.put("V_LongWave", awsModel.getLongWave_AirState());
				row.put("V_GroundWave", awsModel.getGroundWave_AirState());
				row.put("V_Connection", awsModel.getConnection());
				row.put("V_Bus", awsModel.getBus());
				//+ "V_RS232_485_422,V_TemRS232_485_422,V_GroundTemRS232_485_422,V_HumiRS232_485_422,V_RadiRS232_485_422,V_RJ45_LAN,V_Satellite,V_Wireless,V_OpticalFiber,V_Window,"
				row.put("V_RS232_485_422", awsModel.getRS232_485_422());
				row.put("V_TemRS232_485_422", awsModel.getTemperatureRS232_485_422());
				row.put("V_GroundTemRS232_485_422", awsModel.getGroundTemperatureRS232_485_422());
				row.put("V_HumiRS232_485_422", awsModel.getHumidityRS232_485_422());
				row.put("V_RadiRS232_485_422", awsModel.getRadiationRS232_485_422());
				row.put("V_RJ45_LAN", awsModel.getRJ45_LAN());
				row.put("V_Satellite", awsModel.getSatellite());
				row.put("V_Wireless", awsModel.getWireless());
				row.put("V_OpticalFiber", awsModel.getOpticalFiber());
				row.put("V_Window", awsModel.getWindow());
				//	+ "V_Detector,V_CameraLens,V_VideoCaLens,V_TransPower,V_Rece,V_Trans,V_Visor,V_TiltHead,V_VideoCamera,V_Camera,"
				row.put("V_Detector", awsModel.getDetector());
				row.put("V_CameraLens", awsModel.getCameraLens());
				row.put("V_VideoCaLens", awsModel.getVideoCameraLens());
				row.put("V_TransPower", awsModel.getTransmitterPower());
				row.put("V_Rece", awsModel.getReceiver_State());
				row.put("V_Trans", awsModel.getTransmitter_State());
				row.put("V_Visor", awsModel.getVisor());
				row.put("V_TiltHead", awsModel.getTiltHead());
				row.put("V_VideoCamera", awsModel.getVideoCamera());
				row.put("V_Camera", awsModel.getCamera());
				//+ "V_Tracker,V_Collector,V_Tem,V_GroundTemCol,V_Humidity,V_Radiation,V_AD,V_TemAD,V_GroundTemAD,V_HumidityAD,"
				row.put("V_Tracker", awsModel.getTracker_State());
				row.put("V_Collector", awsModel.getCollector());
				row.put("V_Tem", awsModel.getTemperature_State());
				row.put("V_GroundTemCol", awsModel.getGroundTemperature_WorkState());
				row.put("V_Humidity", awsModel.getHumidity_WorkState());
				row.put("V_Radiation", awsModel.getRadiation_WorkState());
				row.put("V_AD", awsModel.getAD());
				row.put("V_TemAD", awsModel.getTemperatureAD());
				row.put("V_GroundTemAD", awsModel.getGroundTemperatureAD());
				row.put("V_HumidityAD", awsModel.getHumidityAD());
				//	+ "V_RadiationAD,V_Count,V_TemCount,V_GroundTemCount,V_HumiCount,V_RadiCount,V_Gate,V_TemGate,V_GroundTemGate,V_HumidityGate,"
				row.put("V_RadiationAD", awsModel.getRadiationAD());
				row.put("V_Count", awsModel.getCounter());
				row.put("V_TemCount", awsModel.getTemperatureCounter());
				row.put("V_GroundTemCount", awsModel.getGroundTemperatureCounter());
				row.put("V_HumiCount", awsModel.getHumidityCounter());
				row.put("V_RadiCount", awsModel.getRadiationCounter());
				row.put("V_Gate", awsModel.getGate_State());
				row.put("V_TemGate", awsModel.getTemperatureGate());
				row.put("V_GroundTemGate", awsModel.getGroundTemperatureGate());
				row.put("V_HumidityGate", awsModel.getHumidityGate());
				//+ "V_RadGate,V_Water,V_Displacement,V_WaterLevel,V_WeiSensor,V_EvapPonds,V_ExMemCard,V_PartsSp,V_PartsVibnFre,V_PosiAid,"
				row.put("V_RadGate", awsModel.getRadiationGate());
				row.put("V_Water", awsModel.getWater());
				row.put("V_Displacement", awsModel.getDisplacement());
				row.put("V_WaterLevel", awsModel.getWaterLevel());
				row.put("V_WeiSensor", awsModel.getWeighingSensor());
				row.put("V_EvapPonds", awsModel.getEvaporationPonds());
				row.put("V_ExMemCard", awsModel.getExternalMemoryCard());
				row.put("V_PartsSp", awsModel.getPartsSpeed());
				row.put("V_PartsVibnFre", awsModel.getPartsVibrationFrequency());
				row.put("V_PosiAid", awsModel.getPositioningAid());
				//+ "V_CalibTimeEqu,V_InstSta,V_InstNameStatus,V_InstPath,V_Observer,V_StartTime,V_EndTime,V_OperCont,V_StaName,V_InstNameMaint,"
				row.put("V_CalibTimeEqu", awsModel.getCalibrationTimeEquipment());
				row.put("V_InstSta", awsModel.getInstrumentStatus());
				row.put("V_InstNameStatus", awsModel.getInstrumentName());
				row.put("V_InstPath", awsModel.getInstrumentPath());
				row.put("V_Observer", awsModel.getObserver());
				row.put("V_StartTime", awsModel.getStartTime());
				row.put("V_EndTime", awsModel.getEndTime());
				row.put("V_OperCont", awsModel.getOperationContent());
				row.put("V_StaName", awsModel.getStationName());
				row.put("V_InstNameMaint", awsModel.getInstrumentName_Maintain());
				//+ "V_Downtime,V_FaultPhen,V_FaultType,V_FaultCause,V_Maintenance,V_MaintMan,V_MaintStaTime,V_MaintEndTime)";
				row.put("V_Downtime", awsModel.getDowntime());
				row.put("V_FaultPhen", awsModel.getFaultPhenomenon());
				row.put("V_FaultType", awsModel.getFaultType());
				row.put("V_FaultCause", awsModel.getFaultCause());
				row.put("V_Maintenance", awsModel.getMaintenance());
				row.put("V_MaintMan", awsModel.getMaintenanceMan());
				row.put("V_MaintStaTime", awsModel.getMaintenanceStartTime());
				row.put("V_MaintEndTime", awsModel.getMaintenanceEndtTime());
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
				di.setIIiii(awsModel.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(date, "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				di.setLATITUDE(String.valueOf(awsModel.getLatitude()));
				di.setLONGTITUDE(String.valueOf(awsModel.getLongitude()));
			    try {
					OTSDbHelper.getInstance().insert(tablename, row);
					diQueues.offer(di);
				}  catch (Exception e) {
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
	        loggerBuffer.append(" \nINSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
	        loggerBuffer.append(" INSERT SUCCESS COUNT : " + successRowCount + "\n");
	        loggerBuffer.append(" INSERT FAILED COUNT : " + (list.size() - successRowCount) + "\n");
		}
		return DataBaseAction.SUCCESS;
	}
}
