package com.thinkgem.jeesite.modules.statistics.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 描述：按日查询ip和pv的表
 *
 * @author Administrator
 * @version 1.0 2016-12-19
 */
@Entity
@Table(name = "STAT_ACCESSDATEIP")
public class AccessDateIP implements Serializable{
	/**
	 * the AccessDateIP.java
	 */
	private static final long serialVersionUID = 1L;
	private String accessdate;
	private String ipSection;
	private String accesstime;
	private String ipsection;
	private Integer ipnum;
	private String source;
	private String sourceType;

	public String getIpSection() {
		return ipSection;
	}

	public void setIpSection(String ipSection) {
		this.ipSection = ipSection;
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

	private Integer pvnum;
	private Integer id;

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

}
