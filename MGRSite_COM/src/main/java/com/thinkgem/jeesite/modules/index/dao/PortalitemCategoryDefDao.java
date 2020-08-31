package com.thinkgem.jeesite.modules.index.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.index.entity.PortalitemCategoryDef;

@Repository
public class PortalitemCategoryDefDao extends BaseDao<PortalitemCategoryDef>{

	public List<PortalitemCategoryDef> findAll(){
		return this.find("from PortalitemCategoryDef where funcitemid !=0 and invalid='0' order by funcitemid,orderno");
	}

	public PortalitemCategoryDef findPortalitemCategoryDefById(int id) {
		List<PortalitemCategoryDef> list=this.find("from PortalitemCategoryDef where invalid='0' and funcitemid =:p1 order by orderno",new Parameter(id));
		return list.get(0);
	}

	public void save(Integer funcitemid,String chnname, String shortchnname, String chndescription, int orderno, String linkurl,
			int invalid) {
		String sql="update PortalitemCategoryDef set chnname=:p2,shortchnname=:p3,chndescription=:p4,orderno=:p5,linkurl=:p6,invalid=:p7 where funcitemid=:p1";
		this.update(sql,new Parameter(funcitemid,chnname,shortchnname,chndescription,orderno,linkurl,invalid));
	}

	public void delById(Integer funcitemid) {
		String sql="delete from PortalitemCategoryDef where funcitemid=:p1";
		this.update(sql,new Parameter(funcitemid));
	}
	
	

	public Integer getPortalitemCategoryDefId(Integer pid) {
		String sql="select max(funcitemid) from SUP_PORTALITEMCATEGORYDEF where parentid=:p1";
		List list=this.findBySql(sql,new Parameter(pid));
		Integer funcitemid=0;
		for(int i = 0; i < list.size(); ++i){	
			if(list.get(i)!=null&&!"".equals(list.get(i))){
				funcitemid=Integer.valueOf(list.get(i).toString());										
			}else{
				funcitemid=0;
			}
		}
		return funcitemid;
	}


	public Integer getPortalitemCategoryDefLayerByPid(Integer parentid) {
		String sql="select layer from SUP_PORTALITEMCATEGORYDEF where parentid=:p1";
		List list=this.findBySql(sql,new Parameter(parentid));
		Integer layer=0;
		if(list.size()>0){
			layer=Integer.valueOf(list.get(0).toString());
		}
		return layer;
	}


	
}
