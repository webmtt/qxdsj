package cma.cimiss2.dpc.quickqc.bean.record.surf;


import cma.cimiss2.dpc.quickqc.bean.QCEle;

/**
 * @description: 雪深雪压
 * @author: When6passBye
 * @create: 2019-07-08 17:37
 **/
public class AwsSolidDepth {

    QCEle<Double> dec_method_snowdepth;
    QCEle<Double> snowdepth; /* 积雪深度 */
    QCEle<Double> snowpressure; /* 雪压 */

    public QCEle<Double> getDec_method_snowdepth() {
        return dec_method_snowdepth;
    }

    public void setDec_method_snowdepth(QCEle<Double> dec_method_snowdepth) {
        this.dec_method_snowdepth = dec_method_snowdepth;
    }

    public QCEle<Double> getSnowdepth() {
        return snowdepth;
    }

    public void setSnowdepth(QCEle<Double> snowdepth) {
        this.snowdepth = snowdepth;
    }

    public QCEle<Double> getSnowpressure() {
        return snowpressure;
    }

    public void setSnowpressure(QCEle<Double> snowpressure) {
        this.snowpressure = snowpressure;
    }
}
