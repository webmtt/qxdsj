package com.thinkgem.jeesite.modules.recordquery.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.recordquery.entity.DataSearchDef;
/**

 * 
 * @author zjw
 * @version 2014-10-13
 */
@Repository
public class DataSearchDefDao extends BaseDao<DataSearchDef> {
	public List<DataSearchDef> findByCode(String code){
		return this.find("from DataSearchDef where datacode=:p1",new Parameter(code));
	}
	
}
