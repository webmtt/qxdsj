package cma.cimiss2.dpc.decoder.bean.radi;

import java.math.BigDecimal;

public class SurfXiliTurbTab {
    private String dRecordId;//记录标识

    private String v01301;//区站号

    private Short v04001;//年

    private Short v04002;//月

    private Short v04003;//日

    private Short v04004;//时

    private BigDecimal v06001;//铁塔所在位置经度

    private BigDecimal v05001;//铁塔所在位置纬度

    private BigDecimal v07001;//梯度塔所处地（湖、海）面拔海高度

    private BigDecimal xV11202;//水平风速（x轴）

    private BigDecimal yV11202;//水平风速（y轴）

    private BigDecimal zV11202;//垂向风速（z轴）

    private BigDecimal co2Density;//二氧化碳绝对密度

    private BigDecimal vaporDensity;//水蒸汽绝对密度

    private BigDecimal vtou;//超声虚温

    private BigDecimal flutrm;//脉动温度

    private BigDecimal v10004;//本站气压

    private BigDecimal vtouWindtem;//超声风温仪传感器诊断值

    private BigDecimal redH2oco2Vue;//红外H20/CO2分析仪诊断值

    private BigDecimal redH2oco2Agc;//红外H20/CO2分析仪AGC值

    private BigDecimal heithThreeWind;//三维超声风温仪距地（湖、海）面高度

    private BigDecimal angleThreeWind;//三维超声风温仪安装角度

    private BigDecimal heithRedH2oco2;//红外H20/CO2分析仪距地（湖、海）面高度

    private String v04300017;//年月日时分（北京时）

    private BigDecimal co2Wpl;//经过WPL变换的二氧化碳通量

    private BigDecimal lhfWpl;//经过WPL变换的潜热通量

    private BigDecimal vtouShf;//用超声虚温计算得到的显热通量

    private BigDecimal momflux;//动量通量

    private BigDecimal friV11202;//摩擦风速

    private BigDecimal nonco2Wpl;//未经过WPL修正的二氧化碳通量

    private BigDecimal nonlhfWpl;//未经过WPL修正的潜热通量

    private BigDecimal moiCo2lhfWpl;//二氧化碳通量WPL变换的潜热修正项

    private BigDecimal moiCo2shfWpl;//二氧化碳通量WPL变换的显热修正项

    private BigDecimal moiLhf;//潜热通量WPL变换的潜热修正项

    private BigDecimal moiShf;//潜热通量WPL变换的显热修正项

    private BigDecimal uzV11202Vari;//垂直风速Uz的方差

    private BigDecimal uzUxVari;//垂直风速Uz和水平风速Ux的协方差

    private BigDecimal uzUyVari;//垂直风速Uz和水平风速Uy的协方差

    private BigDecimal uzV11202Co2densityVari;//垂直风速和二氧化碳密度的协方差

    private BigDecimal uzV11202VaporVari;//垂直风速Uz和水蒸汽密度的协方差

    private BigDecimal uzV11202VtouVari;//垂直风速Uz和超声虚温的协方差

    private BigDecimal uxV11202Vari;//水平风速Ux的方差

    private BigDecimal uxUyVari;//水平风速Ux和Uy的协方差

    private BigDecimal uxV11202Co2densityVari;//水平风速Ux和二氧化碳密度的协方差

    private BigDecimal uxV11202VaporVari;//水平风速Ux和水蒸汽密度的协方差

    private BigDecimal uxV11202VtouVar;//水平风速Ux和超声虚温的协方差

    private BigDecimal uyV11202Vari;//水平风速Uy的方差

    private BigDecimal uyV11202Co2densityVari;//水平风速Uy和二氧化碳密度的协方差

    private BigDecimal uyV11202VaporVari;//水平风速Uy和水蒸汽密度的协方差

    private BigDecimal uyV11202VtouVari;//水平风速Uy和超声虚温的协方差

    private BigDecimal co2densityVari;//二氧化碳密度的方差

    private BigDecimal vaporDensityVari;//水蒸汽密度的方差

    private BigDecimal vtouVari;//超声虚温的方差

    private BigDecimal uxV11202;//水平风速Ux均值

    private BigDecimal uyV11202;//水平风速Uy均值

    private BigDecimal uzV11202;//垂直风速Uz均值

