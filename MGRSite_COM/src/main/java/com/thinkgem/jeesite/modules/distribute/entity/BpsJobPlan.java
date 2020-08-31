package com.thinkgem.jeesite.modules.distribute.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Entity
@Table(name = "bps_jobplan")
public class BpsJobPlan {
	private String jobName;
	private String description;
	private String jobGroup;
	private String className;
	private String jobParameter;
	private String triGgerPolicy;
	private int triGgerPriority;
	private String dayExcluded;
	private int retryCount;
	private int treatasDeadTimeOut;
	private String treatasDeadTimeOutUnit;
	@Id
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getJobGroup() {
		return jobGroup;
	}
	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getJobParameter() {
		return jobParameter;
	}
	public void setJobParameter(String jobParameter) {
		this.jobParameter = jobParameter;
	}
	public String getTriGgerPolicy() {
		return triGgerPolicy;
	}
	public void setTriGgerPolicy(String triGgerPolicy) {
		this.triGgerPolicy = triGgerPolicy;
	}
	public int getTriGgerPriority() {
		return triGgerPriority;
	}
	public void setTriGgerPriority(int triGgerPriority) {
		this.triGgerPriority = triGgerPriority;
	}
	public String getDayExcluded() {
		return dayExcluded;
	}
	public void setDayExcluded(String dayExcluded) {
		this.dayExcluded = dayExcluded;
	}
	public int getRetryCount() {
		return retryCount;
	}
	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}
	public int getTreatasDeadTimeOut() {
		return treatasDeadTimeOut;
	}
	public void setTreatasDeadTimeOut(int treatasDeadTimeOut) {
		this.treatasDeadTimeOut = treatasDeadTimeOut;
	}
	public String getTreatasDeadTimeOutUnit() {
		return treatasDeadTimeOutUnit;
	}
	public void setTreatasDeadTimeOutUnit(String treatasDeadTimeOutUnit) {
		this.treatasDeadTimeOutUnit = treatasDeadTimeOutUnit;
	}
	public int getInvalid() {
		return invalid;
	}
	public void setInvalid(int invalid) {
		this.invalid = invalid;
	}
	public String getQuartzCluster() {
		return quartzCluster;
	}
	public void setQuartzCluster(String quartzCluster) {
		this.quartzCluster = quartzCluster;
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
	private int invalid;
	private String quartzCluster;
	private Date created;
	private String createdBy;
	private Date updated;
	private String updatedBy;

}
