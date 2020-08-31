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
public class RadiMulChnTenTab extends RadiMulChn{
	
	
	/**
	 * @Fields me : 平均误差
	 */
	@Column(value = "V_ME")
	private Double me ;
	
	/**
	 * @Fields mae : 平均绝对误差
	 */
	@Column(value = "V_MAE")
	private Double mae ;
	
	/**
	 * @Fields mse : 均方误差
	 */
	@Column(value = "V_MSE")
	private Double mse ;
	
	/**
	 * @Fields rmse : 均方根误差
	 */
	@Column(value = "V_RMSE")
	private Double rmse ;
	
	/**
	 * @Fields me2 : Bias偏差
	 */
	@Column(value = "V_ME2")
	private Double me2 ;

	public Double getMe() {
		return me;
	}

	public void setMe(Double me) {
		this.me = me;
	}

	public Double getMae() {
		return mae;
	}

	public void setMae(Double mae) {
		this.mae = mae;
	}

	public Double getMse() {
		return mse;
	}

	public void setMse(Double mse) {
		this.mse = mse;
	}

	public Double getRmse() {
		return rmse;
	}

	public void setRmse(Double rmse) {
		this.rmse = rmse;
	}

	public Double getMe2() {
		return me2;
	}

	public void setMe2(Double me2) {
		this.me2 = me2;
	}

	
	
	
}
