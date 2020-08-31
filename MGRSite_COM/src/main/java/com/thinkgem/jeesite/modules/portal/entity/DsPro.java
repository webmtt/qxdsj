/*
 * @(#)DsPro.java 2016-1-21
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


/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016-1-21
 */
@Entity
@Table(name = "DS_PRO")
public class DsPro implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int id;
	private String proname;
	private Boolean protype;
	private String prosql;
	private String params;
	private String proremark;
	private int isused;
	private String createby;
	private Date created;
	private String updateby;
	private Date updated;
	private DsDb dsdb;
		
	// Constructors
	/** default constructor */
	public DsPro() {
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="PRODB") 
	@NotFound(action = NotFoundAction.IGNORE)
	@NotNull
	public DsDb getDsdb() {
		return dsdb;
	}
	public void setDsdb(DsDb dsdb) {
		this.dsdb = dsdb;
	}
	public String getProname() {
		return this.proname;
	}

	public void setProname(String proname) {
		this.proname = proname;
	}

	public Boolean getProtype() {
		return this.protype;
	}

	public void setProtype(Boolean protype) {
		this.protype = protype;
	}

	public String getProsql() {
		return this.prosql;
	}

	public void setProsql(String prosql) {
		this.prosql = prosql;
	}

	public String getParams() {
		return this.params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getProremark() {
		return this.proremark;
	}

	public void setProremark(String proremark) {
		this.proremark = proremark;
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
	@Id
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public int getIsused() {
		return isused;
	}

	public void setIsused(int isused) {
		this.isused = isused;
	}
	@Override
	public String toString() {
		return "DsPro [id=" + id + ", proname=" + proname + ", protype="
				+ protype + ", prosql=" + prosql + ", params=" + params
				+ ", proremark=" + proremark + ", isused=" + isused
				+ ", createby=" + createby + ", created=" + created
				+ ", updateby=" + updateby + ", updated=" + updated + ", dsdb="
				+ dsdb + "]";
	}
	
}
