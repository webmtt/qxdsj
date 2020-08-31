package cma.cimiss2.dpc.decoder.bean.sevp;

/**
 * 
 * <br>
 * @Title:  CityForeList.java   
 * @Package cma.cimiss2.dpc.decoder.bean.sevp.city_forecast_last  
 * @Description:    TODO(全国城镇天气预报最终产品)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月21日 下午6:16:23   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 *
 *
 */
public class CityForeList {
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
	 * nameCN:温度/气温<br>
	 * unit: <br>
	 * BUFR FXY:V12001  <br>
	 * descriptionCN: 
	 */
	private Double TEM;
	/**
	 * NO: 1.3 <br>
	 * nameCN:相对湿度<br>
	 * unit: <br>
	 * BUFR FXY: V13003 <br>
	 * descriptionCN: 
	 */
	private Double RHU;
	/**
	 * NO: 1.4 <br>
	 * nameCN:风向<br>
	 * unit: <br>
	 * BUFR FXY: V11001 <br>
	 * descriptionCN: 
	 */
	private Double WIN_D;
	/**
	 * NO: 1.5 <br>
	 * nameCN:风速<br>
	 * unit: <br>
	 * BUFR FXY:V11002  <br>
	 * descriptionCN: 
	 */
	private Double WIN_S;
	/**
	 * NO: 1.6 <br>
	 * nameCN:气压<br>
	 * unit: <br>
	 * BUFR FXY: V10004 <br>
	 * descriptionCN: 
	 */
	private Double PRS;
	/**
	 * NO: 1.7 <br>
	 * nameCN:可降水分（预报降水量）<br>
	 * unit: <br>
	 * BUFR FXY: V13016 <br>
	 * descriptionCN: 
	 */
	private Double PRE_PRE_Fore;
	/**
	 * NO: 1.8 <br>
	 * nameCN:总云量<br>
	 * unit: <br>
	 * BUFR FXY: V20010 <br>
	 * descriptionCN: 
	 */
	private Double CLO_Cov;
	/**
	 * NO: 1.9 <br>
	 * nameCN:低云量<br>
	 * unit: <br>
	 * BUFR FXY: V20051 <br>
	 * descriptionCN: 
	 */
	private Double CLO_Cov_Low;
	/**
	 * NO: 2.0 <br>
	 * nameCN:天气现象<br>
	 * unit: <br>
	 * BUFR FXY: V20312 <br>
	 * descriptionCN: 
	 */
	private Double WEP;
	/**
	 * NO: 2.1 <br>
	 * nameCN:水平能见度(人工)<br>
	 * unit: <br>
	 * BUFR FXY: V20001 <br>
	 * descriptionCN: 
	 */
	private Double VIS;
	/**
	 * NO: 2.2 <br>
	 * nameCN:未来24小时最高气温<br>
	 * unit: <br>
	 * BUFR FXY: V12016 <br>
	 * descriptionCN: 
	 */
	private Double TEM_Max_24h;
	/**
	 * NO: 2.3 <br>
	 * nameCN:未来24小时最低气温<br>
	 * unit: <br>
	 * BUFR FXY: V12017 <br>
	 * descriptionCN: 
	 */
	private Double TEM_Min_24h;
	/**
	 * NO: 2.4 <br>
	 * nameCN:24小时最大相对湿度<br>
	 * unit: <br>
	 * BUFR FXY: V13008_24 <br>
	 * descriptionCN: 
	 */
	private Double RHU_Max_24h;
	/**
	 * NO: 2.5 <br>
	 * nameCN:24小时最小相对湿度<br>
	 * unit: <br>
	 * BUFR FXY: V13007_24 <br>
	 * descriptionCN: 
	 */
	private Double RHU_Min_24h;
	/**
	 * NO: 2.6 <br>
	 * nameCN:未来24小时降水量<br>
	 * unit: <br>
	 * BUFR FXY: V13023 <br>
	 * descriptionCN: 
	 */
	private Double PRE_24h;
	/**
	 * NO: 2.7 <br>
	 * nameCN:未来12小时降水量<br>
	 * unit: <br>
	 * BUFR FXY:V13022  <br>
	 * descriptionCN: 
	 */
	private Double PRE_12h;
	/**
	 * NO: 2.8 <br>
	 * nameCN:12小时内总云量<br>
	 * unit: <br>
	 * BUFR FXY: V20010_12 <br>
	 * descriptionCN: 
	 */
	private Double CLO_Cov_12h;
	/**
	 * NO: 2.9 <br>
	 * nameCN:12小时内低云量<br>
	 * unit: <br>
	 * BUFR FXY:V20051_12  <br>
	 * descriptionCN: 
	 */
	private Double CLO_Cov_Low_12h;
	/**
	 * NO: 3.0 <br>
	 * nameCN:12小时内天气现象<br>
	 * unit: <br>
	 * BUFR FXY:V20312_12  <br>
	 * descriptionCN: 
	 */
	private Double WEP_Past_12h;
	/**
	 * NO: 3.1 <br>
	 * nameCN:12小时内盛行风向<br>
	 * unit: <br>
	 * BUFR FXY: V11001_12 <br>
	 * descriptionCN: 
	 */
	private Double WIN_PD_12h;
	/**
	 * NO: 3.2 <br>
	 * nameCN:12小时内盛行风向<br>
	 * unit: <br>
	 * BUFR FXY: V11040_12 <br>
	 * descriptionCN: 
	 */
	private Double WIN_S_Max_12h;
	public Double getValidtime() {
		return Validtime;
	}
	public void setValidtime(Double validtime) {
		Validtime = validtime;
	}
	public Double getTEM() {
		return TEM;
	}
	public void setTEM(Double tEM) {
		TEM = tEM;
	}
	public Double getRHU() {
		return RHU;
	}
	public void setRHU(Double rHU) {
		RHU = rHU;
	}
	public Double getWIN_D() {
		return WIN_D;
	}
	public void setWIN_D(Double wIN_D) {
		WIN_D = wIN_D;
	}
	public Double getWIN_S() {
		return WIN_S;
	}
	public void setWIN_S(Double wIN_S) {
		WIN_S = wIN_S;
	}
	public Double getPRS() {
		return PRS;
	}
	public void setPRS(Double pRS) {
		PRS = pRS;
	}
	public Double getPRE_PRE_Fore() {
		return PRE_PRE_Fore;
	}
	public void setPRE_PRE_Fore(Double pRE_PRE_Fore) {
		PRE_PRE_Fore = pRE_PRE_Fore;
	}
	public Double getCLO_Cov() {
		return CLO_Cov;
	}
	public void setCLO_Cov(Double cLO_Cov) {
		CLO_Cov = cLO_Cov;
	}
	public Double getCLO_Cov_Low() {
		return CLO_Cov_Low;
	}
	public void setCLO_Cov_Low(Double cLO_Cov_Low) {
		CLO_Cov_Low = cLO_Cov_Low;
	}
	public Double getWEP() {
		return WEP;
	}
	public void setWEP(Double wEP) {
		WEP = wEP;
	}
	public Double getVIS() {
		return VIS;
	}
	public void setVIS(Double vIS) {
		VIS = vIS;
	}
	public Double getTEM_Max_24h() {
		return TEM_Max_24h;
	}
	public void setTEM_Max_24h(Double tEM_Max_24h) {
		TEM_Max_24h = tEM_Max_24h;
	}
	public Double getTEM_Min_24h() {
		return TEM_Min_24h;
	}
	public void setTEM_Min_24h(Double tEM_Min_24h) {
		TEM_Min_24h = tEM_Min_24h;
	}
	public Double getRHU_Max_24h() {
		return RHU_Max_24h;
	}
	public void setRHU_Max_24h(Double rHU_Max_24h) {
		RHU_Max_24h = rHU_Max_24h;
	}
	public Double getRHU_Min_24h() {
		return RHU_Min_24h;
	}
	public void setRHU_Min_24h(Double rHU_Min_24h) {
		RHU_Min_24h = rHU_Min_24h;
	}
	public Double getPRE_24h() {
		return PRE_24h;
	}
	public void setPRE_24h(Double pRE_24h) {
		PRE_24h = pRE_24h;
	}
	public Double getPRE_12h() {
		return PRE_12h;
	}
	public void setPRE_12h(Double pRE_12h) {
		PRE_12h = pRE_12h;
	}
	public Double getCLO_Cov_12h() {
		return CLO_Cov_12h;
	}
	public void setCLO_Cov_12h(Double cLO_Cov_12h) {
		CLO_Cov_12h = cLO_Cov_12h;
	}
	public Double getCLO_Cov_Low_12h() {
		return CLO_Cov_Low_12h;
	}
	public void setCLO_Cov_Low_12h(Double cLO_Cov_Low_12h) {
		CLO_Cov_Low_12h = cLO_Cov_Low_12h;
	}
	public Double getWEP_Past_12h() {
		return WEP_Past_12h;
	}
	public void setWEP_Past_12h(Double wEP_Past_12h) {
		WEP_Past_12h = wEP_Past_12h;
	}
	public Double getWIN_PD_12h() {
		return WIN_PD_12h;
	}
	public void setWIN_PD_12h(Double wIN_PD_12h) {
		WIN_PD_12h = wIN_PD_12h;
	}
	public Double getWIN_S_Max_12h() {
		return WIN_S_Max_12h;
	}
	public void setWIN_S_Max_12h(Double wIN_S_Max_12h) {
		WIN_S_Max_12h = wIN_S_Max_12h;
	}
}
