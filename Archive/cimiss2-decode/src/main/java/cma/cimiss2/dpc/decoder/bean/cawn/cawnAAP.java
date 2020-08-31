package cma.cimiss2.dpc.decoder.bean.cawn;

import java.util.Date;
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
	黑碳气溶胶实体类。解析时，各要素值均从报文直接取值、入库时各字段直接赋值。
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档:
 *    <ol>
 *      <li> <a href=" "> 《黑碳气溶胶观测资料数据表》 </a>
 *      <li> <a href=" "> 《气测函〔2018〕40号文-附件4.黑碳气溶胶观测数据格式.pdf》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月24日 上午11:34:32   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class cawnAAP {
	/**
	 * NO: 1.1  <br>
     * nameCN: 区站号 <br>
     * unit: <br>
     * BUFR FXY: V01301 <br>
     * descriptionCN: <br>
	 */
	private String V01301;      
	/**
	 * NO: 1.2 <br>
	 * nameCN: 项目代码<br>
	 * unit: <br>
	 * BUFR FXY: V_ITEM_CODE <br>
	 * descriptionCN: 
	 */
	private int itemCode; 
	/**
	 * NO: 1.3<br>
	 * nameCN: 年<br>
	 * unit: <br>
	 * BUFR FXY: V04001<br>
	 * descriptionCN: 
	 */
	private int year; 
	/**
	 * NO: 1.4<br>
	 * nameCN: 年序日<br>
	 * unit: <br>
	 * BUFR FXY: 无<br>
	 * descriptionCN: 
	 */
	private int julianDay; 
	/**
	 * NO: 1.5 <br>
	 * nameCN: 时分<br>
	 * unit:<br>
	 * BUFR FXY: V04004/V04005<br>
	 * descriptionCN:
	 */
	private int hhmm; 
	/**
	 * NO: 1.6 <br>
	 * nameCN: 资料时间<br>
	 * unit: <br>
	 * BUFR FXY: D_DATETIME<br>
	 * descriptionCN: 
	 */
	private Date observationTime;
	/**
	 * NO: 1.7 <br>
	 * nameCN: 流量 <br>
	 * unit: L/min<br>
	 * BUFR FXY: V70012<br>
	 * descriptionCN: 
	 */
	private double flow=999998; 
	/**
	 * NO: 1.8 <br>
	 * nameCN: 370nm浓度 <br>
	 * unit: ng/m3 <br>
	 * BUFR FXY: V_370nmBC<br>
	 * descriptionCN: 
	 */
	private double concentration370=999998; 
	/**
	 * NO: 1.9 <br>
	 * nameCN: 470nm浓度 <br>
	 * unit: ng/m3 <br>
	 * BUFR FXY: V_470nmBC<br>
	 * descriptionCN: 
	 */
	private double concentration470=999998; 
	/**
	 * NO: 1.10 <br>
	 * nameCN: 520nm浓度 <br>
	 * unit: ng/m3 <br>
	 * BUFR FXY: V_520nmBC<br>
	 * descriptionCN: 
	 */
	private double concentration520=999998; 
	/**
	 * NO: 1.11 <br>
	 * nameCN: 590nm浓度 <br>
	 * unit: ng/m3 <br>
	 * BUFR FXY: V_590nmBC<br>
	 * descriptionCN: 
	 */
	private double concentration590=999998; 
	/**
	 * NO: 1.12 <br>
	 * nameCN: 660nm浓度 <br>
	 * unit: ng/m3 <br>
	 * BUFR FXY: V_660nmBC<br>
	 * descriptionCN: 
	 */
	private double concentration660=999998;  
	/**
	 * NO: 1.13 <br>
	 * nameCN: 880nm浓度 <br>
	 * unit: ng/m3 <br>
	 * BUFR FXY: V_880nmBC<br>
	 * descriptionCN: 
	 */
	private double concentration880=999998; 
	/**
	 * NO: 1.14 <br>
	 * nameCN: 950nm浓度 <br>
	 * unit: ng/m3 <br>
	 * BUFR FXY: V_950nmBC<br>
	 * descriptionCN: 
	 */
	private double concentration950=999998; 
	/**
	 * NO: 1.15 <br>
	 * nameCN: 370nm采样区零点信号<br>
	 * unit: <br>
	 * BUFR FXY: V_SZ_370nm<br>
	 * descriptionCN: 
	 */
	private double sampleZoneZeroPointSignal370=999998; 
	/**
	 * NO: 1.16 <br>
	 * nameCN: 370nm采样区测量信号<br>
	 * unit: <br>
	 * BUFR FXY:V_SB_370nm <br>
	 * descriptionCN: 
	 */
	private double sampleZoneMeasureSignal370=999998; 
	/**
	 * NO: 1.17 <br>
	 * nameCN: 370nm参照区零点信号<br>
	 * unit: <br>
	 * BUFR V_RZ_370nm <br>
	 * descriptionCN: 
	 */
	private double referZoneZeroPointSignal370=999998; 
	/**
	 * NO: 1.18 <br>
	 * nameCN: 370nm参照区测量信号 <br>
	 * unit: <br>
	 * BUFR V_RB_370nm <br>
	 * descriptionCN: 
	 */
	private double referZoneMeasureSignal370=999998; 
	/**
	 * NO: 1.19 <br>
	 * nameCN: 370nm分流比  <br>
	 * unit: <br>
	 * BUFR V_Fri_370nm <br>
	 * descriptionCN: 
	 */
	private double splitRatio370=999998; 
	/**
	 * NO: 1.20 <br>
	 * nameCN: 370nm光衰减率  <br>
	 * unit: <br>
	 * BUFR V_Attn_370nm <br>
	 * descriptionCN: 
	 */
	private double lightLossRate370=999998; 
	/**
	 * NO: 1.21 <br>
	 * nameCN: 470nm采样区零点信号  <br>
	 * unit: <br>
	 * BUFR V_SZ_470nm <br>
	 * descriptionCN: 
	 */
	private double sampleZoneZeroPointSignal470=999998; 
	/**
	 * NO: 1.22 <br>
	 * nameCN:  470nm采样区测量信号 <br>
	 * unit: <br>
	 * BUFR V_SB_470nm <br>
	 * descriptionCN: 
	 */
	private double sampleZoneMeasureSignal470=999998; 
	/**
	 * NO: 1.23 <br>
	 * nameCN:  470nm参照区零点信号 <br>
	 * unit: <br>
	 * BUFR V_RZ_470nm <br>
	 * descriptionCN: 
	 */
	private double referZoneZeroPointSignal470=999998; 
	/**
	 * NO: 1.24 <br>
	 * nameCN: 470nm参照区测量信号  <br>
	 * unit: <br>
	 * BUFR V_RB_470nm <br>
	 * descriptionCN: 
	 */
	private double referZoneMeasureSignal470=999998; 
	/**
	 * NO: 1.25 <br>
	 * nameCN:470nm分流比  <br>
	 * unit: <br>
	 * BUFR V_Fri_470nm <br>
	 * descriptionCN: 
	 */
	private double splitRatio470=999998;
	/**
	 * NO: 1.26 <br>
	 * nameCN:470nm光衰减率 <br>
	 * unit: <br>
	 * BUFR V_Attn_470nm <br>
	 * descriptionCN: 
	 */
	private double lightLossRate470=999998; 
	/**
	 * NO: 1.27 <br>
	 * nameCN: 520nm采样区零点信号 <br>
	 * unit: <br>
	 * BUFR V_SZ_520nm <br>
	 * descriptionCN: 
	 */
	private double sampleZoneZeroPointSignal520=999998; 
	/**
	 * NO: 1.28 <br>
	 * nameCN: 520nm采样区测量信号  <br>
	 * unit: <br>
	 * BUFR V_SB_520nm <br>
	 * descriptionCN: 
	 */
	private double sampleZoneMeasureSignal520=999998; 
	/**
	 * NO: 1.29 <br>
	 * nameCN: 520nm参照区零点信号  <br>
	 * unit: <br>
	 * BUFR V_RZ_520nm <br>
	 * descriptionCN: 
	 */
	private double referZoneZeroPointSignal520=999998; 
	/**
	 * NO: 1.30 <br>
	 * nameCN: 520nm参照区测量信号  <br>
	 * unit: <br>
	 * BUFR V_RB_520nm <br>
	 * descriptionCN: 
	 */
	private double referZoneMeasureSignal520=999998; 
	/**
	 * NO: 1.31 <br>
	 * nameCN: 520nm分流比  <br>
	 * unit: <br>
	 * BUFR V_Fri_520nm <br>
	 * descriptionCN: 
	 */
	private double splitRatio520=999998;
	/**
	 * NO: 1.32 <br>
	 * nameCN: 520nm光衰减率  <br>
	 * unit: <br>
	 * BUFR V_Attn_520nm <br>
	 * descriptionCN: 
	 */
	private double lightLossRate520=999998; 
	/**
	 * NO: 1.33 <br>
	 * nameCN: 590nm采样区零点信号  <br>
	 * unit: <br>
	 * BUFR V_SZ_590nm <br>
	 * descriptionCN: 
	 */
	private double sampleZoneZeroPointSignal590=999998; 
	/**
	 * NO: 1.34 <br>
	 * nameCN: 590nm采样区测量信号  <br>
	 * unit: <br>
	 * BUFR V_SB_590nm <br>
	 * descriptionCN: 
	 */
	private double sampleZoneMeasureSignal590=999998; 
	/**
	 * NO: 1.35 <br>
	 * nameCN: 590nm参照区零点信号  <br>
	 * unit: <br>
	 * BUFR V_RZ_590nm <br>
	 * descriptionCN: 
	 */
	private double referZoneZeroPointSignal590=999998; 
	/**
	 * NO: 1.36 <br>
	 * nameCN: 590nm参照区测量信号  <br>
	 * unit: <br>
	 * BUFR V_RB_590nm <br>
	 * descriptionCN: 
	 */
	private double referZoneMeasureSignal590=999998; 
	/**
	 * NO: 1.37 <br>
	 * nameCN: 590nm分流比  <br>
	 * unit: <br>
	 * BUFR V_Fri_590nm <br>
	 * descriptionCN: 
	 */
	private double splitRatio590=999998;
	/**
	 * NO: 1.38 <br>
	 * nameCN: 590nm光衰减率   <br>
	 * unit: <br>
	 * BUFR V_Attn_590nm <br>
	 * descriptionCN: 
	 */
	private double lightLossRate590=999998; 
	/**
	 * NO: 1.39 <br>
	 * nameCN: 660nm采样区零点信号  <br>
	 * unit: <br>
	 * BUFR V_SZ_660nm <br>
	 * descriptionCN: 
	 */
	private double sampleZoneZeroPointSignal660=999998; 
	/**
	 * NO: 1.40 <br>
	 * nameCN:  660nm采样区测量信号  <br>
	 * unit: <br>
	 * BUFR V_SB_660nm <br>
	 * descriptionCN: 
	 */
	private double sampleZoneMeasureSignal660=999998; 
	/**
	 * NO: 1.41 <br>
	 * nameCN: 660nm参照区零点信号  <br>
	 * unit: <br>
	 * BUFR V_RZ_660nm <br>
	 * descriptionCN: 
	 */
	private double referZoneZeroPointSignal660=999998; 
	/**
	 * NO: 1.42 <br>
	 * nameCN: 660nm参照区测量信号  <br>
	 * unit: <br>
	 * BUFR V_RB_660nm <br>
	 * descriptionCN: 
	 */
	private double referZoneMeasureSignal660=999998; 
	/**
	 * NO: 1.43 <br>
	 * nameCN: 660nm分流比  <br>
	 * unit: <br>
	 * BUFR V_Fri_660nm <br>
	 * descriptionCN: 
	 */
	private double splitRatio660=999998;
	/**
	 * NO: 1.44 <br>
	 * nameCN: 660nm光衰减率 <br>
	 * unit: <br>
	 * BUFR V_Attn_660nm <br>
	 * descriptionCN: 
	 */
	private double lightLossRate660=999998; 
	/**
	 * NO: 1.45 <br>
	 * nameCN: 880nm采样区零点信号<br>
	 * unit: <br>
	 * BUFR V_SZ_880nm <br>
	 * descriptionCN: 
	 */
	private double sampleZoneZeroPointSignal880=999998; 
	/**
	 * NO: 1.46 <br>
	 * nameCN: 880nm采样区测量信号<br>
	 * unit: <br>
	 * BUFR V_SB_880nm <br>
	 * descriptionCN: 
	 */
	private double sampleZoneMeasureSignal880=999998;
	/**
	 * NO: 1.47 <br>
	 * nameCN: 880nm参照区零点信号 <br>
	 * unit: <br>
	 * BUFR V_RZ_880nm <br>
	 * descriptionCN: 
	 */
	private double referZoneZeroPointSignal880=999998; 
	/**
	 * NO: 1.48 <br>
	 * nameCN: 880nm参照区测量信号 <br>
	 * unit: <br>
	 * BUFR V_RB_880nm <br>
	 * descriptionCN: 
	 */
	private double referZoneMeasureSignal880=999998; 
	/**
	 * NO: 1.49 <br>
	 * nameCN:  880nm分流比  <br>
	 * unit: <br>
	 * BUFR V_Fri_880nm <br>
	 * descriptionCN: 
	 */
	private double splitRatio880=999998;
	/**
	 * NO: 1.50 <br>
	 * nameCN: 880nm光衰减率   <br>
	 * unit: <br>
	 * BUFR V_Attn_880nm <br>
	 * descriptionCN: 
	 */
	private double lightLossRate880=999998; 
	/**
	 * NO: 1.51 <br>
	 * nameCN: 950nm采样区零点信号 <br>
	 * unit: <br>
	 * BUFR V_SZ_950nm <br>
	 * descriptionCN: 
	 */
	private double sampleZoneZeroPointSignal950=999998; 
	/**
	 * NO: 1.52 <br>
	 * nameCN: 950nm采样区测量信号<br>
	 * unit: <br>
	 * BUFR V_SB_950nm <br>
	 * descriptionCN: 
	 */
	private double sampleZoneMeasureSignal950=999998; 
	/**
	 * NO: 1.53 <br>
	 * nameCN: 950nm参照区零点信号<br>
	 * unit: <br>
	 * BUFR V_RZ_950nm <br>
	 * descriptionCN: 
	 */
	private double referZoneZeroPointSignal950=999998; 
	/**
	 * NO: 1.54 <br>
	 * nameCN: 950nm参照区测量信号 <br>
	 * unit: <br>
	 * BUFR V_RB_950nm <br>
	 * descriptionCN: 
	 */
	private double referZoneMeasureSignal950=999998; 
	/**
	 * NO: 1.55 <br>
	 * nameCN: 950nm分流比 <br>
	 * unit: <br>
	 * BUFR V_Fri_950nm <br>
	 * descriptionCN: 
	 */
	private double splitRatio950=999998;
	/**
	 * NO: 1.56<br>
	 * nameCN:  950nm光衰减率  <br>
	 * unit: <br>
	 * BUFR V_Attn_950nm <br>
	 * descriptionCN: 
	 */
	private double lightLossRate950=999998; 
	
	private double Timebase=999998;//Timebase
	private double referenceSignal370=999998;//370nm参考信号
	private double firstSampleSignal370=999998;//370nm第1采样点信号
	private double secondSampleSignal370=999998;//370nm第2采样点信号
	private double referenceSignal470=999998;//470nm参考信号
	private double firstSampleSignal470=999998;//470nm第1采样点信号
	private double secondSampleSignal470=999998;//470nm第2采样点信号
	private double referenceSignal520=999998;//520nm参考信号
	private double firstSampleSignal520=999998;//520nm第1采样点信号
	private double secondSampleSignal520=999998;//520nm第2采样点信号
	private double referenceSignal590=999998;//590nm参考信号
	private double firstSampleSignal590=999998;//590nm第1采样点信号
	private double secondSampleSignal590=999998;//590nm第2采样点信号
	private double referenceSignal660=999998;//660nm参考信号
	private double firstSampleSignal660=999998;//660nm第1采样点信号
	private double secondSampleSignal660=999998;//66nm第2采样点信号
	private double referenceSignal880=999998;//880nm参考信号
	private double firstSampleSignal880=999998;//880nm第1采样点信号
	private double secondSampleSignal880=999998;//880nm第2采样点信号
	private double referenceSignal950=999998;//950nm参考信号
	private double firstSampleSignal950=999998;//950nm第1采样点信号
	private double secondSampleSignal950=999998;//950nm第2采样点信号
	
	private double flow1=999998;//第1采样点流量
	private double flow2=999998;//FlowC与Flow1之差
	
	
	private double pressure=999998;//气压
	private double temperature=999998;//温度
	private double BB=999998;//通过Sandradewi model得到的生物质燃烧黑碳比例
	private double contTemp=999998;//控制板温度
	private double supplyTemp=999998;//供电板温度
	private double status=999998;//状态码
	private double contStatus=999998;//状态码
	private double detectStatus=999998;//状态码
	private double ledStatus=999998;//状态码
	private double valveStatus=999998;//状态码
	private double ledTemp=999998;//显示板温度
	private double firstSanmpleCon370=999998;//370nm在第1采样点测得的未补偿黑碳浓度
	private double secondSanmpleCon370=999998;//370nm在第2采样点测得的未补偿黑碳浓度
//	private double carbonCon370=999998;//370nm黑碳浓度
	private double firstSanmpleCon470=999998;//470nm在第1采样点测得的未补偿黑碳浓度
	private double secondSanmpleCon470=999998;//470nm在第2采样点测得的未补偿黑碳浓度
//	private double carbonCon470=999998;//470nm黑碳浓度
	
	private double firstSanmpleCon520=999998;//520nm在第1采样点测得的未补偿黑碳浓度
	private double secondSanmpleCon520=999998;//520nm在第2采样点测得的未补偿黑碳浓度
//	private double carbonCon520=999998;//520nm黑碳浓度
	
	private double firstSanmpleCon590=999998;//590nm在第1采样点测得的未补偿黑碳浓度
	private double secondSanmpleCon590=999998;//590nm在第2采样点测得的未补偿黑碳浓度
//	private double carbonCon590=999998;//590nm黑碳浓度
	
	private double firstSanmpleCon660=999998;//660nm在第1采样点测得的未补偿黑碳浓度
	private double secondSanmpleCon660=999998;//660nm在第2采样点测得的未补偿黑碳浓度
//	private double carbonCon660=999998;//660nm黑碳浓度
	
	private double firstSanmpleCon880=999998;//880nm在第1采样点测得的未补偿黑碳浓度
	private double secondSanmpleCon880=999998;//880nm在第2采样点测得的未补偿黑碳浓度
//	private double carbonCon880=999998;//880nm黑碳浓度
	
	private double firstSanmpleCon950=999998;//950nm在第1采样点测得的未补偿黑碳浓度
	private double secondSanmpleCon950=999998;//950nm在第2采样点测得的未补偿黑碳浓度
//	private double carbonCon950=999998;//950nm黑碳浓度
	
	private double compCoeffi370=999998;//370nm补偿系数
	private double compCoeffi470=999998;//470nm补偿系数
	private double compCoeffi520=999998;//520nm补偿系数
	private double compCoeffi590=999998;//590nm补偿系数
	private double compCoeffi660=999998;//660nm补偿系数
	private double compCoeffi880=999998;//880nm补偿系数
	private double compCoeffi950=999998;//950nm补偿系数
	private double tapeAdvCount=999998;//采样带自观测开始的前进量
	public String getV01301() {
		return V01301;
	}
	public int getItemCode() {
		return itemCode;
	}
	public int getYear() {
		return year;
	}
	public int getJulianDay() {
		return julianDay;
	}
	public int getHhmm() {
		return hhmm;
	}
	public Date getObservationTime() {
		return observationTime;
	}
	public double getFlow() {
		return flow;
	}
	public double getConcentration370() {
		return concentration370;
	}
	public double getConcentration470() {
		return concentration470;
	}
	public double getConcentration520() {
		return concentration520;
	}
	public double getConcentration590() {
		return concentration590;
	}
	public double getConcentration660() {
		return concentration660;
	}
	public double getConcentration880() {
		return concentration880;
	}
	public double getConcentration950() {
		return concentration950;
	}
	public double getSampleZoneZeroPointSignal370() {
		return sampleZoneZeroPointSignal370;
	}
	public double getSampleZoneMeasureSignal370() {
		return sampleZoneMeasureSignal370;
	}
	public double getReferZoneZeroPointSignal370() {
		return referZoneZeroPointSignal370;
	}
	public double getReferZoneMeasureSignal370() {
		return referZoneMeasureSignal370;
	}
	public double getSplitRatio370() {
		return splitRatio370;
	}
	public double getLightLossRate370() {
		return lightLossRate370;
	}
	public double getSampleZoneZeroPointSignal470() {
		return sampleZoneZeroPointSignal470;
	}
	public double getSampleZoneMeasureSignal470() {
		return sampleZoneMeasureSignal470;
	}
	public double getReferZoneZeroPointSignal470() {
		return referZoneZeroPointSignal470;
	}
	public double getReferZoneMeasureSignal470() {
		return referZoneMeasureSignal470;
	}
	public double getSplitRatio470() {
		return splitRatio470;
	}
	public double getLightLossRate470() {
		return lightLossRate470;
	}
	public double getSampleZoneZeroPointSignal520() {
		return sampleZoneZeroPointSignal520;
	}
	public double getSampleZoneMeasureSignal520() {
		return sampleZoneMeasureSignal520;
	}
	public double getReferZoneZeroPointSignal520() {
		return referZoneZeroPointSignal520;
	}
	public double getReferZoneMeasureSignal520() {
		return referZoneMeasureSignal520;
	}
	public double getSplitRatio520() {
		return splitRatio520;
	}
	public double getLightLossRate520() {
		return lightLossRate520;
	}
	public double getSampleZoneZeroPointSignal590() {
		return sampleZoneZeroPointSignal590;
	}
	public double getSampleZoneMeasureSignal590() {
		return sampleZoneMeasureSignal590;
	}
	public double getReferZoneZeroPointSignal590() {
		return referZoneZeroPointSignal590;
	}
	public double getReferZoneMeasureSignal590() {
		return referZoneMeasureSignal590;
	}
	public double getSplitRatio590() {
		return splitRatio590;
	}
	public double getLightLossRate590() {
		return lightLossRate590;
	}
	public double getSampleZoneZeroPointSignal660() {
		return sampleZoneZeroPointSignal660;
	}
	public double getSampleZoneMeasureSignal660() {
		return sampleZoneMeasureSignal660;
	}
	public double getReferZoneZeroPointSignal660() {
		return referZoneZeroPointSignal660;
	}
	public double getReferZoneMeasureSignal660() {
		return referZoneMeasureSignal660;
	}
	public double getSplitRatio660() {
		return splitRatio660;
	}
	public double getLightLossRate660() {
		return lightLossRate660;
	}
	public double getSampleZoneZeroPointSignal880() {
		return sampleZoneZeroPointSignal880;
	}
	public double getSampleZoneMeasureSignal880() {
		return sampleZoneMeasureSignal880;
	}
	public double getReferZoneZeroPointSignal880() {
		return referZoneZeroPointSignal880;
	}
	public double getReferZoneMeasureSignal880() {
		return referZoneMeasureSignal880;
	}
	public double getSplitRatio880() {
		return splitRatio880;
	}
	public double getLightLossRate880() {
		return lightLossRate880;
	}
	public double getSampleZoneZeroPointSignal950() {
		return sampleZoneZeroPointSignal950;
	}
	public double getSampleZoneMeasureSignal950() {
		return sampleZoneMeasureSignal950;
	}
	public double getReferZoneZeroPointSignal950() {
		return referZoneZeroPointSignal950;
	}
	public double getReferZoneMeasureSignal950() {
		return referZoneMeasureSignal950;
	}
	public double getSplitRatio950() {
		return splitRatio950;
	}
	public double getLightLossRate950() {
		return lightLossRate950;
	}
	public double getTimebase() {
		return Timebase;
	}
	public double getReferenceSignal370() {
		return referenceSignal370;
	}
	public double getFirstSampleSignal370() {
		return firstSampleSignal370;
	}
	public double getSecondSampleSignal370() {
		return secondSampleSignal370;
	}
	public double getReferenceSignal470() {
		return referenceSignal470;
	}
	public double getFirstSampleSignal470() {
		return firstSampleSignal470;
	}
	public double getSecondSampleSignal470() {
		return secondSampleSignal470;
	}
	public double getReferenceSignal520() {
		return referenceSignal520;
	}
	public double getFirstSampleSignal520() {
		return firstSampleSignal520;
	}
	public double getSecondSampleSignal520() {
		return secondSampleSignal520;
	}
	public double getReferenceSignal590() {
		return referenceSignal590;
	}
	public double getFirstSampleSignal590() {
		return firstSampleSignal590;
	}
	public double getSecondSampleSignal590() {
		return secondSampleSignal590;
	}
	public double getReferenceSignal660() {
		return referenceSignal660;
	}
	public double getFirstSampleSignal660() {
		return firstSampleSignal660;
	}
	public double getSecondSampleSignal660() {
		return secondSampleSignal660;
	}
	public double getReferenceSignal880() {
		return referenceSignal880;
	}
	public double getFirstSampleSignal880() {
		return firstSampleSignal880;
	}
	public double getSecondSampleSignal880() {
		return secondSampleSignal880;
	}
	public double getReferenceSignal950() {
		return referenceSignal950;
	}
	public double getFirstSampleSignal950() {
		return firstSampleSignal950;
	}
	public double getSecondSampleSignal950() {
		return secondSampleSignal950;
	}
	public double getFlow1() {
		return flow1;
	}
	public double getFlow2() {
		return flow2;
	}
	public double getPressure() {
		return pressure;
	}
	public double getTemperature() {
		return temperature;
	}
	public double getBB() {
		return BB;
	}
	public double getContTemp() {
		return contTemp;
	}
	public double getSupplyTemp() {
		return supplyTemp;
	}
	public double getStatus() {
		return status;
	}
	public double getContStatus() {
		return contStatus;
	}
	public double getDetectStatus() {
		return detectStatus;
	}
	public double getLedStatus() {
		return ledStatus;
	}
	public double getValveStatus() {
		return valveStatus;
	}
	public double getLedTemp() {
		return ledTemp;
	}
	public double getFirstSanmpleCon370() {
		return firstSanmpleCon370;
	}
	public double getSecondSanmpleCon370() {
		return secondSanmpleCon370;
	}
//	public double getCarbonCon370() {
//		return carbonCon370;
//	}
	public double getFirstSanmpleCon470() {
		return firstSanmpleCon470;
	}
	public double getSecondSanmpleCon470() {
		return secondSanmpleCon470;
	}
//	public double getCarbonCon470() {
//		return carbonCon470;
//	}
	public double getFirstSanmpleCon520() {
		return firstSanmpleCon520;
	}
	public double getSecondSanmpleCon520() {
		return secondSanmpleCon520;
	}
//	public double getCarbonCon520() {
//		return carbonCon520;
//	}
	public double getFirstSanmpleCon590() {
		return firstSanmpleCon590;
	}
	public double getSecondSanmpleCon590() {
		return secondSanmpleCon590;
	}
//	public double getCarbonCon590() {
//		return carbonCon590;
//	}
	public double getFirstSanmpleCon660() {
		return firstSanmpleCon660;
	}
	public double getSecondSanmpleCon660() {
		return secondSanmpleCon660;
	}
//	public double getCarbonCon660() {
//		return carbonCon660;
//	}
	public double getFirstSanmpleCon880() {
		return firstSanmpleCon880;
	}
	public double getSecondSanmpleCon880() {
		return secondSanmpleCon880;
	}
//	public double getCarbonCon880() {
//		return carbonCon880;
//	}
	public double getFirstSanmpleCon950() {
		return firstSanmpleCon950;
	}
	public double getSecondSanmpleCon950() {
		return secondSanmpleCon950;
	}
//	public double getCarbonCon950() {
//		return carbonCon950;
//	}
	public double getCompCoeffi370() {
		return compCoeffi370;
	}
	public double getCompCoeffi470() {
		return compCoeffi470;
	}
	public double getCompCoeffi520() {
		return compCoeffi520;
	}
	public double getCompCoeffi590() {
		return compCoeffi590;
	}
	public double getCompCoeffi660() {
		return compCoeffi660;
	}
	public double getCompCoeffi880() {
		return compCoeffi880;
	}
	public double getCompCoeffi950() {
		return compCoeffi950;
	}
	public double getTapeAdvCount() {
		return tapeAdvCount;
	}
	public void setV01301(String v01301) {
		V01301 = v01301;
	}
	public void setItemCode(int itemCode) {
		this.itemCode = itemCode;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public void setJulianDay(int julianDay) {
		this.julianDay = julianDay;
	}
	public void setHhmm(int hhmm) {
		this.hhmm = hhmm;
	}
	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}
	public void setFlow(double flow) {
		this.flow = flow;
	}
	public void setConcentration370(double concentration370) {
		this.concentration370 = concentration370;
	}
	public void setConcentration470(double concentration470) {
		this.concentration470 = concentration470;
	}
	public void setConcentration520(double concentration520) {
		this.concentration520 = concentration520;
	}
	public void setConcentration590(double concentration590) {
		this.concentration590 = concentration590;
	}
	public void setConcentration660(double concentration660) {
		this.concentration660 = concentration660;
	}
	public void setConcentration880(double concentration880) {
		this.concentration880 = concentration880;
	}
	public void setConcentration950(double concentration950) {
		this.concentration950 = concentration950;
	}
	public void setSampleZoneZeroPointSignal370(double sampleZoneZeroPointSignal370) {
		this.sampleZoneZeroPointSignal370 = sampleZoneZeroPointSignal370;
	}
	public void setSampleZoneMeasureSignal370(double sampleZoneMeasureSignal370) {
		this.sampleZoneMeasureSignal370 = sampleZoneMeasureSignal370;
	}
	public void setReferZoneZeroPointSignal370(double referZoneZeroPointSignal370) {
		this.referZoneZeroPointSignal370 = referZoneZeroPointSignal370;
	}
	public void setReferZoneMeasureSignal370(double referZoneMeasureSignal370) {
		this.referZoneMeasureSignal370 = referZoneMeasureSignal370;
	}
	public void setSplitRatio370(double splitRatio370) {
		this.splitRatio370 = splitRatio370;
	}
	public void setLightLossRate370(double lightLossRate370) {
		this.lightLossRate370 = lightLossRate370;
	}
	public void setSampleZoneZeroPointSignal470(double sampleZoneZeroPointSignal470) {
		this.sampleZoneZeroPointSignal470 = sampleZoneZeroPointSignal470;
	}
	public void setSampleZoneMeasureSignal470(double sampleZoneMeasureSignal470) {
		this.sampleZoneMeasureSignal470 = sampleZoneMeasureSignal470;
	}
	public void setReferZoneZeroPointSignal470(double referZoneZeroPointSignal470) {
		this.referZoneZeroPointSignal470 = referZoneZeroPointSignal470;
	}
	public void setReferZoneMeasureSignal470(double referZoneMeasureSignal470) {
		this.referZoneMeasureSignal470 = referZoneMeasureSignal470;
	}
	public void setSplitRatio470(double splitRatio470) {
		this.splitRatio470 = splitRatio470;
	}
	public void setLightLossRate470(double lightLossRate470) {
		this.lightLossRate470 = lightLossRate470;
	}
	public void setSampleZoneZeroPointSignal520(double sampleZoneZeroPointSignal520) {
		this.sampleZoneZeroPointSignal520 = sampleZoneZeroPointSignal520;
	}
	public void setSampleZoneMeasureSignal520(double sampleZoneMeasureSignal520) {
		this.sampleZoneMeasureSignal520 = sampleZoneMeasureSignal520;
	}
	public void setReferZoneZeroPointSignal520(double referZoneZeroPointSignal520) {
		this.referZoneZeroPointSignal520 = referZoneZeroPointSignal520;
	}
	public void setReferZoneMeasureSignal520(double referZoneMeasureSignal520) {
		this.referZoneMeasureSignal520 = referZoneMeasureSignal520;
	}
	public void setSplitRatio520(double splitRatio520) {
		this.splitRatio520 = splitRatio520;
	}
	public void setLightLossRate520(double lightLossRate520) {
		this.lightLossRate520 = lightLossRate520;
	}
	public void setSampleZoneZeroPointSignal590(double sampleZoneZeroPointSignal590) {
		this.sampleZoneZeroPointSignal590 = sampleZoneZeroPointSignal590;
	}
	public void setSampleZoneMeasureSignal590(double sampleZoneMeasureSignal590) {
		this.sampleZoneMeasureSignal590 = sampleZoneMeasureSignal590;
	}
	public void setReferZoneZeroPointSignal590(double referZoneZeroPointSignal590) {
		this.referZoneZeroPointSignal590 = referZoneZeroPointSignal590;
	}
	public void setReferZoneMeasureSignal590(double referZoneMeasureSignal590) {
		this.referZoneMeasureSignal590 = referZoneMeasureSignal590;
	}
	public void setSplitRatio590(double splitRatio590) {
		this.splitRatio590 = splitRatio590;
	}
	public void setLightLossRate590(double lightLossRate590) {
		this.lightLossRate590 = lightLossRate590;
	}
	public void setSampleZoneZeroPointSignal660(double sampleZoneZeroPointSignal660) {
		this.sampleZoneZeroPointSignal660 = sampleZoneZeroPointSignal660;
	}
	public void setSampleZoneMeasureSignal660(double sampleZoneMeasureSignal660) {
		this.sampleZoneMeasureSignal660 = sampleZoneMeasureSignal660;
	}
	public void setReferZoneZeroPointSignal660(double referZoneZeroPointSignal660) {
		this.referZoneZeroPointSignal660 = referZoneZeroPointSignal660;
	}
	public void setReferZoneMeasureSignal660(double referZoneMeasureSignal660) {
		this.referZoneMeasureSignal660 = referZoneMeasureSignal660;
	}
	public void setSplitRatio660(double splitRatio660) {
		this.splitRatio660 = splitRatio660;
	}
	public void setLightLossRate660(double lightLossRate660) {
		this.lightLossRate660 = lightLossRate660;
	}
	public void setSampleZoneZeroPointSignal880(double sampleZoneZeroPointSignal880) {
		this.sampleZoneZeroPointSignal880 = sampleZoneZeroPointSignal880;
	}
	public void setSampleZoneMeasureSignal880(double sampleZoneMeasureSignal880) {
		this.sampleZoneMeasureSignal880 = sampleZoneMeasureSignal880;
	}
	public void setReferZoneZeroPointSignal880(double referZoneZeroPointSignal880) {
		this.referZoneZeroPointSignal880 = referZoneZeroPointSignal880;
	}
	public void setReferZoneMeasureSignal880(double referZoneMeasureSignal880) {
		this.referZoneMeasureSignal880 = referZoneMeasureSignal880;
	}
	public void setSplitRatio880(double splitRatio880) {
		this.splitRatio880 = splitRatio880;
	}
	public void setLightLossRate880(double lightLossRate880) {
		this.lightLossRate880 = lightLossRate880;
	}
	public void setSampleZoneZeroPointSignal950(double sampleZoneZeroPointSignal950) {
		this.sampleZoneZeroPointSignal950 = sampleZoneZeroPointSignal950;
	}
	public void setSampleZoneMeasureSignal950(double sampleZoneMeasureSignal950) {
		this.sampleZoneMeasureSignal950 = sampleZoneMeasureSignal950;
	}
	public void setReferZoneZeroPointSignal950(double referZoneZeroPointSignal950) {
		this.referZoneZeroPointSignal950 = referZoneZeroPointSignal950;
	}
	public void setReferZoneMeasureSignal950(double referZoneMeasureSignal950) {
		this.referZoneMeasureSignal950 = referZoneMeasureSignal950;
	}
	public void setSplitRatio950(double splitRatio950) {
		this.splitRatio950 = splitRatio950;
	}
	public void setLightLossRate950(double lightLossRate950) {
		this.lightLossRate950 = lightLossRate950;
	}
	public void setTimebase(double timebase) {
		Timebase = timebase;
	}
	public void setReferenceSignal370(double referenceSignal370) {
		this.referenceSignal370 = referenceSignal370;
	}
	public void setFirstSampleSignal370(double firstSampleSignal370) {
		this.firstSampleSignal370 = firstSampleSignal370;
	}
	public void setSecondSampleSignal370(double secondSampleSignal370) {
		this.secondSampleSignal370 = secondSampleSignal370;
	}
	public void setReferenceSignal470(double referenceSignal470) {
		this.referenceSignal470 = referenceSignal470;
	}
	public void setFirstSampleSignal470(double firstSampleSignal470) {
		this.firstSampleSignal470 = firstSampleSignal470;
	}
	public void setSecondSampleSignal470(double secondSampleSignal470) {
		this.secondSampleSignal470 = secondSampleSignal470;
	}
	public void setReferenceSignal520(double referenceSignal520) {
		this.referenceSignal520 = referenceSignal520;
	}
	public void setFirstSampleSignal520(double firstSampleSignal520) {
		this.firstSampleSignal520 = firstSampleSignal520;
	}
	public void setSecondSampleSignal520(double secondSampleSignal520) {
		this.secondSampleSignal520 = secondSampleSignal520;
	}
	public void setReferenceSignal590(double referenceSignal590) {
		this.referenceSignal590 = referenceSignal590;
	}
	public void setFirstSampleSignal590(double firstSampleSignal590) {
		this.firstSampleSignal590 = firstSampleSignal590;
	}
	public void setSecondSampleSignal590(double secondSampleSignal590) {
		this.secondSampleSignal590 = secondSampleSignal590;
	}
	public void setReferenceSignal660(double referenceSignal660) {
		this.referenceSignal660 = referenceSignal660;
	}
	public void setFirstSampleSignal660(double firstSampleSignal660) {
		this.firstSampleSignal660 = firstSampleSignal660;
	}
	public void setSecondSampleSignal660(double secondSampleSignal660) {
		this.secondSampleSignal660 = secondSampleSignal660;
	}
	public void setReferenceSignal880(double referenceSignal880) {
		this.referenceSignal880 = referenceSignal880;
	}
	public void setFirstSampleSignal880(double firstSampleSignal880) {
		this.firstSampleSignal880 = firstSampleSignal880;
	}
	public void setSecondSampleSignal880(double secondSampleSignal880) {
		this.secondSampleSignal880 = secondSampleSignal880;
	}
	public void setReferenceSignal950(double referenceSignal950) {
		this.referenceSignal950 = referenceSignal950;
	}
	public void setFirstSampleSignal950(double firstSampleSignal950) {
		this.firstSampleSignal950 = firstSampleSignal950;
	}
	public void setSecondSampleSignal950(double secondSampleSignal950) {
		this.secondSampleSignal950 = secondSampleSignal950;
	}
	public void setFlow1(double flow1) {
		this.flow1 = flow1;
	}
	public void setFlow2(double flow2) {
		this.flow2 = flow2;
	}
	public void setPressure(double pressure) {
		this.pressure = pressure;
	}
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	public void setBB(double bB) {
		BB = bB;
	}
	public void setContTemp(double contTemp) {
		this.contTemp = contTemp;
	}
	public void setSupplyTemp(double supplyTemp) {
		this.supplyTemp = supplyTemp;
	}
	public void setStatus(double status) {
		this.status = status;
	}
	public void setContStatus(double contStatus) {
		this.contStatus = contStatus;
	}
	public void setDetectStatus(double detectStatus) {
		this.detectStatus = detectStatus;
	}
	public void setLedStatus(double ledStatus) {
		this.ledStatus = ledStatus;
	}
	public void setValveStatus(double valveStatus) {
		this.valveStatus = valveStatus;
	}
	public void setLedTemp(double ledTemp) {
		this.ledTemp = ledTemp;
	}
	public void setFirstSanmpleCon370(double firstSanmpleCon370) {
		this.firstSanmpleCon370 = firstSanmpleCon370;
	}
	public void setSecondSanmpleCon370(double secondSanmpleCon370) {
		this.secondSanmpleCon370 = secondSanmpleCon370;
	}
//	public void setCarbonCon370(double carbonCon370) {
//		this.carbonCon370 = carbonCon370;
//	}
	public void setFirstSanmpleCon470(double firstSanmpleCon470) {
		this.firstSanmpleCon470 = firstSanmpleCon470;
	}
	public void setSecondSanmpleCon470(double secondSanmpleCon470) {
		this.secondSanmpleCon470 = secondSanmpleCon470;
	}
//	public void setCarbonCon470(double carbonCon470) {
//		this.carbonCon470 = carbonCon470;
//	}
	public void setFirstSanmpleCon520(double firstSanmpleCon520) {
		this.firstSanmpleCon520 = firstSanmpleCon520;
	}
	public void setSecondSanmpleCon520(double secondSanmpleCon520) {
		this.secondSanmpleCon520 = secondSanmpleCon520;
	}
//	public void setCarbonCon520(double carbonCon520) {
//		this.carbonCon520 = carbonCon520;
//	}
	public void setFirstSanmpleCon590(double firstSanmpleCon590) {
		this.firstSanmpleCon590 = firstSanmpleCon590;
	}
	public void setSecondSanmpleCon590(double secondSanmpleCon590) {
		this.secondSanmpleCon590 = secondSanmpleCon590;
	}
//	public void setCarbonCon590(double carbonCon590) {
//		this.carbonCon590 = carbonCon590;
//	}
	public void setFirstSanmpleCon660(double firstSanmpleCon660) {
		this.firstSanmpleCon660 = firstSanmpleCon660;
	}
	public void setSecondSanmpleCon660(double secondSanmpleCon660) {
		this.secondSanmpleCon660 = secondSanmpleCon660;
	}
//	public void setCarbonCon660(double carbonCon660) {
//		this.carbonCon660 = carbonCon660;
//	}
	public void setFirstSanmpleCon880(double firstSanmpleCon880) {
		this.firstSanmpleCon880 = firstSanmpleCon880;
	}
	public void setSecondSanmpleCon880(double secondSanmpleCon880) {
		this.secondSanmpleCon880 = secondSanmpleCon880;
	}
//	public void setCarbonCon880(double carbonCon880) {
//		this.carbonCon880 = carbonCon880;
//	}
	public void setFirstSanmpleCon950(double firstSanmpleCon950) {
		this.firstSanmpleCon950 = firstSanmpleCon950;
	}
	public void setSecondSanmpleCon950(double secondSanmpleCon950) {
		this.secondSanmpleCon950 = secondSanmpleCon950;
	}
//	public void setCarbonCon950(double carbonCon950) {
//		this.carbonCon950 = carbonCon950;
//	}
	public void setCompCoeffi370(double compCoeffi370) {
		this.compCoeffi370 = compCoeffi370;
	}
	public void setCompCoeffi470(double compCoeffi470) {
		this.compCoeffi470 = compCoeffi470;
	}
	public void setCompCoeffi520(double compCoeffi520) {
		this.compCoeffi520 = compCoeffi520;
	}
	public void setCompCoeffi590(double compCoeffi590) {
		this.compCoeffi590 = compCoeffi590;
	}
	public void setCompCoeffi660(double compCoeffi660) {
		this.compCoeffi660 = compCoeffi660;
	}
	public void setCompCoeffi880(double compCoeffi880) {
		this.compCoeffi880 = compCoeffi880;
	}
	public void setCompCoeffi950(double compCoeffi950) {
		this.compCoeffi950 = compCoeffi950;
	}
	public void setTapeAdvCount(double tapeAdvCount) {
		this.tapeAdvCount = tapeAdvCount;
	}


}
