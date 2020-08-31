package com.nmpiesat.idata.user.entity;

import java.io.Serializable;
import java.sql.Date;

/**
 * 数据角色
 *
 * @author yangkq
 * @version 1.0 2020年3月03日
 */

public class DataRole implements Serializable {

  private static final long serialVersionUID = 1L;

  private String dataroleId; // 角色id
  private String dataroleName; // 角色名
  private String descriptionChn;
  private String descriptionEng;
  private int orderNo;
  private int invalid;
  private Date created; // 创建时间
  private String createdBy; // 创建者
  private Date updated; // 修改时间
  private String updatedBy; // 修改者

  public String getDataroleId() {
    return dataroleId;
  }

  public void setDataroleId(String dataroleId) {
    this.dataroleId = dataroleId;
  }

  public String getDataroleName() {
    return dataroleName;
  }

  public void setDataroleName(String dataroleName) {
    this.dataroleName = dataroleName;
  }

  public String getDescriptionChn() {
    return descriptionChn;
  }

  public void setDescriptionChn(String descriptionChn) {
    this.descriptionChn = descriptionChn;
  }

  public String getDescriptionEng() {
    return descriptionEng;
  }

  public void setDescriptionEng(String descriptionEng) {
    this.descriptionEng = descriptionEng;
  }

  public int getOrderNo() {
    return orderNo;
  }

  public void setOrderNo(int orderNo) {
    this.orderNo = orderNo;
  }

  public int getInvalid() {
    return invalid;
  }

  public void setInvalid(int invalid) {
    this.invalid = invalid;
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

  public static long getSerialversionuid() {
    return serialVersionUID;
  }
}
