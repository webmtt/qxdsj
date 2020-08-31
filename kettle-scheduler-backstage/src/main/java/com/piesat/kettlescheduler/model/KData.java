package com.piesat.kettlescheduler.model;

import java.io.Serializable;
import java.util.Date;

/**
 * k_data
 * @author 
 */
public class KData implements Serializable {
    /**
     * 分类ID
     */
    private Integer dataId;

    /**
     * 分类名称
     */
    private String dataName;

    /**
     * 资料大类
     */
    private String dataBigClass;

    /**
     * 资料小类
     */
    private String dataSmallClass;

    /**
     * 服务对象
     */
    private String dataServiceObject;

    /**
     * 单条数据量（字节）
     */
    private String dataSize;

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

    public Integer getDataId() {
        return dataId;
    }

    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getDataBigClass() {
        return dataBigClass;
    }

    public void setDataBigClass(String dataBigClass) {
        this.dataBigClass = dataBigClass;
    }

    public String getDataSmallClass() {
        return dataSmallClass;
    }

    public void setDataSmallClass(String dataSmallClass) {
        this.dataSmallClass = dataSmallClass;
    }

    public String getDataServiceObject() {
        return dataServiceObject;
    }

    public void setDataServiceObject(String dataServiceObject) {
        this.dataServiceObject = dataServiceObject;
    }

    public String getDataSize() {
        return dataSize;
    }

    public void setDataSize(String dataSize) {
        this.dataSize = dataSize;
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
        return "KData{" +
                "dataId=" + dataId +
                ", dataName='" + dataName + '\'' +
                ", dataBigClass='" + dataBigClass + '\'' +
                ", dataSmallClass='" + dataSmallClass + '\'' +
                ", dataServiceObject='" + dataServiceObject + '\'' +
                ", dataSize='" + dataSize + '\'' +
                ", addTime=" + addTime +
                ", addUser=" + addUser +
                ", editTime=" + editTime +
                ", editUser=" + editUser +
                ", delFlag=" + delFlag +
                '}';
    }
}