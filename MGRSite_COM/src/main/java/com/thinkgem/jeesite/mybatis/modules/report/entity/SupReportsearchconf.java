/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.report.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thinkgem.jeesite.mybatis.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 报表查询参数配置Entity
 * @author yang.kq
 * @version 2019-11-21
 */
public class SupReportsearchconf extends DataEntity<SupReportsearchconf> {

	private static final long serialVersionUID = 1L;
	private String paramName;		// 参数名称
	private String paramCode;		// 参数代号
	private String paramType;//参数类型
	private SupReportsearchconf parent;		// 父节点
	public SupReportsearchconf() {
		super();
	}

	public SupReportsearchconf(String id){
		super(id);
	}

	public String getParamType() {
		return paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
	}

	@Length(min=1, max=50, message="参数名称长度必须介于 1 和 50 之间")
	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	@Length(min=0, max=50, message="查询接口长度必须介于 0 和 50 之间")
	public String getParamCode() {
		return paramCode;
	}

	public void setParamCode(String paramCode) {
		this.paramCode = paramCode;
	}

	@JsonBackReference
	@NotNull(message="父节点不能为空")
	public SupReportsearchconf getParent() {
		return parent;
	}

	public void setParent(SupReportsearchconf parent) {
		this.parent = parent;
	}
	@JsonIgnore
	public static void sortList(List<SupReportsearchconf> list, List<SupReportsearchconf> sourcelist, String parentId, boolean cascade){
		for (int i=0; i<sourcelist.size(); i++){
			SupReportsearchconf e = sourcelist.get(i);
			if (e.getParent()!=null && e.getParent().getId()!=null
					&& e.getParent().getId().equals(parentId)){
				list.add(e);
				if (cascade){
					// 判断是否还有子节点, 有则继续获取子节点
					for (int j=0; j<sourcelist.size(); j++){
						SupReportsearchconf child = sourcelist.get(j);
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

	@JsonIgnore
	public static String getRootId(){
		return "1";
	}
}