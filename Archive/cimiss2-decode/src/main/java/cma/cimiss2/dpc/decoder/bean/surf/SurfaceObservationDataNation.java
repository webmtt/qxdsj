package cma.cimiss2.dpc.decoder.bean.surf;


import lombok.Getter;
import lombok.Setter;
import cma.cimiss2.dpc.decoder.annotation.Column;
import cma.cimiss2.dpc.decoder.bean.QCElement;
import cma.cimiss2.dpc.decoder.tools.enumeration.QualityControl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * *****************************************************************************************<br> 
 * <strong>All Rights Reserved By National Meteorological Information Centre (NMIC)</strong><br>
 * *****************************************************************************************<br>

 * 中国地面观测资料解码结果(国家站)
 * <p>
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
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 11/18/2017   shevawen    Initial creation.
 * </pre>
 *
 * @author shevawen
 * @version 0.0.1
 */
@Getter
@Setter
public class SurfaceObservationDataNation implements Serializable{

    private static final long serialVersionUID = -4330105235937647136L;
    /**
     * NO: 1.1  <br>
     * nameCN: 区站号 <br>
     * unit: <br>
     * BUFR FXY: V01301 <br>
     * descriptionCN: 5位数字或第1位为字母，第2-5位为数字
     * decode rule:直接取值。<br>
     * field rule:直接赋值。
     */
    @Column("V01301")
    String stationNumberChina;
    /**
     * NO: 1.2  <br>
     * nameCN: 观测时间 <br>
     * unit: <br>
     * BUFR FXY: V04001/V04002/V04003/V04004 <br>
     * descriptionCN:<br>
     * decode rule:直接取值。<br>
     * field rule:使用java时间转化工具类进行转化,标准形式为：yyyyMMddHHmmss
     */
//    @Column(value = "V04001", pattern = "yyyy")
//    @Column(value = "V04002", pattern = "MM")
//    @Column(value = "V04003", pattern = "dd")
//    @Column(value = "V04004", pattern = "HH")
    @Column("D_DATETIME")
    Date observationTime;
    /**
     * NO: 1.3  <br>
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
     * NO: 1.4  <br>
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
     * NO: 1.5  <br>
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
     * NO: 1.6  <br>
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
     * NO: 1.7  <br>
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
     * NO: 1.8  <br>
     * nameCN: 质量控制标识 <br>
     * unit: <br>
     * BUFR FXY: <br>
     * descriptionCN:
     * 依次标识台站级、省级、国家级对观测数据进行质量控制的情况。“1”为软件自动作过质量控制，“0”为由人机交互进一步作过质量控制，“9”为没有进行任何质量控制<br>
     * decode rule:直接取值。<br>
     * field rule:逐位进行枚举匹配，最后生成一个长度为3的枚举数组。
     */

