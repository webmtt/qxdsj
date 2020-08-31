/*
 * @(#)GroundDataDao.java 2015-9-15
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.index.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.index.entity.PortalImageProductDef;



/**
 * 描述：
 *
 * @author pw
 * @version 1.0 2015-9-15
 */
@Repository
public class PortalImageProductDefDao extends BaseDao<PortalImageProductDef>{
	public List<PortalImageProductDef> getPortalImageListById(int id){
		return this.find("from PortalImageProductDef where invalid = 0 and id=:p1 order by orderno",new Parameter(id));
	}
	public PortalImageProductDef getPortalImageById(int id){
		List<PortalImageProductDef> list=this.find("from PortalImageProductDef where  id=:p1 order by orderno",new Parameter(id));
		PortalImageProductDef portalImageProductDef=new PortalImageProductDef();
		if(list.size()>0){
			 return list.get(0);
		}else{
			 return portalImageProductDef;
		}
	   
	}
	public List<PortalImageProductDef> findAll(){
		return this.find("from PortalImageProductDef where invalid = 0  order by orderno");
	}
	
	public void deletePortalImageById(String id){
		String sql="update PortalImageProductDef set invalid=1 where id=:p1";
		this.update(sql,new Parameter(Integer.valueOf(id)));
	}
}