    private BigDecimal co2densityAvg;//二氧化碳密度均值

    private BigDecimal vaporDensityAvg;//水蒸汽密度均值

    private BigDecimal vtouAvg;//超声虚温均值

    private BigDecimal pohsAvg;//本站气压均值

    private BigDecimal airDensityAvg;//空气密度均值

    private BigDecimal waterVaporAvg;//由同高度上气温和湿度计算得到的水汽密度均值

    private BigDecimal airTemMeanAtmAvg;//由同高度上气温计算得到的空气温度均值

    private BigDecimal meanAirTrm;//由同高度上相对湿度计算得到的空气相对湿度均值

    private BigDecimal aveVapPressure;//由同高度上气温和湿度计算得到的水汽压均值

    private BigDecimal v11201Xavg;//平均水平风速

    private BigDecimal vchV11201;//矢量合成水平风速

    private BigDecimal compassV11201;//罗盘坐标系下的风向方位角

    private BigDecimal sdsWind;//合成风向的标准偏差

    private BigDecimal uwsV11201;//超声风坐标系下的风向角度

    private BigDecimal numSamples;//协方差计算中有效样本总数

    private BigDecimal uwsWarningnum;//超声风传感器警告的总次数

    private BigDecimal analyzerH2oco2Warningnum;//H2O/CO2分析仪警告的总次数

    private BigDecimal uwsTemWarningnum;//超声风传感器虚温温度差警告总次数

    private BigDecimal uwsLockWarningnum;//超声风传感器信号锁定警告总次数

    private BigDecimal uwsLWarningnum;//超声风传感器信号放大高警告总次数

    private BigDecimal uwsHWarningnum;//超声风传感器信号放大低警告总次数

    private BigDecimal analyzerH2oco2Brnum;//H2O/CO2分析仪断路器警告总次数

    private BigDecimal analyzerH2oco2Testwarningnum;//H2O/CO2分析仪检测器警告总次数

    private BigDecimal analyzerPlcH2oco2;//H2O/CO2分析仪相位锁定循环

    private BigDecimal analyzerH2oco2Syncnum;//H2O/CO2分析仪同步警告总次数

    private BigDecimal analyzerH2oco2Avg;//H2O/CO2分析仪AGC均值

    private BigDecimal v02262Avg;//电池电压均值

    private BigDecimal v02264Avg;//面板温度均值

    private BigDecimal v07031;//气压传感器拔海高度

    private String v02320;//采集器型号标识

    private BigDecimal version;//版本号

    private BigDecimal threeWindMode;//三维超声风温仪型号

    private BigDecimal analyzerModelH20co2;//红外H20/CO2分析仪型号

    private Short q48008;//地表植被状况编码

    private BigDecimal heighVegetation;//植被高度

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

    public Short getV04004() {
        return v04004;
    }

    public void setV04004(Short v04004) {
        this.v04004 = v04004;
    }

    public BigDecimal getV06001() {
        return v06001;
    }

    public void setV06001(BigDecimal v06001) {
        this.v06001 = v06001;
    }

    public BigDecimal getV05001() {
        return v05001;
    }

    public void setV05001(BigDecimal v05001) {
        this.v05001 = v05001;
    }

    public BigDecimal getV07001() {
        return v07001;
    }

    public void setV07001(BigDecimal v07001) {
        this.v07001 = v07001;
    }

    public BigDecimal getxV11202() {
        return xV11202;
    }

    public void setxV11202(BigDecimal xV11202) {
        this.xV11202 = xV11202;
    }

    public BigDecimal getyV11202() {
        return yV11202;
    }

    public void setyV11202(BigDecimal yV11202) {
        this.yV11202 = yV11202;
    }

    public BigDecimal getzV11202() {
        return zV11202;
    }

    public void setzV11202(BigDecimal zV11202) {
        this.zV11202 = zV11202;
    }

    public BigDecimal getCo2Density() {
        return co2Density;
    }

    public void setCo2Density(BigDecimal co2Density) {
        this.co2Density = co2Density;
    }

    public BigDecimal getVaporDensity() {
        return vaporDensity;
    }

    public void setVaporDensity(BigDecimal vaporDensity) {
        this.vaporDensity = vaporDensity;
    }

