package cma.cimiss2.dpc.quickqc.bean.record.surf;

import cma.cimiss2.dpc.quickqc.bean.QCEle;

/**
 * @description:
 * @author: When6passBye
 * @create: 2019-07-23 19:10
 **/
public class AwsSs {

    QCEle<Double> year;
    QCEle<Double> month;
    QCEle<Double> day;
    QCEle<Double> time_period_ss0[] = new QCEle[24];
    QCEle<Double> time_period_ss1[] = new QCEle[24];
    QCEle<Double> ss_time[] = new QCEle[24];//小时日照时数
    QCEle<Double> time_period_ss24;
    QCEle<Double> ss24;//日照时数日合计

    public QCEle<Double> getYear() {
        return year;
    }

    public void setYear(QCEle<Double> year) {
        this.year = year;
    }

    public QCEle<Double> getMonth() {
        return month;
    }

    public void setMonth(QCEle<Double> month) {
        this.month = month;
    }

    public QCEle<Double> getDay() {
        return day;
    }

    public void setDay(QCEle<Double> day) {
        this.day = day;
    }

    public QCEle<Double>[] getTime_period_ss0() {
        return time_period_ss0;
    }

    public void setTime_period_ss0(QCEle<Double>[] time_period_ss0) {
        this.time_period_ss0 = time_period_ss0;
    }

    public QCEle<Double>[] getTime_period_ss1() {
        return time_period_ss1;
    }

    public void setTime_period_ss1(QCEle<Double>[] time_period_ss1) {
        this.time_period_ss1 = time_period_ss1;
    }

    public QCEle<Double>[] getSs_time() {
        return ss_time;
    }

    public void setSs_time(QCEle<Double>[] ss_time) {
        this.ss_time = ss_time;
    }

    public QCEle<Double> getTime_period_ss24() {
        return time_period_ss24;
    }

    public void setTime_period_ss24(QCEle<Double> time_period_ss24) {
        this.time_period_ss24 = time_period_ss24;
    }

    public QCEle<Double> getSs24() {
        return ss24;
    }

    public void setSs24(QCEle<Double> ss24) {
        this.ss24 = ss24;
    }
}

