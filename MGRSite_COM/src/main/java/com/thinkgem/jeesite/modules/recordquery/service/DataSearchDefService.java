package com.thinkgem.jeesite.modules.recordquery.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.recordquery.dao.DataSearchDefDao;
import com.thinkgem.jeesite.modules.recordquery.entity.DataSearchDef;


/**
 * 
 * @author zjw
 * @version 2016-12-21
 */
@Component
public class DataSearchDefService extends BaseService {

	@Autowired
	private DataSearchDefDao dataSearchDefDao;
	
	public DataSearchDef findByCode(String dataCode) {
		List<DataSearchDef> list=dataSearchDefDao.findByCode(dataCode);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	
}
