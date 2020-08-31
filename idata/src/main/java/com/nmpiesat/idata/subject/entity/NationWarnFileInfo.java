package com.nmpiesat.idata.subject.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yangkq
 * @version 1.0
 * @date 2020/2/27
 */
public class NationWarnFileInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String fileName;
    private String warnTitle;
    private String  issueTime;
    private String warnTypeCode;
    private String warnTypeName;
    private String warnLevel;
    private String content;
    private String reporter;
    private String checker;
    private Date reportTime;
    private String cimissInDbTime;
    private Integer invalid;
    private String createdBy;
    private Date created;
    private String updatedBy;
    private Date updated;
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getWarnTitle() {
        return warnTitle;
    }
    public void setWarnTitle(String warnTitle) {
        this.warnTitle = warnTitle;
    }
    public String getWarnTypeCode() {
        return warnTypeCode;
    }
    public void setWarnTypeCode(String warnTypeCode) {
        this.warnTypeCode = warnTypeCode;
    }
    public String getWarnTypeName() {
        return warnTypeName;
    }
    public void setWarnTypeName(String warnTypeName) {
        this.warnTypeName = warnTypeName;
    }
    public String getWarnLevel() {
        return warnLevel;
    }
    public void setWarnLevel(String warnLevel) {
        this.warnLevel = warnLevel;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getReporter() {
        return reporter;
    }
    public void setReporter(String reporter) {
        this.reporter = reporter;
    }
    public String getChecker() {
        return checker;
    }
    public void setChecker(String checker) {
        this.checker = checker;
    }
    public Date getReportTime() {
        return reportTime;
    }
    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }
    public String getCimissInDbTime() {
        return cimissInDbTime;
    }
    public void setCimissInDbTime(String cimissInDbTime) {
        this.cimissInDbTime = cimissInDbTime;
    }
    public Integer getInvalid() {
        return invalid;
    }
    public void setInvalid(Integer invalid) {
        this.invalid = invalid;
    }
    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    public Date getCreated() {
        return created;
    }
    public void setCreated(Date created) {
        this.created = created;
    }
    public String getUpdatedBy() {
        return updatedBy;
    }
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
    public Date getUpdated() {
        return updated;
    }
    public void setUpdated(Date updated) {
        this.updated = updated;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    public String getIssueTime() {
        return issueTime;
    }
    public void setIssueTime(String issueTime) {
        this.issueTime = issueTime;
    }
}
