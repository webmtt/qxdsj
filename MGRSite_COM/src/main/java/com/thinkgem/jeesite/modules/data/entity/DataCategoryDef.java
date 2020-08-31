package com.thinkgem.jeesite.modules.data.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


/**
 * 
 * 描述：左侧资料类型菜单实体类
 *
 * @author Administrator
 * @version 1.0 2016年9月5日
 */
@Entity
@Table(name = "BMD_DATACATEGORYDEF")
public class DataCategoryDef {
	private DataCategoryDef parent;
	private Integer categoryid;
	private String chnname;
	private String shortchnname;
	private String engname;
	private String shortengname;
	private String chndescription;
	private String engdescription;
	private String imageurl;
	private String timeseq;//时间序列
	private String imagechntitle;
	private String imageengtitle;
	private Integer categorylayer;
//	private Integer parentid;
	private Integer orderno;
	private Integer invalid;
	private Integer showtype;
	private String templatefile;
	private String linkurl;
	private Integer datacount;
	private Integer showuserrankid;
	private String iconurl;
	private String largeiconurl;
	private String middleiconurl;
	
	private List<DataCategoryDef> list;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="parentid")
	public DataCategoryDef getParent() {
		return parent;
	}
	public void setParent(DataCategoryDef parent) {
		this.parent = parent;
	}
	@Id
	public Integer getCategoryid() {
		return categoryid;
	}
	public void setCategoryid(Integer categoryid) {
		this.categoryid = categoryid;
	}
	public String getChnname() {
		return chnname;
	}
	public void setChnname(String chnname) {
		this.chnname = chnname;
	}
	public String getShortchnname() {
		return shortchnname;
	}
	public void setShortchnname(String shortchnname) {
		this.shortchnname = shortchnname;
	}
	public String getEngname() {
		return engname;
	}
	public void setEngname(String engname) {
		this.engname = engname;
	}
	public String getShortengname() {
		return shortengname;
	}
	public void setShortengname(String shortengname) {
		this.shortengname = shortengname;
	}
	public String getChndescription() {
		return chndescription;
	}
	public void setChndescription(String chndescription) {
		this.chndescription = chndescription;
	}
	public String getEngdescription() {
		return engdescription;
	}
	public void setEngdescription(String engdescription) {
		this.engdescription = engdescription;
	}
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	public String getImagechntitle() {
		return imagechntitle;
	}
	public void setImagechntitle(String imagechntitle) {
		this.imagechntitle = imagechntitle;
	}
	public String getImageengtitle() {
		return imageengtitle;
	}
	public void setImageengtitle(String imageengtitle) {
		this.imageengtitle = imageengtitle;
	}
	public Integer getCategorylayer() {
		return categorylayer;
	}
	public void setCategorylayer(Integer categorylayer) {
		this.categorylayer = categorylayer;
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
	public Integer getShowtype() {
		return showtype;
	}
	public void setShowtype(Integer showtype) {
		this.showtype = showtype;
	}
	public String getTemplatefile() {
		return templatefile;
	}
	public void setTemplatefile(String templatefile) {
		this.templatefile = templatefile;
	}
	public String getLinkurl() {
		return linkurl;
	}
	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}
	public Integer getDatacount() {
		return datacount;
	}
	public void setDatacount(Integer datacount) {
		this.datacount = datacount;
	}
	public Integer getShowuserrankid() {
		return showuserrankid;
	}
	public void setShowuserrankid(Integer showuserrankid) {
		this.showuserrankid = showuserrankid;
	}
	public String getIconurl() {
		return iconurl;
	}
	public void setIconurl(String iconurl) {
		this.iconurl = iconurl;
	}
	public String getLargeiconurl() {
		return largeiconurl;
	}
	public void setLargeiconurl(String largeiconurl) {
		this.largeiconurl = largeiconurl;
	}
	public String getMiddleiconurl() {
		return middleiconurl;
	}
	public void setMiddleiconurl(String middleiconurl) {
		this.middleiconurl = middleiconurl;
	}

	public String getTimeseq() {
		return timeseq;
	}

	public void setTimeseq(String timeseq) {
		this.timeseq = timeseq;
	}

	@OneToMany(fetch=FetchType.LAZY,mappedBy="parent")
	public List<DataCategoryDef> getList() {
		return list;
	}
	public void setList(List<DataCategoryDef> list) {
		this.list = list;
	}
	@Transient
	public static void sortList(List<DataCategoryDef> list, List<DataCategoryDef> sourcelist, Integer id){
		for (int i=0; i<sourcelist.size(); i++){
			DataCategoryDef e = sourcelist.get(i);
			if (e.getParent()!=null && e.getParent().getCategoryid()!=null
					&& e.getParent().getCategoryid().equals(id)){
				list.add(e);
				// 判断是否还有子节点, 有则继续获取子节点
				for (int j=0; j<sourcelist.size(); j++){
					DataCategoryDef child = sourcelist.get(j);
					if (child.getParent()!=null && child.getParent().getCategoryid()!=null
							&& child.getParent().getCategoryid().equals(e.getCategoryid())){
						sortList(list, sourcelist, e.getCategoryid());
						break;
					}
				}
			}
		}
	}
	@Transient
	public static boolean isRoot(String id){
		return id != null && id.equals("0");
	}
	
}
