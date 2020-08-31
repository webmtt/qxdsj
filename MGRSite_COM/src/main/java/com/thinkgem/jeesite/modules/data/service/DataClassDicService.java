/*
 * @(#)DataClassDicService.java 2016年9月27日
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.data.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thinkgem.jeesite.modules.data.dao.DataClassDicDAO;
import com.thinkgem.jeesite.modules.data.entity.DataClassDic;

/**
 * 描述：
 *
 * @author Administrator
 * @version 1.0 2016年9月27日
 */
@Service
public class DataClassDicService {
	@Autowired
	private DataClassDicDAO dataClassDicDao;
	public List<DataClassDic> getDicList() {
		return dataClassDicDao.getDicList();
	}

}
