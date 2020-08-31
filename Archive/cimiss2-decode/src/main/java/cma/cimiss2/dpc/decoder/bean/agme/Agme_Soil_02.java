package cma.cimiss2.dpc.decoder.bean.agme;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * 土壤相对湿度数据文件格式
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
 * @author wangzunpeng
 */
public class Agme_Soil_02 extends AgmeReportHeader {
	/**
	 * NO: 1.6  <br>
	 * nameCN: 资料时间 <br>
	 * unit: 日期<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 资料时间
	 */
	Date obsTime;
	/**
	 * NO: 1.7  <br>
	 * nameCN: 地段类型 <br>
	 * unit: <br>
	 * BUFR FXY: V71115 <br>
	 * descriptionCN: 0为作物观测地段 1为固定观测地段 2为加密观测地段 3为其他观测地段
	 * 
	 */
	Double geographyType;

	/**
	 * NO: 1.8  <br>
	 * nameCN: 作物名称 <br>
	 * unit: <br>
	 * BUFR FXY: V71115 <br>
	 * descriptionCN: 详见《编码》作物名称部分
	 * 
	 */
	Double cropName;
	/**
	 * NO: 1.9  <br>
	 * nameCN: 发育期 <br>
	 * unit: <br>
	 * BUFR FXY: V71115 <br>
	 * descriptionCN: 详见《编码》作物发育期部分
	 * 
	 */
	Double cropPeriod;
	/**
	 * NO: 1.10  <br>
	 * nameCN: 干土层厚度 <br>
	 * unit: 厘米（cm）<br>
	 * BUFR FXY: V71115 <br>
	 * descriptionCN: 仅含与测墒同步的观测量
	 * 
	 */
	Double drySoilThickness;
	/**
	 * NO: 1.11  <br>
	 * nameCN: 10cm土壤相对湿度 <br>
	 * unit: %<br>
	 * BUFR FXY: V71115 <br>
	 * descriptionCN: 各土层土壤相对湿度测量值
	 * 
	 */
	Double soilHumidity_10;
	/**
	 * NO: 1.12  <br>
	 * nameCN: 20cm土壤相对湿度 <br>
	 * unit: %<br>
	 * BUFR FXY: V71115 <br>
	 * descriptionCN: 各土层土壤相对湿度测量值
	 * 
	 */
	Double soilHumidity_20;
	/**
	 * NO: 1.13  <br>
	 * nameCN: 30cm土壤相对湿度 <br>
	 * unit: %<br>
	 * BUFR FXY: V71115 <br>
	 * descriptionCN: 各土层土壤相对湿度测量值
	 * 
	 */
	Double soilHumidity_30;
	/**
	 * NO: 1.14  <br>
	 * nameCN: 40cm土壤相对湿度 <br>
	 * unit: %<br>
	 * BUFR FXY: V71115 <br>
	 * descriptionCN: 各土层土壤相对湿度测量值
	 * 
	 */
	Double soilHumidity_40;
	/**
	 * NO: 1.15  <br>
	 * nameCN: 50cm土壤相对湿度 <br>
	 * unit: %<br>
	 * BUFR FXY: V71115 <br>
	 * descriptionCN: 各土层土壤相对湿度测量值
	 * 
	 */
	Double soilHumidity_50;
	/**
	 * NO: 1.16  <br>
	 * nameCN: 60cm土壤相对湿度 <br>
	 * unit: %<br>
	 * BUFR FXY: V71115 <br>
	 * descriptionCN: 各土层土壤相对湿度测量值
	 * 
	 */
	Double soilHumidity_60;
	/**
	 * NO: 1.17  <br>
	 * nameCN: 70cm土壤相对湿度 <br>
	 * unit: %<br>
	 * BUFR FXY: V71115 <br>
	 * descriptionCN: 各土层土壤相对湿度测量值
	 * 
	 */
	Double soilHumidity_70;
	/**
	 * NO: 1.18  <br>
	 * nameCN: 80cm土壤相对湿度 <br>
	 * unit: %<br>
	 * BUFR FXY: V71115 <br>
	 * descriptionCN: 各土层土壤相对湿度测量值
	 * 
	 */
	Double soilHumidity_80;
	/**
	 * NO: 1.19  <br>
	 * nameCN: 90cm土壤相对湿度 <br>
	 * unit: %<br>
	 * BUFR FXY: V71115 <br>
	 * descriptionCN: 各土层土壤相对湿度测量值
	 * 
	 */
	Double soilHumidity_90;
	/**
	 * NO: 1.20  <br>
	 * nameCN: 100cm土壤相对湿度 <br>
	 * unit: %<br>
	 * BUFR FXY: V71115 <br>
	 * descriptionCN: 各土层土壤相对湿度测量值
	 * 
	 */
	Double soilHumidity_100;
	/**
	 * NO: 1.21  <br>
	 * nameCN: 灌溉或降水 <br>
	 * unit: %<br>
	 * BUFR FXY: V71115 <br>
	 * descriptionCN: 0为无灌溉和降水 1为有灌溉或降水
	 * 
	 */
	Double irrigationOrPrecipitation;
	/**
	 * NO: 1.22  <br>
	 * nameCN: 地下水位 <br>
	 * unit: 0.1米<br>
	 * BUFR FXY: V71115 <br>
	 * descriptionCN: 仅含与测墒同步的观测量。大于等于2米编20
	 */
	Double groundWaterLevel;

