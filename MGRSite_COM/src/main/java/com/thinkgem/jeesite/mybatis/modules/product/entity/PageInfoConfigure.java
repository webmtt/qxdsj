/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.product.entity;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

import com.thinkgem.jeesite.mybatis.common.persistence.DataEntity;

/**
 * 产品库页面配置Entity
 * @author yang.kq
 * @version 2019-11-04
 */
public class PageInfoConfigure extends DataEntity<PageInfoConfigure> {
	
	private static final long serialVersionUID = 1L;
	private String pageName;		// 页面名称
	private String pageContent;		// 页面内容
	private Integer pageType;		// 页面类型
	
	public PageInfoConfigure() {
		super();
	}

	public PageInfoConfigure(String id){
		super(id);
	}

	@Length(min=1, max=50, message="页面名称长度必须介于 1 和 50 之间")
	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	
	@Length(min=1, max=200, message="页面内容长度必须介于 1 和 200 之间")
	public String getPageContent() {
		return pageContent;
	}

	public void setPageContent(String pageContent) {
		this.pageContent = pageContent;
	}
	
	@NotNull(message="页面类型不能为空")
	public Integer getPageType() {
		return pageType;
	}

	public void setPageType(Integer pageType) {
		this.pageType = pageType;
	}
	
}