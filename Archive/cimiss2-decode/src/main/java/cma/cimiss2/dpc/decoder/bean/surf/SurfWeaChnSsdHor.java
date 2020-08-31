package cma.cimiss2.dpc.decoder.bean.surf;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

import cma.cimiss2.dpc.decoder.annotation.Column;
import cma.cimiss2.dpc.decoder.bean.QCElement;

/**
 * *****************************************************************************************<br> 
 * <strong>All Rights Reserved By National Meteorological Information Centre (NMIC)</strong><br>
 * *****************************************************************************************<br>

 * 质控后日照日数据文件格式
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
public class SurfWeaChnSsdHor {

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
     * decode rule:进行纯数字校验.取前两位数字记为a1,中间两位数字除以60记为a2,最后两位数字除以3600记为a3,取值为a1+a2+a3.<br>
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
     * decode rule:进行纯数字校验.取前两位数字记为a1,中间两位数字除以60记为a2,最后两位数字除以3600记为a3,取值为a1+a2+a3.<br>
     * field rule:直接赋值。
     */
    @Column("V06001")
    Double longitude;
    /**
     * NO: 1.4  <br>
     * nameCN: 日照时制方式 <br>
     * unit: 1m <br>
     * BUFR FXY: V14332 <br>
     * descriptionCN: 1：为真太阳时，由人工观测仪器测得；4：为地方时，由自动观测仪器测得<br>
     * decode rule:直接取值。<br>
     * field rule:直接赋值。
     */
    @Column("V14332")
    Integer timeSystem;
    /**
     * NO: 1.5  <br>
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
     * BUFR FXY: V04001/V04002/V04003/V04004 <br>
     * descriptionCN:<br>
     * field rule:使用java时间转化工具类进行转化,标准形式为：yyyyMMddHHmmss<br>
     * decode rule:直接取值。<br>
     * field rule:直接赋值。
     */

//    @Column(value = "V04001", pattern = "yyyy")
//    @Column(value = "V04002", pattern = "MM")
//    @Column(value = "V04003", pattern = "dd")
    @Column("D_DATETIME")
    Date observationTime;

    /**
     * NO: 2.2-2.25 <br>
     * nameCN: 时日照时数 <br>
     * unit:  hour<br>
     * BUFR FXY: V14032_001 ... V14032_024<br>
     * descriptionCN:<br>
     * decode rule:取值后除以10。<br>
     * field rule:直接赋值。
     */
    List<QCElement<Double>> totalSunshineveryHour;

    /**
     * NO: 2.6 <br>
     * nameCN: 日照时数 <br>
     * unit:  hour<br>
     * BUFR FXY: V14032<br>
     * descriptionCN:<br>
     * decode rule:取值后除以10。<br>
     * field rule:直接赋值。
     */
    @Column("V14032")
    QCElement<Double> totalSunshinDay;

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

	public Integer getTimeSystem() {
		return timeSystem;
	}

	public void setTimeSystem(Integer timeSystem) {
		this.timeSystem = timeSystem;
	}

	public String getCorrectionIndicator() {
		return correctionIndicator;
	}

	public void setCorrectionIndicator(String correctionIndicator) {
		this.correctionIndicator = correctionIndicator;
	}

	public Date getObservationTime() {
		return observationTime;
	}

	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}

	public List<QCElement<Double>> getTotalSunshineveryHour() {
		return totalSunshineveryHour;
	}

	public void setTotalSunshineveryHour(List<QCElement<Double>> totalSunshineveryHour) {
		this.totalSunshineveryHour = totalSunshineveryHour;
	}

	public QCElement<Double> getTotalSunshinDay() {
		return totalSunshinDay;
	}

	public void setTotalSunshinDay(QCElement<Double> totalSunshinDay) {
		this.totalSunshinDay = totalSunshinDay;
	}
    
    


}
