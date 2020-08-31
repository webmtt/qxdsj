package com.thinkgem.jeesite.modules.recordquery.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 描述：数据 资料
 *
 * @author Administrator
 * @version 1.0 2016年10月9日
 */
@Entity
@Table(name = "BMD_DATASEARCHDEF")
public class DataSearchDef {
	private String datacode;
	
	private String DataChnname;
	@Id
	public String getDatacode() {
		return datacode;
	}
	public void setDatacode(String datacode) {
		this.datacode = datacode;
	}
	public String getDataChnname() {
		return DataChnname;
	}
	public void setDataChnname(String dataChnname) {
		DataChnname = dataChnname;
	}
	
	
}
