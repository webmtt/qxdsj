package com.thinkgem.jeesite.modules.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 描述：数据检索条件项表
 */
@Entity
@Table(name = "BMD_SEARCHCONDITEMS")
public class SearchCondItems {
	@Id
	private Integer id;//not null
	private String  itemtype;//not null
	private String  itemcaption;//not null
	private String  itemvalue;//not null
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
	public String getItemtype() {
		return itemtype;
	}
	public void setItemtype(String itemtype) {
		this.itemtype = itemtype;
	}
	public String getItemcaption() {
		return itemcaption;
	}
	public void setItemcaption(String itemcaption) {
		this.itemcaption = itemcaption;
	}
	public String getItemvalue() {
		return itemvalue;
	}
	public void setItemvalue(String itemvalue) {
		this.itemvalue = itemvalue;
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
		return "SearchCondItems [id=" + id + ", itemtype=" + itemtype + ", itemcaption=" + itemcaption + ", itemvalue="
				+ itemvalue + ", orderno=" + orderno + ", invalid=" + invalid + ", created=" + created + ", createdby="
				+ createdby + ", updated=" + updated + ", updatedby=" + updatedby + "]";
	}
	
}
