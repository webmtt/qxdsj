package com.thinkgem.jeesite.modules.index.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 描述：栏目Entity
 *
 * @author Administrator
 * @version 2018年11月20日
 */
@Entity
@Table(name = "SUP_FUNCITMESDEF")
public class FuncItmesDef {
	private Integer funcItemID;
	private String CHNName;
	private String shortCHNName;
	private String CHNDescription;
	private String imageUrl;
	private Integer layer;
	private Integer parentID;
	private Integer showType;
	private String linkUrl;
	private String keyWord;
	private Integer orderNo;
	private Integer invalid;
	private Timestamp created;
	private String createdBy;
	private Timestamp updated;
	private String updatedBy;
	private String itemCode;
	private String iconUrl;
	private String bigIconUrl;
	private String speccolImageUrl;
	private String throughUrl;
	private String throughTypeCode;
	private Integer throughPageHeight;
	private Integer throughPageWidth;
	private Integer isNosearch;
	private String sumDescription;
	private String obs_itemCode;
	private String showStyle;
	private Integer obs_orderNo;
	private Integer isSitemap;
	
	private String parentIDs;
	
	@Id
	public Integer getFuncItemID() {
		return funcItemID;
	}
	public void setFuncItemID(Integer funcItemID) {
		this.funcItemID = funcItemID;
	}
	public String getCHNName() {
		return CHNName;
	}
	public void setCHNName(String cHNName) {
		CHNName = cHNName;
	}
	public String getShortCHNName() {
		return shortCHNName;
	}
	public void setShortCHNName(String shortCHNName) {
		this.shortCHNName = shortCHNName;
	}
	public String getCHNDescription() {
		return CHNDescription;
	}
	public void setCHNDescription(String cHNDescription) {
		CHNDescription = cHNDescription;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Integer getLayer() {
		return layer;
	}
	public void setLayer(Integer layer) {
		this.layer = layer;
	}
	public Integer getParentID() {
		return parentID;
	}
	public void setParentID(Integer parentID) {
		this.parentID = parentID;
	}
	public Integer getShowType() {
		return showType;
	}
	public void setShowType(Integer showType) {
		this.showType = showType;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
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
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Timestamp getUpdated() {
		return updated;
	}
	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getBigIconUrl() {
		return bigIconUrl;
	}
	public void setBigIconUrl(String bigIconUrl) {
		this.bigIconUrl = bigIconUrl;
	}
	public String getSpeccolImageUrl() {
		return speccolImageUrl;
	}
	public void setSpeccolImageUrl(String speccolImageUrl) {
		this.speccolImageUrl = speccolImageUrl;
	}
	public String getThroughUrl() {
		return throughUrl;
	}
	public void setThroughUrl(String throughUrl) {
		this.throughUrl = throughUrl;
	}
	public String getThroughTypeCode() {
		return throughTypeCode;
	}
	public void setThroughTypeCode(String throughTypeCode) {
		this.throughTypeCode = throughTypeCode;
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
	public Integer getIsNosearch() {
		return isNosearch;
	}
	public void setIsNosearch(Integer isNosearch) {
		this.isNosearch = isNosearch;
	}
	public String getSumDescription() {
		return sumDescription;
	}
	public void setSumDescription(String sumDescription) {
		this.sumDescription = sumDescription;
	}
	public String getObs_itemCode() {
		return obs_itemCode;
	}
	public void setObs_itemCode(String obs_itemCode) {
		this.obs_itemCode = obs_itemCode;
	}
	public String getShowStyle() {
		return showStyle;
	}
	public void setShowStyle(String showStyle) {
		this.showStyle = showStyle;
	}
	public Integer getObs_orderNo() {
		return obs_orderNo;
	}
	public void setObs_orderNo(Integer obs_orderNo) {
		this.obs_orderNo = obs_orderNo;
	}
	public Integer getIsSitemap() {
		return isSitemap;
	}
	public void setIsSitemap(Integer isSitemap) {
		this.isSitemap = isSitemap;
	}
	
	@Transient
	public String getParentIDs() {
		return parentIDs;
	}
	public void setParentIDs(String parentIDs) {
		this.parentIDs = parentIDs;
	}
	
	@Override
	public String toString() {
		return "FuncItmesDef [funcItemID=" + funcItemID + ", CHNName=" + CHNName + ", shortCHNName=" + shortCHNName
				+ ", CHNDescription=" + CHNDescription + ", imageUrl=" + imageUrl + ", layer=" + layer + ", parentID="
				+ parentID + ", showType=" + showType + ", linkUrl=" + linkUrl + ", keyWord=" + keyWord + ", orderNo="
				+ orderNo + ", invalid=" + invalid + ", created=" + created + ", createdBy=" + createdBy + ", updated="
				+ updated + ", updatedBy=" + updatedBy + ", itemCode=" + itemCode + ", iconUrl=" + iconUrl
				+ ", bigIconUrl=" + bigIconUrl + ", speccolImageUrl=" + speccolImageUrl + ", throughUrl=" + throughUrl
				+ ", throughTypeCode=" + throughTypeCode + ", throughPageHeight=" + throughPageHeight
				+ ", throughPageWidth=" + throughPageWidth + ", isNosearch=" + isNosearch + ", sumDescription="
				+ sumDescription + ", obs_itemCode=" + obs_itemCode + ", showStyle=" + showStyle + ", obs_orderNo="
				+ obs_orderNo + ", isSitemap=" + isSitemap + "]";
	}
	
}
