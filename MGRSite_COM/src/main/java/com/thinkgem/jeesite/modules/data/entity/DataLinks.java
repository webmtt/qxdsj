package com.thinkgem.jeesite.modules.data.entity;

import javax.persistence.*;
/**
 * 描述：相关链接
 *
 * @author Administrator
 * @version 1.0 2016年10月9日
 */
@Entity
@Table(name = "BMD_DATALINKS")
public class DataLinks {
  private Integer linkid;
  private String datacode;
  private Integer linktype;
  private String linkname;
  private String linkurl;
  private Integer orderno;
  private Integer invalid;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Integer getLinkid() {
    return linkid;
  }

  public void setLinkid(Integer linkid) {
    this.linkid = linkid;
  }

  public String getDatacode() {
    return datacode;
  }

  public void setDatacode(String datacode) {
    this.datacode = datacode;
  }

  public Integer getLinktype() {
    return linktype;
  }

  public void setLinktype(Integer linktype) {
    this.linktype = linktype;
  }

  public String getLinkname() {
    return linkname;
  }

  public void setLinkname(String linkname) {
    this.linkname = linkname;
  }

  public String getLinkurl() {
    return linkurl;
  }

  public void setLinkurl(String linkurl) {
    this.linkurl = linkurl;
  }

  public Integer getOrderno() {
    return orderno;
  }

  public void setOrderno(Integer orderno) {
    this.orderno = orderno;
  }

  public Integer getInvalid() {
    return invalid;
  }

  public void setInvalid(Integer invalid) {
    this.invalid = invalid;
  }

  @Override
  public String toString() {
    return "DataLinks [linkid="
        + linkid
        + ", datacode="
        + datacode
        + ", linktype="
        + linktype
        + ", linkname="
        + linkname
        + ", linkurl="
        + linkurl
        + ", orderno="
        + orderno
        + ", invalid="
        + invalid
        + "]";
  }
}
