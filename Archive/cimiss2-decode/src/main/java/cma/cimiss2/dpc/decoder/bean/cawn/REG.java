package cma.cimiss2.dpc.decoder.bean.cawn;

import java.util.Date;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 *	大气成分反应性气体观测资料实体类。解析时，各要素值均从报文直接取值、入库时各字段直接赋值。
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《反应性气体资料数据表》 </a> <br>
 *      要素名称 | 字段编码：<br>
 *      纬度	V05001<br>
		经度	V06001<br>
		测站高度	V07001<br>
		数据记录频率	V15800<br>
		SO2浓度（ppb）	V15810<br>
		NO浓度	V15814<br>
		NO2浓度	V15817<br>
		NOX浓度	V15820<br>
		CO浓度（ppm）	V15823<br>
		O3浓度（ppb）	V15826<br>
		第一通道	V05042_01<br>
		第一通道测量代码	V05045_01<br>
		第二通道	V05042_02<br>
		第二通道测量代码	V05045_02<br>
		第三通道	V05042_03<br>
		第三通道测量代码	V05045_03<br>
		第四通道	V05042_04<br>
		第四通道测量代码	V05045_04<br>
		第五通道	V05042_05<br>
		第五通道测量代码	V05045_05<br>
		第六通道	V05042_06<br>
		第六通道测量代码	V05045_06<br>
		第七通道	V05042_07<br>
		第七通道测量代码	V05045_07<br>
		SO2数据标志	V15802_SO2<br>
		SO2浓度五分钟最大值	V15810_5_005<br>
		SO2浓度五分钟最小值	V15810_5_006<br>
		SO2浓度五分钟标准差	V15810_5_004<br>
		NO数据标志	V15802_NO<br>
		NO浓度五分钟最大值	V15814_5_005<br>
		NO浓度五分钟最小值	V15814_5_006<br>
		NO浓度五分钟标准差	V15814_5_004<br>
		NO2数据标志	V15802_NO2<br>
		NO2浓度五分钟最大值	V15817_5_005<br>
		NO2浓度五分钟最小值	V15817_5_006<br>
		NO2浓度五分钟标准差	V15817_5_004<br>
		NOX数据标志	V15802_NOX<br>
		NOX浓度五分钟最大值	V15820_5_005<br>
		NOX浓度五分钟最小值	V15820_5_006<br>
		NOX浓度五分钟标准差	V15820_5_004<br>
		CO数据标志	V15802_CO<br>
		CO浓度五分钟最大值	V15823_5_005<br>
		CO浓度五分钟最小值	V15823_5_006<br>
		CO浓度五分钟标准差	V15823_5_004<br>
		O3数据标志	V15802_O3<br>
		O3浓度五分钟最大值	V15826_5_005<br>
		O3浓度五分钟最小值	V15826_5_006<br>
		O3浓度五分钟标准差	V15826_5_004<br>
		室内温度	V12001<br>
		SO2仪器SN	V15440_SO2<br>
		SO2仪器状态码	V15441_SO2<br>
		SO2仪器内部温度	V15442_SO2<br>
		SO2仪器腔室温度	V15443_SO2<br>
		SO2仪器压力	V15444_SO2<br>
		SO2仪器样气流量	V15840_SO2<br>
		SO2仪器光电倍增管电压	V15437_SO2<br>
		SO2仪器灯管电压	V15841_SO2<br>
		SO2仪器灯光强度	V15445_SO2<br>
		SO2仪器外部警报	V15438_SO2<br>
		SO2仪器零点	V15448_SO2<br>
		SO2仪器斜率	V15439_SO2<br>
		SO2仪器校准后的SO2浓度	V15811_SO2<br>
		SO2仪器订正标识	V15449_SO2<br>
		NOX仪器SN	V15440_NOX<br>
		NOX仪器状态码	V15441_NOX<br>
		NOX仪器内部温度	V15442_NOX<br>
		NOX仪器腔室温度	V15443_NOX<br>
		NOX仪器光电倍增管温度	V15842_NOX<br>
		NOX仪器转换器温度	V15843_NOX<br>
		NOX仪器压力	V15444_NOX<br>
		NOX仪器样气流量	V15840_NOX<br>
		NOX仪器光电倍增管电压	V15437_NOX<br>
		NOX仪器臭氧发生器流量	V15844_NOX<br>
		NOX仪器外部警报	V15438_NOX<br>
		NO零点	V15448_NO<br>
		NO斜率	V15439_NO<br>
		校准后的NO浓度（ppb）	V15815_NO<br>
		NO订正标识	V15449_NO<br>
		校准后的NO2浓度（ppb）	V15818_NO2<br>
		NO2订正标识	V15449_NO2<br>
		NOX零点	V15448_NOX<br>
		NOX斜率	V15439_NOX<br>
		校准后的NOX浓度（ppb）	V15821_NOX<br>
		NOX订正标识	V15449_NOX<br>
		CO仪器SN	V15440_CO<br>
		CO仪器状态码	V15441_CO<br>
		CO仪器内部温度	V15442_CO<br>
		CO仪器反应室温度	V15443_CO<br>
		CO仪器压力	V15444_CO<br>
		CO仪器样气流量	V15840_CO<br>
		CO仪器偏置电压	V15845_CO<br>
		CO仪器马达速度	V15846_CO<br>
		CO仪器S/R比	V15847_CO<br>
		CO光源强度	V15848_CO<br>
		CO仪器外部警报	V15438_CO<br>
		CO仪器零点	V15448_CO<br>
		CO仪器斜率	V15439_CO<br>
		CO仪器校准后的CO浓度（ppm）	V15824_CO<br>
		CO仪器订正标识	V15449_CO<br>
		O3仪器SN	V15440_O3<br>
		O3仪器状态码	V15441_O3<br>
		O3仪器A单元灯光强度	V15445_O3A<br>
		O3仪器B单元灯光强度	V15445_O3B<br>
		O3仪器A单元噪声	V15446_O3A<br>
		O3仪器B单元噪声	V15446_O3B<br>
		O3仪器A单元流速	V16447_O3A<br>
		O3仪器B单元流速	V15447_O3B<br>
		O3仪器压力	V15444_O3<br>
		O3仪器光座温度	V15849_O3<br>
		O3仪器灯温度	V15850_O3<br>
		O3仪器外部警报	V15438_O3<br>
		O3仪器零点	V15448_O3<br>
		O3仪器斜率	V15439_O3<br>
		O3仪器校准后的O3浓度（ppb）	V15827_O3<br>
		O3仪器订正标识	V15449_O3<br>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月24日 上午11:28:29   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class REG {
	/**
	 * 台站号
	 */
	private String stationNumberChina;
	/**
	 * 经度,单位：度
	 */
	private double longtitude = 999999;
	/**
	 * 纬度，单位:度
	 */
	private double latitude = 999999;
	/**
	 * 测站高度，单位：米
	 */
	private double heightAboveSeaLevel = 999999;
	/**
	 * 记录频率
	 */
	private double recordFrequency = 999998;
	/**
	 * 时间（年月日时分秒）
	 */
	private Date observationTime;
	/**
	 * SO2仪器SN
	 */
	private String SO2_DeviceSN = "999998";
	/**
	 * SO2仪器状态码
	 */
	private String SO2_DeviceStateCode = "999998";
	/**
	 * SO2浓度（ppb）
	 */
	private double SO2_Density = 999998;
	/**
	 * SO2仪器内部温度
	 */
	private double SO2_DeviceInnerTemperature = 999998;
	/**
	 * SO2仪器腔室温度
	 */
	private double SO2_DeviceChamberTemperature = 999998;
	/**
	 * SO2仪器压力
	 */
	private double SO2_DevicePressure = 999998;
	/**
	 * SO2仪器样气流量
	 */
	private double SO2_DeviceGasFlow = 999998;
	/**
	 * SO2仪器光电倍增管电压
	 */
	private double SO2_DevicePhotomultiplierVoltage = 999998;
	/**
	 * SO2仪器灯管电压
	 */
	private double SO2_DeviceTubeVoltage = 999998;
	/**
	 * SO2仪器灯光强度
	 */
	private double SO2_DeviceLightIntensity = 999998;
	/**
	 * SO2仪器外部警报
	 */
	private int SO2_DeviceExternalAlarm = 999998;
	/**
	 * SO2仪器零点
	 */
	private double SO2_DeviceZeroPoint = 999998;
	/**
	 * SO2仪器斜率
	 */
	private double SO2_DeviceSlope = 999998;
	/**
	 * SO2仪器校准后的SO2浓度
	 */
	private double SO2_DeviceCalibrationDensity = 999998; 
	/**
	 * SO2仪器订正标识
	 */
	private int SO2_DeviceRevisedSign = 999998;
	/**
	 * NOX仪器SN
	 */
	private String NOX_DeviceSN = "999998";
	/**
	 * NOX仪器状态码
	 */
	private String NOX_DeviceStateCode = "999998";
	/**
	 * NO浓度
	 */
	private double NO_Density = 999998;
	/**
	 * NO2浓度
	 */
	private double NO2_Density = 999998;
	/**
	 * NOX浓度
	 */
	private double NOX_Density = 999998;
	/**
	 * NOX仪器内部温度
	 */
	private double NOX_DeviceInnerTemperature = 999998;
	/**
	 * NOX仪器腔室温度
	 */
	private double NOX_DeviceChamberTemperature = 999998;
	/**
	 * NOX仪器光电倍增管温度
	 */
	private double NOX_DevicePhotomultiplierTemperature = 999998;
	/**
	 * NOX仪器转换器温度
	 */
	private double NOX_DeviceConverterTemperature = 999998;
	/**
	 * NOX仪器压力
	 */
	private double NOX_DevicePressure = 999998;
	/**
	 * NOX仪器样气流量
	 */
	private double NOX_DeviceGasFlow = 999998;
	/**
	 * NOX仪器光电倍增管电压
	 */
	private double NOX_DevicePhotomultiplierVoltage = 999998;
	/**
	 * NOX仪器臭氧发生器流量
	 */
	private double NOX_DeviceO3GeneratorFlow = 999998;
	/**
	 * NOX仪器外部警报
	 */
	private int NOX_DeviceExternalAlarm = 999998;
	/**
	 * NO零点
	 */
	private double NO_ZeroPoint = 999998;
	/**
	 * NO斜率
	 */
	private double NO_Slope = 999998;
	/**
	 * 校准后的NO浓度（ppb）
	 */
	private double NO_CalibrationDensity = 999998;
	/**
	 * NO订正标识
	 */
	private int NO_RevisedSign = 999998;
	/**
	 * 校准后的NO2浓度（ppb）
	 */
	private double NO2_CalibrationDensity = 999998;
	/**
	 * NO2订正标识
	 */
	private int NO2_RevisedSign = 999998;
	/**
	 * NOX零点
	 */
	private double NOX_ZeroPoint = 999998;
	/**
	 * NOX斜率
	 */
	private double NOX_Slope = 999998;
	/**
	 * 校准后的NOX浓度（ppb）
	 */
	private double NOX_CalibrationDensity = 999998;
	/**
	 * NOX订正标识
	 */
	private int NOX_RevisedSign = 999998;
	/**
	 * CO仪器SN
	 */
	private String CO_DeviceSN = "999998";
	/**
	 * CO仪器状态码
	 */
	private String CO_DeviceStateCode = "999998";
	/**
	 * CO浓度（ppm）
	 */
	private double CO_Density  = 999998;
	/**
	 * CO仪器内部温度
	 */
	private double CO_DeviceInnerTemperature = 999998;
	/**
	 * CO仪器反应室温度
	 */
	private double CO_DeviceChamberTemperature = 999998;
	/**
	 * CO仪器压力
	 */
	private double CO_DevicePressure = 999998;
	/**
	 * CO仪器样气流量
	 */
	private double CO_DeviceGasFlow  = 999998;
	/**
	 * CO仪器偏置电压
	 */
	private double CO_DeviceBiasVoltage = 999998;
	/**
	 * CO仪器马达速度
	 */
	private double CO_DeviceMotorSpeed = 999998;
	/**
	 * CO仪器S/R比
	 */
	private double CO_DeviceSRRatio = 999998;
	/**
	 * CO光源强度
	 */
	private double CO_LightIntensity = 999998;
	/**
	 * CO仪器外部警报
	 */
	private int CO_DeviceExternalAlarm = 999998;
	/**
	 * CO仪器零点
	 */
	private double CO_DeviceZeroPoint = 999998;
	/**
	 * CO仪器斜率
	 */
	private double CO_DeviceSlope = 999998;
	/**
	 * CO仪器校准后的CO浓度（ppm）
	 */
	private double CO_DeviceCalibrationDensity = 999998;
	/**
	 * CO仪器订正标识
	 */
	private int CO_DeviceRevisedSign = 999998;
	/**
	 * O3仪器SN
	 */
	private String O3_DeviceSN = "999998";
	/**
	 * O3仪器状态码
	 */
	private String O3_DeviceStateCode = "999998";
	/**
	 * O3浓度（ppb）
	 */
	private double O3_Density = 999998;
	/**
	 * O3仪器A单元灯光强度
	 */
	private int O3_DeviceALightIntensity = 999998;
	/**
	 * O3仪器B单元灯光强度
	 */
	private int O3_DeviceBLightIntensity = 999998;
	/**
	 * O3仪器A单元噪声
	 */
	private double O3_DeviceANoise = 999998;
	/**
	 * O3仪器B单元噪声
	 */
	private double O3_DeviceBNoise = 999998;
	/**
	 * O3仪器A单元流速
	 */
	private double O3_DeviceAFlowRate = 999998;
	/*
	 * O3仪器B单元流速
	 */
	private double O3_DeviceBFlowRate = 999998;
	/**
	 * O3仪器压力
	 */
	private double O3_DevicePressure = 999998;
	/**
	 * O3仪器光座温度
	 */
	private double O3_DeviceLightSeatTemperature = 999998;
	/**
	 * O3仪器灯温度
	 */
	private double O3_DeviceLightTemperature = 999998;
	/**
	 * O3仪器外部警报
	 */
	private int O3_DeviceExternalAlarm = 999998;
	/**
	 * O3仪器零点
	 */
	private double O3_DeviceZeroPoint = 999998;
	/**
	 * O3仪器斜率
	 */
	private double O3_DeviceSlope = 999998;
	/**
	 * O3仪器校准后的O3浓度（ppb）
	 */
	private double O3_DeviceCalibrationDensity = 999998;
	/**
	 * O3仪器订正标识
	 */
	private int O3_DeviceRevisedSign = 999998;

	
	/**
	 * 项目代码
	 */
	private int itemCode = 999998;
	/**
	 * 年
	 */
	private int year;
	/**
	 * 积日
	 */
	private int dayOfYear;
	/**
	 * 时分
	 */
	private int hhmm;
	/**
	 * SO2浓度五分钟平均值
	 */
	private double SO2_5minAvgDensity = 999998;
	/**
	 * SO2数据标志
	 */
	private String SO2_DataSign = "999998";
	/**
	 * NO浓度五分钟平均值
	 */
	private double NO_5minAvgDensity = 999998;
	/**
	 * NO数据标志
	 */
	private String NO_DataSign = "999998";
	/**
	 * NO2浓度五分钟平均值
	 */
	private double NO2_5minAvgDensity = 999998;
	/**
	 * NO2数据标志
	 */
	private String NO2_DataSign = "999998";
	/**
	 * NOx浓度五分钟平均值
	 */
	private double NOX_5minAvgDensity = 999998;
	/**
	 * NOx数据标志
	 */
	private String NOX_DataSign = "999998";
	/**
	 * CO浓度五分钟平均值
	 */
	private double CO_5minAvgDensity = 999998;
	/**
	 * CO数据标志
	 */
	private String CO_DataSign = "999998";
	/**
	 * O3浓度五分钟平均值
	 */
	private double O3_5minAvgDensity = 999998;
	/**
	 * O3数据标志
	 */
	private String O3_DataSign = "999998";
	/**
	 * SO2浓度五分钟最大值
	 */
	private double SO2_5minMaxDensity = 999998;
	/**
	 * NO浓度五分钟最大值
	 */
	private double NO_5minMaxDensity = 999998;
	/**
	 * NO2浓度五分钟最大值
	 */
	private double NO2_5minMaxDensity = 999998;
	/**
	 * NOx浓度五分钟最大值
	 */
	private double NOX_5minMaxDensity = 999998;
	/*
	 * CO浓度五分钟最大值
	 */
	private double CO_5minMaxDensity = 999998;
	/**
	 * O3浓度五分钟最大值
	 */
	private double O3_5minMaxDensity = 999998;
	/**
	 * SO2浓度五分钟最小值
	 */
	private double SO2_5minMinDensity = 999998;
	/**
	 * NO浓度五分钟最小值
	 */
	private double NO_5minMinDensity = 999998;
	/**
	 * NO2浓度五分钟最小值
	 */
	private double NO2_5minMinDensity = 999998;
	/**
	 * NOx浓度五分钟最小值
	 */
	private double NOX_5minMinDensity = 999998;
	/**
	 * CO浓度五分钟最小值
	 */
	private double CO_5minMinDensity = 999998;
	/**
	 * O3浓度五分钟最小值
	 */
	private double O3_5minMinDensity = 999998;
	/**
	 * SO2浓度五分钟标准差
	 */
	private double SO2_5minDensitySTDEV = 999998;
	/**
	 * NO浓度五分钟标准差
	 */
	private double NO_5minDensitySTDEV = 999998;
	/**
	 * NO2浓度五分钟标准差
	 */
	private double NO2_5minDensitySTDEV = 999998;
	/**
	 * NOx浓度五分钟标准差
	 */
	private double NOX_5minDensitySTDEV = 999998;
	/**
	 * CO浓度五分钟标准差
	 */
	private double CO_5minDensitySTDEV = 999998;
	/**
	 * O3浓度五分钟标准差
	 */
	private double O3_5minDensitySTDEV = 999998;

	
	/**
	 * 第一通道
	 */
	private int channel_1 = 999998;
	/**
	 * 第一通道测量代码
	 */
	private int measureCode_1 = 999998;
	/**
	 * 第二通道
	 */
	private int channel_2 = 999998;
	/**
	 * 第二通道测量代码
	 */
	private int measureCode_2 = 999998;
	/**
	 * 第三通道
	 */
	private int channel_3 = 999998;
	/**
	 * 第三通道测量代码
	 */
	private int measureCode_3 = 999998;
	/**
	 * 第四通道
	 */
	private int channel_4 = 999998;
	/**
	 * 第四通道测量代码
	 */
	private int measureCode_4 = 999998;
	/**
	 * 第五通道
	 */
	private int channel_5 = 999998;
	/**
	 * 第五通道测量代码
	 */
	private int measureCode_5 = 999998;
	/**
	 * 第六通道
	 */
	private int channel_6 = 999998;
	/**
	 * 第六通道测量代码
	 */
	private int measureCode_6 = 999998;
	/**
	 * 第七通道
	 */
	private int channel_7 = 999998;
	/**
	 * 室内温度
	 */
	private double innerTemperature = 999998;
	/**
	 * 第七通道测量代码
	 */
	private int measureCode_7 = 999998;

	
	public Date getObservationTime() {
		return observationTime;
	}
	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}
	public String getSO2_DeviceSN() {
		return SO2_DeviceSN;
	}
	public void setSO2_DeviceSN(String sO2_DeviceSN) {
		SO2_DeviceSN = sO2_DeviceSN;
	}
	public String getSO2_DeviceStateCode() {
		return SO2_DeviceStateCode;
	}
	public void setSO2_DeviceStateCode(String sO2_DeviceStateCode) {
		SO2_DeviceStateCode = sO2_DeviceStateCode;
	}
	public double getSO2_Density() {
		return SO2_Density;
	}
	public void setSO2_Density(double sO2_Density) {
		SO2_Density = sO2_Density;
	}
	public double getSO2_DeviceInnerTemperature() {
		return SO2_DeviceInnerTemperature;
	}
	public void setSO2_DeviceInnerTemperature(double sO2_DeviceInnerTemperature) {
		SO2_DeviceInnerTemperature = sO2_DeviceInnerTemperature;
	}
	public double getSO2_DeviceChamberTemperature() {
		return SO2_DeviceChamberTemperature;
	}
	public void setSO2_DeviceChamberTemperature(double sO2_DeviceChamberTemperature) {
		SO2_DeviceChamberTemperature = sO2_DeviceChamberTemperature;
	}
	public double getSO2_DevicePressure() {
		return SO2_DevicePressure;
	}
	public void setSO2_DevicePressure(double sO2_DevicePressure) {
		SO2_DevicePressure = sO2_DevicePressure;
	}
	public double getSO2_DeviceGasFlow() {
		return SO2_DeviceGasFlow;
	}
	public void setSO2_DeviceGasFlow(double sO2_DeviceGasFlow) {
		SO2_DeviceGasFlow = sO2_DeviceGasFlow;
	}
	public double getSO2_DevicePhotomultiplierVoltage() {
		return SO2_DevicePhotomultiplierVoltage;
	}
	public void setSO2_DevicePhotomultiplierVoltage(double sO2_DevicePhotomultiplierVoltage) {
		SO2_DevicePhotomultiplierVoltage = sO2_DevicePhotomultiplierVoltage;
	}
	public double getSO2_DeviceTubeVoltage() {
		return SO2_DeviceTubeVoltage;
	}
	public void setSO2_DeviceTubeVoltage(double sO2_DeviceTubeVoltage) {
		SO2_DeviceTubeVoltage = sO2_DeviceTubeVoltage;
	}
	public double getSO2_DeviceLightIntensity() {
		return SO2_DeviceLightIntensity;
	}
	public void setSO2_DeviceLightIntensity(double sO2_DeviceLightIntensity) {
		SO2_DeviceLightIntensity = sO2_DeviceLightIntensity;
	}
	public int getSO2_DeviceExternalAlarm() {
		return SO2_DeviceExternalAlarm;
	}
	public void setSO2_DeviceExternalAlarm(int sO2_DeviceExternalAlarm) {
		SO2_DeviceExternalAlarm = sO2_DeviceExternalAlarm;
	}
	public double getSO2_DeviceZeroPoint() {
		return SO2_DeviceZeroPoint;
	}
	public void setSO2_DeviceZeroPoint(double sO2_DeviceZeroPoint) {
		SO2_DeviceZeroPoint = sO2_DeviceZeroPoint;
	}
	public double getSO2_DeviceSlope() {
		return SO2_DeviceSlope;
	}
	public void setSO2_DeviceSlope(double sO2_DeviceSlope) {
		SO2_DeviceSlope = sO2_DeviceSlope;
	}
	public double getSO2_DeviceCalibrationDensity() {
		return SO2_DeviceCalibrationDensity;
	}
	public void setSO2_DeviceCalibrationDensity(double sO2_DeviceCalibrationDensity) {
		SO2_DeviceCalibrationDensity = sO2_DeviceCalibrationDensity;
	}
	public int getSO2_DeviceRevisedSign() {
		return SO2_DeviceRevisedSign;
	}
	public void setSO2_DeviceRevisedSign(int sO2_DeviceRevisedSign) {
		SO2_DeviceRevisedSign = sO2_DeviceRevisedSign;
	}
	public String getNOX_DeviceSN() {
		return NOX_DeviceSN;
	}
	public void setNOX_DeviceSN(String nOX_DeviceSN) {
		NOX_DeviceSN = nOX_DeviceSN;
	}
	public String getNOX_DeviceStateCode() {
		return NOX_DeviceStateCode;
	}
	public void setNOX_DeviceStateCode(String nOX_DeviceStateCode) {
		NOX_DeviceStateCode = nOX_DeviceStateCode;
	}
	public double getNO_Density() {
		return NO_Density;
	}
	public void setNO_Density(double nO_Density) {
		NO_Density = nO_Density;
	}
	public double getNO2_Density() {
		return NO2_Density;
	}
	public void setNO2_Density(double nO2_Density) {
		NO2_Density = nO2_Density;
	}
	public double getNOX_Density() {
		return NOX_Density;
	}
	public void setNOX_Density(double nOX_Density) {
		NOX_Density = nOX_Density;
	}
	public double getNOX_DeviceInnerTemperature() {
		return NOX_DeviceInnerTemperature;
	}
	public void setNOX_DeviceInnerTemperature(double nOX_DeviceInnerTemperature) {
		NOX_DeviceInnerTemperature = nOX_DeviceInnerTemperature;
	}
	public double getNOX_DeviceChamberTemperature() {
		return NOX_DeviceChamberTemperature;
	}
	public void setNOX_DeviceChamberTemperature(double nOX_DeviceChamberTemperature) {
		NOX_DeviceChamberTemperature = nOX_DeviceChamberTemperature;
	}
	public double getNOX_DevicePhotomultiplierTemperature() {
		return NOX_DevicePhotomultiplierTemperature;
	}
	public void setNOX_DevicePhotomultiplierTemperature(double nOX_DevicePhotomultiplierTemperature) {
		NOX_DevicePhotomultiplierTemperature = nOX_DevicePhotomultiplierTemperature;
	}
	public double getNOX_DeviceConverterTemperature() {
		return NOX_DeviceConverterTemperature;
	}
	public void setNOX_DeviceConverterTemperature(double nOX_DeviceConverterTemperature) {
		NOX_DeviceConverterTemperature = nOX_DeviceConverterTemperature;
	}
	public double getNOX_DevicePressure() {
		return NOX_DevicePressure;
	}
	public void setNOX_DevicePressure(double nOX_DevicePressure) {
		NOX_DevicePressure = nOX_DevicePressure;
	}
	public double getNOX_DeviceGasFlow() {
		return NOX_DeviceGasFlow;
	}
	public void setNOX_DeviceGasFlow(double nOX_DeviceGasFlow) {
		NOX_DeviceGasFlow = nOX_DeviceGasFlow;
	}
	public double getNOX_DevicePhotomultiplierVoltage() {
		return NOX_DevicePhotomultiplierVoltage;
	}
	public void setNOX_DevicePhotomultiplierVoltage(double nOX_DevicePhotomultiplierVoltage) {
		NOX_DevicePhotomultiplierVoltage = nOX_DevicePhotomultiplierVoltage;
	}
	public double getNOX_DeviceO3GeneratorFlow() {
		return NOX_DeviceO3GeneratorFlow;
	}
	public void setNOX_DeviceO3GeneratorFlow(double nOX_DeviceO3GeneratorFlow) {
		NOX_DeviceO3GeneratorFlow = nOX_DeviceO3GeneratorFlow;
	}
	public int getNOX_DeviceExternalAlarm() {
		return NOX_DeviceExternalAlarm;
	}
	public void setNOX_DeviceExternalAlarm(int nOX_DeviceExternalAlarm) {
		NOX_DeviceExternalAlarm = nOX_DeviceExternalAlarm;
	}
	public double getNO_ZeroPoint() {
		return NO_ZeroPoint;
	}
	public void setNO_ZeroPoint(double nO_ZeroPoint) {
		NO_ZeroPoint = nO_ZeroPoint;
	}
	public double getNO_Slope() {
		return NO_Slope;
	}
	public void setNO_Slope(double nO_Slope) {
		NO_Slope = nO_Slope;
	}
	public double getNO_CalibrationDensity() {
		return NO_CalibrationDensity;
	}
	public void setNO_CalibrationDensity(double nO_CalibrationDensity) {
		NO_CalibrationDensity = nO_CalibrationDensity;
	}
	public int getNO_RevisedSign() {
		return NO_RevisedSign;
	}
	public void setNO_RevisedSign(int nO_RevisedSign) {
		NO_RevisedSign = nO_RevisedSign;
	}
	public double getNO2_CalibrationDensity() {
		return NO2_CalibrationDensity;
	}
	public void setNO2_CalibrationDensity(double nO2_CalibrationDensity) {
		NO2_CalibrationDensity = nO2_CalibrationDensity;
	}
	public int getNO2_RevisedSign() {
		return NO2_RevisedSign;
	}
	public void setNO2_RevisedSign(int nO2_RevisedSign) {
		NO2_RevisedSign = nO2_RevisedSign;
	}
	public double getNOX_ZeroPoint() {
		return NOX_ZeroPoint;
	}
	public void setNOX_ZeroPoint(double nOX_ZeroPoint) {
		NOX_ZeroPoint = nOX_ZeroPoint;
	}
	public double getNOX_Slope() {
		return NOX_Slope;
	}
	public void setNOX_Slope(double nOX_Slope) {
		NOX_Slope = nOX_Slope;
	}
	public double getNOX_CalibrationDensity() {
		return NOX_CalibrationDensity;
	}
	public void setNOX_CalibrationDensity(double nOX_CalibrationDensity) {
		NOX_CalibrationDensity = nOX_CalibrationDensity;
	}
	public int getNOX_RevisedSign() {
		return NOX_RevisedSign;
	}
	public void setNOX_RevisedSign(int nOX_RevisedSign) {
		NOX_RevisedSign = nOX_RevisedSign;
	}
	public String getCO_DeviceSN() {
		return CO_DeviceSN;
	}
	public void setCO_DeviceSN(String cO_DeviceSN) {
		CO_DeviceSN = cO_DeviceSN;
	}
	public String getCO_DeviceStateCode() {
		return CO_DeviceStateCode;
	}
	public void setCO_DeviceStateCode(String cO_DeviceStateCode) {
		CO_DeviceStateCode = cO_DeviceStateCode;
	}
	public double getCO_Density() {
		return CO_Density;
	}
	public void setCO_Density(double cO_Density) {
		CO_Density = cO_Density;
	}
	public double getCO_DeviceInnerTemperature() {
		return CO_DeviceInnerTemperature;
	}
	public void setCO_DeviceInnerTemperature(double cO_DeviceInnerTemperature) {
		CO_DeviceInnerTemperature = cO_DeviceInnerTemperature;
	}
	public double getCO_DeviceChamberTemperature() {
		return CO_DeviceChamberTemperature;
	}
	public void setCO_DeviceChamberTemperature(double cO_DeviceChamberTemperature) {
		CO_DeviceChamberTemperature = cO_DeviceChamberTemperature;
	}
	public double getCO_DevicePressure() {
		return CO_DevicePressure;
	}
	public void setCO_DevicePressure(double cO_DevicePressure) {
		CO_DevicePressure = cO_DevicePressure;
	}
	public double getCO_DeviceGasFlow() {
		return CO_DeviceGasFlow;
	}
	public void setCO_DeviceGasFlow(double cO_DeviceGasFlow) {
		CO_DeviceGasFlow = cO_DeviceGasFlow;
	}
	public double getCO_DeviceBiasVoltage() {
		return CO_DeviceBiasVoltage;
	}
	public void setCO_DeviceBiasVoltage(double cO_DeviceBiasVoltage) {
		CO_DeviceBiasVoltage = cO_DeviceBiasVoltage;
	}
	public double getCO_DeviceMotorSpeed() {
		return CO_DeviceMotorSpeed;
	}
	public void setCO_DeviceMotorSpeed(double cO_DeviceMotorSpeed) {
		CO_DeviceMotorSpeed = cO_DeviceMotorSpeed;
	}
	public double getCO_DeviceSRRatio() {
		return CO_DeviceSRRatio;
	}
	public void setCO_DeviceSRRatio(double cO_DeviceSRRatio) {
		CO_DeviceSRRatio = cO_DeviceSRRatio;
	}
	public double getCO_LightIntensity() {
		return CO_LightIntensity;
	}
	public void setCO_LightIntensity(double cO_LightIntensity) {
		CO_LightIntensity = cO_LightIntensity;
	}
	public int getCO_DeviceExternalAlarm() {
		return CO_DeviceExternalAlarm;
	}
	public void setCO_DeviceExternalAlarm(int cO_DeviceExternalAlarm) {
		CO_DeviceExternalAlarm = cO_DeviceExternalAlarm;
	}
	public double getCO_DeviceZeroPoint() {
		return CO_DeviceZeroPoint;
	}
	public void setCO_DeviceZeroPoint(double cO_DeviceZeroPoint) {
		CO_DeviceZeroPoint = cO_DeviceZeroPoint;
	}
	public double getCO_DeviceSlope() {
		return CO_DeviceSlope;
	}
	public void setCO_DeviceSlope(double cO_DeviceSlope) {
		CO_DeviceSlope = cO_DeviceSlope;
	}
	public double getCO_DeviceCalibrationDensity() {
		return CO_DeviceCalibrationDensity;
	}
	public void setCO_DeviceCalibrationDensity(double cO_DeviceCalibrationDensity) {
		CO_DeviceCalibrationDensity = cO_DeviceCalibrationDensity;
	}
	public int getCO_DeviceRevisedSign() {
		return CO_DeviceRevisedSign;
	}
	public void setCO_DeviceRevisedSign(int cO_DeviceRevisedSign) {
		CO_DeviceRevisedSign = cO_DeviceRevisedSign;
	}
	public String getO3_DeviceSN() {
		return O3_DeviceSN;
	}
	public void setO3_DeviceSN(String o3_DeviceSN) {
		O3_DeviceSN = o3_DeviceSN;
	}
	public String getO3_DeviceStateCode() {
		return O3_DeviceStateCode;
	}
	public void setO3_DeviceStateCode(String o3_DeviceStateCode) {
		O3_DeviceStateCode = o3_DeviceStateCode;
	}
	public double getO3_Density() {
		return O3_Density;
	}
	public void setO3_Density(double o3_Density) {
		O3_Density = o3_Density;
	}
	public int getO3_DeviceALightIntensity() {
		return O3_DeviceALightIntensity;
	}
	public void setO3_DeviceALightIntensity(int o3_DeviceALightIntensity) {
		O3_DeviceALightIntensity = o3_DeviceALightIntensity;
	}
	public int getO3_DeviceBLightIntensity() {
		return O3_DeviceBLightIntensity;
	}
	public void setO3_DeviceBLightIntensity(int o3_DeviceBLightIntensity) {
		O3_DeviceBLightIntensity = o3_DeviceBLightIntensity;
	}
	public double getO3_DeviceANoise() {
		return O3_DeviceANoise;
	}
	public void setO3_DeviceANoise(double o3_DeviceANoise) {
		O3_DeviceANoise = o3_DeviceANoise;
	}
	public double getO3_DeviceBNoise() {
		return O3_DeviceBNoise;
	}
	public void setO3_DeviceBNoise(double o3_DeviceBNoise) {
		O3_DeviceBNoise = o3_DeviceBNoise;
	}
	public double getO3_DeviceAFlowRate() {
		return O3_DeviceAFlowRate;
	}
	public void setO3_DeviceAFlowRate(double o3_DeviceAFlowRate) {
		O3_DeviceAFlowRate = o3_DeviceAFlowRate;
	}
	public double getO3_DeviceBFlowRate() {
		return O3_DeviceBFlowRate;
	}
	public void setO3_DeviceBFlowRate(double o3_DeviceBFlowRate) {
		O3_DeviceBFlowRate = o3_DeviceBFlowRate;
	}
	public double getO3_DevicePressure() {
		return O3_DevicePressure;
	}
	public void setO3_DevicePressure(double o3_DevicePressure) {
		O3_DevicePressure = o3_DevicePressure;
	}
	public double getO3_DeviceLightSeatTemperature() {
		return O3_DeviceLightSeatTemperature;
	}
	public void setO3_DeviceLightSeatTemperature(double o3_DeviceLightSeatTemperature) {
		O3_DeviceLightSeatTemperature = o3_DeviceLightSeatTemperature;
	}
	public double getO3_DeviceLightTemperature() {
		return O3_DeviceLightTemperature;
	}
	public void setO3_DeviceLightTemperature(double o3_DeviceLightTemperature) {
		O3_DeviceLightTemperature = o3_DeviceLightTemperature;
	}
	public int getO3_DeviceExternalAlarm() {
		return O3_DeviceExternalAlarm;
	}
	public void setO3_DeviceExternalAlarm(int o3_DeviceExternalAlarm) {
		O3_DeviceExternalAlarm = o3_DeviceExternalAlarm;
	}
	public double getO3_DeviceZeroPoint() {
		return O3_DeviceZeroPoint;
	}
	public void setO3_DeviceZeroPoint(double o3_DeviceZeroPoint) {
		O3_DeviceZeroPoint = o3_DeviceZeroPoint;
	}
	public double getO3_DeviceSlope() {
		return O3_DeviceSlope;
	}
	public void setO3_DeviceSlope(double o3_DeviceSlope) {
		O3_DeviceSlope = o3_DeviceSlope;
	}
	public double getO3_DeviceCalibrationDensity() {
		return O3_DeviceCalibrationDensity;
	}
	public void setO3_DeviceCalibrationDensity(double o3_DeviceCalibrationDensity) {
		O3_DeviceCalibrationDensity = o3_DeviceCalibrationDensity;
	}
	public int getO3_DeviceRevisedSign() {
		return O3_DeviceRevisedSign;
	}
	public void setO3_DeviceRevisedSign(int o3_DeviceRevisedSign) {
		O3_DeviceRevisedSign = o3_DeviceRevisedSign;
	}
	public String getStationNumberChina() {
		return stationNumberChina;
	}
	public void setStationNumberChina(String stationNumberChina) {
		this.stationNumberChina = stationNumberChina;
	}
	public double getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getHeightAboveSeaLevel() {
		return heightAboveSeaLevel;
	}
	public void setHeightAboveSeaLevel(double heightAboveSeaLevel) {
		this.heightAboveSeaLevel = heightAboveSeaLevel;
	}
	public double getRecordFrequency() {
		return recordFrequency;
	}
	public void setRecordFrequency(double recordFrequency) {
		this.recordFrequency = recordFrequency;
	}
	public int getItemCode() {
		return itemCode;
	}
	public void setItemCode(int itemCode) {
		this.itemCode = itemCode;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getDayOfYear() {
		return dayOfYear;
	}
	public void setDayOfYear(int dayOfYear) {
		this.dayOfYear = dayOfYear;
	}
	public int getHhmm() {
		return hhmm;
	}
	public void setHhmm(int hhmm) {
		this.hhmm = hhmm;
	}
	public double getSO2_5minAvgDensity() {
		return SO2_5minAvgDensity;
	}
	public void setSO2_5minAvgDensity(double sO2_5minAvgDensity) {
		SO2_5minAvgDensity = sO2_5minAvgDensity;
	}
	public String getSO2_DataSign() {
		return SO2_DataSign;
	}
	public void setSO2_DataSign(String sO2_DataSign) {
		SO2_DataSign = sO2_DataSign;
	}
	public double getNO_5minAvgDensity() {
		return NO_5minAvgDensity;
	}
	public void setNO_5minAvgDensity(double nO_5minAvgDensity) {
		NO_5minAvgDensity = nO_5minAvgDensity;
	}
	public String getNO_DataSign() {
		return NO_DataSign;
	}
	public void setNO_DataSign(String nO_DataSign) {
		NO_DataSign = nO_DataSign;
	}
	public double getNO2_5minAvgDensity() {
		return NO2_5minAvgDensity;
	}
	public void setNO2_5minAvgDensity(double nO2_5minAvgDensity) {
		NO2_5minAvgDensity = nO2_5minAvgDensity;
	}
	public String getNO2_DataSign() {
		return NO2_DataSign;
	}
	public void setNO2_DataSign(String nO2_DataSign) {
		NO2_DataSign = nO2_DataSign;
	}
	public double getNOX_5minAvgDensity() {
		return NOX_5minAvgDensity;
	}
	public void setNOX_5minAvgDensity(double nOX_5minAvgDensity) {
		NOX_5minAvgDensity = nOX_5minAvgDensity;
	}
	public String getNOX_DataSign() {
		return NOX_DataSign;
	}
	public void setNOX_DataSign(String nOX_DataSign) {
		NOX_DataSign = nOX_DataSign;
	}
	public double getCO_5minAvgDensity() {
		return CO_5minAvgDensity;
	}
	public void setCO_5minAvgDensity(double cO_5minAvgDensity) {
		CO_5minAvgDensity = cO_5minAvgDensity;
	}
	public String getCO_DataSign() {
		return CO_DataSign;
	}
	public void setCO_DataSign(String cO_DataSign) {
		CO_DataSign = cO_DataSign;
	}
	public double getO3_5minAvgDensity() {
		return O3_5minAvgDensity;
	}
	public void setO3_5minAvgDensity(double o3_5minAvgDensity) {
		O3_5minAvgDensity = o3_5minAvgDensity;
	}
	public String getO3_DataSign() {
		return O3_DataSign;
	}
	public void setO3_DataSign(String o3_DataSign) {
		O3_DataSign = o3_DataSign;
	}
	public double getSO2_5minMaxDensity() {
		return SO2_5minMaxDensity;
	}
	public void setSO2_5minMaxDensity(double sO2_5minMaxDensity) {
		SO2_5minMaxDensity = sO2_5minMaxDensity;
	}
	public double getNO_5minMaxDensity() {
		return NO_5minMaxDensity;
	}
	public void setNO_5minMaxDensity(double nO_5minMaxDensity) {
		NO_5minMaxDensity = nO_5minMaxDensity;
	}
	public double getNO2_5minMaxDensity() {
		return NO2_5minMaxDensity;
	}
	public void setNO2_5minMaxDensity(double nO2_5minMaxDensity) {
		NO2_5minMaxDensity = nO2_5minMaxDensity;
	}
	public double getNOX_5minMaxDensity() {
		return NOX_5minMaxDensity;
	}
	public void setNOX_5minMaxDensity(double nOX_5minMaxDensity) {
		NOX_5minMaxDensity = nOX_5minMaxDensity;
	}
	public double getCO_5minMaxDensity() {
		return CO_5minMaxDensity;
	}
	public void setCO_5minMaxDensity(double cO_5minMaxDensity) {
		CO_5minMaxDensity = cO_5minMaxDensity;
	}
	public double getO3_5minMaxDensity() {
		return O3_5minMaxDensity;
	}
	public void setO3_5minMaxDensity(double o3_5minMaxDensity) {
		O3_5minMaxDensity = o3_5minMaxDensity;
	}
	public double getSO2_5minMinDensity() {
		return SO2_5minMinDensity;
	}
	public void setSO2_5minMinDensity(double sO2_5minMinDensity) {
		SO2_5minMinDensity = sO2_5minMinDensity;
	}
	public double getNO_5minMinDensity() {
		return NO_5minMinDensity;
	}
	public void setNO_5minMinDensity(double nO_5minMinDensity) {
		NO_5minMinDensity = nO_5minMinDensity;
	}
	public double getNO2_5minMinDensity() {
		return NO2_5minMinDensity;
	}
	public void setNO2_5minMinDensity(double nO2_5minMinDensity) {
		NO2_5minMinDensity = nO2_5minMinDensity;
	}
	public double getNOX_5minMinDensity() {
		return NOX_5minMinDensity;
	}
	public void setNOX_5minMinDensity(double nOX_5minMinDensity) {
		NOX_5minMinDensity = nOX_5minMinDensity;
	}
	public double getCO_5minMinDensity() {
		return CO_5minMinDensity;
	}
	public void setCO_5minMinDensity(double cO_5minMinDensity) {
		CO_5minMinDensity = cO_5minMinDensity;
	}
	public double getO3_5minMinDensity() {
		return O3_5minMinDensity;
	}
	public void setO3_5minMinDensity(double o3_5minMinDensity) {
		O3_5minMinDensity = o3_5minMinDensity;
	}
	public double getSO2_5minDensitySTDEV() {
		return SO2_5minDensitySTDEV;
	}
	public void setSO2_5minDensitySTDEV(double sO2_5minDensitySTDEV) {
		SO2_5minDensitySTDEV = sO2_5minDensitySTDEV;
	}
	public double getNO_5minDensitySTDEV() {
		return NO_5minDensitySTDEV;
	}
	public void setNO_5minDensitySTDEV(double nO_5minDensitySTDEV) {
		NO_5minDensitySTDEV = nO_5minDensitySTDEV;
	}
	public double getNO2_5minDensitySTDEV() {
		return NO2_5minDensitySTDEV;
	}
	public void setNO2_5minDensitySTDEV(double nO2_5minDensitySTDEV) {
		NO2_5minDensitySTDEV = nO2_5minDensitySTDEV;
	}
	public double getNOX_5minDensitySTDEV() {
		return NOX_5minDensitySTDEV;
	}
	public void setNOX_5minDensitySTDEV(double nOX_5minDensitySTDEV) {
		NOX_5minDensitySTDEV = nOX_5minDensitySTDEV;
	}
	public double getCO_5minDensitySTDEV() {
		return CO_5minDensitySTDEV;
	}
	public void setCO_5minDensitySTDEV(double cO_5minDensitySTDEV) {
		CO_5minDensitySTDEV = cO_5minDensitySTDEV;
	}
	public double getO3_5minDensitySTDEV() {
		return O3_5minDensitySTDEV;
	}
	public void setO3_5minDensitySTDEV(double o3_5minDensitySTDEV) {
		O3_5minDensitySTDEV = o3_5minDensitySTDEV;
	}
	public int getChannel_1() {
		return channel_1;
	}
	public void setChannel_1(int channel_1) {
		this.channel_1 = channel_1;
	}
	public int getMeasureCode_1() {
		return measureCode_1;
	}
	public void setMeasureCode_1(int measureCode_1) {
		this.measureCode_1 = measureCode_1;
	}
	public int getChannel_2() {
		return channel_2;
	}
	public void setChannel_2(int channel_2) {
		this.channel_2 = channel_2;
	}
	public int getMeasureCode_2() {
		return measureCode_2;
	}
	public void setMeasureCode_2(int measureCode_2) {
		this.measureCode_2 = measureCode_2;
	}
	public int getChannel_3() {
		return channel_3;
	}
	public void setChannel_3(int channel_3) {
		this.channel_3 = channel_3;
	}
	public int getMeasureCode_3() {
		return measureCode_3;
	}
	public void setMeasureCode_3(int measureCode_3) {
		this.measureCode_3 = measureCode_3;
	}
	public int getChannel_4() {
		return channel_4;
	}
	public void setChannel_4(int channel_4) {
		this.channel_4 = channel_4;
	}
	public int getMeasureCode_4() {
		return measureCode_4;
	}
	public void setMeasureCode_4(int measureCode_4) {
		this.measureCode_4 = measureCode_4;
	}
	public int getChannel_5() {
		return channel_5;
	}
	public void setChannel_5(int channel_5) {
		this.channel_5 = channel_5;
	}
	public int getMeasureCode_5() {
		return measureCode_5;
	}
	public void setMeasureCode_5(int measureCode_5) {
		this.measureCode_5 = measureCode_5;
	}
	public int getChannel_6() {
		return channel_6;
	}
	public void setChannel_6(int channel_6) {
		this.channel_6 = channel_6;
	}
	public int getMeasureCode_6() {
		return measureCode_6;
	}
	public void setMeasureCode_6(int measureCode_6) {
		this.measureCode_6 = measureCode_6;
	}
	public int getChannel_7() {
		return channel_7;
	}
	public void setChannel_7(int channel_7) {
		this.channel_7 = channel_7;
	}
	public double getInnerTemperature() {
		return innerTemperature;
	}
	public void setInnerTemperature(double innerTemperature) {
		this.innerTemperature = innerTemperature;
	}
	public int getMeasureCode_7() {
		return measureCode_7;
	}
	public void setMeasureCode_7(int measureCode_7) {
		this.measureCode_7 = measureCode_7;
	}
	
}
