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
public class RadiMulChnPenTab extends RadiMulChn{
	
	
	/**
	 * @Fields total : 配对样本总数
	 */
	@Column(value = "V_TOTAL")
	private Double total ;
	
	/**
	 * @Fields index : 当前匹配对的索引
	 */
	@Column(value = "V_INDEX")
	private long index ;
	
	/**
	 * @Fields areaStationNumber(character) : 区站号(字符) 
	 */
	@Column(value = "V01301")
	private String stationNumberC;


	/**
	 * @Fields latitude : 纬度 
	 */
	@Column(value = "V05001")
	private Double latitude=999999.0;
	
	/**
	 * @Fields longitude : 经度 
	 */
	@Column(value = "V06001")
	private Double longitude=999999.0;
	
	
	
	/**
	 * @Fields obsLvl : 观测水平压力（hPa）或累计间隔（小时）
	 */
	@Column(value = "V_OBSLVL")
	private String obsLvl ;
	
	/**
	 * @Fields elevationAltitude : 测站高度 
	 */
	@Column(value = "V07001")
	private Double elevationAltitude=999999.0;
	
	/**
	 * @Fields fcst : 插值到观测位置的预报值
	 */
	@Column(value = "V_FCST")
	private Double fcst ;
	
	/**
	 * @Fields obs : 观测值
	 */
	@Column(value = "V_OBS")
	private Double obs ;
	
	private String climo;
	
	private String obsqc;

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public long getIndex() {
		return index;
	}

	public void setIndex(long index) {
		this.index = index;
	}

	public String getObsLvl() {
		return obsLvl;
	}

	public void setObsLvl(String obsLvl) {
		this.obsLvl = obsLvl;
	}

	public Double getFcst() {
		return fcst;
	}

	public void setFcst(Double fcst) {
		this.fcst = fcst;
	}

	public Double getObs() {
		return obs;
	}

	public void setObs(Double obs) {
		this.obs = obs;
	}

	public String getStationNumberC() {
		return stationNumberC;
	}

	public void setStationNumberC(String stationNumberC) {
		this.stationNumberC = stationNumberC;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getElevationAltitude() {
		return elevationAltitude;
	}

	public void setElevationAltitude(Double elevationAltitude) {
		this.elevationAltitude = elevationAltitude;
	}

	public String getClimo() {
		return climo;
	}

	public void setClimo(String climo) {
		this.climo = climo;
	}

	public String getObsqc() {
		return obsqc;
	}

	public void setObsqc(String obsqc) {
		this.obsqc = obsqc;
	}
	
	
	
	
}
