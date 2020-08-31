package com.nmpiesat.idata.interfacedata.entity;


import java.io.Serializable;

public class InterfaceDefine implements Serializable {

    private String dataCode;
    private String dataName;
    private String dataClassId;
    private int serialNoInClass;
    private String dDataId;
    private String staNetCode;

    public String getDataCode() {
        return dataCode;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getDataClassId() {
        return dataClassId;
    }

    public void setDataClassId(String dataClassId) {
        this.dataClassId = dataClassId;
    }

    public int getSerialNoInClass() {
        return serialNoInClass;
    }

    public void setSerialNoInClass(int serialNoInClass) {
        this.serialNoInClass = serialNoInClass;
    }

    public String getdDataId() {
        return dDataId;
    }

    public void setdDataId(String dDataId) {
        this.dDataId = dDataId;
    }

    public String getStaNetCode() {
        return staNetCode;
    }

    public void setStaNetCode(String staNetCode) {
        this.staNetCode = staNetCode;
    }

    public InterfaceDefine(String dataCode, String dataName, String dataClassId, int serialNoInClass, String dDataId, String staNetCode) {
        this.dataCode = dataCode;
        this.dataName = dataName;
        this.dataClassId = dataClassId;
        this.serialNoInClass = serialNoInClass;
        this.dDataId = dDataId;
        this.staNetCode = staNetCode;
    }

    public InterfaceDefine(){}

    @Override
    public String toString() {
        return "InterfaceDefine{" +
                "dataCode='" + dataCode + '\'' +
                ", dataName='" + dataName + '\'' +
                ", dataClassId='" + dataClassId + '\'' +
                ", serialNoInClass=" + serialNoInClass +
                ", dDataId='" + dDataId + '\'' +
                ", staNetCode='" + staNetCode + '\'' +
                '}';
    }
}
