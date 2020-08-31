package com.thinkgem.jeesite.modules.access.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "STAT_ACCESSFUNCDATEINFO")
public class AccessFuncDateInfo {
	private String accessdate;
	private String itemid;
	private int pvnumber;
	private int staytime;
	private int id;
	private int ipnum;
	private String sourceType;

	public String getAccessdate() {
		return accessdate;
	}

	public void setAccessdate(String accessdate) {
		this.accessdate = accessdate;
	}

	public String getItemid() {
		return itemid;
	}

	public void setItemid(String itemid) {
		this.itemid = itemid;
	}

	public int getPvnumber() {
		return pvnumber;
	}

	public void setPvnumber(int pvnumber) {
		this.pvnumber = pvnumber;
	}

	public int getStaytime() {
		return staytime;
	}

	public void setStaytime(int staytime) {
		this.staytime = staytime;
	}

	@Id
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIpnum() {
		return ipnum;
	}

	public void setIpnum(int ipnum) {
		this.ipnum = ipnum;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	
}

