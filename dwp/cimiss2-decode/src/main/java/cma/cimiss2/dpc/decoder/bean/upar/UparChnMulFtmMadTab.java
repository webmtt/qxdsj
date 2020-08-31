package cma.cimiss2.dpc.decoder.bean.upar;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class UparChnMulFtmMadTab {
    private String dRecordId;//记录标识

    private String dDataId;

    public String getdDataId() {
        return dDataId;
    }

    public void setdDataId(String dDataId) {
        this.dDataId = dDataId;
    }

    private String dDatetime;//资料时间

    private Short v04001;//年

    private Short v04002;//月

    private Short v04003;//日

    private String v01301;//区站号/观测平台标识(字符)

    private Integer v01300;//区站号/观测平台标识(数字)

    private BigDecimal vTimeLevel;//实际观测时次

    private BigDecimal v10004;//气压

    private BigDecimal v10009;//位势高度

    private BigDecimal v12001;//温度

    private BigDecimal v12003;//露点温度

    private BigDecimal v11001;//风向

    private BigDecimal v11002;//风速

    private Short q10009;//位势高度质量控制码

    private BigDecimal q12001;//温度质量控制码

    private Short q12003;//露点温度质量控制码

    private Short q11001;//风向质量控制码

    private Short q11002;//风速质量控制码

    public String getdRecordId() {
        return dRecordId;
    }

    public void setdRecordId(String dRecordId) {
        this.dRecordId = dRecordId == null ? null : dRecordId.trim();
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

    public String getV01301() {
        return v01301;
    }

    public void setV01301(String v01301) {
        this.v01301 = v01301 == null ? null : v01301.trim();
    }

    public Integer getV01300() {
        return v01300;
    }

    public void setV01300(Integer v01300) {
        this.v01300 = v01300;
    }

    public BigDecimal getvTimeLevel() {
        return vTimeLevel;
    }

    public void setvTimeLevel(BigDecimal vTimeLevel) {
        this.vTimeLevel = vTimeLevel;
    }

    public BigDecimal getV10004() {
        return v10004;
    }

    public void setV10004(BigDecimal v10004) {
        this.v10004 = v10004;
    }

    public BigDecimal getV10009() {
        return v10009;
    }

    public void setV10009(BigDecimal v10009) {
        this.v10009 = v10009;
    }

    public BigDecimal getV12001() {
        return v12001;
    }

    public void setV12001(BigDecimal v12001) {
        this.v12001 = v12001;
    }

    public BigDecimal getV12003() {
        return v12003;
    }

    public void setV12003(BigDecimal v12003) {
        this.v12003 = v12003;
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

    public Short getQ10009() {
        return q10009;
    }

    public void setQ10009(Short q10009) {
        this.q10009 = q10009;
    }

    public BigDecimal getQ12001() {
        return q12001;
    }

    public void setQ12001(BigDecimal q12001) {
        this.q12001 = q12001;
    }

    public Short getQ12003() {
        return q12003;
    }

    public void setQ12003(Short q12003) {
        this.q12003 = q12003;
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