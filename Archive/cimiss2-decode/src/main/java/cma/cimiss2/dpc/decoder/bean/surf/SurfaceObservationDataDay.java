package cma.cimiss2.dpc.decoder.bean.surf;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import cma.cimiss2.dpc.decoder.annotation.Column;
import cma.cimiss2.dpc.decoder.bean.QCElement;

/**
 * *****************************************************************************************<br> 
 * <strong>All Rights Reserved By National Meteorological Information Centre (NMIC)</strong><br>
 * *****************************************************************************************<br>

 * 国家级站日数据文件格式
 * <p>
 * notes:
 * <ul>
 * <li>定义参考了一下文档
 * 	<ol>
 * 		<li> <a href="../../../附录1：数据文件命名规则和格式11.rtf">《实时-历史地面气象资料一体化数据文件命名规则和格式》</a>
 * 		<li> 《CIMISS系统要素代码表（版本6 初稿）》
 * 		<li> <a href="http://www.emc.ncep.noaa.gov/mmb/data_processing/bufrtab_tableb.htm">《BUFR TABLE B》</a>
 * 	</ol>
 * </li>
 * </ul>
 *
 * @author shevawen
 */
@Getter
@Setter
public class SurfaceObservationDataDay {
    /**
     * NO: 1.1  <br>
     * nameCN: 区站号 <br>
     * unit: <br>
     * BUFR FXY: V01301 <br>
     * descriptionCN: 5位数字或第1位为字母，第2-5位为数字<br>
     * decode rule:直接取值。<br>
     * field rule:直接赋值。
     */
    @Column("V01301")
    String stationNumberChina;

    /**
     * NO: 1.2  <br>
     * nameCN: 纬度 <br>
     * unit: 度 <br>
     * BUFR FXY: V05001 <br>
     * descriptionCN:<br>
     * decode rule:进行纯数字校验.取前两位数字记为a1,中间两位数字除以60记为a2,最后两位数字除以3600记为a3,取值为a1+a2+a3。<br>
     * field rule:直接赋值。
     */
    @Column("V05001")
    Double latitude;
    /**
     * NO: 1.3  <br>
     * nameCN: 经度 <br>
     * unit: 度 <br>
     * BUFR FXY: V06001 <br>
     * descriptionCN:<br>
     * decode rule:进行纯数字校验.取前3位数字记为a1,中间两位数字除以60记为a2,最后两位数字除以3600记为a3,取值为a1+a2+a3。<br>
     * field rule:直接赋值。
     */
    @Column("V06001")
    Double longitude;

    /**
     * NO: 1.4  <br>
     * nameCN: 文件更正标识 <br>
     * unit: <br>
     * BUFR FXY: V_BBB <br>
     * descriptionCN: 为非更正数据时，固定编“000”；为测站更正数据时，编码规则同文件名中的CCx<br>
     * decode rule:直接取值。<br>
     * field rule:直接赋值。
     */
    @Column("V_BBB")
    String correctionIndicator;

