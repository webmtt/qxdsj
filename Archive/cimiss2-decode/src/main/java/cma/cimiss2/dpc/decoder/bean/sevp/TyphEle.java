package cma.cimiss2.dpc.decoder.bean.sevp;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
	台风实况与预报数据要素实体类。
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《台风实况与预报资料数据要素表》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月24日 上午11:38:47   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class TyphEle {
	/**
	 *  预报时效	V04320	NUMBER(3)
	 */
    private int forecastEfficiency = 0; 
    /**
     * 中心纬度(定位)	V05001	NUMBER(10,4)
     */
    private double latitude = 999999;  
    /**
     * 中心经度(定位)	V06001	NUMBER(10,4)
     */
    private double longtitude = 999999; 
    /**
     * 中心气压(定位)	V10004	NUMBER(10,4)	N	单位：百帕
     */
    private double centerPressure = 999998; 
    /**
     * 中心附近最大持续风风速	V11320	NUMBER(10,4)
     */
    private double maxSustainedWind = 999998; 
    /**
     * 中心附近最大阵风风速	V11041
     */
    private double gustSpeed = 999998;
    /**
     * 大于等于7级大风的方位1	V11410_07_01	NUMBER(10,4)
     */
    private double azimuthOfWindBeyondL7_01 = 999998;  
    /**
     * 大于等于7级大风的方位2	V11410_07_02	NUMBER(10,4)
     */
    private double azimuthOfWindBeyondL7_02 = 999998;   
    /**
     * 大于等于7级大风的方位3	V11410_07_03	NUMBER(10,4)
     */
    private double azimuthOfWindBeyondL7_03 = 999998;    
    /**
     *  大于等于7级大风的方位4	V11410_07_04	NUMBER(10,4)
     */
    private double azimuthOfWindBeyondL7_04 = 999998;   
    /**
     * 大于等于7级大风方位1的风圈半径	V11411_07_01	NUMBER(10,4)
     */
    private double windCircleRadiusL7_01 = 999998;   
    /**
     * 大于等于7级大风方位2的风圈半径	V11411_07_02	NUMBER(10,4)
     */
    private double windCircleRadiusL7_02 = 999998;   
    /**
     * 大于等于7级大风方位3的风圈半径	V11411_07_03	NUMBER(10,4)
     */
    private double windCircleRadiusL7_03 = 999998;    
    /**
     *  大于等于7级大风方位4的风圈半径	V11411_07_04	NUMBER(10,4)
     */
    private double windCircleRadiusL7_04 = 999998;   
    /**
     * 大于等于10级大风的方位1	V11410_10_01
     */
    private double azimuthOfWindBeyondL10_01 = 999998;    
    /**
     * 大于等于10级大风的方位2	V11410_10_02
     */
    private double azimuthOfWindBeyondL10_02 = 999998;  
    /**
     *  大于等于10级大风的方位3	V11410_10_03
     */
    private double azimuthOfWindBeyondL10_03 = 999998;  
    /**
     *  大于等于10级大风的方位4	V11410_10_04
     */
    private double azimuthOfWindBeyondL10_04 = 999998;   
    /**
     * 大于等于10级大风方位1的风圈半径	V11411_10_01
     */
    private double windCircleRadiusL10_01 = 999998;    
    /**
     * 大于等于10级大风方位2的风圈半径	V11411_10_02
     */
    private double windCircleRadiusL10_02 = 999998;     
    /**
     * 大于等于10级大风方位3的风圈半径	V11411_10_03
     */
    private double windCircleRadiusL10_03 = 999998;     
    /**
     *  大于等于10级大风方位4的风圈半径	V11411_10_04
     */
    private double windCircleRadiusL10_04 = 999998;   
    /**
     * 大于等于12级大风的方位1	V11410_12_01
     */
    private double azimuthOfWindBeyondL12_01 = 999998; 
    /**
     *  大于等于12级大风的方位2	V11410_12_02
     */
    private double azimuthOfWindBeyondL12_02 = 999998;   
    /**
     *  大于等于12级大风的方位3	V11410_12_03
     */
    private double azimuthOfWindBeyondL12_03 = 999998;    
    /**
     * 大于等于12级大风的方位4	V11410_12_04
     */
    private double azimuthOfWindBeyondL12_04 = 999998;  
    /**
     * 大于等于12级大风方位1的风圈半径	V11411_12_01
     */
    private double windCircleRadiusL12_01 = 999998;    
    /**
     * 大于等于12级大风方位2的风圈半径	V11411_12_02
     */
    private double windCircleRadiusL12_02 = 999998;  
    /**
     *  大于等于12级大风方位3的风圈半径	V11411_12_03
     */
    private double windCircleRadiusL12_03 = 999998;  
    /**
     *  大于等于12级大风方位4的风圈半径	V11411_12_04
     */
    private double windCircleRadiusL12_04 = 999998; 
    /**
     *  未来移向	V19301	NUMBER(10,4)
     */
    private double movingDir = 999998;  
    /**
     *  未来移速	V19302	NUMBER(10,4)
     */
    private double movingSpeed = 999998;  
     /**
      *  台风强度	V19303	NUMBER(6)
      */
    private int typhIntensity = 999998; 
    /**
     *  未来趋势	V19304	NUMBER(6)
     */
    private int trend = 999998;  
	
	public int getForecastEfficiency() {
		return forecastEfficiency;
	}
	public void setForecastEfficiency(int forecastEfficiency) {
		this.forecastEfficiency = forecastEfficiency;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}
	public double getCenterPressure() {
		return centerPressure;
	}
	public void setCenterPressure(double centerPressure) {
		this.centerPressure = centerPressure;
	}
	public double getMaxSustainedWind() {
		return maxSustainedWind;
	}
	public void setMaxSustainedWind(double maxSustainedWind) {
		this.maxSustainedWind = maxSustainedWind;
	}
	public double getAzimuthOfWindBeyondL7_01() {
		return azimuthOfWindBeyondL7_01;
	}
	public void setAzimuthOfWindBeyondL7_01(double azimuthOfWindBeyondL7_01) {
		this.azimuthOfWindBeyondL7_01 = azimuthOfWindBeyondL7_01;
	}
	public double getAzimuthOfWindBeyondL7_02() {
		return azimuthOfWindBeyondL7_02;
	}
	public void setAzimuthOfWindBeyondL7_02(double azimuthOfWindBeyondL7_02) {
		this.azimuthOfWindBeyondL7_02 = azimuthOfWindBeyondL7_02;
	}
	public double getAzimuthOfWindBeyondL7_03() {
		return azimuthOfWindBeyondL7_03;
	}
	public void setAzimuthOfWindBeyondL7_03(double azimuthOfWindBeyondL7_03) {
		this.azimuthOfWindBeyondL7_03 = azimuthOfWindBeyondL7_03;
	}
	public double getAzimuthOfWindBeyondL7_04() {
		return azimuthOfWindBeyondL7_04;
	}
	public void setAzimuthOfWindBeyondL7_04(double azimuthOfWindBeyondL7_04) {
		this.azimuthOfWindBeyondL7_04 = azimuthOfWindBeyondL7_04;
	}
	public double getWindCircleRadiusL7_01() {
		return windCircleRadiusL7_01;
	}
	public void setWindCircleRadiusL7_01(double windCircleRadiusL7_01) {
		this.windCircleRadiusL7_01 = windCircleRadiusL7_01;
	}
	public double getWindCircleRadiusL7_02() {
		return windCircleRadiusL7_02;
	}
	public void setWindCircleRadiusL7_02(double windCircleRadiusL7_02) {
		this.windCircleRadiusL7_02 = windCircleRadiusL7_02;
	}
	public double getWindCircleRadiusL7_03() {
		return windCircleRadiusL7_03;
	}
	public void setWindCircleRadiusL7_03(double windCircleRadiusL7_03) {
		this.windCircleRadiusL7_03 = windCircleRadiusL7_03;
	}
	public double getWindCircleRadiusL7_04() {
		return windCircleRadiusL7_04;
	}
	public void setWindCircleRadiusL7_04(double windCircleRadiusL7_04) {
		this.windCircleRadiusL7_04 = windCircleRadiusL7_04;
	}
	public double getAzimuthOfWindBeyondL10_01() {
		return azimuthOfWindBeyondL10_01;
	}
	public void setAzimuthOfWindBeyondL10_01(double azimuthOfWindBeyondL10_01) {
		this.azimuthOfWindBeyondL10_01 = azimuthOfWindBeyondL10_01;
	}
	public double getAzimuthOfWindBeyondL10_02() {
		return azimuthOfWindBeyondL10_02;
	}
	public void setAzimuthOfWindBeyondL10_02(double azimuthOfWindBeyondL10_02) {
		this.azimuthOfWindBeyondL10_02 = azimuthOfWindBeyondL10_02;
	}
	public double getAzimuthOfWindBeyondL10_03() {
		return azimuthOfWindBeyondL10_03;
	}
	public void setAzimuthOfWindBeyondL10_03(double azimuthOfWindBeyondL10_03) {
		this.azimuthOfWindBeyondL10_03 = azimuthOfWindBeyondL10_03;
	}
	public double getAzimuthOfWindBeyondL10_04() {
		return azimuthOfWindBeyondL10_04;
	}
	public void setAzimuthOfWindBeyondL10_04(double azimuthOfWindBeyondL10_04) {
		this.azimuthOfWindBeyondL10_04 = azimuthOfWindBeyondL10_04;
	}
	public double getWindCircleRadiusL10_01() {
		return windCircleRadiusL10_01;
	}
	public void setWindCircleRadiusL10_01(double windCircleRadiusL10_01) {
		this.windCircleRadiusL10_01 = windCircleRadiusL10_01;
	}
	public double getWindCircleRadiusL10_02() {
		return windCircleRadiusL10_02;
	}
	public void setWindCircleRadiusL10_02(double windCircleRadiusL10_02) {
		this.windCircleRadiusL10_02 = windCircleRadiusL10_02;
	}
	public double getWindCircleRadiusL10_03() {
		return windCircleRadiusL10_03;
	}
	public void setWindCircleRadiusL10_03(double windCircleRadiusL10_03) {
		this.windCircleRadiusL10_03 = windCircleRadiusL10_03;
	}
	public double getWindCircleRadiusL10_04() {
		return windCircleRadiusL10_04;
	}
	public void setWindCircleRadiusL10_04(double windCircleRadiusL10_04) {
		this.windCircleRadiusL10_04 = windCircleRadiusL10_04;
	}
	public double getAzimuthOfWindBeyondL12_01() {
		return azimuthOfWindBeyondL12_01;
	}
	public void setAzimuthOfWindBeyondL12_01(double azimuthOfWindBeyondL12_01) {
		this.azimuthOfWindBeyondL12_01 = azimuthOfWindBeyondL12_01;
	}
	public double getAzimuthOfWindBeyondL12_02() {
		return azimuthOfWindBeyondL12_02;
	}
	public void setAzimuthOfWindBeyondL12_02(double azimuthOfWindBeyondL12_02) {
		this.azimuthOfWindBeyondL12_02 = azimuthOfWindBeyondL12_02;
	}
	public double getAzimuthOfWindBeyondL12_03() {
		return azimuthOfWindBeyondL12_03;
	}
	public void setAzimuthOfWindBeyondL12_03(double azimuthOfWindBeyondL12_03) {
		this.azimuthOfWindBeyondL12_03 = azimuthOfWindBeyondL12_03;
	}
	public double getAzimuthOfWindBeyondL12_04() {
		return azimuthOfWindBeyondL12_04;
	}
	public void setAzimuthOfWindBeyondL12_04(double azimuthOfWindBeyondL12_04) {
		this.azimuthOfWindBeyondL12_04 = azimuthOfWindBeyondL12_04;
	}
	public double getWindCircleRadiusL12_01() {
		return windCircleRadiusL12_01;
	}
	public void setWindCircleRadiusL12_01(double windCircleRadiusL12_01) {
		this.windCircleRadiusL12_01 = windCircleRadiusL12_01;
	}
	public double getWindCircleRadiusL12_02() {
		return windCircleRadiusL12_02;
	}
	public void setWindCircleRadiusL12_02(double windCircleRadiusL12_02) {
		this.windCircleRadiusL12_02 = windCircleRadiusL12_02;
	}
	public double getWindCircleRadiusL12_03() {
		return windCircleRadiusL12_03;
	}
	public void setWindCircleRadiusL12_03(double windCircleRadiusL12_03) {
		this.windCircleRadiusL12_03 = windCircleRadiusL12_03;
	}
	public double getWindCircleRadiusL12_04() {
		return windCircleRadiusL12_04;
	}
	public void setWindCircleRadiusL12_04(double windCircleRadiusL12_04) {
		this.windCircleRadiusL12_04 = windCircleRadiusL12_04;
	}
	public double getMovingDir() {
		return movingDir;
	}
	public void setMovingDir(double movingDir) {
		this.movingDir = movingDir;
	}
	public double getMovingSpeed() {
		return movingSpeed;
	}
	public void setMovingSpeed(double movingSpeed) {
		this.movingSpeed = movingSpeed;
	}
	public int getTyphIntensity() {
		return typhIntensity;
	}
	public void setTyphIntensity(int typhIntensity) {
		this.typhIntensity = typhIntensity;
	}
	public int getTrend() {
		return trend;
	}
	public void setTrend(int trend) {
		this.trend = trend;
	}
	public double getGustSpeed() {
		return gustSpeed;
	}
	public void setGustSpeed(double gustSpeed) {
		this.gustSpeed = gustSpeed;
	}
	
	
}
