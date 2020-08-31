package com.thinkgem.jeesite.modules.Users.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 服务级别表
 */
@Entity
@Table(name="SUP_SERVICELEVEL")
public class ServiceLevel {
	private int servicelevel;
	private int subordernum;
	private int filesumsize;
	@Id
	public int getServicelevel() {
		return servicelevel;
	}
	public void setServicelevel(int servicelevel) {
		this.servicelevel = servicelevel;
	}
	public int getSubordernum() {
		return subordernum;
	}
	public void setSubordernum(int subordernum) {
		this.subordernum = subordernum;
	}
	public int getFilesumsize() {
		return filesumsize;
	}
	public void setFilesumsize(int filesumsize) {
		this.filesumsize = filesumsize;
	}
	


}
