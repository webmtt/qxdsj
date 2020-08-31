package cma.cimiss2.dpc.decoder.bean.radi;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class RadiHhhtUrayTab {
    private String dRetainId;

    public String getdRetainId() {
        return dRetainId;
    }

    public void setdRetainId(String dRetainId) {
        this.dRetainId = dRetainId;
    }

    private String dDataId;

    private String dIymdhm;//入库时间

    private String dDatetime;//资料时间

    private Short v04001;//年

    private Short v04002;//月

    private Short v04003;//日

    private Short v04004;//时

    private Short v04005;//分

    private String v01301;//区站号/观测平台标识(字符)

    private Integer v01300;//区站号/观测平台标识(数字)

    private String uva;//UVA

    private String uvb;//UVB

    private String cmaUvi;//CMA_UVI

    private String inlUvi;//INL_UVI

    public String getdDataId() {
        return dDataId;
    }

    public void setdDataId(String dDataId) {
        this.dDataId = dDataId;
    }

    public String getdIymdhm() {
        return dIymdhm;
    }

    public void setdIymdhm(String dIymdhm) {
        this.dIymdhm = dIymdhm;
    }

    public String getdDatetime() {
        return dDatetime;
    }

    public void setdDatetime(String dDatetime) {
        this.dDatetime = dDatetime;
    }

    public Short getV04001() {
        return v04001;
    }

    public void setV04001(Short v04001) {
        this.v04001 = v04001;
    }

    public Short getV04002() {
        return v04002;
    }

    public void setV04002(Short v04002) {
        this.v04002 = v04002;
    }

    public Short getV04003() {
        return v04003;
    }

    public void setV04003(Short v04003) {
        this.v04003 = v04003;
    }

    public Short getV04004() {
        return v04004;
    }

    public void setV04004(Short v04004) {
        this.v04004 = v04004;
    }

    public Short getV04005() {
        return v04005;
    }

    public void setV04005(Short v04005) {
        this.v04005 = v04005;
    }

    public String getV01301() {
        return v01301;
    }

    public void setV01301(String v01301) {
        this.v01301 = v01301;
    }

    public Integer getV01300() {
        return v01300;
    }

    public void setV01300(Integer v01300) {
        this.v01300 = v01300;
    }

    public String getUva() {
        return uva;
    }

    public void setUva(String uva) {
        this.uva = uva;
    }

    public String getUvb() {
        return uvb;
    }

    public void setUvb(String uvb) {
        this.uvb = uvb;
    }

    public String getCmaUvi() {
        return cmaUvi;
    }

    public void setCmaUvi(String cmaUvi) {
        this.cmaUvi = cmaUvi;
    }

    public String getInlUvi() {
        return inlUvi;
    }

    public void setInlUvi(String inlUvi) {
        this.inlUvi = inlUvi;
    }
}