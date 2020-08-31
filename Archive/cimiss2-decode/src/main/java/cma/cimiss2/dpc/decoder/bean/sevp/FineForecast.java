package cma.cimiss2.dpc.decoder.bean.sevp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



/**
 * 
 * -------------------------------------------------------------------------------
 * <br>
 * @Title:  fine_forecast.java   
 * @Package cma.cimiss2.dpc.decoder.bean.sevp.fine_forecast   
 * @Description:    TODO(全国城镇精细化预报产品解码实体类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月1日 下午2:07:53   maxiyue    Initial creation.
 * </pre>
 * 
 * @author maxiyue
 *---------------------------------------------------------------------------------
 */

public class FineForecast {
	/**
     * NO: 1.1  <br>
     * nameCN: 区站号 <br>
     * unit: <br>
     * BUFR FXY: V01301 <br>
     * descriptionCN: 5位数字或第1位为字母，第2-5位为数字
     */
    String stationNumberChina;
    /**
     * NO: 1.2 <br>
     * nameCN: 纬度 <br>
     * unit: 度 <br>
     * BUFR FXY: V05001 <br>
     * descriptionCN:
     */
    Double latitude;
    /**
     * NO: 1.3  <br>
     * nameCN: 经度 <br>
     * unit: 度 <br>
     * BUFR FXY: V06001 <br>
     * descriptionCN:
     */
    Double longitude;
    /**
     * NO: 1.4  <br>
     * nameCN: 观测场拔海高度 <br>
     * unit: 1m <br>
     * BUFR FXY: V07001 <br>
     * descriptionCN: 若低于海平面，为负值
     */
    Double heightOfSationGroundAboveMeanSeaLevel;
    /**
	 * NO: 1.6  <br>
	 * nameCN: 观测时间 <br>
	 * unit: 日期<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 时间（年月日时分秒共计14位，不足位补“0”）。
	 */
	Date observationTime;
	/**
	 * NO: 1.7  <br>
	 * nameCN: 更正标志  <br>
	 * unit: <br>
	 * BUFR FXY: V_BBB <br>
	 * descriptionCN: 
	 */
	private String correctSign= "000";
	/**
	 * NO: 1.8  <br>
	 * nameCN:编报(加工)中心  <br>
	 * unit: <br>
	 * BUFR FXY: V_CCCC <br>
	 * descriptionCN: 
	 */
	private String Bul_Center;
	/**
	 * NO: 1.9  <br>
	 * nameCN:产品描述  <br>
	 * unit: <br>
	 * BUFR FXY: V_PROD_DESC <br>
	 * descriptionCN: 
	 */
	private String PROD_DESC;
	/**
	 * NO: 2.0  <br>
	 * nameCN:产品代码  <br>
	 * unit: <br>
	 * BUFR FXY: V_PROD_CODE <br>
	 * descriptionCN: 
	 */
	private String Prod_Code;
	/**
	 * NO: 2.1 <br>
	 * nameCN:最大预报时效  <br>
	 * unit: <br>
	 * BUFR FXY:  V04320_005<br>
	 * descriptionCN: 
	 */
	private Double Validtime_Max;
	/**
	 * NO: 2.2 <br>
	 * nameCN:最大时效间隔  <br>
	 * unit: <br>
	 * BUFR FXY:  V04320_005_080<br>
	 * descriptionCN: 
	 */
	private Double Validtime_Max_Intr;
	/**
	 * NO: 2.3 <br>
	 * nameCN:预报时效个数<br>
	 * unit: <br>
	 * BUFR FXY: V04320_041 <br>
	 * descriptionCN: 
	 */
	private Double Validtime_Count;
	/**
	 * NO: 2.4 <br>
	 * nameCN:预报产品个数<br>
	 * unit: <br>
	 * BUFR FXY: V_PROD_NUM <br>
	 * descriptionCN: 
	 */
	 /**
	 * NO:   <br>
	 * nameCN: 预报时间 <br>
	 * unit: 日期<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 时间（年月日时分秒共计14位，不足位补“0”）。
	 */
	Date ForecastTime;
	public Date getForecastTime() {
		return ForecastTime;
	}
	public void setForecastTime(Date forecastTime) {
		ForecastTime = forecastTime;
	}
	private Double V_PROD_NUM;
	List <FineList> fine  = new ArrayList<>();
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
	public Date getObservationTime() {
		return observationTime;
	}
	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}
	public String getCorrectSign() {
		return correctSign;
	}
	public void setCorrectSign(String correctSign) {
		this.correctSign = correctSign;
	}
	public String getBul_Center() {
		return Bul_Center;
	}
	public void setBul_Center(String bul_Center) {
		Bul_Center = bul_Center;
	}
	public String getPROD_DESC() {
		return PROD_DESC;
	}
	public void setPROD_DESC(String pROD_DESC) {
		PROD_DESC = pROD_DESC;
	}
	public String getProd_Code() {
		return Prod_Code;
	}
	public void setProd_Code(String prod_Code) {
		Prod_Code = prod_Code;
	}
	public Double getValidtime_Max() {
		return Validtime_Max;
	}
	public void setValidtime_Max(Double validtime_Max) {
		Validtime_Max = validtime_Max;
	}
	public Double getValidtime_Max_Intr() {
		return Validtime_Max_Intr;
	}
	public void setValidtime_Max_Intr(Double validtime_Max_Intr) {
		Validtime_Max_Intr = validtime_Max_Intr;
	}
	public Double getValidtime_Count() {
		return Validtime_Count;
	}
	public void setValidtime_Count(Double validtime_Count) {
		Validtime_Count = validtime_Count;
	}
	public Double getV_PROD_NUM() {
		return V_PROD_NUM;
	}
	public void setV_PROD_NUM(Double v_PROD_NUM) {
		V_PROD_NUM = v_PROD_NUM;
	}
	public List<FineList> getFine() {
		return fine;
	}
	public void setFine(List<FineList> fine) {
		this.fine = fine;
	}
	public void put(FineList fine){
		this.fine.add(fine);
	}
	
	
	
	
	
	

}
