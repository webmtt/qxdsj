package com.thinkgem.jeesite.modules.access.entity;

import javax.persistence.*;

@Entity
@Table(name = "STAT_ACCESSDATEDATA")
public class AccessData {
	private String accessdate;
	private Integer ipnum;
	private Integer pvnum;
	private Integer id;
	private Integer proIpNum;
	private Integer cenIpNum;
	private Integer propvNum;
	private Integer cenPvNum;
	private String funitemId;
	private String org;

	public String getAccessdate() {
		return accessdate;
	}

	public void setAccessdate(String accessdate) {
		this.accessdate = accessdate;
	}

	public Integer getIpnum() {
		return ipnum;
	}

	public void setIpnum(Integer ipnum) {
		this.ipnum = ipnum;
	}

	@Id
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProIpNum() {
		return proIpNum;
	}

	public void setProIpNum(Integer proIpNum) {
		this.proIpNum = proIpNum;
	}

	public Integer getCenIpNum() {
		return cenIpNum;
	}

	public void setCenIpNum(Integer cenIpNum) {
		this.cenIpNum = cenIpNum;
	}

	public Integer getPvnum() {
		return pvnum;
	}

	public void setPvnum(Integer pvnum) {
		this.pvnum = pvnum;
	}

	public Integer getPropvNum() {
		return propvNum;
	}

	public void setPropvNum(Integer propvNum) {
		this.propvNum = propvNum;
	}

	public Integer getCenPvNum() {
		return cenPvNum;
	}

	public void setCenPvNum(Integer cenPvNum) {
		this.cenPvNum = cenPvNum;
	}

	public String getFunitemId() {
		return funitemId;
	}

	public void setFunitemId(String funitemId) {
		this.funitemId = funitemId;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}
	
	
	

}
