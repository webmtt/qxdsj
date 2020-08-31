package com.thinkgem.jeesite.mybatis.modules.filedecode.common.bean;

// TODO: Auto-generated Javadoc

import java.io.Serializable;

/**
 * *****************************************************************************************  
 * 类描述：  原始报告信息
 *  
 *
 * @author wuzuoqiang
 * 
 * *****************************************************************************************
 * @version 1.0
 *  
 *  
 * Version    Date       ModifiedBy                 Content  
 * -------- ---------    ----------         ------------------------  
 * 1.0      2017年12月28日       wuzuoqiang  
 * @param <T> the generic type
 * @ClassName:  ReoprtInfo
 * @date： 2017年12月28日 上午9:43:28
 */
public class ReportInfo<T> implements Serializable {
	
	/** 原始报文内容. */
	private String report;
	
	/** 原始报告头实体. */
	private T t;
	
	/**
	 * Instantiates a new report info.
	 *
	 * @param t the t
	 * @param report the report
	 */
	public ReportInfo(T t, String report) {
		this.t = t;
		this.report = report;
	}
	
	/**
	 * Instantiates a new report info.
	 */
	public ReportInfo() {
		
	}

	/**
	 * Gets the report.
	 *
	 * @return the report
	 */
	public String getReport() {
		return report;
	}

	/**
	 * Sets the report.
	 *
	 * @param report the new report
	 */
	public void setReport(String report) {
		this.report = report;
	}

	/**
	 * Gets the t.
	 *
	 * @return the t
	 */
	public T getT() {
		return t;
	}

	/**
	 * Sets the t.
	 *
	 * @param t the new t
	 */
	public void setT(T t) {
		this.t = t;
	}
	
	
}
