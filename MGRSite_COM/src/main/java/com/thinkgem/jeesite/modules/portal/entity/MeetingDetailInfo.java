package com.thinkgem.jeesite.modules.portal.entity;

import org.hibernate.annotations.Formula;

import javax.persistence.*;

@Entity
@Table(name="MEETING_DETAIL_INFO")
public class MeetingDetailInfo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID",unique=true, nullable=false, precision=10, scale=0)
	private int id;
	
	@Column(name="PID")
	private int pid;
	
//	@Column(name="NAME")
//	private String name;
	
	@Column(name="MEETING_DATE")
	private String date;
	
	@Column(name="STARTTIME")
	private String startTime;
	
	@Column(name="ENDTIME")
	private String endTime;
	
	@Column(name="CONTENT")
	private String content;
	
	@Column(name="SPOKESMAN")
	private String spokesman;
	
	@Column(name="UNIT")
	private String unit;
	
	@Column(name="DOWNLOAD_ADDRESS")
	private String download_address;
	
	private String fileName;
	
//	private String name;
	@Formula("(select m.name from MEETING_INFO m where m.id=pid)")
	private String name;
	
	@Transient
	private String time;
	private int invalid;
	
	
	public int getInvalid() {
		return invalid;
	}

	public void setInvalid(int invalid) {
		this.invalid = invalid;
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSpokesman() {
		return spokesman;
	}

	public void setSpokesman(String spokesman) {
		this.spokesman = spokesman;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getDownload_address() {
		return download_address;
	}

	public void setDownload_address(String download_address) {
		this.download_address = download_address;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	

}
