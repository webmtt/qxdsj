package com.piesat.kettlescheduler.model;

import java.io.Serializable;
import java.util.Date;

/**
 * k_category
 * @author 
 */
public class KCategory implements Serializable {
    /**
     * 分类ID
     */
    private Integer categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

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

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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
        return "KCategory{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", addTime=" + addTime +
                ", addUser=" + addUser +
                ", editTime=" + editTime +
                ", editUser=" + editUser +
                ", delFlag=" + delFlag +
                '}';
    }
}