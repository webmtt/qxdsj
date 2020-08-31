package com.thinkgem.jeesite.modules.configure.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "SUP_THROUGHTYPEDEF")
public class ThroughTypeDef {
	
	private String throughTypeCode;
	private String chnName;
	private int isIncludeHeader;
	private String headerTheme;
	private int isIncludeFooter;
	private String footerTheme;
	private int isIncludeColumn;
	private String columnTheme;
	private int invalid;
	
	
	@Id
	public String getThroughTypeCode() {
		return throughTypeCode;
	}
	public void setThroughTypeCode(String throughTypeCode) {
		this.throughTypeCode = throughTypeCode;
	}
	public String getChnName() {
		return chnName;
	}
	public void setChnName(String chnName) {
		this.chnName = chnName;
	}
	public int getIsIncludeHeader() {
		return isIncludeHeader;
	}
	public void setIsIncludeHeader(int isIncludeHeader) {
		this.isIncludeHeader = isIncludeHeader;
	}
	public String getHeaderTheme() {
		return headerTheme;
	}
	public void setHeaderTheme(String headerTheme) {
		this.headerTheme = headerTheme;
	}
	public int getIsIncludeFooter() {
		return isIncludeFooter;
	}
	public void setIsIncludeFooter(int isIncludeFooter) {
		this.isIncludeFooter = isIncludeFooter;
	}
	public String getFooterTheme() {
		return footerTheme;
	}
	public void setFooterTheme(String footerTheme) {
		this.footerTheme = footerTheme;
	}
	public int getIsIncludeColumn() {
		return isIncludeColumn;
	}
	public void setIsIncludeColumn(int isIncludeColumn) {
		this.isIncludeColumn = isIncludeColumn;
	}
	public String getColumnTheme() {
		return columnTheme;
	}
	public void setColumnTheme(String columnTheme) {
		this.columnTheme = columnTheme;
	}
	public int getInvalid() {
		return invalid;
	}
	public void setInvalid(int invalid) {
		this.invalid = invalid;
	}
	
}
