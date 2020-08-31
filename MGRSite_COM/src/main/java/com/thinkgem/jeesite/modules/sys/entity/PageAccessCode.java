package com.thinkgem.jeesite.modules.sys.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 访问页面的code
 * @author yb
 * @version 2014-10-30
 */
@Entity
@Table(name = "STAT_PAGE_ACCESS_CODE")
public class PageAccessCode implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer pageId;
	private String pageName;
	private String page;
	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Id
	public Integer getPageId() {
		return pageId;
	}
	public void setPageId(Integer pageId) {
		this.pageId = pageId;
	}
}


