package cma.cimiss2.dpc.decoder.bean.cawn;

import java.util.Date;

import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
/**
 * 
 * <br>
 * @Title:  pmDA.java
 * @Package cma.cimiss2.dpc.decoder.bean.cawn
 * @Description:(气溶胶初级质控日值资料实体类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年4月11日 上午11:18:25   maxiyue   Initial creation.
 * </pre>
 * 
 * @author maxiyue
 */
public class pmDA  extends AgmeReportHeader{
	
	/**
	 * @Fields stationNumberChina : 区站号(字符) 
	 */
	private String stationNumberChina = "999999";

	/**
	 * @Fields latitude : 纬度 
	 */
	private Double latitude = 999999.0;

	/**
	 * @Fields longitude : 经度 
	 */
	private Double longitude = 999999.0;

	public String getStationNumberChina() {
		return stationNumberChina;
	}

	public void setStationNumberChina(String stationNumberChina) {
		this.stationNumberChina = stationNumberChina;
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

	public Double getHeightOfSationGroundAboveMeanSeaLevel() {
		return heightOfSationGroundAboveMeanSeaLevel;
	}

	public void setHeightOfSationGroundAboveMeanSeaLevel(Double heightOfSationGroundAboveMeanSeaLevel) {
		this.heightOfSationGroundAboveMeanSeaLevel = heightOfSationGroundAboveMeanSeaLevel;
	}

	public Date getObservationTime() {
		return observationTime;
	}

	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}

	public Double getPM1_1h_AVG() {
		return PM1_1h_AVG;
	}

	public void setPM1_1h_AVG(Double pM1_1h_AVG) {
		PM1_1h_AVG = pM1_1h_AVG;
	}

	public Double getPM1_1h_AVG_COUNT() {
		return PM1_1h_AVG_COUNT;
	}

	public void setPM1_1h_AVG_COUNT(Double pM1_1h_AVG_COUNT) {
		PM1_1h_AVG_COUNT = pM1_1h_AVG_COUNT;
	}

	public Double getPM10_5min_AVG() {
		return PM10_5min_AVG;
	}

	public void setPM10_5min_AVG(Double pM10_5min_AVG) {
		PM10_5min_AVG = pM10_5min_AVG;
	}

	public Double getPM10_5min_AVG_COUNT() {
		return PM10_5min_AVG_COUNT;
	}

	public void setPM10_5min_AVG_COUNT(Double pM10_5min_AVG_COUNT) {
		PM10_5min_AVG_COUNT = pM10_5min_AVG_COUNT;
	}

	public Double getPM10_30min_AVG() {
		return PM10_30min_AVG;
	}

	public void setPM10_30min_AVG(Double pM10_30min_AVG) {
		PM10_30min_AVG = pM10_30min_AVG;
	}

	public Double getPM10_30min_AVG_COUNT() {
		return PM10_30min_AVG_COUNT;
	}

	public void setPM10_30min_AVG_COUNT(Double pM10_30min_AVG_COUNT) {
		PM10_30min_AVG_COUNT = pM10_30min_AVG_COUNT;
	}

	public Double getPM10_1h_AVG() {
		return PM10_1h_AVG;
	}

	public void setPM10_1h_AVG(Double pM10_1h_AVG) {
		PM10_1h_AVG = pM10_1h_AVG;
	}

	public Double getPM10_1h_AVG_COUNT() {
		return PM10_1h_AVG_COUNT;
	}

	public void setPM10_1h_AVG_COUNT(Double pM10_1h_AVG_COUNT) {
		PM10_1h_AVG_COUNT = pM10_1h_AVG_COUNT;
	}

	public Double getPM10_24h_AVG() {
		return PM10_24h_AVG;
	}

	public void setPM10_24h_AVG(Double pM10_24h_AVG) {
		PM10_24h_AVG = pM10_24h_AVG;
	}

	public Double getPM10_24h_AVG_COUNT() {
		return PM10_24h_AVG_COUNT;
	}

	public void setPM10_24h_AVG_COUNT(Double pM10_24h_AVG_COUNT) {
		PM10_24h_AVG_COUNT = pM10_24h_AVG_COUNT;
	}

	public Double getPM2p5_5min_AVG() {
		return PM2p5_5min_AVG;
	}

	public void setPM2p5_5min_AVG(Double pM2p5_5min_AVG) {
		PM2p5_5min_AVG = pM2p5_5min_AVG;
	}

	public Double getPM2p5_5min_AVG_COUNT() {
		return PM2p5_5min_AVG_COUNT;
	}

