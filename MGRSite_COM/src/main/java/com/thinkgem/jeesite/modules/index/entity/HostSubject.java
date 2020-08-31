package com.thinkgem.jeesite.modules.index.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "SUP_HOTSUBJECTDEF")
//@SequenceGenerator(name="my_seq", sequenceName="SEQ_STORE")
public class HostSubject {
	private Integer id;
	private String imageurl;
	private String linkurl;
	private Integer orderno;
	private int invalid;
	private String areaItem;
	private String title;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Id
	@Column(name="ID")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="my_seq")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name="IMAGEURL")
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	@Column(name="LINKURL")
	public String getLinkurl() {
		return linkurl;
	}
	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}
	@Column(name="ORDERNO")
	public Integer getOrderno() {
		return orderno;
	}
	public void setOrderno(Integer orderno) {
		this.orderno = orderno;
	}
	@Column(name="INVALID")
	public int getInvalid() {
		return invalid;
	}
	public void setInvalid(int invalid) {
		this.invalid = invalid;
	}
	public String getAreaItem() {
		return areaItem;
	}
	public void setAreaItem(String areaItem) {
		this.areaItem = areaItem;
	}
	
	
	
}
