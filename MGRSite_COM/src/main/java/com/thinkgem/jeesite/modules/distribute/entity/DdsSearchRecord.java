package com.thinkgem.jeesite.modules.distribute.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "dds_searchrecord")
public class DdsSearchRecord {
 
	private Integer id;//id
	private String dataId;//数据id
	private String timeCondBegin;//时间条件开始
	private String timeCondEnd;//时间条件结束
	private Date createdTime;//生成时间
	private String hostName;//主机名
	private String taskId;//作业号
	private String retriveStatus;//数据回调状态  0：默认  待回调，1：回调中，2回调成功，-1：接口无数据，-2：回调失败
	private Date retriveBeginTime;//数据回调开始时间
	private Date retriveEndTime;//数据回调结束时间
	private Integer transStatus;//转换状态（调Fortran程序）  0默认，1成功
	private Integer redoCount;//重做次数  0默认
	private String fileName;//文件名
	private String filePath;//文件路径
	private Date created;//记录创建时间
	private String createdBy;//记录创建主机名
	private Date updated;//记录更新时间
	private String updatedBy;//记录更新主机名
	@Id
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDataId() {
		return dataId;
	}
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	public String getTimeCondBegin() {
		return timeCondBegin;
	}
	public void setTimeCondBegin(String timeCondBegin) {
		this.timeCondBegin = timeCondBegin;
	}
	public String getTimeCondEnd() {
		return timeCondEnd;
	}
	public void setTimeCondEnd(String timeCondEnd) {
		this.timeCondEnd = timeCondEnd;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getRetriveStatus() {
		return retriveStatus;
	}
	public void setRetriveStatus(String retriveStatus) {
		this.retriveStatus = retriveStatus;
	}
	public Date getRetriveBeginTime() {
		return retriveBeginTime;
	}
	public void setRetriveBeginTime(Date retriveBeginTime) {
		this.retriveBeginTime = retriveBeginTime;
	}
	public Date getRetriveEndTime() {
		return retriveEndTime;
	}
	public void setRetriveEndTime(Date retriveEndTime) {
		this.retriveEndTime = retriveEndTime;
	}
	public Integer getTransStatus() {
		return transStatus;
	}
	public void setTransStatus(Integer transStatus) {
		this.transStatus = transStatus;
	}
	public Integer getRedoCount() {
		return redoCount;
	}
	public void setRedoCount(Integer redoCount) {
		this.redoCount = redoCount;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
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
	public DdsSearchRecord() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DdsSearchRecord(Integer id, String dataId, String timeCondBegin, String timeCondEnd, Date createdTime,
			String hostName, String taskId, String retriveStatus, Date retriveBeginTime, Date retriveEndTime,
			Integer transStatus, Integer redoCount, String fileName, String filePath, Date created, String createdBy,
			Date updated, String updatedBy) {
		super();
		this.id = id;
		this.dataId = dataId;
		this.timeCondBegin = timeCondBegin;
		this.timeCondEnd = timeCondEnd;
		this.createdTime = createdTime;
		this.hostName = hostName;
		this.taskId = taskId;
		this.retriveStatus = retriveStatus;
		this.retriveBeginTime = retriveBeginTime;
		this.retriveEndTime = retriveEndTime;
		this.transStatus = transStatus;
		this.redoCount = redoCount;
		this.fileName = fileName;
		this.filePath = filePath;
		this.created = created;
		this.createdBy = createdBy;
		this.updated = updated;
		this.updatedBy = updatedBy;
	}
	
	
	
	
	
	
	
}
