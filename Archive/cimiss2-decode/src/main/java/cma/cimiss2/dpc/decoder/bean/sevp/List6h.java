package cma.cimiss2.dpc.decoder.bean.sevp;
/**
 * 
 * <br>
 * @Title:  List6h.java
 * @Package cma.cimiss2.dpc.decoder.bean.sevp.forcast_6h
 * @Description:    TODO(大城市逐6小时精细化预报资料实体类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月6日 上午11:13:05   maxiyue   Initial creation.
 * </pre>
 * 
 * @author maxiyue
 */
public class List6h {
	/**
	 * NO: 1.1 <br>
	 * nameCN:预报时效<br>
	 * unit: <br>
	 * BUFR FXY:V04320  <br>
	 * descriptionCN: 
	 */
	private Double Validtime;
	/**
	 * NO: 1.2 <br>
	 * nameCN:6小时内天气现象<br>
	 * unit: <br>
	 * BUFR FXY:V20312_06  <br>
	 * descriptionCN: 
	 */
	private Double WEP_Past_6h;
	/**
	 * NO: 1.3 <br>
	 * nameCN:6小时内最高气温<br>
	 * unit: <br>
	 * BUFR FXY:V12011_06  <br>
	 * descriptionCN: 
	 */
	private Double TEM_Max_6h;
	/**
	 * NO: 1.4 <br>
	 * nameCN:6小时内最低气温<br>
	 * unit: <br>
	 * BUFR FXY:V12012_06  <br>
	 * descriptionCN: 
	 */
	private Double TEM_Min_6h;
	/**
	 * NO: 1.5 <br>
	 * nameCN:6小时内的盛行风向<br>
	 * unit: <br>
	 * BUFR FXY:V11001_06  <br>
	 * descriptionCN: 
	 */
	private Double WIN_PD_6h;
	/**
	 * NO: 1.6 <br>
	 * nameCN:6小时内的最大风速<br>
	 * unit: <br>
	 * BUFR FXY:V11040_06 <br>
	 * descriptionCN: 
	 */
	private Double WIN_S_Max_6h;
	/**
	 * NO: 1.7 <br>
	 * nameCN:6小时累计降水量<br>
	 * unit: <br>
	 * BUFR FXY:V13021 <br>
	 * descriptionCN: 
	 */
	private Double PRE_6h;
	public Double getValidtime() {
		return Validtime;
	}
	public void setValidtime(Double validtime) {
		Validtime = validtime;
	}
	public Double getWEP_Past_6h() {
		return WEP_Past_6h;
	}
	public void setWEP_Past_6h(Double wEP_Past_6h) {
		WEP_Past_6h = wEP_Past_6h;
	}
	public Double getTEM_Max_6h() {
		return TEM_Max_6h;
	}
	public void setTEM_Max_6h(Double tEM_Max_6h) {
		TEM_Max_6h = tEM_Max_6h;
	}
	public Double getTEM_Min_6h() {
		return TEM_Min_6h;
	}
	public void setTEM_Min_6h(Double tEM_Min_6h) {
		TEM_Min_6h = tEM_Min_6h;
	}
	public Double getWIN_PD_6h() {
		return WIN_PD_6h;
	}
	public void setWIN_PD_6h(Double wIN_PD_6h) {
		WIN_PD_6h = wIN_PD_6h;
	}
	public Double getWIN_S_Max_6h() {
		return WIN_S_Max_6h;
	}
	public void setWIN_S_Max_6h(Double wIN_S_Max_6h) {
		WIN_S_Max_6h = wIN_S_Max_6h;
	}
	public Double getPRE_6h() {
		return PRE_6h;
	}
	public void setPRE_6h(Double pRE_6h) {
		PRE_6h = pRE_6h;
	}
}
