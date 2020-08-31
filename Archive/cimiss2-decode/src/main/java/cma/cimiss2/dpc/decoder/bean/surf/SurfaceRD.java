package cma.cimiss2.dpc.decoder.bean.surf;

import java.util.Date;
import java.util.List;

import cma.cimiss2.dpc.decoder.bean.QCElement;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
	公路交通气象观测站实时地面气象要素资料实体类
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《中国公路交通基本气象要素数据表》 </a>
 *      <li> <a href=" "> 《中国公路交通分钟降水资料数据表》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月24日 下午3:02:40   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class SurfaceRD {
	// 1 测站基本信息段
	/**
	 * NO: 1.1  <br>
	 * nameCN: 区站号 <br>
	 * unit: 无  <br>
	 * BUFR FXY: V01301 <br>
	 * descriptionCN: 5位数字或第1位为字母，第2-5位为数字<br>
	 * decode rule:直接取值 <br>
     * field rule:直接赋值 <br>
	 */
	private String stationNumberChina;

	/**
	 * NO: 1.2  <br>
	 * nameCN: 观测时间 <br>
	 * unit: <br>
	 * BUFR FXY: V04001/V04002/V04003/V04004/V04005/V04006 <br>
	 * descriptionCN:  <br>
	 * decode rule:直接取值 <br>
	 * field rule:使用java时间转化工具类进行转化,标准形式为：yyyyMMddHHmmss <br>
	 */
	private Date observationTime;
	
	/**
	 * NO: 1.3  <br>
	 * nameCN: 纬度 <br>
	 * unit: 度 <br>
	 * BUFR FXY: V05001 <br>
	 * descriptionCN:<br>
	 * decode rule: 度分秒转化为度 <br>
	 * field rule:直接赋值<br>
	 */
	private Double latitude;
	
	/**
	 * NO: 1.4  <br>
	 * nameCN: 经度 <br>
	 * unit: 度 <br>
	 * BUFR FXY: V06001 <br>
	 * descriptionCN:<br>
	 * decode rule: 度分秒转化为度 <br>
	 * field rule:直接赋值<br>
	 */
	private Double longitude;
	
	/**
	 * NO: 1.5  <br>
	 * nameCN: 观测场拔海高度 <br>
	 * unit: 1m <br>
	 * BUFR FXY: V07001 <br>
	 * descriptionCN: 保留一位小数，扩大10倍记录，高位不足补“0”，若低于海平面，首位存入“-”<br>
	 * decode rule: 取值后除以10<br>
	 * field rule: 直接赋值<br>
	 */
	private double heightOfSationGroundAboveMeanSeaLevel;
	
	/**
	 * No： 1.6 <br>
	 * nameCN: 质量控制标识 <br>
	 * unit: 无 <br>
	 * BUFR FXY: V_SQCODE\V_PQCODE\V_NQCODE <br>
	 * descriptionCN: 依次标识设备级、省级、国家级对观测数据进行质量控制的情况。“1”为软件自动作过质量控制，“0”为由人机交互进一步作过质量控制，“9”为没有进行任何质量控制<br>
	 * decode rule: 直接取值<br>
	 * field rule: 直接赋值<br>
	 */
	private String qualityControl;

	/** 
	 * No： 1.7 <br>
	 * nameCN: 文件更正标识 <br>
	 * unit: 无  <br>
	 * BUFR FXY: V_BBB <br>
	 * descriptionCN: 为非更正数据时，固定编“000”；为测站更正数据时，第一次更正编码“CCA”，第二次更正编码“CCB”,依次编码。<br>
	 * decode rule: 直接取值<br>
	 * field rule: 直接赋值<br>
	 */
	private String fileRevisionSign;
	
	/*
	 * 2 温度和湿度数据
	 */
	/**
	 * No: 2.1 <br>
	 * nameCN: 气温 <br>
	 * unit: 1度 <br>
	 * BUFR FXY: V12001 <br>
	 * descriptionCN: 当前时刻的空气温度<br>
	 * decode rule: (1000-取值)*0.1 <br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Float> airTemperature;
	
	/**
	 * No: 2.2 <br>
	 * nameCN: 最高气温 <br>
	 * unit: 1度 <br>
	 * BUFR FXY: V12011 <br>
	 * descriptionCN: 每1小时内的最高气温<br>
	 * decode rule: (1000-取值)*0.1 <br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Float> maxAirTemperature;
	
	/**
	 * No: 2.3 <br>
	 * nameCN: 最高气温出现时间<br>
	 * unit： <br>
	 * BUFR FXY: V12011_052 <br>
	 * decriptionCN: 每1小时内最高气温出现时间<br>
	 * decode rule: 直接取值 <br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Integer> timeOfMaxAirTemperature;
	
	/**
	 * No: 2.4 <br>
	 * nameCN: 最低气温<br>
	 * unit: 1度 <br>
	 * BUFR FXY: V12012 <br>
	 * descriptionCN: 每1小时内的最低气温<br>
	 * decode rule: (1000-取值)*0.1 <br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Float> minAirTemperature;
	
	/**
	 * No: 2.5 <br>
	 * nameCN: 最低气温出现时间<br>
	 * unit: <br>
	 * BUFR FXY: V12012_052 <br>
	 * descriptionCN: 每1小时内最低气温出现时间<br>
	 * decode rule: 直接取值  <br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Integer> timeOfMinAirTemperature;
	
	/**
	 * No: 2.6 <br>
	 * nameCN: 露点温度 <br>
	 * unit: 1度 <br>
	 * BUFR FXY: V12003 <br>
	 * descriptionCN: 当前时刻的露点温度值<br>
	 * decode rule: (1000-取值)*0.1 <br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Float> dewpointTemperature;
	
	/**
	 * No: 2.7 <br>
	 * nameCN: 相对湿度<br>
	 * unit: % <br>
	 * BUFR FXY: V13003 <br>
	 * descriptionCN: 当前时刻的相对湿度<br>
	 * decode rule: 直接取值  <br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Integer> relativeHumidity;
	
	/**
	 * No: 2.8 <br>
	 * namecCN: 最小相对湿度<br>
	 * unit: % <br>
	 * BUFR FXY: V13007 <br>
	 * descriptionCN: 每1小时内的最小相对湿度值<br>
	 * decode rule: 直接取值  <br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Integer> minRelativeHumidity;
	
	/**
	 * No: 2.9 <br>
	 * nameCN: 最小相对湿度出现时间 <br>
	 * unit: <br>
	 * BUFR FXY: V13007_052 <br>
	 * descriptionCN: <br>
	 * decode rule: 直接取值  <br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Integer> timeOfMinRelativeHumidity;
	
	/**
	 * No: 2.10 <br>
	 * nameCN: 水汽压 <br>
	 * unit: 1hPa <br>
	 * BUFR FXY: V13004 <br>
	 * descriptionCN: 当前时刻的水汽压值 <br>
	 * decode rule: 取值除以10 <br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Float> waterVaporPressure;
	
	/*3 累计降水量数据
	 */
	/**
	 * No: 3.1 <br>
	 * nameCN: 小时累计降水量 <br>
	 * unit: 1mm <br>
	 * BUFR FXY: V13019 <br>
	 * descriptionCN: 每1小时内的降水量累计量，从前一整点到当前时刻的累计值。<br>
	 * decode rule: 取值除以10 <br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Float> hourlyCumulativePrecipitation;

	/*
	 * 4 风观测数据
	 */
	/**
	 * No: 4.1 <br>
	 * nameCN: 2分钟风向<br>
	 * unit: 1度 <br>
	 * BUFR FXY: V11290 <br>
	 * descriptionCN:当前时刻的2分钟平均风向<br>
	 * decode rule: 静风时，取值999017，不然直接取值<br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Integer> twoMinWindDirection;
	
	/**
	 * No: 4.2 <br>
	 * nameCN: 2分钟平均风速<br>
	 * unit: 1 m/s <br>
	 * BUFR FXY: V11291 <br>
	 * descriptionCN: 当前时刻的2分钟平均风速 <br>
	 * decode rule: 取值除以10 <br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Float> twoMinWindAvgSpeed;
	
	/**
	 * No: 4.3 <br>
	 * nameCN: 10分钟风向  <br>
	 * unit: 度 <br>
	 * BUFR FXY: V11292 <br>
	 * descriptionCN: 当前时刻的10分钟平均风向<br>
	 * decode rule: 静风时，取值999017，不然直接取值<br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Integer> tenMinWindDirection;
	
	/**
	 * No: 4.4 <br>
	 * nameCN: 10分钟平均风速 <br>
	 * unit: 1 m/s <br>
	 * BUFR FXY: V11293 <br>
	 * descriptionCN: 当前时刻的10分钟平均风速<br>
	 * decode rule: 取值除以10 <br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Float> tenMinWindAvgSpeed;
	
	/**
	 * No: 4.5 <br>
	 * nameCN: 最大风速的风向 <br>
	 * unit: 1度 <br>
	 * BUFR FXY: V11296 <br>
	 * descriptionCN: 每1小时内10分钟最大风速的风向<br>
	 * decode rule: 静风时，取值999017，不然直接取值<br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Integer> directionOfMaxWindSpeed;
	
	/**
	 * No: 4.6 <br>
	 * nameCN: 最大风速<br>
	 * unit: 1 m/s <br>
	 * BUFR FXY: V11042 <br>
	 * descriptionCN: 每1小时内10分钟最大风速<br>
	 * decode rule: 取值除以10 <br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Float> maxWindSpeed;
	
	/**
	 * No: 4.7 <br>
	 * nameCN: 最大风速出现时间<br>
	 * unit:  <br>
	 * BUFR FXY: V11042_052 <br>
	 * descriptionCN: 每1小时内10分钟最大风速出现时间，时分各两位，下同<br>
	 * decode rule: 直接取值 <br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Integer> timeOfMaxWindSpeed;
	
	/**
	 * No: 4.8 <br>
	 * nameCN: 瞬时风向 <br>
	 * unit:  1度 <br>
	 * BUFR FXY:V11001 <br>
	 * descriptionCN: 当前时刻的瞬时风向<br>
	 * decode rule: 静风时，取值999017，不然直接取值<br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Integer> instantaneousWindDirection;
	
	/**
	 * No: 4.9 <br>
	 * nameCN: 瞬时风速 <br>
	 * unit: 1 m/s <br>
	 * BUFR FXY: V11002 <br>
	 * descriptionCN: 当前时刻的瞬时风速<br>
	 * decode rule: 取值除以10 <br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Float> instantaneousWindSpeed;
	
	/**
	 * No: 4.10 <br>
	 * nameCN: 极大风速的风向 <br>
	 * unit:  1 度 <br>
	 * BUFR FXY:V11211 <br>
	 * descriptionCN: 每1小时内的极大风速的风向<br>
	 * decode rule: 静风时，取值999017，不然直接取值<br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Integer> directionOfExtremWindSpeed;
	
	/**
	 * No: 4.11 <br>
	 * nameCN: 极大风速<br>
	 * unit:  1 m/s <br>
	 * BUFR FXY:V11046 <br>
	 * descriptionCN: 每1小时内的极大风速<br>
	 * decode rule: 取值除以10 <br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Float> extremWindSpeed;
	
	/**
	 * No: 4.12 <br>
	 * nameCN: 极大风速出现的时间 <br>
	 * unit:   <br>
	 * BUFR FXY: V11046_052 <br>
	 * descriptionCN: 每1小时内极大风速出现时间<br>
	 * decode rule: 直接取值 <br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Integer> timeOfExtremWindSpeed;
	
	/**
	 * No: 4.13 <br>
	 * nameCN: 分钟内极大风速的风向  <br>
	 * unit: 1度 <br>
	 * BUFR FXY: V11211_001 <br>
	 * descriptionCN: 每分钟内的极大风速的风向<br>
	 * decode rule: 静风时，取值999017，不然直接取值<br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Integer> directionOfExtremWindSpeedMinutely;
	
	/**
	 * No: 4.14 <br>
	 * nameCN: 分钟内极大风速  <br>
	 * unit:  1 m/s <br>
	 * BUFR FXY: V11046_001 <br>
	 * descriptionCN: 每分钟内的极大风速<br>
	 * decode rule: 取值除以10 <br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Float> extremeWindSpeedMinutely;
	
	/**
	 * 5 路温数据
	 */
	/**
	 * No: 5.1 <br>
	 * nameCN: 路面温度  <br>
	 * unit:  1度 <br>
	 * BUFR FXY: V12421 <br>
	 * descriptionCN: 当前时刻的路面温度值<br>
	 * decode rule: (1000-取值)*0.1 <br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Float> roadSurfTemperature;
	
	/**
	 * No: 5.2 <br>
	 * nameCN: 路面最高温度  <br>
	 * unit:  1度 <br>
	 * BUFR FXY:V12422 <br>
	 * descriptionCN: 当前时刻的路面最高温度值<br>
	 * decode rule: (1000-取值)*0.1 <br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Float> maxRoadSurfTemperature;
	
	/**
	 * No: 5.3 <br>
	 * nameCN: 路面最高温度出现时间  <br>
	 * unit:   <br>
	 * BUFR FXY: V12422_052 <br>
	 * descriptionCN: 每1小时内路面最高温度出现时间<br>
	 * decode rule: 直接取值  <br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Integer> timeOfMaxRoadSurfTemperature;
	
	/**
	 * No: 5.4 <br>
	 * nameCN: 路面最低温度<br>
	 * unit:  1度 <br>
	 * BUFR FXY: V12423<br>
	 * descriptionCN: 每1小时内的路面最低温度<br>
	 * decode rule: (1000-取值)*0.1 <br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Float> minRoadSurfTemperature;
	
	/**
	 * No: 5.5 <br>
	 * nameCN: 路面最低温度出现时间<br>
	 * unit:   <br>
	 * BUFR FXY: V12423_052 <br>
	 * descriptionCN: 每1小时内路面最低温度出现时间<br>
	 * decode rule: 直接取值<br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Integer> timeOfMinRoadSurfTemperature;
	
	/**
	 * No: 5.6 <br>
	 * nameCN: 路基温度（-10cm）<br>
	 * unit: 1度 <br>
	 * BUFR FXY:V12424 <br>
	 * descriptionCN: 当前时刻的10厘米路温值<br>
	 * decode rule: (1000-取值)*0.1 <br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Float> roadBaseTemperature;
	
	/*
	 * 6能见度数据
	 */
	/**
	 * No: 6.1 <br>
	 * nameCN: 1分钟平均水平能见度 <br>
	 * unit:  1m <br>
	 * BUFR FXY: V20001_701_01 <br>
	 * descriptionCN: 当前时刻的1分钟平均水平能见度<br>
	 * decode rule: 直接取值 <br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Integer> oneMinAvgVisibility;
	
	/**
	 * No: 6.2 <br>
	 * nameCN: 最小能见度  <br>
	 * unit:  1m <br>
	 * BUFR FXY: V20059 <br>
	 * descriptionCN: 每1小时内的最小能见度<br>
	 * decode rule: 直接取值 <br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Integer> minVisibility;
	
	/**
	 * No: 6.3 <br>
	 * nameCN: 最小能见度出现时间   <br>
	 * unit:   <br>
	 * BUFR FXY: V20059_052 <br>
	 * descriptionCN: 每1小时内的最小能见度出现时间<br>
	 * decode rule: 直接取值 <br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Integer> timeOfMinVisibility;
	
	/*
	 * 7 路面状况数据
	 */
	/**
	 * No: 7.1 <br>
	 * nameCN: 路面状态 <br>
	 * unit:  <br>
	 * BUFR FXY: V20062_1\V20062_2\V20062_3<br>
	 * descriptionCN: 当前时刻路面状态，每种状态2位，最多存入3种现象，不足3种现象时低位用相应长度的“-”填充。 <br>
	 * decode rule: 直接取值 <br>
	 * field rule: 直接赋值 <br>
	 */
	private List<QCElement<Integer>> roadSurfCondition;
	
	/**
	 * No: 7.2 <br>
	 * nameCN: 路面雪层厚度<br>
	 * unit: 1mm <br>
	 * BUFR FXY: V13013<br>
	 * descriptionCN: 当前时刻雪层厚度  <br>
	 * decode rule: 取值除以10 <br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Float> snowThickness;
	
	/**
	 * No: 7.3 <br>
	 * nameCN: 路面水层厚度<br>
	 * unit: 1mm <br>
	 * BUFR FXY: V13368 <br>
	 * descriptionCN: 当前时刻水层厚度  <br>
	 * decode rule: 取值除以10 <br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Float> waterThickness;
	
	/**
	 * No: 7.4 <br>
	 * nameCN: 路面冰层厚度 <br>
	 * unit: 1mm <br>
	 * BUFR FXY: V13369 <br>
	 * descriptionCN: 当前时刻冰层厚度<br>
	 * decode rule: 取值除以10 <br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Float> iceThickness;
	
	/**
	 * No: 7.5 <br>
	 * nameCN: 路面冰点温度 <br>
	 * unit: 1度 <br>
	 * BUFR FXY: V12425 <br>
	 * descriptionCN: 当前时刻冰点温度<br>
	 * decode rule: 取值除以10 <br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Float> freezingPointTemperature;
	
	/**
	 * No: 7.6 <br>
	 * nameCN: 融雪剂浓度  <br>
	 * unit: % <br>
	 * BUFR FXY: V15505 <br>
	 * descriptionCN: 当前时刻融雪剂浓度<br>
	 * decode rule: 取值除以10 <br>
	 * field rule: 直接赋值 <br>
	 */
	private QCElement<Float> concentrationOfSnowMeltAgent;
	
	/*
	 * 8 天气现象数据
	 */
	/**
	 * No: 8.1 <br>
	 * nameCN: 现在天气现象编码 <br>
	 * unit:  <br>
	 * BUFR FXY: V20003_01\V20003_02\V20003_03\V20003_04\V20003_05\V20003_06<br>
	 * descriptionCN: 按世界气象组织（WMO）有关自动气象站SYNOP天气代码表示，每种天气现象2位，最多存入6种现象，不足6种现象时低位用相应长度的“-”填充。<br>
	 * decode rule: 直接取值 <br>
	 * field rule: 直接赋值 <br>
	 */
	private List<QCElement<Integer>> weatherPhenomenonCode;
    
	/*
	 * 9. 小时内逐分钟降水量数据
	 */
	/**
	 * No: 9.1 <br>
	 * nameCN: 小时内逐分钟降水量  <br>
	 * unit: 1mm <br>
	 * BUFR FXY: V13011 <br>
	 * descriptionCN: 小时内逐分钟降水量共120Byte，每分钟2Byte，即1～2位为第1分钟的记录，3～4位为第2分钟的记录……，如此类推，119～120位为第60分钟的记录；每分钟内无降水时存入“00”，微量存入“,,”，降水量≥10.0mm时，一律存入99，缺测存入“//”。传输时间间隔不足60分钟时，剩余字节用相应长度的“-”填充。<br>
	 * decode rule: 微量降水取值999990，缺省取值999999，--取值999998，其他取值除以10 <br>
	 * field rule: 直接赋值 <br>
	 */
	private List<QCElement<Float>> minutelyPrecipitation;

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

	public double getHeightOfSationGroundAboveMeanSeaLevel() {
		return heightOfSationGroundAboveMeanSeaLevel;
	}

	public void setHeightOfSationGroundAboveMeanSeaLevel(double heightOfSationGroundAboveMeanSeaLevel) {
		this.heightOfSationGroundAboveMeanSeaLevel = heightOfSationGroundAboveMeanSeaLevel;
	}

	public String getQualityControl() {
		return qualityControl;
	}

	public void setQualityControl(String qualityControl) {
		this.qualityControl = qualityControl;
	}

	public String getFileRevisionSign() {
		return fileRevisionSign;
	}

	public void setFileRevisionSign(String fileRevisionSign) {
		this.fileRevisionSign = fileRevisionSign;
	}

	public QCElement<Float> getAirTemperature() {
		return airTemperature;
	}

	public void setAirTemperature(QCElement<Float> airTemperature) {
		this.airTemperature = airTemperature;
	}

	public QCElement<Float> getMaxAirTemperature() {
		return maxAirTemperature;
	}

	public void setMaxAirTemperature(QCElement<Float> maxAirTemperature) {
		this.maxAirTemperature = maxAirTemperature;
	}

	public QCElement<Integer> getTimeOfMaxAirTemperature() {
		return timeOfMaxAirTemperature;
	}

	public void setTimeOfMaxAirTemperature(QCElement<Integer> timeOfMaxAirTemperature) {
		this.timeOfMaxAirTemperature = timeOfMaxAirTemperature;
	}

	public QCElement<Float> getMinAirTemperature() {
		return minAirTemperature;
	}

	public void setMinAirTemperature(QCElement<Float> minAirTemperature) {
		this.minAirTemperature = minAirTemperature;
	}

	public QCElement<Integer> getTimeOfMinAirTemperature() {
		return timeOfMinAirTemperature;
	}

	public void setTimeOfMinAirTemperature(QCElement<Integer> timeOfMinAirTemperature) {
		this.timeOfMinAirTemperature = timeOfMinAirTemperature;
	}

	public QCElement<Float> getDewpointTemperature() {
		return dewpointTemperature;
	}

	public void setDewpointTemperature(QCElement<Float> dewpointTemperature) {
		this.dewpointTemperature = dewpointTemperature;
	}

	public QCElement<Integer> getRelativeHumidity() {
		return relativeHumidity;
	}

	public void setRelativeHumidity(QCElement<Integer> relativeHumidity) {
		this.relativeHumidity = relativeHumidity;
	}

	public QCElement<Integer> getMinRelativeHumidity() {
		return minRelativeHumidity;
	}

	public void setMinRelativeHumidity(QCElement<Integer> minRelativeHumidity) {
		this.minRelativeHumidity = minRelativeHumidity;
	}

	public QCElement<Integer> getTimeOfMinRelativeHumidity() {
		return timeOfMinRelativeHumidity;
	}

	public void setTimeOfMinRelativeHumidity(QCElement<Integer> timeOfMinRelativeHumidity) {
		this.timeOfMinRelativeHumidity = timeOfMinRelativeHumidity;
	}

	public QCElement<Float> getWaterVaporPressure() {
		return waterVaporPressure;
	}

	public void setWaterVaporPressure(QCElement<Float> waterVaporPressure) {
		this.waterVaporPressure = waterVaporPressure;
	}

	public QCElement<Float> getHourlyCumulativePrecipitation() {
		return hourlyCumulativePrecipitation;
	}

	public void setHourlyCumulativePrecipitation(QCElement<Float> hourlyCumulativePrecipitation) {
		this.hourlyCumulativePrecipitation = hourlyCumulativePrecipitation;
	}

	public QCElement<Integer> getTwoMinWindDirection() {
		return twoMinWindDirection;
	}

	public void setTwoMinWindDirection(QCElement<Integer> twoMinWindDirection) {
		this.twoMinWindDirection = twoMinWindDirection;
	}

	public QCElement<Float> getTwoMinWindAvgSpeed() {
		return twoMinWindAvgSpeed;
	}

	public void setTwoMinWindAvgSpeed(QCElement<Float> twoMinWindAvgSpeed) {
		this.twoMinWindAvgSpeed = twoMinWindAvgSpeed;
	}

	public QCElement<Integer> getTenMinWindDirection() {
		return tenMinWindDirection;
	}

	public void setTenMinWindDirection(QCElement<Integer> tenMinWindDirection) {
		this.tenMinWindDirection = tenMinWindDirection;
	}

	public QCElement<Float> getTenMinWindAvgSpeed() {
		return tenMinWindAvgSpeed;
	}

	public void setTenMinWindAvgSpeed(QCElement<Float> tenMinWindAvgSpeed) {
		this.tenMinWindAvgSpeed = tenMinWindAvgSpeed;
	}

	public QCElement<Integer> getDirectionOfMaxWindSpeed() {
		return directionOfMaxWindSpeed;
	}

	public void setDirectionOfMaxWindSpeed(QCElement<Integer> directionOfMaxWindSpeed) {
		this.directionOfMaxWindSpeed = directionOfMaxWindSpeed;
	}

	public QCElement<Float> getMaxWindSpeed() {
		return maxWindSpeed;
	}

	public void setMaxWindSpeed(QCElement<Float> maxWindSpeed) {
		this.maxWindSpeed = maxWindSpeed;
	}

	public QCElement<Integer> getTimeOfMaxWindSpeed() {
		return timeOfMaxWindSpeed;
	}

	public void setTimeOfMaxWindSpeed(QCElement<Integer> timeOfMaxWindSpeed) {
		this.timeOfMaxWindSpeed = timeOfMaxWindSpeed;
	}

	public QCElement<Integer> getInstantaneousWindDirection() {
		return instantaneousWindDirection;
	}

	public void setInstantaneousWindDirection(QCElement<Integer> instantaneousWindDirection) {
		this.instantaneousWindDirection = instantaneousWindDirection;
	}

	public QCElement<Float> getInstantaneousWindSpeed() {
		return instantaneousWindSpeed;
	}

	public void setInstantaneousWindSpeed(QCElement<Float> instantaneousWindSpeed) {
		this.instantaneousWindSpeed = instantaneousWindSpeed;
	}

	public QCElement<Integer> getDirectionOfExtremWindSpeed() {
		return directionOfExtremWindSpeed;
	}

	public void setDirectionOfExtremWindSpeed(QCElement<Integer> directionOfExtremWindSpeed) {
		this.directionOfExtremWindSpeed = directionOfExtremWindSpeed;
	}

	public QCElement<Float> getExtremWindSpeed() {
		return extremWindSpeed;
	}

	public void setExtremWindSpeed(QCElement<Float> extremWindSpeed) {
		this.extremWindSpeed = extremWindSpeed;
	}

	public QCElement<Integer> getTimeOfExtremWindSpeed() {
		return timeOfExtremWindSpeed;
	}

	public void setTimeOfExtremWindSpeed(QCElement<Integer> timeOfExtremWindSpeed) {
		this.timeOfExtremWindSpeed = timeOfExtremWindSpeed;
	}

	public QCElement<Integer> getDirectionOfExtremWindSpeedMinutely() {
		return directionOfExtremWindSpeedMinutely;
	}

	public void setDirectionOfExtremWindSpeedMinutely(QCElement<Integer> directionOfExtremWindSpeedMinutely) {
		this.directionOfExtremWindSpeedMinutely = directionOfExtremWindSpeedMinutely;
	}

	public QCElement<Float> getExtremeWindSpeedMinutely() {
		return extremeWindSpeedMinutely;
	}

	public void setExtremeWindSpeedMinutely(QCElement<Float> extremeWindSpeedMinutely) {
		this.extremeWindSpeedMinutely = extremeWindSpeedMinutely;
	}

	public QCElement<Float> getRoadSurfTemperature() {
		return roadSurfTemperature;
	}

	public void setRoadSurfTemperature(QCElement<Float> roadSurfTemperature) {
		this.roadSurfTemperature = roadSurfTemperature;
	}

	public QCElement<Float> getMaxRoadSurfTemperature() {
		return maxRoadSurfTemperature;
	}

	public void setMaxRoadSurfTemperature(QCElement<Float> maxRoadSurfTemperature) {
		this.maxRoadSurfTemperature = maxRoadSurfTemperature;
	}

	public QCElement<Integer> getTimeOfMaxRoadSurfTemperature() {
		return timeOfMaxRoadSurfTemperature;
	}

	public void setTimeOfMaxRoadSurfTemperature(QCElement<Integer> timeOfMaxRoadSurfTemperature) {
		this.timeOfMaxRoadSurfTemperature = timeOfMaxRoadSurfTemperature;
	}

	public QCElement<Float> getMinRoadSurfTemperature() {
		return minRoadSurfTemperature;
	}

	public void setMinRoadSurfTemperature(QCElement<Float> minRoadSurfTemperature) {
		this.minRoadSurfTemperature = minRoadSurfTemperature;
	}

	public QCElement<Integer> getTimeOfMinRoadSurfTemperature() {
		return timeOfMinRoadSurfTemperature;
	}

	public void setTimeOfMinRoadSurfTemperature(QCElement<Integer> timeOfMinRoadSurfTemperature) {
		this.timeOfMinRoadSurfTemperature = timeOfMinRoadSurfTemperature;
	}

	public QCElement<Float> getRoadBaseTemperature() {
		return roadBaseTemperature;
	}

	public void setRoadBaseTemperature(QCElement<Float> roadBaseTemperature) {
		this.roadBaseTemperature = roadBaseTemperature;
	}

	public QCElement<Integer> getOneMinAvgVisibility() {
		return oneMinAvgVisibility;
	}

	public void setOneMinAvgVisibility(QCElement<Integer> oneMinAvgVisibility) {
		this.oneMinAvgVisibility = oneMinAvgVisibility;
	}

	public QCElement<Integer> getMinVisibility() {
		return minVisibility;
	}

	public void setMinVisibility(QCElement<Integer> minVisibility) {
		this.minVisibility = minVisibility;
	}

	public QCElement<Integer> getTimeOfMinVisibility() {
		return timeOfMinVisibility;
	}

	public void setTimeOfMinVisibility(QCElement<Integer> timeOfMinVisibility) {
		this.timeOfMinVisibility = timeOfMinVisibility;
	}

	public List<QCElement<Integer>> getRoadSurfCondition() {
		return roadSurfCondition;
	}

	public void setRoadSurfCondition(List<QCElement<Integer>> roadSurfCondition) {
		this.roadSurfCondition = roadSurfCondition;
	}

	public QCElement<Float> getSnowThickness() {
		return snowThickness;
	}

	public void setSnowThickness(QCElement<Float> snowThickness) {
		this.snowThickness = snowThickness;
	}

	public QCElement<Float> getWaterThickness() {
		return waterThickness;
	}

	public void setWaterThickness(QCElement<Float> waterThickness) {
		this.waterThickness = waterThickness;
	}

	public QCElement<Float> getIceThickness() {
		return iceThickness;
	}

	public void setIceThickness(QCElement<Float> iceThickness) {
		this.iceThickness = iceThickness;
	}

	public QCElement<Float> getFreezingPointTemperature() {
		return freezingPointTemperature;
	}

	public void setFreezingPointTemperature(QCElement<Float> freezingPointTemperature) {
		this.freezingPointTemperature = freezingPointTemperature;
	}

	public QCElement<Float> getConcentrationOfSnowMeltAgent() {
		return concentrationOfSnowMeltAgent;
	}

	public void setConcentrationOfSnowMeltAgent(QCElement<Float> concentrationOfSnowMeltAgent) {
		this.concentrationOfSnowMeltAgent = concentrationOfSnowMeltAgent;
	}

	public List<QCElement<Integer>> getWeatherPhenomenonCode() {
		return weatherPhenomenonCode;
	}

	public void setWeatherPhenomenonCode(List<QCElement<Integer>> weatherPhenomenonCode) {
		this.weatherPhenomenonCode = weatherPhenomenonCode;
	}

	public List<QCElement<Float>> getMinutelyPrecipitation() {
		return minutelyPrecipitation;
	}

	public void setMinutelyPrecipitation(List<QCElement<Float>> minutelyPrecipitation) {
		this.minutelyPrecipitation = minutelyPrecipitation;
	}
	
}
