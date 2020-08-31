package cma.cimiss2.dpc.decoder.surf;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.surf.AWS;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.ElementValUtil;
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
	Main class of decode the surface device state data<br>
	地面设备信息状态资料解码主类
 *
 * <p>
 * notes:
  * 解码程序分为两个步骤:数据读取及预处理、实体对象构建及赋值。
 * <ul>
 * <li>数据读取及预处理过程：
 * <ol>
 * <li>无特殊说明时，所有包含”/”的数据全部转化成999999。
 * <li>未通过数据校验时，该条数据将会被当成错误报文处理。
 * </ol>
 * </li>
 * <li>实体对象构建及赋值：
 * <ol>
 * <li>校验所有段的数据的数据类型是否合法。
 * <li>所有的赋值失败都会导致解码错误，该条数据将被当成错误报文处理。
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cimiss2.dpc.decoder.bean.surf.AWS}。
 * </ol>
 * </li>
 * </ul>
 * <strong> sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br>
 * <root>
  <StateAndInformationOfStation>
    <StationInformation>
      <StationID>57625</StationID>
      <Latitudes>283211</Latitudes>
      <Longitudes>1072730</Longitudes>
      <DateTime>20181006230821</DateTime>
    </StationInformation>
    <StateValue>
      <Communication>0</Communication>
      <Pressure>0</Pressure>
      <Temperature>1</Temperature>
      <WetbulbTemperature>0</WetbulbTemperature>
      <Humidity>0</Humidity>
      <WindDirection>0</WindDirection>
      <WindSpeed>0</WindSpeed>
      <Rainfall>1</Rainfall>
      <Rain>1</Rain><br>
      <GrassTemperature>0</GrassTemperature><br>
      <GroundTemperature>0</GroundTemperature><br>
      <GroundTemperature-5cm>0</GroundTemperature-5cm>
      <GroundTemperature-10cm>0</GroundTemperature-10cm>
      <GroundTemperature-15cm>0</GroundTemperature-15cm> 
      ...
      <br>
      
 * <strong>code:</strong><br>
 * SAXReader reader = new SAXReader();<br>
 * Document doc = reader.read("D:\\AWS\\Z_SURF_I_57625_20181006150800_R_AWS_FTM.XML");<br>
 * String string = doc.asXML();<br>
 * DecodeAWS decodeAWS = new DecodeAWS();<br>
 * ParseResult<AWS> aParseResult = decodeAWS.parseFile("Z_SURF_I_57625_20181006150800_R_AWS_FTM.XML", string);<br>
 * System.out.println("台站：" + aParseResult.getData().get(0).getStationNumberChina());<br>
 * <strong>output:</strong><br>
 * Z_SURF_I_57625_20181006150800_R_AWS_FTM.XML<br>
 * 台站：57625<br>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年10月10日 下午2:06:33   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class DecodeAWS {
	/**
	 * 解码结果
	 */
	private ParseResult<AWS> parseResult = new ParseResult<AWS>(false); 
	/**
	 * 地面设备状态报文解码方法，封装成ParseResult  
	 * @param file 待解码的文件
	 * @param fileContent 报文段
	 * @return: ParseResult 解码结果集<AWS>  
	 */
	public ParseResult<AWS> parseFile(String filename, String fileContent){
		System.out.println(filename);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Element StateAndInformationOfStation;
		Element StationInformation;
		String StationID = "";
		String Latitudes = "";
		String Longitudes = "";
		String DateTime = "";
		Element StateValue;
		Element Selftest;
		Element Sensor;
		Element Power;
		Element Temperature;
		Element HeatingElement;
		Element VentilationComponents;
		Element Communication;
		Element WindowContamination;
		Element WorkStatus;
		Element StatusInformaiton;
		Element MaintenanceInformaiton;
		
		try{
			// 字符串转XML
			Document doc = 	DocumentHelper.parseText(fileContent);
			Element root = doc.getRootElement();
			if(root == null || !root.getName().equals("root")){
				ReportError re = new ReportError();
				re.setMessage("Format error: report does not have root node! "+filename);
				parseResult.put(re);
				return parseResult;
			}
			StateAndInformationOfStation = root.element("StateAndInformationOfStation");
			if(StateAndInformationOfStation != null){
				StationInformation = StateAndInformationOfStation.element("StationInformation");
				AWS aws = new AWS(); 
				if(StationInformation != null){
					try{
						StationID = StationInformation.elementText("StationID").trim();
						Latitudes = StationInformation.elementText("Latitudes").trim();
						Longitudes = StationInformation.elementText("Longitudes").trim();
						DateTime = StationInformation.elementText("DateTime").trim();
						Date dt = new Date();
						dt = simpleDateFormat.parse(DateTime);
						if(!TimeCheckUtil.checkTime(dt)){
							ReportError re = new ReportError();
							re.setMessage("DataTime out of range：time:"+dt+" stationCode:"+StationID);
							re.setSegment(StationInformation.asXML());
							parseResult.put(re);
							return parseResult;
						}
						aws.setObservationTime(dt);
						aws.setStationNumberChina(StationID);
						aws.setLatitude(ElementValUtil.getlatitude(Latitudes));
						aws.setLongitude(ElementValUtil.getLongitude(Longitudes));
					}catch (Exception e) {
						ReportError re = new ReportError();
						re.setMessage("Format error: StationInformation parse error! ");
						re.setSegment(StationInformation.asXML());
						parseResult.put(re);
						return parseResult;
					}
				}
				else{
					ReportError re = new ReportError();
					re.setMessage("Format error: StationInformation Node is null! ");
					parseResult.put(re);
					return parseResult;
				}
				StateValue = StateAndInformationOfStation.element("StateValue");
				if(StateValue != null){
					aws.setCommunication(getIntValue(StateValue.elementText("Communication")));
					aws.setPressure(getIntValue(StateValue.elementText("Pressure")));
					aws.setTemperature(getIntValue(StateValue.elementText("Temperature")));
					aws.setWetbulbTemperature(getIntValue(StateValue.elementText("WetbulbTemperature")));
					aws.setHumidity(getIntValue(StateValue.elementText("Humidity")));
					aws.setWindDirection(getIntValue(StateValue.elementText("WindDirection")));
					aws.setWindSpeed(getIntValue(StateValue.elementText("WindSpeed")));
					aws.setRainfall(getIntValue(StateValue.elementText("Rainfall")));
					aws.setRain(getIntValue(StateValue.elementText("Rain")));
					aws.setGrassTemperature(getIntValue(StateValue.elementText("GrassTemperature")));
					aws.setGroundTemperature(getIntValue(StateValue.elementText("GroundTemperature")));
					aws.setGroundTemperature5cm(getIntValue(StateValue.elementText("GroundTemperature-5cm")));
					aws.setGroundTemperature10cm(getIntValue(StateValue.elementText("GroundTemperature-10cm")));
					aws.setGroundTemperature15cm(getIntValue(StateValue.elementText("GroundTemperature-15cm")));
					aws.setGroundTemperature20cm(getIntValue(StateValue.elementText("GroundTemperature-20cm")));
					aws.setGroundTemperature40cm(getIntValue(StateValue.elementText("GroundTemperature-40cm")));
					aws.setGroundTemperature80cm(getIntValue(StateValue.elementText("GroundTemperature-80cm")));
					aws.setGroundTemperature160cm(getIntValue(StateValue.elementText("GroundTemperature-160cm")));
					aws.setGroundTemperature320cm(getIntValue(StateValue.elementText("GroundTemperature-320cm")));
					aws.setEvaporation(getIntValue(StateValue.elementText("Evaporation")));
					aws.setSunshine(getIntValue(StateValue.elementText("Sunshine")));
					aws.setVisibility(getIntValue(StateValue.elementText("Visibility")));
					aws.setCloudAmount(getIntValue(StateValue.elementText("CloudAmount")));
					aws.setCloudHeight(getIntValue(StateValue.elementText("CloudHeight")));
					aws.setSubStationClock(getIntValue(StateValue.elementText("Sub-StationClock")));
					aws.setDataCollection(getIntValue(StateValue.elementText("DataCollection")));
					aws.setPowerSupply(getIntValue(StateValue.elementText("PowerSupply")));
					aws.setMotherboardVoltage(getFloatValue(StateValue.elementText("MotherboardVoltage")));
					aws.setMotherboardTemperature(getFloatValue(StateValue.elementText("MotherboardTemperature")));
					aws.setCollectorCommunication(getIntValue(StateValue.elementText("CollectorCommunication")));
					aws.setChassisTemperature(getFloatValue(StateValue.elementText("ChassisTemperature")));
					aws.setPowerVoltage(getFloatValue(StateValue.elementText("PowerVoltage")));
					aws.setSolar(getIntValue(StateValue.elementText("PowerVoltage")));
					aws.setSolarVentilation(getIntValue(StateValue.elementText("SolarVentilation")));
					aws.setTracker(getIntValue(StateValue.elementText("Tracker")));
					aws.setScattering(getIntValue(StateValue.elementText("Scattering")));
					aws.setScatteringVentilation(getIntValue(StateValue.elementText("ScatteringVentilation")));
					aws.setPyranometer(getIntValue(StateValue.elementText("Pyranometer")));
					aws.setPyranometerVentilation(getIntValue(StateValue.elementText("PyranometerVentilation")));
					aws.setReflection(getIntValue(StateValue.elementText("Reflection")));
					aws.setReflectionVentilation(getIntValue(StateValue.elementText("ReflectionVentilation")));
					aws.setLongWave(getIntValue(StateValue.elementText("LongWave")));
					aws.setLongWaveVentilation(getIntValue(StateValue.elementText("LongWaveVentilation")));
					aws.setLongWaveTemperature(getIntValue(StateValue.elementText("LongWaveTemperature")));
					aws.setEarthWave(getIntValue(StateValue.elementText("EarthWave")));
					aws.setEarthWaveVentilation(getIntValue(StateValue.elementText("EarthWaveVentilation")));
					aws.setEarthWaveTemperature(getIntValue(StateValue.elementText("EarthWaveTemperature")));
					aws.setUltraviolet(getIntValue(StateValue.elementText("Ultraviole")));
					aws.setUltravioletTemperature(getIntValue(StateValue.elementText("UltravioletTemperature")));
					aws.setPARMeter(getIntValue(StateValue.elementText("PARMeter")));
				}
				else{
					ReportError re = new ReportError();
					re.setMessage("Format error: StateValue Node is null! ");
					parseResult.put(re);
				}
				Selftest = StateAndInformationOfStation.element("Self-test");
				if(Selftest != null){
					aws.setInstrument(getIntValue(Selftest.elementText("Instrument")));
					aws.setClimate(getIntValue(Selftest.elementText("Climate")));
					aws.setGroundTemperature_SelfCheck(getIntValue(Selftest.elementText("GroundTemperature")));
					aws.setHumidity_SelfCheck(getIntValue(Selftest.elementText("Humidity")));
					aws.setRadiation(getIntValue(Selftest.elementText("RadiationSub-Collection")));
				}
				else{
					ReportError re = new ReportError();
					re.setMessage("Format error: Selftest Node is null! ");
					parseResult.put(re);
				}
				Sensor = StateAndInformationOfStation.element("Sensor");
				if(Sensor != null){
					aws.setSensorWorking(getIntValue(Sensor.elementText("SensorWorking")));
					aws.setTemperature15dm(getIntValue(Sensor.elementText("Temperature-1.5m")));
					aws.setGrassSurface(getIntValue(Sensor.elementText("GrassSurface")));
					aws.setSurface(getIntValue(Sensor.elementText("Surface")));
					aws.setGround5cm(getIntValue(Sensor.elementText("Ground-5cm")));
					aws.setGround10cm(getIntValue(Sensor.elementText("Ground-10cm")));
					aws.setGround15cm(getIntValue(Sensor.elementText("Ground-15cm")));
					aws.setGround20cm(getIntValue(Sensor.elementText("Ground-20cm")));
					aws.setGround40cm(getIntValue(Sensor.elementText("Ground-40cm")));
					aws.setGround80cm(getIntValue(Sensor.elementText("Ground-80cm")));
					aws.setGround160cm(getIntValue(Sensor.elementText("Ground-160cm")));
					aws.setGround320cm(getIntValue(Sensor.elementText("Ground-320cm")));
					aws.setSurfaceTemperature(getIntValue(Sensor.elementText("SurfaceTemperature")));
					aws.setFreezingTemperature(getIntValue(Sensor.elementText("FreezingTemperature")));
					aws.setRelativeHumidity15dm(getIntValue(Sensor.elementText("RelativeHumidity-1.5m")));
					aws.setWindDirection_State(getIntValue(Sensor.elementText("WindDirection")));
					aws.setWindSpeed_State(getIntValue(Sensor.elementText("WindSpeed")));
					aws.setPressure_State(getIntValue(Sensor.elementText("Pressure")));
					aws.setNoWeighingRaingauge(getIntValue(Sensor.elementText("NoWeighingRaingauge")));
					aws.setWeighingRaingauge(getIntValue(Sensor.elementText("WeighingRaingauge")));
					aws.setEvaporation_State(getIntValue(Sensor.elementText("Evaporation")));
					aws.setPyranometer_State(getIntValue(Sensor.elementText("Pyranometer")));
					aws.setReflection_State(getIntValue(Sensor.elementText("Reflection")));
					aws.setDirect(getIntValue(Sensor.elementText("Direct")));
					aws.setScattering_State(getIntValue(Sensor.elementText("Scattering")));
					aws.setNetPyranometer(getIntValue(Sensor.elementText("NetPyranometer")));
					aws.setUltravioletAB(getIntValue(Sensor.elementText("Ultraviole-AB")));
					aws.setUltravioletA(getIntValue(Sensor.elementText("Ultraviole-A")));
					aws.setUltravioletB(getIntValue(Sensor.elementText("Ultraviole-B")));
					aws.setPARMeter_State(getIntValue(Sensor.elementText("PARMeter")));
					aws.setLongWave_State(getIntValue(Sensor.elementText("LongWave")));
					aws.setGroundWave(getIntValue(Sensor.elementText("GroundWave")));
					aws.setSunshine_State(getIntValue(Sensor.elementText("Sunshine")));
					aws.setCloudHeight_State(getIntValue(Sensor.elementText("CloudHeight")));
					aws.setCloudAmount_State(getIntValue(Sensor.elementText("CloudAmount")));
					aws.setCloudForm(getIntValue(Sensor.elementText("CloudForm")));
					aws.setVisibilityInstrument(getIntValue(Sensor.elementText("VisibilityInstrument")));
					aws.setPhenomenonInstrument(getIntValue(Sensor.elementText("PhenomenonInstrument")));
					aws.setAntennaIcing(getIntValue(Sensor.elementText("AntennaIcing")));
					aws.setRoadCondition(getIntValue(Sensor.elementText("RoadCondition")));
				}
				else{
					ReportError re = new ReportError();
					re.setMessage("Format error: Sensor Node is null! ");
					parseResult.put(re);
				}
				
				Power = StateAndInformationOfStation.element("Power");
				if(Power != null){
					aws.setExternalPower(getIntValue(Power.elementText("ExternalPower")));
					aws.setClimate_State(getIntValue(Power.elementText("Climate")));
					aws.setGroundTemperature_PowerState(getIntValue(Power.elementText("GroundTemperature")));
					aws.setHumidity_PowerState(getIntValue(Power.elementText("Humidity")));
					aws.setRadiation_PowerState(getIntValue(Power.elementText("Radiation")));
					aws.setInstrumentVoltage(getIntValue(Power.elementText("InstrumentVoltage")));
					aws.setClimateVoltage(getIntValue(Power.elementText("ClimateVoltage")));
					aws.setGroundTemperatureVoltage(getIntValue(Power.elementText("GroundTemperatureVoltage")));
					aws.setHumidityVoltage(getIntValue(Power.elementText("HumidityVoltage")));
					aws.setRadiationVoltage(getIntValue(Power.elementText("RadiationVoltage")));
					aws.setImageVoltage(getIntValue(Power.elementText("ImageVoltage")));
					aws.setBatteryVoltage(getIntValue(Power.elementText("BatteryVoltage")));
					aws.setACDCVoltage(getIntValue(Power.elementText("AC-DCVoltage")));
					aws.setVisorVoltage(getIntValue(Power.elementText("VisorVoltage")));
					aws.setTiltHeadVoltage(getIntValue(Power.elementText("TiltHeadVoltage")));
					aws.setInstrumentCurrent(getIntValue(Power.elementText("InstrumentCurrent")));
					aws.setTemperatureCurrent(getIntValue(Power.elementText("TemperatureCurrent")));
					aws.setGroundTemperatureCurrent(getIntValue(Power.elementText("GroundTemperatureCurrent")));
					aws.setHumidityCurrent(getIntValue(Power.elementText("HumidityCurrent")));
					aws.setRadiationCurrent(getIntValue(Power.elementText("RadiationCurrent")));
					aws.setSolarPanels(getIntValue(Power.elementText("SolarPanels")));
				}
				else{
					ReportError re = new ReportError();
					re.setMessage("Format error: Power Node is null! ");
					parseResult.put(re);
				}
				Temperature = StateAndInformationOfStation.element("Temperature");
				if(Temperature != null){
					aws.setInstrument_TemperatureState(getIntValue(Temperature.elementText("Instrument")));
					aws.setTemperatureState(getIntValue(Temperature.elementText("Temperature")));
					aws.setGroundTemperature_State(getIntValue(Temperature.elementText("GroundTemperature")));
					aws.setHumidity_State(getIntValue(Temperature.elementText("Humidity")));
					aws.setRadiationMotherboard(getIntValue(Temperature.elementText("RadiationSub-CollectionMotherboard")));
					aws.setProbe(getIntValue(Temperature.elementText("Probe")));
					aws.setChassis(getIntValue(Temperature.elementText("Chassis")));
					aws.setPyranometerCavity(getIntValue(Temperature.elementText("PyranometerCavity")));
					aws.setReflectionCavity(getIntValue(Temperature.elementText("ReflectionCavity")));
					aws.setDirectCavity(getIntValue(Temperature.elementText("DirectCavity")));
					aws.setScatteringCavity(getIntValue(Temperature.elementText("ScatteringCavity")));
					aws.setNetPyranometerCavity(getIntValue(Temperature.elementText("NetPyranometerCavity")));
					aws.setUltravioletCavityAB(getIntValue(Temperature.elementText("UltravioleCavity-AB")));
					aws.setUltravioletCavityA(getIntValue(Temperature.elementText("UltravioleCavity-A")));
					aws.setUltravioletCavityB(getIntValue(Temperature.elementText("UltravioleCavity-B")));
					aws.setPARMeterCavity(getIntValue(Temperature.elementText("PARMeterCavity")));
					aws.setLongWaveCavity(getIntValue(Temperature.elementText("LongWaveCavity")));
					aws.setGroundWaveCavity(getIntValue(Temperature.elementText("GroundWaveCavity")));
					aws.setThermostat(getIntValue(Temperature.elementText("Thermostat")));
					aws.setPyranometerThermostat(getIntValue(Temperature.elementText("PyranometerThermostat")));;
					aws.setReflectionThermostat(getIntValue(Temperature.elementText("ReflectionThermostat")));
					aws.setDirectThermostat(getIntValue(Temperature.elementText("DirectThermostat")));
					aws.setScatteringThermostat(getIntValue(Temperature.elementText("ScatteringThermostat")));
					aws.setNetPyranometerThermostat(getIntValue(Temperature.elementText("NetPyranometerThermostat")));
					aws.setUltravioletThermostatAB(getIntValue(Temperature.elementText("UltravioleThermostat-AB")));
					aws.setUltravioletThermostatA(getIntValue(Temperature.elementText("UltravioleThermostat-A")));
					aws.setUltravioletThermostatB(getIntValue(Temperature.elementText("UltravioleThermostat-B")));
					aws.setPARMeterThermostat(getIntValue(Temperature.elementText("PARMeterThermostat")));
					aws.setLongWaveThermostat(getIntValue(Temperature.elementText("LongWaveThermostat")));
					aws.setGroundWaveThermostat(getIntValue(Temperature.elementText("GroundWaveThermostat")));
					aws.setChassisTemperature_State(getIntValue(Temperature.elementText("ChassisTemperature")));
				}
				else{
					ReportError re = new ReportError();
					re.setMessage("Format error: Temperature Node is null! ");
					parseResult.put(re);
				}
				HeatingElement = StateAndInformationOfStation.element("HeatingElement");
				if(HeatingElement != null){
					aws.setEquipmentHeating(getIntValue(HeatingElement.elementText("EquipmentHeating")));
					aws.setTransmitterHeating(getIntValue(HeatingElement.elementText("TransmitterHeating")));
					aws.setReceiverHeating(getIntValue(HeatingElement.elementText("ReceiverHeating")));
					aws.setCameraHeating(getIntValue(HeatingElement.elementText("CameraHeating")));
					aws.setVideoCameraHeating(getIntValue(HeatingElement.elementText("VideoCameraHeating")));
				}
				else{
					ReportError re = new ReportError();
					re.setMessage("Format error: HeatingElement Node is null! ");
					parseResult.put(re);
				}
				VentilationComponents = StateAndInformationOfStation.element("VentilationComponents");
				if(VentilationComponents != null){
					aws.setInstrument_State(getIntValue(VentilationComponents.elementText("Instrument")));
					aws.setTransmitter(getIntValue(VentilationComponents.elementText("Transmitter")));
					aws.setReceiver(getIntValue(VentilationComponents.elementText("Receiver")));
					aws.setHood(getIntValue(VentilationComponents.elementText("Hood")));
					aws.setHoodSpeed(getIntValue(VentilationComponents.elementText("HoodSpeed")));
					aws.setPyranometer_AirState(getIntValue(VentilationComponents.elementText("Pyranometer")));
					aws.setReflection_AirState(getIntValue(VentilationComponents.elementText("Reflection")));
					aws.setDirect_AirState(getIntValue(VentilationComponents.elementText("Direct")));
					aws.setScattering_AirState(getIntValue(VentilationComponents.elementText("Scattering")));
					aws.setNetPyranometer_State(getIntValue(VentilationComponents.elementText("NetPyranometer")));
					aws.setUltravioletAB_AirState(getIntValue(VentilationComponents.elementText("Ultraviole-AB")));
					aws.setUltravioletA_AirState(getIntValue(VentilationComponents.elementText("Ultraviole-A")));
					aws.setUltravioletB_AirState(getIntValue(VentilationComponents.elementText("Ultraviole-B")));
					aws.setPARMeter_AirState(getIntValue(VentilationComponents.elementText("PARMeter")));
					aws.setLongWave_AirState(getIntValue(VentilationComponents.elementText("LongWave")));
					aws.setGroundWave_AirState(getIntValue(VentilationComponents.elementText("GroundWave")));
				}
				else{
					ReportError re = new ReportError();
					re.setMessage("Format error: VentilationComponents Node is null! ");
					parseResult.put(re);
				}
				
				Communication = StateAndInformationOfStation.element("Communication");
				if(Communication != null){
					aws.setConnection(getIntValue(Communication.elementText("Connection")));
					aws.setBus(getIntValue(Communication.elementText("Bus")));
					aws.setRS232_485_422(getIntValue(Communication.elementText("RS232-485-422")));
					aws.setTemperatureRS232_485_422(getIntValue(Communication.elementText("TemperatureRS232-485-422")));
					aws.setGroundTemperatureRS232_485_422(getIntValue(Communication.elementText("GroundRS232-485-422")));
					aws.setHumidityRS232_485_422(getIntValue(Communication.elementText("HumidityRS232-485-422")));
					aws.setRadiationRS232_485_422(getIntValue(Communication.elementText("RadiationRS232-485-422")));
					aws.setRJ45_LAN(getIntValue(Communication.elementText("RJ45-LAN")));
					aws.setSatellite(getIntValue(Communication.elementText("Satellite")));
					aws.setWireless(getIntValue(Communication.elementText("Wireless")));
					aws.setOpticalFiber(getIntValue(Communication.elementText("OpticalFiber")));
				}
				else{
					ReportError re = new ReportError();
					re.setMessage("Format error: Communication Node is null! ");
					parseResult.put(re);
				}
				
				WindowContamination = StateAndInformationOfStation.element("WindowContamination");
				if(WindowContamination != null){
					aws.setWindow(getIntValue(WindowContamination.elementText("Window")));
					aws.setDetector(getIntValue(WindowContamination.elementText("Detector")));
					aws.setCameraLens(getIntValue(WindowContamination.elementText("CameraLens")));
					aws.setVideoCameraLens(getIntValue(WindowContamination.elementText("VideoCameraLens")));
				}
				else{
					ReportError re = new ReportError();
					re.setMessage("Format error: WindowContamination Node is null! ");
					parseResult.put(re);
				}
				
				WorkStatus = StateAndInformationOfStation.element("WorkStatus");
				if(WorkStatus != null){
					aws.setTransmitterPower(getIntValue(WorkStatus.elementText("TransmitterPower")));
					aws.setReceiver_State(getIntValue(WorkStatus.elementText("Receiver")));
					aws.setTransmitter_State(getIntValue(WorkStatus.elementText("Transmitter")));
					aws.setVisor(getIntValue(WorkStatus.elementText("Visor")));
					aws.setTiltHead(getIntValue(WorkStatus.elementText("TiltHead")));
					aws.setVideoCamera(getIntValue(WorkStatus.elementText("VideoCamera")));
					aws.setCamera(getIntValue(WorkStatus.elementText("Camera")));
					aws.setTracker_State(getIntValue(WorkStatus.elementText("Tracker")));
					aws.setCollector(getIntValue(WorkStatus.elementText("Collector")));
					aws.setTemperature_State(getIntValue(WorkStatus.elementText("Temperature")));
					aws.setGroundTemperature_WorkState(getIntValue(WorkStatus.elementText("GroundTemperature")));
					aws.setHumidity_WorkState(getIntValue(WorkStatus.elementText("Humidity")));
					aws.setRadiation_WorkState(getIntValue(WorkStatus.elementText("RadiationCollector")));
					aws.setAD(getIntValue(WorkStatus.elementText("AD")));
					aws.setTemperatureAD(getIntValue(WorkStatus.elementText("TemperatureAD")));
					aws.setGroundTemperatureAD(getIntValue(WorkStatus.elementText("GroundTemperatureAD")));
					aws.setHumidityAD(getIntValue(WorkStatus.elementText("HumidityAD")));
					aws.setRadiationAD(getIntValue(WorkStatus.elementText("RadiationAD")));
					aws.setCounter(getIntValue(WorkStatus.elementText("Counter")));
					aws.setTemperatureCounter(getIntValue(WorkStatus.elementText("TemperatureCounter")));
					aws.setGroundTemperatureCounter(getIntValue(WorkStatus.elementText("GroundTemperatureCounter")));
					aws.setHumidityCounter(getIntValue(WorkStatus.elementText("HumidityCounter")));
					aws.setRadiationCounter(getIntValue(WorkStatus.elementText("RadiationCounter")));
					aws.setGate_State(getIntValue(WorkStatus.elementText("Gate")));
					aws.setTemperatureGate(getIntValue(WorkStatus.elementText("TemperatureGate")));
					aws.setGroundTemperatureGate(getIntValue(WorkStatus.elementText("GroundTemperatureGate")));
					aws.setHumidityGate(getIntValue(WorkStatus.elementText("HumidityGate")));
					aws.setRadiationGate(getIntValue(WorkStatus.elementText("RadiationGate")));
					aws.setWater(getIntValue(WorkStatus.elementText("Water")));
					aws.setDisplacement(getIntValue(WorkStatus.elementText("Displacement")));
					aws.setWaterLevel(getIntValue(WorkStatus.elementText("WaterLevel")));
					aws.setWeighingSensor(getIntValue(WorkStatus.elementText("WeighingSensor")));
					aws.setEvaporationPonds(getIntValue(WorkStatus.elementText("EvaporationPonds")));
					aws.setExternalMemoryCard(getIntValue(WorkStatus.elementText("ExternalMemoryCard")));
					aws.setPartsSpeed(getIntValue(WorkStatus.elementText("PartsSpeed")));
					aws.setPartsVibrationFrequency(getIntValue(WorkStatus.elementText("PartsVibrationFrequency")));
					aws.setPositioningAid(getIntValue(WorkStatus.elementText("PositioningAid")));
					aws.setCalibrationTimeEquipment(getIntValue(WorkStatus.elementText("CalibrationTimeEquipment")));
				}
				else{
					ReportError re = new ReportError();
					re.setMessage("Format error: WorkStatus Node is null! ");
					parseResult.put(re);
				}
				
				StatusInformaiton = StateAndInformationOfStation.element("StatusInformaiton");
				if(StatusInformaiton != null){
					aws.setInstrumentStatus(getStr(StatusInformaiton.elementText("InstrumentStatus")));
					aws.setInstrumentName(getStr(StatusInformaiton.elementText("InstrumentName")));
					aws.setInstrumentPath(getStr(StatusInformaiton.elementText("InstrumentPath")));
					aws.setObserver(getStr(StatusInformaiton.elementText("Observer")));
					aws.setStartTime(getStr(StatusInformaiton.elementText("StartTime")));
					aws.setEndTime(getStr(StatusInformaiton.elementText("EndTime")));
					aws.setOperationContent(getStr(StatusInformaiton.elementText("OperationContent")));
				}
				else{
					ReportError re = new ReportError();
					re.setMessage("Format error: StatusInformaiton Node is null! ");
					parseResult.put(re);
				}
				
				MaintenanceInformaiton = StateAndInformationOfStation.element("MaintenanceInformaiton");
				if(MaintenanceInformaiton != null){
					aws.setStationName(getStr(MaintenanceInformaiton.elementText("StationName")));
					aws.setInstrumentName_Maintain(getStr(MaintenanceInformaiton.elementText("InstrumentName")));
					aws.setDowntime(getStr(MaintenanceInformaiton.elementText("Downtime")));
					aws.setFaultPhenomenon(getStr(MaintenanceInformaiton.elementText("FaultPhenomenon")));
					aws.setFaultType(getStr(MaintenanceInformaiton.elementText("FaultType")));
					aws.setFaultCause(getStr(MaintenanceInformaiton.elementText("FaultCause")));
					aws.setMaintenance(getStr(MaintenanceInformaiton.elementText("Maintenance")));
					aws.setMaintenanceMan(getStr(MaintenanceInformaiton.elementText("MaintenanceMan")));
					aws.setMaintenanceStartTime(getStr(MaintenanceInformaiton.elementText("MaintenanceStartTime")));
					aws.setMaintenanceEndtTime(getStr(MaintenanceInformaiton.elementText("MaintenanceEndtTime")));
				}
				else{
					ReportError re = new ReportError();
					re.setMessage("Format error: MaintenanceInformaiton Node is null! ");
					parseResult.put(re);
				}
				parseResult.put(aws);
				parseResult.setSuccess(true);
				
//				List<Element> listSIFs = StateAndInformationOfStation.elements("StationInformation");
//				logger.info("\r\n StationInformation size() = " + listSIFs.size());
//				
//				List<Element> SV = StateAndInformationOfStation.elements("StateValue");
//				logger.info("\r\n StateValue size() = " + SV.size());
//				
//				List<Element> ST = StateAndInformationOfStation.elements("Selftest");
//				logger.info("\r\n Selftest size() = " + ST.size());
//				
//				List<Element> S = StateAndInformationOfStation.elements("Sensor");
//				logger.info("\r\n Sensor size() = " + S.size());
//				
//				List<Element> P = StateAndInformationOfStation.elements("Power");
//				logger.info("\r\n Power size() = " + P.size());
//				
//				List<Element> Te = StateAndInformationOfStation.elements("Temperature");
//				logger.info("\r\n Temperature size() = " + Te.size());
//				
//				List<Element> T = StateAndInformationOfStation.elements("HeatingElement");
//				logger.info("\r\n HeatingElement size() = " + T.size());
//				
//				List<Element> V = StateAndInformationOfStation.elements("VentilationComponents");
//				logger.info("\r\n VentilationComponents size() = " + V.size());
//				
//				List<Element> C = StateAndInformationOfStation.elements("Communication");
//				logger.info("\r\n Communication size() = " + C.size());
//				
//				List<Element> WC = StateAndInformationOfStation.elements("WindowContamination");
//				logger.info("\r\n WindowContamination size() = " + WC.size());
//				
//				List<Element> WS= StateAndInformationOfStation.elements("WorkStatus");
//				logger.info("\r\n WorkStatus size() = " + WS.size());
//				
//				List<Element> SI= StateAndInformationOfStation.elements("StatusInformaiton");
//				logger.info("\r\n StatusInformaiton size() = " + SI.size());
//				
//				List<Element> MI = StateAndInformationOfStation.elements("MaintenanceInformaiton");
//				logger.info("\r\n MaintenanceInformaiton size() = " + MI.size());
//				
			}
			else{
				ReportError re = new ReportError();
				re.setMessage("Format error: StateAndInformationOfStation Node is null! ");
				parseResult.put(re);
				return parseResult;
			}
			
		}catch (DocumentException e) {
			ReportError re = new ReportError();
			re.setMessage("CONVERT FILE CONTENT TO XML FAILED: " + e.getMessage());
			parseResult.put(re);
		} catch(Exception e2){
			ReportError re = new ReportError();
			re.setMessage("XML FILE FORMAT ERROR: ");
			parseResult.put(re);
		} 
	    return parseResult;
	}   
	/**
	 * 
	 * @Title: getIntValue   
	 * @Description: TODO(解析整数类型要素)   
	 * @param str
	 * @return int      
	 */
	static int getIntValue(String str){
		if(str == null || (str = str.trim()).equals(""))
			return 999999;
		else{
			try{
				return Integer.parseInt(str);
			}catch (Exception e) {
				return 999999;
			}
		}
	}
	/**
	 * 
	 * @Title: getFloatValue   
	 * @Description: TODO(解析浮点类型要素)   
	 * @param str
	 * @return double      
	 */
	static double getFloatValue(String str){
		if(str == null || (str = str.trim()).equals(""))
			return 999999;
		else{
			try{
				return Double.parseDouble(str);
			}catch (Exception e) {
				return 999999;
			}
		}
	}
	/**
	 * 
	 * @Title: getStr   
	 * @Description: TODO(解析字符串类型要素)   
	 * @param str
	 * @return String      
	 */
	static String getStr(String str){
		if(str == null || (str = str.trim()).equals(""))
			return "999999";
		else{
			return str.trim();
		}
	}
	public static void main(String[] args) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document doc = reader.read("D:\\AWS\\Z_SURF_I_57625_20181006150800_R_AWS_FTM.XML");
		String string = doc.asXML();
		DecodeAWS decodeAWS = new DecodeAWS();
		ParseResult<AWS> aParseResult = decodeAWS.parseFile("Z_SURF_I_57625_20181006150800_R_AWS_FTM.XML", string);
		System.out.println("台站：" + aParseResult.getData().get(0).getStationNumberChina());
	}
}
