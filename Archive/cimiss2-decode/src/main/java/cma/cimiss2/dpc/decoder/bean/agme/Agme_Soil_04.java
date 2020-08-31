package cma.cimiss2.dpc.decoder.bean.agme;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * 有效水分储存量数据文件格式
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
public class Agme_Soil_04 extends AgmeReportHeader {
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
	 * NO: 1.11  <br>
	 * nameCN: 10cm有效水分储存量 <br>
	 * unit:毫米(mm)<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 土壤中含有的大于凋萎湿度的水分储存量
	 * 
	 */
	Double validSoilWaterContent_10;
	/**
	 * NO: 1.12  <br>
	 * nameCN: 20cm有效水分储存量<br>
	 * unit: 毫米(mm)<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 土壤中含有的大于凋萎湿度的水分储存量
	 * 
	 */
	Double validSoilWaterContent_20;
	/**
	 * NO: 1.13  <br>
	 * nameCN: 30cm有效水分储存量 <br>
	 * unit: 毫米(mm)<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 土壤中含有的大于凋萎湿度的水分储存量
	 * 
	 */
	Double validSoilWaterContent_30;
	/**
	 * NO: 1.14  <br>
	 * nameCN: 40cm有效水分储存量 <br>
	 * unit: %<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 土壤中含有的大于凋萎湿度的水分储存量
	 * 
	 */
	Double validSoilWaterContent_40;
	/**
	 * NO: 1.15  <br>
	 * nameCN: 50cm有效水分储存量 <br>
	 * unit: %<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 土壤中含有的大于凋萎湿度的水分储存量
	 * 
	 */
	Double validSoilWaterContent_50;
	/**
	 * NO: 1.16  <br>
	 * nameCN: 60cm有效水分储存量<br>
	 * unit: %<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 土壤中含有的大于凋萎湿度的水分储存量
	 * 
	 */
	Double validSoilWaterContent_60;
	/**
	 * NO: 1.17  <br>
	 * nameCN: 70cm有效水分储存量 <br>
	 * unit: 毫米(mm)<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 土壤中含有的大于凋萎湿度的水分储存量
	 * 
	 */
	Double validSoilWaterContent_70;
	/**
	 * NO: 1.18  <br>
	 * nameCN: 80cm有效水分储存量 <br>
	 * unit: 毫米(mm)<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 土壤中含有的大于凋萎湿度的水分储存量
	 * 
	 */
	Double validSoilWaterContent_80;
	/**
	 * NO: 1.19  <br>
	 * nameCN: 90cm有效水分储存量 <br>
	 * unit: 毫米(mm)<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 土壤中含有的大于凋萎湿度的水分储存量
	 * 
	 */
	Double validSoilWaterContent_90;
	/**
	 * NO: 1.20  <br>
	 * nameCN: 100cm有效水分储存量 <br>
	 * unit: 毫米(mm)<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 土壤中含有的大于凋萎湿度的水分储存量
	 * 
	 */
	Double validSoilWaterContent_100;

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
	 * Gets the valid soil water content 10.
	 *
	 * @return the valid soil water content 10
	 */
	public Double getValidSoilWaterContent_10() {
		return validSoilWaterContent_10;
	}

	/**
	 * Sets the valid soil water content 10.
	 *
	 * @param validSoilWaterContent_10 the new valid soil water content 10
	 */
	public void setValidSoilWaterContent_10(Double validSoilWaterContent_10) {
		this.validSoilWaterContent_10 = validSoilWaterContent_10;
	}

	/**
	 * Gets the valid soil water content 20.
	 *
	 * @return the valid soil water content 20
	 */
	public Double getValidSoilWaterContent_20() {
		return validSoilWaterContent_20;
	}

	/**
	 * Sets the valid soil water content 20.
	 *
	 * @param validSoilWaterContent_20 the new valid soil water content 20
	 */
	public void setValidSoilWaterContent_20(Double validSoilWaterContent_20) {
		this.validSoilWaterContent_20 = validSoilWaterContent_20;
	}

