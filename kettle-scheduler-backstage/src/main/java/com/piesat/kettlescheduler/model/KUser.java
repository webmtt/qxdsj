package com.piesat.kettlescheduler.model;

import java.io.Serializable;
import java.util.Date;

/**
 * k_user
 * @author 
 */
public class KUser implements Serializable {
    /**
     * 用户ID
     */
    private Integer uId;

    /**
     * 用户昵称
     */
    private String uNickname;

    /**
     * 用户邮箱
     */
    private String uEmail;

    /**
     * 用于电话
     */
    private String uPhone;

    /**
     * 用户账号
     */
    private String uAccount;

    /**
     * 用户密码
     */
    private String uPassword;

    /**
     * 添加时间
     */
    private Date addTime;

    /**
     * 添加者
     */
    private Integer addUser;

    /**
     * 编辑时间
     */
    private Date editTime;

    /**
     * 编辑者
     */
    private Integer editUser;

    /**
     * 是否删除（1：存在；0：删除）
     */
    private Integer delFlag;

    private static final long serialVersionUID = 1L;

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public String getuNickname() {
        return uNickname;
    }

    public void setuNickname(String uNickname) {
        this.uNickname = uNickname;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getuPhone() {
        return uPhone;
    }

    public void setuPhone(String uPhone) {
        this.uPhone = uPhone;
    }

    public String getuAccount() {
        return uAccount;
    }

    public void setuAccount(String uAccount) {
        this.uAccount = uAccount;
    }

    public String getuPassword() {
        return uPassword;
    }

    public void setuPassword(String uPassword) {
        this.uPassword = uPassword;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Integer getAddUser() {
        return addUser;
    }

    public void setAddUser(Integer addUser) {
        this.addUser = addUser;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public Integer getEditUser() {
        return editUser;
    }

    public void setEditUser(Integer editUser) {
        this.editUser = editUser;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return "KUser{" +
                "uId=" + uId +
                ", uNickname='" + uNickname + '\'' +
                ", uEmail='" + uEmail + '\'' +
                ", uPhone='" + uPhone + '\'' +
                ", uAccount='" + uAccount + '\'' +
                ", uPassword='" + uPassword + '\'' +
                ", addTime=" + addTime +
                ", addUser=" + addUser +
                ", editTime=" + editTime +
                ", editUser=" + editUser +
                ", delFlag=" + delFlag +
                '}';
    }
}