    QualityControl[] qualityControl;
    /**
     * NO: 1.9  <br>
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
     * NO: 2.2  <br>
     * nameCN: 海平面气压 <br>
     * unit: 1hPa <br>
     * BUFR FXY: V10051 <br>
     * descriptionCN: 当前时刻的海平面气压值<br>
     * decode rule:进行纯数字校验。取值后除以10。<br>
     * field rule:直接赋值。
     */
    @Column("V10051")
    QCElement<Double> pressureReducedToMeanSeaLevel;
    /**
     * NO: 2.3  <br>
     * nameCN: 3小时变压 <br>
     * unit: 1hPa <br>
     * BUFR FXY: V10061 <br>
     * descriptionCN: 正点本站气压与前3小时本站气压之差，非正点时值为null<br>
     * decode rule:进行纯数字校验。1000减去数值记为a1，最后取值为a1除以10<br>
     * field rule:直接赋值。
     */
    @Column("V10061")
    QCElement<Double> pressureChange3Hours;
    /**
     * NO: 2.4  <br>
     * nameCN: 24小时变压 <br>
     * unit: 1hPa <br>
     * BUFR FXY: V10062 <br>
     * descriptionCN: 正点本站气压与前24小时本站气压之差，非正点时值为null<br>
     * decode rule:进行纯数字校验。1000减去数值记为a1，最后取值为a1除以10<br>
     * field rule:直接赋值。
     */
    @Column("V10062")
    QCElement<Double> pressureChange24Hours;
    /**
     * NO: 2.5  <br>
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
     * NO: 2.6  <br>
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
     * NO: 2.7  <br>
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
     * NO: 2.8  <br>
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
     * NO: 3.1  <br>
     * nameCN: 气温 <br>
     * unit: 1℃ <br>
     * BUFR FXY: V12001 <br>
     * descriptionCN: 当前时刻的空气温度<br>
     * decode rule:进行纯数字校验。1000减去数值记为a1，最后取值为a1除以10。<br>
     * field rule:直接赋值。
     */
    @Column("V12001")
    QCElement<Double> temperature;
    /**
     * NO: 3.2  <br>
     * nameCN: 最高气温 <br>
     * unit: 1℃ <br>
     * BUFR FXY: V12011 <br>
     * descriptionCN: 每1小时内的最高气温<br>
     * decode rule:进行纯数字校验。1000减去数值记为a1，最后取值为a1除以10。<br>
     * field rule:直接赋值。
     */
    @Column("V12011")
    QCElement<Double> temperatureMax;
    /**
     * NO: 3.3  <br>
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
     * NO: 3.4  <br>
     * nameCN: 最低气温 <br>
     * unit: 1℃ <br>
     * BUFR FXY: V12012 <br>
     * descriptionCN: 每1小时内的最低气温<br>
     * decode rule:进行纯数字校验。1000减去数值记为a1，最后取值为a1除以10。<br>
     * field rule:直接赋值。
     */
    @Column("V12012")
    QCElement<Double> temperatureMin;
    /**
     * NO: 3.5  <br>
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
     * NO: 3.6  <br>
     * nameCN: 24小时变温 <br>
     * unit: 1℃ <br>
     * BUFR FXY: V12405 <br>
     * descriptionCN: 正点气温与前24小时气温之差，在业务软件中自动计算求得，非正点时值为null<br>
     * decode rule:进行纯数字校验。1000减去数值记为a1，最后取值为a1除以10<br>
     * field rule:直接赋值。
     */
    @Column("V12405")
    QCElement<Double> temperatureChange24Hours;
    /**
     * NO: 3.7  <br>
     * nameCN: 过去24小时最高气温 <br>
     * unit: 1℃ <br>
     * BUFR FXY: V12016 <br>
     * descriptionCN: 软件自动统计求得，在18、00时，为编报1SnTxTxTx组，非正点时记为缺测<br>
     * decode rule:进行纯数字校验。1000减去数值记为a1，最后取值为a1除以10<br>
     * field rule:直接赋值。
     */
    @Column("V12016")
    QCElement<Double> temperatureMax24Hours;
    /**
     * NO: 3.8  <br>
     * nameCN: 过去24小时最低气温 <br>
     * unit: 1℃ <br>
     * BUFR FXY: V12017 <br>
     * descriptionCN: 软件自动统计求得，00、06时，为编报2SnTnTnTn组，非正点时记为缺测<br>
     * decode rule:进行纯数字校验。1000减去数值记为a1，最后取值为a1除以10<br>
     * field rule:直接赋值。
     */
    @Column("V12017")
    QCElement<Double> temperatureMin24Hours;
    /**
     * NO: 3.9  <br>
     * nameCN: 露点温度 <br>
     * unit: 1℃ <br>
     * BUFR FXY: V12003 <br>
     * descriptionCN: 当前时刻的露点温度值<br>
     * decode rule:进行纯数字校验。1000减去数值记为a1，最后取值为a1除以10<br>
     * field rule:直接赋值。
     */
    @Column("V12003")
    QCElement<Double> wetBulbTemperature;
    /**
     * NO: 3.10   <br>
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
     * NO: 3.11   <br>
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
     * NO: 3.12   <br>
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
     * NO: 3.13   <br>
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
     * NO: 4.1  <br>
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
     * NO: 4.2  <br>
     * nameCN: 过去3小时降水量 <br>
     * unit: 1mm <br>
     * BUFR FXY: V13020 <br>
     * descriptionCN: 软件从小时降水量自动统计，自动站缺测时，为雨量筒人工观测降水量。非正点时值为null<br>
     * decode rule:进行纯数字校验。取值后除以10。<br>
     * field rule:直接赋值。
     */
    @Column("V13020")
    QCElement<Double> precipitation3Hours;
    /**
     * NO: 4.3  <br>
     * nameCN: 过去6小时降水量 <br>
     * unit: 1mm <br>
     * BUFR FXY: V13021 <br>
     * descriptionCN: 软件从小时降水量自动统计，自动站缺测时，为雨量筒人工观测降水量。非正点时值为null<br>
     * decode rule:进行纯数字校验。取值后除以10。<br>
     * field rule:直接赋值。
     */
    @Column("V13021")
    QCElement<Double> precipitation6Hours;
    /**
     * NO: 4.4  <br>
     * nameCN: 过去12小时降水量 <br>
     * unit: 1mm <br>
     * BUFR FXY: V13022 <br>
     * descriptionCN: 软件从小时降水量自动统计，自动站缺测时，为雨量筒人工观测降水量，非正点时值为null<br>
     * decode rule:进行纯数字校验。取值后除以10。<br>
     * field rule:直接赋值。
     */
    @Column("V13022")
    QCElement<Double> precipitation12Hours;
    /**
     * NO: 4.5  <br>
     * nameCN: 24小时降水量 <br>
     * unit: 1mm <br>
     * BUFR FXY: V13023 <br>
     * descriptionCN: 软件从小时降水量自动统计，自动站缺测时，为雨量筒人工观测降水量，非正点时值为null<br>
     * decode rule:进行纯数字校验。取值后除以10。<br>
     * field rule:直接赋值。
     */
    @Column("V13023")
    QCElement<Double> precipitation24Hours;
    /**
     * NO: 4.6  <br>
     * nameCN: 人工加密观测降水量描述时间周期 <br>
     * unit: 1hour <br>
     * BUFR FXY: V04080_04 <br>
     * descriptionCN: 任意时段累积降水量，人工设置，满足应急加密观测需要。无加密观测降水量时值为null<br>
     * decode rule:进行纯数字校验。取值后除以10。<br>
     * field rule:直接赋值。
     */
    @Column("V04080_04")
    QCElement<Integer> intervalOfAdditionalManualObservationsOfPrecipitation;
    /**
     * NO: 4.7  <br>
     * nameCN: 人工加密观测降水量 <br>
     * unit: 1mm <br>
     * BUFR FXY: V13011 <br>
     * descriptionCN: 在4.6中指定累积时段的降水量。无此内容时，记为缺测<br>
     * decode rule:进行纯数字校验。取值后除以10。<br>
     * field rule:直接赋值。
     */
    @Column("V13011")
    QCElement<Double> precipitationAdditionalManualObservational;
    /**
     * NO: 4.8  <br>
     * nameCN: 小时蒸发量 <br>
     * unit: 1mm <br>
     * BUFR FXY: V13033 <br>
     * descriptionCN: 每1小时内的蒸发累计量<br>
     * decode rule:进行纯数字校验。取值后除以10。<br>
     * field rule:直接赋值。
     */
    @Column("V13033")
    QCElement<Double> evaporation;
    /**
     * NO: 5.1  <br>
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
     * NO: 5.2  <br>
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
     * NO: 5.3  <br>
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
     * NO: 5.4  <br>
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
     * NO: 5.5  <br>
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
     * NO: 5.6  <br>
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
     * NO: 5.7  <br>
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
     * NO: 5.8  <br>
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
     * NO: 5.9  <br>
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
     * NO: 5.10   <br>
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
     * NO: 5.11   <br>
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
     * NO: 5.12   <br>
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
     * NO: 5.13   <br>
     * nameCN: 过去6小时极大风速 <br>
     * unit: 1m/s <br>
     * BUFR FXY: V11504_06 <br>
     * descriptionCN:<br>
     * decode rule:进行纯数字校验。取值后除以10。<br>
     * field rule:直接赋值。
     */
    @Column("V11504_06")
    QCElement<Double> extremeWindSpeed6Hours;
    /**
     * NO: 5.14   <br>
     * nameCN: 过去6小时极大风向 <br>
     * unit: 1° <br>
     * BUFR FXY: V11503_06 <br>
     * descriptionCN:<br>
     * decode rule:优先特殊处理：000时取值为360，PPC时取值为999017。进行纯数字校验。<br>
     * field rule:直接赋值。
     */
    @Column("V11503_06")
    QCElement<Double> extremeWindDirection6Hours;
    /**
     * NO: 5.15   <br>
     * nameCN: 过去12小时极大风速 <br>
     * unit: 1m/s <br>
     * BUFR FXY: V11504_12 <br>
     * descriptionCN: 由软件自动从自动站数据中挑取，非正点时值为null<br>
     * decode rule:进行纯数字校验。取值后除以10。<br>
     * field rule:直接赋值。
     */
    @Column("V11504_12")
    QCElement<Double> extremeWindSpeed12Hours;
    /**
     * NO: 5.16   <br>
     * nameCN: 过去12小时极大风向 <br>
     * unit: 1° <br>
     * BUFR FXY: V11503_12 <br>
     * descriptionCN: 由软件自动从自动站数据中挑取，非正点时值为null<br>
     * decode rule:优先特殊处理：000时取值为360，PPC时取值为999017。进行纯数字校验。<br>
     * field rule:直接赋值。
     */
    @Column("V11503_12")
    QCElement<Double> extremeWindDirection12Hours;
    /**
     * NO: 6.1  <br>
     * nameCN: 地表温度 <br>
     * unit: 1℃ <br>
     * BUFR FXY: V12120 <br>
     * descriptionCN: 当前时刻的地面温度值<br>
     * decode rule:进行纯数字校验。1000减去数值记为a1，最后取值为a1除以10<br>
     * field rule:直接赋值。
     */
    @Column("V12120")
    QCElement<Double> landSurfaceTemperature;
    /**
     * NO: 6.2  <br>
     * nameCN: 地表最高温度 <br>
     * unit: 1℃ <br>
     * BUFR FXY: V12311 <br>
     * descriptionCN: 每1小时内的地面最高温度<br>
     * decode rule:进行纯数字校验。1000减去数值记为a1，最后取值为a1除以10<br>
     * field rule:直接赋值。
     */
    @Column("V12311")
    QCElement<Double> landSurfaceTemperatureMax;
    /**
     * NO: 6.3  <br>
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
     * NO: 6.4  <br>
     * nameCN: 地面表最低温度 <br>
     * unit: 1℃ <br>
     * BUFR FXY: V12121 <br>
     * descriptionCN: 每1小时内的地面最低温度<br>
     * decode rule:进行纯数字校验。1000减去数值记为a1，最后取值为a1除以10<br>
     * field rule:直接赋值。
     */
    @Column("V12121")
    QCElement<Double> landSurfaceTemperatureMin;
    /**
     * NO: 6.5  <br>
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
     * NO: 6.6  <br>
     * nameCN: 过去12小时最低地面温度 <br>
     * unit: 1℃ <br>
     * BUFR FXY: V12013 <br>
     * descriptionCN: 在业务软件中自动计算求得，00时为编报3SnTgTgTg组，非正点时记为缺测<br>
     * decode rule:进行纯数字校验。1000减去数值记为a1，最后取值为a1除以10<br>
     * field rule:直接赋值。
     */
    @Column("V12013")
    QCElement<Double> landSurfaceTemperatureMin12Hours;
    /**
     * NO: 6.7  <br>
     * nameCN: 5厘米地温 <br>
     * unit: 1℃ <br>
     * BUFR FXY: V12030_005 <br>
     * descriptionCN: 当前时刻的5厘米地温值<br>
     * decode rule:进行纯数字校验。1000减去数值记为a1，最后取值为a1除以10<br>
     * field rule:直接赋值。
     */
    @Column("V12030_005")
    QCElement<Double> soilTemperature5CM;
    /**
     * NO: 6.8  <br>
     * nameCN: 10厘米地温 <br>
     * unit: 1℃ <br>
     * BUFR FXY: V12030_010 <br>
     * descriptionCN: 当前时刻的10厘米地温值<br>
     * decode rule:进行纯数字校验。1000减去数值记为a1，最后取值为a1除以10<br>
     * field rule:直接赋值。
     */
    @Column("V12030_010")
    QCElement<Double> soilTemperature10CM;
    /**
     * NO: 6.9  <br>
     * nameCN: 15厘米地温 <br>
     * unit: 1℃ <br>
     * BUFR FXY: V12030_015 <br>
     * descriptionCN: 当前时刻的15厘米地温值<br>
     * decode rule:进行纯数字校验。1000减去数值记为a1，最后取值为a1除以10<br>
     * field rule:直接赋值。
     */
    @Column("V12030_015")
    QCElement<Double> soilTemperature15CM;
    /**
     * NO: 6.10    <br>
     * nameCN: 20厘米地温 <br>
     * unit: 1℃ <br>
     * BUFR FXY: V12030_020 <br>
     * descriptionCN: 当前时刻的20厘米地温值<br>
     * decode rule:进行纯数字校验。1000减去数值记为a1，最后取值为a1除以10<br>
     * field rule:直接赋值。
     */
    @Column("V12030_020")
    QCElement<Double> soilTemperature20CM;
    /**
     * NO: 6.11    <br>
     * nameCN: 40厘米地温 <br>
     * unit: 1℃ <br>
     * BUFR FXY: V12030_040 <br>
     * descriptionCN: 当前时刻的40厘米地温值<br>
     * decode rule:进行纯数字校验。1000减去数值记为a1，最后取值为a1除以10<br>
     * field rule:直接赋值。
     */
    @Column("V12030_040")
    QCElement<Double> soilTemperature40CM;
    /**
     * NO: 6.12    <br>
     * nameCN: 80厘米地温 <br>
     * unit: 1℃ <br>
     * BUFR FXY: V12030_080 <br>
     * descriptionCN: 当前时刻的80厘米地温值<br>
     * decode rule:进行纯数字校验。1000减去数值记为a1，最后取值为a1除以10<br>
     * field rule:直接赋值。
     */
    @Column("V12030_080")
    QCElement<Double> soilTemperature80CM;
    /**
     * NO: 6.13    <br>
     * nameCN: 160厘米地温 <br>
     * unit: 1℃ <br>
     * BUFR FXY: V12030_160 <br>
     * descriptionCN: 当前时刻的160厘米地温值<br>
     * decode rule:进行纯数字校验。1000减去数值记为a1，最后取值为a1除以10<br>
     * field rule:直接赋值。
     */
    @Column("V12030_160")
    QCElement<Double> soilTemperature160CM;
    /**
     * NO: 6.14    <br>
     * nameCN: 320厘米地温 <br>
     * unit: 1℃ <br>
     * BUFR FXY: V12030_320 <br>
     * descriptionCN: 当前时刻的320厘米地温值<br>
     * decode rule:进行纯数字校验。1000减去数值记为a1，最后取值为a1除以10<br>
     * field rule:直接赋值。
     */
    @Column("V12030_320")
    QCElement<Double> soilTemperature320CM;
    /**
     * NO: 6.15    <br>
     * nameCN: 草面温度 <br>
     * unit: 1℃ <br>
     * BUFR FXY: V12314 <br>
     * descriptionCN: 当前时刻的草面温度值<br>
     * decode rule:进行纯数字校验。1000减去数值记为a1，最后取值为a1除以10<br>
     * field rule:直接赋值。
     */
    @Column("V12314")
    QCElement<Double> grassTemperature;
    /**
     * NO: 6.16    <br>
     * nameCN: 草面最高温度 <br>
     * unit: 1℃ <br>
     * BUFR FXY: V12315 <br>
     * descriptionCN: 每1小时内的草面最高温度<br>
     * decode rule:进行纯数字校验。1000减去数值记为a1，最后取值为a1除以10<br>
     * field rule:直接赋值。
     */
    @Column("V12315")
    QCElement<Double> grassTemperatureMax;
    /**
     * NO: 6.17    <br>
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
     * NO: 6.18    <br>
     * nameCN: 草面最低温度 <br>
     * unit: 1℃ <br>
     * BUFR FXY: V12316 <br>
     * descriptionCN: 每1小时内的草面最低温度<br>
     * decode rule:进行纯数字校验。1000减去数值记为a1，最后取值为a1除以10<br>
     * field rule:直接赋值。
     */
    @Column("V12316")
    QCElement<Double> grassTemperatureMin;
    /**
     * NO: 6.19    <br>
     * nameCN: 草面最低出现时间 <br>
     * unit: <br>
     * BUFR FXY: V12316_052 <br>
     * descriptionCN: 每1小时内草面最低温度出现时间<br>
     * decode rule:进行纯数字校验。1000减去数值记为a1，最后取值为a1除以10<br>
     * field rule:直接赋值。
     */
    @Column("V12316_052")
    QCElement<Integer> grassTemperatureMinOccurrenceTime;
    /**
     * NO: 7.1  <br>
     * nameCN: 1分钟平均水平能见度 <br>
     * unit: 1m <br>
     * BUFR FXY: V20001_701_01 <br>
     * descriptionCN: 当前时刻的1分钟平均水平能见度<br>
     * decode rule:直接取值。<br>
     * field rule:直接赋值。
     */
    @Column("V20001_701_01")
    QCElement<Integer> horizontalVisibility1Minitue;
    /**
     * NO: 7.2  <br>
     * nameCN: 10分钟平均水平能见度 <br>
     * unit: 1m <br>
     * BUFR FXY: V20001_701_10 <br>
     * descriptionCN: 当前时刻的10分钟平均水平能见度<br>
     * decode rule:直接取值。<br>
     * field rule:直接赋值。
     */
    @Column("V20001_701_10")
    QCElement<Integer> horizontalVisibility10Minitues;
    /**
     * NO: 7.3  <br>
     * nameCN: 最小能见度 <br>
     * unit: 1m <br>
     * BUFR FXY: V20059 <br>
     * descriptionCN: 每1小时内的最小能见度<br>
     * decode rule:直接取值。<br>
     * field rule:直接赋值。
     */
    @Column("V20059")
    QCElement<Integer> horizontalVisibility1HourMin;
    /**
     * NO: 7.4  <br>
     * nameCN: 最小能见度出现时间 <br>
     * unit: <br>
     * BUFR FXY: V20059_052 <br>
     * descriptionCN: 每1小时内的最小能见度出现时间<br>
     * decode rule:直接取值。<br>
     * field rule:直接赋值。
     */
    @Column("V20059_052")
    QCElement<Integer> horizontalVisibility1HourMinOccurrenceTime;
    /**
     * NO: 8.1  <br>
     * nameCN: 能见度 <br>
     * unit: 1km <br>
     * BUFR FXY: V20001 <br>
     * descriptionCN: 正点的能见度，非正点为null<br>
     * decode rule:取值乘以100。<br>
     * field rule:直接赋值。
     */
    @Column("V20001")
    QCElement<Double> horizontalVisibilityHourly;
    /**
     * NO: 8.2  <br>
     * nameCN: 总云量 <br>
     * unit: 1成 <br>
     * BUFR FXY: V20010 <br>
     * descriptionCN: 正点的总云量，非正点为null<br>
     * decode rule:特殊处理：10-取值为100。进行纯数字校验。取值乘以10。<br>
     * field rule:直接赋值。
     */
    @Column("V20010")
    QCElement<Double> cloudAmount;
    /**
     * NO: 8.3  <br>
     * nameCN: 低云量 <br>
     * unit: 1成 <br>
     * BUFR FXY: V20051 <br>
     * descriptionCN: 正点的低云量，非正点为null<br>
     * decode rule:特殊处理：10-取值为100。进行纯数字校验。取值乘以10。<br>
     * field rule:直接赋值。
     */
    @Column("V20051")
    QCElement<Double> cloudAmountLow;
    /**
     * NO: 8.4  <br>
     * nameCN: 编报云量 <br>
     * unit: 1成 <br>
     * BUFR FXY: V20011 <br>
     * descriptionCN: 正点的低云状或中云状云量，由人工输入，为编报Nh<br>
     * decode rule:特殊处理：10-取值为100。进行纯数字校验。取值乘以10。<br>
     * field rule:直接赋值。
     */
    @Column("V20011")
    QCElement<Double> cloudAmountLowAndMiddle;
    /**
     * NO: 8.5  <br>
     * nameCN: 云高 <br>
     * unit: 1m <br>
     * BUFR FXY: V20013 <br>
     * descriptionCN: 正点的低（中）云状云高，由人工输入，为编报iRiXhVV中的h；当无Nh的云时，若无云高值，均写入2500<br>
     * decode rule:特殊处理：10-取值为100。进行纯数字校验。取值乘以10。<br>
     * field rule:直接赋值。
     */
    @Column("V20013")
    QCElement<Integer> cloudHeightLowAndMiddle;
    /**
     * NO: 8.6  <br>
     * nameCN: 云状 <br>
     * unit: <br>
     * BUFR FXY: V20350 <br>
     * descriptionCN: 人工输入，最多8种云，按简码编<br>
     * decode rule:直接取值。<br>
     * field rule:数组共8个元素。如果为24位“-”，数组中的元素全部为999998；如果包含“-”，先去除高位的“-”；循环取3个字符当成一个数组元素；如果数组元素包含“/”，该数组元素重新赋值为999999；如果元素不够8个，后面的元素全部用999998填充。
     */

