package cma.cimiss2.dpc.decoder.bean.surf;

import java.util.Date;
/**
 * *****************************************************************************************<br> 
 * <strong>All Rights Reserved By National Meteorological Information Centre (NMIC)</strong><br>
 * *****************************************************************************************<br>
 * 国内地面自动站运行状态和设备信息数据解析类 (解析时，各字段直接从报文取值，入库时直接赋值)
 * <p>
 * 
 * notes:
 * <ul>
 * <li>定义参考了一下文档
 * 	<ol>
 * 		<li> <a href=" ">《国内地面自动站运行状态和设备信息要素表.docx》</a>
 * 	</ol>
 * </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年10月10日 上午11:43:32   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 */
public class AWS {
	/**
	 * NO: 1.1  <br>
	 * nameCN: 区站号 <br>
	 * unit: <br>
	 * BUFR FXY: V01301 <br>
	 * descriptionCN: 5位数字或第1位为字母，第2-5位为数字
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private String stationNumberChina;
	/**
	 * NO: 1.2  <br>
	 * nameCN: 纬度 <br>
	 * unit: 度 <br>
	 * BUFR FXY: V05001 <br>
	 * descriptionCN:
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private double latitude = 999999;
	
	/**
	 * NO: 1.3  <br>
	 * nameCN: 经度 <br>
	 * unit: 度 <br>
	 * BUFR FXY: V06001 <br>
	 * descriptionCN:
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private double longitude = 999999;
	
	/**
	 * NO: 1.4  <br>
	 * nameCN: 资料时间  <br>
	 * unit: <br>
	 * BUFR FXY: V04001,V04002, V04003, V04004  <br>
	 * descriptionCN: 分、秒均为0
	 * decode rule:直接取值。<br>
     * field rule:使用java时间转化工具类进行转化,标准形式为：yyyyMMddHHmmss。
	 */
	private Date ObservationTime;
	
	/**
	 * NO: 2.1  <br>
	 * nameCN: 计算机与子站的通信状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:  整数/Integer	0：正常；1：不正常
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private int Communication = 999999;
	
	/**
	 * NO: 2.2  <br>
	 * nameCN: 气压传感器是否开通  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:  整数/Integer	0：开通；1：未开通
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private int Pressure = 999999;
	
	/**
	 * NO: 2.3  <br>
	 * nameCN: 气温传感器是否开通  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:  整数/Integer	0：开通；1：未开通
	 */
	private int Temperature = 999999;
	
	/**
	 * NO: 2.4 湿球温度传感器是否开通 <br>
	 * nameCN:   <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：开通；1：未开通
	 */
	private int WetbulbTemperature = 999999;
	
	/**
	 * NO: 2.5  <br>
	 * nameCN: 湿敏电容传感器是否开通<br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:  整数/Integer	0：开通；1：未开通
	 */
	private int Humidity = 999999;
	
	/**
	 * NO: 2.6  <br>
	 * nameCN: 风向传感器是否开通  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：开通；1：未开通
	 */
	private int WindDirection = 999999;
	
	/**
	 * NO: 2.7  <br>
	 * nameCN: 风速传感器是否开通 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：开通；1：未开通
	 */
	private int WindSpeed = 999999;
	
	/**
	 * NO: 2.8  <br>
	 * nameCN:  雨量传感器是否开通 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 	整数/Integer	0：开通；1：未开通
	 */
	private int Rainfall = 999999;
	
	/**
	 * NO: 2.9  <br>
	 * nameCN: 感雨传感器是否开通	  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:  	整数/Integer	0：开通；1：未开通
	 */
	private int Rain = 999999;
	
	/**
	 * NO: 2.10  <br>
	 * nameCN:   草面温度传感器是否开通	 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 	整数/Integer	0：开通；1：未开通
	 */
	private int GrassTemperature = 999999;
	
	/**
	 * NO: 2.11  <br>
	 * nameCN:  地面温度传感器是否开通 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:  整数/Integer	0：开通；1：未开通
	 */
	private int GroundTemperature = 999999;
	
	/**
	 * NO: 2.12  <br>
	 * nameCN:   5cm地温传感器是否开通 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:	整数/Integer	0：开通；1：未开通
	 */
	private int GroundTemperature5cm = 999999;
	
	/**
	 * NO: 2.13 <br>
	 * nameCN: 10cm地温传感器是否开通	  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：开通；1：未开通
	 */
	private int GroundTemperature10cm = 999999;
	
	/**
	 * NO: 2.14  <br>
	 * nameCN: 15cm地温传感器是否开通  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：开通；1：未开通
	 */
	private int GroundTemperature15cm = 999999;
	
	/**
	 * NO: 2.15  <br>
	 * nameCN: 20cm地温传感器是否开通  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：开通；1：未开通
	 */
	private int GroundTemperature20cm = 999999;
	
	/**
	 * NO: 2.16  <br>
	 * nameCN: 40cm地温传感器是否开通  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：开通；1：未开通
	 */
	private int GroundTemperature40cm = 999999;
	
	/**
	 * NO: 2.17  <br>
	 * nameCN: 80cm地温传感器是否开通  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：开通；1：未开通
	 */
	private int GroundTemperature80cm = 999999;
	
	/**
	 * NO: 2.18  <br>
	 * nameCN: 160cm地温传感器是否开通  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：开通；1：未开通
	 */
	private int GroundTemperature160cm = 999999;
	
	/**
	 * NO: 2.19  <br>
	 * nameCN: 320cm地温传感器是否开通  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：开通；1：未开通
	 */
	private int GroundTemperature320cm = 999999;
	
	/**
	 * NO: 2.20  <br>
	 * nameCN:  蒸发传感器是否开通 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:  整数/Integer	0：开通；1：未开通
	 */
	private int Evaporation = 999999;
	
	/**
	 * NO: 2.21  <br>
	 * nameCN: 日照传感器是否开通  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：开通；1：未开通
	 */
	private int Sunshine = 999999;
	
	/**
	 * NO: 2.22  <br>
	 * nameCN: 能见度传感器是否开通 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:  整数/Integer	0：开通；1：未开通
	 */
	private int Visibility = 999999;
	
	/**
	 * NO: 2.23  <br>
	 * nameCN:  云量传感器是否开通 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：开通；1：未开通
	 */
	private int CloudAmount = 999999;
	
	/**
	 * NO: 2.24  <br>
	 * nameCN:  云高传感器是否开通  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：开通；1：未开通
	 */
	private int CloudHeight = 999999;
	
	/**
	 * NO: 2.25  <br>
	 * nameCN:  子站是否修改了时钟  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：修改；1：未修改
	 */
	private int SubStationClock = 999999;
	
	/**
	 * NO: 2.26  <br>
	 * nameCN: 采集器数据是否正确读取  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：读取成功；1：读取失败
	 */
	private int DataCollection = 999999;
	
	/**
	 * NO: 2.27  <br>
	 * nameCN:  供电方式 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:  整型	0：市电；1：备份电源；/：不能获取
	 */
	private int PowerSupply = 999999;
	
	/**
	 * NO: 2.28  <br>
	 * nameCN:  采集器主板电压 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:  实型/Decimal	单位：V，保留1位小数，不能获取时，用“999999.0”表示
	 */
	private double MotherboardVoltage = 999999;
	
	/**
	 * NO: 2.29  <br>
	 * nameCN:  采集器主板温度  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 实型/Decimal	单位：V，保留1位小数，不能获取时，用“999999.0”表示
	 */
	private double MotherboardTemperature = 999999;
	
	/**
	 * NO: 2.30  <br>
	 * nameCN:  采集器通讯状态	  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 	整数/ Integer	0：正常；1：不正常
	 */
	private int CollectorCommunication = 999999;
	
	/**
	 * NO: 2.31  <br>
	 * nameCN: 机箱温度  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:  实型/Decimal	单位：℃，保留1位小数，不能获取时，用“999999.0”表示
	 */
	private double ChassisTemperature = 999999;
	
	/**
	 * NO: 2.32  <br>
	 * nameCN:  电源电压 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:	实型/Decimal	单位：V，保留1位小数，不能获取时，用“999999.0”表示
	 */
	private double PowerVoltage = 999999;
	
	/**
	 * NO: 2.33  <br>
	 * nameCN: 太阳直接辐射表是否开通  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：开通；1：未开通
	 */
	private int Solar = 999999;
	
	/**
	 * NO: 2.34 <br>
	 * nameCN: 太阳直接辐射表通风是否正常  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：不正常
	 */
	private int SolarVentilation = 999999;
	
	/**
	 * NO: 2.35  <br>
	 * nameCN: 跟踪器状态是否正常  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：不正常
	 */
	private int Tracker = 999999;
	
	/**
	 * NO: 2.36  <br>
	 * nameCN: 散射辐射表是否开通  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：开通；1：未开通
	 */
	private int Scattering = 999999;
	
	/**
	 * NO: 2.37  <br>
	 * nameCN: 散射辐射表通风是否正常  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：不正常
	 */
	private int ScatteringVentilation = 999999;
	
	/**
	 * NO: 2.38  <br>
	 * nameCN:  总辐射表是否开通 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：开通；1：未开通
	 */
	private int Pyranometer = 999999;
	
	/**
	 * NO: 2.39  <br>
	 * nameCN:   总辐射表通风是否正常 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:整数/ Integer	0：正常；1：不正常
	 */
	private int PyranometerVentilation = 999999;
	
	/**
	 * NO: 2.40  <br>
	 * nameCN: 反射辐射表是否开通  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:	整数/ Integer	0：开通；1：未开通
	 */
	private int Reflection = 999999;
	
	/**
	 * NO: 2.41  <br>
	 * nameCN: 反射辐射表通风是否正常  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：不正常
	 */
	private int ReflectionVentilation = 999999;
	
	/**
	 * NO: 2.42  <br>
	 * nameCN:  大气长波辐射表是否开通	 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:  	整数/ Integer	0：开通；1：未开通
	 */
	private int LongWave = 999999;
	
	/**
	 * NO: 2.43  <br>
	 * nameCN:  大气长波辐射表通风是否正常 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:  	整数/ Integer	0：正常；1：不正常
	 */
	private int LongWaveVentilation = 999999;
	
	/**
	 * NO: 2.44  <br>
	 * nameCN: 大气长波辐射表腔体温度是否正常  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:整数/ Integer	0：正常；1：不正常  
	 */
	private int LongWaveTemperature = 999999;
	
	/**
	 * NO: 2.45  <br>
	 * nameCN:  地球长波辐射表是否开通 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：开通；1：未开通
	 */
	private int EarthWave = 999999;
	
	/**
	 * NO: 2.46  <br>
	 * nameCN: 地球长波辐射表通风是否正常  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：不正常
	 */
	private int EarthWaveVentilation = 999999;
	
