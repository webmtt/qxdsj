package cma.cimiss2.dpc.decoder.bean.ocean;

import java.util.Date;
import java.util.List;

/**
 * 
 * <br>
 * @Title:  WaveInfo.java   
 * @Package cma.cimiss2.dpc.decoder.bean.ocean   
 * @Description:    TODO(海上测站或远程平台（飞机或卫星）的波浪信息报告实体类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年7月30日 上午11:13:59   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 *
 */
public class WaveInfo {
	
	/**
	 * NO:   <br>
	 * nameCN: 更正标志  <br>
	 * unit: <br>
	 * BUFR FXY: V_BBB <br>
	 * descriptionCN: 
	 */
	private String correctSign = "000";
	
	/**
	 * NO:   <br>
	 * nameCN: 编报中心  <br>
	 * unit: <br>
	 * BUFR FXY: CCCC <br>
	 * descriptionCN: 
	 */
	private String reportCenter;
	
	/**
	 * NO: 0.0 <br>
	 * nameCN: 报文类型 <br>
	 * unit: <br>
	 * BUFR FXY:  <br>
	 * descriptionCN: 
	 */
	private String type = "";
	
	/**
	 * NO: 0.1 <br>
	 * nameCN: 呼号或海上观测平台编号 <br>
	 * unit: <br>
	 * BUFR FXY:  <br>
	 * descriptionCN: 
	 */
	private String  buoyIdentifier = "";
	
	/**
	 * NO: 0.2 <br>
	 * nameCN: 观测时间 <br>
	 * unit:  <br>
	 * BUFR FXY:  <br>
	 * descriptionCN: 日月年  时分
	 */
	private Date observationTime;
	
	/**
	 * NO: 0.3 <br>
	 * nameCN: 区站号 <br>
	 * unit: <br>
	 * BUFR FXY: V01301 <br>
	 * descriptionCN: 
	 */
	private String stationNumberChina;
	
	/**
	 * NO: 0.3-1  <br>
	 * nameCN: 纬度 <br>
	 * unit:  度  <br>
	 * BUFR FXY: V05001 <br>
	 * descriptionCN:
	 */
	private double latitude = 999999.0;
	
	/**
	 * NO: 0.3-2  <br>
	 * nameCN: 经度 <br>
	 * unit:  度  <br>
	 * BUFR FXY: V06001 <br>
	 * descriptionCN:
	 */
	private double longtitude = 999999.0;
	
	/**
	 * NO: 0.4-1  <br>
	 * nameCN: 波数的频率指示码 <br>
	 * unit:    <br>
	 * BUFR FXY:  <br>
	 * descriptionCN:
	 */
	private int Ia = 999999;

	/**
	 * NO: 0.4-2  <br>
	 * nameCN: 频谱数据的计算方法指示码 <br>
	 * unit:    <br>
	 * BUFR FXY:  <br>
	 * descriptionCN:
	 */
	private int Im = 999999;
	
	/**
	 * NO: 0.4-3  <br>
	 * nameCN: 平台类型指示码 <br>
	 * unit:    <br>
	 * BUFR FXY:  <br>
	 * descriptionCN:
	 */
	private int Ip = 999999;
	
	/**
	 * NO: 0.5  <br>
	 * nameCN: 水深 <br>
	 * unit:  米  <br>
	 * BUFR FXY:  <br>
	 * descriptionCN: 
	 */
	private double depthOfWater = 999999;
	
	/**
	 * NO: 0.6  <br>
	 * nameCN: 有效波高 <br>
	 * unit:  厘米  <br>
	 * BUFR FXY:  <br>
	 * descriptionCN: 
	 */
	private double significantWaveHeight = 999999;
	
	/**
	 * NO: 0.7  <br>
	 * nameCN: 频谱峰值周期 <br>
	 * unit:    <br>
	 * BUFR FXY:  <br>
	 * descriptionCN: 
	 */
	private double cycleOfPeakSpectrum = 999999;
	
	/**
	 * NO: 0.8 <br>
	 * nameCN: 最大浪高  <br>
	 * unit:  位势十米  <br>
	 * BUFR FXY:  <br>
	 * descriptionCN: 
	 */
	private double maxWaveHeight = 999999;
	
