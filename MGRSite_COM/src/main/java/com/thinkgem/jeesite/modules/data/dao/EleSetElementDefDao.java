package com.thinkgem.jeesite.modules.data.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.data.entity.EleSetElementDef;

@Repository
public class EleSetElementDefDao extends BaseDao<EleSetElementDef>{
	public List<EleSetElementDef> getListByelesetcode(String elesetcode,String elegroupcode) {
		String sql="from EleSetElementDef where elesetcode =:p1 and elegroupcode=:p2 order by orderno";
		return this.find(sql, new Parameter(elesetcode,elegroupcode));
	}
	public List<EleSetElementDef> getListByelesetcode2(String elesetcode) {
		String sql="from EleSetElementDef where elesetcode =:p1 order by orderno";
		return this.find(sql, new Parameter(elesetcode));
	}
	public List<EleSetElementDef> getListByelesetcode(String elesetcode) {
		String sql="from EleSetElementDef where elesetcode =:p1 and invalid=0  order by orderno";
		return this.find(sql, new Parameter(elesetcode));
	}
	public Integer getMaxId() {
		String sql="select max(id) from BMD_ELESETELEMENTDEF";
		List list=this.findBySql(sql);
		Integer maxId=Integer.valueOf(list.get(0).toString());
		return maxId;
	}
	public void deleteElementDef(Integer id,Integer invalid) {
		if(invalid==0){
			String sql="update EleSetElementDef set invalid =1 where id =:p1 ";
			this.update(sql,new Parameter(id));
		}else if(invalid==1){
			String sql="update EleSetElementDef set invalid =0 where id =:p1 ";
			this.update(sql,new Parameter(id));
		}
		
	}
}
