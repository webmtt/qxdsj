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
@Table(name = "SUP_PORTALIMPORTDEF")
public class PortalimPortDef {
	@Id
	private Integer recommenditemid;
	private String chnname;   
	private String layerdescription;
	private String iconurl;
	private String linkurl;
	private Integer showtype;
	private Integer orderno;  
	private Integer invalid;
	private Date  created;
	private String  createdby;
	private Date updated;
	private String updatebu;
	public Integer getRecommenditemid() {
		return recommenditemid;
	}
	public void setRecommenditemid(Integer recommenditemid) {
		this.recommenditemid = recommenditemid;
	}
	public String getChnname() {
		return chnname;
	}
	public void setChnname(String chnname) {
		this.chnname = chnname;
	}
	public String getLayerdescription() {
		return layerdescription;
	}
	public void setLayerdescription(String layerdescription) {
		this.layerdescription = layerdescription;
	}
	public String getIconurl() {
		return iconurl;
	}
	public void setIconurl(String iconurl) {
		this.iconurl = iconurl;
	}
	public String getLinkurl() {
		return linkurl;
	}
	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}
	public Integer getShowtype() {
		return showtype;
	}
	public void setShowtype(Integer showtype) {
		this.showtype = showtype;
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
	public String getUpdatebu() {
		return updatebu;
	}
	public void setUpdatebu(String updatebu) {
		this.updatebu = updatebu;
	}
	@Override
	public String toString() {
		return "PortalimPortDef [recommenditemid=" + recommenditemid + ", chnname=" + chnname + ", layerdescription="
				+ layerdescription + ", iconurl=" + iconurl + ", linkurl=" + linkurl + ", showtype=" + showtype
				+ ", orderno=" + orderno + ", invalid=" + invalid + ", created=" + created + ", createdby=" + createdby
				+ ", updated=" + updated + ", updatebu=" + updatebu + "]";
	}
	
}
