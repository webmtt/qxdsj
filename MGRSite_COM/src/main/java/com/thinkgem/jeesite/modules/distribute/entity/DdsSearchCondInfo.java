package com.thinkgem.jeesite.modules.distribute.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Entity
@Table(name = "dds_searchcondinfo")
public class DdsSearchCondInfo {
	private Integer id;
	private String dataId;
	private String searchCond;
	private Integer orderNo;
	private int invalid;
	private Date created;
	private String createdBy;
	private Date updated;
	private String updatedBy;
	private String pageDataSource;
	private String pageCond;
	private String pageEleMent;
	
	public String getDataId() {
		return dataId;
	}
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	public String getSearchCond() {
		return searchCond;
	}
	public void setSearchCond(String searchCond) {
		this.searchCond = searchCond;
	}
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	public int getInvalid() {
		return invalid;
	}
	public void setInvalid(int invalid) {
		this.invalid = invalid;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getPageDataSource() {
		return pageDataSource;
	}
	public void setPageDataSource(String pageDataSource) {
		this.pageDataSource = pageDataSource;
	}
	public String getPageCond() {
		return pageCond;
	}
	public void setPageCond(String pageCond) {
		this.pageCond = pageCond;
	}
	public String getPageEleMent() {
		return pageEleMent;
	}
	public void setPageEleMent(String pageEleMent) {
		this.pageEleMent = pageEleMent;
	}
	@Id
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
}
