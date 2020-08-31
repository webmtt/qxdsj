package cma.cimiss2.dpc.decoder.bean.agme;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
     畜群基本情况调查
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《农业气象畜牧要素—畜群基本情况调查》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年9月6日 下午5:25:10   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class Agme_Grass_08 {
	
	/** nameCN: 调查时间 <br> unit: 日期<br> BUFR FXY: <br> descriptionCN: 日期	年月日时分秒. */
	Date observationTime;
	
	/** nameCN: 春季日平均放牧时数 <br> unit: 小时<br> BUFR FXY:V_AVG_GRAZE_01 <br> descriptionCN: <br> decode rule: 直接取值<br> field rule: 直接赋值<br>. */
	int avgSpringDailyGrazHours;
	
	/** nameCN: 夏季日平均放牧时数 <br> unit: 小时<br> BUFR FXY:V_AVG_GRAZE_02 <br> descriptionCN: <br> decode rule: 直接取值<br> field rule: 直接赋值<br>. */
	int avgSummerDailyGrazHours;
	
	/** nameCN: 秋季日平均放牧时数 <br> unit: 小时<br> BUFR FXY: V_AVG_GRAZE_03<br> descriptionCN: <br> decode rule: 直接取值<br> field rule: 直接赋值<br>. */
	int avgFallDailyGrazHours;
	
	/** nameCN: 冬季日平均放牧时数 <br> unit: 小时<br> BUFR FXY: V_AVG_GRAZE_04<br> descriptionCN: <br> decode rule: 直接取值<br> field rule: 直接赋值<br>. */
	int avgWinterDailyGrazHours;
	
	/** nameCN: 有无棚舍 <br> unit: <br> BUFR FXY:V_HOVEL <br> descriptionCN: 0无棚舍,1有棚舍<br> decode rule: 直接取值<br> field rule: 直接赋值<br>. */
	int isHaveSuccah;
	
	/** nameCN: 棚舍数量 <br> unit: <br> BUFR FXY: V_HOVEL_COUNT<br> descriptionCN: <br> decode rule: 直接取值<br> field rule: 直接赋值<br>. */
	int succahNum;
	/**
	 * nameCN: 棚舍长 <br>
	 * unit: 1m<br>
	 * BUFR FXY:V_HOVEL_LEN <br>
	 * descriptionCN: <br>
     * decode rule: 取值 * 0.1<br>
     * field rule: 直接赋值<br>
	 */
	Double succahLong;
	/**
	 * nameCN: 棚舍宽 <br>
	 * unit: 1m<br>
	 * BUFR FXY:V_HOVEL_W <br>
	 * descriptionCN: <br>
     * decode rule: 取值 * 0.1<br>
     * field rule: 直接赋值<br>
	 */
	Double succahWide;
	/**
	 * nameCN: 棚舍高 <br>
	 * unit: 1m<br>
	 * BUFR FXY: V_HOVEL_H<br>
	 * descriptionCN: <br>
     * decode rule: 取值 * 0.1<br>
     * field rule: 直接赋值<br>
	 */
	Double succahHigh;
	
	/** nameCN: 棚舍结构 <br> unit: <br> BUFR FXY: V_HOVEL_ST <br> descriptionCN: 最多20个字符<br> decode rule: 直接取值<br> field rule: 直接赋值<br>. */
	String succahFrame;
	
	/** nameCN: 棚舍型式 <br> unit: <br> BUFR FXY:V_HOVEL_TYPE <br> descriptionCN: 最多20个字符<br> decode rule: 直接取值<br> field rule: 直接赋值<br>. */
	String succahType;
	
	/** nameCN: 棚舍门窗开向 <br> unit: <br> BUFR FXY:V_HOVEL_WD <br> descriptionCN: 最多10个字符<br> decode rule: 直接取值<br> field rule: 直接赋值<br>. */
	String succahWinDirection;
	
	/** nameCN: 畜群家畜名称 <br> unit: <br> BUFR FXY: V71501<br> descriptionCN: 最多20个字符<br> decode rule: 直接取值<br> field rule: 直接赋值<br>. */
	String livestockName;
	
	/** nameCN: 家畜品种 <br> unit: <br> BUFR FXY: V71601<br> descriptionCN: 最多20个字符<br> decode rule: 直接取值<br> field rule: 直接赋值<br>. */
	String  LivestockBreeds;
	
	/** nameCN: 畜群所属单位 <br> unit: <br> BUFR FXY:V_ORGAN <br> descriptionCN: 最多100个字符<br> decode rule: 直接取值<br> field rule: 直接赋值<br>. */
	String LivestockUnit ;
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
	 * Gets the avg spring daily graz hours.
	 *
	 * @return the avg spring daily graz hours
	 */
	public int getAvgSpringDailyGrazHours() {
		return avgSpringDailyGrazHours;
	}
	
	/**
	 * Sets the avg spring daily graz hours.
	 *
	 * @param avgSpringDailyGrazHours the new avg spring daily graz hours
	 */
	public void setAvgSpringDailyGrazHours(int avgSpringDailyGrazHours) {
		this.avgSpringDailyGrazHours = avgSpringDailyGrazHours;
	}
	
	/**
	 * Gets the avg summer daily graz hours.
	 *
	 * @return the avg summer daily graz hours
	 */
	public int getAvgSummerDailyGrazHours() {
		return avgSummerDailyGrazHours;
	}
	
	/**
	 * Sets the avg summer daily graz hours.
	 *
	 * @param avgSummerDailyGrazHours the new avg summer daily graz hours
	 */
	public void setAvgSummerDailyGrazHours(int avgSummerDailyGrazHours) {
		this.avgSummerDailyGrazHours = avgSummerDailyGrazHours;
	}
	
	/**
	 * Gets the avg fall daily graz hours.
	 *
	 * @return the avg fall daily graz hours
	 */
	public int getAvgFallDailyGrazHours() {
		return avgFallDailyGrazHours;
	}
	
	/**
	 * Sets the avg fall daily graz hours.
	 *
	 * @param avgFallDailyGrazHours the new avg fall daily graz hours
	 */
	public void setAvgFallDailyGrazHours(int avgFallDailyGrazHours) {
		this.avgFallDailyGrazHours = avgFallDailyGrazHours;
	}
	
	/**
	 * Gets the avg winter daily graz hours.
	 *
	 * @return the avg winter daily graz hours
	 */
	public int getAvgWinterDailyGrazHours() {
		return avgWinterDailyGrazHours;
	}
	
	/**
	 * Sets the avg winter daily graz hours.
	 *
	 * @param avgWinterDailyGrazHours the new avg winter daily graz hours
	 */
	public void setAvgWinterDailyGrazHours(int avgWinterDailyGrazHours) {
		this.avgWinterDailyGrazHours = avgWinterDailyGrazHours;
	}
	
	/**
	 * Gets the checks if is have succah.
	 *
	 * @return the checks if is have succah
	 */
	public int getIsHaveSuccah() {
		return isHaveSuccah;
	}
	
	/**
	 * Sets the checks if is have succah.
	 *
	 * @param isHaveSuccah the new checks if is have succah
	 */
	public void setIsHaveSuccah(int isHaveSuccah) {
		this.isHaveSuccah = isHaveSuccah;
	}
	
	/**
	 * Gets the succah num.
	 *
	 * @return the succah num
	 */
	public int getSuccahNum() {
		return succahNum;
	}
	
	/**
	 * Sets the succah num.
	 *
	 * @param succahNum the new succah num
	 */
	public void setSuccahNum(int succahNum) {
		this.succahNum = succahNum;
	}
	
	/**
	 * Gets the succah long.
	 *
	 * @return the succah long
	 */
	public Double getSuccahLong() {
		return succahLong;
	}
	
	/**
	 * Sets the succah long.
	 *
	 * @param succahLong the new succah long
	 */
	public void setSuccahLong(Double succahLong) {
		this.succahLong = succahLong;
	}
	
	/**
	 * Gets the succah wide.
	 *
	 * @return the succah wide
	 */
	public Double getSuccahWide() {
		return succahWide;
	}
	
	/**
	 * Sets the succah wide.
	 *
	 * @param succahWide the new succah wide
	 */
	public void setSuccahWide(Double succahWide) {
		this.succahWide = succahWide;
	}
	
	/**
	 * Gets the succah high.
	 *
	 * @return the succah high
	 */
	public Double getSuccahHigh() {
		return succahHigh;
	}
	
	/**
	 * Sets the succah high.
	 *
	 * @param succahHigh the new succah high
	 */
	public void setSuccahHigh(Double succahHigh) {
		this.succahHigh = succahHigh;
	}
	
	/**
	 * Gets the succah frame.
	 *
	 * @return the succah frame
	 */
	public String getSuccahFrame() {
		return succahFrame;
	}
	
	/**
	 * Sets the succah frame.
	 *
	 * @param succahFrame the new succah frame
	 */
	public void setSuccahFrame(String succahFrame) {
		this.succahFrame = succahFrame;
	}
	
	/**
	 * Gets the succah type.
	 *
	 * @return the succah type
	 */
	public String getSuccahType() {
		return succahType;
	}
	
	/**
	 * Sets the succah type.
	 *
	 * @param succahType the new succah type
	 */
	public void setSuccahType(String succahType) {
		this.succahType = succahType;
	}
	
	/**
	 * Gets the succah win direction.
	 *
	 * @return the succah win direction
	 */
	public String getSuccahWinDirection() {
		return succahWinDirection;
	}
	
	/**
	 * Sets the succah win direction.
	 *
	 * @param succahWinDirection the new succah win direction
	 */
	public void setSuccahWinDirection(String succahWinDirection) {
		this.succahWinDirection = succahWinDirection;
	}
	
	/**
	 * Gets the livestock name.
	 *
	 * @return the livestock name
	 */
	public String getLivestockName() {
		return livestockName;
	}
	
	/**
	 * Sets the livestock name.
	 *
	 * @param livestockName the new livestock name
	 */
	public void setLivestockName(String livestockName) {
		this.livestockName = livestockName;
	}
	
	/**
	 * Gets the livestock breeds.
	 *
	 * @return the livestock breeds
	 */
	public String getLivestockBreeds() {
		return LivestockBreeds;
	}
	
	/**
	 * Sets the livestock breeds.
	 *
	 * @param livestockBreeds the new livestock breeds
	 */
	public void setLivestockBreeds(String livestockBreeds) {
		LivestockBreeds = livestockBreeds;
	}
	
	/**
	 * Gets the livestock unit.
	 *
	 * @return the livestock unit
	 */
	public String getLivestockUnit() {
		return LivestockUnit;
	}
	
	/**
	 * Sets the livestock unit.
	 *
	 * @param livestockUnit the new livestock unit
	 */
	public void setLivestockUnit(String livestockUnit) {
		LivestockUnit = livestockUnit;
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
