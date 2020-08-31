package com.thinkgem.jeesite.modules.sys.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



/**
 * 公共参数Entity
 * @author yb
 * @version 2014-12-19
 */
@Entity
@Table(name = "DMD_ComParas")
public class Comparas implements Serializable {

	private static final long serialVersionUID = 1L;
	private String keyid;//键
	private String type;//类型
	private Integer booleanvalue;//布尔类型参数
	private Integer intvalue;//整型参数
	private String stringvalue;//路径
	private Integer invalid;
	private String name;//名称
	private String description;//描述
	private Date created;//创建时间

	public Comparas() {
		super();
	}

	@Id
	public String getKeyid() {
		return keyid;
	}

	public void setKeyid(String keyid) {
		this.keyid = keyid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public String getStringvalue() {
		return stringvalue;
	}

	public void setStringvalue(String stringvalue) {
		this.stringvalue = stringvalue;
	}



	public Integer getBooleanvalue() {
		return booleanvalue;
	}

	public void setBooleanvalue(Integer booleanvalue) {
		this.booleanvalue = booleanvalue;
	}

	public Integer getIntvalue() {
		return intvalue;
	}

	public void setIntvalue(Integer intvalue) {
		this.intvalue = intvalue;
	}

	public Integer getInvalid() {
		return invalid;
	}

	public void setInvalid(Integer invalid) {
		this.invalid = invalid;
	}

	@Override
	public String toString() {
		return "Comparas [keyid=" + keyid + ", type=" + type + ", booleanvalue=" + booleanvalue + ", intvalue="
				+ intvalue + ", stringvalue=" + stringvalue + ", invalid=" + invalid + "]";
	}



}


