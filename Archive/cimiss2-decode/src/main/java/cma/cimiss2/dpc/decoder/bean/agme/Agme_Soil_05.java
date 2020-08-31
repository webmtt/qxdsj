package cma.cimiss2.dpc.decoder.bean.agme;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * 土壤冻结与解冻数据文件格式
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
public class Agme_Soil_05 extends AgmeReportHeader {
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
	 * NO: 1.10  <br>
	 * nameCN: 土层深度 <br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 0为表层；1为10cm；2为20cm
	 * 
	 */
	Double soilDepth;
	/**
	 * NO: 1.11  <br>
	 * nameCN: 土层状态 <br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 0为冻结；1为解冻
	 * 
	 */
	Double soilStatus;

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
	 * Gets the soil status.
	 *
	 * @return the soil status
	 */
	public Double getSoilStatus() {
		return soilStatus;
	}

	/**
	 * Sets the soil status.
	 *
	 * @param soilStatus the new soil status
	 */
	public void setSoilStatus(Double soilStatus) {
		this.soilStatus = soilStatus;
	}

}
