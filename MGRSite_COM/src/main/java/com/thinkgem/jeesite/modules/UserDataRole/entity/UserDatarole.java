package com.thinkgem.jeesite.modules.UserDataRole.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户角色
 * @author RainingTime
 * @version 1.0 2018年7月5日
 *
 */
@Entity
@Table(name="SUP_USERDATAROLE")
public class UserDatarole implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	private String id;//主键id
	private String dataroleId;//角色id
	private String userId;//用户id
	
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
