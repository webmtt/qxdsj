package com.thinkgem.jeesite.modules.Users.entity;

import java.io.Serializable;
import java.util.Date;

/** 用户证件表 */
public class student implements Serializable {
  /** the student.java */
  private static final long serialVersionUID = 1L;

  private String name;
  private Date date;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }
}
