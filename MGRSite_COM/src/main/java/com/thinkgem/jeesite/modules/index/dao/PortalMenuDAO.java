/*
 * @(#)DataCategoryDAO.java 2016年9月26日
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.index.dao;

import java.math.BigDecimal;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.index.entity.PortalMenu;

/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016年9月26日
 */
@Repository
public class PortalMenuDAO extends BaseDao<PortalMenu>{
	
	public void delById(Integer menuid) {
		String sql="delete from PortalMenu where menuid=:p1";
		this.update(sql,new Parameter(menuid));
		
	}
	public void delByPid(Integer menuid) {
		String sql="delete from PortalMenu where parentid=:p1";
		this.update(sql,new Parameter(menuid));
		
	}
	
}
