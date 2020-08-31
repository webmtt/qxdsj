/*
 * @(#)EmergencyNotice.java 2016-8-16
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.index.entity;

import java.io.Serializable;

/**
 * 描述：
 *
 * @author sunhui
 * @version 1.0 2018-10-23
 */
public class EmergencyNotice implements Serializable{
	private static final long serialVersionUID = 1L;
	public String id;
	public String title;
	public String content;
	public String publishDep;
	public String publishDate;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPublishDep() {
		return publishDep;
	}
	public void setPublishDep(String publishDep) {
		this.publishDep = publishDep;
	}
	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
	
}
