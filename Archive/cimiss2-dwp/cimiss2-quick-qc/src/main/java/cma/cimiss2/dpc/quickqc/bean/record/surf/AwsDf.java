package cma.cimiss2.dpc.quickqc.bean.record.surf;

import cma.cimiss2.dpc.quickqc.bean.QCEle;

/**
 * @description:小时数据风向风速
 * @author: When6passBye
 * @create: 2019-07-23 18:25
 **/
public class AwsDf {
    private QCEle<Double> hg_sen_sta;/*传感器离地面的高度*/
    private QCEle<Double> hw_sen_sta;/*传感器离水面的高度*/

    /*瞬时风*/
    private QCEle<Double> dddins; /* 瞬时风向 */
    private QCEle<Double> fins; /* 瞬时风速 */

    /*2分钟平均和10分钟平均风*/
    private QCEle<Double> time_mean_df_mean10_02;/*时间意义*/
    private QCEle<Double>[] time_period_df_mean10_02 = new QCEle[2]/*10分钟风向时间周期*/;
    private QCEle<Double>[] ddd_mean10_02 = new QCEle[2]; /* 10分钟风向、2分钟风向 */
    private QCEle<Double>[] f_mean10_02 = new QCEle[2]; /* 10分钟平均风速、2分钟平均风速 */
    private QCEle<Double> time_mean_def0;/*时间意义*/

    /*最大风和极大风*/
    private QCEle<Double> time_period_dfmax;
    private QCEle<Double> dddmax; /* 最大风速的风向 */
    private QCEle<Double> fmax; /* 最大风速 */
    private QCEle<Double> time_hour_dfmax; /* 小时最大风速的时间 */
    private QCEle<Double> time_minute_dfmax; /* 小时最大风速的时间 */
    private QCEle<Double> dddmost; /* 极大风速的风向 */
    private QCEle<Double> fmost; /* 极大风速 */
    private QCEle<Double> time_hour_dfmost;/* 小时极大风速 的时间 */
    private QCEle<Double> time_minute_dfmost; /* 小时极大风速 的时间 */

    /*过去6小时和12小时极大风*/
    private QCEle<Double>[] time_period_dfmost06_12 = new QCEle[2];
    private QCEle<Double>[] dddmost06_12 = new QCEle[2]; /* 6小时、12小时最大瞬时风速的风向 */
    private QCEle<Double>[] fmost06_12 = new QCEle[2]; /* 6小时、12小时最大瞬时风速 */

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

    public QCEle<Double> getDddins() {
        return dddins;
    }

    public void setDddins(QCEle<Double> dddins) {
        this.dddins = dddins;
    }

    public QCEle<Double> getFins() {
        return fins;
    }

    public void setFins(QCEle<Double> fins) {
        this.fins = fins;
    }

    public QCEle<Double> getTime_mean_df_mean10_02() {
        return time_mean_df_mean10_02;
    }

    public void setTime_mean_df_mean10_02(QCEle<Double> time_mean_df_mean10_02) {
        this.time_mean_df_mean10_02 = time_mean_df_mean10_02;
    }

    public QCEle<Double>[] getTime_period_df_mean10_02() {
        return time_period_df_mean10_02;
    }

    public void setTime_period_df_mean10_02(QCEle<Double>[] time_period_df_mean10_02) {
        this.time_period_df_mean10_02 = time_period_df_mean10_02;
    }

    public QCEle<Double>[] getDdd_mean10_02() {
        return ddd_mean10_02;
    }

    public void setDdd_mean10_02(QCEle<Double>[] ddd_mean10_02) {
        this.ddd_mean10_02 = ddd_mean10_02;
    }

    public QCEle<Double>[] getF_mean10_02() {
        return f_mean10_02;
    }

    public void setF_mean10_02(QCEle<Double>[] f_mean10_02) {
        this.f_mean10_02 = f_mean10_02;
    }

    public QCEle<Double> getTime_mean_def0() {
        return time_mean_def0;
    }

    public void setTime_mean_def0(QCEle<Double> time_mean_def0) {
        this.time_mean_def0 = time_mean_def0;
    }

    public QCEle<Double> getTime_period_dfmax() {
        return time_period_dfmax;
    }

    public void setTime_period_dfmax(QCEle<Double> time_period_dfmax) {
        this.time_period_dfmax = time_period_dfmax;
    }

    public QCEle<Double> getDddmax() {
        return dddmax;
    }

    public void setDddmax(QCEle<Double> dddmax) {
        this.dddmax = dddmax;
    }

    public QCEle<Double> getFmax() {
        return fmax;
    }

    public void setFmax(QCEle<Double> fmax) {
        this.fmax = fmax;
    }

    public QCEle<Double> getTime_hour_dfmax() {
        return time_hour_dfmax;
    }

    public void setTime_hour_dfmax(QCEle<Double> time_hour_dfmax) {
        this.time_hour_dfmax = time_hour_dfmax;
    }

    public QCEle<Double> getTime_minute_dfmax() {
        return time_minute_dfmax;
    }

    public void setTime_minute_dfmax(QCEle<Double> time_minute_dfmax) {
        this.time_minute_dfmax = time_minute_dfmax;
    }

    public QCEle<Double> getDddmost() {
        return dddmost;
    }

    public void setDddmost(QCEle<Double> dddmost) {
        this.dddmost = dddmost;
    }

    public QCEle<Double> getFmost() {
        return fmost;
    }

    public void setFmost(QCEle<Double> fmost) {
        this.fmost = fmost;
    }

    public QCEle<Double> getTime_hour_dfmost() {
        return time_hour_dfmost;
    }

    public void setTime_hour_dfmost(QCEle<Double> time_hour_dfmost) {
        this.time_hour_dfmost = time_hour_dfmost;
    }

    public QCEle<Double> getTime_minute_dfmost() {
        return time_minute_dfmost;
    }

    public void setTime_minute_dfmost(QCEle<Double> time_minute_dfmost) {
        this.time_minute_dfmost = time_minute_dfmost;
    }

    public QCEle<Double>[] getTime_period_dfmost06_12() {
        return time_period_dfmost06_12;
    }

    public void setTime_period_dfmost06_12(QCEle<Double>[] time_period_dfmost06_12) {
        this.time_period_dfmost06_12 = time_period_dfmost06_12;
    }

    public QCEle<Double>[] getDddmost06_12() {
        return dddmost06_12;
    }

    public void setDddmost06_12(QCEle<Double>[] dddmost06_12) {
        this.dddmost06_12 = dddmost06_12;
    }

    public QCEle<Double>[] getFmost06_12() {
        return fmost06_12;
    }

    public void setFmost06_12(QCEle<Double>[] fmost06_12) {
        this.fmost06_12 = fmost06_12;
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