	/**
	 * Gets the obs time.
	 *
	 * @return the obs time
	 */
	public Date getObsTime() {
		return obsTime;
	}

	/**
	 * Sets the obs time.
	 *
	 * @param obsTime the new obs time
	 */
	public void setObsTime(Date obsTime) {
		this.obsTime = obsTime;
	}

	/**
	 * Gets the geography type.
	 *
	 * @return the geography type
	 */
	public Double getGeographyType() {
		return geographyType;
	}

	/**
	 * Sets the geography type.
	 *
	 * @param geographyType the new geography type
	 */
	public void setGeographyType(Double geographyType) {
		this.geographyType = geographyType;
	}

	/**
	 * Gets the crop name.
	 *
	 * @return the crop name
	 */
	public Double getCropName() {
		return cropName;
	}

	/**
	 * Sets the crop name.
	 *
	 * @param cropName the new crop name
	 */
	public void setCropName(Double cropName) {
		this.cropName = cropName;
	}

	/**
	 * Gets the crop period.
	 *
	 * @return the crop period
	 */
	public Double getCropPeriod() {
		return cropPeriod;
	}

	/**
	 * Sets the crop period.
	 *
	 * @param cropPeriod the new crop period
	 */
	public void setCropPeriod(Double cropPeriod) {
		this.cropPeriod = cropPeriod;
	}

	/**
	 * Gets the dry soil thickness.
	 *
	 * @return the dry soil thickness
	 */
	public Double getDrySoilThickness() {
		return drySoilThickness;
	}

	/**
	 * Sets the dry soil thickness.
	 *
	 * @param drySoilThickness the new dry soil thickness
	 */
	public void setDrySoilThickness(Double drySoilThickness) {
		this.drySoilThickness = drySoilThickness;
	}

	/**
	 * Gets the soil humidity 10.
	 *
	 * @return the soil humidity 10
	 */
	public Double getSoilHumidity_10() {
		return soilHumidity_10;
	}

	/**
	 * Sets the soil humidity 10.
	 *
	 * @param soilHumidity_10 the new soil humidity 10
	 */
	public void setSoilHumidity_10(Double soilHumidity_10) {
		this.soilHumidity_10 = soilHumidity_10;
	}

	/**
	 * Gets the soil humidity 20.
	 *
	 * @return the soil humidity 20
	 */
	public Double getSoilHumidity_20() {
		return soilHumidity_20;
	}

	/**
	 * Sets the soil humidity 20.
	 *
	 * @param soilHumidity_20 the new soil humidity 20
	 */
	public void setSoilHumidity_20(Double soilHumidity_20) {
		this.soilHumidity_20 = soilHumidity_20;
	}

	/**
	 * Gets the soil humidity 30.
	 *
	 * @return the soil humidity 30
	 */
	public Double getSoilHumidity_30() {
		return soilHumidity_30;
	}

	/**
	 * Sets the soil humidity 30.
	 *
	 * @param soilHumidity_30 the new soil humidity 30
	 */
	public void setSoilHumidity_30(Double soilHumidity_30) {
		this.soilHumidity_30 = soilHumidity_30;
	}

