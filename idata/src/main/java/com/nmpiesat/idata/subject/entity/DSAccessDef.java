package com.nmpiesat.idata.subject.entity;

import java.io.Serializable;

/**
 * @author yangkq
 * @version 1.0
 * @date 2020/2/27
 */
public class DSAccessDef implements Serializable {

    private static final long serialVersionUID = 1L;
    private String dSAccessCode;
    private String accessType;
    private String accessURL;
    private String dataParaName;
    private String invalid;
    public String getdSAccessCode() {
        return dSAccessCode;
    }
    public void setdSAccessCode(String dSAccessCode) {
        this.dSAccessCode = dSAccessCode;
    }
    public String getAccessType() {
        return accessType;
    }
    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }
    public String getAccessURL() {
        return accessURL;
    }
    public void setAccessURL(String accessURL) {
        this.accessURL = accessURL;
    }
    public String getDataParaName() {
        return dataParaName;
    }
    public void setDataParaName(String dataParaName) {
        this.dataParaName = dataParaName;
    }
    public String getInvalid() {
        return invalid;
    }
    public void setInvalid(String invalid) {
        this.invalid = invalid;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
