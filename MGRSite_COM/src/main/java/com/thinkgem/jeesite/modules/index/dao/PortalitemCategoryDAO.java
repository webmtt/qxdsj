/*
 * @(#)DataCategoryDAO.java 2016年9月26日
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.index.dao;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.index.entity.PortalitemCategory;

/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016年9月26日
 */
@Repository
public class PortalitemCategoryDAO extends BaseDao<PortalitemCategory>{
	@Override
	public void save(PortalitemCategory entity) {
		super.save(entity);
	}

	public void delByPid(Integer funcitemid) {
		String sql="delete from PortalitemCategory where parentid=:p1";
		this.update(sql,new Parameter(funcitemid));
		
	}

	public List<PortalitemCategory> findByPid(Integer funcitemid) {
		String sql="from PortalitemCategory where parentid=:p1";
		List<PortalitemCategory> list=this.find(sql,new Parameter(funcitemid));
		return list;
	}

	
}
