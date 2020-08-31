package com.thinkgem.jeesite.modules.distribute.dao;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.distribute.entity.DdsSearchCondInfo;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class DdsSearchCondInfoDao extends BaseDao<DdsSearchCondInfo> {

	public List<DdsSearchCondInfo> findDdsSearchCondInfoList(String dataId) {
		String hql = "from DdsSearchCondInfo where dataId=:p1 and invalid=0 order by orderNo";
		return this.find(hql,new Parameter(dataId));
		
	}

	public Page<DdsSearchCondInfo> getDdsSearchCondInfoPage(Page<DdsSearchCondInfo> page,
                                                            DdsSearchCondInfo ddsSearchCondInfo) {
		String hql = "from DdsSearchCondInfo where invalid=0";
		return this.find(page,hql);
	}
	public int  getMaxId() {
		String sql = "select max(id) from dds_searchcondinfo";
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

	public void delete(int id) {
		String hql="update DdsSearchCondInfo set invalid=1 where id=:p1";
		this.update(hql,new Parameter(id));
		
	}
}
