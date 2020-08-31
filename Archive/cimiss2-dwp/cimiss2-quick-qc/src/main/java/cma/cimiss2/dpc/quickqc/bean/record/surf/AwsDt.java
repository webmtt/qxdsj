package cma.cimiss2.dpc.quickqc.bean.record.surf;

import cma.cimiss2.dpc.quickqc.bean.QCEle;

/**
 * @description: 地表温度、浅层地温和深层地温
 * @author: When6passBye
 * @create: 2019-07-23 18:30
 **/
public class AwsDt {
    QCEle<Double> d0; /* 地表温度 */
    QCEle<Double> d0min12; /* 12小时最低地温 */
    QCEle<Double> depth[] = new QCEle[8];/*地下深度*/
    QCEle<Double> d[] = new QCEle[8];/*土壤温度*/
    QCEle<Double> depth_ug;

    QCEle<Double> first_sta_d0max;
    QCEle<Double> time_period_d0max;
    QCEle<Double> d0max; /* 小时最高地温 */
    QCEle<Double> time_hour_d0max;
    QCEle<Double> time_minute_d0max;
    QCEle<Double> first_sta_def0;/*一级统计（缺省）*/

    QCEle<Double> first_sta_d0min;
    QCEle<Double> time_period_d0min;
    QCEle<Double> d0min; /* 小时最低地温 */
    QCEle<Double> time_hour_d0min; /* 小时最低地温时间 */
    QCEle<Double> time_minute_d0min; /* 小时最低地温时间 */
    QCEle<Double> first_sta_def1;/*一级统计（缺省）*/

    public QCEle<Double> getD0() {
        return d0;
    }

    public void setD0(QCEle<Double> d0) {
        this.d0 = d0;
    }

    public QCEle<Double> getD0min12() {
        return d0min12;
    }

    public void setD0min12(QCEle<Double> d0min12) {
        this.d0min12 = d0min12;
    }

    public QCEle<Double>[] getDepth() {
        return depth;
    }

    public void setDepth(QCEle<Double>[] depth) {
        this.depth = depth;
    }

    public QCEle<Double>[] getD() {
        return d;
    }

    public void setD(QCEle<Double>[] d) {
        this.d = d;
    }

    public QCEle<Double> getDepth_ug() {
        return depth_ug;
    }

    public void setDepth_ug(QCEle<Double> depth_ug) {
        this.depth_ug = depth_ug;
    }

    public QCEle<Double> getFirst_sta_d0max() {
        return first_sta_d0max;
    }

    public void setFirst_sta_d0max(QCEle<Double> first_sta_d0max) {
        this.first_sta_d0max = first_sta_d0max;
    }

    public QCEle<Double> getTime_period_d0max() {
        return time_period_d0max;
    }

    public void setTime_period_d0max(QCEle<Double> time_period_d0max) {
        this.time_period_d0max = time_period_d0max;
    }

    public QCEle<Double> getD0max() {
        return d0max;
    }

    public void setD0max(QCEle<Double> d0max) {
        this.d0max = d0max;
    }

    public QCEle<Double> getTime_hour_d0max() {
        return time_hour_d0max;
    }

    public void setTime_hour_d0max(QCEle<Double> time_hour_d0max) {
        this.time_hour_d0max = time_hour_d0max;
    }

    public QCEle<Double> getTime_minute_d0max() {
        return time_minute_d0max;
    }

    public void setTime_minute_d0max(QCEle<Double> time_minute_d0max) {
        this.time_minute_d0max = time_minute_d0max;
    }

    public QCEle<Double> getFirst_sta_def0() {
        return first_sta_def0;
    }

    public void setFirst_sta_def0(QCEle<Double> first_sta_def0) {
        this.first_sta_def0 = first_sta_def0;
    }

    public QCEle<Double> getFirst_sta_d0min() {
        return first_sta_d0min;
    }

    public void setFirst_sta_d0min(QCEle<Double> first_sta_d0min) {
        this.first_sta_d0min = first_sta_d0min;
    }

    public QCEle<Double> getTime_period_d0min() {
        return time_period_d0min;
    }

    public void setTime_period_d0min(QCEle<Double> time_period_d0min) {
        this.time_period_d0min = time_period_d0min;
    }

    public QCEle<Double> getD0min() {
        return d0min;
    }

    public void setD0min(QCEle<Double> d0min) {
        this.d0min = d0min;
    }

    public QCEle<Double> getTime_hour_d0min() {
        return time_hour_d0min;
    }

    public void setTime_hour_d0min(QCEle<Double> time_hour_d0min) {
        this.time_hour_d0min = time_hour_d0min;
    }

    public QCEle<Double> getTime_minute_d0min() {
        return time_minute_d0min;
    }

    public void setTime_minute_d0min(QCEle<Double> time_minute_d0min) {
        this.time_minute_d0min = time_minute_d0min;
    }

    public QCEle<Double> getFirst_sta_def1() {
        return first_sta_def1;
    }

    public void setFirst_sta_def1(QCEle<Double> first_sta_def1) {
        this.first_sta_def1 = first_sta_def1;
    }
}

