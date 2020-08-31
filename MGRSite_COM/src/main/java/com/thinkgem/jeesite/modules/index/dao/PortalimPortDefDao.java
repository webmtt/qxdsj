/*
 * @(#)GroundDataDao.java 2015-9-15
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.index.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.index.entity.PortalimPortDef;



/**
 * 描述：
 *
 * @author pw
 * @version 1.0 2015-9-15
 */
@Repository
public class PortalimPortDefDao extends BaseDao<PortalimPortDef>{

	//无条件查询
	public Page<PortalimPortDef> findPortalimPortDefPage(Page<PortalimPortDef> page) {
		String sql="from  PortalimPortDef order by orderno";
		return this.find(page, sql);
	}
	
	public List<PortalimPortDef> findPortalimPortDef() {
		String sql="from  PortalimPortDef order by orderno";
		return this.find(sql);
	}
	
	//有条件-模糊查询
	public Page<PortalimPortDef> getByPage(Page<PortalimPortDef> page, String chnname) {
		String sql="from PortalimPortDef where  chnname like :p1";
		return this.find(page,sql,new Parameter("%"+chnname+"%"));
	}
	
	public List<PortalimPortDef> getByChnname(String chnname) {
		String sql="from PortalimPortDef where  chnname like :p1";
		return this.find(sql,new Parameter("%"+chnname+"%"));
	}
	
	public PortalimPortDef getMaxId() {
		String sql="select max(t.recommenditemid),max(t.orderno) from  sup_portalimportdef t";
		List<Object[]> list=this.findBySql(sql);
		Integer recommenditemid=Integer.valueOf(list.get(0)[0].toString());
		Integer orderno=Integer.valueOf(list.get(0)[1].toString());
		
		PortalimPortDef p=new PortalimPortDef();
		p.setRecommenditemid(recommenditemid);
		p.setOrderno(orderno);
		
		return p;
	}

	public void delppd(Integer recommenditemid) {
		String sql="delete from PortalimPortDef t where t.recommenditemid=:p1";
		this.update(sql,new Parameter(recommenditemid));
		
	}
	
	public PortalimPortDef findPortalimPortDefById(Integer recommenditemid) {
		String sql="from PortalimPortDef t where t.recommenditemid=:p1";
		List<PortalimPortDef> list=this.find(sql, new Parameter(recommenditemid));
		return list.get(0);
	}

	public List<PortalimPortDef> getReverseData(Integer id1, Integer id2) {
		String sql="from  PortalimPortDef order by orderno";
		List<PortalimPortDef> list=this.find(sql);
		
		return list;
	}

	
}
