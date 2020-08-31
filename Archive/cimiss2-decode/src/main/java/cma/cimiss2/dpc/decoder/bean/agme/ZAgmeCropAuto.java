package cma.cimiss2.dpc.decoder.bean.agme;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
	农作物自动观测数据 要素实体类。解码时，各要素值从报文中直接取值；入库时，各要素解码结果直接赋值给数据库字段
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《农作物自动观测设备状态数据要素》 </a>
 *      <li> <a href=" "> 《农作物自动观测数据要素》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年9月6日 下午3:59:13   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class ZAgmeCropAuto implements Cloneable{
	
	/** 缺测替代值. */
	public static int defaultD = 999999;
	
	private long imageTime = 999999;
	// 1. 台站基本信息数据段要素字典
	/**
	 * NO: 1.1 <br>
	 * nameCN: 区站号 <br>
	 * unit: <br>
	 * BUFR FXY: V01301 <br>
	 * descriptionCN: 
	 */
	private String stationNumberChina;
	/**
	 * NO: 1.2  <br>
	 * nameCN: 测定时间 <br>
	 * unit: 日期 <br>
	 * BUFR FXY: V04001/V04002/V04003//V04004 <br>
	 * descriptionCN: 年月日时分秒<br>
	 * decode rule: 直接取值 <br>
	 * field rule: 使用java时间转化工具类进行转化, 标准形式为：yyyyMMddHHmmss <br>
	 */
	private Date ObservationTime ;
	
	/**
	 * NO: 1.3  <br>
	 * nameCN: 经度 <br>
	 * unit: 度  <br>
	 * BUFR FXY: V06001 <br>
	 * descriptionCN:  
	 **/
	private double longitude =999999.0;
	
	/**
	 * NO: 1.4  <br>
	 * nameCN: 纬度 <br>
	 * unit: 度  <br>
	 * BUFR FXY: V05001 <br>
	 * descriptionCN: 
	 */
	private double latitude =999999.0;
	/**
	 * NO: 1.5  <br>
	 * nameCN: 观测场拔海高度 <br>
	 * unit: m <br>
	 * BUFR FXY: V07030 <br>
	 * descriptionCN: 若低于海平面，为负值
	 */
	private double heightOfStaionAboveSeaLevel = defaultD;
	
	/**
	 * NO: 1.6  <br>
	 * nameCN: 气压传感器拔海高度 <br>
	 * unit: m <br>
	 * BUFR FXY: V07031 <br>
	 * descriptionCN: 
	 */
	private double heightOfPressureSensor = defaultD;

	/**
	 * NO: 1.7  <br>
	 * nameCN: 质量控制标识  <br>
	 * unit: m <br>
	 * BUFR FXY: V33256 <br>
	 * descriptionCN: 
	 */
	private int qualityControl = 9;
	
	/**
	 * NO: 1.8  <br>
	 * nameCN: 更正报标识 <br>
	 * unit:  <br>
	 * BUFR FXY: V35024 <br>
	 * descriptionCN: 
	 */
	private String correctMarker;
	
	// 2. 作物数据段要素字典
	/**
	 * NO: 2.1  <br>
	 * nameCN: 农气图像传感器距地高度  <br>
	 * unit: m <br>
	 * BUFR FXY: V07256 <br>
	 * descriptionCN: 
	 */
	private double imageSensorHeightAboveGround  = defaultD;
	
	/**
	 * NO: 2.2  <br>
	 * nameCN: 农气图像传感器焦距  <br>
	 * unit: mm <br>
	 * BUFR FXY: V02271 <br>
	 * descriptionCN: 
	 */
	private int imageSensorFocus  = defaultD;
	
	/**
	 * NO: 2.3  <br>
	 * nameCN: 作物观测面积  <br>
	 * unit: m2 <br>
	 * BUFR FXY: V48002 <br>
	 * descriptionCN: 
	 */
	private double obvArea = defaultD;
	
	/**
	 * NO: 2.4  <br>
	 * nameCN: 观测方式  <br>
	 * unit:  <br>
	 * BUFR FXY: V02256 <br>
	 * descriptionCN: 
	 */
	private int obvMethod = defaultD;
	
	/**
	 * NO: 2.5  <br>
	 * nameCN: 图像传感器标识  <br>
	 * unit:  <br>
	 * BUFR FXY: V02259 <br>
	 * descriptionCN: 
	 */
	private String imageSensorID ="999999";
	
	/**
	 * NO: 2.6  <br>
	 * nameCN: 图像传感器连接状态  <br>
	 * unit:  <br>
	 * BUFR FXY: V02270 <br>
	 * descriptionCN: 
	 */
	private int imageSensorConnectionStatus = defaultD;
	
	/**
	 * NO: 2.7  <br>
	 * nameCN: GPS授时模块有无标识  <br>
	 * unit:  <br>
	 * BUFR FXY: V02257 <br>
	 * descriptionCN: 
	 */
	private int GPSTimingMarker = defaultD;
	
	/**
	 * NO: 2.8  <br>
	 * nameCN: CF外存储设备有无标识 <br>
	 * unit:  <br>
	 * BUFR FXY: V02258 <br>
	 * descriptionCN: 
	 */
	private int CFPeripheralStorageMarker = defaultD;
	
	/**
	 * NO: 2.9  <br>
	 * nameCN: 农气测报软件版本号 <br>
	 * unit:  <br>
	 * BUFR FXY: V02260 <br>
	 * descriptionCN: 
	 */
	private String agmeReportSoftwareVersion="999999";
	
	// 3. 作物数据段要素字典
	/**
	 * NO: 3.1  <br>
	 * nameCN: 采集器运行状态  <br>
	 * unit:  <br>
	 * BUFR FXY: V02261 <br>
	 * descriptionCN: 
	 */
	private int colllectorRunningState = defaultD;
	
	/**
	 * NO: 3.2  <br>
	 * nameCN: 采集器电源电压  <br>
	 * unit: 伏  <br>
	 * BUFR FXY: V02262 <br>
	 * descriptionCN: 
	 */
	private double collectorVoltage = defaultD;
	
	/**
	 * NO: 3.3  <br>
	 * nameCN: 采集器供电类型  <br>
	 * unit:   <br>
	 * BUFR FXY: V02263 <br>
	 * descriptionCN: 
	 */
	private int collectorPowerSupplyType = defaultD;
	
	/**
	 * NO: 3.4  <br>
	 * nameCN: 采集器主板温度  <br>
	 * unit:  ℃  <br>
	 * BUFR FXY: V02264 <br>
	 * descriptionCN: 
	 */
	private double collectorMainboardTemperature = defaultD;

	/**
	 * NO: 3.5  <br>
	 * nameCN: 采集器CF卡状态  <br>
	 * unit:   <br>
	 * BUFR FXY: V02265 <br>
	 * descriptionCN: 
	 */
	private int collectorCFstate = defaultD;
	
	/**
	 * NO: 3.6  <br>
	 * nameCN: 采集器CF剩余空间  <br>
	 * unit:  MB <br>
	 * BUFR FXY: V02266 <br>
	 * descriptionCN: 
	 */
	private int collectorCFRemainSpace = defaultD;
	
	/**
	 * NO: 3.7  <br>
	 * nameCN: 采集器GPS模块工作状态 <br>
	 * unit:   <br>
	 * BUFR FXY: V02267 <br>
	 * descriptionCN: 
	 */
	private int collectorGPSWorkingstate = defaultD;
	
	/**
	 * NO: 3.8  <br>
	 * nameCN: 采集器门开关状态 <br>
	 * unit:   <br>
	 * BUFR FXY: V02268 <br>
	 * descriptionCN: 
	 */
	private int collectorGateswitchState = defaultD;
	
	/**
	 * NO: 3.9  <br>
	 * nameCN: 采集器LAN终端通信状态 <br>
	 * unit:   <br>
	 * BUFR FXY: V02269 <br>
	 * descriptionCN: 
	 */
	private int collectorLANterminalCommunicationState = defaultD;
	
   //4. crop11
	/**
	 * NO: 4.1  <br>
	 * nameCN: 作物名称编码  <br>
	 * unit:   <br>
	 * BUFR FXY: V48000 <br>
	 * descriptionCN: 
	 */
	private int cropName = defaultD;
	/**
	 * NO: 4.2  <br>
	 * nameCN:作物发育期  <br>
	 * unit:   <br>
	 * BUFR FXY: V48001 <br>
	 * descriptionCN: 
	 */
	private int cropGrowthPeriod = defaultD;
	
	/**
	 * NO: 4.3  <br>
	 * nameCN: 作物发育期质量控制码  <br>
	 * unit:   <br>
	 * BUFR FXY: Q48001 <br>
	 * descriptionCN: 
	 */
	private int cropGrowthPeriodQC = 9;
		
	/**
	 * NO: 4.4  <br>
	 * nameCN: 发育开始时间  <br>
	 * unit:   <br>
	 * BUFR FXY: V26256 <br>
	 * descriptionCN: 
	 */
	private String growthStarttime="999999";
	/**
	 * NO: 4.5  <br>
	 * nameCN: 发育开始时间质量控制码	 <br>
	 * unit:   <br>
	 * BUFR FXY: Q26256 <br>
	 * descriptionCN: 
	 */
	private int growthStarttimeQC = 9;
		
	/**
	 * NO: 4.6  <br>
	 * nameCN: 发育持续时间 <br>
	 * unit: 天  <br>
	 * BUFR FXY: V26257 <br>
	 * descriptionCN: 
	 */
	private int growthDuration = defaultD;
		
	/**
	 * NO: 4.7  <br>
	 * nameCN: 发育持续时间质量控制码 <br>
	 * unit:   <br>
	 * BUFR FXY: Q26257 <br>
	 * descriptionCN: 
	 */
	private int growthDurationQC = 9;
	
	/**	
	 * NO: 4.8  <br>
	 * nameCN: 发育期百分率 <br>
	 * unit: %  <br>
	 * BUFR FXY: V48009 <br>
	 * descriptionCN: 
	 */
	private int growthPeriodPercent = defaultD;
		
	/**	
	 * NO: 4.9  <br>
	 * nameCN: 发育期百分率质量控制码 <br>
	 * unit:   <br>
	 * BUFR FXY: Q48009 <br>
	 * descriptionCN: 
	 */
	private int growthPeriodPercentQC = 9;
		
	/**	
	 * NO: 4.10  <br>
	 * nameCN:植被覆盖度 <br>
	 * unit: %  <br>
	 * BUFR FXY: V48008 <br>
	 * descriptionCN: 
	 */
	private int plantCoverage = defaultD;
		
	/**	
	 * NO: 4.11  <br>
	 * nameCN: 植被覆盖度质量控制码  <br>
	 * unit:   <br>
	 * BUFR FXY: Q48008 <br>
	 * descriptionCN: 
	 */
	private int plantCoverageQC = 9;
		
	/**	
	 * NO: 4.12  <br>
	 * nameCN: 叶面积指数  <br>
	 * unit:   <br>
	 * BUFR FXY: V48010 <br>
	 * descriptionCN: 
	 */
	private double leafAreaIndex = defaultD;
	
	/**	
	 * NO: 4.13  <br>
	 * nameCN: 叶面积指数质量控制码  <br>
	 * unit:   <br>
	 * BUFR FXY: Q48010 <br>
	 * descriptionCN: 
	 */
	private int leafAreaIndexQC = 9;
		
	/**	
	 * NO: 4.14  <br>
	 * nameCN: 冠层高度  <br>
	 * unit: 厘米  <br>
	 * BUFR FXY: V48034 <br>
	 * descriptionCN: 
	 */
	private int canopyHeight = defaultD;
	
	/**	
	 * NO: 4.15  <br>
	 * nameCN: 冠层高度质量控制码  <br>
	 * unit:   <br>
	 * BUFR FXY: Q48034 <br>
	 * descriptionCN: 
	 */	
	private int canopyHeightQC = 9;
	
	/**	
	 * NO: 4.16  <br>
	 * nameCN: 植株密度  <br>
	 * unit:  株/平方米 <br>
	 * BUFR FXY: V48007 <br>
	 * descriptionCN: 
	 */	
	private double plantDensity = defaultD;
	
	/**	
	 * NO: 4.17  <br>
	 * nameCN: 植株密度质量控制码  <br>
	 * unit:    <br>
	 * BUFR FXY: Q48007 <br>
	 * descriptionCN: 
	 */	
	private int plantDensityQC = 9;
	
	/**	
	 * NO: 4.18  <br>
	 * nameCN: 干重  <br>
	 * unit:  克/平方米  <br>
	 * BUFR FXY: V48301 <br>
	 * descriptionCN: 
	 */	
	private double dryWeight = defaultD;
	
	/**	
	 * NO: 4.19  <br>
	 * nameCN: 干重质量控制面码  <br>
	 * unit:    <br>
	 * BUFR FXY: Q48301 <br>
	 * descriptionCN: 
	 */	
	private int dryWeightQC = 9;
	
	/**	
	 * NO: 4.20  <br>
	 * nameCN: 生长状况  <br>
	 * unit:     <br>
	 * BUFR FXY: V48006 <br>
	 * descriptionCN: 
	 */	
	private int growthState = defaultD;
	
	/**	
	 * NO: 4.21  <br>
	 * nameCN: 生长状况质量控制码  <br>
	 * unit:     <br>
	 * BUFR FXY: Q48006 <br>
	 * descriptionCN: 
	 */	
	private int growthStateQC = 9;
	
	/**	
	 * NO: 4.22  <br>
	 * nameCN: 灾害名称  <br>
	 * unit:     <br>
	 * BUFR FXY: V51000 <br>
	 * descriptionCN: 
	 */
	private int disaName = defaultD;
	
	/**	
	 * NO: 4.23  <br>
	 * nameCN: 受灾等级  <br>
	 * unit:     <br>
	 * BUFR FXY: V51009 <br>
	 * descriptionCN: 
	 */
	private int disaLevel = defaultD;
	
	/**	
	 * NO: 4.24  <br>
	 * nameCN: 受灾等级质量控制码  <br>
	 * unit:     <br>
	 * BUFR FXY: Q51002 <br>
	 * descriptionCN: 
	 */
	private int disaLevelQC = 9;
	
	/**	
	 * NO: 4.25  <br>
	 * nameCN: 灾害开始时间  <br>
	 * unit:     <br>
	 * BUFR FXY: V26269 <br>
	 * descriptionCN: 
	 */
	private String disaStarttime="999999";
	
	/**	
	 * NO: 4.26  <br>
	 * nameCN: 灾害开始时间质控码  <br>
	 * unit:     <br>
	 * BUFR FXY: Q26269 <br>
	 * descriptionCN: 
	 */
	private int disaStarttimeQC = 9;
		
	/**	
	 * NO: 4.27  <br>
	 * nameCN: 灾害持续时间  <br>
	 * unit:     <br>
	 * BUFR FXY: V26270 <br>
	 * descriptionCN: 
	 */
	private int disaDuration = defaultD;
		
	/**	
	 * NO: 4.28  <br>
	 * nameCN: 灾害持续时间质量控制码 <br>
	 * unit:     <br>
	 * BUFR FXY: Q26270 <br>
	 * descriptionCN: 
	 */
	private int disaDurationQC = 9;
	
	/**	
	 * NO: 4.29  <br>
	 * nameCN: 作物图像格式 <br>
	 * unit:     <br>
	 * BUFR FXY: V30256 <br>
	 * descriptionCN: 
	 */	
	private String cropImageFormat="999999";
	
	/**	
	 * NO: 4.30  <br>
	 * nameCN: 作物图像 <br>
	 * unit:     <br>
	 * BUFR FXY: V48033 <br>
	 * descriptionCN: 
	 */
	private byte[] cropImage;
	
	/**
	 * Gets the station number china.
	 *
	 * @return the station number china
	 */
	public String getStationNumberChina() {
		return stationNumberChina;
	}

	/**
	 * Sets the station number china.
	 *
	 * @param stationNumberChina the new station number china
	 */
	public void setStationNumberChina(String stationNumberChina) {
		this.stationNumberChina = stationNumberChina;
	}

	/**
	 * Gets the latitude.
	 *
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Sets the latitude.
	 *
	 * @param latitude the new latitude
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * Gets the longitude.
	 *
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Sets the longitude.
	 *
	 * @param longitude the new longitude
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * Gets the height of pressure sensor.
	 *
	 * @return the height of pressure sensor
	 */
	public double getHeightOfPressureSensor() {
		return heightOfPressureSensor;
	}

	/**
	 * Sets the height of pressure sensor.
	 *
	 * @param heightOfPressureSensor the new height of pressure sensor
	 */
	public void setHeightOfPressureSensor(double heightOfPressureSensor) {
		this.heightOfPressureSensor = heightOfPressureSensor;
	}

	/**
	 * Gets the quality control.
	 *
	 * @return the quality control
	 */
	public int getQualityControl() {
		return qualityControl;
	}

	/**
	 * Sets the quality control.
	 *
	 * @param qualityControl the new quality control
	 */
	public void setQualityControl(int qualityControl) {
		this.qualityControl = qualityControl;
	}

	/**
	 * Gets the image sensor height above ground.
	 *
	 * @return the image sensor height above ground
	 */
	public double getImageSensorHeightAboveGround() {
		return imageSensorHeightAboveGround;
	}

	/**
	 * Sets the image sensor height above ground.
	 *
	 * @param imageSensorHeightAboveGround the new image sensor height above ground
	 */
	public void setImageSensorHeightAboveGround(double imageSensorHeightAboveGround) {
		this.imageSensorHeightAboveGround = imageSensorHeightAboveGround;
	}

	/**
	 * Gets the image sensor focus.
	 *
	 * @return the image sensor focus
	 */
	public int getImageSensorFocus() {
		return imageSensorFocus;
	}

	/**
	 * Sets the image sensor focus.
	 *
	 * @param imageSensorFocus the new image sensor focus
	 */
	public void setImageSensorFocus(int imageSensorFocus) {
		this.imageSensorFocus = imageSensorFocus;
	}

	/**
	 * Gets the obv area.
	 *
	 * @return the obv area
	 */
	public double getObvArea() {
		return obvArea;
	}

	/**
	 * Sets the obv area.
	 *
	 * @param obvArea the new obv area
	 */
	public void setObvArea(double obvArea) {
		this.obvArea = obvArea;
	}

	/**
	 * Gets the obv method.
	 *
	 * @return the obv method
	 */
	public int getObvMethod() {
		return obvMethod;
	}

	/**
	 * Sets the obv method.
	 *
	 * @param obvMethod the new obv method
	 */
	public void setObvMethod(int obvMethod) {
		this.obvMethod = obvMethod;
	}

	/**
	 * Gets the image sensor ID.
	 *
	 * @return the image sensor ID
	 */
	public String getImageSensorID() {
		return imageSensorID;
	}

	/**
	 * Sets the image sensor ID.
	 *
	 * @param imageSensorID the new image sensor ID
	 */
	public void setImageSensorID(String imageSensorID) {
		this.imageSensorID = imageSensorID;
	}

	/**
	 * Gets the image sensor connection status.
	 *
	 * @return the image sensor connection status
	 */
	public int getImageSensorConnectionStatus() {
		return imageSensorConnectionStatus;
	}

	/**
	 * Sets the image sensor connection status.
	 *
	 * @param imageSensorConnectionStatus the new image sensor connection status
	 */
	public void setImageSensorConnectionStatus(int imageSensorConnectionStatus) {
		this.imageSensorConnectionStatus = imageSensorConnectionStatus;
	}

	/**
	 * Gets the GPS timing marker.
	 *
	 * @return the GPS timing marker
	 */
	public int getGPSTimingMarker() {
		return GPSTimingMarker;
	}

	/**
	 * Sets the GPS timing marker.
	 *
	 * @param gPSTimingMarker the new GPS timing marker
	 */
	public void setGPSTimingMarker(int gPSTimingMarker) {
		GPSTimingMarker = gPSTimingMarker;
	}

	/**
	 * Gets the CF peripheral storage marker.
	 *
	 * @return the CF peripheral storage marker
	 */
	public int getCFPeripheralStorageMarker() {
		return CFPeripheralStorageMarker;
	}

	/**
	 * Sets the CF peripheral storage marker.
	 *
	 * @param cFPeripheralStorageMarker the new CF peripheral storage marker
	 */
	public void setCFPeripheralStorageMarker(int cFPeripheralStorageMarker) {
		CFPeripheralStorageMarker = cFPeripheralStorageMarker;
	}

	/**
	 * Gets the agme report software version.
	 *
	 * @return the agme report software version
	 */
	public String getAgmeReportSoftwareVersion() {
		return agmeReportSoftwareVersion;
	}

	/**
	 * Sets the agme report software version.
	 *
	 * @param agmeReportSoftwareVersion the new agme report software version
	 */
	public void setAgmeReportSoftwareVersion(String agmeReportSoftwareVersion) {
		this.agmeReportSoftwareVersion = agmeReportSoftwareVersion;
	}

	/**
	 * Gets the colllector running state.
	 *
	 * @return the colllector running state
	 */
	public int getColllectorRunningState() {
		return colllectorRunningState;
	}

	/**
	 * Sets the colllector running state.
	 *
	 * @param colllectorRunningState the new colllector running state
	 */
	public void setColllectorRunningState(int colllectorRunningState) {
		this.colllectorRunningState = colllectorRunningState;
	}

	/**
	 * Gets the collector voltage.
	 *
	 * @return the collector voltage
	 */
	public double getCollectorVoltage() {
		return collectorVoltage;
	}

	/**
	 * Sets the collector voltage.
	 *
	 * @param collectorVoltage the new collector voltage
	 */
	public void setCollectorVoltage(double collectorVoltage) {
		this.collectorVoltage = collectorVoltage;
	}

	/**
	 * Gets the collector power supply type.
	 *
	 * @return the collector power supply type
	 */
	public int getCollectorPowerSupplyType() {
		return collectorPowerSupplyType;
	}

	/**
	 * Sets the collector power supply type.
	 *
	 * @param collectorPowerSupplyType the new collector power supply type
	 */
	public void setCollectorPowerSupplyType(int collectorPowerSupplyType) {
		this.collectorPowerSupplyType = collectorPowerSupplyType;
	}

	/**
	 * Gets the collector mainboard temperature.
	 *
	 * @return the collector mainboard temperature
	 */
	public double getCollectorMainboardTemperature() {
		return collectorMainboardTemperature;
	}

	/**
	 * Sets the collector mainboard temperature.
	 *
	 * @param collectorMainboardTemperature the new collector mainboard temperature
	 */
	public void setCollectorMainboardTemperature(double collectorMainboardTemperature) {
		this.collectorMainboardTemperature = collectorMainboardTemperature;
	}

	/**
	 * Gets the collector C fstate.
	 *
	 * @return the collector C fstate
	 */
	public int getCollectorCFstate() {
		return collectorCFstate;
	}

	/**
	 * Sets the collector C fstate.
	 *
	 * @param collectorCFstate the new collector C fstate
	 */
	public void setCollectorCFstate(int collectorCFstate) {
		this.collectorCFstate = collectorCFstate;
	}

	/**
	 * Gets the collector CF remain space.
	 *
	 * @return the collector CF remain space
	 */
	public int getCollectorCFRemainSpace() {
		return collectorCFRemainSpace;
	}

	/**
	 * Sets the collector CF remain space.
	 *
	 * @param collectorCFRemainSpace the new collector CF remain space
	 */
	public void setCollectorCFRemainSpace(int collectorCFRemainSpace) {
		this.collectorCFRemainSpace = collectorCFRemainSpace;
	}

	/**
	 * Gets the collector GPS workingstate.
	 *
	 * @return the collector GPS workingstate
	 */
	public int getCollectorGPSWorkingstate() {
		return collectorGPSWorkingstate;
	}

	/**
	 * Sets the collector GPS workingstate.
	 *
	 * @param collectorGPSWorkingstate the new collector GPS workingstate
	 */
	public void setCollectorGPSWorkingstate(int collectorGPSWorkingstate) {
		this.collectorGPSWorkingstate = collectorGPSWorkingstate;
	}

	/**
	 * Gets the collector gateswitch state.
	 *
	 * @return the collector gateswitch state
	 */
	public int getCollectorGateswitchState() {
		return collectorGateswitchState;
	}

	/**
	 * Sets the collector gateswitch state.
	 *
	 * @param collectorGateswitchState the new collector gateswitch state
	 */
	public void setCollectorGateswitchState(int collectorGateswitchState) {
		this.collectorGateswitchState = collectorGateswitchState;
	}

	/**
	 * Gets the collector LA nterminal communication state.
	 *
	 * @return the collector LA nterminal communication state
	 */
	public int getCollectorLANterminalCommunicationState() {
		return collectorLANterminalCommunicationState;
	}

	/**
	 * Sets the collector LA nterminal communication state.
	 *
	 * @param collectorLANterminalCommunicationState the new collector LA nterminal communication state
	 */
	public void setCollectorLANterminalCommunicationState(int collectorLANterminalCommunicationState) {
		this.collectorLANterminalCommunicationState = collectorLANterminalCommunicationState;
	}

	/**
	 * Gets the crop name.
	 *
	 * @return the crop name
	 */
	public int getCropName() {
		return cropName;
	}

	/**
	 * Sets the crop name.
	 *
	 * @param cropName the new crop name
	 */
	public void setCropName(int cropName) {
		this.cropName = cropName;
	}

	/**
	 * Gets the crop growth period.
	 *
	 * @return the crop growth period
	 */
	public int getCropGrowthPeriod() {
		return cropGrowthPeriod;
	}

	/**
	 * Sets the crop growth period.
	 *
	 * @param cropGrowthPeriod the new crop growth period
	 */
	public void setCropGrowthPeriod(int cropGrowthPeriod) {
		this.cropGrowthPeriod = cropGrowthPeriod;
	}

	/**
	 * Gets the crop growth period QC.
	 *
	 * @return the crop growth period QC
	 */
	public int getCropGrowthPeriodQC() {
		return cropGrowthPeriodQC;
	}

	/**
	 * Sets the crop growth period QC.
	 *
	 * @param cropGrowthPeriodQC the new crop growth period QC
	 */
	public void setCropGrowthPeriodQC(int cropGrowthPeriodQC) {
		this.cropGrowthPeriodQC = cropGrowthPeriodQC;
	}

	/**
	 * Gets the growth starttime.
	 *
	 * @return the growth starttime
	 */
	public String getGrowthStarttime() {
		return growthStarttime;
	}

	/**
	 * Sets the growth starttime.
	 *
	 * @param growthStarttime the new growth starttime
	 */
	public void setGrowthStarttime(String growthStarttime) {
		this.growthStarttime = growthStarttime;
	}

	/**
	 * Gets the growth starttime QC.
	 *
	 * @return the growth starttime QC
	 */
	public int getGrowthStarttimeQC() {
		return growthStarttimeQC;
	}

	/**
	 * Sets the growth starttime QC.
	 *
	 * @param growthStarttimeQC the new growth starttime QC
	 */
	public void setGrowthStarttimeQC(int growthStarttimeQC) {
		this.growthStarttimeQC = growthStarttimeQC;
	}

	/**
	 * Gets the growth duration.
	 *
	 * @return the growth duration
	 */
	public int getGrowthDuration() {
		return growthDuration;
	}

	/**
	 * Sets the growth duration.
	 *
	 * @param growthDuration the new growth duration
	 */
	public void setGrowthDuration(int growthDuration) {
		this.growthDuration = growthDuration;
	}

	/**
	 * Gets the growth duration QC.
	 *
	 * @return the growth duration QC
	 */
	public int getGrowthDurationQC() {
		return growthDurationQC;
	}

	/**
	 * Sets the growth duration QC.
	 *
	 * @param growthDurationQC the new growth duration QC
	 */
	public void setGrowthDurationQC(int growthDurationQC) {
		this.growthDurationQC = growthDurationQC;
	}

	/**
	 * Gets the growth period percent.
	 *
	 * @return the growth period percent
	 */
	public int getGrowthPeriodPercent() {
		return growthPeriodPercent;
	}

	/**
	 * Sets the growth period percent.
	 *
	 * @param growthPeriodPercent the new growth period percent
	 */
	public void setGrowthPeriodPercent(int growthPeriodPercent) {
		this.growthPeriodPercent = growthPeriodPercent;
	}

	/**
	 * Gets the growth period percent QC.
	 *
	 * @return the growth period percent QC
	 */
	public int getGrowthPeriodPercentQC() {
		return growthPeriodPercentQC;
	}

	/**
	 * Sets the growth period percent QC.
	 *
	 * @param growthPeriodPercentQC the new growth period percent QC
	 */
	public void setGrowthPeriodPercentQC(int growthPeriodPercentQC) {
		this.growthPeriodPercentQC = growthPeriodPercentQC;
	}

	/**
	 * Gets the plant coverage.
	 *
	 * @return the plant coverage
	 */
	public int getPlantCoverage() {
		return plantCoverage;
	}

	/**
	 * Sets the plant coverage.
	 *
	 * @param plantCoverage the new plant coverage
	 */
	public void setPlantCoverage(int plantCoverage) {
		this.plantCoverage = plantCoverage;
	}

	/**
	 * Gets the plant coverage QC.
	 *
	 * @return the plant coverage QC
	 */
	public int getPlantCoverageQC() {
		return plantCoverageQC;
	}

	/**
	 * Sets the plant coverage QC.
	 *
	 * @param plantCoverageQC the new plant coverage QC
	 */
	public void setPlantCoverageQC(int plantCoverageQC) {
		this.plantCoverageQC = plantCoverageQC;
	}

	/**
	 * Gets the leaf area index.
	 *
	 * @return the leaf area index
	 */
	public double getLeafAreaIndex() {
		return leafAreaIndex;
	}

	/**
	 * Sets the leaf area index.
	 *
	 * @param leafAreaIndex the new leaf area index
	 */
	public void setLeafAreaIndex(double leafAreaIndex) {
		this.leafAreaIndex = leafAreaIndex;
	}

	/**
	 * Gets the leaf area index QC.
	 *
	 * @return the leaf area index QC
	 */
	public int getLeafAreaIndexQC() {
		return leafAreaIndexQC;
	}

	/**
	 * Sets the leaf area index QC.
	 *
	 * @param leafAreaIndexQC the new leaf area index QC
	 */
	public void setLeafAreaIndexQC(int leafAreaIndexQC) {
		this.leafAreaIndexQC = leafAreaIndexQC;
	}

	/**
	 * Gets the canopy height.
	 *
	 * @return the canopy height
	 */
	public int getCanopyHeight() {
		return canopyHeight;
	}

	/**
	 * Sets the canopy height.
	 *
	 * @param canopyHeight the new canopy height
	 */
	public void setCanopyHeight(int canopyHeight) {
		this.canopyHeight = canopyHeight;
	}

	/**
	 * Gets the canopy height QC.
	 *
	 * @return the canopy height QC
	 */
	public int getCanopyHeightQC() {
		return canopyHeightQC;
	}

	/**
	 * Sets the canopy height QC.
	 *
	 * @param canopyHeightQC the new canopy height QC
	 */
	public void setCanopyHeightQC(int canopyHeightQC) {
		this.canopyHeightQC = canopyHeightQC;
	}

	/**
	 * Gets the plant density.
	 *
	 * @return the plant density
	 */
	public double getPlantDensity() {
		return plantDensity;
	}

	/**
	 * Sets the plant density.
	 *
	 * @param plantDensity the new plant density
	 */
	public void setPlantDensity(double plantDensity) {
		this.plantDensity = plantDensity;
	}

	/**
	 * Gets the plant density QC.
	 *
	 * @return the plant density QC
	 */
	public int getPlantDensityQC() {
		return plantDensityQC;
	}

	/**
	 * Sets the plant density QC.
	 *
	 * @param plantDensityQC the new plant density QC
	 */
	public void setPlantDensityQC(int plantDensityQC) {
		this.plantDensityQC = plantDensityQC;
	}

	/**
	 * Gets the dry weight.
	 *
	 * @return the dry weight
	 */
	public double getDryWeight() {
		return dryWeight;
	}

	/**
	 * Sets the dry weight.
	 *
	 * @param dryWeight the new dry weight
	 */
	public void setDryWeight(double dryWeight) {
		this.dryWeight = dryWeight;
	}

	/**
	 * Gets the dry weight QC.
	 *
	 * @return the dry weight QC
	 */
	public int getDryWeightQC() {
		return dryWeightQC;
	}

	/**
	 * Sets the dry weight QC.
	 *
	 * @param dryWeightQC the new dry weight QC
	 */
	public void setDryWeightQC(int dryWeightQC) {
		this.dryWeightQC = dryWeightQC;
	}

	/**
	 * Gets the growth state.
	 *
	 * @return the growth state
	 */
	public int getGrowthState() {
		return growthState;
	}

	/**
	 * Sets the growth state.
	 *
	 * @param growthState the new growth state
	 */
	public void setGrowthState(int growthState) {
		this.growthState = growthState;
	}

	/**
	 * Gets the growth state QC.
	 *
	 * @return the growth state QC
	 */
	public int getGrowthStateQC() {
		return growthStateQC;
	}

	/**
	 * Sets the growth state QC.
	 *
	 * @param growthStateQC the new growth state QC
	 */
	public void setGrowthStateQC(int growthStateQC) {
		this.growthStateQC = growthStateQC;
	}

	/**
	 * Gets the disa name.
	 *
	 * @return the disa name
	 */
	public int getDisaName() {
		return disaName;
	}

	/**
	 * Sets the disa name.
	 *
	 * @param disaName the new disa name
	 */
	public void setDisaName(int disaName) {
		this.disaName = disaName;
	}

	/**
	 * Gets the disa level.
	 *
	 * @return the disa level
	 */
	public int getDisaLevel() {
		return disaLevel;
	}

	/**
	 * Sets the disa level.
	 *
	 * @param disaLevel the new disa level
	 */
	public void setDisaLevel(int disaLevel) {
		this.disaLevel = disaLevel;
	}

	/**
	 * Gets the disa level QC.
	 *
	 * @return the disa level QC
	 */
	public int getDisaLevelQC() {
		return disaLevelQC;
	}

	/**
	 * Sets the disa level QC.
	 *
	 * @param disaLevelQC the new disa level QC
	 */
	public void setDisaLevelQC(int disaLevelQC) {
		this.disaLevelQC = disaLevelQC;
	}

	/**
	 * Gets the disa starttime.
	 *
	 * @return the disa starttime
	 */
	public String getDisaStarttime() {
		return disaStarttime;
	}

	/**
	 * Sets the disa starttime.
	 *
	 * @param disaStarttime the new disa starttime
	 */
	public void setDisaStarttime(String disaStarttime) {
		this.disaStarttime = disaStarttime;
	}

	/**
	 * Gets the disa starttime QC.
	 *
	 * @return the disa starttime QC
	 */
	public int getDisaStarttimeQC() {
		return disaStarttimeQC;
	}

	/**
	 * Sets the disa starttime QC.
	 *
	 * @param disaStarttimeQC the new disa starttime QC
	 */
	public void setDisaStarttimeQC(int disaStarttimeQC) {
		this.disaStarttimeQC = disaStarttimeQC;
	}

	/**
	 * Gets the disa duration.
	 *
	 * @return the disa duration
	 */
	public int getDisaDuration() {
		return disaDuration;
	}

	/**
	 * Sets the disa duration.
	 *
	 * @param disaDuration the new disa duration
	 */
	public void setDisaDuration(int disaDuration) {
		this.disaDuration = disaDuration;
	}

	/**
	 * Gets the disa duration QC.
	 *
	 * @return the disa duration QC
	 */
	public int getDisaDurationQC() {
		return disaDurationQC;
	}

	/**
	 * Sets the disa duration QC.
	 *
	 * @param disaDurationQC the new disa duration QC
	 */
	public void setDisaDurationQC(int disaDurationQC) {
		this.disaDurationQC = disaDurationQC;
	}

	/**
	 * Gets the crop image format.
	 *
	 * @return the crop image format
	 */
	public String getCropImageFormat() {
		return cropImageFormat;
	}

	/**
	 * Sets the crop image format.
	 *
	 * @param cropImageFormat the new crop image format
	 */
	public void setCropImageFormat(String cropImageFormat) {
		this.cropImageFormat = cropImageFormat;
	}

	/**
	 * Gets the crop image.
	 *
	 * @return the crop image
	 */
	public byte[] getCropImage() {
		return cropImage;
	}

	/**
	 * Sets the crop image.
	 *
	 * @param cropImage the new crop image
	 */
	public void setCropImage(byte[] cropImage) {
		this.cropImage = cropImage;
	}

	/**
	 * Gets the height of staion above sea level.
	 *
	 * @return the height of staion above sea level
	 */
	public double getHeightOfStaionAboveSeaLevel() {
		return heightOfStaionAboveSeaLevel;
	}

	/**
	 * Sets the height of staion above sea level.
	 *
	 * @param heightOfStaionAboveSeaLevel the new height of staion above sea level
	 */
	public void setHeightOfStaionAboveSeaLevel(double heightOfStaionAboveSeaLevel) {
		this.heightOfStaionAboveSeaLevel = heightOfStaionAboveSeaLevel;
	}

	/**
	 * Gets the correct marker.
	 *
	 * @return the correct marker
	 */
	public String getCorrectMarker() {
		return correctMarker;
	}

	/**
	 * Sets the correct marker.
	 *
	 * @param correctMarker the new correct marker
	 */
	public void setCorrectMarker(String correctMarker) {
		this.correctMarker = correctMarker;
	}

	/**
	 * Gets the observation time.
	 *
	 * @return the observation time
	 */
	public Date getObservationTime() {
		return ObservationTime;
	}

	/**
	 * Sets the observation time.
	 *
	 * @param observationTime the new observation time
	 */
	public void setObservationTime(Date observationTime) {
		ObservationTime = observationTime;
	}
	public long getImageTime() {
		return imageTime;
	}

	public void setImageTime(long imageTime) {
		this.imageTime = imageTime;
	}
	
	 @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
