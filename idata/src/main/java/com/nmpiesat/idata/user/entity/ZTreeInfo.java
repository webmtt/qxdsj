package com.nmpiesat.idata.user.entity;

/**
 * @author yang.kq
 * @version 1.0
 * @date 2019/12/6 10:28
 */
public class ZTreeInfo {
  private String categoryid; // id
  private String chnname; // 名称
  private Integer pid; // 父id

  public void setCategoryid(String categoryid) {
    this.categoryid = categoryid;
  }

  public void setChnname(String chnname) {
    this.chnname = chnname;
  }

  public void setPid(Integer pid) {
    this.pid = pid;
  }

  public String getCategoryid() {
    return categoryid;
  }

  public String getChnname() {
    return chnname;
  }

  public Integer getPid() {
    return pid;
  }
}
