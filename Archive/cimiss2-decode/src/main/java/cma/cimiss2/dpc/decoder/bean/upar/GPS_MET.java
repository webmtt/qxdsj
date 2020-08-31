package cma.cimiss2.dpc.decoder.bean.upar;

import java.util.Date;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * GPS/MET 水汽资料实体类
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《GPSMET和酸雨资料解码算法.docx》 </a>
 *      <li> <a href=" "> 《GPS/MET水汽资料数据表》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月27日 下午5:07:53   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class GPS_MET {
	/**
	 * NO: 1  <br>
	 * nameCN: 资料时间  <br>
	 * unit: <br>
	 * BUFR FXY: D_DATETIME <br>
	 * descriptionCN: V04001，V04002，V04003，V04004V04005<br>
     * decode rule:直接取值<br>
     * field rule:使用java时间转化工具类进行转化,标准形式为：yyyyMMddHHmmss
	 */
	private Date observationTime;
	
	/**
	 * NO: 2  <br>
	 * nameCN: 台站代码  <br>
	 * unit: <br>
	 * BUFR FXY: V_STATIONCODE <br>
	 * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule: 直接赋值
	 */
	private String stationCode;
	
	/**
	 * NO: 3  <br>
	 * nameCN: 区站号 <br>
	 * unit: <br>
	 * BUFR FXY: V01301 <br>
	 * descriptionCN: <br>
     * decode rule: 直接取值, 或者缺省值999999<br>
     * field rule: 直接赋值
	 */	
	private String stationNumberChina;
	
	/**
	 * NO: 4  <br>
	 * nameCN: 纬度 <br>
	 * unit: 度 <br>
	 * BUFR FXY: V05001 <br>
	 * descriptionCN:<br>
     * decode rule:直接取值<br>
     * field rule: 直接赋值
	 */
	private float latitude = 999999;
	
	/**
	 * NO: 5  <br>
	 * nameCN: 经度 <br>
	 * unit: 度 <br>
	 * BUFR FXY: V06001 <br>
	 * descriptionCN:<br>
     * decode rule:直接取值<br>
     * field rule: 直接赋值
	 */
	private float longtitude = 999999;
	
	/**
	 * NO: 6  <br>
	 * nameCN: 测站拔海高度 <br>
	 * unit: 1m <br>
	 * BUFR FXY: V07001 <br>
	 * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule: 直接赋值
	 */
	private float heightOfSationGroundAboveMeanSeaLevel = 999999;
	
	/**
	 * NO: 7  <br>
	 * nameCN: 总的天顶延迟 <br>
	 * unit:  1m <br>
	 * BUFR FXY: V07350 <br>
	 * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule: 直接赋值
	 */
	private float totalZenithDelay = 999999; 
	
	/**
	 * NO: 8  <br>
	 * nameCN: 气压 <br>
	 * unit: 百帕 <br>
	 * BUFR FXY: V10004 <br>
	 * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule: 直接赋值
	 */
	private float pressure = 999999;
	
	/**
	 * NO: 9  <br>
	 * nameCN: 气温 <br>
	 * unit:  摄氏度 <br>
	 * BUFR FXY: V12001 <br>
	 * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule: 直接赋值
	 */
	private float airTemperature = 999999;
	
	/**
	 * NO: 10  <br>
	 * nameCN: 相对湿度  <br>
	 * unit: %  <br>
	 * BUFR FXY: V13003  <br>
	 * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule: 直接赋值
	 */
	private float relativeHumidity = 999999;
	
	/**
	 * NO: 11  <br>
	 * nameCN: 降水量  <br>
	 * unit: 毫米 <br>
	 * BUFR FXY: V_V13016  <br>
	 * descriptionCN: 
	 */
	private float precipitationAmount = 999999;
	
	/**
	 * NO: 12  <br>
	 * nameCN: 电 子浓度  <br>
	 * unit:  <br>
	 * BUFR FXY:  V13017 <br>
	 * descriptionCN: V13017 not insert into DB
     * decode rule:直接取值<br>
     * field rule: 直接赋值
	 */
	private float Electronic = 999999;

	public String getStationNumberChina() {
		return stationNumberChina;
	}

	public void setStationNumberChina(String stationNumberChina) {
		this.stationNumberChina = stationNumberChina;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(float longtitude) {
		this.longtitude = longtitude;
	}

	public float getHeightOfSationGroundAboveMeanSeaLevel() {
		return heightOfSationGroundAboveMeanSeaLevel;
	}

	public void setHeightOfSationGroundAboveMeanSeaLevel(float heightOfSationGroundAboveMeanSeaLevel) {
		this.heightOfSationGroundAboveMeanSeaLevel = heightOfSationGroundAboveMeanSeaLevel;
	}

	public float getAirTemperature() {
		return airTemperature;
	}

	public void setAirTemperature(float airTemperature) {
		this.airTemperature = airTemperature;
	}

	public float getRelativeHumidity() {
		return relativeHumidity;
	}

	public void setRelativeHumidity(float relativeHumidity) {
		this.relativeHumidity = relativeHumidity;
	}

	public float getPrecipitationAmount() {
		return precipitationAmount;
	}

	public void setPrecipitationAmount(float precipitationAmount) {
		this.precipitationAmount = precipitationAmount;
	}

	public float getElectronic() {
		return Electronic;
	}

	public void setElectronic(float electronic) {
		Electronic = electronic;
	}

	public Date getObservationTime() {
		return observationTime;
	}

	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public float getTotalZenithDelay() {
		return totalZenithDelay;
	}

	public void setTotalZenithDelay(float totalZenithDelay) {
		this.totalZenithDelay = totalZenithDelay;
	}

	public float getPressure() {
		return pressure;
	}

	public void setPressure(float pressure) {
		this.pressure = pressure;
	}
}
