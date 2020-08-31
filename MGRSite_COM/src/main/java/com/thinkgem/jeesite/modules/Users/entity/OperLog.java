/*
 * @(#)OperLog.java 2016年3月16日
 *
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.Users.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 描述：用户管理日志
 *
 * @author Administrator
 * @version 1.0 2016年3月16日
 */
@Entity
@Table(name = "SUP_OperLog")
public class OperLog implements Serializable {
  /** the OperLog.java */
  private static final long serialVersionUID = 1L;

  @Id private String id;
  private String operUser;
  private String operType;
  private String operedUser;
  private String remarks;
  private Date created;
  private String createdBy;
  private Date updated;
  private String updatedBy;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getOperUser() {
    return operUser;
  }

  public void setOperUser(String operUser) {
    this.operUser = operUser;
  }

  public String getOperType() {
    return operType;
  }

  public void setOperType(String operType) {
    this.operType = operType;
  }

  public String getOperedUser() {
    return operedUser;
  }

  public void setOperedUser(String operedUser) {
    this.operedUser = operedUser;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public Date getUpdated() {
    return updated;
  }

  public void setUpdated(Date updated) {
    this.updated = updated;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public String getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }
}
