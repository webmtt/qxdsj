package com.thinkgem.jeesite.modules.data.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.data.entity.EleSetEleGroupDef;
import com.thinkgem.jeesite.modules.data.entity.SearchCondCfg;

@Repository
public class EleSetEleGroupDefDao extends BaseDao<EleSetEleGroupDef>{
	public List<EleSetEleGroupDef> getListByelesetcode(String elesetcode) {
		String sql="from EleSetEleGroupDef where elesetcode =:p1  order by orderno";
		return this.find(sql, new Parameter(elesetcode));
	}
	public Integer getMaxId() {
		String sql="select max(id) from BMD_ELESETELEGROUPDEF";
		List list=this.findBySql(sql);
		Integer maxId=Integer.valueOf(list.get(0).toString());
		return maxId;
	}
	public String getMaxElegroupcode(String elesetcode) {
		String sql="select max(elegroupcode) from BMD_ELESETELEGROUPDEF where elesetcode=:p1";
		List list=this.findBySql(sql,new Parameter(elesetcode));
		return String.valueOf(list.get(0).toString());
	}
	public void deleteEleGroupCode(Integer id){
		String sql="update EleSetEleGroupDef set invalid=1 where id=:p1";
		this.update(sql,new Parameter(id));
	}
}
