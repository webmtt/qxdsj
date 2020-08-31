package com.thinkgem.jeesite.modules.recordquery.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.recordquery.dao.DatadefDao;
import com.thinkgem.jeesite.modules.recordquery.entity.DataDef1;


/**
 * 卫星仪器数据管理Service
 * @author gaoyw
 * @version 2014-10-13
 */
@Component
public class DatadefService extends BaseService {

	@Autowired
	private DatadefDao datadefDao;
	
	public List<DataDef1> findAll() {
		return datadefDao.findAll();
	}
	
	
}
