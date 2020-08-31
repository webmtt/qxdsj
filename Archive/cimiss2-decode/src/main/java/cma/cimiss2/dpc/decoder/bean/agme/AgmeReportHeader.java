package cma.cimiss2.dpc.decoder.bean.agme;

import java.util.Date;

import cma.cimiss2.dpc.decoder.tools.enumeration.ObservationMethod;

// TODO: Auto-generated Javadoc
/**
 * <br>.
 *
 * @author wuzuoqiang
 * @Title:  AgmeReportHeader.java
 * @Package cma.cimiss2.dpc.decoder.bean.agme
 * @Description:    TODO(农气报文段头信息)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月21日 下午1:37:20   wuzuoqiang    Initial creation.
 * </pre>
 */
public class AgmeReportHeader{
	
	/**
     * NO: 1.1  <br>
     * nameCN: 区站号 <br>
     * unit: <br>
     * BUFR FXY: V01301 <br>
     * descriptionCN: 5位数字或第1位为字母，第2-5位为数字
     */
    String stationNumberChina;
    /**
     * NO: 1.3  <br>
     * nameCN: 纬度 <br>
     * unit: 度 <br>
     * BUFR FXY: V05001 <br>
     * descriptionCN:
     */
    Double latitude;
    /**
     * NO: 1.4  <br>
     * nameCN: 经度 <br>
     * unit: 度 <br>
     * BUFR FXY: V06001 <br>
     * descriptionCN:
     */
    Double longitude;
    /**
     * NO: 1.5  <br>
     * nameCN: 观测场拔海高度 <br>
     * unit: 1m <br>
     * BUFR FXY: V07001 <br>
     * descriptionCN: 若低于海平面，为负值
     */
    Double heightOfSationGroundAboveMeanSeaLevel;
    /**
     * NO: 1.6  <br>
     * nameCN: 气压传感器拔海高度 <br>
     * unit: 1m <br>
     * BUFR FXY: V07031 <br>
     * descriptionCN: 无气压传感器时，值为null ，若低于海平面，为负值
     */
    Double heightOfBarometerAboveMeanSeaLevel;
    /**
     * NO: 1.7  <br>
     * nameCN: 观测方式 <br>
     * unit: <br>
     * BUFR FXY: <br>
     * descriptionCN:
     * 当器测项目为人工观测时 MANUAL，器测项目为自动站观测时 EQUIPMENT
     */
    ObservationMethod observationMethod;
    
    /** nameCN: 记录段个数 <br> unit: <br> BUFR FXY: <br> descriptionCN:. */
    int reporetCount;
    
    /**
     * Gets the correctsign.
     *
     * @return the correctsign
     */
    public String getCorrectsign() {
		return correctsign;
	}

	/**
	 * Sets the correctsign.
	 *
	 * @param correctsign the new correctsign
	 */
	public void setCorrectsign(String correctsign) {
		this.correctsign = correctsign;
	}

	/**
	 * Gets the bul center.
	 *
	 * @return the bul center
	 */
	public String getBul_center() {
		return bul_center;
	}

	/**
	 * Sets the bul center.
	 *
	 * @param bul_center the new bul center
	 */
	public void setBul_center(String bul_center) {
		this.bul_center = bul_center;
	}
	
	/**
	 * Gets the TTA aii.
	 *
	 * @return the TTA aii
	 */
	public String getTTAAii() {
		return TTAAii;
	}

	/**
	 * Sets the TTA aii.
	 *
	 * @param tTAAii the new TTA aii
	 */
	public void setTTAAii(String tTAAii) {
		TTAAii = tTAAii;
	}
	
	/** The report time. */
	Date report_time;
    
    /** The forecast time. */
    Date forecast_time;
    
    /** The crop type. */
    String cropType;
    
    /** The correctsign. */
    String correctsign;
    
    /** The bul center. */
    String bul_center;
    
    /** The TTA aii. */
    String TTAAii;
    
    /**
     * Instantiates a new agme report header.
     *
     * @param agmeReportHeader the agme report header
     */
    public AgmeReportHeader(AgmeReportHeader agmeReportHeader) {
    	setHeightOfBarometerAboveMeanSeaLevel(agmeReportHeader.getHeightOfBarometerAboveMeanSeaLevel());
    	setHeightOfSationGroundAboveMeanSeaLevel(agmeReportHeader.getHeightOfSationGroundAboveMeanSeaLevel());
    	setLatitude(agmeReportHeader.getLatitude());
    	setLongitude(agmeReportHeader.getLongitude());
    	setStationNumberChina(agmeReportHeader.getStationNumberChina());
    	setObservationMethod(agmeReportHeader.getObservationMethod());
    	setReporetCount(agmeReportHeader.getReporetCount());
    }
    
