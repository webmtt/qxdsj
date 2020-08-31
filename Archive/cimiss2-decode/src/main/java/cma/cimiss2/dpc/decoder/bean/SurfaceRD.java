package cma.cimiss2.dpc.decoder.bean;

import java.util.Date;
import java.util.List;

// TODO: Auto-generated Javadoc
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
	
	/** 5 路温数据. */
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

	/**
	 * Gets the station number china.
	 *
	 * @return the station number china
	 */
	public String getStationNumberChina() {
		return stationNumberChina;
	}

	/**
	 * Sets the station number china.
	 *
	 * @param stationNumberChina the new station number china
	 */
	public void setStationNumberChina(String stationNumberChina) {
		this.stationNumberChina = stationNumberChina;
	}

	/**
	 * Gets the observation time.
	 *
	 * @return the observation time
	 */
	public Date getObservationTime() {
		return observationTime;
	}

	/**
	 * Sets the observation time.
	 *
	 * @param observationTime the new observation time
	 */
	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}

	/**
	 * Gets the latitude.
	 *
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * Sets the latitude.
	 *
	 * @param latitude the new latitude
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * Gets the longitude.
	 *
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * Sets the longitude.
	 *
	 * @param longitude the new longitude
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * Gets the height of sation ground above mean sea level.
	 *
	 * @return the height of sation ground above mean sea level
	 */
	public double getHeightOfSationGroundAboveMeanSeaLevel() {
		return heightOfSationGroundAboveMeanSeaLevel;
	}

	/**
	 * Sets the height of sation ground above mean sea level.
	 *
	 * @param heightOfSationGroundAboveMeanSeaLevel the new height of sation ground above mean sea level
	 */
	public void setHeightOfSationGroundAboveMeanSeaLevel(double heightOfSationGroundAboveMeanSeaLevel) {
		this.heightOfSationGroundAboveMeanSeaLevel = heightOfSationGroundAboveMeanSeaLevel;
	}

	/**
	 * Gets the quality control.
	 *
	 * @return the quality control
	 */
	public String getQualityControl() {
		return qualityControl;
	}

	/**
	 * Sets the quality control.
	 *
	 * @param qualityControl the new quality control
	 */
	public void setQualityControl(String qualityControl) {
		this.qualityControl = qualityControl;
	}

	/**
	 * Gets the file revision sign.
	 *
	 * @return the file revision sign
	 */
	public String getFileRevisionSign() {
		return fileRevisionSign;
	}

	/**
	 * Sets the file revision sign.
	 *
	 * @param fileRevisionSign the new file revision sign
	 */
	public void setFileRevisionSign(String fileRevisionSign) {
		this.fileRevisionSign = fileRevisionSign;
	}

	/**
	 * Gets the air temperature.
	 *
	 * @return the air temperature
	 */
	public QCElement<Float> getAirTemperature() {
		return airTemperature;
	}

	/**
	 * Sets the air temperature.
	 *
	 * @param airTemperature the new air temperature
	 */
	public void setAirTemperature(QCElement<Float> airTemperature) {
		this.airTemperature = airTemperature;
	}

	/**
	 * Gets the max air temperature.
	 *
	 * @return the max air temperature
	 */
	public QCElement<Float> getMaxAirTemperature() {
		return maxAirTemperature;
	}

	/**
	 * Sets the max air temperature.
	 *
	 * @param maxAirTemperature the new max air temperature
	 */
	public void setMaxAirTemperature(QCElement<Float> maxAirTemperature) {
		this.maxAirTemperature = maxAirTemperature;
	}

	/**
	 * Gets the time of max air temperature.
	 *
	 * @return the time of max air temperature
	 */
	public QCElement<Integer> getTimeOfMaxAirTemperature() {
		return timeOfMaxAirTemperature;
	}

	/**
	 * Sets the time of max air temperature.
	 *
	 * @param timeOfMaxAirTemperature the new time of max air temperature
	 */
	public void setTimeOfMaxAirTemperature(QCElement<Integer> timeOfMaxAirTemperature) {
		this.timeOfMaxAirTemperature = timeOfMaxAirTemperature;
	}

	/**
	 * Gets the min air temperature.
	 *
	 * @return the min air temperature
	 */
	public QCElement<Float> getMinAirTemperature() {
		return minAirTemperature;
	}

	/**
	 * Sets the min air temperature.
	 *
	 * @param minAirTemperature the new min air temperature
	 */
	public void setMinAirTemperature(QCElement<Float> minAirTemperature) {
		this.minAirTemperature = minAirTemperature;
	}

	/**
	 * Gets the time of min air temperature.
	 *
	 * @return the time of min air temperature
	 */
	public QCElement<Integer> getTimeOfMinAirTemperature() {
		return timeOfMinAirTemperature;
	}

	/**
	 * Sets the time of min air temperature.
	 *
	 * @param timeOfMinAirTemperature the new time of min air temperature
	 */
	public void setTimeOfMinAirTemperature(QCElement<Integer> timeOfMinAirTemperature) {
		this.timeOfMinAirTemperature = timeOfMinAirTemperature;
	}

	/**
	 * Gets the dewpoint temperature.
	 *
	 * @return the dewpoint temperature
	 */
	public QCElement<Float> getDewpointTemperature() {
		return dewpointTemperature;
	}

	/**
	 * Sets the dewpoint temperature.
	 *
	 * @param dewpointTemperature the new dewpoint temperature
	 */
	public void setDewpointTemperature(QCElement<Float> dewpointTemperature) {
		this.dewpointTemperature = dewpointTemperature;
	}

	/**
	 * Gets the relative humidity.
	 *
	 * @return the relative humidity
	 */
	public QCElement<Integer> getRelativeHumidity() {
		return relativeHumidity;
	}

	/**
	 * Sets the relative humidity.
	 *
	 * @param relativeHumidity the new relative humidity
	 */
	public void setRelativeHumidity(QCElement<Integer> relativeHumidity) {
		this.relativeHumidity = relativeHumidity;
	}

	/**
	 * Gets the min relative humidity.
	 *
	 * @return the min relative humidity
	 */
	public QCElement<Integer> getMinRelativeHumidity() {
		return minRelativeHumidity;
	}

	/**
	 * Sets the min relative humidity.
	 *
	 * @param minRelativeHumidity the new min relative humidity
	 */
	public void setMinRelativeHumidity(QCElement<Integer> minRelativeHumidity) {
		this.minRelativeHumidity = minRelativeHumidity;
	}

	/**
	 * Gets the time of min relative humidity.
	 *
	 * @return the time of min relative humidity
	 */
	public QCElement<Integer> getTimeOfMinRelativeHumidity() {
		return timeOfMinRelativeHumidity;
	}

	/**
	 * Sets the time of min relative humidity.
	 *
	 * @param timeOfMinRelativeHumidity the new time of min relative humidity
	 */
	public void setTimeOfMinRelativeHumidity(QCElement<Integer> timeOfMinRelativeHumidity) {
		this.timeOfMinRelativeHumidity = timeOfMinRelativeHumidity;
	}

	/**
	 * Gets the water vapor pressure.
	 *
	 * @return the water vapor pressure
	 */
	public QCElement<Float> getWaterVaporPressure() {
		return waterVaporPressure;
	}

	/**
	 * Sets the water vapor pressure.
	 *
	 * @param waterVaporPressure the new water vapor pressure
	 */
	public void setWaterVaporPressure(QCElement<Float> waterVaporPressure) {
		this.waterVaporPressure = waterVaporPressure;
	}

	/**
	 * Gets the hourly cumulative precipitation.
	 *
	 * @return the hourly cumulative precipitation
	 */
	public QCElement<Float> getHourlyCumulativePrecipitation() {
		return hourlyCumulativePrecipitation;
	}

	/**
	 * Sets the hourly cumulative precipitation.
	 *
	 * @param hourlyCumulativePrecipitation the new hourly cumulative precipitation
	 */
	public void setHourlyCumulativePrecipitation(QCElement<Float> hourlyCumulativePrecipitation) {
		this.hourlyCumulativePrecipitation = hourlyCumulativePrecipitation;
	}

	/**
	 * Gets the two min wind direction.
	 *
	 * @return the two min wind direction
	 */
	public QCElement<Integer> getTwoMinWindDirection() {
		return twoMinWindDirection;
	}

	/**
	 * Sets the two min wind direction.
	 *
	 * @param twoMinWindDirection the new two min wind direction
	 */
	public void setTwoMinWindDirection(QCElement<Integer> twoMinWindDirection) {
		this.twoMinWindDirection = twoMinWindDirection;
	}

	/**
	 * Gets the two min wind avg speed.
	 *
	 * @return the two min wind avg speed
	 */
	public QCElement<Float> getTwoMinWindAvgSpeed() {
		return twoMinWindAvgSpeed;
	}

	/**
	 * Sets the two min wind avg speed.
	 *
	 * @param twoMinWindAvgSpeed the new two min wind avg speed
	 */
	public void setTwoMinWindAvgSpeed(QCElement<Float> twoMinWindAvgSpeed) {
		this.twoMinWindAvgSpeed = twoMinWindAvgSpeed;
	}

	/**
	 * Gets the ten min wind direction.
	 *
	 * @return the ten min wind direction
	 */
	public QCElement<Integer> getTenMinWindDirection() {
		return tenMinWindDirection;
	}

	/**
	 * Sets the ten min wind direction.
	 *
	 * @param tenMinWindDirection the new ten min wind direction
	 */
	public void setTenMinWindDirection(QCElement<Integer> tenMinWindDirection) {
		this.tenMinWindDirection = tenMinWindDirection;
	}

	/**
	 * Gets the ten min wind avg speed.
	 *
	 * @return the ten min wind avg speed
	 */
	public QCElement<Float> getTenMinWindAvgSpeed() {
		return tenMinWindAvgSpeed;
	}

	/**
	 * Sets the ten min wind avg speed.
	 *
	 * @param tenMinWindAvgSpeed the new ten min wind avg speed
	 */
	public void setTenMinWindAvgSpeed(QCElement<Float> tenMinWindAvgSpeed) {
		this.tenMinWindAvgSpeed = tenMinWindAvgSpeed;
	}

	/**
	 * Gets the direction of max wind speed.
	 *
	 * @return the direction of max wind speed
	 */
	public QCElement<Integer> getDirectionOfMaxWindSpeed() {
		return directionOfMaxWindSpeed;
	}

	/**
	 * Sets the direction of max wind speed.
	 *
	 * @param directionOfMaxWindSpeed the new direction of max wind speed
	 */
	public void setDirectionOfMaxWindSpeed(QCElement<Integer> directionOfMaxWindSpeed) {
		this.directionOfMaxWindSpeed = directionOfMaxWindSpeed;
	}

	/**
	 * Gets the max wind speed.
	 *
	 * @return the max wind speed
	 */
	public QCElement<Float> getMaxWindSpeed() {
		return maxWindSpeed;
	}

	/**
	 * Sets the max wind speed.
	 *
	 * @param maxWindSpeed the new max wind speed
	 */
	public void setMaxWindSpeed(QCElement<Float> maxWindSpeed) {
		this.maxWindSpeed = maxWindSpeed;
	}

	/**
	 * Gets the time of max wind speed.
	 *
	 * @return the time of max wind speed
	 */
	public QCElement<Integer> getTimeOfMaxWindSpeed() {
		return timeOfMaxWindSpeed;
	}

	/**
	 * Sets the time of max wind speed.
	 *
	 * @param timeOfMaxWindSpeed the new time of max wind speed
	 */
	public void setTimeOfMaxWindSpeed(QCElement<Integer> timeOfMaxWindSpeed) {
		this.timeOfMaxWindSpeed = timeOfMaxWindSpeed;
	}

	/**
	 * Gets the instantaneous wind direction.
	 *
	 * @return the instantaneous wind direction
	 */
	public QCElement<Integer> getInstantaneousWindDirection() {
		return instantaneousWindDirection;
	}

	/**
	 * Sets the instantaneous wind direction.
	 *
	 * @param instantaneousWindDirection the new instantaneous wind direction
	 */
	public void setInstantaneousWindDirection(QCElement<Integer> instantaneousWindDirection) {
		this.instantaneousWindDirection = instantaneousWindDirection;
	}

	/**
	 * Gets the instantaneous wind speed.
	 *
	 * @return the instantaneous wind speed
	 */
	public QCElement<Float> getInstantaneousWindSpeed() {
		return instantaneousWindSpeed;
	}

	/**
	 * Sets the instantaneous wind speed.
	 *
	 * @param instantaneousWindSpeed the new instantaneous wind speed
	 */
	public void setInstantaneousWindSpeed(QCElement<Float> instantaneousWindSpeed) {
		this.instantaneousWindSpeed = instantaneousWindSpeed;
	}

	/**
	 * Gets the direction of extrem wind speed.
	 *
	 * @return the direction of extrem wind speed
	 */
	public QCElement<Integer> getDirectionOfExtremWindSpeed() {
		return directionOfExtremWindSpeed;
	}

	/**
	 * Sets the direction of extrem wind speed.
	 *
	 * @param directionOfExtremWindSpeed the new direction of extrem wind speed
	 */
	public void setDirectionOfExtremWindSpeed(QCElement<Integer> directionOfExtremWindSpeed) {
		this.directionOfExtremWindSpeed = directionOfExtremWindSpeed;
	}

	/**
	 * Gets the extrem wind speed.
	 *
	 * @return the extrem wind speed
	 */
	public QCElement<Float> getExtremWindSpeed() {
		return extremWindSpeed;
	}

	/**
	 * Sets the extrem wind speed.
	 *
	 * @param extremWindSpeed the new extrem wind speed
	 */
	public void setExtremWindSpeed(QCElement<Float> extremWindSpeed) {
		this.extremWindSpeed = extremWindSpeed;
	}

	/**
	 * Gets the time of extrem wind speed.
	 *
	 * @return the time of extrem wind speed
	 */
	public QCElement<Integer> getTimeOfExtremWindSpeed() {
		return timeOfExtremWindSpeed;
	}

	/**
	 * Sets the time of extrem wind speed.
	 *
	 * @param timeOfExtremWindSpeed the new time of extrem wind speed
	 */
	public void setTimeOfExtremWindSpeed(QCElement<Integer> timeOfExtremWindSpeed) {
		this.timeOfExtremWindSpeed = timeOfExtremWindSpeed;
	}

	/**
	 * Gets the direction of extrem wind speed minutely.
	 *
	 * @return the direction of extrem wind speed minutely
	 */
	public QCElement<Integer> getDirectionOfExtremWindSpeedMinutely() {
		return directionOfExtremWindSpeedMinutely;
	}

	/**
	 * Sets the direction of extrem wind speed minutely.
	 *
	 * @param directionOfExtremWindSpeedMinutely the new direction of extrem wind speed minutely
	 */
	public void setDirectionOfExtremWindSpeedMinutely(QCElement<Integer> directionOfExtremWindSpeedMinutely) {
		this.directionOfExtremWindSpeedMinutely = directionOfExtremWindSpeedMinutely;
	}

	/**
	 * Gets the extreme wind speed minutely.
	 *
	 * @return the extreme wind speed minutely
	 */
	public QCElement<Float> getExtremeWindSpeedMinutely() {
		return extremeWindSpeedMinutely;
	}

	/**
	 * Sets the extreme wind speed minutely.
	 *
	 * @param extremeWindSpeedMinutely the new extreme wind speed minutely
	 */
	public void setExtremeWindSpeedMinutely(QCElement<Float> extremeWindSpeedMinutely) {
		this.extremeWindSpeedMinutely = extremeWindSpeedMinutely;
	}

	/**
	 * Gets the road surf temperature.
	 *
	 * @return the road surf temperature
	 */
	public QCElement<Float> getRoadSurfTemperature() {
		return roadSurfTemperature;
	}

	/**
	 * Sets the road surf temperature.
	 *
	 * @param roadSurfTemperature the new road surf temperature
	 */
	public void setRoadSurfTemperature(QCElement<Float> roadSurfTemperature) {
		this.roadSurfTemperature = roadSurfTemperature;
	}

	/**
	 * Gets the max road surf temperature.
	 *
	 * @return the max road surf temperature
	 */
	public QCElement<Float> getMaxRoadSurfTemperature() {
		return maxRoadSurfTemperature;
	}

	/**
	 * Sets the max road surf temperature.
	 *
	 * @param maxRoadSurfTemperature the new max road surf temperature
	 */
	public void setMaxRoadSurfTemperature(QCElement<Float> maxRoadSurfTemperature) {
		this.maxRoadSurfTemperature = maxRoadSurfTemperature;
	}

	/**
	 * Gets the time of max road surf temperature.
	 *
	 * @return the time of max road surf temperature
	 */
	public QCElement<Integer> getTimeOfMaxRoadSurfTemperature() {
		return timeOfMaxRoadSurfTemperature;
	}

	/**
	 * Sets the time of max road surf temperature.
	 *
	 * @param timeOfMaxRoadSurfTemperature the new time of max road surf temperature
	 */
	public void setTimeOfMaxRoadSurfTemperature(QCElement<Integer> timeOfMaxRoadSurfTemperature) {
		this.timeOfMaxRoadSurfTemperature = timeOfMaxRoadSurfTemperature;
	}

	/**
	 * Gets the min road surf temperature.
	 *
	 * @return the min road surf temperature
	 */
	public QCElement<Float> getMinRoadSurfTemperature() {
		return minRoadSurfTemperature;
	}

	/**
	 * Sets the min road surf temperature.
	 *
	 * @param minRoadSurfTemperature the new min road surf temperature
	 */
	public void setMinRoadSurfTemperature(QCElement<Float> minRoadSurfTemperature) {
		this.minRoadSurfTemperature = minRoadSurfTemperature;
	}

	/**
	 * Gets the time of min road surf temperature.
	 *
	 * @return the time of min road surf temperature
	 */
	public QCElement<Integer> getTimeOfMinRoadSurfTemperature() {
		return timeOfMinRoadSurfTemperature;
	}

	/**
	 * Sets the time of min road surf temperature.
	 *
	 * @param timeOfMinRoadSurfTemperature the new time of min road surf temperature
	 */
	public void setTimeOfMinRoadSurfTemperature(QCElement<Integer> timeOfMinRoadSurfTemperature) {
		this.timeOfMinRoadSurfTemperature = timeOfMinRoadSurfTemperature;
	}

	/**
	 * Gets the road base temperature.
	 *
	 * @return the road base temperature
	 */
	public QCElement<Float> getRoadBaseTemperature() {
		return roadBaseTemperature;
	}

	/**
	 * Sets the road base temperature.
	 *
	 * @param roadBaseTemperature the new road base temperature
	 */
	public void setRoadBaseTemperature(QCElement<Float> roadBaseTemperature) {
		this.roadBaseTemperature = roadBaseTemperature;
	}

	/**
	 * Gets the one min avg visibility.
	 *
	 * @return the one min avg visibility
	 */
	public QCElement<Integer> getOneMinAvgVisibility() {
		return oneMinAvgVisibility;
	}

	/**
	 * Sets the one min avg visibility.
	 *
	 * @param oneMinAvgVisibility the new one min avg visibility
	 */
	public void setOneMinAvgVisibility(QCElement<Integer> oneMinAvgVisibility) {
		this.oneMinAvgVisibility = oneMinAvgVisibility;
	}

	/**
	 * Gets the min visibility.
	 *
	 * @return the min visibility
	 */
	public QCElement<Integer> getMinVisibility() {
		return minVisibility;
	}

	/**
	 * Sets the min visibility.
	 *
	 * @param minVisibility the new min visibility
	 */
	public void setMinVisibility(QCElement<Integer> minVisibility) {
		this.minVisibility = minVisibility;
	}

	/**
	 * Gets the time of min visibility.
	 *
	 * @return the time of min visibility
	 */
	public QCElement<Integer> getTimeOfMinVisibility() {
		return timeOfMinVisibility;
	}

	/**
	 * Sets the time of min visibility.
	 *
	 * @param timeOfMinVisibility the new time of min visibility
	 */
	public void setTimeOfMinVisibility(QCElement<Integer> timeOfMinVisibility) {
		this.timeOfMinVisibility = timeOfMinVisibility;
	}

	/**
	 * Gets the road surf condition.
	 *
	 * @return the road surf condition
	 */
	public List<QCElement<Integer>> getRoadSurfCondition() {
		return roadSurfCondition;
	}

	/**
	 * Sets the road surf condition.
	 *
	 * @param roadSurfCondition the new road surf condition
	 */
	public void setRoadSurfCondition(List<QCElement<Integer>> roadSurfCondition) {
		this.roadSurfCondition = roadSurfCondition;
	}

	/**
	 * Gets the snow thickness.
	 *
	 * @return the snow thickness
	 */
	public QCElement<Float> getSnowThickness() {
		return snowThickness;
	}

	/**
	 * Sets the snow thickness.
	 *
	 * @param snowThickness the new snow thickness
	 */
	public void setSnowThickness(QCElement<Float> snowThickness) {
		this.snowThickness = snowThickness;
	}

	/**
	 * Gets the water thickness.
	 *
	 * @return the water thickness
	 */
	public QCElement<Float> getWaterThickness() {
		return waterThickness;
	}

	/**
	 * Sets the water thickness.
	 *
	 * @param waterThickness the new water thickness
	 */
	public void setWaterThickness(QCElement<Float> waterThickness) {
		this.waterThickness = waterThickness;
	}

	/**
	 * Gets the ice thickness.
	 *
	 * @return the ice thickness
	 */
	public QCElement<Float> getIceThickness() {
		return iceThickness;
	}

	/**
	 * Sets the ice thickness.
	 *
	 * @param iceThickness the new ice thickness
	 */
	public void setIceThickness(QCElement<Float> iceThickness) {
		this.iceThickness = iceThickness;
	}

	/**
	 * Gets the freezing point temperature.
	 *
	 * @return the freezing point temperature
	 */
	public QCElement<Float> getFreezingPointTemperature() {
		return freezingPointTemperature;
	}

	/**
	 * Sets the freezing point temperature.
	 *
	 * @param freezingPointTemperature the new freezing point temperature
	 */
	public void setFreezingPointTemperature(QCElement<Float> freezingPointTemperature) {
		this.freezingPointTemperature = freezingPointTemperature;
	}

	/**
	 * Gets the concentration of snow melt agent.
	 *
	 * @return the concentration of snow melt agent
	 */
	public QCElement<Float> getConcentrationOfSnowMeltAgent() {
		return concentrationOfSnowMeltAgent;
	}

	/**
	 * Sets the concentration of snow melt agent.
	 *
	 * @param concentrationOfSnowMeltAgent the new concentration of snow melt agent
	 */
	public void setConcentrationOfSnowMeltAgent(QCElement<Float> concentrationOfSnowMeltAgent) {
		this.concentrationOfSnowMeltAgent = concentrationOfSnowMeltAgent;
	}

	/**
	 * Gets the weather phenomenon code.
	 *
	 * @return the weather phenomenon code
	 */
	public List<QCElement<Integer>> getWeatherPhenomenonCode() {
		return weatherPhenomenonCode;
	}

	/**
	 * Sets the weather phenomenon code.
	 *
	 * @param weatherPhenomenonCode the new weather phenomenon code
	 */
	public void setWeatherPhenomenonCode(List<QCElement<Integer>> weatherPhenomenonCode) {
		this.weatherPhenomenonCode = weatherPhenomenonCode;
	}

	/**
	 * Gets the minutely precipitation.
	 *
	 * @return the minutely precipitation
	 */
	public List<QCElement<Float>> getMinutelyPrecipitation() {
		return minutelyPrecipitation;
	}

	/**
	 * Sets the minutely precipitation.
	 *
	 * @param minutelyPrecipitation the new minutely precipitation
	 */
	public void setMinutelyPrecipitation(List<QCElement<Float>> minutelyPrecipitation) {
		this.minutelyPrecipitation = minutelyPrecipitation;
	}
	
}
