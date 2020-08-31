package com.thinkgem.jeesite.modules.data.service;


import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.data.dao.DataNameDao;
import com.thinkgem.jeesite.modules.data.entity.DataCategory;
import com.thinkgem.jeesite.modules.data.entity.DataCategoryDef;
import com.thinkgem.jeesite.modules.data.entity.DataDef;

/**
 * Service
 * @author cds
 * @version 2017-10-16
 */
//@Component
@Service
@Transactional(readOnly = true)
public class DataNameService extends BaseService {

	@Autowired
	private  DataNameDao dataNameDao;
	public  List<DataCategoryDef> datalist1(Integer parentid){
		return dataNameDao.datalist1(parentid);
	}
	public List datalist2(){
		return dataNameDao.datalist2();
	}
	public List<Object[]> datalist3(){
		return dataNameDao.datalist3();
	}
	public List dataSum(){
		return dataNameDao.dataSum();
	}
	public List dataSum2(){
		return dataNameDao.dataSum2();
	}
	//julian
//	public List datalist4() {
//		return dataNameDao.datalist4();
//	}
	
	
}
