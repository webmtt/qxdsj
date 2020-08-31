package cma.cimiss2.dpc.decoder.bean.sevp;

import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;

/**
 * 
 * <br>
 * @Title:  UvMonitorForecastproduct.java
 * @Package cma.cimiss2.dpc.decoder.bean.uv
 * @Description:    TODO(紫外线监测与预报产品资料解码实体类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月12日 上午9:33:16   maxiyue   Initial creation.
 * </pre>
 * 
 * @author maxiyue
 */
public class UvMonitorForecastproduct extends AgmeReportHeader{
	
	/**
	 * NO: 1.1  <br>
	 * nameCN: 区站号 <br>
	 * unit: <br>
	 * description: 区站号,5位数字或第1位为字母，第2-5位为数字
	 */
	String stationNumberChina;
	/**
	  * nameCN: <br>
	  * unit:<br>
	  * descriptionCN: 预报值
	  */
	double uvPredictedValue;
	/**
	  * nameCN: <br>
	  * unit:<br>
	  * descriptionCN:观测平均值
	  */
	double uvObserveAverageValue;
	/**
	  * nameCN: <br>
	  * unit:<br>
	  * descriptionCN:日观测最大值
	  */
	double uvDailyObserveMaximum;
	public String getStationNumberChina() {
		return stationNumberChina;
	}
	public void setStationNumberChina(String stationNumberChina) {
		this.stationNumberChina = stationNumberChina;
	}
	public double getUvPredictedValue() {
		return uvPredictedValue;
	}
	public void setUvPredictedValue(double uvPredictedValue) {
		this.uvPredictedValue = uvPredictedValue;
	}
	public double getUvObserveAverageValue() {
		return uvObserveAverageValue;
	}
	public void setUvObserveAverageValue(double uvObserveAverageValue) {
		this.uvObserveAverageValue = uvObserveAverageValue;
	}
	public double getUvDailyObserveMaximum() {
		return uvDailyObserveMaximum;
	}
	public void setUvDailyObserveMaximum(double uvDailyObserveMaximum) {
		this.uvDailyObserveMaximum = uvDailyObserveMaximum;
	}
}
