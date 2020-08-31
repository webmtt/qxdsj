package cma.cimiss2.dpc.decoder.bean;
// TODO: Auto-generated Javadoc

/**
 * <br>.
 *
 * @author cuihongyuan
 * @Title:  Typh.java
 * @Package cma.cimiss2.dpc.decoder.bean
 * @Description:    TODO(台风实况与预报数据实体类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月6日 上午10:47:11   cuihongyuan    Initial creation.
 * </pre>
 */
public class Typh {
	
	/** The report center. */
	private String reportCenter; //编报(加工)中心	V_CCCC
	
	/** The report center code. */
	private int reportCenterCode; //编报中心代号	V01035
	
	/** The typh name. */
	private String typhName; // 台风名	V_TYPH_NAME	VARCHAR2(20)
	
	/** The typh level. */
	private double typhLevel = 99.0; // 台风等级	V01333	NUMBER(2)
	
	/** The internal code. */
	private int internalCode; //国内编号	V01330	NUMBER(6)
	
	/** The international code. */
	private int internationalCode; //国际编号	V01332	NUMBER(6)
    
    /** The forecast efficiency. */
    private int forecastEfficiency; // 预报时效	V04320	NUMBER(3)
    
    /** The latitude. */
    private double latitude;  //中心纬度(定位)	V05001	NUMBER(10,4)
    
    /** The longtitude. */
    private double longtitude; //中心经度(定位)	V06001	NUMBER(10,4)

    /** The center pressure. */
    private double centerPressure; // 中心气压(定位)	V10004	NUMBER(10,4)	N	单位：百帕
    
    /** The max sustained wind. */
    private double maxSustainedWind; // 中心附近最大持续风风速	V11320	NUMBER(10,4)

    /** The azimuth of wind beyond L 7 01. */
    private double azimuthOfWindBeyondL7_01 = 45;  // 大于等于7级大风的方位1	V11410_07_01	NUMBER(10,4)
    
    /** The azimuth of wind beyond L 7 02. */
    private double azimuthOfWindBeyondL7_02 = 135;  // 大于等于7级大风的方位2	V11410_07_02	NUMBER(10,4)
    
    /** The azimuth of wind beyond L 7 03. */
    private double azimuthOfWindBeyondL7_03 = 225; //  大于等于7级大风的方位3	V11410_07_03	NUMBER(10,4)
    
    /** The azimuth of wind beyond L 7 04. */
    private double azimuthOfWindBeyondL7_04 = 315;  // 大于等于7级大风的方位4	V11410_07_04	NUMBER(10,4)
    
    /** The wind circle radius L 7 01. */
    private double windCircleRadiusL7_01 = 999999.0; // 大于等于7级大风方位1的风圈半径	V11411_07_01	NUMBER(10,4)
    
    /** The wind circle radius L 7 02. */
    private double windCircleRadiusL7_02 = 999999.0;   // 大于等于7级大风方位2的风圈半径	V11411_07_02	NUMBER(10,4)
    
    /** The wind circle radius L 7 03. */
    private double windCircleRadiusL7_03 = 999999.0;   // 大于等于7级大风方位3的风圈半径	V11411_07_03	NUMBER(10,4)
    
    /** The wind circle radius L 7 04. */
    private double windCircleRadiusL7_04 = 999999.0;  //   大于等于7级大风方位4的风圈半径	V11411_07_04	NUMBER(10,4)
   
    /** The azimuth of wind beyond L 10 01. */
    private double azimuthOfWindBeyondL10_01 = 45;//    大于等于10级大风的方位1	V11410_10_01
    
    /** The azimuth of wind beyond L 10 02. */
    private double azimuthOfWindBeyondL10_02 = 135; //  大于等于10级大风的方位2	V11410_10_02
    
    /** The azimuth of wind beyond L 10 03. */
    private double azimuthOfWindBeyondL10_03 = 225;//   大于等于10级大风的方位3	V11410_10_03
    
    /** The azimuth of wind beyond L 10 04. */
    private double azimuthOfWindBeyondL10_04 = 315;//   大于等于10级大风的方位4	V11410_10_04
 
    /** The wind circle radius L 10 01. */
    private double windCircleRadiusL10_01 = 999999.0; //   大于等于10级大风方位1的风圈半径	V11411_10_01
    
    /** The wind circle radius L 10 02. */
    private double windCircleRadiusL10_02 = 999999.0; //   大于等于10级大风方位2的风圈半径	V11411_10_02
    
