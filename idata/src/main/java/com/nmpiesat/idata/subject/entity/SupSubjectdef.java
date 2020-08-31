/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.nmpiesat.idata.subject.entity;


import java.util.ArrayList;
import java.util.List;

/**
 * 专题产品Entity
 * @author yangkq
 * @version 2020-02-28
 */
public class SupSubjectdef{
	
	private static final long serialVersionUID = 1L;
	private String id;
	private String product_name;		// 产品名称
	private String description;		// 描述
	private String keyword;		// 关键字
	private String procode;//产品code
	private String kind;//要素种类
	private String type;//产品类型：1-首页显示，2-热门产品，3-特色产品
	private String smallPng;//专题产品主页图片路径和热门产品图片
	private String bigPng;//首页图片显示
	private String parent_id;		// 父节点
	private String create_by;		// 创建人
	private String create_date;		// 创建时间
	private String update_by;		// 更新人
	private String update_date;		// 更新时间
	private String remarks;		// 备注
	private String del_flag;		// 删除标记

	private List<SupSubjectdef> child=new ArrayList<>();

	public void setChild(List<SupSubjectdef> child) {
		this.child = child;
	}

	public List<SupSubjectdef> getChild() {
		return child;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setSmallPng(String smallPng) {
		this.smallPng = smallPng;
	}

	public void setBigPng(String bigPng) {
		this.bigPng = bigPng;
	}


	public String getSmallPng() {
		return smallPng;
	}

	public String getBigPng() {
		return bigPng;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getProcode() {
		return procode;
	}

	public void setProcode(String procode) {
		this.procode = procode;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}
	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public void setCreate_by(String create_by) {
		this.create_by = create_by;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public void setUpdate_by(String update_by) {
		this.update_by = update_by;
	}

	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public void setDel_flag(String del_flag) {
		this.del_flag = del_flag;
	}

	public String getId() {
		return id;
	}

	public String getProduct_name() {
		return product_name;
	}

	public String getDescription() {
		return description;
	}

	public String getKeyword() {
		return keyword;
	}


	public String getParent_id() {
		return parent_id;
	}

	public String getCreate_by() {
		return create_by;
	}

	public String getCreate_date() {
		return create_date;
	}

	public String getUpdate_by() {
		return update_by;
	}

	public String getUpdate_date() {
		return update_date;
	}

	public String getRemarks() {
		return remarks;
	}

	public String getDel_flag() {
		return del_flag;
	}
}
