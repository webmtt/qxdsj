package cma.cimiss2.dpc.decoder.bean.surf;

import java.util.Date;
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
	/**
	 * NO: 1  <br>
	 * nameCN: 测站编码  <br>
	 * unit: <br>
	 * BUFR FXY: V01301 <br>
	 * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值<br>
	 */
	private String StationCode;
	
	/**
	 * NO: 2  <br>
	 * nameCN: 测站名称  <br>
	 * unit: <br>
	 * BUFR FXY:  <br>
	 * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule: 不是入库字段<br>
	 */
	private String StationName;
	
	/**
	 * NO: 3  <br>
	 * nameCN: 资料日期时间  <br>
	 * unit: <br>
	 * BUFR FXY: V04001/V04002/V04003/V04004 <br>
	 * descriptionCN: <br>
     * decode rule:直接取值。<br>
     * field rule:使用java时间转化工具类进行转化,标准形式为：yyyyMMddHHmmss
	 */
	private Date ObservationTime;
	
	/**
	 * NO: 4  <br>
	 * nameCN: 深度含水率10cm  <br>
	 * unit: 100%  <br>
	 * BUFR FXY: V71655_010 <br>
	 * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值<br>
	 */
	private float RateOfWaterContent_10cm;
	
	/**
	 * NO: 5  <br>
	 * nameCN: 深度含水率20cm  <br>
	 * unit: 100% <br>
	 * BUFR FXY: V71655_020 <br>
	 * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值<br>
	 */
	private float RateOfWaterContent_20cm;
	
	/**
	 * NO: 6  <br>
	 * nameCN: 深度含水率30cm  <br>
	 * unit: 100%  <br>
	 * BUFR FXY: V71655_030 <br>
	 * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值<br>
	 */
	private float RateOfWaterContent_30cm;
	
	/**
	 * NO: 7  <br>
	 * nameCN: 100%  深度含水率40cm  <br>
	 * unit: <br>
	 * BUFR FXY: V71655_040 <br>
	 * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值<br>
	 */
	private float RateOfWaterContent_40cm;
	
	/**
	 * NO: 8  <br>
	 * nameCN: 深度含水60cm  <br>
	 * unit: 100% <br>
	 * BUFR FXY: V71655_060 <br>
	 * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值<br>
	 */
	private float RateOfWaterContent_60cm;
	
	/**
	 * NO: 9  <br>
	 * nameCN: 深度含水率80cm  <br>
	 * unit: 100%  <br>
	 * BUFR FXY: V71655_080 <br>
	 * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值<br>
	 */
	private float RateOfWaterContent_80cm;
	
	/**
	 * NO: 10  <br>
	 * nameCN: 深度含水率100cm  <br>
	 * unit: 100% <br>
	 * BUFR FXY: V71655_100 <br>
	 * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值<br>
	 */
	private float RateOfWaterContent_100cm;

	public String getStationCode() {
		return StationCode;
	}

	public void setStationCode(String stationCode) {
		StationCode = stationCode;
	}

	public String getStationName() {
		return StationName;
	}

	public void setStationName(String stationName) {
		StationName = stationName;
	}

	public Date getObservationTime() {
		return ObservationTime;
	}

	public void setObservationTime(Date observationTime) {
		ObservationTime = observationTime;
	}

	public float getRateOfWaterContent_10cm() {
		return RateOfWaterContent_10cm;
	}

	public void setRateOfWaterContent_10cm(float rateOfWaterContent_10cm) {
		RateOfWaterContent_10cm = rateOfWaterContent_10cm;
	}

	public float getRateOfWaterContent_20cm() {
		return RateOfWaterContent_20cm;
	}

	public void setRateOfWaterContent_20cm(float rateOfWaterContent_20cm) {
		RateOfWaterContent_20cm = rateOfWaterContent_20cm;
	}

	public float getRateOfWaterContent_30cm() {
		return RateOfWaterContent_30cm;
	}

	public void setRateOfWaterContent_30cm(float rateOfWaterContent_30cm) {
		RateOfWaterContent_30cm = rateOfWaterContent_30cm;
	}

	public float getRateOfWaterContent_40cm() {
		return RateOfWaterContent_40cm;
	}

	public void setRateOfWaterContent_40cm(float rateOfWaterContent_40cm) {
		RateOfWaterContent_40cm = rateOfWaterContent_40cm;
	}

	public float getRateOfWaterContent_60cm() {
		return RateOfWaterContent_60cm;
	}

	public void setRateOfWaterContent_60cm(float rateOfWaterContent_60cm) {
		RateOfWaterContent_60cm = rateOfWaterContent_60cm;
	}

	public float getRateOfWaterContent_80cm() {
		return RateOfWaterContent_80cm;
	}

	public void setRateOfWaterContent_80cm(float rateOfWaterContent_80cm) {
		RateOfWaterContent_80cm = rateOfWaterContent_80cm;
	}

	public float getRateOfWaterContent_100cm() {
		return RateOfWaterContent_100cm;
	}

	public void setRateOfWaterContent_100cm(float rateOfWaterContent_100cm) {
		RateOfWaterContent_100cm = rateOfWaterContent_100cm;
	}
}
