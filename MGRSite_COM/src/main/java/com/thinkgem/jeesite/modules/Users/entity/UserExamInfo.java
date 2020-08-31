/*
 * @(#)UserExamInfo.java 2016年3月22日
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.Users.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 描述：用户审核信息表
 *
 * @author Administrator
 * @version 1.0 2016年3月22日
 */
@Entity
@Table(name="SUP_USEREXAMINFO")
public class UserExamInfo {
	@Id
	private String id;
	private String email;
	private String code;
	private String reason;
	private String remarks;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}	
}
