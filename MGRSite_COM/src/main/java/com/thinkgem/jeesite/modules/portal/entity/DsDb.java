/*
 * @(#)DsDb.java 2016-1-21
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.portal.entity;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 描述：
 * 
 * @author Administrator
 * @version 1.0 2016-1-21
 */
@Entity
@Table(name = "DS_DB")
public class DsDb implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String dbname;
	private String dbtype;
	private String dblink;
	private String username;
	private String password;
	private String dbremark;
	private int isused;
	private String createby;
	private Date created;
	private String updateby;
	private Date updated;
	private Set<DsPro> dsPros = new HashSet<DsPro>(0);
	
	// Constructors
	/** default constructor */
	public DsDb() {
	}
	/** full constructor */
	// Property accessors
	@OneToMany
	@JoinColumn(name="PRODB") 
	@NotFound(action = NotFoundAction.IGNORE)
	@NotNull
	public Set<DsPro> getDsPros() {
		return this.dsPros;
	}

	public void setDsPros(Set<DsPro> dsPros) {
		this.dsPros = dsPros;
	}
	@Id
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDbname() {
		return this.dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public String getDbtype() {
		return this.dbtype;
	}

	public void setDbtype(String dbtype) {
		this.dbtype = dbtype;
	}

	public String getDblink() {
		return this.dblink;
	}

	public void setDblink(String dblink) {
		this.dblink = dblink;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDbremark() {
		return this.dbremark;
	}

	public void setDbremark(String dbremark) {
		this.dbremark = dbremark;
	}

	public String getCreateby() {
		return this.createby;
	}

	public void setCreateby(String createby) {
		this.createby = createby;
	}

	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getUpdateby() {
		return this.updateby;
	}

	public void setUpdateby(String updateby) {
		this.updateby = updateby;
	}

	public Date getUpdated() {
		return this.updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public int getIsused() {
		return isused;
	}

	public void setIsused(int isused) {
		this.isused = isused;
	}
	@Override
	public String toString() {
		return "DsDb [id=" + id + ", dbname=" + dbname + ", dbtype=" + dbtype
				+ ", dblink=" + dblink + ", username=" + username
				+ ", password=" + password + ", dbremark=" + dbremark
				+ ", isused=" + isused + ", createby=" + createby
				+ ", created=" + created + ", updateby=" + updateby
				+ ", updated=" + updated + ", dsPros=" + "" + "]";
	}
	
}
