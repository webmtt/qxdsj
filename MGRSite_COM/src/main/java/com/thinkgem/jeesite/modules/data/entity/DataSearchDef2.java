package com.thinkgem.jeesite.modules.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 描述：数据资料检索定义表
 */
@Entity
@Table(name = "BMD_DATASEARCHDEF")
public class DataSearchDef2 {
	@Id
	private String datacode;
	private String dsaccesscode;
	private String searchsetcode;
	private String elesetcode;
	private String searchpageattachcond;
	private String searchpageorderby;
	private Integer  invalid;
	private Date  created;
	private String createdby;
	private Date  updated;
	private String updatedby;
	private String interfacesetcode;
	private String defaultstaions;
	private String parentdatacode;
	private String datachnname;
	private String searchname;
	private String udatacode;
	public String getDatacode() {
		return datacode;
	}
	public void setDatacode(String datacode) {
		this.datacode = datacode;
	}
	public String getDsaccesscode() {
		return dsaccesscode;
	}
	public void setDsaccesscode(String dsaccesscode) {
		this.dsaccesscode = dsaccesscode;
	}
	public String getSearchsetcode() {
		return searchsetcode;
	}
	public void setSearchsetcode(String searchsetcode) {
		this.searchsetcode = searchsetcode;
	}
	public String getElesetcode() {
		return elesetcode;
	}
	public void setElesetcode(String elesetcode) {
		this.elesetcode = elesetcode;
	}
	public String getSearchpageattachcond() {
		return searchpageattachcond;
	}
	public void setSearchpageattachcond(String searchpageattachcond) {
		this.searchpageattachcond = searchpageattachcond;
	}
	public String getSearchpageorderby() {
		return searchpageorderby;
	}
	public void setSearchpageorderby(String searchpageorderby) {
		this.searchpageorderby = searchpageorderby;
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
	public String getInterfacesetcode() {
		return interfacesetcode;
	}
	public void setInterfacesetcode(String interfacesetcode) {
		this.interfacesetcode = interfacesetcode;
	}
	public String getDefaultstaions() {
		return defaultstaions;
	}
	public void setDefaultstaions(String defaultstaions) {
		this.defaultstaions = defaultstaions;
	}
	public String getParentdatacode() {
		return parentdatacode;
	}
	public void setParentdatacode(String parentdatacode) {
		this.parentdatacode = parentdatacode;
	}
	public String getDatachnname() {
		return datachnname;
	}
	public void setDatachnname(String datachnname) {
		this.datachnname = datachnname;
	}
	public String getSearchname() {
		return searchname;
	}
	public void setSearchname(String searchname) {
		this.searchname = searchname;
	}
	public String getUdatacode() {
		return udatacode;
	}
	public void setUdatacode(String udatacode) {
		this.udatacode = udatacode;
	}
	@Override
	public String toString() {
		return "DataSearchDef [datacode=" + datacode + ", dsaccesscode=" + dsaccesscode + ", searchsetcode="
				+ searchsetcode + ", elesetcode=" + elesetcode + ", searchpageattachcond=" + searchpageattachcond
				+ ", searchpageorderby=" + searchpageorderby + ", invalid=" + invalid + ", created=" + created
				+ ", createdby=" + createdby + ", updated=" + updated + ", updatedby=" + updatedby
				+ ", interfacesetcode=" + interfacesetcode + ", defaultstaions=" + defaultstaions + ", parentdatacode="
				+ parentdatacode + ", datachnname=" + datachnname + ", searchname=" + searchname + ", udatacode="
				+ udatacode + "]";
	}
	
}
