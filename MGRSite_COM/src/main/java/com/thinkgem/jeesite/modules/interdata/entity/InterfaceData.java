/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.interdata.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.thinkgem.jeesite.mybatis.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * API接口展示Entity
 * @author zhaoxiaojun
 * @version 2019-12-06
 */
public class InterfaceData extends DataEntity<InterfaceData> {
	
	private String dataClassId;
	private String dataClassName;
	private int serialNo;
	private String description;
	private String shortName;

	public String getDataClassId() {
		return dataClassId;
	}

	public void setDataClassId(String dataClassId) {
		this.dataClassId = dataClassId;
	}

	public String getDataClassName() {
		return dataClassName;
	}

	public void setDataClassName(String dataClassName) {
		this.dataClassName = dataClassName;
	}

	public int getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

}