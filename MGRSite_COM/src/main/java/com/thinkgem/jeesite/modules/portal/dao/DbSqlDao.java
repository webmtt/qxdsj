/*
 * @(#)DbSqlDao.java 2016-1-21
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.portal.dao;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.portal.entity.DsPro;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016-1-21
 */
@Repository
public class DbSqlDao extends BaseDao<DsPro> {
	@Transactional
	public void dbSql(){
		List<DsPro> list = this.find("from DsPro");
	}
	public DsPro getDsPro(String proname){
		List<DsPro> list = this.find("from DsPro where proname=:p1",new Parameter(proname));
		if(list.size()>0){
			DsPro dsPro = list.get(0);
				return dsPro;
		}else{
			return null;
		}
		
	}
}
