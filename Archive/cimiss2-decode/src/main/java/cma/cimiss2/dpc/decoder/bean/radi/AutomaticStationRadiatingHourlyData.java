package cma.cimiss2.dpc.decoder.bean.radi;

import java.util.Date;

import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>

 * 自动站辐射逐小时资料实体类
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《自动站辐射资料解码算法》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月27日 下午2:36:00   maxiyue    Initial creation.
 * </pre>
 * 
 * @author maxiyue
 * @version 0.0.1
 */
public class AutomaticStationRadiatingHourlyData extends AgmeReportHeader{
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
	 * NO: 4  <br>
	 * nameCN: 资料时间  <br>
	 * unit: <br>
	 * BUFR FXY: V04001\V04002\V04003\V04004 <br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:使用java时间转化工具类进行转化,标准形式为：yyyyMMddHHmmss
	 */
	private Date observationTime;
	/**
	 * NO: 5 <br>
	 * nameCN: 总辐射辐照度 <br>
	 * unit: W.m-2<br>
	 * BUFR FXY: V14311<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double totalRadiationIrradiance ;
	/**
	 * NO: 6 <br>
	 * nameCN: 净全辐射辐照度 <br>
	 * unit: W.m-2<br>
	 * BUFR FXY: V14312<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double netRadiationIrradiance ;
	/**
	 * NO: 7 <br>
	 * nameCN: 直接辐射辐照度<br>
	 * unit: W.m-2<br>
	 * BUFR FXY: V14313<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double directRadiationIrradiance ;
	/**
	 * NO: 8 <br>
	 * nameCN: 散射辐射辐照度<br>
	 * unit: W.m-2<br>
	 * BUFR FXY: V14314<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double scatteringRadiationIrradiance ;
	/**
	 * NO: 9 <br>
	 * nameCN: 反射辐射辐照度<br>
	 * unit: W.m-2<br>
	 * BUFR FXY: V14315<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double reflectionRadiationIrradiance ;
	/**
	 * NO: 10 <br>
	 * nameCN: 紫外辐射辐照度<br>
	 * unit:W.m-2 <br>
	 * BUFR FXY: V14316<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double ultravioletRadiationIrradiance ;
	/**
	 * NO: 11<br>
	 * nameCN: 总辐射(曝辐量)<br>
	 * unit: MJ.m-2<br>
	 * BUFR FXY: V14320<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以100<br>
     * field rule:直接赋值
	 */
	private Double totalRadiationExposure ;
	/**
	 * NO: 12<br>
	 * nameCN: 总辐射辐照度最大值<br>
	 * unit: W.m-2<br>
	 * BUFR FXY: V14311_05<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double radiationIrradianceMax ;
	/**
	 * NO: 13<br>
	 * nameCN: 总辐射辐照度最大出现时间<br>
	 * unit: 格式：时分<br>
	 * BUFR FXY: V14021_05_052<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double radiationIrradianceMaxtime ;
	/**
	 * NO: 14<br>
	 * nameCN: 净全辐射(曝辐量)<br>
	 * unit: MJ.m-2<br>
	 * BUFR FXY: V14308<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以100<br>
     * field rule:直接赋值
	 */
	private Double netRadiationExposure ;
	/**
	 * NO: 15<br>
	 * nameCN: 净辐射辐照度最大值<br>
	 * unit: W.m-2<br>
	 * BUFR FXY: V14312_05<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double netRadiationIrradiationMax ;
	/**
	 * NO: 16<br>
	 * nameCN: 净辐射辐照度最大出现时间<br>
	 * unit: 格式：时分<br>
	 * BUFR FXY: V14312_05_052<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double netRadiationIrradiationMaxtime ;
	/**
	 * NO: 17<br>
	 * nameCN: 净辐射辐照度最小值<br>
	 * unit: W.m-2<br>
	 * BUFR FXY: V14312_06<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double netRadiationIrradiationMin ;
	/**
	 * NO: 18<br>
	 * nameCN: 净辐射辐照度最小出现时间<br>
	 * unit: 格式：时分<br>
	 * BUFR FXY: V14312_06_052<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double netRadiationIrradiationMintime ;
	/**
	 * NO: 19<br>
	 * nameCN: 直接辐射(曝辐量)<br>
	 * unit: MJ.m-2<br>
	 * BUFR FXY:V14322<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以100<br>
     * field rule:直接赋值
	 */
	private Double directRadiationExposure ;
	/**
	 * NO: 20<br>
	 * nameCN: 直接辐射辐照度最大值<br>
	 * unit: W.m-2<br>
	 * BUFR FXY:V14313_05<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double directRadiationIrradianceMax;
	/**
	 * NO: 21<br>
	 * nameCN: 直接辐射辐照度最大出现时间<br>
	 * unit: 格式：时分<br>
	 * BUFR FXY:V14313_05_052<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double directRadiationIrradianceMaxtime ;
	/**
	 * NO: 22<br>
	 * nameCN: 散射辐射(曝辐量)<br>
	 * unit:MJ.m-2 <br>
	 * BUFR FXY:V14309<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以100<br>
     * field rule:直接赋值
	 */
	private Double scatteredRadiationExposure;
	/**
	 * NO: 23<br>
	 * nameCN: 散射辐射辐照度最大值<br>
	 * unit:W.m-2 <br>
	 * BUFR FXY:V14314_05<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double scatteringRadiationIrradianceMax;
	/**
	 * NO: 24<br>
	 * nameCN: 散射辐射辐照度最大出现时间<br>
	 * unit: 格式：时分<br>
	 * BUFR FXY:V14314_05_052<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double scatteringRadiationIrradianceMaxtime;
	/**
	 * NO: 25<br>
	 * nameCN: 反射辐射(曝辐量)<br>
	 * unit: MJ.m-2<br>
	 * BUFR FXY:V14306<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以100<br>
     * field rule:直接赋值
	 */
	private Double reflectedRadiationExposure;
	/**
	 * NO: 26<br>
	 * nameCN: 反射辐射辐照度最大值<br>
	 * unit: W.m-2<br>
	 * BUFR FXY:V14315_05<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double reflectedRadiationIrradianceMax;
	/**
	 * NO: 27<br>
	 * nameCN: 反射辐射辐照度最大出现时间<br>
	 * unit:格式：时分 <br>
	 * BUFR FXY:V14315_05_052<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double reflectedRadiationIrradianceMaxtime;
	/**
	 * NO: 28<br>
	 * nameCN:紫外辐射(曝辐量)<br>
	 * unit: MJ.m-2<br>
	 * BUFR FXY:V14307<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以100<br>
     * field rule:直接赋值
	 */
	private Double ultravioletRadiationExposure;
	/**
	 * NO: 29<br>
	 * nameCN:紫外辐射辐照度最大值<br>
	 * unit:W.m-2 <br>
	 * BUFR FXY:V14316_05<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double uvIrradiationMax;
	/**
	 * NO: 30<br>
	 * nameCN:紫外辐射辐照度最大出现时间<br>
	 * unit: 格式：时分<br>
	 * BUFR FXY:V14316_05_052<br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private Double uvIrradiationMaxtime;
	/**
	 * NO: 31<br>
	 * nameCN:日照时数<br>
	 * unit:小时 <br>
	 * BUFR FXY:V14032<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以10<br>
     * field rule:直接赋值
	 */
	private Double sunShineHours;
	/**
	 * NO: 32<br>
	 * nameCN:大气浑浊度指标<br>
	 * unit: <br>
	 * BUFR FXY:V15483<br>
	 * descriptionCN: <br>
	 * decode rule:取值除以100<br>
     * field rule:直接赋值
	 */
	private Double atmosphericTurbidityIndex;
	/**
	 * NO: 33<br>
	 * nameCN:总辐射辐照度质控码<br>
	 * unit: <br>
	 * BUFR FXY:Q14311<br>
	 * descriptionCN: <br>
	 * decode rule:赋默认值9<br>
     * field rule:直接赋值
	 */
	private Double Q_totalRadiationIrradiance=9.0 ;
	/**
	 * NO: 34<br>
	 * nameCN:净全辐射辐照度质控码<br>
	 * unit: <br>
	 * BUFR FXY:Q14312<br>
	 * descriptionCN: <br>
	 * decode rule:赋默认值9<br>
     * field rule:直接赋值
	 */
	private Double Q_netRadiationIrradiance=9.0 ;
	/**
	 * NO: 35<br>
	 * nameCN:直接辐射辐照度质控码<br>
	 * unit: <br>
	 * BUFR FXY:Q14313<br>
	 * descriptionCN: <br>
	 * decode rule:赋默认值9<br>
     * field rule:直接赋值
	 */
	private Double Q_directRadiationIrradiance=9.0 ;
	/**
	 * NO: 36<br>
	 * nameCN:散射辐射辐照度质控码<br>
	 * unit: <br>
	 * BUFR FXY:Q14314<br>
	 * descriptionCN: <br>
	 * decode rule:赋默认值9<br>
     * field rule:直接赋值
	 */
	private Double Q_scatteringRadiationIrradianc=9.0 ;
	/**
	 * NO: 37<br>
	 * nameCN:反射辐射辐照度质控码<br>
	 * unit: <br>
	 * BUFR FXY:Q14315<br>
	 * descriptionCN: <br>
	 * decode rule:赋默认值9<br>
     * field rule:直接赋值
	 */
	private Double Q_reflectionRadiationIrradiance=9.0 ;
	/**
	 * NO: 38<br>
	 * nameCN:紫外辐射辐照度质控码<br>
	 * unit: <br>
	 * BUFR FXY:Q14316<br>
	 * descriptionCN: <br>
	 * decode rule:赋默认值9<br>
     * field rule:直接赋值
	 */
	private Double 	Q_ultravioletRadiationIrradiance =9.0;
	/**
	 * NO: 39<br>
	 * nameCN:总辐射(曝辐量)质控码<br>
	 * unit: <br>
	 * BUFR FXY:Q14320<br>
	 * descriptionCN: <br>
	 * decode rule:赋默认值9<br>
     * field rule:直接赋值
	 */
	private Double Q_totalRadiationExposure=9.0 ;
	/**
	 * NO: 40<br>
	 * nameCN:总辐射辐照度最大值质控码<br>
	 * unit: <br>
	 * BUFR FXY:Q14311_05<br>
	 * descriptionCN: <br>
	 * decode rule:赋默认值9<br>
     * field rule:直接赋值
	 */
	private Double Q_radiationIrradianceMax =9.0;
	/**
	 * NO: 41<br>
	 * nameCN:总辐射辐照度最大出现时间质控码 <br>
	 * unit: <br>
	 * BUFR FXY:Q14021_05_052<br>
	 * descriptionCN: <br>
	 * decode rule:赋默认值9<br>
     * field rule:直接赋值
	 */
	private Double Q_radiationIrradianceMaxtime =9.0;
	/**
	 * NO: 42<br>
	 * nameCN:净全辐射(曝辐量)质控码 <br>
	 * unit: <br>
	 * BUFR FXY:Q14308<br>
	 * descriptionCN: <br>
	 * decode rule:赋默认值9<br>
     * field rule:直接赋值
	 */
	private Double Q_netRadiationExposure =9.0;
	/**
	 * NO: 43<br>
	 * nameCN:净辐射辐照度最大值质控码 <br>
	 * unit: <br>
	 * BUFR FXY:Q14312_05<br>
	 * descriptionCN: <br>
	 * decode rule:赋默认值9<br>
     * field rule:直接赋值
	 */
	private Double Q_netRadiationIrradiationMax =9.0;
	/**
	 * NO: 44<br>
	 * nameCN:净辐射辐照度最大出现时间质控码 <br>
	 * unit: <br>
	 * BUFR FXY:Q14312_05_052<br>
	 * descriptionCN: <br>
	 * decode rule:赋默认值9<br>
     * field rule:直接赋值
	 */
	private Double Q_netRadiationIrradiationMaxtime =9.0;
	/**
	 * NO: 45<br>
	 * nameCN:净辐射辐照度最小值质控码 <br>
	 * unit: <br>
	 * BUFR FXY:Q14312_06<br>
	 * descriptionCN: <br>
	 * decode rule:赋默认值9<br>
     * field rule:直接赋值
	 */
	private Double Q_netRadiationIrradiationMin =9.0;
	/**
	 * NO: 46<br>
	 * nameCN:净辐射辐照度最小出现时间质控码 <br>
	 * unit: <br>
	 * BUFR FXY:Q14312_06_052<br>
	 * descriptionCN: <br>
	 * decode rule:赋默认值9<br>
     * field rule:直接赋值
	 */
	private Double Q_netRadiationIrradiationMintime=9.0 ;
	/**
	 * NO: 47<br>
	 * nameCN:直接辐射(曝辐量)质控码 <br>
	 * unit: <br>
	 * BUFR FXY:Q14322<br>
	 * descriptionCN: <br>
	 * decode rule:赋默认值9<br>、
     * field rule:直接赋值
	 */
	private Double Q_directRadiationExposure =9.0;
	/**
	 * NO: 48<br>
	 * nameCN:直接辐射辐照度最大值质控码 <br>
	 * unit: <br>
	 * BUFR FXY:Q14313_05<br>
	 * descriptionCN: <br>
	 * decode rule:赋默认值9<br>
     * field rule:直接赋值
	 */
	private Double Q_directRadiationIrradianceMax=9.0;
	/**
	 * NO: 49<br>
	 * nameCN:直接辐射辐照度最大出现时间质控码<br>
	 * unit: <br>
	 * BUFR FXY:Q14313_05_052<br>
	 * descriptionCN: <br>
	 * decode rule:赋默认值9<br>
     * field rule:直接赋值
	 */
	private Double Q_directRadiationIrradianceMaxtime=9.0 ;
	/**
	 * NO: 50<br>
	 * nameCN:散射辐射(曝辐量)质控码<br>
	 * unit: <br>
	 * BUFR FXY:Q14309<br>
	 * descriptionCN: <br>
	 * decode rule:赋默认值9<br>
     * field rule:直接赋值
	 */
	private Double Q_scatteredRadiationExposure=9.0;
	/**
	 * NO: 51<br>
	 * nameCN:散射辐射辐照度最大值质控码<br>
	 * unit: <br>
	 * BUFR FXY:Q14314_05<br>
	 * descriptionCN: <br>
	 * decode rule:赋默认值9<br>
     * field rule:直接赋值
	 */
	private Double Q_scatteringRadiationIrradianceMax=9.0;
	/**
	 * NO: 52<br>
	 * nameCN:散射辐射辐照度最大出现时间质控码 <br>
	 * unit: <br>
	 * BUFR FXY:Q14314_05_052<br>
	 * descriptionCN: <br>
	 * decode rule:赋默认值9<br>
     * field rule:直接赋值
	 */
	private Double Q_scatteringRadiationIrradianceMaxtime=9.0;
	/**
	 * NO: 53<br>
	 * nameCN:反射辐射(曝辐量)质控码 <br>
	 * unit: <br>
	 * BUFR FXY:Q14306<br>
	 * descriptionCN: <br>
	 * decode rule:赋默认值9<br>
     * field rule:直接赋值
	 */
	private Double Q_reflectedRadiationExposure=9.0;
	/**
	 * NO: 54<br>
	 * nameCN:反射辐射辐照度最大值质控码  <br>
	 * unit: <br>
	 * BUFR FXY:Q14315_05<br>
	 * descriptionCN: <br>
	 * decode rule:赋默认值9<br>
     * field rule:直接赋值
	 */
	private Double Q_reflectedRadiationIrradianceMax=9.0;
	/**
	 * NO: 55<br>
	 * nameCN:反射辐射辐照度最大出现时间质控码  <br>
	 * unit: <br>
	 * BUFR FXY:Q14315_05_052<br>
	 * descriptionCN: <br>
	 * decode rule:赋默认值9<br>
     * field rule:直接赋值
	 */
	private Double Q_reflectedRadiationIrradianceMaxtime=9.0;
	/**
	 * NO: 56<br>
	 * nameCN:紫外辐射(曝辐量)质控码 <br>
	 * unit: <br>
	 * BUFR FXY:Q14307<br>
	 * descriptionCN: <br>
	 * decode rule:赋默认值9<br>
     * field rule:直接赋值
	 */
	private Double Q_ultravioletRadiationExposure=9.0;
	/**
	 * NO: 57<br>
	 * nameCN:紫外辐射辐照度最大值质控码 <br>
	 * unit: <br>
	 * BUFR FXY:Q14316_05<br>
	 * descriptionCN: <br>
	 * decode rule:赋默认值9<br>
     * field rule:直接赋值
	 */
	private Double Q_uvIrradiationMax=9.0;
	/**
	 * NO: 58<br>
	 * nameCN:紫外辐射辐照度最大出现时间质控码 <br>
	 * unit: <br>
	 * BUFR FXY:Q14316_05_052<br>
	 * descriptionCN: <br>
	 * decode rule:赋默认值9<br>
     * field rule:直接赋值
	 */
	private Double Q_uvIrradiationMaxtime=9.0;
	/**
	 * NO: 59<br>
	 * nameCN:日照时数质控码 <br>
	 * unit: <br>
	 * BUFR FXY:Q14032<br>
	 * descriptionCN: <br>
	 * decode rule:赋默认值9<br>
     * field rule:直接赋值
	 */
	private Double Q_sunShineHours=9.0;
	/**
	 * NO: 60<br>
	 * nameCN:大气浑浊度指标质控码 <br>
	 * unit: <br>
	 * BUFR FXY:Q15483<br>
	 * descriptionCN: <br>
	 * decode rule:赋默认值9<br>
     * field rule:直接赋值
	 */
	private Double Q_atmosphericTurbidityIndex=9.0;

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

