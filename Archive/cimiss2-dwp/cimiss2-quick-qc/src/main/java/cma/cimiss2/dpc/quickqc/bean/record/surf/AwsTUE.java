package cma.cimiss2.dpc.quickqc.bean.record.surf;

import cma.cimiss2.dpc.quickqc.bean.QCEle;

/**
 * @description: 温湿度水气压
 * @author: When6passBye
 * @create: 2019-07-08 17:23
 **/
public class AwsTUE {
    QCEle<Double> hg_sen_sta;/*传感器离地面的高度*/
    QCEle<Double> hw_sen_sta;/*传感器离水面的高度*/
    QCEle<Double> t; /* 气温 */
    QCEle<Double> td; /* 露点温度 */
    QCEle<Double> u; /* 湿度 */
    QCEle<Double> e; /* 水汽压 */
    QCEle<Double> time_period_tmax;/*时间周期*/
    QCEle<Double> tmax; /* 小时最高气温 */
    QCEle<Double> time_hour_tmax;/*现象出现时*/
    QCEle<Double> time_minute_tmax;/*现象出现分*/
    QCEle<Double> time_period_tmin;
    QCEle<Double> tmin; /* 小时最低气温 */
    QCEle<Double> time_hour_tmin;/*现象出现时*/
    QCEle<Double> time_minute_tmin;/*现象出现分*/
    QCEle<Double> time_period_umin;
    QCEle<Double> umin; /* 小时最低湿度 */
    QCEle<Double> time_hour_umin;/*现象出现时*/
    QCEle<Double> time_minute_umin;/*现象出现分*/
    QCEle<Double> t24; /* 24小时变温 */
    QCEle<Double> tmax24; /* 24小时最高气温 */
    QCEle<Double> tmin24; /* 24小时最低气温 */
    QCEle<Double> hg_sen_end;/*传感器离地面的高度*/
    QCEle<Double> hw_sen_end;/*传感器离水面的高度*/

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

    public QCEle<Double> getT() {
        return t;
    }

    public void setT(QCEle<Double> t) {
        this.t = t;
    }

    public QCEle<Double> getTd() {
        return td;
    }

    public void setTd(QCEle<Double> td) {
        this.td = td;
    }

    public QCEle<Double> getU() {
        return u;
    }

    public void setU(QCEle<Double> u) {
        this.u = u;
    }

    public QCEle<Double> getE() {
        return e;
    }

    public void setE(QCEle<Double> e) {
        this.e = e;
    }

    public QCEle<Double> getTime_period_tmax() {
        return time_period_tmax;
    }

    public void setTime_period_tmax(QCEle<Double> time_period_tmax) {
        this.time_period_tmax = time_period_tmax;
    }

    public QCEle<Double> getTmax() {
        return tmax;
    }

    public void setTmax(QCEle<Double> tmax) {
        this.tmax = tmax;
    }

    public QCEle<Double> getTime_hour_tmax() {
        return time_hour_tmax;
    }

    public void setTime_hour_tmax(QCEle<Double> time_hour_tmax) {
        this.time_hour_tmax = time_hour_tmax;
    }

    public QCEle<Double> getTime_minute_tmax() {
        return time_minute_tmax;
    }

    public void setTime_minute_tmax(QCEle<Double> time_minute_tmax) {
        this.time_minute_tmax = time_minute_tmax;
    }

    public QCEle<Double> getTime_period_tmin() {
        return time_period_tmin;
    }

    public void setTime_period_tmin(QCEle<Double> time_period_tmin) {
        this.time_period_tmin = time_period_tmin;
    }

    public QCEle<Double> getTmin() {
        return tmin;
    }

    public void setTmin(QCEle<Double> tmin) {
        this.tmin = tmin;
    }

    public QCEle<Double> getTime_hour_tmin() {
        return time_hour_tmin;
    }

    public void setTime_hour_tmin(QCEle<Double> time_hour_tmin) {
        this.time_hour_tmin = time_hour_tmin;
    }

    public QCEle<Double> getTime_minute_tmin() {
        return time_minute_tmin;
    }

    public void setTime_minute_tmin(QCEle<Double> time_minute_tmin) {
        this.time_minute_tmin = time_minute_tmin;
    }

    public QCEle<Double> getTime_period_umin() {
        return time_period_umin;
    }

    public void setTime_period_umin(QCEle<Double> time_period_umin) {
        this.time_period_umin = time_period_umin;
    }

    public QCEle<Double> getUmin() {
        return umin;
    }

    public void setUmin(QCEle<Double> umin) {
        this.umin = umin;
    }

    public QCEle<Double> getTime_hour_umin() {
        return time_hour_umin;
    }

    public void setTime_hour_umin(QCEle<Double> time_hour_umin) {
        this.time_hour_umin = time_hour_umin;
    }

    public QCEle<Double> getTime_minute_umin() {
        return time_minute_umin;
    }

    public void setTime_minute_umin(QCEle<Double> time_minute_umin) {
        this.time_minute_umin = time_minute_umin;
    }

    public QCEle<Double> getT24() {
        return t24;
    }

    public void setT24(QCEle<Double> t24) {
        this.t24 = t24;
    }

    public QCEle<Double> getTmax24() {
        return tmax24;
    }

    public void setTmax24(QCEle<Double> tmax24) {
        this.tmax24 = tmax24;
    }

    public QCEle<Double> getTmin24() {
        return tmin24;
    }

    public void setTmin24(QCEle<Double> tmin24) {
        this.tmin24 = tmin24;
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
