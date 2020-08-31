package com.thinkgem.jeesite.modules.index.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.index.entity.PortalMenuDef;

@Repository
public class PortalMenuDefDao extends BaseDao<PortalMenuDef>{
//排序方式portalmenu0_.MENUID || portalmenu0_.parentid,portalmenu0_.orderno

	@Override
	public List<PortalMenuDef> findAll(){
		return this.find("from PortalMenuDef where  invalid='0' order by parentid,orderno");
	}

	public PortalMenuDef findPortalMenuDefById(Integer menuid) {
		String sql="from PortalMenuDef where  invalid='0' and menuid =:p1 order by menuid||parentid,orderno";
		List<PortalMenuDef> list=this.find(sql, new Parameter(menuid));
		return list.get(0);
	}
	
	public Integer getMaxMid(Integer parentid) {
		String sql="select max(menuid) from SUP_PORTALMENUDEF where  invalid='0' and parentid =:p1 ";
//		List<PortalMenuDef> list=this.findBySql(sql, new Parameter(parentid));
		List list=this.findBySql(sql, new Parameter(parentid));
		if(list.get(0)!=null){
			return Integer.valueOf(list.get(0).toString());
		}else{
			return Integer.valueOf(parentid.toString()+"00");
		}
	}

	public Integer getMaxOrderno(Integer parentid) {
		String sql="select max(orderno) from SUP_PORTALMENUDEF where  invalid='0' and parentid =:p1 ";
		List list=this.findBySql(sql, new Parameter(parentid));
		if(list.get(0)!=null){
			return Integer.valueOf(list.get(0).toString());
		}else{
			return 0;
		}
	}

}
