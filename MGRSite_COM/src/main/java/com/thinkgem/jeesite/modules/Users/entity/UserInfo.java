package com.thinkgem.jeesite.modules.Users.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/** 用户Entity */
@Entity
@Table(name = "SUP_USERINFO")
public class UserInfo implements Serializable {

  private static final long serialVersionUID = 1L;
  private String iD; // 用户ID
  /*private String userName2;//用户名
*/ private String userName; // 登录名
  private String emailName;//邮箱
  private String password; // 登录密码
  private int iscreate;//创建产品库页面（0-否，1-是）
  /*private String cpwd;*/
  // ftp密码
  private String chName; // 姓名
  private String phone; // 办公电话
  private String orgID; // 工作单位
  private String wechatNumber; // 微信号
  private String mobile; // 手机
  private String userType; // 用户类型(默认 1:内网首页登陆；2：管理站点登陆；3内网和管理站点同时登陆。)
  private int userRankID; // 用户级别（默认：2：国家级用户权限）
  private int isActive; // 激活状态（默认0：待审核，1：审核通过，2：不通过，5：全部）
  private String activeCode; // 激活码
  private Date lastLoginDate; // 最后登录日期
  private Date created; // 记录创建时间
  private String createdBy; // 记录创建主机名
  private Date updated; // 记录更新时间
  private String updatedBy; // 记录更新主机名
  private String delFlag; // 删除标志
  private String position; // 职位
  /*private int regSource;*/
  private int isSpecDatarole; // 是否已绑定角色
  private int servicelevel;
  private String dataInfo;//所需数据信息
  private String url; // 产品库配置地址

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setIscreate(int iscreate) {
    this.iscreate = iscreate;
  }

  public int getIscreate() {
    return iscreate;
  }

  public void setDataInfo(String dataInfo) {
    this.dataInfo = dataInfo;
  }

  public String getDataInfo() {
    return dataInfo;
  }
  /*    public int getRegSource() {
  	return regSource;
  }
  public void setRegSource(int regSource) {
  	this.regSource = regSource;
  }*/
  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public String getJobTitle() {
    return jobTitle;
  }

  public void setJobTitle(String jobTitle) {
    this.jobTitle = jobTitle;
  }
  /*public String getProvince() {
  	return province;
  }
  public void setProvince(String province) {
  	this.province = province;
  }*/
  /*public String getCompany() {
  	return company;
  }
  public void setCompany(String company) {
  	this.company = company;
  }*/
  public static long getSerialversionuid() {
    return serialVersionUID;
  }
  // 职称
  private String jobTitle;
  // 年龄范围
  private Integer ageRange;

  private Integer phoneModel;
  // 手机类型
  /*private String province;*/
  // 省份
  /*private String company;*/
  // 单位
  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getChName() {
    return chName;
  }

  public void setChName(String chName) {
    this.chName = chName;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getOrgID() {
    return orgID;
  }

  public void setOrgID(String orgID) {
    this.orgID = orgID;
  }

  public String getWechatNumber() {
    return wechatNumber;
  }

  public void setWechatNumber(String wechatNumber) {
    this.wechatNumber = wechatNumber;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public int getUserRankID() {
    return userRankID;
  }

  public void setUserRankID(int userRankID) {
    this.userRankID = userRankID;
  }

  @Id
  public String getiD() {
    return iD;
  }

  public void setiD(String iD) {
    this.iD = iD;
  }

  public int getIsActive() {
    return isActive;
  }

  public void setIsActive(int isActive) {
    this.isActive = isActive;
  }

  public String getActiveCode() {
    return activeCode;
  }

  public void setActiveCode(String activeCode) {
    this.activeCode = activeCode;
  }

  public Date getLastLoginDate() {
    return lastLoginDate;
  }

  public void setLastLoginDate(Date lastLoginDate) {
    this.lastLoginDate = lastLoginDate;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public Date getUpdated() {
    return updated;
  }

  public void setUpdated(Date updated) {
    this.updated = updated;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public String getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }

  public String getUserType() {
    return userType;
  }

  public void setUserType(String userType) {
    this.userType = userType;
  }

  public String getDelFlag() {
    return delFlag;
  }

  public void setDelFlag(String delFlag) {
    this.delFlag = delFlag;
  }
  /*	public String getUserName2() {
  	return userName2;
  }
  public void setUserName2(String userName2) {
  	this.userName2 = userName2;
  }*/
  /*public String getCpwd() {
  	return cpwd;
  }
  public void setCpwd(String cpwd) {
  	this.cpwd = cpwd;
  }*/
  public Integer getAgeRange() {
    return ageRange;
  }

  public void setAgeRange(Integer ageRange) {
    this.ageRange = ageRange;
  }

  public Integer getPhoneModel() {
    return phoneModel;
  }

  public void setPhoneModel(Integer phoneModel) {
    this.phoneModel = phoneModel;
  }

  public int getServicelevel() {
    return servicelevel;
  }

  public void setServicelevel(int servicelevel) {
    this.servicelevel = servicelevel;
  }

  public void setEmailName(String emailName) {
    this.emailName = emailName;
  }

  public String getEmailName() {
    return emailName;
  }
}
