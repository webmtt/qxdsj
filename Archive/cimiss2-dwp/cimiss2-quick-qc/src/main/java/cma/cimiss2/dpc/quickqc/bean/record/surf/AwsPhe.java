package cma.cimiss2.dpc.quickqc.bean.record.surf;

import cma.cimiss2.dpc.quickqc.bean.QCEle;

/**
 * @description: 其他天气现象
 * @author: When6passBye
 * @create: 2019-07-08 17:40
 **/
public class AwsPhe {

    QCEle<Double> phe_dec_system;
    QCEle<Double> tornado_distance; /* 龙卷尘卷风距测站距离编码 */
    QCEle<Double> tornado_position; /* 龙卷尘卷风距测站方位编码 */
    QCEle<Double> wireicingdia; /* 电线积冰（雨凇）直径 */
    QCEle<Double> maxhaildia; /* 最大冰雹直径 */
    QCEle<Double> phenomenacode; /* 现在天气现象编码 */
    QCEle<Double> timecycle; /* 过去天气描述时间周期 */
    QCEle<Double> pastweather_01; /* 过去天气01 */
    QCEle<Double> pastweather_02; /* 过去天气02 */

    public QCEle<Double> getPhe_dec_system() {
        return phe_dec_system;
    }

    public void setPhe_dec_system(QCEle<Double> phe_dec_system) {
        this.phe_dec_system = phe_dec_system;
    }

    public QCEle<Double> getTornado_distance() {
        return tornado_distance;
    }

    public void setTornado_distance(QCEle<Double> tornado_distance) {
        this.tornado_distance = tornado_distance;
    }

    public QCEle<Double> getTornado_position() {
        return tornado_position;
    }

    public void setTornado_position(QCEle<Double> tornado_position) {
        this.tornado_position = tornado_position;
    }

    public QCEle<Double> getWireicingdia() {
        return wireicingdia;
    }

    public void setWireicingdia(QCEle<Double> wireicingdia) {
        this.wireicingdia = wireicingdia;
    }

    public QCEle<Double> getMaxhaildia() {
        return maxhaildia;
    }

    public void setMaxhaildia(QCEle<Double> maxhaildia) {
        this.maxhaildia = maxhaildia;
    }

    public QCEle<Double> getPhenomenacode() {
        return phenomenacode;
    }

    public void setPhenomenacode(QCEle<Double> phenomenacode) {
        this.phenomenacode = phenomenacode;
    }

    public QCEle<Double> getTimecycle() {
        return timecycle;
    }

    public void setTimecycle(QCEle<Double> timecycle) {
        this.timecycle = timecycle;
    }

    public QCEle<Double> getPastweather_01() {
        return pastweather_01;
    }

    public void setPastweather_01(QCEle<Double> pastweather_01) {
        this.pastweather_01 = pastweather_01;
    }

    public QCEle<Double> getPastweather_02() {
        return pastweather_02;
    }

    public void setPastweather_02(QCEle<Double> pastweather_02) {
        this.pastweather_02 = pastweather_02;
    }
}

