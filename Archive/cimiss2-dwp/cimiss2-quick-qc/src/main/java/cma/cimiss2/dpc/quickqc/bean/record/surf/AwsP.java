package cma.cimiss2.dpc.quickqc.bean.record.surf;

import cma.cimiss2.dpc.quickqc.bean.Constants;
import cma.cimiss2.dpc.quickqc.bean.QCEle;

/**
 * @description: 气压
 * @author: When6passBye
 * @create: 2019-07-23 18:17
 **/

public class AwsP {

    QCEle<Double> p; /* 气压 */
    QCEle<Double> p0; /* 海平面气压 */
    QCEle<Double> p03; /* 3小时变压 */
    QCEle<Double> p_cha;/*气压倾向特征*/
    QCEle<Double> p24; /* 24小时变压 */
    QCEle<Double> p_sta; /* 标准层气压 */
    QCEle<Double> h;/*测站位势高度*/
    QCEle<Double> first_sta_pmax; /* 一级统计 */
    QCEle<Double> time_period_pmax;
    QCEle<Double> pmax; /* 小时最高气压 */
    QCEle<Double> time_hour_pmax;/*现象出现最大时分*/
    QCEle<Double> time_hour_pmin;;/*现象出现最小时分*/
    QCEle<Double> first_sta_def0; /* 一级统计 （缺省）*/
    QCEle<Double> first_sta_pmin; /* 一级统计 */
    QCEle<Double> time_period_pmin;/*时间周期*/
    QCEle<Double> pmin; /* 小时最低气压 */

    QCEle<Double> first_st_def1; /* 一级统计(缺省) */

    public AwsP(){
        this.p=new QCEle(Constants.NO_MEANS);
        this.p0=new QCEle(Constants.NO_MEANS);
        this.p03=new QCEle(Constants.NO_MEANS);
        this.p_cha=new QCEle(Constants.NO_MEANS);
        this.p24=new QCEle(Constants.NO_MEANS);
        this.first_sta_pmax=new QCEle(Constants.NO_MEANS);
        this.time_period_pmax=new QCEle(Constants.NO_MEANS);
        this.pmax=new QCEle(Constants.NO_MEANS);
        this.time_hour_pmax=new QCEle(Constants.NO_MEANS);
        this.first_sta_def0=new QCEle(Constants.NO_MEANS);
        this.first_sta_pmin=new QCEle(Constants.NO_MEANS);
        this.pmin=new QCEle(Constants.NO_MEANS);
        this.first_st_def1=new QCEle(Constants.NO_MEANS);

    }

    public QCEle<Double> getP() {
        return p;
    }

    public void setP(QCEle<Double> p) {
        this.p = p;
    }

    public QCEle<Double> getP0() {
        return p0;
    }

    public void setP0(QCEle<Double> p0) {
        this.p0 = p0;
    }

    public QCEle<Double> getP03() {
        return p03;
    }

    public void setP03(QCEle<Double> p03) {
        this.p03 = p03;
    }

    public QCEle<Double> getP_cha() {
        return p_cha;
    }

    public void setP_cha(QCEle<Double> p_cha) {
        this.p_cha = p_cha;
    }

    public QCEle<Double> getP24() {
        return p24;
    }

    public void setP24(QCEle<Double> p24) {
        this.p24 = p24;
    }

    public QCEle<Double> getP_sta() {
        return p_sta;
    }

    public void setP_sta(QCEle<Double> p_sta) {
        this.p_sta = p_sta;
    }

    public QCEle<Double> getH() {
        return h;
    }

    public void setH(QCEle<Double> h) {
        this.h = h;
    }

    public QCEle<Double> getFirst_sta_pmax() {
        return first_sta_pmax;
    }

    public void setFirst_sta_pmax(QCEle<Double> first_sta_pmax) {
        this.first_sta_pmax = first_sta_pmax;
    }

    public QCEle<Double> getTime_period_pmax() {
        return time_period_pmax;
    }

    public void setTime_period_pmax(QCEle<Double> time_period_pmax) {
        this.time_period_pmax = time_period_pmax;
    }

    public QCEle<Double> getPmax() {
        return pmax;
    }

    public void setPmax(QCEle<Double> pmax) {
        this.pmax = pmax;
    }

    public QCEle<Double> getTime_hour_pmax() {
        return time_hour_pmax;
    }

    public void setTime_hour_pmax(QCEle<Double> time_hour_pmax) {
        this.time_hour_pmax = time_hour_pmax;
    }

    public QCEle<Double> getTime_hour_pmin() {
        return time_hour_pmin;
    }

    public void setTime_hour_pmin(QCEle<Double> time_hour_pmin) {
        this.time_hour_pmin = time_hour_pmin;
    }

    public QCEle<Double> getFirst_sta_def0() {
        return first_sta_def0;
    }

    public void setFirst_sta_def0(QCEle<Double> first_sta_def0) {
        this.first_sta_def0 = first_sta_def0;
    }

    public QCEle<Double> getFirst_sta_pmin() {
        return first_sta_pmin;
    }

    public void setFirst_sta_pmin(QCEle<Double> first_sta_pmin) {
        this.first_sta_pmin = first_sta_pmin;
    }

    public QCEle<Double> getTime_period_pmin() {
        return time_period_pmin;
    }

    public void setTime_period_pmin(QCEle<Double> time_period_pmin) {
        this.time_period_pmin = time_period_pmin;
    }

    public QCEle<Double> getPmin() {
        return pmin;
    }

    public void setPmin(QCEle<Double> pmin) {
        this.pmin = pmin;
    }

    public QCEle<Double> getFirst_st_def1() {
        return first_st_def1;
    }

    public void setFirst_st_def1(QCEle<Double> first_st_def1) {
        this.first_st_def1 = first_st_def1;
    }
}