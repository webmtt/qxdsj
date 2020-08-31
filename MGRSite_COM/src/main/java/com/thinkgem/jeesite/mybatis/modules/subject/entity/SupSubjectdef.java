/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.subject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thinkgem.jeesite.mybatis.common.persistence.DataEntity;
import com.thinkgem.jeesite.mybatis.modules.report.entity.SupReportsearchconf;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 专题产品Entity
 * @author yangkq
 * @version 2020-02-28
 */
public class SupSubjectdef extends DataEntity<SupSubjectdef> {
	
	private static final long serialVersionUID = 1L;
	private String productName;		// 产品名称
	private String description;		// 描述
	private String keyword;		// 关键字
	private String procode;//产品code
	private String kind;//要素种类
	private String type;//产品类型：1-首页显示，2-热门产品，3-特色产品
	private String smallPng;//专题产品主页图片路径和热门产品图片
	private String bigPng;//首页图片显示
	private String ispub;//是否发布：1-是，2-否
	private SupSubjectdef parent;		// 父节点
	private String oldProCode;//存储旧的产品代号
	private String oldKind;//存储旧要素种类
	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getProcode() {
		return procode;
	}

	public void setProcode(String procode) {
		this.procode = procode;
	}

	public String getIspub() {
		return ispub;
	}

	public void setIspub(String ispub) {
		this.ispub = ispub;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
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

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getOldProCode() {
		return oldProCode;
	}

	public void setOldProCode(String oldProCode) {
		this.oldProCode = oldProCode;
	}

	public String getOldKind() {
		return oldKind;
	}

	public void setOldKind(String oldKind) {
		this.oldKind = oldKind;
	}

	public SupSubjectdef() {
		super();
	}

	public SupSubjectdef(String id){
		super(id);
	}

	@Length(min=1, max=50, message="产品名称长度必须介于 1 和 50 之间")
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	@Length(min=0, max=100, message="描述长度必须介于 0 和 100 之间")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Length(min=0, max=100, message="关键字长度必须介于 0 和 100 之间")
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	@JsonBackReference
	@NotNull(message="父节点不能为空")
	public SupSubjectdef getParent() {
		return parent;
	}

	public void setParent(SupSubjectdef parent) {
		this.parent = parent;
	}
	@JsonIgnore
	public static String getRootId(){
		return "1";
	}
	@JsonIgnore
	public static void sortList(List<SupSubjectdef> list, List<SupSubjectdef> sourcelist, String parentId, boolean cascade){
		for (int i=0; i<sourcelist.size(); i++){
			SupSubjectdef e = sourcelist.get(i);
			if (e.getParent()!=null && e.getParent().getId()!=null
					&& e.getParent().getId().equals(parentId)){
				list.add(e);
				if (cascade){
					// 判断是否还有子节点, 有则继续获取子节点
					for (int j=0; j<sourcelist.size(); j++){
						SupSubjectdef child = sourcelist.get(j);
						if(child.getParent()!=null && child.getParent().getId()!=null
								&& child.getParent().getId().equals(e.getId())) {
							sortList(list, sourcelist, e.getId(), true);
							break;
						}

					}
				}
			}
		}
	}
}
