package cma.cimiss2.dpc.quickqc.bean;

/**
 * 要素插值需要的x，y的序列
 */
public class InterpolateSeq {
	/**
	 * 根据日极值，插值计算24小时的小时极值, 极大时
	 */
	private double MaxHour;
	/**
	 * 根据日极值，插值计算24小时的小时极值, 极小时
	 */
	private double MinHour;
	/**
	 * 插值x序列
	 */
	private double[] x = {0, 0, 0, 0};
	
	/**
	 * 插值y_min序列
	 */
	private double[] yMin = {0, 0, 0, 0};

	/**
	 * 插值y_min序列
	 */
	private double[] yMax = {0, 0, 0, 0};

	public double[] getyMax() {
		return yMax;
	}

	public void setyMax(double[] yMax) {
		this.yMax = yMax;
	}

	public double[] getyMin() {
		return yMin;
	}

	public void setyMin(double[] yMin) {
		this.yMin = yMin;
	}

	public double[] getX() {
		return x;
	}

	public void setX(double[] x) {
		this.x = x;
	}

	public double getMaxHour() {
		return MaxHour;
	}

	public void setMaxHour(double maxHour) {
		MaxHour = maxHour;
	}

	public double getMinHour() {
		return MinHour;
	}

	public void setMinHour(double minHour) {
		MinHour = minHour;
	}

	
	
	
	
}
