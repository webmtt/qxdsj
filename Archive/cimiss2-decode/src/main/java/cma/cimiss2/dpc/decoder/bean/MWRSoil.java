package cma.cimiss2.dpc.decoder.bean;

import java.util.Date;
// TODO: Auto-generated Javadoc
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
       水利部土壤墒情资料实体类
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《水利部土壤墒情数据表》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月24日 下午4:45:18   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class MWRSoil {
	
	/** NO: 1  <br> nameCN: 测站编码  <br> unit: <br> BUFR FXY: V01301 <br> descriptionCN: <br> decode rule:直接取值<br> field rule:直接赋值<br>. */
	private String StationCode;
	
	/** NO: 2  <br> nameCN: 测站名称  <br> unit: <br> BUFR FXY:  <br> descriptionCN: <br> decode rule:直接取值<br> field rule: 不是入库字段<br>. */
	private String StationName;
	
	/** NO: 3  <br> nameCN: 资料日期时间  <br> unit: <br> BUFR FXY: V04001/V04002/V04003/V04004 <br> descriptionCN: <br> decode rule:直接取值。<br> field rule:使用java时间转化工具类进行转化,标准形式为：yyyyMMddHHmmss. */
	private Date ObservationTime;
	
	/** NO: 4  <br> nameCN: 深度含水率10cm  <br> unit: 100%  <br> BUFR FXY: V71655_010 <br> descriptionCN: <br> decode rule:直接取值<br> field rule:直接赋值<br>. */
	private float RateOfWaterContent_10cm;
	
	/** NO: 5  <br> nameCN: 深度含水率20cm  <br> unit: 100% <br> BUFR FXY: V71655_020 <br> descriptionCN: <br> decode rule:直接取值<br> field rule:直接赋值<br>. */
	private float RateOfWaterContent_20cm;
	
	/** NO: 6  <br> nameCN: 深度含水率30cm  <br> unit: 100%  <br> BUFR FXY: V71655_030 <br> descriptionCN: <br> decode rule:直接取值<br> field rule:直接赋值<br>. */
	private float RateOfWaterContent_30cm;
	
	/** NO: 7  <br> nameCN: 100%  深度含水率40cm  <br> unit: <br> BUFR FXY: V71655_040 <br> descriptionCN: <br> decode rule:直接取值<br> field rule:直接赋值<br>. */
	private float RateOfWaterContent_40cm;
	
	/** NO: 8  <br> nameCN: 深度含水60cm  <br> unit: 100% <br> BUFR FXY: V71655_060 <br> descriptionCN: <br> decode rule:直接取值<br> field rule:直接赋值<br>. */
	private float RateOfWaterContent_60cm;
	
	/** NO: 9  <br> nameCN: 深度含水率80cm  <br> unit: 100%  <br> BUFR FXY: V71655_080 <br> descriptionCN: <br> decode rule:直接取值<br> field rule:直接赋值<br>. */
	private float RateOfWaterContent_80cm;
	
	/** NO: 10  <br> nameCN: 深度含水率100cm  <br> unit: 100% <br> BUFR FXY: V71655_100 <br> descriptionCN: <br> decode rule:直接取值<br> field rule:直接赋值<br>. */
	private float RateOfWaterContent_100cm;

	/**
	 * Gets the station code.
	 *
	 * @return the station code
	 */
	public String getStationCode() {
		return StationCode;
	}

	/**
	 * Sets the station code.
	 *
	 * @param stationCode the new station code
	 */
	public void setStationCode(String stationCode) {
		StationCode = stationCode;
	}

	/**
	 * Gets the station name.
	 *
	 * @return the station name
	 */
	public String getStationName() {
		return StationName;
	}

	/**
	 * Sets the station name.
	 *
	 * @param stationName the new station name
	 */
	public void setStationName(String stationName) {
		StationName = stationName;
	}

	/**
	 * Gets the observation time.
	 *
	 * @return the observation time
	 */
	public Date getObservationTime() {
		return ObservationTime;
	}

	/**
	 * Sets the observation time.
	 *
	 * @param observationTime the new observation time
	 */
	public void setObservationTime(Date observationTime) {
		ObservationTime = observationTime;
	}

	/**
	 * Gets the rate of water content 10 cm.
	 *
	 * @return the rate of water content 10 cm
	 */
	public float getRateOfWaterContent_10cm() {
		return RateOfWaterContent_10cm;
	}

	/**
	 * Sets the rate of water content 10 cm.
	 *
	 * @param rateOfWaterContent_10cm the new rate of water content 10 cm
	 */
	public void setRateOfWaterContent_10cm(float rateOfWaterContent_10cm) {
		RateOfWaterContent_10cm = rateOfWaterContent_10cm;
	}

	/**
	 * Gets the rate of water content 20 cm.
	 *
	 * @return the rate of water content 20 cm
	 */
	public float getRateOfWaterContent_20cm() {
		return RateOfWaterContent_20cm;
	}

	/**
	 * Sets the rate of water content 20 cm.
	 *
	 * @param rateOfWaterContent_20cm the new rate of water content 20 cm
	 */
	public void setRateOfWaterContent_20cm(float rateOfWaterContent_20cm) {
		RateOfWaterContent_20cm = rateOfWaterContent_20cm;
	}

	/**
	 * Gets the rate of water content 30 cm.
	 *
	 * @return the rate of water content 30 cm
	 */
	public float getRateOfWaterContent_30cm() {
		return RateOfWaterContent_30cm;
	}

	/**
	 * Sets the rate of water content 30 cm.
	 *
	 * @param rateOfWaterContent_30cm the new rate of water content 30 cm
	 */
	public void setRateOfWaterContent_30cm(float rateOfWaterContent_30cm) {
		RateOfWaterContent_30cm = rateOfWaterContent_30cm;
	}

	/**
	 * Gets the rate of water content 40 cm.
	 *
	 * @return the rate of water content 40 cm
	 */
	public float getRateOfWaterContent_40cm() {
		return RateOfWaterContent_40cm;
	}

	/**
	 * Sets the rate of water content 40 cm.
	 *
	 * @param rateOfWaterContent_40cm the new rate of water content 40 cm
	 */
	public void setRateOfWaterContent_40cm(float rateOfWaterContent_40cm) {
		RateOfWaterContent_40cm = rateOfWaterContent_40cm;
	}

	/**
	 * Gets the rate of water content 60 cm.
	 *
	 * @return the rate of water content 60 cm
	 */
	public float getRateOfWaterContent_60cm() {
		return RateOfWaterContent_60cm;
	}

	/**
	 * Sets the rate of water content 60 cm.
	 *
	 * @param rateOfWaterContent_60cm the new rate of water content 60 cm
	 */
	public void setRateOfWaterContent_60cm(float rateOfWaterContent_60cm) {
		RateOfWaterContent_60cm = rateOfWaterContent_60cm;
	}

	/**
	 * Gets the rate of water content 80 cm.
	 *
	 * @return the rate of water content 80 cm
	 */
	public float getRateOfWaterContent_80cm() {
		return RateOfWaterContent_80cm;
	}

	/**
	 * Sets the rate of water content 80 cm.
	 *
	 * @param rateOfWaterContent_80cm the new rate of water content 80 cm
	 */
	public void setRateOfWaterContent_80cm(float rateOfWaterContent_80cm) {
		RateOfWaterContent_80cm = rateOfWaterContent_80cm;
	}

	/**
	 * Gets the rate of water content 100 cm.
	 *
	 * @return the rate of water content 100 cm
	 */
	public float getRateOfWaterContent_100cm() {
		return RateOfWaterContent_100cm;
	}

	/**
	 * Sets the rate of water content 100 cm.
	 *
	 * @param rateOfWaterContent_100cm the new rate of water content 100 cm
	 */
	public void setRateOfWaterContent_100cm(float rateOfWaterContent_100cm) {
		RateOfWaterContent_100cm = rateOfWaterContent_100cm;
	}
}