    public BigDecimal getVtou() {
        return vtou;
    }

    public void setVtou(BigDecimal vtou) {
        this.vtou = vtou;
    }

    public BigDecimal getFlutrm() {
        return flutrm;
    }

    public void setFlutrm(BigDecimal flutrm) {
        this.flutrm = flutrm;
    }

    public BigDecimal getV10004() {
        return v10004;
    }

    public void setV10004(BigDecimal v10004) {
        this.v10004 = v10004;
    }

    public BigDecimal getVtouWindtem() {
        return vtouWindtem;
    }

    public void setVtouWindtem(BigDecimal vtouWindtem) {
        this.vtouWindtem = vtouWindtem;
    }

    public BigDecimal getRedH2oco2Vue() {
        return redH2oco2Vue;
    }

    public void setRedH2oco2Vue(BigDecimal redH2oco2Vue) {
        this.redH2oco2Vue = redH2oco2Vue;
    }

    public BigDecimal getRedH2oco2Agc() {
        return redH2oco2Agc;
    }

    public void setRedH2oco2Agc(BigDecimal redH2oco2Agc) {
        this.redH2oco2Agc = redH2oco2Agc;
    }

    public BigDecimal getHeithThreeWind() {
        return heithThreeWind;
    }

    public void setHeithThreeWind(BigDecimal heithThreeWind) {
        this.heithThreeWind = heithThreeWind;
    }

    public BigDecimal getAngleThreeWind() {
        return angleThreeWind;
    }

    public void setAngleThreeWind(BigDecimal angleThreeWind) {
        this.angleThreeWind = angleThreeWind;
    }

    public BigDecimal getHeithRedH2oco2() {
        return heithRedH2oco2;
    }

    public void setHeithRedH2oco2(BigDecimal heithRedH2oco2) {
        this.heithRedH2oco2 = heithRedH2oco2;
    }

    public String getV04300017() {
        return v04300017;
    }

    public void setV04300017(String v04300017) {
        this.v04300017 = v04300017;
    }

    public BigDecimal getCo2Wpl() {
        return co2Wpl;
    }

    public void setCo2Wpl(BigDecimal co2Wpl) {
        this.co2Wpl = co2Wpl;
    }

    public BigDecimal getLhfWpl() {
        return lhfWpl;
    }

    public void setLhfWpl(BigDecimal lhfWpl) {
        this.lhfWpl = lhfWpl;
    }

    public BigDecimal getVtouShf() {
        return vtouShf;
    }

    public void setVtouShf(BigDecimal vtouShf) {
        this.vtouShf = vtouShf;
    }

    public BigDecimal getMomflux() {
        return momflux;
    }

    public void setMomflux(BigDecimal momflux) {
        this.momflux = momflux;
    }

    public BigDecimal getFriV11202() {
        return friV11202;
    }

    public void setFriV11202(BigDecimal friV11202) {
        this.friV11202 = friV11202;
    }

    public BigDecimal getNonco2Wpl() {
        return nonco2Wpl;
    }

    public void setNonco2Wpl(BigDecimal nonco2Wpl) {
        this.nonco2Wpl = nonco2Wpl;
    }

    public BigDecimal getNonlhfWpl() {
        return nonlhfWpl;
    }

    public void setNonlhfWpl(BigDecimal nonlhfWpl) {
        this.nonlhfWpl = nonlhfWpl;
    }

    public BigDecimal getMoiCo2lhfWpl() {
        return moiCo2lhfWpl;
    }

    public void setMoiCo2lhfWpl(BigDecimal moiCo2lhfWpl) {
        this.moiCo2lhfWpl = moiCo2lhfWpl;
    }

    public BigDecimal getMoiCo2shfWpl() {
        return moiCo2shfWpl;
    }

    public void setMoiCo2shfWpl(BigDecimal moiCo2shfWpl) {
        this.moiCo2shfWpl = moiCo2shfWpl;
    }

    public BigDecimal getMoiLhf() {
        return moiLhf;
    }

    public void setMoiLhf(BigDecimal moiLhf) {
        this.moiLhf = moiLhf;
    }

    public BigDecimal getMoiShf() {
        return moiShf;
    }

    public void setMoiShf(BigDecimal moiShf) {
        this.moiShf = moiShf;
    }