	public Date getObservationTime() {
		return observationTime;
	}

	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}

	public Double getTotalRadiationIrradiance() {
		return totalRadiationIrradiance;
	}

	public void setTotalRadiationIrradiance(Double totalRadiationIrradiance) {
		this.totalRadiationIrradiance = totalRadiationIrradiance;
	}

	public Double getNetRadiationIrradiance() {
		return netRadiationIrradiance;
	}

	public void setNetRadiationIrradiance(Double netRadiationIrradiance) {
		this.netRadiationIrradiance = netRadiationIrradiance;
	}

	public Double getDirectRadiationIrradiance() {
		return directRadiationIrradiance;
	}

	public void setDirectRadiationIrradiance(Double directRadiationIrradiance) {
		this.directRadiationIrradiance = directRadiationIrradiance;
	}

	public Double getScatteringRadiationIrradiance() {
		return scatteringRadiationIrradiance;
	}

	public void setScatteringRadiationIrradiance(Double scatteringRadiationIrradiance) {
		this.scatteringRadiationIrradiance = scatteringRadiationIrradiance;
	}

	public Double getReflectionRadiationIrradiance() {
		return reflectionRadiationIrradiance;
	}

	public void setReflectionRadiationIrradiance(Double reflectionRadiationIrradiance) {
		this.reflectionRadiationIrradiance = reflectionRadiationIrradiance;
	}

	public Double getUltravioletRadiationIrradiance() {
		return ultravioletRadiationIrradiance;
	}

	public void setUltravioletRadiationIrradiance(Double ultravioletRadiationIrradiance) {
		this.ultravioletRadiationIrradiance = ultravioletRadiationIrradiance;
	}

	public Double getTotalRadiationExposure() {
		return totalRadiationExposure;
	}

	public void setTotalRadiationExposure(Double totalRadiationExposure) {
		this.totalRadiationExposure = totalRadiationExposure;
	}

	public Double getRadiationIrradianceMax() {
		return radiationIrradianceMax;
	}

	public void setRadiationIrradianceMax(Double radiationIrradianceMax) {
		this.radiationIrradianceMax = radiationIrradianceMax;
	}

	public Double getRadiationIrradianceMaxtime() {
		return radiationIrradianceMaxtime;
	}

	public void setRadiationIrradianceMaxtime(Double radiationIrradianceMaxtime) {
		this.radiationIrradianceMaxtime = radiationIrradianceMaxtime;
	}

	public Double getNetRadiationExposure() {
		return netRadiationExposure;
	}

	public void setNetRadiationExposure(Double netRadiationExposure) {
		this.netRadiationExposure = netRadiationExposure;
	}

	public Double getNetRadiationIrradiationMax() {
		return netRadiationIrradiationMax;
	}

	public void setNetRadiationIrradiationMax(Double netRadiationIrradiationMax) {
		this.netRadiationIrradiationMax = netRadiationIrradiationMax;
	}

	public Double getNetRadiationIrradiationMaxtime() {
		return netRadiationIrradiationMaxtime;
	}

	public void setNetRadiationIrradiationMaxtime(Double netRadiationIrradiationMaxtime) {
		this.netRadiationIrradiationMaxtime = netRadiationIrradiationMaxtime;
	}

	public Double getNetRadiationIrradiationMin() {
		return netRadiationIrradiationMin;
	}

	public void setNetRadiationIrradiationMin(Double netRadiationIrradiationMin) {
		this.netRadiationIrradiationMin = netRadiationIrradiationMin;
	}

	public Double getNetRadiationIrradiationMintime() {
		return netRadiationIrradiationMintime;
	}

	public void setNetRadiationIrradiationMintime(Double netRadiationIrradiationMintime) {
		this.netRadiationIrradiationMintime = netRadiationIrradiationMintime;
	}

	public Double getDirectRadiationExposure() {
		return directRadiationExposure;
	}

	public void setDirectRadiationExposure(Double directRadiationExposure) {
		this.directRadiationExposure = directRadiationExposure;
	}

	public Double getDirectRadiationIrradianceMax() {
		return directRadiationIrradianceMax;
	}

	public void setDirectRadiationIrradianceMax(Double directRadiationIrradianceMax) {
		this.directRadiationIrradianceMax = directRadiationIrradianceMax;
	}

	public Double getDirectRadiationIrradianceMaxtime() {
		return directRadiationIrradianceMaxtime;
	}

	public void setDirectRadiationIrradianceMaxtime(Double directRadiationIrradianceMaxtime) {
		this.directRadiationIrradianceMaxtime = directRadiationIrradianceMaxtime;
	}

	public Double getScatteredRadiationExposure() {
		return scatteredRadiationExposure;
	}

	public void setScatteredRadiationExposure(Double scatteredRadiationExposure) {
		this.scatteredRadiationExposure = scatteredRadiationExposure;
	}

	public Double getScatteringRadiationIrradianceMax() {
		return scatteringRadiationIrradianceMax;
	}

	public void setScatteringRadiationIrradianceMax(Double scatteringRadiationIrradianceMax) {
		this.scatteringRadiationIrradianceMax = scatteringRadiationIrradianceMax;
	}

	public Double getScatteringRadiationIrradianceMaxtime() {
		return scatteringRadiationIrradianceMaxtime;
	}

	public void setScatteringRadiationIrradianceMaxtime(Double scatteringRadiationIrradianceMaxtime) {
		this.scatteringRadiationIrradianceMaxtime = scatteringRadiationIrradianceMaxtime;
	}

	public Double getReflectedRadiationExposure() {
		return reflectedRadiationExposure;
	}

	public void setReflectedRadiationExposure(Double reflectedRadiationExposure) {
		this.reflectedRadiationExposure = reflectedRadiationExposure;
	}

	public Double getReflectedRadiationIrradianceMax() {
		return reflectedRadiationIrradianceMax;
	}

	public void setReflectedRadiationIrradianceMax(Double reflectedRadiationIrradianceMax) {
		this.reflectedRadiationIrradianceMax = reflectedRadiationIrradianceMax;
	}

	public Double getReflectedRadiationIrradianceMaxtime() {
		return reflectedRadiationIrradianceMaxtime;
	}

	public void setReflectedRadiationIrradianceMaxtime(Double reflectedRadiationIrradianceMaxtime) {
		this.reflectedRadiationIrradianceMaxtime = reflectedRadiationIrradianceMaxtime;
	}

	public Double getUltravioletRadiationExposure() {
		return ultravioletRadiationExposure;
	}

	public void setUltravioletRadiationExposure(Double ultravioletRadiationExposure) {
		this.ultravioletRadiationExposure = ultravioletRadiationExposure;
	}

	public Double getUvIrradiationMax() {
		return uvIrradiationMax;
	}

	public void setUvIrradiationMax(Double uvIrradiationMax) {
		this.uvIrradiationMax = uvIrradiationMax;
	}

	public Double getUvIrradiationMaxtime() {
		return uvIrradiationMaxtime;
	}

	public void setUvIrradiationMaxtime(Double uvIrradiationMaxtime) {
		this.uvIrradiationMaxtime = uvIrradiationMaxtime;
	}

	public Double getSunShineHours() {
		return sunShineHours;
	}

	public void setSunShineHours(Double sunShineHours) {
		this.sunShineHours = sunShineHours;
	}

	public Double getAtmosphericTurbidityIndex() {
		return atmosphericTurbidityIndex;
	}

	public void setAtmosphericTurbidityIndex(Double atmosphericTurbidityIndex) {
		this.atmosphericTurbidityIndex = atmosphericTurbidityIndex;
	}

	public Double getQ_totalRadiationIrradiance() {
		return Q_totalRadiationIrradiance;
	}

	public void setQ_totalRadiationIrradiance(Double q_totalRadiationIrradiance) {
		Q_totalRadiationIrradiance = q_totalRadiationIrradiance;
	}

	public Double getQ_netRadiationIrradiance() {
		return Q_netRadiationIrradiance;
	}

	public void setQ_netRadiationIrradiance(Double q_netRadiationIrradiance) {
		Q_netRadiationIrradiance = q_netRadiationIrradiance;
	}

	public Double getQ_directRadiationIrradiance() {
		return Q_directRadiationIrradiance;
	}

	public void setQ_directRadiationIrradiance(Double q_directRadiationIrradiance) {
		Q_directRadiationIrradiance = q_directRadiationIrradiance;
	}

	public Double getQ_scatteringRadiationIrradianc() {
		return Q_scatteringRadiationIrradianc;
	}

	public void setQ_scatteringRadiationIrradianc(Double q_scatteringRadiationIrradianc) {
		Q_scatteringRadiationIrradianc = q_scatteringRadiationIrradianc;
	}

	public Double getQ_reflectionRadiationIrradiance() {
		return Q_reflectionRadiationIrradiance;
	}

	public void setQ_reflectionRadiationIrradiance(Double q_reflectionRadiationIrradiance) {
		Q_reflectionRadiationIrradiance = q_reflectionRadiationIrradiance;
	}

	public Double getQ_ultravioletRadiationIrradiance() {
		return Q_ultravioletRadiationIrradiance;
	}

	public void setQ_ultravioletRadiationIrradiance(Double q_ultravioletRadiationIrradiance) {
		Q_ultravioletRadiationIrradiance = q_ultravioletRadiationIrradiance;
	}

	public Double getQ_totalRadiationExposure() {
		return Q_totalRadiationExposure;
	}

	public void setQ_totalRadiationExposure(Double q_totalRadiationExposure) {
		Q_totalRadiationExposure = q_totalRadiationExposure;
	}

	public Double getQ_radiationIrradianceMax() {
		return Q_radiationIrradianceMax;
	}

	public void setQ_radiationIrradianceMax(Double q_radiationIrradianceMax) {
		Q_radiationIrradianceMax = q_radiationIrradianceMax;
	}

	public Double getQ_radiationIrradianceMaxtime() {
		return Q_radiationIrradianceMaxtime;
	}

	public void setQ_radiationIrradianceMaxtime(Double q_radiationIrradianceMaxtime) {
		Q_radiationIrradianceMaxtime = q_radiationIrradianceMaxtime;
	}

	public Double getQ_netRadiationExposure() {
		return Q_netRadiationExposure;
	}

	public void setQ_netRadiationExposure(Double q_netRadiationExposure) {
		Q_netRadiationExposure = q_netRadiationExposure;
	}

	public Double getQ_netRadiationIrradiationMax() {
		return Q_netRadiationIrradiationMax;
	}

	public void setQ_netRadiationIrradiationMax(Double q_netRadiationIrradiationMax) {
		Q_netRadiationIrradiationMax = q_netRadiationIrradiationMax;
	}

	public Double getQ_netRadiationIrradiationMaxtime() {
		return Q_netRadiationIrradiationMaxtime;
	}

	public void setQ_netRadiationIrradiationMaxtime(Double q_netRadiationIrradiationMaxtime) {
		Q_netRadiationIrradiationMaxtime = q_netRadiationIrradiationMaxtime;
	}

	public Double getQ_netRadiationIrradiationMin() {
		return Q_netRadiationIrradiationMin;
	}

	public void setQ_netRadiationIrradiationMin(Double q_netRadiationIrradiationMin) {
		Q_netRadiationIrradiationMin = q_netRadiationIrradiationMin;
	}

	public Double getQ_netRadiationIrradiationMintime() {
		return Q_netRadiationIrradiationMintime;
	}

	public void setQ_netRadiationIrradiationMintime(Double q_netRadiationIrradiationMintime) {
		Q_netRadiationIrradiationMintime = q_netRadiationIrradiationMintime;
	}

	public Double getQ_directRadiationExposure() {
		return Q_directRadiationExposure;
	}

	public void setQ_directRadiationExposure(Double q_directRadiationExposure) {
		Q_directRadiationExposure = q_directRadiationExposure;
	}

	public Double getQ_directRadiationIrradianceMax() {
		return Q_directRadiationIrradianceMax;
	}

	public void setQ_directRadiationIrradianceMax(Double q_directRadiationIrradianceMax) {
		Q_directRadiationIrradianceMax = q_directRadiationIrradianceMax;
	}

	public Double getQ_directRadiationIrradianceMaxtime() {
		return Q_directRadiationIrradianceMaxtime;
	}

	public void setQ_directRadiationIrradianceMaxtime(Double q_directRadiationIrradianceMaxtime) {
		Q_directRadiationIrradianceMaxtime = q_directRadiationIrradianceMaxtime;
	}

	public Double getQ_scatteredRadiationExposure() {
		return Q_scatteredRadiationExposure;
	}

	public void setQ_scatteredRadiationExposure(Double q_scatteredRadiationExposure) {
		Q_scatteredRadiationExposure = q_scatteredRadiationExposure;
	}

	public Double getQ_scatteringRadiationIrradianceMax() {
		return Q_scatteringRadiationIrradianceMax;
	}

	public void setQ_scatteringRadiationIrradianceMax(Double q_scatteringRadiationIrradianceMax) {
		Q_scatteringRadiationIrradianceMax = q_scatteringRadiationIrradianceMax;
	}

	public Double getQ_scatteringRadiationIrradianceMaxtime() {
		return Q_scatteringRadiationIrradianceMaxtime;
	}

	public void setQ_scatteringRadiationIrradianceMaxtime(Double q_scatteringRadiationIrradianceMaxtime) {
		Q_scatteringRadiationIrradianceMaxtime = q_scatteringRadiationIrradianceMaxtime;
	}

	public Double getQ_reflectedRadiationExposure() {
		return Q_reflectedRadiationExposure;
	}

	public void setQ_reflectedRadiationExposure(Double q_reflectedRadiationExposure) {
		Q_reflectedRadiationExposure = q_reflectedRadiationExposure;
	}

	public Double getQ_reflectedRadiationIrradianceMax() {
		return Q_reflectedRadiationIrradianceMax;
	}

	public void setQ_reflectedRadiationIrradianceMax(Double q_reflectedRadiationIrradianceMax) {
		Q_reflectedRadiationIrradianceMax = q_reflectedRadiationIrradianceMax;
	}

	public Double getQ_reflectedRadiationIrradianceMaxtime() {
		return Q_reflectedRadiationIrradianceMaxtime;
	}

	public void setQ_reflectedRadiationIrradianceMaxtime(Double q_reflectedRadiationIrradianceMaxtime) {
		Q_reflectedRadiationIrradianceMaxtime = q_reflectedRadiationIrradianceMaxtime;
	}

	public Double getQ_ultravioletRadiationExposure() {
		return Q_ultravioletRadiationExposure;
	}

	public void setQ_ultravioletRadiationExposure(Double q_ultravioletRadiationExposure) {
		Q_ultravioletRadiationExposure = q_ultravioletRadiationExposure;
	}

	public Double getQ_uvIrradiationMax() {
		return Q_uvIrradiationMax;
	}

	public void setQ_uvIrradiationMax(Double q_uvIrradiationMax) {
		Q_uvIrradiationMax = q_uvIrradiationMax;
	}

	public Double getQ_uvIrradiationMaxtime() {
		return Q_uvIrradiationMaxtime;
	}

	public void setQ_uvIrradiationMaxtime(Double q_uvIrradiationMaxtime) {
		Q_uvIrradiationMaxtime = q_uvIrradiationMaxtime;
	}

	public Double getQ_sunShineHours() {
		return Q_sunShineHours;
	}

	public void setQ_sunShineHours(Double q_sunShineHours) {
		Q_sunShineHours = q_sunShineHours;
	}

	public Double getQ_atmosphericTurbidityIndex() {
		return Q_atmosphericTurbidityIndex;
	}

	public void setQ_atmosphericTurbidityIndex(Double q_atmosphericTurbidityIndex) {
		Q_atmosphericTurbidityIndex = q_atmosphericTurbidityIndex;
	}
	
	
	
}
