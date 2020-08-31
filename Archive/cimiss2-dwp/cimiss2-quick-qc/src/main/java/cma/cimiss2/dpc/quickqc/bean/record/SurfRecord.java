package cma.cimiss2.dpc.quickqc.bean.record;

import cma.cimiss2.dpc.quickqc.bean.BaseStationInfo;
import cma.cimiss2.dpc.quickqc.bean.Ccitt;
import cma.cimiss2.dpc.quickqc.bean.record.surf.*;

/**
 * @description: 地面数据(包括小时和分钟)
 * @author: When6passBye
 * @create: 2019-07-23 17:40
 **/
public class SurfRecord extends BaseStationInfo {

    private String method; /*观测方式, 当器测项目为人工观测时存入1，器测项目为自动站观测时存入4*/
    private String qcFlag; /*质量控制标识, 1-自动控制，0-人工交互控制，9-无控制*/

    /* 小时数据字段定义 */
    /*PP*/
    private AwsP p;

    /*TH*/
    private AwsTUE tue;
    /*RE*/

    private AwsR r;
    /*L*/
    private AwsL l;

    private double timePeriodL24;

    private double l24; /* 日蒸发量 */

    /*WI*/
    private AwsDf df;

    /*Ds*/
    private AwsDt dt;

    /*草温*/
    private AwsTg tg;

    /*路面温度*/
    private AwsRt rt;

    /*VV*/
    private AwsV v;

    /*CW*/
    private AwsCloud cloud;
    /*地面状态*/

    private AwsGroundState gs;
    /*SP*/
    private AwsSnowDepth snow;
    /*冻土深度*/
    private AwsSolidDepth sd;
    /*电线积冰*/
    private AwsWrieIce wire;
    /*路面状况*/
    private AwsRoadState rs;
    /*其他重要天气现象*/
    private AwsPhe phe;
    private Ccitt conPphe;/*小时连续天气现象*/
    /*日照*/
    private AwsSs ss;


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getQcFlag() {
        return qcFlag;
    }

    public void setQcFlag(String qcFlag) {
        this.qcFlag = qcFlag;
    }

    public AwsP getP() {
        return p;
    }

    public void setP(AwsP p) {
        this.p = p;
    }

    public AwsTUE getTue() {
        return tue;
    }

    public void setTue(AwsTUE tue) {
        this.tue = tue;
    }

    public AwsR getR() {
        return r;
    }

    public void setR(AwsR r) {
        this.r = r;
    }

    public AwsL getL() {
        return l;
    }

    public void setL(AwsL l) {
        this.l = l;
    }

    public double getTimePeriodL24() {
        return timePeriodL24;
    }

    public void setTimePeriodL24(double timePeriodL24) {
        this.timePeriodL24 = timePeriodL24;
    }

    public double getL24() {
        return l24;
    }

    public void setL24(double l24) {
        this.l24 = l24;
    }

    public AwsDf getDf() {
        return df;
    }

    public void setDf(AwsDf df) {
        this.df = df;
    }

    public Ccitt getConPphe() {
        return conPphe;
    }

    public void setConPphe(Ccitt conPphe) {
        this.conPphe = conPphe;
    }

    public AwsDt getDt() {
        return dt;
    }

    public void setDt(AwsDt dt) {
        this.dt = dt;
    }

    public AwsTg getTg() {
        return tg;
    }

    public void setTg(AwsTg tg) {
        this.tg = tg;
    }

    public AwsRt getRt() {
        return rt;
    }

    public void setRt(AwsRt rt) {
        this.rt = rt;
    }

    public AwsV getV() {
        return v;
    }

    public void setV(AwsV v) {
        this.v = v;
    }

    public AwsCloud getCloud() {
        return cloud;
    }

    public void setCloud(AwsCloud cloud) {
        this.cloud = cloud;
    }

    public AwsGroundState getGs() {
        return gs;
    }

    public void setGs(AwsGroundState gs) {
        this.gs = gs;
    }

    public AwsSnowDepth getSnow() {
        return snow;
    }

    public void setSnow(AwsSnowDepth snow) {
        this.snow = snow;
    }

    public AwsSolidDepth getSd() {
        return sd;
    }

    public void setSd(AwsSolidDepth sd) {
        this.sd = sd;
    }

    public AwsWrieIce getWire() {
        return wire;
    }

    public void setWire(AwsWrieIce wire) {
        this.wire = wire;
    }

    public AwsRoadState getRs() {
        return rs;
    }

    public void setRs(AwsRoadState rs) {
        this.rs = rs;
    }

    public AwsPhe getPhe() {
        return phe;
    }

    public void setPhe(AwsPhe phe) {
        this.phe = phe;
    }


    public AwsSs getSs() {
        return ss;
    }

    public void setSs(AwsSs ss) {
        this.ss = ss;
    }
}