    /** The wind circle radius L 10 03. */
    private double windCircleRadiusL10_03 = 999999.0; //   大于等于10级大风方位3的风圈半径	V11411_10_03
    
    /** The wind circle radius L 10 04. */
    private double windCircleRadiusL10_04 = 999999.0; //   大于等于10级大风方位4的风圈半径	V11411_10_04

    /** The azimuth of wind beyond L 12 01. */
    private double azimuthOfWindBeyondL12_01 = 45; //大于等于12级大风的方位1	V11410_12_01
    
    /** The azimuth of wind beyond L 12 02. */
    private double azimuthOfWindBeyondL12_02 = 135;  //  大于等于12级大风的方位2	V11410_12_02
    
    /** The azimuth of wind beyond L 12 03. */
    private double azimuthOfWindBeyondL12_03 = 225; //   大于等于12级大风的方位3	V11410_12_03
    
    /** The azimuth of wind beyond L 12 04. */
    private double azimuthOfWindBeyondL12_04 = 315;  //  大于等于12级大风的方位4	V11410_12_04
    
    
    /** The wind circle radius L 12 01. */
    private double windCircleRadiusL12_01 = 999999.0;   //  大于等于12级大风方位1的风圈半径	V11411_12_01
    
    /** The wind circle radius L 12 02. */
    private double windCircleRadiusL12_02 = 999999.0;   //  大于等于12级大风方位2的风圈半径	V11411_12_02
    
    /** The wind circle radius L 12 03. */
    private double windCircleRadiusL12_03 = 999999.0;   //  大于等于12级大风方位3的风圈半径	V11411_12_03
    
    /** The wind circle radius L 12 04. */
    private double windCircleRadiusL12_04 = 999999.0;  //   大于等于12级大风方位4的风圈半径	V11411_12_04

    /** The moving dir. */
    private double movingDir = 999999.0; // 未来移向	V19301	NUMBER(10,4)
    
    /** The moving speed. */
    private double movingSpeed = 999999.0; //未来移速	V19302	NUMBER(10,4)
    
    /** The typh intensity. */
    private int typhIntensity = 999999;// 台风强度	V19303	NUMBER(6)
    
    /** The trend. */
    private int trend; //  未来趋势	V19304	NUMBER(6)

	
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
	
	/**
	 * Gets the report center code.
	 *
	 * @return the report center code
	 */
	public int getReportCenterCode() {
		return reportCenterCode;
	}
	
	/**
	 * Sets the report center code.
	 *
	 * @param reportCenterCode the new report center code
	 */
	public void setReportCenterCode(int reportCenterCode) {
		this.reportCenterCode = reportCenterCode;
	}
	
	/**
	 * Gets the typh name.
	 *
	 * @return the typh name
	 */
	public String getTyphName() {
		return typhName;
	}
	
	/**
	 * Sets the typh name.
	 *
	 * @param typhName the new typh name
	 */
	public void setTyphName(String typhName) {
		this.typhName = typhName;
	}
	
	/**
	 * Gets the typh level.
	 *
	 * @return the typh level
	 */
	public double getTyphLevel() {
		return typhLevel;
	}
	
	/**
	 * Sets the typh level.
	 *
	 * @param typhLevel the new typh level
	 */
	public void setTyphLevel(double typhLevel) {
		this.typhLevel = typhLevel;
	}
	
	/**
	 * Gets the internal code.
	 *
	 * @return the internal code
	 */
	public int getInternalCode() {
		return internalCode;
	}
	
	/**
	 * Sets the internal code.
	 *
	 * @param internalCode the new internal code
	 */
	public void setInternalCode(int internalCode) {
		this.internalCode = internalCode;
	}
	
	/**
	 * Gets the international code.
	 *
	 * @return the international code
	 */
	public int getInternationalCode() {
		return internationalCode;
	}
	
	/**
	 * Sets the international code.
	 *
	 * @param internationalCode the new international code
	 */
	public void setInternationalCode(int internationalCode) {
		this.internationalCode = internationalCode;
	}
	
	/**
	 * Gets the forecast efficiency.
	 *
	 * @return the forecast efficiency
	 */
	public int getForecastEfficiency() {
		return forecastEfficiency;
	}
	
	/**
	 * Sets the forecast efficiency.
	 *
	 * @param forecastEfficiency the new forecast efficiency
	 */
	public void setForecastEfficiency(int forecastEfficiency) {
		this.forecastEfficiency = forecastEfficiency;
	}
	
