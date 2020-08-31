/*
 * @(#)DataName.java 2017-10-16
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.data.entity;

import java.io.Serializable;

/**
 * 描述：
 *
 * @author cds
 * @version 1.0 2017-10-16
 */
public class DataName implements Serializable {

	/**
	 * the DataName.java
	 */
	private static final long serialVersionUID = 1L;
	private String data_type;
	private String data_type2;
	
	private String data_name;
	//julian
	//元数据信息描述
	private String data_dataCode;
	private String data_updateFreq;
	private String data_produceTime;
	private String data_spatialResolution;
	private String data_referSystem;
	private String data_keyWords;
	private String data_publishTime;
	//地理范围描述
	private String data_areaScope;
	private String data_northLat;
	private String data_southLat;
	private String data_astLon;
	private String data_westLon;
	//时间覆盖范围
	private String data_dataBeginTime;
	private String data_dataEndTime;
	private String data_obsFreq;
	//联系方法
	private String data_producer;
	private String data_productionUnit;
	private String data_contactInfo;
	
	@Override
	public String toString() {
		return "DataName [data_type=" + data_type + ", data_type2=" + data_type2 + ", data_name=" + data_name
				+ ", data_dataCode=" + data_dataCode + ", data_updateFreq=" + data_updateFreq + ", data_produceTime="
				+ data_produceTime + ", data_spatialResolution=" + data_spatialResolution + ", data_referSystem="
				+ data_referSystem + ", data_keyWords=" + data_keyWords + ", data_publishTime=" + data_publishTime
				+ ", data_areaScope=" + data_areaScope + ", data_northLat=" + data_northLat + ", data_southLat="
				+ data_southLat + ", data_astLon=" + data_astLon + ", data_westLon=" + data_westLon
				+ ", data_dataBeginTime=" + data_dataBeginTime + ", data_dataEndTime=" + data_dataEndTime
				+ ", data_obsFreq=" + data_obsFreq + ", data_producer=" + data_producer + ", data_productionUnit="
				+ data_productionUnit + ", data_contactInfo=" + data_contactInfo + "]";
	}
	
	public DataName(String data_type, String data_type2, String data_name, String data_dataCode, String data_updateFreq,
			String data_produceTime, String data_spatialResolution, String data_referSystem, String data_keyWords,
			String data_publishTime, String data_areaScope, String data_northLat, String data_southLat,
			String data_astLon, String data_westLon, String data_dataBeginTime, String data_dataEndTime,
			String data_obsFreq, String data_producer, String data_productionUnit, String data_contactInfo) {
		super();
		this.data_type = data_type;
		this.data_type2 = data_type2;
		this.data_name = data_name;
		this.data_dataCode = data_dataCode;
		this.data_updateFreq = data_updateFreq;
		this.data_produceTime = data_produceTime;
		this.data_spatialResolution = data_spatialResolution;
		this.data_referSystem = data_referSystem;
		this.data_keyWords = data_keyWords;
		this.data_publishTime = data_publishTime;
		this.data_areaScope = data_areaScope;
		this.data_northLat = data_northLat;
		this.data_southLat = data_southLat;
		this.data_astLon = data_astLon;
		this.data_westLon = data_westLon;
		this.data_dataBeginTime = data_dataBeginTime;
		this.data_dataEndTime = data_dataEndTime;
		this.data_obsFreq = data_obsFreq;
		this.data_producer = data_producer;
		this.data_productionUnit = data_productionUnit;
		this.data_contactInfo = data_contactInfo;
	}


	public String getData_dataBeginTime() {
		return data_dataBeginTime;
	}
	public void setData_dataBeginTime(String data_dataBeginTime) {
		this.data_dataBeginTime = data_dataBeginTime;
	}
	public String getData_dataEndTime() {
		return data_dataEndTime;
	}
	public void setData_dataEndTime(String data_dataEndTime) {
		this.data_dataEndTime = data_dataEndTime;
	}
	public String getData_obsFreq() {
		return data_obsFreq;
	}
	public void setData_obsFreq(String data_obsFreq) {
		this.data_obsFreq = data_obsFreq;
	}
	public String getData_producer() {
		return data_producer;
	}
	public void setData_producer(String data_producer) {
		this.data_producer = data_producer;
	}
	public String getData_productionUnit() {
		return data_productionUnit;
	}
	public void setData_productionUnit(String data_productionUnit) {
		this.data_productionUnit = data_productionUnit;
	}
	public String getData_contactInfo() {
		return data_contactInfo;
	}
	public void setData_contactInfo(String data_contactInfo) {
		this.data_contactInfo = data_contactInfo;
	}
	public String getData_areaScope() {
		return data_areaScope;
	}
	public void setData_areaScope(String data_areaScope) {
		this.data_areaScope = data_areaScope;
	}
	public String getData_northLat() {
		return data_northLat;
	}
	public void setData_northLat(String data_northLat) {
		this.data_northLat = data_northLat;
	}
	public String getData_southLat() {
		return data_southLat;
	}
	public void setData_southLat(String data_southLat) {
		this.data_southLat = data_southLat;
	}
	public String getData_astLon() {
		return data_astLon;
	}
	public void setData_astLon(String data_astLon) {
		this.data_astLon = data_astLon;
	}
	public String getData_westLon() {
		return data_westLon;
	}
	public void setData_westLon(String data_westLon) {
		this.data_westLon = data_westLon;
	}
	public String getData_dataCode() {
		return data_dataCode;
	}
	public void setData_dataCode(String data_dataCode) {
		this.data_dataCode = data_dataCode;
	}
	public String getData_updateFreq() {
		return data_updateFreq;
	}
	public void setData_updateFreq(String data_updateFreq) {
		this.data_updateFreq = data_updateFreq;
	}
	public String getData_produceTime() {
		return data_produceTime;
	}
	public void setData_produceTime(String data_produceTime) {
		this.data_produceTime = data_produceTime;
	}
	public String getData_spatialResolution() {
		return data_spatialResolution;
	}
	public void setData_spatialResolution(String data_spatialResolution) {
		this.data_spatialResolution = data_spatialResolution;
	}
	public String getData_referSystem() {
		return data_referSystem;
	}
	public void setData_referSystem(String data_referSystem) {
		this.data_referSystem = data_referSystem;
	}
	public String getData_keyWords() {
		return data_keyWords;
	}
	public void setData_keyWords(String data_keyWords) {
		this.data_keyWords = data_keyWords;
	}
	public String getData_publishTime() {
		return data_publishTime;
	}
	public void setData_publishTime(String data_publishTime) {
		this.data_publishTime = data_publishTime;
	}
	public DataName() {
		super();
		// TODO Auto-generated constructor stub
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public DataName(String data_type, String data_type2, String data_name) {
		super();
		this.data_type = data_type;
		this.data_type2 = data_type2;
		this.data_name = data_name;
	}
	public String getData_type() {
		return data_type;
	}
	public void setData_type(String data_type) {
		this.data_type = data_type;
	}
	public String getData_type2() {
		return data_type2;
	}
	public void setData_type2(String data_type2) {
		this.data_type2 = data_type2;
	}
	public String getData_name() {
		return data_name;
	}
	public void setData_name(String data_name) {
		this.data_name = data_name;
	}
	
	
}
