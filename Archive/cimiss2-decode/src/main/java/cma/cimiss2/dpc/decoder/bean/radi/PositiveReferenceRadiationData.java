package cma.cimiss2.dpc.decoder.bean.radi;

import java.util.Date;

import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>

 *正点基准辐射资料实体类
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <li> <a href=" "> 《基准辐射资料处理原则》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月28日 下午3:02:30   maxiyue    Initial creation.
 * </pre>
 * 
 * @author maxiyue
 * @version 0.0.1
 */
public class PositiveReferenceRadiationData extends AgmeReportHeader{
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
	 * unit: 米 <br>
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
	 * unit:  米<br>
	 * BUFR FXY:V07032_1 <br>
	 * descriptionCN: <br>
	 * decode rule:取值除以10<br>
     * field rule:直接赋值
	 */
	private Double DRA_Sensor_Heigh;
	/**
	 * NO:7 <br>
	 * nameCN: 散射辐射辐射表距地高度 <br>
	 * unit:  米<br>
	 * BUFR FXY:V07032_2 <br>
	 * descriptionCN: <br>
	 * decode rule:取值除以10<br>
     * field rule:直接赋值
	 */
	private Double SRA_Sensor_Heigh;
	/**
	 * NO:8 <br>
	 * nameCN: 总辐射辐射表距地高度 <br>
	 * unit: 米 <br>
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
	 * unit: 米 <br>
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
	 * unit: 米 <br>
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
	 * nameCN: 太阳直接辐射辐照度小时平均值<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14313_701_60<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double DRA_Avg_Hour;
	/**
	 * NO:15 <br>
	 * nameCN: 直接辐射(曝辐量)<br>
	 * unit: MJ/m2 <br>
	 * BUFR FXY:V14322<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以100<br>
     * field rule:直接赋值
	 */
	private Double directRadiationExposure;
	/**
	 * NO:16 <br>
	 * nameCN: 太阳直接辐射辐照度小时内最大值<br>
	 * unit:  W/m2<br>
	 * BUFR FXY:V14313_05_60<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double DRA_Max_Hour;
	/**
	 * NO:17<br>
	 * nameCN: 直接辐射辐照度最大出现时间<br>
	 * unit:  <br>
	 * BUFR FXY:V14313_05_052<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double DRA_Max_OTime;
	/**
	 * NO:18<br>
	 * nameCN: 散射辐射辐照度小时平均值<br>
	 * unit:  W/m2<br>
	 * BUFR FXY:V14314_701_60<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double SRA_Avg_Hour;
	/**
	 * NO:19<br>
	 * nameCN: 散射辐射(曝辐量)<br>
	 * unit: MJ/m2 <br>
	 * BUFR FXY:V14309<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以100<br>
     * field rule:直接赋值
	 */
	private Double scatteredRadiationExposure;
	/**
	 * NO:20<br>
	 * nameCN:散射辐射辐照度小时内最大值<br>
	 * unit:  W/m2<br>
	 * BUFR FXY:V14314_05_60<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double SRA_Max_Hour;
	/**
	 * NO:21<br>
	 * nameCN:散射辐射辐照度最大出现时间<br>
	 * unit:  <br>
	 * BUFR FXY:V14314_05_052<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double SRA_Max_OTime;
	/**
	 * NO:22<br>
	 * nameCN:总辐射辐照度小时平均值<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14311_701_60<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double QRA_Avg_Hour;
	/**
	 * NO:23<br>
	 * nameCN: 总辐射(曝辐量)<br>
	 * unit:  MJ/m2<br>
	 * BUFR FXY:V14320<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以100<br>
     * field rule:直接赋值
	 */
	private Double totalRadiationExposure;
	/**
	 * NO:24<br>
	 * nameCN: 总辐射辐照度小时内最大值<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14311_05_60<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double QRA_Max_Hour;
	/**
	 * NO:25<br>
	 * nameCN: 总辐射辐照度小时内最大值出现时间<br>
	 * unit:  <br>
	 * BUFR FXY:V14311_05_052<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double QRA_Max_Hour_OTime;
	/**
	 * NO:26<br>
	 * nameCN: 反射辐射辐照度小时平均值<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14315_701_60<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double RRA_Avg_Hour;
	/**
	 * NO:27<br>
	 * nameCN: 反射辐射小时曝辐量<br>
	 * unit:  MJ/m2<br>
	 * BUFR FXY:V14305<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以100<br>
     * field rule:直接赋值
	 */
	private Double RRA_Hour;
	/**
	 * NO:28<br>
	 * nameCN: 反射辐射辐照度小时内最大值<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14315_05_60<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double RRA_Max_Hour;
	/**
	 * NO:29<br>
	 * nameCN: 反射辐射辐照度最大出现时间<br>
	 * unit:  <br>
	 * BUFR FXY:V14315_05_052<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double RRA_Max_OTime;
	/**
	 * NO:30<br>
	 * nameCN: 大气长波辐射辐照度小时平均值<br>
	 * unit:  W/m2<br>
	 * BUFR FXY:V14318_701_60<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double ALR_Avg_Hour;
	/**
	 * NO:31<br>
	 * nameCN: 大气长波辐射小时曝辐量<br>
	 * unit: MJ/m2 <br>
	 * BUFR FXY:V14323<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以100<br>
     * field rule:直接赋值
	 */
	private Double ALR_Hour;
	/**
	 * NO:32<br>
	 * nameCN: 大气长波辐射辐照度小时内最小值<br>
	 * unit:  W/m2<br>
	 * BUFR FXY:V14318_06_60<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double ALR_Min_Hour;
	/**
	 * NO:33<br>
	 * nameCN:大气长波辐射辐照度小时内最小值出现时间<br>
	 * unit:  <br>
	 * BUFR FXY:V14318_06_052<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double ALR_Min_Mi_OTIime;
	/**
	 * NO:34<br>
	 * nameCN:大气长波辐射辐照度小时内最大值<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14318_05_60<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double ALR_Max_Hour;
	/**
	 * NO:35<br>
	 * nameCN:大气长波辐射辐照度小时内最大值出现时间<br>
	 * unit:  <br>
	 * BUFR FXY:V14318_05_052<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double ALR_Max_Mi_OTime;
	/**
	 * NO:36<br>
	 * nameCN:地球长波辐射辐照度小时平均值<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14319_701_60<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double ELR_Avg_Hour;
	/**
	 * NO:37<br>
	 * nameCN:地球长波辐射小时曝辐量<br>
	 * unit: MJ/m2 <br>
	 * BUFR FXY:V14324<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以100<br>
     * field rule:直接赋值
	 */
	private Double ELR_Hour;
	/**
	 * NO:38<br>
	 * nameCN:地球长波辐射辐照度60分钟最小值<br>
	 * unit:W/m2  <br>
	 * BUFR FXY:V14319_06_60<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double ELR_Min_Hour;
	/**
	 * NO:39<br>
	 * nameCN:地球长波辐射辐照度小时内最小值出现时间<br>
	 * unit:  <br>
	 * BUFR FXY:V14319_06_052<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double ELR_Min_Mi_Otime;
	/**
	 * NO:40<br>
	 * nameCN:地球长波辐射辐照度小时最大值<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14319_05_60<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double ELR_Max_Hour;
	/**
	 * NO:41<br>
	 * nameCN:地球长波辐射辐照度小时内最大值出现时间<br>
	 * unit:  <br>
	 * BUFR FXY:V14319_05_052<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double ELR_Max_Mi_OTime;
	/**
	 * NO:42<br>
	 * nameCN:紫外辐射（UVA）辐照度小时平均值<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14316_701_60<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以100<br>
     * field rule:直接赋值
	 */
	private Double UVA_Avg_Hour;
	/**
	 * NO:43<br>
	 * nameCN:紫外辐射(曝辐量)<br>
	 * unit:MJ/m2  <br>
	 * BUFR FXY:V14307<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以1000<br>
     * field rule:直接赋值
	 */
	private Double ultravioletRadiationExposure;
	/**
	 * NO:44<br>
	 * nameCN:紫外辐射（UVA）辐照度1分钟最大值<br>
	 * unit:  W/m2<br>
	 * BUFR FXY:V14316_05_01<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以100<br>
     * field rule:直接赋值
	 */
	private Double UV_Max_Mi;
	/**
	 * NO:45<br>
	 * nameCN:紫外辐射辐照度最大出现时间<br>
	 * unit:  <br>
	 * BUFR FXY:V14316_05_052<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double UV_Max_OTime;
	/**
	 * NO:46<br>
	 * nameCN:紫外辐射（UVB）辐照度小时平均值<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14317_701_60<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以100<br>
     * field rule:直接赋值
	 */
	private Double UVB_Avg_Hour;
	/**
	 * NO:47<br>
	 * nameCN:紫外辐射（UVB）小时曝辐量<br>
	 * unit: MJ/m2 <br>
	 * BUFR FXY:V14310<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以1000<br>
     * field rule:直接赋值
	 */
	private Double UV_Hour;
	/**
	 * NO:48<br>
	 * nameCN:紫外辐射（UVB）辐照度小时内最大值<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14317_05_60<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以100<br>
     * field rule:直接赋值
	 */
	private Double UVB_Max_Hour;
	/**
	 * NO:49<br>
	 * nameCN:紫外辐射（UVB）辐照度小时内最大值出现时间<br>
	 * unit:  <br>
	 * BUFR FXY:V14317_05_052<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double UVB_Max_Hour_OTime;
	/**
	 * NO:50<br>
	 * nameCN:光合有效辐射辐照度小时平均值<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14340_701_60<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double PAR_Avg_Hour;
	/**
	 * NO:51<br>
	 * nameCN:光合有效辐射辐照度小时内最大值<br>
	 * unit: W/m2 <br>
	 * BUFR FXY:V14340_05_60<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double PAR_Max_Hour;
	/**
	 * NO:52<br>
	 * nameCN:光合有效辐射小时内最大值出现时间<br>
	 * unit:  <br>
	 * BUFR FXY:V14340_05_052<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double PAR_Max_Mi_OTime;

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

	public Double getDRA_Avg_Hour() {
		return DRA_Avg_Hour;
	}

	public void setDRA_Avg_Hour(Double dRA_Avg_Hour) {
		DRA_Avg_Hour = dRA_Avg_Hour;
	}

	public Double getDirectRadiationExposure() {
		return directRadiationExposure;
	}

	public void setDirectRadiationExposure(Double directRadiationExposure) {
		this.directRadiationExposure = directRadiationExposure;
	}

	public Double getDRA_Max_Hour() {
		return DRA_Max_Hour;
	}

	public void setDRA_Max_Hour(Double dRA_Max_Hour) {
		DRA_Max_Hour = dRA_Max_Hour;
	}

	public Double getDRA_Max_OTime() {
		return DRA_Max_OTime;
	}

	public void setDRA_Max_OTime(Double dRA_Max_OTime) {
		DRA_Max_OTime = dRA_Max_OTime;
	}

	public Double getSRA_Avg_Hour() {
		return SRA_Avg_Hour;
	}

	public void setSRA_Avg_Hour(Double sRA_Avg_Hour) {
		SRA_Avg_Hour = sRA_Avg_Hour;
	}

	public Double getScatteredRadiationExposure() {
		return scatteredRadiationExposure;
	}

	public void setScatteredRadiationExposure(Double scatteredRadiationExposure) {
		this.scatteredRadiationExposure = scatteredRadiationExposure;
	}

	public Double getSRA_Max_Hour() {
		return SRA_Max_Hour;
	}

	public void setSRA_Max_Hour(Double sRA_Max_Hour) {
		SRA_Max_Hour = sRA_Max_Hour;
	}

	public Double getSRA_Max_OTime() {
		return SRA_Max_OTime;
	}

	public void setSRA_Max_OTime(Double sRA_Max_OTime) {
		SRA_Max_OTime = sRA_Max_OTime;
	}

	public Double getQRA_Avg_Hour() {
		return QRA_Avg_Hour;
	}

	public void setQRA_Avg_Hour(Double qRA_Avg_Hour) {
		QRA_Avg_Hour = qRA_Avg_Hour;
	}

	public Double getTotalRadiationExposure() {
		return totalRadiationExposure;
	}

	public void setTotalRadiationExposure(Double totalRadiationExposure) {
		this.totalRadiationExposure = totalRadiationExposure;
	}

	public Double getQRA_Max_Hour() {
		return QRA_Max_Hour;
	}

	public void setQRA_Max_Hour(Double qRA_Max_Hour) {
		QRA_Max_Hour = qRA_Max_Hour;
	}

	public Double getQRA_Max_Hour_OTime() {
		return QRA_Max_Hour_OTime;
	}

	public void setQRA_Max_Hour_OTime(Double qRA_Max_Hour_OTime) {
		QRA_Max_Hour_OTime = qRA_Max_Hour_OTime;
	}

	public Double getRRA_Avg_Hour() {
		return RRA_Avg_Hour;
	}

	public void setRRA_Avg_Hour(Double rRA_Avg_Hour) {
		RRA_Avg_Hour = rRA_Avg_Hour;
	}

	public Double getRRA_Hour() {
		return RRA_Hour;
	}

	public void setRRA_Hour(Double rRA_Hour) {
		RRA_Hour = rRA_Hour;
	}

	public Double getRRA_Max_Hour() {
		return RRA_Max_Hour;
	}

	public void setRRA_Max_Hour(Double rRA_Max_Hour) {
		RRA_Max_Hour = rRA_Max_Hour;
	}

	public Double getRRA_Max_OTime() {
		return RRA_Max_OTime;
	}

	public void setRRA_Max_OTime(Double rRA_Max_OTime) {
		RRA_Max_OTime = rRA_Max_OTime;
	}

	public Double getALR_Avg_Hour() {
		return ALR_Avg_Hour;
	}

	public void setALR_Avg_Hour(Double aLR_Avg_Hour) {
		ALR_Avg_Hour = aLR_Avg_Hour;
	}

	public Double getALR_Hour() {
		return ALR_Hour;
	}

	public void setALR_Hour(Double aLR_Hour) {
		ALR_Hour = aLR_Hour;
	}

	public Double getALR_Min_Hour() {
		return ALR_Min_Hour;
	}

	public void setALR_Min_Hour(Double aLR_Min_Hour) {
		ALR_Min_Hour = aLR_Min_Hour;
	}

	public Double getALR_Min_Mi_OTIime() {
		return ALR_Min_Mi_OTIime;
	}

	public void setALR_Min_Mi_OTIime(Double aLR_Min_Mi_OTIime) {
		ALR_Min_Mi_OTIime = aLR_Min_Mi_OTIime;
	}

	public Double getALR_Max_Hour() {
		return ALR_Max_Hour;
	}

	public void setALR_Max_Hour(Double aLR_Max_Hour) {
		ALR_Max_Hour = aLR_Max_Hour;
	}

	public Double getALR_Max_Mi_OTime() {
		return ALR_Max_Mi_OTime;
	}

	public void setALR_Max_Mi_OTime(Double aLR_Max_Mi_OTime) {
		ALR_Max_Mi_OTime = aLR_Max_Mi_OTime;
	}

	public Double getELR_Avg_Hour() {
		return ELR_Avg_Hour;
	}

	public void setELR_Avg_Hour(Double eLR_Avg_Hour) {
		ELR_Avg_Hour = eLR_Avg_Hour;
	}

	public Double getELR_Hour() {
		return ELR_Hour;
	}

	public void setELR_Hour(Double eLR_Hour) {
		ELR_Hour = eLR_Hour;
	}

	public Double getELR_Min_Hour() {
		return ELR_Min_Hour;
	}

	public void setELR_Min_Hour(Double eLR_Min_Hour) {
		ELR_Min_Hour = eLR_Min_Hour;
	}

	public Double getELR_Min_Mi_Otime() {
		return ELR_Min_Mi_Otime;
	}

	public void setELR_Min_Mi_Otime(Double eLR_Min_Mi_Otime) {
		ELR_Min_Mi_Otime = eLR_Min_Mi_Otime;
	}

	public Double getELR_Max_Hour() {
		return ELR_Max_Hour;
	}

	public void setELR_Max_Hour(Double eLR_Max_Hour) {
		ELR_Max_Hour = eLR_Max_Hour;
	}

	public Double getELR_Max_Mi_OTime() {
		return ELR_Max_Mi_OTime;
	}

	public void setELR_Max_Mi_OTime(Double eLR_Max_Mi_OTime) {
		ELR_Max_Mi_OTime = eLR_Max_Mi_OTime;
	}

	public Double getUVA_Avg_Hour() {
		return UVA_Avg_Hour;
	}

	public void setUVA_Avg_Hour(Double uVA_Avg_Hour) {
		UVA_Avg_Hour = uVA_Avg_Hour;
	}

	public Double getUltravioletRadiationExposure() {
		return ultravioletRadiationExposure;
	}

	public void setUltravioletRadiationExposure(Double ultravioletRadiationExposure) {
		this.ultravioletRadiationExposure = ultravioletRadiationExposure;
	}

	public Double getUV_Max_Mi() {
		return UV_Max_Mi;
	}

	public void setUV_Max_Mi(Double uV_Max_Mi) {
		UV_Max_Mi = uV_Max_Mi;
	}

	public Double getUV_Max_OTime() {
		return UV_Max_OTime;
	}

	public void setUV_Max_OTime(Double uV_Max_OTime) {
		UV_Max_OTime = uV_Max_OTime;
	}

	public Double getUVB_Avg_Hour() {
		return UVB_Avg_Hour;
	}

	public void setUVB_Avg_Hour(Double uVB_Avg_Hour) {
		UVB_Avg_Hour = uVB_Avg_Hour;
	}

	public Double getUV_Hour() {
		return UV_Hour;
	}

	public void setUV_Hour(Double uV_Hour) {
		UV_Hour = uV_Hour;
	}

	public Double getUVB_Max_Hour() {
		return UVB_Max_Hour;
	}

	public void setUVB_Max_Hour(Double uVB_Max_Hour) {
		UVB_Max_Hour = uVB_Max_Hour;
	}

	public Double getUVB_Max_Hour_OTime() {
		return UVB_Max_Hour_OTime;
	}

	public void setUVB_Max_Hour_OTime(Double uVB_Max_Hour_OTime) {
		UVB_Max_Hour_OTime = uVB_Max_Hour_OTime;
	}

	public Double getPAR_Avg_Hour() {
		return PAR_Avg_Hour;
	}

	public void setPAR_Avg_Hour(Double pAR_Avg_Hour) {
		PAR_Avg_Hour = pAR_Avg_Hour;
	}

	public Double getPAR_Max_Hour() {
		return PAR_Max_Hour;
	}

	public void setPAR_Max_Hour(Double pAR_Max_Hour) {
		PAR_Max_Hour = pAR_Max_Hour;
	}

	public Double getPAR_Max_Mi_OTime() {
		return PAR_Max_Mi_OTime;
	}

	public void setPAR_Max_Mi_OTime(Double pAR_Max_Mi_OTime) {
		PAR_Max_Mi_OTime = pAR_Max_Mi_OTime;
	}
	
	
}
