package cma.cimiss2.dpc.decoder.bean.radi;

import java.util.Date;

import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>

 *分钟基准辐射实体类
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《基准辐射资料处理原则》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月28日 上午11:40:26   maxiyue    Initial creation.
 * </pre>
 * 
 * @author maxiyue
 * @version 0.0.1
 */
public class MinutesReferenceRadiationData extends AgmeReportHeader{
	/**
	 * NO: 1  <br>
	 * nameCN: 区站号 <br>
	 * unit: 无  <br>
	 * BUFR FXY: V01301 <br>
	 * descriptionCN: 5位数字或第1位为字母，第2-5位为数字<br>
	 * decode rule: 直接取值<br>
     * field rule: 直接赋值 <br>
	 */
	private String stationNumberChina ;
	/**
	 * NO:2  <br>
	 * nameCN: 纬度  <br>
	 * unit: 度 <br>
	 * BUFR FXY: V05001 <br>
	 * descriptionCN: <br>
	 * decode rule:纬度转换成度分秒的形式，前两位度不变，分除以60，秒除以3600<br>
     * field rule:直接赋值
	 */
	private Double latitude ;
	/**
	 * NO:3  <br>
	 * nameCN: 经度  <br>
	 * unit: 度 <br>
	 * BUFR FXY: V06001 <br>
	 * descriptionCN: <br>
	 * decode rule:经度转换成度分秒的形式，前三位度不变，分除以60，秒除以3600<br>
     * field rule:直接赋值
	 */
	private Double longitude ;
	/**
	 * NO:4 <br>
	 * nameCN: 测站高度   <br>
	 * unit:  米<br>
	 * BUFR FXY:V07001 <br>
	 * descriptionCN: <br>
	 * decode rule:取值除以10<br>
     * field rule:直接赋值
	 */
	private Double heightOfSationGroundAboveMeanSeaLevel;
	/**
	 * NO: 5 <br>
	 * nameCN: 观测时间  <br>
	 * unit: <br>
	 * BUFR FXY: V04001\V04002\V04003\V04004 <br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:使用java时间转化工具类进行转化,标准形式为：yyyyMMddHHmmss
	 */
	private Date observationTime;
	/**
	 * NO:6 <br>
	 * nameCN: 太阳直接辐射辐射表距地高度 <br>
	 * unit: 米 <br>
	 * BUFR FXY:V07032_1 <br>
	 * descriptionCN: <br>
	 * decode rule:取值除以10<br>
     * field rule:直接赋值
	 */
	private Double DRA_Sensor_Heigh;
	/**
	 * NO:7 <br>
	 * nameCN: 散射辐射辐射表距地高度 <br>
	 * unit: 米 <br>
	 * BUFR FXY:V07032_2 <br>
	 * descriptionCN: <br>
	 * decode rule:取值除以10<br>
     * field rule:直接赋值
	 */
	private Double SRA_Sensor_Heigh;
	/**
	 * NO:8 <br>
	 * nameCN: 总辐射辐射表距地高度 <br>
	 * unit:  米<br>
	 * BUFR FXY:V07032_3 <br>
	 * descriptionCN: <br>
	 * decode rule:取值除以10<br>
     * field rule:直接赋值
	 */
	private Double QRA_Sensor_Heigh;
	/**
	 * NO:9 <br>
	 * nameCN: 反射辐射辐射表距地高度 <br>
	 * unit: 米 <br>
	 * BUFR FXY:V07032_4 <br>
	 * descriptionCN: <br>
	 * decode rule:取值除以10<br>
     * field rule:直接赋值
	 */
	private Double RRA_Sensor_Heigh;
	/**
	 * NO:10 <br>
	 * nameCN: 大气长波辐射辐射表距地高度<br>
	 * unit:  米<br>
	 * BUFR FXY:V07032_5 <br>
	 * descriptionCN: <br>
	 * decode rule:取值除以10<br>
     * field rule:直接赋值
	 */
	private Double LR_Atmo_Sensor_Heigh;
	/**
	 * NO:11 <br>
	 * nameCN: 地球长波辐射辐射表距地高度<br>
	 * unit: 米 <br>
	 * BUFR FXY:V07032_6 <br>
	 * descriptionCN: <br>
	 * decode rule:取值除以10<br>
     * field rule:直接赋值
	 */
	private Double LR_Earth_Sensor_Heigh;
	/**
	 * NO:12 <br>
	 * nameCN: 紫外辐射辐射表距地高度<br>
	 * unit:  米<br>
	 * BUFR FXY:V07032_7 <br>
	 * descriptionCN: <br>
	 * decode rule:取值除以10<br>
     * field rule:直接赋值
	 */
	private Double UV_Sensor_Heigh;
	/**
	 * NO:13 <br>
	 * nameCN: 光合有效辐射辐射表距地高度<br>
	 * unit: 米 <br>
	 * BUFR FXY:V07032_8 <br>
	 * descriptionCN: <br>
	 * decode rule:取值除以10<br>
     * field rule:直接赋值
	 */
	private Double PAR_Sensor_Heigh;
	/**
	 * NO:14 <br>
	 * nameCN: 太阳直接辐射辐照度1分钟平均值<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14313_701_01 <br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double DRA_Avg_Mi;
	/**
	 * NO:15 <br>
	 * nameCN: 太阳直接辐射辐照度1分钟最小值<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14313_06_01 <br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double DRA_Min_Mi;
	/**
	 * NO:16 <br>
	 * nameCN: 太阳直接辐射辐照度1分钟最大值<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14313_05_01 <br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double DRA_Max_Mi;
	/**
	 * NO:17 <br>
	 * nameCN: 太阳直接辐射辐照度1分钟标准差<br>
	 * unit:  W/m2<br>
	 * BUFR FXY:V14313_04_01 <br>
	 * descriptionCN: <br>
	 * decode rule:取值除以100<br>
     * field rule:直接赋值
	 */
	private Double DRA_Mi_SD;
	/**
	 * NO:18 <br>
	 * nameCN: 散射辐射辐照度1分钟平均值<br>
	 * unit:  <br>
	 * BUFR FXY:V14314_701_01 <br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double SRA_Avg_Mi;
	/**
	 * NO:19<br>
	 * nameCN: 散射辐射辐照度1分钟最小值<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14314_06_01<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double SRA_Min_Mi;
	/**
	 * NO:20<br>
	 * nameCN: 散射辐射辐照度1分钟最大值<br>
	 * unit:W/m2  <br>
	 * BUFR FXY:V14314_05_01<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double SRA_Max_Mi;
	/**
	 * NO:21<br>
	 * nameCN: 散射辐射辐照度1分钟标准差<br>
	 * unit:  W/m2<br>
	 * BUFR FXY:V14314_04_01<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以100<br>
     * field rule:直接赋值
	 */
	private Double SRA_Mi_SD;
	/**
	 * NO:22<br>
	 * nameCN: 散射辐射辐射表1分钟平均通风速度<br>
	 * unit: m/s <br>
	 * BUFR FXY:V14402<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以10<br>
     * field rule:直接赋值
	 */
	private Double WIND_S_Avg_SRA_Mi;
	/**
	 * NO:23<br>
	 * nameCN:散射辐射辐射表1分钟平均辐射表体温度<br>
	 * unit:  ℃<br>
	 * BUFR FXY:V14412<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以10<br>
     * field rule:直接赋值
	 */
	private Double TEM_Avg_SRA_Mi;
	/**
	 * NO:24<br>
	 * nameCN:总辐射辐照度1分钟平均值<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14311_701_01<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double QRA_Avg_Mi;
	/**
	 * NO:25<br>
	 * nameCN:总辐射辐照度1分钟最小值<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14311_06_01<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double QRA_Min_Mi;
	/**
	 * NO:26<br>
	 * nameCN:总辐射辐照度1分钟最大值<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14311_05_01<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double QRA_Max_Min;
	/**
	 * NO:27<br>
	 * nameCN:总辐射辐照度1分钟标准差<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14311_04_01<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以100<br>
     * field rule:直接赋值
	 */
	private Double QRA_Mi_SD;
	/**
	 * NO:28<br>
	 * nameCN:总辐射辐射表1分钟平均通风速度<br>
	 * unit: m/s <br>
	 * BUFR FXY:V14401<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以10<br>
     * field rule:直接赋值
	 */
	private Double WIND_S_Avg_QRA_Mi;
	/**
	 * NO:29<br>
	 * nameCN:总辐射辐射表1分钟平均辐射表体温度<br>
	 * unit:℃  <br>
	 * BUFR FXY:V14411<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以10<br>
     * field rule:直接赋值
	 */
	private Double TEM_Avg_QRA_Mi;
	/**
	 * NO:30<br>
	 * nameCN:反射辐射辐照度1分钟平均值<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14315_701_01<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double RRA_Avg_Mi;
	/**
	 * NO:31<br>
	 * nameCN:反射辐射辐照度1分钟最小值<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14315_06_01<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double RRA_Min_Mi;
	/**
	 * NO:32<br>
	 * nameCN:反射辐射辐照度1分钟最大值<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14315_05_01<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double RRA_Max_Mi;
	/**
	 * NO:33<br>
	 * nameCN:反射辐射辐照度1分钟标准差<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14315_04_01<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以100<br>
     * field rule:直接赋值
	 */
	private Double RRA_Mi_SD;
	/**
	 * NO:34<br>
	 * nameCN:反射辐射辐射表1分钟平均通风速度<br>
	 * unit: m/s <br>
	 * BUFR FXY:V14403<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以10<br>
     * field rule:直接赋值
	 */
	private Double WIND_S_Avg_RRA_Mi;
	/**
	 * NO:35<br>
	 * nameCN:反射辐射辐射表1分钟平均辐射表体温度<br>
	 * unit: ℃ <br>
	 * BUFR FXY:V14413<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以10<br>
     * field rule:直接赋值
	 */
	private Double TEM_Avg_RRA_Mi;
	/**
	 * NO:36<br>
	 * nameCN:大气长波辐射辐照度1分钟平均值<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14318_701_01<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double ALR_Avg_Mi;
	/**
	 * NO:37<br>
	 * nameCN:大气长波辐射辐照度1分钟最小值<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14318_06_01<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double ALR_Min_Mi;
	/**
	 * NO:38<br>
	 * nameCN:大气长波辐射辐照度1分钟最大值<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14318_05_01<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double ALR_Max_Mi;
	/**
	 * NO:39<br>
	 * nameCN:大气长波辐射辐照度1分钟标准差<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14318_04_01<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以100<br>
     * field rule:直接赋值
	 */
	private Double ALR_Mi_SD;
	/**
	 * NO:40<br>
	 * nameCN:大气长波辐射辐射表1分钟平均通风速度<br>
	 * unit:  m/s<br>
	 * BUFR FXY:V14404<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以10<br>
     * field rule:直接赋值
	 */
	private Double WIND_S_Avg_ALR_Mi;
	/**
	 * NO:41<br>
	 * nameCN:大气长波辐射辐射表1分钟平均腔体温度<br>
	 * unit: ℃ <br>
	 * BUFR FXY:V14414<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以10<br>
     * field rule:直接赋值
	 */
	private Double TEM_Avg_ALR_Mi;
	/**
	 * NO:42<br>
	 * nameCN:地球长波辐射辐照度1分钟平均值<br>
	 * unit:  W/m2<br>
	 * BUFR FXY:V14319_701_01<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double ELR_Avg_Mi;
	/**
	 * NO:43<br>
	 * nameCN:地球长波辐射辐照度1分钟最小值<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14319_06_01<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double ELR_Min_Mi;
	/**
	 * NO:44<br>
	 * nameCN:地球长波辐射辐照度1分钟最大值<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14319_05_01<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double ELR_Max_Mi;
	/**
	 * NO:45<br>
	 * nameCN:地球长波辐射辐照度1分钟标准差<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14319_04_01<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以100<br>
     * field rule:直接赋值
	 */
	private Double ELR_Mi_SD;
	/**
	 * NO:46<br>
	 * nameCN:地球长波辐射辐射表1分钟平均通风速度<br>
	 * unit:  m/s<br>
	 * BUFR FXY:V14405<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以10<br>
     * field rule:直接赋值
	 */
	private Double WIND_S_Avg_ELR_Mi;
	/**
	 * NO:47<br>
	 * nameCN:地球长波辐射辐射表1分钟平均腔体温度<br>
	 * unit: ℃ <br>
	 * BUFR FXY:V14415<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以10<br>
     * field rule:直接赋值
	 */
	private Double TEM_Avg_ELR_Mi;
	/**
	 * NO:48<br>
	 * nameCN:紫外辐射（UVA）辐照度1分钟平均值<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14316_701_01<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以100<br>
     * field rule:直接赋值
	 */
	private Double UVA_Avg_Mi;
	/**
	 * NO:49<br>
	 * nameCN:紫外辐射（UVA）辐照度1分钟最小值<br>
	 * unit:  W/m2<br>
	 * BUFR FXY:V14316_06_01<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以100<br>
     * field rule:直接赋值
	 */
	private Double UVA_Min_Mi;
	/**
	 * NO:50<br>
	 * nameCN:紫外辐射（UVA）辐照度1分钟最大值<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14316_05_01<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以100<br>
     * field rule:直接赋值
	 */
	private Double UV_Max_Mi;
	/**
	 * NO:51<br>
	 * nameCN:紫外辐射（UVA）辐照度1分钟标准差<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14316_04_01<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以100<br>
     * field rule:直接赋值
	 */
	private Double UVA_Mi_SD;
	/**
	 * NO:52<br>
	 * nameCN:紫外辐射（UVB）辐照度1分钟平均值<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14317_701_01<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以100<br>
     * field rule:直接赋值
	 */
	private Double UVB_Avg_Mi;
	/**
	 * NO:53<br>
	 * nameCN:紫外辐射（UVB）辐照度1分钟最小值<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14317_06_01<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以100<br>
     * field rule:直接赋值
	 */
	private Double UVB_Min_Mi;
	/**
	 * NO:54<br>
	 * nameCN:紫外辐射（UVB）辐照度1分钟最大值<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14317_05_01<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以100<br>
     * field rule:直接赋值
	 */
	private Double UVB_Max_Mi;
	/**
	 * NO:55<br>
	 * nameCN:紫外辐射（UVB）辐照度1分钟标准差<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14317_04_01<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以100<br>
     * field rule:直接赋值
	 */
	private Double UVB_Mi_SD;
	/**
	 * NO:56<br>
	 * nameCN:紫外辐射辐射表恒温器1分钟平均温度<br>
	 * unit: ℃ <br>
	 * BUFR FXY:V14416<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以10<br>
     * field rule:直接赋值
	 */
	private Double TEM_Avg_UV_Mi;
	/**
	 * NO:57<br>
	 * nameCN:光合有效辐射辐照度1分钟平均值<br>
	 * unit:  W/m2<br>
	 * BUFR FXY:V14340_701_01<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double PAR_Avg_Mi;
	/**
	 * NO:58<br>
	 * nameCN:光合有效辐射辐照度1分钟最小值<br>
	 * unit:  W/m2<br>
	 * BUFR FXY:V14340_06_01<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double PAR_Min_Mi;
	/**
	 * NO:59<br>
	 * nameCN:光合有效辐射辐照度1分钟最大值<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14340_05_01<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double PAR_Max_Mi;
	/**
	 * NO:60<br>
	 * nameCN:光合有效辐射辐照度1分钟标准差<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14340_04_01<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以100<br>
     * field rule:直接赋值
	 */
	private Double PAR_Mi_SD;
	/**
	 * 标志时间是否是第24小时
	 */
	private int DATE_Flag=0;
	public int getDATE_Flag() {
		return DATE_Flag;
	}
	public void setDATE_Flag(int dATE_Flag) {
		DATE_Flag = dATE_Flag;
	}
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
	public Date getObservationTime() {
		return observationTime;
	}
	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}
	public Double getDRA_Sensor_Heigh() {
		return DRA_Sensor_Heigh;
	}
	public void setDRA_Sensor_Heigh(Double dRA_Sensor_Heigh) {
		DRA_Sensor_Heigh = dRA_Sensor_Heigh;
	}
	public Double getSRA_Sensor_Heigh() {
		return SRA_Sensor_Heigh;
	}
	public void setSRA_Sensor_Heigh(Double sRA_Sensor_Heigh) {
		SRA_Sensor_Heigh = sRA_Sensor_Heigh;
	}
	public Double getQRA_Sensor_Heigh() {
		return QRA_Sensor_Heigh;
	}
	public void setQRA_Sensor_Heigh(Double qRA_Sensor_Heigh) {
		QRA_Sensor_Heigh = qRA_Sensor_Heigh;
	}
	public Double getRRA_Sensor_Heigh() {
		return RRA_Sensor_Heigh;
	}
	public void setRRA_Sensor_Heigh(Double rRA_Sensor_Heigh) {
		RRA_Sensor_Heigh = rRA_Sensor_Heigh;
	}
	public Double getLR_Atmo_Sensor_Heigh() {
		return LR_Atmo_Sensor_Heigh;
	}
	public void setLR_Atmo_Sensor_Heigh(Double lR_Atmo_Sensor_Heigh) {
		LR_Atmo_Sensor_Heigh = lR_Atmo_Sensor_Heigh;
	}
	public Double getLR_Earth_Sensor_Heigh() {
		return LR_Earth_Sensor_Heigh;
	}
	public void setLR_Earth_Sensor_Heigh(Double lR_Earth_Sensor_Heigh) {
		LR_Earth_Sensor_Heigh = lR_Earth_Sensor_Heigh;
	}
	public Double getUV_Sensor_Heigh() {
		return UV_Sensor_Heigh;
	}
	public void setUV_Sensor_Heigh(Double uV_Sensor_Heigh) {
		UV_Sensor_Heigh = uV_Sensor_Heigh;
	}
	public Double getPAR_Sensor_Heigh() {
		return PAR_Sensor_Heigh;
	}
	public void setPAR_Sensor_Heigh(Double pAR_Sensor_Heigh) {
		PAR_Sensor_Heigh = pAR_Sensor_Heigh;
	}
	public Double getDRA_Avg_Mi() {
		return DRA_Avg_Mi;
	}
	public void setDRA_Avg_Mi(Double dRA_Avg_Mi) {
		DRA_Avg_Mi = dRA_Avg_Mi;
	}
	public Double getDRA_Min_Mi() {
		return DRA_Min_Mi;
	}
	public void setDRA_Min_Mi(Double dRA_Min_Mi) {
		DRA_Min_Mi = dRA_Min_Mi;
	}
	public Double getDRA_Max_Mi() {
		return DRA_Max_Mi;
	}
	public void setDRA_Max_Mi(Double dRA_Max_Mi) {
		DRA_Max_Mi = dRA_Max_Mi;
	}
	public Double getDRA_Mi_SD() {
		return DRA_Mi_SD;
	}
	public void setDRA_Mi_SD(Double dRA_Mi_SD) {
		DRA_Mi_SD = dRA_Mi_SD;
	}
	public Double getSRA_Avg_Mi() {
		return SRA_Avg_Mi;
	}
	public void setSRA_Avg_Mi(Double sRA_Avg_Mi) {
		SRA_Avg_Mi = sRA_Avg_Mi;
	}
	public Double getSRA_Min_Mi() {
		return SRA_Min_Mi;
	}
	public void setSRA_Min_Mi(Double sRA_Min_Mi) {
		SRA_Min_Mi = sRA_Min_Mi;
	}
	public Double getSRA_Max_Mi() {
		return SRA_Max_Mi;
	}
	public void setSRA_Max_Mi(Double sRA_Max_Mi) {
		SRA_Max_Mi = sRA_Max_Mi;
	}
	public Double getSRA_Mi_SD() {
		return SRA_Mi_SD;
	}
	public void setSRA_Mi_SD(Double sRA_Mi_SD) {
		SRA_Mi_SD = sRA_Mi_SD;
	}
	public Double getWIND_S_Avg_SRA_Mi() {
		return WIND_S_Avg_SRA_Mi;
	}
	public void setWIND_S_Avg_SRA_Mi(Double wIND_S_Avg_SRA_Mi) {
		WIND_S_Avg_SRA_Mi = wIND_S_Avg_SRA_Mi;
	}
	public Double getTEM_Avg_SRA_Mi() {
		return TEM_Avg_SRA_Mi;
	}
	public void setTEM_Avg_SRA_Mi(Double tEM_Avg_SRA_Mi) {
		TEM_Avg_SRA_Mi = tEM_Avg_SRA_Mi;
	}
	public Double getQRA_Avg_Mi() {
		return QRA_Avg_Mi;
	}
	public void setQRA_Avg_Mi(Double qRA_Avg_Mi) {
		QRA_Avg_Mi = qRA_Avg_Mi;
	}
	public Double getQRA_Min_Mi() {
		return QRA_Min_Mi;
	}
	public void setQRA_Min_Mi(Double qRA_Min_Mi) {
		QRA_Min_Mi = qRA_Min_Mi;
	}
	public Double getQRA_Max_Min() {
		return QRA_Max_Min;
	}
	public void setQRA_Max_Min(Double qRA_Max_Min) {
		QRA_Max_Min = qRA_Max_Min;
	}
	public Double getQRA_Mi_SD() {
		return QRA_Mi_SD;
	}
	public void setQRA_Mi_SD(Double qRA_Mi_SD) {
		QRA_Mi_SD = qRA_Mi_SD;
	}
	public Double getWIND_S_Avg_QRA_Mi() {
		return WIND_S_Avg_QRA_Mi;
	}
	public void setWIND_S_Avg_QRA_Mi(Double wIND_S_Avg_QRA_Mi) {
		WIND_S_Avg_QRA_Mi = wIND_S_Avg_QRA_Mi;
	}
	public Double getTEM_Avg_QRA_Mi() {
		return TEM_Avg_QRA_Mi;
	}
	public void setTEM_Avg_QRA_Mi(Double tEM_Avg_QRA_Mi) {
		TEM_Avg_QRA_Mi = tEM_Avg_QRA_Mi;
	}
	public Double getRRA_Avg_Mi() {
		return RRA_Avg_Mi;
	}
	public void setRRA_Avg_Mi(Double rRA_Avg_Mi) {
		RRA_Avg_Mi = rRA_Avg_Mi;
	}
	public Double getRRA_Min_Mi() {
		return RRA_Min_Mi;
	}
	public void setRRA_Min_Mi(Double rRA_Min_Mi) {
		RRA_Min_Mi = rRA_Min_Mi;
	}
	public Double getRRA_Max_Mi() {
		return RRA_Max_Mi;
	}
	public void setRRA_Max_Mi(Double rRA_Max_Mi) {
		RRA_Max_Mi = rRA_Max_Mi;
	}
	public Double getRRA_Mi_SD() {
		return RRA_Mi_SD;
	}
	public void setRRA_Mi_SD(Double rRA_Mi_SD) {
		RRA_Mi_SD = rRA_Mi_SD;
	}
	public Double getWIND_S_Avg_RRA_Mi() {
		return WIND_S_Avg_RRA_Mi;
	}
	public void setWIND_S_Avg_RRA_Mi(Double wIND_S_Avg_RRA_Mi) {
		WIND_S_Avg_RRA_Mi = wIND_S_Avg_RRA_Mi;
	}
	public Double getTEM_Avg_RRA_Mi() {
		return TEM_Avg_RRA_Mi;
	}
	public void setTEM_Avg_RRA_Mi(Double tEM_Avg_RRA_Mi) {
		TEM_Avg_RRA_Mi = tEM_Avg_RRA_Mi;
	}
	public Double getALR_Avg_Mi() {
		return ALR_Avg_Mi;
	}
	public void setALR_Avg_Mi(Double aLR_Avg_Mi) {
		ALR_Avg_Mi = aLR_Avg_Mi;
	}
	public Double getALR_Min_Mi() {
		return ALR_Min_Mi;
	}
	public void setALR_Min_Mi(Double aLR_Min_Mi) {
		ALR_Min_Mi = aLR_Min_Mi;
	}
	public Double getALR_Max_Mi() {
		return ALR_Max_Mi;
	}
	public void setALR_Max_Mi(Double aLR_Max_Mi) {
		ALR_Max_Mi = aLR_Max_Mi;
	}
	public Double getALR_Mi_SD() {
		return ALR_Mi_SD;
	}
	public void setALR_Mi_SD(Double aLR_Mi_SD) {
		ALR_Mi_SD = aLR_Mi_SD;
	}
	public Double getWIND_S_Avg_ALR_Mi() {
		return WIND_S_Avg_ALR_Mi;
	}
	public void setWIND_S_Avg_ALR_Mi(Double wIND_S_Avg_ALR_Mi) {
		WIND_S_Avg_ALR_Mi = wIND_S_Avg_ALR_Mi;
	}
	public Double getTEM_Avg_ALR_Mi() {
		return TEM_Avg_ALR_Mi;
	}
	public void setTEM_Avg_ALR_Mi(Double tEM_Avg_ALR_Mi) {
		TEM_Avg_ALR_Mi = tEM_Avg_ALR_Mi;
	}
	public Double getELR_Avg_Mi() {
		return ELR_Avg_Mi;
	}
	public void setELR_Avg_Mi(Double eLR_Avg_Mi) {
		ELR_Avg_Mi = eLR_Avg_Mi;
	}
	public Double getELR_Min_Mi() {
		return ELR_Min_Mi;
	}
	public void setELR_Min_Mi(Double eLR_Min_Mi) {
		ELR_Min_Mi = eLR_Min_Mi;
	}
	public Double getELR_Max_Mi() {
		return ELR_Max_Mi;
	}
	public void setELR_Max_Mi(Double eLR_Max_Mi) {
		ELR_Max_Mi = eLR_Max_Mi;
	}
	public Double getELR_Mi_SD() {
		return ELR_Mi_SD;
	}
	public void setELR_Mi_SD(Double eLR_Mi_SD) {
		ELR_Mi_SD = eLR_Mi_SD;
	}
	public Double getWIND_S_Avg_ELR_Mi() {
		return WIND_S_Avg_ELR_Mi;
	}
	public void setWIND_S_Avg_ELR_Mi(Double wIND_S_Avg_ELR_Mi) {
		WIND_S_Avg_ELR_Mi = wIND_S_Avg_ELR_Mi;
	}
	public Double getTEM_Avg_ELR_Mi() {
		return TEM_Avg_ELR_Mi;
	}
	public void setTEM_Avg_ELR_Mi(Double tEM_Avg_ELR_Mi) {
		TEM_Avg_ELR_Mi = tEM_Avg_ELR_Mi;
	}
	public Double getUVA_Avg_Mi() {
		return UVA_Avg_Mi;
	}
	public void setUVA_Avg_Mi(Double uVA_Avg_Mi) {
		UVA_Avg_Mi = uVA_Avg_Mi;
	}
	public Double getUVA_Min_Mi() {
		return UVA_Min_Mi;
	}
	public void setUVA_Min_Mi(Double uVA_Min_Mi) {
		UVA_Min_Mi = uVA_Min_Mi;
	}
	public Double getUV_Max_Mi() {
		return UV_Max_Mi;
	}
	public void setUV_Max_Mi(Double uV_Max_Mi) {
		UV_Max_Mi = uV_Max_Mi;
	}
	public Double getUVA_Mi_SD() {
		return UVA_Mi_SD;
	}
	public void setUVA_Mi_SD(Double uVA_Mi_SD) {
		UVA_Mi_SD = uVA_Mi_SD;
	}
	public Double getUVB_Avg_Mi() {
		return UVB_Avg_Mi;
	}
	public void setUVB_Avg_Mi(Double uVB_Avg_Mi) {
		UVB_Avg_Mi = uVB_Avg_Mi;
	}
	public Double getUVB_Min_Mi() {
		return UVB_Min_Mi;
	}
	public void setUVB_Min_Mi(Double uVB_Min_Mi) {
		UVB_Min_Mi = uVB_Min_Mi;
	}
	public Double getUVB_Max_Mi() {
		return UVB_Max_Mi;
	}
	public void setUVB_Max_Mi(Double uVB_Max_Mi) {
		UVB_Max_Mi = uVB_Max_Mi;
	}
	public Double getUVB_Mi_SD() {
		return UVB_Mi_SD;
	}
	public void setUVB_Mi_SD(Double uVB_Mi_SD) {
		UVB_Mi_SD = uVB_Mi_SD;
	}
	public Double getTEM_Avg_UV_Mi() {
		return TEM_Avg_UV_Mi;
	}
	public void setTEM_Avg_UV_Mi(Double tEM_Avg_UV_Mi) {
		TEM_Avg_UV_Mi = tEM_Avg_UV_Mi;
	}
	public Double getPAR_Avg_Mi() {
		return PAR_Avg_Mi;
	}
	public void setPAR_Avg_Mi(Double pAR_Avg_Mi) {
		PAR_Avg_Mi = pAR_Avg_Mi;
	}
	public Double getPAR_Min_Mi() {
		return PAR_Min_Mi;
	}
	public void setPAR_Min_Mi(Double pAR_Min_Mi) {
		PAR_Min_Mi = pAR_Min_Mi;
	}
	public Double getPAR_Max_Mi() {
		return PAR_Max_Mi;
	}
	public void setPAR_Max_Mi(Double pAR_Max_Mi) {
		PAR_Max_Mi = pAR_Max_Mi;
	}
	public Double getPAR_Mi_SD() {
		return PAR_Mi_SD;
	}
	public void setPAR_Mi_SD(Double pAR_Mi_SD) {
		PAR_Mi_SD = pAR_Mi_SD;
	}
	
}
