package com.thinkgem.jeesite.modules.access.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "STAT_ACCESSFUNCONFIG")
public class AccessFunConfig {
	private String id;
	private String funcItemId;
	private String funcItemName;
	private String parentFuncitemId;
	private String url;
	
	private String recordTime;//用户访问记录时间
	private String parentFuncItemName;//父栏目
	
	@Id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFuncItemId() {
		return funcItemId;
	}
	public void setFuncItemId(String funcItemId) {
		this.funcItemId = funcItemId;
	}
	public String getFuncItemName() {
		return funcItemName;
	}
	public void setFuncItemName(String funcItemName) {
		this.funcItemName = funcItemName;
	}
	public String getParentFuncitemId() {
		return parentFuncitemId;
	}
	public void setParentFuncitemId(String parentFuncitemId) {
		this.parentFuncitemId = parentFuncitemId;
	}
	
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Transient
	public String getRecordTime() {
		return recordTime;
	}
	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
	}
	@Transient
	public String getParentFuncItemName() {
		return parentFuncItemName;
	}
	public void setParentFuncItemName(String parentFuncItemName) {
		this.parentFuncItemName = parentFuncItemName;
	}
	
}
