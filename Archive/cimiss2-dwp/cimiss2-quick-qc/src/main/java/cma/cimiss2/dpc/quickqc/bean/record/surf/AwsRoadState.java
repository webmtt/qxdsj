package cma.cimiss2.dpc.quickqc.bean.record.surf;

import cma.cimiss2.dpc.quickqc.bean.QCEle;

/**
 * @description: 路面状态
 * @author: When6passBye
 * @create: 2019-07-23 19:02
 **/
public class AwsRoadState {

    QCEle<Double> road_state;
    QCEle<Double> depth_snow;/*路面雪层厚度*/
    QCEle<Double> depth_water;/*路面水层厚度*/
    QCEle<Double> depth_ice;/*路面冰层厚度*/
    QCEle<Double> t_icepoint;/*路面冰点温度*/
    QCEle<Double> snowmelt_con;/*融雪剂浓度*/

    public QCEle<Double> getRoad_state() {
        return road_state;
    }

    public void setRoad_state(QCEle<Double> road_state) {
        this.road_state = road_state;
    }

    public QCEle<Double> getDepth_snow() {
        return depth_snow;
    }

    public void setDepth_snow(QCEle<Double> depth_snow) {
        this.depth_snow = depth_snow;
    }

    public QCEle<Double> getDepth_water() {
        return depth_water;
    }

    public void setDepth_water(QCEle<Double> depth_water) {
        this.depth_water = depth_water;
    }

    public QCEle<Double> getDepth_ice() {
        return depth_ice;
    }

    public void setDepth_ice(QCEle<Double> depth_ice) {
        this.depth_ice = depth_ice;
    }

    public QCEle<Double> getT_icepoint() {
        return t_icepoint;
    }

    public void setT_icepoint(QCEle<Double> t_icepoint) {
        this.t_icepoint = t_icepoint;
    }

    public QCEle<Double> getSnowmelt_con() {
        return snowmelt_con;
    }

    public void setSnowmelt_con(QCEle<Double> snowmelt_con) {
        this.snowmelt_con = snowmelt_con;
    }
}
