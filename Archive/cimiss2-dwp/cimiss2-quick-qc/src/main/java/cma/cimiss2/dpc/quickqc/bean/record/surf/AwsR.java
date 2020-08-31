package cma.cimiss2.dpc.quickqc.bean.record.surf;

import cma.cimiss2.dpc.quickqc.bean.Constants;
import cma.cimiss2.dpc.quickqc.bean.QCEle;

/**
 * @description: 降水
 * @author: When6passBye
 * @create: 2019-07-23 18:22
 **/
public class AwsR {
    QCEle<Double> hg_sen_sta;/*传感器离地面的高度*/
    QCEle<Double> hw_sen_sta;/*传感器离水面的高度*/
    QCEle<Double> method_r;/*降水测量方法*/
    QCEle<Double> r; /* 小时降水量 */
    QCEle<Double> r03; /* 3小时累计降水量 */
    QCEle<Double> r06; /* 6小时累计降水量 */
    QCEle<Double> r12; /* 12小时累计降水量 */
    QCEle<Double> r24; /* 24小时累计降水量 */
    QCEle<Double> time_period_r_total;
    QCEle<Double> r_total;/*总降水量*/
    QCEle<Double> hg_sen_end;/*传感器离地面的高度*/
    QCEle<Double> hw_sen_end;/*传感器离水面的高度*/

    public AwsR(){
        this.hg_sen_sta=new QCEle(Constants.NO_MEANS);
        this.hw_sen_sta=new QCEle(Constants.NO_MEANS);
        this.method_r=new QCEle(Constants.NO_MEANS);
        this.r=new QCEle(Constants.NO_MEANS);
        this.r03=new QCEle(Constants.NO_MEANS);
        this.r06=new QCEle(Constants.NO_MEANS);
        this.r12=new QCEle(Constants.NO_MEANS);
        this.r24=new QCEle(Constants.NO_MEANS);
        this.time_period_r_total=new QCEle(Constants.NO_MEANS);
        this.r_total=new QCEle(Constants.NO_MEANS);
        this.hg_sen_end=new QCEle(Constants.NO_MEANS);
        this.hw_sen_end=new QCEle(Constants.NO_MEANS);

    }

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

    public QCEle<Double> getMethod_r() {
        return method_r;
    }

    public void setMethod_r(QCEle<Double> method_r) {
        this.method_r = method_r;
    }

    public QCEle<Double> getR() {
        return r;
    }

    public void setR(QCEle<Double> r) {
        this.r = r;
    }

    public QCEle<Double> getR03() {
        return r03;
    }

    public void setR03(QCEle<Double> r03) {
        this.r03 = r03;
    }

    public QCEle<Double> getR06() {
        return r06;
    }

    public void setR06(QCEle<Double> r06) {
        this.r06 = r06;
    }

    public QCEle<Double> getR12() {
        return r12;
    }

    public void setR12(QCEle<Double> r12) {
        this.r12 = r12;
    }

    public QCEle<Double> getR24() {
        return r24;
    }

    public void setR24(QCEle<Double> r24) {
        this.r24 = r24;
    }

    public QCEle<Double> getTime_period_r_total() {
        return time_period_r_total;
    }

    public void setTime_period_r_total(QCEle<Double> time_period_r_total) {
        this.time_period_r_total = time_period_r_total;
    }

    public QCEle<Double> getR_total() {
        return r_total;
    }

    public void setR_total(QCEle<Double> r_total) {
        this.r_total = r_total;
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

