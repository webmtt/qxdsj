package cma.cimiss2.dpc.decoder.bean.agme;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
        家畜羯羊重调查
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《农业气象畜牧要素—家畜羯羊重调查》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年9月6日 下午5:08:36   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class Agme_Grass_07 {
	
	/** nameCN: 调查时间 <br> unit: 日期<br> BUFR FXY: D_DATETIME <br> descriptionCN: 日期	年月日时分秒. */
	Date observationTime;
	/**
	 * nameCN: 羯羊_1体重 <br>
	 * unit: 1公斤<br>
	 * BUFR FXY: V_ WEIGHT_01<br>
	 * descriptionCN: 	<br>
     * decode rule: 取值*0.1<br>
     * field rule: 直接赋值<br>
	 */
	Double ramWeight_1;
	/**
	 * nameCN: 羯羊_2体重 <br>
	 * unit: 1公斤<br>
	 * BUFR FXY: V_ WEIGHT_02<br>
	 * descriptionCN: 	<br>
     * decode rule: 取值*0.1<br>
     * field rule: 直接赋值<br>
	 */
	Double ramWeight_2;
	/**
	 * nameCN: 羯羊_3体重 <br>
	 * unit: 1公斤<br>
	 * BUFR FXY: V_ WEIGHT_03<br>
	 * descriptionCN: 	<br>
     * decode rule: 取值*0.1<br>
     * field rule: 直接赋值<br>
	 */
	Double ramWeight_3;
	/**
	 * nameCN: 羯羊_4体重 <br>
	 * unit: 1公斤<br>
	 * BUFR FXY: V_ WEIGHT_04<br>
	 * descriptionCN: 	<br>
     * decode rule: 取值*0.1<br>
     * field rule: 直接赋值<br>
	 */
	Double ramWeight_4;
	/**
	 * nameCN: 羯羊_5体重 <br>
	 * unit: 1公斤<br>
	 * BUFR FXY: V_ WEIGHT_05<br>
	 * descriptionCN: 	<br>
     * decode rule: 取值*0.1<br>
     * field rule: 直接赋值<br>
	 */
	Double ramWeight_5;
	
	/**
	 * nameCN: 平均 <br>
	 * unit: 1公斤<br>
	 * BUFR FXY:V_ WEIGHT_AVG <br>
	 * descriptionCN: <br>
     * decode rule: 取值*0.1<br>
     * field rule: 直接赋值<br>
	 */
	Double avgWeight;
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
	 * Gets the ram weight 1.
	 *
	 * @return the ram weight 1
	 */
	public Double getRamWeight_1() {
		return ramWeight_1;
	}

	/**
	 * Sets the ram weight 1.
	 *
	 * @param ramWeight_1 the new ram weight 1
	 */
	public void setRamWeight_1(Double ramWeight_1) {
		this.ramWeight_1 = ramWeight_1;
	}

	/**
	 * Gets the ram weight 2.
	 *
	 * @return the ram weight 2
	 */
	public Double getRamWeight_2() {
		return ramWeight_2;
	}

	/**
	 * Sets the ram weight 2.
	 *
	 * @param ramWeight_2 the new ram weight 2
	 */
	public void setRamWeight_2(Double ramWeight_2) {
		this.ramWeight_2 = ramWeight_2;
	}

	/**
	 * Gets the ram weight 3.
	 *
	 * @return the ram weight 3
	 */
	public Double getRamWeight_3() {
		return ramWeight_3;
	}

	/**
	 * Sets the ram weight 3.
	 *
	 * @param ramWeight_3 the new ram weight 3
	 */
	public void setRamWeight_3(Double ramWeight_3) {
		this.ramWeight_3 = ramWeight_3;
	}

	/**
	 * Gets the ram weight 4.
	 *
	 * @return the ram weight 4
	 */
	public Double getRamWeight_4() {
		return ramWeight_4;
	}

	/**
	 * Sets the ram weight 4.
	 *
	 * @param ramWeight_4 the new ram weight 4
	 */
	public void setRamWeight_4(Double ramWeight_4) {
		this.ramWeight_4 = ramWeight_4;
	}

	/**
	 * Gets the ram weight 5.
	 *
	 * @return the ram weight 5
	 */
	public Double getRamWeight_5() {
		return ramWeight_5;
	}

	/**
	 * Sets the ram weight 5.
	 *
	 * @param ramWeight_5 the new ram weight 5
	 */
	public void setRamWeight_5(Double ramWeight_5) {
		this.ramWeight_5 = ramWeight_5;
	}

	/**
	 * Gets the avg weight.
	 *
	 * @return the avg weight
	 */
	public Double getAvgWeight() {
		return avgWeight;
	}

	/**
	 * Sets the avg weight.
	 *
	 * @param avgWeight the new avg weight
	 */
	public void setAvgWeight(Double avgWeight) {
		this.avgWeight = avgWeight;
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
