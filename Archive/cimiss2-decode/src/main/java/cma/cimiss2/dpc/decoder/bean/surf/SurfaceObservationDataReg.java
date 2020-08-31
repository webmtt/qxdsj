package cma.cimiss2.dpc.decoder.bean.surf;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import cma.cimiss2.dpc.decoder.annotation.Column;
import cma.cimiss2.dpc.decoder.bean.QCElement;

/**
 * *****************************************************************************************<br> 
 * <strong>All Rights Reserved By National Meteorological Information Centre (NMIC)</strong><br>
 * *****************************************************************************************<br>

 * 中国地面观测资料解码结果(区域站)
 * 
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
public class SurfaceObservationDataReg implements Serializable {

	private static final long serialVersionUID = 7010519447394907522L;
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
	 * descriptionCN: <br>
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
	 * descriptionCN:
	 * decode rule:进行纯数字校验.取前3位数字记为a1,中间两位数字除以60记为a2,最后两位数字除以3600记为a3,取值为a1+a2+a3.<br>
	 * field rule:直接赋值。
	 */
	@Column("V06001")
	Double longitude;
	/**
	 * NO: 1.4  <br>
	 * nameCN: 观测场拔海高度 <br>
	 * unit: 1m <br>
	 * BUFR FXY: V07001 <br>
	 * descriptionCN: 若低于海平面，为负值<br>
	 * decode rule:取值后除以10。<br>
	 * field rule:直接赋值。
	 */
	@Column("V07001")
	Double heightOfSationGroundAboveMeanSeaLevel;
	/**
	 * NO: 1.5  <br>
	 * nameCN: 气压传感器拔海高度 <br>
	 * unit: 1m <br>
	 * BUFR FXY: V07031 <br>
	 * descriptionCN: 无气压传感器时，值为null ，若低于海平面，为负值<br>
	 * decode rule:取值后除以10。<br>
	 * field rule:直接赋值。
	 */
	@Column("V07031")
	Double heightOfBarometerAboveMeanSeaLevel;
	/**
	 * NO: 1.6  <br>
	 * nameCN: 观测方式 <br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN:
	 * 当器测项目为人工观测时 MANUAL，器测项目为自动站观测时 EQUIPMENT<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 */
	String observationMethod;
	/**
	 * NO: 1.7  <br>
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
	 * decode rule:直接取值。<br>
	 * field rule:使用java时间转化工具类进行转化,标准形式为：yyyyMMddHHmmss
	 */
