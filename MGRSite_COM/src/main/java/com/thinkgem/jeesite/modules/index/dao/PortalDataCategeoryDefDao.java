/*
 * @(#)GroundDataDao.java 2015-9-15
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.index.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.data.entity.CategoryDataRelt;
import com.thinkgem.jeesite.modules.index.entity.PortalDataCategoryDef;



/**
 * 描述：
 *
 * @author pw
 * @version 1.0 2015-9-15
 */
@Repository
public class PortalDataCategeoryDefDao extends BaseDao<PortalDataCategoryDef>{
	public List<PortalDataCategoryDef> getPortalDataCategeoryDefList(int layer){
		return this.find("from PortalDataCategoryDef where invalid = 0 and layer=:p1 order by orderno",new Parameter(layer));
	}
	public List<PortalDataCategoryDef> getPortalDataListById(int id){
		return this.find("from PortalDataCategoryDef where invalid = 0 and id=:p1 order by orderno",new Parameter(id));
	}
	public PortalDataCategoryDef getPortalDataById(int id){
		List<PortalDataCategoryDef> list=this.find("from PortalDataCategoryDef where invalid = 0 and id=:p1 order by orderno",new Parameter(id));
		PortalDataCategoryDef portalDataCategoryDef=new PortalDataCategoryDef();
		if(list.size()>0){
			 return list.get(0);
		}else{
			 return portalDataCategoryDef;
		}
	   
	}
	public List<PortalDataCategoryDef> findAll(){
		return this.find("from PortalDataCategoryDef where invalid = 0  order by orderno");
	}
	public List<PortalDataCategoryDef> findDataTwo(){
		return this.find("from PortalDataCategoryDef where invalid = 0 and Layer In(0,1)  order by orderno");
	}
	public Page<PortalDataCategoryDef> findCateGoryByPage(String id, Page<PortalDataCategoryDef> page) {
		String sql="from PortalDataCategoryDef where id=:p1";
		return this.find(page, sql, new Parameter(Integer.parseInt(id)));
	}
	public void deletePortalDataById(String id){
		String sql="update PortalDataCategoryDef set invalid=1 where id=:p1";
		this.update(sql,new Parameter(Integer.valueOf(id)));
	}
	public void deletePortalDataByPid(String pid){
		String sql="update PortalDataCategoryDef set invalid=1 where parentid=:p1";
		this.update(sql,new Parameter(Integer.valueOf(pid)));
	}
}
