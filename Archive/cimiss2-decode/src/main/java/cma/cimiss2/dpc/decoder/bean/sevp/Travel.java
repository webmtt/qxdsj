package cma.cimiss2.dpc.decoder.bean.sevp;

import java.util.Date;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
	旅游景区气象服务产品实体类
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《旅游区气象服务产品观测资料要素表》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月24日 上午11:37:06   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class Travel{
	/**
	 * 服务产品标识编码   D_SVCO
	 */
	private String productID;
	/**
	 * 发布时间  D_DATETIME
	 */
	private Date releaseTime;
	/**
	 * 发布单位名称  V_PUNA
	 */
	private String releaseOrgName = "999999";
	/**
	 * 所属省级气象局编码   V_PUPROCO
	 */
	private String provCode = "999999";
	/**
	 * 发布单位编码	V_PUCO
	 */
	private String releaseOrgCode = "999999";
	/**
	 * 预警/景观名称  V_SPNA
	 */
	private String sceneryName = "999999";
	/**
	 * 预警/景观类型编码	V_SPCO
	 */
	private String sceneryCode = "999999";
	/**
	 * 景区预警级别/景观产品信息	V_VA
	 */
	private int sceneryProductInfo = 999999;
	/**
	 * 影响起始时间 	V_ISTS
	 */
	private String affectedStartTime = "999999";
	/**
	 * 预计影响结束时间	V_ITEN
	 */
	private String affectedEndTime = "999999";
	/**
	 * 景区名称	V_IVNA
	 */
	private String scenicSpotName = "999999";
	/**
	 * 景区纬度	V05001 度
	 */
	private double scenicSpotLat = 999999;
	/**
	 * 景区经度	V06001 度
	 */
	private double scenicSpotLon = 999999;
	/**
	 * 影响景点名称	V_IVSN 
	 */
	private String affectedScenicSpotName = "999999";
	/**
	 * 影响景点纬度	V_IVSLAT，字符串
	 */
	private String affectedScenicSportLat = "999999";
	/**
	 * 影响景点经度	V_IVSLON，字符串
	 */
	private String affectedScenicSportLon = "999999";
	/**
	 * 灾害预警/景观产品内容	V_PD
	 */
	private String disaOrProdContent = "999999";
	/**
	 * 演变趋势	V_ET,代码表
	 */
	private int evolutionTrend = 999999;
	/**
	 * 防御对策 / 观景建议  V_PS
	 */
	private String suggestions = "999999";
	/**
	 * 景区预警信号发布与撤销标识	V_WA，代码表
	 */
	private int signalReleaseAndCancelCode = 999999;
	/**
	 * 发布人姓名   V_PUER
	 */
	private String publisherName = "999999";
	
	
	public String getProductID() {
		return productID;
	}
	public void setProductID(String productID) {
		this.productID = productID;
	}
	public Date getReleaseTime() {
		return releaseTime;
	}
	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}
	public String getReleaseOrgName() {
		return releaseOrgName;
	}
	public void setReleaseOrgName(String releaseOrgName) {
		this.releaseOrgName = releaseOrgName;
	}
	public String getProvCode() {
		return provCode;
	}
	public void setProvCode(String provCode) {
		this.provCode = provCode;
	}
	public String getReleaseOrgCode() {
		return releaseOrgCode;
	}
	public void setReleaseOrgCode(String releaseOrgCode) {
		this.releaseOrgCode = releaseOrgCode;
	}
	public String getSceneryCode() {
		return sceneryCode;
	}
	public void setSceneryCode(String sceneryCode) {
		this.sceneryCode = sceneryCode;
	}
	public String getSceneryName() {
		return sceneryName;
	}
	public void setSceneryName(String sceneryName) {
		this.sceneryName = sceneryName;
	}
	public int getSceneryProductInfo() {
		return sceneryProductInfo;
	}
	public void setSceneryProductInfo(int sceneryProductInfo) {
		this.sceneryProductInfo = sceneryProductInfo;
	}
	public String getAffectedStartTime() {
		return affectedStartTime;
	}
	public void setAffectedStartTime(String affectedStartTime) {
		this.affectedStartTime = affectedStartTime;
	}
	public String getAffectedEndTime() {
		return affectedEndTime;
	}
	public void setAffectedEndTime(String affectedEndTime) {
		this.affectedEndTime = affectedEndTime;
	}
	public String getScenicSpotName() {
		return scenicSpotName;
	}
	public void setScenicSpotName(String scenicSpotName) {
		this.scenicSpotName = scenicSpotName;
	}
	public double getScenicSpotLat() {
		return scenicSpotLat;
	}
	public void setScenicSpotLat(double scenicSpotLat) {
		this.scenicSpotLat = scenicSpotLat;
	}
	public double getScenicSpotLon() {
		return scenicSpotLon;
	}
	public void setScenicSpotLon(double scenicSpotLon) {
		this.scenicSpotLon = scenicSpotLon;
	}
	public String getAffectedScenicSpotName() {
		return affectedScenicSpotName;
	}
	public void setAffectedScenicSpotName(String affectedScenicSpotName) {
		this.affectedScenicSpotName = affectedScenicSpotName;
	}
	public String getAffectedScenicSportLat() {
		return affectedScenicSportLat;
	}
	public void setAffectedScenicSportLat(String affectedScenicSportLat) {
		this.affectedScenicSportLat = affectedScenicSportLat;
	}
	public String getAffectedScenicSportLon() {
		return affectedScenicSportLon;
	}
	public void setAffectedScenicSportLon(String affectedScenicSportLon) {
		this.affectedScenicSportLon = affectedScenicSportLon;
	}
	public String getDisaOrProdContent() {
		return disaOrProdContent;
	}
	public void setDisaOrProdContent(String disaOrProdContent) {
		this.disaOrProdContent = disaOrProdContent;
	}
	public int getEvolutionTrend() {
		return evolutionTrend;
	}
	public void setEvolutionTrend(int evolutionTrend) {
		this.evolutionTrend = evolutionTrend;
	}
	public String getSuggestions() {
		return suggestions;
	}
	public void setSuggestions(String suggestions) {
		this.suggestions = suggestions;
	}
	public int getSignalReleaseAndCancelCode() {
		return signalReleaseAndCancelCode;
	}
	public void setSignalReleaseAndCancelCode(int signalReleaseAndCancelCode) {
		this.signalReleaseAndCancelCode = signalReleaseAndCancelCode;
	}
	public String getPublisherName() {
		return publisherName;
	}
	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}
}