/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.arithmetic.entity;

import com.thinkgem.jeesite.mybatis.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

/**
 * 算法信息管理Entity
 *
 * @author yang.kq
 * @version 2019-11-22
 */
public class SupArithmeticsPackage extends DataEntity<SupArithmeticsPackage> {

  private static final long serialVersionUID = 1L;
  private String ariName; // 算法名称
  private String ariPackageName; // 算法包路径
  private String classUrl; // 算法类路径
  private String ariMethod; // 方法名
  private String purpose; // 用途
  private String status; // 当前状态
  private String params; // 方法参数信息
  private String pridictValue; // 预期值

  public void setPridictValue(String pridictValue) {
    this.pridictValue = pridictValue;
  }

  public String getPridictValue() {
    return pridictValue;
  }

  public String getParams() {
    return params;
  }

  public void setParams(String params) {
    this.params = params;
  }

  public SupArithmeticsPackage() {
    super();
  }

  public SupArithmeticsPackage(String id) {
    super(id);
  }

  @Length(min = 1, max = 255, message = "算法名称长度必须介于 1 和 255 之间")
  public String getAriName() {
    return ariName;
  }

  public void setAriName(String ariName) {
    this.ariName = ariName;
  }

  @Length(min = 1, max = 255, message = "算法包路径长度必须介于 1 和 255 之间")
  public String getAriPackageName() {
    return ariPackageName;
  }

  public void setAriPackageName(String ariPackageName) {
    this.ariPackageName = ariPackageName;
  }

  @Length(min = 1, max = 255, message = "算法类路径长度必须介于 1 和 255 之间")
  public String getClassUrl() {
    return classUrl;
  }

  public void setClassUrl(String classUrl) {
    this.classUrl = classUrl;
  }

  @Length(min = 1, max = 50, message = "方法名长度必须介于 1 和 50 之间")
  public String getAriMethod() {
    return ariMethod;
  }

  public void setAriMethod(String ariMethod) {
    this.ariMethod = ariMethod;
  }

  @Length(min = 1, max = 255, message = "用途长度必须介于 1 和 255 之间")
  public String getPurpose() {
    return purpose;
  }

  public void setPurpose(String purpose) {
    this.purpose = purpose;
  }

  @Length(min = 1, max = 10, message = "当前状态长度必须介于 1 和 10 之间")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
