package cma.cimiss2.dpc.decoder.bean.upar;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class UparChnMulFtmWewTab {
    private String dRecordId;//记录标识

    public String getdDataId() {
        return dDataId;
    }

    public void setdDataId(String dDataId) {
        this.dDataId = dDataId;
    }

    private String dDataId;

    private String v01301;//区站号(字符)

    private Short v04001;//资料观测年

    private Short v04002;//资料观测月

    private Short v04003;//资料观测日

    private String times;//时次

    private String dDatetime;//资料时间

    private BigDecimal arrivaltime;//观测高度到达时间

    private BigDecimal v10009;//位势高度

    private BigDecimal v11001;//风向

    private BigDecimal v11002;//风速

    private Short q11001;//风向质量控制码

    private Short q11002;//风速质量控制码

    public String getdRecordId() {
        return dRecordId;
    }

    public void setdRecordId(String dRecordId) {
        this.dRecordId = dRecordId == null ? null : dRecordId.trim();
    }

    public String getV01301() {
        return v01301;
    }

    public void setV01301(String v01301) {
        this.v01301 = v01301 == null ? null : v01301.trim();
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

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getdDatetime() {
        return dDatetime;
    }

    public void setdDatetime(String dDatetime) {
        this.dDatetime = dDatetime;
    }

    public BigDecimal getArrivaltime() {
        return arrivaltime;
    }

    public void setArrivaltime(BigDecimal arrivaltime) {
        this.arrivaltime = arrivaltime;
    }

    public BigDecimal getV10009() {
        return v10009;
    }

    public void setV10009(BigDecimal v10009) {
        this.v10009 = v10009;
    }

    public BigDecimal getV11001() {
        return v11001;
    }

    public void setV11001(BigDecimal v11001) {
        this.v11001 = v11001;
    }

    public BigDecimal getV11002() {
        return v11002;
    }

    public void setV11002(BigDecimal v11002) {
        this.v11002 = v11002;
    }

    public Short getQ11001() {
        return q11001;
    }

    public void setQ11001(Short q11001) {
        this.q11001 = q11001;
    }

    public Short getQ11002() {
        return q11002;
    }

    public void setQ11002(Short q11002) {
        this.q11002 = q11002;
    }
}