package com.thinkgem.jeesite.modules.Users.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/** 用户角色表 */
@Entity
@Table(name = "SUP_ROLEINFO")
public class RoleInfo {
  @Id private String id;
  private String name;
  private String create_by;
  private Date create_date;
  private String update_by;
  private Date update_date;
  private String remarks;
  private char del_flag;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCreate_by() {
    return create_by;
  }

  public void setCreate_by(String create_by) {
    this.create_by = create_by;
  }

  public Date getCreate_date() {
    return create_date;
  }

  public void setCreate_date(Date create_date) {
    this.create_date = create_date;
  }

  public String getUpdate_by() {
    return update_by;
  }

  public void setUpdate_by(String update_by) {
    this.update_by = update_by;
  }

  public Date getUpdate_date() {
    return update_date;
  }

  public void setUpdate_date(Date update_date) {
    this.update_date = update_date;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public char getDel_flag() {
    return del_flag;
  }

  public void setDel_flag(char del_flag) {
    this.del_flag = del_flag;
  }
}
