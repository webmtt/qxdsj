package com.nmpiesat.idata.data.entity;

import java.io.Serializable;

public class DataReferDef implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer referid;
    private String datacode;
    private String refername;
    private Integer orderno;
    private Integer invalid;
    public Integer getReferid() {
        return referid;
    }
    public void setReferid(Integer referid) {
        this.referid = referid;
    }
    public String getDatacode() {
        return datacode;
    }
    public void setDatacode(String datacode) {
        this.datacode = datacode;
    }
    public String getRefername() {
        return refername;
    }
    public void setRefername(String refername) {
        this.refername = refername;
    }
    public Integer getOrderno() {
        return orderno;
    }
    public void setOrderno(Integer orderno) {
        this.orderno = orderno;
    }
    public Integer getInvalid() {
        return invalid;
    }
    public void setInvalid(Integer invalid) {
        this.invalid = invalid;
    }
    @Override
    public String toString() {
        return "DataReferDef [referid=" + referid + ", datacode=" + datacode
                + ", refername=" + refername + ", orderno=" + orderno
                + ", invalid=" + invalid + "]";
    }
}
