package cma.cimiss2.dpc.decoder.bean.agme;

import java.math.BigDecimal;
import java.util.Date;

public class SandWind {

    private BigDecimal dRecordId;//记录标识

    private String dDataId;//资料标识

    private Date dIymdhm;//入库时间

    private Date dRymdhm;//收到时间

    private Date dUpdateTime;//监测时间

    private Date dDatetime;//资料时间

    private String v01301;//区站号(字符)

    private Double v01300;//区站号(数字)

    private String a00201;//观测序列描述

    private Long v05001;//样点纬度

    private Long v06001;//样点经度

    private Long v07001;//测站海拔高度

    private Integer v04001;//资料测定年

    private Integer v04002;//资料测定月

    private Integer v04003;//资料测定日

    private Double a00401;//观测区域内平均风积厚度（cm）

    private Double a00402;//观测区域外平均风积厚度（cm）

    private Double a00403;//观测区域内平均风蚀厚度（cm）

    private Double a00404;//观测区域外平均风蚀厚度（cm）

    private String a00007;//观测员

    private String a00008;//校对员

    private String a00009;//备注

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
        this.dDataId = dDataId == null ? null : dDataId.trim();
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

    public String getV01301() {
        return v01301;
    }

    public void setV01301(String v01301) {
        this.v01301 = v01301 == null ? null : v01301.trim();
    }

    public Double getV01300() {
        return v01300;
    }

    public void setV01300(Double v01300) {
        this.v01300 = v01300;
    }

    public String getA00201() {
        return a00201;
    }

    public void setA00201(String a00201) {
        this.a00201 = a00201 == null ? null : a00201.trim();
    }

    public Long getV05001() {
        return v05001;
    }

    public void setV05001(Long v05001) {
        this.v05001 = v05001;
    }

    public Long getV06001() {
        return v06001;
    }

    public void setV06001(Long v06001) {
        this.v06001 = v06001;
    }

    public Long getV07001() {
        return v07001;
    }

    public void setV07001(Long v07001) {
        this.v07001 = v07001;
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

    public Double getA00401() {
        return a00401;
    }

    public void setA00401(Double a00401) {
        this.a00401 = a00401;
    }

    public Double getA00402() {
        return a00402;
    }

    public void setA00402(Double a00402) {
        this.a00402 = a00402;
    }

    public Double getA00403() {
        return a00403;
    }

    public void setA00403(Double a00403) {
        this.a00403 = a00403;
    }

    public Double getA00404() {
        return a00404;
    }

    public void setA00404(Double a00404) {
        this.a00404 = a00404;
    }

    public String getA00007() {
        return a00007;
    }

    public void setA00007(String a00007) {
        this.a00007 = a00007 == null ? null : a00007.trim();
    }

    public String getA00008() {
        return a00008;
    }

    public void setA00008(String a00008) {
        this.a00008 = a00008 == null ? null : a00008.trim();
    }

    public String getA00009() {
        return a00009;
    }

    public void setA00009(String a00009) {
        this.a00009 = a00009 == null ? null : a00009.trim();
    }
}
