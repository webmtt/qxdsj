package cma.cimiss2.dpc.decoder.bean.upar;

import java.util.Date;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * 飞机高空探测报告资料实体类
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《飞机高空探测报告(UD/UA)数据表》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月27日 下午5:51:56   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class UDUA {
	/**
	 * NO: 1  <br>
	 * nameCN: 报告标识  <br>
	 * unit: <br>
	 * BUFR FXY: TTAAII <br>
	 * descriptionCN: <br>
     * decode rule:直接取值 <br>
     * field rule:直接赋值 
	 */
	private String reportType;
	
	/**
	 * NO: 2  <br>
	 * nameCN: 资料时间  <br>
	 * unit: <br>
	 * BUFR FXY: D_DATETIME <br>
	 * descriptionCN: V04001\V04002\V04003\V04004\V04005\V04006<br>
     * decode rule:直接取值。<br>
     * field rule:使用java时间转化工具类进行转化,标准形式为：yyyyMMddHHmmss
	 */
	private Date dataTime;
	
	/**
	 * NO: 3  <br>
	 * nameCN: 飞行状态和观测类型标识  <br>
	 * unit: 代码表 <br>
	 * BUFR FXY:  V08009 <br>
	 * descriptionCN: IpIpIp  =UNS，取值2；=LVR，取值3；=LVW，取值4；=ASC，取值5；=DES，取值6；
     * decode rule: 直接取值 <br>
     * field rule: 直接赋值  <br>
	 */
	private int planeStateAndObsType=999998;
	
	/**
	 * NO: 4  <br>
	 * nameCN: 飞行标识符  <br>
	 * unit: <br>
	 * BUFR FXY: V01301 <br>
	 * descriptionCN: Ia...Ia，长度小于等于8,字母起首   <br>
     * decode rule: 直接取值 <br>
     * field rule: 直接赋值  <br>
	 */
	private String planeID;
	
	/**
	 * NO: 5  <br>
	 * nameCN: 纬度  <br>
	 * unit: 度 <br>
	 * BUFR FXY: V05001 <br>
	 * descriptionCN: LaLaLaLaA<br>
     * decode rule: 度分转化为度 <br>
     * field rule: 直接赋值  <br>
	 */
	private double latitude = 999999;
	
	/**
	 * NO: 6  <br>
	 * nameCN: 经度  <br>
	 * unit: 度 <br>
	 * BUFR FXY: V06001 <br>
	 * descriptionCN: LoLoLoLoLoB<br>
     * decode rule: 度分转化为度 <br>
     * field rule: 直接赋值  <br>
	 */
	private double longtitude = 999999;
	
	/**
	 * NO: 7  <br>
	 * nameCN: 标准气压高度  <br>
	 * unit: 1m <br>
	 * BUFR FXY: V07002 <br>
	 * descriptionCN: ShH1H1H1, 指飞行器从空中到标准气压平面（1013.2hPa）的垂道距离, ×30.48。<br>
     * decode rule: 取值单位为百英尺，取值 * 30.48<br>
     * field rule: 直接赋值  <br>
	 */
	private double heightOfStandardPressure = 999998;
	
	/**
	 * NO: 8  <br>
	 * nameCN: 温度  <br>
	 * unit: 1度  （TaTaTa时）、1度（TaTa时）<br>
	 * BUFR FXY: V12001 <br>
	 * descriptionCN: 标准气压高度 处的气温, 气温≧0时编码为PS，气温小于0时编码为MS <br>
	 * decode rule: SSTaTaTa 或 SSTaTa, 取值*0.1，或者直接取值<br>
	 * field rule: 直接赋值  <br>
	 */
	private double temperature = 999998;
	
	/**
	 * NO: 9  <br>
	 * nameCN: 露点温度  <br>
	 * unit: 1度  <br>
	 * BUFR FXY: V12003 <br>
	 * descriptionCN: TdTdTd 计算同气温。<br>
	 * decode rule: 计算同气温值  <br>
	 * field rule: 直接赋值  <br>
	 */
	private double dewPoint = 999998;
	
	/**
	 * NO: 10  <br>
	 * nameCN: 相对湿度  <br>
	 * unit: %  <br>
	 * BUFR FXY: V13003 <br>
	 * descriptionCN: UUU, 标准气压高度 处的相对湿度。<br>
	 * decode rule: 取缺测值999999  <br>
	 * field rule: 直接赋值  <br>
	 */
	private double relativehumidity = 999998;
	
	/**
	 * NO: 11  <br>
	 * nameCN: 风向  <br>
	 * unit: 度  <br>
	 * BUFR FXY: V11001 <br>
	 * descriptionCN: ddd, 风向，以度为单位，取值000～360。
	 * decode rule: 直接取值  <br>
	 * field rule: 直接赋值  <br>
	 */
	private int windDir = 999998;
	/**
	 * NO: 12  <br>
	 * nameCN: 风速 <br>
	 * unit: m/s <br>
	 * BUFR FXY: V11002 <br>
	 * descriptionCN: fff, 海里/时，国内编报的单位为 <br>
	 * decode rule: 直接取值，或者取值*0.51 + 0.5  <br>
	 * field rule: 直接赋值  <br>
	 */
	private double windSpeed = 999998;
	/**
	 * NO: 13  <br>
	 * nameCN: 湍流度  <br>
	 * unit: 代码表  <br>
	 * BUFR FXY: V11031 <br>
	 * descriptionCN: TBBa<br>
	 * decode rule: 直接取值 <br>
	 * field rule: 直接赋值  <br>
	 */
	private int turbulence = 999998;
	
	/**
	 * NO: 14  <br>
	 * nameCN: 飞机导航系统类型  <br>
	 * unit: 代码表  <br>
	 * BUFR FXY: V02061 <br>
	 * descriptionCN: s1<br>
	 * decode rule: 直接取值 <br>
	 * field rule: 直接赋值  <br>
	 */
	private int naviType = 999998;
	
	/**
	 * NO: 15  <br>
	 * nameCN: 飞机数据转发系统类型  <br>
	 * unit: 代码表  <br>
	 * BUFR FXY: V02062 <br>
	 * descriptionCN: s2 <br>
	 * decode rule: 直接取值 <br>
	 * field rule: 直接赋值  <br>
	 */
	private int dataTransType = 999998;
	
	/**
	 * NO: 16  <br>
	 * nameCN: 温度观测准确性代码  <br>
	 * unit: 代码表  <br>
	 * BUFR FXY: V02005 <br>
	 * descriptionCN: s3<br>
	 * decode rule: 直接取值 <br>
	 * field rule: 直接赋值  <br>
	 */
	private int temperatureObsAccuCode = 999998;
	
	/**
	 * NO: 17  <br>
	 * nameCN: 绝对飞行高度  <br>
	 * unit: 百英尺  <br>
	 * BUFR FXY:  <br>
	 * descriptionCN: 飞行器到海平面的高度 FHdHdHd<br>
	 * decode rule: 取缺测值 999999 <br>
	 * field rule: 直接赋值  <br>
	 */
	private double aircraftHeightAboveSeaLevel = 999998;

	/**
	 * NO: 18  <br>
	 * nameCN: 最大等价垂直阵风速  <br>
	 * unit: 1m/s  <br>
	 * BUFR FXY: V11036 <br>
	 * descriptionCN: VGFgFgFg, 如果编报中心为“ZBBB”，取编码值<br>
	 * decode rule: 取值 * 0.1 <br>
	 * field rule: 直接赋值  <br>
	 */
	private double verticalWindSpeed = 999998;

	/**
	 * NO: 19  <br>
	 * nameCN: 更正报标识  <br>
	 * unit:   <br>
	 * BUFR FXY: V_BBB <br>
	 * descriptionCN:<br>
	 * decode rule: 直接取值 <br>
	 * field rule: 直接赋值  <br>
	 */
	private String correctSign;
	/**
	 * NO: 20  <br>
	 * nameCN: 编报中心 <br>
	 * unit:   <br>
	 * BUFR FXY: V_CCCC <br>
	 * descriptionCN: <br>
	 * decode rule: 直接取值 <br>
	 * field rule: 直接赋值  <br>
	 */
	private String reportCenter;
	
	/**
	 * NO: 21  <br>
	 * nameCN: 观测时间  <br>
	 * unit: <br>
	 * BUFR FXY: V04001_02\V04002_02\V04003_02\V04004_02\V04005_02 <br>
	 * descriptionCN: <br>
	 * decode rule: 直接取值 <br>
	 * field rule: 直接赋值  <br>
	 */
	private Date observationTime;
	
	
	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public Date getObservationTime() {
		return observationTime;
	}

	public void setObservationTime(java.util.Date time) {
		this.observationTime = time;
	}

	public int getPlaneStateAndObsType() {
		return planeStateAndObsType;
	}

	public void setPlaneStateAndObsType(int planeStateAndObsType) {
		this.planeStateAndObsType = planeStateAndObsType;
	}

	public String getPlaneID() {
		return planeID;
	}

	public void setPlaneID(String planeID) {
		this.planeID = planeID;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}

	public double getHeightOfStandardPressure() {
		return heightOfStandardPressure;
	}

	public void setHeightOfStandardPressure(double heightOfStandardPressure) {
		this.heightOfStandardPressure = heightOfStandardPressure;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public int getWindDir() {
		return windDir;
	}

	public void setWindDir(int windDir) {
		this.windDir = windDir;
	}

	public double getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}

	public int getTurbulence() {
		return turbulence;
	}

	public void setTurbulence(int turbulence) {
		this.turbulence = turbulence;
	}

	public int getNaviType() {
		return naviType;
	}

	public void setNaviType(int naviType) {
		this.naviType = naviType;
	}

	public int getDataTransType() {
		return dataTransType;
	}

	public void setDataTransType(int dataTransType) {
		this.dataTransType = dataTransType;
	}

	public int getTemperatureObsAccuCode() {
		return temperatureObsAccuCode;
	}

	public void setTemperatureObsAccuCode(int temperatureObsAccuCode) {
		this.temperatureObsAccuCode = temperatureObsAccuCode;
	}

	public double getVerticalWindSpeed() {
		return verticalWindSpeed;
	}

	public void setVerticalWindSpeed(double verticalWindSpeed) {
		this.verticalWindSpeed = verticalWindSpeed;
	}

	public String getCorrectSign() {
		return correctSign;
	}

	public void setCorrectSign(String correctSign) {
		this.correctSign = correctSign;
	}

	public String getReportCenter() {
		return reportCenter;
	}

	public void setReportCenter(String reportCenter) {
		this.reportCenter = reportCenter;
	}

	public Date getDataTime() {
		return dataTime;
	}

	public void setDataTime(Date dataTime) {
		this.dataTime = dataTime;
	}

	public double getAircraftHeightAboveSeaLevel() {
		return aircraftHeightAboveSeaLevel;
	}

	public void setAircraftHeightAboveSeaLevel(double aircraftHeightAboveSeaLevel) {
		this.aircraftHeightAboveSeaLevel = aircraftHeightAboveSeaLevel;
	}

	public double getDewPoint() {
		return dewPoint;
	}

	public void setDewPoint(double dewPoint) {
		this.dewPoint = dewPoint;
	}

	public double getRelativehumidity() {
		return relativehumidity;
	}

	public void setRelativehumidity(double relativehumidity) {
		this.relativehumidity = relativehumidity;
	}
}
