package com.piesat.core.model;

import java.util.Date;

public class KData {
    /**
      * 分类ID
      **/
    private Integer dataId ;
    /**
     * 分类名称
     **/
    private String dataName;
    /**
     * 资料大类
     **/
    private String dataBigClass;
    /**
     * 资料小类
     **/
    private String dataSmallClass;
    /**
     * 服务对象
     **/
    private String dataServiceObject;
    /**
     * 单条数据量    字节
     **/
    private String dataSize;
    /**
     * 添加者
     **/
    private Integer addUser ;
    /**
     * 是否删除（1：存在；0：删除）
     **/
    private Integer delFlag ;
    /**
     * 编辑者
     **/
    private Integer editUser ;
    /**
     * 添加时间
     **/
    private Date addTime ;
    /**
     * 编辑时间
     **/
    private Date editTime ;

    public KData() {
    }

    public KData(Integer dataId, String dataName, String dataBigClass, String dataSmallClass, String dataServiceObject, String dataSize, Integer addUser, Integer delFlag, Integer editUser, Date addTime, Date editTime) {
        this.dataId = dataId;
        this.dataName = dataName;
        this.dataBigClass = dataBigClass;
        this.dataSmallClass = dataSmallClass;
        this.dataServiceObject = dataServiceObject;
        this.dataSize = dataSize;
        this.addUser = addUser;
        this.delFlag = delFlag;
        this.editUser = editUser;
        this.addTime = addTime;
        this.editTime = editTime;
    }

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

    public Integer getAddUser() {
        return addUser;
    }

    public void setAddUser(Integer addUser) {
        this.addUser = addUser;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Integer getEditUser() {
        return editUser;
    }

    public void setEditUser(Integer editUser) {
        this.editUser = editUser;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }
}
