package cma.cimiss2.dpc.decoder.bean.agme;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * 土壤水文物理特性数据文件格式
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
public class Agme_Soil_01 extends AgmeReportHeader {
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
	 * nameCN: 土层深度 <br>
	 * unit: 厘米（cm）<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 分别为10,20,…,100厘米10个层
	 */
	Double soilDepth;
	/**
	 * NO: 1.8  <br>
	 * nameCN: 田间持水量 <br>
	 * unit: 0.1%<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 土壤所能保持的毛管悬着水的最大量
	 */
	Double soilWaterContent;
	/**
	 * NO: 1.8  <br>
	 * nameCN: 土壤容重 <br>
	 * unit: 0.01克/ 立方厘米<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 单位体积内的干土重
	 */
	Double soilBulkDensity;
	/**
	 * NO: 1.8  <br>
	 * nameCN: 凋萎湿度 <br>
	 * unit:0.1% <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 致使植株叶片开始呈现凋萎状态时的土壤湿度
	 */
	Double wiltingHumidity;

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
	 * Gets the soil depth.
	 *
	 * @return the soil depth
	 */
	public Double getSoilDepth() {
		return soilDepth;
	}

	/**
	 * Sets the soil depth.
	 *
	 * @param soilDepth the new soil depth
	 */
	public void setSoilDepth(Double soilDepth) {
		this.soilDepth = soilDepth;
	}

	/**
	 * Gets the soil water content.
	 *
	 * @return the soil water content
	 */
	public Double getSoilWaterContent() {
		return soilWaterContent;
	}

	/**
	 * Sets the soil water content.
	 *
	 * @param soilWaterContent the new soil water content
	 */
	public void setSoilWaterContent(Double soilWaterContent) {
		this.soilWaterContent = soilWaterContent;
	}

	/**
	 * Gets the soil bulk density.
	 *
	 * @return the soil bulk density
	 */
	public Double getSoilBulkDensity() {
		return soilBulkDensity;
	}

	/**
	 * Sets the soil bulk density.
	 *
	 * @param soilBulkDensity the new soil bulk density
	 */
	public void setSoilBulkDensity(Double soilBulkDensity) {
		this.soilBulkDensity = soilBulkDensity;
	}

	/**
	 * Gets the wilting humidity.
	 *
	 * @return the wilting humidity
	 */
	public Double getWiltingHumidity() {
		return wiltingHumidity;
	}

	/**
	 * Sets the wilting humidity.
	 *
	 * @param wiltingHumidity the new wilting humidity
	 */
	public void setWiltingHumidity(Double wiltingHumidity) {
		this.wiltingHumidity = wiltingHumidity;
	}

}