	/**
	 * NO: 0.9 <br>
	 * nameCN: 平均波周期  <br>
	 * unit:    <br>
	 * BUFR FXY:  <br>
	 * descriptionCN: （十分之一秒，或以米为单位的平均波长）
	 */
	private double cycleOfAvgWave = 999999;
	
	/**
	 * NO: 0.10 <br>
	 * nameCN: 估算的斜率传感器的有效波高 <br>
	 * unit:   厘米 <br>
	 * BUFR FXY:  <br>
	 * descriptionCN:
	 */
	private double waveHeightOfSensor = 999999;
	
	/**
	 * NO: 0.11 <br>
	 * nameCN: 由斜率传感器得获得的光谱峰值周期，或者波长的频谱峰值<br>
	 * unit:  米 <br>
	 * BUFR FXY:  <br>
	 * descriptionCN:
	 */
	private double spectrumPeak = 999999;
	
	/**
	 * NO: 0.12 <br>
	 * nameCN: 来源于斜率传感器的平均周期 <br>
	 * unit:  （十分之一秒，或以米为单位的平均波长）  <br>
	 * BUFR FXY:  <br>
	 * descriptionCN:
	 */
	private double avgCycleOfSensor = 999999;
	
	/**
	 * NO: 0.13 <br>
	 * nameCN: 以4度为单位的真实方向<br>
	 * unit:   <br>
	 * BUFR FXY:  <br>
	 * descriptionCN:
	 */
	private double trueDir = 999999;
	
	/**
	 * NO: 1.0 <br>
	 * nameCN: 频段总数  <br>
	 * unit:   <br>
	 * BUFR FXY:  <br>
	 * descriptionCN:
	 */
	private int bandsNumber = 999999;
	
	/**
	 * NO: 1.1 <br>
	 * nameCN: 采样间隔 <br>
	 * unit: 十分位秒或米 <br>
	 * BUFR FXY:  <br>
	 * descriptionCN:
	 */
	private double sampleInterval = 999999; 
	
	/**
	 * NO: 1.2 <br>
	 * nameCN: 国际水域标识码 <br>
	 * unit:   <br>
	 * BUFR FXY:  <br>
	 * descriptionCN:
	 */
	private List<Integer> subBandNum;
	
	
	/**
	 * NO: 1.3 <br>
	 * nameCN: 波浪的持续时间（秒），或者波浪长度（十米） <br>
	 * unit:   <br>
	 * BUFR FXY:  <br>
	 * descriptionCN:
	 */
	private double waveDuration = 999999;
	
	/**
	 * NO: 1.4 <br>
	 * nameCN: 序列中的中心频率(Hz)，或中心波数(m –1 )，幂由x给定。 <br>
	 * unit:   <br>
	 * BUFR FXY:  <br>
	 * descriptionCN:
	 */
	private List<Double> centerFrequency;
	
	/**
	 * NO: 1.5 <br>
	 * nameCN: 增加到之前的中心频率(Hz)、或第一个中心波数(m –1 )的增量，以此获得此序列中下一个中心频率(Hz)，或第一个中心波数(m –1 )，幂由x给定。 <br>
	 * unit:   <br>
	 * BUFR FXY:  <br>
	 * descriptionCN:
	 */
	private List<Double> increment;
	
	
	/**
	 * NO: 1.6 <br>
	 * nameCN: 波浪谱数据的幂 (Exponent for spectral wave data) <br>
	 * unit:   <br>
	 * BUFR FXY:  <br>
	 * descriptionCN:
	 */
	private List<Double> expOfWaveSpectrumData;
	
	/**
	 * NO: 2.0 <br>
	 * nameCN:  <br>
	 * unit:   <br>
	 * BUFR FXY:  <br>
	 * descriptionCN: 从起伏传感器（heave sensor）推导出的最大无向波密度，频率为m 2 Hz –1，波数为m 3。
	 */
	private double maxUndirectedWaveDensity_HeaveSensor = 999999;
	
	/**
	 * NO: 2.1 <br>
	 * nameCN:  <br>
	 * unit:   <br>
	 * BUFR FXY:  <br>
	 * descriptionCN: 由起伏传感器（heave sensor）确定的最大无向波密度所在的频段号。
	 */
	private int frequencyBandOfMaxUndirectedWaveDensity_HeaveSensor = 999999;
	