	/**
	 * NO: 2.47  <br>
	 * nameCN: 地球长波辐射表腔体温度是否正常  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：不正常
	 */
	private int EarthWaveTemperature = 999999;
	
	/**
	 * NO: 2.48  <br>
	 * nameCN: 紫外辐射表是否开通	  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:整数/ Integer	0：开通；1：未开通
	 */
	private int Ultraviolet = 999999;
	
	/**
	 * NO: 2.49  <br>
	 * nameCN: 紫外辐射表恒温器温度是否正常  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：不正常
	 */
	private int UltravioletTemperature = 999999;
	
	/**
	 * NO: 2.50  <br>
	 * nameCN: 光合有效辐射表是否开通  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:  整数/ Integer	0：开通；1：未开通
	 */
	private int PARMeter = 999999;
	
	/**
	 * NO: 3.1  <br>
	 * nameCN: 设备自检状态	  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:  	整数/Integer	0：正常；1：不正常
	 */
	private int Instrument = 999999;
	
	/**
	 * NO: 3.2  <br>
	 * nameCN:  气候分采自检状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:  整数/Integer	0：正常；1：不正常
	 */
	private int Climate = 999999;
	
	/**
	 * NO: 3.3  <br>
	 * nameCN:地温分采自检状态   <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:  		整数/Integer	0：正常；1：不正常
	 */
	private int GroundTemperature_SelfCheck = 999999;
	
	/**
	 * NO: 3.4  <br>
	 * nameCN:  温湿分采自检状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：不正常
	 */
	private int Humidity_SelfCheck = 999999;
	
	/**
	 * NO: 3.5  <br>
	 * nameCN: 辐射分采自检状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:  整数/ Integer	0：正常；1：不正常;
	 */
	private int Radiation = 999999;		
	
	/**
	 * NO: 4.1  <br>
	 * nameCN:  传感器工作状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int SensorWorking = 999999;
	
	/**
	 * NO: 4.2  <br>
	 * nameCN:  1.5米气温传感器状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int Temperature15dm = 999999;
	
	/**
	 * NO: 4.3  <br>
	 * nameCN: 草面温度传感器状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int GrassSurface = 999999;
	
	/**
	 * NO: 4.4  <br>
	 * nameCN:   地表温度传感器状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:整数/Integer	0：正常；1：异常；2：故障
	 */
	private int Surface = 999999;
	
	/**
	 * NO: 4.5  <br>
	 * nameCN: 5cm地温传感器状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int Ground5cm = 999999;
	
	/**
	 * NO: 4.6  <br>
	 * nameCN:浅层10cm地温传感器的工作状态   <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int Ground10cm = 999999;
	
	/**
	 * NO: 4.7  <br>
	 * nameCN: 浅层15cm地温传感器的工作状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int Ground15cm = 999999;
	
	/**
	 * NO: 4.8  <br>
	 * nameCN: 浅层25cm地温传感器的工作状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int Ground20cm = 999999;
	
	/**
	 * NO: 4.9  <br>
	 * nameCN: 浅层40cm地温传感器的工作状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int Ground40cm = 999999;
	
	/**
	 * NO: 4.10  <br>
	 * nameCN: 浅层80cm地温传感器的工作状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int Ground80cm = 999999;
	
	/**
	 * NO: 4.11  <br>
	 * nameCN: 浅层160cm地温传感器的工作状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int Ground160cm = 999999;
	
	/**
	 * NO: 4.12  <br>
	 * nameCN: 浅层320cm地温传感器的工作状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int Ground320cm = 999999;
	
	/**
	 * NO: 4.13  <br>
	 * nameCN: 液面温度传感器的工作状 态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 	整数/Integer	0：正常；1：异常；2：故障
	 */
	private int SurfaceTemperature = 999999;
	
	/**
	 * NO: 4.14  <br>
	 * nameCN: 冰点温度传感器的工作状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int FreezingTemperature = 999999;
	
	/**
	 * NO: 4.15  <br>
	 * nameCN:  1.5米相对湿度传感器的工作状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int RelativeHumidity15dm = 999999;
	
	/**
	 * NO: 4.16  <br>
	 * nameCN:  风向传感器的工作状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:  整数/Integer	0：正常；1：异常；2：故障
	 */
	private int WindDirection_State = 999999;
	
	/**
	 * NO: 4.17  <br>
	 * nameCN: 风速传感器的工作状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int WindSpeed_State = 999999;
	
	/**
	 * NO: 4.18  <br>
	 * nameCN: 气压传感器的工作状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:  整数/Integer	0：正常；1：异常；2：故障
	 */
	private int Pressure_State = 999999;
	
	/**
	 * NO: 4.19 <br>
	 * nameCN: 雨量传感器(非称重方式)的工作状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:  整数/Integer	0：正常；1：异常；2：故障
	 */
	private int NoWeighingRaingauge = 999999;
	
	/**
	 * NO: 4.20  <br>
	 * nameCN:   称重雨量传感器的工作状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 	 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int WeighingRaingauge = 999999;
	
	/**
	 * NO: 4.21  <br>
	 * nameCN: 蒸发传感器的工作状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:  整数/Integer	0：正常；1：异常；2：故障
	 */
	private int Evaporation_State = 999999;
	
	/**
	 * NO: 4.22  <br>
	 * nameCN:  总辐射表传感器的工作状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；6：未开通
	 */
	private int Pyranometer_State = 999999;
	
	/**
	 * NO: 4.23  <br>
	 * nameCN:   反射辐射表传感器的工作状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；6：未开通
	 */
	private int Reflection_State = 999999;
	
	/**
	 * NO: 4.24  <br>
	 * nameCN:  直接辐射表传感器的工作状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；6：未开通
	 */
	private int Direct = 999999;
	
	/**
	 * NO: 4.25  <br>
	 * nameCN: 散射辐射表传感器的工作状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；6：未开通
	 */
	private int Scattering_State = 999999;
	
	/**
	 * NO: 4.26  <br>
	 * nameCN: 净辐射表传感器的工作状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:整数/ Integer	0：正常；1：异常；2：故障
	 */
	private int NetPyranometer = 999999;
	
	/**
	 * NO: 4.27 <br>
	 * nameCN:  紫外（A+B）辐射表传感器的工作状态	 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:	整数/ Integer	0：正常；6：未开通
	 */
	private int UltravioletAB = 999999;
	
	/**
	 * NO: 4.28  <br>
	 * nameCN:  紫外A辐射表传感器的工作状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；2：故障
	 */
	private int UltravioletA = 999999;
	
	/**
	 * NO: 4.29  <br>
	 * nameCN:  紫外B辐射表传感器的工作状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；2：故障
	 */
	private int UltravioletB = 999999;
	
	/**
	 * NO: 4.30  <br>
	 * nameCN: 光合有效辐射表传感器的工作状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；6：未开通
	 */
	private int PARMeter_State = 999999;
	
	/**
	 * NO: 4.31  <br>
	 * nameCN: 大气长波辐射表传感器的工作状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；6：未开通
	 */
	private int LongWave_State = 999999;
	
	/**
	 * NO: 4.32  <br>
	 * nameCN:地面长波辐射表传感器的工作状态   <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；6：未开通
	 */
	private int GroundWave = 999999;
	
	/**
	 * NO: 4.33  <br>
	 * nameCN:  日照传感器的工作状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int Sunshine_State = 999999;
	
	/**
	 * NO: 4.34  <br>
	 * nameCN:  云高传感器的工作状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int CloudHeight_State = 999999;
	
	/**
	 * NO: 4.35  <br>
	 * nameCN: 云量传感器仪的工作状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int CloudAmount_State = 999999;
	
	/**
	 * NO: 4.36  <br>
	 * nameCN:  云状传感器仪的工作状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int CloudForm = 999999;
	
	/**
	 * NO: 4.37  <br>
	 * nameCN: 能见度仪的工作状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int VisibilityInstrument = 999999;
	
	/**
	 * NO: 4.38  <br>
	 * nameCN:  天气现象仪的工作状态	 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int PhenomenonInstrument = 999999;
	
	/**
	 * NO: 4.39  <br>
	 * nameCN: 天线结冰传感器的工作状态	  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int AntennaIcing = 999999;
	
	/**
	 * NO: 4.40  <br>
	 * nameCN: 路面状况传感器的工作状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int RoadCondition = 999999;
	
	/**
	 * NO: 5.1  <br>
	 * nameCN: 外接电源  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	6：交流；7：直流；8：未接外部电源
	 */
	private int ExternalPower = 999999;
	
	/**
	 * NO: 5.2 <br>
	 * nameCN:  气候分采外接电源状态	  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	6：交流；7：直流；8：未接外部电源
	 */
	private int Climate_State = 999999;
	
	/**
	 * NO: 5.3  <br>
	 * nameCN:  地温分采外接电源状态	 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	6：交流；7：直流；8：未接外部电源
	 */
	private int GroundTemperature_PowerState = 999999;
	
	/**
	 * NO: 5.4  <br>
	 * nameCN:   温湿分采外接电源状态	 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	6：交流；7：直流；8：未接外部电源
	 */
	private int Humidity_PowerState = 999999;
	
	/**
	 * NO: 5.5  <br>
	 * nameCN: 辐射分采外接电源状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	6：交流；7：直流；8：未接外部电源
	 */
	private int Radiation_PowerState = 999999;
	
	/**
	 * NO: 5.6  <br>
	 * nameCN: 设备/主采主板电压状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；3：偏高；4：偏低
	 */
	private int InstrumentVoltage = 999999;
	
	/**
	 * NO: 5.7  <br>
	 * nameCN: 气候分采的主板电压状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；3：偏高；4：偏低
	 */
	private int ClimateVoltage = 999999;

	/**
	 * NO: 5.8  <br>
	 * nameCN: 地温分采的主板电压状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；3：偏高；4：偏低
	 */
	private int GroundTemperatureVoltage = 999999;
	
	/**
	 * NO: 5.9  <br>
	 * nameCN: 温湿分采的主板电压状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；3：偏高；4：偏低
	 */
	private int HumidityVoltage = 999999;
	
	/**
	 * NO: 5.10  <br>
	 * nameCN:  辐射分采的主板电压状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；3：偏高；4：偏低
	 */
	private int RadiationVoltage = 999999;
	
	/**
	 * NO: 5.11  <br>
	 * nameCN: 图像主采主板工作电压状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；3：偏高；4：偏低 
	 */
	private int ImageVoltage = 999999;
	
	/**
	 * NO: 5.12  <br>
	 * nameCN: 蓄电池电压状态	  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:  	整数/Integer	0：正常；3：偏高；4：偏低；5：停止
	 */
	private int BatteryVoltage = 999999;
	
	/**
	 * NO: 5.13  <br>
	 * nameCN: AC-DC电压状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；3：偏高；4：偏低；5：停止
	 */
	private int ACDCVoltage = 999999;
	
