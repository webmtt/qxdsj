package com.piesat.core.model;

import java.util.Date;

/**
 * @title: kettle-scheduler->KmKettleLog
 * @description: 任务执行日志
 * @author: YuWenjie
 * @date: 2020-04-28 10:12
 **/
public class KmKettleLog {

    private Integer id;
    private Integer jobId;
    private Integer jobRecordId;
    private Integer categoryId;
    private Integer type;
    private Long count;
    private Date date;
    private Long sumCount;

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
                ", sumCount=" + sumCount +
                '}';
    }
}
