package cma.cimiss2.dpc.decoder.bean.agme;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class NonsatMics {
    private String dRecordId;//记录标识

    private Short v04001;//资料观测年

    private Short v04002;//资料观测月

    private Short v04003;//资料观测日

    private Short v04004;//资料观测时

    private BigDecimal v12030000;//0cm地温

    private String v12030005;//5cm地温

    private BigDecimal v12030010;//10cm地温

    private BigDecimal v12030015;//15cm地温

    private BigDecimal co2;//二氧化碳浓度上层

    private BigDecimal v13011;//分钟雨量

    private BigDecimal parM;//光合有效辐射

    private BigDecimal parH;//光合有效辐射小时累计

    private BigDecimal light;//光强上层

    private BigDecimal airHM;//空气湿度中间

    private BigDecimal airTM;//空气温度中间

    private BigDecimal airTU;//空气温度中间上

    private BigDecimal airTD;//空气温度中间下

    private BigDecimal v11291;//两分钟风速

    private BigDecimal v11290;//两分钟风向

    private BigDecimal v10004;//平均气压

    private String dDatetime;//日期时间

    private String dRymdhm;//数据接收时间

    private BigDecimal v11002;//瞬时风速

    private BigDecimal v11001;//瞬时风向

    private String v01301;//台站号

    private BigDecimal surHU;//土壤湿度上层

    private BigDecimal surHD;//土壤湿度下层

    private BigDecimal surHM;//土壤湿度中层

    private BigDecimal v13003;//相对湿度

    private BigDecimal radi;//总辐射

    private BigDecimal radiMin;//总辐射分钟

    private BigDecimal radiHour;//总辐射小时累计

    private BigDecimal collectV;//采集器电压

    private BigDecimal collectT;//采集器温度

    private String minuteHM;//分钟小时标识

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

    public BigDecimal getV12030000() {
        return v12030000;
    }

    public void setV12030000(BigDecimal v12030000) {
        this.v12030000 = v12030000;
    }

    public String getV12030005() {
        return v12030005;
    }

    public void setV12030005(String v12030005) {
        this.v12030005 = v12030005 == null ? null : v12030005.trim();
    }

    public BigDecimal getV12030010() {
        return v12030010;
    }

    public void setV12030010(BigDecimal v12030010) {
        this.v12030010 = v12030010;
    }

    public BigDecimal getV12030015() {
        return v12030015;
    }

    public void setV12030015(BigDecimal v12030015) {
        this.v12030015 = v12030015;
    }

    public BigDecimal getCo2() {
        return co2;
    }

    public void setCo2(BigDecimal co2) {
        this.co2 = co2;
    }

    public BigDecimal getV13011() {
        return v13011;
    }

    public void setV13011(BigDecimal v13011) {
        this.v13011 = v13011;
    }

    public BigDecimal getParM() {
        return parM;
    }

    public void setParM(BigDecimal parM) {
        this.parM = parM;
    }

    public BigDecimal getParH() {
        return parH;
    }

    public void setParH(BigDecimal parH) {
        this.parH = parH;
    }

    public BigDecimal getLight() {
        return light;
    }

    public void setLight(BigDecimal light) {
        this.light = light;
    }

    public BigDecimal getAirHM() {
        return airHM;
    }

    public void setAirHM(BigDecimal airHM) {
        this.airHM = airHM;
    }

    public BigDecimal getAirTM() {
        return airTM;
    }

    public void setAirTM(BigDecimal airTM) {
        this.airTM = airTM;
    }

    public BigDecimal getAirTU() {
        return airTU;
    }

    public void setAirTU(BigDecimal airTU) {
        this.airTU = airTU;
    }

    public BigDecimal getAirTD() {
        return airTD;
    }

    public void setAirTD(BigDecimal airTD) {
        this.airTD = airTD;
    }

    public BigDecimal getV11291() {
        return v11291;
    }

    public void setV11291(BigDecimal v11291) {
        this.v11291 = v11291;
    }

    public BigDecimal getV11290() {
        return v11290;
    }

    public void setV11290(BigDecimal v11290) {
        this.v11290 = v11290;
    }

    public BigDecimal getV10004() {
        return v10004;
    }

    public void setV10004(BigDecimal v10004) {
        this.v10004 = v10004;
    }

    public String getdDatetime() {
        return dDatetime;
    }

    public void setdDatetime(String dDatetime) {
        this.dDatetime = dDatetime;
    }

    public String getdRymdhm() {
        return dRymdhm;
    }

    public void setdRymdhm(String dRymdhm) {
        this.dRymdhm = dRymdhm;
    }

    public BigDecimal getV11002() {
        return v11002;
    }

    public void setV11002(BigDecimal v11002) {
        this.v11002 = v11002;
    }

    public BigDecimal getV11001() {
        return v11001;
    }

    public void setV11001(BigDecimal v11001) {
        this.v11001 = v11001;
    }

    public String getV01301() {
        return v01301;
    }

    public void setV01301(String v01301) {
        this.v01301 = v01301 == null ? null : v01301.trim();
    }

    public BigDecimal getSurHU() {
        return surHU;
    }

    public void setSurHU(BigDecimal surHU) {
        this.surHU = surHU;
    }

    public BigDecimal getSurHD() {
        return surHD;
    }

    public void setSurHD(BigDecimal surHD) {
        this.surHD = surHD;
    }

    public BigDecimal getSurHM() {
        return surHM;
    }

    public void setSurHM(BigDecimal surHM) {
        this.surHM = surHM;
    }

    public BigDecimal getV13003() {
        return v13003;
    }

    public void setV13003(BigDecimal v13003) {
        this.v13003 = v13003;
    }

    public BigDecimal getRadi() {
        return radi;
    }

    public void setRadi(BigDecimal radi) {
        this.radi = radi;
    }

    public BigDecimal getRadiMin() {
        return radiMin;
    }

    public void setRadiMin(BigDecimal radiMin) {
        this.radiMin = radiMin;
    }

    public BigDecimal getRadiHour() {
        return radiHour;
    }

    public void setRadiHour(BigDecimal radiHour) {
        this.radiHour = radiHour;
    }

    public BigDecimal getCollectV() {
        return collectV;
    }

    public void setCollectV(BigDecimal collectV) {
        this.collectV = collectV == null ? null : collectV;
    }

    public BigDecimal getCollectT() {
        return collectT;
    }

    public void setCollectT(BigDecimal collectT) {
        this.collectT = collectT;
    }

    public String getMinuteHM() {
        return minuteHM;
    }

    public void setMinuteHM(String minuteHM) {
        this.minuteHM = minuteHM == null ? null : minuteHM.trim();
    }
}