    public BigDecimal getUzV11202Vari() {
        return uzV11202Vari;
    }

    public void setUzV11202Vari(BigDecimal uzV11202Vari) {
        this.uzV11202Vari = uzV11202Vari;
    }

    public BigDecimal getUzUxVari() {
        return uzUxVari;
    }

    public void setUzUxVari(BigDecimal uzUxVari) {
        this.uzUxVari = uzUxVari;
    }

    public BigDecimal getUzUyVari() {
        return uzUyVari;
    }

    public void setUzUyVari(BigDecimal uzUyVari) {
        this.uzUyVari = uzUyVari;
    }

    public BigDecimal getUzV11202Co2densityVari() {
        return uzV11202Co2densityVari;
    }

    public void setUzV11202Co2densityVari(BigDecimal uzV11202Co2densityVari) {
        this.uzV11202Co2densityVari = uzV11202Co2densityVari;
    }

    public BigDecimal getUzV11202VaporVari() {
        return uzV11202VaporVari;
    }

    public void setUzV11202VaporVari(BigDecimal uzV11202VaporVari) {
        this.uzV11202VaporVari = uzV11202VaporVari;
    }

    public BigDecimal getUzV11202VtouVari() {
        return uzV11202VtouVari;
    }

    public void setUzV11202VtouVari(BigDecimal uzV11202VtouVari) {
        this.uzV11202VtouVari = uzV11202VtouVari;
    }

    public BigDecimal getUxV11202Vari() {
        return uxV11202Vari;
    }

    public void setUxV11202Vari(BigDecimal uxV11202Vari) {
        this.uxV11202Vari = uxV11202Vari;
    }

    public BigDecimal getUxUyVari() {
        return uxUyVari;
    }

    public void setUxUyVari(BigDecimal uxUyVari) {
        this.uxUyVari = uxUyVari;
    }

    public BigDecimal getUxV11202Co2densityVari() {
        return uxV11202Co2densityVari;
    }

    public void setUxV11202Co2densityVari(BigDecimal uxV11202Co2densityVari) {
        this.uxV11202Co2densityVari = uxV11202Co2densityVari;
    }

    public BigDecimal getUxV11202VaporVari() {
        return uxV11202VaporVari;
    }

    public void setUxV11202VaporVari(BigDecimal uxV11202VaporVari) {
        this.uxV11202VaporVari = uxV11202VaporVari;
    }

    public BigDecimal getUxV11202VtouVar() {
        return uxV11202VtouVar;
    }

    public void setUxV11202VtouVar(BigDecimal uxV11202VtouVar) {
        this.uxV11202VtouVar = uxV11202VtouVar;
    }

    public BigDecimal getUyV11202Vari() {
        return uyV11202Vari;
    }

    public void setUyV11202Vari(BigDecimal uyV11202Vari) {
        this.uyV11202Vari = uyV11202Vari;
    }

    public BigDecimal getUyV11202Co2densityVari() {
        return uyV11202Co2densityVari;
    }

    public void setUyV11202Co2densityVari(BigDecimal uyV11202Co2densityVari) {
        this.uyV11202Co2densityVari = uyV11202Co2densityVari;
    }

    public BigDecimal getUyV11202VaporVari() {
        return uyV11202VaporVari;
    }

    public void setUyV11202VaporVari(BigDecimal uyV11202VaporVari) {
        this.uyV11202VaporVari = uyV11202VaporVari;
    }

    public BigDecimal getUyV11202VtouVari() {
        return uyV11202VtouVari;
    }

    public void setUyV11202VtouVari(BigDecimal uyV11202VtouVari) {
        this.uyV11202VtouVari = uyV11202VtouVari;
    }

    public BigDecimal getCo2densityVari() {
        return co2densityVari;
    }

    public void setCo2densityVari(BigDecimal co2densityVari) {
        this.co2densityVari = co2densityVari;
    }

    public BigDecimal getVaporDensityVari() {
        return vaporDensityVari;
    }

    public void setVaporDensityVari(BigDecimal vaporDensityVari) {
        this.vaporDensityVari = vaporDensityVari;
    }

    public BigDecimal getVtouVari() {
        return vtouVari;
    }

    public void setVtouVari(BigDecimal vtouVari) {
        this.vtouVari = vtouVari;
    }

