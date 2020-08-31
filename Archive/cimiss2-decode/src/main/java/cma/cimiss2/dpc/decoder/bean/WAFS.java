package cma.cimiss2.dpc.decoder.bean;

import java.util.Date;
import java.util.List;
// TODO: Auto-generated Javadoc
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
	
	 /* (non-Javadoc)
 	 * @see java.lang.Object#clone()
 	 */
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
	private int autoStationMark = 999999;
	
	/**
	 * NO: 2.2  <br>
	 * nameCN: 风向 <br>
	 * unit: 度 <br>
	 * BUFR FXY: V11001 <br>
	 * descriptionCN: <br>
	 * decode rule: 取ddd，当为VRB时取999999。<br>
     * field rule:直接赋值
	 */
	private double windDirection = 999999;

	/**
	 * NO: 2.3  <br>
	 * nameCN: 风速<br>
	 * unit: m/s <br>
	 * BUFR FXY: V11002 <br>
	 * descriptionCN: <br>
	 * decode rule: 取ff。当为MPS时取电码值；当为KT时取电码值/2+0.5；当为KMH时取电码值*0.278。<br>
	 * field rule:直接赋值
	 */
	private double windSpeed = 999999;
	
	/**
	 * NO: 2.4  <br>
	 * nameCN: 最大风速 <br>
	 * unit: m/s <br>
	 * BUFR FXY: V11041 <br>
	 * descriptionCN: <br>
	 * decode rule:直接取值，取fmfm <br>
     * field rule:直接赋值
	 */
	private double maxWindSpeed = 999999;
	
	/**
	 * NO: 2.5  <br>
	 * nameCN: 风向反时针变化极值 <br>
	 * unit: 度 <br>
	 * BUFR FXY: V11041_001 <br>
	 * descriptionCN: <br>
	 * decode rule:直接取值，取dndndn <br>
     * field rule:直接赋值
	 */
	private double extremeChangeValueOfWindDireInCounterClockwise = 999999;
	
	/**
	 * NO: 2.6  <br>
	 * nameCN: 风向顺时针变化极值 <br>
	 * unit: 度 <br>
	 * BUFR FXY: V11041_002 <br>
	 * descriptionCN: <br>
	 * decode rule:直接取值，取dxdxdx <br>
     * field rule:直接赋值
	 */
	private double extremeChangeValueOfWindDireInClockwise = 999999;
	
	/**
	 * NO: 2.7  <br>
	 * nameCN: 水平能见度（或最小能见度）  <br>
	 * unit: 1m <br>
	 * BUFR FXY: V20001_001 <br>
	 * descriptionCN: <br>
	 * decode rule:直接取值, 取VVVV<br>
     * field rule:直接赋值
	 */
	private float horizontalVisibility = 999999;
	
	/**
	 * NO: 2.8  <br>
	 * nameCN: 水平能见度方向  <br>
	 * unit: 方位 <br>
	 * BUFR FXY: C05021_001 <br>
	 * descriptionCN: <br>
	 * decode rule:直接取值,取VVVV后的Dv<br>
	 * field rule:直接赋值
	 */
	private String directionOfHorizontalVisibility = "//";
	
	/**
	 * NO: 2.9  <br>
	 * nameCN: 最大水平能见度  <br>
	 * unit: 1m <br>
	 * BUFR FXY: V20001_002 <br>
	 * descriptionCN: <br>
	 * decode rule:直接取值,取VxVxVxVx， 无VxVxVxVx时取缺省值。<br>
	 * field rule:直接赋值
	 */
	private float maxHorizontalVisibility = 999999;
	
	/**
	 * NO: 2.10  <br>
	 * nameCN: 最大水平能见度的方向  <br>
	 * unit: 方位 <br>
	 * BUFR FXY: C05021_002 <br>
	 * descriptionCN: <br>
	 * decode rule:直接取值, 取VxVxVxVx后的Dv，无VxVxVxVx时取空值。<br>
	 * field rule:直接赋值 
	 */
	private String dirOfMaxHorizontalVisibility = "//";
	
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
	private float verticalVisibility = 999999;
	
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
	private float temperature = 999999;
	
	/**
	 * NO: 2.20  <br>
	 * nameCN: 露点 <br>
	 * unit: 摄氏度 <br>
	 * BUFR FXY: V12003 <br>
	 * descriptionCN: <br>
	 * decode rule: 直接取值，取T'T',当为M T'T'时，取（-T'T'）。<br>
	 * field rule:直接赋值
	 */
	private float dewPoint = 999999;
	
	/**
	 * NO: 2.21  <br>
	 * nameCN: 海平面气压 <br>
	 * unit: 百帕  <br>
	 * BUFR FXY: V10051 <br>
	 * descriptionCN: <br>
	 * decode rule: 以Q开头：取PHPHPHPH，以A开头：取PHPHPHPH值х0.338（hPa）。<br>
	 * field rule:直接赋值
	 */
	private double pressureAboveSeaLevel = 999999;
	
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
	
	/**
	 * Gets the auto station mark.
	 *
	 * @return the auto station mark
	 */
	public int getAutoStationMark() {
		return autoStationMark;
	}

	/**
	 * Sets the auto station mark.
	 *
	 * @param autoStationMark the new auto station mark
	 */
	public void setAutoStationMark(int autoStationMark) {
		this.autoStationMark = autoStationMark;
	}

	/**
	 * Gets the wind direction.
	 *
	 * @return the wind direction
	 */
	public double getWindDirection() {
		return windDirection;
	}

	/**
	 * Sets the wind direction.
	 *
	 * @param windDirection the new wind direction
	 */
	public void setWindDirection(double windDirection) {
		this.windDirection = windDirection;
	}

	/**
	 * Gets the wind speed.
	 *
	 * @return the wind speed
	 */
	public double getWindSpeed() {
		return windSpeed;
	}

	/**
	 * Sets the wind speed.
	 *
	 * @param windSpeed the new wind speed
	 */
	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}

	/**
	 * Gets the max wind speed.
	 *
	 * @return the max wind speed
	 */
	public double getMaxWindSpeed() {
		return maxWindSpeed;
	}

	/**
	 * Sets the max wind speed.
	 *
	 * @param maxWindSpeed the new max wind speed
	 */
	public void setMaxWindSpeed(double maxWindSpeed) {
		this.maxWindSpeed = maxWindSpeed;
	}

	/**
	 * Gets the extreme change value of wind dire in counter clockwise.
	 *
	 * @return the extreme change value of wind dire in counter clockwise
	 */
	public double getExtremeChangeValueOfWindDireInCounterClockwise() {
		return extremeChangeValueOfWindDireInCounterClockwise;
	}

	/**
	 * Sets the extreme change value of wind dire in counter clockwise.
	 *
	 * @param extremeChangeValueOfWindDireInCounterClockwise the new extreme change value of wind dire in counter clockwise
	 */
	public void setExtremeChangeValueOfWindDireInCounterClockwise(double extremeChangeValueOfWindDireInCounterClockwise) {
		this.extremeChangeValueOfWindDireInCounterClockwise = extremeChangeValueOfWindDireInCounterClockwise;
	}

	/**
	 * Gets the extreme change value of wind dire in clockwise.
	 *
	 * @return the extreme change value of wind dire in clockwise
	 */
	public double getExtremeChangeValueOfWindDireInClockwise() {
		return extremeChangeValueOfWindDireInClockwise;
	}

	/**
	 * Sets the extreme change value of wind dire in clockwise.
	 *
	 * @param extremeChangeValueOfWindDireInClockwise the new extreme change value of wind dire in clockwise
	 */
	public void setExtremeChangeValueOfWindDireInClockwise(double extremeChangeValueOfWindDireInClockwise) {
		this.extremeChangeValueOfWindDireInClockwise = extremeChangeValueOfWindDireInClockwise;
	}

	/**
	 * Gets the horizontal visibility.
	 *
	 * @return the horizontal visibility
	 */
	public float getHorizontalVisibility() {
		return horizontalVisibility;
	}

	/**
	 * Sets the horizontal visibility.
	 *
	 * @param horizontalVisibility the new horizontal visibility
	 */
	public void setHorizontalVisibility(float horizontalVisibility) {
		this.horizontalVisibility = horizontalVisibility;
	}

	/**
	 * Gets the direction of horizontal visibility.
	 *
	 * @return the direction of horizontal visibility
	 */
	public String getDirectionOfHorizontalVisibility() {
		return directionOfHorizontalVisibility;
	}

	/**
	 * Sets the direction of horizontal visibility.
	 *
	 * @param directionOfHorizontalVisibility the new direction of horizontal visibility
	 */
	public void setDirectionOfHorizontalVisibility(String directionOfHorizontalVisibility) {
		this.directionOfHorizontalVisibility = directionOfHorizontalVisibility;
	}

	/**
	 * Gets the max horizontal visibility.
	 *
	 * @return the max horizontal visibility
	 */
	public float getMaxHorizontalVisibility() {
		return maxHorizontalVisibility;
	}

	/**
	 * Sets the max horizontal visibility.
	 *
	 * @param maxHorizontalVisibility the new max horizontal visibility
	 */
	public void setMaxHorizontalVisibility(float maxHorizontalVisibility) {
		this.maxHorizontalVisibility = maxHorizontalVisibility;
	}

	/**
	 * Gets the dir of max horizontal visibility.
	 *
	 * @return the dir of max horizontal visibility
	 */
	public String getDirOfMaxHorizontalVisibility() {
		return dirOfMaxHorizontalVisibility;
	}

	/**
	 * Sets the dir of max horizontal visibility.
	 *
	 * @param dirOfMaxHorizontalVisibility the new dir of max horizontal visibility
	 */
	public void setDirOfMaxHorizontalVisibility(String dirOfMaxHorizontalVisibility) {
		this.dirOfMaxHorizontalVisibility = dirOfMaxHorizontalVisibility;
	}

	/**
	 * Gets the runway visual range.
	 *
	 * @return the runway visual range
	 */
	public List<Float> getRunwayVisualRange() {
		return runwayVisualRange;
	}

	/**
	 * Sets the runway visual range.
	 *
	 * @param runwayVisualRange the new runway visual range
	 */
	public void setRunwayVisualRange(List<Float> runwayVisualRange) {
		this.runwayVisualRange = runwayVisualRange;
	}

	/**
	 * Gets the runway numbers.
	 *
	 * @return the runway numbers
	 */
	public List<String> getRunwayNumbers() {
		return runwayNumbers;
	}

	/**
	 * Sets the runway numbers.
	 *
	 * @param runwayNumbers the new runway numbers
	 */
	public void setRunwayNumbers(List<String> runwayNumbers) {
		this.runwayNumbers = runwayNumbers;
	}

	/**
	 * Gets the max runway visual range every 10 mins.
	 *
	 * @return the max runway visual range every 10 mins
	 */
	public List<Float> getMaxRunwayVisualRangeEvery10mins() {
		return maxRunwayVisualRangeEvery10mins;
	}

	/**
	 * Sets the max runway visual range every 10 mins.
	 *
	 * @param maxRunwayVisualRangeEvery10mins the new max runway visual range every 10 mins
	 */
	public void setMaxRunwayVisualRangeEvery10mins(List<Float> maxRunwayVisualRangeEvery10mins) {
		this.maxRunwayVisualRangeEvery10mins = maxRunwayVisualRangeEvery10mins;
	}

	/**
	 * Gets the trends of runway viusal range.
	 *
	 * @return the trends of runway viusal range
	 */
	public List<Character> getTrendsOfRunwayViusalRange() {
		return trendsOfRunwayViusalRange;
	}

	/**
	 * Sets the trends of runway viusal range.
	 *
	 * @param trendsOfRunwayViusalRange the new trends of runway viusal range
	 */
	public void setTrendsOfRunwayViusalRange(List<Character> trendsOfRunwayViusalRange) {
		this.trendsOfRunwayViusalRange = trendsOfRunwayViusalRange;
	}

	/**
	 * Gets the weather phenomenons.
	 *
	 * @return the weather phenomenons
	 */
	public List<String> getWeatherPhenomenons() {
		return weatherPhenomenons;
	}

	/**
	 * Sets the weather phenomenons.
	 *
	 * @param weatherPhenomenons the new weather phenomenons
	 */
	public void setWeatherPhenomenons(List<String> weatherPhenomenons) {
		this.weatherPhenomenons = weatherPhenomenons;
	}

	/**
	 * Gets the vertical visibility.
	 *
	 * @return the vertical visibility
	 */
	public float getVerticalVisibility() {
		return verticalVisibility;
	}

	/**
	 * Sets the vertical visibility.
	 *
	 * @param verticalVisibility the new vertical visibility
	 */
	public void setVerticalVisibility(float verticalVisibility) {
		this.verticalVisibility = verticalVisibility;
	}

	/**
	 * Gets the cloud shape and amount.
	 *
	 * @return the cloud shape and amount
	 */
	public List<String> getCloudShapeAndAmount() {
		return cloudShapeAndAmount;
	}

	/**
	 * Sets the cloud shape and amount.
	 *
	 * @param cloudShapeAndAmount the new cloud shape and amount
	 */
	public void setCloudShapeAndAmount(List<String> cloudShapeAndAmount) {
		this.cloudShapeAndAmount = cloudShapeAndAmount;
	}

	/**
	 * Gets the height of cloud base.
	 *
	 * @return the height of cloud base
	 */
	public List<Float> getHeightOfCloudBase() {
		return heightOfCloudBase;
	}

	/**
	 * Sets the height of cloud base.
	 *
	 * @param heightOfCloudBase the new height of cloud base
	 */
	public void setHeightOfCloudBase(List<Float> heightOfCloudBase) {
		this.heightOfCloudBase = heightOfCloudBase;
	}

	/**
	 * Gets the temperature.
	 *
	 * @return the temperature
	 */
	public float getTemperature() {
		return temperature;
	}

	/**
	 * Sets the temperature.
	 *
	 * @param temperature the new temperature
	 */
	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	/**
	 * Gets the dew point.
	 *
	 * @return the dew point
	 */
	public float getDewPoint() {
		return dewPoint;
	}

	/**
	 * Sets the dew point.
	 *
	 * @param dewPoint the new dew point
	 */
	public void setDewPoint(float dewPoint) {
		this.dewPoint = dewPoint;
	}

	/**
	 * Gets the pressure above sea level.
	 *
	 * @return the pressure above sea level
	 */
	public double getPressureAboveSeaLevel() {
		return pressureAboveSeaLevel;
	}

	/**
	 * Sets the pressure above sea level.
	 *
	 * @param pressureAboveSeaLevel the new pressure above sea level
	 */
	public void setPressureAboveSeaLevel(double pressureAboveSeaLevel) {
		this.pressureAboveSeaLevel = pressureAboveSeaLevel;
	}

	/**
	 * Gets the recent weather phenomenons.
	 *
	 * @return the recent weather phenomenons
	 */
	public List<String> getRecentWeatherPhenomenons() {
		return recentWeatherPhenomenons;
	}

	/**
	 * Sets the recent weather phenomenons.
	 *
	 * @param recentWeatherPhenomenons the new recent weather phenomenons
	 */
	public void setRecentWeatherPhenomenons(List<String> recentWeatherPhenomenons) {
		this.recentWeatherPhenomenons = recentWeatherPhenomenons;
	}

	/**
	 * Gets the wind shear runway numbers.
	 *
	 * @return the wind shear runway numbers
	 */
	public List<String> getWindShearRunwayNumbers() {
		return windShearRunwayNumbers;
	}

	/**
	 * Sets the wind shear runway numbers.
	 *
	 * @param windShearRunwayNumbers the new wind shear runway numbers
	 */
	public void setWindShearRunwayNumbers(List<String> windShearRunwayNumbers) {
		this.windShearRunwayNumbers = windShearRunwayNumbers;
	}

	/**
	 * Gets the annotations.
	 *
	 * @return the annotations
	 */
	public String getAnnotations() {
		return annotations;
	}

	/**
	 * Sets the annotations.
	 *
	 * @param annotations the new annotations
	 */
	public void setAnnotations(String annotations) {
		this.annotations = annotations;
	}

	/**
	 * Gets the report type.
	 *
	 * @return the report type
	 */
	public String getReportType() {
		return reportType;
	}

	/**
	 * Sets the report type.
	 *
	 * @param reportType the new report type
	 */
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	/**
	 * Gets the terminal sign.
	 *
	 * @return the terminal sign
	 */
	public String getTerminalSign() {
		return terminalSign;
	}

	/**
	 * Sets the terminal sign.
	 *
	 * @param terminalSign the new terminal sign
	 */
	public void setTerminalSign(String terminalSign) {
		this.terminalSign = terminalSign;
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
	 * Gets the correct sign.
	 *
	 * @return the correct sign
	 */
	public String getCorrectSign() {
		return correctSign;
	}

	/**
	 * Sets the correct sign.
	 *
	 * @param correctSign the new correct sign
	 */
	public void setCorrectSign(String correctSign) {
		this.correctSign = correctSign;
	}

	/**
	 * Gets the report center.
	 *
	 * @return the report center
	 */
	public String getReportCenter() {
		return reportCenter;
	}

	/**
	 * Sets the report center.
	 *
	 * @param reportCenter the new report center
	 */
	public void setReportCenter(String reportCenter) {
		this.reportCenter = reportCenter;
	}
}
