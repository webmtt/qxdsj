package com.thinkgem.jeesite.modules.distribute.dao;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.distribute.entity.DdsPushConfInfo;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class DdsPushConfInfoDao extends BaseDao<DdsPushConfInfo> {
	public List<DdsPushConfInfo> findDdsPushConfInfoList(String pushId) {
		String hql = "from DdsPushConfInfo where pushId=:p1";
		return this.find(hql,new Parameter(pushId));
	}
	public List<DdsPushConfInfo> findDdsPushConfInfoListByDataId(String dataId) {
		String hql = "from DdsPushConfInfo where dataId=:p1 and invalid=0";
		return this.find(hql,new Parameter(dataId));
	}
	public void updateByDataId(String dataId) {
		String sql = "update DdsPushConfInfo set invalid=1 where dataId=:p1 and invalid=0";
		this.update(sql,new Parameter(dataId));
	}
	public int  getMaxId() {
		String sql = "select max(pushId) from dds_pushconfinfo";
		List<Object> list=this.findBySql(sql);
		int id=0;
		for(Object o:list){
			if(o!=null){
				id=Integer.parseInt(o.toString());				
			}else{
				id=0;
			}
		}
		return id;
		
	}
	public Page<DdsPushConfInfo> getDdsPushConfInfoPage(Page<DdsPushConfInfo> page, DdsPushConfInfo ddsPushConfInfo) {
		String hql = "from DdsFtpInfo where invalid=0";
		return this.find(page,hql);
	}
	
}
