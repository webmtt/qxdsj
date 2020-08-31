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
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;


/**
 * 
 * 描述：气象业务
 *
 * @author Administrator
 * @version 1.0 2016年10月9日
 */
@Entity
@Table(name = "SUP_PORTALMENUDEF")
public class PortalMenuDef {
	private PortalMenuDef parent;
	
	private Integer menuid;
	private String  chnname;
//	private Integer parentid;    
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
	
	private List<PortalMenuDef> list;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="parentid")
	public PortalMenuDef getParent() {
		return parent;
	}
	public void setParent(PortalMenuDef parent) {
		this.parent = parent;
	}
	
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
	public void setChnname(String chnname) {
		this.chnname = chnname;
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
	public Integer getLayer() {
		return layer;
	}
	public void setLayer(Integer layer) {
		this.layer = layer;
	}
	public String getMenucode() {
		return menucode;
	}
	public void setMenucode(String menucode) {
		this.menucode = menucode;
	}
	
	
	@OneToMany(fetch=FetchType.LAZY,mappedBy="parent")
	public List<PortalMenuDef> getList() {
		return list;
	}
	public void setList(List<PortalMenuDef> list) {
		this.list = list;
	}
	
	@Override
	public String toString() {
		return "PortalMenuDef [parent=" + parent + ", menuid=" + menuid + ", chnname=" + chnname + ", showtype="
				+ showtype + ", linkurl=" + linkurl + ", orderno=" + orderno + ", invalid=" + invalid + ", created="
				+ created + ", createdby=" + createdby + ", updated=" + updated + ", updatedby=" + updatedby
				+ ", layer=" + layer + ", menucode=" + menucode + ", list=" + list + "]";
	}
	@Transient
	public static void sortList(List<PortalMenuDef> list, List<PortalMenuDef> sourcelist, Integer id){
		for (int i=0; i<sourcelist.size(); i++){
			PortalMenuDef e = sourcelist.get(i);
			if (e.getParent()!=null && e.getParent().getMenuid()!=null
					&& e.getParent().getMenuid().equals(id)){
				list.add(e);
				// 判断是否还有子节点, 有则继续获取子节点
				for (int j=0; j<sourcelist.size(); j++){
					PortalMenuDef child = sourcelist.get(j);
					if (child.getParent()!=null && child.getParent().getMenuid()!=null
							&& child.getParent().getMenuid().equals(e.getMenuid())){
						sortList(list, sourcelist, e.getMenuid());
						break;
					}
				}
			}
		}
	}
	@Transient
	public static boolean isRoot(String id){
		return id != null && id.equals("0");
	}        
	
	
}
