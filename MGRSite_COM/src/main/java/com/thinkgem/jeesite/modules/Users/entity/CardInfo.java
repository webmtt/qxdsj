package com.thinkgem.jeesite.modules.Users.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Timestamp;

/** 用户证件表 */
@Entity
@Table(name = "SUP_Card")
public class CardInfo implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id private String iD; // 用户ID
  //  private String validateType;
  private Blob EmpCard; // 证件照图片或协议图片
  //  private Blob protocalImg; // 协议图片
  private Timestamp created; // 记录创建时间
  private String createdBy; // 记录创建主机名
  private Timestamp updated; // 记录更新时间
  private String updatedBy; // 记录更新主机名
  private char delFlag; // 删除标志

  public Timestamp getCreated() {
    return created;
  }

  public void setCreated(Timestamp created) {
    this.created = created;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public Timestamp getUpdated() {
    return updated;
  }

  public void setUpdated(Timestamp updated) {
    this.updated = updated;
  }

  public String getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }

  public char getDelFlag() {
    return delFlag;
  }

  public void setDelFlag(char delFlag) {
    this.delFlag = delFlag;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  public String getiD() {
    return iD;
  }

  public void setiD(String iD) {
    this.iD = iD;
  }

  public Blob getEmpCard() {
    return EmpCard;
  }

  public void setEmpCard(Blob empCard) {
    EmpCard = empCard;
  }

  //  public String getValidateType() {
  //    return validateType;
  //  }
  //
  //  public void setValidateType(String validateType) {
  //    this.validateType = validateType;
  //  }

  //  public Blob getProtocalImg() {
  //    return protocalImg;
  //  }
  //
  //  public void setProtocalImg(Blob protocalImg) {
  //    this.protocalImg = protocalImg;
  //  }
}
