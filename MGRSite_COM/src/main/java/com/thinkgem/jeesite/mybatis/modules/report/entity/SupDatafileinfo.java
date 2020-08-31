/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.report.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.mybatis.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 报表数据文件管理Entity
 *
 * @author yang.kq
 * @version 2019-11-06
 */
public class SupDatafileinfo extends DataEntity<SupDatafileinfo> {

  private static final long serialVersionUID = 1L;
  private String dataName; // 文件名称
  private String link; // 存储路径
  private String dataType; // 文件类型
  private Date starttimeCon; // 内容开始时间
  private Date endtimeCon; // 文件结束时间
  private int state; // 文件状态 1-待入库，2-入库中，3-已入库

  public void setState(int state) {
    this.state = state;
  }

  public int getState() {
    return state;
  }

  public SupDatafileinfo() {
    super();
  }

  public SupDatafileinfo(String id) {
    super(id);
  }

  @Length(min = 1, max = 50, message = "文件名称长度必须介于 1 和 50 之间")
  public String getDataName() {
    return dataName;
  }

  public void setDataName(String dataName) {
    this.dataName = dataName;
  }

  @Length(min = 1, max = 900, message = "存储路径长度必须介于 1 和 900 之间")
  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  @Length(min = 1, max = 11, message = "文件类型长度必须介于 1 和 11 之间")
  public String getDataType() {
    return dataType;
  }

  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @NotNull(message = "内容开始时间不能为空")
  public Date getStarttimeCon() {
    return starttimeCon;
  }

  public void setStarttimeCon(Date starttimeCon) {
    this.starttimeCon = starttimeCon;
  }

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @NotNull(message = "文件结束时间不能为空")
  public Date getEndtimeCon() {
    return endtimeCon;
  }

  public void setEndtimeCon(Date endtimeCon) {
    this.endtimeCon = endtimeCon;
  }
}
