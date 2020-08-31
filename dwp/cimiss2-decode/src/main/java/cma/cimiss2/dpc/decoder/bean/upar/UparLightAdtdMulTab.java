package cma.cimiss2.dpc.decoder.bean.upar;

import java.math.BigDecimal;
import java.sql.Timestamp;


public class UparLightAdtdMulTab {
    private String dRecordId;//记录标识

    private String dSourceId;//数据来源

    private String dDataId;//资料标识

    private String dIymdhm;//入库时间

    private String dRymdhm;//收到时间

    private String dUpdateTime;//更新时间

    private String dDatetime;//资料时间

    private BigDecimal v05001;//纬度

    private BigDecimal v06001;//经度

    private Short v04001;//年

    private Short v04002;//月

    private Short v04003;//日

    private Short v04004;//时

    private Short v04005;//分

    private Short v04006;//秒

    private Integer v04007;//毫秒

    private Integer v08300;//当日闪电个数序号

    private BigDecimal v73011;//定位误差

    private BigDecimal v73016;//电流（回击峰值）强度

    private BigDecimal v73023;//回击最大陡度

    private String v73110;//定位方式

    private String v010151;//雷电地理位置信息省

    private String v010152;//雷电地理位置信息市

    private String v010153;//雷电地理位置信息县

    private String vBbb;//更正报标志

    public String getdRecordId() {
        return dRecordId;
    }

    public void setdRecordId(String dRecordId) {
        this.dRecordId = dRecordId == null ? null : dRecordId.trim();
    }

    public String getdSourceId() {
        return dSourceId;
    }

    public void setdSourceId(String dSourceId) {
        this.dSourceId = dSourceId == null ? null : dSourceId.trim();
    }

    public String getdDataId() {
        return dDataId;
    }

    public void setdDataId(String dDataId) {
        this.dDataId = dDataId == null ? null : dDataId.trim();
    }

    public String getdIymdhm() {
        return dIymdhm;
    }

    public void setdIymdhm(String dIymdhm) {
        this.dIymdhm = dIymdhm;
    }

    public String getdRymdhm() {
        return dRymdhm;
    }

    public void setdRymdhm(String dRymdhm) {
        this.dRymdhm = dRymdhm;
    }

    public String getdUpdateTime() {
        return dUpdateTime;
    }

    public void setdUpdateTime(String dUpdateTime) {
        this.dUpdateTime = dUpdateTime;
    }

    public String getdDatetime() {
        return dDatetime;
    }

    public void setdDatetime(String dDatetime) {
        this.dDatetime = dDatetime;
    }

    public BigDecimal getV05001() {
        return v05001;
    }

    public void setV05001(BigDecimal v05001) {
        this.v05001 = v05001;
    }

    public BigDecimal getV06001() {
        return v06001;
    }

    public void setV06001(BigDecimal v06001) {
        this.v06001 = v06001;
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

    public Short getV04006() {
        return v04006;
    }

    public void setV04006(Short v04006) {
        this.v04006 = v04006;
    }

    public Integer getV04007() {
        return v04007;
    }

    public void setV04007(Integer v04007) {
        this.v04007 = v04007;
    }

    public Integer getV08300() {
        return v08300;
    }

    public void setV08300(Integer v08300) {
        this.v08300 = v08300;
    }

    public BigDecimal getV73011() {
        return v73011;
    }

    public void setV73011(BigDecimal v73011) {
        this.v73011 = v73011;
    }

    public BigDecimal getV73016() {
        return v73016;
    }

    public void setV73016(BigDecimal v73016) {
        this.v73016 = v73016;
    }

    public BigDecimal getV73023() {
        return v73023;
    }

    public void setV73023(BigDecimal v73023) {
        this.v73023 = v73023;
    }

    public String getV73110() {
        return v73110;
    }

    public void setV73110(String v73110) {
        this.v73110 = v73110 == null ? null : v73110.trim();
    }

    public String getV010151() {
        return v010151;
    }

    public void setV010151(String v010151) {
        this.v010151 = v010151 == null ? null : v010151.trim();
    }

    public String getV010152() {
        return v010152;
    }

    public void setV010152(String v010152) {
        this.v010152 = v010152 == null ? null : v010152.trim();
    }

    public String getV010153() {
        return v010153;
    }

    public void setV010153(String v010153) {
        this.v010153 = v010153 == null ? null : v010153.trim();
    }

    public String getvBbb() {
        return vBbb;
    }

    public void setvBbb(String vBbb) {
        this.vBbb = vBbb == null ? null : vBbb.trim();
    }
}