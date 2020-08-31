package com.piesat.kettlescheduler.model;

import java.io.Serializable;
import java.util.Date;

/**
 * k_job
 * @author 
 */
public class KJob implements Serializable {
    /**
     * 作业ID
     */
    private Integer jobId;

    /**
     * 任务分类
     */
    private Integer categoryId;

    /**
     * 数据分类
     */
    private Integer dataIds;

    /**
     * 作业名称
     */
    private String jobName;

    /**
     * 1:数据库资源库；2:上传的文件
     */
    private Integer jobType;

    /**
     * 作业的资源库ID
     */
    private Integer jobRepositoryId;

    /**
     * 定时策略（外键ID）
     */
    private Integer jobQuartz;

    /**
     * 作业执行记录（外键ID）
     */
    private Integer jobRecord;

    /**
     * 日志级别(basic，detail，error，debug，minimal，rowlevel）
     */
    private String jobLogLevel;

    /**
     * 状态（1：正在运行；2：已停止）
     */
    private Integer jobStatus;

    /**
     * 添加时间
     */
    private Date addTime;

    /**
     * 添加者
     */
    private Integer addUser;

    /**
     * 编辑时间
     */
    private Date editTime;

    /**
     * 编辑者
     */
    private Integer editUser;

    /**
     * 是否删除（1：存在；0：删除）
     */
    private Integer delFlag;

    /**
     * 任务描述
     */
    private String jobDescription;

    /**
     * 作业保存路径（可以是资源库中的路径也可以是服务器中保存作业文件的路径）
     */
    private String jobPath;

    private static final long serialVersionUID = 1L;

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getDataIds() {
        return dataIds;
    }

    public void setDataIds(Integer dataIds) {
        this.dataIds = dataIds;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Integer getJobType() {
        return jobType;
    }

    public void setJobType(Integer jobType) {
        this.jobType = jobType;
    }

    public Integer getJobRepositoryId() {
        return jobRepositoryId;
    }

    public void setJobRepositoryId(Integer jobRepositoryId) {
        this.jobRepositoryId = jobRepositoryId;
    }

    public Integer getJobQuartz() {
        return jobQuartz;
    }

    public void setJobQuartz(Integer jobQuartz) {
        this.jobQuartz = jobQuartz;
    }

    public Integer getJobRecord() {
        return jobRecord;
    }

    public void setJobRecord(Integer jobRecord) {
        this.jobRecord = jobRecord;
    }

    public String getJobLogLevel() {
        return jobLogLevel;
    }

    public void setJobLogLevel(String jobLogLevel) {
        this.jobLogLevel = jobLogLevel;
    }

    public Integer getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(Integer jobStatus) {
        this.jobStatus = jobStatus;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Integer getAddUser() {
        return addUser;
    }

    public void setAddUser(Integer addUser) {
        this.addUser = addUser;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public Integer getEditUser() {
        return editUser;
    }

    public void setEditUser(Integer editUser) {
        this.editUser = editUser;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobPath() {
        return jobPath;
    }

    public void setJobPath(String jobPath) {
        this.jobPath = jobPath;
    }

    @Override
    public String toString() {
        return "KJob{" +
                "jobId=" + jobId +
                ", categoryId=" + categoryId +
                ", dataIds=" + dataIds +
                ", jobName='" + jobName + '\'' +
                ", jobType=" + jobType +
                ", jobRepositoryId=" + jobRepositoryId +
                ", jobQuartz=" + jobQuartz +
                ", jobRecord=" + jobRecord +
                ", jobLogLevel='" + jobLogLevel + '\'' +
                ", jobStatus=" + jobStatus +
                ", addTime=" + addTime +
                ", addUser=" + addUser +
                ", editTime=" + editTime +
                ", editUser=" + editUser +
                ", delFlag=" + delFlag +
                ", jobDescription='" + jobDescription + '\'' +
                ", jobPath='" + jobPath + '\'' +
                '}';
    }
}