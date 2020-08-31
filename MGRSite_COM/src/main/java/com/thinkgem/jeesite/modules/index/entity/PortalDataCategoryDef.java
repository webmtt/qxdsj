package com.thinkgem.jeesite.modules.index.entity;

import java.util.List;

import javax.persistence.Column;
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

@Entity
@Table(name = "SUP_PortalDataCategoryDef")
public class PortalDataCategoryDef {
	private Integer id;
	private String chnName;
	private String shortChnName;
	private int layer;
	private PortalDataCategoryDef parent;
	private String linkurl;
	private int showType;
	private int orderNo;
	private int invalid;
	private String imageUrl;
	private String chnDescription;
	@Id
	@Column(name="FuncItemID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name="CHNName")
	public String getChnName() {
		return chnName;
	}
	public void setChnName(String chnName) {
		this.chnName = chnName;
	}
	@Column(name="ShortCHNName")
	public String getShortChnName() {
		return shortChnName;
	}
	public void setShortChnName(String shortChnName) {
		this.shortChnName = shortChnName;
	}
	public int getLayer() {
		return layer;
	}
	public void setLayer(int layer) {
		this.layer = layer;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="parentid")
	@NotFound(action = NotFoundAction.IGNORE)
	@NotNull
	public PortalDataCategoryDef getParent() {
		return parent;
	}
	public void setParent(PortalDataCategoryDef parent) {
		this.parent = parent;
	}
	@Column(name="LinkURL")
	public String getLinkurl() {
		return linkurl;
	}
	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}
	public int getShowType() {
		return showType;
	}
	public void setShowType(int showType) {
		this.showType = showType;
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
	@Column(name="ImageUrl")
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getChnDescription() {
		return chnDescription;
	}
	public void setChnDescription(String chnDescription) {
		this.chnDescription = chnDescription;
	}
	@Transient
	public static void sortList(List<PortalDataCategoryDef> list, List<PortalDataCategoryDef> sourcelist, Integer id){
		for (int i=0; i<sourcelist.size(); i++){
			PortalDataCategoryDef e = sourcelist.get(i);
			if (e.getParent()!=null && e.getParent().getId()!=null
					&& e.getParent().getId().equals(id)){
				list.add(e);
				// 判断是否还有子节点, 有则继续获取子节点
				for (int j=0; j<sourcelist.size(); j++){
					PortalDataCategoryDef child = sourcelist.get(j);
					if (child.getParent()!=null && child.getParent().getId()!=null
							&& child.getParent().getId().equals(e.getId())){
						sortList(list, sourcelist, e.getId());
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
