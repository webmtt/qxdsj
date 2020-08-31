package com.nmpiesat.idata.data.entity;

import java.io.Serializable;


public class CategoryDataRelt implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer categoryid;
	private String datacode;
	private Integer orderno;

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
