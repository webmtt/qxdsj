/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.industry.entity;


import com.thinkgem.jeesite.mybatis.common.persistence.DataEntity;

/**
 * 行业应用服务Entity
 * @author songyan
 * @version 2020-02-26
 */
public class IndustryApplication extends DataEntity<IndustryApplication> {
	
	private static final long serialVersionUID = 1L;
	private String imageurl;		// 图片URL
	private String title;		// 标题
	private String entitle;		// 英文标题
	private String content;		// 简介
	private String example;//样例
	private String creatTime;//创建时间

	public String getExample() {
		return example;
	}

	public void setExample(String example) {
		this.example = example;
	}

	public IndustryApplication() {
		super();
	}

	public String getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}

	public IndustryApplication(String id){
		super(id);
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getEntitle() {
		return entitle;
	}

	public void setEntitle(String entitle) {
		this.entitle = entitle;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}