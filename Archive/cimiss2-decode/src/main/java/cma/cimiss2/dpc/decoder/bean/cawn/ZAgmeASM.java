package cma.cimiss2.dpc.decoder.bean.cawn;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * 
 * <br>
 * @Title:  ZAgmeASM.java
 * @Package cma.cimiss2.dpc.decoder.bean.asm
 * @Description:    TODO(自动土壤水分观测数据解码实体类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月12日 上午9:24:21   maxiyue   Initial creation.
 * </pre>
 * 
 * @author maxiyue
 */
public class ZAgmeASM {
	/**
	 * NO: 1.1  <br>
	 * nameCN: 区站号 <br>
	 * unit: <br>
	 * description: 区站号,5位数字或第1位为字母，第2-5位为数字
	 */
	String stationNumberChina;
	/**
	 * NO: 1.2  <br>
	 * nameCN: 纬度 <br>
	 * unit: 度分秒  <br>
	 * descriptionCN:测量地纬度
	 */
	Double latitude;
	/**
	 * NO: 1.3  <br>
	 * nameCN: 经度 <br>
	 * unit: 度分秒  <br>
	 * descriptionCN:测量地经度
	 */
	 Double longitude;
	/**
	 * NO: 1.4  <br>
	 * nameCN: 观测场拔海高度 <br>
	 * unit:<br>
	 * descriptionCN: 测量地拔海高度
	 */
	 Double heightOfSationGroundAboveMeanSeaLevel;
	 /**
	  * NO: 1.5  <br>
	  * nameCN: 测量地段标示 <br>
	  * unit: <br>
	  * descriptionCN: 测量地段标示
	  */
	 String MeasureLocationIndication;
	 /**
	  * NO: 1.6  <br>
	  * nameCN: 观测时间 <br>
	  * unit: 日期 <br>
	  * descriptionCN: 观测时间,年月日
	  */
	 Date observationTime;
	 
	 /**
	  * nameCN: 土壤层次标示，土壤体积含水量,土壤相对湿度,土壤重量含水率,土壤有效水分贮存量<br>
	  * unit:<br>
	  * descriptionCN: 土壤层次标示，土壤体积含水量,土壤相对湿度,土壤重量含水率,土壤有效水分贮存量

	  */
	List<ASMLevel> asm_soil = new ArrayList<>();
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
	public String getMeasureLocationIndication() {
		return MeasureLocationIndication;
	}
	public void setMeasureLocationIndication(String measureLocationIndication) {
		MeasureLocationIndication = measureLocationIndication;
	}
	public Date getObservationTime() {
		return observationTime;
	}
	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}
	public List<ASMLevel> getAsm_soil() {
		return asm_soil;
	}
	public void setAsm_soil(List<ASMLevel> asm_soil) {
		this.asm_soil = asm_soil;
	}
	
	public void put(ASMLevel agmeLevel){
		this.asm_soil.add(agmeLevel);
	}

}
