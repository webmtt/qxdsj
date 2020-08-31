package com.thinkgem.jeesite.modules.index.entity;

import java.sql.Timestamp;
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
@Table(name = "SUP_PORTALITEMCATEGORYDEF")
public class PortalitemCategory {
	
	private Integer funcitemid;
	private String  chnname;
	private String  shortchnname;
	private Integer layer;
	private Integer parentid;    
	private Integer showtype;
	private String  linkurl;
	private Integer orderno;
	private Integer invalid;
	private Timestamp created;        
	private String  createdby;
	private Timestamp updated;
	private String  updatedby;
	private String  chndescription;
	private Integer  isopen;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getFuncitemid() {
		return funcitemid;
	}
	public void setFuncitemid(Integer funcitemid) {
		this.funcitemid = funcitemid;
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
	public Integer getLayer() {
		return layer;
	}
	public void setLayer(Integer layer) {
		this.layer = layer;
	}
	
	public Integer getParentid() {
		return parentid;
	}
	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}
	public Integer getShowtype() {
		return showtype;
	}
	public void setShowtype(Integer showtype) {
		this.showtype = showtype;
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
	public Timestamp getCreated() {
		return created;
	}
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	public String getCreatedby() {
		return createdby;
	}
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	public Timestamp getUpdated() {
		return updated;
	}
	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}
	public String getUpdatedby() {
		return updatedby;
	}
	public void setUpdatedby(String updatedby) {
		this.updatedby = updatedby;
	}
	public String getChndescription() {
		return chndescription;
	}
	public void setChndescription(String chndescription) {
		this.chndescription = chndescription;
	}
	public Integer getIsopen() {
		return isopen;
	}
	public void setIsopen(Integer isopen) {
		this.isopen = isopen;
	}
	@Override
	public String toString() {
		return "PortalitemCategory [funcitemid=" + funcitemid + ", chnname=" + chnname + ", shortchnname="
				+ shortchnname + ", layer=" + layer + ", parentid=" + parentid + ", showtype=" + showtype
				+ ", linkurl=" + linkurl + ", orderno=" + orderno + ", invalid=" + invalid + ", created=" + created
				+ ", createdby=" + createdby + ", updated=" + updated + ", updatedby=" + updatedby
				+ ", chndescription=" + chndescription + ", isopen=" + isopen + "]";
	}
	
	
}
