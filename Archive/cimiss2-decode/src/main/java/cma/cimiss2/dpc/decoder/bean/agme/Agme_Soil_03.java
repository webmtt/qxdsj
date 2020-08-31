package cma.cimiss2.dpc.decoder.bean.agme;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * 水分总储存量数据文件格式
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
public class Agme_Soil_03 extends AgmeReportHeader {
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
	 * nameCN: 10cm水分总储存量 <br>
	 * unit:毫米(mm)<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 一定深度的土壤中总的含水量
	 * 
	 */
	Double soilWaterContent_10;
	/**
	 * NO: 1.12  <br>
	 * nameCN: 20cm水分总储存量 <br>
	 * unit: 毫米(mm)<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 一定深度的土壤中总的含水量
	 * 
	 */
	Double soilWaterContent_20;
	/**
	 * NO: 1.13  <br>
	 * nameCN: 30cm水分总储存量 <br>
	 * unit: 毫米(mm)<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 一定深度的土壤中总的含水量
	 * 
	 */
	Double soilWaterContent_30;
	/**
	 * NO: 1.14  <br>
	 * nameCN: 40cm水分总储存量 <br>
	 * unit: %<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 各土层土壤相对湿度测量值
	 * 
	 */
	Double soilWaterContent_40;
	/**
	 * NO: 1.15  <br>
	 * nameCN: 50cm水分总储存量 <br>
	 * unit: %<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 各土层土壤相对湿度测量值
	 * 
	 */
	Double soilWaterContent_50;
	/**
	 * NO: 1.16  <br>
	 * nameCN: 60cm水分总储存量 <br>
	 * unit: %<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 各土层土壤相对湿度测量值
	 * 
	 */
	Double soilWaterContent_60;
	/**
	 * NO: 1.17  <br>
	 * nameCN: 70cm水分总储存量 <br>
	 * unit: 毫米(mm)<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 一定深度的土壤中总的含水量
	 * 
	 */
	Double soilWaterContent_70;
	/**
	 * NO: 1.18  <br>
	 * nameCN: 80cm水分总储存量 <br>
	 * unit: 毫米(mm)<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 一定深度的土壤中总的含水量
	 * 
	 */
	Double soilWaterContent_80;
	/**
	 * NO: 1.19  <br>
	 * nameCN: 90cm水分总储存量 <br>
	 * unit: 毫米(mm)<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 一定深度的土壤中总的含水量
	 * 
	 */
	Double soilWaterContent_90;
	/**
	 * NO: 1.20  <br>
	 * nameCN: 100cm水分总储存量 <br>
	 * unit: 毫米(mm)<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 一定深度的土壤中总的含水量
	 * 
	 */
	Double soilWaterContent_100;

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
	 * Gets the soil water content 10.
	 *
	 * @return the soil water content 10
	 */
	public Double getSoilWaterContent_10() {
		return soilWaterContent_10;
	}

	/**
	 * Sets the soil water content 10.
	 *
	 * @param soilWaterContent_10 the new soil water content 10
	 */
	public void setSoilWaterContent_10(Double soilWaterContent_10) {
		this.soilWaterContent_10 = soilWaterContent_10;
	}

	/**
	 * Gets the soil water content 20.
	 *
	 * @return the soil water content 20
	 */
	public Double getSoilWaterContent_20() {
		return soilWaterContent_20;
	}

	/**
	 * Sets the soil water content 20.
	 *
	 * @param soilWaterContent_20 the new soil water content 20
	 */
	public void setSoilWaterContent_20(Double soilWaterContent_20) {
		this.soilWaterContent_20 = soilWaterContent_20;
	}

	/**
	 * Gets the soil water content 30.
	 *
	 * @return the soil water content 30
	 */
	public Double getSoilWaterContent_30() {
		return soilWaterContent_30;
	}

	/**
	 * Sets the soil water content 30.
	 *
	 * @param soilWaterContent_30 the new soil water content 30
	 */
	public void setSoilWaterContent_30(Double soilWaterContent_30) {
		this.soilWaterContent_30 = soilWaterContent_30;
	}

	/**
	 * Gets the soil water content 40.
	 *
	 * @return the soil water content 40
	 */
	public Double getSoilWaterContent_40() {
		return soilWaterContent_40;
	}

	/**
	 * Sets the soil water content 40.
	 *
	 * @param soilWaterContent_40 the new soil water content 40
	 */
	public void setSoilWaterContent_40(Double soilWaterContent_40) {
		this.soilWaterContent_40 = soilWaterContent_40;
	}

	/**
	 * Gets the soil water content 50.
	 *
	 * @return the soil water content 50
	 */
	public Double getSoilWaterContent_50() {
		return soilWaterContent_50;
	}

	/**
	 * Sets the soil water content 50.
	 *
	 * @param soilWaterContent_50 the new soil water content 50
	 */
	public void setSoilWaterContent_50(Double soilWaterContent_50) {
		this.soilWaterContent_50 = soilWaterContent_50;
	}

	/**
	 * Gets the soil water content 60.
	 *
	 * @return the soil water content 60
	 */
	public Double getSoilWaterContent_60() {
		return soilWaterContent_60;
	}

	/**
	 * Sets the soil water content 60.
	 *
	 * @param soilWaterContent_60 the new soil water content 60
	 */
	public void setSoilWaterContent_60(Double soilWaterContent_60) {
		this.soilWaterContent_60 = soilWaterContent_60;
	}

	/**
	 * Gets the soil water content 70.
	 *
	 * @return the soil water content 70
	 */
	public Double getSoilWaterContent_70() {
		return soilWaterContent_70;
	}

	/**
	 * Sets the soil water content 70.
	 *
	 * @param soilWaterContent_70 the new soil water content 70
	 */
	public void setSoilWaterContent_70(Double soilWaterContent_70) {
		this.soilWaterContent_70 = soilWaterContent_70;
	}

	/**
	 * Gets the soil water content 80.
	 *
	 * @return the soil water content 80
	 */
	public Double getSoilWaterContent_80() {
		return soilWaterContent_80;
	}

	/**
	 * Sets the soil water content 80.
	 *
	 * @param soilWaterContent_80 the new soil water content 80
	 */
	public void setSoilWaterContent_80(Double soilWaterContent_80) {
		this.soilWaterContent_80 = soilWaterContent_80;
	}

	/**
	 * Gets the soil water content 90.
	 *
	 * @return the soil water content 90
	 */
	public Double getSoilWaterContent_90() {
		return soilWaterContent_90;
	}

	/**
	 * Sets the soil water content 90.
	 *
	 * @param soilWaterContent_90 the new soil water content 90
	 */
	public void setSoilWaterContent_90(Double soilWaterContent_90) {
		this.soilWaterContent_90 = soilWaterContent_90;
	}

	/**
	 * Gets the soil water content 100.
	 *
	 * @return the soil water content 100
	 */
	public Double getSoilWaterContent_100() {
		return soilWaterContent_100;
	}

	/**
	 * Sets the soil water content 100.
	 *
	 * @param soilWaterContent_100 the new soil water content 100
	 */
	public void setSoilWaterContent_100(Double soilWaterContent_100) {
		this.soilWaterContent_100 = soilWaterContent_100;
	}

}
