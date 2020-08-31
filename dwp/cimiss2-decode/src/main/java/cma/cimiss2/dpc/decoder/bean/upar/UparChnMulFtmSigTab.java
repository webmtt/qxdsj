package cma.cimiss2.dpc.decoder.bean.upar;

import java.math.BigDecimal;
import java.sql.Timestamp;


public class UparChnMulFtmSigTab {
    private String dRecordId;//记录标识

    private String dDataId;

    private String V01301;

    public String getdDataId() {
        return dDataId;
    }

    public void setdDataId(String dDataId) {
        this.dDataId = dDataId;
    }

    private Short v04001;//年

    private Short v04002;//月

    private String dDatetime;//资料时间

    private String dUpdateTime;//时次

    private String vLevelC;//层号

    private BigDecimal v10004;//气压

    private BigDecimal v12001;//温度

    private BigDecimal v12003;//露点温度

    private Short qBasicparameter;//基本参数质量控制码

    private Short q07004;//气压质量控制码

    private Short q12001;//温度质量控制码

    private BigDecimal q12003;//露点温度质量控制码

    public String getV01301() {
        return V01301;
    }

    public void setV01301(String v01301) {
        V01301 = v01301;
    }

    public String getdRecordId() {
        return dRecordId;
    }

    public void setdRecordId(String dRecordId) {
        this.dRecordId = dRecordId == null ? null : dRecordId.trim();
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

    public String getdDatetime() {
        return dDatetime;
    }

    public void setdDatetime(String dDatetime) {
        this.dDatetime = dDatetime;
    }

    public String getdUpdateTime() {
        return dUpdateTime;
    }

    public void setdUpdateTime(String dUpdateTime) {
        this.dUpdateTime = dUpdateTime;
    }

    public String getvLevelC() {
        return vLevelC;
    }

    public void setvLevelC(String vLevelC) {
        this.vLevelC = vLevelC;
    }

    public BigDecimal getV10004() {
        return v10004;
    }

    public void setV10004(BigDecimal v10004) {
        this.v10004 = v10004;
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

    public Short getqBasicparameter() {
        return qBasicparameter;
    }

    public void setqBasicparameter(Short qBasicparameter) {
        this.qBasicparameter = qBasicparameter;
    }

    public Short getQ07004() {
        return q07004;
    }

    public void setQ07004(Short q07004) {
        this.q07004 = q07004;
    }

    public Short getQ12001() {
        return q12001;
    }

    public void setQ12001(Short q12001) {
        this.q12001 = q12001;
    }

    public BigDecimal getQ12003() {
        return q12003;
    }

    public void setQ12003(BigDecimal q12003) {
        this.q12003 = q12003;
    }
}