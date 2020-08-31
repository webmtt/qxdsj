package cma.cimiss2.dpc.decoder.bean.agme;

// TODO: Auto-generated Javadoc
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
     牧事活动调查子要素
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《农业气象畜牧要素—牧事活动调查》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年9月6日 下午5:35:02   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class Agme_Grass_09 {
	
	/** nameCN: 调查起始时间 <br> unit: 日期<br> BUFR FXY:V04300_017<br> descriptionCN: 日期	年月日时分秒<br> decode rule: 直接取值<br> field rule: 直接赋值<br>. */
	String  observationStarTime;
	
	/** nameCN: 调查结束时间 <br> unit: 日期<br> BUFR FXY:V04300_018 <br> descriptionCN: 日期	年月日时分秒<br> decode rule: 直接取值<br> field rule: 直接赋值<br>. */
	String observationEndTime;
	
	/** nameCN: 牧事活动名称 <br> unit: <br> BUFR FXY: V71616_02<br> descriptionCN: 编码	详见《编码》牧事活动部分<br> decode rule: 直接取值<br> field rule: 直接赋值<br>. */
	int animalHusbandryName;
	
	/** nameCN: 生产性能<br> unit: <br> BUFR FXY:V_PROD_FUNC <br> descriptionCN: 最多200个字符<br> decode rule: 直接取值<br> field rule: 直接赋值<br>. */
    String productPerformance;
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
	 * Gets the observation star time.
	 *
	 * @return the observation star time
	 */
	public String getObservationStarTime() {
		return observationStarTime;
	}
	
	/**
	 * Sets the observation star time.
	 *
	 * @param observationStarTime the new observation star time
	 */
	public void setObservationStarTime(String observationStarTime) {
		this.observationStarTime = observationStarTime;
	}
	
	/**
	 * Gets the observation end time.
	 *
	 * @return the observation end time
	 */
	public String getObservationEndTime() {
		return observationEndTime;
	}
	
	/**
	 * Sets the observation end time.
	 *
	 * @param observationEndTime the new observation end time
	 */
	public void setObservationEndTime(String observationEndTime) {
		this.observationEndTime = observationEndTime;
	}
	
	/**
	 * Gets the animal husbandry name.
	 *
	 * @return the animal husbandry name
	 */
	public int getAnimalHusbandryName() {
		return animalHusbandryName;
	}
	
	/**
	 * Sets the animal husbandry name.
	 *
	 * @param animalHusbandryName the new animal husbandry name
	 */
	public void setAnimalHusbandryName(int animalHusbandryName) {
		this.animalHusbandryName = animalHusbandryName;
	}
	
	/**
	 * Gets the product performance.
	 *
	 * @return the product performance
	 */
	public String getProductPerformance() {
		return productPerformance;
	}
	
	/**
	 * Sets the product performance.
	 *
	 * @param productPerformance the new product performance
	 */
	public void setProductPerformance(String productPerformance) {
		this.productPerformance = productPerformance;
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
