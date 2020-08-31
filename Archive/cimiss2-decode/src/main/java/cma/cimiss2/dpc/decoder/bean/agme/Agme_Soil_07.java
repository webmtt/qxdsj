package cma.cimiss2.dpc.decoder.bean.agme;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * 干土层与地下水位子要素
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
public class Agme_Soil_07 extends AgmeReportHeader {
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
	 * nameCN: 干土层厚度 <br>
	 * unit: 厘米（cm）<br>
	 * BUFR FXY: V71115 <br>
	 * descriptionCN: 任何时次的测定值
	 * 
	 */
	Double drySoilThickness;
	/**
	 * NO: 1.22  <br>
	 * nameCN: 地下水位 <br>
	 * unit: 0.1米<br>
	 * BUFR FXY: V71115 <br>
	 * descriptionCN: 任何时次的测定值。大于等于2米未测量时编9200
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
