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
@Table(name = "STAT_ACCESSDATEDATA")
public class AccessDateData implements Serializable{
	/**
	 * the AccessDateData.java
	 */
	private static final long serialVersionUID = 1L;
	private String accessdate;
	private Integer id;
	private Integer pvNum;
	private Integer ipNum;
	private float downNum;
	private Integer proIpNum;
	private Integer cenIpNum;
	private Integer funItemId;
	private Integer proPvNum;
	private Integer cenPvNum;
    private String org;
	public Integer getPvNum() {
		return pvNum;
	}

	public void setPvNum(Integer pvNum) {
		this.pvNum = pvNum;
	}

	public Integer getIpNum() {
		return ipNum;
	}

	public void setIpNum(Integer ipNum) {
		this.ipNum = ipNum;
	}

	public float getDownNum() {
		return downNum;
	}

	public void setDownNum(float downNum) {
		this.downNum = downNum;
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

	public Integer getFunItemId() {
		return funItemId;
	}

	public void setFunItemId(Integer funItemId) {
		this.funItemId = funItemId;
	}

	public Integer getProPvNum() {
		return proPvNum;
	}

	public void setProPvNum(Integer proPvNum) {
		this.proPvNum = proPvNum;
	}

	public Integer getCenPvNum() {
		return cenPvNum;
	}

	public void setCenPvNum(Integer cenPvNum) {
		this.cenPvNum = cenPvNum;
	}

	public String getAccessdate() {
		return accessdate;
	}

	public void setAccessdate(String accessdate) {
		this.accessdate = accessdate;
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

}
