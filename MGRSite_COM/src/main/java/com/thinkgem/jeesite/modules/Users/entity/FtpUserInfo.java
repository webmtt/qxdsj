package com.thinkgem.jeesite.modules.Users.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016年11月29日
 */
@Entity
@Table(name="users")
public class FtpUserInfo {
	private String User;
	private String Password;
	private String dir;
	private Integer uid;
	private Integer gid;
	@Id
	public String getUser() {
		return User;
	}
	public void setUser(String user) {
		User = user;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public Integer getGid() {
		return gid;
	}
	public void setGid(Integer gid) {
		this.gid = gid;
	}
}
