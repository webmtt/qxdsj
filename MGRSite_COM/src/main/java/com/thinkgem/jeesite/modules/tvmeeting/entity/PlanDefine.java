/*
 * @(#)PlanDefine.java 2016年1月27日
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.tvmeeting.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016年1月27日
 */
@Entity
@Table(name="TVMEETING_PLANDEF")
public class PlanDefine implements Serializable{
	
	/**
	 * the PlanDefine.java
	 */
	private static final long serialVersionUID = 1L;
	private String MeetingId;
	private String MeetingDate;
	private String BeginTime;
	private String EndTime;
	private String Content;
	private int MeetLevel;
	private int MeetType;
	private int MeetTheme;
	private int MeetArea;
	private String MeetRange;
	private int IsDouble;
	private String OrganizationUni;
	private String Contacts;
	private String ContactWay;
	private String MeetTitle;
	private int Invalid;
	public int getMeetLevel() {
		return MeetLevel;
	}
	public void setMeetLevel(int meetLevel) {
		MeetLevel = meetLevel;
	}
	public int getMeetType() {
		return MeetType;
	}
	public void setMeetType(int meetType) {
		MeetType = meetType;
	}
	public int getMeetTheme() {
		return MeetTheme;
	}
	public void setMeetTheme(int meetTheme) {
		MeetTheme = meetTheme;
	}
	public int getMeetArea() {
		return MeetArea;
	}
	public void setMeetArea(int meetArea) {
		MeetArea = meetArea;
	}
	public int getIsDouble() {
		return IsDouble;
	}
	public void setIsDouble(int isDouble) {
		IsDouble = isDouble;
	}
	public String getOrganizationUni() {
		return OrganizationUni;
	}
	public void setOrganizationUni(String organizationUni) {
		OrganizationUni = organizationUni;
	}
	public String getContacts() {
		return Contacts;
	}
	public void setContacts(String contacts) {
		Contacts = contacts;
	}
	public String getContactWay() {
		return ContactWay;
	}
	public void setContactWay(String contactWay) {
		ContactWay = contactWay;
	}
	public int getInvalid() {
		return Invalid;
	}
	public void setInvalid(int invalid) {
		Invalid = invalid;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	private Date Created;
	private String CreatedBy;
	private Date Updated;
	private String UpdatedBy;
	@Id
	@Column(name = "MEETINGID", unique = true, nullable = false)
	public String getMeetingId() {
		return MeetingId;
	}
	public void setMeetingId(String meetingId) {
		MeetingId = meetingId;
	}
	@Column(name="MEETINGDATE")
	public String getMeetingDate() {
		return MeetingDate;
	}
	public void setMeetingDate(String meetingDate) {
		MeetingDate = meetingDate;
	}
	@Column(name="BEGINTIME")
	public String getBeginTime() {
		return BeginTime;
	}
	public void setBeginTime(String beginTime) {
		BeginTime = beginTime;
	}
	@Column(name="ENDTIME")
	public String getEndTime() {
		return EndTime;
	}
	public void setEndTime(String endTime) {
		EndTime = endTime;
	}
	@Column(name="CONTENT")
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	

	@Column(name="CREATEDBY")
	public String getCreatedBy() {
		return CreatedBy;
	}
	public void setCreatedBy(String createdBy) {
		CreatedBy = createdBy;
	}
	

	@Column(name="UPDATEDBY")
	public String getUpdatedBy() {
		return UpdatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		UpdatedBy = updatedBy;
	}
	@Column(name="UPDATED")
	public Date getUpdated() {
		return Updated;
	}
	public void setUpdated(Date updated) {
		Updated = updated;
	}
	@Column(name="CREATED")
	public Date getCreated() {
		return Created;
	}
	public void setCreated(Date created) {
		Created = created;
	}
	public String getMeetTitle() {
		return MeetTitle;
	}
	public void setMeetTitle(String meetTitle) {
		MeetTitle = meetTitle;
	}
	public String getMeetRange() {
		return MeetRange;
	}
	public void setMeetRange(String meetRange) {
		MeetRange = meetRange;
	}
	
}
