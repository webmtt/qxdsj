package com.thinkgem.jeesite.modules.index.dao;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.index.entity.ObservationInfo;

@Repository
public class ObservationInfoDao extends BaseDao<ObservationInfo>{

	public List<ObservationInfo> findData() {
		String sql="from ObservationInfo where invalid=0 order by orderno";
		List<ObservationInfo> list=this.find(sql);
		return list;
	}

	
	public List<ObservationInfo> getByChnname(String chnname) {
		String sql="from ObservationInfo where invalid=0 and chnname like :p1 order by orderno";
		List<ObservationInfo> list=this.find(sql, new Parameter("%"+chnname+"%"));
		return list;
	}

	public ObservationInfo getById(String id) {
		String sql="from ObservationInfo where invalid=0 and id=:p1 order by orderno";
		List<ObservationInfo> list=this.find(sql, new Parameter(id));
		return list.get(0);
	}

	public void delObservationInfo(String id) {
		String sql="delete from ObservationInfo where id=:p1 ";
		this.update(sql, new Parameter(id));
	}

	
	
}
