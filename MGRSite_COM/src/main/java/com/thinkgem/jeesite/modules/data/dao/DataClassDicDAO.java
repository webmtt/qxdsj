/*
 * @(#)DataClassDicDAO.java 2016年9月27日
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.data.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.modules.data.entity.DataClassDic;

/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016年9月27日
 */
@Repository
public class DataClassDicDAO extends BaseDao<DataClassDic>{

	public List<DataClassDic> getDicList() {
		String sql="from DataClassDic order by orderNo";
		return this.find(sql);
	}

}
