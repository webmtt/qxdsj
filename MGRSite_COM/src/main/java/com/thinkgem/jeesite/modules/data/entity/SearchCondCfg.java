package com.thinkgem.jeesite.modules.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 描述：数据检索条件通用配置表
 */
@Entity
@Table(name = "BMD_SEARCHCONDCFG")
public class SearchCondCfg {
	@Id
	private String searchconfigcode;//not null,
	private String datasource;//not null,
	private String chndescription;
	private String engdescription;
	private String captionfield;
	private String valuefield;
	private Integer isdataspec;
	private Integer rowitemcount;
	private String  wherecond;
	private String  orderbycond;
	private Integer invalid;
	private Date  created;
	private String  createdby;
	private Date  updated;
	private String  updatedby;
	public String getSearchconfigcode() {
		return searchconfigcode;
	}
	public void setSearchconfigcode(String searchconfigcode) {
		this.searchconfigcode = searchconfigcode;
	}
	public String getDatasource() {
		return datasource;
	}
	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}
	public String getChndescription() {
		return chndescription;
	}
	public void setChndescription(String chndescription) {
		this.chndescription = chndescription;
	}
	public String getEngdescription() {
		return engdescription;
	}
	public void setEngdescription(String engdescription) {
		this.engdescription = engdescription;
	}
	public String getCaptionfield() {
		return captionfield;
	}
	public void setCaptionfield(String captionfield) {
		this.captionfield = captionfield;
	}
	public String getValuefield() {
		return valuefield;
	}
	public void setValuefield(String valuefield) {
		this.valuefield = valuefield;
	}
	public Integer getIsdataspec() {
		return isdataspec;
	}
	public void setIsdataspec(Integer isdataspec) {
		this.isdataspec = isdataspec;
	}
	public Integer getRowitemcount() {
		return rowitemcount;
	}
	public void setRowitemcount(Integer rowitemcount) {
		this.rowitemcount = rowitemcount;
	}
	public String getWherecond() {
		return wherecond;
	}
	public void setWherecond(String wherecond) {
		this.wherecond = wherecond;
	}
	public String getOrderbycond() {
		return orderbycond;
	}
	public void setOrderbycond(String orderbycond) {
		this.orderbycond = orderbycond;
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
		return "SearchCondCfg [searchconfigcode=" + searchconfigcode + ", datasource=" + datasource
				+ ", chndescription=" + chndescription + ", engdescription=" + engdescription + ", captionfield="
				+ captionfield + ", valuefield=" + valuefield + ", isdataspec=" + isdataspec + ", rowitemcount="
				+ rowitemcount + ", wherecond=" + wherecond + ", orderbycond=" + orderbycond + ", invalid=" + invalid
				+ ", created=" + created + ", createdby=" + createdby + ", updated=" + updated + ", updatedby="
				+ updatedby + "]";
	}
	
}
