package cma.cimiss2.dpc.decoder.bean.asm;
/**
 * 
 * <br>
 * @Title:  ASMLevel.java
 * @Package cma.cimiss2.dpc.decoder.bean.asm
 * @Description:    TODO(自动土壤水分观测数据解码实体类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月12日 上午9:27:31   maxiyue   Initial creation.
 * </pre>
 * 
 * @author maxiyue
 */
public class ASMLevel {
	/**
	  * nameCN: 土壤层次标示<br>
	  * unit:<br>
	  * descriptionCN: 土壤层次标示，固定输入“L010，L020，L030，.....”
	  */
	 Double soilLevelLabeling;
	 /**
	  * nameCN:土壤体积含水量<br>
	  * unit:<br>
	  * descriptionCN:土壤体积含水量
	  */
	 Double soilVolumeMoistureContent;
	 /**
	  * nameCN:土壤相对湿度<br>
	  * unit:<br>
	  * descriptionCN:土壤相对湿度
	  */
	 Double  soilRelativeHumidity;
	 /**
	  * nameCN:土壤重量含水率量<br>
	  * unit:<br>
	  * descriptionCN:土壤重量含水量
	  */
	 Double soilWeightMoistureContent;
	 /**
	  * nameCN:土壤有效水分贮存量<br>
	  * unit:<br>
	  * descriptionCN:土壤有效水分贮存量
	  */
	 Integer  soilAavailableWaterContent;
	
	public Double getSoilLevelLabeling() {
		return soilLevelLabeling;
	}
	public void setSoilLevelLabeling(Double soilLevelLabeling) {
		this.soilLevelLabeling = soilLevelLabeling;
	}
	public Double getSoilVolumeMoistureContent() {
		return soilVolumeMoistureContent;
	}
	public void setSoilVolumeMoistureContent(Double soilVolumeMoistureContent) {
		this.soilVolumeMoistureContent = soilVolumeMoistureContent;
	}
	public Double getSoilRelativeHumidity() {
		return soilRelativeHumidity;
	}
	public void setSoilRelativeHumidity(Double soilRelativeHumidity) {
		this.soilRelativeHumidity = soilRelativeHumidity;
	}
	public Double getSoilWeightMoistureContent() {
		return soilWeightMoistureContent;
	}
	public void setSoilWeightMoistureContent(Double soilWeightMoistureContent) {
		this.soilWeightMoistureContent = soilWeightMoistureContent;
	}
	public Integer getSoilAavailableWaterContent() {
		return soilAavailableWaterContent;
	}
	public void setSoilAavailableWaterContent(Integer soilAavailableWaterContent) {
		this.soilAavailableWaterContent = soilAavailableWaterContent;
	}
	
	

}
