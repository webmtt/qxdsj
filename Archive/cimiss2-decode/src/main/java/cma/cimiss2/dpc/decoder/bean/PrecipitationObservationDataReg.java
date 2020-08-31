package cma.cimiss2.dpc.decoder.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import cma.cimiss2.dpc.decoder.annotation.Column;

// TODO: Auto-generated Javadoc
/**
 * 区域自动雨量站数据文件格式
 * 
 * notes:
 * <ul>
 * <li>定义参考了一下文档
 * <ol>
 * <li>《实时-历史地面气象资料一体化数据文件命名规则和格式》
 * <li>《CIMISS系统要素代码表（版本6 初稿）》
 * <li>《BUFR TABLE B》 @see <a href=
 * "http://www.emc.ncep.noaa.gov/mmb/data_processing/bufrtab_tableb.htm">http://www.emc.ncep.noaa.gov/mmb/data_processing/bufrtab_tableb.htm</a>
 * </ol>
 * </li>
 * </ul>
 * 
 * @author shevawen
 */
public class PrecipitationObservationDataReg implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * NO: 1.1  <br>
	 * nameCN: 区站号 <br>
	 * unit: <br>
	 * BUFR FXY: V01301 <br>
	 * descriptionCN: 5位数字或第1位为字母，第2-5位为数字
	 */
	@Column("V01301")
	String stationNumberChina;

	/**
	 * NO: 1.2  <br>
	 * nameCN: 纬度 <br>
	 * unit: 度 <br>
	 * BUFR FXY: V05001 <br>
	 * descriptionCN:
	 */
	@Column("V05001")
	Double latitude;
	/**
	 * NO: 1.3  <br>
	 * nameCN: 经度 <br>
	 * unit: 度 <br>
	 * BUFR FXY: V06001 <br>
	 * descriptionCN:
	 */
	@Column("V06001")
	Double longitude;
	/**
	 * NO: 1.4  <br>
	 * nameCN: 观测场拔海高度 <br>
	 * unit: 1m <br>
	 * BUFR FXY: V07001 <br>
	 * descriptionCN: 若低于海平面，为负值
	 */
	@Column("V07001")
	Double heightOfSationGroundAboveMeanSeaLevel;

	/**
	 * NO: 2.1  <br>
	 * nameCN: 观测时间 <br>
	 * unit: <br>
	 * BUFR FXY: V04001/V04002/V04003/V04004 <br>
	 * descriptionCN:
	 */
//	@Column(value = "V04001", pattern = "yyyy")
//	@Column(value = "V04002", pattern = "MM")
//	@Column(value = "V04003", pattern = "dd")
//	@Column(value = "V04004", pattern = "HH")
	@Column("D_DATETIME")
	Date observationTime;
	/**
	 * NO: 2.2  <br>
	 * nameCN: 小时累计雨量 <br>
	 * unit: 1mm <br>
	 * BUFR FXY: V13019 <br>
	 * descriptionCN: 前小时正点至当前时刻的小时累计雨量
	 */

	QCElement<Double> precipitation1Hour;
	/**
	 * NO: 2.3  <br>
	 * nameCN: 日累计雨量 <br>
	 * unit: 1mm <br>
	 * BUFR FXY: V13019 <br>
	 * descriptionCN: 20 时至当前时刻的累计雨量
	 */

	QCElement<Double> precipitation1Day;
	/**
	 * NO: 2.4 <br>
	 * nameCN: 小时内每分钟降水量数据 <br>
	 * unit: 1mm <br>
	 * BUFR FXY: <br>
	 * descriptionCN:上一小时的正点至当前观测时间的每分钟雨量。每分钟两位高位不足补“0”，记录缺测填“//”
	 */
	List<QCElement<Double>> precipitationEveryMinutes;

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
	 * Gets the observation time.
	 *
	 * @return the observation time
	 */
	public Date getObservationTime() {
		return observationTime;
	}

	/**
	 * Sets the observation time.
	 *
	 * @param observationTime the new observation time
	 */
	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}

	/**
	 * Gets the precipitation 1 hour.
	 *
	 * @return the precipitation 1 hour
	 */
	public QCElement<Double> getPrecipitation1Hour() {
		return precipitation1Hour;
	}

	/**
	 * Sets the precipitation 1 hour.
	 *
	 * @param precipitation1Hour the new precipitation 1 hour
	 */
	public void setPrecipitation1Hour(QCElement<Double> precipitation1Hour) {
		this.precipitation1Hour = precipitation1Hour;
	}

	/**
	 * Gets the precipitation 1 day.
	 *
	 * @return the precipitation 1 day
	 */
	public QCElement<Double> getPrecipitation1Day() {
		return precipitation1Day;
	}

	/**
	 * Sets the precipitation 1 day.
	 *
	 * @param precipitation1Day the new precipitation 1 day
	 */
	public void setPrecipitation1Day(QCElement<Double> precipitation1Day) {
		this.precipitation1Day = precipitation1Day;
	}

	/**
	 * Gets the precipitation every minutes.
	 *
	 * @return the precipitation every minutes
	 */
	public List<QCElement<Double>> getPrecipitationEveryMinutes() {
		return precipitationEveryMinutes;
	}

	/**
	 * Sets the precipitation every minutes.
	 *
	 * @param precipitationEveryMinutes the new precipitation every minutes
	 */
	public void setPrecipitationEveryMinutes(List<QCElement<Double>> precipitationEveryMinutes) {
		this.precipitationEveryMinutes = precipitationEveryMinutes;
	}
}
