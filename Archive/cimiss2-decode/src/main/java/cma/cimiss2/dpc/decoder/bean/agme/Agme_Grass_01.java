package cma.cimiss2.dpc.decoder.bean.agme;

import java.util.Date;


/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
      牧草发育期子要素实体类
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《农业气象畜牧要素—牧草发育期》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年9月6日 下午4:55:00   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class Agme_Grass_01 {
	
	/** nameCN: 观测时间 <br> unit: 日期<br> BUFR FXY: D_DATETIME <br> descriptionCN: 日期	年月日时分秒. */
	Date observationTime;
	
	/** nameCN: 牧草名称 <br> unit: <br> BUFR FXY:V71501 <br> descriptionCN: 编码	详见《编码》牧草名称部分<br> decode rule: 直接取值<br> field rule: 直接赋值<br>. */
	int  herbageName;
	
	/** nameCN: 发育期 <br> unit: <br> BUFR FXY: V71002 <br> descriptionCN: 编码，详见《编码》作物发育期部分<br> decode rule: 直接取值<br> field rule: 直接赋值<br>. */
	int developmentalPeriod;
	
	/** nameCN: 发育期百分率 <br> unit: %<br> BUFR FXY: V71010 <br> descriptionCN: 进入发育期的株（茎）数比例<br> decode rule: 直接取值<br> field rule: 直接赋值<br>. */
	Double PerDevlopmentPeriod;
	
	//头信息
	 /**
     * NO: 1.1  <br>
     * nameCN: 区站号 <br>
     * unit: <br>
     * BUFR FXY: V01301 <br>
     * descriptionCN: 5位数字或第1位为字母，第2-5位为数字
     */
    String stationNumberChina;
    /**
     * NO: 1.3  <br>
     * nameCN: 纬度 <br>
     * unit: 度 <br>
     * BUFR FXY: V05001 <br>
     * descriptionCN:<br>
     * decode rule: 度分秒转化为度<br>
     * field rule: 直接赋值<br>
     */
    Double latitude;
    /**
     * NO: 1.4  <br>
     * nameCN: 经度 <br>
     * unit: 度 <br>
     * BUFR FXY: V06001 <br>
     * descriptionCN:<br>
     * decode rule: 度分秒转化为度<br>
     * field rule: 直接赋值<br>
     */
    Double longitude;
    /**
     * NO: 1.5  <br>
     * nameCN: 观测场拔海高度 <br>
     * unit: 1m <br>
     * BUFR FXY: V07001 <br>
     * descriptionCN: 若低于海平面，为负值<br>
     * decode rule: 取值 * 0.1<br>
     * field rule: 直接赋值<br>
     */
    Double heightOfSationGroundAboveMeanSeaLevel;
    /**
     * NO: 1.6  <br>
     * nameCN: 气压传感器拔海高度 <br>
     * unit: 1m <br>
     * BUFR FXY: V07031 <br>
     * descriptionCN: 无气压传感器时，值为null ，若低于海平面，为负值<br>
     * decode rule: 取值 * 0.1<br>
     * field rule: 直接赋值<br>
     */
    Double heightOfBarometerAboveMeanSeaLevel;
	
	/**
	 * Gets the observation time.
	 *
	 * @return the observation time
	 */
	public Date getObservationTime() {
		return observationTime;
	}
	
	/**
	 * Sets the observation time.
	 *
	 * @param observationTime the new observation time
	 */
	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}
	
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
	public Double getLatitude() {
		return latitude;
	}
	
	/**
	 * Sets the latitude.
	 *
	 * @param latitude the new latitude
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	/**
	 * Gets the longitude.
	 *
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}
	
	/**
	 * Sets the longitude.
	 *
	 * @param longitude the new longitude
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	/**
	 * Gets the height of sation ground above mean sea level.
	 *
	 * @return the height of sation ground above mean sea level
	 */
	public Double getHeightOfSationGroundAboveMeanSeaLevel() {
		return heightOfSationGroundAboveMeanSeaLevel;
	}
	
	/**
	 * Sets the height of sation ground above mean sea level.
	 *
	 * @param heightOfSationGroundAboveMeanSeaLevel the new height of sation ground above mean sea level
	 */
	public void setHeightOfSationGroundAboveMeanSeaLevel(Double heightOfSationGroundAboveMeanSeaLevel) {
		this.heightOfSationGroundAboveMeanSeaLevel = heightOfSationGroundAboveMeanSeaLevel;
	}
	
	/**
	 * Gets the height of barometer above mean sea level.
	 *
	 * @return the height of barometer above mean sea level
	 */
	public Double getHeightOfBarometerAboveMeanSeaLevel() {
		return heightOfBarometerAboveMeanSeaLevel;
	}
	
	/**
	 * Sets the height of barometer above mean sea level.
	 *
	 * @param heightOfBarometerAboveMeanSeaLevel the new height of barometer above mean sea level
	 */
	public void setHeightOfBarometerAboveMeanSeaLevel(Double heightOfBarometerAboveMeanSeaLevel) {
		this.heightOfBarometerAboveMeanSeaLevel = heightOfBarometerAboveMeanSeaLevel;
	}
	
	/**
	 * Gets the herbage name.
	 *
	 * @return the herbage name
	 */
	public int getHerbageName() {
		return herbageName;
	}
	
	/**
	 * Sets the herbage name.
	 *
	 * @param herbageName the new herbage name
	 */
	public void setHerbageName(int herbageName) {
		this.herbageName = herbageName;
	}
	
	/**
	 * Gets the developmental period.
	 *
	 * @return the developmental period
	 */
	public int getDevelopmentalPeriod() {
		return developmentalPeriod;
	}
	
	/**
	 * Sets the developmental period.
	 *
	 * @param developmentalPeriod the new developmental period
	 */
	public void setDevelopmentalPeriod(int developmentalPeriod) {
		this.developmentalPeriod = developmentalPeriod;
	}
	
	/**
	 * Gets the per devlopment period.
	 *
	 * @return the per devlopment period
	 */
	public Double getPerDevlopmentPeriod() {
		return PerDevlopmentPeriod;
	}
	
	/**
	 * Sets the per devlopment period.
	 *
	 * @param perDevlopmentPeriod the new per devlopment period
	 */
	public void setPerDevlopmentPeriod(Double perDevlopmentPeriod) {
		PerDevlopmentPeriod = perDevlopmentPeriod;
	}
	
	

}
