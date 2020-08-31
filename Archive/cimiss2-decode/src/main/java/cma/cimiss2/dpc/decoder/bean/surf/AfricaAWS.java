package cma.cimiss2.dpc.decoder.bean.surf;

import java.util.Date;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>

 *
 * <p>
 * notes: 非洲援建解析 实体类
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《》 </a>
 *      <li> <a href=" "> 《》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2019年9月17日 上午9:40:44   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class AfricaAWS {

	private String correctSign;
	/** 第一段
	 * 
	 */
	/**
	 * @Fields stationNumberChina : 区站号  5字节
	 */
	private String stationNumberChina;
	/**
	 * @Fields latitude : 纬度 6字节
	 */
	private double latitude = 999999;

	/**
	 * @Fields longitude : 经度 7字节
	 */
	private double longitude = 999999;
	/**
	 * @Fields heightOfSationGroundAboveMeanSeaLevel : 测站海拔高度  5字节
	 */
	private double heightOfSationGroundAboveMeanSeaLevel = 999998;

	/**
	 * @Fields 气压传感器拔海高度  5字节 
	 */
	private double  heightOfBaromSensor = 999998;
	
	/**
	 * @Fields 观测方式   1字节, 固定存入4
	 */
	private int obserMethod = 4;
	
	/** 
	 *   第二段  52个要素
	 */
	/**
	 *观测时间	14字节	年月日时分秒（国际时，yyyyMMddhhmmss），其中：秒固定为“00”，为正点观测资料时，分记录为“00”
	 */
	private Date obserDateTime;
	/**
	 *
	2分钟风向	3字节	当前时刻的2分钟风向
	*/
	private double windDir_2min = 999998;
	/**
	 *
	2分钟平均风速	3字节	当前时刻的2分钟平均风速
	*/
	private double windSpd_2min =999998;
	/**
	 *
	10分钟风向	3字节	当前时刻的10分钟风向
	*/
	private double windDir_10min = 999998;
	/**
	 *
	10分钟平均风速	3字节	当前时刻的10分钟平均风速
	*/
	private double windSpd_10min = 999998;
	/**
	 *
	最大风速的风向	3字节	每1小时内10分钟最大风速的风向
	*/
	private double windDirOfMaxSpd = 999998;
	/**
	 *
	最大风速	3字节	每1小时内10分钟最大风速
	*/
	private double maxWindSpd = 999998;
	/**
	 *
	最大风速出现时间	4字节	每1小时内10分钟最大风速出现时间，时分各两位，下同
	*/
	private int occurTimeOfMaxWind = 999998;
	/**
	 *
	瞬时风向	3字节	当前时刻的瞬时风向
	*/
	private double windDir = 999998;
	/**
	 *
	瞬时风速	3字节	当前时刻的瞬时风速
	*/
	private double windSpd = 999998;
	/**
	 *
	极大风速的风向	3字节	每1小时内的极大风速的风向
	*/
	private double windDirOfExtremMaxWind = 999998;
	/**
	 *
	极大风速	3字节	每1小时内的极大风速
	*/
	private double extremMaxWindSpd = 999998;
	/**
	 *
	极大风速出现时间	4字节	每1小时内极大风速出现时间
	*/
	private int occurTimeOfExtremMaxWind = 999998;
	/**
	 *
	小时降水量	4字节	每1小时内的雨量累计值
	*/
	private double rainAmountHourly = 999998;
	/**
	 *
	气温	4字节	当前时刻的空气温度
	*/
	private double temperature = 999998;
	/**
	 *
	最高气温	4字节	每1小时内的最高气温
	*/
	private double maxTemperature = 999998;
	/**
	 *
	最高气温出现时间	4字节	每1小时内的最高气温出现时间
	*/
	private int occurTimeOfMaxTemp = 999998;
	/**
	 *
	最低气温	4字节	每1小时内的最低气温
	*/
	private double minTemperature = 999998;
	/**
	 *
	最低气温出现时间	4字节	每1小时内的最低气温出现时间
	*/
	private int occurTimeOfMinTemp = 999998;
	/**
	 *
	相对湿度	3字节	当前时刻的相对湿度
	*/
	private double relativeHumid = 999998;
	/**
	 *
	最小相对湿度	3字节	每1小时内的最小相对湿度值
	*/
	private double minRelativeHumid = 999998;
	/**
	 *
	最小相对湿度出现时间	4字节	每1小时内的最小相对湿度出现时间
	*/
	private int occurTimeOfMinRelativeHumid = 999998;
	/**
	 *
	水汽压	3字节	当前时刻的水汽压值
	*/
	private double vaporPress = 999998;
	/**
	 *
	露点温度	4字节	当前时刻的露点温度值
	*/
	private double dewPoint = 999998;
	/**
	 *
	本站气压	5字节	当前时刻的本站气压值
	*/
	private double stationPress = 999998;
	/**
	 *
	最高本站气压	5字节	每1小时内的最高本站气压值
	*/
	private double maxStationPress = 999998;
	/**
	 *
	最高本站气压出现时间	4字节	每1小时内的最高本站气压出现时间
	*/
	private int occurTimeOfMaxStaPress = 999998;
	/**
	 *
	最低本站气压	5字节	每1小时内的最低本站气压值
	*/
	private double minStationPress = 999998;
	/**
	 *
	最低本站气压出现时间	4字节	每1小时内的最低本站气压出现时间
	*/
	private int occurTimeOfMinStaPress = 999998;
	/**
	 *
	草面（雪面）温度	4字节	当前时刻的草面（雪面）温度值
	*/
	private double grassGroundTemp = 999998;
	/**
	 *
	草面（雪面）最高温度	4字节	每1小时内的草面（雪面）最高温度
	*/
	private double maxGrassGroundTemp = 999998;
	/**
	 *
	草面（雪面）最高出现时间	4字节	每1小时内的草面（雪面）最高温度出现时间
	*/
	private int occurTimeOfMaxGrassGroundTemp = 999998;
	/**
	 *
	草面（雪面）最低温度	4字节	每1小时内的草面（雪面）最低温度
	*/
	private double minGrassGroundTemp = 999998;
	/**
	 *
	草面（雪面）最低出现时间	4字节	每1小时内的草面（雪面）最低温度出现时间
	*/
	private int occurTimeOfMinGrassGroundTemp = 999998;
	/**
	 *
	地面温度	4字节	当前时刻的地面温度值
	*/
	private double groundTemp = 999998;
	/**
	 *
	地面最高温度	4字节	每1小时内的地面最高温度
	*/
	private double maxGroundTemp = 999998;
	/**
	 *
	地面最高出现时间	4字节	每1小时内的地面最高温度出现时间
	*/
	private int occurTimeOfMaxGroundTemp = 999998;
	/**
	 *
	地面最低温度	4字节	每1小时内的地面最低温度
	*/
	private double minGroundTemp = 999998;
	/**
	 *
	地面最低出现时间	4字节	每1小时内的地面最低温度出现时间
	*/
	private int occurTimeOfMinGroundTemp = 999998;
	/**
	 *
	5厘米地温	4字节	当前时刻的5厘米地温值
	*/
	private double groundTemp_5cm = 999998;
	/**
	 *
	10厘米地温	4字节	当前时刻的10厘米地温值
	*/
	private double groundTemp_10cm = 999998;
	/**
	 *
	15厘米地温	4字节	当前时刻的15厘米地温值
	*/
	private double groundTemp_15cm = 999998;
	/**
	 *
	20厘米地温	4字节	当前时刻的20厘米地温值
	*/
	private double groundTemp_20cm = 999998;
	/**
	 *
	40厘米地温	4字节	当前时刻的40厘米地温值
	*/
	private double groundTemp_40cm = 999998;
	/**
	 *
	80厘米地温	4字节	当前时刻的80厘米地温值
	*/
	private double groundTemp_80cm = 999998;
	/**
	 *
	160厘米地温	4字节	当前时刻的160厘米地温值
	*/
	private double groundTemp_160cm = 999998;
	/**
	 *
	320厘米地温		4字节	当前时刻的320厘米地温值
	*/
	private double groundTemp_320cm = 999998;
	/**
	 *
	蒸发量	4字节	每1小时内的蒸发累计量
	*/
	private double vaporHourly = 999998;
	/**
	 *
	海平面气压	5字节	当前时刻的海平面气压值
	*/
	private double seaLevelPress = 999998;
	/**
	 *
	能见度	5字节	当前时刻的能见度
	*/
	private double visibility = 999998;
	/**
	 *
	最小能见度	5字节	每1小时内的最小能见度
	*/
	private double minVisibility = 999998;
	/**
	 *
	最小能见度出现时间	4字节	每1小时内的最小能见度出现时间
	*/ 
	private int occurTimeOfMinVisibility = 999998;
	
	
	/**
	 *  第三段: 小时内分钟降水量，120个字节，每分钟2个字节。每分钟内无降水时存入“00”，微量存入“,,”，降水量≥10.0mm时，一律存入99，缺测存入“//”
	 */
	private double rainMinutely[];
	
	/**
	 *  第四段：该条记录由相应软件自动形成。某时次不需要观测或编码的项目，相应记录或编码用相应位长的“/”填充，例如：9时无编报云量，编报云量记录为///，不需编云、天编码，则云状编码记录为24个“/”、天气现象编码记录为////，6小时降水量组编6///1。
		第4条记录目前只有少数站才有。
	 */
	/**
	1.	能见度	3字节	正点的能见度，由人工输入
	*/
	private double visibilityHourly = 999998;
	/**
	2.	总云量	3字节	正点的总云量，由人工输入
	*/
	private double cloudAmountHourly = 999998;
	/**
	3.	低云量	3字节	正点的低云量，由人工输入
	*/
	private double lowCloudAmountHourly = 999998;
	/**
	4.	编报云量	3字节	正点的低云状或中云状云量，由人工输入
	*/
	private double lowOrMidCloudAmountHourly = 999998;
	/**
	5.	云高	4字节	正点的低（中）云状云高，由人工输入
	*/
	private double heightOfLowOrMidCloudHourly = 999998;
	/**
	6.	云状	24字节	最多8种云，按简码编
	*/
	private int[] cloudShape;
	/**
	7.	云状编码   	3字节	正点的云状编码，由人工输入
	*/
	private String cloudShapeHouly = "999998";
	/**
	8.	天气现象编码	4字节	正点的天气现象编码，由人工输入
	*/
	private int weatherPheno  = 999998;
	/**
	9.	6小时或12小时降水量组编码	5字节	18、0、6、12时（国际时，下同）定时天气报中，编报6RRR1或6RRR2组
	*/
	private double rainAmount_6Hour = 999998;
	/**
	9.1.	6小时或12小时降水量组编码	5字节	18、0、6、12时（国际时，下同）定时天气报中，编报6RRR1或6RRR2组
	*/
	private double rainAmount_12Hour = 999998;
	
	
	/**
	10.1.	24小时变压变温组	5字节	0、3、6、9、12、15、18、21时（国际时，下同）定时天气报中，编报0P24P24 T24T24组
	*/
	private double pressChange_24Hour = 999998;
	/**
	10.2.	24小时变压变温组	5字节	0、3、6、9、12、15、18、21时（国际时，下同）定时天气报中，编报0P24P24 T24T24组
	*/
	private double temChange_24Hour = 999998;
	/**
	11.	24小降水量组编码	5字节	21、0时定时天气报中，编报7R24R24R24R24组
	*/
	private double rainAmount_24Hour = 999998;
	/**
	12.	过去24小时最高气温组	5字节	18、0时定时天气报中，编报1SnTxTxTx组
	*/
	private double maxTemperature_24Hour = 999998;
	/**
	13.	过去24小时最低气温组	5字节	0、6时定时天气报中，编报1SnTnTnTn组
	*/
	private double minTemperature_24Hour = 999998;
	/**
	14.	过去12小时最低地面温度	5字节	0时定时天气报中，编报1SnTgTgTg组
	*/
	private double minTemperature_12Hour = 999998;
	/**
	15.	积雪深度	3字节	0时或6、12时的观测值，由人工输入
	*/
	private double snowDepth_0_6_12Hour = 999998;
	/**
	16.	雪压	3字节	0时或6、12时的观测值，由人工输入
	*/
	private double snowPress_0_6_12Hour	= 999998;
	/**
	17.	冻土深度	3字节	0时最大下限值，由人工输入
	*/
	private double frozenSoilDepth = 999998;
	/**
	18.	地面状态	2字节	6时观测值，由人工输入
	*/
	private double groundState_6Hour = 999998;
	/**
	19.	重要天气极大风速	5字节	18、0、6、12时定时天气报中，编报的911fxfx组
	*/
	private double extremWindSpdOfImportantWeathre = 999998;
	/**
	 * 20.	重要天气极大风速之风向	5字节	18、0、6、12时定时天气报中，编报的915dd组
	 */
	private double dirOfExtremMaxWindOfImportantWeathre = 999998;
	/**
	21.	重要天气尘（龙）卷	5字节	18、0、6、12时定时天气报中，编报的919MwDa组
	*/
	private double tornadoOfImportantWeather = 999998;
	/**
	22.	重要天气雨凇	5字节	18、0、6、12时定时天气报中，编报的934RR组
	*/
	private double  glazeOfImportantWeather = 999998;
	/**
	23.	重要天气冰雹直径	5字节	18、0、6、12时定时天气报中，编报的939nn组
	*/
	private double hailDiameterOfImportantWeather = 999998;
	
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
	public double getHeightOfSationGroundAboveMeanSeaLevel() {
		return heightOfSationGroundAboveMeanSeaLevel;
	}
	public void setHeightOfSationGroundAboveMeanSeaLevel(double heightOfSationGroundAboveMeanSeaLevel) {
		this.heightOfSationGroundAboveMeanSeaLevel = heightOfSationGroundAboveMeanSeaLevel;
	}
	public double getHeightOfBaromSensor() {
		return heightOfBaromSensor;
	}
	public void setHeightOfBaromSensor(double heightOfBaromSensor) {
		this.heightOfBaromSensor = heightOfBaromSensor;
	}
	public int getObserMethod() {
		return obserMethod;
	}
	public void setObserMethod(int obserMethod) {
		this.obserMethod = obserMethod;
	}
	public Date getObserDateTime() {
		return obserDateTime;
	}
	public void setObserDateTime(Date obserDateTime) {
		this.obserDateTime = obserDateTime;
	}
	public double getWindDir_2min() {
		return windDir_2min;
	}
	public void setWindDir_2min(double windDir_2min) {
		this.windDir_2min = windDir_2min;
	}
	public double getWindSpd_2min() {
		return windSpd_2min;
	}
	public void setWindSpd_2min(double windSpd_2min) {
		this.windSpd_2min = windSpd_2min;
	}
	public double getWindDir_10min() {
		return windDir_10min;
	}
	public void setWindDir_10min(double windDir_10min) {
		this.windDir_10min = windDir_10min;
	}
	public double getWindSpd_10min() {
		return windSpd_10min;
	}
	public void setWindSpd_10min(double windSpd_10min) {
		this.windSpd_10min = windSpd_10min;
	}
	public double getWindDirOfMaxSpd() {
		return windDirOfMaxSpd;
	}
	public void setWindDirOfMaxSpd(double windDirOfMaxSpd) {
		this.windDirOfMaxSpd = windDirOfMaxSpd;
	}
	public int getOccurTimeOfMaxWind() {
		return occurTimeOfMaxWind;
	}
	public void setOccurTimeOfMaxWind(int occurTimeOfMaxWind) {
		this.occurTimeOfMaxWind = occurTimeOfMaxWind;
	}
	public double getMaxWindSpd() {
		return maxWindSpd;
	}
	public void setMaxWindSpd(double maxWindSpd) {
		this.maxWindSpd = maxWindSpd;
	}
	public double getWindDir() {
		return windDir;
	}
	public void setWindDir(double windDir) {
		this.windDir = windDir;
	}
	public double getWindSpd() {
		return windSpd;
	}
	public void setWindSpd(double windSpd) {
		this.windSpd = windSpd;
	}
	public double getWindDirOfExtremMaxWind() {
		return windDirOfExtremMaxWind;
	}
	public void setWindDirOfExtremMaxWind(double windDirOfExtremMaxWind) {
		this.windDirOfExtremMaxWind = windDirOfExtremMaxWind;
	}
	public double getExtremMaxWindSpd() {
		return extremMaxWindSpd;
	}
	public void setExtremMaxWindSpd(double extremMaxWindSpd) {
		this.extremMaxWindSpd = extremMaxWindSpd;
	}
	public int getOccurTimeOfExtremMaxWind() {
		return occurTimeOfExtremMaxWind;
	}
	public void setOccurTimeOfExtremMaxWind(int occurTimeOfExtremMaxWind) {
		this.occurTimeOfExtremMaxWind = occurTimeOfExtremMaxWind;
	}
	public double getRainAmountHourly() {
		return rainAmountHourly;
	}
	public void setRainAmountHourly(double rainAmountHourly) {
		this.rainAmountHourly = rainAmountHourly;
	}
	public double getTemperature() {
		return temperature;
	}
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	public int getOccurTimeOfMaxTemp() {
		return occurTimeOfMaxTemp;
	}
	public void setOccurTimeOfMaxTemp(int occurTimeOfMaxTemp) {
		this.occurTimeOfMaxTemp = occurTimeOfMaxTemp;
	}
	public double getMaxTemperature() {
		return maxTemperature;
	}
	public void setMaxTemperature(double maxTemperature) {
		this.maxTemperature = maxTemperature;
	}
	public double getMinTemperature() {
		return minTemperature;
	}
	public void setMinTemperature(double minTemperature) {
		this.minTemperature = minTemperature;
	}
	public int getOccurTimeOfMinTemp() {
		return occurTimeOfMinTemp;
	}
	public void setOccurTimeOfMinTemp(int occurTimeOfMinTemp) {
		this.occurTimeOfMinTemp = occurTimeOfMinTemp;
	}
	public double getMinRelativeHumid() {
		return minRelativeHumid;
	}
	public void setMinRelativeHumid(double minRelativeHumid) {
		this.minRelativeHumid = minRelativeHumid;
	}
	public double getRelativeHumid() {
		return relativeHumid;
	}
	public void setRelativeHumid(double relativeHumid) {
		this.relativeHumid = relativeHumid;
	}
	public int getOccurTimeOfMinRelativeHumid() {
		return occurTimeOfMinRelativeHumid;
	}
	public void setOccurTimeOfMinRelativeHumid(int occurTimeOfMinRelativeHumid) {
		this.occurTimeOfMinRelativeHumid = occurTimeOfMinRelativeHumid;
	}
	public double getVaporPress() {
		return vaporPress;
	}
	public void setVaporPress(double vaporPress) {
		this.vaporPress = vaporPress;
	}
	public double getDewPoint() {
		return dewPoint;
	}
	public void setDewPoint(double dewPoint) {
		this.dewPoint = dewPoint;
	}
	public double getStationPress() {
		return stationPress;
	}
	public void setStationPress(double stationPress) {
		this.stationPress = stationPress;
	}
	public double getMaxStationPress() {
		return maxStationPress;
	}
	public void setMaxStationPress(double maxStationPress) {
		this.maxStationPress = maxStationPress;
	}
	public int getOccurTimeOfMaxStaPress() {
		return occurTimeOfMaxStaPress;
	}
	public void setOccurTimeOfMaxStaPress(int occurTimeOfMaxStaPress) {
		this.occurTimeOfMaxStaPress = occurTimeOfMaxStaPress;
	}
	public double getMinStationPress() {
		return minStationPress;
	}
	public void setMinStationPress(double minStationPress) {
		this.minStationPress = minStationPress;
	}
	public double getGrassGroundTemp() {
		return grassGroundTemp;
	}
	public void setGrassGroundTemp(double grassGroundTemp) {
		this.grassGroundTemp = grassGroundTemp;
	}
	public int getOccurTimeOfMinStaPress() {
		return occurTimeOfMinStaPress;
	}
	public void setOccurTimeOfMinStaPress(int occurTimeOfMinStaPress) {
		this.occurTimeOfMinStaPress = occurTimeOfMinStaPress;
	}
	public int getOccurTimeOfMaxGrassGroundTemp() {
		return occurTimeOfMaxGrassGroundTemp;
	}
	public void setOccurTimeOfMaxGrassGroundTemp(int occurTimeOfMaxGrassGroundTemp) {
		this.occurTimeOfMaxGrassGroundTemp = occurTimeOfMaxGrassGroundTemp;
	}
	public double getMaxGrassGroundTemp() {
		return maxGrassGroundTemp;
	}
	public void setMaxGrassGroundTemp(double maxGrassGroundTemp) {
		this.maxGrassGroundTemp = maxGrassGroundTemp;
	}
	public double getMinGrassGroundTemp() {
		return minGrassGroundTemp;
	}
	public void setMinGrassGroundTemp(double minGrassGroundTemp) {
		this.minGrassGroundTemp = minGrassGroundTemp;
	}
	public double getGroundTemp() {
		return groundTemp;
	}
	public void setGroundTemp(double groundTemp) {
		this.groundTemp = groundTemp;
	}
	public int getOccurTimeOfMinGrassGroundTemp() {
		return occurTimeOfMinGrassGroundTemp;
	}
	public void setOccurTimeOfMinGrassGroundTemp(int occurTimeOfMinGrassGroundTemp) {
		this.occurTimeOfMinGrassGroundTemp = occurTimeOfMinGrassGroundTemp;
	}
	public double getMaxGroundTemp() {
		return maxGroundTemp;
	}
	public void setMaxGroundTemp(double maxGroundTemp) {
		this.maxGroundTemp = maxGroundTemp;
	}
	public int getOccurTimeOfMaxGroundTemp() {
		return occurTimeOfMaxGroundTemp;
	}
	public void setOccurTimeOfMaxGroundTemp(int occurTimeOfMaxGroundTemp) {
		this.occurTimeOfMaxGroundTemp = occurTimeOfMaxGroundTemp;
	}
	public int getOccurTimeOfMinGroundTemp() {
		return occurTimeOfMinGroundTemp;
	}
	public void setOccurTimeOfMinGroundTemp(int occurTimeOfMinGroundTemp) {
		this.occurTimeOfMinGroundTemp = occurTimeOfMinGroundTemp;
	}
	public double getMinGroundTemp() {
		return minGroundTemp;
	}
	public void setMinGroundTemp(double minGroundTemp) {
		this.minGroundTemp = minGroundTemp;
	}
	public double getGroundTemp_5cm() {
		return groundTemp_5cm;
	}
	public void setGroundTemp_5cm(double groundTemp_5cm) {
		this.groundTemp_5cm = groundTemp_5cm;
	}
	public double getGroundTemp_15cm() {
		return groundTemp_15cm;
	}
	public void setGroundTemp_15cm(double groundTemp_15cm) {
		this.groundTemp_15cm = groundTemp_15cm;
	}
	public double getGroundTemp_20cm() {
		return groundTemp_20cm;
	}
	public void setGroundTemp_20cm(double groundTemp_20cm) {
		this.groundTemp_20cm = groundTemp_20cm;
	}
	public double getGroundTemp_10cm() {
		return groundTemp_10cm;
	}
	public void setGroundTemp_10cm(double groundTemp_10cm) {
		this.groundTemp_10cm = groundTemp_10cm;
	}
	public double getGroundTemp_40cm() {
		return groundTemp_40cm;
	}
	public void setGroundTemp_40cm(double groundTemp_40cm) {
		this.groundTemp_40cm = groundTemp_40cm;
	}
	public double getGroundTemp_160cm() {
		return groundTemp_160cm;
	}
	public void setGroundTemp_160cm(double groundTemp_160cm) {
		this.groundTemp_160cm = groundTemp_160cm;
	}
	public double getGroundTemp_80cm() {
		return groundTemp_80cm;
	}
	public void setGroundTemp_80cm(double groundTemp_80cm) {
		this.groundTemp_80cm = groundTemp_80cm;
	}
	public double getVaporHourly() {
		return vaporHourly;
	}
	public void setVaporHourly(double vaporHourly) {
		this.vaporHourly = vaporHourly;
	}
	public double getGroundTemp_320cm() {
		return groundTemp_320cm;
	}
	public void setGroundTemp_320cm(double groundTemp_320cm) {
		this.groundTemp_320cm = groundTemp_320cm;
	}
	public double getSeaLevelPress() {
		return seaLevelPress;
	}
	public void setSeaLevelPress(double seaLevelPress) {
		this.seaLevelPress = seaLevelPress;
	}
	public double getMinVisibility() {
		return minVisibility;
	}
	public void setMinVisibility(double minVisibility) {
		this.minVisibility = minVisibility;
	}
	public double getVisibility() {
		return visibility;
	}
	public void setVisibility(double visibility) {
		this.visibility = visibility;
	}
	public double[] getRainMinutely() {
		return rainMinutely;
	}
	public void setRainMinutely(double rainMinutely[]) {
		this.rainMinutely = rainMinutely;
	}
	public int getOccurTimeOfMinVisibility() {
		return occurTimeOfMinVisibility;
	}
	public void setOccurTimeOfMinVisibility(int occurTimeOfMinVisibility) {
		this.occurTimeOfMinVisibility = occurTimeOfMinVisibility;
	}
	public double getVisibilityHourly() {
		return visibilityHourly;
	}
	public void setVisibilityHourly(double visibilityHourly) {
		this.visibilityHourly = visibilityHourly;
	}
	public double getCloudAmountHourly() {
		return cloudAmountHourly;
	}
	public void setCloudAmountHourly(double cloudAmountHourly) {
		this.cloudAmountHourly = cloudAmountHourly;
	}
	public double getLowCloudAmountHourly() {
		return lowCloudAmountHourly;
	}
	public void setLowCloudAmountHourly(double lowCloudAmountHourly) {
		this.lowCloudAmountHourly = lowCloudAmountHourly;
	}
	public String getCloudShapeHouly() {
		return cloudShapeHouly;
	}
	public void setCloudShapeHouly(String cloudShapeHouly) {
		this.cloudShapeHouly = cloudShapeHouly;
	}

	public double getLowOrMidCloudAmountHourly() {
		return lowOrMidCloudAmountHourly;
	}
	public void setLowOrMidCloudAmountHourly(double lowOrMidCloudAmountHourly) {
		this.lowOrMidCloudAmountHourly = lowOrMidCloudAmountHourly;
	}
	public double getHeightOfLowOrMidCloudHourly() {
		return heightOfLowOrMidCloudHourly;
	}
	public void setHeightOfLowOrMidCloudHourly(double heightOfLowOrMidCloudHourly) {
		this.heightOfLowOrMidCloudHourly = heightOfLowOrMidCloudHourly;
	}
	public int getWeatherPheno() {
		return weatherPheno;
	}
	public void setWeatherPheno(int weatherPheno) {
		this.weatherPheno = weatherPheno;
	}
	public double getRainAmount_24Hour() {
		return rainAmount_24Hour;
	}
	public void setRainAmount_24Hour(double rainAmount_24Hour) {
		this.rainAmount_24Hour = rainAmount_24Hour;
	}
	public double getMaxTemperature_24Hour() {
		return maxTemperature_24Hour;
	}
	public void setMaxTemperature_24Hour(double maxTemperature_24Hour) {
		this.maxTemperature_24Hour = maxTemperature_24Hour;
	}
	public double getMinTemperature_24Hour() {
		return minTemperature_24Hour;
	}
	public void setMinTemperature_24Hour(double minTemperature_24Hour) {
		this.minTemperature_24Hour = minTemperature_24Hour;
	}
	public double getMinTemperature_12Hour() {
		return minTemperature_12Hour;
	}
	public void setMinTemperature_12Hour(double minTemperature_12Hour) {
		this.minTemperature_12Hour = minTemperature_12Hour;
	}
	public double getFrozenSoilDepth() {
		return frozenSoilDepth;
	}
	public void setFrozenSoilDepth(double frozenSoilDepth) {
		this.frozenSoilDepth = frozenSoilDepth;
	}
	public double getSnowDepth_0_6_12Hour() {
		return snowDepth_0_6_12Hour;
	}
	public void setSnowDepth_0_6_12Hour(double snowDepth_0_6_12Hour) {
		this.snowDepth_0_6_12Hour = snowDepth_0_6_12Hour;
	}
	public double getGroundState_6Hour() {
		return groundState_6Hour;
	}
	public void setGroundState_6Hour(double groundState_6Hour) {
		this.groundState_6Hour = groundState_6Hour;
	}
	public double getDirOfExtremMaxWindOfImportantWeathre() {
		return dirOfExtremMaxWindOfImportantWeathre;
	}
	public void setDirOfExtremMaxWindOfImportantWeathre(double dirOfExtremMaxWindOfImportantWeathre) {
		this.dirOfExtremMaxWindOfImportantWeathre = dirOfExtremMaxWindOfImportantWeathre;
	}
	public double getExtremWindSpdOfImportantWeathre() {
		return extremWindSpdOfImportantWeathre;
	}
	public void setExtremWindSpdOfImportantWeathre(double extremWindSpdOfImportantWeathre) {
		this.extremWindSpdOfImportantWeathre = extremWindSpdOfImportantWeathre;
	}
	public double getTornadoOfImportantWeather() {
		return tornadoOfImportantWeather;
	}
	public void setTornadoOfImportantWeather(double tornadoOfImportantWeather) {
		this.tornadoOfImportantWeather = tornadoOfImportantWeather;
	}
	public double getGlazeOfImportantWeather() {
		return glazeOfImportantWeather;
	}
	public void setGlazeOfImportantWeather(double glazeOfImportantWeather) {
		this.glazeOfImportantWeather = glazeOfImportantWeather;
	}
	public double getSnowPress_0_6_12Hour() {
		return snowPress_0_6_12Hour;
	}
	public void setSnowPress_0_6_12Hour(double snowPress_0_6_12Hour) {
		this.snowPress_0_6_12Hour = snowPress_0_6_12Hour;
	}
	public double getHailDiameterOfImportantWeather() {
		return hailDiameterOfImportantWeather;
	}
	public void setHailDiameterOfImportantWeather(double hailDiameterOfImportantWeather) {
		this.hailDiameterOfImportantWeather = hailDiameterOfImportantWeather;
	}
	public int[] getCloudShape() {
		return cloudShape;
	}
	public void setCloudShape(int[] cloudShape) {
		this.cloudShape = cloudShape;
	}
	public double getPressChange_24Hour() {
		return pressChange_24Hour;
	}
	public void setPressChange_24Hour(double pressChange_24Hour) {
		this.pressChange_24Hour = pressChange_24Hour;
	}
	public double getTemChange_24Hour() {
		return temChange_24Hour;
	}
	public void setTemChange_24Hour(double temChange_24Hour) {
		this.temChange_24Hour = temChange_24Hour;
	}
	public double getRainAmount_12Hour() {
		return rainAmount_12Hour;
	}
	public void setRainAmount_12Hour(double rainAmount_12Hour) {
		this.rainAmount_12Hour = rainAmount_12Hour;
	}
	public double getRainAmount_6Hour() {
		return rainAmount_6Hour;
	}
	public void setRainAmount_6Hour(double rainAmount_6Hour) {
		this.rainAmount_6Hour = rainAmount_6Hour;
	}
	public String getCorrectSign() {
		return correctSign;
	}
	public void setCorrectSign(String correctSign) {
		this.correctSign = correctSign;
	}
	
	

}
