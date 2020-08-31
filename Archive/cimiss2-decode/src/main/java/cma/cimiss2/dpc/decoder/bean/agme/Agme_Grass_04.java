package cma.cimiss2.dpc.decoder.bean.agme;

import java.util.Date;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
   覆盖度及草层采食度子要素
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《农业气象畜牧要素—牧草覆盖度及草层采食度》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年9月6日 下午5:04:30   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class Agme_Grass_04{
	/**
	 * nameCN: 观测时间 <br>
	 * unit: 日期<br>
	 * BUFR FXY:D_DATETIME <br>
	 * descriptionCN: 日期	年月日时分秒
	 */
	Date observationTime;
	/**
	 * nameCN: 覆盖度 <br>
	 * unit: %<br>
	 * BUFR FXY: V71009 <br>
	 * descriptionCN: 灌木、半灌木的覆盖地面比例<br>
     * decode rule: 直接取值<br>
     * field rule: 直接赋值<br>
	 */
	Double coverDegree ;
	
	/**
	 * nameCN: 草层状况评价 <br>
	 * unit: <br>
	 * BUFR FXY: V71907 <br>
	 * descriptionCN: 1为优；2为良；3为中；4为差；5为很差<br>
     * decode rule: 直接取值<br>
     * field rule: 直接赋值<br>
	 */
	int evaluaGrassCondition;
	
	/**
	 * nameCN: 采食度 <br>
	 * unit: <br>
	 * BUFR FXY:V71908 <br>
	 * descriptionCN: 1为轻微；2为轻；3为中；4为重；5为很重<br>
     * decode rule: 直接取值<br>
     * field rule: 直接赋值<br>
	 */
	int feedingDegree;
	/**
	 * nameCN: 采食率 <br>
	 * unit: %<br>
	 * BUFR FXY: V71909<br>
	 * descriptionCN: 混合牧草的家畜采食率<br>
     * decode rule: 直接取值<br>
     * field rule: 直接赋值<br>
	 */
	Double feedingRate;
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
	public Date getObservationTime() {
		return observationTime;
	}
	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}
	public Double getCoverDegree() {
		return coverDegree;
	}
	public void setCoverDegree(Double coverDegree) {
		this.coverDegree = coverDegree;
	}
	public int getEvaluaGrassCondition() {
		return evaluaGrassCondition;
	}
	public void setEvaluaGrassCondition(int evaluaGrassCondition) {
		this.evaluaGrassCondition = evaluaGrassCondition;
	}
	public int getFeedingDegree() {
		return feedingDegree;
	}
	public void setFeedingDegree(int feedingDegree) {
		this.feedingDegree = feedingDegree;
	}
	public Double getFeedingRate() {
		return feedingRate;
	}
	public void setFeedingRate(Double feedingRate) {
		this.feedingRate = feedingRate;
	}
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
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getHeightOfSationGroundAboveMeanSeaLevel() {
		return heightOfSationGroundAboveMeanSeaLevel;
	}
	public void setHeightOfSationGroundAboveMeanSeaLevel(Double heightOfSationGroundAboveMeanSeaLevel) {
		this.heightOfSationGroundAboveMeanSeaLevel = heightOfSationGroundAboveMeanSeaLevel;
	}
	public Double getHeightOfBarometerAboveMeanSeaLevel() {
		return heightOfBarometerAboveMeanSeaLevel;
	}
	public void setHeightOfBarometerAboveMeanSeaLevel(Double heightOfBarometerAboveMeanSeaLevel) {
		this.heightOfBarometerAboveMeanSeaLevel = heightOfBarometerAboveMeanSeaLevel;
	}

}
