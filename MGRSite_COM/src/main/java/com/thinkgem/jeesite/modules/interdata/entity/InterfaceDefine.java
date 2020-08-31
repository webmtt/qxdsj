package com.thinkgem.jeesite.modules.interdata.entity;

import com.thinkgem.jeesite.mybatis.common.persistence.DataEntity;

public class InterfaceDefine extends DataEntity<InterfaceDefine> {

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
}
