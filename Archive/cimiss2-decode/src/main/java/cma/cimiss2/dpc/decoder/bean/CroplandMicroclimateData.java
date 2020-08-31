package cma.cimiss2.dpc.decoder.bean;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * 农田小气候数据文件格式
 * 
 * notes:
 * <ul>
 * <li>定义参考了一下文档
 * <ol>
 * <li>《实时-历史地面气象资料一体化数据文件命名规则和格式》
 * <li>《CIMISS系统要素代码表（版本6 初稿）》
 * </ol>
 * </li>
 * </ul>.
 *
 * @author wuzuoqiang
 */
public class CroplandMicroclimateData {

	/**
	 * NO: 1.1  <br>
	 * nameCN: 区站号 <br>
	 * unit: <br>
	 * BUFR FXY: V01301 <br>
	 * descriptionCN: 5位数字或第1位为字母，第2-5位为数字
	 */
	String stationNumberChina;

	/**
	 * NO: 1.1  <br>
	 * nameCN: 区站名 <br>
	 * unit: <br>
	 * descriptionCN: 为所在地村名
	 */
	String stationNameChina;

	/**
	 * NO: 1.1  <br>
	 * nameCN: 省名 <br>
	 * unit: <br>
	 * descriptionCN: 省份名称
	 */
	String provinceNameChina;

	/**
	 * NO: 1.2  <br>
	 * nameCN: 纬度 <br>
	 * unit: 度 <br>
	 * BUFR FXY: V05001 <br>
	 * descriptionCN:
	 */
	Double latitude;
	/**
	 * NO: 1.3  <br>
	 * nameCN: 经度 <br>
	 * unit: 度 <br>
	 * BUFR FXY: V06001 <br>
	 * descriptionCN:
	 */
	Double longitude;
	/**
	 * NO: 1.4  <br>
	 * nameCN: 观测场拔海高度 <br>
	 * unit: 1m <br>
	 * BUFR FXY: V07001 <br>
	 * descriptionCN: 若低于海平面，为负值
	 */
	Double heightOfSationGroundAboveMeanSeaLevel;

	/**
	 * NO: 1.1  <br>
	 * nameCN: 观测作物名称<br>
	 * unit: <br>
	 * descriptionCN: 观测作物名称
	 */
	String cropName;

	/**
	 * NO: 2.1  <br>
	 * nameCN: 本站气压 <br>
	 * unit: 1hPa <br>
	 * BUFR FXY: V10004 <br>
	 * descriptionCN: 当前时刻的本站气压值
	 */
	double stationPressure;

	/**
	 * NO: 2.1  <br>
	 * nameCN: 观测时间 <br>
	 * unit: <br>
	 * BUFR FXY: V04001/V04002/V04003/V04004 <br>
	 * descriptionCN:
	 */
	Date observationTime;

	/**
	 * NO: 2.15  <br>
	 * nameCN: 30cm气温 <br>
	 * unit: 1℃ <br>
	 * BUFR FXY: V12001 <br>
	 * descriptionCN: 当前时刻的30cm气温
	 */
	double temperature_30;

	/**
	 * NO: 2.15  <br>
	 * nameCN: 60cm气温 <br>
	 * unit: 1℃ <br>
	 * BUFR FXY: V12001 <br>
	 * descriptionCN: 当前时刻的60cm气温
	 */
	double temperature_60;

	/**
	 * NO: 2.15  <br>
	 * nameCN: 150cm气温 <br>
	 * unit: 1℃ <br>
	 * BUFR FXY: V12001 <br>
	 * descriptionCN: 当前时刻的150cm气温
	 */
	double temperature_150;

	/**
	 * NO: 2.15  <br>
	 * nameCN: 300cm气温 <br>
	 * unit: 1℃ <br>
	 * BUFR FXY: V12001 <br>
	 * descriptionCN: 当前时刻的300cm气温
	 */
	double temperature_300;

	/**
	 * NO: 2.15  <br>
	 * nameCN: 冠层温度 <br>
	 * unit: 1℃ <br>
	 * descriptionCN: 冠层温度
	 */
	double temperatureCanopy;

