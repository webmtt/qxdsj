package cma.cimiss2.dpc.decoder.bean.agme;

import java.math.BigDecimal;
import java.util.Date;

public class GressWeight {

    private BigDecimal dRecordId;//记录标识

    private String dDataId;//资料标识

    private Date dIymdhm;//入库时间

    private Date dRymdhm;//收到时间

    private String v01301;//区站号(字符)

    private Double v01300;//区站号(数字)

    private Date dUpdateTime;//更新时间

    private Date dDatetime;//监测时间

    private Integer v04001;//资料测定年

    private Integer v04002;//资料测定月

    private Integer v04003;//资料测定日

    private String a02500;//观测场内牧草鲜重

    private String a02501;//观测场内牧草干重

    private String a02502;//观测场内干鲜比

    private String a02503;//观测场外牧草鲜重

    private String a02504;//观测场外牧草干重

    private String a02505;//观测场外干鲜比

    private String a00007;//观测员

    private String a00008;//校对员

    private String a00009;//备注

    private String prefectureName;//盟市名称

    public BigDecimal getdRecordId() {
        return dRecordId;
    }

    public void setdRecordId(BigDecimal dRecordId) {
        this.dRecordId = dRecordId;
    }

    public String getdDataId() {
        return dDataId;
    }

    public void setdDataId(String dDataId) {
        this.dDataId = dDataId;
    }

    public Date getdIymdhm() {
        return dIymdhm;
    }

    public void setdIymdhm(Date dIymdhm) {
        this.dIymdhm = dIymdhm;
    }

    public Date getdRymdhm() {
        return dRymdhm;
    }

    public void setdRymdhm(Date dRymdhm) {
        this.dRymdhm = dRymdhm;
    }

    public String getV01301() {
        return v01301;
    }

    public void setV01301(String v01301) {
        this.v01301 = v01301;
    }

    public Double getV01300() {
        return v01300;
    }

    public void setV01300(Double v01300) {
        this.v01300 = v01300;
    }

    public Date getdUpdateTime() {
        return dUpdateTime;
    }

    public void setdUpdateTime(Date dUpdateTime) {
        this.dUpdateTime = dUpdateTime;
    }

    public Date getdDatetime() {
        return dDatetime;
    }

    public void setdDatetime(Date dDatetime) {
        this.dDatetime = dDatetime;
    }

    public Integer getV04001() {
        return v04001;
    }

    public void setV04001(Integer v04001) {
        this.v04001 = v04001;
    }

    public Integer getV04002() {
        return v04002;
    }

    public void setV04002(Integer v04002) {
        this.v04002 = v04002;
    }

    public Integer getV04003() {
        return v04003;
    }

    public void setV04003(Integer v04003) {
        this.v04003 = v04003;
    }

    public String getA02500() {
        return a02500;
    }

    public void setA02500(String a02500) {
        this.a02500 = a02500;
    }

    public String getA02501() {
        return a02501;
    }

    public void setA02501(String a02501) {
        this.a02501 = a02501;
    }

    public String getA02502() {
        return a02502;
    }

    public void setA02502(String a02502) {
        this.a02502 = a02502;
    }

    public String getA02503() {
        return a02503;
    }

    public void setA02503(String a02503) {
        this.a02503 = a02503;
    }

    public String getA02504() {
        return a02504;
    }

    public void setA02504(String a02504) {
        this.a02504 = a02504;
    }

    public String getA02505() {
        return a02505;
    }

    public void setA02505(String a02505) {
        this.a02505 = a02505;
    }

    public String getA00007() {
        return a00007;
    }

    public void setA00007(String a00007) {
        this.a00007 = a00007;
    }

    public String getA00008() {
        return a00008;
    }

    public void setA00008(String a00008) {
        this.a00008 = a00008;
    }

    public String getA00009() {
        return a00009;
    }

    public void setA00009(String a00009) {
        this.a00009 = a00009;
    }

    public String getPrefectureName() {
        return prefectureName;
    }

    public void setPrefectureName(String prefectureName) {
        this.prefectureName = prefectureName;
    }
}
