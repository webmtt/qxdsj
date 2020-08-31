package com.thinkgem.jeesite.modules.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 描述：数据检索条件集合定义表
 */
@Entity
@Table(name = "BMD_SEARCHSETDEF")
public class SearchSetDef {
	@Id
	private Integer id;//not null
	private String  searchsetcode;//not null
	private String  searchgroupcode;//not null
	private String  chnname;// not null
	private String  engname;
	private Integer isoptional;
	private Integer orderno;
	private Integer invalid;
	private Date created;
	private String  createdby;
	private Date  updated;
	private String  updatedby;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSearchsetcode() {
		return searchsetcode;
	}
	public void setSearchsetcode(String searchsetcode) {
		this.searchsetcode = searchsetcode;
	}
	public String getSearchgroupcode() {
		return searchgroupcode;
	}
	public void setSearchgroupcode(String searchgroupcode) {
		this.searchgroupcode = searchgroupcode;
	}
	public String getChnname() {
		return chnname;
	}
	public void setChnname(String chnname) {
		this.chnname = chnname;
	}
	public String getEngname() {
		return engname;
	}
	public void setEngname(String engname) {
		this.engname = engname;
	}
	public Integer getIsoptional() {
		return isoptional;
	}
	public void setIsoptional(Integer isoptional) {
		this.isoptional = isoptional;
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
		return "SearchSetDef [id=" + id + ", searchsetcode=" + searchsetcode + ", searchgroupcode=" + searchgroupcode
				+ ", chnname=" + chnname + ", engname=" + engname + ", isoptional=" + isoptional + ", orderno="
				+ orderno + ", invalid=" + invalid + ", created=" + created + ", createdby=" + createdby + ", updated="
				+ updated + ", updatedby=" + updatedby + "]";
	}
}
