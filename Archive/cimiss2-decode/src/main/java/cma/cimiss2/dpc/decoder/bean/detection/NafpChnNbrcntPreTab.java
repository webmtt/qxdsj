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
public class NafpChnNbrcntPreTab extends RadiMulChn{
	
	
	/**
	 * @Fields fbs : FBS评分
	 */
	@Column(value = "V_FBS")
	private String fbs ;
	
	/**
	 * @Fields fss : FSS评分
	 */
	@Column(value = "V_FSS")
	private String fss ;
	
	/**
	 * @Fields afss : AFSS评分
	 */
	@Column(value = "V_AFSS")
	private String afss ;

	public String getFbs() {
		return fbs;
	}

	public void setFbs(String fbs) {
		this.fbs = fbs;
	}

	public String getFss() {
		return fss;
	}

	public void setFss(String fss) {
		this.fss = fss;
	}

	public String getAfss() {
		return afss;
	}

	public void setAfss(String afss) {
		this.afss = afss;
	}

	
	
	
	
}
