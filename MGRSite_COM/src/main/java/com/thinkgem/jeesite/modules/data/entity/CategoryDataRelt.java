package com.thinkgem.jeesite.modules.data.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
@Entity
@Table(name = "BMD_CATEGORYDATARELT")
public class CategoryDataRelt {
	private Integer id;
	private Integer categoryid;
	private String datacode;
	private Integer orderno;
	@Id
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCategoryid() {
		return categoryid;
	}
	public void setCategoryid(Integer categoryid) {
		this.categoryid = categoryid;
	}
	public String getDatacode() {
		return datacode;
	}
	public void setDatacode(String datacode) {
		this.datacode = datacode;
	}
	public Integer getOrderno() {
		return orderno;
	}
	public void setOrderno(Integer orderno) {
		this.orderno = orderno;
	}
	@Override
	public String toString() {
		return "CategoryDataRelt [id=" + id + ", categoryid=" + categoryid
				+ ", datacode=" + datacode + ", orderno=" + orderno + "]";
	}
	
	
	
}