	/**
	 * Gets the soil humidity 40.
	 *
	 * @return the soil humidity 40
	 */
	public Double getSoilHumidity_40() {
		return soilHumidity_40;
	}

	/**
	 * Sets the soil humidity 40.
	 *
	 * @param soilHumidity_40 the new soil humidity 40
	 */
	public void setSoilHumidity_40(Double soilHumidity_40) {
		this.soilHumidity_40 = soilHumidity_40;
	}

	/**
	 * Gets the soil humidity 50.
	 *
	 * @return the soil humidity 50
	 */
	public Double getSoilHumidity_50() {
		return soilHumidity_50;
	}

	/**
	 * Sets the soil humidity 50.
	 *
	 * @param soilHumidity_50 the new soil humidity 50
	 */
	public void setSoilHumidity_50(Double soilHumidity_50) {
		this.soilHumidity_50 = soilHumidity_50;
	}

	/**
	 * Gets the soil humidity 60.
	 *
	 * @return the soil humidity 60
	 */
	public Double getSoilHumidity_60() {
		return soilHumidity_60;
	}

	/**
	 * Sets the soil humidity 60.
	 *
	 * @param soilHumidity_60 the new soil humidity 60
	 */
	public void setSoilHumidity_60(Double soilHumidity_60) {
		this.soilHumidity_60 = soilHumidity_60;
	}

	/**
	 * Gets the soil humidity 70.
	 *
	 * @return the soil humidity 70
	 */
	public Double getSoilHumidity_70() {
		return soilHumidity_70;
	}

	/**
	 * Sets the soil humidity 70.
	 *
	 * @param soilHumidity_70 the new soil humidity 70
	 */
	public void setSoilHumidity_70(Double soilHumidity_70) {
		this.soilHumidity_70 = soilHumidity_70;
	}

	/**
	 * Gets the soil humidity 80.
	 *
	 * @return the soil humidity 80
	 */
	public Double getSoilHumidity_80() {
		return soilHumidity_80;
	}

	/**
	 * Sets the soil humidity 80.
	 *
	 * @param soilHumidity_80 the new soil humidity 80
	 */
	public void setSoilHumidity_80(Double soilHumidity_80) {
		this.soilHumidity_80 = soilHumidity_80;
	}

	/**
	 * Gets the soil humidity 90.
	 *
	 * @return the soil humidity 90
	 */
	public Double getSoilHumidity_90() {
		return soilHumidity_90;
	}

	/**
	 * Sets the soil humidity 90.
	 *
	 * @param soilHumidity_90 the new soil humidity 90
	 */
	public void setSoilHumidity_90(Double soilHumidity_90) {
		this.soilHumidity_90 = soilHumidity_90;
	}

	/**
	 * Gets the soil humidity 100.
	 *
	 * @return the soil humidity 100
	 */
	public Double getSoilHumidity_100() {
		return soilHumidity_100;
	}

	/**
	 * Sets the soil humidity 100.
	 *
	 * @param soilHumidity_100 the new soil humidity 100
	 */
	public void setSoilHumidity_100(Double soilHumidity_100) {
		this.soilHumidity_100 = soilHumidity_100;
	}

	/**
	 * Gets the irrigation or precipitation.
	 *
	 * @return the irrigation or precipitation
	 */
	public Double getIrrigationOrPrecipitation() {
		return irrigationOrPrecipitation;
	}

	/**
	 * Sets the irrigation or precipitation.
	 *
	 * @param irrigationOrPrecipitation the new irrigation or precipitation
	 */
	public void setIrrigationOrPrecipitation(Double irrigationOrPrecipitation) {
		this.irrigationOrPrecipitation = irrigationOrPrecipitation;
	}

	/**
	 * Gets the ground water level.
	 *
	 * @return the ground water level
	 */
	public Double getGroundWaterLevel() {
		return groundWaterLevel;
	}

	/**
	 * Sets the ground water level.
	 *
	 * @param groundWaterLevel the new ground water level
	 */
	public void setGroundWaterLevel(Double groundWaterLevel) {
		this.groundWaterLevel = groundWaterLevel;
	}

}