	/**
	 * Gets the latitude.
	 *
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}
	
	/**
	 * Sets the latitude.
	 *
	 * @param latitude the new latitude
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	/**
	 * Gets the longtitude.
	 *
	 * @return the longtitude
	 */
	public double getLongtitude() {
		return longtitude;
	}
	
	/**
	 * Sets the longtitude.
	 *
	 * @param longtitude the new longtitude
	 */
	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}
	
	/**
	 * Gets the center pressure.
	 *
	 * @return the center pressure
	 */
	public double getCenterPressure() {
		return centerPressure;
	}
	
	/**
	 * Sets the center pressure.
	 *
	 * @param centerPressure the new center pressure
	 */
	public void setCenterPressure(double centerPressure) {
		this.centerPressure = centerPressure;
	}
	
	/**
	 * Gets the max sustained wind.
	 *
	 * @return the max sustained wind
	 */
	public double getMaxSustainedWind() {
		return maxSustainedWind;
	}
	
	/**
	 * Sets the max sustained wind.
	 *
	 * @param maxSustainedWind the new max sustained wind
	 */
	public void setMaxSustainedWind(double maxSustainedWind) {
		this.maxSustainedWind = maxSustainedWind;
	}
	
	/**
	 * Gets the azimuth of wind beyond L 7 01.
	 *
	 * @return the azimuth of wind beyond L 7 01
	 */
	public double getAzimuthOfWindBeyondL7_01() {
		return azimuthOfWindBeyondL7_01;
	}
	
	/**
	 * Sets the azimuth of wind beyond L 7 01.
	 *
	 * @param azimuthOfWindBeyondL7_01 the new azimuth of wind beyond L 7 01
	 */
	public void setAzimuthOfWindBeyondL7_01(double azimuthOfWindBeyondL7_01) {
		this.azimuthOfWindBeyondL7_01 = azimuthOfWindBeyondL7_01;
	}
	
	/**
	 * Gets the azimuth of wind beyond L 7 02.
	 *
	 * @return the azimuth of wind beyond L 7 02
	 */
	public double getAzimuthOfWindBeyondL7_02() {
		return azimuthOfWindBeyondL7_02;
	}
	
	/**
	 * Sets the azimuth of wind beyond L 7 02.
	 *
	 * @param azimuthOfWindBeyondL7_02 the new azimuth of wind beyond L 7 02
	 */
	public void setAzimuthOfWindBeyondL7_02(double azimuthOfWindBeyondL7_02) {
		this.azimuthOfWindBeyondL7_02 = azimuthOfWindBeyondL7_02;
	}
	
	/**
	 * Gets the azimuth of wind beyond L 7 03.
	 *
	 * @return the azimuth of wind beyond L 7 03
	 */
	public double getAzimuthOfWindBeyondL7_03() {
		return azimuthOfWindBeyondL7_03;
	}
	
	/**
	 * Sets the azimuth of wind beyond L 7 03.
	 *
	 * @param azimuthOfWindBeyondL7_03 the new azimuth of wind beyond L 7 03
	 */
	public void setAzimuthOfWindBeyondL7_03(double azimuthOfWindBeyondL7_03) {
		this.azimuthOfWindBeyondL7_03 = azimuthOfWindBeyondL7_03;
	}
	
	/**
	 * Gets the azimuth of wind beyond L 7 04.
	 *
	 * @return the azimuth of wind beyond L 7 04
	 */
	public double getAzimuthOfWindBeyondL7_04() {
		return azimuthOfWindBeyondL7_04;
	}
	
	/**
	 * Sets the azimuth of wind beyond L 7 04.
	 *
	 * @param azimuthOfWindBeyondL7_04 the new azimuth of wind beyond L 7 04
	 */
	public void setAzimuthOfWindBeyondL7_04(double azimuthOfWindBeyondL7_04) {
		this.azimuthOfWindBeyondL7_04 = azimuthOfWindBeyondL7_04;
	}
	
	/**
	 * Gets the wind circle radius L 7 01.
	 *
	 * @return the wind circle radius L 7 01
	 */
	public double getWindCircleRadiusL7_01() {
		return windCircleRadiusL7_01;
	}
	
	/**
	 * Sets the wind circle radius L 7 01.
	 *
	 * @param windCircleRadiusL7_01 the new wind circle radius L 7 01
	 */
	public void setWindCircleRadiusL7_01(double windCircleRadiusL7_01) {
		this.windCircleRadiusL7_01 = windCircleRadiusL7_01;
	}
	
	/**
	 * Gets the wind circle radius L 7 02.
	 *
	 * @return the wind circle radius L 7 02
	 */
	public double getWindCircleRadiusL7_02() {
		return windCircleRadiusL7_02;
	}
	
	/**
	 * Sets the wind circle radius L 7 02.
	 *
	 * @param windCircleRadiusL7_02 the new wind circle radius L 7 02
	 */
	public void setWindCircleRadiusL7_02(double windCircleRadiusL7_02) {
		this.windCircleRadiusL7_02 = windCircleRadiusL7_02;
	}
	
	/**
	 * Gets the wind circle radius L 7 03.
	 *
	 * @return the wind circle radius L 7 03
	 */
	public double getWindCircleRadiusL7_03() {
		return windCircleRadiusL7_03;
	}
	
	/**
	 * Sets the wind circle radius L 7 03.
	 *
	 * @param windCircleRadiusL7_03 the new wind circle radius L 7 03
	 */
	public void setWindCircleRadiusL7_03(double windCircleRadiusL7_03) {
		this.windCircleRadiusL7_03 = windCircleRadiusL7_03;
	}
	
	/**
	 * Gets the wind circle radius L 7 04.
	 *
	 * @return the wind circle radius L 7 04
	 */
	public double getWindCircleRadiusL7_04() {
		return windCircleRadiusL7_04;
	}
	
	/**
	 * Sets the wind circle radius L 7 04.
	 *
	 * @param windCircleRadiusL7_04 the new wind circle radius L 7 04
	 */
	public void setWindCircleRadiusL7_04(double windCircleRadiusL7_04) {
		this.windCircleRadiusL7_04 = windCircleRadiusL7_04;
	}
	
	/**
	 * Gets the azimuth of wind beyond L 10 01.
	 *
	 * @return the azimuth of wind beyond L 10 01
	 */
	public double getAzimuthOfWindBeyondL10_01() {
		return azimuthOfWindBeyondL10_01;
	}
	
	/**
	 * Sets the azimuth of wind beyond L 10 01.
	 *
	 * @param azimuthOfWindBeyondL10_01 the new azimuth of wind beyond L 10 01
	 */
	public void setAzimuthOfWindBeyondL10_01(double azimuthOfWindBeyondL10_01) {
		this.azimuthOfWindBeyondL10_01 = azimuthOfWindBeyondL10_01;
	}
	
	/**
	 * Gets the azimuth of wind beyond L 10 02.
	 *
	 * @return the azimuth of wind beyond L 10 02
	 */
	public double getAzimuthOfWindBeyondL10_02() {
		return azimuthOfWindBeyondL10_02;
	}
	
	/**
	 * Sets the azimuth of wind beyond L 10 02.
	 *
	 * @param azimuthOfWindBeyondL10_02 the new azimuth of wind beyond L 10 02
	 */
	public void setAzimuthOfWindBeyondL10_02(double azimuthOfWindBeyondL10_02) {
		this.azimuthOfWindBeyondL10_02 = azimuthOfWindBeyondL10_02;
	}
	
	/**
	 * Gets the azimuth of wind beyond L 10 03.
	 *
	 * @return the azimuth of wind beyond L 10 03
	 */
	public double getAzimuthOfWindBeyondL10_03() {
		return azimuthOfWindBeyondL10_03;
	}
	
	/**
	 * Sets the azimuth of wind beyond L 10 03.
	 *
	 * @param azimuthOfWindBeyondL10_03 the new azimuth of wind beyond L 10 03
	 */
	public void setAzimuthOfWindBeyondL10_03(double azimuthOfWindBeyondL10_03) {
		this.azimuthOfWindBeyondL10_03 = azimuthOfWindBeyondL10_03;
	}
	
	/**
	 * Gets the azimuth of wind beyond L 10 04.
	 *
	 * @return the azimuth of wind beyond L 10 04
	 */
	public double getAzimuthOfWindBeyondL10_04() {
		return azimuthOfWindBeyondL10_04;
	}
	
	/**
	 * Sets the azimuth of wind beyond L 10 04.
	 *
	 * @param azimuthOfWindBeyondL10_04 the new azimuth of wind beyond L 10 04
	 */
	public void setAzimuthOfWindBeyondL10_04(double azimuthOfWindBeyondL10_04) {
		this.azimuthOfWindBeyondL10_04 = azimuthOfWindBeyondL10_04;
	}
	
	/**
	 * Gets the wind circle radius L 10 01.
	 *
	 * @return the wind circle radius L 10 01
	 */
	public double getWindCircleRadiusL10_01() {
		return windCircleRadiusL10_01;
	}
	
	/**
	 * Sets the wind circle radius L 10 01.
	 *
	 * @param windCircleRadiusL10_01 the new wind circle radius L 10 01
	 */
	public void setWindCircleRadiusL10_01(double windCircleRadiusL10_01) {
		this.windCircleRadiusL10_01 = windCircleRadiusL10_01;
	}
	
	/**
	 * Gets the wind circle radius L 10 02.
	 *
	 * @return the wind circle radius L 10 02
	 */
	public double getWindCircleRadiusL10_02() {
		return windCircleRadiusL10_02;
	}
	
	/**
	 * Sets the wind circle radius L 10 02.
	 *
	 * @param windCircleRadiusL10_02 the new wind circle radius L 10 02
	 */
	public void setWindCircleRadiusL10_02(double windCircleRadiusL10_02) {
		this.windCircleRadiusL10_02 = windCircleRadiusL10_02;
	}
	
	/**
	 * Gets the wind circle radius L 10 03.
	 *
	 * @return the wind circle radius L 10 03
	 */
	public double getWindCircleRadiusL10_03() {
		return windCircleRadiusL10_03;
	}
	
	/**
	 * Sets the wind circle radius L 10 03.
	 *
	 * @param windCircleRadiusL10_03 the new wind circle radius L 10 03
	 */
	public void setWindCircleRadiusL10_03(double windCircleRadiusL10_03) {
		this.windCircleRadiusL10_03 = windCircleRadiusL10_03;
	}
	
	/**
	 * Gets the wind circle radius L 10 04.
	 *
	 * @return the wind circle radius L 10 04
	 */
	public double getWindCircleRadiusL10_04() {
		return windCircleRadiusL10_04;
	}
	
	/**
	 * Sets the wind circle radius L 10 04.
	 *
	 * @param windCircleRadiusL10_04 the new wind circle radius L 10 04
	 */
	public void setWindCircleRadiusL10_04(double windCircleRadiusL10_04) {
		this.windCircleRadiusL10_04 = windCircleRadiusL10_04;
	}
	
	/**
	 * Gets the azimuth of wind beyond L 12 01.
	 *
	 * @return the azimuth of wind beyond L 12 01
	 */
	public double getAzimuthOfWindBeyondL12_01() {
		return azimuthOfWindBeyondL12_01;
	}
	
	/**
	 * Sets the azimuth of wind beyond L 12 01.
	 *
	 * @param azimuthOfWindBeyondL12_01 the new azimuth of wind beyond L 12 01
	 */
	public void setAzimuthOfWindBeyondL12_01(double azimuthOfWindBeyondL12_01) {
		this.azimuthOfWindBeyondL12_01 = azimuthOfWindBeyondL12_01;
	}
	
	/**
	 * Gets the azimuth of wind beyond L 12 02.
	 *
	 * @return the azimuth of wind beyond L 12 02
	 */
	public double getAzimuthOfWindBeyondL12_02() {
		return azimuthOfWindBeyondL12_02;
	}
	
	/**
	 * Sets the azimuth of wind beyond L 12 02.
	 *
	 * @param azimuthOfWindBeyondL12_02 the new azimuth of wind beyond L 12 02
	 */
	public void setAzimuthOfWindBeyondL12_02(double azimuthOfWindBeyondL12_02) {
		this.azimuthOfWindBeyondL12_02 = azimuthOfWindBeyondL12_02;
	}
	
	/**
	 * Gets the azimuth of wind beyond L 12 03.
	 *
	 * @return the azimuth of wind beyond L 12 03
	 */
	public double getAzimuthOfWindBeyondL12_03() {
		return azimuthOfWindBeyondL12_03;
	}
	
	/**
	 * Sets the azimuth of wind beyond L 12 03.
	 *
	 * @param azimuthOfWindBeyondL12_03 the new azimuth of wind beyond L 12 03
	 */
	public void setAzimuthOfWindBeyondL12_03(double azimuthOfWindBeyondL12_03) {
		this.azimuthOfWindBeyondL12_03 = azimuthOfWindBeyondL12_03;
	}
	
	/**
	 * Gets the azimuth of wind beyond L 12 04.
	 *
	 * @return the azimuth of wind beyond L 12 04
	 */
	public double getAzimuthOfWindBeyondL12_04() {
		return azimuthOfWindBeyondL12_04;
	}
	
	/**
	 * Sets the azimuth of wind beyond L 12 04.
	 *
	 * @param azimuthOfWindBeyondL12_04 the new azimuth of wind beyond L 12 04
	 */
	public void setAzimuthOfWindBeyondL12_04(double azimuthOfWindBeyondL12_04) {
		this.azimuthOfWindBeyondL12_04 = azimuthOfWindBeyondL12_04;
	}
	
	/**
	 * Gets the wind circle radius L 12 01.
	 *
	 * @return the wind circle radius L 12 01
	 */
	public double getWindCircleRadiusL12_01() {
		return windCircleRadiusL12_01;
	}
	
	/**
	 * Sets the wind circle radius L 12 01.
	 *
	 * @param windCircleRadiusL12_01 the new wind circle radius L 12 01
	 */
	public void setWindCircleRadiusL12_01(double windCircleRadiusL12_01) {
		this.windCircleRadiusL12_01 = windCircleRadiusL12_01;
	}
	
	/**
	 * Gets the wind circle radius L 12 02.
	 *
	 * @return the wind circle radius L 12 02
	 */
	public double getWindCircleRadiusL12_02() {
		return windCircleRadiusL12_02;
	}
	
	/**
	 * Sets the wind circle radius L 12 02.
	 *
	 * @param windCircleRadiusL12_02 the new wind circle radius L 12 02
	 */
	public void setWindCircleRadiusL12_02(double windCircleRadiusL12_02) {
		this.windCircleRadiusL12_02 = windCircleRadiusL12_02;
	}
	
	/**
	 * Gets the wind circle radius L 12 03.
	 *
	 * @return the wind circle radius L 12 03
	 */
	public double getWindCircleRadiusL12_03() {
		return windCircleRadiusL12_03;
	}
	
	/**
	 * Sets the wind circle radius L 12 03.
	 *
	 * @param windCircleRadiusL12_03 the new wind circle radius L 12 03
	 */
	public void setWindCircleRadiusL12_03(double windCircleRadiusL12_03) {
		this.windCircleRadiusL12_03 = windCircleRadiusL12_03;
	}
	
	/**
	 * Gets the wind circle radius L 12 04.
	 *
	 * @return the wind circle radius L 12 04
	 */
	public double getWindCircleRadiusL12_04() {
		return windCircleRadiusL12_04;
	}
	
	/**
	 * Sets the wind circle radius L 12 04.
	 *
	 * @param windCircleRadiusL12_04 the new wind circle radius L 12 04
	 */
	public void setWindCircleRadiusL12_04(double windCircleRadiusL12_04) {
		this.windCircleRadiusL12_04 = windCircleRadiusL12_04;
	}
	
	/**
	 * Gets the moving dir.
	 *
	 * @return the moving dir
	 */
	public double getMovingDir() {
		return movingDir;
	}
	
	/**
	 * Sets the moving dir.
	 *
	 * @param movingDir the new moving dir
	 */
	public void setMovingDir(double movingDir) {
		this.movingDir = movingDir;
	}
	
	/**
	 * Gets the moving speed.
	 *
	 * @return the moving speed
	 */
	public double getMovingSpeed() {
		return movingSpeed;
	}
	
	/**
	 * Sets the moving speed.
	 *
	 * @param movingSpeed the new moving speed
	 */
	public void setMovingSpeed(double movingSpeed) {
		this.movingSpeed = movingSpeed;
	}
	
	/**
	 * Gets the typh intensity.
	 *
	 * @return the typh intensity
	 */
	public int getTyphIntensity() {
		return typhIntensity;
	}
	
	/**
	 * Sets the typh intensity.
	 *
	 * @param typhIntensity the new typh intensity
	 */
	public void setTyphIntensity(int typhIntensity) {
		this.typhIntensity = typhIntensity;
	}
	
	/**
	 * Gets the trend.
	 *
	 * @return the trend
	 */
	public int getTrend() {
		return trend;
	}
	
	/**
	 * Sets the trend.
	 *
	 * @param trend the new trend
	 */
	public void setTrend(int trend) {
		this.trend = trend;
	}
	
}
