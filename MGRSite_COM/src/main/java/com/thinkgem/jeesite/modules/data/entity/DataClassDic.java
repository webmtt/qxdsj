/*
 * @(#)DataClassDic.java 2016年9月27日
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.data.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 描述：CMISS大类规范
 *
 * @author Administrator
 * @version 1.0 2016年9月27日
 */
@Entity
@Table(name="DMD_DATACLASSDIC")
public class DataClassDic {
	@Id
	private String dataClassCode;
	private String uDataClassCode;
	private String chNName;
	private Integer orderNo;
	public String getDataClassCode() {
		return dataClassCode;
	}
	public void setDataClassCode(String dataClassCode) {
		this.dataClassCode = dataClassCode;
	}
	public String getuDataClassCode() {
		return uDataClassCode;
	}
	public void setuDataClassCode(String uDataClassCode) {
		this.uDataClassCode = uDataClassCode;
	}

	public String getChNName() {
		return chNName;
	}
	public void setChNName(String chNName) {
		this.chNName = chNName;
	}
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	
}
