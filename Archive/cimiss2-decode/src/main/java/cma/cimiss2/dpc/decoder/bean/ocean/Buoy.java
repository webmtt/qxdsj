package cma.cimiss2.dpc.decoder.bean.ocean;

import java.util.Date;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
         国内浮标站观测资料实体类
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《气发〔2008〕163号附件1—海洋气象浮标观测站功能需求书（上报稿）.doc》  </a>
 *      <li> <a href=" "> 《国内海上浮标站数据表.docx》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月24日 上午10:52:46   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class Buoy {

	/**
	 * NO: 1.1 <br>
	 * nameCN: 区站号 <br>
	 * unit: <br>
	 * BUFR FXY: V01301 <br>
	 * descriptionCN: 5位数字或第1位为字母，第2-5位为数字<br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private String stationNumberChina;
	
	/**
	 * NO: 1.2  <br>
	 * nameCN: 纬度 <br>
	 * unit: 度  <br>
	 * BUFR FXY: V05001 <br>
	 * descriptionCN:<br>
	 * decode rule: 取值为度分秒，转化为度<br>
     * field rule:直接赋值
	 */
	private double latitude;
	/**
	 * NO: 1.3  <br>
	 * nameCN: 经度 <br>
	 * unit: 度  <br>
	 * BUFR FXY: V06001 <br>
	 * descriptionCN:<br>
	 * decode rule: 取值为度分秒，转化为度<br>
     * field rule:直接赋值
	 */
	private double longtitude;
	/**
	 * NO: 1.4  <br>
	 * nameCN: 观测场拔海高度 <br>
	 * unit: 1m <br>
	 * BUFR FXY: V07001 <br>
	 * descriptionCN: 保留一位小数，扩大10倍记录，高位不足补“0” <br>
	 * decode rule: 取值 除以10<br>
	 * field rule:直接赋值
	 */
	private double heightOfStationGroundAboveMeanSeaLevel;
	
	/**
	 * NO: 1.5  <br>
	 * nameCN: 海盐传感器距海面深度 <br>
	 * unit: 1m <br>
	 * BUFR FXY: V07302  <br>
	 * descriptionCN: 保留一位小数，扩大10倍记录，高位不足补“0”<br>
	 * decode rule: 取值 除以10<br>
	 * field rule:直接赋值
	 */
	 private double depthOfSeasaltSensor;
	
	/**
	 * NO: 1.6  <br>
	 * nameCN: 波高传感器距海面高度 <br>
	 * unit: 1m <br>
	 * BUFR FXY: V07303 <br>
	 * descriptionCN: 保留一位小数，扩大10倍记录，高位不足补“0”<br>
	 * decode rule: 取值 除以10<br>
	 * field rule:直接赋值
	 */
	private double heightOfWaveHeightSensor;
	
	/**
	 * NO: 1.7  <br>
	 * nameCN: 采集器型号 <br>
	 * unit:  <br>
	 * BUFR FXY: V02320 <br>
	 * descriptionCN: 任意字符，位数不足时高位补“/”<br>
	 * decode rule: 直接取值 <br>
	 * field rule:直接赋值
	 */
	private String collectorType;
	
	/**
	 * NO: 1.8  <br>
	 * nameCN: 站类标识 <br>
	 * unit:  <br>
	 * BUFR FXY: V02001 <br>
	 * descriptionCN: 浮标站存“1”，海上平台站存“2”，船舶站存“3”，其它站存“4”<br>
	 * decode rule: 直接取值 <br>
	 * field rule:直接赋值
	 */
	private int stationType;
	
	/**
	 * NO: 2.1  <br>
	 * nameCN: 观测时间 <br>
	 * unit:  <br>
	 * BUFR FXY: D_datetime <br>
	 * descriptionCN: <br>
     * decode rule: 直接取值<br>
     * field rule: 使用java时间转化工具类进行转化,标准形式为：yyyyMMddHHmmss
	 */
	private Date observationTime;
	
	/**
	 * NO: 2.2  <br>
	 * nameCN:浮标方位  <br>
	 * unit: 度  <br>
	 * BUFR FXY: V22400 <br>
	 * descriptionCN: 当前时刻的浮标方位 <br>
	 * decode rule: 直接取值 <br>
	 * field rule:直接赋值
	 */
	private double buoyDir;
	
	/**
	 * NO: 2.3  <br>
	 * nameCN: 海表温度 <br>
	 * unit: 摄氏度 <br>
	 * BUFR FXY: V22049 <br>
	 * descriptionCN: 每1小时内的海表温度<br>
	 * decode rule: 取值除以10 <br>
	 * field rule:直接赋值
	 */
	private double seaTemperature;
	
	/**
	 * NO: 2.4  <br>
	 * nameCN: 海表最高温度 <br>
	 * unit: 摄氏度  <br>
	 * BUFR FXY: V22300 <br>
	 * descriptionCN:每1小时内的海表最高温度 <br>
	 * decode rule: 取值除以10 <br>
	 * field rule:直接赋值
	 */
	private double maxSeaTemperature;
	
	/**
	 * NO: 2.5  <br>
	 * nameCN: 海表最高出现时间  <br>
	 * unit:  <br>
	 * BUFR FXY: V22300_052 <br>
	 * descriptionCN: 每1小时内海表最高温度出现时间 <br>
	 * decode rule: 直接取值  <br>
	 * field rule:直接赋值
	 */
	private int timeOfmaxSeaTemperature;
	
	/**
	 * NO: 2.6  <br>
	 * nameCN: 海表最低温度 <br>
	 * unit: 摄氏度  <br>
	 * BUFR FXY: V22301 <br>
	 * descriptionCN:每1小时内的海表最低温度 <br>
	 * decode rule: 取值除以10 <br>
	 * field rule:直接赋值
	 */
	private double minSeaTemperature;
	
	/**
	 * NO: 2.7  <br>
	 * nameCN: 海表最低出现时间 <br>
	 * unit:  <br>
	 * BUFR FXY: V22301_052 <br>
	 * descriptionCN: 每1小时内海表最低温度出现时间 <br>
	 * decode rule: 直接取值  <br>
	 * field rule:直接赋值
	 */
	private int timeOfMinSeaTemperature;
	
	/**
	 * NO: 2.8  <br>
	 * nameCN: 海水盐度 <br>
	 * unit: 千分之一 <br>
	 * BUFR FXY: V22380 <br>
	 * descriptionCN: 当前时刻的海水盐度 <br>
	 * decode rule: 取值除以10 <br>
	 * field rule:直接赋值
	 */
	private double salinity;
	
	/**
	 * NO: 2.9  <br>
	 * nameCN: 海水平均盐度 <br>
	 * unit: 千分之一 <br>
	 * BUFR FXY: V22380_061 <br>
	 * descriptionCN: 上一正点后至当前时刻的海水平均盐度 <br>
	 * decode rule: 取值除以10 <br>
	 * field rule:直接赋值
	 */
	private double avgSalinity;
	/**
	 * NO: 2.10  <br>
	 * nameCN: 海水电导率 <br>
	 * unit: 1mS/cm <br>
	 * BUFR FXY: V22381 <br>
	 * descriptionCN: 当前时刻的海水电导率<br>
	 * decode rule: 取值除以100 <br>
	 * field rule:直接赋值
	 */
	private double electricalConductivity;
	
	/**
	 * NO: 2.11  <br>
	 * nameCN: 海水平均电导率 <br>
	 * unit: 1mS/cm <br>
	 * BUFR FXY: V22381_061 <br>
	 * descriptionCN: <br>
	 * decode rule: 取值除以100 <br>
	 * field rule:直接赋值
	 */
	private double avgElectricalConductivity;
	
	/**
	 * NO: 2.12  <br>
	 * nameCN: 有效波高 <br>
	 * unit: 1m <br>
	 * BUFR FXY: V22073 <br>
	 * descriptionCN:当前时刻的有效波高 <br>
	 * decode rule: 取值除以10 <br>
	 * field rule:直接赋值
	 */
	private double significantWaveHeight;
	/**
	 * NO: 2.13  <br>
	 * nameCN: 有效波高的周期 <br>
	 * unit: 1s <br>
	 * BUFR FXY: V22073_052 <br>
	 * descriptionCN:当前时刻有效波高的周期 <br>
	 * decode rule: 取值除以10 <br>
	 * field rule:直接赋值
	 */
	private double cycleOfSignificantWaveHeight;
	
	/**
	 * NO: 2.14  <br>
	 * nameCN:平均波高  <br>
	 * unit: 1m <br>
	 * BUFR FXY: V22073_061 <br>
	 * descriptionCN:上一正点后至当前时刻的平均波高 <br>
	 * decode rule: 取值除以10 <br>
	 * field rule:直接赋值
	 */
	private double avgWaveHeight;
	/**
	 * NO: 2.15  <br>
	 * nameCN: 平均波周期 <br>
	 * unit: 1s <br>
	 * BUFR FXY: V22385 <br>
	 * descriptionCN: 上一正点后至当前时刻的平均波周期 <br>
	 * decode rule: 取值除以10 <br>
	 * field rule:直接赋值
	 */
	private double cycleOfAvgWave;
	/**
	 * NO: 2.16  <br>
	 * nameCN: 最大波高 <br>
	 * unit: 1m <br>
	 * BUFR FXY: V22386 <br>
	 * descriptionCN:每1小时内最大波高 <br>
	 * decode rule: 取值除以10 <br>
	 * field rule:直接赋值
	 */
	private double maxWaveHeight;
	/**
	 * NO: 2.17  <br>
	 * nameCN: 最大波高出现时间 <br>
	 * unit:  <br>
	 * BUFR FXY: V22062 <br>
	 * descriptionCN:每1小时内最大波高出现时间 <br>
	 * decode rule: 直接取值  <br>
	 * field rule: 直接赋值
	 */
	private int timeOfMaxWaveHeight;
	/**
	 * NO: 2.18  <br>
	 * nameCN: 最大波高的周期 <br>
	 * unit: 1s <br>
	 * BUFR FXY: V22062_701 <br>
	 * descriptionCN: 每1小时内最大波高的周期 <br>
	 * decode rule: 取值除以10  <br>
	 * field rule:直接赋值
	 */
	private double cycleOfMaxWaveHeight;
	
	/**
	 * NO: 2.19  <br>
	 * nameCN: 表层海洋面流速 <br>
	 * unit: 1m/s <br>
	 * BUFR FXY: V22306 <br>
	 * descriptionCN: <br>
	 * decode rule: 取值除以10  <br>
	 * field rule:直接赋值
	 */
	private double currentVelocity;
	/**
	 * NO: 2.20  <br>
	 * nameCN: 表层海洋面波向 <br>
	 * unit: 度 <br>
	 * BUFR FXY: V22306_701 <br>
	 * descriptionCN: <br>
	 * decode rule: 直接取值  <br>
	 * field rule: 直接赋值
	 */
	private double currentDir;
	/**
	 * NO: 2.21  <br>
	 * nameCN: 海水浊度 <br>
	 * unit: 1NTU <br>
	 * BUFR FXY: V22310 <br>
	 * descriptionCN: 当前时刻的海水浊度<br>
	 * decode rule: 直接取值  <br>
	 * field rule: 直接赋值
	 */
	private double seaTurbidity;
	/**
	 * NO: 2.22  <br>
	 * nameCN: 海水平均浊度 <br>
	 * unit: 1NTU <br>
	 * BUFR FXY: V22310_701 <br>
	 * descriptionCN: 上一正点后至当前时刻的海水平均浊度 <br>
	 * decode rule: 直接取值  <br>
	 * field rule: 直接赋值
	 */
	private double avgSeaTurbidity;
	
	/**
	 * NO: 2.23  <br>
	 * nameCN: 海水叶绿素浓度  <br>
	 * unit: 1μg/L <br>
	 * BUFR FXY: V22311 <br>
	 * descriptionCN: 当前时刻的海水叶绿素浓度<br>
	 * decode rule: 直接取值  <br>
	 * field rule: 直接赋值
	 */
	private double chlorophyllConcentration;
	/**
	 * NO: 2.24  <br>
	 * nameCN: 海水平均叶绿素浓度  <br>
	 * unit: 1μg/L <br>
	 * BUFR FXY: V22311_701 <br>
	 * descriptionCN:上一正点后至当前时刻的海水平均叶绿素浓度 <br>
	 * decode rule: 直接取值  <br>
	 * field rule: 直接赋值
	 */
	private double avgChlorophyllConcentration;

	public String getStationNumberChina() {
		return stationNumberChina;
	}

	public void setStationNumberChina(String stationNumberChina) {
		this.stationNumberChina = stationNumberChina;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public double getDepthOfSeasaltSensor() {
		return depthOfSeasaltSensor;
	}

	public void setDepthOfSeasaltSensor(double depthOfSeasaltSensor) {
		this.depthOfSeasaltSensor = depthOfSeasaltSensor;
	}

	public double getHeightOfWaveHeightSensor() {
		return heightOfWaveHeightSensor;
	}

	public void setHeightOfWaveHeightSensor(double heightOfWaveHeightSensor) {
		this.heightOfWaveHeightSensor = heightOfWaveHeightSensor;
	}

	public String getCollectorType() {
		return collectorType;
	}

	public void setCollectorType(String collectorType) {
		this.collectorType = collectorType;
	}

	public int getStationType() {
		return stationType;
	}

	public void setStationType(int stationType) {
		this.stationType = stationType;
	}

	public Date getObservationTime() {
		return observationTime;
	}

	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}

	public double getBuoyDir() {
		return buoyDir;
	}

	public void setBuoyDir(double buoyDir) {
		this.buoyDir = buoyDir;
	}

	public double getSeaTemperature() {
		return seaTemperature;
	}

	public void setSeaTemperature(double seaTemperature) {
		this.seaTemperature = seaTemperature;
	}

	public double getMaxSeaTemperature() {
		return maxSeaTemperature;
	}

	public void setMaxSeaTemperature(double maxSeaTemperature) {
		this.maxSeaTemperature = maxSeaTemperature;
	}

	public int getTimeOfmaxSeaTemperature() {
		return timeOfmaxSeaTemperature;
	}

	public void setTimeOfmaxSeaTemperature(int timeOfmaxSeaTemperature) {
		this.timeOfmaxSeaTemperature = timeOfmaxSeaTemperature;
	}

	public double getMinSeaTemperature() {
		return minSeaTemperature;
	}

	public void setMinSeaTemperature(double minSeaTemperature) {
		this.minSeaTemperature = minSeaTemperature;
	}

	public int getTimeOfMinSeaTemperature() {
		return timeOfMinSeaTemperature;
	}

	public void setTimeOfMinSeaTemperature(int timeOfMinSeaTemperature) {
		this.timeOfMinSeaTemperature = timeOfMinSeaTemperature;
	}

	public double getSalinity() {
		return salinity;
	}

	public void setSalinity(double salinity) {
		this.salinity = salinity;
	}

	public double getAvgSalinity() {
		return avgSalinity;
	}

	public void setAvgSalinity(double avgSalinity) {
		this.avgSalinity = avgSalinity;
	}

	public double getElectricalConductivity() {
		return electricalConductivity;
	}

	public void setElectricalConductivity(double electricalConductivity) {
		this.electricalConductivity = electricalConductivity;
	}

	public double getAvgElectricalConductivity() {
		return avgElectricalConductivity;
	}

	public void setAvgElectricalConductivity(double avgElectricalConductivity) {
		this.avgElectricalConductivity = avgElectricalConductivity;
	}

	public double getSignificantWaveHeight() {
		return significantWaveHeight;
	}

	public void setSignificantWaveHeight(double significantWaveHeight) {
		this.significantWaveHeight = significantWaveHeight;
	}

	public double getCycleOfSignificantWaveHeight() {
		return cycleOfSignificantWaveHeight;
	}

	public void setCycleOfSignificantWaveHeight(double cycleOfSignificantWaveHeight) {
		this.cycleOfSignificantWaveHeight = cycleOfSignificantWaveHeight;
	}

	public double getAvgWaveHeight() {
		return avgWaveHeight;
	}

	public void setAvgWaveHeight(double avgWaveHeight) {
		this.avgWaveHeight = avgWaveHeight;
	}

	public double getCycleOfAvgWave() {
		return cycleOfAvgWave;
	}

	public void setCycleOfAvgWave(double cycleOfAvgWave) {
		this.cycleOfAvgWave = cycleOfAvgWave;
	}

	public double getMaxWaveHeight() {
		return maxWaveHeight;
	}

	public void setMaxWaveHeight(double maxWaveHeight) {
		this.maxWaveHeight = maxWaveHeight;
	}

	public int getTimeOfMaxWaveHeight() {
		return timeOfMaxWaveHeight;
	}

	public void setTimeOfMaxWaveHeight(int timeOfMaxWaveHeight) {
		this.timeOfMaxWaveHeight = timeOfMaxWaveHeight;
	}

	public double getCycleOfMaxWaveHeight() {
		return cycleOfMaxWaveHeight;
	}

	public void setCycleOfMaxWaveHeight(double cycleOfMaxWaveHeight) {
		this.cycleOfMaxWaveHeight = cycleOfMaxWaveHeight;
	}

	public double getCurrentVelocity() {
		return currentVelocity;
	}

	public void setCurrentVelocity(double currentVelocity) {
		this.currentVelocity = currentVelocity;
	}

	public double getCurrentDir() {
		return currentDir;
	}

	public void setCurrentDir(double currentDir) {
		this.currentDir = currentDir;
	}

	public double getSeaTurbidity() {
		return seaTurbidity;
	}

	public void setSeaTurbidity(double seaTurbidity) {
		this.seaTurbidity = seaTurbidity;
	}

	public double getAvgSeaTurbidity() {
		return avgSeaTurbidity;
	}

	public void setAvgSeaTurbidity(double avgSeaTurbidity) {
		this.avgSeaTurbidity = avgSeaTurbidity;
	}

	public double getChlorophyllConcentration() {
		return chlorophyllConcentration;
	}

	public void setChlorophyllConcentration(double chlorophyllConcentration) {
		this.chlorophyllConcentration = chlorophyllConcentration;
	}

	public double getAvgChlorophyllConcentration() {
		return avgChlorophyllConcentration;
	}

	public void setAvgChlorophyllConcentration(double avgChlorophyllConcentration) {
		this.avgChlorophyllConcentration = avgChlorophyllConcentration;
	}

	public double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}

	public double getHeightOfStationGroundAboveMeanSeaLevel() {
		return heightOfStationGroundAboveMeanSeaLevel;
	}

	public void setHeightOfStationGroundAboveMeanSeaLevel(double heightOfStationGroundAboveMeanSeaLevel) {
		this.heightOfStationGroundAboveMeanSeaLevel = heightOfStationGroundAboveMeanSeaLevel;
	}

}
