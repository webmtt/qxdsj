package com.thinkgem.jeesite.modules.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 描述：要素集合要素分组定义表
 */
@Entity
@Table(name = "BMD_ELESETELEGROUPDEF")
public class EleSetEleGroupDef {
	@Id
	private Integer id;//not null
	private String  elesetcode;//not null
	private String  elegroupcode;//not null
	private String  chnname;//not null
	private Integer  orderno;
	private Integer invalid;
	private Date  created;
	private String  createdby;
	private Date  updated;
	private String  updatedby;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getElesetcode() {
		return elesetcode;
	}
	public void setElesetcode(String elesetcode) {
		this.elesetcode = elesetcode;
	}
	public String getElegroupcode() {
		return elegroupcode;
	}
	public void setElegroupcode(String elegroupcode) {
		this.elegroupcode = elegroupcode;
	}
	public String getChnname() {
		return chnname;
	}
	public void setChnname(String chnname) {
		this.chnname = chnname;
	}
	public Integer getOrderno() {
		return orderno;
	}
	public void setOrderno(Integer orderno) {
		this.orderno = orderno;
	}
	public Integer getInvalid() {
		return invalid;
	}
	public void setInvalid(Integer invalid) {
		this.invalid = invalid;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getCreatedby() {
		return createdby;
	}
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	public String getUpdatedby() {
		return updatedby;
	}
	public void setUpdatedby(String updatedby) {
		this.updatedby = updatedby;
	}
	@Override
	public String toString() {
		return "EleSetEleGroupDef [id=" + id + ", elesetcode=" + elesetcode + ", elegroupcode=" + elegroupcode
				+ ", chnname=" + chnname + ", orderno=" + orderno + ", invalid=" + invalid + ", created=" + created
				+ ", createdby=" + createdby + ", updated=" + updated + ", updatedby=" + updatedby + "]";
	}
	
}
