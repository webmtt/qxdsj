package com.thinkgem.jeesite.modules.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 描述：数据检索条件表
 */
@Entity
@Table(name = "BMD_SEARCHCONDDEF")
public class SearchCondDef {
	@Id
	private Integer id;//not null
	private String  searchgroupcode;//not null
	private String  searchcode;
	private String  engname;
	private String  chnname;//not null
	private String  searchtype;//not null
	private String  searchsubtype;
	private String  datasourcetype;
	private String  searchattach;
	private String  searchattach2;
	private Integer isoptional;
	private Integer isvaluelimit;
	private String  valuelimit;
	private String  valuelimitunit;
	private Integer begindatetype;
	private String  begindate;
	private Integer enddatetype;
	private String  enddate;
	private Integer minvalue;
	private Integer maxvalue;
	private Integer orderno;
	private Integer invalid;
	private Date  created;
	private String  createdby;
	private Date  updated;
	private String  updatedby;
	private String  defaultvalue;
	private Integer ishidden;
	private String  attachsearchcode;
	private String  optiondefault;
	private Integer groupdefaultsearch;
	private String  searchsubcode;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSearchgroupcode() {
		return searchgroupcode;
	}
	public void setSearchgroupcode(String searchgroupcode) {
		this.searchgroupcode = searchgroupcode;
	}
	public String getSearchcode() {
		return searchcode;
	}
	public void setSearchcode(String searchcode) {
		this.searchcode = searchcode;
	}
	public String getEngname() {
		return engname;
	}
	public void setEngname(String engname) {
		this.engname = engname;
	}
	public String getChnname() {
		return chnname;
	}
	public void setChnname(String chnname) {
		this.chnname = chnname;
	}
	public String getSearchtype() {
		return searchtype;
	}
	public void setSearchtype(String searchtype) {
		this.searchtype = searchtype;
	}
	public String getSearchsubtype() {
		return searchsubtype;
	}
	public void setSearchsubtype(String searchsubtype) {
		this.searchsubtype = searchsubtype;
	}
	public String getDatasourcetype() {
		return datasourcetype;
	}
	public void setDatasourcetype(String datasourcetype) {
		this.datasourcetype = datasourcetype;
	}
	public String getSearchattach() {
		return searchattach;
	}
	public void setSearchattach(String searchattach) {
		this.searchattach = searchattach;
	}
	public String getSearchattach2() {
		return searchattach2;
	}
	public void setSearchattach2(String searchattach2) {
		this.searchattach2 = searchattach2;
	}
	public Integer getIsoptional() {
		return isoptional;
	}
	public void setIsoptional(Integer isoptional) {
		this.isoptional = isoptional;
	}
	public Integer getIsvaluelimit() {
		return isvaluelimit;
	}
	public void setIsvaluelimit(Integer isvaluelimit) {
		this.isvaluelimit = isvaluelimit;
	}
	public String getValuelimit() {
		return valuelimit;
	}
	public void setValuelimit(String valuelimit) {
		this.valuelimit = valuelimit;
	}
	public String getValuelimitunit() {
		return valuelimitunit;
	}
	public void setValuelimitunit(String valuelimitunit) {
		this.valuelimitunit = valuelimitunit;
	}
	public Integer getBegindatetype() {
		return begindatetype;
	}
	public void setBegindatetype(Integer begindatetype) {
		this.begindatetype = begindatetype;
	}
	public String getBegindate() {
		return begindate;
	}
	public void setBegindate(String begindate) {
		this.begindate = begindate;
	}
	public Integer getEnddatetype() {
		return enddatetype;
	}
	public void setEnddatetype(Integer enddatetype) {
		this.enddatetype = enddatetype;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public Integer getMinvalue() {
		return minvalue;
	}
	public void setMinvalue(Integer minvalue) {
		this.minvalue = minvalue;
	}
	public Integer getMaxvalue() {
		return maxvalue;
	}
	public void setMaxvalue(Integer maxvalue) {
		this.maxvalue = maxvalue;
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
	public String getDefaultvalue() {
		return defaultvalue;
	}
	public void setDefaultvalue(String defaultvalue) {
		this.defaultvalue = defaultvalue;
	}
	public Integer getIshidden() {
		return ishidden;
	}
	public void setIshidden(Integer ishidden) {
		this.ishidden = ishidden;
	}
	public String getAttachsearchcode() {
		return attachsearchcode;
	}
	public void setAttachsearchcode(String attachsearchcode) {
		this.attachsearchcode = attachsearchcode;
	}
	public String getOptiondefault() {
		return optiondefault;
	}
	public void setOptiondefault(String optiondefault) {
		this.optiondefault = optiondefault;
	}
	public Integer getGroupdefaultsearch() {
		return groupdefaultsearch;
	}
	public void setGroupdefaultsearch(Integer groupdefaultsearch) {
		this.groupdefaultsearch = groupdefaultsearch;
	}
	public String getSearchsubcode() {
		return searchsubcode;
	}
	public void setSearchsubcode(String searchsubcode) {
		this.searchsubcode = searchsubcode;
	}
	@Override
	public String toString() {
		return "SearchCondDef [id=" + id + ", searchgroupcode=" + searchgroupcode + ", searchcode=" + searchcode
				+ ", engname=" + engname + ", chnname=" + chnname + ", searchtype=" + searchtype + ", searchsubtype="
				+ searchsubtype + ", datasourcetype=" + datasourcetype + ", searchattach=" + searchattach
				+ ", searchattach2=" + searchattach2 + ", isoptional=" + isoptional + ", isvaluelimit=" + isvaluelimit
				+ ", valuelimit=" + valuelimit + ", valuelimitunit=" + valuelimitunit + ", begindatetype="
				+ begindatetype + ", begindate=" + begindate + ", enddatetype=" + enddatetype + ", enddate=" + enddate
				+ ", minvalue=" + minvalue + ", maxvalue=" + maxvalue + ", orderno=" + orderno + ", invalid=" + invalid
				+ ", created=" + created + ", createdby=" + createdby + ", updated=" + updated + ", updatedby="
				+ updatedby + ", defaultvalue=" + defaultvalue + ", ishidden=" + ishidden + ", attachsearchcode="
				+ attachsearchcode + ", optiondefault=" + optiondefault + ", groupdefaultsearch=" + groupdefaultsearch
				+ ", searchsubcode=" + searchsubcode + "]";
	}
}