    public BigDecimal getUxV11202() {
        return uxV11202;
    }

    public void setUxV11202(BigDecimal uxV11202) {
        this.uxV11202 = uxV11202;
    }

    public BigDecimal getUyV11202() {
        return uyV11202;
    }

    public void setUyV11202(BigDecimal uyV11202) {
        this.uyV11202 = uyV11202;
    }

    public BigDecimal getUzV11202() {
        return uzV11202;
    }

    public void setUzV11202(BigDecimal uzV11202) {
        this.uzV11202 = uzV11202;
    }

    public BigDecimal getCo2densityAvg() {
        return co2densityAvg;
    }

    public void setCo2densityAvg(BigDecimal co2densityAvg) {
        this.co2densityAvg = co2densityAvg;
    }

    public BigDecimal getVaporDensityAvg() {
        return vaporDensityAvg;
    }

    public void setVaporDensityAvg(BigDecimal vaporDensityAvg) {
        this.vaporDensityAvg = vaporDensityAvg;
    }

    public BigDecimal getVtouAvg() {
        return vtouAvg;
    }

    public void setVtouAvg(BigDecimal vtouAvg) {
        this.vtouAvg = vtouAvg;
    }

    public BigDecimal getPohsAvg() {
        return pohsAvg;
    }

    public void setPohsAvg(BigDecimal pohsAvg) {
        this.pohsAvg = pohsAvg;
    }

    public BigDecimal getAirDensityAvg() {
        return airDensityAvg;
    }

    public void setAirDensityAvg(BigDecimal airDensityAvg) {
        this.airDensityAvg = airDensityAvg;
    }

    public BigDecimal getWaterVaporAvg() {
        return waterVaporAvg;
    }

    public void setWaterVaporAvg(BigDecimal waterVaporAvg) {
        this.waterVaporAvg = waterVaporAvg;
    }

    public BigDecimal getAirTemMeanAtmAvg() {
        return airTemMeanAtmAvg;
    }

    public void setAirTemMeanAtmAvg(BigDecimal airTemMeanAtmAvg) {
        this.airTemMeanAtmAvg = airTemMeanAtmAvg;
    }

    public BigDecimal getMeanAirTrm() {
        return meanAirTrm;
    }

    public void setMeanAirTrm(BigDecimal meanAirTrm) {
        this.meanAirTrm = meanAirTrm;
    }

    public BigDecimal getAveVapPressure() {
        return aveVapPressure;
    }

    public void setAveVapPressure(BigDecimal aveVapPressure) {
        this.aveVapPressure = aveVapPressure;
    }

    public BigDecimal getV11201Xavg() {
        return v11201Xavg;
    }

    public void setV11201Xavg(BigDecimal v11201Xavg) {
        this.v11201Xavg = v11201Xavg;
    }

    public BigDecimal getVchV11201() {
        return vchV11201;
    }

    public void setVchV11201(BigDecimal vchV11201) {
        this.vchV11201 = vchV11201;
    }

    public BigDecimal getCompassV11201() {
        return compassV11201;
    }

    public void setCompassV11201(BigDecimal compassV11201) {
        this.compassV11201 = compassV11201;
    }

    public BigDecimal getSdsWind() {
        return sdsWind;
    }

    public void setSdsWind(BigDecimal sdsWind) {
        this.sdsWind = sdsWind;
    }

    public BigDecimal getUwsV11201() {
        return uwsV11201;
    }

    public void setUwsV11201(BigDecimal uwsV11201) {
        this.uwsV11201 = uwsV11201;
    }

    public BigDecimal getNumSamples() {
        return numSamples;
    }

    public void setNumSamples(BigDecimal numSamples) {
        this.numSamples = numSamples;
    }

    public BigDecimal getUwsWarningnum() {
        return uwsWarningnum;
    }

    public void setUwsWarningnum(BigDecimal uwsWarningnum) {
        this.uwsWarningnum = uwsWarningnum;
    }

    public BigDecimal getAnalyzerH2oco2Warningnum() {
        return analyzerH2oco2Warningnum;
    }

    public void setAnalyzerH2oco2Warningnum(BigDecimal analyzerH2oco2Warningnum) {
        this.analyzerH2oco2Warningnum = analyzerH2oco2Warningnum;
    }

