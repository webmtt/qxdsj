package com.thinkgem.jeesite.modules.access.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "STAT_ACCESSIP")
public class AccessIP {
	private String accessdate;
	private String accesstime;
	private String ipsection;
	private Integer ipnum;
	private Integer pvnum;
	private Integer id;
	private String source;
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

	public String getIpsection() {
		return ipsection;
	}

	public void setIpsection(String ipsection) {
		this.ipsection = ipsection;
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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public Integer getPvnum() {
		return pvnum;
	}

	public void setPvnum(Integer pvnum) {
		this.pvnum = pvnum;
	}
	
	

}