    /**
     * NO: 2.1  <br>
     * nameCN: 观测时间 <br>
     * unit: <br>
     * descriptionCN: 其中时分秒固定为“120000”<br>
     * decode rule:直接取值。<br>
     * field rule:使用java时间转化工具类进行转化,标准形式为：yyyyMMddHHmmss。
     */

//    @Column(value = "V04001", pattern = "yyyy")
//    @Column(value = "V04002", pattern = "MM")
//    @Column(value = "V04003", pattern = "dd")
    @Column("D_DATETIME")
    Date observationTime;
    /**
     * NO: 2.2  <br>
     * nameCN: 20-8时雨量筒观测降水量 <br>
     * unit: 1mm <br>V13308
     * descriptionCN:<br>
     * decode rule:进行纯数字校验。取值后除以10。<br>
     * field rule:直接赋值。
     */
    @Column("V13308")
    QCElement<Double> precipitationBetween20And8;
    /**
     * NO: 2.3  <br>
     * nameCN: 8-20时雨量筒观测降水量 <br>
     * unit: 1mm <br>
     * descriptionCN:<br>
     * decode rule:进行纯数字校验。取值后除以10。<br>
     * field rule:直接赋值。
     */
    @Column("V13309")
    QCElement<Double> precipitationBetween8And20;
    /**
     * NO: 2.4  <br>
     * nameCN: 蒸发量 <br>
     * unit: 1mm <br>
     * descriptionCN:<br>
     * decode rule:进行纯数字校验。取值后除以10。<br>
     * field rule:直接赋值。
     */
    @Column("V13032")
    @Column("V13033")
    QCElement<Double> evaporation;//??????????
    /**
     * NO: 2.5  <br>
     * nameCN: 电线积冰-现象 <br>
     * unit: <br>
     * descriptionCN:<br>
     * decode rule:直接取值。<br>
     * field rule:直接赋值。
     */
    @Column("V20305")
    QCElement<String> overheadWireIce;
    /**
     * NO: 2.6  <br>
     * nameCN: 电线积冰-南北方向直径 <br>
     * unit: 1mm<br>
     * descriptionCN:<br>
     * decode rule:直接取值。<br>
     * field rule:直接赋值。
     */
    @Column("V20326_NS")
    QCElement<Double> diameterOfOverheadWireIceNS;
    /**
     * NO: 2.7  <br>
     * nameCN: 电线积冰-南北方向厚度 <br>
     * unit: 1mm<br>
     * descriptionCN:<br>
     * decode rule:直接取值。<br>
     * field rule:直接赋值。
     */
    @Column("V20306_NS")
    QCElement<Double> thicknessOfOverheadWireIceNS;
    /**
     * NO: 2.8  <br>
     * nameCN: 电线积冰-南北方向重量 <br>
     * unit: 1g/m<br>
     * descriptionCN:<br>
     * decode rule:直接取值。<br>
     * field rule:直接赋值。
     */
    @Column("V20307_NS")
    QCElement<Double> weightOfOverheadWireIceNS;
    /**
     * NO: 2.9  <br>
     * nameCN: 电线积冰-东西方向直径 <br>
     * unit: 1mm<br>
     * descriptionCN:<br>
     * decode rule:直接取值。<br>
     * field rule:直接赋值。
     */
    @Column("V20326_WE")
    QCElement<Double> diameterOfOverheadWireIceEW;
    /**
     * NO: 2.10  <br>
     * nameCN: 电线积冰-东西方向厚度 <br>
     * unit: 1mm<br>
     * descriptionCN:<br>
     * decode rule:直接取值。<br>
     * field rule:直接赋值。
     */
    @Column("V20306_WE")
    QCElement<Double> thicknessOfOverheadWireIceEW;
    /**
     * NO: 2.11  <br>
     * nameCN: 电线积冰-东西方向重量 <br>
     * unit: 1g/m<br>
     * descriptionCN:<br>
     * decode rule:直接取值。<br>
     * field rule:直接赋值。
     */
    @Column("V20307_WE")
    QCElement<Double> weightOfOverheadWireIceEW;
    /**
     * NO: 2.12  <br>
     * nameCN: 电线积冰-温度 <br>
     * unit: 1℃<br>
     * descriptionCN:<br>
     * decode rule:直接取值。<br>
     * field rule:直接赋值。
     */
    @Column("V12001")
    QCElement<Double> overheadWireIceTemperature;
    /**
     * NO: 2.13  <br>
     * nameCN: 电线积冰-风向 <br>
     * unit: 1℃<br>
     * descriptionCN:<br>
     * decode rule:直接取值。<br>
     * field rule:直接赋值。
     */
    @Column("V11001")
    QCElement<Double> overheadWireIceWindDirection;

    /**
     * NO: 2.13  <br>
     * nameCN: 电线积冰-风速 <br>
     * unit: 0.1ｍ/ｓ<br>
     * descriptionCN:<br>
     * decode rule:直接取值。<br>
     * field rule:直接赋值。
     */
    @Column("V11002")
    QCElement<Double> overheadWireIceWindSpeed;
    /**
     * NO: 3.1  <br>
     * nameCN: 天气现象 <br>
     * unit: <br>
     * descriptionCN:<br>
     * decode rule:直接取值。<br>
     * field rule:直接赋值。
     */
//    @Column("V20303")
//    QCElement<String> weather;//??????
     @Column("V20304")
     QCElement<String> weather;

	public String getStationNumberChina() {
		return stationNumberChina;
	}

	public void setStationNumberChina(String stationNumberChina) {
		this.stationNumberChina = stationNumberChina;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Date getObservationTime() {
		return observationTime;
	}

	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}

