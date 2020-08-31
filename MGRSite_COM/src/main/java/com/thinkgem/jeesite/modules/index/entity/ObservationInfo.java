package com.thinkgem.jeesite.modules.index.entity;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * 
 * 描述：气象业务
 *
 * @author Administrator
 * @version 1.0 2016年10月9日
 */
@Entity
@Table(name = "SUP_OBSERVATIONINFO")
public class ObservationInfo {
	 private String  id;//not null
	 private String  chnname;//not null
	 private String  shortchnname;// not null,
	 private String  linkurl;
	 private Integer orderno;//not null
	 private Integer invalid;//not null
	 private Date created;        
	 private String  createdby;
	 private Date updated;
	 private String  updatedby;
	 private String  imgurl;//上传图片
	 private Integer procount;
	 private Integer pastdate;	
	
	@Id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getChnname() {
		return chnname;
	}
	public void setChnname(String chnname) {
		this.chnname = chnname;
	}
	public String getShortchnname() {
		return shortchnname;
	}
	public void setShortchnname(String shortchnname) {
		this.shortchnname = shortchnname;
	}
	public String getLinkurl() {
		return linkurl;
	}
	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
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
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public Integer getProcount() {
		return procount;
	}
	public void setProcount(Integer procount) {
		this.procount = procount;
	}
	public Integer getPastdate() {
		return pastdate;
	}
	public void setPastdate(Integer pastdate) {
		this.pastdate = pastdate;
	}
	@Override
	public String toString() {
		return "ObservationInfo [id=" + id + ", chnname=" + chnname + ", shortchnname=" + shortchnname + ", linkurl="
				+ linkurl + ", orderno=" + orderno + ", invalid=" + invalid + ", created=" + created + ", createdby="
				+ createdby + ", updated=" + updated + ", updatedby=" + updatedby + ", imgurl=" + imgurl
				+ ", procount=" + procount + ", pastdate=" + pastdate + "]";
	}
}
