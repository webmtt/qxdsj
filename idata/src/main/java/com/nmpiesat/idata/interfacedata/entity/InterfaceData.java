package com.nmpiesat.idata.interfacedata.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * sys_interface
 * @author 
 */
public class InterfaceData implements Serializable {
    private String dataClassId;
    private String dataClassName;
    private int serialNo;
    private String description;
    private String shortName;

    public String getDataClassId() {
        return dataClassId;
    }

    public void setDataClassId(String dataClassId) {
        this.dataClassId = dataClassId;
    }

    public String getDataClassName() {
        return dataClassName;
    }

    public void setDataClassName(String dataClassName) {
        this.dataClassName = dataClassName;
    }

    public int getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(int serialNo) {
        this.serialNo = serialNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public InterfaceData(String dataClassId, String dataClassName, int serialNo, String description, String shortName) {
        this.dataClassId = dataClassId;
        this.dataClassName = dataClassName;
        this.serialNo = serialNo;
        this.description = description;
        this.shortName = shortName;
    }

    public InterfaceData(){}

    @Override
    public String toString() {
        return "InterfaceData{" +
                "dataClassId='" + dataClassId + '\'' +
                ", dataClassName='" + dataClassName + '\'' +
                ", serialNo=" + serialNo +
                ", description='" + description + '\'' +
                ", shortName='" + shortName + '\'' +
                '}';
    }
}