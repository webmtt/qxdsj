package com.thinkgem.jeesite.modules.index.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SUP_PORTALIMAGEPRODUCTDEF")
public class PortalImageProductDef {
	private Integer id;
	private String title;
	private int IsStatic;
	private String productCode;
	private String dataCode;
	private String dataSourse;
	private int orderNo;
	private int invalid;
	private String imageUrl;
	private String linkUrl;
	private String showType;
	@Id
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getIsStatic() {
		return IsStatic;
	}
	public void setIsStatic(int isStatic) {
		IsStatic = isStatic;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getDataCode() {
		return dataCode;
	}
	public void setDataCode(String dataCode) {
		this.dataCode = dataCode;
	}
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	public int getInvalid() {
		return invalid;
	}
	public void setInvalid(int invalid) {
		this.invalid = invalid;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public String getDataSourse() {
		return dataSourse;
	}
	public void setDataSourse(String dataSourse) {
		this.dataSourse = dataSourse;
	}
	public String getShowType() {
		return showType;
	}
	public void setShowType(String showType) {
		this.showType = showType;
	}
}
