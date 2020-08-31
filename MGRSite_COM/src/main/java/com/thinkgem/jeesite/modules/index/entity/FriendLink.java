package com.thinkgem.jeesite.modules.index.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "SUP_FRIENDLINKDEF")
public class FriendLink implements Serializable{
	private static final long serialVersionUID = 1L;
	private int linkid;
	private String linktype;
	private String chnname;
	private String engname;
	private String imageurl;
	private String linkurl;
	private int orderno;
	private int invalid;
	private Date created;
	private String createdby;
	private Date updated;
	private String updatedby;
	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getLinkid() {
		return linkid;
	}
	public void setLinkid(int linkid) {
		this.linkid = linkid;
	}
	
	public String getLinktype() {
		return linktype;
	}
	public void setLinktype(String linktype) {
		this.linktype = linktype;
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
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	public String getLinkurl() {
		return linkurl;
	}
	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}
	public int getOrderno() {
		return orderno;
	}
	public void setOrderno(int orderno) {
		this.orderno = orderno;
	}
	public int getInvalid() {
		return invalid;
	}
	public void setInvalid(int invalid) {
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
	
	
}
