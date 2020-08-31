package com.thinkgem.jeesite.modules.UserDataRole.entity;

import java.io.Serializable;

/**
 * 
 * 联合实体类
 * @author RainingTime
 * @version 1.0 2018年7月5日
 *
 */
public class EntityUserDatarole implements Serializable{

	private static final long serialVersionUID = 1L;
	private String id;//
	private String dataroleId;//角色id
	private String dataroleName;//角色名
	private String userName;//用户账号
	private String chName;//用户名
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDataroleId() {
		return dataroleId;
	}
	public void setDataroleId(String dataroleId) {
		this.dataroleId = dataroleId;
	}
	public String getDataroleName() {
		return dataroleName;
	}
	public void setDataroleName(String dataroleName) {
		this.dataroleName = dataroleName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getChName() {
		return chName;
	}
	public void setChName(String chName) {
		this.chName = chName;
	}
	
}
