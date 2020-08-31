package cma.cimiss2.dpc.decoder.bean.agme;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * 降水灌溉与渗透子要素
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
public class Agme_Soil_08 extends AgmeReportHeader {
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
	 * NO: 1.8  <br>
	 * nameCN: 降水灌溉与渗透项目 <br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 0为降水；1为灌溉；2为渗透
	 * 
	 */
	Double preOrIrrigationOrInfiltrationPro;

	/**
	 * NO: 1.9  <br>
	 * nameCN: 降水灌溉量或渗透深度 <br>
	 * unit: 0.1毫米（mm）/立方米/厘米（cm）<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 观测时段内合计量；透雨或接墒编9998
	 * 
	 */
	Double preOrIrrigationOrInfiltrationDepth;

	/**
	 * NO: 1.10  <br>
	 * nameCN: 出现时间 <br>
	 * unit: 字符<br>
	 * BUFR FXY: <br>
	 * descriptionCN: 降水灌溉量或渗透出现的时间
	 * 
	 */
	String occurTime;

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
	 * Gets the pre or irrigation or infiltration pro.
	 *
	 * @return the pre or irrigation or infiltration pro
	 */
	public Double getPreOrIrrigationOrInfiltrationPro() {
		return preOrIrrigationOrInfiltrationPro;
	}

	/**
	 * Sets the pre or irrigation or infiltration pro.
	 *
	 * @param preOrIrrigationOrInfiltrationPro the new pre or irrigation or infiltration pro
	 */
	public void setPreOrIrrigationOrInfiltrationPro(Double preOrIrrigationOrInfiltrationPro) {
		this.preOrIrrigationOrInfiltrationPro = preOrIrrigationOrInfiltrationPro;
	}

	/**
	 * Gets the pre or irrigation or infiltration depth.
	 *
	 * @return the pre or irrigation or infiltration depth
	 */
	public Double getPreOrIrrigationOrInfiltrationDepth() {
		return preOrIrrigationOrInfiltrationDepth;
	}

	/**
	 * Sets the pre or irrigation or infiltration depth.
	 *
	 * @param preOrIrrigationOrInfiltrationDepth the new pre or irrigation or infiltration depth
	 */
	public void setPreOrIrrigationOrInfiltrationDepth(Double preOrIrrigationOrInfiltrationDepth) {
		this.preOrIrrigationOrInfiltrationDepth = preOrIrrigationOrInfiltrationDepth;
	}

	/**
	 * Gets the occur time.
	 *
	 * @return the occur time
	 */
	public String getOccurTime() {
		return occurTime;
	}

	/**
	 * Sets the occur time.
	 *
	 * @param occurTime the new occur time
	 */
	public void setOccurTime(String occurTime) {
		this.occurTime = occurTime;
	}

}
