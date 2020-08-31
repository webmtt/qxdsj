package cma.cimiss2.dpc.decoder.bean.sevp;

import java.util.Date;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
	台风实况与预报数据键表实体类
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《台风实况与预报要素资料数据键表》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月24日 上午11:39:32   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class TyphKey {
	/**
	 * 资料观测时间
	 */
	private Date observationTime;
	/**
	 * 编报(加工)中心	V_CCCC
	 */
	private String reportCenter; 
	/**
	 * 编报中心代号	V01035
	 */
	private int reportCenterCode; 
	/**
	 * 台风名	V_TYPH_NAME	VARCHAR2(20)
	 */
	private String typhName = ""; 
	/**
	 * 台风等级	V01333	NUMBER(2)
	 */
	private String typhLevel = "999998"; 
	/**
	 * 国内编号	V01330	NUMBER(6)
	 */
	private int internalCode = 999998; 
	/**
	 * 国际编号	V01332	NUMBER(6)
	 */
	private int internationalCode = 999998; 
	/**
	 * 产品类型	V01398	NUMBER(6)
	 */
	private int productType = 999998; 
	/**
	 * 预报时效个数  V04320_041	NUMBER(2)
	 */
	private int numOfForecastEfficiency = 0; 
	/**
	 * 资料更正报标识 V_BBB
	 */
	private String V_BBB = "000";
	/**
	 * 报告类别
	 */
	private String V_TT = ""; 
	/**
	 * 地理编码
	 */
	private String V_AA = ""; 
	/**
	 * 公报编码
	 */
	private  String V_II = "";  
		
	public String getReportCenter() {
		return reportCenter;
	}
	public void setReportCenter(String reportCenter) {
		this.reportCenter = reportCenter;
	}
	public int getReportCenterCode() {
		return reportCenterCode;
	}
	public void setReportCenterCode(int reportCenterCode) {
		this.reportCenterCode = reportCenterCode;
	}
	public String getTyphName() {
		return typhName;
	}
	public void setTyphName(String typhName) {
		this.typhName = typhName;
	}
	public String getTyphLevel() {
		return typhLevel;
	}
	public void setTyphLevel(String typhLevel) {
		this.typhLevel = typhLevel;
	}
	public Date getObservationTime() {
		return observationTime;
	}
	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}public int getInternalCode() {
		return internalCode;
	}
	public void setInternalCode(int internalCode) {
		this.internalCode = internalCode;
	}
	public int getInternationalCode() {
		return internationalCode;
	}
	public void setInternationalCode(int internationalCode) {
		this.internationalCode = internationalCode;
	}
	public int getNumOfForecastEfficiency() {
		return numOfForecastEfficiency;
	}
	public void setNumOfForecastEfficiency(int numOfForecastEfficiency) {
		this.numOfForecastEfficiency = numOfForecastEfficiency;
	}
	public int getProductType() {
		return productType;
	}
	public void setProductType(int productType) {
		this.productType = productType;
	}
	public String getV_BBB() {
		return V_BBB;
	}
	public void setV_BBB(String v_BBB) {
		V_BBB = v_BBB;
	}
	public String getV_TT() {
		return V_TT;
	}
	public void setV_TT(String v_TT) {
		V_TT = v_TT;
	}
	public String getV_AA() {
		return V_AA;
	}
	public void setV_AA(String v_AA) {
		V_AA = v_AA;
	}
	public String getV_II() {
		return V_II;
	}
	public void setV_II(String v_II) {
		V_II = v_II;
	}
}
