package cma.cimiss2.dpc.decoder.bean.agme;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * 土壤重量含水率子要素
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
public class Agme_Soil_06 extends AgmeReportHeader {
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
	 * nameCN: 10cm土壤重量含水率 <br>
	 * unit: 0.1%<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 土壤中含有的大于凋萎湿度的水分储存量
	 * 
	 */
	Double soilWeightWaterContent_10;
	/**
	 * NO: 1.11  <br>
	 * nameCN: 20cm土壤重量含水率 <br>
	 * unit: 0.1%<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 土壤中含有的大于凋萎湿度的水分储存量
	 * 
	 */
	Double soilWeightWaterContent_20;
	/**
	 * NO: 1.12  <br>
	 * nameCN: 30cm土壤重量含水率 <br>
	 * unit: 0.1%<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 土壤中含有的大于凋萎湿度的水分储存量
	 * 
	 */
	Double soilWeightWaterContent_30;
	/**
	 * NO: 1.13  <br>
	 * nameCN: 40cm土壤重量含水率 <br>
	 * unit: 0.1%<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 土壤中含有的大于凋萎湿度的水分储存量
	 * 
	 */
	Double soilWeightWaterContent_40;
	/**
	 * NO: 1.14  <br>
	 * nameCN: 50cm土壤重量含水率 <br>
	 * unit: 0.1%<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 土壤中含有的大于凋萎湿度的水分储存量
	 * 
	 */
	Double soilWeightWaterContent_50;
	/**
	 * NO: 1.15  <br>
	 * nameCN: 60cm土壤重量含水率 <br>
	 * unit: 0.1%<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 土壤中含有的大于凋萎湿度的水分储存量
	 * 
	 */
	Double soilWeightWaterContent_60;
	/**
	 * NO: 1.16  <br>
	 * nameCN: 70cm土壤重量含水率 <br>
	 * unit: 0.1%<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 土壤中含有的大于凋萎湿度的水分储存量
	 * 
	 */
	Double soilWeightWaterContent_70;
	/**
	 * NO: 1.17  <br>
	 * nameCN: 80cm土壤重量含水率 <br>
	 * unit: 0.1%<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 土壤中含有的大于凋萎湿度的水分储存量
	 * 
	 */
	Double soilWeightWaterContent_80;
	/**
	 * NO: 1.18  <br>
	 * nameCN: 90cm土壤重量含水率 <br>
	 * unit: 0.1%<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 土壤中含有的大于凋萎湿度的水分储存量
	 * 
	 */
	Double soilWeightWaterContent_90;
	/**
	 * NO: 1.19  <br>
	 * nameCN: 100cm土壤重量含水率 <br>
	 * unit: 0.1%<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 土壤中含有的大于凋萎湿度的水分储存量
	 * 
	 */
	Double soilWeightWaterContent_100;

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
	 * Gets the soil weight water content 10.
	 *
	 * @return the soil weight water content 10
	 */
	public Double getSoilWeightWaterContent_10() {
		return soilWeightWaterContent_10;
	}

	/**
	 * Sets the soil weight water content 10.
	 *
	 * @param soilWeightWaterContent_10 the new soil weight water content 10
	 */
	public void setSoilWeightWaterContent_10(Double soilWeightWaterContent_10) {
		this.soilWeightWaterContent_10 = soilWeightWaterContent_10;
	}

	/**
	 * Gets the soil weight water content 20.
	 *
	 * @return the soil weight water content 20
	 */
	public Double getSoilWeightWaterContent_20() {
		return soilWeightWaterContent_20;
	}

	/**
	 * Sets the soil weight water content 20.
	 *
	 * @param soilWeightWaterContent_20 the new soil weight water content 20
	 */
	public void setSoilWeightWaterContent_20(Double soilWeightWaterContent_20) {
		this.soilWeightWaterContent_20 = soilWeightWaterContent_20;
	}

	/**
	 * Gets the soil weight water content 30.
	 *
	 * @return the soil weight water content 30
	 */
	public Double getSoilWeightWaterContent_30() {
		return soilWeightWaterContent_30;
	}

