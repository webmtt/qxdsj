package com.thinkgem.jeesite.modules.UserDataRole.entity;

/**
 * @author yang.kq
 * @version 1.0
 * @date 2019/12/6 10:28
 */
public class ZTreeInfo {
  private String id; // id
  private String name; // 名称
  private String pid; // 父id

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

  public String getPid() {
    return pid;
  }

  public void setPid(String pid) {
    this.pid = pid;
  }
}
