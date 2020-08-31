package cma.cimiss2.dpc.quickqc.bean.record.surf;

import cma.cimiss2.dpc.quickqc.bean.QCEle;

/**
 * @description: 电线积冰
 * @author: When6passBye
 * @create: 2019-07-23 19:01
 **/
public class AwsWrieIce {
    QCEle<Double> wireicing[]=new QCEle[2];/*电线积冰-现象 */
    QCEle<Double> NS_dia; /*电线积冰-南北方向直径*/
    QCEle<Double> NS_ply; /*电线积冰-南北方向厚度*/
    QCEle<Double> NS_weight; /*电线积冰-南北方向重量*/
    QCEle<Double> WE_dia; /*电线积冰-东西方向直径*/
    QCEle<Double> WE_ply; /*电线积冰-东西方向厚度*/
    QCEle<Double> WE_weight; /*电线积冰-东西方向重量*/
    QCEle<Double> t_wireicing;/*电线积冰-温度*/
    QCEle<Double> ddd_wireicing;/*电线积冰-风向*/
    QCEle<Double> f_wireicing;/*电线积冰-风速*/

    public QCEle<Double>[] getWireicing() {
        return wireicing;
    }

    public void setWireicing(QCEle<Double>[] wireicing) {
        this.wireicing = wireicing;
    }

    public QCEle<Double> getNS_dia() {
        return NS_dia;
    }

    public void setNS_dia(QCEle<Double> NS_dia) {
        this.NS_dia = NS_dia;
    }

    public QCEle<Double> getNS_ply() {
        return NS_ply;
    }

    public void setNS_ply(QCEle<Double> NS_ply) {
        this.NS_ply = NS_ply;
    }

    public QCEle<Double> getNS_weight() {
        return NS_weight;
    }

    public void setNS_weight(QCEle<Double> NS_weight) {
        this.NS_weight = NS_weight;
    }

    public QCEle<Double> getWE_dia() {
        return WE_dia;
    }

    public void setWE_dia(QCEle<Double> WE_dia) {
        this.WE_dia = WE_dia;
    }

    public QCEle<Double> getWE_ply() {
        return WE_ply;
    }

    public void setWE_ply(QCEle<Double> WE_ply) {
        this.WE_ply = WE_ply;
    }

    public QCEle<Double> getWE_weight() {
        return WE_weight;
    }

    public void setWE_weight(QCEle<Double> WE_weight) {
        this.WE_weight = WE_weight;
    }

    public QCEle<Double> getT_wireicing() {
        return t_wireicing;
    }

    public void setT_wireicing(QCEle<Double> t_wireicing) {
        this.t_wireicing = t_wireicing;
    }

    public QCEle<Double> getDdd_wireicing() {
        return ddd_wireicing;
    }

    public void setDdd_wireicing(QCEle<Double> ddd_wireicing) {
        this.ddd_wireicing = ddd_wireicing;
    }

    public QCEle<Double> getF_wireicing() {
        return f_wireicing;
    }

    public void setF_wireicing(QCEle<Double> f_wireicing) {
        this.f_wireicing = f_wireicing;
    }
}