	/**
	 * Sets the soil weight water content 30.
	 *
	 * @param soilWeightWaterContent_30 the new soil weight water content 30
	 */
	public void setSoilWeightWaterContent_30(Double soilWeightWaterContent_30) {
		this.soilWeightWaterContent_30 = soilWeightWaterContent_30;
	}

	/**
	 * Gets the soil weight water content 40.
	 *
	 * @return the soil weight water content 40
	 */
	public Double getSoilWeightWaterContent_40() {
		return soilWeightWaterContent_40;
	}

	/**
	 * Sets the soil weight water content 40.
	 *
	 * @param soilWeightWaterContent_40 the new soil weight water content 40
	 */
	public void setSoilWeightWaterContent_40(Double soilWeightWaterContent_40) {
		this.soilWeightWaterContent_40 = soilWeightWaterContent_40;
	}

	/**
	 * Gets the soil weight water content 50.
	 *
	 * @return the soil weight water content 50
	 */
	public Double getSoilWeightWaterContent_50() {
		return soilWeightWaterContent_50;
	}

	/**
	 * Sets the soil weight water content 50.
	 *
	 * @param soilWeightWaterContent_50 the new soil weight water content 50
	 */
	public void setSoilWeightWaterContent_50(Double soilWeightWaterContent_50) {
		this.soilWeightWaterContent_50 = soilWeightWaterContent_50;
	}

	/**
	 * Gets the soil weight water content 60.
	 *
	 * @return the soil weight water content 60
	 */
	public Double getSoilWeightWaterContent_60() {
		return soilWeightWaterContent_60;
	}

	/**
	 * Sets the soil weight water content 60.
	 *
	 * @param soilWeightWaterContent_60 the new soil weight water content 60
	 */
	public void setSoilWeightWaterContent_60(Double soilWeightWaterContent_60) {
		this.soilWeightWaterContent_60 = soilWeightWaterContent_60;
	}

	/**
	 * Gets the soil weight water content 70.
	 *
	 * @return the soil weight water content 70
	 */
	public Double getSoilWeightWaterContent_70() {
		return soilWeightWaterContent_70;
	}

	/**
	 * Sets the soil weight water content 70.
	 *
	 * @param soilWeightWaterContent_70 the new soil weight water content 70
	 */
	public void setSoilWeightWaterContent_70(Double soilWeightWaterContent_70) {
		this.soilWeightWaterContent_70 = soilWeightWaterContent_70;
	}

	/**
	 * Gets the soil weight water content 80.
	 *
	 * @return the soil weight water content 80
	 */
	public Double getSoilWeightWaterContent_80() {
		return soilWeightWaterContent_80;
	}

	/**
	 * Sets the soil weight water content 80.
	 *
	 * @param soilWeightWaterContent_80 the new soil weight water content 80
	 */
	public void setSoilWeightWaterContent_80(Double soilWeightWaterContent_80) {
		this.soilWeightWaterContent_80 = soilWeightWaterContent_80;
	}

	/**
	 * Gets the soil weight water content 90.
	 *
	 * @return the soil weight water content 90
	 */
	public Double getSoilWeightWaterContent_90() {
		return soilWeightWaterContent_90;
	}

	/**
	 * Sets the soil weight water content 90.
	 *
	 * @param soilWeightWaterContent_90 the new soil weight water content 90
	 */
	public void setSoilWeightWaterContent_90(Double soilWeightWaterContent_90) {
		this.soilWeightWaterContent_90 = soilWeightWaterContent_90;
	}

	/**
	 * Gets the soil weight water content 100.
	 *
	 * @return the soil weight water content 100
	 */
	public Double getSoilWeightWaterContent_100() {
		return soilWeightWaterContent_100;
	}

	/**
	 * Sets the soil weight water content 100.
	 *
	 * @param soilWeightWaterContent_100 the new soil weight water content 100
	 */
	public void setSoilWeightWaterContent_100(Double soilWeightWaterContent_100) {
		this.soilWeightWaterContent_100 = soilWeightWaterContent_100;
	}

}
