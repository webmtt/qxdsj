package com.thinkgem.jeesite.modules.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 描述：要素集合要素定义表
 */
@Entity
@Table(name = "BMD_ELESETELEMENTDEF")
public class EleSetElementDef {
	@Id
	private Integer id;//not null
	private String  elesetcode;//not null
	private String  uelecode;//not null
	private String  celecode;
	private String  elementname;//not null
	private String  elegroupcode;
	private String  descriptionchn;
	private String  descriptioneng;
	private String  datatype;
	private String  dataunit;
	private String  elementrange;
	private String  specvalue;
	private String  specvaluedesc;
	private Integer  isoptional;
	private Integer  isfilter;
	private Integer  orderno;
	private Integer  invalid;
	private Date  created;
	private String  createdby;
	private Date  updated;
	private String  updatedby;
	private Integer  ishidden;
	private Integer  isqc;
	private Integer  isseleted;
	private String  dataformat;
	private String  filtervalue;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getElesetcode() {
		return elesetcode;
	}
	public void setElesetcode(String elesetcode) {
		this.elesetcode = elesetcode;
	}
	public String getUelecode() {
		return uelecode;
	}
	public void setUelecode(String uelecode) {
		this.uelecode = uelecode;
	}
	public String getCelecode() {
		return celecode;
	}
	public void setCelecode(String celecode) {
		this.celecode = celecode;
	}
	public String getElementname() {
		return elementname;
	}
	public void setElementname(String elementname) {
		this.elementname = elementname;
	}
	public String getElegroupcode() {
		return elegroupcode;
	}
	public void setElegroupcode(String elegroupcode) {
		this.elegroupcode = elegroupcode;
	}
	public String getDescriptionchn() {
		return descriptionchn;
	}
	public void setDescriptionchn(String descriptionchn) {
		this.descriptionchn = descriptionchn;
	}
	public String getDescriptioneng() {
		return descriptioneng;
	}
	public void setDescriptioneng(String descriptioneng) {
		this.descriptioneng = descriptioneng;
	}
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public String getDataunit() {
		return dataunit;
	}
	public void setDataunit(String dataunit) {
		this.dataunit = dataunit;
	}
	public String getElementrange() {
		return elementrange;
	}
	public void setElementrange(String elementrange) {
		this.elementrange = elementrange;
	}
	public String getSpecvalue() {
		return specvalue;
	}
	public void setSpecvalue(String specvalue) {
		this.specvalue = specvalue;
	}
	public String getSpecvaluedesc() {
		return specvaluedesc;
	}
	public void setSpecvaluedesc(String specvaluedesc) {
		this.specvaluedesc = specvaluedesc;
	}
	public Integer getIsoptional() {
		return isoptional;
	}
	public void setIsoptional(Integer isoptional) {
		this.isoptional = isoptional;
	}
	public Integer getIsfilter() {
		return isfilter;
	}
	public void setIsfilter(Integer isfilter) {
		this.isfilter = isfilter;
	}
	public Integer getOrderno() {
		return orderno;
	}
	public void setOrderno(Integer orderno) {
		this.orderno = orderno;
	}
	public Integer getInvalid() {
		return invalid;
	}
	public void setInvalid(Integer invalid) {
		this.invalid = invalid;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getCreatedby() {
		return createdby;
	}
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	public String getUpdatedby() {
		return updatedby;
	}
	public void setUpdatedby(String updatedby) {
		this.updatedby = updatedby;
	}
	public Integer getIshidden() {
		return ishidden;
	}
	public void setIshidden(Integer ishidden) {
		this.ishidden = ishidden;
	}
	public Integer getIsqc() {
		return isqc;
	}
	public void setIsqc(Integer isqc) {
		this.isqc = isqc;
	}
	public Integer getIsseleted() {
		return isseleted;
	}
	public void setIsseleted(Integer isseleted) {
		this.isseleted = isseleted;
	}
	public String getDataformat() {
		return dataformat;
	}
	public void setDataformat(String dataformat) {
		this.dataformat = dataformat;
	}
	public String getFiltervalue() {
		return filtervalue;
	}
	public void setFiltervalue(String filtervalue) {
		this.filtervalue = filtervalue;
	}
	@Override
	public String toString() {
		return "EleSetElementDef [id=" + id + ", elesetcode=" + elesetcode + ", uelecode=" + uelecode + ", celecode="
				+ celecode + ", elementname=" + elementname + ", elegroupcode=" + elegroupcode + ", descriptionchn="
				+ descriptionchn + ", descriptioneng=" + descriptioneng + ", datatype=" + datatype + ", dataunit="
				+ dataunit + ", elementrange=" + elementrange + ", specvalue=" + specvalue + ", specvaluedesc="
				+ specvaluedesc + ", isoptional=" + isoptional + ", isfilter=" + isfilter + ", orderno=" + orderno
				+ ", invalid=" + invalid + ", created=" + created + ", createdby=" + createdby + ", updated=" + updated
				+ ", updatedby=" + updatedby + ", ishidden=" + ishidden + ", isqc=" + isqc + ", isseleted=" + isseleted
				+ ", dataformat=" + dataformat + ", filtervalue=" + filtervalue + "]";
	}
	
}