    QCElement<String[]> cloudType;
    /**
     * NO: 8.7  <br>
     * nameCN: 云状编码（云码） <br>
     * unit: <br>
     * BUFR FXY: V20350 <br>
     * descriptionCN: 按《GD-01Ⅲ》规定形成的云状编码（CLCMCH），由人工输入云状，软件自动形成编码<br>
     * decode rule:直接取值。<br>
     * field rule:直接赋值。
     */

    QCElement<String> cloudType1;
    /**
     * NO: 8.8  <br>
     * nameCN: 现在天气现象编码 <br>
     * unit: <br>
     * BUFR FXY: V20003 <br>
     * descriptionCN: 按《GD-01Ⅲ》规定形成的现在天气现象编码(ww)，由人工输入，不能自动观测或人工输入时，固定编“//”<br>
     * decode rule:直接取值。<br>
     * field rule:直接赋值。
     */
    @Column("V20003")
    QCElement<String> presentWeather;
    /**
     * NO: 8.9  <br>
     * nameCN: 过去天气描述时间周期 <br>
     * unit: <br>
     * BUFR FXY: V04080_05 <br>
     * descriptionCN:
     * 对于天气报时次为06；补充天气报时次为03；加密天气报的00时为12，其他加密天气报时次为06；非发天气(加密)报时次，固定编“//”<br>
     * decode rule:直接取值。<br>
     * field rule:直接赋值。
     */
    @Column("V04080_05")
    QCElement<Integer> periodForPastWeather;
    /**
     * NO: 8.10   <br>
     * nameCN: 过去天气（1） <br>
     * unit: <br>
     * BUFR FXY: V20004 <br>
     * descriptionCN: 按《GD-01Ⅲ》规定形成的过去天气编码（W1），由人工输入，不能自动观测或人工输入时，固定编“//”<br>
     * decode rule:直接取值。<br>
     * field rule:直接赋值。
     */
    @Column("V20004")
    QCElement<String> pastWeather1;
    /**
     * NO: 8.11   <br>
     * nameCN: 过去天气（2） <br>
     * unit: <br>
     * BUFR FXY: V20005 <br>
     * descriptionCN: 按《GD-01Ⅲ》规定形成的过去天气编码（W2），由人工输入，不能自动观测或人工输入时，固定编“//”<br>
     * decode rule:直接取值。<br>
     * field rule:直接赋值。
     */
    @Column("V20005")
    QCElement<String> pastWeather2;
    /**
     * NO: 8.12   <br>
     * nameCN: 地面状态 <br>
     * unit: <br>
     * BUFR FXY: V20062 <br>
     * descriptionCN: 06时人工观测值，由人工输入，其他时次固定编“//”<br>
     * decode rule:直接取值。<br>
     * field rule:直接赋值。
     */
    @Column("V20062")
    QCElement<Integer> groundState;
    /**
     * NO: 9.1  <br>
     * nameCN: 积雪深度 <br>
     * unit: 1cm <br>
     * BUFR FXY: V13013 <br>
     * descriptionCN: 00时或应急加密观测时次观测值，由人工输入，00时为编报925SS组，无人工观测值时，固定编“////”<br>
     * decode rule:特殊处理：包含“，”时，取值为999990。进行纯数字校验。取值除以10<br>
     * field rule:直接赋值。
     */
    @Column("V13013")
    QCElement<Double> SnowDepth;
    /**
     * NO: 9.2  <br>
     * nameCN: 雪压 <br>
     * unit: 1g/cm2 <br>
     * BUFR FXY: V13330 <br>
     * descriptionCN: 00时或应急加密观测时次观测值，由人工输入，无人工观测值时值为null<br>
     * decode rule:特殊处理：包含“，”时，取值为999990。进行纯数字校验。取值除以10<br>
     * field rule:直接赋值。
     */
    @Column("V13330")
    QCElement<Double> snowPressure;
    /**
     * NO: 9.3  <br>
     * nameCN: 冻土深度第1栏上限值 <br>
     * unit: 1cm <br>
     * BUFR FXY: V20330_01 <br>
     * descriptionCN: 00时人工观测或应急加密观测时次观测值，由人工输入，无人工观测值时值为null<br>
     * decode rule:特殊处理：包含“，”时，取值为999990。进行纯数字校验。取值除以10<br>
     * field rule:直接赋值。
     */
    @Column("V20330_01")
    QCElement<Integer> frostDepthFirstLayerUpper;
    /**
     * NO: 9.4  <br>
     * nameCN: 冻土深度第1栏下限值 <br>
     * unit: 1cm <br>
     * BUFR FXY: V20331_01 <br>
     * descriptionCN: 00时人工观测或应急加密观测时次观测值，由人工输入，无人工观测值时值为null<br>
     * decode rule:特殊处理：包含“，”时，取值为999990。进行纯数字校验。取值除以10<br>
     * field rule:直接赋值。
     */
    @Column("V20331_01")
    QCElement<Integer> frostDepthFirstLayerLower;
    /**
     * NO: 9.5  <br>
     * nameCN: 冻土深度第2栏上限值 <br>
     * unit: 1cm <br>
     * BUFR FXY: V20330_02 <br>
     * descriptionCN: 00时人工观测或应急加密观测时次观测值，由人工输入，无人工观测值时值为null<br>
     * decode rule:特殊处理：包含“，”时，取值为999990。进行纯数字校验。取值除以10<br>
     * field rule:直接赋值。
     */
    @Column("V20330_02")
    QCElement<Integer> frostDepthSecondLayerUpper;
    /**
     * NO: 9.6  <br>
     * nameCN: 冻土深度第2栏下限值 <br>
     * unit: 1cm <br>
     * BUFR FXY: V20331_02 <br>
     * descriptionCN: 00时人工观测或应急加密观测时次观测值，由人工输入，无人工观测值时值为null<br>
     * decode rule:特殊处理：包含“，”时，取值为999990。进行纯数字校验。取值除以10<br>
     * field rule:直接赋值。
     */
    @Column("V20331_02")
    QCElement<Integer> frostDepthSecondLayerLower;
    /**
     * NO: 9.7  <br>
     * nameCN: 龙卷、尘卷风距测站距离编码 <br>
     * unit: <br>
     * BUFR FXY: <br>
     * descriptionCN:
     * 按《GD-01Ⅲ》规定输入的Mw码，在18、00、06、12时[加密]天气报中，人工输入，无人工观测值时，固定编“////”<br>
     * decode rule:特殊处理：包含“，”时，取值为999990。进行纯数字校验。取值除以10<br>
     * field rule:直接赋值。
     */
    QCElement<Integer> tornadoDistance;
    /**
     * NO: 9.8  <br>
     * nameCN: 龙卷、尘卷风距测站方位编码 <br>
     * unit: <br>
     * BUFR FXY: <br>
     * descriptionCN: 按《GD-01Ⅲ》规定输入的Da码，在18、00、06、12时，人工输入，无人工观测值时，值为null<br>
     * decode rule:特殊处理：包含“，”时，取值为999990。进行纯数字校验。取值除以10<br>
     * field rule:直接赋值。
     */
    QCElement<Integer> tornadoDirection;
    /**
     * NO: 9.9  <br>
     * nameCN: 电线积冰（雨凇）直径 <br>
     * unit: 1mm <br>
     * BUFR FXY: <br>
     * descriptionCN: 按《GD-01Ⅲ》规定在18、00、06、12时，人工输入，无人工观测值时，值为null<br>
     * decode rule:特殊处理：取编码值后面2位，编码值为00～26、91～97、99时，取空白；编码值为27，取微量；编码值为28～55时，取编码值；编码值为56～90时，取（编码值-50）×10；编码值为98时，取400；不符合特殊处理的数据取值999999。<br>
     * field rule:直接赋值。
     */
    QCElement<Integer> diameterOfOverheadWireIce;
    /**
     * NO: 9.10          <br>
     * nameCN: 最大冰雹直径 <br>
     * unit: 1mm <br>
     * BUFR FXY: <br>
     * descriptionCN: 按《GD-01Ⅲ》规定在18、00、06、12时，人工输入，无人工观测值时，值为null<br>
     * decode rule:特殊处理：取编码值后面2位。纯数字验证。<br>
     * field rule:直接赋值。
     */

