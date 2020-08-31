package com.thinkgem.jeesite.modules.recordquery.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.modules.recordquery.entity.DataDef1;
/**

 * 卫星仪器数据管理DAO接口
 * @author gaoyw
 * @version 2014-10-13
 */
@Repository
public class DatadefDao extends BaseDao<DataDef1> {
	public List<DataDef1> findAll(){
		return this.find("from DataDef1");
	}
	
}