	/**
	 * NO: 2.2 <br>
	 * nameCN:  <br>
	 * unit:   <br>
	 * BUFR FXY:  <br>
	 * descriptionCN: 在给定频段，由起伏传感器（heave sensor）推导出的最大无向波密度同CmCmCm 给定的最大无向波密度的比率。
	 */
	private List<Double> Ratio_HeaveSensor;
	
	/**
	 * NO: 3.0 <br>
	 * nameCN:  <br>
	 * unit:   <br>
	 * BUFR FXY:  <br>
	 * descriptionCN: 倾斜传感器推导出的最大无向波密度，频率为m 2 Hz –1，波数为m 3。
	 */
	private double maxUndirectedWaveDensity_SlopeSensor = 999999;
	
	/**
	 * NO: 3.1 <br>
	 * nameCN:  <br>
	 * unit:   <br>
	 * BUFR FXY:  <br>
	 * descriptionCN: 由倾斜传感器确定的最大无向波密度所在的频段号。
	 */
	private double frequencyBandOfMaxUndirectedWaveDensity_SlopeSensor = 999999;
	
	/**
	 * NO: 3.2 <br>
	 * nameCN:  <br>
	 * unit:   <br>
	 * BUFR FXY:  <br>
	 * descriptionCN:在给定频段，由倾斜传感器推导出的最大无向波密度同C sm C sm C sm 给定的最大无向波密度的比率
	 */
	private List<Double> Ratio_SlopeSensor;
	
	/**
	 * NO: 4.0 <br>
	 * nameCN:  <br>
	 * unit:   <br>
	 * BUFR FXY:  <br>
	 * descriptionCN: 平均方向，以4度为单位，均为指定波段波浪相对于真北的来向(Code table 0880)。
	 */
	 private List<Double> meanDir; 
	
	/**
	 * NO: 4.1 <br>
	 * nameCN:  <br>
	 * unit:   <br>
	 * BUFR FXY:  <br>
	 * descriptionCN: 主方向，以4度为单位，均为指定波段波浪相对于真北的来向(Code table 0880)。
	 */
	 private List<Double> principalDir;
	 
	/**
	 * NO: 4.2 <br>
	 * nameCN:  <br>
	 * unit:   <br>
	 * BUFR FXY:  <br>
	 * descriptionCN: 第一个导自傅里叶系数的归一化的极坐标。
	 */
	 private List<Double> firstNormalizedPolarCoordinate;
	 
	/**
	 * NO: 4.3 <br>
	 * nameCN:  <br>
	 * unit:   <br>
	 * BUFR FXY:  <br>
	 * descriptionCN:  第二个导自傅里叶系数的归一化的极坐标。
	 */
	 private List<Double> secondNormalizedPolarCoordinate; 
	 
	 /**
	 * NO: 5.0 <br>
	 * nameCN: 有向波或无向波谱数据指示符（代码表）  <br>
	 * unit:   <br>
	 * BUFR FXY:  <br>
	 * descriptionCN:
	 */
	 private int indicatorOfDirectionalOrNonDirectionalSpectralWaveData;
	 
	 /**
	 * NO: 5.1 <br>
	 * nameCN: 从第一个到第n个频率（波数，如果指定为波数）谱估计值。使用频率还是波数，由Ia 指定。 <br>
	 * unit:   <br>
	 * BUFR FXY:  <br>
	 * descriptionCN:
	 */
	 private List<Double> spectralEstimation;
	 
	 /**
	 * NO: 5.2 <br>
	 * nameCN:  <br>
	 * unit:   <br>
	 * BUFR FXY:  <br>
	 * descriptionCN:真实方向，以4度为单位，均为波浪的来向
	 */
	 private List<Double> trurDir_ComingOfWave;
	 
	 /**
	 * NO: 5.3 <br>
	 * nameCN: 全方位的主波的传播方向 <br>
	 * unit:   <br>
	 * BUFR FXY:  <br>
	 * descriptionCN: 
	 */
	 private List<Double> directionalSpreadOfDominantWave;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBuoyIdentifier() {
		return buoyIdentifier;
	}

	public void setBuoyIdentifier(String buoyIdentifier) {
		this.buoyIdentifier = buoyIdentifier;
	}

