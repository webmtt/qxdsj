package cma.cimiss2.dpc.quickqc.bean.record.surf;

import cma.cimiss2.dpc.quickqc.bean.QCEle;

/**
 * @description:蒸发
 * @author: When6passBye
 * @create: 2019-07-23 18:24
 **/
public class AwsL {
    QCEle<Double> time_period;
    QCEle<Double> dev_type_l;/*测量蒸发的仪器类型或作物类型*/
    QCEle<Double> l; /* 小时蒸发量 */
    QCEle<Double> l_wl;/*蒸发水位*/

    public QCEle<Double> getTime_period() {
        return time_period;
    }

    public void setTime_period(QCEle<Double> time_period) {
        this.time_period = time_period;
    }

    public QCEle<Double> getDev_type_l() {
        return dev_type_l;
    }

    public void setDev_type_l(QCEle<Double> dev_type_l) {
        this.dev_type_l = dev_type_l;
    }

    public QCEle<Double> getL() {
        return l;
    }

    public void setL(QCEle<Double> l) {
        this.l = l;
    }

    public QCEle<Double> getL_wl() {
        return l_wl;
    }

    public void setL_wl(QCEle<Double> l_wl) {
        this.l_wl = l_wl;
    }
}