	/**
	 * Gets the valid soil water content 30.
	 *
	 * @return the valid soil water content 30
	 */
	public Double getValidSoilWaterContent_30() {
		return validSoilWaterContent_30;
	}

	/**
	 * Sets the valid soil water content 30.
	 *
	 * @param validSoilWaterContent_30 the new valid soil water content 30
	 */
	public void setValidSoilWaterContent_30(Double validSoilWaterContent_30) {
		this.validSoilWaterContent_30 = validSoilWaterContent_30;
	}

	/**
	 * Gets the valid soil water content 40.
	 *
	 * @return the valid soil water content 40
	 */
	public Double getValidSoilWaterContent_40() {
		return validSoilWaterContent_40;
	}

	/**
	 * Sets the valid soil water content 40.
	 *
	 * @param validSoilWaterContent_40 the new valid soil water content 40
	 */
	public void setValidSoilWaterContent_40(Double validSoilWaterContent_40) {
		this.validSoilWaterContent_40 = validSoilWaterContent_40;
	}

	/**
	 * Gets the valid soil water content 50.
	 *
	 * @return the valid soil water content 50
	 */
	public Double getValidSoilWaterContent_50() {
		return validSoilWaterContent_50;
	}

	/**
	 * Sets the valid soil water content 50.
	 *
	 * @param validSoilWaterContent_50 the new valid soil water content 50
	 */
	public void setValidSoilWaterContent_50(Double validSoilWaterContent_50) {
		this.validSoilWaterContent_50 = validSoilWaterContent_50;
	}

	/**
	 * Gets the valid soil water content 60.
	 *
	 * @return the valid soil water content 60
	 */
	public Double getValidSoilWaterContent_60() {
		return validSoilWaterContent_60;
	}

	/**
	 * Sets the valid soil water content 60.
	 *
	 * @param validSoilWaterContent_60 the new valid soil water content 60
	 */
	public void setValidSoilWaterContent_60(Double validSoilWaterContent_60) {
		this.validSoilWaterContent_60 = validSoilWaterContent_60;
	}

	/**
	 * Gets the valid soil water content 70.
	 *
	 * @return the valid soil water content 70
	 */
	public Double getValidSoilWaterContent_70() {
		return validSoilWaterContent_70;
	}

	/**
	 * Sets the valid soil water content 70.
	 *
	 * @param validSoilWaterContent_70 the new valid soil water content 70
	 */
	public void setValidSoilWaterContent_70(Double validSoilWaterContent_70) {
		this.validSoilWaterContent_70 = validSoilWaterContent_70;
	}

	/**
	 * Gets the valid soil water content 80.
	 *
	 * @return the valid soil water content 80
	 */
	public Double getValidSoilWaterContent_80() {
		return validSoilWaterContent_80;
	}

	/**
	 * Sets the valid soil water content 80.
	 *
	 * @param validSoilWaterContent_80 the new valid soil water content 80
	 */
	public void setValidSoilWaterContent_80(Double validSoilWaterContent_80) {
		this.validSoilWaterContent_80 = validSoilWaterContent_80;
	}

	/**
	 * Gets the valid soil water content 90.
	 *
	 * @return the valid soil water content 90
	 */
	public Double getValidSoilWaterContent_90() {
		return validSoilWaterContent_90;
	}

	/**
	 * Sets the valid soil water content 90.
	 *
	 * @param validSoilWaterContent_90 the new valid soil water content 90
	 */
	public void setValidSoilWaterContent_90(Double validSoilWaterContent_90) {
		this.validSoilWaterContent_90 = validSoilWaterContent_90;
	}

	/**
	 * Gets the valid soil water content 100.
	 *
	 * @return the valid soil water content 100
	 */
	public Double getValidSoilWaterContent_100() {
		return validSoilWaterContent_100;
	}

	/**
	 * Sets the valid soil water content 100.
	 *
	 * @param validSoilWaterContent_100 the new valid soil water content 100
	 */
	public void setValidSoilWaterContent_100(Double validSoilWaterContent_100) {
		this.validSoilWaterContent_100 = validSoilWaterContent_100;
	}

}