	/**
	 * NO: 5.14  <br>
	 * nameCN: 遮阳板工作电压状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；3：偏高；4：偏低；5：停止
	 */
	private int VisorVoltage = 999999;
	
	/**
	 * NO: 5.15  <br>
	 * nameCN: 旋转云台工作电压状态	  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:  	整数/Integer	0：正常；3：偏高；4：偏低；5：停止
	 */
	private int TiltHeadVoltage = 999999;
	
	/**
	 * NO: 5.16  <br>
	 * nameCN: 设备/主采 工作电流状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；3：偏高；4：偏低；5：停止
	 */
	private int InstrumentCurrent = 999999;
	
	/**
	 * NO: 5.17  <br>
	 * nameCN:气温分采的工作电流状态   <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；3：偏高；4：偏低；5：停止
	 */
	private int TemperatureCurrent = 999999;
	
	/**
	 * NO: 5.18  <br>
	 * nameCN:  地温分采的工作电流状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；3：偏高；4：偏低；5：停止
	 */
	private int GroundTemperatureCurrent = 999999;
	
	/**
	 * NO: 5.19  <br>
	 * nameCN: 温湿分采的工作电流状态	  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；3：偏高；4：偏低；5：停止
	 */
	private int HumidityCurrent = 999999;
	
	/**
	 * NO: 5.20  <br>
	 * nameCN:  辐射分采的工作电流状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；3：偏高；4：偏低；5：停止
	 */
	private int RadiationCurrent = 999999;
	
	/**
	 * NO: 5.21  <br>
	 * nameCN: 太阳能电池板状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	长度1字节，取值为0或2
	 */
	private int SolarPanels = 999999;
	
	/**
	 * NO: 6.1  <br>
	 * nameCN: 设备/主采主板环境温度状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；3：偏高；4：偏低
	 */
	private int Instrument_TemperatureState = 999999;
	
	/**
	 * NO: 6.2  <br>
	 * nameCN: 气温分采的主板温度状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；3：偏高；4：偏低
	 */
	private int TemperatureState = 999999;
	
	/**
	 * NO: 6.3  <br>
	 * nameCN:  地温分采的主板温度状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；3：偏高；4：偏低
	 */
	private int GroundTemperature_State = 999999;
	
	/**
	 * NO: 6.4  <br>
	 * nameCN: 温湿分采的主板温度状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；3：偏高；4：偏低
	 */
	private int Humidity_State = 999999;
	
	/**
	 * NO: 6.5  <br>
	 * nameCN:  辐射分采的主板温度状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；3：偏高；4：偏低
	 */
	private int RadiationMotherboard = 999999;
	
	/**
	 * NO: 6.6  <br>
	 * nameCN: 探测器温度状态	  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 	整数/Integer	0：正常；3：偏高；4：偏低
	 */
	private int Probe = 999999;
	
	/**
	 * NO: 6.7  <br>
	 * nameCN:  腔体温度状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；3：偏高；4：偏低
	 */
	private int Chassis = 999999;
	
	/**
	 * NO: 6.8  <br>
	 * nameCN: 总辐射表腔体温度状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；
	 */
	private int PyranometerCavity = 999999;
	
	/**
	 * NO: 6.9  <br>
	 * nameCN:  反射辐射表腔体温度状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；
	 */
	private int ReflectionCavity = 999999;
	
	/**
	 * NO: 6.10  <br>
	 * nameCN: 直接辐射表腔体温度状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；
	 */
	private int DirectCavity = 999999;
	
	/**
	 * NO: 6.11  <br>
	 * nameCN:  散射辐射表腔体温度状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；
	 */
	private int ScatteringCavity = 999999;
	
	/**
	 * NO: 6.12  <br>
	 * nameCN:  净辐射表腔体温度状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；
	 */
	private int NetPyranometerCavity = 999999;
	
	/**
	 * NO: 6.13  <br>
	 * nameCN:  紫外（A+B）辐射表腔体温度状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；
	 */
	private int UltravioletCavityAB = 999999;
	
	/**
	 * NO: 6.14  <br>
	 * nameCN: 紫外A辐射表腔体温度状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；
	 */
	private int UltravioletCavityA = 999999;
	
	/**
	 * NO: 6.15  <br>
	 * nameCN: 紫外B辐射表腔体温度状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:整数/ Integer	0：正常；1：异常；
	 */
	private int UltravioletCavityB = 999999;
	
	/**
	 * NO: 6.16  <br>
	 * nameCN: 光合有效辐射表腔体温度状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 	整数/ Integer	0：正常；1：异常；
	 */
	private int PARMeterCavity = 999999;
	
	/**
	 * NO: 6.17  <br>
	 * nameCN: 大气长波辐射表腔体温度状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；
	 */
	private int LongWaveCavity = 999999;
	
	/**
	 * NO: 6.18  <br>
	 * nameCN: 地面长波辐射表腔体温度状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；
	 */
	private int GroundWaveCavity = 999999;
	
	/**
	 * NO: 6.19  <br>
	 * nameCN: 恒温器温度状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；
	 */
	private int Thermostat = 999999;
	
	/**
	 * NO: 6.20  <br>
	 * nameCN:  总辐射表恒温器温度状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；
	 */
	private int PyranometerThermostat = 999999;
	
	/**
	 * NO: 6.21  <br>
	 * nameCN:   反射辐射表恒温器温度状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；
	 */
	private int ReflectionThermostat = 999999;
	
	/**
	 * NO: 6.22  <br>
	 * nameCN: 直接辐射表恒温器温度状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；
	 */
	private int DirectThermostat = 999999;
	
	/**
	 * NO: 6.23  <br>
	 * nameCN: 散射辐射表恒温器温度状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；
	 */
	private int ScatteringThermostat = 999999;
	
	/**
	 * NO: 6.24  <br>
	 * nameCN:净辐射表恒温器温度状态  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；
	 */
	private int NetPyranometerThermostat = 999999;
	
	/**
	 * NO: 6.25  <br>
	 * nameCN: 紫外（A+B）辐射表恒温器温度状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；
	 */
	private int UltravioletThermostatAB = 999999;
	
	/**
	 * NO: 6.26  <br>
	 * nameCN: 紫外A辐射表恒温器温度状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；
	 */
	private int UltravioletThermostatA = 999999;
	
	/**
	 * NO: 6.27  <br>
	 * nameCN: 紫外B辐射表恒温器温度状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；
	 */
	private int UltravioletThermostatB = 999999;
	
	/**
	 * NO: 6.28  <br>
	 * nameCN: 光合有效辐射表恒温器温度状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；
	 */
	private int PARMeterThermostat = 999999;
	
	/**
	 * NO: 6.29  <br>
	 * nameCN:大气长波辐射表恒温器温度状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 		整数/ Integer	0：正常；1：异常；
	 */
	private int LongWaveThermostat = 999999;
	
	/**
	 * NO: 6.30  <br>
	 * nameCN:  地面长波辐射表恒温器温度状态<br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；
	 */
	private int GroundWaveThermostat = 999999;
	
	/**
	 * NO: 6.31  <br>
	 * nameCN: 机箱温度状态	 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 	整数/ Integer	0：正常；3：偏高；4：偏低
	 */
	private int ChassisTemperature_State = 999999;
	
	/**
	 * NO: 7.1  <br>
	 * nameCN: 设备加热 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；2：故障；3：偏高；4：偏低
	 */
	private int EquipmentHeating = 999999;
	
	/**
	 * NO: 7.2  <br>
	 * nameCN: 发射器加热 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；2：故障；3：偏高；4：偏低
	 */
	private int TransmitterHeating = 999999;
	
	/**
	 * NO: 7.3  <br>
	 * nameCN: 接收器加热 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；2：故障；3：偏高；4：偏低
	 */
	private int ReceiverHeating = 999999;
	
	/**
	 * NO: 7.4  <br>
	 * nameCN: 相机加热 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；2：故障；3：偏高；4：偏低
	 */
	private int CameraHeating = 999999;
	
	/**
	 * NO: 7.5  <br>
	 * nameCN: 摄像机加热 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；2：故障；3：偏高；4：偏低
	 */
	private int VideoCameraHeating = 999999;
	
	/**
	 * NO: 8.1  <br>
	 * nameCN: 设备通风状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；2：故障；3：偏高；4：偏低
	 */
	private int Instrument_State = 999999;
	
	/**
	 * NO: 8.2  <br>
	 * nameCN: 发射器通风状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；2：故障；3：偏高；4：偏低
	 */
	private int Transmitter = 999999;
	
	/**
	 * NO: 8.3  <br>
	 * nameCN:  接收器通风状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；2：故障；3：偏高；4：偏低
	 */
	private int Receiver = 999999;
	
	/**
	 * NO: 8.4  <br>
	 * nameCN:  通风罩通风状态<br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；2：异常；3：偏高；4：偏低
	 */
	private int Hood = 999999;
	
	/**
	 * NO: 8.5  <br>
	 * nameCN:气温观测通风罩速度  <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；2：异常；3：偏高；4：偏低
	 */
	private int HoodSpeed = 999999;
	
	/**
	 * NO: 8.6  <br>
	 * nameCN: 总辐射表通风状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；
	 */
	private int Pyranometer_AirState = 999999;
	
	/**
	 * NO: 8.7  <br>
	 * nameCN: 反射辐射表通风状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；
	 */
	private int Reflection_AirState = 999999;
	
	/**
	 * NO: 8.8  <br>
	 * nameCN:  直接辐射表通风状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；
	 */
	private int Direct_AirState = 999999;
	
	/**
	 * NO: 8.9  <br>
	 * nameCN: 散射辐射表通风状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；
	 */
	private int Scattering_AirState = 999999;
	
	/**
	 * NO: 8.10  <br>
	 * nameCN: 净辐射表通风状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:	整数/ Integer	0：正常；1：异常；
	 */
	private int NetPyranometer_State = 999999;
	
	/**
	 * NO: 8.11  <br>
	 * nameCN: 紫外（A+B）辐射表通风状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；
	 */
	private int UltravioletAB_AirState = 999999;
	
	/**
	 * NO: 8.12  <br>
	 * nameCN: 紫外A辐射表通风状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；
	 */
	private int UltravioletA_AirState = 999999;
	
	/**
	 * NO: 8.13  <br>
	 * nameCN: 紫外B辐射表通风状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；
	 */
	private int UltravioletB_AirState = 999999;
	
	/**
	 * NO: 8.14  <br>
	 * nameCN: 光合有效辐射表通风状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；
	 */
	private int PARMeter_AirState = 999999;
	
	/**
	 * NO: 8.15  <br>
	 * nameCN:  <br>
	 * unit: <br>
	 * BUFR FXY: 大气长波辐射表通风状态  <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；
	 */
	private int LongWave_AirState = 999999;
	
