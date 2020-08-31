package cma.cimiss2.dpc.quickqc.bean.record.surf;

import cma.cimiss2.dpc.quickqc.bean.QCEle;

/**
 * @description: 草温
 * @author: When6passBye
 * @create: 2019-07-23 18:32
 **/
public class AwsTg {
    QCEle<Double> tg; /* 草面温度 */
    QCEle<Double> first_sta_tgmax;
    QCEle<Double> time_period_tgmax;
    QCEle<Double> tgmax; /* 小时最高草面温度 */
    QCEle<Double> time_hour_tgmax; /* 小时最高草面温度时间 */
    QCEle<Double> time_minute_tgmax; /* 小时最高草面温度时间 */
    QCEle<Double> first_sta_def0;

    QCEle<Double> first_sat_tgmin;
    QCEle<Double> time_period_tgmin;
    QCEle<Double> tgmin; /* 小时最低草面温度 */
    QCEle<Double> time_hour_tgmin; /* 小时最低草面温度时间 */
    QCEle<Double> time_minute_tgmin; /* 小时最低草面温度时间 */
    QCEle<Double> first_sta_def1;

    public QCEle<Double> getTg() {
        return tg;
    }

    public void setTg(QCEle<Double> tg) {
        this.tg = tg;
    }

    public QCEle<Double> getFirst_sta_tgmax() {
        return first_sta_tgmax;
    }

    public void setFirst_sta_tgmax(QCEle<Double> first_sta_tgmax) {
        this.first_sta_tgmax = first_sta_tgmax;
    }

    public QCEle<Double> getTime_period_tgmax() {
        return time_period_tgmax;
    }

    public void setTime_period_tgmax(QCEle<Double> time_period_tgmax) {
        this.time_period_tgmax = time_period_tgmax;
    }

    public QCEle<Double> getTgmax() {
        return tgmax;
    }

    public void setTgmax(QCEle<Double> tgmax) {
        this.tgmax = tgmax;
    }

    public QCEle<Double> getTime_hour_tgmax() {
        return time_hour_tgmax;
    }

    public void setTime_hour_tgmax(QCEle<Double> time_hour_tgmax) {
        this.time_hour_tgmax = time_hour_tgmax;
    }

    public QCEle<Double> getTime_minute_tgmax() {
        return time_minute_tgmax;
    }

    public void setTime_minute_tgmax(QCEle<Double> time_minute_tgmax) {
        this.time_minute_tgmax = time_minute_tgmax;
    }

    public QCEle<Double> getFirst_sta_def0() {
        return first_sta_def0;
    }

    public void setFirst_sta_def0(QCEle<Double> first_sta_def0) {
        this.first_sta_def0 = first_sta_def0;
    }

    public QCEle<Double> getFirst_sat_tgmin() {
        return first_sat_tgmin;
    }

    public void setFirst_sat_tgmin(QCEle<Double> first_sat_tgmin) {
        this.first_sat_tgmin = first_sat_tgmin;
    }

    public QCEle<Double> getTime_period_tgmin() {
        return time_period_tgmin;
    }

    public void setTime_period_tgmin(QCEle<Double> time_period_tgmin) {
        this.time_period_tgmin = time_period_tgmin;
    }

    public QCEle<Double> getTgmin() {
        return tgmin;
    }

    public void setTgmin(QCEle<Double> tgmin) {
        this.tgmin = tgmin;
    }

    public QCEle<Double> getTime_hour_tgmin() {
        return time_hour_tgmin;
    }

    public void setTime_hour_tgmin(QCEle<Double> time_hour_tgmin) {
        this.time_hour_tgmin = time_hour_tgmin;
    }

    public QCEle<Double> getTime_minute_tgmin() {
        return time_minute_tgmin;
    }

    public void setTime_minute_tgmin(QCEle<Double> time_minute_tgmin) {
        this.time_minute_tgmin = time_minute_tgmin;
    }

    public QCEle<Double> getFirst_sta_def1() {
        return first_sta_def1;
    }

    public void setFirst_sta_def1(QCEle<Double> first_sta_def1) {
        this.first_sta_def1 = first_sta_def1;
    }
}
