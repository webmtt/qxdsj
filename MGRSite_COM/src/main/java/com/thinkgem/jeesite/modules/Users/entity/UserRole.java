package com.thinkgem.jeesite.modules.Users.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 用户证件表
 */
@Entity
@Table(name="SUP_USERROLE")
public class UserRole {
	@Id
	private String userID;
	private String roleID;
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getRoleID() {
		return roleID;
	}
	public void setRoleID(String roleID) {
		this.roleID = roleID;
	}
	
	
}
