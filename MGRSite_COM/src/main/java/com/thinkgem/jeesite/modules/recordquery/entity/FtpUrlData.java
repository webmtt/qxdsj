package com.thinkgem.jeesite.modules.recordquery.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name = "STAT_FTPURLDATA")
public class FtpUrlData {
	private String dataCode;
	private String accessDate;
	private Integer pvNumber;
	private Integer ipNumber;
	private Long downSize;
	private Integer id;
	private String org;
	private String orgType;
	private FtpUrlConf ftpUrlConf;
	
	public String getDataCode() {
		return dataCode;
	}
	public void setDataCode(String dataCode) {
		this.dataCode = dataCode;
	}
	
	public String getAccessDate() {
		return accessDate;
	}
	public void setAccessDate(String accessDate) {
		this.accessDate = accessDate;
	}
	public Integer getPvNumber() {
		return pvNumber;
	}
	public void setPvNumber(Integer pvNumber) {
		this.pvNumber = pvNumber;
	}
	public Integer getIpNumber() {
		return ipNumber;
	}
	public void setIpNumber(Integer ipNumber) {
		this.ipNumber = ipNumber;
	}
	public Long getDownSize() {
		return downSize;
	}
	public void setDownSize(Long downSize) {
		this.downSize = downSize;
	}
	@Id 
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getOrg() {
		return org;
	}
	public void setOrg(String org) {
		this.org = org;
	}
	public String getorgType() {
		return orgType;
	}
	public void setorgType(String orgType) {
		this.orgType = orgType;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="dataCode",insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	@NotNull
	public FtpUrlConf getFtpUrlConf() {
		return ftpUrlConf;
	}
	public void setFtpUrlConf(FtpUrlConf ftpUrlConf) {
		this.ftpUrlConf = ftpUrlConf;
	}
	
	
	
}
