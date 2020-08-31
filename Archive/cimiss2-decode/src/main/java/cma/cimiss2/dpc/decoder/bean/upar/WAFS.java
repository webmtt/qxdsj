package cma.cimiss2.dpc.decoder.bean.upar;

import java.util.Date;
import java.util.List;
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
	飞机地面天气报资料实体类
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《WAFS机场地面天气资料数据表.docx》 </a>
 *      <li> <a href=" "> 《民航气象中心传输资料格式说明--for气象局PA3.docx》 </a>
 *     <li> <a href=" ">  《机场天气报告METAR.docx》 </a>
 *     <li> <a href=" ">  《metar报处理.docx》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月27日 上午8:32:19   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class WAFS implements Cloneable{
	
	 @Override  
	 public Object clone() {
		 WAFS wafs = null;
		 try{
			 wafs = (WAFS) super.clone();
		 }catch (CloneNotSupportedException  e) {
			 e.printStackTrace(); 
		}
		 return wafs;
	 }
	/**
	 * NO: 1.1  <br>
	 * nameCN: METAR(SA):日常机场天气报告; SPECI(SP):特殊机场天气报告 <br>
	 * unit: <br>
	 * BUFR FXY: C_TYPE <br>
	 * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private String reportType;
	
	/**
	 * NO: 1.2  <br>
	 * nameCN: 航站标识  <br>
	 * unit: <br>
	 * BUFR FXY: V_OBCC <br>
	 * descriptionCN: 
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private String terminalSign;
	
	/**
	 * NO: 1.3  <br>
	 * nameCN: 资料时间  <br>
	 * unit: <br>
	 * BUFR FXY: D_DATETIME <br>
	 * descriptionCN: 
     * decode rule:直接取值<br>
     * field rule:使用java时间转化工具类进行转化,标准形式为：yyyyMMddHHmmss
	 */
	private Date observationTime;
	
	/**
	 * NO: 1.4  <br>
	 * nameCN: 更正标志  <br>
	 * unit: <br>
	 * BUFR FXY: V_BBB <br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private String correctSign = "000";
	
	/**
	 * NO: 1.5  <br>
	 * nameCN: 编报中心  <br>
	 * unit: <br>
	 * BUFR FXY: CCCC <br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private String reportCenter;
	
	/**
	 * NO: 2.1  <br>
	 * nameCN: 自动站标识 <br>
	 * unit: <br>
	 * BUFR FXY: V02001 <br>
	 * descriptionCN: <br>
	 * decode rule:直接取值<br>
     * field rule:直接赋值
	 */
	private int autoStationMark = 999998;
	
	/**
	 * NO: 2.2  <br>
	 * nameCN: 风向 <br>
	 * unit: 度 <br>
	 * BUFR FXY: V11001 <br>
	 * descriptionCN: <br>
	 * decode rule: 取ddd，当为VRB时取999998。<br>
     * field rule:直接赋值
	 */
	private double windDirection = 999998;

	/**
	 * NO: 2.3  <br>
	 * nameCN: 风速<br>
	 * unit: m/s <br>
	 * BUFR FXY: V11002 <br>
	 * descriptionCN: <br>
	 * decode rule: 取ff。当为MPS时取电码值；当为KT时取电码值/2+0.5；当为KMH时取电码值*0.278。<br>
	 * field rule:直接赋值
	 */
	private double windSpeed = 999998;
	
	/**
	 * NO: 2.4  <br>
	 * nameCN: 最大风速 <br>
	 * unit: m/s <br>
	 * BUFR FXY: V11041 <br>
	 * descriptionCN: <br>
	 * decode rule:直接取值，取fmfm <br>
     * field rule:直接赋值
	 */
	private double maxWindSpeed = 999998;
	
	/**
	 * NO: 2.5  <br>
	 * nameCN: 风向反时针变化极值 <br>
	 * unit: 度 <br>
	 * BUFR FXY: V11041_001 <br>
	 * descriptionCN: <br>
	 * decode rule:直接取值，取dndndn <br>
     * field rule:直接赋值
	 */
	private double extremeChangeValueOfWindDireInCounterClockwise = 999998;
	
	/**
	 * NO: 2.6  <br>
	 * nameCN: 风向顺时针变化极值 <br>
	 * unit: 度 <br>
	 * BUFR FXY: V11041_002 <br>
	 * descriptionCN: <br>
	 * decode rule:直接取值，取dxdxdx <br>
     * field rule:直接赋值
	 */
	private double extremeChangeValueOfWindDireInClockwise = 999998;
	
	/**
	 * NO: 2.7  <br>
	 * nameCN: 水平能见度（或最小能见度）  <br>
	 * unit: 1m <br>
	 * BUFR FXY: V20001_001 <br>
	 * descriptionCN: <br>
	 * decode rule:直接取值, 取VVVV<br>
     * field rule:直接赋值
	 */
	private float horizontalVisibility = 999998;
	
	/**
	 * NO: 2.8  <br>
	 * nameCN: 水平能见度方向  <br>
	 * unit: 方位 <br>
	 * BUFR FXY: C05021_001 <br>
	 * descriptionCN: <br>
	 * decode rule:直接取值,取VVVV后的Dv<br>
	 * field rule:直接赋值
	 */
	private String directionOfHorizontalVisibility = "999998";
	
	/**
	 * NO: 2.9  <br>
	 * nameCN: 最大水平能见度  <br>
	 * unit: 1m <br>
	 * BUFR FXY: V20001_002 <br>
	 * descriptionCN: <br>
	 * decode rule:直接取值,取VxVxVxVx， 无VxVxVxVx时取缺省值。<br>
	 * field rule:直接赋值
	 */
	private float maxHorizontalVisibility = 999998;
	
	/**
	 * NO: 2.10  <br>
	 * nameCN: 最大水平能见度的方向  <br>
	 * unit: 方位 <br>
	 * BUFR FXY: C05021_002 <br>
	 * descriptionCN: <br>
	 * decode rule:直接取值, 取VxVxVxVx后的Dv，无VxVxVxVx时取空值。<br>
	 * field rule:直接赋值 
	 */
	private String dirOfMaxHorizontalVisibility = "999998";
	
	/**
	 * NO: 2.11  <br>
	 * nameCN:  跑道编号 1~5  <br>
	 * unit: <br>
	 * BUFR FXY: C_RW1 ~ C_RW5 <br>
	 * descriptionCN: <br>
	 * decode rule: 直接取值，取第一组DRDR<br>
	 * field rule: 直接赋值 
	 */
	private List<String> runwayNumbers;
	
	/**
	 * NO: 2.12  <br>
	 * nameCN: 跑道视程（或10分钟内最小平均跑道视程） 1~5  <br>
	 * unit: 1m <br>
	 * BUFR FXY: V_RWVMIN1 ~ V_RWVMIN5 <br>
	 * descriptionCN: <br>
	 * decode rule: 直接取值，取VRVRVRVR或MVRVRVRVR<br>
	 * field rule: 直接赋值 
	 */
	private List<Float> runwayVisualRange;
	
	/**
	 * NO: 2.13  <br>
	 * nameCN: 10分钟内最大平均跑道视程 1~5  <br>
	 * unit: 1m <br>
	 * BUFR FXY: V_RWVMAX1 ~ V_RWVMAX5 <br>
	 * descriptionCN: <br>
	 * decode rule: 直接取值, 取PVRVRVRVR，无此组时给缺省值。<br>
	 * field rule: 直接赋值 
	 */
	private List<Float> maxRunwayVisualRangeEvery10mins;
	
	/**
	 * NO: 2.14  <br>
	 * nameCN: 跑道视程变化趋势 1~5  <br>
	 * unit: <br>
	 * BUFR FXY: C_RWVT1 ~ C_RWVT5 <br>
	 * descriptionCN: <br>
	 * decode rule: 直接取值, 取电码值i<br>
	 * field rule: 直接赋值 
	 */
	private List<Character> trendsOfRunwayViusalRange;
	
	/**
	 * NO: 2.15  <br>
	 * nameCN: 天气现象1~3 <br>
	 * unit: <br>
	 * BUFR FXY: C20003_001 ~ C20003_003 <br>
	 * descriptionCN: <br>
	 * decode rule: 直接取值<br>
	 * field rule: 直接赋值 
	 */
	private List<String> weatherPhenomenons;
	
	/**
	 * NO: 2.16  <br>
	 * nameCN: 垂直能见度 <br>
	 * unit: 1m <br>
	 * BUFR FXY: V20002 <br>
	 * descriptionCN: <br>
	 * decode rule: 直接取值，当有指示码VV时取hshshs<br>
	 * field rule:直接赋值
	 */
	private float verticalVisibility = 999998;
	
	/**
	 * NO: 2.17  <br>
	 * nameCN: 云状及云量 <br>
	 * unit: <br>
	 * BUFR FXY: C20011_001 ~ C20011_003 <br>
	 * descriptionCN: <br>
	 * decode rule: 直接取值，取NsNsNs<br>
	 * field rule:直接赋值
	 */
	private List<String> cloudShapeAndAmount;
	
	/**
	 * NO: 2.18  <br>
	 * nameCN: 云底高度  <br>
	 * unit: 1m <br>
	 * BUFR FXY: V20013_001 ~ V20013_003 <br>
	 * descriptionCN: <br>
	 * decode rule: 直接取值，取hshshs<br>
	 * field rule:直接赋值
	 */
	private List<Float> heightOfCloudBase;
	
	/**
	 * NO: 2.19  <br>
	 * nameCN: 温度  <br>
	 * unit: 摄氏度 <br>
	 * BUFR FXY: V12001 <br>
	 * descriptionCN: <br>
	 * decode rule: 直接取值，取T'T'；当为M T'T'时，取（-T'T'）。 <br>
	 * field rule:直接赋值
	 */
	private float temperature = 999998;
	
	/**
	 * NO: 2.20  <br>
	 * nameCN: 露点 <br>
	 * unit: 摄氏度 <br>
	 * BUFR FXY: V12003 <br>
	 * descriptionCN: <br>
	 * decode rule: 直接取值，取T'T',当为M T'T'时，取（-T'T'）。<br>
	 * field rule:直接赋值
	 */
	private float dewPoint = 999998;
	
	/**
	 * NO: 2.21  <br>
	 * nameCN: 海平面气压 <br>
	 * unit: 百帕  <br>
	 * BUFR FXY: V10051 <br>
	 * descriptionCN: <br>
	 * decode rule: 以Q开头：取PHPHPHPH，以A开头：取PHPHPHPH值х0.338（hPa）。<br>
	 * field rule:直接赋值
	 */
	private double pressureAboveSeaLevel = 999998;
	
	/**
	 * NO: 2.22  <br>
	 * nameCN: 近时天气现象1~3 <br>
	 * unit: <br>
	 * BUFR FXY: C20003_004 ~C20003_006<br>
	 * descriptionCN: <br>
	 * decode rule: 取RE后的W’W’<br>
	 * field rule:直接赋值
	 */
	private List<String> recentWeatherPhenomenons;

	/**
	 * NO: 2.23  <br>
	 * nameCN: 风切变跑道号1~3 <br>
	 * unit: <br>
	 * BUFR FXY: C_RWY1 ~C_RWY3<br>
	 * descriptionCN: <br>
	 * decode rule: 取WS后的第一组跑道号WS 后为ALL则取值‘ALL’<br>
	 * field rule:直接赋值
	 */
	private List<String> windShearRunwayNumbers;
	
	/**
	 * NO: 2.24  <br>
	 * nameCN: 附注组 <br>
	 * unit: <br>
	 * BUFR FXY: C_RMK <br>
	 * descriptionCN: <br>
	 * decode rule:取RMK后的全部内容，此字段为可空。<br>
	 * field rule: 直接赋值
	 */
	private String annotations = "";

//	private double stationPressure = 999999;
	
	public int getAutoStationMark() {
		return autoStationMark;
	}

	public void setAutoStationMark(int autoStationMark) {
		this.autoStationMark = autoStationMark;
	}

	public double getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(double windDirection) {
		this.windDirection = windDirection;
	}

	public double getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}

	public double getMaxWindSpeed() {
		return maxWindSpeed;
	}

	public void setMaxWindSpeed(double maxWindSpeed) {
		this.maxWindSpeed = maxWindSpeed;
	}

	public double getExtremeChangeValueOfWindDireInCounterClockwise() {
		return extremeChangeValueOfWindDireInCounterClockwise;
	}

	public void setExtremeChangeValueOfWindDireInCounterClockwise(double extremeChangeValueOfWindDireInCounterClockwise) {
		this.extremeChangeValueOfWindDireInCounterClockwise = extremeChangeValueOfWindDireInCounterClockwise;
	}

	public double getExtremeChangeValueOfWindDireInClockwise() {
		return extremeChangeValueOfWindDireInClockwise;
	}

	public void setExtremeChangeValueOfWindDireInClockwise(double extremeChangeValueOfWindDireInClockwise) {
		this.extremeChangeValueOfWindDireInClockwise = extremeChangeValueOfWindDireInClockwise;
	}

	public float getHorizontalVisibility() {
		return horizontalVisibility;
	}

	public void setHorizontalVisibility(float horizontalVisibility) {
		this.horizontalVisibility = horizontalVisibility;
	}

	public String getDirectionOfHorizontalVisibility() {
		return directionOfHorizontalVisibility;
	}

	public void setDirectionOfHorizontalVisibility(String directionOfHorizontalVisibility) {
		this.directionOfHorizontalVisibility = directionOfHorizontalVisibility;
	}

	public float getMaxHorizontalVisibility() {
		return maxHorizontalVisibility;
	}

	public void setMaxHorizontalVisibility(float maxHorizontalVisibility) {
		this.maxHorizontalVisibility = maxHorizontalVisibility;
	}

	public String getDirOfMaxHorizontalVisibility() {
		return dirOfMaxHorizontalVisibility;
	}

	public void setDirOfMaxHorizontalVisibility(String dirOfMaxHorizontalVisibility) {
		this.dirOfMaxHorizontalVisibility = dirOfMaxHorizontalVisibility;
	}

	public List<Float> getRunwayVisualRange() {
		return runwayVisualRange;
	}

	public void setRunwayVisualRange(List<Float> runwayVisualRange) {
		this.runwayVisualRange = runwayVisualRange;
	}

	public List<String> getRunwayNumbers() {
		return runwayNumbers;
	}

	public void setRunwayNumbers(List<String> runwayNumbers) {
		this.runwayNumbers = runwayNumbers;
	}

	public List<Float> getMaxRunwayVisualRangeEvery10mins() {
		return maxRunwayVisualRangeEvery10mins;
	}

	public void setMaxRunwayVisualRangeEvery10mins(List<Float> maxRunwayVisualRangeEvery10mins) {
		this.maxRunwayVisualRangeEvery10mins = maxRunwayVisualRangeEvery10mins;
	}

	public List<Character> getTrendsOfRunwayViusalRange() {
		return trendsOfRunwayViusalRange;
	}

	public void setTrendsOfRunwayViusalRange(List<Character> trendsOfRunwayViusalRange) {
		this.trendsOfRunwayViusalRange = trendsOfRunwayViusalRange;
	}

	public List<String> getWeatherPhenomenons() {
		return weatherPhenomenons;
	}

	public void setWeatherPhenomenons(List<String> weatherPhenomenons) {
		this.weatherPhenomenons = weatherPhenomenons;
	}

	public float getVerticalVisibility() {
		return verticalVisibility;
	}

	public void setVerticalVisibility(float verticalVisibility) {
		this.verticalVisibility = verticalVisibility;
	}

	public List<String> getCloudShapeAndAmount() {
		return cloudShapeAndAmount;
	}

	public void setCloudShapeAndAmount(List<String> cloudShapeAndAmount) {
		this.cloudShapeAndAmount = cloudShapeAndAmount;
	}

	public List<Float> getHeightOfCloudBase() {
		return heightOfCloudBase;
	}

	public void setHeightOfCloudBase(List<Float> heightOfCloudBase) {
		this.heightOfCloudBase = heightOfCloudBase;
	}

	public float getTemperature() {
		return temperature;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	public float getDewPoint() {
		return dewPoint;
	}

	public void setDewPoint(float dewPoint) {
		this.dewPoint = dewPoint;
	}

	public double getPressureAboveSeaLevel() {
		return pressureAboveSeaLevel;
	}

	public void setPressureAboveSeaLevel(double pressureAboveSeaLevel) {
		this.pressureAboveSeaLevel = pressureAboveSeaLevel;
	}

	public List<String> getRecentWeatherPhenomenons() {
		return recentWeatherPhenomenons;
	}

	public void setRecentWeatherPhenomenons(List<String> recentWeatherPhenomenons) {
		this.recentWeatherPhenomenons = recentWeatherPhenomenons;
	}

	public List<String> getWindShearRunwayNumbers() {
		return windShearRunwayNumbers;
	}

	public void setWindShearRunwayNumbers(List<String> windShearRunwayNumbers) {
		this.windShearRunwayNumbers = windShearRunwayNumbers;
	}

	public String getAnnotations() {
		return annotations;
	}

	public void setAnnotations(String annotations) {
		this.annotations = annotations;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getTerminalSign() {
		return terminalSign;
	}

	public void setTerminalSign(String terminalSign) {
		this.terminalSign = terminalSign;
	}

	public Date getObservationTime() {
		return observationTime;
	}

	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}

	public String getCorrectSign() {
		return correctSign;
	}

	public void setCorrectSign(String correctSign) {
		this.correctSign = correctSign;
	}

	public String getReportCenter() {
		return reportCenter;
	}

	public void setReportCenter(String reportCenter) {
		this.reportCenter = reportCenter;
	}

}
