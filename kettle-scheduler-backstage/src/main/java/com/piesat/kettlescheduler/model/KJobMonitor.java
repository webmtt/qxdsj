package com.piesat.kettlescheduler.model;

import java.io.Serializable;
import java.util.Date;

/**
 * k_job_monitor
 * @author
 */
public class KJobMonitor implements Comparable<KJobMonitor>{
    /**
     * 监控作业ID
     */
    private Integer monitorId;

    /**
     * 监控的作业ID
     */
    private Integer monitorJob;

    /**
     * 成功次数
     */
    private Integer monitorSuccess;

    /**
     * 失败次数
     */
    private Integer monitorFail;

    /**
     * 添加人
     */
    private Integer addUser;

    /**
     * 监控状态（是否启动，1:启动；2:停止）
     */
    private Integer monitorStatus;

    private Date lastExecuteTime;

    private Date nextExecuteTime;

    /**
     * 运行状态（起始时间-结束时间,起始时间-结束时间……）
     */
    private String runStatus;

    private String jobName;

    private String categoryName;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    private static final long serialVersionUID = 1L;

    public Integer getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(Integer monitorId) {
        this.monitorId = monitorId;
    }

    public Integer getMonitorJob() {
        return monitorJob;
    }

    public void setMonitorJob(Integer monitorJob) {
        this.monitorJob = monitorJob;
    }

    public Integer getMonitorSuccess() {
        return monitorSuccess;
    }

    public void setMonitorSuccess(Integer monitorSuccess) {
        this.monitorSuccess = monitorSuccess;
    }

    public Integer getMonitorFail() {
        return monitorFail;
    }

    public void setMonitorFail(Integer monitorFail) {
        this.monitorFail = monitorFail;
    }

    public Integer getAddUser() {
        return addUser;
    }

    public void setAddUser(Integer addUser) {
        this.addUser = addUser;
    }

    public Integer getMonitorStatus() {
        return monitorStatus;
    }

    public void setMonitorStatus(Integer monitorStatus) {
        this.monitorStatus = monitorStatus;
    }

    public Date getLastExecuteTime() {
        return lastExecuteTime;
    }

    public void setLastExecuteTime(Date lastExecuteTime) {
        this.lastExecuteTime = lastExecuteTime;
    }

    public Date getNextExecuteTime() {
        return nextExecuteTime;
    }

    public void setNextExecuteTime(Date nextExecuteTime) {
        this.nextExecuteTime = nextExecuteTime;
    }

    public String getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(String runStatus) {
        this.runStatus = runStatus;
    }

    @Override
    public int compareTo(KJobMonitor kJobMonitor) {
        return this.getMonitorSuccess() - kJobMonitor.getMonitorSuccess();//按照成功次数排序;
    }

    @Override
    public String toString() {
        return "KJobMonitor{" +
                "monitorId=" + monitorId +
                ", monitorJob=" + monitorJob +
                ", monitorSuccess=" + monitorSuccess +
                ", monitorFail=" + monitorFail +
                ", addUser=" + addUser +
                ", monitorStatus=" + monitorStatus +
                ", lastExecuteTime=" + lastExecuteTime +
                ", nextExecuteTime=" + nextExecuteTime +
                ", runStatus='" + runStatus + '\'' +
                ", jobName='" + jobName + '\'' +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}