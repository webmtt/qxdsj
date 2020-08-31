package com.thinkgem.jeesite.modules.configure.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name = "SUP_FUNCITMESDEF")
//@SequenceGenerator(name="my_seq", sequenceName="SEQ_STORE")
public class FunItmes {
	private Integer id;
	private Integer invalid;
	private String name;
	private String shortChnName;
	private String description;
	private String picture;
	private Integer layer;
	private FunItmes parent;
	private Integer type;
	private String keywords;
	private Date publishTime;
	private String link;
	private Integer orderno;
	private String throughUrl;
	private ThroughTypeDef throughTypeDef;
	private Integer throughPageHeight;
	private Integer throughPageWidth;
	private String itemcode;
	private String showType;
	private Integer isnosearch;
	private String sumDescription;
	private String areaItem;
	private String fontColor;
	private String iconURL;
	private String showStyle;
	public FunItmes(){
		super();
		this.orderno = 30;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="parentid")
	@NotFound(action = NotFoundAction.IGNORE)
	@NotNull
	public FunItmes getParent() {
		return parent;
	}

	public void setParent(FunItmes parent) {
		this.parent = parent;
	}
	@Column(name = "ITEMCODE")
	public String getItemcode() {
		return itemcode;
	}

	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}

	@Column(name = "SUMDESCRIPTION")
	public String getSumDescription() {
		return sumDescription;
	}

	public void setSumDescription(String sumDescription) {
		this.sumDescription = sumDescription;
	}


	@Column(name = "CHNNAME")
	public String getName() {
		return name;
	}

	@Column(name = "CHNDESCRIPTION")
	public String getDescription() {
		return description;
	}

	@Column(name = "IMAGEURL")
	public String getPicture() {
		return picture;
	}

	@Column(name = "LAYER")
	public Integer getLayer() {
		return layer;
	}


	@Transient
	public Integer getType() {
		type = 1;
		return type;
	}

	@Column(name = "KEYWORD")
	public String getKeywords() {
		return keywords;
	}

	@Column(name = "CREATED")
	public Date getPublishTime() {
		return publishTime;
	}

	@Column(name = "LINKURL")
	public String getLink() {
		return link;
	}

	@Column(name = "ORDERNO")
	public Integer getOrderno() {
		return orderno;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public void setLayer(Integer layer) {
		this.layer = layer;
	}


	public void setType(Integer type) {
		this.type = type;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Column(name = "SHORTCHNNAME")
	public String getShortChnName() {
		return shortChnName;
	}

	public void setShortChnName(String shortChnName) {
		this.shortChnName = shortChnName;
	}

	public void setOrderno(Integer orderno) {
		this.orderno = orderno;
	}

	public String getThroughUrl() {
		return throughUrl;
	}

	public void setThroughUrl(String throughUrl) {
		this.throughUrl = throughUrl;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "THROUGHTYPECODE")
	@NotFound(action = NotFoundAction.IGNORE)
	public ThroughTypeDef getThroughTypeDef() {
		return throughTypeDef;
	}

	public void setThroughTypeDef(ThroughTypeDef throughTypeDef) {
		this.throughTypeDef = throughTypeDef;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public Integer getThroughPageHeight() {
		return throughPageHeight;
	}

	public void setThroughPageHeight(Integer throughPageHeight) {
		this.throughPageHeight = throughPageHeight;
	}

	public Integer getThroughPageWidth() {
		return throughPageWidth;
	}

	public void setThroughPageWidth(Integer throughPageWidth) {
		this.throughPageWidth = throughPageWidth;
	}

	public Integer getInvalid() {
		return invalid;
	}

	public void setInvalid(Integer invalid) {
		this.invalid = invalid;
	}

	public Integer getIsnosearch() {
		return isnosearch;
	}

	public void setIsnosearch(Integer isnosearch) {
		this.isnosearch = isnosearch;
	}

	public String getAreaItem() {
		return areaItem;
	}

	public void setAreaItem(String areaItem) {
		this.areaItem = areaItem;
	}

	public String getFontColor() {
		return fontColor;
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}

	public String getIconURL() {
		return iconURL;
	}

	public void setIconURL(String iconURL) {
		this.iconURL = iconURL;
	}
	@Transient
	public static void sortList(List<FunItmes> list, List<FunItmes> sourcelist, Integer parentId){
		for (int i=0; i<sourcelist.size(); i++){
			FunItmes e = sourcelist.get(i);
			if (e.getParent()!=null&&e.getParent().getId()!=null &&e.getParent().getId()==parentId){
				list.add(e);
				// 判断是否还有子节点, 有则继续获取子节点
				for (int j=0; j<sourcelist.size(); j++){
					FunItmes child = sourcelist.get(j);
					if (child.getParent()!=null&&e.getParent().getId()!=null && child.getParent().getId()==e.getId()){
						sortList(list, sourcelist,e.getId());
						break;
					}
				}
			}
		}
	}
	@Transient
	public static void deleteList(List<FunItmes> list, List<FunItmes> sourcelist, String id,String areaItem){
		for (int i=0; i<sourcelist.size(); i++){
			FunItmes e = sourcelist.get(i);
			if (e.getId()!=null&&String.valueOf(e.getId()).equals(id)){
				e.setAreaItem(e.getAreaItem().replace(","+areaItem+",", ""));
				list.add(e);
				// 判断是否还有子节点, 有则继续获取子节点
				for (int j=0; j<sourcelist.size(); j++){
					FunItmes child = sourcelist.get(j);
					if (child.getParent()!=null&&e.getParent().getId()!=null && child.getParent().getId()==e.getId()){
						deleteList(list, sourcelist,String.valueOf(e.getId()),areaItem);
						break;
					}
				}
			}
		}
	}
	public String getShowStyle() {
		return showStyle;
	}

	public void setShowStyle(String showStyle) {
		this.showStyle = showStyle;
	}
//	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="my_seq")
	@Id
	@Column(name = "FUNCITEMID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
	
	


}
