package com.piesat.kettlescheduler.model;

import java.io.Serializable;
import java.util.Date;

/**
 * k_trans_monitor
 * @author 
 */
public class KTransMonitor implements Comparable<KTransMonitor> {
    /**
     * 监控转换ID
     */
    private Integer monitorId;

    /**
     * 监控的转换的ID
     */
    private Integer monitorTrans;

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

    private String categoryName;
    private String transName;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getTransName() {
        return transName;
    }

    public void setTransName(String transName) {
        this.transName = transName;
    }

    private static final long serialVersionUID = 1L;

    public Integer getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(Integer monitorId) {
        this.monitorId = monitorId;
    }

    public Integer getMonitorTrans() {
        return monitorTrans;
    }

    public void setMonitorTrans(Integer monitorTrans) {
        this.monitorTrans = monitorTrans;
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
    public int compareTo(KTransMonitor o) {
        return this.getMonitorSuccess() - o.getMonitorSuccess();
    }


    @Override
    public String toString() {
        return "KTransMonitor{" +
                "monitorId=" + monitorId +
                ", monitorTrans=" + monitorTrans +
                ", monitorSuccess=" + monitorSuccess +
                ", monitorFail=" + monitorFail +
                ", addUser=" + addUser +
                ", monitorStatus=" + monitorStatus +
                ", lastExecuteTime=" + lastExecuteTime +
                ", nextExecuteTime=" + nextExecuteTime +
                ", runStatus='" + runStatus + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", transName='" + transName + '\'' +
                '}';
    }
}