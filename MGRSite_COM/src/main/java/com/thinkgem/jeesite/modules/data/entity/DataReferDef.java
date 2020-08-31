package com.thinkgem.jeesite.modules.data.entity;

import javax.persistence.*;
/**
 * 描述：引用文献
 *
 * @author Administrator
 * @version 1.0 2016年10月9日
 */
@Entity
@Table(name = "BMD_DATAREFERDEF")
public class DataReferDef {

  private Integer referid;
  private String datacode;
  private String refername;
  private Integer orderno;
  private Integer invalid;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Integer getReferid() {
    return referid;
  }

  public void setReferid(Integer referid) {
    this.referid = referid;
  }

  public String getDatacode() {
    return datacode;
  }

  public void setDatacode(String datacode) {
    this.datacode = datacode;
  }

  public String getRefername() {
    return refername;
  }

  public void setRefername(String refername) {
    this.refername = refername;
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
    return "DataReferDef [referid="
        + referid
        + ", datacode="
        + datacode
        + ", refername="
        + refername
        + ", orderno="
        + orderno
        + ", invalid="
        + invalid
        + "]";
  }
}
