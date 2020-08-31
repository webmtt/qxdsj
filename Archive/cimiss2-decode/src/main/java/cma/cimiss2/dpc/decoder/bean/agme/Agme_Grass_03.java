package cma.cimiss2.dpc.decoder.bean.agme;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
	牧草产量子要素
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《农业气象畜牧要素—牧草产量》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年9月6日 下午5:02:39   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class Agme_Grass_03 {
	
	/** nameCN: 测定时间 <br> unit: 日期<br> BUFR FXY: D_DATETIME <br> descriptionCN: 日期	年月日时分秒. */
	Date observationTime;
	
	/** nameCN: 牧草名称 <br> unit: <br> BUFR FXY: V71501 <br> descriptionCN: 编码	详见《编码》牧草名称部分<br> decode rule: 直接取值<br> field rule: 直接赋值<br>. */
	int  herbageName;
	/**
	 * nameCN: 干重 <br>
	 * unit:1公斤/公顷 <br>
	 * BUFR FXY:V71652 <br>
	 * descriptionCN: 牧草或灌木、半灌木分种产量<br>
     * decode rule: 取值 * 0.1<br>
     * field rule: 直接赋值<br>
	 */
	Double dryWeight;
	/**
	 * nameCN: 鲜重 <br>
	 * unit:1公斤/公顷 <br>
	 * BUFR FXY: V71651 <br>
	 * descriptionCN: <br>
     * decode rule: 取值*0.1<br>
     * field rule: 直接赋值<br>
	 */
	Double freshWeight;
	
	/** nameCN: 干鲜比 <br> unit:% <br> BUFR FXY: V71906 <br> descriptionCN:干重与鲜重的比例 <br> decode rule: 直接取值<br> field rule: 直接赋值<br>. */
	Double dryFreshRatio;
	
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
     * decode rule: 取值为度分秒，转化为度<br>
     * field rule: 直接赋值<br>
    */
   Double latitude;
   /**
    * NO: 1.4  <br>
    * nameCN: 经度 <br>
    * unit: 度 <br>
    * BUFR FXY: V06001 <br>
    * descriptionCN:<br>
     * decode rule: 取值为度分秒，转化为度<br>
     * field rule: 直接赋值<br>
    */
   Double longitude;
   /**
    * NO: 1.5  <br>
    * nameCN: 观测场拔海高度 <br>
    * unit: 1m <br>
    * BUFR FXY: V07001 <br>
    * descriptionCN: 若低于海平面，为负值<br>
     * decode rule: 取值*0.1<br>
     * field rule: 直接赋值<br>
    */
   Double heightOfSationGroundAboveMeanSeaLevel;
   /**
    * NO: 1.6  <br>
    * nameCN: 气压传感器拔海高度 <br>
    * unit: 1m <br>
    * BUFR FXY: V07031 <br>
    * descriptionCN: 无气压传感器时，值为null ，若低于海平面，为负值<br>
     * decode rule: 取值*0.1<br>
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
	 * Gets the dry weight.
	 *
	 * @return the dry weight
	 */
	public Double getDryWeight() {
		return dryWeight;
	}
	
	/**
	 * Sets the dry weight.
	 *
	 * @param dryWeight the new dry weight
	 */
	public void setDryWeight(Double dryWeight) {
		this.dryWeight = dryWeight;
	}
	
	/**
	 * Gets the fresh weight.
	 *
	 * @return the fresh weight
	 */
	public Double getFreshWeight() {
		return freshWeight;
	}
	
	/**
	 * Sets the fresh weight.
	 *
	 * @param freshWeight the new fresh weight
	 */
	public void setFreshWeight(Double freshWeight) {
		this.freshWeight = freshWeight;
	}
	
	/**
	 * Gets the dry fresh ratio.
	 *
	 * @return the dry fresh ratio
	 */
	public Double getDryFreshRatio() {
		return dryFreshRatio;
	}
	
	/**
	 * Sets the dry fresh ratio.
	 *
	 * @param dryFreshRatio the new dry fresh ratio
	 */
	public void setDryFreshRatio(Double dryFreshRatio) {
		this.dryFreshRatio = dryFreshRatio;
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
	


}
