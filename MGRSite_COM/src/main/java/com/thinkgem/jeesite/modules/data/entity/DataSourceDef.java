package com.thinkgem.jeesite.modules.data.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "BMD_DATASOURCEDEF")
public class DataSourceDef implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String dsaccesscode;
	private String accesstype;
	private String accessurl;
	private String dataparaname;
	private Integer invalid;
	@Id
	public String getDsaccesscode() {
		return dsaccesscode;
	}
	public void setDsaccesscode(String dsaccesscode) {
		this.dsaccesscode = dsaccesscode;
	}
	public String getAccesstype() {
		return accesstype;
	}
	public void setAccesstype(String accesstype) {
		this.accesstype = accesstype;
	}
	public String getAccessurl() {
		return accessurl;
	}
	public void setAccessurl(String accessurl) {
		this.accessurl = accessurl;
	}
	public String getDataparaname() {
		return dataparaname;
	}
	public void setDataparaname(String dataparaname) {
		this.dataparaname = dataparaname;
	}
	public Integer getInvalid() {
		return invalid;
	}
	public void setInvalid(Integer invalid) {
		this.invalid = invalid;
	}
	@Override
	public String toString() {
		return "DataSourceDef [dsaccesscode=" + dsaccesscode + ", accesstype="
				+ accesstype + ", accessurl=" + accessurl + ", dataparaname="
				+ dataparaname + ", invalid=" + invalid + "]";
	}
	
	
	
	
	
}