	public Date getObservationTime() {
		return observationTime;
	}

	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}

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

	public double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}

	public int getIa() {
		return Ia;
	}

	public void setIa(int ia) {
		Ia = ia;
	}

	public int getIm() {
		return Im;
	}

	public void setIm(int im) {
		Im = im;
	}

	public int getIp() {
		return Ip;
	}

	public void setIp(int ip) {
		Ip = ip;
	}

	public double getDepthOfWater() {
		return depthOfWater;
	}

	public void setDepthOfWater(double depthOfWater) {
		this.depthOfWater = depthOfWater;
	}

	public double getSignificantWaveHeight() {
		return significantWaveHeight;
	}

	public void setSignificantWaveHeight(double significantWaveHeight) {
		this.significantWaveHeight = significantWaveHeight;
	}

	public double getCycleOfPeakSpectrum() {
		return cycleOfPeakSpectrum;
	}

	public void setCycleOfPeakSpectrum(double cycleOfPeakSpectrum) {
		this.cycleOfPeakSpectrum = cycleOfPeakSpectrum;
	}

	public double getCycleOfAvgWave() {
		return cycleOfAvgWave;
	}

	public void setCycleOfAvgWave(double cycleOfAvgWave) {
		this.cycleOfAvgWave = cycleOfAvgWave;
	}

	public double getWaveHeightOfSensor() {
		return waveHeightOfSensor;
	}

	public void setWaveHeightOfSensor(double waveHeightOfSensor) {
		this.waveHeightOfSensor = waveHeightOfSensor;
	}

	public double getSpectrumPeak() {
		return spectrumPeak;
	}

	public void setSpectrumPeak(double spectrumPeak) {
		this.spectrumPeak = spectrumPeak;
	}

	public double getAvgCycleOfSensor() {
		return avgCycleOfSensor;
	}

	public void setAvgCycleOfSensor(double avgCycleOfSensor) {
		this.avgCycleOfSensor = avgCycleOfSensor;
	}

	public double getTrueDir() {
		return trueDir;
	}

	public void setTrueDir(double trueDir) {
		this.trueDir = trueDir;
	}

	public int getBandsNumber() {
		return bandsNumber;
	}

	public void setBandsNumber(int bandsNumber) {
		this.bandsNumber = bandsNumber;
	}

	public double getSampleInterval() {
		return sampleInterval;
	}

	public void setSampleInterval(double sampleInterval) {
		this.sampleInterval = sampleInterval;
	}

	public double getWaveDuration() {
		return waveDuration;
	}

	public void setWaveDuration(double waveDuration) {
		this.waveDuration = waveDuration;
	}

	public List<Double> getIncrement() {
		return increment;
	}

	public void setIncrement(List<Double> increment) {
		this.increment = increment;
	}

	public List<Double> getExpOfWaveSpectrumData() {
		return expOfWaveSpectrumData;
	}

	public void setExpOfWaveSpectrumData(List<Double> expOfWaveSpectrumData) {
		this.expOfWaveSpectrumData = expOfWaveSpectrumData;
	}

	public double getMaxUndirectedWaveDensity_HeaveSensor() {
		return maxUndirectedWaveDensity_HeaveSensor;
	}

	public void setMaxUndirectedWaveDensity_HeaveSensor(double maxUndirectedWaveDensity_HeaveSensor) {
		this.maxUndirectedWaveDensity_HeaveSensor = maxUndirectedWaveDensity_HeaveSensor;
	}

	public int getFrequencyBandOfMaxUndirectedWaveDensity_HeaveSensor() {
		return frequencyBandOfMaxUndirectedWaveDensity_HeaveSensor;
	}

	public void setFrequencyBandOfMaxUndirectedWaveDensity_HeaveSensor(
			int frequencyBandOfMaxUndirectedWaveDensity_HeaveSensor) {
		this.frequencyBandOfMaxUndirectedWaveDensity_HeaveSensor = frequencyBandOfMaxUndirectedWaveDensity_HeaveSensor;
	}

	public List<Double> getRatio_HeaveSensor() {
		return Ratio_HeaveSensor;
	}

	public void setRatio_HeaveSensor(List<Double> ratio_HeaveSensor) {
		Ratio_HeaveSensor = ratio_HeaveSensor;
	}

	public double getMaxUndirectedWaveDensity_SlopeSensor() {
		return maxUndirectedWaveDensity_SlopeSensor;
	}

	public void setMaxUndirectedWaveDensity_SlopeSensor(double maxUndirectedWaveDensity_SlopeSensor) {
		this.maxUndirectedWaveDensity_SlopeSensor = maxUndirectedWaveDensity_SlopeSensor;
	}

	public double getFrequencyBandOfMaxUndirectedWaveDensity_SlopeSensor() {
		return frequencyBandOfMaxUndirectedWaveDensity_SlopeSensor;
	}

	public void setFrequencyBandOfMaxUndirectedWaveDensity_SlopeSensor(
			double frequencyBandOfMaxUndirectedWaveDensity_SlopeSensor) {
		this.frequencyBandOfMaxUndirectedWaveDensity_SlopeSensor = frequencyBandOfMaxUndirectedWaveDensity_SlopeSensor;
	}

	public List<Double> getRatio_SlopeSensor() {
		return Ratio_SlopeSensor;
	}

	public void setRatio_SlopeSensor(List<Double> ratio_SlopeSensor) {
		Ratio_SlopeSensor = ratio_SlopeSensor;
	}

	public List<Double> getMeanDir() {
		return meanDir;
	}

	public void setMeanDir( List<Double>  meanDir) {
		this.meanDir = meanDir;
	}

	public  List<Double>  getPrincipalDir() {
		return principalDir;
	}

	public void setPrincipalDir( List<Double>  principalDir) {
		this.principalDir = principalDir;
	}

	public  List<Double>  getFirstNormalizedPolarCoordinate() {
		return firstNormalizedPolarCoordinate;
	}

	public void setFirstNormalizedPolarCoordinate( List<Double>  firstNormalizedPolarCoordinate) {
		this.firstNormalizedPolarCoordinate = firstNormalizedPolarCoordinate;
	}

	public  List<Double>  getSecondNormalizedPolarCoordinate() {
		return secondNormalizedPolarCoordinate;
	}

	public void setSecondNormalizedPolarCoordinate( List<Double>  secondNormalizedPolarCoordinate) {
		this.secondNormalizedPolarCoordinate = secondNormalizedPolarCoordinate;
	}

	public int getIndicatorOfDirectionalOrNonDirectionalSpectralWaveData() {
		return indicatorOfDirectionalOrNonDirectionalSpectralWaveData;
	}

	public void setIndicatorOfDirectionalOrNonDirectionalSpectralWaveData(
			int indicatorOfDirectionalOrNonDirectionalSpectralWaveData) {
		this.indicatorOfDirectionalOrNonDirectionalSpectralWaveData = indicatorOfDirectionalOrNonDirectionalSpectralWaveData;
	}

	public List<Double> getSpectralEstimation() {
		return spectralEstimation;
	}

	public void setSpectralEstimation(List<Double> spectralEstimation) {
		this.spectralEstimation = spectralEstimation;
	}

	public List<Double> getTrurDir_ComingOfWave() {
		return trurDir_ComingOfWave;
	}

	public void setTrurDir_ComingOfWave(List<Double> trurDir_ComingOfWave) {
		this.trurDir_ComingOfWave = trurDir_ComingOfWave;
	}

	public List<Double> getDirectionalSpreadOfDominantWave() {
		return directionalSpreadOfDominantWave;
	}

	public void setDirectionalSpreadOfDominantWave(List<Double> directionalSpreadOfDominantWave) {
		this.directionalSpreadOfDominantWave = directionalSpreadOfDominantWave;
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

	public double getMaxWaveHeight() {
		return maxWaveHeight;
	}

	public void setMaxWaveHeight(double maxWaveHeight) {
		this.maxWaveHeight = maxWaveHeight;
	}

	public List<Integer> getSubBandNum() {
		return subBandNum;
	}

	public void setSubBandNum(List<Integer> subBandNum) {
		this.subBandNum = subBandNum;
	}

	public List<Double> getCenterFrequency() {
		return centerFrequency;
	}

	public void setCenterFrequency(List<Double> centerFrequency) {
		this.centerFrequency = centerFrequency;
	}
}
