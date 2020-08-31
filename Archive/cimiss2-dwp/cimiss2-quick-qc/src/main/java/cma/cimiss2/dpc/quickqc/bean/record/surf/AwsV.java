package cma.cimiss2.dpc.quickqc.bean.record.surf;

import cma.cimiss2.dpc.quickqc.bean.QCEle;

/**
 * @description: 小时能见度
 * @author: When6passBye
 * @create: 2019-07-23 18:44
 **/
public class AwsV {
    private QCEle<Double> hg_sen_sta;/*传感器离地面的高度*/
    private QCEle<Double> hw_sen_sta;/*传感器离水面的高度*/
    private QCEle<Double> back_atr;/*后面值的属性*/
    private QCEle<Double> v; /* 能见度 */
    /*1分钟平均和10分钟平均水平能见度*/
    private QCEle<Double> time_mean_v0;
    private QCEle<Double>[] time_period_v01_10 = new QCEle[2];
    private QCEle<Double>[] v01_10 = new QCEle[2]; /* 1分钟、10分钟平均水平能见度 */
    private QCEle<Double> time_mean_def0;
    /*小时内最小水平能见度*/
    private QCEle<Double> first_sta_vmin;
    private QCEle<Double> time_period_vmin;
    private QCEle<Double> vmin; /* 小时最小能见度 */
    private QCEle<Double> time_hour_vmin; /* 小时最小能见度时间 */
    private QCEle<Double> time_minute_vmin; /* 小时最小能见度时间 */
    private QCEle<Double> first_sta_def0;/*时间意义（缺省值）*/
    private QCEle<Double> hg_sen_end;/*传感器离地面的高度*/
    private QCEle<Double> hw_sen_end;/*传感器离水面的高度*/

    public QCEle<Double> getHg_sen_sta() {
        return hg_sen_sta;
    }

    public void setHg_sen_sta(QCEle<Double> hg_sen_sta) {
        this.hg_sen_sta = hg_sen_sta;
    }

    public QCEle<Double> getHw_sen_sta() {
        return hw_sen_sta;
    }

    public void setHw_sen_sta(QCEle<Double> hw_sen_sta) {
        this.hw_sen_sta = hw_sen_sta;
    }

    public QCEle<Double> getBack_atr() {
        return back_atr;
    }

    public void setBack_atr(QCEle<Double> back_atr) {
        this.back_atr = back_atr;
    }

    public QCEle<Double> getV() {
        return v;
    }

    public void setV(QCEle<Double> v) {
        this.v = v;
    }

    public QCEle<Double> getTime_mean_v0() {
        return time_mean_v0;
    }

    public void setTime_mean_v0(QCEle<Double> time_mean_v0) {
        this.time_mean_v0 = time_mean_v0;
    }

    public QCEle<Double>[] getTime_period_v01_10() {
        return time_period_v01_10;
    }

    public void setTime_period_v01_10(QCEle<Double>[] time_period_v01_10) {
        this.time_period_v01_10 = time_period_v01_10;
    }

    public QCEle<Double>[] getV01_10() {
        return v01_10;
    }

    public void setV01_10(QCEle<Double>[] v01_10) {
        this.v01_10 = v01_10;
    }

    public QCEle<Double> getTime_mean_def0() {
        return time_mean_def0;
    }

    public void setTime_mean_def0(QCEle<Double> time_mean_def0) {
        this.time_mean_def0 = time_mean_def0;
    }

    public QCEle<Double> getFirst_sta_vmin() {
        return first_sta_vmin;
    }

    public void setFirst_sta_vmin(QCEle<Double> first_sta_vmin) {
        this.first_sta_vmin = first_sta_vmin;
    }

    public QCEle<Double> getTime_period_vmin() {
        return time_period_vmin;
    }

    public void setTime_period_vmin(QCEle<Double> time_period_vmin) {
        this.time_period_vmin = time_period_vmin;
    }

    public QCEle<Double> getVmin() {
        return vmin;
    }

    public void setVmin(QCEle<Double> vmin) {
        this.vmin = vmin;
    }

    public QCEle<Double> getTime_hour_vmin() {
        return time_hour_vmin;
    }

    public void setTime_hour_vmin(QCEle<Double> time_hour_vmin) {
        this.time_hour_vmin = time_hour_vmin;
    }

    public QCEle<Double> getTime_minute_vmin() {
        return time_minute_vmin;
    }

    public void setTime_minute_vmin(QCEle<Double> time_minute_vmin) {
        this.time_minute_vmin = time_minute_vmin;
    }

    public QCEle<Double> getFirst_sta_def0() {
        return first_sta_def0;
    }

    public void setFirst_sta_def0(QCEle<Double> first_sta_def0) {
        this.first_sta_def0 = first_sta_def0;
    }

    public QCEle<Double> getHg_sen_end() {
        return hg_sen_end;
    }

    public void setHg_sen_end(QCEle<Double> hg_sen_end) {
        this.hg_sen_end = hg_sen_end;
    }

    public QCEle<Double> getHw_sen_end() {
        return hw_sen_end;
    }

    public void setHw_sen_end(QCEle<Double> hw_sen_end) {
        this.hw_sen_end = hw_sen_end;
    }
}