	/**
	 * NO: 8.16  <br>
	 * nameCN: 地面长波辐射表通风状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；
	 */
	private int GroundWave_AirState = 999999;
	
	/**
	 * NO: 9.1  <br>
	 * nameCN:  设备（主采）到串口服务器或PC终端连接的通信状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；
	 */
	private int Connection = 999999;
	
	/**
	 * NO: 9.2  <br>
	 * nameCN: 总线状态（设备与分采或其他智能传感器的总线状态指示） <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int Bus = 999999;
	
	/**
	 * NO: 9.3  <br>
	 * nameCN: RS232/485/422状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int RS232_485_422 = 999999;
	
	/**
	 * NO: 9.4  <br>
	 * nameCN: 气温分采的RS232/485/422状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int TemperatureRS232_485_422 = 999999;
	
	/**
	 * NO: 9.5  <br>
	 * nameCN: 地温分采的RS232/485/422状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int GroundTemperatureRS232_485_422 = 999999;
	
	/**
	 * NO: 9.6  <br>
	 * nameCN: 温湿分采的RS232/485/422状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int HumidityRS232_485_422 = 999999;
	
	/**
	 * NO: 9.7  <br>
	 * nameCN: 辐射分采的RS232/485/422状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；2：故障
	 */
	private int RadiationRS232_485_422 = 999999;
	
	/**
	 * NO: 9.8  <br>
	 * nameCN: RJ45/LAN通信状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 	整数/Integer	0：正常；1：异常；2：故障
	 */
	private int RJ45_LAN = 999999;
	
	/**
	 * NO: 9.9  <br>
	 * nameCN: 卫星通信状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int Satellite = 999999;
	
	/**
	 * NO: 9.10  <br>
	 * nameCN: 无线通信状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int Wireless = 999999;
	
	/**
	 * NO: 9.11  <br>
	 * nameCN: 光纤通信状态	 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:	整数/Integer	0：正常；1：异常；2：故障
	 */
	private int OpticalFiber = 999999;
	
	/**
	 * NO: 10.1  <br>
	 * nameCN: 窗口污染情况 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；6：轻微；7：一般；8：重度；
	 */
	private int Window = 999999;
	
	/**
	 * NO: 10.2  <br>
	 * nameCN: 探测器污染情况 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；6：轻微；7：一般；8：重度；
	 */
	private int Detector = 999999;
	
	/**
	 * NO: 10.3  <br>
	 * nameCN: 相机镜头污染情况 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；6：轻微；7：一般；8：重度；
	 */
	private int CameraLens = 999999;
	
	/**
	 * NO: 10.4  <br>
	 * nameCN: 摄像机镜头污染情况 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；6：轻微；7：一般；8：重度；
	 */
	private int VideoCameraLens = 999999;
	
	/**
	 * NO: 11.1  <br>
	 * nameCN: 发射器能量 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；3：偏高；4：偏低，
	 */
	private int TransmitterPower = 999999;
	
	/**
	 * NO: 11.2  <br>
	 * nameCN:接收器状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int Receiver_State = 999999;
	
	/**
	 * NO: 11.3  <br>
	 * nameCN: 发射器状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int Transmitter_State = 999999;
	
	/**
	 * NO: 11.4  <br>
	 * nameCN:遮阳板工作状况 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int Visor = 999999;
	
	/**
	 * NO: 11.5  <br>
	 * nameCN: 旋转云台工作状况<br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int TiltHead = 999999;
	
	/**
	 * NO: 11.6  <br>
	 * nameCN: 摄像机工作状况 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int VideoCamera = 999999;
	
	/**
	 * NO: 11.7  <br>
	 * nameCN: 相机工作状况 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int Camera = 999999;
	
	/**
	 * NO: 11.8  <br>
	 * nameCN: 跟踪器状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:	整数/Integer	0：正常；1：异常；
	 */
	private int Tracker_State = 999999;
	
	/**
	 * NO: 11.9  <br>
	 * nameCN: 采集器运行状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int Collector = 999999;
	
	/**
	 * NO: 11.10  <br>
	 * nameCN: 气温分采的采集器运行状态<br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int Temperature_State = 999999;
	
	/**
	 * NO: 11.11  <br>
	 * nameCN:  地温分采的采集器运行状态<br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int GroundTemperature_WorkState = 999999;
	
	/**
	 * NO: 11.12  <br>
	 * nameCN: 温湿分采的采集器运行状态	<br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int Humidity_WorkState = 999999;
	
	/**
	 * NO: 11.13  <br>
	 * nameCN: 辐射分采的采集器运行状态<br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；2：故障
	 */
	private int Radiation_WorkState = 999999;
	
	/**
	 * NO: 11.14  <br>
	 * nameCN:AD状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int AD = 999999;
	
	/**
	 * NO: 11.15  <br>
	 * nameCN:气温分采的AD状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int TemperatureAD = 999999;
	
	/**
	 * NO: 11.16  <br>
	 * nameCN: 地温分采的AD状态	<br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int GroundTemperatureAD = 999999;
	
	/**
	 * NO: 11.17  <br>
	 * nameCN: 温湿分采的AD状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int HumidityAD = 999999;
	
	/**
	 * NO: 11.18  <br>
	 * nameCN: 辐射分采的AD状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；2：故障
	 */
	private int RadiationAD = 999999;
	
	/**
	 * NO: 11.19  <br>
	 * nameCN: 计数器状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int Counter = 999999;
	
	/**
	 * NO: 11.20  <br>
	 * nameCN:气温分采的计数器状态	 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:	整数/Integer	0：正常；1：异常；2：故障
	 */
	private int TemperatureCounter = 999999;
	
	/**
	 * NO: 11.21  <br>
	 * nameCN:地温分采的计数器状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int GroundTemperatureCounter = 999999;
			
	/**
	 * NO: 11.22  <br>
	 * nameCN: 温湿分采的计数器状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int HumidityCounter = 999999;
	
	/**
	 * NO: 11.23  <br>
	 * nameCN: 辐射分采的计数器状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；2：故障
	 */
	private int RadiationCounter = 999999;
	
	/**
	 * NO: 11.24  <br>
	 * nameCN: 门状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int Gate_State = 999999;

	/**
	 * NO: 11.25  <br>
	 * nameCN: 气温分采的门状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int TemperatureGate = 999999;
			
	/**
	 * NO: 11.26  <br>
	 * nameCN: 地温分采的门状态	 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int GroundTemperatureGate = 999999;
	
	/**
	 * NO: 11.27  <br>
	 * nameCN: 地温分采的门状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int HumidityGate = 999999;
	
	/**
	 * NO: 11.28  <br>
	 * nameCN: 辐射分采的门状态<br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/ Integer	0：正常；1：异常；2：故障
	 */
	private int RadiationGate = 999999;
	
	/**
	 * NO: 11.29  <br>
	 * nameCN: 进水状态<br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；
	 */	
	private int Water = 999999;
	
	/**
	 * NO: 11.30  <br>
	 * nameCN: 移位状态<br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；
	 */	
	private int Displacement = 999999;
	
	/**
	 * NO: 11.31  <br>
	 * nameCN: 水位状态<br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常：2：故障；3：偏高；4：偏低；
	 */	
	private int WaterLevel = 999999;

	/**
	 * NO: 11.32  <br>
	 * nameCN: 称重传感器盛水桶水位状态<br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常：2：故障；3：偏高；4：偏低；
	 */	
	private int WeighingSensor = 999999;
	
	/**
	 * NO: 11.33  <br>
	 * nameCN: 蒸发池（皿）水位状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常：2：故障；3：偏高；4：偏低；
	 */	
	private int EvaporationPonds = 999999;
	
	/**
	 * NO: 11.34  <br>
	 * nameCN: 外存储卡状态<br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常：2：故障； 4：偏低；
	 */	
	private int ExternalMemoryCard = 999999;
	
	/**
	 * NO: 11.35  <br>
	 * nameCN: 部件转速状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常：2：故障；3：偏高；4：偏低；
	 */		
	private int PartsSpeed = 999999;
			
	/**
	 * NO: 11.36  <br>
	 * nameCN: 部件振动频率状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:	整数/Integer	0：正常：2：故障；3：偏高；4：偏低；
	 */
	private int PartsVibrationFrequency = 999999;
		
	/**
	 * NO: 11.37  <br>
	 * nameCN: 定位辅助设备工作状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:	整数/Integer	0：正常；1：异常；2：故障
	 */
	private int PositioningAid = 999999;
	
	/**
	 * NO: 11.38  <br>
	 * nameCN:对时辅助设备工作状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 整数/Integer	0：正常；1：异常；2：故障
	 */
	private int CalibrationTimeEquipment = 999999;
			
	/**
	 * NO: 12.1  <br>
	 * nameCN:设备状态 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 字符串/String	自由文本，最大位数255
	 */
	private String InstrumentStatus = "999999";
			
	/**
	 * NO: 12.2  <br>
	 * nameCN: 设备名称 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 字符串/String	自由文本，最大位数255
	 */
	private String InstrumentName = "999999";
			
	/**
	 * NO: 12.3  <br>
	 * nameCN:设备路径 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 字符串/String	自由文本，最大位数255
	 */
	private String InstrumentPath = "999999";
			
	/**
	 * NO: 12.4  <br>
	 * nameCN:观测员 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 字符串/String	自由文本，最大位数255
	 */
	private String Observer = "999999";
			
	/**
	 * NO: 12.5  <br>
	 * nameCN: 开始时间 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 字符串/String	格式：YYYYMMDDHHmmss
	 */
	private String StartTime = "999999";
			
	/**
	 * NO: 12.6  <br>
	 * nameCN:结束时间 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 字符串/String	格式：YYYYMMDDHHmmss
	 */
	private String EndTime = "999999";
			
	/**
	 * NO: 12.7  <br>
	 * nameCN: 操作内容<br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 字符串/String	自由文本，最大位数5000
	 */
	private String OperationContent = "999999";
	
	/**
	 * NO: 13.1  <br>
	 * nameCN: 台站名称<br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 字符串/String	自由文本，最大位数255
	 */
	private String StationName = "999999";
			
	/**
	 * NO: 13.2  <br>
	 * nameCN: 设备名称<br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 字符串/String	自由文本，最大位数255
	 */
	private String InstrumentName_Maintain = "999999";
	
	/**
	 * NO: 13.3  <br>
	 * nameCN:	故障时间	 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 字符串/String	格式：YYYYMMDDHHmmss
	 */
	private String Downtime = "999999";
	
	/**
	 * NO: 13.4  <br>
	 * nameCN:故障现象 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN:	字符串/String	自由文本，最大位数5000 
	 */
	private String FaultPhenomenon = "999999";
		
	/**
	 * NO: 13.5  <br>
	 * nameCN: 故障类型<br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 字符串/String	自由文本，最大位数255
	 */
	private String FaultType = "999999";
	
