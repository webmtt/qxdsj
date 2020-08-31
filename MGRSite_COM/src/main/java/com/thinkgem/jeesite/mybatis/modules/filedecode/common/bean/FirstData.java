package com.thinkgem.jeesite.mybatis.modules.filedecode.common.bean;



import java.io.Serializable;

/**
 * @author yangkq
 * @version 1.0
 * @date 2020/4/9
 */

public class FirstData implements Serializable {
    private static final long serialVersionUID = 1L;
    private String areacode; // 区站号
    private String jd; // 经度
    private String wd; // 纬度
    private String viewhight; // 观测场拔海高度
    private String quickhight; // 水银槽拔海高度
    private String ganshight; // 气压感应器拔海高度
    private String observehight; // 观测平台距地高度
    private String obversetype; // 观测方式和测站类别
    private String obsercecode; // 观测项目标识
    private String qualitycode; // 质量控制指示码
    private String arcaninehight; // 风速器距地高度
    private String year;//年
    private String month;//月

    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    public String getJd() {
        return jd;
    }

    public void setJd(String jd) {
        this.jd = jd;
    }

    public String getWd() {
        return wd;
    }

    public void setWd(String wd) {
        this.wd = wd;
    }

    public String getViewhight() {
        return viewhight;
    }

    public void setViewhight(String viewhight) {
        this.viewhight = viewhight;
    }

    public String getQuickhight() {
        return quickhight;
    }

    public void setQuickhight(String quickhight) {
        this.quickhight = quickhight;
    }

    public String getGanshight() {
        return ganshight;
    }

    public void setGanshight(String ganshight) {
        this.ganshight = ganshight;
    }

    public String getObservehight() {
        return observehight;
    }

    public void setObservehight(String observehight) {
        this.observehight = observehight;
    }

    public String getObversetype() {
        return obversetype;
    }

    public void setObversetype(String obversetype) {
        this.obversetype = obversetype;
    }

    public String getObsercecode() {
        return obsercecode;
    }

    public void setObsercecode(String obsercecode) {
        this.obsercecode = obsercecode;
    }

    public String getQualitycode() {
        return qualitycode;
    }

    public void setQualitycode(String qualitycode) {
        this.qualitycode = qualitycode;
    }

    public String getArcaninehight() {
        return arcaninehight;
    }

    public void setArcaninehight(String arcaninehight) {
        this.arcaninehight = arcaninehight;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
