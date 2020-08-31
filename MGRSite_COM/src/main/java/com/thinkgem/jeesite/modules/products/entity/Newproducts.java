package com.thinkgem.jeesite.modules.products.entity;

import com.thinkgem.jeesite.mybatis.common.persistence.DataEntity;

import java.util.Date;

/**
 * 最新产品库配置实体类
 * @author zhaoxiaojun 2020年3月5日11:11:31
 */
public class Newproducts extends DataEntity<Newproducts> {
    private String alerteunit;
    private String alertetype;
    private String alerteproduct;
    private String forecastunit;
    private String forecasttype;
    private String forecastproduct;
    private Date created;

    public String getAlerteunit() {
        return alerteunit;
    }

    public void setAlerteunit(String alerteunit) {
        this.alerteunit = alerteunit;
    }

    public String getAlertetype() {
        return alertetype;
    }

    public void setAlertetype(String alertetype) {
        this.alertetype = alertetype;
    }

    public String getAlerteproduct() {
        return alerteproduct;
    }

    public void setAlerteproduct(String alerteproduct) {
        this.alerteproduct = alerteproduct;
    }

    public String getForecastunit() {
        return forecastunit;
    }

    public void setForecastunit(String forecastunit) {
        this.forecastunit = forecastunit;
    }

    public String getForecasttype() {
        return forecasttype;
    }

    public void setForecasttype(String forecasttype) {
        this.forecasttype = forecasttype;
    }

    public String getForecastproduct() {
        return forecastproduct;
    }

    public void setForecastproduct(String forecastproduct) {
        this.forecastproduct = forecastproduct;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
