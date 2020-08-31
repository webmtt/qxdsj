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
@Table(name = "SUP_PORTALMENUDEF")
public class PortalMenu {
	private Integer menuid;
	private String  chnname;
	private Integer parentid;    
	private Integer showtype;
	private String  linkurl;
	private Integer orderno;
	private Integer invalid;
	private Timestamp created;        
	private String  createdby;
	private Timestamp updated;
	private String  updatedby;
	private Integer layer;
	private String menucode;
	@Id
	public Integer getMenuid() {
		return menuid;
	}
	public void setMenuid(Integer menuid) {
		this.menuid = menuid;
	}
	public String getChnname() {
		return chnname;
	}
	public Integer getParentid() {
		return parentid;
	}
	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}
	public void setChnname(String chnname) {
		this.chnname = chnname;
	}
	
	public String getMenucode() {
		return menucode;
	}
	public void setMenucode(String menucode) {
		this.menucode = menucode;
	}
	
	public Integer getLayer() {
		return layer;
	}
	public void setLayer(Integer layer) {
		this.layer = layer;
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
	@Override
	public String toString() {
		return "PortalMenu [menuid=" + menuid + ", chnname=" + chnname + ", parentid=" + parentid + ", showtype="
				+ showtype + ", linkurl=" + linkurl + ", orderno=" + orderno + ", invalid=" + invalid + ", created="
				+ created + ", createdby=" + createdby + ", updated=" + updated + ", updatedby=" + updatedby
				+ ", layer=" + layer + ", menucode=" + menucode + "]";
	}
	
	
	
}
