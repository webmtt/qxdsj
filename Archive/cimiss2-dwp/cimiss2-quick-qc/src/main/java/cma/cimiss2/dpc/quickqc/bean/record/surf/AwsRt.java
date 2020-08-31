package cma.cimiss2.dpc.quickqc.bean.record.surf;

import cma.cimiss2.dpc.quickqc.bean.QCEle;

/**
 * @description: 路面温度
 * @author: When6passBye
 * @create: 2019-07-23 18:35
 **/
public class AwsRt {

    QCEle<Double> rt;/*路面温度*/
    QCEle<Double> rt10;/*10cm路基温度*/
    QCEle<Double> first_sta_rtmax;
    QCEle<Double> rtmax; /* 小时最高路面温度 */
    QCEle<Double> time_hour_rtmax; /* 小时最高路面温度时间 */
    QCEle<Double> time_minute_rtmax; /* 小时最高路面温度时间 */

    QCEle<Double> first_sta_def0;
    QCEle<Double> first_sta_rtmin;
    QCEle<Double> rtmin; /* 小时最低路面温度 */
    QCEle<Double> time_hour_rtmin; /* 小时最低路面温度时间 */
    QCEle<Double> time_minute_rtmin; /* 小时最低路面温度时间 */
    QCEle<Double> first_sta_def1;

    public QCEle<Double> getRt() {
        return rt;
    }

    public void setRt(QCEle<Double> rt) {
        this.rt = rt;
    }

    public QCEle<Double> getRt10() {
        return rt10;
    }

    public void setRt10(QCEle<Double> rt10) {
        this.rt10 = rt10;
    }

    public QCEle<Double> getFirst_sta_rtmax() {
        return first_sta_rtmax;
    }

    public void setFirst_sta_rtmax(QCEle<Double> first_sta_rtmax) {
        this.first_sta_rtmax = first_sta_rtmax;
    }

    public QCEle<Double> getRtmax() {
        return rtmax;
    }

    public void setRtmax(QCEle<Double> rtmax) {
        this.rtmax = rtmax;
    }

    public QCEle<Double> getTime_hour_rtmax() {
        return time_hour_rtmax;
    }

    public void setTime_hour_rtmax(QCEle<Double> time_hour_rtmax) {
        this.time_hour_rtmax = time_hour_rtmax;
    }

    public QCEle<Double> getTime_minute_rtmax() {
        return time_minute_rtmax;
    }

    public void setTime_minute_rtmax(QCEle<Double> time_minute_rtmax) {
        this.time_minute_rtmax = time_minute_rtmax;
    }

    public QCEle<Double> getFirst_sta_def0() {
        return first_sta_def0;
    }

    public void setFirst_sta_def0(QCEle<Double> first_sta_def0) {
        this.first_sta_def0 = first_sta_def0;
    }

    public QCEle<Double> getFirst_sta_rtmin() {
        return first_sta_rtmin;
    }

    public void setFirst_sta_rtmin(QCEle<Double> first_sta_rtmin) {
        this.first_sta_rtmin = first_sta_rtmin;
    }

    public QCEle<Double> getRtmin() {
        return rtmin;
    }

    public void setRtmin(QCEle<Double> rtmin) {
        this.rtmin = rtmin;
    }

    public QCEle<Double> getTime_hour_rtmin() {
        return time_hour_rtmin;
    }

    public void setTime_hour_rtmin(QCEle<Double> time_hour_rtmin) {
        this.time_hour_rtmin = time_hour_rtmin;
    }

    public QCEle<Double> getTime_minute_rtmin() {
        return time_minute_rtmin;
    }

    public void setTime_minute_rtmin(QCEle<Double> time_minute_rtmin) {
        this.time_minute_rtmin = time_minute_rtmin;
    }

    public QCEle<Double> getFirst_sta_def1() {
        return first_sta_def1;
    }

    public void setFirst_sta_def1(QCEle<Double> first_sta_def1) {
        this.first_sta_def1 = first_sta_def1;
    }
}

