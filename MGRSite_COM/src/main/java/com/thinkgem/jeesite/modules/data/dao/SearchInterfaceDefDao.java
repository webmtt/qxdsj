package com.thinkgem.jeesite.modules.data.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.data.entity.SearchInterfaceDef;

@Repository
public class SearchInterfaceDefDao extends BaseDao<SearchInterfaceDef>{
	public List<SearchInterfaceDef> getSearchInterfaceDefList(String interfacename) {
		if(interfacename==null||"".equals(interfacename)){
			String sql="from SearchInterfaceDef  where  invalid=0 ";
			return this.find(sql);
		}else{
			String sql="from SearchInterfaceDef  where  invalid=0 and interfacename like :p1";
			return this.find(sql,new Parameter("%"+interfacename+"%"));
		}
		
	}
	public List<SearchInterfaceDef> getSearchInterfaceDefListByCode(String interfacesetcode) {
		if(interfacesetcode==null||"".equals(interfacesetcode)){
			String sql="from SearchInterfaceDef  ";
			return this.find(sql);
		}else{
			String sql="from SearchInterfaceDef  where   interfacesetcode = :p1";
			return this.find(sql,new Parameter(interfacesetcode));
		}
		
	}
	public Integer getMaxId() {
		String sql="select max(id) from BMD_SEARCHINTERFACEDEF";
		List list=this.findBySql(sql);
		Integer maxId=Integer.valueOf(list.get(0).toString());
		return maxId;
	}
	public void delSearchInterface(Integer id) {
		String sql="update SearchInterfaceDef set invalid=1 where id=:p1";
		this.update(sql, new Parameter(id));
	}
	public List<Object[]> getTypename() {
		String sql="select INTERFACESETCODE,TYPENAME from BMD_SEARCHINTERFACEDEF GROUP BY INTERFACESETCODE,TYPENAME order by TYPENAME";
		List list=this.findBySql(sql);
		return list;
	}
	
}