	// start dyl 17.11.29
	/** descriptionCN: 小时降水量. */
	double hourlyPrecipitation;
	


	/** NO:   <br> nameCN: <br> unit: <br> descriptionCN: 30cm风速. */
	double windSpeed_30;

	/** NO:   <br> nameCN: <br> unit: <br> descriptionCN: 60cm风速. */
	double windSpeed_60;

	/** NO:   <br> nameCN: <br> unit: <br> descriptionCN: 150cm风速. */
	double windSpeed_150;

	/** NO:   <br> nameCN: <br> unit: <br> descriptionCN: 300cm风速. */
	double windSpeed_300;

	/** NO:   <br> nameCN: <br> unit: <br> descriptionCN: 600cm风速. */
	double windSpeed_600;

	/** NO:   <br> nameCN: <br> unit: <br> descriptionCN: 30cm空气相对湿度. */
	double relativeHumidity_Air_30;

	/** NO:   <br> nameCN: <br> unit: <br> descriptionCN: 60cm空气相对湿度. */
	double relativeHumidity_Air_60;

	/** NO:   <br> nameCN: <br> unit: <br> descriptionCN: 150cm空气相对湿度. */

	double relativeHumidity_Air_150;

	/** NO:   <br> nameCN: <br> unit: <br> descriptionCN: 300cm空气相对湿度. */

	double relativeHumidity_Air_300;

	/** NO:   <br> nameCN: <br> unit: <br> descriptionCN: 0cm地温. */

	double surfaceTemperature_0;

	/** NO:   <br> nameCN: <br> unit: <br> descriptionCN: 5cm地温. */

	double surfaceTemperature_5;

	/** NO:   <br> nameCN: <br> unit: <br> descriptionCN: 10cm地温. */
	double surfaceTemperature_10;
	
	/** NO:   <br> nameCN: <br> unit: <br> descriptionCN: 15cm地温. */
	double surfaceTemperature_15;

	/** NO:   <br> nameCN: <br> unit: <br> descriptionCN: 20cm地温. */
	double surfaceTemperature_20;

	/** NO:   <br> nameCN: <br> unit: <br> descriptionCN: 30cm地温. */
	double surfaceTemperature_30;

	/** NO:   <br> nameCN: <br> unit: <br> descriptionCN: 40cm地温. */
	double surfaceTemperature_40;

	/** NO:   <br> nameCN: <br> unit: <br> descriptionCN: 50cm地温. */
	double surfaceTemperature_50;

	/** NO:   <br> nameCN: <br> unit: <br> descriptionCN: 10cm土壤体积含水率. */
	double moisture_Soil_10;

	/** NO:   <br> nameCN: <br> unit: <br> descriptionCN: 20cm土壤体积含水率. */

	double moisture_Soil_20;

	/** NO:   <br> nameCN: <br> unit: <br> descriptionCN: 30cm土壤体积含水率. */
	double moisture_Soil_30;

	/** NO:   <br> nameCN: <br> unit: <br> descriptionCN: 40cm土壤体积含水率. */
	double moisture_Soil_40;

	/** NO:   <br> nameCN: <br> unit: <br> descriptionCN: 50cm土壤体积含水率. */
	double moisture_Soil_50;

	/** NO:   <br> nameCN: <br> unit: <br> descriptionCN: 10cm土壤相对湿度. */
	double relativeHumidity_Soil_10;

	/** NO:   <br> nameCN: <br> unit: <br> descriptionCN: 10cm土壤相对湿度. */
	double relativeHumidity_Soil_20;

	/** NO:   <br> nameCN: <br> unit: <br> descriptionCN: 10cm土壤相对湿度. */
	double relativeHumidity_Soil_30;

	/** NO:   <br> nameCN: <br> unit: <br> descriptionCN: 10cm土壤相对湿度. */
	double relativeHumidity_Soil_40;

	/** NO:   <br> nameCN: <br> unit: <br> descriptionCN: 10cm土壤相对湿度. */
	double relativeHumidity_Soil_50;

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
	 * Gets the station name china.
	 *
	 * @return the station name china
	 */
	public String getStationNameChina() {
		return stationNameChina;
	}

