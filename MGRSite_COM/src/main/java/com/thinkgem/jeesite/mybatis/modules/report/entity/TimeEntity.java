package com.thinkgem.jeesite.mybatis.modules.report.entity;

/**
 * @author yang.kq
 * @version 1.0
 * @date 2019/12/16 11:20
 */
public class TimeEntity {
  private String year;
  private String month;
  private String day;

  public void setYear(String year) {
    this.year = year;
  }

  public String getYear() {
    return year;
  }

  public String getMonth() {
    return month;
  }

  public void setMonth(String month) {
    this.month = month;
  }

  public void setDay(String day) {
    this.day = day;
  }

  public String getDay() {
    return day;
  }

  public String getTime() {
    return year + "-" + month + "-" + day;
  }
}
