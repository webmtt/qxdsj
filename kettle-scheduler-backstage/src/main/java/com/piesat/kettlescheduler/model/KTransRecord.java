package com.piesat.kettlescheduler.model;

import java.io.Serializable;
import java.util.Date;

/**
 * k_trans_record
 * @author 
 */
public class KTransRecord implements Serializable {
    /**
     * 转换记录ID
     */
    private Integer recordId;

    /**
     * 转换ID
     */
    private Integer recordTrans;

    /**
     * 启动时间
     */
    private Date startTime;

    /**
     * 停止时间
     */
    private Date stopTime;

    /**
     * 任务执行结果（1：成功；2：失败）
     */
    private Integer recordStatus;

    /**
     * 添加人
     */
    private Integer addUser;

    /**
     * 转换日志记录文件保存位置
     */
    private String logFilePath;

    private static final long serialVersionUID = 1L;

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Integer getRecordTrans() {
        return recordTrans;
    }

    public void setRecordTrans(Integer recordTrans) {
        this.recordTrans = recordTrans;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public Integer getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }

    public Integer getAddUser() {
        return addUser;
    }

    public void setAddUser(Integer addUser) {
        this.addUser = addUser;
    }

    public String getLogFilePath() {
        return logFilePath;
    }

    public void setLogFilePath(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    @Override
    public String toString() {
        return "KTransRecord{" +
                "recordId=" + recordId +
                ", recordTrans=" + recordTrans +
                ", startTime=" + startTime +
                ", stopTime=" + stopTime +
                ", recordStatus=" + recordStatus +
                ", addUser=" + addUser +
                ", logFilePath='" + logFilePath + '\'' +
                '}';
    }
}