//	@Column(value = "V04001", pattern = "yyyy")
//	@Column(value = "V04002", pattern = "MM")
//	@Column(value = "V04003", pattern = "dd")
//	@Column(value = "V04004", pattern = "HH")
	@Column("D_DATETIME")
	Date observationTime;
	/**
	 * NO: 2.2  <br>
	 * nameCN: 2分钟风向 <br>
	 * unit: 1° <br>
	 * BUFR FXY: V11290 <br>
	 * descriptionCN: 当前时刻的2分钟平均风向<br>
	 * decode rule:优先特殊处理：000时取值为360，PPC时取值为999017。进行纯数字校验。<br>
	 * field rule:直接赋值。
	 */
	@Column("V11290")
	QCElement<Integer> windDirectionAt2m;
	/**
	 * NO: 2.3  <br>
	 * nameCN: 2分钟平均风速 <br>
	 * unit: 1m/s <br>
	 * BUFR FXY: V11291 <br>
	 * descriptionCN: 当前时刻的2分钟平均风速<br>
	 * decode rule:进行纯数字校验。取值后除以10。<br>
	 * field rule:直接赋值。
	 */
	@Column("V11291")
	QCElement<Double> windSpeedAvg2m;
	/**
	 * NO: 2.4  <br>
	 * nameCN: 10分钟风向 <br>
	 * unit: 1° <br>
	 * BUFR FXY: V11292 <br>
	 * descriptionCN: 当前时刻的10分钟平均风向<br>
	 * decode rule:优先特殊处理：000时取值为360，PPC时取值为999017。进行纯数字校验。<br>
	 * field rule:直接赋值。
	 */
	@Column("V11292")
	QCElement<Integer> windDirectionAt10;
	/**
	 * NO: 2.5  <br>
	 * nameCN: 10分钟平均风速 <br>
	 * unit: 1m/s <br>
	 * BUFR FXY: V11293 <br>
	 * descriptionCN: 当前时刻的10分钟平均风速<br>
	 * decode rule:进行纯数字校验。取值后除以10。<br>
	 * field rule:直接赋值。
	 */
	@Column("V11293")
	QCElement<Double> windSpeedAt10;
	/**
	 * NO: 2.6  <br>
	 * nameCN: 最大风速的风向 <br>
	 * unit: 1° <br>
	 * BUFR FXY: V11296 <br>
	 * descriptionCN: 每1小时内10分钟最大风速的风向<br>
	 * decode rule:优先特殊处理：000时取值为360，PPC时取值为999017。进行纯数字校验。<br>
	 * field rule:直接赋值。
	 */
	@Column("V11296")
	QCElement<Integer> windDirectionOfMaxSpeed;
	/**
	 * NO: 2.7  <br>
	 * nameCN: 最大风速 <br>
	 * unit: 1m/s <br>
	 * BUFR FXY: V11042 <br>
	 * descriptionCN: 每1小时内10分钟最大风速<br>
	 * decode rule:进行纯数字校验。取值后除以10。<br>
	 * field rule:直接赋值。
	 */
	@Column("V11042")
	QCElement<Double> windSpeedMax;
	/**
	 * NO: 2.8  <br>
	 * nameCN: 最大风速出现时间 <br>
	 * unit: <br>
	 * BUFR FXY: V11042_052 <br>
	 * descriptionCN: 每1小时内10分钟最大风速出现时间<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 */
	@Column("V11042_052")
	QCElement<Integer> windSpeedMaxOccurrenceTime;
	/**
	 * NO: 2.9  <br>
	 * nameCN: 瞬时风向 <br>
	 * unit: 1° <br>
	 * BUFR FXY: V11201 <br>
	 * descriptionCN: 当前时刻的瞬时风向<br>
	 * decode rule:优先特殊处理：000时取值为360，PPC时取值为999017。进行纯数字校验。<br>
	 * field rule:直接赋值。
	 */
	@Column("V11201")
	QCElement<Integer> windDirectionCurrent;
	/**
	 * NO: 2.10  <br>
	 * nameCN: 瞬时风速 <br>
	 * unit: 1m/s <br>
	 * BUFR FXY: V11202 <br>
	 * descriptionCN: 当前时刻的瞬时风速<br>
	 * decode rule:进行纯数字校验。取值后除以10。<br>
	 * field rule:直接赋值。
	 */
	@Column("V11202")
	QCElement<Double> windSpeedCurrent;
	/**
	 * NO: 2.11   <br>
	 * nameCN: 极大风速的风向 <br>
	 * unit: 1° <br>
	 * BUFR FXY: V11211 <br>
	 * descriptionCN: 每1小时内的极大风速的风向<br>
	 * decode rule:优先特殊处理：000时取值为360，PPC时取值为999017。进行纯数字校验。<br>
	 * field rule:直接赋值。
	 */
	@Column("V11211")
	QCElement<String> windDirectionWhenSpeedMax;
	/**
	 * NO: 2.12   <br>
	 * nameCN: 极大风速 <br>
	 * unit: 1m/s <br>
	 * BUFR FXY: V11046 <br>
	 * descriptionCN: 每1小时内的极大风速<br>
	 * decode rule:进行纯数字校验。取值后除以10。<br>
	 * field rule:直接赋值。
	 */
	@Column("V11046")
	QCElement<Double> extremeWindSpeed1Hour;
	/**
	 * NO: 2.13   <br>
	 * nameCN: 极大风速出现时间 <br>
	 * unit: <br>
	 * BUFR FXY: V11046_052 <br>
	 * descriptionCN: 每1小时内极大风速出现时间<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 */
	@Column("V11046_052")
	QCElement<Integer> windSpeedExtremeOccurrenceTime;
	/**
	 * NO: 2.14  <br>
	 * nameCN: 小时降水量 <br>
	 * unit: 1mm <br>
	 * BUFR FXY: V13019 <br>
	 * descriptionCN: 每1小时内的降水量累计量<br>
	 * decode rule:进行纯数字校验。取值后除以10。<br>
	 * field rule:直接赋值。
	 */
	@Column("V13019")
	QCElement<Double> precipitation1Hour;
	/**
	 * NO: 2.15  <br>
	 * nameCN: 气温 <br>
	 * unit: 1℃ <br>
	 * BUFR FXY: V12001 <br>
	 * descriptionCN: 当前时刻的空气温度<br>
	 * decode rule:进行纯数字校验。取值后除以10。<br>
	 * field rule:直接赋值。
	 */
	@Column("V12001")
	QCElement<Double> temperature;
	/**
	 * NO: 2.16  <br>
	 * nameCN: 最高气温 <br>
	 * unit: 1℃ <br>
	 * BUFR FXY: V12011 <br>
	 * descriptionCN: 每1小时内的最高气温<br>
	 * decode rule:进行纯数字校验。取值后除以10。<br>
	 * field rule:直接赋值。
	 */
	@Column("V12011")
	QCElement<Double> temperatureMax;
	/**
	 * NO: 2.17  <br>
	 * nameCN: 最高气温出现时间 <br>
	 * unit: <br>
	 * BUFR FXY: V12011_052 <br>
	 * descriptionCN: 每1小时内最高气温出现时间<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 */
	@Column("V12011_052")
	QCElement<Integer> temperatureMaxOccurrenceTime;
	/**
	 * NO: 2.18  <br>
	 * nameCN: 最低气温 <br>
	 * unit: 1℃ <br>
	 * BUFR FXY: V12012 <br>
	 * descriptionCN: 每1小时内的最低气温<br>
	 * decode rule:进行纯数字校验。取值后除以10。<br>
	 * field rule:直接赋值。
	 */
	@Column("V12012")
	QCElement<Double> temperatureMin;
	/**
	 * NO: 2.19  <br>
	 * nameCN: 最低气温出现时间 <br>
	 * unit: <br>
	 * BUFR FXY: V12012_052 <br>
	 * descriptionCN: 每1小时内最低气温出现时间<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 */
	@Column("V12012_052")
	QCElement<Integer> temperatureMinOccurrenceTime;
	/**
	 * NO: 2.20   <br>
	 * nameCN: 相对湿度 <br>
	 * unit: 1% <br>
	 * BUFR FXY: V13003 <br>
	 * descriptionCN: 当前时刻的相对湿度<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 */
	@Column("V13003")
	QCElement<Integer> relativeHumidity;
	/**
	 * NO: 2.21   <br>
	 * nameCN: 最小相对湿度 <br>
	 * unit: 1% <br>
	 * BUFR FXY: V13007 <br>
	 * descriptionCN: 每1小时内的最小相对湿度值<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 */
	@Column("V13007")
	QCElement<Integer> relativeHumidityMin;
	/**
	 * NO: 2.22   <br>
	 * nameCN: 最小相对湿度出现时间 <br>
	 * unit: <br>
	 * BUFR FXY: V13007_052 <br>
	 * descriptionCN: 每1小时内最小相对湿度出现时间<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 */
	@Column("V13007_052")
	QCElement<Integer> occurrenceTimeOfRelativeHumidityMin;
	/**
	 * NO: 2.23   <br>
	 * nameCN: 水汽压 <br>
	 * unit: 1hPa <br>
	 * BUFR FXY: V13004 <br>
	 * descriptionCN: 当前时刻的水汽压值<br>
	 * decode rule:进行纯数字校验。取值后除以10。<br>
	 * field rule:直接赋值。
	 */
	@Column("V13004")
	QCElement<Double> vapourPressur;
	/**
	 * NO: 2.24  <br>
	 * nameCN: 露点温度 <br>
	 * unit: 1℃ <br>
	 * BUFR FXY: V12003 <br>
	 * descriptionCN: 当前时刻的露点温度值<br>
	 * decode rule:进行纯数字校验。取值后除以10。<br>
	 * field rule:直接赋值。
	 */
	@Column("V12003")
	QCElement<Double> wetBulbTemperature;
	/**
	 * NO: 2.25  <br>
	 * nameCN: 本站气压 <br>
	 * unit: 1hPa <br>
	 * BUFR FXY: V10004 <br>
	 * descriptionCN: 当前时刻的本站气压值<br>
	 * decode rule:进行纯数字校验。取值后除以10。<br>
	 * field rule:直接赋值。
	 */
	@Column("V10004")
	QCElement<Double> stationPressure;
	/**
	 * NO: 2.26  <br>
	 * nameCN: 最高本站气压 <br>
	 * unit: 1hPa <br>
	 * BUFR FXY: V10301 <br>
	 * descriptionCN: 每1小时内的最高本站气压值<br>
	 * decode rule:进行纯数字校验。取值后除以10。<br>
	 * field rule:直接赋值。
	 */
	@Column("V10301")
	QCElement<Double> stationPressureMax;
	/**
	 * NO: 2.27  <br>
	 * nameCN: 最高本站气压出现时间 <br>
	 * unit: <br>
	 * BUFR FXY: V10301_052 <br>
	 * descriptionCN: 每1小时内最高本站气压出现时间<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 */
	@Column("V10301_052")
	QCElement<Integer> occurrenceTimeOfStationPressureMax;
	/**
	 * NO: 2.28  <br>
	 * nameCN: 最低本站气压 <br>
	 * unit: 1hPa <br>
	 * BUFR FXY: V10302 <br>
	 * descriptionCN: 每1小时内的最低本站气压值<br>
	 * decode rule:进行纯数字校验。取值后除以10。<br>
	 * field rule:直接赋值。
	 */
	@Column("V10302")
	QCElement<Double> stationPressureMin;
	/**
	 * NO: 2.29  <br>
	 * nameCN: 最低本站气压出现时间 <br>
	 * unit: <br>
	 * BUFR FXY: V10302_052 <br>
	 * descriptionCN: 每1小时内最低本站气压出现时间<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 */
	@Column("V10302_052")
	QCElement<Integer> occurrenceTimeOfStationPressureMin;
	/**
	 * NO: 2.30    <br>
	 * nameCN: 草面温度 <br>
	 * unit: 1℃ <br>
	 * BUFR FXY: V12314 <br>
	 * descriptionCN: 当前时刻的草面温度值<br>
	 * decode rule:进行纯数字校验。取值后除以10。<br>
	 * field rule:直接赋值。
	 */
	@Column("V12314")
	QCElement<Double> grassTemperature;
	/**
	 * NO: 2.31    <br>
	 * nameCN: 草面最高温度 <br>
	 * unit: 1℃ <br>
	 * BUFR FXY: V12315 <br>
	 * descriptionCN: 每1小时内的草面最高温度<br>
	 * decode rule:进行纯数字校验。取值后除以10。<br>
	 * field rule:直接赋值。
	 */
	@Column("V12315")
	QCElement<Double> grassTemperatureMax;
	/**
	 * NO: 2.32    <br>
	 * nameCN: 草面最高出现时间 <br>
	 * unit: <br>
	 * BUFR FXY: V12315_052 <br>
	 * descriptionCN: 每1小时内草面最高温度出现时间<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 */
	@Column("V12315_052")
	QCElement<Integer> grassTemperatureMaxOccurrenceTime;
	/**
	 * NO: 2.33    <br>
	 * nameCN: 草面最低温度 <br>
	 * unit: 1℃ <br>
	 * BUFR FXY: V12316 <br>
	 * descriptionCN: 每1小时内的草面最低温度<br>
	 * decode rule:进行纯数字校验。取值后除以10。<br>
	 * field rule:直接赋值。
	 */
	@Column("V12316")
	QCElement<Double> grassTemperatureMin;
	/**
	 * NO: 2.34    <br>
	 * nameCN: 草面最低出现时间 <br>
	 * unit: <br>
	 * BUFR FXY: V12316_052 <br>
	 * descriptionCN: 每1小时内草面最低温度出现时间<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 */
	@Column("V12316_052")
	QCElement<Integer> grassTemperatureMinOccurrenceTime;
	/**
	 * NO: 2.35  <br>
	 * nameCN: 地表温度 <br>
	 * unit: 1℃ <br>
	 * BUFR FXY: V12120 <br>
	 * descriptionCN: 当前时刻的地面温度值<br>
	 * decode rule:进行纯数字校验。取值后除以10。<br>
	 * field rule:直接赋值。
	 */
	@Column("V12120")
	QCElement<Double> landSurfaceTemperature;
	/**
	 * NO: 2.36  <br>
	 * nameCN: 地表最高温度 <br>
	 * unit: 1℃ <br>
	 * BUFR FXY: V12311 <br>
	 * descriptionCN: 每1小时内的地面最高温度<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 */
	@Column("V12311")
	QCElement<Double> landSurfaceTemperatureMax;
	/**
	 * NO: 2.37  <br>
	 * nameCN: 地表最高出现时间 <br>
	 * unit: <br>
	 * BUFR FXY: V12311_052 <br>
	 * descriptionCN: 每1小时内地面最高温度出现时间<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 */
	@Column("V12311_052")
	QCElement<Integer> landSurfaceTemperatureMaxOccurrenceTime;
	/**
	 * NO: 2.38  <br>
	 * nameCN: 地面表最低温度 <br>
	 * unit: 1℃ <br>
	 * BUFR FXY: V12121 <br>
	 * descriptionCN: 每1小时内的地面最低温度<br>
	 * decode rule:进行纯数字校验。取值后除以10。<br>
	 * field rule:直接赋值。
	 */
	@Column("V12121")
	QCElement<Double> landSurfaceTemperatureMin;
	/**
	 * NO: 2.39  <br>
	 * nameCN: 地表最低出现时间 <br>
	 * unit: <br>
	 * BUFR FXY: V12121_052 <br>
	 * descriptionCN: 每1小时内地面最低温度出现时间<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 */
	@Column("V12121_052")
	QCElement<Integer> landSurfaceTemperatureMinOccurrenceTime;
	/**
	 * NO: 2.40  <br>
	 * nameCN: 5厘米地温 <br>
	 * unit: 1℃ <br>
	 * BUFR FXY: V12030_005 <br>
	 * descriptionCN: 当前时刻的5厘米地温值<br>
	 * decode rule:进行纯数字校验。取值后除以10。<br>
	 * field rule:直接赋值。
	 */
	@Column("V12030_005")
	QCElement<Double> soilTemperature5CM;
	/**
	 * NO: 2.41  <br>
	 * nameCN: 10厘米地温 <br>
	 * unit: 1℃ <br>
	 * BUFR FXY: V12030_010 <br>
	 * descriptionCN: 当前时刻的10厘米地温值<br>
	 * decode rule:进行纯数字校验。取值后除以10。<br>
	 * field rule:直接赋值。
	 */
	@Column("V12030_010")
	QCElement<Double> soilTemperature10CM;
	/**
	 * NO: 2.42  <br>
	 * nameCN: 15厘米地温 <br>
	 * unit: 1℃ <br>
	 * BUFR FXY: V12030_015 <br>
	 * descriptionCN: 当前时刻的15厘米地温值<br>
	 * decode rule:进行纯数字校验。取值后除以10。<br>
	 * field rule:直接赋值。
	 */
	@Column("V12030_015")
	QCElement<Double> soilTemperature15CM;
	/**
	 * NO: 2.43    <br>
	 * nameCN: 20厘米地温 <br>
	 * unit: 1℃ <br>
	 * BUFR FXY: V12030_020 <br>
	 * descriptionCN: 当前时刻的20厘米地温值<br>
	 * decode rule:进行纯数字校验。取值后除以10。<br>
	 * field rule:直接赋值。
	 */
	@Column("V12030_020")
	QCElement<Double> soilTemperature20CM;
	/**
	 * NO: 2.44    <br>
	 * nameCN: 40厘米地温 <br>
	 * unit: 1℃ <br>
	 * BUFR FXY: V12030_040 <br>
	 * descriptionCN: 当前时刻的40厘米地温值<br>
	 * decode rule:进行纯数字校验。取值后除以10。<br>
	 * field rule:直接赋值。
	 */@Column("V12030_040")
	QCElement<Double> soilTemperature40CM;
	/**
	 * NO: 2.45    <br>
	 * nameCN: 80厘米地温 <br>
	 * unit: 1℃ <br>
	 * BUFR FXY: V12030_080 <br>
	 * descriptionCN: 当前时刻的80厘米地温值<br>
	 * decode rule:进行纯数字校验。取值后除以10。<br>
	 * field rule:直接赋值。
	 */@Column("V12030_080")
	QCElement<Double> soilTemperature80CM;
	/**
	 * NO: 2.46    <br>
	 * nameCN: 160厘米地温 <br>
	 * unit: 1℃ <br>
	 * BUFR FXY: V12030_160 <br>
	 * descriptionCN: 当前时刻的160厘米地温值<br>
	 * decode rule:进行纯数字校验。取值后除以10。<br>
	 * field rule:直接赋值。
	 */@Column("V12030_160")
	QCElement<Double> soilTemperature160CM;
	/**
	 * NO: 2.47    <br>
	 * nameCN: 320厘米地温 <br>
	 * unit: 1℃ <br>
	 * BUFR FXY: V12030_320 <br>
	 * descriptionCN: 当前时刻的320厘米地温值<br>
	 * decode rule:进行纯数字校验。取值后除以10。<br>
	 * field rule:直接赋值。
	 */@Column("V12030_320")
	QCElement<Double> soilTemperature320CM;
	/**
	 * NO: 2.48  <br>
	 * nameCN: 小时蒸发量 <br>
	 * unit: 1mm <br>
	 * BUFR FXY: V13033 <br>
	 * descriptionCN: 每1小时内的蒸发累计量<br>
	 * decode rule:进行纯数字校验。取值后除以10。<br>
	 * field rule:直接赋值。
	 */@Column("V13033")
	QCElement<Double> evaporation;
	/**
	 * NO: 2.49  <br>
	 * nameCN: 海平面气压 <br>
	 * unit: 1hPa <br>
	 * BUFR FXY: V10051 <br>
	 * descriptionCN: 当前时刻的海平面气压值<br>
	 * decode rule:进行纯数字校验。取值后除以10。<br>
	 * field rule:直接赋值。
	 */@Column("V10051")
	QCElement<Double> pressureReducedToMeanSeaLevel;
	/**
	 * NO: 2.50  <br>
	 * nameCN: 当前时刻的能见度 <br>
	 * unit: 1km <br>
	 * BUFR FXY:  <br>
	 * descriptionCN: 当前时刻的能见度<br>
	 * decode rule:进行纯数字校验。取值后除以10。<br>
	 * field rule:直接赋值。
	 */@Column("V20001_701_01")
	QCElement<Double> horizontalVisibilityCurrent;
	/**
	 * NO: 2.51  <br>
	 * nameCN: 最小能见度 <br>
	 * unit: 1m <br>
	 * BUFR FXY: V20059 <br>
	 * descriptionCN: 每1小时内的最小能见度<br>
	 * decode rule:进行纯数字校验。取值后除以10。<br>
	 * field rule:直接赋值。
	 */@Column("V20059")
	QCElement<Integer> horizontalVisibility1HourMin;
	/**
	 * NO: 2.52  <br>
	 * nameCN: 最小能见度出现时间 <br>
	 * unit: <br>
	 * BUFR FXY: V20059_052 <br>
	 * descriptionCN: 每1小时内的最小能见度出现时间<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 */@Column("V20059_052")
	QCElement<Integer> horizontalVisibility1HourMinOccurrenceTime;
	
    /**
     * NO: 3 <br>
     * nameCN: 小时内每分钟降水量数据 <br>
     * unit: 1mm <br>
     * BUFR FXY: <br>
     * descriptionCN:<br>>
     * decode rule:特殊处理:如果包含“，”，取值为0。<br>
     * field rule:直接赋值。
     */

    List<QCElement<Double>> precipitationEveryMinutes;
	
	/**
	 * NO: 3.1  <br>
	 * nameCN: 能见度 <br>
	 * unit: 1km <br>
	 * BUFR FXY: V20001 <br>
	 * descriptionCN: 正点的能见度，非正点为null<br>
	 * decode rule:进行纯数字校验。取值后除以10。<br>
	 * field rule:直接赋值。
	 */
	@Column("V20001")
	Double horizontalVisibilityHourly;
	/**
	 * NO: 3.2  <br>
	 * nameCN: 总云量 <br>
	 * unit: 1成 <br>
	 * BUFR FXY: V20010 <br>
	 * descriptionCN: 正点的总云量，非正点为null<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 */@Column("V20010")
	Double cloudAmount;
	/**
	 * NO: 3.3  <br>
	 * nameCN: 低云量 <br>
	 * unit: 1成 <br>
	 * BUFR FXY: V20051 <br>
	 * descriptionCN: 正点的低云量，非正点为null<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 */@Column("V20051")
	Double cloudAmountLow;
	/**
	 * NO: 3.4  <br>
	 * nameCN: 编报云量 <br>
	 * unit: 1成 <br>
	 * BUFR FXY: V20011 <br>
	 * descriptionCN: 正点的低云状或中云状云量，由人工输入，为编报Nh<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 */@Column("V20011")
	Double cloudAmountLowAndMiddle;
	/**
	 * NO: 3.5  <br>
	 * nameCN: 云高 <br>
	 * unit: 1m <br>
	 * BUFR FXY: V20013 <br>
	 * descriptionCN: 正点的低（中）云状云高，由人工输入，为编报iRiXhVV中的h；当无Nh的云时，若无云高值，均写入2500<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 */@Column("V20013")
	Integer cloudHeightLowAndMiddle;
	/**
	 * NO: 3.6  <br>
	 * nameCN: 云状 <br>
	 * unit: <br>
	 * BUFR FXY: V20350 <br>
	 * descriptionCN: 人工输入，最多8种云，按简码编<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 */
	String[] cloudType;
	/**
	 * NO: 3.7  <br>
	 * nameCN: 云状编码（云码） <br>
	 * unit: <br>
	 * BUFR FXY: V20350 <br>
	 * descriptionCN: 按《GD-01Ⅲ》规定形成的云状编码（CLCMCH），由人工输入云状，软件自动形成编码<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 */
	String cloudType1;
	/**
	 * NO: 3.8  <br>
	 * nameCN: 现在天气现象编码 <br>
	 * unit: <br>
	 * BUFR FXY: V20003 <br>
	 * descriptionCN: 按《GD-01Ⅲ》规定形成的现在天气现象编码(ww)，由人工输入，不能自动观测或人工输入时，固定编“//”<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 */@Column("V20003")
	String presentWeather;
	/**
	 * NO: 3.9  <br>
	 * nameCN: 6小时或12小时降水量组编码 <br>
	 * unit: 1mm <br>
	 * BUFR FXY: V13021 <br>
	 * descriptionCN: 18、0、6、12时（国际时，下同）定时天气报中，编报6RRR1或6RRR2组<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 *///@Column("V13021")
	String c39;
	/**
	 * NO: 3.10  <br>
	 * nameCN: 24小时变压变温组 <br>
	 * unit: 1mm <br>
	 * BUFR FXY: V13021 <br>
	 * descriptionCN: 18、0、6、12时（国际时，下同）定时天气报中，编报6RRR1或6RRR2组<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 *///@Column("V13021")
	String c310;
	/**
	 * NO: 3.11  <br>
	 * nameCN: 24小时降水量组编码 <br>
	 * unit: 1mm <br>
	 * BUFR FXY: V13021 <br>
	 * descriptionCN: 21、0时定时天气报中，编报7R24R24R24R24组<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 *///@Column("V13021")
	String c311;
	/**
	 * NO: 3.12  <br>
	 * nameCN: 过去24小时最高气温组 <br>
	 * unit: 1mm <br>
	 * BUFR FXY: V13021 <br>
	 * descriptionCN: 18、0时定时天气报中，编报1SnTxTxTx组<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 *///@Column("V13021")
	String c312;
	/**
	 * NO: 3.13  <br>
	 * nameCN: 过去24小时最低气温组 <br>
	 * unit: 1mm <br>
	 * BUFR FXY: V13021 <br>
	 * descriptionCN: 0、6时定时天气报中，编报1SnTnTnTn组<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 */
	//@Column("V13021")
	String c313;
	/**
	 * NO: 3.13  <br>
	 * nameCN: 过去12小时最低地面温度组 <br>
	 * unit: 1mm <br>
	 * BUFR FXY: V13021 <br>
	 * descriptionCN: 0时定时天气报中，编报1SnTgTgTg组<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 */
	//@Column("V13021")
	Double c314;
	/**
	 * NO: 3.15  <br>
	 * nameCN: 积雪深度 <br>
	 * unit: 1cm <br>
	 * BUFR FXY: V13013 <br>
	 * descriptionCN: 00时或应急加密观测时次观测值，由人工输入，00时为编报925SS组，无人工观测值时，固定编“////”<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 */@Column("V13013")
	Double SnowDepth;
	/**
	 * NO: 3.16  <br>
	 * nameCN: 雪压 <br>
	 * unit: 1g/cm2 <br>
	 * BUFR FXY: V13330 <br>
	 * descriptionCN: 00时或应急加密观测时次观测值，由人工输入，无人工观测值时值为null<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 */@Column("V13330")
	Double snowPressure;
	/**
	 * NO: 3.17  <br>
	 * nameCN: 冻土深度 <br>
	 * unit: 1cm <br>
	 * BUFR FXY: V20330_01 <br>
	 * descriptionCN: 0时最大下限值，由人工输入<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 */@Column("V20330_01")
	Integer frostDepth;
	/**
	 * NO: 3.18   <br>
	 * nameCN: 地面状态 <br>
	 * unit: <br>
	 * BUFR FXY: V20062 <br>
	 * descriptionCN: 06时人工观测值，由人工输入，其他时次固定编“//”<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 */@Column("V20062")
	Integer groundState;
	/**
	 * NO: 3.19   <br>
	 * nameCN: 重要天气极大风速 <br>
	 * unit: <br>
	 * BUFR FXY: V20062 <br>
	 * descriptionCN: 18、0、6、12时定时天气报中，编报的911fxfx组<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 *///@Column("V20062")
	String c319;
	/**
	 * NO: 3.20   <br>
	 * nameCN: 重要天气极大风速的风向 <br>
	 * unit: <br>
	 * BUFR FXY: V20062 <br>
	 * descriptionCN: 18、0、6、12时定时天气报中，编报的915dd组<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 *///@Column("V20062")
	String c320;
	/**
	 * NO: 3.21  <br>
	 * nameCN: 重要天气尘（龙）卷 <br>
	 * unit: <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 18、0、6、12时定时天气报中，编报的919MwDa组<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 */
	String tornado;
	/**
	 * NO: 3.22  <br>
	 * nameCN: 重要天气雨凇 <br>
	 * unit: 1mm <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 18、0、6、12时定时天气报中，编报的934RR组<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 */
	String diameterOfOverheadWireIce;
	/**
	 * NO: 3.23  <br>
	 * nameCN: 重要天气冰雹直径 <br>
	 * unit: 1mm <br>
	 * BUFR FXY: <br>
	 * descriptionCN: 18、0、6、12时定时天气报中，编报的939nn组<br>
	 * decode rule:直接取值。<br>
     * field rule:直接赋值。
	 */
	String hailDiameter;
	
	

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



	public Double getHeightOfSationGroundAboveMeanSeaLevel() {
		return heightOfSationGroundAboveMeanSeaLevel;
	}



	public void setHeightOfSationGroundAboveMeanSeaLevel(Double heightOfSationGroundAboveMeanSeaLevel) {
		this.heightOfSationGroundAboveMeanSeaLevel = heightOfSationGroundAboveMeanSeaLevel;
	}



	public Double getHeightOfBarometerAboveMeanSeaLevel() {
		return heightOfBarometerAboveMeanSeaLevel;
	}



	public void setHeightOfBarometerAboveMeanSeaLevel(Double heightOfBarometerAboveMeanSeaLevel) {
		this.heightOfBarometerAboveMeanSeaLevel = heightOfBarometerAboveMeanSeaLevel;
	}



	public String getObservationMethod() {
		return observationMethod;
	}



	public void setObservationMethod(String observationMethod) {
		this.observationMethod = observationMethod;
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



	public QCElement<Integer> getWindDirectionAt2m() {
		return windDirectionAt2m;
	}



	public void setWindDirectionAt2m(QCElement<Integer> windDirectionAt2m) {
		this.windDirectionAt2m = windDirectionAt2m;
	}



	public QCElement<Double> getWindSpeedAvg2m() {
		return windSpeedAvg2m;
	}



	public void setWindSpeedAvg2m(QCElement<Double> windSpeedAvg2m) {
		this.windSpeedAvg2m = windSpeedAvg2m;
	}



	public QCElement<Integer> getWindDirectionAt10() {
		return windDirectionAt10;
	}



	public void setWindDirectionAt10(QCElement<Integer> windDirectionAt10) {
		this.windDirectionAt10 = windDirectionAt10;
	}



	public QCElement<Double> getWindSpeedAt10() {
		return windSpeedAt10;
	}



	public void setWindSpeedAt10(QCElement<Double> windSpeedAt10) {
		this.windSpeedAt10 = windSpeedAt10;
	}



	public QCElement<Integer> getWindDirectionOfMaxSpeed() {
		return windDirectionOfMaxSpeed;
	}



	public void setWindDirectionOfMaxSpeed(QCElement<Integer> windDirectionOfMaxSpeed) {
		this.windDirectionOfMaxSpeed = windDirectionOfMaxSpeed;
	}



	public QCElement<Double> getWindSpeedMax() {
		return windSpeedMax;
	}



	public void setWindSpeedMax(QCElement<Double> windSpeedMax) {
		this.windSpeedMax = windSpeedMax;
	}



	public QCElement<Integer> getWindSpeedMaxOccurrenceTime() {
		return windSpeedMaxOccurrenceTime;
	}



	public void setWindSpeedMaxOccurrenceTime(QCElement<Integer> windSpeedMaxOccurrenceTime) {
		this.windSpeedMaxOccurrenceTime = windSpeedMaxOccurrenceTime;
	}



	public QCElement<Integer> getWindDirectionCurrent() {
		return windDirectionCurrent;
	}



	public void setWindDirectionCurrent(QCElement<Integer> windDirectionCurrent) {
		this.windDirectionCurrent = windDirectionCurrent;
	}



	public QCElement<Double> getWindSpeedCurrent() {
		return windSpeedCurrent;
	}



	public void setWindSpeedCurrent(QCElement<Double> windSpeedCurrent) {
		this.windSpeedCurrent = windSpeedCurrent;
	}



	public QCElement<String> getWindDirectionWhenSpeedMax() {
		return windDirectionWhenSpeedMax;
	}



	public void setWindDirectionWhenSpeedMax(QCElement<String> windDirectionWhenSpeedMax) {
		this.windDirectionWhenSpeedMax = windDirectionWhenSpeedMax;
	}



	public QCElement<Double> getExtremeWindSpeed1Hour() {
		return extremeWindSpeed1Hour;
	}



	public void setExtremeWindSpeed1Hour(QCElement<Double> extremeWindSpeed1Hour) {
		this.extremeWindSpeed1Hour = extremeWindSpeed1Hour;
	}



	public QCElement<Integer> getWindSpeedExtremeOccurrenceTime() {
		return windSpeedExtremeOccurrenceTime;
	}



	public void setWindSpeedExtremeOccurrenceTime(QCElement<Integer> windSpeedExtremeOccurrenceTime) {
		this.windSpeedExtremeOccurrenceTime = windSpeedExtremeOccurrenceTime;
	}



	public QCElement<Double> getPrecipitation1Hour() {
		return precipitation1Hour;
	}



	public void setPrecipitation1Hour(QCElement<Double> precipitation1Hour) {
		this.precipitation1Hour = precipitation1Hour;
	}



	public QCElement<Double> getTemperature() {
		return temperature;
	}



	public void setTemperature(QCElement<Double> temperature) {
		this.temperature = temperature;
	}



	public QCElement<Double> getTemperatureMax() {
		return temperatureMax;
	}



	public void setTemperatureMax(QCElement<Double> temperatureMax) {
		this.temperatureMax = temperatureMax;
	}



	public QCElement<Integer> getTemperatureMaxOccurrenceTime() {
		return temperatureMaxOccurrenceTime;
	}



	public void setTemperatureMaxOccurrenceTime(QCElement<Integer> temperatureMaxOccurrenceTime) {
		this.temperatureMaxOccurrenceTime = temperatureMaxOccurrenceTime;
	}



	public QCElement<Double> getTemperatureMin() {
		return temperatureMin;
	}



	public void setTemperatureMin(QCElement<Double> temperatureMin) {
		this.temperatureMin = temperatureMin;
	}



	public QCElement<Integer> getTemperatureMinOccurrenceTime() {
		return temperatureMinOccurrenceTime;
	}



	public void setTemperatureMinOccurrenceTime(QCElement<Integer> temperatureMinOccurrenceTime) {
		this.temperatureMinOccurrenceTime = temperatureMinOccurrenceTime;
	}



	public QCElement<Integer> getRelativeHumidity() {
		return relativeHumidity;
	}



	public void setRelativeHumidity(QCElement<Integer> relativeHumidity) {
		this.relativeHumidity = relativeHumidity;
	}



	public QCElement<Integer> getRelativeHumidityMin() {
		return relativeHumidityMin;
	}



	public void setRelativeHumidityMin(QCElement<Integer> relativeHumidityMin) {
		this.relativeHumidityMin = relativeHumidityMin;
	}



	public QCElement<Integer> getOccurrenceTimeOfRelativeHumidityMin() {
		return occurrenceTimeOfRelativeHumidityMin;
	}



	public void setOccurrenceTimeOfRelativeHumidityMin(QCElement<Integer> occurrenceTimeOfRelativeHumidityMin) {
		this.occurrenceTimeOfRelativeHumidityMin = occurrenceTimeOfRelativeHumidityMin;
	}



	public QCElement<Double> getVapourPressur() {
		return vapourPressur;
	}



	public void setVapourPressur(QCElement<Double> vapourPressur) {
		this.vapourPressur = vapourPressur;
	}



	public QCElement<Double> getWetBulbTemperature() {
		return wetBulbTemperature;
	}



	public void setWetBulbTemperature(QCElement<Double> wetBulbTemperature) {
		this.wetBulbTemperature = wetBulbTemperature;
	}



	public QCElement<Double> getStationPressure() {
		return stationPressure;
	}



	public void setStationPressure(QCElement<Double> stationPressure) {
		this.stationPressure = stationPressure;
	}



	public QCElement<Double> getStationPressureMax() {
		return stationPressureMax;
	}



	public void setStationPressureMax(QCElement<Double> stationPressureMax) {
		this.stationPressureMax = stationPressureMax;
	}



	public QCElement<Integer> getOccurrenceTimeOfStationPressureMax() {
		return occurrenceTimeOfStationPressureMax;
	}



	public void setOccurrenceTimeOfStationPressureMax(QCElement<Integer> occurrenceTimeOfStationPressureMax) {
		this.occurrenceTimeOfStationPressureMax = occurrenceTimeOfStationPressureMax;
	}



	public QCElement<Double> getStationPressureMin() {
		return stationPressureMin;
	}



	public void setStationPressureMin(QCElement<Double> stationPressureMin) {
		this.stationPressureMin = stationPressureMin;
	}



	public QCElement<Integer> getOccurrenceTimeOfStationPressureMin() {
		return occurrenceTimeOfStationPressureMin;
	}



	public void setOccurrenceTimeOfStationPressureMin(QCElement<Integer> occurrenceTimeOfStationPressureMin) {
		this.occurrenceTimeOfStationPressureMin = occurrenceTimeOfStationPressureMin;
	}



	public QCElement<Double> getGrassTemperature() {
		return grassTemperature;
	}



	public void setGrassTemperature(QCElement<Double> grassTemperature) {
		this.grassTemperature = grassTemperature;
	}



	public QCElement<Double> getGrassTemperatureMax() {
		return grassTemperatureMax;
	}



	public void setGrassTemperatureMax(QCElement<Double> grassTemperatureMax) {
		this.grassTemperatureMax = grassTemperatureMax;
	}



	public QCElement<Integer> getGrassTemperatureMaxOccurrenceTime() {
		return grassTemperatureMaxOccurrenceTime;
	}



	public void setGrassTemperatureMaxOccurrenceTime(QCElement<Integer> grassTemperatureMaxOccurrenceTime) {
		this.grassTemperatureMaxOccurrenceTime = grassTemperatureMaxOccurrenceTime;
	}



	public QCElement<Double> getGrassTemperatureMin() {
		return grassTemperatureMin;
	}



	public void setGrassTemperatureMin(QCElement<Double> grassTemperatureMin) {
		this.grassTemperatureMin = grassTemperatureMin;
	}



	public QCElement<Integer> getGrassTemperatureMinOccurrenceTime() {
		return grassTemperatureMinOccurrenceTime;
	}



	public void setGrassTemperatureMinOccurrenceTime(QCElement<Integer> grassTemperatureMinOccurrenceTime) {
		this.grassTemperatureMinOccurrenceTime = grassTemperatureMinOccurrenceTime;
	}



	public QCElement<Double> getLandSurfaceTemperature() {
		return landSurfaceTemperature;
	}



	public void setLandSurfaceTemperature(QCElement<Double> landSurfaceTemperature) {
		this.landSurfaceTemperature = landSurfaceTemperature;
	}



	public QCElement<Double> getLandSurfaceTemperatureMax() {
		return landSurfaceTemperatureMax;
	}



	public void setLandSurfaceTemperatureMax(QCElement<Double> landSurfaceTemperatureMax) {
		this.landSurfaceTemperatureMax = landSurfaceTemperatureMax;
	}



	public QCElement<Integer> getLandSurfaceTemperatureMaxOccurrenceTime() {
		return landSurfaceTemperatureMaxOccurrenceTime;
	}



	public void setLandSurfaceTemperatureMaxOccurrenceTime(QCElement<Integer> landSurfaceTemperatureMaxOccurrenceTime) {
		this.landSurfaceTemperatureMaxOccurrenceTime = landSurfaceTemperatureMaxOccurrenceTime;
	}



	public QCElement<Double> getLandSurfaceTemperatureMin() {
		return landSurfaceTemperatureMin;
	}



	public void setLandSurfaceTemperatureMin(QCElement<Double> landSurfaceTemperatureMin) {
		this.landSurfaceTemperatureMin = landSurfaceTemperatureMin;
	}



	public QCElement<Integer> getLandSurfaceTemperatureMinOccurrenceTime() {
		return landSurfaceTemperatureMinOccurrenceTime;
	}



	public void setLandSurfaceTemperatureMinOccurrenceTime(QCElement<Integer> landSurfaceTemperatureMinOccurrenceTime) {
		this.landSurfaceTemperatureMinOccurrenceTime = landSurfaceTemperatureMinOccurrenceTime;
	}



	public QCElement<Double> getSoilTemperature5CM() {
		return soilTemperature5CM;
	}



	public void setSoilTemperature5CM(QCElement<Double> soilTemperature5CM) {
		this.soilTemperature5CM = soilTemperature5CM;
	}



	public QCElement<Double> getSoilTemperature10CM() {
		return soilTemperature10CM;
	}



	public void setSoilTemperature10CM(QCElement<Double> soilTemperature10CM) {
		this.soilTemperature10CM = soilTemperature10CM;
	}



	public QCElement<Double> getSoilTemperature15CM() {
		return soilTemperature15CM;
	}



	public void setSoilTemperature15CM(QCElement<Double> soilTemperature15CM) {
		this.soilTemperature15CM = soilTemperature15CM;
	}



	public QCElement<Double> getSoilTemperature20CM() {
		return soilTemperature20CM;
	}



	public void setSoilTemperature20CM(QCElement<Double> soilTemperature20CM) {
		this.soilTemperature20CM = soilTemperature20CM;
	}



	public QCElement<Double> getSoilTemperature40CM() {
		return soilTemperature40CM;
	}



	public void setSoilTemperature40CM(QCElement<Double> soilTemperature40CM) {
		this.soilTemperature40CM = soilTemperature40CM;
	}



	public QCElement<Double> getSoilTemperature80CM() {
		return soilTemperature80CM;
	}



	public void setSoilTemperature80CM(QCElement<Double> soilTemperature80CM) {
		this.soilTemperature80CM = soilTemperature80CM;
	}



	public QCElement<Double> getSoilTemperature160CM() {
		return soilTemperature160CM;
	}



	public void setSoilTemperature160CM(QCElement<Double> soilTemperature160CM) {
		this.soilTemperature160CM = soilTemperature160CM;
	}



	public QCElement<Double> getSoilTemperature320CM() {
		return soilTemperature320CM;
	}



	public void setSoilTemperature320CM(QCElement<Double> soilTemperature320CM) {
		this.soilTemperature320CM = soilTemperature320CM;
	}



	public QCElement<Double> getEvaporation() {
		return evaporation;
	}



	public void setEvaporation(QCElement<Double> evaporation) {
		this.evaporation = evaporation;
	}



	public QCElement<Double> getPressureReducedToMeanSeaLevel() {
		return pressureReducedToMeanSeaLevel;
	}



	public void setPressureReducedToMeanSeaLevel(QCElement<Double> pressureReducedToMeanSeaLevel) {
		this.pressureReducedToMeanSeaLevel = pressureReducedToMeanSeaLevel;
	}



	public QCElement<Double> getHorizontalVisibilityCurrent() {
		return horizontalVisibilityCurrent;
	}



	public void setHorizontalVisibilityCurrent(QCElement<Double> horizontalVisibilityCurrent) {
		this.horizontalVisibilityCurrent = horizontalVisibilityCurrent;
	}



	public QCElement<Integer> getHorizontalVisibility1HourMin() {
		return horizontalVisibility1HourMin;
	}



	public void setHorizontalVisibility1HourMin(QCElement<Integer> horizontalVisibility1HourMin) {
		this.horizontalVisibility1HourMin = horizontalVisibility1HourMin;
	}



	public QCElement<Integer> getHorizontalVisibility1HourMinOccurrenceTime() {
		return horizontalVisibility1HourMinOccurrenceTime;
	}



	public void setHorizontalVisibility1HourMinOccurrenceTime(
			QCElement<Integer> horizontalVisibility1HourMinOccurrenceTime) {
		this.horizontalVisibility1HourMinOccurrenceTime = horizontalVisibility1HourMinOccurrenceTime;
	}



	public List<QCElement<Double>> getPrecipitationEveryMinutes() {
		return precipitationEveryMinutes;
	}



	public void setPrecipitationEveryMinutes(List<QCElement<Double>> precipitationEveryMinutes) {
		this.precipitationEveryMinutes = precipitationEveryMinutes;
	}



	public Double getHorizontalVisibilityHourly() {
		return horizontalVisibilityHourly;
	}



	public void setHorizontalVisibilityHourly(Double horizontalVisibilityHourly) {
		this.horizontalVisibilityHourly = horizontalVisibilityHourly;
	}



	public Double getCloudAmount() {
		return cloudAmount;
	}



	public void setCloudAmount(Double cloudAmount) {
		this.cloudAmount = cloudAmount;
	}



	public Double getCloudAmountLow() {
		return cloudAmountLow;
	}



	public void setCloudAmountLow(Double cloudAmountLow) {
		this.cloudAmountLow = cloudAmountLow;
	}



	public Double getCloudAmountLowAndMiddle() {
		return cloudAmountLowAndMiddle;
	}



	public void setCloudAmountLowAndMiddle(Double cloudAmountLowAndMiddle) {
		this.cloudAmountLowAndMiddle = cloudAmountLowAndMiddle;
	}



	public Integer getCloudHeightLowAndMiddle() {
		return cloudHeightLowAndMiddle;
	}



	public void setCloudHeightLowAndMiddle(Integer cloudHeightLowAndMiddle) {
		this.cloudHeightLowAndMiddle = cloudHeightLowAndMiddle;
	}



	public String[] getCloudType() {
		return cloudType;
	}



	public void setCloudType(String[] cloudType) {
		this.cloudType = cloudType;
	}



	public String getCloudType1() {
		return cloudType1;
	}



	public void setCloudType1(String cloudType1) {
		this.cloudType1 = cloudType1;
	}



	public String getPresentWeather() {
		return presentWeather;
	}



	public void setPresentWeather(String presentWeather) {
		this.presentWeather = presentWeather;
	}



	public String getC39() {
		return c39;
	}



	public void setC39(String c39) {
		this.c39 = c39;
	}



	public String getC310() {
		return c310;
	}



	public void setC310(String c310) {
		this.c310 = c310;
	}



	public String getC311() {
		return c311;
	}



	public void setC311(String c311) {
		this.c311 = c311;
	}



	public String getC312() {
		return c312;
	}



	public void setC312(String c312) {
		this.c312 = c312;
	}



	public String getC313() {
		return c313;
	}



	public void setC313(String c313) {
		this.c313 = c313;
	}



	public Double getC314() {
		return c314;
	}



	public void setC314(Double c314) {
		this.c314 = c314;
	}



	public Double getSnowDepth() {
		return SnowDepth;
	}



	public void setSnowDepth(Double snowDepth) {
		SnowDepth = snowDepth;
	}



	public Double getSnowPressure() {
		return snowPressure;
	}



	public void setSnowPressure(Double snowPressure) {
		this.snowPressure = snowPressure;
	}



	public Integer getFrostDepth() {
		return frostDepth;
	}



	public void setFrostDepth(Integer frostDepth) {
		this.frostDepth = frostDepth;
	}



	public Integer getGroundState() {
		return groundState;
	}



	public void setGroundState(Integer groundState) {
		this.groundState = groundState;
	}



	public String getC319() {
		return c319;
	}



	public void setC319(String c319) {
		this.c319 = c319;
	}



	public String getC320() {
		return c320;
	}



	public void setC320(String c320) {
		this.c320 = c320;
	}



	public String getTornado() {
		return tornado;
	}



	public void setTornado(String tornado) {
		this.tornado = tornado;
	}



	public String getDiameterOfOverheadWireIce() {
		return diameterOfOverheadWireIce;
	}



	public void setDiameterOfOverheadWireIce(String diameterOfOverheadWireIce) {
		this.diameterOfOverheadWireIce = diameterOfOverheadWireIce;
	}



	public String getHailDiameter() {
		return hailDiameter;
	}



	public void setHailDiameter(String hailDiameter) {
		this.hailDiameter = hailDiameter;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	public int getCloudType1Detail(int i) {
    	char c = this.cloudType1.charAt(i);
    	switch(i) {
    	case 0: 
    		if(c=='/') {
    			return 999999;
    		}else {
    			int temp = Integer.parseInt(String.valueOf(c));
    			return (temp+30);
    		}
    	case 1: 
    		if(c=='/') {
    			return 999999;
    		}else {
    			int temp = Integer.parseInt(String.valueOf(c));
    			return (temp+20);
    		}
    	case 2: 
    		if(c=='/') {
    			return 999999;
    		}else {
    			int temp = Integer.parseInt(String.valueOf(c));
    			return (temp+10);
    		}
    	default:
    		return -1;
    	}
	}


	
}
