package cma.cimiss2.dpc.decoder.bean.sevp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * 全国城镇天气预报最终产品实体类。解析时，各要素值均从报文直接取值、入库时各字段直接赋值。
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《全国城镇天气预报最终报告数据要素表》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月24日 上午11:35:37   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class CityForecastLast{
	/**
     * NO: 1.1  <br>
     * nameCN: 区站号 <br>
     * unit: <br>
     * BUFR FXY: V01301 <br>
     * descriptionCN: 
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
	 * unit: <br>
	 * BUFR FXY: D_DATETIME <br>
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
	private int Validtime_Max;
	/**
	 * NO: 2.2 <br>
	 * nameCN:最大时效间隔  <br>
	 * unit: <br>
	 * BUFR FXY:  V04320_005_080<br>
	 * descriptionCN: 
	 */
	private int Validtime_Max_Intr;
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
	private Double V_PROD_NUM;
	 /**
	 * NO: 2.5 <br>
	 * nameCN: 预报时间 <br>
	 * unit: <br>
	 * BUFR FXY: V04001/V04002/V04003/V04004/V04005 <br>
	 * descriptionCN: 时间（年月日时分秒共计14位，不足位补“0”）。
	 */
	Date ForecastTime;
	
	List <CityForeList> Ele  = new ArrayList<>();
	
	
	public Date getForecastTime() {
		return ForecastTime;
	}
	public void setForecastTime(Date forecastTime) {
		ForecastTime = forecastTime;
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
	public List<CityForeList> getEle() {
		return Ele;
	}
	public void setEle(List<CityForeList> ele) {
		this.Ele = ele;
	}
	public void put(CityForeList ele){
		this.Ele.add(ele);
	}
	public int getValidtime_Max() {
		return Validtime_Max;
	}
	public void setValidtime_Max(int validtime_Max) {
		Validtime_Max = validtime_Max;
	}
	public int getValidtime_Max_Intr() {
		return Validtime_Max_Intr;
	}
	public void setValidtime_Max_Intr(int validtime_Max_Intr) {
		Validtime_Max_Intr = validtime_Max_Intr;
	}
}