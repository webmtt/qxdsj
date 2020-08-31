/*
 * @(#)UserFeedBack.java 2016年5月30日
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.user.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 描述：用户反馈信息
 *
 * @author Administrator
 * @version 1.0 2016年5月30日
 */
@Entity
@Table(name="SUP_USERFEEDBACK")
public class UserFeedBack {
	@Id
	private String id;
	private String userID;
	private String fDItemID;
	private String fDTime;
	private String fDTitle;
	private String fDContext;
	private int fDStatus;
	private int unitId;
	private int invalid;
	private String respTime;
 	private String respUserID;
	private String respContext;
	private Timestamp created;
	private String createdBy;
	private Timestamp updated;
	private String updatedBy;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getfDItemID() {
		return fDItemID;
	}
	public void setfDItemID(String fDItemID) {
		this.fDItemID = fDItemID;
	}
	public String getfDTime() {
		return fDTime;
	}
	public void setfDTime(String fDTime) {
		this.fDTime = fDTime;
	}
	public String getfDTitle() {
		return fDTitle;
	}
	public void setfDTitle(String fDTitle) {
		this.fDTitle = fDTitle;
	}
	public String getfDContext() {
		return fDContext;
	}
	public void setfDContext(String fDContext) {
		this.fDContext = fDContext;
	}
	public int getfDStatus() {
		return fDStatus;
	}
	public void setfDStatus(int fDStatus) {
		this.fDStatus = fDStatus;
	}
	public String getRespTime() {
		return respTime;
	}
	public void setRespTime(String respTime) {
		this.respTime = respTime;
	}
	public String getRespUserID() {
		return respUserID;
	}
	public void setRespUserID(String respUserID) {
		this.respUserID = respUserID;
	}
	public String getRespContext() {
		return respContext;
	}
	public void setRespContext(String respContext) {
		this.respContext = respContext;
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
	public int getUnitId() {
		return unitId;
	}
	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}
	public int getInvalid() {
		return invalid;
	}
	public void setInvalid(int invalid) {
		this.invalid = invalid;
	}	
}
