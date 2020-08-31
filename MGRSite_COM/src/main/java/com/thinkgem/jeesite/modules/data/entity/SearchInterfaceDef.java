package com.thinkgem.jeesite.modules.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 描述：数据检索接口定义表
 */
@Entity
@Table(name = "BMD_SEARCHINTERFACEDEF")
public class SearchInterfaceDef {
	@Id
	private Integer id;//not null
	private String  searchcodelist;//not null
	private String  interfacename;
	private String  interfacedesc;
	private Integer  invalid;
	private Date  created;
	private String  createdby;
	private Date  updated;
	private String  updatedby;
	private String  optioncodelist;
	private String  interfacesetcode;//not null
	private String typeName;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSearchcodelist() {
		return searchcodelist;
	}
	public void setSearchcodelist(String searchcodelist) {
		this.searchcodelist = searchcodelist;
	}
	public String getInterfacename() {
		return interfacename;
	}
	public void setInterfacename(String interfacename) {
		this.interfacename = interfacename;
	}
	public String getInterfacedesc() {
		return interfacedesc;
	}
	public void setInterfacedesc(String interfacedesc) {
		this.interfacedesc = interfacedesc;
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
	public String getOptioncodelist() {
		return optioncodelist;
	}
	public void setOptioncodelist(String optioncodelist) {
		this.optioncodelist = optioncodelist;
	}
	public String getInterfacesetcode() {
		return interfacesetcode;
	}
	public void setInterfacesetcode(String interfacesetcode) {
		this.interfacesetcode = interfacesetcode;
	}
	@Override
	public String toString() {
		return "SearchInterfaceDef [id=" + id + ", searchcodelist=" + searchcodelist + ", interfacename="
				+ interfacename + ", interfacedesc=" + interfacedesc + ", invalid=" + invalid + ", created=" + created
				+ ", createdby=" + createdby + ", updated=" + updated + ", updatedby=" + updatedby
				+ ", optioncodelist=" + optioncodelist + ", interfacesetcode=" + interfacesetcode + "]";
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}