	/**
	 * NO: 13.6  <br>
	 * nameCN:故障原因 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 	字符串/String	自由文本，最大位数255
	 */
	private String FaultCause = "999999";
	
	/**
	 * NO: 13.7  <br>
	 * nameCN:维修情况 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 字符串/String	自由文本，最大位数5000
	 */
	private String Maintenance = "999999";
		 	
	/**
	 * NO: 13.8  <br>
	 * nameCN:维修人 <br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 字符串/String	自由文本，最大位数20
	 */
	private String MaintenanceMan = "999999";
			
	/**
	 * NO: 13.9  <br>
	 * nameCN: 	维修开始时间<br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 字符串/String	格式：YYYYMMDDHHmmss
	 */
	private String MaintenanceStartTime = "999999";
	
	/**
	 * NO: 13.10  <br>
	 * nameCN: 维修结束时间<br>
	 * unit: <br>
	 * BUFR FXY:   <br>
	 * descriptionCN: 字符串/String	格式：YYYYMMDDHHmmss
	 */
	private String MaintenanceEndtTime = "999999";

	public String getStationNumberChina() {
		return stationNumberChina;
	}

	public void setStationNumberChina(String stationNumberChina) {
		this.stationNumberChina = stationNumberChina;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public Date getObservationTime() {
		return ObservationTime;
	}

	public void setObservationTime(Date observationTime) {
		ObservationTime = observationTime;
	}

	public int getCommunication() {
		return Communication;
	}

	public void setCommunication(int communication) {
		Communication = communication;
	}

	public int getPressure() {
		return Pressure;
	}

	public void setPressure(int pressure) {
		Pressure = pressure;
	}

	public int getTemperature() {
		return Temperature;
	}

	public void setTemperature(int temperature) {
		Temperature = temperature;
	}

	public int getWetbulbTemperature() {
		return WetbulbTemperature;
	}

	public void setWetbulbTemperature(int wetbulbTemperature) {
		WetbulbTemperature = wetbulbTemperature;
	}

	public int getHumidity() {
		return Humidity;
	}

	public void setHumidity(int humidity) {
		Humidity = humidity;
	}

	public int getWindDirection() {
		return WindDirection;
	}

	public void setWindDirection(int windDirection) {
		WindDirection = windDirection;
	}

	public int getWindSpeed() {
		return WindSpeed;
	}

	public void setWindSpeed(int windSpeed) {
		WindSpeed = windSpeed;
	}

	public int getRainfall() {
		return Rainfall;
	}

	public void setRainfall(int rainfall) {
		Rainfall = rainfall;
	}

	public int getRain() {
		return Rain;
	}

	public void setRain(int rain) {
		Rain = rain;
	}

	public int getGrassTemperature() {
		return GrassTemperature;
	}

	public void setGrassTemperature(int grassTemperature) {
		GrassTemperature = grassTemperature;
	}

	public int getGroundTemperature() {
		return GroundTemperature;
	}

	public void setGroundTemperature(int groundTemperature) {
		GroundTemperature = groundTemperature;
	}

	public int getGroundTemperature5cm() {
		return GroundTemperature5cm;
	}

	public void setGroundTemperature5cm(int groundTemperature5cm) {
		GroundTemperature5cm = groundTemperature5cm;
	}

	public int getGroundTemperature10cm() {
		return GroundTemperature10cm;
	}

	public void setGroundTemperature10cm(int groundTemperature10cm) {
		GroundTemperature10cm = groundTemperature10cm;
	}

	public int getGroundTemperature15cm() {
		return GroundTemperature15cm;
	}

	public void setGroundTemperature15cm(int groundTemperature15cm) {
		GroundTemperature15cm = groundTemperature15cm;
	}

	public int getGroundTemperature20cm() {
		return GroundTemperature20cm;
	}

	public void setGroundTemperature20cm(int groundTemperature20cm) {
		GroundTemperature20cm = groundTemperature20cm;
	}

	public int getGroundTemperature40cm() {
		return GroundTemperature40cm;
	}

	public void setGroundTemperature40cm(int groundTemperature40cm) {
		GroundTemperature40cm = groundTemperature40cm;
	}

	public int getGroundTemperature80cm() {
		return GroundTemperature80cm;
	}

	public void setGroundTemperature80cm(int groundTemperature80cm) {
		GroundTemperature80cm = groundTemperature80cm;
	}

	public int getGroundTemperature160cm() {
		return GroundTemperature160cm;
	}

	public void setGroundTemperature160cm(int groundTemperature160cm) {
		GroundTemperature160cm = groundTemperature160cm;
	}

	public int getGroundTemperature320cm() {
		return GroundTemperature320cm;
	}

	public void setGroundTemperature320cm(int groundTemperature320cm) {
		GroundTemperature320cm = groundTemperature320cm;
	}

	public int getEvaporation() {
		return Evaporation;
	}

	public void setEvaporation(int evaporation) {
		Evaporation = evaporation;
	}

	public int getSunshine() {
		return Sunshine;
	}

	public void setSunshine(int sunshine) {
		Sunshine = sunshine;
	}

	public int getVisibility() {
		return Visibility;
	}

	public void setVisibility(int visibility) {
		Visibility = visibility;
	}

	public int getCloudAmount() {
		return CloudAmount;
	}

	public void setCloudAmount(int cloudAmount) {
		CloudAmount = cloudAmount;
	}

	public int getCloudHeight() {
		return CloudHeight;
	}

	public void setCloudHeight(int cloudHeight) {
		CloudHeight = cloudHeight;
	}

	public int getSubStationClock() {
		return SubStationClock;
	}

	public void setSubStationClock(int subStationClock) {
		SubStationClock = subStationClock;
	}

	public int getDataCollection() {
		return DataCollection;
	}

	public void setDataCollection(int dataCollection) {
		DataCollection = dataCollection;
	}

	public int getPowerSupply() {
		return PowerSupply;
	}

	public void setPowerSupply(int powerSupply) {
		PowerSupply = powerSupply;
	}

	public double getMotherboardVoltage() {
		return MotherboardVoltage;
	}

	public void setMotherboardVoltage(double motherboardVoltage) {
		MotherboardVoltage = motherboardVoltage;
	}

	public double getMotherboardTemperature() {
		return MotherboardTemperature;
	}

	public void setMotherboardTemperature(double motherboardTemperature) {
		MotherboardTemperature = motherboardTemperature;
	}

	public int getCollectorCommunication() {
		return CollectorCommunication;
	}

	public void setCollectorCommunication(int collectorCommunication) {
		CollectorCommunication = collectorCommunication;
	}

	public double getChassisTemperature() {
		return ChassisTemperature;
	}

	public void setChassisTemperature(double chassisTemperature) {
		ChassisTemperature = chassisTemperature;
	}

	public double getPowerVoltage() {
		return PowerVoltage;
	}

	public void setPowerVoltage(double powerVoltage) {
		PowerVoltage = powerVoltage;
	}

	public int getSolar() {
		return Solar;
	}

	public void setSolar(int solar) {
		Solar = solar;
	}

	public int getSolarVentilation() {
		return SolarVentilation;
	}

	public void setSolarVentilation(int solarVentilation) {
		SolarVentilation = solarVentilation;
	}

	public int getTracker() {
		return Tracker;
	}

	public void setTracker(int tracker) {
		Tracker = tracker;
	}

	public int getScattering() {
		return Scattering;
	}

	public void setScattering(int scattering) {
		Scattering = scattering;
	}

	public int getScatteringVentilation() {
		return ScatteringVentilation;
	}

	public void setScatteringVentilation(int scatteringVentilation) {
		ScatteringVentilation = scatteringVentilation;
	}

	public int getPyranometer() {
		return Pyranometer;
	}

	public void setPyranometer(int pyranometer) {
		Pyranometer = pyranometer;
	}

	public int getPyranometerVentilation() {
		return PyranometerVentilation;
	}

	public void setPyranometerVentilation(int pyranometerVentilation) {
		PyranometerVentilation = pyranometerVentilation;
	}

	public int getReflection() {
		return Reflection;
	}

	public void setReflection(int reflection) {
		Reflection = reflection;
	}

	public int getReflectionVentilation() {
		return ReflectionVentilation;
	}

	public void setReflectionVentilation(int reflectionVentilation) {
		ReflectionVentilation = reflectionVentilation;
	}

	public int getLongWave() {
		return LongWave;
	}

	public void setLongWave(int longWave) {
		LongWave = longWave;
	}

	public int getLongWaveVentilation() {
		return LongWaveVentilation;
	}

	public void setLongWaveVentilation(int longWaveVentilation) {
		LongWaveVentilation = longWaveVentilation;
	}

	public int getLongWaveTemperature() {
		return LongWaveTemperature;
	}

	public void setLongWaveTemperature(int longWaveTemperature) {
		LongWaveTemperature = longWaveTemperature;
	}

	public int getEarthWave() {
		return EarthWave;
	}

	public void setEarthWave(int earthWave) {
		EarthWave = earthWave;
	}

	public int getEarthWaveVentilation() {
		return EarthWaveVentilation;
	}

	public void setEarthWaveVentilation(int earthWaveVentilation) {
		EarthWaveVentilation = earthWaveVentilation;
	}

	public int getEarthWaveTemperature() {
		return EarthWaveTemperature;
	}

	public void setEarthWaveTemperature(int earthWaveTemperature) {
		EarthWaveTemperature = earthWaveTemperature;
	}

	public int getUltraviolet() {
		return Ultraviolet;
	}

	public void setUltraviolet(int ultraviolet) {
		Ultraviolet = ultraviolet;
	}

	public int getUltravioletTemperature() {
		return UltravioletTemperature;
	}

	public void setUltravioletTemperature(int ultravioletTemperature) {
		UltravioletTemperature = ultravioletTemperature;
	}

	public int getPARMeter() {
		return PARMeter;
	}

	public void setPARMeter(int pARMeter) {
		PARMeter = pARMeter;
	}

	public int getInstrument() {
		return Instrument;
	}

	public void setInstrument(int instrument) {
		Instrument = instrument;
	}

	public int getClimate() {
		return Climate;
	}

	public void setClimate(int climate) {
		Climate = climate;
	}

	public int getGroundTemperature_SelfCheck() {
		return GroundTemperature_SelfCheck;
	}

	public void setGroundTemperature_SelfCheck(int groundTemperature_SelfCheck) {
		GroundTemperature_SelfCheck = groundTemperature_SelfCheck;
	}

	public int getHumidity_SelfCheck() {
		return Humidity_SelfCheck;
	}

	public void setHumidity_SelfCheck(int humidity_SelfCheck) {
		Humidity_SelfCheck = humidity_SelfCheck;
	}

	public int getRadiation() {
		return Radiation;
	}

	public void setRadiation(int radiation) {
		Radiation = radiation;
	}

	public int getSensorWorking() {
		return SensorWorking;
	}

	public void setSensorWorking(int sensorWorking) {
		SensorWorking = sensorWorking;
	}

	public int getTemperature15dm() {
		return Temperature15dm;
	}

	public void setTemperature15dm(int temperature15dm) {
		Temperature15dm = temperature15dm;
	}

	public int getGrassSurface() {
		return GrassSurface;
	}

	public void setGrassSurface(int grassSurface) {
		GrassSurface = grassSurface;
	}

	public int getSurface() {
		return Surface;
	}

	public void setSurface(int surface) {
		Surface = surface;
	}

	public int getGround5cm() {
		return Ground5cm;
	}

	public void setGround5cm(int ground5cm) {
		Ground5cm = ground5cm;
	}

	public int getGround10cm() {
		return Ground10cm;
	}

	public void setGround10cm(int ground10cm) {
		Ground10cm = ground10cm;
	}

	public int getGround15cm() {
		return Ground15cm;
	}

	public void setGround15cm(int ground15cm) {
		Ground15cm = ground15cm;
	}

	public int getGround20cm() {
		return Ground20cm;
	}

	public void setGround20cm(int ground20cm) {
		Ground20cm = ground20cm;
	}

	public int getGround40cm() {
		return Ground40cm;
	}

	public void setGround40cm(int ground40cm) {
		Ground40cm = ground40cm;
	}

	public int getGround80cm() {
		return Ground80cm;
	}

	public void setGround80cm(int ground80cm) {
		Ground80cm = ground80cm;
	}

	public int getGround160cm() {
		return Ground160cm;
	}

	public void setGround160cm(int ground160cm) {
		Ground160cm = ground160cm;
	}

	public int getGround320cm() {
		return Ground320cm;
	}

	public void setGround320cm(int ground320cm) {
		Ground320cm = ground320cm;
	}

	public int getSurfaceTemperature() {
		return SurfaceTemperature;
	}

	public void setSurfaceTemperature(int surfaceTemperature) {
		SurfaceTemperature = surfaceTemperature;
	}

	public int getFreezingTemperature() {
		return FreezingTemperature;
	}

	public void setFreezingTemperature(int freezingTemperature) {
		FreezingTemperature = freezingTemperature;
	}

	public int getRelativeHumidity15dm() {
		return RelativeHumidity15dm;
	}

	public void setRelativeHumidity15dm(int relativeHumidity15dm) {
		RelativeHumidity15dm = relativeHumidity15dm;
	}

	public int getWindDirection_State() {
		return WindDirection_State;
	}

	public void setWindDirection_State(int windDirection_State) {
		WindDirection_State = windDirection_State;
	}

	public int getWindSpeed_State() {
		return WindSpeed_State;
	}

	public void setWindSpeed_State(int windSpeed_State) {
		WindSpeed_State = windSpeed_State;
	}

	public int getPressure_State() {
		return Pressure_State;
	}

	public void setPressure_State(int pressure_State) {
		Pressure_State = pressure_State;
	}

	public int getNoWeighingRaingauge() {
		return NoWeighingRaingauge;
	}

	public void setNoWeighingRaingauge(int noWeighingRaingauge) {
		NoWeighingRaingauge = noWeighingRaingauge;
	}

	public int getWeighingRaingauge() {
		return WeighingRaingauge;
	}

	public void setWeighingRaingauge(int weighingRaingauge) {
		WeighingRaingauge = weighingRaingauge;
	}

	public int getEvaporation_State() {
		return Evaporation_State;
	}

	public void setEvaporation_State(int evaporation_State) {
		Evaporation_State = evaporation_State;
	}

	public int getPyranometer_State() {
		return Pyranometer_State;
	}

	public void setPyranometer_State(int pyranometer_State) {
		Pyranometer_State = pyranometer_State;
	}

	public int getReflection_State() {
		return Reflection_State;
	}

	public void setReflection_State(int reflection_State) {
		Reflection_State = reflection_State;
	}

	public int getDirect() {
		return Direct;
	}

	public void setDirect(int direct) {
		Direct = direct;
	}

	public int getScattering_State() {
		return Scattering_State;
	}

	public void setScattering_State(int scattering_State) {
		Scattering_State = scattering_State;
	}

	public int getNetPyranometer() {
		return NetPyranometer;
	}

	public void setNetPyranometer(int netPyranometer) {
		NetPyranometer = netPyranometer;
	}

	public int getUltravioletAB() {
		return UltravioletAB;
	}

	public void setUltravioletAB(int ultravioletAB) {
		UltravioletAB = ultravioletAB;
	}

	public int getUltravioletA() {
		return UltravioletA;
	}

	public void setUltravioletA(int ultravioletA) {
		UltravioletA = ultravioletA;
	}

	public int getUltravioletB() {
		return UltravioletB;
	}

	public void setUltravioletB(int ultravioletB) {
		UltravioletB = ultravioletB;
	}

	public int getPARMeter_State() {
		return PARMeter_State;
	}

	public void setPARMeter_State(int pARMeter_State) {
		PARMeter_State = pARMeter_State;
	}

	public int getLongWave_State() {
		return LongWave_State;
	}

	public void setLongWave_State(int longWave_State) {
		LongWave_State = longWave_State;
	}

	public int getGroundWave() {
		return GroundWave;
	}

	public void setGroundWave(int groundWave) {
		GroundWave = groundWave;
	}

	public int getSunshine_State() {
		return Sunshine_State;
	}

	public void setSunshine_State(int sunshine_State) {
		Sunshine_State = sunshine_State;
	}

	public int getCloudHeight_State() {
		return CloudHeight_State;
	}

	public void setCloudHeight_State(int cloudHeight_State) {
		CloudHeight_State = cloudHeight_State;
	}

	public int getCloudAmount_State() {
		return CloudAmount_State;
	}

	public void setCloudAmount_State(int cloudAmount_State) {
		CloudAmount_State = cloudAmount_State;
	}

	public int getCloudForm() {
		return CloudForm;
	}

	public void setCloudForm(int cloudForm) {
		CloudForm = cloudForm;
	}

	public int getVisibilityInstrument() {
		return VisibilityInstrument;
	}

	public void setVisibilityInstrument(int visibilityInstrument) {
		VisibilityInstrument = visibilityInstrument;
	}

	public int getPhenomenonInstrument() {
		return PhenomenonInstrument;
	}

	public void setPhenomenonInstrument(int phenomenonInstrument) {
		PhenomenonInstrument = phenomenonInstrument;
	}

	public int getAntennaIcing() {
		return AntennaIcing;
	}

	public void setAntennaIcing(int antennaIcing) {
		AntennaIcing = antennaIcing;
	}

	public int getRoadCondition() {
		return RoadCondition;
	}

	public void setRoadCondition(int roadCondition) {
		RoadCondition = roadCondition;
	}

	public int getExternalPower() {
		return ExternalPower;
	}

	public void setExternalPower(int externalPower) {
		ExternalPower = externalPower;
	}

	public int getClimate_State() {
		return Climate_State;
	}

	public void setClimate_State(int climate_State) {
		Climate_State = climate_State;
	}

	public int getGroundTemperature_PowerState() {
		return GroundTemperature_PowerState;
	}

	public void setGroundTemperature_PowerState(int groundTemperature_PowerState) {
		GroundTemperature_PowerState = groundTemperature_PowerState;
	}

	public int getHumidity_PowerState() {
		return Humidity_PowerState;
	}

	public void setHumidity_PowerState(int humidity_PowerState) {
		Humidity_PowerState = humidity_PowerState;
	}

	public int getRadiation_PowerState() {
		return Radiation_PowerState;
	}

	public void setRadiation_PowerState(int radiation_PowerState) {
		Radiation_PowerState = radiation_PowerState;
	}

	public int getInstrumentVoltage() {
		return InstrumentVoltage;
	}

	public void setInstrumentVoltage(int instrumentVoltage) {
		InstrumentVoltage = instrumentVoltage;
	}

	public int getClimateVoltage() {
		return ClimateVoltage;
	}

	public void setClimateVoltage(int climateVoltage) {
		ClimateVoltage = climateVoltage;
	}

	public int getGroundTemperatureVoltage() {
		return GroundTemperatureVoltage;
	}

	public void setGroundTemperatureVoltage(int groundTemperatureVoltage) {
		GroundTemperatureVoltage = groundTemperatureVoltage;
	}

	public int getHumidityVoltage() {
		return HumidityVoltage;
	}

	public void setHumidityVoltage(int humidityVoltage) {
		HumidityVoltage = humidityVoltage;
	}

	public int getRadiationVoltage() {
		return RadiationVoltage;
	}

	public void setRadiationVoltage(int radiationVoltage) {
		RadiationVoltage = radiationVoltage;
	}

	public int getImageVoltage() {
		return ImageVoltage;
	}

	public void setImageVoltage(int imageVoltage) {
		ImageVoltage = imageVoltage;
	}

	public int getBatteryVoltage() {
		return BatteryVoltage;
	}

	public void setBatteryVoltage(int batteryVoltage) {
		BatteryVoltage = batteryVoltage;
	}

	public int getACDCVoltage() {
		return ACDCVoltage;
	}

	public void setACDCVoltage(int aCDCVoltage) {
		ACDCVoltage = aCDCVoltage;
	}

	public int getVisorVoltage() {
		return VisorVoltage;
	}

	public void setVisorVoltage(int visorVoltage) {
		VisorVoltage = visorVoltage;
	}

	public int getTiltHeadVoltage() {
		return TiltHeadVoltage;
	}

	public void setTiltHeadVoltage(int tiltHeadVoltage) {
		TiltHeadVoltage = tiltHeadVoltage;
	}

	public int getInstrumentCurrent() {
		return InstrumentCurrent;
	}

	public void setInstrumentCurrent(int instrumentCurrent) {
		InstrumentCurrent = instrumentCurrent;
	}

	public int getTemperatureCurrent() {
		return TemperatureCurrent;
	}

	public void setTemperatureCurrent(int temperatureCurrent) {
		TemperatureCurrent = temperatureCurrent;
	}

	public int getGroundTemperatureCurrent() {
		return GroundTemperatureCurrent;
	}

	public void setGroundTemperatureCurrent(int groundTemperatureCurrent) {
		GroundTemperatureCurrent = groundTemperatureCurrent;
	}

	public int getHumidityCurrent() {
		return HumidityCurrent;
	}

	public void setHumidityCurrent(int humidityCurrent) {
		HumidityCurrent = humidityCurrent;
	}

	public int getRadiationCurrent() {
		return RadiationCurrent;
	}

	public void setRadiationCurrent(int radiationCurrent) {
		RadiationCurrent = radiationCurrent;
	}

	public int getSolarPanels() {
		return SolarPanels;
	}

	public void setSolarPanels(int solarPanels) {
		SolarPanels = solarPanels;
	}

	public int getInstrument_TemperatureState() {
		return Instrument_TemperatureState;
	}

	public void setInstrument_TemperatureState(int instrument_TemperatureState) {
		Instrument_TemperatureState = instrument_TemperatureState;
	}

	public int getTemperatureState() {
		return TemperatureState;
	}

	public void setTemperatureState(int temperatureState) {
		TemperatureState = temperatureState;
	}

	public int getGroundTemperature_State() {
		return GroundTemperature_State;
	}

	public void setGroundTemperature_State(int groundTemperature_State) {
		GroundTemperature_State = groundTemperature_State;
	}

	public int getHumidity_State() {
		return Humidity_State;
	}

	public void setHumidity_State(int humidity_State) {
		Humidity_State = humidity_State;
	}

	public int getRadiationMotherboard() {
		return RadiationMotherboard;
	}

	public void setRadiationMotherboard(int radiationMotherboard) {
		RadiationMotherboard = radiationMotherboard;
	}

	public int getProbe() {
		return Probe;
	}

	public void setProbe(int probe) {
		Probe = probe;
	}

	public int getChassis() {
		return Chassis;
	}

	public void setChassis(int chassis) {
		Chassis = chassis;
	}

	public int getPyranometerCavity() {
		return PyranometerCavity;
	}

	public void setPyranometerCavity(int pyranometerCavity) {
		PyranometerCavity = pyranometerCavity;
	}

	public int getReflectionCavity() {
		return ReflectionCavity;
	}

	public void setReflectionCavity(int reflectionCavity) {
		ReflectionCavity = reflectionCavity;
	}

	public int getDirectCavity() {
		return DirectCavity;
	}

	public void setDirectCavity(int directCavity) {
		DirectCavity = directCavity;
	}

	public int getScatteringCavity() {
		return ScatteringCavity;
	}

	public void setScatteringCavity(int scatteringCavity) {
		ScatteringCavity = scatteringCavity;
	}

	public int getNetPyranometerCavity() {
		return NetPyranometerCavity;
	}

	public void setNetPyranometerCavity(int netPyranometerCavity) {
		NetPyranometerCavity = netPyranometerCavity;
	}

	public int getUltravioletCavityAB() {
		return UltravioletCavityAB;
	}

	public void setUltravioletCavityAB(int ultravioletCavityAB) {
		UltravioletCavityAB = ultravioletCavityAB;
	}

	public int getUltravioletCavityA() {
		return UltravioletCavityA;
	}

	public void setUltravioletCavityA(int ultravioletCavityA) {
		UltravioletCavityA = ultravioletCavityA;
	}

	public int getUltravioletCavityB() {
		return UltravioletCavityB;
	}

	public void setUltravioletCavityB(int ultravioletCavityB) {
		UltravioletCavityB = ultravioletCavityB;
	}

	public int getPARMeterCavity() {
		return PARMeterCavity;
	}

	public void setPARMeterCavity(int pARMeterCavity) {
		PARMeterCavity = pARMeterCavity;
	}

	public int getLongWaveCavity() {
		return LongWaveCavity;
	}

	public void setLongWaveCavity(int longWaveCavity) {
		LongWaveCavity = longWaveCavity;
	}

	public int getGroundWaveCavity() {
		return GroundWaveCavity;
	}

	public void setGroundWaveCavity(int groundWaveCavity) {
		GroundWaveCavity = groundWaveCavity;
	}

	public int getThermostat() {
		return Thermostat;
	}

	public void setThermostat(int thermostat) {
		Thermostat = thermostat;
	}

	public int getPyranometerThermostat() {
		return PyranometerThermostat;
	}

	public void setPyranometerThermostat(int pyranometerThermostat) {
		PyranometerThermostat = pyranometerThermostat;
	}

	public int getReflectionThermostat() {
		return ReflectionThermostat;
	}

	public void setReflectionThermostat(int reflectionThermostat) {
		ReflectionThermostat = reflectionThermostat;
	}

	public int getDirectThermostat() {
		return DirectThermostat;
	}

	public void setDirectThermostat(int directThermostat) {
		DirectThermostat = directThermostat;
	}

	public int getScatteringThermostat() {
		return ScatteringThermostat;
	}

	public void setScatteringThermostat(int scatteringThermostat) {
		ScatteringThermostat = scatteringThermostat;
	}

	public int getNetPyranometerThermostat() {
		return NetPyranometerThermostat;
	}

	public void setNetPyranometerThermostat(int netPyranometerThermostat) {
		NetPyranometerThermostat = netPyranometerThermostat;
	}

	public int getUltravioletThermostatAB() {
		return UltravioletThermostatAB;
	}

	public void setUltravioletThermostatAB(int ultravioletThermostatAB) {
		UltravioletThermostatAB = ultravioletThermostatAB;
	}

	public int getUltravioletThermostatA() {
		return UltravioletThermostatA;
	}

	public void setUltravioletThermostatA(int ultravioletThermostatA) {
		UltravioletThermostatA = ultravioletThermostatA;
	}

	public int getUltravioletThermostatB() {
		return UltravioletThermostatB;
	}

	public void setUltravioletThermostatB(int ultravioletThermostatB) {
		UltravioletThermostatB = ultravioletThermostatB;
	}

	public int getPARMeterThermostat() {
		return PARMeterThermostat;
	}

	public void setPARMeterThermostat(int pARMeterThermostat) {
		PARMeterThermostat = pARMeterThermostat;
	}

	public int getLongWaveThermostat() {
		return LongWaveThermostat;
	}

	public void setLongWaveThermostat(int longWaveThermostat) {
		LongWaveThermostat = longWaveThermostat;
	}

	public int getGroundWaveThermostat() {
		return GroundWaveThermostat;
	}

	public void setGroundWaveThermostat(int groundWaveThermostat) {
		GroundWaveThermostat = groundWaveThermostat;
	}

	public int getChassisTemperature_State() {
		return ChassisTemperature_State;
	}

	public void setChassisTemperature_State(int chassisTemperature_State) {
		ChassisTemperature_State = chassisTemperature_State;
	}

	public int getEquipmentHeating() {
		return EquipmentHeating;
	}

	public void setEquipmentHeating(int equipmentHeating) {
		EquipmentHeating = equipmentHeating;
	}

	public int getTransmitterHeating() {
		return TransmitterHeating;
	}

	public void setTransmitterHeating(int transmitterHeating) {
		TransmitterHeating = transmitterHeating;
	}

	public int getReceiverHeating() {
		return ReceiverHeating;
	}

	public void setReceiverHeating(int receiverHeating) {
		ReceiverHeating = receiverHeating;
	}

	public int getCameraHeating() {
		return CameraHeating;
	}

	public void setCameraHeating(int cameraHeating) {
		CameraHeating = cameraHeating;
	}

	public int getVideoCameraHeating() {
		return VideoCameraHeating;
	}

	public void setVideoCameraHeating(int videoCameraHeating) {
		VideoCameraHeating = videoCameraHeating;
	}

	public int getInstrument_State() {
		return Instrument_State;
	}

	public void setInstrument_State(int instrument_State) {
		Instrument_State = instrument_State;
	}

	public int getTransmitter() {
		return Transmitter;
	}

	public void setTransmitter(int transmitter) {
		Transmitter = transmitter;
	}

	public int getReceiver() {
		return Receiver;
	}

	public void setReceiver(int receiver) {
		Receiver = receiver;
	}

	public int getHood() {
		return Hood;
	}

	public void setHood(int hood) {
		Hood = hood;
	}

	public int getHoodSpeed() {
		return HoodSpeed;
	}

	public void setHoodSpeed(int hoodSpeed) {
		HoodSpeed = hoodSpeed;
	}

	public int getPyranometer_AirState() {
		return Pyranometer_AirState;
	}

	public void setPyranometer_AirState(int pyranometer_AirState) {
		Pyranometer_AirState = pyranometer_AirState;
	}

	public int getReflection_AirState() {
		return Reflection_AirState;
	}

	public void setReflection_AirState(int reflection_AirState) {
		Reflection_AirState = reflection_AirState;
	}

	public int getDirect_AirState() {
		return Direct_AirState;
	}

	public void setDirect_AirState(int direct_AirState) {
		Direct_AirState = direct_AirState;
	}

	public int getScattering_AirState() {
		return Scattering_AirState;
	}

	public void setScattering_AirState(int scattering_AirState) {
		Scattering_AirState = scattering_AirState;
	}

	public int getNetPyranometer_State() {
		return NetPyranometer_State;
	}

	public void setNetPyranometer_State(int netPyranometer_State) {
		NetPyranometer_State = netPyranometer_State;
	}

	public int getUltravioletAB_AirState() {
		return UltravioletAB_AirState;
	}

	public void setUltravioletAB_AirState(int ultravioletAB_AirState) {
		UltravioletAB_AirState = ultravioletAB_AirState;
	}

	public int getUltravioletA_AirState() {
		return UltravioletA_AirState;
	}

	public void setUltravioletA_AirState(int ultravioletA_AirState) {
		UltravioletA_AirState = ultravioletA_AirState;
	}

	public int getUltravioletB_AirState() {
		return UltravioletB_AirState;
	}

	public void setUltravioletB_AirState(int ultravioletB_AirState) {
		UltravioletB_AirState = ultravioletB_AirState;
	}

	public int getPARMeter_AirState() {
		return PARMeter_AirState;
	}

	public void setPARMeter_AirState(int pARMeter_AirState) {
		PARMeter_AirState = pARMeter_AirState;
	}

	public int getLongWave_AirState() {
		return LongWave_AirState;
	}

	public void setLongWave_AirState(int longWave_AirState) {
		LongWave_AirState = longWave_AirState;
	}

	public int getGroundWave_AirState() {
		return GroundWave_AirState;
	}

	public void setGroundWave_AirState(int groundWave_AirState) {
		GroundWave_AirState = groundWave_AirState;
	}

	public int getConnection() {
		return Connection;
	}

	public void setConnection(int connection) {
		Connection = connection;
	}

	public int getBus() {
		return Bus;
	}

	public void setBus(int bus) {
		Bus = bus;
	}

	public int getRS232_485_422() {
		return RS232_485_422;
	}

	public void setRS232_485_422(int rS232_485_422) {
		RS232_485_422 = rS232_485_422;
	}

	public int getTemperatureRS232_485_422() {
		return TemperatureRS232_485_422;
	}

	public void setTemperatureRS232_485_422(int temperatureRS232_485_422) {
		TemperatureRS232_485_422 = temperatureRS232_485_422;
	}

	public int getGroundTemperatureRS232_485_422() {
		return GroundTemperatureRS232_485_422;
	}

	public void setGroundTemperatureRS232_485_422(int groundTemperatureRS232_485_422) {
		GroundTemperatureRS232_485_422 = groundTemperatureRS232_485_422;
	}

	public int getHumidityRS232_485_422() {
		return HumidityRS232_485_422;
	}

	public void setHumidityRS232_485_422(int humidityRS232_485_422) {
		HumidityRS232_485_422 = humidityRS232_485_422;
	}

	public int getRadiationRS232_485_422() {
		return RadiationRS232_485_422;
	}

	public void setRadiationRS232_485_422(int radiationRS232_485_422) {
		RadiationRS232_485_422 = radiationRS232_485_422;
	}

	public int getRJ45_LAN() {
		return RJ45_LAN;
	}

	public void setRJ45_LAN(int rJ45_LAN) {
		RJ45_LAN = rJ45_LAN;
	}

	public int getSatellite() {
		return Satellite;
	}

	public void setSatellite(int satellite) {
		Satellite = satellite;
	}

	public int getWireless() {
		return Wireless;
	}

	public void setWireless(int wireless) {
		Wireless = wireless;
	}

	public int getOpticalFiber() {
		return OpticalFiber;
	}

	public void setOpticalFiber(int opticalFiber) {
		OpticalFiber = opticalFiber;
	}

	public int getWindow() {
		return Window;
	}

	public void setWindow(int window) {
		Window = window;
	}

	public int getDetector() {
		return Detector;
	}

	public void setDetector(int detector) {
		Detector = detector;
	}

	public int getCameraLens() {
		return CameraLens;
	}

	public void setCameraLens(int cameraLens) {
		CameraLens = cameraLens;
	}

	public int getVideoCameraLens() {
		return VideoCameraLens;
	}

	public void setVideoCameraLens(int videoCameraLens) {
		VideoCameraLens = videoCameraLens;
	}

	public int getTransmitterPower() {
		return TransmitterPower;
	}

	public void setTransmitterPower(int transmitterPower) {
		TransmitterPower = transmitterPower;
	}

	public int getReceiver_State() {
		return Receiver_State;
	}

	public void setReceiver_State(int receiver_State) {
		Receiver_State = receiver_State;
	}

	public int getTransmitter_State() {
		return Transmitter_State;
	}

	public void setTransmitter_State(int transmitter_State) {
		Transmitter_State = transmitter_State;
	}

	public int getVisor() {
		return Visor;
	}

	public void setVisor(int visor) {
		Visor = visor;
	}

	public int getTiltHead() {
		return TiltHead;
	}

	public void setTiltHead(int tiltHead) {
		TiltHead = tiltHead;
	}

	public int getVideoCamera() {
		return VideoCamera;
	}

	public void setVideoCamera(int videoCamera) {
		VideoCamera = videoCamera;
	}

	public int getCamera() {
		return Camera;
	}

	public void setCamera(int camera) {
		Camera = camera;
	}

	public int getTracker_State() {
		return Tracker_State;
	}

	public void setTracker_State(int tracker_State) {
		Tracker_State = tracker_State;
	}

	public int getCollector() {
		return Collector;
	}

	public void setCollector(int collector) {
		Collector = collector;
	}

	public int getTemperature_State() {
		return Temperature_State;
	}

	public void setTemperature_State(int temperature_State) {
		Temperature_State = temperature_State;
	}

	public int getGroundTemperature_WorkState() {
		return GroundTemperature_WorkState;
	}

	public void setGroundTemperature_WorkState(int groundTemperature_WorkState) {
		GroundTemperature_WorkState = groundTemperature_WorkState;
	}

	public int getHumidity_WorkState() {
		return Humidity_WorkState;
	}

	public void setHumidity_WorkState(int humidity_WorkState) {
		Humidity_WorkState = humidity_WorkState;
	}

	public int getRadiation_WorkState() {
		return Radiation_WorkState;
	}

	public void setRadiation_WorkState(int radiation_WorkState) {
		Radiation_WorkState = radiation_WorkState;
	}

	public int getAD() {
		return AD;
	}

	public void setAD(int aD) {
		AD = aD;
	}

	public int getTemperatureAD() {
		return TemperatureAD;
	}

	public void setTemperatureAD(int temperatureAD) {
		TemperatureAD = temperatureAD;
	}

	public int getGroundTemperatureAD() {
		return GroundTemperatureAD;
	}

	public void setGroundTemperatureAD(int groundTemperatureAD) {
		GroundTemperatureAD = groundTemperatureAD;
	}

	public int getHumidityAD() {
		return HumidityAD;
	}

	public void setHumidityAD(int humidityAD) {
		HumidityAD = humidityAD;
	}

	public int getRadiationAD() {
		return RadiationAD;
	}

	public void setRadiationAD(int radiationAD) {
		RadiationAD = radiationAD;
	}

	public int getCounter() {
		return Counter;
	}

	public void setCounter(int counter) {
		Counter = counter;
	}

	public int getTemperatureCounter() {
		return TemperatureCounter;
	}

	public void setTemperatureCounter(int temperatureCounter) {
		TemperatureCounter = temperatureCounter;
	}

	public int getGroundTemperatureCounter() {
		return GroundTemperatureCounter;
	}

	public void setGroundTemperatureCounter(int groundTemperatureCounter) {
		GroundTemperatureCounter = groundTemperatureCounter;
	}

	public int getHumidityCounter() {
		return HumidityCounter;
	}

	public void setHumidityCounter(int humidityCounter) {
		HumidityCounter = humidityCounter;
	}

	public int getRadiationCounter() {
		return RadiationCounter;
	}

	public void setRadiationCounter(int radiationCounter) {
		RadiationCounter = radiationCounter;
	}

	public int getGate_State() {
		return Gate_State;
	}

	public void setGate_State(int gate_State) {
		Gate_State = gate_State;
	}

	public int getTemperatureGate() {
		return TemperatureGate;
	}

	public void setTemperatureGate(int temperatureGate) {
		TemperatureGate = temperatureGate;
	}

	public int getGroundTemperatureGate() {
		return GroundTemperatureGate;
	}

	public void setGroundTemperatureGate(int groundTemperatureGate) {
		GroundTemperatureGate = groundTemperatureGate;
	}

	public int getHumidityGate() {
		return HumidityGate;
	}

	public void setHumidityGate(int humidityGate) {
		HumidityGate = humidityGate;
	}

	public int getRadiationGate() {
		return RadiationGate;
	}

	public void setRadiationGate(int radiationGate) {
		RadiationGate = radiationGate;
	}

	public int getWater() {
		return Water;
	}

	public void setWater(int water) {
		Water = water;
	}

	public int getDisplacement() {
		return Displacement;
	}

	public void setDisplacement(int displacement) {
		Displacement = displacement;
	}

	public int getWaterLevel() {
		return WaterLevel;
	}

	public void setWaterLevel(int waterLevel) {
		WaterLevel = waterLevel;
	}

	public int getWeighingSensor() {
		return WeighingSensor;
	}

	public void setWeighingSensor(int weighingSensor) {
		WeighingSensor = weighingSensor;
	}

	public int getEvaporationPonds() {
		return EvaporationPonds;
	}

	public void setEvaporationPonds(int evaporationPonds) {
		EvaporationPonds = evaporationPonds;
	}

	public int getExternalMemoryCard() {
		return ExternalMemoryCard;
	}

	public void setExternalMemoryCard(int externalMemoryCard) {
		ExternalMemoryCard = externalMemoryCard;
	}

	public int getPartsSpeed() {
		return PartsSpeed;
	}

	public void setPartsSpeed(int partsSpeed) {
		PartsSpeed = partsSpeed;
	}

	public int getPartsVibrationFrequency() {
		return PartsVibrationFrequency;
	}

	public void setPartsVibrationFrequency(int partsVibrationFrequency) {
		PartsVibrationFrequency = partsVibrationFrequency;
	}

	public int getPositioningAid() {
		return PositioningAid;
	}

	public void setPositioningAid(int positioningAid) {
		PositioningAid = positioningAid;
	}

	public int getCalibrationTimeEquipment() {
		return CalibrationTimeEquipment;
	}

	public void setCalibrationTimeEquipment(int calibrationTimeEquipment) {
		CalibrationTimeEquipment = calibrationTimeEquipment;
	}

	public String getInstrumentStatus() {
		return InstrumentStatus;
	}

	public void setInstrumentStatus(String instrumentStatus) {
		InstrumentStatus = instrumentStatus;
	}

	public String getInstrumentName() {
		return InstrumentName;
	}

	public void setInstrumentName(String instrumentName) {
		InstrumentName = instrumentName;
	}

	public String getInstrumentPath() {
		return InstrumentPath;
	}

	public void setInstrumentPath(String instrumentPath) {
		InstrumentPath = instrumentPath;
	}

	public String getObserver() {
		return Observer;
	}

	public void setObserver(String observer) {
		Observer = observer;
	}

	public String getStartTime() {
		return StartTime;
	}

	public void setStartTime(String startTime) {
		StartTime = startTime;
	}

	public String getEndTime() {
		return EndTime;
	}

	public void setEndTime(String endTime) {
		EndTime = endTime;
	}

	public String getOperationContent() {
		return OperationContent;
	}

	public void setOperationContent(String operationContent) {
		OperationContent = operationContent;
	}

	public String getStationName() {
		return StationName;
	}

	public void setStationName(String stationName) {
		StationName = stationName;
	}

	public String getInstrumentName_Maintain() {
		return InstrumentName_Maintain;
	}

	public void setInstrumentName_Maintain(String instrumentName_Maintain) {
		InstrumentName_Maintain = instrumentName_Maintain;
	}

	public String getDowntime() {
		return Downtime;
	}

	public void setDowntime(String downtime) {
		Downtime = downtime;
	}

	public String getFaultPhenomenon() {
		return FaultPhenomenon;
	}

	public void setFaultPhenomenon(String faultPhenomenon) {
		FaultPhenomenon = faultPhenomenon;
	}

	public String getFaultType() {
		return FaultType;
	}

	public void setFaultType(String faultType) {
		FaultType = faultType;
	}

	public String getFaultCause() {
		return FaultCause;
	}

	public void setFaultCause(String faultCause) {
		FaultCause = faultCause;
	}

	public String getMaintenance() {
		return Maintenance;
	}

	public void setMaintenance(String maintenance) {
		Maintenance = maintenance;
	}

	public String getMaintenanceMan() {
		return MaintenanceMan;
	}

	public void setMaintenanceMan(String maintenanceMan) {
		MaintenanceMan = maintenanceMan;
	}

	public String getMaintenanceStartTime() {
		return MaintenanceStartTime;
	}

	public void setMaintenanceStartTime(String maintenanceStartTime) {
		MaintenanceStartTime = maintenanceStartTime;
	}

	public String getMaintenanceEndtTime() {
		return MaintenanceEndtTime;
	}

	public void setMaintenanceEndtTime(String maintenanceEndtTime) {
		MaintenanceEndtTime = maintenanceEndtTime;
	}
}
