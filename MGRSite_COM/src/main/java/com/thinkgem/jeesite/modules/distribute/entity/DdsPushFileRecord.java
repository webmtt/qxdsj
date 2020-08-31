package com.thinkgem.jeesite.modules.distribute.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Entity
@Table(name = "dds_pushfilerecord")
public class DdsPushFileRecord {
	private Integer id;//id
	private String ftpId;//ftp地址ID
	private String fileName;//文件名
	private String filePath;//文件路径
	private Date createdTime;//生成时间
	private String taskId;//作业号
	private String pushStatus;//推送状态  0：待推送，1：推送中，2推送成功，-2：推送失败
	private Date pushBeginTime;//推送开始时间
	private Date pushEndTime;//推送结束时间
	private Integer pushNum;//推送次数
	private Date created;//记录创建时间
	private String createdBy;//记录创建主机名
	private Date updated;//记录更新时间
	private String updatedBy;//记录更新主机名
	private String targetPath;//目标路径
	private String hostName;//主机名
	
	@Id
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFtpId() {
		return ftpId;
	}
	public void setFtpId(String ftpId) {
		this.ftpId = ftpId;
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
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getPushStatus() {
		return pushStatus;
	}
	public void setPushStatus(String pushStatus) {
		this.pushStatus = pushStatus;
	}
	public Date getPushBeginTime() {
		return pushBeginTime;
	}
	public void setPushBeginTime(Date pushBeginTime) {
		this.pushBeginTime = pushBeginTime;
	}
	public Date getPushEndTime() {
		return pushEndTime;
	}
	public void setPushEndTime(Date pushEndTime) {
		this.pushEndTime = pushEndTime;
	}
	public Integer getPushNum() {
		return pushNum;
	}
	public void setPushNum(Integer pushNum) {
		this.pushNum = pushNum;
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
	public String getTargetPath() {
		return targetPath;
	}
	public void setTargetPath(String targetPath) {
		this.targetPath = targetPath;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public DdsPushFileRecord() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DdsPushFileRecord(Integer id, String ftpId, String fileName, String filePath, Date createdTime,
			String taskId, String pushStatus, Date pushBeginTime, Date pushEndTime, Integer pushNum, Date created,
			String createdBy, Date updated, String updatedBy, String targetPath, String hostName) {
		super();
		this.id = id;
		this.ftpId = ftpId;
		this.fileName = fileName;
		this.filePath = filePath;
		this.createdTime = createdTime;
		this.taskId = taskId;
		this.pushStatus = pushStatus;
		this.pushBeginTime = pushBeginTime;
		this.pushEndTime = pushEndTime;
		this.pushNum = pushNum;
		this.created = created;
		this.createdBy = createdBy;
		this.updated = updated;
		this.updatedBy = updatedBy;
		this.targetPath = targetPath;
		this.hostName = hostName;
	}
	
	

}
