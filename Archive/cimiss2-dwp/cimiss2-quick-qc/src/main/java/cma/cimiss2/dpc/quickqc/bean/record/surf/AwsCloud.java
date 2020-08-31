package cma.cimiss2.dpc.quickqc.bean.record.surf;

import cma.cimiss2.dpc.quickqc.bean.QCEle;

/**
 * @description: 小时云数据
 * @author: When6passBye
 * @create: 2019-07-23 18:46
 **/
public class AwsCloud {
    private QCEle<Double> cloud_system;/*云探测系统*/
    private QCEle<Double> nn; /* 总云量 */
    private QCEle<Double> ver_mean0;/*垂直探测意义*/
    private QCEle<Double> cloud; /* 编报云量 */
    private QCEle<Double> cloudhigh; /* 云高 */
    private QCEle<Double>[] cloud_class = new QCEle[3];/*低云云类型、中云云类型、高云云类型*/
    private QCEle<Double> ver_mean1;
    private QCEle<Double> nl; /* 低云量 */
    private QCEle<Double>[] cloud_class_add = new QCEle[8];

    public QCEle<Double> getCloud_system() {
        return cloud_system;
    }

    public void setCloud_system(QCEle<Double> cloud_system) {
        this.cloud_system = cloud_system;
    }

    public QCEle<Double> getNn() {
        return nn;
    }

    public void setNn(QCEle<Double> nn) {
        this.nn = nn;
    }

    public QCEle<Double> getVer_mean0() {
        return ver_mean0;
    }

    public void setVer_mean0(QCEle<Double> ver_mean0) {
        this.ver_mean0 = ver_mean0;
    }

    public QCEle<Double> getCloud() {
        return cloud;
    }

    public void setCloud(QCEle<Double> cloud) {
        this.cloud = cloud;
    }

    public QCEle<Double> getCloudhigh() {
        return cloudhigh;
    }

    public void setCloudhigh(QCEle<Double> cloudhigh) {
        this.cloudhigh = cloudhigh;
    }

    public QCEle<Double>[] getCloud_class() {
        return cloud_class;
    }

    public void setCloud_class(QCEle<Double>[] cloud_class) {
        this.cloud_class = cloud_class;
    }

    public QCEle<Double> getVer_mean1() {
        return ver_mean1;
    }

    public void setVer_mean1(QCEle<Double> ver_mean1) {
        this.ver_mean1 = ver_mean1;
    }

    public QCEle<Double> getNl() {
        return nl;
    }

    public void setNl(QCEle<Double> nl) {
        this.nl = nl;
    }

    public QCEle<Double>[] getCloud_class_add() {
        return cloud_class_add;
    }

    public void setCloud_class_add(QCEle<Double>[] cloud_class_add) {
        this.cloud_class_add = cloud_class_add;
    }
}

