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
public class PortalitemCategoryDef {
	private PortalitemCategoryDef parent;
	
	private Integer funcitemid;
	private String  chnname;
	private String  shortchnname;
	private Integer layer;
//	private Integer parentid;    
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
	
	private List<PortalitemCategoryDef> list;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="parentid")
	public PortalitemCategoryDef getParent() {
		return parent;
	}
	public void setParent(PortalitemCategoryDef parent) {
		this.parent = parent;
	}
	
	
	
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
	
//	public Integer getParentid() {
//		return parentid;
//	}
//	public void setParentid(Integer parentid) {
//		this.parentid = parentid;
//	}
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
	
	
	@OneToMany(fetch=FetchType.LAZY,mappedBy="parent")
	public List<PortalitemCategoryDef> getList() {
		return list;
	}
	public void setList(List<PortalitemCategoryDef> list) {
		this.list = list;
	}
	@Override
	public String toString() {
		return "PortalitemCategoryDef [parent=" + parent + ", funcitemid=" + funcitemid + ", chnname=" + chnname
				+ ", shortchnname=" + shortchnname + ", layer=" + layer + ", showtype=" + showtype + ", linkurl="
				+ linkurl + ", orderno=" + orderno + ", invalid=" + invalid + ", created=" + created + ", createdby="
				+ createdby + ", updated=" + updated + ", updatedby=" + updatedby + ", chndescription="
				+ chndescription + ", isopen=" + isopen + ", list=" + list + "]";
	}
	
	@Transient
	public static void sortList(List<PortalitemCategoryDef> list, List<PortalitemCategoryDef> sourcelist, Integer id){
		for (int i=0; i<sourcelist.size(); i++){
			PortalitemCategoryDef e = sourcelist.get(i);
			if (e.getParent()!=null && e.getParent().getFuncitemid()!=null
					&& e.getParent().getFuncitemid().equals(id)){
				list.add(e);
				// 判断是否还有子节点, 有则继续获取子节点
				for (int j=0; j<sourcelist.size(); j++){
					PortalitemCategoryDef child = sourcelist.get(j);
					if (child.getParent()!=null && child.getParent().getFuncitemid()!=null
							&& child.getParent().getFuncitemid().equals(e.getFuncitemid())){
						sortList(list, sourcelist, e.getFuncitemid());
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
