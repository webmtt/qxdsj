package cma.cimiss2.dpc.quickqc.bean;

/**
* *******************************************************************************************<br>
* <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
* *******************************************************************************************<br>
* <b>Description: 要素允许的最大最小值   以及阈值的定义</b><br> 
* @author wuzuoqiang 
* @version 1.0
* @Note
* <b>ProjectName:</b> cimiss2-dwp-qc
* <br><b>PackageName:</b> org.cimiss2.dwp.bean
* <br><b>ClassName:</b> MaxMinValues
* <br><b>Date:</b> 2019年5月11日 下午2:21:26
 */
public class MaxMinValues {
	// 要素最小值
	private double min_value;
	// 要素最大值
	private double max_value;
	// 最小超阈值
	private double min_exceeding_threshold;
	// 最大超阈值
	private double max_exceeding_threshold;
	// 超阈值
	private double exceeding_threshold;
	public double getMinValue() {
		return min_value;
	}
	public void setMinValue(double min_value) {
		this.min_value = min_value;
	}
	public double getMaxValue() {
		return max_value;
	}
	public void setMaxValue(double max_value) {
		this.max_value = max_value;
	}
	public double getMin_exceeding_threshold() {
		return min_exceeding_threshold;
	}
	public void setMin_exceeding_threshold(double min_exceeding_threshold) {
		this.min_exceeding_threshold = min_exceeding_threshold;
	}
	public double getMax_exceeding_threshold() {
		return max_exceeding_threshold;
	}
	public void setMax_exceeding_threshold(double max_exceeding_threshold) {
		this.max_exceeding_threshold = max_exceeding_threshold;
	}
	public double getExceedingThreshold() {
		return exceeding_threshold;
	}
	public void setExceedingThreshold(double exceeding_threshold) {
		this.exceeding_threshold = exceeding_threshold;
	}
	
	
	
}
