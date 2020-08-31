package com.thinkgem.jeesite.modules.access.dao;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.access.entity.AccessFunConfig;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AccessFunConfigDao extends BaseDao<AccessFunConfig> {
	public List<AccessFunConfig> findFirstItems() {
		return this.find("from AccessFunConfig where parentfuncitemid = 0 order by funcitemid");
	}

	public List<AccessFunConfig> findSubItems(String menuId) {
		// TODO Auto-generated method stub
		return this.find("from AccessFunConfig where parentfuncitemid = " + menuId + " order by funcitemid ");
	}
	
	public List<Object[]> findSubItems1(String menuId) {
		return this.findBySql("select funcitemid,funcitemname from STAT_ACCESSFUNCONFIG where parentfuncitemid = " + menuId + " order by funcitemid ");
	}
	public AccessFunConfig findAccessFunConfigByID(String id){
		List<AccessFunConfig> list = this.find("from AccessFunConfig where  funcitemid =:p1 and funcItemName is not null",new Parameter(id));
		if(list==null || list.size()==0){
			AccessFunConfig accessFunConfig=new AccessFunConfig();
			accessFunConfig.setFuncItemName(id);
			return accessFunConfig;
		}
	    return list.get(0);
		
		
	}

	public List<AccessFunConfig> findAllByPid() {
		return this.find("from AccessFunConfig where parentfuncitemid is not null order by funcitemid ");
	}

	public List<AccessFunConfig> findItems(String menuId) {
		return this.find("from AccessFunConfig where funcItemId = " + menuId+" and parentfuncitemid is not null" );
	}
}
