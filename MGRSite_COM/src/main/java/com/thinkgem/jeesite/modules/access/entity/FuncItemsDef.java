package com.thinkgem.jeesite.modules.access.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SUP_FUNCITMESDEF")
public class FuncItemsDef {
	private int funcItemId;
	private String chnName;
	private String shortChnName;
	private String chnDescription;
	private String imageURL;
	private int parentId;

	@Id
	public int getFuncItemId() {
		return funcItemId;
	}

	public void setFuncItemId(int funcItemId) {
		this.funcItemId = funcItemId;
	}

	public String getChnName() {
		return chnName;
	}

	public void setChnName(String chnName) {
		this.chnName = chnName;
	}

	public String getShortChnName() {
		return shortChnName;
	}

	public void setShortChnName(String shortChnName) {
		this.shortChnName = shortChnName;
	}

	public String getChnDescription() {
		return chnDescription;
	}

	public void setChnDescription(String chnDescription) {
		this.chnDescription = chnDescription;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

}
