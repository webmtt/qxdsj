package cma.cimiss2.dpc.decoder.bean.detection;


import cma.cimiss2.dpc.decoder.annotation.Column;
/**
 * 
 * <br>
 * @Title:  RadiMulChnPenTab.java
 * @Package cma.cimiss2.dpc.decoder.bean.radi.sads
 * @Description:    (地面要素MPR检测评估结果实体类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月20日 下午2:18:51   maxiyue   Initial creation.
 * </pre>
 * 
 * @author maxiyue
 */
public class RadiMulChnMonTab extends RadiMulChn{
	
	
	/**
	 * @Fields total : 配对样本总数
	 */
	@Column(value = "V_TOTAL")
	private Double total ;
	
	/**
	 * @Fields fyOy : 预报对和观测对数量NA
	 */
	@Column(value = "V_FYOY")
	private Double fyOy ;
	
	/**
	 * @Fields fyOn : 预报对和观测错数量
	 */
	@Column(value = "V_FYON")
	private Double fyOn ;
	
	/**
	 * @Fields fnOy : 预报错和观测对数量
	 */
	@Column(value = "V_FNOY")
	private Double fnOy ;
	
	/**
	 * @Fields fnOy : 预报错和观测错数量
	 */
	@Column(value = "V_FNON")
	private Double fnOn ;
	

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getFyOy() {
		return fyOy;
	}

	public void setFyOy(Double fyOy) {
		this.fyOy = fyOy;
	}

	public Double getFyOn() {
		return fyOn;
	}

	public void setFyOn(Double fyOn) {
		this.fyOn = fyOn;
	}

	public Double getFnOy() {
		return fnOy;
	}

	public void setFnOy(Double fnOy) {
		this.fnOy = fnOy;
	}

	public Double getFnOn() {
		return fnOn;
	}

	public void setFnOn(Double fnOn) {
		this.fnOn = fnOn;
	}

	
	
	
	
}