	/**
	 * Sets the station name china.
	 *
	 * @param stationNameChina the new station name china
	 */
	public void setStationNameChina(String stationNameChina) {
		this.stationNameChina = stationNameChina;
	}

	/**
	 * Gets the province name china.
	 *
	 * @return the province name china
	 */
	public String getProvinceNameChina() {
		return provinceNameChina;
	}

	/**
	 * Sets the province name china.
	 *
	 * @param provinceNameChina the new province name china
	 */
	public void setProvinceNameChina(String provinceNameChina) {
		this.provinceNameChina = provinceNameChina;
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
	 * Gets the crop name.
	 *
	 * @return the crop name
	 */
	public String getCropName() {
		return cropName;
	}

	/**
	 * Sets the crop name.
	 *
	 * @param cropName the new crop name
	 */
	public void setCropName(String cropName) {
		this.cropName = cropName;
	}

	/**
	 * Gets the station pressure.
	 *
	 * @return the station pressure
	 */
	public double getStationPressure() {
		return stationPressure;
	}

	/**
	 * Sets the station pressure.
	 *
	 * @param stationPressure the new station pressure
	 */
	public void setStationPressure(double stationPressure) {
		this.stationPressure = stationPressure;
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
	 * Gets the temperature 30.
	 *
	 * @return the temperature 30
	 */
	public double getTemperature_30() {
		return temperature_30;
	}

	/**
	 * Sets the temperature 30.
	 *
	 * @param temperature_30 the new temperature 30
	 */
	public void setTemperature_30(double temperature_30) {
		this.temperature_30 = temperature_30;
	}

	/**
	 * Gets the temperature 60.
	 *
	 * @return the temperature 60
	 */
	public double getTemperature_60() {
		return temperature_60;
	}

	/**
	 * Sets the temperature 60.
	 *
	 * @param temperature_60 the new temperature 60
	 */
	public void setTemperature_60(double temperature_60) {
		this.temperature_60 = temperature_60;
	}

	/**
	 * Gets the temperature 150.
	 *
	 * @return the temperature 150
	 */
	public double getTemperature_150() {
		return temperature_150;
	}

	/**
	 * Sets the temperature 150.
	 *
	 * @param temperature_150 the new temperature 150
	 */
	public void setTemperature_150(double temperature_150) {
		this.temperature_150 = temperature_150;
	}

	/**
	 * Gets the temperature 300.
	 *
	 * @return the temperature 300
	 */
	public double getTemperature_300() {
		return temperature_300;
	}

	/**
	 * Sets the temperature 300.
	 *
	 * @param temperature_300 the new temperature 300
	 */
	public void setTemperature_300(double temperature_300) {
		this.temperature_300 = temperature_300;
	}

	/**
	 * Gets the temperature canopy.
	 *
	 * @return the temperature canopy
	 */
	public double getTemperatureCanopy() {
		return temperatureCanopy;
	}

	/**
	 * Sets the temperature canopy.
	 *
	 * @param temperatureCanopy the new temperature canopy
	 */
	public void setTemperatureCanopy(double temperatureCanopy) {
		this.temperatureCanopy = temperatureCanopy;
	}

	/**
	 * Gets the hourly precipitation.
	 *
	 * @return the hourly precipitation
	 */
	public double getHourlyPrecipitation() {
		return hourlyPrecipitation;
	}

	/**
	 * Sets the hourly precipitation.
	 *
	 * @param hourlyPrecipitation the new hourly precipitation
	 */
	public void setHourlyPrecipitation(double hourlyPrecipitation) {
		this.hourlyPrecipitation = hourlyPrecipitation;
	}

	/**
	 * Gets the wind speed 30.
	 *
	 * @return the wind speed 30
	 */
	public double getWindSpeed_30() {
		return windSpeed_30;
	}

	/**
	 * Sets the wind speed 30.
	 *
	 * @param windSpeed_30 the new wind speed 30
	 */
	public void setWindSpeed_30(double windSpeed_30) {
		this.windSpeed_30 = windSpeed_30;
	}

	/**
	 * Gets the wind speed 60.
	 *
	 * @return the wind speed 60
	 */
	public double getWindSpeed_60() {
		return windSpeed_60;
	}

	/**
	 * Sets the wind speed 60.
	 *
	 * @param windSpeed_60 the new wind speed 60
	 */
	public void setWindSpeed_60(double windSpeed_60) {
		this.windSpeed_60 = windSpeed_60;
	}

	/**
	 * Gets the wind speed 150.
	 *
	 * @return the wind speed 150
	 */
	public double getWindSpeed_150() {
		return windSpeed_150;
	}

	/**
	 * Sets the wind speed 150.
	 *
	 * @param windSpeed_150 the new wind speed 150
	 */
	public void setWindSpeed_150(double windSpeed_150) {
		this.windSpeed_150 = windSpeed_150;
	}

	/**
	 * Gets the wind speed 300.
	 *
	 * @return the wind speed 300
	 */
	public double getWindSpeed_300() {
		return windSpeed_300;
	}

	/**
	 * Sets the wind speed 300.
	 *
	 * @param windSpeed_300 the new wind speed 300
	 */
	public void setWindSpeed_300(double windSpeed_300) {
		this.windSpeed_300 = windSpeed_300;
	}

	/**
	 * Gets the wind speed 600.
	 *
	 * @return the wind speed 600
	 */
	public double getWindSpeed_600() {
		return windSpeed_600;
	}

	/**
	 * Sets the wind speed 600.
	 *
	 * @param windSpeed_600 the new wind speed 600
	 */
	public void setWindSpeed_600(double windSpeed_600) {
		this.windSpeed_600 = windSpeed_600;
	}

	/**
	 * Gets the relative humidity air 30.
	 *
	 * @return the relative humidity air 30
	 */
	public double getRelativeHumidity_Air_30() {
		return relativeHumidity_Air_30;
	}

	/**
	 * Sets the relative humidity air 30.
	 *
	 * @param relativeHumidity_Air_30 the new relative humidity air 30
	 */
	public void setRelativeHumidity_Air_30(double relativeHumidity_Air_30) {
		this.relativeHumidity_Air_30 = relativeHumidity_Air_30;
	}

	/**
	 * Gets the relative humidity air 60.
	 *
	 * @return the relative humidity air 60
	 */
	public double getRelativeHumidity_Air_60() {
		return relativeHumidity_Air_60;
	}

	/**
	 * Sets the relative humidity air 60.
	 *
	 * @param relativeHumidity_Air_60 the new relative humidity air 60
	 */
	public void setRelativeHumidity_Air_60(double relativeHumidity_Air_60) {
		this.relativeHumidity_Air_60 = relativeHumidity_Air_60;
	}

	/**
	 * Gets the relative humidity air 150.
	 *
	 * @return the relative humidity air 150
	 */
	public double getRelativeHumidity_Air_150() {
		return relativeHumidity_Air_150;
	}

	/**
	 * Sets the relative humidity air 150.
	 *
	 * @param relativeHumidity_Air_150 the new relative humidity air 150
	 */
	public void setRelativeHumidity_Air_150(double relativeHumidity_Air_150) {
		this.relativeHumidity_Air_150 = relativeHumidity_Air_150;
	}

	/**
	 * Gets the relative humidity air 300.
	 *
	 * @return the relative humidity air 300
	 */
	public double getRelativeHumidity_Air_300() {
		return relativeHumidity_Air_300;
	}

	/**
	 * Sets the relative humidity air 300.
	 *
	 * @param relativeHumidity_Air_300 the new relative humidity air 300
	 */
	public void setRelativeHumidity_Air_300(double relativeHumidity_Air_300) {
		this.relativeHumidity_Air_300 = relativeHumidity_Air_300;
	}

	/**
	 * Gets the surface temperature 0.
	 *
	 * @return the surface temperature 0
	 */
	public double getSurfaceTemperature_0() {
		return surfaceTemperature_0;
	}

	/**
	 * Sets the surface temperature 0.
	 *
	 * @param surfaceTemperature_0 the new surface temperature 0
	 */
	public void setSurfaceTemperature_0(double surfaceTemperature_0) {
		this.surfaceTemperature_0 = surfaceTemperature_0;
	}

	/**
	 * Gets the surface temperature 5.
	 *
	 * @return the surface temperature 5
	 */
	public double getSurfaceTemperature_5() {
		return surfaceTemperature_5;
	}

	/**
	 * Sets the surface temperature 5.
	 *
	 * @param surfaceTemperature_5 the new surface temperature 5
	 */
	public void setSurfaceTemperature_5(double surfaceTemperature_5) {
		this.surfaceTemperature_5 = surfaceTemperature_5;
	}

	/**
	 * Gets the surface temperature 10.
	 *
	 * @return the surface temperature 10
	 */
	public double getSurfaceTemperature_10() {
		return surfaceTemperature_10;
	}

	/**
	 * Sets the surface temperature 10.
	 *
	 * @param surfaceTemperature_10 the new surface temperature 10
	 */
	public void setSurfaceTemperature_10(double surfaceTemperature_10) {
		this.surfaceTemperature_10 = surfaceTemperature_10;
	}

	/**
	 * Gets the surface temperature 15.
	 *
	 * @return the surface temperature 15
	 */
	public double getSurfaceTemperature_15() {
		return surfaceTemperature_15;
	}

	/**
	 * Sets the surface temperature 15.
	 *
	 * @param surfaceTemperature_15 the new surface temperature 15
	 */
	public void setSurfaceTemperature_15(double surfaceTemperature_15) {
		this.surfaceTemperature_15 = surfaceTemperature_15;
	}

	/**
	 * Gets the surface temperature 20.
	 *
	 * @return the surface temperature 20
	 */
	public double getSurfaceTemperature_20() {
		return surfaceTemperature_20;
	}

	/**
	 * Sets the surface temperature 20.
	 *
	 * @param surfaceTemperature_20 the new surface temperature 20
	 */
	public void setSurfaceTemperature_20(double surfaceTemperature_20) {
		this.surfaceTemperature_20 = surfaceTemperature_20;
	}

	/**
	 * Gets the surface temperature 30.
	 *
	 * @return the surface temperature 30
	 */
	public double getSurfaceTemperature_30() {
		return surfaceTemperature_30;
	}

	/**
	 * Sets the surface temperature 30.
	 *
	 * @param surfaceTemperature_30 the new surface temperature 30
	 */
	public void setSurfaceTemperature_30(double surfaceTemperature_30) {
		this.surfaceTemperature_30 = surfaceTemperature_30;
	}

	/**
	 * Gets the surface temperature 40.
	 *
	 * @return the surface temperature 40
	 */
	public double getSurfaceTemperature_40() {
		return surfaceTemperature_40;
	}

	/**
	 * Sets the surface temperature 40.
	 *
	 * @param surfaceTemperature_40 the new surface temperature 40
	 */
	public void setSurfaceTemperature_40(double surfaceTemperature_40) {
		this.surfaceTemperature_40 = surfaceTemperature_40;
	}

	/**
	 * Gets the surface temperature 50.
	 *
	 * @return the surface temperature 50
	 */
	public double getSurfaceTemperature_50() {
		return surfaceTemperature_50;
	}

	/**
	 * Sets the surface temperature 50.
	 *
	 * @param surfaceTemperature_50 the new surface temperature 50
	 */
	public void setSurfaceTemperature_50(double surfaceTemperature_50) {
		this.surfaceTemperature_50 = surfaceTemperature_50;
	}

	/**
	 * Gets the moisture soil 10.
	 *
	 * @return the moisture soil 10
	 */
	public double getMoisture_Soil_10() {
		return moisture_Soil_10;
	}

	/**
	 * Sets the moisture soil 10.
	 *
	 * @param moisture_Soil_10 the new moisture soil 10
	 */
	public void setMoisture_Soil_10(double moisture_Soil_10) {
		this.moisture_Soil_10 = moisture_Soil_10;
	}

	/**
	 * Gets the moisture soil 20.
	 *
	 * @return the moisture soil 20
	 */
	public double getMoisture_Soil_20() {
		return moisture_Soil_20;
	}

	/**
	 * Sets the moisture soil 20.
	 *
	 * @param moisture_Soil_20 the new moisture soil 20
	 */
	public void setMoisture_Soil_20(double moisture_Soil_20) {
		this.moisture_Soil_20 = moisture_Soil_20;
	}

	/**
	 * Gets the moisture soil 30.
	 *
	 * @return the moisture soil 30
	 */
	public double getMoisture_Soil_30() {
		return moisture_Soil_30;
	}

	/**
	 * Sets the moisture soil 30.
	 *
	 * @param moisture_Soil_30 the new moisture soil 30
	 */
	public void setMoisture_Soil_30(double moisture_Soil_30) {
		this.moisture_Soil_30 = moisture_Soil_30;
	}

	/**
	 * Gets the moisture soil 40.
	 *
	 * @return the moisture soil 40
	 */
	public double getMoisture_Soil_40() {
		return moisture_Soil_40;
	}

	/**
	 * Sets the moisture soil 40.
	 *
	 * @param moisture_Soil_40 the new moisture soil 40
	 */
	public void setMoisture_Soil_40(double moisture_Soil_40) {
		this.moisture_Soil_40 = moisture_Soil_40;
	}

	/**
	 * Gets the moisture soil 50.
	 *
	 * @return the moisture soil 50
	 */
	public double getMoisture_Soil_50() {
		return moisture_Soil_50;
	}

	/**
	 * Sets the moisture soil 50.
	 *
	 * @param moisture_Soil_50 the new moisture soil 50
	 */
	public void setMoisture_Soil_50(double moisture_Soil_50) {
		this.moisture_Soil_50 = moisture_Soil_50;
	}

	/**
	 * Gets the relative humidity soil 10.
	 *
	 * @return the relative humidity soil 10
	 */
	public double getRelativeHumidity_Soil_10() {
		return relativeHumidity_Soil_10;
	}

	/**
	 * Sets the relative humidity soil 10.
	 *
	 * @param relativeHumidity_Soil_10 the new relative humidity soil 10
	 */
	public void setRelativeHumidity_Soil_10(double relativeHumidity_Soil_10) {
		this.relativeHumidity_Soil_10 = relativeHumidity_Soil_10;
	}

	/**
	 * Gets the relative humidity soil 20.
	 *
	 * @return the relative humidity soil 20
	 */
	public double getRelativeHumidity_Soil_20() {
		return relativeHumidity_Soil_20;
	}

	/**
	 * Sets the relative humidity soil 20.
	 *
	 * @param relativeHumidity_Soil_20 the new relative humidity soil 20
	 */
	public void setRelativeHumidity_Soil_20(double relativeHumidity_Soil_20) {
		this.relativeHumidity_Soil_20 = relativeHumidity_Soil_20;
	}

	/**
	 * Gets the relative humidity soil 30.
	 *
	 * @return the relative humidity soil 30
	 */
	public double getRelativeHumidity_Soil_30() {
		return relativeHumidity_Soil_30;
	}

	/**
	 * Sets the relative humidity soil 30.
	 *
	 * @param relativeHumidity_Soil_30 the new relative humidity soil 30
	 */
	public void setRelativeHumidity_Soil_30(double relativeHumidity_Soil_30) {
		this.relativeHumidity_Soil_30 = relativeHumidity_Soil_30;
	}

	/**
	 * Gets the relative humidity soil 40.
	 *
	 * @return the relative humidity soil 40
	 */
	public double getRelativeHumidity_Soil_40() {
		return relativeHumidity_Soil_40;
	}

	/**
	 * Sets the relative humidity soil 40.
	 *
	 * @param relativeHumidity_Soil_40 the new relative humidity soil 40
	 */
	public void setRelativeHumidity_Soil_40(double relativeHumidity_Soil_40) {
		this.relativeHumidity_Soil_40 = relativeHumidity_Soil_40;
	}

	/**
	 * Gets the relative humidity soil 50.
	 *
	 * @return the relative humidity soil 50
	 */
	public double getRelativeHumidity_Soil_50() {
		return relativeHumidity_Soil_50;
	}

	/**
	 * Sets the relative humidity soil 50.
	 *
	 * @param relativeHumidity_Soil_50 the new relative humidity soil 50
	 */
	public void setRelativeHumidity_Soil_50(double relativeHumidity_Soil_50) {
		this.relativeHumidity_Soil_50 = relativeHumidity_Soil_50;
	}

	
	

}