    /**
     * Instantiates a new agme report header.
     */
    public AgmeReportHeader() {
    	
    }
    
	/**
	 * Gets the station number china.
	 *
	 * @return the station number china
	 */
	public String getStationNumberChina() {
		return stationNumberChina;
	}
	
	/**
	 * Sets the station number china.
	 *
	 * @param stationNumberChina the new station number china
	 */
	public void setStationNumberChina(String stationNumberChina) {
		this.stationNumberChina = stationNumberChina;
	}
	
	/**
	 * Gets the latitude.
	 *
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}
	
	/**
	 * Sets the latitude.
	 *
	 * @param latitude the new latitude
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	/**
	 * Gets the longitude.
	 *
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}
	
	/**
	 * Sets the longitude.
	 *
	 * @param longitude the new longitude
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	/**
	 * Gets the height of sation ground above mean sea level.
	 *
	 * @return the height of sation ground above mean sea level
	 */
	public Double getHeightOfSationGroundAboveMeanSeaLevel() {
		return heightOfSationGroundAboveMeanSeaLevel;
	}
	
	/**
	 * Sets the height of sation ground above mean sea level.
	 *
	 * @param heightOfSationGroundAboveMeanSeaLevel the new height of sation ground above mean sea level
	 */
	public void setHeightOfSationGroundAboveMeanSeaLevel(Double heightOfSationGroundAboveMeanSeaLevel) {
		this.heightOfSationGroundAboveMeanSeaLevel = heightOfSationGroundAboveMeanSeaLevel;
	}
	
	/**
	 * Gets the height of barometer above mean sea level.
	 *
	 * @return the height of barometer above mean sea level
	 */
	public Double getHeightOfBarometerAboveMeanSeaLevel() {
		return heightOfBarometerAboveMeanSeaLevel;
	}
	
	/**
	 * Sets the height of barometer above mean sea level.
	 *
	 * @param heightOfBarometerAboveMeanSeaLevel the new height of barometer above mean sea level
	 */
	public void setHeightOfBarometerAboveMeanSeaLevel(Double heightOfBarometerAboveMeanSeaLevel) {
		this.heightOfBarometerAboveMeanSeaLevel = heightOfBarometerAboveMeanSeaLevel;
	}
	
	/**
	 * Gets the observation method.
	 *
	 * @return the observation method
	 */
	public ObservationMethod getObservationMethod() {
		return observationMethod;
	}
	
	/**
	 * Sets the observation method.
	 *
	 * @param observationMethod the new observation method
	 */
	public void setObservationMethod(ObservationMethod observationMethod) {
		this.observationMethod = observationMethod;
	}
	
	/**
	 * Gets the reporet count.
	 *
	 * @return the reporet count
	 */
	public int getReporetCount() {
		return reporetCount;
	}
	
	/**
	 * Sets the reporet count.
	 *
	 * @param reporetCount the new reporet count
	 */
	public void setReporetCount(int reporetCount) {
		this.reporetCount = reporetCount;
	}
	
	/**
	 * Gets the crop type.
	 *
	 * @return the crop type
	 */
	public String getCropType() {
		return cropType;
	}
	
	/**
	 * Sets the crop type.
	 *
	 * @param cropType the new crop type
	 */
	public void setCropType(String cropType) {
		this.cropType = cropType;
	}
	
	/**
	 * Gets the forecast time.
	 *
	 * @return the forecast time
	 */
	public Date getForecast_time() {
		return forecast_time;
	}

	/**
	 * Sets the forecast time.
	 *
	 * @param forecast_time the new forecast time
	 */
	public void setForecast_time(Date forecast_time) {
		this.forecast_time = forecast_time;
	}

	/**
	 * Gets the report time.
	 *
	 * @return the report time
	 */
	public Date getReport_time() {
		return report_time;
	}
	
	/**
	 * Sets the report time.
	 *
	 * @param report_time the new report time
	 */
	public void setReport_time(Date report_time) {
		this.report_time = report_time;
	}
    
	
    

}
