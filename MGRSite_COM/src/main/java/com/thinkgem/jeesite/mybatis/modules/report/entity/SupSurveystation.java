/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.report.entity;

import com.thinkgem.jeesite.mybatis.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 报表查询参数配置Entity
 * @author yang.kq
 * @version 2019-11-06
 */
public class SupSurveystation extends DataEntity<SupSurveystation> {
	
	private static final long serialVersionUID = 1L;
	private Long stationNum;		// 区站号
	private String stationName;		// 站点名称
	private String provinces; //省
	private String cities; //市
	private String county;//区
	private String wd; //纬度
	private String jd; //经度
	private String viewHeight;//测站高度

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getProvinces() {
		return provinces;
	}

	public void setProvinces(String provinces) {
		this.provinces = provinces;
	}

	public String getCities() {
		return cities;
	}

	public void setCities(String cities) {
		this.cities = cities;
	}

	public String getWd() {
		return wd;
	}

	public void setWd(String wd) {
		this.wd = wd;
	}

	public String getJd() {
		return jd;
	}

	public void setJd(String jd) {
		this.jd = jd;
	}

	public String getViewHeight() {
		return viewHeight;
	}

	public void setViewHeight(String viewHeight) {
		this.viewHeight = viewHeight;
	}

	public SupSurveystation() {
		super();
	}

	public SupSurveystation(String id){
		super(id);
	}

	@NotNull(message="区站号不能为空")
	public Long getStationNum() {
		return stationNum;
	}

	public void setStationNum(Long stationNum) {
		this.stationNum = stationNum;
	}
	
	@Length(min=1, max=50, message="站点名称长度必须介于 1 和 50 之间")
	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}


}