package com.thinkgem.jeesite.modules.recordquery.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016年8月25日
 */
@Entity
@Table(name = "SUP_SERVICEHOSTIP")
public class HostIp {
	private String ip;
	@Id
	private int id;
	private String name;
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