    QCElement<Double> largestHailDiameter;
    /**
     * NO: 10 <br>
     * nameCN: 小时内每分钟降水量数据 <br>
     * unit: 1mm <br>
     * BUFR FXY: <br>
     * descriptionCN:<br>
     * decode rule:特殊处理:如果包含“，”，取值为0。<br>
     * field rule:直接赋值。
     */

    List<QCElement<Double>> precipitationEveryMinutes;
    /**
     * NO: 11 <br>
     * nameCN: 人工观测连续天气现象 <br>
     * unit: 1mm <br>
     * BUFR FXY: <br>
     * descriptionCN: 不能自动观测或人工输入时，值为null<br>
     * decode rule:直接取值。<br>
     * field rule:直接赋值。
     */
    String weatherManualObservational;

    

    public String getStationNumberChina() {
		return stationNumberChina;
	}



	public void setStationNumberChina(String stationNumberChina) {
		this.stationNumberChina = stationNumberChina;
	}



	public Date getObservationTime() {
		return observationTime;
	}



	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
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



	public QualityControl[] getQualityControl() {
		return qualityControl;
	}



	public void setQualityControl(QualityControl[] qualityControl) {
		this.qualityControl = qualityControl;
	}



	public String getCorrectionIndicator() {
		return correctionIndicator;
	}



