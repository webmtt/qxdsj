package com.piesat.kettlescheduler.model;

import java.io.Serializable;
import java.util.Date;

/**
 * km_kettle_log
 * @author 
 */
public class KmKettleLog implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 作业id
     */
    private Integer jobId;

    /**
     * 作业记录id
     */
    private Integer jobRecordId;

    private Integer categoryId;

    /**
     * 类型（1：job，2：trans）
     */
    private Integer type;

    /**
     * 同步条数
     */
    private Long count;

    private Date date;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 批次号
     */
    private String batchId;

    private Long sumCount;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Integer getJobRecordId() {
        return jobRecordId;
    }

    public void setJobRecordId(Integer jobRecordId) {
        this.jobRecordId = jobRecordId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public Long getSumCount() {
        return sumCount;
    }

    public void setSumCount(Long sumCount) {
        this.sumCount = sumCount;
    }

    @Override
    public String toString() {
        return "KmKettleLog{" +
                "id=" + id +
                ", jobId=" + jobId +
                ", jobRecordId=" + jobRecordId +
                ", categoryId=" + categoryId +
                ", type=" + type +
                ", count=" + count +
                ", date=" + date +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", batchId='" + batchId + '\'' +
                ", sumCount=" + sumCount +
                '}';
    }
}