	public void setPM2p5_5min_AVG_COUNT(Double pM2p5_5min_AVG_COUNT) {
		PM2p5_5min_AVG_COUNT = pM2p5_5min_AVG_COUNT;
	}

	public Double getPM2p5_1h_AVG() {
		return PM2p5_1h_AVG;
	}

	public void setPM2p5_1h_AVG(Double pM2p5_1h_AVG) {
		PM2p5_1h_AVG = pM2p5_1h_AVG;
	}

	public Double getPM2p5_1h_AVG_COUNT() {
		return PM2p5_1h_AVG_COUNT;
	}

	public void setPM2p5_1h_AVG_COUNT(Double pM2p5_1h_AVG_COUNT) {
		PM2p5_1h_AVG_COUNT = pM2p5_1h_AVG_COUNT;
	}

	public Double getPM2p5_24h_AVG() {
		return PM2p5_24h_AVG;
	}

	public void setPM2p5_24h_AVG(Double pM2p5_24h_AVG) {
		PM2p5_24h_AVG = pM2p5_24h_AVG;
	}

	public Double getPM2p5_24h_AVG_COUNT() {
		return PM2p5_24h_AVG_COUNT;
	}

	public void setPM2p5_24h_AVG_COUNT(Double pM2p5_24h_AVG_COUNT) {
		PM2p5_24h_AVG_COUNT = pM2p5_24h_AVG_COUNT;
	}

	/**
	 * @Fields heightOfSationGroundAboveMeanSeaLevel : 测站高度 
	 */
	private Double heightOfSationGroundAboveMeanSeaLevel = 999999.0;

	/**
	 * @Fields observationTime : 观测时间
	 */
	private Date observationTime ;
	
	/**
	 * @Fields PM1_1h_AVG : 1小时观测PM1日平均值
	 */
	private Double PM1_1h_AVG = 999999.0;
	
	/**
	 * @Fields PM1_1h_AVG_COUNT : 1小时观测PM1每日内有效小时的样本数
	 */
	private Double PM1_1h_AVG_COUNT = 999999.0;
	
	/**
	 * @Fields PM10_5min_AVG : 5分钟观测PM10日均值
	 */
	private Double PM10_5min_AVG= 999999.0 ;
	
	/**
	 * @Fields PM10_5min_AVG_COUNT : 5分钟观测PM10日均值每日内有效小时样本数
	 */
	private Double PM10_5min_AVG_COUNT = 999999.0;
	
	/**
	 * @Fields PM10_30min_AVG : 半小时观测PM10日均值
	 */
	private Double PM10_30min_AVG= 999999.0 ;
	
	/**
	 * @Fields PM10_30min_AVG_COUNT : 半小时观测PM10日均值每日内有效小时样本数
	 */
	private Double PM10_30min_AVG_COUNT= 999999.0 ;
	
	/**
	 * @Fields PM10_1h_AVG : 1小时观测PM10日均值
	 */
	private Double PM10_1h_AVG = 999999.0;
	
	/**
	 * @Fields PM10_1h_AVG_COUNT : 1小时观测PM10日均值每日内有效小时样本数
	 */
	private Double PM10_1h_AVG_COUNT = 999999.0;
	
	/**
	 * @Fields PM10_24h_AVG : 24小时观测PM10日均值
	 */
	private Double PM10_24h_AVG= 999999.0 ;
	
	/**
	 * @Fields PM10_24h_AVG_COUNT : 24小时观测PM10日均值每日内有效小时样本数
	 */
	private Double PM10_24h_AVG_COUNT = 999999.0;
	
	/**
	 * @Fields PM2p5_5min_AVG : 5分钟观测PM2.5日均值
	 */
	private Double PM2p5_5min_AVG = 999999.0;
	
	/**
	 * @Fields PM2p5_5min_AVG_COUNT :5分钟观测PM25日均值每日内有效小时样本数
	 */
	private Double PM2p5_5min_AVG_COUNT = 999999.0;
	
	/**
	 * @Fields PM2p5_1h_AVG : 1小时观测PM2.5日均值
	 */
	private Double PM2p5_1h_AVG = 999999.0;
	
	/**
	 * @Fields PM2p5_1h_AVG_COUNT : 1小时观测PM25日均值每日内有效小时样本数
	 */
	private Double PM2p5_1h_AVG_COUNT= 999999.0 ;
	
	/**
	 * @Fields PM2p5_24h_AVG : 24小时观测PM2.5日均值
	 */
	private Double PM2p5_24h_AVG = 999999.0;
	
	/**
	 * @Fields PM2p5_24h_AVG_COUNT : 24小时PM25日均值每日内有效小时样本数
	 */
	private Double PM2p5_24h_AVG_COUNT= 999999.0 ;
	
}
