package cma.cimiss2.dpc.quickqc.bean.record.surf;

import cma.cimiss2.dpc.quickqc.bean.QCEle;

/**
 * @description: 地面状态
 * @author: When6passBye
 * @create: 2019-07-23 18:48
 **/
public class AwsGroundState {
    QCEle<Double> dec_method_groudstate;
    QCEle<Double> groudstate;

    public QCEle<Double> getDec_method_groudstate() {
        return dec_method_groudstate;
    }

    public void setDec_method_groudstate(QCEle<Double> dec_method_groudstate) {
        this.dec_method_groudstate = dec_method_groudstate;
    }

    public QCEle<Double> getGroudstate() {
        return groudstate;
    }

    public void setGroudstate(QCEle<Double> groudstate) {
        this.groudstate = groudstate;
    }
}

