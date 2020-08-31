/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.musicuser.entity;

import com.thinkgem.jeesite.mybatis.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

/**
 * music用户维护Entity
 *
 * @author yang.kq
 * @version 2020-01-20
 */
public class MusicInfo extends DataEntity<MusicInfo> {

  private static final long serialVersionUID = 1L;
  private String name; // 用户名
  private String password; // 用户密码
  private String orgid; // 工作单位

  public MusicInfo() {
    super();
  }

  public MusicInfo(String id) {
    super(id);
  }

  @Length(min = 1, max = 50, message = "用户名长度必须介于 1 和 50 之间")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Length(min = 1, max = 50, message = "用户密码长度必须介于 1 和 50 之间")
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Length(min = 1, max = 50, message = "工作单位长度必须介于 1 和 50 之间")
  public String getOrgid() {
    return orgid;
  }

  public void setOrgid(String orgid) {
    this.orgid = orgid;
  }
}