	public void setCorrectionIndicator(String correctionIndicator) {
		this.correctionIndicator = correctionIndicator;
	}



	public QCElement<Double> getStationPressure() {
		return stationPressure;
	}



	public void setStationPressure(QCElement<Double> stationPressure) {
		this.stationPressure = stationPressure;
	}



	public QCElement<Double> getPressureReducedToMeanSeaLevel() {
		return pressureReducedToMeanSeaLevel;
	}



	public void setPressureReducedToMeanSeaLevel(QCElement<Double> pressureReducedToMeanSeaLevel) {
		this.pressureReducedToMeanSeaLevel = pressureReducedToMeanSeaLevel;
	}



	public QCElement<Double> getPressureChange3Hours() {
		return pressureChange3Hours;
	}



	public void setPressureChange3Hours(QCElement<Double> pressureChange3Hours) {
		this.pressureChange3Hours = pressureChange3Hours;
	}



	public QCElement<Double> getPressureChange24Hours() {
		return pressureChange24Hours;
	}



	public void setPressureChange24Hours(QCElement<Double> pressureChange24Hours) {
		this.pressureChange24Hours = pressureChange24Hours;
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



	public QCElement<Double> getTemperatureChange24Hours() {
		return temperatureChange24Hours;
	}



	public void setTemperatureChange24Hours(QCElement<Double> temperatureChange24Hours) {
		this.temperatureChange24Hours = temperatureChange24Hours;
	}



	public QCElement<Double> getTemperatureMax24Hours() {
		return temperatureMax24Hours;
	}



	public void setTemperatureMax24Hours(QCElement<Double> temperatureMax24Hours) {
		this.temperatureMax24Hours = temperatureMax24Hours;
	}



	public QCElement<Double> getTemperatureMin24Hours() {
		return temperatureMin24Hours;
	}



	public void setTemperatureMin24Hours(QCElement<Double> temperatureMin24Hours) {
		this.temperatureMin24Hours = temperatureMin24Hours;
	}



	public QCElement<Double> getWetBulbTemperature() {
		return wetBulbTemperature;
	}



	public void setWetBulbTemperature(QCElement<Double> wetBulbTemperature) {
		this.wetBulbTemperature = wetBulbTemperature;
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



	public QCElement<Double> getPrecipitation1Hour() {
		return precipitation1Hour;
	}



	public void setPrecipitation1Hour(QCElement<Double> precipitation1Hour) {
		this.precipitation1Hour = precipitation1Hour;
	}



	public QCElement<Double> getPrecipitation3Hours() {
		return precipitation3Hours;
	}



	public void setPrecipitation3Hours(QCElement<Double> precipitation3Hours) {
		this.precipitation3Hours = precipitation3Hours;
	}



	public QCElement<Double> getPrecipitation6Hours() {
		return precipitation6Hours;
	}



	public void setPrecipitation6Hours(QCElement<Double> precipitation6Hours) {
		this.precipitation6Hours = precipitation6Hours;
	}



	public QCElement<Double> getPrecipitation12Hours() {
		return precipitation12Hours;
	}



	public void setPrecipitation12Hours(QCElement<Double> precipitation12Hours) {
		this.precipitation12Hours = precipitation12Hours;
	}



	public QCElement<Double> getPrecipitation24Hours() {
		return precipitation24Hours;
	}



	public void setPrecipitation24Hours(QCElement<Double> precipitation24Hours) {
		this.precipitation24Hours = precipitation24Hours;
	}



	public QCElement<Integer> getIntervalOfAdditionalManualObservationsOfPrecipitation() {
		return intervalOfAdditionalManualObservationsOfPrecipitation;
	}



	public void setIntervalOfAdditionalManualObservationsOfPrecipitation(
			QCElement<Integer> intervalOfAdditionalManualObservationsOfPrecipitation) {
		this.intervalOfAdditionalManualObservationsOfPrecipitation = intervalOfAdditionalManualObservationsOfPrecipitation;
	}



	public QCElement<Double> getPrecipitationAdditionalManualObservational() {
		return precipitationAdditionalManualObservational;
	}



	public void setPrecipitationAdditionalManualObservational(
			QCElement<Double> precipitationAdditionalManualObservational) {
		this.precipitationAdditionalManualObservational = precipitationAdditionalManualObservational;
	}



	public QCElement<Double> getEvaporation() {
		return evaporation;
	}



	public void setEvaporation(QCElement<Double> evaporation) {
		this.evaporation = evaporation;
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



	public QCElement<Double> getExtremeWindSpeed6Hours() {
		return extremeWindSpeed6Hours;
	}



	public void setExtremeWindSpeed6Hours(QCElement<Double> extremeWindSpeed6Hours) {
		this.extremeWindSpeed6Hours = extremeWindSpeed6Hours;
	}



	public QCElement<Double> getExtremeWindDirection6Hours() {
		return extremeWindDirection6Hours;
	}



	public void setExtremeWindDirection6Hours(QCElement<Double> extremeWindDirection6Hours) {
		this.extremeWindDirection6Hours = extremeWindDirection6Hours;
	}



	public QCElement<Double> getExtremeWindSpeed12Hours() {
		return extremeWindSpeed12Hours;
	}



	public void setExtremeWindSpeed12Hours(QCElement<Double> extremeWindSpeed12Hours) {
		this.extremeWindSpeed12Hours = extremeWindSpeed12Hours;
	}



	public QCElement<Double> getExtremeWindDirection12Hours() {
		return extremeWindDirection12Hours;
	}



	public void setExtremeWindDirection12Hours(QCElement<Double> extremeWindDirection12Hours) {
		this.extremeWindDirection12Hours = extremeWindDirection12Hours;
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



	public QCElement<Double> getLandSurfaceTemperatureMin12Hours() {
		return landSurfaceTemperatureMin12Hours;
	}



	public void setLandSurfaceTemperatureMin12Hours(QCElement<Double> landSurfaceTemperatureMin12Hours) {
		this.landSurfaceTemperatureMin12Hours = landSurfaceTemperatureMin12Hours;
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



	public QCElement<Integer> getHorizontalVisibility1Minitue() {
		return horizontalVisibility1Minitue;
	}



	public void setHorizontalVisibility1Minitue(QCElement<Integer> horizontalVisibility1Minitue) {
		this.horizontalVisibility1Minitue = horizontalVisibility1Minitue;
	}



	public QCElement<Integer> getHorizontalVisibility10Minitues() {
		return horizontalVisibility10Minitues;
	}



	public void setHorizontalVisibility10Minitues(QCElement<Integer> horizontalVisibility10Minitues) {
		this.horizontalVisibility10Minitues = horizontalVisibility10Minitues;
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



	public QCElement<Double> getHorizontalVisibilityHourly() {
		return horizontalVisibilityHourly;
	}



	public void setHorizontalVisibilityHourly(QCElement<Double> horizontalVisibilityHourly) {
		this.horizontalVisibilityHourly = horizontalVisibilityHourly;
	}



	public QCElement<Double> getCloudAmount() {
		return cloudAmount;
	}



	public void setCloudAmount(QCElement<Double> cloudAmount) {
		this.cloudAmount = cloudAmount;
	}



	public QCElement<Double> getCloudAmountLow() {
		return cloudAmountLow;
	}



	public void setCloudAmountLow(QCElement<Double> cloudAmountLow) {
		this.cloudAmountLow = cloudAmountLow;
	}



	public QCElement<Double> getCloudAmountLowAndMiddle() {
		return cloudAmountLowAndMiddle;
	}



	public void setCloudAmountLowAndMiddle(QCElement<Double> cloudAmountLowAndMiddle) {
		this.cloudAmountLowAndMiddle = cloudAmountLowAndMiddle;
	}



	public QCElement<Integer> getCloudHeightLowAndMiddle() {
		return cloudHeightLowAndMiddle;
	}



	public void setCloudHeightLowAndMiddle(QCElement<Integer> cloudHeightLowAndMiddle) {
		this.cloudHeightLowAndMiddle = cloudHeightLowAndMiddle;
	}



	public QCElement<String[]> getCloudType() {
		return cloudType;
	}



	public void setCloudType(QCElement<String[]> cloudType) {
		this.cloudType = cloudType;
	}



	public QCElement<String> getCloudType1() {
		return cloudType1;
	}



	public void setCloudType1(QCElement<String> cloudType1) {
		this.cloudType1 = cloudType1;
	}



	public QCElement<String> getPresentWeather() {
		return presentWeather;
	}



	public void setPresentWeather(QCElement<String> presentWeather) {
		this.presentWeather = presentWeather;
	}



	public QCElement<Integer> getPeriodForPastWeather() {
		return periodForPastWeather;
	}



	public void setPeriodForPastWeather(QCElement<Integer> periodForPastWeather) {
		this.periodForPastWeather = periodForPastWeather;
	}



	public QCElement<String> getPastWeather1() {
		return pastWeather1;
	}



	public void setPastWeather1(QCElement<String> pastWeather1) {
		this.pastWeather1 = pastWeather1;
	}



	public QCElement<String> getPastWeather2() {
		return pastWeather2;
	}



	public void setPastWeather2(QCElement<String> pastWeather2) {
		this.pastWeather2 = pastWeather2;
	}



	public QCElement<Integer> getGroundState() {
		return groundState;
	}



	public void setGroundState(QCElement<Integer> groundState) {
		this.groundState = groundState;
	}



	public QCElement<Double> getSnowDepth() {
		return SnowDepth;
	}



	public void setSnowDepth(QCElement<Double> snowDepth) {
		SnowDepth = snowDepth;
	}



	public QCElement<Double> getSnowPressure() {
		return snowPressure;
	}



	public void setSnowPressure(QCElement<Double> snowPressure) {
		this.snowPressure = snowPressure;
	}



	public QCElement<Integer> getFrostDepthFirstLayerUpper() {
		return frostDepthFirstLayerUpper;
	}



	public void setFrostDepthFirstLayerUpper(QCElement<Integer> frostDepthFirstLayerUpper) {
		this.frostDepthFirstLayerUpper = frostDepthFirstLayerUpper;
	}



	public QCElement<Integer> getFrostDepthFirstLayerLower() {
		return frostDepthFirstLayerLower;
	}



	public void setFrostDepthFirstLayerLower(QCElement<Integer> frostDepthFirstLayerLower) {
		this.frostDepthFirstLayerLower = frostDepthFirstLayerLower;
	}



	public QCElement<Integer> getFrostDepthSecondLayerUpper() {
		return frostDepthSecondLayerUpper;
	}



	public void setFrostDepthSecondLayerUpper(QCElement<Integer> frostDepthSecondLayerUpper) {
		this.frostDepthSecondLayerUpper = frostDepthSecondLayerUpper;
	}



	public QCElement<Integer> getFrostDepthSecondLayerLower() {
		return frostDepthSecondLayerLower;
	}



	public void setFrostDepthSecondLayerLower(QCElement<Integer> frostDepthSecondLayerLower) {
		this.frostDepthSecondLayerLower = frostDepthSecondLayerLower;
	}



	public QCElement<Integer> getTornadoDistance() {
		return tornadoDistance;
	}



	public void setTornadoDistance(QCElement<Integer> tornadoDistance) {
		this.tornadoDistance = tornadoDistance;
	}



	public QCElement<Integer> getTornadoDirection() {
		return tornadoDirection;
	}



	public void setTornadoDirection(QCElement<Integer> tornadoDirection) {
		this.tornadoDirection = tornadoDirection;
	}



	public QCElement<Integer> getDiameterOfOverheadWireIce() {
		return diameterOfOverheadWireIce;
	}



	public void setDiameterOfOverheadWireIce(QCElement<Integer> diameterOfOverheadWireIce) {
		this.diameterOfOverheadWireIce = diameterOfOverheadWireIce;
	}



	public QCElement<Double> getLargestHailDiameter() {
		return largestHailDiameter;
	}



	public void setLargestHailDiameter(QCElement<Double> largestHailDiameter) {
		this.largestHailDiameter = largestHailDiameter;
	}



	public List<QCElement<Double>> getPrecipitationEveryMinutes() {
		return precipitationEveryMinutes;
	}



	public void setPrecipitationEveryMinutes(List<QCElement<Double>> precipitationEveryMinutes) {
		this.precipitationEveryMinutes = precipitationEveryMinutes;
	}



	public String getWeatherManualObservational() {
		return weatherManualObservational;
	}



	public void setWeatherManualObservational(String weatherManualObservational) {
		this.weatherManualObservational = weatherManualObservational;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	public int getCloudType1Detail(int i) {
    	char c = this.cloudType1.getValue().charAt(i);
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


