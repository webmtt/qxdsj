package cma.cimiss2.dpc.indb.surf.aws.service;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.surf.AWS;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class DbService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	private static String sod_code = StartConfig.sodCode();
	private static String cts_code = StartConfig.ctsCode();
	public static String V_TT = "地面设备";    // 报文资料类别
	public static BlockingQueue<StatDi> diQueues;
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	
	public static void setDiQueues(BlockingQueue<StatDi> diQueues){
		DbService.diQueues = diQueues;
	}
	public static BlockingQueue<StatDi> getDiQueues(){
		return diQueues;
	}
	/**
	 * 
	 * @Title: processSuccessReport   
	 * @Description: TODO(地面设备状态信息数据的处理)   
	 * @param  parseResult 解码结果集
	 * @param  recv_time    资料接收时间
	 * @param  fileN 解码文件名
	 * @return: DataBaseAction      
	 */
	public static DataBaseAction processSuccessReport(ParseResult<AWS> parseResult, Date recv_time, String fileN) {
		java.sql.Connection connection = ConnectionPoolFactory.getInstance().getConnection("rdb");

		if(connection != null) {
			List<AWS> awss = parseResult.getData();
			insertDB(awss, connection, recv_time, fileN);
			
			for (int j = 0; j < listDi.size(); j++) {
				diQueues.offer(listDi.get(j));
			}
			listDi.clear();
		}
		
		return DataBaseAction.SUCCESS;
	}
	
	/**
	 * 
	 * @Title: insertDB   
	 * @Description: TODO(地面设备状态信息资料入库)   
	 * @param  awss 待入库对象集合
	 * @param  connection 数据库连接
	 * @param  recv_time      资料接收时间
	 * @param fileN 入库文件名称
	 * @return: void      D_RECORD_ID,
	 */
	private static void insertDB(List<AWS> awss, java.sql.Connection connection, Date recv_time, String fileN) {
		PreparedStatement prestmt = null;
		String table_name = StartConfig.valueTable();
		String sql = "insert into " + table_name + "(D_RECORD_ID,D_DATA_ID,D_DATETIME,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,"
				+ "V01301,V_BBB,V04001,V04002,V04003,V04004,V04005,V04006,V05001,V06001,"
				+ "V02460,V02461,V02462,V02463,V02464,V02465,V02466,V02467,V02468,V02470,"
				+ "V02471,V02472_005,V02472_010,V02472_015,V02472_020,V02472_040,V02472_080,V02472_160,V02472_320,V02473,"
				+ "V02474,V02475,V02476,V02477,V02491,V02492,V02495,V02496,V02497,V14421,"
				+ "V12505,V15750,V14420_12,V14420_13,V14422,V14420_22,V14420_23,V14420_32,V14420_33,V14420_42,"
				+ "V14420_43,V14420_52,V14420_53,V14420_54,V14420_62,V14420_63,V14420_64,V14420_72,V14420_74,V14420_82,"
				+ "V_InstrumentS,V_ClimateSta,V_GroundTemStau,V_HumiditySta,V_RadiationSta,V_SensorWork,V_Tem15dm,V_GrassSurf,V_Surface,V_Ground5cm,"
				+ "V_Ground10cm,V_Ground15cm,V_Ground20cm,V_Ground40cm,V_Ground80cm,V_Ground160cm,V_Ground320cm,V_SurfTem,V_FreezingTem,V_RH15dm,"
				+ "V_WindDir,V_WindSpeed,V_Pressure,V_NoWeiRain,V_WeiRain,V_Evaporation,V_Pyrwork,V_RefSensor,V_DireSensor,V_ScatSensor,"
				+ "V_NetPyr,V_UlABSensor,V_UltraASensor,V_UltraBSensor,V_PARMeterSensor,V_LWaveSensor,V_GWaveSensor,V_Sunshine,V_CloudHeight,V_CloudAmount,"
				+ "V_CloudForm,V_VisIns,V_PheIns,V_AntennaIcing,V_RoadCondition,V_ExternalPow,V_Climate,V_GroundTemP,V_HumidityP,V_RadiationP,"
				+ "V_InstrumentV,V_ClimateV,V_GroundTemV,V_HumidityV,V_RadiationV,V_ImageV,V_BatteryVoltage,V_AC_DCV,V_VisorV,V_TiltHeadV,"
				+ "V_InsC,V_TemC,V_GroundTemC,V_HumidityC,V_RadiationC,V_SolarPanels,V_InstaTem,V_Temmb,V_GroundTemmb,V_Humiditymb,"
				+ "V_Radiationmb,V_Probe,V_Chassis,V_PyrC,V_RefCav,V_DirCav,V_ScaCav,V_NetPyrCav,V_UltCavAB,V_UltCavA,"
				+ "V_UltCavB,V_PARCav,V_LWaveCav,V_GWaveCav,V_Thermostat,V_PyrTher,V_RefTherm,V_DirectTher,V_ScaTher,V_NetPyrTher,"
				+ "V_UltTherAB,V_UltTherA,V_UltTherB,V_PARTher,V_LWaveTher,V_GWaveTher,V_ChassisTem,V_EquHea,V_TranHeat,V_ReceHeat,"
				+ "V_CameHeat,V_VideoCaHeat,V_Instrument,V_Transmitter,V_Receiver,V_Hood,V_HoodSpeed,V_PyrSta,V_Reflection,V_Direct,"
				+ "V_Scattering,V_NetPyrmeter,V_UltravAB,V_UltraA,V_UltraB,V_PARMeter,V_LongWave,V_GroundWave,V_Connection,V_Bus,"
				+ "V_RS232_485_422,V_TemRS232_485_422,V_GroundTemRS232_485_422,V_HumiRS232_485_422,V_RadiRS232_485_422,V_RJ45_LAN,V_Satellite,V_Wireless,V_OpticalFiber,V_Window,"
				+ "V_Detector,V_CameraLens,V_VideoCaLens,V_TransPower,V_Rece,V_Trans,V_Visor,V_TiltHead,V_VideoCamera,V_Camera,"
				+ "V_Tracker,V_Collector,V_Tem,V_GroundTemCol,V_Humidity,V_Radiation,V_AD,V_TemAD,V_GroundTemAD,V_HumidityAD,"
				+ "V_RadiationAD,V_Count,V_TemCount,V_GroundTemCount,V_HumiCount,V_RadiCount,V_Gate,V_TemGate,V_GroundTemGate,V_HumidityGate,"
				+ "V_RadGate,V_Water,V_Displacement,V_WaterLevel,V_WeiSensor,V_EvapPonds,V_ExMemCard,V_PartsSp,V_PartsVibnFre,V_PosiAid,"
				+ "V_CalibTimeEqu,V_InstSta,V_InstNameStatus,V_InstPath,V_Observer,V_StartTime,V_EndTime,V_OperCont,V_StaName,V_InstNameMaint,"
				+ "V_Downtime,V_FaultPhen,V_FaultType,V_FaultCause,V_Maintenance,V_MaintMan,V_MaintStaTime,V_MaintEndTime, D_SOURCE_ID)";
		sql +="values(?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?)";
		if(connection != null){		
			try {	
				prestmt = new LoggableStatement(connection, sql);
				if(StartConfig.getDatabaseType() == 1) {
					prestmt.execute("select last_txc_xid()");
				}
				List<String> sqls = new ArrayList<>();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");		
				
				for (int i = 0; i < awss.size(); i++) {
					AWS awsModel = awss.get(i);
					String station = awsModel.getStationNumberChina();
					Date date = awsModel.getObservationTime();
					
					StatDi di = new StatDi();	
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(sod_code);
					di.setDATA_TYPE_1(cts_code);
					di.setTT("地面设备");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1");  //1成功，0失败
					di.setIIiii(station);
					di.setDATA_TIME(TimeUtil.date2String(date, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(awsModel.getLatitude()));
					di.setLONGTITUDE(String.valueOf(awsModel.getLongitude()));
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(fileN).length()));
					di.setDATA_UPDATE_FLAG("000");
					di.setHEIGHT("999999");
					
					listDi.add(di);
					
					int ii = 1;
					prestmt.setString(ii++, sdf.format(date) + "_" + awsModel.getStationNumberChina());
					prestmt.setString(ii++, sod_code);
					prestmt.setTimestamp(ii++, new Timestamp(date.getTime()));
					prestmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					prestmt.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					prestmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					// "V01301,V_BBB,V04001,V04002,V04003,V04004,V04005,V04006,V05001,V06001,"
					prestmt.setString(ii++, station);
					prestmt.setString(ii++, "000");
					prestmt.setInt(ii++, date.getYear() + 1900);
					prestmt.setInt(ii++, date.getMonth() + 1);
					prestmt.setInt(ii++, date.getDate());
					prestmt.setInt(ii++, date.getHours());
					prestmt.setInt(ii++, date.getMinutes());
					prestmt.setInt(ii++, date.getSeconds());
					prestmt.setBigDecimal(ii++,new BigDecimal(String.valueOf(awsModel.getLatitude())));
					prestmt.setBigDecimal(ii++,new BigDecimal(String.valueOf(awsModel.getLongitude())));
					//+ "V02460,V02461,V02462,V02463,V02464,V02465,V02466,V02467,V02468,V02470,"
					prestmt.setInt(ii++, awsModel.getCommunication());
					prestmt.setInt(ii++, awsModel.getPressure());
					prestmt.setInt(ii++, awsModel.getTemperature());
					prestmt.setInt(ii++, awsModel.getWetbulbTemperature());
					prestmt.setInt(ii++, awsModel.getHumidity());
					prestmt.setInt(ii++, awsModel.getWindDirection());
					prestmt.setInt(ii++, awsModel.getWindSpeed());
					prestmt.setInt(ii++, awsModel.getRainfall());
					prestmt.setInt(ii++, awsModel.getRain());
					prestmt.setInt(ii++, awsModel.getGrassTemperature());
					//+ "V02471,V02472_005,V02472_010,V02472_015,V02472_020,V02472_040,V02472_080,V02472_160,V02472_320,V02473,"
					prestmt.setInt(ii++, awsModel.getGroundTemperature());
					prestmt.setInt(ii++, awsModel.getGroundTemperature5cm());
					prestmt.setInt(ii++, awsModel.getGroundTemperature10cm());
					prestmt.setInt(ii++, awsModel.getGroundTemperature15cm());
					prestmt.setInt(ii++, awsModel.getGroundTemperature20cm());
					prestmt.setInt(ii++, awsModel.getGroundTemperature40cm());
					prestmt.setInt(ii++, awsModel.getGroundTemperature80cm());
					prestmt.setInt(ii++, awsModel.getGroundTemperature160cm());
					prestmt.setInt(ii++, awsModel.getGroundTemperature320cm());
					prestmt.setInt(ii++, awsModel.getEvaporation());
					//+ "V02474,V02475,V02476,V02477,V02491,V02492,V02495,V02496,V02497,V14421,"
					prestmt.setInt(ii++, awsModel.getSunshine());
					prestmt.setInt(ii++, awsModel.getVisibility());
					prestmt.setInt(ii++, awsModel.getCloudAmount());
					prestmt.setInt(ii++, awsModel.getCloudHeight());
					prestmt.setInt(ii++, awsModel.getSubStationClock());
					prestmt.setInt(ii++, awsModel.getDataCollection());
					prestmt.setInt(ii++, awsModel.getPowerSupply());
					prestmt.setBigDecimal(ii++,new BigDecimal(String.valueOf(awsModel.getMotherboardVoltage())));
					prestmt.setBigDecimal(ii++,new BigDecimal(String.valueOf(awsModel.getMotherboardTemperature())));
					prestmt.setInt(ii++, awsModel.getCollectorCommunication());
//					+ "V12505,V15750,V14420_12,V14420_13,V14422,V14420_22,V14420_23,V14420_32,V14420_33,V14420_42,"
					prestmt.setBigDecimal(ii++,new BigDecimal(String.valueOf(awsModel.getChassisTemperature())));
					prestmt.setBigDecimal(ii++,new BigDecimal(String.valueOf(awsModel.getPowerVoltage())));
					prestmt.setInt(ii++, awsModel.getSolar());
					prestmt.setInt(ii++, awsModel.getSolarVentilation());
					prestmt.setInt(ii++, awsModel.getTracker());
					prestmt.setInt(ii++, awsModel.getScattering());
					prestmt.setInt(ii++, awsModel.getScatteringVentilation());
					prestmt.setInt(ii++, awsModel.getPyranometer());
					prestmt.setInt(ii++, awsModel.getPyranometerVentilation());
					prestmt.setInt(ii++, awsModel.getReflection());
//					+ "V14420_43,V14420_52,V14420_53,V14420_54,V14420_62,V14420_63,V14420_64,V14420_72,V14420_74,V14420_82,"
					prestmt.setInt(ii++, awsModel.getReflectionVentilation());
					prestmt.setInt(ii++, awsModel.getLongWave());
					prestmt.setInt(ii++, awsModel.getLongWaveVentilation());
					prestmt.setInt(ii++, awsModel.getLongWaveTemperature());
					prestmt.setInt(ii++, awsModel.getEarthWave());
					prestmt.setInt(ii++, awsModel.getEarthWaveVentilation());
					prestmt.setInt(ii++, awsModel.getEarthWaveTemperature());
					prestmt.setInt(ii++, awsModel.getUltraviolet());
					prestmt.setInt(ii++, awsModel.getUltravioletTemperature());
					prestmt.setInt(ii++, awsModel.getPARMeter());
//					+ "V_InstrumentS,V_ClimateSta,V_GroundTemStau,V_HumiditySta,V_RadiationSta,V_SensorWork,V_Tem15dm,V_GrassSurf,V_Surface,V_Ground5cm,"
					prestmt.setInt(ii++, awsModel.getInstrument());
					prestmt.setInt(ii++, awsModel.getClimate());
					prestmt.setInt(ii++, awsModel.getGroundTemperature_SelfCheck());
					prestmt.setInt(ii++, awsModel.getHumidity_SelfCheck());
					prestmt.setInt(ii++, awsModel.getRadiation());
					prestmt.setInt(ii++, awsModel.getSensorWorking());
					prestmt.setInt(ii++, awsModel.getTemperature15dm());
					prestmt.setInt(ii++, awsModel.getGrassSurface());
					prestmt.setInt(ii++, awsModel.getSurface());
					prestmt.setInt(ii++, awsModel.getGround5cm());
//					+ "V_Ground10cm,V_Ground15cm,V_Ground20cm,V_Ground40cm,V_Ground80cm,V_Ground160cm,V_Ground320cm,V_SurfTem,V_FreezingTem,V_RH15dm,"
					prestmt.setInt(ii++, awsModel.getGround10cm());
					prestmt.setInt(ii++, awsModel.getGround15cm());
					prestmt.setInt(ii++, awsModel.getGround20cm());
					prestmt.setInt(ii++, awsModel.getGround40cm());
					prestmt.setInt(ii++, awsModel.getGround80cm());
					prestmt.setInt(ii++, awsModel.getGround160cm());
					prestmt.setInt(ii++, awsModel.getGround320cm());
					prestmt.setInt(ii++, awsModel.getSurfaceTemperature());
					prestmt.setInt(ii++, awsModel.getFreezingTemperature());
					prestmt.setInt(ii++, awsModel.getRelativeHumidity15dm());
//					+ "V_WindDir,V_WindSpeed,V_Pressure,V_NoWeiRain,V_WeiRain,V_Evaporation,V_Pyrwork,V_RefSensor,V_DireSensor,V_ScatSensor,"
					prestmt.setInt(ii++, awsModel.getWindDirection_State());
					prestmt.setInt(ii++, awsModel.getWindSpeed_State());
					prestmt.setInt(ii++, awsModel.getPressure_State());
					prestmt.setInt(ii++, awsModel.getNoWeighingRaingauge());
					prestmt.setInt(ii++, awsModel.getWeighingRaingauge());
					prestmt.setInt(ii++, awsModel.getEvaporation_State());
					prestmt.setInt(ii++, awsModel.getPyranometer_State());
					prestmt.setInt(ii++, awsModel.getReflection_State());
					prestmt.setInt(ii++, awsModel.getDirect());
					prestmt.setInt(ii++, awsModel.getScattering_State());
					// + "V_NetPyr,V_UlABSensor,V_UltraASensor,V_UltraBSensor,V_PARMeterSensor,V_LWaveSensor,V_GWaveSensor,V_Sunshine,V_CloudHeight,V_CloudAmount,"
					prestmt.setInt(ii++, awsModel.getNetPyranometer());
					prestmt.setInt(ii++, awsModel.getUltravioletAB());
					prestmt.setInt(ii++, awsModel.getUltravioletA());
					prestmt.setInt(ii++, awsModel.getUltravioletB());
					prestmt.setInt(ii++, awsModel.getPARMeter_State());
					prestmt.setInt(ii++, awsModel.getLongWave_State());
					prestmt.setInt(ii++, awsModel.getGroundWave());
					prestmt.setInt(ii++, awsModel.getSunshine_State());
					prestmt.setInt(ii++, awsModel.getCloudHeight_State());
					prestmt.setInt(ii++, awsModel.getCloudAmount_State());
					//+ "V_CloudForm,V_VisIns,V_PheIns,V_AntennaIcing,V_RoadCondition,V_ExternalPow,V_Climate,V_GroundTemP,V_HumidityP,V_RadiationP,"
					prestmt.setInt(ii++, awsModel.getCloudForm());
					prestmt.setInt(ii++, awsModel.getVisibilityInstrument());
					prestmt.setInt(ii++, awsModel.getPhenomenonInstrument());
					prestmt.setInt(ii++, awsModel.getAntennaIcing());
					prestmt.setInt(ii++, awsModel.getRoadCondition());
					prestmt.setInt(ii++, awsModel.getExternalPower());
					prestmt.setInt(ii++, awsModel.getClimate_State());
					prestmt.setInt(ii++, awsModel.getGroundTemperature_PowerState());
					prestmt.setInt(ii++, awsModel.getHumidity_PowerState());
					prestmt.setInt(ii++, awsModel.getRadiation_PowerState());
					//+ "V_InstrumentV,V_ClimateV,V_GroundTemV,V_HumidityV,V_RadiationV,V_ImageV,V_BatteryVoltage,V_AC_DCV,V_VisorV,V_TiltHeadV,"
					prestmt.setInt(ii++, awsModel.getInstrumentVoltage());
					prestmt.setInt(ii++, awsModel.getClimateVoltage());
					prestmt.setInt(ii++, awsModel.getGroundTemperatureVoltage());
					prestmt.setInt(ii++, awsModel.getHumidityVoltage());
					prestmt.setInt(ii++, awsModel.getRadiationVoltage());
					prestmt.setInt(ii++, awsModel.getImageVoltage());
					prestmt.setInt(ii++, awsModel.getBatteryVoltage());
					prestmt.setInt(ii++, awsModel.getACDCVoltage());
					prestmt.setInt(ii++, awsModel.getVisorVoltage());
					prestmt.setInt(ii++, awsModel.getTiltHeadVoltage());
					//+ "V_InsC,V_TemC,V_GroundTemC,V_HumidityC,V_RadiationC,V_SolarPanels,V_InstaTem,V_Temmb,V_GroundTemmb,V_Humiditymb,"
					prestmt.setInt(ii++, awsModel.getInstrumentCurrent());
					prestmt.setInt(ii++, awsModel.getTemperatureCurrent());
					prestmt.setInt(ii++, awsModel.getGroundTemperatureCurrent());
					prestmt.setInt(ii++, awsModel.getHumidityCurrent());
					prestmt.setInt(ii++, awsModel.getRadiationCurrent());
					prestmt.setInt(ii++, awsModel.getSolarPanels());
					prestmt.setInt(ii++, awsModel.getInstrument_TemperatureState());
					prestmt.setInt(ii++, awsModel.getTemperatureState());
					prestmt.setInt(ii++, awsModel.getGroundTemperature_State());
					prestmt.setInt(ii++, awsModel.getHumidity_State());
					//+ "V_Radiationmb,V_Probe,V_Chassis,V_PyrC,V_RefCav,V_DirCav,V_ScaCav,V_NetPyrCav,V_UltCavAB,V_UltCavA,"
					prestmt.setInt(ii++, awsModel.getRadiationMotherboard());
					prestmt.setInt(ii++, awsModel.getProbe());
					prestmt.setInt(ii++, awsModel.getChassis());
					prestmt.setInt(ii++, awsModel.getPyranometerCavity());
					prestmt.setInt(ii++, awsModel.getReflectionCavity());
					prestmt.setInt(ii++, awsModel.getDirectCavity());
					prestmt.setInt(ii++, awsModel.getScatteringCavity());
					prestmt.setInt(ii++, awsModel.getNetPyranometerCavity());
					prestmt.setInt(ii++, awsModel.getUltravioletCavityAB());
					prestmt.setInt(ii++, awsModel.getUltravioletCavityA());
					//+ "V_UltCavB,V_PARCav,V_LWaveCav,V_GWaveCav,V_Thermostat,V_PyrTher,V_RefTherm,V_DirectTher,V_ScaTher,V_NetPyrTher,"
					prestmt.setInt(ii++, awsModel.getUltravioletCavityB());
					prestmt.setInt(ii++, awsModel.getPARMeterCavity());
					prestmt.setInt(ii++, awsModel.getLongWaveCavity());
					prestmt.setInt(ii++, awsModel.getGroundWaveCavity());
					prestmt.setInt(ii++, awsModel.getThermostat());
					prestmt.setInt(ii++, awsModel.getPyranometerThermostat());	
					prestmt.setInt(ii++, awsModel.getReflectionThermostat());
					prestmt.setInt(ii++, awsModel.getDirectThermostat());
					prestmt.setInt(ii++, awsModel.getScatteringThermostat());
					prestmt.setInt(ii++, awsModel.getNetPyranometerThermostat());
					//+ "V_UltTherAB,V_UltTherA,V_UltTherB,V_PARTher,V_LWaveTher,V_GWaveTher,V_ChassisTem,V_EquHea,V_TranHeat,V_ReceHeat,"
					prestmt.setInt(ii++, awsModel.getUltravioletThermostatAB());
					prestmt.setInt(ii++, awsModel.getUltravioletThermostatA());
					prestmt.setInt(ii++, awsModel.getUltravioletThermostatB());
					prestmt.setInt(ii++, awsModel.getPARMeterThermostat());
					prestmt.setInt(ii++, awsModel.getLongWaveThermostat());
					prestmt.setInt(ii++, awsModel.getGroundWaveThermostat());
					prestmt.setInt(ii++, awsModel.getChassisTemperature_State());
					prestmt.setInt(ii++, awsModel.getEquipmentHeating());
					prestmt.setInt(ii++, awsModel.getTransmitterHeating());
					prestmt.setInt(ii++, awsModel.getReceiverHeating());
					//+ "V_CameHeat,V_VideoCaHeat,V_Instrument,V_Transmitter,V_Receiver,V_Hood,V_HoodSpeed,V_PyrSta,V_Reflection,V_Direct,"
					prestmt.setInt(ii++, awsModel.getCameraHeating());
					prestmt.setInt(ii++, awsModel.getVideoCameraHeating());
					prestmt.setInt(ii++, awsModel.getInstrument_State());
					prestmt.setInt(ii++, awsModel.getTransmitter());
					prestmt.setInt(ii++, awsModel.getReceiver());
					prestmt.setInt(ii++, awsModel.getHood());
					prestmt.setInt(ii++, awsModel.getHoodSpeed());
					prestmt.setInt(ii++, awsModel.getPyranometer_AirState());
					prestmt.setInt(ii++, awsModel.getReflection_AirState());
					prestmt.setInt(ii++, awsModel.getDirect_AirState());
					//+ "V_Scattering,V_NetPyrmeter,V_UltravAB,V_UltraA,V_UltraB,V_PARMeter,V_LongWave,V_GroundWave,V_Connection,V_Bus,"
					prestmt.setInt(ii++, awsModel.getScattering_AirState());
					prestmt.setInt(ii++, awsModel.getNetPyranometer_State());
					prestmt.setInt(ii++, awsModel.getUltravioletAB_AirState());
					prestmt.setInt(ii++, awsModel.getUltravioletA_AirState());
					prestmt.setInt(ii++, awsModel.getUltravioletAB_AirState());
					prestmt.setInt(ii++, awsModel.getPARMeter_AirState());
					prestmt.setInt(ii++, awsModel.getLongWave_AirState());
					prestmt.setInt(ii++, awsModel.getGroundWave_AirState());
					prestmt.setInt(ii++, awsModel.getConnection());
					prestmt.setInt(ii++, awsModel.getBus());
					//+ "V_RS232-485-422,V_TemRS232-485-422,V_GroundTemRS232-485-422,V_HumiRS232-485-422,V_RadiRS232-485-422,V_RJ45-LAN,V_Satellite,V_Wireless,V_OpticalFiber,V_Window,"
					prestmt.setInt(ii++, awsModel.getRS232_485_422());
					prestmt.setInt(ii++, awsModel.getTemperatureRS232_485_422());
					prestmt.setInt(ii++, awsModel.getGroundTemperatureRS232_485_422());
					prestmt.setInt(ii++, awsModel.getHumidityRS232_485_422());
					prestmt.setInt(ii++, awsModel.getRadiationRS232_485_422());
					prestmt.setInt(ii++, awsModel.getRJ45_LAN());
					prestmt.setInt(ii++, awsModel.getSatellite());
					prestmt.setInt(ii++, awsModel.getWireless());
					prestmt.setInt(ii++, awsModel.getOpticalFiber());
					prestmt.setInt(ii++, awsModel.getWindow());
					//	+ "V_Detector,V_CameraLens,V_VideoCaLens,V_TransPower,V_Rece,V_Trans,V_Visor,V_TiltHead,V_VideoCamera,V_Camera,"
					prestmt.setInt(ii++, awsModel.getDetector());
					prestmt.setInt(ii++, awsModel.getCameraLens());
					prestmt.setInt(ii++, awsModel.getVideoCameraLens());
					prestmt.setInt(ii++, awsModel.getTransmitterPower());
					prestmt.setInt(ii++, awsModel.getReceiver_State());
					prestmt.setInt(ii++, awsModel.getTransmitter_State());
					prestmt.setInt(ii++, awsModel.getVisor());
					prestmt.setInt(ii++, awsModel.getTiltHead());
					prestmt.setInt(ii++, awsModel.getVideoCamera());
					prestmt.setInt(ii++, awsModel.getCamera());
					//+ "V_Tracker,V_Collector,V_Tem,V_GroundTemCol,V_Humidity,V_Radiation,V_AD,V_TemAD,V_GroundTemAD,V_HumidityAD,"
					prestmt.setInt(ii++, awsModel.getTracker_State());
					prestmt.setInt(ii++, awsModel.getCollector());
					prestmt.setInt(ii++, awsModel.getTemperature_State());
					prestmt.setInt(ii++, awsModel.getGroundTemperature_WorkState());
					prestmt.setInt(ii++, awsModel.getHumidity_WorkState());
					prestmt.setInt(ii++, awsModel.getRadiation_WorkState());
					prestmt.setInt(ii++, awsModel.getAD());
					prestmt.setInt(ii++, awsModel.getTemperatureAD());
					prestmt.setInt(ii++, awsModel.getGroundTemperatureAD());
					prestmt.setInt(ii++, awsModel.getHumidityAD());
					//	+ "V_RadiationAD,V_Count,V_TemCount,V_GroundTemCount,V_HumiCount,V_RadiCount,V_Gate,V_TemGate,V_GroundTemGate,V_HumidityGate,"
					prestmt.setInt(ii++, awsModel.getRadiationAD());
					prestmt.setInt(ii++, awsModel.getCounter());
					prestmt.setInt(ii++, awsModel.getTemperatureCounter());
					prestmt.setInt(ii++, awsModel.getGroundTemperatureCounter());
					prestmt.setInt(ii++, awsModel.getHumidityCounter());
					prestmt.setInt(ii++, awsModel.getRadiationCounter());
					prestmt.setInt(ii++, awsModel.getGate_State());
					prestmt.setInt(ii++, awsModel.getTemperatureGate());
					prestmt.setInt(ii++, awsModel.getGroundTemperatureGate());
					prestmt.setInt(ii++, awsModel.getHumidityGate());
					//+ "V_RadGate,V_Water,V_Displacement,V_WaterLevel,V_WeiSensor,V_EvapPonds,V_ExMemCard,V_PartsSp,V_PartsVibnFre,V_PosiAid,"
					prestmt.setInt(ii++, awsModel.getRadiationGate());
					prestmt.setInt(ii++, awsModel.getWater());
					prestmt.setInt(ii++, awsModel.getDisplacement());
					prestmt.setInt(ii++, awsModel.getWaterLevel());
					prestmt.setInt(ii++, awsModel.getWeighingSensor());
					prestmt.setInt(ii++, awsModel.getEvaporationPonds());
					prestmt.setInt(ii++, awsModel.getExternalMemoryCard());
					prestmt.setInt(ii++, awsModel.getPartsSpeed());
					prestmt.setInt(ii++, awsModel.getPartsVibrationFrequency());
					prestmt.setInt(ii++, awsModel.getPositioningAid());
					//+ "V_CalibTimeEqu,V_InstSta,V_InstNameStatus,V_InstPath,V_Observer,V_StartTime,V_EndTime,V_OperCont,V_StaName,V_InstNameMaint,"
					prestmt.setInt(ii++, awsModel.getCalibrationTimeEquipment());
					prestmt.setString(ii++, awsModel.getInstrumentStatus());
					prestmt.setString(ii++, awsModel.getInstrumentName());
					prestmt.setString(ii++, awsModel.getInstrumentPath());
					prestmt.setString(ii++, awsModel.getObserver());
					prestmt.setString(ii++, awsModel.getStartTime());
					prestmt.setString(ii++, awsModel.getEndTime());
					prestmt.setString(ii++, awsModel.getOperationContent());
					prestmt.setString(ii++, awsModel.getStationName());
					prestmt.setString(ii++, awsModel.getInstrumentName_Maintain());
					//+ "V_Downtime,V_FaultPhen,V_FaultType,V_FaultCause,V_Maintenance,V_MaintMan,V_MaintStaTime,V_MaintEndTime)";
					prestmt.setString(ii++, awsModel.getDowntime());
					prestmt.setString(ii++, awsModel.getFaultPhenomenon());
					prestmt.setString(ii++, awsModel.getFaultType());
					prestmt.setString(ii++, awsModel.getFaultCause());
					prestmt.setString(ii++, awsModel.getMaintenance());
					prestmt.setString(ii++, awsModel.getMaintenanceMan());
					prestmt.setString(ii++, awsModel.getMaintenanceStartTime());
					prestmt.setString(ii++, awsModel.getMaintenanceEndtTime());
					prestmt.setString(ii++, cts_code);
					
					prestmt.addBatch();
					sqls.add(((LoggableStatement)prestmt).getQueryString());
				}// end for
				
				try{
					prestmt.executeBatch();
					connection.commit();
					sqls.clear();
				}catch(SQLException e){
					prestmt.clearParameters();
					prestmt.clearBatch();
					connection.rollback();
					execute_sql(sqls, connection, fileN); // 此中，入库失败的会将对应的DI的ProcessState置为1 // 1:失败，0：成功
					infoLogger.error("\n Batch commit failed: "+fileN);
				}
			}catch (SQLException e) {
				infoLogger.error("\n Create Statement failed: "+e.getMessage());
			}finally {
				if(prestmt != null) {
					try {
						prestmt.close();
					} catch (SQLException e) {
						infoLogger.error("\n Close Statement failed: "+e.getMessage());
					}
				}
				try {
					if(connection != null)
						connection.close();
				} catch (SQLException e1) {
					infoLogger.error("\n Close connection failed: "+e1.getMessage());
				}
			}
		} 
		else{
			infoLogger.error("\n Close connection failed!");
		}
	}
	
	/**
	 * 
	 * @Title: execute_sql   
	 * @Description: TODO(批量入库失败时，采用逐条提交)   
	 * @param  sqls 待执行的查询语句集合
	 * @param connection    数据库连接
	 * @return: void      
	 */
	private static void execute_sql(List<String> sqls, java.sql.Connection connection,String filepath) {
		Statement pStatement = null;
		try {
			connection.setAutoCommit(true);
			pStatement = connection.createStatement();
			for (int i = 0; i < sqls.size(); i++) {
//				pStatement = new LoggableStatement(connection, sqls.get(i));
				
				try {
					pStatement.execute(sqls.get(i));
				} catch (Exception e) {
					infoLogger.error("\n file name："+filepath
							+"\n " + listDi.get(i).getIIiii() + " " + listDi.get(i).getDATA_TIME()
							+"\n execute sql error："+sqls.get(i)+"\n "+e.getMessage());
					listDi.get(i).setPROCESS_STATE("0");
				}
			}
		} catch (SQLException e) {
			infoLogger.error("\n Create Statement failed: "+e.getMessage());
		}finally {
			if(pStatement != null) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					infoLogger.error("\n Close Statement failed: "+e.getMessage());
				}
			}
		}		
		
	}
}