	public QCElement<Double> getPrecipitationBetween20And8() {
		return precipitationBetween20And8;
	}

	public void setPrecipitationBetween20And8(QCElement<Double> precipitationBetween20And8) {
		this.precipitationBetween20And8 = precipitationBetween20And8;
	}

	public QCElement<Double> getPrecipitationBetween8And20() {
		return precipitationBetween8And20;
	}

	public void setPrecipitationBetween8And20(QCElement<Double> precipitationBetween8And20) {
		this.precipitationBetween8And20 = precipitationBetween8And20;
	}

	public QCElement<Double> getEvaporation() {
		return evaporation;
	}

	public void setEvaporation(QCElement<Double> evaporation) {
		this.evaporation = evaporation;
	}

	public QCElement<String> getOverheadWireIce() {
		return overheadWireIce;
	}

	public void setOverheadWireIce(QCElement<String> overheadWireIce) {
		this.overheadWireIce = overheadWireIce;
	}

	public QCElement<Double> getDiameterOfOverheadWireIceNS() {
		return diameterOfOverheadWireIceNS;
	}

	public void setDiameterOfOverheadWireIceNS(QCElement<Double> diameterOfOverheadWireIceNS) {
		this.diameterOfOverheadWireIceNS = diameterOfOverheadWireIceNS;
	}

	public QCElement<Double> getThicknessOfOverheadWireIceNS() {
		return thicknessOfOverheadWireIceNS;
	}

	public void setThicknessOfOverheadWireIceNS(QCElement<Double> thicknessOfOverheadWireIceNS) {
		this.thicknessOfOverheadWireIceNS = thicknessOfOverheadWireIceNS;
	}

	public QCElement<Double> getWeightOfOverheadWireIceNS() {
		return weightOfOverheadWireIceNS;
	}

	public void setWeightOfOverheadWireIceNS(QCElement<Double> weightOfOverheadWireIceNS) {
		this.weightOfOverheadWireIceNS = weightOfOverheadWireIceNS;
	}

	public QCElement<Double> getDiameterOfOverheadWireIceEW() {
		return diameterOfOverheadWireIceEW;
	}

	public void setDiameterOfOverheadWireIceEW(QCElement<Double> diameterOfOverheadWireIceEW) {
		this.diameterOfOverheadWireIceEW = diameterOfOverheadWireIceEW;
	}

	public QCElement<Double> getThicknessOfOverheadWireIceEW() {
		return thicknessOfOverheadWireIceEW;
	}

	public void setThicknessOfOverheadWireIceEW(QCElement<Double> thicknessOfOverheadWireIceEW) {
		this.thicknessOfOverheadWireIceEW = thicknessOfOverheadWireIceEW;
	}

	public QCElement<Double> getWeightOfOverheadWireIceEW() {
		return weightOfOverheadWireIceEW;
	}

	public void setWeightOfOverheadWireIceEW(QCElement<Double> weightOfOverheadWireIceEW) {
		this.weightOfOverheadWireIceEW = weightOfOverheadWireIceEW;
	}

	public QCElement<Double> getOverheadWireIceTemperature() {
		return overheadWireIceTemperature;
	}

	public void setOverheadWireIceTemperature(QCElement<Double> overheadWireIceTemperature) {
		this.overheadWireIceTemperature = overheadWireIceTemperature;
	}

	public QCElement<Double> getOverheadWireIceWindDirection() {
		return overheadWireIceWindDirection;
	}

	public void setOverheadWireIceWindDirection(QCElement<Double> overheadWireIceWindDirection) {
		this.overheadWireIceWindDirection = overheadWireIceWindDirection;
	}

	public QCElement<Double> getOverheadWireIceWindSpeed() {
		return overheadWireIceWindSpeed;
	}

	public void setOverheadWireIceWindSpeed(QCElement<Double> overheadWireIceWindSpeed) {
		this.overheadWireIceWindSpeed = overheadWireIceWindSpeed;
	}
    
	 public String getCorrectionIndicator() {
			return correctionIndicator;
	}

	public void setCorrectionIndicator(String correctionIndicator) {
			this.correctionIndicator = correctionIndicator;
	}

	public QCElement<String> getWeather() {
		return weather;
	}

	public void setWeather(QCElement<String> weather) {
		this.weather = weather;
	}
	
}