    public BigDecimal getUwsTemWarningnum() {
        return uwsTemWarningnum;
    }

    public void setUwsTemWarningnum(BigDecimal uwsTemWarningnum) {
        this.uwsTemWarningnum = uwsTemWarningnum;
    }

    public BigDecimal getUwsLockWarningnum() {
        return uwsLockWarningnum;
    }

    public void setUwsLockWarningnum(BigDecimal uwsLockWarningnum) {
        this.uwsLockWarningnum = uwsLockWarningnum;
    }

    public BigDecimal getUwsLWarningnum() {
        return uwsLWarningnum;
    }

    public void setUwsLWarningnum(BigDecimal uwsLWarningnum) {
        this.uwsLWarningnum = uwsLWarningnum;
    }

    public BigDecimal getUwsHWarningnum() {
        return uwsHWarningnum;
    }

    public void setUwsHWarningnum(BigDecimal uwsHWarningnum) {
        this.uwsHWarningnum = uwsHWarningnum;
    }

    public BigDecimal getAnalyzerH2oco2Brnum() {
        return analyzerH2oco2Brnum;
    }

    public void setAnalyzerH2oco2Brnum(BigDecimal analyzerH2oco2Brnum) {
        this.analyzerH2oco2Brnum = analyzerH2oco2Brnum;
    }

    public BigDecimal getAnalyzerH2oco2Testwarningnum() {
        return analyzerH2oco2Testwarningnum;
    }

    public void setAnalyzerH2oco2Testwarningnum(BigDecimal analyzerH2oco2Testwarningnum) {
        this.analyzerH2oco2Testwarningnum = analyzerH2oco2Testwarningnum;
    }

    public BigDecimal getAnalyzerPlcH2oco2() {
        return analyzerPlcH2oco2;
    }

    public void setAnalyzerPlcH2oco2(BigDecimal analyzerPlcH2oco2) {
        this.analyzerPlcH2oco2 = analyzerPlcH2oco2;
    }

    public BigDecimal getAnalyzerH2oco2Syncnum() {
        return analyzerH2oco2Syncnum;
    }

    public void setAnalyzerH2oco2Syncnum(BigDecimal analyzerH2oco2Syncnum) {
        this.analyzerH2oco2Syncnum = analyzerH2oco2Syncnum;
    }

    public BigDecimal getAnalyzerH2oco2Avg() {
        return analyzerH2oco2Avg;
    }

    public void setAnalyzerH2oco2Avg(BigDecimal analyzerH2oco2Avg) {
        this.analyzerH2oco2Avg = analyzerH2oco2Avg;
    }

    public BigDecimal getV02262Avg() {
        return v02262Avg;
    }

    public void setV02262Avg(BigDecimal v02262Avg) {
        this.v02262Avg = v02262Avg;
    }

    public BigDecimal getV02264Avg() {
        return v02264Avg;
    }

    public void setV02264Avg(BigDecimal v02264Avg) {
        this.v02264Avg = v02264Avg;
    }

    public BigDecimal getV07031() {
        return v07031;
    }

    public void setV07031(BigDecimal v07031) {
        this.v07031 = v07031;
    }

    public String getV02320() {
        return v02320;
    }

    public void setV02320(String v02320) {
        this.v02320 = v02320 == null ? null : v02320.trim();
    }

    public BigDecimal getVersion() {
        return version;
    }

    public void setVersion(BigDecimal version) {
        this.version = version;
    }

    public BigDecimal getThreeWindMode() {
        return threeWindMode;
    }

    public void setThreeWindMode(BigDecimal threeWindMode) {
        this.threeWindMode = threeWindMode;
    }

    public BigDecimal getAnalyzerModelH20co2() {
        return analyzerModelH20co2;
    }

    public void setAnalyzerModelH20co2(BigDecimal analyzerModelH20co2) {
        this.analyzerModelH20co2 = analyzerModelH20co2;
    }

    public Short getQ48008() {
        return q48008;
    }

    public void setQ48008(Short q48008) {
        this.q48008 = q48008;
    }

    public BigDecimal getHeighVegetation() {
        return heighVegetation;
    }

    public void setHeighVegetation(BigDecimal heighVegetation) {
        this.heighVegetation = heighVegetation;
    }
}