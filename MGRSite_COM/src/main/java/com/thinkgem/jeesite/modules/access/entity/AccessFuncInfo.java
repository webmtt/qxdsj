package com.thinkgem.jeesite.modules.access.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "STAT_ACCESSFUNCINFO")
public class AccessFuncInfo {
	private String accessdate;
	private String accesstime;
	private String itemid;
	private Integer pvnumber;
	private Integer staytime;
	private Integer id;
	private Integer ipnum;
	private String sourceType;

	public String getAccessdate() {
		return accessdate;
	}

	public void setAccessdate(String accessdate) {
		this.accessdate = accessdate;
	}

	public String getAccesstime() {
		return accesstime;
	}

	public void setAccesstime(String accesstime) {
		this.accesstime = accesstime;
	}

	public String getItemid() {
		return itemid;
	}

	public void setItemid(String itemid) {
		this.itemid = itemid;
	}

	public Integer getPvnumber() {
		return pvnumber;
	}

	public void setPvnumber(Integer pvnumber) {
		this.pvnumber = pvnumber;
	}

	public Integer getStaytime() {
		return staytime;
	}

	public void setStaytime(Integer staytime) {
		this.staytime = staytime;
	}
	@Id
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIpnum() {
		return ipnum;
	}

	public void setIpnum(Integer ipnum) {
		this.ipnum = ipnum;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	

}
