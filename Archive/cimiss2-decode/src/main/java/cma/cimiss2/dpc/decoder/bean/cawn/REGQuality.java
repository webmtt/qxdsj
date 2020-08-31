package cma.cimiss2.dpc.decoder.bean.cawn;

import java.util.Date;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
	大气成分反应性气体质量控制信息实体类。解析时，各要素值均从报文直接取值、入库时各字段直接赋值。
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《反应性气体质量信息资料数据表》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月24日 上午11:20:33   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class REGQuality {
	/**
	 * 观测站区站号
	 * @field V01301
	 */
	private String stationNumberChina;
	/**
	 * 观测时间
	 * @field D_DATETIME
	 */
	private Date observationTime;
	/**
	 * 观测项目英文缩写
	 * @filed V_OBS_ITEM
	 */
	private String observeItem;
	/**
	 * 质控信息起始时间
	 * @field V_QC_BEG
	 */
	private String qcInfoStartTime;
	/**
	 * 质控信息结束时间
	 * @filed V_QC_END
	 */
	private String qcInfoEndTime;
	/**
	 * 运行状态
	 * @filed V_RUN_STATE
	 */
	private String runningState = "999999";
	/**
	 * 维护维修
	 * @filed V_MAINTEN
	 */
	private String maintenance = "999999";
	/**
	 * 周边环境
	 * @filed V_ENVIRON
	 */
	private String peripheralCondition = "999999";
	/**
	 * 天气现象
	 * @filed V_WEATHER_PHEN
	 */
	private String weatherPhone = "999999";
	/**
	 * 值班员名称
	 * @field V_WATCHER
	 */
	private String attendantName = "999999";
	/**
	 * 文字备注
	 * @field V_NOTES
	 */
	private String textNote  = "999999";
	
	public String getStationNumberChina() {
		return stationNumberChina;
	}
	public void setStationNumberChina(String stationNumberChina) {
		this.stationNumberChina = stationNumberChina;
	}
	public String getObserveItem() {
		return observeItem;
	}
	public void setObserveItem(String observeItem) {
		this.observeItem = observeItem;
	}
	public String getQcInfoStartTime() {
		return qcInfoStartTime;
	}
	public void setQcInfoStartTime(String qcInfoStartTime) {
		this.qcInfoStartTime = qcInfoStartTime;
	}
	public String getQcInfoEndTime() {
		return qcInfoEndTime;
	}
	public void setQcInfoEndTime(String qcInfoEndTime) {
		this.qcInfoEndTime = qcInfoEndTime;
	}
	public String getAttendantName() {
		return attendantName;
	}
	public void setAttendantName(String attendantName) {
		this.attendantName = attendantName;
	}
	public String getTextNote() {
		return textNote;
	}
	public void setTextNote(String textNote) {
		this.textNote = textNote;
	}
	public String getRunningState() {
		return runningState;
	}
	public void setRunningState(String runningState) {
		this.runningState = runningState;
	}
	public String getMaintenance() {
		return maintenance;
	}
	public void setMaintenance(String maintenance) {
		this.maintenance = maintenance;
	}
	public String getPeripheralCondition() {
		return peripheralCondition;
	}
	public void setPeripheralCondition(String peripheralCondition) {
		this.peripheralCondition = peripheralCondition;
	}
	public String getWeatherPhone() {
		return weatherPhone;
	}
	public void setWeatherPhone(String weatherPhone) {
		this.weatherPhone = weatherPhone;
	}
	public Date getObservationTime() {
		return observationTime;
	}
	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}

}
