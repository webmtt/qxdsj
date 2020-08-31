package com.thinkgem.jeesite.modules.index.entity;

import java.io.Serializable;

public class Emergency implements Serializable{

	private static final long serialVersionUID = 1L;
	private String emergencyInfo;
	private String nemergencyInfo;
	private String province;
	private String provinceCode;
	private String type;
	private String typeCode;
	private String level;
	private String levelCode;
	private String dataTime;
	private String linkUrl;
	private String linkName;
	private String noticeLevel;
	public String getEmergencyInfo() {
		return emergencyInfo;
	}
	public void setEmergencyInfo(String emergencyInfo) {
		this.emergencyInfo = emergencyInfo;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getLevelCode() {
		return levelCode;
	}
	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getDataTime() {
		return dataTime;
	}
	public void setDataTime(String dataTime) {
		this.dataTime = dataTime;
	}
	public String getNemergencyInfo() {
		return nemergencyInfo;
	}
	public void setNemergencyInfo(String nemergencyInfo) {
		this.nemergencyInfo = nemergencyInfo;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public String getLinkName() {
		return linkName;
	}
	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}
	public String getNoticeLevel() {
		return noticeLevel;
	}
	public void setNoticeLevel(String noticeLevel) {
		this.noticeLevel = noticeLevel;
	}

		
}
