package cma.cimiss2.dpc.decoder.bean.agme;

import java.util.Date;

public class SandMove {

    private Double dRecordId;//记录标识

    private String dDataId;//资料标识

    private Date dIymdhm;//入库时间

    private Date dRymdhm;//收到时间

    private Date dUpdateTime;//监测时间

    private Date dDatetime;//资料时间

    private String v01301;//区站号(字符)

    private Double v01300;//区站号(数字)

    private String v00201;//观测序列描述

    private String v05001;//沙丘纬度

    private String v06001;//沙丘经度

    private Double v07001;//沙丘海拔高度

    private Double a07002;//沙丘高度变化（m）

    private Double v04001;//资料测定年

    private Double v04002;//资料测定月

    private Double v04003;//资料测定日

    private String a00300;//沙丘迎风坡脚移动方向

    private Double a00301;//沙丘迎风坡脚移动距离(m)

    private String a00302;//沙丘丘顶移动方向

    private Double a00303;//沙丘丘顶移动距离（m）

    private String a00304;//沙丘背风坡脚移动方向

    private Double a00305;//沙丘背风坡脚移动距离(m)

    private String a00306;//沙丘迎风坡脚方位角（°）

    private String a00307;//沙丘迎风坡脚最多方位

    private String a00308;//沙丘丘顶方位角（°）

    private String a00309;//沙丘丘顶最多方位

    private String a00310;//沙丘背风坡脚方位角（°）

    private String a00311;//沙丘背风坡脚最多方位

    private String a00312;//风向

    private String a00313;//主风向

    private Double a00314;//风速（米/秒）

    private String a00007;//观测员

    private String a00008;//校对员

    private String a00009;//备注

    public Double getdRecordId() {
        return dRecordId;
    }

    public void setdRecordId(Double dRecordId) {
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

    public String getV00201() {
        return v00201;
    }

    public void setV00201(String v00201) {
        this.v00201 = v00201 == null ? null : v00201.trim();
    }

    public String getV05001() {
        return v05001;
    }

    public void setV05001(String v05001) {
        this.v05001 = v05001;
    }

    public String getV06001() {
        return v06001;
    }

    public void setV06001(String v06001) {
        this.v06001 = v06001;
    }

    public Double getV07001() {
        return v07001;
    }

    public void setV07001(Double v07001) {
        this.v07001 = v07001;
    }

    public Double getA07002() {
        return a07002;
    }

    public void setA07002(Double a07002) {
        this.a07002 = a07002;
    }

    public Double getV04001() {
        return v04001;
    }

    public void setV04001(Double v04001) {
        this.v04001 = v04001;
    }

    public Double getV04002() {
        return v04002;
    }

    public void setV04002(Double v04002) {
        this.v04002 = v04002;
    }

    public Double getV04003() {
        return v04003;
    }

    public void setV04003(Double v04003) {
        this.v04003 = v04003;
    }

    public String getA00300() {
        return a00300;
    }

    public void setA00300(String a00300) {
        this.a00300 = a00300 == null ? null : a00300.trim();
    }

    public Double getA00301() {
        return a00301;
    }

    public void setA00301(Double a00301) {
        this.a00301 = a00301;
    }

    public String getA00302() {
        return a00302;
    }

    public void setA00302(String a00302) {
        this.a00302 = a00302 == null ? null : a00302.trim();
    }

    public Double getA00303() {
        return a00303;
    }

    public void setA00303(Double a00303) {
        this.a00303 = a00303;
    }

    public String getA00304() {
        return a00304;
    }

    public void setA00304(String a00304) {
        this.a00304 = a00304 == null ? null : a00304.trim();
    }

    public Double getA00305() {
        return a00305;
    }

    public void setA00305(Double a00305) {
        this.a00305 = a00305;
    }

    public String getA00306() {
        return a00306;
    }

    public void setA00306(String a00306) {
        this.a00306 = a00306;
    }

    public String getA00307() {
        return a00307;
    }

    public void setA00307(String a00307) {
        this.a00307 = a00307 == null ? null : a00307.trim();
    }

    public String getA00308() {
        return a00308;
    }

    public void setA00308(String a00308) {
        this.a00308 = a00308;
    }

    public String getA00309() {
        return a00309;
    }

    public void setA00309(String a00309) {
        this.a00309 = a00309 == null ? null : a00309.trim();
    }

    public String getA00310() {
        return a00310;
    }

    public void setA00310(String a00310) {
        this.a00310 = a00310;
    }

    public String getA00311() {
        return a00311;
    }

    public void setA00311(String a00311) {
        this.a00311 = a00311 == null ? null : a00311.trim();
    }

    public String getA00312() {
        return a00312;
    }

    public void setA00312(String a00312) {
        this.a00312 = a00312 == null ? null : a00312.trim();
    }

    public String getA00313() {
        return a00313;
    }

    public void setA00313(String a00313) {
        this.a00313 = a00313 == null ? null : a00313.trim();
    }

    public Double getA00314() {
        return a00314;
    }

    public void setA00314(Double a00314) {
        this.a00314 = a00314;
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
