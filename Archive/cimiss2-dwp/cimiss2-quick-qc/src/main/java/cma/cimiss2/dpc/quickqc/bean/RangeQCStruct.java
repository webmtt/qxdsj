package cma.cimiss2.dpc.quickqc.bean;

public class RangeQCStruct {
	/**
	 * 标准差
	 */
	private double stdev;
	
	/**
	 * 范围上限值
	 * */
	private double range_high; 
	
	/**
	 * 范围下限值
	  */
	private double range_low;

	public double getStdev() {
		return stdev;
	}

	public void setStdev(double stdev) {
		this.stdev = stdev;
	}

	public double getRange_high() {
		return range_high;
	}

	public void setRange_high(double range_high) {
		this.range_high = range_high;
	}

	public double getRange_low() {
		return range_low;
	}

	public void setRange_low(double range_low) {
		this.range_low = range_low;
	} 
	
}
